// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

import com.sonyericsson.cameracommon.utility.CommonUtility;
import android.view.MotionEvent;
import java.util.Map;
import android.view.animation.Animation;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorSet;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.widget.RelativeLayout$LayoutParams;
import android.widget.ImageView;
import java.util.Iterator;
import android.animation.TimeInterpolator;
import android.animation.PropertyValuesHolder;
import android.view.animation.PathInterpolator;
import android.animation.ObjectAnimator;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.ViewGroup;
import android.view.ViewGroup$LayoutParams;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.graphics.Point;
import android.content.Context;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.widget.RelativeLayout;
import android.view.View$OnTouchListener;
import com.sonyericsson.android.camera.device.CameraParameters;
import android.os.Handler;
import java.util.HashMap;
import android.view.View;
import com.sonyericsson.cameracommon.animation.FocusRectanglesAnimation;
import android.app.Activity;

public class FocusRectangles
{
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
    private int mCurrentOrientation;
    private State mCurrentState;
    private int mDevicePreviewHeight;
    private int mDevicePreviewWidth;
    private HashMap<String, TaggedRectangle> mFaceRectangles;
    private FaceReflectChecker mFaceReflectChecker;
    private Runnable mFocusAnimationTask;
    private FocusActionListener mFocusEventListener;
    private Handler mHandler;
    private boolean mIsFaceTouchCaptureEnabled;
    private boolean mIsFocusAnimationEnabled;
    private boolean mIsManualFocus;
    private boolean mIsRecording;
    private CameraParameters.FaceDetectionResult mLastFaceDetectionResult;
    private String mLatestSelectedFaceUuid;
    private ObJectTrackingFocusIconState mObJectTrackingFocusIconState;
    private boolean mObjectTrackingRectSupported;
    private final OnFaceRectTouchListener mOnFaceRectTouchListener;
    private View$OnTouchListener mOnTouchListener;
    private TaggedRectangle mPressedRectangle;
    private RelativeLayout mRectangles;
    private final RefreshTrackedObjectRectangleTask mRefreshTrackedObjectRectangleTask;
    private LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private RelativeLayout mSingleAfRect;
    private int mSmileCaptureLevel;
    private int mSmileScore;
    private RelativeLayout mTouchAfRect;
    private RectangleTouchEventDispatcher mTouchEventDispatcher;
    private TaggedRectangle mTrackedObjectRectangle;
    
    public FocusRectangles(final Activity mActivity, final FocusActionListener mFocusEventListener, final int mDevicePreviewWidth, final int mDevicePreviewHeight, final FocusRectanglesViewList list, final View mCaptureArea, final View$OnTouchListener mOnTouchListener, final LayoutDependencyResolver.ScreenAspect mScreenAspect) {
        this.mHandler = new Handler();
        this.mPressedRectangle = null;
        this.mRefreshTrackedObjectRectangleTask = new RefreshTrackedObjectRectangleTask();
        this.mOnFaceRectTouchListener = new OnFaceRectTouchListener();
        this.mIsFaceTouchCaptureEnabled = false;
        this.mIsFocusAnimationEnabled = false;
        this.mCurrentState = (State)new DefaultFocusState();
        this.mIsRecording = false;
        this.mLatestSelectedFaceUuid = null;
        this.mCurrentOrientation = 2;
        this.mTouchEventDispatcher = null;
        this.mFocusAnimationTask = null;
        this.mSmileCaptureLevel = -1;
        this.mIsManualFocus = false;
        this.mObjectTrackingRectSupported = false;
        this.mObJectTrackingFocusIconState = ObJectTrackingFocusIconState.NOT_DISPLAY;
        this.mActivity = mActivity;
        this.mScreenAspect = mScreenAspect;
        this.mFocusEventListener = mFocusEventListener;
        this.mDevicePreviewWidth = mDevicePreviewWidth;
        this.mDevicePreviewHeight = mDevicePreviewHeight;
        this.mAnimation = new FocusRectanglesAnimation((Context)this.mActivity);
        this.mCaptureArea = mCaptureArea;
        this.mOnTouchListener = mOnTouchListener;
        this.initialize(list);
        this.mFaceReflectChecker = new FaceReflectChecker();
    }
    
    private TaggedRectangle addTaggedRectangle(final LayoutInflater layoutInflater, final String key, TaggedRectangle value) {
        if (this.mFaceRectangles.size() >= 5) {
            return null;
        }
        final Rect rect = new Rect();
        final ViewGroup$LayoutParams viewGroup$LayoutParams = new ViewGroup$LayoutParams(-1, -1);
        if (value == null) {
            value = (TaggedRectangle)layoutInflater.inflate(2131492923, (ViewGroup)null);
        }
        this.mRectangles.addView((View)value, viewGroup$LayoutParams);
        value.prepare(0);
        value.setRectPosition(rect.centerX(), rect.centerY(), rect.width(), rect.height());
        value.setRectangleOnTouchListener(this.mOnFaceRectTouchListener);
        this.mFaceRectangles.put(key, value);
        return value;
    }
    
    private void changeFacePriority(final String s) {
        final TaggedRectangle taggedRectangle = this.mFaceRectangles.get(s);
        if (taggedRectangle == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("changeFacePriority() faceUuid ");
            sb.append(s);
            sb.append(" not found.");
            CamLog.e(sb.toString());
            return;
        }
        final Rect convertFromViewToActiveArray = PositionConverter.getInstance().convertFromViewToActiveArray(taggedRectangle.getFaceRect());
        final Point point = new Point(convertFromViewToActiveArray.centerX(), convertFromViewToActiveArray.centerY());
        this.mFocusEventListener.onFaceSelected(point);
        this.mFaceReflectChecker.requestToWaitForFaceReflected(point);
    }
    
    private void changeState(final State mCurrentState) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("changeState to: ");
            sb.append(mCurrentState.getClass().getSimpleName());
            CamLog.d(sb.toString());
        }
        this.mCurrentState = mCurrentState;
    }
    
    private void displayObjectTrackingFocusFrame(final Rect rect) {
        this.mTrackedObjectRectangle.setScaleX(1.0f);
        this.mTrackedObjectRectangle.setScaleY(1.0f);
        this.mTrackedObjectRectangle.setRectImageSize(rect.centerX(), rect.centerY(), rect.width(), rect.height());
        this.mTrackedObjectRectangle.setVisibility(0);
        final ViewGroup$LayoutParams layoutParams = this.mTrackedObjectRectangle.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        this.mTrackedObjectRectangle.requestLayout();
    }
    
    private void faceResultToRectangles(final CameraParameters.FaceDetectionResult faceDetectionResult, final boolean b, final String s) {
        final Rect rect = new Rect(0, 0, this.mDevicePreviewWidth, this.mDevicePreviewHeight);
        FaceInformationList faceInformationList;
        if (faceDetectionResult != null) {
            faceInformationList = FaceDetectUtil.getFaceInformationList(faceDetectionResult, rect, this.getSelectedFaceUuId(faceDetectionResult));
        }
        else {
            faceInformationList = null;
        }
        this.mTouchEventDispatcher.updateFaceList(faceInformationList);
        if (faceInformationList == null) {
            return;
        }
        if (!b) {
            this.updateFaceRectangles(faceInformationList, s, this.mCurrentOrientation, b);
        }
    }
    
    private ObjectAnimator getObjectTrackingAnimator(final TaggedRectangle taggedRectangle, final int n, final Rect rect, final Rect rect2) {
        final PathInterpolator interpolator = new PathInterpolator(0.23f, 1.0f, 0.32f, 1.0f);
        this.mTrackedObjectRectangle.setScaleX(rect.width() / (float)rect2.width());
        this.mTrackedObjectRectangle.setScaleY(rect.height() / (float)rect2.height());
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)taggedRectangle, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("scaleX", new float[] { 1.0f }), PropertyValuesHolder.ofFloat("scaleY", new float[] { 1.0f }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)interpolator);
        return ofPropertyValuesHolder;
    }
    
    private String getSelectedFaceUuId(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        if (faceDetectionResult.extFaceList.size() == 0) {
            return null;
        }
        return Integer.toString(faceDetectionResult.extFaceList.get(faceDetectionResult.indexOfSelectedFace).id);
    }
    
    private void hideFaceRectangles(final boolean b) {
        for (final TaggedRectangle taggedRectangle : this.mFaceRectangles.values()) {
            if (b) {
                taggedRectangle.changeRectangleResource(0);
            }
            taggedRectangle.hide();
        }
    }
    
    private void hideTrackedObjectRecgantle() {
        this.mTrackedObjectRectangle.setVisibility(4);
    }
    
    private void initObjectTrackingAnimation(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
        final Rect convertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(objectTrackingResult.mRectOfTrackedObject);
        this.mTrackedObjectRectangle.changeRectangleResource(this.getTouchIcon());
        this.mObJectTrackingFocusIconState = ObJectTrackingFocusIconState.TOUCH_ICON;
        final int dimensionPixelSize = this.mActivity.getResources().getDimensionPixelSize(2131165336);
        final int dimensionPixelSize2 = this.mActivity.getResources().getDimensionPixelSize(2131165335);
        final int left = convertFromActiveArrayToView.left;
        final int top = convertFromActiveArrayToView.top;
        final int right = convertFromActiveArrayToView.right;
        final int bottom = convertFromActiveArrayToView.bottom;
        final int n = (right - left) / 2;
        final int n2 = (bottom - top) / 2;
        final int n3 = left + n;
        final int n4 = dimensionPixelSize / 2;
        convertFromActiveArrayToView.left = n3 - n4;
        final int n5 = top + n2;
        final int n6 = dimensionPixelSize2 / 2;
        convertFromActiveArrayToView.top = n5 - n6;
        convertFromActiveArrayToView.right = n3 + n4;
        convertFromActiveArrayToView.bottom = n5 + n6;
        this.displayObjectTrackingFocusFrame(convertFromActiveArrayToView);
    }
    
    private void initialize(final FocusRectanglesViewList list) {
        this.mRectangles = list.rectanglesContainer;
        final LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
        this.mFaceRectangles = new HashMap<String, TaggedRectangle>();
        this.mTouchEventDispatcher = new RectangleTouchEventDispatcher(this.mFaceRectangles);
        this.mRectangles.setOnTouchListener((View$OnTouchListener)this.mTouchEventDispatcher);
        View[] array;
        if (list.faceViewList != null) {
            array = list.faceViewList;
        }
        else {
            array = null;
        }
        for (int i = 0; i < 5; ++i) {
            TaggedRectangle taggedRectangle;
            if (array != null) {
                taggedRectangle = (TaggedRectangle)array[i];
            }
            else {
                taggedRectangle = null;
            }
            this.addTaggedRectangle(layoutInflater, Integer.toString(i), taggedRectangle);
        }
        this.mTrackedObjectRectangle = list.trackedObjectView;
        if (this.mTrackedObjectRectangle == null) {
            this.mTrackedObjectRectangle = (TaggedRectangle)layoutInflater.inflate(2131492923, (ViewGroup)null);
        }
        this.mTrackedObjectRectangle.setVisibility(4);
        this.mRectangles.addView((View)this.mTrackedObjectRectangle, new ViewGroup$LayoutParams(-1, -1));
        this.mTrackedObjectRectangle.prepare(3);
        this.mTrackedObjectRectangle.setRectImageSize(0, 0, this.mActivity.getResources().getDimensionPixelSize(2131165336), this.mActivity.getResources().getDimensionPixelSize(2131165335));
        ((ImageView)this.mTrackedObjectRectangle.findViewById(2131296528)).setOnTouchListener(this.mOnTouchListener);
        this.mSingleAfRect = list.singleAfView;
        if (this.mSingleAfRect == null) {
            this.mSingleAfRect = (RelativeLayout)layoutInflater.inflate(2131492924, (ViewGroup)null);
        }
        this.mSingleAfRect.setVisibility(4);
        this.mRectangles.addView((View)this.mSingleAfRect, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        this.mTouchAfRect = list.touchAfView;
        if (this.mTouchAfRect == null) {
            this.mTouchAfRect = (RelativeLayout)layoutInflater.inflate(2131492924, (ViewGroup)null);
        }
        this.mTouchAfRect.setVisibility(4);
        this.mRectangles.addView((View)this.mTouchAfRect, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        ((ImageView)this.mTouchAfRect.findViewById(2131296347)).setOnTouchListener(this.mOnTouchListener);
        this.updateRectanglesCoordinates();
    }
    
    private void onObjectTrackedInternal(final CameraParameters.ObjectTrackingResult objectTrackingResult, final boolean b) {
        if (objectTrackingResult.mIsLost) {
            this.mHandler.postDelayed((Runnable)this.mRefreshTrackedObjectRectangleTask, 1000L);
            return;
        }
        this.mHandler.removeCallbacks((Runnable)this.mRefreshTrackedObjectRectangleTask);
        final Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect((Context)this.mActivity, this.mDevicePreviewWidth / (float)this.mDevicePreviewHeight, this.mScreenAspect);
        final float n = surfaceViewRect.width() / (float)this.mDevicePreviewWidth;
        final float n2 = surfaceViewRect.height() / (float)this.mDevicePreviewHeight;
        int n3;
        int n4;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            n3 = (int)((this.mDevicePreviewWidth - objectTrackingResult.mRectOfTrackedObject.centerY()) * n);
            n4 = (int)(objectTrackingResult.mRectOfTrackedObject.centerX() * n2);
        }
        else {
            n3 = (int)(objectTrackingResult.mRectOfTrackedObject.centerX() * n);
            n4 = (int)(objectTrackingResult.mRectOfTrackedObject.centerY() * n2);
        }
        final FocusRectanglesAnimation.AnimationConfig objectAnimationConfig = this.mAnimation.getObjectAnimationConfig();
        final Rect rect = new Rect(n3 - objectAnimationConfig.mFromWidth / 2, n4 - objectAnimationConfig.mFromHeight / 2, n3 + objectAnimationConfig.mFromWidth / 2, n4 + objectAnimationConfig.mFromHeight / 2);
        this.mTrackedObjectRectangle.setRectImageSize(rect.centerX(), rect.centerY(), -2, -2);
        this.resetObjectTrackingRectangleColor(b);
        this.mTrackedObjectRectangle.setVisibility(0);
        this.mTrackedObjectRectangle.requestLayout();
    }
    
    private void playObjectTrackingAnimation(final CameraParameters.ObjectTrackingResult objectTrackingResult, final boolean b) {
        final Rect rect = new Rect(0, 0, this.mTrackedObjectRectangle.getRectImageWidth(), this.mTrackedObjectRectangle.getRectImageHeight());
        final Rect convertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(objectTrackingResult.mRectOfTrackedObject);
        this.resetObjectTrackingRectangleColor(b);
        this.displayObjectTrackingFocusFrame(convertFromActiveArrayToView);
        if (this.mObJectTrackingFocusIconState == ObJectTrackingFocusIconState.TOUCH_ICON) {
            this.mObJectTrackingFocusIconState = ObJectTrackingFocusIconState.TRACKING_ICON;
            final ObjectAnimator objectTrackingAnimator = this.getObjectTrackingAnimator(this.mTrackedObjectRectangle, 300, rect, convertFromActiveArrayToView);
            final AnimatorSet set = new AnimatorSet();
            set.playTogether(new Animator[] { (Animator)objectTrackingAnimator });
            set.setInterpolator((TimeInterpolator)new PathInterpolator(0.23f, 1.0f, 0.32f, 1.0f));
            set.addListener((Animator$AnimatorListener)new Animator$AnimatorListener(this) {
                final FocusRectangles this$0;
                
                public void onAnimationCancel(final Animator animator) {
                }
                
                public void onAnimationEnd(final Animator animator) {
                    animator.removeAllListeners();
                }
                
                public void onAnimationRepeat(final Animator animator) {
                }
                
                public void onAnimationStart(final Animator animator) {
                }
            });
            set.start();
        }
    }
    
    private void playOnTouchDownAnimationForTouchFocusRect() {
        if (this.mTouchAfRect.getVisibility() == 0) {
            final ImageView imageView = (ImageView)this.mTouchAfRect.findViewById(2131296347);
            imageView.setBackgroundResource(2131230868);
            this.mIsFocusAnimationEnabled = true;
            imageView.setVisibility(4);
            this.mFocusAnimationTask = new Runnable(this) {
                final FocusRectangles this$0;
                
                @Override
                public void run() {
                    final ImageView imageView = (ImageView)this.this$0.mTouchAfRect.findViewById(2131296347);
                    imageView.setVisibility(0);
                    if (this.this$0.mIsFocusAnimationEnabled) {
                        this.this$0.mAnimation.playTouchDownAnimation((View)imageView);
                    }
                    else {
                        this.this$0.mTouchAfRect.setVisibility(4);
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
            this.mFocusAnimationTask = new Runnable(this) {
                final FocusRectangles this$0;
                
                @Override
                public void run() {
                    final ImageView imageView = (ImageView)this.this$0.mTouchAfRect.findViewById(2131296347);
                    imageView.setVisibility(0);
                    if (this.this$0.mIsFocusAnimationEnabled) {
                        this.this$0.mAnimation.playTouchUpAnimation((View)imageView);
                    }
                    else {
                        this.this$0.mTouchAfRect.setVisibility(4);
                    }
                }
            };
            this.mHandler.post(this.mFocusAnimationTask);
            this.mIsFocusAnimationEnabled = true;
        }
    }
    
    private void playTouchFocusStartAnimation(final FocusSetType focusSetType) {
        if (!this.mIsFaceTouchCaptureEnabled) {
            switch (FocusRectangles$4.$SwitchMap$com$sonyericsson$cameracommon$focusview$FocusRectangles$FocusSetType[focusSetType.ordinal()]) {
                case 2: {
                    this.playOnTouchUpAnimationForTouchFocusRect();
                    break;
                }
                case 1: {
                    this.playOnTouchDownAnimationForTouchFocusRect();
                    break;
                }
            }
        }
    }
    
    private void removeObjectFocusRectAnimation() {
        if (this.mTrackedObjectRectangle.getAnimation() == null) {
            return;
        }
        this.mAnimation.cancelAfFocusAnimationObject((View)this.mTrackedObjectRectangle);
        this.mTrackedObjectRectangle.clearAnimation();
        this.mTrackedObjectRectangle.setAnimation((Animation)null);
    }
    
    private void removeSingleFocusRectAnimation() {
        final ImageView imageView = (ImageView)this.mSingleAfRect.findViewById(2131296347);
        if (imageView.getAnimation() == null) {
            return;
        }
        this.mAnimation.cancelAfFocusAnimationSingle((View)imageView);
        imageView.clearAnimation();
        imageView.setAnimation((Animation)null);
    }
    
    private void removeTouchFocusRectAnimation() {
        final ImageView imageView = (ImageView)this.mTouchAfRect.findViewById(2131296347);
        this.mIsFocusAnimationEnabled = false;
        if (imageView.getAnimation() == null) {
            return;
        }
        this.mAnimation.cancelAfFocusAnimationTouch((View)imageView);
        imageView.clearAnimation();
        imageView.setAnimation((Animation)null);
    }
    
    private void resetFaceRectangleColor() {
        for (final TaggedRectangle taggedRectangle : this.mFaceRectangles.values()) {
            taggedRectangle.changeRectangleResource(0);
            taggedRectangle.setSmileGaugeVisibility(4);
        }
    }
    
    private void resetObjectTrackingRectangleColor(final boolean b) {
        int n;
        if (b) {
            n = this.getSuccessIcon();
        }
        else {
            n = this.getNormalIcon();
        }
        this.mTrackedObjectRectangle.changeRectangleResource(n);
    }
    
    private void resetRectanglesColor() {
        this.resetFaceRectangleColor();
        this.resetObjectTrackingRectangleColor(false);
        this.resetTouchFocusRectangleColor();
        this.resetSingleFocusRectangleColor();
    }
    
    private void resetSingleFocusRectangleColor() {
        ((ImageView)this.mSingleAfRect.findViewById(2131296347)).setBackgroundResource(2131230866);
    }
    
    private void resetTouchFocusRectangleColor() {
        final ImageView imageView = (ImageView)this.mTouchAfRect.findViewById(2131296347);
        imageView.setVisibility(0);
        this.mAnimation.startFocusAnimation((View)imageView, 2131230866);
    }
    
    private void setAFLocking(final boolean b) {
        if (b) {
            this.mCurrentState.handleStartAfLock();
        }
        else {
            this.mCurrentState.handleStopAfLock();
        }
        this.setEnableFaceFocusTouch(b ^ true);
    }
    
    private void setFocusPositionInternal(final Point point, final FocusSetType focusSetType) {
        if (point == null) {
            this.mTouchAfRect.scrollTo(0, 0);
            return;
        }
        if (focusSetType == FocusSetType.FIRST) {
            this.hideFaceRectangles(true);
        }
        final int x = point.x;
        final int y = point.y;
        final Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect((Context)this.mActivity, this.mDevicePreviewWidth / (float)this.mDevicePreviewHeight, this.mScreenAspect);
        final FocusRectanglesAnimation.AnimationConfig touchAnimationConfig = this.mAnimation.getTouchAnimationConfig();
        int n;
        if (x < touchAnimationConfig.mToWidth / 2) {
            n = touchAnimationConfig.mToWidth / 2;
        }
        else if (surfaceViewRect.right - touchAnimationConfig.mToWidth / 2 < (n = x)) {
            n = surfaceViewRect.width() - touchAnimationConfig.mToWidth / 2;
        }
        int n2;
        if (y < touchAnimationConfig.mToHeight / 2) {
            n2 = touchAnimationConfig.mToHeight / 2;
        }
        else if (surfaceViewRect.bottom - touchAnimationConfig.mToHeight / 2 < (n2 = y)) {
            n2 = surfaceViewRect.height() - touchAnimationConfig.mToHeight / 2;
        }
        this.mTouchAfRect.scrollTo(surfaceViewRect.width() / 2 - n, surfaceViewRect.height() / 2 - n2);
        this.mTouchAfRect.setVisibility(0);
        this.playTouchFocusStartAnimation(focusSetType);
    }
    
    private void setRectSizeAndPosition(final RelativeLayout relativeLayout, final int leftMargin, final int topMargin, final int width, final int height) {
        final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)relativeLayout.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.leftMargin = leftMargin;
            layoutParams.topMargin = topMargin;
            layoutParams.width = width;
            layoutParams.height = height;
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.LANDSCAPE) {
                layoutParams.addRule(15, -1);
                layoutParams.removeRule(14);
            }
            else {
                layoutParams.removeRule(15);
                layoutParams.addRule(14, -1);
            }
            relativeLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
    }
    
    private void updateFaceRectangles(final FaceInformationList list, String str, final int n, final boolean b) {
        String userSelectedUuid = str;
        if (str == null) {
            str = (userSelectedUuid = list.getUserSelectedUuid());
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("updateFaceRectangles: uuid is not specified, use uuid from API, uuid = ");
                sb.append(str);
                CamLog.d(sb.toString());
                userSelectedUuid = str;
            }
        }
        final Iterator<TaggedRectangle> iterator = this.mFaceRectangles.values().iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            final TaggedRectangle taggedRectangle = iterator.next();
            if (taggedRectangle == null) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("updateFaceRectangles: view is null, index = ");
                    sb2.append(n2);
                    CamLog.d(sb2.toString());
                }
                ++n2;
            }
            else {
                taggedRectangle.clearUpdated();
                if (n2 < list.getNamedFaceList().size()) {
                    final NamedFace namedFace = list.getNamedFace(n2);
                    if (namedFace == null) {
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("updateFaceRectangles: namedFace is null, index = ");
                            sb3.append(n2);
                            CamLog.d(sb3.toString());
                        }
                        ++n2;
                        continue;
                    }
                    this.updateRectangle(taggedRectangle, namedFace, n, b);
                    if (namedFace.mUuid.equals(userSelectedUuid)) {
                        taggedRectangle.changeRectangleResource(2131230869);
                        taggedRectangle.stopAnimation();
                        if (this.mSmileCaptureLevel == -1) {
                            taggedRectangle.setSmileGaugeVisibility(4);
                        }
                        else {
                            taggedRectangle.setSmileLevel(this.mSmileCaptureLevel);
                            taggedRectangle.setSmileGaugeVisibility(0);
                            this.updateSmileGauge(taggedRectangle, list, this.mSmileCaptureLevel, n);
                        }
                        this.mLatestSelectedFaceUuid = taggedRectangle.getUuid();
                    }
                    else {
                        taggedRectangle.setSmileGaugeVisibility(4);
                    }
                }
                else {
                    taggedRectangle.hide();
                    taggedRectangle.update(null, n);
                }
                ++n2;
            }
        }
    }
    
    private void updateFaceRectanglesData(final CameraParameters.FaceDetectionResult faceDetectionResult, final boolean b) {
        this.updateFaceRectanglesData(faceDetectionResult, b, null);
    }
    
    private void updateFaceRectanglesData(final CameraParameters.FaceDetectionResult mLastFaceDetectionResult, final boolean b, final String s) {
        this.faceResultToRectangles(this.mLastFaceDetectionResult = mLastFaceDetectionResult, b, s);
        if (this.mFaceReflectChecker.isWaitingForFaceReflected()) {
            this.mFaceReflectChecker.check(mLastFaceDetectionResult);
        }
    }
    
    private void updateRectangle(final TaggedRectangle taggedRectangle, final NamedFace namedFace, final int n, final boolean b) {
        final Rect convertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(namedFace.mFacePosition);
        boolean b2 = true;
        final StringBuilder sb = new StringBuilder();
        sb.append("Converted rectangle: ");
        sb.append(convertFromActiveArrayToView);
        CamLog.d(sb.toString());
        taggedRectangle.setRectPosition(convertFromActiveArrayToView.centerX(), convertFromActiveArrayToView.centerY(), convertFromActiveArrayToView.width(), convertFromActiveArrayToView.height());
        if (b) {
            taggedRectangle.changeRectangleResource(0);
            taggedRectangle.hide();
        }
        else {
            taggedRectangle.changeRectangleResource(2131230868);
        }
        if (taggedRectangle.getVisibility() != 0) {
            b2 = false;
        }
        if (!b2) {
            taggedRectangle.startRectangleAnimation(n);
        }
        taggedRectangle.update(namedFace.mUuid, n);
        taggedRectangle.setUpdated();
        if (taggedRectangle.getVisibility() != 0) {
            taggedRectangle.requestLayout();
            taggedRectangle.setVisibility(0);
        }
    }
    
    private void updateRectanglesCoordinates() {
        final Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect((Context)this.mActivity, this.mDevicePreviewWidth / (float)this.mDevicePreviewHeight, this.mScreenAspect);
        this.setRectSizeAndPosition(this.mRectangles, surfaceViewRect.left, surfaceViewRect.top, surfaceViewRect.width(), surfaceViewRect.height());
        final Iterator<String> iterator = this.mFaceRectangles.keySet().iterator();
        while (iterator.hasNext()) {
            this.mFaceRectangles.get(iterator.next()).setSize(surfaceViewRect.width(), surfaceViewRect.height());
        }
        this.changeState((State)new DefaultFocusState());
    }
    
    private void updateSmileGauge(final TaggedRectangle taggedRectangle, final FaceInformationList list, final int smileLevel, final int n) {
        final NamedFace namedFaceByUuid = list.getNamedFaceByUuid(taggedRectangle.getUuid());
        if (namedFaceByUuid == null) {
            return;
        }
        final Rect convertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(namedFaceByUuid.mFacePosition);
        taggedRectangle.setSmileGaugesPosition(convertFromActiveArrayToView.left, convertFromActiveArrayToView.top, convertFromActiveArrayToView.right, convertFromActiveArrayToView.bottom, n);
        taggedRectangle.setSmileLevel(smileLevel);
        taggedRectangle.setSmileScore(namedFaceByUuid.mSmileScore);
        this.mSmileScore = namedFaceByUuid.mSmileScore;
    }
    
    public void clearAllFocus() {
        this.clearAllFocusExceptFace();
        this.clearFaceDetection();
    }
    
    public void clearAllFocusExceptFace() {
        this.mCurrentState.handleClearAllFocusExceptFace();
    }
    
    public void clearExceptTouchFocus() {
        this.mCurrentState.handleClearExceptTouchFocus();
    }
    
    public void clearFaceDetection() {
        this.hideFaceRectangles(false);
        this.resetFaceRectangleColor();
        this.mTouchEventDispatcher.updateFaceList(null);
    }
    
    public void clearObjectTracking() {
        this.mCurrentState.handleClearObjectTracking();
    }
    
    public void clearSingleAutoFocus() {
        this.mSingleAfRect.setVisibility(4);
        this.removeSingleFocusRectAnimation();
        this.resetSingleFocusRectangleColor();
    }
    
    public void clearTouchFocus() {
        this.changeState((State)new DefaultFocusState());
        this.setFocusPositionInternal(null, null);
        this.mTouchAfRect.setVisibility(4);
        this.removeTouchFocusRectAnimation();
        this.resetTouchFocusRectangleColor();
    }
    
    public void clearTouched() {
        this.mOnFaceRectTouchListener.clearTouched();
    }
    
    public void disableFaceTouchCapture() {
        this.mIsFaceTouchCaptureEnabled = false;
    }
    
    public void enableFaceTouchCapture() {
        this.mIsFaceTouchCaptureEnabled = true;
    }
    
    protected int getAfFocusingIcon() {
        int n;
        if (this.mObjectTrackingRectSupported) {
            n = 2131230871;
        }
        else {
            n = 2131230866;
        }
        return n;
    }
    
    protected int getNormalIcon() {
        final boolean mIsRecording = this.mIsRecording;
        int n = 2131230871;
        if (mIsRecording) {
            if (!this.mObjectTrackingRectSupported) {
                n = 2131230865;
            }
            return n;
        }
        if (!this.mObjectTrackingRectSupported) {
            n = 2131230866;
        }
        return n;
    }
    
    public int getSelectedFaceSmileScore() {
        final int mSmileScore = this.mSmileScore;
        this.mSmileScore = 0;
        return mSmileScore;
    }
    
    protected int getSuccessIcon() {
        int n;
        if (this.mObjectTrackingRectSupported) {
            n = 2131230871;
        }
        else {
            n = 2131230865;
        }
        return n;
    }
    
    protected int getTouchAfFocusingIcon() {
        int n;
        if (this.mIsManualFocus) {
            n = 0;
        }
        else {
            n = 2131230866;
        }
        return n;
    }
    
    protected int getTouchAfSuccessIcon() {
        int n;
        if (this.mIsManualFocus) {
            n = 0;
        }
        else {
            n = 2131230865;
        }
        return n;
    }
    
    public Rect getTouchFocusIconSize() {
        return new Rect(0, 0, this.mActivity.getResources().getDimensionPixelSize(2131165338), this.mActivity.getResources().getDimensionPixelSize(2131165337));
    }
    
    protected int getTouchIcon() {
        final boolean mIsRecording = this.mIsRecording;
        int n = 2131230870;
        if (mIsRecording) {
            if (!this.mObjectTrackingRectSupported) {
                n = 2131230865;
            }
            return n;
        }
        if (!this.mObjectTrackingRectSupported) {
            n = 2131230866;
        }
        return n;
    }
    
    public boolean isTouchFocus() {
        return this.mCurrentState.getClass().equals(TouchFocusState.class);
    }
    
    public void onAutoFocusCanceled() {
        this.mCurrentState.handleOnAutoFocusCanceled();
    }
    
    public void onAutoFocusDone(final boolean b) {
        this.mCurrentState.handleOnAutoFocusDone(b);
    }
    
    public void onAutoFocusStarted() {
        this.mCurrentState.handleOnAutoFocusStarted();
    }
    
    public void onFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        this.mCurrentState.handleOnFaceDetected(faceDetectionResult);
    }
    
    public void onObjectFocused() {
        this.mTrackedObjectRectangle.changeRectangleResource(2131230871);
    }
    
    public void onObjectLost() {
        if (this.mObjectTrackingRectSupported) {
            this.mTrackedObjectRectangle.changeRectangleResource(2131230872);
        }
        this.mCurrentState.handleOnObjectLost();
    }
    
    public void onObjectRemoved() {
        this.mCurrentState.handleOnObjectRemoved();
    }
    
    public void onObjectTracked(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
        this.mCurrentState.handleOnTrackedObjectStateUpdated(objectTrackingResult);
    }
    
    public void onUiComponentOverlaid() {
        this.mCurrentState.handleOnUiComponentOverlaid();
    }
    
    public void onUiComponentRemoved() {
        this.mCurrentState.handleOnUiComponentRemoved();
    }
    
    public void release() {
        this.mActivity = null;
        this.mFocusEventListener = null;
    }
    
    public void reset() {
        this.stopRecording();
        this.setAFLocking(false);
        this.setLockedBySelfTimer(false);
    }
    
    public void setEnableFaceFocusTouch(final boolean b) {
        if (this.mFaceRectangles == null) {
            return;
        }
        final Iterator<Map.Entry<String, TaggedRectangle>> iterator = this.mFaceRectangles.entrySet().iterator();
        while (iterator.hasNext()) {
            final TaggedRectangle taggedRectangle = ((Map.Entry<K, TaggedRectangle>)iterator.next()).getValue();
            Rectangle.RectangleOnTouchListener mOnFaceRectTouchListener;
            if (!b && !taggedRectangle.isPressed()) {
                mOnFaceRectTouchListener = null;
            }
            else {
                mOnFaceRectTouchListener = this.mOnFaceRectTouchListener;
            }
            taggedRectangle.setRectangleOnTouchListener(mOnFaceRectTouchListener);
        }
    }
    
    public void setFocusIconType(final boolean mIsManualFocus) {
        this.mIsManualFocus = mIsManualFocus;
    }
    
    public void setFocusPosition(Point point, final FocusSetType focusSetType) {
        final int[] array = new int[2];
        this.mRectangles.getLocationOnScreen(array);
        point = new Point(point.x - array[0], point.y - array[1]);
        this.mCurrentState.handleSetFocusPosition(point, focusSetType);
    }
    
    public void setLockedBySelfTimer(final boolean b) {
        this.setEnableFaceFocusTouch(b ^ true);
    }
    
    public void setObjectTrackingRectSupported(final boolean mObjectTrackingRectSupported) {
        this.mObjectTrackingRectSupported = mObjectTrackingRectSupported;
    }
    
    public void setOrientation(final int mCurrentOrientation) {
        this.mCurrentOrientation = mCurrentOrientation;
    }
    
    public void setSmileCaptureThreshold(final int mSmileCaptureLevel) {
        this.mSmileCaptureLevel = mSmileCaptureLevel;
    }
    
    public void setVisibility(final int visibility) {
        this.mRectangles.setVisibility(visibility);
    }
    
    public void startFaceDetection() {
        this.mCurrentState.handleStartFaceDetection();
    }
    
    public void startObjectTracking() {
        this.mObJectTrackingFocusIconState = ObJectTrackingFocusIconState.NOT_DISPLAY;
        this.mCurrentState.handleStartObjectTracking();
    }
    
    public void startRecording() {
        this.mIsRecording = true;
    }
    
    public void stopRecording() {
        this.mIsRecording = false;
    }
    
    public void updateDevicePreviewSize(final int mDevicePreviewWidth, final int mDevicePreviewHeight) {
        this.mDevicePreviewWidth = mDevicePreviewWidth;
        this.mDevicePreviewHeight = mDevicePreviewHeight;
        this.updateRectanglesCoordinates();
    }
    
    class DefaultFocusInLockedState extends DefaultFocusState
    {
        final FocusRectangles this$0;
        
        DefaultFocusInLockedState(final FocusRectangles this$0) {
            this.this$0 = this$0.super();
        }
        
        @Override
        public void handleOnFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        }
        
        @Override
        public void handleStartFaceDetection() {
        }
    }
    
    class DefaultFocusState implements State
    {
        final FocusRectangles this$0;
        
        DefaultFocusState(final FocusRectangles this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void handleClearAllFocusExceptFace() {
            this.this$0.changeState((State)this.this$0.new DefaultFocusState());
            this.this$0.clearSingleAutoFocus();
            this.this$0.clearTouchFocus();
            this.this$0.clearObjectTracking();
        }
        
        @Override
        public void handleClearExceptTouchFocus() {
            this.this$0.clearSingleAutoFocus();
            this.this$0.clearObjectTracking();
            this.this$0.clearFaceDetection();
        }
        
        @Override
        public void handleClearObjectTracking() {
            this.this$0.hideTrackedObjectRecgantle();
            this.this$0.removeObjectFocusRectAnimation();
            this.this$0.resetObjectTrackingRectangleColor(false);
        }
        
        @Override
        public void handleOnAutoFocusCanceled() {
        }
        
        @Override
        public void handleOnAutoFocusDone(final boolean b) {
            final ImageView imageView = (ImageView)this.this$0.mSingleAfRect.findViewById(2131296347);
            if (b) {
                if (imageView.getVisibility() != 0) {
                    imageView.setVisibility(0);
                }
                imageView.setBackgroundResource(2131230865);
                this.this$0.mAnimation.playAfFocusInAnimationSingle((View)imageView);
            }
            else if (imageView.getVisibility() == 0) {
                imageView.setVisibility(4);
            }
        }
        
        @Override
        public void handleOnAutoFocusStarted() {
            this.this$0.setAFLocking(true);
            this.this$0.mSingleAfRect.setVisibility(0);
            final View viewById = this.this$0.mSingleAfRect.findViewById(2131296347);
            if (viewById.getVisibility() != 0) {
                viewById.setVisibility(0);
            }
            this.this$0.mAnimation.startFocusAnimation(viewById, 2131230866);
        }
        
        @Override
        public void handleOnFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (faceDetectionResult.extFaceList.size() == 0) {
                this.this$0.changeState((State)this.this$0.new DefaultFocusState());
                return;
            }
            this.this$0.updateFaceRectanglesData(faceDetectionResult, false);
            this.this$0.changeState((State)new FaceDetectionState());
        }
        
        @Override
        public void handleOnObjectLost() {
        }
        
        @Override
        public void handleOnObjectRemoved() {
        }
        
        @Override
        public void handleOnTrackedObjectStateUpdated(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
        }
        
        @Override
        public void handleOnUiComponentOverlaid() {
            this.this$0.mSingleAfRect.setVisibility(4);
            this.this$0.hideFaceRectangles(false);
            this.this$0.hideTrackedObjectRecgantle();
            this.this$0.mTouchAfRect.setVisibility(4);
        }
        
        @Override
        public void handleOnUiComponentRemoved() {
            this.this$0.mTouchAfRect.setVisibility(4);
            this.this$0.mSingleAfRect.setVisibility(4);
            this.this$0.resetRectanglesColor();
        }
        
        @Override
        public void handleSetFocusPosition(final Point point, final FocusSetType focusSetType) {
            this.this$0.setFocusPositionInternal(point, focusSetType);
            this.this$0.changeState((State)new TouchFocusState());
        }
        
        @Override
        public void handleStartAfLock() {
            this.this$0.changeState((State)this.this$0.new DefaultFocusInLockedState());
        }
        
        @Override
        public void handleStartFaceDetection() {
            this.this$0.clearSingleAutoFocus();
            this.this$0.clearObjectTracking();
            this.this$0.changeState((State)new FaceDetectionState());
        }
        
        @Override
        public void handleStartObjectTracking() {
            this.this$0.clearExceptTouchFocus();
            this.this$0.changeState((State)new ObjectTrackingState());
        }
        
        @Override
        public void handleStopAfLock() {
            this.this$0.changeState((State)this.this$0.new DefaultFocusState());
        }
    }
    
    private interface State
    {
        void handleClearAllFocusExceptFace();
        
        void handleClearExceptTouchFocus();
        
        void handleClearObjectTracking();
        
        void handleOnAutoFocusCanceled();
        
        void handleOnAutoFocusDone(final boolean p0);
        
        void handleOnAutoFocusStarted();
        
        void handleOnFaceDetected(final CameraParameters.FaceDetectionResult p0);
        
        void handleOnObjectLost();
        
        void handleOnObjectRemoved();
        
        void handleOnTrackedObjectStateUpdated(final CameraParameters.ObjectTrackingResult p0);
        
        void handleOnUiComponentOverlaid();
        
        void handleOnUiComponentRemoved();
        
        void handleSetFocusPosition(final Point p0, final FocusSetType p1);
        
        void handleStartAfLock();
        
        void handleStartFaceDetection();
        
        void handleStartObjectTracking();
        
        void handleStopAfLock();
    }
    
    class FaceDetectionInLockedState extends FaceDetectionState
    {
        final FocusRectangles this$0;
        
        FaceDetectionInLockedState(final FocusRectangles this$0) {
        }
        
        @Override
        public void handleOnFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (faceDetectionResult.extFaceList.size() == 0) {
                this.this$0.mLatestSelectedFaceUuid = null;
            }
            if (this.this$0.mLatestSelectedFaceUuid == null) {
                return;
            }
            this.this$0.updateFaceRectanglesData(faceDetectionResult, false, this.this$0.getSelectedFaceUuId(faceDetectionResult));
        }
    }
    
    private class FaceDetectionState extends DefaultFocusState
    {
        final FocusRectangles this$0;
        
        private FaceDetectionState(final FocusRectangles this$0) {
            this.this$0 = this$0.super();
        }
        
        private boolean isFaceRectAvailable() {
            final Iterator iterator = this.this$0.mFaceRectangles.values().iterator();
            while (iterator.hasNext()) {
                if (((TaggedRectangle)iterator.next()).getVisibility() == 0) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public void handleOnAutoFocusDone(final boolean b) {
            if (!this.isFaceRectAvailable()) {
                super.handleOnAutoFocusDone(b);
                return;
            }
            if (this.this$0.mLatestSelectedFaceUuid == null) {
                return;
            }
            final Iterator iterator = this.this$0.mFaceRectangles.entrySet().iterator();
            while (iterator.hasNext()) {
                final TaggedRectangle taggedRectangle = ((Map.Entry<K, TaggedRectangle>)iterator.next()).getValue();
                if (this.this$0.mLatestSelectedFaceUuid.equals(taggedRectangle.getUuid())) {
                    taggedRectangle.changeRectangleResource(2131230869);
                    if (taggedRectangle.isShown()) {
                        continue;
                    }
                    taggedRectangle.setVisibility(0);
                }
                else {
                    taggedRectangle.setVisibility(4);
                }
            }
        }
        
        @Override
        public void handleOnAutoFocusStarted() {
            if (!this.isFaceRectAvailable()) {
                this.this$0.changeState((State)this.this$0.new DefaultFocusState());
                this.this$0.onAutoFocusStarted();
                return;
            }
            this.this$0.setAFLocking(true);
        }
        
        @Override
        public void handleOnFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (faceDetectionResult.extFaceList.size() == 0) {
                this.this$0.clearFaceDetection();
                this.this$0.changeState((State)this.this$0.new DefaultFocusState());
                return;
            }
            this.this$0.updateFaceRectanglesData(faceDetectionResult, false);
        }
        
        @Override
        public void handleOnObjectRemoved() {
        }
        
        @Override
        public void handleOnTrackedObjectStateUpdated(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
            this.this$0.mSingleAfRect.setVisibility(4);
            this.this$0.hideFaceRectangles(true);
            this.this$0.onObjectTrackedInternal(objectTrackingResult, false);
            this.this$0.changeState((State)new ObjectTrackingState());
        }
        
        @Override
        public void handleSetFocusPosition(final Point point, final FocusSetType focusSetType) {
            if (focusSetType != FocusSetType.FIRST) {
                return;
            }
            this.this$0.setFocusPositionInternal(point, focusSetType);
            this.this$0.changeState((State)new TouchFocusState());
        }
        
        @Override
        public void handleStartAfLock() {
            this.this$0.changeState((State)this.this$0.new FaceDetectionInLockedState());
        }
        
        @Override
        public void handleStartFaceDetection() {
        }
        
        @Override
        public void handleStartObjectTracking() {
            this.this$0.clearSingleAutoFocus();
            this.this$0.clearTouchFocus();
            this.this$0.clearFaceDetection();
            this.this$0.changeState((State)new ObjectTrackingState());
        }
        
        @Override
        public void handleStopAfLock() {
            final FocusRectangles this$0 = this.this$0;
            State state;
            if (this.this$0.mLatestSelectedFaceUuid != null) {
                state = this.this$0.new FaceDetectionState();
            }
            else {
                state = this.this$0.new DefaultFocusState();
            }
            this$0.changeState(state);
        }
    }
    
    private class FaceReflectChecker
    {
        private static final long WAIT_FOR_FACE_REFLECTED_TIME_MILLIS = 500L;
        private FaceReflectedCallback mCallback;
        private Point mFaceAreaTriggerPoint;
        private Runnable mTimeoutTask;
        final FocusRectangles this$0;
        
        private FaceReflectChecker(final FocusRectangles this$0) {
            this.this$0 = this$0;
            this.mFaceAreaTriggerPoint = new Point(-1, -1);
            this.mTimeoutTask = new Runnable() {
                final FaceReflectChecker this$1;
                
                @Override
                public void run() {
                    this.this$1.notifyFaceReflected();
                }
            };
        }
        
        private void notifyFaceReflected() {
            this.mFaceAreaTriggerPoint.x = -1;
            this.mFaceAreaTriggerPoint.y = -1;
            if (this.mCallback != null) {
                this.mCallback.onFaceReflected();
                this.setFaceReflectCb(null);
            }
            this.this$0.mHandler.removeCallbacks(this.mTimeoutTask);
        }
        
        public void check(final CameraParameters.FaceDetectionResult faceDetectionResult) {
            if (FaceDetectUtil.isValidFaceDetectionResult(faceDetectionResult)) {
                if (faceDetectionResult.extFaceList.get(faceDetectionResult.indexOfSelectedFace).rect.contains(this.mFaceAreaTriggerPoint.x, this.mFaceAreaTriggerPoint.y)) {
                    this.notifyFaceReflected();
                }
            }
            else {
                this.notifyFaceReflected();
            }
        }
        
        public boolean isWaitingForCapturing() {
            return this.mCallback != null;
        }
        
        public boolean isWaitingForFaceReflected() {
            return this.mFaceAreaTriggerPoint.x >= 0 && this.mFaceAreaTriggerPoint.y >= 0;
        }
        
        public void requestToWaitForFaceReflected(final Point mFaceAreaTriggerPoint) {
            if (!this.this$0.mIsFaceTouchCaptureEnabled) {
                return;
            }
            this.mFaceAreaTriggerPoint = mFaceAreaTriggerPoint;
            this.this$0.mHandler.removeCallbacks(this.mTimeoutTask);
            this.this$0.mHandler.postDelayed(this.mTimeoutTask, 500L);
        }
        
        public void setFaceReflectCb(final FaceReflectedCallback mCallback) {
            this.mCallback = mCallback;
        }
    }
    
    private interface FaceReflectedCallback
    {
        void onFaceReflected();
    }
    
    public enum FocusSetType
    {
        private static final FocusSetType[] $VALUES;
        
        FIRST, 
        MOVE, 
        RELEASE;
        
        static {
            $VALUES = new FocusSetType[] { FocusSetType.FIRST, FocusSetType.MOVE, FocusSetType.RELEASE };
        }
    }
    
    private enum ObJectTrackingFocusIconState
    {
        private static final ObJectTrackingFocusIconState[] $VALUES;
        
        NOT_DISPLAY, 
        TOUCH_ICON, 
        TRACKING_ICON;
        
        static {
            $VALUES = new ObJectTrackingFocusIconState[] { ObJectTrackingFocusIconState.NOT_DISPLAY, ObJectTrackingFocusIconState.TOUCH_ICON, ObJectTrackingFocusIconState.TRACKING_ICON };
        }
    }
    
    class ObjectLostInLockedState extends ObjectTrackingInLockedState
    {
        final FocusRectangles this$0;
        
        ObjectLostInLockedState(final FocusRectangles this$0) {
            this.this$0 = this$0.super();
        }
        
        @Override
        public void handleClearAllFocusExceptFace() {
            this.this$0.changeState((State)this.this$0.new ObjectTrackingStoppedInLockedState());
        }
        
        @Override
        public void handleOnAutoFocusDone(final boolean b) {
        }
        
        @Override
        public void handleOnTrackedObjectStateUpdated(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
        }
        
        @Override
        public void handleStartAfLock() {
        }
        
        @Override
        public void handleStopAfLock() {
            this.this$0.changeState((State)this.this$0.new ObjectLostState());
        }
    }
    
    class ObjectTrackingInLockedState extends ObjectTrackingState
    {
        final FocusRectangles this$0;
        
        ObjectTrackingInLockedState(final FocusRectangles this$0) {
        }
        
        @Override
        public void handleClearAllFocusExceptFace() {
        }
        
        @Override
        public void handleClearObjectTracking() {
        }
        
        @Override
        public void handleOnAutoFocusDone(final boolean b) {
            if (!b) {
                return;
            }
            super.handleOnAutoFocusDone(b);
        }
        
        @Override
        public void handleOnFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        }
        
        @Override
        public void handleOnObjectLost() {
            this.this$0.changeState((State)this.this$0.new ObjectLostInLockedState());
        }
        
        @Override
        public void handleOnObjectRemoved() {
        }
        
        @Override
        public void handleStartFaceDetection() {
        }
        
        @Override
        protected boolean isAFLocking() {
            return true;
        }
    }
    
    private class ObjectTrackingState extends DefaultFocusState
    {
        final FocusRectangles this$0;
        
        private ObjectTrackingState(final FocusRectangles this$0) {
            this.this$0 = this$0.super();
        }
        
        @Override
        public void handleOnAutoFocusDone(final boolean b) {
            this.this$0.mTrackedObjectRectangle.setVisibility(0);
            if (b) {
                this.this$0.mTrackedObjectRectangle.changeRectangleResource(this.this$0.getSuccessIcon());
            }
            else {
                this.this$0.mAnimation.playAfFadeOutAnimationObject((View)this.this$0.mTrackedObjectRectangle);
            }
        }
        
        @Override
        public void handleOnAutoFocusStarted() {
            this.this$0.setAFLocking(true);
            this.this$0.mAnimation.startFocusAnimation(this.this$0.mTrackedObjectRectangle.findViewById(2131296528), this.this$0.getAfFocusingIcon());
        }
        
        @Override
        public void handleOnFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
            this.this$0.updateFaceRectanglesData(faceDetectionResult, true);
        }
        
        @Override
        public void handleOnObjectLost() {
            this.this$0.changeState((State)this.this$0.new ObjectLostState());
        }
        
        @Override
        public void handleOnObjectRemoved() {
            this.this$0.changeState((State)this.this$0.new DefaultFocusState());
        }
        
        @Override
        public void handleOnTrackedObjectStateUpdated(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
            if (this.this$0.mObjectTrackingRectSupported) {
                if (this.this$0.mObJectTrackingFocusIconState == ObJectTrackingFocusIconState.NOT_DISPLAY) {
                    this.this$0.initObjectTrackingAnimation(objectTrackingResult);
                }
                else {
                    this.this$0.playObjectTrackingAnimation(objectTrackingResult, this.isAFLocking());
                }
            }
            else {
                this.this$0.onObjectTrackedInternal(objectTrackingResult, this.isAFLocking());
            }
        }
        
        @Override
        public void handleSetFocusPosition(final Point point, final FocusSetType focusSetType) {
            this.this$0.setFocusPositionInternal(point, focusSetType);
            this.this$0.changeState((State)new TouchFocusState());
        }
        
        @Override
        public void handleStartAfLock() {
            this.this$0.changeState((State)this.this$0.new ObjectTrackingInLockedState());
        }
        
        @Override
        public void handleStartFaceDetection() {
            this.this$0.clearExceptTouchFocus();
            this.this$0.changeState((State)new FaceDetectionState());
        }
        
        @Override
        public void handleStartObjectTracking() {
        }
        
        @Override
        public void handleStopAfLock() {
            this.this$0.changeState((State)this.this$0.new ObjectTrackingState());
        }
        
        protected boolean isAFLocking() {
            return false;
        }
    }
    
    class ObjectLostState extends ObjectTrackingState
    {
        final FocusRectangles this$0;
        
        ObjectLostState(final FocusRectangles this$0) {
        }
        
        @Override
        public void handleOnTrackedObjectStateUpdated(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
            this.this$0.changeState((State)new ObjectTrackingState());
        }
        
        @Override
        public void handleStartAfLock() {
            this.this$0.changeState((State)this.this$0.new ObjectLostInLockedState());
        }
        
        @Override
        public void handleStopAfLock() {
            this.this$0.changeState((State)this.this$0.new ObjectLostState());
        }
    }
    
    class ObjectTrackingStoppedInLockedState extends ObjectLostInLockedState
    {
        final FocusRectangles this$0;
        
        ObjectTrackingStoppedInLockedState(final FocusRectangles this$0) {
            this.this$0 = this$0.super();
        }
        
        @Override
        public void handleClearAllFocusExceptFace() {
        }
        
        @Override
        public void handleStopAfLock() {
            this.this$0.hideTrackedObjectRecgantle();
            this.this$0.removeObjectFocusRectAnimation();
            this.this$0.resetObjectTrackingRectangleColor(false);
            this.this$0.changeState((State)this.this$0.new DefaultFocusState());
        }
    }
    
    class OnFaceRectTouchListener implements RectangleOnTouchListener
    {
        private boolean mIsForceTouchCanceled;
        final FocusRectangles this$0;
        
        OnFaceRectTouchListener(final FocusRectangles this$0) {
            this.this$0 = this$0;
            this.mIsForceTouchCanceled = false;
        }
        
        private boolean isTouchAreaOnTouchCapture(final View view, final MotionEvent motionEvent) {
            if (this.this$0.mIsFaceTouchCaptureEnabled && this.this$0.mCaptureArea != null) {
                final Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                return CommonUtility.isEventContainedInView(this.this$0.mCaptureArea, new Point((int)motionEvent.getX() + rect.left, (int)motionEvent.getY() + rect.top));
            }
            return false;
        }
        
        protected void clearTouched() {
            this.mIsForceTouchCanceled = true;
        }
        
        @Override
        public void onRectTouchCancel(final View obj, final MotionEvent motionEvent) {
            if (this.this$0.mPressedRectangle != null && this.this$0.mPressedRectangle.equals(obj)) {
                obj.setPressed(false);
                this.this$0.mPressedRectangle = null;
                this.mIsForceTouchCanceled = false;
                this.this$0.mFocusEventListener.onCanceled();
            }
        }
        
        @Override
        public void onRectTouchDown(final View obj, final MotionEvent motionEvent) {
            if (this.this$0.mPressedRectangle != null) {
                return;
            }
            if (this.this$0.mFaceReflectChecker.isWaitingForCapturing()) {
                return;
            }
            final Rectangle rectangle = (Rectangle)obj.findViewById(2131296527);
            if (rectangle.getVisibility() == 0) {
                for (final Map.Entry<K, TaggedRectangle> entry : this.this$0.mFaceRectangles.entrySet()) {
                    final TaggedRectangle taggedRectangle = entry.getValue();
                    if (taggedRectangle.equals(obj)) {
                        obj.setPressed(false);
                        this.this$0.mPressedRectangle = taggedRectangle;
                        taggedRectangle.startRectanglePressAnimation();
                        final boolean b = this.this$0.mLatestSelectedFaceUuid != null && this.this$0.mLatestSelectedFaceUuid.equals(taggedRectangle.getUuid());
                        this.this$0.faceResultToRectangles(this.this$0.mLastFaceDetectionResult, false, null);
                        this.this$0.changeFacePriority((String)entry.getKey());
                        if (this.isTouchAreaOnTouchCapture((View)rectangle, motionEvent) && b) {
                            this.this$0.mFocusEventListener.onTouched();
                            break;
                        }
                        break;
                    }
                }
            }
        }
        
        @Override
        public void onRectTouchLongPress(final View obj, final MotionEvent motionEvent) {
            if (CamLog.VERBOSE) {
                CamLog.d("onRectTouchLongPress.");
            }
            if (this.this$0.mPressedRectangle != null && this.this$0.mPressedRectangle.equals(obj)) {
                obj.setPressed(true);
                this.this$0.mFocusEventListener.onLongPressed();
            }
        }
        
        @Override
        public void onRectTouchUp(final View view, final MotionEvent motionEvent) {
            if (this.this$0.mPressedRectangle == null || !this.this$0.mPressedRectangle.equals(view)) {
                return;
            }
            view.setPressed(false);
            this.this$0.mPressedRectangle = null;
            if (this.mIsForceTouchCanceled) {
                this.mIsForceTouchCanceled = false;
                this.this$0.mFocusEventListener.onCanceled();
                return;
            }
            final Rectangle rectangle = (Rectangle)view.findViewById(2131296527);
            if (rectangle.getVisibility() == 0) {
                final Iterator iterator = this.this$0.mFaceRectangles.values().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().equals(view)) {
                        if (!this.isTouchAreaOnTouchCapture((View)rectangle, motionEvent)) {
                            break;
                        }
                        if (this.this$0.mFaceReflectChecker.isWaitingForFaceReflected()) {
                            this.this$0.mFaceReflectChecker.setFaceReflectCb(new FaceReflectedCallback(this) {
                                final OnFaceRectTouchListener this$1;
                                
                                @Override
                                public void onFaceReflected() {
                                    this.this$1.this$0.mFocusEventListener.onReleased();
                                }
                            });
                            break;
                        }
                        this.this$0.mFocusEventListener.onReleased();
                        break;
                    }
                }
            }
        }
    }
    
    class RefreshTrackedObjectRectangleTask implements Runnable
    {
        final FocusRectangles this$0;
        
        RefreshTrackedObjectRectangleTask(final FocusRectangles this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (CamLog.VERBOSE) {
                CamLog.d("RefreshTrackedObjectRectangleTask.run():[IN]");
            }
            if (this.this$0.mFocusEventListener != null && this.this$0.mTrackedObjectRectangle != null) {
                this.this$0.mTrackedObjectRectangle.setVisibility(4);
                this.this$0.onObjectRemoved();
            }
        }
    }
    
    class TouchFocusInLockedState extends TouchFocusState
    {
        final FocusRectangles this$0;
        
        TouchFocusInLockedState(final FocusRectangles this$0) {
        }
        
        @Override
        public void handleOnFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        }
        
        @Override
        public void handleStartFaceDetection() {
        }
    }
    
    private class TouchFocusState extends DefaultFocusState
    {
        private boolean mIsAutoFocusStarted;
        final FocusRectangles this$0;
        
        private TouchFocusState(final FocusRectangles this$0) {
            this.this$0 = this$0.super();
            this.mIsAutoFocusStarted = false;
        }
        
        @Override
        public void handleClearExceptTouchFocus() {
            this.this$0.mAnimation.stopFocusAnimation(this.this$0.mTouchAfRect.findViewById(2131296347));
            super.handleClearExceptTouchFocus();
        }
        
        @Override
        public void handleOnAutoFocusCanceled() {
            this.mIsAutoFocusStarted = false;
            ((ImageView)this.this$0.mTouchAfRect.findViewById(2131296347)).setVisibility(0);
        }
        
        @Override
        public void handleOnAutoFocusDone(final boolean b) {
            this.mIsAutoFocusStarted = false;
            this.this$0.setAFLocking(true);
            final ImageView imageView = (ImageView)this.this$0.mTouchAfRect.findViewById(2131296347);
            imageView.setVisibility(0);
            if (b) {
                imageView.setBackgroundResource(this.this$0.getTouchAfSuccessIcon());
                this.this$0.mAnimation.playAfFocusInAnimationTouch((View)imageView, this.this$0.getTouchAfSuccessIcon());
            }
            else if (!this.this$0.mIsManualFocus) {
                this.this$0.mAnimation.playAfFadeOutAnimationTouch((View)imageView);
            }
        }
        
        @Override
        public void handleOnAutoFocusStarted() {
            this.mIsAutoFocusStarted = true;
            this.this$0.updateFaceRectanglesData(null, true);
            this.this$0.mAnimation.startFocusAnimation(this.this$0.mTouchAfRect.findViewById(2131296347), this.this$0.getTouchAfFocusingIcon());
        }
        
        @Override
        public void handleOnFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
            final FocusRectangles this$0 = this.this$0;
            if (this.mIsAutoFocusStarted) {
                faceDetectionResult = null;
            }
            this$0.updateFaceRectanglesData(faceDetectionResult, true);
        }
        
        @Override
        public void handleOnObjectRemoved() {
        }
        
        @Override
        public void handleOnTrackedObjectStateUpdated(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
        }
        
        @Override
        public void handleOnUiComponentRemoved() {
            this.this$0.mTouchAfRect.setVisibility(0);
            this.this$0.mSingleAfRect.setVisibility(4);
            this.this$0.resetRectanglesColor();
        }
        
        @Override
        public void handleSetFocusPosition(final Point point, final FocusSetType focusSetType) {
            this.mIsAutoFocusStarted = false;
            this.this$0.setFocusPositionInternal(point, focusSetType);
        }
        
        @Override
        public void handleStartAfLock() {
            this.this$0.changeState((State)this.this$0.new TouchFocusInLockedState());
        }
        
        @Override
        public void handleStartFaceDetection() {
            this.this$0.clearSingleAutoFocus();
            this.this$0.clearObjectTracking();
            this.this$0.changeState((State)new FaceDetectionState());
        }
        
        @Override
        public void handleStartObjectTracking() {
            this.this$0.clearExceptTouchFocus();
            this.this$0.changeState((State)new ObjectTrackingState());
        }
        
        @Override
        public void handleStopAfLock() {
            this.this$0.changeState((State)this.this$0.new TouchFocusState());
        }
    }
}
