package com.heaven7.adapter.page;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * the view page view provider
 * @param <T> the data type.
 * @since 2.1.2
 * @author heaven7
 */
public abstract class PageViewProvider<T> extends BasePageProvider{

    public PageViewProvider(Context context) {
        super(context);
    }

    /**
     * called this to determinate if override the default page width.
     * @return true if need. default is false.
     * @see #getPageWidth(int, int, Object)
     */
    public boolean shouldOverridePageWidth() {
        return false;
    }
    /**
     * Returns the proportional width of a given page as a percentage of the
     * ViewPager's measured width from (0.f-1.f]
     *
     * @param position The position of the page requested
     * @param realPosition the real position. see {@linkplain PageDataProvider#getPositionActually(int)}
     * @param data the data
     * @return Proportional width for the given page position
     */
    public float getPageWidth(int position, int realPosition,T data) {
        return 1.f;
    }
    /**
     * called on create the view. you can bind click event here.
     *
     * @param parent the parent view
     * @param position the position item from view pager
     * @param realPosition the real position . see {@linkplain PageDataProvider#getPositionActually(int)}
     * @param data the item data
     * @return the view
     */
    public abstract View createItemView(ViewGroup parent, int position, int realPosition, T data);

    /**
     * called on set data to view .
     * @param v the view which is create by {@linkplain #createItemView(ViewGroup, int,int, Object)}
     * @param position the position
     * @param realPosition the real position . see {@linkplain PageDataProvider#getPositionActually(int)}
     * @param data the data
     */
    public abstract void onBindItemView(View v, int position, int realPosition,T data);
    /**
     * called on destroy item view
     * @param v the view. which is create by {@linkplain #createItemView(ViewGroup, int,int, Object)}
     * @param position the position
     * @param realPosition the real position . see {@linkplain PageDataProvider#getPositionActually(int)}
     * @param data the item
     */
    public void onDestroyItemView(View v, int position, int realPosition,T data){

    }

}
