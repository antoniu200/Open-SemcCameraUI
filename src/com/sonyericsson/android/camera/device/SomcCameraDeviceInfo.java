package com.sonyericsson.android.camera.device;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SomcCameraDeviceInfo {
    private static HashSet<String> getAllCharacteristicsKeyNames() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_OBJECT_TRACKING.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_FACE_SMILE_SCORES_MODE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_SCENE_DETECT_MODE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_CONDITION_DETECT_MODE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_TRACKING_FOCUS_DURING_LOCK.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_RGBCIR_MEASURE_MODE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_TOF_MEASURE_MODE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_MAX_TOF_AREA_COUNT.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_MODES.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_REGION_MODES.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AF_AVAILABLE_REGION_MODES.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB_RANGE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM_RANGE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_SKIN_SMOOTH_LEVEL_RANGE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_HDR_MODES.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_POWER_SAVE_MODES.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_MIN_EXPOSURE_TIME_LIMIT.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_DISTORTION_CORRECTION_MODES.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_FUSION_MODES.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_STILL_HDR_CONFIGURATION_MAP.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_MANUAL_ISO_CONFIGURATION_MAP.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_STILL_PREVIEW_SIZE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_VIDEO_PREVIEW_SIZE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_HDR_VIDEO_PREVIEW_SIZE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_WIDE_ZOOM_TARGET_RATIO.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_FUSION_CONFIGURATION_MAP.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SENSOR_INFO_SENSITIVITY_RANGE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SENSOR_INFO_FUSION_SENSITIVITY_RANGE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_MODULE_SHIFT.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_SUB_PHYSICAL_SIZE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_SUB_ACTIVE_ARRAY_SIZE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_DISTORTION_PARAM_POINTS.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_AVAILABLE_TARGET_STREAM_SOURCE.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_AVAILABLE_STREAM_CONFIGURATION_MAP.getName());
        hashSet.add(SomcCameraCharacteristicsKeys.SONYMOBILE_INFO_SENSOR_NAME.getName());
        return hashSet;
    }

    public static List<CaptureResult.Key<?>> getAllCaptureResultKeys() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_RGBCIR_MEASURE_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_TOF_MEASURE_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_AREA);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SELECT_AREA);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_SCENE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_CONDITION);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_RGBCIR_MEASURE_COLOR);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_TOF_MEASURE_AREA);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AE_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AE_REGION_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AF_REGION_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_POWER_SAVE_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_FUSION_MODE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_FUSION_CONDITION);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_PREVIEW_OUTPUT_STREAM_SOURCE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_STILL_OUTPUT_STREAM_SOURCE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_VIDEO_OUTPUT_STREAM_SOURCE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_SENSOR_SENSITIVITY_HINT);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_SENSOR_ILLUMINANCE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_LENS_FOCAL_LENGTH);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_TARGET_STREAM_SOURCE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_SUB_CROP_REGION);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_SUB_FOCAL_LENGTH);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_SUB_FOCUS_DISTANCE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_ROTATION_ANGLE);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_CENTER_SHIFT);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_EFFECTIVE_AREA);
        arrayList.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_DISTORTION_PARAMETERS);
        return arrayList;
    }

    public static List<CaptureRequest.Key<?>> getAllCaptureRequestKeys() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_RGBCIR_MEASURE_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_TOF_MEASURE_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_REGION_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AF_REGION_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_POWER_SAVE_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_FUSION_MODE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_SENSITIVITY_HINT);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_DUAL_CAMERA_TARGET_STREAM_SOURCE);
        arrayList.add(SomcCaptureRequestKeys.SONYMOBILE_DUAL_CAMERA_SUB_CROP_REGION);
        return arrayList;
    }
}
