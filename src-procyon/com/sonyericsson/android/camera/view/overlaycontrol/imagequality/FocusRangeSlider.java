// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.content.Context;
import com.sonyericsson.android.camera.device.CameraInfo;

class FocusRangeSlider extends SettingItemSlider
{
    private CameraInfo.CameraId mCameraId;
    
    public FocusRangeSlider(final Context context, final SettingItem settingItem, final OnSlideListener onSlideListener, final CameraInfo.CameraId mCameraId) {
        super(context, settingItem, true, false, onSlideListener);
        this.mCameraId = mCameraId;
    }
    
    @Override
    protected int getBackgroundImageResource(final int n) {
        if (n == 2) {
            return 2131231062;
        }
        return 2131231063;
    }
    
    @Override
    protected int getDefaultSettingItemPosition() {
        return (int)FocusRange.getThreshold();
    }
    
    @Override
    protected String getIndicatorContentDescription(int i) {
        i = (int)FocusRange.MF.getFocusRange(this.mCameraId);
        final StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(i));
        sb.append(" ");
        sb.append(this.getString(2131689581));
        return sb.toString();
    }
    
    @Override
    protected String getMaxValue() {
        return null;
    }
    
    @Override
    protected int getMemoryStepCount() {
        return 99;
    }
    
    @Override
    protected String getMinValue() {
        return null;
    }
    
    @Override
    protected int getSelectedSettingItemPosition() {
        if (this.getItem().getChildren().get(FocusRange.AF.ordinal()).isSelected()) {
            return this.getAutoSettingItemPosition();
        }
        if (this.getItem().getChildren().get(FocusRange.MF.ordinal()).isSelected()) {
            return FocusRange.MF.getInt();
        }
        if (this.getItem().getChildren().get(FocusRange.DEFAULT.ordinal()).isSelected()) {
            return FocusRange.DEFAULT.getInt();
        }
        return 0;
    }
    
    @Override
    protected int indexOf(final int n) {
        if (n == this.getAutoSettingItemPosition()) {
            return FocusRange.AF.ordinal();
        }
        if (n == this.getDefaultSettingItemPosition()) {
            return FocusRange.DEFAULT.ordinal();
        }
        return FocusRange.MF.ordinal();
    }
    
    @Override
    protected void updateSelectedSettingItem(final int n, final boolean b) {
        if (n != this.getAutoSettingItemPosition()) {
            if (n == this.getDefaultSettingItemPosition()) {
                FocusRange.DEFAULT.setInt(n);
            }
            else {
                FocusRange.MF.setInt(n);
            }
        }
        super.updateSelectedSettingItem(n, b);
    }
}
