package com.sonyericsson.android.camera.recorder.defaultrecorder;

import android.content.Context;
import android.location.Location;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.Surface;
import com.sonyericsson.android.camera.recorder.RecorderInterface;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonymobile.android.media.MediaRecorderWrapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DefaultRecorder implements RecorderInterface {
    private static final int DELAY_AUDIO_DURATION_IN_MILLIS = 100;
    private static final int MUTE_START_RECORD_SOUND_DURATION_IN_MILLIS = 900;
    private static final int REQUEST_PROGRESS_INTERVAL_IN_MILLIS = 1000;
    private static final String TAG = "DefaultRecorder";
    private static final boolean TRACE = false;
    private RecorderInterface.RecordTrackListener mAudioTrackListener;
    private ParcelFileDescriptor mDescriptor = null;
    private boolean mIsMicrophoneEnabled;
    private long mLastProgressMillis;
    private RecorderInterface.OnErrorListener mOnErrorListener;
    private RecorderInterface.OnMaxReachedListener mOnMaxReachedListener;
    private final MediaRecorderWrapper mRecorder;
    private final int mVideoSource;
    private RecorderInterface.RecordTrackListener mVideoTrackListener;

    private static void trace(String str) {
        CamLog.d(str);
    }

    public DefaultRecorder(int i, boolean z) {
        this.mVideoSource = i;
        this.mRecorder = new MediaRecorderWrapper(z);
        this.mRecorder.useIntelligentActive(z);
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
        if (this.mVideoSource != 2) {
            throw new UnsupportedOperationException("This method is not supported with Surface source");
        }
        return this.mRecorder.getSurface();
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public boolean prepare(Context context, RecorderParameters recorderParameters) {
        this.mRecorder.setOnErrorListener(new OnErrorListener(this.mOnErrorListener));
        this.mRecorder.setOnInfoListener(new OnInfoListener(this.mAudioTrackListener, this.mVideoTrackListener, this.mOnMaxReachedListener));
        if (!setupParameters(context, recorderParameters)) {
            release();
            return false;
        }
        if (!prepareReceiveRecordingInfo()) {
            return false;
        }
        try {
            this.mRecorder.prepare();
            this.mLastProgressMillis = 0L;
            return true;
        } catch (IOException | IllegalStateException unused) {
            release();
            return false;
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void start() throws IllegalStateException {
        this.mRecorder.start();
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void stopAudioRecording() {
        this.mRecorder.stopAudioRecording();
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void stop() throws IllegalStateException, IOException {
        this.mRecorder.stop();
        if (this.mDescriptor != null) {
            try {
                this.mDescriptor.close();
            } catch (IOException e) {
                CamLog.w(e.getMessage());
            }
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void stopOnCameraError() throws IllegalStateException {
        trace("stopOnCameraError() E");
        this.mRecorder.stopOnError();
        trace("stopOnCameraError() X");
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public boolean isAsyncStopSupported() {
        return this.mRecorder.isAsyncStopSupported();
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void stopAsync() throws IllegalStateException {
        this.mRecorder.stopAsync();
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void waitUntilStopCompleted() throws IllegalStateException {
        this.mRecorder.waitUntilStopCompleted();
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void reset() {
        this.mRecorder.reset();
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void release() {
        this.mRecorder.reset();
        this.mRecorder.release();
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void resume() throws IllegalStateException {
        this.mRecorder.resume();
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void pause() throws IllegalStateException {
        this.mRecorder.pause();
    }

    private boolean prepareReceiveRecordingInfo() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        try {
            this.mRecorder.requestProgressInfo(1000);
            return true;
        } catch (UnsupportedOperationException unused) {
            return false;
        }
    }

    private boolean setupParameters(Context context, RecorderParameters recorderParameters) throws IllegalStateException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (recorderParameters.isMicrophoneEnabled()) {
            this.mRecorder.setAudioSource(5);
            this.mRecorder.setVideoSource(this.mVideoSource);
            this.mRecorder.setProfile(recorderParameters.profile());
            this.mIsMicrophoneEnabled = true;
        } else {
            this.mRecorder.setVideoSource(this.mVideoSource);
            this.mRecorder.setOutputFormat(recorderParameters.profile().fileFormat);
            this.mRecorder.setVideoFrameRate(recorderParameters.profile().videoFrameRate);
            this.mRecorder.setVideoSize(recorderParameters.profile().videoFrameWidth, recorderParameters.profile().videoFrameHeight);
            this.mRecorder.setVideoEncodingBitRate(recorderParameters.profile().videoBitRate);
            this.mRecorder.setVideoEncoder(recorderParameters.profile().videoCodec);
            this.mIsMicrophoneEnabled = false;
        }
        setVideoEncodingProfileLevel(recorderParameters);
        if (recorderParameters.hasMaxDuration()) {
            try {
                this.mRecorder.setMaxDuration(recorderParameters.maxDuration());
            } catch (RuntimeException unused) {
            }
        }
        if (recorderParameters.hasMaxFileSize()) {
            try {
                this.mRecorder.setMaxFileSize(recorderParameters.maxFileSize());
            } catch (RuntimeException unused2) {
            }
        }
        if (recorderParameters.hasLocation()) {
            this.mRecorder.setLocation((float) recorderParameters.location().getLatitude(), (float) recorderParameters.location().getLongitude());
        }
        if (recorderParameters.hasOrientationHint()) {
            this.mRecorder.setOrientationHint(recorderParameters.orientationHint());
        }
        setupOutput(context, recorderParameters.outputUri());
        adjustAudioSettings();
        return true;
    }

    private void setVideoEncodingProfileLevel(RecorderParameters recorderParameters) throws IllegalStateException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (recorderParameters == null || recorderParameters.profile() == null) {
            return;
        }
        int i = recorderParameters.profile().videoFrameWidth;
        int i2 = recorderParameters.profile().videoFrameHeight;
        if (recorderParameters.isHdr()) {
            this.mRecorder.setVideoEncodingProfileLevel(PlatformCapability.getVideoHdrRecordingProfile(), 1);
            return;
        }
        if (i >= 3840 && i2 >= 2160) {
            if (recorderParameters.profile().videoCodec == 2) {
                this.mRecorder.setVideoEncodingProfileLevel(8, 1);
                return;
            } else {
                this.mRecorder.setVideoEncodingProfileLevel(1, 1);
                return;
            }
        }
        if (i >= 640 && i2 >= 480) {
            this.mRecorder.setVideoEncodingProfileLevel(8, 1);
        } else {
            this.mRecorder.setVideoEncodingProfileLevel(1, 1);
        }
    }

    private boolean setupOutput(Context context, Uri uri) throws IllegalStateException {
        if (uri.getScheme().equalsIgnoreCase("content")) {
            try {
                this.mDescriptor = context.getContentResolver().openFileDescriptor(uri, "rw");
                if (this.mDescriptor == null) {
                    CamLog.e("openFileDescriptor fd is null.");
                    return false;
                }
                try {
                    this.mRecorder.setOutputFile(this.mDescriptor.getFileDescriptor());
                    return true;
                } catch (UnsupportedOperationException e) {
                    CamLog.e("setOutputFile() failed.", e);
                    return false;
                }
            } catch (FileNotFoundException e2) {
                CamLog.e("openFileDescriptor failed.", e2);
                return false;
            }
        }
        if (!uri.getScheme().equalsIgnoreCase("file")) {
            return false;
        }
        if (StorageUtil.getStorageTypeFromUri(uri, context) != Storage.StorageType.EXTERNAL_CARD) {
            this.mRecorder.setOutputFile(uri.getPath());
            return true;
        }
        Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(context);
        try {
            this.mDescriptor = context.getContentResolver().openFileDescriptor(StorageUtil.createFile(context, sdCardGrantedUri, StorageUtil.getPathAfterDcim(sdCardGrantedUri, uri.getPath())), "rw");
            if (this.mDescriptor == null) {
                CamLog.e("openFileDescriptor fd is null.");
                return false;
            }
            this.mRecorder.setOutputFile(this.mDescriptor.getFileDescriptor());
            return true;
        } catch (FileNotFoundException e3) {
            CamLog.e("openFileDescriptor failed.", e3);
            return false;
        }
    }

    private void adjustAudioSettings() {
        this.mRecorder.adjustAudioStartVolume(MUTE_START_RECORD_SOUND_DURATION_IN_MILLIS);
        this.mRecorder.adjustAudioTimestamp(100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getNameForErrorCode(int i) {
        if (i == 1) {
            return "MEDIA_RECORDER_ERROR_UNKNOWN";
        }
        if (i == 100) {
            return "MEDIA_ERROR_SERVER_DIED";
        }
        return "unknown:" + i;
    }

    private static class OnErrorListener implements MediaRecorder.OnErrorListener {
        private final RecorderInterface.OnErrorListener mListener;

        private OnErrorListener(RecorderInterface.OnErrorListener onErrorListener) {
            this.mListener = onErrorListener;
        }

        private void onError(com.sonymobile.android.media.MediaRecorder mediaRecorder, int i, int i2) {
            CamLog.e("onError() E what:" + DefaultRecorder.getNameForErrorCode(i) + " extra:" + i2);
            this.mListener.onError();
            CamLog.e("onError() X");
        }

        @Override // android.media.MediaRecorder.OnErrorListener
        public void onError(MediaRecorder mediaRecorder, int i, int i2) {
            onError((com.sonymobile.android.media.MediaRecorder) null, i, i2);
        }
    }

    private class OnInfoListener implements MediaRecorder.OnInfoListener {
        private static final int MEDIA_RECORDER_INFO_KIND_MASK = 15;
        private static final int MEDIA_RECORDER_INFO_KIND_SHIFT = 28;
        private static final int MEDIA_RECORDER_INFO_KIND_VIDEO = 1;
        private static final int MEDIA_RECORDER_INFO_KIND_VIDEO_AUDIO = 2;
        private static final int MEDIA_RECORDER_INFO_MASK = 268435455;
        private final RecorderInterface.RecordTrackListener mAudioTrackListener;
        private boolean mIsAudioTrackStarted;
        private boolean mIsVideoTrackStarted;
        private final RecorderInterface.OnMaxReachedListener mOnMaxReachedListener;
        private final RecorderInterface.RecordTrackListener mVideoTrackListener;

        private OnInfoListener(RecorderInterface.RecordTrackListener recordTrackListener, RecorderInterface.RecordTrackListener recordTrackListener2, RecorderInterface.OnMaxReachedListener onMaxReachedListener) {
            this.mAudioTrackListener = recordTrackListener;
            this.mVideoTrackListener = recordTrackListener2;
            this.mOnMaxReachedListener = onMaxReachedListener;
            this.mIsAudioTrackStarted = false;
            this.mIsVideoTrackStarted = false;
        }

        private void onInfo(com.sonymobile.android.media.MediaRecorder mediaRecorder, int i, int i2) {
            int i3 = 268435455 & i;
            int i4 = (i >> 28) & 15;
            switch (i3) {
                case com.sonymobile.android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED /* 800 */:
                    this.mOnMaxReachedListener.onMaxDurationReached();
                    break;
                case com.sonymobile.android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED /* 801 */:
                    this.mOnMaxReachedListener.onMaxFileSizeReached();
                    break;
                case 1000:
                    onCompleted(i4);
                    break;
                case 1001:
                    onProgress(i2, i4);
                    break;
            }
        }

        @Override // android.media.MediaRecorder.OnInfoListener
        public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
            onInfo((com.sonymobile.android.media.MediaRecorder) null, i, i2);
        }

        private void onCompleted(int i) {
            if (!DefaultRecorder.this.mIsMicrophoneEnabled || i != 2) {
                if (DefaultRecorder.this.mIsMicrophoneEnabled || i != 1) {
                    return;
                }
                this.mVideoTrackListener.onCompleted();
                return;
            }
            this.mAudioTrackListener.onCompleted();
        }

        private void onProgress(int i, int i2) {
            if (!DefaultRecorder.this.mIsMicrophoneEnabled) {
                if (i2 == 1) {
                    if (!this.mIsVideoTrackStarted) {
                        this.mVideoTrackListener.onStarted();
                        this.mIsVideoTrackStarted = true;
                    }
                    DefaultRecorder.this.mLastProgressMillis += i;
                    this.mVideoTrackListener.onProgress(DefaultRecorder.this.mLastProgressMillis);
                    return;
                }
                return;
            }
            if (i2 == 2) {
                if (!this.mIsAudioTrackStarted) {
                    this.mAudioTrackListener.onStarted();
                    this.mIsAudioTrackStarted = true;
                }
                DefaultRecorder.this.mLastProgressMillis += i;
                this.mAudioTrackListener.onProgress(DefaultRecorder.this.mLastProgressMillis);
                return;
            }
            if (i2 != 1 || this.mIsVideoTrackStarted) {
                return;
            }
            this.mVideoTrackListener.onStarted();
            this.mIsVideoTrackStarted = true;
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setLocation(Location location) {
        throw new UnsupportedOperationException("setLocation() is not supported.");
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setMaxDurationMillis(long j) {
        throw new UnsupportedOperationException("setMaxDurationMillis() is not supported.");
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setMaxFileSizeBytes(long j) {
        throw new UnsupportedOperationException("setMaxFileSizeBytes() is not supported.");
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setOutputFilePath(String str) {
        throw new UnsupportedOperationException("setOutputFilePath() is not supported.");
    }

    @Override // com.sonyericsson.android.camera.recorder.RecorderInterface
    public void setOrientationHint(int i) {
        throw new UnsupportedOperationException("setOrientationHint() is not supported.");
    }
}
