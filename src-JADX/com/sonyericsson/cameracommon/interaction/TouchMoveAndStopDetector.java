package com.sonyericsson.cameracommon.interaction;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TouchMoveAndStopDetector {
    private static final float DIRECTION_TOLERANCE = 1.0471976f;
    public static final String TAG = "TouchMoveAndStopDetector";
    private TouchStopDetectorListener mListener;
    private final int mTouchSlop;
    private Timer mTouchStopDetectorTimer;
    private TouchStopDetectorTimerTask mTouchStopDetectorTimerTask;
    private int TOUCH_STOP_DETECTION_TIMER_INTERVAL = 200;
    private Handler mUiThreadHandler = new Handler();
    private Point mDownPos = new Point(0, 0);
    private Point mTouchSlopAreaCenterPos = new Point(0, 0);
    private Point mCurrentTouchPos = new Point(0, 0);
    private Point mPreviousTouchPos = new Point(0, 0);
    private Point mLatestCheckedPos = new Point(0, 0);
    private Point mLatestCheckedTrackVec = new Point(0, 0);
    private boolean mIsFingerAlreadyMoved = false;

    public interface TouchStopDetectorListener {
        void onSingleTouchMoveDetected(Point point, Point point2, Point point3);

        void onSingleTouchStopDetected(Point point, Point point2, Point point3);
    }

    public TouchMoveAndStopDetector(int i) {
        this.mTouchSlop = i;
    }

    void release() {
        killTimer();
        this.mListener = null;
    }

    public void setTouchStopDetectorListener(TouchStopDetectorListener touchStopDetectorListener) {
        this.mListener = touchStopDetectorListener;
    }

    public synchronized void startTouchStopDetection(int i, int i2) {
        this.mDownPos.set(i, i2);
        this.mPreviousTouchPos.set(i, i2);
        this.mTouchSlopAreaCenterPos.set(i, i2);
        this.mIsFingerAlreadyMoved = false;
        killTimer();
        this.mTouchStopDetectorTimer = new Timer(true);
        this.mTouchStopDetectorTimerTask = new TouchStopDetectorTimerTask();
        this.mTouchStopDetectorTimer.scheduleAtFixedRate(this.mTouchStopDetectorTimerTask, this.TOUCH_STOP_DETECTION_TIMER_INTERVAL, this.TOUCH_STOP_DETECTION_TIMER_INTERVAL);
    }

    public void updateCurrentPosition(int i, int i2) {
        this.mPreviousTouchPos.set(this.mCurrentTouchPos.x, this.mCurrentTouchPos.y);
        this.mCurrentTouchPos.set(i, i2);
        int i3 = this.mCurrentTouchPos.x - this.mTouchSlopAreaCenterPos.x;
        int i4 = this.mCurrentTouchPos.y - this.mTouchSlopAreaCenterPos.y;
        if (this.mTouchSlop * this.mTouchSlop < (i3 * i3) + (i4 * i4)) {
            this.mIsFingerAlreadyMoved = true;
            if (this.mListener != null) {
                this.mListener.onSingleTouchMoveDetected(this.mCurrentTouchPos, this.mPreviousTouchPos, this.mDownPos);
            }
        }
    }

    public void updateCurrentAndLastPosition(int i, int i2) {
        this.mPreviousTouchPos.set(i, i2);
        this.mCurrentTouchPos.set(i, i2);
    }

    public synchronized void stopTouchStopDetection() {
        killTimer();
        this.mCurrentTouchPos.set(0, 0);
        this.mPreviousTouchPos.set(0, 0);
        this.mLatestCheckedPos.set(0, 0);
        this.mLatestCheckedTrackVec.set(0, 0);
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

    private class TouchStopDetectorTimerTask extends TimerTask {
        private TouchStopDetectorTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            int i = TouchMoveAndStopDetector.this.mCurrentTouchPos.x - TouchMoveAndStopDetector.this.mLatestCheckedPos.x;
            int i2 = TouchMoveAndStopDetector.this.mCurrentTouchPos.y - TouchMoveAndStopDetector.this.mLatestCheckedPos.y;
            Point point = new Point(i, i2);
            float radianFrom2Vector = VectorCalculator.getRadianFrom2Vector(new PointF(point), new PointF(TouchMoveAndStopDetector.this.mLatestCheckedTrackVec));
            TouchMoveAndStopDetector.this.updateLastCheckedParameters(TouchMoveAndStopDetector.this.mCurrentTouchPos.x, TouchMoveAndStopDetector.this.mCurrentTouchPos.y, point);
            if (TouchMoveAndStopDetector.this.mIsFingerAlreadyMoved) {
                if (i != 0 || i2 != 0) {
                    if ((i * i) + (i2 * i2) >= TouchMoveAndStopDetector.this.mTouchSlop * TouchMoveAndStopDetector.this.mTouchSlop || Math.abs(radianFrom2Vector) < TouchMoveAndStopDetector.DIRECTION_TOLERANCE) {
                        return;
                    }
                    TouchMoveAndStopDetector.this.onTouchStopDetected();
                    return;
                }
                TouchMoveAndStopDetector.this.onTouchStopDetected();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLastCheckedParameters(int i, int i2, Point point) {
        this.mLatestCheckedPos.set(i, i2);
        this.mLatestCheckedTrackVec.set(point.x, point.y);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTouchStopDetected() {
        this.mIsFingerAlreadyMoved = false;
        this.mTouchSlopAreaCenterPos.set(this.mCurrentTouchPos.x, this.mCurrentTouchPos.y);
        this.mUiThreadHandler.post(new Runnable() { // from class: com.sonyericsson.cameracommon.interaction.TouchMoveAndStopDetector.1
            @Override // java.lang.Runnable
            public void run() {
                if (TouchMoveAndStopDetector.this.mListener != null) {
                    TouchMoveAndStopDetector.this.mListener.onSingleTouchStopDetected(TouchMoveAndStopDetector.this.mCurrentTouchPos, TouchMoveAndStopDetector.this.mPreviousTouchPos, TouchMoveAndStopDetector.this.mDownPos);
                }
            }
        });
    }
}
