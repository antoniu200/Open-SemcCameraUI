// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.images;

import com.google.android.gms.internal.zzlu;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import com.google.android.gms.common.internal.zzw;
import android.graphics.drawable.BitmapDrawable;
import com.google.android.gms.common.internal.zzb;
import android.graphics.Bitmap;
import com.google.android.gms.internal.zzls;
import com.google.android.gms.internal.zzlt;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.google.android.gms.internal.zzlv;
import android.content.Context;
import android.net.Uri;

public abstract class zza
{
    final zza zzadV;
    protected int zzadW;
    protected int zzadX;
    protected boolean zzadY;
    protected ImageManager.OnImageLoadedListener zzadZ;
    private boolean zzaea;
    private boolean zzaeb;
    private boolean zzaec;
    protected int zzaed;
    
    public zza(final Uri uri, final int zzadX) {
        this.zzadW = 0;
        this.zzadX = 0;
        this.zzadY = false;
        this.zzaea = true;
        this.zzaeb = false;
        this.zzaec = true;
        this.zzadV = new zza(uri);
        this.zzadX = zzadX;
    }
    
    private Drawable zza(final Context context, final zzlv zzlv, final int n) {
        final Resources resources = context.getResources();
        if (this.zzaed > 0) {
            final zzlv.zza zza = new zzlv.zza(n, this.zzaed);
            Drawable drawable;
            if ((drawable = zzlv.get(zza)) == null) {
                drawable = resources.getDrawable(n);
                if ((this.zzaed & 0x1) != 0x0) {
                    drawable = this.zza(resources, drawable);
                }
                zzlv.put(zza, drawable);
            }
            return drawable;
        }
        return resources.getDrawable(n);
    }
    
    protected Drawable zza(final Resources resources, final Drawable drawable) {
        return zzlt.zza(resources, drawable);
    }
    
    protected zzls zza(final Drawable drawable, final Drawable drawable2) {
        Drawable zzoF;
        if (drawable != null) {
            zzoF = drawable;
            if (drawable instanceof zzls) {
                zzoF = ((zzls)drawable).zzoF();
            }
        }
        else {
            zzoF = null;
        }
        return new zzls(zzoF, drawable2);
    }
    
    void zza(final Context context, final Bitmap bitmap, final boolean b) {
        com.google.android.gms.common.internal.zzb.zzs(bitmap);
        Bitmap zza = bitmap;
        if ((this.zzaed & 0x1) != 0x0) {
            zza = zzlt.zza(bitmap);
        }
        final BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), zza);
        if (this.zzadZ != null) {
            this.zzadZ.onImageLoaded(this.zzadV.uri, (Drawable)bitmapDrawable, true);
        }
        this.zza((Drawable)bitmapDrawable, b, false, true);
    }
    
    void zza(final Context context, final zzlv zzlv) {
        if (this.zzaec) {
            Drawable zza = null;
            if (this.zzadW != 0) {
                zza = this.zza(context, zzlv, this.zzadW);
            }
            this.zza(zza, false, true, false);
        }
    }
    
    void zza(final Context context, final zzlv zzlv, final boolean b) {
        Drawable zza;
        if (this.zzadX != 0) {
            zza = this.zza(context, zzlv, this.zzadX);
        }
        else {
            zza = null;
        }
        if (this.zzadZ != null) {
            this.zzadZ.onImageLoaded(this.zzadV.uri, zza, false);
        }
        this.zza(zza, b, false, false);
    }
    
    protected abstract void zza(final Drawable p0, final boolean p1, final boolean p2, final boolean p3);
    
    protected boolean zzb(final boolean b, final boolean b2) {
        return this.zzaea && !b2 && (!b || this.zzaeb);
    }
    
    public void zzby(final int zzadX) {
        this.zzadX = zzadX;
    }
    
    static final class zza
    {
        public final Uri uri;
        
        public zza(final Uri uri) {
            this.uri = uri;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof zza && (this == o || zzw.equal(((zza)o).uri, this.uri));
        }
        
        @Override
        public int hashCode() {
            return zzw.hashCode(this.uri);
        }
    }
    
    public static final class zzb extends zza
    {
        private WeakReference<ImageView> zzaee;
        
        public zzb(final ImageView referent, final int n) {
            super(null, n);
            com.google.android.gms.common.internal.zzb.zzs(referent);
            this.zzaee = new WeakReference<ImageView>(referent);
        }
        
        public zzb(final ImageView referent, final Uri uri) {
            super(uri, 0);
            com.google.android.gms.common.internal.zzb.zzs(referent);
            this.zzaee = new WeakReference<ImageView>(referent);
        }
        
        private void zza(final ImageView imageView, Drawable zza, final boolean b, final boolean b2, final boolean b3) {
            int zzadX = 0;
            final boolean b4 = !b2 && !b3;
            if (b4 && imageView instanceof zzlu) {
                final int zzoH = ((zzlu)imageView).zzoH();
                if (this.zzadX != 0 && zzoH == this.zzadX) {
                    return;
                }
            }
            final boolean zzb = this.zzb(b, b2);
            Drawable drawable = zza;
            if (this.zzadY && (drawable = zza) != null) {
                drawable = zza.getConstantState().newDrawable();
            }
            zza = drawable;
            if (zzb) {
                zza = this.zza(imageView.getDrawable(), drawable);
            }
            imageView.setImageDrawable(zza);
            if (imageView instanceof zzlu) {
                final zzlu zzlu = (zzlu)imageView;
                Uri uri;
                if (b3) {
                    uri = this.zzadV.uri;
                }
                else {
                    uri = null;
                }
                zzlu.zzj(uri);
                if (b4) {
                    zzadX = this.zzadX;
                }
                zzlu.zzbA(zzadX);
            }
            if (zzb) {
                ((zzls)zza).startTransition(250);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof zzb)) {
                return false;
            }
            if (this == o) {
                return true;
            }
            final zzb zzb = (zzb)o;
            final ImageView imageView = this.zzaee.get();
            final ImageView imageView2 = zzb.zzaee.get();
            return imageView2 != null && imageView != null && zzw.equal(imageView2, imageView);
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        protected void zza(final Drawable drawable, final boolean b, final boolean b2, final boolean b3) {
            final ImageView imageView = this.zzaee.get();
            if (imageView != null) {
                this.zza(imageView, drawable, b, b2, b3);
            }
        }
    }
    
    public static final class zzc extends zza
    {
        private WeakReference<ImageManager.OnImageLoadedListener> zzaef;
        
        public zzc(final ImageManager.OnImageLoadedListener referent, final Uri uri) {
            super(uri, 0);
            com.google.android.gms.common.internal.zzb.zzs(referent);
            this.zzaef = new WeakReference<ImageManager.OnImageLoadedListener>(referent);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof zzc)) {
                return false;
            }
            if (this == o) {
                return true;
            }
            final zzc zzc = (zzc)o;
            final ImageManager.OnImageLoadedListener onImageLoadedListener = this.zzaef.get();
            final ImageManager.OnImageLoadedListener onImageLoadedListener2 = zzc.zzaef.get();
            return onImageLoadedListener2 != null && onImageLoadedListener != null && zzw.equal(onImageLoadedListener2, onImageLoadedListener) && zzw.equal(zzc.zzadV, this.zzadV);
        }
        
        @Override
        public int hashCode() {
            return zzw.hashCode(this.zzadV);
        }
        
        @Override
        protected void zza(final Drawable drawable, final boolean b, final boolean b2, final boolean b3) {
            if (!b2) {
                final ImageManager.OnImageLoadedListener onImageLoadedListener = this.zzaef.get();
                if (onImageLoadedListener != null) {
                    onImageLoadedListener.onImageLoaded(this.zzadV.uri, drawable, b3);
                }
            }
        }
    }
}
