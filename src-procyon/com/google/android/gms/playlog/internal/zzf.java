// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.playlog.internal;

import android.os.IInterface;
import android.os.IBinder;
import java.util.Iterator;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;
import com.google.android.gms.internal.zzse;
import java.util.ArrayList;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.internal.zzj;

public class zzf extends zzj<com.google.android.gms.playlog.internal.zza>
{
    private final String zzQe;
    private final com.google.android.gms.playlog.internal.zzd zzaRZ;
    private final com.google.android.gms.playlog.internal.zzb zzaSa;
    private boolean zzaSb;
    private final Object zzpd;
    
    public zzf(final Context context, final Looper looper, final com.google.android.gms.playlog.internal.zzd zzd, final com.google.android.gms.common.internal.zzf zzf) {
        super(context, looper, 24, zzf, zzd, zzd);
        this.zzQe = context.getPackageName();
        (this.zzaRZ = zzx.zzw(zzd)).zza(this);
        this.zzaSa = new com.google.android.gms.playlog.internal.zzb();
        this.zzpd = new Object();
        this.zzaSb = true;
    }
    
    private void zzBv() {
        com.google.android.gms.common.internal.zzb.zzZ(this.zzaSb ^ true);
        if (!this.zzaSa.isEmpty()) {
            PlayLoggerContext zzaRM = null;
            try {
                final ArrayList<LogEvent> list = new ArrayList<LogEvent>();
                for (final com.google.android.gms.playlog.internal.zzb.zza zza : this.zzaSa.zzBt()) {
                    if (zza.zzaRO != null) {
                        this.zzpc().zza(this.zzQe, zza.zzaRM, zzse.zzf(zza.zzaRO));
                    }
                    else {
                        if (!zza.zzaRM.equals(zzaRM)) {
                            if (!list.isEmpty()) {
                                this.zzpc().zza(this.zzQe, zzaRM, list);
                                list.clear();
                            }
                            zzaRM = zza.zzaRM;
                        }
                        list.add(zza.zzaRN);
                    }
                }
                if (!list.isEmpty()) {
                    this.zzpc().zza(this.zzQe, zzaRM, list);
                }
                this.zzaSa.clear();
            }
            catch (final RemoteException ex) {
                Log.e("PlayLoggerImpl", "Couldn't send cached log events to AndroidLog service.  Retaining in memory cache.");
            }
        }
    }
    
    private void zzc(final PlayLoggerContext playLoggerContext, final LogEvent logEvent) {
        this.zzaSa.zza(playLoggerContext, logEvent);
    }
    
    private void zzd(final PlayLoggerContext playLoggerContext, final LogEvent logEvent) {
        String s;
        try {
            this.zzBv();
            this.zzpc().zza(this.zzQe, playLoggerContext, logEvent);
            return;
        }
        catch (final IllegalStateException ex) {
            s = "Service was disconnected.  Will try caching.";
        }
        catch (final RemoteException ex2) {
            s = "Couldn't send log event.  Will try caching.";
        }
        Log.e("PlayLoggerImpl", s);
        this.zzc(playLoggerContext, logEvent);
    }
    
    public void start() {
        synchronized (this.zzpd) {
            if (!this.isConnecting() && !this.isConnected()) {
                this.zzaRZ.zzao(true);
                this.zzoZ();
            }
        }
    }
    
    public void stop() {
        synchronized (this.zzpd) {
            this.zzaRZ.zzao(false);
            this.disconnect();
        }
    }
    
    void zzap(final boolean zzaSb) {
        synchronized (this.zzpd) {
            final boolean zzaSb2 = this.zzaSb;
            this.zzaSb = zzaSb;
            if (zzaSb2 && !this.zzaSb) {
                this.zzBv();
            }
        }
    }
    
    public void zzb(final PlayLoggerContext playLoggerContext, final LogEvent logEvent) {
        synchronized (this.zzpd) {
            if (this.zzaSb) {
                this.zzc(playLoggerContext, logEvent);
            }
            else {
                this.zzd(playLoggerContext, logEvent);
            }
        }
    }
    
    protected com.google.android.gms.playlog.internal.zza zzdA(final IBinder binder) {
        return com.google.android.gms.playlog.internal.zza.zza.zzdz(binder);
    }
    
    @Override
    protected String zzfK() {
        return "com.google.android.gms.playlog.service.START";
    }
    
    @Override
    protected String zzfL() {
        return "com.google.android.gms.playlog.internal.IPlayLogService";
    }
}
