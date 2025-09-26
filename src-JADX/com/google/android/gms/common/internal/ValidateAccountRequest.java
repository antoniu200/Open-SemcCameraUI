package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ValidateAccountRequest implements SafeParcelable {
    public static final Parcelable.Creator<ValidateAccountRequest> CREATOR = new zzad();
    final int mVersionCode;
    private final String zzSb;
    final IBinder zzaeH;
    private final Scope[] zzaeI;
    private final int zzagu;
    private final Bundle zzagv;

    ValidateAccountRequest(int i, int i2, IBinder iBinder, Scope[] scopeArr, Bundle bundle, String str) {
        this.mVersionCode = i;
        this.zzagu = i2;
        this.zzaeH = iBinder;
        this.zzaeI = scopeArr;
        this.zzagv = bundle;
        this.zzSb = str;
    }

    public ValidateAccountRequest(zzp zzpVar, Scope[] scopeArr, String str, Bundle bundle) {
        this(1, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE, zzpVar == null ? null : zzpVar.asBinder(), scopeArr, bundle, str);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getCallingPackage() {
        return this.zzSb;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzad.zza(this, parcel, i);
    }

    public int zzpu() {
        return this.zzagu;
    }

    public Scope[] zzpv() {
        return this.zzaeI;
    }

    public Bundle zzpw() {
        return this.zzagv;
    }
}
