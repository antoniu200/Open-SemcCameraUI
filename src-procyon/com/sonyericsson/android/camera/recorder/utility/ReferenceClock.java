// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.utility;

import com.sonyericsson.android.camera.util.CamLog;
import android.os.SystemClock;
import android.os.Handler;

public class ReferenceClock
{
    public static final String TAG = "ReferenceClock";
    private long mElapsedTimeOffsetMillis;
    private long mInitialUptimeMillis;
    private boolean mIsMeasuring;
    private final TickCallback mTickCallback;
    private final TickEvent mTickEvent;
    private final Handler mTickHandler;
    private final long mTickInterval;
    
    public ReferenceClock() {
        this.mTickEvent = new TickEvent();
        this.mElapsedTimeOffsetMillis = 0L;
        this.mInitialUptimeMillis = 0L;
        this.mTickHandler = null;
        this.mTickCallback = null;
        this.mTickInterval = 0L;
        this.mIsMeasuring = false;
    }
    
    public ReferenceClock(final Handler mTickHandler, final TickCallback mTickCallback, final long mTickInterval) {
        this.mTickEvent = new TickEvent();
        this.mElapsedTimeOffsetMillis = 0L;
        this.mInitialUptimeMillis = 0L;
        this.mTickHandler = mTickHandler;
        this.mTickCallback = mTickCallback;
        this.mTickInterval = mTickInterval;
        this.mIsMeasuring = false;
        if (this.mTickInterval <= 0L) {
            throw new IllegalArgumentException("tickIntervalMillis must be lager that 0.");
        }
    }
    
    private void cancelTickEvent() {
        if (this.mTickHandler != null) {
            this.mTickHandler.removeCallbacks((Runnable)this.mTickEvent);
        }
    }
    
    private long computeElapsedTime(long computeElapsedTimeSinceInitialTime) {
        synchronized (this) {
            final long mElapsedTimeOffsetMillis = this.mElapsedTimeOffsetMillis;
            computeElapsedTimeSinceInitialTime = this.computeElapsedTimeSinceInitialTime(computeElapsedTimeSinceInitialTime);
            return mElapsedTimeOffsetMillis + computeElapsedTimeSinceInitialTime;
        }
    }
    
    private long computeElapsedTimeSinceInitialTime(final long n) {
        synchronized (this) {
            if (this.mIsMeasuring) {
                final long mInitialUptimeMillis = this.mInitialUptimeMillis;
                monitorexit(this);
                return n - mInitialUptimeMillis;
            }
            return 0L;
        }
    }
    
    private long now() {
        return SystemClock.uptimeMillis();
    }
    
    private void scheduleNextTickEvent(long mElapsedTimeOffsetMillis) {
        if (this.mTickHandler != null) {
            synchronized (this) {
                if (this.mIsMeasuring) {
                    mElapsedTimeOffsetMillis = this.computeElapsedTime(mElapsedTimeOffsetMillis) / this.mTickInterval;
                    this.mTickEvent.requestElapsedTime = (mElapsedTimeOffsetMillis + 1L) * this.mTickInterval;
                    final long requestElapsedTime = this.mTickEvent.requestElapsedTime;
                    mElapsedTimeOffsetMillis = this.mElapsedTimeOffsetMillis;
                    this.mTickHandler.postAtTime((Runnable)this.mTickEvent, this.mInitialUptimeMillis + (requestElapsedTime - mElapsedTimeOffsetMillis));
                }
            }
        }
    }
    
    public long elapsedTimeMillis() {
        final long now = this.now();
        synchronized (this) {
            return this.computeElapsedTime(now);
        }
    }
    
    public boolean isMeasuring() {
        synchronized (this) {
            return this.mIsMeasuring;
        }
    }
    
    public void reset(final long mElapsedTimeOffsetMillis) {
        final long now = this.now();
        synchronized (this) {
            this.mElapsedTimeOffsetMillis = mElapsedTimeOffsetMillis;
            this.mInitialUptimeMillis = now;
        }
    }
    
    public void resume() {
        final long now = this.now();
        synchronized (this) {
            if (this.mIsMeasuring) {
                if (CamLog.VERBOSE) {
                    CamLog.d("resume() is invoked in running.");
                }
            }
            else {
                this.mInitialUptimeMillis = now;
                this.mIsMeasuring = true;
                this.scheduleNextTickEvent(now);
            }
        }
    }
    
    public void start() {
        final long now = this.now();
        synchronized (this) {
            this.mElapsedTimeOffsetMillis = 0L;
            this.mInitialUptimeMillis = now;
            this.mIsMeasuring = true;
            this.scheduleNextTickEvent(now);
        }
    }
    
    public void stop() {
        final long now = this.now();
        synchronized (this) {
            if (this.mIsMeasuring) {
                this.mElapsedTimeOffsetMillis += this.computeElapsedTimeSinceInitialTime(now);
                this.mInitialUptimeMillis = 0L;
                this.mIsMeasuring = false;
                this.cancelTickEvent();
            }
            else if (CamLog.VERBOSE) {
                CamLog.d("stop() is invoked in not running.");
            }
        }
    }
    
    private class NotifyTickEvent implements Runnable
    {
        public final long requestElapsedTime;
        final ReferenceClock this$0;
        
        public NotifyTickEvent(final ReferenceClock this$0, final long requestElapsedTime) {
            this.this$0 = this$0;
            this.requestElapsedTime = requestElapsedTime;
        }
        
        @Override
        public void run() {
            this.this$0.mTickCallback.onTick(this.requestElapsedTime);
        }
    }
    
    public interface TickCallback
    {
        void onTick(final long p0);
    }
    
    private class TickEvent implements Runnable
    {
        public long requestElapsedTime;
        final ReferenceClock this$0;
        
        private TickEvent(final ReferenceClock this$0) {
            this.this$0 = this$0;
            this.requestElapsedTime = 0L;
        }
        
        @Override
        public void run() {
            this.this$0.mTickCallback.onTick(this.requestElapsedTime);
            this.this$0.scheduleNextTickEvent(this.this$0.now());
        }
    }
}
