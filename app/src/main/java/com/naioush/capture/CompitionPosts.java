package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class CompitionPosts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compition_posts);



        ArrayList<Post> posts=new ArrayList<>();
        ArrayList<comments> comments=new ArrayList<>();

        RecyclerView rv=findViewById(R.id.rv);
        FirebaseDatabase FBD=FirebaseDatabase.getInstance();
        DatabaseReference DBRef=FBD.getReference();
        CustomAdapter adapter=new CustomAdapter(CompitionPosts.this,posts);


        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(CompitionPosts.this));
        DBRef.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                posts.clear();
                for (DataSnapshot ds:snapshot.getChildren())
                {
                    Log.e("Value",ds.getValue().toString()+"n");
                    Post p=ds.getValue(Post.class);
                    if(p.status.equals("compitition"))
                    {
                        posts.add(p);}
                    Collections.reverse(posts);
                    adapter.notifyDataSetChanged();



                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




    }
}