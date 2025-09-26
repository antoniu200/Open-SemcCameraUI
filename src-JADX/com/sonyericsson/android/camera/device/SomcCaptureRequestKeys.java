package com.sonyericsson.android.camera.device;

import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest;
import java.lang.reflect.InvocationTargetException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SomcCaptureRequestKeys {
    public static final CaptureRequest.Key<Integer> SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER = keyConstructor("com.sonymobile.statistics.objectSelectTrigger", Integer.TYPE);
    public static final CaptureRequest.Key<int[]> SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA = keyConstructor("com.sonymobile.statistics.objectSelectTriggerArea", int[].class);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER = keyConstructor("com.sonymobile.statistics.faceSelectTrigger", Integer.TYPE);
    public static final CaptureRequest.Key<int[]> SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA = keyConstructor("com.sonymobile.statistics.faceSelectTriggerArea", int[].class);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE = keyConstructor("com.sonymobile.statistics.faceSmileScoresMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_STATISTICS_SCENE_DETECT_MODE = keyConstructor("com.sonymobile.statistics.sceneDetectMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE = keyConstructor("com.sonymobile.statistics.conditionDetectMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_STATISTICS_RGBCIR_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.rgbcirMeasureMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_STATISTICS_TOF_MEASURE_MODE = keyConstructor("com.sonymobile.statistics.tofMeasureMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_AE_MODE = keyConstructor("com.sonymobile.control.aeMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_AE_REGION_MODE = keyConstructor("com.sonymobile.control.aeRegionMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_AF_REGION_MODE = keyConstructor("com.sonymobile.control.afRegionMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB = keyConstructor("com.sonymobile.control.awbColorCompensationAb", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM = keyConstructor("com.sonymobile.control.awbColorCompensationGm", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL = keyConstructor("com.sonymobile.control.stillSkinSmoothLevel", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_STILL_HDR_MODE = keyConstructor("com.sonymobile.control.stillHdrMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_POWER_SAVE_MODE = keyConstructor("com.sonymobile.control.powerSaveMode", Integer.TYPE);
    public static final CaptureRequest.Key<Long> SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT = keyConstructor("com.sonymobile.control.exposureTimeLimit", Long.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE = keyConstructor("com.sonymobile.control.distortionCorrectionMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_CONTROL_FUSION_MODE = keyConstructor("com.sonymobile.control.fusionMode", Integer.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_SENSOR_SENSITIVITY_HINT = keyConstructor("com.sonymobile.sensor.sensitivityHint", Integer.TYPE);
    public static final CaptureRequest.Key<Long> SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT = keyConstructor("com.sonymobile.sensor.exposureTimeHint", Long.TYPE);
    public static final CaptureRequest.Key<Integer> SONYMOBILE_DUAL_CAMERA_TARGET_STREAM_SOURCE = keyConstructor("com.sonymobile.dualCamera.targetStreamSource", Integer.TYPE);
    public static final CaptureRequest.Key<Rect> SONYMOBILE_DUAL_CAMERA_SUB_CROP_REGION = keyConstructor("com.sonymobile.dualCamera.subCropRegion", Rect.class);

    private static CaptureRequest.Key<?> keyConstructor(String str, Class<?> cls) {
        try {
            return (CaptureRequest.Key) Class.forName("android.hardware.camera2.CaptureRequest$Key").getConstructor(String.class, Class.class).newInstance(str, cls);
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
