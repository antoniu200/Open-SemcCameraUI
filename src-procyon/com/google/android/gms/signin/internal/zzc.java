// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.signin.internal;

import java.util.List;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<CheckServerAuthResult>
{
    static void zza(final CheckServerAuthResult checkServerAuthResult, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, checkServerAuthResult.mVersionCode);
        zzb.zza(parcel, 2, checkServerAuthResult.zzaVi);
        zzb.zzc(parcel, 3, checkServerAuthResult.zzaVj, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public CheckServerAuthResult zzgC(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        List<Scope> zzc = null;
        boolean zzc2 = false;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzc = zza.zzc(parcel, zzao, Scope.CREATOR);
                    continue;
                }
                case 2: {
                    zzc2 = zza.zzc(parcel, zzao);
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
        return new CheckServerAuthResult(zzg, zzc2, zzc);
    }
    
    public CheckServerAuthResult[] zzjp(final int n) {
        return new CheckServerAuthResult[n];
    }
}
