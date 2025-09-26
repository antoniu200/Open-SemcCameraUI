// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.os.IBinder;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<AuthAccountRequest>
{
    static void zza(final AuthAccountRequest authAccountRequest, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, authAccountRequest.mVersionCode);
        zzb.zza(parcel, 2, authAccountRequest.zzaeH, false);
        zzb.zza(parcel, 3, authAccountRequest.zzaeI, n, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public AuthAccountRequest zzai(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        IBinder zzq = null;
        int zzg = 0;
        Scope[] array = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 3: {
                    array = zza.zzb(parcel, zzao, Scope.CREATOR);
                    continue;
                }
                case 2: {
                    zzq = zza.zzq(parcel, zzao);
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
        return new AuthAccountRequest(zzg, zzq, array);
    }
    
    public AuthAccountRequest[] zzbB(final int n) {
        return new AuthAccountRequest[n];
    }
}
