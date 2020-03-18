package com.heaven7.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    public int indexOfChildItemReally(Object childItem) {
        int parentSize = mDatas.size();
        int index = -1;
        for (int i = 0; i < parentSize; i++) {
            T t = mDatas.get(i);
            index += 1;
            if (t instanceof ExpendableAdapter.ExpendableItem) {
                ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) t;
                if (ei.isExpended()) {
                    List<?> items = ei.getChildItems();
                    int childIndex = items.indexOf(childItem);
                    if(childIndex >= 0){
                        index += childIndex + 1; //for child need add 1
                        return index;
                    }else {
                        index += items.size();
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public int indexOfParentItemReally(T item) {
        int parentSize = mDatas.size();
        int index = -1;
        for (int i = 0; i < parentSize; i++) {
            T t = mDatas.get(i);
            index += 1;
            if(t.equals(item)){
                return index;
            }
            if (t instanceof ExpendableAdapter.ExpendableItem) {
                ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) t;
                if (ei.isExpended()) {
                    index += ei.getChildItems().size();
                }
            }
        }
        return -1;
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
                mAMC.notifyItemRangeRemoved(preCount + 1, ei.getChildItems().size());
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
            ExpendItemWrapper ei = wrapItem(t);
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
        return addChildItems(parentPos, -1, list,  notifyParent);
    }
    @Override
    public boolean addChildItems(int parentPos, int childStartIndex, List<?> list, boolean notifyParent){
        T item = getItem(parentPos);
        if(item instanceof ExpendableAdapter.ExpendableItem){
            ExpendItemWrapper ei = wrapItem(item);
            //-1 mean add to last
            if(childStartIndex == -1){
                childStartIndex = ei.getChildItemCount();
            }
            ei.addChildItems(childStartIndex, list);
            final int preCount = getPreviousCount(parentPos);
            if(ei.isExpended()){
                mAMC.notifyItemRangeInserted(preCount + 1 + childStartIndex, list.size());
            }
            if(notifyParent){
                mAMC.notifyItemChanged(preCount);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeChildItems(int parentPos, int childStartIndex, int count, boolean notifyParent) {
        T item = getItem(parentPos);
        if(item instanceof ExpendableAdapter.ExpendableItem){
            ExpendItemWrapper ei = wrapItem(item);
            ei.removeChildItems(childStartIndex, count);
            final int preCount = getPreviousCount(parentPos);
            if(ei.isExpended()){
                mAMC.notifyItemRangeRemoved(preCount + 1 + childStartIndex, count);
            }
            if(notifyParent){
                mAMC.notifyItemChanged(preCount);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateChildItems(int parentPos, int childStartIndex, int count, boolean notifyParent, ChildItemsUpdater updater) {
        T item = getItem(parentPos);
        if(item instanceof ExpendableAdapter.ExpendableItem){
            ExpendItemWrapper ei = wrapItem(item);
            List<?> items = ei.getChildItems(childStartIndex, count);
            if(updater.update(parentPos, childStartIndex, items)){
                final int preCount = getPreviousCount(parentPos);
                mAMC.notifyItemRangeChanged(preCount + 1 + childStartIndex, items.size());
                if(notifyParent){
                    mAMC.notifyItemChanged(preCount);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateChildItems(int parentPos, boolean notifyParent, ChildItemsUpdater updater) {
        return updateChildItems(parentPos, 0,  -1, notifyParent, updater);
    }

    @Override
    public boolean updateChildItem(int parentPos, int childIndex, boolean notifyParent, ChildItemUpdater updater) {
        T item = getItem(parentPos);
        if(item instanceof ExpendableAdapter.ExpendableItem){
            ExpendItemWrapper ei = wrapItem(item);
            Object child = ei.getChildItem(childIndex);
            if(updater.update(parentPos, childIndex, child)){
                final int preCount = getPreviousCount(parentPos);
                mAMC.notifyItemChanged(preCount + 1 + childIndex);
                if(notifyParent){
                    mAMC.notifyItemChanged(preCount);
                }
                return true;
            }
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
    public void setItem(int parentIndex, T item) {
        int preCount = getPreviousCount(parentIndex);
        T oldItem = getItem(parentIndex);
        int oldCount = computeItemCount(oldItem);
        int newCount = computeItemCount(item);
        mDatas.set(parentIndex, item);
        if(oldCount == newCount){
            mAMC.notifyItemRangeChanged(preCount + 1, oldCount);
        }else {
            mAMC.notifyItemRangeRemoved(preCount + 1, oldCount);
            mAMC.notifyItemRangeInserted(preCount + 1, newCount);
        }
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
            return ((ExpendableAdapter.ExpendableItem) t).getChildItems().size() > 0;
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
            return wrapItem(t).getChildItem(childPos);
        }
        return null;
    }

    @Override
    public Object getChildItem(int realPosition){
        int[] positions = getParentPositions(realPosition, null);
        if(positions == null || positions[1] == -1){
            return null;
        }
        return wrapItem(getItem(positions[0])).getChildItem(positions[1]);
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
                    delta = ei.getChildItems().size();
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
                    total += ei.getChildItems().size();
                }
            }
        }
        return total;
    }

    public static <T> int computeItemCount(T t){
        int count = 1;
        if (t instanceof ExpendableAdapter.ExpendableItem) {
            ExpendableAdapter.ExpendableItem ei = (ExpendableAdapter.ExpendableItem) t;
            if (ei.isExpended()) {
                count+= ei.getChildItems().size();
            }
        }
        return count;
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
    private ExpendItemWrapper wrapItem(T t){
        return new ExpendItemWrapper((ExpendableAdapter.ExpendableItem)t);
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
    private static class ExpendItemWrapper implements ExpendableAdapter.ExpendableItem {
        final ExpendableAdapter.ExpendableItem item;

        public ExpendItemWrapper(ExpendableAdapter.ExpendableItem item) {
            this.item = item;
        }
        public int getChildItemCount(){
            return item.getChildItems().size();
        }
        public Object getChildItem(int childIndex){
            return item.getChildItems().get(childIndex);
        }
        public void addChildItems(int childStartIndex, List<?> list){
            item.getChildItems().addAll(childStartIndex, (Collection) list);
        }
        public void removeChildItems(int childStartIndex, int count){
            List<?> items = item.getChildItems();
            int maxIndex = childStartIndex + count; //exclude
            for (int size = items.size(), i = size ;  i >= 0  ; i --){
                if(i >= childStartIndex && i < maxIndex){
                    items.remove(i);
                }
                if(i < childStartIndex){
                    break;
                }
            }
        }
        //count = -1 . means all after.
        public List<?> getChildItems(int childStartIndex, int count){
            if(count < 0){
                count = item.getChildItems().size() - childStartIndex;
            }
             return item.getChildItems().subList(childStartIndex, childStartIndex + count);
        }
        @Override
        public boolean isExpended() {
            return item.isExpended();
        }
        @Override
        public void setExpended(boolean expend) {
            item.setExpended(expend);
        }
        @Override
        public List<?> getChildItems() {
            return item.getChildItems();
        }
    }
}
