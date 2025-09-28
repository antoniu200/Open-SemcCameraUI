package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzp;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ResolveAccountResponse implements SafeParcelable {
    public static final Parcelable.Creator<ResolveAccountResponse> CREATOR = new zzz();
    final int mVersionCode;
    private boolean zzabG;
    IBinder zzaeH;
    private ConnectionResult zzagq;
    private boolean zzagr;

    public ResolveAccountResponse(int i) {
        this(new ConnectionResult(i, null));
    }

    ResolveAccountResponse(int i, IBinder iBinder, ConnectionResult connectionResult, boolean z, boolean z2) {
        this.mVersionCode = i;
        this.zzaeH = iBinder;
        this.zzagq = connectionResult;
        this.zzabG = z;
        this.zzagr = z2;
    }

    public ResolveAccountResponse(ConnectionResult connectionResult) {
        this(1, null, connectionResult, false, false);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ResolveAccountResponse)) {
            return false;
        }
        ResolveAccountResponse resolveAccountResponse = (ResolveAccountResponse) obj;
        return this.zzagq.equals(resolveAccountResponse.zzagq) && zzpq().equals(resolveAccountResponse.zzpq());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzz.zza(this, parcel, i);
    }

    public zzp zzpq() {
        return zzp.zza.zzaH(this.zzaeH);
    }

    public ConnectionResult zzpr() {
        return this.zzagq;
    }

    public boolean zzps() {
        return this.zzabG;
    }

    public boolean zzpt() {
        return this.zzagr;
    }
}
