package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.WorkSource;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zznc {
    private static final Method zzaip = zzqG();
    private static final Method zzaiq = zzqH();
    private static final Method zzair = zzqI();
    private static final Method zzais = zzqJ();
    private static final Method zzait = zzqK();

    public static int zza(WorkSource workSource) {
        if (zzair != null) {
            try {
                return ((Integer) zzair.invoke(workSource, new Object[0])).intValue();
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
            }
        }
        return 0;
    }

    public static String zza(WorkSource workSource, int i) {
        if (zzait == null) {
            return null;
        }
        try {
            return (String) zzait.invoke(workSource, Integer.valueOf(i));
        } catch (Exception e) {
            Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
            return null;
        }
    }

    public static void zza(WorkSource workSource, int i, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (zzaiq != null) {
            if (str == null) {
                str = "";
            }
            try {
                zzaiq.invoke(workSource, Integer.valueOf(i), str);
                return;
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
                return;
            }
        }
        if (zzaip != null) {
            try {
                zzaip.invoke(workSource, Integer.valueOf(i));
            } catch (Exception e2) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e2);
            }
        }
    }

    public static boolean zzar(Context context) {
        return context.getPackageManager().checkPermission("android.permission.UPDATE_DEVICE_STATS", context.getPackageName()) == 0;
    }

    public static List<String> zzb(WorkSource workSource) {
        int iZza = workSource == null ? 0 : zza(workSource);
        if (iZza == 0) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < iZza; i++) {
            String strZza = zza(workSource, i);
            if (!zznb.zzcA(strZza)) {
                arrayList.add(strZza);
            }
        }
        return arrayList;
    }

    public static WorkSource zzf(int i, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        WorkSource workSource = new WorkSource();
        zza(workSource, i, str);
        return workSource;
    }

    public static WorkSource zzm(Context context, String str) throws PackageManager.NameNotFoundException {
        String str2;
        StringBuilder sb;
        String str3;
        ApplicationInfo applicationInfo;
        if (context == null || context.getPackageManager() == null) {
            return null;
        }
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            str2 = "WorkSourceUtil";
            sb = new StringBuilder();
            str3 = "Could not find package: ";
        }
        if (applicationInfo != null) {
            return zzf(applicationInfo.uid, str);
        }
        str2 = "WorkSourceUtil";
        sb = new StringBuilder();
        str3 = "Could not get applicationInfo from package: ";
        sb.append(str3);
        sb.append(str);
        Log.e(str2, sb.toString());
        return null;
    }

    private static Method zzqG() {
        try {
            return WorkSource.class.getMethod("add", Integer.TYPE);
        } catch (Exception unused) {
            return null;
        }
    }

    private static Method zzqH() {
        if (!zzmx.zzqA()) {
            return null;
        }
        try {
            return WorkSource.class.getMethod("add", Integer.TYPE, String.class);
        } catch (Exception unused) {
            return null;
        }
    }

    private static Method zzqI() {
        try {
            return WorkSource.class.getMethod("size", new Class[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    private static Method zzqJ() {
        try {
            return WorkSource.class.getMethod("get", Integer.TYPE);
        } catch (Exception unused) {
            return null;
        }
    }

    private static Method zzqK() {
        if (!zzmx.zzqA()) {
            return null;
        }
        try {
            return WorkSource.class.getMethod("getName", Integer.TYPE);
        } catch (Exception unused) {
            return null;
        }
    }
}
