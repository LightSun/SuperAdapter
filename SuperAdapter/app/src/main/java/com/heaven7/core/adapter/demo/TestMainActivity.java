package com.heaven7.core.adapter.demo;

import java.util.List;

/**
 * Created by heaven7 on 2017/12/22.
 */
public class TestMainActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(TestQtActivity.class));
        list.add(new ActivityInfo(AudioFxDemo.class));
        list.add(new ActivityInfo(TestExpendAdapterActivity.class));
        list.add(new ActivityInfo(TestCountDownActivity.class));
        list.add(new ActivityInfo(TestAdapterItemActivity.class));
        list.add(new ActivityInfo(TestMultiAdapterItemActivity.class));

        list.add(new ActivityInfo(TestViewPagerActivity.class));
        list.add(new ActivityInfo(TestViewPager2Activity.class));
    }


}
