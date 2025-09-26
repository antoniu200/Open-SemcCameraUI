// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.Handler;
import android.graphics.Rect;

class ObjectTrackingResultChecker extends CaptureResultCheckerBase
{
    public static final int LOW_PASS_FILTER_STRENGTH = 0;
    public static final int MINIMUM_INTERVAL_MILLIS = 100;
    private static final String TAG = "ObjectTrackingResultChecker";
    private final CameraParameters.ObjectTrackingCallback mObjectTrackingCallback;
    private Rect mPreviousObjectSelectArea;
    private long mPreviousObjectSelectTime;
    private boolean mStart;
    
    public ObjectTrackingResultChecker(final Handler handler, final CameraParameters.ObjectTrackingCallback mObjectTrackingCallback) {
        super(handler);
        this.mPreviousObjectSelectArea = null;
        this.mPreviousObjectSelectTime = 0L;
        this.mStart = false;
        this.mObjectTrackingCallback = mObjectTrackingCallback;
    }
    
    private Rect getLowPassFilteredArea(final Rect mPreviousObjectSelectArea, final boolean b) {
        if (this.mPreviousObjectSelectArea == null) {
            this.mPreviousObjectSelectArea = mPreviousObjectSelectArea;
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("starting: left=");
                sb.append(this.mPreviousObjectSelectArea.left);
                sb.append(", top=");
                sb.append(this.mPreviousObjectSelectArea.top);
                sb.append(", right=");
                sb.append(this.mPreviousObjectSelectArea.right);
                sb.append(", bottom=");
                sb.append(this.mPreviousObjectSelectArea.bottom);
                CamLog.d(sb.toString());
            }
            return this.mPreviousObjectSelectArea;
        }
        Rect mPreviousObjectSelectArea2;
        if (!b) {
            if (mPreviousObjectSelectArea.left != 0 || mPreviousObjectSelectArea.top != 0 || mPreviousObjectSelectArea.right != 0 || mPreviousObjectSelectArea.bottom != 0) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("before filtering: left=");
                    sb2.append(mPreviousObjectSelectArea.left);
                    sb2.append(", top=");
                    sb2.append(mPreviousObjectSelectArea.top);
                    sb2.append(", right=");
                    sb2.append(mPreviousObjectSelectArea.right);
                    sb2.append(", bottom=");
                    sb2.append(mPreviousObjectSelectArea.bottom);
                    CamLog.d(sb2.toString());
                }
                mPreviousObjectSelectArea.left = this.getLowPassFilteredValue(mPreviousObjectSelectArea.left, this.mPreviousObjectSelectArea.left);
                mPreviousObjectSelectArea.top = this.getLowPassFilteredValue(mPreviousObjectSelectArea.top, this.mPreviousObjectSelectArea.top);
                mPreviousObjectSelectArea.right = this.getLowPassFilteredValue(mPreviousObjectSelectArea.right, this.mPreviousObjectSelectArea.right);
                mPreviousObjectSelectArea.bottom = this.getLowPassFilteredValue(mPreviousObjectSelectArea.bottom, this.mPreviousObjectSelectArea.bottom);
            }
            mPreviousObjectSelectArea2 = mPreviousObjectSelectArea;
            if (CamLog.VERBOSE) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("after filtering: left=");
                sb3.append(mPreviousObjectSelectArea.left);
                sb3.append(", top=");
                sb3.append(mPreviousObjectSelectArea.top);
                sb3.append(", right=");
                sb3.append(mPreviousObjectSelectArea.right);
                sb3.append(", bottom=");
                sb3.append(mPreviousObjectSelectArea.bottom);
                CamLog.d(sb3.toString());
                mPreviousObjectSelectArea2 = mPreviousObjectSelectArea;
            }
        }
        else {
            mPreviousObjectSelectArea2 = this.mPreviousObjectSelectArea;
        }
        return this.mPreviousObjectSelectArea = mPreviousObjectSelectArea2;
    }
    
    private int getLowPassFilteredValue(final int n, final int n2) {
        return (int)(n * 1.0f + n2 * 0.0f);
    }
    
    @Override
    public void check(final CaptureResultHolder captureResultHolder) {
        final boolean mStart = this.mStart;
        boolean b = true;
        if (!mStart) {
            if (captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER) != 1) {
                return;
            }
            this.mStart = true;
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - this.mPreviousObjectSelectTime < 100L) {
            return;
        }
        this.mPreviousObjectSelectTime = uptimeMillis;
        final int[] array = captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_AREA);
        Rect rect;
        if (array != null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Object select area: left=");
                sb.append(array[0]);
                sb.append(", top=");
                sb.append(array[1]);
                sb.append(", right=");
                sb.append(array[2]);
                sb.append(", bottom=");
                sb.append(array[3]);
                sb.append(", lost=");
                sb.append(array[4]);
                CamLog.d(sb.toString());
            }
            rect = new Rect(array[0], array[1], array[2], array[3]);
            if (array[4] != 1) {
                b = false;
            }
        }
        else {
            if (CamLog.VERBOSE) {
                CamLog.d("Object select area: none.");
            }
            rect = new Rect();
        }
        final Rect lowPassFilteredArea = this.getLowPassFilteredArea(rect, b);
        if (lowPassFilteredArea != null) {
            this.mHandler.post((Runnable)new Runnable(this, new CameraParameters.ObjectTrackingResult(lowPassFilteredArea, b)) {
                final ObjectTrackingResultChecker this$0;
                final CameraParameters.ObjectTrackingResult val$objectTrackingResult;
                
                @Override
                public void run() {
                    if (this.this$0.mObjectTrackingCallback != null) {
                        this.this$0.mObjectTrackingCallback.onObjectTracked(this.val$objectTrackingResult);
                    }
                }
            });
        }
    }
}
