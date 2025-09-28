package com.sonymobile.android.media.internal;

import android.media.CamcorderProfile;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.cameracommon.intent.IntentConstants;
import com.sonymobile.android.media.internal.MediaMuxerWrapper;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SomcMediaRecorder implements MediaMuxerWrapper.MuxerListener {
    public static final long DEFAULT_MAX_FILE_SIZE = 256000000000L;
    public static final int MEDIA_RECORDER_INFO_MASK = 268435455;
    private static final int MIN_DURATION_MS = 100;
    public static final int MSG_CODEC_NOTIFY = 1;
    public static final int MSG_DURATION_REACHED = 2;
    public static final int MSG_FILE_SIZE_REACHED = 3;
    public static final int MSG_MALFORMED_OUTPUT = 4;
    private static final String TAG = "SomcMediaRecorder";
    private final HandlerThread mAudioCodecThread;
    private AudioTrack mAudioTrack;
    private final Handler mCallback;
    private ClockInterface mClock;
    private final EventHandler mEventHandler;
    private final HandlerThread mEventThread;
    private FileDescriptor mFd;
    private final HandlerThread mMuxerThread;
    private MediaMuxerWrapper mMuxerWrapper;
    private int mOutputFormat;
    private String mPath;
    private CountDownLatch mStopLatch;
    private final HandlerThread mVideoCodecThread;
    private VideoTrack mVideoTrack;
    private long mMuxerStartPerformanceTimeMs = 0;
    private long mMuxerStopPerformanceTimeMs = 0;
    private int mVideoFrameRate = 0;
    private double mCaptureRate = 0.0d;
    private int mOrientationHint = -1;
    private float mLongitude = -360.0f;
    private float mLatitude = -360.0f;
    private int mMaxDurationMs = -1;
    private long mMaxFileSize = -1;
    private States mState = States.INITIAL;
    private boolean mVideoSourceSet = false;
    private boolean mMuxerVideoTrackSet = false;
    private boolean mMuxerAudioTrackSet = false;
    private boolean mMuxerRunning = false;
    private int mVideoSource = 0;
    private boolean mRecordingPaused = false;
    private int mLatestProgressTimeMs = 0;
    private boolean mIntelligentActiveEnabled = false;
    private boolean mVideoOnly = true;
    private int mRequestProgressInfoInterval = -1;
    private final Object mStateLock = new Object();
    private final HandlerThread mSomcMediaRecorderThread = new HandlerThread(TAG, -1);

    private enum States {
        INITIAL,
        INITIALIZED,
        DATA_SOURCE_CONFIGURED,
        PREPARED,
        RECORDING,
        STOPPING,
        RELEASED,
        ERROR
    }

    public static int getAudioSourceMax() {
        return 8;
    }

    public SomcMediaRecorder(Handler handler) {
        this.mSomcMediaRecorderThread.start();
        this.mEventHandler = new EventHandler(new WeakReference(this), this.mSomcMediaRecorderThread.getLooper());
        this.mAudioCodecThread = new HandlerThread("AudioCodecThread", -1);
        this.mAudioCodecThread.start();
        this.mVideoCodecThread = new HandlerThread("VideoCodecThread", -1);
        this.mVideoCodecThread.start();
        this.mEventThread = new HandlerThread("EventThread", -1);
        this.mEventThread.start();
        this.mMuxerThread = new HandlerThread("MuxerThread", -1);
        this.mMuxerThread.start();
        this.mCallback = handler;
    }

    public void stopAudioRecording() {
        this.mAudioTrack.stopAudioRecording();
    }

    @Override // com.sonymobile.android.media.internal.MediaMuxerWrapper.MuxerListener
    public void onMaxDurationReached() {
        this.mCallback.obtainMessage(2).sendToTarget();
    }

    @Override // com.sonymobile.android.media.internal.MediaMuxerWrapper.MuxerListener
    public void onMaxFileSizeReached() {
        this.mCallback.obtainMessage(3).sendToTarget();
    }

    @Override // com.sonymobile.android.media.internal.MediaMuxerWrapper.MuxerListener
    public void onStopError() {
        this.mCallback.obtainMessage(4).sendToTarget();
    }

    @Override // com.sonymobile.android.media.internal.MediaMuxerWrapper.MuxerListener
    public void onWriteError() {
        Log.e(TAG, "error while writing to muxer, assume OS file limit reached");
        this.mCallback.obtainMessage(3).sendToTarget();
        closeRecordingOnError();
    }

    private void closeRecordingOnError() {
        if (this.mMuxerRunning) {
            muxerTrackStopped(true);
            muxerTrackStopped(false);
            this.mVideoTrack.stop();
            this.mAudioTrack.stop();
        }
    }

    @Override // com.sonymobile.android.media.internal.MediaMuxerWrapper.MuxerListener
    public void onInfo(int i, int i2) {
        reportInfo(i, i2);
    }

    public Surface getSurface() {
        Surface surface;
        synchronized (this.mStateLock) {
            if (this.mState != States.PREPARED && this.mState != States.RECORDING) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (this.mVideoSourceSet && this.mVideoSource == 2) {
                surface = this.mVideoTrack.getSurface();
            }
            throw new IllegalStateException("Video source not set to VideoSource.SURFACE");
        }
        return surface;
    }

    public void setInputSurface(@NonNull Surface surface) {
        synchronized (this.mStateLock) {
            if (this.mState != States.PREPARED && this.mState != States.RECORDING && this.mState != States.RELEASED && this.mState != States.ERROR) {
                this.mVideoTrack.setInputSurface(surface);
            }
            throw new IllegalStateException("Incorrect state " + this.mState.name());
        }
    }

    public void setAudioSource(int i) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.INITIALIZED && this.mState != States.INITIAL) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (this.mState == States.INITIAL) {
                this.mState = States.INITIALIZED;
            }
            this.mVideoOnly = false;
            createAudioTrack(i, this.mVideoOnly);
        }
    }

    private void createAudioTrack(int i, boolean z) {
        this.mAudioTrack = new AudioTrack(i, this.mEventHandler, this.mAudioCodecThread, this.mEventThread, this.mMuxerThread, !z);
    }

    public void setVideoSource(int i) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.INITIALIZED && this.mState != States.INITIAL) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (i == 1) {
                throw new IllegalArgumentException("VideoSource.CAMERA not supported");
            }
            this.mVideoSource = i;
            this.mVideoTrack = new VideoTrack(this.mEventHandler, this.mVideoCodecThread, this.mEventThread, this.mMuxerThread);
            this.mVideoSourceSet = true;
            if (this.mState == States.INITIAL) {
                this.mState = States.INITIALIZED;
            }
        }
    }

    public void setProfile(CamcorderProfile camcorderProfile) throws IllegalStateException {
        Log.d(TAG, "setProfile");
        if (camcorderProfile != null) {
            setOutputFormat(camcorderProfile.fileFormat);
            setVideoFrameRate(camcorderProfile.videoFrameRate);
            setVideoSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
            setVideoEncodingBitRate(camcorderProfile.videoBitRate);
            setVideoEncoder(camcorderProfile.videoCodec);
            if (camcorderProfile.quality < 1000 || camcorderProfile.quality > 1007) {
                setAudioEncodingBitRate(camcorderProfile.audioBitRate);
                setAudioChannels(camcorderProfile.audioChannels);
                setAudioSamplingRate(camcorderProfile.audioSampleRate);
                setAudioEncoder(camcorderProfile.audioCodec);
            }
        }
    }

    public void setOrientationHint(int i) {
        Log.d(TAG, "setOrientation:" + i);
        synchronized (this.mStateLock) {
            if (this.mState != States.INITIAL && this.mState != States.INITIALIZED && this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (i != 0 && i != 90 && i != 180 && i != 270) {
                throw new IllegalArgumentException("Unsupported angle: " + i);
            }
            this.mOrientationHint = i;
        }
    }

    public void setLocation(float f, float f2) {
        Log.d(TAG, "setLocation lat:" + f + " long:" + f2);
        int i = (int) (((double) (f * 10000.0f)) + 0.5d);
        int i2 = (int) (((double) (10000.0f * f2)) + 0.5d);
        if (i > 900000 || i < -900000) {
            throw new IllegalArgumentException("Latitude: " + f + " out of range.");
        }
        if (i2 > 1800000 || i2 < -1800000) {
            throw new IllegalArgumentException("Longitude: " + f2 + " out of range");
        }
        this.mLatitude = f;
        this.mLongitude = f2;
    }

    public void setOutputFormat(int i) throws IllegalStateException {
        Log.d(TAG, "setOutputFormat:" + i);
        synchronized (this.mStateLock) {
            if (this.mState != States.INITIALIZED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (!this.mVideoSourceSet) {
                throw new IllegalStateException("No video source set");
            }
            if (this.mVideoOnly) {
                createAudioTrack(0, true);
            }
            this.mOutputFormat = i;
            this.mState = States.DATA_SOURCE_CONFIGURED;
        }
    }

    public void setVideoSize(int i, int i2) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mVideoTrack.setVideoSize(i, i2);
        }
    }

    public void setVideoFrameRate(int i) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mVideoTrack.setFrameRate(i);
            this.mVideoFrameRate = i;
        }
    }

    public void setCaptureRate(double d) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mCaptureRate = d;
        }
    }

    public void setVideoEncodingProfileLevel(int i, int i2) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (i >= 0 && i2 >= 0) {
                this.mVideoTrack.setEncodingProfileLevel(i, i2);
            }
            throw new IllegalArgumentException("Video encoding bit rate is not positive");
        }
    }

    public void setVideoBitRateMode(int i) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mVideoTrack.setBitRateMode(i);
        }
    }

    public void setVideoColorAspects(int i, int i2, int i3) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mVideoTrack.setColorAspects(i, i2, i3);
        }
    }

    public void setMaxDuration(int i) throws IllegalArgumentException {
        Log.d(TAG, "setMaxDuration:" + i + " ms");
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (i > 0 && i < 100) {
                throw new IllegalArgumentException("Minmimum duration too short");
            }
            this.mMaxDurationMs = i;
        }
    }

    public void setMaxFileSize(long j) throws IllegalArgumentException {
        Log.d(TAG, "setMaxFileSize:" + j);
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mMaxFileSize = Math.min(j, 256000000000L);
        }
    }

    public void setAudioEncoder(int i) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mAudioTrack.setAudioEncoder(i);
        }
    }

    public void setVideoEncoder(int i) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mVideoTrack.setVideoEncoder(i);
        }
    }

    public void setAudioSamplingRate(int i) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (i <= 0) {
                throw new IllegalArgumentException("Audio sampling rate is not positive");
            }
            this.mAudioTrack.setAudioSamplingRate(i);
        }
    }

    public void setAudioChannels(int i) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (i <= 0) {
                throw new IllegalArgumentException("Number of channels is not positive");
            }
            this.mAudioTrack.setAudioChannels(i);
        }
    }

    public void setAudioEncodingBitRate(int i) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (i <= 0) {
                throw new IllegalArgumentException("Audio encoding bit rate is not positive");
            }
            this.mAudioTrack.setAudioBitRate(i);
        }
    }

    public void setVideoEncodingBitRate(int i) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (i <= 0) {
                throw new IllegalArgumentException("Video encoding bit rate is not positive");
            }
            this.mVideoTrack.setEncodingBitrate(i);
        }
    }

    public void setOutputFile(FileDescriptor fileDescriptor) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mFd = fileDescriptor;
        }
    }

    public void setOutputFile(String str) throws IllegalStateException {
        Log.d(TAG, "setOutputFile:" + str);
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mPath = str;
        }
    }

    public void requestProgressInfo(int i) {
        Log.d(TAG, "requestProgressInfo:" + i);
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mRequestProgressInfoInterval = i;
        }
    }

    public void pause() throws IllegalStateException {
        Log.d(TAG, IntentConstants.BroadcastIntent.MUSICSERVICE_COMMAND_PAUSE);
        synchronized (this.mStateLock) {
            if (this.mState != States.RECORDING) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (!this.mRecordingPaused) {
                this.mClock.pauseClock();
                CountDownLatch countDownLatch = new CountDownLatch(2);
                this.mVideoTrack.pause(countDownLatch, this.mIntelligentActiveEnabled);
                this.mAudioTrack.pause(countDownLatch, this.mIntelligentActiveEnabled);
                try {
                    countDownLatch.await();
                } catch (InterruptedException unused) {
                    Log.d(TAG, "wait for pause was interrupted");
                }
                this.mRecordingPaused = true;
            }
        }
    }

    public void prepare() throws IllegalStateException, IOException {
        Log.d(TAG, CameraActivity.INTENT_SUBJECT_PREPARE);
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (this.mPath == null && this.mFd == null) {
                throw new IOException("No valid output file");
            }
            this.mState = States.PREPARED;
            this.mClock = this.mAudioTrack;
            try {
                if (this.mFd != null) {
                    this.mMuxerWrapper = new MediaMuxerWrapper(this.mPath, this.mFd, this.mOutputFormat, this);
                } else {
                    this.mMuxerWrapper = new MediaMuxerWrapper(this.mPath, this.mOutputFormat, this);
                }
                this.mVideoTrack.setMediaMuxer(this.mMuxerWrapper);
                this.mAudioTrack.setMediaMuxer(this.mMuxerWrapper);
                if (this.mIntelligentActiveEnabled) {
                    this.mVideoTrack.setOperatingRate(120);
                    this.mAudioTrack.setOperatingRate(0);
                } else if (this.mCaptureRate > 0.0d) {
                    if (this.mCaptureRate > this.mVideoFrameRate) {
                        this.mVideoTrack.setOperatingRate((int) this.mCaptureRate);
                        this.mAudioTrack.setOperatingRate((int) this.mCaptureRate);
                    } else if (this.mCaptureRate < this.mVideoFrameRate) {
                        this.mVideoTrack.setOperatingRate((int) this.mCaptureRate);
                    }
                }
                this.mMuxerWrapper.setRequestProgressInfoInterval(this.mRequestProgressInfoInterval);
                this.mVideoTrack.setClock(this.mClock);
                this.mAudioTrack.setClock(this.mClock);
                this.mVideoTrack.prepare();
                this.mAudioTrack.prepare();
            } catch (IOException | IllegalArgumentException unused) {
                throw new IOException("Invalid input parameters");
            }
        }
        if (this.mOrientationHint >= 0) {
            this.mMuxerWrapper.setOrientationHint(this.mOrientationHint);
        }
        if (this.mLatitude > -360.0f && this.mLongitude > -360.0f) {
            this.mMuxerWrapper.setLocation(this.mLatitude, this.mLongitude);
        }
        if (this.mMaxDurationMs >= 0) {
            this.mMuxerWrapper.setMaxDuration(this.mMaxDurationMs);
        }
        if (this.mMaxFileSize >= 0) {
            this.mMuxerWrapper.setMaxFileSize(this.mMaxFileSize);
        }
    }

    public void start() throws IllegalStateException {
        Log.d(TAG, CameraActivity.INTENT_SUBJECT_START);
        synchronized (this.mStateLock) {
            if (this.mState != States.PREPARED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mState = States.RECORDING;
            this.mClock.setStartTime();
            this.mAudioTrack.start();
            this.mVideoTrack.start();
        }
    }

    public void stopAsync() throws IllegalStateException {
        Log.d(TAG, "stopAsync");
        synchronized (this.mStateLock) {
            if (this.mState != States.RECORDING) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mState = States.STOPPING;
            if (this.mMuxerRunning) {
                this.mStopLatch = new CountDownLatch(1);
            }
            this.mClock.stopClock();
            this.mAudioTrack.stop();
            this.mVideoTrack.stop();
        }
    }

    public void stop() throws IllegalStateException {
        Log.d(TAG, "stop");
        synchronized (this.mStateLock) {
            if (this.mState != States.RECORDING) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mState = States.STOPPING;
            if (this.mMuxerRunning) {
                this.mStopLatch = new CountDownLatch(1);
            }
            this.mClock.stopClock();
            this.mAudioTrack.stop();
            this.mVideoTrack.stop();
            waitUntilStopCompleted();
        }
    }

    public void waitUntilStopCompleted() {
        Log.d(TAG, "waitUntilStopCompleted");
        synchronized (this.mStateLock) {
            if (this.mState != States.STOPPING) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (this.mStopLatch != null) {
                try {
                    this.mStopLatch.await();
                } catch (InterruptedException unused) {
                    Log.e(TAG, "wait for stop was interrupted");
                }
            }
            this.mMuxerStartPerformanceTimeMs = 0L;
            this.mMuxerStopPerformanceTimeMs = 0L;
            this.mVideoFrameRate = 0;
            this.mCaptureRate = 0.0d;
            this.mOrientationHint = -1;
            this.mLongitude = -360.0f;
            this.mLatitude = -360.0f;
            this.mMaxDurationMs = -1;
            this.mMaxFileSize = -1L;
            this.mVideoSourceSet = false;
            this.mLatestProgressTimeMs = 0;
            this.mRequestProgressInfoInterval = -1;
            this.mStopLatch = null;
            this.mVideoOnly = true;
            this.mRecordingPaused = false;
            this.mState = States.INITIAL;
        }
    }

    public void stopOnCameraError() throws IllegalStateException {
        Log.d(TAG, "stopOnCameraError");
        closeRecordingOnError();
        stop();
    }

    public void reset() {
        Log.d(TAG, "reset");
        synchronized (this.mStateLock) {
            if (this.mState == States.RELEASED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            this.mState = States.INITIAL;
            if (this.mMuxerWrapper != null) {
                if (this.mMuxerRunning) {
                    this.mMuxerWrapper.stop();
                    this.mAudioTrack.setMediaMuxerStopped();
                    this.mVideoTrack.setMediaMuxerStopped();
                    this.mMuxerRunning = false;
                }
                this.mMuxerWrapper.release();
                this.mMuxerWrapper = null;
            }
            if (this.mVideoTrack != null) {
                this.mVideoTrack.reset();
            }
            if (this.mAudioTrack != null) {
                this.mAudioTrack.reset();
            }
            if (this.mClock != null) {
                this.mClock.resetClock();
            }
            this.mMuxerAudioTrackSet = false;
            this.mMuxerVideoTrackSet = false;
            this.mMuxerStartPerformanceTimeMs = 0L;
            this.mMuxerStopPerformanceTimeMs = 0L;
            this.mVideoFrameRate = 0;
            this.mCaptureRate = 0.0d;
            this.mOrientationHint = -1;
            this.mLongitude = -360.0f;
            this.mLatitude = -360.0f;
            this.mMaxDurationMs = -1;
            this.mMaxFileSize = -1L;
            this.mVideoSourceSet = false;
            this.mLatestProgressTimeMs = 0;
            this.mRequestProgressInfoInterval = -1;
            this.mStopLatch = null;
            this.mVideoOnly = true;
            this.mRecordingPaused = false;
        }
    }

    public void resume() throws IllegalStateException {
        Log.d(TAG, "resume");
        synchronized (this.mStateLock) {
            if (this.mState != States.RECORDING) {
                throw new IllegalStateException("Incorrect state " + this.mState.name());
            }
            if (this.mRecordingPaused) {
                this.mClock.resumeClock();
                CountDownLatch countDownLatch = new CountDownLatch(2);
                this.mVideoTrack.resume(countDownLatch);
                this.mAudioTrack.resume(countDownLatch);
                try {
                    countDownLatch.await();
                } catch (InterruptedException unused) {
                    Log.e(TAG, "wait for resume was interrupted");
                }
                this.mRecordingPaused = false;
            }
        }
    }

    public void release() {
        Log.d(TAG, "release");
        synchronized (this.mStateLock) {
            if (this.mState == States.RELEASED) {
                return;
            }
            if (this.mState != States.INITIAL) {
                throw new IllegalStateException("Incorrect state " + this.mState.name() + ", call reset() before release()");
            }
            if (this.mVideoTrack != null) {
                this.mVideoTrack.release();
            }
            if (this.mAudioTrack != null) {
                this.mAudioTrack.release();
            }
            if (this.mMuxerWrapper != null) {
                this.mMuxerWrapper.release();
            }
            this.mEventThread.quitSafely();
            this.mAudioCodecThread.quitSafely();
            this.mVideoCodecThread.quitSafely();
            this.mMuxerThread.quitSafely();
            this.mSomcMediaRecorderThread.quitSafely();
            this.mState = States.RELEASED;
        }
    }

    public void useIntelligentActive(boolean z) {
        this.mIntelligentActiveEnabled = z;
    }

    public void adjustAudioTimestamp(long j) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name() + " Can only be called in DATA_SOURCE_CONFIGURED");
            }
            this.mAudioTrack.adjustAudioTimeStamp(j);
        }
    }

    public void adjustAudioStartVolume(int i) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                throw new IllegalStateException("Incorrect state " + this.mState.name() + " Can only be called in DATA_SOURCE_CONFIGURED");
            }
            this.mAudioTrack.adjustStartVolume(i);
        }
    }

    public String dump(String str) {
        if (str == null) {
            StringBuilder sb = new StringBuilder("MediaRecorder\n");
            if (this.mPath != null) {
                sb.append("Output file = ");
                sb.append(this.mPath);
                sb.append("\n");
            }
            synchronized (this.mStateLock) {
                sb.append("State = ");
                sb.append(this.mState.name());
                sb.append("\n");
                if (this.mState == States.RECORDING) {
                    if (this.mLatestProgressTimeMs > 0) {
                        sb.append("Recorded ");
                        sb.append(this.mLatestProgressTimeMs);
                        sb.append(" ms\n");
                    }
                    if (this.mPath != null) {
                        File file = new File(this.mPath);
                        if (file.exists()) {
                            sb.append("Written ");
                            sb.append(file.length());
                            sb.append(" bytes to file\n");
                        }
                    }
                }
            }
            return sb.toString();
        }
        if (str.equals("muxer-start-performance-time-ms")) {
            return "" + this.mMuxerStartPerformanceTimeMs;
        }
        if (!str.equals("muxer-stop-performance-time-ms")) {
            return null;
        }
        return "" + this.mMuxerStopPerformanceTimeMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportError(int i) {
        synchronized (this.mStateLock) {
            this.mState = States.ERROR;
            this.mCallback.obtainMessage(1, 4, i).sendToTarget();
        }
    }

    private void reportInfo(int i, int i2) {
        Log.e(TAG, "reportInfo info=" + i + " extra=" + i2);
        this.mCallback.obtainMessage(1, i, i2).sendToTarget();
        this.mLatestProgressTimeMs = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void muxerTrackSet(boolean z) {
        if (z) {
            this.mMuxerVideoTrackSet = true;
        } else {
            this.mMuxerAudioTrackSet = true;
        }
        synchronized (this.mStateLock) {
            if (this.mMuxerAudioTrackSet && this.mMuxerVideoTrackSet) {
                long jNanoTime = System.nanoTime();
                this.mMuxerWrapper.start();
                this.mMuxerStartPerformanceTimeMs = (System.nanoTime() - jNanoTime) / 1000000;
                this.mMuxerRunning = true;
                this.mVideoTrack.setMediaMuxerStarted();
                this.mAudioTrack.setMediaMuxerStarted();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void muxerTrackStopped(boolean z) {
        if (z) {
            this.mMuxerVideoTrackSet = false;
        } else {
            this.mMuxerAudioTrackSet = false;
        }
        if (this.mMuxerAudioTrackSet || this.mMuxerVideoTrackSet) {
            return;
        }
        long jNanoTime = System.nanoTime();
        this.mMuxerWrapper.stop();
        this.mMuxerStopPerformanceTimeMs = (System.nanoTime() - jNanoTime) / 1000000;
        this.mVideoTrack.setMediaMuxerStopped();
        this.mAudioTrack.setMediaMuxerStopped();
        this.mMuxerRunning = false;
        if (this.mStopLatch != null) {
            this.mStopLatch.countDown();
        }
    }

    static class EventHandler extends Handler {
        private final WeakReference<SomcMediaRecorder> mRecorder;

        EventHandler(WeakReference<SomcMediaRecorder> weakReference, Looper looper) {
            super(looper);
            this.mRecorder = weakReference;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SomcMediaRecorder somcMediaRecorder = this.mRecorder.get();
            int i = message.arg1 & SomcMediaRecorder.MEDIA_RECORDER_INFO_MASK;
            int i2 = message.what;
            if (i2 != 1) {
                if (i2 == 101) {
                    somcMediaRecorder.muxerTrackStopped(true);
                    return;
                } else {
                    if (i2 != 103) {
                        return;
                    }
                    somcMediaRecorder.muxerTrackStopped(false);
                    return;
                }
            }
            if (i != 4) {
                switch (i) {
                    case 10:
                        somcMediaRecorder.muxerTrackSet(true);
                        break;
                    case 11:
                        somcMediaRecorder.muxerTrackSet(false);
                        break;
                }
            }
            somcMediaRecorder.reportError(message.arg2);
        }
    }
}
