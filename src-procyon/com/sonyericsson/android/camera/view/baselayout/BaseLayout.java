// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout;

import android.graphics.Canvas;
import android.view.Display;
import android.graphics.Point;
import android.view.MotionEvent;
import com.sonyericsson.cameracommon.utility.AccessibilityHelper;
import android.widget.RelativeLayout$LayoutParams;
import com.sonyericsson.android.camera.view.overlaycontrol.SemiAutoControl;
import com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor;
import com.sonyericsson.android.camera.view.overlaycontrol.ImageQualityControl;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.view.overlaycontrol.EnumValueAccessor;
import com.sonyericsson.android.camera.setting.UiControlSettings;
import android.view.Window;
import com.sonyericsson.android.camera.SideTouchEventDetector;
import com.sonyericsson.android.camera.debug.SideTouchEmulateViewFactory;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.view.View$OnTouchListener;
import android.graphics.drawable.Animatable2$AnimationCallback;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import com.sonyericsson.android.camera.view.BurstCountView;
import android.util.DisplayMetrics;
import android.view.WindowManager$LayoutParams;
import android.graphics.drawable.Drawable;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.ImageView;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.device.CameraInfo;
import java.util.List;
import com.sonyericsson.cameracommon.contentsview.ContentsContainer;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import java.util.Collection;
import java.util.LinkedList;
import com.sonyericsson.cameracommon.contentsview.ContentLoader;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.view.ViewGroup$MarginLayoutParams;
import android.content.Context;
import android.app.Activity;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar;
import android.graphics.Rect;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonyericsson.android.camera.view.SuperSlowMotionTriggerAnimationController;
import com.sonyericsson.android.camera.view.baselayout.indicators.IconTextIndicator;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingIndicator;
import com.sonyericsson.android.camera.view.PrimaryShortcutGroup;
import com.sonyericsson.android.camera.view.PredictiveCaptureIndicatorController;
import android.view.View;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer;
import com.sonyericsson.android.camera.view.baselayout.settingshortcut.ModeSelectorButton;
import com.sonyericsson.android.camera.view.baselayout.indicators.IconIndicator;
import com.sonyericsson.android.camera.view.baselayout.indicators.LowBatteryIndicator;
import com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl;
import android.view.ViewGroup;
import com.sonyericsson.cameracommon.viewfinder.GridLineView;
import com.sonyericsson.cameracommon.viewfinder.indicators.GeotagIndicator;
import com.sonyericsson.android.camera.view.FrontAngleSwitchButton;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButton;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.view.AutoReviewController;
import com.sonyericsson.android.camera.CameraActivity;

public class BaseLayout
{
    public static final LazyInitializer EMPTY_LAZY_INITIALIZER;
    private static final String TAG = "BaseLayout";
    private static IsTalkbackEffective mIsTalkbackEffective;
    private final CameraActivity mActivity;
    private AutoReviewController mAutoReview;
    private FrameLayout mCapturingButtonLayout;
    private Indicator mConditionIndicator;
    private ContentsViewController mContentsViewController;
    private int mCurrentOrientation;
    private OnScreenButton mExtraButton;
    private FrontAngleSwitchButton mFrontAngleSwitchButton;
    private GeotagIndicator mGeoTag;
    private ViewFinderGestureDetector mGestureDetector;
    private GridLineView mGridLineView;
    private ViewGroup mHeadUpDisplay;
    private FrameLayout mHeadUpDisplayContainer;
    private FrameLayout mHintTextViewContainer;
    private LazyInitializer<OverlayControl> mImageQualityControl;
    private boolean mIsBlackScreenShowing;
    private boolean mIsCameraSwitching;
    private boolean mIsFirstDrawn;
    private FrameLayout mLazyInflatedUiComponentContainerFront;
    private FrameLayout mLazyInflatedUiComponentContainerFullScreen;
    private final LowBatteryIndicator mLowBattery;
    private final IconIndicator mLowInternalStorage;
    private final IconIndicator mLowSdCard;
    private ModeSelectorButton mModeButtonShortcut;
    private MruButtonContainer mMruButtonContainer;
    private NavigationBarVisibility mNavigationBarVisibility;
    private OnScreenButtonGroup mOnScreenButtonGroup;
    private final IconIndicator mPhotoSmileCapture;
    private View mPreInflatedHeadUpDisplay;
    private PredictiveCaptureIndicatorController mPredictiveCaptureIndicatorController;
    private FrameLayout mPredictiveLaunchCoverContainer;
    private PredictiveLaunchCoverView mPredictiveLaunchCoverView;
    private View mPreferredFocusView;
    private View mPreview;
    private PreviewContainerLayout mPreviewContainerLayout;
    private PrimaryShortcutGroup mPrimaryShortcut;
    private RecordingIndicator mRecordingIndicator;
    private ViewGroup mRootView;
    private IconTextIndicator mSceneIndicator;
    private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private LazyInitializer<OverlayControl> mSemiAutoControl;
    private OnScreenButton mSubButton;
    private final SuperSlowMotionTriggerAnimationController mSuperSlowMotionTriggerAnimation;
    private FrameLayout mSwitchAnimationContainer;
    private SwitchAnimationView mSwitchAnimationView;
    private Indicator mThermal;
    private View mTopIndicatorsContainer;
    private TutorialController mTutorial;
    private final IconIndicator mVideoSmileCapture;
    private Rect mViewFinderRect;
    private View mWindowCover;
    private Zoombar mZoombar;
    private FrameLayout mZoombarGroup;
    
    static {
        EMPTY_LAZY_INITIALIZER = (LazyInitializer)new LazyInitializer<OverlayControl>() {
            public OverlayControl initView() {
                return null;
            }
        };
        BaseLayout.mIsTalkbackEffective = IsTalkbackEffective.UNKNOWN;
    }
    
    public BaseLayout(final CameraActivity mActivity, final LayoutDependencyResolver.ScreenAspect mScreenAspect) {
        this.mLazyInflatedUiComponentContainerFront = null;
        this.mLazyInflatedUiComponentContainerFullScreen = null;
        this.mSemiAutoControl = BaseLayout.EMPTY_LAZY_INITIALIZER;
        this.mImageQualityControl = BaseLayout.EMPTY_LAZY_INITIALIZER;
        int i = 0;
        this.mIsBlackScreenShowing = false;
        this.mCurrentOrientation = 0;
        this.mViewFinderRect = null;
        this.mGridLineView = null;
        this.mSwitchAnimationView = null;
        this.mIsCameraSwitching = false;
        this.mIsFirstDrawn = false;
        this.mNavigationBarVisibility = null;
        this.mActivity = mActivity;
        this.mScreenAspect = mScreenAspect;
        if (PerfLog.IS_ENABLE) {
            this.mRootView = (ViewGroup)new RootViewForRefLogEnabled(this.mActivity);
        }
        else {
            this.mRootView = (ViewGroup)new RootView(this.mActivity);
        }
        this.mViewFinderRect = LayoutDependencyResolver.getViewFinderSize((Context)mActivity);
        for (View[] container = this.createContainer((Context)this.mActivity); i < container.length; ++i) {
            this.mRootView.addView(container[i]);
        }
        this.setupPreferredFocusView();
        this.setupPreviewContainer();
        this.setupSwitchAnimationContainer();
        this.mLowBattery = new LowBatteryIndicator((Context)mActivity, "low-battery");
        this.mLowInternalStorage = new IconIndicator("low-internal-storage");
        this.mLowSdCard = new IconIndicator("low-sd-card");
        this.mPhotoSmileCapture = new IconIndicator("photo-smile-capture");
        this.mVideoSmileCapture = new IconIndicator("video-smile-capture");
        this.mSuperSlowMotionTriggerAnimation = new SuperSlowMotionTriggerAnimationController();
        LayoutDependencyResolver.requestToDimSystemUi((View)this.mRootView);
    }
    
    private void adjustmentWithSystemLayout() {
        final int navigationBarMargin = LayoutDependencyResolver.getNavigationBarMargin((Context)this.mActivity);
        if (navigationBarMargin <= 0) {
            return;
        }
        final int[] array2;
        final int[] array = array2 = new int[4];
        array2[0] = 2131296426;
        array2[1] = 2131296611;
        array2[2] = 2131296313;
        array2[3] = 2131296438;
        for (int length = array.length, i = 0; i < length; ++i) {
            ((ViewGroup$MarginLayoutParams)((ViewGroup)this.mHeadUpDisplay.findViewById(array[i])).getLayoutParams()).setMargins(0, 0, navigationBarMargin, 0);
        }
    }
    
    private View[] createContainer(@NonNull final Context context) {
        final View[] array = new View[ViewRootChild.values().length];
        this.mPreferredFocusView = new View(context);
        this.mPreviewContainerLayout = new PreviewContainerLayout(context);
        this.mSwitchAnimationContainer = new FrameLayout(context);
        this.mCapturingButtonLayout = (FrameLayout)LayoutInflater.from(context).inflate(2131492906, (ViewGroup)null);
        this.mHeadUpDisplayContainer = new FrameLayout(context);
        this.mPredictiveLaunchCoverContainer = new FrameLayout(context);
        array[ViewRootChild.PREFERRED_FOCUS.getIndex()] = this.mPreferredFocusView;
        array[ViewRootChild.PREVIEW_CONTAINER_LAYOUT.getIndex()] = (View)this.mPreviewContainerLayout;
        array[ViewRootChild.SWITCH_ANIMATION_CONTAINER.getIndex()] = (View)this.mSwitchAnimationContainer;
        array[ViewRootChild.CAPTURE_BUTTON_LAYOUT.getIndex()] = (View)this.mCapturingButtonLayout;
        array[ViewRootChild.HEAD_UP_DISPLAY_CONTAINER.getIndex()] = (View)this.mHeadUpDisplayContainer;
        array[ViewRootChild.PREDICTIVE_LAUNCH_COVER_CONTAINER.getIndex()] = (View)this.mPredictiveLaunchCoverContainer;
        return array;
    }
    
    private NavigationBarVisibility getPreviousNavigationBarVisibility() {
        return this.mNavigationBarVisibility;
    }
    
    private void inflate() {
        final LayoutInflater from = LayoutInflater.from((Context)this.mActivity);
        if (this.mPreInflatedHeadUpDisplay != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("HeadUpDisplay is already inflated.");
            }
            this.mHeadUpDisplay = (ViewGroup)this.mPreInflatedHeadUpDisplay;
            this.mPreInflatedHeadUpDisplay = null;
        }
        else {
            if (CamLog.VERBOSE) {
                CamLog.d("HeadUpDisplay is not inflated.");
            }
            this.mHeadUpDisplay = (ViewGroup)from.inflate(2131492899, (ViewGroup)null);
        }
        this.mHeadUpDisplayContainer.addView((View)this.mHeadUpDisplay);
        this.mHeadUpDisplay.getLayoutParams().width = this.mViewFinderRect.width();
        this.mHeadUpDisplay.getLayoutParams().height = this.mViewFinderRect.height();
        this.mLazyInflatedUiComponentContainerFront = (FrameLayout)this.mActivity.findViewById(2131296438);
        this.mLazyInflatedUiComponentContainerFullScreen = (FrameLayout)this.mActivity.findViewById(2131296441);
    }
    
    private void setupCaptureButtonGroup() {
        if (!this.setupCaptureButtonGroup(this.mActivity.findViewById(2131296342)) && CamLog.VERBOSE) {
            CamLog.w("setupCaptureButtonGroup: fails to setup");
        }
    }
    
    private boolean setupCaptureButtonGroup(final View view) {
        if (view instanceof OnScreenButtonGroup) {
            if (this.mOnScreenButtonGroup == null) {
                this.mOnScreenButtonGroup = (OnScreenButtonGroup)view;
            }
            this.mSubButton = (OnScreenButton)this.mOnScreenButtonGroup.findViewById(2131296632);
            this.mExtraButton = (OnScreenButton)this.mOnScreenButtonGroup.findViewById(2131296396);
            if (this.mGestureDetector != null) {
                this.mGestureDetector.addExclusiveView((View)this.mExtraButton);
                this.mGestureDetector.addExclusiveView((View)this.mSubButton);
                this.mGestureDetector.addExclusiveView(this.mOnScreenButtonGroup.findViewById(2131296460));
            }
            return true;
        }
        return false;
    }
    
    private void setupContentsView(final ContentPallet.ThumbnailStateListener thumbnailStateListener) {
        ContentLoader.SecurityLevel securityLevel;
        if (this.mActivity.isDeviceInSecurityLock()) {
            securityLevel = ContentLoader.SecurityLevel.NEWLY_ADDED_CONTENT_ONLY;
        }
        else {
            securityLevel = ContentLoader.SecurityLevel.NORMAL;
        }
        List list = null;
        if (!this.isCameraSwitching()) {
            if (this.mContentsViewController != null) {
                list = new LinkedList();
                list.addAll(this.mContentsViewController.getLocalContentInfo());
                this.mContentsViewController.release();
            }
            this.mContentsViewController = new ContentsViewController(this.mActivity, this.mActivity.getStorage(), securityLevel, thumbnailStateListener);
            if (list != null) {
                this.mContentsViewController.getLocalContentInfo().addAll(list);
            }
            this.mContentsViewController.setSensorOrientation(this.mCurrentOrientation);
            this.mContentsViewController.reload();
            this.addViewFinderGestureDetectorExclusiveView(this.mActivity.findViewById(2131296370));
        }
    }
    
    private void setupFrontAngleSwitchButton() {
        if (this.mFrontAngleSwitchButton == null && PlatformCapability.isSuperWideSupported(CameraInfo.CameraId.FRONT)) {
            this.mFrontAngleSwitchButton = (FrontAngleSwitchButton)this.mActivity.findViewById(2131296408);
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.mFrontAngleSwitchButton.getLayoutParams();
            layoutParams.rightMargin = this.calculateCaptureButtonAreaHeight();
            this.mFrontAngleSwitchButton.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
    }
    
    private void setupGridLineView() {
        if (this.mGridLineView == null) {
            (this.mGridLineView = new GridLineView((Context)this.mActivity)).setVisibility(4);
            final Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(this.mViewFinderRect);
            this.updateGridLine(rectAccordingToLayoutOrientation.width(), rectAccordingToLayoutOrientation.height());
            this.mPreviewContainerLayout.mPreviewContainer.addView((View)this.mGridLineView);
        }
    }
    
    private void setupModeShortcut() {
        this.mModeButtonShortcut = (ModeSelectorButton)this.mHeadUpDisplay.findViewById(2131296467);
        final boolean exists = ModeSelectorInternalMode.exists(this.getActivity().getLaunchCondition().getCapturingMode());
        this.mModeButtonShortcut.update(exists);
        (this.mMruButtonContainer = (MruButtonContainer)this.mHeadUpDisplay.findViewById(2131296474)).setAvailability(exists ^ true);
    }
    
    private void setupPreferredFocusView() {
        if (this.mPreferredFocusView != null) {
            this.mPreferredFocusView.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(1, 1));
            this.mPreferredFocusView.setFocusable(true);
            this.mPreferredFocusView.setFocusableInTouchMode(true);
            this.mPreferredFocusView.requestFocus();
        }
    }
    
    private void setupPreviewContainer() {
        final FrameLayout$LayoutParams layoutParams = new FrameLayout$LayoutParams(-1, -1);
        layoutParams.setLayoutDirection(0);
        final Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(this.mViewFinderRect);
        layoutParams.width = rectAccordingToLayoutOrientation.width();
        layoutParams.height = rectAccordingToLayoutOrientation.height();
        layoutParams.gravity = 80;
        this.mPreviewContainerLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.mCapturingButtonLayout.setVisibility(4);
    }
    
    private void setupPrimaryShortcut() {
        this.mPrimaryShortcut = (PrimaryShortcutGroup)this.mActivity.findViewById(2131296509);
    }
    
    private void setupRecordingIndicator() {
        if (this.mRecordingIndicator == null) {
            (this.mRecordingIndicator = (RecordingIndicator)this.mActivity.findViewById(2131296523)).setScreenAspect(this.mScreenAspect);
            this.mRecordingIndicator.setOrientation(this.mCurrentOrientation);
            this.mRecordingIndicator.setVisibility(8);
            this.mRecordingIndicator.prepareBeforeRecording(0);
        }
    }
    
    private void setupSceneIndicators() {
        (this.mSceneIndicator = new IconTextIndicator((ImageView)this.mActivity.findViewById(2131296551), (TextView)this.mActivity.findViewById(2131296552))).setSensorOrientation(this.mCurrentOrientation);
        (this.mConditionIndicator = new Indicator((ImageView)this.mActivity.findViewById(2131296550))).setSensorOrientation(this.mCurrentOrientation);
    }
    
    private void setupSettingIndicators() {
        (this.mGeoTag = new GeotagIndicator((ImageView)this.mActivity.findViewById(2131296410))).setSensorOrientation(this.mCurrentOrientation);
        (this.mThermal = new Indicator((ImageView)this.mActivity.findViewById(2131296658))).setSensorOrientation(this.mCurrentOrientation);
    }
    
    private void setupSwitchAnimationContainer() {
        this.mSwitchAnimationContainer.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
    }
    
    private void setupSwitchAnimationView() {
        if (this.mSwitchAnimationView == null) {
            this.mSwitchAnimationView = new SwitchAnimationView((Context)this.mActivity);
            this.mSwitchAnimationContainer.addView((View)this.mSwitchAnimationView);
        }
    }
    
    private void setupTopIndicators() {
        this.mTopIndicatorsContainer = this.mActivity.findViewById(2131296668);
        this.mLowBattery.setup((ViewStub)this.mTopIndicatorsContainer.findViewById(2131296455));
        this.mLowInternalStorage.setup((ViewStub)this.mTopIndicatorsContainer.findViewById(2131296457));
        this.mLowSdCard.setup((ViewStub)this.mTopIndicatorsContainer.findViewById(2131296459));
        this.mPhotoSmileCapture.setup((ViewStub)this.mTopIndicatorsContainer.findViewById(2131296494));
        this.mVideoSmileCapture.setup((ViewStub)this.mTopIndicatorsContainer.findViewById(2131296695));
    }
    
    private void setupTutorial() {
        if (this.mTutorial == null) {
            this.mTutorial = new TutorialController(this.getRootView(), this.getActivity().getWindow());
        }
    }
    
    private void setupZoombar() {
        if (this.mZoombarGroup == null) {
            this.mZoombarGroup = (FrameLayout)this.mActivity.getLayoutInflater().inflate(2131493030, (ViewGroup)null);
            (this.mZoombar = (Zoombar)this.mZoombarGroup.findViewById(2131296700)).setSensorOrientation(this.mCurrentOrientation);
            this.mZoombar.hideImmediately();
            this.getLazyInflatedUiComponentContainerBack().addView((View)this.mZoombarGroup);
        }
        this.repositionZoombar();
    }
    
    private void switchCaptureButtonGroupContainer() {
        if (this.mCapturingButtonLayout != null && this.mOnScreenButtonGroup != null) {
            final ViewGroup viewGroup = (ViewGroup)this.mHeadUpDisplay.findViewById(2131296341);
            if (viewGroup != null) {
                final ViewGroup viewGroup2 = (ViewGroup)this.mCapturingButtonLayout.findViewById(2131296500);
                if (viewGroup2 != null) {
                    viewGroup2.removeView((View)this.mOnScreenButtonGroup);
                    viewGroup.addView((View)this.mOnScreenButtonGroup);
                    this.mCapturingButtonLayout.setVisibility(8);
                    this.mCapturingButtonLayout = null;
                    if (!this.mIsBlackScreenShowing) {
                        this.mRootView.setBackground((Drawable)null);
                    }
                }
            }
        }
    }
    
    private void updateLayout() {
        if (CamLog.VERBOSE) {
            CamLog.d("updateLayout() is called.");
        }
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        final LayoutOrientationResolver.LayoutOrientationType orientation = LayoutOrientationResolver.getInstance().getOrientation();
        final LayoutOrientationResolver.LayoutOrientationType portrait = LayoutOrientationResolver.LayoutOrientationType.PORTRAIT;
        float translationY = 0.0f;
        int n;
        float translationX;
        if (orientation == portrait) {
            if (CamLog.VERBOSE) {
                CamLog.d("ActivityInfo.SCREEN_ORIENTATION_PORTRAIT");
            }
            n = 90;
            translationX = (this.mViewFinderRect.height() - this.mViewFinderRect.width()) / 2.0f;
            translationY = (this.mViewFinderRect.height() - this.mViewFinderRect.width()) / 2.0f;
        }
        else {
            if (CamLog.VERBOSE) {
                CamLog.d("ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE");
            }
            n = 0;
            translationX = 0.0f;
        }
        this.mPreviewContainerLayout.updatePreviewContainerLayout(this.mViewFinderRect, this.mScreenAspect);
        this.mHeadUpDisplay.setRotation((float)n);
        this.mHeadUpDisplay.setTranslationX(translationX);
        this.mHeadUpDisplay.setTranslationY(translationY);
        this.updateOnScreenButtonLayout();
        this.adjustmentWithSystemLayout();
    }
    
    private void updateOnScreenButtonLayout() {
        if (this.mCapturingButtonLayout == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("updateOnScreenButtonLayout: pre-loaded layout is null");
            }
            return;
        }
        final int configurationOrientation = LayoutOrientationResolver.getInstance().getConfigurationOrientation();
        final boolean verbose = CamLog.VERBOSE;
        int n = 0;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("updateOnScreenButtonLayout: orientation = ");
            sb.append(configurationOrientation);
            CamLog.d(sb.toString());
        }
        float translationY = 0.0f;
        float translationX;
        if (configurationOrientation == 1) {
            final int n2 = 90;
            final float n3 = (this.mViewFinderRect.height() - this.mViewFinderRect.width()) / 2.0f;
            final float n4 = (this.mViewFinderRect.width() - this.mViewFinderRect.height()) / 2.0f;
            translationX = n3;
            translationY = n4;
            n = n2;
            if (this.mActivity.getResources().getConfiguration().getLayoutDirection() == 1) {
                translationX = n3 * -1.0f;
                translationY = n4;
                n = n2;
            }
        }
        else {
            translationX = 0.0f;
        }
        this.mCapturingButtonLayout.setRotation((float)n);
        this.mCapturingButtonLayout.setTranslationX(translationX);
        this.mCapturingButtonLayout.setTranslationY(translationY);
    }
    
    public void addViewFinderGestureDetectorExclusiveView(final View view) {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector((Context)this.mActivity);
        }
        this.mGestureDetector.addExclusiveView(view);
    }
    
    public void attachToWindow() {
        this.mActivity.getWindow().addContentView((View)this.mRootView, (ViewGroup$LayoutParams)new WindowManager$LayoutParams(-1, -1));
    }
    
    public int calculateCaptureButtonAreaHeight() {
        final float n = (float)this.mActivity.getResources().getDisplayMetrics().densityDpi;
        this.mActivity.getResources().getDisplayMetrics();
        final float n2 = n / DisplayMetrics.DENSITY_DEVICE_STABLE;
        final Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(this.mActivity.getApplicationContext());
        final int n3 = (int)(Math.max(viewFinderSize.width(), viewFinderSize.height()) * n2);
        final int n4 = (int)(Math.min(viewFinderSize.width(), viewFinderSize.height()) * n2) * 4 / 3;
        final int n5 = (int)(this.mPreview.getTop() * n2);
        final int n6 = (int)(this.mPreview.getHeight() * n2);
        final int n7 = (int)(this.mPreview.getWidth() * n2);
        int dimensionPixelSize;
        if (this.mScreenAspect != LayoutDependencyResolver.ScreenAspect.SIXTEEN_NINE) {
            dimensionPixelSize = this.getActivity().getResources().getDimensionPixelSize(2131165456);
        }
        else {
            dimensionPixelSize = 0;
        }
        int n8 = n5;
        if (n7 == n6) {
            n8 = n5 + n6 - n4;
        }
        return n3 - n4 - n8 - dimensionPixelSize - LayoutDependencyResolver.getNavigationBarMargin((Context)this.mActivity);
    }
    
    public void computeRadiusOfAnimation() {
        if (this.mSwitchAnimationView == null) {
            return;
        }
        this.mSwitchAnimationView.setMaxRadius(this.mViewFinderRect);
    }
    
    public void disableGridLineView() {
        if (this.mGridLineView != null) {
            this.mGridLineView.disable();
        }
    }
    
    public void enableGridLineView() {
        if (this.mGridLineView != null) {
            this.mGridLineView.enable();
        }
    }
    
    public CameraActivity getActivity() {
        return this.mActivity;
    }
    
    public AutoReviewController getAutoReview() {
        return this.mAutoReview;
    }
    
    public LowBatteryIndicator getBatteryIndicator() {
        return this.mLowBattery;
    }
    
    public BurstCountView getBurstCountView() {
        final FrameLayout centerContainer = this.getCenterContainer();
        View view;
        if ((view = ((ViewGroup)centerContainer).findViewById(2131296322)) == null) {
            View.inflate((Context)this.mActivity, 2131493010, (ViewGroup)centerContainer);
            view = ((ViewGroup)centerContainer).findViewById(2131296322);
        }
        return (BurstCountView)view;
    }
    
    public FrameLayout getCenterContainer() {
        return (FrameLayout)this.mActivity.findViewById(2131296348);
    }
    
    public Indicator getConditionIndicator() {
        return this.mConditionIndicator;
    }
    
    public ContentsViewController getContentsViewController() {
        return this.mContentsViewController;
    }
    
    public int getCurrentOrientation() {
        return this.mCurrentOrientation;
    }
    
    public float getDisplaySizeDensity() {
        return this.mActivity.getResources().getDisplayMetrics().density;
    }
    
    public FrontAngleSwitchButton getFrontAngleSwitchButton() {
        return this.mFrontAngleSwitchButton;
    }
    
    public GeotagIndicator getGeoTagIndicator() {
        return this.mGeoTag;
    }
    
    public GridLineView getGridLineView() {
        return this.mGridLineView;
    }
    
    public FrameLayout getHintTextViewContainer() {
        if (this.mHintTextViewContainer != null) {
            return this.mHintTextViewContainer;
        }
        return this.mHintTextViewContainer = (FrameLayout)this.mActivity.findViewById(2131296419);
    }
    
    public LazyInitializer<OverlayControl> getImageQualityControl() {
        return this.mImageQualityControl;
    }
    
    public FrameLayout getLazyInflatedUiComponentContainerBack() {
        final FrameLayout frameLayout = (FrameLayout)this.mActivity.findViewById(2131296440);
        if (this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            frameLayout.setPadding(ResourceUtil.getDimensionPixelSize((Context)this.mActivity, this.mActivity.getPackageName(), 2131165428), 0, 0, 0);
        }
        return frameLayout;
    }
    
    public IconIndicator getLowMemoryInternalIndicator() {
        return this.mLowInternalStorage;
    }
    
    public IconIndicator getLowMemorySdIndicator() {
        return this.mLowSdCard;
    }
    
    public ModeSelectorButton getModeButtonShortcut() {
        return this.mModeButtonShortcut;
    }
    
    public MruButtonContainer getMruButtonContainer() {
        return this.mMruButtonContainer;
    }
    
    public OnScreenButtonGroup getOnScreenButtonGroup() {
        return this.mOnScreenButtonGroup;
    }
    
    public OnScreenButton getOnScreenExtraButton() {
        return this.mExtraButton;
    }
    
    public OnScreenButton getOnScreenSubButton() {
        return this.mSubButton;
    }
    
    public IconIndicator getPhotoSmileCaptureIndicator() {
        return this.mPhotoSmileCapture;
    }
    
    public PredictiveCaptureIndicatorController getPredictiveCaptureIndicatorController() {
        return this.mPredictiveCaptureIndicatorController;
    }
    
    public PredictiveLaunchCoverView getPredictiveLaunchCoverView() {
        return this.mPredictiveLaunchCoverView;
    }
    
    public View getPreview() {
        return this.mPreview;
    }
    
    public ViewGroup getPreviewContainer() {
        return (ViewGroup)this.mPreviewContainerLayout.mPreviewContainer;
    }
    
    public RelativeLayout getPreviewContainerRoot() {
        return this.mPreviewContainerLayout;
    }
    
    public FrameLayout getPreviewOverlayContainer() {
        return this.mPreviewContainerLayout.mPreviewOverlayContainer;
    }
    
    public PrimaryShortcutGroup getPrimaryShortcut() {
        return this.mPrimaryShortcut;
    }
    
    public RecordingIndicator getRecordingIndicator() {
        return this.mRecordingIndicator;
    }
    
    public ViewGroup getRootView() {
        return this.mRootView;
    }
    
    public IconTextIndicator getSceneIndicator() {
        return this.mSceneIndicator;
    }
    
    public LazyInitializer<OverlayControl> getSemiAutoControl() {
        return this.mSemiAutoControl;
    }
    
    public SuperSlowMotionTriggerAnimationController getSuperSlowMotionTriggerAnimation() {
        return this.mSuperSlowMotionTriggerAnimation;
    }
    
    public SwitchAnimationView getSwitchAnimationView() {
        return this.mSwitchAnimationView;
    }
    
    public Indicator getThermalIndicator() {
        return this.mThermal;
    }
    
    public View getTopIndicator() {
        return this.mTopIndicatorsContainer;
    }
    
    public TutorialController getTutorial() {
        return this.mTutorial;
    }
    
    public IconIndicator getVideoSmileCaptureIndicator() {
        return this.mVideoSmileCapture;
    }
    
    public Rect getViewFinderRect() {
        return this.mViewFinderRect;
    }
    
    public Zoombar getZoomBar() {
        return this.mZoombar;
    }
    
    public void hideAutoReview() {
        if (this.mAutoReview != null) {
            this.mAutoReview.hide();
        }
    }
    
    public void hideBlackScreen() {
        if (this.mIsBlackScreenShowing) {
            this.mIsBlackScreenShowing = false;
            this.mRootView.setBackground((Drawable)null);
        }
    }
    
    public void hideContentsViewController() {
        if (this.mContentsViewController != null) {
            this.mContentsViewController.hide();
        }
    }
    
    public void hideLeftIconContainer() {
        this.mActivity.findViewById(2131296443).setVisibility(4);
    }
    
    public void hidePredictiveLaunchCover(final Animatable2$AnimationCallback animatable2$AnimationCallback) {
        if (this.mPredictiveLaunchCoverView != null) {
            this.mPredictiveLaunchCoverView.hide(animatable2$AnimationCallback);
        }
    }
    
    public boolean isAutoReviewShowing() {
        return this.mAutoReview != null && this.mAutoReview.isShowing();
    }
    
    public boolean isCameraSwitching() {
        return this.mIsCameraSwitching;
    }
    
    public boolean isHeadUpDisplayReady() {
        return this.mHeadUpDisplay != null;
    }
    
    public boolean isInDefaultDisplaySize() {
        return this.getDisplaySizeDensity() == 1.0f;
    }
    
    public boolean isInLargerOrMoreDisplaySize() {
        return this.getDisplaySizeDensity() >= 4.0f;
    }
    
    public void pause() {
        if (this.mContentsViewController != null) {
            this.mContentsViewController.pause();
        }
        if (this.mRecordingIndicator != null) {
            this.mRecordingIndicator.setConstraint(false);
            this.mRecordingIndicator.prepareBeforeRecording(0);
            this.mRecordingIndicator.setVisibility(8);
        }
        if (this.mLazyInflatedUiComponentContainerFront != null) {
            this.mLazyInflatedUiComponentContainerFront.setOnTouchListener((View$OnTouchListener)null);
        }
        if (this.mGridLineView != null) {
            if (!this.mActivity.isOneShot()) {
                this.mGridLineView.setAlpha(0.0f);
            }
            this.mGridLineView.disable();
        }
        if (this.mHeadUpDisplay != null) {
            this.mHeadUpDisplay.setVisibility(4);
        }
        if (this.mMruButtonContainer != null) {
            this.mMruButtonContainer.setAvailability(false);
        }
    }
    
    public void refresh() {
        this.mHeadUpDisplay.requestLayout();
        this.mHeadUpDisplay.invalidate();
    }
    
    public void release() {
        if (this.mContentsViewController != null) {
            this.mContentsViewController.release();
        }
        if (this.mLazyInflatedUiComponentContainerFront != null) {
            this.mLazyInflatedUiComponentContainerFront.setOnTouchListener((View$OnTouchListener)null);
        }
        if (this.mLazyInflatedUiComponentContainerFullScreen != null) {
            this.mLazyInflatedUiComponentContainerFullScreen.setOnTouchListener((View$OnTouchListener)null);
        }
        this.mContentsViewController = null;
        this.mLazyInflatedUiComponentContainerFront = null;
        this.mLazyInflatedUiComponentContainerFullScreen = null;
        this.mWindowCover = null;
        this.mHeadUpDisplay = null;
        this.mHeadUpDisplayContainer = null;
        this.mPredictiveLaunchCoverContainer = null;
    }
    
    public void releasePredictiveLaunchCover() {
        if (this.mPredictiveLaunchCoverView != null) {
            this.mPredictiveLaunchCoverView.setOnTouchListener((View$OnTouchListener)null);
            this.mPredictiveLaunchCoverView.setVisibility(8);
            this.mPredictiveLaunchCoverContainer.removeView((View)this.mPredictiveLaunchCoverView);
            this.mPredictiveLaunchCoverView = null;
        }
    }
    
    public void reloadContentsViewController(final ContentPallet.ThumbnailStateListener thumbnailStateListener) {
        if (this.mContentsViewController == null) {
            this.setupContentsView(thumbnailStateListener);
        }
        else {
            this.mContentsViewController.reload();
        }
    }
    
    public void repositionZoombar() {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mZoombarGroup.getLayoutParams();
        if (this.mPreview.getWidth() == this.mPreview.getHeight()) {
            frameLayout$LayoutParams.leftMargin = LayoutDependencyResolver.getViewFinderSize((Context)this.mActivity).height() / 3;
        }
        else {
            frameLayout$LayoutParams.leftMargin = 0;
        }
        final int width = frameLayout$LayoutParams.width;
        int width2;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            width2 = this.mPreview.getHeight();
        }
        else {
            width2 = this.mPreview.getWidth();
        }
        if (width2 != width) {
            frameLayout$LayoutParams.width = width2;
            this.mZoombarGroup.requestLayout();
        }
    }
    
    public void requestToDimSystemUi() {
        LayoutDependencyResolver.requestToDimSystemUi((View)this.mRootView);
        this.setCurrentNavigationBarVisibility(NavigationBarVisibility.LOW_PROFILE);
    }
    
    public void requestToRecoverSystemUi() {
        LayoutDependencyResolver.requestToRecoverSystemUi((View)this.mRootView);
        this.setCurrentNavigationBarVisibility(NavigationBarVisibility.VISIBLE);
    }
    
    public void requestToRestoreSystemUi() {
        if (this.mNavigationBarVisibility == null) {
            return;
        }
        switch (BaseLayout$4.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayout$NavigationBarVisibility[this.getPreviousNavigationBarVisibility().ordinal()]) {
            case 2: {
                this.requestToDimSystemUi();
                break;
            }
            case 1: {
                this.requestToRecoverSystemUi();
                break;
            }
        }
    }
    
    public void resume() {
        this.mIsFirstDrawn = false;
        this.updateLayout();
        if (this.mHeadUpDisplay != null) {
            this.mHeadUpDisplay.setVisibility(0);
        }
    }
    
    void setCurrentNavigationBarVisibility(final NavigationBarVisibility mNavigationBarVisibility) {
        this.mNavigationBarVisibility = mNavigationBarVisibility;
    }
    
    public void setGridLineViewEnabled(final boolean b) {
        if (b) {
            this.enableGridLineView();
        }
        else {
            this.disableGridLineView();
        }
    }
    
    public void setIsCameraSwitching(final boolean mIsCameraSwitching) {
        this.mIsCameraSwitching = mIsCameraSwitching;
    }
    
    public void setOnViewFinderGestureDetector(final ViewFinderGestureDetector.OnViewFinderGestureDetectorListener onGestureDetectorListener) {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector((Context)this.mActivity);
        }
        this.mGestureDetector.setOnGestureDetectorListener(onGestureDetectorListener);
    }
    
    public void setOrientation(final int n) {
        if (CamLog.VERBOSE) {
            CamLog.d("setOrientation: ");
        }
        this.setOrientation(n, n);
    }
    
    public void setOrientation(final int n, final int n2) {
        this.mCurrentOrientation = n;
        if (this.mPredictiveLaunchCoverView != null) {
            this.mPredictiveLaunchCoverView.updateLayout(n);
        }
        if (this.mCapturingButtonLayout != null) {
            this.mOnScreenButtonGroup.setUiOrientation(n);
        }
        if (this.mHeadUpDisplay != null) {
            this.mTutorial.setUiOrientation(n);
            this.mOnScreenButtonGroup.setUiOrientation(n);
            this.mContentsViewController.setSensorOrientation(n);
            this.mGeoTag.setSensorOrientation(n2);
            this.mThermal.setSensorOrientation(n2);
            this.mSceneIndicator.setSensorOrientation(n2);
            this.mConditionIndicator.setSensorOrientation(n2);
            this.mZoombar.setSensorOrientation(n2);
            this.mRecordingIndicator.setOrientation(n2);
            this.mPrimaryShortcut.setUiOrientation(n);
            this.mPredictiveCaptureIndicatorController.setOrientation(n);
            this.mLowBattery.setSensorOrientation(n);
            this.mLowInternalStorage.setSensorOrientation(n);
            this.mLowSdCard.setSensorOrientation(n);
            this.mPhotoSmileCapture.setSensorOrientation(n);
            this.mVideoSmileCapture.setSensorOrientation(n);
            this.mModeButtonShortcut.setUiOrientation(n);
            this.mMruButtonContainer.setRotation(RotationUtil.getAngle(n));
            if (this.mFrontAngleSwitchButton != null) {
                this.mFrontAngleSwitchButton.setUiOrientation(n);
            }
            if (this.mSemiAutoControl.isInitialized()) {
                this.mSemiAutoControl.get().setOrientation(n);
            }
            if (this.mImageQualityControl.isInitialized()) {
                this.mImageQualityControl.get().setOrientation(n);
            }
        }
    }
    
    public void setPreInflatedHeadUpDisplay(final View mPreInflatedHeadUpDisplay) {
        this.mPreInflatedHeadUpDisplay = mPreInflatedHeadUpDisplay;
    }
    
    public void setPreviewSurface(final View mPreview) {
        this.mPreview = mPreview;
    }
    
    public void setStartDraggingSlopEnabled(final boolean startDraggingSlopEnabled) {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector((Context)this.mActivity);
        }
        this.mGestureDetector.setStartDraggingSlopEnabled(startDraggingSlopEnabled);
    }
    
    public void setViewFinderGestureDetectorEnabled(final boolean b, final boolean b2) {
        boolean b3 = b;
        boolean b4 = b2;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            b4 = b;
            b3 = b2;
        }
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector((Context)this.mActivity);
        }
        if (b3) {
            if (b4) {
                this.mGestureDetector.setAcceptDragDirection(ViewFinderGestureDetector.Direction.VERTICAL, ViewFinderGestureDetector.Direction.HORIZONTAL);
            }
            else {
                this.mGestureDetector.setAcceptDragDirection(ViewFinderGestureDetector.Direction.HORIZONTAL);
            }
        }
        else if (b4) {
            this.mGestureDetector.setAcceptDragDirection(ViewFinderGestureDetector.Direction.VERTICAL);
        }
        else {
            this.mGestureDetector.setAcceptDragDirection(ViewFinderGestureDetector.Direction.NONE);
        }
    }
    
    public void setup(final ContentPallet.ThumbnailStateListener thumbnailStateListener) {
        boolean b;
        if (!this.isHeadUpDisplayReady()) {
            if (CamLog.VERBOSE) {
                CamLog.d("INFLATE LAYOUT IN COMMON");
            }
            this.inflate();
            this.updateLayout();
            b = true;
        }
        else {
            b = false;
        }
        LayoutDependencyResolver.resolveLayoutDependencyOnDevice(this.mActivity, (View)this.mHeadUpDisplay);
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector((Context)this.mActivity);
            this.setViewFinderGestureDetectorEnabled(true, true);
        }
        this.setupTutorial();
        this.switchCaptureButtonGroupContainer();
        this.setupCaptureButtonGroup();
        if (b || !this.mContentsViewController.isLoading()) {
            this.setupContentsView(thumbnailStateListener);
        }
        this.setupSettingIndicators();
        this.setupTopIndicators();
        this.setupZoombar();
        this.setupRecordingIndicator();
        this.setupGridLineView();
        this.setupPrimaryShortcut();
        this.setupModeShortcut();
        this.setupSceneIndicators();
        this.setupPredictiveCaptureIndicator();
        this.setupFrontAngleSwitchButton();
        this.setOrientation(this.mCurrentOrientation);
        this.updateAppsUiMarginsForTalkBack();
        this.setupSwitchAnimationView();
        if (DebugParameterUtils.INSTANCE.isEmulateSideTouchEnabled((Context)this.mActivity)) {
            final View create = SideTouchEmulateViewFactory.INSTANCE.create(this.mHeadUpDisplay, SideTouchEventDetector.SideTouchArea.LEFT);
            if (create != null) {
                this.addViewFinderGestureDetectorExclusiveView(create);
            }
            if (SideTouchEmulateViewFactory.INSTANCE.create(this.mHeadUpDisplay, SideTouchEventDetector.SideTouchArea.RIGHT) != null) {
                this.addViewFinderGestureDetectorExclusiveView(create);
            }
        }
        this.mSuperSlowMotionTriggerAnimation.setup(this.mPreviewContainerLayout.mPreviewContainer);
    }
    
    public void setupAutoReview() {
        if (this.mAutoReview == null) {
            this.mAutoReview = new AutoReviewController((Context)this.mActivity, this);
        }
        if (this.isHeadUpDisplayReady()) {
            this.mAutoReview.setup();
        }
    }
    
    public void setupBlankScreen() {
        if (this.mWindowCover == null) {
            final LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
            if (layoutInflater == null) {
                return;
            }
            this.mWindowCover = layoutInflater.inflate(2131492905, (ViewGroup)null);
            final Window window = this.mActivity.getWindow();
            window.addContentView(this.mWindowCover, (ViewGroup$LayoutParams)window.getAttributes());
        }
    }
    
    public void setupImageQualityControl(final UiControlSettings uiControlSettings, final OverlayControl.StateListener stateListener, final EnumValueAccessor<CapturingMode> enumValueAccessor, final EnumValueAccessor<FocusRange> enumValueAccessor2, final EnumValueAccessor<ShutterSpeed> enumValueAccessor3, final EnumValueAccessor<Iso> enumValueAccessor4, final EnumValueAccessor<Ev> enumValueAccessor5, final EnumValueAccessor<WhiteBalance> enumValueAccessor6) {
        if (this.mImageQualityControl.isInitialized()) {
            this.mImageQualityControl.get().release();
        }
        this.mImageQualityControl = (LazyInitializer<OverlayControl>)new LazyInitializer<OverlayControl>(this, (ViewGroup)this.getActivity().findViewById(2131296486), uiControlSettings, stateListener, enumValueAccessor, enumValueAccessor2, enumValueAccessor3, enumValueAccessor4, enumValueAccessor5, enumValueAccessor6) {
            final BaseLayout this$0;
            final EnumValueAccessor val$capturingMode;
            final EnumValueAccessor val$exposure;
            final EnumValueAccessor val$focusRange;
            final EnumValueAccessor val$iso;
            final ViewGroup val$parent;
            final EnumValueAccessor val$shutterSpeed;
            final OverlayControl.StateListener val$stateListener;
            final UiControlSettings val$uiSettings;
            final EnumValueAccessor val$whiteBalance;
            
            public ImageQualityControl initView() {
                return new ImageQualityControl(this.val$parent, this.val$uiSettings, this.this$0.mViewFinderRect, this.this$0.mScreenAspect, this.val$stateListener, this.val$capturingMode, this.val$focusRange, this.val$shutterSpeed, this.val$iso, this.val$exposure, this.val$whiteBalance);
            }
        };
    }
    
    public void setupPredictiveCaptureIndicator() {
        if (this.mPredictiveCaptureIndicatorController == null) {
            (this.mPredictiveCaptureIndicatorController = new PredictiveCaptureIndicatorController(this.mActivity, this.mScreenAspect)).setOrientation(this.mCurrentOrientation);
        }
    }
    
    public void setupPredictiveLaunchCoverView(final PredictiveLaunchCoverView.PredictiveLaunchCoverTouchListener predictiveLaunchCoverTouchListener, final PredictiveLaunchCoverView.PredictiveLaunchCoverType predictiveLaunchCoverType) {
        if (this.mPredictiveLaunchCoverContainer != null && this.mPredictiveLaunchCoverView == null) {
            this.mPredictiveLaunchCoverView = PredictiveLaunchCoverView.inflate((Context)this.getActivity(), predictiveLaunchCoverTouchListener, predictiveLaunchCoverType);
            this.mPredictiveLaunchCoverContainer.addView((View)this.mPredictiveLaunchCoverView);
            this.mPredictiveLaunchCoverView.updateLayout(2);
        }
    }
    
    public void setupPreferentialHeadUpDisplays() {
        CamLog.d("[APP DETAIL] setup on-screen button : E");
        if (this.setupCaptureButtonGroup(this.mCapturingButtonLayout.findViewById(2131296342))) {
            this.mRootView.setBackgroundColor(0);
            this.mCapturingButtonLayout.getLayoutParams().width = this.mViewFinderRect.width();
            this.mCapturingButtonLayout.getLayoutParams().height = this.mViewFinderRect.height();
            final View viewById = this.mCapturingButtonLayout.findViewById(2131296344);
            if (viewById != null) {
                final int navigationBarMargin = LayoutDependencyResolver.getNavigationBarMargin((Context)this.mActivity);
                if (this.mScreenAspect != LayoutDependencyResolver.ScreenAspect.SIXTEEN_NINE) {
                    viewById.getLayoutParams().width = this.calculateCaptureButtonAreaHeight() - navigationBarMargin;
                }
                else {
                    viewById.getLayoutParams().width = this.calculateCaptureButtonAreaHeight();
                }
                ((ViewGroup$MarginLayoutParams)viewById.getLayoutParams()).setMargins(0, 0, navigationBarMargin, 0);
            }
            this.updateOnScreenButtonLayout();
        }
        else {
            CamLog.d("fail to setup");
        }
        CamLog.d("[APP DETAIL] setup on-screen button : X");
    }
    
    public void setupPreviewView() {
        if (this.mPreview != null) {
            this.mPreviewContainerLayout.mPreviewContainer.addView(this.mPreview, 0);
        }
    }
    
    public void setupSemiAutoControl(final OverlayControl.StateListener stateListener, final ValueAccessor<Float> valueAccessor, final ValueAccessor<Float> valueAccessor2, final boolean b) {
        if (this.mSemiAutoControl.isInitialized()) {
            this.mSemiAutoControl.get().release();
        }
        this.mSemiAutoControl = (LazyInitializer<OverlayControl>)new LazyInitializer<OverlayControl>(this, (ViewGroup)this.getActivity().findViewById(2131296412), stateListener, valueAccessor, valueAccessor2, b) {
            final BaseLayout this$0;
            final ValueAccessor val$brightness;
            final ValueAccessor val$color;
            final boolean val$isCollapsed;
            final ViewGroup val$parent;
            final OverlayControl.StateListener val$stateListener;
            
            public OverlayControl initView() {
                return new SemiAutoControl(this.val$parent, this.this$0.mScreenAspect, this.val$stateListener, this.val$color, this.val$brightness, this.val$isCollapsed);
            }
        };
    }
    
    public void showBlackScreen() {
        if (!this.mIsBlackScreenShowing) {
            this.mIsBlackScreenShowing = true;
            this.mRootView.setBackgroundColor(-16777216);
        }
    }
    
    public void showBlankScreen() {
        if (this.mWindowCover != null) {
            this.mWindowCover.setVisibility(0);
        }
    }
    
    public void showContentsViewController() {
        if (this.mContentsViewController != null) {
            this.mContentsViewController.show();
        }
    }
    
    public void showLeftIconContainer() {
        this.mActivity.findViewById(2131296443).setVisibility(0);
    }
    
    public void updateAppsUiMarginsForTalkBack() {
        int navigationBarMargin;
        if (BaseLayout.mIsTalkbackEffective == IsTalkbackEffective.TALKBACK_ON) {
            navigationBarMargin = LayoutDependencyResolver.getNavigationBarMargin((Context)this.mActivity);
        }
        else {
            navigationBarMargin = 0;
        }
        final View viewById = this.mActivity.findViewById(2131296439);
        if (viewById != null) {
            ((ViewGroup$MarginLayoutParams)viewById.getLayoutParams()).setMargins(0, 0, navigationBarMargin, 0);
            viewById.requestLayout();
        }
    }
    
    public void updateGridLine(final int n, final int n2) {
        if (this.mGridLineView != null && n > 1 && n2 > 1) {
            this.mGridLineView.setViewSize(n, n2);
        }
    }
    
    public void updatePreviewContainer(final int width, final int height) {
        final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)this.getPreviewContainer().getLayoutParams();
        if (width == height) {
            final Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize((Context)this.mActivity);
            if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 1) {
                layoutParams.leftMargin = 0;
                layoutParams.topMargin = viewFinderSize.height() / 3;
            }
            else {
                layoutParams.topMargin = 0;
                layoutParams.leftMargin = viewFinderSize.height() / 3;
            }
        }
        else if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 1) {
            layoutParams.topMargin = 0;
        }
        else {
            layoutParams.leftMargin = 0;
        }
        layoutParams.width = width;
        layoutParams.height = height;
        this.getPreviewContainer().setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    public enum IsTalkbackEffective
    {
        private static final IsTalkbackEffective[] $VALUES;
        
        TALKBACK_OFF, 
        TALKBACK_ON, 
        UNKNOWN;
        
        static {
            $VALUES = new IsTalkbackEffective[] { IsTalkbackEffective.UNKNOWN, IsTalkbackEffective.TALKBACK_ON, IsTalkbackEffective.TALKBACK_OFF };
        }
    }
    
    public abstract static class LazyInitializer<T>
    {
        private T mView;
        
        public T get() {
            if (this.mView == null) {
                this.mView = this.initView();
            }
            return this.mView;
        }
        
        abstract T initView();
        
        public boolean isInitialized() {
            return this.mView != null;
        }
    }
    
    public enum NavigationBarVisibility
    {
        private static final NavigationBarVisibility[] $VALUES;
        
        LOW_PROFILE, 
        VISIBLE;
        
        static {
            $VALUES = new NavigationBarVisibility[] { NavigationBarVisibility.VISIBLE, NavigationBarVisibility.LOW_PROFILE };
        }
    }
    
    private static class PreviewContainerLayout extends RelativeLayout
    {
        private Context mContext;
        public final FrameLayout mPreviewContainer;
        public final FrameLayout mPreviewOverlayContainer;
        
        public PreviewContainerLayout(final Context mContext) {
            super(mContext);
            this.mContext = mContext;
            this.addView((View)(this.mPreviewContainer = new FrameLayout(mContext)));
            final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(-2, -1);
            layoutParams.addRule(9, -1);
            layoutParams.addRule(10, -1);
            layoutParams.setMargins(0, 0, 0, 0);
            this.mPreviewContainer.setPadding(0, 0, 0, 0);
            this.mPreviewContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.mPreviewContainer.setId(View.generateViewId());
            final FrameLayout frameLayout = new FrameLayout(mContext);
            this.addView((View)frameLayout);
            frameLayout.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
            frameLayout.addView((View)(this.mPreviewOverlayContainer = new FrameLayout(mContext)));
            final FrameLayout$LayoutParams layoutParams2 = new FrameLayout$LayoutParams(-1, -1);
            layoutParams2.gravity = 3;
            layoutParams2.setMargins(0, 0, 0, 0);
            this.mPreviewOverlayContainer.setPadding(0, 0, 0, 0);
            this.mPreviewOverlayContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            frameLayout.addView((View)LayoutInflater.from(mContext).inflate(2131493026, (ViewGroup)null));
        }
        
        public void updatePreviewContainerLayout(final Rect rect, final LayoutDependencyResolver.ScreenAspect screenAspect) {
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams = (RelativeLayout$LayoutParams)this.mPreviewContainer.getLayoutParams();
            final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
            if (layoutParams != null && relativeLayout$LayoutParams != null) {
                final int dimensionPixelSize = ResourceUtil.getDimensionPixelSize(this.mContext, this.mContext.getPackageName(), 2131165428);
                final int dimensionPixelSize2 = ResourceUtil.getDimensionPixelSize(this.mContext, this.mContext.getPackageName(), 2131165456);
                if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                    if (screenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
                        this.setPadding(0, dimensionPixelSize, 0, dimensionPixelSize2);
                        relativeLayout$LayoutParams.addRule(9, 0);
                        relativeLayout$LayoutParams.addRule(10, -1);
                        relativeLayout$LayoutParams.addRule(15, 0);
                        relativeLayout$LayoutParams.addRule(14, -1);
                    }
                    if (layoutParams.width != rect.height()) {
                        layoutParams.width = rect.height();
                        layoutParams.height = rect.width();
                        this.requestLayout();
                    }
                }
                else {
                    if (screenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
                        this.setPadding(dimensionPixelSize, 0, dimensionPixelSize2, 0);
                        relativeLayout$LayoutParams.addRule(9, -1);
                        relativeLayout$LayoutParams.addRule(10, 0);
                        relativeLayout$LayoutParams.addRule(14, 0);
                        relativeLayout$LayoutParams.addRule(15, -1);
                    }
                    if (layoutParams.width != rect.width()) {
                        layoutParams.width = rect.width();
                        layoutParams.height = rect.height();
                        this.requestLayout();
                    }
                }
            }
        }
    }
    
    private class RootView extends HoverEventInterceptView
    {
        final BaseLayout this$0;
        
        public RootView(final BaseLayout this$0, final Activity activity) {
            this.this$0 = this$0;
            super((Context)activity);
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            return this.this$0.mGestureDetector != null && this.this$0.mGestureDetector.onInterceptTouchEvent(motionEvent);
        }
        
        protected void onMeasure(int width, int height) {
            super.onMeasure(width, height);
            width = this.this$0.mRootView.getWidth();
            height = this.this$0.mRootView.getHeight();
            if (BaseLayout.mIsTalkbackEffective == IsTalkbackEffective.UNKNOWN) {
                if (width > height) {
                    final Display defaultDisplay = this.this$0.mActivity.getWindowManager().getDefaultDisplay();
                    final Point point = new Point();
                    defaultDisplay.getRealSize(point);
                    if (width < point.x) {
                        BaseLayout.mIsTalkbackEffective = IsTalkbackEffective.TALKBACK_ON;
                    }
                    else {
                        BaseLayout.mIsTalkbackEffective = IsTalkbackEffective.TALKBACK_OFF;
                    }
                    this.this$0.updateAppsUiMarginsForTalkBack();
                }
            }
            else if (width < height) {
                BaseLayout.mIsTalkbackEffective = IsTalkbackEffective.UNKNOWN;
            }
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return this.this$0.mGestureDetector != null && this.this$0.mGestureDetector.onTouchEvent(motionEvent);
        }
    }
    
    private class RootViewForRefLogEnabled extends RootView
    {
        final BaseLayout this$0;
        
        public RootViewForRefLogEnabled(final BaseLayout this$0, final Activity activity) {
            this.this$0 = this$0.super(activity);
            this$0.mIsFirstDrawn = false;
        }
        
        protected void dispatchDraw(final Canvas canvas) {
            if (!this.this$0.mIsFirstDrawn) {
                this.this$0.mIsFirstDrawn = true;
                PerfLog.VIEWFINDER_FIRST_DRAW.begin();
                super.dispatchDraw(canvas);
                PerfLog.VIEWFINDER_FIRST_DRAW.end();
            }
            else {
                super.dispatchDraw(canvas);
            }
        }
    }
    
    private enum ViewRootChild
    {
        private static final ViewRootChild[] $VALUES;
        
        CAPTURE_BUTTON_LAYOUT(3), 
        HEAD_UP_DISPLAY_CONTAINER(4), 
        PREDICTIVE_LAUNCH_COVER_CONTAINER(5), 
        PREFERRED_FOCUS(0), 
        PREVIEW_CONTAINER_LAYOUT(1), 
        SWITCH_ANIMATION_CONTAINER(2);
        
        private int mIndex;
        
        static {
            $VALUES = new ViewRootChild[] { ViewRootChild.PREFERRED_FOCUS, ViewRootChild.PREVIEW_CONTAINER_LAYOUT, ViewRootChild.SWITCH_ANIMATION_CONTAINER, ViewRootChild.CAPTURE_BUTTON_LAYOUT, ViewRootChild.HEAD_UP_DISPLAY_CONTAINER, ViewRootChild.PREDICTIVE_LAUNCH_COVER_CONTAINER };
        }
        
        private ViewRootChild(final int mIndex) {
            this.mIndex = mIndex;
        }
        
        public int getIndex() {
            return this.mIndex;
        }
    }
}
