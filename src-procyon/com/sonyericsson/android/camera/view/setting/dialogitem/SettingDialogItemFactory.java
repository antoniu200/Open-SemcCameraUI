// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.dialogitem;

import android.content.Context;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;

public class SettingDialogItemFactory
{
    public static final int BUTTON = 1;
    public static final int BUTTON_DETAILS = 5;
    public static final int CATEGORY_BUTTON = 3;
    public static final int CATEGORY_SWITCH = 4;
    public static final int END_OF_TYPE_LIST = 6;
    public static final int VALUE_BUTTON = 2;
    
    public SettingDialogItem create(final SettingItem settingItem, final ViewGroup viewGroup, final boolean b) {
        final Context context = viewGroup.getContext();
        switch (settingItem.getDialogItemType()) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("The specified type is unknown. type:");
                sb.append(settingItem.getDialogItemType());
                throw new IllegalArgumentException(sb.toString());
            }
            case 5: {
                return new SettingButtonDetails(context, settingItem);
            }
            case 4: {
                return new SettingButton(context, settingItem, b);
            }
            case 3: {
                return new SettingButton(context, settingItem, b);
            }
            case 2: {
                return new SettingButton(context, settingItem, b);
            }
            case 1: {
                return new SettingButton(context, settingItem, b);
            }
        }
    }
    
    public int getDialogItemTypeCount() {
        return 6;
    }
}
