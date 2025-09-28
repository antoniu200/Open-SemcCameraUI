package com.google.android.gms.common.stats;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Debug;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.stats.zzc;
import com.google.android.gms.internal.zzmm;
import com.google.android.gms.internal.zzmy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzb {
    private static final Object zzafW = new Object();
    private static Integer zzahE;
    private static zzb zzahy;
    private final List<String> zzahA;
    private final List<String> zzahB;
    private final List<String> zzahC;
    private zze zzahD;
    private zze zzahF;
    private final List<String> zzahz;

    private zzb() {
        if (getLogLevel() == zzd.LOG_LEVEL_OFF) {
            this.zzahz = Collections.EMPTY_LIST;
            this.zzahA = Collections.EMPTY_LIST;
            this.zzahB = Collections.EMPTY_LIST;
            this.zzahC = Collections.EMPTY_LIST;
            return;
        }
        String str = zzc.zza.zzahI.get();
        this.zzahz = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        String str2 = zzc.zza.zzahJ.get();
        this.zzahA = str2 == null ? Collections.EMPTY_LIST : Arrays.asList(str2.split(","));
        String str3 = zzc.zza.zzahK.get();
        this.zzahB = str3 == null ? Collections.EMPTY_LIST : Arrays.asList(str3.split(","));
        String str4 = zzc.zza.zzahL.get();
        this.zzahC = str4 == null ? Collections.EMPTY_LIST : Arrays.asList(str4.split(","));
        this.zzahD = new zze(1024, zzc.zza.zzahM.get().longValue());
        this.zzahF = new zze(1024, zzc.zza.zzahM.get().longValue());
    }

    private static int getLogLevel() {
        if (zzahE == null) {
            try {
                zzahE = Integer.valueOf(zzmm.zzjA() ? zzc.zza.zzahH.get().intValue() : zzd.LOG_LEVEL_OFF);
            } catch (SecurityException unused) {
                zzahE = Integer.valueOf(zzd.LOG_LEVEL_OFF);
            }
        }
        return zzahE.intValue();
    }

    private void zza(Context context, String str, int i, String str2, String str3, String str4, String str5) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        String strZzl = ((getLogLevel() & zzd.zzahR) == 0 || i == 13) ? null : zzmy.zzl(3, 5);
        long nativeHeapAllocatedSize = (getLogLevel() & zzd.zzahT) != 0 ? Debug.getNativeHeapAllocatedSize() : 0L;
        context.startService(new Intent().setComponent(zzd.zzahN).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", (i == 1 || i == 4 || i == 14) ? new ConnectionEvent(jCurrentTimeMillis, i, null, null, null, null, strZzl, str, SystemClock.elapsedRealtime(), nativeHeapAllocatedSize) : new ConnectionEvent(jCurrentTimeMillis, i, str2, str3, str4, str5, strZzl, str, SystemClock.elapsedRealtime(), nativeHeapAllocatedSize)));
    }

    private void zza(Context context, String str, String str2, Intent intent, int i) {
        String str3;
        String str4;
        String str5;
        if (!zzqi() || this.zzahD == null) {
            return;
        }
        if (i != 4 && i != 1) {
            ServiceInfo serviceInfoZzd = zzd(context, intent);
            if (serviceInfoZzd == null) {
                Log.w("ConnectionTracker", String.format("Client %s made an invalid request %s", str2, intent.toUri(0)));
                return;
            }
            String str6 = serviceInfoZzd.processName;
            String str7 = serviceInfoZzd.name;
            String strZzaq = zzmy.zzaq(context);
            if (!zzb(strZzaq, str2, str6, str7)) {
                return;
            }
            this.zzahD.zzcx(str);
            str4 = str6;
            str3 = strZzaq;
            str5 = str7;
        } else {
            if (!this.zzahD.zzcy(str)) {
                return;
            }
            str3 = null;
            str4 = null;
            str5 = null;
        }
        zza(context, str, i, str3, str2, str4, str5);
    }

    private String zzb(ServiceConnection serviceConnection) {
        return String.valueOf(System.identityHashCode(serviceConnection) | (Process.myPid() << 32));
    }

    private boolean zzb(String str, String str2, String str3, String str4) {
        int logLevel = getLogLevel();
        if (this.zzahz.contains(str) || this.zzahA.contains(str2) || this.zzahB.contains(str3) || this.zzahC.contains(str4)) {
            return false;
        }
        return !str3.equals(str) || (zzd.zzahS & logLevel) == 0;
    }

    private boolean zzc(Context context, Intent intent) {
        ComponentName component = intent.getComponent();
        if (component == null) {
            return false;
        }
        if (com.google.android.gms.common.internal.zzd.zzaeK && "com.google.android.gms".equals(component.getPackageName())) {
            return false;
        }
        return zzmm.zzl(context, component.getPackageName());
    }

    private static ServiceInfo zzd(Context context, Intent intent) {
        List<ResolveInfo> listQueryIntentServices = context.getPackageManager().queryIntentServices(intent, 128);
        if (listQueryIntentServices == null || listQueryIntentServices.size() == 0) {
            Log.w("ConnectionTracker", String.format("There are no handler of this intent: %s\n Stack trace: %s", intent.toUri(0), zzmy.zzl(3, 20)));
            return null;
        }
        if (listQueryIntentServices.size() > 1) {
            Log.w("ConnectionTracker", String.format("Multiple handlers found for this intent: %s\n Stack trace: %s", intent.toUri(0), zzmy.zzl(3, 20)));
            Iterator<ResolveInfo> it = listQueryIntentServices.iterator();
            if (it.hasNext()) {
                Log.w("ConnectionTracker", it.next().serviceInfo.name);
                return null;
            }
        }
        return listQueryIntentServices.get(0).serviceInfo;
    }

    public static zzb zzqh() {
        synchronized (zzafW) {
            if (zzahy == null) {
                zzahy = new zzb();
            }
        }
        return zzahy;
    }

    private boolean zzqi() {
        return com.google.android.gms.common.internal.zzd.zzaeK && getLogLevel() != zzd.LOG_LEVEL_OFF;
    }

    public void zza(Context context, ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
        zza(context, zzb(serviceConnection), (String) null, (Intent) null, 1);
    }

    public void zza(Context context, ServiceConnection serviceConnection, String str, Intent intent) {
        zza(context, zzb(serviceConnection), str, intent, 3);
    }

    public boolean zza(Context context, Intent intent, ServiceConnection serviceConnection, int i) {
        return zza(context, context.getClass().getName(), intent, serviceConnection, i);
    }

    public boolean zza(Context context, String str, Intent intent, ServiceConnection serviceConnection, int i) {
        if (zzc(context, intent)) {
            Log.w("ConnectionTracker", "Attempted to bind to a service in a STOPPED package.");
            return false;
        }
        boolean zBindService = context.bindService(intent, serviceConnection, i);
        if (zBindService) {
            zza(context, zzb(serviceConnection), str, intent, 2);
        }
        return zBindService;
    }

    public void zzb(Context context, ServiceConnection serviceConnection) {
        zza(context, zzb(serviceConnection), (String) null, (Intent) null, 4);
    }
}
