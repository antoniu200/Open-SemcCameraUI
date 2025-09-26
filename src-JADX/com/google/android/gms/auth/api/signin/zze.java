package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zze implements Parcelable.Creator<GoogleSignInConfig> {
    static void zza(GoogleSignInConfig googleSignInConfig, Parcel parcel, int i) {
        int iZzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, googleSignInConfig.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, googleSignInConfig.zzlS(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) googleSignInConfig.getAccount(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, googleSignInConfig.zzlY());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, googleSignInConfig.zzlZ());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, googleSignInConfig.zzma());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, googleSignInConfig.zzmb(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzaq);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzR, reason: merged with bridge method [inline-methods] */
    public GoogleSignInConfig createFromParcel(Parcel parcel) {
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        ArrayList arrayListZzc = null;
        Account account = null;
        String strZzp = null;
        int iZzg = 0;
        boolean zZzc = false;
        boolean zZzc2 = false;
        boolean zZzc3 = false;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzao);
                    break;
                case 2:
                    arrayListZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao, Scope.CREATOR);
                    break;
                case 3:
                    account = (Account) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzao, Account.CREATOR);
                    break;
                case 4:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 5:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 6:
                    zZzc3 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzao);
                    break;
                case 7:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzao);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzao);
                    break;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            return new GoogleSignInConfig(iZzg, (ArrayList<Scope>) arrayListZzc, account, zZzc, zZzc2, zZzc3, strZzp);
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaI, reason: merged with bridge method [inline-methods] */
    public GoogleSignInConfig[] newArray(int i) {
        return new GoogleSignInConfig[i];
    }
}
