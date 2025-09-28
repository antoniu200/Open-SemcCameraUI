package com.google.android.gms.common.stats;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.stats.zzc;
import com.google.android.gms.internal.zzmm;
import com.google.android.gms.internal.zzmr;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzi {
    private static String TAG = "WakeLockTracker";
    private static Integer zzahE;
    private static zzi zzaii = new zzi();

    private static int getLogLevel() {
        try {
            return zzmm.zzjA() ? zzc.zzb.zzahH.get().intValue() : zzd.LOG_LEVEL_OFF;
        } catch (SecurityException unused) {
            return zzd.LOG_LEVEL_OFF;
        }
    }

    private static boolean zzam(Context context) {
        if (zzahE == null) {
            zzahE = Integer.valueOf(getLogLevel());
        }
        return zzahE.intValue() != zzd.LOG_LEVEL_OFF;
    }

    public static zzi zzqr() {
        return zzaii;
    }

    public void zza(Context context, String str, int i, String str2, String str3, int i2, List<String> list) {
        zza(context, str, i, str2, str3, i2, list, 0L);
    }

    public void zza(Context context, String str, int i, String str2, String str3, int i2, List<String> list, long j) {
        if (zzam(context)) {
            if (TextUtils.isEmpty(str)) {
                Log.e(TAG, "missing wakeLock key. " + str);
                return;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (7 == i || 8 == i || 10 == i || 11 == i) {
                try {
                    context.startService(new Intent().setComponent(zzd.zzahN).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", new WakeLockEvent(jCurrentTimeMillis, i, str2, i2, list, str, SystemClock.elapsedRealtime(), zzmr.zzao(context), str3, context.getPackageName(), zzmr.zzap(context), j)));
                } catch (Exception e) {
                    Log.wtf(TAG, e);
                }
            }
        }
    }
}
