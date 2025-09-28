package com.sonyericsson.android.camera.recorder.superslowrecorder;

import android.content.Context;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import android.os.Handler;
import android.util.Pair;
import com.sonyericsson.android.camera.device.CameraActionSound;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController;
import com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController;
import com.sonyericsson.android.camera.recorder.superslowrecorder.VariableSourceMediaRecorder;
import com.sonyericsson.android.camera.recorder.utility.Accessor;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;
import com.sonyericsson.android.camera.recorder.utility.encoder.source.VideoFrameSource;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SuperSlowShotRecorderController extends DefaultRecorderController {
    private static final int MEDIA_FORMAT_OPERATING_RATE = 120;
    private static long MIN_VIDEO_DURATION_MILLIS = 0;
    private static final long START_RECORDING_TIME_OUT_MILLIS = 10000;
    private static final String THREAD_NAME = "SSS_RECORDER_PREPARE";
    private static final boolean TRACE = true;
    private final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener;
    private final DefaultRecorderController.CallbackLock mPrepareSuperSlowRecordingCallbackLock;
    private Future<Boolean> mPrepareTask;
    private final ExecutorService mPrepareTaskExecutor;
    private final DefaultRecorderController.CallbackLock mStartSuperSlowRecordingCallbackLock;
    private final int mSuperSlowFrameNum;
    private final int mSuperSlowFrameRate;

    /* JADX INFO: Access modifiers changed from: private */
    public static void trace(String str) {
        CamLog.d(str);
    }

    public SuperSlowShotRecorderController(Context context, Accessor<CameraActionSound> accessor, Accessor<BypassCamera> accessor2, RecorderController.RecorderListener recorderListener, OnSuperSlowRecordingFinishedListener onSuperSlowRecordingFinishedListener, Handler handler, int i, Handler handler2, boolean z, int i2, int i3) {
        super(context, accessor, accessor2, new VariableSourceMediaRecorder(120), recorderListener, MIN_VIDEO_DURATION_MILLIS, handler, i, handler2, true, false, false, z, true);
        this.mPrepareTaskExecutor = ThreadUtil.buildExecutor(THREAD_NAME);
        trace("SuperSlowShotRecorderController() E");
        this.mSuperSlowFrameRate = i2;
        this.mSuperSlowFrameNum = i3;
        this.mOnSuperSlowRecordingFinishedListener = onSuperSlowRecordingFinishedListener;
        ((VariableSourceMediaRecorder) getRecorder()).setInputDataSourceFactory(new SuperSlowSourceFactory());
        this.mPrepareSuperSlowRecordingCallbackLock = new DefaultRecorderController.CallbackLock();
        this.mStartSuperSlowRecordingCallbackLock = new DefaultRecorderController.CallbackLock();
        trace("SuperSlowShotRecorderController() X");
    }

    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController
    protected boolean prepareCallBack() {
        trace("prepareCallBack() E");
        if (!super.prepareCallBack()) {
            trace("prepareCallBack() X failed.");
            return false;
        }
        getBypassCamera().setSuperSlowCallbacks(new PrepareSuperSlowRecordingCallbackImpl(this.mPrepareSuperSlowRecordingCallbackLock), new StartSuperSlowRecordingCallbackImpl(this.mStartSuperSlowRecordingCallbackLock));
        trace("prepareCallBack() X");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController, com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController
    public boolean prepareInternal(RecorderParameters recorderParameters) {
        this.mPrepareTask = this.mPrepareTaskExecutor.submit(new PrepareTask(recorderParameters));
        return true;
    }

    private class PrepareTask implements Callable<Boolean> {
        private final RecorderParameters mParams;

        public PrepareTask(RecorderParameters recorderParameters) {
            this.mParams = recorderParameters;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        public Boolean call() throws Exception {
            SuperSlowShotRecorderController.trace("prepareInternal() E");
            if (SuperSlowShotRecorderController.this.prepareCallBack()) {
                if (SuperSlowShotRecorderController.super.prepareInternal(this.mParams)) {
                    if (SuperSlowShotRecorderController.this.startBypassCamera()) {
                        SuperSlowShotRecorderController.trace("prepareInternal() X");
                        return true;
                    }
                    CamLog.e("startBypassCamera() is failed in PrepareTask.");
                    SuperSlowShotRecorderController.this.notifyError();
                    return false;
                }
                CamLog.e("prepareInternal() is failed in PrepareTask.");
                SuperSlowShotRecorderController.this.notifyError();
                return false;
            }
            CamLog.e("prepareCallBack() is failed in PrepareTask.");
            SuperSlowShotRecorderController.this.notifyError();
            return false;
        }
    }

    private boolean waitForPrepareCompleted() {
        if (this.mPrepareTask == null) {
            CamLog.e("PrepareTask is not submitted.");
            return false;
        }
        try {
            return this.mPrepareTask.get().booleanValue();
        } catch (InterruptedException unused) {
            CamLog.e("PrepareTask is interrupted.");
            return false;
        } catch (ExecutionException e) {
            CamLog.e("Exception is thrown in PrepareTask.  cause:" + e.getCause().getMessage());
            return false;
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController
    protected void releaseInternal() {
        waitForPrepareCompleted();
        this.mPrepareTaskExecutor.shutdown();
        super.releaseInternal();
    }

    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController
    protected boolean prepareBypassCamera(RecorderParameters recorderParameters) {
        trace("prepareBypassCamera() E frame-rate:" + this.mSuperSlowFrameRate + " frame-num" + this.mSuperSlowFrameNum);
        CountDownLatch countDownLatchRequestLatch = this.mPrepareSuperSlowRecordingCallbackLock.requestLatch();
        try {
            try {
                getBypassCamera().requestPrepareSuperSlowRecording(getRecorder().getSurface(), new BypassCamera.RecordingParameters(new BypassCamera.DataSpace(0, 0, 0)));
                countDownLatchRequestLatch.await();
                this.mPrepareSuperSlowRecordingCallbackLock.release();
                trace("prepareBypassCamera() X");
                return true;
            } catch (InterruptedException | RuntimeException e) {
                trace("prepareBypassCamera() X failed : " + e.getMessage());
                this.mPrepareSuperSlowRecordingCallbackLock.release();
                return false;
            }
        } catch (Throwable th) {
            this.mPrepareSuperSlowRecordingCallbackLock.release();
            throw th;
        }
    }

    private static class PrepareSuperSlowRecordingCallbackImpl implements BypassCamera.PrepareSuperSlowRecordingCallback {
        private final DefaultRecorderController.CallbackLock mLock;

        public PrepareSuperSlowRecordingCallbackImpl(DefaultRecorderController.CallbackLock callbackLock) {
            this.mLock = callbackLock;
        }

        @Override // com.sonymobile.imageprocessor.bypasscamera2.BypassCamera.PrepareSuperSlowRecordingCallback
        public void onPrepareSuperSlowRecordingDone() {
            SuperSlowShotRecorderController.trace("onPrepareSuperSlowRecordingDone() E");
            this.mLock.unlock();
            SuperSlowShotRecorderController.trace("onPrepareSuperSlowRecordingDone() X");
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController, com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController
    protected boolean startInternal() throws TimeoutException {
        trace("startInternal() E");
        if (!waitForPrepareCompleted()) {
            return false;
        }
        CountDownLatch countDownLatchRequestLatch = this.mStartSuperSlowRecordingCallbackLock.requestLatch();
        try {
            getBypassCamera().requestStartSuperSlowRecording();
            try {
                try {
                    trace("startInternal() recorder.start E");
                    getRecorder().start();
                    trace("startInternal() recorder.start X");
                    trace("startInternal() reference-clock.start E");
                    getReferenceClock().start();
                    trace("startInternal() reference-clock.start X");
                    try {
                        trace("startInternal() latch.await E");
                    } catch (InterruptedException e) {
                        trace("startInternal() X failed : " + e.getMessage());
                    }
                    if (!countDownLatchRequestLatch.await(START_RECORDING_TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)) {
                        changeTo(BaseRecorderController.State.RELEASED);
                        if (CamLog.DEBUG) {
                            throw new TimeoutException("Callback of slow motion frame is not sent over 5s from Bypasscamera");
                        }
                        return false;
                    }
                    trace("startInternal() latch.await X");
                    trace("startInternal() post callback E");
                    getCallbackHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.recorder.superslowrecorder.SuperSlowShotRecorderController.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SuperSlowShotRecorderController.this.mOnSuperSlowRecordingFinishedListener.onSuperSlowRecordingFinished();
                        }
                    });
                    trace("startInternal() post callback X");
                    trace("startInternal() X");
                    return true;
                } catch (Throwable th) {
                    try {
                        trace("startInternal() latch.await E");
                    } catch (InterruptedException e2) {
                        trace("startInternal() X failed : " + e2.getMessage());
                    }
                    if (!countDownLatchRequestLatch.await(START_RECORDING_TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)) {
                        changeTo(BaseRecorderController.State.RELEASED);
                        if (CamLog.DEBUG) {
                            throw new TimeoutException("Callback of slow motion frame is not sent over 5s from Bypasscamera");
                        }
                        return false;
                    }
                    trace("startInternal() latch.await X");
                    throw th;
                }
            } catch (IOException | IllegalStateException e3) {
                trace("startInternal() X failed : " + e3.getMessage());
                getRecorder().reset();
                try {
                    trace("startInternal() latch.await E");
                } catch (InterruptedException e4) {
                    trace("startInternal() X failed : " + e4.getMessage());
                }
                if (!countDownLatchRequestLatch.await(START_RECORDING_TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)) {
                    changeTo(BaseRecorderController.State.RELEASED);
                    if (CamLog.DEBUG) {
                        throw new TimeoutException("Callback of slow motion frame is not sent over 5s from Bypasscamera");
                    }
                    return false;
                }
                trace("startInternal() latch.await X");
                return false;
            }
        } catch (RuntimeException e5) {
            trace("startInternal() X failed : " + e5.getMessage());
            return false;
        }
    }

    private static class StartSuperSlowRecordingCallbackImpl implements BypassCamera.StartSuperSlowRecordingCallback {
        private final DefaultRecorderController.CallbackLock mLock;

        public StartSuperSlowRecordingCallbackImpl(DefaultRecorderController.CallbackLock callbackLock) {
            this.mLock = callbackLock;
        }

        @Override // com.sonymobile.imageprocessor.bypasscamera2.BypassCamera.StartSuperSlowRecordingCallback
        public void onStartSuperSlowRecordingDone() {
            SuperSlowShotRecorderController.trace("onStartSuperSlowRecordingDone() E");
            this.mLock.unlock();
            SuperSlowShotRecorderController.trace("onStartSuperSlowRecordingDone() X");
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController
    protected Pair<Boolean, CountDownLatch> stopBypassCamera() {
        return Pair.create(true, null);
    }

    private static class SuperSlowSourceFactory implements VariableSourceMediaRecorder.InputDataSourceFactory {
        private SuperSlowSourceFactory() {
        }

        @Override // com.sonyericsson.android.camera.recorder.superslowrecorder.VariableSourceMediaRecorder.InputDataSourceFactory
        public VideoFrameSource createVideoSource(MediaCodec mediaCodec, CamcorderProfile camcorderProfile) {
            return new VideoFrameSource(mediaCodec);
        }

        @Override // com.sonyericsson.android.camera.recorder.superslowrecorder.VariableSourceMediaRecorder.InputDataSourceFactory
        public InputDataSource createAudioSource(MediaCodec mediaCodec, CamcorderProfile camcorderProfile) {
            throw new UnsupportedOperationException("This recorder doesn't support audio track.");
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController, com.sonyericsson.android.camera.recorder.RecorderController
    public long getRecordingTimeMillis() {
        if (verifyState(BaseRecorderController.State.STOPPING, BaseRecorderController.State.RELEASING)) {
            return (1000 * this.mSuperSlowFrameNum) / 30;
        }
        return super.getRecordingTimeMillis();
    }
}
