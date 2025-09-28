package com.sonyericsson.android.camera.view;

import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.NavigatorContents;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.SideTouchEventDetector;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoSmileCapture;
import com.sonyericsson.android.camera.controller.AbstractDraggingEventHandler;
import com.sonyericsson.android.camera.controller.ChapterThumbnail;
import com.sonyericsson.android.camera.controller.GestureShutter;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.controller.VibrationManager;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import com.sonyericsson.android.camera.controller.xperiaxloops.XperiaXLoopsManager;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.android.camera.setting.UiControlSettings;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.CoordinateUtil;
import com.sonyericsson.android.camera.util.HelpGuide;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.view.AutoReviewContent;
import com.sonyericsson.android.camera.view.AutoReviewController;
import com.sonyericsson.android.camera.view.CaptureArea;
import com.sonyericsson.android.camera.view.LayoutAsyncInflateItems;
import com.sonyericsson.android.camera.view.SuperSlowMotionTriggerAnimationController;
import com.sonyericsson.android.camera.view.ToastContent;
import com.sonyericsson.android.camera.view.UserEventHandler;
import com.sonyericsson.android.camera.view.ViewFinder;
import com.sonyericsson.android.camera.view.animation.AnimationRequest;
import com.sonyericsson.android.camera.view.animation.TransitionAnimationController;
import com.sonyericsson.android.camera.view.baselayout.BaseLayout;
import com.sonyericsson.android.camera.view.baselayout.BaseLayoutPattern;
import com.sonyericsson.android.camera.view.baselayout.BaseLayoutPatternApplier;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.baselayout.LayoutPatternApplier;
import com.sonyericsson.android.camera.view.baselayout.PredictiveLaunchCoverView;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButton;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonItemFactory;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener;
import com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar;
import com.sonyericsson.android.camera.view.hint.HintTextAutoPowerOff;
import com.sonyericsson.android.camera.view.hint.HintTextContent;
import com.sonyericsson.android.camera.view.hint.HintTextHighSensitivityFusionCondition;
import com.sonyericsson.android.camera.view.hint.HintTextHighSensitivityFusionStatus;
import com.sonyericsson.android.camera.view.hint.HintTextSlowMotionDescription;
import com.sonyericsson.android.camera.view.hint.HintTextStandardSlowMotion;
import com.sonyericsson.android.camera.view.hint.HintTextStandardSlowMotionDescription;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowMotion;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowMotionDescription;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowMotionVideoRecording;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowShot;
import com.sonyericsson.android.camera.view.hint.HintTextSuperSlowShotDescription;
import com.sonyericsson.android.camera.view.hint.HintTextThermal;
import com.sonyericsson.android.camera.view.hint.HintTextThermalWarning;
import com.sonyericsson.android.camera.view.hint.HintTextTimedOutMessage;
import com.sonyericsson.android.camera.view.hint.HintTextViewController;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogController;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogRequest;
import com.sonyericsson.android.camera.view.modeselector.AddonMode;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeUtil;
import com.sonyericsson.android.camera.view.modeselector.InternalMode;
import com.sonyericsson.android.camera.view.modeselector.LaunchCameraIntentBuilder;
import com.sonyericsson.android.camera.view.modeselector.Mode;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.android.camera.view.overlaycontrol.EnumValueAccessor;
import com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl;
import com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import com.sonyericsson.android.camera.view.setting.SettingDialogStack;
import com.sonyericsson.android.camera.view.setting.SettingUi;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener;
import com.sonyericsson.android.camera.view.sidetouch.SideTouchUi;
import com.sonyericsson.android.camera.view.tutorial.TutorialContentView;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationFactory;
import com.sonyericsson.cameracommon.capturefeedback.contextview.GLSurfaceContextView;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.focusview.FocusActionListener;
import com.sonyericsson.cameracommon.focusview.FocusRectangles;
import com.sonyericsson.cameracommon.focusview.FocusRectanglesViewList;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener;
import com.sonyericsson.cameracommon.review.ReviewWindowListener;
import com.sonyericsson.cameracommon.storage.PhotoSavingRequest;
import com.sonyericsson.cameracommon.storage.SavingRequest;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameracommon.utility.RegionConfig;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import com.sonyericsson.cameracommon.utility.ViewUtility;
import com.sonyericsson.cameracommon.viewfinder.InflateItem;
import com.sonyericsson.cameracommon.viewfinder.InflateTask;
import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;
import com.sonyericsson.cameracommon.viewfinder.ViewFinderInterface;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingIndicator;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingTimeIndicator;
import com.sonymobile.cameracommon.evf.Evf;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonymobile.cameracommon.view.RecognizedCondition;
import com.sonymobile.cameracommon.view.RecognizedScene;
import com.sonymobile.cameracommon.view.SelfTimerCountDownView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ViewFinderImpl implements StateMachine.OnStateChangedListener, ViewFinder, ViewFinderInterface, CameraActivity.LayoutOrientationChangedListener {
    private static final int AUTO_POWER_OFF_HINT_TEXT_TIME_OUT_TIME_MILLIS = 10000;
    private static final int COLOR_VALUE_MAX = 255;
    private static final List<DialogId> STORAGE_DIALOG_LIST = Arrays.asList(DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL, DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL, DialogId.MEMORY_FULL, DialogId.MEMORY_SD_UNAVAILABLE, DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_SD, DialogId.MEMORY_INTERNAL_UNAVAILABLE, DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD);
    private static final String TAG = "ViewFinderImpl";
    private static final String THREAD_NAME = "InflateTask";
    private static final float VIEW_FINDER_DUSKY = 0.5f;
    private CameraActivity mActivity;
    private TransitionAnimationController mAnimationController;
    private ApplicationNavigator mApplicationNavigator;
    private AutoReviewContentReceiverProxy mAutoReviewProxy;
    private StoreDataResult mAutoReviewStoreData;
    private BaseLayout mBaseLayout;
    private BurstCountView mBurstCountView;
    private CapturingMode mCapturingModeWhenLastSetupHeadDisplay;
    private ViewFinder.UiComponentKind mCurrentDisplayingUiComponent;
    private Evf mEvf;
    private final Evf.LifeCycleCallback mEvfLifeCycleCallback;
    private FocusRectangles mFocusRectangles;
    private FrontAngleSwitchButton mFrontAngleSwitchButton;
    private OnScreenButtonGroup.MutableButtonItem mHighSensitivityFusionButtonItem;
    private HintTextViewController mHintText;
    private OnScreenButtonGroup.MutableButtonItem mImageQualityControlButtonItem;
    private Future<Map<InflateItem, List<View>>> mInflateFuture;
    private Map<InflateItem, List<View>> mInflateItemMap;
    private InstantViewer mInstantViewer;
    private boolean mIsAlreadySlowMotionLearnMoreButtonDisplayed;
    private Boolean mIsFaceDetectionIdSupported;
    private boolean mIsFrontAngleChanging;
    private boolean mIsModeChanging;
    private boolean mIsRequestingStartActivity;
    private boolean mIsSettingChangeAcceptable;
    private boolean mIsSetupHeadupDisplayInvoked;
    private LayoutPattern mLayoutPattern;
    private LayoutPatternApplier mLayoutPatternApplier;
    private XperiaXLoopsManager mLoopsManager;
    private final MessageDialogController mMessageDialog;
    private ModeLoader mModeLoader;
    private View.OnTouchListener mOnFocusRectangleTouchListener;
    private final PostUiInflatedTask mPostUiInflatedTask;
    private View mPreInflatedHeadUpDisplay;
    private View mPreviewCover;
    private PrimaryShortcutGroup mPrimaryShortcutGroup;
    private RecordingTimeReceiverProxy mRecordingTimeProxy;
    private View mSavingProgressBar;
    private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private final ScreenButtonHandler mScreenButtonHandler;
    private SelfTimerCountDownView mSelfTimerCountDownView;
    private SelfTimerCountDownView mSelfTimerCountDownViewNext;
    private SettingDialogStack mSettingDialogStack;
    private SettingMenuExclusiveListener mSettingMenuExclusiveListener;
    private SettingUi mSettingUi;
    private SideTouchUi mSideTouchUi;
    private StateMachine mStateMachine;
    private View mSurfaceBlinderView;
    private final ToastContent mToastContent;
    private final UiControlSettings mUiControlSettings;
    private CaptureArea mViewFinderCaptureArea;
    private View mWindowDisplayFlashScreen;
    private ZoomBarUpdateProxy mZoomBarProxy;
    private CameraDeviceHandler mCameraDevice = null;
    private SelfTimer mPhotoSelfTimerSetting = SelfTimer.OFF;
    private ShutterTrigger mShutterTrigger = ShutterTrigger.OFF;
    private TouchCapture mTouchCapture = null;
    private CaptureFeedback mCaptureFeedback = null;
    private final Rect mGlobalVisibleRect = new Rect();
    private int mRecordingOrientation = 0;
    private boolean mIsSurfaceViewHideWhileAspectChanging = false;
    private boolean mIsAutoReviewRequested = false;
    private ViewFinder.BurstRejectedReason mBurstShootingRejectedReason = ViewFinder.BurstRejectedReason.NONE;
    private boolean mIsEvfPrepared = true;
    private boolean mCanFocusRectanglesBeUpdated = true;
    private int mOrientation = 2;
    private int mPreviewOrientation = 2;
    private boolean mIsPaused = false;
    private boolean mIsSwitchingAnimationProgress = false;
    private boolean mIsThermalWarningDialogShown = false;
    private boolean mHintBurstImageSavedToInternalStorageAlreadyDisplayed = false;
    private boolean mHintCannotBurstUsingFrontCameraAlreadyDisplayed = false;
    private boolean mHintBurstChangeCameraKeySettingAlreadyDisplayed = false;
    private boolean mHintCannotBurstUsingFusionModeAlreadyDisplayed = false;
    private List<Runnable> mDelayUpdatedViewTaskList = new LinkedList();
    private boolean mIsNeedDisplayToastChangeInternalStoarge = false;
    private Storage.StorageStateListener mStorageStateListener = new Storage.StorageStateListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.1
        @Override // com.sonyericsson.cameracommon.storage.Storage.StorageStateListener
        public void onStorageStateChanged(Storage.StorageType storageType, Storage.StorageState storageState, Storage.StorageReadyState storageReadyState) {
        }

        @Override // com.sonyericsson.cameracommon.storage.Storage.StorageStateListener
        public void onStorageSizeChanged(Storage.StorageType storageType, long j) {
            if (CamLog.VERBOSE) {
                CamLog.d("onAvailableSizeUpdated: ");
            }
            CameraApplication.getUiThreadHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.1.1
                @Override // java.lang.Runnable
                public void run() {
                    ViewFinderImpl.this.mBaseLayout.getLowMemoryInternalIndicator().set(!ViewFinderImpl.this.hasEnoughFreeSpace(Storage.StorageType.INTERNAL));
                    ViewFinderImpl.this.mBaseLayout.getLowMemorySdIndicator().set(!ViewFinderImpl.this.hasEnoughFreeSpace(Storage.StorageType.EXTERNAL_CARD));
                }
            });
        }
    };
    private ModeSelector.OnModeSelectListener mModeSelectListener = new ModeSelector.OnModeSelectListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.2
        @Override // com.sonyericsson.android.camera.view.selectabledialog.ModeSelector.OnModeSelectListener
        public void onModeSelected(Mode mode, boolean z) throws Resources.NotFoundException {
            if (ViewFinderImpl.this.mIsSettingChangeAcceptable && ViewFinderImpl.this.isUserOperable()) {
                CameraActivity cameraActivity = ViewFinderImpl.this.mActivity;
                ActivityOptions activityOptionsMakeCustomAnimation = ActivityOptions.makeCustomAnimation(cameraActivity, 0, 0);
                if (mode instanceof InternalMode) {
                    if (ViewFinderImpl.this.mIsRequestingStartActivity) {
                        return;
                    }
                    ModeSelectorInternalMode tag = ((InternalMode) mode).getTag();
                    if (tag != ModeSelectorInternalMode.DUAL_MONOCHROME) {
                        if (ViewFinderImpl.this.mActivity.isDeviceInSecurityLock() && tag.isExternalApp) {
                            Intent intentCommit = LaunchCameraIntentBuilder.create().mode(ViewFinderImpl.this.getCapturingMode().name()).activity("com.sonyericsson.android.camera", CapturingModeUtil.CAMERA_ACTIVITY).callingMode(CapturingModeUtil.filteringPrevName(ViewFinderImpl.this.getCapturingMode().name())).callingActivity(cameraActivity.getPackageName(), CapturingModeUtil.filteringPrevActivity(cameraActivity.getClass().getName())).commit();
                            intentCommit.putExtra(LaunchCondition.EXTRA_LAUNCH_INTERNAL_MODE, tag.ordinal());
                            intentCommit.putExtra(LaunchCondition.EXTRA_LAUNCH_INTERNAL_CALLING_CAPTURING_MODE, ViewFinderImpl.this.mStateMachine.getCurrentCapturingMode().ordinal());
                            ViewFinderImpl.this.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP, intentCommit, activityOptionsMakeCustomAnimation.toBundle(), mode);
                            return;
                        }
                        if (tag.isExternalApp) {
                            int requestCodeFromMode = getRequestCodeFromMode(tag);
                            if (requestCodeFromMode != -1) {
                                if (z) {
                                    LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MRU_SHORTCUT);
                                    LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MRU_SHORTCUT);
                                } else {
                                    LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MODE_SELECTOR);
                                    LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MODE_SELECTOR);
                                }
                                LocalResearchUtil.getInstance().sendEventInternalModeChange(ViewFinderImpl.this.getCapturingMode(), tag);
                                if (CapturingModeUtil.MODE_WHITE_LIST.contains(tag.name())) {
                                    if (tag == ModeSelectorInternalMode.DUAL_BACKGROUND_DEFOCUS) {
                                        ApplicationLauncher.launchExternalCamera(ViewFinderImpl.this.mActivity, requestCodeFromMode, ViewFinderImpl.this.mStateMachine.getUserSetting(), CapturingMode.SCENE_RECOGNITION, true);
                                    } else {
                                        ApplicationLauncher.launchExternalCamera(ViewFinderImpl.this.mActivity, requestCodeFromMode, ViewFinderImpl.this.mStateMachine.getUserSetting(), ViewFinderImpl.this.mStateMachine.getCurrentCapturingMode(), true);
                                    }
                                } else {
                                    if (tag == ModeSelectorInternalMode.DUAL_BACKGROUND_DEFOCUS) {
                                        ApplicationLauncher.launchExternalCamera(ViewFinderImpl.this.mActivity, requestCodeFromMode, ViewFinderImpl.this.mStateMachine.getUserSetting(), CapturingMode.SCENE_RECOGNITION, false);
                                    } else {
                                        ApplicationLauncher.launchExternalCamera(ViewFinderImpl.this.mActivity, requestCodeFromMode, ViewFinderImpl.this.mStateMachine.getUserSetting(), ViewFinderImpl.this.mStateMachine.getCurrentCapturingMode(), false);
                                    }
                                    ViewFinderImpl.this.onAppsUiModeFinish();
                                }
                            }
                        } else {
                            AnimationRequest animationRequest = new AnimationRequest(z ? AnimationRequest.AnimationType.MRU_SHORTCUT : AnimationRequest.AnimationType.MODE_SELECTOR, AnimationRequest.AnimationDegree.START, ViewFinderImpl.this.getCapturingMode(), (CapturingMode) tag.tag);
                            if (ViewFinderImpl.this.requestAnimation(animationRequest)) {
                                ViewFinderImpl.this.hideSurface();
                                ViewFinderImpl.this.setApplicationNavigatorEnabled(false);
                                ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest);
                            }
                        }
                        ViewFinderImpl.this.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT, mode);
                        return;
                    }
                    ViewFinderImpl.this.mSettingUi.openMonochromeDialog(z, ViewFinderImpl.this.getBaseLayout().calculateCaptureButtonAreaHeight(), mode);
                    return;
                }
                if (mode instanceof AddonMode) {
                    CapturingModeAttributes tag2 = ((AddonMode) mode).getTag();
                    Intent intentCommit2 = LaunchCameraIntentBuilder.create().mode(tag2.getModeName()).activity(tag2.getPackageName(), tag2.getActivityName()).callingMode(CapturingModeUtil.filteringPrevName(ViewFinderImpl.this.getCapturingMode().name())).callingActivity(cameraActivity.getPackageName(), CapturingModeUtil.filteringPrevActivity(cameraActivity.getClass().getName())).commit();
                    if (ViewFinderImpl.this.mActivity.isDeviceInSecurityLock()) {
                        ViewFinderImpl.this.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP, intentCommit2, activityOptionsMakeCustomAnimation.toBundle(), mode);
                        return;
                    }
                    if (CapturingModeUtil.isActivityAvailable(cameraActivity, intentCommit2)) {
                        if (ViewFinderImpl.this.requestStartActivity(intentCommit2, (activityOptionsMakeCustomAnimation == null || activityOptionsMakeCustomAnimation.toBundle() == null) ? null : activityOptionsMakeCustomAnimation.toBundle())) {
                            if (z) {
                                LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MRU_SHORTCUT);
                                LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MRU_SHORTCUT);
                            } else {
                                LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MODE_SELECTOR);
                                LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MODE_SELECTOR);
                            }
                            LocalResearchUtil.getInstance().sendEventAddonModeChange(Event.Category.ADDON_FW, Event.AddonFW.APP_SELECTED_ON_MODE_SELECTOR.toString(), AddonMode.generateId(tag2.getPackageName(), tag2.getModeName()));
                            ViewFinderImpl.this.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT, mode);
                        }
                    }
                }
            }
        }

        private int getRequestCodeFromMode(ModeSelectorInternalMode modeSelectorInternalMode) {
            switch (AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[modeSelectorInternalMode.ordinal()]) {
                case 1:
                    return 18;
                case 2:
                    return 16;
                case 3:
                    return 17;
                default:
                    return -1;
            }
        }
    };
    private final UserEventHandler.TouchEventDispatcher mTouchEventDispatcher = new UserEventHandler.TouchEventDispatcher();
    private final Runnable mCheckEvfPreparationTask = new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.3
        @Override // java.lang.Runnable
        public void run() {
            if (ViewFinderImpl.this.mEvf != null) {
                ViewFinderImpl.this.notifyOnEvfPrepared();
            } else {
                CamLog.w("All reference of ViewFinderImpl has aleady been released.");
            }
        }
    };
    private final View.OnClickListener mFrontAngleSwitchButtonClickListener = new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.8
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (CamLog.VERBOSE) {
                CamLog.d("Wide front button is clicked, isCameraSwitching: " + ViewFinderImpl.this.isCameraSwitching());
            }
            ViewFinderImpl.this.mTouchEventDispatcher.sendClick(UserEventHandler.UiComponent.ANGLE_CHANGE_BUTTON, null);
        }
    };
    private final Handler mHandler = new Handler();
    private final Runnable mAfterSwitchAnimationTask = new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.19
        @Override // java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("invoke AfterSwitchAnimationTask");
            }
            ViewFinderImpl.this.mPreviewCover.setAlpha(0.0f);
            ViewFinderImpl.this.mAnimationController.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.FINISH, ViewFinderImpl.this.getCapturingMode(), ViewFinderImpl.this.getCapturingMode()), new TransitionAnimationController.TransitionAnimationCallback() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.19.1
                @Override // com.sonyericsson.android.camera.view.animation.TransitionAnimationController.TransitionAnimationCallback
                public void onAnimationFinished() {
                    ViewFinderImpl.this.resetAnimationProperty();
                    ViewFinderImpl.this.setIsSwitchingAnimationProgress(false);
                }
            });
        }
    };
    private final TutorialController.OnClickSetupWizardButtonListener mOnClickTutorialButtonListener = new TutorialController.OnClickSetupWizardButtonListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.25
        private void doPostProcessing(List<TutorialController.TutorialType> list) {
            MessageSettings messageSettings = ViewFinderImpl.this.mActivity.getStoredSettings().getMessageSettings();
            Iterator<TutorialController.TutorialType> it = list.iterator();
            while (it.hasNext()) {
                Iterator<MessageType> it2 = it.next().messageTypes.iterator();
                while (it2.hasNext()) {
                    messageSettings.setNeverShow(it2.next(), true);
                    messageSettings.save();
                }
            }
            ViewFinderImpl.this.setApplicationNavigatorEnabled(!ViewFinderImpl.this.mActivity.isOneShot());
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialController.OnClickSetupWizardButtonListener
        public void onAccepted(TutorialController.TutorialType tutorialType) {
            if (AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()] != 1) {
                return;
            }
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE, FusionMode.AUTO);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialController.OnClickSetupWizardButtonListener
        public void onDenied(TutorialController.TutorialType tutorialType) {
            if (AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()] != 1) {
                return;
            }
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE, FusionMode.OFF);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialController.OnClickSetupWizardButtonListener
        public void onClose(List<TutorialController.TutorialType> list) {
            if (list.contains(TutorialController.TutorialType.SUPER_SLOW_MOTION_MORE_OPTIONS)) {
                ViewFinderImpl.this.showHiSpeedSdCardRecommendDialogOnModeChange();
            } else if (list.contains(TutorialController.TutorialType.MANUAL_FUSION)) {
                ViewFinderImpl.this.updateHighSensitivityFusionModeForManual();
            }
            doPostProcessing(list);
        }
    };
    private TutorialController.SystemUiAccessor mSystemUiAccessor = new TutorialController.SystemUiAccessor() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.26
        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialController.SystemUiAccessor
        public void onAddFlags(int i) {
            ViewFinderImpl.this.mActivity.getWindow().addFlags(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialController.SystemUiAccessor
        public void onClearFlags(int i) {
            ViewFinderImpl.this.mActivity.getWindow().clearFlags(i);
        }
    };
    private boolean mRequireDisplayFlash = false;
    private boolean mIsDisplayFlashScreenDisplayed = false;
    private int mDisplayFlashColor = -1;
    private final ValueAccessor<Float> mColorValueAccessor = new ValueAccessor<Float>() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.29
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor
        public Float get() {
            return Float.valueOf(ViewFinderImpl.VIEW_FINDER_DUSKY);
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor
        public void set(Float f) {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_AMBER_BLUE_COLOR_CHANGED, f);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor
        public Float reset() {
            ViewFinderImpl.this.disableSemiAutoControl();
            return Float.valueOf(ViewFinderImpl.VIEW_FINDER_DUSKY);
        }
    };
    private final ValueAccessor<Float> mBrightnessValueAccessor = new ValueAccessor<Float>() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.30
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor
        public Float get() {
            return Float.valueOf(ViewFinderImpl.VIEW_FINDER_DUSKY);
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor
        public void set(Float f) {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_BRIGHTNESS_CHANGED, f);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor
        public Float reset() {
            ViewFinderImpl.this.disableSemiAutoControl();
            return Float.valueOf(ViewFinderImpl.VIEW_FINDER_DUSKY);
        }
    };

    public static final void preload() {
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public int getAutoPowerOffHintTextTimeOutDuration() {
        return 10000;
    }

    private static void logPerformance(String str) {
        Log.e("TraceLog", "[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + TAG + "] [" + Thread.currentThread().getName() + " : " + str + "]");
    }

    public UserEventHandler.TouchEventDispatcher getTouchEventDispatcher() {
        return this.mTouchEventDispatcher;
    }

    public enum PredictiveLaunchHideTrigger {
        TOUCH_UP(Event.PredictiveLaunchAction.TOUCH_UP),
        TOUCH_UP_CAPTURE(Event.PredictiveLaunchAction.TOUCH_UP),
        HW_CAMERA_KEY(Event.PredictiveLaunchAction.HW_CAMERA_KEY),
        VOLUME_KEY_SHUTTER(Event.PredictiveLaunchAction.VOLUME_KEY),
        VOLUME_KEY_ZOOM(Event.PredictiveLaunchAction.VOLUME_KEY),
        SIDE_SENSING(Event.PredictiveLaunchAction.SIDE_SENSING),
        OTHER(Event.PredictiveLaunchAction.OTHER);

        public final Event.PredictiveLaunchAction mAction;

        PredictiveLaunchHideTrigger(Event.PredictiveLaunchAction predictiveLaunchAction) {
            this.mAction = predictiveLaunchAction;
        }
    }

    public ViewFinderImpl(Context context, boolean z, LayoutDependencyResolver.ScreenAspect screenAspect, UiControlSettings uiControlSettings) {
        this.mEvf = null;
        this.mEvfLifeCycleCallback = new EvfLifeCycleCallback();
        this.mScreenButtonHandler = new ScreenButtonHandler();
        this.mPostUiInflatedTask = new PostUiInflatedTask();
        this.mScreenAspect = screenAspect;
        createViewFinder((CameraActivity) context, new BaseLayoutPatternApplier(), true);
        if (CamLog.VERBOSE) {
            CamLog.d("CONSTRUCTOR:[IN]");
        }
        if (z) {
            if (this.mActivity.isKeyguardSecure()) {
                this.mActivity.getWindow().addFlags(524288);
            } else {
                dismissKeyguard();
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
        this.mActivity.setContentView(this.mEvf.asView(), getPreviewLayoutParams());
        if (CamLog.DEBUG) {
            CamLog.d("setContentView() : addView(Evf) : X");
        }
        ((View) this.mEvf.asView().getParent()).setLayoutDirection(0);
        if (this.mSelfTimerCountDownViewNext == null) {
            this.mSelfTimerCountDownViewNext = (SelfTimerCountDownView) getActivity().getLayoutInflater().inflate(R.layout.selftimer_counter, (ViewGroup) null);
        }
        this.mToastContent = new ToastContent();
        this.mIsSetupHeadupDisplayInvoked = false;
        this.mMessageDialog = new MessageDialogController(this.mActivity, this.mActivity.getStoredSettings().getMessageSettings(), new MessageDialogOnClickPositiveListenerImpl(), new MessageDialogOnClickNegativeListenerImpl(), new MessageDialogOnCancelListenerImpl(), new MessageDialogOnDismissListenerImpl(), new MessageDialogOnOpenListenerImpl());
        this.mUiControlSettings = uiControlSettings;
    }

    private void dismissKeyguard() {
        ((KeyguardManager) this.mActivity.getSystemService(KeyguardManager.class)).requestDismissKeyguard(this.mActivity, null);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setContentView() {
        if (CamLog.VERBOSE) {
            CamLog.d("setContentView():[IN]");
        }
        setup(this.mEvf.asView());
        clearTouchCapture();
        if (CamLog.VERBOSE) {
            CamLog.d("setContentView():[OUT]");
        }
    }

    private void setup(View view) throws Resources.NotFoundException {
        getLayoutPatternApplier().setup(getBaseLayout(), this.mActivity.isOneShot());
        getBaseLayout().setPreviewSurface(view);
        if (CamLog.VERBOSE) {
            CamLog.d("[APP DETAIL] setup shutter : E");
        }
        getBaseLayout().setupPreferentialHeadUpDisplays();
        ViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.PHOTO_READY;
        CapturingMode capturingMode = getCapturingMode();
        if (capturingMode.isVideo()) {
            headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.VIDEO_READY;
        }
        this.mCapturingModeWhenLastSetupHeadDisplay = capturingMode;
        setOrientation(this.mActivity.getOrientation());
        setSelfTimer(capturingMode, this.mPhotoSelfTimerSetting);
        setupOnScreenCaptureButton(headUpDisplaySetupState);
        changeScreenButtonImage(headUpDisplaySetupState, false);
        if (CamLog.VERBOSE) {
            CamLog.d("[APP DETAIL] setup shutter : X");
        }
        LayoutDependencyResolver.setupRotatableToast(this.mActivity);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void attachToWindow() {
        if (getBaseLayout() != null) {
            getBaseLayout().attachToWindow();
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setStateMachine(StateMachine stateMachine) {
        if (CamLog.VERBOSE) {
            CamLog.d("setStateMachine():[IN]");
        }
        if (stateMachine != null) {
            stateMachine.addOnStateChangedListener(this);
            stateMachine.setGestureShutterWindowHost(new GestureShutterListener());
        } else if (this.mStateMachine != null) {
            this.mStateMachine.removeOnStateChangedListener(this);
        }
        this.mStateMachine = stateMachine;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setCameraDevice(CameraDeviceHandler cameraDeviceHandler) {
        this.mCameraDevice = cameraDeviceHandler;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isSetupHeadupDisplayInvoked() {
        return this.mIsSetupHeadupDisplayInvoked;
    }

    @Override // com.sonyericsson.android.camera.controller.StateMachine.OnStateChangedListener
    public void onStateChanged(StateMachine.CaptureState captureState, Object... objArr) throws Resources.NotFoundException {
        onViewFinderStateChanged(captureState, objArr);
    }

    boolean predictiveLaunchCoverExists() {
        PredictiveLaunchCoverView predictiveLaunchCoverView = getBaseLayout().getPredictiveLaunchCoverView();
        return predictiveLaunchCoverView != null && predictiveLaunchCoverView.exists();
    }

    @Override // com.sonyericsson.android.camera.CameraActivity.LayoutOrientationChangedListener
    public void onLayoutOrientationChanged(CameraActivity.LayoutOrientation layoutOrientation) {
        this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_ORIENTATION_CHANGED, Integer.valueOf(layoutOrientation == CameraActivity.LayoutOrientation.Portrait ? 1 : 2));
    }

    private void setOrientation(int i) throws Resources.NotFoundException {
        if (this.mStateMachine.isRecording()) {
            this.mBaseLayout.setOrientation(i, this.mRecordingOrientation);
            this.mOrientation = i;
            if (this.mApplicationNavigator != null) {
                this.mApplicationNavigator.setOrientation(this.mOrientation);
            }
        } else {
            this.mBaseLayout.setOrientation(i);
            this.mOrientation = i;
            if (this.mApplicationNavigator != null) {
                this.mApplicationNavigator.setOrientation(this.mOrientation);
            }
            if (this.mHintText != null) {
                updateHintTextUiOrientation();
            }
        }
        OnScreenButtonGroup onScreenButtonGroup = getBaseLayout().getOnScreenButtonGroup();
        if (onScreenButtonGroup != null) {
            onScreenButtonGroup.setUiOrientation(i);
        }
        if (this.mMessageDialog != null) {
            this.mMessageDialog.setSensorOrientation(i);
        }
        if (this.mToastContent != null) {
            this.mToastContent.setSensorOrientation(i);
        }
        if (isHeadUpDisplayReady()) {
            if (this.mSettingDialogStack != null) {
                this.mSettingDialogStack.setUiOrientation(i);
            }
            if (this.mInstantViewer != null) {
                this.mInstantViewer.setUiOrientation(i);
            }
            if (this.mFocusRectangles != null) {
                this.mFocusRectangles.setOrientation(i);
            }
            if (this.mSelfTimerCountDownView != null) {
                this.mSelfTimerCountDownView.setSensorOrientation(i);
            }
            if (this.mSettingUi != null) {
                this.mSettingUi.setSensorOrientation(i);
            }
            if (this.mBurstCountView != null) {
                this.mBurstCountView.setUiOrientation(i);
            }
            this.mSideTouchUi.setUiOrientation(i);
        }
    }

    private void updateHintTextUiOrientation() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mEvf.asView().getLayoutParams();
        this.mHintText.setUiOrientation(new Rect(0, 0, layoutParams.width, layoutParams.height), this.mActivity, this.mScreenAspect, this.mOrientation);
        updateVisibilityForSpecificDisplaySize();
    }

    public ViewGroup.LayoutParams getPreviewLayoutParams() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1, 51);
        if (this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 1) {
                this.mPreviewOrientation = 1;
                layoutParams.gravity = 49;
                layoutParams.setMargins(0, ResourceUtil.getDimensionPixelSize(this.mActivity, this.mActivity.getPackageName(), R.dimen.left_icon_area_height), 0, 0);
            } else {
                this.mPreviewOrientation = 2;
                layoutParams.gravity = 19;
                layoutParams.setMargins(ResourceUtil.getDimensionPixelSize(this.mActivity, this.mActivity.getPackageName(), R.dimen.left_icon_area_height), 0, 0, 0);
            }
        }
        return layoutParams;
    }

    public void updatePreviewLayoutParams() {
        if (this.mEvf == null || this.mEvf.asView() == null || this.mScreenAspect != LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE || this.mPreviewOrientation == LayoutOrientationResolver.getInstance().getConfigurationOrientation()) {
            return;
        }
        this.mEvf.asView().setLayoutParams(getPreviewLayoutParams());
    }

    protected void onSideTouchZoom(SideTouchEventDetector.SideTouchEvent sideTouchEvent, int i) {
        Point sideTouchPoint = getSideTouchPoint(sideTouchEvent);
        if (sideTouchPoint == null) {
            return;
        }
        hideZoomBar();
        this.mSideTouchUi.destroyTo(SideTouchUi.Type.COVERING);
        SideTouchUi.Type type = SideTouchUi.Type.ZOOM_BAR;
        this.mSideTouchUi.setUiOrientation(this.mOrientation);
        this.mSideTouchUi.attachIcon(type, sideTouchPoint);
        this.mSideTouchUi.showIcon();
        setZoomRatio(i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected boolean onSideTapped(SideTouchEventDetector.SideTouchEvent sideTouchEvent) {
        Point sideTouchPoint = getSideTouchPoint(sideTouchEvent);
        if (sideTouchPoint == null || this.mSideTouchUi.containsIn(SideTouchUi.Type.CAPTURE_COUNTDOWN, SideTouchUi.Type.VIDEO_COUNTDOWN, SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL)) {
            return false;
        }
        if (getBaseLayout().isAutoReviewShowing()) {
            getBaseLayout().hideAutoReview();
            return false;
        }
        this.mSideTouchUi.destroyTo(SideTouchUi.Type.ZOOM_BAR);
        this.mSideTouchUi.setUiOrientation(this.mOrientation);
        switch (getCapturingMode()) {
            case SCENE_RECOGNITION:
            case SUPERIOR_FRONT:
                if (((SelfTimer) this.mStateMachine.getUserSetting().get(UserSettingKey.SELF_TIMER)) == SelfTimer.OFF) {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.CAPTURE_COUNTDOWN, sideTouchPoint);
                } else {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL, sideTouchPoint);
                }
                return true;
            case VIDEO:
            case FRONT_VIDEO:
                this.mSideTouchUi.attachIcon(SideTouchUi.Type.VIDEO_COUNTDOWN, sideTouchPoint);
                return true;
            default:
                return true;
        }
    }

    private Point getSideTouchPoint(SideTouchEventDetector.SideTouchEvent sideTouchEvent) {
        switch (sideTouchEvent.area) {
            case TOP:
            case BOTTOM:
                return null;
            case LEFT:
                return new Point(0, sideTouchEvent.position);
            case RIGHT:
                return new Point(1439, sideTouchEvent.position);
            default:
                return null;
        }
    }

    public class SideTouchUiButtonListenerFactory {
        public SideTouchUiButtonListenerFactory() {
        }

        public OnScreenButtonListener create(OnScreenButtonItemFactory.ButtonType buttonType) {
            return new OnScreenButtonListenerImpl(buttonType);
        }

        private class OnScreenButtonListenerImpl implements OnScreenButtonListener {
            private final OnScreenButtonItemFactory.ButtonType mButtonType;

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onMove(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            }

            public OnScreenButtonListenerImpl(OnScreenButtonItemFactory.ButtonType buttonType) {
                this.mButtonType = buttonType;
            }

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onDown(OnScreenButton onScreenButton, MotionEvent motionEvent) {
                ViewFinderImpl.this.mTouchEventDispatcher.sendTouchDown(this.mButtonType);
            }

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onUp(OnScreenButton onScreenButton, MotionEvent motionEvent) {
                ViewFinderImpl.this.mTouchEventDispatcher.sendTouchUp(this.mButtonType, new Point((int) motionEvent.getX(), (int) motionEvent.getY()));
            }

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onCancel(OnScreenButton onScreenButton, MotionEvent motionEvent) {
                ViewFinderImpl.this.mTouchEventDispatcher.sendCancel(this.mButtonType);
            }

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onLongPress(OnScreenButton onScreenButton) {
                ViewFinderImpl.this.mTouchEventDispatcher.sendLongClick(this.mButtonType, null);
            }
        }
    }

    private class EvfLifeCycleCallback implements Evf.LifeCycleCallback {
        private EvfLifeCycleCallback() {
        }

        @Override // com.sonymobile.cameracommon.evf.Evf.LifeCycleCallback
        public void onEvfInitialized(Evf evf, int i, int i2) {
            PerfLog.SURFACE_CREATED.transit();
            if (CamLog.DEBUG) {
                CamLog.d("onEvfInitialized() : E");
            }
            if (CamLog.VERBOSE) {
                CamLog.d("onEvfInitialized():[IN] width=" + i + ", height=" + i2);
            }
            ViewFinderImpl.this.notifyOnEvfPrepared(new Rect(0, 0, i, i2));
            if (CamLog.VERBOSE) {
                CamLog.d("onEvfInitialized():[OUT]");
            }
            if (CamLog.DEBUG) {
                CamLog.d("onEvfInitialized() : X");
            }
        }

        @Override // com.sonymobile.cameracommon.evf.Evf.LifeCycleCallback
        public void onEvfSizeChanged(Evf evf, int i, int i2) {
            PerfLog.SURFACE_CHANGED.transit();
            if (CamLog.DEBUG) {
                CamLog.d("onEvfSizeChanged() : E");
            }
            if (CamLog.VERBOSE) {
                CamLog.d("surfaceChanged():[IN] width=" + i + ", height=" + i2);
            }
            ViewFinderImpl.this.notifyOnEvfPrepared(new Rect(0, 0, i, i2));
            if (CamLog.VERBOSE) {
                CamLog.d("surfaceChanged():[OUT]");
            }
            if (CamLog.DEBUG) {
                CamLog.d("onEvfSizeChanged() : X");
            }
        }

        @Override // com.sonymobile.cameracommon.evf.Evf.LifeCycleCallback
        public void onEvfFinalized(Evf evf) {
            PerfLog.SURFACE_DESTROYED.transit();
            if (CamLog.DEBUG) {
                CamLog.d("onEvfFinalized() : E");
            }
            if (ViewFinderImpl.this.mCameraDevice != null) {
                ViewFinderImpl.this.mCameraDevice.stopPreview();
                if (CamLog.DEBUG) {
                    CamLog.d("onEvfFinalized() : X");
                    return;
                }
                return;
            }
            CamLog.w("CameraDevice has already been released.");
        }
    }

    private void setEvfPrepared(boolean z) {
        if (this.mSettingDialogStack != null) {
            this.mSettingDialogStack.setCanceledOnTouchOutside(z);
        }
        this.mIsEvfPrepared = z;
        CamLog.d("setEvfPrepared() : evfPrepared & onTouchOutside = " + z);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isEvfPrepared() {
        CamLog.d("isEvfPrepared() : mIsEvfPrepared = " + this.mIsEvfPrepared);
        return this.mIsEvfPrepared;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void notifyOnEvfPrepared() {
        if (!this.mEvf.asSurface().isValid()) {
            CamLog.d("notifyOnEvfPrepared : Evf does not hold a physical surface yet.");
            return;
        }
        Size surfaceSize = this.mEvf.getSurfaceSize();
        if (surfaceSize != null) {
            notifyOnEvfPrepared(new Rect(0, 0, surfaceSize.getWidth(), surfaceSize.getHeight()));
        }
    }

    private static boolean isNearSameSize(Rect rect, Rect rect2) {
        return ViewUtility.isSimilarAspect(rect.width() / rect.height(), rect2.width() / rect2.height());
    }

    private boolean isNearSameSizeNavigationbar(Rect rect, Rect rect2) throws Resources.NotFoundException {
        if (isNearSameSize(rect, rect2)) {
            return true;
        }
        return isNearSameSize(rect, new Rect(rect2.left, rect2.top, rect2.right + this.mActivity.getResources().getDimensionPixelSize(R.dimen.navigationbar_width), rect2.bottom));
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void requestCheckEvfPreparationRetrying() {
        retryToCheckEvfPreparationDelayed();
    }

    private void retryToCheckEvfPreparationDelayed() {
        cancelCheckEvfPreparationTask();
        CameraApplication.getUiThreadHandler().postDelayed(this.mCheckEvfPreparationTask, 100L);
    }

    private void cancelCheckEvfPreparationTask() {
        CameraApplication.getUiThreadHandler().removeCallbacks(this.mCheckEvfPreparationTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnEvfPrepared(final Rect rect) {
        setEvfPrepared(true);
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_EVF_PREPARED, this.mEvf);
        this.mCanFocusRectanglesBeUpdated = false;
        Handler handler = getBaseLayout().getRootView().getHandler();
        if (handler != null) {
            handler.post(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.4
                @Override // java.lang.Runnable
                public void run() {
                    if (ViewFinderImpl.this.mFocusRectangles != null) {
                        Size sizeAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getSizeAccordingToLayoutOrientation(new Size(rect.width(), rect.height()));
                        ViewFinderImpl.this.mFocusRectangles.updateDevicePreviewSize(sizeAccordingToLayoutOrientation.getWidth(), sizeAccordingToLayoutOrientation.getHeight());
                    }
                    ViewFinderImpl.this.mCanFocusRectanglesBeUpdated = true;
                    ViewFinderImpl.this.updateCaptureAreaSize();
                    ViewFinderImpl.this.setupAutoReview();
                }
            });
        }
    }

    private void resizeEvfScope(Rect rect) {
        Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(rect);
        int iWidth = rectAccordingToLayoutOrientation.width();
        int iHeight = rectAccordingToLayoutOrientation.height();
        float f = iWidth / iHeight;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getPreviewLayoutParams();
        if (iWidth == iHeight) {
            Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(this.mActivity);
            if (LayoutOrientationResolver.getInstance().getConfigurationOrientation() == 1) {
                layoutParams.topMargin += viewFinderSize.height() / 3;
            } else {
                layoutParams.leftMargin += viewFinderSize.height() / 3;
            }
        }
        this.mEvf.asView().setLayoutParams(layoutParams);
        Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect(this.mActivity, f, this.mScreenAspect);
        this.mEvf.resize(surfaceViewRect.width(), surfaceViewRect.height());
        this.mEvf.setFixedSurfaceSize(rect.width(), rect.height());
    }

    private void setupHeadUpDisplay(ViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) throws Resources.NotFoundException {
        PerfLog.VIEWFINDER_SETUP_HEADUP_DISPLAY.begin();
        if (CamLog.VERBOSE) {
            CamLog.d("setupHeadUpDisplay ");
        }
        if (this.mCapturingModeWhenLastSetupHeadDisplay != getCapturingMode()) {
            this.mIsSetupHeadupDisplayInvoked = false;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("setupHeadUpDisplay() prev:" + this.mCapturingModeWhenLastSetupHeadDisplay + " current:" + getCapturingMode());
        }
        if (this.mActivity.isDeviceInSecurityLock() && this.mIsSetupHeadupDisplayInvoked) {
            if (CamLog.VERBOSE) {
                CamLog.d("setupHeadUpDisplay is already invoked.");
                return;
            }
            return;
        }
        this.mCapturingModeWhenLastSetupHeadDisplay = getCapturingMode();
        this.mSurfaceBlinderView = new View(this.mActivity);
        this.mSurfaceBlinderView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.mSurfaceBlinderView.setVisibility(8);
        joinInflateTask();
        if (!isHeadUpDisplayReady()) {
            Rect rect = this.mEvf.getRect();
            boolean z = LayoutOrientationResolver.getInstance().getOrientation() != LayoutOrientationResolver.LayoutOrientationType.PORTRAIT ? rect.width() >= rect.height() : rect.width() <= rect.height();
            if (CamLog.VERBOSE) {
                CamLog.d("isEvfReady : " + z);
            }
            if (!z) {
                this.mActivity.postDelayedEvent(new ReTrySetupHeadUpDisplayTask(), 100L);
                return;
            }
        }
        boolean zIsHeadUpDisplayReady = isHeadUpDisplayReady();
        if (isInflated()) {
            setPreInflatedHeadUpDisplay(getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.HEAD_UP_DISPLAY).get(0));
        }
        requestSetupHeadUpDisplay();
        if (!zIsHeadUpDisplayReady) {
            getBaseLayout().getPreviewOverlayContainer().addView(setupViewFinderLayout());
            this.mCaptureFeedback = setupFeedbackContextView();
            getBaseLayout().getRootView().addView((GLSurfaceContextView) this.mCaptureFeedback);
        }
        setupCaptureButtonArea();
        setupApplicationNavigator(headUpDisplaySetupState);
        setupRightIndicatorArea();
        setupTransitionAnimationController(this.mActivity, headUpDisplaySetupState);
        setupHintText();
        setupDraggingEventHandler();
        setupSettingUi();
        setupContentsView();
        setupCaptureArea(headUpDisplaySetupState);
        setupFocusRectangles();
        setupOnScreenCaptureButton(headUpDisplaySetupState);
        setupInstantViewer();
        setupAutoReview();
        setupSelfTimerCountDownView();
        this.mZoomBarProxy = new ZoomBarUpdateProxy();
        this.mRecordingTimeProxy = new RecordingTimeReceiverProxy();
        this.mAutoReviewProxy = new AutoReviewContentReceiverProxy();
        this.mZoomBarProxy.bindZoomBar(getBaseLayout().getZoomBar());
        this.mRecordingTimeProxy.bindReceiver(getBaseLayout().getRecordingIndicator());
        this.mAutoReviewProxy.bindReceiver(getBaseLayout().getAutoReview());
        setupSideTouchUI();
        setZoomRatio(0);
        setupPrimaryShortcutIcons();
        setupOnScreenShortcut();
        setOrientation(getOrientation());
        updateGridLineView();
        updateFrontAngleSwitchButton();
        if (!isTutorialOpened()) {
            updateVideoHdrCondition(this.mCapturingModeWhenLastSetupHeadDisplay, (VideoHdr) this.mStateMachine.getUserSetting().get(this.mCapturingModeWhenLastSetupHeadDisplay, UserSettingKey.VIDEO_HDR), true);
            changeToLayoutWithSetupState(headUpDisplaySetupState);
        }
        Handler handler = getBaseLayout().getRootView().getHandler();
        if (handler != null) {
            handler.post(this.mPostUiInflatedTask);
        }
        if (!isCameraSwitching()) {
            this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED, headUpDisplaySetupState);
        }
        clearPreInflatedViews();
        this.mIsSetupHeadupDisplayInvoked = true;
        this.mCanFocusRectanglesBeUpdated = true;
        if (this.mAutoReviewStoreData != null && this.mAutoReviewStoreData.savingRequest.common.takenByFastCapture && this.mAutoReviewStoreData.isSuccess()) {
            if (CamLog.DEBUG) {
                CamLog.d("Pending Auto review is shown when ViewFinder is ready.");
            }
            showAutoReview(this.mAutoReviewStoreData);
            this.mAutoReviewStoreData = null;
        }
        setIsCameraSwitching(false);
        PerfLog.VIEWFINDER_SETUP_HEADUP_DISPLAY.end();
    }

    private void setupSideTouchUI() {
        this.mSideTouchUi = new SideTouchUi((FrameLayout) this.mActivity.findViewById(R.id.side_touch_ui_layout), this.mActivity.isOneShot());
        this.mSideTouchUi.setUiOrientation(getOrientation());
        this.mSideTouchUi.setZoomBarUpdateProxy(this.mZoomBarProxy);
        this.mSideTouchUi.setRecordingTimeReceiverProxy(this.mRecordingTimeProxy);
        this.mSideTouchUi.setAutoReviewProxy(this.mAutoReviewProxy);
        this.mSideTouchUi.setScreenButtonListenerFactory(new SideTouchUiButtonListenerFactory());
    }

    private void setupSettingUi() {
        if (this.mSettingDialogStack == null) {
            this.mSettingDialogStack = new SettingDialogStack(this.mActivity, (ViewGroup) this.mActivity.findViewById(R.id.setting_container), getBaseLayout().getViewFinderRect());
            this.mSettingDialogStack.addDialogListener(new SettingDialogListenerImpl());
            this.mSettingDialogStack.addDialogListener(getBaseLayout().getPrimaryShortcut());
            if (this.mSettingMenuExclusiveListener == null) {
                this.mSettingMenuExclusiveListener = new SettingMenuExclusiveListener();
                this.mSettingDialogStack.setExclusiveViewListener(this.mSettingMenuExclusiveListener);
            }
        }
        if (this.mSettingUi == null) {
            this.mSettingUi = new SettingUi(this.mActivity, this.mSettingDialogStack, this.mStateMachine, this, this.mCameraDevice, this.mActivity.isDeviceInSecurityLock());
        } else {
            this.mSettingUi.setDeviceInSecurityLock(this.mActivity.isDeviceInSecurityLock());
        }
        this.mSettingDialogStack.setCapturingMode((CapturingMode) this.mStateMachine.getUserSetting().get(UserSettingKey.CAPTURING_MODE));
        Iterator<Runnable> it = this.mDelayUpdatedViewTaskList.iterator();
        while (it.hasNext()) {
            CameraApplication.getUiThreadHandler().post(it.next());
        }
        this.mDelayUpdatedViewTaskList.clear();
    }

    private class SettingMenuExclusiveListener implements SettingDialogStack.ExclusiveViewListener {
        private SettingMenuExclusiveListener() {
        }

        @Override // com.sonyericsson.android.camera.view.setting.SettingDialogStack.ExclusiveViewListener
        public boolean isExclusiveView(View view, MotionEvent motionEvent) {
            return ViewFinderImpl.this.isExclusiveViewEvent(view, motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isExclusiveViewEvent(View view, MotionEvent motionEvent) {
        boolean zIsOpened;
        if (this.mSettingDialogStack != null && this.mPrimaryShortcutGroup != null) {
            for (Map.Entry<UserSettingKey, View> entry : this.mPrimaryShortcutGroup.getPrimaryShortcutViewMap().entrySet()) {
                if (entry.getKey() == UserSettingKey.SETTING_MENU) {
                    if (this.mOrientation == 1 && this.mSettingDialogStack.isSecondLayerDialogOpened()) {
                        return false;
                    }
                    zIsOpened = this.mSettingDialogStack.isMenuDialogOpened();
                } else {
                    zIsOpened = this.mSettingDialogStack.isOpened(entry.getKey());
                }
                if (zIsOpened) {
                    View value = entry.getValue();
                    Rect rect = new Rect();
                    value.getGlobalVisibleRect(rect);
                    Rect rect2 = new Rect();
                    view.getGlobalVisibleRect(rect2);
                    if (rect.contains(rect2.bottom - ((int) motionEvent.getY()), rect2.left + ((int) motionEvent.getX()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private class SettingDialogListenerImpl implements SettingDialogListener {
        private SettingDialogListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener
        public void onOpenSettingDialog(Object obj) {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_OPENED, new Object[0]);
        }

        @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener
        public void onCloseSettingDialog(Object obj) {
            if (!ViewFinderImpl.this.isAllDialogClosed() || ViewFinderImpl.this.isTutorialOpened()) {
                return;
            }
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
        }
    }

    private RelativeLayout setupViewFinderLayout() {
        RelativeLayout relativeLayout = isInflated() ? (RelativeLayout) getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.FAST_CAPTURING_VIEWFINDER_ITEMS).get(0) : null;
        return relativeLayout == null ? (RelativeLayout) LayoutInflater.from(this.mActivity).inflate(R.layout.fast_capturing_viewfinder_items, (ViewGroup) null) : relativeLayout;
    }

    private GLSurfaceContextView setupFeedbackContextView() {
        GLSurfaceContextView gLSurfaceContextView = new GLSurfaceContextView(getActivity(), null);
        gLSurfaceContextView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        gLSurfaceContextView.setVisibility(4);
        return gLSurfaceContextView;
    }

    private class PostUiInflatedTask implements Runnable {
        private PostUiInflatedTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewFinderImpl.this.setupAnimations();
        }
    }

    private class ReTrySetupHeadUpDisplayTask implements Runnable {
        private ReTrySetupHeadUpDisplayTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ViewFinderImpl.this.mStateMachine == null) {
                return;
            }
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, false);
        }
    }

    private void changeToLayoutWithSetupState(ViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) throws Resources.NotFoundException {
        switch (headUpDisplaySetupState) {
            case PHOTO_READY:
                changeToPhotoReadyView(true);
                break;
            case PHOTO_CAPTURE:
                changeToPhotoCaptureView();
                break;
            case PHOTO_BURST_CAPTURE:
                changeToBurstCaptureView();
                break;
            case VIDEO_READY:
                changeToVideoReadyView();
                break;
            case VIDEO_RECORDING:
                changeToVideoRecordingView();
                break;
            default:
                throw new IllegalStateException("setupHeadUpDisplay():[Illegal State]");
        }
        if (AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation[this.mActivity.getLaunchCondition().getExtraOperation().ordinal()] != 1) {
            return;
        }
        String userSettingKeyName = this.mActivity.getLaunchCondition().getUserSettingKeyName();
        if (userSettingKeyName == null) {
            PostActionToMainThread(null);
        } else {
            PostActionToMainThread(UserSettingKey.valueOf(userSettingKeyName));
        }
    }

    private void PostActionToMainThread(UserSettingKey userSettingKey) {
        CameraApplication.getUiThreadHandler().post(new ActionRunnable(userSettingKey));
    }

    private class ActionRunnable implements Runnable {
        private UserSettingKey mUserSettingKey;

        public ActionRunnable(UserSettingKey userSettingKey) {
            this.mUserSettingKey = userSettingKey;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ViewFinderImpl.this.mStateMachine.isMenuAvailable()) {
                if (this.mUserSettingKey != null) {
                    switch (this.mUserSettingKey) {
                        case SIDE_SENSE:
                        case GEO_TAG:
                        case DESTINATION_TO_SAVE:
                            ViewFinderImpl.this.openUserSelectMenu(this.mUserSettingKey);
                            break;
                        case HELP_GUIDE:
                            if (HelpGuide.isHelpAppAvailable(ViewFinderImpl.this.mActivity)) {
                                HelpGuide.startHelpApp(ViewFinderImpl.this.mActivity);
                            } else {
                                HelpGuide.startOnlineHelp(ViewFinderImpl.this.mActivity);
                            }
                            ViewFinderImpl.this.mActivity.getLaunchCondition().clearExtraOperation();
                            break;
                        case RESET_SETTINGS:
                            ViewFinderImpl.this.showMessageDialog(DialogId.RESET_CONFIRMATION, new Object[0]);
                            break;
                        default:
                            ViewFinderImpl.this.openUserSelectMenu(null);
                            break;
                    }
                }
                ViewFinderImpl.this.openUserSelectMenu(null);
            }
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setupFocusRectangles() {
        int i;
        int height;
        if (this.mCameraDevice == null || this.mCameraDevice.getPreviewSize() == null) {
            i = 0;
            height = 0;
        } else {
            Rect previewSize = this.mCameraDevice.getPreviewSize();
            Size sizeAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getSizeAccordingToLayoutOrientation(new Size(previewSize.width(), previewSize.height()));
            int width = sizeAccordingToLayoutOrientation.getWidth();
            height = sizeAccordingToLayoutOrientation.getHeight();
            i = width;
        }
        this.mOnFocusRectangleTouchListener = new View.OnTouchListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.5
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) throws Resources.NotFoundException {
                switch (motionEvent.getAction()) {
                    case 0:
                        if (CamLog.VERBOSE) {
                            CamLog.d("onTouch ACTION_DOWN");
                        }
                        if (ViewFinderImpl.this.isTouchCaptureEnabled()) {
                            return false;
                        }
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
                        return true;
                    case 1:
                        if (CamLog.VERBOSE) {
                            CamLog.d("onTouch ACTION_UP");
                        }
                        if (ViewFinderImpl.this.isTouchCaptureEnabled()) {
                            return false;
                        }
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
                        if (ViewFinderImpl.this.mCameraDevice.isObjectTrackingRunning() && !ViewFinderImpl.this.isZooming()) {
                            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DESELECT_OBJECT_POSITION, new Object[0]);
                        }
                        ViewFinderImpl.this.hideAutoReview();
                        ViewFinderImpl.this.switchSemiAutoStateByTouch(false);
                        return true;
                    case 2:
                        if (CamLog.VERBOSE) {
                            CamLog.d("onTouch ACTION_MOVE");
                        }
                        if (ViewFinderImpl.this.isTouchCaptureEnabled()) {
                            return false;
                        }
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
                        return true;
                    default:
                        return false;
                }
            }
        };
        FocusRectanglesViewList focusRectanglesViewList = new FocusRectanglesViewList();
        if (isInflated()) {
            focusRectanglesViewList.rectanglesContainer = (RelativeLayout) this.mActivity.findViewById(R.id.focus_rectangles);
            focusRectanglesViewList.faceViewList = (View[]) getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FACE).toArray(new View[0]);
            focusRectanglesViewList.trackedObjectView = (TaggedRectangle) getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_OBJECT_TRACKING).get(0);
            focusRectanglesViewList.singleAfView = (RelativeLayout) getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_SINGLE).get(0);
            focusRectanglesViewList.touchAfView = (RelativeLayout) getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_TOUCH).get(0);
        }
        if (this.mFocusRectangles == null) {
            this.mFocusRectangles = new FocusRectangles(this.mActivity, new FocusActionListenerImpl(), i, height, focusRectanglesViewList, this.mViewFinderCaptureArea, this.mOnFocusRectangleTouchListener, this.mScreenAspect);
        }
        if (this.mStateMachine == null) {
            return;
        }
        if (PlatformCapability.isFaceDetectionAvailable(getCapturingMode().getCameraId())) {
            if (isTouchCaptureEnabled()) {
                this.mFocusRectangles.enableFaceTouchCapture();
            } else {
                this.mFocusRectangles.disableFaceTouchCapture();
            }
        }
        applySmileFocusThreshold(true);
        this.mFocusRectangles.setVisibility(0);
    }

    private void setupCaptureArea(ViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (this.mViewFinderCaptureArea == null) {
            this.mViewFinderCaptureArea = (CaptureArea) this.mActivity.findViewById(R.id.viewfinder_capture);
            updateCaptureAreaSize();
        }
        this.mViewFinderCaptureArea.setCaptureAreaStateListener(new ViewFinderStateListener());
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void updateCaptureAreaSize() {
        if (this.mViewFinderCaptureArea != null) {
            Rect rect = this.mEvf.getRect();
            int iWidth = rect.width();
            int iHeight = rect.height();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mViewFinderCaptureArea.getLayoutParams();
            if (iWidth == iHeight) {
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
            layoutParams.width = iWidth;
            layoutParams.height = iHeight;
            this.mViewFinderCaptureArea.setLayoutParams(layoutParams);
            updatePreviewContainer(iWidth, iHeight);
            this.mHintText.updateHintTextContainer(layoutParams.width, layoutParams.height);
            getBaseLayout().repositionZoombar();
            PositionConverter.getInstance().setSurfaceSize(rect.width(), rect.height());
        }
    }

    private void setupDraggingEventHandler() {
        getBaseLayout().setOnViewFinderGestureDetector(new AbstractDraggingEventHandler(this.mActivity, TransitionAnimationController.getSwipeThreshold(this.mActivity), TransitionAnimationController.getSwitchSwipeThreshold(this.mActivity)) { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.6
            @Override // com.sonyericsson.android.camera.controller.AbstractDraggingEventHandler
            protected boolean canDragging() {
                return ViewFinderImpl.this.mIsSettingChangeAcceptable && ViewFinderImpl.this.isUserOperable();
            }

            @Override // com.sonyericsson.android.camera.controller.AbstractDraggingEventHandler
            protected void sendTouchDownEvent(MotionEvent motionEvent) throws Resources.NotFoundException {
                if (ViewFinderImpl.this.mHintText != null) {
                    ViewFinderImpl.this.mHintText.clearToastContent();
                }
            }

            @Override // com.sonyericsson.android.camera.controller.AbstractDraggingEventHandler
            protected boolean sendStartEvent(AbstractDraggingEventHandler.Direction direction) {
                CapturingMode capturingMode;
                if (CamLog.DEBUG) {
                    CamLog.d("invoke source:" + direction.name());
                }
                if (!isModeChangingEnable(direction)) {
                    return false;
                }
                CapturingMode capturingMode2 = ViewFinderImpl.this.getCapturingMode();
                if (direction == AbstractDraggingEventHandler.Direction.UP || direction == AbstractDraggingEventHandler.Direction.DOWN) {
                    if (ViewFinderImpl.this.mActivity.isOneShot() || capturingMode2 == (capturingMode = ViewFinderImpl.getCapturingMode(ViewFinderImpl.this.getNextContent(NavigatorContents.valueOf(ViewFinderImpl.this.getCapturingMode()), direction), ViewFinderImpl.this.getCapturingMode()))) {
                        return false;
                    }
                    AnimationRequest animationRequest = new AnimationRequest(AnimationRequest.AnimationType.MODE_TOUCH, AnimationRequest.AnimationDegree.START, capturingMode2, capturingMode);
                    if (ViewFinderImpl.this.requestAnimation(animationRequest)) {
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest);
                        return true;
                    }
                }
                if (PlatformCapability.isFrontCameraSupported() && direction == AbstractDraggingEventHandler.Direction.RIGHT) {
                    AnimationRequest animationRequest2 = new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.START, capturingMode2, getSwitchTargetMode(capturingMode2));
                    if (ViewFinderImpl.this.requestAnimation(animationRequest2)) {
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest2);
                        return true;
                    }
                }
                return false;
            }

            @Override // com.sonyericsson.android.camera.controller.AbstractDraggingEventHandler
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

            @Override // com.sonyericsson.android.camera.controller.AbstractDraggingEventHandler
            protected void sendFinishEvent(AbstractDraggingEventHandler.Direction direction) throws Resources.NotFoundException {
                AnimationRequest.AnimationDegree animationDegree;
                if (CamLog.DEBUG) {
                    CamLog.d("invoke source:" + direction.name());
                }
                if (isModeChangingEnable(direction)) {
                    CapturingMode capturingMode = ViewFinderImpl.this.getCapturingMode();
                    if (direction == AbstractDraggingEventHandler.Direction.UP || direction == AbstractDraggingEventHandler.Direction.DOWN) {
                        if (ViewFinderImpl.this.mActivity.isOneShot()) {
                            return;
                        }
                        CapturingMode capturingMode2 = ViewFinderImpl.getCapturingMode(ViewFinderImpl.this.getNextContent(NavigatorContents.valueOf(ViewFinderImpl.this.getCapturingMode()), direction), ViewFinderImpl.this.getCapturingMode());
                        if (CamLog.DEBUG) {
                            CamLog.d("invoke current:" + capturingMode.name() + ", target:" + capturingMode2.name());
                        }
                        if (capturingMode == capturingMode2) {
                            animationDegree = AnimationRequest.AnimationDegree.CANCEL;
                        } else {
                            animationDegree = AnimationRequest.AnimationDegree.EXEC;
                        }
                        if (ViewFinderImpl.this.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.MODE_TOUCH, animationDegree, capturingMode, capturingMode2))) {
                            ViewFinderImpl.this.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
                            PerfLog.SWIPE_ANIMATION_START.transit();
                        }
                    }
                    if (direction == AbstractDraggingEventHandler.Direction.LEFT) {
                        ViewFinderImpl.this.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.CANCEL, capturingMode, getSwitchTargetMode(capturingMode)));
                    }
                    if (direction == AbstractDraggingEventHandler.Direction.RIGHT && ViewFinderImpl.this.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.EXEC, capturingMode, getSwitchTargetMode(capturingMode)))) {
                        ViewFinderImpl.this.setIsSwitchingAnimationProgress(true);
                        ViewFinderImpl.this.setIsCameraSwitching(true);
                        ViewFinderImpl.this.hideSurface();
                    }
                }
            }

            @Override // com.sonyericsson.android.camera.controller.AbstractDraggingEventHandler
            protected void sendCancelEvent(AbstractDraggingEventHandler.Direction direction) {
                if (CamLog.DEBUG) {
                    CamLog.d("invoke source:" + direction.name());
                }
                if (isModeChangingEnable(direction)) {
                    CapturingMode capturingMode = ViewFinderImpl.this.getCapturingMode();
                    if (direction == AbstractDraggingEventHandler.Direction.UP || direction == AbstractDraggingEventHandler.Direction.DOWN) {
                        if (ViewFinderImpl.this.mActivity.isOneShot()) {
                            return;
                        }
                        if (ViewFinderImpl.this.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.MODE_TOUCH, AnimationRequest.AnimationDegree.CANCEL, capturingMode, ViewFinderImpl.this.getCapturingMode()))) {
                            ViewFinderImpl.this.showViews();
                        }
                    }
                    if (direction == AbstractDraggingEventHandler.Direction.RIGHT || direction == AbstractDraggingEventHandler.Direction.LEFT) {
                        ViewFinderImpl.this.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.CANCEL, capturingMode, getSwitchTargetMode(capturingMode)));
                    }
                }
            }

            @Override // com.sonyericsson.android.camera.controller.AbstractDraggingEventHandler, com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector.OnViewFinderGestureDetectorListener
            public void onStartDragging(MotionEvent motionEvent, MotionEvent motionEvent2) {
                super.onStartDragging(motionEvent, motionEvent2);
                if (getModeIndexUnder(motionEvent) == -1) {
                    LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.SWIPE);
                } else {
                    LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.ICON_SWIPE);
                }
            }

            private int getModeIndexUnder(MotionEvent motionEvent) {
                if (ViewFinderImpl.this.mApplicationNavigator == null) {
                    return -1;
                }
                return ViewFinderImpl.this.mApplicationNavigator.getModeIndexUnder((int) motionEvent.getX(), (int) motionEvent.getY());
            }

            private boolean isModeChangingEnable(AbstractDraggingEventHandler.Direction direction) {
                CapturingMode capturingMode = ViewFinderImpl.this.getCapturingMode();
                switch (direction) {
                    case UP:
                    case DOWN:
                        if (capturingMode == CapturingMode.NORMAL || capturingMode == CapturingMode.SLOW_MOTION || capturingMode == CapturingMode.FRONT_PHOTO) {
                        }
                        break;
                    case LEFT:
                    case RIGHT:
                        if (capturingMode == CapturingMode.SLOW_MOTION) {
                        }
                        break;
                }
                return false;
            }

            private CapturingMode getSwitchTargetMode(CapturingMode capturingMode) {
                switch (capturingMode) {
                    case SCENE_RECOGNITION:
                        return CapturingMode.SUPERIOR_FRONT;
                    case SUPERIOR_FRONT:
                        return CapturingMode.SCENE_RECOGNITION;
                    case VIDEO:
                        return CapturingMode.FRONT_VIDEO;
                    case FRONT_VIDEO:
                        return CapturingMode.VIDEO;
                    case NORMAL:
                        return CapturingMode.FRONT_PHOTO;
                    case FRONT_PHOTO:
                        return CapturingMode.NORMAL;
                    default:
                        return CapturingMode.SCENE_RECOGNITION;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NavigatorContents getNextContent(NavigatorContents navigatorContents, AbstractDraggingEventHandler.Direction direction) {
        switch (direction) {
            case UP:
                return navigatorContents.next();
            case DOWN:
                return navigatorContents.previous();
            default:
                return navigatorContents;
        }
    }

    private void setupOnScreenCaptureButton(ViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        changeScreenButtonImage(headUpDisplaySetupState, false);
        if (isHeadUpDisplayReady()) {
            if (this.mImageQualityControlButtonItem == null) {
                this.mImageQualityControlButtonItem = OnScreenButtonItemFactory.createMutableButton(new OnScreenImageQualityControlButtonListener());
                this.mImageQualityControlButtonItem.update().background(R.drawable.secondary_shortcut_selector).commit();
            }
            if (this.mHighSensitivityFusionButtonItem == null) {
                this.mHighSensitivityFusionButtonItem = OnScreenButtonItemFactory.createMutableButton(new OnHighSensitivityFusionButtonStateListener());
                this.mHighSensitivityFusionButtonItem.update().background(R.drawable.secondary_shortcut_selector).commit();
            }
            updateSecondaryShortcutOnScreenButtonResource();
        }
    }

    private void setupSelfTimerCountDownView() {
        if (this.mPhotoSelfTimerSetting == null) {
            return;
        }
        switch (this.mPhotoSelfTimerSetting) {
            case LONG:
            case GESTURE_SHUTTER_COUNT_DOWN:
            case SHORT:
            case LAUNCH_AND_CAPTURE_COUNT_DOWN:
                createSelfTimerCountDownView(this.mPhotoSelfTimerSetting);
                return;
            case OFF:
                removeSelfTimerCountDownView();
                return;
            default:
                throw new IllegalArgumentException("ViewFinderImpl:setupSelfTimerCountDownView [Irregular value] : " + this.mPhotoSelfTimerSetting);
        }
    }

    private void createSelfTimerCountDownView(SelfTimer selfTimer) {
        if (this.mSelfTimerCountDownViewNext == null) {
            this.mSelfTimerCountDownViewNext = (SelfTimerCountDownView) getActivity().getLayoutInflater().inflate(R.layout.selftimer_counter, (ViewGroup) null);
        }
        this.mSelfTimerCountDownViewNext.setSelfTimer(selfTimer);
    }

    private void removeSelfTimerCountDownView() {
        if (this.mSelfTimerCountDownView != null) {
            getBaseLayout().getLazyInflatedUiComponentContainerBack().removeView(this.mSelfTimerCountDownView);
            this.mSelfTimerCountDownView = null;
        }
    }

    private void cancelSelfTimerCountDownView() {
        if (this.mSelfTimerCountDownView != null) {
            this.mSelfTimerCountDownView.cancelSelfTimerCountDownAnimation();
            getBaseLayout().getLazyInflatedUiComponentContainerBack().removeView(this.mSelfTimerCountDownView);
        }
    }

    private void showSelfTimerCountDownView() {
        Size sizeAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getSizeAccordingToLayoutOrientation(new Size(getBaseLayout().getPreview().getWidth(), getBaseLayout().getPreview().getHeight()));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(sizeAccordingToLayoutOrientation.getWidth(), sizeAccordingToLayoutOrientation.getHeight());
        layoutParams.addRule(13);
        removeSelfTimerCountDownView();
        this.mSelfTimerCountDownView = this.mSelfTimerCountDownViewNext;
        this.mSelfTimerCountDownView.setLayoutParams(layoutParams);
        this.mSelfTimerCountDownView.setVisibility(0);
        getBaseLayout().getLazyInflatedUiComponentContainerBack().addView(this.mSelfTimerCountDownView);
        getBaseLayout().getLazyInflatedUiComponentContainerBack().bringChildToFront(this.mSelfTimerCountDownView);
        if (LayoutDependencyResolver.isTenInch(getActivity())) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mSelfTimerCountDownView.getLayoutParams();
            layoutParams2.gravity = 17;
            this.mSelfTimerCountDownView.setLayoutParams(layoutParams2);
        }
        applySmileFocusThreshold(false);
    }

    private boolean isInSelfTimerCountDown() {
        return (getCapturingMode() == CapturingMode.FRONT_VIDEO || getCapturingMode() == CapturingMode.VIDEO || getCurrentLayoutPattern() != BaseLayoutPattern.SELFTIMER) ? false : true;
    }

    private void startSelfTimerCountDownAnimation() {
        if (this.mSelfTimerCountDownView != null) {
            this.mSelfTimerCountDownView.startSelfTimerCountDownAnimation(getCapturingMode() == CapturingMode.FRONT_PHOTO || getCapturingMode() == CapturingMode.SUPERIOR_FRONT);
        }
    }

    private void setupContentsView() {
        this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_PHOTO_STACK_INITIALIZED, getBaseLayout().getContentsViewController());
    }

    private void setupPrimaryShortcutIcons() {
        if (this.mPrimaryShortcutGroup == null) {
            this.mPrimaryShortcutGroup = getBaseLayout().getPrimaryShortcut();
            this.mPrimaryShortcutGroup.setViewFinderAccessor(new ViewFinderAccessorForShortcut());
        }
        if (isCameraSwitching()) {
            return;
        }
        updatePrimaryShortcutIcons();
    }

    private void setupOnScreenShortcut() {
        setMruAvailability(false);
        if (this.mActivity.isOneShot()) {
            getBaseLayout().getModeButtonShortcut().set(false);
            return;
        }
        getBaseLayout().getModeButtonShortcut().set(true);
        getBaseLayout().getModeButtonShortcut().setOnClickListener(new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ViewFinderImpl.this.mIsSettingChangeAcceptable && ViewFinderImpl.this.isUserOperable()) {
                    if (!ModeSelectorInternalMode.exists(ViewFinderImpl.this.getCapturingMode()) && !ViewFinderImpl.this.getCapturingMode().equals(CapturingMode.FRONT_PHOTO)) {
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_OPENED, ViewFinder.UiComponentKind.MODE_SELECTOR);
                    } else {
                        ViewFinderImpl.this.startReturnModeAnimation();
                    }
                }
            }
        });
        getBaseLayout().getMruButtonContainer().setOnModeSelectListener(this.mModeSelectListener);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getBaseLayout().getMruButtonContainer().getLayoutParams();
        layoutParams.rightMargin = getBaseLayout().calculateCaptureButtonAreaHeight();
        getBaseLayout().getMruButtonContainer().setLayoutParams(layoutParams);
    }

    private void updateFrontAngleSwitchButton() {
        this.mFrontAngleSwitchButton = getBaseLayout().getFrontAngleSwitchButton();
        if (this.mFrontAngleSwitchButton == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("updateFrontAngleSwitchButton mFrontAngleSwitchButton is NULL");
            }
        } else if (getCapturingMode().isFront()) {
            FrontAngle frontAngle = (FrontAngle) this.mStateMachine.getUserSetting().get(UserSettingKey.FRONT_ANGLE);
            if (CamLog.VERBOSE) {
                CamLog.d("updateFrontAngleSwitchButton value: " + frontAngle);
            }
            this.mFrontAngleSwitchButton.switchFrontAngle(frontAngle);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void onSettingChanged(UserSettingValue userSettingValue) {
        updatePrimaryShortcutIcon(userSettingValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transitionModeOnNavigator(int i) {
        NavigatorContents navigatorContentsPrevious;
        NavigatorContents navigatorContentsValueOf = NavigatorContents.valueOf(getCapturingMode());
        int iIndexOf = NavigatorContents.indexOf(navigatorContentsValueOf);
        int length = (NavigatorContents.values().length - i) - 1;
        NavigatorContents navigatorContents = NavigatorContents.values()[length];
        if (CamLog.DEBUG) {
            CamLog.d("invoke current:" + navigatorContentsValueOf.name() + ", target:" + navigatorContents.name());
        }
        if (length > iIndexOf) {
            navigatorContentsPrevious = navigatorContentsValueOf.next();
        } else if (length >= iIndexOf) {
            return;
        } else {
            navigatorContentsPrevious = navigatorContentsValueOf.previous();
        }
        AnimationRequest animationRequest = new AnimationRequest(AnimationRequest.AnimationType.MODE_ICON, AnimationRequest.AnimationDegree.START, getCapturingMode(navigatorContentsValueOf, getCapturingMode()), getCapturingMode(navigatorContentsPrevious, getCapturingMode()));
        if (requestAnimation(animationRequest)) {
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest);
        }
    }

    private void updatePrimaryShortcutIcons() {
        if (this.mPrimaryShortcutGroup != null) {
            this.mPrimaryShortcutGroup.updatePrimaryShortcutIcons(getCapturingMode(), this.mStateMachine.getUserSetting(), this.mActivity.isOneShot());
        }
    }

    private void updatePrimaryShortcutIcon(UserSettingValue userSettingValue) {
        if (this.mPrimaryShortcutGroup != null) {
            this.mPrimaryShortcutGroup.updatePrimaryShortcutIcon(userSettingValue.getKey(), userSettingValue.getIconId());
        }
    }

    private void disablePrimaryShortcut() {
        if (this.mPrimaryShortcutGroup != null) {
            this.mPrimaryShortcutGroup.disable();
        }
    }

    private void updateScreenButtonImage(CapturingMode capturingMode) {
        ViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.PHOTO_READY;
        switch (capturingMode) {
            case SCENE_RECOGNITION:
            case SUPERIOR_FRONT:
            case NORMAL:
            case FRONT_PHOTO:
                headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.PHOTO_READY;
                break;
            case VIDEO:
            case FRONT_VIDEO:
                headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.VIDEO_READY;
                break;
            case SLOW_MOTION:
                switch ((SlowMotion) this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION)) {
                    case SUPER_SLOW_MOTION:
                        headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY;
                        break;
                    case SUPER_SLOW_SHOT:
                        headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY;
                        break;
                    case STANDARD_SLOW_MOTION:
                        headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY;
                        break;
                }
        }
        changeScreenButtonImage(headUpDisplaySetupState, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onToggleCameraSwitch() {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_SWITCH_CAMERA, AnimationRequest.AnimationType.NONE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupAnimations() {
        getBaseLayout().getRootView().findViewById(R.id.contents_container).getGlobalVisibleRect(new Rect());
    }

    private boolean isEvfRotateRequired() {
        if (this.mEvf == null) {
            return false;
        }
        Rect rect = this.mEvf.getRect();
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            if (rect.width() > rect.height()) {
                return true;
            }
        } else if (rect.width() < rect.height()) {
            return true;
        }
        return false;
    }

    private void resumeView(FastCapture fastCapture, boolean z) {
        CapturingMode launchCapturingMode = this.mStateMachine.getLaunchCapturingMode();
        if (isHeadUpDisplayReady()) {
            if (this.mInstantViewer != null && this.mInstantViewer.isOpened()) {
                this.mInstantViewer.hide();
            }
            this.mCanFocusRectanglesBeUpdated = true;
            this.mEvf.onResume();
            resume(launchCapturingMode, NavigatorContents.valueOf(launchCapturingMode));
        }
        if (this.mCaptureFeedback != null) {
            this.mCaptureFeedback.onResume();
        }
        if (fastCapture != FastCapture.LAUNCH_AND_CAPTURE && this.mCapturingModeWhenLastSetupHeadDisplay == launchCapturingMode && !z) {
            this.mScreenButtonHandler.refreshButton();
        }
        if (this.mActivity.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.LIFT_TRIGGER) {
            showMessageDialog(DialogId.PREDICTIVE_LAUNCH_DESCRIPTION, new Object[0]);
            getBaseLayout().setupPredictiveLaunchCoverView(new PredictiveLaunchCoverTouchListenerImpl(), ((PredictiveLaunch) this.mStateMachine.getUserSetting().get(UserSettingKey.PREDICTIVE_LAUNCH)).doCapture() ? PredictiveLaunchCoverView.PredictiveLaunchCoverType.TOUCH_TO_LAUNCH_AND_CAPTURE : PredictiveLaunchCoverView.PredictiveLaunchCoverType.TOUCH_TO_LAUNCH);
            changeLayoutTo(BaseLayoutPattern.CLEAR);
            setApplicationNavigatorEnabled(false);
            updateGridLineView();
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
            if (this.mLoopsManager == null) {
                this.mLoopsManager = new XperiaXLoopsManager(this.mActivity);
            }
            this.mLoopsManager.connect();
        }
    }

    private void pauseView() {
        pause();
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
        clearPreInflatedViews();
        if (this.mPreviewCover != null) {
            this.mPreviewCover.setVisibility(4);
        }
        this.mIsSetupHeadupDisplayInvoked = false;
        if (this.mLoopsManager != null && this.mLoopsManager.isConnected()) {
            this.mLoopsManager.disconnect();
        }
        this.mLoopsManager = null;
    }

    private void release() {
        this.mBaseLayout.release();
        this.mWindowDisplayFlashScreen = null;
        this.mActivity.removeOrienationListener(this);
        if (this.mActivity.getGeoTagManager() != null) {
            this.mActivity.getGeoTagManager().setLocationAcquiredListener(null);
        }
        this.mActivity.getStorage().removeStorageStateListener(this.mStorageStateListener);
        if (CamLog.VERBOSE) {
            CamLog.d("release() is called.");
        }
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
        hideSurfaceBlinderView();
        releaseSurfaceBlinderView();
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

    private void onViewFinderStateChanged(StateMachine.CaptureState captureState, Object... objArr) throws Resources.NotFoundException {
        boolean zBooleanValue = false;
        if (CamLog.VERBOSE) {
            CamLog.d("onViewFinderStateChanged():[IN][currentState=" + captureState + "]");
        }
        switch (captureState) {
            case STATE_RESUME:
                this.mTouchEventDispatcher.start();
                this.mIsThermalWarningDialogShown = false;
                if (this.mActivity.awaitViewFinderReady()) {
                    resumeView((FastCapture) objArr[0], ((Boolean) objArr[1]).booleanValue());
                    break;
                }
                break;
            case STATE_PHOTO_READY:
                this.mCurrentDisplayingUiComponent = null;
                this.mBurstShootingRejectedReason = ViewFinder.BurstRejectedReason.NONE;
                changeToPhotoReadyView(false);
                if (objArr == null || objArr.length == 0) {
                    requestToDimSystemUi();
                    break;
                } else if (objArr[0] != ViewFinder.UiComponentKind.ZOOM_BAR) {
                    requestToDimSystemUi();
                    break;
                }
                break;
            case STATE_VIDEO_READY:
                this.mCurrentDisplayingUiComponent = null;
                if (getCapturingMode() == CapturingMode.SLOW_MOTION && !this.mActivity.getLaunchCondition().getLaunchCameraMode().isSlowMotion()) {
                    SlowMotion slowMotion = (SlowMotion) this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION);
                    postSlowMotionHintText();
                    if (AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[slowMotion.ordinal()] == 1 && this.mHintText != null) {
                        this.mHintText.cancel(HintTextSuperSlowMotionVideoRecording.createTag(true));
                        this.mHintText.cancel(HintTextSuperSlowMotionVideoRecording.createTag(false));
                    }
                }
                if (isHeadUpDisplayReady()) {
                    changeToVideoReadyView();
                }
                if (objArr == null || objArr.length == 0) {
                    requestToDimSystemUi();
                    break;
                } else if (objArr[0] != ViewFinder.UiComponentKind.ZOOM_BAR) {
                    requestToDimSystemUi();
                    break;
                }
                break;
            case STATE_CAPTURE_COUNTDOWN:
                changeToSelftimerView(((Boolean) objArr[0]).booleanValue());
                break;
            case STATE_OPERATION_RESTRICTED:
                if (objArr != null && objArr.length != 0) {
                    this.mCurrentDisplayingUiComponent = (ViewFinder.UiComponentKind) objArr[0];
                    changeToDialogView(this.mCurrentDisplayingUiComponent);
                    break;
                }
                break;
            case STATE_PHOTO_AF_SEARCH:
                changeToPhotoFocusSearchView();
                break;
            case STATE_PHOTO_AF_DONE:
                changeToPhotoFocusDoneView((Boolean) objArr[0]);
                break;
            case STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE:
                changeToPhotoCaptureWaitForAfDoneView();
                break;
            case STATE_BURST_CAPTURE_WAIT_FOR_AF_DONE:
                changeToBurstCaptureWaitForAfDoneView();
                break;
            case STATE_PHOTO_CAPTURE:
                changeToPhotoCaptureView();
                clearTouchedScreenButtonGroup();
                break;
            case STATE_BURST_CAPTURE:
                changeToBurstCaptureView();
                break;
            case STATE_VIDEO_RECORDING:
                if (getCapturingMode() == CapturingMode.SLOW_MOTION && this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.STANDARD_SLOW_MOTION) {
                    changeToStandardSlowMotionRecordingView();
                    break;
                } else {
                    changeToVideoRecordingView();
                    break;
                }
            case STATE_VIDEO_STORE:
                attachSideAutoReview();
                break;
            case STATE_FATAL:
                showBlank();
                hideAndCancelAllView();
                break;
            case STATE_PAUSE:
                this.mIsRequestingStartActivity = false;
                this.mHandler.removeCallbacks(this.mAfterSwitchAnimationTask);
                this.mTouchEventDispatcher.start();
                hideAndCancelAllView();
                break;
            case STATE_WARNING:
                if (isHeadUpDisplayReady()) {
                    this.mSideTouchUi.destroyIcon();
                }
                if (this.mFocusRectangles != null) {
                    if (isTouchFocus()) {
                        disableSemiAutoControl();
                    }
                    hideAutoReview();
                    this.mFocusRectangles.clearAllFocus();
                    if (getCapturingMode() == CapturingMode.FRONT_VIDEO || getCapturingMode() == CapturingMode.VIDEO || getCapturingMode() == CapturingMode.SLOW_MOTION) {
                        changeToVideoReadyView();
                    } else {
                        changeToPhotoReadyView(false);
                    }
                }
                requestToDimSystemUi();
                break;
            case STATE_FINALIZE:
                release();
                getDownHeadUpDisplay();
                break;
            case STATE_VIDEO_RECORDING_PAUSING:
                changeToVideoRecordingPauseView();
                break;
            case STATE_CAMERA_SWITCHING:
                changeToModeTransitionView();
                break;
            case STATE_PHOTO_READY_FOR_RECORDING:
                if (objArr != null && objArr.length != 0) {
                    zBooleanValue = ((Boolean) objArr[0]).booleanValue();
                }
                changeToReadyForRecordView(zBooleanValue);
                break;
            case STATE_MODE_CHANGING:
                changeToModeTransitionView();
                break;
            case STATE_WAITING_EVF_PREPARED_IN_MODE_CHANGE:
                if (this.mHintText != null) {
                    this.mHintText.clearAll();
                    break;
                }
                break;
            case STATE_HIGH_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION:
                changeToSuperSlowMotionVideoHighFrameRateRecordingView();
                break;
            case STATE_LOW_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION:
                changeToSuperSlowMotionVideoLowFrameRateRecordingView();
                break;
            case STATE_WAIT_FOR_HIGH_FRAME_RATE_VIDEO_RECORDING_DONE:
                changeToWaitForHighFrameRateRecordingDoneView();
                break;
        }
    }

    public void startReturnModeAnimation() {
        AnimationRequest animationRequest = new AnimationRequest(AnimationRequest.AnimationType.MODE_SELECTOR, AnimationRequest.AnimationDegree.START, getCapturingMode(), CapturingMode.SCENE_RECOGNITION);
        if (requestAnimation(animationRequest)) {
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_TRANSITION_OPERATION, animationRequest);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean requestAnimation(AnimationRequest animationRequest) {
        if (CamLog.DEBUG) {
            CamLog.d("invoke source:" + animationRequest.mType + ", type:" + animationRequest.mDegree + ", from:" + animationRequest.mFrom.name() + ", target:" + animationRequest.mTarget.name());
        }
        switch (animationRequest.mType) {
            case MODE_TOUCH:
                return requestModeSwipeAnimation(animationRequest);
            case MODE_ICON:
                return requestModeIconAnimation(animationRequest);
            case MODE_SELECTOR:
                return requestModeSelectorAnimation(animationRequest);
            case MRU_SHORTCUT:
                return requestMostRecentlyUsedAnimation(animationRequest);
            case SWITCH_TOUCH:
                return requestSwitchAnimation(animationRequest);
            default:
                return false;
        }
    }

    private boolean requestModeSwipeAnimation(final AnimationRequest animationRequest) {
        switch (animationRequest.mDegree) {
            case START:
                boolean zRequestAnimation = this.mAnimationController.requestAnimation(animationRequest);
                if (zRequestAnimation) {
                    hideViews();
                }
                return zRequestAnimation;
            case EXEC:
                return this.mAnimationController.requestAnimation(animationRequest, new TransitionAnimationController.TransitionAnimationCallback() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.9
                    @Override // com.sonyericsson.android.camera.view.animation.TransitionAnimationController.TransitionAnimationCallback
                    public void onAnimationFinished() {
                        PerfLog.SWIPE_ANIMATION_END.transit();
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, animationRequest);
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(animationRequest.mFrom, animationRequest.mTarget);
                    }
                });
            case CANCEL:
                return this.mAnimationController.requestAnimation(animationRequest, new TransitionAnimationController.TransitionAnimationCallback() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.10
                    @Override // com.sonyericsson.android.camera.view.animation.TransitionAnimationController.TransitionAnimationCallback
                    public void onAnimationFinished() {
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, animationRequest);
                    }
                });
            case FINISH:
                return this.mAnimationController.requestAnimation(animationRequest);
            default:
                return false;
        }
    }

    private boolean requestModeIconAnimation(final AnimationRequest animationRequest) {
        int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()];
        if (i != 4) {
            switch (i) {
                case 1:
                    if (this.mAnimationController.requestAnimation(animationRequest)) {
                        final AnimationRequest animationRequest2 = new AnimationRequest(AnimationRequest.AnimationType.MODE_ICON, AnimationRequest.AnimationDegree.EXEC, animationRequest.mFrom, animationRequest.mTarget);
                        this.mActivity.runOnUiThread(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.11
                            @Override // java.lang.Runnable
                            public void run() throws Resources.NotFoundException {
                                if (CamLog.DEBUG) {
                                    CamLog.d("invoke current:" + animationRequest2.mFrom.name() + ", target:" + animationRequest2.mTarget.name());
                                }
                                ViewFinderImpl.this.hideViews();
                                ViewFinderImpl.this.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
                                ViewFinderImpl.this.requestAnimation(animationRequest2);
                            }
                        });
                        break;
                    }
                    break;
                case 2:
                    if (this.mAnimationController.requestAnimation(animationRequest, new TransitionAnimationController.TransitionAnimationCallback() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.12
                        @Override // com.sonyericsson.android.camera.view.animation.TransitionAnimationController.TransitionAnimationCallback
                        public void onAnimationFinished() {
                            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, animationRequest);
                        }
                    })) {
                        LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.ICON_TOUCH);
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(animationRequest.mFrom, animationRequest.mTarget);
                        break;
                    }
                    break;
            }
            return true;
        }
        return this.mAnimationController.requestAnimation(animationRequest);
    }

    private boolean requestModeSelectorAnimation(final AnimationRequest animationRequest) {
        int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()];
        if (i != 4) {
            switch (i) {
                case 1:
                    if (this.mAnimationController.requestAnimation(animationRequest)) {
                        final AnimationRequest animationRequest2 = new AnimationRequest(AnimationRequest.AnimationType.MODE_SELECTOR, AnimationRequest.AnimationDegree.EXEC, animationRequest.mFrom, animationRequest.mTarget);
                        this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.13
                            @Override // java.lang.Runnable
                            public void run() throws Resources.NotFoundException {
                                if (CamLog.DEBUG) {
                                    CamLog.d("invoke current:" + animationRequest2.mFrom.name() + ", target:" + animationRequest2.mTarget.name());
                                }
                                ViewFinderImpl.this.hideSurface();
                                ViewFinderImpl.this.hideViews();
                                ViewFinderImpl.this.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
                                ViewFinderImpl.this.requestAnimation(animationRequest2);
                            }
                        });
                        break;
                    }
                    break;
                case 2:
                    if (this.mAnimationController.requestAnimation(animationRequest, new TransitionAnimationController.TransitionAnimationCallback() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.14
                        @Override // com.sonyericsson.android.camera.view.animation.TransitionAnimationController.TransitionAnimationCallback
                        public void onAnimationFinished() {
                            if (animationRequest.mFrom.isFront() != animationRequest.mTarget.isFront()) {
                                ViewFinderImpl.this.setIsCameraSwitching(true);
                                final AnimationRequest animationRequest3 = new AnimationRequest(AnimationRequest.AnimationType.MODE_SELECTOR, AnimationRequest.AnimationDegree.FINISH, animationRequest.mFrom, animationRequest.mTarget);
                                ViewFinderImpl.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.14.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (CamLog.DEBUG) {
                                            CamLog.d("invoke current:" + animationRequest3.mFrom.name() + ", target:" + animationRequest3.mTarget.name());
                                        }
                                        ViewFinderImpl.this.requestAnimation(animationRequest3);
                                    }
                                });
                            }
                            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, animationRequest);
                        }
                    })) {
                        LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MODE_SELECTOR);
                        LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MODE_SELECTOR);
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(animationRequest.mFrom, animationRequest.mTarget);
                        break;
                    }
                    break;
            }
            return true;
        }
        getBaseLayout().getModeButtonShortcut().update(ModeSelectorInternalMode.exists(animationRequest.mTarget));
        getBaseLayout().getMruButtonContainer().setAvailability(!ModeSelectorInternalMode.exists(animationRequest.mTarget));
        return this.mAnimationController.requestAnimation(animationRequest);
    }

    private boolean requestMostRecentlyUsedAnimation(final AnimationRequest animationRequest) {
        int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()];
        if (i != 4) {
            switch (i) {
                case 1:
                    if (this.mAnimationController.requestAnimation(animationRequest)) {
                        final AnimationRequest animationRequest2 = new AnimationRequest(AnimationRequest.AnimationType.MRU_SHORTCUT, AnimationRequest.AnimationDegree.EXEC, animationRequest.mFrom, animationRequest.mTarget);
                        this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.15
                            @Override // java.lang.Runnable
                            public void run() throws Resources.NotFoundException {
                                if (CamLog.DEBUG) {
                                    CamLog.d("invoke current:" + animationRequest2.mFrom.name() + ", target:" + animationRequest2.mTarget.name());
                                }
                                ViewFinderImpl.this.hideSurface();
                                ViewFinderImpl.this.hideViews();
                                ViewFinderImpl.this.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
                                ViewFinderImpl.this.requestAnimation(animationRequest2);
                            }
                        });
                        break;
                    }
                    break;
                case 2:
                    if (this.mAnimationController.requestAnimation(animationRequest, new TransitionAnimationController.TransitionAnimationCallback() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.16
                        @Override // com.sonyericsson.android.camera.view.animation.TransitionAnimationController.TransitionAnimationCallback
                        public void onAnimationFinished() {
                            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, animationRequest);
                            if (animationRequest.mFrom.isFront() != animationRequest.mTarget.isFront()) {
                                final AnimationRequest animationRequest3 = new AnimationRequest(AnimationRequest.AnimationType.MRU_SHORTCUT, AnimationRequest.AnimationDegree.FINISH, animationRequest.mFrom, animationRequest.mTarget);
                                ViewFinderImpl.this.mHandler.post(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.16.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (CamLog.DEBUG) {
                                            CamLog.d("invoke current:" + animationRequest3.mFrom.name() + ", target:" + animationRequest3.mTarget.name());
                                        }
                                        ViewFinderImpl.this.requestAnimation(animationRequest3);
                                    }
                                });
                            }
                        }
                    })) {
                        LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MRU_SHORTCUT);
                        LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MRU_SHORTCUT);
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(animationRequest.mFrom, animationRequest.mTarget);
                        break;
                    }
                    break;
            }
            return true;
        }
        getBaseLayout().getModeButtonShortcut().update(ModeSelectorInternalMode.exists(animationRequest.mTarget));
        getBaseLayout().getMruButtonContainer().setAvailability(!ModeSelectorInternalMode.exists(animationRequest.mTarget));
        return this.mAnimationController.requestAnimation(animationRequest);
    }

    private boolean requestSwitchAnimation(final AnimationRequest animationRequest) {
        switch (animationRequest.mDegree) {
            case START:
                return startDraggingSwitchStartedAnimation();
            case EXEC:
                return this.mAnimationController.requestAnimation(animationRequest, new TransitionAnimationController.TransitionAnimationCallback() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.18
                    @Override // com.sonyericsson.android.camera.view.animation.TransitionAnimationController.TransitionAnimationCallback
                    public void onAnimationFinished() {
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, animationRequest);
                        ViewFinderImpl.this.mHandler.post(ViewFinderImpl.this.mAfterSwitchAnimationTask);
                        ViewFinderImpl.this.showSurface();
                    }
                });
            case CANCEL:
                return this.mAnimationController.requestAnimation(animationRequest, new TransitionAnimationController.TransitionAnimationCallback() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.17
                    @Override // com.sonyericsson.android.camera.view.animation.TransitionAnimationController.TransitionAnimationCallback
                    public void onAnimationFinished() {
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINISH_TRANSITION_OPERATION, animationRequest);
                        ViewFinderImpl.this.resetAnimationProperty();
                    }
                });
            case FINISH:
                return this.mAnimationController.requestAnimation(animationRequest);
            default:
                return false;
        }
    }

    private void hideAndCancelAllView() throws Resources.NotFoundException {
        if (isInSelfTimerCountDown()) {
            cancelSelfTimerCountDownView();
            changeToPhotoReadyView(false);
        }
        hideAutoReview();
        hideSurfaceBlinderView();
        this.mEvf.hide();
        pauseView();
        changeToPauseView();
        getBaseLayout().releasePredictiveLaunchCover();
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

    /* JADX INFO: Access modifiers changed from: private */
    public void changeLayoutTo(LayoutPattern layoutPattern) {
        changeLayoutTo(layoutPattern, false);
    }

    private boolean needToShowGeoTagIndicator() {
        return !this.mActivity.isOneShot() || PermissionsUtil.areCallerGeoPermissionsGranted(this.mActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeLayoutTo(LayoutPattern layoutPattern, boolean z) {
        if (isHeadUpDisplayReady()) {
            if (CamLog.VERBOSE) {
                CamLog.d("changeLayoutTo: " + layoutPattern);
            }
            if (!z) {
                this.mLayoutPatternApplier.apply(layoutPattern);
            } else {
                this.mLayoutPatternApplier.apply(BaseLayoutPattern.CLEAR);
            }
            this.mLayoutPattern = layoutPattern;
            if (isPreviewLayout(layoutPattern)) {
                if (needToShowGeoTagIndicator()) {
                    this.mBaseLayout.getGeoTagIndicator().set(GeotagManager.isGeoTagEnabled(this.mActivity.getStoredSettings().getUserSettings(), this.mActivity));
                }
                if (this.mActivity.getGeoTagManager() != null) {
                    this.mBaseLayout.getGeoTagIndicator().isAcquired(isAcquired());
                }
            }
            updateAllOverlayControlVisibility();
            updateVisibilityForSpecificDisplaySize();
            if (layoutPattern.equals(BaseLayoutPattern.SELFTIMER)) {
                clearTouchedScreenButtonGroup();
            }
        }
        if (this.mActivity.isOneShot()) {
            getBaseLayout().hideContentsViewController();
        }
    }

    private void changeToPhotoReadyView(boolean z) throws Resources.NotFoundException {
        changeLayoutTo(selectLayoutPatternForPreview());
        if (isHeadUpDisplayReady() && !predictiveLaunchCoverExists()) {
            CapturingMode capturingMode = getCapturingMode();
            if (this.mStateMachine.getCurrentCaptureState() == StateMachine.CaptureState.STATE_WARNING) {
                this.mHintText.clearAll();
            }
            if (!isOverlayControlEnabled()) {
                this.mHintText.showAll();
            } else {
                this.mHintText.show(HintTextContent.HintPriority.HIGH);
            }
            if (this.mInstantViewer != null && this.mInstantViewer.isOpened()) {
                this.mInstantViewer.hide();
            }
            if (z) {
                this.mFocusRectangles.clearAllFocusExceptFace();
            } else {
                this.mFocusRectangles.onUiComponentRemoved();
            }
            boolean z2 = true;
            applySmileFocusThreshold(true);
            this.mFocusRectangles.clearFaceDetection();
            this.mFocusRectangles.reset();
            showPhotoSmileCaptureIndicator();
            hideVideoSmileCaptureIndicator();
            setFrontAngleSwitchButtonVisibility(isFront());
            setLeftIconsVisibility(true);
            setOrientation(getOrientation());
            if (capturingMode == CapturingMode.NORMAL || capturingMode == CapturingMode.FRONT_PHOTO) {
                getBaseLayout().getSceneIndicator().set(false);
                getBaseLayout().getConditionIndicator().set(false);
                setApplicationNavigatorEnabled(false);
            } else {
                updateVisibilityForSpecificDisplaySize();
            }
            getBaseLayout().setViewFinderGestureDetectorEnabled(true, true);
            changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.PHOTO_READY, false);
            CapturingMode capturingMode2 = getCapturingMode();
            if (!ModeSelectorInternalMode.exists(capturingMode2) && !capturingMode2.equals(CapturingMode.FRONT_PHOTO)) {
                z2 = false;
            }
            getBaseLayout().getModeButtonShortcut().update(z2);
            this.mSideTouchUi.detachTo(SideTouchUi.Type.ZOOM_BAR);
            this.mSideTouchUi.destroyTo(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL);
            if (this.mIsNeedDisplayToastChangeInternalStoarge) {
                this.mIsNeedDisplayToastChangeInternalStoarge = false;
                showToastMessage(ToastContent.ToastID.CHANGE_DESTINATION_TO_SAVE);
            }
        }
    }

    private void changeToSelftimerView(boolean z) throws Resources.NotFoundException {
        changeLayoutTo(BaseLayoutPattern.SELFTIMER);
        if (isHeadUpDisplayReady()) {
            if (this.mSideTouchUi.containsIn(SideTouchUi.Type.CAPTURE_COUNTDOWN, SideTouchUi.Type.VIDEO_COUNTDOWN)) {
                this.mSideTouchUi.showIcon();
                return;
            }
            changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.PHOTO_READY, true);
            this.mSettingDialogStack.closeAllSettingDialogs();
            setLeftIconsVisibility(false);
            setFrontAngleSwitchButtonVisibility(false);
            if (z) {
                showSelfTimerCountDownView();
                startSelfTimerCountDownAnimation();
            }
            if (this.mSideTouchUi.containsIn(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL)) {
                this.mSideTouchUi.showIcon();
                this.mScreenButtonHandler.clearMain();
            } else {
                this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.CANCEL_SELFTIMER_LARGE, getOrientation(), true);
            }
            hidePhotoSmileCaptureIndicator();
            setOrientation(getOrientation());
            hideApplicationNavigator();
            hideAutoReview();
            if (!isTouchCaptureEnabled()) {
                this.mFocusRectangles.setLockedBySelfTimer(true);
            }
            if (this.mHintText != null) {
                this.mHintText.hide();
            }
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        }
    }

    private void showPhotoSmileCaptureIndicator() {
        getBaseLayout().getPhotoSmileCaptureIndicator().show();
    }

    private boolean isObjectTrackingEnabled() {
        return this.mStateMachine.getUserSetting().get(UserSettingKey.OBJECT_TRACKING) == ObjectTracking.ON;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTouchCaptureEnabled() {
        TouchCapture touchCapture;
        if (this.mTouchCapture != null) {
            touchCapture = this.mTouchCapture;
        } else {
            touchCapture = (TouchCapture) this.mStateMachine.getUserSetting().get(getCapturingMode(), UserSettingKey.TOUCH_CAPTURE);
        }
        if (touchCapture == null) {
            return false;
        }
        switch (touchCapture) {
            case ON:
                return true;
            case FRONT_ONLY:
                if (isFront()) {
                    return true;
                }
            default:
                return false;
        }
    }

    private boolean isSmileShutterEnabled() {
        return ((SmileCapture) this.mStateMachine.getUserSetting().get(UserSettingKey.SMILE_CAPTURE)).isSmileCaptureOn();
    }

    private void hidePhotoSmileCaptureIndicator() {
        getBaseLayout().getPhotoSmileCaptureIndicator().hide();
    }

    private void showVideoSmileCaptureIndicator() {
        getBaseLayout().getVideoSmileCaptureIndicator().show();
    }

    private void hideVideoSmileCaptureIndicator() {
        getBaseLayout().getVideoSmileCaptureIndicator().hide();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public int getSelectedFaceSmileScore() {
        if (this.mFocusRectangles == null) {
            return 0;
        }
        return this.mFocusRectangles.getSelectedFaceSmileScore();
    }

    private void changeToVideoReadyView() throws Resources.NotFoundException {
        changeLayoutTo(selectLayoutPatternForPreview());
        if (isHeadUpDisplayReady()) {
            CapturingMode capturingMode = getCapturingMode();
            if (this.mStateMachine.getCurrentCaptureState() == StateMachine.CaptureState.STATE_WARNING) {
                this.mHintText.clearAll();
            }
            if (!isOverlayControlEnabled()) {
                this.mHintText.showAll();
            } else {
                this.mHintText.show(HintTextContent.HintPriority.HIGH);
            }
            getBaseLayout().getSceneIndicator().set(false);
            getBaseLayout().getConditionIndicator().set(false);
            updateVideoHdrCondition(capturingMode, (VideoHdr) this.mStateMachine.getUserSetting().get(capturingMode, UserSettingKey.VIDEO_HDR), false);
            if (this.mInstantViewer != null && this.mInstantViewer.isOpened()) {
                this.mInstantViewer.hide();
            }
            if (capturingMode != CapturingMode.SLOW_MOTION) {
                changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.VIDEO_READY, false);
            } else {
                switch ((SlowMotion) this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION)) {
                    case SUPER_SLOW_MOTION:
                        changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY, false);
                        break;
                    case SUPER_SLOW_SHOT:
                        changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY, false);
                        break;
                    case STANDARD_SLOW_MOTION:
                        changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY, false);
                        break;
                }
                this.mFocusRectangles.clearFaceDetection();
            }
            this.mFocusRectangles.clearExceptTouchFocus();
            this.mFocusRectangles.stopRecording();
            this.mFocusRectangles.setEnableFaceFocusTouch(true);
            applySmileFocusThreshold(true);
            hidePhotoSmileCaptureIndicator();
            hideVideoSmileCaptureIndicator();
            if (capturingMode == CapturingMode.SLOW_MOTION) {
                setApplicationNavigatorEnabled(false);
            } else {
                updateVisibilityForSpecificDisplaySize();
            }
            hideVideoSmileCaptureIndicator();
            setFrontAngleSwitchButtonVisibility(isFront());
            setLeftIconsVisibility(true);
            getBaseLayout().setViewFinderGestureDetectorEnabled(true, true);
            CapturingMode capturingMode2 = getCapturingMode();
            getBaseLayout().getModeButtonShortcut().update(ModeSelectorInternalMode.exists(capturingMode2) || capturingMode2.equals(CapturingMode.FRONT_PHOTO));
            if (!this.mSideTouchUi.detachTo(SideTouchUi.Type.ZOOM_BAR) && !this.mSideTouchUi.containsIn(SideTouchUi.Type.AUTO_REVIEW)) {
                this.mSideTouchUi.destroyIcon();
            }
            this.mRecordingTimeProxy.bindReceiver(getBaseLayout().getRecordingIndicator());
            this.mRecordingTimeProxy.reset();
        }
    }

    private void changeToVideoRecordingPauseView() throws Resources.NotFoundException {
        if (this.mLayoutPattern == BaseLayoutPattern.PAUSE_RECORDING) {
            return;
        }
        changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING);
        if (isHeadUpDisplayReady()) {
            applySmileFocusThreshold(true);
            changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.VIDEO_PAUSING, false);
            if (getBaseLayout().getRecordingIndicator() != null) {
                getBaseLayout().getRecordingIndicator().setIndicator(false);
            }
            showVideoSmileCaptureIndicator();
            setFrontAngleSwitchButtonVisibility(false);
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
            setOrientation(getOrientation());
            if (getBaseLayout().getContentsViewController().isLoadingInProvisionalContent()) {
                getBaseLayout().getContentsViewController().show();
            }
            if (this.mHintText != null) {
                this.mHintText.showAll();
            }
            if (this.mSideTouchUi.containsAll(SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE, SideTouchUi.Type.COVERING)) {
                changeLayoutTo(BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING);
                getBaseLayout().getZoomBar().hideDelayed();
                return;
            }
            if (this.mSideTouchUi.detachTo(SideTouchUi.Type.ZOOM_BAR)) {
                if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE)) {
                    changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING, true);
                }
            } else {
                if (!this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR)) {
                    if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE)) {
                        changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING, true);
                        return;
                    }
                    return;
                }
                this.mSideTouchUi.setUiOrientation(this.mRecordingOrientation);
                if (this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_HDR) == VideoHdr.HDR_ON) {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.RECORDING_HDR_PAUSE, null);
                } else {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.RECORDING_PAUSE, null);
                }
                this.mSideTouchUi.setUiOrientation(this.mOrientation);
                this.mSideTouchUi.showIcon();
                changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING, true);
            }
        }
    }

    private void changeToPauseView() {
        changeLayoutTo(BaseLayoutPattern.CLEAR);
        hideApplicationNavigator();
        if (isHeadUpDisplayReady()) {
            if (this.mFocusRectangles != null) {
                this.mFocusRectangles.clearAllFocus();
            }
            if (this.mIsModeChanging) {
                this.mIsModeChanging = false;
            } else {
                getBaseLayout().getContentsViewController().remove();
            }
            hideAutoReview();
            setFrontAngleSwitchButtonVisibility(false);
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
            this.mSideTouchUi.destroyIcon();
        }
    }

    private void changeToZoomingView() {
        changeLayoutTo(BaseLayoutPattern.ZOOMING);
        if (isHeadUpDisplayReady()) {
            applySmileFocusThreshold(false);
            setFrontAngleSwitchButtonVisibility(false);
            setLeftIconsVisibility(false);
            hideAutoReview();
            hideApplicationNavigator();
            if (this.mHintText != null) {
                this.mHintText.hide();
            }
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
            this.mFocusRectangles.setEnableFaceFocusTouch(false);
            this.mSideTouchUi.destroyIcon();
        }
    }

    private void changeToPhotoFocusView() {
        if (isHeadUpDisplayReady()) {
            setFrontAngleSwitchButtonVisibility(false);
            setLeftIconsVisibility(false);
            hideApplicationNavigator();
            hideAutoReview();
            if (this.mHintText != null) {
                this.mHintText.hide();
            }
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        }
    }

    private void changeToPhotoFocusSearchView() {
        changeLayoutTo(BaseLayoutPattern.FOCUS_SEARCHING);
        if (isHeadUpDisplayReady()) {
            changeToPhotoFocusView();
            if (PlatformCapability.isFocusSupported(getCapturingMode().getCameraId())) {
                if (this.mStateMachine.getUserSetting().get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                    this.mFocusRectangles.onAutoFocusStarted();
                } else if (isTouchFocus()) {
                    this.mFocusRectangles.onUiComponentOverlaid();
                }
            }
            setFrontAngleSwitchButtonVisibility(false);
            this.mSideTouchUi.detachTo(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL);
        }
    }

    private void changeToPhotoFocusDoneView(Boolean bool) {
        changeLayoutTo(BaseLayoutPattern.FOCUS_DONE);
        if (isHeadUpDisplayReady()) {
            changeToPhotoFocusView();
            if (PlatformCapability.isFocusSupported(getCapturingMode().getCameraId()) && this.mStateMachine.getUserSetting().get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                this.mFocusRectangles.onAutoFocusDone(bool.booleanValue());
            }
            setFrontAngleSwitchButtonVisibility(false);
        }
    }

    private void changeToPhotoCaptureWaitForAfDoneView() {
        changeLayoutTo(BaseLayoutPattern.CAPTURE);
        if (isHeadUpDisplayReady()) {
            changeToPhotoFocusView();
            if (PlatformCapability.isFocusSupported(getCapturingMode().getCameraId()) && this.mStateMachine.getUserSetting().get(UserSettingKey.FOCUS_RANGE) == FocusRange.AF) {
                this.mFocusRectangles.onAutoFocusStarted();
            }
            setFrontAngleSwitchButtonVisibility(false);
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
            if (this.mSideTouchUi.detachTo(SideTouchUi.Type.CAPTURE_COUNTDOWN) || this.mSideTouchUi.detachTo(SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL)) {
                return;
            }
            this.mSideTouchUi.destroyIcon();
        }
    }

    private void changeToBurstCaptureWaitForAfDoneView() {
        if (this.mLayoutPattern != BaseLayoutPattern.FOCUS_SEARCHING) {
            changeToPhotoFocusView();
        }
        changeToPhotoCaptureWaitForAfDoneView();
    }

    private void changeToPhotoCaptureView() {
        changeLayoutTo(BaseLayoutPattern.CAPTURE);
        if (isHeadUpDisplayReady()) {
            setFrontAngleSwitchButtonVisibility(false);
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        }
    }

    private void changeToBurstCaptureView() {
        changeLayoutTo(BaseLayoutPattern.CAPTURE);
        if (isHeadUpDisplayReady()) {
            hideApplicationNavigator();
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        }
    }

    private void changeToVideoRecordingView() throws Resources.NotFoundException {
        if (this.mLayoutPattern == BaseLayoutPattern.RECORDING) {
            return;
        }
        changeLayoutTo(BaseLayoutPattern.RECORDING);
        if (isHeadUpDisplayReady()) {
            getBaseLayout().getGeoTagIndicator().hide();
            if (getCapturingMode() != CapturingMode.SLOW_MOTION) {
                changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING, false);
            } else {
                cancelSlowMotionHintText();
            }
            getBaseLayout().getRecordingIndicator().setIndicator(true);
            this.mFocusRectangles.startRecording();
            applySmileFocusThreshold(true);
            setLeftIconsVisibility(false);
            setFrontAngleSwitchButtonVisibility(false);
            if (getBaseLayout().getContentsViewController().isLoadingInProvisionalContent()) {
                getBaseLayout().getContentsViewController().show();
            } else {
                getBaseLayout().getContentsViewController().hide();
            }
            showVideoSmileCaptureIndicator();
            hidePhotoSmileCaptureIndicator();
            this.mFocusRectangles.clearExceptTouchFocus();
            this.mFocusRectangles.onUiComponentRemoved();
            this.mFocusRectangles.setEnableFaceFocusTouch(true);
            setOrientation(getOrientation());
            if (this.mHintText != null) {
                this.mHintText.showAll();
            }
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
            hideApplicationNavigator();
            hideAutoReview();
            if (this.mSideTouchUi.containsAll(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR, SideTouchUi.Type.COVERING)) {
                changeLayoutTo(BaseLayoutPattern.ZOOMING_IN_RECORDING);
                getBaseLayout().getZoomBar().hideDelayed();
                return;
            }
            if (this.mSideTouchUi.detachTo(SideTouchUi.Type.ZOOM_BAR)) {
                if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR)) {
                    changeLayoutTo(BaseLayoutPattern.RECORDING, true);
                }
            } else {
                if (!this.mSideTouchUi.containsIn(SideTouchUi.Type.VIDEO_COUNTDOWN, SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE)) {
                    if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR)) {
                        changeLayoutTo(BaseLayoutPattern.RECORDING, true);
                        return;
                    }
                    return;
                }
                this.mSideTouchUi.setUiOrientation(this.mRecordingOrientation);
                if (this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_HDR) == VideoHdr.HDR_ON) {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.RECORDING_HDR, null);
                } else {
                    this.mSideTouchUi.attachIcon(SideTouchUi.Type.RECORDING, null);
                }
                this.mSideTouchUi.setUiOrientation(this.mOrientation);
                this.mSideTouchUi.showIcon();
                changeLayoutTo(BaseLayoutPattern.RECORDING, true);
            }
        }
    }

    private void changeToSuperSlowMotionVideoLowFrameRateRecordingView() throws Resources.NotFoundException {
        changeToVideoRecordingView();
        this.mScreenButtonHandler.setMainRotatability(getOrientation(), false);
        changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_MOTION_RECORDING, false);
        showSuperSlowMotionVideoRecordingHintText();
        setFrontAngleSwitchButtonVisibility(false);
    }

    private void changeToSuperSlowMotionVideoHighFrameRateRecordingView() throws Resources.NotFoundException {
        changeToVideoRecordingView();
        changeLayoutTo(BaseLayoutPattern.HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION);
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.clearOption2();
        if (isTouchCaptureEnabled() && this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.SUPER_SLOW_SHOT) {
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE, this.mRecordingOrientation, false, false);
        } else {
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION_PRESSED, this.mRecordingOrientation, false, false);
        }
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        setFrontAngleSwitchButtonVisibility(false);
    }

    private void changeToWaitForHighFrameRateRecordingDoneView() throws Resources.NotFoundException {
        changeToVideoRecordingView();
        this.mScreenButtonHandler.setMainRotatability(getOrientation(), false);
        if (this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.SUPER_SLOW_SHOT) {
            changeLayoutTo(BaseLayoutPattern.HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION);
            this.mScreenButtonHandler.clearOption1();
            this.mScreenButtonHandler.clearOption2();
            if (isTouchCaptureEnabled()) {
                this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE, this.mRecordingOrientation, false, false);
            } else {
                this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION_DISABLED, this.mRecordingOrientation, false, false);
            }
        } else {
            this.mScreenButtonHandler.clearOption1();
            this.mScreenButtonHandler.setOption2(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_SMALL, this.mRecordingOrientation, false);
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION_DISABLED, this.mRecordingOrientation, false);
            postHintText(new HintTextSuperSlowMotionVideoRecording(true));
            this.mHintText.showAll();
        }
        setFrontAngleSwitchButtonVisibility(false);
    }

    private void changeToStandardSlowMotionRecordingView() throws Resources.NotFoundException {
        changeToVideoRecordingView();
        changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_RECORDING, false);
    }

    private void changeToReadyForRecordView(boolean z) {
        if (!z) {
            changeLayoutTo(BaseLayoutPattern.FOCUS_DONE);
        }
        if (isHeadUpDisplayReady()) {
            if (!z) {
                setFrontAngleSwitchButtonVisibility(false);
                setLeftIconsVisibility(false);
                hideApplicationNavigator();
                hideAutoReview();
                this.mFocusRectangles.setEnableFaceFocusTouch(false);
            }
            if (this.mHintText != null) {
                this.mHintText.hide();
            }
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void hideHudIcons() {
        changeLayoutTo(BaseLayoutPattern.CAPTURE);
    }

    private void changeToVideoZoomingWhileRecordingView() {
        switch ((BaseLayoutPattern) this.mLayoutPattern) {
            case RECORDING:
            case ZOOMING_IN_RECORDING:
                changeLayoutTo(BaseLayoutPattern.ZOOMING_IN_RECORDING);
                break;
            case PAUSE_RECORDING:
            case ZOOMING_IN_PAUSE_RECORDING:
                changeLayoutTo(BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING);
                break;
        }
        if (isHeadUpDisplayReady()) {
            this.mFocusRectangles.clearExceptTouchFocus();
            applySmileFocusThreshold(false);
            hideVideoSmileCaptureIndicator();
            if (this.mHintText != null) {
                this.mHintText.hide();
            }
            setFrontAngleSwitchButtonVisibility(false);
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
            this.mFocusRectangles.setEnableFaceFocusTouch(false);
            if (this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR, SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE, SideTouchUi.Type.ZOOM_BAR)) {
                this.mSideTouchUi.attachIcon(SideTouchUi.Type.COVERING, null);
            }
        }
    }

    private void changeToDialogView(ViewFinder.UiComponentKind uiComponentKind) {
        if (isHeadUpDisplayReady()) {
            if (uiComponentKind != ViewFinder.UiComponentKind.OVERLAY_CONTROL_SEEKING || !isTouchFocus()) {
                this.mFocusRectangles.onUiComponentOverlaid();
            }
            switch (uiComponentKind) {
                case NOTICE_DIALOG:
                case FLASH_DIALOG:
                case SELF_TIMER_DIALOG:
                case ASPECT_RATIO_DIALOG:
                case FUSION_MODE_DIALOG:
                case VIDEO_HDR_DIALOG:
                case HDR_DIALOG:
                case SETTING_DIALOG:
                case MODE_SELECTOR:
                    changeLayoutTo(BaseLayoutPattern.SETTING);
                    this.mHintText.hide();
                    break;
                case REVIEW_WINDOW:
                    changeLayoutTo(BaseLayoutPattern.CLEAR);
                    setLeftIconsVisibility(false);
                    break;
                case OVERLAY_CONTROL_SEEKING:
                    changeLayoutTo(BaseLayoutPattern.OVERLAY_CONTROL_SEEKING);
                    break;
                case TUTORIAL:
                    openTutorial(TutorialController.DisplayTrigger.CHANGE_MODE);
                    break;
            }
            if (uiComponentKind == ViewFinder.UiComponentKind.OVERLAY_CONTROL_SEEKING) {
                setFrontAngleSwitchButtonVisibility(isFront());
                setFrontAngleSwitchButtonClickable(false);
            } else {
                setFrontAngleSwitchButtonVisibility(false);
            }
            getBaseLayout().setViewFinderGestureDetectorEnabled(false, false);
            this.mSideTouchUi.destroyIcon();
        }
    }

    public boolean openTutorial(TutorialController.DisplayTrigger displayTrigger) {
        if (this.mActivity.isOneShot() || this.mActivity.getLaunchCondition().getLaunchTrigger() == LaunchCondition.LaunchTrigger.GOOGLE_ASSISTANT) {
            return false;
        }
        boolean zOpen = getBaseLayout().getTutorial().open(TutorialController.OpenType.create(displayTrigger), this.mActivity.getStoredSettings(), null);
        if (zOpen) {
            changeLayoutTo(BaseLayoutPattern.CLEAR);
            setApplicationNavigatorEnabled(false);
        }
        return zOpen;
    }

    boolean isTutorialOpened() {
        return this.mBaseLayout.getTutorial().isOpened();
    }

    private void changeToModeTransitionView() throws Resources.NotFoundException {
        changeLayoutTo(BaseLayoutPattern.MODE_CHANGING);
        hideViews();
        disablePrimaryShortcut();
        disableModeIconClickable();
        sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_CLOSE_ALL_DIALOGS, new Object[0]);
        cancelPredictiveCaptureIndicatorAnimation();
        setFrontAngleSwitchButtonVisibility(false);
        if (this.mFocusRectangles != null) {
            this.mFocusRectangles.clearFaceDetection();
        }
        this.mToastContent.closeMessage();
        clearMessageDialog();
        if (this.mHintText != null) {
            this.mHintText.clearAll();
        }
        hideAutoReview();
        hideZoomBar();
        this.mIsModeChanging = true;
        this.mSideTouchUi.destroyIcon();
    }

    private void onCapturingModeChanged(CapturingMode capturingMode, boolean z, AnimationRequest.AnimationType animationType) throws Resources.NotFoundException {
        if (CamLog.DEBUG) {
            CamLog.d("onCapturingModeChanged()  request:" + capturingMode.name());
        }
        Rect previewSize = this.mCameraDevice.getPreviewSize();
        if (previewSize != null && isHeadUpDisplayReady()) {
            Size sizeAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getSizeAccordingToLayoutOrientation(new Size(previewSize.width(), previewSize.height()));
            int width = sizeAccordingToLayoutOrientation.getWidth();
            int height = sizeAccordingToLayoutOrientation.getHeight();
            Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect(this.mActivity, width / height, this.mScreenAspect);
            PositionConverter.getInstance().setSurfaceSize(surfaceViewRect.width(), surfaceViewRect.height());
            PositionConverter.getInstance().setPreviewSize(width, height);
            this.mFocusRectangles.updateDevicePreviewSize(width, height);
            this.mFocusRectangles.clearExceptTouchFocus();
            this.mCapturingModeWhenLastSetupHeadDisplay = capturingMode;
            applyShutterTriggerSettings();
            hideVideoSmileCaptureIndicator();
            setZoomRatio(0);
            setOrientation(this.mActivity.getOrientation());
            updateGridLineView(capturingMode);
            if (z) {
                startModeChangedAnimation(getCapturingMode(), capturingMode, animationType);
            }
            switch (capturingMode) {
                case NORMAL:
                case FRONT_PHOTO:
                    enableOverlayControl(getBaseLayout().getImageQualityControl());
                    setApplicationNavigatorEnabled(false);
                    setMruAvailability(false);
                    break;
                case SLOW_MOTION:
                    getBaseLayout().getSuperSlowMotionTriggerAnimation().prepareViews();
                    setApplicationNavigatorEnabled(false);
                    if (!this.mStateMachine.isTutorialNeededToBeShownForCurrentMode()) {
                        showHiSpeedSdCardRecommendDialogOnModeChange();
                    }
                    setMruAvailability(false);
                    break;
                default:
                    resumeApplicationNavigator(NavigatorContents.valueOf(getCapturingMode()));
                    updateVisibilityForSpecificDisplaySize();
                    if (getBaseLayout().getMruButtonContainer().hasInternalMode()) {
                        setMruAvailability(true);
                    }
                    attemptSetupMruButton(capturingMode);
                    break;
            }
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void sendViewUpdateEvent(ViewFinder.ViewUpdateEvent viewUpdateEvent, Object... objArr) throws Resources.NotFoundException {
        UserSettingKey userSettingKey;
        if (CamLog.DEBUG) {
            CamLog.d("sendViewUpdateEvent() event: " + viewUpdateEvent);
        }
        switch (viewUpdateEvent) {
            case EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY:
                setupHeadUpDisplay((ViewFinder.HeadUpDisplaySetupState) objArr[0]);
                checkupThermalCoolingRequest();
                updateSecondaryShortcutOnScreenButtonResource();
                break;
            case EVENT_REQUEST_RESIZE_EVF_SCOPE:
                if (this.mActivity != null) {
                    resizeEvfScope((Rect) objArr[0]);
                    if (this.mHintText != null) {
                        updateHintTextContainer((Rect) objArr[0]);
                    }
                    boolean zBooleanValue = ((Boolean) objArr[1]).booleanValue();
                    if (this.mIsSetupHeadupDisplayInvoked && zBooleanValue && !isTutorialOpened()) {
                        this.mIsSurfaceViewHideWhileAspectChanging = true;
                        this.mEvf.hide();
                        updateCaptureAreaSize();
                        updateGridLineView();
                    }
                    showSurface();
                    break;
                }
                break;
            case EVENT_REQUEST_PREPARE_RECORDING_INDICATOR:
                RecordingIndicator recordingIndicator = getBaseLayout().getRecordingIndicator();
                if (recordingIndicator != null) {
                    VideoHdr videoHdr = (VideoHdr) objArr[3];
                    if (getCapturingMode() != CapturingMode.SLOW_MOTION && videoHdr != VideoHdr.HDR_ON) {
                        if (((Boolean) objArr[2]).booleanValue()) {
                            recordingIndicator.setSequenceMode(true);
                        }
                        recordingIndicator.setConstraint(((Boolean) objArr[1]).booleanValue());
                        recordingIndicator.prepareBeforeRecording(((Integer) objArr[0]).intValue());
                        break;
                    } else {
                        recordingIndicator.setSequenceMode(false);
                        recordingIndicator.setConstraint(false);
                        recordingIndicator.prepareBeforeRecording(((Integer) objArr[0]).intValue());
                        break;
                    }
                }
                break;
            case EVENT_ON_CAPTURING_MODE_CHANGED:
                if (!isCameraSwitching()) {
                    onCapturingModeChanged((CapturingMode) objArr[0], ((Boolean) objArr[1]).booleanValue(), (AnimationRequest.AnimationType) objArr[2]);
                }
                updatePrimaryShortcutIcons();
                updateScreenButtonImage((CapturingMode) objArr[0]);
                if (this.mSettingDialogStack != null) {
                    this.mSettingDialogStack.setCapturingMode((CapturingMode) objArr[0]);
                    break;
                }
                break;
            case EVENT_ON_DETECTED_SCENE_CHANGED:
                onSceneModeChanged((CameraParameters.SceneRecognitionResult) objArr[0]);
                break;
            case EVENT_ON_FACE_DETECTION_STARTED:
                this.mFocusRectangles.startFaceDetection();
                break;
            case EVENT_ON_FACE_DETECTED:
                CameraParameters.FaceDetectionResult faceDetectionResult = (CameraParameters.FaceDetectionResult) objArr[0];
                if (this.mIsFaceDetectionIdSupported == null) {
                    if (!faceDetectionResult.extFaceList.isEmpty()) {
                        this.mIsFaceDetectionIdSupported = FaceDetectUtil.hasValidFaceId(faceDetectionResult);
                    }
                } else if (!this.mIsFaceDetectionIdSupported.booleanValue()) {
                    FaceDetectUtil.setUuidFaceDetectionResult(faceDetectionResult);
                }
                if (!predictiveLaunchCoverExists()) {
                    onFaceDetected(faceDetectionResult);
                    break;
                }
                break;
            case EVENT_ON_OBJECT_TRACKING_STARTED:
                hideAutoReview();
                this.mFocusRectangles.setObjectTrackingRectSupported(true);
                this.mFocusRectangles.startObjectTracking();
                applySmileFocusThreshold(false);
                getBaseLayout().getPhotoSmileCaptureIndicator().set(false);
                getBaseLayout().getVideoSmileCaptureIndicator().set(false);
                break;
            case EVENT_ON_OBJECT_TRACKING_TIMEOUT:
                this.mFocusRectangles.clearObjectTracking();
                break;
            case EVENT_ON_OBJECT_TRACKING_STOP:
                applySmileFocusThreshold(true);
                if (!getCapturingMode().isVideo()) {
                    SmileCapture smileCapture = (SmileCapture) this.mStateMachine.getUserSetting().get(UserSettingKey.SMILE_CAPTURE);
                    if (smileCapture != null) {
                        getBaseLayout().getPhotoSmileCaptureIndicator().set(smileCapture.isSmileCaptureOn());
                        break;
                    }
                } else {
                    VideoSmileCapture videoSmileCapture = (VideoSmileCapture) this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SMILE_CAPTURE);
                    if (videoSmileCapture != null) {
                        getBaseLayout().getVideoSmileCaptureIndicator().set(videoSmileCapture.isSmileCaptureOn());
                        break;
                    }
                }
                break;
            case EVENT_ON_TRACKED_OBJECT_STATE_UPDATED:
                onTrackedObjectStateUpdated((CameraParameters.ObjectTrackingResult) objArr[0]);
                break;
            case EVENT_ON_ZOOM_START:
                this.mZoomBarProxy.bindZoomBar(getBaseLayout().getZoomBar());
                this.mZoomBarProxy.update(getBaseLayout().getZoomBar().getZoomRatios(), ((Integer) objArr[0]).intValue());
                if (!this.mStateMachine.isRecording()) {
                    changeToZoomingView();
                    break;
                } else {
                    changeToVideoZoomingWhileRecordingView();
                    break;
                }
            case EVENT_ON_ZOOM_STOP:
                int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[((BaseLayoutPattern) this.mLayoutPattern).ordinal()];
                if (i == 2) {
                    if (getCapturingMode() == CapturingMode.SLOW_MOTION) {
                        SlowMotion slowMotion = (SlowMotion) this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION);
                        if (slowMotion == SlowMotion.STANDARD_SLOW_MOTION) {
                            changeToStandardSlowMotionRecordingView();
                            break;
                        } else if (slowMotion == SlowMotion.SUPER_SLOW_MOTION) {
                            changeToSuperSlowMotionVideoLowFrameRateRecordingView();
                            break;
                        }
                    } else {
                        changeToVideoRecordingView();
                        break;
                    }
                } else {
                    switch (i) {
                        case 4:
                            changeToVideoRecordingPauseView();
                            break;
                        case 5:
                            if (!getCapturingMode().isVideo()) {
                                changeToPhotoReadyView(false);
                                break;
                            } else {
                                if (getCapturingMode() == CapturingMode.SLOW_MOTION) {
                                    postSlowMotionHintText();
                                }
                                changeToVideoReadyView();
                                break;
                            }
                    }
                }
                break;
            case EVENT_ON_ZOOM_CHANGED:
                int iIntValue = ((Integer) objArr[0]).intValue();
                if (CamLog.VERBOSE) {
                    CamLog.d("EVENT_ON_ZOOM_CHANGED  cur:" + iIntValue);
                }
                Zoombar zoomBar = getBaseLayout().getZoomBar();
                if (zoomBar != null && zoomBar.getVisibility() == 0) {
                    setZoomRatio(iIntValue);
                    break;
                }
                break;
            case EVENT_ON_SELFTIMER_FINISHED:
                cancelSelfTimerCountDownView();
                break;
            case EVENT_ON_FOCUS_POSITION_SELECTED:
                hideAutoReview();
                Point point = (Point) objArr[0];
                FocusRectangles.FocusSetType focusSetType = (FocusRectangles.FocusSetType) objArr[1];
                this.mFocusRectangles.setFocusPosition(point, focusSetType);
                if (focusSetType == FocusRectangles.FocusSetType.FIRST) {
                    this.mFocusRectangles.setVisibility(4);
                    if (PlatformCapability.isFocusSupported(getCapturingMode().getCameraId())) {
                        this.mFocusRectangles.onAutoFocusStarted();
                        break;
                    }
                } else if (focusSetType == FocusRectangles.FocusSetType.RELEASE) {
                    this.mFocusRectangles.setVisibility(0);
                    break;
                } else {
                    this.mFocusRectangles.setVisibility(4);
                    break;
                }
                break;
            case EVENT_ON_FOCUS_POSITION_RELEASED:
                if (isTouchFocus()) {
                    disableSemiAutoControl();
                }
                if (this.mFocusRectangles != null) {
                    this.mFocusRectangles.clearAllFocus();
                    break;
                }
                break;
            case EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE:
                if (isTouchFocus()) {
                    disableSemiAutoControl();
                }
                if (this.mFocusRectangles != null) {
                    this.mFocusRectangles.clearAllFocusExceptFace();
                    break;
                }
                break;
            case EVENT_ON_FOCUS_POSITION_RELEASED_TOUCH_FOCUS:
                if (isTouchFocus()) {
                    disableSemiAutoControl();
                }
                if (this.mFocusRectangles != null && isTouchFocus()) {
                    this.mFocusRectangles.clearTouchFocus();
                    break;
                }
                break;
            case EVENT_ON_FOCUS_POSITION_RELEASED_BY_SELECT_FACE:
                if (this.mFocusRectangles != null) {
                    this.mFocusRectangles.clearAllFocusExceptFace();
                    break;
                }
                break;
            case EVENT_ON_RECORDING_PROGRESS:
                this.mRecordingTimeProxy.notifyOnTimeTicked(((Integer) objArr[0]).intValue());
                break;
            case EVENT_ON_ORIENTATION_CHANGED:
                setOrientation(((Integer) objArr[0]).intValue());
                break;
            case EVENT_UPDATE_DIALOGS:
                hideAutoReview();
                ViewFinder.UiComponentKind uiComponentKind = (ViewFinder.UiComponentKind) objArr[0];
                updateUiComponent(uiComponentKind);
                if (!isOverlayControlVisible() || !this.mStateMachine.isDialogOpened() || isSettingDialogOpened()) {
                    switch (uiComponentKind) {
                        case NOTICE_DIALOG:
                            if (getCapturingMode() == CapturingMode.FRONT_VIDEO || getCapturingMode() == CapturingMode.VIDEO || getCapturingMode() == CapturingMode.SLOW_MOTION) {
                                changeToVideoReadyView();
                            } else {
                                changeToPhotoReadyView(false);
                            }
                            requestToDimSystemUi();
                            break;
                        case FLASH_DIALOG:
                            requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                CapturingMode capturingMode = getCapturingMode();
                                UserSettingKey userSettingKey2 = UserSettingKey.FLASH;
                                if (capturingMode.getType() == 2) {
                                    userSettingKey = UserSettingKey.PHOTO_LIGHT;
                                } else if (capturingMode.isFront()) {
                                    userSettingKey = UserSettingKey.DISPLAY_FLASH;
                                } else {
                                    userSettingKey = UserSettingKey.FLASH;
                                }
                                this.mSettingUi.openShortcutSettingDialog(userSettingKey);
                                break;
                            }
                            break;
                        case SELF_TIMER_DIALOG:
                            requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.SELF_TIMER);
                                break;
                            }
                            break;
                        case ASPECT_RATIO_DIALOG:
                            requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.ASPECT_RATIO);
                                break;
                            }
                            break;
                        case FUSION_MODE_DIALOG:
                            requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.FUSION_MODE);
                                break;
                            }
                            break;
                        case VIDEO_HDR_DIALOG:
                            requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.VIDEO_HDR);
                                break;
                            }
                            break;
                        case HDR_DIALOG:
                            requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openShortcutSettingDialog(UserSettingKey.HDR);
                                break;
                            }
                            break;
                        case SETTING_DIALOG:
                            requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openSettingMenuDialog();
                                break;
                            }
                            break;
                        case MODE_SELECTOR:
                            requestToRecoverSystemUi();
                            if (this.mSettingUi != null) {
                                this.mSettingUi.openModeSelectDialog(this.mModeLoader, this.mModeSelectListener);
                                break;
                            }
                            break;
                        case OVERLAY_CONTROL_SEEKING:
                            requestToDimSystemUi();
                            break;
                    }
                }
                break;
            case EVENT_CLOSE_ALL_DIALOGS:
                closeDialogs();
                break;
            case EVENT_REQUEST_SHOW_INSTANT_VIEWER:
                SavingRequest savingRequest = (SavingRequest) objArr[1];
                StoreDataResult storeDataResult = (StoreDataResult) objArr[2];
                String str = savingRequest.common.mimeType;
                Uri uri = (storeDataResult == null || storeDataResult.savingRequest.getRequestId() != savingRequest.getRequestId()) ? null : storeDataResult.uri;
                if (uri == null) {
                    if (str == MediaSavingConstants.MEDIA_TYPE_MPEG4_MIME || str == MediaSavingConstants.MEDIA_TYPE_3GP_MIME) {
                        openInstantViewer(null, (String) objArr[0], savingRequest);
                        break;
                    } else {
                        openInstantViewer((byte[]) objArr[0], null, savingRequest);
                        break;
                    }
                } else {
                    if (str == MediaSavingConstants.MEDIA_TYPE_MPEG4_MIME || str == MediaSavingConstants.MEDIA_TYPE_3GP_MIME) {
                        openInstantViewer(null, (String) objArr[0], savingRequest);
                    } else {
                        openInstantViewer((byte[]) objArr[0], null, savingRequest);
                    }
                    InstantViewer.launchAlbum(this.mActivity, uri, str, true, this.mStateMachine.getPredictiveCaptureStoreInfo());
                    break;
                }
                break;
            case EVENT_ON_STORE_COMPLETED:
                onStoreCompleted((StoreDataResult) objArr[0], ((Boolean) objArr[1]).booleanValue());
                break;
            case EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION:
                startCaptureFeedbackAnimation();
                break;
            case EVENT_ON_LAZY_INITIALIZATION_TASK_RUN:
                onLazyInitializationTaskRun();
                break;
            case EVENT_ON_ADD_VIDEO_CHAPTER:
                addVideoChapter((ChapterThumbnail) objArr[0]);
                break;
            case EVENT_ON_NOTIFY_THERMAL_NORMAL:
                onNotifyThermalStatus(false);
                break;
            case EVENT_ON_NOTIFY_THERMAL_WARNING:
                onNotifyThermalStatus(true);
                if (!this.mIsThermalWarningDialogShown) {
                    showMessageDialog(DialogId.THERMAL_WARNING, new Object[0]);
                    this.mIsThermalWarningDialogShown = true;
                    break;
                }
                break;
            case EVENT_ON_NOTIFY_THERMAL_CRITICAL:
                if (((Boolean) objArr[0]).booleanValue()) {
                    showToastMessage(ToastContent.ToastID.NEEDS_TO_COOL_DOWN);
                    break;
                } else {
                    showMessageDialog(DialogId.THERMAL_CRITICAL, new Object[0]);
                    break;
                }
            case EVENT_ON_NOTIFY_RESTORE_NAVIGATION_BAR_PREVIOUS_VISIBILITY:
                requestToRestoreSystemUi();
                updateGeotagIcon();
                break;
            case EVENT_REQUEST_UPDATE_GRID_LINE:
                updateGridLineView();
                break;
            case EVENT_ANGLE_CHANGE_START:
                this.mIsFrontAngleChanging = true;
                if (this.mFocusRectangles != null) {
                    this.mFocusRectangles.clearFaceDetection();
                }
                getBaseLayout().getSceneIndicator().set(false);
                getBaseLayout().getConditionIndicator().set(false);
                if (this.mFrontAngleSwitchButton != null) {
                    this.mFrontAngleSwitchButton.switchFrontAngle((FrontAngle) this.mStateMachine.getUserSetting().get(UserSettingKey.FRONT_ANGLE));
                    break;
                }
                break;
            case EVENT_ANGLE_CHANGE_COMPLETED:
                this.mIsFrontAngleChanging = false;
                break;
            case EVENT_ON_BURST_REJECTED:
                onBurstRejected((ViewFinder.BurstRejectedReason) objArr[0]);
                break;
            case EVENT_ON_BURST_SHUTTER_DONE:
                onBurstShutterDone(((Boolean) objArr[0]).booleanValue(), ((Integer) objArr[1]).intValue());
                break;
            case EVENT_ON_BURST_FINISH:
                onBurstFinished();
                break;
            case EVENT_ON_CAPTURE_FINISH:
                onCaptureFinished();
                break;
            case EVENT_ON_CAPTURE_CANCEL:
                onCaptureCanceled();
                break;
            case EVENT_ON_NOTIFY_MAX_DURATION_REACHED:
                UserSettings userSetting = this.mStateMachine.getUserSetting();
                if (!userSetting.isLimitForSizeOrDuration() && VideoSize.MMS != userSetting.get(UserSettingKey.VIDEO_SIZE) && !this.mMessageDialog.isOpened()) {
                    showMessageDialog(DialogId.MAX_DURATION_REACHED, new Object[0]);
                    break;
                }
                break;
            case EVENT_ON_NOTIFY_MAX_FILESIZE_REACHED:
                if (!this.mMessageDialog.isOpened()) {
                    showMessageDialog(DialogId.MAX_FILESIZE_REACHED, new Object[0]);
                    break;
                }
                break;
            case EVENT_ON_NOTIFY_BATTERY_CRITICAL:
                if (((Boolean) objArr[0]).booleanValue()) {
                    showMessageDialog(DialogId.LOW_BATTERY_CRITICAL_ON_RECORDING, new Object[0]);
                    break;
                } else {
                    showMessageDialog(DialogId.LOW_BATTERY_CRITICAL_ON_PHOTO, new Object[0]);
                    break;
                }
            case EVENT_UPDATE_FUSION_MODE:
                if (getBaseLayout().getImageQualityControl().isInitialized() && getBaseLayout().getImageQualityControl().get().isVisible()) {
                    getBaseLayout().getImageQualityControl().get().refresh();
                }
                if (getCapturingMode() == CapturingMode.VIDEO) {
                    FusionMode fusionMode = (FusionMode) objArr[0];
                    if (this.mPrimaryShortcutGroup != null) {
                        this.mPrimaryShortcutGroup.updatePrimaryShortcutIcon(UserSettingKey.FUSION_MODE, fusionMode.getIconId());
                        break;
                    }
                }
                break;
            case EVENT_ON_ISO_CHANGED_BY_FUSION:
                if (this.mSettingDialogStack != null && !this.mSettingDialogStack.isDialogOpened()) {
                    postHintText(new HintTextTimedOutMessage(HintTextTimedOutMessage.MessageType.ISO_CHANGED_BY_FUSION));
                    break;
                }
                break;
            case EVENT_SHOW_BLACK_SCREEN:
                getBaseLayout().showBlackScreen();
                break;
            case EVENT_HIDE_BLACK_SCREEN:
                getBaseLayout().hideBlackScreen();
                break;
            case EVENT_REQUEST_UPDATE_FUSION_CONDITION:
                updateFusionHintText((CameraParameters.FusionResult) objArr[0]);
                break;
            case EVENT_REQUEST_UPDATE_VIDEO_HDR_CONDITION:
                updateVideoHdrCondition(getCapturingMode(), (VideoHdr) objArr[0], ((Boolean) objArr[1]).booleanValue());
                break;
            case EVENT_ON_CAPTURING_MODE_CHANGING:
                onCapturingModeChanging();
                break;
            case EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY:
                this.mIsSettingChangeAcceptable = ((Boolean) objArr[0]).booleanValue();
                break;
            case EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE:
                this.mIsNeedDisplayToastChangeInternalStoarge = ((Boolean) objArr[0]).booleanValue();
                if (!this.mIsNeedDisplayToastChangeInternalStoarge) {
                    showMessageDialog(DialogId.DESTINATION_TO_SAVE_CHANGED_INTERNAL, new Object[0]);
                    break;
                }
                break;
            case EVENT_REQUEST_UPDATE_MRU_SHORTCUT:
                getBaseLayout().getMruButtonContainer().setMode((Mode) objArr[0]);
                break;
            case EVENT_APPS_UI_MODE_FINISH:
                onAppsUiModeFinish();
                break;
            case EVENT_REQUEST_SHOW_UNLOCK_SCREEN_DIALOG:
                CapturingMode capturingMode2 = (CapturingMode) objArr[0];
                Object obj = (Mode) objArr[1];
                ModeSelectorInternalMode tag = ((InternalMode) obj).getTag();
                if (this.mActivity.isDeviceInSecurityLock() && tag == ModeSelectorInternalMode.DUAL_MONOCHROME) {
                    CameraActivity cameraActivity = this.mActivity;
                    ActivityOptions activityOptionsMakeCustomAnimation = ActivityOptions.makeCustomAnimation(cameraActivity, 0, 0);
                    Intent intentCommit = LaunchCameraIntentBuilder.create().mode(getCapturingMode().name()).activity("com.sonyericsson.android.camera", CapturingModeUtil.CAMERA_ACTIVITY).callingMode(CapturingModeUtil.filteringPrevName(getCapturingMode().name())).callingActivity(cameraActivity.getPackageName(), CapturingModeUtil.filteringPrevActivity(cameraActivity.getClass().getName())).commit();
                    intentCommit.putExtra(LaunchCondition.EXTRA_LAUNCH_INTERNAL_MODE, ModeSelectorInternalMode.DUAL_MONOCHROME.ordinal());
                    intentCommit.putExtra(LaunchCondition.EXTRA_LAUNCH_INTERNAL_CALLING_CAPTURING_MODE, capturingMode2.ordinal());
                    showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP, intentCommit, activityOptionsMakeCustomAnimation.toBundle(), obj);
                    break;
                }
                break;
        }
    }

    private void onBurstShutterDone(boolean z, int i) {
        if (isHeadUpDisplayReady()) {
            if (this.mBurstCountView == null) {
                this.mBurstCountView = getBaseLayout().getBurstCountView();
                this.mBurstCountView.setUiOrientation(this.mOrientation);
            }
            this.mBurstCountView.update(i);
            clearTouchedScreenButtonGroup();
            this.mFocusRectangles.onAutoFocusDone(z);
            this.mFocusRectangles.clearTouched();
            this.mViewFinderCaptureArea.clearTouched();
        }
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

    private void onBurstRejected(ViewFinder.BurstRejectedReason burstRejectedReason) throws Resources.NotFoundException {
        if (isPreviewLayout(getCurrentLayoutPattern())) {
            showBurstRejectedMessage(burstRejectedReason);
        } else {
            this.mBurstShootingRejectedReason = burstRejectedReason;
        }
    }

    private void onCaptureFinished() throws Resources.NotFoundException {
        if (isHeadUpDisplayReady()) {
            if (!attachSideAutoReview() && isAutoReviewEnabled()) {
                this.mIsAutoReviewRequested = true;
                this.mAutoReviewProxy.bindReceiver(getBaseLayout().getAutoReview());
            }
            if (this.mBurstShootingRejectedReason != ViewFinder.BurstRejectedReason.NONE) {
                showBurstRejectedMessage(this.mBurstShootingRejectedReason);
                this.mBurstShootingRejectedReason = ViewFinder.BurstRejectedReason.NONE;
            }
            updateAllOverlayControlVisibility();
            updateVisibilityForSpecificDisplaySize();
        }
    }

    private void onCaptureCanceled() throws Resources.NotFoundException {
        if (isHeadUpDisplayReady()) {
            if (this.mBurstShootingRejectedReason != ViewFinder.BurstRejectedReason.NONE) {
                showBurstRejectedMessage(this.mBurstShootingRejectedReason);
                this.mBurstShootingRejectedReason = ViewFinder.BurstRejectedReason.NONE;
            }
            updateAllOverlayControlVisibility();
            updateVisibilityForSpecificDisplaySize();
        }
    }

    private void onBurstFinished() throws Resources.NotFoundException {
        if (!isHeadUpDisplayReady() || this.mBurstCountView == null) {
            return;
        }
        this.mBurstCountView.hide();
        startCaptureFeedbackAnimation();
        if (((DestinationToSave) this.mStateMachine.getUserSetting().get(UserSettingKey.DESTINATION_TO_SAVE)).getType() == Storage.StorageType.INTERNAL || this.mHintBurstImageSavedToInternalStorageAlreadyDisplayed) {
            return;
        }
        this.mHintBurstImageSavedToInternalStorageAlreadyDisplayed = true;
        postHintText(new HintTextTimedOutMessage(HintTextTimedOutMessage.MessageType.BURST_IMAGES_ARE_SAVED_TO_INTERNAL_STORAGE));
    }

    private void onCapturingModeChanging() {
        disableSemiAutoControl();
        disableOverlayControl(getBaseLayout().getImageQualityControl());
    }

    private boolean attachSideAutoReview() {
        if (!this.mSideTouchUi.containsIn(SideTouchUi.Type.CAPTURE_COUNTDOWN, SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR, SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE, SideTouchUi.Type.SELF_TIMER_COUNTDOWN_CANCEL)) {
            return false;
        }
        this.mIsAutoReviewRequested = true;
        this.mSideTouchUi.attachIcon(SideTouchUi.Type.AUTO_REVIEW, null);
        return true;
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

    private void addVideoChapter(ChapterThumbnail chapterThumbnail) {
        RecordingIndicator recordingIndicator = getBaseLayout().getRecordingIndicator();
        YuvImage yuvImage = new YuvImage(chapterThumbnail.yuvData, chapterThumbnail.format.intValue(), chapterThumbnail.rect.width(), chapterThumbnail.rect.height(), null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (yuvImage.compressToJpeg(chapterThumbnail.rect, 80, byteArrayOutputStream)) {
            recordingIndicator.addChapter(byteArrayOutputStream.toByteArray(), chapterThumbnail.orientation());
        }
    }

    private void onLazyInitializationTaskRun() {
        ModeLoader.updatePluginsDatabase(this.mActivity);
        this.mInstantViewer.createAlbumPreloader();
        setupMruButton(getCapturingMode());
    }

    private void setMruAvailability(boolean z) {
        if (this.mActivity.isOneShot()) {
            z = false;
        }
        getBaseLayout().getMruButtonContainer().setAvailability(z);
    }

    private void attemptSetupMruButton(CapturingMode capturingMode) {
        if (this.mModeLoader == null) {
            setupMruButton(capturingMode);
        }
    }

    private void setupMruButton(CapturingMode capturingMode) {
        if (this.mActivity.isOneShot()) {
        }
        switch (capturingMode) {
            case SCENE_RECOGNITION:
            case SUPERIOR_FRONT:
            case VIDEO:
            case FRONT_VIDEO:
                if (this.mModeLoader == null) {
                    this.mModeLoader = new ModeLoader(this.mActivity);
                }
                getBaseLayout().getMruButtonContainer().setup(this.mModeLoader);
                break;
        }
    }

    private void onSceneModeChanged(CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
        if (isHeadUpDisplayReady() && !this.mActivity.isOneShot()) {
            doChangeSceneMode(sceneRecognitionResult);
            doChangeCondition(sceneRecognitionResult);
        }
    }

    private void doChangeSceneMode(CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
        RecognizedScene recognizedSceneCreate = RecognizedScene.create(sceneRecognitionResult.sceneMode);
        int iconId = recognizedSceneCreate.getIconId();
        int textId = recognizedSceneCreate.getTextId();
        if (iconId <= 0 || textId <= 0) {
            if (!sceneRecognitionResult.isMacroRange) {
                getBaseLayout().getSceneIndicator().set(false);
                return;
            } else {
                iconId = R.drawable.cam_scene_recog_macro_icn;
                textId = R.string.cam_strings_focus_mode_macro_txt;
            }
        }
        getBaseLayout().getSceneIndicator().set(true);
        getBaseLayout().getSceneIndicator().setImageResource(iconId);
        getBaseLayout().getSceneIndicator().setTextResource(textId);
    }

    private void doChangeCondition(CameraParameters.SceneRecognitionResult sceneRecognitionResult) {
        int iconId = RecognizedCondition.create(sceneRecognitionResult.deviceStabilityCondition).getIconId();
        if (iconId != -1) {
            getBaseLayout().getConditionIndicator().set(true);
            getBaseLayout().getConditionIndicator().setImageResource(iconId);
        } else {
            getBaseLayout().getConditionIndicator().set(false);
        }
    }

    private void onFaceDetected(CameraParameters.FaceDetectionResult faceDetectionResult) {
        if (isHeadUpDisplayReady()) {
            this.mFocusRectangles.onFaceDetected(faceDetectionResult);
        }
    }

    private void onTrackedObjectStateUpdated(CameraParameters.ObjectTrackingResult objectTrackingResult) {
        if (isHeadUpDisplayReady()) {
            this.mFocusRectangles.onObjectTracked(objectTrackingResult);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void onObjectLost() {
        if (isHeadUpDisplayReady()) {
            this.mFocusRectangles.onObjectLost();
        }
    }

    private class ViewFinderStateListener implements CaptureArea.CaptureAreaStateListener {
        @Override // com.sonyericsson.android.camera.view.CaptureArea.CaptureAreaStateListener
        public void onCaptureAreaMoved() {
        }

        @Override // com.sonyericsson.android.camera.view.CaptureArea.CaptureAreaStateListener
        public void onCaptureAreaStopped() {
        }

        private ViewFinderStateListener() {
        }

        @Override // com.sonyericsson.android.camera.view.CaptureArea.CaptureAreaStateListener
        public void onCaptureAreaTouched() {
            ViewFinderImpl.this.mTouchEventDispatcher.sendTouchDown(UserEventHandler.UiComponent.CAPTURE_AREA);
        }

        @Override // com.sonyericsson.android.camera.view.CaptureArea.CaptureAreaStateListener
        public void onCaptureAreaSingleTapUp(Point point) {
            ViewFinderImpl.this.mTouchEventDispatcher.sendClick(UserEventHandler.UiComponent.CAPTURE_AREA, point);
        }

        @Override // com.sonyericsson.android.camera.view.CaptureArea.CaptureAreaStateListener
        public void onCaptureAreaLongPressed(Point point) {
            ViewFinderImpl.this.mTouchEventDispatcher.sendLongClick(UserEventHandler.UiComponent.CAPTURE_AREA, point);
        }

        @Override // com.sonyericsson.android.camera.view.CaptureArea.CaptureAreaStateListener
        public void onCaptureAreaReleased(Point point) {
            ViewFinderImpl.this.mTouchEventDispatcher.sendTouchUp(UserEventHandler.UiComponent.CAPTURE_AREA, point);
        }

        @Override // com.sonyericsson.android.camera.view.CaptureArea.CaptureAreaStateListener
        public void onCaptureAreaCanceled() {
            ViewFinderImpl.this.mTouchEventDispatcher.sendCancel(UserEventHandler.UiComponent.CAPTURE_AREA);
        }

        @Override // com.sonyericsson.android.camera.view.CaptureArea.CaptureAreaStateListener
        public void onCaptureAreaIsReadyToScale() throws Resources.NotFoundException {
            if (ViewFinderImpl.this.isFront()) {
                if (ViewFinderImpl.this.getCurrentLayoutPattern() != BaseLayoutPattern.SELFTIMER) {
                    ViewFinderImpl.this.notifyZoomOperationRejected();
                    return;
                }
                return;
            }
            ViewFinderImpl.this.mTouchEventDispatcher.sendCaptureAreaScaleReady(UserEventHandler.UiComponent.CAPTURE_AREA);
        }

        @Override // com.sonyericsson.android.camera.view.CaptureArea.CaptureAreaStateListener
        public void onCaptureAreaScaled(float f) {
            if (ViewFinderImpl.this.isFront()) {
                return;
            }
            ViewFinderImpl.this.mTouchEventDispatcher.sendCaptureAreaScaling(UserEventHandler.UiComponent.CAPTURE_AREA, f);
        }
    }

    private class OnHighSensitivityFusionButtonStateListener implements OnScreenButtonListener {
        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onCancel(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onDown(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onLongPress(OnScreenButton onScreenButton) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onMove(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        private OnHighSensitivityFusionButtonStateListener() {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onUp(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            if (ViewFinderImpl.this.getCapturingMode() == CapturingMode.NORMAL && ViewFinderImpl.this.mStateMachine.isSettingChangeAcceptable() && ViewFinderImpl.this.isUserOperable()) {
                ViewFinderImpl.this.mSettingDialogStack.closeCurrentDialog();
                if (ViewFinderImpl.this.openTutorial(TutorialController.DisplayTrigger.CHANGE_MANUAL_FUSION_SETTING)) {
                    ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_OPENED, new Object[0]);
                    return;
                } else {
                    ViewFinderImpl.this.updateHighSensitivityFusionModeForManual();
                    return;
                }
            }
            if (CamLog.VERBOSE) {
                CamLog.d("HighSensitivityFusion button was tapped in mode change");
            }
        }
    }

    private void hideSurfaceBlinderView() {
        if (this.mSurfaceBlinderView == null || this.mSurfaceBlinderView.getVisibility() != 0) {
            return;
        }
        this.mSurfaceBlinderView.setVisibility(4);
        FrameLayout previewOverlayContainer = getBaseLayout().getPreviewOverlayContainer();
        if (previewOverlayContainer != null) {
            previewOverlayContainer.removeView(this.mSurfaceBlinderView);
        }
    }

    private void releaseSurfaceBlinderView() {
        if (this.mSurfaceBlinderView != null) {
            this.mSurfaceBlinderView.setVisibility(4);
            FrameLayout previewOverlayContainer = getBaseLayout().getPreviewOverlayContainer();
            if (previewOverlayContainer != null) {
                previewOverlayContainer.removeView(this.mSurfaceBlinderView);
            }
            this.mSurfaceBlinderView = null;
        }
    }

    private PointF convertTouchPointToDevicePreviewPositionRatio(Point point) {
        Rect rect = this.mEvf.getRect();
        return new PointF((point.x - rect.left) / rect.width(), (point.y - rect.top) / rect.height());
    }

    public RectF convertTouchPointToRectInDevicePreviewPositionRatio(Point point) {
        PointF pointFConvertTouchPointToDevicePreviewPositionRatio = convertTouchPointToDevicePreviewPositionRatio(point);
        Rect rect = this.mEvf.getRect();
        Rect touchFocusIconSize = this.mFocusRectangles.getTouchFocusIconSize();
        int iWidth = rect.width();
        float fWidth = (touchFocusIconSize.width() / iWidth) / 2.0f;
        float fHeight = (touchFocusIconSize.height() / rect.height()) / 2.0f;
        return new RectF(pointFConvertTouchPointToDevicePreviewPositionRatio.x - fWidth, pointFConvertTouchPointToDevicePreviewPositionRatio.y - fHeight, pointFConvertTouchPointToDevicePreviewPositionRatio.x + fWidth, pointFConvertTouchPointToDevicePreviewPositionRatio.y + fHeight);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public int getOrientation() {
        return getBaseLayout().getCurrentOrientation();
    }

    private OnScreenButtonItemFactory.ButtonType getCaptureButtonTypeAccordingToSelfTimerSetting() {
        int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[this.mPhotoSelfTimerSetting.ordinal()];
        if (i == 1) {
            if (isTouchCaptureEnabled()) {
                return OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER_LONG;
            }
            return OnScreenButtonItemFactory.ButtonType.CAPTURE_WITH_SELFTIMER_LONG;
        }
        if (i == 3) {
            if (isTouchCaptureEnabled()) {
                return OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER_SHORT;
            }
            return OnScreenButtonItemFactory.ButtonType.CAPTURE_WITH_SELFTIMER_SHORT;
        }
        if (isTouchCaptureEnabled()) {
            return OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE;
        }
        return OnScreenButtonItemFactory.ButtonType.CAPTURE_LARGE;
    }

    private void changeOnScreenCaptureButtonInManualMain() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInManualMain()");
        }
        if (PlatformCapability.isHighSensitivityFusionSupported(getCapturingMode().getCameraId())) {
            getBaseLayout().getOnScreenButtonGroup().setOption1(this.mHighSensitivityFusionButtonItem, getOrientation(), true);
        } else {
            this.mScreenButtonHandler.clearOption1();
        }
        this.mScreenButtonHandler.setMain(getCaptureButtonTypeAccordingToSelfTimerSetting(), getOrientation(), true);
        this.mScreenButtonHandler.setOption2((OnScreenButtonGroup.Item) this.mImageQualityControlButtonItem, getOrientation(), true);
    }

    private void changeOnScreenCaptureButtonInManualFront() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInManualFront()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(getCaptureButtonTypeAccordingToSelfTimerSetting(), getOrientation(), true);
        this.mScreenButtonHandler.setOption2((OnScreenButtonGroup.Item) this.mImageQualityControlButtonItem, getOrientation(), true);
    }

    private void changeOnScreenCaptureButtonInAutoMain() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInAutoMain()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(getCaptureButtonTypeAccordingToSelfTimerSetting(), getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }

    private void changeOnScreenCaptureButtonInAutoFront() {
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(getCaptureButtonTypeAccordingToSelfTimerSetting(), getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }

    private void changeOnScreenCaptureButtonInSelfTimerCoundDown() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInSelfTimerCoundDown()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.CAPTURE_LARGE, getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }

    private OnScreenButtonItemFactory.ButtonType createStartRecordingButton() {
        if (isTouchCaptureEnabled()) {
            return OnScreenButtonItemFactory.ButtonType.TOUCH_RECORDING_START;
        }
        if (getCapturingMode() == CapturingMode.SLOW_MOTION) {
            if (AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion) this.mStateMachine.getUserSetting().get(getCapturingMode(), UserSettingKey.SLOW_MOTION)).ordinal()] == 2) {
                return OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION;
            }
            return OnScreenButtonItemFactory.ButtonType.START_RECORDING_LARGE;
        }
        return OnScreenButtonItemFactory.ButtonType.START_RECORDING_LARGE;
    }

    private void changeOnScreenCaptureButtonInVideo() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInVideo()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(createStartRecordingButton(), getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
        LocalResearchUtil.getInstance().setMeasurementValid(LocalResearchUtil.MeasurementKey.VIDEO_RECORDING_STOP_READY_FOR_USE);
    }

    private void changeOnScreenSuperSlowMotionButtonInVideo() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenSuperSlowMotionButtonInVideo()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(createStartRecordingButton(), getOrientation(), true);
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

    private void changeOnScreenStandardSlowMotionButtonInVideo() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenStandardSlowMotionButtonInVideo()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(createStartRecordingButton(), getOrientation(), true);
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

    private void changeOnScreenSuperSlowShotButtonInVideo() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenSuperSlowShotButtonInVideo()");
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(createStartRecordingButton(), getOrientation(), true);
        this.mScreenButtonHandler.clearOption2();
    }

    private void changeOnScreenCaptureButtonInVideoRecording() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInSelfTimerCountdown()");
        }
        VideoSize videoSize = (VideoSize) this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SIZE);
        if (!(videoSize != null && videoSize.isConstraint())) {
            this.mScreenButtonHandler.setOption1(OnScreenButtonItemFactory.ButtonType.PAUSE_RECORDING_SMALL, this.mRecordingOrientation, false);
            this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_LARGE, this.mRecordingOrientation, false);
            if (!this.mActivity.isOneShotVideo()) {
                if (((VideoHdr) this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_HDR)) != VideoHdr.HDR_ON) {
                    this.mScreenButtonHandler.setOption2(OnScreenButtonItemFactory.ButtonType.CAPTURE_SMALL, getOrientation(), true);
                    return;
                } else {
                    getBaseLayout().getOnScreenButtonGroup().clearOption2();
                    return;
                }
            }
            this.mScreenButtonHandler.clearOption2();
            return;
        }
        this.mScreenButtonHandler.clearOption1();
        this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_LARGE, this.mRecordingOrientation, false);
        this.mScreenButtonHandler.clearOption2();
    }

    private void changeOnScreenCaptureButtonInVideoPausing() {
        if (CamLog.VERBOSE) {
            CamLog.d("changeOnScreenCaptureButtonInVideo()");
        }
        this.mScreenButtonHandler.setOption1(OnScreenButtonItemFactory.ButtonType.RESUME_RECORDING_SMALL, this.mRecordingOrientation, false);
        this.mScreenButtonHandler.setMain(OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_IN_PAUSE_LARGE, this.mRecordingOrientation, false);
        VideoHdr videoHdr = (VideoHdr) this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_HDR);
        if (!this.mActivity.isOneShotVideo() && videoHdr != VideoHdr.HDR_ON) {
            this.mScreenButtonHandler.setOption2(OnScreenButtonItemFactory.ButtonType.CAPTURE_SMALL, getOrientation(), true);
        } else {
            this.mScreenButtonHandler.clearOption2();
        }
    }

    private void changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("state : " + headUpDisplaySetupState);
        }
        if (getBaseLayout() == null || getBaseLayout().getOnScreenButtonGroup() == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("Screen button group is not created.");
                return;
            }
            return;
        }
        switch (headUpDisplaySetupState) {
            case PHOTO_READY:
            case PHOTO_CAPTURE:
            case PHOTO_BURST_CAPTURE:
                if (z) {
                    changeOnScreenCaptureButtonInSelfTimerCoundDown();
                    break;
                } else {
                    switch (getCapturingMode()) {
                        case SCENE_RECOGNITION:
                            changeOnScreenCaptureButtonInAutoMain();
                            break;
                        case SUPERIOR_FRONT:
                            changeOnScreenCaptureButtonInAutoFront();
                            break;
                        case NORMAL:
                            changeOnScreenCaptureButtonInManualMain();
                            break;
                        case FRONT_PHOTO:
                            changeOnScreenCaptureButtonInManualFront();
                            break;
                    }
                }
            case VIDEO_READY:
                changeOnScreenCaptureButtonInVideo();
                break;
            case VIDEO_RECORDING:
                changeOnScreenCaptureButtonInVideoRecording();
                break;
            case SUPER_SLOW_MOTION_STANDBY:
                changeOnScreenSuperSlowMotionButtonInVideo();
                break;
            case SUPER_SLOW_MOTION_RECORDING:
                changeOnScreenSuperSlowMotionRecordingButtonInVideo();
                break;
            case STANDARD_SLOW_MOTION_STANDBY:
                changeOnScreenStandardSlowMotionButtonInVideo();
                break;
            case STANDARD_SLOW_MOTION_RECORDING:
                changeOnScreenStandardSlowMotionButtonInVideoRecording();
                break;
            case SUPER_SLOW_SHOT_STANDBY:
                changeOnScreenSuperSlowShotButtonInVideo();
                break;
            case VIDEO_PAUSING:
                changeOnScreenCaptureButtonInVideoPausing();
                break;
            default:
                throw new IllegalStateException("ViewFinder.changeScreenButtonBackground():[Unexpected system bar status.] state = " + headUpDisplaySetupState);
        }
        if (isHeadUpDisplayReady()) {
            return;
        }
        getBaseLayout().getOnScreenButtonGroup().clearOption1();
        getBaseLayout().getOnScreenButtonGroup().clearOption2();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public SelfTimer getPhotoSelfTimerSetting() {
        return this.mPhotoSelfTimerSetting;
    }

    private void setupInstantViewer() {
        if (this.mInstantViewer == null || this.mInstantViewer.getParent() == null) {
            if (isInflated()) {
                this.mInstantViewer = (InstantViewer) getPreInflatedView(LayoutAsyncInflateItems.CameraInflateItem.AUTO_REVIEW).get(0);
                this.mInstantViewer.setup(this.mActivity.getStoredSettings().getUserSettings());
            }
            if (this.mInstantViewer == null) {
                this.mInstantViewer = (InstantViewer) LayoutInflater.from(getActivity()).inflate(R.layout.instant_viewer, (ViewGroup) null);
                this.mInstantViewer.setup(this.mActivity.getStoredSettings().getUserSettings());
            }
            getActivity().getWindow().addContentView(this.mInstantViewer, new WindowManager.LayoutParams(-1, -1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFront() {
        return getCapturingMode().isFront();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CapturingMode getCapturingMode() {
        return this.mStateMachine.getCurrentCapturingMode();
    }

    private boolean isNecessaryToReverseForAutoReview(StoreDataResult storeDataResult) {
        if ((storeDataResult.savingRequest instanceof PhotoSavingRequest) && ((PhotoSavingRequest) storeDataResult.savingRequest).photo.isFront()) {
            return storeDataResult.savingRequest.common.orientation == 90 || storeDataResult.savingRequest.common.orientation == 270;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupAutoReview() {
        getBaseLayout().setupAutoReview();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void hideAutoReview() {
        this.mIsAutoReviewRequested = false;
        if (getBaseLayout() != null) {
            getBaseLayout().hideAutoReview();
        }
        if (this.mSideTouchUi != null) {
            this.mSideTouchUi.destroyTo(SideTouchUi.Type.AUTO_REVIEW);
        } else {
            CamLog.e("Hiding the problem (mSideTouchUi = null). Modify the problem correctly.");
        }
        updateAllOverlayControlVisibility();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clickAutoReview(StoreDataResult storeDataResult) {
        SavingRequest savingRequest = storeDataResult.savingRequest;
        Content currentContent = getCurrentContent();
        clickThumbnail(storeDataResult.uri, savingRequest.common.mimeType, savingRequest.common.width, savingRequest.common.height, savingRequest.common.orientation, currentContent != null && currentContent.isMediaDataVerified());
    }

    private void openInstantViewer(byte[] bArr, String str, SavingRequest savingRequest) {
        if (CamLog.VERBOSE) {
            CamLog.d("openInstantViewer: " + savingRequest);
        }
        if (this.mInstantViewer != null) {
            if (!this.mInstantViewer.open(bArr, str, savingRequest.common.mimeType, 0, savingRequest.common.orientation, isFront(), new ReviewWindowListenerImpl(), savingRequest.getRequestId())) {
                closeInstantViewer();
            } else {
                hideApplicationNavigator();
            }
        }
    }

    private void closeInstantViewer() {
        if (this.mInstantViewer != null && this.mInstantViewer.isOpened()) {
            this.mInstantViewer.hide();
        }
        hideAutoReview();
    }

    private class ReviewWindowListenerImpl implements ReviewWindowListener {
        private ReviewWindowListenerImpl() {
        }

        @Override // com.sonyericsson.cameracommon.review.ReviewWindowListener
        public void onReviewWindowOpen() {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_OPENED, ViewFinder.UiComponentKind.REVIEW_WINDOW);
        }

        @Override // com.sonyericsson.cameracommon.review.ReviewWindowListener
        public void onReviewWindowClose() {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public int getRequestId(boolean z) {
        int iCreateEmptyContentFrame;
        if (getBaseLayout().getContentsViewController() != null) {
            preparationForInstantViewer();
            if (z) {
                iCreateEmptyContentFrame = getBaseLayout().getContentsViewController().createContentFrame();
            } else {
                iCreateEmptyContentFrame = getBaseLayout().getContentsViewController().createEmptyContentFrame();
            }
        } else {
            iCreateEmptyContentFrame = -1;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("New request ID: " + iCreateEmptyContentFrame);
        }
        return iCreateEmptyContentFrame;
    }

    private void preparationForInstantViewer() {
        if (CamLog.VERBOSE) {
            CamLog.d("preparationForInstantViewer");
        }
        if (!this.mStateMachine.isRecording() && getBaseLayout().getContentsViewController() != null) {
            getBaseLayout().getContentsViewController().setClickThumbnailProgressListener(new OnClickThumbnailProgressListenerImpl());
        }
        if (this.mInstantViewer == null || this.mInstantViewer.isOpened()) {
            return;
        }
        this.mInstantViewer.clear();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void startHideThumbnail() throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("startHideThumbnail: ");
        }
        if (getBaseLayout().getContentsViewController() == null) {
            return;
        }
        getBaseLayout().getContentsViewController().stopAnimation(false);
        Animation animationLoadAnimation = AnimationUtils.loadAnimation(this.mActivity, R.anim.thumbnail_fade_out);
        animationLoadAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.20
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (ViewFinderImpl.this.mCameraDevice.isRecording()) {
                    ViewFinderImpl.this.getBaseLayout().getContentsViewController().hide();
                }
            }
        });
        getBaseLayout().getContentsViewController().startHideAnimation(animationLoadAnimation);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder, com.sonyericsson.cameracommon.viewfinder.ViewFinderInterface
    public void onCaptureDone() {
        if (isHeadUpDisplayReady()) {
            clearTouchedScreenButtonGroup();
            this.mFocusRectangles.onObjectFocused();
        }
    }

    private void startCaptureFeedbackAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("startCaptureFeedbackAnimation()");
        }
        if (this.mCaptureFeedback != null) {
            this.mCaptureFeedback.start(CaptureFeedbackAnimationFactory.createDefaultAnimation());
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isTouchFocus() {
        if (this.mFocusRectangles == null) {
            return false;
        }
        return this.mFocusRectangles.isTouchFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isZooming() {
        if (this.mLayoutPattern == null) {
            return false;
        }
        int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[((BaseLayoutPattern) this.mLayoutPattern).ordinal()];
        if (i == 2) {
            return true;
        }
        switch (i) {
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFocusing() {
        if (this.mLayoutPattern == null) {
            return false;
        }
        switch ((BaseLayoutPattern) this.mLayoutPattern) {
        }
        return false;
    }

    private void closeSettingDialog() {
        if (this.mSettingDialogStack.isDialogOpened()) {
            this.mSettingDialogStack.closeCurrentDialog();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAllDialogClosed() {
        if (this.mSettingDialogStack != null) {
            return (this.mSettingDialogStack.isDialogOpened() || this.mMessageDialog.isCurrentDialogInList(STORAGE_DIALOG_LIST)) ? false : true;
        }
        return true;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isFlashAndSettingMenuOpened() {
        if (this.mSettingDialogStack != null) {
            return this.mSettingDialogStack.isShortcutDialogOpened() || this.mSettingDialogStack.isMenuDialogOpened();
        }
        return false;
    }

    private void updateUiComponent(ViewFinder.UiComponentKind uiComponentKind) {
        changeToDialogView(uiComponentKind);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void requestInflate(LayoutInflater layoutInflater) {
        if (CamLog.VERBOSE) {
            CamLog.d("startInflateTask in");
        }
        startInflateTask(layoutInflater, FastLayoutAsyncInflateItems.getInflateItemsForFast());
        if (CamLog.VERBOSE) {
            CamLog.d("startInflateTask out");
        }
    }

    private class FocusActionListenerImpl implements FocusActionListener {
        @Override // com.sonyericsson.cameracommon.focusview.FocusActionListener
        public void onTouched() {
        }

        private FocusActionListenerImpl() {
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusActionListener
        public void onCanceled() {
            ViewFinderImpl.this.mBurstShootingRejectedReason = ViewFinder.BurstRejectedReason.NONE;
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusActionListener
        public void onReleased() {
            if (!ViewFinderImpl.this.isAutoReviewShowing()) {
                if (!ViewFinderImpl.this.getCapturingMode().isVideo()) {
                    if (!ViewFinderImpl.this.isPhotoSelfTimerEnabled() || !ViewFinderImpl.this.isPreviewLayout()) {
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE, new Object[0]);
                    } else {
                        ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, Event.SelfTimerTrigger.NORMAL);
                    }
                } else if (!ViewFinderImpl.this.mStateMachine.isRecording()) {
                    ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_RECORDING, new Object[0]);
                }
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.TOUCH_CAPTURE);
                return;
            }
            ViewFinderImpl.this.hideAutoReview();
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusActionListener
        public void onLongPressed() throws Resources.NotFoundException {
            boolean z = !ViewFinderImpl.this.mActivity.isOneShot();
            if (ViewFinderImpl.this.isTouchCaptureEnabled() && ViewFinderImpl.this.isInternalStorageWritable() && z) {
                if (!PlatformCapability.isManualBurstSupported(ViewFinderImpl.this.getCapturingMode().getCameraId())) {
                    if (PlatformCapability.isManualBurstSupported(CameraInfo.CameraId.BACK)) {
                        ViewFinderImpl.this.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_REJECTED, ViewFinder.BurstRejectedReason.CANNOT_BURST_USING_FRONT_CAMERA);
                    }
                } else {
                    if (ViewFinderImpl.this.mStateMachine.getUserSetting().get(UserSettingKey.FUSION_MODE) == FusionMode.ON) {
                        ViewFinderImpl.this.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_REJECTED, ViewFinder.BurstRejectedReason.CANNOT_BURST_DUE_TO_FUSION_MODE);
                        return;
                    }
                    ViewFinderImpl.this.hideAutoReview();
                    ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_BURST, new Object[0]);
                    ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.TOUCH_CAPTURE);
                }
            }
        }

        @Override // com.sonyericsson.cameracommon.focusview.FocusActionListener
        public void onFaceSelected(Point point) {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CHANGE_SELECTED_FACE, point);
        }
    }

    private class OnClickThumbnailProgressListenerImpl implements ContentsViewController.OnClickThumbnailProgressListener {
        private OnClickThumbnailProgressListenerImpl() {
        }

        @Override // com.sonyericsson.cameracommon.contentsview.ContentsViewController.OnClickThumbnailProgressListener
        public void onClickThumbnailProgress() {
            if (CamLog.VERBOSE) {
                CamLog.d("onClickThumbnailProgress");
            }
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_TOUCH_CONTENT_PROGRESS, new Object[0]);
        }
    }

    private void clearSurfaceView() {
        if (this.mEvf != null) {
            this.mEvf.clear();
        }
    }

    private ContentPallet.ThumbnailStateListener getThumbnailStateListener() {
        return new ContentPallet.ThumbnailStateListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.21
            @Override // com.sonyericsson.cameracommon.contentsview.ContentPallet.ThumbnailStateListener
            public void onThumbnailClicked(Content content) {
                if (ViewFinderImpl.this.mIsFrontAngleChanging || content == null) {
                    return;
                }
                Content.ContentInfo contentInfo = content.getContentInfo();
                ViewFinderImpl.this.clickThumbnail(contentInfo.mOriginalUri, contentInfo.mMimeType, contentInfo.mWidth, contentInfo.mHeight, contentInfo.mOrientation, content.isMediaDataVerified());
            }

            @Override // com.sonyericsson.cameracommon.contentsview.ContentPallet.ThumbnailStateListener
            public void onThumbnailCreated(Content content) {
                if (ViewFinderImpl.this.mCameraDevice.getRemainSavingPhotoRequestCount() == 0 || content.getContentInfo().mContentType == Content.ContentsType.PREDICTIVE_CAPTURE) {
                    if (ViewFinderImpl.this.mInstantViewer != null) {
                        ViewFinderImpl.this.mInstantViewer.prepareBitmap(content.getContentInfo().mOriginalUri);
                    }
                    if (content.getContentInfo().mContentType != Content.ContentsType.PREDICTIVE_CAPTURE || ViewFinderImpl.this.mStateMachine.getPredictiveCaptureStoreInfo() == null) {
                        return;
                    }
                    content.getContentInfo().mPredictiveNum = ViewFinderImpl.this.mStateMachine.getPredictiveCaptureStoreInfo().getCaptureNum();
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clickThumbnail(Uri uri, String str, int i, int i2, int i3, boolean z) {
        int currentRequestId = getCurrentRequestId();
        if (CommonUtility.getDefaultGallery(this.mActivity.getApplicationContext(), uri, str) != CommonUtility.DefaultGallerySetting.SONY_ALBUM) {
            launchAlbum(uri, str, z);
            return;
        }
        if (this.mActivity.isDeviceInSecurityLock()) {
            launchAlbum(uri, str, z);
        } else if (prepareInstantViewer(uri)) {
            showInstantViewer(uri, str, i, i2, i3, currentRequestId);
            launchAlbum(uri, str, z);
        } else {
            launchAlbum(uri, str, z);
        }
    }

    private void showInstantViewer(Uri uri, String str, int i, int i2, int i3, int i4) {
        hideApplicationNavigator();
        hideAutoReview();
        this.mInstantViewer.open(this.mInstantViewer.isAlbumBitmapSetting() ? null : uri, str, 0, i3, isFront(), new ReviewWindowListenerImpl(), i4);
    }

    private void launchAlbum(Uri uri, String str, boolean z) {
        if (this.mActivity.isDeviceInSecurityLock()) {
            List<Content.ContentInfo> localContentInfo = getBaseLayout().getContentsViewController().getLocalContentInfo();
            if (CamLog.VERBOSE) {
                CamLog.d("onClick : contentInfoList = " + localContentInfo.size());
            }
            if (localContentInfo.isEmpty()) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            for (Content.ContentInfo contentInfo : localContentInfo) {
                arrayList.add(contentInfo.mOriginalUri);
                arrayList2.add(contentInfo.mMimeType);
                if (contentInfo.mContentType == Content.ContentsType.BURST && contentInfo.mGroupedImage > 0) {
                    Iterator<Long> it = contentInfo.mMediaStoreIds.iterator();
                    while (it.hasNext()) {
                        arrayList3.add(it.next());
                    }
                } else {
                    arrayList3.add(Long.valueOf(contentInfo.mId));
                }
            }
            long[] jArr = new long[arrayList3.size()];
            for (int i = 0; i < arrayList3.size(); i++) {
                jArr[i] = ((Long) arrayList3.get(i)).longValue();
            }
            InstantViewer.launchAlbumSecure(this.mActivity, arrayList, arrayList2, this.mStateMachine.getPredictiveCaptureStoreInfo(), jArr);
            return;
        }
        InstantViewer.launchAlbum(this.mActivity, uri, str, z, this.mStateMachine.getPredictiveCaptureStoreInfo());
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setRecordingOrientation(int i) {
        this.mRecordingOrientation = i;
    }

    private void updateGeotagIcon() {
        if (getBaseLayout().getGeoTagIndicator() == null || this.mActivity == null || !needToShowGeoTagIndicator()) {
            return;
        }
        getBaseLayout().getGeoTagIndicator().set(GeotagManager.isGeoTagEnabled(this.mActivity.getStoredSettings().getUserSettings(), this.mActivity));
    }

    public void switchSemiAutoAvailability() throws Resources.NotFoundException {
        if (getBaseLayout().getSemiAutoControl().isInitialized() && getBaseLayout().getSemiAutoControl().get().isEnabled()) {
            disableSemiAutoControl();
        } else {
            enableSemiAutoControl(false);
        }
        if (this.mSettingDialogStack != null) {
            this.mSettingDialogStack.closeAllSettingDialogs();
        }
    }

    public void switchSemiAutoStateByTouch(boolean z) throws Resources.NotFoundException {
        if (this.mStateMachine == null || !this.mStateMachine.isMenuAvailable() || !isPreviewLayout(getCurrentLayoutPattern()) || isTouchCaptureEnabled() || isObjectTrackingEnabled() || isSmileShutterEnabled() || !isSemiAutoControlAvailable(getCapturingMode())) {
            return;
        }
        if (z) {
            enableSemiAutoControl(true);
        } else {
            disableSemiAutoControl();
        }
    }

    private boolean isSettingDialogOpened() {
        return this.mSettingDialogStack != null && this.mSettingDialogStack.isDialogOpened();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void hideViews() {
        getBaseLayout().getPhotoSmileCaptureIndicator().hide();
        getBaseLayout().getSceneIndicator().hide();
        getBaseLayout().getConditionIndicator().hide();
        getBaseLayout().getGeoTagIndicator().hide();
        getBaseLayout().getLowMemoryInternalIndicator().hide();
        getBaseLayout().getLowMemorySdIndicator().hide();
        getBaseLayout().getThermalIndicator().hide();
        getBaseLayout().getBatteryIndicator().hide();
        if (this.mHintText != null) {
            this.mHintText.hide();
        }
        if (this.mFocusRectangles != null) {
            this.mFocusRectangles.onUiComponentOverlaid();
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void showViews() {
        getBaseLayout().getPhotoSmileCaptureIndicator().show();
        if (needToShowGeoTagIndicator()) {
            getBaseLayout().getGeoTagIndicator().show();
        }
        getBaseLayout().getLowMemoryInternalIndicator().show();
        getBaseLayout().getLowMemorySdIndicator().show();
        getBaseLayout().getThermalIndicator().show();
        getBaseLayout().getBatteryIndicator().show();
        this.mHintText.showAll();
        this.mFocusRectangles.onUiComponentRemoved();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onModeControllableDraggingMove(NavigatorContents navigatorContents, NavigatorContents navigatorContents2, int i, float f) {
        if (CamLog.VERBOSE) {
            CamLog.d("onModeControllableDraggingMove()  from:" + navigatorContents.name() + " to:" + navigatorContents2.name() + " distance:" + i + " progress:" + f);
        }
        setPreviewAlpha(i);
        if (i > 0) {
            setApplicationNavigatorPosition(navigatorContents, f);
        } else if (i < 0) {
            setApplicationNavigatorPosition(navigatorContents, -f);
        }
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

    private void hideZoomBar() {
        setZoombarVisibility(false);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public Rect getPosition(Point point) {
        if (CamLog.VERBOSE) {
            CamLog.d("getPosition(x, y) = (" + point.x + ", " + point.y + ")");
        }
        Rect rect = new Rect(0, 0, getActivity().getResources().getDimensionPixelSize(R.dimen.focus_rect_single_width), getActivity().getResources().getDimensionPixelSize(R.dimen.focus_rect_single_height));
        Rect rectConvertPositionToAligned = CoordinateUtil.convertPositionToAligned(point.x, point.y, this.mEvf.getRect(), this.mEvf.getRect(), rect.width(), rect.height());
        if (CamLog.VERBOSE) {
            CamLog.d("getPosition: " + rectConvertPositionToAligned);
        }
        return rectConvertPositionToAligned;
    }

    private void setLeftIconsVisibility(boolean z) {
        if (z) {
            getBaseLayout().showLeftIconContainer();
        } else {
            getBaseLayout().hideLeftIconContainer();
        }
    }

    private void setFrontAngleSwitchButtonVisibility(boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("setFrontAngleSwitchButtonVisibility visible: " + z);
        }
        if (this.mFrontAngleSwitchButton == null) {
            return;
        }
        if (z) {
            this.mFrontAngleSwitchButton.setOnClickListener(this.mFrontAngleSwitchButtonClickListener);
            this.mFrontAngleSwitchButton.show();
        } else {
            this.mFrontAngleSwitchButton.hide();
            this.mFrontAngleSwitchButton.setOnClickListener(null);
        }
        this.mFrontAngleSwitchButton.setClickable(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFrontAngleSwitchButtonClickable(boolean z) {
        if (this.mFrontAngleSwitchButton == null) {
            return;
        }
        this.mFrontAngleSwitchButton.setClickable(z);
    }

    private boolean prepareInstantViewer(Uri uri) {
        return this.mInstantViewer.setAlbumBitmap(uri);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void prepareGestureShutterCountDown() {
        if (this.mPhotoSelfTimerSetting == SelfTimer.OFF) {
            this.mPhotoSelfTimerSetting = SelfTimer.GESTURE_SHUTTER_COUNT_DOWN;
            if (this.mSelfTimerCountDownViewNext == null) {
                setupSelfTimerCountDownView();
            }
            this.mSelfTimerCountDownViewNext.setSelfTimer(this.mPhotoSelfTimerSetting);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setShutterTrigger(ShutterTrigger shutterTrigger) {
        this.mShutterTrigger = shutterTrigger;
        applyShutterTriggerSettings();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void updateVideoShutterTrigger() {
        applyShutterTriggerSettings();
    }

    private void applyShutterTriggerSettings() {
        this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_GESTURE_SHUTTER_SETTING_CHANGED, Boolean.valueOf(this.mShutterTrigger.isGestureShutterOn()));
        SmileCapture smileCapture = (SmileCapture) this.mStateMachine.getUserSetting().get(UserSettingKey.SMILE_CAPTURE);
        VideoSmileCapture videoSmileCapture = (VideoSmileCapture) this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SMILE_CAPTURE);
        if (getBaseLayout() == null || smileCapture == null || videoSmileCapture == null) {
            return;
        }
        getBaseLayout().getPhotoSmileCaptureIndicator().set(smileCapture.isSmileCaptureOn());
        getBaseLayout().getPhotoSmileCaptureIndicator().setBackgroundResource(smileCapture.getNotificationIconId());
        getBaseLayout().getVideoSmileCaptureIndicator().set(videoSmileCapture.isSmileCaptureOn());
        getBaseLayout().getVideoSmileCaptureIndicator().setBackgroundResource(videoSmileCapture.getNotificationIconId());
        applySmileFocusThreshold(true);
    }

    private void applySmileFocusThreshold(boolean z) {
        if (this.mFocusRectangles != null) {
            int dimenId = -1;
            if (z) {
                SmileCapture smileCapture = (SmileCapture) this.mStateMachine.getUserSetting().get(UserSettingKey.SMILE_CAPTURE);
                VideoSmileCapture videoSmileCapture = (VideoSmileCapture) this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SMILE_CAPTURE);
                if (smileCapture != SmileCapture.OFF && !isZooming() && !isInSelfTimerCountDown()) {
                    dimenId = smileCapture.getDimenId();
                }
                if (videoSmileCapture != VideoSmileCapture.OFF && !isZooming() && !isInSelfTimerCountDown() && this.mStateMachine.isRecording()) {
                    dimenId = videoSmileCapture.getDimenId();
                }
            }
            this.mFocusRectangles.setSmileCaptureThreshold(dimenId);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void showSurface() {
        if (this.mEvf.isShown()) {
            return;
        }
        this.mEvf.show();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void hideSurface() {
        this.mEvf.hide();
    }

    public void reconstructLocalCache() {
        if (getBaseLayout() == null || getBaseLayout().getContentsViewController() == null) {
            return;
        }
        getBaseLayout().getContentsViewController().reconstructLocalCache();
    }

    public void saveLocalCache() {
        if (getBaseLayout() == null || getBaseLayout().getContentsViewController() == null) {
            return;
        }
        getBaseLayout().getContentsViewController().saveLocalCache();
    }

    public void requestCreateContentInfoSync(ArrayList<Uri> arrayList) {
        if (getBaseLayout() == null || getBaseLayout().getContentsViewController() == null) {
            return;
        }
        getBaseLayout().getContentsViewController().requestCreateContentInfoSync(arrayList);
    }

    private void checkupThermalCoolingRequest() {
        if (PlatformCapability.isPowerSavingSupported(this.mStateMachine.getCurrentCameraId())) {
            if (this.mActivity.isThermalWarningReceived()) {
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_ULTRA_LOW, new Object[0]);
            } else if (this.mActivity.isThermalWarningExtraState()) {
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_HEATED_OVER_COOLING_LOW, new Object[0]);
            }
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void onNotifyCoolingUltraLow(boolean z) throws Resources.NotFoundException {
        if (getCapturingMode() == CapturingMode.SLOW_MOTION) {
            return;
        }
        updateThermalHintTextMessage(getCapturingMode());
        if (z && !isZooming()) {
            showHintTextIfNeeded();
        } else {
            this.mHintText.hide();
        }
        if (this.mFocusRectangles != null) {
            this.mFocusRectangles.clearObjectTracking();
            this.mFocusRectangles.clearFaceDetection();
        }
        if (getBaseLayout().getSceneIndicator() != null) {
            getBaseLayout().getSceneIndicator().set(false);
        }
        if (getBaseLayout().getConditionIndicator() != null) {
            getBaseLayout().getConditionIndicator().set(false);
        }
    }

    private void onNotifyThermalStatus(boolean z) throws Resources.NotFoundException {
        if (getBaseLayout().getThermalIndicator() != null) {
            getBaseLayout().getThermalIndicator().set(z);
        }
        if (!z && this.mActivity.isThermalWarningReceived()) {
            updateThermalHintTextMessage(getCapturingMode());
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void postSlowMotionHintText() throws Resources.NotFoundException {
        HintTextContent hintTextSuperSlowMotion;
        SlowMotion slowMotion = (SlowMotion) this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION);
        if (this.mHintText == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("postSlowMotionHintText: hint text is null");
                return;
            }
            return;
        }
        cancelSlowMotionHintText();
        if (slowMotion == SlowMotion.OFF) {
            return;
        }
        HintTextContent hintTextSuperSlowMotionDescription = null;
        switch (slowMotion) {
            case SUPER_SLOW_MOTION:
                hintTextSuperSlowMotion = new HintTextSuperSlowMotion();
                break;
            case SUPER_SLOW_SHOT:
                hintTextSuperSlowMotion = new HintTextSuperSlowShot();
                break;
            case STANDARD_SLOW_MOTION:
                hintTextSuperSlowMotion = new HintTextStandardSlowMotion();
                break;
            default:
                hintTextSuperSlowMotion = null;
                break;
        }
        postHintText(hintTextSuperSlowMotion);
        if (this.mIsAlreadySlowMotionLearnMoreButtonDisplayed) {
            return;
        }
        this.mIsAlreadySlowMotionLearnMoreButtonDisplayed = true;
        switch (slowMotion) {
            case SUPER_SLOW_MOTION:
                hintTextSuperSlowMotionDescription = new HintTextSuperSlowMotionDescription(getBaseLayout().getTutorial(), this.mActivity);
                break;
            case SUPER_SLOW_SHOT:
                hintTextSuperSlowMotionDescription = new HintTextSuperSlowShotDescription(getBaseLayout().getTutorial(), this.mActivity);
                break;
            case STANDARD_SLOW_MOTION:
                hintTextSuperSlowMotionDescription = new HintTextStandardSlowMotionDescription(getBaseLayout().getTutorial(), this.mActivity);
                break;
        }
        postHintText(hintTextSuperSlowMotionDescription);
    }

    private void postHintText(HintTextContent hintTextContent) throws Resources.NotFoundException {
        if (this.mHintText != null) {
            this.mHintText.post(hintTextContent);
            updateVisibilityForSpecificDisplaySize();
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

    private void showSuperSlowMotionVideoRecordingHintText() throws Resources.NotFoundException {
        if (this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION) == SlowMotion.SUPER_SLOW_MOTION) {
            this.mHintText.cancel(HintTextSuperSlowMotionVideoRecording.createTag(true));
            postHintText(new HintTextSuperSlowMotionVideoRecording(false));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllOverlayControlVisibility() {
        updateOverlayControlVisibility(getBaseLayout().getImageQualityControl());
        updateOverlayControlVisibility(getBaseLayout().getSemiAutoControl());
    }

    private void updateOverlayControlVisibility(BaseLayout.LazyInitializer<OverlayControl> lazyInitializer) {
        if (lazyInitializer.isInitialized()) {
            if (isAutoReviewShowing() || this.mIsAutoReviewRequested) {
                lazyInitializer.get().hide();
            } else if (isPreviewLayout(getCurrentLayoutPattern()) || getCurrentLayoutPattern() == BaseLayoutPattern.OVERLAY_CONTROL_SEEKING) {
                lazyInitializer.get().show();
            } else {
                lazyInitializer.get().hide();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVisibilityForSpecificDisplaySize() {
        if (isInLargerOrMoreDisplaySizeOr16_9Device()) {
            BaseLayout.LazyInitializer<OverlayControl> semiAutoControl = getBaseLayout().getSemiAutoControl();
            if (semiAutoControl.isInitialized() && (semiAutoControl.get().isVisible() || (semiAutoControl.get().isEnabled() && (this.mIsAutoReviewRequested || isAutoReviewShowing())))) {
                hideApplicationNavigator();
                hideMruButtonContainer();
                return;
            } else if (this.mHintText != null && this.mHintText.isNoTimeOutHinTextDisplayed() && this.mOrientation == 1 && getCurrentLayoutPattern() != BaseLayoutPattern.MODE_CHANGING) {
                hideApplicationNavigator();
                hideMruButtonContainer();
                return;
            }
        }
        if (isPreviewLayout(getCurrentLayoutPattern()) || getCurrentLayoutPattern() == BaseLayoutPattern.MODE_CHANGING) {
            showApplicationNavigator();
            showMruButtonContainer();
        }
    }

    private boolean isInLargerOrMoreDisplaySizeOr16_9Device() {
        return this.mBaseLayout.isInLargerOrMoreDisplaySize() || this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.SIXTEEN_NINE;
    }

    private class OnAutoReviewEventListenerImpl implements AutoReviewController.OnAutoReviewEventListener {
        private OnAutoReviewEventListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.AutoReviewController.OnAutoReviewEventListener
        public void onAutoReviewClosed() {
            ViewFinderImpl.this.updateAllOverlayControlVisibility();
            ViewFinderImpl.this.updateVisibilityForSpecificDisplaySize();
        }
    }

    public class OnScreenImageQualityControlButtonListener implements OnScreenButtonListener {
        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onCancel(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onDown(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onLongPress(OnScreenButton onScreenButton) {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onMove(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        public OnScreenImageQualityControlButtonListener() {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
        public void onUp(OnScreenButton onScreenButton, MotionEvent motionEvent) throws Resources.NotFoundException {
            if (ViewFinderImpl.this.getBaseLayout().getImageQualityControl().get().isEnabled()) {
                ViewFinderImpl.this.disableOverlayControl(ViewFinderImpl.this.getBaseLayout().getImageQualityControl());
            } else {
                ViewFinderImpl.this.enableOverlayControl(ViewFinderImpl.this.getBaseLayout().getImageQualityControl());
            }
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    public String getString(int i) {
        return ResourceUtil.getString(getActivity(), i);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setSelfTimer(CapturingMode capturingMode, SelfTimer selfTimer) {
        if (CamLog.VERBOSE) {
            CamLog.d("setSelfTimer: " + capturingMode + " " + selfTimer);
        }
        if (this.mActivity.isOneShotVideo()) {
            return;
        }
        if (selfTimer == null) {
            selfTimer = SelfTimer.OFF;
        }
        this.mPhotoSelfTimerSetting = selfTimer;
        setupSelfTimerCountDownView();
    }

    private boolean isRecording() {
        if (this.mLayoutPattern == null) {
            return false;
        }
        int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[((BaseLayoutPattern) this.mLayoutPattern).ordinal()];
        return i == 1 || i == 3;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void commit() {
        ViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.PHOTO_READY;
        switch (getCapturingMode()) {
            case SCENE_RECOGNITION:
            case SUPERIOR_FRONT:
            case NORMAL:
            case FRONT_PHOTO:
                headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.PHOTO_READY;
                break;
            case VIDEO:
            case FRONT_VIDEO:
                if (isRecording()) {
                    if (this.mLayoutPattern == BaseLayoutPattern.RECORDING) {
                        headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING;
                        break;
                    } else {
                        headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.VIDEO_PAUSING;
                        break;
                    }
                } else {
                    headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.VIDEO_READY;
                    break;
                }
            case SLOW_MOTION:
                switch ((SlowMotion) this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION)) {
                    case SUPER_SLOW_MOTION:
                        if (isRecording()) {
                            headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_MOTION_RECORDING;
                            break;
                        } else {
                            headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY;
                            break;
                        }
                    case SUPER_SLOW_SHOT:
                        headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY;
                        break;
                    case STANDARD_SLOW_MOTION:
                        if (isRecording()) {
                            headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_RECORDING;
                            break;
                        } else {
                            headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY;
                            break;
                        }
                }
            default:
                headUpDisplaySetupState = ViewFinder.HeadUpDisplaySetupState.PHOTO_READY;
                break;
        }
        if (getCurrentLayoutPattern() != BaseLayoutPattern.SELFTIMER) {
            changeScreenButtonImage(headUpDisplaySetupState, false);
        }
        updateSecondaryShortcutOnScreenButtonResource();
        if (this.mIsSurfaceViewHideWhileAspectChanging) {
            this.mEvf.show();
            this.mIsSurfaceViewHideWhileAspectChanging = false;
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void updateFocusIconType(boolean z) {
        this.mFocusRectangles.setFocusIconType(z);
    }

    private boolean isAutoReviewEnabled() {
        if (this.mActivity.isOneShot() || getCapturingMode().isVideo()) {
            return false;
        }
        switch ((AutoReview) this.mActivity.getStoredSettings().getUserSettings().get(UserSettingKey.AUTO_REVIEW)) {
        }
        return false;
    }

    public void closeDialogs() {
        if (this.mSettingUi != null) {
            this.mSettingUi.closeDialogs();
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setIsCameraSwitching(boolean z) {
        getBaseLayout().setIsCameraSwitching(z);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isCameraSwitching() {
        return getBaseLayout().isCameraSwitching();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void updateBatteryIndicator(int i) {
        getBaseLayout().getBatteryIndicator().setBatteryLevel(i);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void showSavingProgressBar() throws Resources.NotFoundException {
        if (isHeadUpDisplayReady()) {
            this.mScreenButtonHandler.clearAllButton();
        }
        if (this.mSavingProgressBar == null) {
            int dimensionPixelSize = this.mActivity.getResources().getDimensionPixelSize(R.dimen.saving_progress_bar_size);
            this.mSavingProgressBar = new ProgressBar(this.mActivity);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize);
            layoutParams.gravity = 17;
            this.mActivity.getWindow().addContentView(this.mSavingProgressBar, layoutParams);
        }
        this.mSavingProgressBar.setVisibility(0);
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void hideSavingProgressBar() {
        if (this.mSavingProgressBar != null) {
            this.mSavingProgressBar.setVisibility(8);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void startPredictiveCaptureIndicatorAnimation() {
        getBaseLayout().getPredictiveCaptureIndicatorController().startAnimation();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void cancelPredictiveCaptureIndicatorAnimation() {
        if (getBaseLayout().getPredictiveCaptureIndicatorController() != null) {
            getBaseLayout().getPredictiveCaptureIndicatorController().cancelAnimation();
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void updateSlowMotionView(SlowMotion slowMotion) {
        this.mIsAlreadySlowMotionLearnMoreButtonDisplayed = false;
        switch (slowMotion) {
            case SUPER_SLOW_MOTION:
                changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY, false);
                break;
            case SUPER_SLOW_SHOT:
                changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY, false);
                break;
            case STANDARD_SLOW_MOTION:
                changeScreenButtonImage(ViewFinder.HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY, false);
                break;
        }
        disableSemiAutoControl();
        if (getBaseLayout() != null) {
            disableOverlayControl(getBaseLayout().getImageQualityControl());
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void startSlowMotionFeedbackAnimation() {
        getBaseLayout().getSuperSlowMotionTriggerAnimation().start(new SuperSlowMotionTriggerAnimationController.OnAnimationEndListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.22
            @Override // com.sonyericsson.android.camera.view.SuperSlowMotionTriggerAnimationController.OnAnimationEndListener
            public void onAnimationEnd() {
                ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SLOW_MOTION_FEEDBACK_ANIMATION_END, new Object[0]);
            }
        }, this.mRecordingOrientation == 2);
    }

    private void showHintTextIfNeeded() {
        if ((!isOverlayControlEnabled() || this.mStateMachine.isRecording()) && this.mHintText != null) {
            this.mHintText.showAll();
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void showBlank() {
        this.mBaseLayout.setupBlankScreen();
        this.mBaseLayout.showBlankScreen();
    }

    private boolean isOverlayControlEnabled() {
        if (getBaseLayout().getSemiAutoControl().isInitialized() && getBaseLayout().getSemiAutoControl().get().isEnabled()) {
            return true;
        }
        return getBaseLayout().getImageQualityControl().isInitialized() && getBaseLayout().getImageQualityControl().get().isEnabled();
    }

    private boolean isOverlayControlVisible() {
        if (getBaseLayout().getSemiAutoControl().isInitialized() && getBaseLayout().getSemiAutoControl().get().isVisible()) {
            return true;
        }
        return getBaseLayout().getImageQualityControl().isInitialized() && getBaseLayout().getImageQualityControl().get().isVisible();
    }

    public boolean isSemiAutoEnabled() {
        return getBaseLayout().getSemiAutoControl().isInitialized() && getBaseLayout().getSemiAutoControl().get().isEnabled();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isAutoReviewShowing() {
        if (this.mSideTouchUi != null) {
            return getBaseLayout().isAutoReviewShowing() || this.mSideTouchUi.containsIn(SideTouchUi.Type.AUTO_REVIEW);
        }
        return getBaseLayout().isAutoReviewShowing();
    }

    private boolean isPaused() {
        return this.mIsPaused;
    }

    private void resume(CapturingMode capturingMode, NavigatorContents navigatorContents) {
        this.mIsPaused = false;
        if (isHeadUpDisplayReady()) {
            setApplicationNavigatorEnabled(false);
            if (this.mAnimationController != null) {
                this.mAnimationController.resume();
            }
        }
        if (this.mBaseLayout != null && this.mBaseLayout.getContentsViewController() != null) {
            this.mBaseLayout.getContentsViewController().remove();
        }
        if (isHeadUpDisplayReady()) {
            this.mBaseLayout.setupBlankScreen();
        }
        this.mBaseLayout.resume();
    }

    private void pause() {
        this.mIsPaused = true;
        if (isHeadUpDisplayReady()) {
            disableSemiAutoControl();
            disableOverlayControl(getBaseLayout().getImageQualityControl());
        }
        if (this.mAnimationController != null) {
            this.mAnimationController.pause();
            setIsSwitchingAnimationProgress(false);
        }
        this.mBaseLayout.pause();
        if (this.mInstantViewer != null) {
            this.mInstantViewer.releaseAlbumPreloader();
        }
        TutorialController tutorial = getBaseLayout().getTutorial();
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

    private static boolean isSemiAutoControlAvailable(CapturingMode capturingMode) {
        int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()];
        if (i == 7) {
            return true;
        }
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAppsUiModeFinish() {
        if (!getActivity().isInLockTaskMode()) {
            changeLayoutTo(BaseLayoutPattern.CLEAR);
            if (this.mSettingDialogStack != null) {
                this.mSettingDialogStack.closeAllSettingDialogs(false);
            }
        }
        this.mActivity.abort();
    }

    private void setupTransitionAnimationController(CameraActivity cameraActivity, ViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (this.mAnimationController == null) {
            this.mAnimationController = new TransitionAnimationController(this.mApplicationNavigator, getBaseLayout().getPrimaryShortcut().getAllPrimaryShortcutView(), cameraActivity.findViewById(R.id.sub_button), cameraActivity.findViewById(R.id.extra_button), cameraActivity.findViewById(R.id.main_button), cameraActivity.findViewById(R.id.inner_cover), getBaseLayout().getGridLineView(), getBaseLayout().getModeButtonShortcut(), getBaseLayout().getMruButtonContainer(), cameraActivity.findViewById(R.id.contents_container), getBaseLayout().getFrontAngleSwitchButton(), this.mBaseLayout.getSwitchAnimationView());
        }
        this.mAnimationController.resume();
        if (this.mPreviewCover == null) {
            this.mPreviewCover = getActivity().findViewById(R.id.inner_cover);
        }
        this.mPreviewCover.setAlpha(0.0f);
        this.mPreviewCover.setVisibility(0);
    }

    private void startModeChangedAnimation(CapturingMode capturingMode, CapturingMode capturingMode2, AnimationRequest.AnimationType animationType) {
        if (!requestAnimation(new AnimationRequest(animationType, AnimationRequest.AnimationDegree.FINISH, capturingMode, capturingMode2)) || this.mApplicationNavigator == null) {
            return;
        }
        this.mApplicationNavigator.resetContentDescriptionForModeName();
    }

    private void showApplicationNavigator() {
        if (this.mApplicationNavigator != null) {
            this.mApplicationNavigator.show();
        }
    }

    private void hideApplicationNavigator() {
        if (this.mApplicationNavigator != null) {
            this.mApplicationNavigator.hide();
        }
    }

    private void showMruButtonContainer() {
        if (getBaseLayout().getMruButtonContainer() != null) {
            getBaseLayout().getMruButtonContainer().show();
        }
    }

    private void hideMruButtonContainer() {
        if (getBaseLayout().getMruButtonContainer() != null) {
            getBaseLayout().getMruButtonContainer().hide();
        }
    }

    private void resumeApplicationNavigator(NavigatorContents navigatorContents) {
        boolean zIsOneShot = getActivity().isOneShot();
        if (this.mApplicationNavigator != null) {
            setApplicationNavigatorEnabled(!zIsOneShot);
            if (zIsOneShot) {
                return;
            }
            this.mApplicationNavigator.resume(navigatorContents);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setApplicationNavigatorEnabled(boolean z) {
        switch (getCapturingMode()) {
            case SCENE_RECOGNITION:
            case SUPERIOR_FRONT:
            case VIDEO:
            case FRONT_VIDEO:
                break;
            default:
                z = false;
                break;
        }
        if (predictiveLaunchCoverExists()) {
            z = false;
        }
        if (this.mApplicationNavigator != null) {
            this.mApplicationNavigator.setNavigationEnabled(z);
        }
    }

    private void setupApplicationNavigator(CameraActivity cameraActivity, NavigatorContents navigatorContents) throws Resources.NotFoundException {
        if (this.mApplicationNavigator == null) {
            this.mApplicationNavigator = (ApplicationNavigator) cameraActivity.findViewById(R.id.application_navigator);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mApplicationNavigator.getLayoutParams();
            View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.23
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ViewFinderImpl.this.transitionModeOnNavigator(((Integer) view.getTag()).intValue());
                }
            };
            layoutParams.rightMargin = this.mBaseLayout.calculateCaptureButtonAreaHeight();
            this.mApplicationNavigator.setLayoutParams(layoutParams);
            this.mApplicationNavigator.setup(navigatorContents, getBaseLayout().getViewFinderRect(), this.mBaseLayout.calculateCaptureButtonAreaHeight(), onClickListener);
            this.mApplicationNavigator.setOrientation(this.mOrientation);
        }
        resumeApplicationNavigator(navigatorContents);
    }

    private void setupCaptureButtonArea() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mActivity.findViewById(R.id.right_container).getLayoutParams();
        layoutParams.width = this.mBaseLayout.calculateCaptureButtonAreaHeight();
        this.mActivity.findViewById(R.id.right_container).setLayoutParams(layoutParams);
    }

    private void setupRightIndicatorArea() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mActivity.findViewById(R.id.left_indicator_container).getLayoutParams();
        layoutParams.rightMargin = this.mBaseLayout.calculateCaptureButtonAreaHeight();
        this.mActivity.findViewById(R.id.left_indicator_container).setLayoutParams(layoutParams);
    }

    private void setApplicationNavigatorPosition(NavigatorContents navigatorContents, float f) {
        this.mApplicationNavigator.setDraggingPosition(navigatorContents, f);
    }

    private void setPreviewAlpha(int i) {
        this.mPreviewCover.setAlpha(TransitionAnimationController.getPreviewAlpha(getActivity(), i));
    }

    public static CapturingMode getCapturingMode(NavigatorContents navigatorContents, CapturingMode capturingMode) {
        switch (navigatorContents) {
            case SUPERIOR_AUTO:
                if (!capturingMode.isFront()) {
                    break;
                } else {
                    break;
                }
            case VIDEO:
                if (!capturingMode.isFront()) {
                    break;
                } else {
                    break;
                }
        }
        return CapturingMode.SCENE_RECOGNITION;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setStartDraggingSlopEnabled(boolean z) {
        getBaseLayout().setStartDraggingSlopEnabled(z);
    }

    private void setZoombarVisibility(boolean z) {
        Zoombar zoomBar = getBaseLayout().getZoomBar();
        if (zoomBar != null) {
            if (z) {
                zoomBar.showImmediately();
            } else {
                zoomBar.hideImmediately();
            }
        }
    }

    private boolean startDraggingSwitchStartedAnimation() {
        if (!this.mAnimationController.requestAnimation(new AnimationRequest(AnimationRequest.AnimationType.SWITCH_TOUCH, AnimationRequest.AnimationDegree.START, getCapturingMode(), getCapturingMode()))) {
            return false;
        }
        this.mBaseLayout.computeRadiusOfAnimation();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDraggingSwitchAnimation(float f) {
        if (this.mAnimationController.startSwitchDraggingAnimation(f)) {
            this.mPreviewCover.setAlpha(f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetAnimationProperty() {
        this.mAnimationController.resume();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isSwitchingAnimationProgress() {
        return this.mIsSwitchingAnimationProgress;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIsSwitchingAnimationProgress(boolean z) {
        this.mIsSwitchingAnimationProgress = z;
    }

    private void setupHintText() {
        FrameLayout hintTextViewContainer;
        if (this.mHintText != null || (hintTextViewContainer = getBaseLayout().getHintTextViewContainer()) == null) {
            return;
        }
        this.mHintText = new HintTextViewController(hintTextViewContainer, new HintTextListenerImpl(), this.mScreenAspect);
        updateHintTextUiOrientation();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void clearHintText() {
        if (this.mHintText != null) {
            this.mHintText.clearAll();
        }
    }

    private void updateThermalHintTextMessage(CapturingMode capturingMode) throws Resources.NotFoundException {
        if (this.mHintText == null || capturingMode == CapturingMode.SLOW_MOTION) {
            return;
        }
        postHintText(new HintTextThermalWarning());
    }

    private Content getCurrentContent() {
        ContentsViewController contentsViewController = getBaseLayout().getContentsViewController();
        if (contentsViewController == null) {
            CamLog.w("getCurrentContent() contentsViewController is null.");
            return null;
        }
        return contentsViewController.getCurrentContent();
    }

    private int getCurrentRequestId() {
        ContentsViewController contentsViewController = getBaseLayout().getContentsViewController();
        if (contentsViewController == null) {
            CamLog.w("getCurrentRequestId() contentsViewController is null.");
            return -1;
        }
        return contentsViewController.getCurrentRequestId();
    }

    private class GestureShutterListener implements GestureShutter.WindowHost {
        private GestureShutterView mGestureShutterView;

        private GestureShutterListener() {
            this.mGestureShutterView = null;
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.WindowHost
        public GestureShutterView getGestureShutterView() {
            setupGestureShutterView();
            return this.mGestureShutterView;
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.WindowHost
        public void showGestureShutterView() {
            hideGestureShutterView();
            setupGestureShutterView();
            ViewFinderImpl.this.getBaseLayout().getLazyInflatedUiComponentContainerBack().addView(this.mGestureShutterView);
            ViewFinderImpl.this.getBaseLayout().getLazyInflatedUiComponentContainerBack().bringChildToFront(this.mGestureShutterView);
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.WindowHost
        public void hideGestureShutterView() {
            if (this.mGestureShutterView != null) {
                ViewFinderImpl.this.getBaseLayout().getLazyInflatedUiComponentContainerBack().removeView(this.mGestureShutterView);
            }
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.WindowHost
        public Point getPreviewSize() {
            return LayoutOrientationResolver.getInstance().getPointAccordingToLayoutOrientation(new Point(ViewFinderImpl.this.getBaseLayout().getPreviewContainer().getWidth(), ViewFinderImpl.this.getBaseLayout().getPreviewContainer().getHeight()));
        }

        @Override // com.sonyericsson.android.camera.controller.GestureShutter.WindowHost
        public Rect getViewFinderSize() {
            return LayoutDependencyResolver.getViewFinderSize(ViewFinderImpl.this.mActivity);
        }

        private void setupGestureShutterView() {
            if (this.mGestureShutterView == null) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                this.mGestureShutterView = new GestureShutterView(ViewFinderImpl.this.getActivity());
                this.mGestureShutterView.setLayoutParams(layoutParams);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CameraActivity getActivity() {
        return this.mActivity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BaseLayout getBaseLayout() {
        return this.mBaseLayout;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder, com.sonyericsson.cameracommon.viewfinder.ViewFinderInterface
    public boolean isHeadUpDisplayReady() {
        return this.mBaseLayout != null && this.mBaseLayout.isHeadUpDisplayReady();
    }

    private void requestToRecoverSystemUi() {
        getBaseLayout().requestToRecoverSystemUi();
    }

    private void requestToDimSystemUi() {
        getBaseLayout().requestToDimSystemUi();
    }

    private void requestToRestoreSystemUi() {
        getBaseLayout().requestToRestoreSystemUi();
    }

    private boolean isAcquired() {
        return this.mActivity.getGeoTagManager().isNetworkAcquired() | this.mActivity.getGeoTagManager().isGpsAcquired();
    }

    private void setPreInflatedHeadUpDisplay(View view) {
        this.mPreInflatedHeadUpDisplay = view;
    }

    private void requestSetupHeadUpDisplay() throws Resources.NotFoundException {
        setupHeadUpDisplay();
        if (CamLog.VERBOSE) {
            MeasurePerformance.outResultDelay(1000);
        }
    }

    private void setZoomRatio(int i) {
        if (isHeadUpDisplayReady()) {
            if (CamLog.VERBOSE) {
                CamLog.d("setZoomRatio() current:" + i);
            }
            this.mZoomBarProxy.update(PlatformCapability.getZoomRatios(this.mStateMachine.getCurrentCameraId()), i);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder, com.sonyericsson.cameracommon.viewfinder.ViewFinderInterface
    public void onShutterDone(boolean z) {
        if (isHeadUpDisplayReady()) {
            clearTouchedScreenButtonGroup();
        }
    }

    private void createViewFinder(CameraActivity cameraActivity, LayoutPatternApplier layoutPatternApplier, boolean z) {
        this.mActivity = cameraActivity;
        this.mLayoutPatternApplier = layoutPatternApplier;
        if (z) {
            return;
        }
        initialize();
    }

    public void initialize() {
        this.mBaseLayout = new BaseLayout(this.mActivity, this.mScreenAspect);
        this.mActivity.addOrienationListener(this);
    }

    private void setupHeadUpDisplay() throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("setupHeadUpDisplay() is called.");
        }
        boolean z = this.mPreInflatedHeadUpDisplay == null;
        if (this.mPreInflatedHeadUpDisplay != null) {
            this.mBaseLayout.setPreInflatedHeadUpDisplay(this.mPreInflatedHeadUpDisplay);
            this.mPreInflatedHeadUpDisplay = null;
        }
        this.mBaseLayout.setOrientation(this.mActivity.getOrientation());
        this.mBaseLayout.setup(getThumbnailStateListener());
        this.mBaseLayout.getTutorial().setOnClickTutorialButtonListener(this.mOnClickTutorialButtonListener);
        this.mBaseLayout.getTutorial().setSystemUiAccessor(this.mSystemUiAccessor);
        UserSettings userSetting = this.mStateMachine.getUserSetting();
        this.mBaseLayout.setupImageQualityControl(this.mUiControlSettings, new OverlayControlStateListener(ViewFinder.UiComponentKind.OVERLAY_CONTROL_SEEKING), new EnumValueAccessorImpl(userSetting, UserSettingKey.CAPTURING_MODE), new EnumValueAccessorImpl(userSetting, UserSettingKey.FOCUS_RANGE), new EnumValueAccessorImpl(userSetting, UserSettingKey.SHUTTER_SPEED), new EnumValueAccessorImpl(userSetting, UserSettingKey.ISO), new EnumValueAccessorImpl(userSetting, UserSettingKey.EV), new EnumValueAccessorImpl(userSetting, UserSettingKey.WHITE_BALANCE));
        if (getCapturingMode() == CapturingMode.SLOW_MOTION) {
            this.mBaseLayout.getSuperSlowMotionTriggerAnimation().prepareViews();
        }
        updateIndicatorState();
        if (!z) {
            this.mBaseLayout.reloadContentsViewController(getThumbnailStateListener());
        }
        if (this.mActivity.getGeoTagManager() != null) {
            this.mActivity.getGeoTagManager().setLocationAcquiredListener(new LocationAcquiredListenerImpl());
        }
        if (this.mActivity.getStorage() != null) {
            this.mActivity.getStorage().addStorageStateListener(this.mStorageStateListener);
        }
        getBaseLayout().getZoomBar().setZoombarDisplayChangedListener(new ZoombarDisplayChangedListenerImpl());
        switch (getCapturingMode()) {
            case NORMAL:
            case FRONT_PHOTO:
                enableOverlayControl(getBaseLayout().getImageQualityControl());
                break;
            case SLOW_MOTION:
                getBaseLayout().getSuperSlowMotionTriggerAnimation().prepareViews();
                break;
        }
    }

    private class ZoombarDisplayChangedListenerImpl implements Zoombar.ZoombarDisplayChangedListener {
        private ZoombarDisplayChangedListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar.ZoombarDisplayChangedListener
        public void onShowZoombar() {
            ViewFinderImpl.this.getBaseLayout().getTopIndicator().setVisibility(4);
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar.ZoombarDisplayChangedListener
        public void onZoombarHidden() {
            ViewFinderImpl.this.getBaseLayout().getTopIndicator().setVisibility(0);
            if (ViewFinderImpl.this.mSideTouchUi.detachTo(SideTouchUi.Type.COVERING)) {
                if (ViewFinderImpl.this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING, SideTouchUi.Type.RECORDING_HDR)) {
                    ViewFinderImpl.this.changeLayoutTo(BaseLayoutPattern.RECORDING, true);
                }
                if (ViewFinderImpl.this.mSideTouchUi.containsIn(SideTouchUi.Type.RECORDING_PAUSE, SideTouchUi.Type.RECORDING_HDR_PAUSE)) {
                    ViewFinderImpl.this.changeLayoutTo(BaseLayoutPattern.PAUSE_RECORDING, true);
                }
            }
        }
    }

    private static class EnumValueAccessorImpl<T extends UserSettingValue> implements EnumValueAccessor<T> {
        private final UserSettingKey mKey;
        private final UserSettings mSettings;

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor
        public T reset() {
            return null;
        }

        private EnumValueAccessorImpl(UserSettings userSettings, UserSettingKey userSettingKey) {
            this.mSettings = userSettings;
            this.mKey = userSettingKey;
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor
        public T get() {
            return (T) this.mSettings.get(this.mKey);
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.ValueAccessor
        public void set(T t) {
            this.mSettings.set(t);
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.EnumValueAccessor
        public T[] values() {
            return (T[]) this.mSettings.getOptions(this.mKey);
        }
    }

    private void updateIndicatorState() {
        if (needToShowGeoTagIndicator()) {
            if (GeotagManager.isGeoTagEnabled(this.mActivity.getStoredSettings().getUserSettings(), this.mActivity)) {
                if (this.mActivity.getGeoTagManager() != null) {
                    boolean zIsAcquiring = this.mActivity.getGeoTagManager().isAcquiring();
                    this.mBaseLayout.getGeoTagIndicator().set(true);
                    this.mBaseLayout.getGeoTagIndicator().isAcquired(!zIsAcquiring);
                }
            } else {
                this.mBaseLayout.getGeoTagIndicator().set(false);
            }
        }
        updateLowMemoryIndicator();
        updateThermalIndicator();
        updateBatteryIndicator(this.mActivity.getBatteryLevel());
    }

    private void updateLowMemoryIndicator() {
        this.mBaseLayout.getLowMemoryInternalIndicator().set(!hasEnoughFreeSpace(Storage.StorageType.INTERNAL));
        this.mBaseLayout.getLowMemorySdIndicator().set(!hasEnoughFreeSpace(Storage.StorageType.EXTERNAL_CARD));
    }

    private void updateThermalIndicator() {
        this.mBaseLayout.getThermalIndicator().set(this.mActivity.isThermalWarningState());
    }

    private class LocationAcquiredListenerImpl implements LocationAcquiredListener {
        private LocationAcquiredListenerImpl() {
        }

        @Override // com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener
        public void onAcquired(boolean z, boolean z2) {
            if (ViewFinderImpl.this.isHeadUpDisplayReady()) {
                ViewFinderImpl.this.mBaseLayout.getGeoTagIndicator().isAcquired(z || z2);
            }
        }

        @Override // com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener
        public void onLost() {
            if (ViewFinderImpl.this.isHeadUpDisplayReady()) {
                ViewFinderImpl.this.mBaseLayout.getGeoTagIndicator().isAcquired(false);
            }
        }

        @Override // com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener
        public void onDisabled() {
            ViewFinderImpl.this.mActivity.getStoredSettings().getUserSettings().set(Geotag.OFF);
            ViewFinderImpl.this.mBaseLayout.getGeoTagIndicator().set(false);
            ViewFinderImpl.this.mActivity.readLocationSettings();
        }
    }

    private void startInflateTask(LayoutInflater layoutInflater, List<InflateItem> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("startInflateTask in");
        }
        ExecutorService executorServiceBuildExecutor = ThreadUtil.buildExecutor("InflateTask");
        this.mInflateFuture = executorServiceBuildExecutor.submit(new InflateTask(layoutInflater, list));
        executorServiceBuildExecutor.shutdown();
        if (CamLog.VERBOSE) {
            CamLog.d("startInflateTask out");
        }
    }

    private void joinInflateTask() {
        if (CamLog.VERBOSE) {
            CamLog.d("joinInflateTask in");
        }
        if (this.mInflateFuture != null) {
            try {
                this.mInflateItemMap = this.mInflateFuture.get();
            } catch (InterruptedException e) {
                CamLog.e("join", e);
            } catch (ExecutionException e2) {
                CamLog.e("join", e2);
            }
            this.mInflateFuture = null;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("joinInflateTask out");
        }
    }

    private void clearPreInflatedViews() {
        if (this.mInflateItemMap != null) {
            this.mInflateItemMap.clear();
            this.mInflateItemMap = null;
        }
    }

    private boolean isInflated() {
        return this.mInflateItemMap != null;
    }

    private List<View> getPreInflatedView(InflateItem inflateItem) {
        if (this.mInflateItemMap != null) {
            return this.mInflateItemMap.get(inflateItem);
        }
        return null;
    }

    private LayoutPatternApplier getLayoutPatternApplier() {
        return this.mLayoutPatternApplier;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LayoutPattern getCurrentLayoutPattern() {
        return this.mLayoutPattern;
    }

    private LayoutPattern selectLayoutPatternForPreview() {
        BaseLayoutPattern baseLayoutPattern = BaseLayoutPattern.PREVIEW;
        if (!isStorageReady()) {
            baseLayoutPattern = BaseLayoutPattern.PREVIEW_NO_RECORDING;
        }
        return predictiveLaunchCoverExists() ? BaseLayoutPattern.CLEAR : baseLayoutPattern;
    }

    private boolean isPreviewLayout(LayoutPattern layoutPattern) {
        return layoutPattern == BaseLayoutPattern.PREVIEW || layoutPattern == BaseLayoutPattern.PREVIEW_NO_RECORDING;
    }

    public boolean isPreviewLayout() {
        return isPreviewLayout(getCurrentLayoutPattern());
    }

    private void updateGridLineView() {
        updateGridLineView(null);
    }

    private void updateGridLineView(CapturingMode capturingMode) {
        if (!isHeadUpDisplayReady() || this.mBaseLayout.getGridLineView() == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("called updateGridLineView before ready");
                return;
            }
            return;
        }
        UserSettings userSetting = this.mStateMachine.getUserSetting();
        if (capturingMode == null) {
            capturingMode = (CapturingMode) userSetting.get(UserSettingKey.CAPTURING_MODE);
        }
        Size sizeComputeGridSize = computeGridSize(capturingMode, userSetting);
        this.mBaseLayout.updateGridLine(sizeComputeGridSize.getWidth(), sizeComputeGridSize.getHeight());
        GridLine gridLine = (GridLine) userSetting.get(UserSettingKey.GRID_LINE);
        if (predictiveLaunchCoverExists()) {
            this.mBaseLayout.setGridLineViewEnabled(false);
        } else {
            this.mBaseLayout.setGridLineViewEnabled(gridLine == GridLine.ON);
        }
    }

    private Size computeGridSize(CapturingMode capturingMode, UserSettings userSettings) {
        Rect viewFinderRectSetting = getViewFinderRectSetting(userSettings, capturingMode);
        float fWidth = viewFinderRectSetting.width() / viewFinderRectSetting.height();
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            fWidth = 1.0f / fWidth;
        }
        Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect(this.mActivity, fWidth, this.mScreenAspect);
        return new Size(surfaceViewRect.width(), surfaceViewRect.height());
    }

    private Rect getViewFinderRectSetting(UserSettings userSettings, CapturingMode capturingMode) {
        if (capturingMode.isVideo()) {
            return ((VideoSize) userSettings.get(UserSettingKey.VIDEO_SIZE)).getVideoRect();
        }
        return ((Resolution) userSettings.get(UserSettingKey.RESOLUTION)).getPictureRect();
    }

    private void updatePreviewContainer(int i, int i2) {
        this.mBaseLayout.updatePreviewContainer(i, i2);
    }

    private void updateHintTextContainer(Rect rect) {
        Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(rect);
        Rect surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect(this.mActivity, rectAccordingToLayoutOrientation.width() / rectAccordingToLayoutOrientation.height(), this.mScreenAspect);
        this.mHintText.updateHintTextContainer(surfaceViewRect.width(), surfaceViewRect.height());
        this.mHintText.setUiOrientation(surfaceViewRect, this.mActivity, this.mScreenAspect, this.mOrientation);
    }

    public void showHiSpeedSdCardRecommendDialogOnModeChange() {
        if (isNeedToShowHiSpeedSdCardRecommendation() && this.mStateMachine.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
            showMessageDialog(DialogId.HIGH_SPEED_SD_RECOMMENDATION_ON_MODE_CHANGE, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showHiSpeedSdCardRecommendDialogOnVideoSizeChange() {
        VideoSize videoSize = (VideoSize) this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SIZE);
        if (isNeedToShowHiSpeedSdCardRecommendation() && videoSize.is4KVideo()) {
            showMessageDialog(DialogId.FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_VIDEOSIZE_CHANGE, new Object[0]);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void showHiSpeedSdCardRecommendDialogOnDestinationChange() {
        if (isNeedToShowHiSpeedSdCardRecommendation()) {
            VideoSize videoSize = (VideoSize) this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SIZE);
            if (this.mStateMachine.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION) {
                showMessageDialog(DialogId.HIGH_SPEED_SD_RECOMMENDATION_ON_SETTING_CHANGE, new Object[0]);
            } else if (videoSize.is4KVideo()) {
                showMessageDialog(DialogId.FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_DESTINATION_CHANGE, new Object[0]);
            }
        }
    }

    private boolean isNeedToShowHiSpeedSdCardRecommendation() {
        DestinationToSave destinationToSave = (DestinationToSave) this.mActivity.getStoredSettings().getUserSettings().get(UserSettingKey.DESTINATION_TO_SAVE);
        if (destinationToSave.getType() != Storage.StorageType.EXTERNAL_CARD) {
            return false;
        }
        Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(destinationToSave.getType());
        return currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.AVAILABLE_NEAR_FULL;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isMessageDialogOpened() {
        return this.mMessageDialog.isOpened();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void clearMessageDialog() {
        this.mMessageDialog.clear();
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void showMessageDialog(DialogId dialogId, Object... objArr) {
        if (CamLog.VERBOSE) {
            CamLog.d("showMessageDialog() E : " + dialogId);
        }
        ShowMessageDialogTask showMessageDialogTask = new ShowMessageDialogTask(dialogId, objArr);
        switch (AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[dialogId.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
                if (this.mSettingUi == null) {
                    this.mDelayUpdatedViewTaskList.add(showMessageDialogTask);
                    break;
                } else {
                    showMessageDialogTask.run();
                    break;
                }
            default:
                showMessageDialogTask.run();
                break;
        }
    }

    private class ShowMessageDialogTask implements Runnable {
        private MessageDialogRequest mRequestParam = new MessageDialogRequest();

        public ShowMessageDialogTask(DialogId dialogId, Object... objArr) {
            this.mRequestParam.mDialogId = dialogId;
            this.mRequestParam.mOptions = objArr;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!ViewFinderImpl.this.mIsSetupHeadupDisplayInvoked) {
                ViewFinderImpl.this.mMessageDialog.setSensorOrientation(ViewFinderImpl.this.mActivity.getOrientation());
            }
            if (ViewFinderImpl.this.mMessageDialog.request(this.mRequestParam)) {
                return;
            }
            switch (AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[this.mRequestParam.mDialogId.ordinal()]) {
                case 5:
                    ViewFinderImpl.this.mActivity.requestLaunchAdvancedCamera(LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU, (String) this.mRequestParam.mOptions[0]);
                    break;
                case 6:
                    ViewFinderImpl.this.showHiSpeedSdCardRecommendDialogOnVideoSizeChange();
                    break;
            }
        }
    }

    public class MessageDialogOnClickPositiveListenerImpl implements MessageDialogController.MessageDialogOnClickListener {
        public MessageDialogOnClickPositiveListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnClickListener
        public void onClick(MessageDialogRequest messageDialogRequest) throws Resources.NotFoundException {
            switch (AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 9:
                    ViewFinderImpl.this.openUserSelectMenu(UserSettingKey.DESTINATION_TO_SAVE);
                    break;
                case 5:
                    Object[] objArr = messageDialogRequest.mOptions;
                    if (objArr != null) {
                        ViewFinderImpl.this.mActivity.requestLaunchAdvancedCamera(LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU, (String) objArr[0]);
                        break;
                    } else {
                        ViewFinderImpl.this.mActivity.requestLaunchAdvancedCamera(LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU, null);
                        break;
                    }
                case 6:
                    ViewFinderImpl.this.showHiSpeedSdCardRecommendDialogOnVideoSizeChange();
                    break;
                case 7:
                case 8:
                    ViewFinderImpl.this.mStateMachine.getUserSetting().set(Geotag.ON);
                    ViewFinderImpl.this.launchLocationSourceSettings();
                    break;
                case 10:
                    Intent intent = (Intent) messageDialogRequest.mOptions[0];
                    Bundle bundle = (Bundle) messageDialogRequest.mOptions[1];
                    ViewFinderImpl.this.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT, (Mode) messageDialogRequest.mOptions[2]);
                    ViewFinderImpl.this.requestStartActivityForMessageDialog(intent, bundle);
                    break;
                case 11:
                    ViewFinderImpl.this.openUserSelectMenu(UserSettingKey.DESTINATION_TO_SAVE);
                    ViewFinderImpl.this.mSettingUi.updateSettingMenu(false);
                    break;
                case 12:
                    ViewFinderImpl.this.launchSideSenseSettings();
                    break;
                case 13:
                    ViewFinderImpl.this.mActivity.requestRestartCameraActivityAfterResetSettings();
                    break;
                case 14:
                case 15:
                    PermissionsUtil.requestSdCardGranted(ViewFinderImpl.this.mActivity, 20, StorageUtil.getVolumeUuid(Storage.StorageType.EXTERNAL_CARD, ViewFinderImpl.this.mActivity.getApplicationContext()));
                    break;
            }
        }
    }

    private class MessageDialogOnClickNegativeListenerImpl implements MessageDialogController.MessageDialogOnClickListener {
        private MessageDialogOnClickNegativeListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnClickListener
        public void onClick(MessageDialogRequest messageDialogRequest) {
            int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()];
            if (i != 12) {
                switch (i) {
                    case 7:
                        ViewFinderImpl.this.updateLocation();
                        break;
                    case 8:
                        ViewFinderImpl.this.openSettingMenuDialogInChina();
                        ViewFinderImpl.this.updateLocation();
                        break;
                }
            }
            ViewFinderImpl.this.mStateMachine.getUserSetting().set(SideSense.OFF);
        }
    }

    private class MessageDialogOnCancelListenerImpl implements MessageDialogController.MessageDialogOnCancelListener {
        private MessageDialogOnCancelListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnCancelListener
        public void onCancel(MessageDialogRequest messageDialogRequest) {
            int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()];
            if (i != 12) {
                switch (i) {
                    case 6:
                        ViewFinderImpl.this.showHiSpeedSdCardRecommendDialogOnVideoSizeChange();
                        break;
                    case 7:
                        ViewFinderImpl.this.updateLocation();
                        break;
                    case 8:
                        ViewFinderImpl.this.openSettingMenuDialogInChina();
                        ViewFinderImpl.this.updateLocation();
                        break;
                }
            }
            ViewFinderImpl.this.mStateMachine.getUserSetting().set(SideSense.OFF);
        }
    }

    private class MessageDialogOnDismissListenerImpl implements MessageDialogController.MessageDialogOnDismissListener {
        private MessageDialogOnDismissListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnDismissListener
        public void onDismiss(MessageDialogRequest messageDialogRequest) throws Resources.NotFoundException {
            if (messageDialogRequest.mDialogId != DialogId.PREDICTIVE_LAUNCH_DESCRIPTION) {
                ViewFinderImpl.this.hidePredictiveLaunchCover(PredictiveLaunchHideTrigger.OTHER);
            }
            int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()];
            if (i != 8) {
                if (i != 13) {
                    switch (i) {
                        default:
                            switch (i) {
                                case 16:
                                case 17:
                                    PlatformCapability.setDeviceError(true);
                                    ViewFinderImpl.this.exitByError();
                                    break;
                                case 18:
                                case 19:
                                case 20:
                                case 21:
                                    ViewFinderImpl.this.exitByError();
                                    break;
                                case 22:
                                case 23:
                                case 24:
                                case 25:
                                    ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, ViewFinder.UiComponentKind.FATAL_ALERT_DIALOG);
                                    break;
                                case 31:
                                    ViewFinderImpl.this.mActivity.setupAutoPowerOffTimeOutDuration(ViewFinderImpl.this.predictiveLaunchCoverExists());
                                    ViewFinderImpl.this.mActivity.restartAutoPowerOffTimer();
                                    break;
                            }
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            ViewFinderImpl.this.onCloseStorageDialog();
                            break;
                    }
                }
                ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, ViewFinder.UiComponentKind.NOTICE_DIALOG);
                return;
            }
            ViewFinderImpl.this.openSettingMenuDialogInChina();
        }
    }

    private class MessageDialogOnOpenListenerImpl implements MessageDialogController.MessageDialogOnOpenListener {
        private MessageDialogOnOpenListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogController.MessageDialogOnOpenListener
        public void onOpen(MessageDialogRequest messageDialogRequest) {
            int i = AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[messageDialogRequest.mDialogId.ordinal()];
            if (i != 13) {
                switch (i) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        break;
                    default:
                        switch (i) {
                        }
                }
                ViewFinderImpl.this.onOpenStorageDialog();
                return;
            }
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_OPENED, ViewFinder.UiComponentKind.NOTICE_DIALOG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitByError() {
        if (PlatformCapability.hasDeviceError()) {
            if (this.mActivity != null) {
                this.mActivity.finishAndKillProcess();
                return;
            } else {
                Process.killProcess(Process.myPid());
                return;
            }
        }
        if (this.mActivity == null || this.mIsPaused) {
            return;
        }
        this.mActivity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchLocationSourceSettings() {
        ApplicationLauncher.launchLocationSourceSettings(this.mActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchSideSenseSettings() {
        ApplicationLauncher.launchSideSenseSettings(this.mActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLocation() {
        this.mActivity.getGeoTagManager().updateLocation(Geotag.OFF);
        this.mStateMachine.getUserSetting().set(Geotag.OFF);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openSettingMenuDialogInChina() {
        if (RegionConfig.isChinaRegion(this.mActivity)) {
            this.mSettingUi.openSettingMenuDialog();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOpenStorageDialog() {
        if (this.mCurrentDisplayingUiComponent == null) {
            return;
        }
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_OPENED, new Object[0]);
        if (this.mSettingUi != null) {
            this.mSettingUi.closeDialogs();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCloseStorageDialog() {
        if (isAllDialogClosed()) {
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, ViewFinder.UiComponentKind.SETTING_DIALOG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openUserSelectMenu(UserSettingKey userSettingKey) {
        if (userSettingKey != null && AnonymousClass32.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] == 3) {
            if (((VideoSize) this.mStateMachine.getUserSetting().get(UserSettingKey.VIDEO_SIZE)).is4KVideo()) {
                this.mSettingDialogStack.closeAllSettingDialogs(false);
            }
            requestToRecoverSystemUi();
        }
        updateUiComponent(ViewFinder.UiComponentKind.SETTING_DIALOG);
        this.mSettingUi.openUserSelectMenu(userSettingKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestStartActivityForMessageDialog(final Intent intent, final Bundle bundle) {
        Handler handler = getBaseLayout().getRootView().getHandler();
        if (handler != null) {
            handler.post(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.24
                @Override // java.lang.Runnable
                public void run() {
                    ViewFinderImpl.this.requestStartActivity(intent, bundle);
                    ViewFinderImpl.this.mActivity.abort();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean requestStartActivity(Intent intent, Bundle bundle) {
        if (!this.mActivity.isInLockTaskMode()) {
            this.mCameraDevice.closeCamera(true);
            this.mIsRequestingStartActivity = true;
            try {
                if (!CapturingModeUtil.MODE_WHITE_LIST.contains(intent.getStringExtra("com.sonymobile.camera.addon.intent.extra.CAPTURING_MODE"))) {
                    this.mActivity.startActivity(intent);
                    onAppsUiModeFinish();
                } else if (bundle != null) {
                    this.mActivity.startActivityForResult(intent, 19, bundle);
                } else {
                    this.mActivity.startActivityForResult(intent, 19);
                }
                return true;
            } catch (ActivityNotFoundException e) {
                CamLog.e("Failed to launch the AddOn application. Message : " + e.getMessage());
                return false;
            }
        }
        this.mActivity.finish();
        return false;
    }

    public class HintTextListenerImpl implements HintTextViewController.HintTextContentListener {
        public HintTextListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.hint.HintTextViewController.HintTextContentListener
        public void onContentButtonClick(HintTextViewController hintTextViewController, HintTextContent hintTextContent) throws Resources.NotFoundException {
            if (hintTextContent instanceof HintTextThermal) {
                onClickThermalReadMore();
            } else if (hintTextContent instanceof HintTextSlowMotionDescription) {
                onClickSlowMotionDescription(hintTextViewController, (HintTextSlowMotionDescription) hintTextContent);
            }
        }

        @Override // com.sonyericsson.android.camera.view.hint.HintTextViewController.HintTextContentListener
        public void onStateChanged() {
            ViewFinderImpl.this.updateVisibilityForSpecificDisplaySize();
        }

        private void onClickSlowMotionDescription(final HintTextViewController hintTextViewController, final HintTextSlowMotionDescription hintTextSlowMotionDescription) {
            if (((SlowMotion) ViewFinderImpl.this.mStateMachine.getUserSetting().get(UserSettingKey.SLOW_MOTION)) == SlowMotion.OFF) {
                return;
            }
            hintTextViewController.hide();
            final TutorialController tutorial = ViewFinderImpl.this.getBaseLayout().getTutorial();
            tutorial.open(TutorialController.OpenType.createByReadMore(hintTextSlowMotionDescription.getTutorialType()), null, new TutorialContentView.OnClickCloseButtonListener() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.HintTextListenerImpl.1
                @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.OnClickCloseButtonListener
                public void onClickCloseButton(View view) {
                    int id = view.getId();
                    if (id == 2131296487) {
                        LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.GOT_IT);
                        LocalResearchUtil.getInstance().closeSetupWizard();
                    } else if (id == 2131296491) {
                        LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.SKIP);
                        LocalResearchUtil.getInstance().closeSetupWizard();
                    }
                    hintTextViewController.cancel(hintTextSlowMotionDescription.getTag());
                    tutorial.close();
                    if (ViewFinderImpl.this.mHintText != null) {
                        ViewFinderImpl.this.mHintText.showAll();
                    }
                    ViewFinderImpl.this.setApplicationNavigatorEnabled(!ViewFinderImpl.this.mActivity.isOneShot());
                    ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
                }
            });
            ViewFinderImpl.this.changeLayoutTo(BaseLayoutPattern.CLEAR);
            ViewFinderImpl.this.setApplicationNavigatorEnabled(false);
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_OPENED, new Object[0]);
        }

        private void onClickThermalReadMore() throws Resources.NotFoundException {
            StringBuilder sb = new StringBuilder();
            sb.append(ViewFinderImpl.this.getString(R.string.cam_strings_focus_mode_face_detection_txt));
            if (ViewFinderImpl.this.getCapturingMode() == CapturingMode.SCENE_RECOGNITION || ViewFinderImpl.this.getCapturingMode() == CapturingMode.SUPERIOR_FRONT || ViewFinderImpl.this.getCapturingMode() == CapturingMode.VIDEO || ViewFinderImpl.this.getCapturingMode() == CapturingMode.FRONT_VIDEO) {
                sb.append(System.lineSeparator());
                sb.append(ViewFinderImpl.this.getString(R.string.cam_strings_auto_scene_recognition_txt));
            }
            if (!ViewFinderImpl.this.getCapturingMode().isFront()) {
                sb.append(System.lineSeparator());
                sb.append(ViewFinderImpl.this.getString(R.string.cam_strings_focus_mode_object_tracking_txt));
            }
            if (ViewFinderImpl.this.getCapturingMode().isFront() && !ViewFinderImpl.this.getCapturingMode().isVideo()) {
                sb.append(System.lineSeparator());
                sb.append(ViewFinderImpl.this.getString(R.string.cam_strings_hand_shutter_txt));
            }
            if (ViewFinderImpl.this.isPredictiveCaptureAvailable()) {
                sb.append(System.lineSeparator());
                sb.append(ViewFinderImpl.this.getString(R.string.cam_strings_predictive_capture_txt));
            }
            MessageDialogRequest messageDialogRequest = new MessageDialogRequest();
            messageDialogRequest.mDialogId = DialogId.COOLING_MODE;
            messageDialogRequest.mMessageList = sb.toString();
            ViewFinderImpl.this.mMessageDialog.request(messageDialogRequest);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPredictiveCaptureAvailable() {
        return getCapturingMode().isSuperiorAuto() && !getCapturingMode().isFront() && !getActivity().isOneShot() && PlatformCapability.isBypassCameraSupported() && PlatformCapability.isPredictiveCaptureShotSupported(getCapturingMode().getCameraId());
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void notifyZoomOperationRejected() throws Resources.NotFoundException {
        if (isAllDialogClosed()) {
            postHintText(new HintTextTimedOutMessage(HintTextTimedOutMessage.MessageType.ZOOM_NOT_AVAILABLE));
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isAutoPowerOffWarningDisplayed() {
        if (this.mHintText != null) {
            return this.mHintText.isHintTextDisplayed(HintTextAutoPowerOff.class.getSimpleName());
        }
        return false;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void showAutoPowerOffHintText() throws Resources.NotFoundException {
        if (this.mHintText != null) {
            postHintText(new HintTextAutoPowerOff());
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void hideAutoPowerOffHintText() {
        if (this.mHintText != null) {
            this.mHintText.cancel(HintTextAutoPowerOff.class.getSimpleName());
            updateVisibilityForSpecificDisplaySize();
        }
    }

    private void updateFusionHintText(@Nullable CameraParameters.FusionResult fusionResult) throws Resources.NotFoundException {
        if (this.mHintText != null && PlatformCapability.isHighSensitivityFusionSupported(getCapturingMode().getCameraId())) {
            this.mHintText.cancel(HintTextHighSensitivityFusionStatus.class.getSimpleName());
            this.mHintText.cancel(HintTextHighSensitivityFusionCondition.class.getSimpleName());
            if (fusionResult != null) {
                if (fusionResult.getFusionCondition() == CameraParameters.FusionCondition.CLOSE_TO_SUBJECT) {
                    postHintText(new HintTextHighSensitivityFusionCondition());
                } else if (fusionResult.getFusionStatus() == CameraParameters.FusionStatus.FUSION_SUB_1) {
                    postHintText(new HintTextHighSensitivityFusionStatus());
                } else {
                    updateVisibilityForSpecificDisplaySize();
                }
            }
        }
    }

    private void showToastMessage(ToastContent.ToastID toastID) throws Resources.NotFoundException {
        if (!this.mIsSetupHeadupDisplayInvoked) {
            LayoutDependencyResolver.setupRotatableToast(this.mActivity);
            this.mToastContent.setSensorOrientation(this.mActivity.getOrientation());
        }
        this.mToastContent.show(this.mActivity, toastID);
    }

    private boolean isStorageReady() {
        Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(getCurrentStorage());
        return currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.AVAILABLE_NEAR_FULL;
    }

    private Storage.StorageType getCurrentStorage() {
        if (this.mActivity.isOneShot()) {
            return this.mActivity.getLaunchCondition().getStorageTypeForOneshot();
        }
        UserSettings userSettings = getActivity().getStoredSettings().getUserSettings();
        DestinationToSave destinationToSave = (DestinationToSave) userSettings.get(UserSettingKey.DESTINATION_TO_SAVE);
        if (destinationToSave == null) {
            destinationToSave = (DestinationToSave) userSettings.get(this.mActivity.getLaunchCondition().getCapturingMode(), UserSettingKey.DESTINATION_TO_SAVE);
        }
        return destinationToSave.getType();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasEnoughFreeSpace(Storage.StorageType storageType) {
        DestinationToSave destinationToSave = (DestinationToSave) this.mStateMachine.getUserSetting().get(UserSettingKey.DESTINATION_TO_SAVE);
        Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(storageType);
        return destinationToSave.getType() == storageType ? currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.UNGRANTED : currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.REMOVED || currentState == Storage.StorageState.UNGRANTED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInternalStorageWritable() {
        Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(Storage.StorageType.INTERNAL);
        return currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.AVAILABLE_NEAR_FULL;
    }

    private boolean isCurrentStorageExternal() {
        return getCurrentStorage() == Storage.StorageType.EXTERNAL_CARD;
    }

    private boolean isSdCardWritable() {
        Storage.StorageState currentState = this.mActivity.getStorage().getCurrentState(Storage.StorageType.EXTERNAL_CARD);
        return currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.AVAILABLE_NEAR_FULL;
    }

    private boolean isSdCardRemoved() {
        return this.mActivity.getStorage().getCurrentState(Storage.StorageType.EXTERNAL_CARD) == Storage.StorageState.REMOVED;
    }

    protected boolean onHandleBackKeyTutorial() {
        if (this.mBaseLayout != null && this.mBaseLayout.getTutorial() != null) {
            TutorialController tutorial = this.mBaseLayout.getTutorial();
            if (tutorial.isOpened()) {
                if (tutorial.backToPreviousPage()) {
                    return true;
                }
                MessageSettings messageSettings = this.mActivity.getStoredSettings().getMessageSettings();
                for (TutorialController.TutorialType tutorialType : tutorial.getTutorialTypes()) {
                    switch (tutorialType) {
                        case VIDEO_FUSION:
                            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE, FusionMode.AUTO);
                            break;
                        case MANUAL_FUSION:
                            updateHighSensitivityFusionModeForManual();
                            break;
                    }
                    Iterator<MessageType> it = tutorialType.messageTypes.iterator();
                    while (it.hasNext()) {
                        messageSettings.setNeverShow(it.next(), true);
                        messageSettings.save();
                    }
                }
                LocalResearchUtil.getInstance().sendSetupWizardEvent(Event.WizardResult.BACK_KEY);
                LocalResearchUtil.getInstance().closeSetupWizard();
                tutorial.close();
                setApplicationNavigatorEnabled(!this.mActivity.isOneShot());
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
                return true;
            }
        }
        return false;
    }

    protected boolean closeAutoReviewIfShowing() {
        if (!isAutoReviewShowing()) {
            return false;
        }
        hideAutoReview();
        return true;
    }

    protected boolean closeOverlayControlIfOpened() {
        if (!isOverlayControlVisible()) {
            return false;
        }
        disableSemiAutoControl();
        disableOverlayControl(getBaseLayout().getImageQualityControl());
        return true;
    }

    protected boolean closeSettingDialogIfOpened() {
        if (this.mSettingDialogStack != null) {
            return this.mSettingDialogStack.closeCurrentDialog();
        }
        return false;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void notifyStorageStateChanged(Storage.StorageType storageType, Storage.StorageState storageState, boolean z, boolean z2) {
        if (CamLog.VERBOSE) {
            CamLog.d("onStorageStateChanged: StorageType = " + storageType + ", StorageState = " + storageState + ", isChangeable = " + z);
        }
        if (storageState != Storage.StorageState.AVAILABLE && storageState != Storage.StorageState.AVAILABLE_NEAR_FULL && this.mFocusRectangles != null) {
            this.mFocusRectangles.clearFaceDetection();
        }
        switch (storageState) {
            case AVAILABLE:
            case AVAILABLE_NEAR_FULL:
                this.mMessageDialog.removeDialogsInList(STORAGE_DIALOG_LIST);
                break;
            case FULL:
                if (!z2) {
                    if (z) {
                        switch (storageType) {
                            case EXTERNAL_CARD:
                                showMessageDialog(DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL, new Object[0]);
                                break;
                            case INTERNAL:
                                showMessageDialog(DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_SD, new Object[0]);
                                break;
                        }
                    } else {
                        showMessageDialog(DialogId.MEMORY_FULL, new Object[0]);
                        break;
                    }
                } else {
                    showMessageDialog(DialogId.MEMORY_FULL_IN_BURST_MODE, new Object[0]);
                    break;
                }
                break;
            case UNAVAILABLE:
            case REMOVED:
            case READ_ONLY:
                if (z) {
                    switch (storageType) {
                        case EXTERNAL_CARD:
                            showMessageDialog(DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL, new Object[0]);
                            break;
                        case INTERNAL:
                            showMessageDialog(DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD, new Object[0]);
                            break;
                    }
                } else {
                    switch (storageType) {
                        case EXTERNAL_CARD:
                            showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE, new Object[0]);
                            break;
                        case INTERNAL:
                            showMessageDialog(DialogId.MEMORY_INTERNAL_UNAVAILABLE, new Object[0]);
                            break;
                    }
                }
            case CORRUPT:
                if (AnonymousClass32.$SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageType[storageType.ordinal()] == 1) {
                    showMessageDialog(DialogId.MEMORY_SD_UNAVAILABLE, new Object[0]);
                    break;
                }
                break;
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setDisplayFlashRequired(boolean z) {
        this.mRequireDisplayFlash = z;
    }

    private boolean isDisplayFlashRequired() {
        return this.mRequireDisplayFlash;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void setDisplayFlashColor(int i, int i2, int i3) {
        if (i < 0 || i > 255 || i2 < 0 || i2 > 255 || i3 < 0 || i3 > 255 || !isDisplayFlashRequired()) {
            this.mDisplayFlashColor = -1;
        } else {
            this.mDisplayFlashColor = Color.rgb(i, i2, i3);
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isDisplayFlashScreenDisplayed() {
        return this.mIsDisplayFlashScreenDisplayed;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void showDisplayFlashScreen() {
        if (isDisplayFlashRequired()) {
            if (this.mWindowDisplayFlashScreen == null) {
                LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
                if (layoutInflater == null) {
                    return;
                }
                this.mWindowDisplayFlashScreen = layoutInflater.inflate(R.layout.display_flash_screen, (ViewGroup) null);
                Window window = this.mActivity.getWindow();
                window.addContentView(this.mWindowDisplayFlashScreen, window.getAttributes());
            }
            if (this.mWindowDisplayFlashScreen != null) {
                this.mWindowDisplayFlashScreen.setBackgroundColor(this.mDisplayFlashColor);
                this.mWindowDisplayFlashScreen.setVisibility(0);
                this.mIsDisplayFlashScreenDisplayed = true;
            }
        }
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public void hideDisplayFlashScreen() {
        if (isDisplayFlashScreenDisplayed()) {
            if (this.mWindowDisplayFlashScreen != null) {
                this.mWindowDisplayFlashScreen.setVisibility(8);
            }
            this.mIsDisplayFlashScreenDisplayed = false;
        }
    }

    private void updateVideoHdrCondition(CapturingMode capturingMode, VideoHdr videoHdr, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("updateVideoHdrCondition : " + videoHdr);
        }
        if (!PlatformCapability.isVideoHdrSupported(capturingMode.getCameraId()) || !capturingMode.isVideo() || capturingMode == CapturingMode.SLOW_MOTION || this.mActivity.isOneShotVideo()) {
            return;
        }
        boolean zIsSelectable = UserSettingKey.VIDEO_HDR.isSelectable();
        boolean z2 = videoHdr == VideoHdr.HDR_ON;
        VideoSize videoSize = VideoSize.FULL_HD;
        if (z2 && zIsSelectable) {
            if (this.mFocusRectangles != null) {
                this.mFocusRectangles.clearAllFocus();
            }
            if (!z || this.mMessageDialog.isOpened()) {
                return;
            }
            showMessageDialog(DialogId.VIDEO_HDR_CAUTION, new Object[0]);
        }
    }

    private class ScreenButtonHandler {
        private ScreenButtonHandler() {
        }

        private class OnScreenButtonListenerImpl implements OnScreenButtonListener {
            private final OnScreenButtonItemFactory.ButtonType mButtonType;

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onMove(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            }

            public OnScreenButtonListenerImpl(OnScreenButtonItemFactory.ButtonType buttonType) {
                this.mButtonType = buttonType;
            }

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onDown(OnScreenButton onScreenButton, MotionEvent motionEvent) {
                ViewFinderImpl.this.mTouchEventDispatcher.sendTouchDown(this.mButtonType);
            }

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onUp(OnScreenButton onScreenButton, MotionEvent motionEvent) {
                ViewFinderImpl.this.mTouchEventDispatcher.sendTouchUp(this.mButtonType, new Point((int) motionEvent.getX(), (int) motionEvent.getY()));
            }

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onCancel(OnScreenButton onScreenButton, MotionEvent motionEvent) {
                ViewFinderImpl.this.mTouchEventDispatcher.sendCancel(this.mButtonType);
            }

            @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonListener
            public void onLongPress(OnScreenButton onScreenButton) {
                ViewFinderImpl.this.mTouchEventDispatcher.sendLongClick(this.mButtonType, null);
            }
        }

        protected void setMain(OnScreenButtonItemFactory.ButtonType buttonType, int i, boolean z) {
            setMain(buttonType, i, z, true);
        }

        protected void setMain(OnScreenButtonItemFactory.ButtonType buttonType, int i, boolean z, boolean z2) {
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().setMain(OnScreenButtonItemFactory.createButton(buttonType, z2 ? new OnScreenButtonListenerImpl(buttonType) : null), i, z);
        }

        protected void setOption1(OnScreenButtonItemFactory.ButtonType buttonType, int i, boolean z) {
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().setOption1(OnScreenButtonItemFactory.createButton(buttonType, new OnScreenButtonListenerImpl(buttonType)), i, z);
        }

        protected void setOption1(OnScreenButtonGroup.Item item, int i, boolean z) {
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().setOption1(item, i, z);
        }

        public void setOption2(OnScreenButtonItemFactory.ButtonType buttonType, int i, boolean z) {
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().setOption2(OnScreenButtonItemFactory.createButton(buttonType, new OnScreenButtonListenerImpl(buttonType)), i, z);
        }

        public void setOption2(OnScreenButtonGroup.Item item, int i, boolean z) {
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().setOption2(item, i, z);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearMain() {
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().clearMain();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearOption1() {
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().clearOption1();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearOption2() {
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().clearOption2();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearAllButton() {
            clearOption1();
            clearOption2();
            clearMain();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void refreshButton() {
            clearOption1();
            clearOption2();
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setMainRotatability(int i, boolean z) {
            ViewFinderImpl.this.getBaseLayout().getOnScreenButtonGroup().setMainRotatability(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPhotoSelfTimerEnabled() {
        return ((SelfTimer) this.mStateMachine.getUserSetting().get(UserSettingKey.SELF_TIMER)) != SelfTimer.OFF;
    }

    public boolean isSelfTimerCountDownViewShown() {
        return this.mSelfTimerCountDownView != null && this.mSelfTimerCountDownView.isShown();
    }

    public void clearTouchedScreenButtonGroup() {
        this.mBaseLayout.getOnScreenButtonGroup().clearTouched();
    }

    public void clearCanceledSideTouchEventIcons() {
        if (isSetupHeadupDisplayInvoked() && this.mSideTouchUi.containsIn(SideTouchUi.Type.CAPTURE_COUNTDOWN, SideTouchUi.Type.ZOOM_BAR)) {
            this.mSideTouchUi.destroyIcon();
        }
    }

    public boolean isFrontAngleChanging() {
        return this.mIsFrontAngleChanging;
    }

    public void clearBurstShootingRejectedReason() {
        this.mBurstShootingRejectedReason = ViewFinder.BurstRejectedReason.NONE;
    }

    public boolean canFocusRectanglesBeUpdated() {
        return this.mCanFocusRectanglesBeUpdated;
    }

    @Override // com.sonyericsson.android.camera.view.ViewFinder
    public boolean isUserOperable() {
        return isEvfPrepared() && isSetupHeadupDisplayInvoked() && isHeadUpDisplayReady() && !isFrontAngleChanging() && !isCameraSwitching() && !isSwitchingAnimationProgress();
    }

    private void onStoreCompleted(StoreDataResult storeDataResult, boolean z) {
        CamLog.d("onStoreCompleted() result:" + storeDataResult.savingRequest.getRequestId() + " isLast:" + z);
        if (z) {
            this.mIsAutoReviewRequested = false;
        }
        if (!isHeadUpDisplayReady()) {
            this.mAutoReviewStoreData = storeDataResult;
            return;
        }
        if (!storeDataResult.isSuccess() && isCurrentStorageExternal() && !isSdCardWritable() && !isSdCardRemoved()) {
            showMessageDialog(DialogId.COULD_NOT_SAVE_PHOTO, new Object[0]);
        }
        if (isShownInInstantViewer(storeDataResult)) {
            CamLog.d("Potho which is shown in Instant viewer is saved and start Album for the photo.");
            InstantViewer.launchAlbum(this.mActivity, storeDataResult.uri, storeDataResult.savingRequest.common.mimeType, true, this.mStateMachine.getPredictiveCaptureStoreInfo());
        } else {
            addThumbnail(storeDataResult);
            if (z) {
                showAutoReview(storeDataResult);
            }
        }
    }

    private void addThumbnail(final StoreDataResult storeDataResult) {
        boolean zIsPredictiveCaptureCoverImage;
        final int requestId = storeDataResult.savingRequest.getRequestId();
        final boolean zIsSuccess = storeDataResult.isSuccess();
        final Uri uri = storeDataResult.uri;
        boolean zIsPredictiveCaptureImage = false;
        if (storeDataResult.savingRequest.getFilePath() != null) {
            if (storeDataResult.savingRequest instanceof PhotoSavingRequest) {
                zIsPredictiveCaptureImage = ((PhotoSavingRequest) storeDataResult.savingRequest).isPredictiveCaptureImage();
                zIsPredictiveCaptureCoverImage = ((PhotoSavingRequest) storeDataResult.savingRequest).isPredictiveCaptureCoverImage();
            }
            if (zIsPredictiveCaptureImage || zIsPredictiveCaptureCoverImage) {
                this.mActivity.runOnUiThread(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.27
                    @Override // java.lang.Runnable
                    public void run() throws Resources.NotFoundException {
                        if (ViewFinderImpl.this.getBaseLayout().getContentsViewController() != null) {
                            switch (AnonymousClass32.$SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[storeDataResult.savingRequest.common.savedFileType.ordinal()]) {
                                case 1:
                                    if (storeDataResult.savingRequest.isFinalInSavingGroup()) {
                                        ViewFinderImpl.this.getBaseLayout().getContentsViewController().requestLastContentLoading(requestId);
                                        return;
                                    }
                                    return;
                                case 2:
                                    LayoutPattern currentLayoutPattern = ViewFinderImpl.this.getCurrentLayoutPattern();
                                    if (currentLayoutPattern == BaseLayoutPattern.RECORDING || currentLayoutPattern == BaseLayoutPattern.PAUSE_RECORDING) {
                                        ViewFinderImpl.this.startHideThumbnail();
                                        break;
                                    }
                                    break;
                            }
                            if (requestId == -1) {
                                ViewFinderImpl.this.getBaseLayout().getContentsViewController().remove();
                                ViewFinderImpl.this.getBaseLayout().getContentsViewController().pause();
                                ViewFinderImpl.this.getBaseLayout().getContentsViewController().reload();
                            } else if (!zIsSuccess) {
                                ViewFinderImpl.this.getBaseLayout().getContentsViewController().pause();
                                ViewFinderImpl.this.getBaseLayout().getContentsViewController().reload();
                            } else {
                                PerfLog.STORE_COMPLETE.transit();
                                ViewFinderImpl.this.getBaseLayout().getContentsViewController().addContent(requestId, uri);
                                PerfLog.THUMBNAIL_SHOW.transit();
                            }
                        }
                    }
                });
            }
            return;
        }
        CamLog.d("File path is not set by storage error.");
        zIsPredictiveCaptureCoverImage = false;
        if (zIsPredictiveCaptureImage) {
        }
        this.mActivity.runOnUiThread(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.27
            @Override // java.lang.Runnable
            public void run() throws Resources.NotFoundException {
                if (ViewFinderImpl.this.getBaseLayout().getContentsViewController() != null) {
                    switch (AnonymousClass32.$SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[storeDataResult.savingRequest.common.savedFileType.ordinal()]) {
                        case 1:
                            if (storeDataResult.savingRequest.isFinalInSavingGroup()) {
                                ViewFinderImpl.this.getBaseLayout().getContentsViewController().requestLastContentLoading(requestId);
                                return;
                            }
                            return;
                        case 2:
                            LayoutPattern currentLayoutPattern = ViewFinderImpl.this.getCurrentLayoutPattern();
                            if (currentLayoutPattern == BaseLayoutPattern.RECORDING || currentLayoutPattern == BaseLayoutPattern.PAUSE_RECORDING) {
                                ViewFinderImpl.this.startHideThumbnail();
                                break;
                            }
                            break;
                    }
                    if (requestId == -1) {
                        ViewFinderImpl.this.getBaseLayout().getContentsViewController().remove();
                        ViewFinderImpl.this.getBaseLayout().getContentsViewController().pause();
                        ViewFinderImpl.this.getBaseLayout().getContentsViewController().reload();
                    } else if (!zIsSuccess) {
                        ViewFinderImpl.this.getBaseLayout().getContentsViewController().pause();
                        ViewFinderImpl.this.getBaseLayout().getContentsViewController().reload();
                    } else {
                        PerfLog.STORE_COMPLETE.transit();
                        ViewFinderImpl.this.getBaseLayout().getContentsViewController().addContent(requestId, uri);
                        PerfLog.THUMBNAIL_SHOW.transit();
                    }
                }
            }
        });
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

    private boolean isShownInInstantViewer(StoreDataResult storeDataResult) {
        return this.mInstantViewer != null && this.mInstantViewer.isOpened() && storeDataResult.savingRequest.getRequestId() == this.mInstantViewer.getRequestId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHighSensitivityFusionModeForManual() {
        if (UserSettingKey.FUSION_MODE.isSelectable()) {
            FusionMode fusionMode = (FusionMode) this.mStateMachine.getUserSetting().get(UserSettingKey.FUSION_MODE);
            StateMachine stateMachine = this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_REQUEST_UPDATE_HIGH_SENSITIVITY_FUSION_MODE;
            Object[] objArr = new Object[1];
            objArr[0] = fusionMode == FusionMode.OFF ? FusionMode.ON : FusionMode.OFF;
            stateMachine.sendEvent(transitterEvent, objArr);
            return;
        }
        showMessageDialog(UserSettingKey.FUSION_MODE.getRestrictMessageDialogId(this.mStateMachine.getUserSetting()), new Object[0]);
    }

    private void enableSemiAutoControl(boolean z) throws Resources.NotFoundException {
        if (getBaseLayout().getSemiAutoControl().isInitialized() && getBaseLayout().getSemiAutoControl().get().isEnabled()) {
            return;
        }
        getBaseLayout().setupSemiAutoControl(new OverlayControlStateListener(ViewFinder.UiComponentKind.OVERLAY_CONTROL_SEEKING), this.mColorValueAccessor, this.mBrightnessValueAccessor, z);
        enableOverlayControl(getBaseLayout().getSemiAutoControl());
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_SEMIAUTO_ENABLED, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableOverlayControl(BaseLayout.LazyInitializer<OverlayControl> lazyInitializer) throws Resources.NotFoundException {
        if (lazyInitializer.isInitialized() && lazyInitializer.get().isEnabled()) {
            return;
        }
        lazyInitializer.get().enable();
        lazyInitializer.get().setOrientation(this.mOrientation);
        updateAllOverlayControlVisibility();
        updateVisibilityForSpecificDisplaySize();
        getBaseLayout().getSceneIndicator().set(false);
        getBaseLayout().getConditionIndicator().set(false);
        if (this.mHintText != null) {
            this.mHintText.show(HintTextContent.HintPriority.HIGH);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableSemiAutoControl() {
        if (this.mStateMachine == null || getBaseLayout() == null) {
            return;
        }
        BaseLayout.LazyInitializer<OverlayControl> semiAutoControl = getBaseLayout().getSemiAutoControl();
        if (semiAutoControl.isInitialized() && semiAutoControl.get().isEnabled()) {
            if (isTouchFocus()) {
                this.mFocusRectangles.clearTouchFocus();
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CLEAR_FOCUS, new Object[0]);
            }
            disableOverlayControl(getBaseLayout().getSemiAutoControl());
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_SEMIAUTO_DISABLED, new Object[0]);
        }
    }

    private void disableModeIconClickable() {
        this.mApplicationNavigator.setModeIconClickable(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableOverlayControl(BaseLayout.LazyInitializer<OverlayControl> lazyInitializer) {
        if (lazyInitializer.isInitialized() && lazyInitializer.get().isEnabled()) {
            lazyInitializer.get().disable();
            if (this.mHintText != null && !isSettingDialogOpened()) {
                this.mHintText.showAll();
            }
            updateVisibilityForSpecificDisplaySize();
        }
    }

    private class OverlayControlStateListener implements OverlayControl.StateListener {
        private final ViewFinder.UiComponentKind mKind;

        public OverlayControlStateListener(ViewFinder.UiComponentKind uiComponentKind) {
            this.mKind = uiComponentKind;
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl.StateListener
        public void onValueUpdateStart() {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_OPENED, this.mKind);
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl.StateListener
        public void onValueUpdateEnd() {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, this.mKind);
        }
    }

    public void hidePredictiveLaunchCover(PredictiveLaunchHideTrigger predictiveLaunchHideTrigger) throws Resources.NotFoundException {
        if (predictiveLaunchCoverExists()) {
            switch (predictiveLaunchHideTrigger) {
                case HW_CAMERA_KEY:
                case SIDE_SENSING:
                case VOLUME_KEY_SHUTTER:
                case TOUCH_UP_CAPTURE:
                    VibrationManager.vibrate(this.mActivity, VibrationManager.VibrationPattern.EFFECT_FOR_CAPTURE);
                    break;
                default:
                    VibrationManager.vibrate(this.mActivity, VibrationManager.VibrationPattern.EFFECT_STANDARD);
                    break;
            }
            ResearchUtil.getInstance().sendPredictiveLaunchEvent(predictiveLaunchHideTrigger.mAction);
            getBaseLayout().hidePredictiveLaunchCover(new Animatable2.AnimationCallback() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.31
                @Override // android.graphics.drawable.Animatable2.AnimationCallback
                public void onAnimationEnd(Drawable drawable) {
                    ViewFinderImpl.this.mBaseLayout.releasePredictiveLaunchCover();
                    if (ViewFinderImpl.this.isZooming() || ViewFinderImpl.this.isFocusing()) {
                        return;
                    }
                    ViewFinderImpl.this.mBaseLayout.setViewFinderGestureDetectorEnabled(true, true);
                }
            });
            setApplicationNavigatorEnabled(true);
            updateGridLineView();
            changeToPhotoReadyView(true);
            this.mActivity.setupAutoPowerOffTimeOutDuration(false);
            this.mActivity.restartAutoPowerOffTimer();
            if (this.mLoopsManager != null && this.mLoopsManager.isConnected()) {
                this.mLoopsManager.disconnect();
            }
            this.mLoopsManager = null;
        }
    }

    /* renamed from: com.sonyericsson.android.camera.view.ViewFinderImpl$32, reason: invalid class name */
    static /* synthetic */ class AnonymousClass32 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation;
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId;
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode;

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinderImpl$PredictiveLaunchHideTrigger[PredictiveLaunchHideTrigger.HW_CAMERA_KEY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinderImpl$PredictiveLaunchHideTrigger[PredictiveLaunchHideTrigger.SIDE_SENSING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinderImpl$PredictiveLaunchHideTrigger[PredictiveLaunchHideTrigger.VOLUME_KEY_SHUTTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinderImpl$PredictiveLaunchHideTrigger[PredictiveLaunchHideTrigger.TOUCH_UP_CAPTURE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType = new int[SavingTaskManager.SavedFileType.values().length];
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[SavingTaskManager.SavedFileType.BURST.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[SavingTaskManager.SavedFileType.PHOTO_DURING_REC.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[SavingTaskManager.SavedFileType.PHOTO.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[SavingTaskManager.SavedFileType.VIDEO.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[SavingTaskManager.SavedFileType.TIME_SHIFT.ordinal()] = 5;
            } catch (NoSuchFieldError unused9) {
            }
            $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageState = new int[Storage.StorageState.values().length];
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageState[Storage.StorageState.AVAILABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageState[Storage.StorageState.AVAILABLE_NEAR_FULL.ordinal()] = 2;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageState[Storage.StorageState.FULL.ordinal()] = 3;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageState[Storage.StorageState.UNAVAILABLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageState[Storage.StorageState.REMOVED.ordinal()] = 5;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageState[Storage.StorageState.READ_ONLY.ordinal()] = 6;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageState[Storage.StorageState.CORRUPT.ordinal()] = 7;
            } catch (NoSuchFieldError unused16) {
            }
            $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageType = new int[Storage.StorageType.values().length];
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageType[Storage.StorageType.EXTERNAL_CARD.ordinal()] = 1;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageType[Storage.StorageType.INTERNAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused18) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType = new int[TutorialController.TutorialType.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.VIDEO_FUSION.ordinal()] = 1;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.MANUAL_FUSION.ordinal()] = 2;
            } catch (NoSuchFieldError unused20) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId = new int[DialogId.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_SD.ordinal()] = 3;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD.ordinal()] = 4;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU.ordinal()] = 5;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.THERMAL_NOTE.ordinal()] = 6;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.LOCATION_SERVICE_DISABLE_ON_LAUNCH.ordinal()] = 7;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS.ordinal()] = 8;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.HIGH_SPEED_SD_RECOMMENDATION_ON_MODE_CHANGE.ordinal()] = 9;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP.ordinal()] = 10;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_VIDEOSIZE_CHANGE.ordinal()] = 11;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.SIDE_SENSE_DISABLE_ON_CONTEXTUAL_SETTINGS.ordinal()] = 12;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.RESET_CONFIRMATION.ordinal()] = 13;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.REQUEST_SD_CARD_PERMISSION.ordinal()] = 14;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.SD_CARD_PERMISSION_UNAVAILABLE.ordinal()] = 15;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION.ordinal()] = 16;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.ERROR_UNKNOWN.ordinal()] = 17;
            } catch (NoSuchFieldError unused37) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.ERROR_USE_OF_CAMERA_RESTRICTED.ordinal()] = 18;
            } catch (NoSuchFieldError unused38) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO.ordinal()] = 19;
            } catch (NoSuchFieldError unused39) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.COULD_NOT_SAVE_PHOTO.ordinal()] = 20;
            } catch (NoSuchFieldError unused40) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.COULD_NOT_START_RECORDING.ordinal()] = 21;
            } catch (NoSuchFieldError unused41) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.THERMAL_CRITICAL.ordinal()] = 22;
            } catch (NoSuchFieldError unused42) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.LOW_BATTERY_CRITICAL_ON_RECORDING.ordinal()] = 23;
            } catch (NoSuchFieldError unused43) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.LOW_BATTERY_CRITICAL_ON_PHOTO.ordinal()] = 24;
            } catch (NoSuchFieldError unused44) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT.ordinal()] = 25;
            } catch (NoSuchFieldError unused45) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MEMORY_FULL.ordinal()] = 26;
            } catch (NoSuchFieldError unused46) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MEMORY_SD_UNAVAILABLE.ordinal()] = 27;
            } catch (NoSuchFieldError unused47) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MEMORY_INTERNAL_UNAVAILABLE.ordinal()] = 28;
            } catch (NoSuchFieldError unused48) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MAX_FILESIZE_REACHED.ordinal()] = 29;
            } catch (NoSuchFieldError unused49) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.MAX_DURATION_REACHED.ordinal()] = 30;
            } catch (NoSuchFieldError unused50) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$messagedialog$DialogId[DialogId.PREDICTIVE_LAUNCH_DESCRIPTION.ordinal()] = 31;
            } catch (NoSuchFieldError unused51) {
            }
            $SwitchMap$com$sonyericsson$android$camera$NavigatorContents = new int[NavigatorContents.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$NavigatorContents[NavigatorContents.SUPERIOR_AUTO.ordinal()] = 1;
            } catch (NoSuchFieldError unused52) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$NavigatorContents[NavigatorContents.VIDEO.ordinal()] = 2;
            } catch (NoSuchFieldError unused53) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$AutoReview = new int[AutoReview.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$AutoReview[AutoReview.ALWAYS.ordinal()] = 1;
            } catch (NoSuchFieldError unused54) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$AutoReview[AutoReview.FRONT_ONLY.ordinal()] = 2;
            } catch (NoSuchFieldError unused55) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$AutoReview[AutoReview.OFF.ordinal()] = 3;
            } catch (NoSuchFieldError unused56) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$BurstRejectedReason = new int[ViewFinder.BurstRejectedReason.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$BurstRejectedReason[ViewFinder.BurstRejectedReason.CANNOT_BURST_IN_DARK_CONDITION.ordinal()] = 1;
            } catch (NoSuchFieldError unused57) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$BurstRejectedReason[ViewFinder.BurstRejectedReason.CANNOT_BURST_USING_FRONT_CAMERA.ordinal()] = 2;
            } catch (NoSuchFieldError unused58) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$BurstRejectedReason[ViewFinder.BurstRejectedReason.CANNOT_BURST_DUE_TO_FUSION_MODE.ordinal()] = 3;
            } catch (NoSuchFieldError unused59) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$BurstRejectedReason[ViewFinder.BurstRejectedReason.BURST_IS_DISABLED_BY_CAMERA_KEY_ASSIGN_SETTING.ordinal()] = 4;
            } catch (NoSuchFieldError unused60) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent = new int[ViewFinder.ViewUpdateEvent.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY.ordinal()] = 1;
            } catch (NoSuchFieldError unused61) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_RESIZE_EVF_SCOPE.ordinal()] = 2;
            } catch (NoSuchFieldError unused62) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_PREPARE_RECORDING_INDICATOR.ordinal()] = 3;
            } catch (NoSuchFieldError unused63) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGED.ordinal()] = 4;
            } catch (NoSuchFieldError unused64) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_DETECTED_SCENE_CHANGED.ordinal()] = 5;
            } catch (NoSuchFieldError unused65) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTION_STARTED.ordinal()] = 6;
            } catch (NoSuchFieldError unused66) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED.ordinal()] = 7;
            } catch (NoSuchFieldError unused67) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_STARTED.ordinal()] = 8;
            } catch (NoSuchFieldError unused68) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_TIMEOUT.ordinal()] = 9;
            } catch (NoSuchFieldError unused69) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_STOP.ordinal()] = 10;
            } catch (NoSuchFieldError unused70) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED.ordinal()] = 11;
            } catch (NoSuchFieldError unused71) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_START.ordinal()] = 12;
            } catch (NoSuchFieldError unused72) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_STOP.ordinal()] = 13;
            } catch (NoSuchFieldError unused73) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED.ordinal()] = 14;
            } catch (NoSuchFieldError unused74) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_SELFTIMER_FINISHED.ordinal()] = 15;
            } catch (NoSuchFieldError unused75) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_SELECTED.ordinal()] = 16;
            } catch (NoSuchFieldError unused76) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED.ordinal()] = 17;
            } catch (NoSuchFieldError unused77) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE.ordinal()] = 18;
            } catch (NoSuchFieldError unused78) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_TOUCH_FOCUS.ordinal()] = 19;
            } catch (NoSuchFieldError unused79) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_BY_SELECT_FACE.ordinal()] = 20;
            } catch (NoSuchFieldError unused80) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_RECORDING_PROGRESS.ordinal()] = 21;
            } catch (NoSuchFieldError unused81) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED.ordinal()] = 22;
            } catch (NoSuchFieldError unused82) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS.ordinal()] = 23;
            } catch (NoSuchFieldError unused83) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_CLOSE_ALL_DIALOGS.ordinal()] = 24;
            } catch (NoSuchFieldError unused84) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_INSTANT_VIEWER.ordinal()] = 25;
            } catch (NoSuchFieldError unused85) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_STORE_COMPLETED.ordinal()] = 26;
            } catch (NoSuchFieldError unused86) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION.ordinal()] = 27;
            } catch (NoSuchFieldError unused87) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_LAZY_INITIALIZATION_TASK_RUN.ordinal()] = 28;
            } catch (NoSuchFieldError unused88) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_ADD_VIDEO_CHAPTER.ordinal()] = 29;
            } catch (NoSuchFieldError unused89) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_NORMAL.ordinal()] = 30;
            } catch (NoSuchFieldError unused90) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_WARNING.ordinal()] = 31;
            } catch (NoSuchFieldError unused91) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_CRITICAL.ordinal()] = 32;
            } catch (NoSuchFieldError unused92) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_RESTORE_NAVIGATION_BAR_PREVIOUS_VISIBILITY.ordinal()] = 33;
            } catch (NoSuchFieldError unused93) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_GRID_LINE.ordinal()] = 34;
            } catch (NoSuchFieldError unused94) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ANGLE_CHANGE_START.ordinal()] = 35;
            } catch (NoSuchFieldError unused95) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ANGLE_CHANGE_COMPLETED.ordinal()] = 36;
            } catch (NoSuchFieldError unused96) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_REJECTED.ordinal()] = 37;
            } catch (NoSuchFieldError unused97) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_SHUTTER_DONE.ordinal()] = 38;
            } catch (NoSuchFieldError unused98) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_FINISH.ordinal()] = 39;
            } catch (NoSuchFieldError unused99) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH.ordinal()] = 40;
            } catch (NoSuchFieldError unused100) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURE_CANCEL.ordinal()] = 41;
            } catch (NoSuchFieldError unused101) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_MAX_DURATION_REACHED.ordinal()] = 42;
            } catch (NoSuchFieldError unused102) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_MAX_FILESIZE_REACHED.ordinal()] = 43;
            } catch (NoSuchFieldError unused103) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL.ordinal()] = 44;
            } catch (NoSuchFieldError unused104) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_UPDATE_FUSION_MODE.ordinal()] = 45;
            } catch (NoSuchFieldError unused105) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_ISO_CHANGED_BY_FUSION.ordinal()] = 46;
            } catch (NoSuchFieldError unused106) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_SHOW_BLACK_SCREEN.ordinal()] = 47;
            } catch (NoSuchFieldError unused107) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_HIDE_BLACK_SCREEN.ordinal()] = 48;
            } catch (NoSuchFieldError unused108) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION.ordinal()] = 49;
            } catch (NoSuchFieldError unused109) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_VIDEO_HDR_CONDITION.ordinal()] = 50;
            } catch (NoSuchFieldError unused110) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGING.ordinal()] = 51;
            } catch (NoSuchFieldError unused111) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY.ordinal()] = 52;
            } catch (NoSuchFieldError unused112) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE.ordinal()] = 53;
            } catch (NoSuchFieldError unused113) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT.ordinal()] = 54;
            } catch (NoSuchFieldError unused114) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_APPS_UI_MODE_FINISH.ordinal()] = 55;
            } catch (NoSuchFieldError unused115) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$ViewUpdateEvent[ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_UNLOCK_SCREEN_DIALOG.ordinal()] = 56;
            } catch (NoSuchFieldError unused116) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind = new int[ViewFinder.UiComponentKind.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.NOTICE_DIALOG.ordinal()] = 1;
            } catch (NoSuchFieldError unused117) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.FLASH_DIALOG.ordinal()] = 2;
            } catch (NoSuchFieldError unused118) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.SELF_TIMER_DIALOG.ordinal()] = 3;
            } catch (NoSuchFieldError unused119) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.ASPECT_RATIO_DIALOG.ordinal()] = 4;
            } catch (NoSuchFieldError unused120) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.FUSION_MODE_DIALOG.ordinal()] = 5;
            } catch (NoSuchFieldError unused121) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.VIDEO_HDR_DIALOG.ordinal()] = 6;
            } catch (NoSuchFieldError unused122) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.HDR_DIALOG.ordinal()] = 7;
            } catch (NoSuchFieldError unused123) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.SETTING_DIALOG.ordinal()] = 8;
            } catch (NoSuchFieldError unused124) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.MODE_SELECTOR.ordinal()] = 9;
            } catch (NoSuchFieldError unused125) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.REVIEW_WINDOW.ordinal()] = 10;
            } catch (NoSuchFieldError unused126) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.OVERLAY_CONTROL_SEEKING.ordinal()] = 11;
            } catch (NoSuchFieldError unused127) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$UiComponentKind[ViewFinder.UiComponentKind.TUTORIAL.ordinal()] = 12;
            } catch (NoSuchFieldError unused128) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern = new int[BaseLayoutPattern.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[BaseLayoutPattern.RECORDING.ordinal()] = 1;
            } catch (NoSuchFieldError unused129) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[BaseLayoutPattern.ZOOMING_IN_RECORDING.ordinal()] = 2;
            } catch (NoSuchFieldError unused130) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[BaseLayoutPattern.PAUSE_RECORDING.ordinal()] = 3;
            } catch (NoSuchFieldError unused131) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING.ordinal()] = 4;
            } catch (NoSuchFieldError unused132) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[BaseLayoutPattern.ZOOMING.ordinal()] = 5;
            } catch (NoSuchFieldError unused133) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[BaseLayoutPattern.FOCUS_SEARCHING.ordinal()] = 6;
            } catch (NoSuchFieldError unused134) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$baselayout$BaseLayoutPattern[BaseLayoutPattern.FOCUS_DONE.ordinal()] = 7;
            } catch (NoSuchFieldError unused135) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$TouchCapture = new int[TouchCapture.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$TouchCapture[TouchCapture.ON.ordinal()] = 1;
            } catch (NoSuchFieldError unused136) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$TouchCapture[TouchCapture.FRONT_ONLY.ordinal()] = 2;
            } catch (NoSuchFieldError unused137) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree = new int[AnimationRequest.AnimationDegree.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[AnimationRequest.AnimationDegree.START.ordinal()] = 1;
            } catch (NoSuchFieldError unused138) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[AnimationRequest.AnimationDegree.EXEC.ordinal()] = 2;
            } catch (NoSuchFieldError unused139) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[AnimationRequest.AnimationDegree.CANCEL.ordinal()] = 3;
            } catch (NoSuchFieldError unused140) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[AnimationRequest.AnimationDegree.FINISH.ordinal()] = 4;
            } catch (NoSuchFieldError unused141) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationType = new int[AnimationRequest.AnimationType.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationType[AnimationRequest.AnimationType.MODE_TOUCH.ordinal()] = 1;
            } catch (NoSuchFieldError unused142) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationType[AnimationRequest.AnimationType.MODE_ICON.ordinal()] = 2;
            } catch (NoSuchFieldError unused143) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationType[AnimationRequest.AnimationType.MODE_SELECTOR.ordinal()] = 3;
            } catch (NoSuchFieldError unused144) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationType[AnimationRequest.AnimationType.MRU_SHORTCUT.ordinal()] = 4;
            } catch (NoSuchFieldError unused145) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationType[AnimationRequest.AnimationType.SWITCH_TOUCH.ordinal()] = 5;
            } catch (NoSuchFieldError unused146) {
            }
            $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState = new int[StateMachine.CaptureState.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_NONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused147) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_INITIALIZE.ordinal()] = 2;
            } catch (NoSuchFieldError unused148) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_RESUME.ordinal()] = 3;
            } catch (NoSuchFieldError unused149) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_PHOTO_READY.ordinal()] = 4;
            } catch (NoSuchFieldError unused150) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_VIDEO_READY.ordinal()] = 5;
            } catch (NoSuchFieldError unused151) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_CAPTURE_COUNTDOWN.ordinal()] = 6;
            } catch (NoSuchFieldError unused152) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_OPERATION_RESTRICTED.ordinal()] = 7;
            } catch (NoSuchFieldError unused153) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_PHOTO_AF_SEARCH.ordinal()] = 8;
            } catch (NoSuchFieldError unused154) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_PHOTO_AF_DONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused155) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE.ordinal()] = 10;
            } catch (NoSuchFieldError unused156) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_BURST_CAPTURE_WAIT_FOR_AF_DONE.ordinal()] = 11;
            } catch (NoSuchFieldError unused157) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_PHOTO_CAPTURE.ordinal()] = 12;
            } catch (NoSuchFieldError unused158) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_BURST_CAPTURE.ordinal()] = 13;
            } catch (NoSuchFieldError unused159) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_VIDEO_RECORDING.ordinal()] = 14;
            } catch (NoSuchFieldError unused160) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_VIDEO_CAPTURE_WHILE_RECORDING.ordinal()] = 15;
            } catch (NoSuchFieldError unused161) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_VIDEO_STORE_PHOTO_WHILE_RECORDING.ordinal()] = 16;
            } catch (NoSuchFieldError unused162) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_VIDEO_STORE.ordinal()] = 17;
            } catch (NoSuchFieldError unused163) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_FATAL.ordinal()] = 18;
            } catch (NoSuchFieldError unused164) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_PAUSE.ordinal()] = 19;
            } catch (NoSuchFieldError unused165) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_WARNING.ordinal()] = 20;
            } catch (NoSuchFieldError unused166) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_FINALIZE.ordinal()] = 21;
            } catch (NoSuchFieldError unused167) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_VIDEO_RECORDING_PAUSING.ordinal()] = 22;
            } catch (NoSuchFieldError unused168) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_CAMERA_SWITCHING.ordinal()] = 23;
            } catch (NoSuchFieldError unused169) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_PHOTO_READY_FOR_RECORDING.ordinal()] = 24;
            } catch (NoSuchFieldError unused170) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_MODE_CHANGING.ordinal()] = 25;
            } catch (NoSuchFieldError unused171) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_WAITING_EVF_PREPARED_IN_MODE_CHANGE.ordinal()] = 26;
            } catch (NoSuchFieldError unused172) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_HIGH_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION.ordinal()] = 27;
            } catch (NoSuchFieldError unused173) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_LOW_FRAME_RATE_VIDEO_RECORDING_IN_SUPER_SLOW_MOTION.ordinal()] = 28;
            } catch (NoSuchFieldError unused174) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$CaptureState[StateMachine.CaptureState.STATE_WAIT_FOR_HIGH_FRAME_RATE_VIDEO_RECORDING_DONE.ordinal()] = 29;
            } catch (NoSuchFieldError unused175) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion = new int[SlowMotion.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[SlowMotion.SUPER_SLOW_MOTION.ordinal()] = 1;
            } catch (NoSuchFieldError unused176) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[SlowMotion.SUPER_SLOW_SHOT.ordinal()] = 2;
            } catch (NoSuchFieldError unused177) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[SlowMotion.STANDARD_SLOW_MOTION.ordinal()] = 3;
            } catch (NoSuchFieldError unused178) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer = new int[SelfTimer.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[SelfTimer.LONG.ordinal()] = 1;
            } catch (NoSuchFieldError unused179) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[SelfTimer.GESTURE_SHUTTER_COUNT_DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError unused180) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[SelfTimer.SHORT.ordinal()] = 3;
            } catch (NoSuchFieldError unused181) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[SelfTimer.LAUNCH_AND_CAPTURE_COUNT_DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError unused182) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[SelfTimer.OFF.ordinal()] = 5;
            } catch (NoSuchFieldError unused183) {
            }
            $SwitchMap$com$sonyericsson$android$camera$controller$AbstractDraggingEventHandler$Direction = new int[AbstractDraggingEventHandler.Direction.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$AbstractDraggingEventHandler$Direction[AbstractDraggingEventHandler.Direction.UP.ordinal()] = 1;
            } catch (NoSuchFieldError unused184) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$AbstractDraggingEventHandler$Direction[AbstractDraggingEventHandler.Direction.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError unused185) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$AbstractDraggingEventHandler$Direction[AbstractDraggingEventHandler.Direction.LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError unused186) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$controller$AbstractDraggingEventHandler$Direction[AbstractDraggingEventHandler.Direction.RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError unused187) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey = new int[UserSettingKey.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.SIDE_SENSE.ordinal()] = 1;
            } catch (NoSuchFieldError unused188) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.GEO_TAG.ordinal()] = 2;
            } catch (NoSuchFieldError unused189) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.DESTINATION_TO_SAVE.ordinal()] = 3;
            } catch (NoSuchFieldError unused190) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.HELP_GUIDE.ordinal()] = 4;
            } catch (NoSuchFieldError unused191) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.RESET_SETTINGS.ordinal()] = 5;
            } catch (NoSuchFieldError unused192) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.WHITE_BALANCE.ordinal()] = 6;
            } catch (NoSuchFieldError unused193) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.EV.ordinal()] = 7;
            } catch (NoSuchFieldError unused194) {
            }
            $SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation = new int[LaunchCondition.ExtraOperation.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$LaunchCondition$ExtraOperation[LaunchCondition.ExtraOperation.OPEN_SETTINGS_MENU.ordinal()] = 1;
            } catch (NoSuchFieldError unused195) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState = new int[ViewFinder.HeadUpDisplaySetupState.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.PHOTO_READY.ordinal()] = 1;
            } catch (NoSuchFieldError unused196) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.PHOTO_CAPTURE.ordinal()] = 2;
            } catch (NoSuchFieldError unused197) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.PHOTO_BURST_CAPTURE.ordinal()] = 3;
            } catch (NoSuchFieldError unused198) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.VIDEO_READY.ordinal()] = 4;
            } catch (NoSuchFieldError unused199) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING.ordinal()] = 5;
            } catch (NoSuchFieldError unused200) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY.ordinal()] = 6;
            } catch (NoSuchFieldError unused201) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_MOTION_RECORDING.ordinal()] = 7;
            } catch (NoSuchFieldError unused202) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY.ordinal()] = 8;
            } catch (NoSuchFieldError unused203) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_RECORDING.ordinal()] = 9;
            } catch (NoSuchFieldError unused204) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY.ordinal()] = 10;
            } catch (NoSuchFieldError unused205) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$ViewFinder$HeadUpDisplaySetupState[ViewFinder.HeadUpDisplaySetupState.VIDEO_PAUSING.ordinal()] = 11;
            } catch (NoSuchFieldError unused206) {
            }
            $SwitchMap$com$sonyericsson$android$camera$SideTouchEventDetector$SideTouchArea = new int[SideTouchEventDetector.SideTouchArea.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$SideTouchEventDetector$SideTouchArea[SideTouchEventDetector.SideTouchArea.TOP.ordinal()] = 1;
            } catch (NoSuchFieldError unused207) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$SideTouchEventDetector$SideTouchArea[SideTouchEventDetector.SideTouchArea.BOTTOM.ordinal()] = 2;
            } catch (NoSuchFieldError unused208) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$SideTouchEventDetector$SideTouchArea[SideTouchEventDetector.SideTouchArea.LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError unused209) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$SideTouchEventDetector$SideTouchArea[SideTouchEventDetector.SideTouchArea.RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError unused210) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode = new int[CapturingMode.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SCENE_RECOGNITION.ordinal()] = 1;
            } catch (NoSuchFieldError unused211) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SUPERIOR_FRONT.ordinal()] = 2;
            } catch (NoSuchFieldError unused212) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.VIDEO.ordinal()] = 3;
            } catch (NoSuchFieldError unused213) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.FRONT_VIDEO.ordinal()] = 4;
            } catch (NoSuchFieldError unused214) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.NORMAL.ordinal()] = 5;
            } catch (NoSuchFieldError unused215) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.FRONT_PHOTO.ordinal()] = 6;
            } catch (NoSuchFieldError unused216) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SLOW_MOTION.ordinal()] = 7;
            } catch (NoSuchFieldError unused217) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode = new int[ModeSelectorInternalMode.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[ModeSelectorInternalMode.PORTRAIT_SELFIE.ordinal()] = 1;
            } catch (NoSuchFieldError unused218) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[ModeSelectorInternalMode.DUAL_BACKGROUND_DEFOCUS.ordinal()] = 2;
            } catch (NoSuchFieldError unused219) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[ModeSelectorInternalMode.DUAL_MONOCHROME.ordinal()] = 3;
            } catch (NoSuchFieldError unused220) {
            }
        }
    }

    private class PredictiveLaunchCoverTouchListenerImpl implements PredictiveLaunchCoverView.PredictiveLaunchCoverTouchListener {
        private PredictiveLaunchCoverTouchListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.PredictiveLaunchCoverView.PredictiveLaunchCoverTouchListener
        public void onCircleTouched() {
            ViewFinderImpl.this.mTouchEventDispatcher.sendTouchUp(UserEventHandler.UiComponent.PREDICTIVE_LAUNCH_COVER, null);
        }
    }

    public static class RecordingTimeReceiverProxy {
        private int mCurrentTime;
        private RecordingTimeIndicator mReceiver;

        public void bindReceiver(RecordingTimeIndicator recordingTimeIndicator) {
            this.mReceiver = recordingTimeIndicator;
        }

        public int getCurrentTime() {
            return this.mCurrentTime;
        }

        protected void reset() {
            this.mCurrentTime = 0;
        }

        protected void notifyOnTimeTicked(int i) {
            this.mCurrentTime = i;
            if (this.mReceiver == null) {
                return;
            }
            this.mReceiver.onTimeTicked(i);
        }
    }

    public static class ZoomBarUpdateProxy {
        private Zoombar mZoomBar;

        public void bindZoomBar(Zoombar zoombar) {
            this.mZoomBar = zoombar;
        }

        protected int update(List<Integer> list, int i) {
            this.mZoomBar.setZoomRatios(list);
            return this.mZoomBar.zoom(i);
        }
    }

    public static class AutoReviewContentReceiverProxy {
        private AutoReviewContent.ContentReceiver mReceiver;

        public void bindReceiver(AutoReviewContent.ContentReceiver contentReceiver) {
            this.mReceiver = contentReceiver;
        }

        protected void notifyContent(AutoReviewContent autoReviewContent) {
            this.mReceiver.onReceive(autoReviewContent);
        }
    }

    public class ViewFinderAccessorForShortcut {
        public ViewFinderAccessorForShortcut() {
        }

        public boolean isShortcutButtonClickable() {
            return ViewFinderImpl.this.mIsSettingChangeAcceptable && !ViewFinderImpl.this.isTutorialOpened() && ViewFinderImpl.this.isUserOperable();
        }

        public void switchCamera() {
            ViewFinderImpl.this.setFrontAngleSwitchButtonClickable(false);
            ViewFinderImpl.this.hideAutoReview();
            ViewFinderImpl.this.setIsCameraSwitching(true);
            ViewFinderImpl.this.hideSurface();
            CameraApplication.getUiThreadHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.view.ViewFinderImpl.ViewFinderAccessorForShortcut.1
                @Override // java.lang.Runnable
                public void run() {
                    ViewFinderImpl.this.onToggleCameraSwitch();
                    ViewFinderImpl.this.setFrontAngleSwitchButtonClickable(ViewFinderImpl.this.isFront());
                }
            });
        }

        public void openShorcutDialog(ViewFinder.UiComponentKind uiComponentKind) {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_OPENED, uiComponentKind);
        }

        public void openSettingMenuDialog() {
            ViewFinderImpl.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_MENU, new Object[0]);
        }

        public void showRestrictMessageDialog(UserSettingKey userSettingKey) {
            ViewFinderImpl.this.showMessageDialog(userSettingKey.getRestrictMessageDialogId(ViewFinderImpl.this.mStateMachine.getUserSetting()), new Object[0]);
        }

        public void switchSemiAutoAvailability() throws Resources.NotFoundException {
            ViewFinderImpl.this.switchSemiAutoAvailability();
        }
    }

    public void prepareSelfTimerAndTouchCapture() {
        UserSettings userSetting = this.mStateMachine.getUserSetting();
        this.mPhotoSelfTimerSetting = (SelfTimer) userSetting.get(getCapturingMode(), UserSettingKey.SELF_TIMER);
        this.mTouchCapture = (TouchCapture) userSetting.get(getCapturingMode(), UserSettingKey.TOUCH_CAPTURE);
    }

    private void clearTouchCapture() {
        this.mTouchCapture = null;
    }
}
