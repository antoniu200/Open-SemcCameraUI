// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import java.util.concurrent.ScheduledExecutorService;
import android.media.MediaActionSound;

public class CameraActionSound
{
    private static final int RELEASE_MEDIA_ACTION_SOUND_DELAY_MILLIS = 4000;
    public static final int SHUTTER_CLICK = 0;
    public static final int START_VIDEO_RECORDING = 2;
    public static final int STOP_VIDEO_RECORDING = 3;
    private static final String TAG = "CameraActionSound";
    private static final String THREAD_NAME = "CAS#Main";
    private static final String THREAD_NAME_ONETIME = "CAS#Onetime";
    private boolean mIsReleased;
    private MediaActionSound mMediaActionSound;
    private ScheduledExecutorService mSoundExecutor;
    
    public CameraActionSound() {
        this.mSoundExecutor = null;
        this.mMediaActionSound = new MediaActionSound();
        this.mIsReleased = false;
        this.mSoundExecutor = ThreadUtil.buildScheduledExecutor("CAS#Main", 10);
    }
    
    private static void releaseDelay(final MediaActionSound mediaActionSound, final ScheduledExecutorService scheduledExecutorService) {
        CamLog.d("invoked");
        scheduledExecutorService.schedule(new Runnable(mediaActionSound) {
            final MediaActionSound val$sound;
            
            @Override
            public void run() {
                this.val$sound.release();
            }
        }, 4000L, TimeUnit.MILLISECONDS);
    }
    
    public void load(final int i) {
        synchronized (this) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked isReleased:");
                sb.append(this.mIsReleased);
                sb.append(" name:");
                sb.append(i);
                CamLog.d(sb.toString());
            }
            if (this.mIsReleased) {
                return;
            }
            this.mSoundExecutor.submit(new Runnable(this, this.mMediaActionSound, i) {
                final CameraActionSound this$0;
                final MediaActionSound val$sound;
                final int val$soundName;
                
                @Override
                public void run() {
                    this.val$sound.load(this.val$soundName);
                }
            });
        }
    }
    
    public void play(final int i, final boolean b) {
        synchronized (this) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoked isReleased:");
                sb.append(this.mIsReleased);
                sb.append(" name:");
                sb.append(i);
                sb.append(" sync:");
                sb.append(b);
                CamLog.d(sb.toString());
            }
            Future<?> submit;
            if (this.mIsReleased) {
                final MediaActionSound mediaActionSound = new MediaActionSound();
                mediaActionSound.play(i);
                final ScheduledExecutorService buildScheduledExecutor = ThreadUtil.buildScheduledExecutor("CAS#Onetime");
                releaseDelay(mediaActionSound, buildScheduledExecutor);
                buildScheduledExecutor.shutdown();
                submit = null;
            }
            else {
                submit = this.mSoundExecutor.submit(new Runnable(this, this.mMediaActionSound, i) {
                    final CameraActionSound this$0;
                    final MediaActionSound val$sound;
                    final int val$soundName;
                    
                    @Override
                    public void run() {
                        this.val$sound.play(this.val$soundName);
                    }
                });
            }
            monitorexit(this);
            if (b && submit != null) {
                try {
                    submit.get();
                }
                catch (final InterruptedException | ExecutionException ex) {
                    CamLog.e("play(): Failed to wait for completion.", (Throwable)ex);
                }
            }
        }
    }
    
    public void release() {
        synchronized (this) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoked isReleased:");
            sb.append(this.mIsReleased);
            CamLog.d(sb.toString());
            if (!this.mIsReleased) {
                this.mIsReleased = true;
                releaseDelay(this.mMediaActionSound, this.mSoundExecutor);
                this.mMediaActionSound = null;
                this.mSoundExecutor.shutdown();
                this.mSoundExecutor = null;
            }
        }
    }
}
