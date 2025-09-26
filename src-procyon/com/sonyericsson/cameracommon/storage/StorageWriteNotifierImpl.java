// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import com.sonyericsson.android.camera.util.CamLog;

public class StorageWriteNotifierImpl implements StorageWriteNotifier
{
    private static final String TAG = "StorageWriteNotifierImpl";
    private long mMaxIntervalCount;
    private int mProgressCount;
    private final Storage mStorage;
    private final StorageController mStorageController;
    private final StorageType mStorageType;
    
    public StorageWriteNotifierImpl(final Storage mStorage, final StorageType mStorageType, final int n, final StorageController mStorageController) {
        this.mProgressCount = 0;
        this.mMaxIntervalCount = 0L;
        this.mStorage = mStorage;
        this.mStorageType = mStorageType;
        this.mMaxIntervalCount = n;
        this.mStorageController = mStorageController;
    }
    
    @Override
    public StorageType getStorageType() {
        return this.mStorageType;
    }
    
    @Override
    public void notifyWriteStorage() {
        final long availableStorageSize = this.mStorageController.getAvailableStorageSize(this.mStorageType);
        if (this.mProgressCount == 0 || availableStorageSize < 307200L) {
            ((StorageImpl)this.mStorage).onWriteStorage(this.mStorageType);
        }
        if (this.mProgressCount >= this.mMaxIntervalCount) {
            this.mProgressCount = 0;
        }
        else {
            ++this.mProgressCount;
            final StringBuilder sb = new StringBuilder();
            sb.append("mProgressCount is ");
            sb.append(this.mProgressCount);
            CamLog.d(sb.toString());
        }
    }
}
