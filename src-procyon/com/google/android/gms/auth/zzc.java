// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import java.util.List;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<AccountChangeEventsResponse>
{
    static void zza(final AccountChangeEventsResponse accountChangeEventsResponse, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, accountChangeEventsResponse.mVersion);
        zzb.zzc(parcel, 2, accountChangeEventsResponse.zzoQ, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public AccountChangeEventsResponse zzB(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        List<AccountChangeEvent> zzc = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 2: {
                    zzc = zza.zzc(parcel, zzao, AccountChangeEvent.CREATOR);
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
        return new AccountChangeEventsResponse(zzg, zzc);
    }
    
    public AccountChangeEventsResponse[] zzas(final int n) {
        return new AccountChangeEventsResponse[n];
    }
}
