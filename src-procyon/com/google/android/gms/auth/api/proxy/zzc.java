// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.proxy;

import android.os.Bundle;
import android.app.PendingIntent;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<ProxyResponse>
{
    static void zza(final ProxyResponse proxyResponse, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, proxyResponse.googlePlayServicesStatusCode);
        zzb.zzc(parcel, 1000, proxyResponse.versionCode);
        zzb.zza(parcel, 2, (Parcelable)proxyResponse.recoveryAction, n, false);
        zzb.zzc(parcel, 3, proxyResponse.statusCode);
        zzb.zza(parcel, 4, proxyResponse.zzSK, false);
        zzb.zza(parcel, 5, proxyResponse.body, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public ProxyResponse zzN(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        PendingIntent pendingIntent = null;
        byte[] zzs;
        Object zzr = zzs = null;
        int zzg = 0;
        int zzg3;
        int zzg2 = zzg3 = 0;
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
                        zzs = zza.zzs(parcel, zzao);
                        continue;
                    }
                    case 4: {
                        zzr = zza.zzr(parcel, zzao);
                        continue;
                    }
                    case 3: {
                        zzg3 = zza.zzg(parcel, zzao);
                        continue;
                    }
                    case 2: {
                        pendingIntent = zza.zza(parcel, zzao, (android.os.Parcelable$Creator<PendingIntent>)PendingIntent.CREATOR);
                        continue;
                    }
                    case 1: {
                        zzg2 = zza.zzg(parcel, zzao);
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
        return new ProxyResponse(zzg, zzg2, pendingIntent, zzg3, (Bundle)zzr, zzs);
    }
    
    public ProxyResponse[] zzaE(final int n) {
        return new ProxyResponse[n];
    }
}
