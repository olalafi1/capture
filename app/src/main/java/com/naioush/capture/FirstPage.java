package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.naioush.capture.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        nv.getHeaderView(0).findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FirstPage.this,UserProfile.class);
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