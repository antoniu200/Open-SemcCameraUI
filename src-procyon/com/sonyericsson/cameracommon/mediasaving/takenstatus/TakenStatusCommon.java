// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.takenstatus;

import com.sonyericsson.android.camera.util.CamLog;
import java.util.ArrayList;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import android.net.Uri;
import com.sonyericsson.cameracommon.storage.Storage;
import java.lang.ref.WeakReference;
import java.util.List;
import android.location.Location;

public class TakenStatusCommon
{
    public static final int INVALID_ID = -1;
    public static final String TAG = "TakenStatusCommon";
    public final boolean addToMediaStore;
    public final String cropValue;
    public final String fileExtension;
    public final int height;
    public final Location location;
    public List<WeakReference<Storage.OnStoreCompletedListener>> mCallbacks;
    public int mCaptureIdForPredictiveCapture;
    public long mDateTaken;
    public Uri mExtraOutput;
    public String mFilePath;
    public int mRequestId;
    public String mSaveTimeForPredictiveCapture;
    public int mSomcType;
    public final String mimeType;
    public int orientation;
    public final SavingTaskManager.SavedFileType savedFileType;
    public final boolean takenByFastCapture;
    public final int width;
    
    public TakenStatusCommon(final long mDateTaken, final int orientation, final Location location, final int width, final int height, final String mimeType, final String fileExtension, final SavingTaskManager.SavedFileType savedFileType, final String mFilePath, final String cropValue, final boolean addToMediaStore, final boolean takenByFastCapture) {
        this.mRequestId = -1;
        this.mDateTaken = 0L;
        this.mCallbacks = new ArrayList<WeakReference<Storage.OnStoreCompletedListener>>();
        this.mSomcType = 0;
        this.mCaptureIdForPredictiveCapture = -1;
        this.mDateTaken = mDateTaken;
        this.orientation = orientation;
        this.location = location;
        this.width = width;
        this.height = height;
        this.mimeType = mimeType;
        this.fileExtension = fileExtension;
        this.savedFileType = savedFileType;
        this.mFilePath = mFilePath;
        this.cropValue = cropValue;
        this.addToMediaStore = addToMediaStore;
        this.takenByFastCapture = takenByFastCapture;
    }
    
    public TakenStatusCommon(final TakenStatusCommon takenStatusCommon) {
        this.mRequestId = -1;
        this.mDateTaken = 0L;
        this.mCallbacks = new ArrayList<WeakReference<Storage.OnStoreCompletedListener>>();
        this.mSomcType = 0;
        this.mCaptureIdForPredictiveCapture = -1;
        this.mRequestId = takenStatusCommon.mRequestId;
        this.mDateTaken = takenStatusCommon.mDateTaken;
        this.orientation = takenStatusCommon.orientation;
        this.location = takenStatusCommon.location;
        this.width = takenStatusCommon.width;
        this.height = takenStatusCommon.height;
        this.mimeType = takenStatusCommon.mimeType;
        this.fileExtension = takenStatusCommon.fileExtension;
        this.savedFileType = takenStatusCommon.savedFileType;
        this.mFilePath = takenStatusCommon.mFilePath;
        this.mCallbacks = takenStatusCommon.mCallbacks;
        this.cropValue = takenStatusCommon.cropValue;
        this.addToMediaStore = takenStatusCommon.addToMediaStore;
        this.mExtraOutput = takenStatusCommon.mExtraOutput;
        this.mSomcType = takenStatusCommon.mSomcType;
        this.takenByFastCapture = takenStatusCommon.takenByFastCapture;
    }
    
    public TakenStatusCommon(final TakenStatusCommon takenStatusCommon, final String mFilePath, final long mDateTaken) {
        this.mRequestId = -1;
        this.mDateTaken = 0L;
        this.mCallbacks = new ArrayList<WeakReference<Storage.OnStoreCompletedListener>>();
        this.mSomcType = 0;
        this.mCaptureIdForPredictiveCapture = -1;
        this.mRequestId = takenStatusCommon.mRequestId;
        this.mDateTaken = mDateTaken;
        this.orientation = takenStatusCommon.orientation;
        this.location = takenStatusCommon.location;
        this.width = takenStatusCommon.width;
        this.height = takenStatusCommon.height;
        this.mimeType = takenStatusCommon.mimeType;
        this.fileExtension = takenStatusCommon.fileExtension;
        this.savedFileType = takenStatusCommon.savedFileType;
        this.mFilePath = mFilePath;
        this.mCallbacks = takenStatusCommon.mCallbacks;
        this.cropValue = takenStatusCommon.cropValue;
        this.addToMediaStore = takenStatusCommon.addToMediaStore;
        this.takenByFastCapture = takenStatusCommon.takenByFastCapture;
    }
    
    public void log() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("RequestId          : ");
            sb.append(this.mRequestId);
            CamLog.d(sb.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("DateTaken          : ");
            sb2.append(this.mDateTaken);
            CamLog.d(sb2.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Orientation        : ");
            sb3.append(this.orientation);
            CamLog.d(sb3.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Location           : ");
            sb4.append(this.location);
            CamLog.d(sb4.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Width, Height      : ");
            sb5.append(this.width);
            sb5.append(", ");
            sb5.append(this.height);
            CamLog.d(sb5.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("MimeType           : ");
            sb6.append(this.mimeType);
            CamLog.d(sb6.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("FileExtension      : ");
            sb7.append(this.fileExtension);
            CamLog.d(sb7.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("FilePath           : ");
            sb8.append(this.mFilePath);
            CamLog.d(sb8.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("Callbacks          : ");
            sb9.append(this.mCallbacks);
            CamLog.d(sb9.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("SavedFileType      : ");
            sb10.append(this.savedFileType);
            CamLog.d(sb10.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb11 = new StringBuilder();
            sb11.append("CropValue          : ");
            sb11.append(this.cropValue);
            CamLog.d(sb11.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb12 = new StringBuilder();
            sb12.append("AddToMediaStore    : ");
            sb12.append(this.addToMediaStore);
            CamLog.d(sb12.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb13 = new StringBuilder();
            sb13.append("ExtraOutput        : ");
            sb13.append(this.mExtraOutput);
            CamLog.d(sb13.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb14 = new StringBuilder();
            sb14.append("SomcType           : ");
            sb14.append(this.mSomcType);
            CamLog.d(sb14.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb15 = new StringBuilder();
            sb15.append("TakenByFastCapture : ");
            sb15.append(this.takenByFastCapture);
            CamLog.d(sb15.toString());
        }
    }
}
