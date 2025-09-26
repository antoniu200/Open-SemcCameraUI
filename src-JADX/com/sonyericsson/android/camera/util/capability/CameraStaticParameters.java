package com.sonyericsson.android.camera.util.capability;

import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import android.view.SurfaceHolder;
import com.sonyericsson.android.camera.Constants;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.device.SomcCameraCharacteristicsKeys;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.android.media.MediaRecorder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class CameraStaticParameters {
    private static final int FORMAT_IMPLEMENTATION_DEFINED = 34;
    private static final int FORMAT_JPEG = 33;
    public static final String SENSOR_NAME1_BEAGLE = "SOI13BS1";
    public static final String SENSOR_NAME1_BLAKISTON = "SOI20BS0";
    public static final String SENSOR_NAME1_PANSY = "LGI05BN1";
    public static final String SENSOR_NAME1_POODLE = "LGI13BS0";
    public static final String SENSOR_NAME1_SNAPPER = "LGI08BS0";
    public static final String SENSOR_NAME2_BEAGLE = "SEM13BS1";
    public static final String SENSOR_NAME2_BLAKISTON = "SOI20BS2";
    public static final String SENSOR_NAME2_PANSY = "CHI05BN1";
    public static final String SENSOR_NAME2_POODLE = "MTM13BS0";
    public static final String SENSOR_NAME2_SNAPPER = "CHI08BS0";
    public static final String SENSOR_NAME_COOPER = "SOS20FW0";
    public static final String SENSOR_NAME_COOPER_TMP = "SOI20BSA";
    public static final String SENSOR_NAME_NONE = "";
    public static final Integer[] SHUTTER_SPEED_INVERSE_VALUES = {1, 2, 4, 8, 15, 30, 60, 125, 250, Integer.valueOf(Constants.INTERVAL_OPEN_CAMERA), 1000, Integer.valueOf(MediaRecorder.MEDIA_RECORDER_TRACK_INFO_LIST_END), 4000};
    private static final String TAG = "CameraStaticParameters";
    private CameraCharacteristics mCharacteristics;
    private StreamConfigurationMap mStreamConfigurationMap;

    public CameraStaticParameters(CameraCharacteristics cameraCharacteristics) {
        this.mCharacteristics = cameraCharacteristics;
        this.mStreamConfigurationMap = (StreamConfigurationMap) this.mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
    }

    private <T> T tryGetCharacteristics(CameraCharacteristics.Key<T> key) {
        try {
            return (T) this.mCharacteristics.get(key);
        } catch (IllegalArgumentException unused) {
            CamLog.e("tryGetCharacteristics: Unknown key: " + key.getName());
            return null;
        }
    }

    public List<String> getSupportedPowerSaveModes() {
        ArrayList arrayList = new ArrayList();
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_POWER_SAVE_MODES);
        if (iArr != null) {
            for (int i : iArr) {
                switch (i) {
                    case 0:
                        arrayList.add("off");
                        break;
                    case 1:
                        arrayList.add(CameraParameters.POWER_SAVING_MODE_LOW_POWER);
                        break;
                    case 2:
                        arrayList.add(CameraParameters.POWER_SAVING_MODE_ULTRA_LOW_POWER);
                        break;
                }
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedPowerSaveModes() : " + arrayList);
        }
        return arrayList;
    }

    public long getMinExposureTimeLimit() {
        Long l = (Long) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_MIN_EXPOSURE_TIME_LIMIT);
        if (CamLog.VERBOSE) {
            CamLog.d("getMinExposureTimeLimit() : " + l);
        }
        if (l == null) {
            return 0L;
        }
        return l.longValue();
    }

    public float getMacroValueForManualFocus() {
        Float f = (Float) this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
        if (CamLog.VERBOSE) {
            CamLog.d("getMacroValueForManualFocus() : " + f);
        }
        return f.floatValue();
    }

    public boolean isManualFocusSupported() {
        for (int i : (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)) {
            if (i == 0 && ((Float) this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE)).floatValue() > 0.0f) {
                return true;
            }
        }
        return false;
    }

    public List<String> getSupportedFocusModes() {
        ArrayList arrayList = new ArrayList();
        for (int i : (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)) {
            switch (i) {
                case 0:
                    if (((Float) this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE)).floatValue() == 0.0f) {
                        arrayList.add(CameraParameters.FOCUS_MODE_FIXED);
                        break;
                    } else {
                        arrayList.add(CameraParameters.FOCUS_MODE_MANUAL);
                        arrayList.add(CameraParameters.FOCUS_MODE_INFINITY);
                        break;
                    }
                case 1:
                    arrayList.add("auto");
                    break;
                case 3:
                    arrayList.add(CameraParameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    break;
                case 4:
                    arrayList.add(CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    break;
            }
        }
        if (arrayList.size() == 0) {
            arrayList.add(CameraParameters.FOCUS_MODE_FIXED);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedFocusModes() : " + arrayList);
        }
        return arrayList;
    }

    public int getMaxNumFocusAreas() {
        Integer num = (Integer) this.mCharacteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF);
        if (CamLog.VERBOSE) {
            CamLog.d("getMaxNumFocusAreas() : " + num);
        }
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public List<String> getSupportedFocusAreaModes() {
        ArrayList arrayList = new ArrayList();
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AF_AVAILABLE_REGION_MODES);
        if (iArr == null) {
            return arrayList;
        }
        for (int i : iArr) {
            switch (i) {
                case 0:
                    arrayList.add(CameraParameters.FOCUS_AREA_CENTER);
                    break;
                case 1:
                    arrayList.add("multi");
                    break;
                case 2:
                    arrayList.add("face");
                    break;
                case 3:
                    arrayList.add("user");
                    break;
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedFocusAreaModes() : " + arrayList);
        }
        return arrayList;
    }

    public int getMaxExposureCompensation() {
        return ((Integer) ((Range) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE)).getUpper()).intValue();
    }

    public int getMinExposureCompensation() {
        return ((Integer) ((Range) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE)).getLower()).intValue();
    }

    public float getExposureCompensationStep() {
        return ((Rational) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP)).floatValue();
    }

    public List<String> getSupportedAeRegionModes() {
        ArrayList arrayList = new ArrayList();
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_REGION_MODES);
        if (iArr == null) {
            return arrayList;
        }
        for (int i : iArr) {
            switch (i) {
                case 0:
                    arrayList.add(CameraParameters.AE_REGION_MODE_CENTER_WEIGHTED);
                    break;
                case 1:
                    arrayList.add(CameraParameters.AE_REGION_MODE_FRAME_AVERAGE);
                    break;
                case 2:
                    arrayList.add(CameraParameters.AE_REGION_MODE_SPOT);
                    break;
                case 3:
                    arrayList.add("multi");
                    break;
                case 4:
                    arrayList.add("face");
                    break;
                case 5:
                    arrayList.add("user");
                    break;
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedAeRegionModes() : " + arrayList);
        }
        return arrayList;
    }

    public List<String> getSupportedAeModes() {
        ArrayList arrayList = new ArrayList();
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_MODES);
        if (iArr == null) {
            return arrayList;
        }
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        for (int i : iArr) {
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                    z = true;
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                    z2 = true;
                    break;
                case 9:
                case 10:
                case 11:
                case 12:
                    z3 = true;
                    break;
                case 13:
                case 14:
                    z4 = true;
                    break;
            }
        }
        if (z) {
            arrayList.add("auto");
        }
        if (z2) {
            arrayList.add(CameraParameters.AE_MODE_ISO_PRIO);
        }
        if (z3) {
            arrayList.add(CameraParameters.AE_MODE_SHUTTER_PRIO);
        }
        if (z4) {
            arrayList.add(CameraParameters.AE_MODE_SEMI_AUTO);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedAeModes() : " + arrayList);
        }
        return arrayList;
    }

    public List<String> getSupportedWhiteBalance() {
        ArrayList arrayList = new ArrayList();
        for (int i : (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES)) {
            arrayList.add(CameraParameterConverter.AwbMode.getApi1Value(i));
        }
        return arrayList;
    }

    public List<String> getSupportedShutterSpeedValues() {
        ArrayList arrayList = new ArrayList();
        Range range = (Range) this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
        Long l = (Long) range.getLower();
        Long l2 = (Long) range.getUpper();
        for (int i = 0; i < SHUTTER_SPEED_INVERSE_VALUES.length; i++) {
            Long lValueOf = Long.valueOf(1000000000 / SHUTTER_SPEED_INVERSE_VALUES[i].intValue());
            if (l.longValue() <= lValueOf.longValue() && lValueOf.longValue() <= l2.longValue()) {
                arrayList.add(lValueOf.toString());
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedShutterSpeedValues() : " + arrayList);
        }
        return arrayList;
    }

    public long getMaxShutterSpeed() {
        return ((Long) ((Range) this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE)).getUpper()).longValue();
    }

    public long getMinShutterSpeed() {
        return ((Long) ((Range) this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE)).getLower()).longValue();
    }

    private static String flatten(int[] iArr) {
        if (iArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iArr.length; i++) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(iArr[i]);
        }
        return sb.toString();
    }

    public Range<Integer> getSupportedIsoRange() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SENSOR_INFO_SENSITIVITY_RANGE);
        if (iArr == null) {
            return (Range) this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedIsoRange() : " + flatten(iArr));
        }
        return new Range<>(Integer.valueOf(iArr[0]), Integer.valueOf(iArr[1]));
    }

    public List<String> getSupportedFlashModes() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("off");
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_MODES);
        if (iArr != null) {
            for (int i : iArr) {
                switch (i) {
                    case 2:
                        arrayList.add("auto");
                        break;
                    case 3:
                        arrayList.add("on");
                        break;
                    case 4:
                        arrayList.add(CameraParameters.FLASH_MODE_RED_EYE);
                        break;
                    default:
                        switch (i) {
                            case 15:
                                arrayList.add(CameraParameters.DISPLAY_FLASH_MODE_AUTO);
                                break;
                            case 16:
                                arrayList.add(CameraParameters.DISPLAY_FLASH_MODE_ON);
                                break;
                        }
                }
            }
        }
        if (((Boolean) this.mCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)).booleanValue()) {
            arrayList.add(CameraParameters.FLASH_MODE_TORCH);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedFlashModes() : " + arrayList);
        }
        return arrayList;
    }

    public List<String> getSupportedStillHdrValues() {
        ArrayList arrayList = new ArrayList();
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_HDR_MODES);
        if (iArr != null) {
            for (int i : iArr) {
                switch (i) {
                    case 0:
                        arrayList.add("off");
                        break;
                    case 1:
                        arrayList.add(CameraParameters.STILL_HDR_ON);
                        break;
                }
            }
        }
        if (isConditionDetectionSupported()) {
            arrayList.add("auto");
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedStillHdrValues() : " + arrayList);
        }
        return arrayList;
    }

    public List<Rect> getStillHdrSupportedPictureSizes() {
        ArrayList arrayList = new ArrayList();
        long[] jArr = (long[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_STILL_HDR_CONFIGURATION_MAP);
        if (jArr == null || jArr.length % 4 != 0) {
            return arrayList;
        }
        for (int i = 0; i < jArr.length; i += 4) {
            if (jArr[i] == 33) {
                arrayList.add(new Rect(0, 0, (int) jArr[i + 1], (int) jArr[i + 2]));
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getStillHdrSupportedPictureSizes() : " + arrayList);
        }
        return arrayList;
    }

    public float getMaxZoomRatio() {
        return ((Float) this.mCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM)).floatValue();
    }

    public float getWideZoomTargetRatio() {
        Float f = (Float) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_WIDE_ZOOM_TARGET_RATIO);
        if (CamLog.VERBOSE) {
            CamLog.d("getWideZoomTargetRatio() : " + f);
        }
        if (f == null) {
            return 1.0f;
        }
        return f.floatValue();
    }

    public int getMaxAwbColorCompensationAb() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB_RANGE);
        if (iArr == null) {
            return 0;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getMaxAwbColorCompensationAb() : " + iArr[1]);
        }
        return iArr[1];
    }

    public int getMinAwbColorCompensationAb() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB_RANGE);
        if (iArr == null) {
            return 0;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getMinAwbColorCompensationAb() : " + iArr[0]);
        }
        return iArr[0];
    }

    public boolean isSceneDetectionSupported() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_SCENE_DETECT_MODE);
        if (iArr == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            String[] strArr = new String[1];
            StringBuilder sb = new StringBuilder();
            sb.append("isSceneDetectionSupported() : ");
            sb.append(iArr.length == 2);
            strArr[0] = sb.toString();
            CamLog.d(strArr);
        }
        return iArr.length == 2;
    }

    public int getMaxSoftSkinLevel() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_SKIN_SMOOTH_LEVEL_RANGE);
        if (iArr == null) {
            return 0;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getMaxSoftSkinLevel() : " + iArr[1]);
        }
        return iArr[1];
    }

    public int getMinSoftSkinLevel() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_SKIN_SMOOTH_LEVEL_RANGE);
        if (iArr == null) {
            return 0;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getMinSoftSkinLevel() : " + iArr[0]);
        }
        return iArr[0];
    }

    public boolean isConditionDetectionSupported() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_CONDITION_DETECT_MODE);
        if (iArr == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            String[] strArr = new String[1];
            StringBuilder sb = new StringBuilder();
            sb.append("isConditionDetectionSupported() : ");
            sb.append(iArr.length == 2);
            strArr[0] = sb.toString();
            CamLog.d(strArr);
        }
        return iArr.length == 2;
    }

    public int getMaxNumDetectedFaces() {
        return ((Integer) this.mCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT)).intValue();
    }

    public boolean isFaceDetectionAvailable() {
        int[] iArr = (int[]) this.mCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
        return iArr != null && iArr.length > 1 && getMaxNumDetectedFaces() > 0;
    }

    public boolean isSmileDetectionAvailable() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_FACE_SMILE_SCORES_MODE);
        if (iArr == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            String[] strArr = new String[1];
            StringBuilder sb = new StringBuilder();
            sb.append("isSmileDetectionAvailable() : ");
            sb.append(iArr.length == 2);
            strArr[0] = sb.toString();
            CamLog.d(strArr);
        }
        return iArr.length == 2;
    }

    public boolean isObjectTrackingSupported() {
        Boolean bool = (Boolean) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_OBJECT_TRACKING);
        if (bool == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("isObjectTrackingSupported() : " + bool.booleanValue());
        }
        return bool.booleanValue();
    }

    public boolean isTrackingFocusDuringLockSupported() {
        Boolean bool = (Boolean) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_TRACKING_FOCUS_DURING_LOCK);
        if (bool == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("isTrackingFocusDuringLockSupported() : " + bool.booleanValue());
        }
        return bool.booleanValue();
    }

    public List<Rect> getSupportedPreviewSizes() {
        Size[] outputSizes = this.mStreamConfigurationMap.getOutputSizes(SurfaceHolder.class);
        ArrayList arrayList = new ArrayList();
        if (outputSizes == null) {
            return arrayList;
        }
        for (Size size : outputSizes) {
            arrayList.add(new Rect(0, 0, size.getWidth(), size.getHeight()));
            if (CamLog.VERBOSE) {
                CamLog.d("getSupportedPreviewSizes() : size:  " + size.getWidth() + SharedPrefsTranslator.CONNECTOR_CROSS + size.getHeight());
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedPreviewSizes() : " + arrayList);
        }
        return arrayList;
    }

    public List<int[]> getSupportedPreviewFpsRange() {
        ArrayList arrayList = new ArrayList();
        for (Range range : (Range[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)) {
            arrayList.add(new int[]{((Integer) range.getLower()).intValue(), ((Integer) range.getUpper()).intValue()});
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedPreviewFpsRange() : " + arrayList);
        }
        return arrayList;
    }

    public Rect getPreferredPreviewSizeForStill() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_STILL_PREVIEW_SIZE);
        if (iArr == null || iArr.length != 2) {
            return null;
        }
        Rect rect = new Rect(0, 0, iArr[0], iArr[1]);
        if (CamLog.VERBOSE) {
            CamLog.d("getPreferredPreviewSizeForStill() : " + rect.width() + SharedPrefsTranslator.CONNECTOR_CROSS + rect.height());
        }
        return rect;
    }

    public List<Rect> getSupportedPictureSizes() {
        ArrayList arrayList = new ArrayList();
        Size[] highResolutionOutputSizes = this.mStreamConfigurationMap.getHighResolutionOutputSizes(256);
        if (highResolutionOutputSizes != null) {
            for (Size size : highResolutionOutputSizes) {
                arrayList.add(new Rect(0, 0, size.getWidth(), size.getHeight()));
            }
        }
        Size[] outputSizes = this.mStreamConfigurationMap.getOutputSizes(256);
        if (outputSizes != null) {
            for (Size size2 : outputSizes) {
                arrayList.add(new Rect(0, 0, size2.getWidth(), size2.getHeight()));
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedPictureSizes() : " + arrayList);
        }
        return arrayList;
    }

    public List<Rect> getManualIsoSupportedPictureSizes() {
        ArrayList arrayList = new ArrayList();
        long[] jArr = (long[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_MANUAL_ISO_CONFIGURATION_MAP);
        if (jArr == null || jArr.length % 4 != 0) {
            return arrayList;
        }
        for (int i = 0; i < jArr.length; i += 4) {
            if (jArr[i] == 33) {
                arrayList.add(new Rect(0, 0, (int) jArr[i + 1], (int) jArr[i + 2]));
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getManualIsoSupportedPictureSizes() : " + arrayList);
        }
        return arrayList;
    }

    public Rect getPreferredPreviewSizeForVideo() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_VIDEO_PREVIEW_SIZE);
        if (iArr == null || iArr.length != 2) {
            return null;
        }
        Rect rect = new Rect(0, 0, iArr[0], iArr[1]);
        if (CamLog.VERBOSE) {
            CamLog.d("getPreferredPreviewSizeForVideo() : " + rect.width() + SharedPrefsTranslator.CONNECTOR_CROSS + rect.height());
        }
        return rect;
    }

    public List<VideoConfiguration> getSupportedVideoConfiguration() {
        Size[] outputSizes = this.mStreamConfigurationMap.getOutputSizes(android.media.MediaRecorder.class);
        ArrayList arrayList = new ArrayList();
        if (outputSizes == null) {
            return arrayList;
        }
        for (Size size : outputSizes) {
            int outputMinFrameDuration = (int) (1000000000 / this.mStreamConfigurationMap.getOutputMinFrameDuration(android.media.MediaRecorder.class, size));
            arrayList.add(new VideoConfiguration(size.getWidth(), size.getHeight(), 0, outputMinFrameDuration));
            if (CamLog.VERBOSE) {
                CamLog.d("getSupportedVideoConfiguration() : (width, height, maxfps) = (" + size.getWidth() + ", " + size.getHeight() + ", " + outputMinFrameDuration + ")");
            }
        }
        return arrayList;
    }

    public Rect getPreferredPreviewSizeForHdrVideo() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_HDR_VIDEO_PREVIEW_SIZE);
        if (iArr == null || iArr.length != 2) {
            CamLog.i("Preview Size for Video HDR does not supported.");
            return getPreferredPreviewSizeForVideo();
        }
        Rect rect = new Rect(0, 0, iArr[0], iArr[1]);
        if (CamLog.VERBOSE) {
            CamLog.d("getPreferredPreviewSizeForHdrVideo() : " + rect.width() + SharedPrefsTranslator.CONNECTOR_CROSS + rect.height());
        }
        return rect;
    }

    public List<String> getSupportedFusionModes() {
        ArrayList arrayList = new ArrayList();
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_FUSION_MODES);
        if (iArr == null) {
            return arrayList;
        }
        for (int i : iArr) {
            switch (i) {
                case 0:
                    arrayList.add("off");
                    break;
                case 1:
                    arrayList.add("on");
                    break;
                case 2:
                    arrayList.add("auto");
                    break;
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedFusionModes() : " + arrayList);
        }
        return arrayList;
    }

    public List<Rect> getFusionSupportedPictureSizes() {
        ArrayList arrayList = new ArrayList();
        long[] jArr = (long[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_FUSION_CONFIGURATION_MAP);
        if (jArr == null || jArr.length % 4 != 0) {
            return arrayList;
        }
        for (int i = 0; i < jArr.length; i += 4) {
            if (jArr[i] == 33) {
                arrayList.add(new Rect(0, 0, (int) jArr[i + 1], (int) jArr[i + 2]));
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getFusionSupportedPictureSizes() : " + arrayList);
        }
        return arrayList;
    }

    public List<VideoConfiguration> getFusionSupportedVideoConfiguration() {
        ArrayList arrayList = new ArrayList();
        long[] jArr = (long[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_FUSION_CONFIGURATION_MAP);
        if (jArr == null || jArr.length % 4 != 0) {
            return arrayList;
        }
        for (int i = 0; i < jArr.length; i += 4) {
            if (jArr[i] == 34) {
                int i2 = i + 1;
                int i3 = i + 2;
                int i4 = i + 3;
                arrayList.add(new VideoConfiguration((int) jArr[i2], (int) jArr[i3], 0, (int) (1000000000 / jArr[i4])));
                if (CamLog.VERBOSE) {
                    CamLog.d("getFusionSupportedVideoConfiguration() : (width, height, maxfps) = (" + ((int) jArr[i2]) + ", " + ((int) jArr[i3]) + ", " + ((int) (1000000000 / jArr[i4])) + ")");
                }
            }
        }
        return arrayList;
    }

    public Range<Integer> getSupportedFusionIsoRange() {
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SENSOR_INFO_FUSION_SENSITIVITY_RANGE);
        if (iArr == null) {
            return (Range) this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedFusionIsoRange() : " + flatten(iArr));
        }
        return new Range<>(Integer.valueOf(iArr[0]), Integer.valueOf(iArr[1]));
    }

    public Rect getActiveArraySize() {
        if (this.mCharacteristics != null) {
            Rect rect = (Rect) this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            rect.offsetTo(0, 0);
            return rect;
        }
        return new Rect();
    }

    public int getLensFacing() {
        int iIntValue = ((Integer) this.mCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue();
        if (CamLog.VERBOSE) {
            CamLog.d("getLensFacing() : " + iIntValue);
        }
        return iIntValue;
    }

    public String getSensorName() {
        String str = (String) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_INFO_SENSOR_NAME);
        if (str == null) {
            str = "";
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSensorName() : " + str);
        }
        return str;
    }

    public List<String> getSupportedDistortionCorrection() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("off");
        int[] iArr = (int[]) tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_DISTORTION_CORRECTION_MODES);
        if (iArr != null) {
            for (int i : iArr) {
                if (i == 1) {
                    arrayList.add("on");
                }
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedDistortionCorrection() : " + arrayList);
        }
        return arrayList;
    }
}
