// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.android.media.internal;

import android.os.Message;
import android.os.Looper;
import android.os.Handler;
import java.nio.ByteBuffer;
import android.media.MediaCodec$BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaCodecList;
import java.util.concurrent.CountDownLatch;
import android.media.MediaCodec;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class Track
{
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
    protected int mEncodingBitRate;
    protected EventHandler mEventHandler;
    protected HandlerHelper mHandlerHelper;
    protected boolean mIsPauseLatchDown;
    protected MuxerState mMuxerState;
    protected int mMuxerTrackIndex;
    protected MediaMuxerWrapper mMuxerWrapper;
    protected int mOperatingRate;
    protected CountDownLatch mPauseLatch;
    protected States mState;
    
    public Track() {
        this.mEncodingBitRate = 2000000;
        this.mOperatingRate = 0;
        this.mMuxerState = MuxerState.IDLE;
        this.mMuxerTrackIndex = -1;
    }
    
    protected boolean checkFormat(final MediaCodecList list, final MediaFormat mediaFormat, final String anObject) {
        MediaCodecInfo[] codecInfos;
        int n;
        boolean formatSupported;
        String[] supportedTypes;
        int n2;
        for (codecInfos = list.getCodecInfos(), n = 0, formatSupported = false; n < codecInfos.length && !formatSupported; ++n) {
            for (supportedTypes = codecInfos[n].getSupportedTypes(), n2 = 0; n2 < supportedTypes.length && !formatSupported; ++n2) {
                if (supportedTypes[n2].equals(anObject)) {
                    formatSupported = codecInfos[n].getCapabilitiesForType(anObject).isFormatSupported(mediaFormat);
                }
            }
        }
        return formatSupported;
    }
    
    protected abstract void doPause();
    
    protected abstract void doPrepare();
    
    protected abstract void doRelease();
    
    protected abstract void doReset();
    
    protected abstract void doResume(final CountDownLatch p0);
    
    protected abstract void doStart();
    
    protected abstract void doStop();
    
    protected abstract void doWriteOutputBuffer();
    
    protected boolean isMuxerStarted() {
        return this.mMuxerState == MuxerState.STARTED;
    }
    
    public void pause(final CountDownLatch mPauseLatch, final boolean b) {
        this.mPauseLatch = mPauseLatch;
        this.mIsPauseLatchDown = false;
        if (this.mMuxerState == MuxerState.IDLE && !b) {
            this.mPauseLatch.countDown();
            this.mIsPauseLatchDown = true;
        }
        this.mEventHandler.obtainMessage(5).sendToTarget();
    }
    
    public void prepare() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(3));
    }
    
    public void release() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(4));
        this.mHandlerHelper.releaseAllLocks();
    }
    
    public void reset() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(7));
    }
    
    public void resume(final CountDownLatch countDownLatch) {
        this.mEventHandler.obtainMessage(6, (Object)countDownLatch).sendToTarget();
    }
    
    public void setClock(final ClockInterface mClock) {
        this.mClock = mClock;
    }
    
    public void setEncodingBitrate(final int mEncodingBitRate) {
        this.mEncodingBitRate = mEncodingBitRate;
    }
    
    public void setMediaMuxer(final MediaMuxerWrapper mMuxerWrapper) {
        this.mMuxerWrapper = mMuxerWrapper;
    }
    
    public void setMediaMuxerStarted() {
        this.mMuxerState = MuxerState.STARTED;
    }
    
    public void setMediaMuxerStopped() {
        this.mMuxerState = MuxerState.STOPPED;
    }
    
    public void setOperatingRate(final int mOperatingRate) {
        this.mOperatingRate = mOperatingRate;
    }
    
    public void start() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(1));
    }
    
    public void stop() {
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mEventHandler.obtainMessage(2));
    }
    
    protected static class EncodedBuffer
    {
        public final int bufferIndex;
        public final MediaCodec$BufferInfo bufferInfo;
        public ByteBuffer byteBuffer;
        public boolean containsCopiedBuffer;
        
        EncodedBuffer(final int bufferIndex, final MediaCodec$BufferInfo bufferInfo) {
            this.bufferIndex = bufferIndex;
            this.bufferInfo = bufferInfo;
            this.byteBuffer = null;
            this.containsCopiedBuffer = false;
        }
    }
    
    protected class EventHandler extends Handler
    {
        final Track this$0;
        
        public EventHandler(final Track this$0, final Looper looper) {
            this.this$0 = this$0;
            super(looper);
        }
        
        public void handleMessage(Message message) {
            switch (message.what) {
                case 7: {
                    this.this$0.doReset();
                    message = ((Handler)message.obj).obtainMessage();
                    message.obj = new Object();
                    message.sendToTarget();
                    break;
                }
                case 6: {
                    this.this$0.doResume((CountDownLatch)message.obj);
                    break;
                }
                case 5: {
                    this.this$0.doPause();
                    break;
                }
                case 4: {
                    this.this$0.doRelease();
                    message = ((Handler)message.obj).obtainMessage();
                    message.obj = new Object();
                    message.sendToTarget();
                    break;
                }
                case 3: {
                    this.this$0.doPrepare();
                    message = ((Handler)message.obj).obtainMessage();
                    message.obj = new Object();
                    message.sendToTarget();
                    break;
                }
                case 2: {
                    this.this$0.doStop();
                    message = ((Handler)message.obj).obtainMessage();
                    message.obj = new Object();
                    message.sendToTarget();
                    break;
                }
                case 1: {
                    this.this$0.doStart();
                    message = ((Handler)message.obj).obtainMessage();
                    message.obj = new Object();
                    message.sendToTarget();
                    break;
                }
            }
        }
    }
    
    protected class MuxerHandler extends Handler
    {
        final Track this$0;
        
        public MuxerHandler(final Track this$0, final Looper looper) {
            this.this$0 = this$0;
            super(looper);
        }
        
        private void doFlushBuffers() {
            for (int size = this.this$0.mBufferList.size(), i = 0; i < size; ++i) {
                this.this$0.doWriteOutputBuffer();
            }
        }
        
        public void handleMessage(final Message message) {
            final int what = message.what;
            Label_0027: {
                if (what == 104) {
                    break Label_0027;
                }
                if (what != 110) {
                    return;
                }
                try {
                    this.doFlushBuffers();
                    return;
                    this.this$0.doWriteOutputBuffer();
                }
                catch (final IllegalStateException ex) {}
            }
        }
    }
    
    protected enum MuxerState
    {
        private static final MuxerState[] $VALUES;
        
        IDLE, 
        STARTED, 
        STOPPED;
        
        static {
            $VALUES = new MuxerState[] { MuxerState.IDLE, MuxerState.STARTED, MuxerState.STOPPED };
        }
    }
    
    protected enum States
    {
        private static final States[] $VALUES;
        
        PAUSED, 
        STARTED, 
        STOPPED, 
        STOPPING;
        
        static {
            $VALUES = new States[] { States.STARTED, States.STOPPED, States.STOPPING, States.PAUSED };
        }
    }
}
