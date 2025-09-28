package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum ObjectTracking implements UserSettingValue {
    ON(-1, R.string.cam_strings_settings_on_txt, FocusMode.OBJECT_TRACKING),
    OFF(-1, R.string.cam_strings_settings_off_txt, FocusMode.FACE_DETECTION);

    public static final String TAG = "ObjectTracking";
    private static final int sParameterTextId = 2131689847;
    private final FocusMode mFocusMode;
    private final int mIconId;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131689847;
    }

    ObjectTracking(int i, int i2, FocusMode focusMode) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mFocusMode = focusMode;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.OBJECT_TRACKING;
    }

    public static ObjectTracking[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).OBJECT_TRACKING.get().booleanValue()) {
            arrayList.add(ON);
        }
        arrayList.add(OFF);
        return (ObjectTracking[]) arrayList.toArray(new ObjectTracking[0]);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public FocusMode getFocusMode() {
        return this.mFocusMode;
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

    public static ObjectTracking getDefault(CapturingMode capturingMode) {
        if (!PlatformCapability.getCameraCapability(capturingMode.getCameraId()).OBJECT_TRACKING.get().booleanValue()) {
            return OFF;
        }
        if (capturingMode == CapturingMode.VIDEO) {
            return ON;
        }
        return OFF;
    }
}
