// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import java.util.Date;
import android.database.sqlite.SQLiteFullException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.sonyericsson.android.camera.mediasaving.ExifOption;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import android.media.MediaScannerConnection$OnScanCompletedListener;
import android.media.MediaScannerConnection;
import java.util.List;
import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CapturePerformanceLogger;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import com.sonyericsson.cameracommon.mediasaving.ThreadSafeOutputStream;
import java.nio.ByteBuffer;
import android.support.annotation.AnyThread;
import android.os.Debug;
import android.os.Environment;
import android.provider.DocumentsContract;
import java.io.OutputStream;
import java.io.FileOutputStream;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.android.camera.util.CamLog;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImageReadException;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.HashMap;
import android.os.Looper;
import android.os.HandlerThread;
import android.net.Uri;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collection;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.cameracommon.mediasaving.updator.MediaProviderUpdator;
import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.Map;
import android.os.Handler;
import android.content.Context;
import java.util.concurrent.ExecutorService;

public class SavingTaskManager
{
    private static final String CAN_PUSH_STORE_TASK_HPROF_FILE_NAME = "/can_push_store_task.hprof";
    private static final String SUFFIX_TEMP_FILE = ".rewrite-exif";
    public static final String TAG = "SavingTaskManager";
    private static final String THREAD_NAME = "SavingTask";
    private static final ExecutorService mExecutor;
    Context mContext;
    private Handler mHandler;
    private SavingTaskInquiry mInquiry;
    private final MediaScanController mMediaScanController;
    private final OnScanCompletedListener mOnScanCompletedListener;
    private final Map<Storage.StorageType, Queue<PhotoSavingTask>> mSavingTaskQueueMap;
    private Map<Storage.StorageType, Semaphore> mStorageAccessSemaphoreMap;
    private CameraStorageManager mStorageManager;
    private Handler mStoreDataHandler;
    private Thread mStoreVideoThread;
    private MediaProviderUpdator mUpdator;
    
    static {
        mExecutor = ThreadUtil.buildExecutor("SavingTask");
    }
    
    public SavingTaskManager(final Context mContext, final CameraStorageManager mStorageManager, final Map<Storage.StorageType, Semaphore> mStorageAccessSemaphoreMap) {
        this.mContext = null;
        this.mStorageManager = null;
        this.mStoreVideoThread = null;
        this.mInquiry = new SavingTaskInquiry() {
            final SavingTaskManager this$0;
            
            @Override
            public long getReservedSize(final Storage.StorageType storageType) {
                final boolean containsKey = this.this$0.mSavingTaskQueueMap.containsKey(storageType);
                long n2;
                long n = n2 = 0L;
                if (containsKey) {
                    final Iterator iterator = new LinkedList(this.this$0.mSavingTaskQueueMap.get(storageType)).iterator();
                    while (true) {
                        n2 = n;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        n += ((PhotoSavingTask)iterator.next()).getExpectedFileSize();
                    }
                }
                return n2;
            }
        };
        this.mStoreDataHandler = null;
        this.mOnScanCompletedListener = (OnScanCompletedListener)new OnScanCompletedListener() {
            final SavingTaskManager this$0;
            
            @Override
            public void onScanCompleted(final MediaSavingResult mediaSavingResult, final Uri uri, final PhotoSavingRequest photoSavingRequest) {
                this.this$0.notifyStoreComplete(MediaSavingResult.SUCCESS, uri, photoSavingRequest);
            }
        };
        this.mContext = mContext;
        this.mStorageAccessSemaphoreMap = mStorageAccessSemaphoreMap;
        this.mStorageManager = mStorageManager;
        final HandlerThread handlerThread = new HandlerThread("SavingTaskManager");
        handlerThread.start();
        this.mStoreDataHandler = new Handler(handlerThread.getLooper());
        this.mUpdator = new MediaProviderUpdator(mContext);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mMediaScanController = new MediaScanController(this.mContext, this.mOnScanCompletedListener);
        this.mSavingTaskQueueMap = new HashMap<Storage.StorageType, Queue<PhotoSavingTask>>();
        final Iterator<Storage.StorageType> iterator = StorageUtil.getMountableStorageTypes().iterator();
        while (iterator.hasNext()) {
            this.mSavingTaskQueueMap.put(iterator.next(), new ConcurrentLinkedQueue<PhotoSavingTask>());
        }
    }
    
    private static long getUsedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
    
    private static boolean isEnoughMemory(final long n) {
        return n < Runtime.getRuntime().maxMemory() * 7L / 10L;
    }
    
    private void notifyStoreComplete(final MediaSavingResult mediaSavingResult, final Uri uri, final SavingRequest savingRequest) {
        if (CamLog.VERBOSE) {
            CamLog.d("onNotifyStoreComplete");
        }
        if (this.mStoreDataHandler != null) {
            if (!savingRequest.isOneShot() && mediaSavingResult == MediaSavingResult.SUCCESS) {
                MediaProviderUpdator.sendBroadcastCameraShot(this.mContext, uri);
            }
            this.mStoreDataHandler.post((Runnable)new NotifyStoreCompletedTask(new StoreDataResult(mediaSavingResult, uri, savingRequest)));
        }
        else if (CamLog.VERBOSE) {
            CamLog.d("Maybe Main activity has gone. So cannot send complete message");
        }
    }
    
    private void popPhotoSavingTask(final PhotoSavingTask photoSavingTask) {
        if (CamLog.VERBOSE) {
            CamLog.d("### popPhotoSavingTask");
        }
        final Iterator<Storage.StorageType> iterator = StorageUtil.getMountableStorageTypes().iterator();
        while (iterator.hasNext()) {
            this.mSavingTaskQueueMap.get(iterator.next()).remove(photoSavingTask);
        }
    }
    
    private void pushPhotoSavingTask(final PhotoSavingRequest photoSavingRequest) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("### pushPhotoSavingTask : ");
            sb.append(photoSavingRequest.getRequestId());
            CamLog.d(sb.toString());
        }
        photoSavingRequest.setOneShot(photoSavingRequest.isOneShot());
        final PhotoSavingTask photoSavingTask = new PhotoSavingTask(photoSavingRequest);
        this.mSavingTaskQueueMap.get(photoSavingRequest.getStorageType()).add(photoSavingTask);
        SavingTaskManager.mExecutor.execute(photoSavingTask);
    }
    
    private static void rewriteInplace(final JpegMetadata p0, final File p1) throws IOException, ImageReadException, ImageWriteException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/io/File.lastModified:()J
        //     4: lstore_3       
        //     5: aload_1        
        //     6: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //     9: ldc             ".rewrite-exif"
        //    11: aload_1        
        //    12: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //    15: invokestatic    java/io/File.createTempFile:(Ljava/lang/String;Ljava/lang/String;Ljava/io/File;)Ljava/io/File;
        //    18: astore          5
        //    20: new             Ljava/io/FileOutputStream;
        //    23: dup            
        //    24: aload           5
        //    26: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    29: astore          6
        //    31: aload_0        
        //    32: aload_1        
        //    33: aload           6
        //    35: iconst_1       
        //    36: invokevirtual   com/sonyericsson/cameracommon/storage/JpegMetadata.rewrite:(Ljava/io/File;Ljava/io/OutputStream;Z)V
        //    39: aload           6
        //    41: invokevirtual   java/io/FileOutputStream.flush:()V
        //    44: aload           6
        //    46: invokevirtual   java/io/FileOutputStream.close:()V
        //    49: aload           5
        //    51: aload_1        
        //    52: invokevirtual   java/io/File.renameTo:(Ljava/io/File;)Z
        //    55: istore_2       
        //    56: iload_2        
        //    57: ifeq            164
        //    60: aload_1        
        //    61: lload_3        
        //    62: invokevirtual   java/io/File.setLastModified:(J)Z
        //    65: ifne            107
        //    68: new             Ljava/lang/StringBuilder;
        //    71: astore_0       
        //    72: aload_0        
        //    73: invokespecial   java/lang/StringBuilder.<init>:()V
        //    76: aload_0        
        //    77: ldc_w           "Failed to set last modified time to "
        //    80: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    83: pop            
        //    84: aload_0        
        //    85: aload_1        
        //    86: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    89: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    92: pop            
        //    93: iconst_1       
        //    94: anewarray       Ljava/lang/String;
        //    97: dup            
        //    98: iconst_0       
        //    99: aload_0        
        //   100: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   103: aastore        
        //   104: invokestatic    com/sonyericsson/android/camera/util/CamLog.w:([Ljava/lang/String;)V
        //   107: iload_2        
        //   108: ifne            159
        //   111: aload           5
        //   113: invokevirtual   java/io/File.delete:()Z
        //   116: ifne            159
        //   119: new             Ljava/lang/StringBuilder;
        //   122: dup            
        //   123: invokespecial   java/lang/StringBuilder.<init>:()V
        //   126: astore_0       
        //   127: aload_0        
        //   128: ldc_w           "Could not delete temporary file "
        //   131: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   134: pop            
        //   135: aload_0        
        //   136: aload           5
        //   138: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   141: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   144: pop            
        //   145: iconst_1       
        //   146: anewarray       Ljava/lang/String;
        //   149: dup            
        //   150: iconst_0       
        //   151: aload_0        
        //   152: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   155: aastore        
        //   156: invokestatic    com/sonyericsson/android/camera/util/CamLog.w:([Ljava/lang/String;)V
        //   159: return         
        //   160: astore_0       
        //   161: goto            216
        //   164: new             Ljava/io/IOException;
        //   167: astore_0       
        //   168: new             Ljava/lang/StringBuilder;
        //   171: astore          6
        //   173: aload           6
        //   175: invokespecial   java/lang/StringBuilder.<init>:()V
        //   178: aload           6
        //   180: ldc_w           "Could not replace file "
        //   183: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   186: pop            
        //   187: aload           6
        //   189: aload_1        
        //   190: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   193: pop            
        //   194: aload_0        
        //   195: aload           6
        //   197: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   200: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //   203: aload_0        
        //   204: athrow         
        //   205: astore_0       
        //   206: aload           6
        //   208: invokevirtual   java/io/FileOutputStream.close:()V
        //   211: aload_0        
        //   212: athrow         
        //   213: astore_0       
        //   214: iconst_0       
        //   215: istore_2       
        //   216: iload_2        
        //   217: ifne            268
        //   220: aload           5
        //   222: invokevirtual   java/io/File.delete:()Z
        //   225: ifne            268
        //   228: new             Ljava/lang/StringBuilder;
        //   231: dup            
        //   232: invokespecial   java/lang/StringBuilder.<init>:()V
        //   235: astore_1       
        //   236: aload_1        
        //   237: ldc_w           "Could not delete temporary file "
        //   240: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   243: pop            
        //   244: aload_1        
        //   245: aload           5
        //   247: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   250: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   253: pop            
        //   254: iconst_1       
        //   255: anewarray       Ljava/lang/String;
        //   258: dup            
        //   259: iconst_0       
        //   260: aload_1        
        //   261: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   264: aastore        
        //   265: invokestatic    com/sonyericsson/android/camera/util/CamLog.w:([Ljava/lang/String;)V
        //   268: aload_0        
        //   269: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //  throws org.apache.commons.imaging.ImageReadException
        //  throws org.apache.commons.imaging.ImageWriteException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  31     44     205    213    Any
        //  44     56     213    216    Any
        //  60     107    160    164    Any
        //  164    205    160    164    Any
        //  206    213    213    216    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0107:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2604)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void rewriteInplaceForSdCard(final JpegMetadata jpegMetadata, final File obj) throws IOException, ImageReadException, ImageWriteException {
        final long lastModified = obj.lastModified();
        final Uri searchDocumentSdCard = StorageUtil.searchDocumentSdCard(this.mContext, obj.getPath());
        final String name = obj.getName();
        final Context mContext = this.mContext;
        final StringBuilder sb = new StringBuilder();
        sb.append(obj.getPath());
        sb.append(".rewrite-exif");
        final Uri documentSdCard = StorageUtil.createDocumentSdCard(mContext, sb.toString());
        if (documentSdCard != null) {
            final FileOutputStream fileOutputStream = new FileOutputStream(this.mContext.getContentResolver().openFileDescriptor(documentSdCard, "rw").getFileDescriptor());
            try {
                jpegMetadata.rewrite(obj, fileOutputStream, true);
                fileOutputStream.flush();
                fileOutputStream.close();
                if (!DocumentsContract.deleteDocument(this.mContext.getContentResolver(), searchDocumentSdCard)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not delete old file ");
                    sb2.append(searchDocumentSdCard.toString());
                    CamLog.w(sb2.toString());
                }
                if (DocumentsContract.renameDocument(this.mContext.getContentResolver(), documentSdCard, name) != null) {
                    if (!obj.setLastModified(lastModified)) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Failed to set last modified time to ");
                        sb3.append(obj.getAbsolutePath());
                        CamLog.w(sb3.toString());
                    }
                    return;
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Could not replace file ");
                sb4.append(obj);
                throw new IOException(sb4.toString());
            }
            finally {
                fileOutputStream.close();
            }
        }
        throw new IOException("Could not create temp file");
    }
    
    private void runOnUiThread(final Runnable runnable) {
        this.mHandler.post(runnable);
    }
    
    public boolean canPushStoreTask(final Storage.StorageType storageType) {
        if (isEnoughMemory(getUsedMemory())) {
            return true;
        }
        Runtime.getRuntime().gc();
        if (isEnoughMemory(getUsedMemory())) {
            return true;
        }
        CamLog.e("Temporarily reject capture request since app is low on memory:");
        final StringBuilder sb = new StringBuilder();
        sb.append("\t saving: ");
        sb.append(this.getRemainQueueSize(storageType));
        CamLog.e(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("\tusedMemory: ");
        sb2.append(getUsedMemory());
        sb2.append("byte");
        CamLog.e(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("\t maxMemory: ");
        sb3.append(Runtime.getRuntime().maxMemory());
        sb3.append("byte");
        CamLog.e(sb3.toString());
        if (CamLog.DEBUG && this.getRemainQueueSize(storageType) == 0) {
            try {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(Environment.getExternalStorageDirectory());
                sb4.append("/can_push_store_task.hprof");
                Debug.dumpHprofData(sb4.toString());
            }
            catch (final IOException ex) {
                CamLog.e("canPushStoreTask() fail to dump hprof");
            }
        }
        return false;
    }
    
    public SavingTaskInquiry getInquiry() {
        return this.mInquiry;
    }
    
    public int getRemainQueueSize(final Storage.StorageType storageType) {
        return this.mSavingTaskQueueMap.get(storageType).size();
    }
    
    public void release() {
        if (this.mStoreDataHandler != null && this.mStoreDataHandler.getLooper().getThread() != Looper.getMainLooper().getThread()) {
            this.mStoreDataHandler.getLooper().quitSafely();
        }
        this.mStoreDataHandler = null;
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
        if (photoSavingRequest.getImageData() == null && !photoSavingRequest.isImageReaderUsing()) {
            CamLog.e("### can't store a specified image file.");
            CamLog.e("### so, notify a failure of storing the specified image file.");
            final Iterator iterator = this.mSavingTaskQueueMap.get(photoSavingRequest.getStorageType()).iterator();
            while (iterator.hasNext()) {
                ((PhotoSavingTask)iterator.next()).cancel();
            }
            this.mStorageManager.requestVolumeCheck(photoSavingRequest.getStorageType(), CameraStorageManager.UpdateInterval.IMMEDIATE, CameraStorageManager.UpdateRequestReason.STORING_FAILED);
            this.mStorageManager.requestWriteCheck(photoSavingRequest.getStorageType(), CameraStorageManager.UpdateRequestReason.STORING_FAILED);
            this.runOnUiThread(new Runnable(this, photoSavingRequest) {
                final SavingTaskManager this$0;
                final PhotoSavingRequest val$request;
                
                @Override
                public void run() {
                    this.val$request.close();
                    this.val$request.notifyStoreFailed(MediaSavingResult.FAIL);
                    this.this$0.mStorageManager.checkRemain(true, this.val$request.getStorageType());
                }
            });
        }
        else {
            this.pushPhotoSavingTask(photoSavingRequest);
        }
    }
    
    @AnyThread
    public void storeVideo(final VideoSavingRequest videoSavingRequest) {
        if (CamLog.VERBOSE) {
            CamLog.d("VideoSavingRequest: at storeVideo.");
        }
        videoSavingRequest.log();
        if (CamLog.VERBOSE) {
            final String filePath = videoSavingRequest.getFilePath();
            final StringBuilder sb = new StringBuilder();
            sb.append("storeVideo: ");
            sb.append(filePath);
            CamLog.d(sb.toString());
        }
        Label_0252: {
            if (this.mStoreVideoThread != null) {
                Label_0096: {
                    if (!CamLog.VERBOSE) {
                        break Label_0096;
                    }
                    CamLog.d("Another thread has already started.");
                    try {
                        try {
                            if (CamLog.VERBOSE) {
                                CamLog.d("wait for thread.");
                            }
                            this.mStoreVideoThread.join(3000L);
                            if (this.mStoreVideoThread != null) {
                                CamLog.e("storeVideo: mStoreVideoThread timeout.");
                                final Storage.StorageType storageType = videoSavingRequest.getStorageType();
                                this.mStorageManager.requestVolumeCheck(storageType, CameraStorageManager.UpdateInterval.IMMEDIATE, CameraStorageManager.UpdateRequestReason.VIDEO_STORING_COMPLETED);
                                this.mStorageManager.requestWriteCheck(storageType, CameraStorageManager.UpdateRequestReason.VIDEO_STORING_COMPLETED);
                                this.mStorageManager.checkRemain(true, storageType);
                                this.mStoreVideoThread = null;
                                return;
                            }
                            if (CamLog.VERBOSE) {
                                CamLog.d("wait end.");
                            }
                        }
                        finally {}
                    }
                    catch (final InterruptedException ex) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("Interrupted.");
                        }
                    }
                }
                this.mStoreVideoThread = null;
                break Label_0252;
                this.mStoreVideoThread = null;
            }
        }
        (this.mStoreVideoThread = new Thread(new SavingVideoTask(videoSavingRequest), "Store video thread")).setPriority(1);
        this.mStoreVideoThread.start();
    }
    
    public enum GeoMode
    {
        private static final GeoMode[] $VALUES;
        
        GEO_OFF, 
        GEO_ON, 
        GEO_RESHOW;
        
        static {
            $VALUES = new GeoMode[] { GeoMode.GEO_ON, GeoMode.GEO_OFF, GeoMode.GEO_RESHOW };
        }
    }
    
    private class ImageToFile
    {
        private ByteBuffer mBuffer;
        private byte[] mJpegData;
        ThreadSafeOutputStream mOutputStream;
        private final String mPath;
        private final Storage.StorageType mStorageType;
        private final Uri mUri;
        final SavingTaskManager this$0;
        
        public ImageToFile(final SavingTaskManager this$0, final ByteBuffer mBuffer, final Uri mUri) {
            this.this$0 = this$0;
            this.mOutputStream = null;
            this.mBuffer = mBuffer;
            this.mUri = mUri;
            if ("file".equalsIgnoreCase(mUri.getScheme())) {
                this.mPath = this.mUri.getPath();
                this.mStorageType = StorageUtil.getStorageTypeFromPath(this.mPath, this$0.mContext);
            }
            else {
                this.mPath = null;
                this.mStorageType = Storage.StorageType.UNKNOWN;
            }
        }
        
        public ImageToFile(final SavingTaskManager this$0, final byte[] mJpegData, final Uri mUri) {
            this.this$0 = this$0;
            this.mOutputStream = null;
            this.mJpegData = mJpegData;
            this.mUri = mUri;
            if ("file".equalsIgnoreCase(mUri.getScheme())) {
                this.mPath = this.mUri.getPath();
                this.mStorageType = StorageUtil.getStorageTypeFromPath(this.mPath, this$0.mContext);
            }
            else {
                this.mPath = null;
                this.mStorageType = Storage.StorageType.UNKNOWN;
            }
        }
        
        private OutputStream createOutputStream() throws FileNotFoundException {
            if (this.mStorageType == Storage.StorageType.EXTERNAL_CARD) {
                final Uri documentSdCard = StorageUtil.createDocumentSdCard(this.this$0.mContext, this.mPath);
                if (documentSdCard != null) {
                    return new FileOutputStream(this.this$0.mContext.getContentResolver().openFileDescriptor(documentSdCard, "rw").getFileDescriptor());
                }
                CamLog.e("Document uri is null.");
            }
            else {
                if (this.mPath != null) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Store create by path:");
                        sb.append(this.mPath);
                        CamLog.d(sb.toString());
                    }
                    return new FileOutputStream(this.mPath);
                }
                if (this.mUri != null) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Store create by uri:");
                        sb2.append(this.mUri);
                        CamLog.d(sb2.toString());
                    }
                    return this.this$0.mContext.getContentResolver().openOutputStream(this.mUri);
                }
                CamLog.e("Save path and uri is not set.");
            }
            throw new FileNotFoundException();
        }
        
        private void requestCheckStorage(final Storage.StorageType storageType) {
            if (storageType != Storage.StorageType.UNKNOWN) {
                this.this$0.mStorageManager.updateStorageState(storageType, CameraStorageManager.UpdateRequestReason.STORING_FAILED);
            }
            this.this$0.runOnUiThread(new Runnable(this, storageType) {
                final ImageToFile this$1;
                final Storage.StorageType val$type;
                
                @Override
                public void run() {
                    this.this$1.this$0.mStorageManager.checkRemain(true, this.val$type);
                }
            });
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
        
        public boolean storeData(final Exception ex) {
            try {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Store create file:");
                    sb.append(this.mPath);
                    CamLog.d(sb.toString());
                }
                final OutputStream outputStream = this.createOutputStream();
                synchronized (this) {
                    this.mOutputStream = new ThreadSafeOutputStream(outputStream);
                    monitorexit(this);
                    if (ex != null) {
                        throw ex;
                    }
                    if (this.mBuffer == null) {
                        this.mOutputStream.write(this.mJpegData, 0, this.mJpegData.length);
                    }
                    else if (this.mPath != null) {
                        ((FileOutputStream)outputStream).getChannel().write(this.mBuffer);
                    }
                    else {
                        final int capacity = this.mBuffer.capacity();
                        this.mJpegData = new byte[capacity];
                        this.mBuffer.get(this.mJpegData, 0, capacity);
                        this.mOutputStream.write(this.mJpegData, 0, this.mJpegData.length);
                    }
                    if (this.mOutputStream != null) {
                        try {
                            this.mOutputStream.flush();
                            this.mOutputStream.close();
                        }
                        catch (final IOException ex2) {
                            CamLog.e("IOException occured when closing.");
                            this.mOutputStream = null;
                        }
                    }
                    this.mJpegData = null;
                    return true;
                }
            }
            catch (final Exception ex3) {}
            catch (final IOException ex4) {}
            catch (final FileNotFoundException ex5) {}
            finally {
                if (this.mOutputStream != null) {
                    try {
                        this.mOutputStream.flush();
                        this.mOutputStream.close();
                    }
                    catch (final IOException ex6) {
                        CamLog.e("IOException occured when closing.");
                        this.mOutputStream = null;
                    }
                }
                this.mJpegData = null;
                while (true) {
                    this.mJpegData = null;
                    return false;
                    try {
                        this.mOutputStream.flush();
                        this.mOutputStream.close();
                    }
                    catch (final IOException ex7) {
                        CamLog.e("IOException occured when closing.");
                        this.mOutputStream = null;
                    }
                    continue;
                }
                while (true) {
                    while (true) {
                        this.mJpegData = null;
                        return false;
                        this.mJpegData = null;
                        return false;
                        try {
                            this.mOutputStream.flush();
                            this.mOutputStream.close();
                        }
                        catch (final IOException ex8) {
                            CamLog.e("IOException occured when closing.");
                            this.mOutputStream = null;
                        }
                        continue;
                    }
                    try {
                        this.mOutputStream.flush();
                        this.mOutputStream.close();
                    }
                    catch (final IOException ex9) {
                        CamLog.e("IOException occured when closing.");
                        this.mOutputStream = null;
                    }
                    continue;
                }
            }
        }
    }
    
    private static class MediaScanController
    {
        private final Context mContext;
        private boolean mIsScanning;
        private final OnScanCompletedListener mOnScanCompletedListener;
        private final ArrayList<PhotoSavingRequest> mPendingScanFileList;
        private final Object mScanFileLock;
        
        public MediaScanController(final Context mContext, final OnScanCompletedListener mOnScanCompletedListener) {
            this.mScanFileLock = new Object();
            this.mPendingScanFileList = new ArrayList<PhotoSavingRequest>();
            this.mIsScanning = false;
            this.mContext = mContext;
            this.mOnScanCompletedListener = mOnScanCompletedListener;
        }
        
        private void scanAllPendingFilesLocked() {
            if (this.mPendingScanFileList.isEmpty()) {
                this.mIsScanning = false;
                return;
            }
            this.mIsScanning = true;
            final ArrayList list = new ArrayList();
            list.addAll(this.mPendingScanFileList);
            this.mPendingScanFileList.clear();
            for (final PhotoSavingRequest photoSavingRequest : list) {
                if (CapturePerformanceLogger.get(photoSavingRequest) != null) {
                    CapturePerformanceLogger.get(photoSavingRequest).startScan = SystemClock.uptimeMillis();
                }
            }
            final String[] array = new String[list.size()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = ((PhotoSavingRequest)list.get(i)).getFilePath();
            }
            MediaScannerConnection.scanFile(this.mContext, array, (String[])null, (MediaScannerConnection$OnScanCompletedListener)new OnMediaScanCompletedListener(list));
            final StringBuilder sb = new StringBuilder();
            sb.append("request:");
            sb.append(array.length);
            CamLog.d(sb.toString());
        }
        
        public void requestScanFile(final PhotoSavingRequest e) {
            synchronized (this.mScanFileLock) {
                this.mPendingScanFileList.add(e);
                if (this.mIsScanning) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("pending:");
                    sb.append(this.mPendingScanFileList.size());
                    CamLog.d(sb.toString());
                }
                else {
                    this.scanAllPendingFilesLocked();
                }
            }
        }
        
        private class OnMediaScanCompletedListener implements MediaScannerConnection$OnScanCompletedListener
        {
            private final List<PhotoSavingRequest> mRequests;
            final MediaScanController this$0;
            
            public OnMediaScanCompletedListener(final MediaScanController this$0, final List<PhotoSavingRequest> mRequests) {
                this.this$0 = this$0;
                this.mRequests = mRequests;
            }
            
            private PhotoSavingRequest pop(final String anObject) {
                while (true) {
                    for (final PhotoSavingRequest photoSavingRequest : this.mRequests) {
                        if (photoSavingRequest.getFilePath().equals(anObject)) {
                            final PhotoSavingRequest photoSavingRequest2 = photoSavingRequest;
                            if (photoSavingRequest2 != null) {
                                this.mRequests.remove(photoSavingRequest2);
                            }
                            return photoSavingRequest2;
                        }
                    }
                    final PhotoSavingRequest photoSavingRequest2 = null;
                    continue;
                }
            }
            
            public void onScanCompleted(final String s, final Uri uri) {
                final PhotoSavingRequest pop = this.pop(s);
                if (pop == null) {
                    CamLog.d("onScanCompleted() request of scan completed file is not found.");
                    return;
                }
                if (CapturePerformanceLogger.get(pop) != null) {
                    CapturePerformanceLogger.get(pop).scanFileDone = SystemClock.uptimeMillis();
                    CapturePerformanceLogger.complete(pop);
                }
                else {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("CapturePerformance: get(request) is null. path = ");
                    sb.append(pop.getFilePath());
                    CamLog.d(sb.toString());
                }
                this.this$0.mOnScanCompletedListener.onScanCompleted(MediaSavingResult.SUCCESS, uri, pop);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("remain:");
                sb2.append(this.mRequests.size());
                sb2.append(", pending:");
                sb2.append(this.this$0.mPendingScanFileList.size());
                CamLog.d(sb2.toString());
                synchronized (this.this$0.mScanFileLock) {
                    if (this.mRequests.isEmpty()) {
                        this.this$0.scanAllPendingFilesLocked();
                    }
                }
            }
        }
        
        public interface OnScanCompletedListener
        {
            void onScanCompleted(final MediaSavingResult p0, final Uri p1, final PhotoSavingRequest p2);
        }
    }
    
    private static class NotifyStoreCompletedTask implements Runnable
    {
        private final StoreDataResult mResult;
        
        private NotifyStoreCompletedTask(final StoreDataResult mResult) {
            this.mResult = mResult;
        }
        
        @Override
        public void run() {
            if (this.mResult != null && this.mResult.savingRequest != null) {
                this.mResult.savingRequest.notifyStoreResult(this.mResult);
                if (CamLog.VERBOSE) {
                    CamLog.d(this.getClass().getName(), "mStatus.notifyStoreResult() is called.");
                }
            }
            else if (CamLog.VERBOSE) {
                CamLog.d(this.getClass().getName(), "StoreDataResult or SavingRequest is null.");
            }
        }
    }
    
    public class PhotoSavingTask implements Runnable
    {
        private volatile boolean mIsCanceled;
        private volatile boolean mIsRunning;
        private final PhotoSavingRequest mRequest;
        final SavingTaskManager this$0;
        
        public PhotoSavingTask(final SavingTaskManager this$0, final PhotoSavingRequest mRequest) {
            this.this$0 = this$0;
            this.mIsCanceled = false;
            this.mRequest = mRequest;
            this.mIsRunning = false;
        }
        
        private Uri assignOutput() {
            if (this.mRequest.getExtraOutput() != null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("assignOutput getExtraOutput != null");
                }
                if ("file".equalsIgnoreCase(this.mRequest.getExtraOutput().getScheme())) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("assignOutput getExtraOutput != null 2");
                    }
                    final File file = new File(this.mRequest.getExtraOutput().getPath());
                    if (file.getParentFile().mkdirs()) {
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Create dir: ");
                            sb.append(file.getParentFile().getPath());
                            CamLog.d(sb.toString());
                        }
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Filed mkdirs() : ");
                        sb2.append(file.getParentFile().getPath());
                        CamLog.e(sb2.toString());
                    }
                }
                return this.mRequest.getExtraOutput();
            }
            if (CamLog.VERBOSE) {
                CamLog.d("assignOutput getExtraOutput != null");
            }
            String pathname;
            if (this.mRequest.getSomcType() == 129) {
                if (CamLog.VERBOSE) {
                    CamLog.d("assignOutput getSaveTimeForPredictiveCapture() =  null");
                }
                pathname = this.this$0.mStorageManager.getBurstPhotoPath(this.mRequest);
            }
            else if (this.mRequest.getSaveTimeForPredictiveCapture() == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("assignOutput getSaveTimeForPredictiveCapture() =  null");
                }
                pathname = this.this$0.mStorageManager.getPhotoPath(this.mRequest.getStorageType());
            }
            else {
                if (CamLog.VERBOSE) {
                    CamLog.d("assignOutput getSaveTimeForPredictiveCapture() !=  null");
                }
                pathname = this.this$0.mStorageManager.getPredictiveCapturePhotoPath(this.mRequest);
            }
            if (pathname == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("assignOutput path =  null");
                }
                return null;
            }
            return Uri.fromFile(new File(pathname));
        }
        
        private int getExpectedFileSize() {
            if (this.mIsRunning) {
                return 0;
            }
            if (this.mRequest.isImageReaderUsing()) {
                return 15728640;
            }
            return this.mRequest.getImageData().length;
        }
        
        private void store(Uri queryPhotoFromDatabase) {
            if (CapturePerformanceLogger.get(this.mRequest) != null) {
                CapturePerformanceLogger.get(this.mRequest).startSave = SystemClock.uptimeMillis();
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("storeContent E URI:");
                sb.append(queryPhotoFromDatabase.toString());
                CamLog.d(sb.toString());
            }
            if ("file".equalsIgnoreCase(queryPhotoFromDatabase.getScheme())) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("This uri is file path. ");
                    sb2.append(queryPhotoFromDatabase.getPath());
                    CamLog.d(sb2.toString());
                }
                this.mRequest.setFilePath(queryPhotoFromDatabase.getPath());
            }
            else if ("content".equalsIgnoreCase(queryPhotoFromDatabase.getScheme())) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("This uri is file content. ");
                    sb3.append(queryPhotoFromDatabase.getPath());
                    CamLog.d(sb3.toString());
                }
                this.mRequest.setFilePath(queryPhotoFromDatabase.getPath());
            }
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("[datetaken:");
            sb4.append(this.mRequest.getDateTaken());
            sb4.append("]start saving");
            CamLog.d(sb4.toString());
            if (!this.writeToStorage(this.mRequest, queryPhotoFromDatabase)) {
                this.this$0.notifyStoreComplete(MediaSavingResult.FAIL, Uri.EMPTY, this.mRequest);
                return;
            }
            if (CapturePerformanceLogger.get(this.mRequest) != null) {
                CapturePerformanceLogger.get(this.mRequest).writeFileDone = SystemClock.uptimeMillis();
            }
            if (this.mRequest.shouldUpdateOrientationBeforeStoring) {
                try {
                    final File file = new File(queryPhotoFromDatabase.getPath());
                    final JpegMetadata jpegMetadata = new JpegMetadata(file);
                    jpegMetadata.set(TiffTagConstants.TIFF_TAG_ORIENTATION, ExifOption.getExifOrientation(this.mRequest.common.orientation));
                    if (StorageUtil.getStorageTypeFromPath(queryPhotoFromDatabase.getPath(), this.this$0.mContext) != Storage.StorageType.EXTERNAL_CARD) {
                        rewriteInplace(jpegMetadata, file);
                    }
                    else {
                        this.this$0.rewriteInplaceForSdCard(jpegMetadata, file);
                    }
                }
                catch (final IOException | ImageReadException | ImageWriteException obj) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("Failed to save exifOrientation. : ");
                    sb5.append(obj);
                    CamLog.e(sb5.toString());
                }
            }
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("[datetaken:");
            sb6.append(this.mRequest.getDateTaken());
            sb6.append("]store is success");
            CamLog.d(sb6.toString());
            Label_0854: {
                if (!this.mRequest.common.addToMediaStore) {
                    if (this.mRequest.getExtraOutput() != null) {
                        this.this$0.notifyStoreComplete(MediaSavingResult.SUCCESS, this.mRequest.getExtraOutput(), this.mRequest);
                        break Label_0854;
                    }
                }
                try {
                    final String filePath = this.mRequest.getFilePath();
                    if (PredictiveCapturePathBuilder.isPredictiveCaptureImage(filePath)) {
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        Date parse;
                        try {
                            parse = simpleDateFormat.parse(this.mRequest.getSaveTimeForPredictiveCapture());
                        }
                        catch (final ParseException ex) {
                            final StringBuilder sb7 = new StringBuilder();
                            sb7.append("store: parse failed. filePath:");
                            sb7.append(filePath);
                            sb7.append(" time:");
                            sb7.append(this.mRequest.getSaveTimeForPredictiveCapture());
                            CamLog.e(sb7.toString());
                            parse = null;
                        }
                        if (parse != null && !new File(filePath).setLastModified(parse.getTime())) {
                            final StringBuilder sb8 = new StringBuilder();
                            sb8.append("store: setLastModified failed. filePath:");
                            sb8.append(filePath);
                            sb8.append(" time:");
                            sb8.append(parse.getTime());
                            CamLog.e(sb8.toString());
                        }
                    }
                    if (StorageUtil.getStorageTypeFromPath(queryPhotoFromDatabase.getPath(), this.this$0.mContext) == Storage.StorageType.EXTERNAL_CARD) {
                        this.this$0.mUpdator;
                        queryPhotoFromDatabase = MediaProviderUpdator.queryPhotoFromDatabase(filePath, this.this$0.mContext);
                        if (queryPhotoFromDatabase == null) {
                            this.this$0.mMediaScanController.requestScanFile(this.mRequest);
                        }
                        else {
                            this.this$0.notifyStoreComplete(MediaSavingResult.SUCCESS, queryPhotoFromDatabase, this.mRequest);
                        }
                    }
                    else {
                        this.this$0.mMediaScanController.requestScanFile(this.mRequest);
                    }
                }
                catch (final SQLiteFullException ex2) {
                    this.this$0.notifyStoreComplete(MediaSavingResult.FAIL_MEMORY_FULL, Uri.EMPTY, this.mRequest);
                }
            }
            CamLog.d("store() X");
        }
        
        private void verifyImageDataAfterStoring(final int i, final Uri obj) {
            if ("file".equalsIgnoreCase(obj.getScheme())) {
                final File file = new File(obj.getPath());
                if (file.length() != i) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("The requested image data is not stored correctly. uri:");
                    sb.append(obj);
                    sb.append(" expected:");
                    sb.append(i);
                    sb.append(" actual:");
                    sb.append(file.length());
                    throw new RuntimeException(sb.toString());
                }
            }
        }
        
        private int verifyImageDataBeforeStoring(final PhotoSavingRequest photoSavingRequest, final Uri obj) {
            int n;
            if (photoSavingRequest.isImageReaderUsing()) {
                n = photoSavingRequest.getImageReaderData().limit();
            }
            else {
                n = photoSavingRequest.getImageData().length;
            }
            if (n == 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("The image data is empty. Camera will create broken file.");
                sb.append(" uri:");
                sb.append(obj);
                final String string = sb.toString();
                final ByteBuffer imageReaderData = photoSavingRequest.getImageReaderData();
                String string2 = string;
                if (imageReaderData != null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(string);
                    sb2.append(" buff.capacity:");
                    sb2.append(imageReaderData.capacity());
                    final String string3 = sb2.toString();
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(string3);
                    sb3.append(" buff.limit:");
                    sb3.append(imageReaderData.limit());
                    final String string4 = sb3.toString();
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(string4);
                    sb4.append(" buff.position:");
                    sb4.append(imageReaderData.position());
                    string2 = sb4.toString();
                }
                throw new RuntimeException(string2);
            }
            return n;
        }
        
        private boolean writeToStorage(final PhotoSavingRequest photoSavingRequest, final Uri uri) {
            final boolean b = false;
            boolean b2;
            try {
                final int verifyImageDataBeforeStoring = this.verifyImageDataBeforeStoring(photoSavingRequest, uri);
                if (photoSavingRequest.isImageReaderUsing()) {
                    b2 = this.this$0.new ImageToFile(photoSavingRequest.getImageReaderData(), uri).storeData(null);
                    photoSavingRequest.close();
                }
                else {
                    b2 = this.this$0.new ImageToFile(photoSavingRequest.getImageData(), uri).storeData(null);
                }
                if (CapturePerformanceLogger.get(photoSavingRequest) != null) {
                    CapturePerformanceLogger.get(photoSavingRequest).fileSize = verifyImageDataBeforeStoring;
                }
                if (CamLog.DEBUG && b2) {
                    this.verifyImageDataAfterStoring(verifyImageDataBeforeStoring, uri);
                }
            }
            catch (final IllegalStateException obj) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to store image. : ");
                sb.append(obj);
                CamLog.e(sb.toString());
                b2 = b;
            }
            return b2;
        }
        
        public void cancel() {
            this.mIsCanceled = true;
        }
        
        @Override
        public final void run() {
            if (this.mIsCanceled) {
                return;
            }
            this.mIsRunning = true;
            final Storage.StorageType storageType = this.mRequest.getStorageType();
            final Semaphore semaphore = this.this$0.mStorageAccessSemaphoreMap.get(storageType);
            try {
                semaphore.acquire();
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("PhotoSavingTask[");
                    sb.append(storageType);
                    sb.append("]: E");
                    CamLog.d(sb.toString());
                }
                final Uri assignOutput = this.assignOutput();
                if (assignOutput != null) {
                    this.store(assignOutput);
                    this.this$0.popPhotoSavingTask(this);
                    this.this$0.mStorageManager.updateStorageState(storageType, CameraStorageManager.UpdateRequestReason.PHOTO_STORING_COMPLETED);
                    this.this$0.mStorageManager.checkAndNotifyStateChanged(storageType);
                }
                else {
                    if (CamLog.VERBOSE) {
                        CamLog.d("assignOutput() is null");
                    }
                    this.this$0.popPhotoSavingTask(this);
                    this.this$0.mStorageManager.updateStorageState(storageType, CameraStorageManager.UpdateRequestReason.PHOTO_STORING_COMPLETED);
                    this.mRequest.close();
                    this.this$0.runOnUiThread(new Runnable(this, storageType) {
                        final PhotoSavingTask this$1;
                        final Storage.StorageType val$type;
                        
                        @Override
                        public void run() {
                            this.this$1.this$0.mStorageManager.checkRemain(true, this.val$type);
                        }
                    });
                    this.this$0.notifyStoreComplete(MediaSavingResult.FAIL, Uri.EMPTY, this.mRequest);
                }
                semaphore.release();
                if (CamLog.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("PhotoSavingTask[");
                    sb2.append(storageType);
                    sb2.append("]: X");
                    CamLog.d(sb2.toString());
                }
            }
            catch (final InterruptedException ex) {
                CamLog.e("Failed to acquire of storage access permit.");
            }
        }
    }
    
    public enum SavedFileType
    {
        private static final SavedFileType[] $VALUES;
        
        BURST, 
        PHOTO, 
        PHOTO_DURING_REC, 
        TIME_SHIFT, 
        VIDEO;
        
        static {
            $VALUES = new SavedFileType[] { SavedFileType.PHOTO, SavedFileType.PHOTO_DURING_REC, SavedFileType.VIDEO, SavedFileType.BURST, SavedFileType.TIME_SHIFT };
        }
    }
    
    class SavingVideoTask implements Runnable
    {
        final VideoSavingRequest mRequest;
        final SavingTaskManager this$0;
        
        SavingVideoTask(final SavingTaskManager this$0, final VideoSavingRequest mRequest) {
            this.this$0 = this$0;
            this.mRequest = mRequest;
        }
        
        @Override
        public void run() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: astore_1       
            //     4: aload_0        
            //     5: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //     8: invokevirtual   com/sonyericsson/cameracommon/storage/VideoSavingRequest.getExtraOutput:()Landroid/net/Uri;
            //    11: astore_1       
            //    12: aload_0        
            //    13: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //    16: invokevirtual   com/sonyericsson/cameracommon/storage/VideoSavingRequest.getStorageType:()Lcom/sonyericsson/cameracommon/storage/Storage$StorageType;
            //    19: astore_3       
            //    20: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
            //    23: ifeq            67
            //    26: new             Ljava/lang/StringBuilder;
            //    29: dup            
            //    30: invokespecial   java/lang/StringBuilder.<init>:()V
            //    33: astore_2       
            //    34: aload_2        
            //    35: ldc             "Saving video started: ID: "
            //    37: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    40: pop            
            //    41: aload_2        
            //    42: aload_0        
            //    43: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //    46: invokevirtual   com/sonyericsson/cameracommon/storage/VideoSavingRequest.getRequestId:()I
            //    49: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
            //    52: pop            
            //    53: iconst_1       
            //    54: anewarray       Ljava/lang/String;
            //    57: dup            
            //    58: iconst_0       
            //    59: aload_2        
            //    60: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    63: aastore        
            //    64: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
            //    67: aload_0        
            //    68: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.this$0:Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;
            //    71: invokestatic    com/sonyericsson/cameracommon/storage/SavingTaskManager.access$300:(Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;)Ljava/util/Map;
            //    74: aload_3        
            //    75: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
            //    80: checkcast       Ljava/util/concurrent/Semaphore;
            //    83: astore          4
            //    85: aload           4
            //    87: invokevirtual   java/util/concurrent/Semaphore.acquire:()V
            //    90: getstatic       com/sonyericsson/android/camera/util/CamLog.DEBUG:Z
            //    93: ifeq            138
            //    96: new             Ljava/lang/StringBuilder;
            //    99: astore_2       
            //   100: aload_2        
            //   101: invokespecial   java/lang/StringBuilder.<init>:()V
            //   104: aload_2        
            //   105: ldc             "SavingVideoTask["
            //   107: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   110: pop            
            //   111: aload_2        
            //   112: aload_3        
            //   113: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //   116: pop            
            //   117: aload_2        
            //   118: ldc             "]: E"
            //   120: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   123: pop            
            //   124: iconst_1       
            //   125: anewarray       Ljava/lang/String;
            //   128: dup            
            //   129: iconst_0       
            //   130: aload_2        
            //   131: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   134: aastore        
            //   135: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
            //   138: aload_1        
            //   139: ifnull          263
            //   142: aload_0        
            //   143: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //   146: getfield        com/sonyericsson/cameracommon/storage/VideoSavingRequest.common:Lcom/sonyericsson/cameracommon/mediasaving/takenstatus/TakenStatusCommon;
            //   149: getfield        com/sonyericsson/cameracommon/mediasaving/takenstatus/TakenStatusCommon.addToMediaStore:Z
            //   152: ifeq            222
            //   155: ldc             "file"
            //   157: aload_1        
            //   158: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
            //   161: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
            //   164: ifeq            181
            //   167: aload_0        
            //   168: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //   171: aload_1        
            //   172: invokevirtual   android/net/Uri.getPath:()Ljava/lang/String;
            //   175: invokevirtual   com/sonyericsson/cameracommon/storage/VideoSavingRequest.setFilePath:(Ljava/lang/String;)V
            //   178: goto            204
            //   181: ldc             "content"
            //   183: aload_1        
            //   184: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
            //   187: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
            //   190: ifeq            204
            //   193: aload_0        
            //   194: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //   197: aload_1        
            //   198: invokevirtual   android/net/Uri.getPath:()Ljava/lang/String;
            //   201: invokevirtual   com/sonyericsson/cameracommon/storage/VideoSavingRequest.setFilePath:(Ljava/lang/String;)V
            //   204: aload_0        
            //   205: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.this$0:Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;
            //   208: invokestatic    com/sonyericsson/cameracommon/storage/SavingTaskManager.access$900:(Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;)Lcom/sonyericsson/cameracommon/mediasaving/updator/MediaProviderUpdator;
            //   211: aload_0        
            //   212: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //   215: invokevirtual   com/sonyericsson/cameracommon/mediasaving/updator/MediaProviderUpdator.insertVideoAndSendIntent:(Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;)Landroid/net/Uri;
            //   218: astore_2       
            //   219: goto            224
            //   222: aload_1        
            //   223: astore_2       
            //   224: aload_2        
            //   225: ifnonnull       250
            //   228: ldc             "content"
            //   230: aload_1        
            //   231: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
            //   234: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
            //   237: ifeq            243
            //   240: goto            250
            //   243: getstatic       com/sonyericsson/cameracommon/mediasaving/MediaSavingResult.FAIL:Lcom/sonyericsson/cameracommon/mediasaving/MediaSavingResult;
            //   246: astore_1       
            //   247: goto            304
            //   250: getstatic       com/sonyericsson/cameracommon/mediasaving/MediaSavingResult.SUCCESS:Lcom/sonyericsson/cameracommon/mediasaving/MediaSavingResult;
            //   253: astore_1       
            //   254: goto            304
            //   257: astore_1       
            //   258: aconst_null    
            //   259: astore_2       
            //   260: goto            300
            //   263: aload_0        
            //   264: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.this$0:Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;
            //   267: invokestatic    com/sonyericsson/cameracommon/storage/SavingTaskManager.access$900:(Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;)Lcom/sonyericsson/cameracommon/mediasaving/updator/MediaProviderUpdator;
            //   270: aload_0        
            //   271: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //   274: invokevirtual   com/sonyericsson/cameracommon/mediasaving/updator/MediaProviderUpdator.insertVideoAndSendIntent:(Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;)Landroid/net/Uri;
            //   277: astore_2       
            //   278: aload_2        
            //   279: ifnull          293
            //   282: getstatic       com/sonyericsson/cameracommon/mediasaving/MediaSavingResult.SUCCESS:Lcom/sonyericsson/cameracommon/mediasaving/MediaSavingResult;
            //   285: astore_1       
            //   286: goto            297
            //   289: astore_1       
            //   290: goto            300
            //   293: getstatic       com/sonyericsson/cameracommon/mediasaving/MediaSavingResult.FAIL:Lcom/sonyericsson/cameracommon/mediasaving/MediaSavingResult;
            //   296: astore_1       
            //   297: goto            304
            //   300: getstatic       com/sonyericsson/cameracommon/mediasaving/MediaSavingResult.FAIL_MEMORY_FULL:Lcom/sonyericsson/cameracommon/mediasaving/MediaSavingResult;
            //   303: astore_1       
            //   304: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
            //   307: ifeq            355
            //   310: new             Ljava/lang/StringBuilder;
            //   313: dup            
            //   314: invokespecial   java/lang/StringBuilder.<init>:()V
            //   317: astore          5
            //   319: aload           5
            //   321: ldc             "Saving video finished: ID: "
            //   323: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   326: pop            
            //   327: aload           5
            //   329: aload_0        
            //   330: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //   333: invokevirtual   com/sonyericsson/cameracommon/storage/VideoSavingRequest.getRequestId:()I
            //   336: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
            //   339: pop            
            //   340: iconst_1       
            //   341: anewarray       Ljava/lang/String;
            //   344: dup            
            //   345: iconst_0       
            //   346: aload           5
            //   348: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   351: aastore        
            //   352: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
            //   355: aload_0        
            //   356: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.this$0:Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;
            //   359: invokestatic    com/sonyericsson/cameracommon/storage/SavingTaskManager.access$200:(Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;)Lcom/sonyericsson/cameracommon/storage/CameraStorageManager;
            //   362: aload_3        
            //   363: getstatic       com/sonyericsson/cameracommon/storage/CameraStorageManager$UpdateRequestReason.VIDEO_STORING_COMPLETED:Lcom/sonyericsson/cameracommon/storage/CameraStorageManager$UpdateRequestReason;
            //   366: invokevirtual   com/sonyericsson/cameracommon/storage/CameraStorageManager.updateStorageState:(Lcom/sonyericsson/cameracommon/storage/Storage$StorageType;Lcom/sonyericsson/cameracommon/storage/CameraStorageManager$UpdateRequestReason;)V
            //   369: aload           4
            //   371: invokevirtual   java/util/concurrent/Semaphore.release:()V
            //   374: getstatic       com/sonyericsson/android/camera/util/CamLog.DEBUG:Z
            //   377: ifeq            427
            //   380: new             Ljava/lang/StringBuilder;
            //   383: dup            
            //   384: invokespecial   java/lang/StringBuilder.<init>:()V
            //   387: astore          4
            //   389: aload           4
            //   391: ldc             "SavingVideoTask["
            //   393: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   396: pop            
            //   397: aload           4
            //   399: aload_3        
            //   400: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //   403: pop            
            //   404: aload           4
            //   406: ldc             "]: X"
            //   408: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   411: pop            
            //   412: iconst_1       
            //   413: anewarray       Ljava/lang/String;
            //   416: dup            
            //   417: iconst_0       
            //   418: aload           4
            //   420: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   423: aastore        
            //   424: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
            //   427: aload_0        
            //   428: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.this$0:Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;
            //   431: aload_1        
            //   432: aload_2        
            //   433: aload_0        
            //   434: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.mRequest:Lcom/sonyericsson/cameracommon/storage/VideoSavingRequest;
            //   437: invokestatic    com/sonyericsson/cameracommon/storage/SavingTaskManager.access$600:(Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;Lcom/sonyericsson/cameracommon/mediasaving/MediaSavingResult;Landroid/net/Uri;Lcom/sonyericsson/cameracommon/storage/SavingRequest;)V
            //   440: aload_0        
            //   441: getfield        com/sonyericsson/cameracommon/storage/SavingTaskManager$SavingVideoTask.this$0:Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;
            //   444: aconst_null    
            //   445: invokestatic    com/sonyericsson/cameracommon/storage/SavingTaskManager.access$1202:(Lcom/sonyericsson/cameracommon/storage/SavingTaskManager;Ljava/lang/Thread;)Ljava/lang/Thread;
            //   448: pop            
            //   449: return         
            //   450: astore_1       
            //   451: iconst_1       
            //   452: anewarray       Ljava/lang/String;
            //   455: dup            
            //   456: iconst_0       
            //   457: ldc             "Failed to acquire of storage access permit."
            //   459: aastore        
            //   460: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
            //   463: return         
            //   464: astore_1       
            //   465: goto            300
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                         
            //  -----  -----  -----  -----  ---------------------------------------------
            //  85     138    450    464    Ljava/lang/InterruptedException;
            //  142    178    257    263    Landroid/database/sqlite/SQLiteFullException;
            //  181    204    257    263    Landroid/database/sqlite/SQLiteFullException;
            //  204    219    257    263    Landroid/database/sqlite/SQLiteFullException;
            //  228    240    464    468    Landroid/database/sqlite/SQLiteFullException;
            //  243    247    464    468    Landroid/database/sqlite/SQLiteFullException;
            //  250    254    464    468    Landroid/database/sqlite/SQLiteFullException;
            //  263    278    257    263    Landroid/database/sqlite/SQLiteFullException;
            //  282    286    289    293    Landroid/database/sqlite/SQLiteFullException;
            //  293    297    289    293    Landroid/database/sqlite/SQLiteFullException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0243:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2604)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:662)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
}
