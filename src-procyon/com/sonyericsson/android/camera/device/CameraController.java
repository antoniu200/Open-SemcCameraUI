// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import com.sonyericsson.cameracommon.status.CameraStatusPublisher;
import android.media.Image$Plane;
import android.media.Image;
import com.sonyericsson.cameracommon.status.global.BuiltInCameraIds;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import java.util.Arrays;
import java.util.concurrent.Executor;
import android.hardware.camera2.params.SessionConfiguration;
import android.view.SurfaceHolder;
import android.hardware.camera2.CameraCaptureSession$StateCallback;
import com.sonyericsson.cameracommon.status.GlobalCameraStatusPublisher;
import android.hardware.camera2.CameraAccessException;
import android.support.annotation.WorkerThread;
import android.hardware.camera2.CaptureFailure;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.CameraCaptureSession$CaptureCallback;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import android.hardware.camera2.CameraDevice$StateCallback;
import java.nio.ByteBuffer;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.cameracommon.status.eachcamera.SceneRecognition;
import com.sonyericsson.cameracommon.status.eachcamera.ObjectTracking;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.cameracommon.status.eachcamera.FaceDetection;
import com.sonyericsson.cameracommon.status.eachcamera.FaceIdentification;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import android.hardware.camera2.CaptureResult;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.cameracommon.device.CommonPlatformDependencyResolver;
import com.sonyericsson.cameracommon.status.eachcamera.DeviceStatus;
import com.sonyericsson.cameracommon.status.EachCameraStatusPublisher;
import android.media.ImageReader$OnImageAvailableListener;
import com.sonyericsson.android.camera.util.PerfLog;
import java.util.Iterator;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import android.location.Location;
import android.support.annotation.MainThread;
import android.util.Range;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.util.CamLog;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.CaptureRequest$Key;
import android.hardware.camera2.CaptureRequest;
import java.util.HashSet;
import com.sonyericsson.android.camera.util.ThreadUtil;
import android.os.HandlerThread;
import java.util.ArrayList;
import android.media.ImageReader;
import android.view.Surface;
import android.hardware.camera2.params.OutputConfiguration;
import java.util.List;
import android.graphics.Rect;
import java.util.concurrent.ExecutorService;
import android.hardware.camera2.CameraCaptureSession;
import java.util.Set;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.hardware.camera2.CameraDevice;
import android.content.Context;

class CameraController
{
    private static final CaptureRequestDumper.Type CAPTURE_REQUEST_DUMP_TYPE;
    private final Context mApplicationContext;
    private final CameraControllerCallback mCallback;
    private CameraDevice mCameraDevice;
    private final CameraDeviceHandler.CameraDeviceHandlerInquirer mCameraDeviceHandler;
    private Handler mCameraDeviceStatusThreadHandler;
    private CameraManager mCameraManager;
    private ImageRetriever mCaptureImageRetriever;
    private CaptureRequestDumper mCaptureRequestDumper;
    private CaptureRequestHolder mCaptureRequestHolder;
    private final Object mCaptureResultCheckerLock;
    private final Set<CaptureResultCheckerBase> mCaptureResultCheckerSet;
    private CaptureResultHolder mCaptureResultHolder;
    private CameraCaptureSession mCaptureSession;
    private ExecutorService mCaptureSessionCallbackExecutor;
    private ConfigStateCallback mConfigStateCallback;
    private CameraDeviceHandler.CameraDeviceStatus mCurrentDeviceStatus;
    private final Object mCurrentDeviceStatusLock;
    private FaceDetectionResultChecker mFaceDetectionResultChecker;
    private FusionResultChecker mFusionResultChecker;
    private boolean mIsCameraParametersReady;
    private boolean mIsNeedCheckCropRegion;
    private Rect mLastCropRegion;
    private ObjectTrackingResultChecker mObjectTrackingResultChecker;
    private final OnFaceDetectionCallback mOnFaceDetectionCallback;
    private final OnFusionResultChangedCallback mOnFusionConditionChangedCallback;
    private final Object mOnPreviewStartedListenerLock;
    private final Set<CameraDeviceHandler.OnPreviewStartedListener> mOnPreviewStartedListenerSet;
    private final OnSceneModeChangedCallback mOnSceneModeChangedCallback;
    private final List<OneShotCaptureTask> mOneShotCaptureTaskPendingList;
    private OutputConfiguration mOutputConfiguration;
    private PreviewSessionRequest mPrevPreviewSessionRequest;
    private final PreviewFrameReceiver mPreviewFrameReceiver;
    private PreviewSessionRequest mPreviewRequest;
    private Surface mPreviewSurface;
    private int mReceivedResultCount;
    private final RequestOneImageRetrieverCallback mRequestOneImageRetrieverCallback;
    private SceneRecognitionResultChecker mSceneConditionRecognitionResultChecker;
    private CameraStateCallback mStateCallback;
    private ImageReader mStreamingImageReader;
    private ImageReader mVideoThumbnailImageReader;
    
    static {
        CAPTURE_REQUEST_DUMP_TYPE = CaptureRequestDumper.Type.DIFF;
    }
    
    CameraController(final Context mApplicationContext, final CameraControllerCallback mCallback, final CameraDeviceHandler.CameraDeviceHandlerInquirer mCameraDeviceHandler) {
        this.mCameraDevice = null;
        this.mCaptureSession = null;
        this.mCameraManager = null;
        this.mCaptureRequestHolder = null;
        this.mCaptureResultHolder = null;
        this.mCameraDeviceStatusThreadHandler = null;
        this.mCaptureSessionCallbackExecutor = null;
        this.mPreviewSurface = null;
        this.mReceivedResultCount = 0;
        this.mLastCropRegion = new Rect(0, 0, 0, 0);
        this.mIsNeedCheckCropRegion = false;
        this.mObjectTrackingResultChecker = null;
        this.mSceneConditionRecognitionResultChecker = null;
        this.mFaceDetectionResultChecker = null;
        this.mFusionResultChecker = null;
        this.mCurrentDeviceStatus = CameraDeviceHandler.CameraDeviceStatus.STATUS_RELEASED;
        this.mIsCameraParametersReady = false;
        this.mOnPreviewStartedListenerLock = new Object();
        this.mOneShotCaptureTaskPendingList = new ArrayList<OneShotCaptureTask>();
        this.mApplicationContext = mApplicationContext;
        final HandlerThread handlerThread = new HandlerThread("StatusCallback", 10);
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
        this.mCaptureResultCheckerSet = new HashSet<CaptureResultCheckerBase>();
        this.mOnPreviewStartedListenerSet = new HashSet<CameraDeviceHandler.OnPreviewStartedListener>();
        this.mCallback = mCallback;
        this.mCameraDeviceHandler = mCameraDeviceHandler;
        this.mCameraManager = (CameraManager)mApplicationContext.getSystemService("camera");
        this.mCaptureRequestHolder = new CaptureRequestHolder();
        this.mCaptureResultHolder = new CaptureResultHolder();
    }
    
    private void applyAmberBlueColor(final CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_MODE, 1);
        if (cameraParameters.getWhiteBalance().equals("auto")) {
            this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_AWB_MODE, 1);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB, cameraParameters.getAwbColorCompensationAb());
        }
    }
    
    private void applyDistortionCorrection(final CameraParameters cameraParameters) {
        final Integer api2Value = CameraParameterConverter.DistortionCorrection.getApi2Value(cameraParameters.getDistortionCorrection());
        if (api2Value != null) {
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_DISTORTION_CORRECTION_MODE, api2Value);
        }
    }
    
    private void applyEv(final CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, cameraParameters.getExposureCompensation());
    }
    
    private void applyExposureTimeLimit(final CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT, cameraParameters.getExposureTimeLimit());
    }
    
    private void applyFlashMode(final CameraParameters cameraParameters) {
        final int api2Value = CameraParameterConverter.FlashMode.getApi2Value(cameraParameters.getFlashMode());
        final int api2Value2 = CameraParameterConverter.AeMode.getApi2Value(cameraParameters.getAeMode(), cameraParameters.getFlashMode());
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_MODE, 1);
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_AE_MODE, 1);
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_MODE, api2Value2);
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.FLASH_MODE, api2Value);
    }
    
    private void applyFocusMode(final CameraParameters cameraParameters) {
        final int api2Value = CameraParameterConverter.FocusMode.getApi2Value(cameraParameters.getFocusMode());
        final int api2Value2 = CameraParameterConverter.FocusArea.getApi2Value(cameraParameters.getFocusArea());
        final List<Rect> focusRectangles = cameraParameters.getFocusRectangles();
        final int size = focusRectangles.size();
        final MeteringRectangle[] array = new MeteringRectangle[size];
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setFocusRectangles() : rectangles = ");
            sb.append(focusRectangles);
            CamLog.v(sb.toString());
        }
        for (int i = 0; i < size; ++i) {
            final Rect obj = focusRectangles.get(i);
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("setFocusRectangles() : area.rect = ");
                sb2.append(obj);
                CamLog.v(sb2.toString());
            }
            if (obj.isEmpty()) {
                array[i] = new MeteringRectangle(PlatformCapability.getActiveArraySize(cameraParameters.getCameraId()), 1);
            }
            else {
                array[i] = new MeteringRectangle(new Rect(((Rect)focusRectangles.get(i)).centerX(), ((Rect)focusRectangles.get(i)).centerY(), ((Rect)focusRectangles.get(i)).centerX() + 1, ((Rect)focusRectangles.get(i)).centerY() + 1), 1);
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("setFocusRectangles() : rectangle = ");
                sb3.append(array[i].toString());
                CamLog.v(sb3.toString());
            }
        }
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_MODE, 1);
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_AF_MODE, api2Value);
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Float>)CaptureRequest.LENS_FOCUS_DISTANCE, cameraParameters.getFocusRange());
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AF_REGION_MODE, api2Value2);
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<MeteringRectangle[]>)CaptureRequest.CONTROL_AF_REGIONS, array);
    }
    
    private void applyFpsRange(final CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Range<Integer>>)CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, cameraParameters.getFpsRange());
    }
    
    private void applyFusionMode(final CameraParameters cameraParameters) {
        final Integer api2Value = CameraParameterConverter.FusionMode.getApi2Value(cameraParameters.getFusionMode());
        if (api2Value != null) {
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_FUSION_MODE, api2Value);
        }
    }
    
    private void applyHdr(final CameraParameters cameraParameters) {
        if (this.isSceneRecognitionRunning()) {
            return;
        }
        final Integer api2Value = CameraParameterConverter.StillHdr.getApi2Value(cameraParameters.getStillHdr());
        if (api2Value != null) {
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE, api2Value);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE, 0);
        }
    }
    
    private void applyIso(final CameraParameters cameraParameters) {
        int i;
        if ((i = cameraParameters.getIso()) <= 0) {
            i = Math.max((int)PlatformCapability.getSupportedIsoRange(cameraParameters.getCameraId()).getLower(), (int)PlatformCapability.getSupportedFusionIsoRange(cameraParameters.getCameraId()).getLower());
        }
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_SENSITIVITY_HINT, i);
    }
    
    private void applyMeteringArea(final CameraParameters cameraParameters) {
        final Integer value = CameraParameterConverter.MeteringMode.getApi2Value(cameraParameters.getMeteringMode());
        final MeteringRectangle meteringRectangle = new MeteringRectangle(cameraParameters.getMeteringArea(), 1);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("applyMeteringArea() : aeRegion = (");
            sb.append(meteringRectangle.getRect());
            sb.append(", ");
            sb.append(meteringRectangle.getMeteringWeight());
            sb.append(")");
            CamLog.v(sb.toString());
        }
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_MODE, 1);
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_REGION_MODE, value);
        if (value == 5) {
            this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<MeteringRectangle[]>)CaptureRequest.CONTROL_AE_REGIONS, new MeteringRectangle[] { meteringRectangle });
        }
        else {
            this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<MeteringRectangle[]>)CaptureRequest.CONTROL_AE_REGIONS, new MeteringRectangle[] { new MeteringRectangle(new Rect(PlatformCapability.getActiveArraySize(cameraParameters.getCameraId())), 0) });
        }
    }
    
    @MainThread
    private boolean applyParameters(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        final CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        if (openCloseStatusInfo == null || openCloseStatusInfo.isCloseBypassCameraTaskRequested()) {
            return false;
        }
        final CameraParameters parameters = openCloseStatusInfo.getParameters();
        if (parameters == null) {
            CamLog.d("Parameters not ready.");
            return false;
        }
        this.applyFocusMode(parameters);
        this.applyIso(parameters);
        this.applyShutterSpeed(parameters);
        this.applyFlashMode(parameters);
        this.applyExposureTimeLimit(parameters);
        this.applyEv(parameters);
        this.applyWhiteBalance(parameters);
        this.applyAmberBlueColor(parameters);
        this.applySoftSkin(parameters);
        this.applyHdr(parameters);
        this.applyMeteringArea(parameters);
        this.applyFpsRange(parameters);
        this.applyFusionMode(parameters);
        this.applyDistortionCorrection(parameters);
        this.applyPowerSaveMode(parameters);
        this.applyZoom(parameters);
        this.applySavingRequest(parameters);
        return parameters.needCreatePreviewSession();
    }
    
    private void applyPowerSaveMode(final CameraParameters cameraParameters) {
        final Integer value = CameraParameterConverter.PowerSaveMode.getApi2Value(cameraParameters.getPowerMode());
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_POWER_SAVE_MODE, value);
        if (value == 2 && PlatformCapability.isFaceDetectionAvailable(cameraParameters.getCameraId()) && this.isFaceDetectionRunning()) {
            synchronized (this.mCaptureResultCheckerLock) {
                this.mCaptureResultCheckerSet.remove(this.mFaceDetectionResultChecker);
                monitorexit(this.mCaptureResultCheckerLock);
                this.mFaceDetectionResultChecker = null;
            }
        }
    }
    
    private void applySavingRequest(final CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.JPEG_ORIENTATION, cameraParameters.getRotation());
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Location>)CaptureRequest.JPEG_GPS_LOCATION, cameraParameters.getGpsData());
    }
    
    private void applyShutterSpeed(final CameraParameters cameraParameters) {
        final long shutterSpeed = cameraParameters.getShutterSpeed();
        if (shutterSpeed > 0L) {
            long l;
            if (PlatformCapability.getMinShutterSpeed(cameraParameters.getCameraId()) > shutterSpeed) {
                l = PlatformCapability.getMinShutterSpeed(cameraParameters.getCameraId());
            }
            else {
                l = shutterSpeed;
                if (shutterSpeed > PlatformCapability.getMaxShutterSpeed(cameraParameters.getCameraId())) {
                    l = PlatformCapability.getMaxShutterSpeed(cameraParameters.getCameraId());
                }
            }
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT, l);
        }
    }
    
    private void applySoftSkin(final CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL, cameraParameters.getSoftSkin());
    }
    
    private void applyWhiteBalance(final CameraParameters cameraParameters) {
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_MODE, 1);
        if (cameraParameters.getAwbColorCompensationAb() == 0) {
            this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_AWB_MODE, CameraParameterConverter.AwbMode.getApi2Value(cameraParameters.getWhiteBalance()));
        }
    }
    
    private void applyZoom(final CameraParameters cameraParameters) {
        final float maxZoomRatio = PlatformCapability.getMaxZoomRatio(cameraParameters.getCameraId());
        final float zoom = cameraParameters.getZoom();
        final Rect activeArraySize = PlatformCapability.getActiveArraySize(cameraParameters.getCameraId());
        final Rect obj = new Rect(activeArraySize);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("applyZoom() : ActiveArraySize = ");
            sb.append(activeArraySize);
            CamLog.v(sb.toString());
        }
        final float n = (float)obj.width();
        final float n2 = 1.0f - 1.0f / ((maxZoomRatio - 1.0f) * zoom + 1.0f);
        obj.inset((int)Math.floor(n * 0.5f * n2), (int)Math.floor(obj.height() * 0.5f * n2));
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("applyZoom() : CropSize = ");
            sb2.append(obj);
            CamLog.v(sb2.toString());
        }
        this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Rect>)CaptureRequest.SCALER_CROP_REGION, obj);
    }
    
    private void closeSession(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mPrevPreviewSessionRequest = null;
        this.mCameraDeviceHandler.postCameraDeviceThread(new CloseSessionTask(cameraSessionId));
        this.triggerRestartPreview(cameraSessionId, false);
    }
    
    private boolean createPreviewSession(final CameraDeviceHandler.CameraSessionId cameraSessionId, PreviewSessionRequest previewSessionRequest) {
        final CameraDeviceHandler.CameraDeviceStatus cameraDeviceStatus = this.getCameraDeviceStatus();
        if (cameraDeviceStatus != CameraDeviceHandler.CameraDeviceStatus.STATUS_OPENED && cameraDeviceStatus != CameraDeviceHandler.CameraDeviceStatus.STATUS_READY) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot create preview session due to status: ");
            sb.append(cameraDeviceStatus);
            CamLog.i(sb.toString());
            return false;
        }
        PreviewSessionRequest previewSessionRequest2;
        if ((previewSessionRequest2 = previewSessionRequest) == null) {
            previewSessionRequest = new PreviewSessionRequest(cameraSessionId);
            final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
            previewSessionRequest2 = previewSessionRequest;
            if (parameters != null) {
                if (this.mCameraDeviceHandler.isVideo()) {
                    previewSessionRequest.needVideo(parameters.getVideoSize());
                }
                previewSessionRequest.needVideoHdr(parameters.getVideoHdr() == VideoHdr.HDR_ON);
                previewSessionRequest.needCapturedFrame(parameters.getShutterTrigger() == ShutterTrigger.GESTURE_SHUTTER);
                previewSessionRequest2 = previewSessionRequest;
            }
        }
        if (this.mPrevPreviewSessionRequest != null && previewSessionRequest2.equals(this.mPrevPreviewSessionRequest)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Reject create preview session request due to same request: ");
            sb2.append(previewSessionRequest2);
            CamLog.w(sb2.toString());
            return true;
        }
        this.mPrevPreviewSessionRequest = previewSessionRequest2;
        this.mCameraDeviceHandler.postCameraDeviceThread(new CreateCaptureSessionTask(cameraSessionId, previewSessionRequest2));
        return true;
    }
    
    private Rect get1x1RectOnActiveArrayCoordinate(final CameraParameters cameraParameters, int centerY, int centerX) {
        final Rect activeArraySize = PlatformCapability.getActiveArraySize(cameraParameters.getCameraId());
        final int n = centerY + 1;
        final int n2 = centerX + 1;
        if (activeArraySize.contains(n, n2)) {
            return new Rect(centerY, centerX, n, n2);
        }
        if (activeArraySize.contains(centerY, centerX)) {
            return new Rect(centerY - 1, centerX - 1, centerY, centerX);
        }
        centerX = activeArraySize.centerX();
        centerY = activeArraySize.centerY();
        return new Rect(centerX, centerY, centerX + 1, centerY + 1);
    }
    
    private Context getApplicationContext() {
        return this.mApplicationContext;
    }
    
    private ImageRetriever getVideoImageRetriever(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        if (this.mCaptureImageRetriever == null) {
            this.mCaptureImageRetriever = new VideoThumbnailImageRetriever(cameraSessionId, this.mVideoThumbnailImageReader);
        }
        return this.mCaptureImageRetriever;
    }
    
    private boolean isFaceDetectionRunning() {
        return this.mFaceDetectionResultChecker != null;
    }
    
    private boolean isFusionMonitoringRunning() {
        return this.mFusionResultChecker != null;
    }
    
    private boolean isSceneRecognitionRunning() {
        return this.mSceneConditionRecognitionResultChecker != null;
    }
    
    private void notifyOnPreviewStarted(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        synchronized (this.mOnPreviewStartedListenerLock) {
            final Iterator<CameraDeviceHandler.OnPreviewStartedListener> iterator = this.mOnPreviewStartedListenerSet.iterator();
            while (iterator.hasNext()) {
                iterator.next().onPreviewStarted(cameraSessionId);
            }
            this.mOnPreviewStartedListenerSet.clear();
        }
    }
    
    private void onCameraOtherErrorDetected(final CameraDeviceHandler.CameraSessionId cameraSessionId, final CameraDeviceHandler.CameraSessionInfo cameraSessionInfo, final CameraDeviceHandler.ErrorCode errorCode) {
        this.setCameraDeviceStatus(CameraDeviceHandler.CameraDeviceStatus.STATUS_ERROR);
        cameraSessionInfo.setOtherError();
        this.mCallback.onDeviceError(cameraSessionId, errorCode);
    }
    
    private void prepareStreamingImageReader(final int n, final int n2) {
        PerfLog.PREPARE_IMAGE_READER_STREAMING.begin();
        if (this.mStreamingImageReader != null) {
            if (this.mStreamingImageReader.getHeight() == n2 && this.mStreamingImageReader.getWidth() == n) {
                CamLog.d("mStreamingImageReader has bean already created");
                return;
            }
            this.releaseStreamingImageReader();
        }
        this.mStreamingImageReader = ImageReader.newInstance(n, n2, 35, 2);
        PerfLog.PREPARE_IMAGE_READER_STREAMING.end();
    }
    
    private void prepareVideoImageReader(final int n, final int n2) {
        PerfLog.PREPARE_IMAGE_READER_VIDEO_THUMBNAIL.begin();
        if (this.mVideoThumbnailImageReader != null) {
            if (this.mVideoThumbnailImageReader.getHeight() == n2 && this.mVideoThumbnailImageReader.getWidth() == n) {
                CamLog.d("mVideoThumbnailImageReader has bean already created");
                return;
            }
            this.releaseVideoImageReader();
        }
        this.mVideoThumbnailImageReader = ImageReader.newInstance(n, n2, 35, 1);
        PerfLog.PREPARE_IMAGE_READER_VIDEO_THUMBNAIL.end();
    }
    
    private void releaseStreamingImageReader() {
        if (this.mStreamingImageReader != null) {
            this.mStreamingImageReader.setOnImageAvailableListener((ImageReader$OnImageAvailableListener)null, (Handler)null);
            this.mStreamingImageReader.close();
            this.mStreamingImageReader = null;
        }
    }
    
    private void releaseVideoImageReader() {
        if (this.mVideoThumbnailImageReader != null) {
            this.mVideoThumbnailImageReader.setOnImageAvailableListener((ImageReader$OnImageAvailableListener)null, (Handler)null);
            this.mVideoThumbnailImageReader.close();
            this.mVideoThumbnailImageReader = null;
        }
    }
    
    private void setOneTimeRequestInternal(final CameraDeviceHandler.CameraSessionId cameraSessionId, final CaptureRequestHolder captureRequestHolder, final Object o, final boolean b) {
        try {
            if (this.mCameraDevice != null) {
                CameraParameterValidator.validate(this.mCameraDevice.getId(), captureRequestHolder);
            }
        }
        catch (final RuntimeException ex) {
            if (CamLog.DEBUG) {
                throw ex;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Fail to valid camera parameter. : ");
            sb.append(ex.getMessage());
            CamLog.e(sb.toString());
        }
        final SetOneTimeRequestTask setOneTimeRequestTask = new SetOneTimeRequestTask(cameraSessionId, captureRequestHolder, o);
        if (b) {
            this.mCameraDeviceHandler.postCameraDeviceThreadSync(setOneTimeRequestTask);
        }
        else {
            this.mCameraDeviceHandler.postCameraDeviceThread(setOneTimeRequestTask);
        }
    }
    
    private void setOneTimeRequestInternal(final CameraDeviceHandler.CameraSessionId cameraSessionId, final CaptureRequestHolder captureRequestHolder, final boolean b) {
        this.setOneTimeRequestInternal(cameraSessionId, captureRequestHolder, null, b);
    }
    
    void changeProviderDeviceStatusToRecording(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        final Context applicationContext = this.getApplicationContext();
        if (applicationContext != null) {
            ((CameraStatusPublisher<DeviceStatus>)new EachCameraStatusPublisher(applicationContext, parameters.getCameraId()).putFromParameter(parameters, parameters.getCameraId(), this.mCameraDeviceHandler.isVideo())).put(new DeviceStatus(DeviceStatus.Value.VIDEO_RECORDING)).publish();
        }
    }
    
    void closeCamera(final boolean b, final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.stopSceneRecognition(cameraSessionId);
        this.stopFaceDetection(cameraSessionId);
        this.stopObjectTracking(cameraSessionId);
        this.mIsCameraParametersReady = false;
        Object o = this.mCaptureResultCheckerLock;
        synchronized (o) {
            this.mCaptureResultCheckerSet.clear();
            this.mSceneConditionRecognitionResultChecker = null;
            monitorexit(o);
            o = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
            if (o != null && !((CameraDeviceHandler.CameraSessionInfo)o).isCloseCameraTaskRequested()) {
                ((CameraDeviceHandler.CameraSessionInfo)o).setRequested(CameraDeviceHandler.OpenCloseRequestStatus.CAMERA_CLOSING);
                final CloseCameraTask closeCameraTask = new CloseCameraTask(cameraSessionId);
                if (b) {
                    this.mCameraDeviceHandler.postCameraDeviceThreadSync(closeCameraTask);
                }
                else {
                    this.mCameraDeviceHandler.postCameraDeviceThread(closeCameraTask);
                }
            }
        }
    }
    
    void commit(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mIsCameraParametersReady = true;
        if (this.mPreviewRequest != null) {
            if (this.applyParameters(cameraSessionId)) {
                this.closeSession(cameraSessionId);
                if (this.createPreviewSession(cameraSessionId)) {
                    CamLog.d("commit: preview trigger was fired.");
                }
            }
        }
        else {
            this.commitParameters(cameraSessionId);
        }
    }
    
    void commitParameters(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        synchronized (this) {
            final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
            if (parameters == null) {
                CamLog.w("Cannot get Parameters.");
                return;
            }
            if (!parameters.needApply()) {
                CamLog.d("Parameters already applied.");
                return;
            }
            this.applyParameters(cameraSessionId);
            try {
                if (this.mCameraDevice != null) {
                    CameraParameterValidator.validate(this.mCameraDevice.getId(), this.mCaptureRequestHolder);
                }
            }
            catch (final RuntimeException ex) {
                if (CamLog.DEBUG) {
                    throw ex;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Fail to valid camera parameter. : ");
                sb.append(ex.getMessage());
                CamLog.e(sb.toString());
            }
            try {
                if (this.mPreviewSurface != null && this.mCaptureSession != null) {
                    this.setRepeatingRequestInternal(cameraSessionId, false);
                }
            }
            finally {
                parameters.applied();
            }
        }
    }
    
    AfParametersReflectedChecker createAfParametersResultChecker(final CameraDeviceHandler.CameraSessionId cameraSessionId, final Handler handler) {
        return new AfParametersReflectedChecker(handler, new RequestSnapshotReadyAfterAfParametersReflected(cameraSessionId), this.mCaptureRequestHolder.copy());
    }
    
    boolean createPreviewSession(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        if (this.mPreviewRequest != null && this.createPreviewSession(cameraSessionId, this.mPreviewRequest)) {
            CamLog.d("preview was requested.");
            this.mPreviewRequest = null;
            final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
            if (parameters != null) {
                parameters.createPreviewSessionRequestDone();
            }
            return true;
        }
        return false;
    }
    
    void dump(final StringBuilder sb) {
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("mCameraDevice:");
        sb2.append(this.mCameraDevice);
        sb2.append(",");
        sb.append(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("mCaptureSession:");
        sb3.append(this.mCaptureSession);
        sb3.append(",");
        sb.append(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("mOutputConfiguration:");
        sb4.append(this.mOutputConfiguration);
        sb4.append(",");
        sb.append(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("mStreamingImageReader:");
        sb5.append(this.mStreamingImageReader);
        sb5.append(",");
        sb.append(sb5.toString());
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("mVideoThumbnailImageReader:");
        sb6.append(this.mVideoThumbnailImageReader);
        sb6.append(",");
        sb.append(sb6.toString());
        final StringBuilder sb7 = new StringBuilder();
        sb7.append("mPreviewSurface:");
        sb7.append(this.mPreviewSurface);
        sb7.append(",");
        sb.append(sb7.toString());
        final StringBuilder sb8 = new StringBuilder();
        sb8.append("mCurrentDeviceStatus:");
        sb8.append(this.mCurrentDeviceStatus);
        sb8.append(",");
        sb.append(sb8.toString());
    }
    
    CameraDeviceHandler.CameraDeviceStatus getCameraDeviceStatus() {
        synchronized (this.mCurrentDeviceStatusLock) {
            return this.mCurrentDeviceStatus;
        }
    }
    
    CameraParameters.FusionResult getLatestFusionResult() {
        if (this.isFusionMonitoringRunning()) {
            return this.mFusionResultChecker.getLatestFusionResult();
        }
        return new CameraParameters.FusionResult();
    }
    
    Rect getPhotoPreviewSize(final CameraDeviceHandler.CameraSessionId cameraSessionId, final CameraInfo.CameraId cameraId, final Rect rect) {
        if (this.mCameraDeviceHandler.getParameters(cameraSessionId) != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sony-preferred-preview-size-for-still:");
            sb.append(PlatformCapability.getPreferredPreviewSizeForStill(cameraId));
            CamLog.d(sb.toString());
        }
        final Rect preferredPreviewSizeForStill = PlatformCapability.getPreferredPreviewSizeForStill(cameraId);
        if (preferredPreviewSizeForStill.width() != 0) {
            final Rect preferredPreviewSizeFromCaptureSize = preferredPreviewSizeForStill;
            if (preferredPreviewSizeForStill.height() != 0) {
                return CommonPlatformDependencyResolver.getOptimalStillPreviewRect(rect, preferredPreviewSizeFromCaptureSize, PlatformCapability.getSupportedPreviewSizes(cameraId));
            }
        }
        final Rect preferredPreviewSizeFromCaptureSize = PlatformDependencyResolver.getPreferredPreviewSizeFromCaptureSize(rect);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("preferredPreviewSize is invalid. Get preferredPreviewSize from captureSize: ");
        sb2.append(preferredPreviewSizeFromCaptureSize);
        CamLog.w(sb2.toString());
        return CommonPlatformDependencyResolver.getOptimalStillPreviewRect(rect, preferredPreviewSizeFromCaptureSize, PlatformCapability.getSupportedPreviewSizes(cameraId));
    }
    
    ImageRetriever getStreamingImageRetriever() {
        if (this.mCaptureImageRetriever == null) {
            this.mCaptureImageRetriever = new StreamingImageRetriever();
        }
        return this.mCaptureImageRetriever;
    }
    
    Rect getVideoPreviewSize(final CameraDeviceHandler.CameraSessionId cameraSessionId, final CameraInfo.CameraId cameraId, final Rect rect) {
        if (this.mCameraDeviceHandler.getParameters(cameraSessionId) != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("preferred-preview-size-for-video:");
            sb.append(PlatformCapability.getPreferredPreviewSizeForVideo(cameraId));
            CamLog.d(sb.toString());
        }
        final Rect preferredPreviewSizeForVideo = PlatformCapability.getPreferredPreviewSizeForVideo(cameraId);
        if (preferredPreviewSizeForVideo.width() != 0) {
            final Rect preferredPreviewSizeFromCaptureSize = preferredPreviewSizeForVideo;
            if (preferredPreviewSizeForVideo.height() != 0) {
                return CommonPlatformDependencyResolver.getOptimalVideoPreviewRect(rect, preferredPreviewSizeFromCaptureSize, PlatformCapability.getSupportedPreviewSizes(cameraId));
            }
        }
        final Rect preferredPreviewSizeFromCaptureSize = PlatformDependencyResolver.getPreferredPreviewSizeFromCaptureSize(rect);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("preferredPreviewSize is invalid. Get preferredPreviewSize from videoSize: ");
        sb2.append(preferredPreviewSizeFromCaptureSize);
        CamLog.w(sb2.toString());
        return CommonPlatformDependencyResolver.getOptimalVideoPreviewRect(rect, preferredPreviewSizeFromCaptureSize, PlatformCapability.getSupportedPreviewSizes(cameraId));
    }
    
    void initializeCaptureRequest(final CapturingMode capturingMode) {
        this.mCaptureRequestHolder.setDefault(capturingMode.getCameraId());
    }
    
    boolean isAfParametersReflectedToDevice(final AfParametersReflectedChecker afParametersReflectedChecker) {
        final CaptureResult latest = this.mCaptureResultHolder.getLatest();
        return latest != null && afParametersReflectedChecker.checkSync(latest);
    }
    
    boolean isObjectTrackingRunning() {
        return this.mObjectTrackingResultChecker != null;
    }
    
    void openCamera(final CameraDeviceHandler.CameraSessionId cameraSessionId, final FastCapture fastCapture) {
        CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId).setRequested(CameraDeviceHandler.OpenCloseRequestStatus.CAMERA_OPENING);
        this.mCameraDeviceHandler.postCameraDeviceThread(new OpenCameraTask(cameraSessionId, fastCapture));
    }
    
    void removeOnPreviewStartedListener() {
        synchronized (this.mOnPreviewStartedListenerLock) {
            this.mOnPreviewStartedListenerSet.clear();
        }
    }
    
    void requestOnePreviewFrame(final CameraDeviceHandler.CameraSessionId cameraSessionId, final Handler handler) {
        if (this.mCameraDeviceHandler.getParameters(cameraSessionId).getVideoHdr() != VideoHdr.HDR_ON) {
            this.getVideoImageRetriever(cameraSessionId).requestOneShotPreviewCallback((ImageRetriever.OnImageRetrieverCallback)this.mRequestOneImageRetrieverCallback, handler);
        }
    }
    
    Runnable requestSnapshotReadyAfterAfParametersReflected(final CameraDeviceHandler.CameraSessionId cameraSessionId, final AfParametersReflectedChecker afParametersReflectedChecker) {
        synchronized (this.mCaptureResultCheckerLock) {
            this.mCaptureResultCheckerSet.add(afParametersReflectedChecker);
            monitorexit(this.mCaptureResultCheckerLock);
            return new Runnable(this, afParametersReflectedChecker, cameraSessionId) {
                final CameraController this$0;
                final AfParametersReflectedChecker val$checker;
                final CameraDeviceHandler.CameraSessionId val$sessionId;
                
                @Override
                public void run() {
                    synchronized (this.this$0.mCaptureResultCheckerLock) {
                        if (!this.this$0.mCaptureResultCheckerSet.contains(this.val$checker)) {
                            return;
                        }
                        monitorexit(this.this$0.mCaptureResultCheckerLock);
                        final CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(this.val$sessionId);
                        if (openCloseStatusInfo == null) {
                            CamLog.d("requestSnapshotReadyAfterAfParametersReflected(): This session is already closed.");
                            return;
                        }
                        this.this$0.onCameraOtherErrorDetected(this.val$sessionId, openCloseStatusInfo, CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
                        CamLog.e("requestSnapshotReadyAfterAfParametersReflected : Failed to reflect the parameters to the device.");
                    }
                }
            };
        }
    }
    
    void setCameraDeviceStatus(final CameraDeviceHandler.CameraDeviceStatus cameraDeviceStatus) {
        synchronized (this.mCurrentDeviceStatusLock) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("CurrentDeviceState change to ");
                sb.append(cameraDeviceStatus);
                sb.append(" from ");
                sb.append(this.mCurrentDeviceStatus);
                CamLog.d(sb.toString());
            }
            this.mCurrentDeviceStatus = cameraDeviceStatus;
        }
    }
    
    void setOnPreviewStartedListener(final CameraDeviceHandler.OnPreviewStartedListener onPreviewStartedListener) {
        synchronized (this.mOnPreviewStartedListenerLock) {
            this.mOnPreviewStartedListenerSet.add(onPreviewStartedListener);
        }
    }
    
    void setRepeatingRequestInternal(final CameraDeviceHandler.CameraSessionId cameraSessionId, final boolean b) {
        if (!this.mIsCameraParametersReady) {
            return;
        }
        SetRepeatingRequestTask setRepeatingRequestTask;
        if (this.mStreamingImageReader != null) {
            this.mStreamingImageReader.setOnImageAvailableListener((ImageReader$OnImageAvailableListener)this.mPreviewFrameReceiver, this.mCameraDeviceHandler.getDeviceThreadHandler());
            setRepeatingRequestTask = new SetRepeatingRequestTask(cameraSessionId, this.mStreamingImageReader);
        }
        else {
            setRepeatingRequestTask = new SetRepeatingRequestTask(cameraSessionId, (ImageReader)null);
        }
        if (b) {
            this.mCameraDeviceHandler.postCameraDeviceThreadSync(setRepeatingRequestTask);
        }
        else {
            this.mCameraDeviceHandler.postCameraDeviceThread(setRepeatingRequestTask);
        }
    }
    
    void setSelectedFacePosition(final CameraDeviceHandler.CameraSessionId cameraSessionId, int right, int top) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        final CaptureRequestHolder copy = this.mCaptureRequestHolder.copy();
        final Rect get1x1RectOnActiveArrayCoordinate = this.get1x1RectOnActiveArrayCoordinate(parameters, right, top);
        final int left = get1x1RectOnActiveArrayCoordinate.left;
        top = get1x1RectOnActiveArrayCoordinate.top;
        right = get1x1RectOnActiveArrayCoordinate.right;
        final int bottom = get1x1RectOnActiveArrayCoordinate.bottom;
        copy.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER, 1);
        copy.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA, new int[] { left, top, right, bottom });
        this.setOneTimeRequestInternal(cameraSessionId, copy, false);
    }
    
    void setSurface(final CameraDeviceHandler.CameraSessionId cameraSessionId, final boolean b, final Surface surface) {
        if (b) {
            this.mCameraDeviceHandler.postCameraDeviceThreadSync(new SetSurfaceTask(cameraSessionId, surface));
        }
        else {
            this.mCameraDeviceHandler.postCameraDeviceThread(new SetSurfaceTask(cameraSessionId, surface));
        }
    }
    
    void startFaceDetection(final CameraDeviceHandler.CameraSessionId cameraSessionId, final Handler handler) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        if (this.isFaceDetectionRunning()) {
            if (CamLog.DEBUG) {
                CamLog.d("Face detection is already running.");
            }
            return;
        }
        if (PlatformCapability.isFaceDetectionAvailable(parameters.getCameraId())) {
            this.mFaceDetectionResultChecker = new FaceDetectionResultChecker(handler, this.mOnFaceDetectionCallback);
            synchronized (this.mCaptureResultCheckerLock) {
                this.mCaptureResultCheckerSet.add(this.mFaceDetectionResultChecker);
                monitorexit(this.mCaptureResultCheckerLock);
                parameters.requestApply();
                this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.STATISTICS_FACE_DETECT_MODE, 1);
                this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE, 1);
                this.setRepeatingRequestInternal(cameraSessionId, false);
                ((CameraStatusPublisher<FaceIdentification>)new EachCameraStatusPublisher(this.getApplicationContext(), parameters.getCameraId())).put(new FaceIdentification(FaceIdentification.Value.OFF)).put((FaceIdentification)new FaceDetection(FaceDetection.Value.ON)).publish();
            }
        }
    }
    
    void startFusionMonitoring(final Handler handler) {
        if (this.isFusionMonitoringRunning()) {
            throw new IllegalStateException("Fusion monitoring has already been started.");
        }
        this.mFusionResultChecker = new FusionResultChecker(handler, this.mOnFusionConditionChangedCallback);
        synchronized (this.mCaptureResultCheckerLock) {
            this.mCaptureResultCheckerSet.add(this.mFusionResultChecker);
            CamLog.d("add fusion status checker.");
        }
    }
    
    void startObjectTracking(final CameraDeviceHandler.CameraSessionId cameraSessionId, Handler handler, Rect get1x1RectOnActiveArrayCoordinate, final CameraParameters.ObjectTrackingCallback objectTrackingCallback) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        if (PlatformCapability.isObjectTrackingSupported(parameters.getCameraId())) {
            if (this.isObjectTrackingRunning()) {
                if (CamLog.DEBUG) {
                    CamLog.d("Object tracking is already running.");
                }
                synchronized (this.mCaptureResultCheckerLock) {
                    this.mCaptureResultCheckerSet.remove(this.mObjectTrackingResultChecker);
                    monitorexit(this.mCaptureResultCheckerLock);
                    this.mObjectTrackingResultChecker = null;
                }
            }
            this.mObjectTrackingResultChecker = new ObjectTrackingResultChecker(handler, objectTrackingCallback);
            handler = (Handler)this.mCaptureResultCheckerLock;
            synchronized (handler) {
                this.mCaptureResultCheckerSet.add(this.mObjectTrackingResultChecker);
                monitorexit(handler);
                handler = (Handler)this.mCaptureRequestHolder.copy();
                get1x1RectOnActiveArrayCoordinate = this.get1x1RectOnActiveArrayCoordinate(parameters, get1x1RectOnActiveArrayCoordinate.centerX(), get1x1RectOnActiveArrayCoordinate.centerY());
                ((CaptureRequestHolder)handler).set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER, Integer.valueOf(1));
                ((CaptureRequestHolder)handler).set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA, new int[] { get1x1RectOnActiveArrayCoordinate.left, get1x1RectOnActiveArrayCoordinate.top, get1x1RectOnActiveArrayCoordinate.right, get1x1RectOnActiveArrayCoordinate.bottom });
                this.setOneTimeRequestInternal(cameraSessionId, (CaptureRequestHolder)handler, this.mObjectTrackingResultChecker, false);
                LocalResearchUtil.getInstance().setObjectTrackingTarget(true);
                ((CameraStatusPublisher<ObjectTracking>)new EachCameraStatusPublisher(this.getApplicationContext(), parameters.getCameraId())).put(new ObjectTracking(ObjectTracking.Value.ON)).publish();
            }
        }
    }
    
    void startSceneRecognition(final CameraDeviceHandler.CameraSessionId cameraSessionId, final Handler handler) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        if (this.isSceneRecognitionRunning()) {
            if (CamLog.DEBUG) {
                CamLog.d("Scene recognition is already running.");
            }
            return;
        }
        final CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        if (openCloseStatusInfo != null && openCloseStatusInfo.isCloseCameraTaskRequested()) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is closing or closed.");
            }
            return;
        }
        if (PlatformCapability.isSceneRecognitionSupported(parameters.getCameraId())) {
            final SceneRecognitionResultChecker mSceneConditionRecognitionResultChecker = new SceneRecognitionResultChecker(handler, this.mOnSceneModeChangedCallback, parameters.getCameraId());
            synchronized (this.mCaptureResultCheckerLock) {
                this.mSceneConditionRecognitionResultChecker = mSceneConditionRecognitionResultChecker;
                this.mCaptureResultCheckerSet.add(this.mSceneConditionRecognitionResultChecker);
                monitorexit(this.mCaptureResultCheckerLock);
                parameters.requestApply();
                this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_MODE, 1);
                this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE, 1);
                this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE, 2);
                this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE, 1);
                this.setRepeatingRequestInternal(cameraSessionId, false);
                ((CameraStatusPublisher<SceneRecognition>)new EachCameraStatusPublisher(this.getApplicationContext(), parameters.getCameraId())).put(new SceneRecognition(SceneRecognition.Value.ON)).publish();
            }
        }
    }
    
    void stopFaceDetection(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        if (!this.isFaceDetectionRunning()) {
            if (CamLog.DEBUG) {
                CamLog.d("Face detection is not running.");
            }
            return;
        }
        synchronized (this.mCaptureResultCheckerLock) {
            this.mCaptureResultCheckerSet.remove(this.mFaceDetectionResultChecker);
            monitorexit(this.mCaptureResultCheckerLock);
            this.mFaceDetectionResultChecker = null;
            parameters.requestApply();
            this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.STATISTICS_FACE_DETECT_MODE, 0);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE, 0);
            ((CameraStatusPublisher<FaceIdentification>)new EachCameraStatusPublisher(this.getApplicationContext(), parameters.getCameraId())).put(new FaceIdentification(FaceIdentification.Value.OFF)).put((FaceIdentification)new FaceDetection(FaceDetection.Value.OFF)).publish();
        }
    }
    
    void stopFusionMonitoring() {
        if (this.isFusionMonitoringRunning()) {
            synchronized (this.mCaptureResultCheckerLock) {
                this.mCaptureResultCheckerSet.remove(this.mFusionResultChecker);
                CamLog.d("remove fusion status checker.");
                monitorexit(this.mCaptureResultCheckerLock);
                this.mFusionResultChecker = null;
            }
        }
    }
    
    void stopObjectTracking(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        if (!this.isObjectTrackingRunning()) {
            if (CamLog.DEBUG) {
                CamLog.d("Object tracking is not running.");
            }
            return;
        }
        Object o = this.mCaptureResultCheckerLock;
        synchronized (o) {
            this.mCaptureResultCheckerSet.remove(this.mObjectTrackingResultChecker);
            monitorexit(o);
            this.mObjectTrackingResultChecker = null;
            o = this.mCaptureRequestHolder.copy();
            ((CaptureRequestHolder)o).set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER, Integer.valueOf(2));
            this.setOneTimeRequestInternal(cameraSessionId, (CaptureRequestHolder)o, false);
            LocalResearchUtil.getInstance().setObjectTrackingTarget(false);
            ((CameraStatusPublisher<ObjectTracking>)new EachCameraStatusPublisher(this.getApplicationContext(), parameters.getCameraId())).put(new ObjectTracking(ObjectTracking.Value.OFF)).publish();
        }
    }
    
    void stopPreview(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new StopPreviewTask(cameraSessionId));
    }
    
    void stopPreviewTaskSynchronized(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThreadSync(new StopPreviewTask(cameraSessionId));
        this.triggerRestartPreview(cameraSessionId, false);
    }
    
    void stopSceneRecognition(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        if (!this.isSceneRecognitionRunning()) {
            if (CamLog.DEBUG) {
                CamLog.d("Scene recognition is not running.");
            }
            return;
        }
        cameraSessionId = (CameraDeviceHandler.CameraSessionId)this.mCaptureResultCheckerLock;
        synchronized (cameraSessionId) {
            this.mCaptureResultCheckerSet.remove(this.mSceneConditionRecognitionResultChecker);
            this.mSceneConditionRecognitionResultChecker = null;
            monitorexit(cameraSessionId);
            cameraSessionId = (CameraDeviceHandler.CameraSessionId)CameraParameterConverter.StillHdr.getApi2Value(parameters.getStillHdr());
            this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.CONTROL_MODE, 1);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE, 0);
            this.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Object>)SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE, cameraSessionId);
            this.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE, 0);
            ((CameraStatusPublisher<SceneRecognition>)new EachCameraStatusPublisher(this.getApplicationContext(), parameters.getCameraId())).put(new SceneRecognition(SceneRecognition.Value.OFF)).publish();
        }
    }
    
    void triggerRestartPreview(final CameraDeviceHandler.CameraSessionId cameraSessionId, final boolean b) {
        CamLog.d("triggerRestartPreview()");
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            return;
        }
        if (b) {
            this.mPrevPreviewSessionRequest = null;
            parameters.forceRequestCreatePreviewSession();
        }
        if (this.mPreviewRequest == null) {
            this.mPreviewRequest = new PreviewSessionRequest(cameraSessionId);
        }
        if (this.mCameraDeviceHandler.isVideo()) {
            this.mPreviewRequest.needVideo(parameters.getVideoSize());
        }
        else {
            this.mPreviewRequest.needVideo(null);
        }
        final PreviewSessionRequest mPreviewRequest = this.mPreviewRequest;
        final VideoHdr videoHdr = parameters.getVideoHdr();
        final VideoHdr hdr_ON = VideoHdr.HDR_ON;
        final boolean b2 = false;
        mPreviewRequest.needVideoHdr(videoHdr == hdr_ON);
        final PreviewSessionRequest mPreviewRequest2 = this.mPreviewRequest;
        boolean b3 = b2;
        if (parameters.getShutterTrigger() == ShutterTrigger.GESTURE_SHUTTER) {
            b3 = true;
        }
        mPreviewRequest2.needCapturedFrame(b3);
    }
    
    interface CameraControllerCallback
    {
        void onCropRegionReady();
        
        void onDeviceError(final CameraDeviceHandler.CameraSessionId p0, final CameraDeviceHandler.ErrorCode p1);
        
        void onFaceDetected(final CameraParameters.FaceDetectionResult p0);
        
        void onFusionResultChanged(final CameraParameters.FusionResult p0);
        
        void onOpenCameraRequested(final CameraDeviceHandler.CameraSessionId p0);
        
        void onPreviewFrameUpdated(final ByteBuffer p0, final int p1, final Rect p2);
        
        void onReflected(final CameraDeviceHandler.CameraSessionId p0);
        
        void onSceneModeChanged(final CameraParameters.SceneRecognitionResult p0);
        
        void onSessionDisconnected(final CameraDeviceHandler.CameraSessionId p0);
    }
    
    private class CameraStateCallback extends CameraDevice$StateCallback
    {
        private static final int OPEN_CLOSE_WAIT_TIME_MILLIS = 1000;
        private final Object mCameraDeviceLock;
        private CountDownLatch mCloseCameraLatch;
        private CameraDevice mDevice;
        private boolean mIsRequiredToAbandonDevice;
        private CountDownLatch mOpenCameraLatch;
        private final CameraDeviceHandler.CameraSessionId mSessionId;
        final CameraController this$0;
        
        private CameraStateCallback(final CameraController this$0, final CameraDeviceHandler.CameraSessionId mSessionId) {
            this.this$0 = this$0;
            this.mOpenCameraLatch = new CountDownLatch(1);
            this.mCloseCameraLatch = new CountDownLatch(1);
            this.mCameraDeviceLock = new Object();
            this.mSessionId = mSessionId;
            this.mIsRequiredToAbandonDevice = false;
            this.mDevice = null;
        }
        
        private void cancelOpenCamera() {
            synchronized (this.mCameraDeviceLock) {
                if (this.mDevice != null) {
                    this.mDevice.close();
                    this.mDevice = null;
                }
                this.mIsRequiredToAbandonDevice = true;
            }
        }
        
        private void waitCameraClosed() {
            try {
                if (!this.mCloseCameraLatch.await(1000L, TimeUnit.MILLISECONDS)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Closing camera device is timed-out. sessionId:");
                    sb.append(this.mSessionId);
                    CamLog.w(sb.toString());
                }
            }
            catch (final InterruptedException ex) {
                CamLog.e("waitCameraClosed() : Failed to await", ex);
            }
        }
        
        private CameraDevice waitCameraOpened() {
            try {
                if (!this.mOpenCameraLatch.await(1000L, TimeUnit.MILLISECONDS)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Opening camera device is timed-out. sessionId:");
                    sb.append(this.mSessionId);
                    CamLog.w(sb.toString());
                }
            }
            catch (final InterruptedException ex) {
                CamLog.e("waitCameraOpened() : Failed to await", ex);
            }
            synchronized (this.mCameraDeviceLock) {
                final CameraDevice mDevice = this.mDevice;
                this.mDevice = null;
                this.mIsRequiredToAbandonDevice = true;
                return mDevice;
            }
        }
        
        public void onClosed(final CameraDevice cameraDevice) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked sessionId:");
                sb.append(this.mSessionId);
                CamLog.d(sb.toString());
            }
            this.mCloseCameraLatch.countDown();
        }
        
        public void onDisconnected(final CameraDevice cameraDevice) {
            final StringBuilder sb = new StringBuilder();
            sb.append("CameraStateCallback.onDisconnected() : SessionID = ");
            sb.append(this.mSessionId);
            CamLog.e(sb.toString());
            CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(this.mSessionId).setCameraEvicted();
            this.this$0.mCallback.onSessionDisconnected(this.mSessionId);
        }
        
        public void onError(final CameraDevice cameraDevice, final int n) {
            switch (n) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("onError is called. Error:");
                    sb.append(n);
                    sb.append(" (UNKNOWN). SessionID = ");
                    sb.append(this.mSessionId);
                    CamLog.e(sb.toString());
                    break;
                }
                case 5: {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("onError is called. Error:");
                    sb2.append(n);
                    sb2.append(" (CameraStateCallback.ERROR_CAMERA_SERVICE). SessionID = ");
                    sb2.append(this.mSessionId);
                    CamLog.e(sb2.toString());
                    break;
                }
                case 4: {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("onError is called. Error:");
                    sb3.append(n);
                    sb3.append(" (CameraStateCallback.ERROR_CAMERA_DEVICE). SessionID = ");
                    sb3.append(this.mSessionId);
                    CamLog.e(sb3.toString());
                    break;
                }
                case 3: {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("onError is called. Error:");
                    sb4.append(n);
                    sb4.append(" (CameraStateCallback.ERROR_CAMERA_DISABLED). SessionID = ");
                    sb4.append(this.mSessionId);
                    CamLog.e(sb4.toString());
                    break;
                }
                case 2: {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("onError is called. Error:");
                    sb5.append(n);
                    sb5.append(" (CameraStateCallback.ERROR_MAX_CAMERAS_IN_USE). SessionID = ");
                    sb5.append(this.mSessionId);
                    CamLog.e(sb5.toString());
                    break;
                }
                case 1: {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("onError is called. Error:");
                    sb6.append(n);
                    sb6.append(" (CameraStateCallback.ERROR_CAMERA_IN_USE). SessionID = ");
                    sb6.append(this.mSessionId);
                    CamLog.e(sb6.toString());
                    break;
                }
            }
            CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(this.mSessionId).setCameraError();
            this.this$0.mCallback.onDeviceError(this.mSessionId, CameraDeviceHandler.ErrorCode.ERROR_ON_CAMERA_ERROR);
        }
        
        public void onOpened(final CameraDevice mDevice) {
            synchronized (this.mCameraDeviceLock) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoked sessionId:");
                    sb.append(this.mSessionId);
                    sb.append(" isRequiredToAbandonDevice:");
                    sb.append(this.mIsRequiredToAbandonDevice);
                    CamLog.d(sb.toString());
                }
                if (this.mIsRequiredToAbandonDevice) {
                    mDevice.close();
                }
                else {
                    this.mDevice = mDevice;
                }
                monitorexit(this.mCameraDeviceLock);
                this.mOpenCameraLatch.countDown();
                final Object mCameraDeviceLock = this.mCameraDeviceLock;
                synchronized (this.mCameraDeviceLock) {
                    if (this.mIsRequiredToAbandonDevice && this.mDevice != null) {
                        this.mDevice.close();
                        this.mDevice = null;
                    }
                }
            }
        }
    }
    
    private final class CaptureSessionCallback extends CameraCaptureSession$CaptureCallback
    {
        private static final int IGNORE_CAPTURE_RESULT_THRESHOLD = 5;
        private boolean mIsPreviewStartNotificationRequired;
        private final CameraDeviceHandler.CameraSessionId mSessionId;
        final CameraController this$0;
        
        private CaptureSessionCallback(final CameraController this$0, final CameraDeviceHandler.CameraSessionId mSessionId) {
            this.this$0 = this$0;
            this.mSessionId = mSessionId;
            this.mIsPreviewStartNotificationRequired = false;
        }
        
        private CaptureSessionCallback(final CameraController this$0, final CameraDeviceHandler.CameraSessionId mSessionId, final boolean mIsPreviewStartNotificationRequired) {
            this.this$0 = this$0;
            this.mSessionId = mSessionId;
            this.mIsPreviewStartNotificationRequired = mIsPreviewStartNotificationRequired;
        }
        
        private boolean isCropRegionChanged(final TotalCaptureResult totalCaptureResult) {
            if (totalCaptureResult == null) {
                return false;
            }
            final Rect obj = (Rect)totalCaptureResult.get(CaptureResult.SCALER_CROP_REGION);
            final boolean b = this.this$0.mReceivedResultCount > 1 && (this.this$0.mLastCropRegion.left != obj.left || this.this$0.mLastCropRegion.top != obj.top || this.this$0.mLastCropRegion.right != obj.right || this.this$0.mLastCropRegion.bottom != obj.bottom);
            this.this$0.mLastCropRegion = obj;
            final StringBuilder sb = new StringBuilder();
            sb.append("isCropRegionChanged() newRect:");
            sb.append(obj);
            sb.append(" changed: ");
            sb.append(b);
            CamLog.d(sb.toString());
            return b;
        }
        
        public void onCaptureCompleted(final CameraCaptureSession cameraCaptureSession, final CaptureRequest captureRequest, final TotalCaptureResult totalCaptureResult) {
            if (this.mIsPreviewStartNotificationRequired) {
                this.mIsPreviewStartNotificationRequired = false;
                this.this$0.notifyOnPreviewStarted(this.mSessionId);
                PerfLog.START_PREVIEW.end();
            }
            if (this.this$0.getCameraDeviceStatus() != CameraDeviceHandler.CameraDeviceStatus.STATUS_READY) {
                if (CamLog.DEBUG) {
                    CamLog.d("CurrentDeviceState is not READY.");
                }
                return;
            }
            if (this.this$0.mReceivedResultCount < 5) {
                this.this$0.mReceivedResultCount++;
            }
            if (this.this$0.mIsNeedCheckCropRegion && (this.isCropRegionChanged(totalCaptureResult) || this.this$0.mReceivedResultCount == 5)) {
                this.this$0.mCallback.onCropRegionReady();
                this.this$0.mIsNeedCheckCropRegion = false;
            }
            if (totalCaptureResult != null) {
                this.this$0.mCaptureResultHolder.add((CaptureResult)totalCaptureResult);
                PositionConverter.getInstance().setCropRegion((Rect)totalCaptureResult.get(CaptureResult.SCALER_CROP_REGION));
            }
            if (CamLog.VERBOSE && this.this$0.mCaptureResultHolder != null) {
                this.this$0.mCaptureResultHolder.dumpLatest();
            }
            synchronized (this.this$0.mCaptureResultCheckerLock) {
                final Iterator iterator = this.this$0.mCaptureResultCheckerSet.iterator();
                while (iterator.hasNext()) {
                    ((CaptureResultCheckerBase)iterator.next()).check(this.this$0.mCaptureResultHolder);
                }
            }
        }
        
        public void onCaptureFailed(final CameraCaptureSession cameraCaptureSession, final CaptureRequest captureRequest, final CaptureFailure captureFailure) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked sessionId:");
                sb.append(this.mSessionId);
                sb.append(" captureSession:");
                sb.append(cameraCaptureSession.hashCode());
                sb.append(" reason:");
                sb.append(captureFailure.getReason());
                CamLog.d(sb.toString());
            }
        }
    }
    
    @WorkerThread
    private class CloseCameraTask extends CameraDeviceAccessTask
    {
        final CameraController this$0;
        
        private CloseCameraTask(final CameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
        }
        
        public void doCameraDeviceAccess() {
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().setPerformed(OpenClosePerformStatus.CAMERA_CLOSED);
            this.this$0.mCameraDeviceHandler.releaseRecorderOnCameraClosed();
            final StringBuilder sb = new StringBuilder();
            sb.append("CloseCameraTask() : Current device status = ");
            sb.append(this.this$0.getCameraDeviceStatus());
            CamLog.d(sb.toString());
            switch (CameraController$2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$0.getCameraDeviceStatus().ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4: {
                    while (true) {
                        try {
                            try {
                                if (this.this$0.mCaptureSession != null) {
                                    if (CamLog.DEBUG) {
                                        CamLog.d("stopRepeating()");
                                    }
                                    this.this$0.mCaptureSession.stopRepeating();
                                    if (!this.this$0.mCameraDeviceHandler.isSnapshotRunning()) {
                                        this.this$0.mCaptureSession.abortCaptures();
                                    }
                                    this.this$0.mCaptureSession.close();
                                }
                                this.this$0.mCaptureSession = null;
                                this.this$0.mCaptureRequestDumper = null;
                                this.this$0.mOutputConfiguration = null;
                                this.this$0.mPreviewSurface = null;
                                this.this$0.mCaptureImageRetriever = null;
                            }
                            finally {}
                        }
                        catch (final IllegalStateException cause) {
                            CamLog.e("CloseCameraTask() : Failed by IllegalStateException", cause);
                            if (this.this$0.mCameraDeviceHandler.isIgnoreCameraError() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) {
                                throw new IllegalStateException(cause);
                            }
                            continue;
                        }
                        catch (final CameraAccessException ex) {
                            CamLog.e("CloseCameraTask() : Failed by CameraAccessException", (Throwable)ex);
                            continue;
                        }
                        break;
                    }
                    this.this$0.releaseStreamingImageReader();
                    this.this$0.releaseVideoImageReader();
                    if (this.this$0.mCameraDevice != null) {
                        this.this$0.mCameraDevice.close();
                        this.this$0.mCameraDevice = null;
                    }
                    this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_RELEASED);
                    break;
                    this.this$0.mCaptureSession = null;
                    this.this$0.mCaptureRequestDumper = null;
                    this.this$0.mOutputConfiguration = null;
                    this.this$0.mPreviewSurface = null;
                    this.this$0.mCaptureImageRetriever = null;
                    break;
                }
            }
            final Context access$2100 = this.this$0.getApplicationContext();
            if (access$2100 != null) {
                new EachCameraStatusPublisher(access$2100, this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId()).getCameraId()).putDefaultAll().publish();
                new GlobalCameraStatusPublisher(access$2100).putDefaultAll().publish();
            }
            if (this.this$0.mCameraDeviceHandler.getPreProcessState() != PreProcessState.NOT_STARTED && this.this$0.mCameraDeviceHandler.getPreProcessState() != PreProcessState.PRE_CAPTURE_DONE) {
                this.this$0.mCameraDeviceHandler.changePreProcessStateTo(PreProcessState.PRE_CAPTURE_RELEASED);
            }
            else {
                this.this$0.mCameraDeviceHandler.changePreProcessStateTo(PreProcessState.NOT_STARTED);
            }
            this.this$0.mStateCallback.waitCameraClosed();
            this.this$0.mStateCallback = null;
        }
        
        public void postCameraDeviceAccess() {
            this.mLatch.countDown();
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isOpenCameraTaskPerformed();
        }
    }
    
    @WorkerThread
    private class CloseSessionTask extends CameraDeviceAccessTask
    {
        final CameraController this$0;
        
        private CloseSessionTask(final CameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
        }
        
        public void doCameraDeviceAccess() {
            this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
            while (true) {
                try {
                    try {
                        if (this.this$0.mCaptureSession != null) {
                            if (CamLog.DEBUG) {
                                CamLog.d("stopRepeating()");
                            }
                            this.this$0.mCaptureSession.stopRepeating();
                            if (!this.this$0.mCameraDeviceHandler.isSnapshotRunning()) {
                                this.this$0.mCaptureSession.abortCaptures();
                            }
                            this.this$0.mCaptureSession.close();
                        }
                        this.this$0.mCaptureSession = null;
                        this.this$0.mCaptureRequestDumper = null;
                    }
                    finally {}
                }
                catch (final CameraAccessException ex) {
                    CamLog.e("CloseSessionTask: Close session failed: ", (Throwable)ex);
                    continue;
                }
                break;
            }
            return;
            this.this$0.mCaptureSession = null;
            this.this$0.mCaptureRequestDumper = null;
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController$2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$0.getCameraDeviceStatus().ordinal()]) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed due to wrong status in CloseSessionTask. status: ");
                    sb.append(this.this$0.getCameraDeviceStatus());
                    throw new IllegalStateException(sb.toString());
                }
                case 5: {
                    if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                        CamLog.d("CloseSessionTask : CloseCameraTask is already requested.");
                    }
                    if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) {
                        CamLog.d("CloseSessionTask : Could not execute due to error caused.");
                    }
                    return false;
                }
                case 3: {
                    return true;
                }
                case 1:
                case 2:
                case 4: {
                    return false;
                }
            }
        }
    }
    
    private class ConfigStateCallback extends CameraCaptureSession$StateCallback
    {
        private final CameraDeviceHandler.CameraSessionId mSessionId;
        final CameraController this$0;
        
        private ConfigStateCallback(final CameraController this$0, final CameraDeviceHandler.CameraSessionId mSessionId) {
            this.this$0 = this$0;
            this.mSessionId = mSessionId;
        }
        
        public void onClosed(final CameraCaptureSession cameraCaptureSession) {
            CamLog.d("onClosed()");
        }
        
        public void onConfigureFailed(final CameraCaptureSession cameraCaptureSession) {
            this.this$0.mCameraDeviceHandler.postCameraDeviceThread(new OnCaptureSessionConfigured(this.mSessionId, (CameraCaptureSession)null, this));
        }
        
        public void onConfigured(final CameraCaptureSession cameraCaptureSession) {
            PerfLog.ON_CONFIGURED.begin();
            this.this$0.mCameraDeviceHandler.postCameraDeviceThread(new OnCaptureSessionConfigured(this.mSessionId, cameraCaptureSession, this));
            PerfLog.ON_CONFIGURED.end();
        }
    }
    
    @WorkerThread
    private class CreateCaptureSessionTask extends CameraDeviceAccessTask
    {
        private PreviewSessionRequest mRequest;
        final CameraController this$0;
        
        private CreateCaptureSessionTask(final CameraController this$0, final CameraSessionId cameraSessionId, final PreviewSessionRequest mRequest) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mRequest = mRequest;
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).setPerformancefLog(PerfLog.CREATE_CAPTURE_SESSION_TASK);
        }
        
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("CreateCaptureSessionTask invoked isVideo:");
                sb.append(this.this$0.mCameraDeviceHandler.isVideo());
                CamLog.d(sb.toString());
            }
            final CameraParameters parameters = this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
            this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
            if (CamLog.DEBUG) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("CreateCaptureSessionTask executed request:");
                sb2.append(this.mRequest);
                CamLog.d(sb2.toString());
            }
            if (this.this$0.mCameraDeviceHandler.isVideo()) {
                final int width = parameters.getPreviewSize().width();
                final int height = parameters.getPreviewSize().height();
                if (this.mRequest.isVideoHdr()) {
                    this.this$0.releaseVideoImageReader();
                }
                else {
                    this.this$0.prepareVideoImageReader(width, height);
                }
                this.this$0.releaseStreamingImageReader();
            }
            else {
                this.this$0.releaseVideoImageReader();
                if (this.mRequest.isNeedCapturedFrame()) {
                    this.this$0.prepareStreamingImageReader(parameters.getPreviewSize().width(), parameters.getPreviewSize().height());
                }
                else {
                    this.this$0.releaseStreamingImageReader();
                }
            }
            this.this$0.mCaptureSession = null;
            this.this$0.mCaptureRequestDumper = null;
            this.this$0.mConfigStateCallback = new ConfigStateCallback(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
            this.this$0.mOutputConfiguration = new OutputConfiguration(PlatformDependencyResolver.getSurfaceSize(parameters.getPreviewSize(), this.mRequest.isVideoHdr()), (Class)SurfaceHolder.class);
            final ArrayList list = new ArrayList();
            list.add(this.this$0.mOutputConfiguration);
            if (this.this$0.mStreamingImageReader != null) {
                list.add(new OutputConfiguration(this.this$0.mStreamingImageReader.getSurface()));
            }
            if (this.this$0.mVideoThumbnailImageReader != null) {
                list.add(new OutputConfiguration(this.this$0.mVideoThumbnailImageReader.getSurface()));
            }
            try {
                this.this$0.mCameraDevice.createCaptureSession(new SessionConfiguration(this.mRequest.getOperationMode(), (List)list, (Executor)this.this$0.mCaptureSessionCallbackExecutor, (CameraCaptureSession$StateCallback)this.this$0.mConfigStateCallback));
            }
            catch (final CameraAccessException ex) {
                CamLog.e("CreateCaptureSessionTask() : Failed by CameraAccessException", (Throwable)ex);
                if (this.this$0.mCameraDeviceHandler.isIgnoreCameraError() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) {
                    throw new RuntimeException("Failed in createCaptureSession by CameraAccessException. Reason:", ex.getCause());
                }
                CamLog.w("Failed in createCaptureSession", (Throwable)ex);
            }
        }
        
        @Override
        protected void postCameraDeviceAccess() {
            switch (CameraController$2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$0.getCameraDeviceStatus().ordinal()]) {
                default: {
                    return;
                }
                case 3:
                case 4: {
                    return;
                }
                case 1:
                case 2:
                case 5: {
                    this.this$0.mOneShotCaptureTaskPendingList.clear();
                }
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController$2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$0.getCameraDeviceStatus().ordinal()]) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed due to wrong status in CreateCaptureSessionTask. status: ");
                    sb.append(this.this$0.getCameraDeviceStatus());
                    throw new IllegalStateException(sb.toString());
                }
                case 5: {
                    if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) {
                        return false;
                    }
                    if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                        return false;
                    }
                    this.this$0.triggerRestartPreview(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), true);
                    return false;
                }
                case 3:
                case 4: {
                    return true;
                }
                case 1:
                case 2: {
                    return false;
                }
            }
        }
    }
    
    @WorkerThread
    private class OnCaptureSessionConfigured extends CameraDeviceAccessTask
    {
        private final CameraCaptureSession mResult;
        private final ConfigStateCallback mSender;
        final CameraController this$0;
        
        private OnCaptureSessionConfigured(final CameraController this$0, final CameraSessionId cameraSessionId, final CameraCaptureSession mResult, final ConfigStateCallback mSender) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mResult = mResult;
            this.mSender = mSender;
        }
        
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked sessionId:");
                sb.append(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
                CamLog.d(sb.toString());
            }
            if (this.this$0.mConfigStateCallback != this.mSender) {
                if (CamLog.DEBUG) {
                    CamLog.d("This callback is invalid.");
                }
                return;
            }
            if (this.mResult == null) {
                this.this$0.onCameraOtherErrorDetected(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo(), ErrorCode.ERROR_ON_CONFIGURE_FAILED);
                this.this$0.mOneShotCaptureTaskPendingList.clear();
                return;
            }
            this.this$0.mIsNeedCheckCropRegion = true;
            this.this$0.mReceivedResultCount = 0;
            this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_READY);
            this.this$0.mCaptureSession = this.mResult;
            this.this$0.mCaptureRequestDumper = new CaptureRequestDumper(CameraController.CAPTURE_REQUEST_DUMP_TYPE, this.this$0.mCaptureSession);
            if (this.this$0.mPreviewSurface != null) {
                this.this$0.setRepeatingRequestInternal(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), false);
            }
            final Iterator iterator = this.this$0.mOneShotCaptureTaskPendingList.iterator();
            while (iterator.hasNext()) {
                this.this$0.mCameraDeviceHandler.postCameraDeviceThread((CameraDeviceAccessTask)iterator.next());
                if (CamLog.DEBUG) {
                    CamLog.d("Pending OneShotCaptureTask is posted.");
                }
            }
            this.this$0.mOneShotCaptureTaskPendingList.clear();
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                return false;
            }
            if (this.this$0.getCameraDeviceStatus() != CameraDeviceStatus.STATUS_OPENED) {
                if (CamLog.DEBUG) {
                    CamLog.d("CurrentDeviceState is not OPENED.");
                }
                return false;
            }
            return true;
        }
    }
    
    private class OnFaceDetectionCallback implements FaceDetectionCallback
    {
        final CameraController this$0;
        
        private OnFaceDetectionCallback(final CameraController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onFaceDetection(final FaceDetectionResult faceDetectionResult) {
            if (!this.this$0.isFaceDetectionRunning()) {
                if (CamLog.DEBUG) {
                    CamLog.d("Face detection is stopped.");
                }
                return;
            }
            this.this$0.mCallback.onFaceDetected(faceDetectionResult);
        }
    }
    
    private class OnFusionResultChangedCallback implements FusionResultCallback
    {
        final CameraController this$0;
        
        private OnFusionResultChangedCallback(final CameraController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onFusionResultChanged(final FusionResult fusionResult) {
            this.this$0.mCallback.onFusionResultChanged(fusionResult);
        }
    }
    
    private class OnSceneModeChangedCallback implements SceneRecognitionCallback
    {
        final CameraController this$0;
        
        private OnSceneModeChangedCallback(final CameraController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onSceneModeChanged(final SceneRecognitionResult sceneRecognitionResult) {
            if (!this.this$0.isSceneRecognitionRunning()) {
                if (CamLog.DEBUG) {
                    CamLog.d("Scene recognition is stopped.");
                }
                return;
            }
            if (sceneRecognitionResult == null) {
                LocalResearchUtil.getInstance().clearRecognizedScene();
            }
            this.this$0.mCallback.onSceneModeChanged(sceneRecognitionResult);
        }
    }
    
    @WorkerThread
    private class OneShotCaptureTask extends CameraDeviceAccessTask
    {
        private final CaptureSessionCallback mCaptureSessionCallback;
        private final Surface mCaptureSurface;
        private final CaptureRequestHolder mRequestHolder;
        final CameraController this$0;
        
        private OneShotCaptureTask(final CameraController this$0, final CameraSessionId cameraSessionId, final CaptureRequestHolder mRequestHolder, final ImageReader imageReader) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mRequestHolder = mRequestHolder;
            this.mCaptureSurface = imageReader.getSurface();
            this.mCaptureSessionCallback = new CaptureSessionCallback(cameraSessionId);
        }
        
        public void doCameraDeviceAccess() {
            if (this.this$0.mOutputConfiguration != null) {
                this.this$0.mOutputConfiguration.addSurface(this.this$0.mPreviewSurface);
                try {
                    this.this$0.mCaptureSession.finalizeOutputConfigurations((List)Arrays.asList(this.this$0.mOutputConfiguration));
                    this.this$0.mOutputConfiguration = null;
                }
                catch (final CameraAccessException ex) {
                    CamLog.e("OneShotCaptureTask: finalizeOutputConfigurations failed.");
                    this.this$0.onCameraOtherErrorDetected(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo(), ErrorCode.ERROR_ON_CAMERA_ERROR);
                    return;
                }
            }
            final CaptureRequest captureRequest = this.mRequestHolder.createCaptureRequest(this.this$0.mCameraDevice, 1, new Surface[] { this.mCaptureSurface, this.this$0.mPreviewSurface });
            if (captureRequest == null) {
                CamLog.i("OneShotCaptureTask : CaptureRequest cannot be created.");
                return;
            }
            try {
                if (CamLog.DEBUG) {
                    CamLog.d("capture()");
                }
                this.this$0.mCaptureSession.capture(captureRequest, (CameraCaptureSession$CaptureCallback)this.mCaptureSessionCallback, this.this$0.mCameraDeviceHandler.getDeviceThreadHandler());
                if (CamLog.DEBUG) {
                    this.this$0.mCaptureRequestDumper.update(captureRequest);
                    this.this$0.mCaptureRequestDumper.dump();
                }
            }
            catch (final IllegalArgumentException ex2) {
                CamLog.w("Failed in OneShotCaptureTask.", ex2);
            }
            catch (final CameraAccessException ex3) {
                if (this.this$0.mCameraDeviceHandler.isIgnoreCameraError() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed in OneShotCaptureTask by CameraAccessException. Reason:");
                    sb.append(ex3.getReason());
                    throw new RuntimeException(sb.toString());
                }
                CamLog.w("Failed in OneShotCaptureTask", (Throwable)ex3);
            }
        }
        
        @Override
        protected void postCameraDeviceAccess() {
            if (CameraController$2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$0.getCameraDeviceStatus().ordinal()] == 4) {
                CamLog.d("Pending OneShotCaptureTask is added.");
                this.this$0.mOneShotCaptureTaskPendingList.add(this);
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController$2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$0.getCameraDeviceStatus().ordinal()]) {
                case 5: {
                    if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) {
                        return false;
                    }
                    if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                        return false;
                    }
                    break;
                }
                case 3: {
                    return true;
                }
                case 1:
                case 2:
                case 4: {
                    return false;
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed due to wrong status in OneShotCaptureTask. status: ");
            sb.append(this.this$0.getCameraDeviceStatus());
            throw new IllegalStateException(sb.toString());
        }
    }
    
    @WorkerThread
    private class OpenCameraTask extends CameraDeviceAccessTask
    {
        private final FastCapture mFastCapture;
        final CameraController this$0;
        
        private OpenCameraTask(final CameraController this$0, final CameraSessionId cameraSessionId, final FastCapture mFastCapture) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mFastCapture = mFastCapture;
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).setPerformancefLog(PerfLog.OPEN_CAMERA_TASK);
        }
        
        private boolean isPreProcessing() {
            return this.this$0.mCameraDeviceHandler.getPreProcessState() != PreProcessState.NOT_STARTED && this.this$0.mCameraDeviceHandler.getPreProcessState() != PreProcessState.PRE_CAPTURE_RELEASED;
        }
        
        public void doCameraDeviceAccess() {
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().setPerformed(OpenClosePerformStatus.CAMERA_OPENED);
            final CameraParameters parameters = this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
            if (this.mFastCapture != FastCapture.LAUNCH_AND_CAPTURE) {
                this.this$0.mCallback.onOpenCameraRequested(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("OpenCameraTask invoked cameraId:");
                sb.append(parameters.getCameraId());
                CamLog.d(sb.toString());
            }
            this.this$0.mStateCallback = new CameraStateCallback(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
            try {
                this.this$0.mCameraManager.openCamera(parameters.getCameraId().getCameraDeviceId(), (CameraDevice$StateCallback)this.this$0.mStateCallback, this.this$0.mCameraDeviceStatusThreadHandler);
                if (!this.this$0.mCameraDeviceHandler.awaitLoadSettingsThread()) {
                    CamLog.e("OpenCameraTask() : Failed to load setting.");
                    this.this$0.mStateCallback.cancelOpenCamera();
                    this.this$0.onCameraOtherErrorDetected(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo(), ErrorCode.ERROR_ON_CAMERA_ERROR);
                    return;
                }
                this.this$0.mCaptureRequestHolder.set((android.hardware.camera2.CaptureRequest$Key<Byte>)CaptureRequest.JPEG_QUALITY, (byte)MediaSavingConstants.JpegQuality.getPlatformQualityFromCameraProfile(2));
                this.this$0.mCameraDevice = this.this$0.mStateCallback.waitCameraOpened();
                if (this.this$0.mCameraDevice == null) {
                    CamLog.e("OpenCameraTask() : Failed to open camera.");
                    this.this$0.onCameraOtherErrorDetected(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo(), ErrorCode.ERROR_ON_CAMERA_ERROR);
                    return;
                }
                this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
                final boolean b = this.mFastCapture != FastCapture.LAUNCH_AND_CAPTURE || this.this$0.mCameraDeviceHandler.isNeedCreatePreviewSession();
                if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                    CamLog.d("OpenCameraTask() : CloseCameraTask() is already requested.");
                    return;
                }
                if (!this.isPreProcessing()) {
                    this.this$0.mCameraDeviceHandler.prepareCaptureImageReader();
                }
                this.this$0.mOneShotCaptureTaskPendingList.clear();
                if (b) {
                    this.this$0.createPreviewSession(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
                }
                if (this.mFastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
                    this.this$0.mCallback.onOpenCameraRequested(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
                }
                final Context access$2100 = this.this$0.getApplicationContext();
                if (access$2100 != null) {
                    ((CameraStatusPublisher<DeviceStatus>)new EachCameraStatusPublisher(access$2100, parameters.getCameraId())).put(new DeviceStatus(DeviceStatus.Value.POWER_ON)).publish();
                    ((CameraStatusPublisher<BuiltInCameraIds>)new GlobalCameraStatusPublisher(access$2100)).put(new BuiltInCameraIds(new CameraInfo.CameraId[] { parameters.getCameraId() })).publish();
                }
            }
            catch (final SecurityException | CameraAccessException | IllegalArgumentException ex) {
                CamLog.e("OpenCameraTask() : Failed by CameraAccessException", (Throwable)ex);
                this.this$0.onCameraOtherErrorDetected(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo(), ErrorCode.ERROR_ON_CAMERA_ERROR);
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                if (CamLog.DEBUG) {
                    CamLog.d("OpenCameraTask : CloseCameraTask is already requested.");
                }
                return false;
            }
            if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) {
                if (CamLog.DEBUG) {
                    CamLog.d("OpenCameraTask : should not open because of error caused.");
                }
                return false;
            }
            return true;
        }
    }
    
    private static final class PreviewFrameReceiver implements ImageReader$OnImageAvailableListener
    {
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
        
        private void registerCallback(final ImageRetriever.CaptureImageRequest mCaptureImageRequest) {
            monitorenter(this);
            Label_0046: {
                if (mCaptureImageRequest != null) {
                    break Label_0046;
                }
                try {
                    if (this.mCallbackTask != null && this.mCaptureImageRequest != null) {
                        this.mCaptureImageRequest.handler.removeCallbacks(this.mCallbackTask);
                        this.mCallbackTask = null;
                    }
                    break Label_0046;
                }
                finally {
                    monitorexit(this);
                    this.mCaptureImageRequest = mCaptureImageRequest;
                    monitorexit(this);
                }
            }
        }
        
        public void onImageAvailable(final ImageReader imageReader) {
            final Image acquireLatestImage = imageReader.acquireLatestImage();
            if (acquireLatestImage == null) {
                CamLog.w("onImageAvailable() image is null");
                return;
            }
            synchronized (this) {
                if (this.mCaptureImageRequest == null) {
                    acquireLatestImage.close();
                    return;
                }
                final Rect rect = new Rect(0, 0, acquireLatestImage.getWidth(), acquireLatestImage.getHeight());
                if (imageReader.getImageFormat() != 35) {
                    acquireLatestImage.close();
                    return;
                }
                final Image$Plane[] planes = acquireLatestImage.getPlanes();
                if (planes == null || planes.length != 3) {
                    CamLog.w("YUV_420_888 image is invalid. Planes are invalid.");
                    acquireLatestImage.close();
                    return;
                }
                final int width = acquireLatestImage.getWidth();
                final int height = acquireLatestImage.getHeight();
                if (width % 2 == 0 && height % 2 == 0) {
                    if (this.mOutput == null || width != 0 || height != 0) {
                        this.mOutput = new byte[width * height * 3 / 2];
                    }
                    final Image$Plane image$Plane = planes[0];
                    final Image$Plane image$Plane2 = planes[1];
                    final Image$Plane image$Plane3 = planes[2];
                    ImageConverter.convertFromYuv420_888ToNv21(this.mOutput, width, height, image$Plane.getBuffer(), image$Plane.getRowStride(), image$Plane.getPixelStride(), image$Plane2.getBuffer(), image$Plane2.getRowStride(), image$Plane2.getPixelStride(), image$Plane3.getBuffer(), image$Plane3.getRowStride(), image$Plane3.getPixelStride());
                    final ByteBuffer wrap = ByteBuffer.wrap(this.mOutput);
                    wrap.rewind();
                    acquireLatestImage.close();
                    final ImageRetriever.CaptureImageRequest mCaptureImageRequest = this.mCaptureImageRequest;
                    if (this.mCaptureImageRequest.isOneShot) {
                        this.mCaptureImageRequest = null;
                    }
                    this.mCallbackTask = new Runnable(this, mCaptureImageRequest, wrap, 17, rect) {
                        final PreviewFrameReceiver this$0;
                        final ByteBuffer val$byteBuffer;
                        final int val$imgFormat;
                        final ImageRetriever.CaptureImageRequest val$request;
                        final Rect val$size;
                        
                        @Override
                        public void run() {
                            if (this.val$request != null) {
                                this.val$request.callback.onRetrieved(this.val$byteBuffer, this.val$imgFormat, this.val$size);
                            }
                        }
                    };
                    mCaptureImageRequest.handler.post(this.mCallbackTask);
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("YUV_420_888 image is invalid. Width(");
                sb.append(width);
                sb.append(") or/are Height(");
                sb.append(height);
                sb.append(") is/are invalid.");
                CamLog.w(sb.toString());
                acquireLatestImage.close();
            }
        }
    }
    
    private static class PreviewSessionRequest
    {
        private static final int OPERATION_MODE_SOMC_CAMERA_BT601 = 32768;
        private static final int OPERATION_MODE_SOMC_CAMERA_BT709 = 32771;
        private static final int OPERATION_MODE_SOMC_CAMERA_VIDEO_HDR = 32770;
        private boolean mIsNeedCapturedFrame;
        private boolean mIsVideoHdr;
        private final String mSessionIdTag;
        private VideoSize mVideoSize;
        
        private PreviewSessionRequest(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
            this.mSessionIdTag = cameraSessionId.toString();
            this.mVideoSize = null;
            this.clear();
        }
        
        private void clear() {
            this.mIsNeedCapturedFrame = false;
            this.mIsVideoHdr = false;
            this.mVideoSize = null;
        }
        
        private int getOperationMode() {
            if (this.mIsVideoHdr) {
                return 32770;
            }
            if (this.mVideoSize != null && this.mVideoSize != VideoSize.VGA) {
                return 32771;
            }
            return 32768;
        }
        
        private boolean isNeedCapturedFrame() {
            return this.mIsNeedCapturedFrame;
        }
        
        private boolean isVideoHdr() {
            return this.mIsVideoHdr;
        }
        
        private void needCapturedFrame(final boolean mIsNeedCapturedFrame) {
            this.mIsNeedCapturedFrame = mIsNeedCapturedFrame;
        }
        
        private void needVideo(final VideoSize mVideoSize) {
            this.mVideoSize = mVideoSize;
        }
        
        private void needVideoHdr(final boolean mIsVideoHdr) {
            this.mIsVideoHdr = mIsVideoHdr;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof PreviewSessionRequest && this.toString().equals(o.toString());
        }
        
        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getClass().getSimpleName());
            sb.append("{sessionId=");
            sb.append(this.mSessionIdTag);
            sb.append(", isNeedCapturedFrame=");
            sb.append(this.mIsNeedCapturedFrame);
            sb.append(", isVideoHdr=");
            sb.append(this.mIsVideoHdr);
            sb.append(", OperationMode=");
            sb.append(this.getOperationMode());
            sb.append("}");
            return sb.toString();
        }
    }
    
    private class RequestOneImageRetrieverCallback implements OnImageRetrieverCallback
    {
        final CameraController this$0;
        
        private RequestOneImageRetrieverCallback(final CameraController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onRetrieved(final ByteBuffer byteBuffer, final int n, final Rect rect) {
            this.this$0.mCallback.onPreviewFrameUpdated(byteBuffer, n, rect);
        }
    }
    
    private class RequestSnapshotReadyAfterAfParametersReflected implements AfParametersCallback
    {
        private final CameraDeviceHandler.CameraSessionId mSessionId;
        final CameraController this$0;
        
        private RequestSnapshotReadyAfterAfParametersReflected(final CameraController this$0, final CameraDeviceHandler.CameraSessionId mSessionId) {
            this.this$0 = this$0;
            this.mSessionId = mSessionId;
        }
        
        @Override
        public void onReflected(final AfParametersReflectedChecker afParametersReflectedChecker) {
            synchronized (this.this$0.mCaptureResultCheckerLock) {
                this.this$0.mCaptureResultCheckerSet.remove(afParametersReflectedChecker);
                monitorexit(this.this$0.mCaptureResultCheckerLock);
                this.this$0.mCallback.onReflected(this.mSessionId);
            }
        }
    }
    
    @WorkerThread
    private class SetOneTimeRequestTask extends CameraDeviceAccessTask
    {
        private final CaptureSessionCallback mCaptureSessionCallback;
        private final CaptureRequestHolder mRequestHolder;
        private final Object mTag;
        final CameraController this$0;
        
        private SetOneTimeRequestTask(final CameraController this$0, final CameraSessionId cameraSessionId, final CaptureRequestHolder mRequestHolder, final Object mTag) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mRequestHolder = mRequestHolder;
            this.mCaptureSessionCallback = new CaptureSessionCallback(cameraSessionId);
            this.mTag = mTag;
        }
        
        public void doCameraDeviceAccess() {
            if (this.this$0.mPreviewSurface == null) {
                CamLog.d("SetOneTimeRequestTask : mPreviewSurface is null.");
                return;
            }
            if (!this.this$0.mPreviewSurface.isValid()) {
                this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
                CamLog.d("SetOneTimeRequestTask : mPreviewSurface is invalid.(before creating capture request)");
                return;
            }
            if (this.this$0.mOutputConfiguration != null && this.this$0.mOutputConfiguration.getSurface() == null) {
                this.this$0.mOutputConfiguration.addSurface(this.this$0.mPreviewSurface);
                try {
                    this.this$0.mCaptureSession.finalizeOutputConfigurations((List)Arrays.asList(this.this$0.mOutputConfiguration));
                    this.this$0.mOutputConfiguration = null;
                }
                catch (final CameraAccessException ex) {
                    CamLog.e("SetOneTimeRequestTask: finalizeOutputConfigurations failed.");
                    this.this$0.onCameraOtherErrorDetected(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo(), ErrorCode.ERROR_ON_CAMERA_ERROR);
                    return;
                }
            }
            final CaptureRequest captureRequest = this.mRequestHolder.createCaptureRequest(this.this$0.mCameraDevice, 1, this.mTag, this.this$0.mPreviewSurface);
            if (captureRequest == null) {
                CamLog.i("SetOneTimeRequestTask : CaptureRequest cannot be created.");
                return;
            }
            if (!this.this$0.mPreviewSurface.isValid()) {
                this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
                CamLog.d("SetOneTimeRequestTask : mPreviewSurface is invalid.(before performing capture)");
                return;
            }
            try {
                if (CamLog.DEBUG) {
                    CamLog.d("capture()");
                }
                this.this$0.mCaptureSession.capture(captureRequest, (CameraCaptureSession$CaptureCallback)this.mCaptureSessionCallback, this.this$0.mCameraDeviceHandler.getDeviceThreadHandler());
                if (CamLog.DEBUG) {
                    this.this$0.mCaptureRequestDumper.update(captureRequest);
                    this.this$0.mCaptureRequestDumper.dump();
                }
            }
            catch (final IllegalArgumentException ex2) {
                CamLog.w("Failed in SetOneTimeRequestTask.", ex2);
            }
            catch (final CameraAccessException ex3) {
                if (this.this$0.mCameraDeviceHandler.isIgnoreCameraError() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) {
                    if (this.this$0.mPreviewSurface.isValid()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed in SetOneTimeRequestTask by CameraAccessException. Reason:");
                        sb.append(ex3.getReason());
                        throw new RuntimeException(sb.toString());
                    }
                    this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
                    CamLog.d("SetOneTimeRequestTask : mPreviewSurface is invalid.(after performing capture)");
                }
                else {
                    CamLog.w("Failed in SetOneTimeRequestTask", (Throwable)ex3);
                }
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController$2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$0.getCameraDeviceStatus().ordinal()]) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed due to wrong status in SetOneTimeRequestTask. status: ");
                    sb.append(this.this$0.getCameraDeviceStatus());
                    throw new IllegalStateException(sb.toString());
                }
                case 3: {
                    return true;
                }
                case 1:
                case 2:
                case 4:
                case 5: {
                    return false;
                }
            }
        }
    }
    
    @WorkerThread
    private class SetRepeatingRequestTask extends CameraDeviceAccessTask
    {
        private static final int REPEATING_BURST_COUNT = 3;
        private final CaptureSessionCallback mCaptureSessionCallback;
        private final Surface mCaptureSurface;
        final CameraController this$0;
        
        private SetRepeatingRequestTask(final CameraController this$0, final CameraSessionId cameraSessionId, final ImageReader imageReader) {
            this.this$0 = this$0;
            super(cameraSessionId);
            Surface surface;
            if (imageReader != null) {
                surface = imageReader.getSurface();
            }
            else {
                surface = null;
            }
            this.mCaptureSurface = surface;
            this.mCaptureSessionCallback = new CaptureSessionCallback(cameraSessionId, true);
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).setPerformancefLog(PerfLog.SET_REPEATING_REQUEST_TASK);
        }
        
        public void doCameraDeviceAccess() {
            if (this.this$0.mPreviewSurface == null) {
                if (CamLog.DEBUG) {
                    CamLog.d("Preview surface is not created, so the request is refused.");
                }
                return;
            }
            if (!this.this$0.mPreviewSurface.isValid()) {
                this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
                if (CamLog.DEBUG) {
                    CamLog.d("Preview surface is not valid, so the request is refused.");
                }
                return;
            }
            if (this.this$0.mOutputConfiguration != null) {
                try {
                    this.this$0.mOutputConfiguration.addSurface(this.this$0.mPreviewSurface);
                    try {
                        this.this$0.mCaptureSession.finalizeOutputConfigurations((List)Arrays.asList(this.this$0.mOutputConfiguration));
                        this.this$0.mOutputConfiguration = null;
                    }
                    catch (final IllegalArgumentException ex) {
                        if (!this.this$0.mPreviewSurface.isValid()) {
                            CamLog.w("SetRepeatingRequestTask : Preview surface is not valid, so the request is refused.");
                            return;
                        }
                        throw ex;
                    }
                    catch (final CameraAccessException ex2) {
                        CamLog.e("setRepeatingRequest: finalizeOutputConfigurations failed.");
                        this.this$0.onCameraOtherErrorDetected(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo(), ErrorCode.ERROR_ON_CAMERA_ERROR);
                        return;
                    }
                }
                catch (final IllegalArgumentException ex3) {
                    if (!this.this$0.mPreviewSurface.isValid()) {
                        CamLog.w("SetRepeatingRequestTask : Preview surface is not valid, so the request is refused.");
                        return;
                    }
                    throw ex3;
                }
            }
            final ArrayList list = new ArrayList();
            if (this.this$0.mCameraDeviceHandler.isPreCaptureOnGoing() || this.this$0.mCameraDeviceHandler.isPreScanOnGoing()) {
                this.this$0.mCaptureRequestHolder.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_MODE, 1);
            }
            if (this.mCaptureSurface == null) {
                final CaptureRequest captureRequest = this.this$0.mCaptureRequestHolder.createCaptureRequest(this.this$0.mCameraDevice, 1, new Surface[] { this.this$0.mPreviewSurface });
                if (captureRequest == null) {
                    CamLog.i("SetRepeatingRequestTask : CaptureRequest cannot be created. mCaptureSurface is null.");
                    return;
                }
                list.add(captureRequest);
            }
            else {
                for (int i = 0; i < 3; ++i) {
                    Surface[] array = { this.this$0.mPreviewSurface };
                    if (i == 2) {
                        array = new Surface[] { this.this$0.mPreviewSurface, this.mCaptureSurface };
                    }
                    final CaptureRequest captureRequest2 = this.this$0.mCaptureRequestHolder.createCaptureRequest(this.this$0.mCameraDevice, 1, array);
                    if (captureRequest2 == null) {
                        CamLog.i("SetRepeatingRequestTask : CaptureRequest cannot be created. mCaptureSurface is not null.");
                        return;
                    }
                    list.add(captureRequest2);
                }
            }
            if (!this.this$0.mPreviewSurface.isValid()) {
                this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
                if (CamLog.DEBUG) {
                    CamLog.d("Preview surface is not valid, so the request is refused.");
                }
                return;
            }
            try {
                PerfLog.START_PREVIEW.begin();
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("setRepeatingBurst() requestNum:");
                    sb.append(list.size());
                    CamLog.d(sb.toString());
                }
                this.this$0.mCaptureSession.setRepeatingBurst((List)list, (CameraCaptureSession$CaptureCallback)this.mCaptureSessionCallback, this.this$0.mCameraDeviceHandler.getDeviceThreadHandler());
                if (CamLog.DEBUG) {
                    this.this$0.mCaptureRequestDumper.update((CaptureRequest)list.get(0));
                    this.this$0.mCaptureRequestDumper.dump();
                }
                DeviceStatus.Value value;
                if (this.this$0.mCameraDeviceHandler.isVideo()) {
                    if (this.this$0.mCameraDeviceHandler.isRecording()) {
                        value = DeviceStatus.Value.VIDEO_RECORDING;
                    }
                    else {
                        value = DeviceStatus.Value.VIDEO_PREVIEW;
                    }
                }
                else {
                    value = DeviceStatus.Value.STILL_PREVIEW;
                }
                final EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher(this.this$0.getApplicationContext(), this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId()).getCameraId());
                ((CameraStatusPublisher<DeviceStatus>)eachCameraStatusPublisher).put(new DeviceStatus(value));
                eachCameraStatusPublisher.publish();
            }
            catch (final IllegalArgumentException ex4) {
                CamLog.w("Failed in setRepeatingRequest.", ex4);
            }
            catch (final CameraAccessException ex5) {
                if (this.this$0.mCameraDeviceHandler.isIgnoreCameraError() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) {
                    if (!this.this$0.mPreviewSurface.isValid()) {
                        this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
                        CamLog.d("setRepeatingRequest : mPreviewSurface is invalid.(after performing set repeating request)");
                    }
                    else {
                        if (ex5.getReason() != 3) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failed in setRepeatingRequest by CameraAccessException. Reason:");
                            sb2.append(ex5.getReason());
                            throw new RuntimeException(sb2.toString());
                        }
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Failed in setRepeatingRequest  by CameraAccessException. Reason:");
                        sb3.append(ex5.getReason());
                        CamLog.d(sb3.toString());
                        this.this$0.onCameraOtherErrorDetected(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo(), ErrorCode.ERROR_ON_CAMERA_ERROR);
                    }
                }
                else {
                    CamLog.w("Failed in setRepeatingRequest", (Throwable)ex5);
                }
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController$2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$0.getCameraDeviceStatus().ordinal()]) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed due to wrong status in SetRepeatingRequestTask. status: ");
                    sb.append(this.this$0.getCameraDeviceStatus());
                    throw new IllegalStateException(sb.toString());
                }
                case 5: {
                    return !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused() && ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested() && false;
                }
                case 3: {
                    return true;
                }
                case 1:
                case 2:
                case 4: {
                    return false;
                }
            }
        }
    }
    
    @WorkerThread
    private class SetSurfaceTask extends CameraDeviceAccessTask
    {
        private final Surface mSurface;
        final CameraController this$0;
        
        private SetSurfaceTask(final CameraController this$0, final CameraSessionId cameraSessionId, final Surface mSurface) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mSurface = mSurface;
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).setPerformancefLog(PerfLog.SET_REPEATING_REQUEST_TASK);
        }
        
        public void doCameraDeviceAccess() {
            if (this.mSurface != null && this.mSurface.isValid()) {
                this.this$0.mPreviewSurface = this.mSurface;
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested();
        }
    }
    
    @WorkerThread
    private class StopPreviewTask extends CameraDeviceAccessTask
    {
        final CameraController this$0;
        
        private StopPreviewTask(final CameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
        }
        
        public void doCameraDeviceAccess() {
            this.this$0.setCameraDeviceStatus(CameraDeviceStatus.STATUS_OPENED);
            while (true) {
                try {
                    try {
                        if (this.this$0.mCaptureSession != null) {
                            if (CamLog.DEBUG) {
                                CamLog.d("stopRepeating()");
                            }
                            this.this$0.mCaptureSession.stopRepeating();
                            if (!this.this$0.mCameraDeviceHandler.isSnapshotRunning()) {
                                this.this$0.mCaptureSession.abortCaptures();
                            }
                            this.this$0.mCaptureSession.close();
                        }
                        this.this$0.mCaptureSession = null;
                        this.this$0.mCaptureRequestDumper = null;
                        this.this$0.mOutputConfiguration = null;
                        this.this$0.mPreviewSurface = null;
                        this.this$0.mCaptureImageRetriever = null;
                    }
                    finally {}
                }
                catch (final CameraAccessException | IllegalStateException ex) {
                    CamLog.e("StopPreviewTask: Close session failed: ", (Throwable)ex);
                    continue;
                }
                break;
            }
            this.mLatch.countDown();
            return;
            this.this$0.mCaptureSession = null;
            this.this$0.mCaptureRequestDumper = null;
            this.this$0.mOutputConfiguration = null;
            this.this$0.mPreviewSurface = null;
            this.this$0.mCaptureImageRetriever = null;
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            switch (CameraController$2.$SwitchMap$com$sonyericsson$android$camera$device$CameraDeviceHandler$CameraDeviceStatus[this.this$0.getCameraDeviceStatus().ordinal()]) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed due to wrong status in StopPreviewTask. status: ");
                    sb.append(this.this$0.getCameraDeviceStatus());
                    throw new IllegalStateException(sb.toString());
                }
                case 3:
                case 4: {
                    if (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseCameraTaskRequested()) {
                        CamLog.d("StopPreviewTask : CloseCameraTask is already requested.");
                        this.mLatch.countDown();
                        return false;
                    }
                    return true;
                }
                case 1:
                case 2:
                case 5: {
                    this.mLatch.countDown();
                    return false;
                }
            }
        }
    }
    
    private class StreamingImageRetriever implements ImageRetriever
    {
        final CameraController this$0;
        
        private StreamingImageRetriever(final CameraController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void registerPreviewStreamingCallback(final OnImageRetrieverCallback onImageRetrieverCallback, final Handler handler) {
            this.this$0.mPreviewFrameReceiver.registerCallback(new CaptureImageRequest(onImageRetrieverCallback, false, handler));
        }
        
        @Override
        public void requestOneShotPreviewCallback(final OnImageRetrieverCallback onImageRetrieverCallback, final Handler handler) {
        }
        
        @Override
        public void unregisterPreviewStreamingCallback(final OnImageRetrieverCallback onImageRetrieverCallback) {
            if (this.this$0.mPreviewFrameReceiver.hasCallback(onImageRetrieverCallback)) {
                this.this$0.mPreviewFrameReceiver.registerCallback(null);
            }
        }
    }
    
    private class VideoThumbnailImageRetriever implements ImageRetriever
    {
        private final ImageReader mImageReader;
        private final CameraDeviceHandler.CameraSessionId mSessionId;
        final CameraController this$0;
        
        private VideoThumbnailImageRetriever(final CameraController this$0, final CameraDeviceHandler.CameraSessionId mSessionId, final ImageReader mImageReader) {
            this.this$0 = this$0;
            this.mSessionId = mSessionId;
            this.mImageReader = mImageReader;
        }
        
        @Override
        public void registerPreviewStreamingCallback(final OnImageRetrieverCallback onImageRetrieverCallback, final Handler handler) {
        }
        
        @Override
        public void requestOneShotPreviewCallback(final OnImageRetrieverCallback onImageRetrieverCallback, final Handler handler) {
            if (this.mImageReader == null) {
                return;
            }
            try {
                if (this.this$0.mCameraDevice != null) {
                    CameraParameterValidator.validate(this.this$0.mCameraDevice.getId(), this.this$0.mCaptureRequestHolder);
                }
            }
            catch (final RuntimeException ex) {
                if (CamLog.DEBUG) {
                    throw ex;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Fail to valid camera parameter. : ");
                sb.append(ex.getMessage());
                CamLog.e(sb.toString());
            }
            this.this$0.mPreviewFrameReceiver.registerCallback(new CaptureImageRequest(onImageRetrieverCallback, true, handler));
            this.mImageReader.setOnImageAvailableListener((ImageReader$OnImageAvailableListener)this.this$0.mPreviewFrameReceiver, this.this$0.mCameraDeviceHandler.getDeviceThreadHandler());
            this.this$0.mCameraDeviceHandler.postCameraDeviceThread(new OneShotCaptureTask(this.mSessionId, this.this$0.mCaptureRequestHolder, this.mImageReader));
        }
        
        @Override
        public void unregisterPreviewStreamingCallback(final OnImageRetrieverCallback onImageRetrieverCallback) {
        }
    }
}
