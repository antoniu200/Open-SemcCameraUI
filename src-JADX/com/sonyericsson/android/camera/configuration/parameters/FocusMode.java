package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum FocusMode implements UserSettingValue {
    SINGLE(-1, R.string.cam_strings_focus_mode_single_af_txt, CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE, CameraParameters.FOCUS_MODE_CONTINUOUS_VIDEO, CameraParameters.FOCUS_AREA_CENTER, true),
    FIXED(-1, R.string.cam_strings_focus_mode_single_af_txt, CameraParameters.FOCUS_MODE_FIXED, CameraParameters.FOCUS_MODE_FIXED, CameraParameters.FOCUS_AREA_CENTER, false),
    FACE_DETECTION(-1, R.string.cam_strings_focus_mode_face_detection_txt, CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE, CameraParameters.FOCUS_MODE_CONTINUOUS_VIDEO, CameraParameters.FOCUS_AREA_CENTER, true),
    TOUCH_FOCUS(-1, R.string.cam_strings_focus_mode_touch_focus_txt, CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE, CameraParameters.FOCUS_MODE_CONTINUOUS_VIDEO, CameraParameters.FOCUS_AREA_CENTER, true),
    INFINITY(-1, R.string.cam_strings_focus_mode_single_af_txt, CameraParameters.FOCUS_MODE_INFINITY, CameraParameters.FOCUS_MODE_INFINITY, CameraParameters.FOCUS_AREA_CENTER, true),
    OBJECT_TRACKING(-1, R.string.cam_strings_focus_mode_object_tracking_txt, CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE, CameraParameters.FOCUS_MODE_CONTINUOUS_VIDEO, CameraParameters.FOCUS_AREA_CENTER, true);

    public static final String TAG = "FocusMode";
    private static final int sParameterTextId = 2131689851;
    private final String mFocusArea;
    private final int mIconId;
    private final boolean mSuccessSound;
    private final int mTextId;
    private String mValue;
    private String mValueForVideo;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131689851;
    }

    FocusMode(int i, int i2, String str, String str2, String str3, boolean z) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mValue = str;
        this.mValueForVideo = str2;
        this.mFocusArea = str3;
        this.mSuccessSound = z;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.FOCUS_MODE;
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

    public String getValueForVideo() {
        return this.mValueForVideo;
    }

    public String getFocusArea() {
        return this.mFocusArea;
    }

    public boolean isSuccessSound() {
        return this.mSuccessSound;
    }

    public static FocusMode[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
        List<String> list = cameraCapability.FOCUS_MODE.get();
        List<String> list2 = cameraCapability.FOCUS_AREA.get();
        if (!list.isEmpty()) {
            ArrayList arrayList2 = new ArrayList();
            for (FocusMode focusMode : values()) {
                if (list.contains(focusMode.getValue())) {
                    arrayList2.add(focusMode);
                }
            }
            if (list2.isEmpty()) {
                arrayList = arrayList2;
            } else {
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    FocusMode focusMode2 = (FocusMode) it.next();
                    if (list2.contains(focusMode2.getFocusArea())) {
                        arrayList.add(focusMode2);
                    }
                }
            }
            if (cameraCapability.MAX_NUM_FACE.get().intValue() < 1) {
                remove(FACE_DETECTION, arrayList);
            }
            if (cameraCapability.MAX_NUM_FOCUS_AREA.get().intValue() < 1) {
                remove(TOUCH_FOCUS, arrayList);
            }
            if (!cameraCapability.OBJECT_TRACKING.get().booleanValue()) {
                remove(OBJECT_TRACKING, arrayList);
            }
        }
        return (FocusMode[]) arrayList.toArray(new FocusMode[0]);
    }

    private static void remove(FocusMode focusMode, List<FocusMode> list) {
        if (list.contains(focusMode)) {
            list.remove(focusMode);
        }
    }

    private static FocusMode[] getExpectedOptions(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        if (strArr != null) {
            for (String str : strArr) {
                arrayList.add(valueOf(FocusMode.class, str));
            }
            return (FocusMode[]) arrayList.toArray(new FocusMode[0]);
        }
        return values();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public static void updateValue(CameraInfo.CameraId cameraId, List<String> list) {
        if (cameraId != CameraInfo.CameraId.BACK || list.contains(SINGLE.getValue())) {
            return;
        }
        SINGLE.mValue = "auto";
    }

    public static FocusMode getDefaultValue(CapturingMode capturingMode) {
        switch (capturingMode) {
            case FRONT_VIDEO:
            case SUPERIOR_FRONT:
            case FRONT_PHOTO:
                if (!PlatformCapability.isFocusSupported(capturingMode.getCameraId())) {
                    break;
                } else {
                    break;
                }
        }
        return FACE_DETECTION;
    }
}
