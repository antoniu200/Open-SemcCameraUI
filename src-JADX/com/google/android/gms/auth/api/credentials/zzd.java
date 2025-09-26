package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzd implements Parcelable.Creator<IdToken> {
    static void zza(IdToken idToken, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, idToken.getAccountType(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, idToken.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, idToken.getIdToken(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzH, reason: merged with bridge method [inline-methods] */
    public IdToken createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        int iZzg = 0;
        String strZzp2 = null;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int iZzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao);
            if (iZzbM != 1000) {
                switch (iZzbM) {
                    case 1:
                        strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    case 2:
                        strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    default:
                        com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                        break;
                }
            } else {
                iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new IdToken(iZzg, strZzp, strZzp2);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzay, reason: merged with bridge method [inline-methods] */
    public IdToken[] newArray(int i) {
        return new IdToken[i];
    }
}
