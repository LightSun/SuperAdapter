package com.heaven7.adapter.page;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

/**
 * the wrapped page transformer
 * @author heaven7
 * @since 2.1.2
 */
public class WrappedPageTransformer implements ViewPager2.PageTransformer, ViewPager.PageTransformer {

    private ViewPager.PageTransformer trans1;
    private ViewPager2.PageTransformer trans2;

    private WrappedPageTransformer(ViewPager.PageTransformer trans1, ViewPager2.PageTransformer trans2) {
        this.trans1 = trans1;
        this.trans2 = trans2;
    }
    public WrappedPageTransformer(ViewPager.PageTransformer trans1) {
       this(trans1, null);
    }
    public WrappedPageTransformer(ViewPager2.PageTransformer trans2) {
        this(null, trans2);
    }
    @Override
    public void transformPage(@NonNull View page, float position) {
        if(trans1 != null){
            trans1.transformPage(page, position);
        }else if(trans2 != null){
            trans2.transformPage(page, position);
        }
    }
}
