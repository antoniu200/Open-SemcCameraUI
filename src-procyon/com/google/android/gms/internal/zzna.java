// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Set;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.api.Scope;

public final class zzna
{
    public static String[] zza(final Scope[] array) {
        zzx.zzb(array, "scopes can't be null.");
        final String[] array2 = new String[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i].zznG();
        }
        return array2;
    }
    
    public static String[] zzc(final Set<Scope> set) {
        zzx.zzb(set, "scopes can't be null.");
        return zza(set.toArray(new Scope[set.size()]));
    }
}
