package com.google.android.gms.common.images;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zzls;
import com.google.android.gms.internal.zzlt;
import com.google.android.gms.internal.zzlu;
import com.google.android.gms.internal.zzlv;
import java.lang.ref.WeakReference;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zza {
    final C0009zza zzadV;
    protected int zzadX;
    protected ImageManager.OnImageLoadedListener zzadZ;
    protected int zzaed;
    protected int zzadW = 0;
    protected boolean zzadY = false;
    private boolean zzaea = true;
    private boolean zzaeb = false;
    private boolean zzaec = true;

    /* renamed from: com.google.android.gms.common.images.zza$zza, reason: collision with other inner class name */
    static final class C0009zza {
        public final Uri uri;

        public C0009zza(Uri uri) {
            this.uri = uri;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0009zza)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            return zzw.equal(((C0009zza) obj).uri, this.uri);
        }

        public int hashCode() {
            return zzw.hashCode(this.uri);
        }
    }

    public static final class zzb extends zza {
        private WeakReference<ImageView> zzaee;

        public zzb(ImageView imageView, int i) {
            super(null, i);
            com.google.android.gms.common.internal.zzb.zzs(imageView);
            this.zzaee = new WeakReference<>(imageView);
        }

        public zzb(ImageView imageView, Uri uri) {
            super(uri, 0);
            com.google.android.gms.common.internal.zzb.zzs(imageView);
            this.zzaee = new WeakReference<>(imageView);
        }

        private void zza(ImageView imageView, Drawable drawable, boolean z, boolean z2, boolean z3) {
            boolean z4 = (z2 || z3) ? false : true;
            if (z4 && (imageView instanceof zzlu)) {
                int iZzoH = ((zzlu) imageView).zzoH();
                if (this.zzadX != 0 && iZzoH == this.zzadX) {
                    return;
                }
            }
            boolean zZzb = zzb(z, z2);
            if (this.zzadY && drawable != null) {
                drawable = drawable.getConstantState().newDrawable();
            }
            if (zZzb) {
                drawable = zza(imageView.getDrawable(), drawable);
            }
            imageView.setImageDrawable(drawable);
            if (imageView instanceof zzlu) {
                zzlu zzluVar = (zzlu) imageView;
                zzluVar.zzj(z3 ? this.zzadV.uri : null);
                zzluVar.zzbA(z4 ? this.zzadX : 0);
            }
            if (zZzb) {
                ((zzls) drawable).startTransition(250);
            }
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof zzb)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            ImageView imageView = this.zzaee.get();
            ImageView imageView2 = ((zzb) obj).zzaee.get();
            return (imageView2 == null || imageView == null || !zzw.equal(imageView2, imageView)) ? false : true;
        }

        public int hashCode() {
            return 0;
        }

        @Override // com.google.android.gms.common.images.zza
        protected void zza(Drawable drawable, boolean z, boolean z2, boolean z3) {
            ImageView imageView = this.zzaee.get();
            if (imageView != null) {
                zza(imageView, drawable, z, z2, z3);
            }
        }
    }

    public static final class zzc extends zza {
        private WeakReference<ImageManager.OnImageLoadedListener> zzaef;

        public zzc(ImageManager.OnImageLoadedListener onImageLoadedListener, Uri uri) {
            super(uri, 0);
            com.google.android.gms.common.internal.zzb.zzs(onImageLoadedListener);
            this.zzaef = new WeakReference<>(onImageLoadedListener);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof zzc)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            zzc zzcVar = (zzc) obj;
            ImageManager.OnImageLoadedListener onImageLoadedListener = this.zzaef.get();
            ImageManager.OnImageLoadedListener onImageLoadedListener2 = zzcVar.zzaef.get();
            return onImageLoadedListener2 != null && onImageLoadedListener != null && zzw.equal(onImageLoadedListener2, onImageLoadedListener) && zzw.equal(zzcVar.zzadV, this.zzadV);
        }

        public int hashCode() {
            return zzw.hashCode(this.zzadV);
        }

        @Override // com.google.android.gms.common.images.zza
        protected void zza(Drawable drawable, boolean z, boolean z2, boolean z3) {
            ImageManager.OnImageLoadedListener onImageLoadedListener;
            if (z2 || (onImageLoadedListener = this.zzaef.get()) == null) {
                return;
            }
            onImageLoadedListener.onImageLoaded(this.zzadV.uri, drawable, z3);
        }
    }

    public zza(Uri uri, int i) {
        this.zzadX = 0;
        this.zzadV = new C0009zza(uri);
        this.zzadX = i;
    }

    private Drawable zza(Context context, zzlv zzlvVar, int i) throws Resources.NotFoundException {
        Resources resources = context.getResources();
        if (this.zzaed <= 0) {
            return resources.getDrawable(i);
        }
        zzlv.zza zzaVar = new zzlv.zza(i, this.zzaed);
        Drawable drawableZza = zzlvVar.get(zzaVar);
        if (drawableZza == null) {
            Drawable drawable = resources.getDrawable(i);
            drawableZza = (this.zzaed & 1) != 0 ? zza(resources, drawable) : drawable;
            zzlvVar.put(zzaVar, drawableZza);
        }
        return drawableZza;
    }

    protected Drawable zza(Resources resources, Drawable drawable) {
        return zzlt.zza(resources, drawable);
    }

    protected zzls zza(Drawable drawable, Drawable drawable2) {
        if (drawable == null) {
            drawable = null;
        } else if (drawable instanceof zzls) {
            drawable = ((zzls) drawable).zzoF();
        }
        return new zzls(drawable, drawable2);
    }

    void zza(Context context, Bitmap bitmap, boolean z) {
        com.google.android.gms.common.internal.zzb.zzs(bitmap);
        if ((this.zzaed & 1) != 0) {
            bitmap = zzlt.zza(bitmap);
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        if (this.zzadZ != null) {
            this.zzadZ.onImageLoaded(this.zzadV.uri, bitmapDrawable, true);
        }
        zza(bitmapDrawable, z, false, true);
    }

    void zza(Context context, zzlv zzlvVar) {
        if (this.zzaec) {
            zza(this.zzadW != 0 ? zza(context, zzlvVar, this.zzadW) : null, false, true, false);
        }
    }

    void zza(Context context, zzlv zzlvVar, boolean z) {
        Drawable drawableZza = this.zzadX != 0 ? zza(context, zzlvVar, this.zzadX) : null;
        if (this.zzadZ != null) {
            this.zzadZ.onImageLoaded(this.zzadV.uri, drawableZza, false);
        }
        zza(drawableZza, z, false, false);
    }

    protected abstract void zza(Drawable drawable, boolean z, boolean z2, boolean z3);

    protected boolean zzb(boolean z, boolean z2) {
        if (!this.zzaea || z2) {
            return false;
        }
        return !z || this.zzaeb;
    }

    public void zzby(int i) {
        this.zzadX = i;
    }
}
