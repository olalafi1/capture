package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static com.naioush.capture.Login.userKey;

public class showPost extends AppCompatActivity {
    ImageView send,profileImg,postImg,comImg;
    TextView userName,title,NO_Likes,location;
    EditText commentET;
    RecyclerView commentsRv;
    LinearLayout likeLayout;
    ImageView likeHeart;
    Intent i;
    private SharedPreferences sp;
    Post p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
i=getIntent();
         sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);

        send=findViewById(R.id.send_comment);
        commentET=findViewById(R.id.commentContent);
        commentsRv=findViewById(R.id.commentsRv);
        likeHeart=findViewById(R.id.likeHeart);
        likeLayout=findViewById(R.id.likeLayer);
        NO_Likes=findViewById(R.id.NO_Likes);
        location=findViewById(R.id.location);
        profileImg=findViewById(R.id.profileImg);
        postImg=findViewById(R.id.postImg);
        comImg=findViewById(R.id.comImg);
        userName=findViewById(R.id.userName);
        title=findViewById(R.id.title);


        DatabaseReference DBRef=FirebaseDatabase.getInstance().getReference();
        DBRef.child("Posts").child(i.getStringExtra("postId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                p=snapshot.getValue(Post.class);

                try {
                    setData(p.photo,p.title);
                } catch (IOException e) {
                    e.printStackTrace();
                }

FirebaseDatabase.getInstance().getReference("Users").child(p.createdBy).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
        User u=snapshot.getValue(User.class);
        setUserData(u.Name,u.photo);
    }

    @Override
    public void onCancelled(@NonNull @NotNull DatabaseError error) {

    }
});

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });








        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                java.util.Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

                String  formattedDate = df.format(c);

                FirebaseDatabase FB=FirebaseDatabase.getInstance();
                DatabaseReference DBRef=FB.getReference("Posts");
                Map<String,String> comment=new HashMap<>();
                comment.put("Date",formattedDate);
                comment.put("createdBy",userKey);
                comment.put("postID",p.key);
                comment.put("content",commentET.getText().toString());
                String key=      DBRef.child(p.key).child("comments").push().getKey();
                DBRef.child(p.key).child("comments").child(key).setValue(comment);
                Toast.makeText(showPost.this,"Posted Successfully",Toast.LENGTH_LONG).show();
                commentET.setText("");


            }
        });





















    }
    public void setData(String postImg,String title) throws IOException {
        this.title.setText(title);

        Uri imgUri=Uri.parse(postImg);
        Picasso.get().load(imgUri.toString()).into(this.postImg);
    }

    public void setUserData(String userName,String profileImg)  {
        this.userName.setText(userName);
        Uri imgUri=Uri.parse(profileImg);
        Uri imgUri1=Uri.parse(profileImg);
        Picasso.get().load(imgUri.toString()).into(this.profileImg);

        FirebaseDatabase.getInstance().getReference().child("Users").child(sp.getString("userkey","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                User u=snapshot.getValue(User.class);
                Picasso.get().load(u.photo.toString()).into(comImg);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}