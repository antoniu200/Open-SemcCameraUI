// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common;

import android.app.PendingIntent;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<ConnectionResult>
{
    static void zza(final ConnectionResult connectionResult, final Parcel parcel, final int n) {
        final int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, connectionResult.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, connectionResult.getErrorCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable)connectionResult.getResolution(), n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, connectionResult.getErrorMessage(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }
    
    public ConnectionResult zzac(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        PendingIntent pendingIntent = null;
        String zzp = null;
        int zzg2 = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 4: {
                    zzp = zza.zzp(parcel, zzao);
                    continue;
                }
                case 3: {
                    pendingIntent = zza.zza(parcel, zzao, (android.os.Parcelable$Creator<PendingIntent>)PendingIntent.CREATOR);
                    continue;
                }
                case 2: {
                    zzg2 = zza.zzg(parcel, zzao);
                    continue;
                }
                case 1: {
                    zzg = zza.zzg(parcel, zzao);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new zza.zza(sb.toString(), parcel);
        }
        return new ConnectionResult(zzg, zzg2, pendingIntent, zzp);
    }
    
    public ConnectionResult[] zzbh(final int n) {
        return new ConnectionResult[n];
    }
}
