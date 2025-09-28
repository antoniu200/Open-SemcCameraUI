package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zza {

    /* renamed from: com.google.android.gms.common.internal.safeparcel.zza$zza, reason: collision with other inner class name */
    public static class C0010zza extends RuntimeException {
        public C0010zza(String str, Parcel parcel) {
            super(str + " Parcel: pos=" + parcel.dataPosition() + " size=" + parcel.dataSize());
        }
    }

    public static BigDecimal[] zzA(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        int i2 = parcel.readInt();
        BigDecimal[] bigDecimalArr = new BigDecimal[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            byte[] bArrCreateByteArray = parcel.createByteArray();
            bigDecimalArr[i3] = new BigDecimal(new BigInteger(bArrCreateByteArray), parcel.readInt());
        }
        parcel.setDataPosition(iDataPosition + iZza);
        return bigDecimalArr;
    }

    public static String[] zzB(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        String[] strArrCreateStringArray = parcel.createStringArray();
        parcel.setDataPosition(iDataPosition + iZza);
        return strArrCreateStringArray;
    }

    public static ArrayList<Integer> zzC(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        int i2 = parcel.readInt();
        for (int i3 = 0; i3 < i2; i3++) {
            arrayList.add(Integer.valueOf(parcel.readInt()));
        }
        parcel.setDataPosition(iDataPosition + iZza);
        return arrayList;
    }

    public static ArrayList<String> zzD(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        ArrayList<String> arrayListCreateStringArrayList = parcel.createStringArrayList();
        parcel.setDataPosition(iDataPosition + iZza);
        return arrayListCreateStringArrayList;
    }

    public static Parcel zzE(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        Parcel parcelObtain = Parcel.obtain();
        parcelObtain.appendFrom(parcel, iDataPosition, iZza);
        parcel.setDataPosition(iDataPosition + iZza);
        return parcelObtain;
    }

    public static Parcel[] zzF(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        int i2 = parcel.readInt();
        Parcel[] parcelArr = new Parcel[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = parcel.readInt();
            if (i4 != 0) {
                int iDataPosition2 = parcel.dataPosition();
                Parcel parcelObtain = Parcel.obtain();
                parcelObtain.appendFrom(parcel, iDataPosition2, i4);
                parcelArr[i3] = parcelObtain;
                parcel.setDataPosition(iDataPosition2 + i4);
            } else {
                parcelArr[i3] = null;
            }
        }
        parcel.setDataPosition(iDataPosition + iZza);
        return parcelArr;
    }

    public static int zza(Parcel parcel, int i) {
        return (i & SupportMenu.CATEGORY_MASK) != -65536 ? (i >> 16) & 65535 : parcel.readInt();
    }

    public static <T extends Parcelable> T zza(Parcel parcel, int i, Parcelable.Creator<T> creator) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        T tCreateFromParcel = creator.createFromParcel(parcel);
        parcel.setDataPosition(iDataPosition + iZza);
        return tCreateFromParcel;
    }

    private static void zza(Parcel parcel, int i, int i2) {
        int iZza = zza(parcel, i);
        if (iZza != i2) {
            throw new C0010zza("Expected size " + i2 + " got " + iZza + " (0x" + Integer.toHexString(iZza) + ")", parcel);
        }
    }

    private static void zza(Parcel parcel, int i, int i2, int i3) {
        if (i2 != i3) {
            throw new C0010zza("Expected size " + i3 + " got " + i2 + " (0x" + Integer.toHexString(i2) + ")", parcel);
        }
    }

    public static void zza(Parcel parcel, int i, List list, ClassLoader classLoader) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return;
        }
        parcel.readList(list, classLoader);
        parcel.setDataPosition(iDataPosition + iZza);
    }

    public static int zzao(Parcel parcel) {
        return parcel.readInt();
    }

    public static int zzap(Parcel parcel) {
        int iZzao = zzao(parcel);
        int iZza = zza(parcel, iZzao);
        int iDataPosition = parcel.dataPosition();
        if (zzbM(iZzao) != 20293) {
            throw new C0010zza("Expected object header. Got 0x" + Integer.toHexString(iZzao), parcel);
        }
        int i = iZza + iDataPosition;
        if (i >= iDataPosition && i <= parcel.dataSize()) {
            return i;
        }
        throw new C0010zza("Size read is invalid start=" + iDataPosition + " end=" + i, parcel);
    }

    public static void zzb(Parcel parcel, int i) {
        parcel.setDataPosition(parcel.dataPosition() + zza(parcel, i));
    }

    public static <T> T[] zzb(Parcel parcel, int i, Parcelable.Creator<T> creator) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        T[] tArr = (T[]) parcel.createTypedArray(creator);
        parcel.setDataPosition(iDataPosition + iZza);
        return tArr;
    }

    public static int zzbM(int i) {
        return i & 65535;
    }

    public static <T> ArrayList<T> zzc(Parcel parcel, int i, Parcelable.Creator<T> creator) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        ArrayList<T> arrayListCreateTypedArrayList = parcel.createTypedArrayList(creator);
        parcel.setDataPosition(iDataPosition + iZza);
        return arrayListCreateTypedArrayList;
    }

    public static boolean zzc(Parcel parcel, int i) {
        zza(parcel, i, 4);
        return parcel.readInt() != 0;
    }

    public static Boolean zzd(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        if (iZza == 0) {
            return null;
        }
        zza(parcel, i, iZza, 4);
        return Boolean.valueOf(parcel.readInt() != 0);
    }

    public static byte zze(Parcel parcel, int i) {
        zza(parcel, i, 4);
        return (byte) parcel.readInt();
    }

    public static short zzf(Parcel parcel, int i) {
        zza(parcel, i, 4);
        return (short) parcel.readInt();
    }

    public static int zzg(Parcel parcel, int i) {
        zza(parcel, i, 4);
        return parcel.readInt();
    }

    public static Integer zzh(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        if (iZza == 0) {
            return null;
        }
        zza(parcel, i, iZza, 4);
        return Integer.valueOf(parcel.readInt());
    }

    public static long zzi(Parcel parcel, int i) {
        zza(parcel, i, 8);
        return parcel.readLong();
    }

    public static Long zzj(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        if (iZza == 0) {
            return null;
        }
        zza(parcel, i, iZza, 8);
        return Long.valueOf(parcel.readLong());
    }

    public static BigInteger zzk(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        byte[] bArrCreateByteArray = parcel.createByteArray();
        parcel.setDataPosition(iDataPosition + iZza);
        return new BigInteger(bArrCreateByteArray);
    }

    public static float zzl(Parcel parcel, int i) {
        zza(parcel, i, 4);
        return parcel.readFloat();
    }

    public static Float zzm(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        if (iZza == 0) {
            return null;
        }
        zza(parcel, i, iZza, 4);
        return Float.valueOf(parcel.readFloat());
    }

    public static double zzn(Parcel parcel, int i) {
        zza(parcel, i, 8);
        return parcel.readDouble();
    }

    public static BigDecimal zzo(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        byte[] bArrCreateByteArray = parcel.createByteArray();
        int i2 = parcel.readInt();
        parcel.setDataPosition(iDataPosition + iZza);
        return new BigDecimal(new BigInteger(bArrCreateByteArray), i2);
    }

    public static String zzp(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        String string = parcel.readString();
        parcel.setDataPosition(iDataPosition + iZza);
        return string;
    }

    public static IBinder zzq(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        IBinder strongBinder = parcel.readStrongBinder();
        parcel.setDataPosition(iDataPosition + iZza);
        return strongBinder;
    }

    public static Bundle zzr(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        Bundle bundle = parcel.readBundle();
        parcel.setDataPosition(iDataPosition + iZza);
        return bundle;
    }

    public static byte[] zzs(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        byte[] bArrCreateByteArray = parcel.createByteArray();
        parcel.setDataPosition(iDataPosition + iZza);
        return bArrCreateByteArray;
    }

    public static byte[][] zzt(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return (byte[][]) null;
        }
        int i2 = parcel.readInt();
        byte[][] bArr = new byte[i2][];
        for (int i3 = 0; i3 < i2; i3++) {
            bArr[i3] = parcel.createByteArray();
        }
        parcel.setDataPosition(iDataPosition + iZza);
        return bArr;
    }

    public static boolean[] zzu(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        boolean[] zArrCreateBooleanArray = parcel.createBooleanArray();
        parcel.setDataPosition(iDataPosition + iZza);
        return zArrCreateBooleanArray;
    }

    public static int[] zzv(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        int[] iArrCreateIntArray = parcel.createIntArray();
        parcel.setDataPosition(iDataPosition + iZza);
        return iArrCreateIntArray;
    }

    public static long[] zzw(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        long[] jArrCreateLongArray = parcel.createLongArray();
        parcel.setDataPosition(iDataPosition + iZza);
        return jArrCreateLongArray;
    }

    public static BigInteger[] zzx(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        int i2 = parcel.readInt();
        BigInteger[] bigIntegerArr = new BigInteger[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            bigIntegerArr[i3] = new BigInteger(parcel.createByteArray());
        }
        parcel.setDataPosition(iDataPosition + iZza);
        return bigIntegerArr;
    }

    public static float[] zzy(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        float[] fArrCreateFloatArray = parcel.createFloatArray();
        parcel.setDataPosition(iDataPosition + iZza);
        return fArrCreateFloatArray;
    }

    public static double[] zzz(Parcel parcel, int i) {
        int iZza = zza(parcel, i);
        int iDataPosition = parcel.dataPosition();
        if (iZza == 0) {
            return null;
        }
        double[] dArrCreateDoubleArray = parcel.createDoubleArray();
        parcel.setDataPosition(iDataPosition + iZza);
        return dArrCreateDoubleArray;
    }
}
