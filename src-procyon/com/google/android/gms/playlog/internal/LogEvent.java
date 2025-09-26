// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import java.util.Iterator;
import android.os.Bundle;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class LogEvent implements SafeParcelable
{
    public static final zzc CREATOR;
    public final String tag;
    public final int versionCode;
    public final long zzaRG;
    public final long zzaRH;
    public final byte[] zzaRI;
    public final Bundle zzaRJ;
    
    static {
        CREATOR = new zzc();
    }
    
    LogEvent(final int versionCode, final long zzaRG, final long zzaRH, final String tag, final byte[] zzaRI, final Bundle zzaRJ) {
        this.versionCode = versionCode;
        this.zzaRG = zzaRG;
        this.zzaRH = zzaRH;
        this.tag = tag;
        this.zzaRI = zzaRI;
        this.zzaRJ = zzaRJ;
    }
    
    public LogEvent(final long zzaRG, final long zzaRH, final String tag, final byte[] zzaRI, final String... array) {
        this.versionCode = 1;
        this.zzaRG = zzaRG;
        this.zzaRH = zzaRH;
        this.tag = tag;
        this.zzaRI = zzaRI;
        this.zzaRJ = zzd(array);
    }
    
    private static Bundle zzd(final String... array) {
        if (array == null) {
            return null;
        }
        if (array.length % 2 != 0) {
            throw new IllegalArgumentException("extras must have an even number of elements");
        }
        final int n = array.length / 2;
        if (n == 0) {
            return null;
        }
        final Bundle bundle = new Bundle(n);
        for (int i = 0; i < n; ++i) {
            final int n2 = i * 2;
            bundle.putString(array[n2], array[n2 + 1]);
        }
        return bundle;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
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
            for (final String str : this.zzaRJ.keySet()) {
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
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzc.zza(this, parcel, n);
    }
}
