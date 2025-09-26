// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.lang.reflect.InvocationTargetException;
import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest$Key;

public final class SomcCaptureRequestKeys
{
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_AE_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_AE_REGION_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_AF_REGION_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE;
    public static final CaptureRequest$Key<Long> SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_FUSION_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_POWER_SAVE_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_STILL_HDR_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL;
    public static final CaptureRequest$Key<Rect> SONYMOBILE_DUAL_CAMERA_SUB_CROP_REGION;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_DUAL_CAMERA_TARGET_STREAM_SOURCE;
    public static final CaptureRequest$Key<Long> SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_SENSOR_SENSITIVITY_HINT;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER;
    public static final CaptureRequest$Key<int[]> SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER;
    public static final CaptureRequest$Key<int[]> SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_STATISTICS_RGBCIR_MEASURE_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_STATISTICS_SCENE_DETECT_MODE;
    public static final CaptureRequest$Key<Integer> SONYMOBILE_STATISTICS_TOF_MEASURE_MODE;
    
    static {
        SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER = keyConstructor("com.sonymobile.statistics.objectSelectTrigger", Integer.TYPE);
        SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA = keyConstructor("com.sonymobile.statistics.objectSelectTriggerArea", int[].class);
        SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER = keyConstructor("com.sonymobile.statistics.faceSelectTrigger", Integer.TYPE);
        SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA = keyConstructor("com.sonymobile.statistics.faceSelectTriggerArea", int[].class);
        SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE = keyConstructor("com.sonymobile.statistics.faceSmileScoresMode", Integer.TYPE);
        SONYMOBILE_STATISTICS_SCENE_DETECT_MODE = keyConstructor("com.sonymobile.statistics.sceneDetectMode", Integer.TYPE);
        SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE = keyConstructor("com.sonymobile.statistics.conditionDetectMode", Integer.TYPE);
        SONYMOBILE_STATISTICS_RGBCIR_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.rgbcirMeasureMode", Integer.TYPE);
        SONYMOBILE_STATISTICS_TOF_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.tofMeasureMode", Integer.TYPE);
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
        SONYMOBILE_SENSOR_SENSITIVITY_HINT = keyConstructor("com.sonymobile.sensor.sensitivityHint", Integer.TYPE);
        SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT = keyConstructor("com.sonymobile.sensor.exposureTimeHint", Long.TYPE);
        SONYMOBILE_DUAL_CAMERA_TARGET_STREAM_SOURCE = keyConstructor("com.sonymobile.dualCamera.targetStreamSource", Integer.TYPE);
        SONYMOBILE_DUAL_CAMERA_SUB_CROP_REGION = keyConstructor("com.sonymobile.dualCamera.subCropRegion", Rect.class);
    }
    
    private static CaptureRequest$Key<?> keyConstructor(final String s, final Class<?> clazz) {
        try {
            return (CaptureRequest$Key<?>)Class.forName("android.hardware.camera2.CaptureRequest$Key").getConstructor(String.class, Class.class).newInstance(s, clazz);
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
