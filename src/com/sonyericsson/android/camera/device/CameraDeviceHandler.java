package com.sonyericsson.android.camera.device;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.support.annotation.WorkerThread;
import android.util.ArrayMap;
import android.util.Printer;
import android.view.OrientationEventListener;
import android.view.Surface;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import com.sonyericsson.android.camera.device.BypassCameraController;
import com.sonyericsson.android.camera.device.CameraController;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonyericsson.android.camera.recorder.RecorderException;
import com.sonyericsson.android.camera.recorder.RecorderFactory;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.recorder.superslowrecorder.OnSuperSlowRecordingFinishedListener;
import com.sonyericsson.android.camera.recorder.superslowrecorder.SuperSlowRecorderController;
import com.sonyericsson.android.camera.recorder.utility.Accessor;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.LastSettings;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.CapturePerformanceLogger;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.location.LocationSettingsReader;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import com.sonyericsson.cameracommon.status.EachCameraStatusPublisher;
import com.sonyericsson.cameracommon.status.eachcamera.DeviceStatus;
import com.sonyericsson.cameracommon.status.eachcamera.PhotoLight;
import com.sonyericsson.cameracommon.status.eachcamera.SlowMotion;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameracommon.utility.RecordingUtil;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonymobile.cameracommon.research.parameters.ShootingLabel;
import com.sonymobile.cameracommon.testevent.TestEventSender;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraDeviceHandler {
    static final long CLOSE_BYPASS_CAMERA_TIMEOUT_MILLIS = 100000;
    private static final long DELAY_STATUS_PROVIDER_UPDATE_UNTIL_RECORDING_STARTED_MILLIS = 3000;
    private static final long TIMEOUT_WAIT_FOR_PARAMETERS_TO_BE_REFLECTED_MILLIS = 5000;
    private Context mApplicationContext;
    private BypassCameraController mBypassCameraController;
    private CameraActionSound mCameraActionSound;
    private CameraController mCameraController;
    private final Handler mCameraDeviceThreadHandler;
    private final CameraDeviceAccessTask mInitControllerTask;
    private boolean mIsVideo;
    private RequestFactory.VideoSavingRequestBuilder mLastVideoSavingRequest;
    private StateMachine mStateMachine;
    private StateMachine mStateMachineForSavingRequest;
    private RecorderController mVideoRecorder;
    private final Object mVideoRecorderLock;
    private static final Object sSendPauseEventAndReleaseCameraTaskToken = new Object();
    private static final Map<CameraSessionId, CameraSessionInfo> sCameraSessionInfoMap = new ArrayMap();
    private LoadSettingsThread mLoadSettingsThread = null;
    private Handler mUiThreadHandler = CameraApplication.getUiThreadHandler();
    private SharedPreferences mPreferences = null;
    private GeotagManager mGeotagManager = null;
    private FastCaptureOrientation mFastCaptureOrientation = null;
    private FastCapture mFastCaptureSetting = null;
    private Boolean mIsRecording = false;
    private PreProcessState mPreProcessState = PreProcessState.NOT_STARTED;
    private boolean mIsCameraDisabled = false;
    private boolean mIsFpsLimitationEnabled = false;
    private boolean mActivityIsInForeground = true;
    private boolean mIsInShutdownNow = false;
    private CameraSessionId mCameraSessionId = null;
    private CloseBypassCameraTimeoutTask mCloseBypassCameraTimeoutTask = null;
    private final Runnable mChangeProviderDeviceStatusToRecordingTask = new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.5
        @Override // java.lang.Runnable
        public void run() {
            CameraDeviceHandler.this.mCameraController.changeProviderDeviceStatusToRecording(CameraDeviceHandler.this.mCameraSessionId);
        }
    };

    enum CameraDeviceStatus {
        STATUS_RELEASED,
        STATUS_OPENED,
        STATUS_READY,
        STATUS_EVICTED,
        STATUS_ERROR
    }

    public enum ErrorCode {
        ERROR_ON_CAMERA_ERROR,
        ERROR_ON_CAMERA_DISCONNECTION,
        ERROR_ON_CAPTURE_FAILED,
        ERROR_ON_CONFIGURE_FAILED
    }

    public interface ImageReaderInitializedCallback {
        void onInitialized();
    }

    public interface OnPreviewStartedListener {
        void onPreviewStarted(CameraSessionId cameraSessionId);
    }

    enum OpenClosePerformStatus {
        NONE,
        BYPASS_CAMERA_OPENED,
        CAMERA_OPENED,
        CAMERA_CLOSED,
        BYPASS_CAMERA_CLOSED
    }

    enum OpenCloseRequestStatus {
        NONE,
        BYPASS_CAMERA_OPENING,
        CAMERA_OPENING,
        CAMERA_CLOSING,
        BYPASS_CAMERA_CLOSING
    }

    public enum PreProcessState {
        NOT_STARTED,
        PRE_SCAN_STARTED,
        PRE_SCAN_DONE,
        PRE_CAPTURE_STARTED,
        PRE_SHUTTER_DONE,
        PRE_CAPTURE_DONE,
        PRE_CAPTURE_RELEASED
    }

    public static final void preload() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Context getApplicationContext() {
        return this.mApplicationContext;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpStatus(StringBuilder sb) {
        sb.append("mStateMachine:" + this.mStateMachine + ",");
        sb.append("mPreProcessState:" + this.mPreProcessState.name() + ",");
        sb.append("mActivityIsInForeground:" + this.mActivityIsInForeground + ",");
        sb.append("mIsCameraDisabled:" + this.mIsCameraDisabled + ",");
        CameraSessionInfo.dump(sb);
        this.mCameraController.dump(sb);
        this.mBypassCameraController.dump(sb);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changePreProcessStateTo(PreProcessState preProcessState) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked current:" + this.mPreProcessState + " next:" + preProcessState);
        }
        this.mPreProcessState = preProcessState;
    }

    public void cancelPreProcessState() {
        changePreProcessStateTo(PreProcessState.NOT_STARTED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNeedCreatePreviewSession() {
        return getPreProcessState() == PreProcessState.PRE_SHUTTER_DONE && this.mCameraController.getCameraDeviceStatus() == CameraDeviceStatus.STATUS_OPENED;
    }

    public Object getSendPauseEventAndReleaseCameraTaskToken() {
        return sSendPauseEventAndReleaseCameraTaskToken;
    }

    public void stopAudioRecording() {
        if (this.mVideoRecorder != null) {
            this.mVideoRecorder.stopAudioRecording();
        }
    }

    public int getRemainSavingPhotoRequestCount() {
        return this.mBypassCameraController.getRemainSavingPhotoRequestCount();
    }

    public int getRemainPrevSavingRequestCount() {
        return this.mBypassCameraController.getRemainPrevSavingRequestCount();
    }

    public RequestFactory.PhotoSavingRequestBuilder getAndClearPreCaptureResult() {
        return this.mBypassCameraController.getAndClearPreCaptureResult();
    }

    public void playShutterSound(int i) throws ExecutionException, InterruptedException {
        if (CamLog.DEBUG) {
            CamLog.d("invoked type:" + i);
        }
        if (this.mCameraActionSound != null) {
            switch (i) {
                case 1:
                    this.mCameraActionSound.play(0, false);
                    break;
                case 2:
                    this.mCameraActionSound.play(2, true);
                    break;
            }
        }
    }

    private boolean isBypassCameraSupported() {
        return PlatformCapability.isBypassCameraSupported();
    }

    public static class CameraSessionId {
        private static Object sIdLock = new Object();
        private static int sLastId;
        private final String mTag = makeTag();

        private static String makeTag() {
            String string;
            synchronized (sIdLock) {
                int i = sLastId + 1;
                sLastId = i;
                string = Integer.toString(i);
            }
            return string;
        }

        public String toString() {
            return this.mTag;
        }
    }

    static class CameraSessionInfo {
        private final CameraParameters mCameraParameters;
        private boolean mIsCameraEvicted = false;
        private boolean mIsCameraError = false;
        private boolean mIsOtherError = false;
        private final CameraInfo mCameraInfo = new CameraInfo();
        private OpenCloseRequestStatus mRequested = OpenCloseRequestStatus.NONE;
        private OpenClosePerformStatus mPerformed = OpenClosePerformStatus.NONE;

        CameraSessionInfo(CameraInfo.CameraId cameraId) {
            this.mCameraParameters = new CameraParameters(cameraId);
        }

        static void addOpenCloseStatusInfo(CameraSessionId cameraSessionId, CameraSessionInfo cameraSessionInfo) {
            synchronized (CameraDeviceHandler.sCameraSessionInfoMap) {
                CameraDeviceHandler.sCameraSessionInfoMap.put(cameraSessionId, cameraSessionInfo);
            }
        }

        static CameraSessionInfo getOpenCloseStatusInfo(CameraSessionId cameraSessionId) {
            CameraSessionInfo cameraSessionInfo;
            synchronized (CameraDeviceHandler.sCameraSessionInfoMap) {
                cameraSessionInfo = (CameraSessionInfo) CameraDeviceHandler.sCameraSessionInfoMap.get(cameraSessionId);
            }
            return cameraSessionInfo;
        }

        static void removeOpenCloseStatusInfo(CameraSessionId cameraSessionId) {
            synchronized (CameraDeviceHandler.sCameraSessionInfoMap) {
                if (CameraDeviceHandler.sCameraSessionInfoMap.containsKey(cameraSessionId)) {
                    CameraDeviceHandler.sCameraSessionInfoMap.remove(cameraSessionId);
                }
            }
        }

        synchronized void setRequested(OpenCloseRequestStatus openCloseRequestStatus) {
            this.mRequested = openCloseRequestStatus;
        }

        synchronized void setPerformed(OpenClosePerformStatus openClosePerformStatus) {
            this.mPerformed = openClosePerformStatus;
        }

        synchronized boolean isCloseCameraTaskRequested() {
            switch (this.mRequested) {
                case CAMERA_CLOSING:
                case BYPASS_CAMERA_CLOSING:
                    return true;
                default:
                    return false;
            }
        }

        synchronized boolean isCloseBypassCameraTaskRequested() {
            return AnonymousClass6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$OpenCloseRequestStatus[this.mRequested.ordinal()] == 2;
        }

        synchronized boolean isCloseBypassCameraTaskPerformed() {
            return AnonymousClass6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$OpenClosePerformStatus[this.mPerformed.ordinal()] == 1;
        }

        synchronized boolean isOpenBypassCameraTaskPerformed() {
            return AnonymousClass6.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$OpenClosePerformStatus[this.mPerformed.ordinal()] != 2;
        }

        synchronized boolean isOpenCameraTaskPerformed() {
            switch (this.mPerformed) {
                case NONE:
                case BYPASS_CAMERA_OPENED:
                    return false;
                default:
                    return true;
            }
        }

        synchronized void setCameraEvicted() {
            this.mIsCameraEvicted = true;
        }

        synchronized void setCameraError() {
            this.mIsCameraError = true;
        }

        synchronized void setOtherError() {
            this.mIsOtherError = true;
        }

        synchronized boolean isErrorCaused() {
            boolean z;
            z = true;
            CamLog.i("Error caused by evicted:" + this.mIsCameraEvicted + " deviceError:" + this.mIsCameraError + " otherError:" + this.mIsOtherError);
            if (!this.mIsCameraEvicted && !this.mIsCameraError) {
                if (!this.mIsOtherError) {
                    z = false;
                }
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized String info() {
            return "[" + this.mRequested.name() + "|" + this.mPerformed.name() + "|" + this.mIsCameraEvicted + "|" + this.mIsCameraError + "|" + this.mIsOtherError + "]";
        }

        public static void dump(StringBuilder sb) {
            synchronized (CameraDeviceHandler.sCameraSessionInfoMap) {
                for (Map.Entry entry : CameraDeviceHandler.sCameraSessionInfoMap.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(' ');
                    sb.append(((CameraSessionInfo) entry.getValue()).info());
                    sb.append('\n');
                }
            }
        }

        CameraParameters getParameters() {
            return this.mCameraParameters;
        }

        CameraInfo getCameraInfo() {
            return this.mCameraInfo;
        }
    }

    private boolean isBypassCameraAvailable() {
        CameraSessionInfo openCloseStatusInfo;
        return (this.mCameraSessionId == null || (openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(this.mCameraSessionId)) == null || openCloseStatusInfo.isCloseBypassCameraTaskRequested()) ? false : true;
    }

    public void setOnPreviewStartedListener(OnPreviewStartedListener onPreviewStartedListener) {
        this.mCameraController.setOnPreviewStartedListener(onPreviewStartedListener);
    }

    public void removeOnPreviewStartedListener() {
        this.mCameraController.removeOnPreviewStartedListener();
    }

    public CameraDeviceHandler(Context context) {
        this.mApplicationContext = null;
        this.mApplicationContext = context;
        HandlerThread handlerThread = new HandlerThread("CameraAccess", 10);
        handlerThread.start();
        this.mCameraDeviceThreadHandler = new Handler(handlerThread.getLooper());
        this.mVideoRecorderLock = new Object();
        this.mInitControllerTask = new InitControllerTask();
        runOnCameraDeviceThread(this.mInitControllerTask);
        runOnCameraDeviceThread(new LoadNativeLibraryTask());
    }

    private class InitControllerTask extends CameraDeviceAccessTask {
        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return true;
        }

        private InitControllerTask() {
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected void doCameraDeviceAccess() {
            CameraDeviceHandler.this.mBypassCameraController = new BypassCameraController(CameraDeviceHandler.this.mApplicationContext, new BypassCameraControllerCallbackImpl(), CameraDeviceHandler.this.new CameraDeviceHandlerInquirer());
            CameraDeviceHandler.this.mCameraController = new CameraController(CameraDeviceHandler.this.mApplicationContext, new CameraControllerCallbackImpl(), CameraDeviceHandler.this.new CameraDeviceHandlerInquirer());
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void postCameraDeviceAccess() {
            this.mLatch.countDown();
        }
    }

    private class LoadNativeLibraryTask extends CameraDeviceAccessTask {
        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return true;
        }

        private LoadNativeLibraryTask() {
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected void doCameraDeviceAccess() {
            if (PlatformCapability.isBypassCameraSupported()) {
                BypassCamera.loadNativeLibrary();
            }
            PlatformCapability.awaitPrepare();
        }
    }

    public synchronized CameraSessionId preloadCamera(Context context, UserSettings userSettings, CapturingMode capturingMode, boolean z) {
        FastCapture fastCapture;
        if (isBypassCameraAvailable()) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is already preloaded.");
            }
            return this.mCameraSessionId;
        }
        if (userSettings == null) {
            this.mPreferences = context.getSharedPreferences(SharedPreferencesConstants.CAMERA_SHARED_PREFERENCES_NAME, 0);
        }
        try {
            this.mInitControllerTask.getLatch().await();
            if (this.mPreProcessState != PreProcessState.PRE_CAPTURE_RELEASED) {
                changePreProcessStateTo(PreProcessState.NOT_STARTED);
            }
            if (z) {
                capturingMode = CapturingMode.SCENE_RECOGNITION;
                fastCapture = FastCapture.LAUNCH_AND_CAPTURE;
            } else {
                fastCapture = FastCapture.LAUNCH_ONLY;
            }
            this.mIsVideo = capturingMode.isVideo();
            this.mCameraSessionId = this.mBypassCameraController.openBypassCamera(this.mPreferences, userSettings, fastCapture, capturingMode);
            if (CamLog.DEBUG) {
                CamLog.d("invoked mode:" + capturingMode + " fast-capture:" + z);
            }
            cancelCloseBypassCameraTimeoutTask();
            return this.mCameraSessionId;
        } catch (InterruptedException e) {
            CamLog.e("InitControllerTask is interrupted.", e);
            return null;
        }
    }

    public synchronized boolean prepareCamera(FastCapture fastCapture, CapturingMode capturingMode, UserSettings userSettings) {
        CameraParameterValidator.loadCheckList(getApplicationContext());
        if (((DevicePolicyManager) getApplicationContext().getSystemService("device_policy")).getCameraDisabled(null)) {
            CamLog.i("Use of camera is prohibited by device policy.");
            if (CamLog.DEBUG) {
                CamLog.d("Camera is disabled, so this request was refused.");
            }
            this.mIsCameraDisabled = true;
            return false;
        }
        this.mIsCameraDisabled = false;
        this.mIsVideo = capturingMode.isVideo();
        this.mFastCaptureSetting = fastCapture;
        createCameraActionSound();
        if (!isBypassCameraAvailable()) {
            this.mCameraSessionId = this.mBypassCameraController.openBypassCamera(this.mPreferences, userSettings, this.mFastCaptureSetting, capturingMode);
            cancelCloseBypassCameraTimeoutTask();
        }
        this.mCameraController.initializeCaptureRequest(capturingMode);
        return true;
    }

    public synchronized CameraSessionId openCamera(FastCapture fastCapture, CapturingMode capturingMode, UserSettings userSettings) {
        this.mCameraController.openCamera(this.mCameraSessionId, fastCapture);
        LocalResearchUtil.getInstance().clearAllSettings();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE && (this.mLoadSettingsThread == null || !this.mLoadSettingsThread.isAlive())) {
            this.mLoadSettingsThread = new LoadSettingsThread(capturingMode, userSettings);
            this.mLoadSettingsThread.setName("LoadSettingsThread");
            this.mLoadSettingsThread.setPriority(10);
            this.mLoadSettingsThread.start();
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked mode:" + capturingMode + " quick-launch:" + fastCapture);
        }
        return this.mCameraSessionId;
    }

    @Deprecated
    public CameraParameters getParameters() {
        return getParameters(this.mCameraSessionId);
    }

    public Rect getPreviewSize() {
        if (getParameters() == null) {
            return null;
        }
        return getParameters().getPreviewSize();
    }

    public Float getZoom() {
        CameraParameters parameters = getParameters();
        if (parameters != null) {
            return Float.valueOf(parameters.getZoom());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CameraParameters getParameters(CameraSessionId cameraSessionId) {
        CameraSessionInfo openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        if (openCloseStatusInfo != null) {
            return openCloseStatusInfo.getParameters();
        }
        return null;
    }

    public CameraInfo getCameraInfo() {
        return CameraSessionInfo.getOpenCloseStatusInfo(this.mCameraSessionId).getCameraInfo();
    }

    public void closeCamera(boolean z) {
        if (CameraSessionInfo.getOpenCloseStatusInfo(this.mCameraSessionId) == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        new EachCameraStatusPublisher(getApplicationContext(), getCameraId()).put(new SlowMotion(SlowMotion.Value.OFF)).publish();
        if (this.mPreProcessState == PreProcessState.NOT_STARTED || this.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) {
            changePreProcessStateTo(PreProcessState.NOT_STARTED);
        } else {
            changePreProcessStateTo(PreProcessState.PRE_CAPTURE_RELEASED);
        }
        boolean z2 = z | this.mIsInShutdownNow;
        if (!z2 && isRecorderWorking() && ((BatteryManager) this.mApplicationContext.getSystemService("batterymanager")).getIntProperty(4) == 0) {
            z2 = true;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked sync:" + z2);
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
        if (isRecorderWorking()) {
            CamLog.i("Camera is waiting for completion of capturing in recording video.");
            this.mBypassCameraController.awaitAllSnapshotDone();
        }
        cancelChangeProviderDeviceStatusToRecording();
        synchronized (this.mVideoRecorderLock) {
            if (this.mVideoRecorder != null && (this.mVideoRecorder.isRecording() || this.mVideoRecorder.isPaused())) {
                try {
                    this.mVideoRecorder.stop();
                } catch (RecorderException unused) {
                    throw new RuntimeException("stopRecording():[Failed to stop MediaRecorder.]");
                }
            }
        }
        this.mCameraController.closeCamera(z2, this.mCameraSessionId);
        this.mFastCaptureSetting = null;
        this.mBypassCameraController.closeBypassCamera(z2, this.mCameraSessionId);
        requestCloseBypassCameraTimeoutTask(this.mCameraSessionId);
        releaseCameraActionSound();
    }

    public void closeCamera() {
        closeCamera(false);
    }

    public void closeCamera(CameraSessionId cameraSessionId) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked requested-session:" + cameraSessionId + " current-session:" + this.mCameraSessionId);
        }
        if (this.mCameraSessionId == null || cameraSessionId != this.mCameraSessionId) {
            return;
        }
        closeCamera();
    }

    private class CloseBypassCameraTimeoutTask implements Runnable {
        private final CameraSessionId mSessionId;

        private CloseBypassCameraTimeoutTask(CameraSessionId cameraSessionId) {
            this.mSessionId = cameraSessionId;
        }

        @Override // java.lang.Runnable
        public void run() {
            CameraSessionInfo openCloseStatusInfo;
            if (CameraDeviceHandler.this.mActivityIsInForeground || (openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(this.mSessionId)) == null || !openCloseStatusInfo.isCloseBypassCameraTaskRequested() || openCloseStatusInfo.isCloseBypassCameraTaskPerformed()) {
                return;
            }
            if (CameraDeviceHandler.this.mCameraDeviceThreadHandler != null) {
                CameraDeviceHandler.this.mCameraDeviceThreadHandler.getLooper().dump(new Printer() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.CloseBypassCameraTimeoutTask.1
                    @Override // android.util.Printer
                    public void println(String str) {
                        CamLog.e("CloseBypassCameraTimeoutTask", str);
                    }
                }, "");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n========== TIMEOUT ==========\n");
            CameraDeviceHandler.this.dumpStatus(sb);
            sb.append("=============================");
            CamLog.e("CloseBypassCameraTimeoutTask", sb.toString().replace(',', '\n'));
            throw new RuntimeException("CloseBypassCameraTimeoutTask: The camera app keeps holding the camera hardware resources.");
        }
    }

    private void cancelCloseBypassCameraTimeoutTask() {
        this.mUiThreadHandler.removeCallbacks(this.mCloseBypassCameraTimeoutTask);
        this.mCloseBypassCameraTimeoutTask = null;
    }

    private void requestCloseBypassCameraTimeoutTask(CameraSessionId cameraSessionId) {
        this.mCloseBypassCameraTimeoutTask = new CloseBypassCameraTimeoutTask(cameraSessionId);
        this.mUiThreadHandler.postDelayed(this.mCloseBypassCameraTimeoutTask, CLOSE_BYPASS_CAMERA_TIMEOUT_MILLIS);
    }

    private void createCameraActionSound() {
        this.mCameraDeviceThreadHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.1
            @Override // java.lang.Runnable
            public void run() {
                if (CameraDeviceHandler.this.mCameraActionSound == null) {
                    CameraDeviceHandler.this.mCameraActionSound = new CameraActionSound();
                    CameraDeviceHandler.this.mCameraActionSound.load(2);
                    CameraDeviceHandler.this.mCameraActionSound.load(3);
                    CameraDeviceHandler.this.mCameraActionSound.load(0);
                }
            }
        });
    }

    private void releaseCameraActionSound() {
        this.mCameraDeviceThreadHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.2
            @Override // java.lang.Runnable
            public void run() {
                if (CameraDeviceHandler.this.mCameraActionSound != null) {
                    CameraDeviceHandler.this.mCameraActionSound.release();
                    CameraDeviceHandler.this.mCameraActionSound = null;
                }
            }
        });
    }

    private class LoadSettingsThread extends Thread {
        private final CapturingMode mCapturingMode;
        private final UserSettings mUserSettings;

        private LoadSettingsThread(CapturingMode capturingMode, UserSettings userSettings) {
            this.mCapturingMode = capturingMode;
            this.mUserSettings = userSettings;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("LoadSettingsThread invoked  casuCapture:" + CameraDeviceHandler.this.mFastCaptureSetting + " cameraId:" + this.mCapturingMode.getCameraId());
            }
            if (CameraDeviceHandler.this.mFastCaptureSetting == FastCapture.LAUNCH_AND_CAPTURE) {
                CameraSessionId cameraSessionId = null;
                byte b = 0;
                if (GeotagManager.isGeoTagEnabled((Geotag) this.mUserSettings.get(this.mCapturingMode, UserSettingKey.GEO_TAG), CameraDeviceHandler.this.getApplicationContext())) {
                    CameraDeviceHandler.this.mGeotagManager = new GeotagManager(CameraDeviceHandler.this.getApplicationContext());
                    CameraDeviceHandler.this.mGeotagManager.assignResource();
                    CameraDeviceHandler.this.runOnCameraDeviceThread(new CameraDeviceAccessTask(cameraSessionId) { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.LoadSettingsThread.1
                        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
                        protected boolean verifyCameraDeviceStatus() {
                            return true;
                        }

                        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
                        public void doCameraDeviceAccess() {
                            if (CameraDeviceHandler.this.getApplicationContext() != null && CameraDeviceHandler.this.mGeotagManager != null) {
                                CameraDeviceHandler.this.mGeotagManager.startLocationUpdates(LocationSettingsReader.isLocationProviderAllowed(CameraDeviceHandler.this.getApplicationContext(), "gps"), LocationSettingsReader.isLocationProviderAllowed(CameraDeviceHandler.this.getApplicationContext(), "network"));
                            } else {
                                CamLog.d("Camera has been released.");
                            }
                        }
                    });
                }
                CameraDeviceHandler.this.mFastCaptureOrientation = new FastCaptureOrientation(CameraDeviceHandler.this.getApplicationContext());
                CameraDeviceHandler.this.mFastCaptureOrientation.enable();
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

    public boolean isCameraDisabled() {
        return this.mIsCameraDisabled;
    }

    public void enableFpsLimitation() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
        } else {
            if (this.mIsFpsLimitationEnabled) {
                return;
            }
            this.mIsFpsLimitationEnabled = true;
            int maxPreviewFps = PlatformCapability.getMaxPreviewFps(parameters.getCameraId());
            List<int[]> supportedPreviewFpsRange = PlatformCapability.getSupportedPreviewFpsRange(parameters.getCameraId());
            if (supportedPreviewFpsRange != null) {
                setFpsRange(CameraDeviceUtil.computePreviewFpsRange(parameters.getCameraId(), maxPreviewFps, supportedPreviewFpsRange));
                this.mCameraController.commitParameters(this.mCameraSessionId);
            }
        }
    }

    public void disableFpsLimitation() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mIsFpsLimitationEnabled = false;
    }

    public void preCapture() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return;
        }
        RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilderCreatePreCaptureSavingRequest = createPreCaptureSavingRequest(parameters);
        PerfLog.FAST_PRE_CAPTURE.transit();
        changePreProcessStateTo(PreProcessState.PRE_CAPTURE_STARTED);
        this.mBypassCameraController.requestSnapshot(this.mCameraSessionId, photoSavingRequestBuilderCreatePreCaptureSavingRequest, 1);
    }

    public boolean isPreScanOnGoing() {
        return this.mPreProcessState == PreProcessState.PRE_SCAN_STARTED;
    }

    public boolean isPreCaptureOnGoing() {
        return this.mPreProcessState == PreProcessState.PRE_CAPTURE_STARTED || this.mPreProcessState == PreProcessState.PRE_SHUTTER_DONE;
    }

    public PreProcessState getPreProcessState() {
        return this.mPreProcessState;
    }

    public void setAmberBlueColorAndCommit(int i) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        parameters.setAwbColorCompensationAb(i);
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + i);
        }
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }

    public void setBrightnessAndCommit(int i) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        parameters.setExposureCompensation(i);
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + i);
        }
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }

    public void setFlashMode(Flash flash) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
        } else {
            if (PlatformCapability.isDisplayFlashModeSupported(getCameraId())) {
                if (CamLog.DEBUG) {
                    CamLog.d("Display flash is supported, so this request was refused.");
                    return;
                }
                return;
            }
            if (!PlatformCapability.getSupportedFlashModes(getCameraId()).contains(flash.getValue())) {
                flash = Flash.OFF;
            }
            if (CamLog.DEBUG) {
                CamLog.d("invoked value:" + flash);
            }
            parameters.setFlashMode(flash.getValue());
            new EachCameraStatusPublisher(getApplicationContext(), parameters.getCameraId()).put(new PhotoLight(CameraParameters.FLASH_MODE_TORCH.equals(flash.getValue()) ? PhotoLight.Value.ON : PhotoLight.Value.OFF)).publish();
        }
    }

    public void setDisplayFlashMode(DisplayFlash displayFlash) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return;
        }
        if (!PlatformCapability.isDisplayFlashModeSupported(getCameraId())) {
            if (CamLog.DEBUG) {
                CamLog.d("Display flash is not supported, so this request was refused.");
            }
        } else {
            if (isPreCaptureOnGoing() || isPreScanOnGoing()) {
                if (CamLog.DEBUG) {
                    CamLog.d("Capturing for quick launch is on going, so this request was refused.");
                    return;
                }
                return;
            }
            if (!PlatformCapability.getSupportedFlashModes(getCameraId()).contains(displayFlash.getValue())) {
                displayFlash = DisplayFlash.DISPLAY_OFF;
            }
            if (CamLog.DEBUG) {
                CamLog.d("invoked value:" + displayFlash);
            }
            parameters.setFlashMode(displayFlash.getValue());
        }
    }

    public void setFlashModeAndCommit(Flash flash) {
        if (isPreCaptureOnGoing() || isPreScanOnGoing()) {
            if (CamLog.DEBUG) {
                CamLog.d("Capturing for quick launch is on going, so this request was refused.");
            }
        } else {
            setFlashMode(flash);
            this.mCameraController.commitParameters(this.mCameraSessionId);
        }
    }

    public void setDisplayFlashModeAndCommit(DisplayFlash displayFlash) {
        if (isPreCaptureOnGoing() || isPreScanOnGoing()) {
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + displayFlash);
        }
        setDisplayFlashMode(displayFlash);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }

    public void startFaceDetection() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.startFaceDetection(this.mCameraSessionId, this.mUiThreadHandler);
    }

    public void stopFaceDetection() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.stopFaceDetection(this.mCameraSessionId);
    }

    public void startSceneRecognition() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.startSceneRecognition(this.mCameraSessionId, this.mUiThreadHandler);
    }

    public void stopSceneRecognition() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.stopSceneRecognition(this.mCameraSessionId);
    }

    public CameraInfo.CameraId getCameraId() {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters != null) {
            return parameters.getCameraId();
        }
        return null;
    }

    public boolean isCameraFront() {
        return getCameraId() == CameraInfo.CameraId.FRONT;
    }

    public Rect getPreviewRect(CapturingMode capturingMode, Rect rect) {
        if (capturingMode.isVideo()) {
            CameraParameters parameters = getParameters(this.mCameraSessionId);
            if (parameters == null) {
                return this.mCameraController.getVideoPreviewSize(this.mCameraSessionId, capturingMode.getCameraId(), rect);
            }
            if (parameters.getVideoHdr() == VideoHdr.HDR_ON) {
                return PlatformCapability.getPreferredPreviewSizeForHdrVideo(capturingMode.getCameraId());
            }
            return this.mCameraController.getVideoPreviewSize(this.mCameraSessionId, capturingMode.getCameraId(), rect);
        }
        return this.mCameraController.getPhotoPreviewSize(this.mCameraSessionId, capturingMode.getCameraId(), rect);
    }

    public void setShutterTrigger(ShutterTrigger shutterTrigger) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + shutterTrigger);
        }
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null || parameters.getShutterTrigger() == shutterTrigger) {
            return;
        }
        parameters.setShutterTrigger(shutterTrigger);
        this.mCameraController.triggerRestartPreview(this.mCameraSessionId, false);
    }

    public void setStateMachine(StateMachine stateMachine) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked prev:" + this.mStateMachine + " new:" + stateMachine);
        }
        this.mStateMachine = stateMachine;
        if (this.mStateMachine != null) {
            this.mStateMachineForSavingRequest = this.mStateMachine;
        }
    }

    public void setPreviewSurface(Surface surface) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked surface:" + surface);
        }
        this.mCameraController.setSurface(this.mCameraSessionId, false, surface);
    }

    public void startPreview() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.setRepeatingRequestInternal(this.mCameraSessionId, false);
    }

    public void stopPreview() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked recording:" + isRecorderWorking());
        }
        stopFaceDetection();
        stopSceneRecognition();
        if (!isRecorderWorking()) {
            this.mCameraController.stopPreview(this.mCameraSessionId);
        }
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.CLOSE_INITIAL_RESPONSE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.CLOSE_INITIAL_RESPONSE);
    }

    public void stopPreviewSynchronized() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked recording:" + isRecorderWorking());
        }
        if (isRecorderWorking()) {
            return;
        }
        this.mCameraController.stopPreviewTaskSynchronized(this.mCameraSessionId);
    }

    public void autoFocus() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        if (isBypassCameraSupported()) {
            AfParametersReflectedChecker afParametersReflectedCheckerCreateAfParametersResultChecker = this.mCameraController.createAfParametersResultChecker(this.mCameraSessionId, this.mUiThreadHandler);
            if (this.mCameraController.isAfParametersReflectedToDevice(afParametersReflectedCheckerCreateAfParametersResultChecker)) {
                this.mBypassCameraController.requestSnapshotReady(this.mCameraSessionId);
            } else {
                this.mUiThreadHandler.postDelayed(this.mCameraController.requestSnapshotReadyAfterAfParametersReflected(this.mCameraSessionId, afParametersReflectedCheckerCreateAfParametersResultChecker), TIMEOUT_WAIT_FOR_PARAMETERS_TO_BE_REFLECTED_MILLIS);
            }
        }
    }

    public void cancelAutoFocus() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        if (isBypassCameraSupported()) {
            this.mBypassCameraController.requestSnapshotFree(this.mCameraSessionId);
        }
    }

    private class FastCaptureOrientation extends OrientationEventListener {
        private int mOrientation;

        private FastCaptureOrientation(Context context) {
            super(context);
            this.mOrientation = -1;
        }

        @Override // android.view.OrientationEventListener
        public void onOrientationChanged(int i) {
            this.mOrientation = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getOrientation() {
            int normalizedRotation = RotationUtil.getNormalizedRotation(this.mOrientation);
            CameraInfo cameraInfo = CameraDeviceHandler.this.getCameraInfo();
            switch (cameraInfo.facing) {
            }
            return (cameraInfo.orientation + normalizedRotation) % 360;
        }
    }

    private RequestFactory.PhotoSavingRequestBuilder createPreCaptureSavingRequest(CameraParameters cameraParameters) {
        return new RequestFactory.PhotoSavingRequestBuilder(new TakenStatusCommon(System.currentTimeMillis(), this.mFastCaptureOrientation != null ? this.mFastCaptureOrientation.getOrientation() : 0, this.mGeotagManager != null ? this.mGeotagManager.getCurrentLocation() : null, cameraParameters.getPictureSize().width(), cameraParameters.getPictureSize().height(), MediaSavingConstants.MEDIA_TYPE_JPEG_MIME, MediaSavingConstants.MEDIA_TYPE_JPEG_EXT, SavingTaskManager.SavedFileType.PHOTO, null, "", false, true), new TakenStatusPhoto(), true);
    }

    public void applySavingRequest(RequestFactory.RequestBuilder requestBuilder) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        parameters.setRotation(requestBuilder.mCommonStatus.orientation);
        parameters.removeGpsData();
        if (requestBuilder.mCommonStatus.location != null) {
            if ((requestBuilder.mCommonStatus.location.getLatitude() == 0.0d && requestBuilder.mCommonStatus.location.getLongitude() == 0.0d) ? false : true) {
                parameters.setGpsData(requestBuilder.mCommonStatus.location);
            }
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }

    public void takePicture(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked datetaken:" + photoSavingRequestBuilder.getDateTaken());
        }
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters != null && isBypassCameraSupported()) {
            this.mBypassCameraController.requestSnapshot(this.mCameraSessionId, photoSavingRequestBuilder, parameters.getPredictiveCaptureNum());
        }
    }

    public void prepareBurst() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mBypassCameraController.requestPrepareBurstShot(this.mCameraSessionId);
    }

    public void finishBurst() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mBypassCameraController.requestFinishBurstShot(this.mCameraSessionId);
    }

    public void setSelectedFacePosition(int i, int i2) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked x:" + i + " y:" + i2);
        }
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
        } else if (PlatformCapability.isFaceDetectionAvailable(parameters.getCameraId())) {
            resetFocusModeAndCommit();
            this.mCameraController.setSelectedFacePosition(this.mCameraSessionId, i, i2);
        } else {
            CamLog.i("Face detection is not available.");
        }
    }

    public void setZoom(float f) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return;
        }
        parameters.setZoom(f);
    }

    public void setZoomAndCommit(float f) {
        setZoom(f);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }

    public void setFocusPositionAndCommit(Rect rect) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (PlatformCapability.getMaxNumFocusAreas(getCameraId()) < 1) {
            if (CamLog.DEBUG) {
                CamLog.d("Focus position change is not supported, so this request is refused.");
                return;
            }
            return;
        }
        Rect rect2 = new Rect(PositionConverter.getInstance().convertFromViewToActiveArray(rect));
        parameters.setFocusArea("user");
        if (CamLog.DEBUG) {
            CamLog.d("invoked rect:" + rect);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(rect2);
        parameters.setFocusRectangles(arrayList);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }

    public void resetFocusModeAndCommit() {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (PlatformCapability.getMaxNumFocusAreas(getCameraId()) < 1) {
            if (CamLog.DEBUG) {
                CamLog.d("Focus position change is not supported, so this request is refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked current-focus-mode:" + parameters.getFocusMode());
        }
        if (!CameraParameters.FOCUS_MODE_MANUAL.equals(parameters.getFocusMode())) {
            parameters.setFocusMode(PlatformDependencyResolver.getDefaultFocusModeForFastCapturePhoto(parameters, getCameraId()));
        }
        parameters.setFocusArea(CameraParameters.FOCUS_AREA_CENTER);
        parameters.setFocusRectangles(null);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }

    private boolean isRecorderReady() {
        return this.mVideoRecorder != null && this.mVideoRecorder.isReady();
    }

    public boolean isCameraDeviceStatusReady() {
        return this.mCameraController.getCameraDeviceStatus() == CameraDeviceStatus.STATUS_READY;
    }

    public void updateRecorder(RequestFactory.VideoSavingRequestBuilder videoSavingRequestBuilder, boolean z) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked recorder-is-ready:" + isRecorderReady() + "shutter-sound-requested:" + z);
        }
        if (isRecorderReady()) {
            this.mLastVideoSavingRequest = videoSavingRequestBuilder;
            this.mVideoRecorder.setLocation(videoSavingRequestBuilder.mCommonStatus.location);
            this.mVideoRecorder.setOrientationHint(videoSavingRequestBuilder.mCommonStatus.orientation);
            this.mVideoRecorder.setMaxDurationMillis(videoSavingRequestBuilder.mVideoStatus.maxDurationMills);
            this.mVideoRecorder.setMaxFileSizeBytes(videoSavingRequestBuilder.mVideoStatus.maxFileSizeBytes);
            this.mVideoRecorder.setOutputFilePath(videoSavingRequestBuilder.getFilePath());
            this.mVideoRecorder.setUserSoundSetting(z);
        }
    }

    public void prepareRecorder(RequestFactory.VideoSavingRequestBuilder videoSavingRequestBuilder, RecorderController.RecorderListener recorderListener, OnSuperSlowRecordingFinishedListener onSuperSlowRecordingFinishedListener, boolean z, RecordingProfile recordingProfile, Storage.StorageWriteNotifier storageWriteNotifier) {
        if (this.mVideoRecorder != null && this.mVideoRecorder.isStopping()) {
            if (CamLog.DEBUG) {
                CamLog.d("Recorder is stopping, so this request is refused.");
                return;
            }
            return;
        }
        if (isRecorderReady()) {
            if (CamLog.DEBUG) {
                CamLog.d("Recorder is already ready, so this request is refused.");
                return;
            }
            return;
        }
        this.mLastVideoSavingRequest = videoSavingRequestBuilder;
        Uri extraOutput = videoSavingRequestBuilder.getExtraOutput();
        if (extraOutput == null) {
            extraOutput = Uri.fromFile(new File(videoSavingRequestBuilder.getFilePath()));
        }
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            return;
        }
        RecorderFactory.Parameters parameters2 = new RecorderFactory.Parameters(recorderListener, onSuperSlowRecordingFinishedListener, recordingProfile.getProgressInterval(), z, parameters.getVideoStabilizer(), parameters.getSlowMotion());
        releaseRecorder();
        Accessor<BypassCamera> accessor = new Accessor<BypassCamera>() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sonyericsson.android.camera.recorder.utility.Accessor
            public BypassCamera get() {
                return CameraDeviceHandler.this.mBypassCameraController.getBypassCameraInstance();
            }
        };
        Accessor<CameraActionSound> accessor2 = new Accessor<CameraActionSound>() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sonyericsson.android.camera.recorder.utility.Accessor
            public CameraActionSound get() {
                return CameraDeviceHandler.this.mCameraActionSound;
            }
        };
        VideoSize videoSize = getParameters().getVideoSize();
        this.mVideoRecorder = RecorderFactory.create(getApplicationContext(), accessor2, accessor, this.mUiThreadHandler, this.mCameraDeviceThreadHandler, parameters2, (int) PlatformCapability.getSuperSlowFrameRate(parameters.getCameraId(), videoSize), (int) PlatformCapability.getSuperSlowFrameNum(parameters.getCameraId(), videoSize));
        this.mVideoRecorder.setStorageWriteNotifier(storageWriteNotifier);
        CamcorderProfile camcorderProfile = recordingProfile.getCamcorderProfile();
        if (camcorderProfile == null) {
            CamLog.e("prepareRecorder() : CamcorderProfile is null.");
            throw new RuntimeException("CamcorderProfile is null.");
        }
        boolean z2 = (parameters.getSlowMotion() == com.sonyericsson.android.camera.configuration.parameters.SlowMotion.SUPER_SLOW_SHOT || RecordingUtil.isAudioPolicyActive(getApplicationContext())) ? false : true;
        if (CamLog.DEBUG) {
            CamLog.d("invoked uri:" + extraOutput + " audio-record-enabled:" + z2);
        }
        if (this.mVideoRecorder.prepare(new RecorderParameters.Builder(extraOutput, camcorderProfile).setLocation(videoSavingRequestBuilder.mCommonStatus.location).setMaxDuration((int) videoSavingRequestBuilder.mVideoStatus.maxDurationMills).setMaxFileSize(videoSavingRequestBuilder.mVideoStatus.maxFileSizeBytes).setMicrophoneEnabled(z2).setOrientationHint(videoSavingRequestBuilder.mCommonStatus.orientation).setHdr(parameters.getVideoHdr() == VideoHdr.HDR_ON).setDataSpace(recordingProfile.dataSpace).build())) {
            return;
        }
        CamLog.e("prepareRecorder() : Failed to prepare MediaRecorder.");
        releaseRecorder();
        throw new RuntimeException("prepareRecorder():[Failed to prepare MediaRecorder.]");
    }

    public boolean isSteadyShotSupported() {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            CamLog.w("[getParameters failed] Did not check availability of VideoStabilizer.");
            return false;
        }
        return VideoStabilizer.STEADY_SHOT.isValueEnabled(parameters.getCameraId(), parameters.getVideoSize(), parameters.getVideoHdr());
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

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseRecorderOnCameraClosed() {
        synchronized (this.mVideoRecorderLock) {
            if (this.mVideoRecorder != null) {
                this.mVideoRecorder.release();
            }
        }
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
            }
            changeProviderDeviceStatusToRecording();
        } catch (RecorderException e) {
            CamLog.e("mMediaRecorder.start() fail.");
            releaseVideo();
            if (this.mLastVideoSavingRequest.getFilePath() != null) {
                if (StorageUtil.getStorageTypeFromPath(this.mLastVideoSavingRequest.getFilePath(), this.mApplicationContext) != Storage.StorageType.EXTERNAL_CARD) {
                    try {
                        File file = new File(this.mLastVideoSavingRequest.getFilePath());
                        if (file.exists() && file.isFile() && !file.delete()) {
                            CamLog.e("videoFile.delete(): [Unable to delete empty video file.]");
                        }
                    } catch (Exception unused) {
                        CamLog.e("startRecording: [Unable to delete empty media file.]");
                    }
                } else {
                    Uri uriSearchDocumentSdCard = StorageUtil.searchDocumentSdCard(this.mApplicationContext, this.mLastVideoSavingRequest.getFilePath());
                    if (uriSearchDocumentSdCard != null) {
                        try {
                            if (!DocumentsContract.deleteDocument(this.mApplicationContext.getContentResolver(), uriSearchDocumentSdCard)) {
                                CamLog.w("deleteDocument: [delete failed.]" + uriSearchDocumentSdCard.toString());
                            }
                        } catch (FileNotFoundException | SecurityException e2) {
                            CamLog.e("deleteDocument: [occurred Exception.]", e2);
                        }
                    }
                }
            }
            throw new RuntimeException(e);
        }
    }

    private void changeProviderDeviceStatusToRecording() {
        this.mUiThreadHandler.postDelayed(this.mChangeProviderDeviceStatusToRecordingTask, 3000L);
    }

    private void cancelChangeProviderDeviceStatusToRecording() {
        this.mUiThreadHandler.removeCallbacks(this.mChangeProviderDeviceStatusToRecordingTask);
    }

    public void startSuperSlowMotion() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        if (!(this.mVideoRecorder instanceof SuperSlowRecorderController)) {
            throw new UnsupportedOperationException("Current recorder doesn't support slow motion");
        }
        try {
            ((SuperSlowRecorderController) this.mVideoRecorder).startSuperSlow();
        } catch (RecorderException e) {
            CamLog.e("startSuperSlowMotion() failed:" + e.getMessage());
        }
    }

    public Optional<Long> stopRecording(boolean z) {
        if (z) {
            this.mBypassCameraController.awaitAllSnapshotDone();
        }
        cancelChangeProviderDeviceStatusToRecording();
        new EachCameraStatusPublisher(getApplicationContext(), getCameraId()).put(new DeviceStatus(DeviceStatus.Value.VIDEO_PREVIEW)).publish();
        synchronized (this.mVideoRecorderLock) {
            if (this.mVideoRecorder != null) {
                if (CamLog.DEBUG) {
                    CamLog.d("invoked capturing:" + z + " recording:" + this.mVideoRecorder.isRecording() + " paused:" + this.mVideoRecorder.isPaused());
                }
                if (this.mVideoRecorder.isRecording() || this.mVideoRecorder.isPaused()) {
                    try {
                        this.mVideoRecorder.stop();
                        return Optional.of(Long.valueOf(this.mVideoRecorder.getRecordingTimeMillis()));
                    } catch (RecorderException unused) {
                        throw new RuntimeException("stopRecording():[Failed to stop MediaRecorder.]");
                    }
                }
            } else if (CamLog.DEBUG) {
                CamLog.d("Recorder doesn't exists, so this request is refused.");
            }
            return Optional.empty();
        }
    }

    public void finalizeRecording() {
        synchronized (this.mVideoRecorderLock) {
            if (this.mVideoRecorder != null) {
                try {
                    if (this.mVideoRecorder.isRecording() || this.mVideoRecorder.isPaused()) {
                        this.mVideoRecorder.stop();
                    }
                    long recordingTimeMillis = this.mVideoRecorder.getRecordingTimeMillis();
                    if (CamLog.DEBUG) {
                        CamLog.d("invoked duration:" + recordingTimeMillis);
                    }
                    this.mLastVideoSavingRequest.setDuration(recordingTimeMillis);
                } catch (RecorderException unused) {
                    throw new RuntimeException("stopRecording():[Failed to stop MediaRecorder.]");
                }
            } else if (CamLog.DEBUG) {
                CamLog.d("Recorder doesn't exists, so this request is refused.");
            }
            this.mIsRecording = false;
        }
    }

    public void pauseRecording() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        try {
            if (this.mVideoRecorder == null || !this.mVideoRecorder.isRecording()) {
                return;
            }
            this.mVideoRecorder.pause();
        } catch (RecorderException unused) {
            throw new RuntimeException("pauseRecording():[Failed to pause MediaRecorder.]");
        }
    }

    public void resumeRecording() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        try {
            if (this.mVideoRecorder == null || !this.mVideoRecorder.isPaused()) {
                return;
            }
            this.mVideoRecorder.resume();
            ResearchUtil.getInstance().incrementCountRecordResume();
        } catch (RecorderException unused) {
            throw new RuntimeException("resumeRecording():[Failed to resume MediaRecorder.]");
        }
    }

    public void releaseVideo() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        releaseRecorder();
    }

    public void captureWhileRecording(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        takePicture(photoSavingRequestBuilder);
    }

    public void requestOnePreviewFrame() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.requestOnePreviewFrame(this.mCameraSessionId, this.mUiThreadHandler);
    }

    public void startObjectTracking(Rect rect, CameraParameters.ObjectTrackingCallback objectTrackingCallback) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked position:" + rect);
        }
        this.mCameraController.startObjectTracking(this.mCameraSessionId, this.mUiThreadHandler, rect, objectTrackingCallback);
    }

    public void stopObjectTracking() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.stopObjectTracking(this.mCameraSessionId);
    }

    public void startFusionMonitoring() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.startFusionMonitoring(this.mUiThreadHandler);
    }

    public void stopFusionMonitoring() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mCameraController.stopFusionMonitoring();
    }

    public CameraParameters.FusionResult getLatestFusionResult() {
        return this.mCameraController.getLatestFusionResult();
    }

    public boolean isObjectTrackingRunning() {
        return this.mCameraController.isObjectTrackingRunning();
    }

    public boolean isRecording() {
        return this.mIsRecording.booleanValue();
    }

    public boolean isRecorderWorking() {
        if (this.mVideoRecorder == null) {
            return false;
        }
        if (!this.mVideoRecorder.isRecording() && !this.mVideoRecorder.isPaused() && !this.mVideoRecorder.isStopping()) {
            return false;
        }
        CamLog.d("invoked isRecorderWorking() isRecording:" + this.mVideoRecorder.isRecording() + " isPaused:" + this.mVideoRecorder.isPaused() + " isStopping:" + this.mVideoRecorder.isStopping());
        return true;
    }

    public boolean canRecorderTakeSnapshot() {
        return isBypassCameraNextShotAvailable() && !this.mVideoRecorder.isStarting() && (this.mVideoRecorder.isRecording() || this.mVideoRecorder.isPaused());
    }

    public void setTorchAndCommit(boolean z) {
        if (isPreCaptureOnGoing() || isPreScanOnGoing()) {
            if (CamLog.DEBUG) {
                CamLog.d("Capturing for quick launch is on going, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked on:" + z);
        }
        setFlashMode(z ? Flash.LED_ON : Flash.LED_OFF);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }

    public void setLowPower() {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
        } else {
            if (CamLog.DEBUG) {
                CamLog.d("invoked");
            }
            parameters.setPowerMode(CameraParameters.POWER_SAVING_MODE_LOW_POWER);
            this.mCameraController.commitParameters(this.mCameraSessionId);
        }
    }

    public void setUltraLowPower() {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
        } else {
            if (CamLog.DEBUG) {
                CamLog.d("invoked");
            }
            parameters.setPowerMode(CameraParameters.POWER_SAVING_MODE_ULTRA_LOW_POWER);
            this.mCameraController.commitParameters(this.mCameraSessionId);
        }
    }

    public void setPredictiveCapture(PredictiveCapture predictiveCapture) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
        } else {
            if (DebugParameterUtils.INSTANCE.isAlwaysPredictiveCaptureEnabled(getApplicationContext())) {
                CamLog.i("PredictiveCapture setting has been overwritten by DebugParameterUtils.");
                predictiveCapture = PredictiveCapture.ON;
            }
            parameters.setPredictiveCapture(predictiveCapture.getValue());
            parameters.setPredictiveCaptureNum(predictiveCapture.getCaptureNum());
        }
    }

    public void setPredictiveCaptureAndCommit(PredictiveCapture predictiveCapture) {
        CameraParameters parameters;
        if (CamLog.DEBUG && (parameters = getParameters(this.mCameraSessionId)) != null) {
            String predictiveCapture2 = parameters.getPredictiveCapture();
            if (!predictiveCapture.getValue().equals(predictiveCapture2)) {
                CamLog.d("PredictiveCapture setting was changed : " + predictiveCapture2 + " -> " + predictiveCapture.getValue());
            }
        }
        setPredictiveCapture(predictiveCapture);
        this.mBypassCameraController.commitParameters(this.mCameraSessionId);
    }

    public void setEv(Ev ev) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
        } else {
            if (CamLog.DEBUG) {
                CamLog.d("invoked value:" + ev);
            }
            parameters.setExposureCompensation(ev.getIntValue());
        }
    }

    public void setFocusMode(FocusMode focusMode) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + focusMode + "video:" + this.mIsVideo);
        }
        if (this.mIsVideo) {
            parameters.setFocusMode(focusMode.getValueForVideo());
        } else {
            parameters.setFocusMode(focusMode.getValue());
        }
        if (!PlatformCapability.isFocusSupported(parameters.getCameraId())) {
            CamLog.d("Camera focus isn't supported. FocusArea is not set.");
        } else {
            resetFocusAreaAndRect(focusMode);
        }
        if (FocusMode.OBJECT_TRACKING.equals(focusMode)) {
            return;
        }
        stopObjectTracking();
    }

    public void resetFocusAreaAndRect(FocusMode focusMode) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + focusMode);
        }
        if (focusMode != null) {
            parameters.setFocusArea(focusMode.getFocusArea());
        }
        setFocusRect(new Rect());
    }

    private void setFocusRect(Rect rect) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (PlatformCapability.getMaxNumFocusAreas(getCameraId()) < 1) {
            if (CamLog.DEBUG) {
                CamLog.d("Focus area change is not supported, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked rect:" + rect);
        }
        if (rect.isEmpty()) {
            if (parameters.getFocusArea().equals("user")) {
                Rect activeArraySize = PlatformCapability.getActiveArraySize(getCameraId());
                int iCenterX = activeArraySize.centerX();
                int iCenterY = activeArraySize.centerY();
                rect.set(iCenterX, iCenterY, iCenterX + 1, iCenterY + 1);
            } else {
                rect = new Rect();
            }
        } else {
            parameters.setFocusArea("user");
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(rect);
        parameters.setFocusRectangles(arrayList);
    }

    public void setHdr(Hdr hdr) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
        } else {
            if (CamLog.DEBUG) {
                CamLog.d("invoked value:" + hdr);
            }
            parameters.setStillHdr(hdr.getValue());
        }
    }

    public void setIso(Iso iso) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + iso + " shutter-speed:" + parameters.getShutterSpeed());
        }
        parameters.setIso(iso.getIsoValue());
        if (iso == Iso.ISO_AUTO) {
            if (ShutterSpeed.AUTO.getShutterSpeedInNanoMillis() == parameters.getShutterSpeed()) {
                parameters.setAeMode("auto");
                return;
            } else {
                parameters.setAeMode(CameraParameters.AE_MODE_SHUTTER_PRIO);
                return;
            }
        }
        if (ShutterSpeed.AUTO.getShutterSpeedInNanoMillis() == parameters.getShutterSpeed()) {
            parameters.setAeMode(CameraParameters.AE_MODE_ISO_PRIO);
        } else if (PlatformCapability.getSupportedAeModes(parameters.getCameraId()).contains(CameraParameters.AE_MODE_SEMI_AUTO)) {
            parameters.setAeMode(CameraParameters.AE_MODE_SEMI_AUTO);
        } else {
            setShutterSpeed(ShutterSpeed.AUTO);
        }
    }

    public void setMetering(Metering metering) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + metering);
        }
        parameters.setMeteringMode(metering.getValue());
        setMeteringArea(null, metering);
    }

    public void setMeteringAreaAndCommit(Rect rect, Metering metering) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + rect);
        }
        setMeteringArea(rect != null ? new Rect(PositionConverter.getInstance().convertFromViewToActiveArray(rect)) : null, metering);
        this.mCameraController.commitParameters(this.mCameraSessionId);
    }

    private void setMeteringArea(Rect rect, Metering metering) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked area:" + rect + " metering:" + metering);
        }
        ArrayList arrayList = null;
        if (rect != null) {
            arrayList = new ArrayList();
            arrayList.add(rect);
            parameters.setMeteringMode("user");
        } else {
            parameters.setMeteringMode(metering.getValue());
        }
        parameters.setMeteringArea(arrayList);
    }

    public void setResolution(CameraInfo.CameraId cameraId, Resolution resolution) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked cameraId:" + cameraId + " resolution:" + resolution + " prev-resolution:" + parameters.getPictureSize() + " video:" + this.mIsVideo);
        }
        if (parameters.getPictureSize() != resolution.getPictureRect() && !this.mIsVideo) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        Rect photoPreviewSize = this.mCameraController.getPhotoPreviewSize(this.mCameraSessionId, cameraId, resolution.getPictureRect());
        if (photoPreviewSize != null && !this.mIsVideo) {
            parameters.setPictureSize(resolution.getPictureRect());
            setPreviewSize(photoPreviewSize);
            return;
        }
        throw new IllegalArgumentException();
    }

    private void setPreviewSize(Rect rect) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + rect);
        }
        this.mCameraController.triggerRestartPreview(this.mCameraSessionId, false);
        parameters.setPreviewSize(rect);
        Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(rect);
        PositionConverter.getInstance().setPreviewSize(rectAccordingToLayoutOrientation.width(), rectAccordingToLayoutOrientation.height());
    }

    public void setSoftSkin(SoftSkin softSkin) {
        if (this.mIsVideo || getCameraId() == CameraInfo.CameraId.BACK) {
            if (CamLog.DEBUG) {
                CamLog.d("This request was refused. video:" + this.mIsVideo + " cameraId:" + getCameraId());
                return;
            }
            return;
        }
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
        } else {
            if (CamLog.DEBUG) {
                CamLog.d("invoked value:" + softSkin);
            }
            parameters.setSoftSkin(softSkin.getLevel(PlatformCapability.getMaxSoftSkinLevel(getCameraId())));
        }
    }

    public void setVideoStabilizer(VideoStabilizer videoStabilizer) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + videoStabilizer + " video:" + this.mIsVideo);
        }
        if (!videoStabilizer.getValue().equals(parameters.getVideoStabilizer()) && this.mIsVideo) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        parameters.setVideoStabilizer(videoStabilizer.getValue());
    }

    public void setPreviewSizeAndFpsRangeForVideo(CameraInfo.CameraId cameraId, VideoSize videoSize, VideoHdr videoHdr) {
        Rect videoPreviewSize;
        if (CamLog.DEBUG) {
            CamLog.d("invoked cameraId:" + cameraId + " video-size:" + videoSize);
        }
        if (videoHdr == VideoHdr.HDR_ON) {
            videoPreviewSize = PlatformCapability.getPreferredPreviewSizeForHdrVideo(cameraId);
        } else {
            videoPreviewSize = this.mCameraController.getVideoPreviewSize(this.mCameraSessionId, cameraId, videoSize.getVideoRect());
        }
        if (videoPreviewSize != null) {
            setPreviewSize(videoPreviewSize);
            setFpsRange(CameraDeviceUtil.computePreviewFpsRange(cameraId, RecordingProfile.getVideoFrameRate(videoSize, videoHdr), PlatformCapability.getSupportedPreviewFpsRange(cameraId)));
            return;
        }
        throw new IllegalArgumentException();
    }

    private void setFpsRange(int[] iArr) {
        if (iArr.length > 0) {
            CameraParameters parameters = getParameters(this.mCameraSessionId);
            if (parameters == null) {
                if (CamLog.DEBUG) {
                    CamLog.d("This session has been closed, so this request was refused.");
                    return;
                }
                return;
            }
            if (CamLog.DEBUG) {
                CamLog.d("invoked min:" + iArr[0] + " max:" + iArr[1]);
            }
            parameters.setPreviewFpsRange(iArr[0], iArr[1]);
            return;
        }
        CamLog.e("Ilegal fps range is specified.");
    }

    public void setVideoSize(VideoSize videoSize) {
        if (videoSize == null) {
            CamLog.e("Ilegal video size is specified.");
            return;
        }
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + videoSize + " prev:" + parameters.getVideoSize() + " video:" + this.mIsVideo);
        }
        if (parameters.getVideoSize() != videoSize && this.mIsVideo) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        parameters.setVideoSize(videoSize);
        if (this.mIsVideo) {
            setPreviewSizeAndFpsRangeForVideo(parameters.getCameraId(), videoSize, parameters.getVideoHdr());
        }
    }

    public void setWhiteBalance(WhiteBalance whiteBalance) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
        } else {
            if (CamLog.DEBUG) {
                CamLog.d("invoked value:" + whiteBalance);
            }
            parameters.setWhiteBalance(whiteBalance.getValue());
        }
    }

    public void setSlowMotion(com.sonyericsson.android.camera.configuration.parameters.SlowMotion slowMotion) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + slowMotion + " video:" + this.mIsVideo);
        }
        if (parameters.getSlowMotion() == slowMotion) {
            return;
        }
        if (this.mIsVideo) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        parameters.setSlowMotion(slowMotion);
        switch (slowMotion) {
            case SUPER_SLOW_MOTION:
            case SUPER_SLOW_SHOT:
                parameters.setExposureTimeLimit((long) Math.ceil(1.0E9d / PlatformCapability.getSuperSlowFrameRate(parameters.getCameraId(), parameters.getVideoSize())));
                break;
            default:
                parameters.setExposureTimeLimit(PlatformCapability.getMinExposureTimeLimit(parameters.getCameraId()));
                break;
        }
    }

    public void commit() {
        if (CamLog.DEBUG) {
            CamLog.d("invoked pre-process:" + this.mPreProcessState + " remain-saving-photo:" + getRemainSavingPhotoRequestCount());
        }
        this.mCameraController.commit(this.mCameraSessionId);
        this.mBypassCameraController.commit(this.mCameraSessionId);
        if ((this.mPreProcessState == PreProcessState.NOT_STARTED || this.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) && getRemainSavingPhotoRequestCount() == 0) {
            this.mBypassCameraController.prepareCaptureImageReader(this.mCameraSessionId, null);
        }
    }

    public void setCapturingMode(CapturingMode capturingMode) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + capturingMode + " video:" + this.mIsVideo);
        }
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
                return;
            }
            return;
        }
        if (this.mIsVideo != capturingMode.isVideo()) {
            this.mIsVideo = capturingMode.isVideo();
            this.mBypassCameraController.requestApplyBypassCameraMode();
        }
        if (capturingMode != CapturingMode.VIDEO && parameters.getVideoHdr() != VideoHdr.HDR_OFF) {
            parameters.setVideoHdr(VideoHdr.HDR_OFF);
        }
        setZoom(0.0f);
        if (!this.mIsVideo) {
            setFpsRange(CameraDeviceUtil.computePreviewFpsRange(getCameraId(), PlatformCapability.getMaxPreviewFps(getCameraId()), PlatformCapability.getSupportedPreviewFpsRange(getCameraId())));
        }
        this.mCameraController.triggerRestartPreview(this.mCameraSessionId, true);
    }

    public void setShutterSpeed(ShutterSpeed shutterSpeed) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + shutterSpeed + "iso:" + parameters.getIso());
        }
        parameters.setShutterSpeed(shutterSpeed.getShutterSpeedInNanoMillis());
        if (shutterSpeed == ShutterSpeed.AUTO) {
            if (Iso.ISO_AUTO.getIsoValue() == parameters.getIso()) {
                parameters.setAeMode("auto");
                return;
            } else {
                parameters.setAeMode(CameraParameters.AE_MODE_ISO_PRIO);
                return;
            }
        }
        if (Iso.ISO_AUTO.getIsoValue() == parameters.getIso()) {
            parameters.setAeMode(CameraParameters.AE_MODE_SHUTTER_PRIO);
        } else if (PlatformCapability.getSupportedAeModes(parameters.getCameraId()).contains(CameraParameters.AE_MODE_SEMI_AUTO)) {
            parameters.setAeMode(CameraParameters.AE_MODE_SEMI_AUTO);
        } else {
            setIso(Iso.ISO_AUTO);
        }
    }

    public void setFocusRange(FocusRange focusRange) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + focusRange + " video:" + this.mIsVideo + " focus-mode:" + parameters.getFocusMode());
        }
        if (this.mIsVideo) {
            return;
        }
        if (focusRange == FocusRange.AF) {
            if (PlatformCapability.isFocusSupported(parameters.getCameraId())) {
                parameters.setFocusMode(CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                return;
            } else {
                parameters.setFocusMode(CameraParameters.FOCUS_MODE_FIXED);
                return;
            }
        }
        if (!CameraParameters.FOCUS_MODE_MANUAL.equals(parameters.getFocusMode())) {
            stopObjectTracking();
        }
        parameters.setFocusMode(CameraParameters.FOCUS_MODE_MANUAL);
        parameters.setFocusArea(CameraParameters.FOCUS_AREA_CENTER);
        parameters.setFocusRectangles(null);
        if (FocusRange.DEFAULT == focusRange) {
            parameters.setFocusRange(CameraParameters.MANUAL_FOCUS_1M.floatValue());
        } else {
            parameters.setFocusRange(focusRange.getFocusRange(parameters.getCameraId()));
        }
    }

    public void setFusionMode(FusionMode fusionMode) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
            }
        } else {
            if (CamLog.DEBUG) {
                CamLog.d("invoked value:" + fusionMode);
            }
            parameters.setFusionMode(fusionMode.getValue());
        }
    }

    public void setDistortionCorrection(DistortionCorrection distortionCorrection) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + distortionCorrection);
        }
        if (!PlatformCapability.isDistortionCorrectionSupported(getCameraId()) || this.mIsVideo) {
            distortionCorrection = DistortionCorrection.OFF;
        }
        parameters.setDistortionCorrection(distortionCorrection.getValue());
    }

    public void setActivityForeground(boolean z) {
        this.mActivityIsInForeground = z;
    }

    public void setIsInShutdownNow(boolean z) {
        this.mIsInShutdownNow = z;
    }

    public void setVideoHdr(VideoHdr videoHdr) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked value:" + videoHdr + " prev:" + parameters.getVideoHdr());
        }
        if (parameters.getVideoHdr() != videoHdr) {
            this.mBypassCameraController.requestApplyBypassCameraMode();
            parameters.setVideoHdr(videoHdr);
            this.mCameraController.triggerRestartPreview(this.mCameraSessionId, false);
        }
    }

    public void savePreloadSettings(CapturingMode capturingMode, UserSettings userSettings, LastSettings lastSettings, boolean z) {
        CameraParameters parameters = getParameters(this.mCameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused.");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked mode:" + capturingMode + " onde-shot:" + z + " preview-size:" + parameters.getPreviewSize());
        }
        if (parameters.getPreviewSize() == null || z) {
            return;
        }
        lastSettings.setPreviewSize(parameters.getPreviewSize(), capturingMode);
        lastSettings.setFastCapture((FastCapture) userSettings.get(UserSettingKey.FAST_CAPTURE));
        lastSettings.save();
    }

    public void prepareCaptureImageReader(ImageReaderInitializedCallback imageReaderInitializedCallback) {
        if (CamLog.DEBUG) {
            CamLog.d("invoked");
        }
        this.mBypassCameraController.prepareCaptureImageReader(this.mCameraSessionId, imageReaderInitializedCallback);
    }

    public boolean isBypassCameraNextShotAvailable() {
        return this.mBypassCameraController.isBypassCameraNextShotAvailable();
    }

    static abstract class CameraDeviceAccessTask implements Runnable {
        private static final boolean IS_DUMP_EXCEPTION_TASK_INFO_ENABLED = true;
        private static final boolean IS_DUMP_REJECTED_TASK_INFO_ENABLED = false;
        private final DumpInfo mDumpInfoAtConstruct;
        private final boolean mIsBelongedToSession;
        protected final CountDownLatch mLatch;
        private PerfLog mPerfLog;
        private final CameraSessionId mSessionId;

        protected abstract void doCameraDeviceAccess();

        protected void postCameraDeviceAccess() {
        }

        protected abstract boolean verifyCameraDeviceStatus();

        protected void setPerformancefLog(PerfLog perfLog) {
            this.mPerfLog = perfLog;
        }

        private class DumpInfo {
            private final StackTraceElement[] stackTrace;
            private final String status;

            private DumpInfo() {
                this.stackTrace = Thread.currentThread().getStackTrace();
                CameraSessionInfo openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(CameraDeviceAccessTask.this.mSessionId);
                if (openCloseStatusInfo != null) {
                    this.status = openCloseStatusInfo.info();
                } else {
                    this.status = "CameraSession info is null. So, camera is closed";
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void dump(String str) {
                CamLog.d("[status dump] START " + str);
                CamLog.d("[status dump]   status:" + this.status);
                CamLog.d("[status dump]   trace:");
                for (int i = 1; i < this.stackTrace.length; i++) {
                    CamLog.d("[status dump]     at " + this.stackTrace[i].getClassName() + "#" + this.stackTrace[i].getMethodName());
                }
                CamLog.d("[status dump] END");
            }
        }

        CameraDeviceAccessTask(CameraSessionId cameraSessionId) {
            this(cameraSessionId, true);
        }

        CameraDeviceAccessTask() {
            this(null, false);
        }

        private CameraDeviceAccessTask(CameraSessionId cameraSessionId, boolean z) {
            this.mPerfLog = null;
            this.mLatch = new CountDownLatch(1);
            this.mSessionId = cameraSessionId;
            this.mIsBelongedToSession = z;
            if (CamLog.DEBUG) {
                this.mDumpInfoAtConstruct = new DumpInfo();
            } else {
                this.mDumpInfoAtConstruct = null;
            }
            if (CamLog.DEBUG) {
                CamLog.d("REQUEST:" + getClass().getSimpleName() + " sessionId:" + this.mSessionId);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CountDownLatch getLatch() {
            return this.mLatch;
        }

        protected CameraSessionId getSessionId() {
            return this.mSessionId;
        }

        protected CameraSessionInfo getOpenCloseStatusInfo() {
            CameraSessionInfo openCloseStatusInfo = CameraSessionInfo.getOpenCloseStatusInfo(this.mSessionId);
            if (openCloseStatusInfo != null) {
                return openCloseStatusInfo;
            }
            CameraSessionInfo cameraSessionInfo = new CameraSessionInfo(null);
            cameraSessionInfo.setRequested(OpenCloseRequestStatus.BYPASS_CAMERA_CLOSING);
            cameraSessionInfo.setPerformed(OpenClosePerformStatus.BYPASS_CAMERA_CLOSED);
            return cameraSessionInfo;
        }

        protected void removeOpenCloseStatusInfo() {
            CameraSessionInfo.removeOpenCloseStatusInfo(this.mSessionId);
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                boolean z = (this.mIsBelongedToSession && getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed()) ? false : true;
                if (verifyCameraDeviceStatus() && z) {
                    if (CamLog.DEBUG) {
                        CamLog.d("START:" + getClass().getSimpleName() + " sessionId:" + this.mSessionId);
                    }
                    if (this.mPerfLog != null) {
                        this.mPerfLog.begin();
                    }
                    doCameraDeviceAccess();
                    if (this.mPerfLog != null) {
                        this.mPerfLog.end();
                    }
                    if (CamLog.DEBUG) {
                        CamLog.d("END:" + getClass().getSimpleName() + " sessionId:" + this.mSessionId);
                    }
                } else if (CamLog.DEBUG) {
                    CamLog.d("REJECTED:" + getClass().getSimpleName() + " sessionId:" + this.mSessionId);
                }
                postCameraDeviceAccess();
            } catch (RuntimeException e) {
                if (CamLog.DEBUG) {
                    CamLog.d("EXCEPTION:" + getClass().getSimpleName() + " sessionId:" + this.mSessionId);
                    DumpInfo dumpInfo = this.mDumpInfoAtConstruct;
                    StringBuilder sb = new StringBuilder();
                    sb.append("request ");
                    sb.append(getClass().getSimpleName());
                    dumpInfo.dump(sb.toString());
                    new DumpInfo().dump("performed " + getClass().getSimpleName());
                }
                throw e;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOnCameraDeviceThread(CameraDeviceAccessTask cameraDeviceAccessTask) {
        this.mCameraDeviceThreadHandler.post(cameraDeviceAccessTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOnCameraDeviceThreadSync(CameraDeviceAccessTask cameraDeviceAccessTask) throws InterruptedException {
        this.mCameraDeviceThreadHandler.post(cameraDeviceAccessTask);
        try {
            cameraDeviceAccessTask.getLatch().await();
        } catch (InterruptedException e) {
            CamLog.e("runOnCameraDeviceThreadSync() : Failed to await by InterruptedException", e);
        }
    }

    public ImageRetriever getStreamingImageRetriever() {
        return this.mCameraController.getStreamingImageRetriever();
    }

    private class BypassCameraControllerCallbackImpl implements BypassCameraController.BypassCameraControllerCallback {
        private Runnable mSnapshotReadyDoneTask;

        private BypassCameraControllerCallbackImpl() {
        }

        @Override // com.sonyericsson.android.camera.device.BypassCameraController.BypassCameraControllerCallback
        public void onCameraClosed() {
            if (CamLog.DEBUG) {
                CamLog.d("invoked pre-process:" + CameraDeviceHandler.this.mPreProcessState);
            }
            if (CameraDeviceHandler.this.mPreProcessState == PreProcessState.NOT_STARTED || CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) {
                CameraDeviceHandler.this.changePreProcessStateTo(PreProcessState.NOT_STARTED);
            } else {
                CameraDeviceHandler.this.changePreProcessStateTo(PreProcessState.PRE_CAPTURE_RELEASED);
            }
            CameraDeviceHandler.this.mUiThreadHandler.removeCallbacks(this.mSnapshotReadyDoneTask);
            this.mSnapshotReadyDoneTask = null;
            CameraDeviceHandler.this.mUiThreadHandler.post(new CloseCameraDeviceNotificationTask());
        }

        private class CloseCameraDeviceNotificationTask implements Runnable {
            private CloseCameraDeviceNotificationTask() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (CameraDeviceHandler.this.mStateMachine != null) {
                    CameraDeviceHandler.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_CAMERA_DEVICE_CLOSED, new Object[0]);
                }
            }
        }

        @Override // com.sonyericsson.android.camera.device.BypassCameraController.BypassCameraControllerCallback
        public void onPrepareBurstDone(final boolean z) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked success:" + z);
            }
            CameraDeviceHandler.this.mUiThreadHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.BypassCameraControllerCallbackImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    if (CameraDeviceHandler.this.mStateMachine != null) {
                        CameraDeviceHandler.this.mStateMachine.onPrepareBurstDone(z);
                    }
                }
            });
        }

        @Override // com.sonyericsson.android.camera.device.BypassCameraController.BypassCameraControllerCallback
        public void onShutterDone(int i, int i2, boolean z) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked captureId:" + i + " captureNum:" + i2 + " isAfSuccess:" + z);
            }
            if (CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_CAPTURE_STARTED) {
                CameraDeviceHandler.this.changePreProcessStateTo(PreProcessState.PRE_SHUTTER_DONE);
                if (CameraDeviceHandler.this.isNeedCreatePreviewSession()) {
                    CameraDeviceHandler.this.mCameraController.createPreviewSession(CameraDeviceHandler.this.mCameraSessionId);
                }
            }
            String str = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(new Date());
            for (int i3 = 0; i3 < i2; i3++) {
                if (i3 == 0) {
                    RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilderPeekLastSavingPhotoRequest = CameraDeviceHandler.this.mBypassCameraController.peekLastSavingPhotoRequest();
                    setPredictiveCaptureInfo(photoSavingRequestBuilderPeekLastSavingPhotoRequest, i3, i2, str);
                    CameraDeviceHandler.this.mUiThreadHandler.post(new ShutterDoneHandlerCallbackImpl(photoSavingRequestBuilderPeekLastSavingPhotoRequest, i2, z));
                } else {
                    RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilderCreatePhotoSavingRequest = CameraDeviceHandler.this.mStateMachine.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO);
                    setPredictiveCaptureInfo(photoSavingRequestBuilderCreatePhotoSavingRequest, i3, i2, str);
                    CameraDeviceHandler.this.mBypassCameraController.enqueueSavingPhotoRequest(photoSavingRequestBuilderCreatePhotoSavingRequest);
                }
            }
        }

        private void setPredictiveCaptureInfo(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, int i, int i2, String str) {
            if (i2 > 1) {
                photoSavingRequestBuilder.setSaveTimeForCaptureGroup(str);
                photoSavingRequestBuilder.setCaptureIdForCaptureGourp((i2 - i) - 1);
                if (i == 0) {
                    photoSavingRequestBuilder.setSomcType(100);
                }
            }
        }

        private class ShutterDoneHandlerCallbackImpl implements Runnable {
            private final int mCaptureRequestNum;
            private final boolean mIsAfSuccess;
            private final RequestFactory.PhotoSavingRequestBuilder mRequest;

            private ShutterDoneHandlerCallbackImpl(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, int i, boolean z) {
                this.mRequest = photoSavingRequestBuilder;
                this.mCaptureRequestNum = i;
                this.mIsAfSuccess = z;
            }

            @Override // java.lang.Runnable
            public void run() throws ExecutionException, InterruptedException {
                if (CamLog.DEBUG) {
                    CamLog.d("ShutterDoneHandlerCallbackImpl invoked pre-process:" + CameraDeviceHandler.this.mPreProcessState);
                }
                if (CapturePerformanceLogger.get(this.mRequest) != null) {
                    CapturePerformanceLogger.get(this.mRequest).shutterDone = SystemClock.uptimeMillis();
                }
                if (CameraDeviceHandler.this.mPreProcessState == PreProcessState.NOT_STARTED || CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) {
                    if (CameraDeviceHandler.this.mStateMachine != null) {
                        updatePredictiveCaptureNumForResearchUtil(CameraDeviceHandler.this.mStateMachine);
                        CameraDeviceHandler.this.mStateMachine.onShutterDone(this.mRequest, this.mCaptureRequestNum, this.mIsAfSuccess);
                        playSoundIfPossible(CameraDeviceHandler.this.mStateMachine);
                    } else if (CameraDeviceHandler.this.mStateMachineForSavingRequest != null) {
                        updatePredictiveCaptureNumForResearchUtil(CameraDeviceHandler.this.mStateMachineForSavingRequest);
                        playSoundIfPossible(CameraDeviceHandler.this.mStateMachineForSavingRequest);
                    }
                }
            }

            private void updatePredictiveCaptureNumForResearchUtil(StateMachine stateMachine) {
                if (((PredictiveCapture) stateMachine.getUserSetting().get(stateMachine.getCurrentCapturingMode(), UserSettingKey.PREDICTIVE_CAPTURE)) == PredictiveCapture.OFF) {
                    ResearchUtil.getInstance().setPredictiveCaptureNum(0);
                } else {
                    ResearchUtil.getInstance().setPredictiveCaptureNum(this.mCaptureRequestNum);
                }
            }

            private void playSoundIfPossible(StateMachine stateMachine) throws ExecutionException, InterruptedException {
                if (((ShutterSound) stateMachine.getUserSetting().get(stateMachine.getCurrentCapturingMode(), UserSettingKey.SHUTTER_SOUND)) == ShutterSound.OFF || CameraDeviceHandler.this.isRecording()) {
                    return;
                }
                CameraDeviceHandler.this.playShutterSound(1);
            }
        }

        @Override // com.sonyericsson.android.camera.device.BypassCameraController.BypassCameraControllerCallback
        public void onSnapshotDone(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked requestId:" + photoSavingRequestBuilder.getRequestId());
            }
            CameraDeviceHandler.this.mUiThreadHandler.post(new SnapshotDoneHandlerCallbackImpl(photoSavingRequestBuilder));
        }

        private class SnapshotDoneHandlerCallbackImpl implements Runnable {
            private final RequestFactory.PhotoSavingRequestBuilder localRequestBuilder;

            private SnapshotDoneHandlerCallbackImpl(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
                this.localRequestBuilder = photoSavingRequestBuilder;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (CamLog.DEBUG) {
                    CamLog.d("SnapshotDoneHandlerCallbackImpl invoked pre-process:" + CameraDeviceHandler.this.mPreProcessState);
                }
                if (CapturePerformanceLogger.get(this.localRequestBuilder) != null) {
                    CapturePerformanceLogger.get(this.localRequestBuilder).snapshotDone = SystemClock.uptimeMillis();
                }
                TestEventSender.onPictureTaken();
                if (CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_SHUTTER_DONE) {
                    CameraDeviceHandler.this.changePreProcessStateTo(PreProcessState.PRE_CAPTURE_DONE);
                    if (CameraDeviceHandler.this.mStateMachine != null) {
                        CameraDeviceHandler.this.mStateMachine.onPreTakePictureDone(this.localRequestBuilder);
                    } else {
                        CamLog.i("Launch and capture is done before activity is started.");
                        CameraDeviceHandler.this.mBypassCameraController.setPreCaptureResult(this.localRequestBuilder);
                    }
                } else if (CameraDeviceHandler.this.mPreProcessState == PreProcessState.NOT_STARTED || CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) {
                    if (CameraDeviceHandler.this.mStateMachine != null) {
                        CameraDeviceHandler.this.mStateMachine.onTakePictureDone(this.localRequestBuilder);
                    } else if (CameraDeviceHandler.this.mStateMachineForSavingRequest != null) {
                        CamLog.i("Capture is done after activity is puased.");
                        CameraDeviceHandler.this.mStateMachineForSavingRequest.onTakePictureDone(this.localRequestBuilder);
                    } else {
                        CamLog.e("StateMachine doesn't exists, so captured photo cannot be saved.");
                    }
                } else {
                    this.localRequestBuilder.close();
                }
                Context applicationContext = CameraDeviceHandler.this.getApplicationContext();
                if (applicationContext != null) {
                    new EachCameraStatusPublisher(applicationContext, CameraDeviceHandler.this.getCameraId()).put(new DeviceStatus(CameraDeviceHandler.this.mIsVideo ? DeviceStatus.Value.VIDEO_RECORDING : DeviceStatus.Value.STILL_PREVIEW)).publish();
                }
            }
        }

        private String toString(BypassCamera.DisplayFlashColor displayFlashColor) {
            if (displayFlashColor == null) {
                return "null";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(displayFlashColor.colorRed);
            sb.append(',');
            sb.append(displayFlashColor.colorGreen);
            sb.append(',');
            sb.append(displayFlashColor.colorBlue);
            return sb.toString();
        }

        @Override // com.sonyericsson.android.camera.device.BypassCameraController.BypassCameraControllerCallback
        public void onSnapshotReadyDone(ExecutorService executorService, final boolean z, final boolean z2, final boolean z3, final BypassCamera.DisplayFlashColor displayFlashColor) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked pre-process:" + CameraDeviceHandler.this.mPreProcessState + " isHighQualityBurstAvailable:" + z + " isAfSuccess:" + z2 + " requireFlash:" + z3 + " displayFlashColor:" + toString(displayFlashColor));
            }
            this.mSnapshotReadyDoneTask = new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.BypassCameraControllerCallbackImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    if (CamLog.DEBUG) {
                        CamLog.d("SnapshotReadyDoneTask invoked pre-process:" + CameraDeviceHandler.this.mPreProcessState + " fast-capture:" + CameraDeviceHandler.this.mFastCaptureSetting);
                    }
                    if (CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_SCAN_STARTED) {
                        CameraDeviceHandler.this.changePreProcessStateTo(PreProcessState.PRE_SCAN_DONE);
                        if (CameraDeviceHandler.this.mFastCaptureSetting != FastCapture.LAUNCH_AND_CAPTURE && CameraDeviceHandler.this.mStateMachine != null) {
                            CameraDeviceHandler.this.mStateMachine.onInitialAutoFocusDone(z2);
                            return;
                        }
                        ResearchUtil.getInstance().setTimeAfDone();
                        ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.FAST_CAPTURING_LAUNCH);
                        CameraDeviceHandler.this.preCapture();
                        return;
                    }
                    if ((CameraDeviceHandler.this.mPreProcessState == PreProcessState.NOT_STARTED || CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) && CameraDeviceHandler.this.mStateMachine != null) {
                        CameraDeviceHandler.this.mStateMachine.onAutoFocusDone(z, z2, z3, displayFlashColor.colorRed, displayFlashColor.colorGreen, displayFlashColor.colorBlue);
                    }
                }
            };
            if (CameraDeviceHandler.this.mPreProcessState != PreProcessState.PRE_SCAN_STARTED) {
                CameraDeviceHandler.this.mUiThreadHandler.post(this.mSnapshotReadyDoneTask);
            } else if (!executorService.isShutdown()) {
                executorService.submit(this.mSnapshotReadyDoneTask);
            } else {
                CamLog.w("BypassCameraRequestExecutor already Shutdown");
            }
        }
    }

    private class CameraControllerCallbackImpl implements CameraController.CameraControllerCallback {
        private CameraControllerCallbackImpl() {
        }

        @Override // com.sonyericsson.android.camera.device.CameraController.CameraControllerCallback
        public void onDeviceError(CameraSessionId cameraSessionId, final ErrorCode errorCode) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked sessionId:" + cameraSessionId + " error:" + errorCode);
            }
            CameraDeviceHandler.this.mCameraDeviceThreadHandler.post(new OnErrorTask(cameraSessionId));
            CameraDeviceHandler.this.mUiThreadHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraControllerCallbackImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    if (CameraDeviceHandler.this.mStateMachine != null) {
                        CameraDeviceHandler.this.mStateMachine.onDeviceError(errorCode);
                    }
                }
            });
        }

        @Override // com.sonyericsson.android.camera.device.CameraController.CameraControllerCallback
        public void onSessionDisconnected(CameraSessionId cameraSessionId) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked sessionId:" + cameraSessionId);
            }
            CameraDeviceHandler.this.mCameraDeviceThreadHandler.post(new OnDisconnectedTask(cameraSessionId));
            CameraDeviceHandler.this.mUiThreadHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraControllerCallbackImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    if (CameraDeviceHandler.this.mStateMachine != null) {
                        CameraDeviceHandler.this.mStateMachine.onDeviceError(ErrorCode.ERROR_ON_CAMERA_DISCONNECTION);
                    }
                }
            });
        }

        @WorkerThread
        private class OnErrorTask extends CameraDeviceAccessTask {
            private OnErrorTask(CameraSessionId cameraSessionId) {
                super(cameraSessionId);
            }

            @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
            protected boolean verifyCameraDeviceStatus() {
                switch (CameraDeviceHandler.this.mCameraController.getCameraDeviceStatus()) {
                    case STATUS_RELEASED:
                    case STATUS_OPENED:
                    case STATUS_READY:
                        if (!getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                            return true;
                        }
                        CamLog.d("OnErrorTask : CloseCameraTask is already requested.");
                        return false;
                    case STATUS_ERROR:
                    case STATUS_EVICTED:
                        return false;
                    default:
                        throw new IllegalStateException("Failed due to wrong status in OnErrorTask. status: " + CameraDeviceHandler.this.mCameraController.getCameraDeviceStatus());
                }
            }

            @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
            public void doCameraDeviceAccess() {
                CameraDeviceHandler.this.mCameraController.setCameraDeviceStatus(CameraDeviceStatus.STATUS_ERROR);
                CameraDeviceHandler.this.mUiThreadHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraControllerCallbackImpl.OnErrorTask.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (CameraDeviceHandler.this.isRecorderWorking()) {
                            try {
                                CameraDeviceHandler.this.mVideoRecorder.stopOnCameraError();
                            } catch (RecorderException e) {
                                CamLog.e("Stop recording by Camera error fail." + e.getMessage());
                            }
                        }
                        CameraDeviceHandler.this.closeCamera();
                    }
                });
            }
        }

        @WorkerThread
        private class OnDisconnectedTask extends CameraDeviceAccessTask {
            private OnDisconnectedTask(CameraSessionId cameraSessionId) {
                super(cameraSessionId);
            }

            @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
            protected boolean verifyCameraDeviceStatus() {
                switch (CameraDeviceHandler.this.mCameraController.getCameraDeviceStatus()) {
                    case STATUS_RELEASED:
                    case STATUS_OPENED:
                    case STATUS_READY:
                        return !getOpenCloseStatusInfo().isCloseCameraTaskRequested();
                    case STATUS_ERROR:
                    case STATUS_EVICTED:
                        return false;
                    default:
                        throw new IllegalStateException("Failed due to wrong status in OnDisconnectedTask. status: " + CameraDeviceHandler.this.mCameraController.getCameraDeviceStatus());
                }
            }

            @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
            public void doCameraDeviceAccess() {
                CameraDeviceHandler.this.mCameraController.setCameraDeviceStatus(CameraDeviceStatus.STATUS_EVICTED);
                CameraDeviceHandler.this.mUiThreadHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraControllerCallbackImpl.OnDisconnectedTask.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (CameraDeviceHandler.this.isRecorderWorking()) {
                            try {
                                CameraDeviceHandler.this.mVideoRecorder.stopOnCameraError();
                            } catch (RecorderException e) {
                                CamLog.e("Stop recording by Camera eviction fails." + e.getMessage());
                            }
                        }
                        CameraDeviceHandler.this.closeCamera();
                    }
                });
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraController.CameraControllerCallback
        public void onCropRegionReady() {
            if (CamLog.DEBUG) {
                CamLog.d("invoked");
            }
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.onCropRegionReady();
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraController.CameraControllerCallback
        public void onFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked result:" + faceDetectionResult);
            }
            if (CameraDeviceHandler.this.mStateMachine == null || faceDetectionResult == null) {
                ResearchUtil.getInstance().clearFaceNum();
                return;
            }
            ResearchUtil.getInstance().setFaceNum(faceDetectionResult.extFaceList.size());
            if (CameraDeviceHandler.this.isRecording()) {
                ResearchUtil.getInstance().setRecordingMaxFaceNum(faceDetectionResult.extFaceList.size());
            }
            CameraDeviceHandler.this.mStateMachine.onFaceDetected(faceDetectionResult);
        }

        @Override // com.sonyericsson.android.camera.device.CameraController.CameraControllerCallback
        public void onFusionResultChanged(CameraParameters.FusionResult fusionResult) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked result:" + fusionResult);
            }
            if (fusionResult == null || CameraDeviceHandler.this.mStateMachine == null) {
                return;
            }
            CameraDeviceHandler.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_FUSION_CONDITION_CHANGED, fusionResult);
        }

        @Override // com.sonyericsson.android.camera.device.CameraController.CameraControllerCallback
        public void onSceneModeChanged(CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
            String string;
            if (CamLog.DEBUG) {
                CamLog.d("invoked result:" + sceneRecognitionResult);
            }
            if (sceneRecognitionResult == null) {
                return;
            }
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.onSceneModeChanged(sceneRecognitionResult);
            }
            LocalResearchUtil localResearchUtil = LocalResearchUtil.getInstance();
            if (sceneRecognitionResult.isMacroRange) {
                string = ShootingLabel.RECOGNIZED_SCENE_MACRO;
            } else if (sceneRecognitionResult.sceneMode == null) {
                string = CameraParameterConverter.SceneMode.AUTO.toString();
            } else {
                string = sceneRecognitionResult.sceneMode.toString();
            }
            localResearchUtil.setRecognizedScene(string);
        }

        @Override // com.sonyericsson.android.camera.device.CameraController.CameraControllerCallback
        public void onOpenCameraRequested(CameraSessionId cameraSessionId) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked sessionId:" + cameraSessionId);
            }
            CameraDeviceHandler.this.mUiThreadHandler.post(new OpenCameraDeviceNotificationTask(cameraSessionId));
        }

        private class OpenCameraDeviceNotificationTask implements Runnable {
            private final CameraSessionId mSessionId;

            private OpenCameraDeviceNotificationTask(CameraSessionId cameraSessionId) {
                this.mSessionId = cameraSessionId;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (CameraDeviceHandler.this.mStateMachine != null) {
                    CameraDeviceHandler.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_CAMERA_DEVICE_OPENED, this.mSessionId);
                }
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraController.CameraControllerCallback
        public void onPreviewFrameUpdated(ByteBuffer byteBuffer, int i, Rect rect) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked format:" + i + " rect:" + rect);
            }
            if (CameraDeviceHandler.this.mStateMachine == null || i != 17) {
                return;
            }
            byte[] bArr = new byte[byteBuffer.remaining()];
            byteBuffer.get(bArr);
            byteBuffer.rewind();
            CameraDeviceHandler.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_ONE_PREVIEW_FRAME_UPDATED, bArr, Integer.valueOf(i), rect);
        }

        @Override // com.sonyericsson.android.camera.device.CameraController.CameraControllerCallback
        public void onReflected(CameraSessionId cameraSessionId) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked sessionId:" + cameraSessionId);
            }
            CameraDeviceHandler.this.mBypassCameraController.requestSnapshotReady(cameraSessionId);
        }
    }

    class CameraDeviceHandlerInquirer {
        CameraDeviceHandlerInquirer() {
        }

        CameraParameters getParameters(CameraSessionId cameraSessionId) {
            return CameraDeviceHandler.this.getParameters(cameraSessionId);
        }

        boolean isNeedCreatePreviewSession() {
            return CameraDeviceHandler.this.isNeedCreatePreviewSession();
        }

        boolean isIgnoreCameraError() {
            return CameraDeviceHandler.this.mActivityIsInForeground;
        }

        boolean isVideo() {
            return CameraDeviceHandler.this.mIsVideo;
        }

        boolean isRecording() {
            return CameraDeviceHandler.this.isRecording();
        }

        void releaseRecorderOnCameraClosed() {
            if (CamLog.DEBUG) {
                CamLog.d("invoked");
            }
            CameraDeviceHandler.this.releaseRecorderOnCameraClosed();
        }

        void prepareCaptureImageReader() {
            CameraDeviceHandler.this.prepareCaptureImageReader(null);
        }

        void postCameraDeviceThread(CameraDeviceAccessTask cameraDeviceAccessTask) {
            CameraDeviceHandler.this.runOnCameraDeviceThread(cameraDeviceAccessTask);
        }

        void postCameraDeviceThreadSync(CameraDeviceAccessTask cameraDeviceAccessTask) throws InterruptedException {
            CameraDeviceHandler.this.runOnCameraDeviceThreadSync(cameraDeviceAccessTask);
        }

        Handler getDeviceThreadHandler() {
            return CameraDeviceHandler.this.mCameraDeviceThreadHandler;
        }

        boolean awaitLoadSettingsThread() {
            return CameraDeviceHandler.this.awaitLoadSettingsThread();
        }

        PreProcessState getPreProcessState() {
            return CameraDeviceHandler.this.mPreProcessState;
        }

        void changePreProcessStateTo(PreProcessState preProcessState) {
            CameraDeviceHandler.this.changePreProcessStateTo(preProcessState);
        }

        public boolean isPreScanOnGoing() {
            return CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_SCAN_STARTED;
        }

        public boolean isPreCaptureOnGoing() {
            return CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_CAPTURE_STARTED || CameraDeviceHandler.this.mPreProcessState == PreProcessState.PRE_SHUTTER_DONE;
        }

        boolean isSnapshotRunning() {
            return CameraDeviceHandler.this.mBypassCameraController.isSnapshotRunning();
        }
    }
}
