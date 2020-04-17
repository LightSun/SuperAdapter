package com.heaven7.adapter.page;

import android.content.Context;
import android.view.ViewGroup;

/**
 * @since 2.1.2
 * @author heaven7
 */
/*public*/ class ItemViewContext {

    public final ViewGroup parent;
    public final int position;
    public final int realPosition;
    public final Object data;

    /*public*/ ItemViewContext(ViewGroup parent, int position,int realPosition, Object data) {
        this.parent = parent;
        this.position = position;
        this.realPosition = realPosition;
        this.data = data;
    }
    public Context getContext() {
        return parent.getContext();
    }
}