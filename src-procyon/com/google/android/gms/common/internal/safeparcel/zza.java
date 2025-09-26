// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable$Creator;
import java.util.ArrayList;
import java.math.BigInteger;
import java.math.BigDecimal;
import android.os.Parcel;

public class zza
{
    public static BigDecimal[] zzA(final Parcel parcel, int i) {
        final int zza = zza(parcel, i);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final int int1 = parcel.readInt();
        final BigDecimal[] array = new BigDecimal[int1];
        for (i = 0; i < int1; ++i) {
            array[i] = new BigDecimal(new BigInteger(parcel.createByteArray()), parcel.readInt());
        }
        parcel.setDataPosition(dataPosition + zza);
        return array;
    }
    
    public static String[] zzB(final Parcel parcel, int dataPosition) {
        final int zza = zza(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final String[] stringArray = parcel.createStringArray();
        parcel.setDataPosition(dataPosition + zza);
        return stringArray;
    }
    
    public static ArrayList<Integer> zzC(final Parcel parcel, int i) {
        final int zza = zza(parcel, i);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final ArrayList list = new ArrayList();
        int int1;
        for (int1 = parcel.readInt(), i = 0; i < int1; ++i) {
            list.add(parcel.readInt());
        }
        parcel.setDataPosition(dataPosition + zza);
        return list;
    }
    
    public static ArrayList<String> zzD(final Parcel parcel, int dataPosition) {
        final int zza = zza(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final ArrayList stringArrayList = parcel.createStringArrayList();
        parcel.setDataPosition(dataPosition + zza);
        return stringArrayList;
    }
    
    public static Parcel zzE(final Parcel parcel, int dataPosition) {
        final int zza = zza(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final Parcel obtain = Parcel.obtain();
        obtain.appendFrom(parcel, dataPosition, zza);
        parcel.setDataPosition(dataPosition + zza);
        return obtain;
    }
    
    public static Parcel[] zzF(final Parcel parcel, int i) {
        final int zza = zza(parcel, i);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final int int1 = parcel.readInt();
        final Parcel[] array = new Parcel[int1];
        int int2;
        int dataPosition2;
        Parcel obtain;
        for (i = 0; i < int1; ++i) {
            int2 = parcel.readInt();
            if (int2 != 0) {
                dataPosition2 = parcel.dataPosition();
                obtain = Parcel.obtain();
                obtain.appendFrom(parcel, dataPosition2, int2);
                array[i] = obtain;
                parcel.setDataPosition(dataPosition2 + int2);
            }
            else {
                array[i] = null;
            }
        }
        parcel.setDataPosition(dataPosition + zza);
        return array;
    }
    
    public static int zza(final Parcel parcel, final int n) {
        if ((n & 0xFFFF0000) != 0xFFFF0000) {
            return n >> 16 & 0xFFFF;
        }
        return parcel.readInt();
    }
    
    public static <T extends Parcelable> T zza(final Parcel parcel, int zza, final Parcelable$Creator<T> parcelable$Creator) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final Parcelable parcelable = (Parcelable)parcelable$Creator.createFromParcel(parcel);
        parcel.setDataPosition(dataPosition + zza);
        return (T)parcelable;
    }
    
    private static void zza(final Parcel parcel, int zza, final int i) {
        zza = zza(parcel, zza);
        if (zza != i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected size ");
            sb.append(i);
            sb.append(" got ");
            sb.append(zza);
            sb.append(" (0x");
            sb.append(Integer.toHexString(zza));
            sb.append(")");
            throw new zza(sb.toString(), parcel);
        }
    }
    
    private static void zza(final Parcel parcel, final int n, final int n2, final int i) {
        if (n2 != i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected size ");
            sb.append(i);
            sb.append(" got ");
            sb.append(n2);
            sb.append(" (0x");
            sb.append(Integer.toHexString(n2));
            sb.append(")");
            throw new zza(sb.toString(), parcel);
        }
    }
    
    public static void zza(final Parcel parcel, int dataPosition, final List list, final ClassLoader classLoader) {
        final int zza = zza(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return;
        }
        parcel.readList(list, classLoader);
        parcel.setDataPosition(dataPosition + zza);
    }
    
    public static int zzao(final Parcel parcel) {
        return parcel.readInt();
    }
    
    public static int zzap(final Parcel parcel) {
        final int zzao = zzao(parcel);
        final int zza = zza(parcel, zzao);
        final int dataPosition = parcel.dataPosition();
        if (zzbM(zzao) != 20293) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected object header. Got 0x");
            sb.append(Integer.toHexString(zzao));
            throw new zza(sb.toString(), parcel);
        }
        final int i = zza + dataPosition;
        if (i >= dataPosition && i <= parcel.dataSize()) {
            return i;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Size read is invalid start=");
        sb2.append(dataPosition);
        sb2.append(" end=");
        sb2.append(i);
        throw new zza(sb2.toString(), parcel);
    }
    
    public static void zzb(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        parcel.setDataPosition(parcel.dataPosition() + zza);
    }
    
    public static <T> T[] zzb(final Parcel parcel, int zza, final Parcelable$Creator<T> parcelable$Creator) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final Object[] typedArray = parcel.createTypedArray((Parcelable$Creator)parcelable$Creator);
        parcel.setDataPosition(dataPosition + zza);
        return (T[])typedArray;
    }
    
    public static int zzbM(final int n) {
        return n & 0xFFFF;
    }
    
    public static <T> ArrayList<T> zzc(final Parcel parcel, int zza, final Parcelable$Creator<T> parcelable$Creator) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final ArrayList typedArrayList = parcel.createTypedArrayList((Parcelable$Creator)parcelable$Creator);
        parcel.setDataPosition(dataPosition + zza);
        return typedArrayList;
    }
    
    public static boolean zzc(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return parcel.readInt() != 0;
    }
    
    public static Boolean zzd(final Parcel parcel, final int n) {
        final int zza = zza(parcel, n);
        if (zza == 0) {
            return null;
        }
        zza(parcel, n, zza, 4);
        return parcel.readInt() != 0;
    }
    
    public static byte zze(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return (byte)parcel.readInt();
    }
    
    public static short zzf(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return (short)parcel.readInt();
    }
    
    public static int zzg(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return parcel.readInt();
    }
    
    public static Integer zzh(final Parcel parcel, final int n) {
        final int zza = zza(parcel, n);
        if (zza == 0) {
            return null;
        }
        zza(parcel, n, zza, 4);
        return parcel.readInt();
    }
    
    public static long zzi(final Parcel parcel, final int n) {
        zza(parcel, n, 8);
        return parcel.readLong();
    }
    
    public static Long zzj(final Parcel parcel, final int n) {
        final int zza = zza(parcel, n);
        if (zza == 0) {
            return null;
        }
        zza(parcel, n, zza, 8);
        return parcel.readLong();
    }
    
    public static BigInteger zzk(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final byte[] byteArray = parcel.createByteArray();
        parcel.setDataPosition(dataPosition + zza);
        return new BigInteger(byteArray);
    }
    
    public static float zzl(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return parcel.readFloat();
    }
    
    public static Float zzm(final Parcel parcel, final int n) {
        final int zza = zza(parcel, n);
        if (zza == 0) {
            return null;
        }
        zza(parcel, n, zza, 4);
        return parcel.readFloat();
    }
    
    public static double zzn(final Parcel parcel, final int n) {
        zza(parcel, n, 8);
        return parcel.readDouble();
    }
    
    public static BigDecimal zzo(final Parcel parcel, int int1) {
        final int zza = zza(parcel, int1);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final byte[] byteArray = parcel.createByteArray();
        int1 = parcel.readInt();
        parcel.setDataPosition(dataPosition + zza);
        return new BigDecimal(new BigInteger(byteArray), int1);
    }
    
    public static String zzp(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final String string = parcel.readString();
        parcel.setDataPosition(dataPosition + zza);
        return string;
    }
    
    public static IBinder zzq(final Parcel parcel, int dataPosition) {
        final int zza = zza(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final IBinder strongBinder = parcel.readStrongBinder();
        parcel.setDataPosition(dataPosition + zza);
        return strongBinder;
    }
    
    public static Bundle zzr(final Parcel parcel, int dataPosition) {
        final int zza = zza(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final Bundle bundle = parcel.readBundle();
        parcel.setDataPosition(dataPosition + zza);
        return bundle;
    }
    
    public static byte[] zzs(final Parcel parcel, int dataPosition) {
        final int zza = zza(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final byte[] byteArray = parcel.createByteArray();
        parcel.setDataPosition(dataPosition + zza);
        return byteArray;
    }
    
    public static byte[][] zzt(final Parcel parcel, int i) {
        final int zza = zza(parcel, i);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final int int1 = parcel.readInt();
        final byte[][] array = new byte[int1][];
        for (i = 0; i < int1; ++i) {
            array[i] = parcel.createByteArray();
        }
        parcel.setDataPosition(dataPosition + zza);
        return array;
    }
    
    public static boolean[] zzu(final Parcel parcel, int dataPosition) {
        final int zza = zza(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final boolean[] booleanArray = parcel.createBooleanArray();
        parcel.setDataPosition(dataPosition + zza);
        return booleanArray;
    }
    
    public static int[] zzv(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final int[] intArray = parcel.createIntArray();
        parcel.setDataPosition(dataPosition + zza);
        return intArray;
    }
    
    public static long[] zzw(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final long[] longArray = parcel.createLongArray();
        parcel.setDataPosition(dataPosition + zza);
        return longArray;
    }
    
    public static BigInteger[] zzx(final Parcel parcel, int i) {
        final int zza = zza(parcel, i);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final int int1 = parcel.readInt();
        final BigInteger[] array = new BigInteger[int1];
        for (i = 0; i < int1; ++i) {
            array[i] = new BigInteger(parcel.createByteArray());
        }
        parcel.setDataPosition(dataPosition + zza);
        return array;
    }
    
    public static float[] zzy(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final float[] floatArray = parcel.createFloatArray();
        parcel.setDataPosition(dataPosition + zza);
        return floatArray;
    }
    
    public static double[] zzz(final Parcel parcel, int zza) {
        zza = zza(parcel, zza);
        final int dataPosition = parcel.dataPosition();
        if (zza == 0) {
            return null;
        }
        final double[] doubleArray = parcel.createDoubleArray();
        parcel.setDataPosition(dataPosition + zza);
        return doubleArray;
    }
    
    public static class zza extends RuntimeException
    {
        public zza(final String str, final Parcel parcel) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" Parcel: pos=");
            sb.append(parcel.dataPosition());
            sb.append(" size=");
            sb.append(parcel.dataSize());
            super(sb.toString());
        }
    }
}
