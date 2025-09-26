package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum ShutterSound implements UserSettingValue {
    SOUND1(-1, R.string.cam_strings_settings_on_txt, true, "sound1/"),
    OFF(-1, R.string.cam_strings_settings_off_txt, false, "sound0/");

    public static final String TAG = "ShutterSound";
    private static final int sParameterTextId = 2131689664;
    private final Boolean mBooleanValue;
    private final String mDirectoryName;
    private final int mIconId;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131689664;
    }

    ShutterSound(int i, int i2, Boolean bool, String str) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mBooleanValue = bool;
        this.mDirectoryName = str;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.SHUTTER_SOUND;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return toString();
    }

    public Boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    public static ShutterSound[] getOptions(boolean z) {
        ArrayList arrayList = new ArrayList();
        for (ShutterSound shutterSound : values()) {
            if (shutterSound.mBooleanValue.booleanValue()) {
                arrayList.add(shutterSound);
            } else if (!z) {
                arrayList.add(shutterSound);
            }
        }
        return (ShutterSound[]) arrayList.toArray(new ShutterSound[0]);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public String getDirectoryName() {
        return this.mDirectoryName;
    }
}
