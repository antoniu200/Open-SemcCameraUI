package com.sonyericsson.android.camera.view.setting.settingitem;

import android.content.res.Resources;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TypedSettingItem<T> implements SettingItem {
    public static final String TAG = "TypedSettingItem";
    private final String mAdditionalTextForAccessibility;
    private final List<SettingItem> mChildren;
    private final T mData;
    private final int mDialogItemType;
    private final SettingExecutorInterface<T> mExecutor;
    private final int mIconId;
    private boolean mIsSelected;
    private final boolean mIsSoundEnabled;
    private SettingItem.Selectability mSelectability;
    private final String mSubText;
    private final String mText;
    private final int mTextId;

    public TypedSettingItem(T t, int i, int i2, String str, String str2, int i3, SettingExecutorInterface<T> settingExecutorInterface, boolean z) {
        this.mData = t;
        this.mTextId = i2;
        this.mText = "";
        this.mIconId = i;
        this.mDialogItemType = i3;
        this.mExecutor = settingExecutorInterface;
        this.mIsSoundEnabled = z;
        this.mChildren = new ArrayList();
        this.mSubText = str;
        this.mAdditionalTextForAccessibility = str2;
        this.mIsSelected = false;
        this.mSelectability = SettingItem.Selectability.UNSELECTABLE;
    }

    public TypedSettingItem(T t, int i, String str, String str2, String str3, int i2, SettingExecutorInterface<T> settingExecutorInterface, boolean z) {
        this.mData = t;
        this.mTextId = -1;
        this.mText = str;
        this.mSubText = str2;
        this.mIconId = i;
        this.mDialogItemType = i2;
        this.mAdditionalTextForAccessibility = str3;
        this.mExecutor = settingExecutorInterface;
        this.mIsSoundEnabled = z;
        this.mChildren = new ArrayList();
        this.mIsSelected = false;
        this.mSelectability = SettingItem.Selectability.UNSELECTABLE;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public String getText(Resources resources) {
        if (this.mTextId == -1) {
            return this.mText;
        }
        return resources.getString(this.mTextId);
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public String getSubText(Resources resources) {
        return this.mSubText;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public String getContentDescription(Resources resources) {
        StringBuilder sb = new StringBuilder();
        if (this.mTextId != -1) {
            sb.append(resources.getString(this.mTextId));
        } else {
            sb.append(this.mText);
        }
        if (this.mAdditionalTextForAccessibility != null && !this.mAdditionalTextForAccessibility.isEmpty()) {
            sb.append(' ');
            sb.append(this.mAdditionalTextForAccessibility);
        }
        if (!isSelectable()) {
            sb.append(' ');
            sb.append(resources.getString(R.string.cam_strings_accessibility_not_configurable_txt));
        }
        return sb.toString();
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public int getIconId() {
        return this.mIconId;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public boolean isSelected() {
        return this.mIsSelected;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public boolean isSelectable() {
        return this.mSelectability == SettingItem.Selectability.SELECTABLE;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public SettingItem.Selectability getSelectability() {
        return this.mSelectability;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public void setSelected(boolean z) {
        this.mIsSelected = z;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public void setSelectability(SettingItem.Selectability selectability) {
        this.mSelectability = selectability;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public int getDialogItemType() {
        return this.mDialogItemType;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public List<SettingItem> getChildren() {
        return this.mChildren;
    }

    public T getData() {
        return this.mData;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public void select() {
        this.mIsSelected = true;
        if (this.mExecutor == null) {
            return;
        }
        this.mExecutor.onExecute(this);
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public boolean compareData(SettingItem settingItem) {
        return (settingItem instanceof TypedSettingItem) && this.mData == ((TypedSettingItem) settingItem).mData;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public boolean compareData(Object obj) {
        return this.mData == obj;
    }

    public String getValueText() {
        return this.mAdditionalTextForAccessibility;
    }

    @Override // com.sonyericsson.android.camera.view.setting.settingitem.SettingItem
    public boolean isSoundEnabled() {
        return this.mIsSoundEnabled;
    }
}
