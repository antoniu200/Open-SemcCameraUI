// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.lang.reflect.InvocationTargetException;
import android.graphics.Rect;
import android.hardware.camera2.CaptureResult$Key;

public final class SomcCaptureResultKeys
{
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_AE_MODE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_AE_REGION_MODE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_AF_REGION_MODE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE;
    public static final CaptureResult$Key<Long> SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_FUSION_CONDITION;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_FUSION_MODE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_POWER_SAVE_MODE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_PREVIEW_OUTPUT_STREAM_SOURCE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_STILL_HDR_MODE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_STILL_OUTPUT_STREAM_SOURCE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL;
    public static final CaptureResult$Key<Integer> SONYMOBILE_CONTROL_VIDEO_OUTPUT_STREAM_SOURCE;
    public static final CaptureResult$Key<float[]> SONYMOBILE_DUAL_CAMERA_CENTER_SHIFT;
    public static final CaptureResult$Key<float[]> SONYMOBILE_DUAL_CAMERA_DISTORTION_PARAMETERS;
    public static final CaptureResult$Key<int[]> SONYMOBILE_DUAL_CAMERA_EFFECTIVE_AREA;
    public static final CaptureResult$Key<float[]> SONYMOBILE_DUAL_CAMERA_ROTATION_ANGLE;
    public static final CaptureResult$Key<Rect> SONYMOBILE_DUAL_CAMERA_SUB_CROP_REGION;
    public static final CaptureResult$Key<Float> SONYMOBILE_DUAL_CAMERA_SUB_FOCAL_LENGTH;
    public static final CaptureResult$Key<Float> SONYMOBILE_DUAL_CAMERA_SUB_FOCUS_DISTANCE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_DUAL_CAMERA_TARGET_STREAM_SOURCE;
    public static final CaptureResult$Key<Float> SONYMOBILE_LENS_FOCAL_LENGTH;
    public static final CaptureResult$Key<Long> SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT;
    public static final CaptureResult$Key<Float> SONYMOBILE_SENSOR_ILLUMINANCE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_SENSOR_SENSITIVITY_HINT;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_CONDITION;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE;
    public static final CaptureResult$Key<int[]> SONYMOBILE_STATISTICS_FACE_SELECT_AREA;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER;
    public static final CaptureResult$Key<int[]> SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA;
    public static final CaptureResult$Key<int[]> SONYMOBILE_STATISTICS_FACE_SMILE_SCORES;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE;
    public static final CaptureResult$Key<int[]> SONYMOBILE_STATISTICS_OBJECT_SELECT_AREA;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER;
    public static final CaptureResult$Key<int[]> SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_RGBCIR_MEASURE_COLOR;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_RGBCIR_MEASURE_MODE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_SCENE;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_SCENE_DETECT_MODE;
    public static final CaptureResult$Key<int[]> SONYMOBILE_STATISTICS_TOF_MEASURE_AREA;
    public static final CaptureResult$Key<Integer> SONYMOBILE_STATISTICS_TOF_MEASURE_MODE;
    
    static {
        SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER = keyConstructor("com.sonymobile.statistics.objectSelectTrigger", Integer.TYPE);
        SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA = keyConstructor("com.sonymobile.statistics.objectSelectTriggerArea", int[].class);
        SONYMOBILE_STATISTICS_OBJECT_SELECT_AREA = keyConstructor("com.sonymobile.statistics.objectSelectArea", int[].class);
        SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER = keyConstructor("com.sonymobile.statistics.faceSelectTrigger", Integer.TYPE);
        SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA = keyConstructor("com.sonymobile.statistics.faceSelectTriggerArea", int[].class);
        SONYMOBILE_STATISTICS_FACE_SELECT_AREA = keyConstructor("com.sonymobile.statistics.faceSelectArea", int[].class);
        SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE = keyConstructor("com.sonymobile.statistics.faceSmileScoresMode", Integer.TYPE);
        SONYMOBILE_STATISTICS_FACE_SMILE_SCORES = keyConstructor("com.sonymobile.statistics.faceSmileScores", int[].class);
        SONYMOBILE_STATISTICS_SCENE_DETECT_MODE = keyConstructor("com.sonymobile.statistics.sceneDetectMode", Integer.TYPE);
        SONYMOBILE_STATISTICS_SCENE = keyConstructor("com.sonymobile.statistics.scene", Integer.TYPE);
        SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE = keyConstructor("com.sonymobile.statistics.conditionDetectMode", Integer.TYPE);
        SONYMOBILE_STATISTICS_CONDITION = keyConstructor("com.sonymobile.statistics.condition", Integer.TYPE);
        SONYMOBILE_STATISTICS_RGBCIR_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.rgbcirMeasureMode", Integer.TYPE);
        SONYMOBILE_STATISTICS_RGBCIR_MEASURE_COLOR = keyConstructor("com.sonymobile.statistics.rgbcirMeasureColor", Integer.TYPE);
        SONYMOBILE_STATISTICS_TOF_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.tofMeasureMode", Integer.TYPE);
        SONYMOBILE_STATISTICS_TOF_MEASURE_AREA = keyConstructor("com.sonymobile.statistics.tofMeasureArea", int[].class);
        SONYMOBILE_CONTROL_AE_MODE = keyConstructor("com.sonymobile.control.aeMode", Integer.TYPE);
        SONYMOBILE_CONTROL_AE_REGION_MODE = keyConstructor("com.sonymobile.control.aeRegionMode", Integer.TYPE);
        SONYMOBILE_CONTROL_AF_REGION_MODE = keyConstructor("com.sonymobile.control.afRegionMode", Integer.TYPE);
        SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB = keyConstructor("com.sonymobile.control.awbColorCompensationAb", Integer.TYPE);
        SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM = keyConstructor("com.sonymobile.control.awbColorCompensationGm", Integer.TYPE);
        SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL = keyConstructor("com.sonymobile.control.stillSkinSmoothLevel", Integer.TYPE);
        SONYMOBILE_CONTROL_STILL_HDR_MODE = keyConstructor("com.sonymobile.control.stillHdrMode", Integer.TYPE);
        SONYMOBILE_CONTROL_POWER_SAVE_MODE = keyConstructor("com.sonymobile.control.powerSaveMode", Integer.TYPE);
        SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT = keyConstructor("com.sonymobile.control.exposureTimeLimit", Long.TYPE);
        SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE = keyConstructor("com.sonymobile.control.distortionCorrectionMode", Integer.TYPE);
        SONYMOBILE_CONTROL_FUSION_MODE = keyConstructor("com.sonymobile.control.fusionMode", Integer.TYPE);
        SONYMOBILE_CONTROL_FUSION_CONDITION = keyConstructor("com.sonymobile.control.fusionCondition", Integer.TYPE);
        SONYMOBILE_CONTROL_PREVIEW_OUTPUT_STREAM_SOURCE = keyConstructor("com.sonymobile.control.previewOutputStreamSource", Integer.TYPE);
        SONYMOBILE_CONTROL_STILL_OUTPUT_STREAM_SOURCE = keyConstructor("com.sonymobile.control.stillOutputStreamSource", Integer.TYPE);
        SONYMOBILE_CONTROL_VIDEO_OUTPUT_STREAM_SOURCE = keyConstructor("com.sonymobile.control.videoOutputStreamSource", Integer.TYPE);
        SONYMOBILE_SENSOR_SENSITIVITY_HINT = keyConstructor("com.sonymobile.sensor.sensitivityHint", Integer.TYPE);
        SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT = keyConstructor("com.sonymobile.sensor.exposureTimeHint", Long.TYPE);
        SONYMOBILE_SENSOR_ILLUMINANCE = keyConstructor("com.sonymobile.sensor.illuminance", Float.TYPE);
        SONYMOBILE_LENS_FOCAL_LENGTH = keyConstructor("com.sonymobile.lens.focalLength", Float.TYPE);
        SONYMOBILE_DUAL_CAMERA_TARGET_STREAM_SOURCE = keyConstructor("com.sonymobile.dualCamera.targetStreamSource", Integer.TYPE);
        SONYMOBILE_DUAL_CAMERA_SUB_CROP_REGION = keyConstructor("com.sonymobile.dualCamera.subCropRegion", Rect.class);
        SONYMOBILE_DUAL_CAMERA_SUB_FOCAL_LENGTH = keyConstructor("com.sonymobile.dualCamera.subFocalLength", Float.TYPE);
        SONYMOBILE_DUAL_CAMERA_SUB_FOCUS_DISTANCE = keyConstructor("com.sonymobile.dualCamera.subFocusDistance", Float.TYPE);
        SONYMOBILE_DUAL_CAMERA_ROTATION_ANGLE = keyConstructor("com.sonymobile.dualCamera.rotationAngle", float[].class);
        SONYMOBILE_DUAL_CAMERA_CENTER_SHIFT = keyConstructor("com.sonymobile.dualCamera.centerShift", float[].class);
        SONYMOBILE_DUAL_CAMERA_EFFECTIVE_AREA = keyConstructor("com.sonymobile.dualCamera.effectiveArea", int[].class);
        SONYMOBILE_DUAL_CAMERA_DISTORTION_PARAMETERS = keyConstructor("com.sonymobile.dualCamera.distortionParameters", float[].class);
    }
    
    private static CaptureResult$Key<?> keyConstructor(final String s, final Class<?> clazz) {
        try {
            return (CaptureResult$Key<?>)Class.forName("android.hardware.camera2.CaptureResult$Key").getConstructor(String.class, Class.class).newInstance(s, clazz);
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
