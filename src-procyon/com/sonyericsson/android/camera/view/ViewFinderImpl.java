// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButton;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.view.hint.HintTextThermal;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogRequest;
import com.sonyericsson.android.camera.setting.StoredSettings;
import com.sonyericsson.android.camera.view.hint.HintTextSlowMotionDescription;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.HelpGuide;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.view.Window;
import com.sonyericsson.cameracommon.focusview.FocusActionListener;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;
import com.sonyericsson.cameracommon.focusview.FocusRectanglesViewList;
import com.sonyericsson.android.camera.controller.GestureShutter;
import android.graphics.Color;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.android.camera.view.tutorial.TutorialContentView;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Animatable2$AnimationCallback;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonyericsson.android.camera.controller.VibrationManager;
import com.sonyericsson.android.camera.view.hint.HintTextAutoPowerOff;
import com.sonyericsson.android.camera.util.CoordinateUtil;
import android.graphics.RectF;
import com.sonyericsson.android.camera.view.hint.HintTextThermalWarning;
import android.text.TextUtils;
import com.sonyericsson.android.camera.view.overlaycontrol.ImageQualityControl;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.view.hint.HintTextHighSensitivityFusionCondition;
import com.sonyericsson.android.camera.view.hint.HintTextHighSensitivityFusionStatus;
import android.support.annotation.Nullable;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import com.sonyericsson.cameracommon.viewfinder.InflateTask;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationFactory;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener;
import android.view.WindowManager$LayoutParams;
import android.view.LayoutInflater;
import com.sonyericsson.android.camera.view.overlaycontrol.EnumValueAccessor;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import android.widget.RelativeLayout$LayoutParams;
import android.util.AttributeSet;
import com.sonyericsson.cameracommon.capturefeedback.contextview.GLSurfaceContextView;
import com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector;
import com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar;
import com.sonyericsson.android.camera.view.baselayout.PredictiveLaunchCoverView;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.content.ActivityNotFoundException;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener;
import com.sonyericsson.cameracommon.utility.RegionConfig;
import com.sonyericsson.cameracommon.review.ReviewWindowListener;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.android.camera.view.hint.HintTextTimedOutMessage;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import android.util.Log;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.cameracommon.utility.ViewUtility;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import com.sonyericsson.android.camera.SideTouchEventDetector;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import android.os.Process;
import com.sonymobile.cameracommon.view.RecognizedScene;
import com.sonymobile.cameracommon.view.RecognizedCondition;
import com.sonyericsson.android.camera.device.CameraParameters;
import android.app.KeyguardManager$KeyguardDismissCallback;
import android.app.KeyguardManager;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;
import android.graphics.PointF;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.util.Size;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.storage.SavingRequest;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowMotionVideoRecording;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingTimeIndicator;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.view.hint.HintTextContent;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonItemFactory;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.android.camera.view.hint.HintTextStandardSlowMotionDescription;
import com.sonyericsson.android.camera.view.hint.HintTextStandardSlowMotion;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowShotDescription;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowShot;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowMotionDescription;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.VideoSmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingIndicator;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import android.graphics.YuvImage;
import com.sonyericsson.android.camera.controller.ChapterThumbnail;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.view.baselayout.BaseLayoutPattern;
import com.sonyericsson.cameracommon.storage.PhotoSavingRequest;
import com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl;
import android.net.Uri;
import com.sonyericsson.android.camera.controller.AbstractDraggingEventHandler;
import com.sonyericsson.android.camera.NavigatorContents;
import android.view.MotionEvent;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import android.app.Activity;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.baselayout.BaseLayoutPatternApplier;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import java.util.Iterator;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import android.graphics.Point;
import android.os.Bundle;
import android.content.Intent;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import com.sonyericsson.android.camera.view.animation.AnimationRequest;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeUtil;
import com.sonyericsson.android.camera.view.modeselector.LaunchCameraIntentBuilder;
import com.sonyericsson.android.camera.view.modeselector.AddonMode;
import com.sonyericsson.android.camera.view.modeselector.InternalMode;
import android.app.ActivityOptions;
import com.sonyericsson.android.camera.view.modeselector.Mode;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.LinkedList;
import android.content.Context;
import java.util.Arrays;
import com.sonyericsson.android.camera.setting.UiControlSettings;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.view.sidetouch.SideTouchUi;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.view.setting.SettingUi;
import com.sonyericsson.android.camera.view.setting.SettingDialogStack;
import com.sonymobile.cameracommon.view.SelfTimerCountDownView;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import android.view.View$OnTouchListener;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogController;
import com.sonyericsson.android.camera.controller.xperiaxloops.XperiaXLoopsManager;
import com.sonyericsson.android.camera.view.baselayout.LayoutPatternApplier;
import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;
import android.view.View;
import com.sonyericsson.cameracommon.viewfinder.InflateItem;
import java.util.Map;
import java.util.concurrent.Future;
import com.sonyericsson.android.camera.view.hint.HintTextViewController;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup;
import android.os.Handler;
import android.graphics.Rect;
import android.view.View$OnClickListener;
import com.sonyericsson.cameracommon.focusview.FocusRectangles;
import com.sonymobile.cameracommon.evf.Evf;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor;
import com.sonyericsson.android.camera.view.baselayout.BaseLayout;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.android.camera.view.animation.TransitionAnimationController;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import java.util.List;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.cameracommon.viewfinder.ViewFinderInterface;
import com.sonyericsson.android.camera.controller.StateMachine;

public class ViewFinderImpl implements OnStateChangedListener, ViewFinder, ViewFinderInterface, LayoutOrientationChangedListener
{
    private static final int AUTO_POWER_OFF_HINT_TEXT_TIME_OUT_TIME_MILLIS = 10000;
    private static final int COLOR_VALUE_MAX = 255;
    private static final List<DialogId> STORAGE_DIALOG_LIST;
    private static final String TAG = "ViewFinderImpl";
    private static final String THREAD_NAME = "InflateTask";
    private static final float VIEW_FINDER_DUSKY = 0.5f;
    private CameraActivity mActivity;
    private final Runnable mAfterSwitchAnimationTask;
    private TransitionAnimationController mAnimationController;
    private ApplicationNavigator mApplicationNavigator;
    private AutoReviewContentReceiverProxy mAutoReviewProxy;
    private StoreDataResult mAutoReviewStoreData;
    private BaseLayout mBaseLayout;
    private final ValueAccessor<Float> mBrightnessValueAccessor;
    private BurstCountView mBurstCountView;
    private BurstRejectedReason mBurstShootingRejectedReason;
    private CameraDeviceHandler mCameraDevice;
    private boolean mCanFocusRectanglesBeUpdated;
    private CaptureFeedback mCaptureFeedback;
    private CapturingMode mCapturingModeWhenLastSetupHeadDisplay;
    private final Runnable mCheckEvfPreparationTask;
    private final ValueAccessor<Float> mColorValueAccessor;
    private UiComponentKind mCurrentDisplayingUiComponent;
    private List<Runnable> mDelayUpdatedViewTaskList;
    private int mDisplayFlashColor;
    private Evf mEvf;
    private final Evf.LifeCycleCallback mEvfLifeCycleCallback;
    private FocusRectangles mFocusRectangles;
    private FrontAngleSwitchButton mFrontAngleSwitchButton;
    private final View$OnClickListener mFrontAngleSwitchButtonClickListener;
    private final Rect mGlobalVisibleRect;
    private final Handler mHandler;
    private OnScreenButtonGroup.MutableButtonItem mHighSensitivityFusionButtonItem;
    private boolean mHintBurstChangeCameraKeySettingAlreadyDisplayed;
    private boolean mHintBurstImageSavedToInternalStorageAlreadyDisplayed;
    private boolean mHintCannotBurstUsingFrontCameraAlreadyDisplayed;
    private boolean mHintCannotBurstUsingFusionModeAlreadyDisplayed;
    private HintTextViewController mHintText;
    private OnScreenButtonGroup.MutableButtonItem mImageQualityControlButtonItem;
    private Future<Map<InflateItem, List<View>>> mInflateFuture;
    private Map<InflateItem, List<View>> mInflateItemMap;
    private InstantViewer mInstantViewer;
    private boolean mIsAlreadySlowMotionLearnMoreButtonDisplayed;
    private boolean mIsAutoReviewRequested;
    private boolean mIsDisplayFlashScreenDisplayed;
    private boolean mIsEvfPrepared;
    private Boolean mIsFaceDetectionIdSupported;
    private boolean mIsFrontAngleChanging;
    private boolean mIsModeChanging;
    private boolean mIsNeedDisplayToastChangeInternalStoarge;
    private boolean mIsPaused;
    private boolean mIsRequestingStartActivity;
    private boolean mIsSettingChangeAcceptable;
    private boolean mIsSetupHeadupDisplayInvoked;
    private boolean mIsSurfaceViewHideWhileAspectChanging;
    private boolean mIsSwitchingAnimationProgress;
    private boolean mIsThermalWarningDialogShown;
    private LayoutPattern mLayoutPattern;
    private LayoutPatternApplier mLayoutPatternApplier;
    private XperiaXLoopsManager mLoopsManager;
    private final MessageDialogController mMessageDialog;
    private ModeLoader mModeLoader;
    private ModeSelector.OnModeSelectListener mModeSelectListener;
    private final TutorialController.OnClickSetupWizardButtonListener mOnClickTutorialButtonListener;
    private View$OnTouchListener mOnFocusRectangleTouchListener;
    private int mOrientation;
    private SelfTimer mPhotoSelfTimerSetting;
    private final PostUiInflatedTask mPostUiInflatedTask;
    private View mPreInflatedHeadUpDisplay;
    private View mPreviewCover;
    private int mPreviewOrientation;
    private PrimaryShortcutGroup mPrimaryShortcutGroup;
    private int mRecordingOrientation;
    private RecordingTimeReceiverProxy mRecordingTimeProxy;
    private boolean mRequireDisplayFlash;
    private View mSavingProgressBar;
    private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private final ScreenButtonHandler mScreenButtonHandler;
    private SelfTimerCountDownView mSelfTimerCountDownView;
    private SelfTimerCountDownView mSelfTimerCountDownViewNext;
    private SettingDialogStack mSettingDialogStack;
    private SettingMenuExclusiveListener mSettingMenuExclusiveListener;
    private SettingUi mSettingUi;
    private ShutterTrigger mShutterTrigger;
    private SideTouchUi mSideTouchUi;
    private StateMachine mStateMachine;
    private Storage.StorageStateListener mStorageStateListener;
    private View mSurfaceBlinderView;
    private TutorialController.SystemUiAccessor mSystemUiAccessor;
    private final ToastContent mToastContent;
    private TouchCapture mTouchCapture;
    private final UserEventHandler.TouchEventDispatcher mTouchEventDispatcher;
    private final UiControlSettings mUiControlSettings;
    private CaptureArea mViewFinderCaptureArea;
    private View mWindowDisplayFlashScreen;
    private ZoomBarUpdateProxy mZoomBarProxy;
    
    static {
        STORAGE_DIALOG_LIST = Arrays.asList(DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL, DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL, DialogId.MEMORY_FULL, DialogId.MEMORY_SD_UNAVAILABLE, DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_SD, DialogId.MEMORY_INTERNAL_UNAVAILABLE, DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD);
    }
    
    public ViewFinderImpl(final Context context, final boolean b, final LayoutDependencyResolver.ScreenAspect mScreenAspect, final UiControlSettings mUiControlSettings) {
        this.mCameraDevice = null;
        this.mEvf = null;
        this.mEvfLifeCycleCallback = new EvfLifeCycleCallback();
        this.mPhotoSelfTimerSetting = SelfTimer.OFF;
        this.mShutterTrigger = ShutterTrigger.OFF;
        this.mTouchCapture = null;
        this.mCaptureFeedback = null;
        this.mGlobalVisibleRect = new Rect();
        this.mRecordingOrientation = 0;
        this.mIsSurfaceViewHideWhileAspectChanging = false;
        this.mIsAutoReviewRequested = false;
        this.mBurstShootingRejectedReason = BurstRejectedReason.NONE;
        this.mIsEvfPrepared = true;
        this.mCanFocusRectanglesBeUpdated = true;
        this.mOrientation = 2;
        this.mPreviewOrientation = 2;
        this.mIsPaused = false;
        this.mIsSwitchingAnimationProgress = false;
        this.mIsThermalWarningDialogShown = false;
        this.mHintBurstImageSavedToInternalStorageAlreadyDisplayed = false;
        this.mHintCannotBurstUsingFrontCameraAlreadyDisplayed = false;
        this.mHintBurstChangeCameraKeySettingAlreadyDisplayed = false;
        this.mHintCannotBurstUsingFusionModeAlreadyDisplayed = false;
        this.mScreenButtonHandler = new ScreenButtonHandler();
        this.mDelayUpdatedViewTaskList = new LinkedList<Runnable>();
        this.mIsNeedDisplayToastChangeInternalStoarge = false;
        this.mStorageStateListener = new Storage.StorageStateListener() {
            final ViewFinderImpl this$0;
            
            @Override
            public void onStorageSizeChanged(final StorageType storageType, final long n) {
                if (CamLog.VERBOSE) {
                    CamLog.d("onAvailableSizeUpdated: ");
                }
                CameraApplication.getUiThreadHandler().post((Runnable)new Runnable(this) {
                    final ViewFinderImpl$1 this$1;
                    
                    @Override
                    public void run() {
                        this.this$1.this$0.mBaseLayout.getLowMemoryInternalIndicator().set(this.this$1.this$0.hasEnoughFreeSpace(StorageType.INTERNAL) ^ true);
                        this.this$1.this$0.mBaseLayout.getLowMemorySdIndicator().set(this.this$1.this$0.hasEnoughFreeSpace(StorageType.EXTERNAL_CARD) ^ true);
                    }
                });
            }
            
            @Override
            public void onStorageStateChanged(final StorageType storageType, final StorageState storageState, final StorageReadyState storageReadyState) {
            }
        };
        this.mModeSelectListener = new ModeSelector.OnModeSelectListener() {
            final ViewFinderImpl this$0;
            
            private int getRequestCodeFromMode(final ModeSelectorInternalMode modeSelectorInternalMode) {
                switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[modeSelectorInternalMode.ordinal()]) {
                    default: {
                        return -1;
                    }
                    case 3: {
                        return 17;
                    }
                    case 2: {
                        return 16;
                    }
                    case 1: {
                        return 18;
                    }
                }
            }
            
            @Override
            public void onModeSelected(final Mode mode, final boolean b) {
                if (!this.this$0.mIsSettingChangeAcceptable || !this.this$0.isUserOperable()) {
                    return;
                }
                final CameraActivity access$500 = this.this$0.mActivity;
                final ActivityOptions customAnimation = ActivityOptions.makeCustomAnimation((Context)access$500, 0, 0);
                if (!(mode instanceof InternalMode)) {
                    if (mode instanceof AddonMode) {
                        final CapturingModeAttributes tag = ((AddonMode)mode).getTag();
                        final Intent commit = LaunchCameraIntentBuilder.create().mode(tag.getModeName()).activity(tag.getPackageName(), tag.getActivityName()).callingMode(CapturingModeUtil.filteringPrevName(this.this$0.getCapturingMode().name())).callingActivity(((Context)access$500).getPackageName(), CapturingModeUtil.filteringPrevActivity(((Context)access$500).getClass().getName())).commit();
                        if (this.this$0.mActivity.isDeviceInSecurityLock()) {
                            this.this$0.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP, commit, customAnimation.toBundle(), mode);
                            return;
                        }
                        if (CapturingModeUtil.isActivityAvailable((Context)access$500, commit)) {
                            Bundle bundle;
                            if (customAnimation != null && customAnimation.toBundle() != null) {
                                bundle = customAnimation.toBundle();
                            }
                            else {
                                bundle = null;
                            }
                            if (this.this$0.requestStartActivity(commit, bundle)) {
                                if (b) {
                                    LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MRU_SHORTCUT);
                                    LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MRU_SHORTCUT);
                                }
                                else {
                                    LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MODE_SELECTOR);
                                    LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MODE_SELECTOR);
                                }
                                LocalResearchUtil.getInstance().sendEventAddonModeChange(Event.Category.ADDON_FW, Event.AddonFW.APP_SELECTED_ON_MODE_SELECTOR.toString(), AddonMode.generateId(tag.getPackageName(), tag.getModeName()));
                                this.this$0.sendViewUpdateEvent(ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT, mode);
                            }
                        }
                    }
                    return;
                }
                if (this.this$0.mIsRequestingStartActivity) {
                    return;
                }
                final ModeSelectorInternalMode tag2 = ((InternalMode)mode).getTag();
                if (tag2 == ModeSelectorInternalMode.DUAL_MONOCHROME) {
                    this.this$0.mSettingUi.openMonochromeDialog(b, this.this$0.getBaseLayout().calculateCaptureButtonAreaHeight(), mode);
                    return;
                }
                if (this.this$0.mActivity.isDeviceInSecurityLock() && tag2.isExternalApp) {
                    final Intent commit2 = LaunchCameraIntentBuilder.create().mode(this.this$0.getCapturingMode().name()).activity("com.sonyericsson.android.camera", "com.sonyericsson.android.camera.CameraActivity").callingMode(CapturingModeUtil.filteringPrevName(this.this$0.getCapturingMode().name())).callingActivity(((Context)access$500).getPackageName(), CapturingModeUtil.filteringPrevActivity(((Context)access$500).getClass().getName())).commit();
                    commit2.putExtra("internal_mode", tag2.ordinal());
                    commit2.putExtra("capturing_mode", this.this$0.mStateMachine.getCurrentCapturingMode().ordinal());
                    this.this$0.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP, commit2, customAnimation.toBundle(), mode);
                    return;
                }
                if (tag2.isExternalApp) {
                    final int requestCodeFromMode = this.getRequestCodeFromMode(tag2);
                    if (requestCodeFromMode != -1) {
                        if (b) {
                            LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MRU_SHORTCUT);
                            LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MRU_SHORTCUT);
                        }
                        else {
                            LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MODE_SELECTOR);
                            LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MODE_SELECTOR);
                        }
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(this.this$0.getCapturingMode(), tag2);
                        if (CapturingModeUtil.MODE_WHITE_LIST.contains(tag2.name())) {
                            if (tag2 == ModeSelectorInternalMode.DUAL_BACKGROUND_DEFOCUS) {
                                ApplicationLauncher.launchExternalCamera(this.this$0.mActivity, requestCodeFromMode, this.this$0.mStateMachine.getUserSetting(), CapturingMode.SCENE_RECOGNITION, true);
                            }
                            else {
                                ApplicationLauncher.launchExternalCamera(this.this$0.mActivity, requestCodeFromMode, this.this$0.mStateMachine.getUserSetting(), this.this$0.mStateMachine.getCurrentCapturingMode(), true);
                            }
                        }
                        else {
                            if (tag2 == ModeSelectorInternalMode.DUAL_BACKGROUND_DEFOCUS) {
                                ApplicationLauncher.launchExternalCamera(this.this$0.mActivity, requestCodeFromMode, this.this$0.mStateMachine.getUserSetting(), CapturingMode.SCENE_RECOGNITION, false);
                            }
                            else {
                                ApplicationLauncher.launchExternalCamera(this.this$0.mActivity, requestCodeFromMode, this.this$0.mStateMachine.getUserSetting(), this.this$0.mStateMachine.getCurrentCapturingMode(), false);
                            }
                            this.this$0.onAppsUiModeFinish();
                        }
                    }
                }
                else {
                    AnimationRequest.AnimationType animationType;
                    if (b) {
                        animationType = AnimationRequest.AnimationType.MRU_SHORTCUT;
                    }
                    else {
                        animationType = AnimationRequest.AnimationType.MODE_SELECTOR;
                    }
                    final AnimationRequest animationRequest = new AnimationRequest(animationType, AnimationRequest.AnimationDegree.START, this.this$0.getCapturingMode(), (CapturingMode)tag2.tag);
                    if (this.this$0.requestAnimation(animationRequest)) {
                        this.this$0.hideSurface();
                        this.this$0.setApplicationNavigatorEnabled(false);
                        this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest);
                    }
                }
                this.this$0.sendViewUpdateEvent(ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT, mode);
            }
        };
        this.mTouchEventDispatcher = new UserEventHandler.TouchEventDispatcher();
        this.mCheckEvfPreparationTask = new Runnable() {
            final ViewFinderImpl this$0;
            
            @Override
            public void run() {
                if (this.this$0.mEvf != null) {
                    this.this$0.notifyOnEvfPrepared();
                }
                else {
                    CamLog.w("All reference of ViewFinderImpl has aleady been released.");
                }
            }
        };
        this.mPostUiInflatedTask = new PostUiInflatedTask();
        this.mFrontAngleSwitchButtonClickListener = (View$OnClickListener)new View$OnClickListener() {
            final ViewFinderImpl this$0;
            
            public void onClick(final View view) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Wide front button is clicked, isCameraSwitching: ");
                    sb.append(this.this$0.isCameraSwitching());
                    CamLog.d(sb.toString());
                }
                this.this$0.mTouchEventDispatcher.sendClick(UserEventHandler.UiComponent.ANGLE_CHANGE_BUTTON, null);
            }
        };
        this.mHandler = new Handler();
        this.mAfterSwitchAnimationTask = new Runnable() {
            final ViewFinderImpl this$0;
            
            @Override
            public void run() {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke AfterSwitchAnimationTask");
                }
                this.this$0.mPreviewCover.setAlpha(0.0f);
                this.this$0.mAnimationController.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.FINISH, this.this$0.getCapturingMode(), this.this$0.getCapturingMode()), (TransitionAnimationController.TransitionAnimationCallback)new TransitionAnimationController.TransitionAnimationCallback(this) {
                    final ViewFinderImpl$19 this$1;
                    
                    @Override
                    public void onAnimationFinished() {
                        this.this$1.this$0.resetAnimationProperty();
                        this.this$1.this$0.setIsSwitchingAnimationProgress(false);
                    }
                });
            }
        };
        this.mOnClickTutorialButtonListener = new TutorialController.OnClickSetupWizardButtonListener() {
            final ViewFinderImpl this$0;
            
            private void doPostProcessing(final List<TutorialType> list) {
                final MessageSettings messageSettings = this.this$0.mActivity.getStoredSettings().getMessageSettings();
                final Iterator<TutorialType> iterator = list.iterator();
                while (iterator.hasNext()) {
                    final Iterator<MessageType> iterator2 = iterator.next().messageTypes.iterator();
                    while (iterator2.hasNext()) {
                        messageSettings.setNeverShow(iterator2.next(), true);
                        messageSettings.save();
                    }
                }
                this.this$0.setApplicationNavigatorEnabled(this.this$0.mActivity.isOneShot() ^ true);
                this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
            }
            
            @Override
            public void onAccepted(final TutorialType tutorialType) {
                if (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()] == 1) {
                    this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE, FusionMode.AUTO);
                }
            }
            
            @Override
            public void onClose(final List<TutorialType> list) {
                if (list.contains(TutorialType.SUPER_SLOW_MOTION_MORE_OPTIONS)) {
                    this.this$0.showHiSpeedSdCardRecommendDialogOnModeChange();
                }
                else if (list.contains(TutorialType.MANUAL_FUSION)) {
                    this.this$0.updateHighSensitivityFusionModeForManual();
                }
                this.doPostProcessing(list);
            }
            
            @Override
            public void onDenied(final TutorialType tutorialType) {
                if (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()] == 1) {
                    this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE, FusionMode.OFF);
                }
            }
        };
        this.mSystemUiAccessor = new TutorialController.SystemUiAccessor() {
            final ViewFinderImpl this$0;
            
            @Override
            public void onAddFlags(final int n) {
                this.this$0.mActivity.getWindow().addFlags(n);
            }
            
            @Override
            public void onClearFlags(final int n) {
                this.this$0.mActivity.getWindow().clearFlags(n);
            }
        };
        this.mRequireDisplayFlash = false;
        this.mIsDisplayFlashScreenDisplayed = false;
        this.mDisplayFlashColor = -1;
        this.mColorValueAccessor = new ValueAccessor<Float>() {
            final ViewFinderImpl this$0;
            
            @Override
            public Float get() {
                return 0.5f;
            }
            
            @Override
            public Float reset() {
                this.this$0.disableSemiAutoControl();
                return 0.5f;
            }
            
            @Override
            public void set(final Float n) {
                this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_ON_AMBER_BLUE_COLOR_CHANGED, n);
            }
        };
        this.mBrightnessValueAccessor = new ValueAccessor<Float>() {
            final ViewFinderImpl this$0;
            
            @Override
            public Float get() {
                return 0.5f;
            }
            
            @Override
            public Float reset() {
                this.this$0.disableSemiAutoControl();
                return 0.5f;
            }
            
            @Override
            public void set(final Float n) {
                this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_ON_BRIGHTNESS_CHANGED, n);
            }
        };
        this.mScreenAspect = mScreenAspect;
        this.createViewFinder((CameraActivity)context, new BaseLayoutPatternApplier(), true);
        if (CamLog.VERBOSE) {
            CamLog.d("CONSTRUCTOR:[IN]");
        }
        if (b) {
            if (this.mActivity.isKeyguardSecure()) {
                this.mActivity.getWindow().addFlags(524288);
            }
            else {
                this.dismissKeyguard();
            }
        }
        if (CamLog.DEBUG) {
            CamLog.d("CONSTRUCTOR : new Evf : E");
        }
        this.mEvf = Evf.EvfFactory.generate();
        if (CamLog.DEBUG) {
            CamLog.d("CONSTRUCTOR : new Evf : X");
        }
        this.mEvf.setLifeCycleCallback(this.mEvfLifeCycleCallback);
        this.mEvf.onCreate(context);
        if (CamLog.DEBUG) {
            CamLog.d("setContentView() : addView(Evf) : E");
        }
        this.mActivity.setContentView(this.mEvf.asView(), this.getPreviewLayoutParams());
        if (CamLog.DEBUG) {
            CamLog.d("setContentView() : addView(Evf) : X");
        }
        ((View)this.mEvf.asView().getParent()).setLayoutDirection(0);
        if (this.mSelfTimerCountDownViewNext == null) {
            this.mSelfTimerCountDownViewNext = (SelfTimerCountDownView)this.getActivity().getLayoutInflater().inflate(2131492995, (ViewGroup)null);
        }
        this.mToastContent = new ToastContent();
        this.mIsSetupHeadupDisplayInvoked = false;
        this.mMessageDialog = new MessageDialogController(this.mActivity, this.mActivity.getStoredSettings().getMessageSettings(), (MessageDialogController.MessageDialogOnClickListener)new MessageDialogOnClickPositiveListenerImpl(), (MessageDialogController.MessageDialogOnClickListener)new MessageDialogOnClickNegativeListenerImpl(), (MessageDialogController.MessageDialogOnCancelListener)new MessageDialogOnCancelListenerImpl(), (MessageDialogController.MessageDialogOnDismissListener)new MessageDialogOnDismissListenerImpl(), (MessageDialogController.MessageDialogOnOpenListener)new MessageDialogOnOpenListenerImpl());
        this.mUiControlSettings = mUiControlSettings;
    }
    
    private void PostActionToMainThread(final UserSettingKey userSettingKey) {
        CameraApplication.getUiThreadHandler().post((Runnable)new ActionRunnable(userSettingKey));
    }
    
    private void addThumbnail(final StoreDataResult storeDataResult) {
        final int requestId = storeDataResult.savingRequest.getRequestId();
        final boolean success = storeDataResult.isSuccess();
        final Uri uri = storeDataResult.uri;
        final String filePath = storeDataResult.savingRequest.getFilePath();
        boolean predictiveCaptureImage = false;
        boolean predictiveCaptureCoverImage = false;
        Label_0088: {
            if (filePath != null) {
                if (storeDataResult.savingRequest instanceof PhotoSavingRequest) {
                    predictiveCaptureImage = ((PhotoSavingRequest)storeDataResult.savingRequest).isPredictiveCaptureImage();
                    predictiveCaptureCoverImage = ((PhotoSavingRequest)storeDataResult.savingRequest).isPredictiveCaptureCoverImage();
                    break Label_0088;
                }
            }
            else {
                CamLog.d("File path is not set by storage error.");
            }
            predictiveCaptureCoverImage = false;
        }
        if (!predictiveCaptureImage || predictiveCaptureCoverImage) {
            this.mActivity.runOnUiThread((Runnable)new Runnable(this, storeDataResult, requestId, success, uri) {
                final ViewFinderImpl this$0;
                final Uri val$originalUri;
                final int val$requestId;
                final StoreDataResult val$result;
                final boolean val$resultCode;
                
                @Override
                public void run() {
                    if (this.this$0.getBaseLayout().getContentsViewController() != null) {
                        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[this.val$result.savingRequest.common.savedFileType.ordinal()]) {
                            case 2: {
                                final LayoutPattern access$5800 = this.this$0.getCurrentLayoutPattern();
                                if (access$5800 == BaseLayoutPattern.RECORDING || access$5800 == BaseLayoutPattern.PAUSE_RECORDING) {
                                    this.this$0.startHideThumbnail();
                                    break;
                                }
                                break;
                            }
                            case 1: {
                                if (this.val$result.savingRequest.isFinalInSavingGroup()) {
                                    this.this$0.getBaseLayout().getContentsViewController().requestLastContentLoading(this.val$requestId);
                                }
                                return;
                            }
                        }
                        if (this.val$requestId != -1) {
                            if (this.val$resultCode) {
                                PerfLog.STORE_COMPLETE.transit();
                                this.this$0.getBaseLayout().getContentsViewController().addContent(this.val$requestId, this.val$originalUri);
                                PerfLog.THUMBNAIL_SHOW.transit();
                            }
                            else {
                                this.this$0.getBaseLayout().getContentsViewController().pause();
                                this.this$0.getBaseLayout().getContentsViewController().reload();
                            }
                        }
                        else {
                            this.this$0.getBaseLayout().getContentsViewController().remove();
                            this.this$0.getBaseLayout().getContentsViewController().pause();
                            this.this$0.getBaseLayout().getContentsViewController().reload();
                        }
                    }
                }
            });
        }
    }
    
    private void addVideoChapter(final ChapterThumbnail chapterThumbnail) {
        final RecordingIndicator recordingIndicator = this.getBaseLayout().getRecordingIndicator();
        final YuvImage yuvImage = new YuvImage(chapterThumbnail.yuvData, (int)chapterThumbnail.format, chapterThumbnail.rect.width(), chapterThumbnail.rect.height(), (int[])null);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (yuvImage.compressToJpeg(chapterThumbnail.rect, 80, (OutputStream)byteArrayOutputStream)) {
            recordingIndicator.addChapter(byteArrayOutputStream.toByteArray(), chapterThumbnail.orientation());
        }
    }
    
    private void applyShutterTriggerSettings() {
        this.mStateMachine.sendStaticEvent(StaticEvent.EVENT_ON_GESTURE_SHUTTER_SETTING_CHANGED, this.mShutterTrigger.isGestureShutterOn());
        final SmileCapture smileCapture = (SmileCapture)this.mStateMachine.getUserSetting().get(UserSettingKey.SMILE_CAPTURE);
        final VideoSmileCapture videoSmileCapture = (VideoSmileCapture)this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SMILE_CAPTURE);
        if (this.getBaseLayout() != null && smileCapture != null && videoSmileCapture != null) {
            this.getBaseLayout().getPhotoSmileCaptureIndicator().set(smileCapture.isSmileCaptureOn());
            this.getBaseLayout().getPhotoSmileCaptureIndicator().setBackgroundResource(smileCapture.getNotificationIconId());
            this.getBaseLayout().getVideoSmileCaptureIndicator().set(videoSmileCapture.isSmileCaptureOn());
            this.getBaseLayout().getVideoSmileCaptureIndicator().setBackgroundResource(videoSmileCapture.getNotificationIconId());
            this.applySmileFocusThreshold(true);
        }
    }
    
    private void applySmileFocusThreshold(final boolean b) {
        if (this.mFocusRectangles != null) {
            int dimenId;
            final int n = dimenId = -1;
            if (b) {
                final SmileCapture smileCapture = (SmileCapture)this.mStateMachine.getUserSetting().get(UserSettingKey.SMILE_CAPTURE);
                final VideoSmileCapture videoSmileCapture = (VideoSmileCapture)this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SMILE_CAPTURE);
                int dimenId2 = n;
                if (smileCapture != SmileCapture.OFF) {
                    dimenId2 = n;
                    if (!this.isZooming()) {
                        dimenId2 = n;
                        if (!this.isInSelfTimerCountDown()) {
                            dimenId2 = smileCapture.getDimenId();
                        }
                    }
                }
                dimenId = dimenId2;
                if (videoSmileCapture != VideoSmileCapture.OFF) {
                    dimenId = dimenId2;
                    if (!this.isZooming()) {
                        dimenId = dimenId2;
                        if (!this.isInSelfTimerCountDown()) {
                            dimenId = dimenId2;
                            if (this.mStateMachine.isRecording()) {
                                dimenId = videoSmileCapture.getDimenId();
                            }
                        }
                    }
                }
            }
            this.mFocusRectangles.setSmileCaptureThreshold(dimenId);
        }
    }
    
    private boolean attachSideAutoReview() {
        if (this.mSideTouchUi.containsIn(SideTouchUi.Type.CAPTURE_COUNTDOWN, SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR, SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE, SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL)) {
            this.mIsAutoReviewRequested = true;
            this.mSideTouchUi.attachIcon(SideTouchUi.Type.AUTO_REVIEW, null);
            return true;
        }
        return false;
    }
    
    private void attemptSetupMruButton(final CapturingMode capturingMode) {
        if (this.mModeLoader == null) {
            this.setupMruButton(capturingMode);
        }
    }
    
    private void cancelCheckEvfPreparationTask() {
        CameraApplication.getUiThreadHandler().removeCallbacks(this.mCheckEvfPreparationTask);
    }
    
    private void cancelSelfTimerCountDownView() {
        if (this.mSelfTimerCountDownView != null) {
            this.mSelfTimerCountDownView.cancelSelfTimerCountDownAnimation();
            this.getBaseLayout().getLazyInflatedUiComponentContainerBack().removeView((View)this.mSelfTimerCountDownView);
        }
    }
    
    private void cancelSlowMotionHintText() {
        if (this.mHintText != null) {
            this.mHintText.cancel(HintTextSuperSlowMotion.class.getSimpleName());
            this.mHintText.cancel(HintTextSuperSlowMotionDescription.class.getSimpleName());
            this.mHintText.cancel(HintTextSuperSlowShot.class.getSimpleName());
            this.mHintText.cancel(HintTextSuperSlowShotDescription.class.getSimpleName());
            this.mHintText.cancel(HintTextStandardSlowMotion.class.getSimpleName());
            this.mHintText.cancel(HintTextStandardSlowMotionDescription.class.getSimpleName());
        }
    }
    
    private void changeLayoutTo(final LayoutPattern layoutPattern) {
        this.changeLayoutTo(layoutPattern, false);
    }
    
    private void changeLayoutTo(final LayoutPattern layoutPattern, final boolean b) {
        if (this.isHeadUpDisplayReady()) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("changeLayoutTo: ");
                sb.append(layoutPattern);
                CamLog.d(sb.toString());
            }
            if (!b) {
                this.mLayoutPatternApplier.apply(layoutPattern);
            }
            else {
                this.mLayoutPatternApplier.apply(BaseLayoutPattern.CLEAR);
            }
            this.mLayoutPattern = layoutPattern;
            if (this.isPreviewLayout(layoutPattern)) {
                if (this.needToShowGeoTagIndicator()) {
                    this.mBaseLayout.getGeoTagIndicator().set(GeotagManager.isGeoTagEnabled(this.mActivity.getStoredSettings().getUserSettings(), (Context)this.mActivity));
                }
                if (this.mActivity.getGeoTagManager() != null) {
                    this.mBaseLayout.getGeoTagIndicator().isAcquired(this.isAcquired());
                }
            }
            this.updateAllOverlayControlVisibility();
            this.updateVisibilityForSpecificDisplaySize();
            if (layoutPattern.equals(BaseLayoutPattern.SELFTIMER)) {
                this.clearTouchedScreenButtonGroup();
            }
        }
        if (this.mActivity.isOneShot()) {
            this.getBaseLayout().hideContentsViewController();
        }
    }
    
    private void changeOnScreenCaptureButtonInAutoFront() {
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(this.getCaptureButtonTypeAccordingToSelfTimerSetting(), this.getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }
    
    private void changeOnScreenCaptureButtonInAutoMain() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInAutoMain()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(this.getCaptureButtonTypeAccordingToSelfTimerSetting(), this.getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }
    
    private void changeOnScreenCaptureButtonInManualFront() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInManualFront()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(this.getCaptureButtonTypeAccordingToSelfTimerSetting(), this.getOrientation(), true);
        this.mScreenButtonHandler.setOption2(this.mImageQualityControlButtonItem, this.getOrientation(), true);
    }
    
    private void changeOnScreenCaptureButtonInManualMain() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInManualMain()");
        }
        if (PlatformCapability.isHighSensitivityFusionSupported(this.getCapturingMode().getCameraId())) {
            this.getBaseLayout().getOnScreenButtonGroup().setOption1((OnScreenButtonGroup.Item)this.mHighSensitivityFusionButtonItem, this.getOrientation(), true);
        }
        else {
            this.mScreenButtonHandler.clearOption1();
        }
        this.mScreenButtonHandler.setMain(this.getCaptureButtonTypeAccordingToSelfTimerSetting(), this.getOrientation(), true);
        this.mScreenButtonHandler.setOption2(this.mImageQualityControlButtonItem, this.getOrientation(), true);
    }
    
    private void changeOnScreenCaptureButtonInSelfTimerCoundDown() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInSelfTimerCoundDown()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.CAPTURE_LARGE, this.getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }
    
    private void changeOnScreenCaptureButtonInVideo() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInVideo()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(this.createStartRecordingButton(), this.getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.VIDEO_RECORDING_STOP_READY_FOR_USE);
    }
    
    private void changeOnScreenCaptureButtonInVideoPausing() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInVideo()");
        }
        this.mScreenButtonHandler.setOption1(OnScreenButtonItemFactory.ButtonType.RESUME_RECORDING_SMALL, this.mRecordingOrientation, false);
        this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_IN_PAUSE_LARGE, this.mRecordingOrientation, false);
        final VideoHdr videoHdr = (VideoHdr)this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_HDR);
        if (!this.mActivity.isOneShotVideo() && videoHdr != VideoHdr.HDR_ON) {
            this.mScreenButtonHandler.setOption2(OnScreenButtonItemFactory.ButtonType.CAPTURE_SMALL, this.getOrientation(), true);
        }
        else {
            this.mScreenButtonHandler.clearOption2();
        }
    }
    
    private void changeOnScreenCaptureButtonInVideoRecording() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInSelfTimerCountdown()");
        }
        final VideoSize videoSize = (VideoSize)this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SIZE);
        if (videoSize == null || !videoSize.isConstraint()) {
            this.mScreenButtonHandler.setOption1(OnScreenButtonItemFactory.ButtonType.PAUSE_RECORDING_SMALL, this.mRecordingOrientation, false);
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_LARGE, this.mRecordingOrientation, false);
            if (this.mActivity.isOneShotVideo()) {
                this.mScreenButtonHandler.clearOption2();
            }
            else if (this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_HDR) != VideoHdr.HDR_ON) {
                this.mScreenButtonHandler.setOption2(OnScreenButtonItemFactory.ButtonType.CAPTURE_SMALL, this.getOrientation(), true);
            }
            else {
                this.getBaseLayout().getOnScreenButtonGroup().clearOption2();
            }
        }
        else {
            this.mScreenButtonHandler.clearOption1();
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_LARGE, this.mRecordingOrientation, false);
            this.mScreenButtonHandler.clearOption2();
        }
    }
    
    private void changeOnScreenStandardSlowMotionButtonInVideo() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenStandardSlowMotionButtonInVideo()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(this.createStartRecordingButton(), this.getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }
    
    private void changeOnScreenStandardSlowMotionButtonInVideoRecording() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenStandardSlowMotionButtonInVideoRecording()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_LARGE, this.mRecordingOrientation, false);
        this.mScreenButtonHandler.clearOption2();
    }
    
    private void changeOnScreenSuperSlowMotionButtonInVideo() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenSuperSlowMotionButtonInVideo()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(this.createStartRecordingButton(), this.getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }
    
    private void changeOnScreenSuperSlowMotionRecordingButtonInVideo() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenSuperSlowMotionRecordingButtonInVideo()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION, this.mRecordingOrientation, false);
        this.mScreenButtonHandler.setOption2(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_SMALL, this.mRecordingOrientation, false);
    }
    
    private void changeOnScreenSuperSlowShotButtonInVideo() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenSuperSlowShotButtonInVideo()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(this.createStartRecordingButton(), this.getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }
    
    private void changeScreenButtonImage(final HeadUpDisplaySetupState headUpDisplaySetupState, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("state : ");
            sb.append(headUpDisplaySetupState);
            CamLog.d(sb.toString());
        }
        if (this.getBaseLayout() != null && this.getBaseLayout().getOnScreenButtonGroup() != null) {
            Label_0308: {
                switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[headUpDisplaySetupState.ordinal()]) {
                    default: {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("ViewFinder.changeScreenButtonBackground():[Unexpected system bar status.] state = ");
                        sb2.append(headUpDisplaySetupState);
                        throw new IllegalStateException(sb2.toString());
                    }
                    case 11: {
                        this.changeOnScreenCaptureButtonInVideoPausing();
                        break;
                    }
                    case 10: {
                        this.changeOnScreenSuperSlowShotButtonInVideo();
                        break;
                    }
                    case 9: {
                        this.changeOnScreenStandardSlowMotionButtonInVideoRecording();
                        break;
                    }
                    case 8: {
                        this.changeOnScreenStandardSlowMotionButtonInVideo();
                        break;
                    }
                    case 7: {
                        this.changeOnScreenSuperSlowMotionRecordingButtonInVideo();
                        break;
                    }
                    case 6: {
                        this.changeOnScreenSuperSlowMotionButtonInVideo();
                        break;
                    }
                    case 5: {
                        this.changeOnScreenCaptureButtonInVideoRecording();
                        break;
                    }
                    case 4: {
                        this.changeOnScreenCaptureButtonInVideo();
                        break;
                    }
                    case 1:
                    case 2:
                    case 3: {
                        if (b) {
                            this.changeOnScreenCaptureButtonInSelfTimerCoundDown();
                            break;
                        }
                        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.getCapturingMode().ordinal()]) {
                            default: {
                                break Label_0308;
                            }
                            case 6: {
                                this.changeOnScreenCaptureButtonInManualFront();
                                break Label_0308;
                            }
                            case 5: {
                                this.changeOnScreenCaptureButtonInManualMain();
                                break Label_0308;
                            }
                            case 2: {
                                this.changeOnScreenCaptureButtonInAutoFront();
                                break Label_0308;
                            }
                            case 1: {
                                this.changeOnScreenCaptureButtonInAutoMain();
                                break Label_0308;
                            }
                        }
                        break;
                    }
                }
            }
            if (!this.isHeadUpDisplayReady()) {
                this.getBaseLayout().getOnScreenButtonGroup().clearOption1();
                this.getBaseLayout().getOnScreenButtonGroup().clearOption2();
            }
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Screen button group is not created.");
        }
    }
    
    private void changeToBurstCaptureView() {
        this.changeLayoutTo(BaseLayoutPattern.CAPTURE);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.hideApplicationNavigator();
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
    }
    
    private void changeToBurstCaptureWaitForAfDoneView() {
        if (this.mLayoutPattern != BaseLayoutPattern.FOCUS_SEARCHING) {
            this.changeToPhotoFocusView();
        }
        this.changeToPhotoCaptureWaitForAfDoneView();
    }
    
    private void changeToDialogView(final UiComponentKind uiComponentKind) {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (uiComponentKind != UiComponentKind.OVERLAY_CONTROL_SEEKING || !this.isTouchFocus()) {
            this.mFocusRectangles.onUiComponentOverlaid();
        }
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[uiComponentKind.ordinal()]) {
            case 12: {
                this.openTutorial(TutorialController.DisplayTrigger.CHANGE_MODE);
                break;
            }
            case 11: {
                this.changeLayoutTo(BaseLayoutPattern.OVERLAY_CONTROL_SEEKING);
                break;
            }
            case 10: {
                this.changeLayoutTo(BaseLayoutPattern.CLEAR);
                this.setLeftIconsVisibility(false);
                break;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {
                this.changeLayoutTo(BaseLayoutPattern.SETTING);
                this.mHintText.hide();
                break;
            }
        }
        if (uiComponentKind == UiComponentKind.OVERLAY_CONTROL_SEEKING) {
            this.setFrontAngleSwitchButtonVisibility(this.isFront());
            this.setFrontAngleSwitchButtonClickable(false);
        }
        else {
            this.setFrontAngleSwitchButtonVisibility(false);
        }
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        this.mSideTouchUi.destroyIcon();
    }
    
    private void changeToLayoutWithSetupState(final HeadUpDisplaySetupState headUpDisplaySetupState) {
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[headUpDisplaySetupState.ordinal()]) {
            default: {
                throw new IllegalStateException("setupHeadUpDisplay():[Illegal State]");
            }
            case 5: {
                this.changeToVideoRecordingView();
                break;
            }
            case 4: {
                this.changeToVideoReadyView();
                break;
            }
            case 3: {
                this.changeToBurstCaptureView();
                break;
            }
            case 2: {
                this.changeToPhotoCaptureView();
                break;
            }
            case 1: {
                this.changeToPhotoReadyView(true);
                break;
            }
        }
        if (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation[this.mActivity.getLaunchCondition().getExtraOperation().ordinal()] == 1) {
            final String userSettingKeyName = this.mActivity.getLaunchCondition().getUserSettingKeyName();
            if (userSettingKeyName == null) {
                this.PostActionToMainThread(null);
            }
            else {
                this.PostActionToMainThread(UserSettingKey.valueOf(userSettingKeyName));
            }
        }
    }
    
    private void changeToModeTransitionView() {
        this.changeLayoutTo(BaseLayoutPattern.MODE_CHANGING);
        this.hideViews();
        this.disablePrimaryShortcut();
        this.disableModeIconClickable();
        this.sendViewUpdateEvent(ViewUpdateEvent.EVENT_CLOSE_ALL_DIALOGS, new Object[0]);
        this.cancelPredictiveCaptureIndicatorAnimation();
        this.setFrontAngleSwitchButtonVisibility(false);
        if (this.mFocusRectangles != null) {
            this.mFocusRectangles.clearFaceDetection();
        }
        this.mToastContent.closeMessage();
        this.clearMessageDialog();
        if (this.mHintText != null) {
            this.mHintText.clearAll();
        }
        this.hideAutoReview();
        this.hideZoomBar();
        this.mIsModeChanging = true;
        this.mSideTouchUi.destroyIcon();
    }
    
    private void changeToPauseView() {
        this.changeLayoutTo(BaseLayoutPattern.CLEAR);
        this.hideApplicationNavigator();
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (this.mFocusRectangles != null) {
            this.mFocusRectangles.clearAllFocus();
        }
        if (this.mIsModeChanging) {
            this.mIsModeChanging = false;
        }
        else {
            this.getBaseLayout().getContentsViewController().remove();
        }
        this.hideAutoReview();
        this.setFrontAngleSwitchButtonVisibility(false);
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        this.mSideTouchUi.destroyIcon();
    }
    
    private void changeToPhotoCaptureView() {
        this.changeLayoutTo(BaseLayoutPattern.CAPTURE);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.setFrontAngleSwitchButtonVisibility(false);
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
    }
    
    private void changeToPhotoCaptureWaitForAfDoneView() {
        this.changeLayoutTo(BaseLayoutPattern.CAPTURE);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.changeToPhotoFocusView();
        if (PlatformCapability.isFocusSupported(this.getCapturingMode().getCameraId()) && this.mStateMachine.getUserSetting().get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
            this.mFocusRectangles.onAutoFocusStarted();
        }
        this.setFrontAngleSwitchButtonVisibility(false);
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        if (!this.mSideTouchUi.detachTo(SideTouchUi.Type.CAPTURE_COUNTDOWN) && !this.mSideTouchUi.detachTo(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL)) {
            this.mSideTouchUi.destroyIcon();
        }
    }
    
    private void changeToPhotoFocusDoneView(final Boolean b) {
        this.changeLayoutTo(BaseLayoutPattern.FOCUS_DONE);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.changeToPhotoFocusView();
        if (PlatformCapability.isFocusSupported(this.getCapturingMode().getCameraId()) && this.mStateMachine.getUserSetting().get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
            this.mFocusRectangles.onAutoFocusDone(b);
        }
        this.setFrontAngleSwitchButtonVisibility(false);
    }
    
    private void changeToPhotoFocusSearchView() {
        this.changeLayoutTo(BaseLayoutPattern.FOCUS_SEARCHING);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.changeToPhotoFocusView();
        if (PlatformCapability.isFocusSupported(this.getCapturingMode().getCameraId())) {
            if (this.mStateMachine.getUserSetting().get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                this.mFocusRectangles.onAutoFocusStarted();
            }
            else if (this.isTouchFocus()) {
                this.mFocusRectangles.onUiComponentOverlaid();
            }
        }
        this.setFrontAngleSwitchButtonVisibility(false);
        this.mSideTouchUi.detachTo(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL);
    }
    
    private void changeToPhotoFocusView() {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.setFrontAngleSwitchButtonVisibility(false);
        this.setLeftIconsVisibility(false);
        this.hideApplicationNavigator();
        this.hideAutoReview();
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
    }
    
    private void changeToPhotoReadyView(final boolean b) {
        this.changeLayoutTo(this.selectLayoutPatternForPreview());
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (this.predictiveLaunchCoverExists()) {
            return;
        }
        final CapturingMode capturingMode = this.getCapturingMode();
        if (this.mStateMachine.getCurrentCaptureState() == CaptureState.STATE_WARNING) {
            this.mHintText.clearAll();
        }
        if (!this.isOverlayControlEnabled()) {
            this.mHintText.showAll();
        }
        else {
            this.mHintText.show(HintTextContent.HintPriority.HIGH);
        }
        if (this.mInstantViewer != null && this.mInstantViewer.isOpened()) {
            this.mInstantViewer.hide();
        }
        if (b) {
            this.mFocusRectangles.clearAllFocusExceptFace();
        }
        else {
            this.mFocusRectangles.onUiComponentRemoved();
        }
        final boolean b2 = true;
        this.applySmileFocusThreshold(true);
        this.mFocusRectangles.clearFaceDetection();
        this.mFocusRectangles.reset();
        this.showPhotoSmileCaptureIndicator();
        this.hideVideoSmileCaptureIndicator();
        this.setFrontAngleSwitchButtonVisibility(this.isFront());
        this.setLeftIconsVisibility(true);
        this.setOrientation(this.getOrientation());
        if (capturingMode != CapturingMode.NORMAL && capturingMode != CapturingMode.FRONT_PHOTO) {
            this.updateVisibilityForSpecificDisplaySize();
        }
        else {
            this.getBaseLayout().getSceneIndicator().set(false);
            this.getBaseLayout().getConditionIndicator().set(false);
            this.setApplicationNavigatorEnabled(false);
        }
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(true, true);
        this.changeScreenButtonImage(HeadUpDisplaySetupState.PHOTO_READY, false);
        final CapturingMode capturingMode2 = this.getCapturingMode();
        boolean b3 = b2;
        if (!ModeSelectorInternalMode.exists(capturingMode2)) {
            b3 = (capturingMode2.equals(CapturingMode.FRONT_PHOTO) && b2);
        }
        this.getBaseLayout().getModeButtonShortcut().update(b3);
        this.mSideTouchUi.detachTo(SideTouchUi.Type.ZOOM_BAR);
        this.mSideTouchUi.destroyTo(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL);
        if (this.mIsNeedDisplayToastChangeInternalStoarge) {
            this.mIsNeedDisplayToastChangeInternalStoarge = false;
            this.showToastMessage(ToastContent.ToastID.CHANGE_DESTINATION_TO_SAVE);
        }
    }
    
    private void changeToReadyForRecordView(final boolean b) {
        if (!b) {
            this.changeLayoutTo(BaseLayoutPattern.FOCUS_DONE);
        }
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (!b) {
            this.setFrontAngleSwitchButtonVisibility(false);
            this.setLeftIconsVisibility(false);
            this.hideApplicationNavigator();
            this.hideAutoReview();
            this.mFocusRectangles.setEnableFaceFocusTouch(false);
        }
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
    }
    
    private void changeToSelftimerView(final boolean b) {
        this.changeLayoutTo(BaseLayoutPattern.SELFTIMER);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (this.mSideTouchUi.containsIn(SideTouchUi.Type.CAPTURE_COUNTDOWN, SideTouchUi.Type.VIDEO_COUNTDOWN)) {
            this.mSideTouchUi.showIcon();
            return;
        }
        this.changeScreenButtonImage(HeadUpDisplaySetupState.PHOTO_READY, true);
        this.mSettingDialogStack.closeAllSettingDialogs();
        this.setLeftIconsVisibility(false);
        this.setFrontAngleSwitchButtonVisibility(false);
        if (b) {
            this.showSelfTimerCountDownView();
            this.startSelfTimerCountDownAnimation();
        }
        if (this.mSideTouchUi.containsIn(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL)) {
            this.mSideTouchUi.showIcon();
            this.mScreenButtonHandler.clearMain();
        }
        else {
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.CANCEL_SELFTIMER_LARGE, this.getOrientation(), true);
        }
        this.hidePhotoSmileCaptureIndicator();
        this.setOrientation(this.getOrientation());
        this.hideApplicationNavigator();
        this.hideAutoReview();
        if (!this.isTouchCaptureEnabled()) {
            this.mFocusRectangles.setLockedBySelfTimer(true);
        }
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
    }
    
    private void changeToStandardSlowMotionRecordingView() {
        this.changeToVideoRecordingView();
        this.changeScreenButtonImage(HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_RECORDING, false);
    }
    
    private void changeToSuperSlowMotionVideoHighFrameRateRecordingView() {
        this.changeToVideoRecordingView();
        this.changeLayoutTo(BaseLayoutPattern.HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION);
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.clearOption2();
        if (this.isTouchCaptureEnabled() && this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.SUPER_SLOW_SHOT) {
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE, this.mRecordingOrientation, false, false);
        }
        else {
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION_PRESSED, this.mRecordingOrientation, false, false);
        }
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        this.setFrontAngleSwitchButtonVisibility(false);
    }
    
    private void changeToSuperSlowMotionVideoLowFrameRateRecordingView() {
        this.changeToVideoRecordingView();
        this.mScreenButtonHandler.setMainRotatability(this.getOrientation(), false);
        this.changeScreenButtonImage(HeadUpDisplaySetupState.SUPER_SLOW_MOTION_RECORDING, false);
        this.showSuperSlowMotionVideoRecordingHintText();
        this.setFrontAngleSwitchButtonVisibility(false);
    }
    
    private void changeToVideoReadyView() {
        this.changeLayoutTo(this.selectLayoutPatternForPreview());
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        final CapturingMode capturingMode = this.getCapturingMode();
        if (this.mStateMachine.getCurrentCaptureState() == CaptureState.STATE_WARNING) {
            this.mHintText.clearAll();
        }
        if (!this.isOverlayControlEnabled()) {
            this.mHintText.showAll();
        }
        else {
            this.mHintText.show(HintTextContent.HintPriority.HIGH);
        }
        this.getBaseLayout().getSceneIndicator().set(false);
        this.getBaseLayout().getConditionIndicator().set(false);
        this.updateVideoHdrCondition(capturingMode, (VideoHdr)this.mStateMachine.getUserSetting().get(capturingMode, UserSettingKey.VIDEO_HDR), false);
        if (this.mInstantViewer != null && this.mInstantViewer.isOpened()) {
            this.mInstantViewer.hide();
        }
        if (capturingMode != CapturingMode.SLOW_MOTION) {
            this.changeScreenButtonImage(HeadUpDisplaySetupState.VIDEO_READY, false);
        }
        else {
            switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                case 3: {
                    this.changeScreenButtonImage(HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY, false);
                    break;
                }
                case 2: {
                    this.changeScreenButtonImage(HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY, false);
                    break;
                }
                case 1: {
                    this.changeScreenButtonImage(HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY, false);
                    break;
                }
            }
            this.mFocusRectangles.clearFaceDetection();
        }
        this.mFocusRectangles.clearExceptTouchFocus();
        this.mFocusRectangles.stopRecording();
        this.mFocusRectangles.setEnableFaceFocusTouch(true);
        this.applySmileFocusThreshold(true);
        this.hidePhotoSmileCaptureIndicator();
        this.hideVideoSmileCaptureIndicator();
        if (capturingMode == CapturingMode.SLOW_MOTION) {
            this.setApplicationNavigatorEnabled(false);
        }
        else {
            this.updateVisibilityForSpecificDisplaySize();
        }
        this.hideVideoSmileCaptureIndicator();
        this.setFrontAngleSwitchButtonVisibility(this.isFront());
        this.setLeftIconsVisibility(true);
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(true, true);
        final CapturingMode capturingMode2 = this.getCapturingMode();
        this.getBaseLayout().getModeButtonShortcut().update(ModeSelectorInternalMode.exists(capturingMode2) || capturingMode2.equals(CapturingMode.FRONT_PHOTO));
        if (!this.mSideTouchUi.detachTo(SideTouchUi.Type.ZOOM_BAR) && !this.mSideTouchUi.containsIn(SideTouchUi.Type.AUTO_REVIEW)) {
            this.mSideTouchUi.destroyIcon();
        }
        this.mRecordingTimeProxy.bindReceiver(this.getBaseLayout().getRecordingIndicator());
        this.mRecordingTimeProxy.reset();
    }
    
    private void changeToVideoRecordingPauseView() {
        if (this.mLayoutPattern == BaseLayoutPattern.PAUSE_RECORDING) {
            return;
        }
        this.changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.applySmileFocusThreshold(true);
        this.changeScreenButtonImage(HeadUpDisplaySetupState.VIDEO_PAUSING, false);
        if (this.getBaseLayout().getRecordingIndicator() != null) {
            this.getBaseLayout().getRecordingIndicator().setIndicator(false);
        }
        this.showVideoSmileCaptureIndicator();
        this.setFrontAngleSwitchButtonVisibility(false);
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        this.setOrientation(this.getOrientation());
        if (this.getBaseLayout().getContentsViewController().isLoadingInProvisionalContent()) {
            this.getBaseLayout().getContentsViewController().show();
        }
        if (this.mHintText != null) {
            this.mHintText.showAll();
        }
        if (this.mSideTouchUi.containsAll(SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE, SideTouchUi.Type.COVERING)) {
            this.changeLayoutTo(BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING);
            this.getBaseLayout().getZoomBar().hideDelayed();
            return;
        }
        if (!this.mSideTouchUi.detachTo(SideTouchUi.Type.ZOOM_BAR)) {
            if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR)) {
                this.mSideTouchUi.setUiOrientation(this.mRecordingOrientation);
                if (this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_HDR) == VideoHdr.HDR_ON) {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.RECORDING_HDR_PAUSE, null);
                }
                else {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.RECORDING_PAUSE, null);
                }
                this.mSideTouchUi.setUiOrientation(this.mOrientation);
                this.mSideTouchUi.showIcon();
                this.changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING, true);
            }
            else if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE)) {
                this.changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING, true);
            }
        }
        else if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE)) {
            this.changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING, true);
        }
    }
    
    private void changeToVideoRecordingView() {
        if (this.mLayoutPattern == BaseLayoutPattern.RECORDING) {
            return;
        }
        this.changeLayoutTo(BaseLayoutPattern.RECORDING);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.getBaseLayout().getGeoTagIndicator().hide();
        if (this.getCapturingMode() != CapturingMode.SLOW_MOTION) {
            this.changeScreenButtonImage(HeadUpDisplaySetupState.VIDEO_RECORDING, false);
        }
        else {
            this.cancelSlowMotionHintText();
        }
        this.getBaseLayout().getRecordingIndicator().setIndicator(true);
        this.mFocusRectangles.startRecording();
        this.applySmileFocusThreshold(true);
        this.setLeftIconsVisibility(false);
        this.setFrontAngleSwitchButtonVisibility(false);
        if (this.getBaseLayout().getContentsViewController().isLoadingInProvisionalContent()) {
            this.getBaseLayout().getContentsViewController().show();
        }
        else {
            this.getBaseLayout().getContentsViewController().hide();
        }
        this.showVideoSmileCaptureIndicator();
        this.hidePhotoSmileCaptureIndicator();
        this.mFocusRectangles.clearExceptTouchFocus();
        this.mFocusRectangles.onUiComponentRemoved();
        this.mFocusRectangles.setEnableFaceFocusTouch(true);
        this.setOrientation(this.getOrientation());
        if (this.mHintText != null) {
            this.mHintText.showAll();
        }
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        this.hideApplicationNavigator();
        this.hideAutoReview();
        if (this.mSideTouchUi.containsAll(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR, SideTouchUi.Type.COVERING)) {
            this.changeLayoutTo(BaseLayoutPattern.ZOOMING_IN_RECORDING);
            this.getBaseLayout().getZoomBar().hideDelayed();
            return;
        }
        if (!this.mSideTouchUi.detachTo(SideTouchUi.Type.ZOOM_BAR)) {
            if (this.mSideTouchUi.containsIn(SideTouchUi.Type.VIDEO_COUNTDOWN, SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE)) {
                this.mSideTouchUi.setUiOrientation(this.mRecordingOrientation);
                if (this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_HDR) == VideoHdr.HDR_ON) {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.RECORDING_HDR, null);
                }
                else {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.RECORDING, null);
                }
                this.mSideTouchUi.setUiOrientation(this.mOrientation);
                this.mSideTouchUi.showIcon();
                this.changeLayoutTo(BaseLayoutPattern.RECORDING, true);
            }
            else if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR)) {
                this.changeLayoutTo(BaseLayoutPattern.RECORDING, true);
            }
        }
        else if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR)) {
            this.changeLayoutTo(BaseLayoutPattern.RECORDING, true);
        }
    }
    
    private void changeToVideoZoomingWhileRecordingView() {
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[((BaseLayoutPattern)this.mLayoutPattern).ordinal()]) {
            case 3:
            case 4: {
                this.changeLayoutTo(BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING);
                break;
            }
            case 1:
            case 2: {
                this.changeLayoutTo(BaseLayoutPattern.ZOOMING_IN_RECORDING);
                break;
            }
        }
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.mFocusRectangles.clearExceptTouchFocus();
        this.applySmileFocusThreshold(false);
        this.hideVideoSmileCaptureIndicator();
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        this.setFrontAngleSwitchButtonVisibility(false);
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        this.mFocusRectangles.setEnableFaceFocusTouch(false);
        if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR, SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE, SideTouchUi.Type.ZOOM_BAR)) {
            this.mSideTouchUi.attachIcon(SideTouchUi.Type.COVERING, null);
        }
    }
    
    private void changeToWaitForHighFrameRateRecordingDoneView() {
        this.changeToVideoRecordingView();
        this.mScreenButtonHandler.setMainRotatability(this.getOrientation(), false);
        if (this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.SUPER_SLOW_SHOT) {
            this.changeLayoutTo(BaseLayoutPattern.HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION);
            this.mScreenButtonHandler.clearOption1();
            this.mScreenButtonHandler.clearOption2();
            if (this.isTouchCaptureEnabled()) {
                this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE, this.mRecordingOrientation, false, false);
            }
            else {
                this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION_DISABLED, this.mRecordingOrientation, false, false);
            }
        }
        else {
            this.mScreenButtonHandler.clearOption1();
            this.mScreenButtonHandler.setOption2(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_SMALL, this.mRecordingOrientation, false);
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION_DISABLED, this.mRecordingOrientation, false);
            this.postHintText(new HintTextSuperSlowMotionVideoRecording(true));
            this.mHintText.showAll();
        }
        this.setFrontAngleSwitchButtonVisibility(false);
    }
    
    private void changeToZoomingView() {
        this.changeLayoutTo(BaseLayoutPattern.ZOOMING);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.applySmileFocusThreshold(false);
        this.setFrontAngleSwitchButtonVisibility(false);
        this.setLeftIconsVisibility(false);
        this.hideAutoReview();
        this.hideApplicationNavigator();
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        this.mFocusRectangles.setEnableFaceFocusTouch(false);
        this.mSideTouchUi.destroyIcon();
    }
    
    private void checkupThermalCoolingRequest() {
        if (PlatformCapability.isPowerSavingSupported(this.mStateMachine.getCurrentCameraId())) {
            if (this.mActivity.isThermalWarningReceived()) {
                this.mStateMachine.sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, new Object[0]);
            }
            else if (this.mActivity.isThermalWarningExtraState()) {
                this.mStateMachine.sendEvent(TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW, new Object[0]);
            }
        }
    }
    
    private void clearPreInflatedViews() {
        if (this.mInflateItemMap != null) {
            this.mInflateItemMap.clear();
            this.mInflateItemMap = null;
        }
    }
    
    private void clearSurfaceView() {
        if (this.mEvf != null) {
            this.mEvf.clear();
        }
    }
    
    private void clearTouchCapture() {
        this.mTouchCapture = null;
    }
    
    private void clickAutoReview(final StoreDataResult storeDataResult) {
        final SavingRequest savingRequest = storeDataResult.savingRequest;
        final Content currentContent = this.getCurrentContent();
        this.clickThumbnail(storeDataResult.uri, savingRequest.common.mimeType, savingRequest.common.width, savingRequest.common.height, savingRequest.common.orientation, currentContent != null && currentContent.isMediaDataVerified());
    }
    
    private void clickThumbnail(final Uri uri, final String s, final int n, final int n2, final int n3, final boolean b) {
        final int currentRequestId = this.getCurrentRequestId();
        if (CommonUtility.getDefaultGallery(this.mActivity.getApplicationContext(), uri, s) != CommonUtility.DefaultGallerySetting.SONY_ALBUM) {
            this.launchAlbum(uri, s, b);
        }
        else if (this.mActivity.isDeviceInSecurityLock()) {
            this.launchAlbum(uri, s, b);
        }
        else if (this.prepareInstantViewer(uri)) {
            this.showInstantViewer(uri, s, n, n2, n3, currentRequestId);
            this.launchAlbum(uri, s, b);
        }
        else {
            this.launchAlbum(uri, s, b);
        }
    }
    
    private void closeInstantViewer() {
        if (this.mInstantViewer != null && this.mInstantViewer.isOpened()) {
            this.mInstantViewer.hide();
        }
        this.hideAutoReview();
    }
    
    private void closeSettingDialog() {
        if (this.mSettingDialogStack.isDialogOpened()) {
            this.mSettingDialogStack.closeCurrentDialog();
        }
    }
    
    private Size computeGridSize(final CapturingMode capturingMode, final UserSettings userSettings) {
        final Rect viewFinderRectSetting = this.getViewFinderRectSetting(userSettings, capturingMode);
        float n = viewFinderRectSetting.width() / (float)viewFinderRectSetting.height();
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            n = 1.0f / n;
        }
        final Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect((Context)this.mActivity, n, this.mScreenAspect);
        return new Size(surfaceViewRect.width(), surfaceViewRect.height());
    }
    
    private PointF convertTouchPointToDevicePreviewPositionRatio(final Point point) {
        final Rect rect = this.mEvf.getRect();
        return new PointF((point.x - rect.left) / (float)rect.width(), (point.y - rect.top) / (float)rect.height());
    }
    
    private void createSelfTimerCountDownView(final SelfTimer selfTimer) {
        if (this.mSelfTimerCountDownViewNext == null) {
            this.mSelfTimerCountDownViewNext = (SelfTimerCountDownView)this.getActivity().getLayoutInflater().inflate(2131492995, (ViewGroup)null);
        }
        this.mSelfTimerCountDownViewNext.setSelfTimer(selfTimer);
    }
    
    private OnScreenButtonItemFactory.ButtonType createStartRecordingButton() {
        if (this.isTouchCaptureEnabled()) {
            return OnScreenButtonItemFactory.ButtonType.TOUCH_RECORDING_START;
        }
        if (this.getCapturingMode() != CapturingMode.SLOW_MOTION) {
            return OnScreenButtonItemFactory.ButtonType.START_RECORDING_LARGE;
        }
        if (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.mStateMachine.getUserSetting().get(this.getCapturingMode(), UserSettingKey.SLOW_MOTION)).ordinal()] != 2) {
            return OnScreenButtonItemFactory.ButtonType.START_RECORDING_LARGE;
        }
        return OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION;
    }
    
    private void createViewFinder(final CameraActivity mActivity, final LayoutPatternApplier mLayoutPatternApplier, final boolean b) {
        this.mActivity = mActivity;
        this.mLayoutPatternApplier = mLayoutPatternApplier;
        if (!b) {
            this.initialize();
        }
    }
    
    private void disableModeIconClickable() {
        this.mApplicationNavigator.setModeIconClickable(false);
    }
    
    private void disableOverlayControl(final BaseLayout.LazyInitializer<OverlayControl> lazyInitializer) {
        if (lazyInitializer.isInitialized() && lazyInitializer.get().isEnabled()) {
            lazyInitializer.get().disable();
            if (this.mHintText != null && !this.isSettingDialogOpened()) {
                this.mHintText.showAll();
            }
            this.updateVisibilityForSpecificDisplaySize();
        }
    }
    
    private void disablePrimaryShortcut() {
        if (this.mPrimaryShortcutGroup != null) {
            this.mPrimaryShortcutGroup.disable();
        }
    }
    
    private void disableSemiAutoControl() {
        if (this.mStateMachine != null && this.getBaseLayout() != null) {
            final BaseLayout.LazyInitializer<OverlayControl> semiAutoControl = this.getBaseLayout().getSemiAutoControl();
            if (semiAutoControl.isInitialized() && semiAutoControl.get().isEnabled()) {
                if (this.isTouchFocus()) {
                    this.mFocusRectangles.clearTouchFocus();
                    this.mStateMachine.sendEvent(TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
                }
                this.disableOverlayControl(this.getBaseLayout().getSemiAutoControl());
                this.mStateMachine.sendEvent(TransitterEvent.EVENT_ON_SEMIAUTO_DISABLED, new Object[0]);
            }
        }
    }
    
    private void dismissKeyguard() {
        ((KeyguardManager)this.mActivity.getSystemService((Class)KeyguardManager.class)).requestDismissKeyguard((Activity)this.mActivity, (KeyguardManager$KeyguardDismissCallback)null);
    }
    
    private void doChangeCondition(final CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
        final int iconId = RecognizedCondition.create(sceneRecognitionResult.deviceStabilityCondition).getIconId();
        if (iconId != -1) {
            this.getBaseLayout().getConditionIndicator().set(true);
            this.getBaseLayout().getConditionIndicator().setImageResource(iconId);
        }
        else {
            this.getBaseLayout().getConditionIndicator().set(false);
        }
    }
    
    private void doChangeSceneMode(final CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
        final RecognizedScene create = RecognizedScene.create(sceneRecognitionResult.sceneMode);
        int iconId = create.getIconId();
        int textId = create.getTextId();
        if (iconId <= 0 || textId <= 0) {
            if (!sceneRecognitionResult.isMacroRange) {
                this.getBaseLayout().getSceneIndicator().set(false);
                return;
            }
            iconId = 2131231273;
            textId = 2131689845;
        }
        this.getBaseLayout().getSceneIndicator().set(true);
        this.getBaseLayout().getSceneIndicator().setImageResource(iconId);
        this.getBaseLayout().getSceneIndicator().setTextResource(textId);
    }
    
    private void enableOverlayControl(final BaseLayout.LazyInitializer<OverlayControl> lazyInitializer) {
        if (lazyInitializer.isInitialized() && lazyInitializer.get().isEnabled()) {
            return;
        }
        lazyInitializer.get().enable();
        lazyInitializer.get().setOrientation(this.mOrientation);
        this.updateAllOverlayControlVisibility();
        this.updateVisibilityForSpecificDisplaySize();
        this.getBaseLayout().getSceneIndicator().set(false);
        this.getBaseLayout().getConditionIndicator().set(false);
        if (this.mHintText != null) {
            this.mHintText.show(HintTextContent.HintPriority.HIGH);
        }
    }
    
    private void enableSemiAutoControl(final boolean b) {
        if (this.getBaseLayout().getSemiAutoControl().isInitialized() && this.getBaseLayout().getSemiAutoControl().get().isEnabled()) {
            return;
        }
        this.getBaseLayout().setupSemiAutoControl(new OverlayControlStateListener(UiComponentKind.OVERLAY_CONTROL_SEEKING), this.mColorValueAccessor, this.mBrightnessValueAccessor, b);
        this.enableOverlayControl(this.getBaseLayout().getSemiAutoControl());
        this.mStateMachine.sendEvent(TransitterEvent.EVENT_ON_SEMIAUTO_ENABLED, new Object[0]);
    }
    
    private void exitByError() {
        if (PlatformCapability.hasDeviceError()) {
            if (this.mActivity != null) {
                this.mActivity.finishAndKillProcess();
            }
            else {
                Process.killProcess(Process.myPid());
            }
        }
        else if (this.mActivity != null && !this.mIsPaused) {
            this.mActivity.finish();
        }
    }
    
    private CameraActivity getActivity() {
        return this.mActivity;
    }
    
    private BaseLayout getBaseLayout() {
        return this.mBaseLayout;
    }
    
    private OnScreenButtonItemFactory.ButtonType getCaptureButtonTypeAccordingToSelfTimerSetting() {
        final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[this.mPhotoSelfTimerSetting.ordinal()];
        if (n != 1) {
            if (n != 3) {
                if (this.isTouchCaptureEnabled()) {
                    return OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE;
                }
                return OnScreenButtonItemFactory.ButtonType.CAPTURE_LARGE;
            }
            else {
                if (this.isTouchCaptureEnabled()) {
                    return OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER_SHORT;
                }
                return OnScreenButtonItemFactory.ButtonType.CAPTURE_WITH_SELFTIMER_SHORT;
            }
        }
        else {
            if (this.isTouchCaptureEnabled()) {
                return OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER_LONG;
            }
            return OnScreenButtonItemFactory.ButtonType.CAPTURE_WITH_SELFTIMER_LONG;
        }
    }
    
    private CapturingMode getCapturingMode() {
        return this.mStateMachine.getCurrentCapturingMode();
    }
    
    public static CapturingMode getCapturingMode(final NavigatorContents navigatorContents, final CapturingMode capturingMode) {
        CapturingMode capturingMode2 = null;
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$NavigatorContents[navigatorContents.ordinal()]) {
            default: {
                capturingMode2 = CapturingMode.SCENE_RECOGNITION;
                break;
            }
            case 2: {
                if (capturingMode.isFront()) {
                    capturingMode2 = CapturingMode.FRONT_VIDEO;
                    break;
                }
                capturingMode2 = CapturingMode.VIDEO;
                break;
            }
            case 1: {
                if (capturingMode.isFront()) {
                    capturingMode2 = CapturingMode.SUPERIOR_FRONT;
                    break;
                }
                capturingMode2 = CapturingMode.SCENE_RECOGNITION;
                break;
            }
        }
        return capturingMode2;
    }
    
    private Content getCurrentContent() {
        final ContentsViewController contentsViewController = this.getBaseLayout().getContentsViewController();
        if (contentsViewController == null) {
            CamLog.w("getCurrentContent() contentsViewController is null.");
            return null;
        }
        return contentsViewController.getCurrentContent();
    }
    
    private LayoutPattern getCurrentLayoutPattern() {
        return this.mLayoutPattern;
    }
    
    private int getCurrentRequestId() {
        final ContentsViewController contentsViewController = this.getBaseLayout().getContentsViewController();
        if (contentsViewController == null) {
            CamLog.w("getCurrentRequestId() contentsViewController is null.");
            return -1;
        }
        return contentsViewController.getCurrentRequestId();
    }
    
    private Storage.StorageType getCurrentStorage() {
        if (this.mActivity.isOneShot()) {
            return this.mActivity.getLaunchCondition().getStorageTypeForOneshot();
        }
        final UserSettings userSettings = this.getActivity().getStoredSettings().getUserSettings();
        DestinationToSave destinationToSave;
        if ((destinationToSave = (DestinationToSave)userSettings.get(UserSettingKey.DESTINATION_TO_SAVE)) == null) {
            destinationToSave = (DestinationToSave)userSettings.get(this.mActivity.getLaunchCondition().getCapturingMode(), UserSettingKey.DESTINATION_TO_SAVE);
        }
        return destinationToSave.getType();
    }
    
    private void getDownHeadUpDisplay() {
        if (CamLog.VERBOSE) {
            CamLog.d("getDownHeadUpDisplay() is called.");
        }
        if (this.mEvf != null) {
            this.mEvf.setLifeCycleCallback(null);
            this.mEvf.onDestroy();
            this.mEvf = null;
        }
        this.hideSurfaceBlinderView();
        this.releaseSurfaceBlinderView();
        if (this.mFocusRectangles != null) {
            this.mFocusRectangles.release();
            this.mFocusRectangles = null;
        }
        if (this.mViewFinderCaptureArea != null) {
            this.mViewFinderCaptureArea.setCaptureAreaStateListener(null);
            this.mViewFinderCaptureArea.release();
            this.mViewFinderCaptureArea = null;
        }
        if (this.mCaptureFeedback != null) {
            this.mCaptureFeedback.release();
            this.mCaptureFeedback = null;
        }
        this.mInstantViewer = null;
    }
    
    private LayoutPatternApplier getLayoutPatternApplier() {
        return this.mLayoutPatternApplier;
    }
    
    private NavigatorContents getNextContent(final NavigatorContents navigatorContents, final AbstractDraggingEventHandler.Direction direction) {
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$controller$AbstractDraggingEventHandler$Direction[direction.ordinal()]) {
            default: {
                return navigatorContents;
            }
            case 2: {
                return navigatorContents.previous();
            }
            case 1: {
                return navigatorContents.next();
            }
        }
    }
    
    private List<View> getPreInflatedView(final InflateItem inflateItem) {
        if (this.mInflateItemMap != null) {
            return this.mInflateItemMap.get(inflateItem);
        }
        return null;
    }
    
    private Point getSideTouchPoint(final SideTouchEventDetector.SideTouchEvent sideTouchEvent) {
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$SideTouchEventDetector$SideTouchArea[sideTouchEvent.area.ordinal()]) {
            default: {
                return null;
            }
            case 4: {
                return new Point(1439, sideTouchEvent.position);
            }
            case 3: {
                return new Point(0, sideTouchEvent.position);
            }
            case 1:
            case 2: {
                return null;
            }
        }
    }
    
    private String getString(final int n) {
        return ResourceUtil.getString((Context)this.getActivity(), n);
    }
    
    private ContentPallet.ThumbnailStateListener getThumbnailStateListener() {
        return new ContentPallet.ThumbnailStateListener(this) {
            final ViewFinderImpl this$0;
            
            @Override
            public void onThumbnailClicked(final Content content) {
                if (this.this$0.mIsFrontAngleChanging) {
                    return;
                }
                if (content != null) {
                    final Content.ContentInfo contentInfo = content.getContentInfo();
                    this.this$0.clickThumbnail(contentInfo.mOriginalUri, contentInfo.mMimeType, contentInfo.mWidth, contentInfo.mHeight, contentInfo.mOrientation, content.isMediaDataVerified());
                }
            }
            
            @Override
            public void onThumbnailCreated(final Content content) {
                if (this.this$0.mCameraDevice.getRemainSavingPhotoRequestCount() == 0 || content.getContentInfo().mContentType == Content.ContentsType.PREDICTIVE_CAPTURE) {
                    if (this.this$0.mInstantViewer != null) {
                        this.this$0.mInstantViewer.prepareBitmap(content.getContentInfo().mOriginalUri);
                    }
                    if (content.getContentInfo().mContentType == Content.ContentsType.PREDICTIVE_CAPTURE && this.this$0.mStateMachine.getPredictiveCaptureStoreInfo() != null) {
                        content.getContentInfo().mPredictiveNum = this.this$0.mStateMachine.getPredictiveCaptureStoreInfo().getCaptureNum();
                    }
                }
            }
        };
    }
    
    private Rect getViewFinderRectSetting(final UserSettings userSettings, final CapturingMode capturingMode) {
        Rect rect;
        if (capturingMode.isVideo()) {
            rect = ((VideoSize)userSettings.get(UserSettingKey.VIDEO_SIZE)).getVideoRect();
        }
        else {
            rect = ((Resolution)userSettings.get(UserSettingKey.RESOLUTION)).getPictureRect();
        }
        return rect;
    }
    
    private boolean hasEnoughFreeSpace(final Storage.StorageType storageType) {
        final DestinationToSave destinationToSave = (DestinationToSave)this.mStateMachine.getUserSetting().get(UserSettingKey.DESTINATION_TO_SAVE);
        final Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(storageType);
        final Storage.StorageType type = destinationToSave.getType();
        final boolean b = true;
        final boolean b2 = true;
        if (type == storageType) {
            boolean b3 = b2;
            if (currentState != Storage.StorageState.AVAILABLE) {
                b3 = (currentState == Storage.StorageState.UNGRANTED && b2);
            }
            return b3;
        }
        boolean b4 = b;
        if (currentState != Storage.StorageState.AVAILABLE) {
            b4 = b;
            if (currentState != Storage.StorageState.REMOVED) {
                b4 = (currentState == Storage.StorageState.UNGRANTED && b);
            }
        }
        return b4;
    }
    
    private void hideAndCancelAllView() {
        if (this.isInSelfTimerCountDown()) {
            this.cancelSelfTimerCountDownView();
            this.changeToPhotoReadyView(false);
        }
        this.hideAutoReview();
        this.hideSurfaceBlinderView();
        this.mEvf.hide();
        this.pauseView();
        this.changeToPauseView();
        this.getBaseLayout().releasePredictiveLaunchCover();
        this.mToastContent.closeMessage();
        if (this.mHintText != null) {
            this.mHintText.hide();
            this.mHintText.clearAll();
        }
        this.mHintBurstImageSavedToInternalStorageAlreadyDisplayed = false;
        this.mHintCannotBurstUsingFrontCameraAlreadyDisplayed = false;
        this.mHintBurstChangeCameraKeySettingAlreadyDisplayed = false;
        this.mHintCannotBurstUsingFusionModeAlreadyDisplayed = false;
    }
    
    private void hideApplicationNavigator() {
        if (this.mApplicationNavigator != null) {
            this.mApplicationNavigator.hide();
        }
    }
    
    private void hideMruButtonContainer() {
        if (this.getBaseLayout().getMruButtonContainer() != null) {
            this.getBaseLayout().getMruButtonContainer().hide();
        }
    }
    
    private void hidePhotoSmileCaptureIndicator() {
        this.getBaseLayout().getPhotoSmileCaptureIndicator().hide();
    }
    
    private void hideSurfaceBlinderView() {
        if (this.mSurfaceBlinderView != null && this.mSurfaceBlinderView.getVisibility() == 0) {
            this.mSurfaceBlinderView.setVisibility(4);
            final FrameLayout previewOverlayContainer = this.getBaseLayout().getPreviewOverlayContainer();
            if (previewOverlayContainer != null) {
                previewOverlayContainer.removeView(this.mSurfaceBlinderView);
            }
        }
    }
    
    private void hideVideoSmileCaptureIndicator() {
        this.getBaseLayout().getVideoSmileCaptureIndicator().hide();
    }
    
    private void hideZoomBar() {
        this.setZoombarVisibility(false);
    }
    
    private boolean isAcquired() {
        return this.mActivity.getGeoTagManager().isNetworkAcquired() | this.mActivity.getGeoTagManager().isGpsAcquired();
    }
    
    private boolean isAllDialogClosed() {
        final SettingDialogStack mSettingDialogStack = this.mSettingDialogStack;
        boolean b = true;
        if (mSettingDialogStack != null) {
            if (this.mSettingDialogStack.isDialogOpened() || this.mMessageDialog.isCurrentDialogInList(ViewFinderImpl.STORAGE_DIALOG_LIST)) {
                b = false;
            }
            return b;
        }
        return true;
    }
    
    private boolean isAutoReviewEnabled() {
        if (this.mActivity.isOneShot()) {
            return false;
        }
        if (this.getCapturingMode().isVideo()) {
            return false;
        }
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$AutoReview[((AutoReview)this.mActivity.getStoredSettings().getUserSettings().get(UserSettingKey.AUTO_REVIEW)).ordinal()]) {
            default: {
                return false;
            }
            case 2: {
                return this.getCapturingMode().isFront();
            }
            case 1: {
                return true;
            }
        }
    }
    
    private boolean isCurrentStorageExternal() {
        return this.getCurrentStorage() == Storage.StorageType.EXTERNAL_CARD;
    }
    
    private boolean isDisplayFlashRequired() {
        return this.mRequireDisplayFlash;
    }
    
    private boolean isEvfRotateRequired() {
        if (this.mEvf == null) {
            return false;
        }
        final Rect rect = this.mEvf.getRect();
        final LayoutOrientationResolver.LayoutOrientationType orientation = LayoutOrientationResolver.getInstance().getOrientation();
        final LayoutOrientationResolver.LayoutOrientationType portrait = LayoutOrientationResolver.LayoutOrientationType.PORTRAIT;
        boolean b = true;
        if (orientation == portrait) {
            if (rect.width() > rect.height()) {
                return b;
            }
        }
        else if (rect.width() < rect.height()) {
            return b;
        }
        b = false;
        return b;
    }
    
    private boolean isExclusiveViewEvent(final View view, final MotionEvent motionEvent) {
        if (this.mSettingDialogStack == null) {
            return false;
        }
        if (this.mPrimaryShortcutGroup != null) {
            for (final Map.Entry<Object, V> entry : this.mPrimaryShortcutGroup.getPrimaryShortcutViewMap().entrySet()) {
                boolean b;
                if (entry.getKey() == UserSettingKey.SETTING_MENU) {
                    if (this.mOrientation == 1 && this.mSettingDialogStack.isSecondLayerDialogOpened()) {
                        return false;
                    }
                    b = this.mSettingDialogStack.isMenuDialogOpened();
                }
                else {
                    b = this.mSettingDialogStack.isOpened(entry.getKey());
                }
                if (b) {
                    final View view2 = (View)entry.getValue();
                    final Rect rect = new Rect();
                    view2.getGlobalVisibleRect(rect);
                    final Rect rect2 = new Rect();
                    view.getGlobalVisibleRect(rect2);
                    if (rect.contains(rect2.bottom - (int)motionEvent.getY(), rect2.left + (int)motionEvent.getX())) {
                        return true;
                    }
                    continue;
                }
            }
        }
        return false;
    }
    
    private boolean isFocusing() {
        if (this.mLayoutPattern == null) {
            return false;
        }
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[((BaseLayoutPattern)this.mLayoutPattern).ordinal()]) {
            default: {
                return false;
            }
            case 6:
            case 7: {
                return true;
            }
        }
    }
    
    private boolean isFront() {
        return this.getCapturingMode().isFront();
    }
    
    private boolean isInLargerOrMoreDisplaySizeOr16_9Device() {
        return this.mBaseLayout.isInLargerOrMoreDisplaySize() || this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.SIXTEEN_NINE;
    }
    
    private boolean isInSelfTimerCountDown() {
        final CapturingMode capturingMode = this.getCapturingMode();
        final CapturingMode front_VIDEO = CapturingMode.FRONT_VIDEO;
        boolean b = false;
        if (capturingMode != front_VIDEO && this.getCapturingMode() != CapturingMode.VIDEO) {
            if (this.getCurrentLayoutPattern() == BaseLayoutPattern.SELFTIMER) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    private boolean isInflated() {
        return this.mInflateItemMap != null;
    }
    
    private boolean isInternalStorageWritable() {
        final Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(Storage.StorageType.INTERNAL);
        return currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.AVAILABLE_NEAR_FULL;
    }
    
    private static boolean isNearSameSize(final Rect rect, final Rect rect2) {
        return ViewUtility.isSimilarAspect(rect.width() / (float)rect.height(), rect2.width() / (float)rect2.height());
    }
    
    private boolean isNearSameSizeNavigationbar(final Rect rect, final Rect rect2) {
        return isNearSameSize(rect, rect2) || isNearSameSize(rect, new Rect(rect2.left, rect2.top, rect2.right + this.mActivity.getResources().getDimensionPixelSize(2131165455), rect2.bottom));
    }
    
    private boolean isNecessaryToReverseForAutoReview(final StoreDataResult storeDataResult) {
        final boolean b = storeDataResult.savingRequest instanceof PhotoSavingRequest;
        boolean b2 = false;
        if (b && ((PhotoSavingRequest)storeDataResult.savingRequest).photo.isFront()) {
            if (storeDataResult.savingRequest.common.orientation == 90 || storeDataResult.savingRequest.common.orientation == 270) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    private boolean isNeedToShowHiSpeedSdCardRecommendation() {
        final DestinationToSave destinationToSave = (DestinationToSave)this.mActivity.getStoredSettings().getUserSettings().get(UserSettingKey.DESTINATION_TO_SAVE);
        final Storage.StorageType type = destinationToSave.getType();
        final Storage.StorageType external_CARD = Storage.StorageType.EXTERNAL_CARD;
        boolean b = false;
        if (type != external_CARD) {
            return false;
        }
        final Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(destinationToSave.getType());
        if (currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.AVAILABLE_NEAR_FULL) {
            b = true;
        }
        return b;
    }
    
    private boolean isObjectTrackingEnabled() {
        return this.mStateMachine.getUserSetting().get(UserSettingKey.OBJECT_TRACKING) == ObjectTracking.ON;
    }
    
    private boolean isOverlayControlEnabled() {
        return (this.getBaseLayout().getSemiAutoControl().isInitialized() && this.getBaseLayout().getSemiAutoControl().get().isEnabled()) || (this.getBaseLayout().getImageQualityControl().isInitialized() && this.getBaseLayout().getImageQualityControl().get().isEnabled());
    }
    
    private boolean isOverlayControlVisible() {
        return (this.getBaseLayout().getSemiAutoControl().isInitialized() && this.getBaseLayout().getSemiAutoControl().get().isVisible()) || (this.getBaseLayout().getImageQualityControl().isInitialized() && this.getBaseLayout().getImageQualityControl().get().isVisible());
    }
    
    private boolean isPaused() {
        return this.mIsPaused;
    }
    
    private boolean isPhotoSelfTimerEnabled() {
        return this.mStateMachine.getUserSetting().get(UserSettingKey.SELF_TIMER) != SelfTimer.OFF;
    }
    
    private boolean isPredictiveCaptureAvailable() {
        return this.getCapturingMode().isSuperiorAuto() && !this.getCapturingMode().isFront() && !this.getActivity().isOneShot() && PlatformCapability.isBypassCameraSupported() && PlatformCapability.isPredictiveCaptureShotSupported(this.getCapturingMode().getCameraId());
    }
    
    private boolean isPreviewLayout(final LayoutPattern layoutPattern) {
        return layoutPattern == BaseLayoutPattern.PREVIEW || layoutPattern == BaseLayoutPattern.PREVIEW_NO_RECORDING;
    }
    
    private boolean isRecording() {
        if (this.mLayoutPattern == null) {
            return false;
        }
        final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[((BaseLayoutPattern)this.mLayoutPattern).ordinal()];
        return n == 1 || n == 3;
    }
    
    private boolean isSdCardRemoved() {
        return this.mActivity.getStorage().getCurrentState(Storage.StorageType.EXTERNAL_CARD) == Storage.StorageState.REMOVED;
    }
    
    private boolean isSdCardWritable() {
        final Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(Storage.StorageType.EXTERNAL_CARD);
        return currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.AVAILABLE_NEAR_FULL;
    }
    
    private static boolean isSemiAutoControlAvailable(final CapturingMode capturingMode) {
        final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()];
        if (n != 7) {
            switch (n) {
                default: {
                    return false;
                }
                case 1:
                case 2:
                case 3:
                case 4: {
                    break;
                }
            }
        }
        return true;
    }
    
    private boolean isSettingDialogOpened() {
        return this.mSettingDialogStack != null && this.mSettingDialogStack.isDialogOpened();
    }
    
    private boolean isShownInInstantViewer(final StoreDataResult storeDataResult) {
        return this.mInstantViewer != null && this.mInstantViewer.isOpened() && storeDataResult.savingRequest.getRequestId() == this.mInstantViewer.getRequestId();
    }
    
    private boolean isSmileShutterEnabled() {
        return ((SmileCapture)this.mStateMachine.getUserSetting().get(UserSettingKey.SMILE_CAPTURE)).isSmileCaptureOn();
    }
    
    private boolean isStorageReady() {
        final Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(this.getCurrentStorage());
        return currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.AVAILABLE_NEAR_FULL;
    }
    
    private boolean isTouchCaptureEnabled() {
        TouchCapture mTouchCapture;
        if (this.mTouchCapture != null) {
            mTouchCapture = this.mTouchCapture;
        }
        else {
            mTouchCapture = (TouchCapture)this.mStateMachine.getUserSetting().get(this.getCapturingMode(), UserSettingKey.TOUCH_CAPTURE);
        }
        if (mTouchCapture != null) {
            switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$TouchCapture[mTouchCapture.ordinal()]) {
                case 2: {
                    if (this.isFront()) {
                        return true;
                    }
                    break;
                }
                case 1: {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    private boolean isZooming() {
        if (this.mLayoutPattern == null) {
            return false;
        }
        final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[((BaseLayoutPattern)this.mLayoutPattern).ordinal()];
        if (n != 2) {
            switch (n) {
                default: {
                    return false;
                }
                case 4:
                case 5: {
                    break;
                }
            }
        }
        return true;
    }
    
    private void joinInflateTask() {
        if (CamLog.VERBOSE) {
            CamLog.d("joinInflateTask in");
        }
        if (this.mInflateFuture != null) {
            try {
                this.mInflateItemMap = this.mInflateFuture.get();
            }
            catch (final ExecutionException ex) {
                CamLog.e("join", ex);
            }
            catch (final InterruptedException ex2) {
                CamLog.e("join", ex2);
            }
            this.mInflateFuture = null;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("joinInflateTask out");
        }
    }
    
    private void launchAlbum(final Uri uri, final String s, final boolean b) {
        if (this.mActivity.isDeviceInSecurityLock()) {
            final List<Content.ContentInfo> localContentInfo = this.getBaseLayout().getContentsViewController().getLocalContentInfo();
            final boolean verbose = CamLog.VERBOSE;
            int i = 0;
            if (verbose) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onClick : contentInfoList = ");
                sb.append(localContentInfo.size());
                CamLog.d(sb.toString());
            }
            if (!localContentInfo.isEmpty()) {
                final ArrayList list = new ArrayList();
                final ArrayList<String> list2 = new ArrayList<String>();
                final ArrayList list3 = new ArrayList();
                for (final Content.ContentInfo contentInfo : localContentInfo) {
                    list.add(contentInfo.mOriginalUri);
                    list2.add(contentInfo.mMimeType);
                    if (contentInfo.mContentType == Content.ContentsType.BURST && contentInfo.mGroupedImage > 0) {
                        final Iterator<Long> iterator2 = contentInfo.mMediaStoreIds.iterator();
                        while (iterator2.hasNext()) {
                            list3.add(iterator2.next());
                        }
                    }
                    else {
                        list3.add(contentInfo.mId);
                    }
                }
                final long[] array = new long[list3.size()];
                while (i < list3.size()) {
                    array[i] = (long)list3.get(i);
                    ++i;
                }
                InstantViewer.launchAlbumSecure(this.mActivity, list, list2, this.mStateMachine.getPredictiveCaptureStoreInfo(), array);
            }
        }
        else {
            InstantViewer.launchAlbum(this.mActivity, uri, s, b, this.mStateMachine.getPredictiveCaptureStoreInfo());
        }
    }
    
    private void launchLocationSourceSettings() {
        ApplicationLauncher.launchLocationSourceSettings(this.mActivity);
    }
    
    private void launchSideSenseSettings() {
        ApplicationLauncher.launchSideSenseSettings(this.mActivity);
    }
    
    private static void logPerformance(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[PERFORMANCE] [TIME = ");
        sb.append(System.currentTimeMillis());
        sb.append("] [");
        sb.append("ViewFinderImpl");
        sb.append("] [");
        sb.append(Thread.currentThread().getName());
        sb.append(" : ");
        sb.append(str);
        sb.append("]");
        Log.e("TraceLog", sb.toString());
    }
    
    private boolean needToShowGeoTagIndicator() {
        return !this.mActivity.isOneShot() || PermissionsUtil.areCallerGeoPermissionsGranted(this.mActivity);
    }
    
    private void notifyOnEvfPrepared(final Rect rect) {
        this.setEvfPrepared(true);
        this.mStateMachine.sendEvent(TransitterEvent.EVENT_ON_EVF_PREPARED, this.mEvf);
        this.mCanFocusRectanglesBeUpdated = false;
        final Handler handler = this.getBaseLayout().getRootView().getHandler();
        if (handler != null) {
            handler.post((Runnable)new Runnable(this, rect) {
                final ViewFinderImpl this$0;
                final Rect val$previewRect;
                
                @Override
                public void run() {
                    if (this.this$0.mFocusRectangles != null) {
                        final Size sizeAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getSizeAccordingToLayoutOrientation(new Size(this.val$previewRect.width(), this.val$previewRect.height()));
                        this.this$0.mFocusRectangles.updateDevicePreviewSize(sizeAccordingToLayoutOrientation.getWidth(), sizeAccordingToLayoutOrientation.getHeight());
                    }
                    this.this$0.mCanFocusRectanglesBeUpdated = true;
                    this.this$0.updateCaptureAreaSize();
                    this.this$0.setupAutoReview();
                }
            });
        }
    }
    
    private void onAppsUiModeFinish() {
        if (!this.getActivity().isInLockTaskMode()) {
            this.changeLayoutTo(BaseLayoutPattern.CLEAR);
            if (this.mSettingDialogStack != null) {
                this.mSettingDialogStack.closeAllSettingDialogs(false);
            }
        }
        this.mActivity.abort();
    }
    
    private void onBurstFinished() {
        if (this.isHeadUpDisplayReady() && this.mBurstCountView != null) {
            this.mBurstCountView.hide();
            this.startCaptureFeedbackAnimation();
            if (((DestinationToSave)this.mStateMachine.getUserSetting().get(UserSettingKey.DESTINATION_TO_SAVE)).getType() != Storage.StorageType.INTERNAL && !this.mHintBurstImageSavedToInternalStorageAlreadyDisplayed) {
                this.mHintBurstImageSavedToInternalStorageAlreadyDisplayed = true;
                this.postHintText(new HintTextTimedOutMessage(HintTextTimedOutMessage.MessageType.BURST_IMAGES_ARE_SAVED_TO_INTERNAL_STORAGE));
            }
        }
    }
    
    private void onBurstRejected(final BurstRejectedReason mBurstShootingRejectedReason) {
        if (this.isPreviewLayout(this.getCurrentLayoutPattern())) {
            this.showBurstRejectedMessage(mBurstShootingRejectedReason);
        }
        else {
            this.mBurstShootingRejectedReason = mBurstShootingRejectedReason;
        }
    }
    
    private void onBurstShutterDone(final boolean b, final int n) {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (this.mBurstCountView == null) {
            (this.mBurstCountView = this.getBaseLayout().getBurstCountView()).setUiOrientation(this.mOrientation);
        }
        this.mBurstCountView.update(n);
        this.clearTouchedScreenButtonGroup();
        this.mFocusRectangles.onAutoFocusDone(b);
        this.mFocusRectangles.clearTouched();
        this.mViewFinderCaptureArea.clearTouched();
    }
    
    private void onCaptureCanceled() {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (this.mBurstShootingRejectedReason != BurstRejectedReason.NONE) {
            this.showBurstRejectedMessage(this.mBurstShootingRejectedReason);
            this.mBurstShootingRejectedReason = BurstRejectedReason.NONE;
        }
        this.updateAllOverlayControlVisibility();
        this.updateVisibilityForSpecificDisplaySize();
    }
    
    private void onCaptureFinished() {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (!this.attachSideAutoReview() && this.isAutoReviewEnabled()) {
            this.mIsAutoReviewRequested = true;
            this.mAutoReviewProxy.bindReceiver(this.getBaseLayout().getAutoReview());
        }
        if (this.mBurstShootingRejectedReason != BurstRejectedReason.NONE) {
            this.showBurstRejectedMessage(this.mBurstShootingRejectedReason);
            this.mBurstShootingRejectedReason = BurstRejectedReason.NONE;
        }
        this.updateAllOverlayControlVisibility();
        this.updateVisibilityForSpecificDisplaySize();
    }
    
    private void onCapturingModeChanged(final CapturingMode mCapturingModeWhenLastSetupHeadDisplay, final boolean b, final AnimationRequest.AnimationType animationType) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onCapturingModeChanged()  request:");
            sb.append(mCapturingModeWhenLastSetupHeadDisplay.name());
            CamLog.d(sb.toString());
        }
        final Rect previewSize = this.mCameraDevice.getPreviewSize();
        if (previewSize == null) {
            return;
        }
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        final Size sizeAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getSizeAccordingToLayoutOrientation(new Size(previewSize.width(), previewSize.height()));
        final int width = sizeAccordingToLayoutOrientation.getWidth();
        final int height = sizeAccordingToLayoutOrientation.getHeight();
        final Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect((Context)this.mActivity, width / (float)height, this.mScreenAspect);
        PositionConverter.getInstance().setSurfaceSize(surfaceViewRect.width(), surfaceViewRect.height());
        PositionConverter.getInstance().setPreviewSize(width, height);
        this.mFocusRectangles.updateDevicePreviewSize(width, height);
        this.mFocusRectangles.clearExceptTouchFocus();
        this.mCapturingModeWhenLastSetupHeadDisplay = mCapturingModeWhenLastSetupHeadDisplay;
        this.applyShutterTriggerSettings();
        this.hideVideoSmileCaptureIndicator();
        this.setZoomRatio(0);
        this.setOrientation(this.mActivity.getOrientation());
        this.updateGridLineView(mCapturingModeWhenLastSetupHeadDisplay);
        if (b) {
            this.startModeChangedAnimation(this.getCapturingMode(), mCapturingModeWhenLastSetupHeadDisplay, animationType);
        }
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[mCapturingModeWhenLastSetupHeadDisplay.ordinal()]) {
            default: {
                this.resumeApplicationNavigator(NavigatorContents.valueOf(this.getCapturingMode()));
                this.updateVisibilityForSpecificDisplaySize();
                if (this.getBaseLayout().getMruButtonContainer().hasInternalMode()) {
                    this.setMruAvailability(true);
                }
                this.attemptSetupMruButton(mCapturingModeWhenLastSetupHeadDisplay);
                break;
            }
            case 7: {
                this.getBaseLayout().getSuperSlowMotionTriggerAnimation().prepareViews();
                this.setApplicationNavigatorEnabled(false);
                if (!this.mStateMachine.isTutorialNeededToBeShownForCurrentMode()) {
                    this.showHiSpeedSdCardRecommendDialogOnModeChange();
                }
                this.setMruAvailability(false);
                break;
            }
            case 5:
            case 6: {
                this.enableOverlayControl(this.getBaseLayout().getImageQualityControl());
                this.setApplicationNavigatorEnabled(false);
                this.setMruAvailability(false);
                break;
            }
        }
    }
    
    private void onCapturingModeChanging() {
        this.disableSemiAutoControl();
        this.disableOverlayControl(this.getBaseLayout().getImageQualityControl());
    }
    
    private void onCloseStorageDialog() {
        if (this.isAllDialogClosed()) {
            this.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_CLOSED, UiComponentKind.SETTING_DIALOG);
        }
    }
    
    private void onFaceDetected(final CameraParameters.FaceDetectionResult faceDetectionResult) {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.mFocusRectangles.onFaceDetected(faceDetectionResult);
    }
    
    private void onLazyInitializationTaskRun() {
        ModeLoader.updatePluginsDatabase((Context)this.mActivity);
        this.mInstantViewer.createAlbumPreloader();
        this.setupMruButton(this.getCapturingMode());
    }
    
    private void onModeControllableDraggingMove(final NavigatorContents navigatorContents, final NavigatorContents navigatorContents2, final int n, final float f) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onModeControllableDraggingMove()  from:");
            sb.append(navigatorContents.name());
            sb.append(" to:");
            sb.append(navigatorContents2.name());
            sb.append(" distance:");
            sb.append(n);
            sb.append(" progress:");
            sb.append(f);
            CamLog.d(sb.toString());
        }
        this.setPreviewAlpha(n);
        if (n > 0) {
            this.setApplicationNavigatorPosition(navigatorContents, f);
        }
        else if (n < 0) {
            this.setApplicationNavigatorPosition(navigatorContents, -f);
        }
    }
    
    private void onNotifyThermalStatus(final boolean b) {
        if (this.getBaseLayout().getThermalIndicator() != null) {
            this.getBaseLayout().getThermalIndicator().set(b);
        }
        if (!b) {
            if (this.mActivity.isThermalWarningReceived()) {
                this.updateThermalHintTextMessage(this.getCapturingMode());
            }
        }
    }
    
    private void onOpenStorageDialog() {
        if (this.mCurrentDisplayingUiComponent == null) {
            return;
        }
        this.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_OPENED, new Object[0]);
        if (this.mSettingUi != null) {
            this.mSettingUi.closeDialogs();
        }
    }
    
    private void onSceneModeChanged(final CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (this.mActivity.isOneShot()) {
            return;
        }
        this.doChangeSceneMode(sceneRecognitionResult);
        this.doChangeCondition(sceneRecognitionResult);
    }
    
    private void onStoreCompleted(final StoreDataResult mAutoReviewStoreData, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append("onStoreCompleted() result:");
        sb.append(mAutoReviewStoreData.savingRequest.getRequestId());
        sb.append(" isLast:");
        sb.append(b);
        CamLog.d(sb.toString());
        if (b) {
            this.mIsAutoReviewRequested = false;
        }
        if (!this.isHeadUpDisplayReady()) {
            this.mAutoReviewStoreData = mAutoReviewStoreData;
            return;
        }
        if (!mAutoReviewStoreData.isSuccess() && this.isCurrentStorageExternal() && !this.isSdCardWritable() && !this.isSdCardRemoved()) {
            this.showMessageDialog(DialogId.COULD_NOT_SAVE_PHOTO, new Object[0]);
        }
        if (this.isShownInInstantViewer(mAutoReviewStoreData)) {
            CamLog.d("Potho which is shown in Instant viewer is saved and start Album for the photo.");
            InstantViewer.launchAlbum(this.mActivity, mAutoReviewStoreData.uri, mAutoReviewStoreData.savingRequest.common.mimeType, true, this.mStateMachine.getPredictiveCaptureStoreInfo());
            return;
        }
        this.addThumbnail(mAutoReviewStoreData);
        if (b) {
            this.showAutoReview(mAutoReviewStoreData);
        }
    }
    
    private void onToggleCameraSwitch() {
        this.mStateMachine.sendEvent(TransitterEvent.EVENT_ON_SWITCH_CAMERA, AnimationRequest.AnimationType.NONE);
    }
    
    private void onTrackedObjectStateUpdated(final CameraParameters.ObjectTrackingResult objectTrackingResult) {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.mFocusRectangles.onObjectTracked(objectTrackingResult);
    }
    
    private void onViewFinderStateChanged(final CaptureState obj, final Object... array) {
        final boolean verbose = CamLog.VERBOSE;
        final boolean b = false;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onViewFinderStateChanged():[IN][currentState=");
            sb.append(obj);
            sb.append("]");
            CamLog.d(sb.toString());
        }
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[obj.ordinal()]) {
            default: {
                return;
            }
            case 26: {
                if (this.mHintText != null) {
                    this.mHintText.clearAll();
                }
                return;
            }
            case 14: {
                if (this.getCapturingMode() == CapturingMode.SLOW_MOTION && this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.STANDARD_SLOW_MOTION) {
                    this.changeToStandardSlowMotionRecordingView();
                    return;
                }
                this.changeToVideoRecordingView();
                return;
            }
            case 7: {
                if (array != null && array.length != 0) {
                    this.changeToDialogView(this.mCurrentDisplayingUiComponent = (UiComponentKind)array[0]);
                }
                return;
            }
            case 5: {
                this.mCurrentDisplayingUiComponent = null;
                if (this.getCapturingMode() == CapturingMode.SLOW_MOTION && !this.mActivity.getLaunchCondition().getLaunchCameraMode().isSlowMotion()) {
                    final SlowMotion slowMotion = (SlowMotion)this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION);
                    this.postSlowMotionHintText();
                    if (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[slowMotion.ordinal()] == 1) {
                        if (this.mHintText != null) {
                            this.mHintText.cancel(HintTextSuperSlowMotionVideoRecording.createTag(true));
                            this.mHintText.cancel(HintTextSuperSlowMotionVideoRecording.createTag(false));
                        }
                    }
                }
                if (this.isHeadUpDisplayReady()) {
                    this.changeToVideoReadyView();
                }
                if (array == null || array.length == 0) {
                    this.requestToDimSystemUi();
                    return;
                }
                if (array[0] != UiComponentKind.ZOOM_BAR) {
                    this.requestToDimSystemUi();
                }
                return;
            }
            case 4: {
                this.mCurrentDisplayingUiComponent = null;
                this.mBurstShootingRejectedReason = BurstRejectedReason.NONE;
                this.changeToPhotoReadyView(false);
                if (array == null || array.length == 0) {
                    this.requestToDimSystemUi();
                    return;
                }
                if (array[0] != UiComponentKind.ZOOM_BAR) {
                    this.requestToDimSystemUi();
                }
                return;
            }
            case 3: {
                this.mTouchEventDispatcher.start();
                this.mIsThermalWarningDialogShown = false;
                if (!this.mActivity.awaitViewFinderReady()) {
                    return;
                }
                this.resumeView((FastCapture)array[0], (boolean)array[1]);
                return;
            }
            case 1:
            case 2:
            case 15:
            case 16: {
                return;
            }
            case 29: {
                this.changeToWaitForHighFrameRateRecordingDoneView();
                return;
            }
            case 28: {
                this.changeToSuperSlowMotionVideoLowFrameRateRecordingView();
                return;
            }
            case 27: {
                this.changeToSuperSlowMotionVideoHighFrameRateRecordingView();
                return;
            }
            case 25: {
                this.changeToModeTransitionView();
                return;
            }
            case 24: {
                boolean booleanValue = b;
                if (array != null) {
                    booleanValue = b;
                    if (array.length != 0) {
                        booleanValue = (boolean)array[0];
                    }
                }
                this.changeToReadyForRecordView(booleanValue);
                return;
            }
            case 23: {
                this.changeToModeTransitionView();
                return;
            }
            case 22: {
                this.changeToVideoRecordingPauseView();
                return;
            }
            case 21: {
                this.release();
                this.getDownHeadUpDisplay();
                return;
            }
            case 20: {
                if (this.isHeadUpDisplayReady()) {
                    this.mSideTouchUi.destroyIcon();
                }
                if (this.mFocusRectangles != null) {
                    if (this.isTouchFocus()) {
                        this.disableSemiAutoControl();
                    }
                    this.hideAutoReview();
                    this.mFocusRectangles.clearAllFocus();
                    if (this.getCapturingMode() != CapturingMode.FRONT_VIDEO && this.getCapturingMode() != CapturingMode.VIDEO && this.getCapturingMode() != CapturingMode.SLOW_MOTION) {
                        this.changeToPhotoReadyView(false);
                    }
                    else {
                        this.changeToVideoReadyView();
                    }
                }
                this.requestToDimSystemUi();
                return;
            }
            case 19: {
                this.mIsRequestingStartActivity = false;
                this.mHandler.removeCallbacks(this.mAfterSwitchAnimationTask);
                this.mTouchEventDispatcher.start();
                this.hideAndCancelAllView();
                return;
            }
            case 18: {
                this.showBlank();
                this.hideAndCancelAllView();
                return;
            }
            case 17: {
                this.attachSideAutoReview();
                return;
            }
            case 13: {
                this.changeToBurstCaptureView();
                return;
            }
            case 12: {
                this.changeToPhotoCaptureView();
                this.clearTouchedScreenButtonGroup();
                return;
            }
            case 11: {
                this.changeToBurstCaptureWaitForAfDoneView();
                return;
            }
            case 10: {
                this.changeToPhotoCaptureWaitForAfDoneView();
                return;
            }
            case 9: {
                this.changeToPhotoFocusDoneView((Boolean)array[0]);
                return;
            }
            case 8: {
                this.changeToPhotoFocusSearchView();
                return;
            }
            case 6: {
                this.changeToSelftimerView((boolean)array[0]);
            }
        }
    }
    
    private void openInstantViewer(final byte[] array, final String s, final SavingRequest obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("openInstantViewer: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (this.mInstantViewer != null) {
            if (!this.mInstantViewer.open(array, s, obj.common.mimeType, 0, obj.common.orientation, this.isFront(), new ReviewWindowListenerImpl(), obj.getRequestId())) {
                this.closeInstantViewer();
            }
            else {
                this.hideApplicationNavigator();
            }
        }
    }
    
    private void openSettingMenuDialogInChina() {
        if (RegionConfig.isChinaRegion((Context)this.mActivity)) {
            this.mSettingUi.openSettingMenuDialog();
        }
    }
    
    private void openUserSelectMenu(final UserSettingKey userSettingKey) {
        if (userSettingKey != null) {
            if (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] == 3) {
                if (((VideoSize)this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SIZE)).is4KVideo()) {
                    this.mSettingDialogStack.closeAllSettingDialogs(false);
                }
                this.requestToRecoverSystemUi();
            }
        }
        this.updateUiComponent(UiComponentKind.SETTING_DIALOG);
        this.mSettingUi.openUserSelectMenu(userSettingKey);
    }
    
    private void pause() {
        this.mIsPaused = true;
        if (this.isHeadUpDisplayReady()) {
            this.disableSemiAutoControl();
            this.disableOverlayControl(this.getBaseLayout().getImageQualityControl());
        }
        if (this.mAnimationController != null) {
            this.mAnimationController.pause();
            this.setIsSwitchingAnimationProgress(false);
        }
        this.mBaseLayout.pause();
        if (this.mInstantViewer != null) {
            this.mInstantViewer.releaseAlbumPreloader();
        }
        final TutorialController tutorial = this.getBaseLayout().getTutorial();
        if (tutorial != null) {
            LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.OTHER);
            LocalResearchUtil.getInstance().closeSetupWizard();
            tutorial.pause();
        }
        if (this.mAnimationController != null) {
            this.mAnimationController.pause();
        }
        if (this.mHintText != null) {
            this.mHintText.clearAll();
        }
        this.mDelayUpdatedViewTaskList.clear();
    }
    
    private void pauseView() {
        this.pause();
        this.mEvf.onPause();
        if (this.mBaseLayout != null && this.mBaseLayout.getGeoTagIndicator() != null) {
            this.mBaseLayout.getGeoTagIndicator().release();
        }
        if (this.mSettingDialogStack != null) {
            this.mSettingDialogStack.closeAllSettingDialogs();
        }
        if (this.mCaptureFeedback != null) {
            this.mCaptureFeedback.onPause();
        }
        if (this.mViewFinderCaptureArea != null) {
            this.mViewFinderCaptureArea.setCaptureAreaStateListener(null);
        }
        this.clearPreInflatedViews();
        if (this.mPreviewCover != null) {
            this.mPreviewCover.setVisibility(4);
        }
        this.mIsSetupHeadupDisplayInvoked = false;
        if (this.mLoopsManager != null && this.mLoopsManager.isConnected()) {
            this.mLoopsManager.disconnect();
        }
        this.mLoopsManager = null;
    }
    
    private void postHintText(final HintTextContent hintTextContent) {
        if (this.mHintText != null) {
            this.mHintText.post(hintTextContent);
            this.updateVisibilityForSpecificDisplaySize();
        }
    }
    
    public static final void preload() {
    }
    
    private void preparationForInstantViewer() {
        if (CamLog.VERBOSE) {
            CamLog.d("preparationForInstantViewer");
        }
        if (!this.mStateMachine.isRecording() && this.getBaseLayout().getContentsViewController() != null) {
            this.getBaseLayout().getContentsViewController().setClickThumbnailProgressListener((ContentsViewController.OnClickThumbnailProgressListener)new OnClickThumbnailProgressListenerImpl());
        }
        if (this.mInstantViewer != null && !this.mInstantViewer.isOpened()) {
            this.mInstantViewer.clear();
        }
    }
    
    private boolean prepareInstantViewer(final Uri albumBitmap) {
        return this.mInstantViewer.setAlbumBitmap(albumBitmap);
    }
    
    private void release() {
        this.mBaseLayout.release();
        this.mWindowDisplayFlashScreen = null;
        this.mActivity.removeOrienationListener((CameraActivity.LayoutOrientationChangedListener)this);
        if (this.mActivity.getGeoTagManager() != null) {
            this.mActivity.getGeoTagManager().setLocationAcquiredListener(null);
        }
        this.mActivity.getStorage().removeStorageStateListener(this.mStorageStateListener);
        if (CamLog.VERBOSE) {
            CamLog.d("release() is called.");
        }
    }
    
    private void releaseSurfaceBlinderView() {
        if (this.mSurfaceBlinderView != null) {
            this.mSurfaceBlinderView.setVisibility(4);
            final FrameLayout previewOverlayContainer = this.getBaseLayout().getPreviewOverlayContainer();
            if (previewOverlayContainer != null) {
                previewOverlayContainer.removeView(this.mSurfaceBlinderView);
            }
            this.mSurfaceBlinderView = null;
        }
    }
    
    private void removeSelfTimerCountDownView() {
        if (this.mSelfTimerCountDownView != null) {
            this.getBaseLayout().getLazyInflatedUiComponentContainerBack().removeView((View)this.mSelfTimerCountDownView);
            this.mSelfTimerCountDownView = null;
        }
    }
    
    private boolean requestAnimation(final AnimationRequest animationRequest) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke source:");
            sb.append(animationRequest.mType);
            sb.append(", type:");
            sb.append(animationRequest.mDegree);
            sb.append(", from:");
            sb.append(animationRequest.mFrom.name());
            sb.append(", target:");
            sb.append(animationRequest.mTarget.name());
            CamLog.d(sb.toString());
        }
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationType[animationRequest.mType.ordinal()]) {
            default: {
                return false;
            }
            case 5: {
                return this.requestSwitchAnimation(animationRequest);
            }
            case 4: {
                return this.requestMostRecentlyUsedAnimation(animationRequest);
            }
            case 3: {
                return this.requestModeSelectorAnimation(animationRequest);
            }
            case 2: {
                return this.requestModeIconAnimation(animationRequest);
            }
            case 1: {
                return this.requestModeSwipeAnimation(animationRequest);
            }
        }
    }
    
    private boolean requestModeIconAnimation(AnimationRequest animationRequest) {
        final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()];
        if (n != 4) {
            switch (n) {
                case 2: {
                    if (this.mAnimationController.requestAnimation(animationRequest, (TransitionAnimationController.TransitionAnimationCallback)new TransitionAnimationController.TransitionAnimationCallback(this, animationRequest) {
                        final ViewFinderImpl this$0;
                        final AnimationRequest val$request;
                        
                        @Override
                        public void onAnimationFinished() {
                            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, this.val$request);
                        }
                    })) {
                        LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.ICON_TOUCH);
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(animationRequest.mFrom, animationRequest.mTarget);
                        return true;
                    }
                    break;
                }
                case 1: {
                    if (this.mAnimationController.requestAnimation(animationRequest)) {
                        animationRequest = new AnimationRequest(AnimationRequest.AnimationType.MODE_ICON, AnimationRequest.AnimationDegree.EXEC, animationRequest.mFrom, animationRequest.mTarget);
                        this.mActivity.runOnUiThread((Runnable)new Runnable(this, animationRequest) {
                            final ViewFinderImpl this$0;
                            final AnimationRequest val$nextRequest;
                            
                            @Override
                            public void run() {
                                if (CamLog.DEBUG) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("invoke current:");
                                    sb.append(this.val$nextRequest.mFrom.name());
                                    sb.append(", target:");
                                    sb.append(this.val$nextRequest.mTarget.name());
                                    CamLog.d(sb.toString());
                                }
                                this.this$0.hideViews();
                                this.this$0.sendViewUpdateEvent(ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
                                this.this$0.requestAnimation(this.val$nextRequest);
                            }
                        });
                        return true;
                    }
                    break;
                }
            }
            return false;
        }
        return this.mAnimationController.requestAnimation(animationRequest);
    }
    
    private boolean requestModeSelectorAnimation(AnimationRequest animationRequest) {
        final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()];
        if (n != 4) {
            switch (n) {
                case 2: {
                    if (this.mAnimationController.requestAnimation(animationRequest, (TransitionAnimationController.TransitionAnimationCallback)new TransitionAnimationController.TransitionAnimationCallback(this, animationRequest) {
                        final ViewFinderImpl this$0;
                        final AnimationRequest val$request;
                        
                        @Override
                        public void onAnimationFinished() {
                            if (this.val$request.mFrom.isFront() != this.val$request.mTarget.isFront()) {
                                this.this$0.setIsCameraSwitching(true);
                                this.this$0.mHandler.post((Runnable)new Runnable(this, new AnimationRequest(AnimationRequest.AnimationType.MODE_SELECTOR, AnimationRequest.AnimationDegree.FINISH, this.val$request.mFrom, this.val$request.mTarget)) {
                                    final ViewFinderImpl$14 this$1;
                                    final AnimationRequest val$nextRequest;
                                    
                                    @Override
                                    public void run() {
                                        if (CamLog.DEBUG) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("invoke current:");
                                            sb.append(this.val$nextRequest.mFrom.name());
                                            sb.append(", target:");
                                            sb.append(this.val$nextRequest.mTarget.name());
                                            CamLog.d(sb.toString());
                                        }
                                        this.this$1.this$0.requestAnimation(this.val$nextRequest);
                                    }
                                });
                            }
                            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, this.val$request);
                        }
                    })) {
                        LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MODE_SELECTOR);
                        LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MODE_SELECTOR);
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(animationRequest.mFrom, animationRequest.mTarget);
                        return true;
                    }
                    break;
                }
                case 1: {
                    if (this.mAnimationController.requestAnimation(animationRequest)) {
                        animationRequest = new AnimationRequest(AnimationRequest.AnimationType.MODE_SELECTOR, AnimationRequest.AnimationDegree.EXEC, animationRequest.mFrom, animationRequest.mTarget);
                        this.mHandler.post((Runnable)new Runnable(this, animationRequest) {
                            final ViewFinderImpl this$0;
                            final AnimationRequest val$nextRequest;
                            
                            @Override
                            public void run() {
                                if (CamLog.DEBUG) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("invoke current:");
                                    sb.append(this.val$nextRequest.mFrom.name());
                                    sb.append(", target:");
                                    sb.append(this.val$nextRequest.mTarget.name());
                                    CamLog.d(sb.toString());
                                }
                                this.this$0.hideSurface();
                                this.this$0.hideViews();
                                this.this$0.sendViewUpdateEvent(ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
                                this.this$0.requestAnimation(this.val$nextRequest);
                            }
                        });
                        return true;
                    }
                    break;
                }
            }
            return false;
        }
        this.getBaseLayout().getModeButtonShortcut().update(ModeSelectorInternalMode.exists(animationRequest.mTarget));
        this.getBaseLayout().getMruButtonContainer().setAvailability(ModeSelectorInternalMode.exists(animationRequest.mTarget) ^ true);
        return this.mAnimationController.requestAnimation(animationRequest);
    }
    
    private boolean requestModeSwipeAnimation(final AnimationRequest animationRequest) {
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()]) {
            default: {
                return false;
            }
            case 4: {
                return this.mAnimationController.requestAnimation(animationRequest);
            }
            case 3: {
                return this.mAnimationController.requestAnimation(animationRequest, (TransitionAnimationController.TransitionAnimationCallback)new TransitionAnimationController.TransitionAnimationCallback(this, animationRequest) {
                    final ViewFinderImpl this$0;
                    final AnimationRequest val$request;
                    
                    @Override
                    public void onAnimationFinished() {
                        this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, this.val$request);
                    }
                });
            }
            case 2: {
                return this.mAnimationController.requestAnimation(animationRequest, (TransitionAnimationController.TransitionAnimationCallback)new TransitionAnimationController.TransitionAnimationCallback(this, animationRequest) {
                    final ViewFinderImpl this$0;
                    final AnimationRequest val$request;
                    
                    @Override
                    public void onAnimationFinished() {
                        PerfLog.SWIPE_ANIMATION_END.transit();
                        this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, this.val$request);
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(this.val$request.mFrom, this.val$request.mTarget);
                    }
                });
            }
            case 1: {
                final boolean requestAnimation = this.mAnimationController.requestAnimation(animationRequest);
                if (requestAnimation) {
                    this.hideViews();
                }
                return requestAnimation;
            }
        }
    }
    
    private boolean requestMostRecentlyUsedAnimation(AnimationRequest animationRequest) {
        final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()];
        if (n != 4) {
            switch (n) {
                case 2: {
                    if (this.mAnimationController.requestAnimation(animationRequest, (TransitionAnimationController.TransitionAnimationCallback)new TransitionAnimationController.TransitionAnimationCallback(this, animationRequest) {
                        final ViewFinderImpl this$0;
                        final AnimationRequest val$request;
                        
                        @Override
                        public void onAnimationFinished() {
                            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, this.val$request);
                            if (this.val$request.mFrom.isFront() != this.val$request.mTarget.isFront()) {
                                this.this$0.mHandler.post((Runnable)new Runnable(this, new AnimationRequest(AnimationRequest.AnimationType.MRU_SHORTCUT, AnimationRequest.AnimationDegree.FINISH, this.val$request.mFrom, this.val$request.mTarget)) {
                                    final ViewFinderImpl$16 this$1;
                                    final AnimationRequest val$nextRequest;
                                    
                                    @Override
                                    public void run() {
                                        if (CamLog.DEBUG) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("invoke current:");
                                            sb.append(this.val$nextRequest.mFrom.name());
                                            sb.append(", target:");
                                            sb.append(this.val$nextRequest.mTarget.name());
                                            CamLog.d(sb.toString());
                                        }
                                        this.this$1.this$0.requestAnimation(this.val$nextRequest);
                                    }
                                });
                            }
                        }
                    })) {
                        LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MRU_SHORTCUT);
                        LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MRU_SHORTCUT);
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(animationRequest.mFrom, animationRequest.mTarget);
                        return true;
                    }
                    break;
                }
                case 1: {
                    if (this.mAnimationController.requestAnimation(animationRequest)) {
                        animationRequest = new AnimationRequest(AnimationRequest.AnimationType.MRU_SHORTCUT, AnimationRequest.AnimationDegree.EXEC, animationRequest.mFrom, animationRequest.mTarget);
                        this.mHandler.post((Runnable)new Runnable(this, animationRequest) {
                            final ViewFinderImpl this$0;
                            final AnimationRequest val$nextRequest;
                            
                            @Override
                            public void run() {
                                if (CamLog.DEBUG) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("invoke current:");
                                    sb.append(this.val$nextRequest.mFrom.name());
                                    sb.append(", target:");
                                    sb.append(this.val$nextRequest.mTarget.name());
                                    CamLog.d(sb.toString());
                                }
                                this.this$0.hideSurface();
                                this.this$0.hideViews();
                                this.this$0.sendViewUpdateEvent(ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
                                this.this$0.requestAnimation(this.val$nextRequest);
                            }
                        });
                        return true;
                    }
                    break;
                }
            }
            return false;
        }
        this.getBaseLayout().getModeButtonShortcut().update(ModeSelectorInternalMode.exists(animationRequest.mTarget));
        this.getBaseLayout().getMruButtonContainer().setAvailability(ModeSelectorInternalMode.exists(animationRequest.mTarget) ^ true);
        return this.mAnimationController.requestAnimation(animationRequest);
    }
    
    private void requestSetupHeadUpDisplay() {
        this.setupHeadUpDisplay();
        if (CamLog.VERBOSE) {
            MeasurePerformance.outResultDelay(1000);
        }
    }
    
    private boolean requestStartActivity(final Intent intent, final Bundle bundle) {
        final boolean inLockTaskMode = this.mActivity.isInLockTaskMode();
        boolean b = false;
        if (!inLockTaskMode) {
            this.mCameraDevice.closeCamera(true);
            this.mIsRequestingStartActivity = true;
            try {
                if (CapturingModeUtil.MODE_WHITE_LIST.contains(intent.getStringExtra("com.sonymobile.camera.addon.intent.extra.CAPTURING_MODE"))) {
                    if (bundle != null) {
                        this.mActivity.startActivityForResult(intent, 19, bundle);
                    }
                    else {
                        this.mActivity.startActivityForResult(intent, 19);
                    }
                }
                else {
                    this.mActivity.startActivity(intent);
                    this.onAppsUiModeFinish();
                }
                b = true;
            }
            catch (final ActivityNotFoundException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to launch the AddOn application. Message : ");
                sb.append(ex.getMessage());
                CamLog.e(sb.toString());
            }
            return b;
        }
        this.mActivity.finish();
        return false;
    }
    
    private void requestStartActivityForMessageDialog(final Intent intent, final Bundle bundle) {
        final Handler handler = this.getBaseLayout().getRootView().getHandler();
        if (handler != null) {
            handler.post((Runnable)new Runnable(this, intent, bundle) {
                final ViewFinderImpl this$0;
                final Intent val$intent;
                final Bundle val$options;
                
                @Override
                public void run() {
                    this.this$0.requestStartActivity(this.val$intent, this.val$options);
                    this.this$0.mActivity.abort();
                }
            });
        }
    }
    
    private boolean requestSwitchAnimation(final AnimationRequest animationRequest) {
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()]) {
            default: {
                return false;
            }
            case 4: {
                return this.mAnimationController.requestAnimation(animationRequest);
            }
            case 3: {
                return this.mAnimationController.requestAnimation(animationRequest, (TransitionAnimationController.TransitionAnimationCallback)new TransitionAnimationController.TransitionAnimationCallback(this, animationRequest) {
                    final ViewFinderImpl this$0;
                    final AnimationRequest val$request;
                    
                    @Override
                    public void onAnimationFinished() {
                        this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, this.val$request);
                        this.this$0.resetAnimationProperty();
                    }
                });
            }
            case 2: {
                return this.mAnimationController.requestAnimation(animationRequest, (TransitionAnimationController.TransitionAnimationCallback)new TransitionAnimationController.TransitionAnimationCallback(this, animationRequest) {
                    final ViewFinderImpl this$0;
                    final AnimationRequest val$request;
                    
                    @Override
                    public void onAnimationFinished() {
                        this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, this.val$request);
                        this.this$0.mHandler.post(this.this$0.mAfterSwitchAnimationTask);
                        this.this$0.showSurface();
                    }
                });
            }
            case 1: {
                return this.startDraggingSwitchStartedAnimation();
            }
        }
    }
    
    private void requestToDimSystemUi() {
        this.getBaseLayout().requestToDimSystemUi();
    }
    
    private void requestToRecoverSystemUi() {
        this.getBaseLayout().requestToRecoverSystemUi();
    }
    
    private void requestToRestoreSystemUi() {
        this.getBaseLayout().requestToRestoreSystemUi();
    }
    
    private void resetAnimationProperty() {
        this.mAnimationController.resume();
    }
    
    private void resizeEvfScope(final Rect rect) {
        final Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(rect);
        final int width = rectAccordingToLayoutOrientation.width();
        final int height = rectAccordingToLayoutOrientation.height();
        final float n = width / (float)height;
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.getPreviewLayoutParams();
        if (width == height) {
            final Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize((Context)this.mActivity);
            if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 1) {
                layoutParams.topMargin += viewFinderSize.height() / 3;
            }
            else {
                layoutParams.leftMargin += viewFinderSize.height() / 3;
            }
        }
        this.mEvf.asView().setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        final Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect((Context)this.mActivity, n, this.mScreenAspect);
        this.mEvf.resize(surfaceViewRect.width(), surfaceViewRect.height());
        this.mEvf.setFixedSurfaceSize(rect.width(), rect.height());
    }
    
    private void resume(final CapturingMode capturingMode, final NavigatorContents navigatorContents) {
        this.mIsPaused = false;
        if (this.isHeadUpDisplayReady()) {
            this.setApplicationNavigatorEnabled(false);
            if (this.mAnimationController != null) {
                this.mAnimationController.resume();
            }
        }
        if (this.mBaseLayout != null && this.mBaseLayout.getContentsViewController() != null) {
            this.mBaseLayout.getContentsViewController().remove();
        }
        if (this.isHeadUpDisplayReady()) {
            this.mBaseLayout.setupBlankScreen();
        }
        this.mBaseLayout.resume();
    }
    
    private void resumeApplicationNavigator(final NavigatorContents navigatorContents) {
        final boolean oneShot = this.getActivity().isOneShot();
        if (this.mApplicationNavigator != null) {
            this.setApplicationNavigatorEnabled(oneShot ^ true);
            if (!oneShot) {
                this.mApplicationNavigator.resume(navigatorContents);
            }
        }
    }
    
    private void resumeView(final FastCapture fastCapture, final boolean b) {
        final CapturingMode launchCapturingMode = this.mStateMachine.getLaunchCapturingMode();
        if (this.isHeadUpDisplayReady()) {
            if (this.mInstantViewer != null && this.mInstantViewer.isOpened()) {
                this.mInstantViewer.hide();
            }
            this.mCanFocusRectanglesBeUpdated = true;
            this.mEvf.onResume();
            this.resume(launchCapturingMode, NavigatorContents.valueOf(launchCapturingMode));
        }
        if (this.mCaptureFeedback != null) {
            this.mCaptureFeedback.onResume();
        }
        if (fastCapture != FastCapture.LAUNCH_AND_CAPTURE && this.mCapturingModeWhenLastSetupHeadDisplay == launchCapturingMode && !b) {
            this.mScreenButtonHandler.refreshButton();
        }
        if (this.mActivity.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            this.showMessageDialog(DialogId.PREDICTIVE_LAUNCH_DESCRIPTION, new Object[0]);
            final boolean doCapture = ((PredictiveLaunch)this.mStateMachine.getUserSetting().get(UserSettingKey.PREDICTIVE_LAUNCH)).doCapture();
            final BaseLayout baseLayout = this.getBaseLayout();
            final PredictiveLaunchCoverTouchListenerImpl predictiveLaunchCoverTouchListenerImpl = new PredictiveLaunchCoverTouchListenerImpl();
            PredictiveLaunchCoverView.PredictiveLaunchCoverType predictiveLaunchCoverType;
            if (doCapture) {
                predictiveLaunchCoverType = PredictiveLaunchCoverView.PredictiveLaunchCoverType.TOUCH_TO_LAUNCH_AND_CAPTURE;
            }
            else {
                predictiveLaunchCoverType = PredictiveLaunchCoverView.PredictiveLaunchCoverType.TOUCH_TO_LAUNCH;
            }
            baseLayout.setupPredictiveLaunchCoverView(predictiveLaunchCoverTouchListenerImpl, predictiveLaunchCoverType);
            this.changeLayoutTo(BaseLayoutPattern.CLEAR);
            this.setApplicationNavigatorEnabled(false);
            this.updateGridLineView();
            this.getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
            if (this.mLoopsManager == null) {
                this.mLoopsManager = new XperiaXLoopsManager((Context)this.mActivity);
            }
            this.mLoopsManager.connect();
        }
    }
    
    private void retryToCheckEvfPreparationDelayed() {
        this.cancelCheckEvfPreparationTask();
        CameraApplication.getUiThreadHandler().postDelayed(this.mCheckEvfPreparationTask, 100L);
    }
    
    private LayoutPattern selectLayoutPatternForPreview() {
        BaseLayoutPattern baseLayoutPattern = BaseLayoutPattern.PREVIEW;
        if (!this.isStorageReady()) {
            baseLayoutPattern = BaseLayoutPattern.PREVIEW_NO_RECORDING;
        }
        if (this.predictiveLaunchCoverExists()) {
            baseLayoutPattern = BaseLayoutPattern.CLEAR;
        }
        return baseLayoutPattern;
    }
    
    private void setApplicationNavigatorEnabled(boolean navigationEnabled) {
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.getCapturingMode().ordinal()]) {
            default: {
                navigationEnabled = false;
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                if (this.predictiveLaunchCoverExists()) {
                    navigationEnabled = false;
                }
                if (this.mApplicationNavigator != null) {
                    this.mApplicationNavigator.setNavigationEnabled(navigationEnabled);
                }
            }
        }
    }
    
    private void setApplicationNavigatorPosition(final NavigatorContents navigatorContents, final float n) {
        this.mApplicationNavigator.setDraggingPosition(navigatorContents, n);
    }
    
    private void setEvfPrepared(final boolean b) {
        if (this.mSettingDialogStack != null) {
            this.mSettingDialogStack.setCanceledOnTouchOutside(b);
        }
        this.mIsEvfPrepared = b;
        final StringBuilder sb = new StringBuilder();
        sb.append("setEvfPrepared() : evfPrepared & onTouchOutside = ");
        sb.append(b);
        CamLog.d(sb.toString());
    }
    
    private void setFrontAngleSwitchButtonClickable(final boolean clickable) {
        if (this.mFrontAngleSwitchButton == null) {
            return;
        }
        this.mFrontAngleSwitchButton.setClickable(clickable);
    }
    
    private void setFrontAngleSwitchButtonVisibility(final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setFrontAngleSwitchButtonVisibility visible: ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        if (this.mFrontAngleSwitchButton == null) {
            return;
        }
        if (b) {
            this.mFrontAngleSwitchButton.setOnClickListener(this.mFrontAngleSwitchButtonClickListener);
            this.mFrontAngleSwitchButton.show();
        }
        else {
            this.mFrontAngleSwitchButton.hide();
            this.mFrontAngleSwitchButton.setOnClickListener((View$OnClickListener)null);
        }
        this.mFrontAngleSwitchButton.setClickable(b);
    }
    
    private void setIsSwitchingAnimationProgress(final boolean mIsSwitchingAnimationProgress) {
        this.mIsSwitchingAnimationProgress = mIsSwitchingAnimationProgress;
    }
    
    private void setLeftIconsVisibility(final boolean b) {
        if (b) {
            this.getBaseLayout().showLeftIconContainer();
        }
        else {
            this.getBaseLayout().hideLeftIconContainer();
        }
    }
    
    private void setMruAvailability(boolean availability) {
        if (this.mActivity.isOneShot()) {
            availability = false;
        }
        this.getBaseLayout().getMruButtonContainer().setAvailability(availability);
    }
    
    private void setOrientation(final int uiOrientation) {
        if (this.mStateMachine.isRecording()) {
            this.mBaseLayout.setOrientation(uiOrientation, this.mRecordingOrientation);
            this.mOrientation = uiOrientation;
            if (this.mApplicationNavigator != null) {
                this.mApplicationNavigator.setOrientation(this.mOrientation);
            }
        }
        else {
            this.mBaseLayout.setOrientation(uiOrientation);
            this.mOrientation = uiOrientation;
            if (this.mApplicationNavigator != null) {
                this.mApplicationNavigator.setOrientation(this.mOrientation);
            }
            if (this.mHintText != null) {
                this.updateHintTextUiOrientation();
            }
        }
        final OnScreenButtonGroup onScreenButtonGroup = this.getBaseLayout().getOnScreenButtonGroup();
        if (onScreenButtonGroup != null) {
            onScreenButtonGroup.setUiOrientation(uiOrientation);
        }
        if (this.mMessageDialog != null) {
            this.mMessageDialog.setSensorOrientation(uiOrientation);
        }
        if (this.mToastContent != null) {
            this.mToastContent.setSensorOrientation(uiOrientation);
        }
        if (this.isHeadUpDisplayReady()) {
            if (this.mSettingDialogStack != null) {
                this.mSettingDialogStack.setUiOrientation(uiOrientation);
            }
            if (this.mInstantViewer != null) {
                this.mInstantViewer.setUiOrientation(uiOrientation);
            }
            if (this.mFocusRectangles != null) {
                this.mFocusRectangles.setOrientation(uiOrientation);
            }
            if (this.mSelfTimerCountDownView != null) {
                this.mSelfTimerCountDownView.setSensorOrientation(uiOrientation);
            }
            if (this.mSettingUi != null) {
                this.mSettingUi.setSensorOrientation(uiOrientation);
            }
            if (this.mBurstCountView != null) {
                this.mBurstCountView.setUiOrientation(uiOrientation);
            }
            this.mSideTouchUi.setUiOrientation(uiOrientation);
        }
    }
    
    private void setPreInflatedHeadUpDisplay(final View mPreInflatedHeadUpDisplay) {
        this.mPreInflatedHeadUpDisplay = mPreInflatedHeadUpDisplay;
    }
    
    private void setPreviewAlpha(final int n) {
        this.mPreviewCover.setAlpha(TransitionAnimationController.getPreviewAlpha((Context)this.getActivity(), n));
    }
    
    private void setZoomRatio(final int i) {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setZoomRatio() current:");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        this.mZoomBarProxy.update(PlatformCapability.getZoomRatios(this.mStateMachine.getCurrentCameraId()), i);
    }
    
    private void setZoombarVisibility(final boolean b) {
        final Zoombar zoomBar = this.getBaseLayout().getZoomBar();
        if (zoomBar != null) {
            if (b) {
                zoomBar.showImmediately();
            }
            else {
                zoomBar.hideImmediately();
            }
        }
    }
    
    private void setup(final View previewSurface) {
        this.getLayoutPatternApplier().setup(this.getBaseLayout(), this.mActivity.isOneShot());
        this.getBaseLayout().setPreviewSurface(previewSurface);
        if (CamLog.VERBOSE) {
            CamLog.d("[APP DETAIL] setup shutter : E");
        }
        this.getBaseLayout().setupPreferentialHeadUpDisplays();
        HeadUpDisplaySetupState headUpDisplaySetupState = HeadUpDisplaySetupState.PHOTO_READY;
        final CapturingMode capturingMode = this.getCapturingMode();
        if (capturingMode.isVideo()) {
            headUpDisplaySetupState = HeadUpDisplaySetupState.VIDEO_READY;
        }
        this.mCapturingModeWhenLastSetupHeadDisplay = capturingMode;
        this.setOrientation(this.mActivity.getOrientation());
        this.setSelfTimer(capturingMode, this.mPhotoSelfTimerSetting);
        this.setupOnScreenCaptureButton(headUpDisplaySetupState);
        this.changeScreenButtonImage(headUpDisplaySetupState, false);
        if (CamLog.VERBOSE) {
            CamLog.d("[APP DETAIL] setup shutter : X");
        }
        LayoutDependencyResolver.setupRotatableToast(this.mActivity);
    }
    
    private void setupAnimations() {
        this.getBaseLayout().getRootView().findViewById(2131296370).getGlobalVisibleRect(new Rect());
    }
    
    private void setupApplicationNavigator(final CameraActivity cameraActivity, final NavigatorContents navigatorContents) {
        if (this.mApplicationNavigator == null) {
            this.mApplicationNavigator = (ApplicationNavigator)cameraActivity.findViewById(2131296293);
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.mApplicationNavigator.getLayoutParams();
            final View$OnClickListener view$OnClickListener = (View$OnClickListener)new View$OnClickListener(this) {
                final ViewFinderImpl this$0;
                
                public void onClick(final View view) {
                    this.this$0.transitionModeOnNavigator((int)view.getTag());
                }
            };
            layoutParams.rightMargin = this.mBaseLayout.calculateCaptureButtonAreaHeight();
            this.mApplicationNavigator.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.mApplicationNavigator.setup(navigatorContents, this.getBaseLayout().getViewFinderRect(), this.mBaseLayout.calculateCaptureButtonAreaHeight(), (View$OnClickListener)view$OnClickListener);
            this.mApplicationNavigator.setOrientation(this.mOrientation);
        }
        this.resumeApplicationNavigator(navigatorContents);
    }
    
    private void setupApplicationNavigator(final HeadUpDisplaySetupState headUpDisplaySetupState) {
        final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[headUpDisplaySetupState.ordinal()];
        NavigatorContents navigatorContents = null;
        Label_0070: {
            if (n != 11) {
                switch (n) {
                    default: {
                        navigatorContents = NavigatorContents.SUPERIOR_AUTO;
                        break Label_0070;
                    }
                    case 1:
                    case 2:
                    case 3: {
                        navigatorContents = NavigatorContents.SUPERIOR_AUTO;
                        break Label_0070;
                    }
                    case 4:
                    case 5: {
                        break;
                    }
                }
            }
            navigatorContents = NavigatorContents.VIDEO;
        }
        this.setupApplicationNavigator(this.mActivity, navigatorContents);
    }
    
    private void setupAutoReview() {
        this.getBaseLayout().setupAutoReview();
    }
    
    private void setupCaptureArea(final HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (this.mViewFinderCaptureArea == null) {
            this.mViewFinderCaptureArea = (CaptureArea)this.mActivity.findViewById(2131296696);
            this.updateCaptureAreaSize();
        }
        this.mViewFinderCaptureArea.setCaptureAreaStateListener((CaptureArea.CaptureAreaStateListener)new ViewFinderStateListener());
    }
    
    private void setupCaptureButtonArea() {
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.mActivity.findViewById(2131296534).getLayoutParams();
        layoutParams.width = this.mBaseLayout.calculateCaptureButtonAreaHeight();
        this.mActivity.findViewById(2131296534).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    private void setupContentsView() {
        this.mStateMachine.sendStaticEvent(StaticEvent.EVENT_ON_PHOTO_STACK_INITIALIZED, this.getBaseLayout().getContentsViewController());
    }
    
    private void setupDraggingEventHandler() {
        this.getBaseLayout().setOnViewFinderGestureDetector(new AbstractDraggingEventHandler(this, this.mActivity, TransitionAnimationController.getSwipeThreshold((Context)this.mActivity), TransitionAnimationController.getSwitchSwipeThreshold((Context)this.mActivity)) {
            final ViewFinderImpl this$0;
            
            private int getModeIndexUnder(final MotionEvent motionEvent) {
                if (this.this$0.mApplicationNavigator == null) {
                    return -1;
                }
                return this.this$0.mApplicationNavigator.getModeIndexUnder((int)motionEvent.getX(), (int)motionEvent.getY());
            }
            
            private CapturingMode getSwitchTargetMode(CapturingMode capturingMode) {
                switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
                    default: {
                        capturingMode = CapturingMode.SCENE_RECOGNITION;
                        break;
                    }
                    case 6: {
                        capturingMode = CapturingMode.NORMAL;
                        break;
                    }
                    case 5: {
                        capturingMode = CapturingMode.FRONT_PHOTO;
                        break;
                    }
                    case 4: {
                        capturingMode = CapturingMode.VIDEO;
                        break;
                    }
                    case 3: {
                        capturingMode = CapturingMode.FRONT_VIDEO;
                        break;
                    }
                    case 2: {
                        capturingMode = CapturingMode.SCENE_RECOGNITION;
                        break;
                    }
                    case 1: {
                        capturingMode = CapturingMode.SUPERIOR_FRONT;
                        break;
                    }
                }
                return capturingMode;
            }
            
            private boolean isModeChangingEnable(final Direction direction) {
                final CapturingMode access$900 = this.this$0.getCapturingMode();
                switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$controller$AbstractDraggingEventHandler$Direction[direction.ordinal()]) {
                    default: {
                        return false;
                    }
                    case 3:
                    case 4: {
                        return access$900 != CapturingMode.SLOW_MOTION;
                    }
                    case 1:
                    case 2: {
                        return access$900 != CapturingMode.NORMAL && access$900 != CapturingMode.SLOW_MOTION && access$900 != CapturingMode.FRONT_PHOTO;
                    }
                }
            }
            
            @Override
            protected boolean canDragging() {
                return this.this$0.mIsSettingChangeAcceptable && this.this$0.isUserOperable();
            }
            
            @Override
            public void onStartDragging(final MotionEvent motionEvent, final MotionEvent motionEvent2) {
                super.onStartDragging(motionEvent, motionEvent2);
                if (this.getModeIndexUnder(motionEvent) == -1) {
                    LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.SWIPE);
                }
                else {
                    LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.ICON_SWIPE);
                }
            }
            
            @Override
            protected void sendCancelEvent(final Direction direction) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke source:");
                    sb.append(direction.name());
                    CamLog.d(sb.toString());
                }
                if (!this.isModeChangingEnable(direction)) {
                    return;
                }
                final CapturingMode access$900 = this.this$0.getCapturingMode();
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    if (this.this$0.mActivity.isOneShot()) {
                        return;
                    }
                    if (this.this$0.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.MODE_TOUCH, AnimationRequest.AnimationDegree.CANCEL, access$900, this.this$0.getCapturingMode()))) {
                        this.this$0.showViews();
                    }
                }
                if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                    this.this$0.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.CANCEL, access$900, this.getSwitchTargetMode(access$900)));
                }
            }
            
            @Override
            protected void sendFinishEvent(final Direction direction) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke source:");
                    sb.append(direction.name());
                    CamLog.d(sb.toString());
                }
                if (!this.isModeChangingEnable(direction)) {
                    return;
                }
                final CapturingMode access$900 = this.this$0.getCapturingMode();
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    if (this.this$0.mActivity.isOneShot()) {
                        return;
                    }
                    final CapturingMode capturingMode = ViewFinderImpl.getCapturingMode(this.this$0.getNextContent(NavigatorContents.valueOf(this.this$0.getCapturingMode()), direction), this.this$0.getCapturingMode());
                    if (CamLog.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("invoke current:");
                        sb2.append(access$900.name());
                        sb2.append(", target:");
                        sb2.append(capturingMode.name());
                        CamLog.d(sb2.toString());
                    }
                    AnimationRequest.AnimationDegree animationDegree;
                    if (access$900 == capturingMode) {
                        animationDegree = AnimationRequest.AnimationDegree.CANCEL;
                    }
                    else {
                        animationDegree = AnimationRequest.AnimationDegree.EXEC;
                    }
                    if (this.this$0.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.MODE_TOUCH, animationDegree, access$900, capturingMode))) {
                        this.this$0.sendViewUpdateEvent(ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
                        PerfLog.SWIPE_ANIMATION_START.transit();
                    }
                }
                if (direction == Direction.LEFT) {
                    this.this$0.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.CANCEL, access$900, this.getSwitchTargetMode(access$900)));
                }
                if (direction == Direction.RIGHT && this.this$0.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.EXEC, access$900, this.getSwitchTargetMode(access$900)))) {
                    this.this$0.setIsSwitchingAnimationProgress(true);
                    this.this$0.setIsCameraSwitching(true);
                    this.this$0.hideSurface();
                }
            }
            
            @Override
            protected void sendProgressEvent(final Direction direction, final int n, final float n2) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke source:");
                    sb.append(direction.name());
                    CamLog.d(sb.toString());
                }
                if (!this.isModeChangingEnable(direction)) {
                    return;
                }
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    if (this.this$0.mActivity.isOneShot()) {
                        return;
                    }
                    final NavigatorContents value = NavigatorContents.valueOf(this.this$0.getCapturingMode());
                    NavigatorContents previous;
                    if ((n <= 0) ? value.hasNext() : value.hasPrevious()) {
                        previous = value.previous();
                    }
                    else {
                        previous = value;
                    }
                    if (value == previous) {
                        return;
                    }
                    this.this$0.onModeControllableDraggingMove(value, previous, n, n2);
                }
                if (direction == Direction.RIGHT && n < 0) {
                    this.this$0.startDraggingSwitchAnimation(n2);
                }
            }
            
            @Override
            protected boolean sendStartEvent(final Direction direction) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invoke source:");
                    sb.append(direction.name());
                    CamLog.d(sb.toString());
                }
                if (!this.isModeChangingEnable(direction)) {
                    return false;
                }
                final CapturingMode access$900 = this.this$0.getCapturingMode();
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    if (this.this$0.mActivity.isOneShot()) {
                        return false;
                    }
                    final CapturingMode capturingMode = ViewFinderImpl.getCapturingMode(this.this$0.getNextContent(NavigatorContents.valueOf(this.this$0.getCapturingMode()), direction), this.this$0.getCapturingMode());
                    if (access$900 == capturingMode) {
                        return false;
                    }
                    final AnimationRequest animationRequest = new AnimationRequest(AnimationRequest.AnimationType.MODE_TOUCH, AnimationRequest.AnimationDegree.START, access$900, capturingMode);
                    if (this.this$0.requestAnimation(animationRequest)) {
                        this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest);
                        return true;
                    }
                }
                if (!PlatformCapability.isFrontCameraSupported()) {
                    return false;
                }
                if (direction == Direction.RIGHT) {
                    final AnimationRequest animationRequest2 = new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.START, access$900, this.getSwitchTargetMode(access$900));
                    if (this.this$0.requestAnimation(animationRequest2)) {
                        this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest2);
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            protected void sendTouchDownEvent(final MotionEvent motionEvent) {
                if (this.this$0.mHintText != null) {
                    this.this$0.mHintText.clearToastContent();
                }
            }
        });
    }
    
    private GLSurfaceContextView setupFeedbackContextView() {
        final GLSurfaceContextView glSurfaceContextView = new GLSurfaceContextView((Context)this.getActivity(), null);
        glSurfaceContextView.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        glSurfaceContextView.setVisibility(4);
        return glSurfaceContextView;
    }
    
    private void setupHeadUpDisplay() {
        if (CamLog.VERBOSE) {
            CamLog.d("setupHeadUpDisplay() is called.");
        }
        boolean b = false;
        if (this.mPreInflatedHeadUpDisplay == null) {
            b = true;
        }
        if (this.mPreInflatedHeadUpDisplay != null) {
            this.mBaseLayout.setPreInflatedHeadUpDisplay(this.mPreInflatedHeadUpDisplay);
            this.mPreInflatedHeadUpDisplay = null;
        }
        this.mBaseLayout.setOrientation(this.mActivity.getOrientation());
        this.mBaseLayout.setup(this.getThumbnailStateListener());
        this.mBaseLayout.getTutorial().setOnClickTutorialButtonListener(this.mOnClickTutorialButtonListener);
        this.mBaseLayout.getTutorial().setSystemUiAccessor(this.mSystemUiAccessor);
        final UserSettings userSetting = this.mStateMachine.getUserSetting();
        this.mBaseLayout.setupImageQualityControl(this.mUiControlSettings, new OverlayControlStateListener(UiComponentKind.OVERLAY_CONTROL_SEEKING), new EnumValueAccessorImpl<CapturingMode>(userSetting, UserSettingKey.CAPTURING_MODE), new EnumValueAccessorImpl<FocusRange>(userSetting, UserSettingKey.FOCUS_RANGE), new EnumValueAccessorImpl<ShutterSpeed>(userSetting, UserSettingKey.SHUTTER_SPEED), new EnumValueAccessorImpl<Iso>(userSetting, UserSettingKey.ISO), new EnumValueAccessorImpl<Ev>(userSetting, UserSettingKey.EV), new EnumValueAccessorImpl<WhiteBalance>(userSetting, UserSettingKey.WHITE_BALANCE));
        if (this.getCapturingMode() == CapturingMode.SLOW_MOTION) {
            this.mBaseLayout.getSuperSlowMotionTriggerAnimation().prepareViews();
        }
        this.updateIndicatorState();
        if (!b) {
            this.mBaseLayout.reloadContentsViewController(this.getThumbnailStateListener());
        }
        if (this.mActivity.getGeoTagManager() != null) {
            this.mActivity.getGeoTagManager().setLocationAcquiredListener(new LocationAcquiredListenerImpl());
        }
        if (this.mActivity.getStorage() != null) {
            this.mActivity.getStorage().addStorageStateListener(this.mStorageStateListener);
        }
        this.getBaseLayout().getZoomBar().setZoombarDisplayChangedListener((Zoombar.ZoombarDisplayChangedListener)new ZoombarDisplayChangedListenerImpl());
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.getCapturingMode().ordinal()]) {
            case 7: {
                this.getBaseLayout().getSuperSlowMotionTriggerAnimation().prepareViews();
                break;
            }
            case 5:
            case 6: {
                this.enableOverlayControl(this.getBaseLayout().getImageQualityControl());
                break;
            }
        }
    }
    
    private void setupHeadUpDisplay(final HeadUpDisplaySetupState headUpDisplaySetupState) {
        PerfLog.VIEWFINDER_SETUP_HEADUP_DISPLAY.begin();
        if (CamLog.VERBOSE) {
            CamLog.d("setupHeadUpDisplay ");
        }
        if (this.mCapturingModeWhenLastSetupHeadDisplay != this.getCapturingMode()) {
            this.mIsSetupHeadupDisplayInvoked = false;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setupHeadUpDisplay() prev:");
            sb.append(this.mCapturingModeWhenLastSetupHeadDisplay);
            sb.append(" current:");
            sb.append(this.getCapturingMode());
            CamLog.d(sb.toString());
        }
        if (this.mActivity.isDeviceInSecurityLock() && this.mIsSetupHeadupDisplayInvoked) {
            if (CamLog.VERBOSE) {
                CamLog.d("setupHeadUpDisplay is already invoked.");
            }
            return;
        }
        this.mCapturingModeWhenLastSetupHeadDisplay = this.getCapturingMode();
        (this.mSurfaceBlinderView = new View((Context)this.mActivity)).setBackgroundColor(-16777216);
        this.mSurfaceBlinderView.setVisibility(8);
        this.joinInflateTask();
        if (!this.isHeadUpDisplayReady()) {
            final Rect rect = this.mEvf.getRect();
            final boolean b = (LayoutOrientationResolver.getInstance().getOrientation() != LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) ? (rect.width() >= rect.height()) : (rect.width() <= rect.height());
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("isEvfReady : ");
                sb2.append(b);
                CamLog.d(sb2.toString());
            }
            if (!b) {
                this.mActivity.postDelayedEvent(new ReTrySetupHeadUpDisplayTask(), 100L);
                return;
            }
        }
        final boolean headUpDisplayReady = this.isHeadUpDisplayReady();
        if (this.isInflated()) {
            this.setPreInflatedHeadUpDisplay(this.getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.HEAD_UP_DISPLAY).get(0));
        }
        this.requestSetupHeadUpDisplay();
        if (!headUpDisplayReady) {
            this.getBaseLayout().getPreviewOverlayContainer().addView((View)this.setupViewFinderLayout());
            this.mCaptureFeedback = this.setupFeedbackContextView();
            this.getBaseLayout().getRootView().addView((View)this.mCaptureFeedback);
        }
        this.setupCaptureButtonArea();
        this.setupApplicationNavigator(headUpDisplaySetupState);
        this.setupRightIndicatorArea();
        this.setupTransitionAnimationController(this.mActivity, headUpDisplaySetupState);
        this.setupHintText();
        this.setupDraggingEventHandler();
        this.setupSettingUi();
        this.setupContentsView();
        this.setupCaptureArea(headUpDisplaySetupState);
        this.setupFocusRectangles();
        this.setupOnScreenCaptureButton(headUpDisplaySetupState);
        this.setupInstantViewer();
        this.setupAutoReview();
        this.setupSelfTimerCountDownView();
        this.mZoomBarProxy = new ZoomBarUpdateProxy();
        this.mRecordingTimeProxy = new RecordingTimeReceiverProxy();
        this.mAutoReviewProxy = new AutoReviewContentReceiverProxy();
        this.mZoomBarProxy.bindZoomBar(this.getBaseLayout().getZoomBar());
        this.mRecordingTimeProxy.bindReceiver(this.getBaseLayout().getRecordingIndicator());
        this.mAutoReviewProxy.bindReceiver(this.getBaseLayout().getAutoReview());
        this.setupSideTouchUI();
        this.setZoomRatio(0);
        this.setupPrimaryShortcutIcons();
        this.setupOnScreenShortcut();
        this.setOrientation(this.getOrientation());
        this.updateGridLineView();
        this.updateFrontAngleSwitchButton();
        if (!this.isTutorialOpened()) {
            this.updateVideoHdrCondition(this.mCapturingModeWhenLastSetupHeadDisplay, (VideoHdr)this.mStateMachine.getUserSetting().get(this.mCapturingModeWhenLastSetupHeadDisplay, UserSettingKey.VIDEO_HDR), true);
            this.changeToLayoutWithSetupState(headUpDisplaySetupState);
        }
        final Handler handler = this.getBaseLayout().getRootView().getHandler();
        if (handler != null) {
            handler.post((Runnable)this.mPostUiInflatedTask);
        }
        if (!this.isCameraSwitching()) {
            this.mStateMachine.sendStaticEvent(StaticEvent.EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED, headUpDisplaySetupState);
        }
        this.clearPreInflatedViews();
        this.mIsSetupHeadupDisplayInvoked = true;
        this.mCanFocusRectanglesBeUpdated = true;
        if (this.mAutoReviewStoreData != null && this.mAutoReviewStoreData.savingRequest.common.takenByFastCapture && this.mAutoReviewStoreData.isSuccess()) {
            if (CamLog.DEBUG) {
                CamLog.d("Pending Auto review is shown when ViewFinder is ready.");
            }
            this.showAutoReview(this.mAutoReviewStoreData);
            this.mAutoReviewStoreData = null;
        }
        this.setIsCameraSwitching(false);
        PerfLog.VIEWFINDER_SETUP_HEADUP_DISPLAY.end();
    }
    
    private void setupHintText() {
        if (this.mHintText == null) {
            final FrameLayout hintTextViewContainer = this.getBaseLayout().getHintTextViewContainer();
            if (hintTextViewContainer != null) {
                this.mHintText = new HintTextViewController((ViewGroup)hintTextViewContainer, (HintTextViewController.HintTextContentListener)new HintTextListenerImpl(), this.mScreenAspect);
                this.updateHintTextUiOrientation();
            }
        }
    }
    
    private void setupInstantViewer() {
        if (this.mInstantViewer != null && this.mInstantViewer.getParent() != null) {
            return;
        }
        if (this.isInflated()) {
            (this.mInstantViewer = (InstantViewer)this.getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.AUTO_REVIEW).get(0)).setup(this.mActivity.getStoredSettings().getUserSettings());
        }
        if (this.mInstantViewer == null) {
            (this.mInstantViewer = (InstantViewer)LayoutInflater.from((Context)this.getActivity()).inflate(2131492936, (ViewGroup)null)).setup(this.mActivity.getStoredSettings().getUserSettings());
        }
        this.getActivity().getWindow().addContentView((View)this.mInstantViewer, (ViewGroup$LayoutParams)new WindowManager$LayoutParams(-1, -1));
    }
    
    private void setupMruButton(final CapturingMode capturingMode) {
        if (this.mActivity.isOneShot()) {
            return;
        }
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4: {
                if (this.mModeLoader == null) {
                    this.mModeLoader = new ModeLoader((Context)this.mActivity);
                }
                this.getBaseLayout().getMruButtonContainer().setup(this.mModeLoader);
                break;
            }
        }
    }
    
    private void setupOnScreenCaptureButton(final HeadUpDisplaySetupState headUpDisplaySetupState) {
        this.changeScreenButtonImage(headUpDisplaySetupState, false);
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        if (this.mImageQualityControlButtonItem == null) {
            this.mImageQualityControlButtonItem = OnScreenButtonItemFactory.createMutableButton(new OnScreenImageQualityControlButtonListener());
            this.mImageQualityControlButtonItem.update().background(2131231528).commit();
        }
        if (this.mHighSensitivityFusionButtonItem == null) {
            this.mHighSensitivityFusionButtonItem = OnScreenButtonItemFactory.createMutableButton(new OnHighSensitivityFusionButtonStateListener());
            this.mHighSensitivityFusionButtonItem.update().background(2131231528).commit();
        }
        this.updateSecondaryShortcutOnScreenButtonResource();
    }
    
    private void setupOnScreenShortcut() {
        this.setMruAvailability(false);
        if (this.mActivity.isOneShot()) {
            this.getBaseLayout().getModeButtonShortcut().set(false);
        }
        else {
            this.getBaseLayout().getModeButtonShortcut().set(true);
            this.getBaseLayout().getModeButtonShortcut().setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
                final ViewFinderImpl this$0;
                
                public void onClick(final View view) {
                    if (this.this$0.mIsSettingChangeAcceptable && this.this$0.isUserOperable()) {
                        if (!ModeSelectorInternalMode.exists(this.this$0.getCapturingMode()) && !this.this$0.getCapturingMode().equals(CapturingMode.FRONT_PHOTO)) {
                            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_OPENED, UiComponentKind.MODE_SELECTOR);
                        }
                        else {
                            this.this$0.startReturnModeAnimation();
                        }
                    }
                }
            });
            this.getBaseLayout().getMruButtonContainer().setOnModeSelectListener(this.mModeSelectListener);
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.getBaseLayout().getMruButtonContainer().getLayoutParams();
            layoutParams.rightMargin = this.getBaseLayout().calculateCaptureButtonAreaHeight();
            this.getBaseLayout().getMruButtonContainer().setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
    }
    
    private void setupPrimaryShortcutIcons() {
        if (this.mPrimaryShortcutGroup == null) {
            (this.mPrimaryShortcutGroup = this.getBaseLayout().getPrimaryShortcut()).setViewFinderAccessor(new ViewFinderAccessorForShortcut());
        }
        if (!this.isCameraSwitching()) {
            this.updatePrimaryShortcutIcons();
        }
    }
    
    private void setupRightIndicatorArea() {
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.mActivity.findViewById(2131296445).getLayoutParams();
        layoutParams.rightMargin = this.mBaseLayout.calculateCaptureButtonAreaHeight();
        this.mActivity.findViewById(2131296445).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    private void setupSelfTimerCountDownView() {
        if (this.mPhotoSelfTimerSetting == null) {
            return;
        }
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[this.mPhotoSelfTimerSetting.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewFinderImpl:setupSelfTimerCountDownView [Irregular value] : ");
                sb.append(this.mPhotoSelfTimerSetting);
                throw new IllegalArgumentException(sb.toString());
            }
            case 5: {
                this.removeSelfTimerCountDownView();
                break;
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                this.createSelfTimerCountDownView(this.mPhotoSelfTimerSetting);
                break;
            }
        }
    }
    
    private void setupSettingUi() {
        if (this.mSettingDialogStack == null) {
            (this.mSettingDialogStack = new SettingDialogStack((Context)this.mActivity, (ViewGroup)this.mActivity.findViewById(2131296599), this.getBaseLayout().getViewFinderRect())).addDialogListener(new SettingDialogListenerImpl());
            this.mSettingDialogStack.addDialogListener(this.getBaseLayout().getPrimaryShortcut());
            if (this.mSettingMenuExclusiveListener == null) {
                this.mSettingMenuExclusiveListener = new SettingMenuExclusiveListener();
                this.mSettingDialogStack.setExclusiveViewListener((SettingDialogStack.ExclusiveViewListener)this.mSettingMenuExclusiveListener);
            }
        }
        if (this.mSettingUi == null) {
            this.mSettingUi = new SettingUi(this.mActivity, this.mSettingDialogStack, this.mStateMachine, this, this.mCameraDevice, this.mActivity.isDeviceInSecurityLock());
        }
        else {
            this.mSettingUi.setDeviceInSecurityLock(this.mActivity.isDeviceInSecurityLock());
        }
        this.mSettingDialogStack.setCapturingMode((CapturingMode)this.mStateMachine.getUserSetting().get(UserSettingKey.CAPTURING_MODE));
        final Iterator<Runnable> iterator = this.mDelayUpdatedViewTaskList.iterator();
        while (iterator.hasNext()) {
            CameraApplication.getUiThreadHandler().post((Runnable)iterator.next());
        }
        this.mDelayUpdatedViewTaskList.clear();
    }
    
    private void setupSideTouchUI() {
        (this.mSideTouchUi = new SideTouchUi((ViewGroup)this.mActivity.findViewById(2131296611), this.mActivity.isOneShot())).setUiOrientation(this.getOrientation());
        this.mSideTouchUi.setZoomBarUpdateProxy(this.mZoomBarProxy);
        this.mSideTouchUi.setRecordingTimeReceiverProxy(this.mRecordingTimeProxy);
        this.mSideTouchUi.setAutoReviewProxy(this.mAutoReviewProxy);
        this.mSideTouchUi.setScreenButtonListenerFactory(new SideTouchUiButtonListenerFactory());
    }
    
    private void setupTransitionAnimationController(final CameraActivity cameraActivity, final HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (this.mAnimationController == null) {
            this.mAnimationController = new TransitionAnimationController(this.mApplicationNavigator, this.getBaseLayout().getPrimaryShortcut().getAllPrimaryShortcutView(), cameraActivity.findViewById(2131296632), cameraActivity.findViewById(2131296396), cameraActivity.findViewById(2131296460), cameraActivity.findViewById(2131296433), this.getBaseLayout().getGridLineView(), (View)this.getBaseLayout().getModeButtonShortcut(), (View)this.getBaseLayout().getMruButtonContainer(), cameraActivity.findViewById(2131296370), (View)this.getBaseLayout().getFrontAngleSwitchButton(), this.mBaseLayout.getSwitchAnimationView());
        }
        this.mAnimationController.resume();
        if (this.mPreviewCover == null) {
            this.mPreviewCover = this.getActivity().findViewById(2131296433);
        }
        this.mPreviewCover.setAlpha(0.0f);
        this.mPreviewCover.setVisibility(0);
    }
    
    private RelativeLayout setupViewFinderLayout() {
        RelativeLayout relativeLayout;
        if (this.isInflated()) {
            relativeLayout = (RelativeLayout)this.getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.FAST_CAPTURING_VIEWFINDER_ITEMS).get(0);
        }
        else {
            relativeLayout = null;
        }
        RelativeLayout relativeLayout2 = relativeLayout;
        if (relativeLayout == null) {
            relativeLayout2 = (RelativeLayout)LayoutInflater.from((Context)this.mActivity).inflate(2131492925, (ViewGroup)null);
        }
        return relativeLayout2;
    }
    
    private void showApplicationNavigator() {
        if (this.mApplicationNavigator != null) {
            this.mApplicationNavigator.show();
        }
    }
    
    private void showAutoReview(final StoreDataResult storeDataResult) {
        if (this.mActivity.isOneShot()) {
            return;
        }
        Uri mUri = Uri.EMPTY;
        int n = AutoReview.ALWAYS.getDuration();
        final int n2 = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[storeDataResult.savingRequest.common.savedFileType.ordinal()];
        boolean mIsPhoto = true;
        byte[] mData = null;
        Label_0241: {
            switch (n2) {
                case 4: {
                    if (this.mSideTouchUi.containsIn(SideTouchUi.Type.AUTO_REVIEW)) {
                        mUri = storeDataResult.uri;
                        this.mSideTouchUi.setUiOrientation(this.mRecordingOrientation);
                        break;
                    }
                    break;
                }
                case 3: {
                    if (this.isPreviewLayout(this.getCurrentLayoutPattern()) && this.isSetupHeadupDisplayInvoked()) {
                        if (this.getBaseLayout() != null) {
                            final PhotoSavingRequest photoSavingRequest = (PhotoSavingRequest)storeDataResult.savingRequest;
                            if (this.mSideTouchUi.containsIn(SideTouchUi.Type.AUTO_REVIEW)) {
                                mData = photoSavingRequest.getImageData();
                                mUri = storeDataResult.uri;
                                break Label_0241;
                            }
                            if (this.isAutoReviewEnabled()) {
                                mData = photoSavingRequest.getImageData();
                                mUri = storeDataResult.uri;
                                n = ((AutoReview)this.mStateMachine.getUserSetting().get(UserSettingKey.AUTO_REVIEW)).getDuration();
                                break Label_0241;
                            }
                        }
                    }
                    mData = null;
                    break Label_0241;
                }
            }
            mData = null;
            mIsPhoto = false;
        }
        if ((mUri == null || Uri.EMPTY.equals((Object)mUri)) && mData == null) {
            return;
        }
        final AutoReviewContent autoReviewContent = new AutoReviewContent();
        autoReviewContent.mUri = mUri;
        autoReviewContent.mData = mData;
        autoReviewContent.mIsPhoto = mIsPhoto;
        autoReviewContent.mIsReverse = this.isNecessaryToReverseForAutoReview(storeDataResult);
        autoReviewContent.mDuration = n;
        autoReviewContent.mEventListener = new OnAutoReviewEventListenerImpl();
        autoReviewContent.mClickListener = (View$OnClickListener)new View$OnClickListener(this, storeDataResult) {
            final ViewFinderImpl this$0;
            final StoreDataResult val$result;
            
            public void onClick(final View view) {
                if (this.this$0.mIsFrontAngleChanging) {
                    return;
                }
                this.this$0.clickAutoReview(this.val$result);
            }
        };
        this.mAutoReviewProxy.notifyContent(autoReviewContent);
    }
    
    private void showBurstRejectedMessage(final BurstRejectedReason burstRejectedReason) {
        Enum<HintTextTimedOutMessage.MessageType> enum1 = null;
        Label_0109: {
            switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$BurstRejectedReason[burstRejectedReason.ordinal()]) {
                case 4: {
                    if (!this.mHintBurstChangeCameraKeySettingAlreadyDisplayed) {
                        this.mHintBurstChangeCameraKeySettingAlreadyDisplayed = true;
                        enum1 = HintTextTimedOutMessage.MessageType.BURST_CHANGE_CAMERA_KEY_SETTING;
                        break Label_0109;
                    }
                    break;
                }
                case 3: {
                    if (!this.mHintCannotBurstUsingFusionModeAlreadyDisplayed) {
                        this.mHintCannotBurstUsingFusionModeAlreadyDisplayed = true;
                        enum1 = HintTextTimedOutMessage.MessageType.CANNOT_BURST_DUE_TO_FUSION_MODE;
                        break Label_0109;
                    }
                    break;
                }
                case 2: {
                    if (!this.mHintCannotBurstUsingFrontCameraAlreadyDisplayed) {
                        this.mHintCannotBurstUsingFrontCameraAlreadyDisplayed = true;
                        enum1 = HintTextTimedOutMessage.MessageType.CANNOT_BURST_USING_FRONT_CAMERA;
                        break Label_0109;
                    }
                    break;
                }
                case 1: {
                    enum1 = HintTextTimedOutMessage.MessageType.CANNOT_BURST_IN_DARK_CONDITION;
                    break Label_0109;
                }
            }
            enum1 = null;
        }
        if (enum1 != null) {
            this.postHintText(new HintTextTimedOutMessage((HintTextTimedOutMessage.MessageType)enum1));
        }
    }
    
    private void showHiSpeedSdCardRecommendDialogOnVideoSizeChange() {
        final VideoSize videoSize = (VideoSize)this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SIZE);
        if (this.isNeedToShowHiSpeedSdCardRecommendation() && videoSize.is4KVideo()) {
            this.showMessageDialog(DialogId.FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_VIDEOSIZE_CHANGE, new Object[0]);
        }
    }
    
    private void showHintTextIfNeeded() {
        if (this.isOverlayControlEnabled() && !this.mStateMachine.isRecording()) {
            return;
        }
        if (this.mHintText != null) {
            this.mHintText.showAll();
        }
    }
    
    private void showInstantViewer(Uri uri, final String s, final int n, final int n2, final int n3, final int n4) {
        this.hideApplicationNavigator();
        this.hideAutoReview();
        if (this.mInstantViewer.isAlbumBitmapSetting()) {
            uri = null;
        }
        this.mInstantViewer.open(uri, s, 0, n3, this.isFront(), new ReviewWindowListenerImpl(), n4);
    }
    
    private void showMruButtonContainer() {
        if (this.getBaseLayout().getMruButtonContainer() != null) {
            this.getBaseLayout().getMruButtonContainer().show();
        }
    }
    
    private void showPhotoSmileCaptureIndicator() {
        this.getBaseLayout().getPhotoSmileCaptureIndicator().show();
    }
    
    private void showSelfTimerCountDownView() {
        final Size sizeAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getSizeAccordingToLayoutOrientation(new Size(this.getBaseLayout().getPreview().getWidth(), this.getBaseLayout().getPreview().getHeight()));
        final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(sizeAccordingToLayoutOrientation.getWidth(), sizeAccordingToLayoutOrientation.getHeight());
        layoutParams.addRule(13);
        this.removeSelfTimerCountDownView();
        (this.mSelfTimerCountDownView = this.mSelfTimerCountDownViewNext).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.mSelfTimerCountDownView.setVisibility(0);
        this.getBaseLayout().getLazyInflatedUiComponentContainerBack().addView((View)this.mSelfTimerCountDownView);
        this.getBaseLayout().getLazyInflatedUiComponentContainerBack().bringChildToFront((View)this.mSelfTimerCountDownView);
        if (LayoutDependencyResolver.isTenInch((Context)this.getActivity())) {
            final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.mSelfTimerCountDownView.getLayoutParams();
            layoutParams2.gravity = 17;
            this.mSelfTimerCountDownView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        }
        this.applySmileFocusThreshold(false);
    }
    
    private void showSuperSlowMotionVideoRecordingHintText() {
        if (this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.SUPER_SLOW_MOTION) {
            this.mHintText.cancel(HintTextSuperSlowMotionVideoRecording.createTag(true));
            this.postHintText(new HintTextSuperSlowMotionVideoRecording(false));
        }
    }
    
    private void showToastMessage(final ToastContent.ToastID toastID) {
        if (!this.mIsSetupHeadupDisplayInvoked) {
            LayoutDependencyResolver.setupRotatableToast(this.mActivity);
            this.mToastContent.setSensorOrientation(this.mActivity.getOrientation());
        }
        this.mToastContent.show(this.mActivity, toastID);
    }
    
    private void showVideoSmileCaptureIndicator() {
        this.getBaseLayout().getVideoSmileCaptureIndicator().show();
    }
    
    private void startCaptureFeedbackAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("startCaptureFeedbackAnimation()");
        }
        if (this.mCaptureFeedback != null) {
            this.mCaptureFeedback.start(CaptureFeedbackAnimationFactory.createDefaultAnimation());
        }
    }
    
    private void startDraggingSwitchAnimation(final float alpha) {
        if (this.mAnimationController.startSwitchDraggingAnimation(alpha)) {
            this.mPreviewCover.setAlpha(alpha);
        }
    }
    
    private boolean startDraggingSwitchStartedAnimation() {
        if (this.mAnimationController.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.START, this.getCapturingMode(), this.getCapturingMode()))) {
            this.mBaseLayout.computeRadiusOfAnimation();
            return true;
        }
        return false;
    }
    
    private void startInflateTask(final LayoutInflater layoutInflater, final List<InflateItem> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("startInflateTask in");
        }
        final ExecutorService buildExecutor = ThreadUtil.buildExecutor("InflateTask");
        this.mInflateFuture = buildExecutor.submit((Callable<Map<InflateItem, List<View>>>)new InflateTask(layoutInflater, list));
        buildExecutor.shutdown();
        if (CamLog.VERBOSE) {
            CamLog.d("startInflateTask out");
        }
    }
    
    private void startModeChangedAnimation(final CapturingMode capturingMode, final CapturingMode capturingMode2, final AnimationRequest.AnimationType animationType) {
        if (this.requestAnimation(new AnimationRequest(animationType, AnimationRequest.AnimationDegree.FINISH, capturingMode, capturingMode2)) && this.mApplicationNavigator != null) {
            this.mApplicationNavigator.resetContentDescriptionForModeName();
        }
    }
    
    private void startSelfTimerCountDownAnimation() {
        if (this.mSelfTimerCountDownView != null) {
            this.mSelfTimerCountDownView.startSelfTimerCountDownAnimation(this.getCapturingMode() == CapturingMode.FRONT_PHOTO || this.getCapturingMode() == CapturingMode.SUPERIOR_FRONT);
        }
    }
    
    private void transitionModeOnNavigator(int n) {
        final NavigatorContents value = NavigatorContents.valueOf(this.getCapturingMode());
        final int index = NavigatorContents.indexOf(value);
        n = NavigatorContents.values().length - n - 1;
        final NavigatorContents navigatorContents = NavigatorContents.values()[n];
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke current:");
            sb.append(value.name());
            sb.append(", target:");
            sb.append(navigatorContents.name());
            CamLog.d(sb.toString());
        }
        NavigatorContents navigatorContents2;
        if (n > index) {
            navigatorContents2 = value.next();
        }
        else {
            if (n >= index) {
                return;
            }
            navigatorContents2 = value.previous();
        }
        final AnimationRequest animationRequest = new AnimationRequest(AnimationRequest.AnimationType.MODE_ICON, AnimationRequest.AnimationDegree.START, getCapturingMode(value, this.getCapturingMode()), getCapturingMode(navigatorContents2, this.getCapturingMode()));
        if (this.requestAnimation(animationRequest)) {
            this.mStateMachine.sendEvent(TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest);
        }
    }
    
    private void updateAllOverlayControlVisibility() {
        this.updateOverlayControlVisibility(this.getBaseLayout().getImageQualityControl());
        this.updateOverlayControlVisibility(this.getBaseLayout().getSemiAutoControl());
    }
    
    private void updateFrontAngleSwitchButton() {
        this.mFrontAngleSwitchButton = this.getBaseLayout().getFrontAngleSwitchButton();
        if (this.mFrontAngleSwitchButton == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("updateFrontAngleSwitchButton mFrontAngleSwitchButton is NULL");
            }
            return;
        }
        if (this.getCapturingMode().isFront()) {
            final FrontAngle obj = (FrontAngle)this.mStateMachine.getUserSetting().get(UserSettingKey.FRONT_ANGLE);
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("updateFrontAngleSwitchButton value: ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            this.mFrontAngleSwitchButton.switchFrontAngle(obj);
        }
    }
    
    private void updateFusionHintText(@Nullable final CameraParameters.FusionResult fusionResult) {
        if (this.mHintText == null) {
            return;
        }
        if (PlatformCapability.isHighSensitivityFusionSupported(this.getCapturingMode().getCameraId())) {
            this.mHintText.cancel(HintTextHighSensitivityFusionStatus.class.getSimpleName());
            this.mHintText.cancel(HintTextHighSensitivityFusionCondition.class.getSimpleName());
            if (fusionResult != null) {
                if (fusionResult.getFusionCondition() == CameraParameters.FusionCondition.CLOSE_TO_SUBJECT) {
                    this.postHintText(new HintTextHighSensitivityFusionCondition());
                }
                else if (fusionResult.getFusionStatus() == CameraParameters.FusionStatus.FUSION_SUB_1) {
                    this.postHintText(new HintTextHighSensitivityFusionStatus());
                }
                else {
                    this.updateVisibilityForSpecificDisplaySize();
                }
            }
        }
    }
    
    private void updateGeotagIcon() {
        if (this.getBaseLayout().getGeoTagIndicator() != null && this.mActivity != null && this.needToShowGeoTagIndicator()) {
            this.getBaseLayout().getGeoTagIndicator().set(GeotagManager.isGeoTagEnabled(this.mActivity.getStoredSettings().getUserSettings(), (Context)this.mActivity));
        }
    }
    
    private void updateGridLineView() {
        this.updateGridLineView(null);
    }
    
    private void updateGridLineView(final CapturingMode capturingMode) {
        if (this.isHeadUpDisplayReady() && this.mBaseLayout.getGridLineView() != null) {
            final UserSettings userSetting = this.mStateMachine.getUserSetting();
            CapturingMode capturingMode2;
            if ((capturingMode2 = capturingMode) == null) {
                capturingMode2 = (CapturingMode)userSetting.get(UserSettingKey.CAPTURING_MODE);
            }
            final Size computeGridSize = this.computeGridSize(capturingMode2, userSetting);
            this.mBaseLayout.updateGridLine(computeGridSize.getWidth(), computeGridSize.getHeight());
            final GridLine gridLine = (GridLine)userSetting.get(UserSettingKey.GRID_LINE);
            final boolean predictiveLaunchCoverExists = this.predictiveLaunchCoverExists();
            boolean gridLineViewEnabled = false;
            if (predictiveLaunchCoverExists) {
                this.mBaseLayout.setGridLineViewEnabled(false);
            }
            else {
                final BaseLayout mBaseLayout = this.mBaseLayout;
                if (gridLine == GridLine.ON) {
                    gridLineViewEnabled = true;
                }
                mBaseLayout.setGridLineViewEnabled(gridLineViewEnabled);
            }
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("called updateGridLineView before ready");
        }
    }
    
    private void updateHighSensitivityFusionModeForManual() {
        if (UserSettingKey.FUSION_MODE.isSelectable()) {
            final FusionMode fusionMode = (FusionMode)this.mStateMachine.getUserSetting().get(UserSettingKey.FUSION_MODE);
            final StateMachine mStateMachine = this.mStateMachine;
            final StateMachine.TransitterEvent event_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE = TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE;
            FusionMode fusionMode2;
            if (fusionMode == FusionMode.OFF) {
                fusionMode2 = FusionMode.ON;
            }
            else {
                fusionMode2 = FusionMode.OFF;
            }
            mStateMachine.sendEvent(event_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE, fusionMode2);
        }
        else {
            this.showMessageDialog(UserSettingKey.FUSION_MODE.getRestrictMessageDialogId(this.mStateMachine.getUserSetting()), new Object[0]);
        }
    }
    
    private void updateHintTextContainer(Rect rect) {
        rect = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(rect);
        rect = LayoutDependencyResolver.getSurfaceViewRect((Context)this.mActivity, rect.width() / (float)rect.height(), this.mScreenAspect);
        this.mHintText.updateHintTextContainer(rect.width(), rect.height());
        this.mHintText.setUiOrientation(rect, (Context)this.mActivity, this.mScreenAspect, this.mOrientation);
    }
    
    private void updateHintTextUiOrientation() {
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mEvf.asView().getLayoutParams();
        this.mHintText.setUiOrientation(new Rect(0, 0, frameLayout$LayoutParams.width, frameLayout$LayoutParams.height), (Context)this.mActivity, this.mScreenAspect, this.mOrientation);
        this.updateVisibilityForSpecificDisplaySize();
    }
    
    private void updateIndicatorState() {
        if (this.needToShowGeoTagIndicator()) {
            if (GeotagManager.isGeoTagEnabled(this.mActivity.getStoredSettings().getUserSettings(), (Context)this.mActivity)) {
                if (this.mActivity.getGeoTagManager() != null) {
                    final boolean acquiring = this.mActivity.getGeoTagManager().isAcquiring();
                    this.mBaseLayout.getGeoTagIndicator().set(true);
                    this.mBaseLayout.getGeoTagIndicator().isAcquired(acquiring ^ true);
                }
            }
            else {
                this.mBaseLayout.getGeoTagIndicator().set(false);
            }
        }
        this.updateLowMemoryIndicator();
        this.updateThermalIndicator();
        this.updateBatteryIndicator(this.mActivity.getBatteryLevel());
    }
    
    private void updateLocation() {
        this.mActivity.getGeoTagManager().updateLocation(Geotag.OFF);
        this.mStateMachine.getUserSetting().set(Geotag.OFF);
    }
    
    private void updateLowMemoryIndicator() {
        this.mBaseLayout.getLowMemoryInternalIndicator().set(this.hasEnoughFreeSpace(Storage.StorageType.INTERNAL) ^ true);
        this.mBaseLayout.getLowMemorySdIndicator().set(this.hasEnoughFreeSpace(Storage.StorageType.EXTERNAL_CARD) ^ true);
    }
    
    private void updateOverlayControlVisibility(final BaseLayout.LazyInitializer<OverlayControl> lazyInitializer) {
        if (lazyInitializer.isInitialized()) {
            if (this.isAutoReviewShowing() || this.mIsAutoReviewRequested) {
                lazyInitializer.get().hide();
                return;
            }
            if (!this.isPreviewLayout(this.getCurrentLayoutPattern()) && this.getCurrentLayoutPattern() != BaseLayoutPattern.OVERLAY_CONTROL_SEEKING) {
                lazyInitializer.get().hide();
            }
            else {
                lazyInitializer.get().show();
            }
        }
    }
    
    private void updatePreviewContainer(final int n, final int n2) {
        this.mBaseLayout.updatePreviewContainer(n, n2);
    }
    
    private void updatePrimaryShortcutIcon(final UserSettingValue userSettingValue) {
        if (this.mPrimaryShortcutGroup != null) {
            this.mPrimaryShortcutGroup.updatePrimaryShortcutIcon(userSettingValue.getKey(), userSettingValue.getIconId());
        }
    }
    
    private void updatePrimaryShortcutIcons() {
        if (this.mPrimaryShortcutGroup != null) {
            this.mPrimaryShortcutGroup.updatePrimaryShortcutIcons(this.getCapturingMode(), this.mStateMachine.getUserSetting(), this.mActivity.isOneShot());
        }
    }
    
    private void updateScreenButtonImage(final CapturingMode capturingMode) {
        final HeadUpDisplaySetupState photo_READY = HeadUpDisplaySetupState.PHOTO_READY;
        Enum<HeadUpDisplaySetupState> enum1 = null;
        Label_0153: {
            switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
                default: {
                    enum1 = photo_READY;
                    break;
                }
                case 7: {
                    switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                        default: {
                            enum1 = photo_READY;
                            break Label_0153;
                        }
                        case 3: {
                            enum1 = HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY;
                            break Label_0153;
                        }
                        case 2: {
                            enum1 = HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY;
                            break Label_0153;
                        }
                        case 1: {
                            enum1 = HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY;
                            break Label_0153;
                        }
                    }
                    break;
                }
                case 3:
                case 4: {
                    enum1 = HeadUpDisplaySetupState.VIDEO_READY;
                    break;
                }
                case 1:
                case 2:
                case 5:
                case 6: {
                    enum1 = HeadUpDisplaySetupState.PHOTO_READY;
                    break;
                }
            }
        }
        this.changeScreenButtonImage((HeadUpDisplaySetupState)enum1, false);
    }
    
    private void updateSecondaryShortcutOnScreenButtonResource() {
        if (this.getBaseLayout() != null && this.getBaseLayout().getOnScreenButtonGroup() != null) {
            final CapturingMode capturingMode = this.getCapturingMode();
            final CapturingMode normal = CapturingMode.NORMAL;
            final int n = 1;
            if ((capturingMode == normal || capturingMode == CapturingMode.FRONT_PHOTO) && this.mImageQualityControlButtonItem != null) {
                final StringBuilder sb = new StringBuilder();
                final Iterator<UserSettingKey> iterator = ImageQualityControl.KEYS.iterator();
                int n2 = 0;
                while (iterator.hasNext()) {
                    final UserSettingKey userSettingKey = iterator.next();
                    final UserSettingValue imageQualityControlDefaultValue = SettingUi.getImageQualityControlDefaultValue(userSettingKey);
                    int n3 = n2;
                    if (this.mStateMachine.getUserSetting().get(userSettingKey) != imageQualityControlDefaultValue) {
                        n3 = 1;
                    }
                    final int n4 = -1;
                    n2 = n3;
                    if (userSettingKey.isSelectable()) {
                        int n5;
                        if (capturingMode == CapturingMode.NORMAL) {
                            n5 = SettingUi.getImageQualityControlTabDescription(userSettingKey);
                        }
                        else {
                            n5 = n4;
                            if (capturingMode == CapturingMode.FRONT_PHOTO) {
                                switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()]) {
                                    default: {
                                        n5 = n4;
                                        break;
                                    }
                                    case 6:
                                    case 7: {
                                        n5 = SettingUi.getImageQualityControlTabDescription(userSettingKey);
                                        break;
                                    }
                                }
                            }
                        }
                        final String string = this.getString(n5);
                        n2 = n3;
                        if (TextUtils.isEmpty((CharSequence)string)) {
                            continue;
                        }
                        sb.append(" ");
                        sb.append(string);
                        n2 = n3;
                    }
                }
                int n6;
                if (n2 != 0) {
                    n6 = 2131231032;
                }
                else {
                    n6 = 2131231031;
                }
                this.mImageQualityControlButtonItem.update().icon(n6).text(sb.toString()).commit();
            }
            if (capturingMode == CapturingMode.NORMAL && this.mHighSensitivityFusionButtonItem != null) {
                int n7;
                if (this.mStateMachine.getUserSetting().get(UserSettingKey.FUSION_MODE) == FusionMode.ON) {
                    n7 = n;
                }
                else {
                    n7 = 0;
                }
                int n8;
                if (n7 != 0) {
                    n8 = 2131231065;
                }
                else {
                    n8 = 2131231064;
                }
                int n9;
                if (n7 != 0) {
                    n9 = 2131689574;
                }
                else {
                    n9 = 2131689573;
                }
                this.mHighSensitivityFusionButtonItem.update().icon(n8).description(n9).commit();
            }
        }
    }
    
    private void updateThermalHintTextMessage(final CapturingMode capturingMode) {
        if (this.mHintText != null && capturingMode != CapturingMode.SLOW_MOTION) {
            this.postHintText(new HintTextThermalWarning());
        }
    }
    
    private void updateThermalIndicator() {
        this.mBaseLayout.getThermalIndicator().set(this.mActivity.isThermalWarningState());
    }
    
    private void updateUiComponent(final UiComponentKind uiComponentKind) {
        this.changeToDialogView(uiComponentKind);
    }
    
    private void updateVideoHdrCondition(final CapturingMode capturingMode, final VideoHdr obj, final boolean b) {
        final boolean verbose = CamLog.VERBOSE;
        boolean b2 = true;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("updateVideoHdrCondition : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (PlatformCapability.isVideoHdrSupported(capturingMode.getCameraId()) && capturingMode.isVideo() && capturingMode != CapturingMode.SLOW_MOTION && !this.mActivity.isOneShotVideo()) {
            final boolean selectable = UserSettingKey.VIDEO_HDR.isSelectable();
            if (obj != VideoHdr.HDR_ON) {
                b2 = false;
            }
            final VideoSize videoSize = (VideoSize)this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SIZE);
            final VideoSize full_HD = VideoSize.FULL_HD;
            if (b2 && selectable) {
                if (this.mFocusRectangles != null) {
                    this.mFocusRectangles.clearAllFocus();
                }
                if (b && !this.mMessageDialog.isOpened()) {
                    this.showMessageDialog(DialogId.VIDEO_HDR_CAUTION, new Object[0]);
                }
            }
        }
    }
    
    private void updateVisibilityForSpecificDisplaySize() {
        if (this.isInLargerOrMoreDisplaySizeOr16_9Device()) {
            final BaseLayout.LazyInitializer<OverlayControl> semiAutoControl = this.getBaseLayout().getSemiAutoControl();
            if (semiAutoControl.isInitialized() && (semiAutoControl.get().isVisible() || (semiAutoControl.get().isEnabled() && (this.mIsAutoReviewRequested || this.isAutoReviewShowing())))) {
                this.hideApplicationNavigator();
                this.hideMruButtonContainer();
                return;
            }
            if (this.mHintText != null && this.mHintText.isNoTimeOutHinTextDisplayed() && this.mOrientation == 1 && this.getCurrentLayoutPattern() != BaseLayoutPattern.MODE_CHANGING) {
                this.hideApplicationNavigator();
                this.hideMruButtonContainer();
                return;
            }
        }
        if (this.isPreviewLayout(this.getCurrentLayoutPattern()) || this.getCurrentLayoutPattern() == BaseLayoutPattern.MODE_CHANGING) {
            this.showApplicationNavigator();
            this.showMruButtonContainer();
        }
    }
    
    @Override
    public void attachToWindow() {
        if (this.getBaseLayout() != null) {
            this.getBaseLayout().attachToWindow();
        }
    }
    
    public boolean canFocusRectanglesBeUpdated() {
        return this.mCanFocusRectanglesBeUpdated;
    }
    
    @Override
    public void cancelPredictiveCaptureIndicatorAnimation() {
        if (this.getBaseLayout().getPredictiveCaptureIndicatorController() != null) {
            this.getBaseLayout().getPredictiveCaptureIndicatorController().cancelAnimation();
        }
    }
    
    public void clearBurstShootingRejectedReason() {
        this.mBurstShootingRejectedReason = BurstRejectedReason.NONE;
    }
    
    public void clearCanceledSideTouchEventIcons() {
        if (!this.isSetupHeadupDisplayInvoked()) {
            return;
        }
        if (this.mSideTouchUi.containsIn(SideTouchUi.Type.CAPTURE_COUNTDOWN, SideTouchUi.Type.ZOOM_BAR)) {
            this.mSideTouchUi.destroyIcon();
        }
    }
    
    @Override
    public void clearHintText() {
        if (this.mHintText != null) {
            this.mHintText.clearAll();
        }
    }
    
    @Override
    public void clearMessageDialog() {
        this.mMessageDialog.clear();
    }
    
    public void clearTouchedScreenButtonGroup() {
        this.mBaseLayout.getOnScreenButtonGroup().clearTouched();
    }
    
    protected boolean closeAutoReviewIfShowing() {
        if (this.isAutoReviewShowing()) {
            this.hideAutoReview();
            return true;
        }
        return false;
    }
    
    public void closeDialogs() {
        if (this.mSettingUi != null) {
            this.mSettingUi.closeDialogs();
        }
    }
    
    protected boolean closeOverlayControlIfOpened() {
        if (this.isOverlayControlVisible()) {
            this.disableSemiAutoControl();
            this.disableOverlayControl(this.getBaseLayout().getImageQualityControl());
            return true;
        }
        return false;
    }
    
    protected boolean closeSettingDialogIfOpened() {
        return this.mSettingDialogStack != null && this.mSettingDialogStack.closeCurrentDialog();
    }
    
    @Override
    public void commit() {
        HeadUpDisplaySetupState headUpDisplaySetupState = HeadUpDisplaySetupState.PHOTO_READY;
        Label_0210: {
            switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.getCapturingMode().ordinal()]) {
                default: {
                    headUpDisplaySetupState = HeadUpDisplaySetupState.PHOTO_READY;
                    break;
                }
                case 7: {
                    switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                        default: {
                            break Label_0210;
                        }
                        case 3: {
                            if (this.isRecording()) {
                                headUpDisplaySetupState = HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_RECORDING;
                                break Label_0210;
                            }
                            headUpDisplaySetupState = HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY;
                            break Label_0210;
                        }
                        case 2: {
                            headUpDisplaySetupState = HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY;
                            break Label_0210;
                        }
                        case 1: {
                            if (this.isRecording()) {
                                headUpDisplaySetupState = HeadUpDisplaySetupState.SUPER_SLOW_MOTION_RECORDING;
                                break Label_0210;
                            }
                            headUpDisplaySetupState = HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY;
                            break Label_0210;
                        }
                    }
                    break;
                }
                case 3:
                case 4: {
                    if (!this.isRecording()) {
                        headUpDisplaySetupState = HeadUpDisplaySetupState.VIDEO_READY;
                        break;
                    }
                    if (this.mLayoutPattern == BaseLayoutPattern.RECORDING) {
                        headUpDisplaySetupState = HeadUpDisplaySetupState.VIDEO_RECORDING;
                        break;
                    }
                    headUpDisplaySetupState = HeadUpDisplaySetupState.VIDEO_PAUSING;
                    break;
                }
                case 1:
                case 2:
                case 5:
                case 6: {
                    headUpDisplaySetupState = HeadUpDisplaySetupState.PHOTO_READY;
                    break;
                }
            }
        }
        if (this.getCurrentLayoutPattern() != BaseLayoutPattern.SELFTIMER) {
            this.changeScreenButtonImage(headUpDisplaySetupState, false);
        }
        this.updateSecondaryShortcutOnScreenButtonResource();
        if (this.mIsSurfaceViewHideWhileAspectChanging) {
            this.mEvf.show();
            this.mIsSurfaceViewHideWhileAspectChanging = false;
        }
    }
    
    public RectF convertTouchPointToRectInDevicePreviewPositionRatio(final Point point) {
        final PointF convertTouchPointToDevicePreviewPositionRatio = this.convertTouchPointToDevicePreviewPositionRatio(point);
        final Rect rect = this.mEvf.getRect();
        final Rect touchFocusIconSize = this.mFocusRectangles.getTouchFocusIconSize();
        final int width = rect.width();
        final int height = rect.height();
        final float n = touchFocusIconSize.width() / (float)width;
        final float n2 = touchFocusIconSize.height() / (float)height;
        final float x = convertTouchPointToDevicePreviewPositionRatio.x;
        final float n3 = n / 2.0f;
        final float y = convertTouchPointToDevicePreviewPositionRatio.y;
        final float n4 = n2 / 2.0f;
        return new RectF(x - n3, y - n4, convertTouchPointToDevicePreviewPositionRatio.x + n3, convertTouchPointToDevicePreviewPositionRatio.y + n4);
    }
    
    @Override
    public int getAutoPowerOffHintTextTimeOutDuration() {
        return 10000;
    }
    
    @Override
    public int getOrientation() {
        return this.getBaseLayout().getCurrentOrientation();
    }
    
    @Override
    public SelfTimer getPhotoSelfTimerSetting() {
        return this.mPhotoSelfTimerSetting;
    }
    
    @Override
    public Rect getPosition(final Point point) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getPosition(x, y) = (");
            sb.append(point.x);
            sb.append(", ");
            sb.append(point.y);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        final Rect rect = new Rect(0, 0, this.getActivity().getResources().getDimensionPixelSize(2131165338), this.getActivity().getResources().getDimensionPixelSize(2131165337));
        final Rect convertPositionToAligned = CoordinateUtil.convertPositionToAligned(point.x, point.y, this.mEvf.getRect(), this.mEvf.getRect(), rect.width(), rect.height());
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("getPosition: ");
            sb2.append(convertPositionToAligned);
            CamLog.d(sb2.toString());
        }
        return convertPositionToAligned;
    }
    
    public ViewGroup$LayoutParams getPreviewLayoutParams() {
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-1, -1, 51);
        if (this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 1) {
                this.mPreviewOrientation = 1;
                frameLayout$LayoutParams.gravity = 49;
                frameLayout$LayoutParams.setMargins(0, ResourceUtil.getDimensionPixelSize((Context)this.mActivity, this.mActivity.getPackageName(), 2131165428), 0, 0);
            }
            else {
                this.mPreviewOrientation = 2;
                frameLayout$LayoutParams.gravity = 19;
                frameLayout$LayoutParams.setMargins(ResourceUtil.getDimensionPixelSize((Context)this.mActivity, this.mActivity.getPackageName(), 2131165428), 0, 0, 0);
            }
        }
        return (ViewGroup$LayoutParams)frameLayout$LayoutParams;
    }
    
    @Override
    public int getRequestId(final boolean b) {
        int i;
        if (this.getBaseLayout().getContentsViewController() != null) {
            this.preparationForInstantViewer();
            if (b) {
                i = this.getBaseLayout().getContentsViewController().createContentFrame();
            }
            else {
                i = this.getBaseLayout().getContentsViewController().createEmptyContentFrame();
            }
        }
        else {
            i = -1;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("New request ID: ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        return i;
    }
    
    @Override
    public int getSelectedFaceSmileScore() {
        if (this.mFocusRectangles == null) {
            return 0;
        }
        return this.mFocusRectangles.getSelectedFaceSmileScore();
    }
    
    public UserEventHandler.TouchEventDispatcher getTouchEventDispatcher() {
        return this.mTouchEventDispatcher;
    }
    
    @Override
    public void hideAutoPowerOffHintText() {
        if (this.mHintText != null) {
            this.mHintText.cancel(HintTextAutoPowerOff.class.getSimpleName());
            this.updateVisibilityForSpecificDisplaySize();
        }
    }
    
    @Override
    public void hideAutoReview() {
        this.mIsAutoReviewRequested = false;
        if (this.getBaseLayout() != null) {
            this.getBaseLayout().hideAutoReview();
        }
        if (this.mSideTouchUi != null) {
            this.mSideTouchUi.destroyTo(SideTouchUi.Type.AUTO_REVIEW);
        }
        else {
            CamLog.e("Hiding the problem (mSideTouchUi = null). Modify the problem correctly.");
        }
        this.updateAllOverlayControlVisibility();
    }
    
    @Override
    public void hideDisplayFlashScreen() {
        if (this.isDisplayFlashScreenDisplayed()) {
            if (this.mWindowDisplayFlashScreen != null) {
                this.mWindowDisplayFlashScreen.setVisibility(8);
            }
            this.mIsDisplayFlashScreenDisplayed = false;
        }
    }
    
    @Override
    public void hideHudIcons() {
        this.changeLayoutTo(BaseLayoutPattern.CAPTURE);
    }
    
    public void hidePredictiveLaunchCover(final PredictiveLaunchHideTrigger predictiveLaunchHideTrigger) {
        if (!this.predictiveLaunchCoverExists()) {
            return;
        }
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$ViewFinderImpl$PredictiveLaunchHideTrigger[predictiveLaunchHideTrigger.ordinal()]) {
            default: {
                VibrationManager.vibrate((Context)this.mActivity, VibrationManager.VibrationPattern.EFFECT_STANDARD);
                break;
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                VibrationManager.vibrate((Context)this.mActivity, VibrationManager.VibrationPattern.EFFECT_FOR_CAPTURE);
                break;
            }
        }
        ResearchUtil.getInstance().sendPredictiveLaunchEvent(predictiveLaunchHideTrigger.mAction);
        this.getBaseLayout().hidePredictiveLaunchCover(new Animatable2$AnimationCallback(this) {
            final ViewFinderImpl this$0;
            
            public void onAnimationEnd(final Drawable drawable) {
                this.this$0.mBaseLayout.releasePredictiveLaunchCover();
                if (!this.this$0.isZooming() && !this.this$0.isFocusing()) {
                    this.this$0.mBaseLayout.setViewFinderGestureDetectorEnabled(true, true);
                }
            }
        });
        this.setApplicationNavigatorEnabled(true);
        this.updateGridLineView();
        this.changeToPhotoReadyView(true);
        this.mActivity.setupAutoPowerOffTimeOutDuration(false);
        this.mActivity.restartAutoPowerOffTimer();
        if (this.mLoopsManager != null && this.mLoopsManager.isConnected()) {
            this.mLoopsManager.disconnect();
        }
        this.mLoopsManager = null;
    }
    
    @Override
    public void hideSavingProgressBar() {
        if (this.mSavingProgressBar != null) {
            this.mSavingProgressBar.setVisibility(8);
        }
    }
    
    @Override
    public void hideSurface() {
        this.mEvf.hide();
    }
    
    @Override
    public void hideViews() {
        this.getBaseLayout().getPhotoSmileCaptureIndicator().hide();
        this.getBaseLayout().getSceneIndicator().hide();
        this.getBaseLayout().getConditionIndicator().hide();
        this.getBaseLayout().getGeoTagIndicator().hide();
        this.getBaseLayout().getLowMemoryInternalIndicator().hide();
        this.getBaseLayout().getLowMemorySdIndicator().hide();
        this.getBaseLayout().getThermalIndicator().hide();
        this.getBaseLayout().getBatteryIndicator().hide();
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        if (this.mFocusRectangles != null) {
            this.mFocusRectangles.onUiComponentOverlaid();
        }
    }
    
    public void initialize() {
        this.mBaseLayout = new BaseLayout(this.mActivity, this.mScreenAspect);
        this.mActivity.addOrienationListener((CameraActivity.LayoutOrientationChangedListener)this);
    }
    
    @Override
    public boolean isAutoPowerOffWarningDisplayed() {
        return this.mHintText != null && this.mHintText.isHintTextDisplayed(HintTextAutoPowerOff.class.getSimpleName());
    }
    
    @Override
    public boolean isAutoReviewShowing() {
        if (this.mSideTouchUi != null) {
            final boolean autoReviewShowing = this.getBaseLayout().isAutoReviewShowing();
            boolean b = false;
            if (autoReviewShowing || this.mSideTouchUi.containsIn(SideTouchUi.Type.AUTO_REVIEW)) {
                b = true;
            }
            return b;
        }
        return this.getBaseLayout().isAutoReviewShowing();
    }
    
    @Override
    public boolean isCameraSwitching() {
        return this.getBaseLayout().isCameraSwitching();
    }
    
    @Override
    public boolean isDisplayFlashScreenDisplayed() {
        return this.mIsDisplayFlashScreenDisplayed;
    }
    
    @Override
    public boolean isEvfPrepared() {
        final StringBuilder sb = new StringBuilder();
        sb.append("isEvfPrepared() : mIsEvfPrepared = ");
        sb.append(this.mIsEvfPrepared);
        CamLog.d(sb.toString());
        return this.mIsEvfPrepared;
    }
    
    @Override
    public boolean isFlashAndSettingMenuOpened() {
        final SettingDialogStack mSettingDialogStack = this.mSettingDialogStack;
        boolean b = false;
        if (mSettingDialogStack != null) {
            if (this.mSettingDialogStack.isShortcutDialogOpened() || this.mSettingDialogStack.isMenuDialogOpened()) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    public boolean isFrontAngleChanging() {
        return this.mIsFrontAngleChanging;
    }
    
    @Override
    public boolean isHeadUpDisplayReady() {
        return this.mBaseLayout != null && this.mBaseLayout.isHeadUpDisplayReady();
    }
    
    @Override
    public boolean isMessageDialogOpened() {
        return this.mMessageDialog.isOpened();
    }
    
    public boolean isPreviewLayout() {
        return this.isPreviewLayout(this.getCurrentLayoutPattern());
    }
    
    public boolean isSelfTimerCountDownViewShown() {
        return this.mSelfTimerCountDownView != null && this.mSelfTimerCountDownView.isShown();
    }
    
    public boolean isSemiAutoEnabled() {
        return this.getBaseLayout().getSemiAutoControl().isInitialized() && this.getBaseLayout().getSemiAutoControl().get().isEnabled();
    }
    
    @Override
    public boolean isSetupHeadupDisplayInvoked() {
        return this.mIsSetupHeadupDisplayInvoked;
    }
    
    @Override
    public boolean isSwitchingAnimationProgress() {
        return this.mIsSwitchingAnimationProgress;
    }
    
    @Override
    public boolean isTouchFocus() {
        return this.mFocusRectangles != null && this.mFocusRectangles.isTouchFocus();
    }
    
    boolean isTutorialOpened() {
        return this.mBaseLayout.getTutorial().isOpened();
    }
    
    @Override
    public boolean isUserOperable() {
        return this.isEvfPrepared() && this.isSetupHeadupDisplayInvoked() && this.isHeadUpDisplayReady() && !this.isFrontAngleChanging() && !this.isCameraSwitching() && !this.isSwitchingAnimationProgress();
    }
    
    @Override
    public void notifyOnEvfPrepared() {
        if (!this.mEvf.asSurface().isValid()) {
            CamLog.d("notifyOnEvfPrepared : Evf does not hold a physical surface yet.");
            return;
        }
        final Size surfaceSize = this.mEvf.getSurfaceSize();
        if (surfaceSize != null) {
            this.notifyOnEvfPrepared(new Rect(0, 0, surfaceSize.getWidth(), surfaceSize.getHeight()));
        }
    }
    
    @Override
    public void notifyStorageStateChanged(final Storage.StorageType obj, final Storage.StorageState obj2, final boolean b, final boolean b2) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onStorageStateChanged: StorageType = ");
            sb.append(obj);
            sb.append(", StorageState = ");
            sb.append(obj2);
            sb.append(", isChangeable = ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        if (obj2 != Storage.StorageState.AVAILABLE && obj2 != Storage.StorageState.AVAILABLE_NEAR_FULL && this.mFocusRectangles != null) {
            this.mFocusRectangles.clearFaceDetection();
        }
        Label_0423: {
            switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageState[obj2.ordinal()]) {
                case 7: {
                    if (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageType[obj.ordinal()] != 1) {
                        break;
                    }
                    this.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE, new Object[0]);
                    break;
                }
                case 4:
                case 5:
                case 6: {
                    if (b) {
                        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageType[obj.ordinal()]) {
                            default: {
                                break Label_0423;
                            }
                            case 2: {
                                this.showMessageDialog(DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD, new Object[0]);
                                break Label_0423;
                            }
                            case 1: {
                                this.showMessageDialog(DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL, new Object[0]);
                                break Label_0423;
                            }
                        }
                    }
                    else {
                        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageType[obj.ordinal()]) {
                            default: {
                                break Label_0423;
                            }
                            case 2: {
                                this.showMessageDialog(DialogId.MEMORY_INTERNAL_UNAVAILABLE, new Object[0]);
                                break Label_0423;
                            }
                            case 1: {
                                this.showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE, new Object[0]);
                                break Label_0423;
                            }
                        }
                    }
                    break;
                }
                case 3: {
                    if (b2) {
                        this.showMessageDialog(DialogId.MEMORY_FULL_IN_BURST_MODE, new Object[0]);
                        break;
                    }
                    if (!b) {
                        this.showMessageDialog(DialogId.MEMORY_FULL, new Object[0]);
                        break;
                    }
                    switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageType[obj.ordinal()]) {
                        default: {
                            break Label_0423;
                        }
                        case 2: {
                            this.showMessageDialog(DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_SD, new Object[0]);
                            break Label_0423;
                        }
                        case 1: {
                            this.showMessageDialog(DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL, new Object[0]);
                            break Label_0423;
                        }
                    }
                    break;
                }
                case 1:
                case 2: {
                    this.mMessageDialog.removeDialogsInList(ViewFinderImpl.STORAGE_DIALOG_LIST);
                    break;
                }
            }
        }
    }
    
    @Override
    public void notifyZoomOperationRejected() {
        if (this.isAllDialogClosed()) {
            this.postHintText(new HintTextTimedOutMessage(HintTextTimedOutMessage.MessageType.ZOOM_NOT_AVAILABLE));
        }
    }
    
    @Override
    public void onCaptureDone() {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.clearTouchedScreenButtonGroup();
        this.mFocusRectangles.onObjectFocused();
    }
    
    protected boolean onHandleBackKeyTutorial() {
        if (this.mBaseLayout != null && this.mBaseLayout.getTutorial() != null) {
            final TutorialController tutorial = this.mBaseLayout.getTutorial();
            if (tutorial.isOpened()) {
                if (tutorial.backToPreviousPage()) {
                    return true;
                }
                final MessageSettings messageSettings = this.mActivity.getStoredSettings().getMessageSettings();
                for (final TutorialController.TutorialType tutorialType : tutorial.getTutorialTypes()) {
                    switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()]) {
                        case 2: {
                            this.updateHighSensitivityFusionModeForManual();
                            break;
                        }
                        case 1: {
                            this.mStateMachine.sendEvent(TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE, FusionMode.AUTO);
                            break;
                        }
                    }
                    final Iterator<MessageType> iterator2 = tutorialType.messageTypes.iterator();
                    while (iterator2.hasNext()) {
                        messageSettings.setNeverShow(iterator2.next(), true);
                        messageSettings.save();
                    }
                }
                LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.BACK_KEY);
                LocalResearchUtil.getInstance().closeSetupWizard();
                tutorial.close();
                this.setApplicationNavigatorEnabled(this.mActivity.isOneShot() ^ true);
                this.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onLayoutOrientationChanged(final LayoutOrientation layoutOrientation) {
        int i;
        if (layoutOrientation == LayoutOrientation.Portrait) {
            i = 1;
        }
        else {
            i = 2;
        }
        this.mStateMachine.sendStaticEvent(StaticEvent.EVENT_ON_ORIENTATION_CHANGED, i);
    }
    
    @Override
    public void onNotifyCoolingUltraLow(final boolean b) {
        if (this.getCapturingMode() == CapturingMode.SLOW_MOTION) {
            return;
        }
        this.updateThermalHintTextMessage(this.getCapturingMode());
        if (b && !this.isZooming()) {
            this.showHintTextIfNeeded();
        }
        else {
            this.mHintText.hide();
        }
        if (this.mFocusRectangles != null) {
            this.mFocusRectangles.clearObjectTracking();
            this.mFocusRectangles.clearFaceDetection();
        }
        if (this.getBaseLayout().getSceneIndicator() != null) {
            this.getBaseLayout().getSceneIndicator().set(false);
        }
        if (this.getBaseLayout().getConditionIndicator() != null) {
            this.getBaseLayout().getConditionIndicator().set(false);
        }
    }
    
    @Override
    public void onObjectLost() {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.mFocusRectangles.onObjectLost();
    }
    
    @Override
    public void onSettingChanged(final UserSettingValue userSettingValue) {
        this.updatePrimaryShortcutIcon(userSettingValue);
    }
    
    @Override
    public void onShutterDone(final boolean b) {
        if (!this.isHeadUpDisplayReady()) {
            return;
        }
        this.clearTouchedScreenButtonGroup();
    }
    
    protected boolean onSideTapped(final SideTouchEventDetector.SideTouchEvent sideTouchEvent) {
        final Point sideTouchPoint = this.getSideTouchPoint(sideTouchEvent);
        if (sideTouchPoint == null) {
            return false;
        }
        if (this.mSideTouchUi.containsIn(SideTouchUi.Type.CAPTURE_COUNTDOWN, SideTouchUi.Type.VIDEO_COUNTDOWN, SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL)) {
            return false;
        }
        if (this.getBaseLayout().isAutoReviewShowing()) {
            this.getBaseLayout().hideAutoReview();
            return false;
        }
        this.mSideTouchUi.destroyTo(SideTouchUi.Type.ZOOM_BAR);
        this.mSideTouchUi.setUiOrientation(this.mOrientation);
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.getCapturingMode().ordinal()]) {
            case 3:
            case 4: {
                this.mSideTouchUi.attachIcon(SideTouchUi.Type.VIDEO_COUNTDOWN, sideTouchPoint);
                break;
            }
            case 1:
            case 2: {
                if (this.mStateMachine.getUserSetting().get(UserSettingKey.SELF_TIMER) == SelfTimer.OFF) {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.CAPTURE_COUNTDOWN, sideTouchPoint);
                    break;
                }
                this.mSideTouchUi.attachIcon(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL, sideTouchPoint);
                break;
            }
        }
        return true;
    }
    
    protected void onSideTouchZoom(final SideTouchEventDetector.SideTouchEvent sideTouchEvent, final int zoomRatio) {
        final Point sideTouchPoint = this.getSideTouchPoint(sideTouchEvent);
        if (sideTouchPoint == null) {
            return;
        }
        this.hideZoomBar();
        this.mSideTouchUi.destroyTo(SideTouchUi.Type.COVERING);
        final SideTouchUi.Type zoom_BAR = SideTouchUi.Type.ZOOM_BAR;
        this.mSideTouchUi.setUiOrientation(this.mOrientation);
        this.mSideTouchUi.attachIcon(zoom_BAR, sideTouchPoint);
        this.mSideTouchUi.showIcon();
        this.setZoomRatio(zoomRatio);
    }
    
    @Override
    public void onStateChanged(final CaptureState captureState, final Object... array) {
        this.onViewFinderStateChanged(captureState, array);
    }
    
    public boolean openTutorial(final TutorialController.DisplayTrigger displayTrigger) {
        if (!this.mActivity.isOneShot() && this.mActivity.getLaunchCondition().getLaunchTrigger() != LaunchCondition.LaunchTrigger.GOOGLE_ASSISTANT) {
            final boolean open = this.getBaseLayout().getTutorial().open(TutorialController.OpenType.create(displayTrigger), this.mActivity.getStoredSettings(), null);
            if (open) {
                this.changeLayoutTo(BaseLayoutPattern.CLEAR);
                this.setApplicationNavigatorEnabled(false);
            }
            return open;
        }
        return false;
    }
    
    @Override
    public void postSlowMotionHintText() {
        final SlowMotion slowMotion = (SlowMotion)this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION);
        if (this.mHintText == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("postSlowMotionHintText: hint text is null");
            }
            return;
        }
        this.cancelSlowMotionHintText();
        if (slowMotion == SlowMotion.OFF) {
            return;
        }
        final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[slowMotion.ordinal()];
        final HintTextContent hintTextContent = null;
        HintTextContent hintTextContent2 = null;
        switch (n) {
            default: {
                hintTextContent2 = null;
                break;
            }
            case 3: {
                hintTextContent2 = new HintTextStandardSlowMotion();
                break;
            }
            case 2: {
                hintTextContent2 = new HintTextSuperSlowShot();
                break;
            }
            case 1: {
                hintTextContent2 = new HintTextSuperSlowMotion();
                break;
            }
        }
        this.postHintText(hintTextContent2);
        if (!this.mIsAlreadySlowMotionLearnMoreButtonDisplayed) {
            this.mIsAlreadySlowMotionLearnMoreButtonDisplayed = true;
            HintTextContent hintTextContent3 = null;
            switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[slowMotion.ordinal()]) {
                default: {
                    hintTextContent3 = hintTextContent;
                    break;
                }
                case 3: {
                    hintTextContent3 = new HintTextStandardSlowMotionDescription(this.getBaseLayout().getTutorial(), (Context)this.mActivity);
                    break;
                }
                case 2: {
                    hintTextContent3 = new HintTextSuperSlowShotDescription(this.getBaseLayout().getTutorial(), (Context)this.mActivity);
                    break;
                }
                case 1: {
                    hintTextContent3 = new HintTextSuperSlowMotionDescription(this.getBaseLayout().getTutorial(), (Context)this.mActivity);
                    break;
                }
            }
            this.postHintText(hintTextContent3);
        }
    }
    
    boolean predictiveLaunchCoverExists() {
        final PredictiveLaunchCoverView predictiveLaunchCoverView = this.getBaseLayout().getPredictiveLaunchCoverView();
        return predictiveLaunchCoverView != null && predictiveLaunchCoverView.exists();
    }
    
    @Override
    public void prepareGestureShutterCountDown() {
        if (this.mPhotoSelfTimerSetting == SelfTimer.OFF) {
            this.mPhotoSelfTimerSetting = SelfTimer.GESTURE_SHUTTER_COUNT_DOWN;
            if (this.mSelfTimerCountDownViewNext == null) {
                this.setupSelfTimerCountDownView();
            }
            this.mSelfTimerCountDownViewNext.setSelfTimer(this.mPhotoSelfTimerSetting);
        }
    }
    
    public void prepareSelfTimerAndTouchCapture() {
        final UserSettings userSetting = this.mStateMachine.getUserSetting();
        this.mPhotoSelfTimerSetting = (SelfTimer)userSetting.get(this.getCapturingMode(), UserSettingKey.SELF_TIMER);
        this.mTouchCapture = (TouchCapture)userSetting.get(this.getCapturingMode(), UserSettingKey.TOUCH_CAPTURE);
    }
    
    public void reconstructLocalCache() {
        if (this.getBaseLayout() != null && this.getBaseLayout().getContentsViewController() != null) {
            this.getBaseLayout().getContentsViewController().reconstructLocalCache();
        }
    }
    
    @Override
    public void requestCheckEvfPreparationRetrying() {
        this.retryToCheckEvfPreparationDelayed();
    }
    
    public void requestCreateContentInfoSync(final ArrayList<Uri> list) {
        if (this.getBaseLayout() != null && this.getBaseLayout().getContentsViewController() != null) {
            this.getBaseLayout().getContentsViewController().requestCreateContentInfoSync(list);
        }
    }
    
    @Override
    public void requestInflate(final LayoutInflater layoutInflater) {
        if (CamLog.VERBOSE) {
            CamLog.d("startInflateTask in");
        }
        this.startInflateTask(layoutInflater, FastLayoutAsyncInflateItems.getInflateItemsForFast());
        if (CamLog.VERBOSE) {
            CamLog.d("startInflateTask out");
        }
    }
    
    public void saveLocalCache() {
        if (this.getBaseLayout() != null && this.getBaseLayout().getContentsViewController() != null) {
            this.getBaseLayout().getContentsViewController().saveLocalCache();
        }
    }
    
    @Override
    public void sendViewUpdateEvent(final ViewUpdateEvent obj, final Object... array) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendViewUpdateEvent() event: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        Label_2838: {
            switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[obj.ordinal()]) {
                case 56: {
                    final CapturingMode capturingMode = (CapturingMode)array[0];
                    final Mode mode = (Mode)array[1];
                    final ModeSelectorInternalMode tag = ((InternalMode)mode).getTag();
                    if (this.mActivity.isDeviceInSecurityLock() && tag == ModeSelectorInternalMode.DUAL_MONOCHROME) {
                        final CameraActivity mActivity = this.mActivity;
                        final ActivityOptions customAnimation = ActivityOptions.makeCustomAnimation((Context)mActivity, 0, 0);
                        final Intent commit = LaunchCameraIntentBuilder.create().mode(this.getCapturingMode().name()).activity("com.sonyericsson.android.camera", "com.sonyericsson.android.camera.CameraActivity").callingMode(CapturingModeUtil.filteringPrevName(this.getCapturingMode().name())).callingActivity(((Context)mActivity).getPackageName(), CapturingModeUtil.filteringPrevActivity(((Context)mActivity).getClass().getName())).commit();
                        commit.putExtra("internal_mode", ModeSelectorInternalMode.DUAL_MONOCHROME.ordinal());
                        commit.putExtra("capturing_mode", capturingMode.ordinal());
                        this.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP, commit, customAnimation.toBundle(), mode);
                        return;
                    }
                    break;
                }
                case 55: {
                    this.onAppsUiModeFinish();
                    break;
                }
                case 54: {
                    this.getBaseLayout().getMruButtonContainer().setMode((Mode)array[0]);
                    break;
                }
                case 53: {
                    if (!(this.mIsNeedDisplayToastChangeInternalStoarge = (boolean)array[0])) {
                        this.showMessageDialog(DialogId.DESTINATION_TO_SAVE_CHANGED_INTERNAL, new Object[0]);
                        break;
                    }
                    break;
                }
                case 52: {
                    this.mIsSettingChangeAcceptable = (boolean)array[0];
                    break;
                }
                case 51: {
                    this.onCapturingModeChanging();
                    break;
                }
                case 50: {
                    this.updateVideoHdrCondition(this.getCapturingMode(), (VideoHdr)array[0], (boolean)array[1]);
                    break;
                }
                case 49: {
                    this.updateFusionHintText((CameraParameters.FusionResult)array[0]);
                    break;
                }
                case 48: {
                    this.getBaseLayout().hideBlackScreen();
                    break;
                }
                case 47: {
                    this.getBaseLayout().showBlackScreen();
                    break;
                }
                case 46: {
                    if (this.mSettingDialogStack != null && !this.mSettingDialogStack.isDialogOpened()) {
                        this.postHintText(new HintTextTimedOutMessage(HintTextTimedOutMessage.MessageType.ISO_CHANGED_BY_FUSION));
                        break;
                    }
                    break;
                }
                case 45: {
                    if (this.getBaseLayout().getImageQualityControl().isInitialized() && this.getBaseLayout().getImageQualityControl().get().isVisible()) {
                        this.getBaseLayout().getImageQualityControl().get().refresh();
                    }
                    if (this.getCapturingMode() != CapturingMode.VIDEO) {
                        break;
                    }
                    final FusionMode fusionMode = (FusionMode)array[0];
                    if (this.mPrimaryShortcutGroup != null) {
                        this.mPrimaryShortcutGroup.updatePrimaryShortcutIcon(UserSettingKey.FUSION_MODE, fusionMode.getIconId());
                        break;
                    }
                    break;
                }
                case 44: {
                    if (array[0]) {
                        this.showMessageDialog(DialogId.LOW_BATTERY_CRITICAL_ON_RECORDING, new Object[0]);
                        break;
                    }
                    this.showMessageDialog(DialogId.LOW_BATTERY_CRITICAL_ON_PHOTO, new Object[0]);
                    break;
                }
                case 43: {
                    if (!this.mMessageDialog.isOpened()) {
                        this.showMessageDialog(DialogId.MAX_FILESIZE_REACHED, new Object[0]);
                        break;
                    }
                    break;
                }
                case 42: {
                    final UserSettings userSetting = this.mStateMachine.getUserSetting();
                    if (!userSetting.isLimitForSizeOrDuration() && VideoSize.MMS != userSetting.get(UserSettingKey.VIDEO_SIZE) && !this.mMessageDialog.isOpened()) {
                        this.showMessageDialog(DialogId.MAX_DURATION_REACHED, new Object[0]);
                        break;
                    }
                    break;
                }
                case 41: {
                    this.onCaptureCanceled();
                    break;
                }
                case 40: {
                    this.onCaptureFinished();
                    break;
                }
                case 39: {
                    this.onBurstFinished();
                    break;
                }
                case 38: {
                    this.onBurstShutterDone((boolean)array[0], (int)array[1]);
                    break;
                }
                case 37: {
                    this.onBurstRejected((BurstRejectedReason)array[0]);
                    break;
                }
                case 36: {
                    this.mIsFrontAngleChanging = false;
                    break;
                }
                case 35: {
                    this.mIsFrontAngleChanging = true;
                    if (this.mFocusRectangles != null) {
                        this.mFocusRectangles.clearFaceDetection();
                    }
                    this.getBaseLayout().getSceneIndicator().set(false);
                    this.getBaseLayout().getConditionIndicator().set(false);
                    if (this.mFrontAngleSwitchButton != null) {
                        this.mFrontAngleSwitchButton.switchFrontAngle((FrontAngle)this.mStateMachine.getUserSetting().get(UserSettingKey.FRONT_ANGLE));
                        break;
                    }
                    break;
                }
                case 34: {
                    this.updateGridLineView();
                    break;
                }
                case 33: {
                    this.requestToRestoreSystemUi();
                    this.updateGeotagIcon();
                    break;
                }
                case 32: {
                    if (array[0]) {
                        this.showToastMessage(ToastContent.ToastID.NEEDS_TO_COOL_DOWN);
                        break;
                    }
                    this.showMessageDialog(DialogId.THERMAL_CRITICAL, new Object[0]);
                    break;
                }
                case 31: {
                    this.onNotifyThermalStatus(true);
                    if (!this.mIsThermalWarningDialogShown) {
                        this.showMessageDialog(DialogId.THERMAL_WARNING, new Object[0]);
                        this.mIsThermalWarningDialogShown = true;
                        break;
                    }
                    break;
                }
                case 30: {
                    this.onNotifyThermalStatus(false);
                    break;
                }
                case 29: {
                    this.addVideoChapter((ChapterThumbnail)array[0]);
                    break;
                }
                case 28: {
                    this.onLazyInitializationTaskRun();
                    break;
                }
                case 27: {
                    this.startCaptureFeedbackAnimation();
                    break;
                }
                case 26: {
                    this.onStoreCompleted((StoreDataResult)array[0], (boolean)array[1]);
                    break;
                }
                case 25: {
                    final SavingRequest savingRequest = (SavingRequest)array[1];
                    final StoreDataResult storeDataResult = (StoreDataResult)array[2];
                    final String mimeType = savingRequest.common.mimeType;
                    Uri uri;
                    if (storeDataResult != null && storeDataResult.savingRequest.getRequestId() == savingRequest.getRequestId()) {
                        uri = storeDataResult.uri;
                    }
                    else {
                        uri = null;
                    }
                    if (uri != null) {
                        if (mimeType != "video/mp4" && mimeType != "video/3gpp") {
                            this.openInstantViewer((byte[])array[0], null, savingRequest);
                        }
                        else {
                            this.openInstantViewer(null, (String)array[0], savingRequest);
                        }
                        InstantViewer.launchAlbum(this.mActivity, uri, mimeType, true, this.mStateMachine.getPredictiveCaptureStoreInfo());
                        break;
                    }
                    if (mimeType != "video/mp4" && mimeType != "video/3gpp") {
                        this.openInstantViewer((byte[])array[0], null, savingRequest);
                        break;
                    }
                    this.openInstantViewer(null, (String)array[0], savingRequest);
                    break;
                }
                case 24: {
                    this.closeDialogs();
                    break;
                }
                case 23: {
                    this.hideAutoReview();
                    final UiComponentKind uiComponentKind = (UiComponentKind)array[0];
                    this.updateUiComponent(uiComponentKind);
                    if (this.isOverlayControlVisible() && this.mStateMachine.isDialogOpened() && !this.isSettingDialogOpened()) {
                        return;
                    }
                    switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[uiComponentKind.ordinal()]) {
                        default: {
                            break Label_2838;
                        }
                        case 11: {
                            this.requestToDimSystemUi();
                            break Label_2838;
                        }
                        case 9: {
                            this.requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openModeSelectDialog(this.mModeLoader, this.mModeSelectListener);
                                break Label_2838;
                            }
                            break Label_2838;
                        }
                        case 8: {
                            this.requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openSettingMenuDialog();
                                break Label_2838;
                            }
                            break Label_2838;
                        }
                        case 7: {
                            this.requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.HDR);
                                break Label_2838;
                            }
                            break Label_2838;
                        }
                        case 6: {
                            this.requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.VIDEO_HDR);
                                break Label_2838;
                            }
                            break Label_2838;
                        }
                        case 5: {
                            this.requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.FUSION_MODE);
                                break Label_2838;
                            }
                            break Label_2838;
                        }
                        case 4: {
                            this.requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.ASPECT_RATIO);
                                break Label_2838;
                            }
                            break Label_2838;
                        }
                        case 3: {
                            this.requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.SELF_TIMER);
                                break Label_2838;
                            }
                            break Label_2838;
                        }
                        case 2: {
                            this.requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                final CapturingMode capturingMode2 = this.getCapturingMode();
                                final UserSettingKey flash = UserSettingKey.FLASH;
                                UserSettingKey userSettingKey;
                                if (capturingMode2.getType() == 2) {
                                    userSettingKey = UserSettingKey.PHOTO_LIGHT;
                                }
                                else if (capturingMode2.isFront()) {
                                    userSettingKey = UserSettingKey.DISPLAY_FLASH;
                                }
                                else {
                                    userSettingKey = UserSettingKey.FLASH;
                                }
                                this.mSettingUi.openShortcutSettingDialog(userSettingKey);
                                break Label_2838;
                            }
                            break Label_2838;
                        }
                        case 1: {
                            if (this.getCapturingMode() != CapturingMode.FRONT_VIDEO && this.getCapturingMode() != CapturingMode.VIDEO && this.getCapturingMode() != CapturingMode.SLOW_MOTION) {
                                this.changeToPhotoReadyView(false);
                            }
                            else {
                                this.changeToVideoReadyView();
                            }
                            this.requestToDimSystemUi();
                            break Label_2838;
                        }
                    }
                    break;
                }
                case 22: {
                    this.setOrientation((int)array[0]);
                    break;
                }
                case 21: {
                    this.mRecordingTimeProxy.notifyOnTimeTicked((int)array[0]);
                    break;
                }
                case 20: {
                    if (this.mFocusRectangles != null) {
                        this.mFocusRectangles.clearAllFocusExceptFace();
                        break;
                    }
                    break;
                }
                case 19: {
                    if (this.isTouchFocus()) {
                        this.disableSemiAutoControl();
                    }
                    if (this.mFocusRectangles != null && this.isTouchFocus()) {
                        this.mFocusRectangles.clearTouchFocus();
                        break;
                    }
                    break;
                }
                case 18: {
                    if (this.isTouchFocus()) {
                        this.disableSemiAutoControl();
                    }
                    if (this.mFocusRectangles != null) {
                        this.mFocusRectangles.clearAllFocusExceptFace();
                        break;
                    }
                    break;
                }
                case 17: {
                    if (this.isTouchFocus()) {
                        this.disableSemiAutoControl();
                    }
                    if (this.mFocusRectangles != null) {
                        this.mFocusRectangles.clearAllFocus();
                        break;
                    }
                    break;
                }
                case 16: {
                    this.hideAutoReview();
                    final Point point = (Point)array[0];
                    final FocusRectangles.FocusSetType focusSetType = (FocusRectangles.FocusSetType)array[1];
                    this.mFocusRectangles.setFocusPosition(point, focusSetType);
                    if (focusSetType == FocusRectangles.FocusSetType.FIRST) {
                        this.mFocusRectangles.setVisibility(4);
                        if (PlatformCapability.isFocusSupported(this.getCapturingMode().getCameraId())) {
                            this.mFocusRectangles.onAutoFocusStarted();
                            break;
                        }
                        break;
                    }
                    else {
                        if (focusSetType == FocusRectangles.FocusSetType.RELEASE) {
                            this.mFocusRectangles.setVisibility(0);
                            break;
                        }
                        this.mFocusRectangles.setVisibility(4);
                        break;
                    }
                    break;
                }
                case 15: {
                    this.cancelSelfTimerCountDownView();
                    break;
                }
                case 14: {
                    final int intValue = (int)array[0];
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("EVENT_ON_ZOOM_CHANGED  cur:");
                        sb2.append(intValue);
                        CamLog.d(sb2.toString());
                    }
                    final Zoombar zoomBar = this.getBaseLayout().getZoomBar();
                    if (zoomBar != null && zoomBar.getVisibility() == 0) {
                        this.setZoomRatio(intValue);
                        break;
                    }
                    break;
                }
                case 13: {
                    final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[((BaseLayoutPattern)this.mLayoutPattern).ordinal()];
                    if (n != 2) {
                        switch (n) {
                            default: {
                                break Label_2838;
                            }
                            case 5: {
                                if (!this.getCapturingMode().isVideo()) {
                                    this.changeToPhotoReadyView(false);
                                    break Label_2838;
                                }
                                if (this.getCapturingMode() == CapturingMode.SLOW_MOTION) {
                                    this.postSlowMotionHintText();
                                }
                                this.changeToVideoReadyView();
                                break Label_2838;
                            }
                            case 4: {
                                this.changeToVideoRecordingPauseView();
                                break Label_2838;
                            }
                        }
                    }
                    else {
                        if (this.getCapturingMode() != CapturingMode.SLOW_MOTION) {
                            this.changeToVideoRecordingView();
                            break;
                        }
                        final SlowMotion slowMotion = (SlowMotion)this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION);
                        if (slowMotion == SlowMotion.STANDARD_SLOW_MOTION) {
                            this.changeToStandardSlowMotionRecordingView();
                            break;
                        }
                        if (slowMotion == SlowMotion.SUPER_SLOW_MOTION) {
                            this.changeToSuperSlowMotionVideoLowFrameRateRecordingView();
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 12: {
                    this.mZoomBarProxy.bindZoomBar(this.getBaseLayout().getZoomBar());
                    this.mZoomBarProxy.update(this.getBaseLayout().getZoomBar().getZoomRatios(), (int)array[0]);
                    if (!this.mStateMachine.isRecording()) {
                        this.changeToZoomingView();
                        break;
                    }
                    this.changeToVideoZoomingWhileRecordingView();
                    break;
                }
                case 11: {
                    this.onTrackedObjectStateUpdated((CameraParameters.ObjectTrackingResult)array[0]);
                    break;
                }
                case 10: {
                    this.applySmileFocusThreshold(true);
                    if (!this.getCapturingMode().isVideo()) {
                        final SmileCapture smileCapture = (SmileCapture)this.mStateMachine.getUserSetting().get(UserSettingKey.SMILE_CAPTURE);
                        if (smileCapture != null) {
                            this.getBaseLayout().getPhotoSmileCaptureIndicator().set(smileCapture.isSmileCaptureOn());
                            break;
                        }
                        break;
                    }
                    else {
                        final VideoSmileCapture videoSmileCapture = (VideoSmileCapture)this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SMILE_CAPTURE);
                        if (videoSmileCapture != null) {
                            this.getBaseLayout().getVideoSmileCaptureIndicator().set(videoSmileCapture.isSmileCaptureOn());
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 9: {
                    this.mFocusRectangles.clearObjectTracking();
                    break;
                }
                case 8: {
                    this.hideAutoReview();
                    this.mFocusRectangles.setObjectTrackingRectSupported(true);
                    this.mFocusRectangles.startObjectTracking();
                    this.applySmileFocusThreshold(false);
                    this.getBaseLayout().getPhotoSmileCaptureIndicator().set(false);
                    this.getBaseLayout().getVideoSmileCaptureIndicator().set(false);
                    break;
                }
                case 7: {
                    final CameraParameters.FaceDetectionResult uuidFaceDetectionResult = (CameraParameters.FaceDetectionResult)array[0];
                    if (this.mIsFaceDetectionIdSupported == null) {
                        if (!uuidFaceDetectionResult.extFaceList.isEmpty()) {
                            this.mIsFaceDetectionIdSupported = FaceDetectUtil.hasValidFaceId(uuidFaceDetectionResult);
                        }
                    }
                    else if (!this.mIsFaceDetectionIdSupported) {
                        FaceDetectUtil.setUuidFaceDetectionResult(uuidFaceDetectionResult);
                    }
                    if (!this.predictiveLaunchCoverExists()) {
                        this.onFaceDetected(uuidFaceDetectionResult);
                        break;
                    }
                    break;
                }
                case 6: {
                    this.mFocusRectangles.startFaceDetection();
                    break;
                }
                case 5: {
                    this.onSceneModeChanged((CameraParameters.SceneRecognitionResult)array[0]);
                    break;
                }
                case 4: {
                    if (!this.isCameraSwitching()) {
                        this.onCapturingModeChanged((CapturingMode)array[0], (boolean)array[1], (AnimationRequest.AnimationType)array[2]);
                    }
                    this.updatePrimaryShortcutIcons();
                    this.updateScreenButtonImage((CapturingMode)array[0]);
                    if (this.mSettingDialogStack != null) {
                        this.mSettingDialogStack.setCapturingMode((CapturingMode)array[0]);
                        break;
                    }
                    break;
                }
                case 3: {
                    final RecordingIndicator recordingIndicator = this.getBaseLayout().getRecordingIndicator();
                    if (recordingIndicator == null) {
                        break;
                    }
                    final VideoHdr videoHdr = (VideoHdr)array[3];
                    if (this.getCapturingMode() != CapturingMode.SLOW_MOTION && videoHdr != VideoHdr.HDR_ON) {
                        if (array[2]) {
                            recordingIndicator.setSequenceMode(true);
                        }
                        recordingIndicator.setConstraint((boolean)array[1]);
                        recordingIndicator.prepareBeforeRecording((int)array[0]);
                        break;
                    }
                    recordingIndicator.setSequenceMode(false);
                    recordingIndicator.setConstraint(false);
                    recordingIndicator.prepareBeforeRecording((int)array[0]);
                    break;
                }
                case 2: {
                    if (this.mActivity == null) {
                        break;
                    }
                    this.resizeEvfScope((Rect)array[0]);
                    if (this.mHintText != null) {
                        this.updateHintTextContainer((Rect)array[0]);
                    }
                    final boolean booleanValue = (boolean)array[1];
                    if (this.mIsSetupHeadupDisplayInvoked && booleanValue && !this.isTutorialOpened()) {
                        this.mIsSurfaceViewHideWhileAspectChanging = true;
                        this.mEvf.hide();
                        this.updateCaptureAreaSize();
                        this.updateGridLineView();
                    }
                    this.showSurface();
                    break;
                }
                case 1: {
                    this.setupHeadUpDisplay((HeadUpDisplaySetupState)array[0]);
                    this.checkupThermalCoolingRequest();
                    this.updateSecondaryShortcutOnScreenButtonResource();
                    break;
                }
            }
        }
    }
    
    @Override
    public void setCameraDevice(final CameraDeviceHandler mCameraDevice) {
        this.mCameraDevice = mCameraDevice;
    }
    
    @Override
    public void setContentView() {
        if (CamLog.VERBOSE) {
            CamLog.d("setContentView():[IN]");
        }
        this.setup(this.mEvf.asView());
        this.clearTouchCapture();
        if (CamLog.VERBOSE) {
            CamLog.d("setContentView():[OUT]");
        }
    }
    
    @Override
    public void setDisplayFlashColor(final int n, final int n2, final int n3) {
        if (n >= 0 && n <= 255 && n2 >= 0 && n2 <= 255 && n3 >= 0 && n3 <= 255 && this.isDisplayFlashRequired()) {
            this.mDisplayFlashColor = Color.rgb(n, n2, n3);
        }
        else {
            this.mDisplayFlashColor = -1;
        }
    }
    
    @Override
    public void setDisplayFlashRequired(final boolean mRequireDisplayFlash) {
        this.mRequireDisplayFlash = mRequireDisplayFlash;
    }
    
    @Override
    public void setIsCameraSwitching(final boolean isCameraSwitching) {
        this.getBaseLayout().setIsCameraSwitching(isCameraSwitching);
    }
    
    @Override
    public void setRecordingOrientation(final int mRecordingOrientation) {
        this.mRecordingOrientation = mRecordingOrientation;
    }
    
    @Override
    public void setSelfTimer(final CapturingMode obj, final SelfTimer obj2) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setSelfTimer: ");
            sb.append(obj);
            sb.append(" ");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        if (this.mActivity.isOneShotVideo()) {
            return;
        }
        SelfTimer off;
        if ((off = obj2) == null) {
            off = SelfTimer.OFF;
        }
        this.mPhotoSelfTimerSetting = off;
        this.setupSelfTimerCountDownView();
    }
    
    @Override
    public void setShutterTrigger(final ShutterTrigger mShutterTrigger) {
        this.mShutterTrigger = mShutterTrigger;
        this.applyShutterTriggerSettings();
    }
    
    @Override
    public void setStartDraggingSlopEnabled(final boolean startDraggingSlopEnabled) {
        this.getBaseLayout().setStartDraggingSlopEnabled(startDraggingSlopEnabled);
    }
    
    @Override
    public void setStateMachine(final StateMachine mStateMachine) {
        if (CamLog.VERBOSE) {
            CamLog.d("setStateMachine():[IN]");
        }
        if (mStateMachine != null) {
            mStateMachine.addOnStateChangedListener((StateMachine.OnStateChangedListener)this);
            mStateMachine.setGestureShutterWindowHost(new GestureShutterListener());
        }
        else if (this.mStateMachine != null) {
            this.mStateMachine.removeOnStateChangedListener((StateMachine.OnStateChangedListener)this);
        }
        this.mStateMachine = mStateMachine;
    }
    
    @Override
    public void setupFocusRectangles() {
        int width;
        int height;
        if (this.mCameraDevice != null && this.mCameraDevice.getPreviewSize() != null) {
            final Rect previewSize = this.mCameraDevice.getPreviewSize();
            final Size sizeAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getSizeAccordingToLayoutOrientation(new Size(previewSize.width(), previewSize.height()));
            width = sizeAccordingToLayoutOrientation.getWidth();
            height = sizeAccordingToLayoutOrientation.getHeight();
        }
        else {
            width = 0;
            height = 0;
        }
        this.mOnFocusRectangleTouchListener = (View$OnTouchListener)new View$OnTouchListener(this) {
            final ViewFinderImpl this$0;
            
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    default: {
                        return false;
                    }
                    case 2: {
                        if (CamLog.VERBOSE) {
                            CamLog.d("onTouch ACTION_MOVE");
                        }
                        if (!this.this$0.isTouchCaptureEnabled()) {
                            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
                            return true;
                        }
                        return false;
                    }
                    case 1: {
                        if (CamLog.VERBOSE) {
                            CamLog.d("onTouch ACTION_UP");
                        }
                        if (!this.this$0.isTouchCaptureEnabled()) {
                            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
                            if (this.this$0.mCameraDevice.isObjectTrackingRunning() && !this.this$0.isZooming()) {
                                this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DESELECT_OBJECT_POSITION, new Object[0]);
                            }
                            this.this$0.hideAutoReview();
                            this.this$0.switchSemiAutoStateByTouch(false);
                            return true;
                        }
                        return false;
                    }
                    case 0: {
                        if (CamLog.VERBOSE) {
                            CamLog.d("onTouch ACTION_DOWN");
                        }
                        if (!this.this$0.isTouchCaptureEnabled()) {
                            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
                            return true;
                        }
                        return false;
                    }
                }
            }
        };
        final FocusRectanglesViewList list = new FocusRectanglesViewList();
        if (this.isInflated()) {
            list.rectanglesContainer = (RelativeLayout)this.mActivity.findViewById(2131296404);
            list.faceViewList = this.getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FACE).toArray(new View[0]);
            list.trackedObjectView = (TaggedRectangle)this.getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_OBJECT_TRACKING).get(0);
            list.singleAfView = (RelativeLayout)this.getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_SINGLE).get(0);
            list.touchAfView = (RelativeLayout)this.getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_TOUCH).get(0);
        }
        if (this.mFocusRectangles == null) {
            this.mFocusRectangles = new FocusRectangles(this.mActivity, new FocusActionListenerImpl(), width, height, list, (View)this.mViewFinderCaptureArea, this.mOnFocusRectangleTouchListener, this.mScreenAspect);
        }
        if (this.mStateMachine == null) {
            return;
        }
        if (PlatformCapability.isFaceDetectionAvailable(this.getCapturingMode().getCameraId())) {
            if (this.isTouchCaptureEnabled()) {
                this.mFocusRectangles.enableFaceTouchCapture();
            }
            else {
                this.mFocusRectangles.disableFaceTouchCapture();
            }
        }
        this.applySmileFocusThreshold(true);
        this.mFocusRectangles.setVisibility(0);
    }
    
    @Override
    public void showAutoPowerOffHintText() {
        if (this.mHintText != null) {
            this.postHintText(new HintTextAutoPowerOff());
        }
    }
    
    @Override
    public void showBlank() {
        this.mBaseLayout.setupBlankScreen();
        this.mBaseLayout.showBlankScreen();
    }
    
    @Override
    public void showDisplayFlashScreen() {
        if (this.isDisplayFlashRequired()) {
            if (this.mWindowDisplayFlashScreen == null) {
                final LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
                if (layoutInflater == null) {
                    return;
                }
                this.mWindowDisplayFlashScreen = layoutInflater.inflate(2131492921, (ViewGroup)null);
                final Window window = this.mActivity.getWindow();
                window.addContentView(this.mWindowDisplayFlashScreen, (ViewGroup$LayoutParams)window.getAttributes());
            }
            if (this.mWindowDisplayFlashScreen != null) {
                this.mWindowDisplayFlashScreen.setBackgroundColor(this.mDisplayFlashColor);
                this.mWindowDisplayFlashScreen.setVisibility(0);
                this.mIsDisplayFlashScreenDisplayed = true;
            }
        }
    }
    
    @Override
    public void showHiSpeedSdCardRecommendDialogOnDestinationChange() {
        if (this.isNeedToShowHiSpeedSdCardRecommendation()) {
            final VideoSize videoSize = (VideoSize)this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SIZE);
            if (this.mStateMachine.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
                this.showMessageDialog(DialogId.HIGH_SPEED_SD_RECOMMENDATION_ON_SETTING_CHANGE, new Object[0]);
            }
            else if (videoSize.is4KVideo()) {
                this.showMessageDialog(DialogId.FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_DESTINATION_CHANGE, new Object[0]);
            }
        }
    }
    
    public void showHiSpeedSdCardRecommendDialogOnModeChange() {
        if (this.isNeedToShowHiSpeedSdCardRecommendation() && this.mStateMachine.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
            this.showMessageDialog(DialogId.HIGH_SPEED_SD_RECOMMENDATION_ON_MODE_CHANGE, new Object[0]);
        }
    }
    
    @Override
    public void showMessageDialog(final DialogId obj, final Object... array) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("showMessageDialog() E : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        final ShowMessageDialogTask showMessageDialogTask = new ShowMessageDialogTask(obj, array);
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[obj.ordinal()]) {
            default: {
                showMessageDialogTask.run();
                break;
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                if (this.mSettingUi == null) {
                    this.mDelayUpdatedViewTaskList.add(showMessageDialogTask);
                    break;
                }
                showMessageDialogTask.run();
                break;
            }
        }
    }
    
    @Override
    public void showSavingProgressBar() {
        if (this.isHeadUpDisplayReady()) {
            this.mScreenButtonHandler.clearAllButton();
        }
        if (this.mSavingProgressBar == null) {
            final int dimensionPixelSize = this.mActivity.getResources().getDimensionPixelSize(2131165547);
            this.mSavingProgressBar = (View)new ProgressBar((Context)this.mActivity);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(dimensionPixelSize, dimensionPixelSize);
            frameLayout$LayoutParams.gravity = 17;
            this.mActivity.getWindow().addContentView(this.mSavingProgressBar, (ViewGroup$LayoutParams)frameLayout$LayoutParams);
        }
        this.mSavingProgressBar.setVisibility(0);
    }
    
    @Override
    public void showSurface() {
        if (!this.mEvf.isShown()) {
            this.mEvf.show();
        }
    }
    
    @Override
    public void showViews() {
        this.getBaseLayout().getPhotoSmileCaptureIndicator().show();
        if (this.needToShowGeoTagIndicator()) {
            this.getBaseLayout().getGeoTagIndicator().show();
        }
        this.getBaseLayout().getLowMemoryInternalIndicator().show();
        this.getBaseLayout().getLowMemorySdIndicator().show();
        this.getBaseLayout().getThermalIndicator().show();
        this.getBaseLayout().getBatteryIndicator().show();
        this.mHintText.showAll();
        this.mFocusRectangles.onUiComponentRemoved();
    }
    
    @Override
    public void startHideThumbnail() {
        if (CamLog.VERBOSE) {
            CamLog.d("startHideThumbnail: ");
        }
        if (this.getBaseLayout().getContentsViewController() == null) {
            return;
        }
        this.getBaseLayout().getContentsViewController().stopAnimation(false);
        final Animation loadAnimation = AnimationUtils.loadAnimation((Context)this.mActivity, 2130772002);
        loadAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener(this) {
            final ViewFinderImpl this$0;
            
            public void onAnimationEnd(final Animation animation) {
                if (this.this$0.mCameraDevice.isRecording()) {
                    this.this$0.getBaseLayout().getContentsViewController().hide();
                }
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
            }
        });
        this.getBaseLayout().getContentsViewController().startHideAnimation(loadAnimation);
    }
    
    @Override
    public void startPredictiveCaptureIndicatorAnimation() {
        this.getBaseLayout().getPredictiveCaptureIndicatorController().startAnimation();
    }
    
    public void startReturnModeAnimation() {
        final AnimationRequest animationRequest = new AnimationRequest(AnimationRequest.AnimationType.MODE_SELECTOR, AnimationRequest.AnimationDegree.START, this.getCapturingMode(), CapturingMode.SCENE_RECOGNITION);
        if (this.requestAnimation(animationRequest)) {
            this.mStateMachine.sendEvent(TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest);
        }
    }
    
    @Override
    public void startSlowMotionFeedbackAnimation() {
        this.getBaseLayout().getSuperSlowMotionTriggerAnimation().start((SuperSlowMotionTriggerAnimationController.OnAnimationEndListener)new SuperSlowMotionTriggerAnimationController.OnAnimationEndListener(this) {
            final ViewFinderImpl this$0;
            
            @Override
            public void onAnimationEnd() {
                this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_SLOW_MOTION_FEEDBACK_ANIMATION_END, new Object[0]);
            }
        }, this.mRecordingOrientation == 2);
    }
    
    public void switchSemiAutoAvailability() {
        if (this.getBaseLayout().getSemiAutoControl().isInitialized()) {
            if (this.getBaseLayout().getSemiAutoControl().get().isEnabled()) {
                this.disableSemiAutoControl();
            }
            else {
                this.enableSemiAutoControl(false);
            }
        }
        else {
            this.enableSemiAutoControl(false);
        }
        if (this.mSettingDialogStack != null) {
            this.mSettingDialogStack.closeAllSettingDialogs();
        }
    }
    
    public void switchSemiAutoStateByTouch(final boolean b) {
        if (this.mStateMachine == null || !this.mStateMachine.isMenuAvailable()) {
            return;
        }
        if (!this.isPreviewLayout(this.getCurrentLayoutPattern())) {
            return;
        }
        if (this.isTouchCaptureEnabled()) {
            return;
        }
        if (this.isObjectTrackingEnabled()) {
            return;
        }
        if (this.isSmileShutterEnabled()) {
            return;
        }
        if (!isSemiAutoControlAvailable(this.getCapturingMode())) {
            return;
        }
        if (b) {
            this.enableSemiAutoControl(true);
        }
        else {
            this.disableSemiAutoControl();
        }
    }
    
    @Override
    public void updateBatteryIndicator(final int batteryLevel) {
        this.getBaseLayout().getBatteryIndicator().setBatteryLevel(batteryLevel);
    }
    
    @Override
    public void updateCaptureAreaSize() {
        if (this.mViewFinderCaptureArea != null) {
            final Rect rect = this.mEvf.getRect();
            final int width = rect.width();
            final int height = rect.height();
            final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)this.mViewFinderCaptureArea.getLayoutParams();
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
            this.mViewFinderCaptureArea.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.updatePreviewContainer(width, height);
            this.mHintText.updateHintTextContainer(layoutParams.width, layoutParams.height);
            this.getBaseLayout().repositionZoombar();
            PositionConverter.getInstance().setSurfaceSize(rect.width(), rect.height());
        }
    }
    
    @Override
    public void updateFocusIconType(final boolean focusIconType) {
        this.mFocusRectangles.setFocusIconType(focusIconType);
    }
    
    public void updatePreviewLayoutParams() {
        if (this.mEvf != null && this.mEvf.asView() != null && this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE && this.mPreviewOrientation != LayoutOrientationResolver.getInstance().getConfigurationOrientation()) {
            this.mEvf.asView().setLayoutParams(this.getPreviewLayoutParams());
        }
    }
    
    @Override
    public void updateSlowMotionView(final SlowMotion slowMotion) {
        this.mIsAlreadySlowMotionLearnMoreButtonDisplayed = false;
        switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[slowMotion.ordinal()]) {
            case 3: {
                this.changeScreenButtonImage(HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY, false);
                break;
            }
            case 2: {
                this.changeScreenButtonImage(HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY, false);
                break;
            }
            case 1: {
                this.changeScreenButtonImage(HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY, false);
                break;
            }
        }
        this.disableSemiAutoControl();
        if (this.getBaseLayout() != null) {
            this.disableOverlayControl(this.getBaseLayout().getImageQualityControl());
        }
    }
    
    public void updateTouchCapture(final TouchCapture touchCapture) {
        final boolean b = true;
        int n = 0;
        Label_0057: {
            if (touchCapture != null) {
                n = (b ? 1 : 0);
                switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$TouchCapture[touchCapture.ordinal()]) {
                    case 2: {
                        if (this.isFront()) {
                            n = (b ? 1 : 0);
                            break Label_0057;
                        }
                        break;
                    }
                    case 1: {
                        break Label_0057;
                    }
                }
            }
            n = 0;
        }
        if (n != 0) {
            this.mFocusRectangles.enableFaceTouchCapture();
            if (this.mFocusRectangles.isTouchFocus() || this.mCameraDevice.isObjectTrackingRunning()) {
                this.mStateMachine.sendEvent(TransitterEvent.EVENT_DESELECT_OBJECT_POSITION, new Object[0]);
                this.mStateMachine.sendEvent(TransitterEvent.EVENT_CANCEL_TOUCHED_POSITION, new Object[0]);
            }
        }
        else {
            this.mFocusRectangles.disableFaceTouchCapture();
        }
    }
    
    @Override
    public void updateVideoShutterTrigger() {
        this.applyShutterTriggerSettings();
    }
    
    private class ActionRunnable implements Runnable
    {
        private UserSettingKey mUserSettingKey;
        final ViewFinderImpl this$0;
        
        public ActionRunnable(final ViewFinderImpl this$0, final UserSettingKey mUserSettingKey) {
            this.this$0 = this$0;
            this.mUserSettingKey = mUserSettingKey;
        }
        
        @Override
        public void run() {
            if (this.this$0.mStateMachine.isMenuAvailable()) {
                if (this.mUserSettingKey != null) {
                    switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[this.mUserSettingKey.ordinal()]) {
                        default: {
                            this.this$0.openUserSelectMenu(null);
                            break;
                        }
                        case 5: {
                            this.this$0.showMessageDialog(DialogId.RESET_CONFIRMATION, new Object[0]);
                            break;
                        }
                        case 4: {
                            if (HelpGuide.isHelpAppAvailable((Context)this.this$0.mActivity)) {
                                HelpGuide.startHelpApp((Context)this.this$0.mActivity);
                            }
                            else {
                                HelpGuide.startOnlineHelp((Context)this.this$0.mActivity);
                            }
                            this.this$0.mActivity.getLaunchCondition().clearExtraOperation();
                            break;
                        }
                        case 1:
                        case 2:
                        case 3: {
                            this.this$0.openUserSelectMenu(this.mUserSettingKey);
                            break;
                        }
                    }
                }
                else {
                    this.this$0.openUserSelectMenu(null);
                }
            }
        }
    }
    
    public static class AutoReviewContentReceiverProxy
    {
        private AutoReviewContent.ContentReceiver mReceiver;
        
        public void bindReceiver(final AutoReviewContent.ContentReceiver mReceiver) {
            this.mReceiver = mReceiver;
        }
        
        protected void notifyContent(final AutoReviewContent autoReviewContent) {
            this.mReceiver.onReceive(autoReviewContent);
        }
    }
    
    private static class EnumValueAccessorImpl<T extends UserSettingValue> implements EnumValueAccessor<T>
    {
        private final UserSettingKey mKey;
        private final UserSettings mSettings;
        
        private EnumValueAccessorImpl(final UserSettings mSettings, final UserSettingKey mKey) {
            this.mSettings = mSettings;
            this.mKey = mKey;
        }
        
        @Override
        public T get() {
            return (T)this.mSettings.get(this.mKey);
        }
        
        @Override
        public T reset() {
            return null;
        }
        
        @Override
        public void set(final T t) {
            this.mSettings.set(t);
        }
        
        @Override
        public T[] values() {
            return (T[])this.mSettings.getOptions(this.mKey);
        }
    }
    
    private class EvfLifeCycleCallback implements LifeCycleCallback
    {
        final ViewFinderImpl this$0;
        
        private EvfLifeCycleCallback(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onEvfFinalized(final Evf evf) {
            PerfLog.SURFACE_DESTROYED.transit();
            if (CamLog.DEBUG) {
                CamLog.d("onEvfFinalized() : E");
            }
            if (this.this$0.mCameraDevice == null) {
                CamLog.w("CameraDevice has already been released.");
                return;
            }
            this.this$0.mCameraDevice.stopPreview();
            if (CamLog.DEBUG) {
                CamLog.d("onEvfFinalized() : X");
            }
        }
        
        @Override
        public void onEvfInitialized(final Evf evf, final int i, final int j) {
            PerfLog.SURFACE_CREATED.transit();
            if (CamLog.DEBUG) {
                CamLog.d("onEvfInitialized() : E");
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onEvfInitialized():[IN] width=");
                sb.append(i);
                sb.append(", height=");
                sb.append(j);
                CamLog.d(sb.toString());
            }
            this.this$0.notifyOnEvfPrepared(new Rect(0, 0, i, j));
            if (CamLog.VERBOSE) {
                CamLog.d("onEvfInitialized():[OUT]");
            }
            if (CamLog.DEBUG) {
                CamLog.d("onEvfInitialized() : X");
            }
        }
        
        @Override
        public void onEvfSizeChanged(final Evf evf, final int i, final int j) {
            PerfLog.SURFACE_CHANGED.transit();
            if (CamLog.DEBUG) {
                CamLog.d("onEvfSizeChanged() : E");
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("surfaceChanged():[IN] width=");
                sb.append(i);
                sb.append(", height=");
                sb.append(j);
                CamLog.d(sb.toString());
            }
            this.this$0.notifyOnEvfPrepared(new Rect(0, 0, i, j));
            if (CamLog.VERBOSE) {
                CamLog.d("surfaceChanged():[OUT]");
            }
            if (CamLog.DEBUG) {
                CamLog.d("onEvfSizeChanged() : X");
            }
        }
    }
    
    private class FocusActionListenerImpl implements FocusActionListener
    {
        final ViewFinderImpl this$0;
        
        private FocusActionListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCanceled() {
            this.this$0.mBurstShootingRejectedReason = BurstRejectedReason.NONE;
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
        }
        
        @Override
        public void onFaceSelected(final Point point) {
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_CHANGE_SELECTED_FACE, point);
        }
        
        @Override
        public void onLongPressed() {
            final boolean oneShot = this.this$0.mActivity.isOneShot();
            if (this.this$0.isTouchCaptureEnabled() && this.this$0.isInternalStorageWritable() && (oneShot ^ true)) {
                if (!PlatformCapability.isManualBurstSupported(this.this$0.getCapturingMode().getCameraId())) {
                    if (PlatformCapability.isManualBurstSupported(CameraInfo.CameraId.BACK)) {
                        this.this$0.sendViewUpdateEvent(ViewUpdateEvent.EVENT_ON_BURST_REJECTED, BurstRejectedReason.CANNOT_BURST_USING_FRONT_CAMERA);
                    }
                    return;
                }
                if (this.this$0.mStateMachine.getUserSetting().get(UserSettingKey.FUSION_MODE) == FusionMode.ON) {
                    this.this$0.sendViewUpdateEvent(ViewUpdateEvent.EVENT_ON_BURST_REJECTED, BurstRejectedReason.CANNOT_BURST_DUE_TO_FUSION_MODE);
                    return;
                }
                this.this$0.hideAutoReview();
                this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_CAPTURE_BURST, new Object[0]);
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.TOUCH_CAPTURE);
            }
        }
        
        @Override
        public void onReleased() {
            if (this.this$0.isAutoReviewShowing()) {
                this.this$0.hideAutoReview();
                return;
            }
            if (!this.this$0.getCapturingMode().isVideo()) {
                if (this.this$0.isPhotoSelfTimerEnabled() && this.this$0.isPreviewLayout()) {
                    this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, Event.SelfTimerTrigger.NORMAL);
                }
                else {
                    this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_CAPTURE, new Object[0]);
                }
            }
            else if (!this.this$0.mStateMachine.isRecording()) {
                this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_START_RECORDING, new Object[0]);
            }
            ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.TOUCH_CAPTURE);
        }
        
        @Override
        public void onTouched() {
        }
    }
    
    private class GestureShutterListener implements WindowHost
    {
        private GestureShutterView mGestureShutterView;
        final ViewFinderImpl this$0;
        
        private GestureShutterListener(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
            this.mGestureShutterView = null;
        }
        
        private void setupGestureShutterView() {
            if (this.mGestureShutterView == null) {
                (this.mGestureShutterView = new GestureShutterView((Context)this.this$0.getActivity())).setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
            }
        }
        
        @Override
        public GestureShutterView getGestureShutterView() {
            this.setupGestureShutterView();
            return this.mGestureShutterView;
        }
        
        @Override
        public Point getPreviewSize() {
            return LayoutOrientationResolver.getInstance().getPointAccordingToLayoutOrientation(new Point(this.this$0.getBaseLayout().getPreviewContainer().getWidth(), this.this$0.getBaseLayout().getPreviewContainer().getHeight()));
        }
        
        @Override
        public Rect getViewFinderSize() {
            return LayoutDependencyResolver.getViewFinderSize((Context)this.this$0.mActivity);
        }
        
        @Override
        public void hideGestureShutterView() {
            if (this.mGestureShutterView != null) {
                this.this$0.getBaseLayout().getLazyInflatedUiComponentContainerBack().removeView((View)this.mGestureShutterView);
            }
        }
        
        @Override
        public void showGestureShutterView() {
            this.hideGestureShutterView();
            this.setupGestureShutterView();
            this.this$0.getBaseLayout().getLazyInflatedUiComponentContainerBack().addView((View)this.mGestureShutterView);
            this.this$0.getBaseLayout().getLazyInflatedUiComponentContainerBack().bringChildToFront((View)this.mGestureShutterView);
        }
    }
    
    public class HintTextListenerImpl implements HintTextContentListener
    {
        final ViewFinderImpl this$0;
        
        public HintTextListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        private void onClickSlowMotionDescription(final HintTextViewController hintTextViewController, final HintTextSlowMotionDescription hintTextSlowMotionDescription) {
            if (this.this$0.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.OFF) {
                return;
            }
            hintTextViewController.hide();
            final TutorialController tutorial = this.this$0.getBaseLayout().getTutorial();
            tutorial.open(TutorialController.OpenType.createByReadMore(hintTextSlowMotionDescription.getTutorialType()), null, new TutorialContentView.OnClickCloseButtonListener(this, hintTextViewController, hintTextSlowMotionDescription, tutorial) {
                final HintTextListenerImpl this$1;
                final HintTextSlowMotionDescription val$content;
                final HintTextViewController val$controller;
                final TutorialController val$tutorial;
                
                @Override
                public void onClickCloseButton(final View view) {
                    final int id = view.getId();
                    if (id != 2131296487) {
                        if (id == 2131296491) {
                            LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.SKIP);
                            LocalResearchUtil.getInstance().closeSetupWizard();
                        }
                    }
                    else {
                        LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.GOT_IT);
                        LocalResearchUtil.getInstance().closeSetupWizard();
                    }
                    this.val$controller.cancel(this.val$content.getTag());
                    this.val$tutorial.close();
                    if (this.this$1.this$0.mHintText != null) {
                        this.this$1.this$0.mHintText.showAll();
                    }
                    this.this$1.this$0.setApplicationNavigatorEnabled(this.this$1.this$0.mActivity.isOneShot() ^ true);
                    this.this$1.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
                }
            });
            this.this$0.changeLayoutTo(BaseLayoutPattern.CLEAR);
            this.this$0.setApplicationNavigatorEnabled(false);
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_OPENED, new Object[0]);
        }
        
        private void onClickThermalReadMore() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.this$0.getString(2131689843));
            if (this.this$0.getCapturingMode() == CapturingMode.SCENE_RECOGNITION || this.this$0.getCapturingMode() == CapturingMode.SUPERIOR_FRONT || this.this$0.getCapturingMode() == CapturingMode.VIDEO || this.this$0.getCapturingMode() == CapturingMode.FRONT_VIDEO) {
                sb.append(System.lineSeparator());
                sb.append(this.this$0.getString(2131689643));
            }
            if (!this.this$0.getCapturingMode().isFront()) {
                sb.append(System.lineSeparator());
                sb.append(this.this$0.getString(2131689847));
            }
            if (this.this$0.getCapturingMode().isFront() && !this.this$0.getCapturingMode().isVideo()) {
                sb.append(System.lineSeparator());
                sb.append(this.this$0.getString(2131689868));
            }
            if (this.this$0.isPredictiveCaptureAvailable()) {
                sb.append(System.lineSeparator());
                sb.append(this.this$0.getString(2131690005));
            }
            final MessageDialogRequest messageDialogRequest = new MessageDialogRequest();
            messageDialogRequest.mDialogId = DialogId.COOLING_MODE;
            messageDialogRequest.mMessageList = sb.toString();
            this.this$0.mMessageDialog.request(messageDialogRequest);
        }
        
        @Override
        public void onContentButtonClick(final HintTextViewController hintTextViewController, final HintTextContent hintTextContent) {
            if (hintTextContent instanceof HintTextThermal) {
                this.onClickThermalReadMore();
            }
            else if (hintTextContent instanceof HintTextSlowMotionDescription) {
                this.onClickSlowMotionDescription(hintTextViewController, (HintTextSlowMotionDescription)hintTextContent);
            }
        }
        
        @Override
        public void onStateChanged() {
            this.this$0.updateVisibilityForSpecificDisplaySize();
        }
    }
    
    private class LocationAcquiredListenerImpl implements LocationAcquiredListener
    {
        final ViewFinderImpl this$0;
        
        private LocationAcquiredListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onAcquired(final boolean b, final boolean b2) {
            if (!this.this$0.isHeadUpDisplayReady()) {
                return;
            }
            this.this$0.mBaseLayout.getGeoTagIndicator().isAcquired(b || b2);
        }
        
        @Override
        public void onDisabled() {
            this.this$0.mActivity.getStoredSettings().getUserSettings().set(Geotag.OFF);
            this.this$0.mBaseLayout.getGeoTagIndicator().set(false);
            this.this$0.mActivity.readLocationSettings();
        }
        
        @Override
        public void onLost() {
            if (!this.this$0.isHeadUpDisplayReady()) {
                return;
            }
            this.this$0.mBaseLayout.getGeoTagIndicator().isAcquired(false);
        }
    }
    
    private class MessageDialogOnCancelListenerImpl implements MessageDialogOnCancelListener
    {
        final ViewFinderImpl this$0;
        
        private MessageDialogOnCancelListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCancel(final MessageDialogRequest messageDialogRequest) {
            final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()];
            if (n != 12) {
                switch (n) {
                    case 8: {
                        this.this$0.openSettingMenuDialogInChina();
                        this.this$0.updateLocation();
                        break;
                    }
                    case 7: {
                        this.this$0.updateLocation();
                        break;
                    }
                    case 6: {
                        this.this$0.showHiSpeedSdCardRecommendDialogOnVideoSizeChange();
                        break;
                    }
                }
            }
            else {
                this.this$0.mStateMachine.getUserSetting().set(SideSense.OFF);
            }
        }
    }
    
    private class MessageDialogOnClickNegativeListenerImpl implements MessageDialogOnClickListener
    {
        final ViewFinderImpl this$0;
        
        private MessageDialogOnClickNegativeListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onClick(final MessageDialogRequest messageDialogRequest) {
            final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()];
            if (n != 12) {
                switch (n) {
                    case 8: {
                        this.this$0.openSettingMenuDialogInChina();
                        this.this$0.updateLocation();
                        break;
                    }
                    case 7: {
                        this.this$0.updateLocation();
                        break;
                    }
                }
            }
            else {
                this.this$0.mStateMachine.getUserSetting().set(SideSense.OFF);
            }
        }
    }
    
    public class MessageDialogOnClickPositiveListenerImpl implements MessageDialogOnClickListener
    {
        final ViewFinderImpl this$0;
        
        public MessageDialogOnClickPositiveListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onClick(final MessageDialogRequest messageDialogRequest) {
            switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()]) {
                case 14:
                case 15: {
                    PermissionsUtil.requestSdCardGranted(this.this$0.mActivity, 20, StorageUtil.getVolumeUuid(Storage.StorageType.EXTERNAL_CARD, this.this$0.mActivity.getApplicationContext()));
                    break;
                }
                case 13: {
                    this.this$0.mActivity.requestRestartCameraActivityAfterResetSettings();
                    break;
                }
                case 12: {
                    this.this$0.launchSideSenseSettings();
                    break;
                }
                case 11: {
                    this.this$0.openUserSelectMenu(UserSettingKey.DESTINATION_TO_SAVE);
                    this.this$0.mSettingUi.updateSettingMenu(false);
                    break;
                }
                case 10: {
                    final Intent intent = (Intent)messageDialogRequest.mOptions[0];
                    final Bundle bundle = (Bundle)messageDialogRequest.mOptions[1];
                    this.this$0.sendViewUpdateEvent(ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT, messageDialogRequest.mOptions[2]);
                    this.this$0.requestStartActivityForMessageDialog(intent, bundle);
                    break;
                }
                case 7:
                case 8: {
                    this.this$0.mStateMachine.getUserSetting().set(Geotag.ON);
                    this.this$0.launchLocationSourceSettings();
                    break;
                }
                case 6: {
                    this.this$0.showHiSpeedSdCardRecommendDialogOnVideoSizeChange();
                    break;
                }
                case 5: {
                    final Object[] mOptions = messageDialogRequest.mOptions;
                    if (mOptions != null) {
                        this.this$0.mActivity.requestLaunchAdvancedCamera(LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU, (String)mOptions[0]);
                        break;
                    }
                    this.this$0.mActivity.requestLaunchAdvancedCamera(LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU, null);
                    break;
                }
                case 1:
                case 2:
                case 3:
                case 4:
                case 9: {
                    this.this$0.openUserSelectMenu(UserSettingKey.DESTINATION_TO_SAVE);
                    break;
                }
            }
        }
    }
    
    private class MessageDialogOnDismissListenerImpl implements MessageDialogOnDismissListener
    {
        final ViewFinderImpl this$0;
        
        private MessageDialogOnDismissListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onDismiss(final MessageDialogRequest messageDialogRequest) {
            if (messageDialogRequest.mDialogId != DialogId.PREDICTIVE_LAUNCH_DESCRIPTION) {
                this.this$0.hidePredictiveLaunchCover(PredictiveLaunchHideTrigger.OTHER);
            }
            final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()];
            if (n != 8) {
                Label_0249: {
                    if (n != 13) {
                        Label_0239: {
                            switch (n) {
                                default: {
                                    switch (n) {
                                        default: {
                                            return;
                                        }
                                        case 31: {
                                            this.this$0.mActivity.setupAutoPowerOffTimeOutDuration(this.this$0.predictiveLaunchCoverExists());
                                            this.this$0.mActivity.restartAutoPowerOffTimer();
                                            return;
                                        }
                                        case 22:
                                        case 23:
                                        case 24:
                                        case 25: {
                                            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_CLOSED, UiComponentKind.FATAL_ALERT_DIALOG);
                                            return;
                                        }
                                        case 18:
                                        case 19:
                                        case 20:
                                        case 21: {
                                            this.this$0.exitByError();
                                            return;
                                        }
                                        case 16:
                                        case 17: {
                                            PlatformCapability.setDeviceError(true);
                                            this.this$0.exitByError();
                                            return;
                                        }
                                        case 26:
                                        case 27:
                                        case 28: {
                                            break Label_0239;
                                        }
                                        case 29:
                                        case 30: {
                                            break Label_0249;
                                        }
                                    }
                                    break;
                                }
                                case 1:
                                case 2:
                                case 3:
                                case 4: {
                                    this.this$0.onCloseStorageDialog();
                                    return;
                                }
                            }
                        }
                    }
                }
                this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_CLOSED, UiComponentKind.NOTICE_DIALOG);
            }
            else {
                this.this$0.openSettingMenuDialogInChina();
            }
        }
    }
    
    private class MessageDialogOnOpenListenerImpl implements MessageDialogOnOpenListener
    {
        final ViewFinderImpl this$0;
        
        private MessageDialogOnOpenListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onOpen(final MessageDialogRequest messageDialogRequest) {
            final int n = ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()];
            Label_0097: {
                if (n != 13) {
                    Label_0087: {
                        switch (n) {
                            default: {
                                switch (n) {
                                    default: {
                                        return;
                                    }
                                    case 26:
                                    case 27:
                                    case 28: {
                                        break Label_0087;
                                    }
                                    case 29:
                                    case 30: {
                                        break Label_0097;
                                    }
                                }
                                break;
                            }
                            case 1:
                            case 2:
                            case 3:
                            case 4: {
                                this.this$0.onOpenStorageDialog();
                                return;
                            }
                        }
                    }
                }
            }
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_OPENED, UiComponentKind.NOTICE_DIALOG);
        }
    }
    
    private class OnAutoReviewEventListenerImpl implements OnAutoReviewEventListener
    {
        final ViewFinderImpl this$0;
        
        private OnAutoReviewEventListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onAutoReviewClosed() {
            this.this$0.updateAllOverlayControlVisibility();
            this.this$0.updateVisibilityForSpecificDisplaySize();
        }
    }
    
    private class OnClickThumbnailProgressListenerImpl implements OnClickThumbnailProgressListener
    {
        final ViewFinderImpl this$0;
        
        private OnClickThumbnailProgressListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onClickThumbnailProgress() {
            if (CamLog.VERBOSE) {
                CamLog.d("onClickThumbnailProgress");
            }
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_TOUCH_CONTENT_PROGRESS, new Object[0]);
        }
    }
    
    private class OnHighSensitivityFusionButtonStateListener implements OnScreenButtonListener
    {
        final ViewFinderImpl this$0;
        
        private OnHighSensitivityFusionButtonStateListener(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCancel(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
        }
        
        @Override
        public void onDown(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
        }
        
        @Override
        public void onLongPress(final OnScreenButton onScreenButton) {
        }
        
        @Override
        public void onMove(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
        }
        
        @Override
        public void onUp(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
            if (this.this$0.getCapturingMode() == CapturingMode.NORMAL && this.this$0.mStateMachine.isSettingChangeAcceptable() && this.this$0.isUserOperable()) {
                this.this$0.mSettingDialogStack.closeCurrentDialog();
                if (this.this$0.openTutorial(TutorialController.DisplayTrigger.CHANGE_MANUAL_FUSION_SETTING)) {
                    this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_OPENED, new Object[0]);
                }
                else {
                    this.this$0.updateHighSensitivityFusionModeForManual();
                }
                return;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("HighSensitivityFusion button was tapped in mode change");
            }
        }
    }
    
    public class OnScreenImageQualityControlButtonListener implements OnScreenButtonListener
    {
        final ViewFinderImpl this$0;
        
        public OnScreenImageQualityControlButtonListener(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCancel(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
        }
        
        @Override
        public void onDown(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
        }
        
        @Override
        public void onLongPress(final OnScreenButton onScreenButton) {
        }
        
        @Override
        public void onMove(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
        }
        
        @Override
        public void onUp(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
            if (this.this$0.getBaseLayout().getImageQualityControl().get().isEnabled()) {
                this.this$0.disableOverlayControl((BaseLayout.LazyInitializer)this.this$0.getBaseLayout().getImageQualityControl());
            }
            else {
                this.this$0.enableOverlayControl((BaseLayout.LazyInitializer)this.this$0.getBaseLayout().getImageQualityControl());
            }
        }
    }
    
    private class OverlayControlStateListener implements StateListener
    {
        private final UiComponentKind mKind;
        final ViewFinderImpl this$0;
        
        public OverlayControlStateListener(final ViewFinderImpl this$0, final UiComponentKind mKind) {
            this.this$0 = this$0;
            this.mKind = mKind;
        }
        
        @Override
        public void onValueUpdateEnd() {
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_CLOSED, this.mKind);
        }
        
        @Override
        public void onValueUpdateStart() {
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_OPENED, this.mKind);
        }
    }
    
    private class PostUiInflatedTask implements Runnable
    {
        final ViewFinderImpl this$0;
        
        private PostUiInflatedTask(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.setupAnimations();
        }
    }
    
    private class PredictiveLaunchCoverTouchListenerImpl implements PredictiveLaunchCoverTouchListener
    {
        final ViewFinderImpl this$0;
        
        private PredictiveLaunchCoverTouchListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCircleTouched() {
            this.this$0.mTouchEventDispatcher.sendTouchUp(UserEventHandler.UiComponent.PREDICTIVE_LAUNCH_COVER, null);
        }
    }
    
    public enum PredictiveLaunchHideTrigger
    {
        private static final PredictiveLaunchHideTrigger[] $VALUES;
        
        HW_CAMERA_KEY(Event.PredictiveLaunchAction.HW_CAMERA_KEY), 
        OTHER(Event.PredictiveLaunchAction.OTHER), 
        SIDE_SENSING(Event.PredictiveLaunchAction.SIDE_SENSING), 
        TOUCH_UP(Event.PredictiveLaunchAction.TOUCH_UP), 
        TOUCH_UP_CAPTURE(Event.PredictiveLaunchAction.TOUCH_UP), 
        VOLUME_KEY_SHUTTER(Event.PredictiveLaunchAction.VOLUME_KEY), 
        VOLUME_KEY_ZOOM(Event.PredictiveLaunchAction.VOLUME_KEY);
        
        public final Event.PredictiveLaunchAction mAction;
        
        static {
            $VALUES = new PredictiveLaunchHideTrigger[] { PredictiveLaunchHideTrigger.TOUCH_UP, PredictiveLaunchHideTrigger.TOUCH_UP_CAPTURE, PredictiveLaunchHideTrigger.HW_CAMERA_KEY, PredictiveLaunchHideTrigger.VOLUME_KEY_SHUTTER, PredictiveLaunchHideTrigger.VOLUME_KEY_ZOOM, PredictiveLaunchHideTrigger.SIDE_SENSING, PredictiveLaunchHideTrigger.OTHER };
        }
        
        private PredictiveLaunchHideTrigger(final Event.PredictiveLaunchAction mAction) {
            this.mAction = mAction;
        }
    }
    
    private class ReTrySetupHeadUpDisplayTask implements Runnable
    {
        final ViewFinderImpl this$0;
        
        private ReTrySetupHeadUpDisplayTask(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (this.this$0.mStateMachine == null) {
                return;
            }
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, false);
        }
    }
    
    public static class RecordingTimeReceiverProxy
    {
        private int mCurrentTime;
        private RecordingTimeIndicator mReceiver;
        
        public void bindReceiver(final RecordingTimeIndicator mReceiver) {
            this.mReceiver = mReceiver;
        }
        
        public int getCurrentTime() {
            return this.mCurrentTime;
        }
        
        protected void notifyOnTimeTicked(final int mCurrentTime) {
            this.mCurrentTime = mCurrentTime;
            if (this.mReceiver == null) {
                return;
            }
            this.mReceiver.onTimeTicked(mCurrentTime);
        }
        
        protected void reset() {
            this.mCurrentTime = 0;
        }
    }
    
    private class ReviewWindowListenerImpl implements ReviewWindowListener
    {
        final ViewFinderImpl this$0;
        
        private ReviewWindowListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onReviewWindowClose() {
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
        }
        
        @Override
        public void onReviewWindowOpen() {
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_OPENED, UiComponentKind.REVIEW_WINDOW);
        }
    }
    
    private class ScreenButtonHandler
    {
        final ViewFinderImpl this$0;
        
        private ScreenButtonHandler(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        private void clearAllButton() {
            this.clearOption1();
            this.clearOption2();
            this.clearMain();
        }
        
        private void clearMain() {
            this.this$0.getBaseLayout().getOnScreenButtonGroup().clearMain();
        }
        
        private void clearOption1() {
            this.this$0.getBaseLayout().getOnScreenButtonGroup().clearOption1();
        }
        
        private void clearOption2() {
            this.this$0.getBaseLayout().getOnScreenButtonGroup().clearOption2();
        }
        
        private void refreshButton() {
            this.clearOption1();
            this.clearOption2();
            this.this$0.getBaseLayout().getOnScreenButtonGroup().show();
        }
        
        private void setMainRotatability(final int n, final boolean b) {
            this.this$0.getBaseLayout().getOnScreenButtonGroup().setMainRotatability(n, b);
        }
        
        protected void setMain(final OnScreenButtonItemFactory.ButtonType buttonType, final int n, final boolean b) {
            this.setMain(buttonType, n, b, true);
        }
        
        protected void setMain(final OnScreenButtonItemFactory.ButtonType buttonType, final int n, final boolean b, final boolean b2) {
            final OnScreenButtonGroup onScreenButtonGroup = this.this$0.getBaseLayout().getOnScreenButtonGroup();
            OnScreenButtonListenerImpl onScreenButtonListenerImpl;
            if (b2) {
                onScreenButtonListenerImpl = new OnScreenButtonListenerImpl(buttonType);
            }
            else {
                onScreenButtonListenerImpl = null;
            }
            onScreenButtonGroup.setMain(OnScreenButtonItemFactory.createButton(buttonType, onScreenButtonListenerImpl), n, b);
        }
        
        protected void setOption1(final OnScreenButtonGroup.Item item, final int n, final boolean b) {
            this.this$0.getBaseLayout().getOnScreenButtonGroup().setOption1(item, n, b);
        }
        
        protected void setOption1(final OnScreenButtonItemFactory.ButtonType buttonType, final int n, final boolean b) {
            this.this$0.getBaseLayout().getOnScreenButtonGroup().setOption1(OnScreenButtonItemFactory.createButton(buttonType, new OnScreenButtonListenerImpl(buttonType)), n, b);
        }
        
        public void setOption2(final OnScreenButtonGroup.Item item, final int n, final boolean b) {
            this.this$0.getBaseLayout().getOnScreenButtonGroup().setOption2(item, n, b);
        }
        
        public void setOption2(final OnScreenButtonItemFactory.ButtonType buttonType, final int n, final boolean b) {
            this.this$0.getBaseLayout().getOnScreenButtonGroup().setOption2(OnScreenButtonItemFactory.createButton(buttonType, new OnScreenButtonListenerImpl(buttonType)), n, b);
        }
        
        private class OnScreenButtonListenerImpl implements OnScreenButtonListener
        {
            private final OnScreenButtonItemFactory.ButtonType mButtonType;
            final ScreenButtonHandler this$1;
            
            public OnScreenButtonListenerImpl(final ScreenButtonHandler this$1, final OnScreenButtonItemFactory.ButtonType mButtonType) {
                this.this$1 = this$1;
                this.mButtonType = mButtonType;
            }
            
            @Override
            public void onCancel(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
                this.this$1.this$0.mTouchEventDispatcher.sendCancel(this.mButtonType);
            }
            
            @Override
            public void onDown(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
                this.this$1.this$0.mTouchEventDispatcher.sendTouchDown(this.mButtonType);
            }
            
            @Override
            public void onLongPress(final OnScreenButton onScreenButton) {
                this.this$1.this$0.mTouchEventDispatcher.sendLongClick(this.mButtonType, null);
            }
            
            @Override
            public void onMove(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
            }
            
            @Override
            public void onUp(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
                this.this$1.this$0.mTouchEventDispatcher.sendTouchUp(this.mButtonType, new Point((int)motionEvent.getX(), (int)motionEvent.getY()));
            }
        }
    }
    
    private class SettingDialogListenerImpl implements SettingDialogListener
    {
        final ViewFinderImpl this$0;
        
        private SettingDialogListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCloseSettingDialog(final Object o) {
            if (this.this$0.isAllDialogClosed() && !this.this$0.isTutorialOpened()) {
                this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
            }
        }
        
        @Override
        public void onOpenSettingDialog(final Object o) {
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_OPENED, new Object[0]);
        }
    }
    
    private class SettingMenuExclusiveListener implements ExclusiveViewListener
    {
        final ViewFinderImpl this$0;
        
        private SettingMenuExclusiveListener(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public boolean isExclusiveView(final View view, final MotionEvent motionEvent) {
            return this.this$0.isExclusiveViewEvent(view, motionEvent);
        }
    }
    
    private class ShowMessageDialogTask implements Runnable
    {
        private MessageDialogRequest mRequestParam;
        final ViewFinderImpl this$0;
        
        public ShowMessageDialogTask(final ViewFinderImpl this$0, final DialogId mDialogId, final Object... mOptions) {
            this.this$0 = this$0;
            this.mRequestParam = new MessageDialogRequest();
            this.mRequestParam.mDialogId = mDialogId;
            this.mRequestParam.mOptions = mOptions;
        }
        
        @Override
        public void run() {
            if (!this.this$0.mIsSetupHeadupDisplayInvoked) {
                this.this$0.mMessageDialog.setSensorOrientation(this.this$0.mActivity.getOrientation());
            }
            if (!this.this$0.mMessageDialog.request(this.mRequestParam)) {
                switch (ViewFinderImpl$32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[this.mRequestParam.mDialogId.ordinal()]) {
                    case 6: {
                        this.this$0.showHiSpeedSdCardRecommendDialogOnVideoSizeChange();
                        break;
                    }
                    case 5: {
                        this.this$0.mActivity.requestLaunchAdvancedCamera(LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU, (String)this.mRequestParam.mOptions[0]);
                        break;
                    }
                }
            }
        }
    }
    
    public class SideTouchUiButtonListenerFactory
    {
        final ViewFinderImpl this$0;
        
        public SideTouchUiButtonListenerFactory(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        public OnScreenButtonListener create(final OnScreenButtonItemFactory.ButtonType buttonType) {
            return new OnScreenButtonListenerImpl(buttonType);
        }
        
        private class OnScreenButtonListenerImpl implements OnScreenButtonListener
        {
            private final OnScreenButtonItemFactory.ButtonType mButtonType;
            final SideTouchUiButtonListenerFactory this$1;
            
            public OnScreenButtonListenerImpl(final SideTouchUiButtonListenerFactory this$1, final OnScreenButtonItemFactory.ButtonType mButtonType) {
                this.this$1 = this$1;
                this.mButtonType = mButtonType;
            }
            
            @Override
            public void onCancel(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
                this.this$1.this$0.mTouchEventDispatcher.sendCancel(this.mButtonType);
            }
            
            @Override
            public void onDown(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
                this.this$1.this$0.mTouchEventDispatcher.sendTouchDown(this.mButtonType);
            }
            
            @Override
            public void onLongPress(final OnScreenButton onScreenButton) {
                this.this$1.this$0.mTouchEventDispatcher.sendLongClick(this.mButtonType, null);
            }
            
            @Override
            public void onMove(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
            }
            
            @Override
            public void onUp(final OnScreenButton onScreenButton, final MotionEvent motionEvent) {
                this.this$1.this$0.mTouchEventDispatcher.sendTouchUp(this.mButtonType, new Point((int)motionEvent.getX(), (int)motionEvent.getY()));
            }
        }
    }
    
    public class ViewFinderAccessorForShortcut
    {
        final ViewFinderImpl this$0;
        
        public ViewFinderAccessorForShortcut(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        public boolean isShortcutButtonClickable() {
            return this.this$0.mIsSettingChangeAcceptable && !this.this$0.isTutorialOpened() && this.this$0.isUserOperable();
        }
        
        public void openSettingMenuDialog() {
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_KEY_MENU, new Object[0]);
        }
        
        public void openShorcutDialog(final UiComponentKind uiComponentKind) {
            this.this$0.mStateMachine.sendEvent(TransitterEvent.EVENT_DIALOG_OPENED, uiComponentKind);
        }
        
        public void showRestrictMessageDialog(final UserSettingKey userSettingKey) {
            this.this$0.showMessageDialog(userSettingKey.getRestrictMessageDialogId(this.this$0.mStateMachine.getUserSetting()), new Object[0]);
        }
        
        public void switchCamera() {
            this.this$0.setFrontAngleSwitchButtonClickable(false);
            this.this$0.hideAutoReview();
            this.this$0.setIsCameraSwitching(true);
            this.this$0.hideSurface();
            CameraApplication.getUiThreadHandler().post((Runnable)new Runnable(this) {
                final ViewFinderAccessorForShortcut this$1;
                
                @Override
                public void run() {
                    this.this$1.this$0.onToggleCameraSwitch();
                    this.this$1.this$0.setFrontAngleSwitchButtonClickable(this.this$1.this$0.isFront());
                }
            });
        }
        
        public void switchSemiAutoAvailability() {
            this.this$0.switchSemiAutoAvailability();
        }
    }
    
    private class ViewFinderStateListener implements CaptureAreaStateListener
    {
        final ViewFinderImpl this$0;
        
        private ViewFinderStateListener(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCaptureAreaCanceled() {
            this.this$0.mTouchEventDispatcher.sendCancel(UserEventHandler.UiComponent.CAPTURE_AREA);
        }
        
        @Override
        public void onCaptureAreaIsReadyToScale() {
            if (this.this$0.isFront()) {
                if (this.this$0.getCurrentLayoutPattern() != BaseLayoutPattern.SELFTIMER) {
                    this.this$0.notifyZoomOperationRejected();
                }
                return;
            }
            this.this$0.mTouchEventDispatcher.sendCaptureAreaScaleReady(UserEventHandler.UiComponent.CAPTURE_AREA);
        }
        
        @Override
        public void onCaptureAreaLongPressed(final Point point) {
            this.this$0.mTouchEventDispatcher.sendLongClick(UserEventHandler.UiComponent.CAPTURE_AREA, point);
        }
        
        @Override
        public void onCaptureAreaMoved() {
        }
        
        @Override
        public void onCaptureAreaReleased(final Point point) {
            this.this$0.mTouchEventDispatcher.sendTouchUp(UserEventHandler.UiComponent.CAPTURE_AREA, point);
        }
        
        @Override
        public void onCaptureAreaScaled(final float n) {
            if (this.this$0.isFront()) {
                return;
            }
            this.this$0.mTouchEventDispatcher.sendCaptureAreaScaling(UserEventHandler.UiComponent.CAPTURE_AREA, n);
        }
        
        @Override
        public void onCaptureAreaSingleTapUp(final Point point) {
            this.this$0.mTouchEventDispatcher.sendClick(UserEventHandler.UiComponent.CAPTURE_AREA, point);
        }
        
        @Override
        public void onCaptureAreaStopped() {
        }
        
        @Override
        public void onCaptureAreaTouched() {
            this.this$0.mTouchEventDispatcher.sendTouchDown(UserEventHandler.UiComponent.CAPTURE_AREA);
        }
    }
    
    public static class ZoomBarUpdateProxy
    {
        private Zoombar mZoomBar;
        
        public void bindZoomBar(final Zoombar mZoomBar) {
            this.mZoomBar = mZoomBar;
        }
        
        protected int update(final List<Integer> zoomRatios, final int n) {
            this.mZoomBar.setZoomRatios(zoomRatios);
            return this.mZoomBar.zoom(n);
        }
    }
    
    private class ZoombarDisplayChangedListenerImpl implements ZoombarDisplayChangedListener
    {
        final ViewFinderImpl this$0;
        
        private ZoombarDisplayChangedListenerImpl(final ViewFinderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onShowZoombar() {
            this.this$0.getBaseLayout().getTopIndicator().setVisibility(4);
        }
        
        @Override
        public void onZoombarHidden() {
            this.this$0.getBaseLayout().getTopIndicator().setVisibility(0);
            if (this.this$0.mSideTouchUi.detachTo(SideTouchUi.Type.COVERING)) {
                if (this.this$0.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR)) {
                    this.this$0.changeLayoutTo(BaseLayoutPattern.RECORDING, true);
                }
                if (this.this$0.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE)) {
                    this.this$0.changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING, true);
                }
            }
        }
    }
}
