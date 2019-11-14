package com.heaven7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.heaven7.adapter.util.ViewHelper2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the expendable adapter
 * @param <T> the item type
 * @author heaven7
 * @since 2.0.5
 */
public class ExpendableAdapter<T> extends HeaderFooterAdapter {

    private final List<T> mDatas;
    private final int[] mPoss = new int[2];
    private final ItemTypeDelegate<T> mItemTypeDelegate;
    private ItemBindDelegate<T> mItemBindDelegate;

    public ExpendableAdapter(ItemTypeDelegate<T> typeDelegate, List<T> items) {
        this.mItemTypeDelegate = typeDelegate;
        this.mDatas = items != null ? new ArrayList<>(items) : new ArrayList<T>();
        setCallback(new HeaderFooterAdapter.Callback() {
            @Override
            public int getActuallyItemSize() {
                return computeItemCount(mDatas);
            }
        });
    }
    public void setItemBindDelegate(ItemBindDelegate<T> bindDelegate){
        mItemBindDelegate = bindDelegate;
    }

    public void setItems(List<T> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(List<T> list){
        int oldSize = computeItemCount(mDatas);
        int deltaSize = computeItemCount(list);
        mDatas.addAll(list);
        notifyItemRangeInserted(oldSize + getHeaderSize(), deltaSize);
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
        return addChildItems(parentPos, -1, list,  notifyParent, false);
    }
    public boolean addChildItems(int parentPos, int childStartIndex, List<?> list,  boolean notifyParent){
        return addChildItems(parentPos, childStartIndex, list,  notifyParent, false);
    }
    /**
     *  add child items.
     * @param parentPos the parent pos
     * @param childStartIndex the child start index
     * @param list the list
     * @param notifyParent true to notify parent
     * @param notifyAfter true to notify after positions
     * @return true if add success
     * @since 2.0.5-beta2
     */
    public boolean addChildItems(int parentPos, int childStartIndex, List<?> list, boolean notifyParent, boolean notifyAfter){
        T item = getItem(parentPos);
        if(item instanceof ExpendableItem){
            ExpendableItem ei = (ExpendableItem) item;
            //-1 mean add to last
            if(childStartIndex == -1){
                childStartIndex = ei.getChildItemCount();
            }
            ei.addChildItems(childStartIndex, list);
            final int preCount = getPreCount(parentPos);
            if(ei.isExpended()){
                int itemCount = ei.getChildItemCount();
                notifyItemRangeInserted(preCount + 1 + childStartIndex, list.size());
                if(notifyAfter){
                    int mayChangCount = itemCount - 1 - childStartIndex;
                    if(mayChangCount > 0){
                        notifyItemRangeChanged(preCount + 1 + childStartIndex + list.size(), mayChangCount);
                    }
                }
            }
            if(notifyParent){
                notifyItemChanged(preCount);
            }
            return true;
        }
        return false;
    }
    public void addItem(T item){
        addItems(Arrays.asList(item));
    }
    public List<T> getItems(){
        return mDatas;
    }
    public T getParentItem(int realPosition){
        int[] positions = getParentPositions(realPosition, null);
        if(positions != null){
             return getItem(positions[0]);
        }
        return null;
    }
    public Object getChildItem(int realPosition){
        int[] positions = getParentPositions(realPosition, null);
        if(positions == null || positions[1] == -1){
            return null;
        }
        ExpendableItem item = (ExpendableItem) getItem(positions[0]);
        return item.getChildItem(positions[1]);
    }

    public T getItem(int parentPos) {
        return mDatas.get(parentPos);
    }

    public boolean hasSubItem(int parentPos) {
        T t = getItem(parentPos);
        if (t instanceof ExpendableItem) {
            return ((ExpendableItem) t).getChildItemCount() > 0;
        }
        return false;
    }

    public boolean isExpendItem(int parentPos) {
        return getItem(parentPos) instanceof ExpendableItem;
    }

    public Object getSubItem(int parentPos, int subPos) {
        T t = getItem(parentPos);
        if (t instanceof ExpendableItem) {
            return ((ExpendableItem) t).getChildItem(subPos);
        }
        return null;
    }

    public boolean collapse(int position) {
        int[] poss = getParentPositions(position, null);
        if(poss == null){
            return false;
        }
        return collapseByParent(poss[0]);
    }
    public boolean collapseByParent(int parentPos) {
        T t = getItem(parentPos);
        if (t instanceof ExpendableItem) {
            ExpendableItem ei = (ExpendableItem) t;
            if (ei.isExpended()) {
                ei.setExpended(false);

                final int preCount = getPreCount(parentPos);
                notifyItemChanged(preCount);
                notifyItemRangeRemoved(preCount + 1, ei.getChildItemCount());
                return true;
            }
        }
        return false;
    }
    public boolean expand(int parentPos) {
        int[] poss = getParentPositions(parentPos, null);
        if(poss == null){
            return false;
        }
        return expandByParent(poss[0]);
    }
    public boolean expandByParent(int parentPos) {
        T t = getItem(parentPos);
        if (t instanceof ExpendableItem) {
            ExpendableItem ei = (ExpendableItem) t;
            if (!ei.isExpended()) {
                ei.setExpended(true);
                final int preCount = getPreCount(parentPos);
                notifyItemChanged(preCount);
                notifyItemRangeInserted(preCount + 1, ei.getChildItemCount());
                return true;
            }
        }
        return false;
    }

    /**
     * get parent position include parent pos and sub pos.
     *
     * @param pos     the position. exclude header
     * @param outPoss the out positions. [0] is parent position. [1] is sub position.
     * @return null if can't find
     */
    public int[] getParentPositions(int pos, int[] outPoss) {
        if(outPoss == null){
            outPoss = mPoss;
        }
        int total = 0;
        int delta = 0;
        for (int i = 0, size = mDatas.size(); i < size; i++) {
            T t = mDatas.get(i);
            if (t instanceof ExpendableItem) {
                ExpendableItem ei = (ExpendableItem) t;
                if (ei.isExpended()) {
                    delta = ei.getChildItemCount();
                }else {
                    delta = 0;
                }
            }else {
                delta = 0;
            }
            if (pos >= total && pos <= total + delta) {
                outPoss[0] = i;
                outPoss[1] = pos - total - 1;
                return outPoss;
            }
            total += 1 + delta;
        }
        return null;
    }

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

    private int getPreCount(int parentPos) {
        final int preCount;
        if (parentPos > 0) {
            preCount = computeItemCount(getItems().subList(0, parentPos)) + getHeaderSize();
        } else {
            preCount = 0;
        }
        return preCount;
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

    public int computeItemCount(List<T> list){
        int total = 0;
        for (int i = 0, size = list.size(); i < size; i++) {
            T t = list.get(i);
            total += 1;
            if (t instanceof ExpendableItem) {
                ExpendableItem ei = (ExpendableItem) t;
                if (ei.isExpended()) {
                    total += ei.getChildItemCount();
                }
            }
        }
        return total;
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
        mItemBindDelegate.onBindParentData(this, context, realPosition, parentPos, item, layoutId, helper);
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
        mItemBindDelegate.onBindChildData(this, context, realPosition, parentPos, childPos, item, layoutId, helper);
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

        int getChildItemCount();

        Object getChildItem(int subPos);

        void addChildItems(int childStartIndex, List<?> list);
    }
}
