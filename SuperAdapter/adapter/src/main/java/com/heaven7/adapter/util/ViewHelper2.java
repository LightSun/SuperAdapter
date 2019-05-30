package com.heaven7.adapter.util;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.heaven7.core.util.ViewHelper;
import com.heaven7.core.util.ViewHelperImpl;
import com.heaven7.core.util.viewhelper.action.IViewGetter;

/**
 * @author heaven7
 * @since 1.8.9
 */
public class ViewHelper2 extends ViewHelper {

	public ViewHelper2(View root){
		super(root);
	}

	protected ViewHelperImpl2 locateView(int viewId){
		return (ViewHelperImpl2) mImpl.view(getView(viewId));
	}
	@Override
	protected ViewHelperImpl onCreateViewHelper() {
		return new ViewHelperImpl2();
	}
	@Override
	public ViewHelper2 setRootOnClickListener(View.OnClickListener l) {
		return (ViewHelper2) super.setRootOnClickListener(l);
	}

	@Override
	public ViewHelper2 setTextSizeDp(int viewId, float size) {
		return (ViewHelper2) super.setTextSizeDp(viewId, size);
	}

	@Override
	public ViewHelper2 setTextSize(int viewId, float size) {
		return (ViewHelper2) super.setTextSize(viewId, size);
	}

	@Override
	public ViewHelper2 setText(int viewId, CharSequence text) {
		return (ViewHelper2) super.setText(viewId, text);
	}

	@Override
	public ViewHelper2 toogleVisibility(int viewId) {
		return (ViewHelper2) super.toogleVisibility(viewId);
	}

	@Override
	public ViewHelper2 setTextColor(int viewId, int textColor) {
		return (ViewHelper2) super.setTextColor(viewId, textColor);
	}

	@Override
	public ViewHelper2 setTextAppearance(int viewId, int resId) {
		return (ViewHelper2) super.setTextAppearance(viewId, resId);
	}

	@Override
	public ViewHelper2 setTextColorRes(int viewId, int textColorRes) {
		return (ViewHelper2) super.setTextColorRes(viewId, textColorRes);
	}

	@Override
	public ViewHelper2 setScaleType(int viewId, ImageView.ScaleType type) {
		return (ViewHelper2) super.setScaleType(viewId, type);
	}

	@Override
	public ViewHelper2 setVisibility(int viewId, boolean visible) {
		return (ViewHelper2) super.setVisibility(viewId, visible);
	}

	@Override
	public ViewHelper2 setVisibility(int viewId, int visibility) {
		return (ViewHelper2) super.setVisibility(viewId, visibility);
	}

	@Override
	public ViewHelper2 setTypeface(int viewId, Typeface typeface) {
		return (ViewHelper2) super.setTypeface(viewId, typeface);
	}

	@Override
	public ViewHelper2 setTypeface(Typeface typeface, int... viewIds) {
		return (ViewHelper2) super.setTypeface(typeface, viewIds);
	}

	@Override
	public ViewHelper2 setProgress(int viewId, int progress) {
		return (ViewHelper2) super.setProgress(viewId, progress);
	}

	@Override
	public ViewHelper2 setProgress(int viewId, int progress, int max) {
		return (ViewHelper2) super.setProgress(viewId, progress, max);
	}

	@Override
	public ViewHelper2 setProgressMax(int viewId, int max) {
		return (ViewHelper2) super.setProgressMax(viewId, max);
	}

	@Override
	public ViewHelper2 setRating(int viewId, float rating) {
		return (ViewHelper2) super.setRating(viewId, rating);
	}

	@Override
	public ViewHelper2 setRating(int viewId, float rating, int max) {
		return (ViewHelper2) super.setRating(viewId, rating, max);
	}

	@Override
	public ViewHelper2 setTag(int viewId, Object tag) {
		return (ViewHelper2) super.setTag(viewId, tag);
	}

	@Override
	public ViewHelper2 setTag(int viewId, int key, Object tag) {
		return (ViewHelper2) super.setTag(viewId, key, tag);
	}

	@Override
	public ViewHelper2 setOnTouchListener(int viewId, View.OnTouchListener listener) {
		return (ViewHelper2) super.setOnTouchListener(viewId, listener);
	}
	@Override
	public ViewHelperImpl2 view(int viewId) {
		return (ViewHelperImpl2) super.view(viewId);
	}
	@Override
	public ViewHelperImpl2 viewRoot() {
		return (ViewHelperImpl2) super.viewRoot();
	}
	@Override
	public ViewHelper2 addTextChangedListener(int viewId, TextWatcher watcher) {
		return (ViewHelper2) super.addTextChangedListener(viewId, watcher);
	}
	@Override
	public ViewHelper2 setEnable(int viewId, boolean enable) {
		return (ViewHelper2) super.setEnable(viewId, enable);
	}
	@Override
	public ViewHelper2 setImageResource(int viewId, int imageResId) {
		return (ViewHelper2) super.setImageResource(viewId, imageResId);
	}

	@Override
	public ViewHelper2 setBackgroundColor(int viewId, int color) {
		return (ViewHelper2) super.setBackgroundColor(viewId, color);
	}

	@Override
	public ViewHelper2 setBackgroundRes(int viewId, int backgroundRes) {
		return (ViewHelper2) super.setBackgroundRes(viewId, backgroundRes);
	}

	@Override
	public ViewHelper2 setBackgroundDrawable(int viewId, Drawable drawable) {
		return (ViewHelper2) super.setBackgroundDrawable(viewId, drawable);
	}

	@Override
	public ViewHelper2 setImageDrawable(int viewId, Drawable drawable) {
		return (ViewHelper2) super.setImageDrawable(viewId, drawable);
	}

	@Override
	public ViewHelper2 setImageUrl(int viewId, String imageUrl, IImageLoader loader) {
		return (ViewHelper2) super.setImageUrl(viewId, imageUrl, loader);
	}

	@Override
	public ViewHelper2 setImageURI(int viewId, Uri uri) {
		return (ViewHelper2) super.setImageURI(viewId, uri);
	}

	@Override
	public ViewHelper2 setImageBitmap(int viewId, Bitmap bitmap) {
		return (ViewHelper2) super.setImageBitmap(viewId, bitmap);
	}

	@Override
	public ViewHelper2 setAlpha(int viewId, float value) {
		return (ViewHelper2) super.setAlpha(viewId, value);
	}

	@Override
	public ViewHelper2 linkify(int viewId) {
		return (ViewHelper2) super.linkify(viewId);
	}

	@Override
	public ViewHelper2 linkify(int viewId, int linkifyMask) {
		return (ViewHelper2) super.linkify(viewId, linkifyMask);
	}

	@Override
	public ViewHelper2 setChecked(int viewId, boolean checked) {
		return (ViewHelper2) super.setChecked(viewId, checked);
	}

	@Override
	public ViewHelper2 setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener l) {
		return (ViewHelper2) super.setOnCheckedChangeListener(viewId, l);
	}

	@Override
	public ViewHelper2 setAdapter(int viewId, Adapter adapter) {
		return (ViewHelper2) super.setAdapter(viewId, adapter);
	}

	@Override
	public ViewHelper2 setOnClickListener(int viewId, View.OnClickListener listener) {
		return (ViewHelper2) super.setOnClickListener(viewId, listener);
	}

	@Override
	public ViewHelper2 setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
		return (ViewHelper2) super.setOnLongClickListener(viewId, listener);
	}

	@Override
	public <T extends View> ViewHelper2 performViewGetter(int viewId, IViewGetter<T> getter) {
		return (ViewHelper2) super.performViewGetter(viewId, getter);
	}

	//-------------------------- new 1.8.9 -----------------------------------
	public ViewHelper2 setImageTintColor(int viewId, int color){
		return locateView(viewId).setImageTintColor(color).reverse(this);
	}
	public ViewHelper2 setImageTintList(int viewId, @Nullable ColorStateList tint){
		return locateView(viewId).setImageTintList(tint).reverse(this);
	}
	public ViewHelper2 setImageTintMode(int viewId, PorterDuff.Mode mode){
		return locateView(viewId).setImageTintMode(mode).reverse(this);
	}
	public ViewHelper2 setBackgroundTintColor(int viewId, int color){
		return locateView(viewId).setBackgroundTintColor(color).reverse(this);
	}
	public ViewHelper2 setBackgroundTintList(int viewId, @Nullable ColorStateList tint){
		return locateView(viewId).setBackgroundTintList(tint).reverse(this);
	}
	public ViewHelper2 setBackgroundTintMode(int viewId, PorterDuff.Mode mode){
		return locateView(viewId).setBackgroundTintMode(mode).reverse(this);
	}
	public ViewHelper2 setForegroundTintList(int viewId, @Nullable ColorStateList tint){
		return locateView(viewId).setForegroundTintList(tint).reverse(this);
	}
	public ViewHelper2 setForegroundTintMode(int viewId, PorterDuff.Mode mode){
		return locateView(viewId).setForegroundTintMode(mode).reverse(this);
	}
	public ViewHelper2 setElevation(int viewId, float evevation){
		return locateView(viewId).setElevation(evevation).reverse(this);
	}
	public ViewHelper2 setClipToOutline(int viewId, boolean clipToOutline){
		return locateView(viewId).setClipToOutline(clipToOutline).reverse(this);
	}
	public ViewHelper2 setOutlineProvider(int viewId, ViewOutlineProvider provider){
		return locateView(viewId).setOutlineProvider(provider).reverse(this);
	}

}