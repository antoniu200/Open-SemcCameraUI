// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.android.media.internal;

import android.os.Message;
import android.os.Looper;
import android.media.CamcorderProfile;
import android.support.annotation.NonNull;
import java.io.IOException;
import android.view.Surface;
import java.io.File;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.io.FileDescriptor;
import android.os.Handler;
import android.os.HandlerThread;

public class SomcMediaRecorder implements MuxerListener
{
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
    private double mCaptureRate;
    private ClockInterface mClock;
    private final EventHandler mEventHandler;
    private final HandlerThread mEventThread;
    private FileDescriptor mFd;
    private boolean mIntelligentActiveEnabled;
    private int mLatestProgressTimeMs;
    private float mLatitude;
    private float mLongitude;
    private int mMaxDurationMs;
    private long mMaxFileSize;
    private boolean mMuxerAudioTrackSet;
    private boolean mMuxerRunning;
    private long mMuxerStartPerformanceTimeMs;
    private long mMuxerStopPerformanceTimeMs;
    private final HandlerThread mMuxerThread;
    private boolean mMuxerVideoTrackSet;
    private MediaMuxerWrapper mMuxerWrapper;
    private int mOrientationHint;
    private int mOutputFormat;
    private String mPath;
    private boolean mRecordingPaused;
    private int mRequestProgressInfoInterval;
    private final HandlerThread mSomcMediaRecorderThread;
    private States mState;
    private final Object mStateLock;
    private CountDownLatch mStopLatch;
    private final HandlerThread mVideoCodecThread;
    private int mVideoFrameRate;
    private boolean mVideoOnly;
    private int mVideoSource;
    private boolean mVideoSourceSet;
    private VideoTrack mVideoTrack;
    
    public SomcMediaRecorder(final Handler mCallback) {
        this.mMuxerStartPerformanceTimeMs = 0L;
        this.mMuxerStopPerformanceTimeMs = 0L;
        this.mVideoFrameRate = 0;
        this.mCaptureRate = 0.0;
        this.mOrientationHint = -1;
        this.mLongitude = -360.0f;
        this.mLatitude = -360.0f;
        this.mMaxDurationMs = -1;
        this.mMaxFileSize = -1L;
        this.mState = States.INITIAL;
        this.mVideoSourceSet = false;
        this.mMuxerVideoTrackSet = false;
        this.mMuxerAudioTrackSet = false;
        this.mMuxerRunning = false;
        this.mVideoSource = 0;
        this.mRecordingPaused = false;
        this.mLatestProgressTimeMs = 0;
        this.mIntelligentActiveEnabled = false;
        this.mVideoOnly = true;
        this.mRequestProgressInfoInterval = -1;
        this.mStateLock = new Object();
        (this.mSomcMediaRecorderThread = new HandlerThread("SomcMediaRecorder", -1)).start();
        this.mEventHandler = new EventHandler(new WeakReference<SomcMediaRecorder>(this), this.mSomcMediaRecorderThread.getLooper());
        (this.mAudioCodecThread = new HandlerThread("AudioCodecThread", -1)).start();
        (this.mVideoCodecThread = new HandlerThread("VideoCodecThread", -1)).start();
        (this.mEventThread = new HandlerThread("EventThread", -1)).start();
        (this.mMuxerThread = new HandlerThread("MuxerThread", -1)).start();
        this.mCallback = mCallback;
    }
    
    private void closeRecordingOnError() {
        if (this.mMuxerRunning) {
            this.muxerTrackStopped(true);
            this.muxerTrackStopped(false);
            this.mVideoTrack.stop();
            this.mAudioTrack.stop();
        }
    }
    
    private void createAudioTrack(final int n, final boolean b) {
        this.mAudioTrack = new AudioTrack(n, this.mEventHandler, this.mAudioCodecThread, this.mEventThread, this.mMuxerThread, b ^ true);
    }
    
    public static int getAudioSourceMax() {
        return 8;
    }
    
    private void muxerTrackSet(final boolean b) {
        if (b) {
            this.mMuxerVideoTrackSet = true;
        }
        else {
            this.mMuxerAudioTrackSet = true;
        }
        synchronized (this.mStateLock) {
            if (this.mMuxerAudioTrackSet && this.mMuxerVideoTrackSet) {
                final long nanoTime = System.nanoTime();
                this.mMuxerWrapper.start();
                this.mMuxerStartPerformanceTimeMs = (System.nanoTime() - nanoTime) / 1000000L;
                this.mMuxerRunning = true;
                this.mVideoTrack.setMediaMuxerStarted();
                this.mAudioTrack.setMediaMuxerStarted();
            }
        }
    }
    
    private void muxerTrackStopped(final boolean b) {
        if (b) {
            this.mMuxerVideoTrackSet = false;
        }
        else {
            this.mMuxerAudioTrackSet = false;
        }
        if (!this.mMuxerAudioTrackSet && !this.mMuxerVideoTrackSet) {
            final long nanoTime = System.nanoTime();
            this.mMuxerWrapper.stop();
            this.mMuxerStopPerformanceTimeMs = (System.nanoTime() - nanoTime) / 1000000L;
            this.mVideoTrack.setMediaMuxerStopped();
            this.mAudioTrack.setMediaMuxerStopped();
            this.mMuxerRunning = false;
            if (this.mStopLatch != null) {
                this.mStopLatch.countDown();
            }
        }
    }
    
    private void reportError(final int n) {
        synchronized (this.mStateLock) {
            this.mState = States.ERROR;
            this.mCallback.obtainMessage(1, 4, n).sendToTarget();
        }
    }
    
    private void reportInfo(final int i, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("reportInfo info=");
        sb.append(i);
        sb.append(" extra=");
        sb.append(n);
        Log.e("SomcMediaRecorder", sb.toString());
        this.mCallback.obtainMessage(1, i, n).sendToTarget();
        this.mLatestProgressTimeMs = n;
    }
    
    public void adjustAudioStartVolume(final int n) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                sb.append(" Can only be called in DATA_SOURCE_CONFIGURED");
                throw new IllegalStateException(sb.toString());
            }
            this.mAudioTrack.adjustStartVolume(n);
        }
    }
    
    public void adjustAudioTimestamp(final long n) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                sb.append(" Can only be called in DATA_SOURCE_CONFIGURED");
                throw new IllegalStateException(sb.toString());
            }
            this.mAudioTrack.adjustAudioTimeStamp(n);
        }
    }
    
    public String dump(String s) {
        if (s == null) {
            final StringBuilder sb = new StringBuilder("MediaRecorder\n");
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
                        final File file = new File(this.mPath);
                        if (file.exists()) {
                            sb.append("Written ");
                            sb.append(file.length());
                            sb.append(" bytes to file\n");
                        }
                    }
                }
                monitorexit(this.mStateLock);
                s = sb.toString();
                return s;
            }
        }
        if (s.equals("muxer-start-performance-time-ms")) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.mMuxerStartPerformanceTimeMs);
            s = sb2.toString();
        }
        else if (s.equals("muxer-stop-performance-time-ms")) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(this.mMuxerStopPerformanceTimeMs);
            s = sb3.toString();
        }
        else {
            s = null;
        }
        return s;
    }
    
    public Surface getSurface() {
        synchronized (this.mStateLock) {
            if (this.mState != States.PREPARED && this.mState != States.RECORDING) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (this.mVideoSourceSet && this.mVideoSource == 2) {
                return this.mVideoTrack.getSurface();
            }
            throw new IllegalStateException("Video source not set to VideoSource.SURFACE");
        }
    }
    
    @Override
    public void onInfo(final int n, final int n2) {
        this.reportInfo(n, n2);
    }
    
    @Override
    public void onMaxDurationReached() {
        this.mCallback.obtainMessage(2).sendToTarget();
    }
    
    @Override
    public void onMaxFileSizeReached() {
        this.mCallback.obtainMessage(3).sendToTarget();
    }
    
    @Override
    public void onStopError() {
        this.mCallback.obtainMessage(4).sendToTarget();
    }
    
    @Override
    public void onWriteError() {
        Log.e("SomcMediaRecorder", "error while writing to muxer, assume OS file limit reached");
        this.mCallback.obtainMessage(3).sendToTarget();
        this.closeRecordingOnError();
    }
    
    public void pause() throws IllegalStateException {
        Log.d("SomcMediaRecorder", "pause");
        synchronized (this.mStateLock) {
            if (this.mState != States.RECORDING) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (!this.mRecordingPaused) {
                this.mClock.pauseClock();
                final CountDownLatch countDownLatch = new CountDownLatch(2);
                this.mVideoTrack.pause(countDownLatch, this.mIntelligentActiveEnabled);
                this.mAudioTrack.pause(countDownLatch, this.mIntelligentActiveEnabled);
                try {
                    countDownLatch.await();
                }
                catch (final InterruptedException ex) {
                    Log.d("SomcMediaRecorder", "wait for pause was interrupted");
                }
                this.mRecordingPaused = true;
            }
        }
    }
    
    public void prepare() throws IllegalStateException, IOException {
        Log.d("SomcMediaRecorder", "prepare");
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (this.mPath == null && this.mFd == null) {
                throw new IOException("No valid output file");
            }
            this.mState = States.PREPARED;
            this.mClock = this.mAudioTrack;
            try {
                if (this.mFd != null) {
                    this.mMuxerWrapper = new MediaMuxerWrapper(this.mPath, this.mFd, this.mOutputFormat, (MediaMuxerWrapper.MuxerListener)this);
                }
                else {
                    this.mMuxerWrapper = new MediaMuxerWrapper(this.mPath, this.mOutputFormat, (MediaMuxerWrapper.MuxerListener)this);
                }
                this.mVideoTrack.setMediaMuxer(this.mMuxerWrapper);
                this.mAudioTrack.setMediaMuxer(this.mMuxerWrapper);
                if (this.mIntelligentActiveEnabled) {
                    this.mVideoTrack.setOperatingRate(120);
                    this.mAudioTrack.setOperatingRate(0);
                }
                else if (this.mCaptureRate > 0.0) {
                    if (this.mCaptureRate > this.mVideoFrameRate) {
                        this.mVideoTrack.setOperatingRate((int)this.mCaptureRate);
                        this.mAudioTrack.setOperatingRate((int)this.mCaptureRate);
                    }
                    else if (this.mCaptureRate < this.mVideoFrameRate) {
                        this.mVideoTrack.setOperatingRate((int)this.mCaptureRate);
                    }
                }
                this.mMuxerWrapper.setRequestProgressInfoInterval(this.mRequestProgressInfoInterval);
                this.mVideoTrack.setClock(this.mClock);
                this.mAudioTrack.setClock(this.mClock);
                this.mVideoTrack.prepare();
                this.mAudioTrack.prepare();
                monitorexit(this.mStateLock);
                if (this.mOrientationHint >= 0) {
                    this.mMuxerWrapper.setOrientationHint(this.mOrientationHint);
                }
                if (this.mLatitude > -360.0f && this.mLongitude > -360.0f) {
                    this.mMuxerWrapper.setLocation(this.mLatitude, this.mLongitude);
                }
                if (this.mMaxDurationMs >= 0) {
                    this.mMuxerWrapper.setMaxDuration(this.mMaxDurationMs);
                }
                if (this.mMaxFileSize >= 0L) {
                    this.mMuxerWrapper.setMaxFileSize(this.mMaxFileSize);
                }
            }
            catch (final IOException | IllegalArgumentException ex) {
                throw new IOException("Invalid input parameters");
            }
        }
    }
    
    public void release() {
        Log.d("SomcMediaRecorder", "release");
        synchronized (this.mStateLock) {
            if (this.mState == States.RELEASED) {
                return;
            }
            if (this.mState != States.INITIAL) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                sb.append(", call reset() before release()");
                throw new IllegalStateException(sb.toString());
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
    
    public void requestProgressInfo(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("requestProgressInfo:");
        sb.append(n);
        Log.d("SomcMediaRecorder", sb.toString());
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Incorrect state ");
                sb2.append(this.mState.name());
                throw new IllegalStateException(sb2.toString());
            }
            this.mRequestProgressInfoInterval = n;
        }
    }
    
    public void reset() {
        Log.d("SomcMediaRecorder", "reset");
        synchronized (this.mStateLock) {
            if (this.mState == States.RELEASED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
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
            this.mCaptureRate = 0.0;
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
        Log.d("SomcMediaRecorder", "resume");
        synchronized (this.mStateLock) {
            if (this.mState != States.RECORDING) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (this.mRecordingPaused) {
                this.mClock.resumeClock();
                final CountDownLatch countDownLatch = new CountDownLatch(2);
                this.mVideoTrack.resume(countDownLatch);
                this.mAudioTrack.resume(countDownLatch);
                try {
                    countDownLatch.await();
                }
                catch (final InterruptedException ex) {
                    Log.e("SomcMediaRecorder", "wait for resume was interrupted");
                }
                this.mRecordingPaused = false;
            }
        }
    }
    
    public void setAudioChannels(final int audioChannels) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (audioChannels <= 0) {
                throw new IllegalArgumentException("Number of channels is not positive");
            }
            this.mAudioTrack.setAudioChannels(audioChannels);
        }
    }
    
    public void setAudioEncoder(final int audioEncoder) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mAudioTrack.setAudioEncoder(audioEncoder);
        }
    }
    
    public void setAudioEncodingBitRate(final int audioBitRate) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (audioBitRate <= 0) {
                throw new IllegalArgumentException("Audio encoding bit rate is not positive");
            }
            this.mAudioTrack.setAudioBitRate(audioBitRate);
        }
    }
    
    public void setAudioSamplingRate(final int audioSamplingRate) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (audioSamplingRate <= 0) {
                throw new IllegalArgumentException("Audio sampling rate is not positive");
            }
            this.mAudioTrack.setAudioSamplingRate(audioSamplingRate);
        }
    }
    
    public void setAudioSource(final int n) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.INITIALIZED && this.mState != States.INITIAL) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (this.mState == States.INITIAL) {
                this.mState = States.INITIALIZED;
            }
            this.createAudioTrack(n, this.mVideoOnly = false);
        }
    }
    
    public void setCaptureRate(final double mCaptureRate) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mCaptureRate = mCaptureRate;
        }
    }
    
    public void setInputSurface(@NonNull final Surface inputSurface) {
        synchronized (this.mStateLock) {
            if (this.mState != States.PREPARED && this.mState != States.RECORDING && this.mState != States.RELEASED && this.mState != States.ERROR) {
                this.mVideoTrack.setInputSurface(inputSurface);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Incorrect state ");
            sb.append(this.mState.name());
            throw new IllegalStateException(sb.toString());
        }
    }
    
    public void setLocation(final float mLatitude, final float f) {
        final StringBuilder sb = new StringBuilder();
        sb.append("setLocation lat:");
        sb.append(mLatitude);
        sb.append(" long:");
        sb.append(f);
        Log.d("SomcMediaRecorder", sb.toString());
        final int n = (int)(mLatitude * 10000.0f + 0.5);
        final int n2 = (int)(10000.0f * f + 0.5);
        if (n > 900000 || n < -900000) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Latitude: ");
            sb2.append(mLatitude);
            sb2.append(" out of range.");
            throw new IllegalArgumentException(sb2.toString());
        }
        if (n2 <= 1800000 && n2 >= -1800000) {
            this.mLatitude = mLatitude;
            this.mLongitude = f;
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Longitude: ");
        sb3.append(f);
        sb3.append(" out of range");
        throw new IllegalArgumentException(sb3.toString());
    }
    
    public void setMaxDuration(final int n) throws IllegalArgumentException {
        final StringBuilder sb = new StringBuilder();
        sb.append("setMaxDuration:");
        sb.append(n);
        sb.append(" ms");
        Log.d("SomcMediaRecorder", sb.toString());
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Incorrect state ");
                sb2.append(this.mState.name());
                throw new IllegalStateException(sb2.toString());
            }
            if (n > 0 && n < 100) {
                throw new IllegalArgumentException("Minmimum duration too short");
            }
            this.mMaxDurationMs = n;
        }
    }
    
    public void setMaxFileSize(final long n) throws IllegalArgumentException {
        final StringBuilder sb = new StringBuilder();
        sb.append("setMaxFileSize:");
        sb.append(n);
        Log.d("SomcMediaRecorder", sb.toString());
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Incorrect state ");
                sb2.append(this.mState.name());
                throw new IllegalStateException(sb2.toString());
            }
            this.mMaxFileSize = Math.min(n, 256000000000L);
        }
    }
    
    public void setOrientationHint(final int mOrientationHint) {
        final StringBuilder sb = new StringBuilder();
        sb.append("setOrientation:");
        sb.append(mOrientationHint);
        Log.d("SomcMediaRecorder", sb.toString());
        synchronized (this.mStateLock) {
            if (this.mState != States.INITIAL && this.mState != States.INITIALIZED && this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Incorrect state ");
                sb2.append(this.mState.name());
                throw new IllegalStateException(sb2.toString());
            }
            if (mOrientationHint != 0 && mOrientationHint != 90 && mOrientationHint != 180 && mOrientationHint != 270) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Unsupported angle: ");
                sb3.append(mOrientationHint);
                throw new IllegalArgumentException(sb3.toString());
            }
            this.mOrientationHint = mOrientationHint;
        }
    }
    
    public void setOutputFile(final FileDescriptor mFd) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mFd = mFd;
        }
    }
    
    public void setOutputFile(final String s) throws IllegalStateException {
        final StringBuilder sb = new StringBuilder();
        sb.append("setOutputFile:");
        sb.append(s);
        Log.d("SomcMediaRecorder", sb.toString());
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Incorrect state ");
                sb2.append(this.mState.name());
                throw new IllegalStateException(sb2.toString());
            }
            this.mPath = s;
        }
    }
    
    public void setOutputFormat(final int n) throws IllegalStateException {
        final StringBuilder sb = new StringBuilder();
        sb.append("setOutputFormat:");
        sb.append(n);
        Log.d("SomcMediaRecorder", sb.toString());
        synchronized (this.mStateLock) {
            if (this.mState != States.INITIALIZED) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Incorrect state ");
                sb2.append(this.mState.name());
                throw new IllegalStateException(sb2.toString());
            }
            if (!this.mVideoSourceSet) {
                throw new IllegalStateException("No video source set");
            }
            if (this.mVideoOnly) {
                this.createAudioTrack(0, true);
            }
            this.mOutputFormat = n;
            this.mState = States.DATA_SOURCE_CONFIGURED;
        }
    }
    
    public void setProfile(final CamcorderProfile camcorderProfile) {
        Log.d("SomcMediaRecorder", "setProfile");
        if (camcorderProfile != null) {
            this.setOutputFormat(camcorderProfile.fileFormat);
            this.setVideoFrameRate(camcorderProfile.videoFrameRate);
            this.setVideoSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
            this.setVideoEncodingBitRate(camcorderProfile.videoBitRate);
            this.setVideoEncoder(camcorderProfile.videoCodec);
            if (camcorderProfile.quality < 1000 || camcorderProfile.quality > 1007) {
                this.setAudioEncodingBitRate(camcorderProfile.audioBitRate);
                this.setAudioChannels(camcorderProfile.audioChannels);
                this.setAudioSamplingRate(camcorderProfile.audioSampleRate);
                this.setAudioEncoder(camcorderProfile.audioCodec);
            }
        }
    }
    
    public void setVideoBitRateMode(final int bitRateMode) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mVideoTrack.setBitRateMode(bitRateMode);
        }
    }
    
    public void setVideoColorAspects(final int n, final int n2, final int n3) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mVideoTrack.setColorAspects(n, n2, n3);
        }
    }
    
    public void setVideoEncoder(final int videoEncoder) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mVideoTrack.setVideoEncoder(videoEncoder);
        }
    }
    
    public void setVideoEncodingBitRate(final int encodingBitrate) {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (encodingBitrate <= 0) {
                throw new IllegalArgumentException("Video encoding bit rate is not positive");
            }
            this.mVideoTrack.setEncodingBitrate(encodingBitrate);
        }
    }
    
    public void setVideoEncodingProfileLevel(final int n, final int n2) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (n >= 0 && n2 >= 0) {
                this.mVideoTrack.setEncodingProfileLevel(n, n2);
                return;
            }
            throw new IllegalArgumentException("Video encoding bit rate is not positive");
        }
    }
    
    public void setVideoFrameRate(final int n) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mVideoTrack.setFrameRate(n);
            this.mVideoFrameRate = n;
        }
    }
    
    public void setVideoSize(final int n, final int n2) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.DATA_SOURCE_CONFIGURED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mVideoTrack.setVideoSize(n, n2);
        }
    }
    
    public void setVideoSource(final int mVideoSource) throws IllegalStateException {
        synchronized (this.mStateLock) {
            if (this.mState != States.INITIALIZED && this.mState != States.INITIAL) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (mVideoSource == 1) {
                throw new IllegalArgumentException("VideoSource.CAMERA not supported");
            }
            this.mVideoSource = mVideoSource;
            this.mVideoTrack = new VideoTrack(this.mEventHandler, this.mVideoCodecThread, this.mEventThread, this.mMuxerThread);
            this.mVideoSourceSet = true;
            if (this.mState == States.INITIAL) {
                this.mState = States.INITIALIZED;
            }
        }
    }
    
    public void start() throws IllegalStateException {
        Log.d("SomcMediaRecorder", "start");
        synchronized (this.mStateLock) {
            if (this.mState != States.PREPARED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mState = States.RECORDING;
            this.mClock.setStartTime();
            this.mAudioTrack.start();
            this.mVideoTrack.start();
        }
    }
    
    public void stop() throws IllegalStateException {
        Log.d("SomcMediaRecorder", "stop");
        synchronized (this.mStateLock) {
            if (this.mState != States.RECORDING) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            this.mState = States.STOPPING;
            if (this.mMuxerRunning) {
                this.mStopLatch = new CountDownLatch(1);
            }
            this.mClock.stopClock();
            this.mAudioTrack.stop();
            this.mVideoTrack.stop();
            this.waitUntilStopCompleted();
        }
    }
    
    public void stopAsync() throws IllegalStateException {
        Log.d("SomcMediaRecorder", "stopAsync");
        synchronized (this.mStateLock) {
            if (this.mState != States.RECORDING) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
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
    
    public void stopAudioRecording() {
        this.mAudioTrack.stopAudioRecording();
    }
    
    public void stopOnCameraError() {
        Log.d("SomcMediaRecorder", "stopOnCameraError");
        this.closeRecordingOnError();
        this.stop();
    }
    
    public void useIntelligentActive(final boolean mIntelligentActiveEnabled) {
        this.mIntelligentActiveEnabled = mIntelligentActiveEnabled;
    }
    
    public void waitUntilStopCompleted() {
        Log.d("SomcMediaRecorder", "waitUntilStopCompleted");
        synchronized (this.mStateLock) {
            if (this.mState != States.STOPPING) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Incorrect state ");
                sb.append(this.mState.name());
                throw new IllegalStateException(sb.toString());
            }
            if (this.mStopLatch != null) {
                try {
                    this.mStopLatch.await();
                }
                catch (final InterruptedException ex) {
                    Log.e("SomcMediaRecorder", "wait for stop was interrupted");
                }
            }
            this.mMuxerStartPerformanceTimeMs = 0L;
            this.mMuxerStopPerformanceTimeMs = 0L;
            this.mVideoFrameRate = 0;
            this.mCaptureRate = 0.0;
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
    
    static class EventHandler extends Handler
    {
        private final WeakReference<SomcMediaRecorder> mRecorder;
        
        EventHandler(final WeakReference<SomcMediaRecorder> mRecorder, final Looper looper) {
            super(looper);
            this.mRecorder = mRecorder;
        }
        
        public void handleMessage(final Message message) {
            final SomcMediaRecorder somcMediaRecorder = this.mRecorder.get();
            final int n = message.arg1 & 0xFFFFFFF;
            final int what = message.what;
            if (what != 1) {
                if (what != 101) {
                    if (what == 103) {
                        somcMediaRecorder.muxerTrackStopped(false);
                    }
                }
                else {
                    somcMediaRecorder.muxerTrackStopped(true);
                }
            }
            else if (n != 4) {
                switch (n) {
                    case 11: {
                        somcMediaRecorder.muxerTrackSet(false);
                        break;
                    }
                    case 10: {
                        somcMediaRecorder.muxerTrackSet(true);
                        break;
                    }
                }
            }
            else {
                somcMediaRecorder.reportError(message.arg2);
            }
        }
    }
    
    private enum States
    {
        private static final States[] $VALUES;
        
        DATA_SOURCE_CONFIGURED, 
        ERROR, 
        INITIAL, 
        INITIALIZED, 
        PREPARED, 
        RECORDING, 
        RELEASED, 
        STOPPING;
        
        static {
            $VALUES = new States[] { States.INITIAL, States.INITIALIZED, States.DATA_SOURCE_CONFIGURED, States.PREPARED, States.RECORDING, States.STOPPING, States.RELEASED, States.ERROR };
        }
    }
}
