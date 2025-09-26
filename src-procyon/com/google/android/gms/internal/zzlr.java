// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.Binder;

public abstract class zzlr<T>
{
    private static zza zzadc;
    private static int zzadd = 0;
    private static String zzade = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    private static final Object zzpy;
    private T zzOX;
    protected final String zzue;
    protected final T zzuf;
    
    static {
        zzpy = new Object();
    }
    
    protected zzlr(final String zzue, final T zzuf) {
        this.zzOX = null;
        this.zzue = zzue;
        this.zzuf = zzuf;
    }
    
    public static boolean isInitialized() {
        return zzlr.zzadc != null;
    }
    
    public static zzlr<Float> zza(final String s, final Float n) {
        return new zzlr<Float>(s, n) {
            protected Float zzcc(final String s) {
                return zzlr.zzadc.zzb(this.zzue, (Float)this.zzuf);
            }
        };
    }
    
    public static zzlr<Integer> zza(final String s, final Integer n) {
        return new zzlr<Integer>(s, n) {
            protected Integer zzcb(final String s) {
                return zzlr.zzadc.zzb(this.zzue, (Integer)this.zzuf);
            }
        };
    }
    
    public static zzlr<Long> zza(final String s, final Long n) {
        return new zzlr<Long>(s, n) {
            protected Long zzca(final String s) {
                return zzlr.zzadc.getLong(this.zzue, (Long)this.zzuf);
            }
        };
    }
    
    public static zzlr<Boolean> zzg(final String s, final boolean b) {
        return new zzlr<Boolean>(s, Boolean.valueOf(b)) {
            protected Boolean zzbZ(final String s) {
                return zzlr.zzadc.zzb(this.zzue, (Boolean)this.zzuf);
            }
        };
    }
    
    public static int zzoo() {
        return zzlr.zzadd;
    }
    
    public static zzlr<String> zzu(final String s, final String s2) {
        return new zzlr<String>(s, s2) {
            protected String zzcd(final String s) {
                return zzlr.zzadc.getString(this.zzue, (String)this.zzuf);
            }
        };
    }
    
    public final T get() {
        if (this.zzOX != null) {
            return this.zzOX;
        }
        return this.zzbY(this.zzue);
    }
    
    protected abstract T zzbY(final String p0);
    
    public final T zzop() {
        final long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.get();
        }
        finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
    
    private interface zza
    {
        Long getLong(final String p0, final Long p1);
        
        String getString(final String p0, final String p1);
        
        Boolean zzb(final String p0, final Boolean p1);
        
        Float zzb(final String p0, final Float p1);
        
        Integer zzb(final String p0, final Integer p1);
    }
}
