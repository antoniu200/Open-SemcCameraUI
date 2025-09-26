// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import java.util.Iterator;

public class zzv
{
    private final String separator;
    
    private zzv(final String separator) {
        this.separator = separator;
    }
    
    public static zzv zzcq(final String s) {
        return new zzv(s);
    }
    
    public final String zza(final Iterable<?> iterable) {
        return this.zza(new StringBuilder(), iterable).toString();
    }
    
    public final StringBuilder zza(final StringBuilder sb, final Iterable<?> iterable) {
        final Iterator<?> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            while (true) {
                sb.append(this.zzu(iterator.next()));
                if (!iterator.hasNext()) {
                    break;
                }
                sb.append(this.separator);
            }
        }
        return sb;
    }
    
    CharSequence zzu(final Object o) {
        if (o instanceof CharSequence) {
            return (CharSequence)o;
        }
        return o.toString();
    }
}
