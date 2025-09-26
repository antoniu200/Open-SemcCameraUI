// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller.selftimerfeedback;

import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.Handler;

public class SelfTimerFeedback
{
    private static final int SELF_TIMER_BLINK_DURATION = 100;
    private static final int[] SELF_TIMER_INTERVALS;
    private static final int[] SELF_TIMER_LEVEL_THRESHOLDS;
    private static final int SELF_TIMER_LIGHT_MIN_INTERVAL = 250;
    public static final String TAG = "SelfTimerFeedback";
    private final int mDuration;
    private final Handler mHandler;
    private final boolean mIsBlinkNeeded;
    private boolean mIsOnBlinkFinishedCalled;
    private final LedLight mLedLight;
    private final Runnable mLedOffTask;
    private final SelfTimerFeedbackListener mListener;
    private final TickEvent mOnTickEvent;
    private final PeriodicEvent mPeriodicEvent;
    
    static {
        SELF_TIMER_LEVEL_THRESHOLDS = new int[] { 500, 2000, 4000, 10000 };
        SELF_TIMER_INTERVALS = new int[] { -1, 250, 500, 1000 };
    }
    
    public SelfTimerFeedback(final int mDuration, final LedLight mLedLight, final boolean mIsBlinkNeeded, final SelfTimerFeedbackListener mListener) {
        this.mOnTickEvent = (TickEvent)new TickEvent() {
            final SelfTimerFeedback this$0;
            
            @Override
            public void onTick(final long n) {
                final long lng = this.this$0.mDuration - n;
                final boolean verbose = CamLog.VERBOSE;
                int n2 = 0;
                if (verbose) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("remain time: ");
                    sb.append(lng);
                    CamLog.d(sb.toString());
                }
                if (lng % 1000L == 0L && lng >= 4000L && n > 0L) {
                    this.this$0.mListener.onSoundTypeChange(lng);
                }
                if (lng <= 0L) {
                    this.this$0.mListener.onCountDownFinished();
                    return;
                }
                while (n2 < SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS.length && lng > SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS[n2]) {
                    ++n2;
                }
                int n3;
                if ((n3 = n2) >= SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS.length) {
                    n3 = SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS.length - 1;
                }
                if (this.this$0.mIsBlinkNeeded) {
                    if (n3 == 0) {
                        if (!this.this$0.mIsOnBlinkFinishedCalled) {
                            this.this$0.mListener.onBlinkFinished();
                            this.this$0.mIsOnBlinkFinishedCalled = true;
                        }
                    }
                    else if ((lng - SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS[n3]) % SelfTimerFeedback.SELF_TIMER_INTERVALS[n3] == 0L) {
                        this.this$0.blink();
                    }
                }
            }
        };
        this.mLedOffTask = new Runnable() {
            final SelfTimerFeedback this$0;
            
            @Override
            public void run() {
                this.this$0.mLedLight.turnOff();
            }
        };
        this.mDuration = mDuration;
        this.mLedLight = mLedLight;
        this.mIsBlinkNeeded = mIsBlinkNeeded;
        this.mListener = mListener;
        this.mHandler = new Handler();
        this.mPeriodicEvent = new PeriodicEvent(this.mHandler, this.mOnTickEvent, 250L, this.mDuration);
    }
    
    private void blink() {
        this.mLedLight.turnOn();
        this.mHandler.postDelayed(this.mLedOffTask, 100L);
    }
    
    public void start(final int n) {
        this.mIsOnBlinkFinishedCalled = false;
        this.mPeriodicEvent.start(n);
    }
    
    public void stop() {
        this.mPeriodicEvent.stop();
        this.mHandler.removeCallbacks(this.mLedOffTask);
    }
    
    private static class PeriodicEvent
    {
        private final TickEvent mCallback;
        private long mCounter;
        private final int mDuration;
        private final Handler mHandler;
        private final long mInterval;
        private long mStartTime;
        private final Runnable mTickEvent;
        
        public PeriodicEvent(final Handler mHandler, final TickEvent mCallback, final long mInterval, final int mDuration) {
            this.mTickEvent = new Runnable() {
                final PeriodicEvent this$0;
                
                @Override
                public void run() {
                    this.this$0.mCallback.onTick(this.this$0.mCounter * this.this$0.mInterval);
                    ++this.this$0.mCounter;
                    if (this.this$0.mCounter * this.this$0.mInterval <= this.this$0.mDuration) {
                        this.this$0.scheduleNextTick();
                    }
                }
            };
            this.mHandler = mHandler;
            this.mCallback = mCallback;
            this.mInterval = mInterval;
            this.mDuration = mDuration;
        }
        
        private void scheduleNextTick() {
            this.mHandler.postAtTime(this.mTickEvent, this.mStartTime + this.mCounter * this.mInterval);
        }
        
        public void start(final long n) {
            this.mStartTime = SystemClock.uptimeMillis() + n;
            this.mCounter = 0L;
            this.scheduleNextTick();
        }
        
        public void stop() {
            this.mHandler.removeCallbacks(this.mTickEvent);
        }
        
        public interface TickEvent
        {
            void onTick(final long p0);
        }
    }
    
    public interface SelfTimerFeedbackListener
    {
        void onBlinkFinished();
        
        void onCountDownFinished();
        
        void onSoundTypeChange(final long p0);
    }
}
