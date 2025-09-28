package com.google.android.gms.auth.api.consent;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.firstparty.shared.ScopeDetail;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzb implements Parcelable.Creator<GetConsentIntentRequest> {
    static void zza(GetConsentIntentRequest getConsentIntentRequest, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, getConsentIntentRequest.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, getConsentIntentRequest.getCallingPackage(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, getConsentIntentRequest.getCallingUid());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, getConsentIntentRequest.zzlF(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, (Parcelable) getConsentIntentRequest.getAccount(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, (Parcelable[]) getConsentIntentRequest.zzSe, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, getConsentIntentRequest.zzlG());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 8, getConsentIntentRequest.zzlH());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzD, reason: merged with bridge method [inline-methods] */
    public GetConsentIntentRequest createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        String strZzp2 = null;
        Account account = null;
        ScopeDetail[] scopeDetailArr = null;
        int iZzg = 0;
        int iZzg2 = 0;
        boolean zZzc = false;
        int iZzg3 = 0;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 2:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 3:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 4:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 5:
                    account = (Account) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, Account.CREATOR);
                    break;
                case 6:
                    scopeDetailArr = (ScopeDetail[]) com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao, ScopeDetail.CREATOR);
                    break;
                case 7:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 8:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new GetConsentIntentRequest(iZzg, strZzp, iZzg2, strZzp2, account, scopeDetailArr, zZzc, iZzg3);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzau, reason: merged with bridge method [inline-methods] */
    public GetConsentIntentRequest[] newArray(int i) {
        return new GetConsentIntentRequest[i];
    }
}
