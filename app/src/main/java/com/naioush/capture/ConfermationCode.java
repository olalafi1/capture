package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naioush.capture.R;

import org.jetbrains.annotations.NotNull;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConfermationCode extends AppCompatActivity {
String verificationId;
PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
PhoneAuthProvider.ForceResendingToken token;
EditText n1,n2,n3,n4,n5,n6;
TextView mTextField;
Button resendCode;
LinearLayout timerL;
Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confermation_code);
        i=getIntent();
        n1=findViewById(R.id.no1);
        n2=findViewById(R.id.no2);
        n3=findViewById(R.id.no3);
        n4=findViewById(R.id.no4);
        n5=findViewById(R.id.no5);
        n6=findViewById(R.id.no6);
        mTextField=findViewById(R.id.timer);
        resendCode=findViewById(R.id.resendCode);
        timerL=findViewById(R.id.timerL);

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
timerL.setVisibility(View.GONE);
resendCode.setVisibility(View.VISIBLE);
            }

        }.start();



        n1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                n1.requestFocus();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


      n6.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
verifyMe();
          }

          @Override
          public void afterTextChanged(Editable s) {

          }
      });
        iniCallBack();
        sendCode();

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSendCode();
            }
        });

    }

    public void iniCallBack(){

        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("Sent","Sent");

            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
Log.e("faild","Faild");
            }

            @Override
            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.e("code","Sent"+s);
verificationId=s;
token=forceResendingToken;
            }
        };
    }

    public void reSendCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+970597724423",
                60,
                TimeUnit.SECONDS,
                this,
                callbacks,token);



    }

        public void sendCode(){


        PhoneAuthProvider.getInstance().verifyPhoneNumber("+972597724423",
                60,
                TimeUnit.SECONDS,
               this,
                callbacks);











}
    String code;
    private void verifyMe() {
         code=n1.getText().toString()+n2.getText()+n3.getText()+n4.getText()+n5.getText()+n6.getText()+"";
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
signWithCredental(credential);    }

    private void signWithCredental(PhoneAuthCredential credential) {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                Log.e("userID",mAuth.getCurrentUser().getUid().toString());
            }
        });

        FirebaseDatabase FB=FirebaseDatabase.getInstance();
        DatabaseReference DBRef=FB.getReference("Users");
        Map<String,String> user=new HashMap<>();
        user.put("Name",i.getStringExtra("name"));
        user.put("countryPrefix",i.getStringExtra("prefix"));
        user.put("Mobile",i.getStringExtra("phoneNum"));
        user.put("Email",i.getStringExtra("email"));
        user.put("verifyCode",code);
        user.put("verificationId",verificationId);
        user.put("key",mAuth.getCurrentUser().getUid());
        DBRef.child(mAuth.getCurrentUser().getUid()).setValue(user);

        Toast.makeText(ConfermationCode.this,"Create Account Done Successfully",Toast.LENGTH_LONG).show();
Intent i=new Intent(ConfermationCode.this,FirstPage.class);
startActivity(i);
    }
}