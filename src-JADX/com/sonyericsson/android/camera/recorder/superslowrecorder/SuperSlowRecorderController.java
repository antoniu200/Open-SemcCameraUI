package com.sonyericsson.android.camera.recorder.superslowrecorder;

import android.content.Context;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import android.os.Handler;
import android.os.SystemClock;
import com.sonyericsson.android.camera.device.CameraActionSound;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonyericsson.android.camera.recorder.RecorderException;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController;
import com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController;
import com.sonyericsson.android.camera.recorder.superslowrecorder.VariableSourceMediaRecorder;
import com.sonyericsson.android.camera.recorder.utility.Accessor;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;
import com.sonyericsson.android.camera.recorder.utility.encoder.source.MutableAudioSampleDataSource;
import com.sonyericsson.android.camera.recorder.utility.encoder.source.VideoFrameSource;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SuperSlowRecorderController extends DefaultRecorderController {
    private static final int MEDIA_FORMAT_OPERATING_RATE = 120;
    private static long MIN_VIDEO_DURATION_MILLIS = 3000;
    private static final long START_RECORDING_TIME_OUT_MILLIS = 10000;
    private static final long SUPER_SLOW_PROCESS_TIME_MILLIS = 180;
    private static final String THREAD_NAME = "SSM_RECORDER_PREPARE";
    private static final boolean TRACE = true;
    private MutableAudioSampleDataSource mMutableAudioSource;
    private final int mMuteDurationInMillis;
    private final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener;
    private final DefaultRecorderController.CallbackLock mPrepareSuperSlowRecordingCallbackLock;
    private Future<Boolean> mPrepareTask;
    private final ExecutorService mPrepareTaskExecutor;
    private final int mSilentDurationInMillis;
    private final DefaultRecorderController.CallbackLock mStartSuperSlowRecordingCallbackLock;
    private final int mSuperSlowFrameNum;
    private final int mSuperSlowFrameRate;
    private volatile long mSuperSlowTriggerTimeMillis;

    /* JADX INFO: Access modifiers changed from: private */
    public static void trace(String str) {
        CamLog.d(str);
    }

    public SuperSlowRecorderController(Context context, Accessor<CameraActionSound> accessor, Accessor<BypassCamera> accessor2, RecorderController.RecorderListener recorderListener, OnSuperSlowRecordingFinishedListener onSuperSlowRecordingFinishedListener, Handler handler, int i, Handler handler2, boolean z, int i2, int i3) {
        super(context, accessor, accessor2, new VariableSourceMediaRecorder(120), recorderListener, MIN_VIDEO_DURATION_MILLIS, handler, i, handler2, true, true, true, z, true);
        this.mPrepareTaskExecutor = ThreadUtil.buildExecutor(THREAD_NAME);
        trace("SuperSlowRecorderController() E");
        this.mSuperSlowFrameRate = i2;
        this.mSuperSlowFrameNum = i3;
        this.mOnSuperSlowRecordingFinishedListener = onSuperSlowRecordingFinishedListener;
        this.mMuteDurationInMillis = (this.mSuperSlowFrameNum * 1000) / this.mSuperSlowFrameRate;
        this.mSilentDurationInMillis = (1000 * this.mSuperSlowFrameNum) / 30;
        ((VariableSourceMediaRecorder) getRecorder()).setInputDataSourceFactory(new SuperSlowSourceFactory());
        this.mPrepareSuperSlowRecordingCallbackLock = new DefaultRecorderController.CallbackLock();
        this.mStartSuperSlowRecordingCallbackLock = new DefaultRecorderController.CallbackLock();
        trace("SuperSlowRecorderController() X");
    }

    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController, com.sonyericsson.android.camera.recorder.RecorderController
    public boolean prepare(RecorderParameters recorderParameters) {
        this.mSuperSlowTriggerTimeMillis = 0L;
        return super.prepare(recorderParameters);
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
            if (SuperSlowRecorderController.super.prepareInternal(this.mParams)) {
                return true;
            }
            CamLog.e("prepareInternal() is failed in PrepareTask.");
            SuperSlowRecorderController.this.notifyError();
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

    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController, com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController
    protected boolean startInternal() {
        trace("startInternal() E");
        if (!waitForPrepareCompleted()) {
            return false;
        }
        if (!startRecorder()) {
            trace("startInternal() X failed");
            return false;
        }
        if (!startBypassCamera()) {
            trace("startInternal() X failed");
            return false;
        }
        trace("startInternal() X");
        return true;
    }

    public boolean startSuperSlow() throws RecorderException {
        trace("startSuperSlow() E");
        synchronized (this.mStateLock) {
            if (!verifyState(BaseRecorderController.State.STARTING, BaseRecorderController.State.RECORDING)) {
                trace("start() X failed : illegal state");
                throw new RecorderException("Fail to verify state.");
            }
            executeInBackground(new StartSuperSlowTask());
        }
        trace("startSuperSlow() X");
        return true;
    }

    private class StartSuperSlowTask implements Runnable {
        private StartSuperSlowTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (!SuperSlowRecorderController.this.startBypassCameraSuperSlow()) {
                    SuperSlowRecorderController.this.notifyError();
                } else {
                    SuperSlowRecorderController.this.getCallbackHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.recorder.superslowrecorder.SuperSlowRecorderController.StartSuperSlowTask.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SuperSlowRecorderController.this.mOnSuperSlowRecordingFinishedListener.onSuperSlowRecordingFinished();
                        }
                    });
                }
            } catch (TimeoutException e) {
                if (CamLog.DEBUG) {
                    throw new RuntimeException(e);
                }
                SuperSlowRecorderController.this.notifyError();
            }
        }
    }

    private long computeSuperSlowRemainTime() {
        trace("computeSuperSlowRemainTime()");
        long j = this.mSuperSlowTriggerTimeMillis;
        trace("  super-slow-trigger-time:" + j);
        trace("  super-slow-process-time:180");
        if (j == 0) {
            return 0L;
        }
        long jUptimeMillis = SystemClock.uptimeMillis() - j;
        trace("  elapsed-time-since-trigger:" + jUptimeMillis);
        return Math.max(0L, SUPER_SLOW_PROCESS_TIME_MILLIS - jUptimeMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean startBypassCameraSuperSlow() throws InterruptedException, TimeoutException {
        trace("startBypassCameraSuperSlow() E");
        synchronized (this.mStateLock) {
            if (verifyState(BaseRecorderController.State.RELEASED)) {
                trace("startBypassCameraSuperSlow() X failed to verify state:" + BaseRecorderController.State.RELEASED.name());
                return false;
            }
            if (this.mMutableAudioSource != null) {
                this.mMutableAudioSource.startMute();
            }
            CountDownLatch countDownLatchRequestLatch = this.mStartSuperSlowRecordingCallbackLock.requestLatch();
            this.mSuperSlowTriggerTimeMillis = SystemClock.uptimeMillis();
            try {
                getBypassCamera().requestStartSuperSlowRecording();
                try {
                    if (!countDownLatchRequestLatch.await(START_RECORDING_TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)) {
                        throw new TimeoutException("Callback of slow motion frame is not sent over 5s from Bypasscamera");
                    }
                    trace("actual-elapsed-time-since-trigger:" + (SystemClock.uptimeMillis() - this.mSuperSlowTriggerTimeMillis));
                    this.mSuperSlowTriggerTimeMillis = 0L;
                    this.mStartSuperSlowRecordingCallbackLock.release();
                    trace("startBypassCameraSuperSlow() X");
                    return true;
                } catch (InterruptedException e) {
                    trace("startBypassCameraSuperSlow() X failed : " + e.getMessage());
                    return false;
                } finally {
                    trace("actual-elapsed-time-since-trigger:" + (SystemClock.uptimeMillis() - this.mSuperSlowTriggerTimeMillis));
                    this.mSuperSlowTriggerTimeMillis = 0L;
                    this.mStartSuperSlowRecordingCallbackLock.release();
                }
            } catch (RuntimeException e2) {
                trace("startBypassCameraSuperSlow() X failed : " + e2.getMessage());
                return false;
            }
        }
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
            SuperSlowRecorderController.trace("onPrepareSuperSlowRecordingDone() E");
            this.mLock.unlock();
            SuperSlowRecorderController.trace("onPrepareSuperSlowRecordingDone() X");
        }
    }

    private static class StartSuperSlowRecordingCallbackImpl implements BypassCamera.StartSuperSlowRecordingCallback {
        private final DefaultRecorderController.CallbackLock mLock;

        public StartSuperSlowRecordingCallbackImpl(DefaultRecorderController.CallbackLock callbackLock) {
            this.mLock = callbackLock;
        }

        @Override // com.sonymobile.imageprocessor.bypasscamera2.BypassCamera.StartSuperSlowRecordingCallback
        public void onStartSuperSlowRecordingDone() {
            SuperSlowRecorderController.trace("onStartSuperSlowRecordingDone() E");
            this.mLock.unlock();
            SuperSlowRecorderController.trace("onStartSuperSlowRecordingDone() X");
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController, com.sonyericsson.android.camera.recorder.RecorderController
    public long getRecordingTimeMillis() {
        if (verifyState(BaseRecorderController.State.STOPPING, BaseRecorderController.State.RELEASING)) {
            return this.mMutableAudioSource.getCurrentPresentationTimeMillis() + computeSuperSlowRemainTime();
        }
        return super.getRecordingTimeMillis();
    }

    private class SuperSlowSourceFactory implements VariableSourceMediaRecorder.InputDataSourceFactory {
        private SuperSlowSourceFactory() {
        }

        @Override // com.sonyericsson.android.camera.recorder.superslowrecorder.VariableSourceMediaRecorder.InputDataSourceFactory
        public VideoFrameSource createVideoSource(MediaCodec mediaCodec, CamcorderProfile camcorderProfile) {
            return new VideoFrameSource(mediaCodec);
        }

        @Override // com.sonyericsson.android.camera.recorder.superslowrecorder.VariableSourceMediaRecorder.InputDataSourceFactory
        public InputDataSource createAudioSource(MediaCodec mediaCodec, CamcorderProfile camcorderProfile) {
            SuperSlowRecorderController.this.mMutableAudioSource = new MutableAudioSampleDataSource(mediaCodec, camcorderProfile.audioSampleRate, camcorderProfile.audioChannels, 2, SuperSlowRecorderController.this.mMuteDurationInMillis, SuperSlowRecorderController.this.mSilentDurationInMillis);
            return SuperSlowRecorderController.this.mMutableAudioSource;
        }
    }
}
