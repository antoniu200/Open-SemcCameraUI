// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.settingitem;

import android.content.res.Resources;
import java.util.ArrayList;
import com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface;
import java.util.List;

public class TypedSettingItem<T> implements SettingItem
{
    public static final String TAG = "TypedSettingItem";
    private final String mAdditionalTextForAccessibility;
    private final List<SettingItem> mChildren;
    private final T mData;
    private final int mDialogItemType;
    private final SettingExecutorInterface<T> mExecutor;
    private final int mIconId;
    private boolean mIsSelected;
    private final boolean mIsSoundEnabled;
    private Selectability mSelectability;
    private final String mSubText;
    private final String mText;
    private final int mTextId;
    
    public TypedSettingItem(final T mData, final int mIconId, final int mTextId, final String mSubText, final String mAdditionalTextForAccessibility, final int mDialogItemType, final SettingExecutorInterface<T> mExecutor, final boolean mIsSoundEnabled) {
        this.mData = mData;
        this.mTextId = mTextId;
        this.mText = "";
        this.mIconId = mIconId;
        this.mDialogItemType = mDialogItemType;
        this.mExecutor = mExecutor;
        this.mIsSoundEnabled = mIsSoundEnabled;
        this.mChildren = new ArrayList<SettingItem>();
        this.mSubText = mSubText;
        this.mAdditionalTextForAccessibility = mAdditionalTextForAccessibility;
        this.mIsSelected = false;
        this.mSelectability = Selectability.UNSELECTABLE;
    }
    
    public TypedSettingItem(final T mData, final int mIconId, final String mText, final String mSubText, final String mAdditionalTextForAccessibility, final int mDialogItemType, final SettingExecutorInterface<T> mExecutor, final boolean mIsSoundEnabled) {
        this.mData = mData;
        this.mTextId = -1;
        this.mText = mText;
        this.mSubText = mSubText;
        this.mIconId = mIconId;
        this.mDialogItemType = mDialogItemType;
        this.mAdditionalTextForAccessibility = mAdditionalTextForAccessibility;
        this.mExecutor = mExecutor;
        this.mIsSoundEnabled = mIsSoundEnabled;
        this.mChildren = new ArrayList<SettingItem>();
        this.mIsSelected = false;
        this.mSelectability = Selectability.UNSELECTABLE;
    }
    
    @Override
    public boolean compareData(final SettingItem settingItem) {
        final boolean b = settingItem instanceof TypedSettingItem;
        boolean b2 = false;
        if (b) {
            if (this.mData == ((TypedSettingItem)settingItem).mData) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    @Override
    public boolean compareData(final Object o) {
        return this.mData == o;
    }
    
    @Override
    public List<SettingItem> getChildren() {
        return this.mChildren;
    }
    
    @Override
    public String getContentDescription(final Resources resources) {
        final StringBuilder sb = new StringBuilder();
        if (this.mTextId != -1) {
            sb.append(resources.getString(this.mTextId));
        }
        else {
            sb.append(this.mText);
        }
        if (this.mAdditionalTextForAccessibility != null && !this.mAdditionalTextForAccessibility.isEmpty()) {
            sb.append(' ');
            sb.append(this.mAdditionalTextForAccessibility);
        }
        if (!this.isSelectable()) {
            sb.append(' ');
            sb.append(resources.getString(2131689592));
        }
        return sb.toString();
    }
    
    public T getData() {
        return this.mData;
    }
    
    @Override
    public int getDialogItemType() {
        return this.mDialogItemType;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public Selectability getSelectability() {
        return this.mSelectability;
    }
    
    @Override
    public String getSubText(final Resources resources) {
        return this.mSubText;
    }
    
    @Override
    public String getText(final Resources resources) {
        if (this.mTextId == -1) {
            return this.mText;
        }
        return resources.getString(this.mTextId);
    }
    
    public String getValueText() {
        return this.mAdditionalTextForAccessibility;
    }
    
    @Override
    public boolean isSelectable() {
        return this.mSelectability == Selectability.SELECTABLE;
    }
    
    @Override
    public boolean isSelected() {
        return this.mIsSelected;
    }
    
    @Override
    public boolean isSoundEnabled() {
        return this.mIsSoundEnabled;
    }
    
    @Override
    public void select() {
        this.mIsSelected = true;
        if (this.mExecutor == null) {
            return;
        }
        this.mExecutor.onExecute(this);
    }
    
    @Override
    public void setSelectability(final Selectability mSelectability) {
        this.mSelectability = mSelectability;
    }
    
    @Override
    public void setSelected(final boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }
}
