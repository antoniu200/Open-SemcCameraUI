package com.sonyericsson.android.camera.configuration.parameters;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum SlowMotion implements UserSettingValue {
    SUPER_SLOW_MOTION(-1, R.string.cam_strings_slow_motion_settings_super_txt, R.string.cam_strings_slow_motion_super_hint_1_txt, VideoSize.HD),
    SUPER_SLOW_SHOT(-1, R.string.cam_strings_slow_motion_settings_shot_txt, R.string.cam_strings_slow_motion_shot_hint_1_txt, VideoSize.HD),
    STANDARD_SLOW_MOTION(-1, R.string.cam_strings_slow_motion_settings_standard_txt, R.string.cam_strings_slow_motion_standard_hint_1_txt, VideoSize.HD_120FPS),
    OFF(-1, R.string.cam_strings_settings_off_txt, R.string.cam_strings_settings_off_txt, VideoSize.HD);

    public static final String TAG = "SlowMotion";
    private static final int sParameterTextId = 2131690158;
    private final int mDescriptionTextId;
    private final int mIconId;
    private final int mTextId;
    private final VideoSize mVideoSize;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690158;
    }

    SlowMotion(int i, int i2, int i3, VideoSize videoSize) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mDescriptionTextId = i3;
        this.mVideoSize = videoSize;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.SLOW_MOTION;
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
        return toString();
    }

    public String getDescriptionText(Context context) {
        switch (this) {
            case SUPER_SLOW_MOTION:
            case SUPER_SLOW_SHOT:
                return context.getString(this.mDescriptionTextId, "960");
            case STANDARD_SLOW_MOTION:
                return context.getString(this.mDescriptionTextId, "120");
            default:
                throw new IllegalStateException("This value is not supported.");
        }
    }

    public static SlowMotion[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        if (capturingMode == CapturingMode.SLOW_MOTION) {
            if (PlatformCapability.isSuperSlowMotionSupported(capturingMode.getCameraId())) {
                arrayList.add(SUPER_SLOW_MOTION);
                arrayList.add(SUPER_SLOW_SHOT);
            }
            arrayList.add(STANDARD_SLOW_MOTION);
        } else {
            arrayList.add(OFF);
        }
        return (SlowMotion[]) arrayList.toArray(new SlowMotion[0]);
    }

    public static SlowMotion getDefaultValue(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.SLOW_MOTION) {
            if (PlatformCapability.isSuperSlowMotionSupported(capturingMode.getCameraId())) {
                return SUPER_SLOW_MOTION;
            }
            return STANDARD_SLOW_MOTION;
        }
        return OFF;
    }

    public VideoSize getVideoSize() {
        return this.mVideoSize;
    }
}
