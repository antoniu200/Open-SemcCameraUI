package com.google.android.gms.auth.api.proxy;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ProxyGrpcRequest implements SafeParcelable {
    public static final Parcelable.Creator<ProxyGrpcRequest> CREATOR = new zza();
    public final byte[] body;
    public final String hostname;
    public final String method;
    public final int port;
    public final long timeoutMillis;
    final int versionCode;

    ProxyGrpcRequest(int i, String str, int i2, long j, byte[] bArr, String str2) {
        this.versionCode = i;
        this.hostname = str;
        this.port = i2;
        this.timeoutMillis = j;
        this.body = bArr;
        this.method = str2;
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
