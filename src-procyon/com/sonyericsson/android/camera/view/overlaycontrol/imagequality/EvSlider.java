// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.content.Context;

class EvSlider extends SettingItemSlider
{
    public EvSlider(final Context context, final SettingItem settingItem, final OnSlideListener onSlideListener) {
        super(context, settingItem, false, false, onSlideListener);
    }
    
    @Override
    protected int getBackgroundImageResource(final int n) {
        if (n == 2) {
            return 2131231060;
        }
        return 2131231061;
    }
    
    @Override
    protected int getDefaultSettingItemPosition() {
        return this.getItem().getChildren().size() - 1 - Ev.ZERO.ordinal();
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
