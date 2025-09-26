// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.android.media.internal;

public interface ClockInterface
{
    long getCurrentTimeUs();
    
    long getDurationAtPauseUs();
    
    long getDurationAtStopUs();
    
    long getRecordedDurationUs();
    
    long getStartTimeUs();
    
    long getStopTimeUs();
    
    long getSystemTimeUs();
    
    long getTotalPausedDurationUs();
    
    boolean isPausedAt(final long p0);
    
    boolean isStarted();
    
    void pauseClock();
    
    void resetClock();
    
    void resumeClock();
    
    void setStartTime();
    
    void stopClock();
}
