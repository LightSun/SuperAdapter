package com.heaven7.adapter.page;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

/**
 * the view pager delegate.
 *
 * @param <V> the pager view
 * @author heaven7
 * @see ViewPager
 * @see ViewPager2
 * @since 2.1.2
 */
public abstract class ViewPagerDelegate<V> {

    protected final V view;

    public ViewPagerDelegate(V view) {
        this.view = view;
    }

    public V getView() {
        return view;
    }

    public static ViewPagerDelegate<?> get(View v) {
        if (v instanceof ViewPager) {
            return new PageViewDelegateV1((ViewPager) v);
        } else if (v instanceof ViewPager2) {
            return new PageViewDelegateV2((ViewPager2) v);
        } else {
            throw new UnsupportedOperationException("un-support page view = " + v.getClass().getName());
        }
    }

    public abstract void setAdapter(LifecycleOwner owner, IPageAdapter adapter);

    public abstract <T> void setAdapter(LifecycleOwner owner, PageDataProvider<? extends T> dataProvider,
                                        PageViewProvider<? extends T> viewDelegate, boolean loop);

    public abstract void removeOnPageChangeListener(WrappedPageChangeListener listener);

    public abstract void addOnPageChangListener(WrappedPageChangeListener listener);

    public final void addOnPageChangListener(ViewPager.OnPageChangeListener listener) {
        addOnPageChangListener(new WrappedPageChangeListener(listener));
    }

    public final void addOnPageChangListener(ViewPager2.OnPageChangeCallback listener) {
        addOnPageChangListener(new WrappedPageChangeListener(listener));
    }

    public final void removeOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        removeOnPageChangeListener(new WrappedPageChangeListener(listener));
    }

    public final void removeOnPageChangeListener(ViewPager2.OnPageChangeCallback listener) {
        removeOnPageChangeListener(new WrappedPageChangeListener(listener));
    }

    public abstract void setOffscreenPageLimit(int limit);

    public abstract int getCurrentItem();

    public abstract void setCurrentItem(int item, boolean smoothScroll);

    public abstract void setCurrentItem(int item);

    public abstract boolean beginFakeDrag();
    public abstract void endFakeDrag();
    public abstract boolean fakeDragBy(@Px float offsetPxFloat);
    public abstract boolean isFakeDragging();

    public abstract void setPageTransformer(boolean reverseDrawingOrder, @Nullable WrappedPageTransformer transformer);

    public final void setPageTransformer(boolean reverseDrawingOrder, @Nullable ViewPager.PageTransformer transformer) {
        setPageTransformer(reverseDrawingOrder, new WrappedPageTransformer(transformer));
    }

    public final void setPageTransformer(boolean reverseDrawingOrder, @Nullable ViewPager2.PageTransformer transformer) {
        setPageTransformer(reverseDrawingOrder, new WrappedPageTransformer(transformer));
    }

    public final void setPageTransformer(@Nullable WrappedPageTransformer transformer) {
        setPageTransformer(false, transformer);
    }

    public final void setPageTransformer(@Nullable ViewPager.PageTransformer transformer) {
        setPageTransformer(false, new WrappedPageTransformer(transformer));
    }

    public final void setPageTransformer(@Nullable ViewPager2.PageTransformer transformer) {
        setPageTransformer(false, new WrappedPageTransformer(transformer));
    }

    @TargetApi(17)
    public abstract void setLayoutDirection(int layoutDirection);

    //---------------------- optional ---------------------------

    public abstract void setPageMargin(int marginPixels);

    public abstract void setPageMarginDrawable(@Nullable Drawable d);

    /**
     * set the view pager's orientation. see {@linkplain ViewPager2#ORIENTATION_VERTICAL} and {@linkplain ViewPager2#ORIENTATION_HORIZONTAL}
     *
     * @param orientation the orientation
     */
    public abstract void setOrientation(int orientation);

    private static class PageViewDelegateV1 extends ViewPagerDelegate<ViewPager> {

        public PageViewDelegateV1(ViewPager view) {
            super(view);
        }

        @Override
        public boolean isFakeDragging() {
            return view.isFakeDragging();
        }

        @Override
        public boolean beginFakeDrag() {
            return view.beginFakeDrag();
        }
        @Override
        public void endFakeDrag() {
            view.endFakeDrag();
        }
        @Override
        public boolean fakeDragBy(float offsetPxFloat) {
            view.fakeDragBy(offsetPxFloat);
            return true;
        }

        @Override
        public <T> void setAdapter(LifecycleOwner owner, PageDataProvider<? extends T> dataProvider,
                                   PageViewProvider<? extends T> viewDelegate, boolean loop) {
            setAdapter(owner, new GenericPagerAdapter<T>(dataProvider, viewDelegate, loop));
        }

        @Override
        public void setAdapter(LifecycleOwner owner, IPageAdapter ia) {
            try {
                ((ViewPager) view).setAdapter((PagerAdapter) ia);
            } catch (ClassCastException e) {
                throw new IllegalStateException("the adapter must be child of PagerAdapter.");
            }
            owner.getLifecycle().addObserver(new LifecycleEventObserver0(ia));
        }

        @Override
        public void removeOnPageChangeListener(WrappedPageChangeListener listener) {
            view.removeOnPageChangeListener(listener);
        }

        @Override
        public void addOnPageChangListener(WrappedPageChangeListener listener) {
            view.addOnPageChangeListener(listener);
        }

        @Override
        public void setOffscreenPageLimit(int limit) {
            view.setOffscreenPageLimit(limit);
        }

        @Override
        public int getCurrentItem() {
            return view.getCurrentItem();
        }

        @Override
        public void setCurrentItem(int item, boolean smoothScroll) {
            view.setCurrentItem(item, smoothScroll);
        }

        @Override
        public void setCurrentItem(int item) {
            view.setCurrentItem(item);
        }

        @Override
        public void setPageTransformer(boolean reverseDrawingOrder, @Nullable WrappedPageTransformer transformer) {
            view.setPageTransformer(reverseDrawingOrder, transformer);
        }

        @TargetApi(17)
        @Override
        public void setLayoutDirection(int layoutDirection) {
            if (Build.VERSION.SDK_INT >= 17) {
                view.setLayoutDirection(layoutDirection);
            }
        }

        @Override
        public void setPageMargin(int marginPixels) {
            view.setPageMargin(marginPixels);
        }

        @Override
        public void setPageMarginDrawable(@Nullable Drawable d) {
            view.setPageMarginDrawable(d);
        }

        @Override
        public void setOrientation(int orientation) {
            //not impl
        }
    }

    private static class PageViewDelegateV2 extends ViewPagerDelegate<ViewPager2> {

        public PageViewDelegateV2(ViewPager2 view) {
            super(view);
        }

        @Override
        public boolean isFakeDragging() {
            return view.isFakeDragging();
        }

        @Override
        public boolean beginFakeDrag() {
            return view.beginFakeDrag();
        }
        @Override
        public void endFakeDrag() {
            view.endFakeDrag();
        }
        @Override
        public boolean fakeDragBy(float offsetPxFloat) {
            return view.fakeDragBy(offsetPxFloat);
        }
        @Override
        public <T> void setAdapter(LifecycleOwner owner, PageDataProvider<? extends T> dataProvider,
                                   PageViewProvider<? extends T> viewDelegate, boolean loop) {
            setAdapter(owner, new GenericRvPagerAdapter<T>(dataProvider, viewDelegate, loop));
        }

        @Override
        public void setAdapter(LifecycleOwner owner, IPageAdapter ia) {
            try {
                ((ViewPager2) view).setAdapter((RecyclerView.Adapter) ia);
            } catch (ClassCastException e) {
                throw new IllegalStateException("the adapter must be child of PagerAdapter.");
            }
            owner.getLifecycle().addObserver(new LifecycleEventObserver0(ia));
        }

        @Override
        public void addOnPageChangListener(WrappedPageChangeListener listener) {
            view.registerOnPageChangeCallback(listener);
        }
        @Override
        public void removeOnPageChangeListener(WrappedPageChangeListener listener) {
            view.unregisterOnPageChangeCallback(listener);
        }

        @Override
        public void setOffscreenPageLimit(int limit) {
            view.setOffscreenPageLimit(limit);
        }

        @Override
        public void setCurrentItem(int item, boolean smoothScroll) {
            view.setCurrentItem(item, smoothScroll);
        }

        @Override
        public void setCurrentItem(int item) {
            view.setCurrentItem(item);
        }
        @Override
        public int getCurrentItem() {
            return view.getCurrentItem();
        }
        @Override
        public void setPageTransformer(boolean reverseDrawingOrder, @Nullable WrappedPageTransformer transformer) {
            view.setPageTransformer(transformer);
        }

        @TargetApi(17)
        @Override
        public void setLayoutDirection(int layoutDirection) {
            if (Build.VERSION.SDK_INT >= 17) {
                view.setLayoutDirection(layoutDirection);
            }
        }
        @Override
        public void setPageMargin(int marginPixels) {

        }
        @Override
        public void setPageMarginDrawable(@Nullable Drawable d) {
        }

        @Override
        public void setOrientation(int orientation) {
            view.setOrientation(orientation);
        }
    }

    private static class LifecycleEventObserver0 implements LifecycleEventObserver {

        private final IPageAdapter ia;

        public LifecycleEventObserver0(IPageAdapter ia) {
            this.ia = ia;
        }

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                ia.onDestroy((Activity) source);
            }
        }
    }
}
