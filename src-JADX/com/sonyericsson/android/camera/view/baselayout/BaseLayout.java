package com.sonyericsson.android.camera.view.baselayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Animatable2;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.SideTouchEventDetector;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import com.sonyericsson.android.camera.debug.SideTouchEmulateViewFactory;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.setting.UiControlSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.view.AutoReviewController;
import com.sonyericsson.android.camera.view.BurstCountView;
import com.sonyericsson.android.camera.view.FrontAngleSwitchButton;
import com.sonyericsson.android.camera.view.PredictiveCaptureIndicatorController;
import com.sonyericsson.android.camera.view.PrimaryShortcutGroup;
import com.sonyericsson.android.camera.view.SuperSlowMotionTriggerAnimationController;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.baselayout.PredictiveLaunchCoverView;
import com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector;
import com.sonyericsson.android.camera.view.baselayout.indicators.IconIndicator;
import com.sonyericsson.android.camera.view.baselayout.indicators.IconTextIndicator;
import com.sonyericsson.android.camera.view.baselayout.indicators.LowBatteryIndicator;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButton;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.android.camera.view.baselayout.settingshortcut.ModeSelectorButton;
import com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer;
import com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.android.camera.view.overlaycontrol.EnumValueAccessor;
import com.sonyericsson.android.camera.view.overlaycontrol.ImageQualityControl;
import com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl;
import com.sonyericsson.android.camera.view.overlaycontrol.SemiAutoControl;
import com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonyericsson.cameracommon.contentsview.ContentLoader;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;
import com.sonyericsson.cameracommon.contentsview.ContentsContainer;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.cameracommon.utility.AccessibilityHelper;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.viewfinder.GridLineView;
import com.sonyericsson.cameracommon.viewfinder.indicators.GeotagIndicator;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingIndicator;
import java.util.LinkedList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BaseLayout {
    private static final String TAG = "BaseLayout";
    private final CameraActivity mActivity;
    private AutoReviewController mAutoReview;
    private FrameLayout mCapturingButtonLayout;
    private Indicator mConditionIndicator;
    private ContentsViewController mContentsViewController;
    private OnScreenButton mExtraButton;
    private FrontAngleSwitchButton mFrontAngleSwitchButton;
    private GeotagIndicator mGeoTag;
    private ViewFinderGestureDetector mGestureDetector;
    private ViewGroup mHeadUpDisplay;
    private FrameLayout mHeadUpDisplayContainer;
    private FrameLayout mHintTextViewContainer;
    private final LowBatteryIndicator mLowBattery;
    private final IconIndicator mLowInternalStorage;
    private final IconIndicator mLowSdCard;
    private ModeSelectorButton mModeButtonShortcut;
    private MruButtonContainer mMruButtonContainer;
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
    private OnScreenButton mSubButton;
    private final SuperSlowMotionTriggerAnimationController mSuperSlowMotionTriggerAnimation;
    private FrameLayout mSwitchAnimationContainer;
    private Indicator mThermal;
    private View mTopIndicatorsContainer;
    private TutorialController mTutorial;
    private final IconIndicator mVideoSmileCapture;
    private Rect mViewFinderRect;
    private View mWindowCover;
    private Zoombar mZoombar;
    private FrameLayout mZoombarGroup;
    public static final LazyInitializer EMPTY_LAZY_INITIALIZER = new LazyInitializer<OverlayControl>() { // from class: com.sonyericsson.android.camera.view.baselayout.BaseLayout.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sonyericsson.android.camera.view.baselayout.BaseLayout.LazyInitializer
        /* renamed from: initView */
        public OverlayControl initView2() {
            return null;
        }
    };
    private static IsTalkbackEffective mIsTalkbackEffective = IsTalkbackEffective.UNKNOWN;
    private FrameLayout mLazyInflatedUiComponentContainerFront = null;
    private FrameLayout mLazyInflatedUiComponentContainerFullScreen = null;
    private LazyInitializer<OverlayControl> mSemiAutoControl = EMPTY_LAZY_INITIALIZER;
    private LazyInitializer<OverlayControl> mImageQualityControl = EMPTY_LAZY_INITIALIZER;
    private boolean mIsBlackScreenShowing = false;
    private int mCurrentOrientation = 0;
    private GridLineView mGridLineView = null;
    private SwitchAnimationView mSwitchAnimationView = null;
    private boolean mIsCameraSwitching = false;
    private boolean mIsFirstDrawn = false;
    private NavigationBarVisibility mNavigationBarVisibility = null;

    public enum IsTalkbackEffective {
        UNKNOWN,
        TALKBACK_ON,
        TALKBACK_OFF
    }

    public enum NavigationBarVisibility {
        VISIBLE,
        LOW_PROFILE
    }

    private enum ViewRootChild {
        PREFERRED_FOCUS(0),
        PREVIEW_CONTAINER_LAYOUT(1),
        SWITCH_ANIMATION_CONTAINER(2),
        CAPTURE_BUTTON_LAYOUT(3),
        HEAD_UP_DISPLAY_CONTAINER(4),
        PREDICTIVE_LAUNCH_COVER_CONTAINER(5);

        private int mIndex;

        ViewRootChild(int i) {
            this.mIndex = i;
        }

        public int getIndex() {
            return this.mIndex;
        }
    }

    public static abstract class LazyInitializer<T> {
        private T mView;

        /* renamed from: initView */
        abstract T initView2();

        public boolean isInitialized() {
            return this.mView != null;
        }

        public T get() {
            if (this.mView == null) {
                this.mView = initView2();
            }
            return this.mView;
        }
    }

    public BaseLayout(CameraActivity cameraActivity, LayoutDependencyResolver.ScreenAspect screenAspect) {
        this.mViewFinderRect = null;
        this.mActivity = cameraActivity;
        this.mScreenAspect = screenAspect;
        if (PerfLog.IS_ENABLE) {
            this.mRootView = new RootViewForRefLogEnabled(this.mActivity);
        } else {
            this.mRootView = new RootView(this.mActivity);
        }
        this.mViewFinderRect = LayoutDependencyResolver.getViewFinderSize(cameraActivity);
        for (View view : createContainer(this.mActivity)) {
            this.mRootView.addView(view);
        }
        setupPreferredFocusView();
        setupPreviewContainer();
        setupSwitchAnimationContainer();
        this.mLowBattery = new LowBatteryIndicator(cameraActivity, "low-battery");
        this.mLowInternalStorage = new IconIndicator("low-internal-storage");
        this.mLowSdCard = new IconIndicator("low-sd-card");
        this.mPhotoSmileCapture = new IconIndicator("photo-smile-capture");
        this.mVideoSmileCapture = new IconIndicator("video-smile-capture");
        this.mSuperSlowMotionTriggerAnimation = new SuperSlowMotionTriggerAnimationController();
        LayoutDependencyResolver.requestToDimSystemUi(this.mRootView);
    }

    private class RootView extends AccessibilityHelper.HoverEventInterceptView {
        public RootView(Activity activity) {
            super(activity);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (BaseLayout.this.mGestureDetector == null) {
                return false;
            }
            return BaseLayout.this.mGestureDetector.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (BaseLayout.this.mGestureDetector == null) {
                return false;
            }
            return BaseLayout.this.mGestureDetector.onTouchEvent(motionEvent);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int width = BaseLayout.this.mRootView.getWidth();
            int height = BaseLayout.this.mRootView.getHeight();
            if (BaseLayout.mIsTalkbackEffective != IsTalkbackEffective.UNKNOWN) {
                if (width < height) {
                    IsTalkbackEffective unused = BaseLayout.mIsTalkbackEffective = IsTalkbackEffective.UNKNOWN;
                }
            } else if (width > height) {
                Display defaultDisplay = BaseLayout.this.mActivity.getWindowManager().getDefaultDisplay();
                Point point = new Point();
                defaultDisplay.getRealSize(point);
                if (width < point.x) {
                    IsTalkbackEffective unused2 = BaseLayout.mIsTalkbackEffective = IsTalkbackEffective.TALKBACK_ON;
                } else {
                    IsTalkbackEffective unused3 = BaseLayout.mIsTalkbackEffective = IsTalkbackEffective.TALKBACK_OFF;
                }
                BaseLayout.this.updateAppsUiMarginsForTalkBack();
            }
        }
    }

    private class RootViewForRefLogEnabled extends RootView {
        public RootViewForRefLogEnabled(Activity activity) {
            super(activity);
            BaseLayout.this.mIsFirstDrawn = false;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (!BaseLayout.this.mIsFirstDrawn) {
                BaseLayout.this.mIsFirstDrawn = true;
                PerfLog.VIEWFINDER_FIRST_DRAW.begin();
                super.dispatchDraw(canvas);
                PerfLog.VIEWFINDER_FIRST_DRAW.end();
                return;
            }
            super.dispatchDraw(canvas);
        }
    }

    public void setOnViewFinderGestureDetector(ViewFinderGestureDetector.OnViewFinderGestureDetectorListener onViewFinderGestureDetectorListener) {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector(this.mActivity);
        }
        this.mGestureDetector.setOnGestureDetectorListener(onViewFinderGestureDetectorListener);
    }

    public void setViewFinderGestureDetectorEnabled(boolean z, boolean z2) {
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            z2 = z;
            z = z2;
        }
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector(this.mActivity);
        }
        if (z) {
            if (z2) {
                this.mGestureDetector.setAcceptDragDirection(ViewFinderGestureDetector.Direction.VERTICAL, ViewFinderGestureDetector.Direction.HORIZONTAL);
                return;
            } else {
                this.mGestureDetector.setAcceptDragDirection(ViewFinderGestureDetector.Direction.HORIZONTAL);
                return;
            }
        }
        if (z2) {
            this.mGestureDetector.setAcceptDragDirection(ViewFinderGestureDetector.Direction.VERTICAL);
        } else {
            this.mGestureDetector.setAcceptDragDirection(ViewFinderGestureDetector.Direction.NONE);
        }
    }

    public void setStartDraggingSlopEnabled(boolean z) {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector(this.mActivity);
        }
        this.mGestureDetector.setStartDraggingSlopEnabled(z);
    }

    public void addViewFinderGestureDetectorExclusiveView(View view) {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector(this.mActivity);
        }
        this.mGestureDetector.addExclusiveView(view);
    }

    public boolean isInDefaultDisplaySize() {
        return getDisplaySizeDensity() == 1.0f;
    }

    public boolean isInLargerOrMoreDisplaySize() {
        return getDisplaySizeDensity() >= 4.0f;
    }

    public float getDisplaySizeDensity() {
        return this.mActivity.getResources().getDisplayMetrics().density;
    }

    private View[] createContainer(@NonNull Context context) {
        View[] viewArr = new View[ViewRootChild.values().length];
        this.mPreferredFocusView = new View(context);
        this.mPreviewContainerLayout = new PreviewContainerLayout(context);
        this.mSwitchAnimationContainer = new FrameLayout(context);
        this.mCapturingButtonLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.capturing_button_layout, (ViewGroup) null);
        this.mHeadUpDisplayContainer = new FrameLayout(context);
        this.mPredictiveLaunchCoverContainer = new FrameLayout(context);
        viewArr[ViewRootChild.PREFERRED_FOCUS.getIndex()] = this.mPreferredFocusView;
        viewArr[ViewRootChild.PREVIEW_CONTAINER_LAYOUT.getIndex()] = this.mPreviewContainerLayout;
        viewArr[ViewRootChild.SWITCH_ANIMATION_CONTAINER.getIndex()] = this.mSwitchAnimationContainer;
        viewArr[ViewRootChild.CAPTURE_BUTTON_LAYOUT.getIndex()] = this.mCapturingButtonLayout;
        viewArr[ViewRootChild.HEAD_UP_DISPLAY_CONTAINER.getIndex()] = this.mHeadUpDisplayContainer;
        viewArr[ViewRootChild.PREDICTIVE_LAUNCH_COVER_CONTAINER.getIndex()] = this.mPredictiveLaunchCoverContainer;
        return viewArr;
    }

    private void setupPreviewContainer() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.setLayoutDirection(0);
        Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(this.mViewFinderRect);
        layoutParams.width = rectAccordingToLayoutOrientation.width();
        layoutParams.height = rectAccordingToLayoutOrientation.height();
        layoutParams.gravity = 80;
        this.mPreviewContainerLayout.setLayoutParams(layoutParams);
        this.mCapturingButtonLayout.setVisibility(4);
    }

    private void setupSwitchAnimationContainer() {
        this.mSwitchAnimationContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
    }

    public void attachToWindow() {
        this.mActivity.getWindow().addContentView(this.mRootView, new WindowManager.LayoutParams(-1, -1));
    }

    public void setPreviewSurface(View view) {
        this.mPreview = view;
    }

    public void setupPreviewView() {
        if (this.mPreview != null) {
            this.mPreviewContainerLayout.mPreviewContainer.addView(this.mPreview, 0);
        }
    }

    public void setupAutoReview() {
        if (this.mAutoReview == null) {
            this.mAutoReview = new AutoReviewController(this.mActivity, this);
        }
        if (isHeadUpDisplayReady()) {
            this.mAutoReview.setup();
        }
    }

    private void updateLayout() {
        int i;
        float fHeight;
        if (CamLog.VERBOSE) {
            CamLog.d("updateLayout() is called.");
        }
        if (isHeadUpDisplayReady()) {
            float fHeight2 = 0.0f;
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                if (CamLog.VERBOSE) {
                    CamLog.d("ActivityInfo.SCREEN_ORIENTATION_PORTRAIT");
                }
                i = 90;
                fHeight = (this.mViewFinderRect.height() - this.mViewFinderRect.width()) / 2.0f;
                fHeight2 = (this.mViewFinderRect.height() - this.mViewFinderRect.width()) / 2.0f;
            } else {
                if (CamLog.VERBOSE) {
                    CamLog.d("ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE");
                }
                i = 0;
                fHeight = 0.0f;
            }
            this.mPreviewContainerLayout.updatePreviewContainerLayout(this.mViewFinderRect, this.mScreenAspect);
            this.mHeadUpDisplay.setRotation(i);
            this.mHeadUpDisplay.setTranslationX(fHeight);
            this.mHeadUpDisplay.setTranslationY(fHeight2);
            updateOnScreenButtonLayout();
            adjustmentWithSystemLayout();
        }
    }

    private void updateOnScreenButtonLayout() {
        float fHeight;
        if (this.mCapturingButtonLayout == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("updateOnScreenButtonLayout: pre-loaded layout is null");
                return;
            }
            return;
        }
        int configurationOrientation = LayoutOrientationResolver.getInstance().getConfigurationOrientation();
        int i = 0;
        if (CamLog.VERBOSE) {
            CamLog.d("updateOnScreenButtonLayout: orientation = " + configurationOrientation);
        }
        float fWidth = 0.0f;
        if (configurationOrientation == 1) {
            i = 90;
            fHeight = (this.mViewFinderRect.height() - this.mViewFinderRect.width()) / 2.0f;
            fWidth = (this.mViewFinderRect.width() - this.mViewFinderRect.height()) / 2.0f;
            if (this.mActivity.getResources().getConfiguration().getLayoutDirection() == 1) {
                fHeight *= -1.0f;
            }
        } else {
            fHeight = 0.0f;
        }
        this.mCapturingButtonLayout.setRotation(i);
        this.mCapturingButtonLayout.setTranslationX(fHeight);
        this.mCapturingButtonLayout.setTranslationY(fWidth);
    }

    private void adjustmentWithSystemLayout() {
        int navigationBarMargin = LayoutDependencyResolver.getNavigationBarMargin(this.mActivity);
        if (navigationBarMargin <= 0) {
            return;
        }
        for (int i : new int[]{R.id.icons, R.id.side_touch_ui_layout, R.id.autoreview_layout, R.id.lazy_inflated_ui_component_container}) {
            ((ViewGroup.MarginLayoutParams) ((ViewGroup) this.mHeadUpDisplay.findViewById(i)).getLayoutParams()).setMargins(0, 0, navigationBarMargin, 0);
        }
    }

    public void setupPreferentialHeadUpDisplays() {
        CamLog.d("[APP DETAIL] setup on-screen button : E");
        if (setupCaptureButtonGroup(this.mCapturingButtonLayout.findViewById(R.id.capture_button_group))) {
            this.mRootView.setBackgroundColor(0);
            this.mCapturingButtonLayout.getLayoutParams().width = this.mViewFinderRect.width();
            this.mCapturingButtonLayout.getLayoutParams().height = this.mViewFinderRect.height();
            View viewFindViewById = this.mCapturingButtonLayout.findViewById(R.id.capturing_button_container);
            if (viewFindViewById != null) {
                int navigationBarMargin = LayoutDependencyResolver.getNavigationBarMargin(this.mActivity);
                if (this.mScreenAspect != LayoutDependencyResolver.ScreenAspect.SIXTEEN_NINE) {
                    viewFindViewById.getLayoutParams().width = calculateCaptureButtonAreaHeight() - navigationBarMargin;
                } else {
                    viewFindViewById.getLayoutParams().width = calculateCaptureButtonAreaHeight();
                }
                ((ViewGroup.MarginLayoutParams) viewFindViewById.getLayoutParams()).setMargins(0, 0, navigationBarMargin, 0);
            }
            updateOnScreenButtonLayout();
        } else {
            CamLog.d("fail to setup");
        }
        CamLog.d("[APP DETAIL] setup on-screen button : X");
    }

    public void setup(ContentPallet.ThumbnailStateListener thumbnailStateListener) throws Resources.NotFoundException {
        boolean z;
        if (isHeadUpDisplayReady()) {
            z = false;
        } else {
            if (CamLog.VERBOSE) {
                CamLog.d("INFLATE LAYOUT IN COMMON");
            }
            inflate();
            updateLayout();
            z = true;
        }
        LayoutDependencyResolver.resolveLayoutDependencyOnDevice(this.mActivity, this.mHeadUpDisplay);
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new ViewFinderGestureDetector(this.mActivity);
            setViewFinderGestureDetectorEnabled(true, true);
        }
        setupTutorial();
        switchCaptureButtonGroupContainer();
        setupCaptureButtonGroup();
        if (z || !this.mContentsViewController.isLoading()) {
            setupContentsView(thumbnailStateListener);
        }
        setupSettingIndicators();
        setupTopIndicators();
        setupZoombar();
        setupRecordingIndicator();
        setupGridLineView();
        setupPrimaryShortcut();
        setupModeShortcut();
        setupSceneIndicators();
        setupPredictiveCaptureIndicator();
        setupFrontAngleSwitchButton();
        setOrientation(this.mCurrentOrientation);
        updateAppsUiMarginsForTalkBack();
        setupSwitchAnimationView();
        if (DebugParameterUtils.INSTANCE.isEmulateSideTouchEnabled(this.mActivity)) {
            View viewCreate = SideTouchEmulateViewFactory.INSTANCE.create(this.mHeadUpDisplay, SideTouchEventDetector.SideTouchArea.LEFT);
            if (viewCreate != null) {
                addViewFinderGestureDetectorExclusiveView(viewCreate);
            }
            if (SideTouchEmulateViewFactory.INSTANCE.create(this.mHeadUpDisplay, SideTouchEventDetector.SideTouchArea.RIGHT) != null) {
                addViewFinderGestureDetectorExclusiveView(viewCreate);
            }
        }
        this.mSuperSlowMotionTriggerAnimation.setup(this.mPreviewContainerLayout.mPreviewContainer);
    }

    public void release() {
        if (this.mContentsViewController != null) {
            this.mContentsViewController.release();
        }
        if (this.mLazyInflatedUiComponentContainerFront != null) {
            this.mLazyInflatedUiComponentContainerFront.setOnTouchListener(null);
        }
        if (this.mLazyInflatedUiComponentContainerFullScreen != null) {
            this.mLazyInflatedUiComponentContainerFullScreen.setOnTouchListener(null);
        }
        this.mContentsViewController = null;
        this.mLazyInflatedUiComponentContainerFront = null;
        this.mLazyInflatedUiComponentContainerFullScreen = null;
        this.mWindowCover = null;
        this.mHeadUpDisplay = null;
        this.mHeadUpDisplayContainer = null;
        this.mPredictiveLaunchCoverContainer = null;
    }

    public SuperSlowMotionTriggerAnimationController getSuperSlowMotionTriggerAnimation() {
        return this.mSuperSlowMotionTriggerAnimation;
    }

    public GeotagIndicator getGeoTagIndicator() {
        return this.mGeoTag;
    }

    public LowBatteryIndicator getBatteryIndicator() {
        return this.mLowBattery;
    }

    public IconIndicator getLowMemoryInternalIndicator() {
        return this.mLowInternalStorage;
    }

    public IconIndicator getLowMemorySdIndicator() {
        return this.mLowSdCard;
    }

    public Indicator getThermalIndicator() {
        return this.mThermal;
    }

    public IconIndicator getPhotoSmileCaptureIndicator() {
        return this.mPhotoSmileCapture;
    }

    public IconIndicator getVideoSmileCaptureIndicator() {
        return this.mVideoSmileCapture;
    }

    public PredictiveCaptureIndicatorController getPredictiveCaptureIndicatorController() {
        return this.mPredictiveCaptureIndicatorController;
    }

    public IconTextIndicator getSceneIndicator() {
        return this.mSceneIndicator;
    }

    public Indicator getConditionIndicator() {
        return this.mConditionIndicator;
    }

    public ModeSelectorButton getModeButtonShortcut() {
        return this.mModeButtonShortcut;
    }

    public MruButtonContainer getMruButtonContainer() {
        return this.mMruButtonContainer;
    }

    public LazyInitializer<OverlayControl> getSemiAutoControl() {
        return this.mSemiAutoControl;
    }

    public LazyInitializer<OverlayControl> getImageQualityControl() {
        return this.mImageQualityControl;
    }

    public void setOrientation(int i) throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("setOrientation: ");
        }
        setOrientation(i, i);
    }

    public void setOrientation(int i, int i2) throws Resources.NotFoundException {
        this.mCurrentOrientation = i;
        if (this.mPredictiveLaunchCoverView != null) {
            this.mPredictiveLaunchCoverView.updateLayout(i);
        }
        if (this.mCapturingButtonLayout != null) {
            this.mOnScreenButtonGroup.setUiOrientation(i);
        }
        if (this.mHeadUpDisplay != null) {
            this.mTutorial.setUiOrientation(i);
            this.mOnScreenButtonGroup.setUiOrientation(i);
            this.mContentsViewController.setSensorOrientation(i);
            this.mGeoTag.setSensorOrientation(i2);
            this.mThermal.setSensorOrientation(i2);
            this.mSceneIndicator.setSensorOrientation(i2);
            this.mConditionIndicator.setSensorOrientation(i2);
            this.mZoombar.setSensorOrientation(i2);
            this.mRecordingIndicator.setOrientation(i2);
            this.mPrimaryShortcut.setUiOrientation(i);
            this.mPredictiveCaptureIndicatorController.setOrientation(i);
            this.mLowBattery.setSensorOrientation(i);
            this.mLowInternalStorage.setSensorOrientation(i);
            this.mLowSdCard.setSensorOrientation(i);
            this.mPhotoSmileCapture.setSensorOrientation(i);
            this.mVideoSmileCapture.setSensorOrientation(i);
            this.mModeButtonShortcut.setUiOrientation(i);
            this.mMruButtonContainer.setRotation(RotationUtil.getAngle(i));
            if (this.mFrontAngleSwitchButton != null) {
                this.mFrontAngleSwitchButton.setUiOrientation(i);
            }
            if (this.mSemiAutoControl.isInitialized()) {
                this.mSemiAutoControl.get().setOrientation(i);
            }
            if (this.mImageQualityControl.isInitialized()) {
                this.mImageQualityControl.get().setOrientation(i);
            }
        }
    }

    public View getPreview() {
        return this.mPreview;
    }

    public ViewGroup getPreviewContainer() {
        return this.mPreviewContainerLayout.mPreviewContainer;
    }

    public Rect getViewFinderRect() {
        return this.mViewFinderRect;
    }

    public void setupImageQualityControl(final UiControlSettings uiControlSettings, final OverlayControl.StateListener stateListener, final EnumValueAccessor<CapturingMode> enumValueAccessor, final EnumValueAccessor<FocusRange> enumValueAccessor2, final EnumValueAccessor<ShutterSpeed> enumValueAccessor3, final EnumValueAccessor<Iso> enumValueAccessor4, final EnumValueAccessor<Ev> enumValueAccessor5, final EnumValueAccessor<WhiteBalance> enumValueAccessor6) {
        if (this.mImageQualityControl.isInitialized()) {
            this.mImageQualityControl.get().release();
        }
        final ViewGroup viewGroup = (ViewGroup) getActivity().findViewById(R.id.overlay_control_view_container);
        this.mImageQualityControl = new LazyInitializer<OverlayControl>() { // from class: com.sonyericsson.android.camera.view.baselayout.BaseLayout.2
            @Override // com.sonyericsson.android.camera.view.baselayout.BaseLayout.LazyInitializer
            /* renamed from: initView, reason: merged with bridge method [inline-methods] */
            public OverlayControl initView2() {
                return new ImageQualityControl(viewGroup, uiControlSettings, BaseLayout.this.mViewFinderRect, BaseLayout.this.mScreenAspect, stateListener, enumValueAccessor, enumValueAccessor2, enumValueAccessor3, enumValueAccessor4, enumValueAccessor5, enumValueAccessor6);
            }
        };
    }

    public void setupSemiAutoControl(final OverlayControl.StateListener stateListener, final ValueAccessor<Float> valueAccessor, final ValueAccessor<Float> valueAccessor2, final boolean z) {
        if (this.mSemiAutoControl.isInitialized()) {
            this.mSemiAutoControl.get().release();
        }
        final ViewGroup viewGroup = (ViewGroup) getActivity().findViewById(R.id.head_up_display);
        this.mSemiAutoControl = new LazyInitializer<OverlayControl>() { // from class: com.sonyericsson.android.camera.view.baselayout.BaseLayout.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sonyericsson.android.camera.view.baselayout.BaseLayout.LazyInitializer
            /* renamed from: initView */
            public OverlayControl initView2() {
                return new SemiAutoControl(viewGroup, BaseLayout.this.mScreenAspect, stateListener, valueAccessor, valueAccessor2, z);
            }
        };
    }

    private void setupCaptureButtonGroup() {
        if (setupCaptureButtonGroup(this.mActivity.findViewById(R.id.capture_button_group)) || !CamLog.VERBOSE) {
            return;
        }
        CamLog.w("setupCaptureButtonGroup: fails to setup");
    }

    public int calculateCaptureButtonAreaHeight() {
        float f = this.mActivity.getResources().getDisplayMetrics().densityDpi;
        this.mActivity.getResources().getDisplayMetrics();
        float f2 = f / DisplayMetrics.DENSITY_DEVICE_STABLE;
        Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(this.mActivity.getApplicationContext());
        int iMax = (int) (Math.max(viewFinderSize.width(), viewFinderSize.height()) * f2);
        int iMin = (((int) (Math.min(viewFinderSize.width(), viewFinderSize.height()) * f2)) * 4) / 3;
        int top = (int) (this.mPreview.getTop() * f2);
        int height = (int) (this.mPreview.getHeight() * f2);
        int width = (int) (this.mPreview.getWidth() * f2);
        int dimensionPixelSize = this.mScreenAspect != LayoutDependencyResolver.ScreenAspect.SIXTEEN_NINE ? getActivity().getResources().getDimensionPixelSize(R.dimen.navigator_container_width) : 0;
        if (width == height) {
            top = (top + height) - iMin;
        }
        return (((iMax - iMin) - top) - dimensionPixelSize) - LayoutDependencyResolver.getNavigationBarMargin(this.mActivity);
    }

    private boolean setupCaptureButtonGroup(View view) {
        if (!(view instanceof OnScreenButtonGroup)) {
            return false;
        }
        if (this.mOnScreenButtonGroup == null) {
            this.mOnScreenButtonGroup = (OnScreenButtonGroup) view;
        }
        this.mSubButton = (OnScreenButton) this.mOnScreenButtonGroup.findViewById(R.id.sub_button);
        this.mExtraButton = (OnScreenButton) this.mOnScreenButtonGroup.findViewById(R.id.extra_button);
        if (this.mGestureDetector == null) {
            return true;
        }
        this.mGestureDetector.addExclusiveView(this.mExtraButton);
        this.mGestureDetector.addExclusiveView(this.mSubButton);
        this.mGestureDetector.addExclusiveView(this.mOnScreenButtonGroup.findViewById(R.id.main_button));
        return true;
    }

    public OnScreenButtonGroup getOnScreenButtonGroup() {
        return this.mOnScreenButtonGroup;
    }

    public OnScreenButton getOnScreenSubButton() {
        return this.mSubButton;
    }

    public OnScreenButton getOnScreenExtraButton() {
        return this.mExtraButton;
    }

    private void setupTutorial() {
        if (this.mTutorial == null) {
            this.mTutorial = new TutorialController(getRootView(), getActivity().getWindow());
        }
    }

    private void setupFrontAngleSwitchButton() {
        if (this.mFrontAngleSwitchButton == null && PlatformCapability.isSuperWideSupported(CameraInfo.CameraId.FRONT)) {
            this.mFrontAngleSwitchButton = (FrontAngleSwitchButton) this.mActivity.findViewById(R.id.front_angle_switch_button);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mFrontAngleSwitchButton.getLayoutParams();
            layoutParams.rightMargin = calculateCaptureButtonAreaHeight();
            this.mFrontAngleSwitchButton.setLayoutParams(layoutParams);
        }
    }

    private void setupPreferredFocusView() {
        if (this.mPreferredFocusView != null) {
            this.mPreferredFocusView.setLayoutParams(new FrameLayout.LayoutParams(1, 1));
            this.mPreferredFocusView.setFocusable(true);
            this.mPreferredFocusView.setFocusableInTouchMode(true);
            this.mPreferredFocusView.requestFocus();
        }
    }

    public FrontAngleSwitchButton getFrontAngleSwitchButton() {
        return this.mFrontAngleSwitchButton;
    }

    public TutorialController getTutorial() {
        return this.mTutorial;
    }

    public RecordingIndicator getRecordingIndicator() {
        return this.mRecordingIndicator;
    }

    public AutoReviewController getAutoReview() {
        return this.mAutoReview;
    }

    private void setupContentsView(ContentPallet.ThumbnailStateListener thumbnailStateListener) {
        ContentLoader.SecurityLevel securityLevel;
        if (this.mActivity.isDeviceInSecurityLock()) {
            securityLevel = ContentLoader.SecurityLevel.NEWLY_ADDED_CONTENT_ONLY;
        } else {
            securityLevel = ContentLoader.SecurityLevel.NORMAL;
        }
        LinkedList linkedList = null;
        if (isCameraSwitching()) {
            return;
        }
        if (this.mContentsViewController != null) {
            linkedList = new LinkedList();
            linkedList.addAll(this.mContentsViewController.getLocalContentInfo());
            this.mContentsViewController.release();
        }
        this.mContentsViewController = new ContentsViewController(this.mActivity, this.mActivity.getStorage(), securityLevel, thumbnailStateListener);
        if (linkedList != null) {
            this.mContentsViewController.getLocalContentInfo().addAll(linkedList);
        }
        this.mContentsViewController.setSensorOrientation(this.mCurrentOrientation);
        this.mContentsViewController.reload();
        addViewFinderGestureDetectorExclusiveView((ContentsContainer) this.mActivity.findViewById(R.id.contents_container));
    }

    public int getCurrentOrientation() {
        return this.mCurrentOrientation;
    }

    public ContentsViewController getContentsViewController() {
        return this.mContentsViewController;
    }

    private void setupModeShortcut() {
        this.mModeButtonShortcut = (ModeSelectorButton) this.mHeadUpDisplay.findViewById(R.id.mode_button_shortcut);
        boolean zExists = ModeSelectorInternalMode.exists(getActivity().getLaunchCondition().getCapturingMode());
        this.mModeButtonShortcut.update(zExists);
        this.mMruButtonContainer = (MruButtonContainer) this.mHeadUpDisplay.findViewById(R.id.mru_button_container);
        this.mMruButtonContainer.setAvailability(!zExists);
    }

    private void setupTopIndicators() {
        this.mTopIndicatorsContainer = this.mActivity.findViewById(R.id.top_indicator_container);
        this.mLowBattery.setup((ViewStub) this.mTopIndicatorsContainer.findViewById(R.id.low_battery_indicator_stub));
        this.mLowInternalStorage.setup((ViewStub) this.mTopIndicatorsContainer.findViewById(R.id.low_internal_storage_indicator_stub));
        this.mLowSdCard.setup((ViewStub) this.mTopIndicatorsContainer.findViewById(R.id.low_sd_card_indicator_stub));
        this.mPhotoSmileCapture.setup((ViewStub) this.mTopIndicatorsContainer.findViewById(R.id.photo_smile_capture_indicator_stub));
        this.mVideoSmileCapture.setup((ViewStub) this.mTopIndicatorsContainer.findViewById(R.id.video_smile_capture_indicator_stub));
    }

    private void setupSettingIndicators() {
        this.mGeoTag = new GeotagIndicator((ImageView) this.mActivity.findViewById(R.id.geo_tag_indicator));
        this.mGeoTag.setSensorOrientation(this.mCurrentOrientation);
        this.mThermal = new Indicator((ImageView) this.mActivity.findViewById(R.id.thermal_indicator));
        this.mThermal.setSensorOrientation(this.mCurrentOrientation);
    }

    private void setupSceneIndicators() {
        this.mSceneIndicator = new IconTextIndicator((ImageView) this.mActivity.findViewById(R.id.scene_indicator_icon), (TextView) this.mActivity.findViewById(R.id.scene_indicator_text));
        this.mSceneIndicator.setSensorOrientation(this.mCurrentOrientation);
        this.mConditionIndicator = new Indicator((ImageView) this.mActivity.findViewById(R.id.scene_indicator_condition));
        this.mConditionIndicator.setSensorOrientation(this.mCurrentOrientation);
    }

    private void setupZoombar() {
        if (this.mZoombarGroup == null) {
            this.mZoombarGroup = (FrameLayout) this.mActivity.getLayoutInflater().inflate(R.layout.zoombar, (ViewGroup) null);
            this.mZoombar = (Zoombar) this.mZoombarGroup.findViewById(R.id.zoombar);
            this.mZoombar.setSensorOrientation(this.mCurrentOrientation);
            this.mZoombar.hideImmediately();
            getLazyInflatedUiComponentContainerBack().addView(this.mZoombarGroup);
        }
        repositionZoombar();
    }

    public void repositionZoombar() {
        int width;
        if (isHeadUpDisplayReady()) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mZoombarGroup.getLayoutParams();
            if (this.mPreview.getWidth() == this.mPreview.getHeight()) {
                layoutParams.leftMargin = LayoutDependencyResolver.getViewFinderSize(this.mActivity).height() / 3;
            } else {
                layoutParams.leftMargin = 0;
            }
            int i = layoutParams.width;
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                width = this.mPreview.getHeight();
            } else {
                width = this.mPreview.getWidth();
            }
            if (width != i) {
                layoutParams.width = width;
                this.mZoombarGroup.requestLayout();
            }
        }
    }

    public View getTopIndicator() {
        return this.mTopIndicatorsContainer;
    }

    private void setupRecordingIndicator() {
        if (this.mRecordingIndicator == null) {
            this.mRecordingIndicator = (RecordingIndicator) this.mActivity.findViewById(R.id.recording_progress_indicator);
            this.mRecordingIndicator.setScreenAspect(this.mScreenAspect);
            this.mRecordingIndicator.setOrientation(this.mCurrentOrientation);
            this.mRecordingIndicator.setVisibility(8);
            this.mRecordingIndicator.prepareBeforeRecording(0);
        }
    }

    public void setupPredictiveCaptureIndicator() {
        if (this.mPredictiveCaptureIndicatorController == null) {
            this.mPredictiveCaptureIndicatorController = new PredictiveCaptureIndicatorController(this.mActivity, this.mScreenAspect);
            this.mPredictiveCaptureIndicatorController.setOrientation(this.mCurrentOrientation);
        }
    }

    public boolean isHeadUpDisplayReady() {
        return this.mHeadUpDisplay != null;
    }

    public void hideContentsViewController() {
        if (this.mContentsViewController != null) {
            this.mContentsViewController.hide();
        }
    }

    public void reloadContentsViewController(ContentPallet.ThumbnailStateListener thumbnailStateListener) {
        if (this.mContentsViewController == null) {
            setupContentsView(thumbnailStateListener);
        } else {
            this.mContentsViewController.reload();
        }
    }

    public void resume() {
        this.mIsFirstDrawn = false;
        updateLayout();
        if (this.mHeadUpDisplay != null) {
            this.mHeadUpDisplay.setVisibility(0);
        }
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
            this.mLazyInflatedUiComponentContainerFront.setOnTouchListener(null);
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

    public void showContentsViewController() {
        if (this.mContentsViewController != null) {
            this.mContentsViewController.show();
        }
    }

    public void refresh() {
        this.mHeadUpDisplay.requestLayout();
        this.mHeadUpDisplay.invalidate();
    }

    public void showBlackScreen() {
        if (this.mIsBlackScreenShowing) {
            return;
        }
        this.mIsBlackScreenShowing = true;
        this.mRootView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
    }

    public void hideBlackScreen() {
        if (this.mIsBlackScreenShowing) {
            this.mIsBlackScreenShowing = false;
            this.mRootView.setBackground(null);
        }
    }

    public void setupBlankScreen() {
        LayoutInflater layoutInflater;
        if (this.mWindowCover != null || (layoutInflater = this.mActivity.getLayoutInflater()) == null) {
            return;
        }
        this.mWindowCover = layoutInflater.inflate(R.layout.camera_window_cover, (ViewGroup) null);
        Window window = this.mActivity.getWindow();
        window.addContentView(this.mWindowCover, window.getAttributes());
    }

    public void showBlankScreen() {
        if (this.mWindowCover != null) {
            this.mWindowCover.setVisibility(0);
        }
    }

    public Zoombar getZoomBar() {
        return this.mZoombar;
    }

    private void inflate() {
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.mActivity);
        if (this.mPreInflatedHeadUpDisplay != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("HeadUpDisplay is already inflated.");
            }
            this.mHeadUpDisplay = (ViewGroup) this.mPreInflatedHeadUpDisplay;
            this.mPreInflatedHeadUpDisplay = null;
        } else {
            if (CamLog.VERBOSE) {
                CamLog.d("HeadUpDisplay is not inflated.");
            }
            this.mHeadUpDisplay = (ViewGroup) layoutInflaterFrom.inflate(R.layout.baselayout, (ViewGroup) null);
        }
        this.mHeadUpDisplayContainer.addView(this.mHeadUpDisplay);
        this.mHeadUpDisplay.getLayoutParams().width = this.mViewFinderRect.width();
        this.mHeadUpDisplay.getLayoutParams().height = this.mViewFinderRect.height();
        this.mLazyInflatedUiComponentContainerFront = (FrameLayout) this.mActivity.findViewById(R.id.lazy_inflated_ui_component_container);
        this.mLazyInflatedUiComponentContainerFullScreen = (FrameLayout) this.mActivity.findViewById(R.id.lazy_inflated_ui_component_container_fullscreen);
    }

    public void setPreInflatedHeadUpDisplay(View view) {
        this.mPreInflatedHeadUpDisplay = view;
    }

    public void requestToDimSystemUi() {
        LayoutDependencyResolver.requestToDimSystemUi(this.mRootView);
        setCurrentNavigationBarVisibility(NavigationBarVisibility.LOW_PROFILE);
    }

    public void requestToRecoverSystemUi() {
        LayoutDependencyResolver.requestToRecoverSystemUi(this.mRootView);
        setCurrentNavigationBarVisibility(NavigationBarVisibility.VISIBLE);
    }

    public void requestToRestoreSystemUi() {
        if (this.mNavigationBarVisibility == null) {
        }
        switch (getPreviousNavigationBarVisibility()) {
            case VISIBLE:
                requestToRecoverSystemUi();
                break;
            case LOW_PROFILE:
                requestToDimSystemUi();
                break;
        }
    }

    void setCurrentNavigationBarVisibility(NavigationBarVisibility navigationBarVisibility) {
        this.mNavigationBarVisibility = navigationBarVisibility;
    }

    private NavigationBarVisibility getPreviousNavigationBarVisibility() {
        return this.mNavigationBarVisibility;
    }

    public ViewGroup getRootView() {
        return this.mRootView;
    }

    public FrameLayout getCenterContainer() {
        return (FrameLayout) this.mActivity.findViewById(R.id.center_container);
    }

    public FrameLayout getHintTextViewContainer() {
        if (this.mHintTextViewContainer != null) {
            return this.mHintTextViewContainer;
        }
        this.mHintTextViewContainer = (FrameLayout) this.mActivity.findViewById(R.id.hint_text_view_container);
        return this.mHintTextViewContainer;
    }

    public FrameLayout getPreviewOverlayContainer() {
        return this.mPreviewContainerLayout.mPreviewOverlayContainer;
    }

    public RelativeLayout getPreviewContainerRoot() {
        return this.mPreviewContainerLayout;
    }

    public FrameLayout getLazyInflatedUiComponentContainerBack() {
        FrameLayout frameLayout = (FrameLayout) this.mActivity.findViewById(R.id.lazy_inflated_ui_component_container_back);
        if (this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            frameLayout.setPadding(ResourceUtil.getDimensionPixelSize(this.mActivity, this.mActivity.getPackageName(), R.dimen.left_icon_area_height), 0, 0, 0);
        }
        return frameLayout;
    }

    public CameraActivity getActivity() {
        return this.mActivity;
    }

    public BurstCountView getBurstCountView() {
        FrameLayout centerContainer = getCenterContainer();
        View viewFindViewById = centerContainer.findViewById(R.id.burst_count_view);
        if (viewFindViewById == null) {
            View.inflate(this.mActivity, R.layout.shutter_burst_count_view, centerContainer);
            viewFindViewById = centerContainer.findViewById(R.id.burst_count_view);
        }
        return (BurstCountView) viewFindViewById;
    }

    public void showLeftIconContainer() {
        this.mActivity.findViewById(R.id.left_container).setVisibility(0);
    }

    public void hideLeftIconContainer() {
        this.mActivity.findViewById(R.id.left_container).setVisibility(4);
    }

    public void hideAutoReview() {
        if (this.mAutoReview != null) {
            this.mAutoReview.hide();
        }
    }

    public void setGridLineViewEnabled(boolean z) {
        if (z) {
            enableGridLineView();
        } else {
            disableGridLineView();
        }
    }

    public void enableGridLineView() {
        if (this.mGridLineView != null) {
            this.mGridLineView.enable();
        }
    }

    public void disableGridLineView() {
        if (this.mGridLineView != null) {
            this.mGridLineView.disable();
        }
    }

    public void updateGridLine(int i, int i2) {
        if (this.mGridLineView == null || i <= 1 || i2 <= 1) {
            return;
        }
        this.mGridLineView.setViewSize(i, i2);
    }

    private void setupGridLineView() {
        if (this.mGridLineView == null) {
            this.mGridLineView = new GridLineView(this.mActivity);
            this.mGridLineView.setVisibility(4);
            Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(this.mViewFinderRect);
            updateGridLine(rectAccordingToLayoutOrientation.width(), rectAccordingToLayoutOrientation.height());
            this.mPreviewContainerLayout.mPreviewContainer.addView(this.mGridLineView);
        }
    }

    private void setupPrimaryShortcut() {
        this.mPrimaryShortcut = (PrimaryShortcutGroup) this.mActivity.findViewById(R.id.primary_shortcut_group);
    }

    public PrimaryShortcutGroup getPrimaryShortcut() {
        return this.mPrimaryShortcut;
    }

    public GridLineView getGridLineView() {
        return this.mGridLineView;
    }

    private static class PreviewContainerLayout extends RelativeLayout {
        private Context mContext;
        public final FrameLayout mPreviewContainer;
        public final FrameLayout mPreviewOverlayContainer;

        public PreviewContainerLayout(Context context) {
            super(context);
            this.mContext = context;
            this.mPreviewContainer = new FrameLayout(context);
            addView(this.mPreviewContainer);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -1);
            layoutParams.addRule(9, -1);
            layoutParams.addRule(10, -1);
            layoutParams.setMargins(0, 0, 0, 0);
            this.mPreviewContainer.setPadding(0, 0, 0, 0);
            this.mPreviewContainer.setLayoutParams(layoutParams);
            this.mPreviewContainer.setId(View.generateViewId());
            FrameLayout frameLayout = new FrameLayout(context);
            addView(frameLayout);
            frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            this.mPreviewOverlayContainer = new FrameLayout(context);
            frameLayout.addView(this.mPreviewOverlayContainer);
            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
            layoutParams2.gravity = 3;
            layoutParams2.setMargins(0, 0, 0, 0);
            this.mPreviewOverlayContainer.setPadding(0, 0, 0, 0);
            this.mPreviewOverlayContainer.setLayoutParams(layoutParams2);
            frameLayout.addView((ViewGroup) LayoutInflater.from(context).inflate(R.layout.viewfinder_recording_indicator, (ViewGroup) null));
        }

        public void updatePreviewContainerLayout(Rect rect, LayoutDependencyResolver.ScreenAspect screenAspect) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mPreviewContainer.getLayoutParams();
            ViewGroup.LayoutParams layoutParams2 = getLayoutParams();
            if (layoutParams2 == null || layoutParams == null) {
                return;
            }
            int dimensionPixelSize = ResourceUtil.getDimensionPixelSize(this.mContext, this.mContext.getPackageName(), R.dimen.left_icon_area_height);
            int dimensionPixelSize2 = ResourceUtil.getDimensionPixelSize(this.mContext, this.mContext.getPackageName(), R.dimen.navigator_container_width);
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                if (screenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
                    setPadding(0, dimensionPixelSize, 0, dimensionPixelSize2);
                    layoutParams.addRule(9, 0);
                    layoutParams.addRule(10, -1);
                    layoutParams.addRule(15, 0);
                    layoutParams.addRule(14, -1);
                }
                if (layoutParams2.width != rect.height()) {
                    layoutParams2.width = rect.height();
                    layoutParams2.height = rect.width();
                    requestLayout();
                    return;
                }
                return;
            }
            if (screenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
                setPadding(dimensionPixelSize, 0, dimensionPixelSize2, 0);
                layoutParams.addRule(9, -1);
                layoutParams.addRule(10, 0);
                layoutParams.addRule(14, 0);
                layoutParams.addRule(15, -1);
            }
            if (layoutParams2.width != rect.width()) {
                layoutParams2.width = rect.width();
                layoutParams2.height = rect.height();
                requestLayout();
            }
        }
    }

    public void updatePreviewContainer(int i, int i2) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getPreviewContainer().getLayoutParams();
        if (i == i2) {
            Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(this.mActivity);
            if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 1) {
                layoutParams.leftMargin = 0;
                layoutParams.topMargin = viewFinderSize.height() / 3;
            } else {
                layoutParams.topMargin = 0;
                layoutParams.leftMargin = viewFinderSize.height() / 3;
            }
        } else if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 1) {
            layoutParams.topMargin = 0;
        } else {
            layoutParams.leftMargin = 0;
        }
        layoutParams.width = i;
        layoutParams.height = i2;
        getPreviewContainer().setLayoutParams(layoutParams);
    }

    public void updateAppsUiMarginsForTalkBack() {
        int navigationBarMargin = mIsTalkbackEffective == IsTalkbackEffective.TALKBACK_ON ? LayoutDependencyResolver.getNavigationBarMargin(this.mActivity) : 0;
        View viewFindViewById = this.mActivity.findViewById(R.id.lazy_inflated_ui_component_container_apps_ui);
        if (viewFindViewById != null) {
            ((ViewGroup.MarginLayoutParams) viewFindViewById.getLayoutParams()).setMargins(0, 0, navigationBarMargin, 0);
            viewFindViewById.requestLayout();
        }
    }

    private void setupSwitchAnimationView() {
        if (this.mSwitchAnimationView == null) {
            this.mSwitchAnimationView = new SwitchAnimationView(this.mActivity);
            this.mSwitchAnimationContainer.addView(this.mSwitchAnimationView);
        }
    }

    public void computeRadiusOfAnimation() {
        if (this.mSwitchAnimationView == null) {
            return;
        }
        this.mSwitchAnimationView.setMaxRadius(this.mViewFinderRect);
    }

    public SwitchAnimationView getSwitchAnimationView() {
        return this.mSwitchAnimationView;
    }

    private void switchCaptureButtonGroupContainer() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        if (this.mCapturingButtonLayout == null || this.mOnScreenButtonGroup == null || (viewGroup = (ViewGroup) this.mHeadUpDisplay.findViewById(R.id.capture_button_container)) == null || (viewGroup2 = (ViewGroup) this.mCapturingButtonLayout.findViewById(R.id.preloaded_capture_button_container)) == null) {
            return;
        }
        viewGroup2.removeView(this.mOnScreenButtonGroup);
        viewGroup.addView(this.mOnScreenButtonGroup);
        this.mCapturingButtonLayout.setVisibility(8);
        this.mCapturingButtonLayout = null;
        if (this.mIsBlackScreenShowing) {
            return;
        }
        this.mRootView.setBackground(null);
    }

    public boolean isAutoReviewShowing() {
        if (this.mAutoReview != null) {
            return this.mAutoReview.isShowing();
        }
        return false;
    }

    public void setIsCameraSwitching(boolean z) {
        this.mIsCameraSwitching = z;
    }

    public boolean isCameraSwitching() {
        return this.mIsCameraSwitching;
    }

    public void setupPredictiveLaunchCoverView(PredictiveLaunchCoverView.PredictiveLaunchCoverTouchListener predictiveLaunchCoverTouchListener, PredictiveLaunchCoverView.PredictiveLaunchCoverType predictiveLaunchCoverType) {
        if (this.mPredictiveLaunchCoverContainer == null || this.mPredictiveLaunchCoverView != null) {
            return;
        }
        this.mPredictiveLaunchCoverView = PredictiveLaunchCoverView.inflate(getActivity(), predictiveLaunchCoverTouchListener, predictiveLaunchCoverType);
        this.mPredictiveLaunchCoverContainer.addView(this.mPredictiveLaunchCoverView);
        this.mPredictiveLaunchCoverView.updateLayout(2);
    }

    public PredictiveLaunchCoverView getPredictiveLaunchCoverView() {
        return this.mPredictiveLaunchCoverView;
    }

    public void releasePredictiveLaunchCover() {
        if (this.mPredictiveLaunchCoverView != null) {
            this.mPredictiveLaunchCoverView.setOnTouchListener(null);
            this.mPredictiveLaunchCoverView.setVisibility(8);
            this.mPredictiveLaunchCoverContainer.removeView(this.mPredictiveLaunchCoverView);
            this.mPredictiveLaunchCoverView = null;
        }
    }

    public void hidePredictiveLaunchCover(Animatable2.AnimationCallback animationCallback) {
        if (this.mPredictiveLaunchCoverView != null) {
            this.mPredictiveLaunchCoverView.hide(animationCallback);
        }
    }
}
