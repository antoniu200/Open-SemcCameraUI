package com.sonyericsson.cameracommon.storage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class StorageImpl implements Storage, CameraApplication.Pausable {
    private static final int DATA_LOAD_TASK_SIZE = 1;
    private static final int MULTI_STORAGE_ACCESS_PERMIT_NUM = 2;
    static final long NO_INTERVAL_REMAIN_THRESHOLD = 307200;
    private static final int SINGLE_STORAGE_ACCESS_PERMIT_NUM = 1;
    private static final String TAG = "StrorageImpl";
    private static final String THREAD_NAME_DATE_LOADER_TASK = "DataLoaderTask";
    private Context mContext;
    private ExecutorService mDataLoadExecutor;
    private LinkedBlockingDeque<Future<Long>> mDataLoaderTaskQueue;
    private SavingTaskManager mSavingTaskManager;
    private StorageController mStorageController;
    private CameraStorageManager mCameraStorageManager = null;
    private StorageBroadcastReceiver mStorageBroadcastReceiver = new StorageBroadcastReceiver();
    private Map<Storage.StorageType, Semaphore> mStorageAccessSemaphoreMap = new HashMap();
    private final Object mRequestLock = new Object();

    private class StorageBroadcastReceiver extends BroadcastReceiver {
        private StorageBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) throws InterruptedException {
            String action = intent.getAction();
            String path = intent.getData().getPath();
            Storage.StorageType storageTypeFromPath = StorageUtil.getStorageTypeFromPath(path, context);
            CamLog.i("Action = " + action + ", Type = " + storageTypeFromPath + ", Path = " + path);
            if (action.equals("android.intent.action.MEDIA_MOUNTED")) {
                notifyStorageStatusChanged(storageTypeFromPath, action, path);
                return;
            }
            if (action.equals("android.intent.action.MEDIA_UNMOUNTED")) {
                notifyStorageStatusChanged(storageTypeFromPath, action, path);
            } else if (action.equals("android.intent.action.MEDIA_EJECT")) {
                notifyStorageStatusChanged(storageTypeFromPath, action, path);
            } else if (action.equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
                notifyStorageStatusChanged(storageTypeFromPath, action, path);
            }
        }

        private void notifyStorageStatusChanged(Storage.StorageType storageType, String str, String str2) throws InterruptedException {
            boolean z;
            Iterator<Storage.StorageType> it = StorageUtil.getMountableStorageTypes().iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                } else if (it.next() == storageType) {
                    z = true;
                    break;
                }
            }
            if (z) {
                if (StorageImpl.this.isStorageReadable()) {
                    if (str.equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
                        StorageImpl.this.mStorageController.checkAndNotifyStateChanged(storageType, true);
                    }
                    if (StorageImpl.this.mCameraStorageManager != null) {
                        StorageImpl.this.mCameraStorageManager.updateStorageStateByAction(str, storageType);
                        return;
                    }
                    return;
                }
                CamLog.i("onReceive: storage is not activated.");
                return;
            }
            CamLog.i("StorageType is not mountable. action = " + str + " path=" + str2);
        }
    }

    private class StorageInitializeThread extends Thread {
        private static final String THREAD_NAME_STORAGE_INITIALIZE = "SM#initTask";

        public StorageInitializeThread() {
            setName(THREAD_NAME_STORAGE_INITIALIZE);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            StorageImpl.this.initialize();
        }
    }

    CameraStorageManager getCameraStorageManager() {
        return this.mCameraStorageManager;
    }

    public void open(Context context) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl open");
        }
        this.mContext = context;
        for (Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
            if (storageType == Storage.StorageType.INTERNAL) {
                this.mStorageAccessSemaphoreMap.put(storageType, new Semaphore(2, true));
            } else {
                this.mStorageAccessSemaphoreMap.put(storageType, new Semaphore(1, true));
            }
        }
        this.mStorageController = new StorageController();
        this.mCameraStorageManager = new CameraStorageManager(context, this.mStorageController);
        this.mSavingTaskManager = new SavingTaskManager(context, this.mCameraStorageManager, this.mStorageAccessSemaphoreMap);
        prepareReceiver();
        this.mDataLoaderTaskQueue = new LinkedBlockingDeque<>(1);
        this.mDataLoadExecutor = ThreadUtil.buildExecutor(THREAD_NAME_DATE_LOADER_TASK);
        new StorageInitializeThread().start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initialize() {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl initialize");
        }
        this.mCameraStorageManager.initialize(this.mSavingTaskManager.getInquiry(), this.mStorageAccessSemaphoreMap);
    }

    private void prepareReceiver() {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl prepareReceiver");
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addDataScheme("file");
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_EJECT");
        intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
        this.mContext.registerReceiver(this.mStorageBroadcastReceiver, intentFilter);
    }

    public void close() {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl close");
        }
        release();
    }

    private void releaseReceiver() {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl releaseReceiver");
        }
        this.mContext.unregisterReceiver(this.mStorageBroadcastReceiver);
    }

    private void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl release");
        }
        if (this.mDataLoadExecutor != null) {
            this.mDataLoadExecutor.shutdownNow();
            this.mDataLoadExecutor = null;
        }
        if (this.mDataLoaderTaskQueue != null) {
            synchronized (this.mRequestLock) {
                this.mDataLoaderTaskQueue.clear();
            }
            this.mDataLoaderTaskQueue = null;
        }
        releaseReceiver();
        this.mStorageController.release();
        this.mStorageController = null;
        this.mCameraStorageManager.release();
        this.mCameraStorageManager = null;
        this.mSavingTaskManager.release();
        this.mSavingTaskManager = null;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public boolean requestStore(SavingRequest savingRequest, Storage.StorageType storageType, Storage.OnStoreCompletedListener onStoreCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("requestStore");
        }
        if (!this.mSavingTaskManager.canPushStoreTask(storageType)) {
            return false;
        }
        savingRequest.addCallback(onStoreCompletedListener);
        if (savingRequest instanceof VideoSavingRequest) {
            if (CamLog.VERBOSE) {
                CamLog.d("StorageImpl storeVideo");
            }
            this.mSavingTaskManager.storeVideo((VideoSavingRequest) savingRequest);
            return true;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl Photo");
        }
        this.mSavingTaskManager.storePicture((PhotoSavingRequest) savingRequest);
        return true;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void requestLoad(final Uri uri, int i, final Storage.OnLoadCompletedListener onLoadCompletedListener) throws Throwable {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestLoad");
        }
        final Bitmap bitmapLoad = new ImageLoader(this.mContext, uri, i).load();
        new Handler().post(new Runnable() { // from class: com.sonyericsson.cameracommon.storage.StorageImpl.1
            @Override // java.lang.Runnable
            public void run() {
                if (onLoadCompletedListener != null) {
                    if (bitmapLoad != null) {
                        onLoadCompletedListener.onLoadCompleted(uri, bitmapLoad);
                    } else {
                        onLoadCompletedListener.onLoadFailed(uri, 0);
                    }
                }
            }
        });
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void requestLoad(byte[] bArr, int i, final Storage.OnLoadCompletedListener onLoadCompletedListener) throws Throwable {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestLoad");
        }
        final Bitmap bitmapLoad = new ImageLoader(this.mContext, bArr, i).load();
        new Handler().post(new Runnable() { // from class: com.sonyericsson.cameracommon.storage.StorageImpl.2
            @Override // java.lang.Runnable
            public void run() {
                if (onLoadCompletedListener != null) {
                    onLoadCompletedListener.onLoadCompleted(Uri.EMPTY, bitmapLoad);
                }
            }
        });
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void cancelDataLoad(boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl cancelDataLoad");
        }
        if (this.mDataLoaderTaskQueue != null) {
            synchronized (this.mRequestLock) {
                Iterator<Future<Long>> it = this.mDataLoaderTaskQueue.iterator();
                while (it.hasNext()) {
                    it.next().cancel(z);
                }
                this.mDataLoaderTaskQueue.clear();
            }
        }
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void requestDataLoad(int i, boolean z, Storage.OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestDataLoad");
        }
        loadData(new DataLoader(this.mContext, this.mCameraStorageManager.getReadableStoragePaths(), i, onLoadCompletedListener, z));
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void requestDataLoad(int i, Uri uri, boolean z, Storage.OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestDataLoad");
        }
        loadData(new DataLoader(i, uri, this.mContext, onLoadCompletedListener, z));
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void requestLastDataLoad(int i, boolean z, Storage.OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestDataLoad");
        }
        loadData(new DataLoader(this.mContext, this.mCameraStorageManager.getReadableStoragePaths(), i, 0, onLoadCompletedListener, z));
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void requestCreateContentInfoSync(ArrayList<Uri> arrayList, Storage.OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestCreateContentInfoSync");
        }
        try {
            new DataLoader(this.mContext, arrayList, onLoadCompletedListener, true).call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void cancelDataLoad(long j) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl cancelDataLoad");
        }
        removeFuture(j);
    }

    private void removeFuture(long j) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl removeFuture");
        }
        if (this.mDataLoaderTaskQueue == null) {
            return;
        }
        synchronized (this.mRequestLock) {
            Iterator<Future<Long>> it = this.mDataLoaderTaskQueue.iterator();
            while (it.hasNext()) {
                Future<Long> next = it.next();
                if (next.isDone() && !next.isCancelled()) {
                    long jLongValue = 0;
                    try {
                        jLongValue = next.get().longValue();
                    } catch (InterruptedException unused) {
                        if (CamLog.VERBOSE) {
                            CamLog.d(TAG, "InterruptedException at future.get().");
                        }
                    } catch (ExecutionException unused2) {
                        if (CamLog.VERBOSE) {
                            CamLog.d(TAG, "ExecutionException at future.get().");
                        }
                    }
                    if (jLongValue == j) {
                        if (CamLog.VERBOSE) {
                            CamLog.d(TAG, "remove queue. id = " + jLongValue);
                        }
                        it.remove();
                    }
                }
            }
        }
    }

    private void loadData(DataLoader dataLoader) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl loadData");
        }
        if (this.mDataLoadExecutor == null || this.mDataLoaderTaskQueue == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("mDataLoadExecutor or mDataLoadQueue is null");
                return;
            }
            return;
        }
        Future<Long> futureSubmit = this.mDataLoadExecutor.submit(dataLoader);
        synchronized (this.mRequestLock) {
            if (!this.mDataLoaderTaskQueue.offerLast(futureSubmit)) {
                Future<Long> futurePollFirst = this.mDataLoaderTaskQueue.pollFirst();
                if (futurePollFirst != null) {
                    futurePollFirst.cancel(true);
                }
                this.mDataLoaderTaskQueue.addLast(futureSubmit);
            }
        }
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void addStorageStateListener(Storage.StorageStateListener storageStateListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl addStorageStateListener");
        }
        this.mStorageController.addStorageListener(storageStateListener);
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void removeStorageStateListener(Storage.StorageStateListener storageStateListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl removeStorageStateListener");
        }
        this.mStorageController.removeStorageListener(storageStateListener);
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public Storage.StorageState getCurrentState(Storage.StorageType storageType) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl getCurrentState");
        }
        return this.mStorageController.getStorageState(storageType);
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public long getRemainStorage(Storage.StorageType storageType) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl getRemainStorage");
        }
        this.mCameraStorageManager.checkRemain(false, storageType);
        return this.mStorageController.getAvailableStorageSize(storageType);
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public boolean checkValidUri(Uri uri) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl isIllegalUri");
        }
        return StorageUtil.getPathFromUri(this.mContext, uri) != null;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public boolean canPushStoreRequest(Storage.StorageType storageType) {
        if (isStorageActivated() && getAvailableStorage().contains(storageType)) {
            return this.mSavingTaskManager.canPushStoreTask(storageType);
        }
        return false;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public List<Storage.StorageType> getAvailableStorage() {
        ArrayList arrayList = new ArrayList(Storage.StorageType.values().length);
        for (Storage.StorageType storageType : Storage.StorageType.values()) {
            if (checkStorageState(storageType, Storage.StorageState.AVAILABLE_NEAR_FULL, Storage.StorageState.AVAILABLE)) {
                arrayList.add(storageType);
            }
        }
        return arrayList;
    }

    private boolean checkStorageState(Storage.StorageType storageType, Storage.StorageState... storageStateArr) {
        Storage.StorageState currentState = getCurrentState(storageType);
        for (Storage.StorageState storageState : storageStateArr) {
            if (currentState == storageState) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public boolean isStorageReadable() {
        for (Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
            if (!isStorageReadable(storageType)) {
                if (CamLog.DEBUG) {
                    CamLog.d("type = " + storageType + ", readyState = " + this.mStorageController.getStorageReadyState(storageType));
                }
                return false;
            }
        }
        return true;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public boolean isStorageReadable(Storage.StorageType storageType) {
        return this.mStorageController.getStorageReadyState(storageType).compareTo(Storage.StorageReadyState.ACCESSIBLE) >= 0;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public boolean isStorageActivated() {
        Iterator<Storage.StorageType> it = StorageUtil.getMountableStorageTypes().iterator();
        while (it.hasNext()) {
            if (this.mStorageController.getStorageReadyState(it.next()) != Storage.StorageReadyState.COMPLETED) {
                return false;
            }
        }
        return true;
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void addStorageReadyStateListener(Storage.StorageReadyStateListener storageReadyStateListener) {
        if (storageReadyStateListener != null) {
            this.mStorageController.addStorageReadyStateListener(storageReadyStateListener);
        }
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public void removeStorageReadyStateListener(Storage.StorageReadyStateListener storageReadyStateListener) {
        if (storageReadyStateListener != null) {
            this.mStorageController.removeStorageReadyStateListener(storageReadyStateListener);
        }
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public Storage.StorageWriteNotifier createNotifier(Storage.StorageType storageType, int i) {
        return new StorageWriteNotifierImpl(this, storageType, i, this.mStorageController);
    }

    public void onWriteStorage(Storage.StorageType storageType) {
        if (CamLog.VERBOSE) {
            CamLog.d("onWriteStorage : " + storageType);
        }
        this.mCameraStorageManager.requestVolumeCheck(storageType, CameraStorageManager.UpdateInterval.IMMEDIATE, CameraStorageManager.UpdateRequestReason.PERIODIC_UPDATE);
    }

    @Override // com.sonyericsson.android.camera.CameraApplication.Pausable
    public void resume() {
        if (CamLog.DEBUG) {
            CamLog.d("resume()");
        }
        this.mCameraStorageManager.doResume();
    }

    @Override // com.sonyericsson.android.camera.CameraApplication.Pausable
    public void pause() {
        if (CamLog.DEBUG) {
            CamLog.d("pause()");
        }
        this.mCameraStorageManager.doPause();
    }

    @Override // com.sonyericsson.cameracommon.storage.Storage
    public Uri getSdGrantedUri() {
        if (CamLog.DEBUG) {
            CamLog.d("getSdGrantedUri()");
        }
        return this.mCameraStorageManager.getSdGrantedUri(this.mContext);
    }
}
