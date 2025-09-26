package com.sonyericsson.android.camera.device;

import android.media.MediaActionSound;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraActionSound {
    private static final int RELEASE_MEDIA_ACTION_SOUND_DELAY_MILLIS = 4000;
    public static final int SHUTTER_CLICK = 0;
    public static final int START_VIDEO_RECORDING = 2;
    public static final int STOP_VIDEO_RECORDING = 3;
    private static final String TAG = "CameraActionSound";
    private static final String THREAD_NAME = "CAS#Main";
    private static final String THREAD_NAME_ONETIME = "CAS#Onetime";
    private ScheduledExecutorService mSoundExecutor;
    private MediaActionSound mMediaActionSound = new MediaActionSound();
    private boolean mIsReleased = false;

    public CameraActionSound() {
        this.mSoundExecutor = null;
        this.mSoundExecutor = ThreadUtil.buildScheduledExecutor(THREAD_NAME, 10);
    }

    public void load(final int i) {
        synchronized (this) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked isReleased:" + this.mIsReleased + " name:" + i);
            }
            if (this.mIsReleased) {
                return;
            }
            final MediaActionSound mediaActionSound = this.mMediaActionSound;
            this.mSoundExecutor.submit(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraActionSound.1
                @Override // java.lang.Runnable
                public void run() {
                    mediaActionSound.load(i);
                }
            });
        }
    }

    public void play(final int i, boolean z) throws ExecutionException, InterruptedException {
        Future<?> futureSubmit;
        synchronized (this) {
            if (CamLog.DEBUG) {
                CamLog.d("invoked isReleased:" + this.mIsReleased + " name:" + i + " sync:" + z);
            }
            if (this.mIsReleased) {
                MediaActionSound mediaActionSound = new MediaActionSound();
                mediaActionSound.play(i);
                ScheduledExecutorService scheduledExecutorServiceBuildScheduledExecutor = ThreadUtil.buildScheduledExecutor(THREAD_NAME_ONETIME);
                releaseDelay(mediaActionSound, scheduledExecutorServiceBuildScheduledExecutor);
                scheduledExecutorServiceBuildScheduledExecutor.shutdown();
                futureSubmit = null;
            } else {
                final MediaActionSound mediaActionSound2 = this.mMediaActionSound;
                futureSubmit = this.mSoundExecutor.submit(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraActionSound.2
                    @Override // java.lang.Runnable
                    public void run() {
                        mediaActionSound2.play(i);
                    }
                });
            }
        }
        if (!z || futureSubmit == null) {
            return;
        }
        try {
            futureSubmit.get();
        } catch (InterruptedException | ExecutionException e) {
            CamLog.e("play(): Failed to wait for completion.", e);
        }
    }

    public void release() {
        synchronized (this) {
            CamLog.d("invoked isReleased:" + this.mIsReleased);
            if (!this.mIsReleased) {
                this.mIsReleased = true;
                releaseDelay(this.mMediaActionSound, this.mSoundExecutor);
                this.mMediaActionSound = null;
                this.mSoundExecutor.shutdown();
                this.mSoundExecutor = null;
            }
        }
    }

    private static void releaseDelay(final MediaActionSound mediaActionSound, ScheduledExecutorService scheduledExecutorService) {
        CamLog.d("invoked");
        scheduledExecutorService.schedule(new Runnable() { // from class: com.sonyericsson.android.camera.device.CameraActionSound.3
            @Override // java.lang.Runnable
            public void run() {
                mediaActionSound.release();
            }
        }, 4000L, TimeUnit.MILLISECONDS);
    }
}
