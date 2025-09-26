package com.sonyericsson.android.camera.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import android.util.Size;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.recorder.utility.FpsMonitor;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.CapturePerformanceLogger;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.device.BypassCameraSnapshotInfoFactory;
import com.sonyericsson.cameracommon.device.CommonPlatformDependencyResolver;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.status.EachCameraStatusPublisher;
import com.sonyericsson.cameracommon.status.GlobalCameraStatusPublisher;
import com.sonyericsson.cameracommon.status.eachcamera.BurstShooting;
import com.sonyericsson.cameracommon.status.eachcamera.DeviceStatus;
import com.sonyericsson.cameracommon.status.global.BuiltInCameraIds;
import com.sonyericsson.cameracommon.storage.PhotoSavingRequest;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCameraParameters;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCameraTimeoutException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class BypassCameraController {
    private static final int BYPASSCAMERA_IMAGE_READER_BUFFER_NUM = 1;
    private static final int BYPASSCAMERA_MIN_CAPTURE_BUFFER_NUM = 2;
    private static final int BYPASS_CAMERA_CALLBACK_TIMEOUT_MILLIS = 2000;
    private static final String CLOSE_BYPASS_CAMERA_TASK_WAKE_LOCK_TAG = "CameraApp";
    private static final long IMAGE_READER_PREPARED_WAIT_TIME_MILLIS = 1000;
    private static final String THREAD_NAME = "BypassCamera";
    private static final long TIMEOUT_WAIT_FOR_ALL_SNAPSHOT_DONE_MILLIS_REC = 3000;
    private final Context mApplicationContext;
    private final BypassCameraControllerCallback mCallback;
    private final CameraDeviceHandler.CameraDeviceHandlerInquirer mCameraDeviceHandler;
    private PowerManager.WakeLock mCloseBypassCameraWakeLock;
    private final FpsMonitor mImageFpsMonitor;
    private Handler mImageReaderHandler;
    private volatile CountDownLatch mImageReaderPreparedLatch;
    private final Object mImageReaderPreparedLockObject;
    private CountDownLatch mImageReaderReadyLatch;
    private final Object mImageReaderReadyLockObject;
    private volatile CaptureImageReaderRequest mPrevCaptureImageReaderRequest;
    private final FpsMonitor mShutterFpsMonitor;
    private CountDownLatch mWaitForAllSnapshotLock;
    private CountDownLatch mWaitForSnapshotReadyLock;
    private final Object mWaitForSnapshotReadyLockObject;
    private volatile BypassCamera mBypassCamera = null;
    private BypassCameraParameters mBypassCameraParameters = null;
    private SnapshotCallbackImpl mSnapshotCallback = null;
    private ExecutorService mBypassCameraRequestExecutor = null;
    private RequestFactory.PhotoSavingRequestBuilder mPreCaptureResult = null;
    private boolean mIsApplyBypassCameraModeRequired = false;
    private int mCapturingBufferNum = -1;
    private volatile ImageReader mCaptureImageReader = null;
    private boolean mIsSnapshotReadyWaiting = false;
    private boolean mIsSnapshotReady = false;
    private final Deque<TakenStatusCommon> mRemainRequestQueue = new LinkedList();
    private final Deque<RequestFactory.PhotoSavingRequestBuilder> mSavingPhotoRequestQueue = new LinkedList();

    interface BypassCameraControllerCallback {
        void onCameraClosed();

        void onPrepareBurstDone(boolean z);

        void onShutterDone(int i, int i2, boolean z);

        void onSnapshotDone(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder);

        void onSnapshotReadyDone(ExecutorService executorService, boolean z, boolean z2, boolean z3, BypassCamera.DisplayFlashColor displayFlashColor);
    }

    private boolean isModified(UserSettingValue userSettingValue, UserSettingValue userSettingValue2) {
        return userSettingValue == null || userSettingValue2 == null || userSettingValue != userSettingValue2;
    }

    private static String makeCountDownLatchInfo(String str, CountDownLatch countDownLatch) {
        if (countDownLatch == null) {
            return str + ": null";
        }
        return str + ":" + countDownLatch.getCount();
    }

    void dump(StringBuilder sb) {
        sb.append("mBypassCamera:" + this.mBypassCamera + "\n");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(makeCountDownLatchInfo("mWaitForAllSnapshotLock", this.mWaitForAllSnapshotLock));
        sb2.append("\n");
        sb.append(sb2.toString());
        sb.append(makeCountDownLatchInfo("mImageReaderReadyLatch", this.mImageReaderReadyLatch) + "\n");
        sb.append("mRemainRequestQueue:" + this.mRemainRequestQueue + "\n");
        sb.append("mSavingPhotoRequestQueue:" + this.mSavingPhotoRequestQueue + "\n");
        sb.append("mIsSnapshotReadyWaiting:" + this.mIsSnapshotReadyWaiting + "\n");
        sb.append("mCapturingBufferNum:" + this.mCapturingBufferNum + "\n");
        sb.append("mPreCaptureResult:" + this.mPreCaptureResult + "\n");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Context getApplicationContext() {
        return this.mApplicationContext;
    }

    BypassCameraController(Context context, BypassCameraControllerCallback bypassCameraControllerCallback, CameraDeviceHandler.CameraDeviceHandlerInquirer cameraDeviceHandlerInquirer) {
        this.mApplicationContext = context;
        HandlerThread handlerThread = new HandlerThread("ImageReader");
        handlerThread.start();
        this.mImageReaderHandler = new Handler(handlerThread.getLooper());
        this.mShutterFpsMonitor = new FpsMonitor(PredictiveCapture.AUTO.getCaptureNum());
        this.mImageFpsMonitor = new FpsMonitor(PredictiveCapture.AUTO.getCaptureNum());
        this.mImageReaderReadyLockObject = new Object();
        this.mImageReaderPreparedLockObject = new Object();
        this.mWaitForSnapshotReadyLockObject = new Object();
        this.mCameraDeviceHandler = cameraDeviceHandlerInquirer;
        this.mCallback = bypassCameraControllerCallback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean preloadSettings(SharedPreferences sharedPreferences, UserSettings userSettings, CapturingMode capturingMode, CameraParameters cameraParameters) throws Resources.NotFoundException {
        boolean z;
        Rect optimalPreviewSize;
        Rect optimalPreviewSize2;
        String value;
        VideoHdr videoHdr;
        VideoSize defaultVideoSize;
        Rect pictureRect = null;
        if (userSettings != null) {
            if (capturingMode.isVideo()) {
                defaultVideoSize = (VideoSize) userSettings.get(UserSettingKey.VIDEO_SIZE);
                if (defaultVideoSize == null) {
                    defaultVideoSize = PlatformDependencyResolver.getDefaultVideoSize(capturingMode.getCameraId());
                }
                VideoStabilizer videoStabilizer = (VideoStabilizer) userSettings.get(UserSettingKey.VIDEO_STABILIZER);
                videoHdr = (VideoHdr) userSettings.get(UserSettingKey.VIDEO_HDR);
                value = videoStabilizer.getValue();
                if (videoHdr == VideoHdr.HDR_ON) {
                    optimalPreviewSize2 = PlatformCapability.getPreferredPreviewSizeForHdrVideo(capturingMode.getCameraId());
                } else {
                    Rect preferredPreviewSizeForVideo = PlatformCapability.getPreferredPreviewSizeForVideo(capturingMode.getCameraId());
                    if (preferredPreviewSizeForVideo.width() == 0 || preferredPreviewSizeForVideo.height() == 0) {
                        preferredPreviewSizeForVideo = PlatformDependencyResolver.getPreferredPreviewSizeFromCaptureSize(defaultVideoSize.getVideoRect());
                    }
                    optimalPreviewSize2 = CommonPlatformDependencyResolver.getOptimalVideoPreviewRect(defaultVideoSize.getVideoRect(), preferredPreviewSizeForVideo, PlatformCapability.getSupportedPreviewSizes(capturingMode.getCameraId()));
                }
            } else {
                Resolution defaultValue = (Resolution) userSettings.get(UserSettingKey.RESOLUTION);
                if (defaultValue == null) {
                    defaultValue = Resolution.getDefaultValue(capturingMode);
                }
                optimalPreviewSize2 = PlatformDependencyResolver.getOptimalPreviewSize(capturingMode.getCameraId(), capturingMode.getType(), defaultValue.getPictureRect());
                value = null;
                videoHdr = null;
                pictureRect = defaultValue.getPictureRect();
                defaultVideoSize = null;
            }
            cameraParameters.setVideoSize(defaultVideoSize);
            cameraParameters.setPreviewSize(optimalPreviewSize2);
            cameraParameters.setVideoStabilizer(value);
            cameraParameters.setPictureSize(pictureRect);
            cameraParameters.setVideoHdr(videoHdr);
            if (CamLog.DEBUG) {
                CamLog.d("Load settings from user settings. mode:" + capturingMode + " video:" + cameraParameters.getVideoSize() + " preview:" + cameraParameters.getPreviewSize() + " vs:" + cameraParameters.getVideoStabilizer() + " picture:" + cameraParameters.getPictureSize() + " modified:true");
            }
            return true;
        }
        if (sharedPreferences != null) {
            if (capturingMode.isVideo()) {
                UserSettingKey userSettingKey = UserSettingKey.VIDEO_SIZE;
                String strCreatePrefix = SharedPreferencesAccessor.createPrefix(userSettingKey.getCategory(), capturingMode, "");
                UserSettingValueHolder userSettingValueHolder = new UserSettingValueHolder(null);
                String string = sharedPreferences.getString(strCreatePrefix + userSettingKey, null);
                if (string != null) {
                    userSettingValueHolder.parseValueString(string);
                }
                VideoSize defaultVideoSize2 = (VideoSize) userSettingValueHolder.get();
                UserSettingKey userSettingKey2 = UserSettingKey.VIDEO_STABILIZER;
                String strCreatePrefix2 = SharedPreferencesAccessor.createPrefix(userSettingKey2.getCategory(), capturingMode, "");
                UserSettingValueHolder userSettingValueHolder2 = new UserSettingValueHolder(null);
                String string2 = sharedPreferences.getString(strCreatePrefix2 + userSettingKey2, null);
                if (string2 != null) {
                    userSettingValueHolder2.parseValueString(string2);
                }
                VideoStabilizer recommendedVideoStabilizerValue = (VideoStabilizer) userSettingValueHolder2.get();
                UserSettingKey userSettingKey3 = UserSettingKey.VIDEO_HDR;
                UserSettingValueHolder userSettingValueHolder3 = new UserSettingValueHolder(null);
                String string3 = sharedPreferences.getString(strCreatePrefix2 + userSettingKey3, null);
                if (string3 != null) {
                    userSettingValueHolder3.parseValueString(string3);
                }
                VideoHdr videoHdr2 = (VideoHdr) userSettingValueHolder3.get();
                if (defaultVideoSize2 == null) {
                    defaultVideoSize2 = PlatformDependencyResolver.getDefaultVideoSize(capturingMode.getCameraId());
                }
                if (recommendedVideoStabilizerValue == null) {
                    recommendedVideoStabilizerValue = VideoStabilizer.getRecommendedVideoStabilizerValue(getApplicationContext(), capturingMode, defaultVideoSize2);
                }
                if (videoHdr2 == VideoHdr.HDR_ON) {
                    optimalPreviewSize = PlatformCapability.getPreferredPreviewSizeForHdrVideo(capturingMode.getCameraId());
                } else {
                    optimalPreviewSize = PlatformDependencyResolver.getOptimalPreviewSize(capturingMode.getCameraId(), capturingMode.getType(), defaultVideoSize2.getVideoRect());
                }
                if (isModified(cameraParameters.getVideoSize(), defaultVideoSize2) || !recommendedVideoStabilizerValue.getValue().equals(cameraParameters.getVideoStabilizer()) || isModified(cameraParameters.getPreviewSize(), optimalPreviewSize)) {
                    cameraParameters.setVideoSize(defaultVideoSize2);
                    cameraParameters.setPreviewSize(optimalPreviewSize);
                    cameraParameters.setVideoStabilizer(recommendedVideoStabilizerValue.getValue());
                    cameraParameters.setPictureSize(null);
                    cameraParameters.setVideoHdr(videoHdr2);
                    z = true;
                }
                z = false;
            } else {
                UserSettingKey userSettingKey4 = UserSettingKey.RESOLUTION;
                String strCreatePrefix3 = SharedPreferencesAccessor.createPrefix(userSettingKey4.getCategory(), capturingMode, "");
                UserSettingValueHolder userSettingValueHolder4 = new UserSettingValueHolder(null);
                String string4 = sharedPreferences.getString(strCreatePrefix3 + userSettingKey4, null);
                if (string4 != null) {
                    userSettingValueHolder4.parseValueString(string4);
                }
                Resolution defaultValue2 = (Resolution) userSettingValueHolder4.get();
                if (defaultValue2 == null) {
                    defaultValue2 = Resolution.getDefaultValue(capturingMode);
                }
                Rect optimalPreviewSize3 = PlatformDependencyResolver.getOptimalPreviewSize(capturingMode.getCameraId(), capturingMode.getType(), defaultValue2.getPictureRect());
                if (isModified(cameraParameters.getPictureSize(), defaultValue2.getPictureRect()) || isModified(cameraParameters.getPreviewSize(), optimalPreviewSize3)) {
                    cameraParameters.setPictureSize(defaultValue2.getPictureRect());
                    cameraParameters.setPreviewSize(optimalPreviewSize3);
                    cameraParameters.setVideoStabilizer(null);
                    cameraParameters.setVideoSize(null);
                    cameraParameters.setVideoHdr(null);
                    z = true;
                }
                z = false;
            }
            if (CamLog.DEBUG) {
                CamLog.d("Load settings from shared-preference. mode:" + capturingMode + " video:" + cameraParameters.getVideoSize() + " preview:" + cameraParameters.getPreviewSize() + " vs:" + cameraParameters.getVideoStabilizer() + " picture:" + cameraParameters.getPictureSize() + " modified:" + z);
            }
            return z;
        }
        if (this.mCameraDeviceHandler.isVideo()) {
            VideoSize defaultVideoSize3 = PlatformDependencyResolver.getDefaultVideoSize(capturingMode.getCameraId());
            cameraParameters.setVideoSize(defaultVideoSize3);
            cameraParameters.setVideoStabilizer(VideoStabilizer.getRecommendedVideoStabilizerValue(getApplicationContext(), capturingMode, defaultVideoSize3).getValue());
            cameraParameters.setPreviewSize(PlatformDependencyResolver.getOptimalPreviewSize(capturingMode.getCameraId(), capturingMode.getType(), defaultVideoSize3.getVideoRect()));
            cameraParameters.setPictureSize(null);
        } else {
            cameraParameters.setPictureSize(Resolution.getDefaultValue(capturingMode).getPictureRect());
            cameraParameters.setPreviewSize(PlatformDependencyResolver.getOptimalPreviewSize(capturingMode.getCameraId(), capturingMode.getType(), cameraParameters.getPictureSize()));
            cameraParameters.setVideoStabilizer(null);
            cameraParameters.setVideoSize(null);
        }
        if (CamLog.DEBUG) {
            CamLog.d("Load settings from camera device. mode:" + capturingMode + " video:" + cameraParameters.getVideoSize() + " preview:" + cameraParameters.getPreviewSize() + " vs:" + cameraParameters.getVideoStabilizer() + " picture:" + cameraParameters.getPictureSize() + " modified:true");
        }
        return true;
    }

    private boolean isModified(Rect rect, Rect rect2) {
        return rect == null || rect2 == null || rect.width() != rect2.width() || rect.height() != rect2.height();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String dumpRequestQueueStatus() {
        return "requests [" + this.mSavingPhotoRequestQueue.size() + ',' + this.mRemainRequestQueue.size() + ']';
    }

    private CountDownLatch createSavingPhotoRemainCountDownLatch() {
        synchronized (this.mSavingPhotoRequestQueue) {
            if (this.mWaitForAllSnapshotLock != null && this.mWaitForAllSnapshotLock.getCount() != 0) {
                return this.mWaitForAllSnapshotLock;
            }
            if (CamLog.DEBUG) {
                CamLog.d(dumpRequestQueueStatus());
            }
            if (this.mSavingPhotoRequestQueue.size() == 0 && this.mRemainRequestQueue.size() == 0) {
                return null;
            }
            return new CountDownLatch(this.mSavingPhotoRequestQueue.size() + this.mRemainRequestQueue.size());
        }
    }

    void enqueueSavingPhotoRequest(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        synchronized (this.mSavingPhotoRequestQueue) {
            this.mSavingPhotoRequestQueue.add(photoSavingRequestBuilder);
            if (CamLog.DEBUG) {
                CamLog.d(dumpRequestQueueStatus());
            }
        }
    }

    private class PrepareBurstCallbackImpl implements BypassCamera.PrepareBurstCallback {
        private PrepareBurstCallbackImpl() {
        }

        @Override // com.sonymobile.imageprocessor.bypasscamera2.BypassCamera.PrepareBurstCallback
        public void onPrepareBurstDone(boolean z) {
            BypassCameraController.this.mCallback.onPrepareBurstDone(z);
        }
    }

    private static class FinishBurstCallbackImpl implements BypassCamera.FinishBurstCallback {
        @Override // com.sonymobile.imageprocessor.bypasscamera2.BypassCamera.FinishBurstCallback
        public void onFinishBurstDone() {
        }

        private FinishBurstCallbackImpl() {
        }
    }

    void requestSnapshot(CameraDeviceHandler.CameraSessionId cameraSessionId, RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, int i) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
                return;
            }
            return;
        }
        CapturePerformanceLogger.create(photoSavingRequestBuilder).startSnapshot = SystemClock.uptimeMillis();
        synchronized (this.mSavingPhotoRequestQueue) {
            if (this.mBypassCamera != null) {
                int iMin = Math.min(this.mCapturingBufferNum - getRemainSavingPhotoRequestCount(), i);
                PerfLog.BYPASSCAMERA_REQUEST_SNAPSHOT.transit();
                this.mCameraDeviceHandler.postCameraDeviceThread(new RequestSnapshotTask(cameraSessionId, photoSavingRequestBuilder, iMin));
                enqueueSavingPhotoRequest(photoSavingRequestBuilder);
            } else {
                CamLog.w("requestSnapshot() mBypassCamera == null");
            }
        }
        new EachCameraStatusPublisher(getApplicationContext(), parameters.getCameraId()).put(new DeviceStatus(photoSavingRequestBuilder.mCommonStatus.savedFileType == SavingTaskManager.SavedFileType.PHOTO_DURING_REC ? DeviceStatus.Value.PICTURE_TAKING_DURING_VIDEO_RECORDING : DeviceStatus.Value.PICTURE_TAKING)).publish();
    }

    void requestPrepareBurstShot(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestPrepareBurstShotTask(cameraSessionId));
    }

    void requestFinishBurstShot(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestFinishBurstShotTask(cameraSessionId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RequestFactory.PhotoSavingRequestBuilder dequeueSavingPhotoRequestAndAttachImageReader(ImageReader imageReader) {
        RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilderPoll;
        synchronized (this.mSavingPhotoRequestQueue) {
            photoSavingRequestBuilderPoll = this.mSavingPhotoRequestQueue.poll();
            if (photoSavingRequestBuilderPoll != null) {
                this.mRemainRequestQueue.add(photoSavingRequestBuilderPoll.mCommonStatus);
                photoSavingRequestBuilderPoll.attachImageReader(imageReader, new OnImageReaderDetachedListenerImpl());
            }
            if (CamLog.DEBUG) {
                CamLog.d(dumpRequestQueueStatus());
            }
        }
        return photoSavingRequestBuilderPoll;
    }

    private class OnImageReaderDetachedListenerImpl implements PhotoSavingRequest.OnImageReaderDetachedListener {
        private OnImageReaderDetachedListenerImpl() {
        }

        @Override // com.sonyericsson.cameracommon.storage.PhotoSavingRequest.OnImageReaderDetachedListener
        public void onDetached(ImageReader imageReader) {
            synchronized (BypassCameraController.this.mSavingPhotoRequestQueue) {
                if (BypassCameraController.this.mCaptureImageReader == imageReader) {
                    BypassCameraController.this.mRemainRequestQueue.poll();
                    if (CamLog.DEBUG) {
                        CamLog.d(BypassCameraController.this.dumpRequestQueueStatus());
                    }
                    if (BypassCameraController.this.mWaitForAllSnapshotLock != null) {
                        BypassCameraController.this.mWaitForAllSnapshotLock.countDown();
                    }
                    if (PerfLog.IS_ENABLE && BypassCameraController.this.getRemainSavingPhotoRequestCount() == 0) {
                        CamLog.d("SHUTTER_FPS_MONITOR:" + BypassCameraController.this.mShutterFpsMonitor.dump());
                        CamLog.d("IMAGE_FPS_MONITOR:" + BypassCameraController.this.mImageFpsMonitor.dump());
                        BypassCameraController.this.mShutterFpsMonitor.reset();
                        BypassCameraController.this.mImageFpsMonitor.reset();
                    }
                    return;
                }
                CamLog.e("This callback is not for current ImageReader.");
            }
        }
    }

    RequestFactory.PhotoSavingRequestBuilder peekLastSavingPhotoRequest() {
        RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilderPeekLast;
        synchronized (this.mSavingPhotoRequestQueue) {
            photoSavingRequestBuilderPeekLast = this.mSavingPhotoRequestQueue.peekLast();
            if (CamLog.DEBUG) {
                CamLog.d(dumpRequestQueueStatus());
            }
        }
        return photoSavingRequestBuilderPeekLast;
    }

    boolean isSnapshotRunning() {
        boolean z;
        synchronized (this.mSavingPhotoRequestQueue) {
            if (CamLog.DEBUG) {
                CamLog.d(dumpRequestQueueStatus());
            }
            z = this.mSavingPhotoRequestQueue.size() > 0;
        }
        return z;
    }

    int getRemainSavingPhotoRequestCount() {
        int size;
        synchronized (this.mSavingPhotoRequestQueue) {
            if (CamLog.DEBUG) {
                CamLog.d(dumpRequestQueueStatus());
            }
            size = this.mSavingPhotoRequestQueue.size() + this.mRemainRequestQueue.size();
        }
        return size;
    }

    int getRemainPrevSavingRequestCount() {
        int size;
        synchronized (this.mSavingPhotoRequestQueue) {
            size = this.mSavingPhotoRequestQueue.size() + this.mRemainRequestQueue.size();
            for (RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder : this.mSavingPhotoRequestQueue) {
                if (photoSavingRequestBuilder != null && photoSavingRequestBuilder.mCommonStatus.takenByFastCapture) {
                    size--;
                }
            }
            for (TakenStatusCommon takenStatusCommon : this.mRemainRequestQueue) {
                if (takenStatusCommon != null && takenStatusCommon.takenByFastCapture) {
                    size--;
                }
            }
        }
        return size;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createSnapshotReadyCountDownLatch() {
        synchronized (this.mWaitForSnapshotReadyLockObject) {
            if (this.mIsSnapshotReadyWaiting) {
                this.mWaitForSnapshotReadyLock = new CountDownLatch(1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSnapshotReadyWaiting(boolean z) {
        synchronized (this.mWaitForSnapshotReadyLockObject) {
            this.mIsSnapshotReadyWaiting = z;
            if (!this.mIsSnapshotReadyWaiting && this.mWaitForSnapshotReadyLock != null) {
                this.mWaitForSnapshotReadyLock.countDown();
            }
        }
    }

    void setPreCaptureResult(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        this.mPreCaptureResult = photoSavingRequestBuilder;
    }

    RequestFactory.PhotoSavingRequestBuilder getAndClearPreCaptureResult() {
        RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = this.mPreCaptureResult;
        this.mPreCaptureResult = null;
        return photoSavingRequestBuilder;
    }

    private void acquireCloseBypassCameraWakeLock() {
        if (getApplicationContext() == null) {
            CamLog.e("Application context is not set. So wake lock could not be acquired for closing camera.");
            return;
        }
        if (this.mCloseBypassCameraWakeLock == null) {
            this.mCloseBypassCameraWakeLock = ((PowerManager) getApplicationContext().getSystemService(PowerManager.class)).newWakeLock(1, CLOSE_BYPASS_CAMERA_TASK_WAKE_LOCK_TAG);
        }
        this.mCloseBypassCameraWakeLock.acquire(100000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseCloseBypassCameraWakeLock() {
        if (this.mCloseBypassCameraWakeLock == null) {
            CamLog.e("Wake lock is not created correctly.");
        } else {
            this.mCloseBypassCameraWakeLock.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BypassCamera.VideoMode getVideoMode(CameraInfo.CameraId cameraId, String str, VideoHdr videoHdr) {
        BypassCamera.VideoMode videoMode = BypassCamera.VideoMode.NORMAL;
        if (str != null) {
            if ("on".equals(str) || "on".equals(str)) {
                videoMode = BypassCamera.VideoMode.STEADYSHOT;
            } else if (CameraParameters.VS_ON_INTELLIGENT_ACTIVE.equals(str)) {
                videoMode = BypassCamera.VideoMode.INTELLIGENTACTIVE;
            }
        }
        if (!PlatformCapability.isVideoHdrSupported(cameraId) || videoHdr == null || videoHdr != VideoHdr.HDR_ON) {
            return videoMode;
        }
        if (videoMode == BypassCamera.VideoMode.STEADYSHOT) {
            return BypassCamera.VideoMode.HDR_STEADYSHOT;
        }
        return BypassCamera.VideoMode.HDR;
    }

    @WorkerThread
    private class RequestSnapshotTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private final RequestFactory.PhotoSavingRequestBuilder mBuilder;
        private final int mCaptureNum;

        private RequestSnapshotTask(CameraDeviceHandler.CameraSessionId cameraSessionId, RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, int i) {
            super(cameraSessionId);
            this.mBuilder = photoSavingRequestBuilder;
            this.mCaptureNum = i;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                CamLog.d("requestSnapshot()  captureNum:" + this.mCaptureNum + " dateTaken:" + this.mBuilder.getDateTaken() + " filePath:" + this.mBuilder.getFilePath() + " extraOutput:" + this.mBuilder.getExtraOutput() + " requestId" + this.mBuilder.getRequestId());
            }
            BypassCameraController.this.mBypassCamera.requestSnapshot(BypassCameraSnapshotInfoFactory.create(this.mBuilder, this.mCaptureNum));
        }
    }

    @WorkerThread
    private class RequestPrepareBurstShotTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private RequestPrepareBurstShotTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                CamLog.d("requestPrepareBurstShot()");
            }
            BypassCameraController.this.mBypassCamera.requestPrepareBurstShot();
            Context applicationContext = BypassCameraController.this.getApplicationContext();
            if (applicationContext != null) {
                new EachCameraStatusPublisher(applicationContext, BypassCameraController.this.mCameraDeviceHandler.getParameters(getSessionId()).getCameraId()).put(new BurstShooting(BurstShooting.Value.ON)).publish();
            }
        }
    }

    @WorkerThread
    private class RequestFinishBurstShotTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private RequestFinishBurstShotTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                CamLog.d("requestFinishBurstShot()");
            }
            BypassCameraController.this.mBypassCamera.requestFinishBurstShot();
            Context applicationContext = BypassCameraController.this.getApplicationContext();
            if (applicationContext != null) {
                new EachCameraStatusPublisher(applicationContext, BypassCameraController.this.mCameraDeviceHandler.getParameters(getSessionId()).getCameraId()).put(new BurstShooting(BurstShooting.Value.OFF)).publish();
            }
        }
    }

    private class SnapshotReadyCallbackImpl implements BypassCamera.SnapshotReadyCallback {
        private SnapshotReadyCallbackImpl() {
        }

        @Override // com.sonymobile.imageprocessor.bypasscamera2.BypassCamera.SnapshotReadyCallback
        public void onSnapshotReadyDone(boolean z, boolean z2, boolean z3, BypassCamera.DisplayFlashColor displayFlashColor) throws InterruptedException {
            BypassCameraController.this.setSnapshotReadyWaiting(false);
            BypassCameraController.this.awaitImageReaderReady();
            BypassCameraController.this.mCallback.onSnapshotReadyDone(BypassCameraController.this.mBypassCameraRequestExecutor, z, z2, z3, displayFlashColor);
        }
    }

    private static class SnapshotFreeCallbackImpl implements BypassCamera.SnapshotFreeCallback {
        @Override // com.sonymobile.imageprocessor.bypasscamera2.BypassCamera.SnapshotFreeCallback
        public void onSnapshotFreeDone() {
        }

        private SnapshotFreeCallbackImpl() {
        }
    }

    private class SnapshotCallbackImpl implements BypassCamera.SnapshotCallback, ImageReader.OnImageAvailableListener {
        private SnapshotCallbackImpl() {
        }

        @Override // com.sonymobile.imageprocessor.bypasscamera2.BypassCamera.SnapshotCallback
        public void onShutterDone(int i, int i2, boolean z) {
            PerfLog.BYPASSCAMERA_ON_SHUTTER_DONE.transit();
            if (PerfLog.IS_ENABLE) {
                BypassCameraController.this.mShutterFpsMonitor.addSampleMillis(System.currentTimeMillis());
            }
            LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.SHOT_TO_SHOT_DELAY);
            LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.SHOT_TO_SHOT_DELAY);
            BypassCameraController.this.mCallback.onShutterDone(i, i2, z);
        }

        @Override // com.sonymobile.imageprocessor.bypasscamera2.BypassCamera.SnapshotCallback
        public void onSnapshotDone(int i) {
            PerfLog.BYPASSCAMERA_ON_SNAPSHOT_DONE.transit();
        }

        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader imageReader) {
            PerfLog.BYPASSCAMERA_ON_IMAGE_AVAILABLE.transit();
            if (PerfLog.IS_ENABLE) {
                BypassCameraController.this.mImageFpsMonitor.addSampleMillis(System.currentTimeMillis());
            }
            if (CamLog.DEBUG) {
                String[] strArr = new String[1];
                StringBuilder sb = new StringBuilder();
                sb.append("invoked imageReader:");
                sb.append(imageReader != null);
                sb.append("pre-process:");
                sb.append(BypassCameraController.this.mCameraDeviceHandler.getPreProcessState());
                strArr[0] = sb.toString();
                CamLog.d(strArr);
            }
            if (imageReader != null) {
                RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilderDequeueSavingPhotoRequestAndAttachImageReader = BypassCameraController.this.dequeueSavingPhotoRequestAndAttachImageReader(imageReader);
                if (BypassCameraController.this.mCameraDeviceHandler.getPreProcessState() != CameraDeviceHandler.PreProcessState.PRE_CAPTURE_RELEASED) {
                    BypassCameraController.this.mCallback.onSnapshotDone(photoSavingRequestBuilderDequeueSavingPhotoRequestAndAttachImageReader);
                } else {
                    photoSavingRequestBuilderDequeueSavingPhotoRequestAndAttachImageReader.close();
                }
            }
        }
    }

    @WorkerThread
    private class OpenBypassCameraTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private final CapturingMode mMode;
        private final SharedPreferences mPreferences;
        private final FastCapture mRequestFastCapture;
        private final UserSettings mUserSettings;

        private OpenBypassCameraTask(SharedPreferences sharedPreferences, UserSettings userSettings, CameraDeviceHandler.CameraSessionId cameraSessionId, FastCapture fastCapture, CapturingMode capturingMode) throws Resources.NotFoundException {
            super(cameraSessionId);
            this.mPreferences = sharedPreferences;
            this.mUserSettings = userSettings;
            this.mRequestFastCapture = fastCapture;
            this.mMode = capturingMode;
            if (fastCapture != FastCapture.LAUNCH_AND_CAPTURE) {
                BypassCameraController.this.preloadSettings(this.mPreferences, this.mUserSettings, this.mMode, BypassCameraController.this.mCameraDeviceHandler.getParameters(getSessionId()));
            }
            setPerformancefLog(PerfLog.OPEN_BYPASS_CAMERA_TASK);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return !getOpenCloseStatusInfo().isCloseBypassCameraTaskRequested();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() throws Resources.NotFoundException {
            BypassCamera.Facing facing;
            boolean z;
            boolean z2;
            getOpenCloseStatusInfo().setPerformed(CameraDeviceHandler.OpenClosePerformStatus.BYPASS_CAMERA_OPENED);
            CameraParameters parameters = BypassCameraController.this.mCameraDeviceHandler.getParameters(getSessionId());
            if (this.mRequestFastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
                BypassCameraController.this.preloadSettings(this.mPreferences, this.mUserSettings, this.mMode, parameters);
            }
            switch (parameters.getCameraId()) {
                case BACK:
                    facing = BypassCamera.Facing.BACK;
                    break;
                case FRONT:
                    facing = BypassCamera.Facing.FRONT;
                    break;
                default:
                    throw new IllegalStateException("OpenBypassCameraTask:[preview] [UnExpected camera Id=" + parameters.getCameraId() + "]");
            }
            try {
                BypassCameraController.this.mBypassCameraRequestExecutor = ThreadUtil.buildExecutor(BypassCameraController.THREAD_NAME);
                if (CamLog.DEBUG) {
                    CamLog.d("Open BypassCamera. facing:" + facing);
                }
                BypassCameraController.this.mBypassCamera = BypassCamera.open(facing, BypassCameraController.this.mBypassCameraRequestExecutor);
                BypassCameraController.this.mBypassCameraParameters = new BypassCameraParameters();
                BypassCameraController.this.mSnapshotCallback = new SnapshotCallbackImpl();
                BypassCameraController.this.mBypassCamera.setPhotoCallbacks(new SnapshotReadyCallbackImpl(), BypassCameraController.this.mSnapshotCallback, new SnapshotFreeCallbackImpl());
                BypassCameraController.this.mBypassCamera.setBurstCallbacks(new PrepareBurstCallbackImpl(), new FinishBurstCallbackImpl());
                if (this.mMode.isVideo()) {
                    VideoSize videoSize = parameters.getVideoSize();
                    if (CamLog.DEBUG) {
                        CamLog.d("changeToVideoMode() preview:" + parameters.getPreviewSize() + " video:" + videoSize + " stabilizer:" + parameters.getVideoStabilizer() + " hdr:" + parameters.getVideoHdr());
                    }
                    try {
                        BypassCameraController.this.mBypassCamera.changeToVideoMode(BypassCameraController.this.getVideoMode(parameters.getCameraId(), parameters.getVideoStabilizer(), parameters.getVideoHdr()), BypassCameraController.this.toAndroidUtilSize(parameters.getPreviewSize()), BypassCameraController.this.toAndroidUtilSize(videoSize.getVideoRect()), RecordingProfile.getVideoFrameRate(videoSize, parameters.getVideoHdr()));
                    } catch (BypassCameraTimeoutException unused) {
                        BypassCameraController.this.handleTimeout(getSessionId());
                        BypassCameraController.this.mCameraDeviceHandler.changePreProcessStateTo(CameraDeviceHandler.PreProcessState.NOT_STARTED);
                        return;
                    }
                } else {
                    if (CamLog.DEBUG) {
                        CamLog.d("changeToPhotoMode() preview:" + parameters.getPreviewSize() + " picture:" + parameters.getPictureSize());
                    }
                    try {
                        BypassCameraController.this.mBypassCamera.changeToPhotoMode(BypassCamera.PhotoMode.NORMAL, new Size(parameters.getPreviewSize().width(), parameters.getPreviewSize().height()), new Size(parameters.getPictureSize().width(), parameters.getPictureSize().height()), PredictiveCapture.AUTO.getCaptureNum());
                    } catch (BypassCameraTimeoutException unused2) {
                        BypassCameraController.this.handleTimeout(getSessionId());
                        BypassCameraController.this.mCameraDeviceHandler.changePreProcessStateTo(CameraDeviceHandler.PreProcessState.NOT_STARTED);
                        return;
                    }
                }
                BypassCameraController.this.mCameraDeviceHandler.changePreProcessStateTo(CameraDeviceHandler.PreProcessState.NOT_STARTED);
                Context applicationContext = BypassCameraController.this.getApplicationContext();
                if (applicationContext == null || this.mRequestFastCapture != FastCapture.LAUNCH_AND_CAPTURE || applicationContext.checkSelfPermission("android.permission.CAMERA") == 0) {
                    z = true;
                } else {
                    CamLog.i("Camera permission is not granted, so pre-capture is refused.");
                    z = false;
                }
                switch (this.mRequestFastCapture) {
                    case LAUNCH_ONLY:
                        z2 = false;
                        break;
                    case LAUNCH_AND_CAPTURE:
                        z2 = true;
                        break;
                    default:
                        throw new IllegalStateException("OpenBypassCameraTask():[FastCapture=" + this.mRequestFastCapture + "]");
                }
                if (z2 && z) {
                    PerfLog.FAST_PRE_SCAN.transit();
                    Rect pictureSize = parameters.getPictureSize();
                    BypassCameraController.this.mCameraDeviceHandler.postCameraDeviceThread(new RequestPrepareCaptureImageReaderTask(getSessionId(), 1, 2, new Rect(0, 0, pictureSize.width(), pictureSize.height()), null));
                    BypassCameraController.this.requestSnapshotReady(getSessionId());
                    BypassCameraController.this.mCameraDeviceHandler.changePreProcessStateTo(CameraDeviceHandler.PreProcessState.PRE_SCAN_STARTED);
                }
                if (applicationContext != null) {
                    new EachCameraStatusPublisher(applicationContext, parameters.getCameraId()).put(new DeviceStatus(DeviceStatus.Value.POWER_ON)).publish();
                    new GlobalCameraStatusPublisher(applicationContext).put(new BuiltInCameraIds(parameters.getCameraId())).publish();
                }
            } catch (Exception e) {
                BypassCameraController.this.mBypassCameraRequestExecutor.shutdown();
                BypassCameraController.this.mBypassCameraRequestExecutor = null;
                CamLog.e("Failed to open BypassCamera. " + e);
            }
        }
    }

    @WorkerThread
    private class CloseBypassCameraTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private static final long TIMEOUT_WAIT_FOR_ALL_SNAPSHOT_DONE_MILLIS = 30000;
        private static final long TIMEOUT_WAIT_FOR_EACH_SNAPSHOT_DONE_MILLIS = 15000;
        private static final long TIMEOUT_WAIT_SNAPSHOT_READY_DONE_MILLIS = 5000;
        private final CountDownLatch mWaitForAllSnapshotDoneLock;

        private CloseBypassCameraTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
            this.mWaitForAllSnapshotDoneLock = null;
        }

        private CloseBypassCameraTask(CountDownLatch countDownLatch, CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
            this.mWaitForAllSnapshotDoneLock = countDownLatch;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return (getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() || getOpenCloseStatusInfo().isErrorCaused()) && !getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() throws InterruptedException {
            getOpenCloseStatusInfo().setPerformed(CameraDeviceHandler.OpenClosePerformStatus.BYPASS_CAMERA_CLOSED);
            BypassCameraController.this.awaitImageReaderReady();
            BypassCameraController.this.createSnapshotReadyCountDownLatch();
            try {
                if (BypassCameraController.this.mWaitForSnapshotReadyLock != null) {
                    CamLog.d("Waiting to complete snapshot ready:" + BypassCameraController.this.mWaitForSnapshotReadyLock.getCount());
                    if (BypassCameraController.this.mWaitForSnapshotReadyLock.await(TIMEOUT_WAIT_SNAPSHOT_READY_DONE_MILLIS, TimeUnit.MILLISECONDS)) {
                        CamLog.d("Snapshot ready done is completed");
                    } else {
                        CamLog.e("Timeout of waiting snapshot ready done.");
                    }
                }
                if (this.mWaitForAllSnapshotDoneLock != null) {
                    long count = this.mWaitForAllSnapshotDoneLock.getCount();
                    CamLog.d("Waiting to complete all snapshots:" + count);
                    if (this.mWaitForAllSnapshotDoneLock.await(Math.max(count * TIMEOUT_WAIT_FOR_EACH_SNAPSHOT_DONE_MILLIS, TIMEOUT_WAIT_FOR_ALL_SNAPSHOT_DONE_MILLIS), TimeUnit.MILLISECONDS)) {
                        CamLog.d("All snapshots done are completed");
                    } else {
                        CamLog.e("Timeout of waiting all snapshots done.");
                    }
                }
            } catch (InterruptedException unused) {
                CamLog.e("Intercept waiting request done.");
            }
            BypassCameraController.this.finalizeCaptureImageReader();
            BypassCameraController.this.mPrevCaptureImageReaderRequest = null;
            if (BypassCameraController.this.mIsSnapshotReady) {
                BypassCameraController.this.mBypassCamera.requestSnapshotFree();
                BypassCameraController.this.mIsSnapshotReady = false;
            }
            releaseBypassCamera();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void postCameraDeviceAccess() {
            removeOpenCloseStatusInfo();
            this.mLatch.countDown();
            BypassCameraController.this.releaseCloseBypassCameraWakeLock();
        }

        @WorkerThread
        private void releaseBypassCamera() throws InterruptedException {
            if (BypassCameraController.this.mBypassCamera != null) {
                try {
                    BypassCameraController.this.mBypassCameraRequestExecutor.shutdown();
                    boolean zAwaitTermination = BypassCameraController.this.mBypassCameraRequestExecutor.awaitTermination(2000L, TimeUnit.MILLISECONDS);
                    BypassCameraController.this.mBypassCameraRequestExecutor = null;
                    if (!zAwaitTermination) {
                        CamLog.e("Time-out occurs to release BypassCamera.");
                    }
                } catch (InterruptedException unused) {
                    CamLog.e("Time-out thread is interrupted.");
                }
                BypassCameraController.this.mBypassCamera.close();
                CamLog.d("BypassCamera is closed.");
            }
            BypassCameraController.this.mBypassCamera = null;
            BypassCameraController.this.mBypassCameraParameters = null;
            BypassCameraController.this.mSnapshotCallback = null;
            BypassCameraController.this.mWaitForAllSnapshotLock = null;
            BypassCameraController.this.mWaitForSnapshotReadyLock = null;
            BypassCameraController.this.mSavingPhotoRequestQueue.clear();
            BypassCameraController.this.mRemainRequestQueue.clear();
            BypassCameraController.this.mIsSnapshotReady = false;
            BypassCameraController.this.mCapturingBufferNum = -1;
            BypassCameraController.this.mCallback.onCameraClosed();
        }
    }

    private void changeBypassCameraModeAccordingToCurrentSetting(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("invoked required:" + this.mIsApplyBypassCameraModeRequired + " cameraId:" + parameters.getCameraId() + " video:" + this.mCameraDeviceHandler.isVideo() + " sessionId:" + cameraSessionId + " preProcessState:" + this.mCameraDeviceHandler.getPreProcessState() + " preview:" + parameters.getPreviewSize() + " picture:" + parameters.getPictureSize());
        }
        if (parameters.getPreviewSize() == null) {
            return;
        }
        if (this.mCameraDeviceHandler.isVideo() || parameters.getPictureSize() != null) {
            if ((this.mCameraDeviceHandler.getPreProcessState() == CameraDeviceHandler.PreProcessState.NOT_STARTED || this.mCameraDeviceHandler.getPreProcessState() == CameraDeviceHandler.PreProcessState.PRE_CAPTURE_RELEASED || this.mCameraDeviceHandler.getPreProcessState() == CameraDeviceHandler.PreProcessState.PRE_CAPTURE_DONE) && this.mIsApplyBypassCameraModeRequired) {
                this.mCameraDeviceHandler.postCameraDeviceThread(new ChangeBypassCameraModeTask(cameraSessionId));
                this.mIsApplyBypassCameraModeRequired = false;
            }
        }
    }

    @WorkerThread
    private class ChangeBypassCameraModeTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private ChangeBypassCameraModeTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public boolean verifyCameraDeviceStatus() {
            return getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            CameraParameters parameters = BypassCameraController.this.mCameraDeviceHandler.getParameters(getSessionId());
            SlowMotion slowMotion = parameters.getSlowMotion();
            if (!BypassCameraController.this.mCameraDeviceHandler.isVideo()) {
                if (CamLog.DEBUG) {
                    CamLog.d("changeToPhotoMode() preview:" + parameters.getPreviewSize() + " picture:" + parameters.getPictureSize() + " captureNum:" + PredictiveCapture.AUTO.getCaptureNum());
                }
                try {
                    BypassCameraController.this.mBypassCamera.changeToPhotoMode(BypassCamera.PhotoMode.NORMAL, BypassCameraController.this.toAndroidUtilSize(parameters.getPreviewSize()), BypassCameraController.this.toAndroidUtilSize(parameters.getPictureSize()), PredictiveCapture.AUTO.getCaptureNum());
                } catch (BypassCameraTimeoutException unused) {
                    BypassCameraController.this.handleTimeout(getSessionId());
                }
                if (CamLog.DEBUG) {
                    CamLog.d("setConfig() climax:" + BypassCameraController.this.mBypassCameraParameters.get("climax-recognition"));
                }
                BypassCameraController.this.mBypassCamera.setConfig(BypassCameraController.this.mBypassCameraParameters);
                return;
            }
            if (slowMotion == SlowMotion.SUPER_SLOW_MOTION || slowMotion == SlowMotion.SUPER_SLOW_SHOT) {
                BypassCamera.SuperSlowMode superSlowVideoMode = getSuperSlowVideoMode(slowMotion);
                VideoSize videoSize = parameters.getVideoSize();
                BypassCamera.SuperSlowRecordingParameters superSlowRecordingParametersCreateSuperSlowRecordingParameters = createSuperSlowRecordingParameters();
                int videoFrameRate = RecordingProfile.getVideoFrameRate(videoSize, VideoHdr.HDR_OFF);
                if (CamLog.DEBUG) {
                    CamLog.d("changeToSuperSlowMode() mode:" + superSlowVideoMode + " preview:" + parameters.getPreviewSize() + " video:" + videoSize.getVideoRect() + " framerate:" + videoFrameRate + " superSlowFps:" + superSlowRecordingParametersCreateSuperSlowRecordingParameters.fps + " superSlowFrameNum:" + superSlowRecordingParametersCreateSuperSlowRecordingParameters.frameNum);
                }
                try {
                    BypassCameraController.this.mBypassCamera.changeToSuperSlowMode(superSlowVideoMode, BypassCameraController.this.toAndroidUtilSize(parameters.getPreviewSize()), BypassCameraController.this.toAndroidUtilSize(videoSize.getVideoRect()), videoFrameRate, superSlowRecordingParametersCreateSuperSlowRecordingParameters);
                    return;
                } catch (BypassCameraTimeoutException unused2) {
                    BypassCameraController.this.handleTimeout(getSessionId());
                    return;
                }
            }
            VideoSize videoSize2 = parameters.getVideoSize();
            VideoHdr videoHdr = parameters.getVideoHdr();
            BypassCamera.VideoMode videoMode = BypassCameraController.this.getVideoMode(parameters.getCameraId(), parameters.getVideoStabilizer(), parameters.getVideoHdr());
            int videoFrameRate2 = RecordingProfile.getVideoFrameRate(videoSize2, videoHdr);
            if (CamLog.DEBUG) {
                CamLog.d("changeToVideoMode() mode:" + videoMode + " preview:" + parameters.getPreviewSize() + " video:" + videoSize2.getVideoRect() + " framerate:" + videoFrameRate2);
            }
            try {
                BypassCameraController.this.mBypassCamera.changeToVideoMode(videoMode, BypassCameraController.this.toAndroidUtilSize(parameters.getPreviewSize()), BypassCameraController.this.toAndroidUtilSize(videoSize2.getVideoRect()), videoFrameRate2);
            } catch (BypassCameraTimeoutException unused3) {
                BypassCameraController.this.handleTimeout(getSessionId());
            }
        }

        private BypassCamera.SuperSlowMode getSuperSlowVideoMode(SlowMotion slowMotion) {
            if (slowMotion == SlowMotion.SUPER_SLOW_MOTION) {
                return BypassCamera.SuperSlowMode.SUPER_SLOW_MOTION;
            }
            if (slowMotion == SlowMotion.SUPER_SLOW_SHOT) {
                return BypassCamera.SuperSlowMode.SUPER_SLOW_SHOT;
            }
            throw new IllegalArgumentException("value is not super slow, value = " + slowMotion);
        }

        private BypassCamera.SuperSlowRecordingParameters createSuperSlowRecordingParameters() {
            CameraParameters parameters = BypassCameraController.this.mCameraDeviceHandler.getParameters(getSessionId());
            return new BypassCamera.SuperSlowRecordingParameters((int) PlatformCapability.getSuperSlowFrameRate(parameters.getCameraId(), parameters.getVideoSize()), (int) PlatformCapability.getSuperSlowFrameNum(parameters.getCameraId(), parameters.getVideoSize()));
        }
    }

    void awaitAllSnapshotDone() {
        this.mWaitForAllSnapshotLock = createSavingPhotoRemainCountDownLatch();
        awaitAllSnapshot();
    }

    private void awaitAllSnapshot() {
        if (this.mWaitForAllSnapshotLock != null) {
            CamLog.d("awaitAllSnapshot() Waiting to complete all snapshots. count:" + this.mWaitForAllSnapshotLock.getCount());
            try {
                if (this.mWaitForAllSnapshotLock.await(3000L, TimeUnit.MILLISECONDS)) {
                    CamLog.d("awaitAllSnapshot() snapshots done are completed");
                } else {
                    CamLog.e("awaitAllSnapshot: Timeout of waiting all snapshots done.");
                }
            } catch (InterruptedException unused) {
                CamLog.e("awaitAllSnapshot Intercept waiting request done.");
            }
        }
    }

    void prepareCaptureImageReader(CameraDeviceHandler.CameraSessionId cameraSessionId, CameraDeviceHandler.ImageReaderInitializedCallback imageReaderInitializedCallback) {
        Rect pictureSize;
        CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("This session has been closed, so this request was refused. sessionId:" + cameraSessionId);
                return;
            }
            return;
        }
        if (this.mCameraDeviceHandler.isVideo()) {
            pictureSize = parameters.getVideoSize().getVideoRect();
        } else {
            pictureSize = parameters.getPictureSize();
        }
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestPrepareCaptureImageReaderTask(cameraSessionId, 1, Math.max(2, PredictiveCapture.AUTO.getCaptureNum() * 2), pictureSize, imageReaderInitializedCallback));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTimeout(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        openCloseStatusInfo.setRequested(CameraDeviceHandler.OpenCloseRequestStatus.NONE);
        openCloseStatusInfo.setPerformed(CameraDeviceHandler.OpenClosePerformStatus.NONE);
        openCloseStatusInfo.setOtherError();
    }

    @WorkerThread
    private class RequestPrepareCaptureImageReaderTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private final CaptureImageReaderRequest mRequest;

        private RequestPrepareCaptureImageReaderTask(CameraDeviceHandler.CameraSessionId cameraSessionId, int i, int i2, Rect rect, CameraDeviceHandler.ImageReaderInitializedCallback imageReaderInitializedCallback) {
            super(cameraSessionId);
            if (CamLog.DEBUG) {
                CamLog.d("invoked buffNum:" + i + "," + i2 + " size:" + rect.width() + "," + rect.height());
            }
            this.mRequest = new CaptureImageReaderRequest();
            this.mRequest.mImageReaderBufferNum = i;
            this.mRequest.mCapturingBufferNum = i2;
            this.mRequest.mCaptureSize = rect;
            this.mRequest.mCallback = imageReaderInitializedCallback;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return !getOpenCloseStatusInfo().isErrorCaused();
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            CountDownLatch countDownLatch;
            synchronized (BypassCameraController.this.mImageReaderReadyLockObject) {
                BypassCameraController.this.mImageReaderReadyLatch = new CountDownLatch(1);
                countDownLatch = BypassCameraController.this.mImageReaderReadyLatch;
            }
            BypassCameraController.this.prepareCaptureImageReader(getSessionId(), this.mRequest, countDownLatch);
        }
    }

    private static class CaptureImageReaderRequest {
        private CameraDeviceHandler.ImageReaderInitializedCallback mCallback;
        private Rect mCaptureSize;
        private int mCapturingBufferNum;
        private int mImageReaderBufferNum;

        private CaptureImageReaderRequest() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isSameRequest(CaptureImageReaderRequest captureImageReaderRequest) {
            return captureImageReaderRequest != null && this.mImageReaderBufferNum == captureImageReaderRequest.mImageReaderBufferNum && this.mCapturingBufferNum == captureImageReaderRequest.mCapturingBufferNum && this.mCaptureSize != null && captureImageReaderRequest.mCaptureSize != null && this.mCaptureSize.equals(captureImageReaderRequest.mCaptureSize);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareCaptureImageReader(final CameraDeviceHandler.CameraSessionId cameraSessionId, final CaptureImageReaderRequest captureImageReaderRequest, final CountDownLatch countDownLatch) {
        if (this.mCaptureImageReader != null) {
            if (captureImageReaderRequest.isSameRequest(this.mPrevCaptureImageReaderRequest)) {
                if (CamLog.DEBUG) {
                    CamLog.d("Same ImageReader has bean already requested");
                }
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
                if (captureImageReaderRequest.mCallback != null) {
                    captureImageReaderRequest.mCallback.onInitialized();
                    return;
                }
                return;
            }
            if (CamLog.DEBUG) {
                CamLog.d("ImageReader may be changed to: buffNum:" + captureImageReaderRequest.mImageReaderBufferNum + "," + captureImageReaderRequest.mCapturingBufferNum + " size:" + captureImageReaderRequest.mCaptureSize.width() + "," + captureImageReaderRequest.mCaptureSize.height());
            }
        }
        final boolean z = this.mCaptureImageReader != null;
        this.mCapturingBufferNum = captureImageReaderRequest.mCapturingBufferNum;
        this.mImageReaderHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.BypassCameraController.1
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                BypassCameraController.this.awaitImageReaderPrepared();
                CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
                if (BypassCameraController.this.mBypassCamera != null && openCloseStatusInfo != null && !openCloseStatusInfo.isCloseBypassCameraTaskRequested() && !openCloseStatusInfo.isCloseBypassCameraTaskPerformed()) {
                    if (!captureImageReaderRequest.isSameRequest(BypassCameraController.this.mPrevCaptureImageReaderRequest)) {
                        synchronized (BypassCameraController.this.mImageReaderPreparedLockObject) {
                            if (BypassCameraController.this.mImageReaderPreparedLatch == null || BypassCameraController.this.mImageReaderPreparedLatch.getCount() <= 0) {
                                if (CamLog.DEBUG) {
                                    CamLog.d("Latch ImageReaderPrepared.");
                                }
                                BypassCameraController.this.mImageReaderPreparedLatch = new CountDownLatch(1);
                            }
                        }
                        BypassCameraController.this.mPrevCaptureImageReaderRequest = captureImageReaderRequest;
                        if (z) {
                            BypassCameraController.this.finalizeCaptureImageReaderInternal();
                        }
                        BypassCameraController.this.mCaptureImageReader = ImageReader.newInstance(captureImageReaderRequest.mCaptureSize.width(), captureImageReaderRequest.mCaptureSize.height(), 256, captureImageReaderRequest.mImageReaderBufferNum);
                        BypassCameraController.this.mCaptureImageReader.setOnImageAvailableListener(BypassCameraController.this.mSnapshotCallback, BypassCameraController.this.mImageReaderHandler);
                        int i = captureImageReaderRequest.mCapturingBufferNum + captureImageReaderRequest.mImageReaderBufferNum;
                        PerfLog.BYPASSCAMERA_PREPARE.begin();
                        BypassCameraController.this.requestPrepareSnapshot(i);
                        PerfLog.BYPASSCAMERA_PREPARE.end();
                        BypassCameraController.this.unlockImageReaderReadyLatch(countDownLatch);
                        if (captureImageReaderRequest.mCallback != null) {
                            captureImageReaderRequest.mCallback.onInitialized();
                            return;
                        }
                        return;
                    }
                    if (CamLog.DEBUG) {
                        CamLog.d("Same ImageReader has bean already initialized");
                    }
                    BypassCameraController.this.unlockImageReaderReadyLatch(countDownLatch);
                    if (captureImageReaderRequest.mCallback != null) {
                        captureImageReaderRequest.mCallback.onInitialized();
                        return;
                    }
                    return;
                }
                if (CamLog.DEBUG) {
                    CamLog.d("Skip creating ImageReader. BypassCamera would be closed.");
                }
                BypassCameraController.this.unlockImageReaderReadyLatch(countDownLatch);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public void requestPrepareSnapshot(int i) {
        try {
            if (this.mBypassCamera == null) {
                synchronized (this.mImageReaderPreparedLockObject) {
                    if (this.mImageReaderPreparedLatch != null) {
                        CamLog.d("countDown ImageReaderPreparedLatch to " + (this.mImageReaderPreparedLatch.getCount() - 1));
                        this.mImageReaderPreparedLatch.countDown();
                    }
                }
                return;
            }
            if (CamLog.DEBUG) {
                CamLog.d("requestPrepareSnapshot() totalBufNumber:" + i);
            }
            this.mBypassCamera.requestPrepareSnapshot(this.mCaptureImageReader.getSurface(), i);
            synchronized (this.mImageReaderPreparedLockObject) {
                if (this.mImageReaderPreparedLatch != null) {
                    CamLog.d("countDown ImageReaderPreparedLatch to " + (this.mImageReaderPreparedLatch.getCount() - 1));
                    this.mImageReaderPreparedLatch.countDown();
                }
            }
        } catch (Throwable th) {
            synchronized (this.mImageReaderPreparedLockObject) {
                if (this.mImageReaderPreparedLatch != null) {
                    CamLog.d("countDown ImageReaderPreparedLatch to " + (this.mImageReaderPreparedLatch.getCount() - 1));
                    this.mImageReaderPreparedLatch.countDown();
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finalizeCaptureImageReader() {
        this.mImageReaderHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.BypassCameraController.2
            @Override // java.lang.Runnable
            public void run() {
                BypassCameraController.this.finalizeCaptureImageReaderInternal();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void finalizeCaptureImageReaderInternal() {
        if (this.mCaptureImageReader != null) {
            this.mCaptureImageReader.setOnImageAvailableListener(null, null);
            this.mCaptureImageReader.close();
            this.mCaptureImageReader = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void awaitImageReaderPrepared() throws InterruptedException {
        CountDownLatch countDownLatch;
        synchronized (this.mImageReaderPreparedLockObject) {
            countDownLatch = this.mImageReaderPreparedLatch;
        }
        if (countDownLatch != null) {
            try {
                countDownLatch.await(1000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                CamLog.e("Waiting ImageReader Prepared is interrupted.", e);
            }
        }
    }

    boolean isBypassCameraNextShotAvailable() {
        return this.mCapturingBufferNum - getRemainSavingPhotoRequestCount() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void awaitImageReaderReady() throws InterruptedException {
        CountDownLatch countDownLatch;
        synchronized (this.mImageReaderReadyLockObject) {
            countDownLatch = this.mImageReaderReadyLatch;
        }
        if (countDownLatch != null) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                CamLog.e("Waiting ImageReader ready is interrupted. " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unlockImageReaderReadyLatch(CountDownLatch countDownLatch) {
        if (countDownLatch == null) {
            return;
        }
        synchronized (this.mImageReaderReadyLockObject) {
            countDownLatch.countDown();
            if (this.mImageReaderReadyLatch == countDownLatch) {
                this.mImageReaderReadyLatch = null;
            }
        }
    }

    CameraDeviceHandler.CameraSessionId openBypassCamera(SharedPreferences sharedPreferences, UserSettings userSettings, FastCapture fastCapture, CapturingMode capturingMode) {
        if (!PlatformCapability.isPrepared() || PlatformCapability.hasDeviceError()) {
            CamLog.e("BypassCameraController.openBypassCamera() is rejected because PlatformCapability is not ready.");
            return null;
        }
        CameraDeviceHandler.CameraSessionId cameraSessionId = new CameraDeviceHandler.CameraSessionId();
        CameraDeviceHandler.CameraSessionInfo cameraSessionInfo = new CameraDeviceHandler.CameraSessionInfo(capturingMode.getCameraId());
        cameraSessionInfo.setRequested(CameraDeviceHandler.OpenCloseRequestStatus.BYPASS_CAMERA_OPENING);
        CameraDeviceHandler.CameraSessionInfo.addOpenCloseStatusInfo(cameraSessionId, cameraSessionInfo);
        PlatformCapability.getCameraInfo(capturingMode.getCameraId(), cameraSessionInfo.getCameraInfo());
        this.mCameraDeviceHandler.postCameraDeviceThread(new OpenBypassCameraTask(sharedPreferences, userSettings, cameraSessionId, fastCapture, capturingMode));
        return cameraSessionId;
    }

    void closeBypassCamera(boolean z, CameraDeviceHandler.CameraSessionId cameraSessionId) {
        CloseBypassCameraTask closeBypassCameraTask;
        if (this.mPreCaptureResult != null) {
            this.mPreCaptureResult.close();
            this.mPreCaptureResult = null;
        }
        this.mWaitForAllSnapshotLock = createSavingPhotoRemainCountDownLatch();
        CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        if (openCloseStatusInfo == null) {
            return;
        }
        openCloseStatusInfo.setRequested(CameraDeviceHandler.OpenCloseRequestStatus.BYPASS_CAMERA_CLOSING);
        if (this.mWaitForAllSnapshotLock != null) {
            closeBypassCameraTask = new CloseBypassCameraTask(this.mWaitForAllSnapshotLock, cameraSessionId);
        } else {
            closeBypassCameraTask = new CloseBypassCameraTask(cameraSessionId);
        }
        acquireCloseBypassCameraWakeLock();
        if (z) {
            this.mCameraDeviceHandler.postCameraDeviceThreadSync(closeBypassCameraTask);
        } else {
            this.mCameraDeviceHandler.postCameraDeviceThread(closeBypassCameraTask);
        }
    }

    @WorkerThread
    BypassCamera getBypassCameraInstance() {
        return this.mBypassCamera;
    }

    void requestSnapshotReady(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestSnapshotReadyTask(cameraSessionId));
    }

    @WorkerThread
    private class RequestSnapshotReadyTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private RequestSnapshotReadyTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return (!getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() || getOpenCloseStatusInfo().isErrorCaused() || getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed()) ? false : true;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            BypassCameraController.this.setSnapshotReadyWaiting(true);
            if (CamLog.DEBUG) {
                CamLog.d("requestSnapshotReady()");
            }
            BypassCameraController.this.mIsSnapshotReady = true;
            BypassCameraController.this.mBypassCamera.requestSnapshotReady();
        }
    }

    void requestSnapshotFree(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestSnapshotFreeTask(cameraSessionId));
    }

    @WorkerThread
    private class RequestSnapshotFreeTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private RequestSnapshotFreeTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return (!getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() || getOpenCloseStatusInfo().isErrorCaused() || getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed()) ? false : true;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                CamLog.d("requestSnapshotFree()");
            }
            BypassCameraController.this.mIsSnapshotReady = false;
            BypassCameraController.this.mBypassCamera.requestSnapshotFree();
        }
    }

    void commit(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        awaitImageReaderReady();
        changeBypassCameraModeAccordingToCurrentSetting(cameraSessionId);
    }

    void commitParameters(CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new SetConfigTask(cameraSessionId));
    }

    @WorkerThread
    private class SetConfigTask extends CameraDeviceHandler.CameraDeviceAccessTask {
        private SetConfigTask(CameraDeviceHandler.CameraSessionId cameraSessionId) {
            super(cameraSessionId);
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        protected boolean verifyCameraDeviceStatus() {
            return (!getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() || getOpenCloseStatusInfo().isErrorCaused() || getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed()) ? false : true;
        }

        @Override // com.sonyericsson.android.camera.device.CameraDeviceHandler.CameraDeviceAccessTask
        public void doCameraDeviceAccess() {
            BypassCameraController.this.mBypassCameraParameters.set("climax-recognition", BypassCameraController.this.mCameraDeviceHandler.getParameters(getSessionId()).getPredictiveCapture());
            if (CamLog.DEBUG) {
                CamLog.d("setConfig() climax:" + BypassCameraController.this.mBypassCameraParameters.get("climax-recognition"));
            }
            BypassCameraController.this.mBypassCamera.setConfig(BypassCameraController.this.mBypassCameraParameters);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Size toAndroidUtilSize(Rect rect) {
        return new Size(rect.width(), rect.height());
    }

    void requestApplyBypassCameraMode() {
        this.mIsApplyBypassCameraModeRequired = true;
    }
}
