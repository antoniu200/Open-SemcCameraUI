package com.sonyericsson.android.camera.recorder.superslowrecorder;

import android.content.Context;
import android.location.Location;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.Surface;
import com.google.android.gms.common.Scopes;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.recorder.RecorderInterface;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.android.camera.recorder.utility.FpsMonitor;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataInfo;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;
import com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder;
import com.sonyericsson.android.camera.recorder.utility.encoder.source.VideoFrameSource;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VariableSourceMediaRecorder implements RecorderInterface {
    private static final String AUDIO_MIMETYPE = "audio/mp4a-latm";
    private static final boolean ENABLE_MONITOR_FPS = false;
    private static final int I_FRAME_INTERVAL_SECONDS = 1;
    private static final long PROGRESS_NOTIFICATION_INTERVAL_MILLIS = 1000;
    private static final long STOP_RECORDING_TIME_OUT_MILLIS = 5000;
    private static final String TAG = "VariableSourceMediaRecorder";
    private static final boolean TRACE = false;
    private static final String VIDEO_MIMETYPE = "video/avc";
    private RecorderInterface.RecordTrackListener mAudioTrackListener;
    private Context mContext;
    private MediaEncoder mEncoder;
    private InputDataInfo[] mInputDataInfos;
    private InputDataSourceFactory mInputDataSourceFactory;
    private Location mLocation;
    private long mMaxDurationMillis;
    private long mMaxFileSizeBytes;
    private RecorderInterface.OnErrorListener mOnErrorListener;
    private RecorderInterface.OnMaxReachedListener mOnMaxReachedListener;
    private final int mOperatingRate;
    private int mOrientationHint;
    private String mOutputPath;
    private volatile boolean mResult;
    private Surface mSurface;
    private RecorderInterface.RecordTrackListener mVideoTrackListener;
    private CountDownLatch mWaitUntilStarted;
    private CountDownLatch mWaitUntilStoped;
    private State mState = State.IDLE;
    private final Handler mHandler = new Handler();
    private final FpsMonitor mFpsMonitor = null;

    public interface InputDataSourceFactory {
        InputDataSource createAudioSource(MediaCodec mediaCodec, CamcorderProfile camcorderProfile);

        VideoFrameSource createVideoSource(MediaCodec mediaCodec, CamcorderProfile camcorderProfile);
    }

    private enum State {
        RUNNING,
        IDLE
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public boolean isAsyncStopSupported() {
        return false;
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void stopAudioRecording() {
    }

    private static void trace(String str) {
        CamLog.d(str);
    }

    public VariableSourceMediaRecorder(int i) {
        this.mOperatingRate = i;
    }

    public void setInputDataSourceFactory(InputDataSourceFactory inputDataSourceFactory) {
        if (inputDataSourceFactory == null) {
            throw new IllegalArgumentException("This method cannot accept null as InputDataSourceFactory.");
        }
        this.mInputDataSourceFactory = inputDataSourceFactory;
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setListener(RecorderInterface.RecordTrackListener recordTrackListener, RecorderInterface.RecordTrackListener recordTrackListener2, RecorderInterface.OnErrorListener onErrorListener, RecorderInterface.OnMaxReachedListener onMaxReachedListener) {
        this.mAudioTrackListener = recordTrackListener;
        this.mVideoTrackListener = recordTrackListener2;
        this.mOnErrorListener = onErrorListener;
        this.mOnMaxReachedListener = onMaxReachedListener;
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public Surface getSurface() {
        return this.mSurface;
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public boolean prepare(Context context, RecorderParameters recorderParameters) {
        if (this.mState != State.IDLE) {
            throw new IllegalStateException();
        }
        this.mLocation = recorderParameters.location();
        this.mOrientationHint = recorderParameters.orientationHint();
        this.mOutputPath = recorderParameters.outputUri().getPath();
        this.mMaxDurationMillis = recorderParameters.maxDuration();
        this.mMaxFileSizeBytes = recorderParameters.maxFileSize();
        this.mContext = context;
        boolean zIsMicrophoneEnabled = recorderParameters.isMicrophoneEnabled();
        CamcorderProfile camcorderProfileProfile = recorderParameters.profile();
        if (this.mFpsMonitor != null) {
            this.mFpsMonitor.reset();
        }
        try {
            if (zIsMicrophoneEnabled) {
                this.mInputDataInfos = new InputDataInfo[2];
                this.mInputDataInfos[0] = createVideoInputStreamInfo(camcorderProfileProfile);
                this.mInputDataInfos[1] = createAudioInputStreamInfo(camcorderProfileProfile);
            } else {
                this.mInputDataInfos = new InputDataInfo[1];
                this.mInputDataInfos[0] = createVideoInputStreamInfo(camcorderProfileProfile);
            }
            this.mOutputPath = recorderParameters.outputUri().getPath();
            return true;
        } catch (IOException e) {
            if (this.mInputDataInfos != null) {
                for (InputDataInfo inputDataInfo : this.mInputDataInfos) {
                    inputDataInfo.codec.release();
                    inputDataInfo.source.release();
                }
                this.mInputDataInfos = null;
            }
            CamLog.e("prepare() failed : " + e.getMessage());
            return false;
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void start() throws InterruptedException, IOException {
        FileDescriptor fileDescriptor;
        if (this.mState != State.IDLE || this.mContext == null) {
            throw new IllegalStateException();
        }
        if (StorageUtil.getStorageTypeFromPath(this.mOutputPath, this.mContext) == Storage.StorageType.EXTERNAL_CARD) {
            Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(this.mContext);
            try {
                ParcelFileDescriptor parcelFileDescriptorOpenFileDescriptor = this.mContext.getContentResolver().openFileDescriptor(StorageUtil.createFile(this.mContext, sdCardGrantedUri, StorageUtil.getPathAfterDcim(sdCardGrantedUri, this.mOutputPath)), "rw");
                if (parcelFileDescriptorOpenFileDescriptor == null) {
                    CamLog.e("openFileDescriptor fd is null.");
                    throw new RuntimeException("openFileDescriptor fd is null.");
                }
                fileDescriptor = parcelFileDescriptorOpenFileDescriptor.getFileDescriptor();
                this.mOutputPath = null;
            } catch (FileNotFoundException e) {
                CamLog.e("openFileDescriptor failed.", e);
                throw new RuntimeException(e);
            }
        } else {
            fileDescriptor = null;
        }
        try {
            this.mEncoder = new MediaEncoder(this.mInputDataInfos, this.mOutputPath, fileDescriptor, new MediaEncoderStateListener());
            this.mEncoder.setOrientationHint(this.mOrientationHint);
            this.mEncoder.setMaxDuration(this.mMaxDurationMillis);
            this.mEncoder.setMaxFileSize(this.mMaxFileSizeBytes);
            if (this.mLocation != null) {
                this.mEncoder.setLocation((float) this.mLocation.getLatitude(), (float) this.mLocation.getLongitude());
            }
            this.mInputDataInfos = null;
            this.mWaitUntilStarted = new CountDownLatch(1);
            this.mWaitUntilStoped = new CountDownLatch(1);
            this.mState = State.RUNNING;
            this.mEncoder.start();
            try {
                this.mWaitUntilStarted.await();
            } catch (InterruptedException e2) {
                throw new RuntimeException(e2);
            }
        } catch (IOException e3) {
            CamLog.e("start() failed : " + e3.getMessage());
            throw e3;
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void stop() {
        if (this.mState != State.IDLE) {
            this.mEncoder.stop();
            this.mEncoder = null;
        }
        if (this.mFpsMonitor != null) {
            CamLog.d("FPS_MONITOR:" + this.mFpsMonitor.dump());
        }
        try {
            if (!this.mWaitUntilStoped.await(STOP_RECORDING_TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)) {
                CamLog.e("Encoder doesn't finish correctly. Video file may be corrupt.");
            }
            this.mState = State.IDLE;
            if (!this.mResult) {
                throw new RuntimeException("recording failed.");
            }
        } catch (InterruptedException e) {
            CamLog.e("stop failed due to interruption.", e);
            throw new RuntimeException(e);
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void stopAsync() {
        throw new UnsupportedOperationException("#stopAsync() only supported by SomeMediaRecorder");
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void waitUntilStopCompleted() {
        throw new UnsupportedOperationException("#stopAsync() only supported by SomeMediaRecorder");
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void reset() {
        this.mEncoder = null;
        this.mState = State.IDLE;
        if (this.mInputDataInfos != null) {
            for (InputDataInfo inputDataInfo : this.mInputDataInfos) {
                inputDataInfo.codec.release();
                inputDataInfo.source.release();
            }
            this.mInputDataInfos = null;
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void stopOnCameraError() {
        trace("stopOnCameraError() E");
        stop();
        trace("stopOnCameraError() X");
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void release() {
        if (this.mState != State.IDLE) {
            stop();
        } else {
            reset();
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void resume() {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void pause() {
        throw new UnsupportedOperationException("This method is not supported");
    }

    private MediaFormat createVideoFormat(String str, CamcorderProfile camcorderProfile) {
        MediaFormat mediaFormatCreateVideoFormat = MediaFormat.createVideoFormat(str, camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
        mediaFormatCreateVideoFormat.setInteger("color-format", 2130708361);
        mediaFormatCreateVideoFormat.setInteger("bitrate", camcorderProfile.videoBitRate);
        mediaFormatCreateVideoFormat.setInteger("frame-rate", camcorderProfile.videoFrameRate);
        mediaFormatCreateVideoFormat.setInteger("i-frame-interval", 1);
        mediaFormatCreateVideoFormat.setInteger("operating-rate", this.mOperatingRate);
        if (camcorderProfile.videoFrameWidth >= 3840 && camcorderProfile.videoFrameHeight >= 2160) {
            if (camcorderProfile.videoCodec == 2) {
                mediaFormatCreateVideoFormat.setInteger(Scopes.PROFILE, 8);
                mediaFormatCreateVideoFormat.setInteger("level", 1);
            } else {
                mediaFormatCreateVideoFormat.setInteger(Scopes.PROFILE, 1);
                mediaFormatCreateVideoFormat.setInteger("level", 1);
            }
        } else if (camcorderProfile.videoFrameWidth >= 640 && camcorderProfile.videoFrameHeight >= 480) {
            mediaFormatCreateVideoFormat.setInteger(Scopes.PROFILE, 8);
            mediaFormatCreateVideoFormat.setInteger("level", 1);
        } else {
            mediaFormatCreateVideoFormat.setInteger(Scopes.PROFILE, 1);
            mediaFormatCreateVideoFormat.setInteger("level", 1);
        }
        return mediaFormatCreateVideoFormat;
    }

    private MediaFormat createAudioFormat(String str, CamcorderProfile camcorderProfile) {
        MediaFormat mediaFormatCreateAudioFormat = MediaFormat.createAudioFormat(str, camcorderProfile.audioSampleRate, camcorderProfile.audioChannels);
        mediaFormatCreateAudioFormat.setInteger("bitrate", camcorderProfile.audioBitRate);
        mediaFormatCreateAudioFormat.setInteger("aac-profile", 2);
        return mediaFormatCreateAudioFormat;
    }

    private InputDataInfo createVideoInputStreamInfo(CamcorderProfile camcorderProfile) throws IOException {
        MediaFormat mediaFormatCreateVideoFormat = createVideoFormat(VIDEO_MIMETYPE, camcorderProfile);
        MediaCodec mediaCodecCreateEncoderByType = MediaCodec.createEncoderByType(VIDEO_MIMETYPE);
        mediaCodecCreateEncoderByType.configure(mediaFormatCreateVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        VideoFrameSource videoFrameSourceCreateVideoSource = this.mInputDataSourceFactory.createVideoSource(mediaCodecCreateEncoderByType, camcorderProfile);
        this.mSurface = videoFrameSourceCreateVideoSource.createInputSurface();
        return InputDataInfo.create(mediaFormatCreateVideoFormat, mediaCodecCreateEncoderByType, videoFrameSourceCreateVideoSource);
    }

    private InputDataInfo createAudioInputStreamInfo(CamcorderProfile camcorderProfile) throws IOException {
        MediaFormat mediaFormatCreateAudioFormat = createAudioFormat(AUDIO_MIMETYPE, camcorderProfile);
        MediaCodec mediaCodecCreateEncoderByType = MediaCodec.createEncoderByType(AUDIO_MIMETYPE);
        mediaCodecCreateEncoderByType.configure(mediaFormatCreateAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        return InputDataInfo.create(mediaFormatCreateAudioFormat, mediaCodecCreateEncoderByType, this.mInputDataSourceFactory.createAudioSource(mediaCodecCreateEncoderByType, camcorderProfile));
    }

    private class MediaEncoderStateListener implements MediaEncoder.StateListener {
        private long mLastNotifyProgressMillis = 0;

        public MediaEncoderStateListener() {
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder.StateListener
        public void onStarted() {
            VariableSourceMediaRecorder.this.mWaitUntilStarted.countDown();
            VariableSourceMediaRecorder.this.mHandler.post(new StartNotificationTask());
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder.StateListener
        public void onFinished(boolean z) {
            if (!z) {
                File file = new File(VariableSourceMediaRecorder.this.mOutputPath);
                if (file.exists()) {
                    Context context = CameraApplication.getContext();
                    if (StorageUtil.getStorageTypeFromPath(VariableSourceMediaRecorder.this.mOutputPath, CameraApplication.getContext()) == Storage.StorageType.EXTERNAL_CARD) {
                        Uri uriSearchDocumentSdCard = StorageUtil.searchDocumentSdCard(context, VariableSourceMediaRecorder.this.mOutputPath);
                        if (uriSearchDocumentSdCard != null) {
                            try {
                                if (!DocumentsContract.deleteDocument(context.getContentResolver(), uriSearchDocumentSdCard)) {
                                    CamLog.e("Unable to delete file.");
                                }
                            } catch (FileNotFoundException e) {
                                CamLog.e("Unable to delete file." + e);
                            }
                        }
                    } else if (!file.delete()) {
                        CamLog.e("Unable to delete file.");
                    }
                }
            }
            VariableSourceMediaRecorder.this.mResult = z;
            VariableSourceMediaRecorder.this.mWaitUntilStoped.countDown();
            VariableSourceMediaRecorder.this.mHandler.post(new FinishNotificationTask());
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder.StateListener
        public synchronized void onProgress(long j) {
            long j2 = j / 1000;
            if (j2 - this.mLastNotifyProgressMillis >= 1000) {
                this.mLastNotifyProgressMillis = j2;
                VariableSourceMediaRecorder.this.mHandler.post(new ProgressNotificationTask(this.mLastNotifyProgressMillis));
            }
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder.StateListener
        public void onMaxDurationReached() {
            if (CamLog.DEBUG) {
                CamLog.d(VariableSourceMediaRecorder.TAG, "reached max duration.");
            }
            VariableSourceMediaRecorder.this.mOnMaxReachedListener.onMaxDurationReached();
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder.StateListener
        public void onMaxFileSizeReached() {
            if (CamLog.DEBUG) {
                CamLog.d(VariableSourceMediaRecorder.TAG, "reached max size.");
            }
            VariableSourceMediaRecorder.this.mOnMaxReachedListener.onMaxFileSizeReached();
        }

        private class StartNotificationTask implements Runnable {
            private StartNotificationTask() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VariableSourceMediaRecorder.this.mAudioTrackListener.onStarted();
                VariableSourceMediaRecorder.this.mVideoTrackListener.onStarted();
            }
        }

        private class FinishNotificationTask implements Runnable {
            private FinishNotificationTask() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VariableSourceMediaRecorder.this.mAudioTrackListener.onCompleted();
                VariableSourceMediaRecorder.this.mVideoTrackListener.onCompleted();
            }
        }

        private class ProgressNotificationTask implements Runnable {
            private final long mTimeMillis;

            public ProgressNotificationTask(long j) {
                this.mTimeMillis = j;
            }

            @Override // java.lang.Runnable
            public void run() {
                VariableSourceMediaRecorder.this.mAudioTrackListener.onProgress(this.mTimeMillis);
                VariableSourceMediaRecorder.this.mVideoTrackListener.onProgress(this.mTimeMillis);
            }
        }

        @Override // com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder.StateListener
        public void onStorageFull() {
            VariableSourceMediaRecorder.this.mEncoder.stop();
            VariableSourceMediaRecorder.this.mHandler.post(new ErrorNotificationTask());
        }

        private class ErrorNotificationTask implements Runnable {
            private ErrorNotificationTask() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VariableSourceMediaRecorder.this.mOnErrorListener.onError();
            }
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setLocation(Location location) {
        this.mLocation = location;
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setMaxDurationMillis(long j) {
        this.mMaxDurationMillis = j;
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setMaxFileSizeBytes(long j) {
        this.mMaxFileSizeBytes = Math.min(j, 256000000000L);
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setOutputFilePath(String str) {
        this.mOutputPath = str;
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setOrientationHint(int i) {
        this.mOrientationHint = i;
    }
}
