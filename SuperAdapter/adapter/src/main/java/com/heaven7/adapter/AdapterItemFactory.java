package com.heaven7.adapter;

/**
 * the adapter item factory
 * @param <T> the data type
 * @since 2.1.1
 */
public interface AdapterItemFactory<T> {

    /**
     * called on create adapter item
     * @param t the item data
     * @return the adapter item.
     */
    BaseAdapterItem<T> createAdapterItem(T t);

    /**
     * <p>This can only be called for multi item types. </p>
     * called on get the item unique id for every data from list.
     * if multi data's id is the same. that means they will use the same {@linkplain BaseAdapterItem}.
     * @param data the data
     * @return the unique id for data
     */
    String getItemTypeId(T data);

}
