package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzmu {
    private static final Pattern zzaim = Pattern.compile("\\\\.");
    private static final Pattern zzain = Pattern.compile("[\\\\\"/\b\f\n\r\t]");

    public static String zzcz(String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        Matcher matcher = zzain.matcher(str);
        StringBuffer stringBuffer = null;
        while (matcher.find()) {
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
            }
            char cCharAt = matcher.group().charAt(0);
            if (cCharAt == '\"') {
                str2 = "\\\\\\\"";
            } else if (cCharAt == '/') {
                str2 = "\\\\/";
            } else if (cCharAt != '\\') {
                switch (cCharAt) {
                    case '\b':
                        str2 = "\\\\b";
                        break;
                    case '\t':
                        str2 = "\\\\t";
                        break;
                    case '\n':
                        str2 = "\\\\n";
                        break;
                    default:
                        switch (cCharAt) {
                            case '\f':
                                str2 = "\\\\f";
                                break;
                            case '\r':
                                str2 = "\\\\r";
                                break;
                        }
                }
            } else {
                str2 = "\\\\\\\\";
            }
            matcher.appendReplacement(stringBuffer, str2);
        }
        if (stringBuffer == null) {
            return str;
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static boolean zzd(Object obj, Object obj2) {
        if (obj == null && obj2 == null) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if ((obj instanceof JSONObject) && (obj2 instanceof JSONObject)) {
            JSONObject jSONObject = (JSONObject) obj;
            JSONObject jSONObject2 = (JSONObject) obj2;
            if (jSONObject.length() != jSONObject2.length()) {
                return false;
            }
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                if (!jSONObject2.has(next)) {
                    return false;
                }
                if (!zzd(jSONObject.get(next), jSONObject2.get(next))) {
                    return false;
                }
            }
            return true;
        }
        if (!(obj instanceof JSONArray) || !(obj2 instanceof JSONArray)) {
            return obj.equals(obj2);
        }
        JSONArray jSONArray = (JSONArray) obj;
        JSONArray jSONArray2 = (JSONArray) obj2;
        if (jSONArray.length() != jSONArray2.length()) {
            return false;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                if (!zzd(jSONArray.get(i), jSONArray2.get(i))) {
                    return false;
                }
            } catch (JSONException unused) {
                return false;
            }
        }
        return true;
    }
}
