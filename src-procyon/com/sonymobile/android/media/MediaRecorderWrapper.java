// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.android.media;

import android.media.CamcorderProfile;
import java.io.FileDescriptor;
import android.media.MediaRecorder$OnInfoListener;
import android.media.MediaRecorder$OnErrorListener;
import android.hardware.Camera;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import android.view.Surface;

public class MediaRecorderWrapper
{
    private static final boolean DBG = false;
    private static final String TAG = "MediaRecorderWrapper";
    private MediaRecorder mNewRecorder;
    private android.media.MediaRecorder mOldRecorder;
    private final boolean mUseNew;
    
    public MediaRecorderWrapper() {
        this(false);
    }
    
    public MediaRecorderWrapper(final boolean mUseNew) {
        this.mUseNew = mUseNew;
        if (this.mUseNew) {
            this.mNewRecorder = new MediaRecorder();
        }
        else {
            this.mOldRecorder = new android.media.MediaRecorder();
        }
    }
    
    public static final int getAudioSourceMax() {
        return android.media.MediaRecorder.getAudioSourceMax();
    }
    
    public void adjustAudioStartVolume(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.adjustAudioStartVolume(n);
        }
    }
    
    public void adjustAudioTimestamp(final long n) {
        if (this.mUseNew) {
            this.mNewRecorder.adjustAudioTimestamp(n);
        }
    }
    
    public String dump(final String s) {
        if (this.mUseNew) {
            return this.mNewRecorder.dump(s);
        }
        return null;
    }
    
    public int getMaxAmplitude() throws UnsupportedOperationException {
        if (this.mUseNew) {
            throw new UnsupportedOperationException(" getMaxAmplitude unsupported");
        }
        return this.mOldRecorder.getMaxAmplitude();
    }
    
    public Surface getSurface() {
        if (this.mUseNew) {
            return this.mNewRecorder.getSurface();
        }
        return this.mOldRecorder.getSurface();
    }
    
    public boolean isAsyncStopSupported() {
        return this.mUseNew;
    }
    
    public boolean pause() {
        if (this.mUseNew) {
            this.mNewRecorder.pause();
        }
        else {
            this.mOldRecorder.pause();
        }
        return true;
    }
    
    public void prepare() throws IOException {
        if (this.mUseNew) {
            this.mNewRecorder.prepare();
        }
        else {
            this.mOldRecorder.prepare();
        }
    }
    
    public void release() {
        if (this.mUseNew) {
            this.mNewRecorder.release();
        }
        else {
            this.mOldRecorder.release();
        }
    }
    
    public boolean requestProgressInfo(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.requestProgressInfo(n);
            return true;
        }
        try {
            final Method declaredMethod = android.media.MediaRecorder.class.getDeclaredMethod("setParameter", String.class);
            declaredMethod.setAccessible(true);
            final android.media.MediaRecorder mOldRecorder = this.mOldRecorder;
            final StringBuilder sb = new StringBuilder();
            sb.append("param-track-time-status=");
            sb.append(n * 1000);
            declaredMethod.invoke(mOldRecorder, sb.toString());
            return true;
        }
        catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            return false;
        }
    }
    
    public void reset() {
        if (this.mUseNew) {
            this.mNewRecorder.reset();
        }
        else {
            this.mOldRecorder.reset();
        }
    }
    
    public boolean resume() {
        if (this.mUseNew) {
            this.mNewRecorder.resume();
        }
        else {
            this.mOldRecorder.resume();
        }
        return true;
    }
    
    public void setAudioChannels(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioChannels(n);
        }
        else {
            this.mOldRecorder.setAudioChannels(n);
        }
    }
    
    public void setAudioEncoder(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioEncoder(n);
        }
        else {
            this.mOldRecorder.setAudioEncoder(n);
        }
    }
    
    public void setAudioEncodingBitRate(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioEncodingBitRate(n);
        }
        else {
            this.mOldRecorder.setAudioEncodingBitRate(n);
        }
    }
    
    public void setAudioSamplingRate(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioSamplingRate(n);
        }
        else {
            this.mOldRecorder.setAudioSamplingRate(n);
        }
    }
    
    public void setAudioSource(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setAudioSource(n);
        }
        else {
            this.mOldRecorder.setAudioSource(n);
        }
    }
    
    public void setCamera(final Camera camera) throws UnsupportedOperationException {
        if (this.mUseNew) {
            throw new UnsupportedOperationException("unsupported");
        }
        this.mOldRecorder.setCamera(camera);
    }
    
    public void setCaptureRate(final double n) {
        if (this.mUseNew) {
            this.mNewRecorder.setCaptureRate(n);
        }
        else {
            this.mOldRecorder.setCaptureRate(n);
        }
    }
    
    public void setDebugMode(final boolean b) {
        final boolean mUseNew = this.mUseNew;
    }
    
    public void setInputSurface(final Surface surface) {
        if (this.mUseNew) {
            this.mNewRecorder.setInputSurface(surface);
        }
        else {
            this.mOldRecorder.setInputSurface(surface);
        }
    }
    
    public void setLocation(final float n, final float n2) {
        if (this.mUseNew) {
            this.mNewRecorder.setLocation(n, n2);
        }
        else {
            this.mOldRecorder.setLocation(n, n2);
        }
    }
    
    public void setMaxDuration(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setMaxDuration(n);
        }
        else {
            this.mOldRecorder.setMaxDuration(n);
        }
    }
    
    public void setMaxFileSize(final long n) {
        if (this.mUseNew) {
            this.mNewRecorder.setMaxFileSize(n);
        }
        else {
            this.mOldRecorder.setMaxFileSize(n);
        }
    }
    
    public void setOnErrorListener(final MediaRecorder$OnErrorListener mediaRecorder$OnErrorListener) {
        if (this.mUseNew) {
            this.mNewRecorder.setOnErrorListener(mediaRecorder$OnErrorListener);
        }
        else {
            this.mOldRecorder.setOnErrorListener(mediaRecorder$OnErrorListener);
        }
    }
    
    public void setOnInfoListener(final MediaRecorder$OnInfoListener mediaRecorder$OnInfoListener) {
        if (this.mUseNew) {
            this.mNewRecorder.setOnInfoListener(mediaRecorder$OnInfoListener);
        }
        else {
            this.mOldRecorder.setOnInfoListener(mediaRecorder$OnInfoListener);
        }
    }
    
    public void setOrientationHint(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setOrientationHint(n);
        }
        else {
            this.mOldRecorder.setOrientationHint(n);
        }
    }
    
    public void setOutputFile(final FileDescriptor fileDescriptor) {
        if (this.mUseNew) {
            this.mNewRecorder.setOutputFile(fileDescriptor);
        }
        else {
            this.mOldRecorder.setOutputFile(fileDescriptor);
        }
    }
    
    public void setOutputFile(final String s) {
        if (this.mUseNew) {
            this.mNewRecorder.setOutputFile(s);
        }
        else {
            this.mOldRecorder.setOutputFile(s);
        }
    }
    
    public void setOutputFormat(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setOutputFormat(n);
        }
        else {
            this.mOldRecorder.setOutputFormat(n);
        }
    }
    
    public void setPreviewDisplay(final Surface previewDisplay) throws UnsupportedOperationException {
        if (this.mUseNew) {
            throw new UnsupportedOperationException("setPreviewDisplay unsupported");
        }
        this.mOldRecorder.setPreviewDisplay(previewDisplay);
    }
    
    public void setProfile(final CamcorderProfile camcorderProfile) {
        if (this.mUseNew) {
            this.mNewRecorder.setProfile(camcorderProfile);
        }
        else {
            this.mOldRecorder.setProfile(camcorderProfile);
        }
    }
    
    public void setVideoBitRateMode(final int videoBitRateMode) throws UnsupportedOperationException {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoBitRateMode(videoBitRateMode);
            return;
        }
        throw new UnsupportedOperationException("setVideoBitRateMode unsupported");
    }
    
    public void setVideoColorAspects(final int n, final int n2, final int n3) throws UnsupportedOperationException {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoColorAspects(n, n2, n3);
            return;
        }
        throw new UnsupportedOperationException("setVideoColorAspects unsupported");
    }
    
    public void setVideoEncoder(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoEncoder(n);
        }
        else {
            this.mOldRecorder.setVideoEncoder(n);
        }
    }
    
    public void setVideoEncodingBitRate(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoEncodingBitRate(n);
        }
        else {
            this.mOldRecorder.setVideoEncodingBitRate(n);
        }
    }
    
    public void setVideoEncodingProfileLevel(final int i, final int j) {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoEncodingProfileLevel(i, j);
            return;
        }
        try {
            final Method declaredMethod = android.media.MediaRecorder.class.getDeclaredMethod("setVideoEncodingProfileLevel", Integer.TYPE, Integer.TYPE);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(this.mOldRecorder, i, j);
        }
        catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {}
    }
    
    public void setVideoFrameRate(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoFrameRate(n);
        }
        else {
            this.mOldRecorder.setVideoFrameRate(n);
        }
    }
    
    public void setVideoSize(final int n, final int n2) {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoSize(n, n2);
        }
        else {
            this.mOldRecorder.setVideoSize(n, n2);
        }
    }
    
    public void setVideoSource(final int n) {
        if (this.mUseNew) {
            this.mNewRecorder.setVideoSource(n);
        }
        else {
            this.mOldRecorder.setVideoSource(n);
        }
    }
    
    public void start() {
        if (this.mUseNew) {
            this.mNewRecorder.start();
        }
        else {
            this.mOldRecorder.start();
        }
    }
    
    public void stop() {
        if (this.mUseNew) {
            this.mNewRecorder.stop();
        }
        else {
            this.mOldRecorder.stop();
        }
    }
    
    public void stopAsync() {
        if (this.isAsyncStopSupported()) {
            this.mNewRecorder.stopAsync();
        }
    }
    
    public void stopAudioRecording() {
        if (this.mUseNew) {
            this.mNewRecorder.stopAudioRecording();
        }
    }
    
    public void stopOnError() {
        if (this.mUseNew) {
            this.mNewRecorder.stopOnError();
        }
        else {
            this.mOldRecorder.stop();
        }
    }
    
    public void useIntelligentActive(final boolean b) {
        if (this.mUseNew) {
            this.mNewRecorder.useIntelligentActive(b);
        }
    }
    
    public void waitUntilStopCompleted() {
        if (this.isAsyncStopSupported()) {
            this.mNewRecorder.waitUntilStopCompleted();
        }
    }
}
