package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zza implements Parcelable.Creator<BitmapTeleporter> {
    static void zza(BitmapTeleporter bitmapTeleporter, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, bitmapTeleporter.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) bitmapTeleporter.zzFc, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, bitmapTeleporter.zzWJ);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaf, reason: merged with bridge method [inline-methods] */
    public BitmapTeleporter createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int iZzg = 0;
        ParcelFileDescriptor parcelFileDescriptor = null;
        int iZzg2 = 0;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 2:
                    parcelFileDescriptor = (ParcelFileDescriptor) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, ParcelFileDescriptor.CREATOR);
                    break;
                case 3:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new BitmapTeleporter(iZzg, parcelFileDescriptor, iZzg2);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbq, reason: merged with bridge method [inline-methods] */
    public BitmapTeleporter[] newArray(int i) {
        return new BitmapTeleporter[i];
    }
}
