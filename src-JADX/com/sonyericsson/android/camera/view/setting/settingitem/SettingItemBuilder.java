package com.sonyericsson.android.camera.view.setting.settingitem;

import com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingItemBuilder<T> {
    public static final String TAG = "SettingItemBuilder";
    private final T mData;
    private int mIconId = -1;
    private int mTextId = -1;
    private String mAdditionalTextForAccessibility = "";
    private String mText = "";
    private String mSubText = null;
    private int mDialogItemType = -1;
    private SettingExecutorInterface<T> mExecutor = null;
    private List<SettingItem> mItems = null;
    private SettingItem.Selectability mSelectability = SettingItem.Selectability.SELECTABLE;
    private boolean mSelected = false;
    private boolean mIsSoundEnabled = true;

    private SettingItemBuilder(T t) {
        this.mData = t;
    }

    public static <T> SettingItemBuilder<T> build(T t) {
        return new SettingItemBuilder<>(t);
    }

    public SettingItem commit() {
        TypedSettingItem typedSettingItem;
        if (this.mTextId == -1) {
            typedSettingItem = new TypedSettingItem(this.mData, this.mIconId, this.mText, this.mSubText, this.mAdditionalTextForAccessibility, this.mDialogItemType, this.mExecutor, this.mIsSoundEnabled);
        } else {
            typedSettingItem = new TypedSettingItem(this.mData, this.mIconId, this.mTextId, this.mSubText, this.mAdditionalTextForAccessibility, this.mDialogItemType, this.mExecutor, this.mIsSoundEnabled);
        }
        if (this.mItems != null) {
            Iterator<SettingItem> it = this.mItems.iterator();
            while (it.hasNext()) {
                typedSettingItem.getChildren().add(it.next());
            }
        }
        typedSettingItem.setSelectability(this.mSelectability);
        typedSettingItem.setSelected(this.mSelected);
        return typedSettingItem;
    }

    public SettingItemBuilder<T> iconId(int i) {
        this.mIconId = i;
        return this;
    }

    public SettingItemBuilder<T> textId(int i) {
        this.mTextId = i;
        return this;
    }

    public SettingItemBuilder<T> subText(String str) {
        this.mSubText = str;
        return this;
    }

    public SettingItemBuilder<T> text(String str) {
        this.mText = str;
        return this;
    }

    public SettingItemBuilder<T> additionalTextForAccessibility(String str) {
        this.mAdditionalTextForAccessibility = str;
        return this;
    }

    public SettingItemBuilder<T> dialogItemType(int i) {
        this.mDialogItemType = i;
        return this;
    }

    public SettingItemBuilder<T> executor(SettingExecutorInterface<T> settingExecutorInterface) {
        this.mExecutor = settingExecutorInterface;
        return this;
    }

    public SettingItemBuilder<T> item(SettingItem settingItem) {
        if (this.mItems == null) {
            this.mItems = new ArrayList();
        }
        this.mItems.add(settingItem);
        return this;
    }

    public SettingItemBuilder<T> selected(boolean z) {
        this.mSelected = z;
        return this;
    }

    public SettingItemBuilder<T> selectability(SettingItem.Selectability selectability) {
        this.mSelectability = selectability;
        return this;
    }

    public SettingItemBuilder<T> enableSound(boolean z) {
        this.mIsSoundEnabled = z;
        return this;
    }
}
