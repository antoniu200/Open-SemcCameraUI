// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage.updater;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.LinkedList;
import android.support.annotation.NonNull;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import com.sonyericsson.cameracommon.storage.SavingTaskInquiry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Future;

public class StorageStateUpdater
{
    private static final String THREAD_NAME = "SM#State:";
    private Future mAutoUpdateTask;
    private final ScheduledExecutorService mBackgroundUpdater;
    private final StorageUpdateTask.OnTaskFinishCallback mCallback;
    private final SavingTaskInquiry mInquiry;
    private boolean mIsAutoUpdateEnabled;
    private final Semaphore mSemaphore;
    private final Queue<Future> mStackedTask;
    private final CameraStorageManager mStorageManager;
    private final Storage.StorageType mType;
    
    public StorageStateUpdater(@NonNull final Storage.StorageType storageType, @NonNull final CameraStorageManager mStorageManager, @NonNull final SavingTaskInquiry mInquiry, @NonNull final Semaphore mSemaphore) {
        this.mStackedTask = new LinkedList<Future>();
        this.mCallback = new StorageUpdateTask.OnTaskFinishCallback() {
            final StorageStateUpdater this$0;
            
            @Override
            public void onFinish(final Storage.StorageType obj, final int i) {
                synchronized (this.this$0.mStackedTask) {
                    if (CamLog.DEBUG) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("type: ");
                        sb.append(obj);
                        sb.append(", id: ");
                        sb.append(i);
                        CamLog.d(sb.toString());
                    }
                    this.this$0.mStackedTask.poll();
                }
            }
        };
        this.mType = storageType;
        this.mStorageManager = mStorageManager;
        this.mInquiry = mInquiry;
        this.mSemaphore = mSemaphore;
        final StringBuilder sb = new StringBuilder();
        sb.append("SM#State:");
        sb.append(storageType);
        this.mBackgroundUpdater = ThreadUtil.buildScheduledExecutor(sb.toString());
    }
    
    private void clearStorageUpdateTask() {
        if (this.mAutoUpdateTask != null) {
            this.mAutoUpdateTask.cancel(false);
            this.mAutoUpdateTask = null;
        }
        synchronized (this.mStackedTask) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("cancel ");
                sb.append(this.mStackedTask.size());
                sb.append(" tasks");
                CamLog.d(sb.toString());
            }
            final Iterator<Object> iterator = this.mStackedTask.iterator();
            while (iterator.hasNext()) {
                iterator.next().cancel(false);
            }
            this.mStackedTask.clear();
        }
    }
    
    @NonNull
    public final Semaphore getAccessSemaphore() {
        return this.mSemaphore;
    }
    
    public void release() {
        this.mBackgroundUpdater.shutdownNow();
        this.clearStorageUpdateTask();
    }
    
    public void requestVolumeCheck(final CameraStorageManager.UpdateInterval updateInterval, final CameraStorageManager.UpdateRequestReason updateRequestReason) {
        if (updateRequestReason == CameraStorageManager.UpdateRequestReason.APP_LAUNCH) {
            final StateUpdateTask stateUpdateTask = new StateUpdateTask(this.mType, this.mStorageManager, this.mInquiry, this.mSemaphore, this.mCallback, updateRequestReason);
            synchronized (this.mStackedTask) {
                if (CamLog.DEBUG) {
                    CamLog.d("submit StateUpdateTask.");
                }
                this.mStackedTask.add(this.mBackgroundUpdater.submit((Callable<Object>)stateUpdateTask));
                return;
            }
        }
        if (this.mIsAutoUpdateEnabled) {
            if (this.mAutoUpdateTask != null) {
                this.mAutoUpdateTask.cancel(false);
                this.mAutoUpdateTask = null;
            }
            if (this.mBackgroundUpdater != null && updateInterval != CameraStorageManager.UpdateInterval.STOP) {
                this.mAutoUpdateTask = this.mBackgroundUpdater.schedule((Callable<Object>)new StateUpdateTask(this.mType, this.mStorageManager, this.mInquiry, this.mSemaphore, null, updateRequestReason), updateInterval.getIntervalMillis(), TimeUnit.MILLISECONDS);
            }
        }
        else if (CamLog.DEBUG) {
            CamLog.d("Rejected the update for storage state.");
        }
    }
    
    public void requestWriteCheck(final CameraStorageManager.UpdateRequestReason updateRequestReason) {
        synchronized (this) {
            final WriteCheckTask writeCheckTask = new WriteCheckTask(this.mType, this.mStorageManager, this.mInquiry, this.mSemaphore, this.mCallback, updateRequestReason);
            synchronized (this.mStackedTask) {
                if (CamLog.DEBUG) {
                    CamLog.d("submit WriteCheckTask.");
                }
                this.mStackedTask.add(this.mBackgroundUpdater.submit((Callable<Object>)writeCheckTask));
            }
        }
    }
    
    public void setAutoUpdateEnabled(final boolean mIsAutoUpdateEnabled) {
        this.mIsAutoUpdateEnabled = mIsAutoUpdateEnabled;
        if (this.mIsAutoUpdateEnabled) {
            this.requestVolumeCheck(this.mStorageManager.calculateNextPollingInterval(this.mType), CameraStorageManager.UpdateRequestReason.PERIODIC_UPDATE);
        }
        else {
            this.clearStorageUpdateTask();
        }
    }
}
