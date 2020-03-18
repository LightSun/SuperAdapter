package com.heaven7.adapter;

import android.content.Context;
import android.view.View;

/**
 * the adapter item.
 * @param <T> the data type
 * @since 2.1.1
 */
public abstract class BaseAdapterItem<T> {

    private HeaderFooterAdapter adapter;
    private View mRootView;

    public void setAdapter(HeaderFooterAdapter adapter) {
        this.adapter = adapter;
    }
    public HeaderFooterAdapter getAdapter() {
        return adapter;
    }
    public View getRootView() {
        return mRootView;
    }
    public void setRootView(View mRootView) {
        this.mRootView = mRootView;
    }
    @SuppressWarnings("unchecked")
    public DiffAdapter<T> getAsDiffAdapter(){
        return (DiffAdapter<T>) adapter;
    }
    /**
     * get the layout id
     * @return the layout id
     */
    public abstract int getLayoutId();

    /**
     * called on bind view
     * @param view the view
     */
    public abstract void bindView(View view);

    /**
     * called on bind data.
     * @param context the context
     * @param position the position
     * @param data the data
     */
    public abstract void onBindData(Context context, final int position, final T data);
}
