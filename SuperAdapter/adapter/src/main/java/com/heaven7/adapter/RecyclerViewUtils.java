package com.heaven7.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * the util class of RecyclerView
 * Created by heaven7 on 2016/8/11.
 */
public final class RecyclerViewUtils {

    /**
     * return the last visible item position or -1 for unknown LayoutManager.
     * @param rv the RecyclerView
     * @return the last visible item position or -1 for unknown LayoutManager
     */
    public static int findLastVisibleItemPosition(RecyclerView rv) {
        RecyclerView.LayoutManager lm = rv.getLayoutManager();
        int lastVisibleItemPosition = RecyclerView.NO_POSITION ;
        if (lm instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) lm).findLastVisibleItemPosition();

        } else if (lm instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) lm).findLastVisibleItemPosition();

        } else if (lm instanceof StaggeredGridLayoutManager) {
            int positions[] =  ((StaggeredGridLayoutManager) lm).findLastVisibleItemPositions(null);
            for(int pos : positions){
                if(pos > lastVisibleItemPosition){
                    lastVisibleItemPosition = pos;
                }
            }
        }
        return lastVisibleItemPosition;
    }
    /**
     * return the first visible item position or -1 for unknown LayoutManager.
     * @param rv the RecyclerView
     * @return the first visible item position or -1 for unknown LayoutManager
     * @since 2.0.7
     */
    public static int findFirstVisibleItemPosition(RecyclerView rv) {
        RecyclerView.LayoutManager lm = rv.getLayoutManager();
        int position = Integer.MAX_VALUE ;
        if (lm instanceof GridLayoutManager) {
            position = ((GridLayoutManager) lm).findFirstVisibleItemPosition();

        } else if (lm instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();

        } else if (lm instanceof StaggeredGridLayoutManager) {
            int positions[] =  ((StaggeredGridLayoutManager) lm).findFirstVisibleItemPositions(null);
            for(int pos : positions){
                if(pos < position){
                    position = pos;
                }
            }
        }
        return position != Integer.MAX_VALUE ?  position : RecyclerView.NO_POSITION;
    }

    /**
     * create FullSpannableStaggeredGridLayoutManager and set ISpanSizeLookupHelper for full span of header/footer.
     * @param hfm the header/footer manager
     * @param spanCount the really span count of layoutManager
     * @param orientation the orientation of StaggeredGridLayoutManager.
     * @return a instance of FullSpannableStaggeredGridLayoutManager.
     */
    public static FullSpannableStaggeredGridLayoutManager createStaggeredGridLayoutManager(
            AdapterManager.IHeaderFooterManager hfm, int spanCount, int orientation){
        FullSpannableStaggeredGridLayoutManager lm = new FullSpannableStaggeredGridLayoutManager(spanCount,orientation);
        lm.setSpanSizeLookupHelper(new HeaderFooterSpanSizeLookUp(hfm,spanCount));
        return lm;
    }
    /**
     * create GridLayoutManager and set SpanSizeLookup for full span of header/footer.
     * @param hfm the header/footer manager
     * @param spanCount the really span count of layoutManager
     * @param context the context.
     * @return a instance of GridLayoutManager.
     */
    public static GridLayoutManager createGridLayoutManager(AdapterManager.IHeaderFooterManager hfm,
                                                            Context context, int spanCount){
        GridLayoutManager lm = new GridLayoutManager(context,spanCount);
        lm.setSpanSizeLookup(new HeaderFooterSpanSizeLookUp(hfm,spanCount));
        return lm;
    }

    /*
     * get the whole scroll y distance of recycler view
     * @param rv the recycler view
     * @param strictly true if you want to get it strictly. for false it only support the items height are the same.
     * @return the scroll y
     * @since 2.1.0
     */
    /*public static int getScrollY(RecyclerView rv, boolean strictly) {
        RecyclerView.LayoutManager lm = rv.getLayoutManager();
        int firstVisiblePosition = findFirstVisibleItemPosition(rv);
        View child = lm.findViewByPosition(firstVisiblePosition);
        final int itemHeight;
        ViewGroup.LayoutParams clp = child.getLayoutParams();
        if(clp instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams vmp = (ViewGroup.MarginLayoutParams)clp;
            itemHeight = child.getHeight() + vmp.topMargin + vmp.bottomMargin;
        }else {
            itemHeight = child.getHeight();
        }
        if(firstVisiblePosition == 0){
            return child.getTop();
        }
        if(strictly){
            if(lm instanceof GridLayoutManager){
                GridLayoutManager glm = (GridLayoutManager) lm;
                if(glm.getOrientation() == LinearLayoutManager.VERTICAL){
                    int spanCount = glm.getSpanCount();
                    int preRowCount = firstVisiblePosition / spanCount;
                    return preRowCount * itemHeight - child.getTop();
                }else {
                    return child.getTop();
                }
            }else if(lm instanceof LinearLayoutManager){
                if(((LinearLayoutManager) lm).getOrientation() == LinearLayoutManager.VERTICAL){
                    return (firstVisiblePosition) * itemHeight - child.getTop();
                }
                return child.getTop();
            }else if(lm instanceof StaggeredGridLayoutManager){
                StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager)lm;
                if(sglm.getOrientation() == StaggeredGridLayoutManager.VERTICAL){
                    int spanCount = sglm.getSpanCount();
                    int preRowCount = firstVisiblePosition / spanCount;
                    if(preRowCount == 0){
                        return child.getTop();
                    }
                    int preHeight = 0;
                    for (int i = 0 ; i < preRowCount ; i ++){
                        int start = i * spanCount;
                        for (int k = 0 ; k < spanCount ; k++ ){
                            // lm.findViewByPosition()
                        }
                    }
                }
                return 0;
            }else {
                throw new UnsupportedOperationException("un support layout manager");
            }
        }
        return (firstVisiblePosition) * itemHeight - child.getTop();
    }*/
}
