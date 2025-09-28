package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzc implements Parcelable.Creator<GoogleSignInAccount> {
    static void zza(GoogleSignInAccount googleSignInAccount, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, googleSignInAccount.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, googleSignInAccount.getId(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, googleSignInAccount.getIdToken(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, googleSignInAccount.getEmail(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, googleSignInAccount.getDisplayName(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, (Parcelable) googleSignInAccount.zzlT(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, googleSignInAccount.zzlU(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, googleSignInAccount.zzlV());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzQ, reason: merged with bridge method [inline-methods] */
    public GoogleSignInAccount createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        String strZzp2 = null;
        String strZzp3 = null;
        String strZzp4 = null;
        Uri uri = null;
        String strZzp5 = null;
        int iZzg = 0;
        long jZzi = 0;
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
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 5:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 6:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, Uri.CREATOR);
                    break;
                case 7:
                    strZzp5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                case 8:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new GoogleSignInAccount(iZzg, strZzp, strZzp2, strZzp3, strZzp4, uri, strZzp5, jZzi);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaH, reason: merged with bridge method [inline-methods] */
    public GoogleSignInAccount[] newArray(int i) {
        return new GoogleSignInAccount[i];
    }
}
