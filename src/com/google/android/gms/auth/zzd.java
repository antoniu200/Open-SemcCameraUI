package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzd implements Parcelable.Creator<TokenData> {
    static void zza(TokenData tokenData, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, tokenData.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, tokenData.getToken(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, tokenData.zzlA(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, tokenData.zzlB());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, tokenData.zzlC());
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 6, tokenData.zzlD(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzC, reason: merged with bridge method [inline-methods] */
    public TokenData createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        Long lZzj = null;
        ArrayList<String> arrayListZzD = null;
        int iZzg = 0;
        boolean zZzc = false;
        boolean zZzc2 = false;
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
                    lZzj = com.google.android.gms.common.internal.safeparcel.zza.zzj(parcel, iZzao);
                    break;
                case 4:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 5:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 6:
                    arrayListZzD = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new TokenData(iZzg, strZzp, lZzj, zZzc, zZzc2, arrayListZzD);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzat, reason: merged with bridge method [inline-methods] */
    public TokenData[] newArray(int i) {
        return new TokenData[i];
    }
}
