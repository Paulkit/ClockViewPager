package com.paulck.clockviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by paulck on 30/7/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private int pageNum;

    public SectionsPagerAdapter(FragmentManager fm, int n) {
        super(fm);
        pageNum = n;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("paul", "SectionsPagerAdapter position: " + position);
        Log.d("paul", "SectionsPagerAdapter pageNum: " + pageNum);
        return MainActivity.PlaceholderFragment.newInstance(position, pageNum);
    }

    @Override
    public int getCount() {
        return pageNum;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("paul", "getPageTitle" + position);

        return null;
    }


}