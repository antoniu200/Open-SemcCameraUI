// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AccountChangeEvent implements SafeParcelable
{
    public static final Parcelable$Creator<AccountChangeEvent> CREATOR;
    final int mVersion;
    final long zzRr;
    final String zzRs;
    final int zzRt;
    final int zzRu;
    final String zzRv;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    AccountChangeEvent(final int mVersion, final long zzRr, final String s, final int zzRt, final int zzRu, final String zzRv) {
        this.mVersion = mVersion;
        this.zzRr = zzRr;
        this.zzRs = zzx.zzw(s);
        this.zzRt = zzRt;
        this.zzRu = zzRu;
        this.zzRv = zzRv;
    }
    
    public AccountChangeEvent(final long zzRr, final String s, final int zzRt, final int zzRu, final String zzRv) {
        this.mVersion = 1;
        this.zzRr = zzRr;
        this.zzRs = zzx.zzw(s);
        this.zzRt = zzRt;
        this.zzRu = zzRu;
        this.zzRv = zzRv;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AccountChangeEvent) {
            final AccountChangeEvent accountChangeEvent = (AccountChangeEvent)o;
            return this.mVersion == accountChangeEvent.mVersion && this.zzRr == accountChangeEvent.zzRr && zzw.equal(this.zzRs, accountChangeEvent.zzRs) && this.zzRt == accountChangeEvent.zzRt && this.zzRu == accountChangeEvent.zzRu && zzw.equal(this.zzRv, accountChangeEvent.zzRv);
        }
        return false;
    }
    
    public String getAccountName() {
        return this.zzRs;
    }
    
    public String getChangeData() {
        return this.zzRv;
    }
    
    public int getChangeType() {
        return this.zzRt;
    }
    
    public int getEventIndex() {
        return this.zzRu;
    }
    
    @Override
    public int hashCode() {
        return zzw.hashCode(this.mVersion, this.zzRr, this.zzRs, this.zzRt, this.zzRu, this.zzRv);
    }
    
    @Override
    public String toString() {
        String str = "UNKNOWN";
        switch (this.zzRt) {
            case 4: {
                str = "RENAMED_TO";
                break;
            }
            case 3: {
                str = "RENAMED_FROM";
                break;
            }
            case 2: {
                str = "REMOVED";
                break;
            }
            case 1: {
                str = "ADDED";
                break;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("AccountChangeEvent {accountName = ");
        sb.append(this.zzRs);
        sb.append(", changeType = ");
        sb.append(str);
        sb.append(", changeData = ");
        sb.append(this.zzRv);
        sb.append(", eventIndex = ");
        sb.append(this.zzRu);
        sb.append("}");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
}
