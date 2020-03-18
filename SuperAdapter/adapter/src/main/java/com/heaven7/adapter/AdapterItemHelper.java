package com.heaven7.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * the adapter item helper. used for {@linkplain BaseAdapterItem}.
 * @param <T> the data type
 * @since 2.1.2
 */
/*public*/ class AdapterItemHelper<T> {

    private final BaseAdapterItem<T> mBaseItem;
    private final HeaderFooterAdapter mAdapter;

    private SparseArrayCompat<BaseAdapterItem<T>> mLayoutMap;
    private SparseArrayCompat<BaseAdapterItem<T>> mIdMap;

    private AdapterItemFactory<T> mFactory;

    public AdapterItemHelper(HeaderFooterAdapter adapter,BaseAdapterItem<T> mBaseItem) {
        this.mBaseItem = mBaseItem;
        this.mAdapter = adapter;
        if(mBaseItem != null){
            mBaseItem.setAdapter(adapter);
        }
    }

    /**
     * set adapter item factory
     * @param factory the factory
     */
    public void setAdapterItemFactory(AdapterItemFactory<T> factory) {
        this.mFactory = factory;
    }

    public int getItemLayoutId(T t) {
        if(mBaseItem != null){
            return mBaseItem.getLayoutId();
        }
        if(mFactory == null){
            throw new IllegalStateException("you must call #setAdapterItemFactory(...) first!.");
        }
        String id = mFactory.getItemTypeId(t);
        BaseAdapterItem<T> item;
        if(id != null){
            if(mIdMap == null){
                mIdMap = new SparseArrayCompat<>(3);
            }
            item  = mIdMap.get(id.hashCode());
            if(item == null){
                item = mFactory.createAdapterItem(t);
                if(item == null){
                    throw new UnsupportedOperationException("create Adapter-Item failed.");
                }
                mIdMap.put(id.hashCode(), item);
            }
        }else {
            item = mFactory.createAdapterItem(t);
        }
        if(item == null){
            throw new IllegalStateException("you must provide Adapter-item by #onCreateAdapterItem(...)");
        }
        item.setAdapter(mAdapter);
        if(mLayoutMap == null){
            mLayoutMap = new SparseArrayCompat<>(3);
        }
        mLayoutMap.put(item.getLayoutId(), item);
        return item.getLayoutId();
    }
    public RecyclerView.ViewHolder createViewHolder(HeaderFooterHelper hfHelper, ViewGroup parent, int viewType) {
        if (hfHelper == null || hfHelper.isLayoutIdInRecord(viewType)) {
            BaseAdapterItem<T> item = mBaseItem != null ? mBaseItem : mLayoutMap.get(viewType);
            return new ViewHolder0(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false),
                    viewType, item);
        } else {
            //header or footer
            return new ViewHolder0(hfHelper.findView(viewType, mAdapter.getCallback().getActuallyItemSize()));
        }
    }
    @SuppressWarnings("unchecked")
    public void bindData(RecyclerView.ViewHolder holder, int position, T item) {
        ViewHolder0 vh = (ViewHolder0) holder;
        BaseAdapterItem<T> adapterItem = (BaseAdapterItem<T>) vh.mAdapterItem;
        adapterItem.bindView(holder.itemView);
        adapterItem.onBindData(vh.getContext(), position, item);
    }

    private static class ViewHolder0 extends HeaderFooterAdapter.ViewHolder {
        final BaseAdapterItem<?> mAdapterItem;
        public ViewHolder0(View itemView, int layoutId, BaseAdapterItem<?> item) {
            super(itemView, layoutId);
            this.mAdapterItem = item;
            //can't bind view (like butter-knife) here. or else will cause bug(data display un-ordered.).
            // item.bindView(itemView);
            item.setRootView(itemView);
        }
        public ViewHolder0(View itemView) {
            super(itemView);
            mAdapterItem = null;
        }
    }
}
