package com.sonyericsson.cameracommon.mediasaving;

import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum MediaSavingResult {
    SUCCESS(true, -1, -1),
    FAIL(false, R.string.cam_strings_store_fail_txt, 0),
    FAIL_MEMORY_FULL(false, R.string.cam_strings_memory_full_save_failed_txt, 0);

    public final int mResultCode;
    public final boolean mSuccess;
    public final int mTextId;

    MediaSavingResult(boolean z, int i, int i2) {
        this.mSuccess = z;
        this.mTextId = i;
        this.mResultCode = i2;
    }
}
