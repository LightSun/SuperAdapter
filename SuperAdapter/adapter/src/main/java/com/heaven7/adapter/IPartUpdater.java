package com.heaven7.adapter;

/**
 * the part updater. often used for manage recycler view items.
 * @author heaven7
 * @since 2.0.5
 */
public interface IPartUpdater {

    void notifyItemInserted(int position);

    void notifyItemChanged(int position);

    void notifyItemRemoved(int position);

    void notifyItemMoved(int fromPosition, int toPosition);

    void notifyItemRangeChanged(int positionStart, int itemCount);

    void notifyItemRangeInserted(int positionStart, int itemCount);

    void notifyItemRangeRemoved(int positionStart, int itemCount);

    void notifyDataSetChanged();
}
