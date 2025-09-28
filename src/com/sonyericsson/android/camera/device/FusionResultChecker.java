package com.sonyericsson.android.camera.device;

import android.os.Handler;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class FusionResultChecker extends CaptureResultCheckerBase {
    private static final String TAG = "FusionResultChecker";
    private CameraParameters.FusionResultCallback mCallback;
    private CameraParameters.FusionResult mLatestResult;

    private boolean isInvalidFusionResult(int i, int i2, int i3) {
        boolean z = i3 == 0;
        boolean z2 = i == 2 || i == 3;
        boolean z3 = i2 == 0;
        if (z) {
            return z2 || !z3;
        }
        return false;
    }

    public FusionResultChecker(Handler handler, CameraParameters.FusionResultCallback fusionResultCallback) {
        super(handler);
        this.mCallback = fusionResultCallback;
        this.mLatestResult = new CameraParameters.FusionResult();
    }

    @Override // com.sonyericsson.android.camera.device.CaptureResultCheckerBase
    public void check(CaptureResultHolder captureResultHolder) {
        CameraParameters.FusionStatus fusionStatus;
        CameraParameters.FusionCondition fusionCondition;
        Integer num = (Integer) captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_CONTROL_PREVIEW_OUTPUT_STREAM_SOURCE);
        if (num == null) {
            num = 0;
        }
        Integer num2 = (Integer) captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_CONTROL_FUSION_CONDITION);
        if (num2 == null) {
            num2 = 0;
        }
        Integer num3 = (Integer) captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_CONTROL_FUSION_MODE);
        if (num3 == null) {
            num3 = 0;
        }
        if (isInvalidFusionResult(num.intValue(), num2.intValue(), num3.intValue())) {
            if (CamLog.VERBOSE) {
                CamLog.w("received mismatch fusion result. fusionMode=" + num3 + ", source=" + num + ", fusionCondition=" + num2);
                return;
            }
            return;
        }
        switch (num.intValue()) {
            case 1:
                fusionStatus = CameraParameters.FusionStatus.SUB_1;
                break;
            case 2:
                fusionStatus = CameraParameters.FusionStatus.FUSION_MAIN;
                break;
            case 3:
                fusionStatus = CameraParameters.FusionStatus.FUSION_SUB_1;
                break;
            default:
                fusionStatus = CameraParameters.FusionStatus.MAIN;
                break;
        }
        switch (num2.intValue()) {
            case 1:
                fusionCondition = CameraParameters.FusionCondition.CLOSE_TO_SUBJECT;
                break;
            case 2:
                fusionCondition = CameraParameters.FusionCondition.LENS_COVERED;
                break;
            case 3:
                fusionCondition = CameraParameters.FusionCondition.LOW_CONTRAST;
                break;
            default:
                fusionCondition = CameraParameters.FusionCondition.NORMAL;
                break;
        }
        final CameraParameters.FusionResult fusionResult = new CameraParameters.FusionResult(fusionStatus, fusionCondition);
        if (fusionResult.getFusionStatus() == this.mLatestResult.getFusionStatus() && fusionResult.getFusionCondition() == this.mLatestResult.getFusionCondition()) {
            return;
        }
        this.mLatestResult = fusionResult;
        this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.FusionResultChecker.1
            @Override // java.lang.Runnable
            public void run() {
                if (FusionResultChecker.this.mCallback != null) {
                    FusionResultChecker.this.mCallback.onFusionResultChanged(fusionResult);
                }
            }
        });
    }

    public CameraParameters.FusionResult getLatestFusionResult() {
        return this.mLatestResult;
    }
}
