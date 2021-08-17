package com.naioush.capture;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naioush.capture.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Likes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Likes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Likes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Likes.
     */
    // TODO: Rename and change types and number of parameters
    public static Likes newInstance(String param1, String param2) {
        Likes fragment = new Likes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_likes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        ArrayList<LikeNotification> Likes=new ArrayList<>();
        ArrayList<comments> comments=new ArrayList<>();

        RecyclerView rv=view.findViewById(R.id.rv);
        FirebaseDatabase FBD=FirebaseDatabase.getInstance();
        DatabaseReference DBRef=FBD.getReference();
        LikesCustomAdapter adapter=new LikesCustomAdapter(getContext(),Likes);


        rv.setAdapter(adapter);
        SharedPreferences sp = getContext().getSharedPreferences("loginSaved", Context.MODE_PRIVATE);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        DBRef.child("Notification").child(sp.getString("userkey","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Likes.clear();
                for (DataSnapshot ds:snapshot.getChildren())
                {
                 try {
                     Log.e("Value", ds.getValue().toString() + "n");

                     LikeNotification l = ds.getValue(LikeNotification.class);
                     if (l.status.equals("show")) {
                         Likes.add(l);


                     }
                     Collections.reverse(Likes);
                     adapter.notifyDataSetChanged();


                 }catch (Exception e)
                 {}                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




    }
}