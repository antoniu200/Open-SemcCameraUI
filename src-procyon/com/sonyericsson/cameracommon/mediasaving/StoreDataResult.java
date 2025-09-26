// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving;

import android.net.Uri;
import com.sonyericsson.cameracommon.storage.SavingRequest;

public class StoreDataResult
{
    public final SavingRequest savingRequest;
    public final MediaSavingResult storeResult;
    public final Uri uri;
    
    public StoreDataResult(final MediaSavingResult storeResult, final Uri uri, final SavingRequest savingRequest) {
        this.storeResult = storeResult;
        if (this.isSuccess()) {
            this.uri = uri;
        }
        else {
            this.uri = Uri.EMPTY;
        }
        this.savingRequest = savingRequest;
    }
    
    public int getResultCode() {
        return this.storeResult.mResultCode;
    }
    
    public int getTextId() {
        return this.storeResult.mTextId;
    }
    
    public boolean isSuccess() {
        return this.storeResult == MediaSavingResult.SUCCESS;
    }
}
