package com.sonyericsson.android.camera.util;

import android.os.Build;
import android.util.Log;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CamLog {
    public static final boolean DEBUG = Build.TYPE.equals("userdebug");
    private static final String GLOBAL_TAG = "CameraApp";
    public static final boolean IS_TIME_DEBUG = false;
    public static final boolean VERBOSE;

    static {
        VERBOSE = DEBUG && Log.isLoggable(GLOBAL_TAG, 2);
    }

    public static void v(String... strArr) {
        if (VERBOSE) {
            Log.v(GLOBAL_TAG, makeLogStringWithLongInfo(strArr));
        }
    }

    public static void v(String str, Throwable th) {
        if (VERBOSE) {
            Log.v(GLOBAL_TAG, makeLogStringWithLongInfo(str), th);
        }
    }

    public static void d(String... strArr) {
        if (DEBUG || Log.isLoggable(GLOBAL_TAG, 3)) {
            Log.d(GLOBAL_TAG, makeLogStringWithLongInfo(strArr));
        }
    }

    public static void d(String str, Throwable th) {
        if (DEBUG || Log.isLoggable(GLOBAL_TAG, 3)) {
            Log.d(GLOBAL_TAG, makeLogStringWithLongInfo(str), th);
        }
    }

    public static void i(String... strArr) {
        Log.i(GLOBAL_TAG, makeLogStringWithShortInfo(strArr));
    }

    public static void i(String str, Throwable th) {
        Log.i(GLOBAL_TAG, makeLogStringWithShortInfo(str), th);
    }

    public static void w(String... strArr) {
        Log.w(GLOBAL_TAG, makeLogStringWithShortInfo(strArr));
    }

    public static void w(String str, Throwable th) {
        Log.w(GLOBAL_TAG, makeLogStringWithShortInfo(str), th);
    }

    public static void e(String... strArr) {
        Log.e(GLOBAL_TAG, makeLogStringWithShortInfo(strArr));
    }

    public static void e(String str, Throwable th) {
        Log.e(GLOBAL_TAG, makeLogStringWithShortInfo(str), th);
    }

    private static String makeLogStringWithLongInfo(String... strArr) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        appendTag(sb, stackTraceElement);
        appendTraceInfo(sb, stackTraceElement);
        for (String str : strArr) {
            sb.append(str);
        }
        return sb.toString();
    }

    private static String makeLogStringWithShortInfo(String... strArr) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        appendTag(sb, stackTraceElement);
        for (String str : strArr) {
            sb.append(str);
        }
        return sb.toString();
    }

    private static void appendTag(StringBuilder sb, StackTraceElement stackTraceElement) {
        sb.append('[');
        sb.append(suppressFileExtension(stackTraceElement.getFileName()));
        sb.append("] ");
    }

    private static void appendTraceInfo(StringBuilder sb, StackTraceElement stackTraceElement) {
        sb.append(stackTraceElement.getMethodName());
        sb.append(":");
        sb.append(stackTraceElement.getLineNumber());
        sb.append(" ");
    }

    private static String suppressFileExtension(String str) {
        int iLastIndexOf = str.lastIndexOf(46);
        return (iLastIndexOf <= 0 || iLastIndexOf >= str.length()) ? str : str.substring(0, iLastIndexOf);
    }
}
