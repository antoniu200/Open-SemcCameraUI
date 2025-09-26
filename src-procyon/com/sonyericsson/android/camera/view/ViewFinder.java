// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import android.view.LayoutInflater;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.cameracommon.storage.Storage;
import android.graphics.Rect;
import android.graphics.Point;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;

public interface ViewFinder
{
    void attachToWindow();
    
    void cancelPredictiveCaptureIndicatorAnimation();
    
    void clearHintText();
    
    void clearMessageDialog();
    
    void commit();
    
    int getAutoPowerOffHintTextTimeOutDuration();
    
    int getOrientation();
    
    SelfTimer getPhotoSelfTimerSetting();
    
    Rect getPosition(final Point p0);
    
    int getRequestId(final boolean p0);
    
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
    
    void notifyStorageStateChanged(final Storage.StorageType p0, final Storage.StorageState p1, final boolean p2, final boolean p3);
    
    void notifyZoomOperationRejected();
    
    void onCaptureDone();
    
    void onNotifyCoolingUltraLow(final boolean p0);
    
    void onObjectLost();
    
    void onSettingChanged(final UserSettingValue p0);
    
    void onShutterDone(final boolean p0);
    
    void postSlowMotionHintText();
    
    void prepareGestureShutterCountDown();
    
    void requestCheckEvfPreparationRetrying();
    
    void requestInflate(final LayoutInflater p0);
    
    void sendViewUpdateEvent(final ViewUpdateEvent p0, final Object... p1);
    
    void setCameraDevice(final CameraDeviceHandler p0);
    
    void setContentView();
    
    void setDisplayFlashColor(final int p0, final int p1, final int p2);
    
    void setDisplayFlashRequired(final boolean p0);
    
    void setIsCameraSwitching(final boolean p0);
    
    void setRecordingOrientation(final int p0);
    
    void setSelfTimer(final CapturingMode p0, final SelfTimer p1);
    
    void setShutterTrigger(final ShutterTrigger p0);
    
    void setStartDraggingSlopEnabled(final boolean p0);
    
    void setStateMachine(final StateMachine p0);
    
    void setupFocusRectangles();
    
    void showAutoPowerOffHintText();
    
    void showBlank();
    
    void showDisplayFlashScreen();
    
    void showHiSpeedSdCardRecommendDialogOnDestinationChange();
    
    void showMessageDialog(final DialogId p0, final Object... p1);
    
    void showSavingProgressBar();
    
    void showSurface();
    
    void showViews();
    
    void startHideThumbnail();
    
    void startPredictiveCaptureIndicatorAnimation();
    
    void startSlowMotionFeedbackAnimation();
    
    void updateBatteryIndicator(final int p0);
    
    void updateCaptureAreaSize();
    
    void updateFocusIconType(final boolean p0);
    
    void updateSlowMotionView(final SlowMotion p0);
    
    void updateVideoShutterTrigger();
    
    public enum BurstRejectedReason
    {
        private static final BurstRejectedReason[] $VALUES;
        
        BURST_IS_DISABLED_BY_CAMERA_KEY_ASSIGN_SETTING, 
        CANNOT_BURST_DUE_TO_FUSION_MODE, 
        CANNOT_BURST_IN_DARK_CONDITION, 
        CANNOT_BURST_USING_FRONT_CAMERA, 
        NONE;
        
        static {
            $VALUES = new BurstRejectedReason[] { BurstRejectedReason.NONE, BurstRejectedReason.CANNOT_BURST_IN_DARK_CONDITION, BurstRejectedReason.CANNOT_BURST_USING_FRONT_CAMERA, BurstRejectedReason.CANNOT_BURST_DUE_TO_FUSION_MODE, BurstRejectedReason.BURST_IS_DISABLED_BY_CAMERA_KEY_ASSIGN_SETTING };
        }
    }
    
    public enum HeadUpDisplaySetupState
    {
        private static final HeadUpDisplaySetupState[] $VALUES;
        
        PHOTO_BURST_CAPTURE, 
        PHOTO_CAPTURE, 
        PHOTO_READY, 
        STANDARD_SLOW_MOTION_RECORDING, 
        STANDARD_SLOW_MOTION_STANDBY, 
        SUPER_SLOW_MOTION_RECORDING, 
        SUPER_SLOW_MOTION_STANDBY, 
        SUPER_SLOW_SHOT_STANDBY, 
        VIDEO_PAUSING, 
        VIDEO_READY, 
        VIDEO_RECORDING;
        
        static {
            $VALUES = new HeadUpDisplaySetupState[] { HeadUpDisplaySetupState.PHOTO_READY, HeadUpDisplaySetupState.PHOTO_CAPTURE, HeadUpDisplaySetupState.PHOTO_BURST_CAPTURE, HeadUpDisplaySetupState.VIDEO_READY, HeadUpDisplaySetupState.VIDEO_RECORDING, HeadUpDisplaySetupState.VIDEO_PAUSING, HeadUpDisplaySetupState.SUPER_SLOW_MOTION_STANDBY, HeadUpDisplaySetupState.SUPER_SLOW_MOTION_RECORDING, HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_STANDBY, HeadUpDisplaySetupState.STANDARD_SLOW_MOTION_RECORDING, HeadUpDisplaySetupState.SUPER_SLOW_SHOT_STANDBY };
        }
    }
    
    public enum UiComponentKind
    {
        private static final UiComponentKind[] $VALUES;
        
        ASPECT_RATIO_DIALOG, 
        FATAL_ALERT_DIALOG, 
        FLASH_DIALOG, 
        FUSION_MODE_DIALOG, 
        HDR_DIALOG, 
        MODE_SELECTOR, 
        NOTICE_DIALOG, 
        OVERLAY_CONTROL_SEEKING, 
        REVIEW_WINDOW, 
        SELF_TIMER_DIALOG, 
        SETTING_DIALOG, 
        TUTORIAL, 
        VIDEO_HDR_DIALOG, 
        ZOOM_BAR;
        
        static {
            $VALUES = new UiComponentKind[] { UiComponentKind.ZOOM_BAR, UiComponentKind.SETTING_DIALOG, UiComponentKind.REVIEW_WINDOW, UiComponentKind.OVERLAY_CONTROL_SEEKING, UiComponentKind.FLASH_DIALOG, UiComponentKind.TUTORIAL, UiComponentKind.NOTICE_DIALOG, UiComponentKind.FATAL_ALERT_DIALOG, UiComponentKind.MODE_SELECTOR, UiComponentKind.SELF_TIMER_DIALOG, UiComponentKind.ASPECT_RATIO_DIALOG, UiComponentKind.FUSION_MODE_DIALOG, UiComponentKind.VIDEO_HDR_DIALOG, UiComponentKind.HDR_DIALOG };
        }
    }
    
    public enum ViewUpdateEvent
    {
        private static final ViewUpdateEvent[] $VALUES;
        
        EVENT_ANGLE_CHANGE_COMPLETED, 
        EVENT_ANGLE_CHANGE_START, 
        EVENT_APPS_UI_MODE_FINISH, 
        EVENT_CLOSE_ALL_DIALOGS, 
        EVENT_HIDE_BLACK_SCREEN, 
        EVENT_ON_ADD_VIDEO_CHAPTER, 
        EVENT_ON_BURST_FINISH, 
        EVENT_ON_BURST_REJECTED, 
        EVENT_ON_BURST_SHUTTER_DONE, 
        EVENT_ON_CAPTURE_CANCEL, 
        EVENT_ON_CAPTURE_FINISH, 
        EVENT_ON_CAPTURING_MODE_CHANGED, 
        EVENT_ON_CAPTURING_MODE_CHANGING, 
        EVENT_ON_DETECTED_SCENE_CHANGED, 
        EVENT_ON_FACE_DETECTED, 
        EVENT_ON_FACE_DETECTION_STARTED, 
        EVENT_ON_FOCUS_POSITION_RELEASED, 
        EVENT_ON_FOCUS_POSITION_RELEASED_BY_SELECT_FACE, 
        EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, 
        EVENT_ON_FOCUS_POSITION_RELEASED_TOUCH_FOCUS, 
        EVENT_ON_FOCUS_POSITION_SELECTED, 
        EVENT_ON_ISO_CHANGED_BY_FUSION, 
        EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, 
        EVENT_ON_NOTIFY_BATTERY_CRITICAL, 
        EVENT_ON_NOTIFY_MAX_DURATION_REACHED, 
        EVENT_ON_NOTIFY_MAX_FILESIZE_REACHED, 
        EVENT_ON_NOTIFY_RESTORE_NAVIGATION_BAR_PREVIOUS_VISIBILITY, 
        EVENT_ON_NOTIFY_THERMAL_CRITICAL, 
        EVENT_ON_NOTIFY_THERMAL_NORMAL, 
        EVENT_ON_NOTIFY_THERMAL_WARNING, 
        EVENT_ON_OBJECT_TRACKING_STARTED, 
        EVENT_ON_OBJECT_TRACKING_STOP, 
        EVENT_ON_OBJECT_TRACKING_TIMEOUT, 
        EVENT_ON_ORIENTATION_CHANGED, 
        EVENT_ON_RECORDING_PROGRESS, 
        EVENT_ON_SELFTIMER_FINISHED, 
        EVENT_ON_STORE_COMPLETED, 
        EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, 
        EVENT_ON_ZOOM_CHANGED, 
        EVENT_ON_ZOOM_START, 
        EVENT_ON_ZOOM_STOP, 
        EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION, 
        EVENT_REQUEST_PREPARE_RECORDING_INDICATOR, 
        EVENT_REQUEST_RESIZE_EVF_SCOPE, 
        EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, 
        EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, 
        EVENT_REQUEST_SHOW_INSTANT_VIEWER, 
        EVENT_REQUEST_SHOW_UNLOCK_SCREEN_DIALOG, 
        EVENT_REQUEST_UPDATE_FUSION_CONDITION, 
        EVENT_REQUEST_UPDATE_GRID_LINE, 
        EVENT_REQUEST_UPDATE_MRU_SHORTCUT, 
        EVENT_REQUEST_UPDATE_SELF_TIMER_CONDITION, 
        EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, 
        EVENT_REQUEST_UPDATE_VIDEO_HDR_CONDITION, 
        EVENT_SHOW_BLACK_SCREEN, 
        EVENT_UPDATE_DIALOGS, 
        EVENT_UPDATE_FUSION_MODE;
        
        static {
            $VALUES = new ViewUpdateEvent[] { ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, ViewUpdateEvent.EVENT_REQUEST_RESIZE_EVF_SCOPE, ViewUpdateEvent.EVENT_REQUEST_PREPARE_RECORDING_INDICATOR, ViewUpdateEvent.EVENT_ON_DETECTED_SCENE_CHANGED, ViewUpdateEvent.EVENT_ON_FACE_DETECTION_STARTED, ViewUpdateEvent.EVENT_ON_FACE_DETECTED, ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_STARTED, ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_TIMEOUT, ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_STOP, ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, ViewUpdateEvent.EVENT_ON_ZOOM_START, ViewUpdateEvent.EVENT_ON_ZOOM_STOP, ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED, ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_SELECTED, ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_TOUCH_FOCUS, ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_BY_SELECT_FACE, ViewUpdateEvent.EVENT_ON_RECORDING_PROGRESS, ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED, ViewUpdateEvent.EVENT_ON_SELFTIMER_FINISHED, ViewUpdateEvent.EVENT_UPDATE_DIALOGS, ViewUpdateEvent.EVENT_CLOSE_ALL_DIALOGS, ViewUpdateEvent.EVENT_REQUEST_SHOW_INSTANT_VIEWER, ViewUpdateEvent.EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION, ViewUpdateEvent.EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, ViewUpdateEvent.EVENT_ON_ADD_VIDEO_CHAPTER, ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_NORMAL, ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_WARNING, ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_CRITICAL, ViewUpdateEvent.EVENT_ON_NOTIFY_RESTORE_NAVIGATION_BAR_PREVIOUS_VISIBILITY, ViewUpdateEvent.EVENT_REQUEST_UPDATE_GRID_LINE, ViewUpdateEvent.EVENT_ON_STORE_COMPLETED, ViewUpdateEvent.EVENT_ANGLE_CHANGE_START, ViewUpdateEvent.EVENT_ANGLE_CHANGE_COMPLETED, ViewUpdateEvent.EVENT_ON_NOTIFY_MAX_DURATION_REACHED, ViewUpdateEvent.EVENT_ON_NOTIFY_MAX_FILESIZE_REACHED, ViewUpdateEvent.EVENT_UPDATE_FUSION_MODE, ViewUpdateEvent.EVENT_ON_ISO_CHANGED_BY_FUSION, ViewUpdateEvent.EVENT_SHOW_BLACK_SCREEN, ViewUpdateEvent.EVENT_HIDE_BLACK_SCREEN, ViewUpdateEvent.EVENT_REQUEST_UPDATE_FUSION_CONDITION, ViewUpdateEvent.EVENT_APPS_UI_MODE_FINISH, ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGING, ViewUpdateEvent.EVENT_ON_CAPTURING_MODE_CHANGED, ViewUpdateEvent.EVENT_ON_BURST_SHUTTER_DONE, ViewUpdateEvent.EVENT_ON_BURST_FINISH, ViewUpdateEvent.EVENT_ON_BURST_REJECTED, ViewUpdateEvent.EVENT_ON_CAPTURE_FINISH, ViewUpdateEvent.EVENT_ON_CAPTURE_CANCEL, ViewUpdateEvent.EVENT_ON_NOTIFY_BATTERY_CRITICAL, ViewUpdateEvent.EVENT_REQUEST_UPDATE_VIDEO_HDR_CONDITION, ViewUpdateEvent.EVENT_REQUEST_UPDATE_SELF_TIMER_CONDITION, ViewUpdateEvent.EVENT_REQUEST_UPDATE_SETTING_CHANGE_ACCEPTABILITY, ViewUpdateEvent.EVENT_REQUEST_SHOW_CHANGE_INTERNAL_STORAGE_MESSAGE, ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT, ViewUpdateEvent.EVENT_REQUEST_SHOW_UNLOCK_SCREEN_DIALOG };
        }
    }
}
