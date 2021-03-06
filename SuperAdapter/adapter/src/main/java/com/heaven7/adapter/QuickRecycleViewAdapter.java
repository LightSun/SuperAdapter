/*
 * Copyright (C) 2015
 *            heaven7(donshine723@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.heaven7.adapter.util.CommonItemDecoration;
import com.heaven7.adapter.util.ViewHelper2;
import com.heaven7.core.util.ViewHelper;

import java.util.List;

/**
 * the quick adapter of  RecyclerView. help we fast build a adapter.
 * Created by heaven7 on 2015/8/26.
 *
 * @param <T> the data
 */
public abstract class QuickRecycleViewAdapter<T extends ISelectable> extends HeaderFooterAdapter
        implements AdapterManager.IAdapterManagerCallback, AdapterManager.IHeaderFooterManager,
        AdapterManager.IAdapterManagerCallback2, HeaderFooterAdapter.Callback,
        CommonItemDecoration.AdapterDelegate {

    private int mLayoutId = 0;
    private AdapterManager<T> mAdapterManager;

    /**
     * create QuickRecycleViewAdapter with the layout id. if layoutId==0, the method
     * {@link #getItemLayoutId(int, ISelectable)} will be called.
     *
     * @param layoutId the layout id you want to inflate, or 0 if you want multi item.
     * @param mDatas   the datas
     */
    public QuickRecycleViewAdapter(int layoutId, List<T> mDatas) {
        this(layoutId, mDatas, ISelectable.SELECT_MODE_SINGLE);
    }

    /**
     * create QuickRecycleViewAdapter with the layout id. if layoutId==0, the method
     * {@link #getItemLayoutId(int, ISelectable)} will be called.
     *
     * @param layoutId   the layout id you want to inflate, or 0 if you want multi item.
     * @param mDatas     the datas
     * @param selectMode select mode . {@link ISelectable#SELECT_MODE_SINGLE} or {@link ISelectable#SELECT_MODE_MULTI}
     */
    public QuickRecycleViewAdapter(int layoutId, List<T> mDatas, int selectMode) {
        this(layoutId, mDatas, selectMode, true);
    }

    /**
     * internal
     */
    QuickRecycleViewAdapter(int layoutId, List<T> mDatas, int selectMode, boolean callFinalInit) {
        super();
        if (layoutId < 0) {
            throw new IllegalArgumentException("layoutId can't be negative ");
        }
        this.mLayoutId = layoutId;
        // mAdapterManager = createAdapterManager(mDatas,selectMode);
        mAdapterManager = new AdapterManager<T>(mDatas, selectMode, this) {
            @Override
            public IHeaderFooterManager getHeaderFooterManager() {
                return QuickRecycleViewAdapter.this;
            }
        };
        if (callFinalInit) {
            onFinalInit();
        }
        setCallback(this);
    }

    @Override
    public List getListItems() {
        return getAdapterManager().getItems();
    }
    @Override
    public int getActuallyItemSize() {
        return mAdapterManager.getItemSize();
    }

    /**
     * called before {@link #notifyDataSetChanged()}
     */
    @Override
    public void beforeNotifyDataChanged() {
        getSelectHelper().clearViewHolders();
    }

    /**
     * this is callled after data {@link #notifyDataSetChanged()}
     */
    @Override
    public void afterNotifyDataChanged() {

    }

    /**
     * the init operation of the last, called in constructor
     */
    protected void onFinalInit() {

    }

    @Override
    public final boolean isRecyclable() {
        return true;
    }

    public SelectHelper<T> getSelectHelper() {
        return getAdapterManager().getSelectHelper();
    }

    public final T getItem(int position) {
        return mAdapterManager.getItems().get(position);
    }

    /**
     * select the target position
     * <p>only support select mode = {@link ISelectable#SELECT_MODE_MULTI}</p>
     *
     * @param selectPosition the position of adapter
     **/
    public void addSelected(int selectPosition) {
        getSelectHelper().addSelected(selectPosition);
    }

    /**
     * un select the target position  .
     * <p>only support select mode = {@link ISelectable#SELECT_MODE_MULTI}</p>
     *
     * @param position the position of adapter
     **/
    @Deprecated
    public void addUnselected(int position) {
        getSelectHelper().addUnselected(position);
    }

    /**
     * un select the all selected position.
     * mode single or multi all supoorted
     */
    @Deprecated
    public void clearAllSelected() {
        getSelectHelper().clearAllSelected();
    }

    /**
     * select the target position with notify data.if currentPosition  == position.ignore it.
     * <p>only support select mode = {@link ISelectable#SELECT_MODE_SINGLE} ,this will auto update </p>
     *
     * @param position the position of adapter
     **/
    @Deprecated
    public void setSelected(int position) {
        getSelectHelper().setSelected(position);
    }

    /**
     * un select the target position
     * <p>only support select mode = {@link ISelectable#SELECT_MODE_SINGLE}</p>
     *
     * @param position the position of adapter
     */
    @Deprecated
    public void setUnselected(int position) {
        getSelectHelper().setUnselected(position);
    }

    /**
     * clear selected positions  . this just clear record. bu not notify item change
     * <p> support select mode = {@link ISelectable#SELECT_MODE_SINGLE} or {@link ISelectable#SELECT_MODE_MULTI}</p>
     */
    @Deprecated
    public void clearSelectedPositions() {
        getSelectHelper().clearSelectedPositions();
    }
    @Deprecated
    public T getSelectedData() {
        return getSelectHelper().getSelectedItem();
    }
    @Deprecated
    public List<T> getSelectedItems() {
        return getSelectHelper().getSelectedItems();
    }
    @Deprecated
    public int getSelectedPosition() {
        return getSelectHelper().getSelectedPosition();
    }

    //====================== begin items ========================//

    @Override
    public AdapterManager<T> getAdapterManager() {
        return mAdapterManager;
    }

    //====================== end items ========================//

    //extract for swipe adapter
    protected int getItemViewTypeImpl(HeaderFooterHelper hfHelper, int position) {
        int layoutId = getItemLayoutId(position, getItem(position));
        if (hfHelper != null)
            hfHelper.recordLayoutId(layoutId);
        return layoutId;
    }

    @Override
    public final void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int position) {
        // Logger.w("QuickRecycleViewAdapter", "onBindViewHolder", "position_1 = " + position);
        //notify bind ViewHolder
        getSelectHelper().onBindViewHolder(holder);

        //not in header or footer populate it
        final T item = getItem(position);
        final int layoutId = ((IRecyclerViewHolder) holder).getLayoutId();
        final ViewHelper helper = ((IRecyclerViewHolder) holder).getViewHelper();

        onBindDataImpl(holder, position, item);

        if (getAdapterManager().getPostRunnableCallbacks() != null) {
            final int pos = position;
            for (final AdapterManager.IPostRunnableCallback<T> callback : getAdapterManager()
                    .getPostRunnableCallbacks()) {
                holder.itemView.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onPostCallback(pos, item, layoutId, helper);
                    }
                });
            }
        }
    }

    protected void onBindDataImpl(RecyclerView.ViewHolder holder, int position, T item) {
        final ViewHolder vh = (ViewHolder) holder;
        onBindData(vh.getContext(), position, item, vh.getLayoutId(),
                (ViewHelper)vh.getViewHelper());
    }

    /**
     * if you use multi item ,override this
     *
     * @param position the position
     * @param t        the data
     * @return this item layout id
     **/
    protected int getItemLayoutId(int position, T t) {
        return mLayoutId;
    }

    /**
     * <p>Use{@linkplain #onBindData(Context, int, ISelectable, int, ViewHelper2)} instead.</p>
     * called on bind data.
     * @param context the context
     * @param position the position
     * @param item the item
     * @param itemLayoutId the layoutId
     * @param helper the view helper.
     */
    @Deprecated
    protected void onBindData(Context context, int position, T item,
                                       int itemLayoutId, ViewHelper helper){
        onBindData(context, position, item, itemLayoutId, (ViewHelper2)helper);
    }
    /**
     * called on bind data.
     * @param context the context
     * @param position the position
     * @param item the item
     * @param itemLayoutId the layoutId
     * @param helper the view helper.
     * @since 1.8.9
     */
    protected void onBindData(Context context, int position, T item, int itemLayoutId, ViewHelper2 helper){

    }
}
