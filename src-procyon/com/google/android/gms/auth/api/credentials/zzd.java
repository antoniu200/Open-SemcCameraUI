// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials;

import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzd implements Parcelable$Creator<IdToken>
{
    static void zza(final IdToken idToken, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, idToken.getAccountType(), false);
        zzb.zzc(parcel, 1000, idToken.mVersionCode);
        zzb.zza(parcel, 2, idToken.getIdToken(), false);
        zzb.zzI(parcel, zzaq);
    }
    
    public IdToken zzH(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        String zzp = null;
        int zzg = 0;
        String zzp2 = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            final int zzbM = zza.zzbM(zzao);
            if (zzbM != 1000) {
                switch (zzbM) {
                    default: {
                        zza.zzb(parcel, zzao);
                        continue;
                    }
                    case 2: {
                        zzp2 = zza.zzp(parcel, zzao);
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
        return new IdToken(zzg, zzp, zzp2);
    }
    
    public IdToken[] zzay(final int n) {
        return new IdToken[n];
    }
}
