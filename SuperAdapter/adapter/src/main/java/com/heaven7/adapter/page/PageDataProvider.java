package com.heaven7.adapter.page;

import android.content.Context;

/**
 * the data provider
 * @param <T> the data type
 * @author heaven7
 * @since 2.1.2
 */
public abstract class PageDataProvider<T> extends BasePageProvider{

    public PageDataProvider(Context context) {
        super(context);
    }
    /**
     * get the real item count.
     * @return the item count.
     */
    public abstract int getItemCount();
    /**
     * get the real position actually
     * @param position the position
     * @return the real position for input position
     */
    public int getPositionActually(int position){
        return position % getItemCount();
    }

    /**
     * get the item data from target position, which is from {@linkplain #getPositionActually(int)}
     * @param realPos the real position. 
     * @return the data
     */
    public abstract T getItem(int realPos);
}