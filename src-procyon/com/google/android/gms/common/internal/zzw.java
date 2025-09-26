// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public final class zzw
{
    public static boolean equal(final Object o, final Object obj) {
        return o == obj || (o != null && o.equals(obj));
    }
    
    public static int hashCode(final Object... a) {
        return Arrays.hashCode(a);
    }
    
    public static zza zzv(final Object o) {
        return new zza(o);
    }
    
    public static final class zza
    {
        private final Object zzJm;
        private final List<String> zzago;
        
        private zza(final Object o) {
            this.zzJm = zzx.zzw(o);
            this.zzago = new ArrayList<String>();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(100);
            sb.append(this.zzJm.getClass().getSimpleName());
            sb.append('{');
            for (int size = this.zzago.size(), i = 0; i < size; ++i) {
                sb.append(this.zzago.get(i));
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append('}');
            return sb.toString();
        }
        
        public zza zzg(final String s, final Object obj) {
            final List<String> zzago = this.zzago;
            final StringBuilder sb = new StringBuilder();
            sb.append(zzx.zzw(s));
            sb.append("=");
            sb.append(String.valueOf(obj));
            zzago.add(sb.toString());
            return this;
        }
    }
}
