// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.playlog.internal;

import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zze implements Parcelable$Creator<PlayLoggerContext>
{
    static void zza(final PlayLoggerContext playLoggerContext, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, playLoggerContext.versionCode);
        zzb.zza(parcel, 2, playLoggerContext.packageName, false);
        zzb.zzc(parcel, 3, playLoggerContext.zzaRR);
        zzb.zzc(parcel, 4, playLoggerContext.zzaRS);
        zzb.zza(parcel, 5, playLoggerContext.zzaRT, false);
        zzb.zza(parcel, 6, playLoggerContext.zzaRU, false);
        zzb.zza(parcel, 7, playLoggerContext.zzaRV);
        zzb.zza(parcel, 8, playLoggerContext.zzaRW, false);
        zzb.zza(parcel, 9, playLoggerContext.zzaRX);
        zzb.zzc(parcel, 10, playLoggerContext.zzaRY);
        zzb.zzI(parcel, zzaq);
    }
    
    public PlayLoggerContext zzgj(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        String zzp = null;
        String zzp2 = null;
        String zzp4;
        String zzp3 = zzp4 = zzp2;
        int zzg = 0;
        int zzg3;
        int zzg2 = zzg3 = 0;
        int zzg4;
        int zzc = zzg4 = zzg3;
        boolean zzc2 = true;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 10: {
                    zzg4 = zza.zzg(parcel, zzao);
                    continue;
                }
                case 9: {
                    zzc = (zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 8: {
                    zzp4 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 7: {
                    zzc2 = zza.zzc(parcel, zzao);
                    continue;
                }
                case 6: {
                    zzp3 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 5: {
                    zzp2 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 4: {
                    zzg3 = zza.zzg(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzg2 = zza.zzg(parcel, zzao);
                    continue;
                }
                case 2: {
                    zzp = zza.zzp(parcel, zzao);
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
        return new PlayLoggerContext(zzg, zzp, zzg2, zzg3, zzp2, zzp3, zzc2, zzp4, (boolean)(zzc != 0), zzg4);
    }
    
    public PlayLoggerContext[] zziV(final int n) {
        return new PlayLoggerContext[n];
    }
}
