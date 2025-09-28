package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum PhotoLight implements UserSettingValue {
    ON(R.drawable.cam_core_flash_torch_icn, R.string.cam_strings_settings_on_txt, CameraParameters.FLASH_MODE_TORCH, true),
    OFF(R.drawable.cam_core_flash_torch_off_icn, R.string.cam_strings_settings_off_txt, "off", false);

    public static final String TAG = "PhotoLight";
    private static final int sParameterTextId = 2131689841;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131689841;
    }

    PhotoLight(int i, int i2, String str, boolean z) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mValue = str;
        this.mBooleanValue = z;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.PHOTO_LIGHT;
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
        return this.mValue;
    }

    public static PhotoLight[] getOptions(ActionMode actionMode) {
        ArrayList arrayList = new ArrayList();
        List<String> list = PlatformCapability.getCameraCapability(actionMode.mCameraId).FLASH.get();
        if (!list.isEmpty()) {
            for (PhotoLight photoLight : LedOptionsResolver.getInstance().getPhotoLightOptions(actionMode, list)) {
                Iterator<String> it = list.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (photoLight.getValue().equals(it.next())) {
                            arrayList.add(photoLight);
                            break;
                        }
                    }
                }
            }
        }
        return (PhotoLight[]) arrayList.toArray(new PhotoLight[0]);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    public static PhotoLight getPhotoLightFromParameterString(String str) {
        for (PhotoLight photoLight : values()) {
            if (photoLight.getValue().equals(str)) {
                return photoLight;
            }
        }
        return null;
    }
}
