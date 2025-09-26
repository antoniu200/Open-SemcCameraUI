// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public abstract class DowngradeableSafeParcel implements SafeParcelable
{
    private static final Object zzafm;
    private static ClassLoader zzafn;
    private static Integer zzafo;
    private boolean zzafp;
    
    static {
        zzafm = new Object();
    }
    
    public DowngradeableSafeParcel() {
        this.zzafp = false;
    }
    
    private static boolean zza(final Class<?> clazz) {
        try {
            return "SAFE_PARCELABLE_NULL_STRING".equals(clazz.getField("NULL").get(null));
        }
        catch (final NoSuchFieldException | IllegalAccessException ex) {
            return false;
        }
    }
    
    protected static boolean zzck(final String name) {
        final ClassLoader zzoS = zzoS();
        if (zzoS == null) {
            return true;
        }
        try {
            return zza(zzoS.loadClass(name));
        }
        catch (final Exception ex) {
            return false;
        }
    }
    
    protected static ClassLoader zzoS() {
        synchronized (DowngradeableSafeParcel.zzafm) {
            return DowngradeableSafeParcel.zzafn;
        }
    }
    
    protected static Integer zzoT() {
        synchronized (DowngradeableSafeParcel.zzafm) {
            return DowngradeableSafeParcel.zzafo;
        }
    }
    
    protected boolean zzoU() {
        return this.zzafp;
    }
}
