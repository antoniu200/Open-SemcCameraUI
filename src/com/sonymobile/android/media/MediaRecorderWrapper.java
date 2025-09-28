package com.sonymobile.android.media;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.Surface;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MediaRecorderWrapper {
    private static final boolean DBG = false;
    private static final String TAG = "MediaRecorderWrapper";
    private MediaRecorder mNewRecorder;
    private android.media.MediaRecorder mOldRecorder;
    private final boolean mUseNew;

    public MediaRecorderWrapper() {
        this(false);
    }

    public MediaRecorderWrapper(boolean z) {
        this.mUseNew = z;
        if (this.mUseNew) {
            this.mNewRecorder = new MediaRecorder();
        } else {
            this.mOldRecorder = new android.media.MediaRecorder();
        }
    }

    public void stopAudioRecording() {
        if (this.mUseNew) {
            this.mNewRecorder.stopAudioRecording();
        }
    }

    public void setCamera(Camera camera) throws UnsupportedOperationException {
        if (this.mUseNew) {
            throw new UnsupportedOperationException("unsupported");
        }
        this.mOldRecorder.setCamera(camera);
    }

    public Surface getSurface() {
        if (this.mUseNew) {
            return this.mNewRecorder.getSurface();
        }
        return this.mOldRecorder.getSurface();
    }

    public void setInputSurface(Surface surface) {
        if (this.mUseNew) {
            this.mNewRecorder.setInputSurface(surface);
        } else {
            this.mOldRecorder.setInputSurface(surface);
        }
    }

    public void setPreviewDisplay(Surface surface) throws UnsupportedOperationException {
        if (this.mUseNew) {
            throw new UnsupportedOperationException("setPreviewDisplay unsupported");
        }
        this.mOldRecorder.setPreviewDisplay(surface);
    }

    public void setAudioSource(int i) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioSource(i);
        } else {
            this.mOldRecorder.setAudioSource(i);
        }
    }

    public static final int getAudioSourceMax() {
        return android.media.MediaRecorder.getAudioSourceMax();
    }

    public void setVideoSource(int i) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoSource(i);
        } else {
            this.mOldRecorder.setVideoSource(i);
        }
    }

    public void setProfile(CamcorderProfile camcorderProfile) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setProfile(camcorderProfile);
        } else {
            this.mOldRecorder.setProfile(camcorderProfile);
        }
    }

    public void setCaptureRate(double d) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setCaptureRate(d);
        } else {
            this.mOldRecorder.setCaptureRate(d);
        }
    }

    public void setOrientationHint(int i) {
        if (this.mUseNew) {
            this.mNewRecorder.setOrientationHint(i);
        } else {
            this.mOldRecorder.setOrientationHint(i);
        }
    }

    public void setLocation(float f, float f2) {
        if (this.mUseNew) {
            this.mNewRecorder.setLocation(f, f2);
        } else {
            this.mOldRecorder.setLocation(f, f2);
        }
    }

    public void setOutputFormat(int i) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setOutputFormat(i);
        } else {
            this.mOldRecorder.setOutputFormat(i);
        }
    }

    public void setVideoSize(int i, int i2) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoSize(i, i2);
        } else {
            this.mOldRecorder.setVideoSize(i, i2);
        }
    }

    public void setVideoFrameRate(int i) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoFrameRate(i);
        } else {
            this.mOldRecorder.setVideoFrameRate(i);
        }
    }

    public void setVideoEncodingProfileLevel(int i, int i2) throws IllegalStateException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoEncodingProfileLevel(i, i2);
            return;
        }
        try {
            Method declaredMethod = android.media.MediaRecorder.class.getDeclaredMethod("setVideoEncodingProfileLevel", Integer.TYPE, Integer.TYPE);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(this.mOldRecorder, Integer.valueOf(i), Integer.valueOf(i2));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
        }
    }

    public void setVideoBitRateMode(int i) throws IllegalStateException, UnsupportedOperationException {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoBitRateMode(i);
            return;
        }
        throw new UnsupportedOperationException("setVideoBitRateMode unsupported");
    }

    public void setVideoColorAspects(int i, int i2, int i3) throws IllegalStateException, UnsupportedOperationException {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoColorAspects(i, i2, i3);
            return;
        }
        throw new UnsupportedOperationException("setVideoColorAspects unsupported");
    }

    public void setMaxDuration(int i) throws IllegalArgumentException {
        if (this.mUseNew) {
            this.mNewRecorder.setMaxDuration(i);
        } else {
            this.mOldRecorder.setMaxDuration(i);
        }
    }

    public void setMaxFileSize(long j) throws IllegalArgumentException {
        if (this.mUseNew) {
            this.mNewRecorder.setMaxFileSize(j);
        } else {
            this.mOldRecorder.setMaxFileSize(j);
        }
    }

    public void setAudioEncoder(int i) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioEncoder(i);
        } else {
            this.mOldRecorder.setAudioEncoder(i);
        }
    }

    public void setVideoEncoder(int i) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoEncoder(i);
        } else {
            this.mOldRecorder.setVideoEncoder(i);
        }
    }

    public void setAudioSamplingRate(int i) {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioSamplingRate(i);
        } else {
            this.mOldRecorder.setAudioSamplingRate(i);
        }
    }

    public void setAudioChannels(int i) {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioChannels(i);
        } else {
            this.mOldRecorder.setAudioChannels(i);
        }
    }

    public void setAudioEncodingBitRate(int i) {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioEncodingBitRate(i);
        } else {
            this.mOldRecorder.setAudioEncodingBitRate(i);
        }
    }

    public void setVideoEncodingBitRate(int i) {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoEncodingBitRate(i);
        } else {
            this.mOldRecorder.setVideoEncodingBitRate(i);
        }
    }

    public void setOutputFile(FileDescriptor fileDescriptor) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setOutputFile(fileDescriptor);
        } else {
            this.mOldRecorder.setOutputFile(fileDescriptor);
        }
    }

    public void setOutputFile(String str) throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.setOutputFile(str);
        } else {
            this.mOldRecorder.setOutputFile(str);
        }
    }

    public void prepare() throws IllegalStateException, IOException {
        if (this.mUseNew) {
            this.mNewRecorder.prepare();
        } else {
            this.mOldRecorder.prepare();
        }
    }

    public void start() throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.start();
        } else {
            this.mOldRecorder.start();
        }
    }

    public void stop() throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.stop();
        } else {
            this.mOldRecorder.stop();
        }
    }

    public void stopOnError() throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.stopOnError();
        } else {
            this.mOldRecorder.stop();
        }
    }

    public boolean isAsyncStopSupported() {
        return this.mUseNew;
    }

    public void stopAsync() throws IllegalStateException {
        if (isAsyncStopSupported()) {
            this.mNewRecorder.stopAsync();
        }
    }

    public void waitUntilStopCompleted() throws IllegalStateException {
        if (isAsyncStopSupported()) {
            this.mNewRecorder.waitUntilStopCompleted();
        }
    }

    public boolean pause() throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.pause();
            return true;
        }
        this.mOldRecorder.pause();
        return true;
    }

    public boolean resume() throws IllegalStateException {
        if (this.mUseNew) {
            this.mNewRecorder.resume();
            return true;
        }
        this.mOldRecorder.resume();
        return true;
    }

    public void reset() {
        if (this.mUseNew) {
            this.mNewRecorder.reset();
        } else {
            this.mOldRecorder.reset();
        }
    }

    public void release() {
        if (this.mUseNew) {
            this.mNewRecorder.release();
        } else {
            this.mOldRecorder.release();
        }
    }

    public int getMaxAmplitude() throws UnsupportedOperationException {
        if (this.mUseNew) {
            throw new UnsupportedOperationException(" getMaxAmplitude unsupported");
        }
        return this.mOldRecorder.getMaxAmplitude();
    }

    public void setOnErrorListener(MediaRecorder.OnErrorListener onErrorListener) {
        if (this.mUseNew) {
            this.mNewRecorder.setOnErrorListener(onErrorListener);
        } else {
            this.mOldRecorder.setOnErrorListener(onErrorListener);
        }
    }

    public void setOnInfoListener(MediaRecorder.OnInfoListener onInfoListener) {
        if (this.mUseNew) {
            this.mNewRecorder.setOnInfoListener(onInfoListener);
        } else {
            this.mOldRecorder.setOnInfoListener(onInfoListener);
        }
    }

    public boolean requestProgressInfo(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (this.mUseNew) {
            this.mNewRecorder.requestProgressInfo(i);
            return true;
        }
        try {
            Method declaredMethod = android.media.MediaRecorder.class.getDeclaredMethod("setParameter", String.class);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(this.mOldRecorder, "param-track-time-status=" + (i * 1000));
            return true;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
            return false;
        }
    }

    public void useIntelligentActive(boolean z) {
        if (this.mUseNew) {
            this.mNewRecorder.useIntelligentActive(z);
        }
    }

    public void adjustAudioTimestamp(long j) {
        if (this.mUseNew) {
            this.mNewRecorder.adjustAudioTimestamp(j);
        }
    }

    public void adjustAudioStartVolume(int i) {
        if (this.mUseNew) {
            this.mNewRecorder.adjustAudioStartVolume(i);
        }
    }

    public void setDebugMode(boolean z) {
        boolean z2 = this.mUseNew;
    }

    public String dump(String str) {
        if (this.mUseNew) {
            return this.mNewRecorder.dump(str);
        }
        return null;
    }
}
