package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.controller.GestureShutter;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum ShutterTrigger implements UserSettingValue {
    SMILE_SHUTTER(R.string.cam_strings_smile_shutter_cy_txt, SmileCapture.MIDDLE, false),
    GESTURE_SHUTTER(R.string.cam_strings_hand_shutter_txt, SmileCapture.OFF, true),
    OFF(R.string.cam_strings_settings_off_txt, SmileCapture.OFF, false);

    public static final String TAG = "ShutterTrigger";
    private static final int sParameterTextId = 2131690121;
    private boolean mGestureShutter;
    private SmileCapture mSmileCapture;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690121;
    }

    ShutterTrigger(int i, SmileCapture smileCapture, boolean z) {
        this.mSmileCapture = smileCapture;
        this.mTextId = i;
        this.mGestureShutter = z;
    }

    public static ShutterTrigger[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).SMILE_DETECTION.get().booleanValue()) {
            arrayList.add(SMILE_SHUTTER);
        }
        if ((capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.SUPERIOR_FRONT) && GestureShutter.isGestureShutterSupported()) {
            arrayList.add(GESTURE_SHUTTER);
        }
        if (arrayList.size() != 0) {
            arrayList.add(OFF);
            ShutterTrigger[] shutterTriggerArr = new ShutterTrigger[arrayList.size()];
            arrayList.toArray(shutterTriggerArr);
            return shutterTriggerArr;
        }
        return new ShutterTrigger[0];
    }

    public static ShutterTrigger getDefaultValue(CapturingMode capturingMode) {
        if ((capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.SUPERIOR_FRONT) && GestureShutter.isGestureShutterSupported()) {
            return GESTURE_SHUTTER;
        }
        return OFF;
    }

    public boolean isGestureShutterOn() {
        return this.mGestureShutter;
    }

    public SmileCapture getSmileCapture() {
        return this.mSmileCapture;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.SHUTTER_TRIGGER;
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
        return toString();
    }
}
