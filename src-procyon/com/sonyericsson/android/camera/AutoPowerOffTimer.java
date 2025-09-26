// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import android.os.Message;
import android.os.Handler;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.Context;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import java.util.TimerTask;
import java.util.Timer;

public class AutoPowerOffTimer
{
    private CameraActivity mActivity;
    private int mAutoPowerOffTimeOutDuration;
    private int mAutoPowerOffWarningTimeOutOffset;
    private final AutoPowerOffHandler mHandler;
    private boolean mIsAutoPowerOffTimerEnabled;
    private AutoPowerOffListener mListener;
    private Timer mTimer;
    private Object mUserdata;
    
    public AutoPowerOffTimer(final CameraActivity mActivity, final AutoPowerOffListener mListener) {
        this.mIsAutoPowerOffTimerEnabled = false;
        this.mHandler = new AutoPowerOffHandler();
        this.mActivity = mActivity;
        this.mListener = mListener;
    }
    
    private boolean startAutoPowerOff(final int n) {
        synchronized (this) {
            if (this.mActivity.isInLockTaskMode()) {
                return false;
            }
            if (this.mTimer == null) {
                (this.mTimer = new Timer(true)).schedule(new AutoPowerOffTask(), n);
                return true;
            }
            return false;
        }
    }
    
    private final void startAutoPowerOffTimer() {
        if (DebugParameterUtils.INSTANCE.isAutoPowerOffDisabled((Context)this.mActivity)) {
            return;
        }
        if (!this.mIsAutoPowerOffTimerEnabled) {
            return;
        }
        if (this.mAutoPowerOffTimeOutDuration < this.mAutoPowerOffWarningTimeOutOffset) {
            this.startAutoPowerOff(this.mAutoPowerOffTimeOutDuration);
        }
        else {
            this.startAutoPowerOffWarning(this.mAutoPowerOffTimeOutDuration - this.mAutoPowerOffWarningTimeOutOffset);
        }
    }
    
    private boolean startAutoPowerOffWarning(final int n) {
        synchronized (this) {
            if (this.mActivity.isInLockTaskMode()) {
                return false;
            }
            if (this.mTimer == null) {
                (this.mTimer = new Timer(true)).schedule(new AutoPowerOffWarningTask(), n);
                return true;
            }
            return false;
        }
    }
    
    private final void stopAutoPowerOffTimer() {
        synchronized (this) {
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer.purge();
                this.mTimer = null;
            }
        }
    }
    
    public final void disableAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("disableAutoPowerOffTimer: ");
        }
        this.mHandler.removeAllMessages();
        this.stopAutoPowerOffTimer();
        this.mIsAutoPowerOffTimerEnabled = false;
    }
    
    public final void enableAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("enableAutoPowerOffTimer: ");
        }
        this.mIsAutoPowerOffTimerEnabled = true;
        this.startAutoPowerOffTimer();
    }
    
    public Object getUserdata() {
        return this.mUserdata;
    }
    
    public final void restartAutoPowerOffTimer() {
        synchronized (this) {
            this.mHandler.removeAllMessages();
            this.stopAutoPowerOffTimer();
            this.startAutoPowerOffTimer();
        }
    }
    
    public void setTimeOutDuration(final int mAutoPowerOffTimeOutDuration, final int mAutoPowerOffWarningTimeOutOffset, final Object mUserdata) {
        this.mAutoPowerOffTimeOutDuration = mAutoPowerOffTimeOutDuration;
        this.mAutoPowerOffWarningTimeOutOffset = mAutoPowerOffWarningTimeOutOffset;
        this.mUserdata = mUserdata;
    }
    
    private class AutoPowerOffHandler extends Handler
    {
        private static final int MSG_AUTO_POWER_OFF = 2;
        private static final int MSG_AUTO_POWER_OFF_WARNING = 1;
        final AutoPowerOffTimer this$0;
        
        private AutoPowerOffHandler(final AutoPowerOffTimer this$0) {
            this.this$0 = this$0;
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                case 2: {
                    this.this$0.stopAutoPowerOffTimer();
                    this.this$0.mListener.onAutoPowerOff(this.this$0.mUserdata);
                    break;
                }
                case 1: {
                    this.this$0.stopAutoPowerOffTimer();
                    this.this$0.startAutoPowerOff(this.this$0.mAutoPowerOffWarningTimeOutOffset);
                    this.this$0.mListener.onAutoPowerOffWarning();
                    break;
                }
            }
        }
        
        public void removeAllMessages() {
            this.removeMessages(1);
            this.removeMessages(2);
        }
        
        public void sendAutoPowerOffMessage() {
            this.sendEmptyMessage(2);
        }
        
        public void sendAutoPowerOffWarningMessage() {
            this.sendEmptyMessage(1);
        }
    }
    
    public interface AutoPowerOffListener
    {
        void onAutoPowerOff(final Object p0);
        
        void onAutoPowerOffWarning();
    }
    
    private class AutoPowerOffTask extends TimerTask
    {
        final AutoPowerOffTimer this$0;
        
        private AutoPowerOffTask(final AutoPowerOffTimer this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mHandler.sendAutoPowerOffMessage();
        }
    }
    
    private class AutoPowerOffWarningTask extends TimerTask
    {
        final AutoPowerOffTimer this$0;
        
        private AutoPowerOffWarningTask(final AutoPowerOffTimer this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mHandler.sendAutoPowerOffWarningMessage();
        }
    }
}
