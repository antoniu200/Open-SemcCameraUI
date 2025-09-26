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

public class zzf implements Parcelable$Creator<DeleteRequest>
{
    static void zza(final DeleteRequest deleteRequest, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, (Parcelable)deleteRequest.getCredential(), n, false);
        zzb.zzc(parcel, 1000, deleteRequest.mVersionCode);
        zzb.zzI(parcel, zzaq);
    }
    
    public DeleteRequest zzJ(final Parcel parcel) {
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
        return new DeleteRequest(zzg, credential);
    }
    
    public DeleteRequest[] zzaA(final int n) {
        return new DeleteRequest[n];
    }
}
