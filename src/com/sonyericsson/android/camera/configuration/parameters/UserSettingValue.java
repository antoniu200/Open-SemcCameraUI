package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface UserSettingValue {
    void apply(UserSettingApplicable userSettingApplicable);

    int getIconId();

    UserSettingKey getKey();

    int getKeyTextId();

    String getName();

    int getTextId();

    String getValue();
}
