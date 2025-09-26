// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.firstparty.shared;

import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<FACLConfig>
{
    static void zza(final FACLConfig faclConfig, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, faclConfig.version);
        zzb.zza(parcel, 2, faclConfig.zzTx);
        zzb.zza(parcel, 3, faclConfig.zzTy, false);
        zzb.zza(parcel, 4, faclConfig.zzTz);
        zzb.zza(parcel, 5, faclConfig.zzTA);
        zzb.zza(parcel, 6, faclConfig.zzTB);
        zzb.zza(parcel, 7, faclConfig.zzTC);
        zzb.zzI(parcel, zzaq);
    }
    
    public FACLConfig zzT(final Parcel parcel) {
        final int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int zzg = 0;
        int zzc = 0;
        int zzc3;
        int zzc2 = zzc3 = zzc;
        int zzc5;
        int zzc4 = zzc5 = zzc3;
        String zzp = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    continue;
                }
                case 7: {
                    zzc5 = (com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 6: {
                    zzc4 = (com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 5: {
                    zzc3 = (com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 4: {
                    zzc2 = (com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao) ? 1 : 0);
                    continue;
                }
                case 3: {
                    zzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    continue;
                }
                case 2: {
                    zzc = (com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzao) ? 1 : 0);
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
        return new FACLConfig(zzg, (boolean)(zzc != 0), zzp, (boolean)(zzc2 != 0), (boolean)(zzc3 != 0), (boolean)(zzc4 != 0), (boolean)(zzc5 != 0));
    }
    
    public FACLConfig[] zzaK(final int n) {
        return new FACLConfig[n];
    }
}
