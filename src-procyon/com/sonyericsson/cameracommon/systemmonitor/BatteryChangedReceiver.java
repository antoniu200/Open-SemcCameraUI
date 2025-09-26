// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.systemmonitor;

import android.util.Log;
import android.content.Intent;
import android.content.IntentFilter;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.BatteryManager;
import android.os.Looper;
import android.os.Build;
import android.os.Handler;
import android.content.Context;
import android.content.BroadcastReceiver;

public class BatteryChangedReceiver extends BroadcastReceiver
{
    private static final boolean BATTERY_CHECK_ENABLED;
    private static final String TAG = "BatteryChangedReceiver";
    private static final int THRESHOLD_BATTERY_LEVEL;
    public static final int THRESHOLD_LOW_BATTERY_LEVEL;
    private int mBatteryLevel;
    private int mBatteryStatus;
    private int mBatteryTemperature;
    private final Context mContext;
    private final Handler mHandler;
    private int mHealth;
    private boolean mIsActive;
    private boolean mIsAlreadyBcl;
    private final BatteryChangedReceiverListener mListener;
    private int mPlugType;
    
    static {
        if (!"msm8996".equalsIgnoreCase(Build.BOARD) && !"sdm845".equalsIgnoreCase(Build.BOARD)) {
            THRESHOLD_BATTERY_LEVEL = 1;
            THRESHOLD_LOW_BATTERY_LEVEL = 15;
            BATTERY_CHECK_ENABLED = true;
        }
        else {
            THRESHOLD_BATTERY_LEVEL = 5;
            THRESHOLD_LOW_BATTERY_LEVEL = 15;
            BATTERY_CHECK_ENABLED = true;
        }
    }
    
    public BatteryChangedReceiver(final Context mContext, final BatteryChangedReceiverListener mListener) {
        this.mIsAlreadyBcl = false;
        this.mIsActive = true;
        this.mBatteryLevel = 100;
        this.mBatteryStatus = 1;
        this.mPlugType = 0;
        this.mBatteryTemperature = 0;
        this.mHealth = 1;
        this.mContext = mContext;
        this.mListener = mListener;
        this.mHandler = new Handler(mContext.getMainLooper());
    }
    
    private boolean checkBcl(final int n, final boolean b) {
        if (!this.mIsAlreadyBcl && isCheckEnabled() && n <= BatteryChangedReceiver.THRESHOLD_BATTERY_LEVEL) {
            this.mIsAlreadyBcl = true;
            if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
                this.mListener.onReachBatteryLimit(b);
            }
            else {
                this.mHandler.post((Runnable)new Runnable(this, b) {
                    final BatteryChangedReceiver this$0;
                    final boolean val$isOnStartup;
                    
                    @Override
                    public void run() {
                        this.this$0.mListener.onReachBatteryLimit(this.val$isOnStartup);
                    }
                });
            }
            return true;
        }
        return false;
    }
    
    public static boolean isCheckEnabled() {
        return BatteryChangedReceiver.BATTERY_CHECK_ENABLED;
    }
    
    private void notifyBatteryLevel(final int n) {
        this.mListener.onBatteryLevelChanged(n);
    }
    
    private void notifyLowBattery() {
        this.mListener.onReachLowBattery();
    }
    
    public void checkStartupStatus() {
        this.mIsAlreadyBcl = false;
        final int intProperty = ((BatteryManager)this.mContext.getSystemService("batterymanager")).getIntProperty(4);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkStartupStatus() : Battery Capacity = ");
            sb.append(intProperty);
            CamLog.d(sb.toString());
        }
        this.mBatteryLevel = intProperty;
        if (!this.checkBcl(this.mBatteryLevel, true)) {
            if (isCheckEnabled() && this.mBatteryLevel <= BatteryChangedReceiver.THRESHOLD_LOW_BATTERY_LEVEL) {
                this.notifyLowBattery();
            }
            this.notifyBatteryLevel(this.mBatteryLevel);
        }
    }
    
    public int getBatteryLevel() {
        return this.mBatteryLevel;
    }
    
    public boolean isAlreadyBcl() {
        return this.mIsAlreadyBcl;
    }
    
    public void onCreate() {
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate");
        }
        final IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        intentFilter.setPriority(999);
        this.mContext.registerReceiver((BroadcastReceiver)this, intentFilter);
    }
    
    public void onDestroy() {
        if (CamLog.VERBOSE) {
            CamLog.d("onDestroy");
        }
        this.mContext.unregisterReceiver((BroadcastReceiver)this);
    }
    
    public void onPause() {
        if (CamLog.VERBOSE) {
            CamLog.d("onPause");
        }
        this.mIsAlreadyBcl = false;
        this.mIsActive = true;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (this.mIsActive) {
            if (CamLog.VERBOSE) {
                CamLog.d("Activity is onPause, ignore bcl intent.");
            }
            return;
        }
        final boolean verbose = CamLog.VERBOSE;
        boolean b = true;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Receive action: ");
            sb.append(intent.getAction());
            CamLog.d(sb.toString());
        }
        if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
            final int mBatteryLevel = this.mBatteryLevel;
            this.mBatteryLevel = intent.getIntExtra("level", 100);
            final int mBatteryStatus = this.mBatteryStatus;
            this.mBatteryStatus = intent.getIntExtra("status", 1);
            final int mPlugType = this.mPlugType;
            this.mPlugType = intent.getIntExtra("plugged", 0);
            final int mBatteryTemperature = this.mBatteryTemperature;
            this.mBatteryTemperature = intent.getIntExtra("temperature", 0);
            final int mHealth = this.mHealth;
            this.mHealth = intent.getIntExtra("health", 1);
            final boolean b2 = this.mPlugType != 0;
            if (mPlugType == 0) {
                b = false;
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("level          ");
                sb2.append(mBatteryLevel);
                sb2.append(" --> ");
                sb2.append(this.mBatteryLevel);
                Log.d("BatteryChangedReceiver", sb2.toString());
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("status         ");
                sb3.append(mBatteryStatus);
                sb3.append(" --> ");
                sb3.append(this.mBatteryStatus);
                Log.d("BatteryChangedReceiver", sb3.toString());
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("plugType       ");
                sb4.append(mPlugType);
                sb4.append(" --> ");
                sb4.append(this.mPlugType);
                Log.d("BatteryChangedReceiver", sb4.toString());
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("plugged        ");
                sb5.append(b);
                sb5.append(" --> ");
                sb5.append(b2);
                Log.d("BatteryChangedReceiver", sb5.toString());
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("temperature    ");
                sb6.append(mBatteryTemperature);
                sb6.append(" --> ");
                sb6.append(this.mBatteryTemperature);
                Log.d("BatteryChangedReceiver", sb6.toString());
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("health         ");
                sb7.append(mHealth);
                sb7.append(" --> ");
                sb7.append(this.mHealth);
                Log.d("BatteryChangedReceiver", sb7.toString());
            }
            if (!this.checkBcl(this.mBatteryLevel, false)) {
                if (isCheckEnabled() && this.mBatteryLevel <= BatteryChangedReceiver.THRESHOLD_LOW_BATTERY_LEVEL && mBatteryLevel > BatteryChangedReceiver.THRESHOLD_LOW_BATTERY_LEVEL && mBatteryLevel != this.mBatteryLevel) {
                    this.notifyLowBattery();
                }
                this.notifyBatteryLevel(this.mBatteryLevel);
            }
        }
    }
    
    public void onResume() {
        if (CamLog.VERBOSE) {
            CamLog.d("onResume");
        }
        this.mIsAlreadyBcl = false;
        this.mIsActive = false;
    }
    
    public interface BatteryChangedReceiverListener
    {
        void onBatteryLevelChanged(final int p0);
        
        void onReachBatteryLimit(final boolean p0);
        
        void onReachLowBattery();
    }
}
