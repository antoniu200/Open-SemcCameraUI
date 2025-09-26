// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.superslowrecorder;

import android.provider.DocumentsContract;
import com.sonyericsson.android.camera.CameraApplication;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.io.FileDescriptor;
import android.os.ParcelFileDescriptor;
import android.net.Uri;
import java.io.FileNotFoundException;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.recorder.utility.encoder.source.VideoFrameSource;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;
import java.io.IOException;
import android.media.MediaCrypto;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.CamcorderProfile;
import java.util.concurrent.CountDownLatch;
import android.view.Surface;
import android.location.Location;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataInfo;
import android.os.Handler;
import com.sonyericsson.android.camera.recorder.utility.FpsMonitor;
import com.sonyericsson.android.camera.recorder.utility.encoder.MediaEncoder;
import android.content.Context;
import com.sonyericsson.android.camera.recorder.RecorderInterface;

public class VariableSourceMediaRecorder implements RecorderInterface
{
    private static final String AUDIO_MIMETYPE = "audio/mp4a-latm";
    private static final boolean ENABLE_MONITOR_FPS = false;
    private static final int I_FRAME_INTERVAL_SECONDS = 1;
    private static final long PROGRESS_NOTIFICATION_INTERVAL_MILLIS = 1000L;
    private static final long STOP_RECORDING_TIME_OUT_MILLIS = 5000L;
    private static final String TAG = "VariableSourceMediaRecorder";
    private static final boolean TRACE = false;
    private static final String VIDEO_MIMETYPE = "video/avc";
    private RecordTrackListener mAudioTrackListener;
    private Context mContext;
    private MediaEncoder mEncoder;
    private final FpsMonitor mFpsMonitor;
    private final Handler mHandler;
    private InputDataInfo[] mInputDataInfos;
    private InputDataSourceFactory mInputDataSourceFactory;
    private Location mLocation;
    private long mMaxDurationMillis;
    private long mMaxFileSizeBytes;
    private OnErrorListener mOnErrorListener;
    private OnMaxReachedListener mOnMaxReachedListener;
    private final int mOperatingRate;
    private int mOrientationHint;
    private String mOutputPath;
    private volatile boolean mResult;
    private State mState;
    private Surface mSurface;
    private RecordTrackListener mVideoTrackListener;
    private CountDownLatch mWaitUntilStarted;
    private CountDownLatch mWaitUntilStoped;
    
    public VariableSourceMediaRecorder(final int mOperatingRate) {
        this.mState = State.IDLE;
        this.mHandler = new Handler();
        this.mOperatingRate = mOperatingRate;
        this.mFpsMonitor = null;
    }
    
    private MediaFormat createAudioFormat(final String s, final CamcorderProfile camcorderProfile) {
        final MediaFormat audioFormat = MediaFormat.createAudioFormat(s, camcorderProfile.audioSampleRate, camcorderProfile.audioChannels);
        audioFormat.setInteger("bitrate", camcorderProfile.audioBitRate);
        audioFormat.setInteger("aac-profile", 2);
        return audioFormat;
    }
    
    private InputDataInfo createAudioInputStreamInfo(final CamcorderProfile camcorderProfile) throws IOException {
        final MediaFormat audioFormat = this.createAudioFormat("audio/mp4a-latm", camcorderProfile);
        final MediaCodec encoderByType = MediaCodec.createEncoderByType("audio/mp4a-latm");
        encoderByType.configure(audioFormat, (Surface)null, (MediaCrypto)null, 1);
        return InputDataInfo.create(audioFormat, encoderByType, this.mInputDataSourceFactory.createAudioSource(encoderByType, camcorderProfile));
    }
    
    private MediaFormat createVideoFormat(final String s, final CamcorderProfile camcorderProfile) {
        final MediaFormat videoFormat = MediaFormat.createVideoFormat(s, camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
        videoFormat.setInteger("color-format", 2130708361);
        videoFormat.setInteger("bitrate", camcorderProfile.videoBitRate);
        videoFormat.setInteger("frame-rate", camcorderProfile.videoFrameRate);
        videoFormat.setInteger("i-frame-interval", 1);
        videoFormat.setInteger("operating-rate", this.mOperatingRate);
        if (camcorderProfile.videoFrameWidth >= 3840 && camcorderProfile.videoFrameHeight >= 2160) {
            if (camcorderProfile.videoCodec == 2) {
                videoFormat.setInteger("profile", 8);
                videoFormat.setInteger("level", 1);
            }
            else {
                videoFormat.setInteger("profile", 1);
                videoFormat.setInteger("level", 1);
            }
        }
        else if (camcorderProfile.videoFrameWidth >= 640 && camcorderProfile.videoFrameHeight >= 480) {
            videoFormat.setInteger("profile", 8);
            videoFormat.setInteger("level", 1);
        }
        else {
            videoFormat.setInteger("profile", 1);
            videoFormat.setInteger("level", 1);
        }
        return videoFormat;
    }
    
    private InputDataInfo createVideoInputStreamInfo(final CamcorderProfile camcorderProfile) throws IOException {
        final MediaFormat videoFormat = this.createVideoFormat("video/avc", camcorderProfile);
        final MediaCodec encoderByType = MediaCodec.createEncoderByType("video/avc");
        encoderByType.configure(videoFormat, (Surface)null, (MediaCrypto)null, 1);
        final VideoFrameSource videoSource = this.mInputDataSourceFactory.createVideoSource(encoderByType, camcorderProfile);
        this.mSurface = videoSource.createInputSurface();
        return InputDataInfo.create(videoFormat, encoderByType, videoSource);
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    @Override
    public Surface getSurface() {
        return this.mSurface;
    }
    
    @Override
    public boolean isAsyncStopSupported() {
        return false;
    }
    
    @Override
    public void pause() {
        throw new UnsupportedOperationException("This method is not supported");
    }
    
    @Override
    public boolean prepare(final Context mContext, final RecorderParameters recorderParameters) {
        if (this.mState != State.IDLE) {
            throw new IllegalStateException();
        }
        this.mLocation = recorderParameters.location();
        this.mOrientationHint = recorderParameters.orientationHint();
        this.mOutputPath = recorderParameters.outputUri().getPath();
        this.mMaxDurationMillis = recorderParameters.maxDuration();
        this.mMaxFileSizeBytes = recorderParameters.maxFileSize();
        this.mContext = mContext;
        final boolean microphoneEnabled = recorderParameters.isMicrophoneEnabled();
        final CamcorderProfile profile = recorderParameters.profile();
        if (this.mFpsMonitor != null) {
            this.mFpsMonitor.reset();
        }
        Label_0134: {
            if (!microphoneEnabled) {
                break Label_0134;
            }
            while (true) {
                try {
                    (this.mInputDataInfos = new InputDataInfo[2])[0] = this.createVideoInputStreamInfo(profile);
                    this.mInputDataInfos[1] = this.createAudioInputStreamInfo(profile);
                    break Label_0153;
                }
                catch (final IOException ex) {
                    if (this.mInputDataInfos != null) {
                        for (final InputDataInfo inputDataInfo : this.mInputDataInfos) {
                            inputDataInfo.codec.release();
                            inputDataInfo.source.release();
                        }
                        this.mInputDataInfos = null;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("prepare() failed : ");
                    sb.append(ex.getMessage());
                    CamLog.e(sb.toString());
                    return false;
                    this.mOutputPath = recorderParameters.outputUri().getPath();
                    return true;
                    (this.mInputDataInfos = new InputDataInfo[1])[0] = this.createVideoInputStreamInfo(profile);
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public void release() {
        if (this.mState != State.IDLE) {
            this.stop();
        }
        else {
            this.reset();
        }
    }
    
    @Override
    public void reset() {
        this.mEncoder = null;
        this.mState = State.IDLE;
        if (this.mInputDataInfos != null) {
            for (final InputDataInfo inputDataInfo : this.mInputDataInfos) {
                inputDataInfo.codec.release();
                inputDataInfo.source.release();
            }
            this.mInputDataInfos = null;
        }
    }
    
    @Override
    public void resume() {
        throw new UnsupportedOperationException("This method is not supported");
    }
    
    public void setInputDataSourceFactory(final InputDataSourceFactory mInputDataSourceFactory) {
        if (mInputDataSourceFactory == null) {
            throw new IllegalArgumentException("This method cannot accept null as InputDataSourceFactory.");
        }
        this.mInputDataSourceFactory = mInputDataSourceFactory;
    }
    
    @Override
    public void setListener(final RecordTrackListener mAudioTrackListener, final RecordTrackListener mVideoTrackListener, final OnErrorListener mOnErrorListener, final OnMaxReachedListener mOnMaxReachedListener) {
        this.mAudioTrackListener = mAudioTrackListener;
        this.mVideoTrackListener = mVideoTrackListener;
        this.mOnErrorListener = mOnErrorListener;
        this.mOnMaxReachedListener = mOnMaxReachedListener;
    }
    
    @Override
    public void setLocation(final Location mLocation) {
        this.mLocation = mLocation;
    }
    
    @Override
    public void setMaxDurationMillis(final long mMaxDurationMillis) {
        this.mMaxDurationMillis = mMaxDurationMillis;
    }
    
    @Override
    public void setMaxFileSizeBytes(final long a) {
        this.mMaxFileSizeBytes = Math.min(a, 256000000000L);
    }
    
    @Override
    public void setOrientationHint(final int mOrientationHint) {
        this.mOrientationHint = mOrientationHint;
    }
    
    @Override
    public void setOutputFilePath(final String mOutputPath) {
        this.mOutputPath = mOutputPath;
    }
    
    @Override
    public void start() throws IOException {
        if (this.mState == State.IDLE) {
            if (this.mContext != null) {
                FileDescriptor fileDescriptor = null;
                Label_0137: {
                    if (StorageUtil.getStorageTypeFromPath(this.mOutputPath, this.mContext) == Storage.StorageType.EXTERNAL_CARD) {
                        final Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(this.mContext);
                        final Uri file = StorageUtil.createFile(this.mContext, sdCardGrantedUri, StorageUtil.getPathAfterDcim(sdCardGrantedUri, this.mOutputPath));
                        try {
                            final ParcelFileDescriptor openFileDescriptor = this.mContext.getContentResolver().openFileDescriptor(file, "rw");
                            if (openFileDescriptor == null) {
                                CamLog.e("openFileDescriptor fd is null.");
                                throw new RuntimeException("openFileDescriptor fd is null.");
                            }
                            fileDescriptor = openFileDescriptor.getFileDescriptor();
                            this.mOutputPath = null;
                            break Label_0137;
                        }
                        catch (final FileNotFoundException cause) {
                            CamLog.e("openFileDescriptor failed.", cause);
                            throw new RuntimeException(cause);
                        }
                    }
                    fileDescriptor = null;
                }
                final MediaEncoderStateListener mediaEncoderStateListener = new MediaEncoderStateListener();
                try {
                    (this.mEncoder = new MediaEncoder(this.mInputDataInfos, this.mOutputPath, fileDescriptor, (MediaEncoder.StateListener)mediaEncoderStateListener)).setOrientationHint(this.mOrientationHint);
                    this.mEncoder.setMaxDuration(this.mMaxDurationMillis);
                    this.mEncoder.setMaxFileSize(this.mMaxFileSizeBytes);
                    if (this.mLocation != null) {
                        this.mEncoder.setLocation((float)this.mLocation.getLatitude(), (float)this.mLocation.getLongitude());
                    }
                    this.mInputDataInfos = null;
                    this.mWaitUntilStarted = new CountDownLatch(1);
                    this.mWaitUntilStoped = new CountDownLatch(1);
                    this.mState = State.RUNNING;
                    this.mEncoder.start();
                    try {
                        this.mWaitUntilStarted.await();
                        return;
                    }
                    catch (final InterruptedException cause2) {
                        throw new RuntimeException(cause2);
                    }
                }
                catch (final IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("start() failed : ");
                    sb.append(ex.getMessage());
                    CamLog.e(sb.toString());
                    throw ex;
                }
            }
        }
        throw new IllegalStateException();
    }
    
    @Override
    public void stop() {
        if (this.mState != State.IDLE) {
            this.mEncoder.stop();
            this.mEncoder = null;
        }
        if (this.mFpsMonitor != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("FPS_MONITOR:");
            sb.append(this.mFpsMonitor.dump());
            CamLog.d(sb.toString());
        }
        try {
            if (!this.mWaitUntilStoped.await(5000L, TimeUnit.MILLISECONDS)) {
                CamLog.e("Encoder doesn't finish correctly. Video file may be corrupt.");
            }
            this.mState = State.IDLE;
            if (!this.mResult) {
                throw new RuntimeException("recording failed.");
            }
        }
        catch (final InterruptedException cause) {
            CamLog.e("stop failed due to interruption.", cause);
            throw new RuntimeException(cause);
        }
    }
    
    @Override
    public void stopAsync() {
        throw new UnsupportedOperationException("#stopAsync() only supported by SomeMediaRecorder");
    }
    
    @Override
    public void stopAudioRecording() {
    }
    
    @Override
    public void stopOnCameraError() {
        trace("stopOnCameraError() E");
        this.stop();
        trace("stopOnCameraError() X");
    }
    
    @Override
    public void waitUntilStopCompleted() {
        throw new UnsupportedOperationException("#stopAsync() only supported by SomeMediaRecorder");
    }
    
    public interface InputDataSourceFactory
    {
        InputDataSource createAudioSource(final MediaCodec p0, final CamcorderProfile p1);
        
        VideoFrameSource createVideoSource(final MediaCodec p0, final CamcorderProfile p1);
    }
    
    private class MediaEncoderStateListener implements StateListener
    {
        private long mLastNotifyProgressMillis;
        final VariableSourceMediaRecorder this$0;
        
        public MediaEncoderStateListener(final VariableSourceMediaRecorder this$0) {
            this.this$0 = this$0;
            this.mLastNotifyProgressMillis = 0L;
        }
        
        @Override
        public void onFinished(final boolean b) {
            if (!b) {
                final File file = new File(this.this$0.mOutputPath);
                if (file.exists()) {
                    final Context context = CameraApplication.getContext();
                    if (StorageUtil.getStorageTypeFromPath(this.this$0.mOutputPath, CameraApplication.getContext()) == Storage.StorageType.EXTERNAL_CARD) {
                        final Uri searchDocumentSdCard = StorageUtil.searchDocumentSdCard(context, this.this$0.mOutputPath);
                        if (searchDocumentSdCard != null) {
                            try {
                                if (!DocumentsContract.deleteDocument(context.getContentResolver(), searchDocumentSdCard)) {
                                    CamLog.e("Unable to delete file.");
                                }
                            }
                            catch (final FileNotFoundException obj) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unable to delete file.");
                                sb.append(obj);
                                CamLog.e(sb.toString());
                            }
                        }
                    }
                    else if (!file.delete()) {
                        CamLog.e("Unable to delete file.");
                    }
                }
            }
            this.this$0.mResult = b;
            this.this$0.mWaitUntilStoped.countDown();
            this.this$0.mHandler.post((Runnable)new FinishNotificationTask());
        }
        
        @Override
        public void onMaxDurationReached() {
            if (CamLog.DEBUG) {
                CamLog.d("VariableSourceMediaRecorder", "reached max duration.");
            }
            this.this$0.mOnMaxReachedListener.onMaxDurationReached();
        }
        
        @Override
        public void onMaxFileSizeReached() {
            if (CamLog.DEBUG) {
                CamLog.d("VariableSourceMediaRecorder", "reached max size.");
            }
            this.this$0.mOnMaxReachedListener.onMaxFileSizeReached();
        }
        
        @Override
        public void onProgress(long mLastNotifyProgressMillis) {
            synchronized (this) {
                mLastNotifyProgressMillis /= 1000L;
                if (mLastNotifyProgressMillis - this.mLastNotifyProgressMillis >= 1000L) {
                    this.mLastNotifyProgressMillis = mLastNotifyProgressMillis;
                    this.this$0.mHandler.post((Runnable)new ProgressNotificationTask(this.mLastNotifyProgressMillis));
                }
            }
        }
        
        @Override
        public void onStarted() {
            this.this$0.mWaitUntilStarted.countDown();
            this.this$0.mHandler.post((Runnable)new StartNotificationTask());
        }
        
        @Override
        public void onStorageFull() {
            this.this$0.mEncoder.stop();
            this.this$0.mHandler.post((Runnable)new ErrorNotificationTask());
        }
        
        private class ErrorNotificationTask implements Runnable
        {
            final MediaEncoderStateListener this$1;
            
            private ErrorNotificationTask(final MediaEncoderStateListener this$1) {
                this.this$1 = this$1;
            }
            
            @Override
            public void run() {
                this.this$1.this$0.mOnErrorListener.onError();
            }
        }
        
        private class FinishNotificationTask implements Runnable
        {
            final MediaEncoderStateListener this$1;
            
            private FinishNotificationTask(final MediaEncoderStateListener this$1) {
                this.this$1 = this$1;
            }
            
            @Override
            public void run() {
                this.this$1.this$0.mAudioTrackListener.onCompleted();
                this.this$1.this$0.mVideoTrackListener.onCompleted();
            }
        }
        
        private class ProgressNotificationTask implements Runnable
        {
            private final long mTimeMillis;
            final MediaEncoderStateListener this$1;
            
            public ProgressNotificationTask(final MediaEncoderStateListener this$1, final long mTimeMillis) {
                this.this$1 = this$1;
                this.mTimeMillis = mTimeMillis;
            }
            
            @Override
            public void run() {
                this.this$1.this$0.mAudioTrackListener.onProgress(this.mTimeMillis);
                this.this$1.this$0.mVideoTrackListener.onProgress(this.mTimeMillis);
            }
        }
        
        private class StartNotificationTask implements Runnable
        {
            final MediaEncoderStateListener this$1;
            
            private StartNotificationTask(final MediaEncoderStateListener this$1) {
                this.this$1 = this$1;
            }
            
            @Override
            public void run() {
                this.this$1.this$0.mAudioTrackListener.onStarted();
                this.this$1.this$0.mVideoTrackListener.onStarted();
            }
        }
    }
    
    private enum State
    {
        private static final State[] $VALUES;
        
        IDLE, 
        RUNNING;
        
        static {
            $VALUES = new State[] { State.RUNNING, State.IDLE };
        }
    }
}
