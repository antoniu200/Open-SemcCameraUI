// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import java.util.Iterator;
import android.support.annotation.NonNull;
import java.util.Collections;
import java.util.Arrays;
import android.graphics.Bitmap;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import android.net.Uri;

public interface Storage
{
    void addStorageReadyStateListener(final StorageReadyStateListener p0);
    
    void addStorageStateListener(final StorageStateListener p0);
    
    boolean canPushStoreRequest(final StorageType p0);
    
    void cancelDataLoad(final long p0);
    
    void cancelDataLoad(final boolean p0);
    
    boolean checkValidUri(final Uri p0);
    
    StorageWriteNotifier createNotifier(final StorageType p0, final int p1);
    
    List<StorageType> getAvailableStorage();
    
    StorageState getCurrentState(final StorageType p0);
    
    long getRemainStorage(final StorageType p0);
    
    Uri getSdGrantedUri();
    
    boolean isStorageActivated();
    
    boolean isStorageReadable();
    
    boolean isStorageReadable(final StorageType p0);
    
    void removeStorageReadyStateListener(final StorageReadyStateListener p0);
    
    void removeStorageStateListener(final StorageStateListener p0);
    
    void requestCreateContentInfoSync(final ArrayList<Uri> p0, final OnLoadCompletedListener p1);
    
    void requestDataLoad(final int p0, final Uri p1, final boolean p2, final OnLoadCompletedListener p3);
    
    void requestDataLoad(final int p0, final boolean p1, final OnLoadCompletedListener p2);
    
    void requestLastDataLoad(final int p0, final boolean p1, final OnLoadCompletedListener p2);
    
    void requestLoad(final Uri p0, final int p1, final OnLoadCompletedListener p2);
    
    void requestLoad(final byte[] p0, final int p1, final OnLoadCompletedListener p2);
    
    boolean requestStore(final SavingRequest p0, final StorageType p1, final OnStoreCompletedListener p2);
    
    public interface OnLoadCompletedListener
    {
        void onDataLoadCompleted(final int p0, final boolean p1, final LinkedList<Content.ContentInfo> p2, final Bitmap p3);
        
        void onDataLoadFailed(final int p0);
        
        void onLoadCompleted(final Uri p0, final Bitmap p1);
        
        void onLoadFailed(final Uri p0, final int p1);
    }
    
    public interface OnStoreCompletedListener
    {
        void onStoreCompleted(final Uri p0, final SavingRequest p1, final StorageType p2);
        
        void onStoreFailed(final Uri p0, final SavingRequest p1, final int p2);
    }
    
    public enum StorageReadyState
    {
        private static final StorageReadyState[] $VALUES;
        
        ACCESSIBLE, 
        COMPLETED, 
        INIT, 
        PREPARING, 
        SUSPENDED;
        
        static {
            $VALUES = new StorageReadyState[] { StorageReadyState.INIT, StorageReadyState.PREPARING, StorageReadyState.ACCESSIBLE, StorageReadyState.SUSPENDED, StorageReadyState.COMPLETED };
        }
    }
    
    public interface StorageReadyStateListener
    {
        void onStorageReadyStateChanged(final StorageType p0, final StorageReadyState p1);
    }
    
    public enum StorageState
    {
        private static final StorageState[] $VALUES;
        
        AVAILABLE(new CameraStorageManager.DetailStorageState[] { CameraStorageManager.DetailStorageState.MEMORY_READY }), 
        AVAILABLE_NEAR_FULL(new CameraStorageManager.DetailStorageState[] { CameraStorageManager.DetailStorageState.MEMORY_READY_LOW }), 
        CORRUPT(new CameraStorageManager.DetailStorageState[] { CameraStorageManager.DetailStorageState.MEMORY_ERR_TIMED_OUT }), 
        FULL(new CameraStorageManager.DetailStorageState[] { CameraStorageManager.DetailStorageState.MEMORY_ERR_FULL, CameraStorageManager.DetailStorageState.MEMORY_ERR_FULL_COUNT }), 
        READ_ONLY(new CameraStorageManager.DetailStorageState[] { CameraStorageManager.DetailStorageState.MEMORY_ERR_READ_ONLY }), 
        REMOVED(new CameraStorageManager.DetailStorageState[] { CameraStorageManager.DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD }), 
        UNAVAILABLE(new CameraStorageManager.DetailStorageState[] { CameraStorageManager.DetailStorageState.MEMORY_ERR_ACCESS, CameraStorageManager.DetailStorageState.MEMORY_ERR_FORMAT, CameraStorageManager.DetailStorageState.MEMORY_ERR_SHARED, CameraStorageManager.DetailStorageState.MEMORY_NO_DCIM }), 
        UNGRANTED(new CameraStorageManager.DetailStorageState[] { CameraStorageManager.DetailStorageState.MEMORY_UNGRANTED });
        
        private final List<CameraStorageManager.DetailStorageState> mDetailStateList;
        
        static {
            $VALUES = new StorageState[] { StorageState.REMOVED, StorageState.AVAILABLE, StorageState.AVAILABLE_NEAR_FULL, StorageState.UNAVAILABLE, StorageState.READ_ONLY, StorageState.FULL, StorageState.CORRUPT, StorageState.UNGRANTED };
        }
        
        private StorageState(final CameraStorageManager.DetailStorageState[] a) {
            this.mDetailStateList = Collections.unmodifiableList((List<? extends CameraStorageManager.DetailStorageState>)Arrays.asList((T[])a));
        }
        
        @NonNull
        public static StorageState getState(@NonNull final CameraStorageManager.DetailStorageState detailStorageState) {
            for (final StorageState storageState : values()) {
                final Iterator<CameraStorageManager.DetailStorageState> iterator = storageState.mDetailStateList.iterator();
                while (iterator.hasNext()) {
                    if (detailStorageState.equals(iterator.next())) {
                        return storageState;
                    }
                }
            }
            return StorageState.UNAVAILABLE;
        }
        
        public boolean isWritable() {
            return this == StorageState.AVAILABLE || this == StorageState.AVAILABLE_NEAR_FULL;
        }
    }
    
    public interface StorageStateListener
    {
        void onStorageSizeChanged(final StorageType p0, final long p1);
        
        void onStorageStateChanged(final StorageType p0, final StorageState p1, final StorageReadyState p2);
    }
    
    public enum StorageType
    {
        private static final StorageType[] $VALUES;
        
        EXTERNAL_CARD, 
        INTERNAL, 
        UNKNOWN, 
        USB;
        
        static {
            $VALUES = new StorageType[] { StorageType.INTERNAL, StorageType.EXTERNAL_CARD, StorageType.USB, StorageType.UNKNOWN };
        }
    }
    
    public interface StorageWriteNotifier
    {
        StorageType getStorageType();
        
        void notifyWriteStorage();
    }
}
