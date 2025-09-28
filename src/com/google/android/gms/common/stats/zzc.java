package com.google.android.gms.common.stats;

import com.google.android.gms.internal.zzlr;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzc {
    public static zzlr<Integer> zzahG = zzlr.zza("gms:common:stats:max_num_of_events", (Integer) 100);

    public static final class zza {
        public static zzlr<Integer> zzahH = zzlr.zza("gms:common:stats:connections:level", Integer.valueOf(zzd.LOG_LEVEL_OFF));
        public static zzlr<String> zzahI = zzlr.zzu("gms:common:stats:connections:ignored_calling_processes", "");
        public static zzlr<String> zzahJ = zzlr.zzu("gms:common:stats:connections:ignored_calling_services", "");
        public static zzlr<String> zzahK = zzlr.zzu("gms:common:stats:connections:ignored_target_processes", "");
        public static zzlr<String> zzahL = zzlr.zzu("gms:common:stats:connections:ignored_target_services", "com.google.android.gms.auth.GetToken");
        public static zzlr<Long> zzahM = zzlr.zza("gms:common:stats:connections:time_out_duration", (Long) 600000L);
    }

    public static final class zzb {
        public static zzlr<Integer> zzahH = zzlr.zza("gms:common:stats:wakeLocks:level", Integer.valueOf(zzd.LOG_LEVEL_OFF));
        public static zzlr<Long> zzahM = zzlr.zza("gms:common:stats:wakelocks:time_out_duration", (Long) 600000L);
    }
}
