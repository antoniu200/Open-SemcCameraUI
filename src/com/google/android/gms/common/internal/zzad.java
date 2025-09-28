package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzad implements Parcelable.Creator<ValidateAccountRequest> {
    static void zza(ValidateAccountRequest validateAccountRequest, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, validateAccountRequest.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, validateAccountRequest.zzpu());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, validateAccountRequest.zzaeH, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable[]) validateAccountRequest.zzpv(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, validateAccountRequest.zzpw(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, validateAccountRequest.getCallingPackage(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzan, reason: merged with bridge method [inline-methods] */
    public ValidateAccountRequest createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int iZzg = 0;
        int iZzg2 = 0;
        IBinder iBinderZzq = null;
        Scope[] scopeArr = null;
        Bundle bundleZzr = null;
        String strZzp = null;
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
                    iBinderZzq = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, iZzao);
                    break;
                case 4:
                    scopeArr = (Scope[]) com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao, Scope.CREATOR);
                    break;
                case 5:
                    bundleZzr = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, iZzao);
                    break;
                case 6:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new ValidateAccountRequest(iZzg, iZzg2, iBinderZzq, scopeArr, bundleZzr, strZzp);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbL, reason: merged with bridge method [inline-methods] */
    public ValidateAccountRequest[] newArray(int i) {
        return new ValidateAccountRequest[i];
    }
}
