package com.heaven7.adapter.selector;

import com.heaven7.adapter.ISelector;

import java.util.List;

/**
 * the abstract selector
 * @param <T> the type
 * @author heaven7
 * @since 2.0.7
 */
public abstract class AbstractSelector<T> implements ISelector<T> {

    private final Callback<T> callback;

    public AbstractSelector(Callback<T> callback) {
        this.callback = callback;
    }

    public void notifySelectState(List<T> selected, List<T> unselected){
        callback.notifySelectState(selected, unselected);
    }
}
