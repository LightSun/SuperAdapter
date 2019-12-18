package com.heaven7.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * the selector or single or non-single mode
 * @param <T> the item
 * @since 2.0.7
 */
public class Selector<T extends ISelectable> {

    private final List<Callback<T>> mCallbacks = new ArrayList<>(3);
    private final List<T> mList = new ArrayList<>();
    /**
     * true if is single select mode. false for multi mode.
     * */
    private boolean mSingleMode;

    public Selector(){}
    public Selector(Callback<T> mCallback) {
        mCallbacks.add(mCallback);
    }

    public void addCallback(Callback<T> cb){
        if(!mCallbacks.contains(cb)){
            mCallbacks.add(cb);
        }
    }
    public void removeCallback(Callback<T> cb){
        mCallbacks.remove(cb);
    }

    public boolean isSingleMode() {
        return mSingleMode;
    }
    public void setSingleMode(boolean mSingleMode) {
        this.mSingleMode = mSingleMode;
    }
    public List<T> getSelects(){
        return mList;
    }
    public void select(T t){
        //single mode
        if(isSingleMode() && !mList.isEmpty()){
            //unselect pre
            T oldItem = mList.get(0);
            mList.clear();
            oldItem.setSelected(false);
            dispatchUnSelect(oldItem);
        }
        mList.add(t);
        t.setSelected(true);
        dispatchSelect(t);
    }

    public void unselect(T t){
        mList.remove(t);
        t.setSelected(false);
        dispatchUnSelect(t);
    }

    public boolean isSelect(T t){
        return mList.contains(t);
    }
    public void toggleSelect(T t){
        if(isSelect(t)){
            unselect(t);
        }else {
            select(t);
        }
    }
    public void initialize(List<? extends T> data) {
        mList.clear();
        for (T t : data){
            if(t.isSelected()){
                mList.add(t);
            }
        }
    }
    private void dispatchSelect(T t) {
        for (Callback<T> callback : mCallbacks){
            callback.onSelect(mList, t);
        }
    }
    private void dispatchUnSelect(T t) {
        for (Callback<T> callback : mCallbacks){
            callback.onUnselect(mList, t);
        }
    }
    public interface Callback<T extends ISelectable>{
        void onSelect(List<T> items, T item);
        void onUnselect(List<T> items, T item);
    }
}
