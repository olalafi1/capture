package com.naioush.capture;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naioush.capture.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {
    ArrayList<ArrayList<comments>> commentsArr;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ArrayList<comments>cm=new ArrayList<>();

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
        View mMainView = inflater.inflate(R.layout.fragment_home_page, container,
                false);

        return inflater.inflate(R.layout.fragment_home_page, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

commentsArr=new ArrayList<>();

        LinearLayout Elite=view.findViewById(R.id.eliet);
        LinearLayout team=view.findViewById(R.id.team);
        LinearLayout competition=view.findViewById(R.id.competition);
        TextView teamtext=view.findViewById(R.id.teamtext);
        TextView elitetext=view.findViewById(R.id.eliettext);
        TextView compitiontext=view.findViewById(R.id.competitiontext);
        Log.e(getContext().getClass().getName(),"Start");
        Elite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),EiletPeople.class);
                startActivity(intent);
                teamtext.setBackgroundResource(R.color.Primary);
                compitiontext.setBackgroundResource(R.color.Primary);

            }
        });

        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamtext.setBackgroundResource(R.drawable.selectitem);
                compitiontext.setBackgroundResource(R.color.Primary);

            }
        });



        competition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compitiontext.setBackgroundResource(R.drawable.selectitem);
                teamtext.setBackgroundResource(R.color.Primary);

            }
        });



        ArrayList<Post> posts=new ArrayList<>();
        ArrayList<comments> comments=new ArrayList<>();

        RecyclerView rv=view.findViewById(R.id.rv);
        FirebaseDatabase FBD=FirebaseDatabase.getInstance();
        DatabaseReference DBRef=FBD.getReference();
CustomAdapter adapter=new CustomAdapter(getContext(),posts);


rv.setAdapter(adapter);
rv.setLayoutManager(new LinearLayoutManager(getContext()));
DBRef.child("Posts").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
        posts.clear();
       for (DataSnapshot ds:snapshot.getChildren())
       {
        Post p=ds.getValue(Post.class);
        posts.add(p);
        adapter.notifyDataSetChanged();



       }
    }

    @Override
    public void onCancelled(@NonNull @NotNull DatabaseError error) {

    }
});




    }
}