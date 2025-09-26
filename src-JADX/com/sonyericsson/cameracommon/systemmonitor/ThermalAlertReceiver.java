package com.sonyericsson.cameracommon.systemmonitor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.psm.sysmonservice.ISysmonService;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ThermalAlertReceiver extends BroadcastReceiver {
    private static final String ACTION_CAMERA_COOLED_DOWN_NORMAL = "com.sonyericsson.psm.action.CAMERA_COOLED_DOWN_NORMAL";
    private static final String ACTION_CAMERA_HEATED_CLOSE_TO_SHUTDOWN = "com.sonyericsson.psm.action.CAMERA_HEATED_CLOSE_TO_SHUTDOWN";
    private static final String ACTION_CAMERA_HEATED_OVER_CRITICAL = "com.sonyericsson.psm.action.CAMERA_HEATED_OVER_CRITICAL";
    private static final String ACTION_CAMERA_HEATED_OVER_LOW_TEMP_BURN = "com.sonyericsson.psm.action.CAMERA_HEATED_OVER_LOW_TEMP_BURN";
    private static final String ACTION_CAMERA_LOW_TEMP_BURN_TIMER_RESET = "com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_RESET";
    private static final String ACTION_CAMERA_LOW_TEMP_BURN_TIMER_SET = "com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_SET";
    private static final int CAMERA_CRITICAL = 604;
    private static final int CAMERA_HEATED_CLOSE_TO_SHUTDOWN = 620;
    private static final String CAMERA_HEATED_OVER_WARNING_EXTRA_FUNC = "com.sonyericsson.psm.action.CAMERA_HEATED_OVER_WARNING_EXTRA_FUNC";
    private static final int CAMERA_LOW_TEMP_BURN = 610;
    private static final int CAMERA_NORMAL = 600;
    private static final int CAMERA_WARNING = 603;
    private static final int CAMERA_WARNING_EXTRA = 601;
    private static final int INVALID_LOW_TEMP_BURN_TIMEOUT_DURATION = -1;
    private static final String KEY_LOW_TEMP_BURN_TIMER_DURATION_SEC = "com.sonyericsson.psm.extra.TIMEOUT_SEC";
    private static final int LOW_TEMP_BURN_TIMER_LIMIT_MILLIS = 1800000;
    private static final String SYSMON_SERVICE = "com.sonyericsson.psm.sysmonservice";
    private static final String SYSMON_SERVICE_CLASS = "com.sonyericsson.psm.sysmonservice.SysmonService";
    public static final String TAG = "ThermalAlertReceiver";
    private static final int VARIABLE_LOW_TEMP_BURN_TIMEOUT_DURATION_NOT_SUPPORTED = 0;
    private final Activity mActivity;
    private boolean mIsBindSysmonService;
    private final ThermalAlertReceiverListener mListener;
    private final LowTempBurnTimeoutTimerWrapper mLowTempBurnTimerFixedDuration;
    private final LowTempBurnTimeoutTimerWrapper mLowTempBurnTimerVariableDuration;
    private ISysmonService mSysmonService;
    private boolean mIsAlreadyHighTemperature = false;
    private boolean mIsWarningState = false;
    private boolean mIsWarningExtraState = false;
    private boolean mIsWarningReceived = false;
    private final ServiceConnection mServiceConnectionSysmon = new ServiceConnectionSysmon();

    public interface ThermalAlertReceiverListener {
        void onNotifyThermalNormal();

        void onNotifyThermalWarning(boolean z);

        void onNotifyThermalWarningExtra(boolean z);

        void onReachCriticalTemperature(boolean z);
    }

    public boolean isAlreadyHighTemperature() {
        return this.mIsAlreadyHighTemperature;
    }

    public boolean isWarningState() {
        return this.mIsWarningState;
    }

    public boolean isThermalWarningReceived() {
        return this.mIsWarningReceived;
    }

    public boolean isWarningExtraState() {
        return this.mIsWarningState | this.mIsWarningExtraState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkStartupStatus(int i, String str) {
        this.mIsAlreadyHighTemperature = false;
        switch (i) {
            case CAMERA_NORMAL /* 600 */:
                if (CamLog.VERBOSE) {
                    CamLog.d("Startup status of service[" + str + "] is NORMAL.");
                }
                changeToNormalState();
                break;
            case CAMERA_WARNING_EXTRA /* 601 */:
                if (CamLog.VERBOSE) {
                    CamLog.d("Startup status of service[" + str + "] is CAMERA_WARNING_EXTRA.");
                }
                changeToWarningExtraState(true);
                break;
            case CAMERA_WARNING /* 603 */:
                if (CamLog.VERBOSE) {
                    CamLog.d("Startup status of service[" + str + "] is WARNING.");
                }
                this.mIsAlreadyHighTemperature = true;
                finishOnStartup();
                break;
            case CAMERA_CRITICAL /* 604 */:
                if (CamLog.VERBOSE) {
                    CamLog.d("Startup status of service[" + str + "] is CRITICAL.");
                }
                this.mIsAlreadyHighTemperature = true;
                finishOnStartup();
                break;
            case CAMERA_HEATED_CLOSE_TO_SHUTDOWN /* 620 */:
                if (CamLog.VERBOSE) {
                    CamLog.d("Startup status of service[" + str + "] is CLOSE_TO_SHUTDOWN.");
                }
                changeToWarningState(true);
                break;
            default:
                if (CamLog.VERBOSE) {
                    CamLog.d("Startup status of service[" + str + "] is unknown.");
                    break;
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkLowTempBurnTimeoutTimerDuration(int i, int i2) {
        if (i2 == 0) {
            if (i == CAMERA_LOW_TEMP_BURN) {
                this.mLowTempBurnTimerFixedDuration.requestTimeMillis(1800000L);
            }
        } else if (i2 != -1) {
            if (CamLog.VERBOSE) {
                CamLog.d("Variable duration LTB timer : duration Sec=" + i2);
            }
            this.mLowTempBurnTimerVariableDuration.requestTimeMillis(i2 * 1000);
        }
    }

    class ServiceConnectionSysmon implements ServiceConnection {
        ServiceConnectionSysmon() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ThermalAlertReceiver.this.mSysmonService = ISysmonService.Stub.asInterface(iBinder);
            if (ThermalAlertReceiver.this.mSysmonService != null) {
                try {
                    int thermalLevelForCamera = ThermalAlertReceiver.this.mSysmonService.getThermalLevelForCamera();
                    ThermalAlertReceiver.this.checkStartupStatus(thermalLevelForCamera, "sysmon");
                    ThermalAlertReceiver.this.checkLowTempBurnTimeoutTimerDuration(thermalLevelForCamera, ThermalAlertReceiver.this.mSysmonService.getCameraLowTempBurnTimeoutSec());
                } catch (Exception e) {
                    CamLog.e("sysmon ServiceConnection failed.", e);
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            ThermalAlertReceiver.this.mSysmonService = null;
        }
    }

    public ThermalAlertReceiver(Activity activity, ThermalAlertReceiverListener thermalAlertReceiverListener) {
        this.mActivity = activity;
        this.mListener = thermalAlertReceiverListener;
        this.mLowTempBurnTimerFixedDuration = new LowTempBurnTimeoutTimerWrapper();
        this.mLowTempBurnTimerVariableDuration = new LowTempBurnTimeoutTimerWrapper();
    }

    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        if (!DebugParameterUtils.INSTANCE.isLowPowerModeDisabled(this.mActivity)) {
            intentFilter.addAction(ACTION_CAMERA_HEATED_OVER_CRITICAL);
            intentFilter.addAction(CAMERA_HEATED_OVER_WARNING_EXTRA_FUNC);
            intentFilter.addAction(ACTION_CAMERA_HEATED_OVER_LOW_TEMP_BURN);
            intentFilter.addAction(ACTION_CAMERA_COOLED_DOWN_NORMAL);
            intentFilter.addAction(ACTION_CAMERA_HEATED_CLOSE_TO_SHUTDOWN);
            intentFilter.addAction(ACTION_CAMERA_LOW_TEMP_BURN_TIMER_SET);
            intentFilter.addAction(ACTION_CAMERA_LOW_TEMP_BURN_TIMER_RESET);
        }
        this.mActivity.registerReceiver(this, intentFilter);
        this.mIsWarningExtraState = false;
    }

    public void onDestroy() {
        this.mActivity.unregisterReceiver(this);
    }

    public void onResume() {
        this.mIsAlreadyHighTemperature = false;
        this.mIsWarningExtraState = false;
        Intent intent = new Intent();
        intent.setClassName(SYSMON_SERVICE, SYSMON_SERVICE_CLASS);
        PerfLog.BIND_SYSMON_SERVICE.begin();
        this.mIsBindSysmonService = this.mActivity.bindService(intent, this.mServiceConnectionSysmon, 0);
        PerfLog.BIND_SYSMON_SERVICE.end();
        if (this.mIsBindSysmonService) {
            if (CamLog.VERBOSE) {
                CamLog.d("bind sysmon service");
                return;
            }
            return;
        }
        this.mActivity.unbindService(this.mServiceConnectionSysmon);
    }

    public void onPause() {
        this.mIsAlreadyHighTemperature = false;
        if (this.mIsBindSysmonService) {
            this.mIsBindSysmonService = false;
            this.mActivity.unbindService(this.mServiceConnectionSysmon);
        }
        this.mLowTempBurnTimerFixedDuration.cancel();
        this.mLowTempBurnTimerVariableDuration.cancel();
        this.mIsWarningReceived = false;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int i;
        if (!this.mIsBindSysmonService) {
            if (CamLog.VERBOSE) {
                CamLog.d("Service is already unbinded");
                return;
            }
            return;
        }
        if (this.mIsAlreadyHighTemperature) {
            if (CamLog.VERBOSE) {
                CamLog.d("Temperature is already high");
                return;
            }
            return;
        }
        String action = intent.getAction();
        if (ACTION_CAMERA_HEATED_OVER_CRITICAL.equals(action)) {
            this.mLowTempBurnTimerFixedDuration.cancel();
            this.mLowTempBurnTimerVariableDuration.cancel();
            this.mIsAlreadyHighTemperature = true;
            this.mListener.onReachCriticalTemperature(false);
            return;
        }
        if (ACTION_CAMERA_COOLED_DOWN_NORMAL.equals(action)) {
            this.mLowTempBurnTimerFixedDuration.cancel();
            changeToNormalState();
            return;
        }
        if (ACTION_CAMERA_HEATED_OVER_LOW_TEMP_BURN.equals(action)) {
            this.mLowTempBurnTimerFixedDuration.requestTimeMillis(1800000L);
            return;
        }
        if (ACTION_CAMERA_HEATED_CLOSE_TO_SHUTDOWN.equals(action)) {
            changeToWarningState(false);
            return;
        }
        if (ACTION_CAMERA_LOW_TEMP_BURN_TIMER_SET.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras == null || (i = extras.getInt(KEY_LOW_TEMP_BURN_TIMER_DURATION_SEC, -1)) == -1) {
                return;
            }
            this.mLowTempBurnTimerVariableDuration.requestTimeMillis(i * 1000);
            return;
        }
        if (ACTION_CAMERA_LOW_TEMP_BURN_TIMER_RESET.equals(action)) {
            this.mLowTempBurnTimerVariableDuration.cancel();
        } else if (CAMERA_HEATED_OVER_WARNING_EXTRA_FUNC.equals(action)) {
            changeToWarningExtraState(false);
        }
    }

    private void finishOnStartup() {
        this.mListener.onReachCriticalTemperature(true);
    }

    private class LowTempBurnTimeoutTimerWrapper {
        static final long INVALID_TIMER_TIME = -1;
        private Timer mTimer;
        private long mTimerToBeExpiredTimeMillis;

        private LowTempBurnTimeoutTimerWrapper() {
            this.mTimer = null;
            this.mTimerToBeExpiredTimeMillis = -1L;
        }

        private long getRemainedTimeMillis() {
            if (this.mTimerToBeExpiredTimeMillis == -1) {
                return -1L;
            }
            long jCurrentTimeMillis = this.mTimerToBeExpiredTimeMillis - System.currentTimeMillis();
            if (jCurrentTimeMillis <= 0) {
                return -1L;
            }
            return jCurrentTimeMillis;
        }

        public final synchronized void requestTimeMillis(long j) {
            if (CamLog.VERBOSE) {
                CamLog.d("Request low temp burn timer millis : " + j);
            }
            long remainedTimeMillis = getRemainedTimeMillis();
            if (remainedTimeMillis != -1 && remainedTimeMillis < j) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Current timer is valid.");
                }
            } else {
                cancel();
                this.mTimer = new Timer(true);
                this.mTimer.schedule(new LowTempBurnTimerTask(), j);
                this.mTimerToBeExpiredTimeMillis = System.currentTimeMillis() + j;
            }
        }

        public final synchronized void cancel() {
            if (CamLog.VERBOSE) {
                CamLog.d("Cancel low temp burn timer.");
            }
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer.purge();
                this.mTimer = null;
                this.mTimerToBeExpiredTimeMillis = -1L;
            } else if (CamLog.VERBOSE) {
                CamLog.d("LowTempBurnTimer is already cancel.");
            }
        }

        private class LowTempBurnTimerTask extends TimerTask {
            private final Handler mHandler;

            private LowTempBurnTimerTask() {
                this.mHandler = new Handler();
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (CamLog.VERBOSE) {
                    CamLog.d("LowTempBurn timer expired.");
                }
                cancel();
                this.mHandler.post(new Runnable() { // from class: com.sonyericsson.cameracommon.systemmonitor.ThermalAlertReceiver.LowTempBurnTimeoutTimerWrapper.LowTempBurnTimerTask.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (CamLog.VERBOSE) {
                            CamLog.d("LowTempBurnTimerTask finish");
                        }
                        ThermalAlertReceiver.this.mIsAlreadyHighTemperature = true;
                        ThermalAlertReceiver.this.mListener.onReachCriticalTemperature(false);
                    }
                });
            }
        }
    }

    private void changeToNormalState() {
        this.mIsWarningState = false;
        this.mListener.onNotifyThermalNormal();
    }

    private void changeToWarningState(boolean z) {
        this.mIsWarningState = true;
        this.mIsWarningReceived = true;
        this.mListener.onNotifyThermalWarning(z);
        this.mListener.onNotifyThermalWarningExtra(z);
    }

    private void changeToWarningExtraState(boolean z) {
        this.mIsWarningExtraState = true;
        this.mListener.onNotifyThermalWarningExtra(z);
    }
}
