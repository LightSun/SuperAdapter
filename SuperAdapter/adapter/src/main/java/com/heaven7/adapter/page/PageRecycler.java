package com.heaven7.adapter.page;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.util.Pools;

import com.heaven7.memory.util.Cacher;

/*public*/ class PageRecycler {

    private final Pools.SimplePool<ItemViewContext> mPool = new Pools.SimplePool<>(2);
    private final Cacher<View, ItemViewContext> mCacher;

    public <T> PageRecycler(final PageViewProvider<T> provider, int maxPoolSize) {
        this.mCacher = new Cacher<View, ItemViewContext>(maxPoolSize) {
            @Override @SuppressWarnings("unchecked")
            public View create(ItemViewContext context) {
                return provider.createItemView(context.parent, context.position, context.realPosition, (T) context.data);
            }
        };
    }

    public ItemViewContext obtainItemContext(ViewGroup parent, int position, int realPosition, Object data){
        ItemViewContext context = mPool.acquire();
        if(context == null){
            context = new ItemViewContext();
        }
        context.set(parent, position, realPosition, data);
        return context;
    }

    public void recycleItemContext(ItemViewContext context){
        context.reset();
        mPool.release(context);
    }

    /**
     * called on obtain item view
     * @param context the item context
     * @return the item view
     */
    public View obtainItemView(ItemViewContext context){
        return mCacher.obtain(context);
    }

    /**
     * recycle item view
     * @param view the view
     * @param context the item context
     */
    public void recycleItemView(View view, ItemViewContext context){
        mCacher.recycle(view);
    }
}
