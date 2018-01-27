package com.paulck.clockviewpager;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends AppCompatActivity {

    public SectionsPagerAdapter mSectionsPagerAdapter;
    public ClockSectionsPagerAdapter mClockSectionsPagerAdapter;
    private static int CurrentPagePosition = 0;
    public View content_clock;
    private View headerLayout;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View app_bar_main;
    private RelativeLayout no_clock_ui;
    private int size = 5;
    public ViewPager mClockViewPager;
    private int MarginWidth;
    public ViewPager mViewPager;
    private float WidthOffsetRatio = 0;
    private int currentOnTouching = 0; // 0 mClockViewPager, 1 mViewPager
    private float currentGetScrollX = 0f;
    private float moveBase = 0;
    private int width;
    private MotionEvent event2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();

            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // getStatusBarHeight()
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.setScrimColor(Color.TRANSPARENT);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });


        (findViewById(R.id.toolbar_layout)).bringToFront();


        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.END);


        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                drawerView.getBackground().setAlpha((int) (slideOffset * 255));
                drawerView.setAlpha(slideOffset);
                drawer.setScrimColor(Color.argb((int) (255 * 0.2f * slideOffset), 0, 0, 0));
            }
        };
        toggle.syncState();
        drawer.setDrawerListener(toggle);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        UI();

    }


    private void UI() {
        headerLayout = navigationView.getHeaderView(0);
        app_bar_main = findViewById(R.id.app_bar_main);
        content_clock = app_bar_main.findViewById(R.id.content_main);
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) content_clock);

        no_clock_ui = (RelativeLayout) content_clock.findViewById(R.id.no_clock_ui);

        mClockViewPager = (ViewPager) content_clock.findViewById(R.id.containertop);
        mViewPager = (ViewPager) content_clock.findViewById(R.id.container);
        app_bar_main.setVisibility(View.VISIBLE);
        drawer.setVisibility(View.VISIBLE);
        no_clock_ui.setVisibility(View.GONE);
        HaveClockSetup();

    }


    public void HaveClockSetup() {


        mSectionsPagerAdapter = null;
        mClockSectionsPagerAdapter = null;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), size);


        mClockSectionsPagerAdapter = new ClockSectionsPagerAdapter(getSupportFragmentManager(), size);


        mClockViewPager = (ViewPager) content_clock.findViewById(R.id.containertop);


        mClockViewPager.setVisibility(View.VISIBLE);
        mClockViewPager.setOffscreenPageLimit(20);
        mClockViewPager.setAdapter(mClockSectionsPagerAdapter);
        mClockViewPager.setClipToPadding(false);

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mClockViewPager.getLayoutParams();
        lp.topMargin = getStatusBarHeight() + toolbar.getHeight();
        mClockViewPager.setLayoutParams(lp);

        for (int i = 0; i < size; i++) {

            mClockViewPager.setCurrentItem(i);
        }
        mClockViewPager.setCurrentItem(0);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;


        if (getResources().getDisplayMetrics().density <= 0.75f) {
            MarginWidth = (int) (width * 0.1) * -1;


        } else if (getResources().getDisplayMetrics().density <= 1f) {
            MarginWidth = (int) (width * 0.2) * -1;


        } else if (getResources().getDisplayMetrics().density <= 1.5f) {
            MarginWidth = (int) (width * 0.23) * -1;


        } else if (getResources().getDisplayMetrics().density <= 2f) {
            MarginWidth = (int) (width * 0.26) * -1;


        } else if (getResources().getDisplayMetrics().density <= 3f) {
            MarginWidth = (int) (width * 0.26) * -1;

        } else {
            MarginWidth = (int) (width * 0.3) * -1;

        }

        mClockViewPager.setPageMargin(MarginWidth);


        WidthOffsetRatio = ((float) MarginWidth * -1 / (float) width);

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


                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }


            }

            @Override
            public void onPageSelected(int position) {

                CurrentPagePosition = position;
                if (mViewPager.getCurrentItem() != mClockViewPager.getCurrentItem()) {
                    if (currentOnTouching == 0) {
                        mClockViewPager.setCurrentItem(mViewPager.getCurrentItem());

                    } else {
                        mViewPager.setCurrentItem(mClockViewPager.getCurrentItem());

                    }

                }


            }


        });


        mViewPager = (ViewPager) content_clock.findViewById(R.id.container);
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.setOffscreenPageLimit(20);
        mViewPager.setClipToPadding(false);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrollStateChanged(final int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("paul", "onPageScrolled position: " + position);

            }

            @Override
            public void onPageSelected(int position) {


                CurrentPagePosition = position;

                if (mViewPager.getCurrentItem() != mClockViewPager.getCurrentItem()) {
                    if (currentOnTouching == 0) {
                        mClockViewPager.setCurrentItem(mViewPager.getCurrentItem());

                    } else {
                        mViewPager.setCurrentItem(mClockViewPager.getCurrentItem());

                    }

                }

            }


        });

        try {
            mViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Log.d("paul", "mViewPager cuurent: " + event.getActionMasked());
                    currentOnTouching = 1;

                    try {
                        switch (event.getActionMasked()) {

                            case MotionEvent.ACTION_MOVE:
                                Log.d("paul", "ACTION_MOVE");

                                if (currentGetScrollX > event.getX()) { // drag left
                                    event2 = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX() + (moveBase - currentGetScrollX) * WidthOffsetRatio
                                            , event.getY(), event.getMetaState());
                                } else if (currentGetScrollX < event.getX()) { // drag right

                                    event2 = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX() - (currentGetScrollX - moveBase) * WidthOffsetRatio
                                            , event.getY(), event.getMetaState());
                                }
                                try {
                                    mClockViewPager.onTouchEvent(event2);
                                } catch (Exception ex) {
                                    Log.d("paul", "MotionEvent Exception: " + ex);

                                }
                                currentGetScrollX = event.getX();

                                break;

                            case MotionEvent.ACTION_DOWN:
                                Log.d("paul", "ACTION_DOWN");
                                currentGetScrollX = event.getX();
                                moveBase = event.getX();
                                mClockViewPager.onTouchEvent(event);
                                break;

                            case MotionEvent.ACTION_UP:
                                Log.d("paul", "ACTION_UP");

                                moveBase = 0;
                                event2 = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX()
                                        , event.getY(), event.getMetaState());
                                mClockViewPager.onTouchEvent(event2);
                                break;
                        }

                    } catch (Exception ex) {
                        Log.d("paul", "mViewPager.setOnTouchListener ex: " + ex);

                    }
                    Log.d("paul", "event.getX()" + event.getX());


                    return false;
                }
            });

        } catch (Exception ex) {
            Log.d("paul", "mViewPager.setOnTouchListener: " + ex);

            currentOnTouching = 0;

        }

        try {
            mClockViewPager.setOnTouchListener(new View.OnTouchListener() {


                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    currentOnTouching = 0;

                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_MOVE:


                            if (currentGetScrollX > event.getX()) { // drag left
                                event2 = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX() - (moveBase - currentGetScrollX) * WidthOffsetRatio
                                        , event.getY(), event.getMetaState());

                            } else if (currentGetScrollX < event.getX()) { // drag right

                                event2 = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), (currentGetScrollX - moveBase) * WidthOffsetRatio + event.getX()
                                        , event.getY(), event.getMetaState());
                            }

                            try {
                                mViewPager.onTouchEvent(event2);
                            } catch (Exception ex) {
                                Log.d("paul", "MotionEvent Exception: " + ex);

                            }
                            currentGetScrollX = event.getX();


                            break;

                        case MotionEvent.ACTION_DOWN:

                            currentGetScrollX = event.getX();

                            moveBase = event.getX();
                            mViewPager.onTouchEvent(event);
                            break;

                        case MotionEvent.ACTION_UP:

                            moveBase = 0;
                            event2 = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX()
                                    , event.getY(), event.getMetaState());
                            mViewPager.onTouchEvent(event2);
                            break;
                    }


                    Log.d("paul", "event.getX()" + event.getX());


                    return false;
                }
            });
        } catch (Exception ex) {
            Log.d("paul", "mClockViewPager.setOnTouchListener: " + ex);
            currentOnTouching = 0;


        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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

            ((TextView) rootView.findViewById(R.id.label)).setText("" + mNum);

            Log.d("paul", "PlaceholderFragment onCreateView: " + mNum);


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
            ((TextView) rootView.findViewById(R.id.clocktimeview)).setText("" + mNum);

            Log.d("paul", "ClockPlaceholderFragment onCreateView: " + mNum);


            return rootView;


        }


    }


}
