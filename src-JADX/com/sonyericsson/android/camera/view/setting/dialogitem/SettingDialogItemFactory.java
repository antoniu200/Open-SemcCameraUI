package com.sonyericsson.android.camera.view.setting.dialogitem;

import android.content.Context;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingDialogItemFactory {
    public static final int BUTTON = 1;
    public static final int BUTTON_DETAILS = 5;
    public static final int CATEGORY_BUTTON = 3;
    public static final int CATEGORY_SWITCH = 4;
    public static final int END_OF_TYPE_LIST = 6;
    public static final int VALUE_BUTTON = 2;

    public int getDialogItemTypeCount() {
        return 6;
    }

    public SettingDialogItem create(SettingItem settingItem, ViewGroup viewGroup, boolean z) {
        Context context = viewGroup.getContext();
        switch (settingItem.getDialogItemType()) {
            case 1:
                return new SettingButton(context, settingItem, z);
            case 2:
                return new SettingButton(context, settingItem, z);
            case 3:
                return new SettingButton(context, settingItem, z);
            case 4:
                return new SettingButton(context, settingItem, z);
            case 5:
                return new SettingButtonDetails(context, settingItem);
            default:
                throw new IllegalArgumentException("The specified type is unknown. type:" + settingItem.getDialogItemType());
        }
    }
}
