package com.sonyericsson.cameracommon.storage;

import android.content.ContentValues;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.storage.Storage;
import java.lang.ref.WeakReference;
import java.util.Iterator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class SavingRequest {
    public static final String TAG = "SavingRequest";
    public final TakenStatusCommon common;
    private Storage.StorageType mStorageType = Storage.StorageType.INTERNAL;
    private boolean mFinalRequest = true;
    private boolean mIsOneShot = false;
    public boolean mIsNecessaryMediaUpload = false;

    public abstract ContentValues createContentValues(String str);

    void setStorageType(Storage.StorageType storageType) {
        this.mStorageType = storageType;
    }

    public Storage.StorageType getStorageType() {
        return this.mStorageType;
    }

    public SavingRequest(TakenStatusCommon takenStatusCommon) {
        this.common = takenStatusCommon;
    }

    public SavingRequest(SavingRequest savingRequest) {
        this.common = new TakenStatusCommon(savingRequest.common);
    }

    public void setIsNecessaryMediaUpload(boolean z) {
        this.mIsNecessaryMediaUpload = z;
    }

    public boolean getIsNecessaryMediaUpload() {
        return this.mIsNecessaryMediaUpload;
    }

    public SavingRequest(SavingRequest savingRequest, int i) {
        this.common = new TakenStatusCommon(savingRequest.common.mDateTaken, i, savingRequest.common.location, savingRequest.common.width, savingRequest.common.height, savingRequest.common.mimeType, savingRequest.common.fileExtension, savingRequest.common.savedFileType, savingRequest.common.mFilePath, savingRequest.common.cropValue, savingRequest.common.addToMediaStore, savingRequest.common.takenByFastCapture);
        this.common.mCallbacks = savingRequest.common.mCallbacks;
    }

    public void addCallback(Storage.OnStoreCompletedListener onStoreCompletedListener) {
        Iterator<WeakReference<Storage.OnStoreCompletedListener>> it = this.common.mCallbacks.iterator();
        while (it.hasNext()) {
            if (it.next().get() == onStoreCompletedListener) {
                return;
            }
        }
        this.common.mCallbacks.add(new WeakReference<>(onStoreCompletedListener));
    }

    public int getRequestId() {
        return this.common.mRequestId;
    }

    public void setRequestId(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("setRequestId: " + i);
        }
        this.common.mRequestId = i;
    }

    public String getFilePath() {
        return this.common.mFilePath;
    }

    public void setFilePath(String str) {
        if (CamLog.VERBOSE) {
            CamLog.d("setFilePath: " + str);
        }
        this.common.mFilePath = str;
    }

    public long getDateTaken() {
        return this.common.mDateTaken;
    }

    public void setDateTaken(long j) {
        if (CamLog.VERBOSE) {
            CamLog.d("getDateTaken: " + j);
        }
        this.common.mDateTaken = j;
    }

    public Uri getExtraOutput() {
        return this.common.mExtraOutput;
    }

    public void setExtraOutput(Uri uri) {
        if (CamLog.VERBOSE) {
            CamLog.d("setExtraOutput: " + uri);
        }
        this.common.mExtraOutput = uri;
    }

    public int getSomcType() {
        return this.common.mSomcType;
    }

    public void setSomcType(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("setSomcType: " + i);
        }
        this.common.mSomcType = i;
    }

    public String getSaveTimeForPredictiveCapture() {
        return this.common.mSaveTimeForPredictiveCapture;
    }

    public void setSaveTimeForPredictiveCapture(String str) {
        this.common.mSaveTimeForPredictiveCapture = str;
    }

    public int getCaptureIdForPredictiveCapture() {
        return this.common.mCaptureIdForPredictiveCapture;
    }

    public void setCaptureIdForPredictiveCapture(int i) {
        this.common.mCaptureIdForPredictiveCapture = i;
    }

    public void setFinalInSavingGroup(boolean z) {
        this.mFinalRequest = z;
    }

    public boolean isFinalInSavingGroup() {
        return this.mFinalRequest;
    }

    public void setOneShot(boolean z) {
        this.mIsOneShot = z;
    }

    public boolean isOneShot() {
        return this.mIsOneShot;
    }

    public void notifyStoreFailed(MediaSavingResult mediaSavingResult) {
        Storage.OnStoreCompletedListener onStoreCompletedListener;
        StoreDataResult storeDataResult = new StoreDataResult(MediaSavingResult.FAIL, Uri.EMPTY, this);
        Iterator<WeakReference<Storage.OnStoreCompletedListener>> it = this.common.mCallbacks.iterator();
        while (it.hasNext() && (onStoreCompletedListener = it.next().get()) != null) {
            onStoreCompletedListener.onStoreFailed(storeDataResult.uri, storeDataResult.savingRequest, mediaSavingResult.mResultCode);
        }
    }

    public void notifyStoreResult(StoreDataResult storeDataResult) {
        if (CamLog.VERBOSE) {
            CamLog.d("notifyStoreResult E");
        }
        Iterator<WeakReference<Storage.OnStoreCompletedListener>> it = this.common.mCallbacks.iterator();
        while (it.hasNext()) {
            Storage.OnStoreCompletedListener onStoreCompletedListener = it.next().get();
            if (onStoreCompletedListener == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("notifyStoreResult X - 1");
                    return;
                }
                return;
            } else if (storeDataResult.isSuccess()) {
                onStoreCompletedListener.onStoreCompleted(storeDataResult.uri, storeDataResult.savingRequest, storeDataResult.savingRequest.getStorageType());
            } else {
                onStoreCompletedListener.onStoreFailed(storeDataResult.uri, storeDataResult.savingRequest, storeDataResult.getResultCode());
            }
        }
    }

    public void log() {
        this.common.log();
    }
}
