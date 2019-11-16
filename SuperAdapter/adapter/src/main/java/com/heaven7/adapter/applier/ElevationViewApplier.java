package com.heaven7.adapter.applier;

import android.content.Context;
import android.os.Build;
import android.view.View;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;

/**
 * Created by heaven7 on 2019/3/11.
 * @since 2.0.0
 */
public class ElevationViewApplier extends BaseViewApplier implements RecycleViewComponentsAdapter.TwoStateApplier {

    private final float selectElevation;
    private final float unselectElevation;

    public ElevationViewApplier(int viewId, float selectElevation, float unselectElevation) {
        super(viewId);
        this.selectElevation = selectElevation;
        this.unselectElevation = unselectElevation;
    }

    @Override
    protected void apply(Context context, Object contextData, int position, ISelectable item, View view) {
        if(Build.VERSION.SDK_INT >= 21){
            view.setElevation(item.isSelected() ? selectElevation : unselectElevation);
        }
    }
}
