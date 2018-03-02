package com.paulck.clockviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
        return MainActivity.PlaceholderFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return pageNum;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return null;
    }


}