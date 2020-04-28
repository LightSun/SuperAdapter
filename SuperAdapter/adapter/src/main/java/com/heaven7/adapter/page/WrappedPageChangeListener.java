package com.heaven7.adapter.page;

import androidx.annotation.Px;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

/**
 * the page change wrapper which used to adapt OnPageChangeListener and OnPageChangeCallback.
 *
 * @author heaven7
 * @see androidx.viewpager.widget.ViewPager.OnPageChangeListener
 * @see androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
 * @since 2.1.2
 */
public class WrappedPageChangeListener extends ViewPager2.OnPageChangeCallback implements ViewPager.OnPageChangeListener {

    private ViewPager2.OnPageChangeCallback callback;
    private ViewPager.OnPageChangeListener listener;

    public WrappedPageChangeListener(ViewPager2.OnPageChangeCallback callback) {
        this(callback, null);
    }

    public WrappedPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this(null, listener);
    }

    public WrappedPageChangeListener() {
        this(null, null);
    }

    private WrappedPageChangeListener(ViewPager2.OnPageChangeCallback callback, ViewPager.OnPageChangeListener listener) {
        this.callback = callback;
        this.listener = listener;
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    public void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels) {
        if (callback != null) {
            callback.onPageScrolled(position, positionOffset, positionOffsetPixels);
        } else if (listener != null) {
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    public void onPageSelected(int position) {
        if (callback != null) {
            callback.onPageSelected(position);
        } else if (listener != null) {
            listener.onPageSelected(position);
        }
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager2#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager2#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     * @see ViewPager2#SCROLL_STATE_SETTLING
     */
    public void onPageScrollStateChanged(int state) {
        if (callback != null) {
            callback.onPageScrollStateChanged(state);
        } else if (listener != null) {
            listener.onPageScrollStateChanged(state);
        }
    }
}
