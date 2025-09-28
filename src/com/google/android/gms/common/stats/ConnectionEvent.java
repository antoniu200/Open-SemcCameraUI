package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ConnectionEvent extends zzf implements SafeParcelable {
    public static final Parcelable.Creator<ConnectionEvent> CREATOR = new zza();
    final int mVersionCode;
    private final long zzahn;
    private int zzaho;
    private final String zzahp;
    private final String zzahq;
    private final String zzahr;
    private final String zzahs;
    private final String zzaht;
    private final String zzahu;
    private final long zzahv;
    private final long zzahw;
    private long zzahx;

    ConnectionEvent(int i, long j, int i2, String str, String str2, String str3, String str4, String str5, String str6, long j2, long j3) {
        this.mVersionCode = i;
        this.zzahn = j;
        this.zzaho = i2;
        this.zzahp = str;
        this.zzahq = str2;
        this.zzahr = str3;
        this.zzahs = str4;
        this.zzahx = -1L;
        this.zzaht = str5;
        this.zzahu = str6;
        this.zzahv = j2;
        this.zzahw = j3;
    }

    public ConnectionEvent(long j, int i, String str, String str2, String str3, String str4, String str5, String str6, long j2, long j3) {
        this(1, j, i, str, str2, str3, str4, str5, str6, j2, j3);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public int getEventType() {
        return this.zzaho;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public long getTimeMillis() {
        return this.zzahn;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }

    public String zzpX() {
        return this.zzahp;
    }

    public String zzpY() {
        return this.zzahq;
    }

    public String zzpZ() {
        return this.zzahr;
    }

    public String zzqa() {
        return this.zzahs;
    }

    public String zzqb() {
        return this.zzaht;
    }

    public String zzqc() {
        return this.zzahu;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public long zzqd() {
        return this.zzahx;
    }

    public long zzqe() {
        return this.zzahw;
    }

    public long zzqf() {
        return this.zzahv;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public String zzqg() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t");
        sb.append(zzpX());
        sb.append(SharedPrefsTranslator.CONNECTOR_SLASH);
        sb.append(zzpY());
        sb.append("\t");
        sb.append(zzpZ());
        sb.append(SharedPrefsTranslator.CONNECTOR_SLASH);
        sb.append(zzqa());
        sb.append("\t");
        sb.append(this.zzaht == null ? "" : this.zzaht);
        sb.append("\t");
        sb.append(zzqe());
        return sb.toString();
    }
}
