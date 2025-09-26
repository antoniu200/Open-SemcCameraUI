package com.sonyericsson.android.camera.device;

import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ObjectTrackingResultChecker extends CaptureResultCheckerBase {
    public static final int LOW_PASS_FILTER_STRENGTH = 0;
    public static final int MINIMUM_INTERVAL_MILLIS = 100;
    private static final String TAG = "ObjectTrackingResultChecker";
    private final CameraParameters.ObjectTrackingCallback mObjectTrackingCallback;
    private Rect mPreviousObjectSelectArea;
    private long mPreviousObjectSelectTime;
    private boolean mStart;

    private int getLowPassFilteredValue(int i, int i2) {
        return (int) ((i * 1.0f) + (i2 * 0.0f));
    }

    public ObjectTrackingResultChecker(Handler handler, CameraParameters.ObjectTrackingCallback objectTrackingCallback) {
        super(handler);
        this.mPreviousObjectSelectArea = null;
        this.mPreviousObjectSelectTime = 0L;
        this.mStart = false;
        this.mObjectTrackingCallback = objectTrackingCallback;
    }

    @Override // com.sonyericsson.android.camera.device.CaptureResultCheckerBase
    public void check(CaptureResultHolder captureResultHolder) {
        Rect rect;
        boolean z = true;
        if (!this.mStart) {
            if (((Integer) captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER)).intValue() != 1) {
                return;
            } else {
                this.mStart = true;
            }
        }
        long jUptimeMillis = SystemClock.uptimeMillis();
        if (jUptimeMillis - this.mPreviousObjectSelectTime < 100) {
            return;
        }
        this.mPreviousObjectSelectTime = jUptimeMillis;
        int[] iArr = (int[]) captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_AREA);
        if (iArr != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("Object select area: left=" + iArr[0] + ", top=" + iArr[1] + ", right=" + iArr[2] + ", bottom=" + iArr[3] + ", lost=" + iArr[4]);
            }
            rect = new Rect(iArr[0], iArr[1], iArr[2], iArr[3]);
            if (iArr[4] != 1) {
                z = false;
            }
        } else {
            if (CamLog.VERBOSE) {
                CamLog.d("Object select area: none.");
            }
            rect = new Rect();
        }
        Rect lowPassFilteredArea = getLowPassFilteredArea(rect, z);
        if (lowPassFilteredArea != null) {
            final CameraParameters.ObjectTrackingResult objectTrackingResult = new CameraParameters.ObjectTrackingResult(lowPassFilteredArea, z);
            this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.ObjectTrackingResultChecker.1
                @Override // java.lang.Runnable
                public void run() {
                    if (ObjectTrackingResultChecker.this.mObjectTrackingCallback != null) {
                        ObjectTrackingResultChecker.this.mObjectTrackingCallback.onObjectTracked(objectTrackingResult);
                    }
                }
            });
        }
    }

    private Rect getLowPassFilteredArea(Rect rect, boolean z) {
        if (this.mPreviousObjectSelectArea == null) {
            this.mPreviousObjectSelectArea = rect;
            if (CamLog.VERBOSE) {
                CamLog.d("starting: left=" + this.mPreviousObjectSelectArea.left + ", top=" + this.mPreviousObjectSelectArea.top + ", right=" + this.mPreviousObjectSelectArea.right + ", bottom=" + this.mPreviousObjectSelectArea.bottom);
            }
            return this.mPreviousObjectSelectArea;
        }
        if (!z) {
            if (rect.left != 0 || rect.top != 0 || rect.right != 0 || rect.bottom != 0) {
                if (CamLog.VERBOSE) {
                    CamLog.d("before filtering: left=" + rect.left + ", top=" + rect.top + ", right=" + rect.right + ", bottom=" + rect.bottom);
                }
                rect.left = getLowPassFilteredValue(rect.left, this.mPreviousObjectSelectArea.left);
                rect.top = getLowPassFilteredValue(rect.top, this.mPreviousObjectSelectArea.top);
                rect.right = getLowPassFilteredValue(rect.right, this.mPreviousObjectSelectArea.right);
                rect.bottom = getLowPassFilteredValue(rect.bottom, this.mPreviousObjectSelectArea.bottom);
            }
            if (CamLog.VERBOSE) {
                CamLog.d("after filtering: left=" + rect.left + ", top=" + rect.top + ", right=" + rect.right + ", bottom=" + rect.bottom);
            }
        } else {
            rect = this.mPreviousObjectSelectArea;
        }
        this.mPreviousObjectSelectArea = rect;
        return rect;
    }
}
