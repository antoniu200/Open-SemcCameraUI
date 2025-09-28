package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.EmailSignInConfig;
import com.google.android.gms.auth.api.signin.FacebookSignInConfig;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzh implements Parcelable.Creator<SignInConfiguration> {
    static void zza(SignInConfiguration signInConfiguration, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, signInConfiguration.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, signInConfiguration.zzme(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, signInConfiguration.zzmb(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable) signInConfiguration.zzmf(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, (Parcelable) signInConfiguration.zzmg(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, (Parcelable) signInConfiguration.zzmh(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, signInConfiguration.zzmi(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzS, reason: merged with bridge method [inline-methods] */
    public SignInConfiguration createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        String strZzp2 = null;
        EmailSignInConfig emailSignInConfig = null;
        GoogleSignInConfig googleSignInConfig = null;
        FacebookSignInConfig facebookSignInConfig = null;
        String strZzp3 = null;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 2:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 3:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 4:
                    emailSignInConfig = (EmailSignInConfig) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, EmailSignInConfig.CREATOR);
                    break;
                case 5:
                    googleSignInConfig = (GoogleSignInConfig) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, GoogleSignInConfig.CREATOR);
                    break;
                case 6:
                    facebookSignInConfig = (FacebookSignInConfig) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, FacebookSignInConfig.CREATOR);
                    break;
                case 7:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new SignInConfiguration(iZzg, strZzp, strZzp2, emailSignInConfig, googleSignInConfig, facebookSignInConfig, strZzp3);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaJ, reason: merged with bridge method [inline-methods] */
    public SignInConfiguration[] newArray(int i) {
        return new SignInConfiguration[i];
    }
}
