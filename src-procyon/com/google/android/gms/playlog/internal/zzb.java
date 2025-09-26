// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.playlog.internal;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzsi;
import java.util.ArrayList;

public class zzb
{
    private final ArrayList<zza> zzaRK;
    private int zzaRL;
    
    public zzb() {
        this(100);
    }
    
    public zzb(final int zzaRL) {
        this.zzaRK = new ArrayList<zza>();
        this.zzaRL = zzaRL;
    }
    
    private void zzBu() {
        while (this.getSize() > this.getCapacity()) {
            this.zzaRK.remove(0);
        }
    }
    
    public void clear() {
        this.zzaRK.clear();
    }
    
    public int getCapacity() {
        return this.zzaRL;
    }
    
    public int getSize() {
        return this.zzaRK.size();
    }
    
    public boolean isEmpty() {
        return this.zzaRK.isEmpty();
    }
    
    public ArrayList<zza> zzBt() {
        return this.zzaRK;
    }
    
    public void zza(final PlayLoggerContext playLoggerContext, final LogEvent logEvent) {
        this.zzaRK.add(new zza(playLoggerContext, logEvent));
        this.zzBu();
    }
    
    public static class zza
    {
        public final PlayLoggerContext zzaRM;
        public final LogEvent zzaRN;
        public final zzsi.zzd zzaRO;
        
        private zza(final PlayLoggerContext playLoggerContext, final LogEvent logEvent) {
            this.zzaRM = zzx.zzw(playLoggerContext);
            this.zzaRN = zzx.zzw(logEvent);
            this.zzaRO = null;
        }
    }
}
