// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import com.sonyericsson.android.camera.util.ThreadUtil;
import java.util.ArrayList;
import java.util.List;
import android.net.Uri;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import java.util.concurrent.Callable;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ExecutorService;
import android.content.Context;
import com.sonyericsson.android.camera.CameraApplication;

public class StorageImpl implements Storage, Pausable
{
    private static final int DATA_LOAD_TASK_SIZE = 1;
    private static final int MULTI_STORAGE_ACCESS_PERMIT_NUM = 2;
    static final long NO_INTERVAL_REMAIN_THRESHOLD = 307200L;
    private static final int SINGLE_STORAGE_ACCESS_PERMIT_NUM = 1;
    private static final String TAG = "StrorageImpl";
    private static final String THREAD_NAME_DATE_LOADER_TASK = "DataLoaderTask";
    private CameraStorageManager mCameraStorageManager;
    private Context mContext;
    private ExecutorService mDataLoadExecutor;
    private LinkedBlockingDeque<Future<Long>> mDataLoaderTaskQueue;
    private final Object mRequestLock;
    private SavingTaskManager mSavingTaskManager;
    private Map<StorageType, Semaphore> mStorageAccessSemaphoreMap;
    private StorageBroadcastReceiver mStorageBroadcastReceiver;
    private StorageController mStorageController;
    
    public StorageImpl() {
        this.mCameraStorageManager = null;
        this.mStorageBroadcastReceiver = new StorageBroadcastReceiver();
        this.mStorageAccessSemaphoreMap = new HashMap<StorageType, Semaphore>();
        this.mRequestLock = new Object();
    }
    
    private boolean checkStorageState(final StorageType storageType, final StorageState... array) {
        final StorageState currentState = this.getCurrentState(storageType);
        for (int length = array.length, i = 0; i < length; ++i) {
            if (currentState == array[i]) {
                return true;
            }
        }
        return false;
    }
    
    private void initialize() {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl initialize");
        }
        this.mCameraStorageManager.initialize(this.mSavingTaskManager.getInquiry(), this.mStorageAccessSemaphoreMap);
    }
    
    private void loadData(final DataLoader dataLoader) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl loadData");
        }
        if (this.mDataLoadExecutor != null) {
            if (this.mDataLoaderTaskQueue != null) {
                final Future<Long> submit = this.mDataLoadExecutor.submit((Callable<Long>)dataLoader);
                synchronized (this.mRequestLock) {
                    if (!this.mDataLoaderTaskQueue.offerLast(submit)) {
                        final Future future = this.mDataLoaderTaskQueue.pollFirst();
                        if (future != null) {
                            future.cancel(true);
                        }
                        this.mDataLoaderTaskQueue.addLast(submit);
                    }
                    return;
                }
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("mDataLoadExecutor or mDataLoadQueue is null");
        }
    }
    
    private void prepareReceiver() {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl prepareReceiver");
        }
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addDataScheme("file");
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_EJECT");
        intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
        this.mContext.registerReceiver((BroadcastReceiver)this.mStorageBroadcastReceiver, intentFilter);
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
                monitorexit(this.mRequestLock);
                this.mDataLoaderTaskQueue = null;
            }
        }
        this.releaseReceiver();
        this.mStorageController.release();
        this.mStorageController = null;
        this.mCameraStorageManager.release();
        this.mCameraStorageManager = null;
        this.mSavingTaskManager.release();
        this.mSavingTaskManager = null;
    }
    
    private void releaseReceiver() {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl releaseReceiver");
        }
        this.mContext.unregisterReceiver((BroadcastReceiver)this.mStorageBroadcastReceiver);
    }
    
    private void removeFuture(final long n) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl removeFuture");
        }
        if (this.mDataLoaderTaskQueue == null) {
            return;
        }
        synchronized (this.mRequestLock) {
            final Iterator<Future<Long>> iterator = this.mDataLoaderTaskQueue.iterator();
            while (iterator.hasNext()) {
                final Future future = iterator.next();
                if (future.isDone() && !future.isCancelled()) {
                    final long n2 = 0L;
                    long longValue;
                    try {
                        longValue = (long)future.get();
                    }
                    catch (final ExecutionException ex) {
                        longValue = n2;
                        if (CamLog.VERBOSE) {
                            CamLog.d("StrorageImpl", "ExecutionException at future.get().");
                            longValue = n2;
                        }
                    }
                    catch (final InterruptedException ex2) {
                        longValue = n2;
                        if (CamLog.VERBOSE) {
                            CamLog.d("StrorageImpl", "InterruptedException at future.get().");
                            longValue = n2;
                        }
                    }
                    if (longValue != n) {
                        continue;
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("remove queue. id = ");
                        sb.append(longValue);
                        CamLog.d("StrorageImpl", sb.toString());
                    }
                    iterator.remove();
                }
            }
        }
    }
    
    @Override
    public void addStorageReadyStateListener(final StorageReadyStateListener storageReadyStateListener) {
        if (storageReadyStateListener != null) {
            this.mStorageController.addStorageReadyStateListener(storageReadyStateListener);
        }
    }
    
    @Override
    public void addStorageStateListener(final StorageStateListener storageStateListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl addStorageStateListener");
        }
        this.mStorageController.addStorageListener(storageStateListener);
    }
    
    @Override
    public boolean canPushStoreRequest(final StorageType storageType) {
        return this.isStorageActivated() && this.getAvailableStorage().contains(storageType) && this.mSavingTaskManager.canPushStoreTask(storageType);
    }
    
    @Override
    public void cancelDataLoad(final long n) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl cancelDataLoad");
        }
        this.removeFuture(n);
    }
    
    @Override
    public void cancelDataLoad(final boolean b) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl cancelDataLoad");
        }
        if (this.mDataLoaderTaskQueue != null) {
            synchronized (this.mRequestLock) {
                final Iterator<Future<Long>> iterator = this.mDataLoaderTaskQueue.iterator();
                while (iterator.hasNext()) {
                    iterator.next().cancel(b);
                }
                this.mDataLoaderTaskQueue.clear();
            }
        }
    }
    
    @Override
    public boolean checkValidUri(final Uri uri) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl isIllegalUri");
        }
        return StorageUtil.getPathFromUri(this.mContext, uri) != null;
    }
    
    public void close() {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl close");
        }
        this.release();
    }
    
    @Override
    public StorageWriteNotifier createNotifier(final StorageType storageType, final int n) {
        return new StorageWriteNotifierImpl(this, storageType, n, this.mStorageController);
    }
    
    @Override
    public List<StorageType> getAvailableStorage() {
        final ArrayList list = new ArrayList(StorageType.values().length);
        for (final StorageType storageType : StorageType.values()) {
            if (this.checkStorageState(storageType, StorageState.AVAILABLE_NEAR_FULL, StorageState.AVAILABLE)) {
                list.add(storageType);
            }
        }
        return list;
    }
    
    CameraStorageManager getCameraStorageManager() {
        return this.mCameraStorageManager;
    }
    
    @Override
    public StorageState getCurrentState(final StorageType storageType) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl getCurrentState");
        }
        return this.mStorageController.getStorageState(storageType);
    }
    
    @Override
    public long getRemainStorage(final StorageType storageType) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl getRemainStorage");
        }
        this.mCameraStorageManager.checkRemain(false, storageType);
        return this.mStorageController.getAvailableStorageSize(storageType);
    }
    
    @Override
    public Uri getSdGrantedUri() {
        if (CamLog.DEBUG) {
            CamLog.d("getSdGrantedUri()");
        }
        return this.mCameraStorageManager.getSdGrantedUri(this.mContext);
    }
    
    @Override
    public boolean isStorageActivated() {
        final Iterator<StorageType> iterator = StorageUtil.getMountableStorageTypes().iterator();
        while (iterator.hasNext()) {
            if (this.mStorageController.getStorageReadyState(iterator.next()) != StorageReadyState.COMPLETED) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isStorageReadable() {
        for (final StorageType obj : StorageUtil.getMountableStorageTypes()) {
            if (!this.isStorageReadable(obj)) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("type = ");
                    sb.append(obj);
                    sb.append(", readyState = ");
                    sb.append(this.mStorageController.getStorageReadyState(obj));
                    CamLog.d(sb.toString());
                }
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isStorageReadable(final StorageType storageType) {
        return this.mStorageController.getStorageReadyState(storageType).compareTo(StorageReadyState.ACCESSIBLE) >= 0;
    }
    
    public void onWriteStorage(final StorageType obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onWriteStorage : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mCameraStorageManager.requestVolumeCheck(obj, CameraStorageManager.UpdateInterval.IMMEDIATE, CameraStorageManager.UpdateRequestReason.PERIODIC_UPDATE);
    }
    
    public void open(final Context mContext) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl open");
        }
        this.mContext = mContext;
        for (final StorageType storageType : StorageUtil.getMountableStorageTypes()) {
            if (storageType == StorageType.INTERNAL) {
                this.mStorageAccessSemaphoreMap.put(storageType, new Semaphore(2, true));
            }
            else {
                this.mStorageAccessSemaphoreMap.put(storageType, new Semaphore(1, true));
            }
        }
        this.mStorageController = new StorageController();
        this.mCameraStorageManager = new CameraStorageManager(mContext, this.mStorageController);
        this.mSavingTaskManager = new SavingTaskManager(mContext, this.mCameraStorageManager, this.mStorageAccessSemaphoreMap);
        this.prepareReceiver();
        this.mDataLoaderTaskQueue = new LinkedBlockingDeque<Future<Long>>(1);
        this.mDataLoadExecutor = ThreadUtil.buildExecutor("DataLoaderTask");
        new StorageInitializeThread().start();
    }
    
    @Override
    public void pause() {
        if (CamLog.DEBUG) {
            CamLog.d("pause()");
        }
        this.mCameraStorageManager.doPause();
    }
    
    @Override
    public void removeStorageReadyStateListener(final StorageReadyStateListener storageReadyStateListener) {
        if (storageReadyStateListener != null) {
            this.mStorageController.removeStorageReadyStateListener(storageReadyStateListener);
        }
    }
    
    @Override
    public void removeStorageStateListener(final StorageStateListener storageStateListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl removeStorageStateListener");
        }
        this.mStorageController.removeStorageListener(storageStateListener);
    }
    
    @Override
    public void requestCreateContentInfoSync(final ArrayList<Uri> list, final OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestCreateContentInfoSync");
        }
        final DataLoader dataLoader = new DataLoader(this.mContext, list, onLoadCompletedListener, true);
        try {
            dataLoader.call();
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void requestDataLoad(final int n, final Uri uri, final boolean b, final OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestDataLoad");
        }
        this.loadData(new DataLoader(n, uri, this.mContext, onLoadCompletedListener, b));
    }
    
    @Override
    public void requestDataLoad(final int n, final boolean b, final OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestDataLoad");
        }
        this.loadData(new DataLoader(this.mContext, this.mCameraStorageManager.getReadableStoragePaths(), n, onLoadCompletedListener, b));
    }
    
    @Override
    public void requestLastDataLoad(final int n, final boolean b, final OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestDataLoad");
        }
        this.loadData(new DataLoader(this.mContext, this.mCameraStorageManager.getReadableStoragePaths(), n, 0, onLoadCompletedListener, b));
    }
    
    @Override
    public void requestLoad(final Uri uri, final int n, final OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestLoad");
        }
        new Handler().post((Runnable)new Runnable(this, onLoadCompletedListener, new ImageLoader(this.mContext, uri, n).load(), uri) {
            final StorageImpl this$0;
            final Bitmap val$bitmap;
            final OnLoadCompletedListener val$listener;
            final Uri val$uri;
            
            @Override
            public void run() {
                if (this.val$listener != null) {
                    if (this.val$bitmap != null) {
                        this.val$listener.onLoadCompleted(this.val$uri, this.val$bitmap);
                    }
                    else {
                        this.val$listener.onLoadFailed(this.val$uri, 0);
                    }
                }
            }
        });
    }
    
    @Override
    public void requestLoad(final byte[] array, final int n, final OnLoadCompletedListener onLoadCompletedListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("StorageImpl requestLoad");
        }
        new Handler().post((Runnable)new Runnable(this, onLoadCompletedListener, new ImageLoader(this.mContext, array, n).load()) {
            final StorageImpl this$0;
            final Bitmap val$bitmap;
            final OnLoadCompletedListener val$listener;
            
            @Override
            public void run() {
                if (this.val$listener != null) {
                    this.val$listener.onLoadCompleted(Uri.EMPTY, this.val$bitmap);
                }
            }
        });
    }
    
    @Override
    public boolean requestStore(final SavingRequest savingRequest, final StorageType storageType, final OnStoreCompletedListener onStoreCompletedListener) {
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
            this.mSavingTaskManager.storeVideo((VideoSavingRequest)savingRequest);
        }
        else {
            if (CamLog.VERBOSE) {
                CamLog.d("StorageImpl Photo");
            }
            this.mSavingTaskManager.storePicture((PhotoSavingRequest)savingRequest);
        }
        return true;
    }
    
    @Override
    public void resume() {
        if (CamLog.DEBUG) {
            CamLog.d("resume()");
        }
        this.mCameraStorageManager.doResume();
    }
    
    private class StorageBroadcastReceiver extends BroadcastReceiver
    {
        final StorageImpl this$0;
        
        private StorageBroadcastReceiver(final StorageImpl this$0) {
            this.this$0 = this$0;
        }
        
        private void notifyStorageStatusChanged(final StorageType storageType, final String str, final String str2) {
            final Iterator<StorageType> iterator = StorageUtil.getMountableStorageTypes().iterator();
            while (true) {
                while (iterator.hasNext()) {
                    if (iterator.next() == storageType) {
                        final boolean b = true;
                        if (b) {
                            if (this.this$0.isStorageReadable()) {
                                if (str.equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
                                    this.this$0.mStorageController.checkAndNotifyStateChanged(storageType, true);
                                }
                                if (this.this$0.mCameraStorageManager != null) {
                                    this.this$0.mCameraStorageManager.updateStorageStateByAction(str, storageType);
                                }
                            }
                            else {
                                CamLog.i("onReceive: storage is not activated.");
                            }
                        }
                        else {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("StorageType is not mountable. action = ");
                            sb.append(str);
                            sb.append(" path=");
                            sb.append(str2);
                            CamLog.i(sb.toString());
                        }
                        return;
                    }
                }
                final boolean b = false;
                continue;
            }
        }
        
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            final String path = intent.getData().getPath();
            final StorageType storageTypeFromPath = StorageUtil.getStorageTypeFromPath(path, context);
            final StringBuilder sb = new StringBuilder();
            sb.append("Action = ");
            sb.append(action);
            sb.append(", Type = ");
            sb.append(storageTypeFromPath);
            sb.append(", Path = ");
            sb.append(path);
            CamLog.i(sb.toString());
            if (action.equals("android.intent.action.MEDIA_MOUNTED")) {
                this.notifyStorageStatusChanged(storageTypeFromPath, action, path);
            }
            else if (action.equals("android.intent.action.MEDIA_UNMOUNTED")) {
                this.notifyStorageStatusChanged(storageTypeFromPath, action, path);
            }
            else if (action.equals("android.intent.action.MEDIA_EJECT")) {
                this.notifyStorageStatusChanged(storageTypeFromPath, action, path);
            }
            else if (action.equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
                this.notifyStorageStatusChanged(storageTypeFromPath, action, path);
            }
        }
    }
    
    private class StorageInitializeThread extends Thread
    {
        private static final String THREAD_NAME_STORAGE_INITIALIZE = "SM#initTask";
        final StorageImpl this$0;
        
        public StorageInitializeThread(final StorageImpl this$0) {
            this.this$0 = this$0;
            this.setName("SM#initTask");
        }
        
        @Override
        public void run() {
            super.run();
            this.this$0.initialize();
        }
    }
}
