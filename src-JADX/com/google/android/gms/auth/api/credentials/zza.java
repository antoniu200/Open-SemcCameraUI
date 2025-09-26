package com.google.android.gms.auth.api.credentials;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zza implements Parcelable.Creator<Credential> {
    static void zza(Credential credential, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, credential.getId(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, credential.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, credential.getName(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) credential.getProfilePictureUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, credential.getIdTokens(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, credential.getPassword(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, credential.getAccountType(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, credential.getGeneratedPassword(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, credential.zzlI(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzE, reason: merged with bridge method [inline-methods] */
    public Credential createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String strZzp = null;
        String strZzp2 = null;
        Uri uri = null;
        ArrayList arrayListZzc = null;
        String strZzp3 = null;
        String strZzp4 = null;
        String strZzp5 = null;
        String strZzp6 = null;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int iZzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao);
            if (iZzbM != 1000) {
                switch (iZzbM) {
                    case 1:
                        strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    case 2:
                        strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    case 3:
                        uri = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, Uri.CREATOR);
                        break;
                    case 4:
                        arrayListZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao, IdToken.CREATOR);
                        break;
                    case 5:
                        strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    case 6:
                        strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    case 7:
                        strZzp5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    case 8:
                        strZzp6 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                        break;
                    default:
                        com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                        break;
                }
            } else {
                iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new Credential(iZzg, strZzp, strZzp2, uri, arrayListZzc, strZzp3, strZzp4, strZzp5, strZzp6);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzav, reason: merged with bridge method [inline-methods] */
    public Credential[] newArray(int i) {
        return new Credential[i];
    }
}
