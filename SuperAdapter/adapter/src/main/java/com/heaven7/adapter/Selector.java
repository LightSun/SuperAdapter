package com.heaven7.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * the selector or single or non-single mode
 * @param <T> the item
 * @since 2.0.7
 */
public class Selector<T extends ISelectable> {

    private final List<Callback<T>> mCallbacks;
    private final List<T> mList = new ArrayList<>();
    /**
     * true if is single select mode. false for multi mode.
     * */
    private boolean mSingleMode;

    /**
     * create selector with target list
     * @param list the list
     * @since 2.1.2
     */
    public Selector(List<Callback<T>> list){
        this.mCallbacks = list;
    }
    public Selector(){
        this(new ArrayList<Callback<T>>(3));
    }
    public Selector(Callback<T> mCallback) {
        this();
        mCallbacks.add(mCallback);
    }

    /**
     * get callbacks
     * @return the callbacks
     * @since 2.0.9
     */
    public List<Callback<T>> getCallbacks(){
        return mCallbacks;
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
        if(mList.add(t)){
            t.setSelected(true);
            dispatchSelect(t);
        }
    }
    public void unselect(T t){
        if(mList.remove(t)){
            t.setSelected(false);
            dispatchUnSelect(t);
        }
    }

    /**
     * select without callback. unless you known the effect of call this function.
     * As i recommend you use {@linkplain #select(ISelectable)}
     * @param t the item
     * @since 2.1.2
     */
    public void selectWithoutCallback(T t){
        if(mList.add(t)){
            t.setSelected(true);
        }
    }
    /**
     * unselect without callback. unless you known the effect of call this function.
     * As i recommend you use {@linkplain #unselect(ISelectable)}
     * @param t the item
     * @since 2.1.2
     */
    public void unselectWithoutCallback(T t){
        if(mList.remove(t)){
            t.setSelected(false);
        }
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
