package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zze;
import java.util.regex.Pattern;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zznb {
    private static final Pattern zzaio = Pattern.compile("\\$\\{(.*?)\\}");

    public static boolean zzcA(String str) {
        return str == null || zze.zzaeL.zzb(str);
    }
}
