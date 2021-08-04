package com.naioush.capture;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.naioush.capture.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUp extends AppCompatActivity {
    EditText prefix,name,mobile,email;
    ImageView imageView1;
    private static final int PICK_PHOTO_FOR_AVATAR =1 ;
    private InputStream inputStream;
    private Uri selectedImageUri;
    private String path;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        prefix=findViewById(R.id.prefex);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.phoneNum);
        email=findViewById(R.id.email);
        imageView1=findViewById(R.id.imageView1);
        Button loginbtn=findViewById(R.id.signup);
        sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!name.getText().toString().isEmpty()&&!mobile.getText().toString().isEmpty()
                        &&!prefix.getText().toString().isEmpty()&&!email.getText().toString().isEmpty()){

                    Intent i=new Intent(SignUp.this,ConfermationCode.class);

                    i.putExtra("name",name.getText().toString());
                    i.putExtra("phoneNum",mobile.getText().toString());
                    i.putExtra("prefix",prefix.getText().toString());
                    i.putExtra("email",email.getText().toString());
                    i.putExtra("Mobile",prefix.getText()+""+mobile.getText());
                    i.putExtra("path",path);
                    Log.e("Mobile",prefix.getText()+""+mobile.getText());
                    startActivity(i);



                }
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
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            //Display an error
            return;
        }
        try {
            inputStream = this.getContentResolver().openInputStream(data.getData());


            selectedImageUri = data.getData();
         path=   selectedImageUri.toString();
            imageView1.setImageURI(selectedImageUri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}