package com.google.android.gms.common.stats;

import android.os.PowerManager;
import android.os.Process;
import android.text.TextUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzg {
    public static String zza(PowerManager.WakeLock wakeLock, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf((Process.myPid() << 32) | System.identityHashCode(wakeLock)));
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        sb.append(str);
        return sb.toString();
    }
}
