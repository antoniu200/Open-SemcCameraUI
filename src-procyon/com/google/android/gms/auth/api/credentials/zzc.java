// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials;

import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<CredentialRequest>
{
    static void zza(final CredentialRequest credentialRequest, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, credentialRequest.getSupportsPasswordLogin());
        zzb.zzc(parcel, 1000, credentialRequest.mVersionCode);
        zzb.zza(parcel, 2, credentialRequest.getAccountTypes(), false);
        zzb.zza(parcel, 3, (Parcelable)credentialRequest.getCredentialPickerConfig(), n, false);
        zzb.zza(parcel, 4, (Parcelable)credentialRequest.getCredentialHintPickerConfig(), n, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public CredentialRequest zzG(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        boolean zzc = false;
        String[] zzB = null;
        CredentialPickerConfig credentialPickerConfig2;
        CredentialPickerConfig credentialPickerConfig = credentialPickerConfig2 = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            final int zzbM = zza.zzbM(zzao);
            if (zzbM != 1000) {
                switch (zzbM) {
                    default: {
                        zza.zzb(parcel, zzao);
                        continue;
                    }
                    case 4: {
                        credentialPickerConfig2 = zza.zza(parcel, zzao, CredentialPickerConfig.CREATOR);
                        continue;
                    }
                    case 3: {
                        credentialPickerConfig = zza.zza(parcel, zzao, CredentialPickerConfig.CREATOR);
                        continue;
                    }
                    case 2: {
                        zzB = zza.zzB(parcel, zzao);
                        continue;
                    }
                    case 1: {
                        zzc = zza.zzc(parcel, zzao);
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
        return new CredentialRequest(zzg, zzc, zzB, credentialPickerConfig, credentialPickerConfig2);
    }
    
    public CredentialRequest[] zzax(final int n) {
        return new CredentialRequest[n];
    }
}
