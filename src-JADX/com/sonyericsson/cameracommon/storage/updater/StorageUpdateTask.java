package com.sonyericsson.cameracommon.storage.updater;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import com.sonyericsson.cameracommon.storage.SavingTaskInquiry;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class StorageUpdateTask implements Callable {
    private final OnTaskFinishCallback mCallback;
    private final SavingTaskInquiry mInquiry;
    private final CameraStorageManager.UpdateRequestReason mRequestReason;
    private final Semaphore mStorageAccessSemaphore;
    private final CameraStorageManager mStorageManager;
    private final Storage.StorageType mStorageType;

    public interface OnTaskFinishCallback {
        void onFinish(Storage.StorageType storageType, int i);
    }

    public StorageUpdateTask(@NonNull Storage.StorageType storageType, @NonNull CameraStorageManager cameraStorageManager, @NonNull SavingTaskInquiry savingTaskInquiry, @NonNull Semaphore semaphore, @Nullable OnTaskFinishCallback onTaskFinishCallback, @NonNull CameraStorageManager.UpdateRequestReason updateRequestReason) {
        this.mStorageType = storageType;
        this.mStorageManager = cameraStorageManager;
        this.mInquiry = savingTaskInquiry;
        this.mStorageAccessSemaphore = semaphore;
        this.mCallback = onTaskFinishCallback;
        this.mRequestReason = updateRequestReason;
    }

    protected Storage.StorageType getType() {
        return this.mStorageType;
    }

    protected CameraStorageManager getStorageManager() {
        return this.mStorageManager;
    }

    protected SavingTaskInquiry getInquiry() {
        return this.mInquiry;
    }

    protected CameraStorageManager.UpdateRequestReason getRequestReason() {
        return this.mRequestReason;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0050 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean acquire() throws java.lang.InterruptedException {
        /*
            r5 = this;
            boolean r0 = com.sonyericsson.android.camera.util.CamLog.DEBUG
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L22
            java.lang.String[] r0 = new java.lang.String[r1]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "invoke: id: "
            r3.append(r4)
            int r4 = r5.hashCode()
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r0[r2] = r3
            com.sonyericsson.android.camera.util.CamLog.d(r0)
        L22:
            java.util.concurrent.Semaphore r5 = r5.mStorageAccessSemaphore     // Catch: java.lang.InterruptedException -> L3c
            r3 = 4000(0xfa0, double:1.9763E-320)
            java.util.concurrent.TimeUnit r0 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch: java.lang.InterruptedException -> L3c
            boolean r5 = r5.tryAcquire(r3, r0)     // Catch: java.lang.InterruptedException -> L3c
            boolean r0 = com.sonyericsson.android.camera.util.CamLog.DEBUG     // Catch: java.lang.InterruptedException -> L3a
            if (r0 == 0) goto L43
            java.lang.String r0 = "Semaphore acquired."
            java.lang.String[] r0 = new java.lang.String[]{r0}     // Catch: java.lang.InterruptedException -> L3a
            com.sonyericsson.android.camera.util.CamLog.d(r0)     // Catch: java.lang.InterruptedException -> L3a
            goto L43
        L3a:
            r0 = move-exception
            goto L3e
        L3c:
            r0 = move-exception
            r5 = r2
        L3e:
            java.lang.String r2 = "Unintended interrupt occurred."
            com.sonyericsson.android.camera.util.CamLog.e(r2, r0)
        L43:
            if (r5 != 0) goto L50
            java.lang.RuntimeException r5 = new java.lang.RuntimeException
            java.lang.String r0 = "Semaphore could not be acquired due to timeout"
            r5.<init>(r0)
            r5.fillInStackTrace()
            throw r5
        L50:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.storage.updater.StorageUpdateTask.acquire():boolean");
    }

    protected boolean tryAcquire() {
        if (CamLog.DEBUG) {
            CamLog.d("invoke: id: " + hashCode());
        }
        return this.mStorageAccessSemaphore.tryAcquire();
    }

    protected void release() {
        if (CamLog.DEBUG) {
            CamLog.d("invoke: id: " + hashCode());
        }
        this.mStorageAccessSemaphore.release();
    }

    protected void notifyFinished() {
        if (this.mCallback != null) {
            this.mCallback.onFinish(this.mStorageType, hashCode());
        }
    }
}
