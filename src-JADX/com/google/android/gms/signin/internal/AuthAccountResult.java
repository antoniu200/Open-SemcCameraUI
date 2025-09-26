package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AuthAccountResult implements SafeParcelable {
    public static final Parcelable.Creator<AuthAccountResult> CREATOR = new zza();
    final int mVersionCode;

    public AuthAccountResult() {
        this(1);
    }

    AuthAccountResult(int i) {
        this.mVersionCode = i;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }
}
