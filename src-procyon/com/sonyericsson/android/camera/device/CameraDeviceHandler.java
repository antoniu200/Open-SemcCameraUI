// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import com.sonyericsson.cameracommon.status.CameraStatusPublisher;
import com.sonyericsson.cameracommon.mediasaving.location.LocationSettingsReader;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.view.OrientationEventListener;
import android.util.Printer;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import android.support.annotation.WorkerThread;
import java.nio.ByteBuffer;
import com.sonymobile.cameracommon.testevent.TestEventSender;
import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CapturePerformanceLogger;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonymobile.cameracommon.research.parameters.Event;
import java.util.concurrent.ExecutorService;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import com.sonyericsson.cameracommon.status.eachcamera.DeviceStatus;
import java.util.Optional;
import com.sonyericsson.android.camera.recorder.superslowrecorder.SuperSlowRecorderController;
import java.io.FileNotFoundException;
import android.provider.DocumentsContract;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import android.view.Surface;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.cameracommon.status.eachcamera.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.setting.LastSettings;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import android.media.CamcorderProfile;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.cameracommon.utility.RecordingUtil;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import com.sonyericsson.android.camera.recorder.utility.Accessor;
import com.sonyericsson.android.camera.recorder.RecorderFactory;
import android.net.Uri;
import java.io.File;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.recorder.superslowrecorder.OnSuperSlowRecordingFinishedListener;
import android.content.ComponentName;
import android.app.admin.DevicePolicyManager;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.recorder.RecorderException;
import android.os.BatteryManager;
import com.sonyericsson.cameracommon.status.eachcamera.SlowMotion;
import com.sonyericsson.cameracommon.status.EachCameraStatusPublisher;
import java.util.concurrent.CancellationException;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import java.util.List;
import java.util.ArrayList;
import android.graphics.Rect;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import android.location.Location;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.HandlerThread;
import com.sonyericsson.android.camera.CameraApplication;
import android.util.ArrayMap;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonyericsson.android.camera.controller.StateMachine;
import android.content.SharedPreferences;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import android.os.Handler;
import android.content.Context;
import java.util.Map;

public class CameraDeviceHandler
{
    static final long CLOSE_BYPASS_CAMERA_TIMEOUT_MILLIS = 100000L;
    private static final long DELAY_STATUS_PROVIDER_UPDATE_UNTIL_RECORDING_STARTED_MILLIS = 3000L;
    private static final long TIMEOUT_WAIT_FOR_PARAMETERS_TO_BE_REFLECTED_MILLIS = 5000L;
    private static final Map<CameraSessionId, CameraSessionInfo> sCameraSessionInfoMap;
    private static final Object sSendPauseEventAndReleaseCameraTaskToken;
    private boolean mActivityIsInForeground;
    private Context mApplicationContext;
    private BypassCameraController mBypassCameraController;
    private CameraActionSound mCameraActionSound;
    private CameraController mCameraController;
    private final Handler mCameraDeviceThreadHandler;
    private CameraSessionId mCameraSessionId;
    private final Runnable mChangeProviderDeviceStatusToRecordingTask;
    private CloseBypassCameraTimeoutTask mCloseBypassCameraTimeoutTask;
    private FastCaptureOrientation mFastCaptureOrientation;
    private FastCapture mFastCaptureSetting;
    private GeotagManager mGeotagManager;
    private final CameraDeviceAccessTask mInitControllerTask;
    private boolean mIsCameraDisabled;
    private boolean mIsFpsLimitationEnabled;
    private boolean mIsInShutdownNow;
    private Boolean mIsRecording;
    private boolean mIsVideo;
    private RequestFactory.VideoSavingRequestBuilder mLastVideoSavingRequest;
    private LoadSettingsThread mLoadSettingsThread;
    private PreProcessState mPreProcessState;
    private SharedPreferences mPreferences;
    private StateMachine mStateMachine;
    private StateMachine mStateMachineForSavingRequest;
    private Handler mUiThreadHandler;
    private RecorderController mVideoRecorder;
    private final Object mVideoRecorderLock;
    
    static {
        sSendPauseEventAndReleaseCameraTaskToken = new Object();
        sCameraSessionInfoMap = (Map)new ArrayMap();
    }
    
    public CameraDeviceHandler(final Context mApplicationContext) {
        this.mLoadSettingsThread = null;
        this.mUiThreadHandler = CameraApplication.getUiThreadHandler();
        this.mApplicationContext = null;
        this.mPreferences = null;
        this.mGeotagManager = null;
        this.mFastCaptureOrientation = null;
        this.mFastCaptureSetting = null;
        this.mIsRecording = false;
        this.mPreProcessState = PreProcessState.NOT_STARTED;
        this.mIsCameraDisabled = false;
        this.mIsFpsLimitationEnabled = false;
        this.mActivityIsInForeground = true;
        this.mIsInShutdownNow = false;
        this.mCameraSessionId = null;
        this.mCloseBypassCameraTimeoutTask = null;
        this.mChangeProviderDeviceStatusToRecordingTask = new Runnable() {
            final CameraDeviceHandler this$0;
            
            @Override
            public void run() {
                this.this$0.mCameraController.changeProviderDeviceStatusToRecording(this.this$0.mCameraSessionId);
            }
        };
        this.mApplicationContext = mApplicationContext;
        final HandlerThread handlerThread = new HandlerThread("CameraAccess", 10);
        handlerThread.start();
        this.mCameraDeviceThreadHandler = new Handler(handlerThread.getLooper());
        this.mVideoRecorderLock = new Object();
        this.runOnCameraDeviceThread(this.mInitControllerTask = (CameraDeviceAccessTask)new InitControllerTask());
        this.runOnCameraDeviceThread((CameraDeviceAccessTask)new LoadNativeLibraryTask());
    }
    
    private void cancelChangeProviderDeviceStatusToRecording() {
        this.mUiThreadHandler.removeCallbacks(this.mChangeProviderDeviceStatusToRecordingTask);
    }
    
    private void cancelCloseBypassCameraTimeoutTask() {
        this.mUiThreadHandler.removeCallbacks((Runnable)this.mCloseBypassCameraTimeoutTask);
        this.mCloseBypassCameraTimeoutTask = null;
    }
    
    private void changePreProcessStateTo(final PreProcessState preProcessState) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked current:");
            sb.append(this.mPreProcessState);
            sb.append(" next:");
            sb.append(preProcessState);
            CamLog.d(sb.toString());
        }
        this.mPreProcessState = preProcessState;
    }
    
    private void changeProviderDeviceStatusToRecording() {
        this.mUiThreadHandler.postDelayed(this.mChangeProviderDeviceStatusToRecordingTask, 3000L);
    }
    
    private void createCameraActionSound() {
        this.mCameraDeviceThreadHandler.post((Runnable)new Runnable(this) {
            final CameraDeviceHandler this$0;
            
            @Override
            public void run() {
                if (this.this$0.mCameraActionSound == null) {
                    this.this$0.mCameraActionSound = new CameraActionSound();
                    this.this$0.mCameraActionSound.load(2);
                    this.this$0.mCameraActionSound.load(3);
                    this.this$0.mCameraActionSound.load(0);
                }
            }
        });
    }
    
    private RequestFactory.PhotoSavingRequestBuilder createPreCaptureSavingRequest(final CameraParameters cameraParameters) {
        final long currentTimeMillis = System.currentTimeMillis();
        int access$2100;
        if (this.mFastCaptureOrientation != null) {
            access$2100 = this.mFastCaptureOrientation.getOrientation();
        }
        else {
            access$2100 = 0;
        }
        Location currentLocation = null;
        if (this.mGeotagManager != null) {
            currentLocation = this.mGeotagManager.getCurrentLocation();
        }
        return new RequestFactory.PhotoSavingRequestBuilder(new TakenStatusCommon(currentTimeMillis, access$2100, currentLocation, cameraParameters.getPictureSize().width(), cameraParameters.getPictureSize().height(), "image/jpeg", ".JPG", SavingTaskManager.SavedFileType.PHOTO, null, "", false, true), new TakenStatusPhoto(), true);
    }
    
    private void dumpStatus(final StringBuilder sb) {
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("mStateMachine:");
        sb2.append(this.mStateMachine);
        sb2.append(",");
        sb.append(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("mPreProcessState:");
        sb3.append(this.mPreProcessState.name());
        sb3.append(",");
        sb.append(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("mActivityIsInForeground:");
        sb4.append(this.mActivityIsInForeground);
        sb4.append(",");
        sb.append(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("mIsCameraDisabled:");
        sb5.append(this.mIsCameraDisabled);
        sb5.append(",");
        sb.append(sb5.toString());
        CameraSessionInfo.dump(sb);
        this.mCameraController.dump(sb);
        this.mBypassCameraController.dump(sb);
    }
    
    private Context getApplicationContext() {
        return this.mApplicationContext;
    }
    
    private CameraParameters getParameters(final CameraSessionId cameraSessionId) {
        final CameraSessionInfo openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        if (openCloseStatusInfo != null) {
            return openCloseStatusInfo.getParameters();
        }
        return null;
    }
    
    private boolean isBypassCameraAvailable() {
        if (this.mCameraSessionId != null) {
            final CameraSessionInfo openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(this.mCameraSessionId);
            return openCloseStatusInfo != null && !openCloseStatusInfo.isCloseBypassCameraTaskRequested();
        }
        return false;
    }
    
    private boolean isBypassCameraSupported() {
        return PlatformCapability.isBypassCameraSupported();
    }
    
    private boolean isNeedCreatePreviewSession() {
        return this.getPreProcessState() == PreProcessState.PRE_SHUTTER_DONE && this.mCameraController.getCameraDeviceStatus() == CameraDeviceStatus.STATUS_OPENED;
    }
    
    private boolean isRecorderReady() {
        return this.mVideoRecorder != null && this.mVideoRecorder.isReady();
    }
    
    public static final void preload() {
    }
    
    private void releaseCameraActionSound() {
        this.mCameraDeviceThreadHandler.post((Runnable)new Runnable(this) {
            final CameraDeviceHandler this$0;
            
            @Override
            public void run() {
                if (this.this$0.mCameraActionSound != null) {
                    this.this$0.mCameraActionSound.release();
                    this.this$0.mCameraActionSound = null;
                }
            }
        });
    }
    
    private void releaseRecorderOnCameraClosed() {
        synchronized (this.mVideoRecorderLock) {
            if (this.mVideoRecorder != null) {
                this.mVideoRecorder.release();
            }
        }
    }
    
    private void requestCloseBypassCameraTimeoutTask(final CameraSessionId cameraSessionId) {
        this.mCloseBypassCameraTimeoutTask = new CloseBypassCameraTimeoutTask(cameraSessionId);
        this.mUiThreadHandler.postDelayed((Runnable)this.mCloseBypassCameraTimeoutTask, 100000L);
    }
    
    private void runOnCameraDeviceThread(final CameraDeviceAccessTask cameraDeviceAccessTask) {
        this.mCameraDeviceThreadHandler.post((Runnable)cameraDeviceAccessTask);
    }
    
    private void runOnCameraDeviceThreadSync(final CameraDeviceAccessTask cameraDeviceAccessTask) {
        this.mCameraDeviceThreadHandler.post((Runnable)cameraDeviceAccessTask);
        try {
            cameraDeviceAccessTask.getLatch().await();
        }
        catch (final InterruptedException ex) {
            CamLog.e("runOnCameraDeviceThreadSync() : Failed to await by InterruptedException", ex);
        }
    }
    
    private void setFocusRect(Rect obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (PlatformCapability.getMaxNumFocusAreas(this.getCameraId()) < 1) {
            if (CamLog.DEBUG) {
                CamLog.d("Focus area change is not supported, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked rect:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (obj.isEmpty()) {
            if (parameters.getFocusArea().equals("user")) {
                final Rect activeArraySize = PlatformCapability.getActiveArraySize(this.getCameraId());
                final int centerX = activeArraySize.centerX();
                final int centerY = activeArraySize.centerY();
                obj.set(centerX, centerY, centerX + 1, centerY + 1);
            }
            else {
                obj = new Rect();
            }
        }
        else {
            parameters.setFocusArea("user");
        }
        final ArrayList focusRectangles = new ArrayList();
        focusRectangles.add(obj);
        parameters.setFocusRectangles(focusRectangles);
    }
    
    private void setFpsRange(final int[] array) {
        if (array.length > 0) {
            final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
            if (parameters == null) {
                if (CamLog.DEBUG) {
                    CamLog.d("This session has been closed, so this request was refused.");
                }
                return;
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked min:");
                sb.append(array[0]);
                sb.append(" max:");
                sb.append(array[1]);
                CamLog.d(sb.toString());
            }
            parameters.setPreviewFpsRange(array[0], array[1]);
        }
        else {
            CamLog.e("Ilegal fps range is specified.");
        }
    }
    
    private void setMeteringArea(final Rect obj, final Metering obj2) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked area:");
            sb.append(obj);
            sb.append(" metering:");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        final ArrayList list = null;
        ArrayList meteringArea;
        if (obj != null) {
            final ArrayList list2 = new ArrayList();
            list2.add(obj);
            parameters.setMeteringMode("user");
            meteringArea = list2;
        }
        else {
            parameters.setMeteringMode(obj2.getValue());
            meteringArea = list;
        }
        parameters.setMeteringArea(meteringArea);
    }
    
    private void setPreviewSize(Rect rectAccordingToLayoutOrientation) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(rectAccordingToLayoutOrientation);
            CamLog.d(sb.toString());
        }
        this.mCameraController.triggerRestartPreview(this.mCameraSessionId, false);
        parameters.setPreviewSize(rectAccordingToLayoutOrientation);
        rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(rectAccordingToLayoutOrientation);
        PositionConverter.getInstance().setPreviewSize(rectAccordingToLayoutOrientation.width(), rectAccordingToLayoutOrientation.height());
    }
    
    public void applySavingRequest(final RequestFactory.RequestBuilder requestBuilder) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        parameters.setRotation(requestBuilder.mCommonStatus.orientation);
        parameters.removeGpsData();
        if (requestBuilder.mCommonStatus.location != null) {
            final double latitude = requestBuilder.mCommonStatus.location.getLatitude();
            final double longitude = requestBuilder.mCommonStatus.location.getLongitude();
            if (latitude != 0.0 || longitude != 0.0) {
                parameters.setGpsData(requestBuilder.mCommonStatus.location);
            }
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void autoFocus() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        if (this.isBypassCameraSupported()) {
            final AfParametersReflectedChecker afParametersResultChecker = this.mCameraController.createAfParametersResultChecker(this.mCameraSessionId, this.mUiThreadHandler);
            if (this.mCameraController.isAfParametersReflectedToDevice(afParametersResultChecker)) {
                this.mBypassCameraController.requestSnapshotReady(this.mCameraSessionId);
            }
            else {
                this.mUiThreadHandler.postDelayed(this.mCameraController.requestSnapshotReadyAfterAfParametersReflected(this.mCameraSessionId, afParametersResultChecker), 5000L);
            }
        }
    }
    
    public boolean awaitLoadSettingsThread() {
        if (this.mLoadSettingsThread == null) {
            CamLog.d("awaitSettingLoadThread thread is unnecessary");
            return true;
        }
        boolean b = false;
        Label_0195: {
            try {
                this.mLoadSettingsThread.join(4000L);
                if (!this.mLoadSettingsThread.isAlive()) {
                    b = true;
                    break Label_0195;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Thread:");
                sb.append(this.mLoadSettingsThread.getName());
                sb.append(" is Timed out.");
                CamLog.e(sb.toString());
            }
            catch (final InterruptedException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Thread:");
                sb2.append(this.mLoadSettingsThread.getName());
                sb2.append(" is Interrupted.");
                CamLog.e(sb2.toString(), ex);
            }
            catch (final CancellationException ex2) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Thread:");
                sb3.append(this.mLoadSettingsThread.getName());
                sb3.append(" is Cancelled.");
                CamLog.e(sb3.toString(), ex2);
            }
            b = false;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("invoked success:");
            sb4.append(b);
            CamLog.d(sb4.toString());
        }
        return b;
    }
    
    public boolean canRecorderTakeSnapshot() {
        return this.isBypassCameraNextShotAvailable() && !this.mVideoRecorder.isStarting() && (this.mVideoRecorder.isRecording() || this.mVideoRecorder.isPaused());
    }
    
    public void cancelAutoFocus() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        if (this.isBypassCameraSupported()) {
            this.mBypassCameraController.requestSnapshotFree(this.mCameraSessionId);
        }
    }
    
    public void cancelPreProcessState() {
        this.changePreProcessStateTo(PreProcessState.NOT_STARTED);
    }
    
    public void captureWhileRecording(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        this.takePicture(photoSavingRequestBuilder);
    }
    
    public void closeCamera() {
        this.closeCamera(false);
    }
    
    public void closeCamera(final CameraSessionId obj) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked requested-session:");
            sb.append(obj);
            sb.append(" current-session:");
            sb.append(this.mCameraSessionId);
            CamLog.d(sb.toString());
        }
        if (this.mCameraSessionId != null && obj == this.mCameraSessionId) {
            this.closeCamera();
        }
    }
    
    public void closeCamera(final boolean b) {
        if (CameraSessionInfo.getOpenCloseStatusInfo(this.mCameraSessionId) == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        ((CameraStatusPublisher<SlowMotion>)new EachCameraStatusPublisher(this.getApplicationContext(), this.getCameraId())).put(new SlowMotion(SlowMotion.Value.OFF)).publish();
        if (this.mPreProcessState != PreProcessState.NOT_STARTED && this.mPreProcessState != PreProcessState.PRE_CAPTURE_DONE) {
            this.changePreProcessStateTo(PreProcessState.PRE_CAPTURE_RELEASED);
        }
        else {
            this.changePreProcessStateTo(PreProcessState.NOT_STARTED);
        }
        boolean b3;
        final boolean b2 = b3 = (b | this.mIsInShutdownNow);
        if (!b2) {
            b3 = b2;
            if (this.isRecorderWorking()) {
                b3 = b2;
                if (((BatteryManager)this.mApplicationContext.getSystemService("batterymanager")).getIntProperty(4) == 0) {
                    b3 = true;
                }
            }
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked sync:");
            sb.append(b3);
            CamLog.d(sb.toString());
        }
        if (this.mGeotagManager != null) {
            this.mGeotagManager.releaseResource();
            this.mGeotagManager.release();
            this.mGeotagManager = null;
        }
        if (this.mFastCaptureOrientation != null) {
            this.mFastCaptureOrientation.disable();
            this.mFastCaptureOrientation = null;
        }
        if (this.isRecorderWorking()) {
            CamLog.i("Camera is waiting for completion of capturing in recording video.");
            this.mBypassCameraController.awaitAllSnapshotDone();
        }
        this.cancelChangeProviderDeviceStatusToRecording();
        synchronized (this.mVideoRecorderLock) {
            Label_0335: {
                if (this.mVideoRecorder != null) {
                    if (!this.mVideoRecorder.isRecording()) {
                        if (!this.mVideoRecorder.isPaused()) {
                            break Label_0335;
                        }
                    }
                    try {
                        this.mVideoRecorder.stop();
                    }
                    catch (final RecorderException ex) {
                        throw new RuntimeException("stopRecording():[Failed to stop MediaRecorder.]");
                    }
                }
            }
            monitorexit(this.mVideoRecorderLock);
            this.mCameraController.closeCamera(b3, this.mCameraSessionId);
            this.mFastCaptureSetting = null;
            this.mBypassCameraController.closeBypassCamera(b3, this.mCameraSessionId);
            this.requestCloseBypassCameraTimeoutTask(this.mCameraSessionId);
            this.releaseCameraActionSound();
        }
    }
    
    public void commit() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked pre-process:");
            sb.append(this.mPreProcessState);
            sb.append(" remain-saving-photo:");
            sb.append(this.getRemainSavingPhotoRequestCount());
            CamLog.d(sb.toString());
        }
        this.mCameraController.commit(this.mCameraSessionId);
        this.mBypassCameraController.commit(this.mCameraSessionId);
        if (this.mPreProcessState == PreProcessState.NOT_STARTED || this.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) {
            if (this.getRemainSavingPhotoRequestCount() != 0) {
                return;
            }
            this.mBypassCameraController.prepareCaptureImageReader(this.mCameraSessionId, null);
        }
    }
    
    public void disableFpsLimitation() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mIsFpsLimitationEnabled = false;
    }
    
    public void enableFpsLimitation() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        if (!this.mIsFpsLimitationEnabled) {
            this.mIsFpsLimitationEnabled = true;
            final int maxPreviewFps = PlatformCapability.getMaxPreviewFps(parameters.getCameraId());
            final List<int[]> supportedPreviewFpsRange = PlatformCapability.getSupportedPreviewFpsRange(parameters.getCameraId());
            if (supportedPreviewFpsRange != null) {
                this.setFpsRange(CameraDeviceUtil.computePreviewFpsRange(parameters.getCameraId(), maxPreviewFps, supportedPreviewFpsRange));
                this.mCameraController.commitParameters(this.mCameraSessionId);
            }
        }
    }
    
    public void finalizeRecording() {
        synchronized (this.mVideoRecorderLock) {
            Label_0156: {
                if (this.mVideoRecorder != null) {
                    try {
                        if (this.mVideoRecorder.isRecording() || this.mVideoRecorder.isPaused()) {
                            this.mVideoRecorder.stop();
                        }
                        final long recordingTimeMillis = this.mVideoRecorder.getRecordingTimeMillis();
                        if (CamLog.DEBUG) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("invoked duration:");
                            sb.append(recordingTimeMillis);
                            CamLog.d(sb.toString());
                        }
                        this.mLastVideoSavingRequest.setDuration(recordingTimeMillis);
                        break Label_0156;
                    }
                    catch (final RecorderException ex) {
                        throw new RuntimeException("stopRecording():[Failed to stop MediaRecorder.]");
                    }
                }
                if (CamLog.DEBUG) {
                    CamLog.d("Recorder doesn't exists, so this request is refused.");
                }
            }
            this.mIsRecording = false;
        }
    }
    
    public void finishBurst() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mBypassCameraController.requestFinishBurstShot(this.mCameraSessionId);
    }
    
    public RequestFactory.PhotoSavingRequestBuilder getAndClearPreCaptureResult() {
        return this.mBypassCameraController.getAndClearPreCaptureResult();
    }
    
    public CameraInfo.CameraId getCameraId() {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters != null) {
            return parameters.getCameraId();
        }
        return null;
    }
    
    public CameraInfo getCameraInfo() {
        return CameraSessionInfo.getOpenCloseStatusInfo(this.mCameraSessionId).getCameraInfo();
    }
    
    public CameraParameters.FusionResult getLatestFusionResult() {
        return this.mCameraController.getLatestFusionResult();
    }
    
    @Deprecated
    public CameraParameters getParameters() {
        return this.getParameters(this.mCameraSessionId);
    }
    
    public PreProcessState getPreProcessState() {
        return this.mPreProcessState;
    }
    
    public Rect getPreviewRect(final CapturingMode capturingMode, final Rect rect) {
        if (!capturingMode.isVideo()) {
            return this.mCameraController.getPhotoPreviewSize(this.mCameraSessionId, capturingMode.getCameraId(), rect);
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return this.mCameraController.getVideoPreviewSize(this.mCameraSessionId, capturingMode.getCameraId(), rect);
        }
        if (parameters.getVideoHdr() == VideoHdr.HDR_ON) {
            return PlatformCapability.getPreferredPreviewSizeForHdrVideo(capturingMode.getCameraId());
        }
        return this.mCameraController.getVideoPreviewSize(this.mCameraSessionId, capturingMode.getCameraId(), rect);
    }
    
    public Rect getPreviewSize() {
        if (this.getParameters() == null) {
            return null;
        }
        return this.getParameters().getPreviewSize();
    }
    
    public int getRemainPrevSavingRequestCount() {
        return this.mBypassCameraController.getRemainPrevSavingRequestCount();
    }
    
    public int getRemainSavingPhotoRequestCount() {
        return this.mBypassCameraController.getRemainSavingPhotoRequestCount();
    }
    
    public Object getSendPauseEventAndReleaseCameraTaskToken() {
        return CameraDeviceHandler.sSendPauseEventAndReleaseCameraTaskToken;
    }
    
    public ImageRetriever getStreamingImageRetriever() {
        return this.mCameraController.getStreamingImageRetriever();
    }
    
    public Float getZoom() {
        final CameraParameters parameters = this.getParameters();
        if (parameters != null) {
            return parameters.getZoom();
        }
        return null;
    }
    
    public boolean isBypassCameraNextShotAvailable() {
        return this.mBypassCameraController.isBypassCameraNextShotAvailable();
    }
    
    public boolean isCameraDeviceStatusReady() {
        return this.mCameraController.getCameraDeviceStatus() == CameraDeviceStatus.STATUS_READY;
    }
    
    public boolean isCameraDisabled() {
        return this.mIsCameraDisabled;
    }
    
    public boolean isCameraFront() {
        return this.getCameraId() == CameraInfo.CameraId.FRONT;
    }
    
    public boolean isObjectTrackingRunning() {
        return this.mCameraController.isObjectTrackingRunning();
    }
    
    public boolean isPreCaptureOnGoing() {
        return this.mPreProcessState == PreProcessState.PRE_CAPTURE_STARTED || this.mPreProcessState == PreProcessState.PRE_SHUTTER_DONE;
    }
    
    public boolean isPreScanOnGoing() {
        return this.mPreProcessState == PreProcessState.PRE_SCAN_STARTED;
    }
    
    public boolean isRecorderWorking() {
        if (this.mVideoRecorder == null) {
            return false;
        }
        if (!this.mVideoRecorder.isRecording() && !this.mVideoRecorder.isPaused() && !this.mVideoRecorder.isStopping()) {
            return false;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invoked isRecorderWorking() isRecording:");
        sb.append(this.mVideoRecorder.isRecording());
        sb.append(" isPaused:");
        sb.append(this.mVideoRecorder.isPaused());
        sb.append(" isStopping:");
        sb.append(this.mVideoRecorder.isStopping());
        CamLog.d(sb.toString());
        return true;
    }
    
    public boolean isRecording() {
        return this.mIsRecording;
    }
    
    public boolean isSteadyShotSupported() {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            CamLog.w("[getParameters failed] Did not check availability of VideoStabilizer.");
            return false;
        }
        return VideoStabilizer.STEADY_SHOT.isValueEnabled(parameters.getCameraId(), parameters.getVideoSize(), parameters.getVideoHdr());
    }
    
    public CameraSessionId openCamera(final FastCapture obj, final CapturingMode obj2, final UserSettings userSettings) {
        synchronized (this) {
            this.mCameraController.openCamera(this.mCameraSessionId, obj);
            LocalResearchUtil.getInstance().clearAllSettings();
            if (obj == FastCapture.LAUNCH_AND_CAPTURE && (this.mLoadSettingsThread == null || !this.mLoadSettingsThread.isAlive())) {
                (this.mLoadSettingsThread = new LoadSettingsThread(obj2, userSettings)).setName("LoadSettingsThread");
                this.mLoadSettingsThread.setPriority(10);
                this.mLoadSettingsThread.start();
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked mode:");
                sb.append(obj2);
                sb.append(" quick-launch:");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            return this.mCameraSessionId;
        }
    }
    
    public void pauseRecording() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        try {
            if (this.mVideoRecorder != null && this.mVideoRecorder.isRecording()) {
                this.mVideoRecorder.pause();
            }
        }
        catch (final RecorderException ex) {
            throw new RuntimeException("pauseRecording():[Failed to pause MediaRecorder.]");
        }
    }
    
    public void playShutterSound(final int i) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked type:");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        if (this.mCameraActionSound != null) {
            switch (i) {
                case 2: {
                    this.mCameraActionSound.play(2, true);
                    break;
                }
                case 1: {
                    this.mCameraActionSound.play(0, false);
                    break;
                }
            }
        }
    }
    
    public void preCapture() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return;
        }
        final RequestFactory.PhotoSavingRequestBuilder preCaptureSavingRequest = this.createPreCaptureSavingRequest(parameters);
        PerfLog.FAST_PRE_CAPTURE.transit();
        this.changePreProcessStateTo(PreProcessState.PRE_CAPTURE_STARTED);
        this.mBypassCameraController.requestSnapshot(this.mCameraSessionId, preCaptureSavingRequest, 1);
    }
    
    public CameraSessionId preloadCamera(final Context context, final UserSettings userSettings, CapturingMode scene_RECOGNITION, final boolean b) {
        synchronized (this) {
            if (this.isBypassCameraAvailable()) {
                if (CamLog.DEBUG) {
                    CamLog.d("Camera is already preloaded.");
                }
                return this.mCameraSessionId;
            }
            if (userSettings == null) {
                this.mPreferences = context.getSharedPreferences("com.sonyericsson.android.camera.shared_preferences", 0);
            }
            try {
                this.mInitControllerTask.getLatch().await();
                if (this.mPreProcessState != PreProcessState.PRE_CAPTURE_RELEASED) {
                    this.changePreProcessStateTo(PreProcessState.NOT_STARTED);
                }
                FastCapture fastCapture;
                if (b) {
                    scene_RECOGNITION = CapturingMode.SCENE_RECOGNITION;
                    fastCapture = FastCapture.LAUNCH_AND_CAPTURE;
                }
                else {
                    fastCapture = FastCapture.LAUNCH_ONLY;
                }
                this.mIsVideo = scene_RECOGNITION.isVideo();
                this.mCameraSessionId = this.mBypassCameraController.openBypassCamera(this.mPreferences, userSettings, fastCapture, scene_RECOGNITION);
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoked mode:");
                    sb.append(scene_RECOGNITION);
                    sb.append(" fast-capture:");
                    sb.append(b);
                    CamLog.d(sb.toString());
                }
                this.cancelCloseBypassCameraTimeoutTask();
                return this.mCameraSessionId;
            }
            catch (final InterruptedException ex) {
                CamLog.e("InitControllerTask is interrupted.", ex);
                return null;
            }
        }
    }
    
    public void prepareBurst() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mBypassCameraController.requestPrepareBurstShot(this.mCameraSessionId);
    }
    
    public boolean prepareCamera(final FastCapture mFastCaptureSetting, final CapturingMode capturingMode, final UserSettings userSettings) {
        synchronized (this) {
            CameraParameterValidator.loadCheckList(this.getApplicationContext());
            if (((DevicePolicyManager)this.getApplicationContext().getSystemService("device_policy")).getCameraDisabled((ComponentName)null)) {
                CamLog.i("Use of camera is prohibited by device policy.");
                if (CamLog.DEBUG) {
                    CamLog.d("Camera is disabled, so this request was refused.");
                }
                this.mIsCameraDisabled = true;
                return false;
            }
            this.mIsCameraDisabled = false;
            this.mIsVideo = capturingMode.isVideo();
            this.mFastCaptureSetting = mFastCaptureSetting;
            this.createCameraActionSound();
            if (!this.isBypassCameraAvailable()) {
                this.mCameraSessionId = this.mBypassCameraController.openBypassCamera(this.mPreferences, userSettings, this.mFastCaptureSetting, capturingMode);
                this.cancelCloseBypassCameraTimeoutTask();
            }
            this.mCameraController.initializeCaptureRequest(capturingMode);
            return true;
        }
    }
    
    public void prepareCaptureImageReader(final ImageReaderInitializedCallback imageReaderInitializedCallback) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mBypassCameraController.prepareCaptureImageReader(this.mCameraSessionId, imageReaderInitializedCallback);
    }
    
    public void prepareRecorder(final RequestFactory.VideoSavingRequestBuilder mLastVideoSavingRequest, final RecorderController.RecorderListener recorderListener, final OnSuperSlowRecordingFinishedListener onSuperSlowRecordingFinishedListener, final boolean b, final RecordingProfile recordingProfile, final Storage.StorageWriteNotifier storageWriteNotifier) {
        if (this.mVideoRecorder != null && this.mVideoRecorder.isStopping()) {
            if (CamLog.DEBUG) {
                CamLog.d("Recorder is stopping, so this request is refused.");
            }
            return;
        }
        if (this.isRecorderReady()) {
            if (CamLog.DEBUG) {
                CamLog.d("Recorder is already ready, so this request is refused.");
            }
            return;
        }
        this.mLastVideoSavingRequest = mLastVideoSavingRequest;
        Uri obj;
        if ((obj = ((RequestFactory.RequestBuilder)mLastVideoSavingRequest).getExtraOutput()) == null) {
            obj = Uri.fromFile(new File(((RequestFactory.RequestBuilder)mLastVideoSavingRequest).getFilePath()));
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return;
        }
        final RecorderFactory.Parameters parameters2 = new RecorderFactory.Parameters(recorderListener, onSuperSlowRecordingFinishedListener, recordingProfile.getProgressInterval(), b, parameters.getVideoStabilizer(), parameters.getSlowMotion());
        this.releaseRecorder();
        final Accessor<BypassCamera> accessor = new Accessor<BypassCamera>(this) {
            final CameraDeviceHandler this$0;
            
            @Override
            public BypassCamera get() {
                return this.this$0.mBypassCameraController.getBypassCameraInstance();
            }
        };
        final Accessor<CameraActionSound> accessor2 = new Accessor<CameraActionSound>(this) {
            final CameraDeviceHandler this$0;
            
            @Override
            public CameraActionSound get() {
                return this.this$0.mCameraActionSound;
            }
        };
        final VideoSize videoSize = this.getParameters().getVideoSize();
        (this.mVideoRecorder = RecorderFactory.create(this.getApplicationContext(), accessor2, accessor, this.mUiThreadHandler, this.mCameraDeviceThreadHandler, parameters2, (int)PlatformCapability.getSuperSlowFrameRate(parameters.getCameraId(), videoSize), (int)PlatformCapability.getSuperSlowFrameNum(parameters.getCameraId(), videoSize))).setStorageWriteNotifier(storageWriteNotifier);
        final CamcorderProfile camcorderProfile = recordingProfile.getCamcorderProfile();
        if (camcorderProfile == null) {
            CamLog.e("prepareRecorder() : CamcorderProfile is null.");
            throw new RuntimeException("CamcorderProfile is null.");
        }
        final com.sonyericsson.android.camera.configuration.parameters.SlowMotion slowMotion = parameters.getSlowMotion();
        final com.sonyericsson.android.camera.configuration.parameters.SlowMotion super_SLOW_SHOT = com.sonyericsson.android.camera.configuration.parameters.SlowMotion.SUPER_SLOW_SHOT;
        final boolean b2 = false;
        final boolean b3 = slowMotion != super_SLOW_SHOT && !RecordingUtil.isAudioPolicyActive(this.getApplicationContext());
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked uri:");
            sb.append(obj);
            sb.append(" audio-record-enabled:");
            sb.append(b3);
            CamLog.d(sb.toString());
        }
        final RecorderParameters.Builder setOrientationHint = new RecorderParameters.Builder(obj, camcorderProfile).setLocation(mLastVideoSavingRequest.mCommonStatus.location).setMaxDuration((int)mLastVideoSavingRequest.mVideoStatus.maxDurationMills).setMaxFileSize(mLastVideoSavingRequest.mVideoStatus.maxFileSizeBytes).setMicrophoneEnabled(b3).setOrientationHint(mLastVideoSavingRequest.mCommonStatus.orientation);
        boolean hdr = b2;
        if (parameters.getVideoHdr() == VideoHdr.HDR_ON) {
            hdr = true;
        }
        if (!this.mVideoRecorder.prepare(setOrientationHint.setHdr(hdr).setDataSpace(recordingProfile.dataSpace).build())) {
            CamLog.e("prepareRecorder() : Failed to prepare MediaRecorder.");
            this.releaseRecorder();
            throw new RuntimeException("prepareRecorder():[Failed to prepare MediaRecorder.]");
        }
    }
    
    public void releaseRecorder() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        synchronized (this.mVideoRecorderLock) {
            if (this.mVideoRecorder != null) {
                this.mVideoRecorder.release();
                this.mVideoRecorder = null;
            }
        }
    }
    
    public void releaseVideo() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.releaseRecorder();
    }
    
    public void removeOnPreviewStartedListener() {
        this.mCameraController.removeOnPreviewStartedListener();
    }
    
    public void requestOnePreviewFrame() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.requestOnePreviewFrame(this.mCameraSessionId, this.mUiThreadHandler);
    }
    
    public void resetFocusAreaAndRect(final FocusMode obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (obj != null) {
            parameters.setFocusArea(obj.getFocusArea());
        }
        this.setFocusRect(new Rect());
    }
    
    public void resetFocusModeAndCommit() {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (PlatformCapability.getMaxNumFocusAreas(this.getCameraId()) < 1) {
            if (CamLog.DEBUG) {
                CamLog.d("Focus position change is not supported, so this request is refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked current-focus-mode:");
            sb.append(parameters.getFocusMode());
            CamLog.d(sb.toString());
        }
        if (!"manual".equals(parameters.getFocusMode())) {
            parameters.setFocusMode(PlatformDependencyResolver.getDefaultFocusModeForFastCapturePhoto(parameters, this.getCameraId()));
        }
        parameters.setFocusArea("center");
        parameters.setFocusRectangles(null);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void resumeRecording() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        try {
            if (this.mVideoRecorder != null && this.mVideoRecorder.isPaused()) {
                this.mVideoRecorder.resume();
                ResearchUtil.getInstance().incrementCountRecordResume();
            }
        }
        catch (final RecorderException ex) {
            throw new RuntimeException("resumeRecording():[Failed to resume MediaRecorder.]");
        }
    }
    
    public void savePreloadSettings(final CapturingMode obj, final UserSettings userSettings, final LastSettings lastSettings, final boolean b) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked mode:");
            sb.append(obj);
            sb.append(" onde-shot:");
            sb.append(b);
            sb.append(" preview-size:");
            sb.append(parameters.getPreviewSize());
            CamLog.d(sb.toString());
        }
        if (parameters.getPreviewSize() != null && !b) {
            lastSettings.setPreviewSize(parameters.getPreviewSize(), obj);
            lastSettings.setFastCapture((FastCapture)userSettings.get(UserSettingKey.FAST_CAPTURE));
            lastSettings.save();
        }
    }
    
    public void setActivityForeground(final boolean mActivityIsInForeground) {
        this.mActivityIsInForeground = mActivityIsInForeground;
    }
    
    public void setAmberBlueColorAndCommit(final int n) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        parameters.setAwbColorCompensationAb(n);
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void setBrightnessAndCommit(final int n) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        parameters.setExposureCompensation(n);
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void setCapturingMode(final CapturingMode obj) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            sb.append(" video:");
            sb.append(this.mIsVideo);
            CamLog.d(sb.toString());
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        if (this.mIsVideo != obj.isVideo()) {
            this.mIsVideo = obj.isVideo();
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        if (obj != CapturingMode.VIDEO && parameters.getVideoHdr() != VideoHdr.HDR_OFF) {
            parameters.setVideoHdr(VideoHdr.HDR_OFF);
        }
        this.setZoom(0.0f);
        if (!this.mIsVideo) {
            this.setFpsRange(CameraDeviceUtil.computePreviewFpsRange(this.getCameraId(), PlatformCapability.getMaxPreviewFps(this.getCameraId()), PlatformCapability.getSupportedPreviewFpsRange(this.getCameraId())));
        }
        this.mCameraController.triggerRestartPreview(this.mCameraSessionId, true);
    }
    
    public void setDisplayFlashMode(final DisplayFlash displayFlash) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return;
        }
        if (!PlatformCapability.isDisplayFlashModeSupported(this.getCameraId())) {
            if (CamLog.DEBUG) {
                CamLog.d("Display flash is not supported, so this request was refused.");
            }
            return;
        }
        if (!this.isPreCaptureOnGoing() && !this.isPreScanOnGoing()) {
            DisplayFlash display_OFF = displayFlash;
            if (!PlatformCapability.getSupportedFlashModes(this.getCameraId()).contains(displayFlash.getValue())) {
                display_OFF = DisplayFlash.DISPLAY_OFF;
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked value:");
                sb.append(display_OFF);
                CamLog.d(sb.toString());
            }
            parameters.setFlashMode(display_OFF.getValue());
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("Capturing for quick launch is on going, so this request was refused.");
        }
    }
    
    public void setDisplayFlashModeAndCommit(final DisplayFlash displayFlash) {
        if (!this.isPreCaptureOnGoing() && !this.isPreScanOnGoing()) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked value:");
                sb.append(displayFlash);
                CamLog.d(sb.toString());
            }
            this.setDisplayFlashMode(displayFlash);
            this.mCameraController.commitParameters(this.mCameraSessionId);
        }
    }
    
    public void setDistortionCorrection(DistortionCorrection off) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(off);
            CamLog.d(sb.toString());
        }
        if (!PlatformCapability.isDistortionCorrectionSupported(this.getCameraId()) || this.mIsVideo) {
            off = DistortionCorrection.OFF;
        }
        parameters.setDistortionCorrection(off.getValue());
    }
    
    public void setEv(final Ev obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        parameters.setExposureCompensation(obj.getIntValue());
    }
    
    public void setFlashMode(final Flash flash) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (PlatformCapability.isDisplayFlashModeSupported(this.getCameraId())) {
            if (CamLog.DEBUG) {
                CamLog.d("Display flash is supported, so this request was refused.");
            }
            return;
        }
        Flash off = flash;
        if (!PlatformCapability.getSupportedFlashModes(this.getCameraId()).contains(flash.getValue())) {
            off = Flash.OFF;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(off);
            CamLog.d(sb.toString());
        }
        parameters.setFlashMode(off.getValue());
        final EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher(this.getApplicationContext(), parameters.getCameraId());
        PhotoLight.Value value;
        if ("torch".equals(off.getValue())) {
            value = PhotoLight.Value.ON;
        }
        else {
            value = PhotoLight.Value.OFF;
        }
        ((CameraStatusPublisher<PhotoLight>)eachCameraStatusPublisher).put(new PhotoLight(value)).publish();
    }
    
    public void setFlashModeAndCommit(final Flash flashMode) {
        if (!this.isPreCaptureOnGoing() && !this.isPreScanOnGoing()) {
            this.setFlashMode(flashMode);
            this.mCameraController.commitParameters(this.mCameraSessionId);
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("Capturing for quick launch is on going, so this request was refused.");
        }
    }
    
    public void setFocusMode(final FocusMode focusMode) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(focusMode);
            sb.append("video:");
            sb.append(this.mIsVideo);
            CamLog.d(sb.toString());
        }
        if (this.mIsVideo) {
            parameters.setFocusMode(focusMode.getValueForVideo());
        }
        else {
            parameters.setFocusMode(focusMode.getValue());
        }
        if (!PlatformCapability.isFocusSupported(parameters.getCameraId())) {
            CamLog.d("Camera focus isn't supported. FocusArea is not set.");
        }
        else {
            this.resetFocusAreaAndRect(focusMode);
        }
        if (!FocusMode.OBJECT_TRACKING.equals(focusMode)) {
            this.stopObjectTracking();
        }
    }
    
    public void setFocusPositionAndCommit(final Rect obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (PlatformCapability.getMaxNumFocusAreas(this.getCameraId()) < 1) {
            if (CamLog.DEBUG) {
                CamLog.d("Focus position change is not supported, so this request is refused.");
            }
            return;
        }
        final Rect rect = new Rect(PositionConverter.getInstance().convertFromViewToActiveArray(obj));
        parameters.setFocusArea("user");
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked rect:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        final ArrayList focusRectangles = new ArrayList();
        focusRectangles.add(rect);
        parameters.setFocusRectangles(focusRectangles);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void setFocusRange(final FocusRange obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            sb.append(" video:");
            sb.append(this.mIsVideo);
            sb.append(" focus-mode:");
            sb.append(parameters.getFocusMode());
            CamLog.d(sb.toString());
        }
        if (this.mIsVideo) {
            return;
        }
        if (obj == FocusRange.AF) {
            if (PlatformCapability.isFocusSupported(parameters.getCameraId())) {
                parameters.setFocusMode("continuous-picture");
            }
            else {
                parameters.setFocusMode("fixed");
            }
            return;
        }
        if (!"manual".equals(parameters.getFocusMode())) {
            this.stopObjectTracking();
        }
        parameters.setFocusMode("manual");
        parameters.setFocusArea("center");
        parameters.setFocusRectangles(null);
        if (FocusRange.DEFAULT == obj) {
            parameters.setFocusRange(CameraParameters.MANUAL_FOCUS_1M);
        }
        else {
            parameters.setFocusRange(obj.getFocusRange(parameters.getCameraId()));
        }
    }
    
    public void setFusionMode(final FusionMode obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        parameters.setFusionMode(obj.getValue());
    }
    
    public void setHdr(final Hdr obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        parameters.setStillHdr(obj.getValue());
    }
    
    public void setIsInShutdownNow(final boolean mIsInShutdownNow) {
        this.mIsInShutdownNow = mIsInShutdownNow;
    }
    
    public void setIso(final Iso obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            sb.append(" shutter-speed:");
            sb.append(parameters.getShutterSpeed());
            CamLog.d(sb.toString());
        }
        parameters.setIso(obj.getIsoValue());
        if (obj == Iso.ISO_AUTO) {
            if (ShutterSpeed.AUTO.getShutterSpeedInNanoMillis() == parameters.getShutterSpeed()) {
                parameters.setAeMode("auto");
            }
            else {
                parameters.setAeMode("shutter-prio");
            }
        }
        else if (ShutterSpeed.AUTO.getShutterSpeedInNanoMillis() == parameters.getShutterSpeed()) {
            parameters.setAeMode("iso-prio");
        }
        else if (PlatformCapability.getSupportedAeModes(parameters.getCameraId()).contains("semi-auto")) {
            parameters.setAeMode("semi-auto");
        }
        else {
            this.setShutterSpeed(ShutterSpeed.AUTO);
        }
    }
    
    public void setLowPower() {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        parameters.setPowerMode("low");
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void setMetering(final Metering obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        parameters.setMeteringMode(obj.getValue());
        this.setMeteringArea(null, obj);
    }
    
    public void setMeteringAreaAndCommit(final Rect obj, final Metering metering) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        Rect rect = null;
        if (obj != null) {
            rect = new Rect(PositionConverter.getInstance().convertFromViewToActiveArray(obj));
        }
        this.setMeteringArea(rect, metering);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void setOnPreviewStartedListener(final OnPreviewStartedListener onPreviewStartedListener) {
        this.mCameraController.setOnPreviewStartedListener(onPreviewStartedListener);
    }
    
    public void setPredictiveCapture(PredictiveCapture on) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (DebugParameterUtils.INSTANCE.isAlwaysPredictiveCaptureEnabled(this.getApplicationContext())) {
            CamLog.i("PredictiveCapture setting has been overwritten by DebugParameterUtils.");
            on = PredictiveCapture.ON;
        }
        parameters.setPredictiveCapture(on.getValue());
        parameters.setPredictiveCaptureNum(on.getCaptureNum());
    }
    
    public void setPredictiveCaptureAndCommit(final PredictiveCapture predictiveCapture) {
        if (CamLog.DEBUG) {
            final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
            if (parameters != null) {
                final String predictiveCapture2 = parameters.getPredictiveCapture();
                if (!predictiveCapture.getValue().equals(predictiveCapture2)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("PredictiveCapture setting was changed : ");
                    sb.append(predictiveCapture2);
                    sb.append(" -> ");
                    sb.append(predictiveCapture.getValue());
                    CamLog.d(sb.toString());
                }
            }
        }
        this.setPredictiveCapture(predictiveCapture);
        this.mBypassCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void setPreviewSizeAndFpsRangeForVideo(final CameraInfo.CameraId obj, final VideoSize obj2, final VideoHdr videoHdr) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked cameraId:");
            sb.append(obj);
            sb.append(" video-size:");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        Rect previewSize;
        if (videoHdr == VideoHdr.HDR_ON) {
            previewSize = PlatformCapability.getPreferredPreviewSizeForHdrVideo(obj);
        }
        else {
            previewSize = this.mCameraController.getVideoPreviewSize(this.mCameraSessionId, obj, obj2.getVideoRect());
        }
        if (previewSize != null) {
            this.setPreviewSize(previewSize);
            this.setFpsRange(CameraDeviceUtil.computePreviewFpsRange(obj, RecordingProfile.getVideoFrameRate(obj2, videoHdr), PlatformCapability.getSupportedPreviewFpsRange(obj)));
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public void setPreviewSurface(final Surface obj) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked surface:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mCameraController.setSurface(this.mCameraSessionId, false, obj);
    }
    
    public void setResolution(final CameraInfo.CameraId obj, final Resolution obj2) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked cameraId:");
            sb.append(obj);
            sb.append(" resolution:");
            sb.append(obj2);
            sb.append(" prev-resolution:");
            sb.append(parameters.getPictureSize());
            sb.append(" video:");
            sb.append(this.mIsVideo);
            CamLog.d(sb.toString());
        }
        if (parameters.getPictureSize() != obj2.getPictureRect() && !this.mIsVideo) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        final Rect photoPreviewSize = this.mCameraController.getPhotoPreviewSize(this.mCameraSessionId, obj, obj2.getPictureRect());
        if (photoPreviewSize != null && !this.mIsVideo) {
            parameters.setPictureSize(obj2.getPictureRect());
            this.setPreviewSize(photoPreviewSize);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public void setSelectedFacePosition(final int i, final int j) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked x:");
            sb.append(i);
            sb.append(" y:");
            sb.append(j);
            CamLog.d(sb.toString());
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        if (PlatformCapability.isFaceDetectionAvailable(parameters.getCameraId())) {
            this.resetFocusModeAndCommit();
            this.mCameraController.setSelectedFacePosition(this.mCameraSessionId, i, j);
        }
        else {
            CamLog.i("Face detection is not available.");
        }
    }
    
    public void setShutterSpeed(final ShutterSpeed obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            sb.append("iso:");
            sb.append(parameters.getIso());
            CamLog.d(sb.toString());
        }
        parameters.setShutterSpeed(obj.getShutterSpeedInNanoMillis());
        if (obj == ShutterSpeed.AUTO) {
            if (Iso.ISO_AUTO.getIsoValue() == parameters.getIso()) {
                parameters.setAeMode("auto");
            }
            else {
                parameters.setAeMode("iso-prio");
            }
        }
        else if (Iso.ISO_AUTO.getIsoValue() == parameters.getIso()) {
            parameters.setAeMode("shutter-prio");
        }
        else if (PlatformCapability.getSupportedAeModes(parameters.getCameraId()).contains("semi-auto")) {
            parameters.setAeMode("semi-auto");
        }
        else {
            this.setIso(Iso.ISO_AUTO);
        }
    }
    
    public void setShutterTrigger(final ShutterTrigger shutterTrigger) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(shutterTrigger);
            CamLog.d(sb.toString());
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters != null && parameters.getShutterTrigger() != shutterTrigger) {
            parameters.setShutterTrigger(shutterTrigger);
            this.mCameraController.triggerRestartPreview(this.mCameraSessionId, false);
        }
    }
    
    public void setSlowMotion(final com.sonyericsson.android.camera.configuration.parameters.SlowMotion slowMotion) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(slowMotion);
            sb.append(" video:");
            sb.append(this.mIsVideo);
            CamLog.d(sb.toString());
        }
        if (parameters.getSlowMotion() == slowMotion) {
            return;
        }
        if (this.mIsVideo) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        parameters.setSlowMotion(slowMotion);
        switch (CameraDeviceHandler$6.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[slowMotion.ordinal()]) {
            default: {
                parameters.setExposureTimeLimit(PlatformCapability.getMinExposureTimeLimit(parameters.getCameraId()));
                break;
            }
            case 1:
            case 2: {
                parameters.setExposureTimeLimit((long)Math.ceil(1.0E9 / PlatformCapability.getSuperSlowFrameRate(parameters.getCameraId(), parameters.getVideoSize())));
                break;
            }
        }
    }
    
    public void setSoftSkin(final SoftSkin obj) {
        if (this.mIsVideo || this.getCameraId() == CameraInfo.CameraId.BACK) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("This request was refused. video:");
                sb.append(this.mIsVideo);
                sb.append(" cameraId:");
                sb.append(this.getCameraId());
                CamLog.d(sb.toString());
            }
            return;
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("invoked value:");
            sb2.append(obj);
            CamLog.d(sb2.toString());
        }
        parameters.setSoftSkin(obj.getLevel(PlatformCapability.getMaxSoftSkinLevel(this.getCameraId())));
    }
    
    public void setStateMachine(final StateMachine stateMachine) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked prev:");
            sb.append(this.mStateMachine);
            sb.append(" new:");
            sb.append(stateMachine);
            CamLog.d(sb.toString());
        }
        this.mStateMachine = stateMachine;
        if (this.mStateMachine != null) {
            this.mStateMachineForSavingRequest = this.mStateMachine;
        }
    }
    
    public void setTorchAndCommit(final boolean b) {
        if (!this.isPreCaptureOnGoing() && !this.isPreScanOnGoing()) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked on:");
                sb.append(b);
                CamLog.d(sb.toString());
            }
            Flash flashMode;
            if (b) {
                flashMode = Flash.LED_ON;
            }
            else {
                flashMode = Flash.LED_OFF;
            }
            this.setFlashMode(flashMode);
            this.mCameraController.commitParameters(this.mCameraSessionId);
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("Capturing for quick launch is on going, so this request was refused.");
        }
    }
    
    public void setUltraLowPower() {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        parameters.setPowerMode("ultra-low");
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void setVideoHdr(final VideoHdr videoHdr) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(videoHdr);
            sb.append(" prev:");
            sb.append(parameters.getVideoHdr());
            CamLog.d(sb.toString());
        }
        if (parameters.getVideoHdr() != videoHdr) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
            parameters.setVideoHdr(videoHdr);
            this.mCameraController.triggerRestartPreview(this.mCameraSessionId, false);
        }
    }
    
    public void setVideoSize(final VideoSize videoSize) {
        if (videoSize == null) {
            CamLog.e("Ilegal video size is specified.");
            return;
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(videoSize);
            sb.append(" prev:");
            sb.append(parameters.getVideoSize());
            sb.append(" video:");
            sb.append(this.mIsVideo);
            CamLog.d(sb.toString());
        }
        if (parameters.getVideoSize() != videoSize && this.mIsVideo) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        parameters.setVideoSize(videoSize);
        if (this.mIsVideo) {
            this.setPreviewSizeAndFpsRangeForVideo(parameters.getCameraId(), videoSize, parameters.getVideoHdr());
        }
    }
    
    public void setVideoStabilizer(final VideoStabilizer obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            sb.append(" video:");
            sb.append(this.mIsVideo);
            CamLog.d(sb.toString());
        }
        if (!obj.getValue().equals(parameters.getVideoStabilizer()) && this.mIsVideo) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        parameters.setVideoStabilizer(obj.getValue());
    }
    
    public void setWhiteBalance(final WhiteBalance obj) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked value:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        parameters.setWhiteBalance(obj.getValue());
    }
    
    public void setZoom(final float zoom) {
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return;
        }
        parameters.setZoom(zoom);
    }
    
    public void setZoomAndCommit(final float zoom) {
        this.setZoom(zoom);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }
    
    public void startFaceDetection() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.startFaceDetection(this.mCameraSessionId, this.mUiThreadHandler);
    }
    
    public void startFusionMonitoring() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.startFusionMonitoring(this.mUiThreadHandler);
    }
    
    public void startObjectTracking(final Rect obj, final CameraParameters.ObjectTrackingCallback objectTrackingCallback) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked position:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mCameraController.startObjectTracking(this.mCameraSessionId, this.mUiThreadHandler, obj, objectTrackingCallback);
    }
    
    public void startPreview() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.setRepeatingRequestInternal(this.mCameraSessionId, false);
    }
    
    public void startRecording() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        try {
            synchronized (this.mVideoRecorderLock) {
                if (this.mVideoRecorder != null) {
                    this.mVideoRecorder.start();
                    this.mIsRecording = true;
                }
                monitorexit(this.mVideoRecorderLock);
                this.changeProviderDeviceStatusToRecording();
            }
        }
        catch (final RecorderException cause) {
            CamLog.e("mMediaRecorder.start() fail.");
            this.releaseVideo();
            if (((RequestFactory.RequestBuilder)this.mLastVideoSavingRequest).getFilePath() != null) {
                if (StorageUtil.getStorageTypeFromPath(((RequestFactory.RequestBuilder)this.mLastVideoSavingRequest).getFilePath(), this.mApplicationContext) != Storage.StorageType.EXTERNAL_CARD) {
                    try {
                        final File file = new File(((RequestFactory.RequestBuilder)this.mLastVideoSavingRequest).getFilePath());
                        if (file.exists() && file.isFile() && !file.delete()) {
                            CamLog.e("videoFile.delete(): [Unable to delete empty video file.]");
                        }
                    }
                    catch (final Exception ex) {
                        CamLog.e("startRecording: [Unable to delete empty media file.]");
                    }
                }
                else {
                    final Uri searchDocumentSdCard = StorageUtil.searchDocumentSdCard(this.mApplicationContext, ((RequestFactory.RequestBuilder)this.mLastVideoSavingRequest).getFilePath());
                    if (searchDocumentSdCard != null) {
                        try {
                            if (!DocumentsContract.deleteDocument(this.mApplicationContext.getContentResolver(), searchDocumentSdCard)) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("deleteDocument: [delete failed.]");
                                sb.append(searchDocumentSdCard.toString());
                                CamLog.w(sb.toString());
                            }
                        }
                        catch (final FileNotFoundException | SecurityException ex2) {
                            CamLog.e("deleteDocument: [occurred Exception.]", (Throwable)ex2);
                        }
                    }
                }
            }
            throw new RuntimeException(cause);
        }
    }
    
    public void startSceneRecognition() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.startSceneRecognition(this.mCameraSessionId, this.mUiThreadHandler);
    }
    
    public void startSuperSlowMotion() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        if (!(this.mVideoRecorder instanceof SuperSlowRecorderController)) {
            throw new UnsupportedOperationException("Current recorder doesn't support slow motion");
        }
        try {
            ((SuperSlowRecorderController)this.mVideoRecorder).startSuperSlow();
        }
        catch (final RecorderException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("startSuperSlowMotion() failed:");
            sb.append(ex.getMessage());
            CamLog.e(sb.toString());
        }
    }
    
    public void stopAudioRecording() {
        if (this.mVideoRecorder != null) {
            this.mVideoRecorder.stopAudioRecording();
        }
    }
    
    public void stopFaceDetection() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.stopFaceDetection(this.mCameraSessionId);
    }
    
    public void stopFusionMonitoring() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.stopFusionMonitoring();
    }
    
    public void stopObjectTracking() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.stopObjectTracking(this.mCameraSessionId);
    }
    
    public void stopPreview() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked recording:");
            sb.append(this.isRecorderWorking());
            CamLog.d(sb.toString());
        }
        this.stopFaceDetection();
        this.stopSceneRecognition();
        if (!this.isRecorderWorking()) {
            this.mCameraController.stopPreview(this.mCameraSessionId);
        }
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.CLOSE_INITIAL_RESPONSE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.CLOSE_INITIAL_RESPONSE);
    }
    
    public void stopPreviewSynchronized() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked recording:");
            sb.append(this.isRecorderWorking());
            CamLog.d(sb.toString());
        }
        if (!this.isRecorderWorking()) {
            this.mCameraController.stopPreviewTaskSynchronized(this.mCameraSessionId);
        }
    }
    
    public Optional<Long> stopRecording(final boolean b) {
        if (b) {
            this.mBypassCameraController.awaitAllSnapshotDone();
        }
        this.cancelChangeProviderDeviceStatusToRecording();
        ((CameraStatusPublisher<DeviceStatus>)new EachCameraStatusPublisher(this.getApplicationContext(), this.getCameraId())).put(new DeviceStatus(DeviceStatus.Value.VIDEO_PREVIEW)).publish();
        synchronized (this.mVideoRecorderLock) {
            if (this.mVideoRecorder != null) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoked capturing:");
                    sb.append(b);
                    sb.append(" recording:");
                    sb.append(this.mVideoRecorder.isRecording());
                    sb.append(" paused:");
                    sb.append(this.mVideoRecorder.isPaused());
                    CamLog.d(sb.toString());
                }
                if (!this.mVideoRecorder.isRecording()) {
                    if (!this.mVideoRecorder.isPaused()) {
                        return Optional.empty();
                    }
                }
                try {
                    this.mVideoRecorder.stop();
                    return Optional.of(this.mVideoRecorder.getRecordingTimeMillis());
                }
                catch (final RecorderException ex) {
                    throw new RuntimeException("stopRecording():[Failed to stop MediaRecorder.]");
                }
            }
            if (CamLog.DEBUG) {
                CamLog.d("Recorder doesn't exists, so this request is refused.");
            }
            return Optional.empty();
        }
    }
    
    public void stopSceneRecognition() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.stopSceneRecognition(this.mCameraSessionId);
    }
    
    public void takePicture(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked datetaken:");
            sb.append(((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getDateTaken());
            CamLog.d(sb.toString());
        }
        final CameraParameters parameters = this.getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return;
        }
        if (this.isBypassCameraSupported()) {
            this.mBypassCameraController.requestSnapshot(this.mCameraSessionId, photoSavingRequestBuilder, parameters.getPredictiveCaptureNum());
        }
    }
    
    public void updateRecorder(final RequestFactory.VideoSavingRequestBuilder mLastVideoSavingRequest, final boolean b) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked recorder-is-ready:");
            sb.append(this.isRecorderReady());
            sb.append("shutter-sound-requested:");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        if (this.isRecorderReady()) {
            this.mLastVideoSavingRequest = mLastVideoSavingRequest;
            this.mVideoRecorder.setLocation(mLastVideoSavingRequest.mCommonStatus.location);
            this.mVideoRecorder.setOrientationHint(mLastVideoSavingRequest.mCommonStatus.orientation);
            this.mVideoRecorder.setMaxDurationMillis(mLastVideoSavingRequest.mVideoStatus.maxDurationMills);
            this.mVideoRecorder.setMaxFileSizeBytes(mLastVideoSavingRequest.mVideoStatus.maxFileSizeBytes);
            this.mVideoRecorder.setOutputFilePath(((RequestFactory.RequestBuilder)mLastVideoSavingRequest).getFilePath());
            this.mVideoRecorder.setUserSoundSetting(b);
        }
    }
    
    private class BypassCameraControllerCallbackImpl implements BypassCameraControllerCallback
    {
        private Runnable mSnapshotReadyDoneTask;
        final CameraDeviceHandler this$0;
        
        private BypassCameraControllerCallbackImpl(final CameraDeviceHandler this$0) {
            this.this$0 = this$0;
        }
        
        private void setPredictiveCaptureInfo(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, final int n, final int n2, final String saveTimeForCaptureGroup) {
            if (n2 > 1) {
                photoSavingRequestBuilder.setSaveTimeForCaptureGroup(saveTimeForCaptureGroup);
                photoSavingRequestBuilder.setCaptureIdForCaptureGourp(n2 - n - 1);
                if (n == 0) {
                    ((RequestFactory.RequestBuilder)photoSavingRequestBuilder).setSomcType(100);
                }
            }
        }
        
        private String toString(final BypassCamera.DisplayFlashColor displayFlashColor) {
            if (displayFlashColor == null) {
                return "null";
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(displayFlashColor.colorRed);
            sb.append(',');
            sb.append(displayFlashColor.colorGreen);
            sb.append(',');
            sb.append(displayFlashColor.colorBlue);
            return sb.toString();
        }
        
        @Override
        public void onCameraClosed() {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked pre-process:");
                sb.append(this.this$0.mPreProcessState);
                CamLog.d(sb.toString());
            }
            if (this.this$0.mPreProcessState != PreProcessState.NOT_STARTED && this.this$0.mPreProcessState != PreProcessState.PRE_CAPTURE_DONE) {
                this.this$0.changePreProcessStateTo(PreProcessState.PRE_CAPTURE_RELEASED);
            }
            else {
                this.this$0.changePreProcessStateTo(PreProcessState.NOT_STARTED);
            }
            this.this$0.mUiThreadHandler.removeCallbacks(this.mSnapshotReadyDoneTask);
            this.mSnapshotReadyDoneTask = null;
            this.this$0.mUiThreadHandler.post((Runnable)new CloseCameraDeviceNotificationTask());
        }
        
        @Override
        public void onPrepareBurstDone(final boolean b) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked success:");
                sb.append(b);
                CamLog.d(sb.toString());
            }
            this.this$0.mUiThreadHandler.post((Runnable)new Runnable(this, b) {
                final BypassCameraControllerCallbackImpl this$1;
                final boolean val$success;
                
                @Override
                public void run() {
                    if (this.this$1.this$0.mStateMachine != null) {
                        this.this$1.this$0.mStateMachine.onPrepareBurstDone(this.val$success);
                    }
                }
            });
        }
        
        @Override
        public void onShutterDone(int i, final int j, final boolean b) {
            final boolean debug = CamLog.DEBUG;
            final int n = 0;
            if (debug) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked captureId:");
                sb.append(i);
                sb.append(" captureNum:");
                sb.append(j);
                sb.append(" isAfSuccess:");
                sb.append(b);
                CamLog.d(sb.toString());
            }
            if (this.this$0.mPreProcessState == PreProcessState.PRE_CAPTURE_STARTED) {
                this.this$0.changePreProcessStateTo(PreProcessState.PRE_SHUTTER_DONE);
                if (this.this$0.isNeedCreatePreviewSession()) {
                    this.this$0.mCameraController.createPreviewSession(this.this$0.mCameraSessionId);
                }
            }
            final String format = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(new Date());
            RequestFactory.PhotoSavingRequestBuilder peekLastSavingPhotoRequest;
            RequestFactory.PhotoSavingRequestBuilder photoSavingRequest;
            for (i = n; i < j; ++i) {
                if (i == 0) {
                    peekLastSavingPhotoRequest = this.this$0.mBypassCameraController.peekLastSavingPhotoRequest();
                    this.setPredictiveCaptureInfo(peekLastSavingPhotoRequest, i, j, format);
                    this.this$0.mUiThreadHandler.post((Runnable)new ShutterDoneHandlerCallbackImpl(peekLastSavingPhotoRequest, j, b));
                }
                else {
                    photoSavingRequest = this.this$0.mStateMachine.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO);
                    this.setPredictiveCaptureInfo(photoSavingRequest, i, j, format);
                    this.this$0.mBypassCameraController.enqueueSavingPhotoRequest(photoSavingRequest);
                }
            }
        }
        
        @Override
        public void onSnapshotDone(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked requestId:");
                sb.append(((RequestFactory.RequestBuilder)photoSavingRequestBuilder).getRequestId());
                CamLog.d(sb.toString());
            }
            this.this$0.mUiThreadHandler.post((Runnable)new SnapshotDoneHandlerCallbackImpl(photoSavingRequestBuilder));
        }
        
        @Override
        public void onSnapshotReadyDone(final ExecutorService executorService, final boolean b, final boolean b2, final boolean b3, final BypassCamera.DisplayFlashColor displayFlashColor) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked pre-process:");
                sb.append(this.this$0.mPreProcessState);
                sb.append(" isHighQualityBurstAvailable:");
                sb.append(b);
                sb.append(" isAfSuccess:");
                sb.append(b2);
                sb.append(" requireFlash:");
                sb.append(b3);
                sb.append(" displayFlashColor:");
                sb.append(this.toString(displayFlashColor));
                CamLog.d(sb.toString());
            }
            this.mSnapshotReadyDoneTask = new Runnable(this, b2, b, b3, displayFlashColor) {
                final BypassCameraControllerCallbackImpl this$1;
                final BypassCamera.DisplayFlashColor val$displayFlashColor;
                final boolean val$isAfSuccess;
                final boolean val$isHighQualityBurstAvailable;
                final boolean val$requireFlash;
                
                @Override
                public void run() {
                    if (CamLog.DEBUG) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("SnapshotReadyDoneTask invoked pre-process:");
                        sb.append(this.this$1.this$0.mPreProcessState);
                        sb.append(" fast-capture:");
                        sb.append(this.this$1.this$0.mFastCaptureSetting);
                        CamLog.d(sb.toString());
                    }
                    if (this.this$1.this$0.mPreProcessState == PreProcessState.PRE_SCAN_STARTED) {
                        this.this$1.this$0.changePreProcessStateTo(PreProcessState.PRE_SCAN_DONE);
                        if (this.this$1.this$0.mFastCaptureSetting != FastCapture.LAUNCH_AND_CAPTURE && this.this$1.this$0.mStateMachine != null) {
                            this.this$1.this$0.mStateMachine.onInitialAutoFocusDone(this.val$isAfSuccess);
                        }
                        else {
                            ResearchUtil.getInstance().setTimeAfDone();
                            ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.FAST_CAPTURING_LAUNCH);
                            this.this$1.this$0.preCapture();
                        }
                    }
                    else if ((this.this$1.this$0.mPreProcessState == PreProcessState.NOT_STARTED || this.this$1.this$0.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) && this.this$1.this$0.mStateMachine != null) {
                        this.this$1.this$0.mStateMachine.onAutoFocusDone(this.val$isHighQualityBurstAvailable, this.val$isAfSuccess, this.val$requireFlash, this.val$displayFlashColor.colorRed, this.val$displayFlashColor.colorGreen, this.val$displayFlashColor.colorBlue);
                    }
                }
            };
            if (this.this$0.mPreProcessState == PreProcessState.PRE_SCAN_STARTED) {
                if (!executorService.isShutdown()) {
                    executorService.submit(this.mSnapshotReadyDoneTask);
                }
                else {
                    CamLog.w("BypassCameraRequestExecutor already Shutdown");
                }
            }
            else {
                this.this$0.mUiThreadHandler.post(this.mSnapshotReadyDoneTask);
            }
        }
        
        private class CloseCameraDeviceNotificationTask implements Runnable
        {
            final BypassCameraControllerCallbackImpl this$1;
            
            private CloseCameraDeviceNotificationTask(final BypassCameraControllerCallbackImpl this$1) {
                this.this$1 = this$1;
            }
            
            @Override
            public void run() {
                if (this.this$1.this$0.mStateMachine != null) {
                    this.this$1.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_CAMERA_DEVICE_CLOSED, new Object[0]);
                }
            }
        }
        
        private class ShutterDoneHandlerCallbackImpl implements Runnable
        {
            private final int mCaptureRequestNum;
            private final boolean mIsAfSuccess;
            private final RequestFactory.PhotoSavingRequestBuilder mRequest;
            final BypassCameraControllerCallbackImpl this$1;
            
            private ShutterDoneHandlerCallbackImpl(final BypassCameraControllerCallbackImpl this$1, final RequestFactory.PhotoSavingRequestBuilder mRequest, final int mCaptureRequestNum, final boolean mIsAfSuccess) {
                this.this$1 = this$1;
                this.mRequest = mRequest;
                this.mCaptureRequestNum = mCaptureRequestNum;
                this.mIsAfSuccess = mIsAfSuccess;
            }
            
            private void playSoundIfPossible(final StateMachine stateMachine) {
                if (stateMachine.getUserSetting().get(stateMachine.getCurrentCapturingMode(), UserSettingKey.SHUTTER_SOUND) != ShutterSound.OFF && !this.this$1.this$0.isRecording()) {
                    this.this$1.this$0.playShutterSound(1);
                }
            }
            
            private void updatePredictiveCaptureNumForResearchUtil(final StateMachine stateMachine) {
                if (stateMachine.getUserSetting().get(stateMachine.getCurrentCapturingMode(), UserSettingKey.PREDICTIVE_CAPTURE) == PredictiveCapture.OFF) {
                    ResearchUtil.getInstance().setPredictiveCaptureNum(0);
                }
                else {
                    ResearchUtil.getInstance().setPredictiveCaptureNum(this.mCaptureRequestNum);
                }
            }
            
            @Override
            public void run() {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ShutterDoneHandlerCallbackImpl invoked pre-process:");
                    sb.append(this.this$1.this$0.mPreProcessState);
                    CamLog.d(sb.toString());
                }
                if (CapturePerformanceLogger.get(this.mRequest) != null) {
                    CapturePerformanceLogger.get(this.mRequest).shutterDone = SystemClock.uptimeMillis();
                }
                if (this.this$1.this$0.mPreProcessState == PreProcessState.NOT_STARTED || this.this$1.this$0.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) {
                    if (this.this$1.this$0.mStateMachine != null) {
                        this.updatePredictiveCaptureNumForResearchUtil(this.this$1.this$0.mStateMachine);
                        this.this$1.this$0.mStateMachine.onShutterDone(this.mRequest, this.mCaptureRequestNum, this.mIsAfSuccess);
                        this.playSoundIfPossible(this.this$1.this$0.mStateMachine);
                    }
                    else if (this.this$1.this$0.mStateMachineForSavingRequest != null) {
                        this.updatePredictiveCaptureNumForResearchUtil(this.this$1.this$0.mStateMachineForSavingRequest);
                        this.playSoundIfPossible(this.this$1.this$0.mStateMachineForSavingRequest);
                    }
                }
            }
        }
        
        private class SnapshotDoneHandlerCallbackImpl implements Runnable
        {
            private final RequestFactory.PhotoSavingRequestBuilder localRequestBuilder;
            final BypassCameraControllerCallbackImpl this$1;
            
            private SnapshotDoneHandlerCallbackImpl(final BypassCameraControllerCallbackImpl this$1, final RequestFactory.PhotoSavingRequestBuilder localRequestBuilder) {
                this.this$1 = this$1;
                this.localRequestBuilder = localRequestBuilder;
            }
            
            @Override
            public void run() {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("SnapshotDoneHandlerCallbackImpl invoked pre-process:");
                    sb.append(this.this$1.this$0.mPreProcessState);
                    CamLog.d(sb.toString());
                }
                if (CapturePerformanceLogger.get(this.localRequestBuilder) != null) {
                    CapturePerformanceLogger.get(this.localRequestBuilder).snapshotDone = SystemClock.uptimeMillis();
                }
                TestEventSender.onPictureTaken();
                if (this.this$1.this$0.mPreProcessState == PreProcessState.PRE_SHUTTER_DONE) {
                    this.this$1.this$0.changePreProcessStateTo(PreProcessState.PRE_CAPTURE_DONE);
                    if (this.this$1.this$0.mStateMachine != null) {
                        this.this$1.this$0.mStateMachine.onPreTakePictureDone(this.localRequestBuilder);
                    }
                    else {
                        CamLog.i("Launch and capture is done before activity is started.");
                        this.this$1.this$0.mBypassCameraController.setPreCaptureResult(this.localRequestBuilder);
                    }
                }
                else if (this.this$1.this$0.mPreProcessState != PreProcessState.NOT_STARTED && this.this$1.this$0.mPreProcessState != PreProcessState.PRE_CAPTURE_DONE) {
                    this.localRequestBuilder.close();
                }
                else if (this.this$1.this$0.mStateMachine != null) {
                    this.this$1.this$0.mStateMachine.onTakePictureDone(this.localRequestBuilder);
                }
                else if (this.this$1.this$0.mStateMachineForSavingRequest != null) {
                    CamLog.i("Capture is done after activity is puased.");
                    this.this$1.this$0.mStateMachineForSavingRequest.onTakePictureDone(this.localRequestBuilder);
                }
                else {
                    CamLog.e("StateMachine doesn't exists, so captured photo cannot be saved.");
                }
                final Context access$1600 = this.this$1.this$0.getApplicationContext();
                if (access$1600 != null) {
                    final EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher(access$1600, this.this$1.this$0.getCameraId());
                    DeviceStatus.Value value;
                    if (this.this$1.this$0.mIsVideo) {
                        value = DeviceStatus.Value.VIDEO_RECORDING;
                    }
                    else {
                        value = DeviceStatus.Value.STILL_PREVIEW;
                    }
                    ((CameraStatusPublisher<DeviceStatus>)eachCameraStatusPublisher).put(new DeviceStatus(value)).publish();
                }
            }
        }
    }
    
    private class CameraControllerCallbackImpl implements CameraControllerCallback
    {
        final CameraDeviceHandler this$0;
        
        private CameraControllerCallbackImpl(final CameraDeviceHandler this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCropRegionReady() {
            if (CamLog.DEBUG) {
                CamLog.d("invoked");
            }
            if (this.this$0.mStateMachine != null) {
                this.this$0.mStateMachine.onCropRegionReady();
            }
        }
        
        @Override
        public void onDeviceError(final CameraSessionId obj, final ErrorCode obj2) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked sessionId:");
                sb.append(obj);
                sb.append(" error:");
                sb.append(obj2);
                CamLog.d(sb.toString());
            }
            this.this$0.mCameraDeviceThreadHandler.post((Runnable)new OnErrorTask(obj));
            this.this$0.mUiThreadHandler.post((Runnable)new Runnable(this, obj2) {
                final CameraControllerCallbackImpl this$1;
                final ErrorCode val$error;
                
                @Override
                public void run() {
                    if (this.this$1.this$0.mStateMachine != null) {
                        this.this$1.this$0.mStateMachine.onDeviceError(this.val$error);
                    }
                }
            });
        }
        
        @Override
        public void onFaceDetected(final CameraParameters.FaceDetectionResult obj) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked result:");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (this.this$0.mStateMachine != null && obj != null) {
                ResearchUtil.getInstance().setFaceNum(obj.extFaceList.size());
                if (this.this$0.isRecording()) {
                    ResearchUtil.getInstance().setRecordingMaxFaceNum(obj.extFaceList.size());
                }
                this.this$0.mStateMachine.onFaceDetected(obj);
                return;
            }
            ResearchUtil.getInstance().clearFaceNum();
        }
        
        @Override
        public void onFusionResultChanged(final CameraParameters.FusionResult obj) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked result:");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (obj == null) {
                return;
            }
            if (this.this$0.mStateMachine != null) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_FUSION_CONDITION_CHANGED, obj);
            }
        }
        
        @Override
        public void onOpenCameraRequested(final CameraSessionId obj) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked sessionId:");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            this.this$0.mUiThreadHandler.post((Runnable)new OpenCameraDeviceNotificationTask(obj));
        }
        
        @Override
        public void onPreviewFrameUpdated(final ByteBuffer byteBuffer, final int n, final Rect obj) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked format:");
                sb.append(n);
                sb.append(" rect:");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (this.this$0.mStateMachine != null && n == 17) {
                final byte[] dst = new byte[byteBuffer.remaining()];
                byteBuffer.get(dst);
                byteBuffer.rewind();
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_ONE_PREVIEW_FRAME_UPDATED, dst, n, obj);
            }
        }
        
        @Override
        public void onReflected(final CameraSessionId obj) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked sessionId:");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            this.this$0.mBypassCameraController.requestSnapshotReady(obj);
        }
        
        @Override
        public void onSceneModeChanged(final CameraParameters.SceneRecognitionResult obj) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked result:");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (obj == null) {
                return;
            }
            if (this.this$0.mStateMachine != null) {
                this.this$0.mStateMachine.onSceneModeChanged(obj);
            }
            final LocalResearchUtil instance = LocalResearchUtil.getInstance();
            String recognizedScene;
            if (obj.isMacroRange) {
                recognizedScene = "macro";
            }
            else if (obj.sceneMode == null) {
                recognizedScene = CameraParameterConverter.SceneMode.AUTO.toString();
            }
            else {
                recognizedScene = obj.sceneMode.toString();
            }
            instance.setRecognizedScene(recognizedScene);
        }
        
        @Override
        public void onSessionDisconnected(final CameraSessionId obj) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked sessionId:");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            this.this$0.mCameraDeviceThreadHandler.post((Runnable)new OnDisconnectedTask(obj));
            this.this$0.mUiThreadHandler.post((Runnable)new Runnable(this) {
                final CameraControllerCallbackImpl this$1;
                
                @Override
                public void run() {
                    if (this.this$1.this$0.mStateMachine != null) {
                        this.this$1.this$0.mStateMachine.onDeviceError(ErrorCode.ERROR_ON_CAMERA_DISCONNECTION);
                    }
                }
            });
        }
        
        @WorkerThread
        private class OnDisconnectedTask extends CameraDeviceAccessTask
        {
            final CameraControllerCallbackImpl this$1;
            
            private OnDisconnectedTask(final CameraControllerCallbackImpl this$1, final CameraSessionId cameraSessionId) {
                this.this$1 = this$1;
                super(cameraSessionId);
            }
            
            public void doCameraDeviceAccess() {
                this.this$1.this$0.mCameraController.setCameraDeviceStatus(CameraDeviceStatus.STATUS_EVICTED);
                this.this$1.this$0.mUiThreadHandler.post((Runnable)new Runnable(this) {
                    final OnDisconnectedTask this$2;
                    
                    @Override
                    public void run() {
                        if (this.this$2.this$1.this$0.isRecorderWorking()) {
                            try {
                                this.this$2.this$1.this$0.mVideoRecorder.stopOnCameraError();
                            }
                            catch (final RecorderException ex) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Stop recording by Camera eviction fails.");
                                sb.append(ex.getMessage());
                                CamLog.e(sb.toString());
                            }
                        }
                        this.this$2.this$1.this$0.closeCamera();
                    }
                });
            }
            
            @Override
            protected boolean verifyCameraDeviceStatus() {
                switch (CameraDeviceHandler$6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$1.this$0.mCameraController.getCameraDeviceStatus().ordinal()]) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed due to wrong status in OnDisconnectedTask. status: ");
                        sb.append(this.this$1.this$0.mCameraController.getCameraDeviceStatus());
                        throw new IllegalStateException(sb.toString());
                    }
                    case 4:
                    case 5: {
                        return false;
                    }
                    case 1:
                    case 2:
                    case 3: {
                        return !((CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested();
                    }
                }
            }
        }
        
        @WorkerThread
        private class OnErrorTask extends CameraDeviceAccessTask
        {
            final CameraControllerCallbackImpl this$1;
            
            private OnErrorTask(final CameraControllerCallbackImpl this$1, final CameraSessionId cameraSessionId) {
                this.this$1 = this$1;
                super(cameraSessionId);
            }
            
            public void doCameraDeviceAccess() {
                this.this$1.this$0.mCameraController.setCameraDeviceStatus(CameraDeviceStatus.STATUS_ERROR);
                this.this$1.this$0.mUiThreadHandler.post((Runnable)new Runnable(this) {
                    final OnErrorTask this$2;
                    
                    @Override
                    public void run() {
                        if (this.this$2.this$1.this$0.isRecorderWorking()) {
                            try {
                                this.this$2.this$1.this$0.mVideoRecorder.stopOnCameraError();
                            }
                            catch (final RecorderException ex) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Stop recording by Camera error fail.");
                                sb.append(ex.getMessage());
                                CamLog.e(sb.toString());
                            }
                        }
                        this.this$2.this$1.this$0.closeCamera();
                    }
                });
            }
            
            @Override
            protected boolean verifyCameraDeviceStatus() {
                switch (CameraDeviceHandler$6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$1.this$0.mCameraController.getCameraDeviceStatus().ordinal()]) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed due to wrong status in OnErrorTask. status: ");
                        sb.append(this.this$1.this$0.mCameraController.getCameraDeviceStatus());
                        throw new IllegalStateException(sb.toString());
                    }
                    case 4:
                    case 5: {
                        return false;
                    }
                    case 1:
                    case 2:
                    case 3: {
                        if (((CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                            CamLog.d("OnErrorTask : CloseCameraTask is already requested.");
                            return false;
                        }
                        return true;
                    }
                }
            }
        }
        
        private class OpenCameraDeviceNotificationTask implements Runnable
        {
            private final CameraSessionId mSessionId;
            final CameraControllerCallbackImpl this$1;
            
            private OpenCameraDeviceNotificationTask(final CameraControllerCallbackImpl this$1, final CameraSessionId mSessionId) {
                this.this$1 = this$1;
                this.mSessionId = mSessionId;
            }
            
            @Override
            public void run() {
                if (this.this$1.this$0.mStateMachine != null) {
                    this.this$1.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_CAMERA_DEVICE_OPENED, this.mSessionId);
                }
            }
        }
    }
    
    abstract static class CameraDeviceAccessTask implements Runnable
    {
        private static final boolean IS_DUMP_EXCEPTION_TASK_INFO_ENABLED = true;
        private static final boolean IS_DUMP_REJECTED_TASK_INFO_ENABLED = false;
        private final DumpInfo mDumpInfoAtConstruct;
        private final boolean mIsBelongedToSession;
        protected final CountDownLatch mLatch;
        private PerfLog mPerfLog;
        private final CameraSessionId mSessionId;
        
        CameraDeviceAccessTask() {
            this(null, false);
        }
        
        CameraDeviceAccessTask(final CameraSessionId cameraSessionId) {
            this(cameraSessionId, true);
        }
        
        private CameraDeviceAccessTask(final CameraSessionId mSessionId, final boolean mIsBelongedToSession) {
            this.mPerfLog = null;
            this.mLatch = new CountDownLatch(1);
            this.mSessionId = mSessionId;
            this.mIsBelongedToSession = mIsBelongedToSession;
            if (CamLog.DEBUG) {
                this.mDumpInfoAtConstruct = new DumpInfo();
            }
            else {
                this.mDumpInfoAtConstruct = null;
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("REQUEST:");
                sb.append(this.getClass().getSimpleName());
                sb.append(" sessionId:");
                sb.append(this.mSessionId);
                CamLog.d(sb.toString());
            }
        }
        
        private CountDownLatch getLatch() {
            return this.mLatch;
        }
        
        protected abstract void doCameraDeviceAccess();
        
        protected CameraSessionInfo getOpenCloseStatusInfo() {
            Object openCloseStatusInfo;
            if ((openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(this.mSessionId)) == null) {
                openCloseStatusInfo = new CameraSessionInfo(null);
                ((CameraSessionInfo)openCloseStatusInfo).setRequested(OpenCloseRequestStatus.BYPASS_CAMERA_CLOSING);
                ((CameraSessionInfo)openCloseStatusInfo).setPerformed(OpenClosePerformStatus.BYPASS_CAMERA_CLOSED);
            }
            return (CameraSessionInfo)openCloseStatusInfo;
        }
        
        protected CameraSessionId getSessionId() {
            return this.mSessionId;
        }
        
        protected void postCameraDeviceAccess() {
        }
        
        protected void removeOpenCloseStatusInfo() {
            CameraSessionInfo.removeOpenCloseStatusInfo(this.mSessionId);
        }
        
        @Override
        public final void run() {
            try {
                final boolean b = !this.mIsBelongedToSession || !this.getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
                if (this.verifyCameraDeviceStatus() && b) {
                    if (CamLog.DEBUG) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("START:");
                        sb.append(this.getClass().getSimpleName());
                        sb.append(" sessionId:");
                        sb.append(this.mSessionId);
                        CamLog.d(sb.toString());
                    }
                    if (this.mPerfLog != null) {
                        this.mPerfLog.begin();
                    }
                    this.doCameraDeviceAccess();
                    if (this.mPerfLog != null) {
                        this.mPerfLog.end();
                    }
                    if (CamLog.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("END:");
                        sb2.append(this.getClass().getSimpleName());
                        sb2.append(" sessionId:");
                        sb2.append(this.mSessionId);
                        CamLog.d(sb2.toString());
                    }
                }
                else if (CamLog.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("REJECTED:");
                    sb3.append(this.getClass().getSimpleName());
                    sb3.append(" sessionId:");
                    sb3.append(this.mSessionId);
                    CamLog.d(sb3.toString());
                }
                this.postCameraDeviceAccess();
            }
            catch (final RuntimeException ex) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("EXCEPTION:");
                    sb4.append(this.getClass().getSimpleName());
                    sb4.append(" sessionId:");
                    sb4.append(this.mSessionId);
                    CamLog.d(sb4.toString());
                    final DumpInfo mDumpInfoAtConstruct = this.mDumpInfoAtConstruct;
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("request ");
                    sb5.append(this.getClass().getSimpleName());
                    mDumpInfoAtConstruct.dump(sb5.toString());
                    final DumpInfo dumpInfo = new DumpInfo();
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("performed ");
                    sb6.append(this.getClass().getSimpleName());
                    dumpInfo.dump(sb6.toString());
                }
                throw ex;
            }
        }
        
        protected void setPerformancefLog(final PerfLog mPerfLog) {
            this.mPerfLog = mPerfLog;
        }
        
        protected abstract boolean verifyCameraDeviceStatus();
        
        private class DumpInfo
        {
            private final StackTraceElement[] stackTrace;
            private final String status;
            final CameraDeviceAccessTask this$0;
            
            private DumpInfo(final CameraDeviceAccessTask this$0) {
                this.this$0 = this$0;
                this.stackTrace = Thread.currentThread().getStackTrace();
                final CameraSessionInfo openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(this$0.mSessionId);
                if (openCloseStatusInfo == null) {
                    this.status = "CameraSession info is null. So, camera is closed";
                }
                else {
                    this.status = openCloseStatusInfo.info();
                }
            }
            
            private void dump(final String str) {
                final StringBuilder sb = new StringBuilder();
                sb.append("[status dump] START ");
                sb.append(str);
                CamLog.d(sb.toString());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("[status dump]   status:");
                sb2.append(this.status);
                CamLog.d(sb2.toString());
                CamLog.d("[status dump]   trace:");
                for (int i = 1; i < this.stackTrace.length; ++i) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("[status dump]     at ");
                    sb3.append(this.stackTrace[i].getClassName());
                    sb3.append("#");
                    sb3.append(this.stackTrace[i].getMethodName());
                    CamLog.d(sb3.toString());
                }
                CamLog.d("[status dump] END");
            }
        }
    }
    
    class CameraDeviceHandlerInquirer
    {
        final CameraDeviceHandler this$0;
        
        CameraDeviceHandlerInquirer(final CameraDeviceHandler this$0) {
            this.this$0 = this$0;
        }
        
        boolean awaitLoadSettingsThread() {
            return this.this$0.awaitLoadSettingsThread();
        }
        
        void changePreProcessStateTo(final PreProcessState preProcessState) {
            this.this$0.changePreProcessStateTo(preProcessState);
        }
        
        Handler getDeviceThreadHandler() {
            return this.this$0.mCameraDeviceThreadHandler;
        }
        
        CameraParameters getParameters(final CameraSessionId cameraSessionId) {
            return this.this$0.getParameters(cameraSessionId);
        }
        
        PreProcessState getPreProcessState() {
            return this.this$0.mPreProcessState;
        }
        
        boolean isIgnoreCameraError() {
            return this.this$0.mActivityIsInForeground;
        }
        
        boolean isNeedCreatePreviewSession() {
            return this.this$0.isNeedCreatePreviewSession();
        }
        
        public boolean isPreCaptureOnGoing() {
            return this.this$0.mPreProcessState == PreProcessState.PRE_CAPTURE_STARTED || this.this$0.mPreProcessState == PreProcessState.PRE_SHUTTER_DONE;
        }
        
        public boolean isPreScanOnGoing() {
            return this.this$0.mPreProcessState == PreProcessState.PRE_SCAN_STARTED;
        }
        
        boolean isRecording() {
            return this.this$0.isRecording();
        }
        
        boolean isSnapshotRunning() {
            return this.this$0.mBypassCameraController.isSnapshotRunning();
        }
        
        boolean isVideo() {
            return this.this$0.mIsVideo;
        }
        
        void postCameraDeviceThread(final CameraDeviceAccessTask cameraDeviceAccessTask) {
            this.this$0.runOnCameraDeviceThread(cameraDeviceAccessTask);
        }
        
        void postCameraDeviceThreadSync(final CameraDeviceAccessTask cameraDeviceAccessTask) {
            this.this$0.runOnCameraDeviceThreadSync(cameraDeviceAccessTask);
        }
        
        void prepareCaptureImageReader() {
            this.this$0.prepareCaptureImageReader(null);
        }
        
        void releaseRecorderOnCameraClosed() {
            if (CamLog.DEBUG) {
                CamLog.d("invoked");
            }
            this.this$0.releaseRecorderOnCameraClosed();
        }
    }
    
    enum CameraDeviceStatus
    {
        private static final CameraDeviceStatus[] $VALUES;
        
        STATUS_ERROR, 
        STATUS_EVICTED, 
        STATUS_OPENED, 
        STATUS_READY, 
        STATUS_RELEASED;
        
        static {
            $VALUES = new CameraDeviceStatus[] { CameraDeviceStatus.STATUS_RELEASED, CameraDeviceStatus.STATUS_OPENED, CameraDeviceStatus.STATUS_READY, CameraDeviceStatus.STATUS_EVICTED, CameraDeviceStatus.STATUS_ERROR };
        }
    }
    
    public static class CameraSessionId
    {
        private static Object sIdLock;
        private static int sLastId;
        private final String mTag;
        
        static {
            CameraSessionId.sIdLock = new Object();
        }
        
        public CameraSessionId() {
            this.mTag = makeTag();
        }
        
        private static String makeTag() {
            synchronized (CameraSessionId.sIdLock) {
                return Integer.toString(++CameraSessionId.sLastId);
            }
        }
        
        @Override
        public String toString() {
            return this.mTag;
        }
    }
    
    static class CameraSessionInfo
    {
        private final CameraInfo mCameraInfo;
        private final CameraParameters mCameraParameters;
        private boolean mIsCameraError;
        private boolean mIsCameraEvicted;
        private boolean mIsOtherError;
        private OpenClosePerformStatus mPerformed;
        private OpenCloseRequestStatus mRequested;
        
        CameraSessionInfo(final CameraInfo.CameraId cameraId) {
            this.mIsCameraEvicted = false;
            this.mIsCameraError = false;
            this.mIsOtherError = false;
            this.mCameraInfo = new CameraInfo();
            this.mRequested = OpenCloseRequestStatus.NONE;
            this.mPerformed = OpenClosePerformStatus.NONE;
            this.mCameraParameters = new CameraParameters(cameraId);
        }
        
        static void addOpenCloseStatusInfo(final CameraSessionId cameraSessionId, final CameraSessionInfo cameraSessionInfo) {
            synchronized (CameraDeviceHandler.sCameraSessionInfoMap) {
                CameraDeviceHandler.sCameraSessionInfoMap.put(cameraSessionId, cameraSessionInfo);
            }
        }
        
        public static void dump(final StringBuilder sb) {
            synchronized (CameraDeviceHandler.sCameraSessionInfoMap) {
                for (final Map.Entry<Object, V> entry : CameraDeviceHandler.sCameraSessionInfoMap.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(' ');
                    sb.append(((CameraSessionInfo)entry.getValue()).info());
                    sb.append('\n');
                }
            }
        }
        
        static CameraSessionInfo getOpenCloseStatusInfo(final CameraSessionId cameraSessionId) {
            synchronized (CameraDeviceHandler.sCameraSessionInfoMap) {
                return CameraDeviceHandler.sCameraSessionInfoMap.get(cameraSessionId);
            }
        }
        
        private String info() {
            synchronized (this) {
                final StringBuilder sb = new StringBuilder();
                sb.append("[");
                sb.append(this.mRequested.name());
                sb.append("|");
                sb.append(this.mPerformed.name());
                sb.append("|");
                sb.append(this.mIsCameraEvicted);
                sb.append("|");
                sb.append(this.mIsCameraError);
                sb.append("|");
                sb.append(this.mIsOtherError);
                sb.append("]");
                return sb.toString();
            }
        }
        
        static void removeOpenCloseStatusInfo(final CameraSessionId cameraSessionId) {
            synchronized (CameraDeviceHandler.sCameraSessionInfoMap) {
                if (CameraDeviceHandler.sCameraSessionInfoMap.containsKey(cameraSessionId)) {
                    CameraDeviceHandler.sCameraSessionInfoMap.remove(cameraSessionId);
                }
            }
        }
        
        CameraInfo getCameraInfo() {
            return this.mCameraInfo;
        }
        
        CameraParameters getParameters() {
            return this.mCameraParameters;
        }
        
        boolean isCloseBypassCameraTaskPerformed() {
            synchronized (this) {
                return CameraDeviceHandler$6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$OpenClosePerformStatus[this.mPerformed.ordinal()] == 1;
            }
        }
        
        boolean isCloseBypassCameraTaskRequested() {
            synchronized (this) {
                return CameraDeviceHandler$6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$OpenCloseRequestStatus[this.mRequested.ordinal()] == 2;
            }
        }
        
        boolean isCloseCameraTaskRequested() {
            synchronized (this) {
                switch (CameraDeviceHandler$6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$OpenCloseRequestStatus[this.mRequested.ordinal()]) {
                    default: {
                        return false;
                    }
                    case 1:
                    case 2: {
                        return true;
                    }
                }
            }
        }
        
        boolean isErrorCaused() {
            monitorenter(this);
            final boolean b = true;
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append("Error caused by evicted:");
                sb.append(this.mIsCameraEvicted);
                sb.append(" deviceError:");
                sb.append(this.mIsCameraError);
                sb.append(" otherError:");
                sb.append(this.mIsOtherError);
                CamLog.i(sb.toString());
                boolean b2 = b;
                if (!this.mIsCameraEvicted) {
                    b2 = b;
                    if (!this.mIsCameraError) {
                        b2 = (this.mIsOtherError && b);
                    }
                }
                return b2;
            }
            finally {
                monitorexit(this);
            }
        }
        
        boolean isOpenBypassCameraTaskPerformed() {
            synchronized (this) {
                return CameraDeviceHandler$6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$OpenClosePerformStatus[this.mPerformed.ordinal()] != 2;
            }
        }
        
        boolean isOpenCameraTaskPerformed() {
            synchronized (this) {
                switch (CameraDeviceHandler$6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$OpenClosePerformStatus[this.mPerformed.ordinal()]) {
                    default: {
                        return true;
                    }
                    case 2:
                    case 3: {
                        return false;
                    }
                }
            }
        }
        
        void setCameraError() {
            synchronized (this) {
                this.mIsCameraError = true;
            }
        }
        
        void setCameraEvicted() {
            synchronized (this) {
                this.mIsCameraEvicted = true;
            }
        }
        
        void setOtherError() {
            synchronized (this) {
                this.mIsOtherError = true;
            }
        }
        
        void setPerformed(final OpenClosePerformStatus mPerformed) {
            synchronized (this) {
                this.mPerformed = mPerformed;
            }
        }
        
        void setRequested(final OpenCloseRequestStatus mRequested) {
            synchronized (this) {
                this.mRequested = mRequested;
            }
        }
    }
    
    private class CloseBypassCameraTimeoutTask implements Runnable
    {
        private final CameraSessionId mSessionId;
        final CameraDeviceHandler this$0;
        
        private CloseBypassCameraTimeoutTask(final CameraDeviceHandler this$0, final CameraSessionId mSessionId) {
            this.this$0 = this$0;
            this.mSessionId = mSessionId;
        }
        
        @Override
        public void run() {
            if (this.this$0.mActivityIsInForeground) {
                return;
            }
            final CameraSessionInfo openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(this.mSessionId);
            if (openCloseStatusInfo == null) {
                return;
            }
            if (openCloseStatusInfo.isCloseBypassCameraTaskRequested() && !openCloseStatusInfo.isCloseBypassCameraTaskPerformed()) {
                if (this.this$0.mCameraDeviceThreadHandler != null) {
                    this.this$0.mCameraDeviceThreadHandler.getLooper().dump((Printer)new Printer(this) {
                        final CloseBypassCameraTimeoutTask this$1;
                        
                        public void println(final String s) {
                            CamLog.e("CloseBypassCameraTimeoutTask", s);
                        }
                    }, "");
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("\n========== TIMEOUT ==========\n");
                this.this$0.dumpStatus(sb);
                sb.append("=============================");
                CamLog.e("CloseBypassCameraTimeoutTask", sb.toString().replace(',', '\n'));
                throw new RuntimeException("CloseBypassCameraTimeoutTask: The camera app keeps holding the camera hardware resources.");
            }
        }
    }
    
    public enum ErrorCode
    {
        private static final ErrorCode[] $VALUES;
        
        ERROR_ON_CAMERA_DISCONNECTION, 
        ERROR_ON_CAMERA_ERROR, 
        ERROR_ON_CAPTURE_FAILED, 
        ERROR_ON_CONFIGURE_FAILED;
        
        static {
            $VALUES = new ErrorCode[] { ErrorCode.ERROR_ON_CAMERA_ERROR, ErrorCode.ERROR_ON_CAMERA_DISCONNECTION, ErrorCode.ERROR_ON_CAPTURE_FAILED, ErrorCode.ERROR_ON_CONFIGURE_FAILED };
        }
    }
    
    private class FastCaptureOrientation extends OrientationEventListener
    {
        private int mOrientation;
        final CameraDeviceHandler this$0;
        
        private FastCaptureOrientation(final CameraDeviceHandler this$0, final Context context) {
            this.this$0 = this$0;
            super(context);
            this.mOrientation = -1;
        }
        
        private int getOrientation() {
            final int normalizedRotation = RotationUtil.getNormalizedRotation(this.mOrientation);
            final CameraInfo cameraInfo = this.this$0.getCameraInfo();
            int n = 0;
            switch (CameraDeviceHandler$6.$SwitchMap$com$sonyericsson$android$camera$device$CameraInfo$CameraId[cameraInfo.facing.ordinal()]) {
                default: {
                    n = (cameraInfo.orientation + normalizedRotation) % 360;
                    break;
                }
                case 2: {
                    n = (cameraInfo.orientation + 360 - normalizedRotation) % 360;
                    break;
                }
                case 1: {
                    n = (cameraInfo.orientation + normalizedRotation) % 360;
                    break;
                }
            }
            return n;
        }
        
        public void onOrientationChanged(final int mOrientation) {
            this.mOrientation = mOrientation;
        }
    }
    
    public interface ImageReaderInitializedCallback
    {
        void onInitialized();
    }
    
    private class InitControllerTask extends CameraDeviceAccessTask
    {
        final CameraDeviceHandler this$0;
        
        private InitControllerTask(final CameraDeviceHandler this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        protected void doCameraDeviceAccess() {
            this.this$0.mBypassCameraController = new BypassCameraController(this.this$0.mApplicationContext, (BypassCameraController.BypassCameraControllerCallback)new BypassCameraControllerCallbackImpl(), this.this$0.new CameraDeviceHandlerInquirer());
            this.this$0.mCameraController = new CameraController(this.this$0.mApplicationContext, (CameraController.CameraControllerCallback)new CameraControllerCallbackImpl(), this.this$0.new CameraDeviceHandlerInquirer());
        }
        
        public void postCameraDeviceAccess() {
            this.mLatch.countDown();
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return true;
        }
    }
    
    private class LoadNativeLibraryTask extends CameraDeviceAccessTask
    {
        final CameraDeviceHandler this$0;
        
        private LoadNativeLibraryTask(final CameraDeviceHandler this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        protected void doCameraDeviceAccess() {
            if (PlatformCapability.isBypassCameraSupported()) {
                BypassCamera.loadNativeLibrary();
            }
            PlatformCapability.awaitPrepare();
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return true;
        }
    }
    
    private class LoadSettingsThread extends Thread
    {
        private final CapturingMode mCapturingMode;
        private final UserSettings mUserSettings;
        final CameraDeviceHandler this$0;
        
        private LoadSettingsThread(final CameraDeviceHandler this$0, final CapturingMode mCapturingMode, final UserSettings mUserSettings) {
            this.this$0 = this$0;
            this.mCapturingMode = mCapturingMode;
            this.mUserSettings = mUserSettings;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("LoadSettingsThread invoked  casuCapture:");
                sb.append(this.this$0.mFastCaptureSetting);
                sb.append(" cameraId:");
                sb.append(this.mCapturingMode.getCameraId());
                CamLog.d(sb.toString());
            }
            if (this.this$0.mFastCaptureSetting == FastCapture.LAUNCH_AND_CAPTURE) {
                if (GeotagManager.isGeoTagEnabled((Geotag)this.mUserSettings.get(this.mCapturingMode, UserSettingKey.GEO_TAG), this.this$0.getApplicationContext())) {
                    this.this$0.mGeotagManager = new GeotagManager(this.this$0.getApplicationContext());
                    this.this$0.mGeotagManager.assignResource();
                    this.this$0.runOnCameraDeviceThread((CameraDeviceAccessTask)new CameraDeviceAccessTask(this, null) {
                        final LoadSettingsThread this$1;
                        
                        public void doCameraDeviceAccess() {
                            if (this.this$1.this$0.getApplicationContext() != null && this.this$1.this$0.mGeotagManager != null) {
                                this.this$1.this$0.mGeotagManager.startLocationUpdates(LocationSettingsReader.isLocationProviderAllowed(this.this$1.this$0.getApplicationContext(), "gps"), LocationSettingsReader.isLocationProviderAllowed(this.this$1.this$0.getApplicationContext(), "network"));
                            }
                            else {
                                CamLog.d("Camera has been released.");
                            }
                        }
                        
                        @Override
                        protected boolean verifyCameraDeviceStatus() {
                            return true;
                        }
                    });
                }
                this.this$0.mFastCaptureOrientation = new FastCaptureOrientation(this.this$0.getApplicationContext());
                this.this$0.mFastCaptureOrientation.enable();
            }
        }
    }
    
    public interface OnPreviewStartedListener
    {
        void onPreviewStarted(final CameraSessionId p0);
    }
    
    enum OpenClosePerformStatus
    {
        private static final OpenClosePerformStatus[] $VALUES;
        
        BYPASS_CAMERA_CLOSED, 
        BYPASS_CAMERA_OPENED, 
        CAMERA_CLOSED, 
        CAMERA_OPENED, 
        NONE;
        
        static {
            $VALUES = new OpenClosePerformStatus[] { OpenClosePerformStatus.NONE, OpenClosePerformStatus.BYPASS_CAMERA_OPENED, OpenClosePerformStatus.CAMERA_OPENED, OpenClosePerformStatus.CAMERA_CLOSED, OpenClosePerformStatus.BYPASS_CAMERA_CLOSED };
        }
    }
    
    enum OpenCloseRequestStatus
    {
        private static final OpenCloseRequestStatus[] $VALUES;
        
        BYPASS_CAMERA_CLOSING, 
        BYPASS_CAMERA_OPENING, 
        CAMERA_CLOSING, 
        CAMERA_OPENING, 
        NONE;
        
        static {
            $VALUES = new OpenCloseRequestStatus[] { OpenCloseRequestStatus.NONE, OpenCloseRequestStatus.BYPASS_CAMERA_OPENING, OpenCloseRequestStatus.CAMERA_OPENING, OpenCloseRequestStatus.CAMERA_CLOSING, OpenCloseRequestStatus.BYPASS_CAMERA_CLOSING };
        }
    }
    
    public enum PreProcessState
    {
        private static final PreProcessState[] $VALUES;
        
        NOT_STARTED, 
        PRE_CAPTURE_DONE, 
        PRE_CAPTURE_RELEASED, 
        PRE_CAPTURE_STARTED, 
        PRE_SCAN_DONE, 
        PRE_SCAN_STARTED, 
        PRE_SHUTTER_DONE;
        
        static {
            $VALUES = new PreProcessState[] { PreProcessState.NOT_STARTED, PreProcessState.PRE_SCAN_STARTED, PreProcessState.PRE_SCAN_DONE, PreProcessState.PRE_CAPTURE_STARTED, PreProcessState.PRE_SHUTTER_DONE, PreProcessState.PRE_CAPTURE_DONE, PreProcessState.PRE_CAPTURE_RELEASED };
        }
    }
}
