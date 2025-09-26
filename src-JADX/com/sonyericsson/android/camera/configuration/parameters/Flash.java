package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum Flash implements UserSettingValue {
    AUTO(R.drawable.cam_core_flash_auto_icn, R.string.cam_strings_settings_auto_txt, "auto", true),
    ON(R.drawable.cam_core_flash_fill_flash_icn, R.string.cam_strings_flash_fill_flash_txt, "on", false),
    RED_EYE(R.drawable.cam_core_flash_red_eye_reduc_icn, R.string.cam_strings_flash_red_eye_reduc_txt, CameraParameters.FLASH_MODE_RED_EYE, true),
    OFF(R.drawable.cam_core_flash_off_icn, R.string.cam_strings_settings_off_txt, "off", false),
    LED_ON(R.drawable.cam_core_flash_torch_icn, R.string.cam_strings_flash_torch_txt, CameraParameters.FLASH_MODE_TORCH, false),
    LED_OFF(R.drawable.cam_core_flash_torch_off_icn, R.string.cam_strings_settings_off_txt, "off", false),
    PHOTO_LIGHT_ON_AS_FLASH(PhotoLight.ON.getIconId(), PhotoLight.ON.getTextId(), PhotoLight.ON.getValue(), false);

    public static final String TAG = "Flash";
    private final int mIconId;
    private final boolean mIsSceneDependent;
    private final int mTextId;
    private final String mValue;

    Flash(int i, int i2, String str, boolean z) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mValue = str;
        this.mIsSceneDependent = z;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.FLASH;
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

    public boolean isSceneDependent() {
        return this.mIsSceneDependent;
    }

    public static Flash[] getOptions(ActionMode actionMode) {
        ArrayList arrayList = new ArrayList();
        if (actionMode.mType == 1) {
            List<String> list = PlatformCapability.getCameraCapability(actionMode.mCameraId).FLASH.get();
            if (!list.isEmpty()) {
                for (Flash flash : LedOptionsResolver.getInstance().getFlashOptions(actionMode, list)) {
                    Iterator<String> it = list.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (flash.getValue().equals(it.next())) {
                                arrayList.add(flash);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return (Flash[]) arrayList.toArray(new Flash[0]);
    }

    public static boolean isSupported(CameraInfo.CameraId cameraId) {
        return PlatformCapability.isFlashModeSupported(cameraId);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return LedOptionsResolver.getInstance().getParameterKeyTextId();
    }

    public static Flash getFlashFromParameterString(String str) {
        for (Flash flash : values()) {
            if (flash.getValue().equals(str)) {
                return flash;
            }
        }
        return null;
    }

    public static Flash getDefaultValue() {
        return LedOptionsResolver.getInstance().getDefaultFlash();
    }

    public static int getParameterKeyTitleTextId() {
        return LedOptionsResolver.getInstance().getParameterKeyTitleTextId();
    }
}
