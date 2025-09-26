package com.google.android.gms.internal;

import android.content.Context;
import java.util.regex.Pattern;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzml {
    private static Pattern zzaij;

    public static boolean zzan(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
    }

    public static int zzca(int i) {
        return i / 1000;
    }

    @Deprecated
    public static boolean zzcb(int i) {
        return false;
    }
}
