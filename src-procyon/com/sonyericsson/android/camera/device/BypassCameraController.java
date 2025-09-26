// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import com.sonyericsson.cameracommon.status.CameraStatusPublisher;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.cameracommon.device.BypassCameraSnapshotInfoFactory;
import com.sonyericsson.cameracommon.status.eachcamera.BurstShooting;
import com.sonyericsson.cameracommon.status.global.BuiltInCameraIds;
import com.sonyericsson.cameracommon.status.GlobalCameraStatusPublisher;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCameraTimeoutException;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.cameracommon.status.eachcamera.DeviceStatus;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import com.sonyericsson.cameracommon.status.EachCameraStatusPublisher;
import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CapturePerformanceLogger;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import java.util.Iterator;
import android.support.annotation.WorkerThread;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.cameracommon.device.CommonPlatformDependencyResolver;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import android.media.ImageReader$OnImageAvailableListener;
import com.sonyericsson.cameracommon.storage.PhotoSavingRequest;
import java.util.concurrent.TimeUnit;
import android.os.PowerManager;
import com.sonyericsson.android.camera.util.CamLog;
import android.util.Size;
import android.graphics.Rect;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.setting.UserSettings;
import android.content.SharedPreferences;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import android.os.HandlerThread;
import java.util.LinkedList;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import java.util.Deque;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import java.util.concurrent.CountDownLatch;
import android.os.Handler;
import com.sonyericsson.android.camera.recorder.utility.FpsMonitor;
import android.os.PowerManager$WakeLock;
import android.media.ImageReader;
import java.util.concurrent.ExecutorService;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCameraParameters;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import android.content.Context;

class BypassCameraController
{
    private static final int BYPASSCAMERA_IMAGE_READER_BUFFER_NUM = 1;
    private static final int BYPASSCAMERA_MIN_CAPTURE_BUFFER_NUM = 2;
    private static final int BYPASS_CAMERA_CALLBACK_TIMEOUT_MILLIS = 2000;
    private static final String CLOSE_BYPASS_CAMERA_TASK_WAKE_LOCK_TAG = "CameraApp";
    private static final long IMAGE_READER_PREPARED_WAIT_TIME_MILLIS = 1000L;
    private static final String THREAD_NAME = "BypassCamera";
    private static final long TIMEOUT_WAIT_FOR_ALL_SNAPSHOT_DONE_MILLIS_REC = 3000L;
    private final Context mApplicationContext;
    private volatile BypassCamera mBypassCamera;
    private BypassCameraParameters mBypassCameraParameters;
    private ExecutorService mBypassCameraRequestExecutor;
    private final BypassCameraControllerCallback mCallback;
    private final CameraDeviceHandler.CameraDeviceHandlerInquirer mCameraDeviceHandler;
    private volatile ImageReader mCaptureImageReader;
    private int mCapturingBufferNum;
    private PowerManager$WakeLock mCloseBypassCameraWakeLock;
    private final FpsMonitor mImageFpsMonitor;
    private Handler mImageReaderHandler;
    private volatile CountDownLatch mImageReaderPreparedLatch;
    private final Object mImageReaderPreparedLockObject;
    private CountDownLatch mImageReaderReadyLatch;
    private final Object mImageReaderReadyLockObject;
    private boolean mIsApplyBypassCameraModeRequired;
    private boolean mIsSnapshotReady;
    private boolean mIsSnapshotReadyWaiting;
    private RequestFactory.PhotoSavingRequestBuilder mPreCaptureResult;
    private volatile CaptureImageReaderRequest mPrevCaptureImageReaderRequest;
    private final Deque<TakenStatusCommon> mRemainRequestQueue;
    private final Deque<RequestFactory.PhotoSavingRequestBuilder> mSavingPhotoRequestQueue;
    private final FpsMonitor mShutterFpsMonitor;
    private SnapshotCallbackImpl mSnapshotCallback;
    private CountDownLatch mWaitForAllSnapshotLock;
    private CountDownLatch mWaitForSnapshotReadyLock;
    private final Object mWaitForSnapshotReadyLockObject;
    
    BypassCameraController(final Context mApplicationContext, final BypassCameraControllerCallback mCallback, final CameraDeviceHandler.CameraDeviceHandlerInquirer mCameraDeviceHandler) {
        this.mBypassCamera = null;
        this.mBypassCameraParameters = null;
        this.mSnapshotCallback = null;
        this.mBypassCameraRequestExecutor = null;
        this.mPreCaptureResult = null;
        this.mIsApplyBypassCameraModeRequired = false;
        this.mCapturingBufferNum = -1;
        this.mCaptureImageReader = null;
        this.mIsSnapshotReadyWaiting = false;
        this.mIsSnapshotReady = false;
        this.mApplicationContext = mApplicationContext;
        this.mRemainRequestQueue = new LinkedList<TakenStatusCommon>();
        this.mSavingPhotoRequestQueue = new LinkedList<RequestFactory.PhotoSavingRequestBuilder>();
        final HandlerThread handlerThread = new HandlerThread("ImageReader");
        handlerThread.start();
        this.mImageReaderHandler = new Handler(handlerThread.getLooper());
        this.mShutterFpsMonitor = new FpsMonitor(PredictiveCapture.AUTO.getCaptureNum());
        this.mImageFpsMonitor = new FpsMonitor(PredictiveCapture.AUTO.getCaptureNum());
        this.mImageReaderReadyLockObject = new Object();
        this.mImageReaderPreparedLockObject = new Object();
        this.mWaitForSnapshotReadyLockObject = new Object();
        this.mCameraDeviceHandler = mCameraDeviceHandler;
        this.mCallback = mCallback;
    }
    
    private void acquireCloseBypassCameraWakeLock() {
        if (this.getApplicationContext() == null) {
            CamLog.e("Application context is not set. So wake lock could not be acquired for closing camera.");
            return;
        }
        if (this.mCloseBypassCameraWakeLock == null) {
            this.mCloseBypassCameraWakeLock = ((PowerManager)this.getApplicationContext().getSystemService((Class)PowerManager.class)).newWakeLock(1, "CameraApp");
        }
        this.mCloseBypassCameraWakeLock.acquire(100000L);
    }
    
    private void awaitAllSnapshot() {
        if (this.mWaitForAllSnapshotLock != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("awaitAllSnapshot() Waiting to complete all snapshots. count:");
            sb.append(this.mWaitForAllSnapshotLock.getCount());
            CamLog.d(sb.toString());
            try {
                if (this.mWaitForAllSnapshotLock.await(3000L, TimeUnit.MILLISECONDS)) {
                    CamLog.d("awaitAllSnapshot() snapshots done are completed");
                }
                else {
                    CamLog.e("awaitAllSnapshot: Timeout of waiting all snapshots done.");
                }
            }
            catch (final InterruptedException ex) {
                CamLog.e("awaitAllSnapshot Intercept waiting request done.");
            }
        }
    }
    
    private void awaitImageReaderPrepared() {
        final Object mImageReaderPreparedLockObject = this.mImageReaderPreparedLockObject;
        synchronized (mImageReaderPreparedLockObject) {
            final CountDownLatch mImageReaderPreparedLatch = this.mImageReaderPreparedLatch;
            monitorexit(mImageReaderPreparedLockObject);
            if (mImageReaderPreparedLatch != null) {
                try {
                    mImageReaderPreparedLatch.await(1000L, TimeUnit.MILLISECONDS);
                }
                catch (final InterruptedException mImageReaderPreparedLockObject) {
                    CamLog.e("Waiting ImageReader Prepared is interrupted.", (Throwable)mImageReaderPreparedLockObject);
                }
            }
        }
    }
    
    private void awaitImageReaderReady() {
        Object mImageReaderReadyLockObject = this.mImageReaderReadyLockObject;
        synchronized (mImageReaderReadyLockObject) {
            final CountDownLatch mImageReaderReadyLatch = this.mImageReaderReadyLatch;
            monitorexit(mImageReaderReadyLockObject);
            if (mImageReaderReadyLatch != null) {
                try {
                    mImageReaderReadyLatch.await();
                }
                catch (final InterruptedException obj) {
                    mImageReaderReadyLockObject = new StringBuilder();
                    ((StringBuilder)mImageReaderReadyLockObject).append("Waiting ImageReader ready is interrupted. ");
                    ((StringBuilder)mImageReaderReadyLockObject).append(obj);
                    CamLog.e(((StringBuilder)mImageReaderReadyLockObject).toString());
                }
            }
        }
    }
    
    private void changeBypassCameraModeAccordingToCurrentSetting(final CameraDeviceHandler.CameraSessionId obj) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(obj);
        if (parameters == null) {
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked required:");
            sb.append(this.mIsApplyBypassCameraModeRequired);
            sb.append(" cameraId:");
            sb.append(parameters.getCameraId());
            sb.append(" video:");
            sb.append(this.mCameraDeviceHandler.isVideo());
            sb.append(" sessionId:");
            sb.append(obj);
            sb.append(" preProcessState:");
            sb.append(this.mCameraDeviceHandler.getPreProcessState());
            sb.append(" preview:");
            sb.append(parameters.getPreviewSize());
            sb.append(" picture:");
            sb.append(parameters.getPictureSize());
            CamLog.d(sb.toString());
        }
        if (parameters.getPreviewSize() == null) {
            return;
        }
        if (!this.mCameraDeviceHandler.isVideo() && parameters.getPictureSize() == null) {
            return;
        }
        if (this.mCameraDeviceHandler.getPreProcessState() != CameraDeviceHandler.PreProcessState.NOT_STARTED && this.mCameraDeviceHandler.getPreProcessState() != CameraDeviceHandler.PreProcessState.PRE_CAPTURE_RELEASED && this.mCameraDeviceHandler.getPreProcessState() != CameraDeviceHandler.PreProcessState.PRE_CAPTURE_DONE) {
            return;
        }
        if (!this.mIsApplyBypassCameraModeRequired) {
            return;
        }
        this.mCameraDeviceHandler.postCameraDeviceThread(new ChangeBypassCameraModeTask(obj));
        this.mIsApplyBypassCameraModeRequired = false;
    }
    
    private CountDownLatch createSavingPhotoRemainCountDownLatch() {
        synchronized (this.mSavingPhotoRequestQueue) {
            if (this.mWaitForAllSnapshotLock != null && this.mWaitForAllSnapshotLock.getCount() != 0L) {
                return this.mWaitForAllSnapshotLock;
            }
            if (CamLog.DEBUG) {
                CamLog.d(this.dumpRequestQueueStatus());
            }
            if (this.mSavingPhotoRequestQueue.size() == 0 && this.mRemainRequestQueue.size() == 0) {
                return null;
            }
            return new CountDownLatch(this.mSavingPhotoRequestQueue.size() + this.mRemainRequestQueue.size());
        }
    }
    
    private void createSnapshotReadyCountDownLatch() {
        synchronized (this.mWaitForSnapshotReadyLockObject) {
            if (this.mIsSnapshotReadyWaiting) {
                this.mWaitForSnapshotReadyLock = new CountDownLatch(1);
            }
        }
    }
    
    private RequestFactory.PhotoSavingRequestBuilder dequeueSavingPhotoRequestAndAttachImageReader(final ImageReader imageReader) {
        synchronized (this.mSavingPhotoRequestQueue) {
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = this.mSavingPhotoRequestQueue.poll();
            if (photoSavingRequestBuilder != null) {
                this.mRemainRequestQueue.add(photoSavingRequestBuilder.mCommonStatus);
                photoSavingRequestBuilder.attachImageReader(imageReader, new OnImageReaderDetachedListenerImpl());
            }
            if (CamLog.DEBUG) {
                CamLog.d(this.dumpRequestQueueStatus());
            }
            return photoSavingRequestBuilder;
        }
    }
    
    private String dumpRequestQueueStatus() {
        final StringBuilder sb = new StringBuilder();
        sb.append("requests [");
        sb.append(this.mSavingPhotoRequestQueue.size());
        sb.append(',');
        sb.append(this.mRemainRequestQueue.size());
        sb.append(']');
        return sb.toString();
    }
    
    private void finalizeCaptureImageReader() {
        this.mImageReaderHandler.post((Runnable)new Runnable(this) {
            final BypassCameraController this$0;
            
            @Override
            public void run() {
                this.this$0.finalizeCaptureImageReaderInternal();
            }
        });
    }
    
    private void finalizeCaptureImageReaderInternal() {
        synchronized (this) {
            if (this.mCaptureImageReader != null) {
                this.mCaptureImageReader.setOnImageAvailableListener((ImageReader$OnImageAvailableListener)null, (Handler)null);
                this.mCaptureImageReader.close();
                this.mCaptureImageReader = null;
            }
        }
    }
    
    private Context getApplicationContext() {
        return this.mApplicationContext;
    }
    
    private BypassCamera.VideoMode getVideoMode(final CameraInfo.CameraId cameraId, final String anObject, final VideoHdr videoHdr) {
        BypassCamera.VideoMode videoMode2;
        final BypassCamera.VideoMode videoMode = videoMode2 = BypassCamera.VideoMode.NORMAL;
        if (anObject != null) {
            if ("on".equals(anObject)) {
                videoMode2 = BypassCamera.VideoMode.STEADYSHOT;
            }
            else if ("on".equals(anObject)) {
                videoMode2 = BypassCamera.VideoMode.STEADYSHOT;
            }
            else {
                videoMode2 = videoMode;
                if ("intelligent_active".equals(anObject)) {
                    videoMode2 = BypassCamera.VideoMode.INTELLIGENTACTIVE;
                }
            }
        }
        Enum<BypassCamera.VideoMode> enum1 = videoMode2;
        if (PlatformCapability.isVideoHdrSupported(cameraId)) {
            enum1 = videoMode2;
            if (videoHdr != null) {
                enum1 = videoMode2;
                if (videoHdr == VideoHdr.HDR_ON) {
                    if (videoMode2 == BypassCamera.VideoMode.STEADYSHOT) {
                        enum1 = BypassCamera.VideoMode.HDR_STEADYSHOT;
                    }
                    else {
                        enum1 = BypassCamera.VideoMode.HDR;
                    }
                }
            }
        }
        return (BypassCamera.VideoMode)enum1;
    }
    
    private void handleTimeout(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        final CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        openCloseStatusInfo.setRequested(CameraDeviceHandler.OpenCloseRequestStatus.NONE);
        openCloseStatusInfo.setPerformed(CameraDeviceHandler.OpenClosePerformStatus.NONE);
        openCloseStatusInfo.setOtherError();
    }
    
    private boolean isModified(final Rect rect, final Rect rect2) {
        final boolean b = true;
        if (rect != null && rect2 != null) {
            boolean b2 = b;
            if (rect.width() == rect2.width()) {
                b2 = (rect.height() != rect2.height() && b);
            }
            return b2;
        }
        return true;
    }
    
    private boolean isModified(final UserSettingValue userSettingValue, final UserSettingValue userSettingValue2) {
        boolean b = true;
        if (userSettingValue != null && userSettingValue2 != null) {
            if (userSettingValue == userSettingValue2) {
                b = false;
            }
            return b;
        }
        return true;
    }
    
    private static String makeCountDownLatchInfo(final String s, final CountDownLatch countDownLatch) {
        if (countDownLatch == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(": null");
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(":");
        sb2.append(countDownLatch.getCount());
        return sb2.toString();
    }
    
    private boolean preloadSettings(final SharedPreferences sharedPreferences, final UserSettings userSettings, final CapturingMode obj, final CameraParameters cameraParameters) {
        Rect pictureRect = null;
        if (userSettings != null) {
            String value;
            Rect previewSize;
            VideoSize videoSize;
            UserSettingValue videoHdr2;
            if (obj.isVideo()) {
                VideoSize defaultVideoSize;
                if ((defaultVideoSize = (VideoSize)userSettings.get(UserSettingKey.VIDEO_SIZE)) == null) {
                    defaultVideoSize = PlatformDependencyResolver.getDefaultVideoSize(obj.getCameraId());
                }
                final VideoStabilizer videoStabilizer = (VideoStabilizer)userSettings.get(UserSettingKey.VIDEO_STABILIZER);
                final VideoHdr videoHdr = (VideoHdr)userSettings.get(UserSettingKey.VIDEO_HDR);
                value = videoStabilizer.getValue();
                if (videoHdr == VideoHdr.HDR_ON) {
                    previewSize = PlatformCapability.getPreferredPreviewSizeForHdrVideo(obj.getCameraId());
                    videoSize = defaultVideoSize;
                    videoHdr2 = videoHdr;
                }
                else {
                    final Rect preferredPreviewSizeForVideo = PlatformCapability.getPreferredPreviewSizeForVideo(obj.getCameraId());
                    Rect preferredPreviewSizeFromCaptureSize = null;
                    Label_0140: {
                        if (preferredPreviewSizeForVideo.width() != 0) {
                            preferredPreviewSizeFromCaptureSize = preferredPreviewSizeForVideo;
                            if (preferredPreviewSizeForVideo.height() != 0) {
                                break Label_0140;
                            }
                        }
                        preferredPreviewSizeFromCaptureSize = PlatformDependencyResolver.getPreferredPreviewSizeFromCaptureSize(defaultVideoSize.getVideoRect());
                    }
                    previewSize = CommonPlatformDependencyResolver.getOptimalVideoPreviewRect(defaultVideoSize.getVideoRect(), preferredPreviewSizeFromCaptureSize, PlatformCapability.getSupportedPreviewSizes(obj.getCameraId()));
                    videoSize = defaultVideoSize;
                    videoHdr2 = videoHdr;
                }
            }
            else {
                Resolution defaultValue;
                if ((defaultValue = (Resolution)userSettings.get(UserSettingKey.RESOLUTION)) == null) {
                    defaultValue = Resolution.getDefaultValue(obj);
                }
                previewSize = PlatformDependencyResolver.getOptimalPreviewSize(obj.getCameraId(), obj.getType(), defaultValue.getPictureRect());
                pictureRect = defaultValue.getPictureRect();
                value = null;
                videoHdr2 = (videoSize = null);
            }
            cameraParameters.setVideoSize(videoSize);
            cameraParameters.setPreviewSize(previewSize);
            cameraParameters.setVideoStabilizer(value);
            cameraParameters.setPictureSize(pictureRect);
            cameraParameters.setVideoHdr((VideoHdr)videoHdr2);
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Load settings from user settings. mode:");
                sb.append(obj);
                sb.append(" video:");
                sb.append(cameraParameters.getVideoSize());
                sb.append(" preview:");
                sb.append(cameraParameters.getPreviewSize());
                sb.append(" vs:");
                sb.append(cameraParameters.getVideoStabilizer());
                sb.append(" picture:");
                sb.append(cameraParameters.getPictureSize());
                sb.append(" modified:true");
                CamLog.d(sb.toString());
            }
            return true;
        }
        if (sharedPreferences != null) {
            boolean b = false;
            Label_0969: {
                Label_0966: {
                    if (obj.isVideo()) {
                        final UserSettingKey video_SIZE = UserSettingKey.VIDEO_SIZE;
                        final String prefix = SharedPreferencesAccessor.createPrefix(video_SIZE.getCategory(), obj, "");
                        final UserSettingValueHolder userSettingValueHolder = new UserSettingValueHolder(null);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(prefix);
                        sb2.append(video_SIZE);
                        final String string = sharedPreferences.getString(sb2.toString(), (String)null);
                        if (string != null) {
                            userSettingValueHolder.parseValueString(string);
                        }
                        final VideoSize videoSize2 = userSettingValueHolder.get();
                        final UserSettingKey video_STABILIZER = UserSettingKey.VIDEO_STABILIZER;
                        final String prefix2 = SharedPreferencesAccessor.createPrefix(video_STABILIZER.getCategory(), obj, "");
                        final UserSettingValueHolder userSettingValueHolder2 = new UserSettingValueHolder(null);
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(prefix2);
                        sb3.append(video_STABILIZER);
                        final String string2 = sharedPreferences.getString(sb3.toString(), (String)null);
                        if (string2 != null) {
                            userSettingValueHolder2.parseValueString(string2);
                        }
                        final VideoStabilizer videoStabilizer2 = userSettingValueHolder2.get();
                        final UserSettingKey video_HDR = UserSettingKey.VIDEO_HDR;
                        final UserSettingValueHolder userSettingValueHolder3 = new UserSettingValueHolder(null);
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(prefix2);
                        sb4.append(video_HDR);
                        final String string3 = sharedPreferences.getString(sb4.toString(), (String)null);
                        if (string3 != null) {
                            userSettingValueHolder3.parseValueString(string3);
                        }
                        final VideoHdr videoHdr3 = userSettingValueHolder3.get();
                        VideoSize defaultVideoSize2;
                        if ((defaultVideoSize2 = videoSize2) == null) {
                            defaultVideoSize2 = PlatformDependencyResolver.getDefaultVideoSize(obj.getCameraId());
                        }
                        VideoStabilizer recommendedVideoStabilizerValue;
                        if ((recommendedVideoStabilizerValue = videoStabilizer2) == null) {
                            recommendedVideoStabilizerValue = VideoStabilizer.getRecommendedVideoStabilizerValue(this.getApplicationContext(), obj, defaultVideoSize2);
                        }
                        Rect previewSize2;
                        if (videoHdr3 == VideoHdr.HDR_ON) {
                            previewSize2 = PlatformCapability.getPreferredPreviewSizeForHdrVideo(obj.getCameraId());
                        }
                        else {
                            previewSize2 = PlatformDependencyResolver.getOptimalPreviewSize(obj.getCameraId(), obj.getType(), defaultVideoSize2.getVideoRect());
                        }
                        if (this.isModified(cameraParameters.getVideoSize(), defaultVideoSize2) || !recommendedVideoStabilizerValue.getValue().equals(cameraParameters.getVideoStabilizer()) || this.isModified(cameraParameters.getPreviewSize(), previewSize2)) {
                            cameraParameters.setVideoSize(defaultVideoSize2);
                            cameraParameters.setPreviewSize(previewSize2);
                            cameraParameters.setVideoStabilizer(recommendedVideoStabilizerValue.getValue());
                            cameraParameters.setPictureSize(null);
                            cameraParameters.setVideoHdr(videoHdr3);
                            break Label_0966;
                        }
                    }
                    else {
                        final UserSettingKey resolution = UserSettingKey.RESOLUTION;
                        final String prefix3 = SharedPreferencesAccessor.createPrefix(resolution.getCategory(), obj, "");
                        final UserSettingValueHolder userSettingValueHolder4 = new UserSettingValueHolder(null);
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append(prefix3);
                        sb5.append(resolution);
                        final String string4 = sharedPreferences.getString(sb5.toString(), (String)null);
                        if (string4 != null) {
                            userSettingValueHolder4.parseValueString(string4);
                        }
                        Resolution defaultValue2;
                        if ((defaultValue2 = userSettingValueHolder4.get()) == null) {
                            defaultValue2 = Resolution.getDefaultValue(obj);
                        }
                        final Rect optimalPreviewSize = PlatformDependencyResolver.getOptimalPreviewSize(obj.getCameraId(), obj.getType(), defaultValue2.getPictureRect());
                        if (this.isModified(cameraParameters.getPictureSize(), defaultValue2.getPictureRect()) || this.isModified(cameraParameters.getPreviewSize(), optimalPreviewSize)) {
                            cameraParameters.setPictureSize(defaultValue2.getPictureRect());
                            cameraParameters.setPreviewSize(optimalPreviewSize);
                            cameraParameters.setVideoStabilizer(null);
                            cameraParameters.setVideoSize(null);
                            cameraParameters.setVideoHdr(null);
                            break Label_0966;
                        }
                    }
                    b = false;
                    break Label_0969;
                }
                b = true;
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("Load settings from shared-preference. mode:");
                sb6.append(obj);
                sb6.append(" video:");
                sb6.append(cameraParameters.getVideoSize());
                sb6.append(" preview:");
                sb6.append(cameraParameters.getPreviewSize());
                sb6.append(" vs:");
                sb6.append(cameraParameters.getVideoStabilizer());
                sb6.append(" picture:");
                sb6.append(cameraParameters.getPictureSize());
                sb6.append(" modified:");
                sb6.append(b);
                CamLog.d(sb6.toString());
            }
            return b;
        }
        if (this.mCameraDeviceHandler.isVideo()) {
            final VideoSize defaultVideoSize3 = PlatformDependencyResolver.getDefaultVideoSize(obj.getCameraId());
            cameraParameters.setVideoSize(defaultVideoSize3);
            cameraParameters.setVideoStabilizer(VideoStabilizer.getRecommendedVideoStabilizerValue(this.getApplicationContext(), obj, defaultVideoSize3).getValue());
            cameraParameters.setPreviewSize(PlatformDependencyResolver.getOptimalPreviewSize(obj.getCameraId(), obj.getType(), defaultVideoSize3.getVideoRect()));
            cameraParameters.setPictureSize(null);
        }
        else {
            cameraParameters.setPictureSize(Resolution.getDefaultValue(obj).getPictureRect());
            cameraParameters.setPreviewSize(PlatformDependencyResolver.getOptimalPreviewSize(obj.getCameraId(), obj.getType(), cameraParameters.getPictureSize()));
            cameraParameters.setVideoStabilizer(null);
            cameraParameters.setVideoSize(null);
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("Load settings from camera device. mode:");
            sb7.append(obj);
            sb7.append(" video:");
            sb7.append(cameraParameters.getVideoSize());
            sb7.append(" preview:");
            sb7.append(cameraParameters.getPreviewSize());
            sb7.append(" vs:");
            sb7.append(cameraParameters.getVideoStabilizer());
            sb7.append(" picture:");
            sb7.append(cameraParameters.getPictureSize());
            sb7.append(" modified:true");
            CamLog.d(sb7.toString());
        }
        return true;
    }
    
    private void prepareCaptureImageReader(final CameraDeviceHandler.CameraSessionId cameraSessionId, final CaptureImageReaderRequest captureImageReaderRequest, final CountDownLatch countDownLatch) {
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
                }
                return;
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ImageReader may be changed to: buffNum:");
                sb.append(captureImageReaderRequest.mImageReaderBufferNum);
                sb.append(",");
                sb.append(captureImageReaderRequest.mCapturingBufferNum);
                sb.append(" size:");
                sb.append(captureImageReaderRequest.mCaptureSize.width());
                sb.append(",");
                sb.append(captureImageReaderRequest.mCaptureSize.height());
                CamLog.d(sb.toString());
            }
        }
        final boolean b = this.mCaptureImageReader != null;
        this.mCapturingBufferNum = captureImageReaderRequest.mCapturingBufferNum;
        this.mImageReaderHandler.post((Runnable)new Runnable(this, cameraSessionId, countDownLatch, captureImageReaderRequest, b) {
            final BypassCameraController this$0;
            final CameraDeviceHandler.CameraSessionId val$currentSessionId;
            final CountDownLatch val$imageReaderReadyLatch;
            final boolean val$needToFinalize;
            final CaptureImageReaderRequest val$request;
            
            @Override
            public void run() {
                this.this$0.awaitImageReaderPrepared();
                final CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(this.val$currentSessionId);
                if (this.this$0.mBypassCamera != null && openCloseStatusInfo != null && !openCloseStatusInfo.isCloseBypassCameraTaskRequested()) {
                    if (!openCloseStatusInfo.isCloseBypassCameraTaskPerformed()) {
                        if (this.val$request.isSameRequest(this.this$0.mPrevCaptureImageReaderRequest)) {
                            if (CamLog.DEBUG) {
                                CamLog.d("Same ImageReader has bean already initialized");
                            }
                            this.this$0.unlockImageReaderReadyLatch(this.val$imageReaderReadyLatch);
                            if (this.val$request.mCallback != null) {
                                this.val$request.mCallback.onInitialized();
                            }
                            return;
                        }
                        synchronized (this.this$0.mImageReaderPreparedLockObject) {
                            if (this.this$0.mImageReaderPreparedLatch == null || this.this$0.mImageReaderPreparedLatch.getCount() <= 0L) {
                                if (CamLog.DEBUG) {
                                    CamLog.d("Latch ImageReaderPrepared.");
                                }
                                this.this$0.mImageReaderPreparedLatch = new CountDownLatch(1);
                            }
                            monitorexit(this.this$0.mImageReaderPreparedLockObject);
                            this.this$0.mPrevCaptureImageReaderRequest = this.val$request;
                            if (this.val$needToFinalize) {
                                this.this$0.finalizeCaptureImageReaderInternal();
                            }
                            this.this$0.mCaptureImageReader = ImageReader.newInstance(this.val$request.mCaptureSize.width(), this.val$request.mCaptureSize.height(), 256, this.val$request.mImageReaderBufferNum);
                            this.this$0.mCaptureImageReader.setOnImageAvailableListener((ImageReader$OnImageAvailableListener)this.this$0.mSnapshotCallback, this.this$0.mImageReaderHandler);
                            final int access$4100 = this.val$request.mCapturingBufferNum;
                            final int access$4101 = this.val$request.mImageReaderBufferNum;
                            PerfLog.BYPASSCAMERA_PREPARE.begin();
                            this.this$0.requestPrepareSnapshot(access$4100 + access$4101);
                            PerfLog.BYPASSCAMERA_PREPARE.end();
                            this.this$0.unlockImageReaderReadyLatch(this.val$imageReaderReadyLatch);
                            if (this.val$request.mCallback != null) {
                                this.val$request.mCallback.onInitialized();
                            }
                            return;
                        }
                    }
                }
                if (CamLog.DEBUG) {
                    CamLog.d("Skip creating ImageReader. BypassCamera would be closed.");
                }
                this.this$0.unlockImageReaderReadyLatch(this.val$imageReaderReadyLatch);
            }
        });
    }
    
    private void releaseCloseBypassCameraWakeLock() {
        if (this.mCloseBypassCameraWakeLock == null) {
            CamLog.e("Wake lock is not created correctly.");
            return;
        }
        this.mCloseBypassCameraWakeLock.release();
    }
    
    @WorkerThread
    private void requestPrepareSnapshot(final int i) {
        try {
            if (this.mBypassCamera == null) {
                synchronized (this.mImageReaderPreparedLockObject) {
                    if (this.mImageReaderPreparedLatch != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("countDown ImageReaderPreparedLatch to ");
                        sb.append(this.mImageReaderPreparedLatch.getCount() - 1L);
                        CamLog.d(sb.toString());
                        this.mImageReaderPreparedLatch.countDown();
                    }
                    return;
                }
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("requestPrepareSnapshot() totalBufNumber:");
                sb2.append(i);
                CamLog.d(sb2.toString());
            }
            this.mBypassCamera.requestPrepareSnapshot(this.mCaptureImageReader.getSurface(), i);
            synchronized (this.mImageReaderPreparedLockObject) {
                if (this.mImageReaderPreparedLatch != null) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("countDown ImageReaderPreparedLatch to ");
                    sb3.append(this.mImageReaderPreparedLatch.getCount() - 1L);
                    CamLog.d(sb3.toString());
                    this.mImageReaderPreparedLatch.countDown();
                }
            }
        }
        finally {
            synchronized (this.mImageReaderPreparedLockObject) {
                if (this.mImageReaderPreparedLatch != null) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("countDown ImageReaderPreparedLatch to ");
                    sb4.append(this.mImageReaderPreparedLatch.getCount() - 1L);
                    CamLog.d(sb4.toString());
                    this.mImageReaderPreparedLatch.countDown();
                }
                monitorexit(this.mImageReaderPreparedLockObject);
            }
        }
    }
    
    private void setSnapshotReadyWaiting(final boolean mIsSnapshotReadyWaiting) {
        synchronized (this.mWaitForSnapshotReadyLockObject) {
            this.mIsSnapshotReadyWaiting = mIsSnapshotReadyWaiting;
            if (!this.mIsSnapshotReadyWaiting && this.mWaitForSnapshotReadyLock != null) {
                this.mWaitForSnapshotReadyLock.countDown();
            }
        }
    }
    
    private Size toAndroidUtilSize(final Rect rect) {
        return new Size(rect.width(), rect.height());
    }
    
    private void unlockImageReaderReadyLatch(final CountDownLatch countDownLatch) {
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
    
    void awaitAllSnapshotDone() {
        this.mWaitForAllSnapshotLock = this.createSavingPhotoRemainCountDownLatch();
        this.awaitAllSnapshot();
    }
    
    void closeBypassCamera(final boolean b, final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        if (this.mPreCaptureResult != null) {
            this.mPreCaptureResult.close();
            this.mPreCaptureResult = null;
        }
        this.mWaitForAllSnapshotLock = this.createSavingPhotoRemainCountDownLatch();
        final CameraDeviceHandler.CameraSessionInfo openCloseStatusInfo = CameraDeviceHandler.CameraSessionInfo.getOpenCloseStatusInfo(cameraSessionId);
        if (openCloseStatusInfo == null) {
            return;
        }
        openCloseStatusInfo.setRequested(CameraDeviceHandler.OpenCloseRequestStatus.BYPASS_CAMERA_CLOSING);
        CloseBypassCameraTask closeBypassCameraTask;
        if (this.mWaitForAllSnapshotLock != null) {
            closeBypassCameraTask = new CloseBypassCameraTask(this.mWaitForAllSnapshotLock, cameraSessionId);
        }
        else {
            closeBypassCameraTask = new CloseBypassCameraTask(cameraSessionId);
        }
        this.acquireCloseBypassCameraWakeLock();
        if (b) {
            this.mCameraDeviceHandler.postCameraDeviceThreadSync(closeBypassCameraTask);
        }
        else {
            this.mCameraDeviceHandler.postCameraDeviceThread(closeBypassCameraTask);
        }
    }
    
    void commit(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.awaitImageReaderReady();
        this.changeBypassCameraModeAccordingToCurrentSetting(cameraSessionId);
    }
    
    void commitParameters(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new SetConfigTask(cameraSessionId));
    }
    
    void dump(final StringBuilder sb) {
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("mBypassCamera:");
        sb2.append(this.mBypassCamera);
        sb2.append("\n");
        sb.append(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(makeCountDownLatchInfo("mWaitForAllSnapshotLock", this.mWaitForAllSnapshotLock));
        sb3.append("\n");
        sb.append(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(makeCountDownLatchInfo("mImageReaderReadyLatch", this.mImageReaderReadyLatch));
        sb4.append("\n");
        sb.append(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("mRemainRequestQueue:");
        sb5.append(this.mRemainRequestQueue);
        sb5.append("\n");
        sb.append(sb5.toString());
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("mSavingPhotoRequestQueue:");
        sb6.append(this.mSavingPhotoRequestQueue);
        sb6.append("\n");
        sb.append(sb6.toString());
        final StringBuilder sb7 = new StringBuilder();
        sb7.append("mIsSnapshotReadyWaiting:");
        sb7.append(this.mIsSnapshotReadyWaiting);
        sb7.append("\n");
        sb.append(sb7.toString());
        final StringBuilder sb8 = new StringBuilder();
        sb8.append("mCapturingBufferNum:");
        sb8.append(this.mCapturingBufferNum);
        sb8.append("\n");
        sb.append(sb8.toString());
        final StringBuilder sb9 = new StringBuilder();
        sb9.append("mPreCaptureResult:");
        sb9.append(this.mPreCaptureResult);
        sb9.append("\n");
        sb.append(sb9.toString());
    }
    
    void enqueueSavingPhotoRequest(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        synchronized (this.mSavingPhotoRequestQueue) {
            this.mSavingPhotoRequestQueue.add(photoSavingRequestBuilder);
            if (CamLog.DEBUG) {
                CamLog.d(this.dumpRequestQueueStatus());
            }
        }
    }
    
    RequestFactory.PhotoSavingRequestBuilder getAndClearPreCaptureResult() {
        final RequestFactory.PhotoSavingRequestBuilder mPreCaptureResult = this.mPreCaptureResult;
        this.mPreCaptureResult = null;
        return mPreCaptureResult;
    }
    
    @WorkerThread
    BypassCamera getBypassCameraInstance() {
        return this.mBypassCamera;
    }
    
    int getRemainPrevSavingRequestCount() {
        synchronized (this.mSavingPhotoRequestQueue) {
            int n = this.mSavingPhotoRequestQueue.size() + this.mRemainRequestQueue.size();
            for (final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder : this.mSavingPhotoRequestQueue) {
                if (photoSavingRequestBuilder != null && photoSavingRequestBuilder.mCommonStatus.takenByFastCapture) {
                    --n;
                }
            }
            for (final TakenStatusCommon takenStatusCommon : this.mRemainRequestQueue) {
                if (takenStatusCommon != null && takenStatusCommon.takenByFastCapture) {
                    --n;
                }
            }
            return n;
        }
    }
    
    int getRemainSavingPhotoRequestCount() {
        synchronized (this.mSavingPhotoRequestQueue) {
            if (CamLog.DEBUG) {
                CamLog.d(this.dumpRequestQueueStatus());
            }
            final int size = this.mSavingPhotoRequestQueue.size();
            final int size2 = this.mRemainRequestQueue.size();
            monitorexit(this.mSavingPhotoRequestQueue);
            return size + size2;
        }
    }
    
    boolean isBypassCameraNextShotAvailable() {
        return this.mCapturingBufferNum - this.getRemainSavingPhotoRequestCount() > 0;
    }
    
    boolean isSnapshotRunning() {
        synchronized (this.mSavingPhotoRequestQueue) {
            final boolean debug = CamLog.DEBUG;
            boolean b = false;
            if (debug) {
                CamLog.d(this.dumpRequestQueueStatus());
            }
            if (this.mSavingPhotoRequestQueue.size() > 0) {
                b = true;
            }
            return b;
        }
    }
    
    CameraDeviceHandler.CameraSessionId openBypassCamera(final SharedPreferences sharedPreferences, final UserSettings userSettings, final FastCapture fastCapture, final CapturingMode capturingMode) {
        if (PlatformCapability.isPrepared() && !PlatformCapability.hasDeviceError()) {
            final CameraDeviceHandler.CameraSessionId cameraSessionId = new CameraDeviceHandler.CameraSessionId();
            final CameraDeviceHandler.CameraSessionInfo cameraSessionInfo = new CameraDeviceHandler.CameraSessionInfo(capturingMode.getCameraId());
            cameraSessionInfo.setRequested(CameraDeviceHandler.OpenCloseRequestStatus.BYPASS_CAMERA_OPENING);
            CameraDeviceHandler.CameraSessionInfo.addOpenCloseStatusInfo(cameraSessionId, cameraSessionInfo);
            PlatformCapability.getCameraInfo(capturingMode.getCameraId(), cameraSessionInfo.getCameraInfo());
            this.mCameraDeviceHandler.postCameraDeviceThread(new OpenBypassCameraTask(sharedPreferences, userSettings, cameraSessionId, fastCapture, capturingMode));
            return cameraSessionId;
        }
        CamLog.e("BypassCameraController.openBypassCamera() is rejected because PlatformCapability is not ready.");
        return null;
    }
    
    RequestFactory.PhotoSavingRequestBuilder peekLastSavingPhotoRequest() {
        synchronized (this.mSavingPhotoRequestQueue) {
            final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder = this.mSavingPhotoRequestQueue.peekLast();
            if (CamLog.DEBUG) {
                CamLog.d(this.dumpRequestQueueStatus());
            }
            return photoSavingRequestBuilder;
        }
    }
    
    void prepareCaptureImageReader(final CameraDeviceHandler.CameraSessionId obj, final CameraDeviceHandler.ImageReaderInitializedCallback imageReaderInitializedCallback) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(obj);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("This session has been closed, so this request was refused. sessionId:");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            return;
        }
        Rect rect;
        if (this.mCameraDeviceHandler.isVideo()) {
            rect = parameters.getVideoSize().getVideoRect();
        }
        else {
            rect = parameters.getPictureSize();
        }
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestPrepareCaptureImageReaderTask(obj, 1, Math.max(2, PredictiveCapture.AUTO.getCaptureNum() * 2), rect, imageReaderInitializedCallback));
    }
    
    void requestApplyBypassCameraMode() {
        this.mIsApplyBypassCameraModeRequired = true;
    }
    
    void requestFinishBurstShot(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestFinishBurstShotTask(cameraSessionId));
    }
    
    void requestPrepareBurstShot(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestPrepareBurstShotTask(cameraSessionId));
    }
    
    void requestSnapshot(final CameraDeviceHandler.CameraSessionId cameraSessionId, final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, int min) {
        final CameraParameters parameters = this.mCameraDeviceHandler.getParameters(cameraSessionId);
        if (parameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is not available.");
            }
            return;
        }
        CapturePerformanceLogger.create(photoSavingRequestBuilder).startSnapshot = SystemClock.uptimeMillis();
        Object mSavingPhotoRequestQueue = this.mSavingPhotoRequestQueue;
        synchronized (mSavingPhotoRequestQueue) {
            if (this.mBypassCamera != null) {
                min = Math.min(this.mCapturingBufferNum - this.getRemainSavingPhotoRequestCount(), min);
                PerfLog.BYPASSCAMERA_REQUEST_SNAPSHOT.transit();
                this.mCameraDeviceHandler.postCameraDeviceThread(new RequestSnapshotTask(cameraSessionId, photoSavingRequestBuilder, min));
                this.enqueueSavingPhotoRequest(photoSavingRequestBuilder);
            }
            else {
                CamLog.w("requestSnapshot() mBypassCamera == null");
            }
            monitorexit(mSavingPhotoRequestQueue);
            mSavingPhotoRequestQueue = new EachCameraStatusPublisher(this.getApplicationContext(), parameters.getCameraId());
            DeviceStatus.Value value;
            if (photoSavingRequestBuilder.mCommonStatus.savedFileType == SavingTaskManager.SavedFileType.PHOTO_DURING_REC) {
                value = DeviceStatus.Value.PICTURE_TAKING_DURING_VIDEO_RECORDING;
            }
            else {
                value = DeviceStatus.Value.PICTURE_TAKING;
            }
            ((CameraStatusPublisher<DeviceStatus>)mSavingPhotoRequestQueue).put(new DeviceStatus(value)).publish();
        }
    }
    
    void requestSnapshotFree(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestSnapshotFreeTask(cameraSessionId));
    }
    
    void requestSnapshotReady(final CameraDeviceHandler.CameraSessionId cameraSessionId) {
        this.mCameraDeviceHandler.postCameraDeviceThread(new RequestSnapshotReadyTask(cameraSessionId));
    }
    
    void setPreCaptureResult(final RequestFactory.PhotoSavingRequestBuilder mPreCaptureResult) {
        this.mPreCaptureResult = mPreCaptureResult;
    }
    
    interface BypassCameraControllerCallback
    {
        void onCameraClosed();
        
        void onPrepareBurstDone(final boolean p0);
        
        void onShutterDone(final int p0, final int p1, final boolean p2);
        
        void onSnapshotDone(final RequestFactory.PhotoSavingRequestBuilder p0);
        
        void onSnapshotReadyDone(final ExecutorService p0, final boolean p1, final boolean p2, final boolean p3, final BypassCamera.DisplayFlashColor p4);
    }
    
    private static class CaptureImageReaderRequest
    {
        private CameraDeviceHandler.ImageReaderInitializedCallback mCallback;
        private Rect mCaptureSize;
        private int mCapturingBufferNum;
        private int mImageReaderBufferNum;
        
        private boolean isSameRequest(final CaptureImageReaderRequest captureImageReaderRequest) {
            return captureImageReaderRequest != null && this.mImageReaderBufferNum == captureImageReaderRequest.mImageReaderBufferNum && this.mCapturingBufferNum == captureImageReaderRequest.mCapturingBufferNum && this.mCaptureSize != null && captureImageReaderRequest.mCaptureSize != null && this.mCaptureSize.equals((Object)captureImageReaderRequest.mCaptureSize);
        }
    }
    
    @WorkerThread
    private class ChangeBypassCameraModeTask extends CameraDeviceAccessTask
    {
        final BypassCameraController this$0;
        
        private ChangeBypassCameraModeTask(final BypassCameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
        }
        
        private BypassCamera.SuperSlowRecordingParameters createSuperSlowRecordingParameters() {
            final CameraParameters parameters = this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
            return new BypassCamera.SuperSlowRecordingParameters((int)PlatformCapability.getSuperSlowFrameRate(parameters.getCameraId(), parameters.getVideoSize()), (int)PlatformCapability.getSuperSlowFrameNum(parameters.getCameraId(), parameters.getVideoSize()));
        }
        
        private BypassCamera.SuperSlowMode getSuperSlowVideoMode(final SlowMotion obj) {
            if (obj == SlowMotion.SUPER_SLOW_MOTION) {
                return BypassCamera.SuperSlowMode.SUPER_SLOW_MOTION;
            }
            if (obj == SlowMotion.SUPER_SLOW_SHOT) {
                return BypassCamera.SuperSlowMode.SUPER_SLOW_SHOT;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("value is not super slow, value = ");
            sb.append(obj);
            throw new IllegalArgumentException(sb.toString());
        }
        
        public void doCameraDeviceAccess() {
            final CameraParameters parameters = this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
            final SlowMotion slowMotion = parameters.getSlowMotion();
            if (!this.this$0.mCameraDeviceHandler.isVideo()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("changeToPhotoMode() preview:");
                    sb.append(parameters.getPreviewSize());
                    sb.append(" picture:");
                    sb.append(parameters.getPictureSize());
                    sb.append(" captureNum:");
                    sb.append(PredictiveCapture.AUTO.getCaptureNum());
                    CamLog.d(sb.toString());
                }
                try {
                    this.this$0.mBypassCamera.changeToPhotoMode(BypassCamera.PhotoMode.NORMAL, this.this$0.toAndroidUtilSize(parameters.getPreviewSize()), this.this$0.toAndroidUtilSize(parameters.getPictureSize()), PredictiveCapture.AUTO.getCaptureNum());
                }
                catch (final BypassCameraTimeoutException ex) {
                    this.this$0.handleTimeout(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
                }
                if (CamLog.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("setConfig() climax:");
                    sb2.append(this.this$0.mBypassCameraParameters.get("climax-recognition"));
                    CamLog.d(sb2.toString());
                }
                this.this$0.mBypassCamera.setConfig(this.this$0.mBypassCameraParameters);
            }
            else if (slowMotion != SlowMotion.SUPER_SLOW_MOTION && slowMotion != SlowMotion.SUPER_SLOW_SHOT) {
                final VideoSize videoSize = parameters.getVideoSize();
                final VideoHdr videoHdr = parameters.getVideoHdr();
                final BypassCamera.VideoMode access$2700 = this.this$0.getVideoMode(parameters.getCameraId(), parameters.getVideoStabilizer(), parameters.getVideoHdr());
                final int videoFrameRate = RecordingProfile.getVideoFrameRate(videoSize, videoHdr);
                if (CamLog.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("changeToVideoMode() mode:");
                    sb3.append(access$2700);
                    sb3.append(" preview:");
                    sb3.append(parameters.getPreviewSize());
                    sb3.append(" video:");
                    sb3.append(videoSize.getVideoRect());
                    sb3.append(" framerate:");
                    sb3.append(videoFrameRate);
                    CamLog.d(sb3.toString());
                }
                try {
                    this.this$0.mBypassCamera.changeToVideoMode(access$2700, this.this$0.toAndroidUtilSize(parameters.getPreviewSize()), this.this$0.toAndroidUtilSize(videoSize.getVideoRect()), videoFrameRate);
                }
                catch (final BypassCameraTimeoutException ex2) {
                    this.this$0.handleTimeout(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
                }
            }
            else {
                final BypassCamera.SuperSlowMode superSlowVideoMode = this.getSuperSlowVideoMode(slowMotion);
                final VideoSize videoSize2 = parameters.getVideoSize();
                final BypassCamera.SuperSlowRecordingParameters superSlowRecordingParameters = this.createSuperSlowRecordingParameters();
                final int videoFrameRate2 = RecordingProfile.getVideoFrameRate(videoSize2, VideoHdr.HDR_OFF);
                if (CamLog.DEBUG) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("changeToSuperSlowMode() mode:");
                    sb4.append(superSlowVideoMode);
                    sb4.append(" preview:");
                    sb4.append(parameters.getPreviewSize());
                    sb4.append(" video:");
                    sb4.append(videoSize2.getVideoRect());
                    sb4.append(" framerate:");
                    sb4.append(videoFrameRate2);
                    sb4.append(" superSlowFps:");
                    sb4.append(superSlowRecordingParameters.fps);
                    sb4.append(" superSlowFrameNum:");
                    sb4.append(superSlowRecordingParameters.frameNum);
                    CamLog.d(sb4.toString());
                }
                try {
                    this.this$0.mBypassCamera.changeToSuperSlowMode(superSlowVideoMode, this.this$0.toAndroidUtilSize(parameters.getPreviewSize()), this.this$0.toAndroidUtilSize(videoSize2.getVideoRect()), videoFrameRate2, superSlowRecordingParameters);
                }
                catch (final BypassCameraTimeoutException ex3) {
                    this.this$0.handleTimeout(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
                }
            }
        }
        
        public boolean verifyCameraDeviceStatus() {
            return ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }
    }
    
    @WorkerThread
    private class CloseBypassCameraTask extends CameraDeviceAccessTask
    {
        private static final long TIMEOUT_WAIT_FOR_ALL_SNAPSHOT_DONE_MILLIS = 30000L;
        private static final long TIMEOUT_WAIT_FOR_EACH_SNAPSHOT_DONE_MILLIS = 15000L;
        private static final long TIMEOUT_WAIT_SNAPSHOT_READY_DONE_MILLIS = 5000L;
        private final CountDownLatch mWaitForAllSnapshotDoneLock;
        final BypassCameraController this$0;
        
        private CloseBypassCameraTask(final BypassCameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mWaitForAllSnapshotDoneLock = null;
        }
        
        private CloseBypassCameraTask(final BypassCameraController this$0, final CountDownLatch mWaitForAllSnapshotDoneLock, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mWaitForAllSnapshotDoneLock = mWaitForAllSnapshotDoneLock;
        }
        
        @WorkerThread
        private void releaseBypassCamera() {
            if (this.this$0.mBypassCamera != null) {
                try {
                    this.this$0.mBypassCameraRequestExecutor.shutdown();
                    final boolean awaitTermination = this.this$0.mBypassCameraRequestExecutor.awaitTermination(2000L, TimeUnit.MILLISECONDS);
                    this.this$0.mBypassCameraRequestExecutor = null;
                    if (!awaitTermination) {
                        CamLog.e("Time-out occurs to release BypassCamera.");
                    }
                }
                catch (final InterruptedException ex) {
                    CamLog.e("Time-out thread is interrupted.");
                }
                this.this$0.mBypassCamera.close();
                CamLog.d("BypassCamera is closed.");
            }
            this.this$0.mBypassCamera = null;
            this.this$0.mBypassCameraParameters = null;
            this.this$0.mSnapshotCallback = null;
            this.this$0.mWaitForAllSnapshotLock = null;
            this.this$0.mWaitForSnapshotReadyLock = null;
            this.this$0.mSavingPhotoRequestQueue.clear();
            this.this$0.mRemainRequestQueue.clear();
            this.this$0.mIsSnapshotReady = false;
            this.this$0.mCapturingBufferNum = -1;
            this.this$0.mCallback.onCameraClosed();
        }
        
        public void doCameraDeviceAccess() {
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().setPerformed(OpenClosePerformStatus.BYPASS_CAMERA_CLOSED);
            this.this$0.awaitImageReaderReady();
            this.this$0.createSnapshotReadyCountDownLatch();
            try {
                if (this.this$0.mWaitForSnapshotReadyLock != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Waiting to complete snapshot ready:");
                    sb.append(this.this$0.mWaitForSnapshotReadyLock.getCount());
                    CamLog.d(sb.toString());
                    if (this.this$0.mWaitForSnapshotReadyLock.await(5000L, TimeUnit.MILLISECONDS)) {
                        CamLog.d("Snapshot ready done is completed");
                    }
                    else {
                        CamLog.e("Timeout of waiting snapshot ready done.");
                    }
                }
                if (this.mWaitForAllSnapshotDoneLock != null) {
                    final long count = this.mWaitForAllSnapshotDoneLock.getCount();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Waiting to complete all snapshots:");
                    sb2.append(count);
                    CamLog.d(sb2.toString());
                    if (this.mWaitForAllSnapshotDoneLock.await(Math.max(count * 15000L, 30000L), TimeUnit.MILLISECONDS)) {
                        CamLog.d("All snapshots done are completed");
                    }
                    else {
                        CamLog.e("Timeout of waiting all snapshots done.");
                    }
                }
            }
            catch (final InterruptedException ex) {
                CamLog.e("Intercept waiting request done.");
            }
            this.this$0.finalizeCaptureImageReader();
            this.this$0.mPrevCaptureImageReaderRequest = null;
            if (this.this$0.mIsSnapshotReady) {
                this.this$0.mBypassCamera.requestSnapshotFree();
                this.this$0.mIsSnapshotReady = false;
            }
            this.releaseBypassCamera();
        }
        
        public void postCameraDeviceAccess() {
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).removeOpenCloseStatusInfo();
            this.mLatch.countDown();
            this.this$0.releaseCloseBypassCameraWakeLock();
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return (((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() || ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused()) && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }
    }
    
    private static class FinishBurstCallbackImpl implements FinishBurstCallback
    {
        @Override
        public void onFinishBurstDone() {
        }
    }
    
    private class OnImageReaderDetachedListenerImpl implements OnImageReaderDetachedListener
    {
        final BypassCameraController this$0;
        
        private OnImageReaderDetachedListenerImpl(final BypassCameraController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onDetached(final ImageReader imageReader) {
            synchronized (this.this$0.mSavingPhotoRequestQueue) {
                if (this.this$0.mCaptureImageReader != imageReader) {
                    CamLog.e("This callback is not for current ImageReader.");
                    return;
                }
                this.this$0.mRemainRequestQueue.poll();
                if (CamLog.DEBUG) {
                    CamLog.d(this.this$0.dumpRequestQueueStatus());
                }
                if (this.this$0.mWaitForAllSnapshotLock != null) {
                    this.this$0.mWaitForAllSnapshotLock.countDown();
                }
                if (PerfLog.IS_ENABLE && this.this$0.getRemainSavingPhotoRequestCount() == 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("SHUTTER_FPS_MONITOR:");
                    sb.append(this.this$0.mShutterFpsMonitor.dump());
                    CamLog.d(sb.toString());
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("IMAGE_FPS_MONITOR:");
                    sb2.append(this.this$0.mImageFpsMonitor.dump());
                    CamLog.d(sb2.toString());
                    this.this$0.mShutterFpsMonitor.reset();
                    this.this$0.mImageFpsMonitor.reset();
                }
            }
        }
    }
    
    @WorkerThread
    private class OpenBypassCameraTask extends CameraDeviceAccessTask
    {
        private final CapturingMode mMode;
        private final SharedPreferences mPreferences;
        private final FastCapture mRequestFastCapture;
        private final UserSettings mUserSettings;
        final BypassCameraController this$0;
        
        private OpenBypassCameraTask(final BypassCameraController this$0, final SharedPreferences mPreferences, final UserSettings mUserSettings, final CameraSessionId cameraSessionId, final FastCapture mRequestFastCapture, final CapturingMode mMode) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mPreferences = mPreferences;
            this.mUserSettings = mUserSettings;
            this.mRequestFastCapture = mRequestFastCapture;
            this.mMode = mMode;
            if (mRequestFastCapture != FastCapture.LAUNCH_AND_CAPTURE) {
                this$0.preloadSettings(this.mPreferences, this.mUserSettings, this.mMode, this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId()));
            }
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).setPerformancefLog(PerfLog.OPEN_BYPASS_CAMERA_TASK);
        }
        
        public void doCameraDeviceAccess() {
            ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().setPerformed(OpenClosePerformStatus.BYPASS_CAMERA_OPENED);
            final CameraParameters parameters = this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
            if (this.mRequestFastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
                this.this$0.preloadSettings(this.mPreferences, this.mUserSettings, this.mMode, parameters);
            }
            BypassCamera.Facing obj = null;
            switch (BypassCameraController$3.$SwitchMap$com$sonyericsson$android$camera$device$CameraInfo$CameraId[parameters.getCameraId().ordinal()]) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("OpenBypassCameraTask:[preview] [UnExpected camera Id=");
                    sb.append(parameters.getCameraId());
                    sb.append("]");
                    throw new IllegalStateException(sb.toString());
                }
                case 2: {
                    obj = BypassCamera.Facing.FRONT;
                    break;
                }
                case 1: {
                    obj = BypassCamera.Facing.BACK;
                    break;
                }
            }
            try {
                this.this$0.mBypassCameraRequestExecutor = ThreadUtil.buildExecutor("BypassCamera");
                if (CamLog.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Open BypassCamera. facing:");
                    sb2.append(obj);
                    CamLog.d(sb2.toString());
                }
                this.this$0.mBypassCamera = BypassCamera.open(obj, this.this$0.mBypassCameraRequestExecutor);
                this.this$0.mBypassCameraParameters = new BypassCameraParameters();
                this.this$0.mSnapshotCallback = new SnapshotCallbackImpl();
                this.this$0.mBypassCamera.setPhotoCallbacks((BypassCamera.SnapshotReadyCallback)new SnapshotReadyCallbackImpl(), (BypassCamera.SnapshotCallback)this.this$0.mSnapshotCallback, (BypassCamera.SnapshotFreeCallback)new SnapshotFreeCallbackImpl());
                this.this$0.mBypassCamera.setBurstCallbacks((BypassCamera.PrepareBurstCallback)new PrepareBurstCallbackImpl(), (BypassCamera.FinishBurstCallback)new FinishBurstCallbackImpl());
                Label_0677: {
                    if (this.mMode.isVideo()) {
                        final VideoSize videoSize = parameters.getVideoSize();
                        if (CamLog.DEBUG) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("changeToVideoMode() preview:");
                            sb3.append(parameters.getPreviewSize());
                            sb3.append(" video:");
                            sb3.append(videoSize);
                            sb3.append(" stabilizer:");
                            sb3.append(parameters.getVideoStabilizer());
                            sb3.append(" hdr:");
                            sb3.append(parameters.getVideoHdr());
                            CamLog.d(sb3.toString());
                        }
                        try {
                            this.this$0.mBypassCamera.changeToVideoMode(this.this$0.getVideoMode(parameters.getCameraId(), parameters.getVideoStabilizer(), parameters.getVideoHdr()), this.this$0.toAndroidUtilSize(parameters.getPreviewSize()), this.this$0.toAndroidUtilSize(videoSize.getVideoRect()), RecordingProfile.getVideoFrameRate(videoSize, parameters.getVideoHdr()));
                            break Label_0677;
                        }
                        catch (final BypassCameraTimeoutException ex) {
                            this.this$0.handleTimeout(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
                            this.this$0.mCameraDeviceHandler.changePreProcessStateTo(PreProcessState.NOT_STARTED);
                            return;
                        }
                    }
                    if (CamLog.DEBUG) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("changeToPhotoMode() preview:");
                        sb4.append(parameters.getPreviewSize());
                        sb4.append(" picture:");
                        sb4.append(parameters.getPictureSize());
                        CamLog.d(sb4.toString());
                    }
                    try {
                        this.this$0.mBypassCamera.changeToPhotoMode(BypassCamera.PhotoMode.NORMAL, new Size(parameters.getPreviewSize().width(), parameters.getPreviewSize().height()), new Size(parameters.getPictureSize().width(), parameters.getPictureSize().height()), PredictiveCapture.AUTO.getCaptureNum());
                        this.this$0.mCameraDeviceHandler.changePreProcessStateTo(PreProcessState.NOT_STARTED);
                        final Context access$1300 = this.this$0.getApplicationContext();
                        boolean b;
                        if (access$1300 != null && this.mRequestFastCapture == FastCapture.LAUNCH_AND_CAPTURE && access$1300.checkSelfPermission("android.permission.CAMERA") != 0) {
                            CamLog.i("Camera permission is not granted, so pre-capture is refused.");
                            b = false;
                        }
                        else {
                            b = true;
                        }
                        boolean b2 = false;
                        switch (BypassCameraController$3.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$FastCapture[this.mRequestFastCapture.ordinal()]) {
                            default: {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("OpenBypassCameraTask():[FastCapture=");
                                sb5.append(this.mRequestFastCapture);
                                sb5.append("]");
                                throw new IllegalStateException(sb5.toString());
                            }
                            case 2: {
                                b2 = true;
                                break;
                            }
                            case 1: {
                                b2 = false;
                                break;
                            }
                        }
                        if (b2 && b) {
                            PerfLog.FAST_PRE_SCAN.transit();
                            final Rect pictureSize = parameters.getPictureSize();
                            this.this$0.mCameraDeviceHandler.postCameraDeviceThread(new RequestPrepareCaptureImageReaderTask(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), 1, 2, new Rect(0, 0, pictureSize.width(), pictureSize.height()), (ImageReaderInitializedCallback)null));
                            this.this$0.requestSnapshotReady(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
                            this.this$0.mCameraDeviceHandler.changePreProcessStateTo(PreProcessState.PRE_SCAN_STARTED);
                        }
                        if (access$1300 != null) {
                            ((CameraStatusPublisher<DeviceStatus>)new EachCameraStatusPublisher(access$1300, parameters.getCameraId())).put(new DeviceStatus(DeviceStatus.Value.POWER_ON)).publish();
                            ((CameraStatusPublisher<BuiltInCameraIds>)new GlobalCameraStatusPublisher(access$1300)).put(new BuiltInCameraIds(new CameraInfo.CameraId[] { parameters.getCameraId() })).publish();
                        }
                    }
                    catch (final BypassCameraTimeoutException ex2) {
                        this.this$0.handleTimeout(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId());
                        this.this$0.mCameraDeviceHandler.changePreProcessStateTo(PreProcessState.NOT_STARTED);
                    }
                }
            }
            catch (final Exception obj2) {
                this.this$0.mBypassCameraRequestExecutor.shutdown();
                this.this$0.mBypassCameraRequestExecutor = null;
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("Failed to open BypassCamera. ");
                sb6.append(obj2);
                CamLog.e(sb6.toString());
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseBypassCameraTaskRequested();
        }
    }
    
    private class PrepareBurstCallbackImpl implements PrepareBurstCallback
    {
        final BypassCameraController this$0;
        
        private PrepareBurstCallbackImpl(final BypassCameraController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onPrepareBurstDone(final boolean b) {
            this.this$0.mCallback.onPrepareBurstDone(b);
        }
    }
    
    @WorkerThread
    private class RequestFinishBurstShotTask extends CameraDeviceAccessTask
    {
        final BypassCameraController this$0;
        
        private RequestFinishBurstShotTask(final BypassCameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
        }
        
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                CamLog.d("requestFinishBurstShot()");
            }
            this.this$0.mBypassCamera.requestFinishBurstShot();
            final Context access$1300 = this.this$0.getApplicationContext();
            if (access$1300 != null) {
                ((CameraStatusPublisher<BurstShooting>)new EachCameraStatusPublisher(access$1300, this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId()).getCameraId())).put(new BurstShooting(BurstShooting.Value.OFF)).publish();
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }
    }
    
    @WorkerThread
    private class RequestPrepareBurstShotTask extends CameraDeviceAccessTask
    {
        final BypassCameraController this$0;
        
        private RequestPrepareBurstShotTask(final BypassCameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
        }
        
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                CamLog.d("requestPrepareBurstShot()");
            }
            this.this$0.mBypassCamera.requestPrepareBurstShot();
            final Context access$1300 = this.this$0.getApplicationContext();
            if (access$1300 != null) {
                ((CameraStatusPublisher<BurstShooting>)new EachCameraStatusPublisher(access$1300, this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId()).getCameraId())).put(new BurstShooting(BurstShooting.Value.ON)).publish();
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }
    }
    
    @WorkerThread
    private class RequestPrepareCaptureImageReaderTask extends CameraDeviceAccessTask
    {
        private final CaptureImageReaderRequest mRequest;
        final BypassCameraController this$0;
        
        private RequestPrepareCaptureImageReaderTask(final BypassCameraController this$0, final CameraSessionId cameraSessionId, final int i, final int j, final Rect rect, final ImageReaderInitializedCallback imageReaderInitializedCallback) {
            this.this$0 = this$0;
            super(cameraSessionId);
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked buffNum:");
                sb.append(i);
                sb.append(",");
                sb.append(j);
                sb.append(" size:");
                sb.append(rect.width());
                sb.append(",");
                sb.append(rect.height());
                CamLog.d(sb.toString());
            }
            (this.mRequest = new CaptureImageReaderRequest()).mImageReaderBufferNum = i;
            this.mRequest.mCapturingBufferNum = j;
            this.mRequest.mCaptureSize = rect;
            this.mRequest.mCallback = imageReaderInitializedCallback;
        }
        
        public void doCameraDeviceAccess() {
            synchronized (this.this$0.mImageReaderReadyLockObject) {
                this.this$0.mImageReaderReadyLatch = new CountDownLatch(1);
                final CountDownLatch access$4500 = this.this$0.mImageReaderReadyLatch;
                monitorexit(this.this$0.mImageReaderReadyLockObject);
                this.this$0.prepareCaptureImageReader(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId(), this.mRequest, access$4500);
            }
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused();
        }
    }
    
    @WorkerThread
    private class RequestSnapshotFreeTask extends CameraDeviceAccessTask
    {
        final BypassCameraController this$0;
        
        private RequestSnapshotFreeTask(final BypassCameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
        }
        
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                CamLog.d("requestSnapshotFree()");
            }
            this.this$0.mIsSnapshotReady = false;
            this.this$0.mBypassCamera.requestSnapshotFree();
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }
    }
    
    @WorkerThread
    private class RequestSnapshotReadyTask extends CameraDeviceAccessTask
    {
        final BypassCameraController this$0;
        
        private RequestSnapshotReadyTask(final BypassCameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
        }
        
        public void doCameraDeviceAccess() {
            this.this$0.setSnapshotReadyWaiting(true);
            if (CamLog.DEBUG) {
                CamLog.d("requestSnapshotReady()");
            }
            this.this$0.mIsSnapshotReady = true;
            this.this$0.mBypassCamera.requestSnapshotReady();
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }
    }
    
    @WorkerThread
    private class RequestSnapshotTask extends CameraDeviceAccessTask
    {
        private final RequestFactory.PhotoSavingRequestBuilder mBuilder;
        private final int mCaptureNum;
        final BypassCameraController this$0;
        
        private RequestSnapshotTask(final BypassCameraController this$0, final CameraSessionId cameraSessionId, final RequestFactory.PhotoSavingRequestBuilder mBuilder, final int mCaptureNum) {
            this.this$0 = this$0;
            super(cameraSessionId);
            this.mBuilder = mBuilder;
            this.mCaptureNum = mCaptureNum;
        }
        
        public void doCameraDeviceAccess() {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("requestSnapshot()  captureNum:");
                sb.append(this.mCaptureNum);
                sb.append(" dateTaken:");
                sb.append(((RequestFactory.RequestBuilder)this.mBuilder).getDateTaken());
                sb.append(" filePath:");
                sb.append(((RequestFactory.RequestBuilder)this.mBuilder).getFilePath());
                sb.append(" extraOutput:");
                sb.append(((RequestFactory.RequestBuilder)this.mBuilder).getExtraOutput());
                sb.append(" requestId");
                sb.append(((RequestFactory.RequestBuilder)this.mBuilder).getRequestId());
                CamLog.d(sb.toString());
            }
            this.this$0.mBypassCamera.requestSnapshot(BypassCameraSnapshotInfoFactory.create(this.mBuilder, this.mCaptureNum));
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }
    }
    
    @WorkerThread
    private class SetConfigTask extends CameraDeviceAccessTask
    {
        final BypassCameraController this$0;
        
        private SetConfigTask(final BypassCameraController this$0, final CameraSessionId cameraSessionId) {
            this.this$0 = this$0;
            super(cameraSessionId);
        }
        
        public void doCameraDeviceAccess() {
            this.this$0.mBypassCameraParameters.set("climax-recognition", this.this$0.mCameraDeviceHandler.getParameters(((CameraDeviceHandler.CameraDeviceAccessTask)this).getSessionId()).getPredictiveCapture());
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("setConfig() climax:");
                sb.append(this.this$0.mBypassCameraParameters.get("climax-recognition"));
                CamLog.d(sb.toString());
            }
            this.this$0.mBypassCamera.setConfig(this.this$0.mBypassCameraParameters);
        }
        
        @Override
        protected boolean verifyCameraDeviceStatus() {
            return ((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isOpenBypassCameraTaskPerformed() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isErrorCaused() && !((CameraDeviceHandler.CameraDeviceAccessTask)this).getOpenCloseStatusInfo().isCloseBypassCameraTaskPerformed();
        }
    }
    
    private class SnapshotCallbackImpl implements SnapshotCallback, ImageReader$OnImageAvailableListener
    {
        final BypassCameraController this$0;
        
        private SnapshotCallbackImpl(final BypassCameraController this$0) {
            this.this$0 = this$0;
        }
        
        public void onImageAvailable(final ImageReader imageReader) {
            PerfLog.BYPASSCAMERA_ON_IMAGE_AVAILABLE.transit();
            if (PerfLog.IS_ENABLE) {
                this.this$0.mImageFpsMonitor.addSampleMillis(System.currentTimeMillis());
            }
            if (CamLog.DEBUG) {
                boolean b = true;
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked imageReader:");
                if (imageReader == null) {
                    b = false;
                }
                sb.append(b);
                sb.append("pre-process:");
                sb.append(this.this$0.mCameraDeviceHandler.getPreProcessState());
                CamLog.d(sb.toString());
            }
            if (imageReader != null) {
                final RequestFactory.PhotoSavingRequestBuilder access$1800 = this.this$0.dequeueSavingPhotoRequestAndAttachImageReader(imageReader);
                if (this.this$0.mCameraDeviceHandler.getPreProcessState() != CameraDeviceHandler.PreProcessState.PRE_CAPTURE_RELEASED) {
                    this.this$0.mCallback.onSnapshotDone(access$1800);
                }
                else {
                    access$1800.close();
                }
            }
        }
        
        @Override
        public void onShutterDone(final int n, final int n2, final boolean b) {
            PerfLog.BYPASSCAMERA_ON_SHUTTER_DONE.transit();
            if (PerfLog.IS_ENABLE) {
                this.this$0.mShutterFpsMonitor.addSampleMillis(System.currentTimeMillis());
            }
            LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.SHOT_TO_SHOT_DELAY);
            LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.SHOT_TO_SHOT_DELAY);
            this.this$0.mCallback.onShutterDone(n, n2, b);
        }
        
        @Override
        public void onSnapshotDone(final int n) {
            PerfLog.BYPASSCAMERA_ON_SNAPSHOT_DONE.transit();
        }
    }
    
    private static class SnapshotFreeCallbackImpl implements SnapshotFreeCallback
    {
        @Override
        public void onSnapshotFreeDone() {
        }
    }
    
    private class SnapshotReadyCallbackImpl implements SnapshotReadyCallback
    {
        final BypassCameraController this$0;
        
        private SnapshotReadyCallbackImpl(final BypassCameraController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onSnapshotReadyDone(final boolean b, final boolean b2, final boolean b3, final DisplayFlashColor displayFlashColor) {
            this.this$0.setSnapshotReadyWaiting(false);
            this.this$0.awaitImageReaderReady();
            this.this$0.mCallback.onSnapshotReadyDone(this.this$0.mBypassCameraRequestExecutor, b, b2, b3, displayFlashColor);
        }
    }
}
