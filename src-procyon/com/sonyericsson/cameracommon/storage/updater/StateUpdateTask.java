// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage.updater;

import java.io.Serializable;
import com.sonyericsson.android.camera.util.CamLog;
import android.support.annotation.Nullable;
import java.util.concurrent.Semaphore;
import com.sonyericsson.cameracommon.storage.SavingTaskInquiry;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import android.support.annotation.NonNull;
import com.sonyericsson.cameracommon.storage.Storage;

public class StateUpdateTask extends StorageUpdateTask
{
    public StateUpdateTask(@NonNull final Storage.StorageType storageType, @NonNull final CameraStorageManager cameraStorageManager, @NonNull final SavingTaskInquiry savingTaskInquiry, @NonNull final Semaphore semaphore, @Nullable final OnTaskFinishCallback onTaskFinishCallback, @NonNull final CameraStorageManager.UpdateRequestReason updateRequestReason) {
        super(storageType, cameraStorageManager, savingTaskInquiry, semaphore, onTaskFinishCallback, updateRequestReason);
    }
    
    @Nullable
    @Override
    public Object call() throws Exception {
        Serializable type = this.getType();
        final long reservedSize = this.getInquiry().getReservedSize((Storage.StorageType)type);
        final CameraStorageManager.UpdateRequestReason requestReason = this.getRequestReason();
        if (requestReason == CameraStorageManager.UpdateRequestReason.APP_LAUNCH) {
            if (this.acquire()) {
                try {
                    try {
                        this.getStorageManager().updateStateByVolumeInfo((Storage.StorageType)type, reservedSize, requestReason);
                        this.getStorageManager().checkAndNotifyStateChanged((Storage.StorageType)type);
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
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append("StateUpdateTask[");
                sb.append(type);
                sb.append("]: One time update failed.");
                CamLog.e(sb.toString());
            }
        }
        else {
            Label_0271: {
                if (reservedSize == 0L && this.tryAcquire()) {
                    try {
                        this.getStorageManager().updateStateByVolumeInfo((Storage.StorageType)type, reservedSize, requestReason);
                        this.getStorageManager().checkAndNotifyStateChanged((Storage.StorageType)type);
                        this.release();
                        break Label_0271;
                    }
                    finally {
                        type = new RuntimeException("trace");
                        ((Throwable)type).fillInStackTrace();
                        CamLog.e("Semaphore could not be released.", (Throwable)type);
                    }
                }
                if (CamLog.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("StateUpdateTask[");
                    sb2.append(type);
                    sb2.append("]: SKIP");
                    CamLog.d(sb2.toString());
                }
            }
            this.getStorageManager().requestVolumeCheck((Storage.StorageType)type, this.getStorageManager().calculateNextPollingInterval(this.getType()), CameraStorageManager.UpdateRequestReason.PERIODIC_UPDATE);
        }
        this.notifyFinished();
        return null;
    }
}
