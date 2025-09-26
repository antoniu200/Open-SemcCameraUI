// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.superslowrecorder;

import com.sonyericsson.android.camera.recorder.utility.encoder.source.VideoFrameSource;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import com.sonyericsson.android.camera.recorder.RecorderException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.concurrent.TimeUnit;
import com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController;
import android.os.SystemClock;
import java.util.concurrent.TimeoutException;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.recorder.RecorderInterface;
import android.os.Handler;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import com.sonyericsson.android.camera.device.CameraActionSound;
import com.sonyericsson.android.camera.recorder.utility.Accessor;
import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import com.sonyericsson.android.camera.recorder.utility.encoder.source.MutableAudioSampleDataSource;
import com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController;

public class SuperSlowRecorderController extends DefaultRecorderController
{
    private static final int MEDIA_FORMAT_OPERATING_RATE = 120;
    private static long MIN_VIDEO_DURATION_MILLIS = 3000L;
    private static final long START_RECORDING_TIME_OUT_MILLIS = 10000L;
    private static final long SUPER_SLOW_PROCESS_TIME_MILLIS = 180L;
    private static final String THREAD_NAME = "SSM_RECORDER_PREPARE";
    private static final boolean TRACE = true;
    private MutableAudioSampleDataSource mMutableAudioSource;
    private final int mMuteDurationInMillis;
    private final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener;
    private final CallbackLock mPrepareSuperSlowRecordingCallbackLock;
    private Future<Boolean> mPrepareTask;
    private final ExecutorService mPrepareTaskExecutor;
    private final int mSilentDurationInMillis;
    private final CallbackLock mStartSuperSlowRecordingCallbackLock;
    private final int mSuperSlowFrameNum;
    private final int mSuperSlowFrameRate;
    private volatile long mSuperSlowTriggerTimeMillis;
    
    public SuperSlowRecorderController(final Context context, final Accessor<CameraActionSound> accessor, final Accessor<BypassCamera> accessor2, final RecorderListener recorderListener, final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener, final Handler handler, final int n, final Handler handler2, final boolean b, final int mSuperSlowFrameRate, final int mSuperSlowFrameNum) {
        super(context, accessor, accessor2, new VariableSourceMediaRecorder(120), recorderListener, SuperSlowRecorderController.MIN_VIDEO_DURATION_MILLIS, handler, n, handler2, true, true, true, b, true);
        this.mPrepareTaskExecutor = ThreadUtil.buildExecutor("SSM_RECORDER_PREPARE");
        trace("SuperSlowRecorderController() E");
        this.mSuperSlowFrameRate = mSuperSlowFrameRate;
        this.mSuperSlowFrameNum = mSuperSlowFrameNum;
        this.mOnSuperSlowRecordingFinishedListener = mOnSuperSlowRecordingFinishedListener;
        this.mMuteDurationInMillis = this.mSuperSlowFrameNum * 1000 / this.mSuperSlowFrameRate;
        this.mSilentDurationInMillis = 1000 * this.mSuperSlowFrameNum / 30;
        ((VariableSourceMediaRecorder)this.getRecorder()).setInputDataSourceFactory((VariableSourceMediaRecorder.InputDataSourceFactory)new SuperSlowSourceFactory());
        this.mPrepareSuperSlowRecordingCallbackLock = new CallbackLock();
        this.mStartSuperSlowRecordingCallbackLock = new CallbackLock();
        trace("SuperSlowRecorderController() X");
    }
    
    private long computeSuperSlowRemainTime() {
        trace("computeSuperSlowRemainTime()");
        final long mSuperSlowTriggerTimeMillis = this.mSuperSlowTriggerTimeMillis;
        final StringBuilder sb = new StringBuilder();
        sb.append("  super-slow-trigger-time:");
        sb.append(mSuperSlowTriggerTimeMillis);
        trace(sb.toString());
        trace("  super-slow-process-time:180");
        if (mSuperSlowTriggerTimeMillis == 0L) {
            return 0L;
        }
        final long lng = SystemClock.uptimeMillis() - mSuperSlowTriggerTimeMillis;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("  elapsed-time-since-trigger:");
        sb2.append(lng);
        trace(sb2.toString());
        return Math.max(0L, 180L - lng);
    }
    
    private boolean startBypassCameraSuperSlow() throws TimeoutException {
        trace("startBypassCameraSuperSlow() E");
        Object o = this.mStateLock;
        synchronized (o) {
            if (this.verifyState(State.RELEASED)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("startBypassCameraSuperSlow() X failed to verify state:");
                sb.append(State.RELEASED.name());
                trace(sb.toString());
                return false;
            }
            monitorexit(o);
            if (this.mMutableAudioSource != null) {
                this.mMutableAudioSource.startMute();
            }
            o = this.mStartSuperSlowRecordingCallbackLock.requestLatch();
            this.mSuperSlowTriggerTimeMillis = SystemClock.uptimeMillis();
            try {
                this.getBypassCamera().requestStartSuperSlowRecording();
                try {
                    try {
                        if (!((CountDownLatch)o).await(10000L, TimeUnit.MILLISECONDS)) {
                            throw new TimeoutException("Callback of slow motion frame is not sent over 5s from Bypasscamera");
                        }
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("actual-elapsed-time-since-trigger:");
                        sb2.append(SystemClock.uptimeMillis() - this.mSuperSlowTriggerTimeMillis);
                        trace(sb2.toString());
                        this.mSuperSlowTriggerTimeMillis = 0L;
                        this.mStartSuperSlowRecordingCallbackLock.release();
                        trace("startBypassCameraSuperSlow() X");
                        return true;
                    }
                    finally {}
                }
                catch (final InterruptedException ex) {
                    o = new StringBuilder();
                    ((StringBuilder)o).append("startBypassCameraSuperSlow() X failed : ");
                    ((StringBuilder)o).append(ex.getMessage());
                    trace(((StringBuilder)o).toString());
                    o = new StringBuilder();
                    ((StringBuilder)o).append("actual-elapsed-time-since-trigger:");
                    ((StringBuilder)o).append(SystemClock.uptimeMillis() - this.mSuperSlowTriggerTimeMillis);
                    trace(((StringBuilder)o).toString());
                    this.mSuperSlowTriggerTimeMillis = 0L;
                    this.mStartSuperSlowRecordingCallbackLock.release();
                    return false;
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("actual-elapsed-time-since-trigger:");
                sb3.append(SystemClock.uptimeMillis() - this.mSuperSlowTriggerTimeMillis);
                trace(sb3.toString());
                this.mSuperSlowTriggerTimeMillis = 0L;
                this.mStartSuperSlowRecordingCallbackLock.release();
            }
            catch (final RuntimeException ex2) {
                o = new StringBuilder();
                ((StringBuilder)o).append("startBypassCameraSuperSlow() X failed : ");
                ((StringBuilder)o).append(ex2.getMessage());
                trace(((StringBuilder)o).toString());
                o = new StringBuilder();
                ((StringBuilder)o).append("actual-elapsed-time-since-trigger:");
                ((StringBuilder)o).append(SystemClock.uptimeMillis() - this.mSuperSlowTriggerTimeMillis);
                trace(((StringBuilder)o).toString());
                this.mSuperSlowTriggerTimeMillis = 0L;
                this.mStartSuperSlowRecordingCallbackLock.release();
                return false;
            }
        }
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    private boolean waitForPrepareCompleted() {
        if (this.mPrepareTask == null) {
            CamLog.e("PrepareTask is not submitted.");
            return false;
        }
        try {
            return this.mPrepareTask.get();
        }
        catch (final ExecutionException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception is thrown in PrepareTask.  cause:");
            sb.append(ex.getCause().getMessage());
            CamLog.e(sb.toString());
            return false;
        }
        catch (final InterruptedException ex2) {
            CamLog.e("PrepareTask is interrupted.");
            return false;
        }
    }
    
    @Override
    public long getRecordingTimeMillis() {
        if (this.verifyState(State.STOPPING, State.RELEASING)) {
            return this.mMutableAudioSource.getCurrentPresentationTimeMillis() + this.computeSuperSlowRemainTime();
        }
        return super.getRecordingTimeMillis();
    }
    
    @Override
    public boolean prepare(final RecorderParameters recorderParameters) {
        this.mSuperSlowTriggerTimeMillis = 0L;
        return super.prepare(recorderParameters);
    }
    
    @Override
    protected boolean prepareBypassCamera(final RecorderParameters recorderParameters) {
        final StringBuilder sb = new StringBuilder();
        sb.append("prepareBypassCamera() E frame-rate:");
        sb.append(this.mSuperSlowFrameRate);
        sb.append(" frame-num");
        sb.append(this.mSuperSlowFrameNum);
        trace(sb.toString());
        final CountDownLatch requestLatch = this.mPrepareSuperSlowRecordingCallbackLock.requestLatch();
        try {
            try {
                this.getBypassCamera().requestPrepareSuperSlowRecording(this.getRecorder().getSurface(), new BypassCamera.RecordingParameters(new BypassCamera.DataSpace(0, 0, 0)));
                requestLatch.await();
                this.mPrepareSuperSlowRecordingCallbackLock.release();
                trace("prepareBypassCamera() X");
                return true;
            }
            finally {}
        }
        catch (final RuntimeException | InterruptedException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("prepareBypassCamera() X failed : ");
            sb2.append(((Throwable)ex).getMessage());
            trace(sb2.toString());
            this.mPrepareSuperSlowRecordingCallbackLock.release();
            return false;
        }
        this.mPrepareSuperSlowRecordingCallbackLock.release();
    }
    
    @Override
    protected boolean prepareCallBack() {
        trace("prepareCallBack() E");
        if (!super.prepareCallBack()) {
            trace("prepareCallBack() X failed.");
            return false;
        }
        this.getBypassCamera().setSuperSlowCallbacks((BypassCamera.PrepareSuperSlowRecordingCallback)new PrepareSuperSlowRecordingCallbackImpl(this.mPrepareSuperSlowRecordingCallbackLock), (BypassCamera.StartSuperSlowRecordingCallback)new StartSuperSlowRecordingCallbackImpl(this.mStartSuperSlowRecordingCallbackLock));
        trace("prepareCallBack() X");
        return true;
    }
    
    @Override
    protected boolean prepareInternal(final RecorderParameters recorderParameters) {
        this.mPrepareTask = this.mPrepareTaskExecutor.submit((Callable<Boolean>)new PrepareTask(recorderParameters));
        return true;
    }
    
    @Override
    protected void releaseInternal() {
        this.waitForPrepareCompleted();
        this.mPrepareTaskExecutor.shutdown();
        super.releaseInternal();
    }
    
    @Override
    protected boolean startInternal() {
        trace("startInternal() E");
        if (!this.waitForPrepareCompleted()) {
            return false;
        }
        if (!this.startRecorder()) {
            trace("startInternal() X failed");
            return false;
        }
        if (!this.startBypassCamera()) {
            trace("startInternal() X failed");
            return false;
        }
        trace("startInternal() X");
        return true;
    }
    
    public boolean startSuperSlow() throws RecorderException {
        trace("startSuperSlow() E");
        synchronized (this.mStateLock) {
            if (!this.verifyState(State.STARTING, State.RECORDING)) {
                trace("start() X failed : illegal state");
                throw new RecorderException("Fail to verify state.");
            }
            this.executeInBackground(new StartSuperSlowTask());
            monitorexit(this.mStateLock);
            trace("startSuperSlow() X");
            return true;
        }
    }
    
    private static class PrepareSuperSlowRecordingCallbackImpl implements PrepareSuperSlowRecordingCallback
    {
        private final CallbackLock mLock;
        
        public PrepareSuperSlowRecordingCallbackImpl(final CallbackLock mLock) {
            this.mLock = mLock;
        }
        
        @Override
        public void onPrepareSuperSlowRecordingDone() {
            trace("onPrepareSuperSlowRecordingDone() E");
            this.mLock.unlock();
            trace("onPrepareSuperSlowRecordingDone() X");
        }
    }
    
    private class PrepareTask implements Callable<Boolean>
    {
        private final RecorderParameters mParams;
        final SuperSlowRecorderController this$0;
        
        public PrepareTask(final SuperSlowRecorderController this$0, final RecorderParameters mParams) {
            this.this$0 = this$0;
            this.mParams = mParams;
        }
        
        @Override
        public Boolean call() throws Exception {
            if (this.this$0.prepareInternal(this.mParams)) {
                return true;
            }
            CamLog.e("prepareInternal() is failed in PrepareTask.");
            this.this$0.notifyError();
            return false;
        }
    }
    
    private static class StartSuperSlowRecordingCallbackImpl implements StartSuperSlowRecordingCallback
    {
        private final CallbackLock mLock;
        
        public StartSuperSlowRecordingCallbackImpl(final CallbackLock mLock) {
            this.mLock = mLock;
        }
        
        @Override
        public void onStartSuperSlowRecordingDone() {
            trace("onStartSuperSlowRecordingDone() E");
            this.mLock.unlock();
            trace("onStartSuperSlowRecordingDone() X");
        }
    }
    
    private class StartSuperSlowTask implements Runnable
    {
        final SuperSlowRecorderController this$0;
        
        private StartSuperSlowTask(final SuperSlowRecorderController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            try {
                if (!this.this$0.startBypassCameraSuperSlow()) {
                    this.this$0.notifyError();
                }
                else {
                    this.this$0.getCallbackHandler().post((Runnable)new Runnable(this) {
                        final StartSuperSlowTask this$1;
                        
                        @Override
                        public void run() {
                            this.this$1.this$0.mOnSuperSlowRecordingFinishedListener.onSuperSlowRecordingFinished();
                        }
                    });
                }
            }
            catch (final TimeoutException cause) {
                if (CamLog.DEBUG) {
                    throw new RuntimeException(cause);
                }
                this.this$0.notifyError();
            }
        }
    }
    
    private class SuperSlowSourceFactory implements InputDataSourceFactory
    {
        final SuperSlowRecorderController this$0;
        
        private SuperSlowSourceFactory(final SuperSlowRecorderController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public InputDataSource createAudioSource(final MediaCodec mediaCodec, final CamcorderProfile camcorderProfile) {
            this.this$0.mMutableAudioSource = new MutableAudioSampleDataSource(mediaCodec, camcorderProfile.audioSampleRate, camcorderProfile.audioChannels, 2, this.this$0.mMuteDurationInMillis, this.this$0.mSilentDurationInMillis);
            return this.this$0.mMutableAudioSource;
        }
        
        @Override
        public VideoFrameSource createVideoSource(final MediaCodec mediaCodec, final CamcorderProfile camcorderProfile) {
            return new VideoFrameSource(mediaCodec);
        }
    }
}
