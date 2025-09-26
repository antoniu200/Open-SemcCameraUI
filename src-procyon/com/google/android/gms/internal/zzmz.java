// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

public final class zzmz
{
    private static int zza(final StackTraceElement[] array, final StackTraceElement[] array2) {
        int length = array2.length;
        int length2 = array.length;
        int n = 0;
        while (--length2 >= 0 && --length >= 0 && array2[length].equals(array[length2])) {
            ++n;
        }
        return n;
    }
    
    public static String zzqF() {
        final StringBuilder sb = new StringBuilder();
        final Throwable t = new Throwable();
        StackTraceElement[] stackTrace = t.getStackTrace();
        sb.append("Async stack trace:");
        for (final StackTraceElement obj : stackTrace) {
            sb.append("\n\tat ");
            sb.append(obj);
        }
        StackTraceElement[] stackTrace2;
        for (Throwable obj2 = t.getCause(); obj2 != null; obj2 = obj2.getCause(), stackTrace = stackTrace2) {
            sb.append("\nCaused by: ");
            sb.append(obj2);
            stackTrace2 = obj2.getStackTrace();
            final int zza = zza(stackTrace2, stackTrace);
            for (int j = 0; j < stackTrace2.length - zza; ++j) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\n\tat ");
                sb2.append(stackTrace2[j]);
                sb.append(sb2.toString());
            }
            if (zza > 0) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("\n\t... ");
                sb3.append(zza);
                sb3.append(" more");
                sb.append(sb3.toString());
            }
        }
        return sb.toString();
    }
}
