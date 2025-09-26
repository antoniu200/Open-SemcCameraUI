// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import android.content.Context;
import java.lang.reflect.Method;

public class SomcDevicePolicyManager
{
    public static String DISALLOW_RECORD_AUDIO;
    public static String SOMC_DEVICE_POLICY_SERVICE;
    private static Method mMethodHasUserRestriction;
    private static Class<?> somcDevicePolicyManagerClass;
    private Object mSomcDevicePolicyManager;
    
    static {
        try {
            SomcDevicePolicyManager.somcDevicePolicyManagerClass = Class.forName("com.sonymobile.enterprise.admin.SomcDevicePolicyManager");
            initStringField("SOMC_DEVICE_POLICY_SERVICE");
            initStringField("DISALLOW_RECORD_AUDIO");
            SomcDevicePolicyManager.mMethodHasUserRestriction = SomcDevicePolicyManager.somcDevicePolicyManagerClass.getMethod("hasUserRestriction", String.class);
        }
        catch (final ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException ex) {
            SomcDevicePolicyManager.SOMC_DEVICE_POLICY_SERVICE = null;
            SomcDevicePolicyManager.DISALLOW_RECORD_AUDIO = null;
        }
    }
    
    private SomcDevicePolicyManager(final Object mSomcDevicePolicyManager) {
        this.mSomcDevicePolicyManager = mSomcDevicePolicyManager;
    }
    
    public static SomcDevicePolicyManager getInstance(final Context context) {
        if (SomcDevicePolicyManager.SOMC_DEVICE_POLICY_SERVICE == null) {
            return null;
        }
        return new SomcDevicePolicyManager(context.getSystemService(SomcDevicePolicyManager.SOMC_DEVICE_POLICY_SERVICE));
    }
    
    private static void initStringField(final String s) throws NoSuchFieldException, IllegalAccessException {
        try {
            final String value = (String)SomcDevicePolicyManager.somcDevicePolicyManagerClass.getField(s).get(null);
            final Field declaredField = SomcDevicePolicyManager.class.getDeclaredField(s);
            declaredField.setAccessible(true);
            declaredField.set(null, value);
            declaredField.setAccessible(false);
        }
        catch (final IllegalAccessException | NoSuchFieldException ex) {
            throw ex;
        }
    }
    
    public boolean hasUserRestriction(final String s) {
        if (SomcDevicePolicyManager.mMethodHasUserRestriction == null) {
            return false;
        }
        try {
            return (boolean)SomcDevicePolicyManager.mMethodHasUserRestriction.invoke(this.mSomcDevicePolicyManager, s);
        }
        catch (final IllegalAccessException | InvocationTargetException ex) {
            return false;
        }
    }
}
