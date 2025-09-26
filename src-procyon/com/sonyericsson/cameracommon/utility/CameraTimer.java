// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import java.util.TimerTask;
import android.os.Message;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Timer;
import android.os.Handler;

public class CameraTimer
{
    public static final int MSG_CANCEL = 2;
    public static final int MSG_INTERVAL = 0;
    public static final int MSG_POST_TIMEOUT = 3;
    public static final int MSG_TIMEOUT = 1;
    public static final String TAG = "SelfTimer";
    private long mCurTime;
    private long mDelay;
    private Handler mHandler;
    private long mInterval;
    private String mOptionName;
    private Timer mTimer;
    
    public CameraTimer(final long n, final long n2, final Handler handler, final String s, final long mDelay) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("maxtime = ");
            sb.append(n);
            sb.append(", interval = ");
            sb.append(n2);
            sb.append(", handler = ");
            sb.append(handler);
            sb.append(", optionName = ");
            sb.append(s);
            CamLog.w(sb.toString());
        }
        this.mCurTime = n;
        this.mHandler = handler;
        this.mInterval = n2;
        this.mOptionName = s;
        this.mDelay = mDelay;
        if (n > 0L && n2 > 0L && handler != null && this.mCurTime >= this.mInterval && this.mCurTime <= 2147483647L) {
            this.mTimer = new Timer(true);
        }
        else {
            if (CamLog.VERBOSE) {
                CamLog.d("invalid timer setting.");
            }
            this.mTimer = null;
        }
    }
    
    private void terminateInnerTimer() {
        synchronized (this) {
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer.purge();
                this.mTimer = null;
            }
        }
    }
    
    public void cancel() {
        synchronized (this) {
            if (this.mTimer == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("cancel timer == null, do nothing");
                }
            }
            else {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("cancel schedule.(");
                    sb.append(this.mOptionName);
                    sb.append(")");
                    CamLog.d(sb.toString());
                }
                this.terminateInnerTimer();
                final Message obtain = Message.obtain();
                obtain.arg1 = (int)this.mCurTime;
                obtain.what = 2;
                this.mHandler.sendMessage(obtain);
            }
            this.mHandler.removeMessages(1);
        }
    }
    
    public void start() {
        synchronized (this) {
            if (this.mTimer == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("start timer == null, do nothing");
                }
            }
            else {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("start schedule.(");
                    sb.append(this.mOptionName);
                    sb.append(")");
                    CamLog.d(sb.toString());
                }
                this.mTimer.schedule(new SelfTimerTimerTask(), this.mDelay, this.mInterval);
            }
        }
    }
    
    private class SelfTimerTimerTask extends TimerTask
    {
        final CameraTimer this$0;
        
        private SelfTimerTimerTask(final CameraTimer this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (this.this$0.mCurTime > 0L) {
                final Message obtain = Message.obtain();
                obtain.arg1 = (int)this.this$0.mCurTime;
                obtain.what = 0;
                this.this$0.mHandler.sendMessage(obtain);
            }
            else {
                final Message obtain2 = Message.obtain();
                obtain2.arg1 = (int)this.this$0.mCurTime;
                obtain2.what = 1;
                this.this$0.mHandler.sendMessage(obtain2);
                this.this$0.terminateInnerTimer();
            }
            this.this$0.mCurTime -= this.this$0.mInterval;
        }
    }
}
