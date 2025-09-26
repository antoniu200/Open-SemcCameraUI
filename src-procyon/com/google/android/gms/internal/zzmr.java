// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.content.Intent;
import android.os.PowerManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public final class zzmr
{
    private static IntentFilter zzail;
    
    static {
        zzmr.zzail = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    }
    
    public static int zzao(final Context context) {
        if (context != null && context.getApplicationContext() != null) {
            final Intent registerReceiver = context.getApplicationContext().registerReceiver((BroadcastReceiver)null, zzmr.zzail);
            int n = 0;
            int intExtra;
            if (registerReceiver == null) {
                intExtra = 0;
            }
            else {
                intExtra = registerReceiver.getIntExtra("plugged", 0);
            }
            final boolean b = (intExtra & 0x7) != 0x0;
            boolean b2;
            if (zzmx.zzqC()) {
                b2 = ((PowerManager)context.getSystemService("power")).isInteractive();
            }
            else {
                b2 = ((PowerManager)context.getSystemService("power")).isScreenOn();
            }
            if (b2) {
                n = 1;
            }
            return n << 1 | (b ? 1 : 0);
        }
        return -1;
    }
    
    public static float zzap(final Context context) {
        final Intent registerReceiver = context.getApplicationContext().registerReceiver((BroadcastReceiver)null, zzmr.zzail);
        if (registerReceiver != null) {
            return registerReceiver.getIntExtra("level", -1) / (float)registerReceiver.getIntExtra("scale", -1);
        }
        return Float.NaN;
    }
}
