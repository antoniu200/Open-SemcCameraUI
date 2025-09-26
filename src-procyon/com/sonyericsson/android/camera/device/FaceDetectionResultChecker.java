// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import android.graphics.Rect;
import android.hardware.camera2.CaptureResult$Key;
import android.hardware.camera2.CaptureResult;
import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CamLog;
import android.hardware.camera2.params.Face;
import android.os.Handler;

class FaceDetectionResultChecker extends CaptureResultCheckerBase
{
    private static final int MINIMUM_INTERVAL_MILLIS = 100;
    private static final String TAG = "FaceDetectionResultChecker";
    private final CameraParameters.FaceDetectionCallback mFaceDetectionCallback;
    private long mLastDataTimeMillis;
    private int mPreviousNumberOfFacesDetected;
    
    public FaceDetectionResultChecker(final Handler handler, final CameraParameters.FaceDetectionCallback mFaceDetectionCallback) {
        super(handler);
        this.mLastDataTimeMillis = 0L;
        this.mPreviousNumberOfFacesDetected = 0;
        this.mFaceDetectionCallback = mFaceDetectionCallback;
    }
    
    private boolean isValidFace(final Face face) {
        return face != null && face.getBounds().width() > 0 && face.getBounds().height() > 0;
    }
    
    private boolean isValidResults(final Face[] array, final int[] array2, final int[] array3) {
        if (array == null || array.length == 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("No face data.");
            }
            return false;
        }
        if (array2 == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("Smile score is null.");
            }
            return false;
        }
        if (array.length != array2.length) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Result is invalid: Number of face rectangle and smile score are different. faces num: ");
                sb.append(array.length);
                sb.append(", smile scores num: ");
                sb.append(array2.length);
                CamLog.d(sb.toString());
            }
            return false;
        }
        if (array3 != null && array3.length != 5) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Face select area is invalid: Face select area num: ");
                sb2.append(array3.length);
                CamLog.d(sb2.toString());
            }
            return false;
        }
        return true;
    }
    
    @Override
    public void check(final CaptureResultHolder captureResultHolder) {
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - this.mLastDataTimeMillis < 100L) {
            return;
        }
        this.mLastDataTimeMillis = uptimeMillis;
        final Face[] array = captureResultHolder.getLatestValue((android.hardware.camera2.CaptureResult$Key<Face[]>)CaptureResult.STATISTICS_FACES);
        final int[] array2 = captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES);
        final int[] array3 = captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SELECT_AREA);
        final CameraParameters.FaceDetectionResult obj = new CameraParameters.FaceDetectionResult();
        if (this.isValidResults(array, array2, array3)) {
            int n;
            if (array3 != null) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Select area: ");
                    sb.append(array3[0]);
                    sb.append(", ");
                    sb.append(array3[1]);
                    sb.append(", ");
                    sb.append(array3[2]);
                    sb.append(", ");
                    sb.append(array3[3]);
                    sb.append(", ");
                    sb.append(array3[4]);
                    CamLog.d(sb.toString());
                }
                n = array3[4];
            }
            else {
                n = 0;
            }
            int frameResult = 0;
            for (int i = 0; i < array.length; ++i) {
                if (this.isValidFace(array[i])) {
                    final Rect bounds = array[i].getBounds();
                    final int j = array2[i];
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Face rectangle: ");
                        sb2.append(bounds.toShortString());
                        CamLog.d(sb2.toString());
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Smile score: ");
                        sb3.append(j);
                        CamLog.d(sb3.toString());
                    }
                    obj.addFaceResult(frameResult, bounds.left, bounds.top, bounds.right, bounds.bottom, j);
                    if (n != 0) {
                        if (bounds.contains(array3[0], array3[1], array3[2], array3[3])) {
                            obj.setFrameResult(frameResult);
                        }
                    }
                    ++frameResult;
                }
            }
        }
        if (obj.extFaceList.size() == 0) {
            if (this.mPreviousNumberOfFacesDetected == 0) {
                return;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("Faces are lost.");
            }
            this.mPreviousNumberOfFacesDetected = 0;
        }
        else {
            this.mPreviousNumberOfFacesDetected = obj.extFaceList.size();
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Detected Faces: ");
            sb4.append(obj);
            CamLog.d(sb4.toString());
        }
        this.mHandler.post((Runnable)new Runnable(this, obj) {
            final FaceDetectionResultChecker this$0;
            final CameraParameters.FaceDetectionResult val$faceDetectionResult;
            
            @Override
            public void run() {
                if (this.this$0.mFaceDetectionCallback != null) {
                    this.this$0.mFaceDetectionCallback.onFaceDetection(this.val$faceDetectionResult);
                }
            }
        });
    }
}
