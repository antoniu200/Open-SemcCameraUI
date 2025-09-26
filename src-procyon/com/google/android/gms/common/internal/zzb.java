// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.util.Log;
import android.os.Looper;

public final class zzb
{
    public static void zzZ(final boolean b) {
        if (!b) {
            throw new IllegalStateException();
        }
    }
    
    public static void zza(final boolean b, final Object obj) {
        if (!b) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }
    
    public static void zzci(final String s) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkMainThread: current thread ");
            sb.append(Thread.currentThread());
            sb.append(" IS NOT the main thread ");
            sb.append(Looper.getMainLooper().getThread());
            sb.append("!");
            Log.e("Asserts", sb.toString());
            throw new IllegalStateException(s);
        }
    }
    
    public static void zzcj(final String s) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkNotMainThread: current thread ");
            sb.append(Thread.currentThread());
            sb.append(" IS the main thread ");
            sb.append(Looper.getMainLooper().getThread());
            sb.append("!");
            Log.e("Asserts", sb.toString());
            throw new IllegalStateException(s);
        }
    }
    
    public static void zzs(final Object o) {
        if (o == null) {
            throw new IllegalArgumentException("null reference");
        }
    }
}
