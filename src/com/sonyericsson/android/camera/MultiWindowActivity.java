package com.sonyericsson.android.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Process;
import android.util.SparseArray;
import android.widget.TextView;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.activity.OnActivityResultListener;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MultiWindowActivity extends Activity {
    private static final String TAG = "MultiWindowActivity";
    private SparseArray<OnActivityResultListener> mActivityResultListeners;
    private TextView mMultiWindowMessage;
    private final String[] REQUESTED_PERMISSIONS = {"android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private boolean isForeground = false;
    private boolean grantedPermission = true;
    private boolean isCameraActivityLaunched = false;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.camera_multi_window_mode);
        String string = getResources().getString(getApplicationInfo().labelRes);
        this.mMultiWindowMessage = (TextView) findViewById(R.id.multi_window_message);
        this.mMultiWindowMessage.setText(String.format(getResources().getString(R.string.cam_strings_multi_window_txt), string));
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.isForeground = true;
        if (checkAndRequestSelfPermissions(12, this.REQUESTED_PERMISSIONS)) {
            this.grantedPermission = false;
        } else {
            this.grantedPermission = true;
        }
        if (isInMultiWindowMode() || !this.grantedPermission || this.isCameraActivityLaunched) {
            return;
        }
        launchCamera();
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.isForeground = false;
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z) {
        super.onMultiWindowModeChanged(z);
        if (this.isForeground && !z && this.grantedPermission && !this.isCameraActivityLaunched) {
            launchCamera();
        }
    }

    private void launchCamera() {
        if (checkCameraDisabled()) {
            this.mMultiWindowMessage.setText("");
            showCameraNotAvailableError();
            return;
        }
        this.isCameraActivityLaunched = true;
        Intent intent = new Intent(getIntent());
        String action = intent.getAction();
        if (action == "android.media.action.IMAGE_CAPTURE") {
            intent.setClass(getApplicationContext(), OneshotPhotoActivity.class);
            resetNewTaskFlag(intent);
        } else if (action == "android.media.action.VIDEO_CAPTURE") {
            intent.setClass(getApplicationContext(), OneshotVideoActivity.class);
            resetNewTaskFlag(intent);
        } else if (action == "android.media.action.STILL_IMAGE_CAMERA" || action == "android.media.action.VIDEO_CAMERA") {
            intent.setClass(getApplicationContext(), CameraActivity.class);
        } else {
            if (CamLog.VERBOSE) {
                CamLog.d("No oneshot action found : " + intent);
            }
            intent.setAction("android.intent.action.MAIN");
            intent.setClass(getApplicationContext(), CameraActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        intent.addFlags(33554432);
        startActivity(intent);
        finish();
    }

    private void resetNewTaskFlag(Intent intent) {
        int flags = intent.getFlags();
        if ((268435456 & flags) != 0) {
            intent.setFlags(flags & (-268435457));
        }
    }

    public boolean checkAndRequestSelfPermissions(int i, final String[] strArr) {
        boolean zCheckAndRequestSelfPermissions = PermissionsUtil.checkAndRequestSelfPermissions(this, i, strArr);
        if (zCheckAndRequestSelfPermissions) {
            addActivityResultListener(i, new OnActivityResultListener() { // from class: com.sonyericsson.android.camera.MultiWindowActivity.1
                @Override // com.sonyericsson.cameracommon.activity.OnActivityResultListener
                public boolean onActivityResult(int i2, int i3, Intent intent) {
                    if (i2 != 12 || i3 != -1) {
                        return true;
                    }
                    MultiWindowActivity.this.grantedPermission = PermissionsUtil.arePermissionsGranted(MultiWindowActivity.this, strArr);
                    if (MultiWindowActivity.this.grantedPermission) {
                        return true;
                    }
                    MultiWindowActivity.this.finish();
                    return true;
                }
            });
        }
        return zCheckAndRequestSelfPermissions;
    }

    private boolean addActivityResultListener(int i, OnActivityResultListener onActivityResultListener) {
        if (this.mActivityResultListeners == null) {
            this.mActivityResultListeners = new SparseArray<>();
        }
        if (this.mActivityResultListeners.get(i) != null) {
            return false;
        }
        this.mActivityResultListeners.put(i, onActivityResultListener);
        return true;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.mActivityResultListeners == null) {
            return;
        }
        OnActivityResultListener onActivityResultListener = this.mActivityResultListeners.get(i);
        if (onActivityResultListener != null && onActivityResultListener.onActivityResult(i, i2, intent)) {
            this.mActivityResultListeners.remove(i);
        }
        if (this.mActivityResultListeners.size() == 0) {
            this.mActivityResultListeners = null;
        }
    }

    private boolean checkCameraDisabled() {
        if (!((DevicePolicyManager) getSystemService("device_policy")).getCameraDisabled(null)) {
            return false;
        }
        CamLog.i("[CameraNotAvailable] startCameraOpen: dpm.getCameraDisabled(null)");
        return true;
    }

    private void showCameraNotAvailableError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cam_strings_error_dialog_title_txt);
        builder.setMessage(R.string.cam_strings_use_of_camera_not_authorized_txt);
        AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().addFlags(128);
        alertDialogCreate.setCancelable(true);
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.sonyericsson.android.camera.MultiWindowActivity.2
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                Process.killProcess(Process.myPid());
            }
        });
        alertDialogCreate.show();
    }
}
