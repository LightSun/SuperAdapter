package com.heaven7.adapter.util;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.heaven7.core.util.ViewHelperImpl;

/**
 * @author heaven7
 * @since 1.8.9
 */
public class ViewHelperImpl2 extends ViewHelperImpl {

    public ViewHelperImpl2(View target) {
        super(target);
    }
    public ViewHelperImpl2() {
    }
    //------------------------------------ new -------------------
    public ViewHelperImpl2 setImageTintColor(int color){
        if(Build.VERSION.SDK_INT >= 21) {
            ((ImageView)v).setImageTintList(ColorStateList.valueOf(color));
        }
        return this;
    }
    public ViewHelperImpl2 setImageTintList(@Nullable ColorStateList tint){
        if(Build.VERSION.SDK_INT >= 21) {
            ((ImageView)v).setImageTintList(tint);
        }
        return this;
    }
    public ViewHelperImpl2 setImageTintMode(PorterDuff.Mode mode){
        if(Build.VERSION.SDK_INT >= 21) {
            ((ImageView)v).setImageTintMode(mode);
        }
        return this;
    }

    public ViewHelperImpl2 setBackgroundTintColor(int color){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        return this;
    }
    public ViewHelperImpl2 setBackgroundTintList(@Nullable ColorStateList tint){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setBackgroundTintList(tint);
        }
        return this;
    }
    public ViewHelperImpl2 setBackgroundTintMode(PorterDuff.Mode mode){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setBackgroundTintMode(mode);
        }
        return this;
    }
    public ViewHelperImpl2 setForegroundTintList(@Nullable ColorStateList tint){
        if(Build.VERSION.SDK_INT >= 23) {
            v.setForegroundTintList(tint);
        }
        return this;
    }
    public ViewHelperImpl2 setForegroundTintMode(PorterDuff.Mode mode){
        if(Build.VERSION.SDK_INT >= 23) {
            v.setForegroundTintMode(mode);
        }
        return this;
    }
    public ViewHelperImpl2 setElevation(float evevation){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setElevation(evevation);
        }
        return this;
    }
    public ViewHelperImpl2 setClipToOutline(boolean clipToOutline){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setClipToOutline(clipToOutline);
        }
        return this;
    }
    public ViewHelperImpl2 setOutlineProvider(ViewOutlineProvider provider){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setOutlineProvider(provider);
        }
        return this;
    }
    public ViewHelperImpl2 invalidateOutline(){
        if(Build.VERSION.SDK_INT >= 21) {
            v.invalidateOutline();
        }
        return this;
    }
}
