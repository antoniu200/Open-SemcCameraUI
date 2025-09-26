// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.util.Log;
import android.content.Context;

@Deprecated
public class zzqc implements zza
{
    private final zzqd zzaRC;
    private boolean zzaRD;
    
    public zzqc(final Context context, final int n) {
        this(context, n, null);
    }
    
    public zzqc(final Context context, final int n, final String s) {
        this(context, n, s, null, true);
    }
    
    public zzqc(final Context context, final int n, final String s, final String s2, final boolean b) {
        String name;
        if (context != context.getApplicationContext()) {
            name = context.getClass().getName();
        }
        else {
            name = "OneTimePlayLogger";
        }
        this.zzaRC = new zzqd(context, n, s, s2, (zzqd.zza)this, b, name);
        this.zzaRD = true;
    }
    
    private void zzBq() {
        if (!this.zzaRD) {
            throw new IllegalStateException("Cannot reuse one-time logger after sending.");
        }
    }
    
    public void send() {
        this.zzBq();
        this.zzaRC.start();
        this.zzaRD = false;
    }
    
    @Override
    public void zzBr() {
        this.zzaRC.stop();
    }
    
    @Override
    public void zzBs() {
        Log.w("OneTimePlayLogger", "logger connection failed");
    }
    
    public void zza(final String s, final byte[] array, final String... array2) {
        this.zzBq();
        this.zzaRC.zzb(s, array, array2);
    }
    
    @Override
    public void zzf(final PendingIntent obj) {
        final StringBuilder sb = new StringBuilder();
        sb.append("logger connection failed: ");
        sb.append(obj);
        Log.w("OneTimePlayLogger", sb.toString());
    }
}
