package com.google.android.gms.common.images;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.common.images.zza;
import com.google.android.gms.internal.zzlv;
import com.google.android.gms.internal.zzmg;
import com.google.android.gms.internal.zzmx;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ImageManager {
    private static final Object zzadG = new Object();
    private static HashSet<Uri> zzadH = new HashSet<>();
    private static ImageManager zzadI;
    private static ImageManager zzadJ;
    private final Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService zzadK = Executors.newFixedThreadPool(4);
    private final zzb zzadL;
    private final zzlv zzadM;
    private final Map<com.google.android.gms.common.images.zza, ImageReceiver> zzadN;
    private final Map<Uri, ImageReceiver> zzadO;
    private final Map<Uri, Long> zzadP;

    private final class ImageReceiver extends ResultReceiver {
        private final Uri mUri;
        private final ArrayList<com.google.android.gms.common.images.zza> zzadQ;

        ImageReceiver(Uri uri) {
            super(new Handler(Looper.getMainLooper()));
            this.mUri = uri;
            this.zzadQ = new ArrayList<>();
        }

        @Override // android.os.ResultReceiver
        public void onReceiveResult(int i, Bundle bundle) {
            ImageManager.this.zzadK.execute(ImageManager.this.new zzc(this.mUri, (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }

        public void zzb(com.google.android.gms.common.images.zza zzaVar) {
            com.google.android.gms.common.internal.zzb.zzci("ImageReceiver.addImageRequest() must be called in the main thread");
            this.zzadQ.add(zzaVar);
        }

        public void zzc(com.google.android.gms.common.images.zza zzaVar) {
            com.google.android.gms.common.internal.zzb.zzci("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.zzadQ.remove(zzaVar);
        }

        public void zzoE() {
            Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.putExtra("com.google.android.gms.extras.uri", this.mUri);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", this);
            intent.putExtra("com.google.android.gms.extras.priority", 3);
            ImageManager.this.mContext.sendBroadcast(intent);
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable, boolean z);
    }

    private static final class zza {
        static int zza(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    private static final class zzb extends zzmg<zza.C0009zza, Bitmap> {
        public zzb(Context context) {
            super(zzaj(context));
        }

        private static int zzaj(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(CameraCommonProviderConstants.CapturingModeColumns.ACTIVITY);
            return (int) (0.33f * 1048576 * ((((context.getApplicationInfo().flags & 1048576) != 0) && zzmx.zzqu()) ? zza.zza(activityManager) : activityManager.getMemoryClass()));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.zzmg
        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
        public int sizeOf(zza.C0009zza c0009zza, Bitmap bitmap) {
            return bitmap.getHeight() * bitmap.getRowBytes();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.zzmg
        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
        public void entryRemoved(boolean z, zza.C0009zza c0009zza, Bitmap bitmap, Bitmap bitmap2) {
            super.entryRemoved(z, c0009zza, bitmap, bitmap2);
        }
    }

    private final class zzc implements Runnable {
        private final Uri mUri;
        private final ParcelFileDescriptor zzadS;

        public zzc(Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.mUri = uri;
            this.zzadS = parcelFileDescriptor;
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException, IOException {
            com.google.android.gms.common.internal.zzb.zzcj("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            boolean z = false;
            Bitmap bitmapDecodeFileDescriptor = null;
            if (this.zzadS != null) {
                try {
                    bitmapDecodeFileDescriptor = BitmapFactory.decodeFileDescriptor(this.zzadS.getFileDescriptor());
                } catch (OutOfMemoryError e) {
                    Log.e("ImageManager", "OOM while loading bitmap for uri: " + this.mUri, e);
                    z = true;
                }
                try {
                    this.zzadS.close();
                } catch (IOException e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ImageManager.this.mHandler.post(ImageManager.this.new zzf(this.mUri, bitmapDecodeFileDescriptor, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException unused) {
                Log.w("ImageManager", "Latch interrupted while posting " + this.mUri);
            }
        }
    }

    private final class zzd implements Runnable {
        private final com.google.android.gms.common.images.zza zzadT;

        public zzd(com.google.android.gms.common.images.zza zzaVar) {
            this.zzadT = zzaVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            com.google.android.gms.common.internal.zzb.zzci("LoadImageRunnable must be executed on the main thread");
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.zzadN.get(this.zzadT);
            if (imageReceiver != null) {
                ImageManager.this.zzadN.remove(this.zzadT);
                imageReceiver.zzc(this.zzadT);
            }
            zza.C0009zza c0009zza = this.zzadT.zzadV;
            if (c0009zza.uri == null) {
                this.zzadT.zza(ImageManager.this.mContext, ImageManager.this.zzadM, true);
                return;
            }
            Bitmap bitmapZza = ImageManager.this.zza(c0009zza);
            if (bitmapZza != null) {
                this.zzadT.zza(ImageManager.this.mContext, bitmapZza, true);
                return;
            }
            Long l = (Long) ImageManager.this.zzadP.get(c0009zza.uri);
            if (l != null) {
                if (SystemClock.elapsedRealtime() - l.longValue() < 3600000) {
                    this.zzadT.zza(ImageManager.this.mContext, ImageManager.this.zzadM, true);
                    return;
                }
                ImageManager.this.zzadP.remove(c0009zza.uri);
            }
            this.zzadT.zza(ImageManager.this.mContext, ImageManager.this.zzadM);
            ImageReceiver imageReceiver2 = (ImageReceiver) ImageManager.this.zzadO.get(c0009zza.uri);
            if (imageReceiver2 == null) {
                imageReceiver2 = ImageManager.this.new ImageReceiver(c0009zza.uri);
                ImageManager.this.zzadO.put(c0009zza.uri, imageReceiver2);
            }
            imageReceiver2.zzb(this.zzadT);
            if (!(this.zzadT instanceof zza.zzc)) {
                ImageManager.this.zzadN.put(this.zzadT, imageReceiver2);
            }
            synchronized (ImageManager.zzadG) {
                if (!ImageManager.zzadH.contains(c0009zza.uri)) {
                    ImageManager.zzadH.add(c0009zza.uri);
                    imageReceiver2.zzoE();
                }
            }
        }
    }

    private static final class zze implements ComponentCallbacks2 {
        private final zzb zzadL;

        public zze(zzb zzbVar) {
            this.zzadL = zzbVar;
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
            this.zzadL.evictAll();
        }

        @Override // android.content.ComponentCallbacks2
        public void onTrimMemory(int i) {
            if (i >= 60) {
                this.zzadL.evictAll();
            } else if (i >= 20) {
                this.zzadL.trimToSize(this.zzadL.size() / 2);
            }
        }
    }

    private final class zzf implements Runnable {
        private final Bitmap mBitmap;
        private final Uri mUri;
        private boolean zzadU;
        private final CountDownLatch zzoS;

        public zzf(Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.zzadU = z;
            this.zzoS = countDownLatch;
        }

        private void zza(ImageReceiver imageReceiver, boolean z) {
            ArrayList arrayList = imageReceiver.zzadQ;
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                com.google.android.gms.common.images.zza zzaVar = (com.google.android.gms.common.images.zza) arrayList.get(i);
                if (z) {
                    zzaVar.zza(ImageManager.this.mContext, this.mBitmap, false);
                } else {
                    ImageManager.this.zzadP.put(this.mUri, Long.valueOf(SystemClock.elapsedRealtime()));
                    zzaVar.zza(ImageManager.this.mContext, ImageManager.this.zzadM, false);
                }
                if (!(zzaVar instanceof zza.zzc)) {
                    ImageManager.this.zzadN.remove(zzaVar);
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            com.google.android.gms.common.internal.zzb.zzci("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.mBitmap != null;
            if (ImageManager.this.zzadL != null) {
                if (this.zzadU) {
                    ImageManager.this.zzadL.evictAll();
                    System.gc();
                    this.zzadU = false;
                    ImageManager.this.mHandler.post(this);
                    return;
                }
                if (z) {
                    ImageManager.this.zzadL.put(new zza.C0009zza(this.mUri), this.mBitmap);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.zzadO.remove(this.mUri);
            if (imageReceiver != null) {
                zza(imageReceiver, z);
            }
            this.zzoS.countDown();
            synchronized (ImageManager.zzadG) {
                ImageManager.zzadH.remove(this.mUri);
            }
        }
    }

    private ImageManager(Context context, boolean z) {
        this.mContext = context.getApplicationContext();
        if (z) {
            this.zzadL = new zzb(this.mContext);
            if (zzmx.zzqx()) {
                zzoB();
            }
        } else {
            this.zzadL = null;
        }
        this.zzadM = new zzlv();
        this.zzadN = new HashMap();
        this.zzadO = new HashMap();
        this.zzadP = new HashMap();
    }

    public static ImageManager create(Context context) {
        return zzb(context, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap zza(zza.C0009zza c0009zza) {
        if (this.zzadL == null) {
            return null;
        }
        return this.zzadL.get(c0009zza);
    }

    public static ImageManager zzb(Context context, boolean z) {
        if (z) {
            if (zzadJ == null) {
                zzadJ = new ImageManager(context, true);
            }
            return zzadJ;
        }
        if (zzadI == null) {
            zzadI = new ImageManager(context, false);
        }
        return zzadI;
    }

    private void zzoB() {
        this.mContext.registerComponentCallbacks(new zze(this.zzadL));
    }

    public void loadImage(ImageView imageView, int i) {
        zza(new zza.zzb(imageView, i));
    }

    public void loadImage(ImageView imageView, Uri uri) {
        zza(new zza.zzb(imageView, uri));
    }

    public void loadImage(ImageView imageView, Uri uri, int i) {
        zza.zzb zzbVar = new zza.zzb(imageView, uri);
        zzbVar.zzby(i);
        zza(zzbVar);
    }

    public void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri) {
        zza(new zza.zzc(onImageLoadedListener, uri));
    }

    public void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri, int i) {
        zza.zzc zzcVar = new zza.zzc(onImageLoadedListener, uri);
        zzcVar.zzby(i);
        zza(zzcVar);
    }

    public void zza(com.google.android.gms.common.images.zza zzaVar) {
        com.google.android.gms.common.internal.zzb.zzci("ImageManager.loadImage() must be called in the main thread");
        new zzd(zzaVar).run();
    }
}
