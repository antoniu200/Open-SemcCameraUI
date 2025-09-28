package com.sonyericsson.android.camera;

import android.os.Handler;
import android.os.Message;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AutoPowerOffTimer {
    private CameraActivity mActivity;
    private int mAutoPowerOffTimeOutDuration;
    private int mAutoPowerOffWarningTimeOutOffset;
    private AutoPowerOffListener mListener;
    private Timer mTimer;
    private Object mUserdata;
    private boolean mIsAutoPowerOffTimerEnabled = false;
    private final AutoPowerOffHandler mHandler = new AutoPowerOffHandler();

    public interface AutoPowerOffListener {
        void onAutoPowerOff(Object obj);

        void onAutoPowerOffWarning();
    }

    public AutoPowerOffTimer(CameraActivity cameraActivity, AutoPowerOffListener autoPowerOffListener) {
        this.mActivity = cameraActivity;
        this.mListener = autoPowerOffListener;
    }

    public void setTimeOutDuration(int i, int i2, Object obj) {
        this.mAutoPowerOffTimeOutDuration = i;
        this.mAutoPowerOffWarningTimeOutOffset = i2;
        this.mUserdata = obj;
    }

    public final void enableAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("enableAutoPowerOffTimer: ");
        }
        this.mIsAutoPowerOffTimerEnabled = true;
        startAutoPowerOffTimer();
    }

    public final void disableAutoPowerOffTimer() {
        if (CamLog.VERBOSE) {
            CamLog.d("disableAutoPowerOffTimer: ");
        }
        this.mHandler.removeAllMessages();
        stopAutoPowerOffTimer();
        this.mIsAutoPowerOffTimerEnabled = false;
    }

    public final synchronized void restartAutoPowerOffTimer() {
        this.mHandler.removeAllMessages();
        stopAutoPowerOffTimer();
        startAutoPowerOffTimer();
    }

    private final void startAutoPowerOffTimer() {
        if (!DebugParameterUtils.INSTANCE.isAutoPowerOffDisabled(this.mActivity) && this.mIsAutoPowerOffTimerEnabled) {
            if (this.mAutoPowerOffTimeOutDuration < this.mAutoPowerOffWarningTimeOutOffset) {
                startAutoPowerOff(this.mAutoPowerOffTimeOutDuration);
            } else {
                startAutoPowerOffWarning(this.mAutoPowerOffTimeOutDuration - this.mAutoPowerOffWarningTimeOutOffset);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final synchronized void stopAutoPowerOffTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer.purge();
            this.mTimer = null;
        }
    }

    private synchronized boolean startAutoPowerOffWarning(int i) {
        if (this.mActivity.isInLockTaskMode()) {
            return false;
        }
        if (this.mTimer != null) {
            return false;
        }
        this.mTimer = new Timer(true);
        this.mTimer.schedule(new AutoPowerOffWarningTask(), i);
        return true;
    }

    private class AutoPowerOffWarningTask extends TimerTask {
        private AutoPowerOffWarningTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            AutoPowerOffTimer.this.mHandler.sendAutoPowerOffWarningMessage();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean startAutoPowerOff(int i) {
        if (this.mActivity.isInLockTaskMode()) {
            return false;
        }
        if (this.mTimer != null) {
            return false;
        }
        this.mTimer = new Timer(true);
        this.mTimer.schedule(new AutoPowerOffTask(), i);
        return true;
    }

    private class AutoPowerOffTask extends TimerTask {
        private AutoPowerOffTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            AutoPowerOffTimer.this.mHandler.sendAutoPowerOffMessage();
        }
    }

    private class AutoPowerOffHandler extends Handler {
        private static final int MSG_AUTO_POWER_OFF = 2;
        private static final int MSG_AUTO_POWER_OFF_WARNING = 1;

        private AutoPowerOffHandler() {
        }

        public void sendAutoPowerOffWarningMessage() {
            sendEmptyMessage(1);
        }

        public void sendAutoPowerOffMessage() {
            sendEmptyMessage(2);
        }

        public void removeAllMessages() {
            removeMessages(1);
            removeMessages(2);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    AutoPowerOffTimer.this.stopAutoPowerOffTimer();
                    AutoPowerOffTimer.this.startAutoPowerOff(AutoPowerOffTimer.this.mAutoPowerOffWarningTimeOutOffset);
                    AutoPowerOffTimer.this.mListener.onAutoPowerOffWarning();
                    break;
                case 2:
                    AutoPowerOffTimer.this.stopAutoPowerOffTimer();
                    AutoPowerOffTimer.this.mListener.onAutoPowerOff(AutoPowerOffTimer.this.mUserdata);
                    break;
            }
        }
    }

    public Object getUserdata() {
        return this.mUserdata;
    }
}
