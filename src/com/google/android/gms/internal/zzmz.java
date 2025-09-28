package com.google.android.gms.internal;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzmz {
    private static int zza(StackTraceElement[] stackTraceElementArr, StackTraceElement[] stackTraceElementArr2) {
        int length = stackTraceElementArr2.length;
        int length2 = stackTraceElementArr.length;
        int i = 0;
        while (true) {
            length2--;
            if (length2 < 0 || length - 1 < 0 || !stackTraceElementArr2[length].equals(stackTraceElementArr[length2])) {
                break;
            }
            i++;
        }
        return i;
    }

    public static String zzqF() {
        StringBuilder sb = new StringBuilder();
        Throwable th = new Throwable();
        StackTraceElement[] stackTrace = th.getStackTrace();
        sb.append("Async stack trace:");
        for (StackTraceElement stackTraceElement : stackTrace) {
            sb.append("\n\tat ");
            sb.append(stackTraceElement);
        }
        Throwable cause = th.getCause();
        while (cause != null) {
            sb.append("\nCaused by: ");
            sb.append(cause);
            StackTraceElement[] stackTrace2 = cause.getStackTrace();
            int iZza = zza(stackTrace2, stackTrace);
            for (int i = 0; i < stackTrace2.length - iZza; i++) {
                sb.append("\n\tat " + stackTrace2[i]);
            }
            if (iZza > 0) {
                sb.append("\n\t... " + iZza + " more");
            }
            cause = cause.getCause();
            stackTrace = stackTrace2;
        }
        return sb.toString();
    }
}
