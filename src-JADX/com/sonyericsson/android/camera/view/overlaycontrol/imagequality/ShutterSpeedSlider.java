package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ShutterSpeedSlider extends SettingItemSlider {
    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected int getBackgroundImageResource(int i) {
        return i == 2 ? R.drawable.cam_core_image_quality_control_shutter_speed_slider_icn : R.drawable.cam_core_image_quality_control_shutter_speed_slider_port_icn;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected String getMaxValue() {
        return null;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected String getMinValue() {
        return null;
    }

    public ShutterSpeedSlider(Context context, SettingItem settingItem, OnSlideListener onSlideListener) {
        super(context, settingItem, true, false, onSlideListener);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected int getDefaultSettingItemPosition() {
        return (getItem().getChildren().size() - 1) - ShutterSpeed.getIndexOfDefault();
    }
}
