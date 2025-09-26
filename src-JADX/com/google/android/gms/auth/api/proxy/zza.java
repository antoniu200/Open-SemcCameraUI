package com.google.android.gms.auth.api.proxy;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zza implements Parcelable.Creator<ProxyGrpcRequest> {
    static void zza(ProxyGrpcRequest proxyGrpcRequest, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, proxyGrpcRequest.hostname, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, proxyGrpcRequest.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, proxyGrpcRequest.port);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, proxyGrpcRequest.timeoutMillis);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, proxyGrpcRequest.body, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, proxyGrpcRequest.method, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzL, reason: merged with bridge method [inline-methods] */
    public ProxyGrpcRequest createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int iZzg = 0;
        int iZzg2 = 0;
        String strZzp = null;
        byte[] bArrZzs = null;
        String strZzp2 = null;
        long jZzi = 0;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int iZzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao);
            if (iZzbM != 1000) {
                switch (iZzbM) {
                    case 1:
                        strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    case 2:
                        iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                        break;
                    case 3:
                        jZzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzao);
                        break;
                    case 4:
                        bArrZzs = com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, iZzao);
                        break;
                    case 5:
                        strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
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
            return new ProxyGrpcRequest(iZzg, strZzp, iZzg2, jZzi, bArrZzs, strZzp2);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaC, reason: merged with bridge method [inline-methods] */
    public ProxyGrpcRequest[] newArray(int i) {
        return new ProxyGrpcRequest[i];
    }
}
