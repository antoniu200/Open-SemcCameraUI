// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.firstparty.shared;

import java.util.List;
import java.util.ArrayList;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<ScopeDetail>
{
    static void zza(final ScopeDetail scopeDetail, final Parcel parcel, final int n) {
        final int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, scopeDetail.version);
        zzb.zza(parcel, 2, scopeDetail.description, false);
        zzb.zza(parcel, 3, scopeDetail.zzTH, false);
        zzb.zza(parcel, 4, scopeDetail.zzTI, false);
        zzb.zza(parcel, 5, scopeDetail.zzTJ, false);
        zzb.zza(parcel, 6, scopeDetail.zzTK, false);
        zzb.zzb(parcel, 7, scopeDetail.zzTL, false);
        zzb.zza(parcel, 8, (Parcelable)scopeDetail.zzTM, n, false);
        zzb.zzI(parcel, zzaq);
    }
    
    public ScopeDetail zzV(final Parcel parcel) {
        final int zzap = zza.zzap(parcel);
        ArrayList<String> zzD = new ArrayList<String>();
        String zzp = null;
        Object zzp2 = null;
        Object zzp4;
        String zzp3 = (String)(zzp4 = zzp2);
        Object o;
        String zzp5 = (String)(o = zzp4);
        int zzg = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                default: {
                    zza.zzb(parcel, zzao);
                    continue;
                }
                case 8: {
                    o = zza.zza(parcel, zzao, (android.os.Parcelable$Creator<FACLData>)FACLData.CREATOR);
                    continue;
                }
                case 7: {
                    zzD = zza.zzD(parcel, zzao);
                    continue;
                }
                case 6: {
                    zzp5 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 5: {
                    zzp4 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 4: {
                    zzp3 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 3: {
                    zzp2 = zza.zzp(parcel, zzao);
                    continue;
                }
                case 2: {
                    zzp = zza.zzp(parcel, zzao);
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
        return new ScopeDetail(zzg, zzp, (String)zzp2, zzp3, (String)zzp4, zzp5, zzD, (FACLData)o);
    }
    
    public ScopeDetail[] zzaM(final int n) {
        return new ScopeDetail[n];
    }
}
