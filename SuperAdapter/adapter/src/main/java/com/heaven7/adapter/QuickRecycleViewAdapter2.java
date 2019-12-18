package com.heaven7.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import com.heaven7.adapter.util.ViewHelper2;
import com.heaven7.core.util.ViewHelper;

import java.util.List;

/**
 * most sample as {@linkplain QuickRecycleViewAdapter}.
 * but there are two differents.
 * <li> first,
 * use new {@linkplain Selector} with not record position, just record the item data.
 * the old selector is {@linkplain SelectHelper}.
 * </li>
 * <li> second,
 *     add {@linkplain #setItemTypeDelegate(ItemTypeDelegate)}.
 *     <p>see {@linkplain ItemTypeDelegate} </p>
 * </li>
 * @param <T> the item type
 * @since 2.0.7
 */
public abstract class QuickRecycleViewAdapter2<T extends ISelectable>  extends HeaderFooterAdapter
        implements AdapterManager.IAdapterManagerCallback, AdapterManager.IHeaderFooterManager,
        AdapterManager.IAdapterManagerCallback2, HeaderFooterAdapter.Callback  {

    private int mLayoutId = 0;
    private AdapterManager<T> mAdapterManager;
    private ItemTypeDelegate<T> mTypeDelegate;

    /**
     * create QuickRecycleViewAdapter with the layout id. if layoutId==0, the method
     * {@link #getItemLayoutId(int, ISelectable)} will be called.
     *
     * @param layoutId the layout id you want to inflate, or 0 if you want multi item.
     * @param mDatas   the datas
     */
    public QuickRecycleViewAdapter2(int layoutId, List<T> mDatas) {
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
    public QuickRecycleViewAdapter2(int layoutId, List<T> mDatas, int selectMode) {
        super();
        if (layoutId < 0) {
            throw new IllegalArgumentException("layoutId can't be negative ");
        }
        this.mLayoutId = layoutId;
        mAdapterManager = new AdapterManager<T>(mDatas, selectMode, this, true) {
            @Override
            public IHeaderFooterManager getHeaderFooterManager() {
                return QuickRecycleViewAdapter2.this;
            }
        };
        setCallback(this);
        onFinalInit();
    }
    @Override
    public int getActuallyItemSize() {
        return mAdapterManager.getItemSize();
    }

    public void setItemTypeDelegate(ItemTypeDelegate<T> mTypeDelegate) {
        this.mTypeDelegate = mTypeDelegate;
    }
    /**
     * get selector
     * @return the selector
     */
    public Selector<T> getSelector(){
        return mAdapterManager.getSelector();
    }

    /**
     * called before {@link #notifyDataSetChanged()}
     */
    @Override
    public void beforeNotifyDataChanged() {

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
    public final T getItem(int position) {
        return mAdapterManager.getItems().get(position);
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
        onBindData(vh.getContext(), position, item, vh.getLayoutId(), vh.getViewHelper());
    }

    /**
     * if you use multi item ,override this
     *
     * @param position the position exclude header
     * @param t        the data
     * @return this item layout id
     **/
    protected
    @LayoutRes
    int getItemLayoutId(int position, T t) {
        if(mTypeDelegate != null){
            return mTypeDelegate.getItemLayoutId(position, t);
        }
        return mLayoutId;
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
        if(mTypeDelegate != null){
            mTypeDelegate.onBindData(this, context, position, item, itemLayoutId, helper);
        }
    }

    /**
     * item type delegate
     * @param <T> the item type
     */
    public interface ItemTypeDelegate<T extends ISelectable> {
        /**
         * get item layout id
         *
         * @param item        the item
         * @param position the position exclude header
         * @return the item layout id.
         */
        int getItemLayoutId(int position, T item);

        /**
         * called on bind parent data
         *
         * @param context      the context
         * @param position     the position exclude header
         * @param item         the item
         * @param itemLayoutId the item layout id
         * @param helper       the helper
         */
        void onBindData(QuickRecycleViewAdapter2<T> adapter, Context context, int position, T item, int itemLayoutId, ViewHelper2 helper);
    }
    /**
     * item type delegate
     * @param <T> the item type
     */
    public static abstract class BaseItemTypeDelegate<T extends ISelectable> implements ItemTypeDelegate<T>{

        private final int layoutId;

        public BaseItemTypeDelegate(int layoutId) {
            this.layoutId = layoutId;
        }
        @Override
        public int getItemLayoutId(int position, T item) {
            return layoutId;
        }
    }
}
