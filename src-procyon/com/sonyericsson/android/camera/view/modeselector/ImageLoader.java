// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import android.graphics.BitmapFactory;
import com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.FileOutputStream;
import java.io.File;
import android.os.Environment;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.app.ActivityManager;
import com.sonyericsson.android.camera.util.ThreadUtil;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.LruCache;
import android.content.Context;
import java.util.concurrent.Executor;

public class ImageLoader
{
    private static final boolean DEBUG = false;
    private static final int FADE_IN_TRANSITION_DURATION = 200;
    private static final int HARD_CACHE_SIZE = 5;
    private static final float MAX_PERCENT_FOR_HARD_CACHE = 0.25f;
    private static final Executor NETWORK_DOWNLOADER_EXECUTOR;
    private static final Executor RESOURCE_DOWNLOADER_EXECUTOR;
    private static final String TAG = "ImageLoader";
    private static final String THREAD_NAME_NETWORK_DOWNLOADERS = "AppsUi#Net";
    private static final String THREAD_NAME_RESOURCE_DOWNLOADERS = "AppsUi#Res";
    private static ImageLoader sImageLoader;
    private static final Object sImageLoaderLock;
    private final Context mContext;
    private boolean mFadeInBitmap;
    private final LruCache<String, BitmapDrawable> mImageCache;
    private Bitmap mLoadingBitmap;
    private final Resources mResources;
    
    static {
        NETWORK_DOWNLOADER_EXECUTOR = ThreadUtil.buildPoolExecutor("AppsUi#Net", 3);
        RESOURCE_DOWNLOADER_EXECUTOR = ThreadUtil.buildPoolExecutor("AppsUi#Res", 1);
        sImageLoaderLock = new Object();
    }
    
    private ImageLoader(final Context context) {
        final ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
        final boolean b = (context.getApplicationInfo().flags & 0x100000) != 0x0;
        int n = activityManager.getMemoryClass();
        if (b) {
            n = activityManager.getLargeMemoryClass();
        }
        int n2;
        if ((n2 = (int)(n * 0.25f)) > 5) {
            n2 = 5242880;
        }
        this.mImageCache = new LruCache<String, BitmapDrawable>(this, n2) {
            final ImageLoader this$0;
            
            protected int sizeOf(final String s, final BitmapDrawable bitmapDrawable) {
                Bitmap bitmap;
                if (bitmapDrawable == null) {
                    bitmap = null;
                }
                else {
                    bitmap = bitmapDrawable.getBitmap();
                }
                int byteCount;
                if (bitmap == null) {
                    byteCount = 0;
                }
                else {
                    byteCount = bitmap.getByteCount();
                }
                return byteCount;
            }
        };
        this.mContext = context.getApplicationContext();
        this.mResources = context.getResources();
    }
    
    private static void bitmap2File(final Bitmap bitmap, String child) {
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), child));
            child = null;
            try {
                try {
                    bitmap.compress(Bitmap$CompressFormat.PNG, 100, (OutputStream)fileOutputStream);
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    return;
                }
                finally {
                    if (fileOutputStream != null) {
                        if (child != null) {
                            final FileOutputStream fileOutputStream2 = fileOutputStream;
                            fileOutputStream2.close();
                        }
                        else {
                            fileOutputStream.close();
                        }
                    }
                }
            }
            catch (final Throwable t) {}
            try {
                final FileOutputStream fileOutputStream2 = fileOutputStream;
                fileOutputStream2.close();
            }
            catch (final Throwable t2) {}
        }
        catch (final IOException obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("writeImage2File - io exception: ");
            sb.append(obj);
            CamLog.e(sb.toString());
        }
        catch (final FileNotFoundException obj2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("writeImage2File - file not found: ");
            sb2.append(obj2);
            CamLog.e(sb2.toString());
        }
    }
    
    private static boolean cancelPotentialTask(final String anObject, final ImageView imageView) {
        final ImageLoaderTask imageLoaderTask = getImageLoaderTask(imageView);
        if (imageLoaderTask != null) {
            final String access$000 = imageLoaderTask.imageUri;
            if (access$000 != null && access$000.equals(anObject)) {
                return false;
            }
            imageLoaderTask.cancel(true);
        }
        return true;
    }
    
    private static ImageLoaderTask getImageLoaderTask(final ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable)drawable).getImageLoaderTask();
            }
        }
        return null;
    }
    
    public static final ImageLoader getInstance(final Context context) {
        if (ImageLoader.sImageLoader == null) {
            synchronized (ImageLoader.sImageLoaderLock) {
                if (ImageLoader.sImageLoader == null) {
                    ImageLoader.sImageLoader = new ImageLoader(context);
                }
            }
        }
        return ImageLoader.sImageLoader;
    }
    
    private static void scheduleTask(final ImageLoaderTask imageLoaderTask) {
        final String scheme = Uri.parse(imageLoaderTask.imageUri).getScheme();
        if ("resource".equalsIgnoreCase(scheme)) {
            imageLoaderTask.executeOnExecutor(ImageLoader.RESOURCE_DOWNLOADER_EXECUTOR, (Object[])new Void[0]);
        }
        else if ("http".equalsIgnoreCase(scheme)) {
            imageLoaderTask.executeOnExecutor(ImageLoader.NETWORK_DOWNLOADER_EXECUTOR, (Object[])new Void[0]);
        }
        else if ("https".equalsIgnoreCase(scheme)) {
            imageLoaderTask.executeOnExecutor(ImageLoader.NETWORK_DOWNLOADER_EXECUTOR, (Object[])new Void[0]);
        }
    }
    
    private void setImageDrawable(final ImageView imageView, final Drawable imageDrawable) {
        if (this.mFadeInBitmap) {
            final TransitionDrawable imageDrawable2 = new TransitionDrawable(new Drawable[] { (Drawable)new BitmapDrawable(this.mResources, this.mLoadingBitmap), imageDrawable });
            imageDrawable2.setCrossFadeEnabled(true);
            imageView.setImageDrawable((Drawable)imageDrawable2);
            imageDrawable2.startTransition(200);
        }
        else {
            imageView.setImageDrawable(imageDrawable);
        }
    }
    
    public void release() {
        this.mImageCache.evictAll();
    }
    
    public void removeCache(final String s) {
        if (this.mImageCache.get((Object)s) != null) {
            this.mImageCache.remove((Object)s);
        }
    }
    
    public void requestLoad(final String s, final int n, final int n2, final ImageView imageView) {
        if (s == null || s.length() == 0) {
            return;
        }
        if (n <= 0 || n2 <= 0) {
            return;
        }
        if (imageView == null) {
            return;
        }
        Object imageDrawable = null;
        if (this.mImageCache != null) {
            imageDrawable = this.mImageCache.get((Object)s);
        }
        if (imageDrawable != null) {
            imageView.setImageDrawable((Drawable)imageDrawable);
        }
        else if (cancelPotentialTask(s, imageView)) {
            final ImageLoaderTask imageLoaderTask = new ImageLoaderTask(s, n, n2, imageView);
            imageView.setImageDrawable((Drawable)new AsyncDrawable(this.mResources, this.mLoadingBitmap, imageLoaderTask));
            scheduleTask(imageLoaderTask);
        }
    }
    
    public void requestLoad(final String s, final AbsPanelView absPanelView) {
        if (absPanelView == null) {
            return;
        }
        this.requestLoad(s, absPanelView.getAppIconWidth(), absPanelView.getAppIconHeight(), absPanelView.getAppIconView());
    }
    
    public void setImageFadeIn(final boolean mFadeInBitmap) {
        this.mFadeInBitmap = mFadeInBitmap;
    }
    
    public void setLoadingImage(final int n) {
        this.mLoadingBitmap = BitmapFactory.decodeResource(this.mResources, n);
    }
    
    private static class AsyncDrawable extends BitmapDrawable
    {
        private final WeakReference<ImageLoaderTask> imageLoaderTaskReference;
        
        public AsyncDrawable(final Resources resources, final Bitmap bitmap, final ImageLoaderTask referent) {
            super(resources, bitmap);
            this.imageLoaderTaskReference = new WeakReference<ImageLoaderTask>(referent);
        }
        
        public ImageLoaderTask getImageLoaderTask() {
            return this.imageLoaderTaskReference.get();
        }
    }
    
    private class ImageLoaderTask extends AsyncTask<Void, Void, BitmapDrawable>
    {
        private static final String THREAD_NAME = "AppsUi#AsyncTask";
        private final int imageHeight;
        private final String imageUri;
        private final WeakReference<ImageView> imageViewReference;
        private final int imageWidth;
        final ImageLoader this$0;
        
        public ImageLoaderTask(final ImageLoader this$0, final String imageUri, final int imageWidth, final int imageHeight, final ImageView referent) {
            this.this$0 = this$0;
            this.imageUri = imageUri;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.imageViewReference = new WeakReference<ImageView>(referent);
        }
        
        private ImageView getAttachedImageView() {
            final ImageView imageView = this.imageViewReference.get();
            if (this == getImageLoaderTask(imageView)) {
                return imageView;
            }
            return null;
        }
        
        public BitmapDrawable doInBackground(final Void... array) {
            Thread.currentThread().setName("AppsUi#AsyncTask");
            final boolean cancelled = this.isCancelled();
            BitmapDrawable bitmapDrawable = null;
            Bitmap bitmap;
            if (!cancelled && this.getAttachedImageView() != null) {
                bitmap = ResourceUtil.getBitmap(this.this$0.mContext, this.imageUri, this.imageWidth, this.imageHeight);
            }
            else {
                bitmap = null;
            }
            if (bitmap != null) {
                final BitmapDrawable bitmapDrawable2 = bitmapDrawable = new BitmapDrawable(this.this$0.mResources, bitmap);
                if (this.this$0.mImageCache != null) {
                    this.this$0.mImageCache.put((Object)this.imageUri, (Object)bitmapDrawable2);
                    bitmapDrawable = bitmapDrawable2;
                }
            }
            return bitmapDrawable;
        }
        
        public void onCancelled(final BitmapDrawable bitmapDrawable) {
            super.onCancelled((Object)bitmapDrawable);
        }
        
        public void onPostExecute(BitmapDrawable bitmapDrawable) {
            if (this.isCancelled()) {
                bitmapDrawable = null;
            }
            final ImageView attachedImageView = this.getAttachedImageView();
            if (bitmapDrawable != null && attachedImageView != null) {
                this.this$0.setImageDrawable(attachedImageView, (Drawable)bitmapDrawable);
            }
        }
    }
}
