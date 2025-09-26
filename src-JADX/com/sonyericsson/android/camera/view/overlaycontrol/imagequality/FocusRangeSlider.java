package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class FocusRangeSlider extends SettingItemSlider {
    private CameraInfo.CameraId mCameraId;

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected int getBackgroundImageResource(int i) {
        return i == 2 ? R.drawable.cam_core_image_quality_control_focus_slider_icn : R.drawable.cam_core_image_quality_control_focus_slider_port_icn;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected String getMaxValue() {
        return null;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected int getMemoryStepCount() {
        return 99;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected String getMinValue() {
        return null;
    }

    public FocusRangeSlider(Context context, SettingItem settingItem, OnSlideListener onSlideListener, CameraInfo.CameraId cameraId) {
        super(context, settingItem, true, false, onSlideListener);
        this.mCameraId = cameraId;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected int getSelectedSettingItemPosition() {
        if (getItem().getChildren().get(FocusRange.AF.ordinal()).isSelected()) {
            return getAutoSettingItemPosition();
        }
        if (getItem().getChildren().get(FocusRange.MF.ordinal()).isSelected()) {
            return FocusRange.MF.getInt();
        }
        if (getItem().getChildren().get(FocusRange.DEFAULT.ordinal()).isSelected()) {
            return FocusRange.DEFAULT.getInt();
        }
        return 0;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected void updateSelectedSettingItem(int i, boolean z) {
        if (i != getAutoSettingItemPosition()) {
            if (i == getDefaultSettingItemPosition()) {
                FocusRange.DEFAULT.setInt(i);
            } else {
                FocusRange.MF.setInt(i);
            }
        }
        super.updateSelectedSettingItem(i, z);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected String getIndicatorContentDescription(int i) {
        return String.valueOf((int) FocusRange.MF.getFocusRange(this.mCameraId)) + " " + getString(R.string.cam_strings_accessibility_image_quality_control_focus_manual_adjusted_txt);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected int getDefaultSettingItemPosition() {
        return (int) FocusRange.getThreshold();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingItemSlider
    protected int indexOf(int i) {
        if (i == getAutoSettingItemPosition()) {
            return FocusRange.AF.ordinal();
        }
        if (i == getDefaultSettingItemPosition()) {
            return FocusRange.DEFAULT.ordinal();
        }
        return FocusRange.MF.ordinal();
    }
}
