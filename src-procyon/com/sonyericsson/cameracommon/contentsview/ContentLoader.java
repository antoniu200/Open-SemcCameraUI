// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview;

import android.os.Message;
import android.os.Handler;
import com.sonyericsson.cameracommon.contentsview.contents.ContentFactory;
import java.util.ArrayList;
import java.io.File;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Collection;
import android.database.Cursor;
import android.provider.MediaStore$Images$Media;
import android.provider.MediaStore$Video$Media;
import java.util.Locale;
import com.sonyericsson.cameracommon.mediasaving.updator.CrQueryParameter;
import android.content.Context;
import java.util.Iterator;
import android.net.Uri;
import android.graphics.Bitmap;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import java.util.LinkedList;
import com.sonyericsson.cameracommon.storage.DataLoader;

public class ContentLoader
{
    private static final int MAX_LOCAL_CACHE_NUM = 400;
    public static final float PANORAMA_ASPECT_THRESHOLD = 1.8777778f;
    public static final String TAG = "ContentLoader";
    private final int MEDIA_ID_COUNT_MAX;
    private final ContentCreationCallback mContentCallback;
    private DataLoader.DataLoadCallback mDataCallback;
    private DataLoaderHander mHandler;
    private LinkedList<Content.ContentInfo> mLocalCache;
    private LinkedList<Content.ContentInfo> mLocalCacheBackup;
    private Storage.OnLoadCompletedListener mOnLoadCompleteListener;
    private SecurityLevel mSecurityLevel;
    private Storage mStorage;
    
    public ContentLoader(final Storage mStorage, final SecurityLevel mSecurityLevel, final ContentCreationCallback mContentCallback) {
        this.mDataCallback = new DataCallback();
        this.MEDIA_ID_COUNT_MAX = 400;
        this.mLocalCache = new LinkedList<Content.ContentInfo>();
        this.mOnLoadCompleteListener = new Storage.OnLoadCompletedListener() {
            final ContentLoader this$0;
            
            @Override
            public void onDataLoadCompleted(final int n, final boolean b, final LinkedList<Content.ContentInfo> list, final Bitmap bitmap) {
                this.this$0.mDataCallback.onDataLoaded(true, list, n, b, bitmap);
            }
            
            @Override
            public void onDataLoadFailed(final int n) {
                this.this$0.mDataCallback.onDataLoaded(false, null, n, false, null);
            }
            
            @Override
            public void onLoadCompleted(final Uri uri, final Bitmap bitmap) {
            }
            
            @Override
            public void onLoadFailed(final Uri uri, final int n) {
            }
        };
        this.mSecurityLevel = mSecurityLevel;
        this.mStorage = mStorage;
        this.mContentCallback = mContentCallback;
        this.mHandler = new DataLoaderHander();
    }
    
    private void addLocalCache(final LinkedList<Content.ContentInfo> list) {
        if (this.mLocalCacheBackup != null) {
            if (!this.mLocalCacheBackup.isEmpty() && list.size() == 1 && list.getFirst().mId == this.mLocalCacheBackup.getFirst().mId) {
                this.mLocalCacheBackup.set(0, list.getFirst());
            }
            else {
                final Iterator<Object> iterator = list.iterator();
                while (iterator.hasNext()) {
                    this.mLocalCacheBackup.addFirst(iterator.next());
                    if (this.overLimitSize(this.mLocalCacheBackup)) {
                        this.mLocalCacheBackup.removeLast();
                    }
                }
            }
        }
        if (!this.mLocalCache.isEmpty() && list.size() == 1 && list.getFirst().mId == this.mLocalCache.getFirst().mId) {
            this.mLocalCache.set(0, list.getFirst());
        }
        else {
            final Iterator iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                this.mLocalCache.addFirst((Content.ContentInfo)iterator2.next());
                if (this.overLimitSize(this.mLocalCache)) {
                    this.mLocalCache.removeLast();
                }
            }
        }
    }
    
    private void clearLocalCache() {
        if (this.mLocalCache != null) {
            this.mLocalCache.clear();
        }
    }
    
    private boolean overLimitSize(final LinkedList<Content.ContentInfo> list) {
        if (list.size() > 400) {
            return true;
        }
        final Iterator<Object> iterator = list.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final Content.ContentInfo contentInfo = iterator.next();
            if (contentInfo.mContentType == Content.ContentsType.BURST && contentInfo.mGroupedImage > 0) {
                n += contentInfo.mGroupedImage;
            }
            else if (contentInfo.mContentType == Content.ContentsType.PREDICTIVE_CAPTURE) {
                n += contentInfo.mPredictiveNum;
            }
            else {
                ++n;
            }
        }
        return n > 400;
    }
    
    private void removeFuture(final long n) {
        this.mStorage.cancelDataLoad(n);
    }
    
    public void clearLocalCacheBackup() {
        if (this.mLocalCacheBackup != null) {
            this.mLocalCacheBackup.clear();
            this.mLocalCacheBackup = null;
        }
    }
    
    public LinkedList<Content.ContentInfo> getLocalCache() {
        return this.mLocalCache;
    }
    
    public boolean isRemovedFromDataBase(final Context context, final long l, final int n) {
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "_id" };
        final Locale us = Locale.US;
        boolean b = true;
        final boolean b2 = true;
        crQueryParameter.sortOrder = String.format(us, "%s DESC, %s DESC", "datetaken", "_id");
        crQueryParameter.where = String.format(Locale.US, "%s like '%s'", "_id", l);
        Cursor cursor;
        if (n != 1 && n != 3) {
            if (n == 2) {
                cursor = PhotoStackQueryHelper.crQuery(context.getContentResolver(), MediaStore$Video$Media.EXTERNAL_CONTENT_URI, crQueryParameter);
            }
            else {
                cursor = null;
            }
        }
        else {
            cursor = PhotoStackQueryHelper.crQuery(context.getContentResolver(), MediaStore$Images$Media.EXTERNAL_CONTENT_URI, crQueryParameter);
        }
        if (cursor != null) {
            b = (cursor.getCount() == 0 && b2);
            cursor.close();
        }
        return b;
    }
    
    public void loadLocalCache() {
        if (this.mLocalCacheBackup != null) {
            this.mLocalCache.clear();
            this.mLocalCache = new LinkedList<Content.ContentInfo>(this.mLocalCacheBackup);
        }
    }
    
    public void pause() {
        if (CamLog.VERBOSE) {
            CamLog.d("pause() has been called.");
        }
        this.mStorage.cancelDataLoad(false);
        this.clearLocalCache();
    }
    
    public void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("release() has been called.");
        }
        this.clearLocalCache();
        this.mLocalCache = null;
        this.mDataCallback = null;
        this.mHandler.removeAllMessages();
    }
    
    public void reload(final int n) {
        if (CamLog.VERBOSE) {
            CamLog.d("reload() has been called.");
        }
        if (ContentLoader$2.$SwitchMap$com$sonyericsson$cameracommon$contentsview$ContentLoader$SecurityLevel[this.mSecurityLevel.ordinal()] != 1) {
            if (CamLog.VERBOSE) {
                CamLog.d("reload() : SecurityLevel = NORMAL");
            }
            for (int i = 0; i < n; ++i) {
                this.mStorage.requestDataLoad(i, false, this.mOnLoadCompleteListener);
            }
        }
        else {
            if (CamLog.VERBOSE) {
                CamLog.d("reload() : SecurityLevel = NEWLY_ADDED_CONTENT_ONLY ");
            }
            final LinkedList<Content.ContentInfo> localCache = this.getLocalCache();
            if (localCache != null && localCache.size() > 0) {
                this.request(-1, localCache.getFirst().mOriginalUri);
            }
        }
    }
    
    public void reloadTopContent() {
        if (this.mLocalCache != null && !this.mLocalCache.isEmpty() && !this.mLocalCache.getFirst().mIsContainDetails) {
            this.request(-1, this.mLocalCache.getFirst().mOriginalUri);
        }
    }
    
    public void removeInvalidLocalCache(final Context context) {
        final Iterator<Object> iterator = this.mLocalCache.iterator();
        while (iterator.hasNext()) {
            final Content.ContentInfo contentInfo = iterator.next();
            if (!new File(contentInfo.mOriginalPath).exists()) {
                iterator.remove();
            }
            else {
                if (!this.isRemovedFromDataBase(context, contentInfo.mId, contentInfo.mType)) {
                    continue;
                }
                contentInfo.mIsInTrash = true;
                iterator.remove();
            }
        }
    }
    
    public void request(final int i, final Uri uri) {
        if (CamLog.VERBOSE) {
            CamLog.d("request() has been called.");
            final StringBuilder sb = new StringBuilder();
            sb.append("  requestId:");
            sb.append(i);
            CamLog.d(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  uri:");
            sb2.append(uri.toString());
            CamLog.d(sb2.toString());
        }
        this.mStorage.requestDataLoad(i, uri, true, this.mOnLoadCompleteListener);
    }
    
    public void requestCreateContentInfoSync(final ArrayList<Uri> list) {
        this.mStorage.requestCreateContentInfoSync(list, this.mOnLoadCompleteListener);
    }
    
    public void requestLastDataLoad(final int n) {
        this.mStorage.requestLastDataLoad(n, true, this.mOnLoadCompleteListener);
    }
    
    public void saveLocalCache() {
        this.mLocalCacheBackup = new LinkedList<Content.ContentInfo>(this.mLocalCache);
    }
    
    interface ContentCreationCallback
    {
        void onContentCreated(final int p0, final Content p1, final Bitmap p2);
        
        void onNoContentLoaded();
    }
    
    private class DataCallback implements DataLoadCallback
    {
        final ContentLoader this$0;
        
        private DataCallback(final ContentLoader this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onDataLoaded(final boolean b, final LinkedList<Content.ContentInfo> list, final int i, final boolean b2, final Bitmap bitmap) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onDataLoaded() has been called. result = ");
                sb.append(b);
                sb.append(" , requestId = ");
                sb.append(i);
                CamLog.d(sb.toString());
            }
            if (list != null && !list.isEmpty() && b) {
                if (b2) {
                    this.this$0.addLocalCache(list);
                }
                if (!list.getLast().mIsContainDetails) {
                    this.this$0.request(i, list.getLast().mOriginalUri);
                }
                else {
                    this.this$0.mHandler.notifyContentLoaded(i, this.this$0.new DataLoadResult(ContentFactory.create(list.getLast()), bitmap));
                }
            }
            else {
                CamLog.w("Loading data is failed.");
                this.this$0.mHandler.notifyContentLoaded(i, null);
            }
        }
    }
    
    private class DataLoadResult
    {
        private Bitmap mBitmap;
        private Content mContent;
        final ContentLoader this$0;
        
        public DataLoadResult(final ContentLoader this$0, final Content mContent, final Bitmap mBitmap) {
            this.this$0 = this$0;
            this.mContent = mContent;
            this.mBitmap = mBitmap;
        }
    }
    
    private class DataLoaderHander extends Handler
    {
        private static final int NOTIFY_CONTENT_CREATED = 1;
        private static final int NOTIFY_NO_CONTENT_LOADED = 0;
        final ContentLoader this$0;
        
        private DataLoaderHander(final ContentLoader this$0) {
            this.this$0 = this$0;
        }
        
        private void notifyContentLoaded(final int arg1, final DataLoadResult obj) {
            if (CamLog.VERBOSE) {
                CamLog.d("notifyContentLoaded() has been called.");
            }
            final Message obtain = Message.obtain((Handler)this);
            if (obj != null) {
                obtain.what = 1;
                obtain.obj = obj;
            }
            else {
                obtain.what = 0;
            }
            obtain.arg1 = arg1;
            this.sendMessage(obtain);
        }
        
        private void removeAllMessages() {
            this.removeMessages(1);
            this.removeMessages(0);
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                case 1: {
                    if (CamLog.VERBOSE) {
                        CamLog.d("handleMessage for content creation.");
                    }
                    final int arg1 = message.arg1;
                    final DataLoadResult dataLoadResult = (DataLoadResult)message.obj;
                    this.this$0.removeFuture(dataLoadResult.mContent.getContentInfo().mId);
                    this.this$0.mContentCallback.onContentCreated(arg1, dataLoadResult.mContent, dataLoadResult.mBitmap);
                    break;
                }
                case 0: {
                    this.this$0.mContentCallback.onNoContentLoaded();
                    break;
                }
            }
        }
    }
    
    public enum SecurityLevel
    {
        private static final SecurityLevel[] $VALUES;
        
        NEWLY_ADDED_CONTENT_ONLY, 
        NORMAL;
        
        static {
            $VALUES = new SecurityLevel[] { SecurityLevel.NORMAL, SecurityLevel.NEWLY_ADDED_CONTENT_ONLY };
        }
    }
}
