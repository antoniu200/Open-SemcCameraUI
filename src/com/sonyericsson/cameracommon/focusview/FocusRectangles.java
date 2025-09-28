package com.sonyericsson.cameracommon.focusview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.animation.FocusRectanglesAnimation;
import com.sonyericsson.cameracommon.focusview.Rectangle;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FocusRectangles {
    private static final String ANIMATION_SCALE_X = "scaleX";
    private static final String ANIMATION_SCALE_Y = "scaleY";
    private static final int FOCUS_RECT_SET_DOWN_ANIMATION_START_DELAY_TIME = 100;
    private static final float INTERPOLATOR_CONTROL_X1 = 0.23f;
    private static final float INTERPOLATOR_CONTROL_X2 = 0.32f;
    private static final float INTERPOLATOR_CONTROL_Y1 = 1.0f;
    private static final float INTERPOLATOR_CONTROL_Y2 = 1.0f;
    public static final String TAG = "FocusRectangles";
    private static final int TRACKED_OBJECT_RECT_REFRESH_TIMEOUT = 1000;
    private Activity mActivity;
    private FocusRectanglesAnimation mAnimation;
    private View mCaptureArea;
    private int mDevicePreviewHeight;
    private int mDevicePreviewWidth;
    private HashMap<String, TaggedRectangle> mFaceRectangles;
    private FaceReflectChecker mFaceReflectChecker;
    private FocusActionListener mFocusEventListener;
    private CameraParameters.FaceDetectionResult mLastFaceDetectionResult;
    private View.OnTouchListener mOnTouchListener;
    private RelativeLayout mRectangles;
    private LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private RelativeLayout mSingleAfRect;
    private int mSmileScore;
    private RelativeLayout mTouchAfRect;
    private TaggedRectangle mTrackedObjectRectangle;
    private Handler mHandler = new Handler();
    private TaggedRectangle mPressedRectangle = null;
    private final RefreshTrackedObjectRectangleTask mRefreshTrackedObjectRectangleTask = new RefreshTrackedObjectRectangleTask();
    private final OnFaceRectTouchListener mOnFaceRectTouchListener = new OnFaceRectTouchListener();
    private boolean mIsFaceTouchCaptureEnabled = false;
    private boolean mIsFocusAnimationEnabled = false;
    private State mCurrentState = new DefaultFocusState();
    private boolean mIsRecording = false;
    private String mLatestSelectedFaceUuid = null;
    private int mCurrentOrientation = 2;
    private RectangleTouchEventDispatcher mTouchEventDispatcher = null;
    private Runnable mFocusAnimationTask = null;
    private int mSmileCaptureLevel = -1;
    private boolean mIsManualFocus = false;
    private boolean mObjectTrackingRectSupported = false;
    private ObJectTrackingFocusIconState mObJectTrackingFocusIconState = ObJectTrackingFocusIconState.NOT_DISPLAY;

    private interface FaceReflectedCallback {
        void onFaceReflected();
    }

    public enum FocusSetType {
        FIRST,
        MOVE,
        RELEASE
    }

    private enum ObJectTrackingFocusIconState {
        NOT_DISPLAY,
        TOUCH_ICON,
        TRACKING_ICON
    }

    private interface State {
        void handleClearAllFocusExceptFace();

        void handleClearExceptTouchFocus();

        void handleClearObjectTracking();

        void handleOnAutoFocusCanceled();

        void handleOnAutoFocusDone(boolean z);

        void handleOnAutoFocusStarted();

        void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult);

        void handleOnObjectLost();

        void handleOnObjectRemoved();

        void handleOnTrackedObjectStateUpdated(CameraParameters.ObjectTrackingResult objectTrackingResult);

        void handleOnUiComponentOverlaid();

        void handleOnUiComponentRemoved();

        void handleSetFocusPosition(Point point, FocusSetType focusSetType);

        void handleStartAfLock();

        void handleStartFaceDetection();

        void handleStartObjectTracking();

        void handleStopAfLock();
    }

    public FocusRectangles(Activity activity, FocusActionListener focusActionListener, int i, int i2, FocusRectanglesViewList focusRectanglesViewList, View view, View.OnTouchListener onTouchListener, LayoutDependencyResolver.ScreenAspect screenAspect) {
        this.mActivity = activity;
        this.mScreenAspect = screenAspect;
        this.mFocusEventListener = focusActionListener;
        this.mDevicePreviewWidth = i;
        this.mDevicePreviewHeight = i2;
        this.mAnimation = new FocusRectanglesAnimation(this.mActivity);
        this.mCaptureArea = view;
        this.mOnTouchListener = onTouchListener;
        initialize(focusRectanglesViewList);
        this.mFaceReflectChecker = new FaceReflectChecker();
    }

    public void enableFaceTouchCapture() {
        this.mIsFaceTouchCaptureEnabled = true;
    }

    public void disableFaceTouchCapture() {
        this.mIsFaceTouchCaptureEnabled = false;
    }

    public void clearTouched() {
        this.mOnFaceRectTouchListener.clearTouched();
    }

    public void updateDevicePreviewSize(int i, int i2) {
        this.mDevicePreviewWidth = i;
        this.mDevicePreviewHeight = i2;
        updateRectanglesCoordinates();
    }

    private void initialize(FocusRectanglesViewList focusRectanglesViewList) {
        this.mRectangles = focusRectanglesViewList.rectanglesContainer;
        LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
        this.mFaceRectangles = new HashMap<>();
        this.mTouchEventDispatcher = new RectangleTouchEventDispatcher(this.mFaceRectangles);
        this.mRectangles.setOnTouchListener(this.mTouchEventDispatcher);
        View[] viewArr = focusRectanglesViewList.faceViewList != null ? focusRectanglesViewList.faceViewList : null;
        for (int i = 0; i < 5; i++) {
            addTaggedRectangle(layoutInflater, Integer.toString(i), viewArr != null ? (TaggedRectangle) viewArr[i] : null);
        }
        this.mTrackedObjectRectangle = focusRectanglesViewList.trackedObjectView;
        if (this.mTrackedObjectRectangle == null) {
            this.mTrackedObjectRectangle = (TaggedRectangle) layoutInflater.inflate(R.layout.face_rectangle, (ViewGroup) null);
        }
        this.mTrackedObjectRectangle.setVisibility(4);
        this.mRectangles.addView(this.mTrackedObjectRectangle, new ViewGroup.LayoutParams(-1, -1));
        this.mTrackedObjectRectangle.prepare(3);
        this.mTrackedObjectRectangle.setRectImageSize(0, 0, this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_object_width), this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_object_height));
        ((ImageView) this.mTrackedObjectRectangle.findViewById(R.id.rect_image)).setOnTouchListener(this.mOnTouchListener);
        this.mSingleAfRect = focusRectanglesViewList.singleAfView;
        if (this.mSingleAfRect == null) {
            this.mSingleAfRect = (RelativeLayout) layoutInflater.inflate(R.layout.fast_capturing_auto_focus_rectangles, (ViewGroup) null);
        }
        this.mSingleAfRect.setVisibility(4);
        this.mRectangles.addView(this.mSingleAfRect, new RelativeLayout.LayoutParams(-1, -1));
        this.mTouchAfRect = focusRectanglesViewList.touchAfView;
        if (this.mTouchAfRect == null) {
            this.mTouchAfRect = (RelativeLayout) layoutInflater.inflate(R.layout.fast_capturing_auto_focus_rectangles, (ViewGroup) null);
        }
        this.mTouchAfRect.setVisibility(4);
        this.mRectangles.addView(this.mTouchAfRect, new RelativeLayout.LayoutParams(-1, -1));
        ((ImageView) this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect)).setOnTouchListener(this.mOnTouchListener);
        updateRectanglesCoordinates();
    }

    private TaggedRectangle addTaggedRectangle(LayoutInflater layoutInflater, String str, TaggedRectangle taggedRectangle) {
        if (this.mFaceRectangles.size() >= 5) {
            return null;
        }
        Rect rect = new Rect();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
        if (taggedRectangle == null) {
            taggedRectangle = (TaggedRectangle) layoutInflater.inflate(R.layout.face_rectangle, (ViewGroup) null);
        }
        this.mRectangles.addView(taggedRectangle, layoutParams);
        taggedRectangle.prepare(0);
        taggedRectangle.setRectPosition(rect.centerX(), rect.centerY(), rect.width(), rect.height());
        taggedRectangle.setRectangleOnTouchListener(this.mOnFaceRectTouchListener);
        this.mFaceRectangles.put(str, taggedRectangle);
        return taggedRectangle;
    }

    private void updateRectanglesCoordinates() {
        Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect(this.mActivity, this.mDevicePreviewWidth / this.mDevicePreviewHeight, this.mScreenAspect);
        setRectSizeAndPosition(this.mRectangles, surfaceViewRect.left, surfaceViewRect.top, surfaceViewRect.width(), surfaceViewRect.height());
        Iterator<String> it = this.mFaceRectangles.keySet().iterator();
        while (it.hasNext()) {
            this.mFaceRectangles.get(it.next()).setSize(surfaceViewRect.width(), surfaceViewRect.height());
        }
        changeState(new DefaultFocusState());
    }

    public void release() {
        this.mActivity = null;
        this.mFocusEventListener = null;
    }

    public void onAutoFocusStarted() {
        this.mCurrentState.handleOnAutoFocusStarted();
    }

    public void onAutoFocusDone(boolean z) {
        this.mCurrentState.handleOnAutoFocusDone(z);
    }

    public void onAutoFocusCanceled() {
        this.mCurrentState.handleOnAutoFocusCanceled();
    }

    public void setFocusPosition(Point point, FocusSetType focusSetType) {
        int[] iArr = new int[2];
        this.mRectangles.getLocationOnScreen(iArr);
        this.mCurrentState.handleSetFocusPosition(new Point(point.x - iArr[0], point.y - iArr[1]), focusSetType);
    }

    public void startFaceDetection() {
        this.mCurrentState.handleStartFaceDetection();
    }

    public void onFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
        this.mCurrentState.handleOnFaceDetected(faceDetectionResult);
    }

    public void startObjectTracking() {
        this.mObJectTrackingFocusIconState = ObJectTrackingFocusIconState.NOT_DISPLAY;
        this.mCurrentState.handleStartObjectTracking();
    }

    public void onObjectTracked(CameraParameters.ObjectTrackingResult objectTrackingResult) {
        this.mCurrentState.handleOnTrackedObjectStateUpdated(objectTrackingResult);
    }

    public void onObjectRemoved() {
        this.mCurrentState.handleOnObjectRemoved();
    }

    public void onObjectLost() {
        if (this.mObjectTrackingRectSupported) {
            this.mTrackedObjectRectangle.changeRectangleResource(2131230872);
        }
        this.mCurrentState.handleOnObjectLost();
    }

    public void onObjectFocused() {
        this.mTrackedObjectRectangle.changeRectangleResource(2131230871);
    }

    public void clearExceptTouchFocus() {
        this.mCurrentState.handleClearExceptTouchFocus();
    }

    public void clearAllFocus() {
        clearAllFocusExceptFace();
        clearFaceDetection();
    }

    public void clearAllFocusExceptFace() {
        this.mCurrentState.handleClearAllFocusExceptFace();
    }

    public void clearTouchFocus() {
        changeState(new DefaultFocusState());
        setFocusPositionInternal(null, null);
        this.mTouchAfRect.setVisibility(4);
        removeTouchFocusRectAnimation();
        resetTouchFocusRectangleColor();
    }

    public void clearSingleAutoFocus() {
        this.mSingleAfRect.setVisibility(4);
        removeSingleFocusRectAnimation();
        resetSingleFocusRectangleColor();
    }

    public void clearFaceDetection() {
        hideFaceRectangles(false);
        resetFaceRectangleColor();
        this.mTouchEventDispatcher.updateFaceList(null);
    }

    public void clearObjectTracking() {
        this.mCurrentState.handleClearObjectTracking();
    }

    public void onUiComponentOverlaid() {
        this.mCurrentState.handleOnUiComponentOverlaid();
    }

    public void onUiComponentRemoved() {
        this.mCurrentState.handleOnUiComponentRemoved();
    }

    public void reset() {
        stopRecording();
        setAFLocking(false);
        setLockedBySelfTimer(false);
    }

    public void startRecording() {
        this.mIsRecording = true;
    }

    public void stopRecording() {
        this.mIsRecording = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAFLocking(boolean z) {
        if (z) {
            this.mCurrentState.handleStartAfLock();
        } else {
            this.mCurrentState.handleStopAfLock();
        }
        setEnableFaceFocusTouch(!z);
    }

    public void setLockedBySelfTimer(boolean z) {
        setEnableFaceFocusTouch(!z);
    }

    public void setEnableFaceFocusTouch(boolean z) {
        if (this.mFaceRectangles == null) {
            return;
        }
        Iterator<Map.Entry<String, TaggedRectangle>> it = this.mFaceRectangles.entrySet().iterator();
        while (it.hasNext()) {
            TaggedRectangle value = it.next().getValue();
            value.setRectangleOnTouchListener((z || value.isPressed()) ? this.mOnFaceRectTouchListener : null);
        }
    }

    private class TouchFocusState extends DefaultFocusState {
        private boolean mIsAutoFocusStarted;

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnObjectRemoved() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnTrackedObjectStateUpdated(CameraParameters.ObjectTrackingResult objectTrackingResult) {
        }

        private TouchFocusState() {
            super();
            this.mIsAutoFocusStarted = false;
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusStarted() {
            this.mIsAutoFocusStarted = true;
            FocusRectangles.this.updateFaceRectanglesData(null, true);
            FocusRectangles.this.mAnimation.startFocusAnimation(FocusRectangles.this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect), FocusRectangles.this.getTouchAfFocusingIcon());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusDone(boolean z) {
            this.mIsAutoFocusStarted = false;
            FocusRectangles.this.setAFLocking(true);
            ImageView imageView = (ImageView) FocusRectangles.this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
            imageView.setVisibility(0);
            if (!z) {
                if (FocusRectangles.this.mIsManualFocus) {
                    return;
                }
                FocusRectangles.this.mAnimation.playAfFadeOutAnimationTouch(imageView);
            } else {
                imageView.setBackgroundResource(FocusRectangles.this.getTouchAfSuccessIcon());
                FocusRectangles.this.mAnimation.playAfFocusInAnimationTouch(imageView, FocusRectangles.this.getTouchAfSuccessIcon());
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusCanceled() {
            this.mIsAutoFocusStarted = false;
            ((ImageView) FocusRectangles.this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect)).setVisibility(0);
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleSetFocusPosition(Point point, FocusSetType focusSetType) {
            this.mIsAutoFocusStarted = false;
            FocusRectangles.this.setFocusPositionInternal(point, focusSetType);
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartFaceDetection() {
            FocusRectangles.this.clearSingleAutoFocus();
            FocusRectangles.this.clearObjectTracking();
            FocusRectangles.this.changeState(new FaceDetectionState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
            FocusRectangles focusRectangles = FocusRectangles.this;
            if (this.mIsAutoFocusStarted) {
                faceDetectionResult = null;
            }
            focusRectangles.updateFaceRectanglesData(faceDetectionResult, true);
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartObjectTracking() {
            FocusRectangles.this.clearExceptTouchFocus();
            FocusRectangles.this.changeState(new ObjectTrackingState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleClearExceptTouchFocus() {
            FocusRectangles.this.mAnimation.stopFocusAnimation((ImageView) FocusRectangles.this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect));
            super.handleClearExceptTouchFocus();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnUiComponentRemoved() {
            FocusRectangles.this.mTouchAfRect.setVisibility(0);
            FocusRectangles.this.mSingleAfRect.setVisibility(4);
            FocusRectangles.this.resetRectanglesColor();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new TouchFocusInLockedState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStopAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new TouchFocusState());
        }
    }

    private class ObjectTrackingState extends DefaultFocusState {
        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartObjectTracking() {
        }

        protected boolean isAFLocking() {
            return false;
        }

        private ObjectTrackingState() {
            super();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusStarted() {
            FocusRectangles.this.setAFLocking(true);
            FocusRectangles.this.mAnimation.startFocusAnimation(FocusRectangles.this.mTrackedObjectRectangle.findViewById(R.id.rect_image), FocusRectangles.this.getAfFocusingIcon());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusDone(boolean z) {
            FocusRectangles.this.mTrackedObjectRectangle.setVisibility(0);
            if (z) {
                FocusRectangles.this.mTrackedObjectRectangle.changeRectangleResource(FocusRectangles.this.getSuccessIcon());
            } else {
                FocusRectangles.this.mAnimation.playAfFadeOutAnimationObject(FocusRectangles.this.mTrackedObjectRectangle);
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleSetFocusPosition(Point point, FocusSetType focusSetType) {
            FocusRectangles.this.setFocusPositionInternal(point, focusSetType);
            FocusRectangles.this.changeState(new TouchFocusState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartFaceDetection() {
            FocusRectangles.this.clearExceptTouchFocus();
            FocusRectangles.this.changeState(new FaceDetectionState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
            FocusRectangles.this.updateFaceRectanglesData(faceDetectionResult, true);
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnTrackedObjectStateUpdated(CameraParameters.ObjectTrackingResult objectTrackingResult) throws Resources.NotFoundException {
            if (FocusRectangles.this.mObjectTrackingRectSupported) {
                if (FocusRectangles.this.mObJectTrackingFocusIconState == ObJectTrackingFocusIconState.NOT_DISPLAY) {
                    FocusRectangles.this.initObjectTrackingAnimation(objectTrackingResult);
                    return;
                } else {
                    FocusRectangles.this.playObjectTrackingAnimation(objectTrackingResult, isAFLocking());
                    return;
                }
            }
            FocusRectangles.this.onObjectTrackedInternal(objectTrackingResult, isAFLocking());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnObjectLost() {
            FocusRectangles.this.changeState(FocusRectangles.this.new ObjectLostState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnObjectRemoved() {
            FocusRectangles.this.changeState(FocusRectangles.this.new DefaultFocusState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new ObjectTrackingInLockedState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStopAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new ObjectTrackingState());
        }
    }

    class ObjectLostState extends ObjectTrackingState {
        ObjectLostState() {
            super();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new ObjectLostInLockedState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnTrackedObjectStateUpdated(CameraParameters.ObjectTrackingResult objectTrackingResult) {
            FocusRectangles.this.changeState(new ObjectTrackingState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStopAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new ObjectLostState());
        }
    }

    private class FaceDetectionState extends DefaultFocusState {
        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnObjectRemoved() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartFaceDetection() {
        }

        private FaceDetectionState() {
            super();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusStarted() {
            if (!isFaceRectAvailable()) {
                FocusRectangles.this.changeState(FocusRectangles.this.new DefaultFocusState());
                FocusRectangles.this.onAutoFocusStarted();
            } else {
                FocusRectangles.this.setAFLocking(true);
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusDone(boolean z) {
            if (isFaceRectAvailable()) {
                if (FocusRectangles.this.mLatestSelectedFaceUuid == null) {
                    return;
                }
                Iterator it = FocusRectangles.this.mFaceRectangles.entrySet().iterator();
                while (it.hasNext()) {
                    TaggedRectangle taggedRectangle = (TaggedRectangle) ((Map.Entry) it.next()).getValue();
                    if (FocusRectangles.this.mLatestSelectedFaceUuid.equals(taggedRectangle.getUuid())) {
                        taggedRectangle.changeRectangleResource(2131230869);
                        if (!taggedRectangle.isShown()) {
                            taggedRectangle.setVisibility(0);
                        }
                    } else {
                        taggedRectangle.setVisibility(4);
                    }
                }
                return;
            }
            super.handleOnAutoFocusDone(z);
        }

        private boolean isFaceRectAvailable() {
            Iterator it = FocusRectangles.this.mFaceRectangles.values().iterator();
            while (it.hasNext()) {
                if (((TaggedRectangle) it.next()).getVisibility() == 0) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleSetFocusPosition(Point point, FocusSetType focusSetType) {
            if (focusSetType != FocusSetType.FIRST) {
                return;
            }
            FocusRectangles.this.setFocusPositionInternal(point, focusSetType);
            FocusRectangles.this.changeState(new TouchFocusState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (faceDetectionResult.extFaceList.size() != 0) {
                FocusRectangles.this.updateFaceRectanglesData(faceDetectionResult, false);
            } else {
                FocusRectangles.this.clearFaceDetection();
                FocusRectangles.this.changeState(FocusRectangles.this.new DefaultFocusState());
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartObjectTracking() {
            FocusRectangles.this.clearSingleAutoFocus();
            FocusRectangles.this.clearTouchFocus();
            FocusRectangles.this.clearFaceDetection();
            FocusRectangles.this.changeState(new ObjectTrackingState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnTrackedObjectStateUpdated(CameraParameters.ObjectTrackingResult objectTrackingResult) {
            FocusRectangles.this.mSingleAfRect.setVisibility(4);
            FocusRectangles.this.hideFaceRectangles(true);
            FocusRectangles.this.onObjectTrackedInternal(objectTrackingResult, false);
            FocusRectangles.this.changeState(new ObjectTrackingState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new FaceDetectionInLockedState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStopAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.mLatestSelectedFaceUuid != null ? FocusRectangles.this.new FaceDetectionState() : FocusRectangles.this.new DefaultFocusState());
        }
    }

    class DefaultFocusState implements State {
        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusCanceled() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnObjectLost() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnObjectRemoved() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnTrackedObjectStateUpdated(CameraParameters.ObjectTrackingResult objectTrackingResult) {
        }

        DefaultFocusState() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusStarted() {
            FocusRectangles.this.setAFLocking(true);
            FocusRectangles.this.mSingleAfRect.setVisibility(0);
            View viewFindViewById = FocusRectangles.this.mSingleAfRect.findViewById(R.id.center_auto_focus_rect);
            if (viewFindViewById.getVisibility() != 0) {
                viewFindViewById.setVisibility(0);
            }
            FocusRectangles.this.mAnimation.startFocusAnimation(viewFindViewById, 2131230866);
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusDone(boolean z) {
            ImageView imageView = (ImageView) FocusRectangles.this.mSingleAfRect.findViewById(R.id.center_auto_focus_rect);
            if (z) {
                if (imageView.getVisibility() != 0) {
                    imageView.setVisibility(0);
                }
                imageView.setBackgroundResource(2131230865);
                FocusRectangles.this.mAnimation.playAfFocusInAnimationSingle(imageView);
                return;
            }
            if (imageView.getVisibility() == 0) {
                imageView.setVisibility(4);
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleSetFocusPosition(Point point, FocusSetType focusSetType) {
            FocusRectangles.this.setFocusPositionInternal(point, focusSetType);
            FocusRectangles.this.changeState(new TouchFocusState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartFaceDetection() {
            FocusRectangles.this.clearSingleAutoFocus();
            FocusRectangles.this.clearObjectTracking();
            FocusRectangles.this.changeState(new FaceDetectionState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (faceDetectionResult.extFaceList.size() == 0) {
                FocusRectangles.this.changeState(FocusRectangles.this.new DefaultFocusState());
            } else {
                FocusRectangles.this.updateFaceRectanglesData(faceDetectionResult, false);
                FocusRectangles.this.changeState(new FaceDetectionState());
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartObjectTracking() {
            FocusRectangles.this.clearExceptTouchFocus();
            FocusRectangles.this.changeState(new ObjectTrackingState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleClearExceptTouchFocus() {
            FocusRectangles.this.clearSingleAutoFocus();
            FocusRectangles.this.clearObjectTracking();
            FocusRectangles.this.clearFaceDetection();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleClearAllFocusExceptFace() {
            FocusRectangles.this.changeState(FocusRectangles.this.new DefaultFocusState());
            FocusRectangles.this.clearSingleAutoFocus();
            FocusRectangles.this.clearTouchFocus();
            FocusRectangles.this.clearObjectTracking();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnUiComponentOverlaid() {
            FocusRectangles.this.mSingleAfRect.setVisibility(4);
            FocusRectangles.this.hideFaceRectangles(false);
            FocusRectangles.this.hideTrackedObjectRecgantle();
            FocusRectangles.this.mTouchAfRect.setVisibility(4);
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnUiComponentRemoved() {
            FocusRectangles.this.mTouchAfRect.setVisibility(4);
            FocusRectangles.this.mSingleAfRect.setVisibility(4);
            FocusRectangles.this.resetRectanglesColor();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleClearObjectTracking() {
            FocusRectangles.this.hideTrackedObjectRecgantle();
            FocusRectangles.this.removeObjectFocusRectAnimation();
            FocusRectangles.this.resetObjectTrackingRectangleColor(false);
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new DefaultFocusInLockedState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStopAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new DefaultFocusState());
        }
    }

    class DefaultFocusInLockedState extends DefaultFocusState {
        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartFaceDetection() {
        }

        DefaultFocusInLockedState() {
            super();
        }
    }

    class TouchFocusInLockedState extends TouchFocusState {
        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.TouchFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.TouchFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartFaceDetection() {
        }

        TouchFocusInLockedState() {
            super();
        }
    }

    class FaceDetectionInLockedState extends FaceDetectionState {
        FaceDetectionInLockedState() {
            super();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.FaceDetectionState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (faceDetectionResult.extFaceList.size() == 0) {
                FocusRectangles.this.mLatestSelectedFaceUuid = null;
            }
            if (FocusRectangles.this.mLatestSelectedFaceUuid == null) {
                return;
            }
            FocusRectangles.this.updateFaceRectanglesData(faceDetectionResult, false, FocusRectangles.this.getSelectedFaceUuId(faceDetectionResult));
        }
    }

    class ObjectTrackingInLockedState extends ObjectTrackingState {
        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleClearAllFocusExceptFace() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleClearObjectTracking() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnObjectRemoved() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartFaceDetection() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState
        protected boolean isAFLocking() {
            return true;
        }

        ObjectTrackingInLockedState() {
            super();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusDone(boolean z) {
            if (z) {
                super.handleOnAutoFocusDone(z);
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnObjectLost() {
            FocusRectangles.this.changeState(FocusRectangles.this.new ObjectLostInLockedState());
        }
    }

    class ObjectLostInLockedState extends ObjectTrackingInLockedState {
        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingInLockedState, com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnAutoFocusDone(boolean z) {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleOnTrackedObjectStateUpdated(CameraParameters.ObjectTrackingResult objectTrackingResult) {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStartAfLock() {
        }

        ObjectLostInLockedState() {
            super();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingInLockedState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleClearAllFocusExceptFace() {
            FocusRectangles.this.changeState(FocusRectangles.this.new ObjectTrackingStoppedInLockedState());
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStopAfLock() {
            FocusRectangles.this.changeState(FocusRectangles.this.new ObjectLostState());
        }
    }

    class ObjectTrackingStoppedInLockedState extends ObjectLostInLockedState {
        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectLostInLockedState, com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingInLockedState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleClearAllFocusExceptFace() {
        }

        ObjectTrackingStoppedInLockedState() {
            super();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectLostInLockedState, com.sonyericsson.cameracommon.focusview.FocusRectangles.ObjectTrackingState, com.sonyericsson.cameracommon.focusview.FocusRectangles.DefaultFocusState, com.sonyericsson.cameracommon.focusview.FocusRectangles.State
        public void handleStopAfLock() {
            FocusRectangles.this.hideTrackedObjectRecgantle();
            FocusRectangles.this.removeObjectFocusRectAnimation();
            FocusRectangles.this.resetObjectTrackingRectangleColor(false);
            FocusRectangles.this.changeState(FocusRectangles.this.new DefaultFocusState());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFaceRectanglesData(CameraParameters.FaceDetectionResult faceDetectionResult, boolean z) {
        updateFaceRectanglesData(faceDetectionResult, z, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFaceRectanglesData(CameraParameters.FaceDetectionResult faceDetectionResult, boolean z, String str) {
        this.mLastFaceDetectionResult = faceDetectionResult;
        faceResultToRectangles(faceDetectionResult, z, str);
        if (this.mFaceReflectChecker.isWaitingForFaceReflected()) {
            this.mFaceReflectChecker.check(faceDetectionResult);
        }
    }

    private void updateFaceRectangles(FaceInformationList faceInformationList, String str, int i, boolean z) {
        if (str == null) {
            str = faceInformationList.getUserSelectedUuid();
            if (CamLog.VERBOSE) {
                CamLog.d("updateFaceRectangles: uuid is not specified, use uuid from API, uuid = " + str);
            }
        }
        int i2 = 0;
        for (TaggedRectangle taggedRectangle : this.mFaceRectangles.values()) {
            if (taggedRectangle == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("updateFaceRectangles: view is null, index = " + i2);
                }
                i2++;
            } else {
                taggedRectangle.clearUpdated();
                if (i2 < faceInformationList.getNamedFaceList().size()) {
                    NamedFace namedFace = faceInformationList.getNamedFace(i2);
                    if (namedFace == null) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("updateFaceRectangles: namedFace is null, index = " + i2);
                        }
                        i2++;
                    } else {
                        updateRectangle(taggedRectangle, namedFace, i, z);
                        if (namedFace.mUuid.equals(str)) {
                            taggedRectangle.changeRectangleResource(2131230869);
                            taggedRectangle.stopAnimation();
                            if (this.mSmileCaptureLevel == -1) {
                                taggedRectangle.setSmileGaugeVisibility(4);
                            } else {
                                taggedRectangle.setSmileLevel(this.mSmileCaptureLevel);
                                taggedRectangle.setSmileGaugeVisibility(0);
                                updateSmileGauge(taggedRectangle, faceInformationList, this.mSmileCaptureLevel, i);
                            }
                            this.mLatestSelectedFaceUuid = taggedRectangle.getUuid();
                        } else {
                            taggedRectangle.setSmileGaugeVisibility(4);
                        }
                    }
                } else {
                    taggedRectangle.hide();
                    taggedRectangle.update(null, i);
                }
                i2++;
            }
        }
    }

    public int getSelectedFaceSmileScore() {
        int i = this.mSmileScore;
        this.mSmileScore = 0;
        return i;
    }

    private void updateSmileGauge(TaggedRectangle taggedRectangle, FaceInformationList faceInformationList, int i, int i2) {
        NamedFace namedFaceByUuid = faceInformationList.getNamedFaceByUuid(taggedRectangle.getUuid());
        if (namedFaceByUuid == null) {
            return;
        }
        Rect rectConvertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(namedFaceByUuid.mFacePosition);
        taggedRectangle.setSmileGaugesPosition(rectConvertFromActiveArrayToView.left, rectConvertFromActiveArrayToView.top, rectConvertFromActiveArrayToView.right, rectConvertFromActiveArrayToView.bottom, i2);
        taggedRectangle.setSmileLevel(i);
        taggedRectangle.setSmileScore(namedFaceByUuid.mSmileScore);
        this.mSmileScore = namedFaceByUuid.mSmileScore;
    }

    private void updateRectangle(TaggedRectangle taggedRectangle, NamedFace namedFace, int i, boolean z) {
        Rect rectConvertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(namedFace.mFacePosition);
        CamLog.d("Converted rectangle: " + rectConvertFromActiveArrayToView);
        taggedRectangle.setRectPosition(rectConvertFromActiveArrayToView.centerX(), rectConvertFromActiveArrayToView.centerY(), rectConvertFromActiveArrayToView.width(), rectConvertFromActiveArrayToView.height());
        if (z) {
            taggedRectangle.changeRectangleResource(0);
            taggedRectangle.hide();
        } else {
            taggedRectangle.changeRectangleResource(2131230868);
        }
        if (!(taggedRectangle.getVisibility() == 0)) {
            taggedRectangle.startRectangleAnimation(i);
        }
        taggedRectangle.update(namedFace.mUuid, i);
        taggedRectangle.setUpdated();
        if (taggedRectangle.getVisibility() != 0) {
            taggedRectangle.requestLayout();
            taggedRectangle.setVisibility(0);
        }
    }

    private void setRectSizeAndPosition(RelativeLayout relativeLayout, int i, int i2, int i3, int i4) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.leftMargin = i;
            layoutParams.topMargin = i2;
            layoutParams.width = i3;
            layoutParams.height = i4;
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.LANDSCAPE) {
                layoutParams.addRule(15, -1);
                layoutParams.removeRule(14);
            } else {
                layoutParams.removeRule(15);
                layoutParams.addRule(14, -1);
            }
            relativeLayout.setLayoutParams(layoutParams);
        }
    }

    class OnFaceRectTouchListener implements Rectangle.RectangleOnTouchListener {
        private boolean mIsForceTouchCanceled = false;

        OnFaceRectTouchListener() {
        }

        protected void clearTouched() {
            this.mIsForceTouchCanceled = true;
        }

        @Override // com.sonyericsson.cameracommon.focusview.Rectangle.RectangleOnTouchListener
        public void onRectTouchDown(View view, MotionEvent motionEvent) {
            if (FocusRectangles.this.mPressedRectangle == null && !FocusRectangles.this.mFaceReflectChecker.isWaitingForCapturing()) {
                Rectangle rectangle = (Rectangle) view.findViewById(R.id.rect);
                if (rectangle.getVisibility() == 0) {
                    for (Map.Entry entry : FocusRectangles.this.mFaceRectangles.entrySet()) {
                        TaggedRectangle taggedRectangle = (TaggedRectangle) entry.getValue();
                        if (taggedRectangle.equals(view)) {
                            view.setPressed(false);
                            FocusRectangles.this.mPressedRectangle = taggedRectangle;
                            taggedRectangle.startRectanglePressAnimation();
                            boolean zEquals = FocusRectangles.this.mLatestSelectedFaceUuid != null ? FocusRectangles.this.mLatestSelectedFaceUuid.equals(taggedRectangle.getUuid()) : false;
                            FocusRectangles.this.faceResultToRectangles(FocusRectangles.this.mLastFaceDetectionResult, false, null);
                            FocusRectangles.this.changeFacePriority((String) entry.getKey());
                            if (isTouchAreaOnTouchCapture(rectangle, motionEvent) && zEquals) {
                                FocusRectangles.this.mFocusEventListener.onTouched();
                                return;
                            }
                            return;
                        }
                    }
                }
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.Rectangle.RectangleOnTouchListener
        public void onRectTouchUp(View view, MotionEvent motionEvent) {
            if (FocusRectangles.this.mPressedRectangle == null || !FocusRectangles.this.mPressedRectangle.equals(view)) {
                return;
            }
            view.setPressed(false);
            FocusRectangles.this.mPressedRectangle = null;
            if (this.mIsForceTouchCanceled) {
                this.mIsForceTouchCanceled = false;
                FocusRectangles.this.mFocusEventListener.onCanceled();
                return;
            }
            Rectangle rectangle = (Rectangle) view.findViewById(R.id.rect);
            if (rectangle.getVisibility() == 0) {
                Iterator it = FocusRectangles.this.mFaceRectangles.values().iterator();
                while (it.hasNext()) {
                    if (((TaggedRectangle) it.next()).equals(view)) {
                        if (isTouchAreaOnTouchCapture(rectangle, motionEvent)) {
                            if (FocusRectangles.this.mFaceReflectChecker.isWaitingForFaceReflected()) {
                                FocusRectangles.this.mFaceReflectChecker.setFaceReflectCb(new FaceReflectedCallback() { // from class: com.sonyericsson.cameracommon.focusview.FocusRectangles.OnFaceRectTouchListener.1
                                    @Override // com.sonyericsson.cameracommon.focusview.FocusRectangles.FaceReflectedCallback
                                    public void onFaceReflected() {
                                        FocusRectangles.this.mFocusEventListener.onReleased();
                                    }
                                });
                                return;
                            } else {
                                FocusRectangles.this.mFocusEventListener.onReleased();
                                return;
                            }
                        }
                        return;
                    }
                }
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.Rectangle.RectangleOnTouchListener
        public void onRectTouchCancel(View view, MotionEvent motionEvent) {
            if (FocusRectangles.this.mPressedRectangle == null || !FocusRectangles.this.mPressedRectangle.equals(view)) {
                return;
            }
            view.setPressed(false);
            FocusRectangles.this.mPressedRectangle = null;
            this.mIsForceTouchCanceled = false;
            FocusRectangles.this.mFocusEventListener.onCanceled();
        }

        private boolean isTouchAreaOnTouchCapture(View view, MotionEvent motionEvent) {
            if (!FocusRectangles.this.mIsFaceTouchCaptureEnabled || FocusRectangles.this.mCaptureArea == null) {
                return false;
            }
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            return CommonUtility.isEventContainedInView(FocusRectangles.this.mCaptureArea, new Point(((int) motionEvent.getX()) + rect.left, ((int) motionEvent.getY()) + rect.top));
        }

        @Override // com.sonyericsson.cameracommon.focusview.Rectangle.RectangleOnTouchListener
        public void onRectTouchLongPress(View view, MotionEvent motionEvent) {
            if (CamLog.VERBOSE) {
                CamLog.d("onRectTouchLongPress.");
            }
            if (FocusRectangles.this.mPressedRectangle == null || !FocusRectangles.this.mPressedRectangle.equals(view)) {
                return;
            }
            view.setPressed(true);
            FocusRectangles.this.mFocusEventListener.onLongPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideFaceRectangles(boolean z) {
        for (TaggedRectangle taggedRectangle : this.mFaceRectangles.values()) {
            if (z) {
                taggedRectangle.changeRectangleResource(0);
            }
            taggedRectangle.hide();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideTrackedObjectRecgantle() {
        this.mTrackedObjectRectangle.setVisibility(4);
    }

    private void removeTouchFocusRectAnimation() {
        ImageView imageView = (ImageView) this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
        this.mIsFocusAnimationEnabled = false;
        if (imageView.getAnimation() == null) {
            return;
        }
        this.mAnimation.cancelAfFocusAnimationTouch(imageView);
        imageView.clearAnimation();
        imageView.setAnimation(null);
    }

    private void removeSingleFocusRectAnimation() {
        ImageView imageView = (ImageView) this.mSingleAfRect.findViewById(R.id.center_auto_focus_rect);
        if (imageView.getAnimation() == null) {
            return;
        }
        this.mAnimation.cancelAfFocusAnimationSingle(imageView);
        imageView.clearAnimation();
        imageView.setAnimation(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeObjectFocusRectAnimation() {
        if (this.mTrackedObjectRectangle.getAnimation() == null) {
            return;
        }
        this.mAnimation.cancelAfFocusAnimationObject(this.mTrackedObjectRectangle);
        this.mTrackedObjectRectangle.clearAnimation();
        this.mTrackedObjectRectangle.setAnimation(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetRectanglesColor() {
        resetFaceRectangleColor();
        resetObjectTrackingRectangleColor(false);
        resetTouchFocusRectangleColor();
        resetSingleFocusRectangleColor();
    }

    private void resetFaceRectangleColor() {
        for (TaggedRectangle taggedRectangle : this.mFaceRectangles.values()) {
            taggedRectangle.changeRectangleResource(0);
            taggedRectangle.setSmileGaugeVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetObjectTrackingRectangleColor(boolean z) {
        this.mTrackedObjectRectangle.changeRectangleResource(z ? getSuccessIcon() : getNormalIcon());
    }

    private void resetTouchFocusRectangleColor() {
        ImageView imageView = (ImageView) this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
        imageView.setVisibility(0);
        this.mAnimation.startFocusAnimation(imageView, 2131230866);
    }

    private void resetSingleFocusRectangleColor() {
        ((ImageView) this.mSingleAfRect.findViewById(R.id.center_auto_focus_rect)).setBackgroundResource(2131230866);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFocusPositionInternal(Point point, FocusSetType focusSetType) {
        if (point == null) {
            this.mTouchAfRect.scrollTo(0, 0);
            return;
        }
        if (focusSetType == FocusSetType.FIRST) {
            hideFaceRectangles(true);
        }
        int iWidth = point.x;
        int iHeight = point.y;
        Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect(this.mActivity, this.mDevicePreviewWidth / this.mDevicePreviewHeight, this.mScreenAspect);
        FocusRectanglesAnimation.AnimationConfig touchAnimationConfig = this.mAnimation.getTouchAnimationConfig();
        if (iWidth < touchAnimationConfig.mToWidth / 2) {
            iWidth = touchAnimationConfig.mToWidth / 2;
        } else if (surfaceViewRect.right - (touchAnimationConfig.mToWidth / 2) < iWidth) {
            iWidth = surfaceViewRect.width() - (touchAnimationConfig.mToWidth / 2);
        }
        if (iHeight < touchAnimationConfig.mToHeight / 2) {
            iHeight = touchAnimationConfig.mToHeight / 2;
        } else if (surfaceViewRect.bottom - (touchAnimationConfig.mToHeight / 2) < iHeight) {
            iHeight = surfaceViewRect.height() - (touchAnimationConfig.mToHeight / 2);
        }
        this.mTouchAfRect.scrollTo((surfaceViewRect.width() / 2) - iWidth, (surfaceViewRect.height() / 2) - iHeight);
        this.mTouchAfRect.setVisibility(0);
        playTouchFocusStartAnimation(focusSetType);
    }

    private void playTouchFocusStartAnimation(FocusSetType focusSetType) {
        if (this.mIsFaceTouchCaptureEnabled) {
            return;
        }
        switch (focusSetType) {
            case FIRST:
                playOnTouchDownAnimationForTouchFocusRect();
                break;
            case RELEASE:
                playOnTouchUpAnimationForTouchFocusRect();
                break;
        }
    }

    private void playOnTouchDownAnimationForTouchFocusRect() {
        if (this.mTouchAfRect.getVisibility() == 0) {
            ImageView imageView = (ImageView) this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
            imageView.setBackgroundResource(2131230868);
            this.mIsFocusAnimationEnabled = true;
            imageView.setVisibility(4);
            this.mFocusAnimationTask = new Runnable() { // from class: com.sonyericsson.cameracommon.focusview.FocusRectangles.1
                @Override // java.lang.Runnable
                public void run() {
                    ImageView imageView2 = (ImageView) FocusRectangles.this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
                    imageView2.setVisibility(0);
                    if (FocusRectangles.this.mIsFocusAnimationEnabled) {
                        FocusRectangles.this.mAnimation.playTouchDownAnimation(imageView2);
                    } else {
                        FocusRectangles.this.mTouchAfRect.setVisibility(4);
                    }
                }
            };
            this.mHandler.postDelayed(this.mFocusAnimationTask, 100L);
        }
    }

    private void playOnTouchUpAnimationForTouchFocusRect() {
        if (this.mTouchAfRect.getVisibility() == 0) {
            if (this.mFocusAnimationTask != null) {
                this.mHandler.removeCallbacks(this.mFocusAnimationTask);
            }
            this.mFocusAnimationTask = new Runnable() { // from class: com.sonyericsson.cameracommon.focusview.FocusRectangles.2
                @Override // java.lang.Runnable
                public void run() {
                    ImageView imageView = (ImageView) FocusRectangles.this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
                    imageView.setVisibility(0);
                    if (FocusRectangles.this.mIsFocusAnimationEnabled) {
                        FocusRectangles.this.mAnimation.playTouchUpAnimation(imageView);
                    } else {
                        FocusRectangles.this.mTouchAfRect.setVisibility(4);
                    }
                }
            };
            this.mHandler.post(this.mFocusAnimationTask);
            this.mIsFocusAnimationEnabled = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onObjectTrackedInternal(CameraParameters.ObjectTrackingResult objectTrackingResult, boolean z) {
        int iCenterX;
        int iCenterY;
        if (objectTrackingResult.mIsLost) {
            this.mHandler.postDelayed(this.mRefreshTrackedObjectRectangleTask, 1000L);
            return;
        }
        this.mHandler.removeCallbacks(this.mRefreshTrackedObjectRectangleTask);
        Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect(this.mActivity, this.mDevicePreviewWidth / this.mDevicePreviewHeight, this.mScreenAspect);
        float fWidth = surfaceViewRect.width() / this.mDevicePreviewWidth;
        float fHeight = surfaceViewRect.height() / this.mDevicePreviewHeight;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            iCenterX = (int) ((this.mDevicePreviewWidth - objectTrackingResult.mRectOfTrackedObject.centerY()) * fWidth);
            iCenterY = (int) (objectTrackingResult.mRectOfTrackedObject.centerX() * fHeight);
        } else {
            iCenterX = (int) (objectTrackingResult.mRectOfTrackedObject.centerX() * fWidth);
            iCenterY = (int) (objectTrackingResult.mRectOfTrackedObject.centerY() * fHeight);
        }
        FocusRectanglesAnimation.AnimationConfig objectAnimationConfig = this.mAnimation.getObjectAnimationConfig();
        Rect rect = new Rect(iCenterX - (objectAnimationConfig.mFromWidth / 2), iCenterY - (objectAnimationConfig.mFromHeight / 2), iCenterX + (objectAnimationConfig.mFromWidth / 2), iCenterY + (objectAnimationConfig.mFromHeight / 2));
        this.mTrackedObjectRectangle.setRectImageSize(rect.centerX(), rect.centerY(), -2, -2);
        resetObjectTrackingRectangleColor(z);
        this.mTrackedObjectRectangle.setVisibility(0);
        this.mTrackedObjectRectangle.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeFacePriority(String str) {
        TaggedRectangle taggedRectangle = this.mFaceRectangles.get(str);
        if (taggedRectangle == null) {
            CamLog.e("changeFacePriority() faceUuid " + str + " not found.");
            return;
        }
        Rect rectConvertFromViewToActiveArray = PositionConverter.getInstance().convertFromViewToActiveArray(taggedRectangle.getFaceRect());
        Point point = new Point(rectConvertFromViewToActiveArray.centerX(), rectConvertFromViewToActiveArray.centerY());
        this.mFocusEventListener.onFaceSelected(point);
        this.mFaceReflectChecker.requestToWaitForFaceReflected(point);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void faceResultToRectangles(CameraParameters.FaceDetectionResult faceDetectionResult, boolean z, String str) {
        FaceInformationList faceInformationList = faceDetectionResult != null ? FaceDetectUtil.getFaceInformationList(faceDetectionResult, new Rect(0, 0, this.mDevicePreviewWidth, this.mDevicePreviewHeight), getSelectedFaceUuId(faceDetectionResult)) : null;
        this.mTouchEventDispatcher.updateFaceList(faceInformationList);
        if (faceInformationList == null || z) {
            return;
        }
        updateFaceRectangles(faceInformationList, str, this.mCurrentOrientation, z);
    }

    private class FaceReflectChecker {
        private static final long WAIT_FOR_FACE_REFLECTED_TIME_MILLIS = 500;
        private FaceReflectedCallback mCallback;
        private Point mFaceAreaTriggerPoint;
        private Runnable mTimeoutTask;

        private FaceReflectChecker() {
            this.mFaceAreaTriggerPoint = new Point(-1, -1);
            this.mTimeoutTask = new Runnable() { // from class: com.sonyericsson.cameracommon.focusview.FocusRectangles.FaceReflectChecker.1
                @Override // java.lang.Runnable
                public void run() {
                    FaceReflectChecker.this.notifyFaceReflected();
                }
            };
        }

        public void requestToWaitForFaceReflected(Point point) {
            if (FocusRectangles.this.mIsFaceTouchCaptureEnabled) {
                this.mFaceAreaTriggerPoint = point;
                FocusRectangles.this.mHandler.removeCallbacks(this.mTimeoutTask);
                FocusRectangles.this.mHandler.postDelayed(this.mTimeoutTask, WAIT_FOR_FACE_REFLECTED_TIME_MILLIS);
            }
        }

        public void check(CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (FaceDetectUtil.isValidFaceDetectionResult(faceDetectionResult)) {
                if (faceDetectionResult.extFaceList.get(faceDetectionResult.indexOfSelectedFace).rect.contains(this.mFaceAreaTriggerPoint.x, this.mFaceAreaTriggerPoint.y)) {
                    notifyFaceReflected();
                    return;
                }
                return;
            }
            notifyFaceReflected();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyFaceReflected() {
            this.mFaceAreaTriggerPoint.x = -1;
            this.mFaceAreaTriggerPoint.y = -1;
            if (this.mCallback != null) {
                this.mCallback.onFaceReflected();
                setFaceReflectCb(null);
            }
            FocusRectangles.this.mHandler.removeCallbacks(this.mTimeoutTask);
        }

        public boolean isWaitingForFaceReflected() {
            return this.mFaceAreaTriggerPoint.x >= 0 && this.mFaceAreaTriggerPoint.y >= 0;
        }

        public boolean isWaitingForCapturing() {
            return this.mCallback != null;
        }

        public void setFaceReflectCb(FaceReflectedCallback faceReflectedCallback) {
            this.mCallback = faceReflectedCallback;
        }
    }

    class RefreshTrackedObjectRectangleTask implements Runnable {
        RefreshTrackedObjectRectangleTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.VERBOSE) {
                CamLog.d("RefreshTrackedObjectRectangleTask.run():[IN]");
            }
            if (FocusRectangles.this.mFocusEventListener == null || FocusRectangles.this.mTrackedObjectRectangle == null) {
                return;
            }
            FocusRectangles.this.mTrackedObjectRectangle.setVisibility(4);
            FocusRectangles.this.onObjectRemoved();
        }
    }

    public boolean isTouchFocus() {
        return this.mCurrentState.getClass().equals(TouchFocusState.class);
    }

    public Rect getTouchFocusIconSize() {
        return new Rect(0, 0, this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_single_width), this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_single_height));
    }

    public void setOrientation(int i) {
        this.mCurrentOrientation = i;
    }

    public void setVisibility(int i) {
        this.mRectangles.setVisibility(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeState(State state) {
        if (CamLog.VERBOSE) {
            CamLog.d("changeState to: " + state.getClass().getSimpleName());
        }
        this.mCurrentState = state;
    }

    protected int getNormalIcon() {
        return this.mIsRecording ? this.mObjectTrackingRectSupported ? 2131230871 : 2131230865 : this.mObjectTrackingRectSupported ? 2131230871 : 2131230866;
    }

    protected int getTouchIcon() {
        return this.mIsRecording ? this.mObjectTrackingRectSupported ? 2131230870 : 2131230865 : this.mObjectTrackingRectSupported ? 2131230870 : 2131230866;
    }

    protected int getTouchAfFocusingIcon() {
        return this.mIsManualFocus ? 0 : 2131230866;
    }

    protected int getTouchAfSuccessIcon() {
        return this.mIsManualFocus ? 0 : 2131230865;
    }

    protected int getAfFocusingIcon() {
        return this.mObjectTrackingRectSupported ? 2131230871 : 2131230866;
    }

    protected int getSuccessIcon() {
        return this.mObjectTrackingRectSupported ? 2131230871 : 2131230865;
    }

    private void displayObjectTrackingFocusFrame(Rect rect) {
        this.mTrackedObjectRectangle.setScaleX(1.0f);
        this.mTrackedObjectRectangle.setScaleY(1.0f);
        this.mTrackedObjectRectangle.setRectImageSize(rect.centerX(), rect.centerY(), rect.width(), rect.height());
        this.mTrackedObjectRectangle.setVisibility(0);
        ViewGroup.LayoutParams layoutParams = this.mTrackedObjectRectangle.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        this.mTrackedObjectRectangle.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initObjectTrackingAnimation(CameraParameters.ObjectTrackingResult objectTrackingResult) throws Resources.NotFoundException {
        Rect rectConvertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(objectTrackingResult.mRectOfTrackedObject);
        this.mTrackedObjectRectangle.changeRectangleResource(getTouchIcon());
        this.mObJectTrackingFocusIconState = ObJectTrackingFocusIconState.TOUCH_ICON;
        int dimensionPixelSize = this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_object_width);
        int dimensionPixelSize2 = this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_object_height);
        int i = rectConvertFromActiveArrayToView.left;
        int i2 = rectConvertFromActiveArrayToView.top;
        int i3 = rectConvertFromActiveArrayToView.right;
        int i4 = (rectConvertFromActiveArrayToView.bottom - i2) / 2;
        int i5 = i + ((i3 - i) / 2);
        int i6 = dimensionPixelSize / 2;
        rectConvertFromActiveArrayToView.left = i5 - i6;
        int i7 = i2 + i4;
        int i8 = dimensionPixelSize2 / 2;
        rectConvertFromActiveArrayToView.top = i7 - i8;
        rectConvertFromActiveArrayToView.right = i5 + i6;
        rectConvertFromActiveArrayToView.bottom = i7 + i8;
        displayObjectTrackingFocusFrame(rectConvertFromActiveArrayToView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playObjectTrackingAnimation(CameraParameters.ObjectTrackingResult objectTrackingResult, boolean z) {
        Rect rect = new Rect(0, 0, this.mTrackedObjectRectangle.getRectImageWidth(), this.mTrackedObjectRectangle.getRectImageHeight());
        Rect rectConvertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(objectTrackingResult.mRectOfTrackedObject);
        resetObjectTrackingRectangleColor(z);
        displayObjectTrackingFocusFrame(rectConvertFromActiveArrayToView);
        if (this.mObJectTrackingFocusIconState == ObJectTrackingFocusIconState.TOUCH_ICON) {
            this.mObJectTrackingFocusIconState = ObJectTrackingFocusIconState.TRACKING_ICON;
            ObjectAnimator objectTrackingAnimator = getObjectTrackingAnimator(this.mTrackedObjectRectangle, 300, rect, rectConvertFromActiveArrayToView);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(objectTrackingAnimator);
            animatorSet.setInterpolator(new PathInterpolator(INTERPOLATOR_CONTROL_X1, 1.0f, INTERPOLATOR_CONTROL_X2, 1.0f));
            animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.sonyericsson.cameracommon.focusview.FocusRectangles.3
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    animator.removeAllListeners();
                }
            });
            animatorSet.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSelectedFaceUuId(CameraParameters.FaceDetectionResult faceDetectionResult) {
        if (faceDetectionResult.extFaceList.size() == 0) {
            return null;
        }
        return Integer.toString(faceDetectionResult.extFaceList.get(faceDetectionResult.indexOfSelectedFace).id);
    }

    private ObjectAnimator getObjectTrackingAnimator(TaggedRectangle taggedRectangle, int i, Rect rect, Rect rect2) {
        PathInterpolator pathInterpolator = new PathInterpolator(INTERPOLATOR_CONTROL_X1, 1.0f, INTERPOLATOR_CONTROL_X2, 1.0f);
        this.mTrackedObjectRectangle.setScaleX(rect.width() / rect2.width());
        this.mTrackedObjectRectangle.setScaleY(rect.height() / rect2.height());
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(taggedRectangle, PropertyValuesHolder.ofFloat(ANIMATION_SCALE_X, 1.0f), PropertyValuesHolder.ofFloat(ANIMATION_SCALE_Y, 1.0f));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(pathInterpolator);
        return objectAnimatorOfPropertyValuesHolder;
    }

    public void setObjectTrackingRectSupported(boolean z) {
        this.mObjectTrackingRectSupported = z;
    }

    public void setFocusIconType(boolean z) {
        this.mIsManualFocus = z;
    }

    public void setSmileCaptureThreshold(int i) {
        this.mSmileCaptureLevel = i;
    }
}
