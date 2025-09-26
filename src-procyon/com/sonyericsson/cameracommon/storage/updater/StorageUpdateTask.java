// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage.updater;

import java.util.concurrent.TimeUnit;
import com.sonyericsson.android.camera.util.CamLog;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.concurrent.Semaphore;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import com.sonyericsson.cameracommon.storage.SavingTaskInquiry;
import java.util.concurrent.Callable;

public abstract class StorageUpdateTask implements Callable
{
    private final OnTaskFinishCallback mCallback;
    private final SavingTaskInquiry mInquiry;
    private final CameraStorageManager.UpdateRequestReason mRequestReason;
    private final Semaphore mStorageAccessSemaphore;
    private final CameraStorageManager mStorageManager;
    private final Storage.StorageType mStorageType;
    
    public StorageUpdateTask(@NonNull final Storage.StorageType mStorageType, @NonNull final CameraStorageManager mStorageManager, @NonNull final SavingTaskInquiry mInquiry, @NonNull final Semaphore mStorageAccessSemaphore, @Nullable final OnTaskFinishCallback mCallback, @NonNull final CameraStorageManager.UpdateRequestReason mRequestReason) {
        this.mStorageType = mStorageType;
        this.mStorageManager = mStorageManager;
        this.mInquiry = mInquiry;
        this.mStorageAccessSemaphore = mStorageAccessSemaphore;
        this.mCallback = mCallback;
        this.mRequestReason = mRequestReason;
    }
    
    protected boolean acquire() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke: id: ");
            sb.append(this.hashCode());
            CamLog.d(sb.toString());
        }
        boolean tryAcquire = false;
        Label_0098: {
            try {
                final boolean b = tryAcquire = this.mStorageAccessSemaphore.tryAcquire(4000L, TimeUnit.MILLISECONDS);
                try {
                    if (CamLog.DEBUG) {
                        CamLog.d("Semaphore acquired.");
                        tryAcquire = b;
                    }
                    break Label_0098;
                }
                catch (final InterruptedException ex) {
                    tryAcquire = b;
                }
            }
            catch (final InterruptedException ex) {
                tryAcquire = false;
            }
            final InterruptedException ex;
            CamLog.e("Unintended interrupt occurred.", ex);
        }
        if (!tryAcquire) {
            final RuntimeException ex2 = new RuntimeException("Semaphore could not be acquired due to timeout");
            ex2.fillInStackTrace();
            throw ex2;
        }
        return true;
    }
    
    protected SavingTaskInquiry getInquiry() {
        return this.mInquiry;
    }
    
    protected CameraStorageManager.UpdateRequestReason getRequestReason() {
        return this.mRequestReason;
    }
    
    protected CameraStorageManager getStorageManager() {
        return this.mStorageManager;
    }
    
    protected Storage.StorageType getType() {
        return this.mStorageType;
    }
    
    protected void notifyFinished() {
        if (this.mCallback != null) {
            this.mCallback.onFinish(this.mStorageType, this.hashCode());
        }
    }
    
    protected void release() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke: id: ");
            sb.append(this.hashCode());
            CamLog.d(sb.toString());
        }
        this.mStorageAccessSemaphore.release();
    }
    
    protected boolean tryAcquire() {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invoke: id: ");
            sb.append(this.hashCode());
            CamLog.d(sb.toString());
        }
        return this.mStorageAccessSemaphore.tryAcquire();
    }
    
    public interface OnTaskFinishCallback
    {
        void onFinish(final Storage.StorageType p0, final int p1);
    }
}
