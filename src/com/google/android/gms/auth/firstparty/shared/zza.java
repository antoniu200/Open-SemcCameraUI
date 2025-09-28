package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zza implements Parcelable.Creator<FACLConfig> {
    static void zza(FACLConfig fACLConfig, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, fACLConfig.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, fACLConfig.zzTx);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, fACLConfig.zzTy, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, fACLConfig.zzTz);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, fACLConfig.zzTA);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, fACLConfig.zzTB);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, fACLConfig.zzTC);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzT, reason: merged with bridge method [inline-methods] */
    public FACLConfig createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int iZzg = 0;
        boolean zZzc = false;
        boolean zZzc2 = false;
        boolean zZzc3 = false;
        boolean zZzc4 = false;
        boolean zZzc5 = false;
        String strZzp = null;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 2:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 3:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 4:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 5:
                    zZzc3 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 6:
                    zZzc4 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 7:
                    zZzc5 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new FACLConfig(iZzg, zZzc, strZzp, zZzc2, zZzc3, zZzc4, zZzc5);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaK, reason: merged with bridge method [inline-methods] */
    public FACLConfig[] newArray(int i) {
        return new FACLConfig[i];
    }
}
