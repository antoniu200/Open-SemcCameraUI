package com.sonymobile.android.media;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import com.sonymobile.android.media.internal.SomcMediaRecorder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MediaRecorder {
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_RECORDER_ERROR_UNKNOWN = 1;
    public static final int MEDIA_RECORDER_INFO_MAX_DURATION_REACHED = 800;
    public static final int MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED = 801;
    public static final int MEDIA_RECORDER_INFO_UNKNOWN = 1;
    public static final int MEDIA_RECORDER_TRACK_INFO_COMPLETION_STATUS = 1000;
    public static final int MEDIA_RECORDER_TRACK_INFO_DATA_KBYTES = 1009;
    public static final int MEDIA_RECORDER_TRACK_INFO_DURATION_MS = 1003;
    public static final int MEDIA_RECORDER_TRACK_INFO_ENCODED_FRAMES = 1005;
    public static final int MEDIA_RECORDER_TRACK_INFO_INITIAL_DELAY_MS = 1007;
    public static final int MEDIA_RECORDER_TRACK_INFO_LIST_END = 2000;
    public static final int MEDIA_RECORDER_TRACK_INFO_LIST_START = 1000;
    public static final int MEDIA_RECORDER_TRACK_INFO_MAX_CHUNK_DUR_MS = 1004;
    public static final int MEDIA_RECORDER_TRACK_INFO_PROGRESS_IN_TIME = 1001;
    public static final int MEDIA_RECORDER_TRACK_INFO_START_OFFSET_MS = 1008;
    public static final int MEDIA_RECORDER_TRACK_INFO_TYPE = 1002;
    public static final int MEDIA_RECORDER_TRACK_INTER_CHUNK_TIME_MS = 1006;
    private static final String TAG = "MediaRecorder";
    private boolean mIsAvailable;
    private MediaRecorder.OnErrorListener mOnErrorListener;
    private MediaRecorder.OnInfoListener mOnInfoListener;
    private SomcMediaRecorder mSomcMediaRecorder;

    public static final class AudioSource {
        public static final int AUDIO_SOURCE_INVALID = -1;
        public static final int CAMCORDER = 5;
        public static final int DEFAULT = 0;
        public static final int HOTWORD = 1999;
        public static final int MIC = 1;
        public static final int RADIO_TUNER = 1998;
        public static final int REMOTE_SUBMIX = 8;
        public static final int VOICE_CALL = 4;
        public static final int VOICE_COMMUNICATION = 7;
        public static final int VOICE_DOWNLINK = 3;
        public static final int VOICE_RECOGNITION = 6;
        public static final int VOICE_UPLINK = 2;

        private AudioSource() {
        }
    }

    public static final class VideoSource {
        public static final int CAMERA = 1;
        public static final int DEFAULT = 0;
        public static final int SURFACE = 2;

        private VideoSource() {
        }
    }

    public static final class OutputFormat {
        public static final int AAC_ADIF = 5;
        public static final int AAC_ADTS = 6;
        public static final int AMR_NB = 3;
        public static final int AMR_WB = 3;
        public static final int DEFAULT = 0;
        public static final int MPEG_4 = 2;
        public static final int OUTPUT_FORMAT_MPEG2TS = 8;
        public static final int OUTPUT_FORMAT_RTP_AVP = 7;
        public static final int RAW_AMR = 3;
        public static final int THREE_GPP = 1;
        public static final int WEBM = 9;

        private OutputFormat() {
        }
    }

    public static final class AudioEncoder {
        public static final int AAC = 3;
        public static final int AAC_ELD = 5;
        public static final int AMR_NB = 1;
        public static final int AMR_WB = 2;
        public static final int DEFAULT = 0;
        public static final int HE_AAC = 4;
        public static final int VORBIS = 6;

        private AudioEncoder() {
        }
    }

    public static final class VideoEncoder {
        public static final int DEFAULT = 0;
        public static final int H263 = 1;
        public static final int H264 = 2;
        public static final int HEVC = 5;
        public static final int MPEG_4_SP = 3;
        public static final int VP8 = 4;

        private VideoEncoder() {
        }
    }

    public MediaRecorder() {
        CallbackHandler callbackHandler;
        if (Looper.myLooper() != null) {
            callbackHandler = new CallbackHandler(new WeakReference(this), Looper.myLooper());
        } else if (Looper.getMainLooper() != null) {
            callbackHandler = new CallbackHandler(new WeakReference(this), Looper.getMainLooper());
        } else {
            throw new IllegalArgumentException("MediaRecorder must be created on thread with Looper running");
        }
        this.mSomcMediaRecorder = new SomcMediaRecorder(callbackHandler);
    }

    public void stopAudioRecording() {
        this.mSomcMediaRecorder.stopAudioRecording();
    }

    public void setCamera(Camera camera) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Camera source not supported");
    }

    public Surface getSurface() {
        return this.mSomcMediaRecorder.getSurface();
    }

    public void setInputSurface(@NonNull Surface surface) {
        this.mSomcMediaRecorder.setInputSurface(surface);
    }

    public void setPreviewDisplay(Surface surface) {
        throw new UnsupportedOperationException("setPreviewDisplay not supported");
    }

    public void setAudioSource(int i) throws IllegalStateException {
        this.mSomcMediaRecorder.setAudioSource(i);
    }

    public static final int getAudioSourceMax() {
        return SomcMediaRecorder.getAudioSourceMax();
    }

    public void setVideoSource(int i) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoSource(i);
    }

    public void setProfile(CamcorderProfile camcorderProfile) throws IllegalStateException {
        this.mSomcMediaRecorder.setProfile(camcorderProfile);
    }

    public void setCaptureRate(double d) throws IllegalStateException {
        this.mSomcMediaRecorder.setCaptureRate(d);
    }

    public void setOrientationHint(int i) {
        this.mSomcMediaRecorder.setOrientationHint(i);
    }

    public void setLocation(float f, float f2) {
        this.mSomcMediaRecorder.setLocation(f, f2);
    }

    public void setOutputFormat(int i) throws IllegalStateException {
        this.mSomcMediaRecorder.setOutputFormat(i);
    }

    public void setVideoSize(int i, int i2) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoSize(i, i2);
    }

    public void setVideoFrameRate(int i) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoFrameRate(i);
    }

    public void setVideoEncodingProfileLevel(int i, int i2) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoEncodingProfileLevel(i, i2);
    }

    public void setVideoBitRateMode(int i) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoBitRateMode(i);
    }

    public void setVideoColorAspects(int i, int i2, int i3) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoColorAspects(i, i2, i3);
    }

    public void setMaxDuration(int i) throws IllegalArgumentException {
        this.mSomcMediaRecorder.setMaxDuration(i);
    }

    public void setMaxFileSize(long j) throws IllegalArgumentException {
        this.mSomcMediaRecorder.setMaxFileSize(j);
    }

    public void setAudioEncoder(int i) throws IllegalStateException {
        this.mSomcMediaRecorder.setAudioEncoder(i);
    }

    public void setVideoEncoder(int i) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoEncoder(i);
    }

    public void setAudioSamplingRate(int i) {
        this.mSomcMediaRecorder.setAudioSamplingRate(i);
    }

    public void setAudioChannels(int i) {
        this.mSomcMediaRecorder.setAudioChannels(i);
    }

    public void setAudioEncodingBitRate(int i) {
        this.mSomcMediaRecorder.setAudioEncodingBitRate(i);
    }

    public void setVideoEncodingBitRate(int i) {
        this.mSomcMediaRecorder.setVideoEncodingBitRate(i);
    }

    public void setAuxiliaryOutputFile(FileDescriptor fileDescriptor) {
        throw new UnsupportedOperationException("setAuxiliaryOutputFile(FileDescriptor) unsupported");
    }

    public void setAuxiliaryOutputFile(String str) {
        throw new UnsupportedOperationException("setAuxiliaryOutputFile unsupported");
    }

    public void setOutputFile(FileDescriptor fileDescriptor) throws IllegalStateException {
        this.mSomcMediaRecorder.setOutputFile(fileDescriptor);
    }

    public void setOutputFile(String str) throws IllegalStateException {
        this.mSomcMediaRecorder.setOutputFile(str);
    }

    public void prepare() throws IllegalStateException, IOException {
        this.mSomcMediaRecorder.prepare();
        this.mIsAvailable = true;
    }

    public void start() throws IllegalStateException {
        this.mSomcMediaRecorder.start();
    }

    public void stop() throws IllegalStateException {
        try {
            this.mSomcMediaRecorder.stop();
        } finally {
            this.mIsAvailable = false;
        }
    }

    public void stopOnError() {
        try {
            this.mSomcMediaRecorder.stopOnCameraError();
        } finally {
            this.mIsAvailable = false;
        }
    }

    public void stopAsync() throws IllegalStateException {
        this.mSomcMediaRecorder.stopAsync();
    }

    public void waitUntilStopCompleted() throws IllegalStateException {
        try {
            this.mSomcMediaRecorder.waitUntilStopCompleted();
        } finally {
            this.mIsAvailable = false;
        }
    }

    public void pause() throws IllegalStateException {
        this.mSomcMediaRecorder.pause();
    }

    public void resume() throws IllegalStateException {
        this.mSomcMediaRecorder.resume();
    }

    public void reset() {
        this.mIsAvailable = false;
        this.mSomcMediaRecorder.reset();
    }

    public void setOnErrorListener(MediaRecorder.OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnInfoListener(MediaRecorder.OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }

    public void release() {
        this.mSomcMediaRecorder.release();
    }

    public int getMaxAmplitude() throws IllegalStateException {
        throw new UnsupportedOperationException(" getMaxAmplitude unsupported");
    }

    public void requestProgressInfo(int i) {
        this.mSomcMediaRecorder.requestProgressInfo(i);
    }

    public void useIntelligentActive(boolean z) {
        this.mSomcMediaRecorder.useIntelligentActive(z);
    }

    public void adjustAudioTimestamp(long j) {
        this.mSomcMediaRecorder.adjustAudioTimestamp(j);
    }

    public void adjustAudioStartVolume(int i) {
        this.mSomcMediaRecorder.adjustAudioStartVolume(i);
    }

    public String dump(String str) {
        return this.mSomcMediaRecorder.dump(str);
    }

    private class CallbackHandler extends Handler {
        private final WeakReference<MediaRecorder> mRecorder;

        public CallbackHandler(WeakReference<MediaRecorder> weakReference, Looper looper) {
            super(looper);
            this.mRecorder = weakReference;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.arg1 & SomcMediaRecorder.MEDIA_RECORDER_INFO_MASK;
            switch (message.what) {
                case 1:
                    if (i != 4) {
                        switch (i) {
                            case 1000:
                                if (MediaRecorder.this.mOnInfoListener != null) {
                                    MediaRecorder.this.mOnInfoListener.onInfo(null, message.arg1, message.arg2);
                                    break;
                                }
                                break;
                            case 1001:
                                if (MediaRecorder.this.mOnInfoListener != null) {
                                    MediaRecorder.this.mOnInfoListener.onInfo(null, message.arg1, message.arg2);
                                    break;
                                }
                                break;
                        }
                    } else if (MediaRecorder.this.mOnErrorListener != null) {
                        if (MediaRecorder.this.mIsAvailable) {
                            MediaRecorder.this.mOnErrorListener.onError(null, 1, 0);
                            break;
                        } else {
                            Log.w(MediaRecorder.TAG, "Message is rejected because recorder is already stopped. what:" + message.what + " message:" + i);
                            break;
                        }
                    }
                    break;
                case 2:
                    if (MediaRecorder.this.mOnInfoListener != null) {
                        MediaRecorder.this.mOnInfoListener.onInfo(null, MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED, 0);
                        break;
                    }
                    break;
                case 3:
                    if (MediaRecorder.this.mOnInfoListener != null) {
                        MediaRecorder.this.mOnInfoListener.onInfo(null, MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED, 0);
                        break;
                    }
                    break;
                case 4:
                    if (MediaRecorder.this.mOnErrorListener != null) {
                        if (MediaRecorder.this.mIsAvailable) {
                            MediaRecorder.this.mOnErrorListener.onError(null, 1, 0);
                            break;
                        } else {
                            Log.w(MediaRecorder.TAG, "Message is rejected because recorder is already stopped. what:" + message.what + " message:" + i);
                            break;
                        }
                    }
                    break;
            }
        }
    }

    protected void finalize() {
        if (this.mSomcMediaRecorder != null) {
            try {
                this.mSomcMediaRecorder.reset();
            } catch (IllegalStateException unused) {
            }
            try {
                this.mSomcMediaRecorder.release();
            } catch (IllegalStateException unused2) {
            }
        }
    }
}
