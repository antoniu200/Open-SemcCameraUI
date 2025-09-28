package com.sonyericsson.cameracommon.contentsview;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.contentsview.contents.ContentFactory;
import com.sonyericsson.cameracommon.mediasaving.updator.CrQueryParameter;
import com.sonyericsson.cameracommon.storage.DataLoader;
import com.sonyericsson.cameracommon.storage.Storage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ContentLoader {
    private static final int MAX_LOCAL_CACHE_NUM = 400;
    public static final float PANORAMA_ASPECT_THRESHOLD = 1.8777778f;
    public static final String TAG = "ContentLoader";
    private final ContentCreationCallback mContentCallback;
    private DataLoader.DataLoadCallback mDataCallback;
    private DataLoaderHander mHandler;
    private LinkedList<Content.ContentInfo> mLocalCacheBackup;
    private SecurityLevel mSecurityLevel;
    private Storage mStorage;
    private final int MEDIA_ID_COUNT_MAX = 400;
    private LinkedList<Content.ContentInfo> mLocalCache = new LinkedList<>();
    private Storage.OnLoadCompletedListener mOnLoadCompleteListener = new Storage.OnLoadCompletedListener() { // from class: com.sonyericsson.cameracommon.contentsview.ContentLoader.1
        @Override // com.sonyericsson.cameracommon.storage.Storage.OnLoadCompletedListener
        public void onLoadCompleted(Uri uri, Bitmap bitmap) {
        }

        @Override // com.sonyericsson.cameracommon.storage.Storage.OnLoadCompletedListener
        public void onLoadFailed(Uri uri, int i) {
        }

        @Override // com.sonyericsson.cameracommon.storage.Storage.OnLoadCompletedListener
        public void onDataLoadCompleted(int i, boolean z, LinkedList<Content.ContentInfo> linkedList, Bitmap bitmap) {
            ContentLoader.this.mDataCallback.onDataLoaded(true, linkedList, i, z, bitmap);
        }

        @Override // com.sonyericsson.cameracommon.storage.Storage.OnLoadCompletedListener
        public void onDataLoadFailed(int i) {
            ContentLoader.this.mDataCallback.onDataLoaded(false, null, i, false, null);
        }
    };

    interface ContentCreationCallback {
        void onContentCreated(int i, Content content, Bitmap bitmap);

        void onNoContentLoaded();
    }

    public enum SecurityLevel {
        NORMAL,
        NEWLY_ADDED_CONTENT_ONLY
    }

    public ContentLoader(Storage storage, SecurityLevel securityLevel, ContentCreationCallback contentCreationCallback) {
        this.mDataCallback = new DataCallback();
        this.mSecurityLevel = securityLevel;
        this.mStorage = storage;
        this.mContentCallback = contentCreationCallback;
        this.mHandler = new DataLoaderHander();
    }

    public void pause() {
        if (CamLog.VERBOSE) {
            CamLog.d("pause() has been called.");
        }
        this.mStorage.cancelDataLoad(false);
        clearLocalCache();
    }

    public void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("release() has been called.");
        }
        clearLocalCache();
        this.mLocalCache = null;
        this.mDataCallback = null;
        this.mHandler.removeAllMessages();
    }

    public void request(int i, Uri uri) {
        if (CamLog.VERBOSE) {
            CamLog.d("request() has been called.");
            CamLog.d("  requestId:" + i);
            CamLog.d("  uri:" + uri.toString());
        }
        this.mStorage.requestDataLoad(i, uri, true, this.mOnLoadCompleteListener);
    }

    public void requestLastDataLoad(int i) {
        this.mStorage.requestLastDataLoad(i, true, this.mOnLoadCompleteListener);
    }

    public void requestCreateContentInfoSync(ArrayList<Uri> arrayList) {
        this.mStorage.requestCreateContentInfoSync(arrayList, this.mOnLoadCompleteListener);
    }

    public void reload(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("reload() has been called.");
        }
        if (AnonymousClass2.$SwitchMap$com$sonyericsson$cameracommon$contentsview$ContentLoader$SecurityLevel[this.mSecurityLevel.ordinal()] == 1) {
            if (CamLog.VERBOSE) {
                CamLog.d("reload() : SecurityLevel = NEWLY_ADDED_CONTENT_ONLY ");
            }
            LinkedList<Content.ContentInfo> localCache = getLocalCache();
            if (localCache == null || localCache.size() <= 0) {
                return;
            }
            request(-1, localCache.getFirst().mOriginalUri);
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("reload() : SecurityLevel = NORMAL");
        }
        for (int i2 = 0; i2 < i; i2++) {
            this.mStorage.requestDataLoad(i2, false, this.mOnLoadCompleteListener);
        }
    }

    /* renamed from: com.sonyericsson.cameracommon.contentsview.ContentLoader$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$cameracommon$contentsview$ContentLoader$SecurityLevel = new int[SecurityLevel.values().length];

        static {
            try {
                $SwitchMap$com$sonyericsson$cameracommon$contentsview$ContentLoader$SecurityLevel[SecurityLevel.NEWLY_ADDED_CONTENT_ONLY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFuture(long j) {
        this.mStorage.cancelDataLoad(j);
    }

    private class DataLoadResult {
        private Bitmap mBitmap;
        private Content mContent;

        public DataLoadResult(Content content, Bitmap bitmap) {
            this.mContent = content;
            this.mBitmap = bitmap;
        }
    }

    private class DataLoaderHander extends Handler {
        private static final int NOTIFY_CONTENT_CREATED = 1;
        private static final int NOTIFY_NO_CONTENT_LOADED = 0;

        private DataLoaderHander() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    ContentLoader.this.mContentCallback.onNoContentLoaded();
                    break;
                case 1:
                    if (CamLog.VERBOSE) {
                        CamLog.d("handleMessage for content creation.");
                    }
                    int i = message.arg1;
                    DataLoadResult dataLoadResult = (DataLoadResult) message.obj;
                    ContentLoader.this.removeFuture(dataLoadResult.mContent.getContentInfo().mId);
                    ContentLoader.this.mContentCallback.onContentCreated(i, dataLoadResult.mContent, dataLoadResult.mBitmap);
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyContentLoaded(int i, DataLoadResult dataLoadResult) {
            if (CamLog.VERBOSE) {
                CamLog.d("notifyContentLoaded() has been called.");
            }
            Message messageObtain = Message.obtain(this);
            if (dataLoadResult != null) {
                messageObtain.what = 1;
                messageObtain.obj = dataLoadResult;
            } else {
                messageObtain.what = 0;
            }
            messageObtain.arg1 = i;
            sendMessage(messageObtain);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeAllMessages() {
            removeMessages(1);
            removeMessages(0);
        }
    }

    private class DataCallback implements DataLoader.DataLoadCallback {
        private DataCallback() {
        }

        @Override // com.sonyericsson.cameracommon.storage.DataLoader.DataLoadCallback
        public void onDataLoaded(boolean z, LinkedList<Content.ContentInfo> linkedList, int i, boolean z2, Bitmap bitmap) {
            if (CamLog.VERBOSE) {
                CamLog.d("onDataLoaded() has been called. result = " + z + " , requestId = " + i);
            }
            if (linkedList != null && !linkedList.isEmpty() && z) {
                if (z2) {
                    ContentLoader.this.addLocalCache(linkedList);
                }
                if (linkedList.getLast().mIsContainDetails) {
                    ContentLoader.this.mHandler.notifyContentLoaded(i, ContentLoader.this.new DataLoadResult(ContentFactory.create(linkedList.getLast()), bitmap));
                    return;
                } else {
                    ContentLoader.this.request(i, linkedList.getLast().mOriginalUri);
                    return;
                }
            }
            CamLog.w("Loading data is failed.");
            ContentLoader.this.mHandler.notifyContentLoaded(i, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addLocalCache(LinkedList<Content.ContentInfo> linkedList) {
        if (this.mLocalCacheBackup != null) {
            if (!this.mLocalCacheBackup.isEmpty() && linkedList.size() == 1 && linkedList.getFirst().mId == this.mLocalCacheBackup.getFirst().mId) {
                this.mLocalCacheBackup.set(0, linkedList.getFirst());
            } else {
                Iterator<Content.ContentInfo> it = linkedList.iterator();
                while (it.hasNext()) {
                    this.mLocalCacheBackup.addFirst(it.next());
                    if (overLimitSize(this.mLocalCacheBackup)) {
                        this.mLocalCacheBackup.removeLast();
                    }
                }
            }
        }
        if (!this.mLocalCache.isEmpty() && linkedList.size() == 1 && linkedList.getFirst().mId == this.mLocalCache.getFirst().mId) {
            this.mLocalCache.set(0, linkedList.getFirst());
            return;
        }
        Iterator<Content.ContentInfo> it2 = linkedList.iterator();
        while (it2.hasNext()) {
            this.mLocalCache.addFirst(it2.next());
            if (overLimitSize(this.mLocalCache)) {
                this.mLocalCache.removeLast();
            }
        }
    }

    private boolean overLimitSize(LinkedList<Content.ContentInfo> linkedList) {
        if (linkedList.size() > 400) {
            return true;
        }
        Iterator<Content.ContentInfo> it = linkedList.iterator();
        int i = 0;
        while (it.hasNext()) {
            Content.ContentInfo next = it.next();
            if (next.mContentType == Content.ContentsType.BURST && next.mGroupedImage > 0) {
                i += next.mGroupedImage;
            } else {
                i = next.mContentType == Content.ContentsType.PREDICTIVE_CAPTURE ? i + next.mPredictiveNum : i + 1;
            }
        }
        return i > 400;
    }

    private void clearLocalCache() {
        if (this.mLocalCache != null) {
            this.mLocalCache.clear();
        }
    }

    public LinkedList<Content.ContentInfo> getLocalCache() {
        return this.mLocalCache;
    }

    public void saveLocalCache() {
        this.mLocalCacheBackup = new LinkedList<>(this.mLocalCache);
    }

    public void clearLocalCacheBackup() {
        if (this.mLocalCacheBackup != null) {
            this.mLocalCacheBackup.clear();
            this.mLocalCacheBackup = null;
        }
    }

    public void loadLocalCache() {
        if (this.mLocalCacheBackup != null) {
            this.mLocalCache.clear();
            this.mLocalCache = new LinkedList<>(this.mLocalCacheBackup);
        }
    }

    public void removeInvalidLocalCache(Context context) {
        Iterator<Content.ContentInfo> it = this.mLocalCache.iterator();
        while (it.hasNext()) {
            Content.ContentInfo next = it.next();
            if (!new File(next.mOriginalPath).exists()) {
                it.remove();
            } else if (isRemovedFromDataBase(context, next.mId, next.mType)) {
                next.mIsInTrash = true;
                it.remove();
            }
        }
    }

    public boolean isRemovedFromDataBase(Context context, long j, int i) {
        Cursor cursorCrQuery;
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_id"};
        crQueryParameter.sortOrder = String.format(Locale.US, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "_id", Long.valueOf(j));
        if (i == 1 || i == 3) {
            cursorCrQuery = PhotoStackQueryHelper.crQuery(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, crQueryParameter);
        } else {
            cursorCrQuery = i == 2 ? PhotoStackQueryHelper.crQuery(context.getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, crQueryParameter) : null;
        }
        if (cursorCrQuery != null) {
            z = cursorCrQuery.getCount() == 0;
            cursorCrQuery.close();
        }
        return z;
    }

    public void reloadTopContent() {
        if (this.mLocalCache == null || this.mLocalCache.isEmpty() || this.mLocalCache.getFirst().mIsContainDetails) {
            return;
        }
        request(-1, this.mLocalCache.getFirst().mOriginalUri);
    }
}
