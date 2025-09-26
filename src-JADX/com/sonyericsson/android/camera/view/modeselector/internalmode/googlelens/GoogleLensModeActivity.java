package com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.google.lens.sdk.LensApi;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GoogleLensModeActivity extends Activity {
    private static final long START_TIMEOUT_MILLIS = 5000;
    private Handler mHandler;
    private LensApi mLensApi;
    private State mState = State.READY;
    private final Runnable mStartTimeoutTask = new Runnable() { // from class: com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens.GoogleLensModeActivity.1
        @Override // java.lang.Runnable
        public void run() {
            CamLog.e("Finish. Timeout of launch Google Lens.");
            GoogleLensModeActivity.this.showErrorToast();
            GoogleLensModeActivity.this.finish();
        }
    };

    private enum State {
        READY,
        KEYGUARD,
        DONE
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeTo(State state) {
        if (CamLog.DEBUG) {
            CamLog.d("prev:" + this.mState.name() + " next:" + state.name());
        }
        this.mState = state;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : E");
        }
        super.onCreate(bundle);
        getWindow().addFlags(524288);
        this.mLensApi = new LensApi(this);
        this.mHandler = new Handler();
        changeTo(State.READY);
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : X");
        }
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        if (CamLog.DEBUG) {
            CamLog.d("onNewIntent() : E state:" + this.mState.name());
        }
        super.onNewIntent(intent);
        changeTo(State.READY);
        if (CamLog.DEBUG) {
            CamLog.d("onNewIntent() : X");
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : E state:" + this.mState.name());
        }
        super.onResume();
        this.mLensApi.onResume();
        switch (this.mState) {
            case READY:
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KeyguardManager.class);
                if (keyguardManager.isDeviceLocked()) {
                    changeTo(State.KEYGUARD);
                    keyguardManager.requestDismissKeyguard(this, new KeyguardDismissCallbackImpl());
                    break;
                } else if (startGoogleLensActivity()) {
                    changeTo(State.DONE);
                    break;
                } else {
                    changeTo(State.READY);
                    finish();
                    break;
                }
            case DONE:
                finish();
                break;
        }
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : X");
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : E state:" + this.mState.name());
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

    /* JADX INFO: Access modifiers changed from: private */
    public boolean startGoogleLensActivity() {
        try {
            if (CamLog.DEBUG) {
                CamLog.d("Launch Lens activity");
            }
            this.mLensApi.launchLensActivity(this);
            this.mHandler.postDelayed(this.mStartTimeoutTask, START_TIMEOUT_MILLIS);
            return true;
        } catch (Exception unused) {
            CamLog.e("Fail to launch Lens activity.");
            showErrorToast();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showErrorToast() {
        Toast.makeText(this, getString(R.string.cam_strings_common_error_open_txt, new Object[]{getString(R.string.cam_strings_capturing_mode_google_lens)}), 0).show();
    }

    private class KeyguardDismissCallbackImpl extends KeyguardManager.KeyguardDismissCallback {
        private KeyguardDismissCallbackImpl() {
        }

        @Override // android.app.KeyguardManager.KeyguardDismissCallback
        public void onDismissSucceeded() {
            if (CamLog.DEBUG) {
                CamLog.d("Keyguard successfully dismissed");
            }
            if (GoogleLensModeActivity.this.startGoogleLensActivity()) {
                GoogleLensModeActivity.this.changeTo(State.DONE);
            } else {
                GoogleLensModeActivity.this.changeTo(State.READY);
                GoogleLensModeActivity.this.finish();
            }
        }

        @Override // android.app.KeyguardManager.KeyguardDismissCallback
        public void onDismissError() {
            if (CamLog.DEBUG) {
                CamLog.d("Error dismissing keyguard");
            }
            GoogleLensModeActivity.this.changeTo(State.READY);
            GoogleLensModeActivity.this.finish();
        }

        @Override // android.app.KeyguardManager.KeyguardDismissCallback
        public void onDismissCancelled() {
            if (CamLog.DEBUG) {
                CamLog.d("Keyguard dismiss cancelled");
            }
            GoogleLensModeActivity.this.changeTo(State.READY);
            GoogleLensModeActivity.this.finish();
        }
    }
}
