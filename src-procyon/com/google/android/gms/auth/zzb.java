// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import android.accounts.Account;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<AccountChangeEventsRequest>
{
    static void zza(final AccountChangeEventsRequest accountChangeEventsRequest, final Parcel parcel, final int n) {
        final int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, accountChangeEventsRequest.mVersion);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, accountChangeEventsRequest.zzRu);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, accountChangeEventsRequest.zzRs, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable)accountChangeEventsRequest.zzQd, n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }
    
    public AccountChangeEventsRequest zzA(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        String zzp = null;
        Account account = null;
        int zzg2 = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 4: {
                    account = zza.zza(parcel, zzao, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                    continue;
                }
                case 3: {
                    zzp = zza.zzp(parcel, zzao);
                    continue;
                }
                case 2: {
                    zzg2 = zza.zzg(parcel, zzao);
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
        return new AccountChangeEventsRequest(zzg, zzg2, zzp, account);
    }
    
    public AccountChangeEventsRequest[] zzar(final int n) {
        return new AccountChangeEventsRequest[n];
    }
}
