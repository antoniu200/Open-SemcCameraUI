// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import android.app.Activity;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.rotatableview.RotatableToast;

public class ToastContent
{
    public static final String TAG = "ToastContent";
    private RotatableToast mRotatableToast;
    private int mSensorOrientation;
    
    public ToastContent() {
        this.mSensorOrientation = 2;
        this.mRotatableToast = null;
    }
    
    public void closeMessage() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("closeMessage: , mToast: ");
            sb.append(this.mRotatableToast);
            CamLog.d(sb.toString());
        }
        if (this.mRotatableToast != null) {
            this.mRotatableToast.hideImmediately();
            this.mRotatableToast = null;
        }
    }
    
    public void setSensorOrientation(final int n) {
        this.mSensorOrientation = n;
        if (this.mRotatableToast != null) {
            this.mRotatableToast.setSensorOrientation(n);
        }
    }
    
    public void show(final Activity activity, final ToastID toastID) {
        if (this.mRotatableToast != null) {
            this.mRotatableToast.hideImmediately();
            this.mRotatableToast = null;
        }
        if (activity == null) {
            return;
        }
        (this.mRotatableToast = RotatableToast.inflate(activity)).setDuration(toastID.mDuration);
        this.mRotatableToast.setTextResId(toastID.mMessageResourceID);
        this.mRotatableToast.setSensorOrientation(this.mSensorOrientation);
        this.mRotatableToast.setToastPosition(toastID.mPosition);
        this.mRotatableToast.show();
    }
    
    public enum ToastID
    {
        private static final ToastID[] $VALUES;
        
        CHANGE_DESTINATION_TO_SAVE(2131690094, 1, RotatableToast.ToastPosition.CENTER), 
        NEEDS_TO_COOL_DOWN(2131689774, 1, RotatableToast.ToastPosition.TOP), 
        USE_VOLUME_KEY_TO_ZOOM(2131690262, 0, RotatableToast.ToastPosition.TOP);
        
        private final int mDuration;
        private final int mMessageResourceID;
        private final RotatableToast.ToastPosition mPosition;
        
        static {
            $VALUES = new ToastID[] { ToastID.USE_VOLUME_KEY_TO_ZOOM, ToastID.NEEDS_TO_COOL_DOWN, ToastID.CHANGE_DESTINATION_TO_SAVE };
        }
        
        private ToastID(final int mMessageResourceID, final int mDuration, final RotatableToast.ToastPosition mPosition) {
            this.mMessageResourceID = mMessageResourceID;
            this.mDuration = mDuration;
            this.mPosition = mPosition;
        }
    }
}
