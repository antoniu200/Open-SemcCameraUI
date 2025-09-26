// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.defaultrecorder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.io.IOException;
import android.location.Location;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.android.camera.recorder.RecorderException;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.concurrent.CountDownLatch;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.recorder.utility.ReferenceClock;
import android.content.Context;
import com.sonyericsson.android.camera.device.CameraActionSound;
import com.sonyericsson.android.camera.recorder.utility.Accessor;
import android.os.Handler;
import com.sonyericsson.android.camera.recorder.RecorderInterface;
import com.sonyericsson.android.camera.recorder.RecorderController;

public class BaseRecorderController implements RecorderController
{
    private static final int TIME_OF_START_SOUND_TO_COMPLETE_IN_MILLI = 300;
    private static final boolean TRACE = true;
    private static final boolean TRACE_FOR_PROGRESS = false;
    private final RecorderInterface.RecordTrackListener mAudioTrackListener;
    private final Handler mCallbackHandler;
    private final Accessor<CameraActionSound> mCameraActionSound;
    private final Context mContext;
    private final Handler mDeviceHandler;
    private boolean mIsAdjustRecordingTimeByRecorderNotification;
    protected boolean mIsCameraErrorDetected;
    private boolean mIsMicrophoneEnabled;
    private final boolean mIsStartSoundRequired;
    private boolean mIsStopSoundAlreadyPlayed;
    private final Object mIsStopSoundAlreadyPlayedLock;
    private final boolean mIsStopSoundRequired;
    private boolean mIsUserSoundSettingOn;
    private long mLastNotifyDurationMillis;
    private final RecorderListener mListener;
    private long mMaxDurationMillis;
    private final long mMinDurationMillis;
    private final RecorderInterface.OnErrorListener mOnErrorListener;
    private final RecorderInterface.OnMaxReachedListener mOnMaxReachedListener;
    private final ReferenceClock.TickCallback mOnTickCallback;
    private final RecorderInterface mRecorder;
    private final ReferenceClock mReferenceClock;
    private final boolean mShouldWaitStartSound;
    private State mState;
    protected final Object mStateLock;
    private Storage.StorageWriteNotifier mStorageWriteNotifier;
    private final RecorderInterface.RecordTrackListener mVideoTrackListener;
    private CountDownLatch mWaitUntilWriting;
    
    public BaseRecorderController(final Context mContext, final Accessor<CameraActionSound> mCameraActionSound, final RecorderInterface mRecorder, final Handler mCallbackHandler, final RecorderListener mListener, final long mMinDurationMillis, final int n, final Handler mDeviceHandler, final boolean mIsStartSoundRequired, final boolean mShouldWaitStartSound, final boolean mIsStopSoundRequired, final boolean mIsUserSoundSettingOn) {
        this.mStateLock = new Object();
        this.mIsStopSoundAlreadyPlayedLock = new Object();
        this.mOnErrorListener = new RecorderInterface.OnErrorListener() {
            final BaseRecorderController this$0;
            
            @Override
            public void onError() {
                this.this$0.notifyError();
            }
        };
        this.mOnMaxReachedListener = new RecorderInterface.OnMaxReachedListener() {
            final BaseRecorderController this$0;
            
            @Override
            public void onMaxDurationReached() {
                trace("onMaxDurationReached() E");
                this.this$0.displayMaxDuration();
                this.this$0.notifyFinishResult(Result.MAX_DURATION_REACHED);
                trace("onMaxDurationReached() X");
            }
            
            @Override
            public void onMaxFileSizeReached() {
                trace("onMaxFileSizeReached() E");
                this.this$0.notifyFinishResult(Result.MAX_FILESIZE_REACHED);
                trace("onMaxFileSizeReached() X");
            }
        };
        this.mAudioTrackListener = new RecorderInterface.RecordTrackListener() {
            final BaseRecorderController this$0;
            
            @Override
            public void onCompleted() {
                trace("onCompleted() E: Audio Track");
                this.this$0.playStopSound();
                trace("onCompleted() X: Audio Track");
            }
            
            @Override
            public void onProgress(final long n) {
                this.this$0.notifyDuration(n);
            }
            
            @Override
            public void onStarted() {
                trace("onStarted() E: Audio Track");
                if (this.this$0.mWaitUntilWriting != null && this.this$0.mWaitUntilWriting.getCount() > 0L) {
                    this.this$0.mWaitUntilWriting.countDown();
                }
                trace("onStarted() X: Audio Track");
            }
        };
        this.mVideoTrackListener = new RecorderInterface.RecordTrackListener() {
            final BaseRecorderController this$0;
            
            @Override
            public void onCompleted() {
                trace("onCompleted() E: Video Track");
                trace("onCompleted() X: Video Track");
            }
            
            @Override
            public void onProgress(final long n) {
                this.this$0.notifyDuration(n);
            }
            
            @Override
            public void onStarted() {
                trace("onStarted() E: Video Track");
                if (this.this$0.mWaitUntilWriting != null && this.this$0.mWaitUntilWriting.getCount() > 0L) {
                    this.this$0.mWaitUntilWriting.countDown();
                }
                trace("onStarted() X: Video Track");
            }
        };
        this.mOnTickCallback = new ReferenceClock.TickCallback() {
            final BaseRecorderController this$0;
            
            @Override
            public void onTick(final long n) {
                synchronized (this.this$0.mStateLock) {
                    if (this.this$0.verifyState(State.IDLE, State.RELEASING)) {
                        return;
                    }
                    monitorexit(this.this$0.mStateLock);
                    if (this.this$0.mStorageWriteNotifier != null) {
                        this.this$0.mStorageWriteNotifier.notifyWriteStorage();
                    }
                    this.this$0.mListener.onRecordProgress(n);
                }
            }
        };
        trace("BaseRecorderController() E");
        this.mContext = mContext;
        this.mCameraActionSound = mCameraActionSound;
        this.mListener = mListener;
        this.mCallbackHandler = mCallbackHandler;
        this.changeTo(State.IDLE);
        this.mReferenceClock = new ReferenceClock(this.mCallbackHandler, this.mOnTickCallback, n);
        this.mDeviceHandler = mDeviceHandler;
        this.mMinDurationMillis = mMinDurationMillis;
        this.mIsStartSoundRequired = mIsStartSoundRequired;
        this.mShouldWaitStartSound = mShouldWaitStartSound;
        this.mIsStopSoundRequired = mIsStopSoundRequired;
        this.mIsUserSoundSettingOn = mIsUserSoundSettingOn;
        this.mIsStopSoundAlreadyPlayed = false;
        (this.mRecorder = mRecorder).setListener(this.mAudioTrackListener, this.mVideoTrackListener, this.mOnErrorListener, this.mOnMaxReachedListener);
        this.mIsAdjustRecordingTimeByRecorderNotification = true;
        trace("BaseRecorderController() X");
    }
    
    private void displayMaxDuration() {
        if (this.mMaxDurationMillis > 0L && this.mMaxDurationMillis - this.mLastNotifyDurationMillis >= 0L && this.mMaxDurationMillis - this.mLastNotifyDurationMillis < 1000L) {
            this.mCallbackHandler.post((Runnable)new Runnable(this) {
                final BaseRecorderController this$0;
                
                @Override
                public void run() {
                    this.this$0.mOnTickCallback.onTick(this.this$0.mMaxDurationMillis);
                }
            });
        }
    }
    
    private CameraActionSound getCameraActionSound() {
        return this.mCameraActionSound.get();
    }
    
    private void notifyDuration(final long mLastNotifyDurationMillis) {
        this.mLastNotifyDurationMillis = mLastNotifyDurationMillis;
        if (this.mIsAdjustRecordingTimeByRecorderNotification && !this.mReferenceClock.isMeasuring()) {
            synchronized (this.mStateLock) {
                if (this.verifyState(State.STARTING, State.RECORDING)) {
                    this.mReferenceClock.reset(mLastNotifyDurationMillis);
                    this.mReferenceClock.resume();
                }
                monitorexit(this.mStateLock);
                if (this.mStorageWriteNotifier != null) {
                    this.mStorageWriteNotifier.notifyWriteStorage();
                }
                this.mListener.onRecordProgress(mLastNotifyDurationMillis);
            }
        }
    }
    
    private void notifyFinishResult(final Result result) {
        this.mCallbackHandler.post((Runnable)new NotifyFinishResult(result));
    }
    
    private void playStopSound() {
        final StringBuilder sb = new StringBuilder();
        sb.append("playStopSound() E required:");
        sb.append(this.shouldPlayStopSound());
        trace(sb.toString());
        if (this.shouldPlayStopSound()) {
            boolean b = false;
            Object o = this.mIsStopSoundAlreadyPlayedLock;
            synchronized (o) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("playStopSound() is-already-played:");
                sb2.append(this.mIsStopSoundAlreadyPlayed);
                trace(sb2.toString());
                if (!this.mIsStopSoundAlreadyPlayed) {
                    this.mIsStopSoundAlreadyPlayed = true;
                    b = true;
                }
                monitorexit(o);
                if (b) {
                    o = this.getCameraActionSound();
                    if (o != null) {
                        ((CameraActionSound)o).play(3, true);
                    }
                }
            }
        }
        trace("playStopSound() X");
    }
    
    private boolean shouldPlayStartSound() {
        return this.mIsStartSoundRequired && this.mIsUserSoundSettingOn;
    }
    
    private boolean shouldPlayStopSound() {
        return this.mIsStopSoundRequired && this.mIsUserSoundSettingOn;
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    protected void changeTo(final State mState) {
        final StringBuilder sb = new StringBuilder();
        sb.append("changeTo() ");
        sb.append(mState.name());
        trace(sb.toString());
        this.mState = mState;
    }
    
    protected void disableAdjustRecordingTimeByRecorderNotification() {
        this.mIsAdjustRecordingTimeByRecorderNotification = false;
    }
    
    protected void executeInBackground(final Runnable runnable) {
        this.mDeviceHandler.post(runnable);
    }
    
    protected Handler getCallbackHandler() {
        return this.mCallbackHandler;
    }
    
    protected Context getContext() {
        return this.mContext;
    }
    
    protected RecorderInterface getRecorder() {
        return this.mRecorder;
    }
    
    @Override
    public long getRecordingTimeMillis() {
        return this.mReferenceClock.elapsedTimeMillis();
    }
    
    protected ReferenceClock getReferenceClock() {
        return this.mReferenceClock;
    }
    
    @Override
    public boolean isPaused() {
        synchronized (this.mStateLock) {
            return this.verifyState(State.PAUSED);
        }
    }
    
    @Override
    public boolean isReady() {
        synchronized (this.mStateLock) {
            return this.verifyState(State.PREPARED);
        }
    }
    
    @Override
    public boolean isRecording() {
        synchronized (this.mStateLock) {
            return this.verifyState(State.STARTING, State.RECORDING);
        }
    }
    
    @Override
    public boolean isStarting() {
        synchronized (this.mStateLock) {
            return this.verifyState(State.STARTING);
        }
    }
    
    @Override
    public boolean isStopping() {
        synchronized (this.mStateLock) {
            return this.verifyState(State.STOPPING, State.RELEASING);
        }
    }
    
    protected void notifyError() {
        this.mCallbackHandler.post((Runnable)new OnErrorTask());
    }
    
    @Override
    public void pause() throws RecorderException {
        trace("pause() E");
        synchronized (this.mStateLock) {
            if (!this.verifyState(State.STARTING, State.RECORDING)) {
                trace("pause() X failed : illegal state");
                final StringBuilder sb = new StringBuilder();
                sb.append("Fail to verify state. state:");
                sb.append(this.mState.name());
                throw new RecorderException(sb.toString());
            }
            this.changeTo(State.PAUSED);
            this.executeInBackground(new PauseTask());
            monitorexit(this.mStateLock);
            trace("pause() X");
        }
    }
    
    protected boolean pauseInternal() {
        trace("pauseInternal() E");
        try {
            this.waitUntilFirstVideoFrameWritten();
            this.mReferenceClock.stop();
            this.mRecorder.pause();
            trace("pauseInternal() X");
            return true;
        }
        catch (final RuntimeException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("pauseInternal() X failed : ");
            sb.append(ex.getMessage());
            trace(sb.toString());
            this.mRecorder.reset();
            return false;
        }
    }
    
    protected void playStartSound() {
        final StringBuilder sb = new StringBuilder();
        sb.append("playStartSound() E required:");
        sb.append(this.shouldPlayStartSound());
        trace(sb.toString());
        if (!this.shouldPlayStartSound()) {
            trace("playStartSound() X not required");
            return;
        }
        final CameraActionSound cameraActionSound = this.getCameraActionSound();
        if (cameraActionSound != null) {
            cameraActionSound.play(2, true);
        }
        trace("playStartSound() X");
    }
    
    @Override
    public boolean prepare(final RecorderParameters recorderParameters) {
        trace("prepare() E");
        this.mIsStopSoundAlreadyPlayed = false;
        this.mLastNotifyDurationMillis = 0L;
        synchronized (this.mStateLock) {
            if (!this.verifyState(State.IDLE)) {
                trace("prepare() X failed : illegal state");
                return false;
            }
            this.changeTo(State.PREPARED);
            this.executeInBackground(new PrepareTask(recorderParameters));
            monitorexit(this.mStateLock);
            trace("prepare() X");
            return true;
        }
    }
    
    protected boolean prepareInternal(final RecorderParameters recorderParameters) {
        final StringBuilder sb = new StringBuilder();
        sb.append("prepareInternal() E mic:");
        sb.append(recorderParameters.isMicrophoneEnabled());
        trace(sb.toString());
        this.mIsMicrophoneEnabled = recorderParameters.isMicrophoneEnabled();
        if (recorderParameters.hasMaxDuration()) {
            this.mMaxDurationMillis = recorderParameters.maxDuration();
        }
        else {
            this.mMaxDurationMillis = 0L;
        }
        this.mRecorder.reset();
        final boolean prepare = this.mRecorder.prepare(this.mContext, recorderParameters);
        if (!prepare) {
            synchronized (this.mStateLock) {
                this.changeTo(State.RELEASED);
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("prepareInternal() X success:");
        sb2.append(prepare);
        trace(sb2.toString());
        return prepare;
    }
    
    @Override
    public boolean release() {
        trace("release() E");
        synchronized (this.mStateLock) {
            if (this.verifyState(State.RELEASING, State.RELEASED)) {
                trace("release() X already released");
                return true;
            }
            if (this.verifyState(State.STARTING, State.RECORDING, State.PAUSED)) {
                try {
                    this.stop();
                }
                catch (final RecorderException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("release() X failed : ");
                    sb.append(ex.getMessage());
                    CamLog.e(sb.toString());
                    return false;
                }
            }
            final boolean verifyState = this.verifyState(State.STOPPING, State.IDLE, State.PREPARED);
            this.changeTo(State.RELEASING);
            monitorexit(this.mStateLock);
            Label_0204: {
                if (verifyState) {
                    this.executeInBackground(new Runnable(this) {
                        final BaseRecorderController this$0;
                        
                        @Override
                        public void run() {
                            synchronized (this.this$0.mStateLock) {
                                if (this.this$0.verifyState(State.RELEASED)) {
                                    trace("release() X already released on the other");
                                    return;
                                }
                                monitorexit(this.this$0.mStateLock);
                                this.this$0.releaseInternal();
                                final Object mStateLock = this.this$0.mStateLock;
                                synchronized (this.this$0.mStateLock) {
                                    this.this$0.changeTo(State.RELEASED);
                                }
                            }
                        }
                    });
                    break Label_0204;
                }
                synchronized (this.mStateLock) {
                    this.changeTo(State.RELEASED);
                    monitorexit(this.mStateLock);
                    trace("release() X success");
                    return true;
                }
            }
        }
    }
    
    protected void releaseInternal() {
        this.mRecorder.release();
    }
    
    @Override
    public void resume() throws RecorderException {
        trace("resume() E");
        synchronized (this.mStateLock) {
            if (!this.verifyState(State.PAUSED)) {
                trace("resume() X failed : illegal state");
                final StringBuilder sb = new StringBuilder();
                sb.append("Fail to verify state. state:");
                sb.append(this.mState.name());
                throw new RecorderException(sb.toString());
            }
            this.changeTo(State.RECORDING);
            this.executeInBackground(new ResumeTask());
            monitorexit(this.mStateLock);
            trace("resume() X");
        }
    }
    
    protected boolean resumeInternal() {
        trace("resumeInternal() E");
        try {
            this.mRecorder.resume();
            if (!this.mIsAdjustRecordingTimeByRecorderNotification) {
                this.mReferenceClock.resume();
            }
            trace("resumeInternal() X");
            return true;
        }
        catch (final RuntimeException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("resumeInternal() X failed : ");
            sb.append(ex.getMessage());
            trace(sb.toString());
            this.mRecorder.reset();
            return false;
        }
    }
    
    @Override
    public void setLocation(final Location location) {
        this.mRecorder.setLocation(location);
    }
    
    @Override
    public void setMaxDurationMillis(final long maxDurationMillis) {
        this.mRecorder.setMaxDurationMillis(maxDurationMillis);
    }
    
    @Override
    public void setMaxFileSizeBytes(final long maxFileSizeBytes) {
        this.mRecorder.setMaxFileSizeBytes(maxFileSizeBytes);
    }
    
    @Override
    public void setOrientationHint(final int orientationHint) {
        this.mRecorder.setOrientationHint(orientationHint);
    }
    
    @Override
    public void setOutputFilePath(final String outputFilePath) {
        this.mRecorder.setOutputFilePath(outputFilePath);
    }
    
    @Override
    public void setStorageWriteNotifier(final Storage.StorageWriteNotifier mStorageWriteNotifier) {
        this.mStorageWriteNotifier = mStorageWriteNotifier;
    }
    
    @Override
    public void setUserSoundSetting(final boolean mIsUserSoundSettingOn) {
        this.mIsUserSoundSettingOn = mIsUserSoundSettingOn;
    }
    
    @Override
    public void start() throws RecorderException {
        trace("start() E");
        synchronized (this.mStateLock) {
            if (!this.verifyState(State.PREPARED)) {
                trace("start() X failed : illegal state");
                final StringBuilder sb = new StringBuilder();
                sb.append("Fail to verify state. state:");
                sb.append(this.mState.name());
                throw new RecorderException(sb.toString());
            }
            this.playStartSound();
            this.changeTo(State.STARTING);
            this.executeInBackground(new StartTask());
            monitorexit(this.mStateLock);
            trace("start() X");
        }
    }
    
    protected boolean startInternal() throws TimeoutException {
        trace("startInternal() E");
        this.mWaitUntilWriting = new CountDownLatch(1);
        try {
            this.mRecorder.start();
            this.mCallbackHandler.post((Runnable)new NotifyProgressTask(0L));
            if (this.mIsAdjustRecordingTimeByRecorderNotification) {
                this.mReferenceClock.reset(0L);
            }
            else {
                this.mReferenceClock.start();
            }
            trace("startInternal() X");
            return true;
        }
        catch (final IllegalStateException | IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("startInternal() X failed : ");
            sb.append(((Throwable)ex).getMessage());
            trace(sb.toString());
            this.changeTo(State.RELEASED);
            this.mRecorder.reset();
            return false;
        }
    }
    
    @Override
    public void stop() throws RecorderException {
        trace("stop() E");
        synchronized (this.mStateLock) {
            if (!this.verifyState(State.STARTING, State.RECORDING, State.PAUSED)) {
                trace("stop() X failed : illegal state");
                final StringBuilder sb = new StringBuilder();
                sb.append("Fail to verify state. state:");
                sb.append(this.mState.name());
                throw new RecorderException(sb.toString());
            }
            this.changeTo(State.STOPPING);
            this.executeInBackground(new StopTask());
            monitorexit(this.mStateLock);
            trace("stop() X");
        }
    }
    
    @Override
    public void stopAudioRecording() {
        this.getRecorder().stopAudioRecording();
    }
    
    protected boolean stopInternal() {
        trace("stopInternal() E");
        Label_0018: {
            if (this.mStorageWriteNotifier == null) {
                break Label_0018;
            }
            this.mStorageWriteNotifier = null;
            try {
                try {
                    this.waitUntilFirstVideoFrameWritten();
                    this.mReferenceClock.stop();
                    if (this.mIsCameraErrorDetected) {
                        this.mRecorder.stopOnCameraError();
                    }
                    else {
                        this.mRecorder.stop();
                    }
                    this.mRecorder.reset();
                    trace("stopInternal() X");
                    return true;
                }
                finally {}
            }
            catch (final RuntimeException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("stopInternal() X failed : ");
                sb.append(ex.getMessage());
                trace(sb.toString());
                this.mRecorder.reset();
                return false;
            }
        }
        this.mRecorder.reset();
    }
    
    @Override
    public void stopOnCameraError() throws RecorderException {
        trace("stopOnCameraError() E");
        this.mIsCameraErrorDetected = true;
        this.stop();
        trace("stopOnCameraError() X");
    }
    
    protected boolean verifyState(final State... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == this.mState) {
                return true;
            }
        }
        return false;
    }
    
    protected void waitUntilFirstVideoFrameWritten() {
        trace("waitUntilFirstVideoFrameWritten() E");
        try {
            if (this.mWaitUntilWriting != null) {
                if (!this.mWaitUntilWriting.await(this.mMinDurationMillis, TimeUnit.MILLISECONDS)) {
                    trace("waitUntilFirstVideoFrameWritten() timed-out");
                }
            }
            else {
                Thread.sleep(this.mMinDurationMillis, 0);
            }
        }
        catch (final InterruptedException ex) {
            trace("waitUntilFirstVideoFrameWritten() interrupted at mWaitUntilWriting.await()");
        }
        trace("waitUntilFirstVideoFrameWritten() X");
    }
    
    private class NotifyFinishResult implements Runnable
    {
        private final Result mResult;
        final BaseRecorderController this$0;
        
        public NotifyFinishResult(final BaseRecorderController this$0, final Result mResult) {
            this.this$0 = this$0;
            this.mResult = mResult;
        }
        
        @Override
        public void run() {
            final StringBuilder sb = new StringBuilder();
            sb.append("notifyFinishResult() E result:");
            sb.append(this.mResult.name());
            trace(sb.toString());
            switch (BaseRecorderController$8.$SwitchMap$com$sonyericsson$android$camera$recorder$RecorderController$Result[this.mResult.ordinal()]) {
                case 3:
                case 4: {
                    this.this$0.mListener.onRecordFinished(this.mResult);
                    break;
                }
                case 1:
                case 2: {
                    synchronized (this.this$0.mStateLock) {
                        final boolean verifyState = this.this$0.verifyState(State.STOPPING, State.RELEASING, State.RELEASED);
                        monitorexit(this.this$0.mStateLock);
                        if (verifyState) {
                            this.this$0.mListener.onRecordFinished(this.mResult);
                        }
                        final Object mStateLock = this.this$0.mStateLock;
                        synchronized (this.this$0.mStateLock) {
                            this.this$0.mReferenceClock.reset(Math.max(this.this$0.mReferenceClock.elapsedTimeMillis(), this.this$0.mLastNotifyDurationMillis));
                            if (!this.this$0.verifyState(State.RELEASING, State.RELEASED)) {
                                this.this$0.changeTo(State.IDLE);
                            }
                        }
                    }
                    break;
                }
            }
            trace("notifyFinishResult() X");
        }
    }
    
    private class NotifyProgressTask implements Runnable
    {
        private final long mRecordingTimeMillis;
        final BaseRecorderController this$0;
        
        public NotifyProgressTask(final BaseRecorderController this$0, final long mRecordingTimeMillis) {
            this.this$0 = this$0;
            this.mRecordingTimeMillis = mRecordingTimeMillis;
        }
        
        @Override
        public void run() {
            if (this.this$0.mStorageWriteNotifier != null) {
                this.this$0.mStorageWriteNotifier.notifyWriteStorage();
            }
            this.this$0.mListener.onRecordProgress(this.mRecordingTimeMillis);
        }
    }
    
    private class OnErrorTask implements Runnable
    {
        final BaseRecorderController this$0;
        
        private OnErrorTask(final BaseRecorderController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            trace("onError() E");
            synchronized (this.this$0.mStateLock) {
                if (this.this$0.verifyState(State.IDLE, State.RELEASING)) {
                    return;
                }
                monitorexit(this.this$0.mStateLock);
                this.this$0.playStopSound();
                this.this$0.mListener.onRecordError(0, 0);
                trace("onError() X");
            }
        }
    }
    
    private class PauseTask implements Runnable
    {
        final BaseRecorderController this$0;
        
        private PauseTask(final BaseRecorderController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0.mStateLock) {
                if (this.this$0.verifyState(State.RELEASING, State.RELEASED)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Fail to verify state in PauseTask. state:");
                    sb.append(this.this$0.mState.name());
                    trace(sb.toString());
                    return;
                }
                monitorexit(this.this$0.mStateLock);
                if (!this.this$0.pauseInternal()) {
                    this.this$0.notifyError();
                }
            }
        }
    }
    
    private class PrepareTask implements Runnable
    {
        private final RecorderParameters mParameters;
        final BaseRecorderController this$0;
        
        public PrepareTask(final BaseRecorderController this$0, final RecorderParameters mParameters) {
            this.this$0 = this$0;
            this.mParameters = mParameters;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0.mStateLock) {
                if (this.this$0.verifyState(State.RELEASING, State.RELEASED)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Fail to verify state in PrepareTask. state:");
                    sb.append(this.this$0.mState.name());
                    trace(sb.toString());
                    return;
                }
                monitorexit(this.this$0.mStateLock);
                if (!this.this$0.prepareInternal(this.mParameters)) {
                    this.this$0.notifyError();
                }
            }
        }
    }
    
    private class ResumeTask implements Runnable
    {
        final BaseRecorderController this$0;
        
        private ResumeTask(final BaseRecorderController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0.mStateLock) {
                if (this.this$0.verifyState(State.RELEASING, State.RELEASED)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Fail to verify state in ResumeTask. state:");
                    sb.append(this.this$0.mState.name());
                    trace(sb.toString());
                    return;
                }
                monitorexit(this.this$0.mStateLock);
                if (!this.this$0.resumeInternal()) {
                    this.this$0.notifyError();
                }
            }
        }
    }
    
    private class StartTask implements Runnable
    {
        final BaseRecorderController this$0;
        
        private StartTask(final BaseRecorderController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            final Object mStateLock = this.this$0.mStateLock;
            synchronized (mStateLock) {
                if (this.this$0.verifyState(State.RELEASING, State.RELEASED)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Fail to verify state in StartTask. state:");
                    sb.append(this.this$0.mState.name());
                    trace(sb.toString());
                    return;
                }
                monitorexit(mStateLock);
                if (this.this$0.mShouldWaitStartSound) {
                    try {
                        Thread.sleep(300L);
                    }
                    catch (final InterruptedException mStateLock) {
                        CamLog.w("StartTask interrupted");
                    }
                }
                try {
                    if (!this.this$0.startInternal()) {
                        this.this$0.notifyError();
                    }
                }
                catch (final TimeoutException mStateLock) {
                    if (CamLog.DEBUG) {
                        throw new RuntimeException((Throwable)mStateLock);
                    }
                    this.this$0.notifyError();
                }
                synchronized (this.this$0.mStateLock) {
                    if (this.this$0.verifyState(State.STARTING)) {
                        this.this$0.changeTo(State.RECORDING);
                    }
                }
            }
        }
    }
    
    protected enum State
    {
        private static final State[] $VALUES;
        
        IDLE, 
        PAUSED, 
        PREPARED, 
        RECORDING, 
        RELEASED, 
        RELEASING, 
        STARTING, 
        STOPPING;
        
        static {
            $VALUES = new State[] { State.IDLE, State.PREPARED, State.STARTING, State.RECORDING, State.PAUSED, State.STOPPING, State.RELEASING, State.RELEASED };
        }
    }
    
    private class StopTask implements Runnable
    {
        final BaseRecorderController this$0;
        
        private StopTask(final BaseRecorderController this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            synchronized (this.this$0.mStateLock) {
                if (this.this$0.verifyState(State.RELEASED)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Fail to verify state in StopTask. state:");
                    sb.append(this.this$0.mState.name());
                    trace(sb.toString());
                    return;
                }
                monitorexit(this.this$0.mStateLock);
                if (!this.this$0.mIsMicrophoneEnabled) {
                    this.this$0.playStopSound();
                }
                final boolean stopInternal = this.this$0.stopInternal();
                this.this$0.playStopSound();
                synchronized (this.this$0.mStateLock) {
                    final BaseRecorderController this$0 = this.this$0;
                    Result result;
                    if (stopInternal) {
                        result = Result.SUCCESS;
                    }
                    else {
                        result = Result.FAIL;
                    }
                    this$0.notifyFinishResult(result);
                }
            }
        }
    }
}
