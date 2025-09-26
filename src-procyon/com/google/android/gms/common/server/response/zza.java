// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.converter.ConverterWrapper;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<FastJsonResponse.Field>
{
    static void zza(final FastJsonResponse.Field field, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, field.getVersionCode());
        zzb.zzc(parcel, 2, field.zzpB());
        zzb.zza(parcel, 3, field.zzpH());
        zzb.zzc(parcel, 4, field.zzpC());
        zzb.zza(parcel, 5, field.zzpI());
        zzb.zza(parcel, 6, field.zzpJ(), false);
        zzb.zzc(parcel, 7, field.zzpK());
        zzb.zza(parcel, 8, field.zzpM(), false);
        zzb.zza(parcel, 9, (Parcelable)field.zzpO(), n, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public FastJsonResponse.Field zzav(final Parcel parcel) {
        final int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String zzp = null;
        ConverterWrapper converterWrapper;
        Object zzp2 = converterWrapper = null;
        int zzg = 0;
        int zzg2 = 0;
        int zzg3;
        int zzc = zzg3 = zzg2;
        int zzg4;
        int zzc2 = zzg4 = zzg3;
        while (parcel.dataPosition() < zzap) {
            final int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    continue;
                }
                case 9: {
                    converterWrapper = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, (android.os.Parcelable$Creator<ConverterWrapper>)ConverterWrapper.CREATOR);
                    continue;
                }
                case 8: {
                    zzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    continue;
                }
                case 7: {
                    zzg4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    continue;
                }
                case 6: {
                    zzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    continue;
                }
                case 5: {
                    zzc2 = (com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 4: {
                    zzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzc = (com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 2: {
                    zzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    continue;
                }
                case 1: {
                    zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza(sb.toString(), parcel);
        }
        return new FastJsonResponse.Field(zzg, zzg2, (boolean)(zzc != 0), zzg3, (boolean)(zzc2 != 0), zzp, zzg4, (String)zzp2, converterWrapper);
    }
    
    public FastJsonResponse.Field[] zzbT(final int n) {
        return new FastJsonResponse.Field[n];
    }
}
