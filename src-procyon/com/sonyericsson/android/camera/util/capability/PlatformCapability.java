// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import java.util.concurrent.Callable;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import android.content.Context;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import android.os.Build;
import android.content.SharedPreferences;
import android.util.Range;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import java.util.Iterator;
import android.support.annotation.Nullable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import android.hardware.Camera;
import android.hardware.Camera$CameraInfo;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraAccessException;
import java.util.ArrayList;
import java.util.List;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import android.graphics.Rect;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.concurrent.TimeUnit;
import com.sonyericsson.android.camera.util.ThreadUtil;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.CountDownLatch;
import android.hardware.camera2.CameraManager;
import com.sonyericsson.android.camera.device.CameraInfo;
import java.util.Map;

public class PlatformCapability
{
    private static int CAPABILITY_VERSION = 1;
    private static final String FILE_NAME = "com.sonyericsson.android.camera.supported_values.";
    private static final String KEY_VERSION = "capability-version";
    private static final String PLATFORM_NAME = "platform";
    private static final long PREPARING_START_DELAY = 2000L;
    private static final long PREPARING_TIMEOUT = 1000L;
    public static final String TAG = "PlatformCapability";
    private static boolean mHasDeviceError;
    private static Map<CameraInfo.CameraId, CameraCapabilityList> sCameraCapabilityListMap;
    private static CameraManager sCameraManager;
    private static Map<HolderType, ParameterHolder> sParameterHolderMap;
    private static PlatformCapabilityList sPlatformCapabilityList;
    private static CountDownLatch sPrepareLatch;
    private static PrepareState sPrepareState;
    private static Object sPrepareStateLock;
    private static final ScheduledExecutorService sPrepareTaskExecutor;
    private static Future<Map<HolderType, ParameterHolder>> sPreparingTaskFuture;
    
    static {
        PlatformCapability.sParameterHolderMap = new HashMap<HolderType, ParameterHolder>();
        sPrepareTaskExecutor = ThreadUtil.buildScheduledExecutor("PlatformCapability", 10);
        PlatformCapability.sPrepareState = PrepareState.IDLE;
        PlatformCapability.sPrepareStateLock = new Object();
    }
    
    public static boolean awaitPrepare() {
        return awaitPrepare(0L, TimeUnit.MILLISECONDS);
    }
    
    public static boolean awaitPrepare(final long timeout, final TimeUnit unit) {
        Object sPrepareStateLock = PlatformCapability.sPrepareStateLock;
        synchronized (sPrepareStateLock) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invoked state:");
                sb.append(PlatformCapability.sPrepareState);
                CamLog.d(sb.toString());
            }
            switch (PlatformCapability$1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[PlatformCapability.sPrepareState.ordinal()]) {
                default: {
                    final CountDownLatch sPrepareLatch = PlatformCapability.sPrepareLatch;
                    monitorexit(sPrepareStateLock);
                    if (sPrepareLatch == null) {
                        CamLog.e("Latch object for preparation of platform capability doesn't exist.");
                        return true;
                    }
                    Label_0147: {
                        if (timeout <= 0L) {
                            break Label_0147;
                        }
                        try {
                            if (!sPrepareLatch.await(timeout, unit)) {
                                CamLog.e("Preparation of platform capability is timed-out.");
                            }
                            break Label_0147;
                            sPrepareLatch.await();
                        }
                        catch (final InterruptedException ex) {
                            CamLog.e("Preparation of platform capability is interrupted.");
                        }
                    }
                    synchronized (PlatformCapability.sPrepareStateLock) {
                        if (PlatformCapability$1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[PlatformCapability.sPrepareState.ordinal()] != 3) {
                            if (CamLog.DEBUG) {
                                sPrepareStateLock = new StringBuilder();
                                ((StringBuilder)sPrepareStateLock).append("Fail state:");
                                ((StringBuilder)sPrepareStateLock).append(PlatformCapability.sPrepareState);
                                CamLog.d(((StringBuilder)sPrepareStateLock).toString());
                            }
                            if (sPrepareLatch == PlatformCapability.sPrepareLatch) {
                                PlatformCapability.sPrepareState = PrepareState.TIMED_OUT;
                            }
                            return false;
                        }
                        return true;
                    }
                    break;
                }
                case 2:
                case 3: {
                    return true;
                }
            }
        }
    }
    
    public static Rect getActiveArraySize(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).ACTIVE_ARRAY_SIZE.get();
    }
    
    private static BypassCamera.Facing getBypassCameraFacing(final CameraInfo.CameraId cameraId) {
        if (cameraId == CameraInfo.CameraId.BACK) {
            return BypassCamera.Facing.BACK;
        }
        return BypassCamera.Facing.FRONT;
    }
    
    public static CameraCapabilityList getCameraCapability(final CameraInfo.CameraId cameraId) {
        return getList(cameraId);
    }
    
    private static List<CameraInfo.CameraId> getCameraIdList(final CameraManager cameraManager) {
        try {
            final String[] cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList == null) {
                return null;
            }
            final ArrayList list = new ArrayList();
            for (final String s : cameraIdList) {
                if (s.equals(CameraInfo.CameraId.BACK.getCameraDeviceId())) {
                    list.add(CameraInfo.CameraId.BACK);
                }
                else if (s.equals(CameraInfo.CameraId.FRONT.getCameraDeviceId())) {
                    list.add(CameraInfo.CameraId.FRONT);
                }
            }
            return list;
        }
        catch (final CameraAccessException ex) {
            CamLog.e("Camera Ids could not be retrieved from CameraManager.", (Throwable)ex);
            return null;
        }
    }
    
    public static void getCameraInfo(final CameraInfo.CameraId facing, final CameraInfo cameraInfo) {
        if (isPrepared()) {
            if (!hasDeviceError()) {
                cameraInfo.facing = facing;
                try {
                    cameraInfo.orientation = (int)PlatformCapability.sCameraManager.getCameraCharacteristics(facing.getCameraDeviceId()).get(CameraCharacteristics.SENSOR_ORIENTATION);
                    final Camera$CameraInfo camera$CameraInfo = new Camera$CameraInfo();
                    Camera.getCameraInfo(facing.getCameraDeviceIdApi1(), camera$CameraInfo);
                    cameraInfo.canDisableShutterSound = camera$CameraInfo.canDisableShutterSound;
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("facing = ");
                        sb.append(cameraInfo.facing);
                        sb.append(", orientation = ");
                        sb.append(cameraInfo.orientation);
                        sb.append(", canDisableShutterSound = ");
                        sb.append(cameraInfo.canDisableShutterSound);
                        CamLog.d(sb.toString());
                    }
                    return;
                }
                catch (final CameraAccessException | IllegalArgumentException ex) {
                    CamLog.e("Failed in getCameraCharacteristics", (Throwable)ex);
                    return;
                }
            }
        }
        CamLog.e("CameraInfo cannot be retrieved. Because PlatformCapability is not prepared.");
    }
    
    public static float getExposureCompensationStep(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).EV_STEP.get();
    }
    
    public static String getFileNameForCameraCapability(final CameraInfo.CameraId cameraId) {
        final StringBuilder sb = new StringBuilder();
        sb.append("com.sonyericsson.android.camera.supported_values.");
        sb.append(cameraId.getCameraDeviceId());
        return sb.toString();
    }
    
    public static String getFileNameForPlatformCapability() {
        return "com.sonyericsson.android.camera.supported_values.platform";
    }
    
    @Nullable
    private static ParameterHolder getHolder(final HolderType holderType) {
        if (PlatformCapability.sParameterHolderMap.isEmpty()) {
            if (PlatformCapability.sPreparingTaskFuture.isDone()) {
                try {
                    try {
                        if (CamLog.DEBUG) {
                            CamLog.d("getHolder: get holders: E");
                        }
                        PlatformCapability.sParameterHolderMap.putAll(PlatformCapability.sPreparingTaskFuture.get(1000L, TimeUnit.MILLISECONDS));
                        if (CamLog.DEBUG) {
                            CamLog.d("getHolder: get holders: X");
                        }
                    }
                    finally {}
                }
                catch (final ExecutionException ex) {
                    CamLog.e("Preparing failed", ex);
                }
                catch (final InterruptedException ex2) {
                    CamLog.e("Preparing interrupted", ex2);
                }
                catch (final TimeoutException ex3) {
                    CamLog.e("Preparing timed out", ex3);
                }
                PlatformCapability.sPrepareTaskExecutor.shutdown();
                return PlatformCapability.sParameterHolderMap.get(holderType);
                PlatformCapability.sPrepareTaskExecutor.shutdown();
            }
            else {
                PlatformCapability.sPreparingTaskFuture.cancel(false);
                PlatformCapability.sPrepareTaskExecutor.shutdown();
                try {
                    if (CamLog.DEBUG) {
                        CamLog.d("getHolder: call directly: E");
                    }
                    PlatformCapability.sParameterHolderMap.putAll((Map<? extends HolderType, ? extends ParameterHolder>)new PrepareParametersTask().call());
                    if (CamLog.DEBUG) {
                        CamLog.d("getHolder: call directly: X");
                    }
                }
                catch (final Exception ex4) {
                    CamLog.e("Failed to call directly", ex4);
                }
            }
        }
        return PlatformCapability.sParameterHolderMap.get(holderType);
    }
    
    public static int getLensFacing(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).LENS_FACING.get();
    }
    
    private static CameraCapabilityList getList(final CameraInfo.CameraId cameraId) {
        if (!isPrepared()) {
            throw new IllegalArgumentException("PlatformCapability is not prepared.");
        }
        if (cameraId == null) {
            throw new IllegalArgumentException("CameraId is null");
        }
        if (PlatformCapability.sCameraCapabilityListMap.containsKey(cameraId)) {
            return PlatformCapability.sCameraCapabilityListMap.get(cameraId);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Capabilities are not cached for camera:");
        sb.append(cameraId.name());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static float getMacroValue(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MACRO_FOCUS_RANGE.get();
    }
    
    public static List<Rect> getManualIsoSupportedPictureSizes(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MANUAL_ISO_SUPPORTED_PICTURE_SIZE.get();
    }
    
    public static Integer getMaxAwbColorCompensationAb(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_AWB_AB.get();
    }
    
    public static int getMaxExposureCompensation(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).EV_MAX.get();
    }
    
    public static int getMaxNumDetectedFaces(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_NUM_FACE.get();
    }
    
    public static int getMaxNumFocusAreas(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_NUM_FOCUS_AREA.get();
    }
    
    public static Rect getMaxPixelsPictureSize(final CameraInfo.CameraId cameraId) {
        final Iterator iterator = getList(cameraId).PICTURE_SIZE.get().iterator();
        Rect rect = null;
        while (iterator.hasNext()) {
            final Rect rect2 = (Rect)iterator.next();
            if (pixels(rect2) > pixels(rect)) {
                rect = rect2;
            }
        }
        return new Rect(0, 0, rect.height(), rect.width());
    }
    
    public static int getMaxPreviewFps(final CameraInfo.CameraId cameraId) {
        final List<int[]> supportedPreviewFpsRange = getSupportedPreviewFpsRange(cameraId);
        int n = 0;
        int n2 = 0;
        if (supportedPreviewFpsRange != null) {
            final Iterator<int[]> iterator = supportedPreviewFpsRange.iterator();
            while (true) {
                n = n2;
                if (!iterator.hasNext()) {
                    break;
                }
                final int[] array = iterator.next();
                if (array == null || array.length != 2 || n2 >= array[1]) {
                    continue;
                }
                n2 = array[1];
            }
        }
        return n;
    }
    
    public static long getMaxShutterSpeed(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_SHUTTER_SPEED.get();
    }
    
    public static int getMaxSoftSkinLevel(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_SOFT_SKIN_LEVEL.get();
    }
    
    public static float getMaxZoomRatio(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_ZOOM_RATIO.get();
    }
    
    public static Integer getMinAwbColorCompensationAb(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MIN_AWB_AB.get();
    }
    
    public static int getMinExposureCompensation(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).EV_MIN.get();
    }
    
    public static long getMinExposureTimeLimit(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MIN_SHUTTER_SPEED_LIMIT.get();
    }
    
    public static long getMinShutterSpeed(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MIN_SHUTTER_SPEED.get();
    }
    
    public static int getMinSoftSkinLevel(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).MIN_SOFT_SKIN_LEVEL.get();
    }
    
    public static Rect getPreferredPreviewSizeForHdrVideo(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREVIEW_SIZE_FOR_HDR_VIDEO.get();
    }
    
    public static Rect getPreferredPreviewSizeForStill(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREVIEW_SIZE_FOR_STILL.get();
    }
    
    public static Rect getPreferredPreviewSizeForVideo(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREVIEW_SIZE_FOR_VIDEO.get();
    }
    
    public static List<Rect> getStillHdrSupportedPictureSizes(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).STILL_HDR_SUPPORTED_PICTURE_SIZE.get();
    }
    
    public static long getSuperSlowFrameNum(final CameraInfo.CameraId cameraId, final VideoSize videoSize) {
        for (final VideoConfiguration videoConfiguration : getList(cameraId).SUPER_SLOW_CONFIGURATION.get()) {
            if (videoConfiguration.mWidth == videoSize.getVideoRect().width() && videoConfiguration.mHeight == videoSize.getVideoRect().height()) {
                return videoConfiguration.mFrameNum;
            }
        }
        return 0L;
    }
    
    public static long getSuperSlowFrameRate(final CameraInfo.CameraId cameraId, final VideoSize videoSize) {
        for (final VideoConfiguration videoConfiguration : getList(cameraId).SUPER_SLOW_CONFIGURATION.get()) {
            if (videoConfiguration.mWidth == videoSize.getVideoRect().width() && videoConfiguration.mHeight == videoSize.getVideoRect().height()) {
                return videoConfiguration.mFps;
            }
        }
        return 0L;
    }
    
    public static List<String> getSupportedAeModes(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).AE.get();
    }
    
    public static List<String> getSupportedClimaxRecognition(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREDICTIVE_CAPTURE.get();
    }
    
    public static List<String> getSupportedFlashModes(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).FLASH.get();
    }
    
    public static List<String> getSupportedFocusAreaModes(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).FOCUS_AREA.get();
    }
    
    public static List<String> getSupportedFocusModes(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).FOCUS_MODE.get();
    }
    
    public static Range<Integer> getSupportedFusionIsoRange(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).FUSION_ISO_RANGE.get();
    }
    
    public static Range<Integer> getSupportedIsoRange(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).ISO_RANGE.get();
    }
    
    public static List<String> getSupportedMeteringModes(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).METERING.get();
    }
    
    public static List<Rect> getSupportedPictureSizes(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).PICTURE_SIZE.get();
    }
    
    public static List<int[]> getSupportedPreviewFpsRange(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).FPS_RANGE.get();
    }
    
    public static List<Rect> getSupportedPreviewSizes(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREVIEW_SIZE.get();
    }
    
    public static List<String> getSupportedShutterSpeedValues(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).SHUTTER_SPEED_VALUES.get();
    }
    
    public static List<String> getSupportedStillHdrValues(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).HDR.get();
    }
    
    public static List<VideoConfiguration> getSupportedVideoConfiguration(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).VIDEO_CONFIGURATION.get();
    }
    
    public static List<String> getSupportedWhiteBalance(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).WHITE_BALANCE.get();
    }
    
    public static int getVideoHdrRecordingProfile() {
        final MediaCodecParametersHolder mediaCodecParametersHolder = (MediaCodecParametersHolder)getHolder(HolderType.MEDIA_CODEC);
        if (mediaCodecParametersHolder == null) {
            CamLog.e("parameter is not prepared");
            return 0;
        }
        return mediaCodecParametersHolder.getVideoHdrProfile();
    }
    
    public static float getWideZoomTargetRatio(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).WIDE_ZOOM_TARGET_RATIO.get();
    }
    
    public static List<Integer> getZoomRatios(final CameraInfo.CameraId cameraId) {
        final float maxZoomRatio = getMaxZoomRatio(cameraId);
        final ArrayList obj = new ArrayList();
        for (int i = 0; i <= 120; ++i) {
            obj.add(Math.round(((maxZoomRatio - 1.0f) * i / 120.0f + 1.0f) * 100.0f));
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getZoomRatios() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public static boolean hasDeviceError() {
        return PlatformCapability.mHasDeviceError;
    }
    
    public static boolean isAwbAbCompensationSupported(final CameraInfo.CameraId cameraId) {
        final Integer n = getList(cameraId).MAX_AWB_AB.get();
        final Integer n2 = getList(cameraId).MIN_AWB_AB.get();
        return n != null && n2 != null && n > n2;
    }
    
    private static boolean isBuildFingerprintModified(final SharedPreferences sharedPreferences) {
        if (!sharedPreferences.contains("android.os.Build.FINGERPRINT")) {
            return true;
        }
        final String string = sharedPreferences.getString("android.os.Build.FINGERPRINT", "");
        final String fingerprint = Build.FINGERPRINT;
        if (!string.equals(fingerprint)) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("version mismatch: cached: ");
                sb.append(string);
                sb.append(", current : ");
                sb.append(fingerprint);
                CamLog.d(sb.toString());
            }
            return true;
        }
        return false;
    }
    
    public static boolean isBypassCameraSupported() {
        return isBypassCameraSupportStateHolder.sIsSupported;
    }
    
    public static boolean isDisplayFlashModeSupported(final CameraInfo.CameraId cameraId) {
        final List<String> supportedFlashModes = getSupportedFlashModes(cameraId);
        return supportedFlashModes != null && supportedFlashModes.contains("display-on");
    }
    
    public static boolean isDistortionCorrectionSupported(final CameraInfo.CameraId cameraId) {
        final List list = getList(cameraId).DISTORTION_CORRECTION.get();
        return list != null && list.contains("on");
    }
    
    public static boolean isFaceDetectionAvailable(final CameraInfo.CameraId cameraId) {
        return isSmileDetectionAvailable(cameraId);
    }
    
    public static boolean isFlashModeSupported(final CameraInfo.CameraId cameraId) {
        final List<String> supportedFlashModes = getSupportedFlashModes(cameraId);
        return supportedFlashModes != null && supportedFlashModes.contains("on");
    }
    
    public static boolean isFocusSupported(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).FOCUS_MODE.get().contains("fixed") ^ true;
    }
    
    public static boolean isForceSound(final CameraInfo.CameraId cameraId) {
        final CameraInfo cameraInfo = new CameraInfo();
        getCameraInfo(cameraId, cameraInfo);
        return cameraInfo.canDisableShutterSound ^ true;
    }
    
    public static boolean isFrontCameraSupported() {
        final Iterator<CameraInfo.CameraId> iterator = PlatformCapability.sCameraCapabilityListMap.keySet().iterator();
        while (iterator.hasNext()) {
            if ((CameraInfo.CameraId)iterator.next() == CameraInfo.CameraId.FRONT) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isFullHdVideoFpsSupported(final CameraInfo.CameraId cameraId, final int n) {
        for (final VideoConfiguration videoConfiguration : getList(cameraId).VIDEO_CONFIGURATION.get()) {
            if (videoConfiguration.mWidth == 1920 && videoConfiguration.mHeight == 1080 && videoConfiguration.mFps >= n) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isFusionSupported(final CameraInfo.CameraId cameraId) {
        for (final String s : getList(cameraId).FUSION_MODE.get()) {
            if (s.equals("on") || s.equals("auto")) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isFusionSupportedWith(final CameraInfo.CameraId cameraId, final Resolution resolution) {
        if (!isFusionSupported(cameraId)) {
            return false;
        }
        for (final Rect rect : getList(cameraId).FUSION_SUPPORTED_PICTURE_SIZES.get()) {
            if (resolution.getPictureRect().width() == rect.width() && resolution.getPictureRect().height() == rect.height()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isFusionSupportedWith(final CameraInfo.CameraId cameraId, final VideoSize videoSize) {
        if (!isFusionSupported(cameraId)) {
            return false;
        }
        for (final VideoConfiguration videoConfiguration : getList(cameraId).FUSION_SUPPORTED_VIDEO_CONFIGURATION.get()) {
            if (videoSize.getVideoRect().width() == videoConfiguration.mWidth && videoSize.getVideoRect().height() == videoConfiguration.mHeight && RecordingProfile.getVideoFrameRate(videoSize, VideoHdr.HDR_OFF) <= videoConfiguration.mFps) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isHighSensitivityFusionSupported(final CameraInfo.CameraId cameraId) {
        if (!isFusionSupported(cameraId)) {
            return false;
        }
        final Range range = getList(cameraId).ISO_RANGE.get();
        final Range range2 = getList(cameraId).FUSION_ISO_RANGE.get();
        return range == null || range2 == null || range.getLower() != range2.getLower() || range.getUpper() != range2.getUpper();
    }
    
    public static boolean isLiftTriggerSupported() {
        return PlatformCapability.sPlatformCapabilityList.CAMERA_LIFT_TRIGGER.get();
    }
    
    public static boolean isManualBurstSupported(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).BURST.get().contains("on");
    }
    
    public static boolean isManualFocusSupported(final CameraInfo.CameraId cameraId) {
        final Boolean b = getList(cameraId).MANUAL_FOCUS.get();
        return b != null && b;
    }
    
    public static boolean isObjectTrackingSupported(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).OBJECT_TRACKING.get();
    }
    
    public static boolean isPowerSavingSupported(final CameraInfo.CameraId cameraId) {
        final List list = getList(cameraId).POWER_SAVING_MODE.get();
        final int size = list.size();
        final boolean b = true;
        if (size != 0) {
            boolean b2 = b;
            if (list.size() != 1) {
                return b2;
            }
            if (!list.contains("off")) {
                b2 = b;
                return b2;
            }
        }
        return false;
    }
    
    public static boolean isPredictiveCaptureShotSupported(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREDICTIVE_CAPTURE.get().size() != 0;
    }
    
    public static boolean isPrepared() {
        synchronized (PlatformCapability.sPrepareStateLock) {
            final int n = PlatformCapability$1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[PlatformCapability.sPrepareState.ordinal()];
            final boolean b = false;
            switch (n) {
                default: {
                    monitorexit(PlatformCapability.sPrepareStateLock);
                    return false;
                }
                case 3: {
                    boolean b2 = b;
                    if (PlatformCapability.sCameraCapabilityListMap != null) {
                        b2 = b;
                        if (PlatformCapability.sPlatformCapabilityList != null) {
                            b2 = true;
                        }
                    }
                    return b2;
                }
                case 2: {
                    return false;
                }
            }
        }
    }
    
    public static boolean isSceneRecognitionSupported(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).SCENE_RECOGNITION.get();
    }
    
    private static boolean isSharedPreferencesValid(final SharedPreferences sharedPreferences) {
        if (!isBuildFingerprintModified(sharedPreferences) && !isVersionModified(sharedPreferences)) {
            return true;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("prefs is invalid.");
        }
        return false;
    }
    
    public static boolean isShutterSpeedSupported(final CameraInfo.CameraId cameraId) {
        final List list = getList(cameraId).AE.get();
        final boolean empty = list.isEmpty();
        final boolean b = false;
        if (!empty && list.contains("shutter-prio")) {
            final Long n = getList(cameraId).MAX_SHUTTER_SPEED.get();
            final Long n2 = getList(cameraId).MIN_SHUTTER_SPEED.get();
            boolean b2 = b;
            if (n != null) {
                b2 = b;
                if (n2 != null) {
                    b2 = b;
                    if (n > n2) {
                        b2 = true;
                    }
                }
            }
            return b2;
        }
        return false;
    }
    
    public static boolean isSideTouchSupported() {
        return PlatformCapability.sPlatformCapabilityList.SIDE_SENSE.get();
    }
    
    public static boolean isSmileDetectionAvailable(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).SMILE_DETECTION.get();
    }
    
    public static boolean isSoftSkinSupported(final CameraInfo.CameraId cameraId) {
        return getMaxSoftSkinLevel(cameraId) > getMinSoftSkinLevel(cameraId);
    }
    
    public static boolean isStillHdrSupportedWith(final CameraInfo.CameraId cameraId, final Resolution resolution) {
        for (final Rect rect : getList(cameraId).STILL_HDR_SUPPORTED_PICTURE_SIZE.get()) {
            if (rect.width() == resolution.getPictureRect().width() && rect.height() == resolution.getPictureRect().height()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSuperSlowFullHdSupported(final CameraInfo.CameraId cameraId) {
        for (final VideoConfiguration videoConfiguration : getList(cameraId).SUPER_SLOW_CONFIGURATION.get()) {
            if (videoConfiguration.mWidth == VideoSize.FULL_HD.getVideoRect().width() && videoConfiguration.mHeight == VideoSize.FULL_HD.getVideoRect().height()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSuperSlowMotionSupported(final CameraInfo.CameraId cameraId) {
        final List list = getList(cameraId).SUPER_SLOW_VALUES.get();
        return list != null && list.contains("on");
    }
    
    public static boolean isSuperWideSupported(final CameraInfo.CameraId cameraId) {
        if (getList(cameraId).WIDE_ZOOM_TARGET_RATIO.get() > 1.01) {
            if (CamLog.VERBOSE) {
                CamLog.d("isSuperWideSupported() SUPPORT");
            }
            return true;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("isSuperWideSupported() NOT SUPPORT");
        }
        return false;
    }
    
    public static boolean isTouchAeSupported(final CameraInfo.CameraId cameraId) {
        return getSupportedMeteringModes(cameraId).contains("user");
    }
    
    public static boolean isTouchFocusSupported(final CameraInfo.CameraId cameraId) {
        final int maxNumFocusAreas = getMaxNumFocusAreas(cameraId);
        boolean b = true;
        if (maxNumFocusAreas < 1) {
            b = false;
        }
        return b;
    }
    
    public static boolean isTrackingFocusDuringLockSupported(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).TRACKING_FOCUS_DURING_LOCK.get();
    }
    
    private static boolean isVersionModified(final SharedPreferences sharedPreferences) {
        if (!sharedPreferences.contains("capability-version")) {
            return true;
        }
        final int int1 = sharedPreferences.getInt("capability-version", 0);
        if (int1 != PlatformCapability.CAPABILITY_VERSION) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("version mismatch: cached: ");
                sb.append(int1);
                sb.append(", current : ");
                sb.append(PlatformCapability.CAPABILITY_VERSION);
                CamLog.d(sb.toString());
            }
            return true;
        }
        return false;
    }
    
    public static boolean isVideoHdrSupported(final CameraInfo.CameraId cameraId) {
        return getList(cameraId).HDR_VIDEO_SUPPORTED.get();
    }
    
    public static boolean isVideoSnapshotSupported(final int n) {
        return true;
    }
    
    public static boolean isVideoStabilizerOnHdrSupported(final CameraInfo.CameraId cameraId) {
        return true;
    }
    
    public static boolean isWearableSupported() {
        return PlatformCapability.sPlatformCapabilityList.WEARABLE.get();
    }
    
    private static CameraCapabilityList loadCameraCapabilityFromDevice(final Context context, final CameraManager cameraManager, final CameraInfo.CameraId cameraId) {
        final StringBuilder sb = new StringBuilder();
        sb.append("invoked cameraId:");
        sb.append(cameraId.name());
        CamLog.d(sb.toString());
        try {
            final CameraCapabilityList list = new CameraCapabilityList(context, new CameraStaticParameters(cameraManager.getCameraCharacteristics(cameraId.getCameraDeviceId())), new BypassCameraStaticParameters(BypassCamera.getCaps(getBypassCameraFacing(cameraId))));
            final boolean empty = list.FPS_RANGE.get().isEmpty();
            final boolean empty2 = list.PREVIEW_SIZE.get().isEmpty();
            final boolean empty3 = list.PICTURE_SIZE.get().isEmpty();
            if (!empty && !empty2 && !empty3) {
                CamLog.d("CameraCapabilities are loadded from device.");
                return list;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Mandatory capabilities could not be retrieved. fps-range is empty:");
            sb2.append(empty);
            sb2.append(" preview-size is empty:");
            sb2.append(empty2);
            sb2.append(" picture-size is empty:");
            sb2.append(empty3);
            CamLog.e(sb2.toString());
            return null;
        }
        catch (final CameraAccessException ex) {
            CamLog.e("Fail to load capabilities from device.", (Throwable)ex);
            return null;
        }
    }
    
    private static CameraCapabilityList loadCameraCapabilityFromPreferences(final Context context, final CameraInfo.CameraId cameraId) {
        final StringBuilder sb = new StringBuilder();
        sb.append("invoked cameraId:");
        sb.append(cameraId.name());
        CamLog.d(sb.toString());
        final SharedPreferences loadPreferences = loadPreferences(context, getFileNameForCameraCapability(cameraId));
        if (loadPreferences == null) {
            return null;
        }
        return new CameraCapabilityList(context, loadPreferences);
    }
    
    private static PlatformCapabilityList loadPlatformCapabilityFromDevice(final Context context) {
        try {
            final PlatformCapabilityList list = new PlatformCapabilityList(context);
            CamLog.d("PlatformCapabilities are loaded from device.");
            return list;
        }
        catch (final Exception ex) {
            CamLog.e("Fail to load PlatformCapabilities from device.", ex);
            return null;
        }
    }
    
    private static PlatformCapabilityList loadPlatformCapabilityFromPreferences(final Context context) {
        final SharedPreferences loadPreferences = loadPreferences(context, getFileNameForPlatformCapability());
        if (loadPreferences == null) {
            return null;
        }
        return new PlatformCapabilityList(context, loadPreferences);
    }
    
    private static SharedPreferences loadPreferences(final Context context, final String str) {
        final SharedPreferences sharedPreferences = SharedPreferencesAccessor.getSharedPreferences(context, str, 0);
        if (sharedPreferences == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("shared-preferences is not valid: name = ");
            sb.append(str);
            CamLog.d(sb.toString());
            return null;
        }
        if (isSharedPreferencesValid(sharedPreferences)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("loaded from shared-preferences: name = ");
            sb2.append(str);
            CamLog.d(sb2.toString());
            return sharedPreferences;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("shared-preferences is not valid: name = ");
        sb3.append(str);
        CamLog.i(sb3.toString());
        sharedPreferences.edit().clear().commit();
        return null;
    }
    
    private static int pixels(final Rect rect) {
        if (rect == null) {
            return 0;
        }
        return rect.height() * rect.width();
    }
    
    public static void prepareAsync(final OnPlatformCapabilityPreparedCallback onPlatformCapabilityPreparedCallback) {
        final boolean hasDeviceError = hasDeviceError();
        setDeviceError(false);
        synchronized (PlatformCapability.sPrepareStateLock) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invoked state:");
                sb.append(PlatformCapability.sPrepareState);
                CamLog.d(sb.toString());
            }
            if (hasDeviceError) {
                PlatformCapability.sPrepareState = PrepareState.IDLE;
                if (PlatformCapability.sPlatformCapabilityList != null) {
                    PlatformCapability.sPlatformCapabilityList = null;
                }
                if (PlatformCapability.sCameraCapabilityListMap != null) {
                    PlatformCapability.sCameraCapabilityListMap.clear();
                    PlatformCapability.sCameraCapabilityListMap = null;
                }
            }
            if (PlatformCapability$1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[PlatformCapability.sPrepareState.ordinal()] != 1) {
                PlatformCapability.sPrepareState = PrepareState.RUNNING;
                PlatformCapability.sPrepareLatch = new CountDownLatch(1);
                PlatformCapability.sPrepareTaskExecutor.execute(new PrepareTask(PlatformCapability.sPrepareLatch, onPlatformCapabilityPreparedCallback));
            }
        }
    }
    
    private static void prepareInternal(final CountDownLatch countDownLatch, final OnPlatformCapabilityPreparedCallback onPlatformCapabilityPreparedCallback) {
        PerfLog.PLATFORM_CAPABILITY_PREPARE.begin();
        final Context context = CameraApplication.getContext();
        final CameraManager sCameraManager = (CameraManager)context.getSystemService((Class)CameraManager.class);
        final HashMap sCameraCapabilityListMap = new HashMap();
        try {
            final List<CameraInfo.CameraId> cameraIdList = getCameraIdList(sCameraManager);
            if (cameraIdList != null) {
                if (!cameraIdList.isEmpty()) {
                    final Iterator iterator = cameraIdList.iterator();
                    boolean b = false;
                    while (iterator.hasNext()) {
                        final CameraInfo.CameraId cameraId = (CameraInfo.CameraId)iterator.next();
                        CameraCapabilityList list;
                        if ((list = loadCameraCapabilityFromPreferences(context, cameraId)) == null) {
                            list = loadCameraCapabilityFromDevice(context, sCameraManager, cameraId);
                            b = true;
                        }
                        if (list == null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Platform capability could not load cameraCapabilityList for camera:");
                            sb.append(cameraId.name());
                            CamLog.e(sb.toString());
                            synchronized (PlatformCapability.sPrepareStateLock) {
                                switch (PlatformCapability$1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[PlatformCapability.sPrepareState.ordinal()]) {
                                    case 2:
                                    case 3: {
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("Preparation of platform capability is finished with Illegal state. state:");
                                        sb2.append(PlatformCapability.sPrepareState);
                                        CamLog.w(sb2.toString());
                                        break;
                                    }
                                    case 1: {
                                        if (countDownLatch == PlatformCapability.sPrepareLatch) {
                                            PlatformCapability.sPrepareState = PrepareState.IDLE;
                                            break;
                                        }
                                        break;
                                    }
                                }
                                countDownLatch.countDown();
                                monitorexit(PlatformCapability.sPrepareStateLock);
                                onPlatformCapabilityPreparedCallback.onPrepared();
                                if (CamLog.DEBUG) {
                                    CamLog.d("prepare: request preparing parameters");
                                }
                                PlatformCapability.sPreparingTaskFuture = (Future<Map<HolderType, ParameterHolder>>)PlatformCapability.sPrepareTaskExecutor.schedule((Callable<Object>)new PrepareParametersTask(), 2000L, TimeUnit.MILLISECONDS);
                                return;
                            }
                        }
                        sCameraCapabilityListMap.put(cameraId, list);
                    }
                    PlatformCapabilityList sPlatformCapabilityList = loadPlatformCapabilityFromPreferences(context);
                    boolean b2;
                    if (sPlatformCapabilityList == null) {
                        sPlatformCapabilityList = loadPlatformCapabilityFromDevice(context);
                        b2 = true;
                    }
                    else {
                        b2 = false;
                    }
                    if (sPlatformCapabilityList == null) {
                        synchronized (PlatformCapability.sPrepareStateLock) {
                            switch (PlatformCapability$1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[PlatformCapability.sPrepareState.ordinal()]) {
                                case 2:
                                case 3: {
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Preparation of platform capability is finished with Illegal state. state:");
                                    sb3.append(PlatformCapability.sPrepareState);
                                    CamLog.w(sb3.toString());
                                    break;
                                }
                                case 1: {
                                    if (countDownLatch == PlatformCapability.sPrepareLatch) {
                                        PlatformCapability.sPrepareState = PrepareState.IDLE;
                                        break;
                                    }
                                    break;
                                }
                            }
                            countDownLatch.countDown();
                            monitorexit(PlatformCapability.sPrepareStateLock);
                            onPlatformCapabilityPreparedCallback.onPrepared();
                            if (CamLog.DEBUG) {
                                CamLog.d("prepare: request preparing parameters");
                            }
                            PlatformCapability.sPreparingTaskFuture = (Future<Map<HolderType, ParameterHolder>>)PlatformCapability.sPrepareTaskExecutor.schedule((Callable<Object>)new PrepareParametersTask(), 2000L, TimeUnit.MILLISECONDS);
                            return;
                        }
                    }
                    synchronized (PlatformCapability.sPrepareStateLock) {
                        switch (PlatformCapability$1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[PlatformCapability.sPrepareState.ordinal()]) {
                            case 2:
                            case 3: {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("Preparation of platform capability is finished with Illegal state. state:");
                                sb4.append(PlatformCapability.sPrepareState);
                                CamLog.w(sb4.toString());
                                break;
                            }
                            case 1: {
                                if (countDownLatch != PlatformCapability.sPrepareLatch) {
                                    break;
                                }
                                PlatformCapability.sPrepareState = PrepareState.IDLE;
                                PlatformCapability.sCameraManager = sCameraManager;
                                PlatformCapability.sCameraCapabilityListMap = sCameraCapabilityListMap;
                                PlatformCapability.sPlatformCapabilityList = sPlatformCapabilityList;
                                if (b) {
                                    for (final CameraInfo.CameraId cameraId2 : PlatformCapability.sCameraCapabilityListMap.keySet()) {
                                        store(context, getFileNameForCameraCapability(cameraId2), PlatformCapability.sCameraCapabilityListMap.get(cameraId2).values());
                                    }
                                }
                                if (b2 && PlatformCapability.sPlatformCapabilityList != null) {
                                    store(context, getFileNameForPlatformCapability(), PlatformCapability.sPlatformCapabilityList.values());
                                    break;
                                }
                                break;
                            }
                        }
                        countDownLatch.countDown();
                        monitorexit(PlatformCapability.sPrepareStateLock);
                        onPlatformCapabilityPreparedCallback.onPrepared();
                        if (CamLog.DEBUG) {
                            CamLog.d("prepare: request preparing parameters");
                        }
                        PlatformCapability.sPreparingTaskFuture = (Future<Map<HolderType, ParameterHolder>>)PlatformCapability.sPrepareTaskExecutor.schedule((Callable<Object>)new PrepareParametersTask(), 2000L, TimeUnit.MILLISECONDS);
                        PerfLog.PLATFORM_CAPABILITY_PREPARE.end();
                        return;
                    }
                }
            }
            CamLog.e("Camera list could not be retrieved from camera device.");
            synchronized (PlatformCapability.sPrepareStateLock) {
                switch (PlatformCapability$1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[PlatformCapability.sPrepareState.ordinal()]) {
                    case 2:
                    case 3: {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("Preparation of platform capability is finished with Illegal state. state:");
                        sb5.append(PlatformCapability.sPrepareState);
                        CamLog.w(sb5.toString());
                        break;
                    }
                    case 1: {
                        if (countDownLatch == PlatformCapability.sPrepareLatch) {
                            PlatformCapability.sPrepareState = PrepareState.IDLE;
                            break;
                        }
                        break;
                    }
                }
                countDownLatch.countDown();
                monitorexit(PlatformCapability.sPrepareStateLock);
                onPlatformCapabilityPreparedCallback.onPrepared();
                if (CamLog.DEBUG) {
                    CamLog.d("prepare: request preparing parameters");
                }
                PlatformCapability.sPreparingTaskFuture = (Future<Map<HolderType, ParameterHolder>>)PlatformCapability.sPrepareTaskExecutor.schedule((Callable<Object>)new PrepareParametersTask(), 2000L, TimeUnit.MILLISECONDS);
            }
        }
        finally {
            synchronized (PlatformCapability.sPrepareStateLock) {
                switch (PlatformCapability$1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[PlatformCapability.sPrepareState.ordinal()]) {
                    case 2:
                    case 3: {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("Preparation of platform capability is finished with Illegal state. state:");
                        sb6.append(PlatformCapability.sPrepareState);
                        CamLog.w(sb6.toString());
                        break;
                    }
                    case 1: {
                        if (countDownLatch == PlatformCapability.sPrepareLatch) {
                            PlatformCapability.sPrepareState = PrepareState.IDLE;
                            break;
                        }
                        break;
                    }
                }
                countDownLatch.countDown();
                monitorexit(PlatformCapability.sPrepareStateLock);
                onPlatformCapabilityPreparedCallback.onPrepared();
                if (CamLog.DEBUG) {
                    CamLog.d("prepare: request preparing parameters");
                }
                PlatformCapability.sPreparingTaskFuture = (Future<Map<HolderType, ParameterHolder>>)PlatformCapability.sPrepareTaskExecutor.schedule((Callable<Object>)new PrepareParametersTask(), 2000L, TimeUnit.MILLISECONDS);
            }
        }
    }
    
    public static void setDeviceError(final boolean mHasDeviceError) {
        PlatformCapability.mHasDeviceError = mHasDeviceError;
    }
    
    static boolean store(final Context context, String fingerprint, final List<CapabilityItem<?>> obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("store: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        final SharedPreferences sharedPreferences = SharedPreferencesAccessor.getSharedPreferences(context, fingerprint, 0);
        if (sharedPreferences == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("Failed to obtain shared prefs.");
            }
            return false;
        }
        final SharedPreferences$Editor edit = sharedPreferences.edit();
        fingerprint = Build.FINGERPRINT;
        edit.putString("android.os.Build.FINGERPRINT", fingerprint);
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("android.os.Build.FINGERPRINT: ");
            sb2.append(fingerprint);
            CamLog.d(sb2.toString());
        }
        final int capability_VERSION = PlatformCapability.CAPABILITY_VERSION;
        edit.putInt("capability-version", capability_VERSION);
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("KEY_VERSION: ");
            sb3.append(capability_VERSION);
            CamLog.d(sb3.toString());
        }
        final Iterator<CapabilityItem<?>> iterator = obj.iterator();
        while (iterator.hasNext()) {
            iterator.next().write(edit);
        }
        edit.apply();
        if (CamLog.VERBOSE) {
            CamLog.d("store: success.");
        }
        return true;
    }
    
    private enum HolderType
    {
        private static final HolderType[] $VALUES;
        
        MEDIA_CODEC;
        
        static {
            $VALUES = new HolderType[] { HolderType.MEDIA_CODEC };
        }
    }
    
    public interface OnPlatformCapabilityPreparedCallback
    {
        void onPrepared();
    }
    
    private static class PrepareParametersTask implements Callable<Map<HolderType, ParameterHolder>>
    {
        @Override
        public Map<HolderType, ParameterHolder> call() throws Exception {
            if (CamLog.DEBUG) {
                CamLog.d("PrepareParametersTask: E");
            }
            final HashMap hashMap = new HashMap();
            hashMap.put(HolderType.MEDIA_CODEC, new MediaCodecParametersHolder());
            final Iterator iterator = hashMap.values().iterator();
            while (iterator.hasNext()) {
                ((ParameterHolder)iterator.next()).prepare();
            }
            if (CamLog.DEBUG) {
                CamLog.d("PrepareParametersTask: X");
            }
            return hashMap;
        }
    }
    
    enum PrepareState
    {
        private static final PrepareState[] $VALUES;
        
        IDLE, 
        RUNNING, 
        TIMED_OUT;
        
        static {
            $VALUES = new PrepareState[] { PrepareState.IDLE, PrepareState.RUNNING, PrepareState.TIMED_OUT };
        }
    }
    
    private static class PrepareTask implements Runnable
    {
        private OnPlatformCapabilityPreparedCallback mCallback;
        private final CountDownLatch mLatch;
        
        private PrepareTask(final CountDownLatch mLatch, final OnPlatformCapabilityPreparedCallback mCallback) {
            this.mLatch = mLatch;
            this.mCallback = mCallback;
        }
        
        @Override
        public void run() {
            prepareInternal(this.mLatch, this.mCallback);
        }
    }
    
    private static class isBypassCameraSupportStateHolder
    {
        private static final boolean sIsSupported;
        
        static {
            sIsSupported = isSupported();
        }
        
        private static boolean isSupported() {
            try {
                Class.forName("com.sonymobile.imageprocessor.bypasscamera2.BypassCamera");
                return true;
            }
            catch (final ClassNotFoundException ex) {
                CamLog.i("BypassCamera is NOT SUPPORTED");
                return false;
            }
        }
    }
}
