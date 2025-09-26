package android.support.v4.os;

import android.os.Build;
import android.os.Trace;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class TraceCompat {
    public static void beginSection(String str) {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.beginSection(str);
        }
    }

    public static void endSection() {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.endSection();
        }
    }

    private TraceCompat() {
    }
}
