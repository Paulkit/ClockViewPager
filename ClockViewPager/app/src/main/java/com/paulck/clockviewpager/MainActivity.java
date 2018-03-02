package com.paulck.clockviewpager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    public View content_clock;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    public CustomViewPager mClockViewPager;
    public CustomViewPager mViewPager;
    private float WidthOffsetRatio = 0;
    private int currentOnTouching = 0; // 0 mClockViewPager, 1 mViewPager
    private float currentGetScrollX = 0f;
    private float moveBase = 0;
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        // getStatusBarHeight()
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.setScrimColor(Color.TRANSPARENT);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
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

        alphaChange();



        UI();

    }


    //TODO Nav drawer slide with alpha change

    /**
     * <h1>Nav drawer slide with alpha change</h1>
     * Apply alpha change effect in slider menu layout using navigation drawer
     * <p>
     * <b>Note:</b> Remember add   android:hardwareAccelerated="true" in application tag on AndroidManifest.xml
     *
     * @author PaulCK
     * @version 1.0
     * @since 2018-03-02
     */
    void alphaChange() {
        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.END);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                drawerView.getBackground().setAlpha((int) (slideOffset * 255));
                drawerView.setAlpha(slideOffset);
                drawer.setScrimColor(Color.argb((int) (255 * 0.2f * slideOffset), 0, 0, 0));
            }
        };
        toggle.syncState();
        drawer.addDrawerListener(toggle);


    }


    private void UI() {

        View app_bar_main = findViewById(R.id.app_bar_main);
        content_clock = app_bar_main.findViewById(R.id.content_main);
        // TODO overscroll-decor
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) content_clock);

        RelativeLayout no_clock_ui = (RelativeLayout) content_clock.findViewById(R.id.no_clock_ui);

        mClockViewPager = (CustomViewPager) content_clock.findViewById(R.id.containertop);
        mViewPager = (CustomViewPager) content_clock.findViewById(R.id.container);
        app_bar_main.setVisibility(View.VISIBLE);
        drawer.setVisibility(View.VISIBLE);
        no_clock_ui.setVisibility(View.GONE);
        HaveClockSetup();

    }

    //TODO Two ViewPagers in one activity with different swipe speed
    /**
     * <h1>Two ViewPagers in one activity with different swipe speed</h1>
     * Apply different scroll speed when swipe one of the viewpagers (top viewpager and bottom viewpager)
     * <p>
     * <b>Note:</b> it involve SectionsPagerAdapter.java , CustomViewPager.java ,  ClockSectionsPagerAdapter.java ><br/>
     *    the top viewpager use setPageMargin(marginWidth); to reduce the total width of the viewpager
     *    and increase the scroll speed on bottom viewpager by   .onTouchEvent(event);
     *    the event variable have a formula to calculate the approximately updated MotionEvent
     *    so as to achieve a different swipe speed
     * @author PaulCK
     * @version 1.0
     * @since 2018-03-03
     */
    public void HaveClockSetup() {


        mSectionsPagerAdapter = null;
        mClockSectionsPagerAdapter = null;
        int size = 5;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), size);


        mClockSectionsPagerAdapter = new ClockSectionsPagerAdapter(getSupportFragmentManager(), size);


        mClockViewPager = (CustomViewPager) content_clock.findViewById(R.id.containertop);


        mClockViewPager.setVisibility(View.VISIBLE);
        mClockViewPager.setOffscreenPageLimit(20);
        mClockViewPager.setAdapter(mClockSectionsPagerAdapter);
        mClockViewPager.setClipToPadding(false);

        //TODO leave top-margin space for  mClockViewPager
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mClockViewPager.getLayoutParams();
        lp.topMargin = getStatusBarHeight() + toolbar.getHeight();
        mClockViewPager.setLayoutParams(lp);

        for (int i = 0; i < size; i++) {

            mClockViewPager.setCurrentItem(i);
        }
        mClockViewPager.setCurrentItem(0);


        Display display = getWindowManager().getDefaultDisplay();
        Point display_size = new Point();
        display.getSize(display_size);
        int width = display_size.x;


        int marginWidth;
        if (getResources().getDisplayMetrics().density <= 0.75f) {
            marginWidth = (int) (width * 0.1) * -1;


        } else if (getResources().getDisplayMetrics().density <= 1f) {
            marginWidth = (int) (width * 0.2) * -1;


        } else if (getResources().getDisplayMetrics().density <= 1.5f) {
            marginWidth = (int) (width * 0.23) * -1;


        } else if (getResources().getDisplayMetrics().density <= 2f) {
            marginWidth = (int) (width * 0.26) * -1;


        } else if (getResources().getDisplayMetrics().density <= 3f) {
            marginWidth = (int) (width * 0.26) * -1;

        } else {
            marginWidth = (int) (width * 0.3) * -1;

        }

        mClockViewPager.setPageMargin(marginWidth);


        WidthOffsetRatio = ((float) marginWidth * -1 / (float) width);

        mClockViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrollStateChanged(final int state) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mViewPager.setCurrentItem(mClockViewPager.getCurrentItem(), false);
                }
            }


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {


                if (mViewPager.getCurrentItem() != mClockViewPager.getCurrentItem()) {
                    if (currentOnTouching == 0) {
                        mClockViewPager.setCurrentItem(mViewPager.getCurrentItem());

                    } else {
                        mViewPager.setCurrentItem(mClockViewPager.getCurrentItem());

                    }

                }

            }

        });


        mViewPager = (CustomViewPager) content_clock.findViewById(R.id.container);
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


            }

            @Override
            public void onPageSelected(int position) {


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

                    currentOnTouching = 1;


                        switch (event.getActionMasked()) {

                            case MotionEvent.ACTION_MOVE:

                                if (currentGetScrollX > event.getX()) { // drag left
                                    event2 = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX() + (moveBase - currentGetScrollX) * WidthOffsetRatio
                                            , event.getY(), event.getMetaState());
                                } else if (currentGetScrollX < event.getX()) { // drag right

                                    event2 = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX() - (currentGetScrollX - moveBase) * WidthOffsetRatio
                                            , event.getY(), event.getMetaState());
                                }

                                mClockViewPager.onTouchEvent(event2);

                                currentGetScrollX = event.getX();

                                break;

                            case MotionEvent.ACTION_DOWN:
                                currentGetScrollX = event.getX();
                                moveBase = event.getX();
                                mClockViewPager.onTouchEvent(event);
                                break;

                            case MotionEvent.ACTION_UP:

                                moveBase = 0;
                                event2 = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX()
                                        , event.getY(), event.getMetaState());
                                mClockViewPager.onTouchEvent(event2);
                                v.performClick();
                                break;
                        }




                    return false;
                }
            });

        } catch (Exception ex) {

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


                            mViewPager.onTouchEvent(event2);

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
                            v.performClick();
                            break;
                    }


                    return false;
                }
            });
        } catch (Exception ex) {
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


        public static PlaceholderFragment newInstance(int sectionNumber) {

            /*  base on sectionNumber to retrieve data */
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("section_number", sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


     


                mNum = getArguments().getInt("section_number");
 

        }


        @SuppressLint("SetTextI18n")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ((TextView) rootView.findViewById(R.id.label)).setText("" + mNum);


            return rootView;
        }


    }


    public static class ClockPlaceholderFragment extends Fragment {

        private int mNum;



        public static ClockPlaceholderFragment newInstance(int sectionNumber) {

            /*  base on sectionNumber to retrieve data */
            ClockPlaceholderFragment fragment = new ClockPlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("section_number", sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);




                mNum = getArguments().getInt("section_number");



        }


        @SuppressLint("SetTextI18n")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_clock, container, false);
            ((TextView) rootView.findViewById(R.id.clocktimeview)).setText("" + mNum);


            return rootView;


        }


    }


}
