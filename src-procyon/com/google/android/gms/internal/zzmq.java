// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.content.res.Configuration;
import android.content.res.Resources;

public final class zzmq
{
    public static boolean zzb(final Resources resources) {
        boolean b = false;
        if (resources == null) {
            return false;
        }
        final boolean b2 = (resources.getConfiguration().screenLayout & 0xF) > 3;
        if ((zzmx.zzqu() && b2) || zzc(resources)) {
            b = true;
        }
        return b;
    }
    
    private static boolean zzc(final Resources resources) {
        final Configuration configuration = resources.getConfiguration();
        final boolean zzqw = zzmx.zzqw();
        boolean b2;
        final boolean b = b2 = false;
        if (zzqw) {
            b2 = b;
            if ((configuration.screenLayout & 0xF) <= 3) {
                b2 = b;
                if (configuration.smallestScreenWidthDp >= 600) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
}
