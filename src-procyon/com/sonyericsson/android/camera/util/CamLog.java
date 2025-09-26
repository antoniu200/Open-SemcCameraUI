// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import android.util.Log;
import android.os.Build;

public class CamLog
{
    public static final boolean DEBUG;
    private static final String GLOBAL_TAG = "CameraApp";
    public static final boolean IS_TIME_DEBUG = false;
    public static final boolean VERBOSE;
    
    static {
        DEBUG = Build.TYPE.equals("userdebug");
        VERBOSE = (CamLog.DEBUG && Log.isLoggable("CameraApp", 2));
    }
    
    private static void appendTag(final StringBuilder sb, final StackTraceElement stackTraceElement) {
        sb.append('[');
        sb.append(suppressFileExtension(stackTraceElement.getFileName()));
        sb.append("] ");
    }
    
    private static void appendTraceInfo(final StringBuilder sb, final StackTraceElement stackTraceElement) {
        sb.append(stackTraceElement.getMethodName());
        sb.append(":");
        sb.append(stackTraceElement.getLineNumber());
        sb.append(" ");
    }
    
    public static void d(final String s, final Throwable t) {
        if (CamLog.DEBUG || Log.isLoggable("CameraApp", 3)) {
            Log.d("CameraApp", makeLogStringWithLongInfo(s), t);
        }
    }
    
    public static void d(final String... array) {
        if (CamLog.DEBUG || Log.isLoggable("CameraApp", 3)) {
            Log.d("CameraApp", makeLogStringWithLongInfo(array));
        }
    }
    
    public static void e(final String s, final Throwable t) {
        Log.e("CameraApp", makeLogStringWithShortInfo(s), t);
    }
    
    public static void e(final String... array) {
        Log.e("CameraApp", makeLogStringWithShortInfo(array));
    }
    
    public static void i(final String s, final Throwable t) {
        Log.i("CameraApp", makeLogStringWithShortInfo(s), t);
    }
    
    public static void i(final String... array) {
        Log.i("CameraApp", makeLogStringWithShortInfo(array));
    }
    
    private static String makeLogStringWithLongInfo(final String... array) {
        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        final StringBuilder sb = new StringBuilder();
        appendTag(sb, stackTraceElement);
        appendTraceInfo(sb, stackTraceElement);
        for (int length = array.length, i = 0; i < length; ++i) {
            sb.append(array[i]);
        }
        return sb.toString();
    }
    
    private static String makeLogStringWithShortInfo(final String... array) {
        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        final StringBuilder sb = new StringBuilder();
        appendTag(sb, stackTraceElement);
        for (int length = array.length, i = 0; i < length; ++i) {
            sb.append(array[i]);
        }
        return sb.toString();
    }
    
    private static String suppressFileExtension(final String s) {
        final int lastIndex = s.lastIndexOf(46);
        if (lastIndex > 0 && lastIndex < s.length()) {
            return s.substring(0, lastIndex);
        }
        return s;
    }
    
    public static void v(final String s, final Throwable t) {
        if (CamLog.VERBOSE) {
            Log.v("CameraApp", makeLogStringWithLongInfo(s), t);
        }
    }
    
    public static void v(final String... array) {
        if (CamLog.VERBOSE) {
            Log.v("CameraApp", makeLogStringWithLongInfo(array));
        }
    }
    
    public static void w(final String s, final Throwable t) {
        Log.w("CameraApp", makeLogStringWithShortInfo(s), t);
    }
    
    public static void w(final String... array) {
        Log.w("CameraApp", makeLogStringWithShortInfo(array));
    }
}
