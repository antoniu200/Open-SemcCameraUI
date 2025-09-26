// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal.safeparcel;

import java.util.List;
import android.os.Parcelable;
import android.os.IBinder;
import android.os.Bundle;
import android.os.Parcel;

public class zzb
{
    private static int zzG(final Parcel parcel, final int n) {
        parcel.writeInt(n | 0xFFFF0000);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }
    
    private static void zzH(final Parcel parcel, final int n) {
        final int dataPosition = parcel.dataPosition();
        parcel.setDataPosition(n - 4);
        parcel.writeInt(dataPosition - n);
        parcel.setDataPosition(dataPosition);
    }
    
    public static void zzI(final Parcel parcel, final int n) {
        zzH(parcel, n);
    }
    
    public static void zza(final Parcel parcel, final int n, final byte b) {
        zzb(parcel, n, 4);
        parcel.writeInt((int)b);
    }
    
    public static void zza(final Parcel parcel, final int n, final double n2) {
        zzb(parcel, n, 8);
        parcel.writeDouble(n2);
    }
    
    public static void zza(final Parcel parcel, final int n, final float n2) {
        zzb(parcel, n, 4);
        parcel.writeFloat(n2);
    }
    
    public static void zza(final Parcel parcel, final int n, final long n2) {
        zzb(parcel, n, 8);
        parcel.writeLong(n2);
    }
    
    public static void zza(final Parcel parcel, int zzG, final Bundle bundle, final boolean b) {
        if (bundle == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.writeBundle(bundle);
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, int zzG, final IBinder binder, final boolean b) {
        if (binder == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.writeStrongBinder(binder);
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, int zzG, final Parcel parcel2, final boolean b) {
        if (parcel2 == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.appendFrom(parcel2, 0, parcel2.dataSize());
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, int zzG, final Parcelable parcelable, final int n, final boolean b) {
        if (parcelable == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcelable.writeToParcel(parcel, n);
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, final int n, final Boolean b, final boolean b2) {
        if (b == null) {
            if (b2) {
                zzb(parcel, n, 0);
            }
            return;
        }
        zzb(parcel, n, 4);
        parcel.writeInt((int)(((boolean)b) ? 1 : 0));
    }
    
    public static void zza(final Parcel parcel, final int n, final Float n2, final boolean b) {
        if (n2 == null) {
            if (b) {
                zzb(parcel, n, 0);
            }
            return;
        }
        zzb(parcel, n, 4);
        parcel.writeFloat((float)n2);
    }
    
    public static void zza(final Parcel parcel, final int n, final Integer n2, final boolean b) {
        if (n2 == null) {
            if (b) {
                zzb(parcel, n, 0);
            }
            return;
        }
        zzb(parcel, n, 4);
        parcel.writeInt((int)n2);
    }
    
    public static void zza(final Parcel parcel, final int n, final Long n2, final boolean b) {
        if (n2 == null) {
            if (b) {
                zzb(parcel, n, 0);
            }
            return;
        }
        zzb(parcel, n, 8);
        parcel.writeLong((long)n2);
    }
    
    public static void zza(final Parcel parcel, int zzG, final String s, final boolean b) {
        if (s == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.writeString(s);
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, int i, final List<Integer> list, final boolean b) {
        final int n = 0;
        if (list == null) {
            if (b) {
                zzb(parcel, i, 0);
            }
            return;
        }
        final int zzG = zzG(parcel, i);
        final int size = list.size();
        parcel.writeInt(size);
        for (i = n; i < size; ++i) {
            parcel.writeInt((int)list.get(i));
        }
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, final int n, final short n2) {
        zzb(parcel, n, 4);
        parcel.writeInt((int)n2);
    }
    
    public static void zza(final Parcel parcel, final int n, final boolean b) {
        zzb(parcel, n, 4);
        parcel.writeInt((int)(b ? 1 : 0));
    }
    
    public static void zza(final Parcel parcel, int zzG, final byte[] array, final boolean b) {
        if (array == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.writeByteArray(array);
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, int zzG, final float[] array, final boolean b) {
        if (array == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.writeFloatArray(array);
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, int zzG, final int[] array, final boolean b) {
        if (array == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.writeIntArray(array);
        zzH(parcel, zzG);
    }
    
    public static <T extends Parcelable> void zza(final Parcel parcel, int i, final T[] array, final int n, final boolean b) {
        if (array == null) {
            if (b) {
                zzb(parcel, i, 0);
            }
            return;
        }
        final int zzG = zzG(parcel, i);
        final int length = array.length;
        parcel.writeInt(length);
        Parcelable parcelable;
        for (i = 0; i < length; ++i) {
            parcelable = array[i];
            if (parcelable == null) {
                parcel.writeInt(0);
            }
            else {
                zza(parcel, parcelable, n);
            }
        }
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, int zzG, final String[] array, final boolean b) {
        if (array == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.writeStringArray(array);
        zzH(parcel, zzG);
    }
    
    public static void zza(final Parcel parcel, int i, final byte[][] array, final boolean b) {
        final int n = 0;
        if (array == null) {
            if (b) {
                zzb(parcel, i, 0);
            }
            return;
        }
        final int zzG = zzG(parcel, i);
        final int length = array.length;
        parcel.writeInt(length);
        for (i = n; i < length; ++i) {
            parcel.writeByteArray(array[i]);
        }
        zzH(parcel, zzG);
    }
    
    private static <T extends Parcelable> void zza(final Parcel parcel, final T t, int dataPosition) {
        final int dataPosition2 = parcel.dataPosition();
        parcel.writeInt(1);
        final int dataPosition3 = parcel.dataPosition();
        t.writeToParcel(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        parcel.setDataPosition(dataPosition2);
        parcel.writeInt(dataPosition - dataPosition3);
        parcel.setDataPosition(dataPosition);
    }
    
    public static int zzaq(final Parcel parcel) {
        return zzG(parcel, 20293);
    }
    
    private static void zzb(final Parcel parcel, final int n, final int n2) {
        if (n2 >= 65535) {
            parcel.writeInt(n | 0xFFFF0000);
            parcel.writeInt(n2);
            return;
        }
        parcel.writeInt(n | n2 << 16);
    }
    
    public static void zzb(final Parcel parcel, int zzG, final List<String> list, final boolean b) {
        if (list == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.writeStringList((List)list);
        zzH(parcel, zzG);
    }
    
    public static void zzc(final Parcel parcel, final int n, final int n2) {
        zzb(parcel, n, 4);
        parcel.writeInt(n2);
    }
    
    public static <T extends Parcelable> void zzc(final Parcel parcel, int i, final List<T> list, final boolean b) {
        if (list == null) {
            if (b) {
                zzb(parcel, i, 0);
            }
            return;
        }
        final int zzG = zzG(parcel, i);
        final int size = list.size();
        parcel.writeInt(size);
        Parcelable parcelable;
        for (i = 0; i < size; ++i) {
            parcelable = list.get(i);
            if (parcelable == null) {
                parcel.writeInt(0);
            }
            else {
                zza(parcel, parcelable, 0);
            }
        }
        zzH(parcel, zzG);
    }
    
    public static void zzd(final Parcel parcel, int zzG, final List list, final boolean b) {
        if (list == null) {
            if (b) {
                zzb(parcel, zzG, 0);
            }
            return;
        }
        zzG = zzG(parcel, zzG);
        parcel.writeList(list);
        zzH(parcel, zzG);
    }
}
