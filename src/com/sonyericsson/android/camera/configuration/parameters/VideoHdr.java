package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum VideoHdr implements UserSettingValue {
    HDR_ON(R.drawable.cam_core_hdr_shortcut_on_icn, R.string.cam_strings_settings_on_txt, CameraParameters.VIDEO_HDR_ON),
    HDR_OFF(R.drawable.cam_core_hdr_shortcut_off_icn, R.string.cam_strings_settings_off_txt, "off");

    private static final int sParameterTextId = 2131690222;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690222;
    }

    VideoHdr(int i, int i2, String str) {
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
        return UserSettingKey.VIDEO_HDR;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
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

    public static VideoHdr[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        if (PlatformCapability.isVideoHdrSupported(capturingMode.getCameraId()) && CapturingMode.VIDEO == capturingMode) {
            arrayList.add(HDR_ON);
            arrayList.add(HDR_OFF);
        }
        return (VideoHdr[]) arrayList.toArray(new VideoHdr[0]);
    }

    public static VideoHdr getDefault() {
        return HDR_OFF;
    }
}
