// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.response;

import java.util.ArrayList;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.List;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<FieldMappingDictionary>
{
    static void zza(final FieldMappingDictionary fieldMappingDictionary, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, fieldMappingDictionary.getVersionCode());
        zzb.zzc(parcel, 2, fieldMappingDictionary.zzpS(), false);
        zzb.zza(parcel, 3, fieldMappingDictionary.zzpT(), false);
        zzb.zzI(parcel, zzaq);
    }
    
    public FieldMappingDictionary zzax(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        ArrayList<FieldMappingDictionary.Entry> zzc = null;
        int zzg = 0;
        String zzp = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzp = zza.zzp(parcel, zzao);
                    continue;
                }
                case 2: {
                    zzc = zza.zzc(parcel, zzao, (android.os.Parcelable$Creator<FieldMappingDictionary.Entry>)FieldMappingDictionary.Entry.CREATOR);
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
        return new FieldMappingDictionary(zzg, zzc, zzp);
    }
    
    public FieldMappingDictionary[] zzbV(final int n) {
        return new FieldMappingDictionary[n];
    }
}
