package com.sonyericsson.android.camera.recorder.utility.encoder;

import android.media.MediaFormat;
import com.sonyericsson.android.camera.recorder.utility.encoder.EncodedDataWriteTask;
import com.sonyericsson.android.camera.recorder.utility.encoder.MediaMuxerWrapper;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MediaEncoder {
    public static final String TAG = "MediaEncoder";
    private static final String THREAD_NAME_DATA_WRITE_FOR_EACH_STREAMS = "ME#WriteData";
    private static final String THREAD_NAME_MAIN_TASK = "ME#MainTask";
    private static final String THREAD_NAME_WAIT_TO_COMPLETE_ENCODING = "ME#WaitFinish";
    private static boolean TRACE = true;
    private CountDownLatch mEncoderFinishSignal;
    private CountDownLatch mEncoderFormatChangedSignal;
    private ExecutorService mInputEncodedDataThreadPool;
    private final InputDataInfo[] mInputStreams;
    private ExecutorService mMainTaskExecutor;
    private final MediaMuxerWrapper mMuxer;
    private CountDownLatch mMuxerStartedSignal;
    private final EncodingStateNotifier mNotifier;
    private CountDownLatch mRequestFinishSignal;
    private final Runnable mMainTask = new Runnable() { // from class: com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder.1
        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            if (MediaEncoder.TRACE) {
                CamLog.d("### START RECODING ###");
            }
            MediaEncoder.this.mEncoderFormatChangedSignal = new CountDownLatch(MediaEncoder.this.mInputStreams.length);
            boolean z = true;
            MediaEncoder.this.mMuxerStartedSignal = new CountDownLatch(1);
            MediaEncoder.this.mEncoderFinishSignal = new CountDownLatch(MediaEncoder.this.mInputStreams.length);
            MediaEncoder.this.startEncoders();
            MediaEncoder.this.startInputDataSource();
            MediaEncoder.this.startEncodedDataWriteTasks();
            MediaEncoder.this.sendOnStartedEvent();
            try {
                MediaEncoder.this.startMediaMuxerAfterEncodedFormatIsFixed();
                MediaEncoder.this.waitToCompleteEncoding();
                MediaEncoder.this.stopEncoders();
                try {
                    MediaEncoder.this.stopMuxer();
                } catch (IllegalStateException unused) {
                    CamLog.e("IllegalStateException occur at stopMuxer().");
                    z = false;
                }
                MediaEncoder.this.release();
                MediaEncoder.this.sendOnFinishedEvent(z);
                if (MediaEncoder.TRACE) {
                    CamLog.d("### END RECORDING ###");
                }
            } catch (InterruptedException unused2) {
                CamLog.e("startMediaMuxerAfterEncodedFormatIsFixed() is interrupted");
                MediaEncoder.this.stopEncoders();
            }
        }
    };
    private final EncodedDataWriteTask.EncoderStateListener mEncodedDataWriteListener = new EncodedDataWriteTask.EncoderStateListener() { // from class: com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder.2
        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.EncodedDataWriteTask.EncoderStateListener
        public void onEncoderFormatChanged(MediaFormat mediaFormat) throws InterruptedException {
            MediaEncoder.this.mEncoderFormatChangedSignal.countDown();
            try {
                MediaEncoder.this.mMuxerStartedSignal.await();
            } catch (InterruptedException unused) {
                CamLog.e("mMuxerStartedSignal is interrupted.");
            }
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.EncodedDataWriteTask.EncoderStateListener
        public void onEncoderFinished() {
            MediaEncoder.this.mEncoderFinishSignal.countDown();
        }
    };
    private final Runnable mWaitRequestFinishSignalTask = new Runnable() { // from class: com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder.3
        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            try {
                MediaEncoder.this.mRequestFinishSignal.await();
                if (MediaEncoder.TRACE) {
                    CamLog.d("Start finalization of recording.");
                }
                try {
                    MediaEncoder.this.stopInputDataSource();
                } catch (InterruptedException unused) {
                    CamLog.e("stopInputDataSource is interrupted");
                }
            } catch (InterruptedException unused2) {
                CamLog.e("mRequestFinishSignal is interrupted");
            }
        }
    };

    public interface StateListener {
        void onFinished(boolean z);

        void onMaxDurationReached();

        void onMaxFileSizeReached();

        void onProgress(long j);

        void onStarted();

        void onStorageFull();
    }

    public MediaEncoder(InputDataInfo[] inputDataInfoArr, String str, FileDescriptor fileDescriptor, StateListener stateListener) throws IOException {
        this.mNotifier = stateListener == null ? null : new EncodingStateNotifier(stateListener);
        if (fileDescriptor != null) {
            this.mMuxer = new MediaMuxerWrapper(fileDescriptor, 0, this.mNotifier);
        } else {
            this.mMuxer = new MediaMuxerWrapper(str, 0, this.mNotifier);
        }
        this.mInputStreams = inputDataInfoArr;
        this.mRequestFinishSignal = null;
        this.mInputEncodedDataThreadPool = ThreadUtil.buildPoolExecutor(THREAD_NAME_DATA_WRITE_FOR_EACH_STREAMS, this.mInputStreams.length);
    }

    public void start() {
        this.mMainTaskExecutor = ThreadUtil.buildExecutor(THREAD_NAME_MAIN_TASK);
        synchronized (this) {
            if (this.mRequestFinishSignal != null) {
                throw new IllegalStateException();
            }
            this.mRequestFinishSignal = new CountDownLatch(1);
        }
        this.mMainTaskExecutor.execute(this.mMainTask);
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

    public void release() {
        shutdownEncodedDataThreadPool();
        releaseInputDataSource();
        releaseEncoders();
        try {
            releaseMuxer();
        } catch (IllegalStateException unused) {
            CamLog.e("IllegalStateException occur at releaseMuxer().");
        }
    }

    private static class EncodingStateNotifier implements MediaMuxerWrapper.MuxerListener {
        private final StateListener mStateListener;

        EncodingStateNotifier(StateListener stateListener) {
            this.mStateListener = stateListener;
        }

        public void notifyOnStarted() {
            this.mStateListener.onStarted();
        }

        public void notifyOnFinished(boolean z) {
            this.mStateListener.onFinished(z);
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaMuxerWrapper.MuxerListener
        public void onProgress(long j) {
            this.mStateListener.onProgress(j);
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaMuxerWrapper.MuxerListener
        public void onMaxDurationReached() {
            this.mStateListener.onMaxDurationReached();
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaMuxerWrapper.MuxerListener
        public void onMaxFileSizeReached() {
            this.mStateListener.onMaxFileSizeReached();
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaMuxerWrapper.MuxerListener
        public void onStorageFull() {
            this.mStateListener.onStorageFull();
        }
    }

    void startMediaMuxerAfterEncodedFormatIsFixed() throws InterruptedException {
        if (TRACE) {
            CamLog.d("startMediaMuxer() E");
        }
        this.mEncoderFormatChangedSignal.await();
        if (TRACE) {
            CamLog.d("muxer.start E");
        }
        this.mMuxer.start();
        if (TRACE) {
            CamLog.d("muxer.start X");
        }
        this.mMuxerStartedSignal.countDown();
        if (TRACE) {
            CamLog.d("startMediaMuxer() X");
        }
    }

    void startEncodedDataWriteTasks() {
        if (TRACE) {
            CamLog.d("startEncodedDataWriteTasks() E");
        }
        for (InputDataInfo inputDataInfo : this.mInputStreams) {
            this.mInputEncodedDataThreadPool.execute(new EncodedDataWriteTask(this.mMuxer, inputDataInfo.codec, this.mEncodedDataWriteListener, inputDataInfo.mimeType()));
        }
        if (TRACE) {
            CamLog.d("startEncodedDataWriteTasks() X");
        }
    }

    private void shutdownEncodedDataThreadPool() {
        this.mInputEncodedDataThreadPool.shutdown();
    }

    void startEncoders() {
        if (TRACE) {
            CamLog.d("startEncoders() E");
        }
        for (InputDataInfo inputDataInfo : this.mInputStreams) {
            inputDataInfo.codec.start();
        }
        if (TRACE) {
            CamLog.d("startEncoders() X");
        }
    }

    void stopEncoders() {
        if (TRACE) {
            CamLog.d("stopEncoders() E");
        }
        for (InputDataInfo inputDataInfo : this.mInputStreams) {
            inputDataInfo.codec.stop();
        }
        if (TRACE) {
            CamLog.d("stopEncoders() X");
        }
    }

    void releaseEncoders() {
        if (TRACE) {
            CamLog.d("releaseEncoders() E");
        }
        for (InputDataInfo inputDataInfo : this.mInputStreams) {
            inputDataInfo.codec.release();
        }
        if (TRACE) {
            CamLog.d("releaseEncoders() X");
        }
    }

    void startInputDataSource() {
        for (InputDataInfo inputDataInfo : this.mInputStreams) {
            inputDataInfo.source.start();
        }
    }

    void stopInputDataSource() throws InterruptedException {
        for (InputDataInfo inputDataInfo : this.mInputStreams) {
            inputDataInfo.source.stop();
        }
    }

    void releaseInputDataSource() {
        for (InputDataInfo inputDataInfo : this.mInputStreams) {
            inputDataInfo.source.release();
        }
    }

    void stopMuxer() {
        if (TRACE) {
            CamLog.d("stopMuxer() E");
        }
        this.mMuxer.stop();
        if (TRACE) {
            CamLog.d("stopMuxer() X");
        }
    }

    void releaseMuxer() {
        if (TRACE) {
            CamLog.d("releaseMuxer() E");
        }
        this.mMuxer.release();
        if (TRACE) {
            CamLog.d("releaseMuxer() X");
        }
    }

    void waitToCompleteEncoding() throws InterruptedException {
        if (TRACE) {
            CamLog.d("waitToCompleteEncoding() E");
        }
        ExecutorService executorServiceBuildExecutor = ThreadUtil.buildExecutor(THREAD_NAME_WAIT_TO_COMPLETE_ENCODING);
        Future<?> futureSubmit = executorServiceBuildExecutor.submit(this.mWaitRequestFinishSignalTask);
        try {
            if (TRACE) {
                CamLog.d("EncoderFinishSignal.await");
            }
            this.mEncoderFinishSignal.await();
            if (TRACE) {
                CamLog.d("EncoderFinishSignal.await FINISHED");
            }
        } catch (InterruptedException unused) {
            CamLog.e("mEncoderFinishSignal is interrupted");
        }
        futureSubmit.cancel(true);
        executorServiceBuildExecutor.shutdown();
        if (TRACE) {
            CamLog.d("waitToCompleteEncoding() X");
        }
    }

    void sendOnStartedEvent() {
        if (this.mNotifier != null) {
            this.mNotifier.notifyOnStarted();
        }
    }

    void sendOnFinishedEvent(boolean z) {
        synchronized (this) {
            this.mRequestFinishSignal = null;
        }
        if (this.mNotifier != null) {
            this.mNotifier.notifyOnFinished(z);
        }
    }

    public void setMaxDuration(long j) {
        this.mMuxer.setMaxDuration(j);
    }

    public void setMaxFileSize(long j) {
        this.mMuxer.setMaxFileSize(j);
    }

    public void setOrientationHint(int i) {
        this.mMuxer.setOrientationHint(i);
    }

    public void setLocation(float f, float f2) {
        this.mMuxer.setLocation(f, f2);
    }
}
