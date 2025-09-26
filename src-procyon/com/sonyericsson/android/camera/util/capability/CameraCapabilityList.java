// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import android.content.SharedPreferences;
import android.content.Context;
import android.util.Range;
import java.util.List;
import android.graphics.Rect;

public class CameraCapabilityList
{
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
    
    public CameraCapabilityList(final Context context, final SharedPreferences sharedPreferences) {
        this.EV_MAX = new IntegerCapabilityItem("max-exposure-compensation", sharedPreferences);
        this.EV_MIN = new IntegerCapabilityItem("min-exposure-compensation", sharedPreferences);
        this.EV_STEP = new FloatCapabilityItem("exposure-compensation-step", sharedPreferences);
        this.FLASH = new StringListCapabilityItem("flash-mode", sharedPreferences);
        this.FOCUS_MODE = new StringListCapabilityItem("focus-mode", sharedPreferences);
        this.FPS_RANGE = new IntArrayListCapabilityItem("preview-fps-range", sharedPreferences);
        this.MAX_NUM_FACE = new IntegerCapabilityItem("max-num-detected-faces", sharedPreferences);
        this.MAX_NUM_FOCUS_AREA = new IntegerCapabilityItem("max-num-focus-areas", sharedPreferences);
        this.MAX_ZOOM_RATIO = new FloatCapabilityItem("max-zoom-ratio", sharedPreferences);
        this.WIDE_ZOOM_TARGET_RATIO = new FloatCapabilityItem("wide-zoom-target-ratio", sharedPreferences);
        this.PICTURE_SIZE = new RectListCapabilityItem("picture-size", sharedPreferences);
        this.MANUAL_ISO_SUPPORTED_PICTURE_SIZE = new RectListCapabilityItem("sony-manual-iso-size-values", sharedPreferences);
        this.STILL_HDR_SUPPORTED_PICTURE_SIZE = new RectListCapabilityItem("sony-still-hdr-size-values", sharedPreferences);
        this.PREVIEW_SIZE = new RectListCapabilityItem("preview-size", sharedPreferences);
        this.PREVIEW_SIZE_FOR_STILL = new RectCapabilityItem("sony-preferred-preview-size-for-still", sharedPreferences);
        this.PREVIEW_SIZE_FOR_VIDEO = new RectCapabilityItem("sony-preferred-preview-size-for-video", sharedPreferences);
        this.PREVIEW_SIZE_FOR_HDR_VIDEO = new RectCapabilityItem("sony-preferred-preview-size-for-hdr-video", sharedPreferences);
        this.VIDEO_CONFIGURATION = new VideoConfigurationListCapabilityItem("video-size", sharedPreferences);
        this.WHITE_BALANCE = new StringListCapabilityItem("whitebalance", sharedPreferences);
        this.AE = new StringListCapabilityItem("sony-ae-mode-values", sharedPreferences);
        this.PREDICTIVE_CAPTURE = new StringListCapabilityItem("climax-recognition-values", sharedPreferences);
        this.BURST = new StringListCapabilityItem("burst-values", sharedPreferences);
        this.FOCUS_AREA = new StringListCapabilityItem("sony-focus-area-values", sharedPreferences);
        this.HDR = new StringListCapabilityItem("sony-is-values", sharedPreferences);
        this.ISO_RANGE = new IntegerRangeCapabilityItem("sony-iso-range", sharedPreferences);
        this.METERING = new StringListCapabilityItem("sony-metering-mode-values", sharedPreferences);
        this.OBJECT_TRACKING = new BooleanCapabilityItem("sony-object-tracking-supported", sharedPreferences);
        this.TRACKING_FOCUS_DURING_LOCK = new BooleanCapabilityItem("sony-tracking-focus-during-lock-supported", sharedPreferences);
        this.SCENE_RECOGNITION = new BooleanCapabilityItem("sony-scene-detect-supported", sharedPreferences);
        this.SMILE_DETECTION = new BooleanCapabilityItem("sony-smile-detect-values", sharedPreferences);
        this.VIDEO_STABILIZER = new StringListCapabilityItem("sony-vs-values", sharedPreferences);
        this.STEADY_SHOT_CONFIGURATION = new VideoConfigurationListCapabilityItem("sony-vs-steady-shot-config", sharedPreferences);
        this.INTELLIGENT_ACTIVE_CONFIGURATION = new VideoConfigurationListCapabilityItem("sony-vs-intelligent-active-config", sharedPreferences);
        this.MAX_SOFT_SKIN_LEVEL = new IntegerCapabilityItem("sony-max-soft-skin-level", sharedPreferences);
        this.MIN_SOFT_SKIN_LEVEL = new IntegerCapabilityItem("sony-min-soft-skin-level", sharedPreferences);
        this.MAX_AWB_AB = new IntegerCapabilityItem("sony-max-awb-compensation-ab", sharedPreferences);
        this.MIN_AWB_AB = new IntegerCapabilityItem("sony-min-awb-compensation-ab", sharedPreferences);
        this.MACRO_FOCUS_RANGE = new FloatCapabilityItem("sony-manual-focus-for-macro", sharedPreferences);
        this.MANUAL_FOCUS = new BooleanCapabilityItem("manual-focus-supported", sharedPreferences);
        this.MAX_SHUTTER_SPEED = new LongCapabilityItem("sony-max-shutter-speed", sharedPreferences);
        this.MIN_SHUTTER_SPEED = new LongCapabilityItem("sony-min-shutter-speed", sharedPreferences);
        this.SHUTTER_SPEED_VALUES = new StringListCapabilityItem("sony-shutter-speed-values", sharedPreferences);
        this.POWER_SAVING_MODE = new StringListCapabilityItem("sony-power-save-mode-values", sharedPreferences);
        this.MIN_SHUTTER_SPEED_LIMIT = new LongCapabilityItem("sony-min-shutter-speed-limit", sharedPreferences);
        this.SUPER_SLOW_CONFIGURATION = new VideoConfigurationListCapabilityItem("sony-super-slow-config", sharedPreferences);
        this.SUPER_SLOW_VALUES = new StringListCapabilityItem("super-slow-values", sharedPreferences);
        this.FUSION_MODE = new StringListCapabilityItem("sony-fusion", sharedPreferences);
        this.FUSION_SUPPORTED_PICTURE_SIZES = new RectListCapabilityItem("sony-fusion-supported-picture-size-values", sharedPreferences);
        this.FUSION_SUPPORTED_VIDEO_CONFIGURATION = new VideoConfigurationListCapabilityItem("sony-fusion-supported-video-config", sharedPreferences);
        this.FUSION_ISO_RANGE = new IntegerRangeCapabilityItem("sony-fusion-iso-range", sharedPreferences);
        this.ACTIVE_ARRAY_SIZE = new RectCapabilityItem("active-array-size", sharedPreferences);
        this.LENS_FACING = new IntegerCapabilityItem("lens-facing", sharedPreferences);
        this.SENSOR_NAME = new StringCapabilityItem("sensor-name", sharedPreferences);
        this.HDR_VIDEO_SUPPORTED = new BooleanCapabilityItem("hdr-video-supported", sharedPreferences);
        this.DISTORTION_CORRECTION = new StringListCapabilityItem("distortion-correction", sharedPreferences);
        this.mValues = this.createList();
        this.RESOLUTION_CAPABILITY = new ResolutionCapabilityItem(this.getResolutionOptions(context));
    }
    
    public CameraCapabilityList(final Context context, final CameraStaticParameters cameraStaticParameters, final BypassCameraStaticParameters bypassCameraStaticParameters) {
        this.EV_MAX = new IntegerCapabilityItem("max-exposure-compensation", cameraStaticParameters.getMaxExposureCompensation());
        this.EV_MIN = new IntegerCapabilityItem("min-exposure-compensation", cameraStaticParameters.getMinExposureCompensation());
        this.EV_STEP = new FloatCapabilityItem("exposure-compensation-step", cameraStaticParameters.getExposureCompensationStep());
        this.FLASH = new StringListCapabilityItem("flash-mode", cameraStaticParameters.getSupportedFlashModes());
        this.FOCUS_MODE = new StringListCapabilityItem("focus-mode", cameraStaticParameters.getSupportedFocusModes());
        this.FPS_RANGE = new IntArrayListCapabilityItem("preview-fps-range", cameraStaticParameters.getSupportedPreviewFpsRange());
        this.MAX_NUM_FACE = new IntegerCapabilityItem("max-num-detected-faces", cameraStaticParameters.getMaxNumDetectedFaces());
        this.MAX_NUM_FOCUS_AREA = new IntegerCapabilityItem("max-num-focus-areas", cameraStaticParameters.getMaxNumFocusAreas());
        this.MAX_ZOOM_RATIO = new FloatCapabilityItem("max-zoom-ratio", cameraStaticParameters.getMaxZoomRatio());
        this.WIDE_ZOOM_TARGET_RATIO = new FloatCapabilityItem("wide-zoom-target-ratio", cameraStaticParameters.getWideZoomTargetRatio());
        this.PICTURE_SIZE = new RectListCapabilityItem("picture-size", cameraStaticParameters.getSupportedPictureSizes());
        this.MANUAL_ISO_SUPPORTED_PICTURE_SIZE = new RectListCapabilityItem("sony-manual-iso-size-values", cameraStaticParameters.getManualIsoSupportedPictureSizes());
        this.STILL_HDR_SUPPORTED_PICTURE_SIZE = new RectListCapabilityItem("sony-still-hdr-size-values", cameraStaticParameters.getStillHdrSupportedPictureSizes());
        this.PREVIEW_SIZE = new RectListCapabilityItem("preview-size", cameraStaticParameters.getSupportedPreviewSizes());
        this.PREVIEW_SIZE_FOR_STILL = new RectCapabilityItem("sony-preferred-preview-size-for-still", cameraStaticParameters.getPreferredPreviewSizeForStill());
        this.PREVIEW_SIZE_FOR_VIDEO = new RectCapabilityItem("sony-preferred-preview-size-for-video", cameraStaticParameters.getPreferredPreviewSizeForVideo());
        this.PREVIEW_SIZE_FOR_HDR_VIDEO = new RectCapabilityItem("sony-preferred-preview-size-for-hdr-video", cameraStaticParameters.getPreferredPreviewSizeForHdrVideo());
        final List<VideoConfiguration> supportedVideoConfiguration = cameraStaticParameters.getSupportedVideoConfiguration();
        final List<VideoConfiguration> supportedHighFrameRateVideoConfiguration = bypassCameraStaticParameters.getSupportedHighFrameRateVideoConfiguration();
        final ArrayList list = new ArrayList();
        boolean b = true;
        if (supportedHighFrameRateVideoConfiguration != null && !supportedHighFrameRateVideoConfiguration.isEmpty()) {
        Label_0362:
            for (final VideoConfiguration videoConfiguration : supportedHighFrameRateVideoConfiguration) {
                while (true) {
                    for (final VideoConfiguration videoConfiguration2 : supportedVideoConfiguration) {
                        if (videoConfiguration2.mWidth == videoConfiguration.mWidth && videoConfiguration2.mHeight == videoConfiguration.mHeight) {
                            videoConfiguration2.mFps = videoConfiguration.mFps;
                            final boolean b2 = true;
                            if (!b2) {
                                list.add(videoConfiguration);
                                continue Label_0362;
                            }
                            continue Label_0362;
                        }
                    }
                    final boolean b2 = false;
                    continue;
                }
            }
        }
        supportedVideoConfiguration.addAll(list);
        this.VIDEO_CONFIGURATION = new VideoConfigurationListCapabilityItem("video-size", supportedVideoConfiguration);
        this.WHITE_BALANCE = new StringListCapabilityItem("whitebalance", cameraStaticParameters.getSupportedWhiteBalance());
        this.AE = new StringListCapabilityItem("sony-ae-mode-values", cameraStaticParameters.getSupportedAeModes());
        this.PREDICTIVE_CAPTURE = new StringListCapabilityItem("climax-recognition-values", bypassCameraStaticParameters.getSupportedClimaxRecognition());
        this.BURST = new StringListCapabilityItem("burst-values", bypassCameraStaticParameters.getSupportedBurst());
        this.FOCUS_AREA = new StringListCapabilityItem("sony-focus-area-values", cameraStaticParameters.getSupportedFocusAreaModes());
        this.HDR = new StringListCapabilityItem("sony-is-values", cameraStaticParameters.getSupportedStillHdrValues());
        this.ISO_RANGE = new IntegerRangeCapabilityItem("sony-iso-range", cameraStaticParameters.getSupportedIsoRange());
        this.METERING = new StringListCapabilityItem("sony-metering-mode-values", cameraStaticParameters.getSupportedAeRegionModes());
        this.OBJECT_TRACKING = new BooleanCapabilityItem("sony-object-tracking-supported", cameraStaticParameters.isObjectTrackingSupported());
        this.TRACKING_FOCUS_DURING_LOCK = new BooleanCapabilityItem("sony-tracking-focus-during-lock-supported", cameraStaticParameters.isTrackingFocusDuringLockSupported());
        if (!cameraStaticParameters.isSceneDetectionSupported() || !cameraStaticParameters.isConditionDetectionSupported()) {
            b = false;
        }
        this.SCENE_RECOGNITION = new BooleanCapabilityItem("sony-scene-detect-supported", b);
        this.SMILE_DETECTION = new BooleanCapabilityItem("sony-smile-detect-values", cameraStaticParameters.isSmileDetectionAvailable());
        this.VIDEO_STABILIZER = new StringListCapabilityItem("sony-vs-values", bypassCameraStaticParameters.getSupportedVideoStabilizer());
        this.STEADY_SHOT_CONFIGURATION = new VideoConfigurationListCapabilityItem("sony-vs-steady-shot-config", bypassCameraStaticParameters.getSupportedSteadyShotConfiguration());
        this.INTELLIGENT_ACTIVE_CONFIGURATION = new VideoConfigurationListCapabilityItem("sony-vs-intelligent-active-config", bypassCameraStaticParameters.getSupportedIntelligentActiveConfiguration());
        this.MAX_SOFT_SKIN_LEVEL = new IntegerCapabilityItem("sony-max-soft-skin-level", cameraStaticParameters.getMaxSoftSkinLevel());
        this.MIN_SOFT_SKIN_LEVEL = new IntegerCapabilityItem("sony-min-soft-skin-level", cameraStaticParameters.getMinSoftSkinLevel());
        this.MAX_AWB_AB = new IntegerCapabilityItem("sony-max-awb-compensation-ab", cameraStaticParameters.getMaxAwbColorCompensationAb());
        this.MIN_AWB_AB = new IntegerCapabilityItem("sony-min-awb-compensation-ab", cameraStaticParameters.getMinAwbColorCompensationAb());
        this.MACRO_FOCUS_RANGE = new FloatCapabilityItem("sony-manual-focus-for-macro", cameraStaticParameters.getMacroValueForManualFocus());
        this.MANUAL_FOCUS = new BooleanCapabilityItem("manual-focus-supported", cameraStaticParameters.isManualFocusSupported());
        this.MAX_SHUTTER_SPEED = new LongCapabilityItem("sony-max-shutter-speed", cameraStaticParameters.getMaxShutterSpeed());
        this.MIN_SHUTTER_SPEED = new LongCapabilityItem("sony-min-shutter-speed", cameraStaticParameters.getMinShutterSpeed());
        this.SHUTTER_SPEED_VALUES = new StringListCapabilityItem("sony-shutter-speed-values", cameraStaticParameters.getSupportedShutterSpeedValues());
        this.POWER_SAVING_MODE = new StringListCapabilityItem("sony-power-save-mode-values", cameraStaticParameters.getSupportedPowerSaveModes());
        this.MIN_SHUTTER_SPEED_LIMIT = new LongCapabilityItem("sony-min-shutter-speed-limit", cameraStaticParameters.getMinExposureTimeLimit());
        this.SUPER_SLOW_CONFIGURATION = new VideoConfigurationListCapabilityItem("sony-super-slow-config", bypassCameraStaticParameters.getSupportedSuperSlowConfiguration());
        this.SUPER_SLOW_VALUES = new StringListCapabilityItem("super-slow-values", bypassCameraStaticParameters.getSupportedSuperSlowmotion());
        this.FUSION_MODE = new StringListCapabilityItem("sony-fusion", cameraStaticParameters.getSupportedFusionModes());
        this.FUSION_SUPPORTED_PICTURE_SIZES = new RectListCapabilityItem("sony-fusion-supported-picture-size-values", cameraStaticParameters.getFusionSupportedPictureSizes());
        this.FUSION_SUPPORTED_VIDEO_CONFIGURATION = new VideoConfigurationListCapabilityItem("sony-fusion-supported-video-config", cameraStaticParameters.getFusionSupportedVideoConfiguration());
        this.FUSION_ISO_RANGE = new IntegerRangeCapabilityItem("sony-fusion-iso-range", cameraStaticParameters.getSupportedFusionIsoRange());
        this.ACTIVE_ARRAY_SIZE = new RectCapabilityItem("active-array-size", cameraStaticParameters.getActiveArraySize());
        this.LENS_FACING = new IntegerCapabilityItem("lens-facing", cameraStaticParameters.getLensFacing());
        this.SENSOR_NAME = new StringCapabilityItem("sensor-name", cameraStaticParameters.getSensorName());
        this.HDR_VIDEO_SUPPORTED = new BooleanCapabilityItem("hdr-video-supported", bypassCameraStaticParameters.isVideoHdrSupported());
        this.DISTORTION_CORRECTION = new StringListCapabilityItem("distortion-correction", cameraStaticParameters.getSupportedDistortionCorrection());
        this.mValues = this.createList();
        this.RESOLUTION_CAPABILITY = new ResolutionCapabilityItem(this.getResolutionOptions(context));
    }
    
    private List<CapabilityItem<?>> createList() {
        final ArrayList list = new ArrayList();
        list.add(this.AE);
        list.add(this.PREDICTIVE_CAPTURE);
        list.add(this.BURST);
        list.add(this.EV_MAX);
        list.add(this.EV_MIN);
        list.add(this.EV_STEP);
        list.add(this.FLASH);
        list.add(this.FOCUS_AREA);
        list.add(this.FOCUS_MODE);
        list.add(this.FPS_RANGE);
        list.add(this.HDR);
        list.add(this.ISO_RANGE);
        list.add(this.MAX_NUM_FACE);
        list.add(this.MAX_NUM_FOCUS_AREA);
        list.add(this.MAX_ZOOM_RATIO);
        list.add(this.WIDE_ZOOM_TARGET_RATIO);
        list.add(this.METERING);
        list.add(this.OBJECT_TRACKING);
        list.add(this.TRACKING_FOCUS_DURING_LOCK);
        list.add(this.PICTURE_SIZE);
        list.add(this.MANUAL_ISO_SUPPORTED_PICTURE_SIZE);
        list.add(this.STILL_HDR_SUPPORTED_PICTURE_SIZE);
        list.add(this.PREVIEW_SIZE);
        list.add(this.PREVIEW_SIZE_FOR_STILL);
        list.add(this.PREVIEW_SIZE_FOR_VIDEO);
        list.add(this.PREVIEW_SIZE_FOR_HDR_VIDEO);
        list.add(this.SCENE_RECOGNITION);
        list.add(this.SMILE_DETECTION);
        list.add(this.VIDEO_CONFIGURATION);
        list.add(this.VIDEO_STABILIZER);
        list.add(this.WHITE_BALANCE);
        list.add(this.STEADY_SHOT_CONFIGURATION);
        list.add(this.INTELLIGENT_ACTIVE_CONFIGURATION);
        list.add(this.MAX_SOFT_SKIN_LEVEL);
        list.add(this.MIN_SOFT_SKIN_LEVEL);
        list.add(this.MAX_AWB_AB);
        list.add(this.MIN_AWB_AB);
        list.add(this.MACRO_FOCUS_RANGE);
        list.add(this.MANUAL_FOCUS);
        list.add(this.MAX_SHUTTER_SPEED);
        list.add(this.MIN_SHUTTER_SPEED);
        list.add(this.SHUTTER_SPEED_VALUES);
        list.add(this.POWER_SAVING_MODE);
        list.add(this.MIN_SHUTTER_SPEED_LIMIT);
        list.add(this.SUPER_SLOW_VALUES);
        list.add(this.SUPER_SLOW_CONFIGURATION);
        list.add(this.FUSION_MODE);
        list.add(this.FUSION_SUPPORTED_PICTURE_SIZES);
        list.add(this.FUSION_SUPPORTED_VIDEO_CONFIGURATION);
        list.add(this.FUSION_ISO_RANGE);
        list.add(this.ACTIVE_ARRAY_SIZE);
        list.add(this.LENS_FACING);
        list.add(this.SENSOR_NAME);
        list.add(this.HDR_VIDEO_SUPPORTED);
        list.add(this.DISTORTION_CORRECTION);
        return list;
    }
    
    private ResolutionOptions getResolutionOptions(final Context context) {
        return new ResolutionOptions(context, this.SENSOR_NAME.get(), this.PICTURE_SIZE.get());
    }
    
    public List<CapabilityItem<?>> values() {
        return this.mValues;
    }
}
