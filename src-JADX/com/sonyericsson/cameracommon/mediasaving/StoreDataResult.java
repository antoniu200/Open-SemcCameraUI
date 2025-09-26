package com.sonyericsson.cameracommon.mediasaving;

import android.net.Uri;
import com.sonyericsson.cameracommon.storage.SavingRequest;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class StoreDataResult {
    public final SavingRequest savingRequest;
    public final MediaSavingResult storeResult;
    public final Uri uri;

    public StoreDataResult(MediaSavingResult mediaSavingResult, Uri uri, SavingRequest savingRequest) {
        this.storeResult = mediaSavingResult;
        if (isSuccess()) {
            this.uri = uri;
        } else {
            this.uri = Uri.EMPTY;
        }
        this.savingRequest = savingRequest;
    }

    public int getResultCode() {
        return this.storeResult.mResultCode;
    }

    public boolean isSuccess() {
        return this.storeResult == MediaSavingResult.SUCCESS;
    }

    public int getTextId() {
        return this.storeResult.mTextId;
    }
}
