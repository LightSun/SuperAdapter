package com.heaven7.adapter;

import java.util.ArrayList;
import java.util.List;

public class CountDownManager<T extends CountDownManager.ICountDownItem> {

    private static final boolean DEBUG = true;
    private final Callback<T> mCallback ;
    private final UpdateCallback<T> mUpdateCallback ;
    private final List<T> mItems = new ArrayList<>();

    private SchedulerCallback mScheduler;
    private final Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if(DEBUG){
                long startTime = mCallback.getCurrentTimeMillis();
                System.out.println("CountDownManager: startTime (in seconds) = " + startTime / 1000);
            }
            for(int i = 0 , size = mItems.size() ; i < size ; i ++){
                T t = mItems.get(i);
                long cur = t.getCurrentTime();
                long end = t.getEndTime();
                if(mCallback.shouldEnd(cur, end)){
                    mItems.remove(i);
                    i--;
                    size--;
                }else {
                    if(mCallback.isItemVisible(t)){
                        long value = mCallback.next(t, cur, end);
                        t.setCurrentTime(value);
                        mUpdateCallback.update(t, value);
                    }
                }
            }
        }
    };
    public CountDownManager(Callback<T> mCallback, UpdateCallback<T> updateCallback) {
        this.mCallback = mCallback;
        this.mUpdateCallback = updateCallback;
    }
    public void setScheduerCallback(SchedulerCallback callback){
        mScheduler = callback;
    }
    public void addItem(T item){
        mItems.add(item);
    }
    public void addItems(List<? extends T> items){
        mItems.addAll(items);
    }
    public void removeItem(T item){
        mItems.remove(item);
    }
    public void start() {
        mScheduler.schedulePeriodically(mTask, mCallback.getCountDownPeriod());
    }
    public void cancelAll() {
        cancelAll(true);
    }
    public void cancelAll(boolean clearItems) {
        mScheduler.cancel();
        if(clearItems){
            mItems.clear();
        }
    }
    public interface ICountDownItem {
        long getEndTime();
        long getCurrentTime();
        void setCurrentTime(long value);
    }
    public interface Callback<T extends CountDownManager.ICountDownItem>{
        long getCurrentTimeMillis();
        boolean isItemVisible(T item);
        long getCountDownPeriod(); //in millseconds
        boolean shouldEnd(long cur, long end);
        long next(T item, long current, long end);
    }
    public interface SchedulerCallback {
        void schedulePeriodically(Runnable task, long period);
        void cancel();
    }
    public interface UpdateCallback<T extends CountDownManager.ICountDownItem>{
        void update(T item, long value);
    }
}
