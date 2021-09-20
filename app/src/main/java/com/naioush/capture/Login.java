package com.naioush.capture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.naioush.capture.R;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
int userSize=0;
public static String userKey;
    SharedPreferences sp ;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
        editor = sp.edit();

        Button loginbtn=findViewById(R.id.loginbtn);
        EditText phone_number=findViewById(R.id.phone_number);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        userSize= (int) snapshot.getChildrenCount();
                        int count=0;

                        for (DataSnapshot ds:snapshot.getChildren()) {
                            Log.e("Data",ds.getValue().toString());

                            User user = (User) ds.getValue(User.class);
                            if ((user.countryPrefix + user.Mobile).equals(phone_number.getText().toString())) {
                                userKey = ds.getKey();
                                Log.e("Key",userKey);
sp.edit().putString("userkey",userKey).apply();
sp.edit().commit();

                                Log.e("Key",sp.getString("userkey",""));
                                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(user.verificationId,user.verifyCode);
                                signWithCredental(credential);

                            }

                            if (count == userSize) {
                                Toast.makeText(Login.this, "there is no user with this number", Toast.LENGTH_LONG).show();

                            }
                            count++;

                        }}

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }



                });





















            }
        });

        TextView signup=findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,SignUp.class);
                startActivity(i);
            }
        });
    }

    private void signWithCredental(PhoneAuthCredential credential) {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                Intent i = new Intent(Login.this, FirstPage.class);
                startActivity(i);
                Toast.makeText(Login.this,"تم تسجيل الدخول ",Toast.LENGTH_LONG).show();
            }
        });}

}