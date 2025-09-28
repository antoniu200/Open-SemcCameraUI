package com.sonymobile.cameracommon.view;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.device.CameraParameters;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RecognizedCondition {
    private final CameraParameters.DeviceStabilityCondition mCondition;
    private final int mIconId;
    private final int mTextId;

    private RecognizedCondition(CameraParameters.DeviceStabilityCondition deviceStabilityCondition, int i, int i2) {
        this.mCondition = deviceStabilityCondition;
        this.mIconId = i;
        this.mTextId = i2;
    }

    public static RecognizedCondition create(CameraParameters.DeviceStabilityCondition deviceStabilityCondition) {
        if (deviceStabilityCondition == null) {
            return new RecognizedCondition(null, -1, -1);
        }
        switch (deviceStabilityCondition) {
            case AUTO:
                return new RecognizedCondition(deviceStabilityCondition, -1, -1);
            case MOTION:
                return new RecognizedCondition(deviceStabilityCondition, R.drawable.cam_condition_motion_icn, -1);
            case STABLE:
                return new RecognizedCondition(deviceStabilityCondition, R.drawable.cam_condition_stable_icn, -1);
            case WALK:
                return new RecognizedCondition(deviceStabilityCondition, R.drawable.cam_condition_walk_icn, -1);
            default:
                return new RecognizedCondition(deviceStabilityCondition, -1, -1);
        }
    }

    public CameraParameters.DeviceStabilityCondition getCondition() {
        return this.mCondition;
    }

    public int getIconId() {
        return this.mIconId;
    }

    public int getTextId() {
        return this.mTextId;
    }
}
