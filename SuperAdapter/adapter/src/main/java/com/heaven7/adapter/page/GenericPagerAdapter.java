package com.heaven7.adapter.page;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

/**
 * the generic page adapter
 * @param <T> the data type
 * @since 2.1.2
 * @author heaven7
 */
public class GenericPagerAdapter<T> extends PagerAdapter implements IPageAdapter, BasePageProvider.PageNotifier {

    private final PageRecycler mPageRecycler;
    private final PageDataProvider<T> mDataProvider;
    private final PageViewProvider<T> mViewProvider;
    private final boolean mLoop;

    /**
     * create page adapter by default pool size
     * @param viewProvider the view provider
     * @param dataProvider the data provider
     * @param loop if true means the item count is the max of integer. false use {@linkplain PageDataProvider#getItemCount()}.
     */
    public GenericPagerAdapter(PageDataProvider<? extends T> dataProvider, PageViewProvider<? extends T> viewProvider, boolean loop) {
        this(dataProvider, viewProvider, loop, 8);
    }
    /**
     * create page adapter by target data provider and view provider
     * @param viewProvider the view provider
     * @param dataProvider the data provider
     * @param loop if true means the item count is the max of integer. false use {@linkplain PageDataProvider#getItemCount()}.
     * @param maxPoolSize the max pool size of view
     */
    @SuppressWarnings("unchecked")
    public GenericPagerAdapter(PageDataProvider<? extends T> dataProvider, PageViewProvider<? extends T> viewProvider,
                               boolean loop, int maxPoolSize) {
        dataProvider.setPageNotifier(this);
        viewProvider.setPageNotifier(this);
        this.mDataProvider = (PageDataProvider<T>) dataProvider;
        this.mViewProvider = (PageViewProvider<T>) viewProvider;
        this.mLoop = loop;
        mPageRecycler = new PageRecycler(viewProvider, maxPoolSize);
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

        ItemViewContext itemContext = mPageRecycler.obtainItemContext(container, position, index, data);
        recycle(view, itemContext);
        mPageRecycler.recycleItemContext(itemContext);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int index = mDataProvider.getPositionActually(position);
        T data = mDataProvider.getItem(index);

        ItemViewContext itemContext = mPageRecycler.obtainItemContext(container, position, index, data);
        View view = obtainItemView(itemContext);
        container.addView(view);
        mViewProvider.onBindItemView(view, position, index, data);

        mPageRecycler.recycleItemContext(itemContext);
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

    /**
     * called on obtain item view
     * @param context the item context
     * @return the item view
     */
    protected View obtainItemView(ItemViewContext context){
        return mPageRecycler.obtainItemView(context);
    }

    /**
     * recycle item view
     * @param view the view
     * @param context the item context
     */
    protected void recycle(View view, ItemViewContext context){
        mPageRecycler.recycleItemView(view, context);
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
     * often should called from 'Activity/fragment#onDestroy()'.
     */
    @Override
    public void onDestroy(Context ac) {
        mDataProvider.onDestroy();
        mViewProvider.onDestroy();
    }

}
