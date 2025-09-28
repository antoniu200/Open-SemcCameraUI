package com.sonyericsson.cameracommon.storage.updater;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import com.sonyericsson.cameracommon.storage.SavingTaskInquiry;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.updater.StorageUpdateTask;
import java.util.concurrent.Semaphore;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class WriteCheckTask extends StorageUpdateTask {
    public WriteCheckTask(@NonNull Storage.StorageType storageType, @NonNull CameraStorageManager cameraStorageManager, @NonNull SavingTaskInquiry savingTaskInquiry, @NonNull Semaphore semaphore, @Nullable StorageUpdateTask.OnTaskFinishCallback onTaskFinishCallback, @NonNull CameraStorageManager.UpdateRequestReason updateRequestReason) {
        super(storageType, cameraStorageManager, savingTaskInquiry, semaphore, onTaskFinishCallback, updateRequestReason);
    }

    @Override // java.util.concurrent.Callable
    @Nullable
    public Object call() throws Exception {
        Storage.StorageType type = getType();
        try {
            if (acquire()) {
                try {
                    getStorageManager().updateStorageStateByWriting(type, getRequestReason());
                    getStorageManager().checkAndNotifyStateChanged(type);
                    release();
                } catch (Throwable th) {
                    CamLog.e("occurred exception", th);
                    throw th;
                }
            }
            notifyFinished();
            return null;
        } catch (Throwable th2) {
            RuntimeException runtimeException = new RuntimeException("trace no semaphore release");
            runtimeException.fillInStackTrace();
            CamLog.e("Semaphore could not be released.", runtimeException);
            throw th2;
        }
    }
}
