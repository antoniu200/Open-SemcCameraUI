// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials;

import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<CredentialPickerConfig>
{
    static void zza(final CredentialPickerConfig credentialPickerConfig, final Parcel parcel, int zzaq) {
        zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, credentialPickerConfig.shouldShowAddAccountButton());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, credentialPickerConfig.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, credentialPickerConfig.shouldShowCancelButton());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }
    
    public CredentialPickerConfig zzF(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        boolean zzc = false;
        boolean zzc2 = false;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            final int zzbM = zza.zzbM(zzao);
            if (zzbM != 1000) {
                switch (zzbM) {
                    default: {
                        zza.zzb(parcel, zzao);
                        continue;
                    }
                    case 2: {
                        zzc2 = zza.zzc(parcel, zzao);
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
        return new CredentialPickerConfig(zzg, zzc, zzc2);
    }
    
    public CredentialPickerConfig[] zzaw(final int n) {
        return new CredentialPickerConfig[n];
    }
}
