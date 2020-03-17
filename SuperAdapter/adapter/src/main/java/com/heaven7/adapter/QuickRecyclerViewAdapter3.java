package com.heaven7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.heaven7.adapter.util.ViewHelper2;

import java.util.List;

/**
 * the adapter just wrap {@linkplain BaseAdapterItem}. help to fast move old codes.
 * @param <T> the data type
 * @since 2.1.1
 */
public class QuickRecyclerViewAdapter3<T extends ISelectable> extends QuickRecycleViewAdapter2<T> {

    private final BaseAdapterItem<T> mBaseItem;
    private SparseArrayCompat<BaseAdapterItem<T>> mLayoutMap;
    private SparseArrayCompat<BaseAdapterItem<T>> mIdMap;

    private AdapterItemFactory<T> mFactory;

    public QuickRecyclerViewAdapter3(List<T> mDatas) {
        this(null, mDatas);
    }
    public QuickRecyclerViewAdapter3(BaseAdapterItem<T> item, List<T> mDatas) {
        this(item, mDatas, ISelectable.SELECT_MODE_SINGLE);
    }
    public QuickRecyclerViewAdapter3(BaseAdapterItem<T> item, List<T> mDatas, int selectMode) {
        super(0, mDatas, selectMode);
        this.mBaseItem = item;
        if(item != null){
            item.setAdapter(this);
        }
    }
    public QuickRecyclerViewAdapter3(BaseAdapterItem<T> item, List<T> mDatas, Selector<T> selector) {
        super(0, mDatas, selector);
        this.mBaseItem = item;
        if(item != null){
            item.setAdapter(this);
        }
    }
    @Override
    protected final int getItemLayoutId(int position, T t) {
        if(mBaseItem != null){
            return mBaseItem.getLayoutId();
        }
        String id = mFactory.getItemTypeId(t);
        BaseAdapterItem<T> item;
        if(id != null){
            if(mIdMap == null){
                mIdMap = new SparseArrayCompat<>(3);
            }
            item = mIdMap.get(id.hashCode());
            if(item == null){
                item = mFactory.createAdapterItem(t);
                if(item == null){
                    throw new UnsupportedOperationException("");
                }
                mIdMap.put(id.hashCode(), item);
            }
        }else {
            item = mFactory.createAdapterItem(t);
        }
        if(item == null){
            throw new IllegalStateException("you must provide Adapter item by #onCreateAdapterItem(...)");
        }
        item.setAdapter(this);
        if(mLayoutMap == null){
            mLayoutMap = new SparseArrayCompat<>(3);
        }
        mLayoutMap.put(item.getLayoutId(), item);
        return item.getLayoutId();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderImpl(HeaderFooterHelper hfHelper, ViewGroup parent, int viewType) {
        if (hfHelper == null || hfHelper.isLayoutIdInRecord(viewType)) {
            BaseAdapterItem<T> item = mBaseItem != null ? mBaseItem : mLayoutMap.get(viewType);
            return new ViewHolder0(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false),
                    viewType, item);
        } else {
            //header or footer
            return new ViewHolder0(hfHelper.findView(viewType, getActuallyItemSize()));
        }
    }
    @Override @SuppressWarnings("unchecked")
    protected void onBindDataImpl(RecyclerView.ViewHolder holder, int position, T item) {
        ViewHolder0 vh = (ViewHolder0) holder;
        BaseAdapterItem<T> adapterItem = (BaseAdapterItem<T>) vh.mAdapterItem;
        adapterItem.bindView(holder.itemView);
        adapterItem.onBindData(vh.getContext(), position, item);
    }

    @Override
    public final void setItemTypeDelegate(ItemTypeDelegate<T> mTypeDelegate) {
        throw new UnsupportedOperationException("use BaseAdapterItem instead.");
    }
    @Override
    protected final void onBindData(Context context, int position, T item, int itemLayoutId, ViewHelper2 helper) {
        throw new UnsupportedOperationException("use BaseAdapterItem instead.");
    }

    /**
     * set adapter item factory
     * @param factory the factory
     */
    public void setAdapterItemFactory(AdapterItemFactory<T> factory) {
        this.mFactory = factory;
    }
    private static class ViewHolder0 extends ViewHolder{
        final BaseAdapterItem<?> mAdapterItem;
        public ViewHolder0(View itemView, int layoutId,  BaseAdapterItem<?> item) {
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
