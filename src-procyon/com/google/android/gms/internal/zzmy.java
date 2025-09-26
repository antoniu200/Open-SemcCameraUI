// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Iterator;
import java.util.List;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.app.ActivityManager;
import android.os.Binder;
import android.content.Context;

public class zzmy
{
    private static String zza(final StackTraceElement[] array, int n) {
        n += 4;
        if (n >= array.length) {
            return "<bottom of call stack>";
        }
        final StackTraceElement stackTraceElement = array[n];
        final StringBuilder sb = new StringBuilder();
        sb.append(stackTraceElement.getClassName());
        sb.append(".");
        sb.append(stackTraceElement.getMethodName());
        sb.append(":");
        sb.append(stackTraceElement.getLineNumber());
        return sb.toString();
    }
    
    public static String zzaq(final Context context) {
        return zzj(context, Binder.getCallingPid());
    }
    
    public static String zzj(final Context context, final int n) {
        final List runningAppProcesses = ((ActivityManager)context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo : runningAppProcesses) {
                if (activityManager$RunningAppProcessInfo.pid == n) {
                    return activityManager$RunningAppProcessInfo.processName;
                }
            }
        }
        return null;
    }
    
    public static String zzl(final int n, final int n2) {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final StringBuffer sb = new StringBuffer();
        for (int i = n; i < n2 + n; ++i) {
            sb.append(zza(stackTrace, i));
            sb.append(" ");
        }
        return sb.toString();
    }
}
