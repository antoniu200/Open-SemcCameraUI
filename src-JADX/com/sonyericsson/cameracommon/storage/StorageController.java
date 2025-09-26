package com.sonyericsson.cameracommon.storage;

import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class StorageController {
    public static final String TAG = "StorageController";
    protected Map<Storage.StorageType, Storage.StorageReadyState> mLatestCheckedStorageReadyState;
    protected Map<Storage.StorageType, Storage.StorageState> mLatestCheckedStorageState;
    protected Map<Storage.StorageType, Storage.StorageReadyState> mStorageReadyStateMap;
    protected Map<Storage.StorageType, Storage.StorageState> mStorageStatus;
    protected Map<Storage.StorageType, Long> mAvailableSizeList = new ConcurrentHashMap();
    private final List<Storage.StorageStateListener> mStateListeners = Collections.synchronizedList(new LinkedList());
    private final List<Storage.StorageReadyStateListener> mReadyStateListeners = Collections.synchronizedList(new LinkedList());

    public StorageController() {
        this.mStorageStatus = null;
        this.mStorageReadyStateMap = null;
        this.mLatestCheckedStorageState = null;
        this.mLatestCheckedStorageReadyState = null;
        this.mLatestCheckedStorageReadyState = new ConcurrentHashMap();
        this.mLatestCheckedStorageState = new ConcurrentHashMap();
        this.mStorageReadyStateMap = new ConcurrentHashMap();
        Iterator<Storage.StorageType> it = StorageUtil.getMountableStorageTypes().iterator();
        while (it.hasNext()) {
            setStorageReadyState(it.next(), Storage.StorageReadyState.INIT);
        }
        this.mStorageStatus = new ConcurrentHashMap();
    }

    public void addStorageListener(Storage.StorageStateListener storageStateListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("addStorageListener: " + storageStateListener.getClass().getSimpleName());
        }
        if (this.mStateListeners.contains(storageStateListener)) {
            return;
        }
        this.mStateListeners.add(storageStateListener);
        for (Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
            storageStateListener.onStorageStateChanged(storageType, getStorageState(storageType), this.mStorageReadyStateMap.get(storageType));
        }
    }

    public void removeStorageListener(Storage.StorageStateListener storageStateListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("removeStorageListener: " + storageStateListener.getClass().getSimpleName());
        }
        if (this.mStateListeners.contains(storageStateListener)) {
            this.mStateListeners.remove(storageStateListener);
        }
    }

    public void addStorageReadyStateListener(Storage.StorageReadyStateListener storageReadyStateListener) {
        if (CamLog.DEBUG) {
            CamLog.d("addStorageReadyStateListener: " + storageReadyStateListener.getClass().getSimpleName());
        }
        if (this.mReadyStateListeners.contains(storageReadyStateListener)) {
            return;
        }
        this.mReadyStateListeners.add(storageReadyStateListener);
        for (Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
            storageReadyStateListener.onStorageReadyStateChanged(storageType, this.mStorageReadyStateMap.get(storageType));
        }
    }

    public void removeStorageReadyStateListener(Storage.StorageReadyStateListener storageReadyStateListener) {
        if (CamLog.DEBUG) {
            CamLog.d("removeStorageReadyStateListener: " + storageReadyStateListener.getClass().getSimpleName());
        }
        if (this.mReadyStateListeners.contains(storageReadyStateListener)) {
            this.mReadyStateListeners.remove(storageReadyStateListener);
        }
    }

    public void setStorageState(Storage.StorageType storageType, CameraStorageManager.DetailStorageState detailStorageState) {
        if (StorageUtil.getMountableStorageTypes().contains(storageType)) {
            Storage.StorageState state = Storage.StorageState.getState(detailStorageState);
            if (CamLog.VERBOSE) {
                CamLog.d("update storage: " + storageType + ", " + state + ", detail:" + detailStorageState);
            }
            this.mStorageStatus.put(storageType, state);
        }
    }

    public void setStorageReadyState(Storage.StorageType storageType, Storage.StorageReadyState storageReadyState) {
        if (CamLog.DEBUG) {
            CamLog.d("setStorageReadyState[" + storageType + "] From " + getStorageReadyState(storageType) + " to " + storageReadyState);
        }
        this.mStorageReadyStateMap.put(storageType, storageReadyState);
    }

    public void checkAndNotifyStateChanged(Storage.StorageType storageType, boolean z) {
        Storage.StorageState storageState = this.mStorageStatus.get(storageType);
        if (storageState != null && (this.mLatestCheckedStorageState.get(storageType) != storageState || z)) {
            if (CamLog.DEBUG) {
                CamLog.d("checked: " + storageType + ", before: " + this.mLatestCheckedStorageState.get(storageType) + ", after: " + storageState + ", forceUpdate: " + z);
            }
            this.mLatestCheckedStorageState.put(storageType, storageState);
            notifyStateChanged(storageType);
        }
        notifyAvailableSize(storageType, this.mAvailableSizeList.get(storageType).longValue());
    }

    public void checkAndNotifyReadyStateChanged(Storage.StorageType storageType) {
        Storage.StorageReadyState storageReadyState = this.mStorageReadyStateMap.get(storageType);
        if (this.mLatestCheckedStorageReadyState.get(storageType) != storageReadyState) {
            this.mLatestCheckedStorageReadyState.put(storageType, storageReadyState);
            notifyReadyStateChanged(storageType, storageReadyState);
        }
    }

    private void notifyStateChanged(Storage.StorageType storageType) {
        Storage.StorageState storageState = getStorageState(storageType);
        Storage.StorageReadyState storageReadyState = getStorageReadyState(storageType);
        if (CamLog.DEBUG) {
            CamLog.d("notifyStateChanged: storageType = " + storageType + ", State = " + storageState + ", readyState = " + storageReadyState);
        }
        if (storageReadyState.compareTo(Storage.StorageReadyState.ACCESSIBLE) < 0) {
            if (CamLog.DEBUG) {
                CamLog.d("Storage is not checked yet");
            }
        } else {
            for (int i = 0; i < this.mStateListeners.size(); i++) {
                Storage.StorageStateListener storageStateListener = this.mStateListeners.get(i);
                if (storageStateListener != null) {
                    storageStateListener.onStorageStateChanged(storageType, storageState, storageReadyState);
                }
            }
        }
    }

    private void notifyAvailableSize(Storage.StorageType storageType, long j) {
        for (int i = 0; i < this.mStateListeners.size(); i++) {
            Storage.StorageStateListener storageStateListener = this.mStateListeners.get(i);
            if (storageStateListener != null) {
                storageStateListener.onStorageSizeChanged(storageType, j);
            }
        }
    }

    private void notifyReadyStateChanged(Storage.StorageType storageType, Storage.StorageReadyState storageReadyState) {
        if (CamLog.DEBUG) {
            CamLog.d("notifyReadyStateChanged storageType = " + storageType + ", State = " + storageReadyState);
        }
        for (int i = 0; i < this.mReadyStateListeners.size(); i++) {
            Storage.StorageReadyStateListener storageReadyStateListener = this.mReadyStateListeners.get(i);
            if (storageReadyStateListener != null) {
                storageReadyStateListener.onStorageReadyStateChanged(storageType, storageReadyState);
            }
        }
    }

    public void release() {
        this.mStateListeners.clear();
        if (CamLog.DEBUG) {
            CamLog.d("Notify to listener to be uninitialized Storage!!!");
        }
        this.mReadyStateListeners.clear();
    }

    public Storage.StorageState getStorageState(Storage.StorageType storageType) {
        return this.mStorageStatus.get(storageType);
    }

    @NonNull
    Storage.StorageReadyState getStorageReadyState(Storage.StorageType storageType) {
        return this.mStorageReadyStateMap.get(storageType);
    }

    public void setAvailableStorageSize(Storage.StorageType storageType, long j) {
        if (CamLog.VERBOSE) {
            CamLog.d("setAvailableStorageSize: size = " + j + " type : " + storageType);
        }
        this.mAvailableSizeList.put(storageType, Long.valueOf(j));
    }

    public long getAvailableStorageSize(Storage.StorageType storageType) {
        if (this.mAvailableSizeList.containsKey(storageType)) {
            return this.mAvailableSizeList.get(storageType).longValue();
        }
        return 0L;
    }
}
