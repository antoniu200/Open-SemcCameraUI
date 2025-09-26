// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin;

import java.util.ArrayList;
import android.accounts.Account;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import java.util.List;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zze implements Parcelable$Creator<GoogleSignInConfig>
{
    static void zza(final GoogleSignInConfig googleSignInConfig, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, googleSignInConfig.versionCode);
        zzb.zzc(parcel, 2, googleSignInConfig.zzlS(), false);
        zzb.zza(parcel, 3, (Parcelable)googleSignInConfig.getAccount(), n, false);
        zzb.zza(parcel, 4, googleSignInConfig.zzlY());
        zzb.zza(parcel, 5, googleSignInConfig.zzlZ());
        zzb.zza(parcel, 6, googleSignInConfig.zzma());
        zzb.zza(parcel, 7, googleSignInConfig.zzmb(), false);
        zzb.zzI(parcel, zzaq);
    }
    
    public GoogleSignInConfig zzR(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        ArrayList<Scope> zzc = null;
        String zzp;
        Object o = zzp = null;
        int zzg = 0;
        int zzc2 = 0;
        int zzc4;
        int zzc3 = zzc4 = zzc2;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 7: {
                    zzp = zza.zzp(parcel, zzao);
                    continue;
                }
                case 6: {
                    zzc4 = (zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 5: {
                    zzc3 = (zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 4: {
                    zzc2 = (zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 3: {
                    o = zza.zza(parcel, zzao, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                    continue;
                }
                case 2: {
                    zzc = zza.zzc(parcel, zzao, Scope.CREATOR);
                    continue;
                }
                case 1: {
                    zzg = zza.zzg(parcel, zzao);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Overread allowed size end=");
            sb.append(zzap);
            throw new zza.zza(sb.toString(), parcel);
        }
        return new GoogleSignInConfig(zzg, zzc, (Account)o, (boolean)(zzc2 != 0), (boolean)(zzc3 != 0), (boolean)(zzc4 != 0), zzp);
    }
    
    public GoogleSignInConfig[] zzaI(final int n) {
        return new GoogleSignInConfig[n];
    }
}
