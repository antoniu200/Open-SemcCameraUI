package com.sonyericsson.cameracommon.mediasaving.updator;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.cameracommon.storage.VideoSavingRequest;
import com.sonymobile.media.SomcMediaStore;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MediaProviderUpdator {
    private static final Uri EXTENDED_FILES_CONTENT_URI = SomcMediaStore.ExtendedFiles.getContentUri("external");
    public static final String TAG = "MediaProviderUpdator";
    private static final int TIME_INTERVAL_QUERY_IN_MILLI = 200;
    private static final int TIME_OUT_QUERY_IN_MILLI = 1000;
    private static final int TIME_OUT_SCANNER_IN_MILLI = 30000;
    private static final String VOLUME_EXTERNAL_PRIMARY = "external_primary";
    protected Context mContext;

    public MediaProviderUpdator(Context context) {
        this.mContext = null;
        this.mContext = context;
    }

    private synchronized Uri scanFile(String str) {
        if (CamLog.VERBOSE) {
            CamLog.d("scanFile() is called. Path is : " + str);
        }
        if (str != null) {
            OnScanCompletedListener onScanCompletedListener = new OnScanCompletedListener(str);
            MediaScannerConnection.scanFile(this.mContext, new String[]{str}, null, onScanCompletedListener);
            return onScanCompletedListener.getScanResult();
        }
        CamLog.e("Illegal argument. scanFile is called with null.");
        return null;
    }

    private static class OnScanCompletedListener implements MediaScannerConnection.OnScanCompletedListener {
        private final CountDownLatch mLatch = new CountDownLatch(1);
        private Uri mScanResult;

        public OnScanCompletedListener(String str) {
        }

        @Override // android.media.MediaScannerConnection.OnScanCompletedListener
        public void onScanCompleted(String str, Uri uri) {
            if (CamLog.VERBOSE) {
                CamLog.d("onScanCompleted E");
            }
            if (CamLog.VERBOSE) {
                CamLog.d("  uri:" + uri);
            }
            if (CamLog.VERBOSE) {
                CamLog.d("  path:" + str);
            }
            this.mScanResult = uri;
            this.mLatch.countDown();
            if (CamLog.VERBOSE) {
                CamLog.d("onScanCompleted X");
            }
        }

        public Uri getScanResult() {
            try {
                if (CamLog.VERBOSE) {
                    CamLog.d("getScanResult wait 30 seconds...");
                }
                if (!this.mLatch.await(30000L, TimeUnit.MILLISECONDS)) {
                    CamLog.e("getScanResult is timeout.");
                }
            } catch (InterruptedException e) {
                CamLog.e("scan video file failed.", e);
            }
            if (CamLog.VERBOSE) {
                CamLog.d("getScanResult done. " + this.mScanResult);
            }
            return this.mScanResult;
        }
    }

    private Uri insertVideoContentManager(VideoSavingRequest videoSavingRequest) {
        String filePath = videoSavingRequest.getFilePath();
        if (CamLog.VERBOSE) {
            CamLog.d("insertVideoContentManager: " + filePath);
        }
        Uri uriScanFile = scanFile(filePath);
        if (CamLog.VERBOSE) {
            CamLog.d("insertVideoContentManager: result: " + uriScanFile);
        }
        return uriScanFile;
    }

    public Uri insertVideoAndSendIntent(VideoSavingRequest videoSavingRequest) {
        MediaSavingResult mediaSavingResult = MediaSavingResult.FAIL;
        Uri uriQueryVideoFromDatabase = Uri.EMPTY;
        String filePath = videoSavingRequest.getFilePath();
        if (filePath != null) {
            File file = new File(filePath);
            if (!file.exists() || !file.canRead()) {
                return null;
            }
            if (StorageUtil.getStorageTypeFromPath(filePath, this.mContext) != Storage.StorageType.EXTERNAL_CARD || (uriQueryVideoFromDatabase = queryVideoFromDatabase(filePath, this.mContext)) == null) {
                Uri uriInsertVideoContentManager = insertVideoContentManager(videoSavingRequest);
                uriQueryVideoFromDatabase = uriInsertVideoContentManager;
            }
            if (uriQueryVideoFromDatabase != null) {
                mediaSavingResult = MediaSavingResult.SUCCESS;
            }
        }
        if (mediaSavingResult != MediaSavingResult.SUCCESS) {
            CamLog.e("Failed to inserting a video:" + mediaSavingResult);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("insertVideoAndSendIntent: result: " + uriQueryVideoFromDatabase);
        }
        return uriQueryVideoFromDatabase;
    }

    public static void sendBroadcastCameraShot(Context context, Uri uri) {
        if (CamLog.DEBUG) {
            CamLog.d("sendBroadcastCameraShot: " + uri);
        }
        if (uri == null) {
            return;
        }
        if (context == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Activity has already finished.");
                return;
            }
            return;
        }
        String string = uri.toString();
        if (string.contains(MediaStore.Images.Media.getContentUri(VOLUME_EXTERNAL_PRIMARY).toString())) {
            context.sendBroadcast(new Intent("android.hardware.action.NEW_PICTURE", uri));
            return;
        }
        if (string.contains(MediaStore.Video.Media.getContentUri(VOLUME_EXTERNAL_PRIMARY).toString())) {
            context.sendBroadcast(new Intent("android.hardware.action.NEW_VIDEO", uri));
        } else if (CamLog.DEBUG) {
            CamLog.w("Invalid URI: " + uri);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x008f, code lost:
    
        r12 = r11.getString(r11.getColumnIndex("_id"));
        r12 = android.net.Uri.withAppendedPath(android.net.Uri.parse("content://media/external/video/media"), "" + r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00b4, code lost:
    
        r11.close();
        r3 = r12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.net.Uri queryVideoFromDatabase(java.lang.String r11, android.content.Context r12) {
        /*
            Method dump skipped, instructions count: 299
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.mediasaving.updator.MediaProviderUpdator.queryVideoFromDatabase(java.lang.String, android.content.Context):android.net.Uri");
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x008f, code lost:
    
        r12 = r11.getString(r11.getColumnIndex("_id"));
        r12 = android.net.Uri.withAppendedPath(android.net.Uri.parse("content://media/external/images/media"), "" + r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00b4, code lost:
    
        r11.close();
        r3 = r12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.net.Uri queryPhotoFromDatabase(java.lang.String r11, android.content.Context r12) {
        /*
            Method dump skipped, instructions count: 299
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.mediasaving.updator.MediaProviderUpdator.queryPhotoFromDatabase(java.lang.String, android.content.Context):android.net.Uri");
    }
}
