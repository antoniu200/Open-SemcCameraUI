package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zza implements Parcelable.Creator<ConnectionEvent> {
    static void zza(ConnectionEvent connectionEvent, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, connectionEvent.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, connectionEvent.getTimeMillis());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, connectionEvent.zzpX(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, connectionEvent.zzpY(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, connectionEvent.zzpZ(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, connectionEvent.zzqa(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, connectionEvent.zzqb(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, connectionEvent.zzqf());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 11, connectionEvent.zzqe());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 12, connectionEvent.getEventType());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 13, connectionEvent.zzqc(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaA, reason: merged with bridge method [inline-methods] */
    public ConnectionEvent createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int iZzg = 0;
        int iZzg2 = 0;
        long jZzi = 0;
        long jZzi2 = 0;
        long jZzi3 = 0;
        String strZzp = null;
        String strZzp2 = null;
        String strZzp3 = null;
        String strZzp4 = null;
        String strZzp5 = null;
        String strZzp6 = null;
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
                case 9:
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
                case 4:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 5:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 6:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 7:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 8:
                    strZzp5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 10:
                    jZzi2 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzao);
                    break;
                case 11:
                    jZzi3 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzao);
                    break;
                case 12:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 13:
                    strZzp6 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new ConnectionEvent(iZzg, jZzi, iZzg2, strZzp, strZzp2, strZzp3, strZzp4, strZzp5, strZzp6, jZzi2, jZzi3);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbY, reason: merged with bridge method [inline-methods] */
    public ConnectionEvent[] newArray(int i) {
        return new ConnectionEvent[i];
    }
}
