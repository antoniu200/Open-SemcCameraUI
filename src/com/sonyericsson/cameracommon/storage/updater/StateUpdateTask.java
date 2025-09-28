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
public class StateUpdateTask extends StorageUpdateTask {
    public StateUpdateTask(@NonNull Storage.StorageType storageType, @NonNull CameraStorageManager cameraStorageManager, @NonNull SavingTaskInquiry savingTaskInquiry, @NonNull Semaphore semaphore, @Nullable StorageUpdateTask.OnTaskFinishCallback onTaskFinishCallback, @NonNull CameraStorageManager.UpdateRequestReason updateRequestReason) {
        super(storageType, cameraStorageManager, savingTaskInquiry, semaphore, onTaskFinishCallback, updateRequestReason);
    }

    @Override // java.util.concurrent.Callable
    @Nullable
    public Object call() throws Exception {
        Storage.StorageType type = getType();
        long reservedSize = getInquiry().getReservedSize(type);
        CameraStorageManager.UpdateRequestReason requestReason = getRequestReason();
        if (requestReason == CameraStorageManager.UpdateRequestReason.APP_LAUNCH) {
            if (acquire()) {
                try {
                    try {
                        getStorageManager().updateStateByVolumeInfo(type, reservedSize, requestReason);
                        getStorageManager().checkAndNotifyStateChanged(type);
                        release();
                    } finally {
                    }
                } catch (Throwable th) {
                    RuntimeException runtimeException = new RuntimeException("trace no semaphore release");
                    runtimeException.fillInStackTrace();
                    CamLog.e("Semaphore could not be released.", runtimeException);
                    throw th;
                }
            } else {
                CamLog.e("StateUpdateTask[" + type + "]: One time update failed.");
            }
        } else {
            if (reservedSize == 0 && tryAcquire()) {
                try {
                    getStorageManager().updateStateByVolumeInfo(type, reservedSize, requestReason);
                    getStorageManager().checkAndNotifyStateChanged(type);
                    release();
                } catch (Throwable th2) {
                    RuntimeException runtimeException2 = new RuntimeException("trace");
                    runtimeException2.fillInStackTrace();
                    CamLog.e("Semaphore could not be released.", runtimeException2);
                    throw th2;
                }
            } else if (CamLog.DEBUG) {
                CamLog.d("StateUpdateTask[" + type + "]: SKIP");
            }
            getStorageManager().requestVolumeCheck(type, getStorageManager().calculateNextPollingInterval(getType()), CameraStorageManager.UpdateRequestReason.PERIODIC_UPDATE);
        }
        notifyFinished();
        return null;
    }
}
