// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.converter;

import java.util.ArrayList;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<StringToIntConverter>
{
    static void zza(final StringToIntConverter stringToIntConverter, final Parcel parcel, int zzaq) {
        zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, stringToIntConverter.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, stringToIntConverter.zzpA(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }
    
    public StringToIntConverter zzat(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        ArrayList<StringToIntConverter.Entry> zzc = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 2: {
                    zzc = zza.zzc(parcel, zzao, (android.os.Parcelable$Creator<StringToIntConverter.Entry>)StringToIntConverter.Entry.CREATOR);
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
        return new StringToIntConverter(zzg, zzc);
    }
    
    public StringToIntConverter[] zzbR(final int n) {
        return new StringToIntConverter[n];
    }
}
