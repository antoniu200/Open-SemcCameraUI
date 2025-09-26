// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.util.Base64;

public final class zzmk
{
    public static String zzi(final byte[] array) {
        if (array == null) {
            return null;
        }
        return Base64.encodeToString(array, 0);
    }
    
    public static String zzj(final byte[] array) {
        if (array == null) {
            return null;
        }
        return Base64.encodeToString(array, 10);
    }
}
