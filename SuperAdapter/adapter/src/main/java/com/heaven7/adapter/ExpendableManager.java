package com.heaven7.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the expendable manager
 * @param <T> the parent item type
 */
public final class ExpendableManager<T> implements IExpendableManager<T> {

    private final IPartUpdater mAMC;
    private final List<T> mDatas;

    private final int[] mPoss = new int[2];

    public ExpendableManager(AdapterManager.IHeaderFooterManager mHFM, IPartUpdater mAMC, List<T> mDatas) {
        this.mAMC = new HeaderFooterPartUpdater(mAMC, mHFM);
        this.mDatas = mDatas != null ? new ArrayList<T>(mDatas) : new ArrayList<T>();
    }

    @Override
    public boolean toggleExpend(T data, int position) {
        if(data instanceof ExpendableAdapter.ExpendableItem){
            ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) data;
            if(ei.isExpended()){
                collapse(position);
            }else {
                expand(position);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean toggleExpendByParent(T data, int parentPosition) {
        if(data instanceof ExpendableAdapter.ExpendableItem){
            ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) data;
            if(ei.isExpended()){
                collapseByParent(parentPosition);
            }else {
                expandByParent(parentPosition);
            }
            return true;
        }
        return false;
    }

    @Override
    public void collapseAll() {
        for (int i = 0, size = mDatas.size() ;i < size ; i ++){
            T t = mDatas.get(i);
            if(t instanceof ExpendableAdapter.ExpendableItem){
                ((ExpendableAdapter.ExpendableItem) t).setExpended(false);
            }
        }
        mAMC.notifyDataSetChanged();
    }

    @Override
    public void expendAll() {
        for (int i = 0, size = mDatas.size() ;i < size ; i ++){
            T t = mDatas.get(i);
            if(t instanceof ExpendableAdapter.ExpendableItem){
                ((ExpendableAdapter.ExpendableItem) t).setExpended(true);
            }
        }
        mAMC.notifyDataSetChanged();
    }

    @Override
    public boolean collapse(int position) {
        int[] poss = getParentPositions(position, null);
        if(poss == null){
            return false;
        }
        return collapseByParent(poss[0]);
    }
    @Override
    public boolean collapseByParent(int parentPos) {
        T t = getItem(parentPos);
        if (t instanceof ExpendableAdapter.ExpendableItem) {
            ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) t;
            if (ei.isExpended()) {
                ei.setExpended(false);

                final int preCount = getPreviousCount(parentPos);
                mAMC.notifyItemChanged(preCount);
                mAMC.notifyItemRangeRemoved(preCount + 1, ei.getChildItemCount());
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean expand(int realPosition) {
        int[] poss = getParentPositions(realPosition, null);
        if(poss == null){
            return false;
        }
        return expandByParent(poss[0]);
    }

    @Override
    public boolean expandByParent(int parentPos) {
        T t = getItem(parentPos);
        if (t instanceof ExpendableAdapter.ExpendableItem) {
            ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) t;
            if (ei.getChildItemCount()> 0 && !ei.isExpended()) {
                ei.setExpended(true);
                final int preCount = getPreviousCount(parentPos);
                mAMC.notifyItemChanged(preCount);
                mAMC.notifyItemRangeInserted(preCount + 1, ei.getChildItemCount());
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean addChildItems(int parentPos, List<?> list, boolean notifyParent){
        return addChildItems(parentPos, -1, list,  notifyParent, false);
    }
    @Override
    public boolean addChildItems(int parentPos, int childStartIndex, List<?> list, boolean notifyParent){
        return addChildItems(parentPos, childStartIndex, list,  notifyParent, false);
    }
    @Override
    public boolean addChildItems(int parentPos, int childStartIndex, List<?> list, boolean notifyParent, boolean notifyAfter){
        T item = getItem(parentPos);
        if(item instanceof ExpendableAdapter.ExpendableItem){
            ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) item;
            //-1 mean add to last
            if(childStartIndex == -1){
                childStartIndex = ei.getChildItemCount();
            }
            ei.addChildItems(childStartIndex, list);
            final int preCount = getPreviousCount(parentPos);
            if(ei.isExpended()){
                int itemCount = ei.getChildItemCount();
                mAMC.notifyItemRangeInserted(preCount + 1 + childStartIndex, list.size());
                if(notifyAfter){
                    int mayChangCount = itemCount - 1 - childStartIndex;
                    if(mayChangCount > 0){
                        mAMC.notifyItemRangeChanged(preCount + 1 + childStartIndex + list.size(), mayChangCount);
                    }
                }
            }
            if(notifyParent){
                mAMC.notifyItemChanged(preCount);
            }
            return true;
        }
        return false;
    }

    //---------------------------------------------------------------

    @Override
    public void setItems(List<T> list){
        mDatas.clear();
        mDatas.addAll(list);
        mAMC.notifyDataSetChanged();
    }
    @Override
    public void addItems(List<T> list){
        int oldSize = computeItemCount(mDatas);
        int deltaSize = computeItemCount(list);
        mDatas.addAll(list);
        mAMC.notifyItemRangeInserted(oldSize, deltaSize);
    }
    @Override
    public void addItems(int startParentPos, List<T> list){
        int preCount = getPreviousCount(startParentPos);
        int deltaSize = computeItemCount(list);
        mDatas.addAll(startParentPos, list);
        mAMC.notifyItemRangeInserted(preCount + 1, deltaSize);
    }

    @Override
    public void addItem(T item){
        addItems(Arrays.asList(item));
    }
    @Override
    public List<T> getItems(){
        return mDatas;
    }
    @Override
    public T getItem(int parentPos) {
        return mDatas.get(parentPos);
    }

    @Override
    public boolean hasChildItem(int parentPos) {
        T t = getItem(parentPos);
        if (t instanceof ExpendableAdapter.ExpendableItem) {
            return ((ExpendableAdapter.ExpendableItem) t).getChildItemCount() > 0;
        }
        return false;
    }

    @Override
    public boolean isExpendItem(int parentPos) {
        return getItem(parentPos) instanceof ExpendableAdapter.ExpendableItem;
    }
    @Override
    public T getParentItem(int realPosition){
        int[] positions = getParentPositions(realPosition, null);
        if(positions != null){
            return getItem(positions[0]);
        }
        return null;
    }

    @Override
    public Object getChildItem(int parentPos, int childPos) {
        T t = getItem(parentPos);
        if (t instanceof ExpendableAdapter.ExpendableItem) {
            return ((ExpendableAdapter.ExpendableItem) t).getChildItem(childPos);
        }
        return null;
    }

    @Override
    public Object getChildItem(int realPosition){
        int[] positions = getParentPositions(realPosition, null);
        if(positions == null || positions[1] == -1){
            return null;
        }
        ExpendableAdapter.ExpendableItem item = (ExpendableAdapter.ExpendableItem) getItem(positions[0]);
        return item.getChildItem(positions[1]);
    }
    //-----------------------------------------------------
    @Override
    public int[] getParentPositions(int pos, int[] outPoss) {
        if(outPoss == null){
            outPoss = mPoss;
        }
        int total = 0;
        int delta;
        for (int i = 0, size = mDatas.size(); i < size; i++) {
            T t = mDatas.get(i);
            if (t instanceof ExpendableAdapter.ExpendableItem) {
                ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) t;
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
    /**
     * compute the item count
     * @param list the datas
     * @param <T> the parent data type
     * @return the item count.
     */
    public static <T> int computeItemCount(List<T> list){
        int total = 0;
        T t;
        for (int i = 0, size = list.size(); i < size; i++) {
            t = list.get(i);
            total += 1;
            if (t instanceof ExpendableAdapter.ExpendableItem) {
                ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) t;
                if (ei.isExpended()) {
                    total += ei.getChildItemCount();
                }
            }
        }
        return total;
    }

    /**
     * get previous count exclude header size
     * @param parentPos the parent position
     * @return the previous count
     */
    private int getPreviousCount(int parentPos) {
        if (parentPos > 0) {
            return computeItemCount(getItems().subList(0, parentPos));
        }
        return 0;
    }

    //---------------------------------------------------------------

    private static class HeaderFooterPartUpdater implements IPartUpdater{

        private final AdapterManager.IHeaderFooterManager mHFM;
        private final IPartUpdater mUpdater;

        public HeaderFooterPartUpdater(IPartUpdater mUpdater, AdapterManager.IHeaderFooterManager mHFM) {
            this.mUpdater = mUpdater;
            this.mHFM = mHFM;
        }

        @Override
        public void notifyItemInserted(int position) {
            mUpdater.notifyItemInserted(position + mHFM.getHeaderSize());
        }

        @Override
        public void notifyItemChanged(int position) {
            mUpdater.notifyItemChanged(position + mHFM.getHeaderSize());
        }

        @Override
        public void notifyItemRemoved(int position) {
            mUpdater.notifyItemRemoved(position + mHFM.getHeaderSize());
        }

        @Override
        public void notifyItemMoved(int fromPosition, int toPosition) {
            mUpdater.notifyItemMoved(fromPosition + mHFM.getHeaderSize(), toPosition + mHFM.getHeaderSize());
        }

        @Override
        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            mUpdater.notifyItemRangeChanged(positionStart + mHFM.getHeaderSize(), itemCount);
        }

        @Override
        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            mUpdater.notifyItemRangeInserted(positionStart + mHFM.getHeaderSize(), itemCount);
        }
        @Override
        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            mUpdater.notifyItemRangeRemoved(positionStart + mHFM.getHeaderSize(), itemCount);
        }
        @Override
        public void notifyDataSetChanged() {
            mUpdater.notifyDataSetChanged();
        }
    }
}
