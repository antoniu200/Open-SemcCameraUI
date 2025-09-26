// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class PlayLoggerContext implements SafeParcelable
{
    public static final zze CREATOR;
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
    
    static {
        CREATOR = new zze();
    }
    
    public PlayLoggerContext(final int versionCode, final String packageName, final int zzaRR, final int zzaRS, final String zzaRT, final String zzaRU, final boolean zzaRV, final String zzaRW, final boolean zzaRX, final int zzaRY) {
        this.versionCode = versionCode;
        this.packageName = packageName;
        this.zzaRR = zzaRR;
        this.zzaRS = zzaRS;
        this.zzaRT = zzaRT;
        this.zzaRU = zzaRU;
        this.zzaRV = zzaRV;
        this.zzaRW = zzaRW;
        this.zzaRX = zzaRX;
        this.zzaRY = zzaRY;
    }
    
    @Deprecated
    public PlayLoggerContext(final String s, final int zzaRR, final int zzaRS, final String zzaRT, final String zzaRU, final boolean zzaRV) {
        this.versionCode = 1;
        this.packageName = zzx.zzw(s);
        this.zzaRR = zzaRR;
        this.zzaRS = zzaRS;
        this.zzaRW = null;
        this.zzaRT = zzaRT;
        this.zzaRU = zzaRU;
        this.zzaRV = zzaRV;
        this.zzaRX = false;
        this.zzaRY = 0;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof PlayLoggerContext) {
            final PlayLoggerContext playLoggerContext = (PlayLoggerContext)o;
            return this.versionCode == playLoggerContext.versionCode && this.packageName.equals(playLoggerContext.packageName) && this.zzaRR == playLoggerContext.zzaRR && this.zzaRS == playLoggerContext.zzaRS && zzw.equal(this.zzaRW, playLoggerContext.zzaRW) && zzw.equal(this.zzaRT, playLoggerContext.zzaRT) && zzw.equal(this.zzaRU, playLoggerContext.zzaRU) && this.zzaRV == playLoggerContext.zzaRV && this.zzaRX == playLoggerContext.zzaRX && this.zzaRY == playLoggerContext.zzaRY;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return zzw.hashCode(this.versionCode, this.packageName, this.zzaRR, this.zzaRS, this.zzaRW, this.zzaRT, this.zzaRU, this.zzaRV, this.zzaRX, this.zzaRY);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PlayLoggerContext[");
        sb.append("versionCode=");
        sb.append(this.versionCode);
        sb.append(',');
        sb.append("package=");
        sb.append(this.packageName);
        sb.append(',');
        sb.append("packageVersionCode=");
        sb.append(this.zzaRR);
        sb.append(',');
        sb.append("logSource=");
        sb.append(this.zzaRS);
        sb.append(',');
        sb.append("logSourceName=");
        sb.append(this.zzaRW);
        sb.append(',');
        sb.append("uploadAccount=");
        sb.append(this.zzaRT);
        sb.append(',');
        sb.append("loggingId=");
        sb.append(this.zzaRU);
        sb.append(',');
        sb.append("logAndroidId=");
        sb.append(this.zzaRV);
        sb.append(',');
        sb.append("isAnonymous=");
        sb.append(this.zzaRX);
        sb.append(',');
        sb.append("qosTier=");
        sb.append(this.zzaRY);
        sb.append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zze.zza(this, parcel, n);
    }
}
