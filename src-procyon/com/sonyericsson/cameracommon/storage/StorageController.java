// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class StorageController
{
    public static final String TAG = "StorageController";
    protected Map<Storage.StorageType, Long> mAvailableSizeList;
    protected Map<Storage.StorageType, Storage.StorageReadyState> mLatestCheckedStorageReadyState;
    protected Map<Storage.StorageType, Storage.StorageState> mLatestCheckedStorageState;
    private final List<Storage.StorageReadyStateListener> mReadyStateListeners;
    private final List<Storage.StorageStateListener> mStateListeners;
    protected Map<Storage.StorageType, Storage.StorageReadyState> mStorageReadyStateMap;
    protected Map<Storage.StorageType, Storage.StorageState> mStorageStatus;
    
    public StorageController() {
        this.mStorageStatus = null;
        this.mStorageReadyStateMap = null;
        this.mLatestCheckedStorageState = null;
        this.mLatestCheckedStorageReadyState = null;
        this.mAvailableSizeList = new ConcurrentHashMap<Storage.StorageType, Long>();
        this.mStateListeners = Collections.synchronizedList(new LinkedList<Storage.StorageStateListener>());
        this.mReadyStateListeners = Collections.synchronizedList(new LinkedList<Storage.StorageReadyStateListener>());
        this.mLatestCheckedStorageReadyState = new ConcurrentHashMap<Storage.StorageType, Storage.StorageReadyState>();
        this.mLatestCheckedStorageState = new ConcurrentHashMap<Storage.StorageType, Storage.StorageState>();
        this.mStorageReadyStateMap = new ConcurrentHashMap<Storage.StorageType, Storage.StorageReadyState>();
        final Iterator<Storage.StorageType> iterator = StorageUtil.getMountableStorageTypes().iterator();
        while (iterator.hasNext()) {
            this.setStorageReadyState(iterator.next(), Storage.StorageReadyState.INIT);
        }
        this.mStorageStatus = new ConcurrentHashMap<Storage.StorageType, Storage.StorageState>();
    }
    
    private void notifyAvailableSize(final Storage.StorageType storageType, final long n) {
        for (int i = 0; i < this.mStateListeners.size(); ++i) {
            final Storage.StorageStateListener storageStateListener = this.mStateListeners.get(i);
            if (storageStateListener != null) {
                storageStateListener.onStorageSizeChanged(storageType, n);
            }
        }
    }
    
    private void notifyReadyStateChanged(final Storage.StorageType obj, final Storage.StorageReadyState obj2) {
        final boolean debug = CamLog.DEBUG;
        int i;
        final int n = i = 0;
        if (debug) {
            final StringBuilder sb = new StringBuilder();
            sb.append("notifyReadyStateChanged storageType = ");
            sb.append(obj);
            sb.append(", State = ");
            sb.append(obj2);
            CamLog.d(sb.toString());
            i = n;
        }
        while (i < this.mReadyStateListeners.size()) {
            final Storage.StorageReadyStateListener storageReadyStateListener = this.mReadyStateListeners.get(i);
            if (storageReadyStateListener != null) {
                storageReadyStateListener.onStorageReadyStateChanged(obj, obj2);
            }
            ++i;
        }
    }
    
    private void notifyStateChanged(final Storage.StorageType obj) {
        final Storage.StorageState storageState = this.getStorageState(obj);
        final Storage.StorageReadyState storageReadyState = this.getStorageReadyState(obj);
        final boolean debug = CamLog.DEBUG;
        int i = 0;
        if (debug) {
            final StringBuilder sb = new StringBuilder();
            sb.append("notifyStateChanged: storageType = ");
            sb.append(obj);
            sb.append(", State = ");
            sb.append(storageState);
            sb.append(", readyState = ");
            sb.append(storageReadyState);
            CamLog.d(sb.toString());
        }
        if (storageReadyState.compareTo(Storage.StorageReadyState.ACCESSIBLE) < 0) {
            if (CamLog.DEBUG) {
                CamLog.d("Storage is not checked yet");
            }
            return;
        }
        while (i < this.mStateListeners.size()) {
            final Storage.StorageStateListener storageStateListener = this.mStateListeners.get(i);
            if (storageStateListener != null) {
                storageStateListener.onStorageStateChanged(obj, storageState, storageReadyState);
            }
            ++i;
        }
    }
    
    public void addStorageListener(final Storage.StorageStateListener storageStateListener) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("addStorageListener: ");
            sb.append(storageStateListener.getClass().getSimpleName());
            CamLog.d(sb.toString());
        }
        if (!this.mStateListeners.contains(storageStateListener)) {
            this.mStateListeners.add(storageStateListener);
            for (final Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
                storageStateListener.onStorageStateChanged(storageType, this.getStorageState(storageType), this.mStorageReadyStateMap.get(storageType));
            }
        }
    }
    
    public void addStorageReadyStateListener(final Storage.StorageReadyStateListener storageReadyStateListener) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("addStorageReadyStateListener: ");
            sb.append(storageReadyStateListener.getClass().getSimpleName());
            CamLog.d(sb.toString());
        }
        if (!this.mReadyStateListeners.contains(storageReadyStateListener)) {
            this.mReadyStateListeners.add(storageReadyStateListener);
            for (final Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
                storageReadyStateListener.onStorageReadyStateChanged(storageType, this.mStorageReadyStateMap.get(storageType));
            }
        }
    }
    
    public void checkAndNotifyReadyStateChanged(final Storage.StorageType storageType) {
        final Storage.StorageReadyState storageReadyState = this.mStorageReadyStateMap.get(storageType);
        if (this.mLatestCheckedStorageReadyState.get(storageType) != storageReadyState) {
            this.mLatestCheckedStorageReadyState.put(storageType, storageReadyState);
            this.notifyReadyStateChanged(storageType, storageReadyState);
        }
    }
    
    public void checkAndNotifyStateChanged(final Storage.StorageType obj, final boolean b) {
        final Storage.StorageState obj2 = this.mStorageStatus.get(obj);
        if (obj2 != null && (this.mLatestCheckedStorageState.get(obj) != obj2 || b)) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("checked: ");
                sb.append(obj);
                sb.append(", before: ");
                sb.append(this.mLatestCheckedStorageState.get(obj));
                sb.append(", after: ");
                sb.append(obj2);
                sb.append(", forceUpdate: ");
                sb.append(b);
                CamLog.d(sb.toString());
            }
            this.mLatestCheckedStorageState.put(obj, obj2);
            this.notifyStateChanged(obj);
        }
        this.notifyAvailableSize(obj, this.mAvailableSizeList.get(obj));
    }
    
    public long getAvailableStorageSize(final Storage.StorageType storageType) {
        long longValue;
        if (this.mAvailableSizeList.containsKey(storageType)) {
            longValue = this.mAvailableSizeList.get(storageType);
        }
        else {
            longValue = 0L;
        }
        return longValue;
    }
    
    @NonNull
    Storage.StorageReadyState getStorageReadyState(final Storage.StorageType storageType) {
        return this.mStorageReadyStateMap.get(storageType);
    }
    
    public Storage.StorageState getStorageState(final Storage.StorageType storageType) {
        return this.mStorageStatus.get(storageType);
    }
    
    public void release() {
        this.mStateListeners.clear();
        if (CamLog.DEBUG) {
            CamLog.d("Notify to listener to be uninitialized Storage!!!");
        }
        this.mReadyStateListeners.clear();
    }
    
    public void removeStorageListener(final Storage.StorageStateListener storageStateListener) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("removeStorageListener: ");
            sb.append(storageStateListener.getClass().getSimpleName());
            CamLog.d(sb.toString());
        }
        if (this.mStateListeners.contains(storageStateListener)) {
            this.mStateListeners.remove(storageStateListener);
        }
    }
    
    public void removeStorageReadyStateListener(final Storage.StorageReadyStateListener storageReadyStateListener) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("removeStorageReadyStateListener: ");
            sb.append(storageReadyStateListener.getClass().getSimpleName());
            CamLog.d(sb.toString());
        }
        if (this.mReadyStateListeners.contains(storageReadyStateListener)) {
            this.mReadyStateListeners.remove(storageReadyStateListener);
        }
    }
    
    public void setAvailableStorageSize(final Storage.StorageType obj, final long n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setAvailableStorageSize: size = ");
            sb.append(n);
            sb.append(" type : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mAvailableSizeList.put(obj, n);
    }
    
    public void setStorageReadyState(final Storage.StorageType obj, final Storage.StorageReadyState obj2) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setStorageReadyState[");
            sb.append(obj);
            sb.append("] From ");
            sb.append(this.getStorageReadyState(obj));
            sb.append(" to ");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        this.mStorageReadyStateMap.put(obj, obj2);
    }
    
    public void setStorageState(final Storage.StorageType obj, final CameraStorageManager.DetailStorageState obj2) {
        if (!StorageUtil.getMountableStorageTypes().contains(obj)) {
            return;
        }
        final Storage.StorageState state = Storage.StorageState.getState(obj2);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("update storage: ");
            sb.append(obj);
            sb.append(", ");
            sb.append(state);
            sb.append(", detail:");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        this.mStorageStatus.put(obj, state);
    }
}
