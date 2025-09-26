// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zze;
import java.util.regex.Pattern;

public class zznb
{
    private static final Pattern zzaio;
    
    static {
        zzaio = Pattern.compile("\\$\\{(.*?)\\}");
    }
    
    public static boolean zzcA(final String s) {
        return s == null || zze.zzaeL.zzb(s);
    }
}
