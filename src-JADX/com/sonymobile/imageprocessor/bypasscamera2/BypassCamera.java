package com.sonymobile.imageprocessor.bypasscamera2;

import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCameraParameters;
import java.util.List;
import java.util.concurrent.ExecutorService;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class BypassCamera {
    private static final int CALLBACK_TYPE_ON_FINISH_BURST_DONE = 50;
    private static final int CALLBACK_TYPE_ON_PREPARE_BURST_DONE = 40;
    private static final int CALLBACK_TYPE_ON_PREPARE_SUPER_SLOW_RECORDING_DONE = 4;
    private static final int CALLBACK_TYPE_ON_PREPARE_VIDEO_RECORDING_DONE = 2;
    private static final int CALLBACK_TYPE_ON_SHUTTER_DONE = 20;
    private static final int CALLBACK_TYPE_ON_SNAPSHOT_DONE = 21;
    private static final int CALLBACK_TYPE_ON_SNAPSHOT_FREE_DONE = 30;
    private static final int CALLBACK_TYPE_ON_SNAPSHOT_READY_DONE = 10;
    private static final int CALLBACK_TYPE_ON_START_SUPER_SLOW_RECORDING_DONE = 3;
    private static final int CALLBACK_TYPE_ON_START_VIDEO_RECORDING_DONE = 0;
    private static final int CALLBACK_TYPE_ON_STOP_VIDEO_RECORDING_DONE = 1;
    private static final int INVALID_NATIVE_INSTANCE_POINTER = -1;
    private static final int RET_ERR = -1;
    private static final int RET_OK = 0;
    private static final int RET_TIMEOUT = -2;
    private static final String TAG = "BypassCamera";
    private static final Object sBypassCameraLock = new Object();
    private static boolean sIsBypassCameraLoaded = false;
    private static boolean sIsBypassCameraOpened = false;
    private ExecutorService mCallbackExecutorService;
    private Handler mCallbackHandler;
    private FinishBurstCallback mFinishBurstCallback;
    private long mNativeInstancePointer;
    private PrepareBurstCallback mPrepareBurstCallback;
    private PrepareSuperSlowRecordingCallback mPrepareSuperSlowRecordingCallback;
    private PrepareVideoRecordingCallback mPrepareVideoRecordingCallback;
    private SnapshotCallback mSnapshotCallback;
    private SnapshotFreeCallback mSnapshotFreeCallback;
    private SnapshotReadyCallback mSnapshotReadyCallback;
    private StartSuperSlowRecordingCallback mStartSuperSlowRecordingCallback;
    private StartVideoRecordingCallback mStartVideoRecordingCallback;
    private StopVideoRecordingCallback mStopVideoRecordingCallback;

    public interface FinishBurstCallback {
        void onFinishBurstDone();
    }

    public interface PrepareBurstCallback {
        void onPrepareBurstDone(boolean z);
    }

    public interface PrepareSuperSlowRecordingCallback {
        void onPrepareSuperSlowRecordingDone();
    }

    public interface PrepareVideoRecordingCallback {
        void onPrepareVideoRecordingDone();
    }

    public interface SnapshotCallback {
        void onShutterDone(int i, int i2, boolean z);

        void onSnapshotDone(int i);
    }

    public interface SnapshotFreeCallback {
        void onSnapshotFreeDone();
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReadyDone(boolean z, boolean z2, boolean z3, DisplayFlashColor displayFlashColor);
    }

    public interface StartSuperSlowRecordingCallback {
        void onStartSuperSlowRecordingDone();
    }

    public interface StartVideoRecordingCallback {
        void onStartVideoRecordingDone();
    }

    public interface StopVideoRecordingCallback {
        void onStopVideoRecordingDone();
    }

    private native int nativeChangeToPhotoMode(long j, int i, int i2, int i3, int i4, int i5, int i6);

    private native int nativeChangeToSuperSlowMode(long j, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private native int nativeChangeToVideoMode(long j, int i, int i2, int i3, int i4, int i5, int i6);

    private native int nativeFinalize(long j);

    private static native int nativeGetCaps(int i, BypassCameraParameters.Capability capability);

    private native int nativeGetLatestRequestId(long j);

    private native long nativeInitialize(int i);

    private native boolean nativeIsDebugable();

    private native int nativeRequestFinishBurstShot(long j);

    private native int nativeRequestPrepareBurstShot(long j);

    private native int nativeRequestPrepareSnapshot(long j, Surface surface, int i);

    private native int nativeRequestPrepareSuperSlowRecording(long j, Surface surface, int i, int i2, int i3);

    private native int nativeRequestPrepareVideoRecording(long j, Surface surface, int i, int i2, int i3);

    private native int nativeRequestSnapshot(long j, boolean z, boolean z2, double d, double d2, double d3, boolean z3, String str, boolean z4, int i, boolean z5, int i2, boolean z6, int i3, int i4, int i5, int i6, boolean z7, int i7);

    private native int nativeRequestSnapshotFree(long j);

    private native int nativeRequestSnapshotReady(long j);

    private native int nativeRequestStartSuperSlowRecording(long j);

    private native int nativeRequestStartVideoRecording(long j);

    private native int nativeRequestStopVideoRecording(long j);

    private native int nativeSetConfig(long j, BypassCameraParameters bypassCameraParameters);

    public enum PhotoMode {
        NORMAL(0);

        private final int mModeCode;

        PhotoMode(int i) {
            this.mModeCode = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getModeCode() {
            return this.mModeCode;
        }
    }

    public enum VideoMode {
        NORMAL(0),
        STEADYSHOT(1),
        INTELLIGENTACTIVE(2),
        HDR(3),
        HDR_STEADYSHOT(4);

        private final int mModeCode;

        VideoMode(int i) {
            this.mModeCode = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getModeCode() {
            return this.mModeCode;
        }
    }

    public enum SuperSlowMode {
        SUPER_SLOW_MOTION(0),
        SUPER_SLOW_SHOT(1);

        private final int mModeCode;

        SuperSlowMode(int i) {
            this.mModeCode = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getModeCode() {
            return this.mModeCode;
        }
    }

    public enum Facing {
        BACK(0),
        FRONT(1);

        private final int mCameraId;

        Facing(int i) {
            this.mCameraId = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getCameraId() {
            return this.mCameraId;
        }
    }

    private BypassCamera(Facing facing, Handler handler) {
        this(facing);
        this.mCallbackHandler = handler;
    }

    private BypassCamera(Facing facing, ExecutorService executorService) {
        this(facing);
        this.mCallbackExecutorService = executorService;
    }

    private BypassCamera(Facing facing) {
        this.mNativeInstancePointer = -1L;
        this.mCallbackHandler = null;
        this.mCallbackExecutorService = null;
        this.mSnapshotReadyCallback = null;
        this.mSnapshotCallback = null;
        this.mSnapshotFreeCallback = null;
        this.mPrepareBurstCallback = null;
        this.mFinishBurstCallback = null;
        this.mPrepareVideoRecordingCallback = null;
        this.mStartVideoRecordingCallback = null;
        this.mStopVideoRecordingCallback = null;
        this.mPrepareSuperSlowRecordingCallback = null;
        this.mStartSuperSlowRecordingCallback = null;
        if (nativeIsDebugable()) {
            Log.e(TAG, "CONSTRUCTOR : E");
        }
        this.mNativeInstancePointer = nativeInitialize(facing.getCameraId());
        if (this.mNativeInstancePointer == -1) {
            throw new RuntimeException("Failed to nativeInitialize().");
        }
        if (nativeIsDebugable()) {
            Log.e(TAG, "NativeInstanceHead=" + this.mNativeInstancePointer);
        }
        if (nativeIsDebugable()) {
            Log.e(TAG, "CONSTRUCTOR : X");
        }
    }

    public static void loadNativeLibrary() {
        synchronized (sBypassCameraLock) {
            if (!sIsBypassCameraLoaded) {
                boolean z = Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug");
                if (z) {
                    Log.e("TraceLog", "[CamKPI] [UPTIME=] [BypassCamera][" + Thread.currentThread().getName() + "] : loadLibrary() : E");
                }
                System.loadLibrary("imageprocessorjni");
                if (z) {
                    Log.e("TraceLog", "[CamKPI] [UPTIME=] [BypassCamera][" + Thread.currentThread().getName() + "] : loadLibrary() : X");
                }
                sIsBypassCameraLoaded = true;
            }
        }
    }

    public static BypassCamera open(Facing facing, Handler handler) {
        BypassCamera bypassCamera;
        loadNativeLibrary();
        synchronized (sBypassCameraLock) {
            if (!sIsBypassCameraOpened) {
                bypassCamera = new BypassCamera(facing, handler);
                sIsBypassCameraOpened = true;
            } else {
                throw new RuntimeException("BypassCamera is already opened.");
            }
        }
        return bypassCamera;
    }

    public static BypassCamera open(Facing facing, ExecutorService executorService) {
        BypassCamera bypassCamera;
        loadNativeLibrary();
        synchronized (sBypassCameraLock) {
            if (!sIsBypassCameraOpened) {
                bypassCamera = new BypassCamera(facing, executorService);
                sIsBypassCameraOpened = true;
            } else {
                throw new RuntimeException("BypassCamera is already opened.");
            }
        }
        return bypassCamera;
    }

    public void setPhotoCallbacks(SnapshotReadyCallback snapshotReadyCallback, SnapshotCallback snapshotCallback, SnapshotFreeCallback snapshotFreeCallback) {
        this.mSnapshotReadyCallback = snapshotReadyCallback;
        this.mSnapshotCallback = snapshotCallback;
        this.mSnapshotFreeCallback = snapshotFreeCallback;
    }

    public void setBurstCallbacks(PrepareBurstCallback prepareBurstCallback, FinishBurstCallback finishBurstCallback) {
        this.mPrepareBurstCallback = prepareBurstCallback;
        this.mFinishBurstCallback = finishBurstCallback;
    }

    public void setVideoCallbacks(PrepareVideoRecordingCallback prepareVideoRecordingCallback, StartVideoRecordingCallback startVideoRecordingCallback, StopVideoRecordingCallback stopVideoRecordingCallback) {
        this.mPrepareVideoRecordingCallback = prepareVideoRecordingCallback;
        this.mStartVideoRecordingCallback = startVideoRecordingCallback;
        this.mStopVideoRecordingCallback = stopVideoRecordingCallback;
    }

    public void setSuperSlowCallbacks(PrepareSuperSlowRecordingCallback prepareSuperSlowRecordingCallback, StartSuperSlowRecordingCallback startSuperSlowRecordingCallback) {
        this.mPrepareSuperSlowRecordingCallback = prepareSuperSlowRecordingCallback;
        this.mStartSuperSlowRecordingCallback = startSuperSlowRecordingCallback;
    }

    public void close() {
        if (nativeIsDebugable()) {
            Log.e(TAG, "close() : E");
        }
        synchronized (this) {
            this.mSnapshotReadyCallback = null;
            this.mSnapshotCallback = null;
            this.mSnapshotFreeCallback = null;
            this.mStartVideoRecordingCallback = null;
            this.mStopVideoRecordingCallback = null;
            this.mPrepareBurstCallback = null;
            this.mFinishBurstCallback = null;
        }
        nativeFinalize(this.mNativeInstancePointer);
        if (nativeIsDebugable()) {
            Log.e(TAG, "NativeInstanceHead=" + this.mNativeInstancePointer);
        }
        this.mNativeInstancePointer = -1L;
        synchronized (sBypassCameraLock) {
            sIsBypassCameraOpened = false;
        }
        if (nativeIsDebugable()) {
            Log.e(TAG, "close() : X");
        }
    }

    public void changeToPhotoMode(PhotoMode photoMode, Size size, Size size2, int i) throws BypassCameraTimeoutException {
        switch (nativeChangeToPhotoMode(this.mNativeInstancePointer, photoMode.getModeCode(), size.getWidth(), size.getHeight(), size2.getWidth(), size2.getHeight(), i)) {
            case -2:
                throw new BypassCameraTimeoutException("Timed out to change mode to : " + photoMode);
            case -1:
                this.mNativeInstancePointer = -1L;
                throw new RuntimeException("Failed to change mode to : " + photoMode);
            default:
                return;
        }
    }

    public void changeToVideoMode(VideoMode videoMode, Size size, Size size2, int i) throws BypassCameraTimeoutException {
        switch (nativeChangeToVideoMode(this.mNativeInstancePointer, videoMode.getModeCode(), size.getWidth(), size.getHeight(), size2.getWidth(), size2.getHeight(), i)) {
            case -2:
                throw new BypassCameraTimeoutException("Timed out to change mode to : " + videoMode);
            case -1:
                this.mNativeInstancePointer = -1L;
                throw new RuntimeException("Failed to change mode to : " + videoMode);
            default:
                return;
        }
    }

    public void changeToSuperSlowMode(SuperSlowMode superSlowMode, Size size, Size size2, SuperSlowRecordingParameters superSlowRecordingParameters) {
        throw new RuntimeException("Not implemented.");
    }

    public void changeToSuperSlowMode(SuperSlowMode superSlowMode, Size size, Size size2, int i, SuperSlowRecordingParameters superSlowRecordingParameters) throws BypassCameraTimeoutException {
        switch (nativeChangeToSuperSlowMode(this.mNativeInstancePointer, superSlowMode.getModeCode(), size.getWidth(), size.getHeight(), size2.getWidth(), size2.getHeight(), i, superSlowRecordingParameters.fps, superSlowRecordingParameters.frameNum)) {
            case -2:
                throw new BypassCameraTimeoutException("Timed out to change mode to : " + superSlowMode);
            case -1:
                this.mNativeInstancePointer = -1L;
                throw new RuntimeException("Failed to change mode to : " + superSlowMode);
            default:
                return;
        }
    }

    public void requestPrepareSnapshot(Surface surface, int i) {
        if (nativeRequestPrepareSnapshot(this.mNativeInstancePointer, surface, i) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to requestPrepareSnapshot().");
        }
    }

    public void requestSnapshotReady() {
        if (nativeRequestSnapshotReady(this.mNativeInstancePointer) != 0) {
            throw new RuntimeException("Failed to nativeRequestSnapshotReady().");
        }
    }

    public int requestSnapshot(SnapshotInfo snapshotInfo) {
        if (nativeRequestSnapshot(this.mNativeInstancePointer, snapshotInfo.isValid, snapshotInfo.exifGpsInfo.isExifGpsEnabled, snapshotInfo.exifGpsInfo.exifGpsLatitude, snapshotInfo.exifGpsInfo.exifGpsLongitude, snapshotInfo.exifGpsInfo.exifGpsAltitude, snapshotInfo.exifGpsInfo.isExifGpsProcMethodEnabled, snapshotInfo.exifGpsInfo.exifGpsProcMethod, snapshotInfo.exifGpsInfo.isExifGpsTimestampEnabled, snapshotInfo.exifGpsInfo.exifGpsTimestamp, snapshotInfo.exifOrientationInfo.isExifOrientationEnabled, snapshotInfo.exifOrientationInfo.exifOrientation, snapshotInfo.exifThumbnailInfo.isExifThumbEnabled, snapshotInfo.exifThumbnailInfo.exifThumbWidth, snapshotInfo.exifThumbnailInfo.exifThumbHeight, snapshotInfo.exifThumbnailInfo.exifThumbQuality, snapshotInfo.quality, snapshotInfo.isQualityAutoControlEnabled, snapshotInfo.captureNum) != 0) {
            throw new RuntimeException("Failed to nativeRequestSnapshot().");
        }
        return nativeGetLatestRequestId(this.mNativeInstancePointer);
    }

    public static class SnapshotInfo {
        public final int captureNum;
        public final ExifGpsInfo exifGpsInfo;
        public final ExifOrientationInfo exifOrientationInfo;
        public final ExifThumbnailInfo exifThumbnailInfo;
        public final boolean isQualityAutoControlEnabled;
        public final boolean isValid;
        public final int quality;

        public static class ExifGpsInfo {
            public final double exifGpsAltitude;
            public final double exifGpsLatitude;
            public final double exifGpsLongitude;
            public final String exifGpsProcMethod;
            public final int exifGpsTimestamp;
            public final boolean isExifGpsEnabled;
            public final boolean isExifGpsProcMethodEnabled;
            public final boolean isExifGpsTimestampEnabled;

            public ExifGpsInfo(boolean z, double d, double d2, double d3, boolean z2, String str, boolean z3, int i) {
                this.isExifGpsEnabled = z;
                this.exifGpsLatitude = d;
                this.exifGpsLongitude = d2;
                this.exifGpsAltitude = d3;
                this.isExifGpsProcMethodEnabled = z2;
                this.exifGpsProcMethod = str;
                this.isExifGpsTimestampEnabled = z3;
                this.exifGpsTimestamp = i;
            }
        }

        public static class ExifOrientationInfo {
            public final int exifOrientation;
            public final boolean isExifOrientationEnabled;

            public ExifOrientationInfo(boolean z, int i) {
                this.isExifOrientationEnabled = z;
                this.exifOrientation = i;
            }
        }

        public static class ExifThumbnailInfo {
            public final int exifThumbHeight;
            public final int exifThumbQuality;
            public final int exifThumbWidth;
            public final boolean isExifThumbEnabled;

            public ExifThumbnailInfo(boolean z, int i, int i2, int i3) {
                this.isExifThumbEnabled = z;
                this.exifThumbWidth = i;
                this.exifThumbHeight = i2;
                this.exifThumbQuality = i3;
            }
        }

        public SnapshotInfo(boolean z, ExifGpsInfo exifGpsInfo, ExifOrientationInfo exifOrientationInfo, ExifThumbnailInfo exifThumbnailInfo, int i, boolean z2, int i2) {
            this.isValid = z;
            this.exifGpsInfo = exifGpsInfo;
            this.exifOrientationInfo = exifOrientationInfo;
            this.exifThumbnailInfo = exifThumbnailInfo;
            this.quality = i;
            this.isQualityAutoControlEnabled = z2;
            this.captureNum = i2;
        }
    }

    public void requestSnapshotFree() {
        if (nativeRequestSnapshotFree(this.mNativeInstancePointer) != 0) {
            throw new RuntimeException("Failed to nativeRequestSnapshotFree().");
        }
    }

    public void requestPrepareVideoRecording(Surface surface, RecordingParameters recordingParameters) {
        if (nativeRequestPrepareVideoRecording(this.mNativeInstancePointer, surface, recordingParameters.dataSpace.colorStandard, recordingParameters.dataSpace.colorTransfer, recordingParameters.dataSpace.colorRange) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestPrepareVideoRecording().");
        }
    }

    public void requestStartVideoRecording() {
        if (nativeRequestStartVideoRecording(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestStartVideoRecording().");
        }
    }

    public void requestStopVideoRecording() {
        if (nativeRequestStopVideoRecording(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestStopVideoRecording().");
        }
    }

    public static class DataSpace {
        public final int colorRange;
        public final int colorStandard;
        public final int colorTransfer;

        public DataSpace(int i, int i2, int i3) {
            this.colorStandard = i;
            this.colorTransfer = i2;
            this.colorRange = i3;
        }
    }

    public static class DisplayFlashColor {
        public final int colorBlue;
        public final int colorGreen;
        public final int colorRed;

        public DisplayFlashColor(int i, int i2, int i3) {
            this.colorRed = i;
            this.colorGreen = i2;
            this.colorBlue = i3;
        }
    }

    public static class RecordingParameters {
        public final DataSpace dataSpace;

        public RecordingParameters(DataSpace dataSpace) {
            this.dataSpace = dataSpace;
        }
    }

    public static class SuperSlowRecordingParameters {
        public final int fps;
        public final int frameNum;

        public SuperSlowRecordingParameters(int i, int i2) {
            this.fps = i;
            this.frameNum = i2;
        }
    }

    public void requestPrepareSuperSlowRecording(Surface surface, RecordingParameters recordingParameters) {
        if (nativeRequestPrepareSuperSlowRecording(this.mNativeInstancePointer, surface, recordingParameters.dataSpace.colorStandard, recordingParameters.dataSpace.colorTransfer, recordingParameters.dataSpace.colorRange) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestPrepareSuperSlowRecording().");
        }
    }

    public void requestStartSuperSlowRecording() {
        if (nativeRequestStartSuperSlowRecording(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestStartSuperSlowRecording().");
        }
    }

    public void requestPrepareBurstShot() {
        if (nativeRequestPrepareBurstShot(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestPrepareBurstShot().");
        }
    }

    public void requestFinishBurstShot() {
        if (nativeRequestFinishBurstShot(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestFinishBurstShot().");
        }
    }

    public static BypassCameraParameters getCaps(Facing facing) {
        loadNativeLibrary();
        BypassCameraParameters.Capability capability = new BypassCameraParameters.Capability();
        if (nativeGetCaps(facing.getCameraId(), capability) != 0) {
            throw new RuntimeException("Failed to nativeGetCaps().");
        }
        BypassCameraParameters bypassCameraParameters = new BypassCameraParameters();
        bypassCameraParameters.set(BypassCameraParameters.KEY_VIDEO_STABILIZERS_VALUES, createListValue(capability.mVideoStabilizationMode));
        bypassCameraParameters.set(BypassCameraParameters.KEY_VIDEO_HIGH_FRAME_RATE_CONFIGURATION, createSupportedInfoListValue(capability.mHighFrameRateSupportedInfoList));
        bypassCameraParameters.set(BypassCameraParameters.KEY_VIDEO_STABILIZERS_STEADY_SHOT_CONFIGURATION, createSupportedInfoListValue(capability.mSteadyShotSupportedInfoList));
        bypassCameraParameters.set(BypassCameraParameters.KEY_VIDEO_STABILIZERS_INTELLIGENT_ACTIVE_CONFIGURATION, createSupportedInfoListValue(capability.mIntelligentActiveSupportedInfoList));
        bypassCameraParameters.set(BypassCameraParameters.KEY_SUPER_SLOW_VALUES, createListValue(capability.mSuperSlowMode));
        bypassCameraParameters.set(BypassCameraParameters.KEY_BURST_VALUES, createListValue(capability.mBurstMode));
        bypassCameraParameters.set(BypassCameraParameters.KEY_VIDEO_HDR_VALUES, createListValue(capability.mVideoHdrMode));
        bypassCameraParameters.set(BypassCameraParameters.KEY_VIDEO_SUPER_SLOW_CONFIGURATION, createSuperSlowSupportedInfoListValue(capability.mSuperSlowSupportedInfoList, capability.mSuperSlowFrameNumList));
        bypassCameraParameters.set(BypassCameraParameters.KEY_CLIMAX_RECOGNITION_VALUES, createListValue(capability.mClimaxRecognitionMode));
        return bypassCameraParameters;
    }

    private static String createSupportedInfoListValue(List<BypassCameraParameters.Capability.SupportedInfo> list) {
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (BypassCameraParameters.Capability.SupportedInfo supportedInfo : list) {
            if (supportedInfo != list.get(0)) {
                sb.append(',');
            }
            sb.append(supportedInfo.width);
            sb.append('x');
            sb.append(supportedInfo.height);
            sb.append('/');
            sb.append(supportedInfo.fps);
        }
        return sb.toString();
    }

    private static String createSuperSlowSupportedInfoListValue(List<BypassCameraParameters.Capability.SupportedInfo> list, List<Integer> list2) {
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            BypassCameraParameters.Capability.SupportedInfo supportedInfo = list.get(i);
            int iIntValue = list2.get(i).intValue();
            if (i > 0) {
                sb.append(',');
            }
            sb.append(supportedInfo.width);
            sb.append('x');
            sb.append(supportedInfo.height);
            sb.append('@');
            sb.append(iIntValue);
            sb.append('/');
            sb.append(supportedInfo.fps);
        }
        return sb.toString();
    }

    private static String createListValue(List<String> list) {
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (str != list.get(0)) {
                sb.append(',');
            }
            sb.append(str);
        }
        return sb.toString();
    }

    public void setConfig(BypassCameraParameters bypassCameraParameters) {
        if (nativeSetConfig(this.mNativeInstancePointer, bypassCameraParameters) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeSetConfig().");
        }
    }

    private final void callbackFromNative(int i, int i2, int i3, boolean z, boolean z2, boolean z3, int i4, int i5, int i6) {
        if (nativeIsDebugable()) {
            Log.e(TAG, "callbackFromNative() : E");
        }
        if (i != 10) {
            if (i == 30) {
                if (nativeIsDebugable()) {
                    Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_SNAPSHOT_FREE_DONE");
                }
                callbackToClient(new CallbackSnapshotFreeTask());
            } else if (i == 40) {
                if (nativeIsDebugable()) {
                    Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_PREPARE_BURST_DONE");
                }
                callbackToClient(new CallbackPrepareBurstTask(z2));
            } else if (i != 50) {
                switch (i) {
                    case 20:
                        if (nativeIsDebugable()) {
                            Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_SHUTTER_DONE");
                        }
                        callbackToClient(new CallbackShutterTask(i2, i3, z));
                        break;
                    case 21:
                        if (nativeIsDebugable()) {
                            Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_SNAPSHOT_DONE");
                        }
                        callbackToClient(new CallbackSnapshotTask(i2));
                        break;
                }
            } else {
                if (nativeIsDebugable()) {
                    Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_FINISH_BURST_DONE");
                }
                callbackToClient(new CallbackFinishBurstTask());
            }
        } else {
            if (nativeIsDebugable()) {
                Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_SNAPSHOT_READY_DONE");
            }
            callbackToClient(new CallbackSnapshotReadyTask(z2, z, z3, i4, i5, i6));
        }
        if (nativeIsDebugable()) {
            Log.e(TAG, "callbackFromNative() : X");
        }
    }

    private void callbackToClient(Runnable runnable) {
        if (this.mCallbackExecutorService == null && this.mCallbackHandler == null) {
            Log.e(TAG, "callbackToClient() : Callback object is not installed.");
            return;
        }
        if (this.mCallbackExecutorService != null && !this.mCallbackExecutorService.isShutdown()) {
            this.mCallbackExecutorService.execute(runnable);
        } else if (this.mCallbackHandler != null) {
            this.mCallbackHandler.post(runnable);
        } else {
            Log.w(TAG, "callbackToClient() : Callback is not invoked.");
        }
    }

    private class CallbackSnapshotReadyTask implements Runnable {
        private final DisplayFlashColor mDisplayFlashColor;
        private final boolean mIsAfSuccessed;
        private final boolean mIsHighQualityBurstAvailable;
        private final boolean mRequireDisplayFlash;

        public CallbackSnapshotReadyTask(boolean z, boolean z2, boolean z3, int i, int i2, int i3) {
            this.mIsHighQualityBurstAvailable = z;
            this.mIsAfSuccessed = z2;
            this.mRequireDisplayFlash = z3;
            this.mDisplayFlashColor = new DisplayFlashColor(i, i2, i3);
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mSnapshotReadyCallback != null) {
                    BypassCamera.this.mSnapshotReadyCallback.onSnapshotReadyDone(this.mIsHighQualityBurstAvailable, this.mIsAfSuccessed, this.mRequireDisplayFlash, this.mDisplayFlashColor);
                }
            }
        }
    }

    private class CallbackShutterTask implements Runnable {
        private final int mCaptureNum;
        private final boolean mIsAfSuccessed;
        private final int mRequestId;

        public CallbackShutterTask(int i, int i2, boolean z) {
            this.mRequestId = i;
            this.mCaptureNum = i2;
            this.mIsAfSuccessed = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mSnapshotCallback != null) {
                    BypassCamera.this.mSnapshotCallback.onShutterDone(this.mRequestId, this.mCaptureNum, this.mIsAfSuccessed);
                }
            }
        }
    }

    private class CallbackSnapshotTask implements Runnable {
        private final int mRequestId;

        public CallbackSnapshotTask(int i) {
            this.mRequestId = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mSnapshotCallback != null) {
                    BypassCamera.this.mSnapshotCallback.onSnapshotDone(this.mRequestId);
                }
            }
        }
    }

    private class CallbackSnapshotFreeTask implements Runnable {
        private CallbackSnapshotFreeTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mSnapshotFreeCallback != null) {
                    BypassCamera.this.mSnapshotFreeCallback.onSnapshotFreeDone();
                }
            }
        }
    }

    private class CallbackPrepareBurstTask implements Runnable {
        private final boolean mIsSuccess;

        public CallbackPrepareBurstTask(boolean z) {
            this.mIsSuccess = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mPrepareBurstCallback != null) {
                    BypassCamera.this.mPrepareBurstCallback.onPrepareBurstDone(this.mIsSuccess);
                }
            }
        }
    }

    private class CallbackFinishBurstTask implements Runnable {
        private CallbackFinishBurstTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mFinishBurstCallback != null) {
                    BypassCamera.this.mFinishBurstCallback.onFinishBurstDone();
                }
            }
        }
    }

    private final void callbackFromNativeVideo(int i) {
        if (nativeIsDebugable()) {
            Log.e(TAG, "callbackFromNativeVideo() : E");
        }
        switch (i) {
            case 0:
                callbackToClient(new CallbackStartVideoRecordingTask());
                if (nativeIsDebugable()) {
                    Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_START_VIDEO_RECORDING_DONE");
                    break;
                }
                break;
            case 1:
                callbackToClient(new CallbackStopVideoRecordingTask());
                if (nativeIsDebugable()) {
                    Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_STOP_VIDEO_RECORDING_DONE");
                    break;
                }
                break;
            case 2:
                callbackToClient(new CallbackPrepareVideoRecordingTask());
                if (nativeIsDebugable()) {
                    Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_PREPARE_VIDEO_RECORDING_DONE");
                    break;
                }
                break;
            case 3:
                callbackToClient(new CallbackStartSuperSlowRecordingTask());
                if (nativeIsDebugable()) {
                    Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_START_SUPER_SLOW_RECORDING_DONE");
                    break;
                }
                break;
            case 4:
                callbackToClient(new CallbackPrepareSuperSlowRecordingTask());
                if (nativeIsDebugable()) {
                    Log.e(TAG, "TYPE = CALLBACK_TYPE_ON_PREPARE_SUPER_SLOW_RECORDING_DONE");
                    break;
                }
                break;
        }
        if (nativeIsDebugable()) {
            Log.e(TAG, "callbackFromNativeVideo() : X");
        }
    }

    private class CallbackPrepareVideoRecordingTask implements Runnable {
        private CallbackPrepareVideoRecordingTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mPrepareVideoRecordingCallback != null) {
                    BypassCamera.this.mPrepareVideoRecordingCallback.onPrepareVideoRecordingDone();
                }
            }
        }
    }

    private class CallbackStartVideoRecordingTask implements Runnable {
        private CallbackStartVideoRecordingTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mStartVideoRecordingCallback != null) {
                    BypassCamera.this.mStartVideoRecordingCallback.onStartVideoRecordingDone();
                }
            }
        }
    }

    private class CallbackStopVideoRecordingTask implements Runnable {
        private CallbackStopVideoRecordingTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mStopVideoRecordingCallback != null) {
                    BypassCamera.this.mStopVideoRecordingCallback.onStopVideoRecordingDone();
                }
            }
        }
    }

    private class CallbackPrepareSuperSlowRecordingTask implements Runnable {
        private CallbackPrepareSuperSlowRecordingTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mPrepareSuperSlowRecordingCallback != null) {
                    BypassCamera.this.mPrepareSuperSlowRecordingCallback.onPrepareSuperSlowRecordingDone();
                }
            }
        }
    }

    private class CallbackStartSuperSlowRecordingTask implements Runnable {
        private CallbackStartSuperSlowRecordingTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (BypassCamera.this) {
                if (BypassCamera.this.mStartSuperSlowRecordingCallback != null) {
                    BypassCamera.this.mStartSuperSlowRecordingCallback.onStartSuperSlowRecordingDone();
                }
            }
        }
    }
}
