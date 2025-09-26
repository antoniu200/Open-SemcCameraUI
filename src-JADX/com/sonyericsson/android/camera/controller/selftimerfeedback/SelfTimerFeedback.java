package com.sonyericsson.android.camera.controller.selftimerfeedback;

import android.os.Handler;
import android.os.SystemClock;
import com.sonyericsson.android.camera.Constants;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.android.media.MediaRecorder;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SelfTimerFeedback {
    private static final int SELF_TIMER_BLINK_DURATION = 100;
    private static final int SELF_TIMER_LIGHT_MIN_INTERVAL = 250;
    public static final String TAG = "SelfTimerFeedback";
    private final int mDuration;
    private final boolean mIsBlinkNeeded;
    private boolean mIsOnBlinkFinishedCalled;
    private final LedLight mLedLight;
    private final SelfTimerFeedbackListener mListener;
    private final PeriodicEvent mPeriodicEvent;
    private static final int[] SELF_TIMER_LEVEL_THRESHOLDS = {Constants.INTERVAL_OPEN_CAMERA, MediaRecorder.MEDIA_RECORDER_TRACK_INFO_LIST_END, 4000, IptcConstants.IMAGE_RESOURCE_BLOCK_PRINT_FLAGS_INFO};
    private static final int[] SELF_TIMER_INTERVALS = {-1, 250, Constants.INTERVAL_OPEN_CAMERA, 1000};
    private final PeriodicEvent.TickEvent mOnTickEvent = new PeriodicEvent.TickEvent() { // from class: com.sonyericsson.android.camera.controller.selftimerfeedback.SelfTimerFeedback.1
        @Override // com.sonyericsson.android.camera.controller.selftimerfeedback.SelfTimerFeedback.PeriodicEvent.TickEvent
        public void onTick(long j) {
            long j2 = SelfTimerFeedback.this.mDuration - j;
            int length = 0;
            if (CamLog.VERBOSE) {
                CamLog.d("remain time: " + j2);
            }
            if (j2 % 1000 == 0 && j2 >= 4000 && j > 0) {
                SelfTimerFeedback.this.mListener.onSoundTypeChange(j2);
            }
            if (j2 <= 0) {
                SelfTimerFeedback.this.mListener.onCountDownFinished();
                return;
            }
            while (length < SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS.length && j2 > SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS[length]) {
                length++;
            }
            if (length >= SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS.length) {
                length = SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS.length - 1;
            }
            if (SelfTimerFeedback.this.mIsBlinkNeeded) {
                if (length != 0) {
                    if ((j2 - SelfTimerFeedback.SELF_TIMER_LEVEL_THRESHOLDS[length]) % SelfTimerFeedback.SELF_TIMER_INTERVALS[length] == 0) {
                        SelfTimerFeedback.this.blink();
                    }
                } else {
                    if (SelfTimerFeedback.this.mIsOnBlinkFinishedCalled) {
                        return;
                    }
                    SelfTimerFeedback.this.mListener.onBlinkFinished();
                    SelfTimerFeedback.this.mIsOnBlinkFinishedCalled = true;
                }
            }
        }
    };
    private final Runnable mLedOffTask = new Runnable() { // from class: com.sonyericsson.android.camera.controller.selftimerfeedback.SelfTimerFeedback.2
        @Override // java.lang.Runnable
        public void run() {
            SelfTimerFeedback.this.mLedLight.turnOff();
        }
    };
    private final Handler mHandler = new Handler();

    public interface SelfTimerFeedbackListener {
        void onBlinkFinished();

        void onCountDownFinished();

        void onSoundTypeChange(long j);
    }

    public SelfTimerFeedback(int i, LedLight ledLight, boolean z, SelfTimerFeedbackListener selfTimerFeedbackListener) {
        this.mDuration = i;
        this.mLedLight = ledLight;
        this.mIsBlinkNeeded = z;
        this.mListener = selfTimerFeedbackListener;
        this.mPeriodicEvent = new PeriodicEvent(this.mHandler, this.mOnTickEvent, 250L, this.mDuration);
    }

    public void start(int i) {
        this.mIsOnBlinkFinishedCalled = false;
        this.mPeriodicEvent.start(i);
    }

    public void stop() {
        this.mPeriodicEvent.stop();
        this.mHandler.removeCallbacks(this.mLedOffTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void blink() {
        this.mLedLight.turnOn();
        this.mHandler.postDelayed(this.mLedOffTask, 100L);
    }

    private static class PeriodicEvent {
        private final TickEvent mCallback;
        private long mCounter;
        private final int mDuration;
        private final Handler mHandler;
        private final long mInterval;
        private long mStartTime;
        private final Runnable mTickEvent = new Runnable() { // from class: com.sonyericsson.android.camera.controller.selftimerfeedback.SelfTimerFeedback.PeriodicEvent.1
            @Override // java.lang.Runnable
            public void run() {
                PeriodicEvent.this.mCallback.onTick(PeriodicEvent.this.mCounter * PeriodicEvent.this.mInterval);
                PeriodicEvent.access$804(PeriodicEvent.this);
                if (PeriodicEvent.this.mCounter * PeriodicEvent.this.mInterval <= PeriodicEvent.this.mDuration) {
                    PeriodicEvent.this.scheduleNextTick();
                }
            }
        };

        public interface TickEvent {
            void onTick(long j);
        }

        static /* synthetic */ long access$804(PeriodicEvent periodicEvent) {
            long j = periodicEvent.mCounter + 1;
            periodicEvent.mCounter = j;
            return j;
        }

        public PeriodicEvent(Handler handler, TickEvent tickEvent, long j, int i) {
            this.mHandler = handler;
            this.mCallback = tickEvent;
            this.mInterval = j;
            this.mDuration = i;
        }

        public void start(long j) {
            this.mStartTime = SystemClock.uptimeMillis() + j;
            this.mCounter = 0L;
            scheduleNextTick();
        }

        public void stop() {
            this.mHandler.removeCallbacks(this.mTickEvent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void scheduleNextTick() {
            this.mHandler.postAtTime(this.mTickEvent, this.mStartTime + (this.mCounter * this.mInterval));
        }
    }
}
