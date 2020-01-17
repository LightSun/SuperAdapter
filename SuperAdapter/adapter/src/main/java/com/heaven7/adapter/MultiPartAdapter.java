package com.heaven7.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.heaven7.adapter.util.ViewHelper2;

import java.util.ArrayList;
import java.util.List;

/**
 * the multi part delegate, often used to adapt multi part.
 * @author heaven7
 * @since 2.0.9
 */
public class MultiPartAdapter extends HeaderFooterAdapter implements HeaderFooterAdapter.Callback {

    private final MultiPartManager mMPM;
    /**
     * create adapter by space part and multi other parts
     * @param spacePart the space part, can be null.
     * @param parts the parts
     */
    public MultiPartAdapter(@Nullable PartDelegate spacePart, List<? extends PartDelegate> parts) {
        this.mMPM = new MultiPartManager(this, spacePart, parts);
        setCallback(this);
    }
    public MultiPartAdapter(PartDelegate spacePart){
        this(spacePart, new ArrayList<PartDelegate>());
    }

    public MultiPartManager getMultiPartManager(){
        return mMPM;
    }
    /**
     * set the parts. this will auto notify data changed.
     * @param parts the parts
     * @since 2.1.0
     */
    public void setParts(List<? extends PartDelegate> parts){
        mMPM.setParts(parts);
    }
    /**
     * get the index of item.
     * @param item the item
     * @return the index of item. or -1 mean not found.
     * @since 2.1.0
     */
    public int indexOf(ISelectable item) {
        return mMPM.indexOf(item);
    }
    public List<? extends PartDelegate> getParts() {
        return mMPM.getParts();
    }
    /**
     * get the space part
     * @return the space part
     * @since 2.1.0
     */
    public PartDelegate getSpacePart(){
        return mMPM.getSpacePart();
    }

    protected void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        PartDelegate part = mMPM.findPart(position);
        int partPosition = mMPM.getPartPosition(position);
        part.onBindData(this, vh.getContext(), partPosition, vh.getViewHelper());
    }
    @Override
    protected int getItemViewTypeImpl(HeaderFooterHelper hfHelper, int position) {
        PartDelegate part = mMPM.findPart(position);
        return part.getLayoutId();
    }

    @Override
    public int getActuallyItemSize() {
        return mMPM.getItemCount();
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
