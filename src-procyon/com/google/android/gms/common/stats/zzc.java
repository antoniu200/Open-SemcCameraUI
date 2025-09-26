// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.stats;

import com.google.android.gms.internal.zzlr;

public final class zzc
{
    public static zzlr<Integer> zzahG;
    
    static {
        zzc.zzahG = zzlr.zza("gms:common:stats:max_num_of_events", 100);
    }
    
    public static final class zza
    {
        public static zzlr<Integer> zzahH;
        public static zzlr<String> zzahI;
        public static zzlr<String> zzahJ;
        public static zzlr<String> zzahK;
        public static zzlr<String> zzahL;
        public static zzlr<Long> zzahM;
        
        static {
            zza.zzahH = zzlr.zza("gms:common:stats:connections:level", zzd.LOG_LEVEL_OFF);
            zza.zzahI = zzlr.zzu("gms:common:stats:connections:ignored_calling_processes", "");
            zza.zzahJ = zzlr.zzu("gms:common:stats:connections:ignored_calling_services", "");
            zza.zzahK = zzlr.zzu("gms:common:stats:connections:ignored_target_processes", "");
            zza.zzahL = zzlr.zzu("gms:common:stats:connections:ignored_target_services", "com.google.android.gms.auth.GetToken");
            zza.zzahM = zzlr.zza("gms:common:stats:connections:time_out_duration", 600000L);
        }
    }
    
    public static final class zzb
    {
        public static zzlr<Integer> zzahH;
        public static zzlr<Long> zzahM;
        
        static {
            zzb.zzahH = zzlr.zza("gms:common:stats:wakeLocks:level", zzd.LOG_LEVEL_OFF);
            zzb.zzahM = zzlr.zza("gms:common:stats:wakelocks:time_out_duration", 600000L);
        }
    }
}
