// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzx;
import java.util.List;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AccountChangeEventsResponse implements SafeParcelable
{
    public static final Parcelable$Creator<AccountChangeEventsResponse> CREATOR;
    final int mVersion;
    final List<AccountChangeEvent> zzoQ;
    
    static {
        CREATOR = (Parcelable$Creator)new zzc();
    }
    
    AccountChangeEventsResponse(final int mVersion, final List<AccountChangeEvent> list) {
        this.mVersion = mVersion;
        this.zzoQ = zzx.zzw(list);
    }
    
    public AccountChangeEventsResponse(final List<AccountChangeEvent> list) {
        this.mVersion = 1;
        this.zzoQ = zzx.zzw(list);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public List<AccountChangeEvent> getEvents() {
        return this.zzoQ;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzc.zza(this, parcel, n);
    }
}
