// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import java.util.List;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzd implements Parcelable$Creator<TokenData>
{
    static void zza(final TokenData tokenData, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, tokenData.mVersionCode);
        zzb.zza(parcel, 2, tokenData.getToken(), false);
        zzb.zza(parcel, 3, tokenData.zzlA(), false);
        zzb.zza(parcel, 4, tokenData.zzlB());
        zzb.zza(parcel, 5, tokenData.zzlC());
        zzb.zzb(parcel, 6, tokenData.zzlD(), false);
        zzb.zzI(parcel, zzaq);
    }
    
    public TokenData zzC(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        String zzp = null;
        List<String> zzD;
        Object zzj = zzD = null;
        int zzg = 0;
        boolean zzc2;
        boolean zzc = zzc2 = false;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 6: {
                    zzD = zza.zzD(parcel, zzao);
                    continue;
                }
                case 5: {
                    zzc2 = zza.zzc(parcel, zzao);
                    continue;
                }
                case 4: {
                    zzc = zza.zzc(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzj = zza.zzj(parcel, zzao);
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
        return new TokenData(zzg, zzp, (Long)zzj, zzc, zzc2, zzD);
    }
    
    public TokenData[] zzat(final int n) {
        return new TokenData[n];
    }
}
