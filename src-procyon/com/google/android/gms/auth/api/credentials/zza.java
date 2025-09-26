// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials;

import java.util.List;
import android.net.Uri;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<Credential>
{
    static void zza(final Credential credential, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zza(parcel, 1, credential.getId(), false);
        zzb.zzc(parcel, 1000, credential.mVersionCode);
        zzb.zza(parcel, 2, credential.getName(), false);
        zzb.zza(parcel, 3, (Parcelable)credential.getProfilePictureUri(), n, false);
        zzb.zzc(parcel, 4, credential.getIdTokens(), false);
        zzb.zza(parcel, 5, credential.getPassword(), false);
        zzb.zza(parcel, 6, credential.getAccountType(), false);
        zzb.zza(parcel, 7, credential.getGeneratedPassword(), false);
        zzb.zza(parcel, 8, credential.zzlI(), false);
        zzb.zzI(parcel, zzaq);
    }
    
    public Credential zzE(final Parcel parcel) {
        final int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String zzp = null;
        Object zzp2 = null;
        Object zzc;
        Object o = zzc = zzp2;
        Object zzp4;
        Object zzp3 = zzp4 = zzc;
        Object zzp6;
        String zzp5 = (String)(zzp6 = zzp4);
        int zzg = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            final int zzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao);
            if (zzbM != 1000) {
                switch (zzbM) {
                    default: {
                        com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                        continue;
                    }
                    case 8: {
                        zzp6 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        continue;
                    }
                    case 7: {
                        zzp5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        continue;
                    }
                    case 6: {
                        zzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        continue;
                    }
                    case 5: {
                        zzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        continue;
                    }
                    case 4: {
                        zzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao, IdToken.CREATOR);
                        continue;
                    }
                    case 3: {
                        o = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, (android.os.Parcelable$Creator<Uri>)Uri.CREATOR);
                        continue;
                    }
                    case 2: {
                        zzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        continue;
                    }
                    case 1: {
                        zzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                        continue;
                    }
                }
            }
            else {
                zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza(sb.toString(), parcel);
        }
        return new Credential(zzg, zzp, (String)zzp2, (Uri)o, (List<IdToken>)zzc, (String)zzp3, (String)zzp4, zzp5, (String)zzp6);
    }
    
    public Credential[] zzav(final int n) {
        return new Credential[n];
    }
}
