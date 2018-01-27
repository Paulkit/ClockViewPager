package com.paulck.clockviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by paulck on 10/8/2017.
 */


public class ClockSectionsPagerAdapter extends FragmentPagerAdapter {
    private int pageNum;

    public ClockSectionsPagerAdapter(FragmentManager fm, int n) {
        super(fm);
        pageNum = n;
    }

    @Override
    public Fragment getItem(int position) {
        return MainActivity.ClockPlaceholderFragment.newInstance(position, pageNum);
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

