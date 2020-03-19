package com.heaven7.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.List;

/**
 * the list data update callback which callback by {@linkplain ListUpdateCallback}.
 * @param <T> the data type
 * @author heaven7
 * @since 2.1.2
 */
public interface ListDataUpdateCallback<T> {

    /**
     * Called when {@code count} number of items are inserted at the given position.
     *
     * @param startPosition The start position of the new item.
     * @param data   The items that have been added.
     */
    void onInserted(int startPosition, List<T> data);

    /**
     * Called when {@code count} number of items are removed at the given position.
     *
     * @param startPosition The start position of the new item.
     * @param data   The items that have been removed.
     */
    void onRemoved(int startPosition, List<T> data);

    /**
     * Called when {@code count} number of items are moved at the given position.
     *
     * @param fromPosition The from position of the new item.
     * @param toPosition The to position of the new item.
     * @param data   The item that have been moved.
     */
    void onMoved(int fromPosition, int toPosition, T data);

    /**
     * Called when {@code count} number of items are changed at the given position.
     *
     * @param startPosition The start position of the new item.
     * @param data   The items that have been changed.
     * @param payload   the payload.
     */
    void onChanged(int startPosition, List<T> data, @Nullable Object payload);
}
