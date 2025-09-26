// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.signin.internal;

import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<AuthAccountResult>
{
    static void zza(final AuthAccountResult authAccountResult, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, authAccountResult.mVersionCode);
        zzb.zzI(parcel, zzaq);
    }
    
    public AuthAccountResult zzgB(final Parcel parcel) {
        final int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int zzg = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            if (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao) != 1) {
                com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
            }
            else {
                zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza(sb.toString(), parcel);
        }
        return new AuthAccountResult(zzg);
    }
    
    public AuthAccountResult[] zzjo(final int n) {
        return new AuthAccountResult[n];
    }
}
