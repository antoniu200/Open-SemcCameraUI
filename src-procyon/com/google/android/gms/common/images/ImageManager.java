// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.images;

import android.content.res.Configuration;
import android.content.ComponentCallbacks2;
import android.os.SystemClock;
import java.util.concurrent.CountDownLatch;
import java.io.IOException;
import android.util.Log;
import android.graphics.BitmapFactory;
import com.google.android.gms.internal.zzmg;
import android.app.ActivityManager;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.content.Intent;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import java.util.ArrayList;
import android.os.ResultReceiver;
import com.google.android.gms.common.internal.zzb;
import android.widget.ImageView;
import android.content.ComponentCallbacks;
import android.graphics.Bitmap;
import java.util.HashMap;
import com.google.android.gms.internal.zzmx;
import java.util.concurrent.Executors;
import android.os.Looper;
import java.util.Map;
import com.google.android.gms.internal.zzlv;
import java.util.concurrent.ExecutorService;
import android.os.Handler;
import android.content.Context;
import android.net.Uri;
import java.util.HashSet;

public final class ImageManager
{
    private static final Object zzadG;
    private static HashSet<Uri> zzadH;
    private static ImageManager zzadI;
    private static ImageManager zzadJ;
    private final Context mContext;
    private final Handler mHandler;
    private final ExecutorService zzadK;
    private final zzb zzadL;
    private final zzlv zzadM;
    private final Map<com.google.android.gms.common.images.zza, ImageReceiver> zzadN;
    private final Map<Uri, ImageReceiver> zzadO;
    private final Map<Uri, Long> zzadP;
    
    static {
        zzadG = new Object();
        ImageManager.zzadH = new HashSet<Uri>();
    }
    
    private ImageManager(final Context context, final boolean b) {
        this.mContext = context.getApplicationContext();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.zzadK = Executors.newFixedThreadPool(4);
        if (b) {
            this.zzadL = new zzb(this.mContext);
            if (zzmx.zzqx()) {
                this.zzoB();
            }
        }
        else {
            this.zzadL = null;
        }
        this.zzadM = new zzlv();
        this.zzadN = new HashMap<com.google.android.gms.common.images.zza, ImageReceiver>();
        this.zzadO = new HashMap<Uri, ImageReceiver>();
        this.zzadP = new HashMap<Uri, Long>();
    }
    
    public static ImageManager create(final Context context) {
        return zzb(context, false);
    }
    
    private Bitmap zza(final com.google.android.gms.common.images.zza.zza zza) {
        if (this.zzadL == null) {
            return null;
        }
        return this.zzadL.get(zza);
    }
    
    public static ImageManager zzb(final Context context, final boolean b) {
        if (b) {
            if (ImageManager.zzadJ == null) {
                ImageManager.zzadJ = new ImageManager(context, true);
            }
            return ImageManager.zzadJ;
        }
        if (ImageManager.zzadI == null) {
            ImageManager.zzadI = new ImageManager(context, false);
        }
        return ImageManager.zzadI;
    }
    
    private void zzoB() {
        this.mContext.registerComponentCallbacks((ComponentCallbacks)new zze(this.zzadL));
    }
    
    public void loadImage(final ImageView imageView, final int n) {
        this.zza(new com.google.android.gms.common.images.zza.zzb(imageView, n));
    }
    
    public void loadImage(final ImageView imageView, final Uri uri) {
        this.zza(new com.google.android.gms.common.images.zza.zzb(imageView, uri));
    }
    
    public void loadImage(final ImageView imageView, final Uri uri, final int n) {
        final com.google.android.gms.common.images.zza.zzb zzb = new com.google.android.gms.common.images.zza.zzb(imageView, uri);
        zzb.zzby(n);
        this.zza(zzb);
    }
    
    public void loadImage(final OnImageLoadedListener onImageLoadedListener, final Uri uri) {
        this.zza(new com.google.android.gms.common.images.zza.zzc(onImageLoadedListener, uri));
    }
    
    public void loadImage(final OnImageLoadedListener onImageLoadedListener, final Uri uri, final int n) {
        final com.google.android.gms.common.images.zza.zzc zzc = new com.google.android.gms.common.images.zza.zzc(onImageLoadedListener, uri);
        zzc.zzby(n);
        this.zza(zzc);
    }
    
    public void zza(final com.google.android.gms.common.images.zza zza) {
        com.google.android.gms.common.internal.zzb.zzci("ImageManager.loadImage() must be called in the main thread");
        new zzd(zza).run();
    }
    
    private final class ImageReceiver extends ResultReceiver
    {
        private final Uri mUri;
        private final ArrayList<com.google.android.gms.common.images.zza> zzadQ;
        final ImageManager zzadR;
        
        ImageReceiver(final ImageManager zzadR, final Uri mUri) {
            this.zzadR = zzadR;
            super(new Handler(Looper.getMainLooper()));
            this.mUri = mUri;
            this.zzadQ = new ArrayList<com.google.android.gms.common.images.zza>();
        }
        
        public void onReceiveResult(final int n, final Bundle bundle) {
            this.zzadR.zzadK.execute(this.zzadR.new zzc(this.mUri, (ParcelFileDescriptor)bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }
        
        public void zzb(final com.google.android.gms.common.images.zza e) {
            com.google.android.gms.common.internal.zzb.zzci("ImageReceiver.addImageRequest() must be called in the main thread");
            this.zzadQ.add(e);
        }
        
        public void zzc(final com.google.android.gms.common.images.zza o) {
            com.google.android.gms.common.internal.zzb.zzci("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.zzadQ.remove(o);
        }
        
        public void zzoE() {
            final Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.putExtra("com.google.android.gms.extras.uri", (Parcelable)this.mUri);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", (Parcelable)this);
            intent.putExtra("com.google.android.gms.extras.priority", 3);
            this.zzadR.mContext.sendBroadcast(intent);
        }
    }
    
    public interface OnImageLoadedListener
    {
        void onImageLoaded(final Uri p0, final Drawable p1, final boolean p2);
    }
    
    private static final class zza
    {
        static int zza(final ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }
    
    private static final class zzb extends zzmg<com.google.android.gms.common.images.zza.zza, Bitmap>
    {
        public zzb(final Context context) {
            super(zzaj(context));
        }
        
        private static int zzaj(final Context context) {
            final ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
            int n;
            if ((context.getApplicationInfo().flags & 0x100000) != 0x0 && zzmx.zzqu()) {
                n = zza.zza(activityManager);
            }
            else {
                n = activityManager.getMemoryClass();
            }
            return (int)(0.33f * (1048576 * n));
        }
        
        protected int zza(final com.google.android.gms.common.images.zza.zza zza, final Bitmap bitmap) {
            return bitmap.getHeight() * bitmap.getRowBytes();
        }
        
        protected void zza(final boolean b, final com.google.android.gms.common.images.zza.zza zza, final Bitmap bitmap, final Bitmap bitmap2) {
            super.entryRemoved(b, zza, bitmap, bitmap2);
        }
    }
    
    private final class zzc implements Runnable
    {
        private final Uri mUri;
        final ImageManager zzadR;
        private final ParcelFileDescriptor zzadS;
        
        public zzc(final ImageManager zzadR, final Uri mUri, final ParcelFileDescriptor zzadS) {
            this.zzadR = zzadR;
            this.mUri = mUri;
            this.zzadS = zzadS;
        }
        
        @Override
        public void run() {
            com.google.android.gms.common.internal.zzb.zzcj("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            final ParcelFileDescriptor zzadS = this.zzadS;
            boolean b = false;
            final boolean b2 = false;
            Bitmap decodeFileDescriptor = null;
            final Bitmap bitmap = null;
            if (zzadS != null) {
                try {
                    decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(this.zzadS.getFileDescriptor());
                    b = b2;
                }
                catch (final OutOfMemoryError outOfMemoryError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("OOM while loading bitmap for uri: ");
                    sb.append(this.mUri);
                    Log.e("ImageManager", sb.toString(), (Throwable)outOfMemoryError);
                    b = true;
                    decodeFileDescriptor = bitmap;
                }
                try {
                    this.zzadS.close();
                }
                catch (final IOException ex) {
                    Log.e("ImageManager", "closed failed", (Throwable)ex);
                }
            }
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            this.zzadR.mHandler.post((Runnable)this.zzadR.new zzf(this.mUri, decodeFileDescriptor, b, countDownLatch));
            try {
                countDownLatch.await();
            }
            catch (final InterruptedException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Latch interrupted while posting ");
                sb2.append(this.mUri);
                Log.w("ImageManager", sb2.toString());
            }
        }
    }
    
    private final class zzd implements Runnable
    {
        final ImageManager zzadR;
        private final com.google.android.gms.common.images.zza zzadT;
        
        public zzd(final ImageManager zzadR, final com.google.android.gms.common.images.zza zzadT) {
            this.zzadR = zzadR;
            this.zzadT = zzadT;
        }
        
        @Override
        public void run() {
            com.google.android.gms.common.internal.zzb.zzci("LoadImageRunnable must be executed on the main thread");
            final ImageReceiver imageReceiver = this.zzadR.zzadN.get(this.zzadT);
            if (imageReceiver != null) {
                this.zzadR.zzadN.remove(this.zzadT);
                imageReceiver.zzc(this.zzadT);
            }
            final com.google.android.gms.common.images.zza.zza zzadV = this.zzadT.zzadV;
            if (zzadV.uri == null) {
                this.zzadT.zza(this.zzadR.mContext, this.zzadR.zzadM, true);
                return;
            }
            final Bitmap zza = this.zzadR.zza(zzadV);
            if (zza != null) {
                this.zzadT.zza(this.zzadR.mContext, zza, true);
                return;
            }
            final Long n = this.zzadR.zzadP.get(zzadV.uri);
            if (n != null) {
                if (SystemClock.elapsedRealtime() - n < 3600000L) {
                    this.zzadT.zza(this.zzadR.mContext, this.zzadR.zzadM, true);
                    return;
                }
                this.zzadR.zzadP.remove(zzadV.uri);
            }
            this.zzadT.zza(this.zzadR.mContext, this.zzadR.zzadM);
            ResultReceiver resultReceiver;
            if ((resultReceiver = this.zzadR.zzadO.get(zzadV.uri)) == null) {
                resultReceiver = this.zzadR.new ImageReceiver(zzadV.uri);
                this.zzadR.zzadO.put(zzadV.uri, resultReceiver);
            }
            ((ImageReceiver)resultReceiver).zzb(this.zzadT);
            if (!(this.zzadT instanceof com.google.android.gms.common.images.zza.zzc)) {
                this.zzadR.zzadN.put(this.zzadT, resultReceiver);
            }
            synchronized (ImageManager.zzadG) {
                if (!ImageManager.zzadH.contains(zzadV.uri)) {
                    ImageManager.zzadH.add(zzadV.uri);
                    ((ImageReceiver)resultReceiver).zzoE();
                }
            }
        }
    }
    
    private static final class zze implements ComponentCallbacks2
    {
        private final zzb zzadL;
        
        public zze(final zzb zzadL) {
            this.zzadL = zzadL;
        }
        
        public void onConfigurationChanged(final Configuration configuration) {
        }
        
        public void onLowMemory() {
            this.zzadL.evictAll();
        }
        
        public void onTrimMemory(final int n) {
            if (n >= 60) {
                this.zzadL.evictAll();
                return;
            }
            if (n >= 20) {
                this.zzadL.trimToSize(this.zzadL.size() / 2);
            }
        }
    }
    
    private final class zzf implements Runnable
    {
        private final Bitmap mBitmap;
        private final Uri mUri;
        final ImageManager zzadR;
        private boolean zzadU;
        private final CountDownLatch zzoS;
        
        public zzf(final ImageManager zzadR, final Uri mUri, final Bitmap mBitmap, final boolean zzadU, final CountDownLatch zzoS) {
            this.zzadR = zzadR;
            this.mUri = mUri;
            this.mBitmap = mBitmap;
            this.zzadU = zzadU;
            this.zzoS = zzoS;
        }
        
        private void zza(final ImageReceiver imageReceiver, final boolean b) {
            final ArrayList zza = imageReceiver.zzadQ;
            for (int size = zza.size(), i = 0; i < size; ++i) {
                final com.google.android.gms.common.images.zza zza2 = zza.get(i);
                if (b) {
                    zza2.zza(this.zzadR.mContext, this.mBitmap, false);
                }
                else {
                    this.zzadR.zzadP.put(this.mUri, SystemClock.elapsedRealtime());
                    zza2.zza(this.zzadR.mContext, this.zzadR.zzadM, false);
                }
                if (!(zza2 instanceof com.google.android.gms.common.images.zza.zzc)) {
                    this.zzadR.zzadN.remove(zza2);
                }
            }
        }
        
        @Override
        public void run() {
            com.google.android.gms.common.internal.zzb.zzci("OnBitmapLoadedRunnable must be executed in the main thread");
            final boolean b = this.mBitmap != null;
            if (this.zzadR.zzadL != null) {
                if (this.zzadU) {
                    this.zzadR.zzadL.evictAll();
                    System.gc();
                    this.zzadU = false;
                    this.zzadR.mHandler.post((Runnable)this);
                    return;
                }
                if (b) {
                    this.zzadR.zzadL.put(new com.google.android.gms.common.images.zza.zza(this.mUri), this.mBitmap);
                }
            }
            final ImageReceiver imageReceiver = this.zzadR.zzadO.remove(this.mUri);
            if (imageReceiver != null) {
                this.zza(imageReceiver, b);
            }
            this.zzoS.countDown();
            synchronized (ImageManager.zzadG) {
                ImageManager.zzadH.remove(this.mUri);
            }
        }
    }
}
