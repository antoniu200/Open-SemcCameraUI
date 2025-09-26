// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import com.sonyericsson.android.camera.util.CamLog;
import android.os.Handler;

class FusionResultChecker extends CaptureResultCheckerBase
{
    private static final String TAG = "FusionResultChecker";
    private CameraParameters.FusionResultCallback mCallback;
    private CameraParameters.FusionResult mLatestResult;
    
    public FusionResultChecker(final Handler handler, final CameraParameters.FusionResultCallback mCallback) {
        super(handler);
        this.mCallback = mCallback;
        this.mLatestResult = new CameraParameters.FusionResult();
    }
    
    private boolean isInvalidFusionResult(int n, int n2, int n3) {
        final boolean b = false;
        if (n3 == 0) {
            n3 = 1;
        }
        else {
            n3 = 0;
        }
        if (n != 2 && n != 3) {
            n = 0;
        }
        else {
            n = 1;
        }
        if (n2 == 0) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        boolean b2 = b;
        if (n3 != 0) {
            if (n == 0) {
                b2 = b;
                if (n2 != 0) {
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    @Override
    public void check(final CaptureResultHolder captureResultHolder) {
        Integer value;
        if ((value = captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_CONTROL_PREVIEW_OUTPUT_STREAM_SOURCE)) == null) {
            value = 0;
        }
        Integer value2;
        if ((value2 = captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_CONTROL_FUSION_CONDITION)) == null) {
            value2 = 0;
        }
        Integer value3;
        if ((value3 = captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_CONTROL_FUSION_MODE)) == null) {
            value3 = 0;
        }
        if (this.isInvalidFusionResult(value, value2, value3)) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("received mismatch fusion result. fusionMode=");
                sb.append(value3);
                sb.append(", source=");
                sb.append(value);
                sb.append(", fusionCondition=");
                sb.append(value2);
                CamLog.w(sb.toString());
            }
            return;
        }
        CameraParameters.FusionStatus fusionStatus = null;
        switch (value) {
            default: {
                fusionStatus = CameraParameters.FusionStatus.MAIN;
                break;
            }
            case 3: {
                fusionStatus = CameraParameters.FusionStatus.FUSION_SUB_1;
                break;
            }
            case 2: {
                fusionStatus = CameraParameters.FusionStatus.FUSION_MAIN;
                break;
            }
            case 1: {
                fusionStatus = CameraParameters.FusionStatus.SUB_1;
                break;
            }
        }
        CameraParameters.FusionCondition fusionCondition = null;
        switch (value2) {
            default: {
                fusionCondition = CameraParameters.FusionCondition.NORMAL;
                break;
            }
            case 3: {
                fusionCondition = CameraParameters.FusionCondition.LOW_CONTRAST;
                break;
            }
            case 2: {
                fusionCondition = CameraParameters.FusionCondition.LENS_COVERED;
                break;
            }
            case 1: {
                fusionCondition = CameraParameters.FusionCondition.CLOSE_TO_SUBJECT;
                break;
            }
        }
        final CameraParameters.FusionResult mLatestResult = new CameraParameters.FusionResult(fusionStatus, fusionCondition);
        if (mLatestResult.getFusionStatus() != this.mLatestResult.getFusionStatus() || mLatestResult.getFusionCondition() != this.mLatestResult.getFusionCondition()) {
            this.mLatestResult = mLatestResult;
            this.mHandler.post((Runnable)new Runnable(this, mLatestResult) {
                final FusionResultChecker this$0;
                final CameraParameters.FusionResult val$result;
                
                @Override
                public void run() {
                    if (this.this$0.mCallback != null) {
                        this.this$0.mCallback.onFusionResultChanged(this.val$result);
                    }
                }
            });
        }
    }
    
    public CameraParameters.FusionResult getLatestFusionResult() {
        return this.mLatestResult;
    }
}
