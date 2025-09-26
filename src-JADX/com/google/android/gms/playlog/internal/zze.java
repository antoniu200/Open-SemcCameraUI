package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zze implements Parcelable.Creator<PlayLoggerContext> {
    static void zza(PlayLoggerContext playLoggerContext, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, playLoggerContext.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, playLoggerContext.packageName, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, playLoggerContext.zzaRR);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, playLoggerContext.zzaRS);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, playLoggerContext.zzaRT, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, playLoggerContext.zzaRU, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, playLoggerContext.zzaRV);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, playLoggerContext.zzaRW, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, playLoggerContext.zzaRX);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 10, playLoggerContext.zzaRY);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzgj, reason: merged with bridge method [inline-methods] */
    public PlayLoggerContext createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        String strZzp2 = null;
        String strZzp3 = null;
        String strZzp4 = null;
        int iZzg = 0;
        int iZzg2 = 0;
        int iZzg3 = 0;
        boolean zZzc = false;
        int iZzg4 = 0;
        boolean zZzc2 = true;
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
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 4:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 5:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 6:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 7:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 8:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 9:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 10:
                    iZzg4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new PlayLoggerContext(iZzg, strZzp, iZzg2, iZzg3, strZzp2, strZzp3, zZzc2, strZzp4, zZzc, iZzg4);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zziV, reason: merged with bridge method [inline-methods] */
    public PlayLoggerContext[] newArray(int i) {
        return new PlayLoggerContext[i];
    }
}
