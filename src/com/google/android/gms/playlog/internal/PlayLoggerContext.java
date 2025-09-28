package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PlayLoggerContext implements SafeParcelable {
    public static final zze CREATOR = new zze();
    public final String packageName;
    public final int versionCode;
    public final int zzaRR;
    public final int zzaRS;
    public final String zzaRT;
    public final String zzaRU;
    public final boolean zzaRV;
    public final String zzaRW;
    public final boolean zzaRX;
    public final int zzaRY;

    public PlayLoggerContext(int i, String str, int i2, int i3, String str2, String str3, boolean z, String str4, boolean z2, int i4) {
        this.versionCode = i;
        this.packageName = str;
        this.zzaRR = i2;
        this.zzaRS = i3;
        this.zzaRT = str2;
        this.zzaRU = str3;
        this.zzaRV = z;
        this.zzaRW = str4;
        this.zzaRX = z2;
        this.zzaRY = i4;
    }

    @Deprecated
    public PlayLoggerContext(String str, int i, int i2, String str2, String str3, boolean z) {
        this.versionCode = 1;
        this.packageName = (String) zzx.zzw(str);
        this.zzaRR = i;
        this.zzaRS = i2;
        this.zzaRW = null;
        this.zzaRT = str2;
        this.zzaRU = str3;
        this.zzaRV = z;
        this.zzaRX = false;
        this.zzaRY = 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PlayLoggerContext)) {
            return false;
        }
        PlayLoggerContext playLoggerContext = (PlayLoggerContext) obj;
        return this.versionCode == playLoggerContext.versionCode && this.packageName.equals(playLoggerContext.packageName) && this.zzaRR == playLoggerContext.zzaRR && this.zzaRS == playLoggerContext.zzaRS && zzw.equal(this.zzaRW, playLoggerContext.zzaRW) && zzw.equal(this.zzaRT, playLoggerContext.zzaRT) && zzw.equal(this.zzaRU, playLoggerContext.zzaRU) && this.zzaRV == playLoggerContext.zzaRV && this.zzaRX == playLoggerContext.zzaRX && this.zzaRY == playLoggerContext.zzaRY;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.versionCode), this.packageName, Integer.valueOf(this.zzaRR), Integer.valueOf(this.zzaRS), this.zzaRW, this.zzaRT, this.zzaRU, Boolean.valueOf(this.zzaRV), Boolean.valueOf(this.zzaRX), Integer.valueOf(this.zzaRY));
    }

    public String toString() {
        return "PlayLoggerContext[versionCode=" + this.versionCode + ",package=" + this.packageName + ",packageVersionCode=" + this.zzaRR + ",logSource=" + this.zzaRS + ",logSourceName=" + this.zzaRW + ",uploadAccount=" + this.zzaRT + ",loggingId=" + this.zzaRU + ",logAndroidId=" + this.zzaRV + ",isAnonymous=" + this.zzaRX + ",qosTier=" + this.zzaRY + "]";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }
}
