package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum VideoSmileCapture implements UserSettingValue {
    HIGH(SmileCapture.HIGH, R.drawable.cam_vf_video_smile_capture_big_icn),
    MIDDLE(SmileCapture.MIDDLE, R.drawable.cam_vf_video_smile_capture_normal_icn),
    LOW(SmileCapture.LOW, R.drawable.cam_vf_video_smile_capture_faint_icn),
    OFF(SmileCapture.OFF, -1);

    public static final String TAG = "VideoSmileCapture";
    private static final int sParameterTextId = 2131690249;
    private final int mNotificationIconId;
    private final SmileCapture mSmile;
    private final String mValue;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690249;
    }

    VideoSmileCapture(SmileCapture smileCapture, int i) {
        this.mSmile = smileCapture;
        if (this.mSmile.isSmileCaptureOn()) {
            this.mValue = "on";
        } else {
            this.mValue = "off";
        }
        this.mNotificationIconId = i;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.VIDEO_SMILE_CAPTURE;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mSmile.getIconId();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mSmile.getTextId();
    }

    public int getNotificationIconId() {
        return this.mNotificationIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return this.mValue;
    }

    public static VideoSmileCapture[] getOptions(boolean z, CapturingMode capturingMode) {
        if (z) {
            return new VideoSmileCapture[]{OFF};
        }
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).SMILE_DETECTION.get().booleanValue()) {
            return values();
        }
        return new VideoSmileCapture[0];
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public int getIntValue() {
        return this.mSmile.getIntValue();
    }

    public int getDimenId() {
        return this.mSmile.getDimenId();
    }

    public boolean isSmileCaptureOn() {
        return this.mSmile.isSmileCaptureOn();
    }
}
