package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzc implements Parcelable.Creator<ScopeDetail> {
    static void zza(ScopeDetail scopeDetail, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, scopeDetail.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, scopeDetail.description, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, scopeDetail.zzTH, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, scopeDetail.zzTI, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, scopeDetail.zzTJ, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, scopeDetail.zzTK, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 7, scopeDetail.zzTL, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, (Parcelable) scopeDetail.zzTM, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzV, reason: merged with bridge method [inline-methods] */
    public ScopeDetail createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        ArrayList<String> arrayList = new ArrayList<>();
        String strZzp = null;
        String strZzp2 = null;
        String strZzp3 = null;
        String strZzp4 = null;
        String strZzp5 = null;
        FACLData fACLData = null;
        int iZzg = 0;
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
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 4:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 5:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 6:
                    strZzp5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 7:
                    arrayList = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzao);
                    break;
                case 8:
                    fACLData = (FACLData) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, FACLData.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new ScopeDetail(iZzg, strZzp, strZzp2, strZzp3, strZzp4, strZzp5, arrayList, fACLData);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaM, reason: merged with bridge method [inline-methods] */
    public ScopeDetail[] newArray(int i) {
        return new ScopeDetail[i];
    }
}
