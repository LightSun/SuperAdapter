package com.heaven7.adapter.page;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * the page adapter for ViewPager2
 * @param <T> the data type
 */
public class GenericRvPagerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final PageDataProvider<T> mDataProvider;
    private final PageViewProvider<T> mViewProvider;

    public GenericRvPagerAdapter(PageDataProvider<T> dataProvider, PageViewProvider<T> viewProvider) {
        this.mDataProvider = dataProvider;
        this.mViewProvider = viewProvider;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout layout = new FrameLayout(parent.getContext());
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
        return mDataProvider.getItemCount();
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
}
