package com.heaven7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.heaven7.adapter.util.ViewHelper2;

import java.util.ArrayList;
import java.util.List;

/**
 * the multi part delegate, often used to adapt multi part.
 * @author heaven7
 * @since 2.0.9
 */
public class MultiPartAdapter extends HeaderFooterAdapter {

    private final PartDelegate mSpace;
    private final List<? extends PartDelegate> mParts;

    public MultiPartAdapter(PartDelegate spacePart, List<? extends PartDelegate> parts) {
        setCallback(new Callback() {
            @Override
            public int getActuallyItemSize() {
                return getItemCount0();
            }
        });
        this.mSpace = spacePart;
        this.mParts = parts != null ? parts : new ArrayList<PartDelegate>();
    }
    public MultiPartAdapter(PartDelegate spacePart){
        this(spacePart, new ArrayList<PartDelegate>());
    }

    public List<? extends PartDelegate> getParts() {
        return mParts;
    }

    protected void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        PartDelegate part = findPart(position);
        int partPosition = getPartPosition(position);
        part.onBindData(this, vh.getContext(), partPosition, vh.getViewHelper());
    }
    @Override
    protected int getItemViewTypeImpl(HeaderFooterHelper hfHelper, int position) {
        PartDelegate part = findPart(position);
        return part.getLayoutId();
    }

    private int getItemCount0() {
        final int size = mParts.size();
        int total = 0;
        for (int i = 0; i < size; i++) {
            total += mParts.get(i).getItems().size();
        }
        return total + size - 1;
    }

    private PartDelegate findPart(int position) {
        int index = 0;
        final int size = mParts.size();
        for (int i = 0; i < size; i++) {
            PartDelegate part = mParts.get(i);
            int delta = part.getItems().size();

            if (position >= index && position < index + delta) {
                return part;
            }
            index += delta;
            if(position == index){
                return mSpace;
            }
            if(i != size - 1){
                index += 1;
            }
        }
        throw new IllegalStateException("can't find part for position = "+ position);
    }
    private int getPartPosition(int position) {
        int index = 0;
        final int size = mParts.size();
        for (int i = 0; i < size; i++) {
            PartDelegate part = mParts.get(i);
            int delta = part.getItems().size();
            if (position >= index && position < index + delta) {
                return position - index;
            }
            index += delta;
            if(position == index){
                return 0;
            }
            if(i != size - 1){
                index += 1;
            }
        }
        throw new IllegalStateException("can't find part for position = "+ position);
    }

    /**
     * the part behaviour delegate
     */
    public interface PartDelegate {

        /**
         * get the items
         * @return the items
         */
        List<? extends ISelectable> getItems();

        /**
         * get the layout id of this part
         * @return the layout id
         */
        int getLayoutId();

        /**
         * called on bind data
         * @param adapter the multi part adapter
         * @param context the context
         * @param partPosition the position
         * @param helper the view helper.
         */
        void onBindData(MultiPartAdapter adapter, Context context, int partPosition, ViewHelper2 helper);
    }
}
