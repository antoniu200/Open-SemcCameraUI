package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum PredictiveCapture implements UserSettingValue {
    AUTO(R.string.cam_strings_settings_auto_txt, "auto", 4),
    OFF(R.string.cam_strings_settings_off_txt, "off", 1),
    ON(R.string.cam_strings_settings_on_txt, "on", 4);

    public static final String TAG = "PredictiveCapture";
    private static final int sParameterTextId = 2131690005;
    private final int mCaptureNum;
    private final int mTextId;
    private final String mValue;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690005;
    }

    PredictiveCapture(int i, String str, int i2) {
        this.mTextId = i;
        this.mValue = str;
        this.mCaptureNum = i2;
    }

    public static PredictiveCapture[] getOptions(boolean z, CapturingMode capturingMode) {
        if (!z && capturingMode == CapturingMode.SCENE_RECOGNITION) {
            List<String> list = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).PREDICTIVE_CAPTURE.get();
            if (list.size() != 0) {
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    if (it.next().equals(AUTO.getValue())) {
                        return new PredictiveCapture[]{AUTO, OFF};
                    }
                }
                return new PredictiveCapture[]{OFF};
            }
        }
        return new PredictiveCapture[]{OFF};
    }

    public static PredictiveCapture getDefaultValue(boolean z, CapturingMode capturingMode) {
        if (!z && capturingMode == CapturingMode.SCENE_RECOGNITION) {
            return AUTO;
        }
        return OFF;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.PREDICTIVE_CAPTURE;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return this.mValue;
    }

    public int getCaptureNum() {
        return this.mCaptureNum;
    }
}
