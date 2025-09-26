// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.interaction;

import android.graphics.PointF;
import java.util.TimerTask;
import android.os.Handler;
import java.util.Timer;
import android.graphics.Point;

public class TouchMoveAndStopDetector
{
    private static final float DIRECTION_TOLERANCE = 1.0471976f;
    public static final String TAG = "TouchMoveAndStopDetector";
    private int TOUCH_STOP_DETECTION_TIMER_INTERVAL;
    private Point mCurrentTouchPos;
    private Point mDownPos;
    private boolean mIsFingerAlreadyMoved;
    private Point mLatestCheckedPos;
    private Point mLatestCheckedTrackVec;
    private TouchStopDetectorListener mListener;
    private Point mPreviousTouchPos;
    private final int mTouchSlop;
    private Point mTouchSlopAreaCenterPos;
    private Timer mTouchStopDetectorTimer;
    private TouchStopDetectorTimerTask mTouchStopDetectorTimerTask;
    private Handler mUiThreadHandler;
    
    public TouchMoveAndStopDetector(final int mTouchSlop) {
        this.TOUCH_STOP_DETECTION_TIMER_INTERVAL = 200;
        this.mUiThreadHandler = new Handler();
        this.mDownPos = new Point(0, 0);
        this.mTouchSlopAreaCenterPos = new Point(0, 0);
        this.mCurrentTouchPos = new Point(0, 0);
        this.mPreviousTouchPos = new Point(0, 0);
        this.mLatestCheckedPos = new Point(0, 0);
        this.mLatestCheckedTrackVec = new Point(0, 0);
        this.mIsFingerAlreadyMoved = false;
        this.mTouchSlop = mTouchSlop;
    }
    
    private void killTimer() {
        if (this.mTouchStopDetectorTimer != null) {
            this.mTouchStopDetectorTimer.cancel();
            this.mTouchStopDetectorTimer.purge();
            this.mTouchStopDetectorTimer = null;
        }
        if (this.mTouchStopDetectorTimerTask != null) {
            this.mTouchStopDetectorTimerTask.cancel();
            this.mTouchStopDetectorTimerTask = null;
        }
    }
    
    private void onTouchStopDetected() {
        this.mIsFingerAlreadyMoved = false;
        this.mTouchSlopAreaCenterPos.set(this.mCurrentTouchPos.x, this.mCurrentTouchPos.y);
        this.mUiThreadHandler.post((Runnable)new Runnable(this) {
            final TouchMoveAndStopDetector this$0;
            
            @Override
            public void run() {
                if (this.this$0.mListener != null) {
                    this.this$0.mListener.onSingleTouchStopDetected(this.this$0.mCurrentTouchPos, this.this$0.mPreviousTouchPos, this.this$0.mDownPos);
                }
            }
        });
    }
    
    private void updateLastCheckedParameters(final int n, final int n2, final Point point) {
        this.mLatestCheckedPos.set(n, n2);
        this.mLatestCheckedTrackVec.set(point.x, point.y);
    }
    
    void release() {
        this.killTimer();
        this.mListener = null;
    }
    
    public void setTouchStopDetectorListener(final TouchStopDetectorListener mListener) {
        this.mListener = mListener;
    }
    
    public void startTouchStopDetection(final int n, final int n2) {
        synchronized (this) {
            this.mDownPos.set(n, n2);
            this.mPreviousTouchPos.set(n, n2);
            this.mTouchSlopAreaCenterPos.set(n, n2);
            this.mIsFingerAlreadyMoved = false;
            this.killTimer();
            this.mTouchStopDetectorTimer = new Timer(true);
            this.mTouchStopDetectorTimerTask = new TouchStopDetectorTimerTask();
            this.mTouchStopDetectorTimer.scheduleAtFixedRate(this.mTouchStopDetectorTimerTask, this.TOUCH_STOP_DETECTION_TIMER_INTERVAL, this.TOUCH_STOP_DETECTION_TIMER_INTERVAL);
        }
    }
    
    public void stopTouchStopDetection() {
        synchronized (this) {
            this.killTimer();
            this.mCurrentTouchPos.set(0, 0);
            this.mPreviousTouchPos.set(0, 0);
            this.mLatestCheckedPos.set(0, 0);
            this.mLatestCheckedTrackVec.set(0, 0);
        }
    }
    
    public void updateCurrentAndLastPosition(final int n, final int n2) {
        this.mPreviousTouchPos.set(n, n2);
        this.mCurrentTouchPos.set(n, n2);
    }
    
    public void updateCurrentPosition(int n, int n2) {
        this.mPreviousTouchPos.set(this.mCurrentTouchPos.x, this.mCurrentTouchPos.y);
        this.mCurrentTouchPos.set(n, n2);
        n = this.mCurrentTouchPos.x - this.mTouchSlopAreaCenterPos.x;
        n2 = this.mCurrentTouchPos.y - this.mTouchSlopAreaCenterPos.y;
        if (this.mTouchSlop * this.mTouchSlop < n * n + n2 * n2) {
            this.mIsFingerAlreadyMoved = true;
            if (this.mListener != null) {
                this.mListener.onSingleTouchMoveDetected(this.mCurrentTouchPos, this.mPreviousTouchPos, this.mDownPos);
            }
        }
    }
    
    public interface TouchStopDetectorListener
    {
        void onSingleTouchMoveDetected(final Point p0, final Point p1, final Point p2);
        
        void onSingleTouchStopDetected(final Point p0, final Point p1, final Point p2);
    }
    
    private class TouchStopDetectorTimerTask extends TimerTask
    {
        final TouchMoveAndStopDetector this$0;
        
        private TouchStopDetectorTimerTask(final TouchMoveAndStopDetector this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            final int n = this.this$0.mCurrentTouchPos.x - this.this$0.mLatestCheckedPos.x;
            final int n2 = this.this$0.mCurrentTouchPos.y - this.this$0.mLatestCheckedPos.y;
            final Point point = new Point(n, n2);
            final float radianFrom2Vector = VectorCalculator.getRadianFrom2Vector(new PointF(point), new PointF(this.this$0.mLatestCheckedTrackVec));
            this.this$0.updateLastCheckedParameters(this.this$0.mCurrentTouchPos.x, this.this$0.mCurrentTouchPos.y, point);
            if (!this.this$0.mIsFingerAlreadyMoved) {
                return;
            }
            if (n == 0 && n2 == 0) {
                this.this$0.onTouchStopDetected();
                return;
            }
            if (n * n + n2 * n2 < this.this$0.mTouchSlop * this.this$0.mTouchSlop) {
                if (Math.abs(radianFrom2Vector) < 1.0471976f) {
                    return;
                }
                this.this$0.onTouchStopDetected();
            }
        }
    }
}
