// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<AccountChangeEvent>
{
    static void zza(final AccountChangeEvent accountChangeEvent, final Parcel parcel, int zzaq) {
        zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, accountChangeEvent.mVersion);
        zzb.zza(parcel, 2, accountChangeEvent.zzRr);
        zzb.zza(parcel, 3, accountChangeEvent.zzRs, false);
        zzb.zzc(parcel, 4, accountChangeEvent.zzRt);
        zzb.zzc(parcel, 5, accountChangeEvent.zzRu);
        zzb.zza(parcel, 6, accountChangeEvent.zzRv, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public AccountChangeEvent[] zzaq(final int n) {
        return new AccountChangeEvent[n];
    }
    
    public AccountChangeEvent zzz(final Parcel parcel) {
        final int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        String zzp = null;
        String zzp2 = null;
        int zzg = 0;
        int zzg3;
        int zzg2 = zzg3 = 0;
        long zzi = 0L;
        while (parcel.dataPosition() < zzap) {
            final int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    continue;
                }
                case 6: {
                    zzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    continue;
                }
                case 5: {
                    zzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    continue;
                }
                case 4: {
                    zzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, zzao);
                    continue;
                }
                case 2: {
                    zzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, zzao);
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
        return new AccountChangeEvent(zzg, zzi, zzp, zzg2, zzg3, zzp2);
    }
}
