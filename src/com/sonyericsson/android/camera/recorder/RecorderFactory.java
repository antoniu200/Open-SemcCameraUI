package com.sonyericsson.android.camera.recorder;

import android.content.Context;
import android.os.Handler;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.device.CameraActionSound;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorder;
import com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController;
import com.sonyericsson.android.camera.recorder.superslowrecorder.OnSuperSlowRecordingFinishedListener;
import com.sonyericsson.android.camera.recorder.superslowrecorder.SuperSlowRecorderController;
import com.sonyericsson.android.camera.recorder.superslowrecorder.SuperSlowShotRecorderController;
import com.sonyericsson.android.camera.recorder.utility.Accessor;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RecorderFactory {
    private static long MIN_VIDEO_DURATION_MILLIS = 3000;

    public static class Parameters {
        private final boolean mIsShutterSoundOn;
        private final RecorderController.RecorderListener mListener;
        private final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener;
        private final int mProgressNotificationIntervalMillis;
        private final SlowMotion mSlowMotion;
        private final String mVideoStabilizer;

        public Parameters(RecorderController.RecorderListener recorderListener, OnSuperSlowRecordingFinishedListener onSuperSlowRecordingFinishedListener, int i, boolean z, String str, SlowMotion slowMotion) {
            this.mListener = recorderListener;
            this.mOnSuperSlowRecordingFinishedListener = onSuperSlowRecordingFinishedListener;
            this.mProgressNotificationIntervalMillis = i;
            this.mIsShutterSoundOn = z;
            this.mVideoStabilizer = str;
            this.mSlowMotion = slowMotion;
        }
    }

    public static RecorderController create(Context context, Accessor<CameraActionSound> accessor, Accessor<BypassCamera> accessor2, Handler handler, Handler handler2, Parameters parameters, int i, int i2) {
        switch (parameters.mSlowMotion) {
            case SUPER_SLOW_MOTION:
                return createSuperSlow(context, accessor, accessor2, handler, handler2, parameters, i, i2);
            case SUPER_SLOW_SHOT:
                return createSuperSlowShot(context, accessor, accessor2, handler, handler2, parameters, i, i2);
            default:
                return createDefault(context, accessor, accessor2, parameters.mListener, handler, parameters.mProgressNotificationIntervalMillis, handler2, VideoStabilizer.INTELLIGENT_ACTIVE.getValue().equals(parameters.mVideoStabilizer), parameters.mIsShutterSoundOn);
        }
    }

    private static SuperSlowRecorderController createSuperSlow(Context context, Accessor<CameraActionSound> accessor, Accessor<BypassCamera> accessor2, Handler handler, Handler handler2, Parameters parameters, int i, int i2) {
        CamLog.d("Create recorder : SuperSlowRecorderController progress-interval:" + parameters.mProgressNotificationIntervalMillis + " shutter-sound:" + parameters.mIsShutterSoundOn);
        return new SuperSlowRecorderController(context, accessor, accessor2, parameters.mListener, parameters.mOnSuperSlowRecordingFinishedListener, handler, parameters.mProgressNotificationIntervalMillis, handler2, parameters.mIsShutterSoundOn, i, i2);
    }

    private static RecorderController createSuperSlowShot(Context context, Accessor<CameraActionSound> accessor, Accessor<BypassCamera> accessor2, Handler handler, Handler handler2, Parameters parameters, int i, int i2) {
        CamLog.d("Create recorder : SuperSlowShotRecorderController progress-interval:" + parameters.mProgressNotificationIntervalMillis + " shutter-sound:" + parameters.mIsShutterSoundOn);
        return new SuperSlowShotRecorderController(context, accessor, accessor2, parameters.mListener, parameters.mOnSuperSlowRecordingFinishedListener, handler, parameters.mProgressNotificationIntervalMillis, handler2, parameters.mIsShutterSoundOn, i, i2);
    }

    private static RecorderController createDefault(Context context, Accessor<CameraActionSound> accessor, Accessor<BypassCamera> accessor2, RecorderController.RecorderListener recorderListener, Handler handler, int i, Handler handler2, boolean z, boolean z2) {
        CamLog.d("Create recorder : DefaultRecorderController progress-interval:" + i + " intelligent-active:" + z + " shutter-sound:" + z2);
        return new DefaultRecorderController(context, accessor, accessor2, new DefaultRecorder(2, z), recorderListener, MIN_VIDEO_DURATION_MILLIS, handler, i, handler2, true, true, true, z2, z);
    }
}
