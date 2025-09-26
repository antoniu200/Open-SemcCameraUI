// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder;

import java.io.IOException;
import android.location.Location;
import android.content.Context;
import android.view.Surface;

public interface RecorderInterface
{
    Surface getSurface();
    
    boolean isAsyncStopSupported();
    
    void pause();
    
    boolean prepare(final Context p0, final RecorderParameters p1);
    
    void release();
    
    void reset();
    
    void resume();
    
    void setListener(final RecordTrackListener p0, final RecordTrackListener p1, final OnErrorListener p2, final OnMaxReachedListener p3);
    
    void setLocation(final Location p0);
    
    void setMaxDurationMillis(final long p0);
    
    void setMaxFileSizeBytes(final long p0);
    
    void setOrientationHint(final int p0);
    
    void setOutputFilePath(final String p0);
    
    void start() throws IOException;
    
    void stop();
    
    void stopAsync();
    
    void stopAudioRecording();
    
    void stopOnCameraError();
    
    void waitUntilStopCompleted();
    
    public interface OnErrorListener
    {
        void onError();
    }
    
    public interface OnMaxReachedListener
    {
        void onMaxDurationReached();
        
        void onMaxFileSizeReached();
    }
    
    public interface RecordTrackListener
    {
        void onCompleted();
        
        void onProgress(final long p0);
        
        void onStarted();
    }
}
