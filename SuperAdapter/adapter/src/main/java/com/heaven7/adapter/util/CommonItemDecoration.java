package com.heaven7.adapter.util;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * the common item decoration
 * @author heaven7
 * @since 2.1.9
 */
public class CommonItemDecoration extends RecyclerView.ItemDecoration {

    private final PaddingCallback mCallback;
    private DrawCallback mDrawCallback;
    private final Rect mItemPaddingRect;

    private final Set<Integer> mShouldPadPositions = new HashSet<>();
    private final Rect mTempRect = new Rect();

    public CommonItemDecoration(PaddingCallback mCallback, Rect mItemPaddingRect) {
        this.mCallback = mCallback;
        this.mItemPaddingRect = mItemPaddingRect;
    }

    public DrawCallback getDrawCallback() {
        return mDrawCallback;
    }
    public void setDrawCallback(DrawCallback mDrawCallback) {
        this.mDrawCallback = mDrawCallback;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if(position == 0){
            mShouldPadPositions.clear();
        }
        RecyclerView.Adapter adapter = parent.getAdapter();

        int headerCount = 0;
        AdapterDelegate ad  = null;
        List list = null;
        if(adapter instanceof AdapterDelegate){
            ad = (AdapterDelegate) adapter;
            headerCount = ad.getHeaderSize();
            list = ad.getListItems();
            //only need once
            if(position == 0){
                for (int i = 0 ; i < list.size() ; i ++){
                    if(mCallback.shouldPadding(this, headerCount, i, list.get(i), list)){
                        mShouldPadPositions.add(i);
                    }
                }
            }
        }
        if(position >= headerCount){
            if(ad != null && mShouldPadPositions.contains(position - headerCount)
                    && mCallback.getPadding(this, mTempRect, headerCount, position - headerCount,
                    list.get(position - headerCount), list)){
                outRect.set(mTempRect);
            }else {
                outRect.set(mItemPaddingRect);
            }
        }else {
            outRect.set(0, 0,0 ,0);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if(mDrawCallback != null && adapter instanceof AdapterDelegate){
            final int headerCount = ((AdapterDelegate) adapter).getHeaderSize();
            final List datas = ((AdapterDelegate) adapter).getListItems();

            int childCount = parent.getChildCount();
            for (int i = 0 ; i < childCount ; i ++) {
                View child = parent.getChildAt(i);
                int pos = parent.getChildAdapterPosition(child);
                if(pos >= headerCount){
                    Object obj = datas.get(pos - headerCount);
                    if(mDrawCallback.shouldDraw(this, headerCount, pos- headerCount, obj, datas)){
                        mDrawCallback.onDraw(this, c, child, headerCount, pos- headerCount, obj, datas);
                    }
                }
            }
        }
    }
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if(mDrawCallback != null && adapter instanceof AdapterDelegate){
            final int headerCount = ((AdapterDelegate) adapter).getHeaderSize();
            final List datas = ((AdapterDelegate) adapter).getListItems();

            int childCount = parent.getChildCount();
            for (int i = 0 ; i < childCount ; i ++) {
                View child = parent.getChildAt(i);
                int pos = parent.getChildAdapterPosition(child);
                if(pos >= headerCount){
                    Object obj = datas.get(pos - headerCount);
                    if(mDrawCallback.shouldDrawOVer(this, headerCount, pos- headerCount, obj,  datas)){
                        mDrawCallback.onDrawOver(this, c, child, headerCount, pos- headerCount, obj, datas);
                    }
                }
            }
        }
    }
    /**
     * the adapter delegate used for decoration
     */
    public interface AdapterDelegate{
        /**
         * get the list items
         * @return the items
         */
        List getListItems();
        /**
         * get the header count
         * @return the header count
         */
        int getHeaderSize();
    }

    /**
     * the padding callback
     */
    public interface PaddingCallback {
        /**
         * indicate whether to padding item or not.
         *
         * @param decoration the item decoration
         * @param headerCount the header count
         * @param index the item index, exclude header
         * @param item the item
         * @param items the items
         * @return true if should padding
         */
        boolean shouldPadding(CommonItemDecoration decoration, int headerCount, int index, Object item, List items);

        /**
         * get the item padding
         *
         * @param decoration the item decoration
         * @param out the out rect
         * @param headerCount the headerCount
         * @param index the index,  exclude header
         * @param item the item
         * @param items the items
         * @return true if success.
         */
        boolean getPadding(CommonItemDecoration decoration, Rect out, int headerCount, int index, Object item, List items);
    }

    /**
     * the draw callback
     */
    public interface DrawCallback{
        /**
         * indicate whether to draw extra for item or not.
         *
         *
         * @param decoration the item decoration
         * @param headerCount the header count
         * @param index the item index, exclude header
         * @param item the item
         * @param items the items
         * @return true if should padding
         */
        boolean shouldDraw(CommonItemDecoration decoration, int headerCount, int index, Object item, List items);

        /**
         * indicate whether to draw over extra for item or not.
         *
         *
         * @param decoration the item decoration
         * @param headerCount the header count
         * @param index the item index, exclude header
         * @param item the item
         * @param items the items
         * @return true if should padding
         */
        boolean shouldDrawOVer(CommonItemDecoration decoration, int headerCount, int index, Object item, List items);

        /**
         *  draw extra for child view.
         * @param decoration the item decoration
         * @param c the canvas
         * @param child the child view
         * @param headerCount the header count
         * @param index the item index, exclude header
         * @param item the item
         * @param items the items
         */
        void onDraw(CommonItemDecoration decoration, Canvas c, View child, int headerCount, int index, Object item, List items);


        /**
         * draw over extra for child view.
         * @param decoration the item decoration
         * @param c the canvas
         * @param child the child view
         * @param headerCount the header count
         * @param index the item index, exclude header
         * @param item the item
         * @param items the items
         */
        void onDrawOver(CommonItemDecoration decoration, Canvas c, View child, int headerCount, int index, Object item, List items);
    }
}
