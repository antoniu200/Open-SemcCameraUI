// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.lang.reflect.InvocationTargetException;
import android.hardware.camera2.CameraCharacteristics$Key;

public final class SomcCameraCharacteristicsKeys
{
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AE_AVAILABLE_MODES;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AE_AVAILABLE_REGION_MODES;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AF_AVAILABLE_REGION_MODES;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_DISTORTION_CORRECTION_MODES;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_FUSION_MODES;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_POWER_SAVE_MODES;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_STILL_HDR_MODES;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_STILL_SKIN_SMOOTH_LEVEL_RANGE;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB_RANGE;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM_RANGE;
    public static final CameraCharacteristics$Key<Long> SONYMOBILE_CONTROL_MIN_EXPOSURE_TIME_LIMIT;
    public static final CameraCharacteristics$Key<long[]> SONYMOBILE_DUAL_CAMERA_AVAILABLE_STREAM_CONFIGURATION_MAP;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_DUAL_CAMERA_AVAILABLE_TARGET_STREAM_SOURCE;
    public static final CameraCharacteristics$Key<Integer> SONYMOBILE_DUAL_CAMERA_DISTORTION_PARAM_POINTS;
    public static final CameraCharacteristics$Key<float[]> SONYMOBILE_DUAL_CAMERA_MODULE_SHIFT;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_DUAL_CAMERA_SUB_ACTIVE_ARRAY_SIZE;
    public static final CameraCharacteristics$Key<float[]> SONYMOBILE_DUAL_CAMERA_SUB_PHYSICAL_SIZE;
    public static final CameraCharacteristics$Key<String> SONYMOBILE_INFO_SENSOR_NAME;
    public static final CameraCharacteristics$Key<long[]> SONYMOBILE_SCALER_AVAILABLE_FUSION_CONFIGURATION_MAP;
    public static final CameraCharacteristics$Key<long[]> SONYMOBILE_SCALER_AVAILABLE_MANUAL_ISO_CONFIGURATION_MAP;
    public static final CameraCharacteristics$Key<long[]> SONYMOBILE_SCALER_AVAILABLE_STILL_HDR_CONFIGURATION_MAP;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_SCALER_PREFERRED_HDR_VIDEO_PREVIEW_SIZE;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_SCALER_PREFERRED_STILL_PREVIEW_SIZE;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_SCALER_PREFERRED_VIDEO_PREVIEW_SIZE;
    public static final CameraCharacteristics$Key<Float> SONYMOBILE_SCALER_WIDE_ZOOM_TARGET_RATIO;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_SENSOR_INFO_FUSION_SENSITIVITY_RANGE;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_SENSOR_INFO_SENSITIVITY_RANGE;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_CONDITION_DETECT_MODE;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_FACE_SMILE_SCORES_MODE;
    public static final CameraCharacteristics$Key<Boolean> SONYMOBILE_STATISTICS_INFO_AVAILABLE_OBJECT_TRACKING;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_RGBCIR_MEASURE_MODE;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_SCENE_DETECT_MODE;
    public static final CameraCharacteristics$Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_TOF_MEASURE_MODE;
    public static final CameraCharacteristics$Key<Boolean> SONYMOBILE_STATISTICS_INFO_AVAILABLE_TRACKING_FOCUS_DURING_LOCK;
    public static final CameraCharacteristics$Key<Integer> SONYMOBILE_STATISTICS_INFO_MAX_TOF_AREA_COUNT;
    
    static {
        SONYMOBILE_STATISTICS_INFO_AVAILABLE_OBJECT_TRACKING = keyConstructor("com.sonymobile.statistics.info.availableObjectTracking", Boolean.TYPE);
        SONYMOBILE_STATISTICS_INFO_AVAILABLE_FACE_SMILE_SCORES_MODE = keyConstructor("com.sonymobile.statistics.info.availableFaceSmileScoresMode", int[].class);
        SONYMOBILE_STATISTICS_INFO_AVAILABLE_SCENE_DETECT_MODE = keyConstructor("com.sonymobile.statistics.info.availableSceneDetectMode", int[].class);
        SONYMOBILE_STATISTICS_INFO_AVAILABLE_CONDITION_DETECT_MODE = keyConstructor("com.sonymobile.statistics.info.availableConditionDetectMode", int[].class);
        SONYMOBILE_STATISTICS_INFO_AVAILABLE_TRACKING_FOCUS_DURING_LOCK = keyConstructor("com.sonymobile.statistics.info.availableTrackingFocusDuringLock", Boolean.TYPE);
        SONYMOBILE_STATISTICS_INFO_AVAILABLE_RGBCIR_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.info.availableRgbcirMeasureMode", int[].class);
        SONYMOBILE_STATISTICS_INFO_AVAILABLE_TOF_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.info.availableTofMeasureMode", int[].class);
        SONYMOBILE_STATISTICS_INFO_MAX_TOF_AREA_COUNT = keyConstructor("com.sonymobile.statistics.info.maxTofAreaCount", Integer.TYPE);
        SONYMOBILE_CONTROL_AE_AVAILABLE_MODES = keyConstructor("com.sonymobile.control.aeAvailableModes", int[].class);
        SONYMOBILE_CONTROL_AE_AVAILABLE_REGION_MODES = keyConstructor("com.sonymobile.control.aeAvailableRegionModes", int[].class);
        SONYMOBILE_CONTROL_AF_AVAILABLE_REGION_MODES = keyConstructor("com.sonymobile.control.afAvailableRegionModes", int[].class);
        SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB_RANGE = keyConstructor("com.sonymobile.control.awbColorCompensationAbRange", int[].class);
        SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM_RANGE = keyConstructor("com.sonymobile.control.awbColorCompensationGmRange", int[].class);
        SONYMOBILE_CONTROL_AVAILABLE_STILL_SKIN_SMOOTH_LEVEL_RANGE = keyConstructor("com.sonymobile.control.availableStillSkinSmoothLevelRange", int[].class);
        SONYMOBILE_CONTROL_AVAILABLE_STILL_HDR_MODES = keyConstructor("com.sonymobile.control.availableStillHdrModes", int[].class);
        SONYMOBILE_CONTROL_AVAILABLE_POWER_SAVE_MODES = keyConstructor("com.sonymobile.control.availablePowerSaveModes", int[].class);
        SONYMOBILE_CONTROL_MIN_EXPOSURE_TIME_LIMIT = keyConstructor("com.sonymobile.control.minExposureTimeLimit", Long.TYPE);
        SONYMOBILE_CONTROL_AVAILABLE_DISTORTION_CORRECTION_MODES = keyConstructor("com.sonymobile.control.availableDistortionCorrectionModes", int[].class);
        SONYMOBILE_CONTROL_AVAILABLE_FUSION_MODES = keyConstructor("com.sonymobile.control.availableFusionModes", int[].class);
        SONYMOBILE_SCALER_AVAILABLE_STILL_HDR_CONFIGURATION_MAP = keyConstructor("com.sonymobile.scaler.availableStillHdrConfigurationMap", long[].class);
        SONYMOBILE_SCALER_AVAILABLE_MANUAL_ISO_CONFIGURATION_MAP = keyConstructor("com.sonymobile.scaler.availableManualIsoConfigurationMap", long[].class);
        SONYMOBILE_SCALER_PREFERRED_STILL_PREVIEW_SIZE = keyConstructor("com.sonymobile.scaler.preferredStillPreviewSize", int[].class);
        SONYMOBILE_SCALER_PREFERRED_VIDEO_PREVIEW_SIZE = keyConstructor("com.sonymobile.scaler.preferredVideoPreviewSize", int[].class);
        SONYMOBILE_SCALER_PREFERRED_HDR_VIDEO_PREVIEW_SIZE = keyConstructor("com.sonymobile.scaler.preferredHdrVideoPreviewSize", int[].class);
        SONYMOBILE_SCALER_WIDE_ZOOM_TARGET_RATIO = keyConstructor("com.sonymobile.scaler.wideZoomTargetRatio", Float.TYPE);
        SONYMOBILE_SCALER_AVAILABLE_FUSION_CONFIGURATION_MAP = keyConstructor("com.sonymobile.scaler.availableFusionConfigurationMap", long[].class);
        SONYMOBILE_SENSOR_INFO_SENSITIVITY_RANGE = keyConstructor("com.sonymobile.sensor.info.sensitivityRange", int[].class);
        SONYMOBILE_SENSOR_INFO_FUSION_SENSITIVITY_RANGE = keyConstructor("com.sonymobile.sensor.info.fusionSensitivityRange", int[].class);
        SONYMOBILE_DUAL_CAMERA_MODULE_SHIFT = keyConstructor("com.sonymobile.dualCamera.moduleShift", float[].class);
        SONYMOBILE_DUAL_CAMERA_SUB_PHYSICAL_SIZE = keyConstructor("com.sonymobile.dualCamera.subPhysicalSize", float[].class);
        SONYMOBILE_DUAL_CAMERA_SUB_ACTIVE_ARRAY_SIZE = keyConstructor("com.sonymobile.dualCamera.subActiveArraySize", int[].class);
        SONYMOBILE_DUAL_CAMERA_DISTORTION_PARAM_POINTS = keyConstructor("com.sonymobile.dualCamera.distortionParamPoints", Integer.TYPE);
        SONYMOBILE_DUAL_CAMERA_AVAILABLE_TARGET_STREAM_SOURCE = keyConstructor("com.sonymobile.dualCamera.availableTargetStreamSource", int[].class);
        SONYMOBILE_DUAL_CAMERA_AVAILABLE_STREAM_CONFIGURATION_MAP = keyConstructor("com.sonymobile.dualCamera.availableStreamConfigurationMap", long[].class);
        SONYMOBILE_INFO_SENSOR_NAME = keyConstructor("com.sonymobile.info.sensorName", String.class);
    }
    
    private static CameraCharacteristics$Key<?> keyConstructor(final String s, final Class<?> clazz) {
        try {
            return (CameraCharacteristics$Key<?>)Class.forName("android.hardware.camera2.CameraCharacteristics$Key").getConstructor(String.class, Class.class).newInstance(s, clazz);
        }
        catch (final InvocationTargetException ex) {
            ex.printStackTrace();
        }
        catch (final InstantiationException ex2) {
            ex2.printStackTrace();
        }
        catch (final IllegalAccessException ex3) {
            ex3.printStackTrace();
        }
        catch (final NoSuchMethodException ex4) {
            ex4.printStackTrace();
        }
        catch (final RuntimeException ex5) {
            ex5.printStackTrace();
        }
        catch (final ClassNotFoundException ex6) {
            ex6.printStackTrace();
        }
        return null;
    }
}
