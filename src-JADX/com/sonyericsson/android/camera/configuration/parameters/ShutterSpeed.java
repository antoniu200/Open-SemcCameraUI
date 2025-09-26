package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.Constants;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonymobile.android.media.MediaRecorder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum ShutterSpeed implements UserSettingValue {
    AUTO(-1, R.string.cam_strings_image_quality_control_shutter_speed_auto_txt, "auto", -1),
    D1(-1, R.string.cam_strings_image_quality_control_shutter_speed_1_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 1),
    D2(-1, R.string.cam_strings_image_quality_control_shutter_speed_0_5_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 2),
    D4(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_4_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 4),
    D8(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_8_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 8),
    D15(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_15_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 15),
    D30(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_30_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 30),
    D60(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_60_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 60),
    D125(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_125_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 125),
    D250(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_250_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 250),
    D500(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_500_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, Constants.INTERVAL_OPEN_CAMERA),
    D1000(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_1000_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 1000),
    D2000(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_2000_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, MediaRecorder.MEDIA_RECORDER_TRACK_INFO_LIST_END),
    D4000(-1, R.string.cam_strings_image_quality_control_shutter_speed_denominator_4000_txt, CameraParameters.AE_MODE_SHUTTER_PRIO, 4000);

    private static final int INVALID_VALUE = -1;
    private static int mIndexOfDefault = 1;
    private final String mAeMode;
    private final int mIconId;
    private final int mTextId;
    private final int mValue;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 0;
    }

    ShutterSpeed(int i, int i2, String str, int i3) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mAeMode = str;
        this.mValue = i3;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.SHUTTER_SPEED;
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
        return this.mAeMode;
    }

    public int getShutterSpeedValue() {
        return this.mValue;
    }

    public long getShutterSpeedInNanoMillis() {
        return 1000000000 / this.mValue;
    }

    public static int getIndexOfDefault() {
        return mIndexOfDefault;
    }

    public static ShutterSpeed[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
        List<String> list = cameraCapability.AE.get();
        if (list.isEmpty()) {
            return (ShutterSpeed[]) arrayList.toArray(new ShutterSpeed[0]);
        }
        if (list.contains(AUTO.getValue())) {
            arrayList.add(AUTO);
            if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT || capturingMode == CapturingMode.FRONT_PHOTO || capturingMode.getType() == 2) {
                return (ShutterSpeed[]) arrayList.toArray(new ShutterSpeed[0]);
            }
        }
        if (!list.contains(CameraParameters.AE_MODE_SHUTTER_PRIO)) {
            return (ShutterSpeed[]) arrayList.toArray(new ShutterSpeed[0]);
        }
        List<String> list2 = cameraCapability.SHUTTER_SPEED_VALUES.get();
        for (ShutterSpeed shutterSpeed : values()) {
            if (list2.contains(Long.toString(shutterSpeed.getShutterSpeedInNanoMillis()))) {
                arrayList.add(shutterSpeed);
                if (shutterSpeed.getShutterSpeedValue() <= D125.getShutterSpeedValue()) {
                    mIndexOfDefault = arrayList.size() - 1;
                }
            }
        }
        return (ShutterSpeed[]) arrayList.toArray(new ShutterSpeed[0]);
    }
}
