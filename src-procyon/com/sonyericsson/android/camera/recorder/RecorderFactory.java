// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder;

import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.recorder.superslowrecorder.OnSuperSlowRecordingFinishedListener;
import com.sonyericsson.android.camera.recorder.superslowrecorder.SuperSlowShotRecorderController;
import com.sonyericsson.android.camera.recorder.superslowrecorder.SuperSlowRecorderController;
import com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController;
import com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorder;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import android.os.Handler;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import com.sonyericsson.android.camera.device.CameraActionSound;
import com.sonyericsson.android.camera.recorder.utility.Accessor;
import android.content.Context;

public class RecorderFactory
{
    private static long MIN_VIDEO_DURATION_MILLIS = 3000L;
    
    public static RecorderController create(final Context context, final Accessor<CameraActionSound> accessor, final Accessor<BypassCamera> accessor2, final Handler handler, final Handler handler2, final Parameters parameters, final int n, final int n2) {
        switch (RecorderFactory$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[parameters.mSlowMotion.ordinal()]) {
            default: {
                return createDefault(context, accessor, accessor2, parameters.mListener, handler, parameters.mProgressNotificationIntervalMillis, handler2, VideoStabilizer.INTELLIGENT_ACTIVE.getValue().equals(parameters.mVideoStabilizer), parameters.mIsShutterSoundOn);
            }
            case 2: {
                return createSuperSlowShot(context, accessor, accessor2, handler, handler2, parameters, n, n2);
            }
            case 1: {
                return createSuperSlow(context, accessor, accessor2, handler, handler2, parameters, n, n2);
            }
        }
    }
    
    private static RecorderController createDefault(final Context context, final Accessor<CameraActionSound> accessor, final Accessor<BypassCamera> accessor2, final RecorderController.RecorderListener recorderListener, final Handler handler, final int i, final Handler handler2, final boolean b, final boolean b2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Create recorder : DefaultRecorderController progress-interval:");
        sb.append(i);
        sb.append(" intelligent-active:");
        sb.append(b);
        sb.append(" shutter-sound:");
        sb.append(b2);
        CamLog.d(sb.toString());
        return new DefaultRecorderController(context, accessor, accessor2, new DefaultRecorder(2, b), recorderListener, RecorderFactory.MIN_VIDEO_DURATION_MILLIS, handler, i, handler2, true, true, true, b2, b);
    }
    
    private static SuperSlowRecorderController createSuperSlow(final Context context, final Accessor<CameraActionSound> accessor, final Accessor<BypassCamera> accessor2, final Handler handler, final Handler handler2, final Parameters parameters, final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Create recorder : SuperSlowRecorderController progress-interval:");
        sb.append(parameters.mProgressNotificationIntervalMillis);
        sb.append(" shutter-sound:");
        sb.append(parameters.mIsShutterSoundOn);
        CamLog.d(sb.toString());
        return new SuperSlowRecorderController(context, accessor, accessor2, parameters.mListener, parameters.mOnSuperSlowRecordingFinishedListener, handler, parameters.mProgressNotificationIntervalMillis, handler2, parameters.mIsShutterSoundOn, n, n2);
    }
    
    private static RecorderController createSuperSlowShot(final Context context, final Accessor<CameraActionSound> accessor, final Accessor<BypassCamera> accessor2, final Handler handler, final Handler handler2, final Parameters parameters, final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Create recorder : SuperSlowShotRecorderController progress-interval:");
        sb.append(parameters.mProgressNotificationIntervalMillis);
        sb.append(" shutter-sound:");
        sb.append(parameters.mIsShutterSoundOn);
        CamLog.d(sb.toString());
        return new SuperSlowShotRecorderController(context, accessor, accessor2, parameters.mListener, parameters.mOnSuperSlowRecordingFinishedListener, handler, parameters.mProgressNotificationIntervalMillis, handler2, parameters.mIsShutterSoundOn, n, n2);
    }
    
    public static class Parameters
    {
        private final boolean mIsShutterSoundOn;
        private final RecorderController.RecorderListener mListener;
        private final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener;
        private final int mProgressNotificationIntervalMillis;
        private final SlowMotion mSlowMotion;
        private final String mVideoStabilizer;
        
        public Parameters(final RecorderController.RecorderListener mListener, final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener, final int mProgressNotificationIntervalMillis, final boolean mIsShutterSoundOn, final String mVideoStabilizer, final SlowMotion mSlowMotion) {
            this.mListener = mListener;
            this.mOnSuperSlowRecordingFinishedListener = mOnSuperSlowRecordingFinishedListener;
            this.mProgressNotificationIntervalMillis = mProgressNotificationIntervalMillis;
            this.mIsShutterSoundOn = mIsShutterSoundOn;
            this.mVideoStabilizer = mVideoStabilizer;
            this.mSlowMotion = mSlowMotion;
        }
    }
}
