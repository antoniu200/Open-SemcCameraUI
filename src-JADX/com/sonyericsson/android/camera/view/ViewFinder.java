package com.sonyericsson.android.camera.view;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.LayoutInflater;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.cameracommon.storage.Storage;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface ViewFinder {

    public enum BurstRejectedReason {
        NONE,
        CANNOT_BURST_IN_DARK_CONDITION,
        CANNOT_BURST_USING_FRONT_CAMERA,
        CANNOT_BURST_DUE_TO_FUSION_MODE,
        BURST_IS_DISABLED_BY_CAMERA_KEY_ASSIGN_SETTING
    }

    public enum HeadUpDisplaySetupState {
        PHOTO_READY,
        PHOTO_CAPTURE,
        PHOTO_BURST_CAPTURE,
        VIDEO_READY,
        VIDEO_RECORDING,
        VIDEO_PAUSING,
        SUPER_SLOW_MOTION_STANDBY,
        SUPER_SLOW_MOTION_RECORDING,
        STANDARD_SLOW_MOTION_STANDBY,
        STANDARD_SLOW_MOTION_RECORDING,
        SUPER_SLOW_SHOT_STANDBY
    }

    public enum UiComponentKind {
        ZOOM_BAR,
        SETTING_DIALOG,
        REVIEW_WINDOW,
        OVERLAY_CONTROL_SEEKING,
        FLASH_DIALOG,
        TUTORIAL,
        NOTICE_DIALOG,
        FATAL_ALERT_DIALOG,
        MODE_SELECTOR,
        SELF_TIMER_DIALOG,
        ASPECT_RATIO_DIALOG,
        FUSION_MODE_DIALOG,
        VIDEO_HDR_DIALOG,
        HDR_DIALOG
    }

    public enum ViewUpdateEvent {
        EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY,
        EVENT_REQUEST_RESIZE_EVF_SCOPE,
        EVENT_REQUEST_PREPARE_RECORDING_INDICATOR,
        EVENT_ON_DETECTED_SCENE_CHANGED,
        EVENT_ON_FACE_DETECTION_STARTED,
        EVENT_ON_FACE_DETECTED,
        EVENT_ON_OBJECT_TRACKING_STARTED,
        EVENT_ON_OBJECT_TRACKING_TIMEOUT,
        EVENT_ON_OBJECT_TRACKING_STOP,
        EVENT_ON_TRACKED_OBJECT_STATE_UPDATED,
        EVENT_ON_ZOOM_START,
        EVENT_ON_ZOOM_STOP,
        EVENT_ON_ZOOM_CHANGED,
        EVENT_ON_FOCUS_POSITION_SELECTED,
        EVENT_ON_FOCUS_POSITION_RELEASED,
        EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE,
        EVENT_ON_FOCUS_POSITION_RELEASED_TOUCH_FOCUS,
        EVENT_ON_FOCUS_POSITION_RELEASED_BY_SELECT_FACE,
        EVENT_ON_RECORDING_PROGRESS,
        EVENT_ON_ORIENTATION_CHANGED,
        EVENT_ON_SELFTIMER_FINISHED,
        EVENT_UPDATE_DIALOGS,
        EVENT_CLOSE_ALL_DIALOGS,
        EVENT_REQUEST_SHOW_INSTANT_VIEWER,
        EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION,
        EVENT_ON_LAZY_INITIALIZATION_TASK_RUN,
        EVENT_ON_ADD_VIDEO_CHAPTER,
        EVENT_ON_NOTIFY_THERMAL_NORMAL,
        EVENT_ON_NOTIFY_THERMAL_WARNING,
        EVENT_ON_NOTIFY_THERMAL_CRITICAL,
        EVENT_ON_NOTIFY_RESTORE_NAVIGATION_BAR_PREVIOUS_VISIBILITY,
        EVENT_REQUEST_UPDATE_GRID_LINE,
        EVENT_ON_STORE_COMPLETED,
        EVENT_ANGLE_CHANGE_START,
        EVENT_ANGLE_CHANGE_COMPLETED,
        EVENT_ON_NOTIFY_MAX_DURATION_REACHED,
        EVENT_ON_NOTIFY_MAX_FILESIZE_REACHED,
        EVENT_UPDATE_FUSION_MODE,
        EVENT_ON_ISO_CHANGED_BY_FUSION,
        EVENT_SHOW_BLACK_SCREEN,
        EVENT_HIDE_BLACK_SCREEN,
        EVENT_REQUEST_UPDATE_FUSION_CONDITION,
        EVENT_APPS_UI_MODE_FINISH,
        EVENT_ON_CAPTURING_MODE_CHANGING,
        EVENT_ON_CAPTURING_MODE_CHANGED,
        EVENT_ON_BURST_SHUTTER_DONE,
        EVENT_ON_BURST_FINISH,
        EVENT_ON_BURST_REJECTED,
        EVENT_ON_CAPTURE_FINISH,
        EVENT_ON_CAPTURE_CANCEL,
        EVENT_ON_NOTIFY_BATTERY_CRITICAL,
        EVENT_REQUEST_UPDATE_VIDEO_HDR_CONDITION,
        EVENT_REQUEST_UPDATE_SELF_TIMER_CONDITION,
        EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY,
        EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE,
        EVENT_REQUEST_UPDATE_MRU_SHORTCUT,
        EVENT_REQUEST_SHOW_UNLOCK_SCREEN_DIALOG
    }

    void attachToWindow();

    void cancelPredictiveCaptureIndicatorAnimation();

    void clearHintText();

    void clearMessageDialog();

    void commit();

    int getAutoPowerOffHintTextTimeOutDuration();

    int getOrientation();

    SelfTimer getPhotoSelfTimerSetting();

    Rect getPosition(Point point);

    int getRequestId(boolean z);

    int getSelectedFaceSmileScore();

    void hideAutoPowerOffHintText();

    void hideAutoReview();

    void hideDisplayFlashScreen();

    void hideHudIcons();

    void hideSavingProgressBar();

    void hideSurface();

    void hideViews();

    boolean isAutoPowerOffWarningDisplayed();

    boolean isAutoReviewShowing();

    boolean isCameraSwitching();

    boolean isDisplayFlashScreenDisplayed();

    boolean isEvfPrepared();

    boolean isFlashAndSettingMenuOpened();

    boolean isHeadUpDisplayReady();

    boolean isMessageDialogOpened();

    boolean isSetupHeadupDisplayInvoked();

    boolean isSwitchingAnimationProgress();

    boolean isTouchFocus();

    boolean isUserOperable();

    void notifyOnEvfPrepared();

    void notifyStorageStateChanged(Storage.StorageType storageType, Storage.StorageState storageState, boolean z, boolean z2);

    void notifyZoomOperationRejected();

    void onCaptureDone();

    void onNotifyCoolingUltraLow(boolean z);

    void onObjectLost();

    void onSettingChanged(UserSettingValue userSettingValue);

    void onShutterDone(boolean z);

    void postSlowMotionHintText();

    void prepareGestureShutterCountDown();

    void requestCheckEvfPreparationRetrying();

    void requestInflate(LayoutInflater layoutInflater);

    void sendViewUpdateEvent(ViewUpdateEvent viewUpdateEvent, Object... objArr);

    void setCameraDevice(CameraDeviceHandler cameraDeviceHandler);

    void setContentView();

    void setDisplayFlashColor(int i, int i2, int i3);

    void setDisplayFlashRequired(boolean z);

    void setIsCameraSwitching(boolean z);

    void setRecordingOrientation(int i);

    void setSelfTimer(CapturingMode capturingMode, SelfTimer selfTimer);

    void setShutterTrigger(ShutterTrigger shutterTrigger);

    void setStartDraggingSlopEnabled(boolean z);

    void setStateMachine(StateMachine stateMachine);

    void setupFocusRectangles();

    void showAutoPowerOffHintText();

    void showBlank();

    void showDisplayFlashScreen();

    void showHiSpeedSdCardRecommendDialogOnDestinationChange();

    void showMessageDialog(DialogId dialogId, Object... objArr);

    void showSavingProgressBar();

    void showSurface();

    void showViews();

    void startHideThumbnail();

    void startPredictiveCaptureIndicatorAnimation();

    void startSlowMotionFeedbackAnimation();

    void updateBatteryIndicator(int i);

    void updateCaptureAreaSize();

    void updateFocusIconType(boolean z);

    void updateSlowMotionView(SlowMotion slowMotion);

    void updateVideoShutterTrigger();
}
