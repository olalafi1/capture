package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naioush.capture.R;
import com.naioush.capture.chat.SendOffActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eu.long1.spacetablayout.SpaceTabLayout;

import static com.naioush.capture.Login.userKey;

public class FirstPage extends AppCompatActivity {
    SpaceTabLayout tabLayout;
SharedPreferences sp;
Intent i;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView nv=findViewById(R.id.navigation);
        Log.e("@#$%",userKey);
        sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
        i=getIntent();
        FirebaseDatabase.getInstance().getReference("Users").child(sp.getString("userkey",null)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               User u=snapshot.getValue(User.class);
                TextView name=   nv.getHeaderView(0).findViewById(R.id.name);
                TextView phone=   nv.getHeaderView(0).findViewById(R.id.phone);
                ImageView profileImg=nv.getHeaderView(0).findViewById(R.id.profileImg);
                Picasso.get().load(Uri.parse(u.photo)).into(profileImg);
                name.setText(u.Name);
                phone.setText(u.countryPrefix+u.Mobile);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        nv.getHeaderView(0).findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           Intent i=new Intent(FirstPage.this,UserProfile.class);
           i.putExtra("userID",sp.getString("userkey",""));
                startActivity(i);
            }
        });

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                if(item.getTitle().equals("Logout")){
                    sp.edit().clear().commit();
                    Intent i=new Intent(FirstPage.this,Login.class);
                    startActivity(i);
                }



                return false;
            }
        });


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                if(item.getTitle().equals("Chat")){
                    sp.edit().clear().commit();
//                    Toast.makeText(FirstPage.this, "Clicked", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(FirstPage.this, SendOffActivity.class);
                    startActivity(i);
                }



                return false;
            }
        });


        //add the fragments you want to display in a List
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomePageFragment());
        fragmentList.add(new Offers());

        fragmentList.add(new MyTeam());
        fragmentList.add(new Likes());
        fragmentList.add(new Hall());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);

        //we need the savedInstanceState to get the position
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);
        tabLayout.setTabOneIcon(R.drawable.homeee);
        tabLayout.setTabTwoIcon(R.drawable.offers1);
        tabLayout.setTabThreeIcon(R.drawable.mmmyteeam);
        tabLayout.setTabFourIcon(R.drawable.llikes);
        tabLayout.setTabFiveIcon(R.drawable.hhall);




    }


    //we need the outState to save the position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }
}