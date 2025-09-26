// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.stats;

import android.text.TextUtils;
import android.os.Parcel;
import java.util.List;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class WakeLockEvent extends zzf implements SafeParcelable
{
    public static final Parcelable$Creator<WakeLockEvent> CREATOR;
    private final long mTimeout;
    final int mVersionCode;
    private final long zzahn;
    private int zzaho;
    private final long zzahv;
    private long zzahx;
    private final String zzaia;
    private final int zzaib;
    private final List<String> zzaic;
    private final String zzaid;
    private int zzaie;
    private final String zzaif;
    private final String zzaig;
    private final float zzaih;
    
    static {
        CREATOR = (Parcelable$Creator)new zzh();
    }
    
    WakeLockEvent(final int mVersionCode, final long zzahn, final int zzaho, final String zzaia, final int zzaib, final List<String> zzaic, final String zzaid, final long zzahv, final int zzaie, final String zzaif, final String zzaig, final float zzaih, final long mTimeout) {
        this.mVersionCode = mVersionCode;
        this.zzahn = zzahn;
        this.zzaho = zzaho;
        this.zzaia = zzaia;
        this.zzaif = zzaif;
        this.zzaib = zzaib;
        this.zzahx = -1L;
        this.zzaic = zzaic;
        this.zzaid = zzaid;
        this.zzahv = zzahv;
        this.zzaie = zzaie;
        this.zzaig = zzaig;
        this.zzaih = zzaih;
        this.mTimeout = mTimeout;
    }
    
    public WakeLockEvent(final long n, final int n2, final String s, final int n3, final List<String> list, final String s2, final long n4, final int n5, final String s3, final String s4, final float n6, final long n7) {
        this(1, n, n2, s, n3, list, s2, n4, n5, s3, s4, n6, n7);
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public int getEventType() {
        return this.zzaho;
    }
    
    @Override
    public long getTimeMillis() {
        return this.zzahn;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzh.zza(this, parcel, n);
    }
    
    public String zzqc() {
        return this.zzaid;
    }
    
    @Override
    public long zzqd() {
        return this.zzahx;
    }
    
    public long zzqf() {
        return this.zzahv;
    }
    
    @Override
    public String zzqg() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\t");
        sb.append(this.zzqj());
        sb.append("\t");
        sb.append(this.zzql());
        sb.append("\t");
        String join;
        if (this.zzqm() == null) {
            join = "";
        }
        else {
            join = TextUtils.join((CharSequence)",", (Iterable)this.zzqm());
        }
        sb.append(join);
        sb.append("\t");
        sb.append(this.zzqn());
        sb.append("\t");
        String zzqk;
        if (this.zzqk() == null) {
            zzqk = "";
        }
        else {
            zzqk = this.zzqk();
        }
        sb.append(zzqk);
        sb.append("\t");
        String zzqo;
        if (this.zzqo() == null) {
            zzqo = "";
        }
        else {
            zzqo = this.zzqo();
        }
        sb.append(zzqo);
        sb.append("\t");
        sb.append(this.zzqp());
        return sb.toString();
    }
    
    public String zzqj() {
        return this.zzaia;
    }
    
    public String zzqk() {
        return this.zzaif;
    }
    
    public int zzql() {
        return this.zzaib;
    }
    
    public List<String> zzqm() {
        return this.zzaic;
    }
    
    public int zzqn() {
        return this.zzaie;
    }
    
    public String zzqo() {
        return this.zzaig;
    }
    
    public float zzqp() {
        return this.zzaih;
    }
    
    public long zzqq() {
        return this.mTimeout;
    }
}
