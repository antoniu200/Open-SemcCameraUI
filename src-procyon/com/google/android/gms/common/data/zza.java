// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<BitmapTeleporter>
{
    static void zza(final BitmapTeleporter bitmapTeleporter, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, bitmapTeleporter.mVersionCode);
        zzb.zza(parcel, 2, (Parcelable)bitmapTeleporter.zzFc, n, false);
        zzb.zzc(parcel, 3, bitmapTeleporter.zzWJ);
        zzb.zzI(parcel, zzaq);
    }
    
    public BitmapTeleporter zzaf(final Parcel parcel) {
        final int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int zzg = 0;
        ParcelFileDescriptor parcelFileDescriptor = null;
        int zzg2 = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzao);
                    continue;
                }
                case 2: {
                    parcelFileDescriptor = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzao, (android.os.Parcelable$Creator<ParcelFileDescriptor>)ParcelFileDescriptor.CREATOR);
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
        return new BitmapTeleporter(zzg, parcelFileDescriptor, zzg2);
    }
    
    public BitmapTeleporter[] zzbq(final int n) {
        return new BitmapTeleporter[n];
    }
}
