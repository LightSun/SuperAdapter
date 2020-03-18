package com.heaven7.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * the diff manager which wrap {@linkplain DiffUtil} and {@linkplain AsyncListDiffer}.
 * @param <T> the data type
 * @since 2.1.2
 */
public final class DiffManager<T> {

    private final ItemJudger<T> mCallback;
    private final ListUpdateCallback mDiffCallback;
    private final List<T> mDatas;
    private AsyncListDiffer<T> mAsyncDiffer;

    public DiffManager(ItemJudger<T> callback, ListUpdateCallback diffCb, List<T> mDatas) {
        this.mCallback = callback;
        this.mDatas = mDatas != null ? new ArrayList<T>(mDatas) : new ArrayList<T>();
        this.mDiffCallback = diffCb;
    }
    public void setAsyncDiffExecutor(@Nullable Executor executor, @Nullable AsyncListDiffer.ListListener<T> listener) {
        mAsyncDiffer = new AsyncListDiffer<T>(mDiffCallback, new AsyncDifferConfig.Builder<T>(mCallback)
                .setBackgroundThreadExecutor(executor)
                .build());
        if(listener != null){
            mAsyncDiffer.addListListener(listener);
        }
        mAsyncDiffer.submitList(mDatas);
    }
    public void commit(List<T> newList, boolean detectMove){
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new Judger<T>(mCallback, mDatas, newList), detectMove);
        result.dispatchUpdatesTo(mDiffCallback);
        mDatas.clear();
        mDatas.addAll(newList);
    }
    public void commit(List<T> newList){
        commit(newList, true);
    }
    public void commitAsync(List<T> newList, Runnable commitCallback){
        if(mAsyncDiffer == null){
            throw new IllegalStateException("you must call #setAsyncDifferConfig(...) first!");
        }
        mAsyncDiffer.submitList(newList, commitCallback);
    }
    public void commitAsync(List<T> newList){
        if(mAsyncDiffer == null){
            throw new IllegalStateException("you must call #setAsyncDifferConfig(...) first!");
        }
        mAsyncDiffer.submitList(newList, null);
    }
    public T getItem(int position) {
        if(mAsyncDiffer != null){
            return mAsyncDiffer.getCurrentList().get(position);
        }
        return mDatas.get(position);
    }
    public int getItemCount() {
        if(mAsyncDiffer != null){
            return mAsyncDiffer.getCurrentList().size();
        }
        return mDatas.size();
    }
    public List<T> getCurrentList() {
        if(mAsyncDiffer != null){
            return mAsyncDiffer.getCurrentList();
        }
        return mDatas;
    }

    private static class Judger<T> extends DiffUtil.Callback {

        private final ItemJudger<T> callback;
        private final List<T> oldList;
        private final List<T> newList;

        public Judger(ItemJudger<T> callback, List<T> oldList, List<T> newList) {
            this.callback = callback;
            this.oldList = oldList;
            this.newList = newList;
        }
        @Override
        public int getOldListSize() {
            return oldList.size();
        }
        @Override
        public int getNewListSize() {
            return newList.size();
        }
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            T t1 = oldList.get(oldItemPosition);
            T t2 = newList.get(newItemPosition);
            return callback.areItemsTheSame(t1, t2);
        }
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            T t1 = oldList.get(oldItemPosition);
            T t2 = newList.get(newItemPosition);
            return callback.areContentsTheSame(t1, t2);
        }
    }
    public static abstract class ItemJudger<T> extends DiffUtil.ItemCallback<T>{
    }
}
