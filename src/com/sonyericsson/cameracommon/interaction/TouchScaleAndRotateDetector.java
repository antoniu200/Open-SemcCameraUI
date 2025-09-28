package com.sonyericsson.cameracommon.interaction;

import android.graphics.PointF;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TouchScaleAndRotateDetector {
    private static final int ROTATE_DETECTION_THRESHOLD_DEGREE = 1;
    public static final String TAG = "TouchScaleAndRotateDetector";
    private ScaleAndRotateDetectorListener mListener;
    private PointF mPreviousAxisVec;
    private PointF mPreviousTouchPos0;
    private PointF mPreviousTouchPos1;
    private PointF mCurrentTouchPos0 = new PointF(0.0f, 0.0f);
    private PointF mCurrentTouchPos1 = new PointF(0.0f, 0.0f);
    private PointF mTouchVec0 = new PointF(0.0f, 0.0f);
    private PointF mTouchVec1 = new PointF(0.0f, 0.0f);
    private PointF mCurrentAxisVec = new PointF(0.0f, 0.0f);
    private float mAxisRotateDeg = 0.0f;
    private float mOriginalAxisLen = 0.0f;

    public interface ScaleAndRotateDetectorListener {
        void onDoubleTouchRotateDetected(float f, float f2);

        void onDoubleTouchScaleDetected(float f, float f2, float f3);
    }

    void release() {
        this.mListener = null;
    }

    public void setScaleAndRotateDetectorListener(ScaleAndRotateDetectorListener scaleAndRotateDetectorListener) {
        this.mListener = scaleAndRotateDetectorListener;
    }

    public void startScaleAndRotateDetection(PointF pointF, PointF pointF2) {
        this.mPreviousTouchPos0 = new PointF(pointF.x, pointF.y);
        this.mPreviousTouchPos1 = new PointF(pointF2.x, pointF2.y);
        this.mPreviousAxisVec = new PointF(pointF2.x - pointF.x, pointF2.y - pointF.y);
        this.mOriginalAxisLen = this.mPreviousAxisVec.length();
    }

    public void updateCurrentPosition(PointF pointF, PointF pointF2) {
        this.mCurrentTouchPos0.set(pointF);
        this.mCurrentTouchPos1.set(pointF2);
        this.mTouchVec0.set(this.mCurrentTouchPos0.x - this.mPreviousTouchPos0.x, this.mCurrentTouchPos0.y - this.mPreviousTouchPos0.y);
        this.mTouchVec1.set(this.mCurrentTouchPos1.x - this.mPreviousTouchPos1.x, this.mCurrentTouchPos1.y - this.mPreviousTouchPos1.y);
        this.mCurrentAxisVec.set(this.mCurrentTouchPos1.x - this.mCurrentTouchPos0.x, this.mCurrentTouchPos1.y - this.mCurrentTouchPos0.y);
        if (VectorCalculator.isSquare(this.mCurrentAxisVec, this.mTouchVec0) && VectorCalculator.isSquare(this.mCurrentAxisVec, this.mTouchVec1)) {
            float f = this.mAxisRotateDeg;
            this.mAxisRotateDeg += (float) ((((VectorCalculator.getRadianFrom2Vector(this.mPreviousAxisVec, this.mCurrentAxisVec) * 360.0f) / 2.0f) / 3.141592653589793d) * (0.0f <= (this.mPreviousAxisVec.x * this.mCurrentAxisVec.y) - (this.mCurrentAxisVec.x * this.mPreviousAxisVec.y) ? 1.0f : -1.0f));
            if (1.0f <= Math.abs(this.mAxisRotateDeg - f)) {
                this.mListener.onDoubleTouchRotateDetected(this.mAxisRotateDeg, this.mAxisRotateDeg - f);
            }
        }
        if (VectorCalculator.isParallel(this.mCurrentAxisVec, this.mTouchVec0) && VectorCalculator.isParallel(this.mCurrentAxisVec, this.mTouchVec1)) {
            this.mListener.onDoubleTouchScaleDetected(this.mCurrentAxisVec.length(), this.mPreviousAxisVec.length(), this.mOriginalAxisLen);
        }
        this.mPreviousAxisVec.set(this.mCurrentAxisVec);
        this.mPreviousTouchPos0.set(this.mCurrentTouchPos0);
        this.mPreviousTouchPos1.set(this.mCurrentTouchPos1);
    }

    public void stopScaleAndRotateDetection() {
        this.mCurrentTouchPos0.set(0.0f, 0.0f);
        this.mCurrentTouchPos1.set(0.0f, 0.0f);
        this.mPreviousTouchPos0 = null;
        this.mPreviousTouchPos1 = null;
        this.mTouchVec0.set(0.0f, 0.0f);
        this.mTouchVec1.set(0.0f, 0.0f);
        this.mPreviousAxisVec = null;
        this.mCurrentAxisVec.set(0.0f, 0.0f);
        this.mAxisRotateDeg = 0.0f;
        this.mOriginalAxisLen = 0.0f;
    }
}
