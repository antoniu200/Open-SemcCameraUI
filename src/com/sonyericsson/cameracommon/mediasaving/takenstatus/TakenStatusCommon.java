package com.sonyericsson.cameracommon.mediasaving.takenstatus;

import android.location.Location;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import com.sonyericsson.cameracommon.storage.Storage;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TakenStatusCommon {
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

    public TakenStatusCommon(long j, int i, Location location, int i2, int i3, String str, String str2, SavingTaskManager.SavedFileType savedFileType, String str3, String str4, boolean z, boolean z2) {
        this.mRequestId = -1;
        this.mDateTaken = 0L;
        this.mCallbacks = new ArrayList();
        this.mSomcType = 0;
        this.mCaptureIdForPredictiveCapture = -1;
        this.mDateTaken = j;
        this.orientation = i;
        this.location = location;
        this.width = i2;
        this.height = i3;
        this.mimeType = str;
        this.fileExtension = str2;
        this.savedFileType = savedFileType;
        this.mFilePath = str3;
        this.cropValue = str4;
        this.addToMediaStore = z;
        this.takenByFastCapture = z2;
    }

    public TakenStatusCommon(TakenStatusCommon takenStatusCommon) {
        this.mRequestId = -1;
        this.mDateTaken = 0L;
        this.mCallbacks = new ArrayList();
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

    public TakenStatusCommon(TakenStatusCommon takenStatusCommon, String str, long j) {
        this.mRequestId = -1;
        this.mDateTaken = 0L;
        this.mCallbacks = new ArrayList();
        this.mSomcType = 0;
        this.mCaptureIdForPredictiveCapture = -1;
        this.mRequestId = takenStatusCommon.mRequestId;
        this.mDateTaken = j;
        this.orientation = takenStatusCommon.orientation;
        this.location = takenStatusCommon.location;
        this.width = takenStatusCommon.width;
        this.height = takenStatusCommon.height;
        this.mimeType = takenStatusCommon.mimeType;
        this.fileExtension = takenStatusCommon.fileExtension;
        this.savedFileType = takenStatusCommon.savedFileType;
        this.mFilePath = str;
        this.mCallbacks = takenStatusCommon.mCallbacks;
        this.cropValue = takenStatusCommon.cropValue;
        this.addToMediaStore = takenStatusCommon.addToMediaStore;
        this.takenByFastCapture = takenStatusCommon.takenByFastCapture;
    }

    public void log() {
        if (CamLog.VERBOSE) {
            CamLog.d("RequestId          : " + this.mRequestId);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("DateTaken          : " + this.mDateTaken);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Orientation        : " + this.orientation);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Location           : " + this.location);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Width, Height      : " + this.width + ", " + this.height);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("MimeType           : " + this.mimeType);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("FileExtension      : " + this.fileExtension);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("FilePath           : " + this.mFilePath);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Callbacks          : " + this.mCallbacks);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("SavedFileType      : " + this.savedFileType);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("CropValue          : " + this.cropValue);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("AddToMediaStore    : " + this.addToMediaStore);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("ExtraOutput        : " + this.mExtraOutput);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("SomcType           : " + this.mSomcType);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("TakenByFastCapture : " + this.takenByFastCapture);
        }
    }
}
