package com.sonymobile.android.media.internal;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface ClockInterface {
    long getCurrentTimeUs();

    long getDurationAtPauseUs();

    long getDurationAtStopUs();

    long getRecordedDurationUs();

    long getStartTimeUs();

    long getStopTimeUs();

    long getSystemTimeUs();

    long getTotalPausedDurationUs();

    boolean isPausedAt(long j);

    boolean isStarted();

    void pauseClock();

    void resetClock();

    void resumeClock();

    void setStartTime();

    void stopClock();
}
