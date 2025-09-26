// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.utility.encoder;

import java.util.concurrent.Future;
import java.io.IOException;
import com.sonyericsson.android.camera.util.ThreadUtil;
import android.media.MediaFormat;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.FileDescriptor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CountDownLatch;

public class MediaEncoder
{
    public static final String TAG = "MediaEncoder";
    private static final String THREAD_NAME_DATA_WRITE_FOR_EACH_STREAMS = "ME#WriteData";
    private static final String THREAD_NAME_MAIN_TASK = "ME#MainTask";
    private static final String THREAD_NAME_WAIT_TO_COMPLETE_ENCODING = "ME#WaitFinish";
    private static boolean TRACE = true;
    private final EncodedDataWriteTask.EncoderStateListener mEncodedDataWriteListener;
    private CountDownLatch mEncoderFinishSignal;
    private CountDownLatch mEncoderFormatChangedSignal;
    private ExecutorService mInputEncodedDataThreadPool;
    private final InputDataInfo[] mInputStreams;
    private final Runnable mMainTask;
    private ExecutorService mMainTaskExecutor;
    private final MediaMuxerWrapper mMuxer;
    private CountDownLatch mMuxerStartedSignal;
    private final EncodingStateNotifier mNotifier;
    private CountDownLatch mRequestFinishSignal;
    private final Runnable mWaitRequestFinishSignalTask;
    
    public MediaEncoder(final InputDataInfo[] mInputStreams, final String s, final FileDescriptor fileDescriptor, final StateListener stateListener) throws IOException {
        this.mMainTask = new Runnable() {
            final MediaEncoder this$0;
            
            @Override
            public void run() {
                if (MediaEncoder.TRACE) {
                    CamLog.d("### START RECODING ###");
                }
                this.this$0.mEncoderFormatChangedSignal = new CountDownLatch(this.this$0.mInputStreams.length);
                final MediaEncoder this$0 = this.this$0;
                boolean b = true;
                this$0.mMuxerStartedSignal = new CountDownLatch(1);
                this.this$0.mEncoderFinishSignal = new CountDownLatch(this.this$0.mInputStreams.length);
                this.this$0.startEncoders();
                this.this$0.startInputDataSource();
                this.this$0.startEncodedDataWriteTasks();
                this.this$0.sendOnStartedEvent();
                try {
                    this.this$0.startMediaMuxerAfterEncodedFormatIsFixed();
                    this.this$0.waitToCompleteEncoding();
                    this.this$0.stopEncoders();
                    try {
                        this.this$0.stopMuxer();
                    }
                    catch (final IllegalStateException ex) {
                        CamLog.e("IllegalStateException occur at stopMuxer().");
                        b = false;
                    }
                    this.this$0.release();
                    this.this$0.sendOnFinishedEvent(b);
                    if (MediaEncoder.TRACE) {
                        CamLog.d("### END RECORDING ###");
                    }
                }
                catch (final InterruptedException ex2) {
                    CamLog.e("startMediaMuxerAfterEncodedFormatIsFixed() is interrupted");
                    this.this$0.stopEncoders();
                }
            }
        };
        this.mEncodedDataWriteListener = new EncodedDataWriteTask.EncoderStateListener() {
            final MediaEncoder this$0;
            
            @Override
            public void onEncoderFinished() {
                this.this$0.mEncoderFinishSignal.countDown();
            }
            
            @Override
            public void onEncoderFormatChanged(final MediaFormat mediaFormat) {
                this.this$0.mEncoderFormatChangedSignal.countDown();
                try {
                    this.this$0.mMuxerStartedSignal.await();
                }
                catch (final InterruptedException ex) {
                    CamLog.e("mMuxerStartedSignal is interrupted.");
                }
            }
        };
        this.mWaitRequestFinishSignalTask = new Runnable() {
            final MediaEncoder this$0;
            
            @Override
            public void run() {
                try {
                    this.this$0.mRequestFinishSignal.await();
                    if (MediaEncoder.TRACE) {
                        CamLog.d("Start finalization of recording.");
                    }
                    try {
                        this.this$0.stopInputDataSource();
                    }
                    catch (final InterruptedException ex) {
                        CamLog.e("stopInputDataSource is interrupted");
                    }
                }
                catch (final InterruptedException ex2) {
                    CamLog.e("mRequestFinishSignal is interrupted");
                }
            }
        };
        EncodingStateNotifier mNotifier;
        if (stateListener == null) {
            mNotifier = null;
        }
        else {
            mNotifier = new EncodingStateNotifier(stateListener);
        }
        this.mNotifier = mNotifier;
        if (fileDescriptor != null) {
            this.mMuxer = new MediaMuxerWrapper(fileDescriptor, 0, (MediaMuxerWrapper.MuxerListener)this.mNotifier);
        }
        else {
            this.mMuxer = new MediaMuxerWrapper(s, 0, (MediaMuxerWrapper.MuxerListener)this.mNotifier);
        }
        this.mInputStreams = mInputStreams;
        this.mRequestFinishSignal = null;
        this.mInputEncodedDataThreadPool = ThreadUtil.buildPoolExecutor("ME#WriteData", this.mInputStreams.length);
    }
    
    private void shutdownEncodedDataThreadPool() {
        this.mInputEncodedDataThreadPool.shutdown();
    }
    
    public void release() {
        this.shutdownEncodedDataThreadPool();
        this.releaseInputDataSource();
        this.releaseEncoders();
        try {
            this.releaseMuxer();
        }
        catch (final IllegalStateException ex) {
            CamLog.e("IllegalStateException occur at releaseMuxer().");
        }
    }
    
    void releaseEncoders() {
        if (MediaEncoder.TRACE) {
            CamLog.d("releaseEncoders() E");
        }
        final InputDataInfo[] mInputStreams = this.mInputStreams;
        for (int length = mInputStreams.length, i = 0; i < length; ++i) {
            mInputStreams[i].codec.release();
        }
        if (MediaEncoder.TRACE) {
            CamLog.d("releaseEncoders() X");
        }
    }
    
    void releaseInputDataSource() {
        final InputDataInfo[] mInputStreams = this.mInputStreams;
        for (int length = mInputStreams.length, i = 0; i < length; ++i) {
            mInputStreams[i].source.release();
        }
    }
    
    void releaseMuxer() {
        if (MediaEncoder.TRACE) {
            CamLog.d("releaseMuxer() E");
        }
        this.mMuxer.release();
        if (MediaEncoder.TRACE) {
            CamLog.d("releaseMuxer() X");
        }
    }
    
    void sendOnFinishedEvent(final boolean b) {
        synchronized (this) {
            this.mRequestFinishSignal = null;
            monitorexit(this);
            if (this.mNotifier != null) {
                this.mNotifier.notifyOnFinished(b);
            }
        }
    }
    
    void sendOnStartedEvent() {
        if (this.mNotifier != null) {
            this.mNotifier.notifyOnStarted();
        }
    }
    
    public void setLocation(final float n, final float n2) {
        this.mMuxer.setLocation(n, n2);
    }
    
    public void setMaxDuration(final long maxDuration) {
        this.mMuxer.setMaxDuration(maxDuration);
    }
    
    public void setMaxFileSize(final long maxFileSize) {
        this.mMuxer.setMaxFileSize(maxFileSize);
    }
    
    public void setOrientationHint(final int orientationHint) {
        this.mMuxer.setOrientationHint(orientationHint);
    }
    
    public void start() {
        this.mMainTaskExecutor = ThreadUtil.buildExecutor("ME#MainTask");
        synchronized (this) {
            if (this.mRequestFinishSignal != null) {
                throw new IllegalStateException();
            }
            this.mRequestFinishSignal = new CountDownLatch(1);
            monitorexit(this);
            this.mMainTaskExecutor.execute(this.mMainTask);
        }
    }
    
    void startEncodedDataWriteTasks() {
        if (MediaEncoder.TRACE) {
            CamLog.d("startEncodedDataWriteTasks() E");
        }
        for (final InputDataInfo inputDataInfo : this.mInputStreams) {
            this.mInputEncodedDataThreadPool.execute(new EncodedDataWriteTask(this.mMuxer, inputDataInfo.codec, this.mEncodedDataWriteListener, inputDataInfo.mimeType()));
        }
        if (MediaEncoder.TRACE) {
            CamLog.d("startEncodedDataWriteTasks() X");
        }
    }
    
    void startEncoders() {
        if (MediaEncoder.TRACE) {
            CamLog.d("startEncoders() E");
        }
        final InputDataInfo[] mInputStreams = this.mInputStreams;
        for (int length = mInputStreams.length, i = 0; i < length; ++i) {
            mInputStreams[i].codec.start();
        }
        if (MediaEncoder.TRACE) {
            CamLog.d("startEncoders() X");
        }
    }
    
    void startInputDataSource() {
        final InputDataInfo[] mInputStreams = this.mInputStreams;
        for (int length = mInputStreams.length, i = 0; i < length; ++i) {
            mInputStreams[i].source.start();
        }
    }
    
    void startMediaMuxerAfterEncodedFormatIsFixed() throws InterruptedException {
        if (MediaEncoder.TRACE) {
            CamLog.d("startMediaMuxer() E");
        }
        this.mEncoderFormatChangedSignal.await();
        if (MediaEncoder.TRACE) {
            CamLog.d("muxer.start E");
        }
        this.mMuxer.start();
        if (MediaEncoder.TRACE) {
            CamLog.d("muxer.start X");
        }
        this.mMuxerStartedSignal.countDown();
        if (MediaEncoder.TRACE) {
            CamLog.d("startMediaMuxer() X");
        }
    }
    
    public void stop() {
        if (this.mMainTaskExecutor != null && !this.mMainTaskExecutor.isShutdown()) {
            this.mMainTaskExecutor.shutdown();
        }
        synchronized (this) {
            if (this.mRequestFinishSignal == null) {
                return;
            }
            this.mRequestFinishSignal.countDown();
        }
    }
    
    void stopEncoders() {
        if (MediaEncoder.TRACE) {
            CamLog.d("stopEncoders() E");
        }
        final InputDataInfo[] mInputStreams = this.mInputStreams;
        for (int length = mInputStreams.length, i = 0; i < length; ++i) {
            mInputStreams[i].codec.stop();
        }
        if (MediaEncoder.TRACE) {
            CamLog.d("stopEncoders() X");
        }
    }
    
    void stopInputDataSource() throws InterruptedException {
        final InputDataInfo[] mInputStreams = this.mInputStreams;
        for (int length = mInputStreams.length, i = 0; i < length; ++i) {
            mInputStreams[i].source.stop();
        }
    }
    
    void stopMuxer() {
        if (MediaEncoder.TRACE) {
            CamLog.d("stopMuxer() E");
        }
        this.mMuxer.stop();
        if (MediaEncoder.TRACE) {
            CamLog.d("stopMuxer() X");
        }
    }
    
    void waitToCompleteEncoding() {
        if (MediaEncoder.TRACE) {
            CamLog.d("waitToCompleteEncoding() E");
        }
        final ExecutorService buildExecutor = ThreadUtil.buildExecutor("ME#WaitFinish");
        final Future<?> submit = buildExecutor.submit(this.mWaitRequestFinishSignalTask);
        try {
            if (MediaEncoder.TRACE) {
                CamLog.d("EncoderFinishSignal.await");
            }
            this.mEncoderFinishSignal.await();
            if (MediaEncoder.TRACE) {
                CamLog.d("EncoderFinishSignal.await FINISHED");
            }
        }
        catch (final InterruptedException ex) {
            CamLog.e("mEncoderFinishSignal is interrupted");
        }
        submit.cancel(true);
        buildExecutor.shutdown();
        if (MediaEncoder.TRACE) {
            CamLog.d("waitToCompleteEncoding() X");
        }
    }
    
    private static class EncodingStateNotifier implements MuxerListener
    {
        private final StateListener mStateListener;
        
        EncodingStateNotifier(final StateListener mStateListener) {
            this.mStateListener = mStateListener;
        }
        
        public void notifyOnFinished(final boolean b) {
            this.mStateListener.onFinished(b);
        }
        
        public void notifyOnStarted() {
            this.mStateListener.onStarted();
        }
        
        @Override
        public void onMaxDurationReached() {
            this.mStateListener.onMaxDurationReached();
        }
        
        @Override
        public void onMaxFileSizeReached() {
            this.mStateListener.onMaxFileSizeReached();
        }
        
        @Override
        public void onProgress(final long n) {
            this.mStateListener.onProgress(n);
        }
        
        @Override
        public void onStorageFull() {
            this.mStateListener.onStorageFull();
        }
    }
    
    public interface StateListener
    {
        void onFinished(final boolean p0);
        
        void onMaxDurationReached();
        
        void onMaxFileSizeReached();
        
        void onProgress(final long p0);
        
        void onStarted();
        
        void onStorageFull();
    }
}
