// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import android.net.Uri;
import android.content.ContentValues;
import java.util.Iterator;
import java.lang.ref.WeakReference;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;

public abstract class SavingRequest
{
    public static final String TAG = "SavingRequest";
    public final TakenStatusCommon common;
    private boolean mFinalRequest;
    public boolean mIsNecessaryMediaUpload;
    private boolean mIsOneShot;
    private Storage.StorageType mStorageType;
    
    public SavingRequest(final TakenStatusCommon common) {
        this.mStorageType = Storage.StorageType.INTERNAL;
        this.mFinalRequest = true;
        this.mIsOneShot = false;
        this.mIsNecessaryMediaUpload = false;
        this.common = common;
    }
    
    public SavingRequest(final SavingRequest savingRequest) {
        this.mStorageType = Storage.StorageType.INTERNAL;
        this.mFinalRequest = true;
        this.mIsOneShot = false;
        this.mIsNecessaryMediaUpload = false;
        this.common = new TakenStatusCommon(savingRequest.common);
    }
    
    public SavingRequest(final SavingRequest savingRequest, final int n) {
        this.mStorageType = Storage.StorageType.INTERNAL;
        this.mFinalRequest = true;
        this.mIsOneShot = false;
        this.mIsNecessaryMediaUpload = false;
        this.common = new TakenStatusCommon(savingRequest.common.mDateTaken, n, savingRequest.common.location, savingRequest.common.width, savingRequest.common.height, savingRequest.common.mimeType, savingRequest.common.fileExtension, savingRequest.common.savedFileType, savingRequest.common.mFilePath, savingRequest.common.cropValue, savingRequest.common.addToMediaStore, savingRequest.common.takenByFastCapture);
        this.common.mCallbacks = savingRequest.common.mCallbacks;
    }
    
    public void addCallback(final Storage.OnStoreCompletedListener referent) {
        final Iterator<WeakReference<Storage.OnStoreCompletedListener>> iterator = this.common.mCallbacks.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().get() == referent) {
                return;
            }
        }
        this.common.mCallbacks.add(new WeakReference<Storage.OnStoreCompletedListener>(referent));
    }
    
    public abstract ContentValues createContentValues(final String p0);
    
    public int getCaptureIdForPredictiveCapture() {
        return this.common.mCaptureIdForPredictiveCapture;
    }
    
    public long getDateTaken() {
        return this.common.mDateTaken;
    }
    
    public Uri getExtraOutput() {
        return this.common.mExtraOutput;
    }
    
    public String getFilePath() {
        return this.common.mFilePath;
    }
    
    public boolean getIsNecessaryMediaUpload() {
        return this.mIsNecessaryMediaUpload;
    }
    
    public int getRequestId() {
        return this.common.mRequestId;
    }
    
    public String getSaveTimeForPredictiveCapture() {
        return this.common.mSaveTimeForPredictiveCapture;
    }
    
    public int getSomcType() {
        return this.common.mSomcType;
    }
    
    public Storage.StorageType getStorageType() {
        return this.mStorageType;
    }
    
    public boolean isFinalInSavingGroup() {
        return this.mFinalRequest;
    }
    
    public boolean isOneShot() {
        return this.mIsOneShot;
    }
    
    public void log() {
        this.common.log();
    }
    
    public void notifyStoreFailed(final MediaSavingResult mediaSavingResult) {
        final StoreDataResult storeDataResult = new StoreDataResult(MediaSavingResult.FAIL, Uri.EMPTY, this);
        final Iterator<WeakReference<Storage.OnStoreCompletedListener>> iterator = this.common.mCallbacks.iterator();
        while (iterator.hasNext()) {
            final Storage.OnStoreCompletedListener onStoreCompletedListener = iterator.next().get();
            if (onStoreCompletedListener == null) {
                return;
            }
            onStoreCompletedListener.onStoreFailed(storeDataResult.uri, storeDataResult.savingRequest, mediaSavingResult.mResultCode);
        }
    }
    
    public void notifyStoreResult(final StoreDataResult storeDataResult) {
        if (CamLog.VERBOSE) {
            CamLog.d("notifyStoreResult E");
        }
        final Iterator<WeakReference<Storage.OnStoreCompletedListener>> iterator = this.common.mCallbacks.iterator();
        while (iterator.hasNext()) {
            final Storage.OnStoreCompletedListener onStoreCompletedListener = iterator.next().get();
            if (onStoreCompletedListener == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("notifyStoreResult X - 1");
                }
                return;
            }
            if (storeDataResult.isSuccess()) {
                onStoreCompletedListener.onStoreCompleted(storeDataResult.uri, storeDataResult.savingRequest, storeDataResult.savingRequest.getStorageType());
            }
            else {
                onStoreCompletedListener.onStoreFailed(storeDataResult.uri, storeDataResult.savingRequest, storeDataResult.getResultCode());
            }
        }
    }
    
    public void setCaptureIdForPredictiveCapture(final int mCaptureIdForPredictiveCapture) {
        this.common.mCaptureIdForPredictiveCapture = mCaptureIdForPredictiveCapture;
    }
    
    public void setDateTaken(final long n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getDateTaken: ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        this.common.mDateTaken = n;
    }
    
    public void setExtraOutput(final Uri uri) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setExtraOutput: ");
            sb.append(uri);
            CamLog.d(sb.toString());
        }
        this.common.mExtraOutput = uri;
    }
    
    public void setFilePath(final String s) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setFilePath: ");
            sb.append(s);
            CamLog.d(sb.toString());
        }
        this.common.mFilePath = s;
    }
    
    public void setFinalInSavingGroup(final boolean mFinalRequest) {
        this.mFinalRequest = mFinalRequest;
    }
    
    public void setIsNecessaryMediaUpload(final boolean mIsNecessaryMediaUpload) {
        this.mIsNecessaryMediaUpload = mIsNecessaryMediaUpload;
    }
    
    public void setOneShot(final boolean mIsOneShot) {
        this.mIsOneShot = mIsOneShot;
    }
    
    public void setRequestId(final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setRequestId: ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        this.common.mRequestId = n;
    }
    
    public void setSaveTimeForPredictiveCapture(final String mSaveTimeForPredictiveCapture) {
        this.common.mSaveTimeForPredictiveCapture = mSaveTimeForPredictiveCapture;
    }
    
    public void setSomcType(final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setSomcType: ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        this.common.mSomcType = n;
    }
    
    void setStorageType(final Storage.StorageType mStorageType) {
        this.mStorageType = mStorageType;
    }
}
