// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.util.Iterator;
import com.sonyericsson.android.camera.util.CamLog;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import java.util.Map;
import android.util.Range;
import android.location.Location;
import android.graphics.Rect;
import java.util.List;

public class CameraParameters
{
    public static final String AE_MODE_AUTO = "auto";
    public static final String AE_MODE_ISO_PRIO = "iso-prio";
    public static final String AE_MODE_SEMI_AUTO = "semi-auto";
    public static final String AE_MODE_SHUTTER_PRIO = "shutter-prio";
    public static final String AE_REGION_MODE_CENTER_WEIGHTED = "center-weighted";
    public static final String AE_REGION_MODE_FACE = "face";
    public static final String AE_REGION_MODE_FRAME_AVERAGE = "frame-average";
    public static final String AE_REGION_MODE_MULTI = "multi";
    public static final String AE_REGION_MODE_SPOT = "spot";
    public static final String AE_REGION_MODE_USER_REGION = "user";
    public static final int AE_REGION_WEIGHT = 1;
    public static final String CLIMAX_RECOGNITION_AUTO = "auto";
    public static final String CLIMAX_RECOGNITION_OFF = "off";
    public static final String CLIMAX_RECOGNITION_ON = "on";
    private static final String DEFAULT_AE_MODE = "auto";
    private static final String DEFAULT_AE_REGION = "center-weighted";
    private static final String DEFAULT_AF_AREA = "center";
    private static final String DEFAULT_AF_MODE = "auto";
    private static final int DEFAULT_AWB_COLOR_COMPENSATION_AB = 0;
    private static final int DEFAULT_AWB_COLOR_COMPENSATION_GM = 0;
    private static final String DEFAULT_CLIMAX_RECOGNITION = "auto";
    private static final String DEFAULT_DISTORTION_CORRECTION = "off";
    private static final int DEFAULT_EXPOSURE_COMPENSATION = 0;
    private static final long DEFAULT_EXPOSURE_TIME_LIMIT = 0L;
    private static final String DEFAULT_FLASH_MODE = "off";
    private static final float DEFAULT_FOCUS_RANGE;
    private static final List<Rect> DEFAULT_FOCUS_RECTS;
    private static final String DEFAULT_FUSION_MODE = "off";
    private static final Location DEFAULT_GPS_DATA;
    private static final int DEFAULT_ISO = 50;
    private static final int DEFAULT_JPEG_QUALITY = 19;
    private static final int DEFAULT_MAX_PREVIEW_FPS = 30;
    private static final int DEFAULT_MIN_PREVIEW_FPS = 0;
    private static final String DEFAULT_POWER_MODE = "off";
    private static final Range<Integer> DEFAULT_PREVIEW_FPS_RANGE;
    private static final int DEFAULT_ROTATION = 0;
    private static final long DEFAULT_SHUTTER_SPEED = 4000000L;
    private static final int DEFAULT_SOFT_SKIN = 0;
    private static final String DEFAULT_STILL_HDR = "off";
    private static final String DEFAULT_WHITE_BALANCE = "off";
    public static final float DEFAULT_ZOOM_RATIO = 0.0f;
    public static final String DISPLAY_FLASH_MODE_AUTO = "display-auto";
    public static final String DISPLAY_FLASH_MODE_ON = "display-on";
    public static final String DISTORTION_CORRECTION_OFF = "off";
    public static final String DISTORTION_CORRECTION_ON = "on";
    public static final String FLASH_MODE_AUTO = "auto";
    public static final String FLASH_MODE_OFF = "off";
    public static final String FLASH_MODE_ON = "on";
    public static final String FLASH_MODE_RED_EYE = "red-eye";
    public static final String FLASH_MODE_TORCH = "torch";
    public static final String FOCUS_AREA_CENTER = "center";
    public static final String FOCUS_AREA_FACE = "face";
    public static final String FOCUS_AREA_MULTI = "multi";
    public static final String FOCUS_AREA_USER = "user";
    public static final String FOCUS_MODE_AUTO = "auto";
    public static final String FOCUS_MODE_CONTINUOUS_PICTURE = "continuous-picture";
    public static final String FOCUS_MODE_CONTINUOUS_VIDEO = "continuous-video";
    public static final String FOCUS_MODE_FIXED = "fixed";
    public static final String FOCUS_MODE_INFINITY = "infinity";
    public static final String FOCUS_MODE_MANUAL = "manual";
    public static final int FOCUS_REGION_WEIGHT = 1;
    public static final String FUSION_MODE_AUTO = "auto";
    public static final String FUSION_MODE_OFF = "off";
    public static final String FUSION_MODE_ON = "on";
    public static final String KEY_ACTIVE_ARRAY_SIZE = "active-array-size";
    public static final String KEY_DISTORTION_CORRECTION = "distortion-correction";
    public static final String KEY_EXPOSURE_COMPENSATION_STEP = "exposure-compensation-step";
    public static final String KEY_EX_AE_MODE = "sony-ae-mode";
    public static final String KEY_EX_AWB_COMPENSATION_AB = "sony-awb-compensation-ab";
    public static final String KEY_EX_CLIMAX_RECOGNITION = "climax-recognition";
    public static final String KEY_EX_FOCUS_AREA = "sony-focus-area";
    public static final String KEY_EX_FUSION_MODE = "sony-fusion-mode";
    public static final String KEY_EX_FUSION_SUPPORTED_PICTURE_SIZES = "sony-fusion-supported-picture-size-values";
    public static final String KEY_EX_FUSION_SUPPORTED_VIDEO_CONFIGURATION = "sony-fusion-supported-video-config";
    public static final String KEY_EX_IMAGE_STABILIZER = "sony-is";
    public static final String KEY_EX_INTELLIGENT_ACTIVE_CONFIGURATION = "sony-vs-intelligent-active-config";
    public static final String KEY_EX_ISO = "sony-iso";
    public static final String KEY_EX_MANUAL_FOCUS = "sony-manual-focus";
    public static final String KEY_EX_MANUAL_FOCUS_FOR_MACRO = "sony-manual-focus-for-macro";
    public static final String KEY_EX_MANUAL_ISO_SUPPORTED_SIZES = "sony-manual-iso-size-values";
    public static final String KEY_EX_MAX_AWB_COMPENSATION_AB = "sony-max-awb-compensation-ab";
    public static final String KEY_EX_MAX_SHUTTER_SPEED = "sony-max-shutter-speed";
    public static final String KEY_EX_MAX_SOFT_SKIN_LEVEL = "sony-max-soft-skin-level";
    public static final String KEY_EX_METERING_MODE = "sony-metering-mode";
    public static final String KEY_EX_MIN_AWB_COMPENSATION_AB = "sony-min-awb-compensation-ab";
    public static final String KEY_EX_MIN_SHUTTER_SPEED = "sony-min-shutter-speed";
    public static final String KEY_EX_MIN_SHUTTER_SPEED_LIMIT = "sony-min-shutter-speed-limit";
    public static final String KEY_EX_MIN_SOFT_SKIN_LEVEL = "sony-min-soft-skin-level";
    public static final String KEY_EX_OBJECT_TRACKING_SUPPORTED = "sony-object-tracking-supported";
    public static final String KEY_EX_POWER_SAVE_MODE = "sony-power-save-mode";
    public static final String KEY_EX_SCENE_DETECTION_SUPPORTED = "sony-scene-detect-supported";
    public static final String KEY_EX_SHUTTER_SPEED = "sony-shutter-speed";
    public static final String KEY_EX_SHUTTER_SPEED_LIMIT = "sony-shutter-speed-limit";
    public static final String KEY_EX_SMILE_DETECTION = "sony-smile-detect";
    public static final String KEY_EX_SOFT_SKIN_LEVEL = "sony-soft-skin-level";
    public static final String KEY_EX_STEADY_SHOT_CONFIGURATION = "sony-vs-steady-shot-config";
    public static final String KEY_EX_STILL_HDR_SUPPORTED_SIZES = "sony-still-hdr-size-values";
    public static final String KEY_EX_SUPER_SLOW = "sony-super-slow";
    public static final String KEY_EX_SUPER_SLOW_CONFIGURATION = "sony-super-slow-config";
    public static final String KEY_EX_SUPER_SLOW_FRAME_NUM = "sony-super-slow-framenum";
    public static final String KEY_EX_SUPER_SLOW_VALUES = "sony-super-slow-values";
    public static final String KEY_EX_SUPPORTED_AE_MODES = "sony-ae-mode-values";
    public static final String KEY_EX_SUPPORTED_FOCUS_AREAS = "sony-focus-area-values";
    public static final String KEY_EX_SUPPORTED_FUSION_ISO_RANGE = "sony-fusion-iso-range";
    public static final String KEY_EX_SUPPORTED_FUSION_MODES = "sony-fusion";
    public static final String KEY_EX_SUPPORTED_IMAGE_STABILIZERS = "sony-is-values";
    public static final String KEY_EX_SUPPORTED_ISO_RANGE = "sony-iso-range";
    public static final String KEY_EX_SUPPORTED_METERING_MODES = "sony-metering-mode-values";
    public static final String KEY_EX_SUPPORTED_POWER_SAVE_MODE = "sony-power-save-mode-values";
    public static final String KEY_EX_SUPPORTED_SHUTTER_SPEED = "sony-shutter-speed-values";
    public static final String KEY_EX_SUPPORTED_SMILE_DETECTIONS = "sony-smile-detect-values";
    public static final String KEY_EX_SUPPORTED_VIDEO_STABILIZERS = "sony-vs-values";
    public static final String KEY_EX_TRACKING_FOCUS_DURING_LOCK_SUPPORTED = "sony-tracking-focus-during-lock-supported";
    public static final String KEY_EX_VIDEO_STABILIZER = "sony-vs";
    public static final String KEY_FLASH_MODE = "flash-mode";
    public static final String KEY_FOCUS_AREAS = "focus-areas";
    public static final String KEY_FOCUS_MODE = "focus-mode";
    public static final String KEY_HDR_VIDEO_SUPPORTED = "hdr-video-supported";
    public static final String KEY_LENS_FACING = "lens-facing";
    public static final String KEY_MANUAL_FOCUS_SUPPORTED = "manual-focus-supported";
    public static final String KEY_MAX_EXPOSURE_COMPENSATION = "max-exposure-compensation";
    public static final String KEY_MAX_NUM_DETECTED_FACES = "max-num-detected-faces";
    public static final String KEY_MAX_NUM_FOCUS_AREAS = "max-num-focus-areas";
    public static final String KEY_MAX_ZOOM_RATIO = "max-zoom-ratio";
    public static final String KEY_MIN_EXPOSURE_COMPENSATION = "min-exposure-compensation";
    public static final String KEY_PICTURE_SIZE = "picture-size";
    public static final String KEY_PREFERRED_PREVIEW_SIZE_FOR_HDR_VIDEO = "sony-preferred-preview-size-for-hdr-video";
    public static final String KEY_PREFERRED_PREVIEW_SIZE_FOR_STILL = "sony-preferred-preview-size-for-still";
    public static final String KEY_PREFERRED_PREVIEW_SIZE_FOR_VIDEO = "sony-preferred-preview-size-for-video";
    public static final String KEY_PREVIEW_FPS_RANGE = "preview-fps-range";
    public static final String KEY_PREVIEW_SIZE = "preview-size";
    public static final String KEY_SCENE_MODE = "scene-mode";
    public static final String KEY_SENSOR_NAME = "sensor-name";
    public static final String KEY_VIDEO_SIZE = "video-size";
    public static final String KEY_WHITE_BALANCE = "whitebalance";
    public static final String KEY_WIDE_ZOOM_TARGET_RATIO = "wide-zoom-target-ratio";
    public static final Float MANUAL_FOCUS_1M;
    public static final Float MANUAL_FOCUS_INFINITY;
    public static final int MAX_ZOOM_STEP = 120;
    public static final String POWER_SAVING_MODE_LOW_POWER = "low";
    public static final String POWER_SAVING_MODE_OFF = "off";
    public static final String POWER_SAVING_MODE_ULTRA_LOW_POWER = "ultra-low";
    public static final int PREVIEW_FPS_MAX_INDEX = 1;
    public static final int PREVIEW_FPS_MIN_INDEX = 0;
    public static final String SCENE_MODE_ACTION = "action";
    public static final String SCENE_MODE_AUTO = "auto";
    public static final String SCENE_MODE_BABY = "baby";
    public static final String SCENE_MODE_BACKLIGHT = "backlight";
    public static final String SCENE_MODE_BACKLIGHT_PORTRAIT = "backlight-portrait";
    public static final String SCENE_MODE_BARCODE = "barcode";
    public static final String SCENE_MODE_BEACH = "beach";
    public static final String SCENE_MODE_CANDLELIGHT = "candlelight";
    public static final String SCENE_MODE_DARK = "dark";
    public static final String SCENE_MODE_DISH = "dish";
    public static final String SCENE_MODE_DOCUMENT = "document";
    public static final String SCENE_MODE_FIREWORKS = "fireworks";
    public static final String SCENE_MODE_HDR = "hdr";
    public static final String SCENE_MODE_LANDSCAPE = "landscape";
    public static final String SCENE_MODE_NIGHT = "night";
    public static final String SCENE_MODE_NIGHT_PORTRAIT = "night-portrait";
    public static final String SCENE_MODE_PARTY = "party";
    public static final String SCENE_MODE_PORTRAIT = "portrait";
    public static final String SCENE_MODE_SNOW = "snow";
    public static final String SCENE_MODE_SPORTS = "sports";
    public static final String SCENE_MODE_SPOTLIGHT = "spot-light";
    public static final String SCENE_MODE_STEADYPHOTO = "steadyphoto";
    public static final String SCENE_MODE_SUNSET = "sunset";
    public static final String SCENE_MODE_THEATRE = "theatre";
    public static final String SMILE_CAPTURE_OFF = "off";
    public static final String SMILE_CAPTURE_ON = "on";
    public static final String STILL_HDR_AUTO = "auto";
    public static final String STILL_HDR_OFF = "off";
    public static final String STILL_HDR_ON = "on-still-hdr";
    private static final String TAG = "CameraParameters";
    public static final String VIDEO_HDR_OFF = "off";
    public static final String VIDEO_HDR_ON = "on-video-hdr";
    public static final String VS_OFF = "off";
    public static final String VS_ON = "on";
    public static final String VS_ON_INTELLIGENT_ACTIVE = "intelligent_active";
    public static final String VS_ON_STEADY_SHOT = "on";
    public static final String WHITE_BALANCE_AUTO = "auto";
    public static final String WHITE_BALANCE_CLOUDY_DAYLIGHT = "cloudy-daylight";
    public static final String WHITE_BALANCE_DAYLIGHT = "daylight";
    public static final String WHITE_BALANCE_FLUORESCENT = "fluorescent";
    public static final String WHITE_BALANCE_INCANDESCENT = "incandescent";
    public static final String WHITE_BALANCE_OFF = "off";
    private CameraInfo.CameraId mCameraId;
    private List<Rect> mFocusRects;
    private Location mGpsData;
    private int mMaxCaptureNum;
    private Rect mMeteringArea;
    private boolean mNeedApply;
    private boolean mNeedCreatePreviewSession;
    private final Map<String, Object> mParameters;
    private Range<Integer> mPreviewFpsRange;
    private int mRotation;
    private ShutterTrigger mShutterTrigger;
    private SlowMotion mSlowMotion;
    private VideoHdr mVideoHdr;
    private VideoSize mVideoSize;
    private float mZoomRatio;
    
    static {
        MANUAL_FOCUS_1M = 1.0f;
        MANUAL_FOCUS_INFINITY = 0.0f;
        DEFAULT_FOCUS_RANGE = CameraParameters.MANUAL_FOCUS_1M;
        DEFAULT_FOCUS_RECTS = new ArrayList<Rect>(Arrays.asList(new Rect()));
        DEFAULT_PREVIEW_FPS_RANGE = new Range((Comparable)0, (Comparable)30);
    }
    
    public CameraParameters(final CameraInfo.CameraId mCameraId) {
        this.mZoomRatio = 0.0f;
        this.mMaxCaptureNum = 1;
        this.mFocusRects = CameraParameters.DEFAULT_FOCUS_RECTS;
        this.mPreviewFpsRange = CameraParameters.DEFAULT_PREVIEW_FPS_RANGE;
        this.mGpsData = CameraParameters.DEFAULT_GPS_DATA;
        this.mRotation = 0;
        this.mMeteringArea = new Rect();
        this.mNeedApply = false;
        this.mNeedCreatePreviewSession = false;
        this.mCameraId = CameraInfo.CameraId.BACK;
        this.mParameters = new HashMap<String, Object>();
        this.mCameraId = mCameraId;
        this.mParameters.put("focus-mode", "auto");
        this.mParameters.put("sony-focus-area", "center");
        this.mParameters.put("sony-manual-focus", CameraParameters.DEFAULT_FOCUS_RANGE);
        this.mParameters.put("sony-iso", 50);
        this.mParameters.put("sony-shutter-speed", 4000000L);
        this.mParameters.put("sony-ae-mode", "auto");
        this.mParameters.put("flash-mode", "off");
        this.mParameters.put("whitebalance", "off");
        this.mParameters.put("sony-is", "off");
        this.mParameters.put("sony-metering-mode", "center-weighted");
        this.mParameters.put("sony-power-save-mode", "off");
        this.mParameters.put("sony-awb-compensation-ab", 0);
        this.mParameters.put("climax-recognition", "auto");
        this.mParameters.put("sony-shutter-speed-limit", 0L);
        this.mParameters.put("sony-soft-skin-level", 0);
        this.mParameters.put("exposure-compensation-step", 0);
        this.mParameters.put("sony-fusion-mode", "off");
        this.mParameters.put("distortion-correction", "off");
        this.mParameters.put("preview-size", new Rect(0, 0, 1280, 720));
        this.mParameters.put("picture-size", new Rect(0, 0, 1280, 720));
    }
    
    @Nullable
    private Object getKeyValue(@NonNull final String s) {
        return this.mParameters.get(s);
    }
    
    private boolean setKeyValue(@NonNull final String str, @Nullable final Object obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setKeyValue : key = ");
            sb.append(str);
            sb.append(", value = ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        final Object value = this.mParameters.get(str);
        final boolean b = (value == null) ? (obj != null) : (!value.equals(obj));
        if (b) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("setKeyValue : value is changed from ");
                sb2.append(value);
                sb2.append(" to ");
                sb2.append(obj);
                CamLog.d(sb2.toString());
            }
            this.mParameters.put(str, obj);
            this.mNeedApply = true;
        }
        return b;
    }
    
    public void applied() {
        this.mNeedApply = false;
    }
    
    public void createPreviewSessionRequestDone() {
        this.mNeedCreatePreviewSession = false;
    }
    
    public void forceRequestCreatePreviewSession() {
        this.mNeedCreatePreviewSession = true;
    }
    
    public String getAeMode() {
        return (String)this.getKeyValue("sony-ae-mode");
    }
    
    public int getAwbColorCompensationAb() {
        return (int)this.getKeyValue("sony-awb-compensation-ab");
    }
    
    public CameraInfo.CameraId getCameraId() {
        return this.mCameraId;
    }
    
    public String getDistortionCorrection() {
        return (String)this.getKeyValue("distortion-correction");
    }
    
    public int getExposureCompensation() {
        return (int)this.getKeyValue("exposure-compensation-step");
    }
    
    public long getExposureTimeLimit() {
        return (long)this.getKeyValue("sony-shutter-speed-limit");
    }
    
    public String getFlashMode() {
        return (String)this.getKeyValue("flash-mode");
    }
    
    public String getFocusArea() {
        return (String)this.getKeyValue("sony-focus-area");
    }
    
    public String getFocusMode() {
        return (String)this.getKeyValue("focus-mode");
    }
    
    public float getFocusRange() {
        return (float)this.getKeyValue("sony-manual-focus");
    }
    
    public List<Rect> getFocusRectangles() {
        for (final Rect obj : this.mFocusRects) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getFocusRectangles() : rectangle = ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
        }
        return this.mFocusRects;
    }
    
    public Range<Integer> getFpsRange() {
        return this.mPreviewFpsRange;
    }
    
    public String getFusionMode() {
        return (String)this.getKeyValue("sony-fusion-mode");
    }
    
    public Location getGpsData() {
        return this.mGpsData;
    }
    
    public int getIso() {
        return (int)this.getKeyValue("sony-iso");
    }
    
    public Rect getMeteringArea() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getMeteringArea() : ");
            sb.append(this.mMeteringArea);
            CamLog.d(sb.toString());
        }
        return this.mMeteringArea;
    }
    
    public String getMeteringMode() {
        return (String)this.getKeyValue("sony-metering-mode");
    }
    
    public Rect getPictureSize() {
        return (Rect)this.getKeyValue("picture-size");
    }
    
    public String getPowerMode() {
        return (String)this.getKeyValue("sony-power-save-mode");
    }
    
    public String getPredictiveCapture() {
        return (String)this.getKeyValue("climax-recognition");
    }
    
    public int getPredictiveCaptureNum() {
        return this.mMaxCaptureNum;
    }
    
    public Rect getPreviewSize() {
        return (Rect)this.getKeyValue("preview-size");
    }
    
    public int getRotation() {
        return this.mRotation;
    }
    
    public long getShutterSpeed() {
        return (long)this.getKeyValue("sony-shutter-speed");
    }
    
    public ShutterTrigger getShutterTrigger() {
        return this.mShutterTrigger;
    }
    
    public SlowMotion getSlowMotion() {
        return this.mSlowMotion;
    }
    
    public int getSoftSkin() {
        return (int)this.getKeyValue("sony-soft-skin-level");
    }
    
    public String getStillHdr() {
        return (String)this.getKeyValue("sony-is");
    }
    
    public VideoHdr getVideoHdr() {
        return this.mVideoHdr;
    }
    
    public VideoSize getVideoSize() {
        return this.mVideoSize;
    }
    
    public String getVideoStabilizer() {
        return (String)this.getKeyValue("sony-vs");
    }
    
    public String getWhiteBalance() {
        return (String)this.getKeyValue("whitebalance");
    }
    
    public float getZoom() {
        return this.mZoomRatio;
    }
    
    public boolean needApply() {
        return this.mNeedApply;
    }
    
    public boolean needCreatePreviewSession() {
        return this.mNeedCreatePreviewSession;
    }
    
    public void removeGpsData() {
        this.setGpsData(null);
    }
    
    public void requestApply() {
        this.mNeedApply = true;
    }
    
    public void setAeMode(final String s) {
        this.setKeyValue("sony-ae-mode", s);
    }
    
    public void setAwbColorCompensationAb(final int i) {
        this.setKeyValue("sony-awb-compensation-ab", i);
    }
    
    public void setDistortionCorrection(final String s) {
        this.setKeyValue("distortion-correction", s);
    }
    
    public void setExposureCompensation(final int i) {
        this.setKeyValue("exposure-compensation-step", i);
    }
    
    public void setExposureTimeLimit(final long l) {
        this.setKeyValue("sony-shutter-speed-limit", l);
    }
    
    public void setFlashMode(final String s) {
        this.setKeyValue("flash-mode", s);
    }
    
    public void setFocusArea(final String s) {
        this.setKeyValue("sony-focus-area", s);
    }
    
    public void setFocusMode(final String s) {
        this.setKeyValue("focus-mode", s);
    }
    
    public void setFocusRange(final float f) {
        this.setKeyValue("sony-manual-focus", f);
    }
    
    public void setFocusRectangles(@Nullable final List<Rect> list) {
        List<Rect> mFocusRects = list;
        if (list == null) {
            mFocusRects = new ArrayList<Rect>();
            mFocusRects.add(new Rect());
        }
        final int size = this.mFocusRects.size();
        final int size2 = mFocusRects.size();
        final int n = 0;
        int n3 = 0;
        Label_0116: {
            if (size == size2) {
                int n2 = 0;
                while (true) {
                    n3 = n;
                    if (n2 >= this.mFocusRects.size()) {
                        break Label_0116;
                    }
                    if (!this.mFocusRects.get(n2).equals(mFocusRects.get(n2))) {
                        break;
                    }
                    ++n2;
                }
            }
            n3 = 1;
        }
        if (n3 != 0) {
            this.mFocusRects = mFocusRects;
            this.mNeedApply = true;
        }
    }
    
    public void setFusionMode(final String s) {
        this.setKeyValue("sony-fusion-mode", s);
    }
    
    public void setGpsData(final Location location) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setGpsData() : location = ");
            sb.append(location);
            CamLog.d(sb.toString());
        }
        if (location != this.mGpsData) {
            this.mNeedApply = true;
            this.mGpsData = location;
        }
    }
    
    public void setIso(final int i) {
        this.setKeyValue("sony-iso", i);
    }
    
    public void setMeteringArea(final List<Rect> list) {
        if (list != null && list.size() >= 1) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("setMeteringArea() : value = ");
                sb.append(list.get(0));
                CamLog.d(sb.toString());
            }
            final int centerX = list.get(0).centerX();
            final int centerY = list.get(0).centerY();
            final Rect mMeteringArea = new Rect(centerX, centerY, centerX + 1, centerY + 1);
            if (mMeteringArea.left != this.mMeteringArea.left || mMeteringArea.top != this.mMeteringArea.top || mMeteringArea.right != this.mMeteringArea.right || mMeteringArea.bottom != this.mMeteringArea.bottom) {
                this.mNeedApply = true;
                this.mMeteringArea = mMeteringArea;
            }
        }
    }
    
    public void setMeteringMode(final String s) {
        this.setKeyValue("sony-metering-mode", s);
    }
    
    public void setPictureSize(final Rect rect) {
        if (this.setKeyValue("picture-size", rect)) {
            this.mNeedCreatePreviewSession = true;
        }
    }
    
    public void setPowerMode(final String s) {
        this.setKeyValue("sony-power-save-mode", s);
    }
    
    public void setPredictiveCapture(final String s) {
        this.setKeyValue("climax-recognition", s);
    }
    
    public void setPredictiveCaptureNum(final int mMaxCaptureNum) {
        if (this.mMaxCaptureNum != mMaxCaptureNum) {
            this.mMaxCaptureNum = mMaxCaptureNum;
            this.mNeedCreatePreviewSession = true;
            this.mNeedApply = true;
        }
    }
    
    public void setPreviewFpsRange(final int n, final int n2) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setPreviewFpsRange() : min = ");
            sb.append(n);
            sb.append(", max = ");
            sb.append(n2);
            CamLog.d(sb.toString());
        }
        if ((int)this.mPreviewFpsRange.getLower() == n && (int)this.mPreviewFpsRange.getUpper() == n2) {
            return;
        }
        this.mNeedApply = true;
        this.mPreviewFpsRange = (Range<Integer>)new Range((Comparable)n, (Comparable)n2);
    }
    
    public void setPreviewSize(final Rect rect) {
        if (this.setKeyValue("preview-size", rect)) {
            this.mNeedCreatePreviewSession = true;
        }
    }
    
    public void setRotation(final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setRotation() : orientation = ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        if (n != this.mRotation) {
            this.mNeedApply = true;
            this.mRotation = n;
        }
    }
    
    public void setShutterSpeed(final long l) {
        this.setKeyValue("sony-shutter-speed", l);
    }
    
    public void setShutterTrigger(final ShutterTrigger mShutterTrigger) {
        if (this.mShutterTrigger != mShutterTrigger) {
            this.mShutterTrigger = mShutterTrigger;
            this.mNeedCreatePreviewSession = true;
            this.mNeedApply = true;
        }
    }
    
    public void setSlowMotion(final SlowMotion mSlowMotion) {
        if (this.mSlowMotion != mSlowMotion) {
            this.mSlowMotion = mSlowMotion;
            this.mNeedCreatePreviewSession = true;
            this.mNeedApply = true;
        }
    }
    
    public void setSoftSkin(final int i) {
        this.setKeyValue("sony-soft-skin-level", i);
    }
    
    public void setStillHdr(final String s) {
        this.setKeyValue("sony-is", s);
    }
    
    public void setVideoHdr(final VideoHdr mVideoHdr) {
        if (this.mVideoHdr != mVideoHdr) {
            this.mVideoHdr = mVideoHdr;
            this.mNeedCreatePreviewSession = true;
            this.mNeedApply = true;
        }
    }
    
    public void setVideoSize(final VideoSize mVideoSize) {
        if (this.mVideoSize != mVideoSize) {
            this.mVideoSize = mVideoSize;
            this.mNeedCreatePreviewSession = true;
            this.mNeedApply = true;
        }
    }
    
    public void setVideoStabilizer(final String s) {
        this.setKeyValue("sony-vs", s);
    }
    
    public void setWhiteBalance(final String s) {
        this.setKeyValue("whitebalance", s);
    }
    
    public void setZoom(final float n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setZoom() : zoomRatio = ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        if (n != this.mZoomRatio) {
            this.mNeedApply = true;
            this.mZoomRatio = n;
        }
    }
    
    public interface AfParametersCallback
    {
        void onReflected(final AfParametersReflectedChecker p0);
    }
    
    public enum DeviceStabilityCondition
    {
        private static final DeviceStabilityCondition[] $VALUES;
        
        AUTO(0), 
        MOTION(1), 
        STABLE(2), 
        WALK(3);
        
        private final int mConditionValue;
        
        static {
            $VALUES = new DeviceStabilityCondition[] { DeviceStabilityCondition.AUTO, DeviceStabilityCondition.MOTION, DeviceStabilityCondition.STABLE, DeviceStabilityCondition.WALK };
        }
        
        private DeviceStabilityCondition(final int mConditionValue) {
            this.mConditionValue = mConditionValue;
        }
        
        public static DeviceStabilityCondition getCondition(final int n) {
            final DeviceStabilityCondition[] values = values();
            for (int i = 0; i < values.length; ++i) {
                if (values[i].getConditionValue() == n) {
                    return values[i];
                }
            }
            return null;
        }
        
        private int getConditionValue() {
            return this.mConditionValue;
        }
    }
    
    public static class ExtFace
    {
        public int id;
        public Rect rect;
        public int smileScore;
        
        public ExtFace() {
            this.id = -1;
        }
    }
    
    interface FaceDetectionCallback
    {
        void onFaceDetection(final FaceDetectionResult p0);
    }
    
    public static class FaceDetectionResult
    {
        public List<ExtFace> extFaceList;
        public int faceNum;
        public int indexOfSelectedFace;
        
        public FaceDetectionResult() {
            this.extFaceList = new ArrayList<ExtFace>();
        }
        
        void addFaceResult(final int id, final int n, final int n2, final int n3, final int n4, final int smileScore) {
            final ExtFace extFace = new ExtFace();
            extFace.smileScore = smileScore;
            extFace.id = id;
            extFace.rect = new Rect(n, n2, n3, n4);
            this.extFaceList.add(extFace);
        }
        
        void setFrameResult(final int indexOfSelectedFace) {
            this.indexOfSelectedFace = indexOfSelectedFace;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            sb.append(this.faceNum);
            sb.append(',');
            sb.append(this.indexOfSelectedFace);
            sb.append(',');
            for (final ExtFace extFace : this.extFaceList) {
                sb.append('[');
                sb.append(extFace.id);
                sb.append(',');
                sb.append(extFace.rect.toString());
                sb.append(',');
                sb.append(extFace.smileScore);
                sb.append("]");
            }
            sb.append(']');
            return sb.toString();
        }
    }
    
    public enum FusionCondition
    {
        private static final FusionCondition[] $VALUES;
        
        CLOSE_TO_SUBJECT, 
        LENS_COVERED, 
        LOW_CONTRAST, 
        NORMAL;
        
        static {
            $VALUES = new FusionCondition[] { FusionCondition.NORMAL, FusionCondition.CLOSE_TO_SUBJECT, FusionCondition.LENS_COVERED, FusionCondition.LOW_CONTRAST };
        }
    }
    
    public static class FusionResult
    {
        private FusionCondition mFusionCondition;
        private FusionStatus mFusionStatus;
        
        public FusionResult() {
            this.mFusionStatus = FusionStatus.UNKNOWN;
            this.mFusionCondition = FusionCondition.NORMAL;
        }
        
        public FusionResult(final FusionStatus mFusionStatus, final FusionCondition mFusionCondition) {
            this.mFusionStatus = mFusionStatus;
            this.mFusionCondition = mFusionCondition;
        }
        
        public FusionCondition getFusionCondition() {
            return this.mFusionCondition;
        }
        
        public FusionStatus getFusionStatus() {
            return this.mFusionStatus;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[FusionStatus=");
            sb.append(this.mFusionStatus);
            sb.append("][FusionCondition=");
            sb.append(this.mFusionCondition);
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface FusionResultCallback
    {
        void onFusionResultChanged(final FusionResult p0);
    }
    
    public enum FusionStatus
    {
        private static final FusionStatus[] $VALUES;
        
        FUSION_MAIN, 
        FUSION_SUB_1, 
        MAIN, 
        SUB_1, 
        UNKNOWN;
        
        static {
            $VALUES = new FusionStatus[] { FusionStatus.UNKNOWN, FusionStatus.MAIN, FusionStatus.SUB_1, FusionStatus.FUSION_MAIN, FusionStatus.FUSION_SUB_1 };
        }
    }
    
    public interface ObjectTrackingCallback
    {
        void onObjectTracked(final ObjectTrackingResult p0);
    }
    
    public static class ObjectTrackingResult
    {
        public boolean mIsLost;
        public Rect mRectOfTrackedObject;
        
        protected ObjectTrackingResult(final Rect mRectOfTrackedObject, final boolean mIsLost) {
            this.mRectOfTrackedObject = mRectOfTrackedObject;
            this.mIsLost = mIsLost;
        }
    }
    
    interface SceneRecognitionCallback
    {
        void onSceneModeChanged(final SceneRecognitionResult p0);
    }
    
    public static class SceneRecognitionResult
    {
        public DeviceStabilityCondition deviceStabilityCondition;
        public boolean isMacroRange;
        public CameraParameterConverter.SceneMode sceneMode;
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            sb.append(this.sceneMode);
            sb.append(',');
            sb.append(this.deviceStabilityCondition);
            sb.append(',');
            sb.append(this.isMacroRange);
            sb.append(']');
            return sb.toString();
        }
    }
}
