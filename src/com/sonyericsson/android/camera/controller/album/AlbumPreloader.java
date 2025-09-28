package com.sonyericsson.android.camera.controller.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.sonyericsson.album.fastview.FastViewManager;
import com.sonyericsson.album.fastview.FastViewUnavailableException;
import com.sonyericsson.android.camera.util.CamLog;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AlbumPreloader {
    public static final String TAG = "AlbumPreloader";
    private Context mContext;
    private FastViewManager mFastViewManager;
    private boolean mIsAvailable;
    private final Map<Uri, PrepareBitmapTask> mRequestTasks = new LinkedHashMap();
    private final PreloadedCacheHolder mPreloadedCache = new PreloadedCacheHolder();
    private boolean mIsReleased = false;
    private Object mPreloadingLock = new Object();

    private static class PreloadedCacheHolder {
        private Bitmap mBitmap;
        private Uri mUri;

        private PreloadedCacheHolder() {
        }

        public void update(@NonNull Uri uri, @NonNull Bitmap bitmap) {
            if (isSameUri(uri) && bitmap == this.mBitmap) {
                return;
            }
            clear();
            this.mUri = uri;
            this.mBitmap = bitmap;
        }

        @Nullable
        public Bitmap get() {
            Bitmap bitmap = this.mBitmap;
            this.mUri = null;
            this.mBitmap = null;
            return bitmap;
        }

        public void clear() {
            if (this.mBitmap != null) {
                this.mBitmap.recycle();
                this.mBitmap = null;
            }
            this.mUri = null;
        }

        public boolean isSameUri(Uri uri) {
            if (this.mUri != null) {
                return this.mUri.equals(uri);
            }
            return this.mUri == uri;
        }
    }

    public AlbumPreloader(Context context) {
        this.mIsAvailable = false;
        this.mContext = context;
        try {
            this.mFastViewManager = new FastViewManager(this.mContext);
            this.mFastViewManager.setOnPrewarmedListener(new FastViewManager.OnPrewarmedListener() { // from class: com.sonyericsson.android.camera.controller.album.AlbumPreloader.1
                @Override // com.sonyericsson.album.fastview.FastViewManager.OnPrewarmedListener
                public void onPrewarmed() {
                    if (CamLog.VERBOSE) {
                        CamLog.d("Prewarm album");
                    }
                    if (AlbumPreloader.this.mIsReleased) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("Activity is already stopped.");
                        }
                        AlbumPreloader.this.mFastViewManager.cooldown();
                    }
                }
            });
            this.mIsAvailable = true;
        } catch (FastViewUnavailableException unused) {
            CamLog.e("Failed to open FastViewManager");
        }
    }

    public void prewarmAlbum() {
        if (this.mIsAvailable) {
            new PrewarmAlbumTask().execute(new Object[0]);
        }
    }

    public void prepareBitmap(Uri uri) {
        if (this.mIsAvailable && uri != null) {
            synchronized (this.mPreloadingLock) {
                if (!this.mRequestTasks.containsKey(uri)) {
                    PrepareBitmapTask prepareBitmapTask = new PrepareBitmapTask();
                    this.mRequestTasks.put(uri, prepareBitmapTask);
                    prepareBitmapTask.execute(uri);
                }
            }
        }
    }

    public Bitmap getBitmap(Uri uri) {
        if (!this.mIsAvailable || uri == null) {
            return null;
        }
        synchronized (this.mPreloadingLock) {
            if (CamLog.VERBOSE) {
                CamLog.d("Get bitmap : " + uri.toString());
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

    public void release() {
        if (this.mIsAvailable) {
            if (CamLog.VERBOSE) {
                CamLog.d("release()");
            }
            synchronized (this.mPreloadingLock) {
                Iterator<Uri> it = this.mRequestTasks.keySet().iterator();
                while (it.hasNext()) {
                    this.mRequestTasks.get(it.next()).cancel(false);
                }
                this.mRequestTasks.clear();
                this.mPreloadedCache.clear();
            }
            if (this.mFastViewManager != null) {
                this.mFastViewManager.cooldown();
            }
            this.mIsReleased = true;
        }
    }

    private final class PrewarmAlbumTask extends AsyncTask {
        private static final String THREAD_NAME = "PrewarmAlbum";

        private PrewarmAlbumTask() {
        }

        @Override // android.os.AsyncTask
        protected Object doInBackground(Object[] objArr) {
            Thread.currentThread().setName(THREAD_NAME);
            AlbumPreloader.this.mFastViewManager.prewarm();
            return null;
        }
    }

    private final class PrepareBitmapTask extends AsyncTask {
        private static final String THREAD_NAME = "PrepareBitmap";

        private PrepareBitmapTask() {
        }

        @Override // android.os.AsyncTask
        protected Object doInBackground(Object[] objArr) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Thread.currentThread().setName(THREAD_NAME);
            Uri uri = (Uri) objArr[0];
            Bitmap bitmap = AlbumPreloader.this.mFastViewManager.getBitmap(uri);
            synchronized (AlbumPreloader.this.mPreloadingLock) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Prepare bitmap : " + uri.toString());
                }
                AlbumPreloader.this.mPreloadedCache.update(uri, bitmap);
                AlbumPreloader.this.mRequestTasks.remove(uri);
            }
            if (AlbumPreloader.this.mFastViewManager == null) {
                return null;
            }
            AlbumPreloader.this.mFastViewManager.prepare(uri);
            return null;
        }
    }
}
