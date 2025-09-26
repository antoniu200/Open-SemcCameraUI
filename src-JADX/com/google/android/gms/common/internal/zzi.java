package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzi implements Parcelable.Creator<GetServiceRequest> {
    static void zza(GetServiceRequest getServiceRequest, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, getServiceRequest.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, getServiceRequest.zzafq);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, getServiceRequest.zzafr);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, getServiceRequest.zzafs, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, getServiceRequest.zzaft, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, (Parcelable[]) getServiceRequest.zzafu, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, getServiceRequest.zzafv, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, (Parcelable) getServiceRequest.zzafw, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzak, reason: merged with bridge method [inline-methods] */
    public GetServiceRequest createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int iZzg = 0;
        int iZzg2 = 0;
        int iZzg3 = 0;
        String strZzp = null;
        IBinder iBinderZzq = null;
        Scope[] scopeArr = null;
        Bundle bundleZzr = null;
        Account account = null;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 2:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 3:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 4:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 5:
                    iBinderZzq = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, iZzao);
                    break;
                case 6:
                    scopeArr = (Scope[]) com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao, Scope.CREATOR);
                    break;
                case 7:
                    bundleZzr = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, iZzao);
                    break;
                case 8:
                    account = (Account) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, Account.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new GetServiceRequest(iZzg, iZzg2, iZzg3, strZzp, iBinderZzq, scopeArr, bundleZzr, account);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbD, reason: merged with bridge method [inline-methods] */
    public GetServiceRequest[] newArray(int i) {
        return new GetServiceRequest[i];
    }
}
