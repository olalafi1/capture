package com.naioush.capture;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import eu.long1.spacetablayout.SpaceTabLayout;

import static com.naioush.capture.Login.userKey;

public class MainActivity extends AppCompatActivity {
    SpaceTabLayout tabLayout;
FirebaseAuth mAuth;

    private SharedPreferences sp;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
String key=sp.getString("userkey",null);
if(key!=null){
    userKey=key;
    Intent i=new Intent(MainActivity.this,FirstPage.class);
    startActivity(i);
 }

    else{
    Intent i=new Intent(MainActivity.this,Login.class);
    startActivity(i);
}
    }
}