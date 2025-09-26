package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.common.server.response.FastJsonResponse;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zza implements Parcelable.Creator<FastJsonResponse.Field> {
    static void zza(FastJsonResponse.Field field, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, field.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, field.zzpB());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, field.zzpH());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, field.zzpC());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, field.zzpI());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, field.zzpJ(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 7, field.zzpK());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, field.zzpM(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, (Parcelable) field.zzpO(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzav, reason: merged with bridge method [inline-methods] */
    public FastJsonResponse.Field createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        String strZzp2 = null;
        ConverterWrapper converterWrapper = null;
        int iZzg = 0;
        int iZzg2 = 0;
        boolean zZzc = false;
        int iZzg3 = 0;
        boolean zZzc2 = false;
        int iZzg4 = 0;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 2:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 3:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 4:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 5:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 6:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 7:
                    iZzg4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 8:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 9:
                    converterWrapper = (ConverterWrapper) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, ConverterWrapper.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new FastJsonResponse.Field(iZzg, iZzg2, zZzc, iZzg3, zZzc2, strZzp, iZzg4, strZzp2, converterWrapper);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbT, reason: merged with bridge method [inline-methods] */
    public FastJsonResponse.Field[] newArray(int i) {
        return new FastJsonResponse.Field[i];
    }
}
