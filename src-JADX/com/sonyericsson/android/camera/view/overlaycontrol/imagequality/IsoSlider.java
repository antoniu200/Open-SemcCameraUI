package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class IsoSlider extends SettingItemSlider {
    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    public final int getBackgroundImageResource(int i) {
        return i == 2 ? R.drawable.cam_core_image_quality_control_iso_slider_icn : R.drawable.cam_core_image_quality_control_iso_slider_port_icn;
    }

    public IsoSlider(Context context, SettingItem settingItem, OnSlideListener onSlideListener) {
        super(context, settingItem, true, true, onSlideListener);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected String getMaxValue() {
        if (getItem().getChildren().size() <= 1) {
            return null;
        }
        int i = Integer.MIN_VALUE;
        for (int i2 = 0; i2 < getItem().getChildren().size(); i2++) {
            int isoValue = ((Iso) ((TypedSettingItem) getItem().getChildren().get(i2)).getData()).getIsoValue();
            if (isoValue > 0 && isoValue > i) {
                i = isoValue;
            }
        }
        return String.valueOf(i);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected String getMinValue() {
        if (getItem().getChildren().size() <= 1) {
            return null;
        }
        int i = Integer.MAX_VALUE;
        for (int i2 = 0; i2 < getItem().getChildren().size(); i2++) {
            int isoValue = ((Iso) ((TypedSettingItem) getItem().getChildren().get(i2)).getData()).getIsoValue();
            if (isoValue > 0 && isoValue < i) {
                i = isoValue;
            }
        }
        return String.valueOf(i);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    public final int getDefaultSettingItemPosition() {
        return (getItem().getChildren().size() - 1) - Iso.getIndexOfDefault();
    }
}
