package com.heaven7.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.heaven7.adapter.util.ViewHelper2;

import java.util.List;

/**
 * the adapter which use {@linkplain BaseAdapterItem} style to do with RecyclerView Adapter.
 * @param <T> the data type
 * @since 2.1.1
 * @see #setAdapterItemFactory(AdapterItemFactory)
 * @see BaseAdapterItem
 */
public class QuickRecyclerViewAdapter3<T extends ISelectable> extends QuickRecycleViewAdapter2<T> {

    private final AdapterItemHelper<T> mAIH;

    public QuickRecyclerViewAdapter3(List<T> mDatas) {
        this(null, mDatas);
    }
    public QuickRecyclerViewAdapter3(BaseAdapterItem<T> item, List<T> mDatas) {
        this(item, mDatas, ISelectable.SELECT_MODE_SINGLE);
    }
    public QuickRecyclerViewAdapter3(BaseAdapterItem<T> item, List<T> mDatas, int selectMode) {
        super(0, mDatas, selectMode);
        mAIH = new AdapterItemHelper<>(this, item);
    }
    public QuickRecyclerViewAdapter3(BaseAdapterItem<T> item, List<T> mDatas, Selector<T> selector) {
        super(0, mDatas, selector);
        mAIH = new AdapterItemHelper<>(this, item);
    }
    /**
     * set adapter item factory
     * @param factory the factory
     */
    public void setAdapterItemFactory(AdapterItemFactory<T> factory) {
        mAIH.setAdapterItemFactory(factory);
    }
    @Override
    protected final int getItemLayoutId(int position, T t) {
        return mAIH.getItemLayoutId(t);
    }
    @Override
    protected final RecyclerView.ViewHolder onCreateViewHolderImpl(HeaderFooterHelper hfHelper, ViewGroup parent, int viewType) {
        return mAIH.createViewHolder(hfHelper, parent, viewType);
    }
    @Override
    protected void onBindDataImpl(RecyclerView.ViewHolder holder, int position, T item) {
        mAIH.bindData(holder, position, item);
    }

    @Override
    public final void setItemTypeDelegate(ItemTypeDelegate<T> mTypeDelegate) {
        throw new UnsupportedOperationException("use BaseAdapterItem instead.");
    }
    @Override
    protected final void onBindData(Context context, int position, T item, int itemLayoutId, ViewHelper2 helper) {
        throw new UnsupportedOperationException("use BaseAdapterItem instead.");
    }
}
