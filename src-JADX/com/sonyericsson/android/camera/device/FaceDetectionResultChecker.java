package com.sonyericsson.android.camera.device;

import android.graphics.Rect;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.params.Face;
import android.os.Handler;
import android.os.SystemClock;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class FaceDetectionResultChecker extends CaptureResultCheckerBase {
    private static final int MINIMUM_INTERVAL_MILLIS = 100;
    private static final String TAG = "FaceDetectionResultChecker";
    private final CameraParameters.FaceDetectionCallback mFaceDetectionCallback;
    private long mLastDataTimeMillis;
    private int mPreviousNumberOfFacesDetected;

    public FaceDetectionResultChecker(Handler handler, CameraParameters.FaceDetectionCallback faceDetectionCallback) {
        super(handler);
        this.mLastDataTimeMillis = 0L;
        this.mPreviousNumberOfFacesDetected = 0;
        this.mFaceDetectionCallback = faceDetectionCallback;
    }

    @Override // com.sonyericsson.android.camera.device.CaptureResultCheckerBase
    public void check(CaptureResultHolder captureResultHolder) {
        int i;
        char c;
        int i2;
        int i3;
        long jUptimeMillis = SystemClock.uptimeMillis();
        if (jUptimeMillis - this.mLastDataTimeMillis < 100) {
            return;
        }
        this.mLastDataTimeMillis = jUptimeMillis;
        Face[] faceArr = (Face[]) captureResultHolder.getLatestValue(CaptureResult.STATISTICS_FACES);
        int[] iArr = (int[]) captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES);
        int[] iArr2 = (int[]) captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_FACE_SELECT_AREA);
        final CameraParameters.FaceDetectionResult faceDetectionResult = new CameraParameters.FaceDetectionResult();
        if (isValidResults(faceArr, iArr, iArr2)) {
            char c2 = 3;
            if (iArr2 != null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Select area: " + iArr2[0] + ", " + iArr2[1] + ", " + iArr2[2] + ", " + iArr2[3] + ", " + iArr2[4]);
                }
                i = iArr2[4];
            } else {
                i = 0;
            }
            int i4 = 0;
            int i5 = 0;
            while (i5 < faceArr.length) {
                if (isValidFace(faceArr[i5])) {
                    Rect bounds = faceArr[i5].getBounds();
                    int i6 = iArr[i5];
                    if (CamLog.VERBOSE) {
                        CamLog.d("Face rectangle: " + bounds.toShortString());
                        CamLog.d("Smile score: " + i6);
                    }
                    int i7 = i4;
                    i2 = i5;
                    faceDetectionResult.addFaceResult(i4, bounds.left, bounds.top, bounds.right, bounds.bottom, i6);
                    if (i != 0) {
                        c = 3;
                        if (bounds.contains(iArr2[0], iArr2[1], iArr2[2], iArr2[3])) {
                            i3 = i7;
                            faceDetectionResult.setFrameResult(i3);
                        } else {
                            i3 = i7;
                        }
                    } else {
                        i3 = i7;
                        c = 3;
                    }
                    i4 = i3 + 1;
                } else {
                    c = c2;
                    i2 = i5;
                }
                i5 = i2 + 1;
                c2 = c;
            }
        }
        if (faceDetectionResult.extFaceList.size() == 0) {
            if (this.mPreviousNumberOfFacesDetected == 0) {
                return;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("Faces are lost.");
            }
            this.mPreviousNumberOfFacesDetected = 0;
        } else {
            this.mPreviousNumberOfFacesDetected = faceDetectionResult.extFaceList.size();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Detected Faces: " + faceDetectionResult);
        }
        this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.FaceDetectionResultChecker.1
            @Override // java.lang.Runnable
            public void run() {
                if (FaceDetectionResultChecker.this.mFaceDetectionCallback != null) {
                    FaceDetectionResultChecker.this.mFaceDetectionCallback.onFaceDetection(faceDetectionResult);
                }
            }
        });
    }

    private boolean isValidResults(Face[] faceArr, int[] iArr, int[] iArr2) {
        if (faceArr == null || faceArr.length == 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("No face data.");
            }
            return false;
        }
        if (iArr == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("Smile score is null.");
            }
            return false;
        }
        if (faceArr.length != iArr.length) {
            if (CamLog.VERBOSE) {
                CamLog.d("Result is invalid: Number of face rectangle and smile score are different. faces num: " + faceArr.length + ", smile scores num: " + iArr.length);
            }
            return false;
        }
        if (iArr2 == null || iArr2.length == 5) {
            return true;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Face select area is invalid: Face select area num: " + iArr2.length);
        }
        return false;
    }

    private boolean isValidFace(Face face) {
        return face != null && face.getBounds().width() > 0 && face.getBounds().height() > 0;
    }
}
