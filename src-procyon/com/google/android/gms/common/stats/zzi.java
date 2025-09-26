// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.stats;

import android.os.Parcelable;
import android.content.Intent;
import com.google.android.gms.internal.zzmr;
import android.os.SystemClock;
import android.util.Log;
import android.text.TextUtils;
import java.util.List;
import android.content.Context;
import com.google.android.gms.internal.zzmm;

public class zzi
{
    private static String TAG = "WakeLockTracker";
    private static Integer zzahE;
    private static zzi zzaii;
    
    static {
        zzi.zzaii = new zzi();
    }
    
    private static int getLogLevel() {
        try {
            if (zzmm.zzjA()) {
                return zzc.zzb.zzahH.get();
            }
            return zzd.LOG_LEVEL_OFF;
        }
        catch (final SecurityException ex) {
            return zzd.LOG_LEVEL_OFF;
        }
    }
    
    private static boolean zzam(final Context context) {
        if (zzi.zzahE == null) {
            zzi.zzahE = getLogLevel();
        }
        return zzi.zzahE != zzd.LOG_LEVEL_OFF;
    }
    
    public static zzi zzqr() {
        return zzi.zzaii;
    }
    
    public void zza(final Context context, final String s, final int n, final String s2, final String s3, final int n2, final List<String> list) {
        this.zza(context, s, n, s2, s3, n2, list, 0L);
    }
    
    public void zza(final Context context, final String str, final int n, String tag, final String s, final int n2, final List<String> list, final long n3) {
        if (!zzam(context)) {
            return;
        }
        if (TextUtils.isEmpty((CharSequence)str)) {
            tag = zzi.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("missing wakeLock key. ");
            sb.append(str);
            Log.e(tag, sb.toString());
            return;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        if (7 == n || 8 == n || 10 == n || 11 == n) {
            final WakeLockEvent wakeLockEvent = new WakeLockEvent(currentTimeMillis, n, tag, n2, list, str, SystemClock.elapsedRealtime(), zzmr.zzao(context), s, context.getPackageName(), zzmr.zzap(context), n3);
            try {
                context.startService(new Intent().setComponent(zzd.zzahN).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", (Parcelable)wakeLockEvent));
            }
            catch (final Exception ex) {
                Log.wtf(zzi.TAG, (Throwable)ex);
            }
        }
    }
}
