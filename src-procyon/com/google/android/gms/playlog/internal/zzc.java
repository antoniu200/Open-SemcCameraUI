// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.playlog.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<LogEvent>
{
    static void zza(final LogEvent logEvent, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, logEvent.versionCode);
        zzb.zza(parcel, 2, logEvent.zzaRG);
        zzb.zza(parcel, 3, logEvent.tag, false);
        zzb.zza(parcel, 4, logEvent.zzaRI, false);
        zzb.zza(parcel, 5, logEvent.zzaRJ, false);
        zzb.zza(parcel, 6, logEvent.zzaRH);
        zzb.zzI(parcel, zzaq);
    }
    
    public LogEvent zzgi(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        long zzi = 0L;
        long zzi2 = 0L;
        String zzp = null;
        Bundle zzr;
        Object zzs = zzr = null;
        int zzg = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 6: {
                    zzi2 = zza.zzi(parcel, zzao);
                    continue;
                }
                case 5: {
                    zzr = zza.zzr(parcel, zzao);
                    continue;
                }
                case 4: {
                    zzs = zza.zzs(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzp = zza.zzp(parcel, zzao);
                    continue;
                }
                case 2: {
                    zzi = zza.zzi(parcel, zzao);
                    continue;
                }
                case 1: {
                    zzg = zza.zzg(parcel, zzao);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new zza.zza(sb.toString(), parcel);
        }
        return new LogEvent(zzg, zzi, zzi2, zzp, (byte[])zzs, zzr);
    }
    
    public LogEvent[] zziU(final int n) {
        return new LogEvent[n];
    }
}
