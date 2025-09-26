// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.imageprocessor.bypasscamera2;

import android.util.Size;
import android.view.Surface;
import android.os.Build;
import java.util.Iterator;
import java.util.List;
import android.util.Log;
import android.os.Handler;
import java.util.concurrent.ExecutorService;

public final class BypassCamera
{
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
    private static final Object sBypassCameraLock;
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
    
    static {
        sBypassCameraLock = new Object();
    }
    
    private BypassCamera(final Facing facing) {
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
        if (this.nativeIsDebugable()) {
            Log.e("BypassCamera", "CONSTRUCTOR : E");
        }
        this.mNativeInstancePointer = this.nativeInitialize(facing.getCameraId());
        if (this.mNativeInstancePointer == -1L) {
            throw new RuntimeException("Failed to nativeInitialize().");
        }
        if (this.nativeIsDebugable()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("NativeInstanceHead=");
            sb.append(this.mNativeInstancePointer);
            Log.e("BypassCamera", sb.toString());
        }
        if (this.nativeIsDebugable()) {
            Log.e("BypassCamera", "CONSTRUCTOR : X");
        }
    }
    
    private BypassCamera(final Facing facing, final Handler mCallbackHandler) {
        this(facing);
        this.mCallbackHandler = mCallbackHandler;
    }
    
    private BypassCamera(final Facing facing, final ExecutorService mCallbackExecutorService) {
        this(facing);
        this.mCallbackExecutorService = mCallbackExecutorService;
    }
    
    private final void callbackFromNative(final int n, final int n2, final int n3, final boolean b, final boolean b2, final boolean b3, final int n4, final int n5, final int n6) {
        if (this.nativeIsDebugable()) {
            Log.e("BypassCamera", "callbackFromNative() : E");
        }
        if (n != 10) {
            if (n != 30) {
                if (n != 40) {
                    if (n != 50) {
                        switch (n) {
                            case 21: {
                                if (this.nativeIsDebugable()) {
                                    Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_SNAPSHOT_DONE");
                                }
                                this.callbackToClient(new CallbackSnapshotTask(n2));
                                break;
                            }
                            case 20: {
                                if (this.nativeIsDebugable()) {
                                    Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_SHUTTER_DONE");
                                }
                                this.callbackToClient(new CallbackShutterTask(n2, n3, b));
                                break;
                            }
                        }
                    }
                    else {
                        if (this.nativeIsDebugable()) {
                            Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_FINISH_BURST_DONE");
                        }
                        this.callbackToClient(new CallbackFinishBurstTask());
                    }
                }
                else {
                    if (this.nativeIsDebugable()) {
                        Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_PREPARE_BURST_DONE");
                    }
                    this.callbackToClient(new CallbackPrepareBurstTask(b2));
                }
            }
            else {
                if (this.nativeIsDebugable()) {
                    Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_SNAPSHOT_FREE_DONE");
                }
                this.callbackToClient(new CallbackSnapshotFreeTask());
            }
        }
        else {
            if (this.nativeIsDebugable()) {
                Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_SNAPSHOT_READY_DONE");
            }
            this.callbackToClient(new CallbackSnapshotReadyTask(b2, b, b3, n4, n5, n6));
        }
        if (this.nativeIsDebugable()) {
            Log.e("BypassCamera", "callbackFromNative() : X");
        }
    }
    
    private final void callbackFromNativeVideo(final int n) {
        if (this.nativeIsDebugable()) {
            Log.e("BypassCamera", "callbackFromNativeVideo() : E");
        }
        switch (n) {
            case 4: {
                this.callbackToClient(new CallbackPrepareSuperSlowRecordingTask());
                if (this.nativeIsDebugable()) {
                    Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_PREPARE_SUPER_SLOW_RECORDING_DONE");
                    break;
                }
                break;
            }
            case 3: {
                this.callbackToClient(new CallbackStartSuperSlowRecordingTask());
                if (this.nativeIsDebugable()) {
                    Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_START_SUPER_SLOW_RECORDING_DONE");
                    break;
                }
                break;
            }
            case 2: {
                this.callbackToClient(new CallbackPrepareVideoRecordingTask());
                if (this.nativeIsDebugable()) {
                    Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_PREPARE_VIDEO_RECORDING_DONE");
                    break;
                }
                break;
            }
            case 1: {
                this.callbackToClient(new CallbackStopVideoRecordingTask());
                if (this.nativeIsDebugable()) {
                    Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_STOP_VIDEO_RECORDING_DONE");
                    break;
                }
                break;
            }
            case 0: {
                this.callbackToClient(new CallbackStartVideoRecordingTask());
                if (this.nativeIsDebugable()) {
                    Log.e("BypassCamera", "TYPE = CALLBACK_TYPE_ON_START_VIDEO_RECORDING_DONE");
                    break;
                }
                break;
            }
        }
        if (this.nativeIsDebugable()) {
            Log.e("BypassCamera", "callbackFromNativeVideo() : X");
        }
    }
    
    private void callbackToClient(final Runnable runnable) {
        if (this.mCallbackExecutorService == null && this.mCallbackHandler == null) {
            Log.e("BypassCamera", "callbackToClient() : Callback object is not installed.");
        }
        else if (this.mCallbackExecutorService != null && !this.mCallbackExecutorService.isShutdown()) {
            this.mCallbackExecutorService.execute(runnable);
        }
        else if (this.mCallbackHandler != null) {
            this.mCallbackHandler.post(runnable);
        }
        else {
            Log.w("BypassCamera", "callbackToClient() : Callback is not invoked.");
        }
    }
    
    private static String createListValue(final List<String> list) {
        if (list.isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final String str : list) {
            if (str != list.get(0)) {
                sb.append(',');
            }
            sb.append(str);
        }
        return sb.toString();
    }
    
    private static String createSuperSlowSupportedInfoListValue(final List<BypassCameraParameters.Capability.SupportedInfo> list, final List<Integer> list2) {
        if (list.isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            final BypassCameraParameters.Capability.SupportedInfo supportedInfo = list.get(i);
            final int intValue = list2.get(i);
            if (i > 0) {
                sb.append(',');
            }
            sb.append(supportedInfo.width);
            sb.append('x');
            sb.append(supportedInfo.height);
            sb.append('@');
            sb.append(intValue);
            sb.append('/');
            sb.append(supportedInfo.fps);
        }
        return sb.toString();
    }
    
    private static String createSupportedInfoListValue(final List<BypassCameraParameters.Capability.SupportedInfo> list) {
        if (list.isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final BypassCameraParameters.Capability.SupportedInfo supportedInfo : list) {
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
    
    public static BypassCameraParameters getCaps(final Facing facing) {
        loadNativeLibrary();
        final BypassCameraParameters.Capability capability = new BypassCameraParameters.Capability();
        if (nativeGetCaps(facing.getCameraId(), capability) != 0) {
            throw new RuntimeException("Failed to nativeGetCaps().");
        }
        final BypassCameraParameters bypassCameraParameters = new BypassCameraParameters();
        bypassCameraParameters.set("vs-values", createListValue(capability.mVideoStabilizationMode));
        bypassCameraParameters.set("video-high-frame-rate-configuration", createSupportedInfoListValue(capability.mHighFrameRateSupportedInfoList));
        bypassCameraParameters.set("vs-steady-shot-configuration", createSupportedInfoListValue(capability.mSteadyShotSupportedInfoList));
        bypassCameraParameters.set("vs-intelligent-active-configuration", createSupportedInfoListValue(capability.mIntelligentActiveSupportedInfoList));
        bypassCameraParameters.set("super-slow-values", createListValue(capability.mSuperSlowMode));
        bypassCameraParameters.set("burst-values", createListValue(capability.mBurstMode));
        bypassCameraParameters.set("video-hdr-values", createListValue(capability.mVideoHdrMode));
        bypassCameraParameters.set("video-super-slow-configuration", createSuperSlowSupportedInfoListValue(capability.mSuperSlowSupportedInfoList, capability.mSuperSlowFrameNumList));
        bypassCameraParameters.set("climax-recognition-values", createListValue(capability.mClimaxRecognitionMode));
        return bypassCameraParameters;
    }
    
    public static void loadNativeLibrary() {
        synchronized (BypassCamera.sBypassCameraLock) {
            if (!BypassCamera.sIsBypassCameraLoaded) {
                final boolean b = Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug");
                if (b) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("[CamKPI] [UPTIME=] [BypassCamera][");
                    sb.append(Thread.currentThread().getName());
                    sb.append("] : loadLibrary() : E");
                    Log.e("TraceLog", sb.toString());
                }
                System.loadLibrary("imageprocessorjni");
                if (b) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("[CamKPI] [UPTIME=] [BypassCamera][");
                    sb2.append(Thread.currentThread().getName());
                    sb2.append("] : loadLibrary() : X");
                    Log.e("TraceLog", sb2.toString());
                }
                BypassCamera.sIsBypassCameraLoaded = true;
            }
        }
    }
    
    private native int nativeChangeToPhotoMode(final long p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    private native int nativeChangeToSuperSlowMode(final long p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8);
    
    private native int nativeChangeToVideoMode(final long p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    private native int nativeFinalize(final long p0);
    
    private static native int nativeGetCaps(final int p0, final BypassCameraParameters.Capability p1);
    
    private native int nativeGetLatestRequestId(final long p0);
    
    private native long nativeInitialize(final int p0);
    
    private native boolean nativeIsDebugable();
    
    private native int nativeRequestFinishBurstShot(final long p0);
    
    private native int nativeRequestPrepareBurstShot(final long p0);
    
    private native int nativeRequestPrepareSnapshot(final long p0, final Surface p1, final int p2);
    
    private native int nativeRequestPrepareSuperSlowRecording(final long p0, final Surface p1, final int p2, final int p3, final int p4);
    
    private native int nativeRequestPrepareVideoRecording(final long p0, final Surface p1, final int p2, final int p3, final int p4);
    
    private native int nativeRequestSnapshot(final long p0, final boolean p1, final boolean p2, final double p3, final double p4, final double p5, final boolean p6, final String p7, final boolean p8, final int p9, final boolean p10, final int p11, final boolean p12, final int p13, final int p14, final int p15, final int p16, final boolean p17, final int p18);
    
    private native int nativeRequestSnapshotFree(final long p0);
    
    private native int nativeRequestSnapshotReady(final long p0);
    
    private native int nativeRequestStartSuperSlowRecording(final long p0);
    
    private native int nativeRequestStartVideoRecording(final long p0);
    
    private native int nativeRequestStopVideoRecording(final long p0);
    
    private native int nativeSetConfig(final long p0, final BypassCameraParameters p1);
    
    public static BypassCamera open(final Facing facing, final Handler handler) {
        loadNativeLibrary();
        synchronized (BypassCamera.sBypassCameraLock) {
            if (!BypassCamera.sIsBypassCameraOpened) {
                final BypassCamera bypassCamera = new BypassCamera(facing, handler);
                BypassCamera.sIsBypassCameraOpened = true;
                return bypassCamera;
            }
            throw new RuntimeException("BypassCamera is already opened.");
        }
    }
    
    public static BypassCamera open(final Facing facing, final ExecutorService executorService) {
        loadNativeLibrary();
        synchronized (BypassCamera.sBypassCameraLock) {
            if (!BypassCamera.sIsBypassCameraOpened) {
                final BypassCamera bypassCamera = new BypassCamera(facing, executorService);
                BypassCamera.sIsBypassCameraOpened = true;
                return bypassCamera;
            }
            throw new RuntimeException("BypassCamera is already opened.");
        }
    }
    
    public void changeToPhotoMode(final PhotoMode photoMode, final Size size, final Size size2, final int n) throws BypassCameraTimeoutException {
        switch (this.nativeChangeToPhotoMode(this.mNativeInstancePointer, photoMode.getModeCode(), size.getWidth(), size.getHeight(), size2.getWidth(), size2.getHeight(), n)) {
            default: {
                return;
            }
            case -1: {
                this.mNativeInstancePointer = -1L;
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to change mode to : ");
                sb.append(photoMode);
                throw new RuntimeException(sb.toString());
            }
            case -2: {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Timed out to change mode to : ");
                sb2.append(photoMode);
                throw new BypassCameraTimeoutException(sb2.toString());
            }
        }
    }
    
    public void changeToSuperSlowMode(final SuperSlowMode superSlowMode, final Size size, final Size size2, final int n, final SuperSlowRecordingParameters superSlowRecordingParameters) throws BypassCameraTimeoutException {
        switch (this.nativeChangeToSuperSlowMode(this.mNativeInstancePointer, superSlowMode.getModeCode(), size.getWidth(), size.getHeight(), size2.getWidth(), size2.getHeight(), n, superSlowRecordingParameters.fps, superSlowRecordingParameters.frameNum)) {
            default: {
                return;
            }
            case -1: {
                this.mNativeInstancePointer = -1L;
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to change mode to : ");
                sb.append(superSlowMode);
                throw new RuntimeException(sb.toString());
            }
            case -2: {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Timed out to change mode to : ");
                sb2.append(superSlowMode);
                throw new BypassCameraTimeoutException(sb2.toString());
            }
        }
    }
    
    public void changeToSuperSlowMode(final SuperSlowMode superSlowMode, final Size size, final Size size2, final SuperSlowRecordingParameters superSlowRecordingParameters) {
        throw new RuntimeException("Not implemented.");
    }
    
    public void changeToVideoMode(final VideoMode videoMode, final Size size, final Size size2, final int n) throws BypassCameraTimeoutException {
        switch (this.nativeChangeToVideoMode(this.mNativeInstancePointer, videoMode.getModeCode(), size.getWidth(), size.getHeight(), size2.getWidth(), size2.getHeight(), n)) {
            default: {
                return;
            }
            case -1: {
                this.mNativeInstancePointer = -1L;
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to change mode to : ");
                sb.append(videoMode);
                throw new RuntimeException(sb.toString());
            }
            case -2: {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Timed out to change mode to : ");
                sb2.append(videoMode);
                throw new BypassCameraTimeoutException(sb2.toString());
            }
        }
    }
    
    public void close() {
        if (this.nativeIsDebugable()) {
            Log.e("BypassCamera", "close() : E");
        }
        synchronized (this) {
            this.mSnapshotReadyCallback = null;
            this.mSnapshotCallback = null;
            this.mSnapshotFreeCallback = null;
            this.mStartVideoRecordingCallback = null;
            this.mStopVideoRecordingCallback = null;
            this.mPrepareBurstCallback = null;
            this.mFinishBurstCallback = null;
            monitorexit(this);
            this.nativeFinalize(this.mNativeInstancePointer);
            if (this.nativeIsDebugable()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("NativeInstanceHead=");
                sb.append(this.mNativeInstancePointer);
                Log.e("BypassCamera", sb.toString());
            }
            this.mNativeInstancePointer = -1L;
            synchronized (BypassCamera.sBypassCameraLock) {
                BypassCamera.sIsBypassCameraOpened = false;
                monitorexit(BypassCamera.sBypassCameraLock);
                if (this.nativeIsDebugable()) {
                    Log.e("BypassCamera", "close() : X");
                }
            }
        }
    }
    
    public void requestFinishBurstShot() {
        if (this.nativeRequestFinishBurstShot(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestFinishBurstShot().");
        }
    }
    
    public void requestPrepareBurstShot() {
        if (this.nativeRequestPrepareBurstShot(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestPrepareBurstShot().");
        }
    }
    
    public void requestPrepareSnapshot(final Surface surface, final int n) {
        if (this.nativeRequestPrepareSnapshot(this.mNativeInstancePointer, surface, n) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to requestPrepareSnapshot().");
        }
    }
    
    public void requestPrepareSuperSlowRecording(final Surface surface, final RecordingParameters recordingParameters) {
        if (this.nativeRequestPrepareSuperSlowRecording(this.mNativeInstancePointer, surface, recordingParameters.dataSpace.colorStandard, recordingParameters.dataSpace.colorTransfer, recordingParameters.dataSpace.colorRange) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestPrepareSuperSlowRecording().");
        }
    }
    
    public void requestPrepareVideoRecording(final Surface surface, final RecordingParameters recordingParameters) {
        if (this.nativeRequestPrepareVideoRecording(this.mNativeInstancePointer, surface, recordingParameters.dataSpace.colorStandard, recordingParameters.dataSpace.colorTransfer, recordingParameters.dataSpace.colorRange) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestPrepareVideoRecording().");
        }
    }
    
    public int requestSnapshot(final SnapshotInfo snapshotInfo) {
        if (this.nativeRequestSnapshot(this.mNativeInstancePointer, snapshotInfo.isValid, snapshotInfo.exifGpsInfo.isExifGpsEnabled, snapshotInfo.exifGpsInfo.exifGpsLatitude, snapshotInfo.exifGpsInfo.exifGpsLongitude, snapshotInfo.exifGpsInfo.exifGpsAltitude, snapshotInfo.exifGpsInfo.isExifGpsProcMethodEnabled, snapshotInfo.exifGpsInfo.exifGpsProcMethod, snapshotInfo.exifGpsInfo.isExifGpsTimestampEnabled, snapshotInfo.exifGpsInfo.exifGpsTimestamp, snapshotInfo.exifOrientationInfo.isExifOrientationEnabled, snapshotInfo.exifOrientationInfo.exifOrientation, snapshotInfo.exifThumbnailInfo.isExifThumbEnabled, snapshotInfo.exifThumbnailInfo.exifThumbWidth, snapshotInfo.exifThumbnailInfo.exifThumbHeight, snapshotInfo.exifThumbnailInfo.exifThumbQuality, snapshotInfo.quality, snapshotInfo.isQualityAutoControlEnabled, snapshotInfo.captureNum) != 0) {
            throw new RuntimeException("Failed to nativeRequestSnapshot().");
        }
        return this.nativeGetLatestRequestId(this.mNativeInstancePointer);
    }
    
    public void requestSnapshotFree() {
        if (this.nativeRequestSnapshotFree(this.mNativeInstancePointer) != 0) {
            throw new RuntimeException("Failed to nativeRequestSnapshotFree().");
        }
    }
    
    public void requestSnapshotReady() {
        if (this.nativeRequestSnapshotReady(this.mNativeInstancePointer) != 0) {
            throw new RuntimeException("Failed to nativeRequestSnapshotReady().");
        }
    }
    
    public void requestStartSuperSlowRecording() {
        if (this.nativeRequestStartSuperSlowRecording(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestStartSuperSlowRecording().");
        }
    }
    
    public void requestStartVideoRecording() {
        if (this.nativeRequestStartVideoRecording(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestStartVideoRecording().");
        }
    }
    
    public void requestStopVideoRecording() {
        if (this.nativeRequestStopVideoRecording(this.mNativeInstancePointer) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeRequestStopVideoRecording().");
        }
    }
    
    public void setBurstCallbacks(final PrepareBurstCallback mPrepareBurstCallback, final FinishBurstCallback mFinishBurstCallback) {
        this.mPrepareBurstCallback = mPrepareBurstCallback;
        this.mFinishBurstCallback = mFinishBurstCallback;
    }
    
    public void setConfig(final BypassCameraParameters bypassCameraParameters) {
        if (this.nativeSetConfig(this.mNativeInstancePointer, bypassCameraParameters) != 0) {
            this.mNativeInstancePointer = -1L;
            throw new RuntimeException("Failed to nativeSetConfig().");
        }
    }
    
    public void setPhotoCallbacks(final SnapshotReadyCallback mSnapshotReadyCallback, final SnapshotCallback mSnapshotCallback, final SnapshotFreeCallback mSnapshotFreeCallback) {
        this.mSnapshotReadyCallback = mSnapshotReadyCallback;
        this.mSnapshotCallback = mSnapshotCallback;
        this.mSnapshotFreeCallback = mSnapshotFreeCallback;
    }
    
    public void setSuperSlowCallbacks(final PrepareSuperSlowRecordingCallback mPrepareSuperSlowRecordingCallback, final StartSuperSlowRecordingCallback mStartSuperSlowRecordingCallback) {
        this.mPrepareSuperSlowRecordingCallback = mPrepareSuperSlowRecordingCallback;
        this.mStartSuperSlowRecordingCallback = mStartSuperSlowRecordingCallback;
    }
    
    public void setVideoCallbacks(final PrepareVideoRecordingCallback mPrepareVideoRecordingCallback, final StartVideoRecordingCallback mStartVideoRecordingCallback, final StopVideoRecordingCallback mStopVideoRecordingCallback) {
        this.mPrepareVideoRecordingCallback = mPrepareVideoRecordingCallback;
        this.mStartVideoRecordingCallback = mStartVideoRecordingCallback;
        this.mStopVideoRecordingCallback = mStopVideoRecordingCallback;
    }
    
    private class CallbackFinishBurstTask implements Runnable
    {
        final BypassCamera this$0;
        
        private CallbackFinishBurstTask(final BypassCamera this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mFinishBurstCallback != null) {
                    this.this$0.mFinishBurstCallback.onFinishBurstDone();
                }
            }
        }
    }
    
    private class CallbackPrepareBurstTask implements Runnable
    {
        private final boolean mIsSuccess;
        final BypassCamera this$0;
        
        public CallbackPrepareBurstTask(final BypassCamera this$0, final boolean mIsSuccess) {
            this.this$0 = this$0;
            this.mIsSuccess = mIsSuccess;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mPrepareBurstCallback != null) {
                    this.this$0.mPrepareBurstCallback.onPrepareBurstDone(this.mIsSuccess);
                }
            }
        }
    }
    
    private class CallbackPrepareSuperSlowRecordingTask implements Runnable
    {
        final BypassCamera this$0;
        
        private CallbackPrepareSuperSlowRecordingTask(final BypassCamera this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mPrepareSuperSlowRecordingCallback != null) {
                    this.this$0.mPrepareSuperSlowRecordingCallback.onPrepareSuperSlowRecordingDone();
                }
            }
        }
    }
    
    private class CallbackPrepareVideoRecordingTask implements Runnable
    {
        final BypassCamera this$0;
        
        private CallbackPrepareVideoRecordingTask(final BypassCamera this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mPrepareVideoRecordingCallback != null) {
                    this.this$0.mPrepareVideoRecordingCallback.onPrepareVideoRecordingDone();
                }
            }
        }
    }
    
    private class CallbackShutterTask implements Runnable
    {
        private final int mCaptureNum;
        private final boolean mIsAfSuccessed;
        private final int mRequestId;
        final BypassCamera this$0;
        
        public CallbackShutterTask(final BypassCamera this$0, final int mRequestId, final int mCaptureNum, final boolean mIsAfSuccessed) {
            this.this$0 = this$0;
            this.mRequestId = mRequestId;
            this.mCaptureNum = mCaptureNum;
            this.mIsAfSuccessed = mIsAfSuccessed;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mSnapshotCallback != null) {
                    this.this$0.mSnapshotCallback.onShutterDone(this.mRequestId, this.mCaptureNum, this.mIsAfSuccessed);
                }
            }
        }
    }
    
    private class CallbackSnapshotFreeTask implements Runnable
    {
        final BypassCamera this$0;
        
        private CallbackSnapshotFreeTask(final BypassCamera this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mSnapshotFreeCallback != null) {
                    this.this$0.mSnapshotFreeCallback.onSnapshotFreeDone();
                }
            }
        }
    }
    
    private class CallbackSnapshotReadyTask implements Runnable
    {
        private final DisplayFlashColor mDisplayFlashColor;
        private final boolean mIsAfSuccessed;
        private final boolean mIsHighQualityBurstAvailable;
        private final boolean mRequireDisplayFlash;
        final BypassCamera this$0;
        
        public CallbackSnapshotReadyTask(final BypassCamera this$0, final boolean mIsHighQualityBurstAvailable, final boolean mIsAfSuccessed, final boolean mRequireDisplayFlash, final int n, final int n2, final int n3) {
            this.this$0 = this$0;
            this.mIsHighQualityBurstAvailable = mIsHighQualityBurstAvailable;
            this.mIsAfSuccessed = mIsAfSuccessed;
            this.mRequireDisplayFlash = mRequireDisplayFlash;
            this.mDisplayFlashColor = new DisplayFlashColor(n, n2, n3);
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mSnapshotReadyCallback != null) {
                    this.this$0.mSnapshotReadyCallback.onSnapshotReadyDone(this.mIsHighQualityBurstAvailable, this.mIsAfSuccessed, this.mRequireDisplayFlash, this.mDisplayFlashColor);
                }
            }
        }
    }
    
    private class CallbackSnapshotTask implements Runnable
    {
        private final int mRequestId;
        final BypassCamera this$0;
        
        public CallbackSnapshotTask(final BypassCamera this$0, final int mRequestId) {
            this.this$0 = this$0;
            this.mRequestId = mRequestId;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mSnapshotCallback != null) {
                    this.this$0.mSnapshotCallback.onSnapshotDone(this.mRequestId);
                }
            }
        }
    }
    
    private class CallbackStartSuperSlowRecordingTask implements Runnable
    {
        final BypassCamera this$0;
        
        private CallbackStartSuperSlowRecordingTask(final BypassCamera this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mStartSuperSlowRecordingCallback != null) {
                    this.this$0.mStartSuperSlowRecordingCallback.onStartSuperSlowRecordingDone();
                }
            }
        }
    }
    
    private class CallbackStartVideoRecordingTask implements Runnable
    {
        final BypassCamera this$0;
        
        private CallbackStartVideoRecordingTask(final BypassCamera this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mStartVideoRecordingCallback != null) {
                    this.this$0.mStartVideoRecordingCallback.onStartVideoRecordingDone();
                }
            }
        }
    }
    
    private class CallbackStopVideoRecordingTask implements Runnable
    {
        final BypassCamera this$0;
        
        private CallbackStopVideoRecordingTask(final BypassCamera this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0) {
                if (this.this$0.mStopVideoRecordingCallback != null) {
                    this.this$0.mStopVideoRecordingCallback.onStopVideoRecordingDone();
                }
            }
        }
    }
    
    public static class DataSpace
    {
        public final int colorRange;
        public final int colorStandard;
        public final int colorTransfer;
        
        public DataSpace(final int colorStandard, final int colorTransfer, final int colorRange) {
            this.colorStandard = colorStandard;
            this.colorTransfer = colorTransfer;
            this.colorRange = colorRange;
        }
    }
    
    public static class DisplayFlashColor
    {
        public final int colorBlue;
        public final int colorGreen;
        public final int colorRed;
        
        public DisplayFlashColor(final int colorRed, final int colorGreen, final int colorBlue) {
            this.colorRed = colorRed;
            this.colorGreen = colorGreen;
            this.colorBlue = colorBlue;
        }
    }
    
    public enum Facing
    {
        private static final Facing[] $VALUES;
        
        BACK(0), 
        FRONT(1);
        
        private final int mCameraId;
        
        static {
            $VALUES = new Facing[] { Facing.BACK, Facing.FRONT };
        }
        
        private Facing(final int mCameraId) {
            this.mCameraId = mCameraId;
        }
        
        private int getCameraId() {
            return this.mCameraId;
        }
    }
    
    public interface FinishBurstCallback
    {
        void onFinishBurstDone();
    }
    
    public enum PhotoMode
    {
        private static final PhotoMode[] $VALUES;
        
        NORMAL(0);
        
        private final int mModeCode;
        
        static {
            $VALUES = new PhotoMode[] { PhotoMode.NORMAL };
        }
        
        private PhotoMode(final int mModeCode) {
            this.mModeCode = mModeCode;
        }
        
        private int getModeCode() {
            return this.mModeCode;
        }
    }
    
    public interface PrepareBurstCallback
    {
        void onPrepareBurstDone(final boolean p0);
    }
    
    public interface PrepareSuperSlowRecordingCallback
    {
        void onPrepareSuperSlowRecordingDone();
    }
    
    public interface PrepareVideoRecordingCallback
    {
        void onPrepareVideoRecordingDone();
    }
    
    public static class RecordingParameters
    {
        public final DataSpace dataSpace;
        
        public RecordingParameters(final DataSpace dataSpace) {
            this.dataSpace = dataSpace;
        }
    }
    
    public interface SnapshotCallback
    {
        void onShutterDone(final int p0, final int p1, final boolean p2);
        
        void onSnapshotDone(final int p0);
    }
    
    public interface SnapshotFreeCallback
    {
        void onSnapshotFreeDone();
    }
    
    public static class SnapshotInfo
    {
        public final int captureNum;
        public final ExifGpsInfo exifGpsInfo;
        public final ExifOrientationInfo exifOrientationInfo;
        public final ExifThumbnailInfo exifThumbnailInfo;
        public final boolean isQualityAutoControlEnabled;
        public final boolean isValid;
        public final int quality;
        
        public SnapshotInfo(final boolean isValid, final ExifGpsInfo exifGpsInfo, final ExifOrientationInfo exifOrientationInfo, final ExifThumbnailInfo exifThumbnailInfo, final int quality, final boolean isQualityAutoControlEnabled, final int captureNum) {
            this.isValid = isValid;
            this.exifGpsInfo = exifGpsInfo;
            this.exifOrientationInfo = exifOrientationInfo;
            this.exifThumbnailInfo = exifThumbnailInfo;
            this.quality = quality;
            this.isQualityAutoControlEnabled = isQualityAutoControlEnabled;
            this.captureNum = captureNum;
        }
        
        public static class ExifGpsInfo
        {
            public final double exifGpsAltitude;
            public final double exifGpsLatitude;
            public final double exifGpsLongitude;
            public final String exifGpsProcMethod;
            public final int exifGpsTimestamp;
            public final boolean isExifGpsEnabled;
            public final boolean isExifGpsProcMethodEnabled;
            public final boolean isExifGpsTimestampEnabled;
            
            public ExifGpsInfo(final boolean isExifGpsEnabled, final double exifGpsLatitude, final double exifGpsLongitude, final double exifGpsAltitude, final boolean isExifGpsProcMethodEnabled, final String exifGpsProcMethod, final boolean isExifGpsTimestampEnabled, final int exifGpsTimestamp) {
                this.isExifGpsEnabled = isExifGpsEnabled;
                this.exifGpsLatitude = exifGpsLatitude;
                this.exifGpsLongitude = exifGpsLongitude;
                this.exifGpsAltitude = exifGpsAltitude;
                this.isExifGpsProcMethodEnabled = isExifGpsProcMethodEnabled;
                this.exifGpsProcMethod = exifGpsProcMethod;
                this.isExifGpsTimestampEnabled = isExifGpsTimestampEnabled;
                this.exifGpsTimestamp = exifGpsTimestamp;
            }
        }
        
        public static class ExifOrientationInfo
        {
            public final int exifOrientation;
            public final boolean isExifOrientationEnabled;
            
            public ExifOrientationInfo(final boolean isExifOrientationEnabled, final int exifOrientation) {
                this.isExifOrientationEnabled = isExifOrientationEnabled;
                this.exifOrientation = exifOrientation;
            }
        }
        
        public static class ExifThumbnailInfo
        {
            public final int exifThumbHeight;
            public final int exifThumbQuality;
            public final int exifThumbWidth;
            public final boolean isExifThumbEnabled;
            
            public ExifThumbnailInfo(final boolean isExifThumbEnabled, final int exifThumbWidth, final int exifThumbHeight, final int exifThumbQuality) {
                this.isExifThumbEnabled = isExifThumbEnabled;
                this.exifThumbWidth = exifThumbWidth;
                this.exifThumbHeight = exifThumbHeight;
                this.exifThumbQuality = exifThumbQuality;
            }
        }
    }
    
    public interface SnapshotReadyCallback
    {
        void onSnapshotReadyDone(final boolean p0, final boolean p1, final boolean p2, final DisplayFlashColor p3);
    }
    
    public interface StartSuperSlowRecordingCallback
    {
        void onStartSuperSlowRecordingDone();
    }
    
    public interface StartVideoRecordingCallback
    {
        void onStartVideoRecordingDone();
    }
    
    public interface StopVideoRecordingCallback
    {
        void onStopVideoRecordingDone();
    }
    
    public enum SuperSlowMode
    {
        private static final SuperSlowMode[] $VALUES;
        
        SUPER_SLOW_MOTION(0), 
        SUPER_SLOW_SHOT(1);
        
        private final int mModeCode;
        
        static {
            $VALUES = new SuperSlowMode[] { SuperSlowMode.SUPER_SLOW_MOTION, SuperSlowMode.SUPER_SLOW_SHOT };
        }
        
        private SuperSlowMode(final int mModeCode) {
            this.mModeCode = mModeCode;
        }
        
        private int getModeCode() {
            return this.mModeCode;
        }
    }
    
    public static class SuperSlowRecordingParameters
    {
        public final int fps;
        public final int frameNum;
        
        public SuperSlowRecordingParameters(final int fps, final int frameNum) {
            this.fps = fps;
            this.frameNum = frameNum;
        }
    }
    
    public enum VideoMode
    {
        private static final VideoMode[] $VALUES;
        
        HDR(3), 
        HDR_STEADYSHOT(4), 
        INTELLIGENTACTIVE(2), 
        NORMAL(0), 
        STEADYSHOT(1);
        
        private final int mModeCode;
        
        static {
            $VALUES = new VideoMode[] { VideoMode.NORMAL, VideoMode.STEADYSHOT, VideoMode.INTELLIGENTACTIVE, VideoMode.HDR, VideoMode.HDR_STEADYSHOT };
        }
        
        private VideoMode(final int mModeCode) {
            this.mModeCode = mModeCode;
        }
        
        private int getModeCode() {
            return this.mModeCode;
        }
    }
}
