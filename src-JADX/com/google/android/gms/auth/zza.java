package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zza implements Parcelable.Creator<AccountChangeEvent> {
    static void zza(AccountChangeEvent accountChangeEvent, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, accountChangeEvent.mVersion);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, accountChangeEvent.zzRr);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, accountChangeEvent.zzRs, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, accountChangeEvent.zzRt);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 5, accountChangeEvent.zzRu);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, accountChangeEvent.zzRv, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaq, reason: merged with bridge method [inline-methods] */
    public AccountChangeEvent[] newArray(int i) {
        return new AccountChangeEvent[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzz, reason: merged with bridge method [inline-methods] */
    public AccountChangeEvent createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        String strZzp2 = null;
        int iZzg = 0;
        int iZzg2 = 0;
        int iZzg3 = 0;
        long jZzi = 0;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 2:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzao);
                    break;
                case 3:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 4:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 5:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 6:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new AccountChangeEvent(iZzg, jZzi, strZzp, iZzg2, iZzg3, strZzp2);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }
}
