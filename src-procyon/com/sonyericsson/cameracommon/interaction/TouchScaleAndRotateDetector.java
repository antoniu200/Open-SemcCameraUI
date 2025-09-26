// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.interaction;

import android.graphics.PointF;

public class TouchScaleAndRotateDetector
{
    private static final int ROTATE_DETECTION_THRESHOLD_DEGREE = 1;
    public static final String TAG = "TouchScaleAndRotateDetector";
    private float mAxisRotateDeg;
    private PointF mCurrentAxisVec;
    private PointF mCurrentTouchPos0;
    private PointF mCurrentTouchPos1;
    private ScaleAndRotateDetectorListener mListener;
    private float mOriginalAxisLen;
    private PointF mPreviousAxisVec;
    private PointF mPreviousTouchPos0;
    private PointF mPreviousTouchPos1;
    private PointF mTouchVec0;
    private PointF mTouchVec1;
    
    public TouchScaleAndRotateDetector() {
        this.mCurrentTouchPos0 = new PointF(0.0f, 0.0f);
        this.mCurrentTouchPos1 = new PointF(0.0f, 0.0f);
        this.mTouchVec0 = new PointF(0.0f, 0.0f);
        this.mTouchVec1 = new PointF(0.0f, 0.0f);
        this.mCurrentAxisVec = new PointF(0.0f, 0.0f);
        this.mAxisRotateDeg = 0.0f;
        this.mOriginalAxisLen = 0.0f;
    }
    
    void release() {
        this.mListener = null;
    }
    
    public void setScaleAndRotateDetectorListener(final ScaleAndRotateDetectorListener mListener) {
        this.mListener = mListener;
    }
    
    public void startScaleAndRotateDetection(final PointF pointF, final PointF pointF2) {
        this.mPreviousTouchPos0 = new PointF(pointF.x, pointF.y);
        this.mPreviousTouchPos1 = new PointF(pointF2.x, pointF2.y);
        this.mPreviousAxisVec = new PointF(pointF2.x - pointF.x, pointF2.y - pointF.y);
        this.mOriginalAxisLen = this.mPreviousAxisVec.length();
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
    
    public void updateCurrentPosition(final PointF pointF, final PointF pointF2) {
        this.mCurrentTouchPos0.set(pointF);
        this.mCurrentTouchPos1.set(pointF2);
        this.mTouchVec0.set(this.mCurrentTouchPos0.x - this.mPreviousTouchPos0.x, this.mCurrentTouchPos0.y - this.mPreviousTouchPos0.y);
        this.mTouchVec1.set(this.mCurrentTouchPos1.x - this.mPreviousTouchPos1.x, this.mCurrentTouchPos1.y - this.mPreviousTouchPos1.y);
        this.mCurrentAxisVec.set(this.mCurrentTouchPos1.x - this.mCurrentTouchPos0.x, this.mCurrentTouchPos1.y - this.mCurrentTouchPos0.y);
        if (VectorCalculator.isSquare(this.mCurrentAxisVec, this.mTouchVec0) && VectorCalculator.isSquare(this.mCurrentAxisVec, this.mTouchVec1)) {
            final float radianFrom2Vector = VectorCalculator.getRadianFrom2Vector(this.mPreviousAxisVec, this.mCurrentAxisVec);
            float n;
            if (0.0f <= this.mPreviousAxisVec.x * this.mCurrentAxisVec.y - this.mCurrentAxisVec.x * this.mPreviousAxisVec.y) {
                n = 1.0f;
            }
            else {
                n = -1.0f;
            }
            final float n2 = (float)(radianFrom2Vector * 360.0f / 2.0f / 3.141592653589793 * n);
            final float mAxisRotateDeg = this.mAxisRotateDeg;
            this.mAxisRotateDeg += n2;
            if (1.0f <= Math.abs(this.mAxisRotateDeg - mAxisRotateDeg)) {
                this.mListener.onDoubleTouchRotateDetected(this.mAxisRotateDeg, this.mAxisRotateDeg - mAxisRotateDeg);
            }
        }
        if (VectorCalculator.isParallel(this.mCurrentAxisVec, this.mTouchVec0) && VectorCalculator.isParallel(this.mCurrentAxisVec, this.mTouchVec1)) {
            this.mListener.onDoubleTouchScaleDetected(this.mCurrentAxisVec.length(), this.mPreviousAxisVec.length(), this.mOriginalAxisLen);
        }
        this.mPreviousAxisVec.set(this.mCurrentAxisVec);
        this.mPreviousTouchPos0.set(this.mCurrentTouchPos0);
        this.mPreviousTouchPos1.set(this.mCurrentTouchPos1);
    }
    
    public interface ScaleAndRotateDetectorListener
    {
        void onDoubleTouchRotateDetected(final float p0, final float p1);
        
        void onDoubleTouchScaleDetected(final float p0, final float p1, final float p2);
    }
}
