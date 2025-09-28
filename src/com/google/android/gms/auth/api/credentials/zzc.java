package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzc implements Parcelable.Creator<CredentialRequest> {
    static void zza(CredentialRequest credentialRequest, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, credentialRequest.getSupportsPasswordLogin());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, credentialRequest.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, credentialRequest.getAccountTypes(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) credentialRequest.getCredentialPickerConfig(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable) credentialRequest.getCredentialHintPickerConfig(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzG, reason: merged with bridge method [inline-methods] */
    public CredentialRequest createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int iZzg = 0;
        boolean zZzc = false;
        String[] strArrZzB = null;
        CredentialPickerConfig credentialPickerConfig = null;
        CredentialPickerConfig credentialPickerConfig2 = null;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            int iZzbM = com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao);
            if (iZzbM != 1000) {
                switch (iZzbM) {
                    case 1:
                        zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                        break;
                    case 2:
                        strArrZzB = com.google.android.gms.common.internal.safeparcel.zza.zzB(parcel, iZzao);
                        break;
                    case 3:
                        credentialPickerConfig = (CredentialPickerConfig) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, CredentialPickerConfig.CREATOR);
                        break;
                    case 4:
                        credentialPickerConfig2 = (CredentialPickerConfig) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, CredentialPickerConfig.CREATOR);
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
            return new CredentialRequest(iZzg, zZzc, strArrZzB, credentialPickerConfig, credentialPickerConfig2);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzax, reason: merged with bridge method [inline-methods] */
    public CredentialRequest[] newArray(int i) {
        return new CredentialRequest[i];
    }
}
