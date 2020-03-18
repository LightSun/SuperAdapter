package com.heaven7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.heaven7.adapter.util.ViewHelper2;
import com.heaven7.core.util.ViewHelper;

import java.util.ArrayList;

/**
 * the adapter just wrap the header and footer
 * <p>Note: Sub-class must call {@linkplain #setCallback(Callback)} before any call.</p>
 * @author heaven7
 * @since 2.0.5
 */
public abstract class HeaderFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements AdapterManager.IHeaderFooterManager, IPartUpdater, AdapterAttachStateManager{

    private ArrayList<AdapterAttachStateManager.OnAttachStateChangeListener> mStateListeners;
    private HeaderFooterHelper mHeaderFooterHelper;
    private Callback mCallback;

    /** must call this in constructor */
    protected void setCallback(Callback mCallback){
        this.mCallback = mCallback;
    }

    /**
     * get callback .
     * @return the callback
     * @since 2.1.2
     */
    protected Callback getCallback() {
        return mCallback;
    }
    /**
     * get the header footer helper
     * @return the header footer helper
     * @since 2.1.1
     */
    protected HeaderFooterHelper getHeaderFooterHelper(){
        return mHeaderFooterHelper;
    }
    @Override
    public void addHeaderView(View v) {
        if (mHeaderFooterHelper == null)
            mHeaderFooterHelper = new HeaderFooterHelper();
        int headerSize = getHeaderSize();
        mHeaderFooterHelper.addHeaderView(v);
        notifyItemInserted(headerSize);
    }

    @Override
    public void removeHeaderView(View v) {
        if (mHeaderFooterHelper != null) {
            int index = mHeaderFooterHelper.removeHeaderView(v);
            if (index != -1) {
                notifyItemRemoved(index);
            }
        }
    }

    @Override
    public void addFooterView(View v) {
        if (mHeaderFooterHelper == null)
            mHeaderFooterHelper = new HeaderFooterHelper();
        int itemCount = getItemCount();
        mHeaderFooterHelper.addFooterView(v);
        notifyItemInserted(itemCount);
    }

    @Override
    public void removeFooterView(View v) {
        if (mHeaderFooterHelper != null) {
            int index = mHeaderFooterHelper.removeFooterView(v);
            if (index != -1) {
                notifyItemRemoved(index + getHeaderSize() + mCallback.getActuallyItemSize());
            }
        }
    }

    @Override
    public int getHeaderSize() {
        return mHeaderFooterHelper == null ? 0 : mHeaderFooterHelper.getHeaderViewSize();
    }

    @Override
    public int getFooterSize() {
        return mHeaderFooterHelper == null ? 0 : mHeaderFooterHelper.getFooterViewSize();
    }

    @Override
    public boolean isHeader(int position) {
        return mHeaderFooterHelper != null && mHeaderFooterHelper.isInHeader(position);
    }

    @Override
    public boolean isFooter(int position) {
        return mHeaderFooterHelper != null && mHeaderFooterHelper.isInFooter(position, mCallback.getActuallyItemSize());
    }

    //--------------------------------------------------------------
    @Override
    public final int getItemViewType(int position) {
        if (mHeaderFooterHelper != null) {
            //in header or footer
            if (mHeaderFooterHelper.isInHeader(position) ||
                    mHeaderFooterHelper.isInFooter(position, mCallback.getActuallyItemSize()))
                return position;

            position -= mHeaderFooterHelper.getHeaderViewSize();
        }
        return getItemViewTypeImpl(mHeaderFooterHelper, position);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolderImpl(mHeaderFooterHelper, parent, viewType);
    }

    @Override
    public final int getItemCount() {
        return mHeaderFooterHelper == null ? mCallback.getActuallyItemSize() :
                mCallback.getActuallyItemSize() + mHeaderFooterHelper.getHeaderViewSize() +
                        mHeaderFooterHelper.getFooterViewSize();
    }
    //----------------------------------------------------------
    protected RecyclerView.ViewHolder onCreateViewHolderImpl(HeaderFooterHelper hfHelper,
                                                             ViewGroup parent, int viewType) {
        if (hfHelper == null || hfHelper.isLayoutIdInRecord(viewType)) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    viewType, parent, false), viewType);
        } else {
            //header or footer
            return new ViewHolder(hfHelper.findView(viewType, mCallback.getActuallyItemSize()));
        }
    }
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Logger.w("QuickRecycleViewAdapter", "onBindViewHolder", "position_1 = " + position);
        position = holder.getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        // Logger.w("QuickRecycleViewAdapter", "onBindViewHolder", "position_2 = " + position);
        if (mHeaderFooterHelper != null) {
            if (mHeaderFooterHelper.isInHeader(position)
                    || mHeaderFooterHelper.isInFooter(position, mCallback.getActuallyItemSize())) {
                /** let head/footer fullspan in StaggeredGridLayoutManager
                 * added in 1.5
                 */
                ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                }
                return;
            }
            position -= mHeaderFooterHelper.getHeaderViewSize();
        }
        if (!(holder instanceof QuickRecycleViewAdapter.IRecyclerViewHolder)) {
            throw new RuntimeException("all quick adapter's viewHolder must implement" +
                    " the interface IRecyclerViewHolder");
        }
        onBindViewHolderImpl(holder, position);
    }
    // may use
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof QuickRecycleViewAdapter.IRecyclerViewHolder) {
            ((QuickRecycleViewAdapter.IRecyclerViewHolder) holder).getViewHelper().getRootView().clearAnimation();
        }
    }

    @Override
    public void addOnAttachStateChangeListener(AdapterAttachStateManager.OnAttachStateChangeListener l) {
        if(mStateListeners == null){
            mStateListeners = new ArrayList<>();
        }
        mStateListeners.add(l);
    }
    @Override
    public void removeOnAttachStateChangeListener(AdapterAttachStateManager.OnAttachStateChangeListener l) {
        if(mStateListeners != null){
            mStateListeners.remove(l);
        }
    }
    @Override
    public boolean hasOnAttachStateChangeListener(AdapterAttachStateManager.OnAttachStateChangeListener l) {
        return mStateListeners != null && mStateListeners.contains(l);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(mStateListeners != null && mStateListeners.size() > 0) {
            for (AdapterAttachStateManager.OnAttachStateChangeListener l : mStateListeners) {
                l.onAttachedToRecyclerView(recyclerView, this);
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if(mStateListeners != null && mStateListeners.size() > 0) {
            for (AdapterAttachStateManager.OnAttachStateChangeListener l : mStateListeners) {
                l.onDetachedFromRecyclerView(recyclerView, this);
            }
        }
    }
    /**
     * called on bind view holder impl
     * @param holder the view holder
     * @param position the position
     */
    protected abstract void onBindViewHolderImpl(RecyclerView.ViewHolder holder, int position);

    /**
     * get item view type impl
     * @param hfHelper the header footer helper
     * @param position the position
     * @return the item type
     */
    protected abstract int getItemViewTypeImpl(HeaderFooterHelper hfHelper, int position);
   /* {
        int layoutId = getItemLayoutId(position, getItem(position));
        if (hfHelper != null)
            hfHelper.recordLayoutId(layoutId);
        return layoutId;
    }*/
    /**
     * the callback of this
     */
    public interface Callback{
        /**
         * get the item size really. exclude header and footer.
         * @return the item size
         */
        int getActuallyItemSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements QuickRecycleViewAdapter.IRecyclerViewHolder {

        public final ViewHelper2 mViewHelper;
        /**
         * if is in header or footer ,mLayoutId = 0
         */
        public final int mLayoutId;

        public ViewHolder(View itemView, int layoutId) {
            super(itemView);
            this.mLayoutId = layoutId;
            this.mViewHelper = new ViewHelper2(itemView);
        }

        public ViewHolder(View itemView) {
            this(itemView, 0);
        }

        public Context getContext() {
            return mViewHelper.getContext();
        }

        @Override
        public int getLayoutId() {
            return mLayoutId;
        }

        @Override
        public ViewHelper2 getViewHelper() {
            return mViewHelper;
        }
    }

    public interface IRecyclerViewHolder {
        /**
         * get the item layout id.
         *
         * @return the item layout id
         */
        int getLayoutId();

        /**
         * get the ViewHelper
         *
         * @return the view helper
         */
        ViewHelper getViewHelper();
    }
}
