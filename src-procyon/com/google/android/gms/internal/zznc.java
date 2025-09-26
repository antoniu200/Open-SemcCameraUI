// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Context;
import android.util.Log;
import android.os.WorkSource;
import java.lang.reflect.Method;

public class zznc
{
    private static final Method zzaip;
    private static final Method zzaiq;
    private static final Method zzair;
    private static final Method zzais;
    private static final Method zzait;
    
    static {
        zzaip = zzqG();
        zzaiq = zzqH();
        zzair = zzqI();
        zzais = zzqJ();
        zzait = zzqK();
    }
    
    public static int zza(final WorkSource obj) {
        if (zznc.zzair != null) {
            try {
                return (int)zznc.zzair.invoke(obj, new Object[0]);
            }
            catch (final Exception ex) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", (Throwable)ex);
            }
        }
        return 0;
    }
    
    public static String zza(final WorkSource obj, final int i) {
        if (zznc.zzait != null) {
            try {
                return (String)zznc.zzait.invoke(obj, i);
            }
            catch (final Exception ex) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", (Throwable)ex);
            }
        }
        return null;
    }
    
    public static void zza(final WorkSource workSource, final int n, final String s) {
        if (zznc.zzaiq != null) {
            String s2;
            if ((s2 = s) == null) {
                s2 = "";
            }
            try {
                zznc.zzaiq.invoke(workSource, n, s2);
                return;
            }
            catch (final Exception ex) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", (Throwable)ex);
                return;
            }
        }
        if (zznc.zzaip != null) {
            try {
                zznc.zzaip.invoke(workSource, n);
            }
            catch (final Exception ex2) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", (Throwable)ex2);
            }
        }
    }
    
    public static boolean zzar(final Context context) {
        return context.getPackageManager().checkPermission("android.permission.UPDATE_DEVICE_STATS", context.getPackageName()) == 0;
    }
    
    public static List<String> zzb(final WorkSource workSource) {
        int i = 0;
        int zza;
        if (workSource == null) {
            zza = 0;
        }
        else {
            zza = zza(workSource);
        }
        if (zza == 0) {
            return Collections.EMPTY_LIST;
        }
        final ArrayList list = new ArrayList();
        while (i < zza) {
            final String zza2 = zza(workSource, i);
            if (!zznb.zzcA(zza2)) {
                list.add(zza2);
            }
            ++i;
        }
        return list;
    }
    
    public static WorkSource zzf(final int n, final String s) {
        final WorkSource workSource = new WorkSource();
        zza(workSource, n, s);
        return workSource;
    }
    
    public static WorkSource zzm(final Context context, final String str) {
        if (context != null) {
            if (context.getPackageManager() == null) {
                return null;
            }
            while (true) {
                try {
                    final ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 0);
                    if (applicationInfo == null) {
                        final StringBuilder sb = new StringBuilder();
                        final String str2 = "Could not get applicationInfo from package: ";
                        sb.append(str2);
                        sb.append(str);
                        Log.e("WorkSourceUtil", sb.toString());
                        return null;
                    }
                    return zzf(applicationInfo.uid, str);
                }
                catch (final PackageManager$NameNotFoundException ex) {
                    final StringBuilder sb = new StringBuilder();
                    final String str2 = "Could not find package: ";
                    continue;
                }
                break;
            }
        }
        return null;
    }
    
    private static Method zzqG() {
        try {
            return WorkSource.class.getMethod("add", Integer.TYPE);
        }
        catch (final Exception ex) {
            return null;
        }
    }
    
    private static Method zzqH() {
        Label_0031: {
            if (!zzmx.zzqA()) {
                break Label_0031;
            }
            try {
                return WorkSource.class.getMethod("add", Integer.TYPE, String.class);
                return null;
            }
            catch (final Exception ex) {
                return null;
            }
        }
    }
    
    private static Method zzqI() {
        try {
            return WorkSource.class.getMethod("size", (Class<?>[])new Class[0]);
        }
        catch (final Exception ex) {
            return null;
        }
    }
    
    private static Method zzqJ() {
        try {
            return WorkSource.class.getMethod("get", Integer.TYPE);
        }
        catch (final Exception ex) {
            return null;
        }
    }
    
    private static Method zzqK() {
        Label_0026: {
            if (!zzmx.zzqA()) {
                break Label_0026;
            }
            try {
                return WorkSource.class.getMethod("getName", Integer.TYPE);
                return null;
            }
            catch (final Exception ex) {
                return null;
            }
        }
    }
}
