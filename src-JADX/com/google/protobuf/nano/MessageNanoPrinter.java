package com.google.protobuf.nano;

import com.sonyericsson.android.camera.view.modeselector.ResourceUtil;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import kotlin.text.Typography;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class MessageNanoPrinter {
    private static final String INDENT = "  ";
    private static final int MAX_STRING_LEN = 200;

    private MessageNanoPrinter() {
    }

    public static <T extends MessageNano> String print(T message) throws SecurityException, IllegalArgumentException {
        if (message == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            print(null, message, new StringBuffer(), stringBuffer);
            return stringBuffer.toString();
        } catch (IllegalAccessException e) {
            String strValueOf = String.valueOf(e.getMessage());
            return strValueOf.length() != 0 ? "Error printing proto: ".concat(strValueOf) : new String("Error printing proto: ");
        } catch (InvocationTargetException e2) {
            String strValueOf2 = String.valueOf(e2.getMessage());
            return strValueOf2.length() != 0 ? "Error printing proto: ".concat(strValueOf2) : new String("Error printing proto: ");
        }
    }

    private static void print(String identifier, Object object, StringBuffer indentBuf, StringBuffer buf) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (object == null) {
            return;
        }
        if (object instanceof MessageNano) {
            int length = indentBuf.length();
            if (identifier != null) {
                buf.append(indentBuf);
                buf.append(deCamelCaseify(identifier));
                buf.append(" <\n");
                indentBuf.append(INDENT);
            }
            Class<?> cls = object.getClass();
            for (Field field : cls.getFields()) {
                int modifiers = field.getModifiers();
                String name = field.getName();
                if (!"cachedSize".equals(name) && (modifiers & 1) == 1 && (modifiers & 8) != 8 && !name.startsWith("_") && !name.endsWith("_")) {
                    Class<?> type = field.getType();
                    Object obj = field.get(object);
                    if (type.isArray()) {
                        if (type.getComponentType() == Byte.TYPE) {
                            print(name, obj, indentBuf, buf);
                        } else {
                            int length2 = obj == null ? 0 : Array.getLength(obj);
                            for (int i = 0; i < length2; i++) {
                                print(name, Array.get(obj, i), indentBuf, buf);
                            }
                        }
                    } else {
                        print(name, obj, indentBuf, buf);
                    }
                }
            }
            for (Method method : cls.getMethods()) {
                String name2 = method.getName();
                if (name2.startsWith("set")) {
                    String strSubstring = name2.substring(3);
                    try {
                        String strValueOf = String.valueOf(strSubstring);
                        if (((Boolean) cls.getMethod(strValueOf.length() != 0 ? "has".concat(strValueOf) : new String("has"), new Class[0]).invoke(object, new Object[0])).booleanValue()) {
                            String strValueOf2 = String.valueOf(strSubstring);
                            print(strSubstring, cls.getMethod(strValueOf2.length() != 0 ? "get".concat(strValueOf2) : new String("get"), new Class[0]).invoke(object, new Object[0]), indentBuf, buf);
                        }
                    } catch (NoSuchMethodException unused) {
                    }
                }
            }
            if (identifier != null) {
                indentBuf.setLength(length);
                buf.append(indentBuf);
                buf.append(">\n");
                return;
            }
            return;
        }
        String strDeCamelCaseify = deCamelCaseify(identifier);
        buf.append(indentBuf);
        buf.append(strDeCamelCaseify);
        buf.append(": ");
        if (object instanceof String) {
            String strSanitizeString = sanitizeString((String) object);
            buf.append("\"");
            buf.append(strSanitizeString);
            buf.append("\"");
        } else if (object instanceof byte[]) {
            appendQuotedBytes((byte[]) object, buf);
        } else {
            buf.append(object);
        }
        buf.append("\n");
    }

    private static String deCamelCaseify(String identifier) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < identifier.length(); i++) {
            char cCharAt = identifier.charAt(i);
            if (i == 0) {
                stringBuffer.append(Character.toLowerCase(cCharAt));
            } else if (Character.isUpperCase(cCharAt)) {
                stringBuffer.append('_');
                stringBuffer.append(Character.toLowerCase(cCharAt));
            } else {
                stringBuffer.append(cCharAt);
            }
        }
        return stringBuffer.toString();
    }

    private static String sanitizeString(String str) {
        if (!str.startsWith(ResourceUtil.HTTP_SCHEME) && str.length() > 200) {
            str = String.valueOf(str.substring(0, 200)).concat("[...]");
        }
        return escapeString(str);
    }

    private static String escapeString(String str) {
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt < ' ' || cCharAt > '~' || cCharAt == '\"' || cCharAt == '\'') {
                sb.append(String.format("\\u%04x", Integer.valueOf(cCharAt)));
            } else {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }

    private static void appendQuotedBytes(byte[] bytes, StringBuffer builder) {
        if (bytes == null) {
            builder.append("\"\"");
            return;
        }
        builder.append(Typography.quote);
        for (byte b : bytes) {
            int i = b & 255;
            if (i == 92 || i == 34) {
                builder.append('\\');
                builder.append((char) i);
            } else if (i < 32 || i >= 127) {
                builder.append(String.format("\\%03o", Integer.valueOf(i)));
            } else {
                builder.append((char) i);
            }
        }
        builder.append(Typography.quote);
    }
}
