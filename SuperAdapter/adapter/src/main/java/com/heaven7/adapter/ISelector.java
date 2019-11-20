package com.heaven7.adapter;

import java.util.List;

/**
 * the selector. unlike the {@linkplain ISelectHelper}.
 * @param <T> the data type
 * @author heaven7
 * @since 2.0.7
 */
public interface ISelector<T> {

    /**
     * set select items
     *
     * @param items the all select items to init.
     */
    void setSelectItems(List<T> items);

    /**
     * select the target item.
     * @param item the item to selected.
     * @return true if operate success.
     */
    boolean select(T item);

    /**
     * unselect(cancel select) the target  item.
     *
     * @param item the item.
     * @return true if operate success.
     */
    boolean unselect(T item);

    /**
     * toggle the select state of the target item
     *
     * @param item the item
     * @return true if operate success.
     */
    boolean toggleSelect(T item);

    /**
     * clear the select items.
     * @param notify true to notify changed
     */
    void clear(boolean notify);

    /**
     * get the select position list.
     *
     * @return the list which contains the all select position.
     */
    List<T> getSelectItems();

    interface Callback<T>{
        void notifySelectState(List<T> selected, List<T> unselected);
    }
}
