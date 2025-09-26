// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.android.media.internal;

import android.support.annotation.NonNull;
import android.os.Message;
import android.os.Bundle;
import android.media.MediaCodec$BufferInfo;
import android.os.Looper;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.io.IOException;
import android.media.MediaCodec$CodecException;
import android.util.Log;
import java.util.concurrent.LinkedBlockingDeque;
import android.media.MediaCrypto;
import android.media.MediaCodec$Callback;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaCodecList;
import android.os.HandlerThread;
import android.view.Surface;
import android.os.Handler;

class VideoTrack extends Track
{
    private static final int ENCODER_REQUEST_SYNC_FRAME = 0;
    private static final int I_FRAME_INTERVAL = 1;
    static final String TAG = "VideoTrack";
    private static final long TIME_OUT_STOPPING_MILLISECONDS = 1000L;
    private static final long WAIT_FOR_MEDIA_MUXER_START_TIMED_OUT_US = 10000000L;
    private int mBitRateMode;
    private final Handler mCallback;
    private final CodecHandler mCodecHandler;
    private int mColorRange;
    private int mColorStandard;
    private int mColorTransfer;
    protected int mEncodingLevel;
    protected int mEncodingProfile;
    private boolean mFirstCodecConfigFrame;
    private long mFirstVideoFrameTimeUs;
    private int mFrameDropCounter;
    private long mFrameInterval;
    private int mFrameRate;
    private int mHeight;
    private boolean mKeyFrameRequested;
    private long mLastRecordedVideoTimestampUs;
    private final MuxerHandler mMuxerHandler;
    private boolean mPauseResumeFlag;
    private long mRecordedDurationAtStopUs;
    private Surface mSourceSurface;
    private String mVideoMime;
    private long mVideoOffset;
    private boolean mWaitForKeyFrame;
    private int mWidth;
    private final String[] videoMimeTypes;
    
    VideoTrack(final Handler mCallback, final HandlerThread handlerThread, final HandlerThread handlerThread2, final HandlerThread handlerThread3) {
        this.videoMimeTypes = new String[] { "", "video/3gpp", "video/avc", "video/mp4v-es", "video/x-vnd.on2.vp8", "video/hevc", "video/x-vnd.on2.vp9" };
        this.mFrameRate = 30;
        this.mEncodingProfile = 0;
        this.mEncodingLevel = 0;
        this.mBitRateMode = -1;
        this.mColorStandard = -1;
        this.mColorTransfer = -1;
        this.mColorRange = -1;
        this.mFrameInterval = 1000000 / this.mFrameRate;
        this.mFirstVideoFrameTimeUs = -1L;
        this.mRecordedDurationAtStopUs = -1L;
        this.mLastRecordedVideoTimestampUs = 0L;
        this.mWaitForKeyFrame = false;
        this.mVideoOffset = 0L;
        this.mKeyFrameRequested = false;
        this.mPauseResumeFlag = false;
        this.mFirstCodecConfigFrame = true;
        this.mCodecHandler = new CodecHandler(handlerThread.getLooper());
        this.mEventHandler = new EventHandler(handlerThread2.getLooper());
        this.mMuxerHandler = new MuxerHandler(handlerThread3.getLooper());
        this.mHandlerHelper = new HandlerHelper();
        this.mCallback = mCallback;
        this.mState = States.STOPPED;
    }
    
    @Override
    protected void doPause() {
        this.mPauseLatch.countDown();
    }
    
    @Override
    protected void doPrepare() {
        final MediaCodecList list = new MediaCodecList(0);
        final MediaFormat videoFormat = MediaFormat.createVideoFormat(this.mVideoMime, this.mWidth, this.mHeight);
        videoFormat.setInteger("bitrate", this.mEncodingBitRate);
        videoFormat.setInteger("frame-rate", this.mFrameRate);
        videoFormat.setInteger("i-frame-interval", 1);
        if (this.mOperatingRate > 0) {
            videoFormat.setInteger("operating-rate", this.mOperatingRate);
            videoFormat.setString("ts-schema", "android.generic.1+0");
        }
        if (this.mEncodingProfile == 0) {
            if (this.mVideoMime.equals("video/avc")) {
                if (this.mWidth >= 1280 && this.mHeight >= 720) {
                    videoFormat.setInteger("profile", 8);
                }
                else {
                    videoFormat.setInteger("profile", 1);
                }
                videoFormat.setInteger("level", 1);
            }
        }
        else {
            videoFormat.setInteger("profile", this.mEncodingProfile);
            int mEncodingLevel;
            if (this.mEncodingLevel == 0) {
                mEncodingLevel = 1;
            }
            else {
                mEncodingLevel = this.mEncodingLevel;
            }
            videoFormat.setInteger("level", mEncodingLevel);
        }
        videoFormat.setInteger("color-format", 2130708361);
        if (this.mColorStandard >= 0 && this.mColorTransfer >= 0 && this.mColorRange >= 0) {
            videoFormat.setInteger("color-standard", this.mColorStandard);
            videoFormat.setInteger("color-transfer", this.mColorTransfer);
            videoFormat.setInteger("color-range", this.mColorRange);
        }
        else {
            if (this.mWidth >= 1280 && this.mHeight >= 720) {
                videoFormat.setInteger("color-standard", 1);
            }
            else {
                videoFormat.setInteger("color-standard", 4);
            }
            videoFormat.setInteger("color-transfer", 3);
            videoFormat.setInteger("color-range", 2);
        }
        if (this.checkFormat(list, videoFormat, this.mVideoMime)) {
            final String encoderForFormat = list.findEncoderForFormat(videoFormat);
            try {
                (this.mEncoder = MediaCodec.createByCodecName(encoderForFormat)).setCallback((MediaCodec$Callback)new VideoEncoderCallback());
                try {
                    if (this.mBitRateMode >= 0) {
                        videoFormat.setInteger("bitrate-mode", this.mBitRateMode);
                    }
                    this.mEncoder.configure(videoFormat, (Surface)null, (MediaCrypto)null, 1);
                    if (this.mSourceSurface == null) {
                        this.mSourceSurface = this.mEncoder.createInputSurface();
                    }
                    else {
                        this.mEncoder.setInputSurface(this.mSourceSurface);
                    }
                    this.mBufferList = new LinkedBlockingDeque<EncodedBuffer>();
                }
                catch (final MediaCodec$CodecException ex) {
                    Log.e("VideoTrack", "Failed to configure MediaCodec");
                    this.mCallback.obtainMessage(1, 4, 1).sendToTarget();
                    return;
                }
            }
            catch (final IOException | NullPointerException | IllegalArgumentException ex2) {
                Log.e("VideoTrack", "Unable to create encoder", (Throwable)ex2);
                this.mCallback.obtainMessage(1, 4, 1).sendToTarget();
                return;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Video format is not supported ");
        sb.append(videoFormat.toString());
        Log.e("VideoTrack", sb.toString());
        this.mCallback.obtainMessage(1, 4, 2);
    }
    
    @Override
    protected void doRelease() {
        if (this.mEncoder != null) {
            this.mEncoder.release();
            this.mEncoder = null;
        }
        this.mMuxerHandler.removeMessages(104);
    }
    
    @Override
    protected void doReset() {
        if (this.mEncoder != null) {
            if (this.mMuxerState == MuxerState.STARTED) {
                this.mState = States.STOPPING;
                this.mEncoder.signalEndOfInputStream();
            }
            else if (this.mState != States.STOPPED) {
                this.mState = States.STOPPED;
                try {
                    this.mEncoder.stop();
                }
                catch (final IllegalStateException obj) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(obj);
                    sb.append(" occurred. Maybe MediaCodec is released.");
                    Log.e("VideoTrack", sb.toString(), (Throwable)obj);
                }
            }
        }
    }
    
    @Override
    protected void doResume(final CountDownLatch countDownLatch) {
        countDownLatch.countDown();
    }
    
    @Override
    protected void doStart() {
        this.mEncoder.start();
        this.mFrameDropCounter = 0;
        this.mFirstVideoFrameTimeUs = -1L;
        this.mRecordedDurationAtStopUs = -1L;
        this.mPauseResumeFlag = false;
        this.mWaitForKeyFrame = false;
        this.mKeyFrameRequested = false;
        this.mFirstCodecConfigFrame = true;
        this.mState = States.STARTED;
    }
    
    @Override
    protected void doStop() {
        this.mRecordedDurationAtStopUs = this.mClock.getDurationAtStopUs();
        this.mCodecHandler.sendEmptyMessageDelayed(111, 1000L);
    }
    
    @Override
    protected void doWriteOutputBuffer() throws IllegalStateException {
        if (!this.mBufferList.isEmpty() && this.mState != States.STOPPED && this.isMuxerStarted()) {
            final EncodedBuffer encodedBuffer = this.mBufferList.removeFirst();
            ByteBuffer byteBuffer;
            if (encodedBuffer.containsCopiedBuffer) {
                byteBuffer = encodedBuffer.byteBuffer;
            }
            else {
                byteBuffer = this.mEncoder.getOutputBuffer(encodedBuffer.bufferIndex);
            }
            if (byteBuffer != null) {
                if (encodedBuffer.bufferInfo.size != 0) {
                    this.mMuxerWrapper.writeSampleData(this.mMuxerTrackIndex, byteBuffer, encodedBuffer.bufferInfo);
                }
            }
            if (!encodedBuffer.containsCopiedBuffer) {
                this.mEncoder.releaseOutputBuffer(encodedBuffer.bufferIndex, false);
            }
            if ((encodedBuffer.bufferInfo.flags & 0x4) != 0x0) {
                this.mEncoder.stop();
                this.mCallback.sendMessage(this.mCallback.obtainMessage(101));
                this.mState = States.STOPPED;
                this.mMuxerWrapper.endTrack(this.mMuxerTrackIndex);
            }
        }
    }
    
    public Surface getSurface() {
        return this.mSourceSurface;
    }
    
    void setBitRateMode(final int mBitRateMode) {
        this.mBitRateMode = mBitRateMode;
    }
    
    void setColorAspects(final int mColorStandard, final int mColorTransfer, final int mColorRange) {
        this.mColorStandard = mColorStandard;
        this.mColorTransfer = mColorTransfer;
        this.mColorRange = mColorRange;
    }
    
    void setEncodingProfileLevel(final int mEncodingProfile, final int mEncodingLevel) {
        this.mEncodingProfile = mEncodingProfile;
        this.mEncodingLevel = mEncodingLevel;
    }
    
    void setFrameRate(final int mFrameRate) {
        this.mFrameRate = mFrameRate;
        long mFrameInterval;
        if (this.mFrameRate != 0) {
            mFrameInterval = 1000000 / this.mFrameRate;
        }
        else {
            mFrameInterval = 30000L;
        }
        this.mFrameInterval = mFrameInterval;
    }
    
    void setInputSurface(final Surface surface) {
        this.mSourceSurface = surface;
        if (this.mEncoder != null) {
            this.mEncoder.setInputSurface(surface);
        }
    }
    
    @Override
    public void setMediaMuxerStarted() {
        super.setMediaMuxerStarted();
        this.mMuxerHandler.obtainMessage(110).sendToTarget();
    }
    
    void setVideoEncoder(final int n) {
        if (n >= 0 && n <= this.videoMimeTypes.length - 1) {
            this.mVideoMime = this.videoMimeTypes[n];
        }
        else {
            this.mVideoMime = null;
        }
    }
    
    void setVideoSize(final int mWidth, final int mHeight) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;
    }
    
    private class CodecHandler extends Handler
    {
        final VideoTrack this$0;
        
        CodecHandler(final VideoTrack this$0, final Looper looper) {
            this.this$0 = this$0;
            super(looper);
        }
        
        private void addTrack() {
            if (this.this$0.mMuxerTrackIndex < 0) {
                this.this$0.mMuxerTrackIndex = this.this$0.mMuxerWrapper.addTrack(this.this$0.mEncoder.getOutputFormat());
                this.this$0.mCallback.obtainMessage(1, 10, 0).sendToTarget();
            }
        }
        
        private void doQueueOutputBuffer(final int n, final MediaCodec$BufferInfo mediaCodec$BufferInfo) {
            if (!this.this$0.mVideoMime.equals("video/x-vnd.on2.vp8") && !this.this$0.mVideoMime.equals("video/3gpp")) {
                if ((mediaCodec$BufferInfo.flags & 0x2) == 0x2 && this.this$0.mFirstCodecConfigFrame) {
                    this.addTrack();
                    this.this$0.mEncoder.releaseOutputBuffer(n, false);
                    this.this$0.mFirstCodecConfigFrame = false;
                    return;
                }
            }
            else if (this.this$0.mFirstCodecConfigFrame) {
                this.addTrack();
                this.this$0.mFirstCodecConfigFrame = false;
            }
            final boolean b = (mediaCodec$BufferInfo.flags & 0x1) == 0x1;
            if (this.this$0.mFirstVideoFrameTimeUs < 0L) {
                if (b) {
                    this.this$0.mFirstVideoFrameTimeUs = mediaCodec$BufferInfo.presentationTimeUs;
                    mediaCodec$BufferInfo.presentationTimeUs = 0L;
                }
                this.queueBuffer(n, mediaCodec$BufferInfo);
                return;
            }
            if ((mediaCodec$BufferInfo.flags & 0x4) == 0x4) {
                this.queueBuffer(n, mediaCodec$BufferInfo);
                return;
            }
            final long n2 = mediaCodec$BufferInfo.presentationTimeUs - this.this$0.mFirstVideoFrameTimeUs;
            mediaCodec$BufferInfo.presentationTimeUs = n2 - this.this$0.mVideoOffset;
            if (this.this$0.mRecordedDurationAtStopUs > 0L && mediaCodec$BufferInfo.presentationTimeUs > this.this$0.mRecordedDurationAtStopUs && this.this$0.mState != States.STOPPED && this.this$0.mState != States.STOPPING) {
                this.this$0.mState = States.STOPPING;
                this.this$0.mEncoder.signalEndOfInputStream();
            }
            if (mediaCodec$BufferInfo.size == 0) {
                this.queueBuffer(n, mediaCodec$BufferInfo);
                return;
            }
            if (!this.this$0.mClock.isPausedAt(n2)) {
                if (this.this$0.mPauseResumeFlag) {
                    this.this$0.mWaitForKeyFrame = true;
                    this.this$0.mKeyFrameRequested = false;
                    this.this$0.mPauseResumeFlag = false;
                }
                if (this.this$0.mWaitForKeyFrame) {
                    if (!this.this$0.mKeyFrameRequested) {
                        final Bundle parameters = new Bundle();
                        parameters.putInt("request-sync", 0);
                        this.this$0.mEncoder.setParameters(parameters);
                        this.this$0.mKeyFrameRequested = true;
                    }
                    if (!b) {
                        this.this$0.mEncoder.releaseOutputBuffer(n, false);
                        this.this$0.mFrameDropCounter++;
                        this.this$0.mLastRecordedVideoTimestampUs += this.this$0.mFrameInterval;
                        return;
                    }
                    this.this$0.mWaitForKeyFrame = false;
                    final long presentationTimeUs = this.this$0.mLastRecordedVideoTimestampUs + this.this$0.mFrameInterval;
                    this.this$0.mVideoOffset = n2 - presentationTimeUs;
                    mediaCodec$BufferInfo.presentationTimeUs = presentationTimeUs;
                }
                this.queueBuffer(n, mediaCodec$BufferInfo);
            }
            else {
                if (!this.this$0.mPauseResumeFlag) {
                    this.this$0.mPauseResumeFlag = true;
                }
                this.this$0.mEncoder.releaseOutputBuffer(n, false);
            }
        }
        
        private void doTimeOutBufferCallback() {
            if (this.this$0.mRecordedDurationAtStopUs > 0L && this.this$0.mState != States.STOPPED && this.this$0.mState != States.STOPPING) {
                this.this$0.mState = States.STOPPING;
                final StringBuilder sb = new StringBuilder();
                sb.append("Forced stop due to timeout of buffer callback : recording duration at stop = ");
                sb.append(this.this$0.mRecordedDurationAtStopUs);
                sb.append(" , last recorded timestamp = ");
                sb.append(this.this$0.mLastRecordedVideoTimestampUs);
                Log.w("VideoTrack", sb.toString());
                this.this$0.mEncoder.signalEndOfInputStream();
            }
        }
        
        private void queueBuffer(final int n, final MediaCodec$BufferInfo mediaCodec$BufferInfo) {
            final EncodedBuffer e = new EncodedBuffer(n, mediaCodec$BufferInfo);
            if (this.this$0.mMuxerState == MuxerState.IDLE) {
                final ByteBuffer outputBuffer = this.this$0.mEncoder.getOutputBuffer(n);
                if (outputBuffer != null) {
                    e.byteBuffer = ByteBuffer.allocate(outputBuffer.limit());
                    outputBuffer.rewind();
                    e.byteBuffer.put(outputBuffer);
                    e.containsCopiedBuffer = true;
                }
                this.this$0.mEncoder.releaseOutputBuffer(n, false);
                if (mediaCodec$BufferInfo.presentationTimeUs > 10000000L) {
                    Log.e("VideoTrack", "MediaMuxer is timed out.");
                    throw new RuntimeException("MediaMuxer is timed out.");
                }
            }
            this.this$0.mBufferList.add(e);
            this.this$0.mLastRecordedVideoTimestampUs = mediaCodec$BufferInfo.presentationTimeUs;
            if ((mediaCodec$BufferInfo.flags & 0x4) != 0x0) {
                this.this$0.mMuxerHandler.obtainMessage(110).sendToTarget();
            }
            else {
                this.this$0.mMuxerHandler.obtainMessage(104).sendToTarget();
            }
        }
        
        public void handleMessage(final Message message) {
            final int what = message.what;
            if (what != 105) {
                if (what == 111) {
                    this.doTimeOutBufferCallback();
                }
            }
            else {
                this.doQueueOutputBuffer(message.arg1, (MediaCodec$BufferInfo)message.obj);
            }
        }
    }
    
    private class VideoEncoderCallback extends MediaCodec$Callback
    {
        final VideoTrack this$0;
        
        private VideoEncoderCallback(final VideoTrack this$0) {
            this.this$0 = this$0;
        }
        
        public void onError(@NonNull final MediaCodec mediaCodec, @NonNull final MediaCodec$CodecException ex) {
            Log.e("VideoTrack", "Error from encoder", (Throwable)ex);
            this.this$0.mCallback.obtainMessage(1, 4, 0).sendToTarget();
        }
        
        public void onInputBufferAvailable(@NonNull final MediaCodec mediaCodec, final int n) {
        }
        
        public void onOutputBufferAvailable(@NonNull final MediaCodec mediaCodec, final int n, @NonNull final MediaCodec$BufferInfo mediaCodec$BufferInfo) {
            this.this$0.mCodecHandler.obtainMessage(105, n, 0, (Object)mediaCodec$BufferInfo).sendToTarget();
        }
        
        public void onOutputFormatChanged(@NonNull final MediaCodec mediaCodec, @NonNull final MediaFormat mediaFormat) {
        }
    }
}
