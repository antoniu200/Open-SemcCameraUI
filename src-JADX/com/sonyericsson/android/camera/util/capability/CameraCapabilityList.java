package com.sonyericsson.android.camera.util.capability;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.util.Range;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCameraParameters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraCapabilityList {
    public static final String TAG = "CameraCapabilityList";
    public final CapabilityItem<Rect> ACTIVE_ARRAY_SIZE;
    public final CapabilityItem<List<String>> AE;
    public final CapabilityItem<List<String>> BURST;
    public final CapabilityItem<List<String>> DISTORTION_CORRECTION;
    public final CapabilityItem<Integer> EV_MAX;
    public final CapabilityItem<Integer> EV_MIN;
    public final CapabilityItem<Float> EV_STEP;
    public final CapabilityItem<List<String>> FLASH;
    public final CapabilityItem<List<String>> FOCUS_AREA;
    public final CapabilityItem<List<String>> FOCUS_MODE;
    public final CapabilityItem<List<int[]>> FPS_RANGE;
    public final CapabilityItem<Range<Integer>> FUSION_ISO_RANGE;
    public final CapabilityItem<List<String>> FUSION_MODE;
    public final CapabilityItem<List<Rect>> FUSION_SUPPORTED_PICTURE_SIZES;
    public final CapabilityItem<List<VideoConfiguration>> FUSION_SUPPORTED_VIDEO_CONFIGURATION;
    public final CapabilityItem<List<String>> HDR;
    public final CapabilityItem<Boolean> HDR_VIDEO_SUPPORTED;
    public final CapabilityItem<List<VideoConfiguration>> INTELLIGENT_ACTIVE_CONFIGURATION;
    public final CapabilityItem<Range<Integer>> ISO_RANGE;
    public final CapabilityItem<Integer> LENS_FACING;
    public final CapabilityItem<Float> MACRO_FOCUS_RANGE;
    public final CapabilityItem<Boolean> MANUAL_FOCUS;
    public final CapabilityItem<List<Rect>> MANUAL_ISO_SUPPORTED_PICTURE_SIZE;
    public final CapabilityItem<Integer> MAX_AWB_AB;
    public final CapabilityItem<Integer> MAX_NUM_FACE;
    public final CapabilityItem<Integer> MAX_NUM_FOCUS_AREA;
    public final CapabilityItem<Long> MAX_SHUTTER_SPEED;
    public final CapabilityItem<Integer> MAX_SOFT_SKIN_LEVEL;
    public final CapabilityItem<Float> MAX_ZOOM_RATIO;
    public final CapabilityItem<List<String>> METERING;
    public final CapabilityItem<Integer> MIN_AWB_AB;
    public final CapabilityItem<Long> MIN_SHUTTER_SPEED;
    public final CapabilityItem<Long> MIN_SHUTTER_SPEED_LIMIT;
    public final CapabilityItem<Integer> MIN_SOFT_SKIN_LEVEL;
    public final CapabilityItem<Boolean> OBJECT_TRACKING;
    public final CapabilityItem<List<Rect>> PICTURE_SIZE;
    public final CapabilityItem<List<String>> POWER_SAVING_MODE;
    public final CapabilityItem<List<String>> PREDICTIVE_CAPTURE;
    public final CapabilityItem<List<Rect>> PREVIEW_SIZE;
    public final CapabilityItem<Rect> PREVIEW_SIZE_FOR_HDR_VIDEO;
    public final CapabilityItem<Rect> PREVIEW_SIZE_FOR_STILL;
    public final CapabilityItem<Rect> PREVIEW_SIZE_FOR_VIDEO;
    public final CapabilityItem<ResolutionOptions> RESOLUTION_CAPABILITY;
    public final CapabilityItem<Boolean> SCENE_RECOGNITION;
    public final CapabilityItem<String> SENSOR_NAME;
    public final CapabilityItem<List<String>> SHUTTER_SPEED_VALUES;
    public final CapabilityItem<Boolean> SMILE_DETECTION;
    public final CapabilityItem<List<VideoConfiguration>> STEADY_SHOT_CONFIGURATION;
    public final CapabilityItem<List<Rect>> STILL_HDR_SUPPORTED_PICTURE_SIZE;
    public final CapabilityItem<List<VideoConfiguration>> SUPER_SLOW_CONFIGURATION;
    public final CapabilityItem<List<String>> SUPER_SLOW_VALUES;
    public final CapabilityItem<Boolean> TRACKING_FOCUS_DURING_LOCK;
    public final CapabilityItem<List<VideoConfiguration>> VIDEO_CONFIGURATION;
    public final CapabilityItem<List<String>> VIDEO_STABILIZER;
    public final CapabilityItem<List<String>> WHITE_BALANCE;
    public final CapabilityItem<Float> WIDE_ZOOM_TARGET_RATIO;
    private final List<CapabilityItem<?>> mValues;

    public CameraCapabilityList(Context context, SharedPreferences sharedPreferences) {
        this.EV_MAX = new IntegerCapabilityItem(CameraParameters.KEY_MAX_EXPOSURE_COMPENSATION, sharedPreferences);
        this.EV_MIN = new IntegerCapabilityItem(CameraParameters.KEY_MIN_EXPOSURE_COMPENSATION, sharedPreferences);
        this.EV_STEP = new FloatCapabilityItem(CameraParameters.KEY_EXPOSURE_COMPENSATION_STEP, sharedPreferences);
        this.FLASH = new StringListCapabilityItem(CameraParameters.KEY_FLASH_MODE, sharedPreferences);
        this.FOCUS_MODE = new StringListCapabilityItem(CameraParameters.KEY_FOCUS_MODE, sharedPreferences);
        this.FPS_RANGE = new IntArrayListCapabilityItem(CameraParameters.KEY_PREVIEW_FPS_RANGE, sharedPreferences);
        this.MAX_NUM_FACE = new IntegerCapabilityItem(CameraParameters.KEY_MAX_NUM_DETECTED_FACES, sharedPreferences);
        this.MAX_NUM_FOCUS_AREA = new IntegerCapabilityItem(CameraParameters.KEY_MAX_NUM_FOCUS_AREAS, sharedPreferences);
        this.MAX_ZOOM_RATIO = new FloatCapabilityItem(CameraParameters.KEY_MAX_ZOOM_RATIO, sharedPreferences);
        this.WIDE_ZOOM_TARGET_RATIO = new FloatCapabilityItem(CameraParameters.KEY_WIDE_ZOOM_TARGET_RATIO, sharedPreferences);
        this.PICTURE_SIZE = new RectListCapabilityItem(CameraParameters.KEY_PICTURE_SIZE, sharedPreferences);
        this.MANUAL_ISO_SUPPORTED_PICTURE_SIZE = new RectListCapabilityItem(CameraParameters.KEY_EX_MANUAL_ISO_SUPPORTED_SIZES, sharedPreferences);
        this.STILL_HDR_SUPPORTED_PICTURE_SIZE = new RectListCapabilityItem(CameraParameters.KEY_EX_STILL_HDR_SUPPORTED_SIZES, sharedPreferences);
        this.PREVIEW_SIZE = new RectListCapabilityItem(CameraParameters.KEY_PREVIEW_SIZE, sharedPreferences);
        this.PREVIEW_SIZE_FOR_STILL = new RectCapabilityItem(CameraParameters.KEY_PREFERRED_PREVIEW_SIZE_FOR_STILL, sharedPreferences);
        this.PREVIEW_SIZE_FOR_VIDEO = new RectCapabilityItem(CameraParameters.KEY_PREFERRED_PREVIEW_SIZE_FOR_VIDEO, sharedPreferences);
        this.PREVIEW_SIZE_FOR_HDR_VIDEO = new RectCapabilityItem(CameraParameters.KEY_PREFERRED_PREVIEW_SIZE_FOR_HDR_VIDEO, sharedPreferences);
        this.VIDEO_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_VIDEO_SIZE, sharedPreferences);
        this.WHITE_BALANCE = new StringListCapabilityItem(CameraParameters.KEY_WHITE_BALANCE, sharedPreferences);
        this.AE = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_AE_MODES, sharedPreferences);
        this.PREDICTIVE_CAPTURE = new StringListCapabilityItem(BypassCameraParameters.KEY_CLIMAX_RECOGNITION_VALUES, sharedPreferences);
        this.BURST = new StringListCapabilityItem(BypassCameraParameters.KEY_BURST_VALUES, sharedPreferences);
        this.FOCUS_AREA = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_FOCUS_AREAS, sharedPreferences);
        this.HDR = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_IMAGE_STABILIZERS, sharedPreferences);
        this.ISO_RANGE = new IntegerRangeCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_ISO_RANGE, sharedPreferences);
        this.METERING = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_METERING_MODES, sharedPreferences);
        this.OBJECT_TRACKING = new BooleanCapabilityItem(CameraParameters.KEY_EX_OBJECT_TRACKING_SUPPORTED, sharedPreferences);
        this.TRACKING_FOCUS_DURING_LOCK = new BooleanCapabilityItem(CameraParameters.KEY_EX_TRACKING_FOCUS_DURING_LOCK_SUPPORTED, sharedPreferences);
        this.SCENE_RECOGNITION = new BooleanCapabilityItem(CameraParameters.KEY_EX_SCENE_DETECTION_SUPPORTED, sharedPreferences);
        this.SMILE_DETECTION = new BooleanCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_SMILE_DETECTIONS, sharedPreferences);
        this.VIDEO_STABILIZER = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_VIDEO_STABILIZERS, sharedPreferences);
        this.STEADY_SHOT_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_EX_STEADY_SHOT_CONFIGURATION, sharedPreferences);
        this.INTELLIGENT_ACTIVE_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_EX_INTELLIGENT_ACTIVE_CONFIGURATION, sharedPreferences);
        this.MAX_SOFT_SKIN_LEVEL = new IntegerCapabilityItem(CameraParameters.KEY_EX_MAX_SOFT_SKIN_LEVEL, sharedPreferences);
        this.MIN_SOFT_SKIN_LEVEL = new IntegerCapabilityItem(CameraParameters.KEY_EX_MIN_SOFT_SKIN_LEVEL, sharedPreferences);
        this.MAX_AWB_AB = new IntegerCapabilityItem(CameraParameters.KEY_EX_MAX_AWB_COMPENSATION_AB, sharedPreferences);
        this.MIN_AWB_AB = new IntegerCapabilityItem(CameraParameters.KEY_EX_MIN_AWB_COMPENSATION_AB, sharedPreferences);
        this.MACRO_FOCUS_RANGE = new FloatCapabilityItem(CameraParameters.KEY_EX_MANUAL_FOCUS_FOR_MACRO, sharedPreferences);
        this.MANUAL_FOCUS = new BooleanCapabilityItem(CameraParameters.KEY_MANUAL_FOCUS_SUPPORTED, sharedPreferences);
        this.MAX_SHUTTER_SPEED = new LongCapabilityItem(CameraParameters.KEY_EX_MAX_SHUTTER_SPEED, sharedPreferences);
        this.MIN_SHUTTER_SPEED = new LongCapabilityItem(CameraParameters.KEY_EX_MIN_SHUTTER_SPEED, sharedPreferences);
        this.SHUTTER_SPEED_VALUES = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_SHUTTER_SPEED, sharedPreferences);
        this.POWER_SAVING_MODE = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_POWER_SAVE_MODE, sharedPreferences);
        this.MIN_SHUTTER_SPEED_LIMIT = new LongCapabilityItem(CameraParameters.KEY_EX_MIN_SHUTTER_SPEED_LIMIT, sharedPreferences);
        this.SUPER_SLOW_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_EX_SUPER_SLOW_CONFIGURATION, sharedPreferences);
        this.SUPER_SLOW_VALUES = new StringListCapabilityItem(BypassCameraParameters.KEY_SUPER_SLOW_VALUES, sharedPreferences);
        this.FUSION_MODE = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_FUSION_MODES, sharedPreferences);
        this.FUSION_SUPPORTED_PICTURE_SIZES = new RectListCapabilityItem(CameraParameters.KEY_EX_FUSION_SUPPORTED_PICTURE_SIZES, sharedPreferences);
        this.FUSION_SUPPORTED_VIDEO_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_EX_FUSION_SUPPORTED_VIDEO_CONFIGURATION, sharedPreferences);
        this.FUSION_ISO_RANGE = new IntegerRangeCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_FUSION_ISO_RANGE, sharedPreferences);
        this.ACTIVE_ARRAY_SIZE = new RectCapabilityItem(CameraParameters.KEY_ACTIVE_ARRAY_SIZE, sharedPreferences);
        this.LENS_FACING = new IntegerCapabilityItem(CameraParameters.KEY_LENS_FACING, sharedPreferences);
        this.SENSOR_NAME = new StringCapabilityItem(CameraParameters.KEY_SENSOR_NAME, sharedPreferences);
        this.HDR_VIDEO_SUPPORTED = new BooleanCapabilityItem(CameraParameters.KEY_HDR_VIDEO_SUPPORTED, sharedPreferences);
        this.DISTORTION_CORRECTION = new StringListCapabilityItem(CameraParameters.KEY_DISTORTION_CORRECTION, sharedPreferences);
        this.mValues = createList();
        this.RESOLUTION_CAPABILITY = new ResolutionCapabilityItem(getResolutionOptions(context));
    }

    public CameraCapabilityList(Context context, CameraStaticParameters cameraStaticParameters, BypassCameraStaticParameters bypassCameraStaticParameters) {
        boolean z;
        this.EV_MAX = new IntegerCapabilityItem(CameraParameters.KEY_MAX_EXPOSURE_COMPENSATION, Integer.valueOf(cameraStaticParameters.getMaxExposureCompensation()));
        this.EV_MIN = new IntegerCapabilityItem(CameraParameters.KEY_MIN_EXPOSURE_COMPENSATION, Integer.valueOf(cameraStaticParameters.getMinExposureCompensation()));
        this.EV_STEP = new FloatCapabilityItem(CameraParameters.KEY_EXPOSURE_COMPENSATION_STEP, Float.valueOf(cameraStaticParameters.getExposureCompensationStep()));
        this.FLASH = new StringListCapabilityItem(CameraParameters.KEY_FLASH_MODE, cameraStaticParameters.getSupportedFlashModes());
        this.FOCUS_MODE = new StringListCapabilityItem(CameraParameters.KEY_FOCUS_MODE, cameraStaticParameters.getSupportedFocusModes());
        this.FPS_RANGE = new IntArrayListCapabilityItem(CameraParameters.KEY_PREVIEW_FPS_RANGE, cameraStaticParameters.getSupportedPreviewFpsRange());
        this.MAX_NUM_FACE = new IntegerCapabilityItem(CameraParameters.KEY_MAX_NUM_DETECTED_FACES, Integer.valueOf(cameraStaticParameters.getMaxNumDetectedFaces()));
        this.MAX_NUM_FOCUS_AREA = new IntegerCapabilityItem(CameraParameters.KEY_MAX_NUM_FOCUS_AREAS, Integer.valueOf(cameraStaticParameters.getMaxNumFocusAreas()));
        this.MAX_ZOOM_RATIO = new FloatCapabilityItem(CameraParameters.KEY_MAX_ZOOM_RATIO, Float.valueOf(cameraStaticParameters.getMaxZoomRatio()));
        this.WIDE_ZOOM_TARGET_RATIO = new FloatCapabilityItem(CameraParameters.KEY_WIDE_ZOOM_TARGET_RATIO, Float.valueOf(cameraStaticParameters.getWideZoomTargetRatio()));
        this.PICTURE_SIZE = new RectListCapabilityItem(CameraParameters.KEY_PICTURE_SIZE, cameraStaticParameters.getSupportedPictureSizes());
        this.MANUAL_ISO_SUPPORTED_PICTURE_SIZE = new RectListCapabilityItem(CameraParameters.KEY_EX_MANUAL_ISO_SUPPORTED_SIZES, cameraStaticParameters.getManualIsoSupportedPictureSizes());
        this.STILL_HDR_SUPPORTED_PICTURE_SIZE = new RectListCapabilityItem(CameraParameters.KEY_EX_STILL_HDR_SUPPORTED_SIZES, cameraStaticParameters.getStillHdrSupportedPictureSizes());
        this.PREVIEW_SIZE = new RectListCapabilityItem(CameraParameters.KEY_PREVIEW_SIZE, cameraStaticParameters.getSupportedPreviewSizes());
        this.PREVIEW_SIZE_FOR_STILL = new RectCapabilityItem(CameraParameters.KEY_PREFERRED_PREVIEW_SIZE_FOR_STILL, cameraStaticParameters.getPreferredPreviewSizeForStill());
        this.PREVIEW_SIZE_FOR_VIDEO = new RectCapabilityItem(CameraParameters.KEY_PREFERRED_PREVIEW_SIZE_FOR_VIDEO, cameraStaticParameters.getPreferredPreviewSizeForVideo());
        this.PREVIEW_SIZE_FOR_HDR_VIDEO = new RectCapabilityItem(CameraParameters.KEY_PREFERRED_PREVIEW_SIZE_FOR_HDR_VIDEO, cameraStaticParameters.getPreferredPreviewSizeForHdrVideo());
        List<VideoConfiguration> supportedVideoConfiguration = cameraStaticParameters.getSupportedVideoConfiguration();
        List<VideoConfiguration> supportedHighFrameRateVideoConfiguration = bypassCameraStaticParameters.getSupportedHighFrameRateVideoConfiguration();
        ArrayList arrayList = new ArrayList();
        if (supportedHighFrameRateVideoConfiguration != null && !supportedHighFrameRateVideoConfiguration.isEmpty()) {
            for (VideoConfiguration videoConfiguration : supportedHighFrameRateVideoConfiguration) {
                Iterator<VideoConfiguration> it = supportedVideoConfiguration.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = false;
                        break;
                    }
                    VideoConfiguration next = it.next();
                    if (next.mWidth == videoConfiguration.mWidth && next.mHeight == videoConfiguration.mHeight) {
                        next.mFps = videoConfiguration.mFps;
                        z = true;
                        break;
                    }
                }
                if (!z) {
                    arrayList.add(videoConfiguration);
                }
            }
        }
        supportedVideoConfiguration.addAll(arrayList);
        this.VIDEO_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_VIDEO_SIZE, supportedVideoConfiguration);
        this.WHITE_BALANCE = new StringListCapabilityItem(CameraParameters.KEY_WHITE_BALANCE, cameraStaticParameters.getSupportedWhiteBalance());
        this.AE = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_AE_MODES, cameraStaticParameters.getSupportedAeModes());
        this.PREDICTIVE_CAPTURE = new StringListCapabilityItem(BypassCameraParameters.KEY_CLIMAX_RECOGNITION_VALUES, bypassCameraStaticParameters.getSupportedClimaxRecognition());
        this.BURST = new StringListCapabilityItem(BypassCameraParameters.KEY_BURST_VALUES, bypassCameraStaticParameters.getSupportedBurst());
        this.FOCUS_AREA = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_FOCUS_AREAS, cameraStaticParameters.getSupportedFocusAreaModes());
        this.HDR = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_IMAGE_STABILIZERS, cameraStaticParameters.getSupportedStillHdrValues());
        this.ISO_RANGE = new IntegerRangeCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_ISO_RANGE, cameraStaticParameters.getSupportedIsoRange());
        this.METERING = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_METERING_MODES, cameraStaticParameters.getSupportedAeRegionModes());
        this.OBJECT_TRACKING = new BooleanCapabilityItem(CameraParameters.KEY_EX_OBJECT_TRACKING_SUPPORTED, Boolean.valueOf(cameraStaticParameters.isObjectTrackingSupported()));
        this.TRACKING_FOCUS_DURING_LOCK = new BooleanCapabilityItem(CameraParameters.KEY_EX_TRACKING_FOCUS_DURING_LOCK_SUPPORTED, Boolean.valueOf(cameraStaticParameters.isTrackingFocusDuringLockSupported()));
        this.SCENE_RECOGNITION = new BooleanCapabilityItem(CameraParameters.KEY_EX_SCENE_DETECTION_SUPPORTED, Boolean.valueOf(cameraStaticParameters.isSceneDetectionSupported() && cameraStaticParameters.isConditionDetectionSupported()));
        this.SMILE_DETECTION = new BooleanCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_SMILE_DETECTIONS, Boolean.valueOf(cameraStaticParameters.isSmileDetectionAvailable()));
        this.VIDEO_STABILIZER = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_VIDEO_STABILIZERS, bypassCameraStaticParameters.getSupportedVideoStabilizer());
        this.STEADY_SHOT_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_EX_STEADY_SHOT_CONFIGURATION, bypassCameraStaticParameters.getSupportedSteadyShotConfiguration());
        this.INTELLIGENT_ACTIVE_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_EX_INTELLIGENT_ACTIVE_CONFIGURATION, bypassCameraStaticParameters.getSupportedIntelligentActiveConfiguration());
        this.MAX_SOFT_SKIN_LEVEL = new IntegerCapabilityItem(CameraParameters.KEY_EX_MAX_SOFT_SKIN_LEVEL, Integer.valueOf(cameraStaticParameters.getMaxSoftSkinLevel()));
        this.MIN_SOFT_SKIN_LEVEL = new IntegerCapabilityItem(CameraParameters.KEY_EX_MIN_SOFT_SKIN_LEVEL, Integer.valueOf(cameraStaticParameters.getMinSoftSkinLevel()));
        this.MAX_AWB_AB = new IntegerCapabilityItem(CameraParameters.KEY_EX_MAX_AWB_COMPENSATION_AB, Integer.valueOf(cameraStaticParameters.getMaxAwbColorCompensationAb()));
        this.MIN_AWB_AB = new IntegerCapabilityItem(CameraParameters.KEY_EX_MIN_AWB_COMPENSATION_AB, Integer.valueOf(cameraStaticParameters.getMinAwbColorCompensationAb()));
        this.MACRO_FOCUS_RANGE = new FloatCapabilityItem(CameraParameters.KEY_EX_MANUAL_FOCUS_FOR_MACRO, Float.valueOf(cameraStaticParameters.getMacroValueForManualFocus()));
        this.MANUAL_FOCUS = new BooleanCapabilityItem(CameraParameters.KEY_MANUAL_FOCUS_SUPPORTED, Boolean.valueOf(cameraStaticParameters.isManualFocusSupported()));
        this.MAX_SHUTTER_SPEED = new LongCapabilityItem(CameraParameters.KEY_EX_MAX_SHUTTER_SPEED, Long.valueOf(cameraStaticParameters.getMaxShutterSpeed()));
        this.MIN_SHUTTER_SPEED = new LongCapabilityItem(CameraParameters.KEY_EX_MIN_SHUTTER_SPEED, Long.valueOf(cameraStaticParameters.getMinShutterSpeed()));
        this.SHUTTER_SPEED_VALUES = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_SHUTTER_SPEED, cameraStaticParameters.getSupportedShutterSpeedValues());
        this.POWER_SAVING_MODE = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_POWER_SAVE_MODE, cameraStaticParameters.getSupportedPowerSaveModes());
        this.MIN_SHUTTER_SPEED_LIMIT = new LongCapabilityItem(CameraParameters.KEY_EX_MIN_SHUTTER_SPEED_LIMIT, Long.valueOf(cameraStaticParameters.getMinExposureTimeLimit()));
        this.SUPER_SLOW_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_EX_SUPER_SLOW_CONFIGURATION, bypassCameraStaticParameters.getSupportedSuperSlowConfiguration());
        this.SUPER_SLOW_VALUES = new StringListCapabilityItem(BypassCameraParameters.KEY_SUPER_SLOW_VALUES, bypassCameraStaticParameters.getSupportedSuperSlowmotion());
        this.FUSION_MODE = new StringListCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_FUSION_MODES, cameraStaticParameters.getSupportedFusionModes());
        this.FUSION_SUPPORTED_PICTURE_SIZES = new RectListCapabilityItem(CameraParameters.KEY_EX_FUSION_SUPPORTED_PICTURE_SIZES, cameraStaticParameters.getFusionSupportedPictureSizes());
        this.FUSION_SUPPORTED_VIDEO_CONFIGURATION = new VideoConfigurationListCapabilityItem(CameraParameters.KEY_EX_FUSION_SUPPORTED_VIDEO_CONFIGURATION, cameraStaticParameters.getFusionSupportedVideoConfiguration());
        this.FUSION_ISO_RANGE = new IntegerRangeCapabilityItem(CameraParameters.KEY_EX_SUPPORTED_FUSION_ISO_RANGE, cameraStaticParameters.getSupportedFusionIsoRange());
        this.ACTIVE_ARRAY_SIZE = new RectCapabilityItem(CameraParameters.KEY_ACTIVE_ARRAY_SIZE, cameraStaticParameters.getActiveArraySize());
        this.LENS_FACING = new IntegerCapabilityItem(CameraParameters.KEY_LENS_FACING, Integer.valueOf(cameraStaticParameters.getLensFacing()));
        this.SENSOR_NAME = new StringCapabilityItem(CameraParameters.KEY_SENSOR_NAME, cameraStaticParameters.getSensorName());
        this.HDR_VIDEO_SUPPORTED = new BooleanCapabilityItem(CameraParameters.KEY_HDR_VIDEO_SUPPORTED, Boolean.valueOf(bypassCameraStaticParameters.isVideoHdrSupported()));
        this.DISTORTION_CORRECTION = new StringListCapabilityItem(CameraParameters.KEY_DISTORTION_CORRECTION, cameraStaticParameters.getSupportedDistortionCorrection());
        this.mValues = createList();
        this.RESOLUTION_CAPABILITY = new ResolutionCapabilityItem(getResolutionOptions(context));
    }

    private List<CapabilityItem<?>> createList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.AE);
        arrayList.add(this.PREDICTIVE_CAPTURE);
        arrayList.add(this.BURST);
        arrayList.add(this.EV_MAX);
        arrayList.add(this.EV_MIN);
        arrayList.add(this.EV_STEP);
        arrayList.add(this.FLASH);
        arrayList.add(this.FOCUS_AREA);
        arrayList.add(this.FOCUS_MODE);
        arrayList.add(this.FPS_RANGE);
        arrayList.add(this.HDR);
        arrayList.add(this.ISO_RANGE);
        arrayList.add(this.MAX_NUM_FACE);
        arrayList.add(this.MAX_NUM_FOCUS_AREA);
        arrayList.add(this.MAX_ZOOM_RATIO);
        arrayList.add(this.WIDE_ZOOM_TARGET_RATIO);
        arrayList.add(this.METERING);
        arrayList.add(this.OBJECT_TRACKING);
        arrayList.add(this.TRACKING_FOCUS_DURING_LOCK);
        arrayList.add(this.PICTURE_SIZE);
        arrayList.add(this.MANUAL_ISO_SUPPORTED_PICTURE_SIZE);
        arrayList.add(this.STILL_HDR_SUPPORTED_PICTURE_SIZE);
        arrayList.add(this.PREVIEW_SIZE);
        arrayList.add(this.PREVIEW_SIZE_FOR_STILL);
        arrayList.add(this.PREVIEW_SIZE_FOR_VIDEO);
        arrayList.add(this.PREVIEW_SIZE_FOR_HDR_VIDEO);
        arrayList.add(this.SCENE_RECOGNITION);
        arrayList.add(this.SMILE_DETECTION);
        arrayList.add(this.VIDEO_CONFIGURATION);
        arrayList.add(this.VIDEO_STABILIZER);
        arrayList.add(this.WHITE_BALANCE);
        arrayList.add(this.STEADY_SHOT_CONFIGURATION);
        arrayList.add(this.INTELLIGENT_ACTIVE_CONFIGURATION);
        arrayList.add(this.MAX_SOFT_SKIN_LEVEL);
        arrayList.add(this.MIN_SOFT_SKIN_LEVEL);
        arrayList.add(this.MAX_AWB_AB);
        arrayList.add(this.MIN_AWB_AB);
        arrayList.add(this.MACRO_FOCUS_RANGE);
        arrayList.add(this.MANUAL_FOCUS);
        arrayList.add(this.MAX_SHUTTER_SPEED);
        arrayList.add(this.MIN_SHUTTER_SPEED);
        arrayList.add(this.SHUTTER_SPEED_VALUES);
        arrayList.add(this.POWER_SAVING_MODE);
        arrayList.add(this.MIN_SHUTTER_SPEED_LIMIT);
        arrayList.add(this.SUPER_SLOW_VALUES);
        arrayList.add(this.SUPER_SLOW_CONFIGURATION);
        arrayList.add(this.FUSION_MODE);
        arrayList.add(this.FUSION_SUPPORTED_PICTURE_SIZES);
        arrayList.add(this.FUSION_SUPPORTED_VIDEO_CONFIGURATION);
        arrayList.add(this.FUSION_ISO_RANGE);
        arrayList.add(this.ACTIVE_ARRAY_SIZE);
        arrayList.add(this.LENS_FACING);
        arrayList.add(this.SENSOR_NAME);
        arrayList.add(this.HDR_VIDEO_SUPPORTED);
        arrayList.add(this.DISTORTION_CORRECTION);
        return arrayList;
    }

    public List<CapabilityItem<?>> values() {
        return this.mValues;
    }

    private ResolutionOptions getResolutionOptions(Context context) {
        return new ResolutionOptions(context, this.SENSOR_NAME.get(), this.PICTURE_SIZE.get());
    }
}
