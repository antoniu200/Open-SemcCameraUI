// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.converter;

import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<StringToIntConverter.Entry>
{
    static void zza(final StringToIntConverter.Entry entry, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, entry.versionCode);
        zzb.zza(parcel, 2, entry.zzagS, false);
        zzb.zzc(parcel, 3, entry.zzagT);
        zzb.zzI(parcel, zzaq);
    }
    
    public StringToIntConverter.Entry zzau(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        String zzp = null;
        int zzg2 = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
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
        return new StringToIntConverter.Entry(zzg, zzp, zzg2);
    }
    
    public StringToIntConverter.Entry[] zzbS(final int n) {
        return new StringToIntConverter.Entry[n];
    }
}
