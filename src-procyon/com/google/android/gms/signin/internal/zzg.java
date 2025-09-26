// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.signin.internal;

import android.accounts.Account;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzg implements Parcelable$Creator<RecordConsentRequest>
{
    static void zza(final RecordConsentRequest recordConsentRequest, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, recordConsentRequest.mVersionCode);
        zzb.zza(parcel, 2, (Parcelable)recordConsentRequest.getAccount(), n, false);
        zzb.zza(parcel, 3, recordConsentRequest.zzCj(), n, false);
        zzb.zza(parcel, 4, recordConsentRequest.zzmb(), false);
        zzb.zzI(parcel, zzaq);
    }
    
    public RecordConsentRequest zzgD(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        Account account = null;
        int zzg = 0;
        Scope[] array = null;
        String zzp = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 4: {
                    zzp = zza.zzp(parcel, zzao);
                    continue;
                }
                case 3: {
                    array = zza.zzb(parcel, zzao, Scope.CREATOR);
                    continue;
                }
                case 2: {
                    account = zza.zza(parcel, zzao, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
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
        return new RecordConsentRequest(zzg, account, array, zzp);
    }
    
    public RecordConsentRequest[] zzjr(final int n) {
        return new RecordConsentRequest[n];
    }
}
