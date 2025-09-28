package com.sonyericsson.android.camera.recorder;

import android.content.Context;
import android.location.Location;
import android.view.Surface;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface RecorderInterface {

    public interface OnErrorListener {
        void onError();
    }

    public interface OnMaxReachedListener {
        void onMaxDurationReached();

        void onMaxFileSizeReached();
    }

    public interface RecordTrackListener {
        void onCompleted();

        void onProgress(long j);

        void onStarted();
    }

    Surface getSurface();

    boolean isAsyncStopSupported();

    void pause();

    boolean prepare(Context context, RecorderParameters recorderParameters);

    void release();

    void reset();

    void resume();

    void setListener(RecordTrackListener recordTrackListener, RecordTrackListener recordTrackListener2, OnErrorListener onErrorListener, OnMaxReachedListener onMaxReachedListener);

    void setLocation(Location location);

    void setMaxDurationMillis(long j);

    void setMaxFileSizeBytes(long j);

    void setOrientationHint(int i);

    void setOutputFilePath(String str);

    void start() throws IOException;

    void stop();

    void stopAsync();

    void stopAudioRecording();

    void stopOnCameraError();

    void waitUntilStopCompleted();
}
