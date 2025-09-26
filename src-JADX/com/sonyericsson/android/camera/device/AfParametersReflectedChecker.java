package com.sonyericsson.android.camera.device;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Handler;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class AfParametersReflectedChecker extends CaptureResultCheckerBase {
    private static final String TAG = "AfParametersReflectedChecker";
    private static final boolean TRACE = false;
    private CameraParameters.AfParametersCallback mCallback;
    private CaptureRequestHolder mReqHolder;

    private static void trace(String str) {
        CamLog.d(str);
    }

    public AfParametersReflectedChecker(Handler handler, CameraParameters.AfParametersCallback afParametersCallback, CaptureRequestHolder captureRequestHolder) {
        super(handler);
        this.mCallback = afParametersCallback;
        this.mReqHolder = captureRequestHolder;
    }

    @Override // com.sonyericsson.android.camera.device.CaptureResultCheckerBase
    public void check(CaptureResultHolder captureResultHolder) {
        if (checkSync(captureResultHolder.getLatest())) {
            this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.AfParametersReflectedChecker.1
                @Override // java.lang.Runnable
                public void run() {
                    if (AfParametersReflectedChecker.this.mCallback != null) {
                        AfParametersReflectedChecker.this.mCallback.onReflected(AfParametersReflectedChecker.this);
                    }
                }
            });
        }
    }

    public boolean checkSync(CaptureResult captureResult) {
        int iIntValue = ((Integer) this.mReqHolder.get(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AF_REGION_MODE)).intValue();
        int iIntValue2 = ((Integer) this.mReqHolder.get(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_MODE)).intValue();
        int iIntValue3 = ((Integer) this.mReqHolder.get(CaptureRequest.FLASH_MODE)).intValue();
        int iIntValue4 = ((Integer) captureResult.get(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AF_REGION_MODE)).intValue();
        if (iIntValue == iIntValue4) {
            return ((iIntValue4 == 0) || ((MeteringRectangle[]) this.mReqHolder.get(CaptureRequest.CONTROL_AF_REGIONS))[0].getRect().equals(((MeteringRectangle[]) captureResult.get(TotalCaptureResult.CONTROL_AF_REGIONS))[0].getRect())) && iIntValue2 == ((Integer) captureResult.get(SomcCaptureResultKeys.SONYMOBILE_CONTROL_AE_MODE)).intValue() && iIntValue3 == ((Integer) captureResult.get(TotalCaptureResult.FLASH_MODE)).intValue();
        }
        return false;
    }
}
