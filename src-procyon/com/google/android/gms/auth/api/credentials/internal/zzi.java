// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials.internal;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzi implements Parcelable$Creator<SaveRequest>
{
    static void zza(final SaveRequest saveRequest, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, (Parcelable)saveRequest.getCredential(), n, false);
        zzb.zzc(parcel, 1000, saveRequest.mVersionCode);
        zzb.zzI(parcel, zzaq);
    }
    
    public SaveRequest zzK(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        Credential credential = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            final int zzbM = zza.zzbM(zzao);
            if (zzbM != 1) {
                if (zzbM != 1000) {
                    zza.zzb(parcel, zzao);
                }
                else {
                    zzg = zza.zzg(parcel, zzao);
                }
            }
            else {
                credential = zza.zza(parcel, zzao, Credential.CREATOR);
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new zza.zza(sb.toString(), parcel);
        }
        return new SaveRequest(zzg, credential);
    }
    
    public SaveRequest[] zzaB(final int n) {
        return new SaveRequest[n];
    }
}
