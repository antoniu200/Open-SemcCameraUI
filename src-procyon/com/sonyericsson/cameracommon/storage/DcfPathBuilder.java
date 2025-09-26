// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import com.sonyericsson.android.camera.util.PerfLog;
import java.io.FilenameFilter;
import java.util.concurrent.Callable;
import java.util.Date;
import android.net.Uri;
import android.os.Environment;
import com.sonyericsson.android.camera.CameraApplication;
import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.io.IOException;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.concurrent.TimeUnit;
import com.sonyericsson.android.camera.util.ThreadUtil;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;

public class DcfPathBuilder
{
    public static final String DCF_DIR_NAME_FREE_WORD = "ANDRO";
    public static final String DCF_FILE_NAME_FREE_WORD_MOVIE = "MOV_";
    public static final String DCF_FILE_NAME_FREE_WORD_PICTURE = "DSC_";
    public static final int LENGTH_OF_DIR_NAME = 8;
    public static final int LENGTH_OF_FILE_NAME = 12;
    public static final int MAX_DIR_NAME = 999;
    public static final int MAX_FILE_NAME = 9999;
    public static final int MIN_DIR_NAME = 100;
    public static final int MIN_FILE_NAME = 1;
    private static final ScanResult SCAN_RESULT_FAILED;
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
    private ExecutorService mScanExecutor;
    private Future<?> mScanFuture;
    private ScanResult mScanResult;
    
    static {
        SCAN_RESULT_FAILED = new ScanResult(ScanResultState.SCAN_FAILED, -1, -1);
    }
    
    public DcfPathBuilder(final String mRoot) {
        this.mScanResult = null;
        this.mRoot = mRoot;
        this.mDirNameFilter = new DcfImageDirNameFilter();
        this.mFileNameFilter = new DcfImageFileNameFilter();
        this.mScanExecutor = ThreadUtil.buildExecutor("DCF Path Builder");
    }
    
    private String assignImageFilePath(final int i, final Storage.StorageType storageType) throws IOException {
        synchronized (this) {
            final Future<?> mScanFuture = this.mScanFuture;
            this.mScanFuture = null;
            monitorexit(this);
            if (mScanFuture != null) {
                try {
                    this.mScanResult = DcfPathBuilder.SCAN_RESULT_FAILED;
                    this.mScanResult = (ScanResult)mScanFuture.get(60000L, TimeUnit.MILLISECONDS);
                    this.mDirNo = this.mScanResult.resultDirNo;
                    this.mFileNo = this.mScanResult.resultFileNo;
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("ScanResult is updated. mDirNo: ");
                        sb.append(this.mDirNo);
                        sb.append(", mFileNo: ");
                        sb.append(this.mFileNo);
                        CamLog.d(sb.toString());
                    }
                }
                catch (final ExecutionException obj) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to scan.");
                    sb2.append(obj);
                    throw new IOException(sb2.toString());
                }
                catch (final TimeoutException obj2) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Failed to scan.");
                    sb3.append(obj2);
                    throw new IOException(sb3.toString());
                }
                catch (final InterruptedException obj3) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Failed to scan.");
                    sb4.append(obj3);
                    throw new IOException(sb4.toString());
                }
            }
            if (this.mScanResult.resultState != ScanResultState.SCAN_SUCCEEDED) {
                CamLog.e("assignImageFilePath scan failed.");
                return null;
            }
            if (this.mDirNo > 999) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("assignImageFilePath over max dir. ");
                sb5.append(this.mDirNo);
                CamLog.e(sb5.toString());
                return null;
            }
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(String.format(Locale.US, "%03d", this.mDirNo));
            sb6.append("ANDRO");
            final String string = sb6.toString();
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(getDcimDirectory(this.mRoot));
            sb7.append("/");
            sb7.append(string);
            final String string2 = sb7.toString();
            if (storageType != Storage.StorageType.EXTERNAL_CARD) {
                final File obj4 = new File(string2);
                if (!obj4.exists() && !obj4.mkdirs()) {
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append("assignImageFilePath create dir failed: ");
                    sb8.append(obj4);
                    CamLog.e(sb8.toString());
                    return null;
                }
            }
            else {
                final Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(CameraApplication.getContext());
                String string3;
                if (StorageUtil.isExistDcimDirectory(sdCardGrantedUri)) {
                    string3 = string;
                }
                else {
                    final StringBuilder sb9 = new StringBuilder();
                    sb9.append(Environment.DIRECTORY_DCIM);
                    sb9.append("/");
                    sb9.append(string);
                    string3 = sb9.toString();
                }
                if (StorageUtil.createDirectory(CameraApplication.getContext(), sdCardGrantedUri, string3) == null) {
                    return null;
                }
            }
            String str = null;
            switch (i) {
                default: {
                    final StringBuilder sb10 = new StringBuilder();
                    sb10.append("assignImageFilePath type failed. ");
                    sb10.append(i);
                    CamLog.e(sb10.toString());
                    return null;
                }
                case 1: {
                    final StringBuilder sb11 = new StringBuilder();
                    sb11.append(string2);
                    sb11.append("/MOV_");
                    str = sb11.toString();
                    break;
                }
                case 0: {
                    final StringBuilder sb12 = new StringBuilder();
                    sb12.append(string2);
                    sb12.append("/DSC_");
                    str = sb12.toString();
                    break;
                }
            }
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(str);
            sb13.append(String.format(Locale.US, "%04d", this.mFileNo));
            final String string4 = sb13.toString();
            ++this.mFileNo;
            if (this.mFileNo > 9999) {
                ++this.mDirNo;
                this.mFileNo = 1;
            }
            return string4;
        }
    }
    
    public static boolean checkAndCreateDirectory(final String s) {
        final File obj = new File(getDcimDirectory(s));
        final boolean directory = obj.isDirectory();
        final boolean b = false;
        if (!directory) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("mkdirs(): ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (StorageUtil.getStorageTypeFromPath(s, CameraApplication.getContext()) != Storage.StorageType.EXTERNAL_CARD) {
                if (!obj.mkdirs()) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed mkdirs() : ");
                    sb2.append(obj.getPath());
                    CamLog.e(sb2.toString());
                    return b;
                }
            }
            else {
                final Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(CameraApplication.getContext());
                boolean b2 = b;
                if (sdCardGrantedUri == null) {
                    return b2;
                }
                if (!StorageUtil.isExistDcimDirectory(sdCardGrantedUri) && StorageUtil.createDirectory(CameraApplication.getContext(), sdCardGrantedUri, Environment.DIRECTORY_DCIM) == null) {
                    b2 = b;
                    return b2;
                }
            }
        }
        return true;
    }
    
    private static boolean checkStorageWritable(String tempFile) {
        final long time = new Date().getTime();
        final File file = null;
        boolean b = true;
        if (tempFile != null) {
            try {
                tempFile = (String)File.createTempFile(String.valueOf(time), null, new File(tempFile));
            }
            catch (final SecurityException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed createTempFile() not allowed : ");
                sb.append(tempFile);
                CamLog.e(sb.toString());
                return false;
            }
            catch (final IOException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed createTempFile() : ");
                sb2.append(tempFile);
                CamLog.e(sb2.toString());
                return false;
            }
            catch (final IllegalArgumentException ex3) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Failed createTempFile() parameter error : ");
                sb3.append(tempFile);
                CamLog.e(sb3.toString());
                return false;
            }
            finally {
                return true;
            }
        }
        b = false;
        final File file2 = file;
        if (file2 != null) {
            try {
                if (!file2.delete()) {
                    CamLog.e("tempFile delete error");
                }
            }
            catch (final SecurityException ex4) {
                CamLog.e("Failed delete() not allowed");
            }
        }
        return b;
    }
    
    public static boolean checkWritable(String dcimDirectory) {
        final boolean checkAndCreateDirectory = checkAndCreateDirectory(dcimDirectory);
        final boolean b = false;
        if (!checkAndCreateDirectory) {
            return false;
        }
        dcimDirectory = getDcimDirectory(dcimDirectory);
        boolean b2 = b;
        if (new File(dcimDirectory).canWrite()) {
            b2 = b;
            if (checkStorageWritable(dcimDirectory)) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public static String getDcimDirectory(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/");
        sb.append(Environment.DIRECTORY_DCIM);
        return sb.toString();
    }
    
    public static boolean isAlreadyLastFileExist(String str) {
        str = getDcimDirectory(str);
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/");
        sb.append(String.format(Locale.US, "%03d", 999));
        sb.append("ANDRO");
        if (!new File(sb.toString()).isDirectory()) {
            return false;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append("/");
        sb2.append(String.format(Locale.US, "%03d", 999));
        sb2.append("ANDRO");
        final String string = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(string);
        sb3.append("/DSC_");
        sb3.append(String.format(Locale.US, "%04d", 9999));
        sb3.append(".JPG");
        if (new File(sb3.toString()).isFile()) {
            return true;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(str);
        sb4.append("/");
        sb4.append(String.format(Locale.US, "%03d", 999));
        sb4.append("ANDRO");
        final String string2 = sb4.toString();
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(string2);
        sb5.append("/MOV_");
        sb5.append(String.format(Locale.US, "%04d", 9999));
        str = sb5.toString();
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(str);
        sb6.append(".mp4");
        if (new File(sb6.toString()).isFile()) {
            return true;
        }
        final StringBuilder sb7 = new StringBuilder();
        sb7.append(str);
        sb7.append(".3gp");
        return new File(sb7.toString()).isFile();
    }
    
    private boolean isAssignedFileAlreadyExist(final String pathname) {
        return pathname != null && new File(pathname).exists();
    }
    
    public String getPhotoPath(final Storage.StorageType storageType) {
        String string = null;
        while (true) {
            String assignImageFilePath;
            try {
                assignImageFilePath = this.assignImageFilePath(0, storageType);
            }
            catch (final IOException ex) {
                CamLog.e("getPhotoPath failed.", ex);
                assignImageFilePath = string;
            }
            string = assignImageFilePath;
            if (assignImageFilePath != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(assignImageFilePath);
                sb.append(".JPG");
                string = sb.toString();
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("getPhotoPath: ");
                sb2.append(string);
                CamLog.d(sb2.toString());
            }
            if (string == null || !this.isAssignedFileAlreadyExist(string)) {
                break;
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Assigned file is exist. Try again. ");
                sb3.append(this.mRoot);
                CamLog.d(sb3.toString());
            }
            this.startScan();
        }
        return string;
    }
    
    public String getRootPath() {
        return this.mRoot;
    }
    
    public String getVideoPath(final String str, final Storage.StorageType storageType) {
        String s = "/dev/null";
        while (true) {
            try {
                s = this.assignImageFilePath(1, storageType);
            }
            catch (final IOException ex) {
                CamLog.e("getVideoPath failed.", ex);
            }
            if (s == null) {
                s = "/dev/null";
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(str);
                s = sb.toString();
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("getVideoPath: ");
                sb2.append(s);
                CamLog.d(sb2.toString());
            }
            if (s == "/dev/null" || !this.isAssignedFileAlreadyExist(s)) {
                break;
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Assigned file is exist. Try again. ");
                sb3.append(this.mRoot);
                CamLog.d(sb3.toString());
            }
            this.startScan();
        }
        return s;
    }
    
    public void startScan() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("startScan is called: ");
            sb.append(this.mRoot);
            CamLog.d(sb.toString());
        }
        synchronized (this) {
            if (this.mScanFuture != null) {
                if (!this.mScanFuture.isDone()) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Now scanning. Ignore: ");
                        sb2.append(this.mRoot);
                        CamLog.d(sb2.toString());
                    }
                    return;
                }
                if (CamLog.VERBOSE) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Discard previous unread scan result. ");
                    sb3.append(this.mRoot);
                    CamLog.d(sb3.toString());
                }
                this.mScanFuture = null;
            }
            if (this.mScanFuture == null) {
                this.mScanFuture = this.mScanExecutor.submit((Callable<?>)new ScanTask());
                if (CamLog.VERBOSE) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Scan has submitted: ");
                    sb4.append(this.mRoot);
                    CamLog.d(sb4.toString());
                }
            }
        }
    }
    
    private static class DcfImageDirNameFilter implements FilenameFilter
    {
        private String mFilterDirName;
        private int mFilterDirNo;
        
        @Override
        public boolean accept(final File parent, final String s) {
            try {
                if (s.length() == 8) {
                    final int int1 = Integer.parseInt((String)s.subSequence(0, 3));
                    if (int1 >= this.mFilterDirNo && 100 <= int1 && int1 <= 999) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(String.format(Locale.US, "%03d", int1));
                        sb.append("ANDRO");
                        if (sb.toString().equalsIgnoreCase(s)) {
                            final File file = new File(parent, s);
                            if (file.isDirectory()) {
                                this.mFilterDirNo = int1;
                                this.mFilterDirName = file.getAbsolutePath();
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            catch (final NumberFormatException ex) {
                return false;
            }
        }
    }
    
    private static class DcfImageFileNameFilter implements FilenameFilter
    {
        private int mFilterFileNo;
        
        @Override
        public boolean accept(final File file, final String s) {
            try {
                if (s.length() == 12) {
                    final int int1 = Integer.parseInt((String)s.subSequence(4, 8));
                    if (int1 >= this.mFilterFileNo && 1 <= int1 && int1 <= 9999) {
                        this.mFilterFileNo = int1;
                        return true;
                    }
                }
                return false;
            }
            catch (final NumberFormatException ex) {
                return false;
            }
        }
    }
    
    private static class ScanResult
    {
        final int resultDirNo;
        final int resultFileNo;
        final ScanResultState resultState;
        
        ScanResult(final ScanResultState resultState, final int resultDirNo, final int resultFileNo) {
            this.resultState = resultState;
            this.resultDirNo = resultDirNo;
            this.resultFileNo = resultFileNo;
        }
    }
    
    private enum ScanResultState
    {
        private static final ScanResultState[] $VALUES;
        
        SCAN_FAILED, 
        SCAN_SUCCEEDED;
        
        static {
            $VALUES = new ScanResultState[] { ScanResultState.SCAN_SUCCEEDED, ScanResultState.SCAN_FAILED };
        }
    }
    
    class ScanTask implements Callable<ScanResult>
    {
        private int mScanDirNo;
        private int mScanFileNo;
        final DcfPathBuilder this$0;
        
        ScanTask(final DcfPathBuilder this$0) {
            this.this$0 = this$0;
            this.mScanDirNo = 100;
            this.mScanFileNo = 1;
        }
        
        private final boolean search() {
            if (DcfPathBuilder.checkAndCreateDirectory(this.this$0.mRoot)) {
                return this.searchImageDir();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("search error DCIM is not exist. ");
            sb.append(this.this$0.mRoot);
            CamLog.e(sb.toString());
            return false;
        }
        
        private boolean searchImageDir() {
            final File file = new File(DcfPathBuilder.getDcimDirectory(this.this$0.mRoot));
            this.this$0.mDirNameFilter.mFilterDirNo = 100;
            final String[] list = file.list(this.this$0.mDirNameFilter);
            if (list != null && list.length != 0) {
                this.mScanDirNo = this.this$0.mDirNameFilter.mFilterDirNo;
                final String access$500 = this.this$0.mDirNameFilter.mFilterDirName;
                final StringBuilder sb = new StringBuilder();
                sb.append(DcfPathBuilder.getDcimDirectory(this.this$0.mRoot));
                sb.append("/");
                sb.append(new File(access$500).getName());
                return this.searchImageNo(sb.toString());
            }
            this.mScanDirNo = 100;
            this.mScanFileNo = 1;
            return true;
        }
        
        private boolean searchImageNo(final String pathname) {
            final File file = new File(pathname);
            this.this$0.mFileNameFilter.mFilterFileNo = 1;
            final String[] list = file.list(this.this$0.mFileNameFilter);
            if (list != null && list.length != 0) {
                this.mScanFileNo = this.this$0.mFileNameFilter.mFilterFileNo + 1;
            }
            else {
                this.mScanFileNo = 1;
            }
            if (this.mScanFileNo > 9999) {
                ++this.mScanDirNo;
                this.mScanFileNo = 1;
            }
            if (this.mScanDirNo > 999) {
                final StringBuilder sb = new StringBuilder();
                sb.append("searchImageNo over max dir. ");
                sb.append(this.mScanDirNo);
                CamLog.e(sb.toString());
                return false;
            }
            return true;
        }
        
        @Override
        public ScanResult call() {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ScanTask in: ");
                sb.append(this.this$0.mRoot);
                CamLog.d(sb.toString());
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("start: ");
                sb2.append(System.currentTimeMillis());
                CamLog.d(sb2.toString());
            }
            PerfLog.DCF_PATH_BUILDER_SCAN.begin();
            ScanResultState scanResultState;
            if (this.search()) {
                scanResultState = ScanResultState.SCAN_SUCCEEDED;
            }
            else {
                CamLog.e("Scan failed.");
                scanResultState = ScanResultState.SCAN_FAILED;
                this.mScanDirNo = -1;
                this.mScanFileNo = -1;
            }
            PerfLog.DCF_PATH_BUILDER_SCAN.end();
            if (CamLog.VERBOSE) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("end  : ");
                sb3.append(System.currentTimeMillis());
                CamLog.d(sb3.toString());
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("ScanTask out:");
                sb4.append(this.this$0.mRoot);
                CamLog.d(sb4.toString());
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("ScanTask result dirNo: ");
                sb5.append(this.mScanDirNo);
                sb5.append(", fileNo: ");
                sb5.append(this.mScanFileNo);
                CamLog.d(sb5.toString());
            }
            return new ScanResult(scanResultState, this.mScanDirNo, this.mScanFileNo);
        }
    }
}
