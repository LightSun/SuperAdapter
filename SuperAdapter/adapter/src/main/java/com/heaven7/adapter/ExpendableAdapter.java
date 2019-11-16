package com.heaven7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.heaven7.adapter.util.ViewHelper2;

import java.util.List;

/**
 * the expendable adapter
 * @param <T> the item type
 * @author heaven7
 * @since 2.0.5
 */
public class ExpendableAdapter<T> extends HeaderFooterAdapter {

    private final ItemTypeDelegate<T> mItemTypeDelegate;
    private ItemBindDelegate<T> mItemBindDelegate;

    private final ExpendableManager<T> mManager;

    public ExpendableAdapter(ItemTypeDelegate<T> typeDelegate, List<T> items) {
        this.mItemTypeDelegate = typeDelegate;
        mManager = new ExpendableManager<T>(this, this, items);
        setCallback(new HeaderFooterAdapter.Callback() {
            @Override
            public int getActuallyItemSize() {
                return ExpendableManager.computeItemCount(mManager.getItems());
            }
        });
    }
    public void setItemBindDelegate(ItemBindDelegate<T> bindDelegate){
        mItemBindDelegate = bindDelegate;
    }

    public IExpendableManager<T> getManager(){
        return mManager;
    }

    public void setItems(List<T> list){
        mManager.setItems(list);
    }

    public void addItems(List<T> list){
        mManager.addItems(list);
    }
    /**
     *  add child items.
     * @param parentPos the parent pos
     * @param list the list
     * @param notifyParent true to notify parent
     * @return true if add success
     * @since 2.0.5-beta2
     */
    public boolean addChildItems(int parentPos, List<?> list, boolean notifyParent){
        return mManager.addChildItems(parentPos, list,  notifyParent);
    }
    public boolean addChildItems(int parentPos, int childStartIndex, List<?> list,  boolean notifyParent){
        return mManager.addChildItems(parentPos, childStartIndex, list,  notifyParent);
    }
    public void addItem(T item){
        mManager.addItem(item);
    }
    public List<T> getItems(){
        return mManager.getItems();
    }
    public T getParentItem(int realPosition){
        return mManager.getParentItem(realPosition);
    }
    public Object getChildItem(int realPosition){
        return mManager.getChildItem(realPosition);
    }
    public T getItem(int parentPos) {
        return mManager.getItem(parentPos);
    }
    public boolean hasChildItem(int parentPos) {
        return mManager.hasChildItem(parentPos);
    }

    public boolean isExpendItem(int parentPos) {
        return mManager.isExpendItem(parentPos);
    }

    public Object getChildItem(int parentPos, int childPos) {
        return mManager.getChildItem(parentPos, childPos);
    }

    public boolean collapse(int position) {
        return mManager.collapse(position);
    }
    public boolean collapseByParent(int parentPos) {
        return mManager.collapseByParent(parentPos);
    }
    public boolean expand(int parentPos) {
        return mManager.expand(parentPos);
    }
    public boolean expandByParent(int parentPos) {
        return mManager.expandByParent(parentPos);
    }

    /**
     * get parent position include parent pos and sub pos.
     *
     * @param pos     the position. exclude header
     * @param outPoss the out positions. [0] is parent position. [1] is sub position.
     * @return null if can't find
     */
    public int[] getParentPositions(int pos, int[] outPoss) {
        return mManager.getParentPositions(pos, outPoss);
    }

    //-----------------------------------------------------------------------------------------
    @Override
    protected void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int position) {
        IRecyclerViewHolder vh = (IRecyclerViewHolder) holder;

        int[] poss = getParentPositions(position, null);
        int parentPosition = poss[0];
        T t = getItem(parentPosition);
        //not sub position.
        if (poss[1] == -1) {
            onBindParentData(vh.getViewHelper().getContext(), position, parentPosition,
                    t, vh.getLayoutId(), (ViewHelper2) vh.getViewHelper());
        }else {
            onBindChildData(vh.getViewHelper().getContext(), position, parentPosition, poss[1],
                    t, vh.getLayoutId(), (ViewHelper2) vh.getViewHelper());
        }
    }

    @Override
    protected int getItemViewTypeImpl(HeaderFooterHelper hfHelper, int position) {
        int layoutId = getItemLayoutId(hfHelper, position);
        if (hfHelper != null)
            hfHelper.recordLayoutId(layoutId);
        return layoutId;
    }
    private int getItemLayoutId(HeaderFooterHelper hfHelper, int position) {
        int[] poss = getParentPositions(position, null);
        int parentPosition = poss[0];
        int pos = parentPosition != -1 ? parentPosition : position;
        T t = getItem(pos);
        //not sub position.
        if (poss[1] == -1) {
            // may be normal item or expend main item
            return mItemTypeDelegate.getItemLayoutId(t, position, pos);
        }
        return mItemTypeDelegate.getChildItemLayoutId(t, position, parentPosition, poss[1]);
    }
    /**
     * called on bind parent data
     * @param context the context
     * @param realPosition the real pos exclude header
     * @param parentPos the parent pos exclude header
     * @param item the item
     * @param helper the helper
     */
    protected void onBindParentData(Context context, int realPosition, int parentPos, T item, int layoutId, ViewHelper2 helper){
        if(mItemBindDelegate != null){
            mItemBindDelegate.onBindParentData(this, context, realPosition, parentPos, item, layoutId, helper);
        }
    }
    /**
     * called on bind child data
     * @param context the context
     * @param realPosition the real pos exclude header
     * @param parentPos the parent pos exclude header
     * @param childPos the child pos exclude header
     * @param item the item
     * @param helper the helper
     */
    protected void onBindChildData(Context context, int realPosition, int parentPos, int childPos, T item, int layoutId,ViewHelper2 helper){
        if(mItemBindDelegate != null){
            mItemBindDelegate.onBindChildData(this, context, realPosition, parentPos, childPos, item, layoutId, helper);
        }
    }

    public interface ItemTypeDelegate<T> {
        /**
         * get item layout id
         *
         * @param item        the item
         * @param position the position exclude header
         * @param realPosition the real position exclude header
         * @return the item layout id.
         */
        int getItemLayoutId(T item, int realPosition, int position);

        /**
         * get the sub item layout id
         *
         * @param item              the parent item
         * @param realPosition the real position exclude header
         * @param parentPosition the parent position exclude header
         * @param childPos       the child position exclude header
         * @return the layout id of sub layout.
         */
        int getChildItemLayoutId(T item, int realPosition, int parentPosition, int childPos);
    }

    /**
     * the item bind delegate
     * @param <T> the parent item
     */
    public interface ItemBindDelegate<T>{
        /**
         * called on bind parent data
         * @param adapter the expend adapter
         * @param context the context
         * @param realPosition the real pos exclude header
         * @param parentPos the parent pos exclude header
         * @param item the item
         * @param itemLayoutId the item layout id
         * @param helper the helper
         */
        void onBindParentData(ExpendableAdapter<T> adapter, Context context, int realPosition, int parentPos, T item,
                              int itemLayoutId, ViewHelper2 helper);
        /**
         * called on bind child data
         * @param adapter the expend adapter
         * @param context the context
         * @param realPosition the real pos exclude header
         * @param parentPos the parent pos exclude header
         * @param childPos the child pos exclude header
         * @param item the item
         * @param itemLayoutId the item layout id
         * @param helper the helper
         */
        void onBindChildData(ExpendableAdapter<T> adapter, Context context, int realPosition, int parentPos, int childPos, T item,
                             int itemLayoutId, ViewHelper2 helper);
    }

    /**
     * the expend item
     */
    public interface ExpendableItem {

        boolean isExpended();

        void setExpended(boolean expend);

        List<?> getChildItems();
    }
}
