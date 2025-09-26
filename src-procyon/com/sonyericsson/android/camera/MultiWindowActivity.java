// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import android.os.Bundle;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import android.app.AlertDialog;
import android.os.Process;
import android.content.DialogInterface;
import android.content.DialogInterface$OnCancelListener;
import android.content.Context;
import android.app.AlertDialog$Builder;
import android.content.Intent;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.ComponentName;
import android.app.admin.DevicePolicyManager;
import android.widget.TextView;
import com.sonyericsson.cameracommon.activity.OnActivityResultListener;
import android.util.SparseArray;
import android.app.Activity;

public class MultiWindowActivity extends Activity
{
    private static final String TAG = "MultiWindowActivity";
    private final String[] REQUESTED_PERMISSIONS;
    private boolean grantedPermission;
    private boolean isCameraActivityLaunched;
    private boolean isForeground;
    private SparseArray<OnActivityResultListener> mActivityResultListeners;
    private TextView mMultiWindowMessage;
    
    public MultiWindowActivity() {
        this.REQUESTED_PERMISSIONS = new String[] { "android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE" };
        this.isForeground = false;
        this.grantedPermission = true;
        this.isCameraActivityLaunched = false;
    }
    
    private boolean addActivityResultListener(final int n, final OnActivityResultListener onActivityResultListener) {
        if (this.mActivityResultListeners == null) {
            this.mActivityResultListeners = (SparseArray<OnActivityResultListener>)new SparseArray();
        }
        if (this.mActivityResultListeners.get(n) != null) {
            return false;
        }
        this.mActivityResultListeners.put(n, (Object)onActivityResultListener);
        return true;
    }
    
    private boolean checkCameraDisabled() {
        if (((DevicePolicyManager)this.getSystemService("device_policy")).getCameraDisabled((ComponentName)null)) {
            CamLog.i("[CameraNotAvailable] startCameraOpen: dpm.getCameraDisabled(null)");
            return true;
        }
        return false;
    }
    
    private void launchCamera() {
        if (this.checkCameraDisabled()) {
            this.mMultiWindowMessage.setText((CharSequence)"");
            this.showCameraNotAvailableError();
            return;
        }
        this.isCameraActivityLaunched = true;
        final Intent obj = new Intent(this.getIntent());
        final String action = obj.getAction();
        if (action == "android.media.action.IMAGE_CAPTURE") {
            obj.setClass(this.getApplicationContext(), (Class)OneshotPhotoActivity.class);
            this.resetNewTaskFlag(obj);
        }
        else if (action == "android.media.action.VIDEO_CAPTURE") {
            obj.setClass(this.getApplicationContext(), (Class)OneshotVideoActivity.class);
            this.resetNewTaskFlag(obj);
        }
        else {
            if (action != "android.media.action.STILL_IMAGE_CAMERA" && action != "android.media.action.VIDEO_CAMERA") {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("No oneshot action found : ");
                    sb.append(obj);
                    CamLog.d(sb.toString());
                }
                obj.setAction("android.intent.action.MAIN");
                obj.setClass(this.getApplicationContext(), (Class)CameraActivity.class);
                this.startActivity(obj);
                this.finish();
                return;
            }
            obj.setClass(this.getApplicationContext(), (Class)CameraActivity.class);
        }
        obj.addFlags(33554432);
        this.startActivity(obj);
        this.finish();
    }
    
    private void resetNewTaskFlag(final Intent intent) {
        final int flags = intent.getFlags();
        if ((0x10000000 & flags) != 0x0) {
            intent.setFlags(flags & 0xEFFFFFFF);
        }
    }
    
    private void showCameraNotAvailableError() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setTitle(2131689770);
        alertDialog$Builder.setMessage(2131690207);
        final AlertDialog create = alertDialog$Builder.create();
        create.getWindow().addFlags(128);
        create.setCancelable(true);
        create.setCanceledOnTouchOutside(false);
        create.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener(this) {
            final MultiWindowActivity this$0;
            
            public void onCancel(final DialogInterface dialogInterface) {
                Process.killProcess(Process.myPid());
            }
        });
        create.show();
    }
    
    public boolean checkAndRequestSelfPermissions(final int n, final String[] array) {
        final boolean checkAndRequestSelfPermissions = PermissionsUtil.checkAndRequestSelfPermissions(this, n, array);
        if (checkAndRequestSelfPermissions) {
            this.addActivityResultListener(n, new OnActivityResultListener(this, array) {
                final MultiWindowActivity this$0;
                final String[] val$permissions;
                
                @Override
                public boolean onActivityResult(final int n, final int n2, final Intent intent) {
                    if (n == 12) {
                        if (n2 == -1) {
                            this.this$0.grantedPermission = PermissionsUtil.arePermissionsGranted((Context)this.this$0, this.val$permissions);
                            if (!this.this$0.grantedPermission) {
                                this.this$0.finish();
                            }
                        }
                    }
                    return true;
                }
            });
        }
        return checkAndRequestSelfPermissions;
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (this.mActivityResultListeners == null) {
            return;
        }
        final OnActivityResultListener onActivityResultListener = (OnActivityResultListener)this.mActivityResultListeners.get(n);
        if (onActivityResultListener != null && onActivityResultListener.onActivityResult(n, n2, intent)) {
            this.mActivityResultListeners.remove(n);
        }
        if (this.mActivityResultListeners.size() == 0) {
            this.mActivityResultListeners = null;
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131492902);
        (this.mMultiWindowMessage = (TextView)this.findViewById(2131296475)).setText((CharSequence)String.format(this.getResources().getString(2131689955), this.getResources().getString(this.getApplicationInfo().labelRes)));
    }
    
    public void onMultiWindowModeChanged(final boolean b) {
        super.onMultiWindowModeChanged(b);
        if (!this.isForeground) {
            return;
        }
        if (!b && this.grantedPermission && !this.isCameraActivityLaunched) {
            this.launchCamera();
        }
    }
    
    protected void onPause() {
        super.onPause();
        this.isForeground = false;
    }
    
    protected void onResume() {
        super.onResume();
        this.isForeground = true;
        if (this.checkAndRequestSelfPermissions(12, this.REQUESTED_PERMISSIONS)) {
            this.grantedPermission = false;
        }
        else {
            this.grantedPermission = true;
        }
        if (!this.isInMultiWindowMode() && this.grantedPermission && !this.isCameraActivityLaunched) {
            this.launchCamera();
        }
    }
}
