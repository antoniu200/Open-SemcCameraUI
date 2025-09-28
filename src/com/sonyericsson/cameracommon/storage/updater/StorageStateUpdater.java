package com.sonyericsson.cameracommon.storage.updater;

import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import com.sonyericsson.cameracommon.storage.SavingTaskInquiry;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.updater.StorageUpdateTask;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class StorageStateUpdater {
    private static final String THREAD_NAME = "SM#State:";
    private Future mAutoUpdateTask;
    private final ScheduledExecutorService mBackgroundUpdater;
    private final SavingTaskInquiry mInquiry;
    private boolean mIsAutoUpdateEnabled;
    private final Semaphore mSemaphore;
    private final CameraStorageManager mStorageManager;
    private final Storage.StorageType mType;
    private final Queue<Future> mStackedTask = new LinkedList();
    private final StorageUpdateTask.OnTaskFinishCallback mCallback = new StorageUpdateTask.OnTaskFinishCallback() { // from class: com.sonyericsson.cameracommon.storage.updater.StorageStateUpdater.1
        @Override // com.sonyericsson.cameracommon.storage.updater.StorageUpdateTask.OnTaskFinishCallback
        public void onFinish(Storage.StorageType storageType, int i) {
            synchronized (StorageStateUpdater.this.mStackedTask) {
                if (CamLog.DEBUG) {
                    CamLog.d("type: " + storageType + ", id: " + i);
                }
                StorageStateUpdater.this.mStackedTask.poll();
            }
        }
    };

    public StorageStateUpdater(@NonNull Storage.StorageType storageType, @NonNull CameraStorageManager cameraStorageManager, @NonNull SavingTaskInquiry savingTaskInquiry, @NonNull Semaphore semaphore) {
        this.mType = storageType;
        this.mStorageManager = cameraStorageManager;
        this.mInquiry = savingTaskInquiry;
        this.mSemaphore = semaphore;
        this.mBackgroundUpdater = ThreadUtil.buildScheduledExecutor(THREAD_NAME + storageType);
    }

    public void release() {
        this.mBackgroundUpdater.shutdownNow();
        clearStorageUpdateTask();
    }

    public void requestVolumeCheck(CameraStorageManager.UpdateInterval updateInterval, CameraStorageManager.UpdateRequestReason updateRequestReason) {
        if (updateRequestReason == CameraStorageManager.UpdateRequestReason.APP_LAUNCH) {
            StateUpdateTask stateUpdateTask = new StateUpdateTask(this.mType, this.mStorageManager, this.mInquiry, this.mSemaphore, this.mCallback, updateRequestReason);
            synchronized (this.mStackedTask) {
                if (CamLog.DEBUG) {
                    CamLog.d("submit StateUpdateTask.");
                }
                this.mStackedTask.add(this.mBackgroundUpdater.submit(stateUpdateTask));
            }
            return;
        }
        if (this.mIsAutoUpdateEnabled) {
            if (this.mAutoUpdateTask != null) {
                this.mAutoUpdateTask.cancel(false);
                this.mAutoUpdateTask = null;
            }
            if (this.mBackgroundUpdater == null || updateInterval == CameraStorageManager.UpdateInterval.STOP) {
                return;
            }
            this.mAutoUpdateTask = this.mBackgroundUpdater.schedule(new StateUpdateTask(this.mType, this.mStorageManager, this.mInquiry, this.mSemaphore, null, updateRequestReason), updateInterval.getIntervalMillis(), TimeUnit.MILLISECONDS);
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("Rejected the update for storage state.");
        }
    }

    public synchronized void requestWriteCheck(CameraStorageManager.UpdateRequestReason updateRequestReason) {
        WriteCheckTask writeCheckTask = new WriteCheckTask(this.mType, this.mStorageManager, this.mInquiry, this.mSemaphore, this.mCallback, updateRequestReason);
        synchronized (this.mStackedTask) {
            if (CamLog.DEBUG) {
                CamLog.d("submit WriteCheckTask.");
            }
            this.mStackedTask.add(this.mBackgroundUpdater.submit(writeCheckTask));
        }
    }

    public void setAutoUpdateEnabled(boolean z) {
        this.mIsAutoUpdateEnabled = z;
        if (this.mIsAutoUpdateEnabled) {
            requestVolumeCheck(this.mStorageManager.calculateNextPollingInterval(this.mType), CameraStorageManager.UpdateRequestReason.PERIODIC_UPDATE);
        } else {
            clearStorageUpdateTask();
        }
    }

    @NonNull
    public final Semaphore getAccessSemaphore() {
        return this.mSemaphore;
    }

    private void clearStorageUpdateTask() {
        if (this.mAutoUpdateTask != null) {
            this.mAutoUpdateTask.cancel(false);
            this.mAutoUpdateTask = null;
        }
        synchronized (this.mStackedTask) {
            if (CamLog.DEBUG) {
                CamLog.d("cancel " + this.mStackedTask.size() + " tasks");
            }
            Iterator<Future> it = this.mStackedTask.iterator();
            while (it.hasNext()) {
                it.next().cancel(false);
            }
            this.mStackedTask.clear();
        }
    }
}
