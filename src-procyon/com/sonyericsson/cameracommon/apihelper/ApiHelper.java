// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.apihelper;

public class ApiHelper
{
    public static final String TAG = "ApiHelper";
    
    public static int getIntFieldIfExists(final Class<?> clazz, final String name, final Class<?> obj, final int n) {
        try {
            return clazz.getDeclaredField(name).getInt(obj);
        }
        catch (final Exception ex) {
            return n;
        }
    }
    
    private static boolean hasClass(final String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (final Throwable t) {
            return false;
        }
    }
    
    private static boolean hasField(final Class<?> clazz, final String name) {
        try {
            clazz.getDeclaredField(name);
            return true;
        }
        catch (final NoSuchFieldException ex) {
            return false;
        }
    }
    
    private static boolean hasField(final String className, final String s) {
        try {
            return hasField(Class.forName(className), s);
        }
        catch (final Throwable t) {
            return false;
        }
    }
    
    private static boolean hasMethod(final String className, final String name) {
        try {
            Class.forName(className).getDeclaredMethod(name, (Class<?>[])new Class[0]);
            return true;
        }
        catch (final Throwable t) {
            return false;
        }
    }
    
    private static boolean hasMethod(final String className, final String name, final Class<?>... parameterTypes) {
        try {
            Class.forName(className).getDeclaredMethod(name, parameterTypes);
            return true;
        }
        catch (final Throwable t) {
            return false;
        }
    }
    
    private static boolean hasMethod(final String className, final String name, final String... array) {
        try {
            final Class<?> forName = Class.forName(className);
            final Class[] parameterTypes = new Class[array.length];
            for (int i = 0; i < array.length; ++i) {
                parameterTypes[i] = Class.forName(array[i]);
            }
            forName.getDeclaredMethod(name, (Class[])parameterTypes);
            return true;
        }
        catch (final Throwable t) {
            return false;
        }
    }
    
    public static boolean hasSpacialApis() {
        return hasClass("android.os.storage.StorageManager") && hasClass("android.os.storage.StorageVolume") && hasClass("android.os.storage.StorageManager$StorageType") && hasMethod("android.os.storage.StorageManager", "getVolumeList") && hasMethod("android.os.storage.StorageManager", "getVolumePath", "android.os.storage.StorageManager$StorageType") && hasMethod("android.os.storage.StorageManager", "getVolumeType", String.class) && hasMethod("android.os.storage.StorageManager", "getVolumeState", String.class) && hasField("com.android.internal.R$id", "message") && hasClass("com.sonyericsson.provider.SemcMediaStore") && hasClass("com.sonyericsson.provider.SemcMediaStore$ExtendedFiles$ExtendedFileColumns");
    }
}
