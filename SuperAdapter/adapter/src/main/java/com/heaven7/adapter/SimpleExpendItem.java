package com.heaven7.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * the simple expend item
 * @param <T> the child item type
 * @since 2.0.5
 */
public class SimpleExpendItem<T> implements ExpendableAdapter.ExpendableItem {

    private boolean expended;
    private final List<T> items;

    public SimpleExpendItem(List<T> items) {
        this.items = items;
    }
    public SimpleExpendItem() {
        this(new ArrayList<T>());
    }
    public void addChildItem(T item){
        items.add(item);
    }
    public List<T> getChildItems(){
        return items;
    }

    @Override
    public boolean isExpended() {
        return expended;
    }
    @Override
    public void setExpended(boolean expend) {
        expended = expend;
    }
}
