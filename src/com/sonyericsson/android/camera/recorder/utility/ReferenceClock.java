package com.sonyericsson.android.camera.recorder.utility;

import android.os.Handler;
import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ReferenceClock {
    public static final String TAG = "ReferenceClock";
    private long mElapsedTimeOffsetMillis;
    private long mInitialUptimeMillis;
    private boolean mIsMeasuring;
    private final TickCallback mTickCallback;
    private final TickEvent mTickEvent;
    private final Handler mTickHandler;
    private final long mTickInterval;

    public interface TickCallback {
        void onTick(long j);
    }

    public ReferenceClock() {
        this.mTickEvent = new TickEvent();
        this.mElapsedTimeOffsetMillis = 0L;
        this.mInitialUptimeMillis = 0L;
        this.mTickHandler = null;
        this.mTickCallback = null;
        this.mTickInterval = 0L;
        this.mIsMeasuring = false;
    }

    public ReferenceClock(Handler handler, TickCallback tickCallback, long j) {
        this.mTickEvent = new TickEvent();
        this.mElapsedTimeOffsetMillis = 0L;
        this.mInitialUptimeMillis = 0L;
        this.mTickHandler = handler;
        this.mTickCallback = tickCallback;
        this.mTickInterval = j;
        this.mIsMeasuring = false;
        if (this.mTickInterval <= 0) {
            throw new IllegalArgumentException("tickIntervalMillis must be lager that 0.");
        }
    }

    public void start() {
        long jNow = now();
        synchronized (this) {
            this.mElapsedTimeOffsetMillis = 0L;
            this.mInitialUptimeMillis = jNow;
            this.mIsMeasuring = true;
            scheduleNextTickEvent(jNow);
        }
    }

    public void stop() {
        long jNow = now();
        synchronized (this) {
            if (this.mIsMeasuring) {
                this.mElapsedTimeOffsetMillis += computeElapsedTimeSinceInitialTime(jNow);
                this.mInitialUptimeMillis = 0L;
                this.mIsMeasuring = false;
                cancelTickEvent();
            } else if (CamLog.VERBOSE) {
                CamLog.d("stop() is invoked in not running.");
            }
        }
    }

    public void resume() {
        long jNow = now();
        synchronized (this) {
            if (this.mIsMeasuring) {
                if (CamLog.VERBOSE) {
                    CamLog.d("resume() is invoked in running.");
                }
            } else {
                this.mInitialUptimeMillis = jNow;
                this.mIsMeasuring = true;
                scheduleNextTickEvent(jNow);
            }
        }
    }

    public void reset(long j) {
        long jNow = now();
        synchronized (this) {
            this.mElapsedTimeOffsetMillis = j;
            this.mInitialUptimeMillis = jNow;
        }
    }

    public long elapsedTimeMillis() {
        long jComputeElapsedTime;
        long jNow = now();
        synchronized (this) {
            jComputeElapsedTime = computeElapsedTime(jNow);
        }
        return jComputeElapsedTime;
    }

    public boolean isMeasuring() {
        boolean z;
        synchronized (this) {
            z = this.mIsMeasuring;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long now() {
        return SystemClock.uptimeMillis();
    }

    private long computeElapsedTime(long j) {
        long jComputeElapsedTimeSinceInitialTime;
        synchronized (this) {
            jComputeElapsedTimeSinceInitialTime = this.mElapsedTimeOffsetMillis + computeElapsedTimeSinceInitialTime(j);
        }
        return jComputeElapsedTimeSinceInitialTime;
    }

    private long computeElapsedTimeSinceInitialTime(long j) {
        synchronized (this) {
            if (!this.mIsMeasuring) {
                return 0L;
            }
            return j - this.mInitialUptimeMillis;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleNextTickEvent(long j) {
        if (this.mTickHandler != null) {
            synchronized (this) {
                if (this.mIsMeasuring) {
                    long jComputeElapsedTime = computeElapsedTime(j) / this.mTickInterval;
                    this.mTickEvent.requestElapsedTime = (jComputeElapsedTime + 1) * this.mTickInterval;
                    this.mTickHandler.postAtTime(this.mTickEvent, this.mInitialUptimeMillis + (this.mTickEvent.requestElapsedTime - this.mElapsedTimeOffsetMillis));
                }
            }
        }
    }

    private void cancelTickEvent() {
        if (this.mTickHandler != null) {
            this.mTickHandler.removeCallbacks(this.mTickEvent);
        }
    }

    private class TickEvent implements Runnable {
        public long requestElapsedTime;

        private TickEvent() {
            this.requestElapsedTime = 0L;
        }

        @Override // java.lang.Runnable
        public void run() {
            ReferenceClock.this.mTickCallback.onTick(this.requestElapsedTime);
            ReferenceClock.this.scheduleNextTickEvent(ReferenceClock.this.now());
        }
    }

    private class NotifyTickEvent implements Runnable {
        public final long requestElapsedTime;

        public NotifyTickEvent(long j) {
            this.requestElapsedTime = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            ReferenceClock.this.mTickCallback.onTick(this.requestElapsedTime);
        }
    }
}
