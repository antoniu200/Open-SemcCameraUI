// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.stats;

import android.text.TextUtils;
import android.os.Process;
import android.os.PowerManager$WakeLock;

public class zzg
{
    public static String zza(final PowerManager$WakeLock powerManager$WakeLock, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf((long)Process.myPid() << 32 | (long)System.identityHashCode(powerManager$WakeLock)));
        String str = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            str = "";
        }
        sb.append(str);
        return sb.toString();
    }
}
