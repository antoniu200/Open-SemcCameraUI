// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import android.os.Parcel;
import android.text.TextUtils;
import android.accounts.Account;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AccountChangeEventsRequest implements SafeParcelable
{
    public static final Parcelable$Creator<AccountChangeEventsRequest> CREATOR;
    final int mVersion;
    Account zzQd;
    @Deprecated
    String zzRs;
    int zzRu;
    
    static {
        CREATOR = (Parcelable$Creator)new zzb();
    }
    
    public AccountChangeEventsRequest() {
        this.mVersion = 1;
    }
    
    AccountChangeEventsRequest(final int mVersion, final int zzRu, final String zzRs, final Account zzQd) {
        this.mVersion = mVersion;
        this.zzRu = zzRu;
        this.zzRs = zzRs;
        if (zzQd == null && !TextUtils.isEmpty((CharSequence)zzRs)) {
            this.zzQd = new Account(zzRs, "com.google");
            return;
        }
        this.zzQd = zzQd;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Account getAccount() {
        return this.zzQd;
    }
    
    public String getAccountName() {
        return this.zzRs;
    }
    
    public int getEventIndex() {
        return this.zzRu;
    }
    
    public AccountChangeEventsRequest setAccount(final Account zzQd) {
        this.zzQd = zzQd;
        return this;
    }
    
    public AccountChangeEventsRequest setAccountName(final String zzRs) {
        this.zzRs = zzRs;
        return this;
    }
    
    public AccountChangeEventsRequest setEventIndex(final int zzRu) {
        this.zzRu = zzRu;
        return this;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
}
