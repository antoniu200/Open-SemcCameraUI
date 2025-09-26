package com.sonyericsson.android.camera.view;

import android.app.Activity;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.rotatableview.RotatableToast;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ToastContent {
    public static final String TAG = "ToastContent";
    private int mSensorOrientation = 2;
    private RotatableToast mRotatableToast = null;

    public enum ToastID {
        USE_VOLUME_KEY_TO_ZOOM(R.string.cam_strings_zoom_help_txt, 0, RotatableToast.ToastPosition.TOP),
        NEEDS_TO_COOL_DOWN(R.string.cam_strings_error_high_temp_already_high_txt, 1, RotatableToast.ToastPosition.TOP),
        CHANGE_DESTINATION_TO_SAVE(R.string.cam_strings_sd_permission_data_storage_info_txt, 1, RotatableToast.ToastPosition.CENTER);

        private final int mDuration;
        private final int mMessageResourceID;
        private final RotatableToast.ToastPosition mPosition;

        ToastID(int i, int i2, RotatableToast.ToastPosition toastPosition) {
            this.mMessageResourceID = i;
            this.mDuration = i2;
            this.mPosition = toastPosition;
        }
    }

    public void setSensorOrientation(int i) {
        this.mSensorOrientation = i;
        if (this.mRotatableToast != null) {
            this.mRotatableToast.setSensorOrientation(i);
        }
    }

    public void show(Activity activity, ToastID toastID) {
        if (this.mRotatableToast != null) {
            this.mRotatableToast.hideImmediately();
            this.mRotatableToast = null;
        }
        if (activity == null) {
            return;
        }
        this.mRotatableToast = RotatableToast.inflate(activity);
        this.mRotatableToast.setDuration(toastID.mDuration);
        this.mRotatableToast.setTextResId(toastID.mMessageResourceID);
        this.mRotatableToast.setSensorOrientation(this.mSensorOrientation);
        this.mRotatableToast.setToastPosition(toastID.mPosition);
        this.mRotatableToast.show();
    }

    public void closeMessage() {
        if (CamLog.VERBOSE) {
            CamLog.d("closeMessage: , mToast: " + this.mRotatableToast);
        }
        if (this.mRotatableToast != null) {
            this.mRotatableToast.hideImmediately();
            this.mRotatableToast = null;
        }
    }
}
