package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.naioush.capture.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.naioush.capture.Login.userKey;

public class addPost extends AppCompatActivity {
SharedPreferences sp;
    private static final int PICK_PHOTO_FOR_AVATAR =1 ;
ImageView img;
Button addPost;
EditText titleEt;
TextView addLocation;
Switch privacyS;
String formattedDate;
RadioButton homeR,compitionR;
    Uri selectedImageUri;
    InputStream inputStream;
    String Date,title,NO_likes,Comments,photo,classification,location,privacy,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post2);
        sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
        img=findViewById(R.id.post_photo);
addPost=findViewById(R.id.post);
titleEt=findViewById(R.id.title);
addLocation=findViewById(R.id.addlocation);
privacyS=findViewById(R.id.privacy);
homeR=findViewById(R.id.home);
compitionR=findViewById(R.id.compition);
        pickImage();

        Object calendar = Calendar.getInstance();
        java.util.Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + sp.getString("userKey",null));

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
         formattedDate = df.format(c);
addPost.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Date=formattedDate;
        title=titleEt.getText().toString();
        NO_likes="0";
        Comments = null;


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference myRef = storageRef.child("images/"+selectedImageUri.getLastPathSegment());
        UploadTask uploadTask = myRef.putFile(selectedImageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return myRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    photo=downloadUri.toString();
                    Log.e("URL",downloadUri.toString()+"");





                    if(privacyS.isChecked()){
                        privacy="private";
                    }
                    else
                        privacy="public";



                    if(compitionR.isActivated()){

                        status="compition";
                    }
                    else
                        status="home";













                    addPost();

                } else {
                    // Handle failures
                    // ...
                }
            }
        });






    }
});


classification="1";




addLocation.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(addPost.this);
        alertDialog.setTitle("Add Location");
        alertDialog.setMessage("Enter Location");

        final EditText input = new EditText(addPost.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(android.R.drawable.ic_menu_mylocation);

        alertDialog.setPositiveButton("done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        input.getText();



location=input.getText().toString();
addLocation.setText(input.getText().toString());





                    }
                });

        alertDialog.setNegativeButton("الغاء",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }
});




    }


    public void addPost(){
        Log.e("user!!!",userKey);
        FirebaseDatabase FBD=FirebaseDatabase.getInstance();
        DatabaseReference DBRef=FBD.getReference("Posts");

        String postKey=DBRef.push().getKey();

        Post post=  new Post(postKey,formattedDate,title,photo,classification,location,privacy,status,userKey);

        Map<String,String> Post=new HashMap<>();
        Post.put("Date",post.Date);
        Post.put("key",post.key);
        Post.put("title",post.title);
        Post.put("photo",post.photo);
        Post.put("classification",post.classification);
        Post.put("privacy",post.privacy);
        Post.put("status",post.status);
        Post.put("location",post.location);
        Post.put("createdBy",userKey);
        DBRef.child(postKey).setValue(post);


    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                 inputStream = this.getContentResolver().openInputStream(data.getData());


                 selectedImageUri = data.getData();
                img.setImageURI(selectedImageUri);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }




}