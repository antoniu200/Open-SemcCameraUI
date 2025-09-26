// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.ArrayList;
import com.google.android.gms.common.internal.zzw;

public final class zzmj
{
    public static <T> int zza(final T[] array, final T t) {
        int i = 0;
        int length;
        if (array != null) {
            length = array.length;
        }
        else {
            length = 0;
        }
        while (i < length) {
            if (zzw.equal(array[i], t)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static void zza(final StringBuilder sb, final double[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(Double.toString(array[i]));
        }
    }
    
    public static void zza(final StringBuilder sb, final float[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(Float.toString(array[i]));
        }
    }
    
    public static void zza(final StringBuilder sb, final int[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(Integer.toString(array[i]));
        }
    }
    
    public static void zza(final StringBuilder sb, final long[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(Long.toString(array[i]));
        }
    }
    
    public static <T> void zza(final StringBuilder sb, final T[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(array[i].toString());
        }
    }
    
    public static void zza(final StringBuilder sb, final String[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("\"");
            sb.append(array[i]);
            sb.append("\"");
        }
    }
    
    public static void zza(final StringBuilder sb, final boolean[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(Boolean.toString(array[i]));
        }
    }
    
    public static Integer[] zza(final int[] array) {
        if (array == null) {
            return null;
        }
        final int length = array.length;
        final Integer[] array2 = new Integer[length];
        for (int i = 0; i < length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    public static <T> ArrayList<T> zzb(final T[] array) {
        final int length = array.length;
        final ArrayList list = new ArrayList<T>(length);
        for (int i = 0; i < length; ++i) {
            list.add(array[i]);
        }
        return (ArrayList<T>)list;
    }
    
    public static <T> boolean zzb(final T[] array, final T t) {
        return zza(array, t) >= 0;
    }
    
    public static <T> ArrayList<T> zzqs() {
        return new ArrayList<T>();
    }
}
