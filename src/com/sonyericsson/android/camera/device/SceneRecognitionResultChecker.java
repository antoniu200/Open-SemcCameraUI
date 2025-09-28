package com.sonyericsson.android.camera.device;

import android.hardware.camera2.CaptureResult;
import android.os.Handler;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class SceneRecognitionResultChecker extends CaptureResultCheckerBase {
    private static final float MACRO_RANGE_IN_METER = 0.1455f;
    private static final String TAG = "SceneRecognitionResultChecker";
    private CameraInfo.CameraId mCameraId;
    private Integer mCondition;
    private Integer mLastCondition;
    private boolean mLastMacroRange;
    private Integer mLastScene;
    private boolean mMacroRange;
    private Integer mScene;
    private final CameraParameters.SceneRecognitionCallback mSceneRecognitionCallback;

    public SceneRecognitionResultChecker(Handler handler, CameraParameters.SceneRecognitionCallback sceneRecognitionCallback, CameraInfo.CameraId cameraId) {
        super(handler);
        this.mScene = null;
        this.mCondition = null;
        this.mMacroRange = false;
        this.mCameraId = CameraInfo.CameraId.BACK;
        this.mLastScene = 100;
        this.mLastCondition = 0;
        this.mLastMacroRange = false;
        this.mSceneRecognitionCallback = sceneRecognitionCallback;
        this.mCameraId = cameraId;
    }

    @Override // com.sonyericsson.android.camera.device.CaptureResultCheckerBase
    public void check(CaptureResultHolder captureResultHolder) {
        this.mScene = (Integer) captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_SCENE);
        if (this.mScene == null) {
            this.mScene = 100;
            if (CamLog.VERBOSE) {
                CamLog.d("Scene is set as AUTO since not detected.");
            }
        }
        this.mCondition = (Integer) captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_CONDITION);
        if (this.mCondition == null) {
            this.mCondition = 0;
            if (CamLog.VERBOSE) {
                CamLog.d("Condition is set as UNKNOWN since not detected.");
            }
        }
        float fFloatValue = 1.0f / ((Float) captureResultHolder.getLatestValue(CaptureResult.LENS_FOCUS_DISTANCE)).floatValue();
        if (isMacroDetectionSupported() && !Float.isInfinite(fFloatValue) && fFloatValue <= MACRO_RANGE_IN_METER) {
            this.mMacroRange = true;
        } else {
            this.mMacroRange = false;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Scene: " + this.mScene + ", Condition: " + this.mCondition + ", Macro: " + this.mMacroRange);
        }
        if (this.mScene.intValue() == this.mLastScene.intValue() && this.mCondition.intValue() == this.mLastCondition.intValue() && this.mMacroRange == this.mLastMacroRange) {
            if (CamLog.VERBOSE) {
                CamLog.d("Same Scene/Condition/Macro status.");
                return;
            }
            return;
        }
        final CameraParameters.SceneRecognitionResult sceneRecognitionResult = new CameraParameters.SceneRecognitionResult();
        sceneRecognitionResult.sceneMode = CameraParameterConverter.SceneMode.getSceneMode(this.mScene.intValue());
        sceneRecognitionResult.deviceStabilityCondition = CameraParameters.DeviceStabilityCondition.getCondition(this.mCondition.intValue());
        sceneRecognitionResult.isMacroRange = this.mMacroRange;
        this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.device.SceneRecognitionResultChecker.1
            @Override // java.lang.Runnable
            public void run() {
                if (SceneRecognitionResultChecker.this.mSceneRecognitionCallback != null) {
                    SceneRecognitionResultChecker.this.mSceneRecognitionCallback.onSceneModeChanged(sceneRecognitionResult);
                }
            }
        });
        this.mLastScene = this.mScene;
        this.mLastCondition = this.mCondition;
        this.mLastMacroRange = this.mMacroRange;
    }

    private boolean isMacroDetectionSupported() {
        if (this.mCameraId == CameraInfo.CameraId.FRONT) {
            return false;
        }
        return PlatformCapability.isFocusSupported(this.mCameraId);
    }
}
