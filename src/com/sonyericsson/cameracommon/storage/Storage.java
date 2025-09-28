package com.sonyericsson.cameracommon.storage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface Storage {

    public interface OnLoadCompletedListener {
        void onDataLoadCompleted(int i, boolean z, LinkedList<Content.ContentInfo> linkedList, Bitmap bitmap);

        void onDataLoadFailed(int i);

        void onLoadCompleted(Uri uri, Bitmap bitmap);

        void onLoadFailed(Uri uri, int i);
    }

    public interface OnStoreCompletedListener {
        void onStoreCompleted(Uri uri, SavingRequest savingRequest, StorageType storageType);

        void onStoreFailed(Uri uri, SavingRequest savingRequest, int i);
    }

    public enum StorageReadyState {
        INIT,
        PREPARING,
        ACCESSIBLE,
        SUSPENDED,
        COMPLETED
    }

    public interface StorageReadyStateListener {
        void onStorageReadyStateChanged(StorageType storageType, StorageReadyState storageReadyState);
    }

    public interface StorageStateListener {
        void onStorageSizeChanged(StorageType storageType, long j);

        void onStorageStateChanged(StorageType storageType, StorageState storageState, StorageReadyState storageReadyState);
    }

    public enum StorageType {
        INTERNAL,
        EXTERNAL_CARD,
        USB,
        UNKNOWN
    }

    public interface StorageWriteNotifier {
        StorageType getStorageType();

        void notifyWriteStorage();
    }

    void addStorageReadyStateListener(StorageReadyStateListener storageReadyStateListener);

    void addStorageStateListener(StorageStateListener storageStateListener);

    boolean canPushStoreRequest(StorageType storageType);

    void cancelDataLoad(long j);

    void cancelDataLoad(boolean z);

    boolean checkValidUri(Uri uri);

    StorageWriteNotifier createNotifier(StorageType storageType, int i);

    List<StorageType> getAvailableStorage();

    StorageState getCurrentState(StorageType storageType);

    long getRemainStorage(StorageType storageType);

    Uri getSdGrantedUri();

    boolean isStorageActivated();

    boolean isStorageReadable();

    boolean isStorageReadable(StorageType storageType);

    void removeStorageReadyStateListener(StorageReadyStateListener storageReadyStateListener);

    void removeStorageStateListener(StorageStateListener storageStateListener);

    void requestCreateContentInfoSync(ArrayList<Uri> arrayList, OnLoadCompletedListener onLoadCompletedListener);

    void requestDataLoad(int i, Uri uri, boolean z, OnLoadCompletedListener onLoadCompletedListener);

    void requestDataLoad(int i, boolean z, OnLoadCompletedListener onLoadCompletedListener);

    void requestLastDataLoad(int i, boolean z, OnLoadCompletedListener onLoadCompletedListener);

    void requestLoad(Uri uri, int i, OnLoadCompletedListener onLoadCompletedListener);

    void requestLoad(byte[] bArr, int i, OnLoadCompletedListener onLoadCompletedListener);

    boolean requestStore(SavingRequest savingRequest, StorageType storageType, OnStoreCompletedListener onStoreCompletedListener);

    public enum StorageState {
        REMOVED(CameraStorageManager.DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD),
        AVAILABLE(CameraStorageManager.DetailStorageState.MEMORY_READY),
        AVAILABLE_NEAR_FULL(CameraStorageManager.DetailStorageState.MEMORY_READY_LOW),
        UNAVAILABLE(CameraStorageManager.DetailStorageState.MEMORY_ERR_ACCESS, CameraStorageManager.DetailStorageState.MEMORY_ERR_FORMAT, CameraStorageManager.DetailStorageState.MEMORY_ERR_SHARED, CameraStorageManager.DetailStorageState.MEMORY_NO_DCIM),
        READ_ONLY(CameraStorageManager.DetailStorageState.MEMORY_ERR_READ_ONLY),
        FULL(CameraStorageManager.DetailStorageState.MEMORY_ERR_FULL, CameraStorageManager.DetailStorageState.MEMORY_ERR_FULL_COUNT),
        CORRUPT(CameraStorageManager.DetailStorageState.MEMORY_ERR_TIMED_OUT),
        UNGRANTED(CameraStorageManager.DetailStorageState.MEMORY_UNGRANTED);

        private final List<CameraStorageManager.DetailStorageState> mDetailStateList;

        StorageState(CameraStorageManager.DetailStorageState... detailStorageStateArr) {
            this.mDetailStateList = Collections.unmodifiableList(Arrays.asList(detailStorageStateArr));
        }

        @NonNull
        public static StorageState getState(@NonNull CameraStorageManager.DetailStorageState detailStorageState) {
            for (StorageState storageState : values()) {
                Iterator<CameraStorageManager.DetailStorageState> it = storageState.mDetailStateList.iterator();
                while (it.hasNext()) {
                    if (detailStorageState.equals(it.next())) {
                        return storageState;
                    }
                }
            }
            return UNAVAILABLE;
        }

        public boolean isWritable() {
            return this == AVAILABLE || this == AVAILABLE_NEAR_FULL;
        }
    }
}
