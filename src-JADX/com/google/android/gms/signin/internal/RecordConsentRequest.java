package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RecordConsentRequest implements SafeParcelable {
    public static final Parcelable.Creator<RecordConsentRequest> CREATOR = new zzg();
    final int mVersionCode;
    private final Account zzQd;
    private final String zzTl;
    private final Scope[] zzaVk;

    RecordConsentRequest(int i, Account account, Scope[] scopeArr, String str) {
        this.mVersionCode = i;
        this.zzQd = account;
        this.zzaVk = scopeArr;
        this.zzTl = str;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzQd;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzg.zza(this, parcel, i);
    }

    public Scope[] zzCj() {
        return this.zzaVk;
    }

    public String zzmb() {
        return this.zzTl;
    }
}
