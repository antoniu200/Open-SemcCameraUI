// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.stats;

public abstract class zzf
{
    public static int zzahY = 0;
    public static int zzahZ = 1;
    
    public abstract int getEventType();
    
    public abstract long getTimeMillis();
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getTimeMillis());
        sb.append("\t");
        sb.append(this.getEventType());
        sb.append("\t");
        sb.append(this.zzqd());
        sb.append(this.zzqg());
        return sb.toString();
    }
    
    public abstract long zzqd();
    
    public abstract String zzqg();
}
