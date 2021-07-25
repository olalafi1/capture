package com.naioush.capture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naioush.capture.R;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
int userSize=0;
static String userKey="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                            Map<String, String> user = (Map<String, String>) ds.getValue();

                            if ((user.get("countryPrefix") + user.get("Mobile")).equals(phone_number.getText().toString())) {
                                userKey = ds.getKey();
                                Intent i = new Intent(Login.this, FirstPage.class);
                                startActivity(i);
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
}