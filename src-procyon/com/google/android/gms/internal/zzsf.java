// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Array;

public final class zzsf
{
    private static void zza(String str, final Object o, final StringBuffer sb, final StringBuffer sb2) throws IllegalAccessException, InvocationTargetException {
        if (o == null) {
            return;
        }
        Label_0452: {
            if (!(o instanceof zzse)) {
                break Label_0452;
            }
            final int length = sb.length();
            if (str != null) {
                sb2.append(sb);
                sb2.append(zzfB(str));
                sb2.append(" <\n");
                sb.append("  ");
            }
            final Class<?> class1 = o.getClass();
            for (final Field field : class1.getFields()) {
                final int modifiers = field.getModifiers();
                final String name = field.getName();
                if (!"cachedSize".equals(name)) {
                    if ((modifiers & 0x1) == 0x1 && (modifiers & 0x8) != 0x8 && !name.startsWith("_") && !name.endsWith("_")) {
                        final Class<?> type = field.getType();
                        final Object value = field.get(o);
                        if (type.isArray() && type.getComponentType() != Byte.TYPE) {
                            int length3;
                            if (value == null) {
                                length3 = 0;
                            }
                            else {
                                length3 = Array.getLength(value);
                            }
                            for (int j = 0; j < length3; ++j) {
                                zza(name, Array.get(value, j), sb, sb2);
                            }
                        }
                        else {
                            zza(name, value, sb, sb2);
                        }
                    }
                }
            }
            final Method[] methods = class1.getMethods();
            final int length4 = methods.length;
            int n = 0;
        Label_0424_Outer:
            while (true) {
                Label_0430: {
                    if (n >= length4) {
                        break Label_0430;
                    }
                    final String name2 = methods[n].getName();
                    while (true) {
                        if (!name2.startsWith("set")) {
                            break Label_0424;
                        }
                        final String substring = name2.substring(3);
                        try {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("has");
                            sb3.append(substring);
                            if (class1.getMethod(sb3.toString(), (Class<?>[])new Class[0]).invoke(o, new Object[0])) {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("get");
                                sb4.append(substring);
                                zza(substring, class1.getMethod(sb4.toString(), (Class<?>[])new Class[0]).invoke(o, new Object[0]), sb, sb2);
                            }
                            ++n;
                            continue Label_0424_Outer;
                            while (true) {
                                sb2.append(str);
                                return;
                                iftrue(Label_0547:)(str == null);
                                Block_21: {
                                    Block_19: {
                                        break Block_19;
                                        Label_0514: {
                                            iftrue(Label_0532:)(!(o instanceof byte[]));
                                        }
                                        break Block_21;
                                    }
                                    sb.setLength(length);
                                    sb2.append(sb);
                                    str = ">\n";
                                    continue;
                                }
                                zza((byte[])o, sb2);
                                Label_0538: {
                                    break Label_0538;
                                    while (true) {
                                        str = zzfC((String)o);
                                        sb2.append("\"");
                                        sb2.append(str);
                                        sb2.append("\"");
                                        break Label_0538;
                                        str = zzfB(str);
                                        sb2.append(sb);
                                        sb2.append(str);
                                        sb2.append(": ");
                                        iftrue(Label_0514:)(!(o instanceof String));
                                        continue;
                                    }
                                    Label_0547: {
                                        return;
                                    }
                                    Label_0532:
                                    sb2.append(o);
                                }
                                str = "\n";
                                continue;
                            }
                        }
                        catch (final NoSuchMethodException ex) {
                            continue;
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    
    private static void zza(final byte[] array, final StringBuffer sb) {
        if (array == null) {
            sb.append("\"\"");
            return;
        }
        sb.append('\"');
        for (int i = 0; i < array.length; ++i) {
            final int j = array[i] & 0xFF;
            if (j != 92 && j != 34) {
                if (j < 32 || j >= 127) {
                    sb.append(String.format("\\%03o", j));
                    continue;
                }
            }
            else {
                sb.append('\\');
            }
            sb.append((char)j);
        }
        sb.append('\"');
    }
    
    private static String zzcz(final String s) {
        final int length = s.length();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 >= ' ' && char1 <= '~' && char1 != '\"' && char1 != '\'') {
                sb.append(char1);
            }
            else {
                sb.append(String.format("\\u%04x", (int)char1));
            }
        }
        return sb.toString();
    }
    
    private static String zzfB(final String s) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            char lowerCase = '\0';
            Label_0034: {
                if (i != 0) {
                    lowerCase = char1;
                    if (!Character.isUpperCase(char1)) {
                        break Label_0034;
                    }
                    sb.append('_');
                }
                lowerCase = Character.toLowerCase(char1);
            }
            sb.append(lowerCase);
        }
        return sb.toString();
    }
    
    private static String zzfC(final String s) {
        String string = s;
        if (!s.startsWith("http")) {
            string = s;
            if (s.length() > 200) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s.substring(0, 200));
                sb.append("[...]");
                string = sb.toString();
            }
        }
        return zzcz(string);
    }
    
    public static <T extends zzse> String zzg(final T t) {
        if (t == null) {
            return "";
        }
        final StringBuffer sb = new StringBuffer();
        try {
            zza(null, t, new StringBuffer(), sb);
            return sb.toString();
        }
        catch (final InvocationTargetException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Error printing proto: ");
            ex.getMessage();
        }
        catch (final IllegalAccessException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Error printing proto: ");
            ex2.getMessage();
            goto Label_0056;
        }
    }
}
