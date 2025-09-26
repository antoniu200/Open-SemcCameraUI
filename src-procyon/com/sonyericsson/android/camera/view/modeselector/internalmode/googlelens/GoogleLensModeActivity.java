// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens;

import android.app.KeyguardManager$KeyguardDismissCallback;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.widget.Toast;
import com.sonyericsson.android.camera.util.CamLog;
import com.google.lens.sdk.LensApi;
import android.os.Handler;
import android.app.Activity;

public class GoogleLensModeActivity extends Activity
{
    private static final long START_TIMEOUT_MILLIS = 5000L;
    private Handler mHandler;
    private LensApi mLensApi;
    private final Runnable mStartTimeoutTask;
    private State mState;
    
    public GoogleLensModeActivity() {
        this.mState = State.READY;
        this.mStartTimeoutTask = new Runnable() {
            final GoogleLensModeActivity this$0;
            
            @Override
            public void run() {
                CamLog.e("Finish. Timeout of launch Google Lens.");
                this.this$0.showErrorToast();
                this.this$0.finish();
            }
        };
    }
    
    private void changeTo(final State mState) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("prev:");
            sb.append(this.mState.name());
            sb.append(" next:");
            sb.append(mState.name());
            CamLog.d(sb.toString());
        }
        this.mState = mState;
    }
    
    private void showErrorToast() {
        Toast.makeText((Context)this, (CharSequence)this.getString(2131689698, new Object[] { this.getString(2131689674) }), 0).show();
    }
    
    private boolean startGoogleLensActivity() {
        try {
            if (CamLog.DEBUG) {
                CamLog.d("Launch Lens activity");
            }
            this.mLensApi.launchLensActivity(this);
            this.mHandler.postDelayed(this.mStartTimeoutTask, 5000L);
            return true;
        }
        catch (final Exception ex) {
            CamLog.e("Fail to launch Lens activity.");
            this.showErrorToast();
            return false;
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : E");
        }
        super.onCreate(bundle);
        this.getWindow().addFlags(524288);
        this.mLensApi = new LensApi((Context)this);
        this.mHandler = new Handler();
        this.changeTo(State.READY);
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : X");
        }
    }
    
    protected void onNewIntent(final Intent intent) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onNewIntent() : E state:");
            sb.append(this.mState.name());
            CamLog.d(sb.toString());
        }
        super.onNewIntent(intent);
        this.changeTo(State.READY);
        if (CamLog.DEBUG) {
            CamLog.d("onNewIntent() : X");
        }
    }
    
    protected void onPause() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onPause() : E state:");
            sb.append(this.mState.name());
            CamLog.d(sb.toString());
        }
        if (this.mState == State.DONE) {
            this.mHandler.removeCallbacks(this.mStartTimeoutTask);
        }
        super.onPause();
        this.mLensApi.onPause();
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : X");
        }
    }
    
    protected void onResume() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onResume() : E state:");
            sb.append(this.mState.name());
            CamLog.d(sb.toString());
        }
        super.onResume();
        this.mLensApi.onResume();
        while (true) {
            switch (GoogleLensModeActivity$2.$SwitchMap$com$sonyericsson$android$camera$view$modeselector$internalmode$googlelens$GoogleLensModeActivity$State[this.mState.ordinal()]) {
                default: {
                    break Label_0175;
                }
                case 1: {
                    final KeyguardManager keyguardManager = (KeyguardManager)this.getSystemService((Class)KeyguardManager.class);
                    if (keyguardManager.isDeviceLocked()) {
                        this.changeTo(State.KEYGUARD);
                        keyguardManager.requestDismissKeyguard((Activity)this, (KeyguardManager$KeyguardDismissCallback)new KeyguardDismissCallbackImpl());
                        break Label_0175;
                    }
                    if (this.startGoogleLensActivity()) {
                        this.changeTo(State.DONE);
                        break Label_0175;
                    }
                    this.changeTo(State.READY);
                    this.finish();
                    break Label_0175;
                }
                case 2: {
                    if (CamLog.DEBUG) {
                        CamLog.d("onResume() : X");
                    }
                    return;
                }
                case 3: {
                    this.finish();
                    continue;
                }
            }
            break;
        }
    }
    
    private class KeyguardDismissCallbackImpl extends KeyguardManager$KeyguardDismissCallback
    {
        final GoogleLensModeActivity this$0;
        
        private KeyguardDismissCallbackImpl(final GoogleLensModeActivity this$0) {
            this.this$0 = this$0;
        }
        
        public void onDismissCancelled() {
            if (CamLog.DEBUG) {
                CamLog.d("Keyguard dismiss cancelled");
            }
            this.this$0.changeTo(State.READY);
            this.this$0.finish();
        }
        
        public void onDismissError() {
            if (CamLog.DEBUG) {
                CamLog.d("Error dismissing keyguard");
            }
            this.this$0.changeTo(State.READY);
            this.this$0.finish();
        }
        
        public void onDismissSucceeded() {
            if (CamLog.DEBUG) {
                CamLog.d("Keyguard successfully dismissed");
            }
            if (this.this$0.startGoogleLensActivity()) {
                this.this$0.changeTo(State.DONE);
            }
            else {
                this.this$0.changeTo(State.READY);
                this.this$0.finish();
            }
        }
    }
    
    private enum State
    {
        private static final State[] $VALUES;
        
        DONE, 
        KEYGUARD, 
        READY;
        
        static {
            $VALUES = new State[] { State.READY, State.KEYGUARD, State.DONE };
        }
    }
}
