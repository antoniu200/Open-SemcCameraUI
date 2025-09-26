// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.content.Context;

class ShutterSpeedSlider extends SettingItemSlider
{
    public ShutterSpeedSlider(final Context context, final SettingItem settingItem, final OnSlideListener onSlideListener) {
        super(context, settingItem, true, false, onSlideListener);
    }
    
    @Override
    protected int getBackgroundImageResource(final int n) {
        if (n == 2) {
            return 2131231068;
        }
        return 2131231069;
    }
    
    @Override
    protected int getDefaultSettingItemPosition() {
        return this.getItem().getChildren().size() - 1 - ShutterSpeed.getIndexOfDefault();
    }
    
    @Override
    protected String getMaxValue() {
        return null;
    }
    
    @Override
    protected String getMinValue() {
        return null;
    }
}
