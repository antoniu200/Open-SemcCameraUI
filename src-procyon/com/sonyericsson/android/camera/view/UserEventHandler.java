// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonItemFactory;
import java.util.HashMap;
import java.util.Map;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.cameracommon.focusview.FocusRectangles;
import android.os.Handler;
import com.sonyericsson.android.camera.view.angle.SideTouchZoomStepCalculator;
import com.sonyericsson.android.camera.view.angle.PinchZoomStepCalculator;
import com.sonyericsson.android.camera.view.angle.KeyZoomStepCalculator;
import com.sonyericsson.android.camera.view.angle.FrontAngleChangeCalculator;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.view.angle.VariableIndex;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.CameraKey;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.graphics.Point;
import android.content.Context;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.SideTouchEventDetector;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.cameracommon.keytranslator.KeyEventTranslator;
import com.sonyericsson.android.camera.CameraActivity;

public class UserEventHandler
{
    private static final String TAG = "UserEventHandler";
    private ActionByKey mActionByKey;
    private final CameraActivity mActivity;
    private final AngleActionHandler mAngleActionHandler;
    private EventSource mHandlingEventSource;
    private boolean mIsBurstShotEnabled;
    private final KeyEventTranslator mKeyEventTranslator;
    private final MessageSettings mMessageSettings;
    private final SideTouchEventDetector mSideTouchCameraGestureDetector;
    private final StateMachine mStateMachine;
    private final Storage mStorage;
    private final TouchEventProcedureManager mTouchEventProcedures;
    private final UserSettings mUserSettings;
    private final ViewFinderImpl mViewFinder;
    
    public UserEventHandler(final CameraActivity mActivity, final ViewFinderImpl mViewFinder, final StateMachine mStateMachine, final Storage mStorage, final UserSettings mUserSettings, final MessageSettings mMessageSettings, final boolean mIsBurstShotEnabled) {
        this.mActionByKey = ActionByKey.NONE;
        this.mActivity = mActivity;
        this.mViewFinder = mViewFinder;
        this.mStateMachine = mStateMachine;
        this.mStorage = mStorage;
        this.mUserSettings = mUserSettings;
        this.mMessageSettings = mMessageSettings;
        this.mKeyEventTranslator = new KeyEventTranslator(this.mUserSettings);
        this.mTouchEventProcedures = new TouchEventProcedureManager();
        this.mHandlingEventSource = null;
        this.mIsBurstShotEnabled = mIsBurstShotEnabled;
        this.mAngleActionHandler = new AngleActionHandler();
        this.mSideTouchCameraGestureDetector = new SideTouchEventDetector((Context)mActivity, (SideTouchEventDetector.OnSideTouchGestureListener)new OnSideTouchGestureListenerImpl());
    }
    
    private boolean canObjectTracking() {
        return this.isObjectTrackingEnabled() && (!PlatformCapability.isPowerSavingSupported(this.getCurrentCapturingMode().getCameraId()) || !this.mActivity.isThermalWarningReceived()) && !this.isManualFocus();
    }
    
    private boolean canSelfTimerActivation() {
        return this.isPhotoSelfTimerEnabled() && this.isIdle();
    }
    
    private void dispatchCaptureAreaScaleReady(final TouchEventSource touchEventSource) {
        if (!this.isEventAccepted((EventSource)touchEventSource)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchCaptureAreaScaleReady() not accepted. requested:");
            sb.append(touchEventSource.toString());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return;
        }
        final TouchEventProcedure find = this.mTouchEventProcedures.find(touchEventSource);
        if (find != null) {
            find.doTouchAreaScaleReady();
        }
    }
    
    private void dispatchCaptureAreaScaling(final TouchEventSource touchEventSource, final float n) {
        if (!this.isEventAccepted((EventSource)touchEventSource)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchCaptureAreaScaling() not accepted. requested:");
            sb.append(touchEventSource.toString());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return;
        }
        final TouchEventProcedure find = this.mTouchEventProcedures.find(touchEventSource);
        if (find != null) {
            find.doTouchAreaScaling(n);
        }
    }
    
    private void dispatchClick(final TouchEventSource touchEventSource, final Point point) {
        if (!this.isEventAccepted((EventSource)touchEventSource)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchClick() not accepted. requested:");
            sb.append(touchEventSource.toString());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return;
        }
        final TouchEventProcedure find = this.mTouchEventProcedures.find(touchEventSource);
        if (find != null) {
            find.doClick(point);
        }
    }
    
    private boolean dispatchKeyDown(final KeyEvent keyEvent) {
        if (this.mViewFinder == null) {
            return true;
        }
        if (this.mViewFinder.isAutoPowerOffWarningDisplayed()) {
            return true;
        }
        if (keyEvent.getRepeatCount() > 0) {
            return this.dispatchKeyDownAfterTheSecondTime(keyEvent);
        }
        return this.dispatchKeyDownInTheFirstTime(keyEvent);
    }
    
    private boolean dispatchKeyDownAfterTheSecondTime(final KeyEvent keyEvent) {
        if (!this.isEventAccepted((EventSource)KeyEventSource.from(keyEvent.getKeyCode()))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchKeyDownAfterTheSecondTime() not accepted. requested:");
            sb.append(keyEvent.getKeyCode());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return true;
        }
        return UserEventHandler$1.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[this.mKeyEventTranslator.translateKeyCodeOnDown(keyEvent.getKeyCode()).ordinal()] != 1;
    }
    
    private boolean dispatchKeyDownInTheFirstTime(final KeyEvent keyEvent) {
        final boolean startEventHandling = this.startEventHandling((EventSource)KeyEventSource.from(keyEvent.getKeyCode()));
        boolean b = false;
        if (!startEventHandling) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchKeyDownInTheFirstTime() startEventHandling() not accepted. requested:");
            sb.append(keyEvent.getKeyCode());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return true;
        }
        switch (UserEventHandler$1.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[this.mKeyEventTranslator.translateKeyCodeOnDown(keyEvent.getKeyCode()).ordinal()]) {
            default: {
                return false;
            }
            case 8:
            case 9:
            case 10: {
                return true;
            }
            case 7: {
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.CLOSE_INITIAL_RESPONSE);
                if (CamLog.DEBUG) {
                    CamLog.d("CLOSE_INITIAL_RESPONSE : start");
                }
                return true;
            }
            case 5:
            case 6: {
                if (this.mViewFinder.isFlashAndSettingMenuOpened()) {
                    this.mActionByKey = ActionByKey.REJECT;
                    this.mViewFinder.closeDialogs();
                    return true;
                }
                final boolean predictiveLaunchCoverExists = this.mViewFinder.predictiveLaunchCoverExists();
                this.mViewFinder.hidePredictiveLaunchCover(ViewFinderImpl.PredictiveLaunchHideTrigger.VOLUME_KEY_SHUTTER);
                if (this.notifyEventReady(predictiveLaunchCoverExists)) {
                    this.mActionByKey = ActionByKey.READY;
                }
                keyEvent.startTracking();
                return true;
            }
            case 4: {
                if (!this.mViewFinder.isUserOperable()) {
                    return true;
                }
                if (this.mActionByKey == ActionByKey.REJECT) {
                    if (this.mViewFinder.isFlashAndSettingMenuOpened()) {
                        this.mViewFinder.closeDialogs();
                    }
                    return true;
                }
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.CAMERA_KEY);
                this.notifyShutterKeyEvent(true);
                keyEvent.startTracking();
                return true;
            }
            case 3: {
                if (this.mViewFinder.isFlashAndSettingMenuOpened()) {
                    this.mActionByKey = ActionByKey.REJECT;
                    this.mViewFinder.closeDialogs();
                    return true;
                }
                final boolean predictiveLaunchCoverExists2 = this.mViewFinder.predictiveLaunchCoverExists();
                this.mViewFinder.hidePredictiveLaunchCover(ViewFinderImpl.PredictiveLaunchHideTrigger.HW_CAMERA_KEY);
                if (this.notifyEventReady(predictiveLaunchCoverExists2)) {
                    this.mActionByKey = ActionByKey.READY;
                }
                this.mViewFinder.clearCanceledSideTouchEventIcons();
                return true;
            }
            case 2: {
                if (!this.mViewFinder.isUserOperable()) {
                    return true;
                }
                this.mActionByKey = ActionByKey.NONE;
                this.mViewFinder.hidePredictiveLaunchCover(ViewFinderImpl.PredictiveLaunchHideTrigger.VOLUME_KEY_ZOOM);
                if (keyEvent.getKeyCode() == 24) {
                    b = true;
                }
                this.mAngleActionHandler.prepareKeyZooming(b);
                return true;
            }
            case 1: {
                return this.mStateMachine.isVideoRecording();
            }
        }
    }
    
    private boolean dispatchKeyLongPress(final KeyEvent keyEvent) {
        if (!this.isEventAccepted((EventSource)KeyEventSource.from(keyEvent.getKeyCode()))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchKeyLongPress() not accepted. requested:");
            sb.append(keyEvent.getKeyCode());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return true;
        }
        switch (UserEventHandler$1.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[this.mKeyEventTranslator.translateKeyCodeOnLongPress(keyEvent.getKeyCode()).ordinal()]) {
            case 5:
            case 6: {
                if (!this.mViewFinder.isUserOperable()) {
                    return true;
                }
                if (this.mActionByKey == ActionByKey.REJECT) {
                    return true;
                }
                if (this.notifyEventCaptureBurst()) {
                    this.mActionByKey = ActionByKey.CAPTURE_BURST;
                    break;
                }
                break;
            }
            case 4: {
                if (this.mActionByKey == ActionByKey.CAPTURE) {
                    this.notifyBurstShotRejectedReason(this.mHandlingEventSource);
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    private boolean dispatchKeyUp(final KeyEvent keyEvent) {
        if (!this.isEventAccepted((EventSource)KeyEventSource.from(keyEvent.getKeyCode()))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchKeyUp() not accepted. requested:");
            sb.append(keyEvent.getKeyCode());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return true;
        }
        this.stopEventHandling((EventSource)KeyEventSource.from(keyEvent.getKeyCode()));
        final KeyEventTranslator.TranslatedKeyCode translateKeyCodeOnUp = this.mKeyEventTranslator.translateKeyCodeOnUp(keyEvent.getKeyCode());
        final ActionByKey mActionByKey = this.mActionByKey;
        this.mActionByKey = ActionByKey.NONE;
        switch (UserEventHandler$1.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[translateKeyCodeOnUp.ordinal()]) {
            default: {
                return false;
            }
            case 9:
            case 10: {
                return true;
            }
            case 8: {
                if (this.mViewFinder.isHeadUpDisplayReady() && this.mViewFinder.isEvfPrepared()) {
                    this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_MENU, new Object[0]);
                    return true;
                }
                return true;
            }
            case 7: {
                if (this.mViewFinder.isSelfTimerCountDownViewShown()) {
                    this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SELFTIMER_CANCEL, new Object[0]);
                    return true;
                }
                if (this.mStateMachine.isVideoRecording()) {
                    this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_STOP_RECORDING, new Object[0]);
                    return true;
                }
                if (this.mViewFinder.onHandleBackKeyTutorial()) {
                    return true;
                }
                if (this.mViewFinder.closeAutoReviewIfShowing()) {
                    return true;
                }
                if (this.mViewFinder.closeSettingDialogIfOpened()) {
                    return true;
                }
                if (this.mViewFinder.closeOverlayControlIfOpened()) {
                    return true;
                }
                if (!this.mStateMachine.canApplicationBeFinished()) {
                    return true;
                }
                final CapturingMode currentCapturingMode = this.getCurrentCapturingMode();
                if (!ModeSelectorInternalMode.exists(currentCapturingMode) && !currentCapturingMode.equals(CapturingMode.FRONT_PHOTO)) {
                    return false;
                }
                this.mViewFinder.startReturnModeAnimation();
                return true;
            }
            case 5:
            case 6: {
                if (!this.mViewFinder.isUserOperable()) {
                    return true;
                }
                if (mActionByKey == ActionByKey.CAPTURE_BURST) {
                    this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
                    return true;
                }
                if (mActionByKey == ActionByKey.REJECT) {
                    return true;
                }
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.VOLUME_KEY);
                this.notifyShutterKeyEvent(false);
                return true;
            }
            case 4: {
                if (mActionByKey == ActionByKey.CAPTURE_BURST) {
                    this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
                }
                return true;
            }
            case 3: {
                if (mActionByKey == ActionByKey.READY) {
                    this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
                }
                return true;
            }
            case 2: {
                this.mAngleActionHandler.stopZooming();
                return true;
            }
            case 1: {
                return this.mStateMachine.isVideoRecording();
            }
        }
    }
    
    private void dispatchLongClick(final TouchEventSource touchEventSource, final Point point) {
        if (!this.isEventAccepted((EventSource)touchEventSource)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchLongClick() not accepted. requested:");
            sb.append(touchEventSource.toString());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return;
        }
        final TouchEventProcedure find = this.mTouchEventProcedures.find(touchEventSource);
        if (find != null) {
            find.doLongClick(point);
        }
    }
    
    private boolean dispatchSideTouchEvent(final MotionEvent motionEvent) {
        return this.mViewFinder == null || this.mViewFinder.isAutoPowerOffWarningDisplayed() || this.mSideTouchCameraGestureDetector.onSideTouchEvent(motionEvent, this.mViewFinder.getOrientation());
    }
    
    private void dispatchTouchCancel(final TouchEventSource touchEventSource) {
        this.mActivity.restartAutoPowerOffTimer();
        if (!this.isEventAccepted((EventSource)touchEventSource)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchTouchCancel() not accepted. requested:");
            sb.append(touchEventSource.toString());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return;
        }
        final TouchEventProcedure find = this.mTouchEventProcedures.find(touchEventSource);
        if (find != null) {
            find.doCancel();
        }
        this.stopEventHandling((EventSource)touchEventSource);
    }
    
    private void dispatchTouchDown(final TouchEventSource touchEventSource) {
        if (!this.startEventHandling((EventSource)touchEventSource)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchTouchDown() startEventHandling() not accepted. requested:");
            sb.append(touchEventSource.toString());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return;
        }
        final TouchEventProcedure find = this.mTouchEventProcedures.find(touchEventSource);
        if (find != null) {
            find.doTouchDown();
        }
    }
    
    private void dispatchTouchUp(final TouchEventSource touchEventSource, final Point point) {
        this.mActivity.restartAutoPowerOffTimer();
        if (!this.isEventAccepted((EventSource)touchEventSource)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchTouchUp() not accepted. requested:");
            sb.append(touchEventSource.toString());
            sb.append(" current:");
            sb.append(this.mHandlingEventSource);
            CamLog.i(sb.toString());
            return;
        }
        final TouchEventProcedure find = this.mTouchEventProcedures.find(touchEventSource);
        if (find != null) {
            find.doTouchUp(point);
        }
        this.stopEventHandling((EventSource)touchEventSource);
    }
    
    private boolean dispatchVirtualKeyEvent(final VirtualKeyEvent virtualKeyEvent) {
        if (UserEventHandler$1.$SwitchMap$com$sonyericsson$android$camera$view$UserEventHandler$VirtualKeyEvent[virtualKeyEvent.ordinal()] != 1) {
            return false;
        }
        if (this.mViewFinder.predictiveLaunchCoverExists()) {
            return false;
        }
        if (this.mViewFinder.isMessageDialogOpened()) {
            return false;
        }
        LocalResearchUtil.getInstance().setPredictiveLaunchState(false);
        ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.SMILE_CAPTURE);
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_READY, new Object[0]);
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE, new Object[0]);
        this.mActivity.restartAutoPowerOffTimer();
        return true;
    }
    
    private CapturingMode getCurrentCapturingMode() {
        return (CapturingMode)this.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
    }
    
    private boolean isBurstShotEnabled() {
        if (!this.mIsBurstShotEnabled) {
            return false;
        }
        final CapturingMode currentCapturingMode = this.getCurrentCapturingMode();
        return !currentCapturingMode.isVideo() && PlatformCapability.isManualBurstSupported(currentCapturingMode.getCameraId()) && this.mUserSettings.get(UserSettingKey.FUSION_MODE) != FusionMode.ON;
    }
    
    private boolean isCameraKeyAssignedToBurstShot() {
        return CameraKey.BURST_SHOT.equals(this.mUserSettings.get(UserSettingKey.CAMERA_KEY));
    }
    
    private boolean isCurrentStorageWritable() {
        return this.isStorageWritable(((DestinationToSave)this.mUserSettings.get(UserSettingKey.DESTINATION_TO_SAVE)).getType());
    }
    
    private boolean isEventAccepted(final EventSource obj) {
        return this.mHandlingEventSource == null || this.mHandlingEventSource.equals(obj);
    }
    
    private boolean isIdle() {
        return this.mViewFinder.isPreviewLayout();
    }
    
    private boolean isInternalStorageWritable() {
        return this.isStorageWritable(Storage.StorageType.INTERNAL);
    }
    
    private boolean isManualFocus() {
        return this.mUserSettings.get(UserSettingKey.FOCUS_RANGE) != FocusRange.AF;
    }
    
    private boolean isObjectTrackingEnabled() {
        return this.mUserSettings.get(UserSettingKey.OBJECT_TRACKING) == ObjectTracking.ON;
    }
    
    private boolean isPhotoSelfTimerEnabled() {
        return this.mUserSettings.get(UserSettingKey.SELF_TIMER) != SelfTimer.OFF;
    }
    
    private boolean isStorageWritable(final Storage.StorageType storageType) {
        final Storage.StorageState currentState = this.mStorage.getCurrentState(storageType);
        return currentState == Storage.StorageState.AVAILABLE || currentState == Storage.StorageState.AVAILABLE_NEAR_FULL;
    }
    
    private boolean isSuperSlowMode() {
        return this.mUserSettings.get(UserSettingKey.SLOW_MOTION) == SlowMotion.SUPER_SLOW_MOTION;
    }
    
    private boolean isTouchCaptureEnabled() {
        final TouchCapture touchCapture = (TouchCapture)this.mUserSettings.get(UserSettingKey.TOUCH_CAPTURE);
        if (touchCapture != null) {
            switch (UserEventHandler$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$TouchCapture[touchCapture.ordinal()]) {
                case 2: {
                    if (this.getCurrentCapturingMode().isFront()) {
                        return true;
                    }
                    break;
                }
                case 1: {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void notifyBurstShotRejectedReason(final EventSource eventSource) {
        if (!this.mIsBurstShotEnabled) {
            return;
        }
        final CapturingMode currentCapturingMode = this.getCurrentCapturingMode();
        if (currentCapturingMode.isVideo()) {
            return;
        }
        if (PlatformCapability.isManualBurstSupported(currentCapturingMode.getCameraId())) {
            if (!this.isCameraKeyAssignedToBurstShot() && KeyEventSource.CAMERA.equals(eventSource)) {
                this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_REJECTED, ViewFinder.BurstRejectedReason.BURST_IS_DISABLED_BY_CAMERA_KEY_ASSIGN_SETTING);
            }
            else if (this.mUserSettings.get(UserSettingKey.FUSION_MODE) == FusionMode.ON) {
                this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_REJECTED, ViewFinder.BurstRejectedReason.CANNOT_BURST_DUE_TO_FUSION_MODE);
            }
        }
        else if ((this.isCameraKeyAssignedToBurstShot() || !KeyEventSource.CAMERA.equals(eventSource)) && PlatformCapability.isManualBurstSupported(CameraInfo.CameraId.BACK)) {
            this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ON_BURST_REJECTED, ViewFinder.BurstRejectedReason.CANNOT_BURST_USING_FRONT_CAMERA);
        }
    }
    
    private boolean notifyEventCaptureBurst() {
        if (this.isInternalStorageWritable()) {
            if (this.isBurstShotEnabled()) {
                if (this.canSelfTimerActivation()) {
                    this.mViewFinder.closeDialogs();
                    final StateMachine mStateMachine = this.mStateMachine;
                    StateMachine.TransitterEvent transitterEvent;
                    if (!this.getCurrentCapturingMode().isVideo()) {
                        transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_READY;
                    }
                    else {
                        transitterEvent = StateMachine.TransitterEvent.EVENT_RECORD_READY;
                    }
                    mStateMachine.sendEvent(transitterEvent, new Object[0]);
                }
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_BURST, new Object[0]);
                return true;
            }
            this.notifyBurstShotRejectedReason(this.mHandlingEventSource);
        }
        else if (!this.isBurstShotEnabled()) {
            this.notifyBurstShotRejectedReason(this.mHandlingEventSource);
        }
        return false;
    }
    
    private boolean notifyEventReady() {
        return this.notifyEventReady(false);
    }
    
    private boolean notifyEventReady(final boolean predictiveLaunchState) {
        if (!this.mViewFinder.isUserOperable()) {
            return false;
        }
        LocalResearchUtil.getInstance().setPredictiveLaunchState(predictiveLaunchState);
        if (this.canSelfTimerActivation()) {
            return false;
        }
        this.mViewFinder.closeDialogs();
        final StateMachine mStateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent;
        if (!this.getCurrentCapturingMode().isVideo()) {
            transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_READY;
        }
        else {
            transitterEvent = StateMachine.TransitterEvent.EVENT_RECORD_READY;
        }
        mStateMachine.sendEvent(transitterEvent, new Object[0]);
        return true;
    }
    
    private void notifyShutterKeyEvent(final boolean b) {
        if (this.mViewFinder.isDisplayFlashScreenDisplayed()) {
            return;
        }
        this.mViewFinder.closeDialogs();
        final StateMachine.TransitterEvent selectShutterKeyAction = this.selectShutterKeyAction(b);
        switch (UserEventHandler$1.$SwitchMap$com$sonyericsson$android$camera$controller$StateMachine$TransitterEvent[selectShutterKeyAction.ordinal()]) {
            default: {
                this.mStateMachine.sendEvent(selectShutterKeyAction, new Object[0]);
                break;
            }
            case 3: {
                this.mActionByKey = ActionByKey.CAPTURE;
                this.mStateMachine.sendEvent(selectShutterKeyAction, Event.SelfTimerTrigger.NORMAL);
                break;
            }
            case 2: {
                this.mActionByKey = ActionByKey.CAPTURE;
                this.mStateMachine.sendEvent(selectShutterKeyAction, new Object[0]);
                break;
            }
            case 1: {
                this.mActionByKey = ActionByKey.CAPTURE_BURST;
                this.mStateMachine.sendEvent(selectShutterKeyAction, new Object[0]);
                break;
            }
        }
    }
    
    private void resetKeyEventHandling() {
        this.mKeyEventTranslator.reset();
        if (this.mHandlingEventSource instanceof KeyEventSource) {
            this.mHandlingEventSource = null;
            this.mActionByKey = ActionByKey.NONE;
        }
        this.mAngleActionHandler.clear();
    }
    
    private void resetSideTouchEventHandling() {
        if (this.mHandlingEventSource instanceof SideTouchEventSource) {
            this.mHandlingEventSource = null;
        }
        this.mAngleActionHandler.clear();
    }
    
    private void resetTouchEventHandling() {
        if (this.mHandlingEventSource instanceof TouchEventSource) {
            this.mHandlingEventSource = null;
        }
        this.mAngleActionHandler.clear();
    }
    
    private StateMachine.TransitterEvent selectDefaultPhotoAction(final boolean b) {
        if (this.isBurstShotEnabled() && b && this.isInternalStorageWritable()) {
            return StateMachine.TransitterEvent.EVENT_CAPTURE_BURST;
        }
        if (this.canSelfTimerActivation()) {
            return StateMachine.TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN;
        }
        return StateMachine.TransitterEvent.EVENT_CAPTURE;
    }
    
    private StateMachine.TransitterEvent selectDefaultVideoAction() {
        if (this.mStateMachine.isVideoRecording()) {
            return StateMachine.TransitterEvent.EVENT_STOP_RECORDING;
        }
        return StateMachine.TransitterEvent.EVENT_START_RECORDING;
    }
    
    private StateMachine.TransitterEvent selectShutterKeyAction(final boolean b) {
        if (!this.getCurrentCapturingMode().isVideo()) {
            return this.selectDefaultPhotoAction(b && this.isCameraKeyAssignedToBurstShot());
        }
        if (this.isSuperSlowMode() && this.mStateMachine.isVideoRecording()) {
            return StateMachine.TransitterEvent.EVENT_TRIGGER_SLOW_MOTION;
        }
        return this.selectDefaultVideoAction();
    }
    
    private boolean startEventHandling(final EventSource eventSource) {
        if (this.mHandlingEventSource == null) {
            this.mHandlingEventSource = eventSource;
            return true;
        }
        if (!KeyEventSource.FOCUS.equals(this.mHandlingEventSource)) {
            return false;
        }
        if (KeyEventSource.CAMERA.equals(eventSource)) {
            this.mHandlingEventSource = eventSource;
            return true;
        }
        return false;
    }
    
    private void stopEventHandling(final EventSource obj) {
        if (this.mHandlingEventSource == null) {
            return;
        }
        if (this.mHandlingEventSource.equals(obj)) {
            this.mHandlingEventSource = null;
        }
    }
    
    public void release() {
        this.mSideTouchCameraGestureDetector.unregister();
    }
    
    private enum ActionByKey
    {
        private static final ActionByKey[] $VALUES;
        
        CAPTURE, 
        CAPTURE_BURST, 
        NONE, 
        READY, 
        REJECT;
        
        static {
            $VALUES = new ActionByKey[] { ActionByKey.READY, ActionByKey.REJECT, ActionByKey.CAPTURE, ActionByKey.CAPTURE_BURST, ActionByKey.NONE };
        }
    }
    
    private class AngleActionHandler
    {
        private VariableIndex.Calculator mCalculator;
        private VariableIndex mCurrentVariable;
        private VariableUserEventTicker mEventTicker;
        private int mSideTouchScrollDistance;
        final UserEventHandler this$0;
        
        private AngleActionHandler(final UserEventHandler this$0) {
            this.this$0 = this$0;
        }
        
        private void clear() {
            if (this.mEventTicker != null) {
                this.mEventTicker.stop();
                this.mEventTicker = null;
            }
            this.mCurrentVariable = null;
            this.mCalculator = null;
        }
        
        private boolean finishZoom() {
            if (!this.isHandling()) {
                return false;
            }
            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ZOOM_FINISH, new Object[0]);
            this.clear();
            return true;
        }
        
        private int getFrontAngleStep(final FrontAngle frontAngle) {
            int n;
            if (frontAngle == FrontAngle.CROPPED) {
                n = (int)((PlatformCapability.getWideZoomTargetRatio(CameraInfo.CameraId.FRONT) - 1.0) / (PlatformCapability.getMaxZoomRatio(CameraInfo.CameraId.FRONT) - 1.0) * 120.0);
            }
            else {
                n = 0;
            }
            return n;
        }
        
        private boolean isHandling() {
            return this.mCurrentVariable != null && this.mCalculator != null;
        }
        
        private boolean performFrontAngleChange(final FrontAngle frontAngle) {
            return this.performZoom(this.getFrontAngleStep(frontAngle));
        }
        
        private boolean performKeyZooming() {
            return this.performZoom(System.currentTimeMillis());
        }
        
        private boolean performPinchZooming(final float f) {
            return this.performZoom(f);
        }
        
        private boolean performZoom(final Object... array) {
            if (!this.isHandling()) {
                return false;
            }
            final int index = this.mCurrentVariable.getIndex();
            this.mCurrentVariable = this.mCalculator.calculate(this.mCurrentVariable, array);
            final boolean b = index != this.mCurrentVariable.getIndex();
            if (b) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ZOOM_PERFORM, this.mCurrentVariable);
            }
            return b;
        }
        
        private boolean prepareFrontAngleChange(final FrontAngle frontAngle) {
            if (!this.this$0.getCurrentCapturingMode().isFront()) {
                return false;
            }
            if (this.this$0.mStateMachine.isAngleEventReceivable()) {
                this.mCalculator = new FrontAngleChangeCalculator();
                this.mCurrentVariable = new VariableIndex(120, 0, this.getFrontAngleStep(frontAngle));
                return true;
            }
            return false;
        }
        
        private boolean prepareKeyZooming(final boolean b) {
            if (this.prepareZoom(new KeyZoomStepCalculator(b))) {
                (this.mEventTicker = new VariableUserEventTicker()).start(33, (OnEventTickedListener)new OnEventTickedListener(this) {
                    final AngleActionHandler this$1;
                    
                    @Override
                    public void onTicked(final VariableUserEventTicker variableUserEventTicker) {
                        this.this$1.performKeyZooming();
                    }
                });
                return true;
            }
            return false;
        }
        
        private boolean preparePinchZooming() {
            return this.prepareZoom(new PinchZoomStepCalculator());
        }
        
        private boolean prepareSideTouchZoom() {
            if (this.prepareZoom(new SideTouchZoomStepCalculator())) {
                this.mSideTouchScrollDistance = 0;
                (this.mEventTicker = new VariableUserEventTicker()).start(33, (OnEventTickedListener)new OnEventTickedListener(this) {
                    final AngleActionHandler this$1;
                    
                    @Override
                    public void onTicked(final VariableUserEventTicker variableUserEventTicker) {
                        this.this$1.performZoom(this.this$1.mSideTouchScrollDistance);
                    }
                });
                return true;
            }
            return false;
        }
        
        private boolean prepareZoom(final VariableIndex.Calculator mCalculator) {
            if (this.isHandling()) {
                return false;
            }
            if (!this.this$0.getCurrentCapturingMode().isFront() && this.this$0.isCurrentStorageWritable()) {
                if (this.this$0.mStateMachine.isAngleEventReceivable()) {
                    this.mCalculator = mCalculator;
                    final Float zoom = this.this$0.mStateMachine.getZoom();
                    if (zoom != null) {
                        this.mCurrentVariable = new VariableIndex(120, 0, (int)(120.0f * zoom));
                        this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ZOOM_PREPARE, this.mCurrentVariable);
                        return true;
                    }
                }
                return false;
            }
            this.this$0.mViewFinder.notifyZoomOperationRejected();
            return false;
        }
        
        private boolean stopZooming() {
            return this.finishZoom();
        }
        
        private void switchFrontAngle() {
            final FrontAngle frontAngle = (FrontAngle)this.this$0.mUserSettings.get(UserSettingKey.FRONT_ANGLE);
            if (this.prepareFrontAngleChange(frontAngle)) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ANGLE_CHANGE_START, new Object[0]);
                FrontAngle frontAngle2;
                if (frontAngle == FrontAngle.DEFAULT) {
                    frontAngle2 = FrontAngle.CROPPED;
                }
                else {
                    frontAngle2 = FrontAngle.DEFAULT;
                }
                this.this$0.mUserSettings.set(frontAngle2);
                this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_ANGLE_CHANGE_START, new Object[0]);
                LocalResearchUtil.getInstance().setSettingsValue(frontAngle, frontAngle2, this.this$0.getCurrentCapturingMode());
                (this.mEventTicker = new VariableUserEventTicker()).start(33, (OnEventTickedListener)new OnEventTickedListener(this, frontAngle2) {
                    int mCount = 1;
                    final AngleActionHandler this$1;
                    final FrontAngle val$newAngle;
                    
                    @Override
                    public void onTicked(final VariableUserEventTicker variableUserEventTicker) {
                        this.this$1.performFrontAngleChange(this.val$newAngle);
                        --this.mCount;
                        if (this.mCount <= 0) {
                            variableUserEventTicker.stop();
                            this.this$1.finishZoom();
                        }
                    }
                });
            }
        }
        
        private void updateSideTouchZoomStrength(final int mSideTouchScrollDistance) {
            this.mSideTouchScrollDistance = mSideTouchScrollDistance;
        }
        
        protected int getCurrentAngle() {
            return this.mCurrentVariable.getIndex();
        }
    }
    
    private static class VariableUserEventTicker implements Runnable
    {
        private static final int INTERVAL_30_FPS = 33;
        private Handler mHandler;
        private int mInterval;
        private OnEventTickedListener mOnTickingListener;
        
        private void postSchedule(final long n) {
            if (this.mOnTickingListener == null) {
                return;
            }
            if (this.mHandler == null) {
                this.mHandler = new Handler();
            }
            this.mHandler.removeCallbacks((Runnable)this);
            this.mHandler.postDelayed((Runnable)this, n);
        }
        
        @Override
        public void run() {
            final long currentTimeMillis = System.currentTimeMillis();
            this.mOnTickingListener.onTicked(this);
            final int n = this.mInterval - (int)(System.currentTimeMillis() - currentTimeMillis);
            long n2;
            if (n < 0) {
                n2 = 0L;
            }
            else {
                n2 = n;
            }
            this.postSchedule(n2);
        }
        
        void start(final int mInterval, final OnEventTickedListener mOnTickingListener) {
            this.mInterval = mInterval;
            this.mOnTickingListener = mOnTickingListener;
            this.postSchedule(0L);
        }
        
        void stop() {
            this.mHandler.removeCallbacks((Runnable)this);
            this.mOnTickingListener = null;
        }
        
        public interface OnEventTickedListener
        {
            void onTicked(final VariableUserEventTicker p0);
        }
    }
    
    private class AngleChangeButtonProcedure extends TouchEventProcedure
    {
        final UserEventHandler this$0;
        
        private AngleChangeButtonProcedure(final UserEventHandler this$0) {
        }
        
        public void doClick(final Point point) {
            this.this$0.mAngleActionHandler.switchFrontAngle();
        }
    }
    
    private class TouchEventProcedure
    {
        final UserEventHandler this$0;
        
        private TouchEventProcedure(final UserEventHandler this$0) {
            this.this$0 = this$0;
        }
        
        void doCancel() {
        }
        
        void doClick(final Point point) {
        }
        
        void doLongClick(final Point point) {
        }
        
        void doTouchAreaScaleReady() {
        }
        
        void doTouchAreaScaling(final float n) {
        }
        
        void doTouchDown() {
        }
        
        void doTouchUp(final Point point) {
        }
    }
    
    private class CaptureAreaEventProcedure extends TouchEventProcedure
    {
        final UserEventHandler this$0;
        
        private CaptureAreaEventProcedure(final UserEventHandler this$0) {
        }
        
        public void doCancel() {
            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
        }
        
        void setFocusPositionToDeviceAndViewFinder(final Point point, final FocusRectangles.FocusSetType focusSetType) {
            if (PlatformCapability.isTouchFocusSupported(this.this$0.mStateMachine.getCurrentCameraId())) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SET_TOUCHED_POSITION, point, this.this$0.mViewFinder.convertTouchPointToRectInDevicePreviewPositionRatio(point), focusSetType);
            }
        }
        
        void setSelectedObjectPositionToDeviceAndViewFinder(final Point point) {
            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SET_SELECTED_OBJECT_POSITION, point, this.this$0.mViewFinder.getPosition(point));
        }
    }
    
    private class CaptureAreaTouchEventProcedureSelector extends TouchEventProcedure
    {
        private final PhotoCaptureAreaEventProcedure mPhoto;
        private final FrontPhotoCaptureAreaEventProcedure mPhotoFront;
        private final SuperSlowVideoCaptureAreaEventProcedure mSuperSlow;
        private final VideoCaptureAreaEventProcedure mVideo;
        private final FrontVideoCaptureAreaEventProcedure mVideoFront;
        final UserEventHandler this$0;
        
        private CaptureAreaTouchEventProcedureSelector(final UserEventHandler this$0) {
            this.mPhoto = new PhotoCaptureAreaEventProcedure();
            this.mPhotoFront = new FrontPhotoCaptureAreaEventProcedure();
            this.mVideo = new VideoCaptureAreaEventProcedure();
            this.mVideoFront = new FrontVideoCaptureAreaEventProcedure();
            this.mSuperSlow = new SuperSlowVideoCaptureAreaEventProcedure();
        }
        
        private TouchEventProcedure getCaptureAreaProcedure() {
            final CapturingMode capturingMode = (CapturingMode)this.this$0.mUserSettings.get(UserSettingKey.CAPTURING_MODE);
            if (capturingMode.isVideo()) {
                if (capturingMode.isFront()) {
                    return this.mVideoFront;
                }
                if (this.this$0.isSuperSlowMode()) {
                    return this.mSuperSlow;
                }
                return this.mVideo;
            }
            else {
                if (capturingMode.isFront()) {
                    return this.mPhotoFront;
                }
                return this.mPhoto;
            }
        }
        
        @Override
        void doCancel() {
            this.getCaptureAreaProcedure().doCancel();
        }
        
        @Override
        void doClick(final Point point) {
            this.getCaptureAreaProcedure().doClick(point);
        }
        
        @Override
        void doLongClick(final Point point) {
            this.getCaptureAreaProcedure().doLongClick(point);
        }
        
        @Override
        void doTouchAreaScaleReady() {
            this.getCaptureAreaProcedure().doTouchAreaScaleReady();
        }
        
        @Override
        void doTouchAreaScaling(final float n) {
            this.getCaptureAreaProcedure().doTouchAreaScaling(n);
        }
        
        @Override
        void doTouchDown() {
            this.getCaptureAreaProcedure().doTouchDown();
        }
        
        @Override
        void doTouchUp(final Point point) {
            this.getCaptureAreaProcedure().doTouchUp(point);
        }
    }
    
    private class CaptureButtonProcedure extends TouchEventProcedure
    {
        private boolean mIsBurst;
        private boolean mIsTouched;
        final UserEventHandler this$0;
        
        private CaptureButtonProcedure(final UserEventHandler this$0) {
            this.mIsTouched = false;
            this.mIsBurst = false;
        }
        
        public void doCancel() {
            if (!this.mIsTouched) {
                return;
            }
            this.mIsTouched = false;
            this.this$0.mViewFinder.clearBurstShootingRejectedReason();
            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
        }
        
        public void doLongClick(final Point point) {
            if (!this.mIsTouched) {
                return;
            }
            this.mIsBurst = this.this$0.notifyEventCaptureBurst();
        }
        
        public void doTouchDown() {
            this.mIsTouched = true;
            this.mIsBurst = false;
            this.this$0.mViewFinder.clearCanceledSideTouchEventIcons();
            this.this$0.notifyEventReady();
        }
        
        public void doTouchUp(final Point point) {
            PerfLog.CAPTURE_BUTTON_TAP.transit();
            this.mIsTouched = false;
            if (this.this$0.mViewFinder.isSwitchingAnimationProgress()) {
                return;
            }
            this.this$0.mViewFinder.closeDialogs();
            ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.CAPTURE_BUTTON);
            if (this.mIsBurst) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
            }
            else {
                this.this$0.mStateMachine.sendEvent(this.this$0.selectDefaultPhotoAction(false), new Object[0]);
            }
        }
    }
    
    private abstract static class EventDispatcher
    {
        private UserEventHandler mHandler;
        private boolean mIsRunning;
        
        private EventDispatcher() {
            this.mIsRunning = false;
        }
        
        public void attach(final UserEventHandler mHandler) {
            this.mHandler = mHandler;
        }
        
        protected UserEventHandler getHandler() {
            return this.mHandler;
        }
        
        protected boolean isRunning() {
            return this.mIsRunning;
        }
        
        protected void reset() {
        }
        
        public void start() {
            this.mIsRunning = true;
            this.reset();
        }
        
        public void stop() {
            this.mIsRunning = false;
            this.reset();
        }
    }
    
    private interface EventSource
    {
    }
    
    private class FrontPhotoCaptureAreaEventProcedure extends CaptureAreaEventProcedure
    {
        protected boolean mIsBurst;
        final UserEventHandler this$0;
        
        private FrontPhotoCaptureAreaEventProcedure(final UserEventHandler this$0) {
            this.mIsBurst = false;
        }
        
        @Override
        public void doCancel() {
            if (this.this$0.mAngleActionHandler.stopZooming()) {
                return;
            }
            this.this$0.mViewFinder.clearBurstShootingRejectedReason();
            final StateMachine access$3400 = this.this$0.mStateMachine;
            StateMachine.TransitterEvent transitterEvent;
            if (this.this$0.mViewFinder.isTouchFocus() && !this.mIsBurst) {
                transitterEvent = StateMachine.TransitterEvent.EVENT_CLEAR_FOCUS;
            }
            else {
                transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL;
            }
            access$3400.sendEvent(transitterEvent, new Object[0]);
            this.mIsBurst = false;
        }
        
        public void doClick(final Point point) {
            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_AF_AFTER_OBJECT_TRACKED, point, this.this$0.mViewFinder.getPosition(point));
            if (this.this$0.isTouchCaptureEnabled()) {
                if (this.this$0.mViewFinder.isSwitchingAnimationProgress()) {
                    return;
                }
                this.this$0.mStateMachine.sendEvent(this.this$0.selectDefaultPhotoAction(false), new Object[0]);
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.TOUCH_CAPTURE);
            }
        }
        
        public void doLongClick(final Point point) {
            if (this.this$0.mViewFinder.isSwitchingAnimationProgress()) {
                return;
            }
            if (!this.this$0.isTouchCaptureEnabled()) {
                return;
            }
            if (this.this$0.isInternalStorageWritable()) {
                if (this.this$0.isBurstShotEnabled()) {
                    this.this$0.mViewFinder.hideAutoReview();
                    if (this.this$0.canObjectTracking()) {
                        this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_AF_AFTER_OBJECT_TRACKED, point, this.this$0.mViewFinder.getPosition(point));
                    }
                    else {
                        ((CaptureAreaEventProcedure)this).setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.FIRST);
                        ((CaptureAreaEventProcedure)this).setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.RELEASE);
                    }
                    this.mIsBurst = true;
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_BURST, new Object[0]);
                }
                else {
                    this.this$0.notifyBurstShotRejectedReason(this.this$0.mHandlingEventSource);
                }
            }
            else if (!this.this$0.isBurstShotEnabled()) {
                this.this$0.notifyBurstShotRejectedReason(this.this$0.mHandlingEventSource);
            }
        }
        
        public void doTouchDown() {
            this.mIsBurst = false;
        }
        
        public void doTouchUp(final Point point) {
            if (this.this$0.mViewFinder.isFrontAngleChanging()) {
                return;
            }
            if (this.this$0.mViewFinder.isAutoReviewShowing()) {
                this.this$0.mViewFinder.hideAutoReview();
                return;
            }
            if (!this.this$0.mViewFinder.canFocusRectanglesBeUpdated()) {
                return;
            }
            if (this.this$0.isTouchCaptureEnabled()) {
                if (this.this$0.mViewFinder.isSwitchingAnimationProgress()) {
                    return;
                }
                this.this$0.mStateMachine.sendEvent(this.this$0.selectDefaultPhotoAction(false), new Object[0]);
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.TOUCH_CAPTURE);
            }
            else if (this.this$0.isCurrentStorageWritable()) {
                ((CaptureAreaEventProcedure)this).setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.FIRST);
                ((CaptureAreaEventProcedure)this).setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.RELEASE);
            }
        }
    }
    
    private class FrontVideoCaptureAreaEventProcedure extends CaptureAreaEventProcedure
    {
        final UserEventHandler this$0;
        
        private FrontVideoCaptureAreaEventProcedure(final UserEventHandler this$0) {
        }
        
        @Override
        public void doCancel() {
            if (this.this$0.mAngleActionHandler.stopZooming()) {
                return;
            }
            if (this.this$0.isTouchCaptureEnabled()) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
            }
        }
        
        public void doTouchUp(final Point point) {
            if (this.this$0.mViewFinder.isAutoReviewShowing()) {
                this.this$0.mViewFinder.hideAutoReview();
                return;
            }
            if (this.this$0.isTouchCaptureEnabled()) {
                this.this$0.mStateMachine.sendEvent(this.this$0.selectDefaultVideoAction(), new Object[0]);
            }
        }
    }
    
    public static class KeyEventDispatcher extends EventDispatcher
    {
        @Override
        protected void reset() {
            ((EventDispatcher)this).getHandler().resetKeyEventHandling();
        }
        
        public boolean sendKeyDown(final KeyEvent keyEvent) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("KeyEventDispatcher#sendKeyDown() event:");
                    sb.append(keyEvent.toString());
                    CamLog.d(sb.toString());
                }
                return ((EventDispatcher)this).getHandler().dispatchKeyDown(keyEvent);
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("KeyEventDispatcher#sendKeyDown() event is rejected. event:");
            sb2.append(keyEvent.toString());
            CamLog.i(sb2.toString());
            return false;
        }
        
        public boolean sendKeyLongPress(final KeyEvent keyEvent) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("KeyEventDispatcher#sendKeyLongPress() event:");
                    sb.append(keyEvent.toString());
                    CamLog.d(sb.toString());
                }
                return ((EventDispatcher)this).getHandler().dispatchKeyLongPress(keyEvent);
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("KeyEventDispatcher#sendKeyLongPress() event is rejected. event:");
            sb2.append(keyEvent.toString());
            CamLog.i(sb2.toString());
            return false;
        }
        
        public boolean sendKeyUp(final KeyEvent keyEvent) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("KeyEventDispatcher#sendKeyUp() event:");
                    sb.append(keyEvent.toString());
                    CamLog.d(sb.toString());
                }
                return ((EventDispatcher)this).getHandler().dispatchKeyUp(keyEvent);
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("KeyEventDispatcher#sendKeyUp() event is rejected. event:");
            sb2.append(keyEvent.toString());
            CamLog.i(sb2.toString());
            return false;
        }
    }
    
    private static class KeyEventSource implements EventSource
    {
        private static KeyEventSource CAMERA;
        private static KeyEventSource FOCUS;
        private final int mKeyCode;
        
        static {
            KeyEventSource.CAMERA = from(27);
            KeyEventSource.FOCUS = from(80);
        }
        
        private KeyEventSource(final int mKeyCode) {
            this.mKeyCode = mKeyCode;
        }
        
        public static KeyEventSource from(final int n) {
            return new KeyEventSource(n);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o != null && this.getClass() == o.getClass() && this.mKeyCode == ((KeyEventSource)o).mKeyCode);
        }
        
        @Override
        public int hashCode() {
            return this.mKeyCode;
        }
    }
    
    private class OnSideTouchGestureListenerImpl implements OnSideTouchGestureListener
    {
        private SideTouchEvent mTriggerEvent;
        final UserEventHandler this$0;
        
        private OnSideTouchGestureListenerImpl(final UserEventHandler this$0) {
            this.this$0 = this$0;
        }
        
        private boolean isSideTouchAvailableMode() {
            switch (UserEventHandler$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.this$0.getCurrentCapturingMode().ordinal()]) {
                default: {
                    return false;
                }
                case 1:
                case 2:
                case 3:
                case 4: {
                    return true;
                }
            }
        }
        
        @Override
        public void onDoubleTap(final SideTouchEvent obj, final int n, final int n2) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("OnSideTouchGestureListenerImpl#onDoubleTap() ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (!this.this$0.isEventAccepted((EventSource)SideTouchEventSource.SIDE_SENSOR)) {
                return;
            }
            if (!this.isSideTouchAvailableMode()) {
                return;
            }
            if (this.this$0.mAngleActionHandler.stopZooming()) {
                return;
            }
            if (this.this$0.mViewFinder.closeSettingDialogIfOpened() || !this.this$0.mViewFinder.isUserOperable() || this.this$0.mViewFinder.isTutorialOpened()) {
                return;
            }
            if (!this.this$0.isCurrentStorageWritable()) {
                return;
            }
            if (this.this$0.mActionByKey == ActionByKey.CAPTURE_BURST) {
                return;
            }
            if (this.this$0.mViewFinder.predictiveLaunchCoverExists()) {
                this.this$0.mViewFinder.hidePredictiveLaunchCover(ViewFinderImpl.PredictiveLaunchHideTrigger.SIDE_SENSING);
                LocalResearchUtil.getInstance().setPredictiveLaunchState(true);
            }
            else {
                LocalResearchUtil.getInstance().setPredictiveLaunchState(false);
            }
            if (this.this$0.mViewFinder.isSelfTimerCountDownViewShown()) {
                if (this.this$0.getCurrentCapturingMode().isVideo()) {
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_RECORDING, new Object[0]);
                }
                else {
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE, new Object[0]);
                }
            }
            else {
                if (!this.this$0.mViewFinder.isPreviewLayout()) {
                    return;
                }
                if (this.this$0.mViewFinder.onSideTapped(obj)) {
                    ResearchUtil.getInstance().setSideSensePosition(n, n2);
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_CAPTURE_COUNTDOWN, Event.SelfTimerTrigger.SIDE_SENSE);
                }
            }
        }
        
        @Override
        public void onGestureFinished() {
            if (!this.this$0.isEventAccepted((EventSource)SideTouchEventSource.SIDE_SENSOR)) {
                return;
            }
            this.this$0.resetSideTouchEventHandling();
        }
        
        @Override
        public void onGestureStart() {
            this.this$0.startEventHandling((EventSource)SideTouchEventSource.SIDE_SENSOR);
        }
        
        @Override
        public void onScroll(final SideTouchEvent obj) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("OnSideTouchGestureListenerImpl#onScrollEnd() ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (!this.this$0.isEventAccepted((EventSource)SideTouchEventSource.SIDE_SENSOR)) {
                return;
            }
            if (!this.this$0.getCurrentCapturingMode().isFront() && this.mTriggerEvent != null && this.mTriggerEvent.area == obj.area) {
                this.this$0.mAngleActionHandler.updateSideTouchZoomStrength(obj.position - this.mTriggerEvent.position);
            }
        }
        
        @Override
        public void onScrollEnd() {
            if (CamLog.DEBUG) {
                CamLog.d("OnSideTouchGestureListenerImpl#onScrollEnd()");
            }
            if (!this.this$0.isEventAccepted((EventSource)SideTouchEventSource.SIDE_SENSOR)) {
                return;
            }
            if (!this.this$0.getCurrentCapturingMode().isFront()) {
                this.this$0.mAngleActionHandler.stopZooming();
            }
        }
        
        @Override
        public void onScrollStart(final SideTouchEvent sideTouchEvent) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("OnSideTouchGestureListenerImpl#onScrollStart() ");
                sb.append(sideTouchEvent);
                CamLog.d(sb.toString());
            }
            if (!this.this$0.isEventAccepted((EventSource)SideTouchEventSource.SIDE_SENSOR)) {
                return;
            }
            if (!this.isSideTouchAvailableMode()) {
                return;
            }
            if (this.this$0.mViewFinder.isUserOperable() && !this.this$0.mViewFinder.isTutorialOpened()) {
                if (!this.this$0.mViewFinder.predictiveLaunchCoverExists()) {
                    if (!this.this$0.getCurrentCapturingMode().isFront()) {
                        this.mTriggerEvent = sideTouchEvent;
                        if (this.this$0.mAngleActionHandler.prepareSideTouchZoom()) {
                            this.this$0.mViewFinder.onSideTouchZoom(sideTouchEvent, this.this$0.mAngleActionHandler.getCurrentAngle());
                        }
                    }
                    else {
                        this.this$0.mViewFinder.notifyZoomOperationRejected();
                    }
                }
            }
        }
    }
    
    private class PhotoCaptureAreaEventProcedure extends FrontPhotoCaptureAreaEventProcedure
    {
        final UserEventHandler this$0;
        
        private PhotoCaptureAreaEventProcedure(final UserEventHandler this$0) {
        }
        
        private void startAutoFocusAfterObjectTrackedIfPossible(final Point point) {
            if (!this.this$0.canSelfTimerActivation()) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_AF_AFTER_OBJECT_TRACKED, point, this.this$0.mViewFinder.getPosition(point));
            }
        }
        
        public void doTouchAreaScaleReady() {
            this.this$0.mAngleActionHandler.preparePinchZooming();
        }
        
        public void doTouchAreaScaling(final float n) {
            this.this$0.mAngleActionHandler.performPinchZooming(n);
        }
        
        @Override
        public void doTouchUp(final Point selectedObjectPositionToDeviceAndViewFinder) {
            boolean b;
            if (this.this$0.mViewFinder.isAutoReviewShowing()) {
                this.this$0.mViewFinder.hideAutoReview();
                b = true;
            }
            else {
                b = false;
            }
            if (this.this$0.mAngleActionHandler.stopZooming()) {
                b = true;
            }
            if (b) {
                return;
            }
            if (!this.this$0.mViewFinder.canFocusRectanglesBeUpdated()) {
                return;
            }
            if (this.this$0.canObjectTracking()) {
                ((CaptureAreaEventProcedure)this).setSelectedObjectPositionToDeviceAndViewFinder(selectedObjectPositionToDeviceAndViewFinder);
            }
            else if (this.this$0.isCurrentStorageWritable()) {
                ((CaptureAreaEventProcedure)this).setFocusPositionToDeviceAndViewFinder(selectedObjectPositionToDeviceAndViewFinder, FocusRectangles.FocusSetType.FIRST);
                ((CaptureAreaEventProcedure)this).setFocusPositionToDeviceAndViewFinder(selectedObjectPositionToDeviceAndViewFinder, FocusRectangles.FocusSetType.RELEASE);
                this.this$0.mViewFinder.switchSemiAutoStateByTouch(true);
            }
            else {
                this.this$0.mViewFinder.switchSemiAutoStateByTouch(true);
            }
            if (!this.this$0.isTouchCaptureEnabled()) {
                return;
            }
            if (this.this$0.canObjectTracking()) {
                this.startAutoFocusAfterObjectTrackedIfPossible(selectedObjectPositionToDeviceAndViewFinder);
            }
            if (this.this$0.mViewFinder.isSwitchingAnimationProgress()) {
                return;
            }
            if (this.mIsBurst) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
            }
            else {
                this.this$0.mStateMachine.sendEvent(this.this$0.selectDefaultPhotoAction(false), new Object[0]);
            }
            ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.TOUCH_CAPTURE);
            LocalResearchUtil.getInstance().setPredictiveLaunchState(false);
        }
    }
    
    private class PredictiveLaunchCoverProcedure extends TouchEventProcedure
    {
        final UserEventHandler this$0;
        
        private PredictiveLaunchCoverProcedure(final UserEventHandler this$0) {
        }
        
        @Override
        void doTouchUp(final Point point) {
            if (((PredictiveLaunch)this.this$0.mStateMachine.getUserSetting().get(UserSettingKey.PREDICTIVE_LAUNCH)).doCapture()) {
                this.this$0.mViewFinder.hidePredictiveLaunchCover(ViewFinderImpl.PredictiveLaunchHideTrigger.TOUCH_UP_CAPTURE);
                if (this.this$0.mViewFinder.closeAutoReviewIfShowing()) {
                    return;
                }
                if (this.this$0.mAngleActionHandler.stopZooming()) {
                    return;
                }
                if (this.this$0.mViewFinder.closeSettingDialogIfOpened() || !this.this$0.mViewFinder.isUserOperable() || this.this$0.mViewFinder.isTutorialOpened()) {
                    return;
                }
                if (!this.this$0.isCurrentStorageWritable()) {
                    return;
                }
                ResearchUtil.getInstance().setCaptureTrigger(Event.CaptureTrigger.TOUCH_CAPTURE_PREDICTIVE_LAUNCH);
                LocalResearchUtil.getInstance().setPredictiveLaunchState(true);
                this.this$0.notifyShutterKeyEvent(false);
            }
            else {
                this.this$0.mViewFinder.hidePredictiveLaunchCover(ViewFinderImpl.PredictiveLaunchHideTrigger.TOUCH_UP);
            }
        }
    }
    
    private class SelfTimerCancelButtonProcedure extends TouchEventProcedure
    {
        final UserEventHandler this$0;
        
        private SelfTimerCancelButtonProcedure(final UserEventHandler this$0) {
        }
        
        public void doTouchUp(final Point point) {
            this.this$0.mViewFinder.clearTouchedScreenButtonGroup();
            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SELFTIMER_CANCEL, new Object[0]);
        }
    }
    
    public static class SideTouchEventDispatcher extends EventDispatcher
    {
        @Override
        protected void reset() {
            ((EventDispatcher)this).getHandler().resetSideTouchEventHandling();
        }
        
        public boolean send(final MotionEvent motionEvent) {
            if (((EventDispatcher)this).getHandler() != null && (((EventDispatcher)this).isRunning() || motionEvent.getAction() == 3)) {
                if (CamLog.DEBUG) {
                    CamLog.d("SideTouchEventDispatcher#send()");
                }
                return ((EventDispatcher)this).getHandler().dispatchSideTouchEvent(motionEvent);
            }
            CamLog.i("SideTouchEventDispatcher#send() event is rejected.");
            return false;
        }
    }
    
    private enum SideTouchEventSource implements EventSource
    {
        private static final SideTouchEventSource[] $VALUES;
        
        SIDE_SENSOR;
        
        static {
            $VALUES = new SideTouchEventSource[] { SideTouchEventSource.SIDE_SENSOR };
        }
    }
    
    private class StopSlowMotionRecordingButtonProcedure extends TouchEventProcedure
    {
        final UserEventHandler this$0;
        
        private StopSlowMotionRecordingButtonProcedure(final UserEventHandler this$0) {
        }
        
        public void doTouchUp(final Point point) {
            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_STOP_RECORDING_SLOW_MOTION_BUTTON_RELEASE, new Object[0]);
        }
    }
    
    private class SuperSlowTriggerButtonProcedure extends VideoRecordingButtonProcedure
    {
        final UserEventHandler this$0;
        
        private SuperSlowTriggerButtonProcedure(final UserEventHandler this$0) {
        }
        
        @Override
        public void doTouchUp(final Point point) {
            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_TRIGGER_SLOW_MOTION, new Object[0]);
        }
    }
    
    private class VideoRecordingButtonProcedure extends TouchEventProcedure
    {
        final UserEventHandler this$0;
        
        private VideoRecordingButtonProcedure(final UserEventHandler this$0) {
        }
        
        public void doCancel() {
            this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_CANCEL, new Object[0]);
        }
        
        public void doTouchDown() {
            this.this$0.notifyEventReady();
        }
        
        public void doTouchUp(final Point point) {
            StateMachine.TransitterEvent transitterEvent;
            if (this.this$0.mStateMachine.isRecording()) {
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.VIDEO_RECORDING_STOP_READY_FOR_USE);
                transitterEvent = StateMachine.TransitterEvent.EVENT_STOP_RECORDING;
            }
            else {
                transitterEvent = StateMachine.TransitterEvent.EVENT_START_RECORDING;
            }
            this.this$0.mStateMachine.sendEvent(transitterEvent, new Object[0]);
        }
    }
    
    private class SuperSlowVideoCaptureAreaEventProcedure extends VideoCaptureAreaEventProcedure
    {
        final UserEventHandler this$0;
        
        private SuperSlowVideoCaptureAreaEventProcedure(final UserEventHandler this$0) {
        }
        
        @Override
        public void doTouchUp(final Point point) {
            if (!this.this$0.mAngleActionHandler.isHandling() && this.this$0.isTouchCaptureEnabled()) {
                if (this.this$0.mStateMachine.isVideoRecording()) {
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_TRIGGER_SLOW_MOTION, new Object[0]);
                }
                else {
                    this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_RECORDING, new Object[0]);
                }
            }
            else {
                super.doTouchUp(point);
            }
        }
    }
    
    private class VideoCaptureAreaEventProcedure extends FrontVideoCaptureAreaEventProcedure
    {
        final UserEventHandler this$0;
        
        private VideoCaptureAreaEventProcedure(final UserEventHandler this$0) {
        }
        
        public void doTouchAreaScaleReady() {
            this.this$0.mAngleActionHandler.preparePinchZooming();
        }
        
        public void doTouchAreaScaling(final float n) {
            this.this$0.mAngleActionHandler.performPinchZooming(n);
        }
        
        @Override
        public void doTouchUp(final Point selectedObjectPositionToDeviceAndViewFinder) {
            if (this.this$0.mViewFinder.isAutoReviewShowing()) {
                this.this$0.mViewFinder.hideAutoReview();
                return;
            }
            if (this.this$0.mAngleActionHandler.stopZooming()) {
                return;
            }
            if (this.this$0.canObjectTracking()) {
                ((CaptureAreaEventProcedure)this).setSelectedObjectPositionToDeviceAndViewFinder(selectedObjectPositionToDeviceAndViewFinder);
            }
            if (this.this$0.isTouchCaptureEnabled()) {
                this.this$0.mStateMachine.sendEvent(this.this$0.selectDefaultVideoAction(), new Object[0]);
            }
        }
    }
    
    public static class TouchEventDispatcher extends EventDispatcher
    {
        @Override
        protected void reset() {
            ((EventDispatcher)this).getHandler().resetTouchEventHandling();
        }
        
        public void sendCancel(final TouchEventSource touchEventSource) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("TouchEventDispatcher#sendCancel() source:");
                    sb.append(touchEventSource.toString());
                    CamLog.d(sb.toString());
                }
                ((EventDispatcher)this).getHandler().dispatchTouchCancel(touchEventSource);
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("TouchEventDispatcher#sendCancel() event is rejected. source:");
                sb2.append(touchEventSource.toString());
                CamLog.i(sb2.toString());
            }
        }
        
        public void sendCaptureAreaScaleReady(final TouchEventSource touchEventSource) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("TouchEventDispatcher#sendCaptureAreaScaleReady() source:");
                    sb.append(touchEventSource.toString());
                    CamLog.d(sb.toString());
                }
                ((EventDispatcher)this).getHandler().dispatchCaptureAreaScaleReady(touchEventSource);
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("TouchEventDispatcher#sendCaptureAreaScaleReady() event is rejected.source:");
                sb2.append(touchEventSource.toString());
                CamLog.i(sb2.toString());
            }
        }
        
        public void sendCaptureAreaScaling(final TouchEventSource touchEventSource, final float n) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("TouchEventDispatcher#sendCaptureAreaScaling() source:");
                    sb.append(touchEventSource.toString());
                    CamLog.d(sb.toString());
                }
                ((EventDispatcher)this).getHandler().dispatchCaptureAreaScaling(touchEventSource, n);
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("TouchEventDispatcher#sendCaptureAreaScaling() event is rejected. source:");
                sb2.append(touchEventSource.toString());
                CamLog.i(sb2.toString());
            }
        }
        
        public void sendClick(final TouchEventSource touchEventSource, final Point point) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("TouchEventDispatcher#sendClick() source:");
                    sb.append(touchEventSource.toString());
                    CamLog.d(sb.toString());
                }
                ((EventDispatcher)this).getHandler().dispatchClick(touchEventSource, point);
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("TouchEventDispatcher#sendClick() event is rejected. source:");
                sb2.append(touchEventSource.toString());
                CamLog.i(sb2.toString());
            }
        }
        
        public void sendLongClick(final TouchEventSource touchEventSource, final Point point) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("TouchEventDispatcher#sendLongClick() source:");
                    sb.append(touchEventSource.toString());
                    CamLog.d(sb.toString());
                }
                ((EventDispatcher)this).getHandler().dispatchLongClick(touchEventSource, point);
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("TouchEventDispatcher#sendLongClick() event is rejected. source:");
                sb2.append(touchEventSource.toString());
                CamLog.i(sb2.toString());
            }
        }
        
        public void sendTouchDown(final TouchEventSource touchEventSource) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("TouchEventDispatcher#sendTouchDown() source:");
                    sb.append(touchEventSource.toString());
                    CamLog.d(sb.toString());
                }
                ((EventDispatcher)this).getHandler().dispatchTouchDown(touchEventSource);
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("TouchEventDispatcher#sendTouchDown() event is rejected. source:");
                sb2.append(touchEventSource.toString());
                CamLog.i(sb2.toString());
            }
        }
        
        public void sendTouchUp(final TouchEventSource touchEventSource, final Point point) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("TouchEventDispatcher#sendTouchUp() source:");
                    sb.append(touchEventSource.toString());
                    CamLog.d(sb.toString());
                }
                ((EventDispatcher)this).getHandler().dispatchTouchUp(touchEventSource, point);
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("TouchEventDispatcher#sendTouchUp() event is rejected. source:");
                sb2.append(touchEventSource.toString());
                CamLog.i(sb2.toString());
            }
        }
    }
    
    private class TouchEventProcedureManager
    {
        private final Map<TouchEventSource, TouchEventProcedure> mProcedures;
        final UserEventHandler this$0;
        
        public TouchEventProcedureManager(final UserEventHandler this$0) {
            this.this$0 = this$0;
            this.mProcedures = new HashMap<TouchEventSource, TouchEventProcedure>();
            this.register(new CaptureButtonProcedure(), OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE, OnScreenButtonItemFactory.ButtonType.CAPTURE_SMALL, OnScreenButtonItemFactory.ButtonType.CAPTURE_LARGE, OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER, OnScreenButtonItemFactory.ButtonType.CAPTURE_WITH_SELFTIMER_SHORT, OnScreenButtonItemFactory.ButtonType.CAPTURE_WITH_SELFTIMER_LONG, OnScreenButtonItemFactory.ButtonType.CAPTURE_WITH_SELFTIMER_LARGE, OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER_SHORT, OnScreenButtonItemFactory.ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER_LONG, OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_SNAPSHOT_RECORDING);
            this.register(new StopSlowMotionRecordingButtonProcedure(), OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_SMALL);
            this.register(new SelfTimerCancelButtonProcedure(), OnScreenButtonItemFactory.ButtonType.CANCEL_SELFTIMER_LARGE, OnScreenButtonItemFactory.ButtonType.CANCEL_SELFTIMER_SIDE);
            this.register(new VideoRecordingButtonProcedure(), OnScreenButtonItemFactory.ButtonType.TOUCH_RECORDING_START, OnScreenButtonItemFactory.ButtonType.START_RECORDING_LARGE, OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_LARGE, OnScreenButtonItemFactory.ButtonType.STOP_RECORDING_IN_PAUSE_LARGE, OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_START_RECORDING, OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING, OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING_IN_PAUSE);
            this.register(new SuperSlowTriggerButtonProcedure(), OnScreenButtonItemFactory.ButtonType.TRIGGER_SUPER_SLOW_MOTION);
            this.register(this$0.new VideoRecordingCamcordButtonProcedure(false), OnScreenButtonItemFactory.ButtonType.RESUME_RECORDING_SMALL, OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_RESUME_RECORDING);
            this.register(this$0.new VideoRecordingCamcordButtonProcedure(true), OnScreenButtonItemFactory.ButtonType.PAUSE_RECORDING_SMALL, OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_PAUSE_RECORDING);
            this.register(new CaptureAreaTouchEventProcedureSelector(), UiComponent.CAPTURE_AREA);
            this.register(new AngleChangeButtonProcedure(), UiComponent.ANGLE_CHANGE_BUTTON);
            this.register(new PredictiveLaunchCoverProcedure(), UiComponent.PREDICTIVE_LAUNCH_COVER);
        }
        
        private void register(final TouchEventProcedure touchEventProcedure, final TouchEventSource... array) {
            for (int length = array.length, i = 0; i < length; ++i) {
                this.mProcedures.put(array[i], touchEventProcedure);
            }
        }
        
        public TouchEventProcedure find(final TouchEventSource touchEventSource) {
            final TouchEventProcedure touchEventProcedure = this.mProcedures.get(touchEventSource);
            if (CamLog.DEBUG) {
                if (touchEventProcedure != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("find(");
                    sb.append(touchEventSource.toString());
                    sb.append(") TouchEventProcedure:");
                    sb.append(touchEventProcedure.getClass().getSimpleName());
                    CamLog.d(sb.toString());
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("find(");
                    sb2.append(touchEventSource.toString());
                    sb2.append(") TouchEventProcedure:not found");
                    CamLog.d(sb2.toString());
                }
            }
            return touchEventProcedure;
        }
    }
    
    public interface TouchEventSource extends EventSource
    {
    }
    
    public enum UiComponent implements TouchEventSource
    {
        private static final UiComponent[] $VALUES;
        
        ANGLE_CHANGE_BUTTON, 
        CAPTURE_AREA, 
        PREDICTIVE_LAUNCH_COVER;
        
        static {
            $VALUES = new UiComponent[] { UiComponent.CAPTURE_AREA, UiComponent.ANGLE_CHANGE_BUTTON, UiComponent.PREDICTIVE_LAUNCH_COVER };
        }
    }
    
    private class VideoRecordingCamcordButtonProcedure extends VideoRecordingButtonProcedure
    {
        private final boolean mIsPauseButton;
        final UserEventHandler this$0;
        
        public VideoRecordingCamcordButtonProcedure(final UserEventHandler this$0, final boolean mIsPauseButton) {
            this.mIsPauseButton = mIsPauseButton;
        }
        
        @Override
        public void doCancel() {
        }
        
        @Override
        public void doTouchDown() {
        }
        
        @Override
        public void doTouchUp(final Point point) {
            final StateMachine access$3400 = this.this$0.mStateMachine;
            StateMachine.TransitterEvent transitterEvent;
            if (this.mIsPauseButton) {
                transitterEvent = StateMachine.TransitterEvent.EVENT_PAUSE_RECORDING;
            }
            else {
                transitterEvent = StateMachine.TransitterEvent.EVENT_RESUME_RECORDING;
            }
            access$3400.sendEvent(transitterEvent, new Object[0]);
        }
    }
    
    public enum VirtualKeyEvent
    {
        private static final VirtualKeyEvent[] $VALUES;
        
        SMILE_CAPTURE;
        
        static {
            $VALUES = new VirtualKeyEvent[] { VirtualKeyEvent.SMILE_CAPTURE };
        }
    }
    
    public static class VirtualKeyEventDispatcher extends EventDispatcher
    {
        public boolean sendVirtualKeyEvent(final VirtualKeyEvent virtualKeyEvent) {
            if (((EventDispatcher)this).getHandler() != null && ((EventDispatcher)this).isRunning()) {
                if (CamLog.DEBUG) {
                    CamLog.d("VirtualKeyEventDispatcher#sendVirtualKeyEvent()");
                }
                return ((EventDispatcher)this).getHandler().dispatchVirtualKeyEvent(virtualKeyEvent);
            }
            CamLog.i("VirtualKeyEventDispatcher#sendVirtualKeyEvent() event is rejected.");
            return false;
        }
    }
}
