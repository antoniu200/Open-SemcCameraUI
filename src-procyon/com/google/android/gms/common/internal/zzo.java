// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.util.Log;
import com.google.android.gms.internal.zzqc;
import android.content.Context;

public final class zzo
{
    public static final int zzagk;
    private static final String zzagl;
    private final String zzagm;
    private final String zzagn;
    
    static {
        zzagk = 23 - " PII_LOG".length();
    }
    
    public zzo(final String s) {
        this(s, zzo.zzagl);
    }
    
    public zzo(final String zzagm, final String zzagn) {
        zzx.zzb(zzagm, "log tag cannot be null");
        zzx.zzb(zzagm.length() <= 23, "tag \"%s\" is longer than the %d character maximum", zzagm, 23);
        this.zzagm = zzagm;
        if (zzagn != null && zzagn.length() > 0) {
            this.zzagn = zzagn;
            return;
        }
        this.zzagn = zzo.zzagl;
    }
    
    private String zzcp(final String str) {
        if (this.zzagn == null) {
            return str;
        }
        return this.zzagn.concat(str);
    }
    
    public void zza(final Context context, final String s, final String s2, final Throwable t) {
        final StackTraceElement[] stackTrace = t.getStackTrace();
        final StringBuilder sb = new StringBuilder();
        for (int n = 0; n < stackTrace.length && n < 2; ++n) {
            sb.append(stackTrace[n].toString());
            sb.append("\n");
        }
        final zzqc zzqc = new zzqc(context, 10);
        zzqc.zza("GMS_WTF", null, "GMS_WTF", sb.toString());
        zzqc.send();
        if (this.zzbH(7)) {
            Log.e(s, this.zzcp(s2), t);
            Log.wtf(s, this.zzcp(s2), t);
        }
    }
    
    public void zza(final String s, final String s2, final Throwable t) {
        if (this.zzbH(4)) {
            Log.i(s, this.zzcp(s2), t);
        }
    }
    
    public void zzb(final String s, final String s2, final Throwable t) {
        if (this.zzbH(5)) {
            Log.w(s, this.zzcp(s2), t);
        }
    }
    
    public boolean zzbH(final int n) {
        return Log.isLoggable(this.zzagm, n);
    }
    
    public void zzc(final String s, final String s2, final Throwable t) {
        if (this.zzbH(6)) {
            Log.e(s, this.zzcp(s2), t);
        }
    }
    
    public void zzx(final String s, final String s2) {
        if (this.zzbH(3)) {
            Log.d(s, this.zzcp(s2));
        }
    }
    
    public void zzy(final String s, final String s2) {
        if (this.zzbH(5)) {
            Log.w(s, this.zzcp(s2));
        }
    }
    
    public void zzz(final String s, final String s2) {
        if (this.zzbH(6)) {
            Log.e(s, this.zzcp(s2));
        }
    }
}
