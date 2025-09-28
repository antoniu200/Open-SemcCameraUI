package com.sonyericsson.android.camera.view.setting.dialogitem;

import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class SettingDialogItem {
    private SettingItem mItem;

    public abstract View getView();

    public void refresh() {
    }

    public void reset() {
    }

    public void setClickable(boolean z) {
    }

    public void setUiOrientation(int i) {
    }

    public abstract void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams);

    public SettingDialogItem(SettingItem settingItem) {
        this.mItem = settingItem;
    }

    public void setItem(SettingItem settingItem) {
        this.mItem = settingItem;
    }

    public SettingItem getItem() {
        return this.mItem;
    }

    public void select(SettingItem settingItem) {
        settingItem.select();
    }

    protected DrawableStateChanger changeDrawableState(SettingAdapter.ItemLayoutParams itemLayoutParams) {
        return new DrawableStateChanger(itemLayoutParams);
    }
}
