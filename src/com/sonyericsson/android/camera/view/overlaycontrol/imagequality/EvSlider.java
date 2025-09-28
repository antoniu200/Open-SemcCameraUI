package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class EvSlider extends SettingItemSlider {
    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected int getBackgroundImageResource(int i) {
        return i == 2 ? R.drawable.cam_core_image_quality_control_exposure_value_slider_icn : R.drawable.cam_core_image_quality_control_exposure_value_slider_port_icn;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected String getMaxValue() {
        return null;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected String getMinValue() {
        return null;
    }

    public EvSlider(Context context, SettingItem settingItem, OnSlideListener onSlideListener) {
        super(context, settingItem, false, false, onSlideListener);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected int getDefaultSettingItemPosition() {
        return (getItem().getChildren().size() - 1) - Ev.ZERO.ordinal();
    }
}
