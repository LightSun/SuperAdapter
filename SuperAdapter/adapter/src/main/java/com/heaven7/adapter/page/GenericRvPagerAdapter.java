package com.heaven7.adapter.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.StatefulAdapter;

/**
 * the page adapter for ViewPager2
 * @param <T> the data type
 * @since 2.1.2
 */
public class GenericRvPagerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IPageAdapter,
        StatefulAdapter, BasePageProvider.PageNotifier {

    private final PageDataProvider<T> mDataProvider;
    private final PageViewProvider<T> mViewProvider;
    private final boolean mLoop;

    @SuppressWarnings("unchecked")
    public GenericRvPagerAdapter(PageDataProvider<? extends T> dataProvider, PageViewProvider<? extends T> viewProvider, boolean mLoop) {
        dataProvider.setPageNotifier(this);
        viewProvider.setPageNotifier(this);

        this.mDataProvider = (PageDataProvider<T>) dataProvider;
        this.mViewProvider = (PageViewProvider<T>) viewProvider;
        this.mLoop = mLoop;
        onCreate(dataProvider.getContext());
    }
    public PageDataProvider<T> getDataProvider(){
        return mDataProvider;
    }
    public PageViewProvider<T> getViewProvider(){
        return mViewProvider;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout layout = new FrameLayout(parent.getContext());
        //view pager2 require this.
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new RecyclerView.ViewHolder(layout){};
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int realPos = mDataProvider.getPositionActually(position);
        T item = mDataProvider.getItem(realPos);
        ViewGroup parent = (ViewGroup) holder.itemView;
        View contentView = mViewProvider.createItemView(parent, position, realPos, item);

        parent.removeAllViews();
        parent.addView(contentView);

        mViewProvider.onBindItemView(contentView, position, realPos, item);
    }

    @Override
    public int getItemCount() {
        return mLoop ? Integer.MAX_VALUE : mDataProvider.getItemCount();
    }
    @Override
    public int getItemViewType(int position) {
        return mDataProvider.getPositionActually(position);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ViewGroup parent = (ViewGroup) holder.itemView;
        if(parent.getChildCount() > 0){
            int position = holder.getLayoutPosition();
            int realPos = mDataProvider.getPositionActually(position);
            T item = mDataProvider.getItem(realPos);
            View child = parent.getChildAt(0);
            mViewProvider.onDestroyItemView(child, position, realPos, item);
        }
    }
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
    public void restoreState(@Nullable Parcelable state) {
        if(state == null){
            mDataProvider.restoreState(null, null);
            mViewProvider.restoreState(null, null);
        }else {
            Bundle mState = (Bundle) state;
            Parcelable dp = mState.getParcelable("dp");
            Parcelable vp = mState.getParcelable("vp");
            mDataProvider.restoreState(dp, null);
            mViewProvider.restoreState(vp, null);
        }
    }
}
