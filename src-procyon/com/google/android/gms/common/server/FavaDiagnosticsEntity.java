// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class FavaDiagnosticsEntity implements SafeParcelable
{
    public static final zza CREATOR;
    final int mVersionCode;
    public final String zzagM;
    public final int zzagN;
    
    static {
        CREATOR = new zza();
    }
    
    public FavaDiagnosticsEntity(final int mVersionCode, final String zzagM, final int zzagN) {
        this.mVersionCode = mVersionCode;
        this.zzagM = zzagM;
        this.zzagN = zzagN;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
}
