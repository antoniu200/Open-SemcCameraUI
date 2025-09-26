// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.firstparty.shared;

import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<FACLData>
{
    static void zza(final FACLData faclData, final Parcel parcel, final int n) {
        final int zzaq = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, faclData.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable)faclData.zzTD, n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, faclData.zzTE, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, faclData.zzTF);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, faclData.zzTG, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, zzaq);
    }
    
    public FACLData zzU(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        int zzg = 0;
        boolean zzc = false;
        FACLConfig faclConfig = null;
        String zzp2;
        String zzp = zzp2 = null;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 5: {
                    zzp2 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 4: {
                    zzc = zza.zzc(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzp = zza.zzp(parcel, zzao);
                    continue;
                }
                case 2: {
                    faclConfig = zza.zza(parcel, zzao, (android.os.Parcelable$Creator<FACLConfig>)FACLConfig.CREATOR);
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
        return new FACLData(zzg, faclConfig, zzp, zzc, zzp2);
    }
    
    public FACLData[] zzaL(final int n) {
        return new FACLData[n];
    }
}
