// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.os.IBinder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzz implements Parcelable$Creator<ResolveAccountResponse>
{
    static void zza(final ResolveAccountResponse resolveAccountResponse, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, resolveAccountResponse.mVersionCode);
        zzb.zza(parcel, 2, resolveAccountResponse.zzaeH, false);
        zzb.zza(parcel, 3, (Parcelable)resolveAccountResponse.zzpr(), n, false);
        zzb.zza(parcel, 4, resolveAccountResponse.zzps());
        zzb.zza(parcel, 5, resolveAccountResponse.zzpt());
        zzb.zzI(parcel, zzaq);
    }
    
    public ResolveAccountResponse zzam(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        IBinder zzq = null;
        ConnectionResult connectionResult = null;
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
                case 5: {
                    zzc2 = zza.zzc(parcel, zzao);
                    continue;
                }
                case 4: {
                    zzc = zza.zzc(parcel, zzao);
                    continue;
                }
                case 3: {
                    connectionResult = zza.zza(parcel, zzao, ConnectionResult.CREATOR);
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
        return new ResolveAccountResponse(zzg, zzq, connectionResult, zzc, zzc2);
    }
    
    public ResolveAccountResponse[] zzbK(final int n) {
        return new ResolveAccountResponse[n];
    }
}
