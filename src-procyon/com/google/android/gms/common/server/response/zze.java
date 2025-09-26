// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.response;

import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zze implements Parcelable$Creator<SafeParcelResponse>
{
    static void zza(final SafeParcelResponse safeParcelResponse, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, safeParcelResponse.getVersionCode());
        zzb.zza(parcel, 2, safeParcelResponse.zzpV(), false);
        zzb.zza(parcel, 3, (Parcelable)safeParcelResponse.zzpW(), n, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public SafeParcelResponse zzaz(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        Parcel zzE = null;
        int zzg = 0;
        FieldMappingDictionary fieldMappingDictionary = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 3: {
                    fieldMappingDictionary = zza.zza(parcel, zzao, (android.os.Parcelable$Creator<FieldMappingDictionary>)FieldMappingDictionary.CREATOR);
                    continue;
                }
                case 2: {
                    zzE = zza.zzE(parcel, zzao);
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
        return new SafeParcelResponse(zzg, zzE, fieldMappingDictionary);
    }
    
    public SafeParcelResponse[] zzbX(final int n) {
        return new SafeParcelResponse[n];
    }
}
