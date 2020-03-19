package com.heaven7.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * the diff manager which wrap {@linkplain DiffUtil} and {@linkplain AsyncListDiffer}.
 * @param <T> the data type
 * @since 2.1.2
 */
public final class DiffManager<T> {

    private final ItemCallback<T> mCallback;
    private final ListUpdateCallback mDiffCallback;
    private final List<T> mDatas;
    private AsyncListDiffer<T> mAsyncDiffer;

    private final List<T> mOldList = new ArrayList<>();

    /**
     * create diff manager with item callback and list update callback
     * @param callback the item callback
     * @param listCallback the list callback
     * @param mDatas the datas
     */
    public DiffManager(ItemCallback<T> callback, ListUpdateCallback listCallback, List<T> mDatas) {
        this.mCallback = callback;
        this.mDatas = mDatas != null ? new ArrayList<T>(mDatas) : new ArrayList<T>();
        this.mDiffCallback = listCallback;
    }
    /**
     * set async diff executor for {@linkplain AsyncListDiffer}.
     * @param workerThreadExecutor the worker thread executor
     */
    public void setAsyncDiffExecutor(@Nullable Executor workerThreadExecutor){
        setAsyncDiffExecutor(workerThreadExecutor, null);
    }
    /**
     * set async diff executor for {@linkplain AsyncListDiffer}.
     * @param workerThreadExecutor the worker thread executor. null means clear async mode.
     * @param listener the list listener
     * @see #isAsyncMode()
     */
    public void setAsyncDiffExecutor(@Nullable Executor workerThreadExecutor, @Nullable AsyncListDiffer.ListListener<T> listener) {
        //if previous have async differ. replace to new data
        if(mAsyncDiffer != null){
            mDatas.clear();
            mDatas.addAll(mAsyncDiffer.getCurrentList());
        }
        //set new differ
        if(workerThreadExecutor != null){
            mAsyncDiffer = new AsyncListDiffer<T>(mDiffCallback, new AsyncDifferConfig.Builder<T>(mCallback)
                    .setBackgroundThreadExecutor(workerThreadExecutor)
                    .build());
            if(listener != null){
                mAsyncDiffer.addListListener(listener);
            }
            mAsyncDiffer.submitList(mDatas);
        }else {
            mAsyncDiffer = null;
        }
    }
    /**
     * indicate diff manager is work with async mode or not.
     * @return true if is async.
     * @see AsyncListDiffer
     */
    public boolean isAsyncMode(){
        return mAsyncDiffer != null;
    }

    /**
     * remove list listener. this only used for async mode.
     * @param listListener the list listener
     * @see #isAsyncMode()
     */
    public void removeListListener(AsyncListDiffer.ListListener<T> listListener){
        if(mAsyncDiffer != null){
            mAsyncDiffer.removeListListener(listListener);
        }
    }

    /**
     * get the old list data. this is only valid when commit doing or else empty.
     * @return the previous list
     */
    public List<T> getPreviousList(){
        return mOldList;
    }
    /**
     * commit the new list to diff manager. if is async mode . it will use {@linkplain AsyncListDiffer}.
     * @param newList the new list to commit
     * @param commitCallback the commit callback. this is called after commit ok. can be null.
     * @see #isAsyncMode()
     * @see AsyncListDiffer
     */
    public void commit(List<T> newList, Runnable commitCallback){
        commitCallback = new PostCommitTask(commitCallback);
        //save old data.
        mOldList.clear();
        mOldList.addAll(getCurrentList());
        if(mAsyncDiffer != null){
            mAsyncDiffer.submitList(newList, commitCallback);
        }else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new Judger<T>(mCallback, mDatas, newList), true);
            result.dispatchUpdatesTo(mDiffCallback);
            mDatas.clear();
            mDatas.addAll(newList);
            commitCallback.run();
        }
    }
    /**
     * commit the new list to diff manager. if is async mode . it will use {@linkplain AsyncListDiffer}.
     * @param newList the new list to commit
     * @see #isAsyncMode()
     * @see AsyncListDiffer
     */
    public void commit(List<T> newList){
        commit(newList, null);
    }

    /**
     * get the item at target position
     * @param position the position
     * @return the item data
     */
    public T getItem(int position) {
        if(mAsyncDiffer != null){
            return mAsyncDiffer.getCurrentList().get(position);
        }
        return mDatas.get(position);
    }
    /**
     * get the real data size
     * @return the item count.
     */
    public int getItemCount() {
        if(mAsyncDiffer != null){
            return mAsyncDiffer.getCurrentList().size();
        }
        return mDatas.size();
    }
    /**
     * get the current data list. and this list is read only.
     * @return the read only list
     */
    public List<T> getCurrentList() {
        if(mAsyncDiffer != null){
            return mAsyncDiffer.getCurrentList();
        }
        return Collections.unmodifiableList(mDatas);
    }

    private class PostCommitTask implements Runnable{
        final Runnable base;
        public PostCommitTask(@Nullable Runnable base) {
            this.base = base;
        }
        @Override
        public void run() {
            mOldList.clear();
            if(base != null){
                base.run();
            }
        }
    }

    private static class Judger<T> extends DiffUtil.Callback {

        private final ItemCallback<T> callback;
        private final List<T> oldList;
        private final List<T> newList;

        public Judger(ItemCallback<T> callback, List<T> oldList, List<T> newList) {
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
        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            T t1 = oldList.get(oldItemPosition);
            T t2 = newList.get(newItemPosition);
            return callback.getChangePayload(t1, t2);
        }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     * <p>
     * {@link DiffUtil.Callback} serves two roles - list indexing, and item diffing. ItemCallback handles
     * just the second of these, which allows separation of code that indexes into an array or List
     * from the presentation-layer and content specific diffing code.
     *
     * @param <T> Type of items to compare.
     */
    public static abstract class ItemCallback<T> extends DiffUtil.ItemCallback<T>{
    }
}
