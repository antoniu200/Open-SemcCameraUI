package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SomcDevicePolicyManager {
    public static String DISALLOW_RECORD_AUDIO;
    public static String SOMC_DEVICE_POLICY_SERVICE;
    private static Method mMethodHasUserRestriction;
    private static Class<?> somcDevicePolicyManagerClass;
    private Object mSomcDevicePolicyManager;

    static {
        try {
            somcDevicePolicyManagerClass = Class.forName("com.sonymobile.enterprise.admin.SomcDevicePolicyManager");
            initStringField("SOMC_DEVICE_POLICY_SERVICE");
            initStringField("DISALLOW_RECORD_AUDIO");
            mMethodHasUserRestriction = somcDevicePolicyManagerClass.getMethod("hasUserRestriction", String.class);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException unused) {
            SOMC_DEVICE_POLICY_SERVICE = null;
            DISALLOW_RECORD_AUDIO = null;
        }
    }

    private static void initStringField(String str) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            String str2 = (String) somcDevicePolicyManagerClass.getField(str).get(null);
            Field declaredField = SomcDevicePolicyManager.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(null, str2);
            declaredField.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw e;
        }
    }

    public static SomcDevicePolicyManager getInstance(Context context) {
        if (SOMC_DEVICE_POLICY_SERVICE == null) {
            return null;
        }
        return new SomcDevicePolicyManager(context.getSystemService(SOMC_DEVICE_POLICY_SERVICE));
    }

    private SomcDevicePolicyManager(Object obj) {
        this.mSomcDevicePolicyManager = obj;
    }

    public boolean hasUserRestriction(String str) {
        if (mMethodHasUserRestriction == null) {
            return false;
        }
        try {
            return ((Boolean) mMethodHasUserRestriction.invoke(this.mSomcDevicePolicyManager, str)).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException unused) {
            return false;
        }
    }
}
