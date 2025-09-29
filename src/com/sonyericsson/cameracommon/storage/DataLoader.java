package com.sonyericsson.cameracommon.storage;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import com.sonyericsson.cameracommon.constants.SomcFileTypeConstants;
import com.sonyericsson.cameracommon.contentsview.PhotoStackQueryHelper;
import com.sonyericsson.cameracommon.contentsview.QueryParameterAdapter;
import com.sonyericsson.cameracommon.contentsview.ThumbnailFactory;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.updator.CrQueryParameter;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonymobile.media.SomcMediaStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DataLoader implements Callable<Long> {
    private static final int COLUMN_INDEX_BUCKETID = 7;
    private static final int COLUMN_INDEX_DATA = 1;
    private static final int COLUMN_INDEX_HEIGHT = 5;
    private static final int COLUMN_INDEX_ID = 0;
    private static final int COLUMN_INDEX_MIME = 2;
    private static final int COLUMN_INDEX_ORIENTATION = 6;
    private static final int COLUMN_INDEX_WIDTH = 4;
    public static final String EXTENDED_FILES_COLUMN_ID = "files_id";
    public static final Uri EXTENDED_FILES_CONTENT_URI = SomcMediaStore.ExtendedFiles.getContentUri("external");
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

    public interface DataLoadCallback {
        void onDataLoaded(boolean z, LinkedList<Content.ContentInfo> linkedList, int i, boolean z2, Bitmap bitmap);
    }

    public DataLoader(Context context, List<String> list, int i, Storage.OnLoadCompletedListener onLoadCompletedListener, boolean z) {
        this.CONTENT_EXTENSIONS = new String[]{MediaSavingConstants.MEDIA_TYPE_JPEG_EXT, MediaSavingConstants.MEDIA_TYPE_3GP_EXT, MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT};
        this.mParam = null;
        this.mRequestId = -1;
        this.mParam = setupQueryParam(list, i);
        this.mContext = context;
        this.mResolver = this.mContext.getContentResolver();
        this.mDataLoadCallback = onLoadCompletedListener;
        this.mIsRegisterCache = z;
    }

    public DataLoader(Context context, List<String> list, int i, int i2, Storage.OnLoadCompletedListener onLoadCompletedListener, boolean z) {
        this.CONTENT_EXTENSIONS = new String[]{MediaSavingConstants.MEDIA_TYPE_JPEG_EXT, MediaSavingConstants.MEDIA_TYPE_3GP_EXT, MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT};
        this.mParam = null;
        this.mRequestId = i;
        this.mParam = setupQueryParam(list, i2);
        this.mContext = context;
        this.mResolver = this.mContext.getContentResolver();
        this.mDataLoadCallback = onLoadCompletedListener;
        this.mIsRegisterCache = z;
    }

    public DataLoader(int i, Uri uri, Context context, Storage.OnLoadCompletedListener onLoadCompletedListener, boolean z) {
        this.CONTENT_EXTENSIONS = new String[]{MediaSavingConstants.MEDIA_TYPE_JPEG_EXT, MediaSavingConstants.MEDIA_TYPE_3GP_EXT, MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT};
        this.mParam = null;
        this.mRequestId = i;
        try {
            this.mMediaId = Integer.parseInt(uri.getLastPathSegment());
        } catch (Exception unused) {
            CamLog.w("mediaId is not corrected.");
        }
        this.mContext = context;
        this.mResolver = this.mContext.getContentResolver();
        this.mDataLoadCallback = onLoadCompletedListener;
        this.mIsRegisterCache = z;
    }

    public DataLoader(Context context, ArrayList<Uri> arrayList, Storage.OnLoadCompletedListener onLoadCompletedListener, boolean z) {
        this.CONTENT_EXTENSIONS = new String[]{MediaSavingConstants.MEDIA_TYPE_JPEG_EXT, MediaSavingConstants.MEDIA_TYPE_3GP_EXT, MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT};
        this.mParam = null;
        this.mRequestId = -1;
        this.mContext = context;
        this.mResolver = this.mContext.getContentResolver();
        this.mMediaUris = arrayList;
        this.mDataLoadCallback = onLoadCompletedListener;
        this.mIsRegisterCache = z;
    }

	@Override // java.util.concurrent.Callable
	public java.lang.Long call() throws java.lang.Exception {
		if (CamLog.VERBOSE) {
			CamLog.d(new String[] { "call() has been called." });
		}

		long lastId = 0L;
		LinkedList<Content.ContentInfo> list = new LinkedList<>();
		Cursor c;

		if (mParam != null) {
			c = getLatestImageInfo();
		} else if (mMediaUris != null) {
			c = getImagesInfo(mMediaUris);
		} else {
			c = getCoverImageInfo(mMediaId);
		}

		Bitmap thumbnail = null;
		boolean hasData = false;

		try {
			if (c != null) {
				if (CamLog.VERBOSE) {
					CamLog.d(new String[] { "cursor count = " + c.getCount() });
				}

				if (mMediaUris != null) {
					// iterate all rows from provided URIs
					while (!c.isAfterLast()) {
						Content.ContentInfo info =
								createContentInfoForMediaUris(c);
						if (info != null) {
							list.addLast(info);
						}
						c.moveToNext();
					}
				} else {
					// single/latest item path (with predictive-capture follow-up)
					Content.ContentInfo info =
							createContentInfo(c);

					if (info != null
							&& PredictiveCapturePathBuilder.isPredictiveCaptureImage(info.mOriginalPath)) {

						String ts = PredictiveCapturePathBuilder.getTimeStamp(info.mOriginalPath);
						Cursor pc = getPredictiveCaptureImageInfo(ts, info.mBucketId);
						if (pc != null) {
							try {
								info = createContentInfo(pc);
							} finally {
								pc.close();
							}
						}
					}

					if (info != null) {
						list.addLast(info);
					}
				}
			}
		} finally {
			try {
				if (c != null) c.close();
			} catch (Throwable t) {
				// mirror fallback: clear params then rethrow
				mParam = null;
				mMediaUris = null;
				throw t;
			}
		}

		// clear query params as in fallback
		mParam = null;
		mMediaUris = null;

		if (!list.isEmpty()) {
			Content.ContentInfo last = list.getLast();
			lastId = last.mId;

			if (last.mIsContainDetails) {
				Bitmap b = decodeThumbnail(last);
				if (b != null) {
					last.mIsMediaDataVerified = true;
				}
				thumbnail = b;
			}
			hasData = true;
		}

		if (hasData) {
			mDataLoadCallback.onDataLoadCompleted(mRequestId, mIsRegisterCache, list, thumbnail);
		} else {
			mDataLoadCallback.onDataLoadFailed(mRequestId);
		}

		return java.lang.Long.valueOf(lastId);
	}

    private CrQueryParameter setupQueryParam(List<String> list, int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("setupQueryParam() has been called. offset = " + i);
        }
        ArrayList arrayList = new ArrayList();
        generateQueryPathForEachStorage(arrayList, list);
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id"};
        crQueryParameter.offset = i;
        crQueryParameter.limit = 1;
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        StringBuilder sb = new StringBuilder();
        sb.append("(somctype!=129)");
        sb.append(" AND (media_type==1 OR media_type==3)");
        sb.append(" AND ");
        sb.append("(somctype!=130)");
        sb.append(" AND (");
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (i2 != 0) {
                sb.append(" OR ");
            }
            sb.append((String) arrayList.get(i2));
        }
        sb.append(")");
        crQueryParameter.where = sb.toString();
        return crQueryParameter;
    }

    private List<String> generateQueryPathForEachStorage(List<String> list, List<String> list2) {
        Iterator<String> it = list2.iterator();
        while (it.hasNext()) {
            generateQueryPathForOneStorage(list, it.next());
        }
        return list;
    }

    private List<String> generateQueryPathForOneStorage(List<String> list, String str) {
        for (String str2 : this.CONTENT_EXTENSIONS) {
            list.add(generatePathSelection(str, Environment.DIRECTORY_DCIM, "%" + str2));
        }
        return list;
    }

    private String generatePathSelection(String... strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append("_data");
        sb.append(" like '");
        for (String str : strArr) {
            if (!str.startsWith(SharedPrefsTranslator.CONNECTOR_SLASH)) {
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

    private Content.ContentInfo createContentInfo(Cursor cursor, boolean z) {
        Uri uriWithAppendedPath;
        int mediaId = getMediaId(cursor);
        int i = 2;
        String string = cursor.getString(2);
        String string2 = cursor.getString(1);
        int i2 = cursor.getInt(4);
        int i3 = cursor.getInt(5);
        int i4 = cursor.getInt(7);
        String fileExtension = CommonUtility.getFileExtension(string2);
        if (string.equals(MediaSavingConstants.MEDIA_TYPE_JPEG_MIME) || isSupportedFileExtension(MediaSavingConstants.MEDIA_TYPE_JPEG_EXT, fileExtension)) {
            uriWithAppendedPath = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(mediaId));
            i = 1;
        } else if (string.equals(MediaSavingConstants.MEDIA_TYPE_MPEG4_MIME) || isSupportedFileExtension(MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT, fileExtension) || string.equals(MediaSavingConstants.MEDIA_TYPE_3GP_MIME) || isSupportedFileExtension(MediaSavingConstants.MEDIA_TYPE_3GP_EXT, fileExtension)) {
            uriWithAppendedPath = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(mediaId));
        } else if (string.equals(MediaSavingConstants.MEDIA_TYPE_MPO_MIME)) {
            uriWithAppendedPath = Uri.withAppendedPath(QueryParameterAdapter.MPO_3DPICTURES_CONTENT_URI, String.valueOf(mediaId));
            i = 3;
        } else {
            if (!CamLog.VERBOSE) {
                return null;
            }
            CamLog.d("query error : mime = " + string);
            return null;
        }
        int i5 = i == 1 ? cursor.getInt(6) : 0;
        Content.ContentInfo contentInfo = new Content.ContentInfo();
        contentInfo.mId = mediaId;
        contentInfo.mOriginalUri = uriWithAppendedPath;
        contentInfo.mOriginalPath = string2;
        contentInfo.mType = i;
        contentInfo.mWidth = i2;
        contentInfo.mHeight = i3;
        contentInfo.mOrientation = i5;
        contentInfo.mMimeType = string;
        contentInfo.mBucketId = i4;
        contentInfo.mIsContainDetails = z;
        if (z) {
            contentInfo.mGroupedImage = getGroupedImageCount(i4);
            contentInfo.mSomcType = getSomcType(string2);
            contentInfo.mIsVideoHdr = isVideoHdr(string2);
            contentInfo.mContentType = getContentType(contentInfo);
            if (contentInfo.mContentType == Content.ContentsType.BURST) {
                contentInfo.mMediaStoreIds = getGroupedImageMediaID(i4, contentInfo);
            }
        }
        return contentInfo;
    }

    private List<Long> getGroupedImageMediaID(int i, Content.ContentInfo contentInfo) {
        ArrayList arrayList = new ArrayList();
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"bucket_id", "_id"};
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "bucket_id", Integer.valueOf(i));
        Cursor cursorCrQuery = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursorCrQuery == null) {
            arrayList.add(Long.valueOf(contentInfo.mId));
        } else {
            while (cursorCrQuery.moveToNext()) {
                arrayList.add(Long.valueOf(Long.valueOf(cursorCrQuery.getString(cursorCrQuery.getColumnIndex("_id"))).longValue()));
            }
        }
        cursorCrQuery.close();
        return arrayList;
    }

    private Content.ContentInfo createContentInfo(Cursor cursor) {
        return createContentInfo(cursor, true);
    }

    private Content.ContentInfo createContentInfoForMediaUris(Cursor cursor) {
        return createContentInfo(cursor, false);
    }

    private int getMediaId(Cursor cursor) {
        int i = cursor.getInt(0);
        if (CamLog.VERBOSE) {
            CamLog.d("getMediaId: " + i);
        }
        return i;
    }

    private int getGroupedImageCount(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("getGroupedImageCount bucketId : " + i);
        }
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"bucket_id"};
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "bucket_id", Integer.valueOf(i));
        Cursor cursorCrQuery = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursorCrQuery == null) {
            return 1;
        }
        int count = cursorCrQuery.getCount();
        cursorCrQuery.close();
        return count;
    }

    private int getSomcType(String str) {
        if (CamLog.VERBOSE) {
            CamLog.d("getSomcType path : " + str);
        }
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_data", SomcMediaStore.ExtendedFiles.ExtendedFileColumns.SOMC_FILE_TYPE};
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "_data", str);
        Cursor cursorCrQuery = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursorCrQuery == null) {
            return 0;
        }
        int i = cursorCrQuery.moveToFirst() ? cursorCrQuery.getInt(cursorCrQuery.getColumnIndex(SomcMediaStore.ExtendedFiles.ExtendedFileColumns.SOMC_FILE_TYPE)) : 0;
        cursorCrQuery.close();
        if (CamLog.VERBOSE) {
            CamLog.d("somcType = " + i);
        }
        return i;
    }

    private boolean isVideoHdr(String str) {
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_data", SomcFileTypeConstants.IS_HDR};
        boolean z = false;
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "_data", str);
        Cursor cursorCrQuery = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursorCrQuery != null) {
            try {
                if (cursorCrQuery.moveToFirst()) {
                    if (cursorCrQuery.getInt(cursorCrQuery.getColumnIndex(SomcFileTypeConstants.IS_HDR)) == 1) {
                        z = true;
                    }
                }
            } finally {
                if (cursorCrQuery != null) {
                    cursorCrQuery.close();
                }
            }
        }
        return z;
    }

    private Content.ContentsType getContentType(Content.ContentInfo contentInfo) {
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
        if (contentInfo.mType == 2) {
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
            if (contentInfo.mWidth >= 3840 || contentInfo.mHeight >= 3840) {
                if (contentInfo.mIsVideoHdr) {
                    return Content.ContentsType.HDR_VIDEO_4K;
                }
                return Content.ContentsType.VIDEO_4K;
            }
            if (contentInfo.mIsVideoHdr) {
                return Content.ContentsType.HDR_VIDEO;
            }
            return Content.ContentsType.VIDEO;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Unsupported file type");
        }
        return Content.ContentsType.NONE;
    }

    private Bitmap decodeThumbnail(Content.ContentInfo contentInfo) {
        if (CamLog.VERBOSE) {
            CamLog.d("decodeThumbnail() has been called.");
        }
        Bitmap bitmapCreateAntiAliasBitmap = null;
        if (contentInfo != null) {
            if (contentInfo.mOriginalPath == null) {
                contentInfo.mOriginalPath = getMediaPath(contentInfo.mId, contentInfo.mType);
            }
            Bitmap bitmapCreateMicroThumbnail = ThumbnailFactory.createMicroThumbnail(contentInfo);
            bitmapCreateAntiAliasBitmap = bitmapCreateMicroThumbnail != null ? createAntiAliasBitmap(bitmapCreateMicroThumbnail, bitmapCreateMicroThumbnail.getWidth()) : bitmapCreateMicroThumbnail;
            if (CamLog.VERBOSE) {
                CamLog.d("decodeThumbnail(): thumbnail = " + bitmapCreateAntiAliasBitmap);
            }
        }
        return bitmapCreateAntiAliasBitmap;
    }

    private Bitmap createAntiAliasBitmap(Bitmap bitmap, int i) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, i, i, (Matrix) null, true);
    }

    private String getMediaPath(long j, int i) {
        Uri uri;
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        switch (i) {
            case 1:
            case 3:
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                crQueryParameter.projection = new String[]{"_data"};
                crQueryParameter.where = String.format(Locale.US, "%s=%s", "_id", Long.valueOf(j));
                crQueryParameter.offset = 0;
                crQueryParameter.limit = 1;
                break;
            case 2:
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                crQueryParameter.projection = new String[]{"_data"};
                crQueryParameter.where = String.format(Locale.US, "%s=%s", "_id", Long.valueOf(j));
                crQueryParameter.offset = 0;
                crQueryParameter.limit = 1;
                break;
            default:
                return null;
        }
        Cursor cursorCrQuery = PhotoStackQueryHelper.crQuery(this.mResolver, uri, crQueryParameter);
        try {
            if (cursorCrQuery == null) {
                return null;
            }
            if (cursorCrQuery.moveToPosition(0)) {
                return cursorCrQuery.getString(0);
            }
            return null;
        } catch (RuntimeException unused) {
            CamLog.e("The specified column isn't found.");
            return null;
        } finally {
            cursorCrQuery.close();
        }
    }

    private Cursor getPredictiveCaptureImageInfo(String str, int i) {
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id"};
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC", "title");
        StringBuilder sb = new StringBuilder();
        sb.append("(_data REGEXP '.*/DSCPDC_\\d{4}_BURST" + str + "(|_" + PredictiveCapturePathBuilder.DCF_FILE_NAME_FREE_WORD_COVER + ").[jJ][pP][eE]?[gG]')");
        sb.append(" AND ");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("(bucket_id==");
        sb2.append(i);
        sb2.append(")");
        sb.append(sb2.toString());
        crQueryParameter.where = sb.toString();
        Cursor cursorCrQuery = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursorCrQuery == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getPredictiveCaptureImageInfo: null");
            }
            return null;
        }
        if (cursorCrQuery.moveToFirst()) {
            return cursorCrQuery;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getPredictiveCaptureImageInfo: row: 0");
        }
        cursorCrQuery.close();
        return null;
    }

    private Cursor getLatestImageInfo() {
        Cursor cursorCrQuery = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, this.mParam);
        if (cursorCrQuery == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getLatestImageInfo: null");
            }
            return null;
        }
        if (cursorCrQuery.moveToFirst()) {
            return cursorCrQuery;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getLatestImageInfo: row: 0");
        }
        cursorCrQuery.close();
        return null;
    }

    private Cursor getCoverImageInfo(int i) {
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id", SomcMediaStore.ExtendedFiles.ExtendedFileColumns.SOMC_FILE_TYPE};
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "_id", Integer.valueOf(i));
        Cursor cursorCrQuery = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursorCrQuery == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getCoverImageInfo: null");
            }
            return null;
        }
        if (!cursorCrQuery.moveToFirst()) {
            if (CamLog.VERBOSE) {
                CamLog.d("getCoverImageInfo: row: 0");
            }
            cursorCrQuery.close();
            return null;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getCoverImageInfo somcType: " + getSomcType(cursorCrQuery.getString(1)));
        }
        return cursorCrQuery;
    }

    private Cursor getImagesInfo(ArrayList<Uri> arrayList) throws NumberFormatException {
        int i = Integer.parseInt(arrayList.get(arrayList.size() - 1).getLastPathSegment());
        int i2 = Integer.parseInt(arrayList.get(0).getLastPathSegment());
        int iMin = Math.min(i, i2);
        int iMax = Math.max(i, i2);
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_id", "_data", "mime_type", "datetaken", "width", "height", "orientation", "bucket_id", SomcMediaStore.ExtendedFiles.ExtendedFileColumns.SOMC_FILE_TYPE};
        crQueryParameter.where = String.format(Locale.US, "%s >= '%s' AND %s <= '%s'", "_id", Integer.valueOf(iMin), "_id", Integer.valueOf(iMax));
        Cursor cursorCrQuery = PhotoStackQueryHelper.crQuery(this.mResolver, EXTENDED_FILES_CONTENT_URI, crQueryParameter);
        if (cursorCrQuery == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getImagesInfo: null");
            }
            return null;
        }
        if (cursorCrQuery.moveToFirst()) {
            return cursorCrQuery;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getImagesInfo: row: 0");
        }
        cursorCrQuery.close();
        return null;
    }

    private boolean isSupportedFileExtension(String str, String str2) {
        return str2 != null && str.toUpperCase().equals(str2.toUpperCase());
    }
}
