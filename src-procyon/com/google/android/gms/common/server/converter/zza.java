// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.converter;

import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<ConverterWrapper>
{
    static void zza(final ConverterWrapper converterWrapper, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, converterWrapper.getVersionCode());
        zzb.zza(parcel, 2, (Parcelable)converterWrapper.zzpy(), n, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public ConverterWrapper zzas(final Parcel parcel) {
        final int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int zzg = 0;
        StringToIntConverter stringToIntConverter = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    continue;
                }
                case 2: {
                    stringToIntConverter = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, (android.os.Parcelable$Creator<StringToIntConverter>)StringToIntConverter.CREATOR);
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
        return new ConverterWrapper(zzg, stringToIntConverter);
    }
    
    public ConverterWrapper[] zzbQ(final int n) {
        return new ConverterWrapper[n];
    }
}
