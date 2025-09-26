// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzw;
import android.graphics.drawable.Drawable;

public final class zzlv extends zzmg<zza, Drawable>
{
    public zzlv() {
        super(10);
    }
    
    public static final class zza
    {
        public final int zzaeE;
        public final int zzaeF;
        
        public zza(final int zzaeE, final int zzaeF) {
            this.zzaeE = zzaeE;
            this.zzaeF = zzaeF;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof zza)) {
                return false;
            }
            if (this == o) {
                return true;
            }
            final zza zza = (zza)o;
            return zza.zzaeE == this.zzaeE && zza.zzaeF == this.zzaeF;
        }
        
        @Override
        public int hashCode() {
            return zzw.hashCode(this.zzaeE, this.zzaeF);
        }
    }
}
