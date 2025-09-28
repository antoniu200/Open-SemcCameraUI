package com.google.android.gms.internal;

import com.sonyericsson.android.camera.view.modeselector.ResourceUtil;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import kotlin.text.Typography;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzsf {
    private static void zza(String str, Object obj, StringBuffer stringBuffer, StringBuffer stringBuffer2) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
        String str2;
        if (obj == null) {
            return;
        }
        if (obj instanceof zzse) {
            int length = stringBuffer.length();
            if (str != null) {
                stringBuffer2.append(stringBuffer);
                stringBuffer2.append(zzfB(str));
                stringBuffer2.append(" <\n");
                stringBuffer.append("  ");
            }
            Class<?> cls = obj.getClass();
            for (Field field : cls.getFields()) {
                int modifiers = field.getModifiers();
                String name = field.getName();
                if (!"cachedSize".equals(name) && (modifiers & 1) == 1 && (modifiers & 8) != 8 && !name.startsWith("_") && !name.endsWith("_")) {
                    Class<?> type = field.getType();
                    Object obj2 = field.get(obj);
                    if (!type.isArray() || type.getComponentType() == Byte.TYPE) {
                        zza(name, obj2, stringBuffer, stringBuffer2);
                    } else {
                        int length2 = obj2 == null ? 0 : Array.getLength(obj2);
                        for (int i = 0; i < length2; i++) {
                            zza(name, Array.get(obj2, i), stringBuffer, stringBuffer2);
                        }
                    }
                }
            }
            for (Method method : cls.getMethods()) {
                String name2 = method.getName();
                if (name2.startsWith("set")) {
                    String strSubstring = name2.substring(3);
                    try {
                        if (((Boolean) cls.getMethod("has" + strSubstring, new Class[0]).invoke(obj, new Object[0])).booleanValue()) {
                            zza(strSubstring, cls.getMethod("get" + strSubstring, new Class[0]).invoke(obj, new Object[0]), stringBuffer, stringBuffer2);
                        }
                    } catch (NoSuchMethodException unused) {
                    }
                }
            }
            if (str == null) {
                return;
            }
            stringBuffer.setLength(length);
            stringBuffer2.append(stringBuffer);
            str2 = ">\n";
        } else {
            String strZzfB = zzfB(str);
            stringBuffer2.append(stringBuffer);
            stringBuffer2.append(strZzfB);
            stringBuffer2.append(": ");
            if (obj instanceof String) {
                String strZzfC = zzfC((String) obj);
                stringBuffer2.append("\"");
                stringBuffer2.append(strZzfC);
                stringBuffer2.append("\"");
            } else if (obj instanceof byte[]) {
                zza((byte[]) obj, stringBuffer2);
            } else {
                stringBuffer2.append(obj);
            }
            str2 = "\n";
        }
        stringBuffer2.append(str2);
    }

    private static void zza(byte[] bArr, StringBuffer stringBuffer) {
        if (bArr == null) {
            stringBuffer.append("\"\"");
            return;
        }
        stringBuffer.append(Typography.quote);
        for (byte b : bArr) {
            int i = b & 255;
            if (i == 92 || i == 34) {
                stringBuffer.append('\\');
            } else {
                if (i < 32 || i >= 127) {
                    stringBuffer.append(String.format("\\%03o", Integer.valueOf(i)));
                }
            }
            stringBuffer.append((char) i);
        }
        stringBuffer.append(Typography.quote);
    }

    private static String zzcz(String str) {
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

    private static String zzfB(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (i == 0) {
                cCharAt = Character.toLowerCase(cCharAt);
            } else if (Character.isUpperCase(cCharAt)) {
                stringBuffer.append('_');
                cCharAt = Character.toLowerCase(cCharAt);
            }
            stringBuffer.append(cCharAt);
        }
        return stringBuffer.toString();
    }

    private static String zzfC(String str) {
        if (!str.startsWith(ResourceUtil.HTTP_SCHEME) && str.length() > 200) {
            str = str.substring(0, 200) + "[...]";
        }
        return zzcz(str);
    }

    public static <T extends zzse> String zzg(T t) throws SecurityException, IllegalArgumentException {
        StringBuilder sb;
        String message;
        if (t == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            zza(null, t, new StringBuffer(), stringBuffer);
            return stringBuffer.toString();
        } catch (IllegalAccessException e) {
            sb = new StringBuilder();
            sb.append("Error printing proto: ");
            message = e.getMessage();
            sb.append(message);
            return sb.toString();
        } catch (InvocationTargetException e2) {
            sb = new StringBuilder();
            sb.append("Error printing proto: ");
            message = e2.getMessage();
            sb.append(message);
            return sb.toString();
        }
    }
}
