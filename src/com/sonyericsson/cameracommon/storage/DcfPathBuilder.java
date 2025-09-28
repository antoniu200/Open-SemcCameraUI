package com.sonyericsson.cameracommon.storage;

import android.net.Uri;
import android.os.Environment;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import com.sonyericsson.cameracommon.constants.CommonConstants;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.storage.Storage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DcfPathBuilder {
    public static final String DCF_DIR_NAME_FREE_WORD = "ANDRO";
    public static final String DCF_FILE_NAME_FREE_WORD_MOVIE = "MOV_";
    public static final String DCF_FILE_NAME_FREE_WORD_PICTURE = "DSC_";
    public static final int LENGTH_OF_DIR_NAME = 8;
    public static final int LENGTH_OF_FILE_NAME = 12;
    public static final int MAX_DIR_NAME = 999;
    public static final int MAX_FILE_NAME = 9999;
    public static final int MIN_DIR_NAME = 100;
    public static final int MIN_FILE_NAME = 1;
    private static final ScanResult SCAN_RESULT_FAILED = new ScanResult(ScanResultState.SCAN_FAILED, -1, -1);
    private static final int SCAN_WAIT_TIME = 60000;
    public static final String TAG = "DcfPathBuilder";
    public static final int TYPE_PICTURE = 0;
    public static final int TYPE_VIDEO = 1;
    public static final String VOLUME_EXTERNAL = "external";
    private DcfImageDirNameFilter mDirNameFilter;
    private int mDirNo;
    private DcfImageFileNameFilter mFileNameFilter;
    private int mFileNo;
    private final String mRoot;
    private Future<?> mScanFuture;
    private ScanResult mScanResult = null;
    private ExecutorService mScanExecutor = ThreadUtil.buildExecutor(CommonConstants.THREAD_SCAN_STORAGE);

    private enum ScanResultState {
        SCAN_SUCCEEDED,
        SCAN_FAILED
    }

    private static class ScanResult {
        final int resultDirNo;
        final int resultFileNo;
        final ScanResultState resultState;

        ScanResult(ScanResultState scanResultState, int i, int i2) {
            this.resultState = scanResultState;
            this.resultDirNo = i;
            this.resultFileNo = i2;
        }
    }

    private static class DcfImageDirNameFilter implements FilenameFilter {
        private String mFilterDirName;
        private int mFilterDirNo;

        private DcfImageDirNameFilter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            int i;
            try {
                if (str.length() == 8 && (i = Integer.parseInt((String) str.subSequence(0, 3))) >= this.mFilterDirNo && 100 <= i && i <= 999) {
                    if ((String.format(Locale.US, "%03d", Integer.valueOf(i)) + DcfPathBuilder.DCF_DIR_NAME_FREE_WORD).equalsIgnoreCase(str)) {
                        File file2 = new File(file, str);
                        if (file2.isDirectory()) {
                            this.mFilterDirNo = i;
                            this.mFilterDirName = file2.getAbsolutePath();
                            return true;
                        }
                    }
                }
                return false;
            } catch (NumberFormatException unused) {
                return false;
            }
        }
    }

    private static class DcfImageFileNameFilter implements FilenameFilter {
        private int mFilterFileNo;

        private DcfImageFileNameFilter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            int i;
            try {
                if (str.length() != 12 || (i = Integer.parseInt((String) str.subSequence(4, 8))) < this.mFilterFileNo || 1 > i || i > 9999) {
                    return false;
                }
                this.mFilterFileNo = i;
                return true;
            } catch (NumberFormatException unused) {
                return false;
            }
        }
    }

    public DcfPathBuilder(String str) {
        this.mRoot = str;
        this.mDirNameFilter = new DcfImageDirNameFilter();
        this.mFileNameFilter = new DcfImageFileNameFilter();
    }

    public void startScan() {
        if (CamLog.VERBOSE) {
            CamLog.d("startScan is called: " + this.mRoot);
        }
        synchronized (this) {
            if (this.mScanFuture != null) {
                if (this.mScanFuture.isDone()) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Discard previous unread scan result. " + this.mRoot);
                    }
                    this.mScanFuture = null;
                } else {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Now scanning. Ignore: " + this.mRoot);
                    }
                    return;
                }
            }
            if (this.mScanFuture == null) {
                this.mScanFuture = this.mScanExecutor.submit(new ScanTask());
                if (CamLog.VERBOSE) {
                    CamLog.d("Scan has submitted: " + this.mRoot);
                }
            }
        }
    }

    class ScanTask implements Callable<ScanResult> {
        private int mScanDirNo = 100;
        private int mScanFileNo = 1;

        ScanTask() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        public ScanResult call() {
            ScanResultState scanResultState;
            if (CamLog.VERBOSE) {
                CamLog.d("ScanTask in: " + DcfPathBuilder.this.mRoot);
            }
            if (CamLog.VERBOSE) {
                CamLog.d("start: " + System.currentTimeMillis());
            }
            PerfLog.DCF_PATH_BUILDER_SCAN.begin();
            if (search()) {
                scanResultState = ScanResultState.SCAN_SUCCEEDED;
            } else {
                CamLog.e("Scan failed.");
                scanResultState = ScanResultState.SCAN_FAILED;
                this.mScanDirNo = -1;
                this.mScanFileNo = -1;
            }
            PerfLog.DCF_PATH_BUILDER_SCAN.end();
            if (CamLog.VERBOSE) {
                CamLog.d("end  : " + System.currentTimeMillis());
            }
            if (CamLog.VERBOSE) {
                CamLog.d("ScanTask out:" + DcfPathBuilder.this.mRoot);
            }
            if (CamLog.VERBOSE) {
                CamLog.d("ScanTask result dirNo: " + this.mScanDirNo + ", fileNo: " + this.mScanFileNo);
            }
            return new ScanResult(scanResultState, this.mScanDirNo, this.mScanFileNo);
        }

        private final boolean search() {
            if (!DcfPathBuilder.checkAndCreateDirectory(DcfPathBuilder.this.mRoot)) {
                CamLog.e("search error DCIM is not exist. " + DcfPathBuilder.this.mRoot);
                return false;
            }
            return searchImageDir();
        }

        private boolean searchImageDir() {
            File file = new File(DcfPathBuilder.getDcimDirectory(DcfPathBuilder.this.mRoot));
            DcfPathBuilder.this.mDirNameFilter.mFilterDirNo = 100;
            String[] list = file.list(DcfPathBuilder.this.mDirNameFilter);
            if (list != null && list.length != 0) {
                this.mScanDirNo = DcfPathBuilder.this.mDirNameFilter.mFilterDirNo;
                return searchImageNo(DcfPathBuilder.getDcimDirectory(DcfPathBuilder.this.mRoot) + SharedPrefsTranslator.CONNECTOR_SLASH + new File(DcfPathBuilder.this.mDirNameFilter.mFilterDirName).getName());
            }
            this.mScanDirNo = 100;
            this.mScanFileNo = 1;
            return true;
        }

        private boolean searchImageNo(String str) {
            File file = new File(str);
            DcfPathBuilder.this.mFileNameFilter.mFilterFileNo = 1;
            String[] list = file.list(DcfPathBuilder.this.mFileNameFilter);
            if (list != null && list.length != 0) {
                this.mScanFileNo = DcfPathBuilder.this.mFileNameFilter.mFilterFileNo + 1;
            } else {
                this.mScanFileNo = 1;
            }
            if (this.mScanFileNo > 9999) {
                this.mScanDirNo++;
                this.mScanFileNo = 1;
            }
            if (this.mScanDirNo <= 999) {
                return true;
            }
            CamLog.e("searchImageNo over max dir. " + this.mScanDirNo);
            return false;
        }
    }

    public static boolean checkAndCreateDirectory(String str) {
        File file = new File(getDcimDirectory(str));
        if (!file.isDirectory()) {
            if (CamLog.VERBOSE) {
                CamLog.d("mkdirs(): " + file);
            }
            if (StorageUtil.getStorageTypeFromPath(str, CameraApplication.getContext()) != Storage.StorageType.EXTERNAL_CARD) {
                if (!file.mkdirs()) {
                    CamLog.e("Failed mkdirs() : " + file.getPath());
                    return false;
                }
            } else {
                Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(CameraApplication.getContext());
                if (sdCardGrantedUri == null) {
                    return false;
                }
                if (!StorageUtil.isExistDcimDirectory(sdCardGrantedUri) && StorageUtil.createDirectory(CameraApplication.getContext(), sdCardGrantedUri, Environment.DIRECTORY_DCIM) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isAlreadyLastFileExist(String str) {
        String dcimDirectory = getDcimDirectory(str);
        if (!new File(dcimDirectory + SharedPrefsTranslator.CONNECTOR_SLASH + String.format(Locale.US, "%03d", 999) + DCF_DIR_NAME_FREE_WORD).isDirectory()) {
            return false;
        }
        if (new File((dcimDirectory + SharedPrefsTranslator.CONNECTOR_SLASH + String.format(Locale.US, "%03d", 999) + DCF_DIR_NAME_FREE_WORD) + "/DSC_" + String.format(Locale.US, "%04d", Integer.valueOf(MAX_FILE_NAME)) + MediaSavingConstants.MEDIA_TYPE_JPEG_EXT).isFile()) {
            return true;
        }
        String str2 = (dcimDirectory + SharedPrefsTranslator.CONNECTOR_SLASH + String.format(Locale.US, "%03d", 999) + DCF_DIR_NAME_FREE_WORD) + "/MOV_" + String.format(Locale.US, "%04d", Integer.valueOf(MAX_FILE_NAME));
        if (new File(str2 + MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT).isFile()) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(MediaSavingConstants.MEDIA_TYPE_3GP_EXT);
        return new File(sb.toString()).isFile();
    }

    private String assignImageFilePath(int i, Storage.StorageType storageType) throws IOException {
        Future<?> future;
        String str;
        synchronized (this) {
            future = this.mScanFuture;
            this.mScanFuture = null;
        }
        if (future != null) {
            try {
                this.mScanResult = SCAN_RESULT_FAILED;
                this.mScanResult = (ScanResult) future.get(60000L, TimeUnit.MILLISECONDS);
                this.mDirNo = this.mScanResult.resultDirNo;
                this.mFileNo = this.mScanResult.resultFileNo;
                if (CamLog.VERBOSE) {
                    CamLog.d("ScanResult is updated. mDirNo: " + this.mDirNo + ", mFileNo: " + this.mFileNo);
                }
            } catch (InterruptedException e) {
                throw new IOException("Failed to scan." + e);
            } catch (ExecutionException e2) {
                throw new IOException("Failed to scan." + e2);
            } catch (TimeoutException e3) {
                throw new IOException("Failed to scan." + e3);
            }
        }
        if (this.mScanResult.resultState != ScanResultState.SCAN_SUCCEEDED) {
            CamLog.e("assignImageFilePath scan failed.");
            return null;
        }
        if (this.mDirNo > 999) {
            CamLog.e("assignImageFilePath over max dir. " + this.mDirNo);
            return null;
        }
        String str2 = String.format(Locale.US, "%03d", Integer.valueOf(this.mDirNo)) + DCF_DIR_NAME_FREE_WORD;
        String str3 = getDcimDirectory(this.mRoot) + SharedPrefsTranslator.CONNECTOR_SLASH + str2;
        if (storageType != Storage.StorageType.EXTERNAL_CARD) {
            File file = new File(str3);
            if (!file.exists() && !file.mkdirs()) {
                CamLog.e("assignImageFilePath create dir failed: " + file);
                return null;
            }
        } else {
            Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(CameraApplication.getContext());
            if (!StorageUtil.isExistDcimDirectory(sdCardGrantedUri)) {
                str2 = Environment.DIRECTORY_DCIM + SharedPrefsTranslator.CONNECTOR_SLASH + str2;
            }
            if (StorageUtil.createDirectory(CameraApplication.getContext(), sdCardGrantedUri, str2) == null) {
                return null;
            }
        }
        switch (i) {
            case 0:
                str = str3 + "/DSC_";
                break;
            case 1:
                str = str3 + "/MOV_";
                break;
            default:
                CamLog.e("assignImageFilePath type failed. " + i);
                return null;
        }
        String str4 = str + String.format(Locale.US, "%04d", Integer.valueOf(this.mFileNo));
        this.mFileNo++;
        if (this.mFileNo > 9999) {
            this.mDirNo++;
            this.mFileNo = 1;
        }
        return str4;
    }

    public static String getDcimDirectory(String str) {
        return str + SharedPrefsTranslator.CONNECTOR_SLASH + Environment.DIRECTORY_DCIM;
    }

    public String getPhotoPath(Storage.StorageType storageType) throws IOException {
        String strAssignImageFilePath = null;
        while (true) {
            try {
                strAssignImageFilePath = assignImageFilePath(0, storageType);
            } catch (IOException e) {
                CamLog.e("getPhotoPath failed.", e);
            }
            if (strAssignImageFilePath != null) {
                strAssignImageFilePath = strAssignImageFilePath + MediaSavingConstants.MEDIA_TYPE_JPEG_EXT;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("getPhotoPath: " + strAssignImageFilePath);
            }
            if (strAssignImageFilePath == null || !isAssignedFileAlreadyExist(strAssignImageFilePath)) {
                break;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("Assigned file is exist. Try again. " + this.mRoot);
            }
            startScan();
        }
        return strAssignImageFilePath;
    }

    public String getVideoPath(String str, Storage.StorageType storageType) throws IOException {
        String strAssignImageFilePath = MediaSavingConstants.INVALID_FILE_PATH;
        while (true) {
            try {
                strAssignImageFilePath = assignImageFilePath(1, storageType);
            } catch (IOException e) {
                CamLog.e("getVideoPath failed.", e);
            }
            if (strAssignImageFilePath == null) {
                strAssignImageFilePath = MediaSavingConstants.INVALID_FILE_PATH;
            } else {
                strAssignImageFilePath = strAssignImageFilePath + str;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("getVideoPath: " + strAssignImageFilePath);
            }
            if (strAssignImageFilePath == MediaSavingConstants.INVALID_FILE_PATH || !isAssignedFileAlreadyExist(strAssignImageFilePath)) {
                break;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("Assigned file is exist. Try again. " + this.mRoot);
            }
            startScan();
        }
        return strAssignImageFilePath;
    }

    private boolean isAssignedFileAlreadyExist(String str) {
        if (str != null) {
            return new File(str).exists();
        }
        return false;
    }

    public String getRootPath() {
        return this.mRoot;
    }

    public static boolean checkWritable(String str) {
        if (!checkAndCreateDirectory(str)) {
            return false;
        }
        String dcimDirectory = getDcimDirectory(str);
        return new File(dcimDirectory).canWrite() && checkStorageWritable(dcimDirectory);
    }

    private static boolean checkStorageWritable(String str) {
        String strValueOf = String.valueOf(new Date().getTime());
        File fileCreateTempFile = null;
        boolean z = true;
        try {
            if (str != null) {
                try {
                    try {
                        fileCreateTempFile = File.createTempFile(strValueOf, null, new File(str));
                    } catch (IOException unused) {
                        CamLog.e("Failed createTempFile() : " + str);
                        return false;
                    } catch (IllegalArgumentException unused2) {
                        CamLog.e("Failed createTempFile() parameter error : " + str);
                        return false;
                    }
                } catch (SecurityException unused3) {
                    CamLog.e("Failed createTempFile() not allowed : " + str);
                    return false;
                }
            } else {
                z = false;
            }
            if (fileCreateTempFile != null) {
                try {
                    if (!fileCreateTempFile.delete()) {
                        CamLog.e("tempFile delete error");
                    }
                } catch (SecurityException unused4) {
                    CamLog.e("Failed delete() not allowed");
                }
            }
            return z;
        } catch (Throwable unused5) {
            return true;
        }
    }
}
