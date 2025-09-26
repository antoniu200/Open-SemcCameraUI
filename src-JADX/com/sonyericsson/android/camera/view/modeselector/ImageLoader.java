package com.sonyericsson.android.camera.view.modeselector;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageView;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;
import com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageLoader {
    private static final boolean DEBUG = false;
    private static final int FADE_IN_TRANSITION_DURATION = 200;
    private static final int HARD_CACHE_SIZE = 5;
    private static final float MAX_PERCENT_FOR_HARD_CACHE = 0.25f;
    private static final String TAG = "ImageLoader";
    private static ImageLoader sImageLoader;
    private final Context mContext;
    private boolean mFadeInBitmap;
    private final LruCache<String, BitmapDrawable> mImageCache;
    private Bitmap mLoadingBitmap;
    private final Resources mResources;
    private static final String THREAD_NAME_NETWORK_DOWNLOADERS = "AppsUi#Net";
    private static final Executor NETWORK_DOWNLOADER_EXECUTOR = ThreadUtil.buildPoolExecutor(THREAD_NAME_NETWORK_DOWNLOADERS, 3);
    private static final String THREAD_NAME_RESOURCE_DOWNLOADERS = "AppsUi#Res";
    private static final Executor RESOURCE_DOWNLOADER_EXECUTOR = ThreadUtil.buildPoolExecutor(THREAD_NAME_RESOURCE_DOWNLOADERS, 1);
    private static final Object sImageLoaderLock = new Object();

    public static final ImageLoader getInstance(Context context) {
        if (sImageLoader == null) {
            synchronized (sImageLoaderLock) {
                if (sImageLoader == null) {
                    sImageLoader = new ImageLoader(context);
                }
            }
        }
        return sImageLoader;
    }

    private ImageLoader(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(CameraCommonProviderConstants.CapturingModeColumns.ACTIVITY);
        int largeMemoryClass = (int) (((context.getApplicationInfo().flags & 1048576) != 0 ? activityManager.getLargeMemoryClass() : activityManager.getMemoryClass()) * MAX_PERCENT_FOR_HARD_CACHE);
        this.mImageCache = new LruCache<String, BitmapDrawable>(largeMemoryClass > 5 ? 5242880 : largeMemoryClass) { // from class: com.sonyericsson.android.camera.view.modeselector.ImageLoader.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.util.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                Bitmap bitmap = bitmapDrawable == null ? null : bitmapDrawable.getBitmap();
                if (bitmap == null) {
                    return 0;
                }
                return bitmap.getByteCount();
            }
        };
        this.mContext = context.getApplicationContext();
        this.mResources = context.getResources();
    }

    public void requestLoad(String str, AbsPanelView absPanelView) {
        if (absPanelView == null) {
            return;
        }
        requestLoad(str, absPanelView.getAppIconWidth(), absPanelView.getAppIconHeight(), absPanelView.getAppIconView());
    }

    public void requestLoad(String str, int i, int i2, ImageView imageView) {
        if (str == null || str.length() == 0 || i <= 0 || i2 <= 0 || imageView == null) {
            return;
        }
        BitmapDrawable bitmapDrawable = this.mImageCache != null ? this.mImageCache.get(str) : null;
        if (bitmapDrawable != null) {
            imageView.setImageDrawable(bitmapDrawable);
        } else if (cancelPotentialTask(str, imageView)) {
            ImageLoaderTask imageLoaderTask = new ImageLoaderTask(str, i, i2, imageView);
            imageView.setImageDrawable(new AsyncDrawable(this.mResources, this.mLoadingBitmap, imageLoaderTask));
            scheduleTask(imageLoaderTask);
        }
    }

    private static void scheduleTask(ImageLoaderTask imageLoaderTask) {
        String scheme = Uri.parse(imageLoaderTask.imageUri).getScheme();
        if (ResourceUtil.RESOURCE_SCHEME.equalsIgnoreCase(scheme)) {
            imageLoaderTask.executeOnExecutor(RESOURCE_DOWNLOADER_EXECUTOR, new Void[0]);
        } else if (ResourceUtil.HTTP_SCHEME.equalsIgnoreCase(scheme)) {
            imageLoaderTask.executeOnExecutor(NETWORK_DOWNLOADER_EXECUTOR, new Void[0]);
        } else if (ResourceUtil.HTTPS_SCHEME.equalsIgnoreCase(scheme)) {
            imageLoaderTask.executeOnExecutor(NETWORK_DOWNLOADER_EXECUTOR, new Void[0]);
        }
    }

    public void setLoadingImage(int i) {
        this.mLoadingBitmap = BitmapFactory.decodeResource(this.mResources, i);
    }

    public void setImageFadeIn(boolean z) {
        this.mFadeInBitmap = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImageDrawable(ImageView imageView, Drawable drawable) {
        if (this.mFadeInBitmap) {
            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{new BitmapDrawable(this.mResources, this.mLoadingBitmap), drawable});
            transitionDrawable.setCrossFadeEnabled(true);
            imageView.setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(200);
            return;
        }
        imageView.setImageDrawable(drawable);
    }

    private static boolean cancelPotentialTask(String str, ImageView imageView) {
        ImageLoaderTask imageLoaderTask = getImageLoaderTask(imageView);
        if (imageLoaderTask != null) {
            String str2 = imageLoaderTask.imageUri;
            if (str2 != null && str2.equals(str)) {
                return false;
            }
            imageLoaderTask.cancel(true);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImageLoaderTask getImageLoaderTask(ImageView imageView) {
        if (imageView == null) {
            return null;
        }
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AsyncDrawable) {
            return ((AsyncDrawable) drawable).getImageLoaderTask();
        }
        return null;
    }

    public void removeCache(String str) {
        if (this.mImageCache.get(str) != null) {
            this.mImageCache.remove(str);
        }
    }

    public void release() {
        this.mImageCache.evictAll();
    }

    private class ImageLoaderTask extends AsyncTask<Void, Void, BitmapDrawable> {
        private static final String THREAD_NAME = "AppsUi#AsyncTask";
        private final int imageHeight;
        private final String imageUri;
        private final WeakReference<ImageView> imageViewReference;
        private final int imageWidth;

        public ImageLoaderTask(String str, int i, int i2, ImageView imageView) {
            this.imageUri = str;
            this.imageWidth = i;
            this.imageHeight = i2;
            this.imageViewReference = new WeakReference<>(imageView);
        }

        @Override // android.os.AsyncTask
        public BitmapDrawable doInBackground(Void... voidArr) {
            Thread.currentThread().setName(THREAD_NAME);
            BitmapDrawable bitmapDrawable = null;
            Bitmap bitmap = (isCancelled() || getAttachedImageView() == null) ? null : ResourceUtil.getBitmap(ImageLoader.this.mContext, this.imageUri, this.imageWidth, this.imageHeight);
            if (bitmap != null) {
                bitmapDrawable = new BitmapDrawable(ImageLoader.this.mResources, bitmap);
                if (ImageLoader.this.mImageCache != null) {
                    ImageLoader.this.mImageCache.put(this.imageUri, bitmapDrawable);
                }
            }
            return bitmapDrawable;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(BitmapDrawable bitmapDrawable) {
            if (isCancelled()) {
                bitmapDrawable = null;
            }
            ImageView attachedImageView = getAttachedImageView();
            if (bitmapDrawable == null || attachedImageView == null) {
                return;
            }
            ImageLoader.this.setImageDrawable(attachedImageView, bitmapDrawable);
        }

        @Override // android.os.AsyncTask
        public void onCancelled(BitmapDrawable bitmapDrawable) {
            super.onCancelled((ImageLoaderTask) bitmapDrawable);
        }

        private ImageView getAttachedImageView() {
            ImageView imageView = this.imageViewReference.get();
            if (this == ImageLoader.getImageLoaderTask(imageView)) {
                return imageView;
            }
            return null;
        }
    }

    private static void bitmap2File(Bitmap bitmap, String str) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), str));
            Throwable th = null;
            try {
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                if (fileOutputStream != null) {
                    if (th != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        fileOutputStream.close();
                    }
                }
                throw th3;
            }
        } catch (FileNotFoundException e) {
            CamLog.e("writeImage2File - file not found: " + e);
        } catch (IOException e2) {
            CamLog.e("writeImage2File - io exception: " + e2);
        }
    }

    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<ImageLoaderTask> imageLoaderTaskReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap, ImageLoaderTask imageLoaderTask) {
            super(resources, bitmap);
            this.imageLoaderTaskReference = new WeakReference<>(imageLoaderTask);
        }

        public ImageLoaderTask getImageLoaderTask() {
            return this.imageLoaderTaskReference.get();
        }
    }
}
