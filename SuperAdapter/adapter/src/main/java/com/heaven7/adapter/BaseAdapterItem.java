package com.heaven7.adapter;

import android.content.Context;
import android.view.View;

/**
 * the adapter item.
 * @param <T> the data type
 * @since 2.1.1
 */
public abstract class BaseAdapterItem<T extends ISelectable> {

    private QuickRecycleViewAdapter2<T> adapter;
    private View mRootView;

    public void setAdapter(QuickRecycleViewAdapter2<T> adapter) {
        this.adapter = adapter;
    }
    public QuickRecycleViewAdapter2<T> getAdapter() {
        return adapter;
    }
    public View getRootView() {
        return mRootView;
    }
    public void setRootView(View mRootView) {
        this.mRootView = mRootView;
    }
    public AdapterManager<T> getAdapterManager(){
        return adapter.getAdapterManager();
    }
    public abstract int getLayoutId();

    public abstract void bindView(View view);

    public abstract void onBindData(Context context, final int position, final T data);
}
