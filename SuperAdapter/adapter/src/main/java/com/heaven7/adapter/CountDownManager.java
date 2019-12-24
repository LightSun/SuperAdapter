package com.heaven7.adapter;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * the count down manager
 * @param <T> the count down item
 * @author heaven7
 * @since 1.1.7
 */
public class CountDownManager<T extends CountDownManager.ICountDownItem> {

    private static final boolean DEBUG = false;
    private final Callback<T> mCallback ;
    private final Updater<T> mUpdateCallback ;
    private final List<T> mItems = new ArrayList<>();

    private IScheduler mScheduler = new SimpleScheduler();
    private final Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if(DEBUG){
                long startTime = mCallback.getCurrentTimeMillis();
                System.out.println("CountDownManager: startTime (in seconds) = " + startTime / 1000);
            }
            for(int i = 0 , size = mItems.size() ; i < size ; i ++){
                T t = mItems.get(i);
                if(mCallback.shouldEnd(t)){
                    mItems.remove(i);
                    i--;
                    size--;
                }else {
                    if(mCallback.isItemVisible(t)){
                        long value = mCallback.next(t);
                        t.setCurrentTime(value);
                        mUpdateCallback.update(t, value);
                    }
                }
            }
        }
    };
    private Object mScheduleObject;


    public CountDownManager(Callback<T> mCallback, Updater<T> updater) {
        this.mCallback = mCallback;
        this.mUpdateCallback = updater;
    }
    public CountDownManager(Updater<T> updater) {
        this(new SimpleCallback<T>(), updater);
    }
    public void setScheduler(IScheduler scheduler){
        this.mScheduler = scheduler;
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
    public List<T> getItems(){
        return mItems;
    }
    public void start() {
        if(mScheduleObject != null){
            throw new IllegalStateException("can't schedule twice.");
        }
        mScheduleObject = mScheduler.schedulePeriodically(mTask, mCallback.getCountDownPeriod());
    }
    public void cancelAll() {
        cancelAll(true);
    }
    public void cancelAll(boolean clearItems) {
        mScheduler.cancel(mScheduleObject);
        mScheduleObject = null;
        if(clearItems){
            mItems.clear();
        }
    }

    /**
     * the count down item delegate
     */
    public interface ICountDownItem {
        /**
         * get the current time
         * @return the current time
         */
        long getCurrentTime();

        /**
         * set current time
         * @param value the current time
         */
        void setCurrentTime(long value);
    }

    /**
     * the config callback of count down manager
     * @param <T> the count down item type
     */
    public interface Callback<T extends CountDownManager.ICountDownItem>{
        /**
         * get current time in mills
         * @return the current time
         */
        long getCurrentTimeMillis();
        /**
         * indicate the item is visible or not.
         * @param item the item
         * @return true if visible
         */
        boolean isItemVisible(T item);
        /**
         * get the count down period
         * @return the count down period in mills
         */
        long getCountDownPeriod();
        /**
         *
         * call this to determinate the count down of target item should be end or not.
         * @param item the item
         * @return true if should end the count down for target item
         */
        boolean shouldEnd(T item);
        /**
         * get the next time. which can used to increase or decrease.
         * @param item the item
         * @return the next time of target item
         */
        long next(T item);
    }

    /**
     * the scheduler used to schedule
     */
    public interface IScheduler {
        /**
         * schedule the task periodically
         * @param task the task
         * @param period the period
         * @return the schedule result
         */
        Object schedulePeriodically(Runnable task, long period);
        /**
         * cancel schedule
         * @param result the schedule result from {@linkplain #schedulePeriodically(Runnable, long)}.
         */
        void cancel(Object result);
    }
    /**
     * the update callback
     * @param <T> the count down item
     */
    public interface Updater<T extends CountDownManager.ICountDownItem>{
        /**
         * called on update ui
         * @param item the item
         * @param value the count down time value.
         */
        void update(T item, long value);
    }

    public static class SimpleScheduler implements IScheduler{

        private ScheduledExecutorService mExecutor;

        @Override
        public Object schedulePeriodically(Runnable task, long period) {
            if(mExecutor == null){
                mExecutor = Executors.newSingleThreadScheduledExecutor();
            }
            return mExecutor.schedule(task, period, TimeUnit.MILLISECONDS);
        }

        @Override
        public void cancel(Object result) {
            if(mExecutor != null){
                mExecutor.shutdownNow();
                mExecutor = null;
            }
        }
    }
    public static class SimpleCallback<T extends ICountDownItem> implements Callback<T>{
        @Override
        public long getCurrentTimeMillis() {
            return SystemClock.elapsedRealtime();
        }
        @Override
        public boolean isItemVisible(T item) {
            return true;
        }
        @Override
        public long getCountDownPeriod() {
            return 1000;
        }
        @Override
        public boolean shouldEnd(T item) {
            return item.getCurrentTime() <= 0;
        }
        @Override
        public long next(T item) {
            return item.getCurrentTime() - getCountDownPeriod();
        }
    }
}
