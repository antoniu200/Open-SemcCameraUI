// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import com.sonyericsson.android.camera.setting.SettingsFactory;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.android.camera.controller.VibrationManager;
import android.os.PowerManager;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import java.util.TimerTask;
import android.widget.Toast;
import android.content.Intent;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.setting.LastSettings;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Timer;
import android.content.BroadcastReceiver;

public class CameraButtonIntentReceiver extends BroadcastReceiver
{
    private static final int CAMERA_DEVICE_AUTO_RELEASE_TIMER_DURATION = 5000;
    private static final String NORMAL_LAUNCH_FAST_CAPTURE_START_SUBJECT = "start";
    private static final long START_UP_WAKE_LOCK_DURATION_MILLIS = 1000L;
    private static final String TAG = "CameraButtonIntentReceiver";
    private static ReceiverState sCurrentState;
    private static IntentKind sLatestIntent;
    private static Timer sReleaseTimer;
    private static final Object sReleaseTimerLock;
    
    static {
        sReleaseTimerLock = new Object();
        CameraButtonIntentReceiver.sCurrentState = ReceiverState.IDLE;
        CameraButtonIntentReceiver.sLatestIntent = IntentKind.NULL;
    }
    
    private static void changeStateTo(final ReceiverState receiverState) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("changeTo:");
            sb.append(receiverState);
            sb.append(" from:");
            sb.append(CameraButtonIntentReceiver.sCurrentState);
            CamLog.d(sb.toString());
        }
        CameraButtonIntentReceiver.sCurrentState = receiverState;
    }
    
    @SuppressLint({ "NewApi" })
    private boolean isInLockTaskMode(final Context context) {
        return ((ActivityManager)context.getSystemService("activity")).getLockTaskModeState() != 0;
    }
    
    private boolean isQuickLaunchValid(final LastSettings lastSettings) {
        return lastSettings.getFastCapture() != FastCapture.OFF;
    }
    
    private void onCancelReceived(final Context context) {
        ((CameraApplication)context.getApplicationContext()).getCameraDevice().closeCamera();
        releaseCameraDeviceReleaseTimer();
    }
    
    private void onDirectStartReceived(final Context context, final String s, final LastSettings lastSettings) {
        final FastCapture fastCapture = lastSettings.getFastCapture();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            ((CameraApplication)context.getApplicationContext()).getCameraDevice().preloadCamera(context, null, CapturingMode.SCENE_RECOGNITION, true);
        }
        final Intent intent = new Intent();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            intent.setAction("com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH_AND_CAPTURE");
        }
        else {
            intent.setAction("com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH");
        }
        intent.setClass(context, (Class)CameraActivityOnLockScreen.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", s);
        intent.putExtra("com.sonyericsson.android.camera.extra.launchTrigger", LaunchCondition.LaunchTrigger.LOCK_SCREEN.toString());
        context.startActivity(intent);
    }
    
    private void onNullReceived(final Context context, final LastSettings lastSettings) {
        final FastCapture fastCapture = lastSettings.getFastCapture();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            ((CameraApplication)context.getApplicationContext()).getCameraDevice().preloadCamera(context, null, CapturingMode.SCENE_RECOGNITION, true);
        }
        final Intent intent = new Intent();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            intent.setAction("com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH_AND_CAPTURE");
        }
        else {
            intent.setAction("com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH");
        }
        intent.setClass(context, (Class)CameraActivityOnLockScreen.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", "start");
        intent.putExtra("com.sonyericsson.android.camera.extra.launchTrigger", LaunchCondition.LaunchTrigger.HW_CAMERA_KEY.toString());
        context.startActivity(intent);
    }
    
    private void onPrepareReceived(final Context context, final LastSettings lastSettings) {
        final Intent intent = new Intent("com.sonymobile.cameracommon.intent.ACTION_FORCE_EXIT_REQUEST");
        intent.setFlags(268435456);
        context.sendBroadcast(intent);
        if (lastSettings.getFastCapture() == FastCapture.LAUNCH_AND_CAPTURE) {
            ((CameraApplication)context.getApplicationContext()).getCameraDevice().preloadCamera(context, null, CapturingMode.SCENE_RECOGNITION, true);
            startCameraDeviceReleaseTimer(context);
        }
    }
    
    private void onStartReceived(final Context context, final String s, final LastSettings lastSettings) {
        final FastCapture fastCapture = lastSettings.getFastCapture();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            ((CameraApplication)context.getApplicationContext()).getCameraDevice().preloadCamera(context, null, CapturingMode.SCENE_RECOGNITION, true);
        }
        final Intent intent = new Intent();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            intent.setAction("com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH_AND_CAPTURE");
        }
        else {
            intent.setAction("com.sonyericsson.android.camera.intent.action.QUICK_LAUNCH");
        }
        intent.setClass(context, (Class)CameraActivityOnLockScreen.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", s);
        intent.putExtra("com.sonyericsson.android.camera.extra.launchTrigger", LaunchCondition.LaunchTrigger.HW_CAMERA_KEY_LOCK.toString());
        context.startActivity(intent);
    }
    
    public static final void preload() {
    }
    
    public static void releaseCameraDeviceReleaseTimer() {
        synchronized (CameraButtonIntentReceiver.class) {
            if (CamLog.DEBUG) {
                CamLog.d("Cancel camera release due to timeout.");
            }
            synchronized (CameraButtonIntentReceiver.sReleaseTimerLock) {
                if (CameraButtonIntentReceiver.sReleaseTimer != null) {
                    CameraButtonIntentReceiver.sReleaseTimer.cancel();
                    CameraButtonIntentReceiver.sReleaseTimer.purge();
                    CameraButtonIntentReceiver.sReleaseTimer = null;
                }
            }
        }
    }
    
    private static void setLatestIntent(final IntentKind sLatestIntent) {
        CameraButtonIntentReceiver.sLatestIntent = sLatestIntent;
    }
    
    private void showScreenPinnedToastMessage(final Context context) {
        if (this.isInLockTaskMode(context)) {
            Toast.makeText(context, 2131690093, 0).show();
        }
    }
    
    private static void startCameraDeviceReleaseTimer(final Context context) {
        synchronized (CameraButtonIntentReceiver.class) {
            if (CamLog.DEBUG) {
                CamLog.d("Schedule camera release due to timeout.");
            }
            synchronized (CameraButtonIntentReceiver.sReleaseTimerLock) {
                if (CameraButtonIntentReceiver.sReleaseTimer == null) {
                    (CameraButtonIntentReceiver.sReleaseTimer = new Timer(true)).schedule(new CameraDeviceReleaseTimerTask(context), 5000L);
                }
            }
        }
    }
    
    private void startMeasurement(final Context context, final IntentKind intentKind) {
        switch (CameraButtonIntentReceiver$1.$SwitchMap$com$sonyericsson$android$camera$CameraButtonIntentReceiver$IntentKind[intentKind.ordinal()]) {
            case 3:
            case 4: {
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
                break;
            }
            case 2: {
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
                break;
            }
            case 1: {
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
                break;
            }
        }
    }
    
    private void wakeUpAndVibrateOnLaunch(final Context context) {
        ((PowerManager)context.getSystemService("power")).newWakeLock(268435482, "CameraButtonIntentReceiver").acquire(1000L);
        VibrationManager.vibrate(context, VibrationManager.VibrationPattern.EFFECT_STANDARD);
    }
    
    public final void onReceive(final Context context, final Intent intent) {
        synchronized (this) {
            if (this.isOrderedBroadcast()) {
                this.abortBroadcast();
                if (CamLog.DEBUG) {
                    CamLog.d("Intent has been aborted.");
                }
            }
            final Intent intent2 = new Intent("android.intent.action.MAIN");
            intent2.setClass(context, (Class)CameraActivity.class);
            if (!CommonUtility.isActivityAvailable(context, intent2)) {
                CamLog.i("Camera is disabled, so the request to start camera is refused.");
                return;
            }
            final String stringExtra = intent.getStringExtra("android.intent.extra.SUBJECT");
            if ("prepare".equals(stringExtra)) {
                PerfLog.FAST_CAMERA_BUTTON_INTENT_RECEIVED.transit();
                setLatestIntent(IntentKind.PREPARE);
            }
            else if ("start".equals(stringExtra)) {
                setLatestIntent(IntentKind.START);
            }
            else if ("start-secure".equals(stringExtra)) {
                setLatestIntent(IntentKind.START_SECURE);
            }
            else if ("cancel".equals(stringExtra)) {
                setLatestIntent(IntentKind.CANCEL);
            }
            else if ("activity-resumed".equals(stringExtra)) {
                setLatestIntent(IntentKind.ACTIVITY_RESUMED);
            }
            else if ("activity-paused".equals(stringExtra)) {
                setLatestIntent(IntentKind.ACTIVITY_PAUSED);
            }
            else {
                setLatestIntent(IntentKind.NULL);
            }
            final LastSettings lastSettings = SettingsFactory.create(context, ((CameraApplication)context.getApplicationContext()).getStorage()).getLastSettings();
            final StringBuilder sb = new StringBuilder();
            sb.append("Receive intent for camera. kind:");
            sb.append(CameraButtonIntentReceiver.sLatestIntent);
            sb.append(" state:");
            sb.append(CameraButtonIntentReceiver.sCurrentState);
            sb.append(" lastMode:");
            sb.append(lastSettings.getFastCapture());
            sb.append(" inLockMode:");
            sb.append(this.isInLockTaskMode(context));
            CamLog.i(sb.toString());
            Label_1249: {
                switch (CameraButtonIntentReceiver$1.$SwitchMap$com$sonyericsson$android$camera$CameraButtonIntentReceiver$ReceiverState[CameraButtonIntentReceiver.sCurrentState.ordinal()]) {
                    case 4: {
                        switch (CameraButtonIntentReceiver$1.$SwitchMap$com$sonyericsson$android$camera$CameraButtonIntentReceiver$IntentKind[CameraButtonIntentReceiver.sLatestIntent.ordinal()]) {
                            default: {
                                break Label_1249;
                            }
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5: {
                                break Label_1249;
                            }
                            case 7: {
                                changeStateTo(ReceiverState.IDLE);
                                break Label_1249;
                            }
                            case 6: {
                                releaseCameraDeviceReleaseTimer();
                                break Label_1249;
                            }
                        }
                        break;
                    }
                    case 3: {
                        switch (CameraButtonIntentReceiver$1.$SwitchMap$com$sonyericsson$android$camera$CameraButtonIntentReceiver$IntentKind[CameraButtonIntentReceiver.sLatestIntent.ordinal()]) {
                            default: {
                                break Label_1249;
                            }
                            case 4: {
                                releaseCameraDeviceReleaseTimer();
                                if (!this.isQuickLaunchValid(lastSettings)) {
                                    return;
                                }
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                    break Label_1249;
                                }
                                this.onDirectStartReceived(context, stringExtra, lastSettings);
                                this.startMeasurement(context, CameraButtonIntentReceiver.sLatestIntent);
                                break Label_1249;
                            }
                            case 3: {
                                releaseCameraDeviceReleaseTimer();
                                if (!this.isQuickLaunchValid(lastSettings)) {
                                    return;
                                }
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                    break Label_1249;
                                }
                                this.onDirectStartReceived(context, stringExtra, lastSettings);
                                this.startMeasurement(context, CameraButtonIntentReceiver.sLatestIntent);
                                break Label_1249;
                            }
                            case 2: {
                                releaseCameraDeviceReleaseTimer();
                                if (!this.isQuickLaunchValid(lastSettings)) {
                                    return;
                                }
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                    break Label_1249;
                                }
                                changeStateTo(ReceiverState.PREPARE);
                                this.onPrepareReceived(context, lastSettings);
                                this.startMeasurement(context, CameraButtonIntentReceiver.sLatestIntent);
                                break Label_1249;
                            }
                            case 1: {
                                releaseCameraDeviceReleaseTimer();
                                if (!this.isQuickLaunchValid(lastSettings)) {
                                    return;
                                }
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                    break Label_1249;
                                }
                                changeStateTo(ReceiverState.IDLE);
                                this.onNullReceived(context, lastSettings);
                                this.startMeasurement(context, CameraButtonIntentReceiver.sLatestIntent);
                                break Label_1249;
                            }
                            case 5: {
                                break Label_1249;
                            }
                            case 7: {
                                changeStateTo(ReceiverState.IDLE);
                                break Label_1249;
                            }
                            case 6: {
                                changeStateTo(ReceiverState.ACTIVE);
                                break Label_1249;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (CameraButtonIntentReceiver$1.$SwitchMap$com$sonyericsson$android$camera$CameraButtonIntentReceiver$IntentKind[CameraButtonIntentReceiver.sLatestIntent.ordinal()]) {
                            default: {
                                break Label_1249;
                            }
                            case 4: {
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                    break Label_1249;
                                }
                                changeStateTo(ReceiverState.STARTING);
                                this.onStartReceived(context, stringExtra, lastSettings);
                                this.wakeUpAndVibrateOnLaunch(context);
                                break Label_1249;
                            }
                            case 3: {
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                    break Label_1249;
                                }
                                changeStateTo(ReceiverState.STARTING);
                                this.onStartReceived(context, stringExtra, lastSettings);
                                this.wakeUpAndVibrateOnLaunch(context);
                                break Label_1249;
                            }
                            case 1:
                            case 2:
                            case 7: {
                                break Label_1249;
                            }
                            case 6: {
                                releaseCameraDeviceReleaseTimer();
                                changeStateTo(ReceiverState.ACTIVE);
                                startCameraDeviceReleaseTimer(context);
                                break Label_1249;
                            }
                            case 5: {
                                changeStateTo(ReceiverState.IDLE);
                                this.onCancelReceived(context);
                                break Label_1249;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (CameraButtonIntentReceiver$1.$SwitchMap$com$sonyericsson$android$camera$CameraButtonIntentReceiver$IntentKind[CameraButtonIntentReceiver.sLatestIntent.ordinal()]) {
                            default: {
                                break Label_1249;
                            }
                            case 4: {
                                if (!this.isQuickLaunchValid(lastSettings)) {
                                    return;
                                }
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                }
                                else {
                                    changeStateTo(ReceiverState.STARTING);
                                    this.onDirectStartReceived(context, stringExtra, lastSettings);
                                    this.startMeasurement(context, CameraButtonIntentReceiver.sLatestIntent);
                                }
                                this.showScreenPinnedToastMessage(context);
                                break Label_1249;
                            }
                            case 3: {
                                if (!this.isQuickLaunchValid(lastSettings)) {
                                    return;
                                }
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                }
                                else {
                                    changeStateTo(ReceiverState.STARTING);
                                    this.onDirectStartReceived(context, stringExtra, lastSettings);
                                    this.startMeasurement(context, CameraButtonIntentReceiver.sLatestIntent);
                                }
                                this.showScreenPinnedToastMessage(context);
                                break Label_1249;
                            }
                            case 2: {
                                if (!this.isQuickLaunchValid(lastSettings)) {
                                    return;
                                }
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                    break Label_1249;
                                }
                                changeStateTo(ReceiverState.PREPARE);
                                this.onPrepareReceived(context, lastSettings);
                                this.startMeasurement(context, CameraButtonIntentReceiver.sLatestIntent);
                                break Label_1249;
                            }
                            case 1: {
                                if (!this.isQuickLaunchValid(lastSettings)) {
                                    return;
                                }
                                if (this.isInLockTaskMode(context)) {
                                    changeStateTo(ReceiverState.IDLE);
                                    setLatestIntent(IntentKind.NULL);
                                    break Label_1249;
                                }
                                this.onNullReceived(context, lastSettings);
                                this.startMeasurement(context, CameraButtonIntentReceiver.sLatestIntent);
                                break Label_1249;
                            }
                            case 5:
                            case 7: {
                                break Label_1249;
                            }
                            case 6: {
                                changeStateTo(ReceiverState.ACTIVE);
                                break Label_1249;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
    
    private static class CameraDeviceReleaseTimerTask extends TimerTask
    {
        private final Context mContext;
        
        private CameraDeviceReleaseTimerTask(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is released due to timeout.");
            }
            synchronized (CameraButtonIntentReceiver.sReleaseTimerLock) {
                if (CameraButtonIntentReceiver.sReleaseTimer != null) {
                    CameraButtonIntentReceiver.sReleaseTimer.cancel();
                    CameraButtonIntentReceiver.sReleaseTimer.purge();
                    CameraButtonIntentReceiver.sReleaseTimer = null;
                }
                monitorexit(CameraButtonIntentReceiver.sReleaseTimerLock);
                changeStateTo(ReceiverState.IDLE);
                ((CameraApplication)this.mContext.getApplicationContext()).getCameraDevice().closeCamera();
            }
        }
    }
    
    private enum IntentKind
    {
        private static final IntentKind[] $VALUES;
        
        ACTIVITY_PAUSED, 
        ACTIVITY_RESUMED, 
        CANCEL, 
        NULL, 
        PREPARE, 
        START, 
        START_SECURE;
        
        static {
            $VALUES = new IntentKind[] { IntentKind.NULL, IntentKind.PREPARE, IntentKind.START, IntentKind.START_SECURE, IntentKind.CANCEL, IntentKind.ACTIVITY_RESUMED, IntentKind.ACTIVITY_PAUSED };
        }
    }
    
    private enum ReceiverState
    {
        private static final ReceiverState[] $VALUES;
        
        ACTIVE, 
        IDLE, 
        PREPARE, 
        STARTING;
        
        static {
            $VALUES = new ReceiverState[] { ReceiverState.IDLE, ReceiverState.PREPARE, ReceiverState.STARTING, ReceiverState.ACTIVE };
        }
    }
}
