package com.heaven7.adapter;

import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

/**
 * the adapter update callback with header
 * @author heaven7
 * @since 2.1.2
 */
/*private*/ final class AdapterListUpdateCallback implements ListUpdateCallback {

    private final RecyclerView.Adapter mAdapter;
    private final AdapterManager.IHeaderFooterManager hfm;

    public AdapterListUpdateCallback(RecyclerView.Adapter adapter, AdapterManager.IHeaderFooterManager hfm) {
        this.mAdapter = adapter;
        this.hfm = hfm;
    }

    @Override
    public void onInserted(int position, int count) {
        mAdapter.notifyItemRangeInserted(position + hfm.getHeaderSize(), count);
    }

    @Override
    public void onRemoved(int position, int count) {
        mAdapter.notifyItemRangeRemoved(position + hfm.getHeaderSize(), count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        mAdapter.notifyItemMoved(fromPosition + hfm.getHeaderSize(), toPosition);
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        mAdapter.notifyItemRangeChanged(position + hfm.getHeaderSize(), count, payload);
    }
}