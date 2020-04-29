package com.heaven7.adapter.page;

import android.content.Context;
import android.view.ViewGroup;

/**
 * @since 2.1.2
 * @author heaven7
 */
public class ItemViewContext {

    public ViewGroup parent;
    public int position;
    public int realPosition;
    public Object data;

    public void set(ViewGroup parent, int position,int realPosition, Object data) {
        this.parent = parent;
        this.position = position;
        this.realPosition = realPosition;
        this.data = data;
    }
    public Context getContext() {
        return parent.getContext();
    }
    public void reset(){
        set(null, -1, -1, null);
    }

}