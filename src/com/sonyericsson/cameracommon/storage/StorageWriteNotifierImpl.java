package com.sonyericsson.cameracommon.storage;

import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.Storage;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class StorageWriteNotifierImpl implements Storage.StorageWriteNotifier {
    private static final String TAG = "StorageWriteNotifierImpl";
    private long mMaxIntervalCount;
    private int mProgressCount = 0;
    private final Storage mStorage;
    private final StorageController mStorageController;
    private final Storage.StorageType mStorageType;

    public StorageWriteNotifierImpl(Storage storage, Storage.StorageType storageType, int i, StorageController storageController) {
        this.mMaxIntervalCount = 0L;
        this.mStorage = storage;
        this.mStorageType = storageType;
        this.mMaxIntervalCount = i;
        this.mStorageController = storageController;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage.StorageWriteNotifier
    public Storage.StorageType getStorageType() {
        return this.mStorageType;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage.StorageWriteNotifier
    public void notifyWriteStorage() {
        long availableStorageSize = this.mStorageController.getAvailableStorageSize(this.mStorageType);
        if (this.mProgressCount == 0 || availableStorageSize < 307200) {
            ((StorageImpl) this.mStorage).onWriteStorage(this.mStorageType);
        }
        if (this.mProgressCount >= this.mMaxIntervalCount) {
            this.mProgressCount = 0;
            return;
        }
        this.mProgressCount++;
        CamLog.d("mProgressCount is " + this.mProgressCount);
    }
}
