// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.consent;

import android.accounts.Account;
import com.google.android.gms.auth.firstparty.shared.ScopeDetail;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<GetConsentIntentRequest>
{
    static void zza(final GetConsentIntentRequest getConsentIntentRequest, final Parcel parcel, final int n) {
        final int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, getConsentIntentRequest.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, getConsentIntentRequest.getCallingPackage(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, getConsentIntentRequest.getCallingUid());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, getConsentIntentRequest.zzlF(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, (Parcelable)getConsentIntentRequest.getAccount(), n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, getConsentIntentRequest.zzSe, n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, getConsentIntentRequest.zzlG());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 8, getConsentIntentRequest.zzlH());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }
    
    public GetConsentIntentRequest zzD(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        String zzp = null;
        Object zzp2 = null;
        Object o2;
        Object o = o2 = zzp2;
        int zzg = 0;
        int zzg2 = 0;
        int zzg3;
        int zzc = zzg3 = zzg2;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 8: {
                    zzg3 = zza.zzg(parcel, zzao);
                    continue;
                }
                case 7: {
                    zzc = (zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 6: {
                    o2 = zza.zzb(parcel, zzao, (android.os.Parcelable$Creator<ScopeDetail>)ScopeDetail.CREATOR);
                    continue;
                }
                case 5: {
                    o = zza.zza(parcel, zzao, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                    continue;
                }
                case 4: {
                    zzp2 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzg2 = zza.zzg(parcel, zzao);
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
        return new GetConsentIntentRequest(zzg, zzp, zzg2, (String)zzp2, (Account)o, (ScopeDetail[])o2, (boolean)(zzc != 0), zzg3);
    }
    
    public GetConsentIntentRequest[] zzau(final int n) {
        return new GetConsentIntentRequest[n];
    }
}
