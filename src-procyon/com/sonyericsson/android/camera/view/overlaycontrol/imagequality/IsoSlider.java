// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.content.Context;

class IsoSlider extends SettingItemSlider
{
    public IsoSlider(final Context context, final SettingItem settingItem, final OnSlideListener onSlideListener) {
        super(context, settingItem, true, true, onSlideListener);
    }
    
    public final int getBackgroundImageResource(final int n) {
        if (n == 2) {
            return 2131231066;
        }
        return 2131231067;
    }
    
    public final int getDefaultSettingItemPosition() {
        return this.getItem().getChildren().size() - 1 - Iso.getIndexOfDefault();
    }
    
    @Override
    protected String getMaxValue() {
        if (this.getItem().getChildren().size() <= 1) {
            return null;
        }
        int i = Integer.MIN_VALUE;
        int n;
        for (int j = 0; j < this.getItem().getChildren().size(); ++j, i = n) {
            final int isoValue = this.getItem().getChildren().get(j).getData().getIsoValue();
            n = i;
            if (isoValue > 0 && isoValue > (n = i)) {
                n = isoValue;
            }
        }
        return String.valueOf(i);
    }
    
    @Override
    protected String getMinValue() {
        if (this.getItem().getChildren().size() <= 1) {
            return null;
        }
        int i = Integer.MAX_VALUE;
        int n;
        for (int j = 0; j < this.getItem().getChildren().size(); ++j, i = n) {
            final int isoValue = this.getItem().getChildren().get(j).getData().getIsoValue();
            n = i;
            if (isoValue > 0 && isoValue < (n = i)) {
                n = isoValue;
            }
        }
        return String.valueOf(i);
    }
}
