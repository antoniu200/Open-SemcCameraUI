// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.updator;

import android.database.Cursor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import android.provider.MediaStore$Video$Media;
import android.content.Intent;
import android.provider.MediaStore$Images$Media;
import android.media.MediaScannerConnection$OnScanCompletedListener;
import android.media.MediaScannerConnection;
import android.os.SystemClock;
import android.content.ContentResolver;
import com.sonyericsson.cameracommon.contentsview.PhotoStackQueryHelper;
import java.util.Locale;
import java.io.File;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.VideoSavingRequest;
import com.sonymobile.media.SomcMediaStore;
import android.content.Context;
import android.net.Uri;

public class MediaProviderUpdator
{
    private static final Uri EXTENDED_FILES_CONTENT_URI;
    public static final String TAG = "MediaProviderUpdator";
    private static final int TIME_INTERVAL_QUERY_IN_MILLI = 200;
    private static final int TIME_OUT_QUERY_IN_MILLI = 1000;
    private static final int TIME_OUT_SCANNER_IN_MILLI = 30000;
    private static final String VOLUME_EXTERNAL_PRIMARY = "external_primary";
    protected Context mContext;
    
    static {
        EXTENDED_FILES_CONTENT_URI = SomcMediaStore.ExtendedFiles.getContentUri("external");
    }
    
    public MediaProviderUpdator(final Context mContext) {
        this.mContext = null;
        this.mContext = mContext;
    }
    
    private Uri insertVideoContentManager(final VideoSavingRequest videoSavingRequest) {
        final String filePath = videoSavingRequest.getFilePath();
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("insertVideoContentManager: ");
            sb.append(filePath);
            CamLog.d(sb.toString());
        }
        final Uri scanFile = this.scanFile(filePath);
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("insertVideoContentManager: result: ");
            sb2.append(scanFile);
            CamLog.d(sb2.toString());
        }
        return scanFile;
    }
    
    public static Uri queryPhotoFromDatabase(final String s, Context crQuery) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("queryPhotoFromDatabase: start: ");
            sb.append(s);
            CamLog.d(sb.toString());
        }
        final MediaSavingResult fail = MediaSavingResult.FAIL;
        final Uri uri = null;
        final Uri uri2 = null;
        MediaSavingResult success = fail;
        Uri obj = uri;
        if (s != null) {
            success = fail;
            obj = uri;
            if (crQuery != null) {
                final File file = new File(s);
                if (!file.exists() || !file.canRead()) {
                    return null;
                }
                Object o = crQuery.getContentResolver();
                Object string = new CrQueryParameter();
                ((CrQueryParameter)string).projection = new String[] { "_id", "_data" };
                ((CrQueryParameter)string).sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
                ((CrQueryParameter)string).where = String.format(Locale.US, "%s like '%s'", "_data", s);
                final long currentTimeMillis = System.currentTimeMillis();
                Uri uri3;
                while (true) {
                    uri3 = uri2;
                    if (System.currentTimeMillis() - currentTimeMillis >= 1000L) {
                        break;
                    }
                    crQuery = (Context)PhotoStackQueryHelper.crQuery((ContentResolver)o, MediaProviderUpdator.EXTENDED_FILES_CONTENT_URI, (CrQueryParameter)string);
                    if (crQuery != null) {
                        try {
                            if (((Cursor)crQuery).moveToFirst()) {
                                string = ((Cursor)crQuery).getString(((Cursor)crQuery).getColumnIndex("_id"));
                                o = Uri.parse("content://media/external/images/media");
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("");
                                sb2.append((String)string);
                                Uri.withAppendedPath((Uri)o, sb2.toString());
                                break;
                            }
                        }
                        finally {
                            ((Cursor)crQuery).close();
                        }
                    }
                    SystemClock.sleep(200L);
                    if (!CamLog.DEBUG) {
                        continue;
                    }
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Failed to query:");
                    sb3.append(System.currentTimeMillis());
                    CamLog.e(sb3.toString());
                }
                success = fail;
                if ((obj = uri3) != null) {
                    success = MediaSavingResult.SUCCESS;
                    obj = uri3;
                }
            }
        }
        if (success != MediaSavingResult.SUCCESS && CamLog.DEBUG) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Failed to query photo:");
            sb4.append(success);
            CamLog.e(sb4.toString());
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("queryPhotoFromDatabase: result: ");
            sb5.append(obj);
            CamLog.d(sb5.toString());
        }
        return obj;
    }
    
    public static Uri queryVideoFromDatabase(final String s, Context crQuery) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("queryVideoFromDatabase: start: ");
            sb.append(s);
            CamLog.d(sb.toString());
        }
        final MediaSavingResult fail = MediaSavingResult.FAIL;
        final Uri uri = null;
        final Uri uri2 = null;
        MediaSavingResult success = fail;
        Uri obj = uri;
        if (s != null) {
            success = fail;
            obj = uri;
            if (crQuery != null) {
                final File file = new File(s);
                if (!file.exists() || !file.canRead()) {
                    return null;
                }
                Object contentResolver = crQuery.getContentResolver();
                Object string = new CrQueryParameter();
                ((CrQueryParameter)string).projection = new String[] { "_id", "_data" };
                ((CrQueryParameter)string).sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
                ((CrQueryParameter)string).where = String.format(Locale.US, "%s like '%s'", "_data", s);
                final long currentTimeMillis = System.currentTimeMillis();
                Uri uri3;
                while (true) {
                    uri3 = uri2;
                    if (System.currentTimeMillis() - currentTimeMillis >= 1000L) {
                        break;
                    }
                    crQuery = (Context)PhotoStackQueryHelper.crQuery((ContentResolver)contentResolver, MediaProviderUpdator.EXTENDED_FILES_CONTENT_URI, (CrQueryParameter)string);
                    if (crQuery != null) {
                        try {
                            if (((Cursor)crQuery).moveToFirst()) {
                                string = ((Cursor)crQuery).getString(((Cursor)crQuery).getColumnIndex("_id"));
                                final Uri parse = Uri.parse("content://media/external/video/media");
                                contentResolver = new StringBuilder();
                                ((StringBuilder)contentResolver).append("");
                                ((StringBuilder)contentResolver).append((String)string);
                                Uri.withAppendedPath(parse, ((StringBuilder)contentResolver).toString());
                                break;
                            }
                        }
                        finally {
                            ((Cursor)crQuery).close();
                        }
                    }
                    SystemClock.sleep(200L);
                    if (!CamLog.DEBUG) {
                        continue;
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to query video:");
                    sb2.append(System.currentTimeMillis());
                    CamLog.e(sb2.toString());
                }
                success = fail;
                if ((obj = uri3) != null) {
                    success = MediaSavingResult.SUCCESS;
                    obj = uri3;
                }
            }
        }
        if (success != MediaSavingResult.SUCCESS && CamLog.DEBUG) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Failed to query video:");
            sb3.append(success);
            CamLog.e(sb3.toString());
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("queryVideoFromDatabase: result: ");
            sb4.append(obj);
            CamLog.d(sb4.toString());
        }
        return obj;
    }
    
    private Uri scanFile(final String str) {
        synchronized (this) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("scanFile() is called. Path is : ");
                sb.append(str);
                CamLog.d(sb.toString());
            }
            if (str != null) {
                final OnScanCompletedListener onScanCompletedListener = new OnScanCompletedListener(str);
                MediaScannerConnection.scanFile(this.mContext, new String[] { str }, (String[])null, (MediaScannerConnection$OnScanCompletedListener)onScanCompletedListener);
                return onScanCompletedListener.getScanResult();
            }
            CamLog.e("Illegal argument. scanFile is called with null.");
            return null;
        }
    }
    
    public static void sendBroadcastCameraShot(final Context context, final Uri uri) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendBroadcastCameraShot: ");
            sb.append(uri);
            CamLog.d(sb.toString());
        }
        if (uri == null) {
            return;
        }
        if (context == null) {
            if (CamLog.DEBUG) {
                CamLog.d("Activity has already finished.");
            }
            return;
        }
        final String string = uri.toString();
        if (string.contains(MediaStore$Images$Media.getContentUri("external_primary").toString())) {
            context.sendBroadcast(new Intent("android.hardware.action.NEW_PICTURE", uri));
        }
        else if (string.contains(MediaStore$Video$Media.getContentUri("external_primary").toString())) {
            context.sendBroadcast(new Intent("android.hardware.action.NEW_VIDEO", uri));
        }
        else if (CamLog.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid URI: ");
            sb2.append(uri);
            CamLog.w(sb2.toString());
        }
    }
    
    public Uri insertVideoAndSendIntent(final VideoSavingRequest videoSavingRequest) {
        final MediaSavingResult fail = MediaSavingResult.FAIL;
        Uri empty = Uri.EMPTY;
        final String filePath = videoSavingRequest.getFilePath();
        MediaSavingResult success = fail;
        if (filePath != null) {
            final File file = new File(filePath);
            if (!file.exists() || !file.canRead()) {
                return null;
            }
            Uri queryVideoFromDatabase = null;
            Label_0094: {
                Uri uri;
                if (StorageUtil.getStorageTypeFromPath(filePath, this.mContext) == Storage.StorageType.EXTERNAL_CARD) {
                    if ((queryVideoFromDatabase = queryVideoFromDatabase(filePath, this.mContext)) != null) {
                        break Label_0094;
                    }
                    uri = this.insertVideoContentManager(videoSavingRequest);
                }
                else {
                    uri = this.insertVideoContentManager(videoSavingRequest);
                }
                queryVideoFromDatabase = uri;
            }
            success = fail;
            if ((empty = queryVideoFromDatabase) != null) {
                success = MediaSavingResult.SUCCESS;
                empty = queryVideoFromDatabase;
            }
        }
        if (success != MediaSavingResult.SUCCESS) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to inserting a video:");
            sb.append(success);
            CamLog.e(sb.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("insertVideoAndSendIntent: result: ");
            sb2.append(empty);
            CamLog.d(sb2.toString());
        }
        return empty;
    }
    
    private static class OnScanCompletedListener implements MediaScannerConnection$OnScanCompletedListener
    {
        private final CountDownLatch mLatch;
        private Uri mScanResult;
        
        public OnScanCompletedListener(final String s) {
            this.mLatch = new CountDownLatch(1);
        }
        
        public Uri getScanResult() {
            try {
                if (CamLog.VERBOSE) {
                    CamLog.d("getScanResult wait 30 seconds...");
                }
                if (!this.mLatch.await(30000L, TimeUnit.MILLISECONDS)) {
                    CamLog.e("getScanResult is timeout.");
                }
            }
            catch (final InterruptedException ex) {
                CamLog.e("scan video file failed.", ex);
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getScanResult done. ");
                sb.append(this.mScanResult);
                CamLog.d(sb.toString());
            }
            return this.mScanResult;
        }
        
        public void onScanCompleted(final String str, final Uri uri) {
            if (CamLog.VERBOSE) {
                CamLog.d("onScanCompleted E");
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  uri:");
                sb.append(uri);
                CamLog.d(sb.toString());
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("  path:");
                sb2.append(str);
                CamLog.d(sb2.toString());
            }
            this.mScanResult = uri;
            this.mLatch.countDown();
            if (CamLog.VERBOSE) {
                CamLog.d("onScanCompleted X");
            }
        }
    }
}
