package com.paulck.clockviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //  Field mViewScroller = null;
    public SectionsPagerAdapter mSectionsPagerAdapter;
    public ClockSectionsPagerAdapter mClockSectionsPagerAdapter;
    private static int CurrentPagePosition = 0;
    public static MainActivity mActivity;
    public ViewPager mViewPager;
    public ViewPager mClockViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;

        mSectionsPagerAdapter = null;
        mClockSectionsPagerAdapter = null;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 5);

        mClockSectionsPagerAdapter = new ClockSectionsPagerAdapter(getSupportFragmentManager(), 5);
        mClockViewPager = (ViewPager) findViewById(R.id.containertop);
        mClockViewPager.setVisibility(View.VISIBLE);
        mClockViewPager.setOffscreenPageLimit(20);
        mClockViewPager.setClipToPadding (false);
        mClockViewPager.setPageMargin(12);

        mClockViewPager.setAdapter(mClockSectionsPagerAdapter);
        mClockViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager.setCurrentItem(mClockViewPager.getCurrentItem(), false);
                }
            }


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("TT", "mClockViewPager onPageScrolled position: " + position);
                Log.d("TT", "mClockViewPager onPageScrolled positionOffset: " + positionOffset);
                Log.d("TT", "mClockViewPager onPageScrolled positionOffsetPixels: " + positionOffsetPixels);
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }

                mViewPager.scrollTo(mClockViewPager.getScrollX() +60, mViewPager.getScrollY());


            }

            @Override
            public void onPageSelected(int position) {
                Log.d("paul", "mClockViewPager onPageSelected position: " + position);
                CurrentPagePosition = position;

            }


        });


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.setOffscreenPageLimit(20);



        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;
            private int startIndicator = 0;

            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());


            @Override
            public void onPageScrollStateChanged(final int state) {

                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                         mClockViewPager.setCurrentItem(mViewPager.getCurrentItem(), false);

                }


            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("paul", "onPageScrolled position: " + position);
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }

                mClockViewPager.scrollTo((int) (mViewPager.getScrollX()), mClockViewPager.getScrollY());


            }

            @Override
            public void onPageSelected(int position) {
                Log.d("paul", "onPageSelected position: " + position);


                CurrentPagePosition = position;

            }


        });

    }


    public static class PlaceholderFragment extends Fragment {


        private int mNum;
        private static int totalPage;


        public static PlaceholderFragment newInstance(int sectionNumber, int totalsize) {
            Log.d("paul", "PlaceholderFragment newInstance sectionNumber: " + sectionNumber);

            /**  base on sectionNumber to retrieve data ***/
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("section_number", sectionNumber);
            totalPage = totalsize;
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            try {


                mNum = getArguments().getInt("section_number");


            } catch (Exception ex) {
                Log.d("paul", "restart error: " + ex);
            }

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView device_name = (TextView) rootView.findViewById(R.id.device_name);
            Log.d("paul", "PlaceholderFragment onCreateView: " + mNum);


            //    music_control_region.add((RelativeLayout) rootView.findViewById(R.id.music_control_region));


            return rootView;
        }


    }


    public static class ClockPlaceholderFragment extends Fragment {

        private int mNum;
        private static int totalPage;


        public static ClockPlaceholderFragment newInstance(int sectionNumber, int totalsize) {

            /***  base on sectionNumber to retrieve data ***/
            ClockPlaceholderFragment fragment = new ClockPlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("section_number", sectionNumber);
            totalPage = totalsize;
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            try {


                mNum = getArguments().getInt("section_number");


            } catch (Exception ex) {
                Log.d("paul", "restart error: " + ex);


            }


        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_clock, container, false);
            TextView device_name = (TextView) rootView.findViewById(R.id.device_name);
            Log.d("paul", "ClockPlaceholderFragment onCreateView: " + mNum);


            return rootView;


        }

    }


}
