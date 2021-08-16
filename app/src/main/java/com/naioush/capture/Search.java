package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
EditText SearchEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchEditText=findViewById(R.id.SearchEditText);
        RecyclerView rv=findViewById(R.id.rv);

        ArrayList<User>users =new ArrayList<>();;

        CustomAdapterSearch adapter=new CustomAdapterSearch(this,users);


        rv.setAdapter(adapter);

        rv.setLayoutManager(new LinearLayoutManager(this));

        SearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("Search",SearchEditText.getText().toString());
//users.clear();
                FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren() ) {
                            User u=ds.getValue(User.class);
                            Log.e("Search",u.photo);

                            if(u.Name.contains(SearchEditText.getText().toString())){
                                Log.e("Search",u.Name);
                                users.add(u);
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });










            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}