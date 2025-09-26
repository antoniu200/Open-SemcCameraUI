package com.google.android.gms.playlog.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LogEvent implements SafeParcelable {
    public static final zzc CREATOR = new zzc();
    public final String tag;
    public final int versionCode;
    public final long zzaRG;
    public final long zzaRH;
    public final byte[] zzaRI;
    public final Bundle zzaRJ;

    LogEvent(int i, long j, long j2, String str, byte[] bArr, Bundle bundle) {
        this.versionCode = i;
        this.zzaRG = j;
        this.zzaRH = j2;
        this.tag = str;
        this.zzaRI = bArr;
        this.zzaRJ = bundle;
    }

    public LogEvent(long j, long j2, String str, byte[] bArr, String... strArr) {
        this.versionCode = 1;
        this.zzaRG = j;
        this.zzaRH = j2;
        this.tag = str;
        this.zzaRI = bArr;
        this.zzaRJ = zzd(strArr);
    }

    private static Bundle zzd(String... strArr) {
        if (strArr == null) {
            return null;
        }
        if (strArr.length % 2 != 0) {
            throw new IllegalArgumentException("extras must have an even number of elements");
        }
        int length = strArr.length / 2;
        if (length == 0) {
            return null;
        }
        Bundle bundle = new Bundle(length);
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bundle.putString(strArr[i2], strArr[i2 + 1]);
        }
        return bundle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("tag=");
        sb.append(this.tag);
        sb.append(",");
        sb.append("eventTime=");
        sb.append(this.zzaRG);
        sb.append(",");
        sb.append("eventUptime=");
        sb.append(this.zzaRH);
        sb.append(",");
        if (this.zzaRJ != null && !this.zzaRJ.isEmpty()) {
            sb.append("keyValues=");
            for (String str : this.zzaRJ.keySet()) {
                sb.append("(");
                sb.append(str);
                sb.append(",");
                sb.append(this.zzaRJ.getString(str));
                sb.append(")");
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }
}
