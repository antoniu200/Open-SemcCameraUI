// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.util;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.io.File;
import java.awt.color.ICC_Profile;

public final class Debug
{
    private static final boolean DEBUG = false;
    private static final String NEWLINE = "\r\n";
    private static long counter;
    
    private Debug() {
    }
    
    private static String byteQuadToString(final int i) {
        final byte j = (byte)(i >> 24 & 0xFF);
        final byte k = (byte)(i >> 16 & 0xFF);
        final byte l = (byte)(i >> 8 & 0xFF);
        final byte m = (byte)(i >> 0 & 0xFF);
        final char c = (char)j;
        final char c2 = (char)k;
        final char c3 = (char)l;
        final char c4 = (char)m;
        final StringBuilder sb = new StringBuilder(31);
        sb.append(new String(new char[] { c, c2, c3, c4 }));
        sb.append(" bytequad: ");
        sb.append(i);
        sb.append(" b1: ");
        sb.append(j);
        sb.append(" b2: ");
        sb.append(k);
        sb.append(" b3: ");
        sb.append(l);
        sb.append(" b4: ");
        sb.append(m);
        return sb.toString();
    }
    
    public static void debug() {
    }
    
    public static void debug(final String s) {
    }
    
    private static void debug(String string, final ICC_Profile icc_Profile) {
        final StringBuilder sb = new StringBuilder();
        sb.append("ICC_Profile ");
        sb.append(string);
        sb.append(": ");
        if (icc_Profile == null) {
            string = "null";
        }
        else {
            string = icc_Profile.toString();
        }
        sb.append(string);
        debug(sb.toString());
        if (icc_Profile != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("\t getProfileClass: ");
            sb2.append(byteQuadToString(icc_Profile.getProfileClass()));
            debug(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("\t getPCSType: ");
            sb3.append(byteQuadToString(icc_Profile.getPCSType()));
            debug(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("\t getColorSpaceType() : ");
            sb4.append(byteQuadToString(icc_Profile.getColorSpaceType()));
            debug(sb4.toString());
        }
    }
    
    private static void debug(String path, final File file) {
        final StringBuilder sb = new StringBuilder();
        sb.append(path);
        sb.append(": ");
        if (file == null) {
            path = "null";
        }
        else {
            path = file.getPath();
        }
        sb.append(path);
        debug(sb.toString());
    }
    
    public static void debug(final String s, final Object o) {
        if (o == null) {
            debug(s, "null");
        }
        else if (o instanceof char[]) {
            debug(s, (char[])o);
        }
        else if (o instanceof byte[]) {
            debug(s, (byte[])o);
        }
        else if (o instanceof int[]) {
            debug(s, (int[])o);
        }
        else if (o instanceof String) {
            debug(s, (String)o);
        }
        else if (o instanceof List) {
            debug(s, (List<?>)o);
        }
        else if (o instanceof Map) {
            debug(s, (Map<?, ?>)o);
        }
        else if (o instanceof ICC_Profile) {
            debug(s, (ICC_Profile)o);
        }
        else if (o instanceof File) {
            debug(s, (File)o);
        }
        else if (o instanceof Date) {
            debug(s, (Date)o);
        }
        else if (o instanceof Calendar) {
            debug(s, (Calendar)o);
        }
        else {
            debug(s, o.toString());
        }
    }
    
    private static void debug(final String str, final String str2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" ");
        sb.append(str2);
        debug(sb.toString());
    }
    
    private static void debug(final String s, final Calendar calendar) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        String format;
        if (calendar == null) {
            format = "null";
        }
        else {
            format = simpleDateFormat.format(calendar.getTime());
        }
        debug(s, format);
    }
    
    private static void debug(final String s, final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        String format;
        if (date == null) {
            format = "null";
        }
        else {
            format = simpleDateFormat.format(date);
        }
        debug(s, format);
    }
    
    private static void debug(final String str, final List<?> list) {
        final StringBuilder sb = new StringBuilder();
        sb.append(" [");
        final long counter = Debug.counter;
        Debug.counter = 1L + counter;
        sb.append(counter);
        sb.append("]");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(" (");
        sb2.append(list.size());
        sb2.append(")");
        sb2.append(string);
        debug(sb2.toString());
        for (final Object next : list) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("\t");
            sb3.append(next.toString());
            sb3.append(string);
            debug(sb3.toString());
        }
        debug();
    }
    
    private static void debug(final String s, final Map<?, ?> map) {
        debug(getDebug(s, map));
    }
    
    private static void debug(final String s, final byte[] array) {
        debug(getDebug(s, array));
    }
    
    private static void debug(final String s, final char[] array) {
        debug(getDebug(s, array));
    }
    
    private static void debug(final String s, final int[] array) {
        debug(getDebug(s, array));
    }
    
    public static void debug(final Throwable t) {
        debug(getDebug(t));
    }
    
    public static void debug(final Throwable t, final int n) {
        debug(getDebug(t, n));
    }
    
    private static String getDebug(final String s, final Map<?, ?> map) {
        final StringBuilder sb = new StringBuilder();
        if (map == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(" map: ");
            sb2.append((Object)null);
            return sb2.toString();
        }
        final ArrayList list = new ArrayList((Collection<? extends E>)map.keySet());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append(" map: ");
        sb3.append(list.size());
        sb3.append("\r\n");
        sb.append(sb3.toString());
        for (int i = 0; i < list.size(); ++i) {
            final Object value = list.get(i);
            final Object value2 = map.get(value);
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("\t");
            sb4.append(i);
            sb4.append(": '");
            sb4.append(value);
            sb4.append("' -> '");
            sb4.append(value2);
            sb4.append("'");
            sb4.append("\r\n");
            sb.append(sb4.toString());
        }
        sb.append("\r\n");
        return sb.toString();
    }
    
    private static String getDebug(final String s, final byte[] array) {
        return getDebug(s, array, 250);
    }
    
    private static String getDebug(final String s, final byte[] array, final int n) {
        final StringBuilder sb = new StringBuilder();
        if (array == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(" (");
            sb2.append((Object)null);
            sb2.append(")");
            sb2.append("\r\n");
            sb.append(sb2.toString());
        }
        else {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(" (");
            sb3.append(array.length);
            sb3.append(")");
            sb3.append("\r\n");
            sb.append(sb3.toString());
            for (int i = 0; i < n && i < array.length; ++i) {
                final int n2 = 0xFF & array[i];
                char c;
                if (n2 != 0 && n2 != 10 && n2 != 11 && n2 != 13) {
                    c = (char)n2;
                }
                else {
                    c = ' ';
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("\t");
                sb4.append(i);
                sb4.append(": ");
                sb4.append(n2);
                sb4.append(" (");
                sb4.append(c);
                sb4.append(", 0x");
                sb4.append(Integer.toHexString(n2));
                sb4.append(")");
                sb4.append("\r\n");
                sb.append(sb4.toString());
            }
            if (array.length > n) {
                sb.append("\t...\r\n");
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
    
    private static String getDebug(final String s, final char[] array) {
        final StringBuilder sb = new StringBuilder();
        if (array == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(" (");
            sb2.append((Object)null);
            sb2.append(")");
            sb2.append("\r\n");
            sb.append(sb2.toString());
        }
        else {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(" (");
            sb3.append(array.length);
            sb3.append(")");
            sb3.append("\r\n");
            sb.append(sb3.toString());
            for (final char c : array) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("\t");
                sb4.append(c);
                sb4.append(" (");
                sb4.append(c & '\u00ff');
                sb4.append(")");
                sb4.append("\r\n");
                sb.append(sb4.toString());
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
    
    private static String getDebug(final String s, final int[] array) {
        final StringBuilder sb = new StringBuilder();
        if (array == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(" (");
            sb2.append((Object)null);
            sb2.append(")");
            sb2.append("\r\n");
            sb.append(sb2.toString());
        }
        else {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(" (");
            sb3.append(array.length);
            sb3.append(")");
            sb3.append("\r\n");
            sb.append(sb3.toString());
            for (final int j : array) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("\t");
                sb4.append(j);
                sb4.append("\r\n");
                sb.append(sb4.toString());
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
    
    private static String getDebug(final Throwable t) {
        return getDebug(t, -1);
    }
    
    private static String getDebug(final Throwable t, final int n) {
        final StringBuilder sb = new StringBuilder(35);
        final String lowerCase = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:SSS", Locale.ENGLISH).format(new Date()).toLowerCase();
        sb.append("\r\n");
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Throwable: ");
        String string;
        if (t == null) {
            string = "";
        }
        else {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("(");
            sb3.append(t.getClass().getName());
            sb3.append(")");
            string = sb3.toString();
        }
        sb2.append(string);
        sb2.append(":");
        sb2.append(lowerCase);
        sb2.append("\r\n");
        sb.append(sb2.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Throwable: ");
        String localizedMessage;
        if (t == null) {
            localizedMessage = "null";
        }
        else {
            localizedMessage = t.getLocalizedMessage();
        }
        sb4.append(localizedMessage);
        sb4.append("\r\n");
        sb.append(sb4.toString());
        sb.append("\r\n");
        sb.append(getStackTrace(t, n));
        sb.append("Caught here:\r\n");
        sb.append(getStackTrace(new Exception(), n, 1));
        sb.append("\r\n");
        return sb.toString();
    }
    
    private static String getStackTrace(final Throwable t, final int n) {
        return getStackTrace(t, n, 0);
    }
    
    private static String getStackTrace(final Throwable t, final int n, int n2) {
        final StringBuilder sb = new StringBuilder();
        if (t != null) {
            final StackTraceElement[] stackTrace = t.getStackTrace();
            if (stackTrace != null) {
                while (n2 < stackTrace.length && (n < 0 || n2 < n)) {
                    final StackTraceElement stackTraceElement = stackTrace[n2];
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("\tat ");
                    sb2.append(stackTraceElement.getClassName());
                    sb2.append(".");
                    sb2.append(stackTraceElement.getMethodName());
                    sb2.append("(");
                    sb2.append(stackTraceElement.getFileName());
                    sb2.append(":");
                    sb2.append(stackTraceElement.getLineNumber());
                    sb2.append(")");
                    sb2.append("\r\n");
                    sb.append(sb2.toString());
                    ++n2;
                }
                if (n >= 0 && stackTrace.length > n) {
                    sb.append("\t...\r\n");
                }
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
