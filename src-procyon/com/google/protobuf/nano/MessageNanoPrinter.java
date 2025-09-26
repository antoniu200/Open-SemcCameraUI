// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

public final class MessageNanoPrinter
{
    private static final String INDENT = "  ";
    private static final int MAX_STRING_LEN = 200;
    
    private MessageNanoPrinter() {
    }
    
    private static void appendQuotedBytes(final byte[] array, final StringBuffer sb) {
        if (array == null) {
            sb.append("\"\"");
            return;
        }
        sb.append('\"');
        for (int i = 0; i < array.length; ++i) {
            final int j = array[i] & 0xFF;
            if (j != 92 && j != 34) {
                if (j >= 32 && j < 127) {
                    sb.append((char)j);
                }
                else {
                    sb.append(String.format("\\%03o", j));
                }
            }
            else {
                sb.append('\\');
                sb.append((char)j);
            }
        }
        sb.append('\"');
    }
    
    private static String deCamelCaseify(final String s) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (i == 0) {
                sb.append(Character.toLowerCase(char1));
            }
            else if (Character.isUpperCase(char1)) {
                sb.append('_');
                sb.append(Character.toLowerCase(char1));
            }
            else {
                sb.append(char1);
            }
        }
        return sb.toString();
    }
    
    private static String escapeString(final String s) {
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
    
    public static <T extends MessageNano> String print(final T t) {
        if (t == null) {
            return "";
        }
        final StringBuffer sb = new StringBuffer();
        try {
            print(null, t, new StringBuffer(), sb);
            return sb.toString();
        }
        catch (final InvocationTargetException ex) {
            final String value = String.valueOf(ex.getMessage());
            String concat;
            if (value.length() != 0) {
                concat = "Error printing proto: ".concat(value);
            }
            else {
                concat = new String("Error printing proto: ");
            }
            return concat;
        }
        catch (final IllegalAccessException ex2) {
            final String value2 = String.valueOf(ex2.getMessage());
            String concat2;
            if (value2.length() != 0) {
                concat2 = "Error printing proto: ".concat(value2);
            }
            else {
                concat2 = new String("Error printing proto: ");
            }
            return concat2;
        }
    }
    
    private static void print(String s, final Object o, final StringBuffer sb, final StringBuffer sb2) throws IllegalAccessException, InvocationTargetException {
        if (o == null) {
            return;
        }
        Label_0485: {
            if (!(o instanceof MessageNano)) {
                break Label_0485;
            }
            final int length = sb.length();
            if (s != null) {
                sb2.append(sb);
                sb2.append(deCamelCaseify(s));
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
                        if (type.isArray()) {
                            if (type.getComponentType() == Byte.TYPE) {
                                print(name, value, sb, sb2);
                            }
                            else {
                                int length3;
                                if (value == null) {
                                    length3 = 0;
                                }
                                else {
                                    length3 = Array.getLength(value);
                                }
                                for (int j = 0; j < length3; ++j) {
                                    print(name, Array.get(value, j), sb, sb2);
                                }
                            }
                        }
                        else {
                            print(name, value, sb, sb2);
                        }
                    }
                }
            }
            final Method[] methods = class1.getMethods();
            final int length4 = methods.length;
            int n = 0;
        Label_0453_Outer:
            while (true) {
                Label_0459: {
                    if (n >= length4) {
                        break Label_0459;
                    }
                    final String name2 = methods[n].getName();
                Label_0571_Outer:
                    while (true) {
                        if (!name2.startsWith("set")) {
                            break Label_0453;
                        }
                        final String substring = name2.substring(3);
                        try {
                            final String value2 = String.valueOf(substring);
                            String concat;
                            if (value2.length() != 0) {
                                concat = "has".concat(value2);
                            }
                            else {
                                concat = new String("has");
                            }
                            if (class1.getMethod(concat, (Class<?>[])new Class[0]).invoke(o, new Object[0])) {
                                final String value3 = String.valueOf(substring);
                                String concat2;
                                if (value3.length() != 0) {
                                    concat2 = "get".concat(value3);
                                }
                                else {
                                    concat2 = new String("get");
                                }
                                print(substring, class1.getMethod(concat2, (Class<?>[])new Class[0]).invoke(o, new Object[0]), sb, sb2);
                            }
                            ++n;
                            continue Label_0453_Outer;
                        Label_0571:
                            while (true) {
                            Block_22_Outer:
                                while (true) {
                                    sb.setLength(length);
                                    sb2.append(sb);
                                    sb2.append(">\n");
                                    return;
                                    Label_0547: {
                                        iftrue(Label_0565:)(!(o instanceof byte[]));
                                    }
                                    while (true) {
                                        Block_23: {
                                            break Block_23;
                                            s = sanitizeString((String)o);
                                            sb2.append("\"");
                                            sb2.append(s);
                                            sb2.append("\"");
                                            sb2.append("\n");
                                            Label_0578:
                                            return;
                                        }
                                        appendQuotedBytes((byte[])o, sb2);
                                        continue Label_0571;
                                        s = deCamelCaseify(s);
                                        sb2.append(sb);
                                        sb2.append(s);
                                        sb2.append(": ");
                                        iftrue(Label_0547:)(!(o instanceof String));
                                        continue Label_0571_Outer;
                                    }
                                    iftrue(Label_0578:)(s == null);
                                    continue Block_22_Outer;
                                }
                                Label_0565: {
                                    sb2.append(o);
                                }
                                continue Label_0571;
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
    
    private static String sanitizeString(final String s) {
        String concat = s;
        if (!s.startsWith("http")) {
            concat = s;
            if (s.length() > 200) {
                concat = String.valueOf(s.substring(0, 200)).concat("[...]");
            }
        }
        return escapeString(concat);
    }
}
