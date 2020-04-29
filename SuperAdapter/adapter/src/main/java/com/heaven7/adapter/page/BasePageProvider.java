package com.heaven7.adapter.page;

import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.Nullable;

/**
 * the base Page Provider
 * @since 2.1.2
 * @author heaven7
 */
public abstract class BasePageProvider {

    private final Context context;
    private PageNotifier notifier;

    public BasePageProvider(Context context) {
        this.context = context;
    }

    public void setPageNotifier(PageNotifier notifier) {
        this.notifier = notifier;
    }

    public PageNotifier getPageNotifier(){
        return notifier;
    }

    public Context getContext() {
        return context;
    }

    public void notifyDataSetChanged(){
        if(notifier != null){
            notifier.notifyDataSetChanged();
        }
    }

    /**
     * called on destroy
     */
    public void onDestroy(){

    }

    /**
     * Save any instance state associated with this adapter and its pages that should be
     * restored if the current UI state needs to be reconstructed.
     *
     * @return Saved state for this adapter
     */
    @Nullable
    public Parcelable saveState() {
        return null;
    }
    /**
     * Restore any instance state associated with this adapter and its pages
     * that was previously saved by {@link #saveState()}.
     *
     * @param state State previously saved by a call to {@link #saveState()}
     * @param loader A ClassLoader that should be used to instantiate any restored objects
     */
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
    }

    public interface PageNotifier{
        void notifyDataSetChanged();
    }
}
