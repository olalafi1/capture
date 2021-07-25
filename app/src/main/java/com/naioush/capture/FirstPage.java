package com.naioush.capture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.naioush.capture.R;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

public class FirstPage extends AppCompatActivity {
    SpaceTabLayout tabLayout;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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