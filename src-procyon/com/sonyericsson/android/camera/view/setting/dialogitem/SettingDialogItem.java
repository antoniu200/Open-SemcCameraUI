// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.dialogitem;

import android.view.ViewGroup;
import android.view.View;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;

public abstract class SettingDialogItem
{
    private SettingItem mItem;
    
    public SettingDialogItem(final SettingItem mItem) {
        this.mItem = mItem;
    }
    
    protected DrawableStateChanger changeDrawableState(final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        return new DrawableStateChanger(itemLayoutParams);
    }
    
    public SettingItem getItem() {
        return this.mItem;
    }
    
    public abstract View getView();
    
    public void refresh() {
    }
    
    public void reset() {
    }
    
    public void select(final SettingItem settingItem) {
        settingItem.select();
    }
    
    public void setClickable(final boolean b) {
    }
    
    public void setItem(final SettingItem mItem) {
        this.mItem = mItem;
    }
    
    public void setUiOrientation(final int n) {
    }
    
    public abstract void update(final ViewGroup p0, final SettingAdapter.ItemLayoutParams p1);
}
