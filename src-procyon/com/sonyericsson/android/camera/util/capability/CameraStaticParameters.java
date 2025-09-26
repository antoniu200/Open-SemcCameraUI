// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import com.sonyericsson.android.camera.device.CameraParameterConverter;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.util.Size;
import android.util.Range;
import com.sonyericsson.android.camera.device.SomcCameraCharacteristicsKeys;
import java.util.ArrayList;
import java.util.List;
import android.util.Rational;
import android.graphics.Rect;
import com.sonyericsson.android.camera.util.CamLog;
import android.hardware.camera2.CameraCharacteristics$Key;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.CameraCharacteristics;

final class CameraStaticParameters
{
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
    public static final Integer[] SHUTTER_SPEED_INVERSE_VALUES;
    private static final String TAG = "CameraStaticParameters";
    private CameraCharacteristics mCharacteristics;
    private StreamConfigurationMap mStreamConfigurationMap;
    
    static {
        SHUTTER_SPEED_INVERSE_VALUES = new Integer[] { 1, 2, 4, 8, 15, 30, 60, 125, 250, 500, 1000, 2000, 4000 };
    }
    
    public CameraStaticParameters(final CameraCharacteristics mCharacteristics) {
        this.mCharacteristics = mCharacteristics;
        this.mStreamConfigurationMap = (StreamConfigurationMap)this.mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
    }
    
    private static String flatten(final int[] array) {
        if (array == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }
    
    private <T> T tryGetCharacteristics(final CameraCharacteristics$Key<T> cameraCharacteristics$Key) {
        try {
            return (T)this.mCharacteristics.get((CameraCharacteristics$Key)cameraCharacteristics$Key);
        }
        catch (final IllegalArgumentException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("tryGetCharacteristics: Unknown key: ");
            sb.append(cameraCharacteristics$Key.getName());
            CamLog.e(sb.toString());
            return null;
        }
    }
    
    public Rect getActiveArraySize() {
        if (this.mCharacteristics != null) {
            final Rect rect = (Rect)this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            rect.offsetTo(0, 0);
            return rect;
        }
        return new Rect();
    }
    
    public float getExposureCompensationStep() {
        return ((Rational)this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP)).floatValue();
    }
    
    public List<Rect> getFusionSupportedPictureSizes() {
        final ArrayList obj = new ArrayList();
        final long[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_FUSION_CONFIGURATION_MAP);
        if (array != null && array.length % 4 == 0) {
            for (int i = 0; i < array.length; i += 4) {
                if (array[i] == 33L) {
                    obj.add(new Rect(0, 0, (int)array[i + 1], (int)array[i + 2]));
                }
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getFusionSupportedPictureSizes() : ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            return obj;
        }
        return obj;
    }
    
    public List<VideoConfiguration> getFusionSupportedVideoConfiguration() {
        final ArrayList list = new ArrayList();
        final long[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_FUSION_CONFIGURATION_MAP);
        if (array != null && array.length % 4 == 0) {
            for (int i = 0; i < array.length; i += 4) {
                if (array[i] == 34L) {
                    final int n = i + 1;
                    final int n2 = (int)array[n];
                    final int n3 = i + 2;
                    final int n4 = (int)array[n3];
                    final int n5 = i + 3;
                    list.add(new VideoConfiguration(n2, n4, 0, (int)(1000000000L / array[n5])));
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("getFusionSupportedVideoConfiguration() : (width, height, maxfps) = (");
                        sb.append((int)array[n]);
                        sb.append(", ");
                        sb.append((int)array[n3]);
                        sb.append(", ");
                        sb.append((int)(1000000000L / array[n5]));
                        sb.append(")");
                        CamLog.d(sb.toString());
                    }
                }
            }
            return list;
        }
        return list;
    }
    
    public int getLensFacing() {
        final int intValue = (int)this.mCharacteristics.get(CameraCharacteristics.LENS_FACING);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getLensFacing() : ");
            sb.append(intValue);
            CamLog.d(sb.toString());
        }
        return intValue;
    }
    
    public float getMacroValueForManualFocus() {
        final Float obj = (Float)this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getMacroValueForManualFocus() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<Rect> getManualIsoSupportedPictureSizes() {
        final ArrayList obj = new ArrayList();
        final long[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_MANUAL_ISO_CONFIGURATION_MAP);
        if (array != null && array.length % 4 == 0) {
            for (int i = 0; i < array.length; i += 4) {
                if (array[i] == 33L) {
                    obj.add(new Rect(0, 0, (int)array[i + 1], (int)array[i + 2]));
                }
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getManualIsoSupportedPictureSizes() : ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            return obj;
        }
        return obj;
    }
    
    public int getMaxAwbColorCompensationAb() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB_RANGE);
        if (array == null) {
            return 0;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getMaxAwbColorCompensationAb() : ");
            sb.append(array[1]);
            CamLog.d(sb.toString());
        }
        return array[1];
    }
    
    public int getMaxExposureCompensation() {
        return (int)((Range)this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE)).getUpper();
    }
    
    public int getMaxNumDetectedFaces() {
        return (int)this.mCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT);
    }
    
    public int getMaxNumFocusAreas() {
        final Integer obj = (Integer)this.mCharacteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getMaxNumFocusAreas() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (obj == null) {
            return 0;
        }
        return obj;
    }
    
    public long getMaxShutterSpeed() {
        return (long)((Range)this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE)).getUpper();
    }
    
    public int getMaxSoftSkinLevel() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_SKIN_SMOOTH_LEVEL_RANGE);
        if (array != null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getMaxSoftSkinLevel() : ");
                sb.append(array[1]);
                CamLog.d(sb.toString());
            }
            return array[1];
        }
        return 0;
    }
    
    public float getMaxZoomRatio() {
        return (float)this.mCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
    }
    
    public int getMinAwbColorCompensationAb() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB_RANGE);
        if (array == null) {
            return 0;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getMinAwbColorCompensationAb() : ");
            sb.append(array[0]);
            CamLog.d(sb.toString());
        }
        return array[0];
    }
    
    public int getMinExposureCompensation() {
        return (int)((Range)this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE)).getLower();
    }
    
    public long getMinExposureTimeLimit() {
        final Long obj = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_MIN_EXPOSURE_TIME_LIMIT);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getMinExposureTimeLimit() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (obj == null) {
            return 0L;
        }
        return obj;
    }
    
    public long getMinShutterSpeed() {
        return (long)((Range)this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE)).getLower();
    }
    
    public int getMinSoftSkinLevel() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_SKIN_SMOOTH_LEVEL_RANGE);
        if (array != null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getMinSoftSkinLevel() : ");
                sb.append(array[0]);
                CamLog.d(sb.toString());
            }
            return array[0];
        }
        return 0;
    }
    
    public Rect getPreferredPreviewSizeForHdrVideo() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_HDR_VIDEO_PREVIEW_SIZE);
        if (array != null && array.length == 2) {
            final Rect rect = new Rect(0, 0, array[0], array[1]);
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getPreferredPreviewSizeForHdrVideo() : ");
                sb.append(rect.width());
                sb.append("x");
                sb.append(rect.height());
                CamLog.d(sb.toString());
            }
            return rect;
        }
        CamLog.i("Preview Size for Video HDR does not supported.");
        return this.getPreferredPreviewSizeForVideo();
    }
    
    public Rect getPreferredPreviewSizeForStill() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_STILL_PREVIEW_SIZE);
        if (array != null && array.length == 2) {
            final Rect rect = new Rect(0, 0, array[0], array[1]);
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getPreferredPreviewSizeForStill() : ");
                sb.append(rect.width());
                sb.append("x");
                sb.append(rect.height());
                CamLog.d(sb.toString());
            }
            return rect;
        }
        return null;
    }
    
    public Rect getPreferredPreviewSizeForVideo() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_PREFERRED_VIDEO_PREVIEW_SIZE);
        if (array != null && array.length == 2) {
            final Rect rect = new Rect(0, 0, array[0], array[1]);
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getPreferredPreviewSizeForVideo() : ");
                sb.append(rect.width());
                sb.append("x");
                sb.append(rect.height());
                CamLog.d(sb.toString());
            }
            return rect;
        }
        return null;
    }
    
    public String getSensorName() {
        String str;
        if ((str = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_INFO_SENSOR_NAME)) == null) {
            str = "";
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSensorName() : ");
            sb.append(str);
            CamLog.d(sb.toString());
        }
        return str;
    }
    
    public List<Rect> getStillHdrSupportedPictureSizes() {
        final ArrayList obj = new ArrayList();
        final long[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_AVAILABLE_STILL_HDR_CONFIGURATION_MAP);
        if (array != null && array.length % 4 == 0) {
            for (int i = 0; i < array.length; i += 4) {
                if (array[i] == 33L) {
                    obj.add(new Rect(0, 0, (int)array[i + 1], (int)array[i + 2]));
                }
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getStillHdrSupportedPictureSizes() : ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            return obj;
        }
        return obj;
    }
    
    public List<String> getSupportedAeModes() {
        final ArrayList obj = new ArrayList();
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_MODES);
        if (array == null) {
            return obj;
        }
        final int length = array.length;
        final int n = 0;
        final boolean b;
        int n2 = (b = false) ? 1 : 0;
        int n4;
        int n3 = n4 = (b ? 1 : 0);
        int n5 = b ? 1 : 0;
        for (int i = n; i < length; ++i) {
            switch (array[i]) {
                case 13:
                case 14: {
                    n4 = 1;
                    break;
                }
                case 9:
                case 10:
                case 11:
                case 12: {
                    n3 = 1;
                    break;
                }
                case 5:
                case 6:
                case 7:
                case 8: {
                    n5 = 1;
                    break;
                }
                case 1:
                case 2:
                case 3:
                case 4: {
                    n2 = 1;
                    break;
                }
            }
        }
        if (n2 != 0) {
            obj.add("auto");
        }
        if (n5 != 0) {
            obj.add("iso-prio");
        }
        if (n3 != 0) {
            obj.add("shutter-prio");
        }
        if (n4 != 0) {
            obj.add("semi-auto");
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedAeModes() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedAeRegionModes() {
        final ArrayList obj = new ArrayList();
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_REGION_MODES);
        if (array == null) {
            return obj;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            switch (array[i]) {
                case 5: {
                    obj.add("user");
                    break;
                }
                case 4: {
                    obj.add("face");
                    break;
                }
                case 3: {
                    obj.add("multi");
                    break;
                }
                case 2: {
                    obj.add("spot");
                    break;
                }
                case 1: {
                    obj.add("frame-average");
                    break;
                }
                case 0: {
                    obj.add("center-weighted");
                    break;
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedAeRegionModes() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedDistortionCorrection() {
        final ArrayList obj = new ArrayList();
        obj.add("off");
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_DISTORTION_CORRECTION_MODES);
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                if (array[i] == 1) {
                    obj.add("on");
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedDistortionCorrection() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedFlashModes() {
        final ArrayList obj = new ArrayList();
        obj.add("off");
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AE_AVAILABLE_MODES);
        if (array != null) {
            for (final int n : array) {
                Label_0174: {
                    switch (n) {
                        default: {
                            switch (n) {
                                default: {
                                    break Label_0174;
                                }
                                case 16: {
                                    obj.add("display-on");
                                    break Label_0174;
                                }
                                case 15: {
                                    obj.add("display-auto");
                                    break Label_0174;
                                }
                            }
                            break;
                        }
                        case 4: {
                            obj.add("red-eye");
                            break;
                        }
                        case 3: {
                            obj.add("on");
                            break;
                        }
                        case 2: {
                            obj.add("auto");
                            break;
                        }
                    }
                }
            }
        }
        if (this.mCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
            obj.add("torch");
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedFlashModes() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedFocusAreaModes() {
        final ArrayList obj = new ArrayList();
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AF_AVAILABLE_REGION_MODES);
        if (array == null) {
            return obj;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            switch (array[i]) {
                case 3: {
                    obj.add("user");
                    break;
                }
                case 2: {
                    obj.add("face");
                    break;
                }
                case 1: {
                    obj.add("multi");
                    break;
                }
                case 0: {
                    obj.add("center");
                    break;
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedFocusAreaModes() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedFocusModes() {
        final ArrayList obj = new ArrayList();
        final int[] array = (int[])this.mCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        for (int length = array.length, i = 0; i < length; ++i) {
            switch (array[i]) {
                case 4: {
                    obj.add("continuous-picture");
                    break;
                }
                case 3: {
                    obj.add("continuous-video");
                    break;
                }
                case 1: {
                    obj.add("auto");
                    break;
                }
                case 0: {
                    if ((float)this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE) == 0.0f) {
                        obj.add("fixed");
                        break;
                    }
                    obj.add("manual");
                    obj.add("infinity");
                    break;
                }
            }
        }
        if (obj.size() == 0) {
            obj.add("fixed");
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedFocusModes() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public Range<Integer> getSupportedFusionIsoRange() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SENSOR_INFO_FUSION_SENSITIVITY_RANGE);
        if (array == null) {
            return (Range<Integer>)this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedFusionIsoRange() : ");
            sb.append(flatten(array));
            CamLog.d(sb.toString());
        }
        return (Range<Integer>)new Range((Comparable)array[0], (Comparable)array[1]);
    }
    
    public List<String> getSupportedFusionModes() {
        final ArrayList obj = new ArrayList();
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_FUSION_MODES);
        if (array == null) {
            return obj;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            switch (array[i]) {
                case 2: {
                    obj.add("auto");
                    break;
                }
                case 1: {
                    obj.add("on");
                    break;
                }
                case 0: {
                    obj.add("off");
                    break;
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedFusionModes() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public Range<Integer> getSupportedIsoRange() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SENSOR_INFO_SENSITIVITY_RANGE);
        if (array == null) {
            return (Range<Integer>)this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedIsoRange() : ");
            sb.append(flatten(array));
            CamLog.d(sb.toString());
        }
        return (Range<Integer>)new Range((Comparable)array[0], (Comparable)array[1]);
    }
    
    public List<Rect> getSupportedPictureSizes() {
        final ArrayList obj = new ArrayList();
        final Size[] highResolutionOutputSizes = this.mStreamConfigurationMap.getHighResolutionOutputSizes(256);
        if (highResolutionOutputSizes != null) {
            for (final Size size : highResolutionOutputSizes) {
                obj.add(new Rect(0, 0, size.getWidth(), size.getHeight()));
            }
        }
        final Size[] outputSizes = this.mStreamConfigurationMap.getOutputSizes(256);
        if (outputSizes != null) {
            for (final Size size2 : outputSizes) {
                obj.add(new Rect(0, 0, size2.getWidth(), size2.getHeight()));
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedPictureSizes() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedPowerSaveModes() {
        final ArrayList obj = new ArrayList();
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_POWER_SAVE_MODES);
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                switch (array[i]) {
                    case 2: {
                        obj.add("ultra-low");
                        break;
                    }
                    case 1: {
                        obj.add("low");
                        break;
                    }
                    case 0: {
                        obj.add("off");
                        break;
                    }
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedPowerSaveModes() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<int[]> getSupportedPreviewFpsRange() {
        final ArrayList obj = new ArrayList();
        for (final Range range : (Range[])this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)) {
            obj.add(new int[] { (int)range.getLower(), (int)range.getUpper() });
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedPreviewFpsRange() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<Rect> getSupportedPreviewSizes() {
        final Size[] outputSizes = this.mStreamConfigurationMap.getOutputSizes((Class)SurfaceHolder.class);
        final ArrayList obj = new ArrayList();
        if (outputSizes == null) {
            return obj;
        }
        for (final Size size : outputSizes) {
            obj.add(new Rect(0, 0, size.getWidth(), size.getHeight()));
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getSupportedPreviewSizes() : size:  ");
                sb.append(size.getWidth());
                sb.append("x");
                sb.append(size.getHeight());
                CamLog.d(sb.toString());
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("getSupportedPreviewSizes() : ");
            sb2.append(obj);
            CamLog.d(sb2.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedShutterSpeedValues() {
        final ArrayList obj = new ArrayList();
        final Range range = (Range)this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
        final Long n = (Long)range.getLower();
        final Long n2 = (Long)range.getUpper();
        for (int i = 0; i < CameraStaticParameters.SHUTTER_SPEED_INVERSE_VALUES.length; ++i) {
            final Long value = (Long)(1000000000 / CameraStaticParameters.SHUTTER_SPEED_INVERSE_VALUES[i]);
            if (n <= value && value <= n2) {
                obj.add(value.toString());
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedShutterSpeedValues() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedStillHdrValues() {
        final ArrayList obj = new ArrayList();
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_CONTROL_AVAILABLE_STILL_HDR_MODES);
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                switch (array[i]) {
                    case 1: {
                        obj.add("on-still-hdr");
                        break;
                    }
                    case 0: {
                        obj.add("off");
                        break;
                    }
                }
            }
        }
        if (this.isConditionDetectionSupported()) {
            obj.add("auto");
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedStillHdrValues() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<VideoConfiguration> getSupportedVideoConfiguration() {
        final Size[] outputSizes = this.mStreamConfigurationMap.getOutputSizes((Class)MediaRecorder.class);
        final ArrayList list = new ArrayList();
        if (outputSizes == null) {
            return list;
        }
        for (final Size size : outputSizes) {
            final long outputMinFrameDuration = this.mStreamConfigurationMap.getOutputMinFrameDuration((Class)MediaRecorder.class, size);
            final int width = size.getWidth();
            final int height = size.getHeight();
            final int j = (int)(1000000000L / outputMinFrameDuration);
            list.add(new VideoConfiguration(width, height, 0, j));
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getSupportedVideoConfiguration() : (width, height, maxfps) = (");
                sb.append(size.getWidth());
                sb.append(", ");
                sb.append(size.getHeight());
                sb.append(", ");
                sb.append(j);
                sb.append(")");
                CamLog.d(sb.toString());
            }
        }
        return list;
    }
    
    public List<String> getSupportedWhiteBalance() {
        final ArrayList list = new ArrayList();
        final int[] array = (int[])this.mCharacteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(CameraParameterConverter.AwbMode.getApi1Value(array[i]));
        }
        return list;
    }
    
    public float getWideZoomTargetRatio() {
        final Float obj = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_SCALER_WIDE_ZOOM_TARGET_RATIO);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getWideZoomTargetRatio() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (obj == null) {
            return 1.0f;
        }
        return obj;
    }
    
    public boolean isConditionDetectionSupported() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_CONDITION_DETECT_MODE);
        final boolean b = false;
        if (array == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("isConditionDetectionSupported() : ");
            sb.append(array.length == 2);
            CamLog.d(sb.toString());
        }
        boolean b2 = b;
        if (array.length == 2) {
            b2 = true;
        }
        return b2;
    }
    
    public boolean isFaceDetectionAvailable() {
        final int[] array = (int[])this.mCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
        return array != null && array.length > 1 && this.getMaxNumDetectedFaces() > 0;
    }
    
    public boolean isManualFocusSupported() {
        final int[] array = (int[])this.mCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == 0 && (float)this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE) > 0.0f) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isObjectTrackingSupported() {
        final Boolean b = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_OBJECT_TRACKING);
        if (b == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("isObjectTrackingSupported() : ");
            sb.append((boolean)b);
            CamLog.d(sb.toString());
        }
        return b;
    }
    
    public boolean isSceneDetectionSupported() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_SCENE_DETECT_MODE);
        final boolean b = false;
        if (array == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("isSceneDetectionSupported() : ");
            sb.append(array.length == 2);
            CamLog.d(sb.toString());
        }
        boolean b2 = b;
        if (array.length == 2) {
            b2 = true;
        }
        return b2;
    }
    
    public boolean isSmileDetectionAvailable() {
        final int[] array = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_FACE_SMILE_SCORES_MODE);
        final boolean b = false;
        if (array == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("isSmileDetectionAvailable() : ");
            sb.append(array.length == 2);
            CamLog.d(sb.toString());
        }
        boolean b2 = b;
        if (array.length == 2) {
            b2 = true;
        }
        return b2;
    }
    
    public boolean isTrackingFocusDuringLockSupported() {
        final Boolean b = this.tryGetCharacteristics(SomcCameraCharacteristicsKeys.SONYMOBILE_STATISTICS_INFO_AVAILABLE_TRACKING_FOCUS_DURING_LOCK);
        if (b == null) {
            return false;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("isTrackingFocusDuringLockSupported() : ");
            sb.append((boolean)b);
            CamLog.d(sb.toString());
        }
        return b;
    }
}
