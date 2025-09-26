// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable$ConstantState;
import android.os.SystemClock;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable$Callback;
import android.graphics.drawable.Drawable;

public final class zzls extends Drawable implements Drawable$Callback
{
    private int mFrom;
    private long zzNY;
    private boolean zzaea;
    private int zzaeh;
    private int zzaei;
    private int zzaej;
    private int zzaek;
    private int zzael;
    private boolean zzaem;
    private zzb zzaen;
    private Drawable zzaeo;
    private Drawable zzaep;
    private boolean zzaeq;
    private boolean zzaer;
    private boolean zzaes;
    private int zzaet;
    
    public zzls(Drawable zzoG, final Drawable drawable) {
        this(null);
        Drawable zzoG2 = zzoG;
        if (zzoG == null) {
            zzoG2 = zza.zzaeu;
        }
        (this.zzaeo = zzoG2).setCallback((Drawable$Callback)this);
        final zzb zzaen = this.zzaen;
        zzaen.zzaex |= zzoG2.getChangingConfigurations();
        if ((zzoG = drawable) == null) {
            zzoG = zza.zzaeu;
        }
        (this.zzaep = zzoG).setCallback((Drawable$Callback)this);
        final zzb zzaen2 = this.zzaen;
        zzaen2.zzaex |= zzoG.getChangingConfigurations();
    }
    
    zzls(final zzb zzb) {
        this.zzaeh = 0;
        this.zzaej = 255;
        this.zzael = 0;
        this.zzaea = true;
        this.zzaen = new zzb(zzb);
    }
    
    public boolean canConstantState() {
        if (!this.zzaeq) {
            this.zzaer = (this.zzaeo.getConstantState() != null && this.zzaep.getConstantState() != null);
            this.zzaeq = true;
        }
        return this.zzaer;
    }
    
    public void draw(final Canvas canvas) {
        final int zzaeh = this.zzaeh;
        int n = 1;
        final int n2 = 1;
        switch (zzaeh) {
            case 2: {
                if (this.zzNY >= 0L) {
                    final float a = (SystemClock.uptimeMillis() - this.zzNY) / (float)this.zzaek;
                    if (a >= 1.0f) {
                        n = n2;
                    }
                    else {
                        n = 0;
                    }
                    if (n != 0) {
                        this.zzaeh = 0;
                    }
                    this.zzael = (int)(this.mFrom + (this.zzaei - this.mFrom) * Math.min(a, 1.0f));
                    break;
                }
                break;
            }
            case 1: {
                this.zzNY = SystemClock.uptimeMillis();
                this.zzaeh = 2;
                n = 0;
                break;
            }
        }
        final int zzael = this.zzael;
        final boolean zzaea = this.zzaea;
        final Drawable zzaeo = this.zzaeo;
        final Drawable zzaep = this.zzaep;
        if (n != 0) {
            if (!zzaea || zzael == 0) {
                zzaeo.draw(canvas);
            }
            if (zzael == this.zzaej) {
                zzaep.setAlpha(this.zzaej);
                zzaep.draw(canvas);
            }
            return;
        }
        if (zzaea) {
            zzaeo.setAlpha(this.zzaej - zzael);
        }
        zzaeo.draw(canvas);
        if (zzaea) {
            zzaeo.setAlpha(this.zzaej);
        }
        if (zzael > 0) {
            zzaep.setAlpha(zzael);
            zzaep.draw(canvas);
            zzaep.setAlpha(this.zzaej);
        }
        this.invalidateSelf();
    }
    
    public int getChangingConfigurations() {
        return this.zzaen.zzaex | (super.getChangingConfigurations() | this.zzaen.zzaew);
    }
    
    public Drawable$ConstantState getConstantState() {
        if (this.canConstantState()) {
            this.zzaen.zzaew = this.getChangingConfigurations();
            return this.zzaen;
        }
        return null;
    }
    
    public int getIntrinsicHeight() {
        return Math.max(this.zzaeo.getIntrinsicHeight(), this.zzaep.getIntrinsicHeight());
    }
    
    public int getIntrinsicWidth() {
        return Math.max(this.zzaeo.getIntrinsicWidth(), this.zzaep.getIntrinsicWidth());
    }
    
    public int getOpacity() {
        if (!this.zzaes) {
            this.zzaet = Drawable.resolveOpacity(this.zzaeo.getOpacity(), this.zzaep.getOpacity());
            this.zzaes = true;
        }
        return this.zzaet;
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        if (zzmx.zzqu()) {
            final Drawable$Callback callback = this.getCallback();
            if (callback != null) {
                callback.invalidateDrawable((Drawable)this);
            }
        }
    }
    
    public Drawable mutate() {
        if (!this.zzaem && super.mutate() == this) {
            if (!this.canConstantState()) {
                throw new IllegalStateException("One or more children of this LayerDrawable does not have constant state; this drawable cannot be mutated.");
            }
            this.zzaeo.mutate();
            this.zzaep.mutate();
            this.zzaem = true;
        }
        return this;
    }
    
    protected void onBoundsChange(final Rect rect) {
        this.zzaeo.setBounds(rect);
        this.zzaep.setBounds(rect);
    }
    
    public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
        if (zzmx.zzqu()) {
            final Drawable$Callback callback = this.getCallback();
            if (callback != null) {
                callback.scheduleDrawable((Drawable)this, runnable, n);
            }
        }
    }
    
    public void setAlpha(final int n) {
        if (this.zzael == this.zzaej) {
            this.zzael = n;
        }
        this.zzaej = n;
        this.invalidateSelf();
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.zzaeo.setColorFilter(colorFilter);
        this.zzaep.setColorFilter(colorFilter);
    }
    
    public void startTransition(final int zzaek) {
        this.mFrom = 0;
        this.zzaei = this.zzaej;
        this.zzael = 0;
        this.zzaek = zzaek;
        this.zzaeh = 1;
        this.invalidateSelf();
    }
    
    public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
        if (zzmx.zzqu()) {
            final Drawable$Callback callback = this.getCallback();
            if (callback != null) {
                callback.unscheduleDrawable((Drawable)this, runnable);
            }
        }
    }
    
    public Drawable zzoF() {
        return this.zzaep;
    }
    
    private static final class zza extends Drawable
    {
        private static final zzls.zza zzaeu;
        private static final zzls.zza.zza zzaev;
        
        static {
            zzaeu = new zzls.zza();
            zzaev = new zzls.zza.zza();
        }
        
        public void draw(final Canvas canvas) {
        }
        
        public Drawable$ConstantState getConstantState() {
            return zzls.zza.zzaev;
        }
        
        public int getOpacity() {
            return -2;
        }
        
        public void setAlpha(final int n) {
        }
        
        public void setColorFilter(final ColorFilter colorFilter) {
        }
        
        private static final class zza extends Drawable$ConstantState
        {
            public int getChangingConfigurations() {
                return 0;
            }
            
            public Drawable newDrawable() {
                return zzls.zza.zzaeu;
            }
        }
    }
    
    static final class zzb extends Drawable$ConstantState
    {
        int zzaew;
        int zzaex;
        
        zzb(final zzb zzb) {
            if (zzb != null) {
                this.zzaew = zzb.zzaew;
                this.zzaex = zzb.zzaex;
            }
        }
        
        public int getChangingConfigurations() {
            return this.zzaew;
        }
        
        public Drawable newDrawable() {
            return new zzls(this);
        }
    }
}
