// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.systemmonitor;

import android.os.IBinder;
import android.content.ComponentName;
import android.os.Handler;
import java.util.TimerTask;
import java.util.Timer;
import com.sonyericsson.android.camera.util.PerfLog;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import android.content.IntentFilter;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.psm.sysmonservice.ISysmonService;
import android.content.ServiceConnection;
import android.app.Activity;
import android.content.BroadcastReceiver;

public class ThermalAlertReceiver extends BroadcastReceiver
{
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
    private boolean mIsAlreadyHighTemperature;
    private boolean mIsBindSysmonService;
    private boolean mIsWarningExtraState;
    private boolean mIsWarningReceived;
    private boolean mIsWarningState;
    private final ThermalAlertReceiverListener mListener;
    private final LowTempBurnTimeoutTimerWrapper mLowTempBurnTimerFixedDuration;
    private final LowTempBurnTimeoutTimerWrapper mLowTempBurnTimerVariableDuration;
    private final ServiceConnection mServiceConnectionSysmon;
    private ISysmonService mSysmonService;
    
    public ThermalAlertReceiver(final Activity mActivity, final ThermalAlertReceiverListener mListener) {
        this.mIsAlreadyHighTemperature = false;
        this.mIsWarningState = false;
        this.mIsWarningExtraState = false;
        this.mIsWarningReceived = false;
        this.mActivity = mActivity;
        this.mListener = mListener;
        this.mServiceConnectionSysmon = (ServiceConnection)new ServiceConnectionSysmon();
        this.mLowTempBurnTimerFixedDuration = new LowTempBurnTimeoutTimerWrapper();
        this.mLowTempBurnTimerVariableDuration = new LowTempBurnTimeoutTimerWrapper();
    }
    
    private void changeToNormalState() {
        this.mIsWarningState = false;
        this.mListener.onNotifyThermalNormal();
    }
    
    private void changeToWarningExtraState(final boolean b) {
        this.mIsWarningExtraState = true;
        this.mListener.onNotifyThermalWarningExtra(b);
    }
    
    private void changeToWarningState(final boolean b) {
        this.mIsWarningState = true;
        this.mIsWarningReceived = true;
        this.mListener.onNotifyThermalWarning(b);
        this.mListener.onNotifyThermalWarningExtra(b);
    }
    
    private void checkLowTempBurnTimeoutTimerDuration(final int n, final int i) {
        if (i == 0) {
            if (n == 610) {
                this.mLowTempBurnTimerFixedDuration.requestTimeMillis(1800000L);
            }
        }
        else if (i != -1) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Variable duration LTB timer : duration Sec=");
                sb.append(i);
                CamLog.d(sb.toString());
            }
            this.mLowTempBurnTimerVariableDuration.requestTimeMillis(i * 1000);
        }
    }
    
    private void checkStartupStatus(final int n, final String s) {
        this.mIsAlreadyHighTemperature = false;
        switch (n) {
            default: {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Startup status of service[");
                    sb.append(s);
                    sb.append("] is unknown.");
                    CamLog.d(sb.toString());
                    break;
                }
                break;
            }
            case 620: {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Startup status of service[");
                    sb2.append(s);
                    sb2.append("] is CLOSE_TO_SHUTDOWN.");
                    CamLog.d(sb2.toString());
                }
                this.changeToWarningState(true);
                break;
            }
            case 604: {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Startup status of service[");
                    sb3.append(s);
                    sb3.append("] is CRITICAL.");
                    CamLog.d(sb3.toString());
                }
                this.mIsAlreadyHighTemperature = true;
                this.finishOnStartup();
                break;
            }
            case 603: {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Startup status of service[");
                    sb4.append(s);
                    sb4.append("] is WARNING.");
                    CamLog.d(sb4.toString());
                }
                this.mIsAlreadyHighTemperature = true;
                this.finishOnStartup();
                break;
            }
            case 601: {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("Startup status of service[");
                    sb5.append(s);
                    sb5.append("] is CAMERA_WARNING_EXTRA.");
                    CamLog.d(sb5.toString());
                }
                this.changeToWarningExtraState(true);
                break;
            }
            case 600: {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("Startup status of service[");
                    sb6.append(s);
                    sb6.append("] is NORMAL.");
                    CamLog.d(sb6.toString());
                }
                this.changeToNormalState();
                break;
            }
        }
    }
    
    private void finishOnStartup() {
        this.mListener.onReachCriticalTemperature(true);
    }
    
    public boolean isAlreadyHighTemperature() {
        return this.mIsAlreadyHighTemperature;
    }
    
    public boolean isThermalWarningReceived() {
        return this.mIsWarningReceived;
    }
    
    public boolean isWarningExtraState() {
        return this.mIsWarningState | this.mIsWarningExtraState;
    }
    
    public boolean isWarningState() {
        return this.mIsWarningState;
    }
    
    public void onCreate() {
        final IntentFilter intentFilter = new IntentFilter();
        if (!DebugParameterUtils.INSTANCE.isLowPowerModeDisabled((Context)this.mActivity)) {
            intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_CRITICAL");
            intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_WARNING_EXTRA_FUNC");
            intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_LOW_TEMP_BURN");
            intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_COOLED_DOWN_NORMAL");
            intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_HEATED_CLOSE_TO_SHUTDOWN");
            intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_SET");
            intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_RESET");
        }
        this.mActivity.registerReceiver((BroadcastReceiver)this, intentFilter);
        this.mIsWarningExtraState = false;
    }
    
    public void onDestroy() {
        this.mActivity.unregisterReceiver((BroadcastReceiver)this);
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
    
    public void onReceive(final Context context, final Intent intent) {
        if (!this.mIsBindSysmonService) {
            if (CamLog.VERBOSE) {
                CamLog.d("Service is already unbinded");
            }
            return;
        }
        if (this.mIsAlreadyHighTemperature) {
            if (CamLog.VERBOSE) {
                CamLog.d("Temperature is already high");
            }
            return;
        }
        final String action = intent.getAction();
        if ("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_CRITICAL".equals(action)) {
            this.mLowTempBurnTimerFixedDuration.cancel();
            this.mLowTempBurnTimerVariableDuration.cancel();
            this.mIsAlreadyHighTemperature = true;
            this.mListener.onReachCriticalTemperature(false);
        }
        else if ("com.sonyericsson.psm.action.CAMERA_COOLED_DOWN_NORMAL".equals(action)) {
            this.mLowTempBurnTimerFixedDuration.cancel();
            this.changeToNormalState();
        }
        else if ("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_LOW_TEMP_BURN".equals(action)) {
            this.mLowTempBurnTimerFixedDuration.requestTimeMillis(1800000L);
        }
        else if ("com.sonyericsson.psm.action.CAMERA_HEATED_CLOSE_TO_SHUTDOWN".equals(action)) {
            this.changeToWarningState(false);
        }
        else if ("com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_SET".equals(action)) {
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                final int int1 = extras.getInt("com.sonyericsson.psm.extra.TIMEOUT_SEC", -1);
                if (int1 != -1) {
                    this.mLowTempBurnTimerVariableDuration.requestTimeMillis(int1 * 1000);
                }
            }
        }
        else if ("com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_RESET".equals(action)) {
            this.mLowTempBurnTimerVariableDuration.cancel();
        }
        else if ("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_WARNING_EXTRA_FUNC".equals(action)) {
            this.changeToWarningExtraState(false);
        }
    }
    
    public void onResume() {
        this.mIsAlreadyHighTemperature = false;
        this.mIsWarningExtraState = false;
        final Intent intent = new Intent();
        intent.setClassName("com.sonyericsson.psm.sysmonservice", "com.sonyericsson.psm.sysmonservice.SysmonService");
        PerfLog.BIND_SYSMON_SERVICE.begin();
        this.mIsBindSysmonService = this.mActivity.bindService(intent, this.mServiceConnectionSysmon, 0);
        PerfLog.BIND_SYSMON_SERVICE.end();
        if (this.mIsBindSysmonService) {
            if (CamLog.VERBOSE) {
                CamLog.d("bind sysmon service");
            }
        }
        else {
            this.mActivity.unbindService(this.mServiceConnectionSysmon);
        }
    }
    
    private class LowTempBurnTimeoutTimerWrapper
    {
        static final long INVALID_TIMER_TIME = -1L;
        private Timer mTimer;
        private long mTimerToBeExpiredTimeMillis;
        final ThermalAlertReceiver this$0;
        
        private LowTempBurnTimeoutTimerWrapper(final ThermalAlertReceiver this$0) {
            this.this$0 = this$0;
            this.mTimer = null;
            this.mTimerToBeExpiredTimeMillis = -1L;
        }
        
        private long getRemainedTimeMillis() {
            if (this.mTimerToBeExpiredTimeMillis == -1L) {
                return -1L;
            }
            final long n = this.mTimerToBeExpiredTimeMillis - System.currentTimeMillis();
            if (n <= 0L) {
                return -1L;
            }
            return n;
        }
        
        public final void cancel() {
            synchronized (this) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Cancel low temp burn timer.");
                }
                if (this.mTimer != null) {
                    this.mTimer.cancel();
                    this.mTimer.purge();
                    this.mTimer = null;
                    this.mTimerToBeExpiredTimeMillis = -1L;
                }
                else if (CamLog.VERBOSE) {
                    CamLog.d("LowTempBurnTimer is already cancel.");
                }
            }
        }
        
        public final void requestTimeMillis(final long n) {
            synchronized (this) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Request low temp burn timer millis : ");
                    sb.append(n);
                    CamLog.d(sb.toString());
                }
                final long remainedTimeMillis = this.getRemainedTimeMillis();
                if (remainedTimeMillis != -1L && remainedTimeMillis < n) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Current timer is valid.");
                    }
                    return;
                }
                this.cancel();
                (this.mTimer = new Timer(true)).schedule(new LowTempBurnTimerTask(), n);
                this.mTimerToBeExpiredTimeMillis = System.currentTimeMillis() + n;
            }
        }
        
        private class LowTempBurnTimerTask extends TimerTask
        {
            private final Handler mHandler;
            final LowTempBurnTimeoutTimerWrapper this$1;
            
            private LowTempBurnTimerTask(final LowTempBurnTimeoutTimerWrapper this$1) {
                this.this$1 = this$1;
                this.mHandler = new Handler();
            }
            
            @Override
            public void run() {
                if (CamLog.VERBOSE) {
                    CamLog.d("LowTempBurn timer expired.");
                }
                this.cancel();
                this.mHandler.post((Runnable)new Runnable(this) {
                    final LowTempBurnTimerTask this$2;
                    
                    @Override
                    public void run() {
                        if (CamLog.VERBOSE) {
                            CamLog.d("LowTempBurnTimerTask finish");
                        }
                        this.this$2.this$1.this$0.mIsAlreadyHighTemperature = true;
                        this.this$2.this$1.this$0.mListener.onReachCriticalTemperature(false);
                    }
                });
            }
        }
    }
    
    class ServiceConnectionSysmon implements ServiceConnection
    {
        final ThermalAlertReceiver this$0;
        
        ServiceConnectionSysmon(final ThermalAlertReceiver this$0) {
            this.this$0 = this$0;
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            this.this$0.mSysmonService = ISysmonService.Stub.asInterface(binder);
            if (this.this$0.mSysmonService != null) {
                try {
                    final int thermalLevelForCamera = this.this$0.mSysmonService.getThermalLevelForCamera();
                    this.this$0.checkStartupStatus(thermalLevelForCamera, "sysmon");
                    this.this$0.checkLowTempBurnTimeoutTimerDuration(thermalLevelForCamera, this.this$0.mSysmonService.getCameraLowTempBurnTimeoutSec());
                }
                catch (final Exception ex) {
                    CamLog.e("sysmon ServiceConnection failed.", ex);
                }
            }
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
            this.this$0.mSysmonService = null;
        }
    }
    
    public interface ThermalAlertReceiverListener
    {
        void onNotifyThermalNormal();
        
        void onNotifyThermalWarning(final boolean p0);
        
        void onNotifyThermalWarningExtra(final boolean p0);
        
        void onReachCriticalTemperature(final boolean p0);
    }
}
