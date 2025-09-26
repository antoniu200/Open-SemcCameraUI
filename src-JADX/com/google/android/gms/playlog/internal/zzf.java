package com.google.android.gms.playlog.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzse;
import com.google.android.gms.playlog.internal.zza;
import com.google.android.gms.playlog.internal.zzb;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzf extends zzj<zza> {
    private final String zzQe;
    private final zzd zzaRZ;
    private final zzb zzaSa;
    private boolean zzaSb;
    private final Object zzpd;

    public zzf(Context context, Looper looper, zzd zzdVar, com.google.android.gms.common.internal.zzf zzfVar) {
        super(context, looper, 24, zzfVar, zzdVar, zzdVar);
        this.zzQe = context.getPackageName();
        this.zzaRZ = (zzd) zzx.zzw(zzdVar);
        this.zzaRZ.zza(this);
        this.zzaSa = new zzb();
        this.zzpd = new Object();
        this.zzaSb = true;
    }

    private void zzBv() {
        com.google.android.gms.common.internal.zzb.zzZ(!this.zzaSb);
        if (this.zzaSa.isEmpty()) {
            return;
        }
        PlayLoggerContext playLoggerContext = null;
        try {
            ArrayList arrayList = new ArrayList();
            Iterator<zzb.zza> it = this.zzaSa.zzBt().iterator();
            while (it.hasNext()) {
                zzb.zza next = it.next();
                if (next.zzaRO != null) {
                    zzpc().zza(this.zzQe, next.zzaRM, zzse.zzf(next.zzaRO));
                } else {
                    if (!next.zzaRM.equals(playLoggerContext)) {
                        if (!arrayList.isEmpty()) {
                            zzpc().zza(this.zzQe, playLoggerContext, arrayList);
                            arrayList.clear();
                        }
                        playLoggerContext = next.zzaRM;
                    }
                    arrayList.add(next.zzaRN);
                }
            }
            if (!arrayList.isEmpty()) {
                zzpc().zza(this.zzQe, playLoggerContext, arrayList);
            }
            this.zzaSa.clear();
        } catch (RemoteException unused) {
            Log.e("PlayLoggerImpl", "Couldn't send cached log events to AndroidLog service.  Retaining in memory cache.");
        }
    }

    private void zzc(PlayLoggerContext playLoggerContext, LogEvent logEvent) {
        this.zzaSa.zza(playLoggerContext, logEvent);
    }

    private void zzd(PlayLoggerContext playLoggerContext, LogEvent logEvent) {
        String str;
        String str2;
        try {
            zzBv();
            zzpc().zza(this.zzQe, playLoggerContext, logEvent);
        } catch (RemoteException unused) {
            str = "PlayLoggerImpl";
            str2 = "Couldn't send log event.  Will try caching.";
            Log.e(str, str2);
            zzc(playLoggerContext, logEvent);
        } catch (IllegalStateException unused2) {
            str = "PlayLoggerImpl";
            str2 = "Service was disconnected.  Will try caching.";
            Log.e(str, str2);
            zzc(playLoggerContext, logEvent);
        }
    }

    public void start() {
        synchronized (this.zzpd) {
            if (!isConnecting() && !isConnected()) {
                this.zzaRZ.zzao(true);
                zzoZ();
            }
        }
    }

    public void stop() {
        synchronized (this.zzpd) {
            this.zzaRZ.zzao(false);
            disconnect();
        }
    }

    void zzap(boolean z) {
        synchronized (this.zzpd) {
            boolean z2 = this.zzaSb;
            this.zzaSb = z;
            if (z2 && !this.zzaSb) {
                zzBv();
            }
        }
    }

    public void zzb(PlayLoggerContext playLoggerContext, LogEvent logEvent) {
        synchronized (this.zzpd) {
            if (this.zzaSb) {
                zzc(playLoggerContext, logEvent);
            } else {
                zzd(playLoggerContext, logEvent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.zzj
    /* renamed from: zzdA, reason: merged with bridge method [inline-methods] */
    public zza zzW(IBinder iBinder) {
        return zza.AbstractBinderC0031zza.zzdz(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzfK() {
        return "com.google.android.gms.playlog.service.START";
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzfL() {
        return "com.google.android.gms.playlog.internal.IPlayLogService";
    }
}
