package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzmr {
    private static IntentFilter zzail = new IntentFilter("android.intent.action.BATTERY_CHANGED");

    public static int zzao(Context context) {
        if (context == null || context.getApplicationContext() == null) {
            return -1;
        }
        Intent intentRegisterReceiver = context.getApplicationContext().registerReceiver(null, zzail);
        return ((zzmx.zzqC() ? ((PowerManager) context.getSystemService("power")).isInteractive() : ((PowerManager) context.getSystemService("power")).isScreenOn() ? 1 : 0) << 1) | (((intentRegisterReceiver == null ? 0 : intentRegisterReceiver.getIntExtra("plugged", 0)) & 7) != 0 ? 1 : 0);
    }

    public static float zzap(Context context) {
        if (context.getApplicationContext().registerReceiver(null, zzail) == null) {
            return Float.NaN;
        }
        return r3.getIntExtra("level", -1) / r3.getIntExtra("scale", -1);
    }
}
