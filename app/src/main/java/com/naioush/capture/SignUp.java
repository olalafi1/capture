package com.naioush.capture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.naioush.capture.R;

public class SignUp extends AppCompatActivity {
    EditText prefix,name,mobile,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        prefix=findViewById(R.id.prefex);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.phoneNum);
        email=findViewById(R.id.email);
        Button loginbtn=findViewById(R.id.signup);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(SignUp.this,ConfermationCode.class);

                i.putExtra("name",name.getText().toString());
                i.putExtra("phoneNum",mobile.getText().toString());
                i.putExtra("prefix",prefix.getText().toString());
                i.putExtra("email",email.getText().toString());
                Log.e("Mobile",prefix.getText()+""+mobile.getText());

                if(!name.getText().toString().isEmpty()&&!mobile.getText().toString().isEmpty()
                        &&!prefix.getText().toString().isEmpty()&&!email.getText().toString().isEmpty())
                    startActivity(i);
                else
                    Toast.makeText(SignUp.this,"please fill all records",Toast.LENGTH_LONG).show();
            }
        });

        TextView signup=findViewById(R.id.login);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignUp.this,Login.class);
                startActivity(i);
            }
        });
    }
}