package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class IdToken implements SafeParcelable {
    public static final Parcelable.Creator<IdToken> CREATOR = new zzd();
    final int mVersionCode;
    private final String zzSk;
    private final String zzSs;

    IdToken(int i, String str, String str2) {
        this.mVersionCode = i;
        this.zzSk = str;
        this.zzSs = str2;
    }

    public IdToken(String str, String str2) {
        this(1, str, str2);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getAccountType() {
        return this.zzSk;
    }

    public String getIdToken() {
        return this.zzSs;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzd.zza(this, parcel, i);
    }
}
