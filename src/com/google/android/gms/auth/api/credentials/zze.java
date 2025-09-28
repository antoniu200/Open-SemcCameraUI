package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zze implements Parcelable.Creator<PasswordSpecification> {
    static void zza(PasswordSpecification passwordSpecification, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, passwordSpecification.zzSv, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, passwordSpecification.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 2, passwordSpecification.zzSw, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, passwordSpecification.zzSx, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, passwordSpecification.zzSy);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 5, passwordSpecification.zzSz);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzI, reason: merged with bridge method [inline-methods] */
    public PasswordSpecification createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        ArrayList<String> arrayListZzD = null;
        ArrayList<Integer> arrayListZzC = null;
        int iZzg = 0;
        int iZzg2 = 0;
        int iZzg3 = 0;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int iZzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao);
            if (iZzbM != 1000) {
                switch (iZzbM) {
                    case 1:
                        strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    case 2:
                        arrayListZzD = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzao);
                        break;
                    case 3:
                        arrayListZzC = com.google.android.gms.common.internal.safeparcel.zza.zzC(parcel, iZzao);
                        break;
                    case 4:
                        iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                        break;
                    case 5:
                        iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
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
            return new PasswordSpecification(iZzg, strZzp, arrayListZzD, arrayListZzC, iZzg2, iZzg3);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaz, reason: merged with bridge method [inline-methods] */
    public PasswordSpecification[] newArray(int i) {
        return new PasswordSpecification[i];
    }
}
