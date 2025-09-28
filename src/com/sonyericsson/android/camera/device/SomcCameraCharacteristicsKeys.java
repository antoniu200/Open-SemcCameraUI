package com.sonyericsson.android.camera.device;

import android.hardware.camera2.CameraCharacteristics;
import java.lang.reflect.InvocationTargetException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SomcCameraCharacteristicsKeys {
    public static final CameraCharacteristics.Key<Boolean> SONYMOBILE_STATISTICS_INFO_AVAILABLE_OBJECT_TRACKING = keyConstructor("com.sonymobile.statistics.info.availableObjectTracking", Boolean.TYPE);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_FACE_SMILE_SCORES_MODE = keyConstructor("com.sonymobile.statistics.info.availableFaceSmileScoresMode", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_SCENE_DETECT_MODE = keyConstructor("com.sonymobile.statistics.info.availableSceneDetectMode", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_CONDITION_DETECT_MODE = keyConstructor("com.sonymobile.statistics.info.availableConditionDetectMode", int[].class);
    public static final CameraCharacteristics.Key<Boolean> SONYMOBILE_STATISTICS_INFO_AVAILABLE_TRACKING_FOCUS_DURING_LOCK = keyConstructor("com.sonymobile.statistics.info.availableTrackingFocusDuringLock", Boolean.TYPE);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_RGBCIR_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.info.availableRgbcirMeasureMode", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_STATISTICS_INFO_AVAILABLE_TOF_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.info.availableTofMeasureMode", int[].class);
    public static final CameraCharacteristics.Key<Integer> SONYMOBILE_STATISTICS_INFO_MAX_TOF_AREA_COUNT = keyConstructor("com.sonymobile.statistics.info.maxTofAreaCount", Integer.TYPE);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AE_AVAILABLE_MODES = keyConstructor("com.sonymobile.control.aeAvailableModes", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AE_AVAILABLE_REGION_MODES = keyConstructor("com.sonymobile.control.aeAvailableRegionModes", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AF_AVAILABLE_REGION_MODES = keyConstructor("com.sonymobile.control.afAvailableRegionModes", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB_RANGE = keyConstructor("com.sonymobile.control.awbColorCompensationAbRange", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM_RANGE = keyConstructor("com.sonymobile.control.awbColorCompensationGmRange", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_STILL_SKIN_SMOOTH_LEVEL_RANGE = keyConstructor("com.sonymobile.control.availableStillSkinSmoothLevelRange", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_STILL_HDR_MODES = keyConstructor("com.sonymobile.control.availableStillHdrModes", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_POWER_SAVE_MODES = keyConstructor("com.sonymobile.control.availablePowerSaveModes", int[].class);
    public static final CameraCharacteristics.Key<Long> SONYMOBILE_CONTROL_MIN_EXPOSURE_TIME_LIMIT = keyConstructor("com.sonymobile.control.minExposureTimeLimit", Long.TYPE);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_DISTORTION_CORRECTION_MODES = keyConstructor("com.sonymobile.control.availableDistortionCorrectionModes", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_CONTROL_AVAILABLE_FUSION_MODES = keyConstructor("com.sonymobile.control.availableFusionModes", int[].class);
    public static final CameraCharacteristics.Key<long[]> SONYMOBILE_SCALER_AVAILABLE_STILL_HDR_CONFIGURATION_MAP = keyConstructor("com.sonymobile.scaler.availableStillHdrConfigurationMap", long[].class);
    public static final CameraCharacteristics.Key<long[]> SONYMOBILE_SCALER_AVAILABLE_MANUAL_ISO_CONFIGURATION_MAP = keyConstructor("com.sonymobile.scaler.availableManualIsoConfigurationMap", long[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_SCALER_PREFERRED_STILL_PREVIEW_SIZE = keyConstructor("com.sonymobile.scaler.preferredStillPreviewSize", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_SCALER_PREFERRED_VIDEO_PREVIEW_SIZE = keyConstructor("com.sonymobile.scaler.preferredVideoPreviewSize", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_SCALER_PREFERRED_HDR_VIDEO_PREVIEW_SIZE = keyConstructor("com.sonymobile.scaler.preferredHdrVideoPreviewSize", int[].class);
    public static final CameraCharacteristics.Key<Float> SONYMOBILE_SCALER_WIDE_ZOOM_TARGET_RATIO = keyConstructor("com.sonymobile.scaler.wideZoomTargetRatio", Float.TYPE);
    public static final CameraCharacteristics.Key<long[]> SONYMOBILE_SCALER_AVAILABLE_FUSION_CONFIGURATION_MAP = keyConstructor("com.sonymobile.scaler.availableFusionConfigurationMap", long[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_SENSOR_INFO_SENSITIVITY_RANGE = keyConstructor("com.sonymobile.sensor.info.sensitivityRange", int[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_SENSOR_INFO_FUSION_SENSITIVITY_RANGE = keyConstructor("com.sonymobile.sensor.info.fusionSensitivityRange", int[].class);
    public static final CameraCharacteristics.Key<float[]> SONYMOBILE_DUAL_CAMERA_MODULE_SHIFT = keyConstructor("com.sonymobile.dualCamera.moduleShift", float[].class);
    public static final CameraCharacteristics.Key<float[]> SONYMOBILE_DUAL_CAMERA_SUB_PHYSICAL_SIZE = keyConstructor("com.sonymobile.dualCamera.subPhysicalSize", float[].class);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_DUAL_CAMERA_SUB_ACTIVE_ARRAY_SIZE = keyConstructor("com.sonymobile.dualCamera.subActiveArraySize", int[].class);
    public static final CameraCharacteristics.Key<Integer> SONYMOBILE_DUAL_CAMERA_DISTORTION_PARAM_POINTS = keyConstructor("com.sonymobile.dualCamera.distortionParamPoints", Integer.TYPE);
    public static final CameraCharacteristics.Key<int[]> SONYMOBILE_DUAL_CAMERA_AVAILABLE_TARGET_STREAM_SOURCE = keyConstructor("com.sonymobile.dualCamera.availableTargetStreamSource", int[].class);
    public static final CameraCharacteristics.Key<long[]> SONYMOBILE_DUAL_CAMERA_AVAILABLE_STREAM_CONFIGURATION_MAP = keyConstructor("com.sonymobile.dualCamera.availableStreamConfigurationMap", long[].class);
    public static final CameraCharacteristics.Key<String> SONYMOBILE_INFO_SENSOR_NAME = keyConstructor("com.sonymobile.info.sensorName", String.class);

    private static CameraCharacteristics.Key<?> keyConstructor(String str, Class<?> cls) {
        try {
            return (CameraCharacteristics.Key) Class.forName("android.hardware.camera2.CameraCharacteristics$Key").getConstructor(String.class, Class.class).newInstance(str, cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (InstantiationException e3) {
            e3.printStackTrace();
            return null;
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            return null;
        } catch (RuntimeException e5) {
            e5.printStackTrace();
            return null;
        } catch (InvocationTargetException e6) {
            e6.printStackTrace();
            return null;
        }
    }
}
