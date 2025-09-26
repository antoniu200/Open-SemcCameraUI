// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.text.TextUtils;
import android.os.Looper;

public final class zzx
{
    public static void zzZ(final boolean b) {
        if (!b) {
            throw new IllegalStateException();
        }
    }
    
    public static int zza(final int n, final Object obj) {
        if (n == 0) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
        return n;
    }
    
    public static void zza(final boolean b, final Object obj) {
        if (!b) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }
    
    public static void zza(final boolean b, final String format, final Object... args) {
        if (!b) {
            throw new IllegalStateException(String.format(format, args));
        }
    }
    
    public static void zzaa(final boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }
    
    public static <T> T zzb(final T t, final Object obj) {
        if (t == null) {
            throw new NullPointerException(String.valueOf(obj));
        }
        return t;
    }
    
    public static void zzb(final boolean b, final Object obj) {
        if (!b) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
    }
    
    public static void zzb(final boolean b, final String format, final Object... args) {
        if (!b) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }
    
    public static int zzbI(final int n) {
        if (n == 0) {
            throw new IllegalArgumentException("Given Integer is zero");
        }
        return n;
    }
    
    public static void zzci(final String s) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException(s);
        }
    }
    
    public static void zzcj(final String s) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException(s);
        }
    }
    
    public static String zzcr(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("Given String is empty or null");
        }
        return s;
    }
    
    public static String zzh(final String s, final Object obj) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
        return s;
    }
    
    public static <T> T zzw(final T t) {
        if (t == null) {
            throw new NullPointerException("null reference");
        }
        return t;
    }
}
