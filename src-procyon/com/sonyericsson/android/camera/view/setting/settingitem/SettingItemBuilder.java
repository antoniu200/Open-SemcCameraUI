// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.settingitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface;

public class SettingItemBuilder<T>
{
    public static final String TAG = "SettingItemBuilder";
    private String mAdditionalTextForAccessibility;
    private final T mData;
    private int mDialogItemType;
    private SettingExecutorInterface<T> mExecutor;
    private int mIconId;
    private boolean mIsSoundEnabled;
    private List<SettingItem> mItems;
    private SettingItem.Selectability mSelectability;
    private boolean mSelected;
    private String mSubText;
    private String mText;
    private int mTextId;
    
    private SettingItemBuilder(final T mData) {
        this.mIconId = -1;
        this.mTextId = -1;
        this.mAdditionalTextForAccessibility = "";
        this.mText = "";
        this.mSubText = null;
        this.mDialogItemType = -1;
        this.mExecutor = null;
        this.mItems = null;
        this.mSelectability = SettingItem.Selectability.SELECTABLE;
        this.mSelected = false;
        this.mIsSoundEnabled = true;
        this.mData = mData;
    }
    
    public static <T> SettingItemBuilder<T> build(final T t) {
        return new SettingItemBuilder<T>(t);
    }
    
    public SettingItemBuilder<T> additionalTextForAccessibility(final String mAdditionalTextForAccessibility) {
        this.mAdditionalTextForAccessibility = mAdditionalTextForAccessibility;
        return this;
    }
    
    public SettingItem commit() {
        TypedSettingItem typedSettingItem;
        if (this.mTextId == -1) {
            typedSettingItem = new TypedSettingItem((T)this.mData, this.mIconId, this.mText, this.mSubText, this.mAdditionalTextForAccessibility, this.mDialogItemType, (SettingExecutorInterface<T>)this.mExecutor, this.mIsSoundEnabled);
        }
        else {
            typedSettingItem = new TypedSettingItem((T)this.mData, this.mIconId, this.mTextId, this.mSubText, this.mAdditionalTextForAccessibility, this.mDialogItemType, (SettingExecutorInterface<T>)this.mExecutor, this.mIsSoundEnabled);
        }
        if (this.mItems != null) {
            final Iterator<SettingItem> iterator = this.mItems.iterator();
            while (iterator.hasNext()) {
                typedSettingItem.getChildren().add(iterator.next());
            }
        }
        typedSettingItem.setSelectability(this.mSelectability);
        typedSettingItem.setSelected(this.mSelected);
        return typedSettingItem;
    }
    
    public SettingItemBuilder<T> dialogItemType(final int mDialogItemType) {
        this.mDialogItemType = mDialogItemType;
        return this;
    }
    
    public SettingItemBuilder<T> enableSound(final boolean mIsSoundEnabled) {
        this.mIsSoundEnabled = mIsSoundEnabled;
        return this;
    }
    
    public SettingItemBuilder<T> executor(final SettingExecutorInterface<T> mExecutor) {
        this.mExecutor = mExecutor;
        return this;
    }
    
    public SettingItemBuilder<T> iconId(final int mIconId) {
        this.mIconId = mIconId;
        return this;
    }
    
    public SettingItemBuilder<T> item(final SettingItem settingItem) {
        if (this.mItems == null) {
            this.mItems = new ArrayList<SettingItem>();
        }
        this.mItems.add(settingItem);
        return this;
    }
    
    public SettingItemBuilder<T> selectability(final SettingItem.Selectability mSelectability) {
        this.mSelectability = mSelectability;
        return this;
    }
    
    public SettingItemBuilder<T> selected(final boolean mSelected) {
        this.mSelected = mSelected;
        return this;
    }
    
    public SettingItemBuilder<T> subText(final String mSubText) {
        this.mSubText = mSubText;
        return this;
    }
    
    public SettingItemBuilder<T> text(final String mText) {
        this.mText = mText;
        return this;
    }
    
    public SettingItemBuilder<T> textId(final int mTextId) {
        this.mTextId = mTextId;
        return this;
    }
}
