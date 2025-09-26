// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zze implements Parcelable$Creator<PasswordSpecification>
{
    static void zza(final PasswordSpecification passwordSpecification, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, passwordSpecification.zzSv, false);
        zzb.zzc(parcel, 1000, passwordSpecification.mVersionCode);
        zzb.zzb(parcel, 2, passwordSpecification.zzSw, false);
        zzb.zza(parcel, 3, passwordSpecification.zzSx, false);
        zzb.zzc(parcel, 4, passwordSpecification.zzSy);
        zzb.zzc(parcel, 5, passwordSpecification.zzSz);
        zzb.zzI(parcel, zzaq);
    }
    
    public PasswordSpecification zzI(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        String zzp = null;
        ArrayList<Integer> zzC;
        List<E> zzD = (List<E>)(zzC = null);
        int zzg = 0;
        int zzg3;
        int zzg2 = zzg3 = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            final int zzbM = zza.zzbM(zzao);
            if (zzbM != 1000) {
                switch (zzbM) {
                    default: {
                        zza.zzb(parcel, zzao);
                        continue;
                    }
                    case 5: {
                        zzg3 = zza.zzg(parcel, zzao);
                        continue;
                    }
                    case 4: {
                        zzg2 = zza.zzg(parcel, zzao);
                        continue;
                    }
                    case 3: {
                        zzC = zza.zzC(parcel, zzao);
                        continue;
                    }
                    case 2: {
                        zzD = (List<E>)zza.zzD(parcel, zzao);
                        continue;
                    }
                    case 1: {
                        zzp = zza.zzp(parcel, zzao);
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
        return new PasswordSpecification(zzg, zzp, (List<String>)zzD, zzC, zzg2, zzg3);
    }
    
    public PasswordSpecification[] zzaz(final int n) {
        return new PasswordSpecification[n];
    }
}
