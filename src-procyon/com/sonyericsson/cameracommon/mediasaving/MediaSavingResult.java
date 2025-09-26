// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving;

public enum MediaSavingResult
{
    private static final MediaSavingResult[] $VALUES;
    
    FAIL(false, 2131690172, 0), 
    FAIL_MEMORY_FULL(false, 2131689945, 0), 
    SUCCESS(true, -1, -1);
    
    public final int mResultCode;
    public final boolean mSuccess;
    public final int mTextId;
    
    static {
        $VALUES = new MediaSavingResult[] { MediaSavingResult.SUCCESS, MediaSavingResult.FAIL, MediaSavingResult.FAIL_MEMORY_FULL };
    }
    
    private MediaSavingResult(final boolean mSuccess, final int mTextId, final int mResultCode) {
        this.mSuccess = mSuccess;
        this.mTextId = mTextId;
        this.mResultCode = mResultCode;
    }
}
