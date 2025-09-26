// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.CaptureResult$Key;
import android.hardware.camera2.CaptureRequest$Key;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.Handler;

class AfParametersReflectedChecker extends CaptureResultCheckerBase
{
    private static final String TAG = "AfParametersReflectedChecker";
    private static final boolean TRACE = false;
    private CameraParameters.AfParametersCallback mCallback;
    private CaptureRequestHolder mReqHolder;
    
    public AfParametersReflectedChecker(final Handler handler, final CameraParameters.AfParametersCallback mCallback, final CaptureRequestHolder mReqHolder) {
        super(handler);
        this.mCallback = mCallback;
        this.mReqHolder = mReqHolder;
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    @Override
    public void check(final CaptureResultHolder captureResultHolder) {
        if (!this.checkSync(captureResultHolder.getLatest())) {
            return;
        }
        this.mHandler.post((Runnable)new Runnable(this) {
            final AfParametersReflectedChecker this$0;
            
            @Override
            public void run() {
                if (this.this$0.mCallback != null) {
                    this.this$0.mCallback.onReflected(this.this$0);
                }
            }
        });
    }
    
    public boolean checkSync(final CaptureResult captureResult) {
        final int intValue = this.mReqHolder.get(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AF_REGION_MODE);
        final int intValue2 = this.mReqHolder.get(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_MODE);
        final int intValue3 = this.mReqHolder.get((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.FLASH_MODE);
        final int intValue4 = (int)captureResult.get((CaptureResult$Key)SomcCaptureResultKeys.SONYMOBILE_CONTROL_AF_REGION_MODE);
        return intValue == intValue4 && (intValue4 == 0 || ((MeteringRectangle[])this.mReqHolder.get((android.hardware.camera2.CaptureRequest$Key<Object>)CaptureRequest.CONTROL_AF_REGIONS))[0].getRect().equals((Object)((MeteringRectangle[])captureResult.get(TotalCaptureResult.CONTROL_AF_REGIONS))[0].getRect())) && intValue2 == (int)captureResult.get((CaptureResult$Key)SomcCaptureResultKeys.SONYMOBILE_CONTROL_AE_MODE) && intValue3 == (int)captureResult.get(TotalCaptureResult.FLASH_MODE);
    }
}
