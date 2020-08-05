package com.heaven7.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * the diff adapter which can use sync and async to diff sync data.
 * <p><h3>have multi features:</h3>
 *     <ul>
 *         <li>Support sync and async diff data. see {@linkplain #setAsyncDiffExecutor(Executor)} and {@linkplain #commit(List, Runnable)}.</li>
 *         <li>Support common {@linkplain Selector}. see {@linkplain #setSelector(Selector)}</li>
 *         <li>Support {@linkplain BaseAdapterItem} to bind data</li>
 *         <li>Support listen callback on data changed. see {@linkplain ListDataUpdateCallback}</li>
 *     </ul>
 * </p>
 * @param <T> the data type
 * @since 2.1.2
 */
public class DiffAdapter<T> extends HeaderFooterAdapter {

    private final DiffManager<T> mDiffManager;
    private final AdapterItemHelper<T> mAIH;
    private SelectCallback0 mSelectCallback;
    private Selector<ISelectable> mSelector;

    private ListDataUpdateCallback<T> mListDataUpdateCallback;

    /**
     * create diff adapter
     * @param callback the item callback
     * @param item the base adapter item. can be null.
     * @param mDatas the init datas. can be null/
     */
    public DiffAdapter(@NonNull DiffManager.ItemCallback<T> callback, @Nullable BaseAdapterItem<T> item, @Nullable List<T> mDatas){
        mDiffManager = new DiffManager<T>(callback,
                new AdapterListUpdateCallback(this, this, new InternalListUpdateCallback()),
                mDatas);
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

    /**
     * set the selector if you need. call this you should make all data impl {@linkplain ISelectable} to do with select state.
     * after call this, you often should call {@linkplain #initializeSelectState()} to initialize select state for current data.
     * @param selector the selector
     */
    @SuppressWarnings("unchecked")
    public void setSelector(Selector<? extends ISelectable> selector) {
        if(mSelectCallback == null){
            mSelectCallback = new SelectCallback0();
        }
        if(mSelector != null){
            mSelector.removeCallback(mSelectCallback);
        }
        this.mSelector = (Selector<ISelectable>) selector;
        this.mSelector.addCallback(mSelectCallback);
    }

    /**
     * initialize select state for current data.
     */
    @SuppressWarnings("unchecked")
    public void initializeSelectState(){
        try {
            this.mSelector.initialize((List<? extends ISelectable>) getCurrentList());
        }catch (ClassCastException e){
            throw new IllegalStateException("you must make every item data impl ISelectable");
        }
    }

    /**
     * set list data update callback
     * @param callback the list data update callback
     */
    public void setListDataUpdateCallback(ListDataUpdateCallback<T> callback) {
        this.mListDataUpdateCallback = callback;
    }

    /**
     * set async diff executor for {@linkplain AsyncListDiffer}.
     * @param workerThreadExecutor the worker thread executor. null means clear async mode.
     * @param listener the list listener
     * @see #isAsyncMode()
     */
    public void setAsyncDiffExecutor(@Nullable Executor workerThreadExecutor, @Nullable AsyncListDiffer.ListListener<T> listener){
        mDiffManager.setAsyncDiffExecutor(workerThreadExecutor, listener);
    }
    /**
     * set async diff executor for {@linkplain AsyncListDiffer}.
     * @param workerThreadExecutor the worker thread executor
     */
    public void setAsyncDiffExecutor(@Nullable Executor workerThreadExecutor){
        mDiffManager.setAsyncDiffExecutor(workerThreadExecutor, null);
    }

    /**
     * indicate diff manager is work with async mode or not.
     * @return true if is async.
     * @see AsyncListDiffer
     */
    public boolean isAsyncMode(){
        return mDiffManager.isAsyncMode();
    }

    /**
     * commit the new list to diff manager
     * @param newList the new list
     * @param commitCallback the commit callback. this is called after commit ok. can be null.
     */
    public void commit(List<T> newList, @Nullable Runnable commitCallback){
        mDiffManager.commit(newList, commitCallback);
    }

    /**
     * commit the new list to diff manager
     * @param newList the new list
     */
    public void commit(List<T> newList){
        mDiffManager.commit(newList, null);
    }
    /**
     * get the current data list. and this list is read only.
     * @return the read only list
     */
    public List<T> getCurrentList(){
        return mDiffManager.getCurrentList();
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
    private class InternalListUpdateCallback implements ListUpdateCallback{
        @Override
        public void onInserted(int position, int count) {
            List<T> list = null;
            if(mSelector != null){
                list = getData(position, count);
                if(ISelectable.class.isAssignableFrom(list.get(0).getClass())){
                    //selectable data. so add to selector if need.
                    List<ISelectable> selects = mSelector.getSelects();
                    for (T t : list){
                        ISelectable iset = (ISelectable) t;
                        if(iset.isSelected()){
                            selects.add(iset);
                        }
                    }
                }
            }
            if(mListDataUpdateCallback != null){
                if(list == null){
                    list = getData(position, count);
                }
                mListDataUpdateCallback.onInserted(position, list);
            }
        }
        @Override
        public void onRemoved(int position, int count) {
            List<T> list = null;
            if(mSelector != null){
                list = getData(position, count);
                mSelector.getSelects().removeAll(list);
            }
            if(mListDataUpdateCallback != null){
                if(list == null){
                    list = getData(position, count);
                }
                mListDataUpdateCallback.onRemoved(position, list);
            }
        }
        @Override
        public void onMoved(int fromPosition, int toPosition) {
            if(mListDataUpdateCallback != null){
                List<T> previousList = mDiffManager.getPreviousList();
                if(previousList.isEmpty()){
                    throw new IllegalStateException();
                }
                T t = previousList.get(fromPosition);
                mListDataUpdateCallback.onMoved(fromPosition, toPosition, t);
            }
        }
        @Override
        public void onChanged(int position, int count, @Nullable Object payload) {
            if(mListDataUpdateCallback != null){
                List<T> list = getData(position, count);
                mListDataUpdateCallback.onChanged(position, list, payload);
            }
        }
        private List<T> getData(int position, int count){
            List<T> previousList = mDiffManager.getPreviousList();
            if(previousList.isEmpty()){
                throw new IllegalStateException();
            }
            return previousList.subList(position, position + count);
        }
    }

}
