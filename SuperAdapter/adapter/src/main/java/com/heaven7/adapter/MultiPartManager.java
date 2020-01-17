package com.heaven7.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * the multi part manager
 * @since 2.1.0
 */
public final class MultiPartManager {

    private final List<MultiPartAdapter.PartDelegate> mParts;
    private final MultiPartAdapter.PartDelegate mSpace;
    private final IPartUpdater mUpdater;

    public MultiPartManager(IPartUpdater updater, MultiPartAdapter.PartDelegate mSpace, List<? extends MultiPartAdapter.PartDelegate> parts) {
        this.mUpdater = updater;
        this.mSpace = mSpace;
        this.mParts = parts != null ? new ArrayList<>(parts) : new ArrayList<MultiPartAdapter.PartDelegate>();
    }
    public List<? extends MultiPartAdapter.PartDelegate> getParts() {
        return mParts;
    }
    /**
     * get the space part
     * @return the space part
     */
    public MultiPartAdapter.PartDelegate getSpacePart(){
        return mSpace;
    }
    /**
     * get the index of item.
     * @param item the item
     * @return the index of item. or -1 mean not found.
     */
    public int indexOf(ISelectable item) {
        int index = 0;
        final int size = mParts.size();
        for (int i = 0; i < size; i++) {
            MultiPartAdapter.PartDelegate part = mParts.get(i);
            int ind = part.getItems().indexOf(item);
            if(ind >= 0){
                return index + ind;
            }
            index += part.getItems().size();
            if(hasSpacePart()){
                if(i != size - 1){
                    index += 1;
                }
            }
        }
        return -1;
    }
    public int getItemCount() {
        final int size = mParts.size();
        int total = 0;
        for (int i = 0; i < size; i++) {
            total += mParts.get(i).getItems().size();
        }
        return total + (hasSpacePart() ? size - 1 : 0);
    }
    public MultiPartAdapter.PartDelegate findPart(int position) {
        int index = 0;
        final int size = mParts.size();
        for (int i = 0; i < size; i++) {
            MultiPartAdapter.PartDelegate part = mParts.get(i);
            int delta = part.getItems().size();

            if (position >= index && position < index + delta) {
                return part;
            }
            index += delta;
            if(hasSpacePart()){
                if(position == index){
                    return mSpace;
                }
                if(i != size - 1){
                    index += 1;
                }
            }
        }
        throw new IllegalStateException("can't find part for position = "+ position);
    }
    public int getPartPosition(int position) {
        int index = 0;
        final int size = mParts.size();
        for (int i = 0; i < size; i++) {
            MultiPartAdapter.PartDelegate part = mParts.get(i);
            int delta = part.getItems().size();
            if (position >= index && position < index + delta) {
                return position - index;
            }
            index += delta;
            //check if has space
            if(hasSpacePart()){
                if(position == index){
                    return 0;
                }
                if(i != size - 1){
                    index += 1;
                }
            }
        }
        throw new IllegalStateException("can't find part for position = "+ position);
    }
    public boolean hasSpacePart(){
        return mSpace != null;
    }
    public void setParts(List<? extends MultiPartAdapter.PartDelegate> parts) {
        this.mParts.clear();
        this.mParts.addAll(parts);
        mUpdater.notifyDataSetChanged();
    }
}
