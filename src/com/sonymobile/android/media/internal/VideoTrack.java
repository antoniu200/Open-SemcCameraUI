package com.sonymobile.android.media.internal;

import android.media.MediaCodec;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import com.google.android.gms.common.Scopes;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonymobile.android.media.internal.Track;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class VideoTrack extends Track {
    private static final int ENCODER_REQUEST_SYNC_FRAME = 0;
    private static final int I_FRAME_INTERVAL = 1;
    static final String TAG = "VideoTrack";
    private static final long TIME_OUT_STOPPING_MILLISECONDS = 1000;
    private static final long WAIT_FOR_MEDIA_MUXER_START_TIMED_OUT_US = 10000000;
    private final Handler mCallback;
    private final CodecHandler mCodecHandler;
    private int mFrameDropCounter;
    private int mHeight;
    private final Track.MuxerHandler mMuxerHandler;
    private Surface mSourceSurface;
    private String mVideoMime;
    private int mWidth;
    private final String[] videoMimeTypes = {"", MediaSavingConstants.MEDIA_TYPE_3GP_MIME, "video/avc", "video/mp4v-es", "video/x-vnd.on2.vp8", "video/hevc", "video/x-vnd.on2.vp9"};
    private int mFrameRate = 30;
    protected int mEncodingProfile = 0;
    protected int mEncodingLevel = 0;
    private int mBitRateMode = -1;
    private int mColorStandard = -1;
    private int mColorTransfer = -1;
    private int mColorRange = -1;
    private long mFrameInterval = 1000000 / this.mFrameRate;
    private long mFirstVideoFrameTimeUs = -1;
    private long mRecordedDurationAtStopUs = -1;
    private long mLastRecordedVideoTimestampUs = 0;
    private boolean mWaitForKeyFrame = false;
    private long mVideoOffset = 0;
    private boolean mKeyFrameRequested = false;
    private boolean mPauseResumeFlag = false;
    private boolean mFirstCodecConfigFrame = true;

    static /* synthetic */ int access$908(VideoTrack videoTrack) {
        int i = videoTrack.mFrameDropCounter;
        videoTrack.mFrameDropCounter = i + 1;
        return i;
    }

    VideoTrack(Handler handler, HandlerThread handlerThread, HandlerThread handlerThread2, HandlerThread handlerThread3) {
        this.mCodecHandler = new CodecHandler(handlerThread.getLooper());
        this.mEventHandler = new Track.EventHandler(handlerThread2.getLooper());
        this.mMuxerHandler = new Track.MuxerHandler(handlerThread3.getLooper());
        this.mHandlerHelper = new HandlerHelper();
        this.mCallback = handler;
        this.mState = Track.States.STOPPED;
    }

    public Surface getSurface() {
        return this.mSourceSurface;
    }

    void setInputSurface(Surface surface) {
        this.mSourceSurface = surface;
        if (this.mEncoder != null) {
            this.mEncoder.setInputSurface(surface);
        }
    }

    void setVideoSize(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    void setFrameRate(int i) {
        this.mFrameRate = i;
        this.mFrameInterval = this.mFrameRate != 0 ? 1000000 / this.mFrameRate : 30000L;
    }

    void setEncodingProfileLevel(int i, int i2) {
        this.mEncodingProfile = i;
        this.mEncodingLevel = i2;
    }

    void setBitRateMode(int i) {
        this.mBitRateMode = i;
    }

    void setColorAspects(int i, int i2, int i3) {
        this.mColorStandard = i;
        this.mColorTransfer = i2;
        this.mColorRange = i3;
    }

    void setVideoEncoder(int i) {
        if (i < 0 || i > this.videoMimeTypes.length - 1) {
            this.mVideoMime = null;
        } else {
            this.mVideoMime = this.videoMimeTypes[i];
        }
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doPause() {
        this.mPauseLatch.countDown();
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doPrepare() {
        MediaCodecList mediaCodecList = new MediaCodecList(0);
        MediaFormat mediaFormatCreateVideoFormat = MediaFormat.createVideoFormat(this.mVideoMime, this.mWidth, this.mHeight);
        mediaFormatCreateVideoFormat.setInteger("bitrate", this.mEncodingBitRate);
        mediaFormatCreateVideoFormat.setInteger("frame-rate", this.mFrameRate);
        mediaFormatCreateVideoFormat.setInteger("i-frame-interval", 1);
        if (this.mOperatingRate > 0) {
            mediaFormatCreateVideoFormat.setInteger("operating-rate", this.mOperatingRate);
            mediaFormatCreateVideoFormat.setString("ts-schema", "android.generic.1+0");
        }
        if (this.mEncodingProfile == 0) {
            if (this.mVideoMime.equals("video/avc")) {
                if (this.mWidth >= 1280 && this.mHeight >= 720) {
                    mediaFormatCreateVideoFormat.setInteger(Scopes.PROFILE, 8);
                } else {
                    mediaFormatCreateVideoFormat.setInteger(Scopes.PROFILE, 1);
                }
                mediaFormatCreateVideoFormat.setInteger("level", 1);
            }
        } else {
            mediaFormatCreateVideoFormat.setInteger(Scopes.PROFILE, this.mEncodingProfile);
            mediaFormatCreateVideoFormat.setInteger("level", this.mEncodingLevel == 0 ? 1 : this.mEncodingLevel);
        }
        mediaFormatCreateVideoFormat.setInteger("color-format", 2130708361);
        if (this.mColorStandard >= 0 && this.mColorTransfer >= 0 && this.mColorRange >= 0) {
            mediaFormatCreateVideoFormat.setInteger("color-standard", this.mColorStandard);
            mediaFormatCreateVideoFormat.setInteger("color-transfer", this.mColorTransfer);
            mediaFormatCreateVideoFormat.setInteger("color-range", this.mColorRange);
        } else {
            if (this.mWidth >= 1280 && this.mHeight >= 720) {
                mediaFormatCreateVideoFormat.setInteger("color-standard", 1);
            } else {
                mediaFormatCreateVideoFormat.setInteger("color-standard", 4);
            }
            mediaFormatCreateVideoFormat.setInteger("color-transfer", 3);
            mediaFormatCreateVideoFormat.setInteger("color-range", 2);
        }
        if (checkFormat(mediaCodecList, mediaFormatCreateVideoFormat, this.mVideoMime)) {
            try {
                this.mEncoder = MediaCodec.createByCodecName(mediaCodecList.findEncoderForFormat(mediaFormatCreateVideoFormat));
                this.mEncoder.setCallback(new VideoEncoderCallback());
                try {
                    if (this.mBitRateMode >= 0) {
                        mediaFormatCreateVideoFormat.setInteger("bitrate-mode", this.mBitRateMode);
                    }
                    this.mEncoder.configure(mediaFormatCreateVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                    if (this.mSourceSurface == null) {
                        this.mSourceSurface = this.mEncoder.createInputSurface();
                    } else {
                        this.mEncoder.setInputSurface(this.mSourceSurface);
                    }
                    this.mBufferList = new LinkedBlockingDeque<>();
                    return;
                } catch (MediaCodec.CodecException unused) {
                    Log.e(TAG, "Failed to configure MediaCodec");
                    this.mCallback.obtainMessage(1, 4, 1).sendToTarget();
                    return;
                }
            } catch (IOException | IllegalArgumentException | NullPointerException e) {
                Log.e(TAG, "Unable to create encoder", e);
                this.mCallback.obtainMessage(1, 4, 1).sendToTarget();
                return;
            }
        }
        Log.e(TAG, "Video format is not supported " + mediaFormatCreateVideoFormat.toString());
        this.mCallback.obtainMessage(1, 4, 2);
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doStart() {
        this.mEncoder.start();
        this.mFrameDropCounter = 0;
        this.mFirstVideoFrameTimeUs = -1L;
        this.mRecordedDurationAtStopUs = -1L;
        this.mPauseResumeFlag = false;
        this.mWaitForKeyFrame = false;
        this.mKeyFrameRequested = false;
        this.mFirstCodecConfigFrame = true;
        this.mState = Track.States.STARTED;
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doRelease() {
        if (this.mEncoder != null) {
            this.mEncoder.release();
            this.mEncoder = null;
        }
        this.mMuxerHandler.removeMessages(104);
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doResume(CountDownLatch countDownLatch) {
        countDownLatch.countDown();
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doReset() {
        if (this.mEncoder != null) {
            if (this.mMuxerState == Track.MuxerState.STARTED) {
                this.mState = Track.States.STOPPING;
                this.mEncoder.signalEndOfInputStream();
            } else if (this.mState != Track.States.STOPPED) {
                this.mState = Track.States.STOPPED;
                try {
                    this.mEncoder.stop();
                } catch (IllegalStateException e) {
                    Log.e(TAG, e + " occurred. Maybe MediaCodec is released.", e);
                }
            }
        }
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doStop() {
        this.mRecordedDurationAtStopUs = this.mClock.getDurationAtStopUs();
        this.mCodecHandler.sendEmptyMessageDelayed(111, 1000L);
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doWriteOutputBuffer() throws IllegalStateException {
        ByteBuffer outputBuffer;
        if (this.mBufferList.isEmpty() || this.mState == Track.States.STOPPED || !isMuxerStarted()) {
            return;
        }
        Track.EncodedBuffer encodedBufferRemoveFirst = this.mBufferList.removeFirst();
        if (encodedBufferRemoveFirst.containsCopiedBuffer) {
            outputBuffer = encodedBufferRemoveFirst.byteBuffer;
        } else {
            outputBuffer = this.mEncoder.getOutputBuffer(encodedBufferRemoveFirst.bufferIndex);
        }
        if (outputBuffer != null && encodedBufferRemoveFirst.bufferInfo.size != 0) {
            this.mMuxerWrapper.writeSampleData(this.mMuxerTrackIndex, outputBuffer, encodedBufferRemoveFirst.bufferInfo);
        }
        if (!encodedBufferRemoveFirst.containsCopiedBuffer) {
            this.mEncoder.releaseOutputBuffer(encodedBufferRemoveFirst.bufferIndex, false);
        }
        if ((encodedBufferRemoveFirst.bufferInfo.flags & 4) != 0) {
            this.mEncoder.stop();
            this.mCallback.sendMessage(this.mCallback.obtainMessage(101));
            this.mState = Track.States.STOPPED;
            this.mMuxerWrapper.endTrack(this.mMuxerTrackIndex);
        }
    }

    @Override // com.sonymobile.android.media.internal.Track
    public void setMediaMuxerStarted() {
        super.setMediaMuxerStarted();
        this.mMuxerHandler.obtainMessage(110).sendToTarget();
    }

    private class CodecHandler extends Handler {
        CodecHandler(Looper looper) {
            super(looper);
        }

        private void doQueueOutputBuffer(int i, MediaCodec.BufferInfo bufferInfo) {
            if (VideoTrack.this.mVideoMime.equals("video/x-vnd.on2.vp8") || VideoTrack.this.mVideoMime.equals(MediaSavingConstants.MEDIA_TYPE_3GP_MIME)) {
                if (VideoTrack.this.mFirstCodecConfigFrame) {
                    addTrack();
                    VideoTrack.this.mFirstCodecConfigFrame = false;
                }
            } else {
                if (((bufferInfo.flags & 2) == 2) && VideoTrack.this.mFirstCodecConfigFrame) {
                    addTrack();
                    VideoTrack.this.mEncoder.releaseOutputBuffer(i, false);
                    VideoTrack.this.mFirstCodecConfigFrame = false;
                    return;
                }
            }
            boolean z = (bufferInfo.flags & 1) == 1;
            if (VideoTrack.this.mFirstVideoFrameTimeUs < 0) {
                if (z) {
                    VideoTrack.this.mFirstVideoFrameTimeUs = bufferInfo.presentationTimeUs;
                    bufferInfo.presentationTimeUs = 0L;
                }
                queueBuffer(i, bufferInfo);
                return;
            }
            if (!((bufferInfo.flags & 4) == 4)) {
                long j = bufferInfo.presentationTimeUs - VideoTrack.this.mFirstVideoFrameTimeUs;
                bufferInfo.presentationTimeUs = j - VideoTrack.this.mVideoOffset;
                if (VideoTrack.this.mRecordedDurationAtStopUs > 0 && bufferInfo.presentationTimeUs > VideoTrack.this.mRecordedDurationAtStopUs && VideoTrack.this.mState != Track.States.STOPPED && VideoTrack.this.mState != Track.States.STOPPING) {
                    VideoTrack.this.mState = Track.States.STOPPING;
                    VideoTrack.this.mEncoder.signalEndOfInputStream();
                }
                if (bufferInfo.size == 0) {
                    queueBuffer(i, bufferInfo);
                    return;
                }
                if (!VideoTrack.this.mClock.isPausedAt(j)) {
                    if (VideoTrack.this.mPauseResumeFlag) {
                        VideoTrack.this.mWaitForKeyFrame = true;
                        VideoTrack.this.mKeyFrameRequested = false;
                        VideoTrack.this.mPauseResumeFlag = false;
                    }
                    if (VideoTrack.this.mWaitForKeyFrame) {
                        if (!VideoTrack.this.mKeyFrameRequested) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("request-sync", 0);
                            VideoTrack.this.mEncoder.setParameters(bundle);
                            VideoTrack.this.mKeyFrameRequested = true;
                        }
                        if (z) {
                            VideoTrack.this.mWaitForKeyFrame = false;
                            long j2 = VideoTrack.this.mLastRecordedVideoTimestampUs + VideoTrack.this.mFrameInterval;
                            VideoTrack.this.mVideoOffset = j - j2;
                            bufferInfo.presentationTimeUs = j2;
                        } else {
                            VideoTrack.this.mEncoder.releaseOutputBuffer(i, false);
                            VideoTrack.access$908(VideoTrack.this);
                            VideoTrack.this.mLastRecordedVideoTimestampUs += VideoTrack.this.mFrameInterval;
                            return;
                        }
                    }
                    queueBuffer(i, bufferInfo);
                    return;
                }
                if (!VideoTrack.this.mPauseResumeFlag) {
                    VideoTrack.this.mPauseResumeFlag = true;
                }
                VideoTrack.this.mEncoder.releaseOutputBuffer(i, false);
                return;
            }
            queueBuffer(i, bufferInfo);
        }

        private void addTrack() {
            if (VideoTrack.this.mMuxerTrackIndex < 0) {
                VideoTrack.this.mMuxerTrackIndex = VideoTrack.this.mMuxerWrapper.addTrack(VideoTrack.this.mEncoder.getOutputFormat());
                VideoTrack.this.mCallback.obtainMessage(1, 10, 0).sendToTarget();
            }
        }

        private void queueBuffer(int i, MediaCodec.BufferInfo bufferInfo) {
            Track.EncodedBuffer encodedBuffer = new Track.EncodedBuffer(i, bufferInfo);
            if (VideoTrack.this.mMuxerState == Track.MuxerState.IDLE) {
                ByteBuffer outputBuffer = VideoTrack.this.mEncoder.getOutputBuffer(i);
                if (outputBuffer != null) {
                    encodedBuffer.byteBuffer = ByteBuffer.allocate(outputBuffer.limit());
                    outputBuffer.rewind();
                    encodedBuffer.byteBuffer.put(outputBuffer);
                    encodedBuffer.containsCopiedBuffer = true;
                }
                VideoTrack.this.mEncoder.releaseOutputBuffer(i, false);
                if (bufferInfo.presentationTimeUs > VideoTrack.WAIT_FOR_MEDIA_MUXER_START_TIMED_OUT_US) {
                    Log.e(VideoTrack.TAG, "MediaMuxer is timed out.");
                    throw new RuntimeException("MediaMuxer is timed out.");
                }
            }
            VideoTrack.this.mBufferList.add(encodedBuffer);
            VideoTrack.this.mLastRecordedVideoTimestampUs = bufferInfo.presentationTimeUs;
            if ((bufferInfo.flags & 4) != 0) {
                VideoTrack.this.mMuxerHandler.obtainMessage(110).sendToTarget();
            } else {
                VideoTrack.this.mMuxerHandler.obtainMessage(104).sendToTarget();
            }
        }

        private void doTimeOutBufferCallback() {
            if (VideoTrack.this.mRecordedDurationAtStopUs <= 0 || VideoTrack.this.mState == Track.States.STOPPED || VideoTrack.this.mState == Track.States.STOPPING) {
                return;
            }
            VideoTrack.this.mState = Track.States.STOPPING;
            Log.w(VideoTrack.TAG, "Forced stop due to timeout of buffer callback : recording duration at stop = " + VideoTrack.this.mRecordedDurationAtStopUs + " , last recorded timestamp = " + VideoTrack.this.mLastRecordedVideoTimestampUs);
            VideoTrack.this.mEncoder.signalEndOfInputStream();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 105) {
                doQueueOutputBuffer(message.arg1, (MediaCodec.BufferInfo) message.obj);
            } else {
                if (i != 111) {
                    return;
                }
                doTimeOutBufferCallback();
            }
        }
    }

    private class VideoEncoderCallback extends MediaCodec.Callback {
        @Override // android.media.MediaCodec.Callback
        public void onInputBufferAvailable(@NonNull MediaCodec mediaCodec, int i) {
        }

        @Override // android.media.MediaCodec.Callback
        public void onOutputFormatChanged(@NonNull MediaCodec mediaCodec, @NonNull MediaFormat mediaFormat) {
        }

        private VideoEncoderCallback() {
        }

        @Override // android.media.MediaCodec.Callback
        public void onOutputBufferAvailable(@NonNull MediaCodec mediaCodec, int i, @NonNull MediaCodec.BufferInfo bufferInfo) {
            VideoTrack.this.mCodecHandler.obtainMessage(105, i, 0, bufferInfo).sendToTarget();
        }

        @Override // android.media.MediaCodec.Callback
        public void onError(@NonNull MediaCodec mediaCodec, @NonNull MediaCodec.CodecException codecException) {
            Log.e(VideoTrack.TAG, "Error from encoder", codecException);
            VideoTrack.this.mCallback.obtainMessage(1, 4, 0).sendToTarget();
        }
    }
}
