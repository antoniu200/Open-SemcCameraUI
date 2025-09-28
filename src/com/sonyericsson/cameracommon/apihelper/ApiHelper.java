package com.sonyericsson.cameracommon.apihelper;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ApiHelper {
    public static final String TAG = "ApiHelper";

    public static int getIntFieldIfExists(Class<?> cls, String str, Class<?> cls2, int i) {
        try {
            return cls.getDeclaredField(str).getInt(cls2);
        } catch (Exception unused) {
            return i;
        }
    }

    private static boolean hasField(String str, String str2) {
        try {
            return hasField(Class.forName(str), str2);
        } catch (Throwable unused) {
            return false;
        }
    }

    private static boolean hasField(Class<?> cls, String str) throws NoSuchFieldException {
        try {
            cls.getDeclaredField(str);
            return true;
        } catch (NoSuchFieldException unused) {
            return false;
        }
    }

    private static boolean hasClass(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static boolean hasMethod(String str, String str2, Class<?>... clsArr) {
        try {
            Class.forName(str).getDeclaredMethod(str2, clsArr);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static boolean hasMethod(String str, String str2) {
        try {
            Class.forName(str).getDeclaredMethod(str2, new Class[0]);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static boolean hasMethod(String str, String str2, String... strArr) {
        try {
            Class<?> cls = Class.forName(str);
            Class<?>[] clsArr = new Class[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                clsArr[i] = Class.forName(strArr[i]);
            }
            cls.getDeclaredMethod(str2, clsArr);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean hasSpacialApis() {
        return hasClass("android.os.storage.StorageManager") && hasClass("android.os.storage.StorageVolume") && hasClass("android.os.storage.StorageManager$StorageType") && hasMethod("android.os.storage.StorageManager", "getVolumeList") && hasMethod("android.os.storage.StorageManager", "getVolumePath", "android.os.storage.StorageManager$StorageType") && hasMethod("android.os.storage.StorageManager", "getVolumeType", (Class<?>[]) new Class[]{String.class}) && hasMethod("android.os.storage.StorageManager", "getVolumeState", (Class<?>[]) new Class[]{String.class}) && hasField("com.android.internal.R$id", "message") && hasClass("com.sonyericsson.provider.SemcMediaStore") && hasClass("com.sonyericsson.provider.SemcMediaStore$ExtendedFiles$ExtendedFileColumns");
    }
}
