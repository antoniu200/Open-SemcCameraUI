package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum TouchIntention implements UserSettingValue {
    OBJECT_TRACKING(-1, R.string.cam_strings_focus_mode_object_tracking_txt),
    FOCUS_ONLY(-1, R.string.cam_strings_touch_to_focus_adjust_exposure_only_focus_txt),
    FOCUS_AND_EXPOSURE(-1, R.string.cam_strings_touch_to_focus_adjust_exposure_focus_and_exposure_txt);

    public static final String TAG = "TouchIntention";
    private static final int sParameterTextId = 2131690198;
    private final int mIconId;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690198;
    }

    TouchIntention(int i, int i2) {
        this.mIconId = i;
        this.mTextId = i2;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.TOUCH_INTENTION;
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

    public static TouchIntention[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        if (capturingMode != CapturingMode.SCENE_RECOGNITION && capturingMode != CapturingMode.NORMAL) {
            return (TouchIntention[]) arrayList.toArray(new TouchIntention[0]);
        }
        CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
        if (cameraCapability.METERING.get().contains("user")) {
            if (capturingMode == CapturingMode.NORMAL && cameraCapability.OBJECT_TRACKING.get().booleanValue()) {
                arrayList.add(OBJECT_TRACKING);
            }
            arrayList.add(FOCUS_ONLY);
            arrayList.add(FOCUS_AND_EXPOSURE);
        }
        return (TouchIntention[]) arrayList.toArray(new TouchIntention[0]);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public static TouchIntention getDefaultValue(CapturingMode capturingMode) {
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).METERING.get().contains("user")) {
            return FOCUS_AND_EXPOSURE;
        }
        return FOCUS_ONLY;
    }
}
