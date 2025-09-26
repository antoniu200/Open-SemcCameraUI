package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AuthAccountRequest implements SafeParcelable {
    public static final Parcelable.Creator<AuthAccountRequest> CREATOR = new zzc();
    final int mVersionCode;
    final IBinder zzaeH;
    final Scope[] zzaeI;

    AuthAccountRequest(int i, IBinder iBinder, Scope[] scopeArr) {
        this.mVersionCode = i;
        this.zzaeH = iBinder;
        this.zzaeI = scopeArr;
    }

    public AuthAccountRequest(zzp zzpVar, Set<Scope> set) {
        this(1, zzpVar.asBinder(), (Scope[]) set.toArray(new Scope[set.size()]));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }
}
