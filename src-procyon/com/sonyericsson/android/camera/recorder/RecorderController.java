// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder;

import com.sonyericsson.cameracommon.storage.RequestFactory;
import android.support.annotation.Nullable;
import com.sonyericsson.cameracommon.storage.Storage;
import android.location.Location;

public interface RecorderController
{
    long getRecordingTimeMillis();
    
    boolean isPaused();
    
    boolean isReady();
    
    boolean isRecording();
    
    boolean isStarting();
    
    boolean isStopping();
    
    void pause() throws RecorderException;
    
    boolean prepare(final RecorderParameters p0);
    
    boolean release();
    
    void resume() throws RecorderException;
    
    void setLocation(final Location p0);
    
    void setMaxDurationMillis(final long p0);
    
    void setMaxFileSizeBytes(final long p0);
    
    void setOrientationHint(final int p0);
    
    void setOutputFilePath(final String p0);
    
    void setStorageWriteNotifier(@Nullable final Storage.StorageWriteNotifier p0);
    
    void setUserSoundSetting(final boolean p0);
    
    void start() throws RecorderException;
    
    void stop() throws RecorderException;
    
    void stopAudioRecording();
    
    void stopOnCameraError() throws RecorderException;
    
    public interface RecorderListener
    {
        void onRecordError(final int p0, final int p1);
        
        void onRecordFinished(final Result p0);
        
        void onRecordProgress(final long p0);
        
        void setSavingRequestBuilder(final RequestFactory.VideoSavingRequestBuilder p0);
    }
    
    public enum Result
    {
        private static final Result[] $VALUES;
        
        FAIL, 
        MAX_DURATION_REACHED, 
        MAX_FILESIZE_REACHED, 
        SUCCESS;
        
        static {
            $VALUES = new Result[] { Result.SUCCESS, Result.FAIL, Result.MAX_DURATION_REACHED, Result.MAX_FILESIZE_REACHED };
        }
    }
}
