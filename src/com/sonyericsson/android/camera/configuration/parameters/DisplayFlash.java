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
public enum DisplayFlash implements UserSettingValue {
    DISPLAY_AUTO(R.drawable.cam_core_flash_auto_icn, R.string.cam_strings_settings_auto_txt, CameraParameters.DISPLAY_FLASH_MODE_AUTO),
    DISPLAY_ON(R.drawable.cam_core_flash_fill_flash_icn, R.string.cam_strings_flash_fill_flash_txt, CameraParameters.DISPLAY_FLASH_MODE_ON),
    DISPLAY_OFF(R.drawable.cam_core_flash_off_icn, R.string.cam_strings_settings_off_txt, "off");

    public static final String TAG = "DisplayFlash";
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    DisplayFlash(int i, int i2, String str) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mValue = str;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.DISPLAY_FLASH;
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

    public static DisplayFlash[] getOptions(ActionMode actionMode) {
        ArrayList arrayList = new ArrayList();
        if (actionMode.mType == 1) {
            List<String> list = PlatformCapability.getCameraCapability(actionMode.mCameraId).FLASH.get();
            if (!list.isEmpty()) {
                for (DisplayFlash displayFlash : LedOptionsResolver.getInstance().getDisplayFlashOptions(actionMode, list)) {
                    Iterator<String> it = list.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (displayFlash.getValue().equals(it.next())) {
                                arrayList.add(displayFlash);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return (DisplayFlash[]) arrayList.toArray(new DisplayFlash[0]);
    }

    public static boolean isSupported(CameraInfo.CameraId cameraId) {
        return PlatformCapability.isDisplayFlashModeSupported(cameraId);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return LedOptionsResolver.getInstance().getParameterKeyTextId();
    }

    public static DisplayFlash getDisplayFlashFromParameterString(String str) {
        for (DisplayFlash displayFlash : values()) {
            if (displayFlash.getValue().equals(str)) {
                return displayFlash;
            }
        }
        return null;
    }

    public static DisplayFlash getDefaultValue() {
        return LedOptionsResolver.getInstance().getDefaultDisplayFlash();
    }

    public static int getParameterKeyTitleTextId() {
        return LedOptionsResolver.getInstance().getParameterKeyTitleTextId();
    }
}
