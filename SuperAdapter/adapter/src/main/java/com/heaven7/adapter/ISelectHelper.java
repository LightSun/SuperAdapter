package com.heaven7.adapter;

import java.util.List;

/**
 * <p>Use {@linkplain Selector} instead.</p>
 * the select helper interface.
 * Created by heaven7 on 2017/1/9.
 *
 * @since 1.8.5
 */
@Deprecated
public interface ISelectHelper {

    int NO_POSITION = -1;

    /**
     * init the all select position. often called in method create adapter.
     *
     * @param positions the all select positions to init.
     * @param notify    true to notify
     */
    void initSelectPosition(List<Integer> positions, boolean notify);

    /**
     * select the target position of item.
     *
     * @param position the position.
     * @return true if operate success.
     */
    boolean select(int position);

    /**
     * unselect(cancel select) the target position of item.
     *
     * @param position the position.
     * @return true if operate success.
     */
    boolean unselect(int position);

    /**
     * toggle the select state of the target item
     *
     * @param position the position which indicate the target item.
     * @return true if operate success.
     */
    boolean toggleSelect(int position);

    /**
     * clear the select position/positions of this select helper, but not notify date changed.
     */
    void clearSelectedPosition();

    /**
     * clear the selected position/positions of this select helper, and notify date changed.
     */
    void clearSelectedState();

    /**
     * get the select position list.
     *
     * @return the list which contains the all select position.
     */
    int[] getSelectPosition();

    /**
     * notify the selector state changed. this is often called by child.
     * @param unselectPostions the unselected state of positions. can be null.
     * @param selectPostions the selected state of positions.can be null.
     */
    void notifySelectorStateChanged(int[] unselectPostions, int[] selectPostions);


    /**
     * the selector notifier.
     * @since 1.8.5
     */
    interface SelectorNotifier {

        /**
         * Notify the selector state change. This is called when the select state changed.
         * @param unselectPostions the selected position to cancel.
         * @param selectPostions the selected position to add/join.
         */
        void notifySelectorStateChanged(int[] unselectPostions, int[] selectPostions);
    }

}
