// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<EmailSignInConfig>
{
    static void zza(final EmailSignInConfig emailSignInConfig, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, emailSignInConfig.versionCode);
        zzb.zza(parcel, 2, (Parcelable)emailSignInConfig.zzlO(), n, false);
        zzb.zza(parcel, 3, emailSignInConfig.zzlQ(), false);
        zzb.zza(parcel, 4, (Parcelable)emailSignInConfig.zzlP(), n, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public EmailSignInConfig zzO(final Parcel parcel) {
        final int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        Uri uri = null;
        int zzg = 0;
        String zzp = null;
        Uri uri2 = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    continue;
                }
                case 4: {
                    uri2 = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, (android.os.Parcelable$Creator<Uri>)Uri.CREATOR);
                    continue;
                }
                case 3: {
                    zzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    continue;
                }
                case 2: {
                    uri = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, (android.os.Parcelable$Creator<Uri>)Uri.CREATOR);
                    continue;
                }
                case 1: {
                    zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza(sb.toString(), parcel);
        }
        return new EmailSignInConfig(zzg, uri, zzp, uri2);
    }
    
    public EmailSignInConfig[] zzaF(final int n) {
        return new EmailSignInConfig[n];
    }
}
