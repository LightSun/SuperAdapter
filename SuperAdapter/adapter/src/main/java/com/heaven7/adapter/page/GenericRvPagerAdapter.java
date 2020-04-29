package com.heaven7.adapter.page;

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
    private final PageRecycler mPageRecycler;

    /**
     * create page adapter by default pool size
     * @param viewProvider the view provider
     * @param dataProvider the data provider
     * @param loop if true means the item count is the max of integer. false use {@linkplain PageDataProvider#getItemCount()}.
     */
    public GenericRvPagerAdapter(PageDataProvider<? extends T> dataProvider, final PageViewProvider<? extends T> viewProvider,
                                 boolean loop) {
        this(dataProvider, viewProvider, loop, 5);
    }
    /**
     * create page adapter by target data provider and view provider
     * @param viewProvider the view provider
     * @param dataProvider the data provider
     * @param loop if true means the item count is the max of integer. false use {@linkplain PageDataProvider#getItemCount()}.
     * @param maxPoolSize the max pool size of view
     */
    @SuppressWarnings("unchecked")
    public GenericRvPagerAdapter(PageDataProvider<? extends T> dataProvider, final PageViewProvider<? extends T> viewProvider,
                                 boolean loop, int maxPoolSize) {
        dataProvider.setPageNotifier(this);
        viewProvider.setPageNotifier(this);

        this.mDataProvider = (PageDataProvider<T>) dataProvider;
        this.mViewProvider = (PageViewProvider<T>) viewProvider;
        this.mLoop = loop;
        this.mPageRecycler = new PageRecycler(viewProvider, maxPoolSize);
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

        ItemViewContext itemContext = mPageRecycler.obtainItemContext(parent, position, realPos, item);
        View contentView = obtainItemView(itemContext);
        mPageRecycler.recycleItemContext(itemContext);

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
            ItemViewContext itemContext = mPageRecycler.obtainItemContext(parent, position, realPos, item);

            View child = parent.getChildAt(0);
            mViewProvider.onDestroyItemView(child, position, realPos, item);
            parent.removeAllViews();

            //recycle
            recycle(child, itemContext);
            mPageRecycler.recycleItemContext(itemContext);
        }
    }
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

}
