package com.heaven7.adapter;

import java.util.List;

/**
 * the expendable manager
 * @param <T> the parent data type
 * @since 2.0.5
 */
public interface IExpendableManager<T> {

    /**
     * toggle expend
     * @param data the parent data
     * @param position the real position.
     */
    boolean toggleExpend(T data, int position);

    /**
     * toggle expend by parent
     * @param data the parent data
     * @param parentPosition the parent position.
     */
    boolean toggleExpendByParent(T data, int parentPosition);

    /**
     * collapse all parents.
     */
    void collapseAll();
    /**
     * expend all parents.
     */
    void expendAll();
    /**
     * collapse by real position
     * @param position the real position exclude header
     * @return true if expend success.
     */
    boolean collapse(int position);

    /**
     * collapse by parent
     * @param parentPos the parent position exclude header
     * @return true if expend success.
     */
    boolean collapseByParent(int parentPos);

    /**
     * expend by real position
     * @param realPosition the real position exclude header
     * @return true if expend success.
     */
    boolean expand(int realPosition);

    /**
     * expend by parent
     * @param parentPos the parent position exclude header
     * @return true if expend success.
     */
    boolean expandByParent(int parentPos);

    /**
     *  add child items.
     * @param parentPos the parent pos
     * @param list the list
     * @param notifyParent true to notify parent
     * @return true if add success
     */
    boolean addChildItems(int parentPos, List<?> list, boolean notifyParent);

    /**
     *  add child items.
     * @param parentPos the parent pos exclude header
     * @param childStartIndex the child start index
     * @param list the list
     * @param notifyParent true to notify parent
     * @return true if add success
     */
    boolean addChildItems(int parentPos, int childStartIndex, List<?> list, boolean notifyParent);

    /**
     *  add child items.
     * @param parentPos the parent pos
     * @param childStartIndex the child start index
     * @param list the list
     * @param notifyParent true to notify parent
     * @param notifyAfter true to notify after positions of this parent position
     * @return true if add success
     */
    boolean addChildItems(int parentPos, int childStartIndex, List<?> list, boolean notifyParent, boolean notifyAfter);

    /**
     * set items
     * @param list the items data
     */
    void setItems(List<T> list);

    /**
     * add item sas parent item
     * @param list the items
     */
    void addItems(List<T> list);

    /**
     * add item sas parent item
     * @param startParentPos the start parent position exclude header
     * @param list the items
     */
    void addItems(int startParentPos, List<T> list);

    /**
     * add item as parent item
     * @param item the item
     */
    void addItem(T item);

    /**
     * get the items
     * @return the items
     */
    List<T> getItems();

    /**
     * get the parent item by target parent position.
     * @param parentPos the parent position exclude header
     * @return the parent item
     */
    T getItem(int parentPos);

    /**
     * indicate that the parent position has child item or not.
     * @param parentPos the parent position exclude header
     * @return true if has child item
     */
    boolean hasChildItem(int parentPos);

    /**
     * indicate that the parent position is expend item or not.
     * @param parentPos the parent position exclude header
     * @return true if is expend item
     */
    boolean isExpendItem(int parentPos);

    /**
     * get the parent item
     * @param realPosition the real position exclude header
     * @return the parent data
     */
    T getParentItem(int realPosition);

    /**
     * get the child item
     * @param parentPos the parent position exclude header
     * @param childPos the child position exclude header
     * @return the child item.
     */
    Object getChildItem(int parentPos, int childPos);

    /**
     * get the child item
     * @param realPosition the real position
     * @return the child item data
     */
    Object getChildItem(int realPosition);

    /**
     * get parent position include parent pos and sub pos.
     *
     * @param pos     the position. exclude header
     * @param outPoss the out positions. [0] is parent position. [1] is child position.
     * @return null if can't find
     */
    int[] getParentPositions(int pos, int[] outPoss);
}
