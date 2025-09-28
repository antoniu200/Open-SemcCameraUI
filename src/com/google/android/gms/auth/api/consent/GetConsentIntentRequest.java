package com.google.android.gms.auth.api.consent;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.firstparty.shared.ScopeDetail;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GetConsentIntentRequest implements SafeParcelable {
    public static final Parcelable.Creator<GetConsentIntentRequest> CREATOR = new zzb();
    private final int mVersionCode;
    private final Account zzQd;
    private final String zzSb;
    private final int zzSc;
    private final String zzSd;
    final ScopeDetail[] zzSe;
    private final boolean zzSf;
    private final int zzSg;

    GetConsentIntentRequest(int i, String str, int i2, String str2, Account account, ScopeDetail[] scopeDetailArr, boolean z, int i3) {
        this.mVersionCode = i;
        this.zzSb = str;
        this.zzSc = i2;
        this.zzSd = str2;
        this.zzQd = (Account) zzx.zzw(account);
        this.zzSe = scopeDetailArr;
        this.zzSf = z;
        this.zzSg = i3;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzQd;
    }

    public String getCallingPackage() {
        return this.zzSb;
    }

    public int getCallingUid() {
        return this.zzSc;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzb.zza(this, parcel, i);
    }

    public String zzlF() {
        return this.zzSd;
    }

    public boolean zzlG() {
        return this.zzSf;
    }

    public int zzlH() {
        return this.zzSg;
    }
}
