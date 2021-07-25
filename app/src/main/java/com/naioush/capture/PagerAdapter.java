package com.naioush.capture;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new HomePageFragment();
            case 1:
                return new Offers();

            case 2:
                return new MyTeam();
            case 3:
                return new Likes();
            case 4:
                return new Hall();

            default:
                return null;}
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}