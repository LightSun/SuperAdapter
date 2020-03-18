package com.heaven7.adapter;

import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * the diff adapter
 * @param <T> the data type
 * @since 2.1.2
 */
public class DiffAdapter<T> extends HeaderFooterAdapter {

    private final DiffManager<T> mDiffManager;
    private final AdapterItemHelper<T> mAIH;
    private SelectCallback0 mSelectCallback;
    private Selector<ISelectable> mSelector;

    public DiffAdapter(DiffManager.ItemJudger<T> judger, BaseAdapterItem<T> item, @Nullable List<T> mDatas){
        mDiffManager = new DiffManager<T>(judger, new AdapterListUpdateCallback(this, this), mDatas);
        setCallback(new Callback() {
            @Override
            public int getActuallyItemSize() {
                return mDiffManager.getItemCount();
            }
        });
        this.mAIH = new AdapterItemHelper<>(this, item);
    }
    /**
     * set adapter item factory
     * @param factory the factory
     */
    public void setAdapterItemFactory(AdapterItemFactory<T> factory) {
        mAIH.setAdapterItemFactory(factory);
    }
    public void setSelector(Selector<ISelectable> selector) {
        if(mSelectCallback == null){
            mSelectCallback = new SelectCallback0();
        }
        if(mSelector != null){
            mSelector.removeCallback(mSelectCallback);
        }
        this.mSelector = selector;
        selector.addCallback(mSelectCallback);
    }
    public void setAsyncDiffExecutor(@Nullable Executor executor, @Nullable AsyncListDiffer.ListListener<T> listener){
        mDiffManager.setAsyncDiffExecutor(executor, listener);
    }
    public void setAsyncDiffExecutor(@Nullable Executor executor){
        mDiffManager.setAsyncDiffExecutor(executor, null);
    }
    public void commit(List<T> newList){
        mDiffManager.commit(newList);
    }
    public void commit(List<T> newList, boolean detectMove){
        mDiffManager.commit(newList, detectMove);
    }
    public void commitAsync(List<T> newList, Runnable commitCallback){
        mDiffManager.commitAsync(newList, commitCallback);
    }
    public void commitAsync(List<T> newList){
        mDiffManager.commitAsync(newList, null);
    }
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderImpl(HeaderFooterHelper hfHelper, ViewGroup parent, int viewType) {
        return mAIH.createViewHolder(hfHelper, parent, viewType);
    }
    @Override
    protected void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int position) {
        T item = mDiffManager.getItem(position);
        mAIH.bindData(holder, position, item);
    }
    @Override
    protected int getItemViewTypeImpl(HeaderFooterHelper hfHelper, int position) {
        int layoutId = mAIH.getItemLayoutId(mDiffManager.getItem(position));
        if (hfHelper != null)
            hfHelper.recordLayoutId(layoutId);
        return layoutId;
    }

    private class SelectCallback0 implements Selector.Callback<ISelectable>{
        @Override
        public void onSelect(List<ISelectable> items, ISelectable item) {
            int index = mDiffManager.getCurrentList().indexOf(item);
            if(index >= 0){
                notifyItemChanged(index + getHeaderSize());
            }
        }
        @Override
        public void onUnselect(List<ISelectable> items, ISelectable item) {
            int index = mDiffManager.getCurrentList().indexOf(item);
            if(index >= 0){
                notifyItemChanged(index + getHeaderSize());
            }
        }
    }

}
