// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.response;

import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<FieldMappingDictionary.FieldMapPair>
{
    static void zza(final FieldMappingDictionary.FieldMapPair fieldMapPair, final Parcel parcel, final int n) {
        final int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, fieldMapPair.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, fieldMapPair.key, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable)fieldMapPair.zzahi, n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }
    
    public FieldMappingDictionary.FieldMapPair zzaw(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        String zzp = null;
        int zzg = 0;
        FastJsonResponse.Field<?, ?> field = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 3: {
                    field = (FastJsonResponse.Field<?, ?>)zza.zza(parcel, zzao, (android.os.Parcelable$Creator<FastJsonResponse.Field>)FastJsonResponse.Field.CREATOR);
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
        return new FieldMappingDictionary.FieldMapPair(zzg, zzp, field);
    }
    
    public FieldMappingDictionary.FieldMapPair[] zzbU(final int n) {
        return new FieldMappingDictionary.FieldMapPair[n];
    }
}
