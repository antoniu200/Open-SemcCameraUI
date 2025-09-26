// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import android.os.Bundle;
import android.database.CursorWindow;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zze implements Parcelable$Creator<DataHolder>
{
    static void zza(final DataHolder dataHolder, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, dataHolder.zzow(), false);
        zzb.zzc(parcel, 1000, dataHolder.getVersionCode());
        zzb.zza(parcel, 2, dataHolder.zzox(), n, false);
        zzb.zzc(parcel, 3, dataHolder.getStatusCode());
        zzb.zza(parcel, 4, dataHolder.zzor(), false);
        zzb.zzI(parcel, zzaq);
    }
    
    public DataHolder zzag(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        int zzg2 = 0;
        String[] zzB = null;
        Bundle zzr;
        Object o = zzr = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            final int zzbM = zza.zzbM(zzao);
            if (zzbM != 1000) {
                switch (zzbM) {
                    default: {
                        zza.zzb(parcel, zzao);
                        continue;
                    }
                    case 4: {
                        zzr = zza.zzr(parcel, zzao);
                        continue;
                    }
                    case 3: {
                        zzg2 = zza.zzg(parcel, zzao);
                        continue;
                    }
                    case 2: {
                        o = zza.zzb(parcel, zzao, (android.os.Parcelable$Creator<CursorWindow>)CursorWindow.CREATOR);
                        continue;
                    }
                    case 1: {
                        zzB = zza.zzB(parcel, zzao);
                        continue;
                    }
                }
            }
            else {
                zzg = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new zza.zza(sb.toString(), parcel);
        }
        final DataHolder dataHolder = new DataHolder(zzg, zzB, (CursorWindow[])o, zzg2, zzr);
        dataHolder.zzov();
        return dataHolder;
    }
    
    public DataHolder[] zzbv(final int n) {
        return new DataHolder[n];
    }
}
