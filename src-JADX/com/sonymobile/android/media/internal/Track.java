package com.sonymobile.android.media.internal;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class Track {
    public static final int AUDIO_CODEC_MUXER_TRACK_ADDED = 11;
    public static final int CODEC_ERROR = 4;
    public static final int CODEC_FORMAT_UNSUPPORTED = 2;
    public static final int CODEC_ILLEGAL_STATE = 3;
    public static final int CODEC_UNSUPPORTED = 1;
    protected static final int MSG_AUDIO_CODEC_STOPPED = 103;
    protected static final int MSG_FLUSH_OUTPUT_BUFFERS = 110;
    protected static final int MSG_HANDLE_INPUT_BUFFER = 102;
    protected static final int MSG_PAUSE = 5;
    protected static final int MSG_PREPARE = 3;
    protected static final int MSG_QUEUE_OUTPUT_BUFFER = 105;
    protected static final int MSG_RELEASE = 4;
    protected static final int MSG_RELEASE_AUDIO_RECORDER = 108;
    protected static final int MSG_RESET = 7;
    protected static final int MSG_RESUME = 6;
    protected static final int MSG_START = 1;
    protected static final int MSG_START_AUDIO_RECORDER = 109;
    protected static final int MSG_STOP = 2;
    protected static final int MSG_STOP_AUDIO_RECORDER = 107;
    protected static final int MSG_TIME_OUT_OUTPUT_BUFFER_RECEIVE = 111;
    protected static final int MSG_VIDEO_CODEC_STOPPED = 101;
    protected static final int MSG_WRITE_OUTPUT_BUFFER = 104;
    private static final String TAG = "Track";
    public static final int UNKNOWN = 0;
    public static final int VIDEO_CODEC_MUXER_TRACK_ADDED = 10;
    protected LinkedBlockingDeque<EncodedBuffer> mBufferList;
    protected ClockInterface mClock;
    protected MediaCodec mEncoder;
    protected EventHandler mEventHandler;
    protected HandlerHelper mHandlerHelper;
    protected boolean mIsPauseLatchDown;
    protected MediaMuxerWrapper mMuxerWrapper;
    protected CountDownLatch mPauseLatch;
    protected States mState;
    protected int mEncodingBitRate = 2000000;
    protected int mOperatingRate = 0;
    protected MuxerState mMuxerState = MuxerState.IDLE;
    protected int mMuxerTrackIndex = -1;

    protected enum MuxerState {
        IDLE,
        STARTED,
        STOPPED
    }

    protected enum States {
        STARTED,
        STOPPED,
        STOPPING,
        PAUSED
    }

    protected abstract void doPause();

    protected abstract void doPrepare();

    protected abstract void doRelease();

    protected abstract void doReset();

    protected abstract void doResume(CountDownLatch countDownLatch);

    protected abstract void doStart();

    protected abstract void doStop();

    protected abstract void doWriteOutputBuffer();

    public void setEncodingBitrate(int i) {
        this.mEncodingBitRate = i;
    }

    public void setOperatingRate(int i) {
        this.mOperatingRate = i;
    }

    public void setClock(ClockInterface clockInterface) {
        this.mClock = clockInterface;
    }

    public void pause(CountDownLatch countDownLatch, boolean z) {
        this.mPauseLatch = countDownLatch;
        this.mIsPauseLatchDown = false;
        if (this.mMuxerState == MuxerState.IDLE && !z) {
            this.mPauseLatch.countDown();
            this.mIsPauseLatchDown = true;
        }
        this.mEventHandler.obtainMessage(5).sendToTarget();
    }

    public void prepare() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(3));
    }

    public void start() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(1));
    }

    public void stop() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(2));
    }

    public void release() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(4));
        this.mHandlerHelper.releaseAllLocks();
    }

    public void resume(CountDownLatch countDownLatch) {
        this.mEventHandler.obtainMessage(6, countDownLatch).sendToTarget();
    }

    public void reset() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(7));
    }

    public void setMediaMuxer(MediaMuxerWrapper mediaMuxerWrapper) {
        this.mMuxerWrapper = mediaMuxerWrapper;
    }

    public void setMediaMuxerStarted() {
        this.mMuxerState = MuxerState.STARTED;
    }

    public void setMediaMuxerStopped() {
        this.mMuxerState = MuxerState.STOPPED;
    }

    protected boolean isMuxerStarted() {
        return this.mMuxerState == MuxerState.STARTED;
    }

    protected static class EncodedBuffer {
        public final int bufferIndex;
        public final MediaCodec.BufferInfo bufferInfo;
        public ByteBuffer byteBuffer = null;
        public boolean containsCopiedBuffer = false;

        EncodedBuffer(int i, MediaCodec.BufferInfo bufferInfo) {
            this.bufferIndex = i;
            this.bufferInfo = bufferInfo;
        }
    }

    protected boolean checkFormat(MediaCodecList mediaCodecList, MediaFormat mediaFormat, String str) {
        MediaCodecInfo[] codecInfos = mediaCodecList.getCodecInfos();
        int i = 0;
        boolean z = false;
        while (i < codecInfos.length && !z) {
            String[] supportedTypes = codecInfos[i].getSupportedTypes();
            boolean zIsFormatSupported = z;
            for (int i2 = 0; i2 < supportedTypes.length && !zIsFormatSupported; i2++) {
                if (supportedTypes[i2].equals(str)) {
                    zIsFormatSupported = codecInfos[i].getCapabilitiesForType(str).isFormatSupported(mediaFormat);
                }
            }
            i++;
            z = zIsFormatSupported;
        }
        return z;
    }

    protected class MuxerHandler extends Handler {
        public MuxerHandler(Looper looper) {
            super(looper);
        }

        private void doFlushBuffers() {
            int size = Track.this.mBufferList.size();
            for (int i = 0; i < size; i++) {
                Track.this.doWriteOutputBuffer();
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            try {
                if (i == 104) {
                    Track.this.doWriteOutputBuffer();
                } else if (i != 110) {
                } else {
                    doFlushBuffers();
                }
            } catch (IllegalStateException unused) {
            }
        }
    }

    protected class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Track.this.doStart();
                    Message messageObtainMessage = ((Handler) message.obj).obtainMessage();
                    messageObtainMessage.obj = new Object();
                    messageObtainMessage.sendToTarget();
                    break;
                case 2:
                    Track.this.doStop();
                    Message messageObtainMessage2 = ((Handler) message.obj).obtainMessage();
                    messageObtainMessage2.obj = new Object();
                    messageObtainMessage2.sendToTarget();
                    break;
                case 3:
                    Track.this.doPrepare();
                    Message messageObtainMessage3 = ((Handler) message.obj).obtainMessage();
                    messageObtainMessage3.obj = new Object();
                    messageObtainMessage3.sendToTarget();
                    break;
                case 4:
                    Track.this.doRelease();
                    Message messageObtainMessage4 = ((Handler) message.obj).obtainMessage();
                    messageObtainMessage4.obj = new Object();
                    messageObtainMessage4.sendToTarget();
                    break;
                case 5:
                    Track.this.doPause();
                    break;
                case 6:
                    Track.this.doResume((CountDownLatch) message.obj);
                    break;
                case 7:
                    Track.this.doReset();
                    Message messageObtainMessage5 = ((Handler) message.obj).obtainMessage();
                    messageObtainMessage5.obj = new Object();
                    messageObtainMessage5.sendToTarget();
                    break;
            }
        }
    }
}
