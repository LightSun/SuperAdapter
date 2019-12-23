package com.heaven7.core.adapter.demo.module;

import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.CountDownManager;

public class SimpleCountDownItem extends BaseSelector implements CountDownManager.ICountDownItem {

    private long due;
    private long currentTime = Long.MIN_VALUE;

    public SimpleCountDownItem(long due) {
        this.due = due;
    }
    @Override
    public long getEndTime() {
        return 0;
    }
    @Override
    public long getCurrentTime() {
        if(currentTime == Long.MIN_VALUE){
            return due;
        }
        return currentTime;
    }
    @Override
    public void setCurrentTime(long value) {
        currentTime = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleCountDownItem that = (SimpleCountDownItem) o;
        return due == that.due;
    }
    @Override
    public int hashCode() {
        return Long.valueOf(due).hashCode();
    }
}
