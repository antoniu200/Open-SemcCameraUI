package com.sonyericsson.android.camera.device;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.device.CaptureRequestDumper;
import com.sonyericsson.android.camera.device.ImageRetriever;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.device.CommonPlatformDependencyResolver;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.status.EachCameraStatusPublisher;
import com.sonyericsson.cameracommon.status.GlobalCameraStatusPublisher;
import com.sonyericsson.cameracommon.status.eachcamera.DeviceStatus;
import com.sonyericsson.cameracommon.status.eachcamera.FaceDetection;
import com.sonyericsson.cameracommon.status.eachcamera.FaceIdentification;
import com.sonyericsson.cameracommon.status.eachcamera.ObjectTracking;
import com.sonyericsson.cameracommon.status.eachcamera.SceneRecognition;
import com.sonyericsson.cameracommon.status.global.BuiltInCameraIds;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class CameraController {
    private static final CaptureRequestDumper.Type CAPTURE_REQUEST_DUMP_TYPE = CaptureRequestDumper.Type.DIFF;
    private final Context mApplicationContext;
    private final CameraControllerCallback mCallback;
    private final CameraDeviceHandler.CameraDeviceHandlerInquirer mCameraDeviceHandler;
    private Handler mCameraDeviceStatusThreadHandler;
    private CameraManager mCameraManager;
    private ImageRetriever mCaptureImageRetriever;
    private CaptureRequestDumper mCaptureRequestDumper;
    private CaptureRequestHolder mCaptureRequestHolder;
    private final Object mCaptureResultCheckerLock;
    private final Set<CaptureResultCheckerBase> mCaptureResultCheckerSet;
    private CaptureResultHolder mCaptureResultHolder;
    private ExecutorService mCaptureSessionCallbackExecutor;
    private ConfigStateCallback mConfigStateCallback;
    private final Object mCurrentDeviceStatusLock;
    private final OnFaceDetectionCallback mOnFaceDetectionCallback;
    private final OnFusionResultChangedCallback mOnFusionConditionChangedCallback;
    private final Set<CameraDeviceHandler.OnPreviewStartedListener> mOnPreviewStartedListenerSet;
    private final OnSceneModeChangedCallback mOnSceneModeChangedCallback;
    private OutputConfiguration mOutputConfiguration;
    private PreviewSessionRequest mPrevPreviewSessionRequest;
    private final PreviewFrameReceiver mPreviewFrameReceiver;
    private PreviewSessionRequest mPreviewRequest;
    private final RequestOneImageRetrieverCallback mRequestOneImageRetrieverCallback;
    private CameraStateCallback mStateCallback;
    private ImageReader mStreamingImageReader;
    private ImageReader mVideoThumbnailImageReader;
    private CameraDevice mCameraDevice = null;
    private CameraCaptureSession mCaptureSession = null;
    private Surface mPreviewSurface = null;
    private int mReceivedResultCount = 0;
    private Rect mLastCropRegion = new Rect(0, 0, 0, 0);
    private boolean mIsNeedCheckCropRegion = false;
    private ObjectTrackingResultChecker mObjectTrackingResultChecker = null;
    private SceneRecognitionResultChecker mSceneConditionRecognitionResultChecker = null;
    private FaceDetectionResultChecker mFaceDetectionResultChecker = null;
    private FusionResultChecker mFusionResultChecker = null;
    private CameraDeviceHandler.CameraDeviceStatus mCurrentDeviceStatus = CameraDeviceHandler.CameraDeviceStatus.STATUS_RELEASED;
    private boolean mIsCameraParametersReady = false;
    private final Object mOnPreviewStartedListenerLock = new Object();
    private final List<OneShotCaptureTask> mOneShotCaptureTaskPendingList = new ArrayList();

    interface CameraControllerCallback {
        void onCropRegionReady();

        void onDeviceError(CameraDeviceHandler.CameraSessionId cameraSessionId, CameraDeviceHandler.ErrorCode errorCode);

        void onFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult);

        void onFusionResultChanged(CameraParameters.FusionResult fusionResult);

        void onOpenCameraRequested(CameraDeviceHandler.CameraSessionId cameraSessionId);

        void onPreviewFrameUpdated(ByteBuffer byteBuffer, int i, Rect rect);

        void onReflected(CameraDeviceHandler.CameraSessionId cameraSessionId);

        void onSceneModeChanged(CameraParameters.SceneRecognitionResult sceneRecognitionResult);

        void onSessionDisconnected(CameraDeviceHandler.CameraSessionId cameraSessionId);
    }

    static /* synthetic */ int access$4708(CameraController cameraController) {
        int i = cameraController.mReceivedResultCount;
        cameraController.mReceivedResultCount = i + 1;
        return i;
    }

    void dump(StringBuilder sb) {
        sb.append("mCameraDevice:" + this.mCameraDevice + ",");
        sb.append("mCaptureSession:" + this.mCaptureSession + ",");
        sb.append("mOutputConfiguration:" + this.mOutputConfiguration + ",");
        sb.append("mStreamingImageReader:" + this.mStreamingImageReader + ",");
        sb.append("mVideoThumbnailImageReader:" + this.mVideoThumbnailImageReader + ",");
        sb.append("mPreviewSurface:" + this.mPreviewSurface + ",");
        sb.append("mCurrentDeviceStatus:" + this.mCurrentDeviceStatus + ",");
    }

    void setOnPreviewStartedListener(CameraDeviceHandler.OnPreviewStartedListener onPreviewStartedListener) {
        synchronized (this.mOnPreviewStartedListenerLock) {
            this.mOnPreviewStartedListenerSet.add(onPreviewStartedListener);
        }
    }

    void removeOnPreviewStartedListener() {
        synchronized (this.mOnPreviewStartedListenerLock) {
            this.mOnPreviewStartedListenerSet.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnPreviewStarted(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        synchronized (this.mOnPreviewStartedListenerLock) {
            Iterator<CameraDeviceHandler.OnPreviewStartedListener> it = this.mOnPreviewStartedListenerSet.iterator();
            while (it.hasNext()) {
                it.next().onPreviewStarted(cameraSessionId);
            }
            this.mOnPreviewStartedListenerSet.clear();
        }
    }

    private static class PreviewSessionRequest {
        private static final int OPERATION_MODE_SOMC_CAMERA_BT601 = 32768;
        private static final int OPERATION_MODE_SOMC_CAMERA_BT709 = 32771;
        private static final int OPERATION_MODE_SOMC_CAMERA_VIDEO_HDR = 32770;
        private boolean mIsNeedCapturedFrame;
        private boolean mIsVideoHdr;
        private final String mSessionIdTag;
        private VideoSize mVideoSize;

        private PreviewSessionRequest(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            this.mSessionIdTag = cameraSessionId.toString();
            this.mVideoSize = null;
            clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void needCapturedFrame(boolean z) {
            this.mIsNeedCapturedFrame = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void needVideoHdr(boolean z) {
            this.mIsVideoHdr = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void needVideo(VideoSize videoSize) {
            this.mVideoSize = videoSize;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isNeedCapturedFrame() {
            return this.mIsNeedCapturedFrame;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isVideoHdr() {
            return this.mIsVideoHdr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getOperationMode() {
            return this.mIsVideoHdr ? OPERATION_MODE_SOMC_CAMERA_VIDEO_HDR : (this.mVideoSize == null || this.mVideoSize == VideoSize.VGA) ? 32768 : 32771;
        }

        private void clear() {
            this.mIsNeedCapturedFrame = false;
            this.mIsVideoHdr = false;
            this.mVideoSize = null;
        }

        public String toString() {
            return getClass().getSimpleName() + "{sessionId=" + this.mSessionIdTag + ", isNeedCapturedFrame=" + this.mIsNeedCapturedFrame + ", isVideoHdr=" + this.mIsVideoHdr + ", OperationMode=" + getOperationMode() + "}";
        }

        public boolean equals(Object obj) {
            return (obj instanceof PreviewSessionRequest) && toString().equals(obj.toString());
        }

        public int hashCode() {
            return toString().hashCode();
        }
    }

    private class RequestOneImageRetrieverCallback implements ImageRetriever.OnImageRetrieverCallback {
        private RequestOneImageRetrieverCallback() {
        }

        @Override // com.sonyericsson.android.camera.device.ImageRetriever.OnImageRetrieverCallback
        public void onRetrieved(ByteBuffer byteBuffer, int i, Rect rect) {
            CameraController.this.mCallback.onPreviewFrameUpdated(byteBuffer, i, rect);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Context getApplicationContext() {
        return this.mApplicationContext;
    }

    CameraController(Context context, CameraControllerCallback cameraControllerCallback, CameraDeviceHandler.CameraDeviceHandlerInquirer cameraDeviceHandlerInquirer) {
        this.mCameraManager = null;
        this.mCaptureRequestHolder = null;
        this.mCaptureResultHolder = null;
        this.mCameraDeviceStatusThreadHandler = null;
        this.mCaptureSessionCallbackExecutor = null;
        this.mApplicationContext = context;
        HandlerThread handlerThread = new HandlerThread("StatusCallback", 10);
        handlerThread.start();
        this.mCameraDeviceStatusThreadHandler = new Handler(handlerThread.getLooper());
        this.mCaptureSessionCallbackExecutor = ThreadUtil.buildExecutor("CameraCaptureSessionCallback", 10);
        this.mOnSceneModeChangedCallback = new OnSceneModeChangedCallback();
        this.mOnFaceDetectionCallback = new OnFaceDetectionCallback();
        this.mOnFusionConditionChangedCallback = new OnFusionResultChangedCallback();
        this.mRequestOneImageRetrieverCallback = new RequestOneImageRetrieverCallback();
        this.mCurrentDeviceStatusLock = new Object();
        this.mCaptureResultCheckerLock = new Object();
        this.mPreviewFrameReceiver = new PreviewFrameReceiver();
        this.mCaptureResultCheckerSet = new HashSet();
        this.mOnPreviewStartedListenerSet = new HashSet();
        this.mCallback = cameraControllerCallback;
        this.mCameraDeviceHandler = cameraDeviceHandlerInquirer;
        this.mCameraManager = (CameraManager) context.getSystemService("camera");
        this.mCaptureRequestHolder = new CaptureRequestHolder();
        this.mCaptureResultHolder = new CaptureResultHolder();
    }

    void initializeCaptureRequest(CapturingMode capturingMode) {
        this.mCaptureRequestHolder.setDefault(capturingMode.getCameraId());
    }

    void openCamera(CameraDeviceHandler.CameraSessionId cameraSessionId, FastCapture fastCapture) {
        CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId).setRequested(CameraDeviceHandler.OpenCloseRequestStatus.CAMERA_OPENING);
        this.mCameraDeviceHandler.postCameraDeviceThread(new OpenCameraTask(cameraSessionId, fastCapture));
    }

    void closeCamera(boolean z, CameraDeviceHandler.CameraSessionId cameraSessionId) {
        stopSceneRecognition(cameraSessionId);
        stopFaceDetection(cameraSessionId);
        stopObjectTracking(cameraSessionId);
        this.mIsCameraParametersReady = false;
        synchronized (this.mCaptureResultCheckerLock) {
            this.mCaptureResultCheckerSet.clear();
            this.mSceneConditionRecognitionResultChecker = null;
        }
        CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        if (openCloseStatusInfo == null || openCloseStatusInfo.isCloseCameraTaskRequested()) {
            return;
        }
        openCloseStatusInfo.setRequested(CameraDeviceHandler.OpenCloseRequestStatus.CAMERA_CLOSING);
        CloseCameraTask closeCameraTask = new CloseCameraTask(cameraSessionId);
        if (z) {
            this.mCameraDeviceHandler.postCameraDeviceThreadSync(closeCameraTask);
        } else {
            this.mCameraDeviceHandler.postCameraDeviceThread(closeCameraTask);
        }
    }

    void stopPreview(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new StopPreviewTask(cameraSessionId));
    }

    void stopPreviewTaskSynchronized(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThreadSync(new StopPreviewTask(cameraSessionId));
        triggerRestartPreview(cameraSessionId, false);
    }

    private void closeSession(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mPrevPreviewSessionRequest = null;
        this.mCameraDeviceHandler.postCameraDeviceThread(new CloseSessionTask(cameraSessionId));
        triggerRestartPreview(cameraSessionId, false);
    }

    @WorkerThread
    private class OpenCameraTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private final FastCapture mFastCapture;

        private OpenCameraTask(CameraDeviceHandler.CameraSessionId cameraSessionId, FastCapture fastCapture) {
            super(cameraSessionId);
            this.mFastCapture = fastCapture;
            setPerformancefLog(PerfLog.OPEN_CAMERA_TASK);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            if (getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                if (CamLog.DEBUG) {
                    CamLog.d("OpenCameraTask : CloseCameraTask is already requested.");
                }
                return false;
            }
            if (!getOpenCloseStatusInfo().isErrorCaused()) {
                return true;
            }
            if (CamLog.DEBUG) {
                CamLog.d("OpenCameraTask : should not open because of error caused.");
            }
            return false;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() throws CameraAccessException {
            getOpenCloseStatusInfo().setPerformed(CameraDeviceHandler.OpenClosePerformStatus.CAMERA_OPENED);
            CameraParameters parameters = CameraController.this.mCameraDeviceHandler.getParameters(getSessionId());
            if (this.mFastCapture != FastCapture.LAUNCH_AND_CAPTURE) {
                CameraController.this.mCallback.onOpenCameraRequested(getSessionId());
            }
            if (CamLog.DEBUG) {
                CamLog.d("OpenCameraTask invoked cameraId:" + parameters.getCameraId());
            }
            CameraController.this.mStateCallback = new CameraStateCallback(getSessionId());
            try {
                CameraController.this.mCameraManager.openCamera(parameters.getCameraId().getCameraDeviceId(), CameraController.this.mStateCallback, CameraController.this.mCameraDeviceStatusThreadHandler);
                if (!CameraController.this.mCameraDeviceHandler.awaitLoadSettingsThread()) {
                    CamLog.e("OpenCameraTask() : Failed to load setting.");
                    CameraController.this.mStateCallback.cancelOpenCamera();
                    CameraController.this.onCameraOtherErrorDetected(getSessionId(), getOpenCloseStatusInfo(), CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
                    return;
                }
                CameraController.this.mCaptureRequestHolder.set(CaptureRequest.JPEG_QUALITY, Byte.valueOf((byte) MediaSavingConstants.JpegQuality.getPlatformQualityFromCameraProfile(2)));
                CameraController.this.mCameraDevice = CameraController.this.mStateCallback.waitCameraOpened();
                if (CameraController.this.mCameraDevice == null) {
                    CamLog.e("OpenCameraTask() : Failed to open camera.");
                    CameraController.this.onCameraOtherErrorDetected(getSessionId(), getOpenCloseStatusInfo(), CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
                    return;
                }
                CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
                boolean zIsNeedCreatePreviewSession = this.mFastCapture != FastCapture.LAUNCH_AND_CAPTURE ? true : CameraController.this.mCameraDeviceHandler.isNeedCreatePreviewSession();
                if (getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                    CamLog.d("OpenCameraTask() : CloseCameraTask() is already requested.");
                    return;
                }
                if (!isPreProcessing()) {
                    CameraController.this.mCameraDeviceHandler.prepareCaptureImageReader();
                }
                CameraController.this.mOneShotCaptureTaskPendingList.clear();
                if (zIsNeedCreatePreviewSession) {
                    CameraController.this.createPreviewSession(getSessionId());
                }
                if (this.mFastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
                    CameraController.this.mCallback.onOpenCameraRequested(getSessionId());
                }
                Context applicationContext = CameraController.this.getApplicationContext();
                if (applicationContext != null) {
                    new EachCameraStatusPublisher(applicationContext, parameters.getCameraId()).put(new DeviceStatus(DeviceStatus.Value.POWER_ON)).publish();
                    new GlobalCameraStatusPublisher(applicationContext).put(new BuiltInCameraIds(parameters.getCameraId())).publish();
                }
            } catch (CameraAccessException | IllegalArgumentException | SecurityException e) {
                CamLog.e("OpenCameraTask() : Failed by CameraAccessException", e);
                CameraController.this.onCameraOtherErrorDetected(getSessionId(), getOpenCloseStatusInfo(), CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
            }
        }

        private boolean isPreProcessing() {
            return (CameraController.this.mCameraDeviceHandler.getPreProcessState() == CameraDeviceHandler.PreProcessState.NOT_STARTED || CameraController.this.mCameraDeviceHandler.getPreProcessState() == CameraDeviceHandler.PreProcessState.PRE_CAPTURE_RELEASED) ? false : true;
        }
    }

    @WorkerThread
    private class CloseCameraTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private CloseCameraTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return getOpenCloseStatusInfo().isOpenCameraTaskPerformed();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            getOpenCloseStatusInfo().setPerformed(CameraDeviceHandler.OpenClosePerformStatus.CAMERA_CLOSED);
            CameraController.this.mCameraDeviceHandler.releaseRecorderOnCameraClosed();
            CamLog.d("CloseCameraTask() : Current device status = " + CameraController.this.getCameraDeviceStatus());
            try {
                switch (CameraController.this.getCameraDeviceStatus()) {
                    case STATUS_ERROR:
                    case STATUS_EVICTED:
                    case STATUS_READY:
                    case STATUS_OPENED:
                        try {
                            if (CameraController.this.mCaptureSession != null) {
                                if (CamLog.DEBUG) {
                                    CamLog.d("stopRepeating()");
                                }
                                CameraController.this.mCaptureSession.stopRepeating();
                                if (!CameraController.this.mCameraDeviceHandler.isSnapshotRunning()) {
                                    CameraController.this.mCaptureSession.abortCaptures();
                                }
                                CameraController.this.mCaptureSession.close();
                            }
                        } catch (CameraAccessException e) {
                            CamLog.e("CloseCameraTask() : Failed by CameraAccessException", e);
                        } catch (IllegalStateException e2) {
                            CamLog.e("CloseCameraTask() : Failed by IllegalStateException", e2);
                            if (CameraController.this.mCameraDeviceHandler.isIgnoreCameraError() && !getOpenCloseStatusInfo().isErrorCaused()) {
                                throw new IllegalStateException(e2);
                            }
                        }
                        CameraController.this.releaseStreamingImageReader();
                        CameraController.this.releaseVideoImageReader();
                        if (CameraController.this.mCameraDevice != null) {
                            CameraController.this.mCameraDevice.close();
                            CameraController.this.mCameraDevice = null;
                        }
                        CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_RELEASED);
                        break;
                    default:
                        Context applicationContext = CameraController.this.getApplicationContext();
                        if (applicationContext != null) {
                            new EachCameraStatusPublisher(applicationContext, CameraController.this.mCameraDeviceHandler.getParameters(getSessionId()).getCameraId()).putDefaultAll().publish();
                            new GlobalCameraStatusPublisher(applicationContext).putDefaultAll().publish();
                        }
                        if (CameraController.this.mCameraDeviceHandler.getPreProcessState() == CameraDeviceHandler.PreProcessState.NOT_STARTED || CameraController.this.mCameraDeviceHandler.getPreProcessState() == CameraDeviceHandler.PreProcessState.PRE_CAPTURE_DONE) {
                            CameraController.this.mCameraDeviceHandler.changePreProcessStateTo(CameraDeviceHandler.PreProcessState.NOT_STARTED);
                        } else {
                            CameraController.this.mCameraDeviceHandler.changePreProcessStateTo(CameraDeviceHandler.PreProcessState.PRE_CAPTURE_RELEASED);
                        }
                        CameraController.this.mStateCallback.waitCameraClosed();
                        CameraController.this.mStateCallback = null;
                        return;
                }
            } finally {
                CameraController.this.mCaptureSession = null;
                CameraController.this.mCaptureRequestDumper = null;
                CameraController.this.mOutputConfiguration = null;
                CameraController.this.mPreviewSurface = null;
                CameraController.this.mCaptureImageRetriever = null;
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void postCameraDeviceAccess() {
            this.mLatch.countDown();
        }
    }

    private class CameraStateCallback extends CameraDevice.StateCallback {
        private static final int OPEN_CLOSE_WAIT_TIME_MILLIS = 1000;
        private final Object mCameraDeviceLock;
        private CountDownLatch mCloseCameraLatch;
        private CameraDevice mDevice;
        private boolean mIsRequiredToAbandonDevice;
        private CountDownLatch mOpenCameraLatch;
        private final CameraDeviceHandler.CameraSessionId mSessionId;

        private CameraStateCallback(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            this.mOpenCameraLatch = new CountDownLatch(1);
            this.mCloseCameraLatch = new CountDownLatch(1);
            this.mCameraDeviceLock = new Object();
            this.mSessionId = cameraSessionId;
            this.mIsRequiredToAbandonDevice = false;
            this.mDevice = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CameraDevice waitCameraOpened() {
            CameraDevice cameraDevice;
            try {
                if (!this.mOpenCameraLatch.await(1000L, TimeUnit.MILLISECONDS)) {
                    CamLog.w("Opening camera device is timed-out. sessionId:" + this.mSessionId);
                }
            } catch (InterruptedException e) {
                CamLog.e("waitCameraOpened() : Failed to await", e);
            }
            synchronized (this.mCameraDeviceLock) {
                cameraDevice = this.mDevice;
                this.mDevice = null;
                this.mIsRequiredToAbandonDevice = true;
            }
            return cameraDevice;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancelOpenCamera() {
            synchronized (this.mCameraDeviceLock) {
                if (this.mDevice != null) {
                    this.mDevice.close();
                    this.mDevice = null;
                }
                this.mIsRequiredToAbandonDevice = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void waitCameraClosed() {
            try {
                if (this.mCloseCameraLatch.await(1000L, TimeUnit.MILLISECONDS)) {
                    return;
                }
                CamLog.w("Closing camera device is timed-out. sessionId:" + this.mSessionId);
            } catch (InterruptedException e) {
                CamLog.e("waitCameraClosed() : Failed to await", e);
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            synchronized (this.mCameraDeviceLock) {
                if (CamLog.DEBUG) {
                    CamLog.d("invoked sessionId:" + this.mSessionId + " isRequiredToAbandonDevice:" + this.mIsRequiredToAbandonDevice);
                }
                if (this.mIsRequiredToAbandonDevice) {
                    cameraDevice.close();
                } else {
                    this.mDevice = cameraDevice;
                }
            }
            this.mOpenCameraLatch.countDown();
            synchronized (this.mCameraDeviceLock) {
                if (this.mIsRequiredToAbandonDevice && this.mDevice != null) {
                    this.mDevice.close();
                    this.mDevice = null;
                }
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            CamLog.e("CameraStateCallback.onDisconnected() : SessionID = " + this.mSessionId);
            CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(this.mSessionId).setCameraEvicted();
            CameraController.this.mCallback.onSessionDisconnected(this.mSessionId);
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int i) {
            switch (i) {
                case 1:
                    CamLog.e("onError is called. Error:" + i + " (CameraStateCallback.ERROR_CAMERA_IN_USE). SessionID = " + this.mSessionId);
                    break;
                case 2:
                    CamLog.e("onError is called. Error:" + i + " (CameraStateCallback.ERROR_MAX_CAMERAS_IN_USE). SessionID = " + this.mSessionId);
                    break;
                case 3:
                    CamLog.e("onError is called. Error:" + i + " (CameraStateCallback.ERROR_CAMERA_DISABLED). SessionID = " + this.mSessionId);
                    break;
                case 4:
                    CamLog.e("onError is called. Error:" + i + " (CameraStateCallback.ERROR_CAMERA_DEVICE). SessionID = " + this.mSessionId);
                    break;
                case 5:
                    CamLog.e("onError is called. Error:" + i + " (CameraStateCallback.ERROR_CAMERA_SERVICE). SessionID = " + this.mSessionId);
                    break;
                default:
                    CamLog.e("onError is called. Error:" + i + " (UNKNOWN). SessionID = " + this.mSessionId);
                    break;
            }
            CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(this.mSessionId).setCameraError();
            CameraController.this.mCallback.onDeviceError(this.mSessionId, CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked sessionId:" + this.mSessionId);
            }
            this.mCloseCameraLatch.countDown();
        }
    }

    synchronized void commitParameters(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            CamLog.w("Cannot get Parameters.");
            return;
        }
        if (!parameters.needApply()) {
            CamLog.d("Parameters already applied.");
            return;
        }
        applyParameters(cameraSessionId);
        try {
        } catch (RuntimeException e) {
            if (CamLog.DEBUG) {
                throw e;
            }
            CamLog.e("Fail to valid camera parameter. : " + e.getMessage());
        }
        if (this.mCameraDevice != null) {
            CameraParameterValidator.validate(this.mCameraDevice.getId(), this.mCaptureRequestHolder);
            try {
                if (this.mPreviewSurface != null && this.mCaptureSession != null) {
                    setRepeatingRequestInternal(cameraSessionId, false);
                }
                return;
            } finally {
                parameters.applied();
            }
        }
        if (this.mPreviewSurface != null) {
            setRepeatingRequestInternal(cameraSessionId, false);
        }
        return;
    }

    @MainThread
    private boolean applyParameters(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        if (openCloseStatusInfo == null || openCloseStatusInfo.isCloseBypassCameraTaskRequested()) {
            return false;
        }
        CameraParameters parameters = openCloseStatusInfo.getParameters();
        if (parameters == null) {
            CamLog.d("Parameters not ready.");
            return false;
        }
        applyFocusMode(parameters);
        applyIso(parameters);
        applyShutterSpeed(parameters);
        applyFlashMode(parameters);
        applyExposureTimeLimit(parameters);
        applyEv(parameters);
        applyWhiteBalance(parameters);
        applyAmberBlueColor(parameters);
        applySoftSkin(parameters);
        applyHdr(parameters);
        applyMeteringArea(parameters);
        applyFpsRange(parameters);
        applyFusionMode(parameters);
        applyDistortionCorrection(parameters);
        applyPowerSaveMode(parameters);
        applyZoom(parameters);
        applySavingRequest(parameters);
        return parameters.needCreatePreviewSession();
    }

    boolean createPreviewSession(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        if (this.mPreviewRequest == null || !createPreviewSession(cameraSessionId, this.mPreviewRequest)) {
            return false;
        }
        CamLog.d("preview was requested.");
        this.mPreviewRequest = null;
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            return true;
        }
        parameters.createPreviewSessionRequestDone();
        return true;
    }

    private boolean createPreviewSession(CameraDeviceHandler.CameraSessionId cameraSessionId, PreviewSessionRequest previewSessionRequest) {
        CameraDeviceHandler.CameraDeviceStatus cameraDeviceStatus = getCameraDeviceStatus();
        if (cameraDeviceStatus == CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED || cameraDeviceStatus == CameraDeviceHandler.CameraDeviceStatus.STATUS_READY) {
            if (previewSessionRequest == null) {
                previewSessionRequest = new PreviewSessionRequest(cameraSessionId);
                CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
                if (parameters != null) {
                    if (this.mCameraDeviceHandler.isVideo()) {
                        previewSessionRequest.needVideo(parameters.getVideoSize());
                    }
                    previewSessionRequest.needVideoHdr(parameters.getVideoHdr() == VideoHdr.HDR_ON);
                    previewSessionRequest.needCapturedFrame(parameters.getShutterTrigger() == ShutterTrigger.GESTURE_SHUTTER);
                }
            }
            if (this.mPrevPreviewSessionRequest != null && previewSessionRequest.equals(this.mPrevPreviewSessionRequest)) {
                CamLog.w("Reject create preview session request due to same request: " + previewSessionRequest);
                return true;
            }
            this.mPrevPreviewSessionRequest = previewSessionRequest;
            this.mCameraDeviceHandler.postCameraDeviceThread(new CreateCaptureSessionTask(cameraSessionId, previewSessionRequest));
            return true;
        }
        CamLog.i("Cannot create preview session due to status: " + cameraDeviceStatus);
        return false;
    }

    @WorkerThread
    private class CreateCaptureSessionTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private PreviewSessionRequest mRequest;

        private CreateCaptureSessionTask(CameraDeviceHandler.CameraSessionId cameraSessionId, PreviewSessionRequest previewSessionRequest) {
            super(cameraSessionId);
            this.mRequest = previewSessionRequest;
            setPerformancefLog(PerfLog.CREATE_CAPTURE_SESSION_TASK);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController.this.getCameraDeviceStatus()) {
                case STATUS_ERROR:
                case STATUS_EVICTED:
                    return false;
                case STATUS_READY:
                case STATUS_OPENED:
                    return true;
                case STATUS_RELEASED:
                    if (getOpenCloseStatusInfo().isErrorCaused() || getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                        return false;
                    }
                    CameraController.this.triggerRestartPreview(getSessionId(), true);
                    return false;
                default:
                    throw new IllegalStateException("Failed due to wrong status in CreateCaptureSessionTask. status: " + CameraController.this.getCameraDeviceStatus());
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected void postCameraDeviceAccess() {
            switch (CameraController.this.getCameraDeviceStatus()) {
                case STATUS_ERROR:
                case STATUS_EVICTED:
                case STATUS_RELEASED:
                    CameraController.this.mOneShotCaptureTaskPendingList.clear();
                    break;
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() throws CameraAccessException {
            if (CamLog.DEBUG) {
                CamLog.d("CreateCaptureSessionTask invoked isVideo:" + CameraController.this.mCameraDeviceHandler.isVideo());
            }
            CameraParameters parameters = CameraController.this.mCameraDeviceHandler.getParameters(getSessionId());
            CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
            if (CamLog.DEBUG) {
                CamLog.d("CreateCaptureSessionTask executed request:" + this.mRequest);
            }
            if (!CameraController.this.mCameraDeviceHandler.isVideo()) {
                CameraController.this.releaseVideoImageReader();
                if (this.mRequest.isNeedCapturedFrame()) {
                    CameraController.this.prepareStreamingImageReader(parameters.getPreviewSize().width(), parameters.getPreviewSize().height());
                } else {
                    CameraController.this.releaseStreamingImageReader();
                }
            } else {
                int iWidth = parameters.getPreviewSize().width();
                int iHeight = parameters.getPreviewSize().height();
                if (this.mRequest.isVideoHdr()) {
                    CameraController.this.releaseVideoImageReader();
                } else {
                    CameraController.this.prepareVideoImageReader(iWidth, iHeight);
                }
                CameraController.this.releaseStreamingImageReader();
            }
            CameraController.this.mCaptureSession = null;
            CameraController.this.mCaptureRequestDumper = null;
            CameraController.this.mConfigStateCallback = new ConfigStateCallback(getSessionId());
            CameraController.this.mOutputConfiguration = new OutputConfiguration(PlatformDependencyResolver.getSurfaceSize(parameters.getPreviewSize(), this.mRequest.isVideoHdr()), SurfaceHolder.class);
            ArrayList arrayList = new ArrayList();
            arrayList.add(CameraController.this.mOutputConfiguration);
            if (CameraController.this.mStreamingImageReader != null) {
                arrayList.add(new OutputConfiguration(CameraController.this.mStreamingImageReader.getSurface()));
            }
            if (CameraController.this.mVideoThumbnailImageReader != null) {
                arrayList.add(new OutputConfiguration(CameraController.this.mVideoThumbnailImageReader.getSurface()));
            }
            try {
                CameraController.this.mCameraDevice.createCaptureSession(new SessionConfiguration(this.mRequest.getOperationMode(), arrayList, CameraController.this.mCaptureSessionCallbackExecutor, CameraController.this.mConfigStateCallback));
            } catch (CameraAccessException e) {
                CamLog.e("CreateCaptureSessionTask() : Failed by CameraAccessException", e);
                if (!CameraController.this.mCameraDeviceHandler.isIgnoreCameraError() || getOpenCloseStatusInfo().isErrorCaused()) {
                    CamLog.w("Failed in createCaptureSession", e);
                    return;
                }
                throw new RuntimeException("Failed in createCaptureSession by CameraAccessException. Reason:", e.getCause());
            }
        }
    }

    private class ConfigStateCallback extends CameraCaptureSession.StateCallback {
        private final CameraDeviceHandler.CameraSessionId mSessionId;

        private ConfigStateCallback(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            this.mSessionId = cameraSessionId;
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            PerfLog.ON_CONFIGURED.begin();
            CameraController.this.mCameraDeviceHandler.postCameraDeviceThread(new OnCaptureSessionConfigured(this.mSessionId, cameraCaptureSession, this));
            PerfLog.ON_CONFIGURED.end();
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            CameraController.this.mCameraDeviceHandler.postCameraDeviceThread(new OnCaptureSessionConfigured(this.mSessionId, null, this));
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onClosed(CameraCaptureSession cameraCaptureSession) {
            CamLog.d("onClosed()");
        }
    }

    @WorkerThread
    private class OnCaptureSessionConfigured extends CameraDeviceHandler.CameraDeviceAccessTask {
        private final CameraCaptureSession mResult;
        private final ConfigStateCallback mSender;

        private OnCaptureSessionConfigured(CameraDeviceHandler.CameraSessionId cameraSessionId, CameraCaptureSession cameraCaptureSession, ConfigStateCallback configStateCallback) {
            super(cameraSessionId);
            this.mResult = cameraCaptureSession;
            this.mSender = configStateCallback;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            if (getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                return false;
            }
            if (CameraController.this.getCameraDeviceStatus() == CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED) {
                return true;
            }
            if (CamLog.DEBUG) {
                CamLog.d("CurrentDeviceState is not OPENED.");
            }
            return false;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                CamLog.d("invoked sessionId:" + getSessionId());
            }
            if (CameraController.this.mConfigStateCallback != this.mSender) {
                if (CamLog.DEBUG) {
                    CamLog.d("This callback is invalid.");
                    return;
                }
                return;
            }
            if (this.mResult == null) {
                CameraController.this.onCameraOtherErrorDetected(getSessionId(), getOpenCloseStatusInfo(), CameraDeviceHandler.ErrorCode.ERROR_ON_CONFIGURE_FAILED);
                CameraController.this.mOneShotCaptureTaskPendingList.clear();
                return;
            }
            CameraController.this.mIsNeedCheckCropRegion = true;
            CameraController.this.mReceivedResultCount = 0;
            CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_READY);
            CameraController.this.mCaptureSession = this.mResult;
            CameraController.this.mCaptureRequestDumper = new CaptureRequestDumper(CameraController.CAPTURE_REQUEST_DUMP_TYPE, CameraController.this.mCaptureSession);
            if (CameraController.this.mPreviewSurface != null) {
                CameraController.this.setRepeatingRequestInternal(getSessionId(), false);
            }
            Iterator it = CameraController.this.mOneShotCaptureTaskPendingList.iterator();
            while (it.hasNext()) {
                CameraController.this.mCameraDeviceHandler.postCameraDeviceThread((OneShotCaptureTask) it.next());
                if (CamLog.DEBUG) {
                    CamLog.d("Pending OneShotCaptureTask is posted.");
                }
            }
            CameraController.this.mOneShotCaptureTaskPendingList.clear();
        }
    }

    void setSurface(CameraDeviceHandler.CameraSessionId cameraSessionId, boolean z, Surface surface) {
        if (z) {
            this.mCameraDeviceHandler.postCameraDeviceThreadSync(new SetSurfaceTask(cameraSessionId, surface));
        } else {
            this.mCameraDeviceHandler.postCameraDeviceThread(new SetSurfaceTask(cameraSessionId, surface));
        }
    }

    @WorkerThread
    private class SetSurfaceTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private final Surface mSurface;

        private SetSurfaceTask(CameraDeviceHandler.CameraSessionId cameraSessionId, Surface surface) {
            super(cameraSessionId);
            this.mSurface = surface;
            setPerformancefLog(PerfLog.SET_REPEATING_REQUEST_TASK);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return !getOpenCloseStatusInfo().isCloseCameraTaskRequested();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            if (this.mSurface == null || !this.mSurface.isValid()) {
                return;
            }
            CameraController.this.mPreviewSurface = this.mSurface;
        }
    }

    void setRepeatingRequestInternal(CameraDeviceHandler.CameraSessionId cameraSessionId, boolean z) {
        SetRepeatingRequestTask setRepeatingRequestTask;
        if (this.mIsCameraParametersReady) {
            byte b = 0;
            byte b2 = 0;
            if (this.mStreamingImageReader != null) {
                this.mStreamingImageReader.setOnImageAvailableListener(this.mPreviewFrameReceiver, this.mCameraDeviceHandler.getDeviceThreadHandler());
                setRepeatingRequestTask = new SetRepeatingRequestTask(cameraSessionId, this.mStreamingImageReader);
            } else {
                setRepeatingRequestTask = new SetRepeatingRequestTask(cameraSessionId, b2 == true ? 1 : 0);
            }
            if (z) {
                this.mCameraDeviceHandler.postCameraDeviceThreadSync(setRepeatingRequestTask);
            } else {
                this.mCameraDeviceHandler.postCameraDeviceThread(setRepeatingRequestTask);
            }
        }
    }

    @WorkerThread
    private class SetRepeatingRequestTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private static final int REPEATING_BURST_COUNT = 3;
        private final CaptureSessionCallback mCaptureSessionCallback;
        private final Surface mCaptureSurface;

        private SetRepeatingRequestTask(CameraDeviceHandler.CameraSessionId cameraSessionId, ImageReader imageReader) {
            super(cameraSessionId);
            this.mCaptureSurface = imageReader != null ? imageReader.getSurface() : null;
            this.mCaptureSessionCallback = new CaptureSessionCallback(cameraSessionId, true);
            setPerformancefLog(PerfLog.SET_REPEATING_REQUEST_TASK);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController.this.getCameraDeviceStatus()) {
                case STATUS_ERROR:
                case STATUS_EVICTED:
                case STATUS_OPENED:
                    return false;
                case STATUS_READY:
                    return true;
                case STATUS_RELEASED:
                    return (!getOpenCloseStatusInfo().isErrorCaused() && getOpenCloseStatusInfo().isCloseCameraTaskRequested()) ? false : false;
                default:
                    throw new IllegalStateException("Failed due to wrong status in SetRepeatingRequestTask. status: " + CameraController.this.getCameraDeviceStatus());
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() throws CameraAccessException {
            DeviceStatus.Value value;
            if (CameraController.this.mPreviewSurface != null) {
                if (CameraController.this.mPreviewSurface.isValid()) {
                    if (CameraController.this.mOutputConfiguration != null) {
                        try {
                            CameraController.this.mOutputConfiguration.addSurface(CameraController.this.mPreviewSurface);
                            try {
                                CameraController.this.mCaptureSession.finalizeOutputConfigurations(Arrays.asList(CameraController.this.mOutputConfiguration));
                                CameraController.this.mOutputConfiguration = null;
                            } catch (CameraAccessException unused) {
                                CamLog.e("setRepeatingRequest: finalizeOutputConfigurations failed.");
                                CameraController.this.onCameraOtherErrorDetected(getSessionId(), getOpenCloseStatusInfo(), CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
                                return;
                            } catch (IllegalArgumentException e) {
                                if (!CameraController.this.mPreviewSurface.isValid()) {
                                    CamLog.w("SetRepeatingRequestTask : Preview surface is not valid, so the request is refused.");
                                    return;
                                }
                                throw e;
                            }
                        } catch (IllegalArgumentException e2) {
                            if (!CameraController.this.mPreviewSurface.isValid()) {
                                CamLog.w("SetRepeatingRequestTask : Preview surface is not valid, so the request is refused.");
                                return;
                            }
                            throw e2;
                        }
                    }
                    ArrayList arrayList = new ArrayList();
                    if (CameraController.this.mCameraDeviceHandler.isPreCaptureOnGoing() || CameraController.this.mCameraDeviceHandler.isPreScanOnGoing()) {
                        CameraController.this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_MODE, 1);
                    }
                    if (this.mCaptureSurface == null) {
                        CaptureRequest captureRequestCreateCaptureRequest = CameraController.this.mCaptureRequestHolder.createCaptureRequest(CameraController.this.mCameraDevice, 1, CameraController.this.mPreviewSurface);
                        if (captureRequestCreateCaptureRequest == null) {
                            CamLog.i("SetRepeatingRequestTask : CaptureRequest cannot be created. mCaptureSurface is null.");
                            return;
                        }
                        arrayList.add(captureRequestCreateCaptureRequest);
                    } else {
                        for (int i = 0; i < 3; i++) {
                            Surface[] surfaceArr = {CameraController.this.mPreviewSurface};
                            if (i == 2) {
                                surfaceArr = new Surface[]{CameraController.this.mPreviewSurface, this.mCaptureSurface};
                            }
                            CaptureRequest captureRequestCreateCaptureRequest2 = CameraController.this.mCaptureRequestHolder.createCaptureRequest(CameraController.this.mCameraDevice, 1, surfaceArr);
                            if (captureRequestCreateCaptureRequest2 == null) {
                                CamLog.i("SetRepeatingRequestTask : CaptureRequest cannot be created. mCaptureSurface is not null.");
                                return;
                            }
                            arrayList.add(captureRequestCreateCaptureRequest2);
                        }
                    }
                    if (!CameraController.this.mPreviewSurface.isValid()) {
                        CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
                        if (CamLog.DEBUG) {
                            CamLog.d("Preview surface is not valid, so the request is refused.");
                            return;
                        }
                        return;
                    }
                    try {
                        PerfLog.START_PREVIEW.begin();
                        if (CamLog.DEBUG) {
                            CamLog.d("setRepeatingBurst() requestNum:" + arrayList.size());
                        }
                        CameraController.this.mCaptureSession.setRepeatingBurst(arrayList, this.mCaptureSessionCallback, CameraController.this.mCameraDeviceHandler.getDeviceThreadHandler());
                        if (CamLog.DEBUG) {
                            CameraController.this.mCaptureRequestDumper.update((CaptureRequest) arrayList.get(0));
                            CameraController.this.mCaptureRequestDumper.dump();
                        }
                        if (CameraController.this.mCameraDeviceHandler.isVideo()) {
                            if (CameraController.this.mCameraDeviceHandler.isRecording()) {
                                value = DeviceStatus.Value.VIDEO_RECORDING;
                            } else {
                                value = DeviceStatus.Value.VIDEO_PREVIEW;
                            }
                        } else {
                            value = DeviceStatus.Value.STILL_PREVIEW;
                        }
                        EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher(CameraController.this.getApplicationContext(), CameraController.this.mCameraDeviceHandler.getParameters(getSessionId()).getCameraId());
                        eachCameraStatusPublisher.put(new DeviceStatus(value));
                        eachCameraStatusPublisher.publish();
                        return;
                    } catch (CameraAccessException e3) {
                        if (CameraController.this.mCameraDeviceHandler.isIgnoreCameraError() && !getOpenCloseStatusInfo().isErrorCaused()) {
                            if (!CameraController.this.mPreviewSurface.isValid()) {
                                CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
                                CamLog.d("setRepeatingRequest : mPreviewSurface is invalid.(after performing set repeating request)");
                                return;
                            } else {
                                if (e3.getReason() == 3) {
                                    CamLog.d("Failed in setRepeatingRequest  by CameraAccessException. Reason:" + e3.getReason());
                                    CameraController.this.onCameraOtherErrorDetected(getSessionId(), getOpenCloseStatusInfo(), CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
                                    return;
                                }
                                throw new RuntimeException("Failed in setRepeatingRequest by CameraAccessException. Reason:" + e3.getReason());
                            }
                        }
                        CamLog.w("Failed in setRepeatingRequest", e3);
                        return;
                    } catch (IllegalArgumentException e4) {
                        CamLog.w("Failed in setRepeatingRequest.", e4);
                        return;
                    }
                }
                CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
                if (CamLog.DEBUG) {
                    CamLog.d("Preview surface is not valid, so the request is refused.");
                    return;
                }
                return;
            }
            if (CamLog.DEBUG) {
                CamLog.d("Preview surface is not created, so the request is refused.");
            }
        }
    }

    private void setOneTimeRequestInternal(CameraDeviceHandler.CameraSessionId cameraSessionId, CaptureRequestHolder captureRequestHolder, boolean z) throws InterruptedException {
        setOneTimeRequestInternal(cameraSessionId, captureRequestHolder, null, z);
    }

    private void setOneTimeRequestInternal(CameraDeviceHandler.CameraSessionId cameraSessionId, CaptureRequestHolder captureRequestHolder, Object obj, boolean z) throws InterruptedException {
        try {
            if (this.mCameraDevice != null) {
                CameraParameterValidator.validate(this.mCameraDevice.getId(), captureRequestHolder);
            }
        } catch (RuntimeException e) {
            if (CamLog.DEBUG) {
                throw e;
            }
            CamLog.e("Fail to valid camera parameter. : " + e.getMessage());
        }
        SetOneTimeRequestTask setOneTimeRequestTask = new SetOneTimeRequestTask(cameraSessionId, captureRequestHolder, obj);
        if (z) {
            this.mCameraDeviceHandler.postCameraDeviceThreadSync(setOneTimeRequestTask);
        } else {
            this.mCameraDeviceHandler.postCameraDeviceThread(setOneTimeRequestTask);
        }
    }

    @WorkerThread
    private class SetOneTimeRequestTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private final CaptureSessionCallback mCaptureSessionCallback;
        private final CaptureRequestHolder mRequestHolder;
        private final Object mTag;

        private SetOneTimeRequestTask(CameraDeviceHandler.CameraSessionId cameraSessionId, CaptureRequestHolder captureRequestHolder, Object obj) {
            super(cameraSessionId);
            this.mRequestHolder = captureRequestHolder;
            this.mCaptureSessionCallback = new CaptureSessionCallback(cameraSessionId);
            this.mTag = obj;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController.this.getCameraDeviceStatus()) {
                case STATUS_ERROR:
                case STATUS_EVICTED:
                case STATUS_OPENED:
                case STATUS_RELEASED:
                    return false;
                case STATUS_READY:
                    return true;
                default:
                    throw new IllegalStateException("Failed due to wrong status in SetOneTimeRequestTask. status: " + CameraController.this.getCameraDeviceStatus());
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() throws CameraAccessException {
            if (CameraController.this.mPreviewSurface != null) {
                if (CameraController.this.mPreviewSurface.isValid()) {
                    if (CameraController.this.mOutputConfiguration != null && CameraController.this.mOutputConfiguration.getSurface() == null) {
                        CameraController.this.mOutputConfiguration.addSurface(CameraController.this.mPreviewSurface);
                        try {
                            CameraController.this.mCaptureSession.finalizeOutputConfigurations(Arrays.asList(CameraController.this.mOutputConfiguration));
                            CameraController.this.mOutputConfiguration = null;
                        } catch (CameraAccessException unused) {
                            CamLog.e("SetOneTimeRequestTask: finalizeOutputConfigurations failed.");
                            CameraController.this.onCameraOtherErrorDetected(getSessionId(), getOpenCloseStatusInfo(), CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
                            return;
                        }
                    }
                    CaptureRequest captureRequestCreateCaptureRequest = this.mRequestHolder.createCaptureRequest(CameraController.this.mCameraDevice, 1, this.mTag, CameraController.this.mPreviewSurface);
                    if (captureRequestCreateCaptureRequest != null) {
                        if (!CameraController.this.mPreviewSurface.isValid()) {
                            CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
                            CamLog.d("SetOneTimeRequestTask : mPreviewSurface is invalid.(before performing capture)");
                            return;
                        }
                        try {
                            if (CamLog.DEBUG) {
                                CamLog.d("capture()");
                            }
                            CameraController.this.mCaptureSession.capture(captureRequestCreateCaptureRequest, this.mCaptureSessionCallback, CameraController.this.mCameraDeviceHandler.getDeviceThreadHandler());
                            if (CamLog.DEBUG) {
                                CameraController.this.mCaptureRequestDumper.update(captureRequestCreateCaptureRequest);
                                CameraController.this.mCaptureRequestDumper.dump();
                                return;
                            }
                            return;
                        } catch (CameraAccessException e) {
                            if (CameraController.this.mCameraDeviceHandler.isIgnoreCameraError() && !getOpenCloseStatusInfo().isErrorCaused()) {
                                if (!CameraController.this.mPreviewSurface.isValid()) {
                                    CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
                                    CamLog.d("SetOneTimeRequestTask : mPreviewSurface is invalid.(after performing capture)");
                                    return;
                                } else {
                                    throw new RuntimeException("Failed in SetOneTimeRequestTask by CameraAccessException. Reason:" + e.getReason());
                                }
                            }
                            CamLog.w("Failed in SetOneTimeRequestTask", e);
                            return;
                        } catch (IllegalArgumentException e2) {
                            CamLog.w("Failed in SetOneTimeRequestTask.", e2);
                            return;
                        }
                    }
                    CamLog.i("SetOneTimeRequestTask : CaptureRequest cannot be created.");
                    return;
                }
                CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
                CamLog.d("SetOneTimeRequestTask : mPreviewSurface is invalid.(before creating capture request)");
                return;
            }
            CamLog.d("SetOneTimeRequestTask : mPreviewSurface is null.");
        }
    }

    @WorkerThread
    private class StopPreviewTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private StopPreviewTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController.this.getCameraDeviceStatus()) {
                case STATUS_ERROR:
                case STATUS_EVICTED:
                case STATUS_RELEASED:
                    this.mLatch.countDown();
                    return false;
                case STATUS_READY:
                case STATUS_OPENED:
                    if (!getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                        return true;
                    }
                    CamLog.d("StopPreviewTask : CloseCameraTask is already requested.");
                    this.mLatch.countDown();
                    return false;
                default:
                    throw new IllegalStateException("Failed due to wrong status in StopPreviewTask. status: " + CameraController.this.getCameraDeviceStatus());
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
            try {
                try {
                    if (CameraController.this.mCaptureSession != null) {
                        if (CamLog.DEBUG) {
                            CamLog.d("stopRepeating()");
                        }
                        CameraController.this.mCaptureSession.stopRepeating();
                        if (!CameraController.this.mCameraDeviceHandler.isSnapshotRunning()) {
                            CameraController.this.mCaptureSession.abortCaptures();
                        }
                        CameraController.this.mCaptureSession.close();
                    }
                } catch (CameraAccessException | IllegalStateException e) {
                    CamLog.e("StopPreviewTask: Close session failed: ", e);
                }
                this.mLatch.countDown();
            } finally {
                CameraController.this.mCaptureSession = null;
                CameraController.this.mCaptureRequestDumper = null;
                CameraController.this.mOutputConfiguration = null;
                CameraController.this.mPreviewSurface = null;
                CameraController.this.mCaptureImageRetriever = null;
            }
        }
    }

    @WorkerThread
    private class CloseSessionTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private CloseSessionTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController.this.getCameraDeviceStatus()) {
                case STATUS_ERROR:
                case STATUS_EVICTED:
                case STATUS_OPENED:
                    return false;
                case STATUS_READY:
                    return true;
                case STATUS_RELEASED:
                    if (getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                        CamLog.d("CloseSessionTask : CloseCameraTask is already requested.");
                    }
                    if (getOpenCloseStatusInfo().isErrorCaused()) {
                        CamLog.d("CloseSessionTask : Could not execute due to error caused.");
                    }
                    return false;
                default:
                    throw new IllegalStateException("Failed due to wrong status in CloseSessionTask. status: " + CameraController.this.getCameraDeviceStatus());
            }
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            CameraController.this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED);
            try {
                try {
                    if (CameraController.this.mCaptureSession != null) {
                        if (CamLog.DEBUG) {
                            CamLog.d("stopRepeating()");
                        }
                        CameraController.this.mCaptureSession.stopRepeating();
                        if (!CameraController.this.mCameraDeviceHandler.isSnapshotRunning()) {
                            CameraController.this.mCaptureSession.abortCaptures();
                        }
                        CameraController.this.mCaptureSession.close();
                    }
                } catch (CameraAccessException e) {
                    CamLog.e("CloseSessionTask: Close session failed: ", e);
                }
            } finally {
                CameraController.this.mCaptureSession = null;
                CameraController.this.mCaptureRequestDumper = null;
            }
        }
    }

    @WorkerThread
    private class OneShotCaptureTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private final CaptureSessionCallback mCaptureSessionCallback;
        private final Surface mCaptureSurface;
        private final CaptureRequestHolder mRequestHolder;

        private OneShotCaptureTask(CameraDeviceHandler.CameraSessionId cameraSessionId, CaptureRequestHolder captureRequestHolder, ImageReader imageReader) {
            super(cameraSessionId);
            this.mRequestHolder = captureRequestHolder;
            this.mCaptureSurface = imageReader.getSurface();
            this.mCaptureSessionCallback = new CaptureSessionCallback(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController.this.getCameraDeviceStatus()) {
                case STATUS_ERROR:
                case STATUS_EVICTED:
                case STATUS_OPENED:
                    return false;
                case STATUS_READY:
                    return true;
                case STATUS_RELEASED:
                    if (getOpenCloseStatusInfo().isErrorCaused() || getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                        return false;
                    }
                    break;
            }
            throw new IllegalStateException("Failed due to wrong status in OneShotCaptureTask. status: " + CameraController.this.getCameraDeviceStatus());
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected void postCameraDeviceAccess() {
            if (AnonymousClass2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[CameraController.this.getCameraDeviceStatus().ordinal()] != 4) {
                return;
            }
            CamLog.d("Pending OneShotCaptureTask is added.");
            CameraController.this.mOneShotCaptureTaskPendingList.add(this);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() throws CameraAccessException {
            if (CameraController.this.mOutputConfiguration != null) {
                CameraController.this.mOutputConfiguration.addSurface(CameraController.this.mPreviewSurface);
                try {
                    CameraController.this.mCaptureSession.finalizeOutputConfigurations(Arrays.asList(CameraController.this.mOutputConfiguration));
                    CameraController.this.mOutputConfiguration = null;
                } catch (CameraAccessException unused) {
                    CamLog.e("OneShotCaptureTask: finalizeOutputConfigurations failed.");
                    CameraController.this.onCameraOtherErrorDetected(getSessionId(), getOpenCloseStatusInfo(), CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
                    return;
                }
            }
            CaptureRequest captureRequestCreateCaptureRequest = this.mRequestHolder.createCaptureRequest(CameraController.this.mCameraDevice, 1, this.mCaptureSurface, CameraController.this.mPreviewSurface);
            if (captureRequestCreateCaptureRequest == null) {
                CamLog.i("OneShotCaptureTask : CaptureRequest cannot be created.");
                return;
            }
            try {
                if (CamLog.DEBUG) {
                    CamLog.d("capture()");
                }
                CameraController.this.mCaptureSession.capture(captureRequestCreateCaptureRequest, this.mCaptureSessionCallback, CameraController.this.mCameraDeviceHandler.getDeviceThreadHandler());
                if (CamLog.DEBUG) {
                    CameraController.this.mCaptureRequestDumper.update(captureRequestCreateCaptureRequest);
                    CameraController.this.mCaptureRequestDumper.dump();
                }
            } catch (CameraAccessException e) {
                if (!CameraController.this.mCameraDeviceHandler.isIgnoreCameraError() || getOpenCloseStatusInfo().isErrorCaused()) {
                    CamLog.w("Failed in OneShotCaptureTask", e);
                    return;
                }
                throw new RuntimeException("Failed in OneShotCaptureTask by CameraAccessException. Reason:" + e.getReason());
            } catch (IllegalArgumentException e2) {
                CamLog.w("Failed in OneShotCaptureTask.", e2);
            }
        }
    }

    private static final class PreviewFrameReceiver implements ImageReader.OnImageAvailableListener {
        private Runnable mCallbackTask;
        private ImageRetriever.CaptureImageRequest mCaptureImageRequest;
        private byte[] mOutput;

        private PreviewFrameReceiver() {
            this.mCaptureImageRequest = null;
        }

        private boolean hasCallback(final ImageRetriever.OnImageRetrieverCallback onImageRetrieverCallback) {
            synchronized (this) {
                return this.mCaptureImageRequest != null && this.mCaptureImageRequest.callback == onImageRetrieverCallback;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void registerCallback(ImageRetriever.CaptureImageRequest captureImageRequest) {
            synchronized (this) {
                if (captureImageRequest == null) {
                    try {
                        if (this.mCallbackTask != null && this.mCaptureImageRequest != null) {
                            this.mCaptureImageRequest.handler.removeCallbacks(this.mCallbackTask);
                            this.mCallbackTask = null;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                this.mCaptureImageRequest = captureImageRequest;
            }
        }

        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader imageReader) {
            Image imageAcquireLatestImage = imageReader.acquireLatestImage();
            if (imageAcquireLatestImage == null) {
                CamLog.w("onImageAvailable() image is null");
                return;
            }
            synchronized (this) {
                if (this.mCaptureImageRequest == null) {
                    imageAcquireLatestImage.close();
                    return;
                }
                final Rect rect = new Rect(0, 0, imageAcquireLatestImage.getWidth(), imageAcquireLatestImage.getHeight());
                if (imageReader.getImageFormat() == 35) {
                    Image.Plane[] planes = imageAcquireLatestImage.getPlanes();
                    if (planes != null && planes.length == 3) {
                        int width = imageAcquireLatestImage.getWidth();
                        int height = imageAcquireLatestImage.getHeight();
                        if (width % 2 == 0 && height % 2 == 0) {
                            if (this.mOutput == null || width != 0 || height != 0) {
                                this.mOutput = new byte[((width * height) * 3) / 2];
                            }
                            Image.Plane plane = planes[0];
                            Image.Plane plane2 = planes[1];
                            Image.Plane plane3 = planes[2];
                            ImageConverter.convertFromYuv420_888ToNv21(this.mOutput, width, height, plane.getBuffer(), plane.getRowStride(), plane.getPixelStride(), plane2.getBuffer(), plane2.getRowStride(), plane2.getPixelStride(), plane3.getBuffer(), plane3.getRowStride(), plane3.getPixelStride());
                            final ByteBuffer byteBufferWrap = ByteBuffer.wrap(this.mOutput);
                            byteBufferWrap.rewind();
                            final int i = 17;
                            imageAcquireLatestImage.close();
                            final ImageRetriever.CaptureImageRequest captureImageRequest = this.mCaptureImageRequest;
                            if (this.mCaptureImageRequest.isOneShot) {
                                this.mCaptureImageRequest = null;
                            }
                            this.mCallbackTask = new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraController.PreviewFrameReceiver.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (captureImageRequest != null) {
                                        captureImageRequest.callback.onRetrieved(byteBufferWrap, i, rect);
                                    }
                                }
                            };
                            captureImageRequest.handler.post(this.mCallbackTask);
                            return;
                        }
                        CamLog.w("YUV_420_888 image is invalid. Width(" + width + ") or/are Height(" + height + ") is/are invalid.");
                        imageAcquireLatestImage.close();
                        return;
                    }
                    CamLog.w("YUV_420_888 image is invalid. Planes are invalid.");
                    imageAcquireLatestImage.close();
                    return;
                }
                imageAcquireLatestImage.close();
            }
        }
    }

    private final class CaptureSessionCallback extends CameraCaptureSession.CaptureCallback {
        private static final int IGNORE_CAPTURE_RESULT_THRESHOLD = 5;
        private boolean mIsPreviewStartNotificationRequired;
        private final CameraDeviceHandler.CameraSessionId mSessionId;

        private CaptureSessionCallback(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            this.mSessionId = cameraSessionId;
            this.mIsPreviewStartNotificationRequired = false;
        }

        private CaptureSessionCallback(CameraDeviceHandler.CameraSessionId cameraSessionId, boolean z) {
            this.mSessionId = cameraSessionId;
            this.mIsPreviewStartNotificationRequired = z;
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult) {
            if (this.mIsPreviewStartNotificationRequired) {
                this.mIsPreviewStartNotificationRequired = false;
                CameraController.this.notifyOnPreviewStarted(this.mSessionId);
                PerfLog.START_PREVIEW.end();
            }
            if (CameraController.this.getCameraDeviceStatus() == CameraDeviceHandler.CameraDeviceStatus.STATUS_READY) {
                if (CameraController.this.mReceivedResultCount < 5) {
                    CameraController.access$4708(CameraController.this);
                }
                if (CameraController.this.mIsNeedCheckCropRegion && (isCropRegionChanged(totalCaptureResult) || CameraController.this.mReceivedResultCount == 5)) {
                    CameraController.this.mCallback.onCropRegionReady();
                    CameraController.this.mIsNeedCheckCropRegion = false;
                }
                if (totalCaptureResult != null) {
                    CameraController.this.mCaptureResultHolder.add(totalCaptureResult);
                    PositionConverter.getInstance().setCropRegion((Rect) totalCaptureResult.get(CaptureResult.SCALER_CROP_REGION));
                }
                if (CamLog.VERBOSE && CameraController.this.mCaptureResultHolder != null) {
                    CameraController.this.mCaptureResultHolder.dumpLatest();
                }
                synchronized (CameraController.this.mCaptureResultCheckerLock) {
                    Iterator it = CameraController.this.mCaptureResultCheckerSet.iterator();
                    while (it.hasNext()) {
                        ((CaptureResultCheckerBase) it.next()).check(CameraController.this.mCaptureResultHolder);
                    }
                }
                return;
            }
            if (CamLog.DEBUG) {
                CamLog.d("CurrentDeviceState is not READY.");
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureFailed(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, CaptureFailure captureFailure) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked sessionId:" + this.mSessionId + " captureSession:" + cameraCaptureSession.hashCode() + " reason:" + captureFailure.getReason());
            }
        }

        private boolean isCropRegionChanged(TotalCaptureResult totalCaptureResult) {
            if (totalCaptureResult == null) {
                return false;
            }
            Rect rect = (Rect) totalCaptureResult.get(CaptureResult.SCALER_CROP_REGION);
            boolean z = CameraController.this.mReceivedResultCount > 1 && !(CameraController.this.mLastCropRegion.left == rect.left && CameraController.this.mLastCropRegion.top == rect.top && CameraController.this.mLastCropRegion.right == rect.right && CameraController.this.mLastCropRegion.bottom == rect.bottom);
            CameraController.this.mLastCropRegion = rect;
            CamLog.d("isCropRegionChanged() newRect:" + rect + " changed: " + z);
            return z;
        }
    }

    private class VideoThumbnailImageRetriever implements ImageRetriever {
        private final ImageReader mImageReader;
        private final CameraDeviceHandler.CameraSessionId mSessionId;

        @Override // com.sonyericsson.android.camera.device.ImageRetriever
        public void registerPreviewStreamingCallback(ImageRetriever.OnImageRetrieverCallback onImageRetrieverCallback, Handler handler) {
        }

        @Override // com.sonyericsson.android.camera.device.ImageRetriever
        public void unregisterPreviewStreamingCallback(ImageRetriever.OnImageRetrieverCallback onImageRetrieverCallback) {
        }

        private VideoThumbnailImageRetriever(CameraDeviceHandler.CameraSessionId cameraSessionId, ImageReader imageReader) {
            this.mSessionId = cameraSessionId;
            this.mImageReader = imageReader;
        }

        @Override // com.sonyericsson.android.camera.device.ImageRetriever
        public void requestOneShotPreviewCallback(ImageRetriever.OnImageRetrieverCallback onImageRetrieverCallback, Handler handler) {
            if (this.mImageReader == null) {
                return;
            }
            try {
                if (CameraController.this.mCameraDevice != null) {
                    CameraParameterValidator.validate(CameraController.this.mCameraDevice.getId(), CameraController.this.mCaptureRequestHolder);
                }
            } catch (RuntimeException e) {
                if (CamLog.DEBUG) {
                    throw e;
                }
                CamLog.e("Fail to valid camera parameter. : " + e.getMessage());
            }
            CameraController.this.mPreviewFrameReceiver.registerCallback(new ImageRetriever.CaptureImageRequest(onImageRetrieverCallback, true, handler));
            this.mImageReader.setOnImageAvailableListener(CameraController.this.mPreviewFrameReceiver, CameraController.this.mCameraDeviceHandler.getDeviceThreadHandler());
            CameraController.this.mCameraDeviceHandler.postCameraDeviceThread(new OneShotCaptureTask(this.mSessionId, CameraController.this.mCaptureRequestHolder, this.mImageReader));
        }
    }

    private class StreamingImageRetriever implements ImageRetriever {
        @Override // com.sonyericsson.android.camera.device.ImageRetriever
        public void requestOneShotPreviewCallback(ImageRetriever.OnImageRetrieverCallback onImageRetrieverCallback, Handler handler) {
        }

        private StreamingImageRetriever() {
        }

        @Override // com.sonyericsson.android.camera.device.ImageRetriever
        public void registerPreviewStreamingCallback(ImageRetriever.OnImageRetrieverCallback onImageRetrieverCallback, Handler handler) {
            CameraController.this.mPreviewFrameReceiver.registerCallback(new ImageRetriever.CaptureImageRequest(onImageRetrieverCallback, false, handler));
        }

        @Override // com.sonyericsson.android.camera.device.ImageRetriever
        public void unregisterPreviewStreamingCallback(ImageRetriever.OnImageRetrieverCallback onImageRetrieverCallback) {
            if (CameraController.this.mPreviewFrameReceiver.hasCallback(onImageRetrieverCallback)) {
                CameraController.this.mPreviewFrameReceiver.registerCallback(null);
            }
        }
    }

    private ImageRetriever getVideoImageRetriever(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        if (this.mCaptureImageRetriever == null) {
            this.mCaptureImageRetriever = new VideoThumbnailImageRetriever(cameraSessionId, this.mVideoThumbnailImageReader);
        }
        return this.mCaptureImageRetriever;
    }

    ImageRetriever getStreamingImageRetriever() {
        if (this.mCaptureImageRetriever == null) {
            this.mCaptureImageRetriever = new StreamingImageRetriever();
        }
        return this.mCaptureImageRetriever;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareStreamingImageReader(int i, int i2) {
        PerfLog.PREPARE_IMAGE_READER_STREAMING.begin();
        if (this.mStreamingImageReader != null) {
            if (this.mStreamingImageReader.getHeight() == i2 && this.mStreamingImageReader.getWidth() == i) {
                CamLog.d("mStreamingImageReader has bean already created");
                return;
            }
            releaseStreamingImageReader();
        }
        this.mStreamingImageReader = ImageReader.newInstance(i, i2, 35, 2);
        PerfLog.PREPARE_IMAGE_READER_STREAMING.end();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseStreamingImageReader() {
        if (this.mStreamingImageReader != null) {
            this.mStreamingImageReader.setOnImageAvailableListener(null, null);
            this.mStreamingImageReader.close();
            this.mStreamingImageReader = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareVideoImageReader(int i, int i2) {
        PerfLog.PREPARE_IMAGE_READER_VIDEO_THUMBNAIL.begin();
        if (this.mVideoThumbnailImageReader != null) {
            if (this.mVideoThumbnailImageReader.getHeight() == i2 && this.mVideoThumbnailImageReader.getWidth() == i) {
                CamLog.d("mVideoThumbnailImageReader has bean already created");
                return;
            }
            releaseVideoImageReader();
        }
        this.mVideoThumbnailImageReader = ImageReader.newInstance(i, i2, 35, 1);
        PerfLog.PREPARE_IMAGE_READER_VIDEO_THUMBNAIL.end();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseVideoImageReader() {
        if (this.mVideoThumbnailImageReader != null) {
            this.mVideoThumbnailImageReader.setOnImageAvailableListener(null, null);
            this.mVideoThumbnailImageReader.close();
            this.mVideoThumbnailImageReader = null;
        }
    }

    void startFaceDetection(CameraDeviceHandler.CameraSessionId cameraSessionId, Handler handler) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
                return;
            }
            return;
        }
        if (isFaceDetectionRunning()) {
            if (CamLog.DEBUG) {
                CamLog.d("Face detection is already running.");
            }
        } else if (PlatformCapability.isFaceDetectionAvailable(parameters.getCameraId())) {
            this.mFaceDetectionResultChecker = new FaceDetectionResultChecker(handler, this.mOnFaceDetectionCallback);
            synchronized (this.mCaptureResultCheckerLock) {
                this.mCaptureResultCheckerSet.add(this.mFaceDetectionResultChecker);
            }
            parameters.requestApply();
            this.mCaptureRequestHolder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, 1);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE, 1);
            setRepeatingRequestInternal(cameraSessionId, false);
            new EachCameraStatusPublisher(getApplicationContext(), parameters.getCameraId()).put(new FaceIdentification(FaceIdentification.Value.OFF)).put(new FaceDetection(FaceDetection.Value.ON)).publish();
        }
    }

    void stopFaceDetection(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
        } else {
            if (!isFaceDetectionRunning()) {
                if (CamLog.DEBUG) {
                    CamLog.d("Face detection is not running.");
                    return;
                }
                return;
            }
            synchronized (this.mCaptureResultCheckerLock) {
                this.mCaptureResultCheckerSet.remove(this.mFaceDetectionResultChecker);
            }
            this.mFaceDetectionResultChecker = null;
            parameters.requestApply();
            this.mCaptureRequestHolder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, 0);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE, 0);
            new EachCameraStatusPublisher(getApplicationContext(), parameters.getCameraId()).put(new FaceIdentification(FaceIdentification.Value.OFF)).put(new FaceDetection(FaceDetection.Value.OFF)).publish();
        }
    }

    void startSceneRecognition(CameraDeviceHandler.CameraSessionId cameraSessionId, Handler handler) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
                return;
            }
            return;
        }
        if (isSceneRecognitionRunning()) {
            if (CamLog.DEBUG) {
                CamLog.d("Scene recognition is already running.");
                return;
            }
            return;
        }
        CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        if (openCloseStatusInfo != null && openCloseStatusInfo.isCloseCameraTaskRequested()) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is closing or closed.");
            }
        } else if (PlatformCapability.isSceneRecognitionSupported(parameters.getCameraId())) {
            SceneRecognitionResultChecker sceneRecognitionResultChecker = new SceneRecognitionResultChecker(handler, this.mOnSceneModeChangedCallback, parameters.getCameraId());
            synchronized (this.mCaptureResultCheckerLock) {
                this.mSceneConditionRecognitionResultChecker = sceneRecognitionResultChecker;
                this.mCaptureResultCheckerSet.add(this.mSceneConditionRecognitionResultChecker);
            }
            parameters.requestApply();
            this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_MODE, 1);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE, 1);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE, 2);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE, 1);
            setRepeatingRequestInternal(cameraSessionId, false);
            new EachCameraStatusPublisher(getApplicationContext(), parameters.getCameraId()).put(new SceneRecognition(SceneRecognition.Value.ON)).publish();
        }
    }

    void stopSceneRecognition(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
                return;
            }
            return;
        }
        if (!isSceneRecognitionRunning()) {
            if (CamLog.DEBUG) {
                CamLog.d("Scene recognition is not running.");
                return;
            }
            return;
        }
        synchronized (this.mCaptureResultCheckerLock) {
            this.mCaptureResultCheckerSet.remove(this.mSceneConditionRecognitionResultChecker);
            this.mSceneConditionRecognitionResultChecker = null;
        }
        Integer api2Value = CameraParameterConverter.StillHdr.getApi2Value(parameters.getStillHdr());
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_MODE, 1);
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE, 0);
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE, api2Value);
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE, 0);
        new EachCameraStatusPublisher(getApplicationContext(), parameters.getCameraId()).put(new SceneRecognition(SceneRecognition.Value.OFF)).publish();
    }

    void startObjectTracking(CameraDeviceHandler.CameraSessionId cameraSessionId, Handler handler, Rect rect, CameraParameters.ObjectTrackingCallback objectTrackingCallback) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
                return;
            }
            return;
        }
        if (PlatformCapability.isObjectTrackingSupported(parameters.getCameraId())) {
            if (isObjectTrackingRunning()) {
                if (CamLog.DEBUG) {
                    CamLog.d("Object tracking is already running.");
                }
                synchronized (this.mCaptureResultCheckerLock) {
                    this.mCaptureResultCheckerSet.remove(this.mObjectTrackingResultChecker);
                }
                this.mObjectTrackingResultChecker = null;
            }
            this.mObjectTrackingResultChecker = new ObjectTrackingResultChecker(handler, objectTrackingCallback);
            synchronized (this.mCaptureResultCheckerLock) {
                this.mCaptureResultCheckerSet.add(this.mObjectTrackingResultChecker);
            }
            CaptureRequestHolder captureRequestHolderCopy = this.mCaptureRequestHolder.copy();
            Rect rect2 = get1x1RectOnActiveArrayCoordinate(parameters, rect.centerX(), rect.centerY());
            captureRequestHolderCopy.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER, 1);
            captureRequestHolderCopy.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA, new int[]{rect2.left, rect2.top, rect2.right, rect2.bottom});
            setOneTimeRequestInternal(cameraSessionId, captureRequestHolderCopy, this.mObjectTrackingResultChecker, false);
            LocalResearchUtil.getInstance().setObjectTrackingTarget(true);
            new EachCameraStatusPublisher(getApplicationContext(), parameters.getCameraId()).put(new ObjectTracking(ObjectTracking.Value.ON)).publish();
        }
    }

    void stopObjectTracking(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
        } else {
            if (!isObjectTrackingRunning()) {
                if (CamLog.DEBUG) {
                    CamLog.d("Object tracking is not running.");
                    return;
                }
                return;
            }
            synchronized (this.mCaptureResultCheckerLock) {
                this.mCaptureResultCheckerSet.remove(this.mObjectTrackingResultChecker);
            }
            this.mObjectTrackingResultChecker = null;
            CaptureRequestHolder captureRequestHolderCopy = this.mCaptureRequestHolder.copy();
            captureRequestHolderCopy.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER, 2);
            setOneTimeRequestInternal(cameraSessionId, captureRequestHolderCopy, false);
            LocalResearchUtil.getInstance().setObjectTrackingTarget(false);
            new EachCameraStatusPublisher(getApplicationContext(), parameters.getCameraId()).put(new ObjectTracking(ObjectTracking.Value.OFF)).publish();
        }
    }

    void startFusionMonitoring(Handler handler) {
        if (isFusionMonitoringRunning()) {
            throw new IllegalStateException("Fusion monitoring has already been started.");
        }
        this.mFusionResultChecker = new FusionResultChecker(handler, this.mOnFusionConditionChangedCallback);
        synchronized (this.mCaptureResultCheckerLock) {
            this.mCaptureResultCheckerSet.add(this.mFusionResultChecker);
            CamLog.d("add fusion status checker.");
        }
    }

    void stopFusionMonitoring() {
        if (isFusionMonitoringRunning()) {
            synchronized (this.mCaptureResultCheckerLock) {
                this.mCaptureResultCheckerSet.remove(this.mFusionResultChecker);
                CamLog.d("remove fusion status checker.");
            }
            this.mFusionResultChecker = null;
        }
    }

    CameraParameters.FusionResult getLatestFusionResult() {
        if (isFusionMonitoringRunning()) {
            return this.mFusionResultChecker.getLatestFusionResult();
        }
        return new CameraParameters.FusionResult();
    }

    boolean isObjectTrackingRunning() {
        return this.mObjectTrackingResultChecker != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSceneRecognitionRunning() {
        return this.mSceneConditionRecognitionResultChecker != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFaceDetectionRunning() {
        return this.mFaceDetectionResultChecker != null;
    }

    private boolean isFusionMonitoringRunning() {
        return this.mFusionResultChecker != null;
    }

    CameraDeviceHandler.CameraDeviceStatus getCameraDeviceStatus() {
        CameraDeviceHandler.CameraDeviceStatus cameraDeviceStatus;
        synchronized (this.mCurrentDeviceStatusLock) {
            cameraDeviceStatus = this.mCurrentDeviceStatus;
        }
        return cameraDeviceStatus;
    }

    void setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus cameraDeviceStatus) {
        synchronized (this.mCurrentDeviceStatusLock) {
            if (CamLog.DEBUG) {
                CamLog.d("CurrentDeviceState change to " + cameraDeviceStatus + " from " + this.mCurrentDeviceStatus);
            }
            this.mCurrentDeviceStatus = cameraDeviceStatus;
        }
    }

    private class OnSceneModeChangedCallback implements CameraParameters.SceneRecognitionCallback {
        private OnSceneModeChangedCallback() {
        }

        @Override // com.sonyericsson.android.camera.device.CameraParameters.SceneRecognitionCallback
        public void onSceneModeChanged(CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
            if (CameraController.this.isSceneRecognitionRunning()) {
                if (sceneRecognitionResult == null) {
                    LocalResearchUtil.getInstance().clearRecognizedScene();
                }
                CameraController.this.mCallback.onSceneModeChanged(sceneRecognitionResult);
            } else if (CamLog.DEBUG) {
                CamLog.d("Scene recognition is stopped.");
            }
        }
    }

    private class OnFaceDetectionCallback implements CameraParameters.FaceDetectionCallback {
        private OnFaceDetectionCallback() {
        }

        @Override // com.sonyericsson.android.camera.device.CameraParameters.FaceDetectionCallback
        public void onFaceDetection(CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (CameraController.this.isFaceDetectionRunning()) {
                CameraController.this.mCallback.onFaceDetected(faceDetectionResult);
            } else if (CamLog.DEBUG) {
                CamLog.d("Face detection is stopped.");
            }
        }
    }

    private class OnFusionResultChangedCallback implements CameraParameters.FusionResultCallback {
        private OnFusionResultChangedCallback() {
        }

        @Override // com.sonyericsson.android.camera.device.CameraParameters.FusionResultCallback
        public void onFusionResultChanged(CameraParameters.FusionResult fusionResult) {
            CameraController.this.mCallback.onFusionResultChanged(fusionResult);
        }
    }

    void setSelectedFacePosition(CameraDeviceHandler.CameraSessionId cameraSessionId, int i, int i2) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
        } else {
            CaptureRequestHolder captureRequestHolderCopy = this.mCaptureRequestHolder.copy();
            Rect rect = get1x1RectOnActiveArrayCoordinate(parameters, i, i2);
            int[] iArr = {rect.left, rect.top, rect.right, rect.bottom};
            captureRequestHolderCopy.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER, 1);
            captureRequestHolderCopy.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA, iArr);
            setOneTimeRequestInternal(cameraSessionId, captureRequestHolderCopy, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCameraOtherErrorDetected(CameraDeviceHandler.CameraSessionId cameraSessionId, CameraDeviceHandler.CameraSessionInfo cameraSessionInfo, CameraDeviceHandler.ErrorCode errorCode) {
        setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_ERROR);
        cameraSessionInfo.setOtherError();
        this.mCallback.onDeviceError(cameraSessionId, errorCode);
    }

    void commit(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mIsCameraParametersReady = true;
        if (this.mPreviewRequest != null) {
            if (applyParameters(cameraSessionId)) {
                closeSession(cameraSessionId);
                if (createPreviewSession(cameraSessionId)) {
                    CamLog.d("commit: preview trigger was fired.");
                    return;
                }
                return;
            }
            return;
        }
        commitParameters(cameraSessionId);
    }

    void triggerRestartPreview(CameraDeviceHandler.CameraSessionId cameraSessionId, boolean z) {
        CamLog.d("triggerRestartPreview()");
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            return;
        }
        if (z) {
            this.mPrevPreviewSessionRequest = null;
            parameters.forceRequestCreatePreviewSession();
        }
        if (this.mPreviewRequest == null) {
            this.mPreviewRequest = new PreviewSessionRequest(cameraSessionId);
        }
        if (this.mCameraDeviceHandler.isVideo()) {
            this.mPreviewRequest.needVideo(parameters.getVideoSize());
        } else {
            this.mPreviewRequest.needVideo(null);
        }
        this.mPreviewRequest.needVideoHdr(parameters.getVideoHdr() == VideoHdr.HDR_ON);
        this.mPreviewRequest.needCapturedFrame(parameters.getShutterTrigger() == ShutterTrigger.GESTURE_SHUTTER);
    }

    private void applyAmberBlueColor(CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_MODE, 1);
        if (cameraParameters.getWhiteBalance().equals("auto")) {
            this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_AWB_MODE, 1);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB, Integer.valueOf(cameraParameters.getAwbColorCompensationAb()));
        }
    }

    private void applyFlashMode(CameraParameters cameraParameters) {
        int api2Value = CameraParameterConverter.FlashMode.getApi2Value(cameraParameters.getFlashMode());
        int api2Value2 = CameraParameterConverter.AeMode.getApi2Value(cameraParameters.getAeMode(), cameraParameters.getFlashMode());
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_MODE, 1);
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_AE_MODE, 1);
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_MODE, Integer.valueOf(api2Value2));
        this.mCaptureRequestHolder.set(CaptureRequest.FLASH_MODE, Integer.valueOf(api2Value));
    }

    private void applyZoom(CameraParameters cameraParameters) {
        float maxZoomRatio = ((PlatformCapability.getMaxZoomRatio(cameraParameters.getCameraId()) - 1.0f) * cameraParameters.getZoom()) + 1.0f;
        Rect activeArraySize = PlatformCapability.getActiveArraySize(cameraParameters.getCameraId());
        Rect rect = new Rect(activeArraySize);
        if (CamLog.VERBOSE) {
            CamLog.v("applyZoom() : ActiveArraySize = " + activeArraySize);
        }
        float f = 1.0f - (1.0f / maxZoomRatio);
        rect.inset((int) Math.floor(rect.width() * 0.5f * f), (int) Math.floor(rect.height() * 0.5f * f));
        if (CamLog.VERBOSE) {
            CamLog.v("applyZoom() : CropSize = " + rect);
        }
        this.mCaptureRequestHolder.set(CaptureRequest.SCALER_CROP_REGION, rect);
    }

    void changeProviderDeviceStatusToRecording(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
        } else {
            Context applicationContext = getApplicationContext();
            if (applicationContext != null) {
                new EachCameraStatusPublisher(applicationContext, parameters.getCameraId()).putFromParameter(parameters, parameters.getCameraId(), this.mCameraDeviceHandler.isVideo()).put(new DeviceStatus(DeviceStatus.Value.VIDEO_RECORDING)).publish();
            }
        }
    }

    private void applyPowerSaveMode(CameraParameters cameraParameters) {
        Integer numValueOf = Integer.valueOf(CameraParameterConverter.PowerSaveMode.getApi2Value(cameraParameters.getPowerMode()));
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_POWER_SAVE_MODE, numValueOf);
        if (numValueOf.intValue() == 2 && PlatformCapability.isFaceDetectionAvailable(cameraParameters.getCameraId()) && isFaceDetectionRunning()) {
            synchronized (this.mCaptureResultCheckerLock) {
                this.mCaptureResultCheckerSet.remove(this.mFaceDetectionResultChecker);
            }
            this.mFaceDetectionResultChecker = null;
        }
    }

    private void applyEv(CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(cameraParameters.getExposureCompensation()));
    }

    private void applyExposureTimeLimit(CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT, Long.valueOf(cameraParameters.getExposureTimeLimit()));
    }

    private void applyFocusMode(CameraParameters cameraParameters) {
        Integer numValueOf = Integer.valueOf(CameraParameterConverter.FocusMode.getApi2Value(cameraParameters.getFocusMode()));
        Integer numValueOf2 = Integer.valueOf(CameraParameterConverter.FocusArea.getApi2Value(cameraParameters.getFocusArea()));
        List<Rect> focusRectangles = cameraParameters.getFocusRectangles();
        int size = focusRectangles.size();
        MeteringRectangle[] meteringRectangleArr = new MeteringRectangle[size];
        if (CamLog.VERBOSE) {
            CamLog.v("setFocusRectangles() : rectangles = " + focusRectangles);
        }
        for (int i = 0; i < size; i++) {
            Rect rect = focusRectangles.get(i);
            if (CamLog.VERBOSE) {
                CamLog.v("setFocusRectangles() : area.rect = " + rect);
            }
            if (rect.isEmpty()) {
                meteringRectangleArr[i] = new MeteringRectangle(PlatformCapability.getActiveArraySize(cameraParameters.getCameraId()), 1);
            } else {
                meteringRectangleArr[i] = new MeteringRectangle(new Rect(focusRectangles.get(i).centerX(), focusRectangles.get(i).centerY(), focusRectangles.get(i).centerX() + 1, focusRectangles.get(i).centerY() + 1), 1);
            }
            if (CamLog.VERBOSE) {
                CamLog.v("setFocusRectangles() : rectangle = " + meteringRectangleArr[i].toString());
            }
        }
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_MODE, 1);
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_AF_MODE, numValueOf);
        this.mCaptureRequestHolder.set(CaptureRequest.LENS_FOCUS_DISTANCE, Float.valueOf(cameraParameters.getFocusRange()));
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AF_REGION_MODE, numValueOf2);
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_AF_REGIONS, meteringRectangleArr);
    }

    private void applyHdr(CameraParameters cameraParameters) {
        Integer api2Value;
        if (isSceneRecognitionRunning() || (api2Value = CameraParameterConverter.StillHdr.getApi2Value(cameraParameters.getStillHdr())) == null) {
            return;
        }
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE, api2Value);
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE, 0);
    }

    private void applyIso(CameraParameters cameraParameters) {
        int iso = cameraParameters.getIso();
        if (iso <= 0) {
            iso = Math.max(((Integer) PlatformCapability.getSupportedIsoRange(cameraParameters.getCameraId()).getLower()).intValue(), ((Integer) PlatformCapability.getSupportedFusionIsoRange(cameraParameters.getCameraId()).getLower()).intValue());
        }
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_SENSITIVITY_HINT, Integer.valueOf(iso));
    }

    private void applyMeteringArea(CameraParameters cameraParameters) {
        Integer numValueOf = Integer.valueOf(CameraParameterConverter.MeteringMode.getApi2Value(cameraParameters.getMeteringMode()));
        MeteringRectangle meteringRectangle = new MeteringRectangle(cameraParameters.getMeteringArea(), 1);
        if (CamLog.VERBOSE) {
            CamLog.v("applyMeteringArea() : aeRegion = (" + meteringRectangle.getRect() + ", " + meteringRectangle.getMeteringWeight() + ")");
        }
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_MODE, 1);
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_REGION_MODE, numValueOf);
        if (numValueOf.intValue() == 5) {
            this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_AE_REGIONS, new MeteringRectangle[]{meteringRectangle});
        } else {
            this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_AE_REGIONS, new MeteringRectangle[]{new MeteringRectangle(new Rect(PlatformCapability.getActiveArraySize(cameraParameters.getCameraId())), 0)});
        }
    }

    Rect getPhotoPreviewSize(CameraDeviceHandler.CameraSessionId cameraSessionId, CameraInfo.CameraId cameraId, Rect rect) {
        if (this.mCameraDeviceHandler.getParameters(cameraSessionId) != null) {
            CamLog.d("sony-preferred-preview-size-for-still:" + PlatformCapability.getPreferredPreviewSizeForStill(cameraId));
        }
        Rect preferredPreviewSizeForStill = PlatformCapability.getPreferredPreviewSizeForStill(cameraId);
        if (preferredPreviewSizeForStill.width() == 0 || preferredPreviewSizeForStill.height() == 0) {
            preferredPreviewSizeForStill = PlatformDependencyResolver.getPreferredPreviewSizeFromCaptureSize(rect);
            CamLog.w("preferredPreviewSize is invalid. Get preferredPreviewSize from captureSize: " + preferredPreviewSizeForStill);
        }
        return CommonPlatformDependencyResolver.getOptimalStillPreviewRect(rect, preferredPreviewSizeForStill, PlatformCapability.getSupportedPreviewSizes(cameraId));
    }

    private void applySoftSkin(CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL, Integer.valueOf(cameraParameters.getSoftSkin()));
    }

    Rect getVideoPreviewSize(CameraDeviceHandler.CameraSessionId cameraSessionId, CameraInfo.CameraId cameraId, Rect rect) {
        if (this.mCameraDeviceHandler.getParameters(cameraSessionId) != null) {
            CamLog.d("preferred-preview-size-for-video:" + PlatformCapability.getPreferredPreviewSizeForVideo(cameraId));
        }
        Rect preferredPreviewSizeForVideo = PlatformCapability.getPreferredPreviewSizeForVideo(cameraId);
        if (preferredPreviewSizeForVideo.width() == 0 || preferredPreviewSizeForVideo.height() == 0) {
            preferredPreviewSizeForVideo = PlatformDependencyResolver.getPreferredPreviewSizeFromCaptureSize(rect);
            CamLog.w("preferredPreviewSize is invalid. Get preferredPreviewSize from videoSize: " + preferredPreviewSizeForVideo);
        }
        return CommonPlatformDependencyResolver.getOptimalVideoPreviewRect(rect, preferredPreviewSizeForVideo, PlatformCapability.getSupportedPreviewSizes(cameraId));
    }

    private void applyFpsRange(CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, cameraParameters.getFpsRange());
    }

    private void applyWhiteBalance(CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_MODE, 1);
        if (cameraParameters.getAwbColorCompensationAb() == 0) {
            this.mCaptureRequestHolder.set(CaptureRequest.CONTROL_AWB_MODE, Integer.valueOf(CameraParameterConverter.AwbMode.getApi2Value(cameraParameters.getWhiteBalance())));
        }
    }

    private void applySavingRequest(CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set(CaptureRequest.JPEG_ORIENTATION, Integer.valueOf(cameraParameters.getRotation()));
        this.mCaptureRequestHolder.set(CaptureRequest.JPEG_GPS_LOCATION, cameraParameters.getGpsData());
    }

    private void applyShutterSpeed(CameraParameters cameraParameters) {
        long shutterSpeed = cameraParameters.getShutterSpeed();
        if (shutterSpeed > 0) {
            if (PlatformCapability.getMinShutterSpeed(cameraParameters.getCameraId()) > shutterSpeed) {
                shutterSpeed = PlatformCapability.getMinShutterSpeed(cameraParameters.getCameraId());
            } else if (shutterSpeed > PlatformCapability.getMaxShutterSpeed(cameraParameters.getCameraId())) {
                shutterSpeed = PlatformCapability.getMaxShutterSpeed(cameraParameters.getCameraId());
            }
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT, Long.valueOf(shutterSpeed));
        }
    }

    private void applyFusionMode(CameraParameters cameraParameters) {
        Integer api2Value = CameraParameterConverter.FusionMode.getApi2Value(cameraParameters.getFusionMode());
        if (api2Value != null) {
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_FUSION_MODE, api2Value);
        }
    }

    private void applyDistortionCorrection(CameraParameters cameraParameters) {
        Integer api2Value = CameraParameterConverter.DistortionCorrection.getApi2Value(cameraParameters.getDistortionCorrection());
        if (api2Value != null) {
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE, api2Value);
        }
    }

    void requestOnePreviewFrame(CameraDeviceHandler.CameraSessionId cameraSessionId, Handler handler) {
        if (this.mCameraDeviceHandler.getParameters(cameraSessionId).getVideoHdr() != VideoHdr.HDR_ON) {
            getVideoImageRetriever(cameraSessionId).requestOneShotPreviewCallback(this.mRequestOneImageRetrieverCallback, handler);
        }
    }

    private Rect get1x1RectOnActiveArrayCoordinate(CameraParameters cameraParameters, int i, int i2) {
        Rect activeArraySize = PlatformCapability.getActiveArraySize(cameraParameters.getCameraId());
        int i3 = i + 1;
        int i4 = i2 + 1;
        if (activeArraySize.contains(i3, i4)) {
            return new Rect(i, i2, i3, i4);
        }
        if (activeArraySize.contains(i, i2)) {
            return new Rect(i - 1, i2 - 1, i, i2);
        }
        int iCenterX = activeArraySize.centerX();
        int iCenterY = activeArraySize.centerY();
        return new Rect(iCenterX, iCenterY, iCenterX + 1, iCenterY + 1);
    }

    AfParametersReflectedChecker createAfParametersResultChecker(CameraDeviceHandler.CameraSessionId cameraSessionId, Handler handler) {
        return new AfParametersReflectedChecker(handler, new RequestSnapshotReadyAfterAfParametersReflected(cameraSessionId), this.mCaptureRequestHolder.copy());
    }

    boolean isAfParametersReflectedToDevice(AfParametersReflectedChecker afParametersReflectedChecker) {
        CaptureResult latest = this.mCaptureResultHolder.getLatest();
        return latest != null && afParametersReflectedChecker.checkSync(latest);
    }

    Runnable requestSnapshotReadyAfterAfParametersReflected(final CameraDeviceHandler.CameraSessionId cameraSessionId, final AfParametersReflectedChecker afParametersReflectedChecker) {
        synchronized (this.mCaptureResultCheckerLock) {
            this.mCaptureResultCheckerSet.add(afParametersReflectedChecker);
        }
        return new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraController.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (CameraController.this.mCaptureResultCheckerLock) {
                    if (CameraController.this.mCaptureResultCheckerSet.contains(afParametersReflectedChecker)) {
                        CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
                        if (openCloseStatusInfo != null) {
                            CameraController.this.onCameraOtherErrorDetected(cameraSessionId, openCloseStatusInfo, CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
                            CamLog.e("requestSnapshotReadyAfterAfParametersReflected : Failed to reflect the parameters to the device.");
                        } else {
                            CamLog.d("requestSnapshotReadyAfterAfParametersReflected(): This session is already closed.");
                        }
                    }
                }
            }
        };
    }

    private class RequestSnapshotReadyAfterAfParametersReflected implements CameraParameters.AfParametersCallback {
        private final CameraDeviceHandler.CameraSessionId mSessionId;

        private RequestSnapshotReadyAfterAfParametersReflected(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            this.mSessionId = cameraSessionId;
        }

        @Override // com.sonyericsson.android.camera.device.CameraParameters.AfParametersCallback
        public void onReflected(AfParametersReflectedChecker afParametersReflectedChecker) {
            synchronized (CameraController.this.mCaptureResultCheckerLock) {
                CameraController.this.mCaptureResultCheckerSet.remove(afParametersReflectedChecker);
            }
            CameraController.this.mCallback.onReflected(this.mSessionId);
        }
    }
}
