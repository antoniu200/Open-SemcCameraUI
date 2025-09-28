package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ResolveAccountRequest implements SafeParcelable {
    public static final Parcelable.Creator<ResolveAccountRequest> CREATOR = new zzy();
    final int mVersionCode;
    private final Account zzQd;
    private final int zzagp;

    ResolveAccountRequest(int i, Account account, int i2) {
        this.mVersionCode = i;
        this.zzQd = account;
        this.zzagp = i2;
    }

    public ResolveAccountRequest(Account account, int i) {
        this(1, account, i);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzQd;
    }

    public int getSessionId() {
        return this.zzagp;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzy.zza(this, parcel, i);
    }
}
