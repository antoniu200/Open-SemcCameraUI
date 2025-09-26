// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage.updater;

import com.sonyericsson.android.camera.util.CamLog;
import android.support.annotation.Nullable;
import java.util.concurrent.Semaphore;
import com.sonyericsson.cameracommon.storage.SavingTaskInquiry;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import android.support.annotation.NonNull;
import com.sonyericsson.cameracommon.storage.Storage;

public class WriteCheckTask extends StorageUpdateTask
{
    public WriteCheckTask(@NonNull final Storage.StorageType storageType, @NonNull final CameraStorageManager cameraStorageManager, @NonNull final SavingTaskInquiry savingTaskInquiry, @NonNull final Semaphore semaphore, @Nullable final OnTaskFinishCallback onTaskFinishCallback, @NonNull final CameraStorageManager.UpdateRequestReason updateRequestReason) {
        super(storageType, cameraStorageManager, savingTaskInquiry, semaphore, onTaskFinishCallback, updateRequestReason);
    }
    
    @Nullable
    @Override
    public Object call() throws Exception {
        final Storage.StorageType type = this.getType();
        if (this.acquire()) {
            try {
                try {
                    this.getStorageManager().updateStorageStateByWriting(type, this.getRequestReason());
                    this.getStorageManager().checkAndNotifyStateChanged(type);
                    this.release();
                }
                finally {}
            }
            catch (final Throwable t) {
                CamLog.e("occurred exception", t);
                throw t;
            }
            final RuntimeException ex = new RuntimeException("trace no semaphore release");
            ex.fillInStackTrace();
            CamLog.e("Semaphore could not be released.", ex);
        }
        this.notifyFinished();
        return null;
    }
}
