// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller.album;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Iterator;
import android.graphics.Bitmap;
import com.sonyericsson.album.fastview.FastViewUnavailableException;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.LinkedHashMap;
import android.net.Uri;
import java.util.Map;
import com.sonyericsson.album.fastview.FastViewManager;
import android.content.Context;

public class AlbumPreloader
{
    public static final String TAG = "AlbumPreloader";
    private Context mContext;
    private FastViewManager mFastViewManager;
    private boolean mIsAvailable;
    private boolean mIsReleased;
    private final PreloadedCacheHolder mPreloadedCache;
    private Object mPreloadingLock;
    private final Map<Uri, PrepareBitmapTask> mRequestTasks;
    
    public AlbumPreloader(final Context mContext) {
        this.mRequestTasks = new LinkedHashMap<Uri, PrepareBitmapTask>();
        this.mPreloadedCache = new PreloadedCacheHolder();
        this.mIsReleased = false;
        this.mPreloadingLock = new Object();
        this.mIsAvailable = false;
        this.mContext = mContext;
        try {
            (this.mFastViewManager = new FastViewManager(this.mContext)).setOnPrewarmedListener((FastViewManager.OnPrewarmedListener)new FastViewManager.OnPrewarmedListener(this) {
                final AlbumPreloader this$0;
                
                @Override
                public void onPrewarmed() {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Prewarm album");
                    }
                    if (this.this$0.mIsReleased) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("Activity is already stopped.");
                        }
                        this.this$0.mFastViewManager.cooldown();
                    }
                }
            });
            this.mIsAvailable = true;
        }
        catch (final FastViewUnavailableException ex) {
            CamLog.e("Failed to open FastViewManager");
        }
    }
    
    public Bitmap getBitmap(final Uri uri) {
        if (!this.mIsAvailable) {
            return null;
        }
        if (uri == null) {
            return null;
        }
        synchronized (this.mPreloadingLock) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Get bitmap : ");
                sb.append(uri.toString());
                CamLog.d(sb.toString());
            }
            if (this.mPreloadedCache.isSameUri(uri)) {
                return this.mPreloadedCache.get();
            }
            if (this.mRequestTasks.containsKey(uri)) {
                this.mRequestTasks.remove(uri).cancel(false);
            }
            return this.mFastViewManager.getBitmap(uri);
        }
    }
    
    public void prepareBitmap(final Uri uri) {
        if (!this.mIsAvailable) {
            return;
        }
        if (uri == null) {
            return;
        }
        synchronized (this.mPreloadingLock) {
            if (!this.mRequestTasks.containsKey(uri)) {
                final PrepareBitmapTask prepareBitmapTask = new PrepareBitmapTask();
                this.mRequestTasks.put(uri, prepareBitmapTask);
                prepareBitmapTask.execute(new Object[] { uri });
            }
        }
    }
    
    public void prewarmAlbum() {
        if (!this.mIsAvailable) {
            return;
        }
        new PrewarmAlbumTask().execute(new Object[0]);
    }
    
    public void release() {
        if (!this.mIsAvailable) {
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("release()");
        }
        synchronized (this.mPreloadingLock) {
            final Iterator<Uri> iterator = this.mRequestTasks.keySet().iterator();
            while (iterator.hasNext()) {
                this.mRequestTasks.get(iterator.next()).cancel(false);
            }
            this.mRequestTasks.clear();
            this.mPreloadedCache.clear();
            monitorexit(this.mPreloadingLock);
            if (this.mFastViewManager != null) {
                this.mFastViewManager.cooldown();
            }
            this.mIsReleased = true;
        }
    }
    
    private static class PreloadedCacheHolder
    {
        private Bitmap mBitmap;
        private Uri mUri;
        
        public void clear() {
            if (this.mBitmap != null) {
                this.mBitmap.recycle();
                this.mBitmap = null;
            }
            this.mUri = null;
        }
        
        @Nullable
        public Bitmap get() {
            final Bitmap mBitmap = this.mBitmap;
            this.mUri = null;
            this.mBitmap = null;
            return mBitmap;
        }
        
        public boolean isSameUri(final Uri uri) {
            if (this.mUri != null) {
                return this.mUri.equals((Object)uri);
            }
            return this.mUri == uri;
        }
        
        public void update(@NonNull final Uri mUri, @NonNull final Bitmap mBitmap) {
            if (this.isSameUri(mUri) && mBitmap == this.mBitmap) {
                return;
            }
            this.clear();
            this.mUri = mUri;
            this.mBitmap = mBitmap;
        }
    }
    
    private final class PrepareBitmapTask extends AsyncTask
    {
        private static final String THREAD_NAME = "PrepareBitmap";
        final AlbumPreloader this$0;
        
        private PrepareBitmapTask(final AlbumPreloader this$0) {
            this.this$0 = this$0;
        }
        
        protected Object doInBackground(final Object[] array) {
            Thread.currentThread().setName("PrepareBitmap");
            final Uri uri = (Uri)array[0];
            final Bitmap bitmap = this.this$0.mFastViewManager.getBitmap(uri);
            synchronized (this.this$0.mPreloadingLock) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Prepare bitmap : ");
                    sb.append(uri.toString());
                    CamLog.d(sb.toString());
                }
                this.this$0.mPreloadedCache.update(uri, bitmap);
                this.this$0.mRequestTasks.remove(uri);
                monitorexit(this.this$0.mPreloadingLock);
                if (this.this$0.mFastViewManager != null) {
                    this.this$0.mFastViewManager.prepare(uri);
                }
                return null;
            }
        }
    }
    
    private final class PrewarmAlbumTask extends AsyncTask
    {
        private static final String THREAD_NAME = "PrewarmAlbum";
        final AlbumPreloader this$0;
        
        private PrewarmAlbumTask(final AlbumPreloader this$0) {
            this.this$0 = this$0;
        }
        
        protected Object doInBackground(final Object[] array) {
            Thread.currentThread().setName("PrewarmAlbum");
            this.this$0.mFastViewManager.prewarm();
            return null;
        }
    }
}
