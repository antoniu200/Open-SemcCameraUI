// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import java.util.LinkedList;
import com.sonyericsson.cameracommon.contentsview.PhotoStackQueryHelper;
import java.util.Locale;
import android.os.Environment;
import java.util.Iterator;
import com.sonyericsson.cameracommon.contentsview.ThumbnailFactory;
import android.provider.MediaStore$Images$Media;
import android.provider.MediaStore$Video$Media;
import com.sonyericsson.cameracommon.contentsview.QueryParameterAdapter;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.Bitmap;
import java.util.List;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.media.SomcMediaStore;
import android.content.ContentResolver;
import com.sonyericsson.cameracommon.mediasaving.updator.CrQueryParameter;
import java.util.ArrayList;
import android.content.Context;
import android.net.Uri;
import java.util.concurrent.Callable;

public class DataLoader implements Callable<Long>
{
    private static final int COLUMN_INDEX_BUCKETID = 7;
    private static final int COLUMN_INDEX_DATA = 1;
    private static final int COLUMN_INDEX_HEIGHT = 5;
    private static final int COLUMN_INDEX_ID = 0;
    private static final int COLUMN_INDEX_MIME = 2;
    private static final int COLUMN_INDEX_ORIENTATION = 6;
    private static final int COLUMN_INDEX_WIDTH = 4;
    public static final String EXTENDED_FILES_COLUMN_ID = "files_id";
    public static final Uri EXTENDED_FILES_CONTENT_URI;
    public static final float PANORAMA_ASPECT_THRESHOLD = 1.8777778f;
    public static final String TAG = "DataLoader";
    private final String[] CONTENT_EXTENSIONS;
    private Context mContext;
    private Storage.OnLoadCompletedListener mDataLoadCallback;
    private boolean mIsRegisterCache;
    private int mMediaId;
    private ArrayList<Uri> mMediaUris;
    private CrQueryParameter mParam;
    private int mRequestId;
    private final ContentResolver mResolver;
    
    static {
        EXTENDED_FILES_CONTENT_URI = SomcMediaStore.ExtendedFiles.getContentUri("external");
    }
    
    public DataLoader(final int mRequestId, final Uri uri, final Context mContext, final Storage.OnLoadCompletedListener mDataLoadCallback, final boolean mIsRegisterCache) {
        this.CONTENT_EXTENSIONS = new String[] { ".JPG", ".3gp", ".mp4" };
        this.mParam = null;
        this.mRequestId = mRequestId;
        try {
            this.mMediaId = Integer.parseInt(uri.getLastPathSegment());
        }
        catch (final Exception ex) {
            CamLog.w("mediaId is not corrected.");
        }
        this.mContext = mContext;
        this.mResolver = this.mContext.getContentResolver();
        this.mDataLoadCallback = mDataLoadCallback;
        this.mIsRegisterCache = mIsRegisterCache;
    }
    
    public DataLoader(final Context mContext, final ArrayList<Uri> mMediaUris, final Storage.OnLoadCompletedListener mDataLoadCallback, final boolean mIsRegisterCache) {
        this.CONTENT_EXTENSIONS = new String[] { ".JPG", ".3gp", ".mp4" };
        this.mParam = null;
        this.mRequestId = -1;
        this.mContext = mContext;
        this.mResolver = this.mContext.getContentResolver();
        this.mMediaUris = mMediaUris;
        this.mDataLoadCallback = mDataLoadCallback;
        this.mIsRegisterCache = mIsRegisterCache;
    }
    
    public DataLoader(final Context mContext, final List<String> list, final int mRequestId, final int n, final Storage.OnLoadCompletedListener mDataLoadCallback, final boolean mIsRegisterCache) {
        this.CONTENT_EXTENSIONS = new String[] { ".JPG", ".3gp", ".mp4" };
        this.mParam = null;
        this.mRequestId = mRequestId;
        this.mParam = this.setupQueryParam(list, n);
        this.mContext = mContext;
        this.mResolver = this.mContext.getContentResolver();
        this.mDataLoadCallback = mDataLoadCallback;
        this.mIsRegisterCache = mIsRegisterCache;
    }
    
    public DataLoader(final Context mContext, final List<String> list, final int n, final Storage.OnLoadCompletedListener mDataLoadCallback, final boolean mIsRegisterCache) {
        this.CONTENT_EXTENSIONS = new String[] { ".JPG", ".3gp", ".mp4" };
        this.mParam = null;
        this.mRequestId = -1;
        this.mParam = this.setupQueryParam(list, n);
        this.mContext = mContext;
        this.mResolver = this.mContext.getContentResolver();
        this.mDataLoadCallback = mDataLoadCallback;
        this.mIsRegisterCache = mIsRegisterCache;
    }
    
    private Bitmap createAntiAliasBitmap(final Bitmap bitmap, final int n) {
        if (bitmap != null && !bitmap.isRecycled()) {
            return Bitmap.createBitmap(bitmap, 0, 0, n, n, (Matrix)null, true);
        }
        return null;
    }
    
    private Content.ContentInfo createContentInfo(final Cursor cursor) {
        return this.createContentInfo(cursor, true);
    }
    
    private Content.ContentInfo createContentInfo(final Cursor cursor, final boolean mIsContainDetails) {
        final int mediaId = this.getMediaId(cursor);
        int mType = 2;
        final String string = cursor.getString(2);
        final String string2 = cursor.getString(1);
        final int int1 = cursor.getInt(4);
        final int int2 = cursor.getInt(5);
        final int int3 = cursor.getInt(7);
        final String fileExtension = CommonUtility.getFileExtension(string2);
        final boolean equals = string.equals("image/jpeg");
        int int4 = 0;
        Uri mOriginalUri;
        if (!equals && !this.isSupportedFileExtension(".JPG", fileExtension)) {
            if (!string.equals("video/mp4") && !this.isSupportedFileExtension(".mp4", fileExtension)) {
                if (!string.equals("video/3gpp") && !this.isSupportedFileExtension(".3gp", fileExtension)) {
                    if (!string.equals("image/mpo")) {
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("query error : mime = ");
                            sb.append(string);
                            CamLog.d(sb.toString());
                        }
                        return null;
                    }
                    mOriginalUri = Uri.withAppendedPath(QueryParameterAdapter.MPO_3DPICTURES_CONTENT_URI, String.valueOf(mediaId));
                    mType = 3;
                }
                else {
                    mOriginalUri = Uri.withAppendedPath(MediaStore$Video$Media.EXTERNAL_CONTENT_URI, String.valueOf(mediaId));
                }
            }
            else {
                mOriginalUri = Uri.withAppendedPath(MediaStore$Video$Media.EXTERNAL_CONTENT_URI, String.valueOf(mediaId));
            }
        }
        else {
            mOriginalUri = Uri.withAppendedPath(MediaStore$Images$Media.EXTERNAL_CONTENT_URI, String.valueOf(mediaId));
            mType = 1;
        }
        if (mType == 1) {
            int4 = cursor.getInt(6);
        }
        final Content.ContentInfo contentInfo = new Content.ContentInfo();
        contentInfo.mId = mediaId;
        contentInfo.mOriginalUri = mOriginalUri;
        contentInfo.mOriginalPath = string2;
        contentInfo.mType = mType;
        contentInfo.mWidth = int1;
        contentInfo.mHeight = int2;
        contentInfo.mOrientation = int4;
        contentInfo.mMimeType = string;
        contentInfo.mBucketId = int3;
        contentInfo.mIsContainDetails = mIsContainDetails;
        if (mIsContainDetails) {
            contentInfo.mGroupedImage = this.getGroupedImageCount(int3);
            contentInfo.mSomcType = this.getSomcType(string2);
            contentInfo.mIsVideoHdr = this.isVideoHdr(string2);
            contentInfo.mContentType = this.getContentType(contentInfo);
            if (contentInfo.mContentType == Content.ContentsType.BURST) {
                contentInfo.mMediaStoreIds = this.getGroupedImageMediaID(int3, contentInfo);
            }
        }
        return contentInfo;
    }
    
    private Content.ContentInfo createContentInfoForMediaUris(final Cursor cursor) {
        return this.createContentInfo(cursor, false);
    }
    
    private Bitmap decodeThumbnail(final Content.ContentInfo contentInfo) {
        if (CamLog.VERBOSE) {
            CamLog.d("decodeThumbnail() has been called.");
        }
        Bitmap bitmap = null;
        if (contentInfo != null) {
            if (contentInfo.mOriginalPath == null) {
                contentInfo.mOriginalPath = this.getMediaPath(contentInfo.mId, contentInfo.mType);
            }
            Bitmap obj = ThumbnailFactory.createMicroThumbnail(contentInfo);
            if (obj != null) {
                obj = this.createAntiAliasBitmap(obj, obj.getWidth());
            }
            bitmap = obj;
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("decodeThumbnail(): thumbnail = ");
                sb.append(obj);
                CamLog.d(sb.toString());
                bitmap = obj;
            }
        }
        return bitmap;
    }
    
    private String generatePathSelection(final String... array) {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append("_data");
        sb.append(" like '");
        for (final String str : array) {
            if (!str.startsWith("/")) {
                sb.append('/');
            }
            sb.append(str);
        }
        sb.append("'");
        sb.append(" AND ");
        sb.append("_data NOT LIKE '%/.%'");
        sb.append(")");
        return sb.toString();
    }
    
    private List<String> generateQueryPathForEachStorage(final List<String> list, final List<String> list2) {
        final Iterator<String> iterator = list2.iterator();
        while (iterator.hasNext()) {
            this.generateQueryPathForOneStorage(list, iterator.next());
        }
        return list;
    }
    
    private List<String> generateQueryPathForOneStorage(final List<String> list, final String s) {
        for (final String str : this.CONTENT_EXTENSIONS) {
            final String directory_DCIM = Environment.DIRECTORY_DCIM;
            final StringBuilder sb = new StringBuilder();
            sb.append("%");
            sb.append(str);
            list.add(this.generatePathSelection(s, directory_DCIM, sb.toString()));
        }
        return list;
    }
    
    private Content.ContentsType getContentType(final Content.ContentInfo contentInfo) {
        if (contentInfo.mType == 1) {
            if (PredictiveCapturePathBuilder.isPredictiveCaptureImage(contentInfo.mOriginalPath)) {
                return Content.ContentsType.PREDICTIVE_CAPTURE;
            }
            if (contentInfo.mSomcType == 129 || contentInfo.mSomcType == 2) {
                return Content.ContentsType.BURST;
            }
            if (contentInfo.mSomcType == 130 || contentInfo.mSomcType == 4) {
                return Content.ContentsType.TIME_SHIFT;
            }
            if (contentInfo.mSomcType == 42) {
                return Content.ContentsType.SOUND_PHOTO;
            }
            return Content.ContentsType.PHOTO;
        }
        else {
            if (contentInfo.mType != 2) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Unsupported file type");
                }
                return Content.ContentsType.NONE;
            }
            if (contentInfo.mSomcType == 12) {
                return Content.ContentsType.TIME_SHIFT_VIDEO;
            }
            if (contentInfo.mSomcType == 11) {
                return Content.ContentsType.TIME_SHIFT_VIDEO_120F;
            }
            if (SlowMotionPathBuilder.isSuperSlowMotionVideo(contentInfo.mOriginalPath)) {
                return Content.ContentsType.SUPER_SLOW_MOTION_VIDEO;
            }
            if (SlowMotionPathBuilder.isSuperSlowShotVideo(contentInfo.mOriginalPath)) {
                return Content.ContentsType.SUPER_SLOW_SHOT_VIDEO;
            }
            if (SlowMotionPathBuilder.isStandardSlowMotionVideo(contentInfo.mOriginalPath)) {
                return Content.ContentsType.STANDARD_SLOW_MOTION_VIDEO;
            }
            if (SlowMotionPathBuilder.isHFRVideo(contentInfo.mOriginalPath)) {
                return Content.ContentsType.HIGH_FRAME_RATE_VIDEO;
            }
            if (contentInfo.mWidth < 3840 && contentInfo.mHeight < 3840) {
                if (contentInfo.mIsVideoHdr) {
                    return Content.ContentsType.HDR_VIDEO;
                }
                return Content.ContentsType.VIDEO;
            }
            else {
                if (contentInfo.mIsVideoHdr) {
                    return Content.ContentsType.HDR_VIDEO_4K;
                }
                return Content.ContentsType.VIDEO_4K;
            }
        }
    }
    
    private Cursor getCoverImageInfo(int somcType) {
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id", "somctype" };
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "_id", somcType);
        final Cursor crQuery = PhotoStackQueryHelper.crQuery(this.mResolver, DataLoader.EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (crQuery == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getCoverImageInfo: null");
            }
            return null;
        }
        if (!crQuery.moveToFirst()) {
            if (CamLog.VERBOSE) {
                CamLog.d("getCoverImageInfo: row: 0");
            }
            crQuery.close();
            return null;
        }
        if (CamLog.VERBOSE) {
            somcType = this.getSomcType(crQuery.getString(1));
            final StringBuilder sb = new StringBuilder();
            sb.append("getCoverImageInfo somcType: ");
            sb.append(somcType);
            CamLog.d(sb.toString());
        }
        return crQuery;
    }
    
    private int getGroupedImageCount(int count) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getGroupedImageCount bucketId : ");
            sb.append(count);
            CamLog.d(sb.toString());
        }
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "bucket_id" };
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "bucket_id", count);
        final Cursor crQuery = PhotoStackQueryHelper.crQuery(this.mResolver, DataLoader.EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (crQuery == null) {
            return 1;
        }
        count = crQuery.getCount();
        crQuery.close();
        return count;
    }
    
    private List<Long> getGroupedImageMediaID(final int i, final Content.ContentInfo contentInfo) {
        final ArrayList list = new ArrayList();
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "bucket_id", "_id" };
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "bucket_id", i);
        final Cursor crQuery = PhotoStackQueryHelper.crQuery(this.mResolver, DataLoader.EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (crQuery == null) {
            list.add(contentInfo.mId);
        }
        else {
            while (crQuery.moveToNext()) {
                list.add(Long.valueOf(crQuery.getString(crQuery.getColumnIndex("_id"))));
            }
        }
        crQuery.close();
        return list;
    }
    
    private Cursor getImagesInfo(final ArrayList<Uri> list) {
        final int int1 = Integer.parseInt(list.get(list.size() - 1).getLastPathSegment());
        final int int2 = Integer.parseInt(list.get(0).getLastPathSegment());
        final int min = Math.min(int1, int2);
        final int max = Math.max(int1, int2);
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id", "somctype" };
        crQueryParameter.where = String.format(Locale.US, "%s >= '%s' AND %s <= '%s'", "_id", min, "_id", max);
        final Cursor crQuery = PhotoStackQueryHelper.crQuery(this.mResolver, DataLoader.EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (crQuery == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getImagesInfo: null");
            }
            return null;
        }
        if (!crQuery.moveToFirst()) {
            if (CamLog.VERBOSE) {
                CamLog.d("getImagesInfo: row: 0");
            }
            crQuery.close();
            return null;
        }
        return crQuery;
    }
    
    private Cursor getLatestImageInfo() {
        final Cursor crQuery = PhotoStackQueryHelper.crQuery(this.mResolver, DataLoader.EXTENDED_FILES_CONTENT_URI, this.mParam);
        if (crQuery == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getLatestImageInfo: null");
            }
            return null;
        }
        if (!crQuery.moveToFirst()) {
            if (CamLog.VERBOSE) {
                CamLog.d("getLatestImageInfo: row: 0");
            }
            crQuery.close();
            return null;
        }
        return crQuery;
    }
    
    private int getMediaId(final Cursor cursor) {
        final int int1 = cursor.getInt(0);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getMediaId: ");
            sb.append(int1);
            CamLog.d(sb.toString());
        }
        return int1;
    }
    
    private String getMediaPath(final long n, final int n2) {
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        Uri uri = null;
        switch (n2) {
            default: {
                return null;
            }
            case 2: {
                uri = MediaStore$Video$Media.EXTERNAL_CONTENT_URI;
                crQueryParameter.projection = new String[] { "_data" };
                crQueryParameter.where = String.format(Locale.US, "%s=%s", "_id", n);
                crQueryParameter.offset = 0;
                crQueryParameter.limit = 1;
                break;
            }
            case 1:
            case 3: {
                uri = MediaStore$Images$Media.EXTERNAL_CONTENT_URI;
                crQueryParameter.projection = new String[] { "_data" };
                crQueryParameter.where = String.format(Locale.US, "%s=%s", "_id", n);
                crQueryParameter.offset = 0;
                crQueryParameter.limit = 1;
                break;
            }
        }
        final Cursor crQuery = PhotoStackQueryHelper.crQuery(this.mResolver, uri, crQueryParameter);
        if (crQuery == null) {
            return null;
        }
        try {
            try {
                if (crQuery.moveToPosition(0)) {
                    final String string = crQuery.getString(0);
                    crQuery.close();
                    return string;
                }
                crQuery.close();
                return null;
            }
            finally {}
        }
        catch (final RuntimeException ex) {
            CamLog.e("The specified column isn't found.");
            crQuery.close();
            return null;
        }
        crQuery.close();
    }
    
    private Cursor getPredictiveCaptureImageInfo(final String str, final int i) {
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id" };
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC", "title");
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("(_data REGEXP '.*/DSCPDC_\\d{4}_BURST");
        sb2.append(str);
        sb2.append("(|_");
        sb2.append("COVER");
        sb2.append(").[jJ][pP][eE]?[gG]')");
        sb.append(sb2.toString());
        sb.append(" AND ");
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("(bucket_id==");
        sb3.append(i);
        sb3.append(")");
        sb.append(sb3.toString());
        crQueryParameter.where = sb.toString();
        final Cursor crQuery = PhotoStackQueryHelper.crQuery(this.mResolver, DataLoader.EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (crQuery == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getPredictiveCaptureImageInfo: null");
            }
            return null;
        }
        if (!crQuery.moveToFirst()) {
            if (CamLog.VERBOSE) {
                CamLog.d("getPredictiveCaptureImageInfo: row: 0");
            }
            crQuery.close();
            return null;
        }
        return crQuery;
    }
    
    private int getSomcType(final String str) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSomcType path : ");
            sb.append(str);
            CamLog.d(sb.toString());
        }
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "_data", "somctype" };
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "_data", str);
        final Cursor crQuery = PhotoStackQueryHelper.crQuery(this.mResolver, DataLoader.EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (crQuery == null) {
            return 0;
        }
        int int1;
        if (crQuery.moveToFirst()) {
            int1 = crQuery.getInt(crQuery.getColumnIndex("somctype"));
        }
        else {
            int1 = 0;
        }
        crQuery.close();
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("somcType = ");
            sb2.append(int1);
            CamLog.d(sb2.toString());
        }
        return int1;
    }
    
    private boolean isSupportedFileExtension(final String s, final String s2) {
        return s2 != null && s.toUpperCase().equals(s2.toUpperCase());
    }
    
    private boolean isVideoHdr(String crQuery) {
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "_data", "is_hdr" };
        final Locale us = Locale.US;
        final boolean b = false;
        crQueryParameter.sortOrder = String.format(us, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "_data", crQuery);
        crQuery = (String)PhotoStackQueryHelper.crQuery(this.mResolver, DataLoader.EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        boolean b2 = b;
        if (crQuery != null) {
            b2 = b;
            try {
                if (((Cursor)crQuery).moveToFirst()) {
                    final int int1 = ((Cursor)crQuery).getInt(((Cursor)crQuery).getColumnIndex("is_hdr"));
                    b2 = b;
                    if (int1 == 1) {
                        b2 = true;
                    }
                }
            }
            finally {
                if (crQuery != null) {
                    ((Cursor)crQuery).close();
                }
            }
        }
        if (crQuery != null) {
            ((Cursor)crQuery).close();
        }
        return b2;
    }
    
    private CrQueryParameter setupQueryParam(final List<String> list, int i) {
        final boolean verbose = CamLog.VERBOSE;
        final int n = 0;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setupQueryParam() has been called. offset = ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        final ArrayList list2 = new ArrayList<String>();
        this.generateQueryPathForEachStorage(list2, list);
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id" };
        crQueryParameter.offset = i;
        crQueryParameter.limit = 1;
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("(somctype!=129)");
        sb2.append(" AND (media_type==1 OR media_type==3)");
        sb2.append(" AND ");
        sb2.append("(somctype!=130)");
        sb2.append(" AND (");
        for (i = n; i < list2.size(); ++i) {
            if (i != 0) {
                sb2.append(" OR ");
            }
            sb2.append((String)list2.get(i));
        }
        sb2.append(")");
        crQueryParameter.where = sb2.toString();
        return crQueryParameter;
    }
    
    @Override
    public Long call() throws Exception {
        if (CamLog.VERBOSE) {
            CamLog.d("call() has been called.");
        }
        long mId = 0L;
        final LinkedList list = new LinkedList();
        Cursor cursor;
        if (this.mParam != null) {
            cursor = this.getLatestImageInfo();
        }
        else if (this.mMediaUris != null) {
            cursor = this.getImagesInfo(this.mMediaUris);
        }
        else {
            cursor = this.getCoverImageInfo(this.mMediaId);
        }
        boolean b = false;
        final Bitmap bitmap = null;
        final Throwable exception = null;
        Label_0406: {
            if (cursor == null) {
                break Label_0406;
            }
            Throwable t2 = null;
            try {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("cursor count = ");
                    sb.append(cursor.getCount());
                    CamLog.d(sb.toString());
                }
                if (this.mMediaUris != null) {
                    while (!cursor.isAfterLast()) {
                        final Content.ContentInfo contentInfoForMediaUris = this.createContentInfoForMediaUris(cursor);
                        if (contentInfoForMediaUris != null) {
                            list.addLast(contentInfoForMediaUris);
                        }
                        cursor.moveToNext();
                    }
                    break Label_0406;
                }
                Content.ContentInfo contentInfo = this.createContentInfo(cursor);
                Content.ContentInfo e;
                if ((e = contentInfo) != null) {
                    e = contentInfo;
                    if (PredictiveCapturePathBuilder.isPredictiveCaptureImage(contentInfo.mOriginalPath)) {
                        final Cursor predictiveCaptureImageInfo = this.getPredictiveCaptureImageInfo(PredictiveCapturePathBuilder.getTimeStamp(contentInfo.mOriginalPath), contentInfo.mBucketId);
                        Label_0314: {
                            if (predictiveCaptureImageInfo != null) {
                                try {
                                    this.createContentInfo(predictiveCaptureImageInfo);
                                    break Label_0314;
                                }
                                catch (final Throwable contentInfo) {
                                    try {
                                        throw contentInfo;
                                    }
                                    finally {}
                                }
                                finally {
                                    contentInfo = null;
                                }
                                if (predictiveCaptureImageInfo != null) {
                                    if (contentInfo != null) {
                                        try {
                                            predictiveCaptureImageInfo.close();
                                        }
                                        catch (final Throwable exception) {
                                            ((Throwable)contentInfo).addSuppressed(exception);
                                        }
                                    }
                                    else {
                                        predictiveCaptureImageInfo.close();
                                    }
                                }
                                throw;
                            }
                        }
                        e = contentInfo;
                        if (predictiveCaptureImageInfo != null) {
                            predictiveCaptureImageInfo.close();
                            e = contentInfo;
                        }
                    }
                }
                if (e != null) {
                    list.addLast(e);
                    break Label_0406;
                }
                break Label_0406;
            }
            catch (final Throwable t2) {
                try {
                    throw t2;
                }
                finally {}
            }
            finally {
                t2 = null;
            }
            Label_0403: {
                if (cursor == null) {
                    break Label_0403;
                }
                Label_0396: {
                    if (t2 == null) {
                        break Label_0396;
                    }
                    try {
                        final Throwable t3;
                        try {
                            cursor.close();
                            throw t3;
                        }
                        catch (final Throwable exception2) {
                            t2.addSuppressed(exception2);
                            throw t3;
                        }
                        cursor.close();
                        throw t3;
                        iftrue(Label_0436:)(cursor == null);
                        cursor.close();
                    }
                    finally {
                        this.mParam = null;
                        this.mMediaUris = null;
                    }
                }
            }
        }
        Label_0436: {
            this.mParam = null;
        }
        this.mMediaUris = null;
        Bitmap decodeThumbnail = bitmap;
        if (!list.isEmpty()) {
            mId = ((Content.ContentInfo)list.getLast()).mId;
            decodeThumbnail = (Bitmap)exception;
            if (((Content.ContentInfo)list.getLast()).mIsContainDetails) {
                decodeThumbnail = this.decodeThumbnail(list.getLast());
                if (decodeThumbnail != null) {
                    ((Content.ContentInfo)list.getLast()).mIsMediaDataVerified = true;
                }
            }
            b = true;
        }
        if (b) {
            this.mDataLoadCallback.onDataLoadCompleted(this.mRequestId, this.mIsRegisterCache, list, decodeThumbnail);
        }
        else {
            this.mDataLoadCallback.onDataLoadFailed(this.mRequestId);
        }
        return mId;
    }
    
    public interface DataLoadCallback
    {
        void onDataLoaded(final boolean p0, final LinkedList<Content.ContentInfo> p1, final int p2, final boolean p3, final Bitmap p4);
    }
}
