package com.sonyericsson.android.camera;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.controller.VibrationManager;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.LastSettings;
import com.sonyericsson.android.camera.setting.SettingsFactory;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;
import com.sonyericsson.cameracommon.constants.CommonConstants;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraButtonIntentReceiver extends BroadcastReceiver {
    private static final int CAMERA_DEVICE_AUTO_RELEASE_TIMER_DURATION = 5000;
    private static final String NORMAL_LAUNCH_FAST_CAPTURE_START_SUBJECT = "start";
    private static final long START_UP_WAKE_LOCK_DURATION_MILLIS = 1000;
    private static final String TAG = "CameraButtonIntentReceiver";
    private static Timer sReleaseTimer;
    private static final Object sReleaseTimerLock = new Object();
    private static ReceiverState sCurrentState = ReceiverState.IDLE;
    private static IntentKind sLatestIntent = IntentKind.NULL;

    private enum IntentKind {
        NULL,
        PREPARE,
        START,
        START_SECURE,
        CANCEL,
        ACTIVITY_RESUMED,
        ACTIVITY_PAUSED
    }

    private enum ReceiverState {
        IDLE,
        PREPARE,
        STARTING,
        ACTIVE
    }

    public static final void preload() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void changeStateTo(ReceiverState receiverState) {
        if (CamLog.DEBUG) {
            CamLog.d("changeTo:" + receiverState + " from:" + sCurrentState);
        }
        sCurrentState = receiverState;
    }

    private static void setLatestIntent(IntentKind intentKind) {
        sLatestIntent = intentKind;
    }

    @Override // android.content.BroadcastReceiver
    public final synchronized void onReceive(Context context, Intent intent) {
        if (isOrderedBroadcast()) {
            abortBroadcast();
            if (CamLog.DEBUG) {
                CamLog.d("Intent has been aborted.");
            }
        }
        Intent intent2 = new Intent("android.intent.action.MAIN");
        intent2.setClass(context, CameraActivity.class);
        if (!CommonUtility.isActivityAvailable(context, intent2)) {
            CamLog.i("Camera is disabled, so the request to start camera is refused.");
            return;
        }
        String stringExtra = intent.getStringExtra("android.intent.extra.SUBJECT");
        if (CameraActivity.INTENT_SUBJECT_PREPARE.equals(stringExtra)) {
            PerfLog.FAST_CAMERA_BUTTON_INTENT_RECEIVED.transit();
            setLatestIntent(IntentKind.PREPARE);
        } else if ("start".equals(stringExtra)) {
            setLatestIntent(IntentKind.START);
        } else if (CameraActivity.INTENT_SUBJECT_START_SECURE.equals(stringExtra)) {
            setLatestIntent(IntentKind.START_SECURE);
        } else if (CameraActivity.INTENT_SUBJECT_CANCEL.equals(stringExtra)) {
            setLatestIntent(IntentKind.CANCEL);
        } else if (CameraActivity.INTENT_SUBJECT_RESUMED.equals(stringExtra)) {
            setLatestIntent(IntentKind.ACTIVITY_RESUMED);
        } else if (CameraActivity.INTENT_SUBJECT_PAUSED.equals(stringExtra)) {
            setLatestIntent(IntentKind.ACTIVITY_PAUSED);
        } else {
            setLatestIntent(IntentKind.NULL);
        }
        LastSettings lastSettings = SettingsFactory.create(context, ((CameraApplication) context.getApplicationContext()).getStorage()).getLastSettings();
        CamLog.i("Receive intent for camera. kind:" + sLatestIntent + " state:" + sCurrentState + " lastMode:" + lastSettings.getFastCapture() + " inLockMode:" + isInLockTaskMode(context));
        switch (sCurrentState) {
            case IDLE:
                switch (sLatestIntent) {
                    case NULL:
                        if (isQuickLaunchValid(lastSettings)) {
                            if (isInLockTaskMode(context)) {
                                changeStateTo(ReceiverState.IDLE);
                                setLatestIntent(IntentKind.NULL);
                                break;
                            } else {
                                onNullReceived(context, lastSettings);
                                startMeasurement(context, sLatestIntent);
                                break;
                            }
                        } else {
                            return;
                        }
                    case PREPARE:
                        if (isQuickLaunchValid(lastSettings)) {
                            if (isInLockTaskMode(context)) {
                                changeStateTo(ReceiverState.IDLE);
                                setLatestIntent(IntentKind.NULL);
                                break;
                            } else {
                                changeStateTo(ReceiverState.PREPARE);
                                onPrepareReceived(context, lastSettings);
                                startMeasurement(context, sLatestIntent);
                                break;
                            }
                        } else {
                            return;
                        }
                    case START:
                        if (isQuickLaunchValid(lastSettings)) {
                            if (isInLockTaskMode(context)) {
                                changeStateTo(ReceiverState.IDLE);
                                setLatestIntent(IntentKind.NULL);
                            } else {
                                changeStateTo(ReceiverState.STARTING);
                                onDirectStartReceived(context, stringExtra, lastSettings);
                                startMeasurement(context, sLatestIntent);
                            }
                            showScreenPinnedToastMessage(context);
                            break;
                        } else {
                            return;
                        }
                    case START_SECURE:
                        if (isQuickLaunchValid(lastSettings)) {
                            if (isInLockTaskMode(context)) {
                                changeStateTo(ReceiverState.IDLE);
                                setLatestIntent(IntentKind.NULL);
                            } else {
                                changeStateTo(ReceiverState.STARTING);
                                onDirectStartReceived(context, stringExtra, lastSettings);
                                startMeasurement(context, sLatestIntent);
                            }
                            showScreenPinnedToastMessage(context);
                            break;
                        } else {
                            return;
                        }
                    case ACTIVITY_RESUMED:
                        changeStateTo(ReceiverState.ACTIVE);
                        break;
                }
            case PREPARE:
                switch (sLatestIntent) {
                    case START:
                        if (isInLockTaskMode(context)) {
                            changeStateTo(ReceiverState.IDLE);
                            setLatestIntent(IntentKind.NULL);
                            break;
                        } else {
                            changeStateTo(ReceiverState.STARTING);
                            onStartReceived(context, stringExtra, lastSettings);
                            wakeUpAndVibrateOnLaunch(context);
                            break;
                        }
                    case START_SECURE:
                        if (isInLockTaskMode(context)) {
                            changeStateTo(ReceiverState.IDLE);
                            setLatestIntent(IntentKind.NULL);
                            break;
                        } else {
                            changeStateTo(ReceiverState.STARTING);
                            onStartReceived(context, stringExtra, lastSettings);
                            wakeUpAndVibrateOnLaunch(context);
                            break;
                        }
                    case CANCEL:
                        changeStateTo(ReceiverState.IDLE);
                        onCancelReceived(context);
                        break;
                    case ACTIVITY_RESUMED:
                        releaseCameraDeviceReleaseTimer();
                        changeStateTo(ReceiverState.ACTIVE);
                        startCameraDeviceReleaseTimer(context);
                        break;
                }
            case STARTING:
                switch (sLatestIntent) {
                    case NULL:
                        releaseCameraDeviceReleaseTimer();
                        if (isQuickLaunchValid(lastSettings)) {
                            if (isInLockTaskMode(context)) {
                                changeStateTo(ReceiverState.IDLE);
                                setLatestIntent(IntentKind.NULL);
                                break;
                            } else {
                                changeStateTo(ReceiverState.IDLE);
                                onNullReceived(context, lastSettings);
                                startMeasurement(context, sLatestIntent);
                                break;
                            }
                        } else {
                            return;
                        }
                    case PREPARE:
                        releaseCameraDeviceReleaseTimer();
                        if (isQuickLaunchValid(lastSettings)) {
                            if (isInLockTaskMode(context)) {
                                changeStateTo(ReceiverState.IDLE);
                                setLatestIntent(IntentKind.NULL);
                                break;
                            } else {
                                changeStateTo(ReceiverState.PREPARE);
                                onPrepareReceived(context, lastSettings);
                                startMeasurement(context, sLatestIntent);
                                break;
                            }
                        } else {
                            return;
                        }
                    case START:
                        releaseCameraDeviceReleaseTimer();
                        if (isQuickLaunchValid(lastSettings)) {
                            if (isInLockTaskMode(context)) {
                                changeStateTo(ReceiverState.IDLE);
                                setLatestIntent(IntentKind.NULL);
                                break;
                            } else {
                                onDirectStartReceived(context, stringExtra, lastSettings);
                                startMeasurement(context, sLatestIntent);
                                break;
                            }
                        } else {
                            return;
                        }
                    case START_SECURE:
                        releaseCameraDeviceReleaseTimer();
                        if (isQuickLaunchValid(lastSettings)) {
                            if (isInLockTaskMode(context)) {
                                changeStateTo(ReceiverState.IDLE);
                                setLatestIntent(IntentKind.NULL);
                                break;
                            } else {
                                onDirectStartReceived(context, stringExtra, lastSettings);
                                startMeasurement(context, sLatestIntent);
                                break;
                            }
                        } else {
                            return;
                        }
                    case ACTIVITY_RESUMED:
                        changeStateTo(ReceiverState.ACTIVE);
                        break;
                    case ACTIVITY_PAUSED:
                        changeStateTo(ReceiverState.IDLE);
                        break;
                }
            case ACTIVE:
                switch (sLatestIntent) {
                    case ACTIVITY_RESUMED:
                        releaseCameraDeviceReleaseTimer();
                        break;
                    case ACTIVITY_PAUSED:
                        changeStateTo(ReceiverState.IDLE);
                        break;
                }
        }
    }

    private void wakeUpAndVibrateOnLaunch(Context context) {
        ((PowerManager) context.getSystemService("power")).newWakeLock(268435482, TAG).acquire(1000L);
        VibrationManager.vibrate(context, VibrationManager.VibrationPattern.EFFECT_STANDARD);
    }

    private void startMeasurement(Context context, IntentKind intentKind) {
        switch (intentKind) {
            case NULL:
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
                break;
            case PREPARE:
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE);
                break;
            case START:
            case START_SECURE:
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
                LocalResearchUtil.getInstance().startMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE);
                break;
        }
    }

    private boolean isQuickLaunchValid(LastSettings lastSettings) {
        return lastSettings.getFastCapture() != FastCapture.OFF;
    }

    private void showScreenPinnedToastMessage(Context context) {
        if (isInLockTaskMode(context)) {
            Toast.makeText(context, R.string.cam_strings_screen_pinned_txt, 0).show();
        }
    }

    @SuppressLint({"NewApi"})
    private boolean isInLockTaskMode(Context context) {
        return ((ActivityManager) context.getSystemService(CameraCommonProviderConstants.CapturingModeColumns.ACTIVITY)).getLockTaskModeState() != 0;
    }

    private void onNullReceived(Context context, LastSettings lastSettings) {
        FastCapture fastCapture = lastSettings.getFastCapture();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            ((CameraApplication) context.getApplicationContext()).getCameraDevice().preloadCamera(context, null, CapturingMode.SCENE_RECOGNITION, true);
        }
        Intent intent = new Intent();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            intent.setAction(LaunchCondition.ACTION_QUICK_LAUNCH_AND_CAPTURE);
        } else {
            intent.setAction(LaunchCondition.ACTION_QUICK_LAUNCH);
        }
        intent.setClass(context, CameraActivityOnLockScreen.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", "start");
        intent.putExtra(LaunchCondition.LAUNCH_TRIGGER, LaunchCondition.LaunchTrigger.HW_CAMERA_KEY.toString());
        context.startActivity(intent);
    }

    private void onPrepareReceived(Context context, LastSettings lastSettings) {
        Intent intent = new Intent(CommonConstants.INTENT_ACTION_FORCE_EXIT_REQUEST);
        intent.setFlags(268435456);
        context.sendBroadcast(intent);
        if (lastSettings.getFastCapture() == FastCapture.LAUNCH_AND_CAPTURE) {
            ((CameraApplication) context.getApplicationContext()).getCameraDevice().preloadCamera(context, null, CapturingMode.SCENE_RECOGNITION, true);
            startCameraDeviceReleaseTimer(context);
        }
    }

    private void onStartReceived(Context context, String str, LastSettings lastSettings) {
        FastCapture fastCapture = lastSettings.getFastCapture();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            ((CameraApplication) context.getApplicationContext()).getCameraDevice().preloadCamera(context, null, CapturingMode.SCENE_RECOGNITION, true);
        }
        Intent intent = new Intent();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            intent.setAction(LaunchCondition.ACTION_QUICK_LAUNCH_AND_CAPTURE);
        } else {
            intent.setAction(LaunchCondition.ACTION_QUICK_LAUNCH);
        }
        intent.setClass(context, CameraActivityOnLockScreen.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", str);
        intent.putExtra(LaunchCondition.LAUNCH_TRIGGER, LaunchCondition.LaunchTrigger.HW_CAMERA_KEY_LOCK.toString());
        context.startActivity(intent);
    }

    private void onDirectStartReceived(Context context, String str, LastSettings lastSettings) {
        FastCapture fastCapture = lastSettings.getFastCapture();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            ((CameraApplication) context.getApplicationContext()).getCameraDevice().preloadCamera(context, null, CapturingMode.SCENE_RECOGNITION, true);
        }
        Intent intent = new Intent();
        if (fastCapture == FastCapture.LAUNCH_AND_CAPTURE) {
            intent.setAction(LaunchCondition.ACTION_QUICK_LAUNCH_AND_CAPTURE);
        } else {
            intent.setAction(LaunchCondition.ACTION_QUICK_LAUNCH);
        }
        intent.setClass(context, CameraActivityOnLockScreen.class);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", str);
        intent.putExtra(LaunchCondition.LAUNCH_TRIGGER, LaunchCondition.LaunchTrigger.LOCK_SCREEN.toString());
        context.startActivity(intent);
    }

    private void onCancelReceived(Context context) {
        ((CameraApplication) context.getApplicationContext()).getCameraDevice().closeCamera();
        releaseCameraDeviceReleaseTimer();
    }

    private static synchronized void startCameraDeviceReleaseTimer(Context context) {
        if (CamLog.DEBUG) {
            CamLog.d("Schedule camera release due to timeout.");
        }
        synchronized (sReleaseTimerLock) {
            if (sReleaseTimer == null) {
                sReleaseTimer = new Timer(true);
                sReleaseTimer.schedule(new CameraDeviceReleaseTimerTask(context), 5000L);
            }
        }
    }

    public static synchronized void releaseCameraDeviceReleaseTimer() {
        if (CamLog.DEBUG) {
            CamLog.d("Cancel camera release due to timeout.");
        }
        synchronized (sReleaseTimerLock) {
            if (sReleaseTimer != null) {
                sReleaseTimer.cancel();
                sReleaseTimer.purge();
                sReleaseTimer = null;
            }
        }
    }

    private static class CameraDeviceReleaseTimerTask extends TimerTask {
        private final Context mContext;

        private CameraDeviceReleaseTimerTask(Context context) {
            this.mContext = context;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (CamLog.DEBUG) {
                CamLog.d("Camera is released due to timeout.");
            }
            synchronized (CameraButtonIntentReceiver.sReleaseTimerLock) {
                if (CameraButtonIntentReceiver.sReleaseTimer != null) {
                    CameraButtonIntentReceiver.sReleaseTimer.cancel();
                    CameraButtonIntentReceiver.sReleaseTimer.purge();
                    Timer unused = CameraButtonIntentReceiver.sReleaseTimer = null;
                }
            }
            CameraButtonIntentReceiver.changeStateTo(ReceiverState.IDLE);
            ((CameraApplication) this.mContext.getApplicationContext()).getCameraDevice().closeCamera();
        }
    }
}
