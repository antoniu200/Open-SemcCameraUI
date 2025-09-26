// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.stats;

import java.io.Serializable;
import java.util.List;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzh implements Parcelable$Creator<WakeLockEvent>
{
    static void zza(final WakeLockEvent wakeLockEvent, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, wakeLockEvent.mVersionCode);
        zzb.zza(parcel, 2, wakeLockEvent.getTimeMillis());
        zzb.zza(parcel, 4, wakeLockEvent.zzqj(), false);
        zzb.zzc(parcel, 5, wakeLockEvent.zzql());
        zzb.zzb(parcel, 6, wakeLockEvent.zzqm(), false);
        zzb.zza(parcel, 8, wakeLockEvent.zzqf());
        zzb.zza(parcel, 10, wakeLockEvent.zzqk(), false);
        zzb.zzc(parcel, 11, wakeLockEvent.getEventType());
        zzb.zza(parcel, 12, wakeLockEvent.zzqc(), false);
        zzb.zza(parcel, 13, wakeLockEvent.zzqo(), false);
        zzb.zzc(parcel, 14, wakeLockEvent.zzqn());
        zzb.zza(parcel, 15, wakeLockEvent.zzqp());
        zzb.zza(parcel, 16, wakeLockEvent.zzqq());
        zzb.zzI(parcel, zzaq);
    }
    
    public WakeLockEvent zzaB(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        long zzi = 0L;
        long zzi3;
        long zzi2 = zzi3 = 0L;
        int zzg = 0;
        int zzg2 = 0;
        int zzg4;
        int zzg3 = zzg4 = zzg2;
        String zzp = null;
        String zzp2;
        Serializable zzD = zzp2 = null;
        String zzp4;
        String zzp3 = zzp4 = zzp2;
        float zzl = 0.0f;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 16: {
                    zzi3 = zza.zzi(parcel, zzao);
                    continue;
                }
                case 15: {
                    zzl = zza.zzl(parcel, zzao);
                    continue;
                }
                case 14: {
                    zzg4 = zza.zzg(parcel, zzao);
                    continue;
                }
                case 13: {
                    zzp4 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 12: {
                    zzp2 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 11: {
                    zzg2 = zza.zzg(parcel, zzao);
                    continue;
                }
                case 10: {
                    zzp3 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 8: {
                    zzi2 = zza.zzi(parcel, zzao);
                    continue;
                }
                case 6: {
                    zzD = zza.zzD(parcel, zzao);
                    continue;
                }
                case 5: {
                    zzg3 = zza.zzg(parcel, zzao);
                    continue;
                }
                case 4: {
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
        return new WakeLockEvent(zzg, zzi, zzg2, zzp, zzg3, (List<String>)zzD, zzp2, zzi2, zzg4, zzp3, zzp4, zzl, zzi3);
    }
    
    public WakeLockEvent[] zzbZ(final int n) {
        return new WakeLockEvent[n];
    }
}
