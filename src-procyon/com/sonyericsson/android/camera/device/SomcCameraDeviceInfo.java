// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.util.HashSet;
import android.hardware.camera2.CaptureResult$Key;
import java.util.ArrayList;
import android.hardware.camera2.CaptureRequest$Key;
import java.util.List;

public final class SomcCameraDeviceInfo
{
    public static List<CaptureRequest$Key<?>> getAllCaptureRequestKeys() {
        final ArrayList list = new ArrayList();
        list.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_RGBCIR_MEASURE_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_TOF_MEASURE_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_REGION_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AF_REGION_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_POWER_SAVE_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_FUSION_MODE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_SENSITIVITY_HINT);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_DUAL_CAMERA_TARGET_STREAM_SOURCE);
        list.add(SomcCaptureRequestKeys.SONYMOBILE_DUAL_CAMERA_SUB_CROP_REGION);
        return list;
    }
    
    public static List<CaptureResult$Key<?>> getAllCaptureResultKeys() {
        final ArrayList list = new ArrayList();
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_RGBCIR_MEASURE_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_TOF_MEASURE_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_AREA);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SELECT_AREA);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_SCENE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_CONDITION);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_RGBCIR_MEASURE_COLOR);
        list.add(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_TOF_MEASURE_AREA);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AE_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AE_REGION_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AF_REGION_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_POWER_SAVE_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_FUSION_MODE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_FUSION_CONDITION);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_PREVIEW_OUTPUT_STREAM_SOURCE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_STILL_OUTPUT_STREAM_SOURCE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_CONTROL_VIDEO_OUTPUT_STREAM_SOURCE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_SENSOR_SENSITIVITY_HINT);
        list.add(SomcCaptureResultKeys.SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT);
        list.add(SomcCaptureResultKeys.SONYMOBILE_SENSOR_ILLUMINANCE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_LENS_FOCAL_LENGTH);
        list.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_TARGET_STREAM_SOURCE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_SUB_CROP_REGION);
        list.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_SUB_FOCAL_LENGTH);
        list.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_SUB_FOCUS_DISTANCE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_ROTATION_ANGLE);
        list.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_CENTER_SHIFT);
        list.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_EFFECTIVE_AREA);
        list.add(SomcCaptureResultKeys.SONYMOBILE_DUAL_CAMERA_DISTORTION_PARAMETERS);
        return list;
    }
    
    private static HashSet<String> getAllCharacteristicsKeyNames() {
        final HashSet set = new HashSet();
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_OBJECT_TRACKING.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_FACE_SMILE_SCORES_MODE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_SCENE_DETECT_MODE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_CONDITION_DETECT_MODE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_TRACKING_FOCUS_DURING_LOCK.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_RGBCIR_MEASURE_MODE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_TOF_MEASURE_MODE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_MAX_TOF_AREA_COUNT.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_MODES.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_REGION_MODES.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AF_AVAILABLE_REGION_MODES.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB_RANGE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM_RANGE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_SKIN_SMOOTH_LEVEL_RANGE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_HDR_MODES.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_POWER_SAVE_MODES.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_MIN_EXPOSURE_TIME_LIMIT.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_DISTORTION_CORRECTION_MODES.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_FUSION_MODES.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_STILL_HDR_CONFIGURATION_MAP.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_MANUAL_ISO_CONFIGURATION_MAP.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_STILL_PREVIEW_SIZE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_VIDEO_PREVIEW_SIZE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_HDR_VIDEO_PREVIEW_SIZE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_WIDE_ZOOM_TARGET_RATIO.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_FUSION_CONFIGURATION_MAP.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SENSOR_INFO_SENSITIVITY_RANGE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_SENSOR_INFO_FUSION_SENSITIVITY_RANGE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_MODULE_SHIFT.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_SUB_PHYSICAL_SIZE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_SUB_ACTIVE_ARRAY_SIZE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_DISTORTION_PARAM_POINTS.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_AVAILABLE_TARGET_STREAM_SOURCE.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_DUAL_CAMERA_AVAILABLE_STREAM_CONFIGURATION_MAP.getName());
        set.add(SomcCameraCharacteristicsKeys.SONYMOBILE_INFO_SENSOR_NAME.getName());
        return set;
    }
}
