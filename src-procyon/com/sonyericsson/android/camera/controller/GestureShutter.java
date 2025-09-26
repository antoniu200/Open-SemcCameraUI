// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller;

import com.sonyericsson.android.camera.research.LocalResearchUtil;
import android.graphics.Point;
import com.sonyericsson.android.camera.gestureshutter.HandSignsDetector;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.RectF;
import android.graphics.Rect;
import android.os.Handler;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.device.ImageRetriever;
import com.sonyericsson.android.camera.gestureshutter.HandSignsDetectorInterface;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.view.GestureShutterView;

public class GestureShutter
{
    private static final boolean IS_GESTURE_SHUTTER_SUPPORTED;
    private static final String TAG = "GestureShutter";
    private GestureShutterView.AnimationStatusListener mAnimationListener;
    private CapturingMode mCapturingMode;
    private ControllerHost mControllerHost;
    private HandSignsDetectorInterface.DetectResultListener mDetectResultListener;
    private HandSignsDetectorInterface mHandSignsDetector;
    private ImageRetriever mImageRetriever;
    private boolean mIsEnabled;
    private boolean mIsGestureShutterOn;
    private boolean mIsPreviewing;
    private boolean mIsSelftimerRunning;
    private CameraActivity.LayoutOrientation mLayoutOrientation;
    private CameraActivity.LayoutOrientationChangedListener mOrientationListener;
    private State mState;
    private Handler mUIScheduler;
    private WindowHost mWindowHost;
    
    static {
        IS_GESTURE_SHUTTER_SUPPORTED = ("".equalsIgnoreCase("noGesture") ^ true);
    }
    
    public GestureShutter(final ControllerHost mControllerHost, final WindowHost mWindowHost) {
        this.mIsEnabled = true;
        this.mIsGestureShutterOn = false;
        this.mIsPreviewing = false;
        this.mCapturingMode = CapturingMode.UNKNOWN;
        this.mIsSelftimerRunning = false;
        this.mLayoutOrientation = CameraActivity.LayoutOrientation.Unknown;
        this.mImageRetriever = null;
        this.mUIScheduler = new Handler();
        this.mWindowHost = null;
        this.mControllerHost = null;
        this.mHandSignsDetector = null;
        this.mState = null;
        this.mAnimationListener = new GestureShutterView.AnimationStatusListener() {
            final GestureShutter this$0;
            
            @Override
            public void handleConfirmingFinished() {
                this.this$0.mState.handleConfirmingFinished();
            }
            
            @Override
            public void handleProceedFinished() {
                this.this$0.mState.handleProceedFinished();
            }
            
            @Override
            public void handleRewindFinished() {
                this.this$0.mState.handleRewindFinished();
            }
        };
        this.mDetectResultListener = new HandSignsDetectorInterface.DetectResultListener() {
            final GestureShutter this$0;
            
            @Override
            public void onDetectResult(final DetectResultInterface detectResultInterface) {
                this.this$0.handleDetectResult(detectResultInterface);
            }
        };
        this.mOrientationListener = new CameraActivity.LayoutOrientationChangedListener() {
            final GestureShutter this$0;
            
            @Override
            public void onLayoutOrientationChanged(final LayoutOrientation layoutOrientation) {
                if (layoutOrientation != this.this$0.mLayoutOrientation) {
                    this.this$0.mLayoutOrientation = layoutOrientation;
                    if (this.this$0.mHandSignsDetector != null) {
                        this.this$0.mHandSignsDetector.setLayoutOrientation(this.this$0.mLayoutOrientation);
                    }
                }
            }
        };
        this.mControllerHost = mControllerHost;
        this.mWindowHost = mWindowHost;
        this.changeState((State)new StateInitializing());
    }
    
    private void changeState(final State state) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("State is changing from ");
            sb.append(this.mState);
            sb.append(" to ");
            sb.append(state);
            CamLog.d(sb.toString(), new Exception());
        }
        (this.mState = state).entry();
    }
    
    private HandSignsDetectorInterface createDetector() {
        if (CamLog.VERBOSE) {
            CamLog.d("Creating HandSignsDetector");
        }
        return new HandSignsDetector(this.mDetectResultListener, this.mUIScheduler);
    }
    
    public static boolean isGestureShutterSupported() {
        return GestureShutter.IS_GESTURE_SHUTTER_SUPPORTED;
    }
    
    private boolean isOperableMode() {
        return this.mCapturingMode == CapturingMode.FRONT_PHOTO || this.mCapturingMode == CapturingMode.SUPERIOR_FRONT;
    }
    
    private boolean shouldPerformDetection() {
        final boolean verbose = CamLog.VERBOSE;
        final boolean b = false;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("shoudPerformDetection? setting is:");
            String str;
            if (this.mIsGestureShutterOn) {
                str = "ON";
            }
            else {
                str = "OFF";
            }
            sb.append(str);
            sb.append(" isPreviewing:");
            sb.append(this.mIsPreviewing);
            sb.append(" isSelftimerRunning:");
            sb.append(this.mIsSelftimerRunning);
            sb.append(" capturingMode:");
            sb.append(this.mCapturingMode);
            CamLog.d(sb.toString());
        }
        boolean b2 = b;
        if (this.mIsGestureShutterOn) {
            b2 = b;
            if (this.mIsPreviewing) {
                b2 = b;
                if (!this.mIsSelftimerRunning) {
                    b2 = b;
                    if (this.isOperableMode()) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    private RectF translateFromDetectToPreview(final Rect rect, final int n, final int n2) {
        if (n != 0 && n2 != 0) {
            final RectF rectF = new RectF();
            final Point previewSize = this.mWindowHost.getPreviewSize();
            final float n3 = previewSize.x / (float)n;
            final float n4 = previewSize.y / (float)n2;
            if (previewSize.x == previewSize.y) {
                final Rect viewFinderSize = this.mWindowHost.getViewFinderSize();
                rectF.left = previewSize.x - rect.right * n3 + viewFinderSize.height() / 3.0f;
                rectF.right = previewSize.x - rect.left * n3 + viewFinderSize.height() / 3.0f;
            }
            else {
                rectF.left = previewSize.x - rect.right * n3;
                rectF.right = previewSize.x - rect.left * n3;
            }
            rectF.top = rect.top * n4;
            rectF.bottom = rect.bottom * n4;
            return rectF;
        }
        return null;
    }
    
    public void handleDetectResult(final HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
        this.mState.handleDetectResult(detectResultInterface);
    }
    
    public void handlePreviewStarted(final CapturingMode mCapturingMode, final ImageRetriever mImageRetriever) {
        this.mImageRetriever = mImageRetriever;
        this.mIsPreviewing = true;
        this.mCapturingMode = mCapturingMode;
        this.mState.updateDetectionStatus();
    }
    
    public void handlePreviewStopped() {
        this.mIsPreviewing = false;
        this.mImageRetriever = null;
        this.mState.updateDetectionStatus();
    }
    
    public void handleSelftimerStarted() {
        this.mIsSelftimerRunning = true;
        this.mState.updateDetectionStatus();
    }
    
    public void handleSelftimerStopped(final boolean b) {
        this.mIsSelftimerRunning = false;
        this.mControllerHost.resetGestureShutterCountDown();
        if (!b) {
            this.mState.updateDetectionStatus();
        }
    }
    
    public void handleSettingsChanged(final boolean mIsGestureShutterOn) {
        this.mIsGestureShutterOn = mIsGestureShutterOn;
        this.mState.updateDetectionStatus();
    }
    
    public void release() {
        this.changeState((State)new StateStopped(true));
    }
    
    public void setEnabled(final boolean mIsEnabled) {
        if (!(this.mIsEnabled = mIsEnabled)) {
            this.changeState((State)new StateStopped(false));
        }
    }
    
    public void setWindowHost(final WindowHost windowHost) {
        this.mState.setWindowHost(windowHost);
    }
    
    public interface ControllerHost
    {
        void addOrientationListener(final CameraActivity.LayoutOrientationChangedListener p0);
        
        CameraActivity.LayoutOrientation getLayoutOrientation();
        
        void prepareGestureShutterCountDown();
        
        void removeOrientationListener(final CameraActivity.LayoutOrientationChangedListener p0);
        
        void resetGestureShutterCountDown();
        
        void startGestureShutterCountDown();
    }
    
    private abstract class State
    {
        final boolean mCanStartDetection;
        final boolean mCanStopDetection;
        final GestureShutter this$0;
        
        protected State(final GestureShutter this$0, final boolean mCanStartDetection, final boolean mCanStopDetection) {
            this.this$0 = this$0;
            this.mCanStartDetection = mCanStartDetection;
            this.mCanStopDetection = mCanStopDetection;
        }
        
        void entry() {
        }
        
        void handleConfirmingFinished() {
        }
        
        void handleDetectResult(final HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
        }
        
        void handleProceedFinished() {
        }
        
        void handleRewindFinished() {
        }
        
        void setWindowHost(final WindowHost windowHost) {
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
        
        void updateDetectionStatus() {
            if (this.mCanStartDetection && this.this$0.shouldPerformDetection()) {
                LocalResearchUtil.getInstance().startHandSignLostNumCounting();
                this.this$0.changeState((State)this.this$0.new StateStandBy());
            }
            else if (this.mCanStopDetection && !this.this$0.shouldPerformDetection()) {
                LocalResearchUtil.getInstance().resetHandSignLostNum();
                this.this$0.changeState((State)this.this$0.new StateStopped(false));
            }
        }
    }
    
    private class StateConfirming extends State
    {
        final GestureShutter this$0;
        
        protected StateConfirming(final GestureShutter this$0) {
            this.this$0 = this$0.super(false, true);
        }
        
        @Override
        void entry() {
            this.this$0.mWindowHost.getGestureShutterView().startConfirming();
        }
        
        @Override
        void handleConfirmingFinished() {
            this.this$0.changeState((State)this.this$0.new StateStopped(false));
            if (this.this$0.mIsEnabled) {
                this.this$0.mControllerHost.prepareGestureShutterCountDown();
                this.this$0.mControllerHost.startGestureShutterCountDown();
            }
        }
    }
    
    private class StateInitializing extends State
    {
        final GestureShutter this$0;
        
        StateInitializing(final GestureShutter this$0) {
            this.this$0 = this$0.super(false, false);
        }
        
        @Override
        void entry() {
            this.this$0.mControllerHost.addOrientationListener(this.this$0.mOrientationListener);
            if (this.this$0.mWindowHost != null) {
                this.this$0.changeState((State)this.this$0.new StateStopped(false));
            }
        }
        
        @Override
        void setWindowHost(final WindowHost windowHost) {
            this.this$0.mWindowHost = windowHost;
            this.this$0.changeState((State)this.this$0.new StateStopped(false));
        }
    }
    
    private class StateRecognitionProceeding extends State
    {
        final RectF mInitialFrame;
        final GestureShutter this$0;
        
        StateRecognitionProceeding(final GestureShutter this$0, final RectF rectF) {
            this.this$0 = this$0.super(false, true);
            (this.mInitialFrame = new RectF()).set(rectF);
        }
        
        @Override
        void entry() {
            this.this$0.mWindowHost.showGestureShutterView();
            this.this$0.mWindowHost.getGestureShutterView().setListener(this.this$0.mAnimationListener);
            this.this$0.mWindowHost.getGestureShutterView().startProceed(this.mInitialFrame);
        }
        
        @Override
        void handleDetectResult(final HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
            if (!this.this$0.mIsEnabled) {
                return;
            }
            if (detectResultInterface.getStatus() == HandSignsDetectorInterface.DetectResultInterface.HandStatus.PALM && this.this$0.mHandSignsDetector != null) {
                final RectF access$1300 = this.this$0.translateFromDetectToPreview(detectResultInterface.getArea(), this.this$0.mHandSignsDetector.getDetectWidth(), this.this$0.mHandSignsDetector.getDetectHeight());
                if (access$1300 != null) {
                    this.this$0.mWindowHost.getGestureShutterView().updateFrame(access$1300);
                }
            }
            else {
                this.this$0.changeState((State)this.this$0.new StateRecognitionRewinding());
            }
        }
        
        @Override
        void handleProceedFinished() {
            this.this$0.changeState((State)this.this$0.new StateConfirming());
        }
    }
    
    private class StateRecognitionRewinding extends State
    {
        final GestureShutter this$0;
        
        protected StateRecognitionRewinding(final GestureShutter this$0) {
            this.this$0 = this$0.super(false, true);
        }
        
        @Override
        void entry() {
            LocalResearchUtil.getInstance().countUpHandSignLostNum();
            this.this$0.mWindowHost.getGestureShutterView().startRewind();
        }
        
        @Override
        void handleDetectResult(final HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
            if (!this.this$0.mIsEnabled) {
                return;
            }
            if (detectResultInterface.getStatus() == HandSignsDetectorInterface.DetectResultInterface.HandStatus.PALM && this.this$0.mHandSignsDetector != null) {
                final RectF access$1300 = this.this$0.translateFromDetectToPreview(detectResultInterface.getArea(), this.this$0.mHandSignsDetector.getDetectWidth(), this.this$0.mHandSignsDetector.getDetectHeight());
                if (access$1300 != null) {
                    this.this$0.changeState((State)this.this$0.new StateRecognitionProceeding(access$1300));
                }
            }
        }
        
        @Override
        void handleRewindFinished() {
            this.this$0.changeState((State)this.this$0.new StateStandBy());
        }
    }
    
    private class StateReleasing extends State
    {
        final GestureShutter this$0;
        
        StateReleasing(final GestureShutter this$0) {
            this.this$0 = this$0.super(false, false);
        }
        
        @Override
        void entry() {
            this.this$0.mControllerHost.removeOrientationListener(this.this$0.mOrientationListener);
        }
    }
    
    private class StateStandBy extends State
    {
        final GestureShutter this$0;
        
        protected StateStandBy(final GestureShutter this$0) {
            this.this$0 = this$0.super(false, true);
        }
        
        @Override
        void entry() {
            this.this$0.mWindowHost.hideGestureShutterView();
            if (this.this$0.mHandSignsDetector == null) {
                this.this$0.mHandSignsDetector = this.this$0.createDetector();
            }
            if (!this.this$0.mHandSignsDetector.isStarted()) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Detection not started, start it now");
                }
                this.this$0.mHandSignsDetector.startDetect(this.this$0.mImageRetriever);
            }
            if (this.this$0.mLayoutOrientation == CameraActivity.LayoutOrientation.Unknown) {
                this.this$0.mLayoutOrientation = this.this$0.mControllerHost.getLayoutOrientation();
            }
            this.this$0.mHandSignsDetector.setLayoutOrientation(this.this$0.mLayoutOrientation);
        }
        
        @Override
        void handleDetectResult(final HandSignsDetectorInterface.DetectResultInterface detectResultInterface) {
            if (detectResultInterface.getStatus() == HandSignsDetectorInterface.DetectResultInterface.HandStatus.PALM && this.this$0.mHandSignsDetector != null && this.this$0.mIsEnabled) {
                final RectF access$1300 = this.this$0.translateFromDetectToPreview(detectResultInterface.getArea(), this.this$0.mHandSignsDetector.getDetectWidth(), this.this$0.mHandSignsDetector.getDetectHeight());
                if (access$1300 != null) {
                    this.this$0.changeState((State)this.this$0.new StateRecognitionProceeding(access$1300));
                }
            }
        }
    }
    
    private class StateStopped extends State
    {
        private final boolean mStopForRelease;
        final GestureShutter this$0;
        
        StateStopped(final GestureShutter this$0, final boolean mStopForRelease) {
            this.this$0 = this$0.super(true, false);
            this.mStopForRelease = mStopForRelease;
        }
        
        private void releaseDetectorIfNeeded() {
            if (this.this$0.mHandSignsDetector != null && (!this.this$0.mIsGestureShutterOn || this.mStopForRelease || !this.this$0.isOperableMode() || !this.this$0.mIsEnabled)) {
                this.this$0.mHandSignsDetector.release();
                this.this$0.mHandSignsDetector = null;
            }
        }
        
        @Override
        void entry() {
            if (this.this$0.mWindowHost != null) {
                this.this$0.mWindowHost.hideGestureShutterView();
            }
            if (this.this$0.mHandSignsDetector != null) {
                if (this.this$0.mHandSignsDetector.isStarted()) {
                    this.this$0.mHandSignsDetector.stopDetect();
                }
                this.releaseDetectorIfNeeded();
            }
            if (this.mStopForRelease) {
                this.this$0.changeState((State)this.this$0.new StateReleasing());
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append(" [mStopForRelease=");
            sb.append(this.mStopForRelease);
            sb.append("]");
            return sb.toString();
        }
        
        @Override
        void updateDetectionStatus() {
            this.releaseDetectorIfNeeded();
            super.updateDetectionStatus();
        }
    }
    
    public interface WindowHost
    {
        GestureShutterView getGestureShutterView();
        
        Point getPreviewSize();
        
        Rect getViewFinderSize();
        
        void hideGestureShutterView();
        
        void showGestureShutterView();
    }
}
