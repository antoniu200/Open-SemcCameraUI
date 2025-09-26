package com.sonyericsson.cameracommon.storage;

import android.content.Context;
import android.database.sqlite.SQLiteFullException;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.support.annotation.AnyThread;
import com.sonyericsson.android.camera.mediasaving.ExifOption;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.CapturePerformanceLogger;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.cameracommon.constants.CommonConstants;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.ThreadSafeOutputStream;
import com.sonyericsson.cameracommon.mediasaving.updator.MediaProviderUpdator;
import com.sonyericsson.cameracommon.storage.CameraStorageManager;
import com.sonyericsson.cameracommon.storage.Storage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SavingTaskManager {
    private static final String CAN_PUSH_STORE_TASK_HPROF_FILE_NAME = "/can_push_store_task.hprof";
    private static final String SUFFIX_TEMP_FILE = ".rewrite-exif";
    public static final String TAG = "SavingTaskManager";
    private static final String THREAD_NAME = "SavingTask";
    private static final ExecutorService mExecutor = ThreadUtil.buildExecutor(THREAD_NAME);
    Context mContext;
    private Handler mHandler;
    private final MediaScanController mMediaScanController;
    private final Map<Storage.StorageType, Queue<PhotoSavingTask>> mSavingTaskQueueMap;
    private Map<Storage.StorageType, Semaphore> mStorageAccessSemaphoreMap;
    private CameraStorageManager mStorageManager;
    private Handler mStoreDataHandler;
    private MediaProviderUpdator mUpdator;
    private Thread mStoreVideoThread = null;
    private SavingTaskInquiry mInquiry = new SavingTaskInquiry() { // from class: com.sonyericsson.cameracommon.storage.SavingTaskManager.1
        @Override // com.sonyericsson.cameracommon.storage.SavingTaskInquiry
        public long getReservedSize(Storage.StorageType storageType) {
            long expectedFileSize = 0;
            if (SavingTaskManager.this.mSavingTaskQueueMap.containsKey(storageType)) {
                while (new LinkedList((Collection) SavingTaskManager.this.mSavingTaskQueueMap.get(storageType)).iterator().hasNext()) {
                    expectedFileSize += ((PhotoSavingTask) r5.next()).getExpectedFileSize();
                }
            }
            return expectedFileSize;
        }
    };
    private final MediaScanController.OnScanCompletedListener mOnScanCompletedListener = new MediaScanController.OnScanCompletedListener() { // from class: com.sonyericsson.cameracommon.storage.SavingTaskManager.3
        @Override // com.sonyericsson.cameracommon.storage.SavingTaskManager.MediaScanController.OnScanCompletedListener
        public void onScanCompleted(MediaSavingResult mediaSavingResult, Uri uri, PhotoSavingRequest photoSavingRequest) {
            SavingTaskManager.this.notifyStoreComplete(MediaSavingResult.SUCCESS, uri, photoSavingRequest);
        }
    };

    public enum GeoMode {
        GEO_ON,
        GEO_OFF,
        GEO_RESHOW
    }

    public enum SavedFileType {
        PHOTO,
        PHOTO_DURING_REC,
        VIDEO,
        BURST,
        TIME_SHIFT
    }

    public class PhotoSavingTask implements Runnable {
        private volatile boolean mIsCanceled = false;
        private volatile boolean mIsRunning = false;
        private final PhotoSavingRequest mRequest;

        public PhotoSavingTask(PhotoSavingRequest photoSavingRequest) {
            this.mRequest = photoSavingRequest;
        }

        private Uri assignOutput() {
            String predictiveCapturePhotoPath;
            if (this.mRequest.getExtraOutput() != null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("assignOutput getExtraOutput != null");
                }
                if ("file".equalsIgnoreCase(this.mRequest.getExtraOutput().getScheme())) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("assignOutput getExtraOutput != null 2");
                    }
                    File file = new File(this.mRequest.getExtraOutput().getPath());
                    if (file.getParentFile().mkdirs()) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("Create dir: " + file.getParentFile().getPath());
                        }
                    } else {
                        CamLog.e("Filed mkdirs() : " + file.getParentFile().getPath());
                    }
                }
                return this.mRequest.getExtraOutput();
            }
            if (CamLog.VERBOSE) {
                CamLog.d("assignOutput getExtraOutput != null");
            }
            if (this.mRequest.getSomcType() == 129) {
                if (CamLog.VERBOSE) {
                    CamLog.d("assignOutput getSaveTimeForPredictiveCapture() =  null");
                }
                predictiveCapturePhotoPath = SavingTaskManager.this.mStorageManager.getBurstPhotoPath(this.mRequest);
            } else if (this.mRequest.getSaveTimeForPredictiveCapture() == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("assignOutput getSaveTimeForPredictiveCapture() =  null");
                }
                predictiveCapturePhotoPath = SavingTaskManager.this.mStorageManager.getPhotoPath(this.mRequest.getStorageType());
            } else {
                if (CamLog.VERBOSE) {
                    CamLog.d("assignOutput getSaveTimeForPredictiveCapture() !=  null");
                }
                predictiveCapturePhotoPath = SavingTaskManager.this.mStorageManager.getPredictiveCapturePhotoPath(this.mRequest);
            }
            if (predictiveCapturePhotoPath == null) {
                if (!CamLog.VERBOSE) {
                    return null;
                }
                CamLog.d("assignOutput path =  null");
                return null;
            }
            return Uri.fromFile(new File(predictiveCapturePhotoPath));
        }

        private int verifyImageDataBeforeStoring(PhotoSavingRequest photoSavingRequest, Uri uri) {
            int length;
            if (photoSavingRequest.isImageReaderUsing()) {
                length = photoSavingRequest.getImageReaderData().limit();
            } else {
                length = photoSavingRequest.getImageData().length;
            }
            if (length != 0) {
                return length;
            }
            String str = "The image data is empty. Camera will create broken file. uri:" + uri;
            ByteBuffer imageReaderData = photoSavingRequest.getImageReaderData();
            if (imageReaderData != null) {
                str = ((str + " buff.capacity:" + imageReaderData.capacity()) + " buff.limit:" + imageReaderData.limit()) + " buff.position:" + imageReaderData.position();
            }
            throw new RuntimeException(str);
        }

        private void verifyImageDataAfterStoring(int i, Uri uri) {
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                File file = new File(uri.getPath());
                if (file.length() != i) {
                    throw new RuntimeException("The requested image data is not stored correctly. uri:" + uri + " expected:" + i + " actual:" + file.length());
                }
            }
        }

        private boolean writeToStorage(PhotoSavingRequest photoSavingRequest, Uri uri) {
            boolean zStoreData;
            try {
                int iVerifyImageDataBeforeStoring = verifyImageDataBeforeStoring(photoSavingRequest, uri);
                if (photoSavingRequest.isImageReaderUsing()) {
                    zStoreData = SavingTaskManager.this.new ImageToFile(photoSavingRequest.getImageReaderData(), uri).storeData(null);
                    photoSavingRequest.close();
                } else {
                    zStoreData = SavingTaskManager.this.new ImageToFile(photoSavingRequest.getImageData(), uri).storeData(null);
                }
                if (CapturePerformanceLogger.get(photoSavingRequest) != null) {
                    CapturePerformanceLogger.get(photoSavingRequest).fileSize = iVerifyImageDataBeforeStoring;
                }
                if (CamLog.DEBUG && zStoreData) {
                    verifyImageDataAfterStoring(iVerifyImageDataBeforeStoring, uri);
                }
                return zStoreData;
            } catch (IllegalStateException e) {
                CamLog.e("Failed to store image. : " + e);
                return false;
            }
        }

        @Override // java.lang.Runnable
        public final void run() throws InterruptedException {
            if (this.mIsCanceled) {
                return;
            }
            this.mIsRunning = true;
            final Storage.StorageType storageType = this.mRequest.getStorageType();
            Semaphore semaphore = (Semaphore) SavingTaskManager.this.mStorageAccessSemaphoreMap.get(storageType);
            try {
                semaphore.acquire();
                if (CamLog.DEBUG) {
                    CamLog.d("PhotoSavingTask[" + storageType + "]: E");
                }
                Uri uriAssignOutput = assignOutput();
                if (uriAssignOutput != null) {
                    store(uriAssignOutput);
                    SavingTaskManager.this.popPhotoSavingTask(this);
                    SavingTaskManager.this.mStorageManager.updateStorageState(storageType, CameraStorageManager.UpdateRequestReason.PHOTO_STORING_COMPLETED);
                    SavingTaskManager.this.mStorageManager.checkAndNotifyStateChanged(storageType);
                } else {
                    if (CamLog.VERBOSE) {
                        CamLog.d("assignOutput() is null");
                    }
                    SavingTaskManager.this.popPhotoSavingTask(this);
                    SavingTaskManager.this.mStorageManager.updateStorageState(storageType, CameraStorageManager.UpdateRequestReason.PHOTO_STORING_COMPLETED);
                    this.mRequest.close();
                    SavingTaskManager.this.runOnUiThread(new Runnable() { // from class: com.sonyericsson.cameracommon.storage.SavingTaskManager.PhotoSavingTask.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SavingTaskManager.this.mStorageManager.checkRemain(true, storageType);
                        }
                    });
                    SavingTaskManager.this.notifyStoreComplete(MediaSavingResult.FAIL, Uri.EMPTY, this.mRequest);
                }
                semaphore.release();
                if (CamLog.DEBUG) {
                    CamLog.d("PhotoSavingTask[" + storageType + "]: X");
                }
            } catch (InterruptedException unused) {
                CamLog.e("Failed to acquire of storage access permit.");
            }
        }

        public void cancel() {
            this.mIsCanceled = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getExpectedFileSize() {
            if (this.mIsRunning) {
                return 0;
            }
            return this.mRequest.isImageReaderUsing() ? CommonConstants.EXPECTED_CAPTURE_FILESIZE_FOR_IMAGE_READER : this.mRequest.getImageData().length;
        }

        private void store(Uri uri) {
            Date date;
            if (CapturePerformanceLogger.get(this.mRequest) != null) {
                CapturePerformanceLogger.get(this.mRequest).startSave = SystemClock.uptimeMillis();
            }
            if (CamLog.VERBOSE) {
                CamLog.d("storeContent E URI:" + uri.toString());
            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("This uri is file path. " + uri.getPath());
                }
                this.mRequest.setFilePath(uri.getPath());
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("This uri is file content. " + uri.getPath());
                }
                this.mRequest.setFilePath(uri.getPath());
            }
            CamLog.d("[datetaken:" + this.mRequest.getDateTaken() + "]start saving");
            if (!writeToStorage(this.mRequest, uri)) {
                SavingTaskManager.this.notifyStoreComplete(MediaSavingResult.FAIL, Uri.EMPTY, this.mRequest);
                return;
            }
            if (CapturePerformanceLogger.get(this.mRequest) != null) {
                CapturePerformanceLogger.get(this.mRequest).writeFileDone = SystemClock.uptimeMillis();
            }
            if (this.mRequest.shouldUpdateOrientationBeforeStoring) {
                try {
                    File file = new File(uri.getPath());
                    JpegMetadata jpegMetadata = new JpegMetadata(file);
                    jpegMetadata.set(TiffTagConstants.TIFF_TAG_ORIENTATION, ExifOption.getExifOrientation(this.mRequest.common.orientation));
                    if (StorageUtil.getStorageTypeFromPath(uri.getPath(), SavingTaskManager.this.mContext) != Storage.StorageType.EXTERNAL_CARD) {
                        SavingTaskManager.rewriteInplace(jpegMetadata, file);
                    } else {
                        SavingTaskManager.this.rewriteInplaceForSdCard(jpegMetadata, file);
                    }
                } catch (IOException | ImageReadException | ImageWriteException e) {
                    CamLog.e("Failed to save exifOrientation. : " + e);
                }
            }
            CamLog.d("[datetaken:" + this.mRequest.getDateTaken() + "]store is success");
            if (!this.mRequest.common.addToMediaStore && this.mRequest.getExtraOutput() != null) {
                SavingTaskManager.this.notifyStoreComplete(MediaSavingResult.SUCCESS, this.mRequest.getExtraOutput(), this.mRequest);
            } else {
                try {
                    String filePath = this.mRequest.getFilePath();
                    if (PredictiveCapturePathBuilder.isPredictiveCaptureImage(filePath)) {
                        try {
                            date = new SimpleDateFormat("yyyyMMddHHmmssSSS").parse(this.mRequest.getSaveTimeForPredictiveCapture());
                        } catch (ParseException unused) {
                            CamLog.e("store: parse failed. filePath:" + filePath + " time:" + this.mRequest.getSaveTimeForPredictiveCapture());
                            date = null;
                        }
                        if (date != null && !new File(filePath).setLastModified(date.getTime())) {
                            CamLog.e("store: setLastModified failed. filePath:" + filePath + " time:" + date.getTime());
                        }
                    }
                    if (StorageUtil.getStorageTypeFromPath(uri.getPath(), SavingTaskManager.this.mContext) == Storage.StorageType.EXTERNAL_CARD) {
                        MediaProviderUpdator unused2 = SavingTaskManager.this.mUpdator;
                        Uri uriQueryPhotoFromDatabase = MediaProviderUpdator.queryPhotoFromDatabase(filePath, SavingTaskManager.this.mContext);
                        if (uriQueryPhotoFromDatabase == null) {
                            SavingTaskManager.this.mMediaScanController.requestScanFile(this.mRequest);
                        } else {
                            SavingTaskManager.this.notifyStoreComplete(MediaSavingResult.SUCCESS, uriQueryPhotoFromDatabase, this.mRequest);
                        }
                    } else {
                        SavingTaskManager.this.mMediaScanController.requestScanFile(this.mRequest);
                    }
                } catch (SQLiteFullException unused3) {
                    SavingTaskManager.this.notifyStoreComplete(MediaSavingResult.FAIL_MEMORY_FULL, Uri.EMPTY, this.mRequest);
                }
            }
            CamLog.d("store() X");
        }
    }

    private class ImageToFile {
        private ByteBuffer mBuffer;
        private byte[] mJpegData;
        ThreadSafeOutputStream mOutputStream = null;
        private final String mPath;
        private final Storage.StorageType mStorageType;
        private final Uri mUri;

        public ImageToFile(byte[] bArr, Uri uri) {
            this.mJpegData = bArr;
            this.mUri = uri;
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                this.mPath = this.mUri.getPath();
                this.mStorageType = StorageUtil.getStorageTypeFromPath(this.mPath, SavingTaskManager.this.mContext);
            } else {
                this.mPath = null;
                this.mStorageType = Storage.StorageType.UNKNOWN;
            }
        }

        public ImageToFile(ByteBuffer byteBuffer, Uri uri) {
            this.mBuffer = byteBuffer;
            this.mUri = uri;
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                this.mPath = this.mUri.getPath();
                this.mStorageType = StorageUtil.getStorageTypeFromPath(this.mPath, SavingTaskManager.this.mContext);
            } else {
                this.mPath = null;
                this.mStorageType = Storage.StorageType.UNKNOWN;
            }
        }

        public String getPath() {
            if (this.mPath != null) {
                return this.mPath;
            }
            if (this.mUri != null) {
                return this.mUri.getPath();
            }
            CamLog.e("Save path and uri is not set.");
            return null;
        }

        private OutputStream createOutputStream() throws FileNotFoundException {
            if (this.mStorageType == Storage.StorageType.EXTERNAL_CARD) {
                Uri uriCreateDocumentSdCard = StorageUtil.createDocumentSdCard(SavingTaskManager.this.mContext, this.mPath);
                if (uriCreateDocumentSdCard != null) {
                    return new FileOutputStream(SavingTaskManager.this.mContext.getContentResolver().openFileDescriptor(uriCreateDocumentSdCard, "rw").getFileDescriptor());
                }
                CamLog.e("Document uri is null.");
            } else {
                if (this.mPath != null) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Store create by path:" + this.mPath);
                    }
                    return new FileOutputStream(this.mPath);
                }
                if (this.mUri != null) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Store create by uri:" + this.mUri);
                    }
                    return SavingTaskManager.this.mContext.getContentResolver().openOutputStream(this.mUri);
                }
                CamLog.e("Save path and uri is not set.");
            }
            throw new FileNotFoundException();
        }

        public boolean storeData(Exception exc) {
            try {
                try {
                    try {
                        if (CamLog.VERBOSE) {
                            CamLog.d("Store create file:" + this.mPath);
                        }
                        OutputStream outputStreamCreateOutputStream = createOutputStream();
                        synchronized (this) {
                            this.mOutputStream = new ThreadSafeOutputStream(outputStreamCreateOutputStream);
                        }
                        if (exc != null) {
                            throw exc;
                        }
                        if (this.mBuffer == null) {
                            this.mOutputStream.write(this.mJpegData, 0, this.mJpegData.length);
                        } else if (this.mPath != null) {
                            ((FileOutputStream) outputStreamCreateOutputStream).getChannel().write(this.mBuffer);
                        } else {
                            int iCapacity = this.mBuffer.capacity();
                            this.mJpegData = new byte[iCapacity];
                            this.mBuffer.get(this.mJpegData, 0, iCapacity);
                            this.mOutputStream.write(this.mJpegData, 0, this.mJpegData.length);
                        }
                        if (this.mOutputStream != null) {
                            try {
                                this.mOutputStream.flush();
                                this.mOutputStream.close();
                            } catch (IOException unused) {
                                CamLog.e("IOException occured when closing.");
                                this.mOutputStream = null;
                            }
                        }
                        this.mJpegData = null;
                        return true;
                    } catch (FileNotFoundException e) {
                        CamLog.e("Store fail file not found:" + e.getMessage());
                        requestCheckStorage(this.mStorageType);
                        if (this.mOutputStream != null) {
                            try {
                                this.mOutputStream.flush();
                                this.mOutputStream.close();
                            } catch (IOException unused2) {
                                CamLog.e("IOException occured when closing.");
                                this.mOutputStream = null;
                            }
                        }
                        this.mJpegData = null;
                        return false;
                    } catch (Exception e2) {
                        CamLog.e("Occurred other exception:" + e2.toString());
                        requestCheckStorage(this.mStorageType);
                        if (this.mOutputStream != null) {
                            try {
                                this.mOutputStream.flush();
                                this.mOutputStream.close();
                            } catch (IOException unused3) {
                                CamLog.e("IOException occured when closing.");
                                this.mOutputStream = null;
                            }
                        }
                        this.mJpegData = null;
                        return false;
                    }
                } catch (IOException unused4) {
                    CamLog.e("Store fail I/O Exception:" + this.mPath);
                    requestCheckStorage(this.mStorageType);
                    if (this.mOutputStream != null) {
                        try {
                            this.mOutputStream.flush();
                            this.mOutputStream.close();
                        } catch (IOException unused5) {
                            CamLog.e("IOException occured when closing.");
                            this.mOutputStream = null;
                        }
                    }
                    this.mJpegData = null;
                    return false;
                }
            } catch (Throwable th) {
                if (this.mOutputStream != null) {
                    try {
                        this.mOutputStream.flush();
                        this.mOutputStream.close();
                    } catch (IOException unused6) {
                        CamLog.e("IOException occured when closing.");
                        this.mOutputStream = null;
                    }
                }
                this.mJpegData = null;
                throw th;
            }
        }

        private void requestCheckStorage(final Storage.StorageType storageType) {
            if (storageType != Storage.StorageType.UNKNOWN) {
                SavingTaskManager.this.mStorageManager.updateStorageState(storageType, CameraStorageManager.UpdateRequestReason.STORING_FAILED);
            }
            SavingTaskManager.this.runOnUiThread(new Runnable() { // from class: com.sonyericsson.cameracommon.storage.SavingTaskManager.ImageToFile.1
                @Override // java.lang.Runnable
                public void run() {
                    SavingTaskManager.this.mStorageManager.checkRemain(true, storageType);
                }
            });
        }
    }

    public SavingTaskManager(Context context, CameraStorageManager cameraStorageManager, Map<Storage.StorageType, Semaphore> map) {
        this.mContext = null;
        this.mStorageManager = null;
        this.mStoreDataHandler = null;
        this.mContext = context;
        this.mStorageAccessSemaphoreMap = map;
        this.mStorageManager = cameraStorageManager;
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        this.mStoreDataHandler = new Handler(handlerThread.getLooper());
        this.mUpdator = new MediaProviderUpdator(context);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mMediaScanController = new MediaScanController(this.mContext, this.mOnScanCompletedListener);
        this.mSavingTaskQueueMap = new HashMap();
        Iterator<Storage.StorageType> it = StorageUtil.getMountableStorageTypes().iterator();
        while (it.hasNext()) {
            this.mSavingTaskQueueMap.put(it.next(), new ConcurrentLinkedQueue());
        }
    }

    @AnyThread
    public void storePicture(final PhotoSavingRequest photoSavingRequest) {
        if (CamLog.VERBOSE) {
            CamLog.d("### storePicture() is called.");
        }
        if (CamLog.VERBOSE) {
            CamLog.d("PhotoSavingRequest: at storePicture.");
        }
        photoSavingRequest.log();
        if (photoSavingRequest.getImageData() != null || photoSavingRequest.isImageReaderUsing()) {
            pushPhotoSavingTask(photoSavingRequest);
            return;
        }
        CamLog.e("### can't store a specified image file.");
        CamLog.e("### so, notify a failure of storing the specified image file.");
        Iterator<PhotoSavingTask> it = this.mSavingTaskQueueMap.get(photoSavingRequest.getStorageType()).iterator();
        while (it.hasNext()) {
            it.next().cancel();
        }
        this.mStorageManager.requestVolumeCheck(photoSavingRequest.getStorageType(), CameraStorageManager.UpdateInterval.IMMEDIATE, CameraStorageManager.UpdateRequestReason.STORING_FAILED);
        this.mStorageManager.requestWriteCheck(photoSavingRequest.getStorageType(), CameraStorageManager.UpdateRequestReason.STORING_FAILED);
        runOnUiThread(new Runnable() { // from class: com.sonyericsson.cameracommon.storage.SavingTaskManager.2
            @Override // java.lang.Runnable
            public void run() {
                photoSavingRequest.close();
                photoSavingRequest.notifyStoreFailed(MediaSavingResult.FAIL);
                SavingTaskManager.this.mStorageManager.checkRemain(true, photoSavingRequest.getStorageType());
            }
        });
    }

    private void pushPhotoSavingTask(PhotoSavingRequest photoSavingRequest) {
        if (CamLog.VERBOSE) {
            CamLog.d("### pushPhotoSavingTask : " + photoSavingRequest.getRequestId());
        }
        photoSavingRequest.setOneShot(photoSavingRequest.isOneShot());
        PhotoSavingTask photoSavingTask = new PhotoSavingTask(photoSavingRequest);
        this.mSavingTaskQueueMap.get(photoSavingRequest.getStorageType()).add(photoSavingTask);
        mExecutor.execute(photoSavingTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void popPhotoSavingTask(PhotoSavingTask photoSavingTask) {
        if (CamLog.VERBOSE) {
            CamLog.d("### popPhotoSavingTask");
        }
        Iterator<Storage.StorageType> it = StorageUtil.getMountableStorageTypes().iterator();
        while (it.hasNext()) {
            this.mSavingTaskQueueMap.get(it.next()).remove(photoSavingTask);
        }
    }

    public SavingTaskInquiry getInquiry() {
        return this.mInquiry;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStoreComplete(MediaSavingResult mediaSavingResult, Uri uri, SavingRequest savingRequest) {
        if (CamLog.VERBOSE) {
            CamLog.d("onNotifyStoreComplete");
        }
        if (this.mStoreDataHandler != null) {
            if (!savingRequest.isOneShot() && mediaSavingResult == MediaSavingResult.SUCCESS) {
                MediaProviderUpdator.sendBroadcastCameraShot(this.mContext, uri);
            }
            this.mStoreDataHandler.post(new NotifyStoreCompletedTask(new StoreDataResult(mediaSavingResult, uri, savingRequest)));
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Maybe Main activity has gone. So cannot send complete message");
        }
    }

    private static class NotifyStoreCompletedTask implements Runnable {
        private final StoreDataResult mResult;

        private NotifyStoreCompletedTask(StoreDataResult storeDataResult) {
            this.mResult = storeDataResult;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mResult != null && this.mResult.savingRequest != null) {
                this.mResult.savingRequest.notifyStoreResult(this.mResult);
                if (CamLog.VERBOSE) {
                    CamLog.d(getClass().getName(), "mStatus.notifyStoreResult() is called.");
                    return;
                }
                return;
            }
            if (CamLog.VERBOSE) {
                CamLog.d(getClass().getName(), "StoreDataResult or SavingRequest is null.");
            }
        }
    }

    @AnyThread
    public void storeVideo(VideoSavingRequest videoSavingRequest) {
        if (CamLog.VERBOSE) {
            CamLog.d("VideoSavingRequest: at storeVideo.");
        }
        videoSavingRequest.log();
        if (CamLog.VERBOSE) {
            CamLog.d("storeVideo: " + videoSavingRequest.getFilePath());
        }
        if (this.mStoreVideoThread != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("Another thread has already started.");
            }
            try {
                try {
                    if (CamLog.VERBOSE) {
                        CamLog.d("wait for thread.");
                    }
                    this.mStoreVideoThread.join(MaxVideoSize.GUARANTEED_MIN_DURATION_IN_MILLIS);
                } catch (InterruptedException unused) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Interrupted.");
                    }
                }
                if (this.mStoreVideoThread != null) {
                    CamLog.e("storeVideo: mStoreVideoThread timeout.");
                    Storage.StorageType storageType = videoSavingRequest.getStorageType();
                    this.mStorageManager.requestVolumeCheck(storageType, CameraStorageManager.UpdateInterval.IMMEDIATE, CameraStorageManager.UpdateRequestReason.VIDEO_STORING_COMPLETED);
                    this.mStorageManager.requestWriteCheck(storageType, CameraStorageManager.UpdateRequestReason.VIDEO_STORING_COMPLETED);
                    this.mStorageManager.checkRemain(true, storageType);
                    return;
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("wait end.");
                }
            } finally {
                this.mStoreVideoThread = null;
            }
        }
        this.mStoreVideoThread = new Thread(new SavingVideoTask(videoSavingRequest), MediaSavingConstants.THREAD_STORE_VIDEO);
        this.mStoreVideoThread.setPriority(1);
        this.mStoreVideoThread.start();
    }

    class SavingVideoTask implements Runnable {
        final VideoSavingRequest mRequest;

        SavingVideoTask(VideoSavingRequest videoSavingRequest) {
            this.mRequest = videoSavingRequest;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00dc  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x010c  */
        /* JADX WARN: Type inference failed for: r0v13 */
        /* JADX WARN: Type inference failed for: r0v15 */
        /* JADX WARN: Type inference failed for: r0v16 */
        /* JADX WARN: Type inference failed for: r0v3, types: [com.sonyericsson.cameracommon.mediasaving.MediaSavingResult] */
        /* JADX WARN: Type inference failed for: r0v4, types: [com.sonyericsson.cameracommon.mediasaving.MediaSavingResult] */
        /* JADX WARN: Type inference failed for: r0v7, types: [android.net.Uri] */
        /* JADX WARN: Type inference failed for: r0v8 */
        /* JADX WARN: Type inference failed for: r0v9 */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() throws java.lang.InterruptedException {
            /*
                Method dump skipped, instructions count: 320
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.storage.SavingTaskManager.SavingVideoTask.run():void");
        }
    }

    public boolean canPushStoreTask(Storage.StorageType storageType) throws IOException {
        if (isEnoughMemory(getUsedMemory())) {
            return true;
        }
        Runtime.getRuntime().gc();
        if (isEnoughMemory(getUsedMemory())) {
            return true;
        }
        CamLog.e("Temporarily reject capture request since app is low on memory:");
        CamLog.e("\t saving: " + getRemainQueueSize(storageType));
        CamLog.e("\tusedMemory: " + getUsedMemory() + "byte");
        CamLog.e("\t maxMemory: " + Runtime.getRuntime().maxMemory() + "byte");
        if (CamLog.DEBUG && getRemainQueueSize(storageType) == 0) {
            try {
                Debug.dumpHprofData(Environment.getExternalStorageDirectory() + CAN_PUSH_STORE_TASK_HPROF_FILE_NAME);
            } catch (IOException unused) {
                CamLog.e("canPushStoreTask() fail to dump hprof");
            }
        }
        return false;
    }

    private static long getUsedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    private static boolean isEnoughMemory(long j) {
        return j < (Runtime.getRuntime().maxMemory() * 7) / 10;
    }

    public void release() {
        if (this.mStoreDataHandler != null && this.mStoreDataHandler.getLooper().getThread() != Looper.getMainLooper().getThread()) {
            this.mStoreDataHandler.getLooper().quitSafely();
        }
        this.mStoreDataHandler = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void rewriteInplace(JpegMetadata jpegMetadata, File file) throws Throwable {
        boolean zRenameTo;
        long jLastModified = file.lastModified();
        File fileCreateTempFile = File.createTempFile(file.getName(), SUFFIX_TEMP_FILE, file.getParentFile());
        FileOutputStream fileOutputStream = new FileOutputStream(fileCreateTempFile);
        try {
            try {
                jpegMetadata.rewrite(file, (OutputStream) fileOutputStream, true);
                fileOutputStream.flush();
                fileOutputStream.close();
                zRenameTo = fileCreateTempFile.renameTo(file);
                try {
                    if (zRenameTo) {
                        if (!file.setLastModified(jLastModified)) {
                            CamLog.w("Failed to set last modified time to " + file.getAbsolutePath());
                        }
                        if (zRenameTo || fileCreateTempFile.delete()) {
                            return;
                        }
                        CamLog.w("Could not delete temporary file " + fileCreateTempFile.getAbsolutePath());
                        return;
                    }
                    throw new IOException("Could not replace file " + file);
                } catch (Throwable th) {
                    th = th;
                    if (!zRenameTo && !fileCreateTempFile.delete()) {
                        CamLog.w("Could not delete temporary file " + fileCreateTempFile.getAbsolutePath());
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                fileOutputStream.close();
                throw th2;
            }
        } catch (Throwable th3) {
            th = th3;
            zRenameTo = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rewriteInplaceForSdCard(JpegMetadata jpegMetadata, File file) throws ImageWriteException, IOException, ImageReadException {
        long jLastModified = file.lastModified();
        Uri uriSearchDocumentSdCard = StorageUtil.searchDocumentSdCard(this.mContext, file.getPath());
        String name = file.getName();
        Uri uriCreateDocumentSdCard = StorageUtil.createDocumentSdCard(this.mContext, file.getPath() + SUFFIX_TEMP_FILE);
        if (uriCreateDocumentSdCard != null) {
            FileOutputStream fileOutputStream = new FileOutputStream(this.mContext.getContentResolver().openFileDescriptor(uriCreateDocumentSdCard, "rw").getFileDescriptor());
            try {
                jpegMetadata.rewrite(file, (OutputStream) fileOutputStream, true);
                fileOutputStream.flush();
                fileOutputStream.close();
                if (!DocumentsContract.deleteDocument(this.mContext.getContentResolver(), uriSearchDocumentSdCard)) {
                    CamLog.w("Could not delete old file " + uriSearchDocumentSdCard.toString());
                }
                if (DocumentsContract.renameDocument(this.mContext.getContentResolver(), uriCreateDocumentSdCard, name) != null) {
                    if (file.setLastModified(jLastModified)) {
                        return;
                    }
                    CamLog.w("Failed to set last modified time to " + file.getAbsolutePath());
                    return;
                }
                throw new IOException("Could not replace file " + file);
            } catch (Throwable th) {
                fileOutputStream.close();
                throw th;
            }
        }
        throw new IOException("Could not create temp file");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOnUiThread(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public int getRemainQueueSize(Storage.StorageType storageType) {
        return this.mSavingTaskQueueMap.get(storageType).size();
    }

    private static class MediaScanController {
        private final Context mContext;
        private final OnScanCompletedListener mOnScanCompletedListener;
        private final Object mScanFileLock = new Object();
        private final ArrayList<PhotoSavingRequest> mPendingScanFileList = new ArrayList<>();
        private boolean mIsScanning = false;

        public interface OnScanCompletedListener {
            void onScanCompleted(MediaSavingResult mediaSavingResult, Uri uri, PhotoSavingRequest photoSavingRequest);
        }

        public MediaScanController(Context context, OnScanCompletedListener onScanCompletedListener) {
            this.mContext = context;
            this.mOnScanCompletedListener = onScanCompletedListener;
        }

        public void requestScanFile(PhotoSavingRequest photoSavingRequest) {
            synchronized (this.mScanFileLock) {
                this.mPendingScanFileList.add(photoSavingRequest);
                if (this.mIsScanning) {
                    CamLog.d("pending:" + this.mPendingScanFileList.size());
                } else {
                    scanAllPendingFilesLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void scanAllPendingFilesLocked() {
            if (this.mPendingScanFileList.isEmpty()) {
                this.mIsScanning = false;
                return;
            }
            this.mIsScanning = true;
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(this.mPendingScanFileList);
            this.mPendingScanFileList.clear();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                PhotoSavingRequest photoSavingRequest = (PhotoSavingRequest) it.next();
                if (CapturePerformanceLogger.get(photoSavingRequest) != null) {
                    CapturePerformanceLogger.get(photoSavingRequest).startScan = SystemClock.uptimeMillis();
                }
            }
            String[] strArr = new String[arrayList.size()];
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = ((PhotoSavingRequest) arrayList.get(i)).getFilePath();
            }
            MediaScannerConnection.scanFile(this.mContext, strArr, null, new OnMediaScanCompletedListener(arrayList));
            CamLog.d("request:" + strArr.length);
        }

        private class OnMediaScanCompletedListener implements MediaScannerConnection.OnScanCompletedListener {
            private final List<PhotoSavingRequest> mRequests;

            public OnMediaScanCompletedListener(List<PhotoSavingRequest> list) {
                this.mRequests = list;
            }

            private PhotoSavingRequest pop(String str) {
                PhotoSavingRequest next;
                Iterator<PhotoSavingRequest> it = this.mRequests.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        next = null;
                        break;
                    }
                    next = it.next();
                    if (next.getFilePath().equals(str)) {
                        break;
                    }
                }
                if (next != null) {
                    this.mRequests.remove(next);
                }
                return next;
            }

            @Override // android.media.MediaScannerConnection.OnScanCompletedListener
            public void onScanCompleted(String str, Uri uri) {
                PhotoSavingRequest photoSavingRequestPop = pop(str);
                if (photoSavingRequestPop == null) {
                    CamLog.d("onScanCompleted() request of scan completed file is not found.");
                    return;
                }
                if (CapturePerformanceLogger.get(photoSavingRequestPop) != null) {
                    CapturePerformanceLogger.get(photoSavingRequestPop).scanFileDone = SystemClock.uptimeMillis();
                    CapturePerformanceLogger.complete(photoSavingRequestPop);
                } else {
                    CamLog.d("CapturePerformance: get(request) is null. path = " + photoSavingRequestPop.getFilePath());
                }
                MediaScanController.this.mOnScanCompletedListener.onScanCompleted(MediaSavingResult.SUCCESS, uri, photoSavingRequestPop);
                CamLog.d("remain:" + this.mRequests.size() + ", pending:" + MediaScanController.this.mPendingScanFileList.size());
                synchronized (MediaScanController.this.mScanFileLock) {
                    if (this.mRequests.isEmpty()) {
                        MediaScanController.this.scanAllPendingFilesLocked();
                    }
                }
            }
        }
    }
}
