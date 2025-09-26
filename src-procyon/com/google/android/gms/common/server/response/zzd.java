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

public class zzd implements Parcelable$Creator<FieldMappingDictionary.Entry>
{
    static void zza(final FieldMappingDictionary.Entry entry, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, entry.versionCode);
        zzb.zza(parcel, 2, entry.className, false);
        zzb.zzc(parcel, 3, entry.zzahh, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public FieldMappingDictionary.Entry zzay(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        String zzp = null;
        int zzg = 0;
        ArrayList<FieldMappingDictionary.FieldMapPair> zzc = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzc = zza.zzc(parcel, zzao, (android.os.Parcelable$Creator<FieldMappingDictionary.FieldMapPair>)FieldMappingDictionary.FieldMapPair.CREATOR);
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
        return new FieldMappingDictionary.Entry(zzg, zzp, zzc);
    }
    
    public FieldMappingDictionary.Entry[] zzbW(final int n) {
        return new FieldMappingDictionary.Entry[n];
    }
}
