package com.heaven7.adapter.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.heaven7.memory.util.Cacher;

/**
 * the generic page adapter
 * @param <T> the data type
 * @since 2.1.2
 * @author heaven7
 */
public class GenericPagerAdapter<T> extends PagerAdapter implements IPageAdapter{

    private final Cacher<View, ItemViewContext> mCacher;
    private final PageDataProvider<T> mDataProvider;
    private final PageViewProvider<T> mViewProvider;
    private final boolean mLoop;

    /**
     * create page adapter by default pool size
     * @param viewProvider the view provider
     * @param dataProvider the data provider
     */
    public GenericPagerAdapter(PageDataProvider<? extends T> dataProvider, PageViewProvider<? extends T> viewProvider, boolean loop) {
        this(dataProvider, viewProvider, loop, 8);
    }
    /**
     * create page adapter by target data provider and view provider
     * @param viewProvider the view provider
     * @param dataProvider the data provider
     * @param maxPoolSize the max pool size of view
     */
    @SuppressWarnings("unchecked")
    public GenericPagerAdapter(PageDataProvider<? extends T> dataProvider, PageViewProvider<? extends T> viewProvider,
                               boolean loop, int maxPoolSize) {
        this.mDataProvider = (PageDataProvider<T>) dataProvider;
        this.mViewProvider = (PageViewProvider<T>) viewProvider;
        this.mLoop = loop;
        this.mCacher = new Cacher<View, ItemViewContext>(maxPoolSize) {
            @Override @SuppressWarnings("unchecked")
            public View create(ItemViewContext tvContext) {
                return mViewProvider.createItemView(tvContext.parent, tvContext.position,
                        tvContext.realPosition, (T)tvContext.data);
            }
        };
        onCreate(dataProvider.getContext());
    }
    public PageDataProvider<T> getDataProvider(){
        return mDataProvider;
    }
    public PageViewProvider<T> getViewProvider(){
        return mViewProvider;
    }

    @Override
    public int getCount() {
        return mLoop ? Integer.MAX_VALUE : mDataProvider.getItemCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        int index = mDataProvider.getPositionActually(position);
        T data = mDataProvider.getItem(index);
        mViewProvider.onDestroyItemView(view, position, index, data);
        container.removeView(view);
        mCacher.recycle(view);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int index = mDataProvider.getPositionActually(position);
        T data = mDataProvider.getItem(index);
        View view = mCacher.obtain(new ItemViewContext(container, position, index, data));
        container.addView(view);
        mViewProvider.onBindItemView(view, position, index, data);
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        if(mViewProvider.shouldOverridePageWidth()){
            int index = mDataProvider.getPositionActually(position);
            T data = mDataProvider.getItem(index);
            return mViewProvider.getPageWidth(position, index, data);
        }
        return super.getPageWidth(position);
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        Bundle mState = new Bundle();
        Parcelable dp = mDataProvider.saveState();
        if(dp != null){
            mState.putParcelable("dp", dp);
        }
        Parcelable vp = mViewProvider.saveState();
        if(vp != null){
            mState.putParcelable("vp", vp);
        }
        return mState;
    }
    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
        if(state == null){
            mDataProvider.restoreState(null, loader);
            mViewProvider.restoreState(null, loader);
        }else {
            Bundle mState = (Bundle) state;
            Parcelable dp = mState.getParcelable("dp");
            Parcelable vp = mState.getParcelable("vp");
            mDataProvider.restoreState(dp, loader);
            mViewProvider.restoreState(vp, loader);
        }
    }
    //called on detach/data_changed(notify/set currentItem)
   /* @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
    }*/
    @Override
    public void onCreate(Context ac) {

    }
    /**
     * often should called from {@linkplain Activity#onDestroy()}
     */
    @Override
    public void onDestroy(Context ac) {
        mDataProvider.onDestroy();
        mViewProvider.onDestroy();
    }
}
