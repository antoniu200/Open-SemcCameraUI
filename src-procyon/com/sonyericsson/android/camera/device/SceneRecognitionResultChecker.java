// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import android.hardware.camera2.CaptureResult$Key;
import android.hardware.camera2.CaptureResult;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import android.os.Handler;

class SceneRecognitionResultChecker extends CaptureResultCheckerBase
{
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
    
    public SceneRecognitionResultChecker(final Handler handler, final CameraParameters.SceneRecognitionCallback mSceneRecognitionCallback, final CameraInfo.CameraId mCameraId) {
        super(handler);
        this.mScene = null;
        this.mCondition = null;
        this.mMacroRange = false;
        this.mCameraId = CameraInfo.CameraId.BACK;
        this.mLastScene = 100;
        this.mLastCondition = 0;
        this.mLastMacroRange = false;
        this.mSceneRecognitionCallback = mSceneRecognitionCallback;
        this.mCameraId = mCameraId;
    }
    
    private boolean isMacroDetectionSupported() {
        return this.mCameraId != CameraInfo.CameraId.FRONT && PlatformCapability.isFocusSupported(this.mCameraId);
    }
    
    @Override
    public void check(final CaptureResultHolder captureResultHolder) {
        this.mScene = captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_SCENE);
        if (this.mScene == null) {
            this.mScene = 100;
            if (CamLog.VERBOSE) {
                CamLog.d("Scene is set as AUTO since not detected.");
            }
        }
        this.mCondition = captureResultHolder.getLatestValue(SomcCaptureResultKeys.SONYMOBILE_STATISTICS_CONDITION);
        if (this.mCondition == null) {
            this.mCondition = 0;
            if (CamLog.VERBOSE) {
                CamLog.d("Condition is set as UNKNOWN since not detected.");
            }
        }
        final float v = 1.0f / captureResultHolder.getLatestValue((android.hardware.camera2.CaptureResult$Key<Float>)CaptureResult.LENS_FOCUS_DISTANCE);
        if (!this.isMacroDetectionSupported()) {
            this.mMacroRange = false;
        }
        else if (Float.isInfinite(v)) {
            this.mMacroRange = false;
        }
        else if (v <= 0.1455f) {
            this.mMacroRange = true;
        }
        else {
            this.mMacroRange = false;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Scene: ");
            sb.append(this.mScene);
            sb.append(", Condition: ");
            sb.append(this.mCondition);
            sb.append(", Macro: ");
            sb.append(this.mMacroRange);
            CamLog.d(sb.toString());
        }
        if (this.mScene == (int)this.mLastScene && this.mCondition == (int)this.mLastCondition && this.mMacroRange == this.mLastMacroRange) {
            if (CamLog.VERBOSE) {
                CamLog.d("Same Scene/Condition/Macro status.");
            }
            return;
        }
        final CameraParameters.SceneRecognitionResult sceneRecognitionResult = new CameraParameters.SceneRecognitionResult();
        sceneRecognitionResult.sceneMode = CameraParameterConverter.SceneMode.getSceneMode(this.mScene);
        sceneRecognitionResult.deviceStabilityCondition = CameraParameters.DeviceStabilityCondition.getCondition(this.mCondition);
        sceneRecognitionResult.isMacroRange = this.mMacroRange;
        this.mHandler.post((Runnable)new Runnable(this, sceneRecognitionResult) {
            final SceneRecognitionResultChecker this$0;
            final CameraParameters.SceneRecognitionResult val$sceneRecognitionResult;
            
            @Override
            public void run() {
                if (this.this$0.mSceneRecognitionCallback != null) {
                    this.this$0.mSceneRecognitionCallback.onSceneModeChanged(this.val$sceneRecognitionResult);
                }
            }
        });
        this.mLastScene = this.mScene;
        this.mLastCondition = this.mCondition;
        this.mLastMacroRange = this.mMacroRange;
    }
}
