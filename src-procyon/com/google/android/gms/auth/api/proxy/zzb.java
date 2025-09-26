// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.proxy;

import android.os.Bundle;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<ProxyRequest>
{
    static void zza(final ProxyRequest proxyRequest, final Parcel parcel, int zzaq) {
        zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, proxyRequest.url, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, proxyRequest.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, proxyRequest.httpMethod);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, proxyRequest.timeoutMillis);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, proxyRequest.body, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, proxyRequest.zzSK, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }
    
    public ProxyRequest zzM(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        int zzg2 = 0;
        String zzp = null;
        Bundle zzr;
        Object zzs = zzr = null;
        long zzi = 0L;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            final int zzbM = zza.zzbM(zzao);
            if (zzbM != 1000) {
                switch (zzbM) {
                    default: {
                        zza.zzb(parcel, zzao);
                        continue;
                    }
                    case 5: {
                        zzr = zza.zzr(parcel, zzao);
                        continue;
                    }
                    case 4: {
                        zzs = zza.zzs(parcel, zzao);
                        continue;
                    }
                    case 3: {
                        zzi = zza.zzi(parcel, zzao);
                        continue;
                    }
                    case 2: {
                        zzg2 = zza.zzg(parcel, zzao);
                        continue;
                    }
                    case 1: {
                        zzp = zza.zzp(parcel, zzao);
                        continue;
                    }
                }
            }
            else {
                zzg = zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new zza.zza(sb.toString(), parcel);
        }
        return new ProxyRequest(zzg, zzp, zzg2, zzi, (byte[])zzs, zzr);
    }
    
    public ProxyRequest[] zzaD(final int n) {
        return new ProxyRequest[n];
    }
}
