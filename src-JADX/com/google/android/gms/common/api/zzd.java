package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzd implements Parcelable.Creator<Status> {
    static void zza(Status status, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, status.getStatusCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, status.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, status.getStatusMessage(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) status.zznH(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzae, reason: merged with bridge method [inline-methods] */
    public Status createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int iZzg = 0;
        String strZzp = null;
        PendingIntent pendingIntent = null;
        int iZzg2 = 0;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int iZzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao);
            if (iZzbM != 1000) {
                switch (iZzbM) {
                    case 1:
                        iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                        break;
                    case 2:
                        strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    case 3:
                        pendingIntent = (PendingIntent) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, PendingIntent.CREATOR);
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
            return new Status(iZzg, iZzg2, strZzp, pendingIntent);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbm, reason: merged with bridge method [inline-methods] */
    public Status[] newArray(int i) {
        return new Status[i];
    }
}
