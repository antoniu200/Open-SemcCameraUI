package com.sonyericsson.android.camera.view.setting.settingitem;

import android.content.res.Resources;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface SettingItem {

    public enum Selectability {
        SELECTABLE,
        UNSELECTABLE,
        RESTRICTED
    }

    boolean compareData(SettingItem settingItem);

    boolean compareData(Object obj);

    List<SettingItem> getChildren();

    String getContentDescription(Resources resources);

    int getDialogItemType();

    int getIconId();

    Selectability getSelectability();

    String getSubText(Resources resources);

    String getText(Resources resources);

    boolean isSelectable();

    boolean isSelected();

    boolean isSoundEnabled();

    void select();

    void setSelectability(Selectability selectability);

    void setSelected(boolean z);
}
