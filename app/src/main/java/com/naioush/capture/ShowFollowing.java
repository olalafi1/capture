package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShowFollowing extends AppCompatActivity {
ArrayList<User>users;
    SharedPreferences sp ;
Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_following);

        users=new ArrayList<>();
        sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);

add=findViewById(R.id.addteam);
        RecyclerView rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        CustomAdaptershowFollowing adapter=new CustomAdaptershowFollowing(this,users);
        rv.setAdapter(adapter);

        Log.e("Following",sp.getString("userkey",""));

        FirebaseDatabase.getInstance().getReference("Users").child(sp.getString("userkey","")).child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()
                     ) {
                    Log.e("Followingkk",ds.getKey().toString());


                    FirebaseDatabase.getInstance().getReference("Users").child(ds.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                            users.add(dataSnapshot.getValue(User.class));
                            adapter.notifyDataSetChanged();
                        }


                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                        }
                    });



                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ShowFollowing.this);
                            EditText et=new EditText(ShowFollowing.this);
                            et.setHint("اسم المجموعة");



                            builder.setView(et);

                            builder.setMessage("ادخل اسم المجموعة ").setPositiveButton("تم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ArrayList<User>selecteduser=adapter.selecteduser;
                                    String key=  FirebaseDatabase.getInstance().getReference("Users")
                                            .child(sp.getString("userkey", "")).child("Teams").push().getKey();


                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(sp.getString("userkey", "")).child("Teams").child(key).child("name").setValue(et.getText().toString());

                                    for (int i=0;i<selecteduser.size();i++) {
                                        String key1=  FirebaseDatabase.getInstance().getReference("Users")
                                                .child(sp.getString("userkey", "")).child("Teams").child(key).child("member").push().getKey();
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(sp.getString("userkey", "")).child("Teams").child(key).child("member").child(key1).setValue(selecteduser.get(i).key);
                                        onBackPressed();
                                    }





                                }
                            }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
                                }
                            });
builder.show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }
}