package com.heaven7.core.adapter.demo;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class TestViewPager2Activity extends TestViewPagerActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_vp2;
    }

    @Override
    protected View getPagerView() {
        ViewPager2 vp = (ViewPager2) super.getPagerView();
        vp.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        return vp;
    }
}
