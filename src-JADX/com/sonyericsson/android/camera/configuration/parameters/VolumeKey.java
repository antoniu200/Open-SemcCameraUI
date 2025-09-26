package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum VolumeKey implements UserSettingValue {
    ZOOM(-1, R.string.cam_strings_volumekey_zoom_txt),
    VOLUME(-1, R.string.cam_strings_volumekey_volume_txt),
    HW_CAMERA_KEY(-1, R.string.cam_strings_volumekey_shutter_txt);

    public static final String TAG = "VolumeKey";
    private static final int sParameterTextId = 2131690252;
    private final int mIconId;
    private final int mTextId;
    private String mValue;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690252;
    }

    VolumeKey(int i, int i2) {
        this.mIconId = i;
        this.mTextId = i2;
    }

    public static VolumeKey getDefault() {
        return ZOOM;
    }

    public static VolumeKey[] getOptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(ZOOM);
        arrayList.add(VOLUME);
        arrayList.add(HW_CAMERA_KEY);
        return (VolumeKey[]) arrayList.toArray(new VolumeKey[0]);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.VOLUME_KEY;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return this.mValue;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }
}
