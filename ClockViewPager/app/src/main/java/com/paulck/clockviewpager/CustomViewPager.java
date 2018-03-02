package com.paulck.clockviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by paulck on 2/3/2018.
 */

public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }


    @Override
    public boolean performClick() {
        // do what you want
        return true;
    }
}