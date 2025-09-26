package com.google.android.gms.common.server;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FavaDiagnosticsEntity implements SafeParcelable {
    public static final zza CREATOR = new zza();
    final int mVersionCode;
    public final String zzagM;
    public final int zzagN;

    public FavaDiagnosticsEntity(int i, String str, int i2) {
        this.mVersionCode = i;
        this.zzagM = str;
        this.zzagN = i2;
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
