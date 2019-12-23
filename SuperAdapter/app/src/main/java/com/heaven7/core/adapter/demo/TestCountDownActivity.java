package com.heaven7.core.adapter.demo;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.heaven7.adapter.AdapterManager;
import com.heaven7.adapter.CountDownManager;
import com.heaven7.adapter.QuickRecycleViewAdapter2;
import com.heaven7.adapter.util.ViewHelper2;
import com.heaven7.core.adapter.demo.module.SimpleCountDownItem;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.MainWorker;
import com.heaven7.java.base.util.Disposable;
import com.heaven7.java.pc.schedulers.Schedulers;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 测试倒计时
 */
public class TestCountDownActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    private static final String TAG = "TestCountDownActivity";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Random mRandom = new Random();
    private CountDownManager<SimpleCountDownItem> mCDM;
    private Adapter0 mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_count_down);
        ButterKnife.bind(this);

        setAdapter();

        mCDM = new CountDownManager<>(new Callback0(), new UpdateCallback0());
        mCDM.setScheduerCallback(new SimpleScheduler());
        mCDM.start();
    }

    private int nextValue() {
        return Math.abs(mRandom.nextInt()) + 20 * 1000;
    }

    @OnClick(R.id.bt_add)
    public void onClickAdd(){
        long val = nextValue();
        SimpleCountDownItem item = new SimpleCountDownItem(val);
        mAdapter.getAdapterManager().addItem(item);
        mCDM.addItem(item);
    }

    @OnClick(R.id.bt_remove)
    public void onClickRemove(){
        AdapterManager<SimpleCountDownItem> am = mAdapter.getAdapterManager();
        if(am.getItemSize() > 0){
            int index = mRandom.nextInt(am.getItemSize());
            SimpleCountDownItem item = am.getItemAt(index);
            Logger.d(TAG, "onClickRemove", "index = " +index);
            mCDM.removeItem(item);
        }
    }
    @OnClick(R.id.bt_remove2)
    public void onClickRemoveAll(){
        AdapterManager<SimpleCountDownItem> am = mAdapter.getAdapterManager();
        if(am.getItemSize() > 0){
            int index = mRandom.nextInt(am.getItemSize());
            SimpleCountDownItem item = am.getItemAt(index);
            Logger.d(TAG, "onClickRemoveAll", "index = " +index);
            mCDM.removeItem(item);
            am.removeItem(item);
        }
    }
    @OnClick(R.id.bt_change)
    public void onClickChange(){
        AdapterManager<SimpleCountDownItem> am = mAdapter.getAdapterManager();
        if(am.getItemSize() > 0){
            int index = mRandom.nextInt(am.getItemSize());
            SimpleCountDownItem item = am.getItemAt(index);

            long val = nextValue();
            item.setCurrentTime(val);
            Logger.d(TAG, "onClickChange", "index = " +index + ",new_Time = " + val);
        }
    }

    private void setAdapter() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(mAdapter = new Adapter0(android.R.layout.simple_list_item_1, null));
    }

    private void updateImpl(SimpleCountDownItem item, long value){
        int index = mAdapter.getAdapterManager().getItems().indexOf(item);
        if(index >= 0){
            mAdapter.getAdapterManager().notifyItemChanged(index);
        }
        System.err.println("updateImpl : " + index + " ,value = " + value);
    }

    private class Adapter0 extends QuickRecycleViewAdapter2<SimpleCountDownItem>{

        public Adapter0(int layoutId, List<SimpleCountDownItem> mDatas) {
            super(layoutId, mDatas);
        }
        @Override
        protected void onBindData(Context context, int position, SimpleCountDownItem item, int itemLayoutId, ViewHelper2 helper) {
            helper.setText(android.R.id.text1, item.getCurrentTime() / 1000 +"");
        }
    }
    private class UpdateCallback0 implements CountDownManager.UpdateCallback<SimpleCountDownItem>{
        @Override
        public void update(SimpleCountDownItem item, long value) {
             MainWorker.post(new Runnable() {
                 @Override
                 public void run() {
                     updateImpl(item, value);
                 }
             });
        }
    }

    private class Callback0 implements CountDownManager.Callback<SimpleCountDownItem>{
        @Override
        public long getCurrentTimeMillis() {
            return SystemClock.elapsedRealtime();
        }
        @Override
        public boolean isItemVisible(SimpleCountDownItem item) {
            return true;
        }
        @Override
        public long getCountDownPeriod() {
            return 1000;
        }
        @Override
        public boolean shouldEnd(long cur, long end) {
            return cur <= 0;
        }
        @Override
        public long next(SimpleCountDownItem item, long current, long end) {
            return current - getCountDownPeriod();
        }
    }
    private class SimpleScheduler implements CountDownManager.SchedulerCallback{

        private Disposable mDispose;

        @Override
        public void schedulePeriodically(Runnable task, long period) {
            mDispose = Schedulers.single()
                    .newWorker()
                    .schedulePeriodically(task, 0 , period, TimeUnit.MILLISECONDS);
        }
        @Override
        public void cancel() {
            if(mDispose != null){
                mDispose.dispose();
                mDispose = null;
            }
        }
    }
}
