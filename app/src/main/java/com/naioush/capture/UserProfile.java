package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {
Intent i;
ImageView profileImg;
Button Follow;
TextView Email,userName,NO_POST,NO_LIKES,NO_FOLLOWERS,NO_FOLLOWING;
ArrayList<Post> posts;
RecyclerView rv;
    int count=0;
    int Likescount=0;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profileImg=findViewById(R.id.profileImg);
        NO_FOLLOWERS=findViewById(R.id.Folowers);
        NO_FOLLOWING=findViewById(R.id.Following);
        sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);

        Follow=findViewById(R.id.Follow);
        Email=findViewById(R.id.email);
        NO_LIKES=findViewById(R.id.NO_LIKES);
        NO_POST=findViewById(R.id.NO_POST);
userName=findViewById(R.id.userName);
posts=new ArrayList<>();



        i=getIntent();
        String userId=i.getStringExtra("userID");




        if(sp.getString("userkey","").equals(userId)){

            Follow.setVisibility(View.GONE);
        }
try {

        FirebaseDatabase.getInstance().getReference("Users")
                .child(sp.getString("userkey","")).child("Following")
               .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.e("FFF",snapshot.getValue()+"");
try {
    if(snapshot.getValue().toString().contains(userId)){
        Follow.setVisibility(View.GONE);


    }
}catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
Follow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Followers").child(sp.getString("userkey","")).setValue("");
        FirebaseDatabase.getInstance().getReference("Users")
                .child(sp.getString("userkey","")).child("Following")
                .child(userId).setValue("");





    }
});}catch (Exception e){}


        CustomAdapter adapter=new CustomAdapter(UserProfile.this,posts);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(UserProfile.this));
        rv.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User u=snapshot.getValue(User.class);
                Picasso.get().load(Uri.parse(u.photo)).into(profileImg);
                Email.setText(u.Email);
                userName.setText(u.Name);
                Log.e("SnapShotUser", snapshot.child("Following").getChildrenCount()+"");
                Log.e("SnapShotUser", snapshot.child("Followers").getChildrenCount()+"");
                NO_FOLLOWING.setText(snapshot.child("Following").getChildrenCount()+"\nFollowing");
                NO_FOLLOWERS.setText(snapshot.child("Followers").getChildrenCount()+"\nFollowers");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference("Posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Likescount=0;

                Post p=snapshot.getValue(Post.class);
                if(p.createdBy.equals(userId)){
                    count++;

                    posts.add(p);
                    NO_POST.setText(count+"\n"+"POST");


                    FirebaseDatabase.getInstance().getReference("Posts").child("-MgK0Eqq1fjP_RK0rzDy").child("Likes").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot1, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            Likescount++;
                            NO_LIKES.setText(Likescount+"\nLike");
                            Log.e("DS@@",snapshot1.getValue()+"AA");
                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });








                }
                adapter.notifyDataSetChanged();
             /*   for (DataSnapshot ds:snapshot.getChildren()) {
                    Post p=ds.getValue(Post.class);
                    if(p.createdBy.equals(userId)){
                        count++;

                        posts.add(p);
                        NO_POST.setText(count+"\n"+"POST");


                        FirebaseDatabase.getInstance().getReference("Posts").child(ds.getKey()).child("Likes").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                Likescount+=snapshot.getChildrenCount();
                            }

                            @Override
                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });








                    }
                    adapter.notifyDataSetChanged();
                }
*/


                Log.e("DS",snapshot.getValue()+"");
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}