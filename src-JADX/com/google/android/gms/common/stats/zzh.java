package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzh implements Parcelable.Creator<WakeLockEvent> {
    static void zza(WakeLockEvent wakeLockEvent, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, wakeLockEvent.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, wakeLockEvent.getTimeMillis());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, wakeLockEvent.zzqj(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 5, wakeLockEvent.zzql());
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 6, wakeLockEvent.zzqm(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, wakeLockEvent.zzqf());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, wakeLockEvent.zzqk(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 11, wakeLockEvent.getEventType());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 12, wakeLockEvent.zzqc(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 13, wakeLockEvent.zzqo(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 14, wakeLockEvent.zzqn());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 15, wakeLockEvent.zzqp());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 16, wakeLockEvent.zzqq());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaB, reason: merged with bridge method [inline-methods] */
    public WakeLockEvent createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        long jZzi = 0;
        long jZzi2 = 0;
        long jZzi3 = 0;
        int iZzg = 0;
        int iZzg2 = 0;
        int iZzg3 = 0;
        int iZzg4 = 0;
        String strZzp = null;
        ArrayList<String> arrayListZzD = null;
        String strZzp2 = null;
        String strZzp3 = null;
        String strZzp4 = null;
        float fZzl = 0.0f;
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
                case 7:
                case 9:
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
                case 4:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 5:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 6:
                    arrayListZzD = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzao);
                    break;
                case 8:
                    jZzi2 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzao);
                    break;
                case 10:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 11:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 12:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 13:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 14:
                    iZzg4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 15:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzao);
                    break;
                case 16:
                    jZzi3 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new WakeLockEvent(iZzg, jZzi, iZzg2, strZzp, iZzg3, arrayListZzD, strZzp2, jZzi2, iZzg4, strZzp3, strZzp4, fZzl, jZzi3);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbZ, reason: merged with bridge method [inline-methods] */
    public WakeLockEvent[] newArray(int i) {
        return new WakeLockEvent[i];
    }
}
