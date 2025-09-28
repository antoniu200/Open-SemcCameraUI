package com.sonyericsson.android.camera.recorder;

import android.location.Location;
import android.support.annotation.Nullable;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import com.sonyericsson.cameracommon.storage.Storage;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface RecorderController {

    public interface RecorderListener {
        void onRecordError(int i, int i2);

        void onRecordFinished(Result result);

        void onRecordProgress(long j);

        void setSavingRequestBuilder(RequestFactory.VideoSavingRequestBuilder videoSavingRequestBuilder);
    }

    public enum Result {
        SUCCESS,
        FAIL,
        MAX_DURATION_REACHED,
        MAX_FILESIZE_REACHED
    }

    long getRecordingTimeMillis();

    boolean isPaused();

    boolean isReady();

    boolean isRecording();

    boolean isStarting();

    boolean isStopping();

    void pause() throws RecorderException;

    boolean prepare(RecorderParameters recorderParameters);

    boolean release();

    void resume() throws RecorderException;

    void setLocation(Location location);

    void setMaxDurationMillis(long j);

    void setMaxFileSizeBytes(long j);

    void setOrientationHint(int i);

    void setOutputFilePath(String str);

    void setStorageWriteNotifier(@Nullable Storage.StorageWriteNotifier storageWriteNotifier);

    void setUserSoundSetting(boolean z);

    void start() throws RecorderException;

    void stop() throws RecorderException;

    void stopAudioRecording();

    void stopOnCameraError() throws RecorderException;
}
