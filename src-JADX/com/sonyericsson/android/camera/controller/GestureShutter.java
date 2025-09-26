package com.sonyericsson.android.camera.controller;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.device.ImageRetriever;
import com.sonyericsson.android.camera.gestureshutter.HandSignsDetector;
import com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.GestureShutterView;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GestureShutter {
    private static final boolean IS_GESTURE_SHUTTER_SUPPORTED = !"".equalsIgnoreCase("noGesture");
    private static final String TAG = "GestureShutter";
    private ControllerHost mControllerHost;
    private WindowHost mWindowHost;
    private boolean mIsEnabled = true;
    private boolean mIsGestureShutterOn = false;
    private boolean mIsPreviewing = false;
    private CapturingMode mCapturingMode = CapturingMode.UNKNOWN;
    private boolean mIsSelftimerRunning = false;
    private CameraActivity.LayoutOrientation mLayoutOrientation = CameraActivity.LayoutOrientation.Unknown;
    private ImageRetriever mImageRetriever = null;
    private Handler mUIScheduler = new Handler();
    private HandSignsDetectorInterface mHandSignsDetector = null;
    private State mState = null;
    private GestureShutterView.AnimationStatusListener mAnimationListener = new GestureShutterView.AnimationStatusListener() { // from class: com.sonyericsson.android.camera.controller.GestureShutter.1
        @Override // com.sonyericsson.android.camera.view.GestureShutterView.AnimationStatusListener
        public void handleRewindFinished() {
            GestureShutter.this.mState.handleRewindFinished();
        }

        @Override // com.sonyericsson.android.camera.view.GestureShutterView.AnimationStatusListener
        public void handleProceedFinished() {
            GestureShutter.this.mState.handleProceedFinished();
        }

        @Override // com.sonyericsson.android.camera.view.GestureShutterView.AnimationStatusListener
        public void handleConfirmingFinished() {
            GestureShutter.this.mState.handleConfirmingFinished();
        }
    };
    private HandSignsDetectorInterface.DetectResultListener mDetectResultListener = new HandSignsDetectorInterface.DetectResultListener() { // from class: com.sonyericsson.android.camera.controller.GestureShutter.2
        @Override // com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface.DetectResultListener
        public void onDetectResult(HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
            GestureShutter.this.handleDetectResult(detectResultInterface);
        }
    };
    private CameraActivity.LayoutOrientationChangedListener mOrientationListener = new CameraActivity.LayoutOrientationChangedListener() { // from class: com.sonyericsson.android.camera.controller.GestureShutter.3
        @Override // com.sonyericsson.android.camera.CameraActivity.LayoutOrientationChangedListener
        public void onLayoutOrientationChanged(CameraActivity.LayoutOrientation layoutOrientation) {
            if (layoutOrientation != GestureShutter.this.mLayoutOrientation) {
                GestureShutter.this.mLayoutOrientation = layoutOrientation;
                if (GestureShutter.this.mHandSignsDetector != null) {
                    GestureShutter.this.mHandSignsDetector.setLayoutOrientation(GestureShutter.this.mLayoutOrientation);
                }
            }
        }
    };

    public interface ControllerHost {
        void addOrientationListener(CameraActivity.LayoutOrientationChangedListener layoutOrientationChangedListener);

        CameraActivity.LayoutOrientation getLayoutOrientation();

        void prepareGestureShutterCountDown();

        void removeOrientationListener(CameraActivity.LayoutOrientationChangedListener layoutOrientationChangedListener);

        void resetGestureShutterCountDown();

        void startGestureShutterCountDown();
    }

    public interface WindowHost {
        GestureShutterView getGestureShutterView();

        Point getPreviewSize();

        Rect getViewFinderSize();

        void hideGestureShutterView();

        void showGestureShutterView();
    }

    public static boolean isGestureShutterSupported() {
        return IS_GESTURE_SHUTTER_SUPPORTED;
    }

    public GestureShutter(ControllerHost controllerHost, WindowHost windowHost) {
        this.mWindowHost = null;
        this.mControllerHost = null;
        this.mControllerHost = controllerHost;
        this.mWindowHost = windowHost;
        changeState(new StateInitializing());
    }

    public void setEnabled(boolean z) {
        this.mIsEnabled = z;
        if (z) {
            return;
        }
        changeState(new StateStopped(false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeState(State state) {
        if (CamLog.VERBOSE) {
            CamLog.d("State is changing from " + this.mState + " to " + state, new Exception());
        }
        this.mState = state;
        this.mState.entry();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HandSignsDetectorInterface createDetector() {
        if (CamLog.VERBOSE) {
            CamLog.d("Creating HandSignsDetector");
        }
        return new HandSignsDetector(this.mDetectResultListener, this.mUIScheduler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldPerformDetection() {
        if (CamLog.VERBOSE) {
            String[] strArr = new String[1];
            StringBuilder sb = new StringBuilder();
            sb.append("shoudPerformDetection? setting is:");
            sb.append(this.mIsGestureShutterOn ? "ON" : "OFF");
            sb.append(" isPreviewing:");
            sb.append(this.mIsPreviewing);
            sb.append(" isSelftimerRunning:");
            sb.append(this.mIsSelftimerRunning);
            sb.append(" capturingMode:");
            sb.append(this.mCapturingMode);
            strArr[0] = sb.toString();
            CamLog.d(strArr);
        }
        return this.mIsGestureShutterOn && this.mIsPreviewing && !this.mIsSelftimerRunning && isOperableMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOperableMode() {
        return this.mCapturingMode == CapturingMode.FRONT_PHOTO || this.mCapturingMode == CapturingMode.SUPERIOR_FRONT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RectF translateFromDetectToPreview(Rect rect, int i, int i2) {
        if (i == 0 || i2 == 0) {
            return null;
        }
        RectF rectF = new RectF();
        Point previewSize = this.mWindowHost.getPreviewSize();
        float f = previewSize.x / i;
        float f2 = previewSize.y / i2;
        if (previewSize.x == previewSize.y) {
            Rect viewFinderSize = this.mWindowHost.getViewFinderSize();
            rectF.left = (previewSize.x - (rect.right * f)) + (viewFinderSize.height() / 3.0f);
            rectF.right = (previewSize.x - (rect.left * f)) + (viewFinderSize.height() / 3.0f);
        } else {
            rectF.left = previewSize.x - (rect.right * f);
            rectF.right = previewSize.x - (rect.left * f);
        }
        rectF.top = rect.top * f2;
        rectF.bottom = rect.bottom * f2;
        return rectF;
    }

    public void setWindowHost(WindowHost windowHost) {
        this.mState.setWindowHost(windowHost);
    }

    public void handlePreviewStarted(CapturingMode capturingMode, ImageRetriever imageRetriever) {
        this.mImageRetriever = imageRetriever;
        this.mIsPreviewing = true;
        this.mCapturingMode = capturingMode;
        this.mState.updateDetectionStatus();
    }

    public void handlePreviewStopped() {
        this.mIsPreviewing = false;
        this.mImageRetriever = null;
        this.mState.updateDetectionStatus();
    }

    public void handleSettingsChanged(boolean z) {
        this.mIsGestureShutterOn = z;
        this.mState.updateDetectionStatus();
    }

    public void handleSelftimerStarted() {
        this.mIsSelftimerRunning = true;
        this.mState.updateDetectionStatus();
    }

    public void handleSelftimerStopped(boolean z) {
        this.mIsSelftimerRunning = false;
        this.mControllerHost.resetGestureShutterCountDown();
        if (z) {
            return;
        }
        this.mState.updateDetectionStatus();
    }

    public void handleDetectResult(HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
        this.mState.handleDetectResult(detectResultInterface);
    }

    public void release() {
        changeState(new StateStopped(true));
    }

    private abstract class State {
        final boolean mCanStartDetection;
        final boolean mCanStopDetection;

        void entry() {
        }

        void handleConfirmingFinished() {
        }

        void handleDetectResult(HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
        }

        void handleProceedFinished() {
        }

        void handleRewindFinished() {
        }

        void setWindowHost(WindowHost windowHost) {
        }

        protected State(boolean z, boolean z2) {
            this.mCanStartDetection = z;
            this.mCanStopDetection = z2;
        }

        void updateDetectionStatus() {
            if (this.mCanStartDetection && GestureShutter.this.shouldPerformDetection()) {
                LocalResearchUtil.getInstance().startHandSignLostNumCounting();
                GestureShutter.this.changeState(GestureShutter.this.new StateStandBy());
            } else {
                if (!this.mCanStopDetection || GestureShutter.this.shouldPerformDetection()) {
                    return;
                }
                LocalResearchUtil.getInstance().resetHandSignLostNum();
                GestureShutter.this.changeState(GestureShutter.this.new StateStopped(false));
            }
        }

        public String toString() {
            return getClass().getSimpleName();
        }
    }

    private class StateInitializing extends State {
        StateInitializing() {
            super(false, false);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void setWindowHost(WindowHost windowHost) {
            GestureShutter.this.mWindowHost = windowHost;
            GestureShutter.this.changeState(GestureShutter.this.new StateStopped(false));
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void entry() {
            GestureShutter.this.mControllerHost.addOrientationListener(GestureShutter.this.mOrientationListener);
            if (GestureShutter.this.mWindowHost != null) {
                GestureShutter.this.changeState(GestureShutter.this.new StateStopped(false));
            }
        }
    }

    private class StateReleasing extends State {
        StateReleasing() {
            super(false, false);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void entry() {
            GestureShutter.this.mControllerHost.removeOrientationListener(GestureShutter.this.mOrientationListener);
        }
    }

    private class StateStopped extends State {
        private final boolean mStopForRelease;

        StateStopped(boolean z) {
            super(true, false);
            this.mStopForRelease = z;
        }

        private void releaseDetectorIfNeeded() {
            if (GestureShutter.this.mHandSignsDetector != null) {
                if (GestureShutter.this.mIsGestureShutterOn && !this.mStopForRelease && GestureShutter.this.isOperableMode() && GestureShutter.this.mIsEnabled) {
                    return;
                }
                GestureShutter.this.mHandSignsDetector.release();
                GestureShutter.this.mHandSignsDetector = null;
            }
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void entry() {
            if (GestureShutter.this.mWindowHost != null) {
                GestureShutter.this.mWindowHost.hideGestureShutterView();
            }
            if (GestureShutter.this.mHandSignsDetector != null) {
                if (GestureShutter.this.mHandSignsDetector.isStarted()) {
                    GestureShutter.this.mHandSignsDetector.stopDetect();
                }
                releaseDetectorIfNeeded();
            }
            if (this.mStopForRelease) {
                GestureShutter.this.changeState(GestureShutter.this.new StateReleasing());
            }
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void updateDetectionStatus() {
            releaseDetectorIfNeeded();
            super.updateDetectionStatus();
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        public String toString() {
            return super.toString() + " [mStopForRelease=" + this.mStopForRelease + "]";
        }
    }

    private class StateStandBy extends State {
        protected StateStandBy() {
            super(false, true);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void entry() {
            GestureShutter.this.mWindowHost.hideGestureShutterView();
            if (GestureShutter.this.mHandSignsDetector == null) {
                GestureShutter.this.mHandSignsDetector = GestureShutter.this.createDetector();
            }
            if (!GestureShutter.this.mHandSignsDetector.isStarted()) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Detection not started, start it now");
                }
                GestureShutter.this.mHandSignsDetector.startDetect(GestureShutter.this.mImageRetriever);
            }
            if (GestureShutter.this.mLayoutOrientation == CameraActivity.LayoutOrientation.Unknown) {
                GestureShutter.this.mLayoutOrientation = GestureShutter.this.mControllerHost.getLayoutOrientation();
            }
            GestureShutter.this.mHandSignsDetector.setLayoutOrientation(GestureShutter.this.mLayoutOrientation);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void handleDetectResult(HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
            RectF rectFTranslateFromDetectToPreview;
            if (detectResultInterface.getStatus() != HandSignsDetectorInterface.DetectResultInterface.HandStatus.PALM || GestureShutter.this.mHandSignsDetector == null || !GestureShutter.this.mIsEnabled || (rectFTranslateFromDetectToPreview = GestureShutter.this.translateFromDetectToPreview(detectResultInterface.getArea(), GestureShutter.this.mHandSignsDetector.getDetectWidth(), GestureShutter.this.mHandSignsDetector.getDetectHeight())) == null) {
                return;
            }
            GestureShutter.this.changeState(GestureShutter.this.new StateRecognitionProceeding(rectFTranslateFromDetectToPreview));
        }
    }

    private class StateRecognitionProceeding extends State {
        final RectF mInitialFrame;

        StateRecognitionProceeding(RectF rectF) {
            super(false, true);
            this.mInitialFrame = new RectF();
            this.mInitialFrame.set(rectF);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void entry() {
            GestureShutter.this.mWindowHost.showGestureShutterView();
            GestureShutter.this.mWindowHost.getGestureShutterView().setListener(GestureShutter.this.mAnimationListener);
            GestureShutter.this.mWindowHost.getGestureShutterView().startProceed(this.mInitialFrame);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void handleProceedFinished() {
            GestureShutter.this.changeState(GestureShutter.this.new StateConfirming());
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void handleDetectResult(HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
            if (GestureShutter.this.mIsEnabled) {
                if (detectResultInterface.getStatus() != HandSignsDetectorInterface.DetectResultInterface.HandStatus.PALM || GestureShutter.this.mHandSignsDetector == null) {
                    GestureShutter.this.changeState(GestureShutter.this.new StateRecognitionRewinding());
                    return;
                }
                RectF rectFTranslateFromDetectToPreview = GestureShutter.this.translateFromDetectToPreview(detectResultInterface.getArea(), GestureShutter.this.mHandSignsDetector.getDetectWidth(), GestureShutter.this.mHandSignsDetector.getDetectHeight());
                if (rectFTranslateFromDetectToPreview != null) {
                    GestureShutter.this.mWindowHost.getGestureShutterView().updateFrame(rectFTranslateFromDetectToPreview);
                }
            }
        }
    }

    private class StateRecognitionRewinding extends State {
        protected StateRecognitionRewinding() {
            super(false, true);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void entry() {
            LocalResearchUtil.getInstance().countUpHandSignLostNum();
            GestureShutter.this.mWindowHost.getGestureShutterView().startRewind();
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void handleRewindFinished() {
            GestureShutter.this.changeState(GestureShutter.this.new StateStandBy());
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void handleDetectResult(HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
            RectF rectFTranslateFromDetectToPreview;
            if (GestureShutter.this.mIsEnabled && detectResultInterface.getStatus() == HandSignsDetectorInterface.DetectResultInterface.HandStatus.PALM && GestureShutter.this.mHandSignsDetector != null && (rectFTranslateFromDetectToPreview = GestureShutter.this.translateFromDetectToPreview(detectResultInterface.getArea(), GestureShutter.this.mHandSignsDetector.getDetectWidth(), GestureShutter.this.mHandSignsDetector.getDetectHeight())) != null) {
                GestureShutter.this.changeState(GestureShutter.this.new StateRecognitionProceeding(rectFTranslateFromDetectToPreview));
            }
        }
    }

    private class StateConfirming extends State {
        protected StateConfirming() {
            super(false, true);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void entry() {
            GestureShutter.this.mWindowHost.getGestureShutterView().startConfirming();
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.State
        void handleConfirmingFinished() {
            GestureShutter.this.changeState(GestureShutter.this.new StateStopped(false));
            if (GestureShutter.this.mIsEnabled) {
                GestureShutter.this.mControllerHost.prepareGestureShutterCountDown();
                GestureShutter.this.mControllerHost.startGestureShutterCountDown();
            }
        }
    }
}
