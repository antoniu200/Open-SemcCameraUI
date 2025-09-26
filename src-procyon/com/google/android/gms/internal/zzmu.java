// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.regex.Matcher;
import android.text.TextUtils;
import java.util.regex.Pattern;

public final class zzmu
{
    private static final Pattern zzaim;
    private static final Pattern zzain;
    
    static {
        zzaim = Pattern.compile("\\\\.");
        zzain = Pattern.compile("[\\\\\"/\b\f\n\r\t]");
    }
    
    public static String zzcz(final String input) {
        String string = input;
        if (!TextUtils.isEmpty((CharSequence)input)) {
            final Matcher matcher = zzmu.zzain.matcher(input);
            StringBuffer sb = null;
            while (matcher.find()) {
                StringBuffer sb2;
                if ((sb2 = sb) == null) {
                    sb2 = new StringBuffer();
                }
                final char char1 = matcher.group().charAt(0);
                String replacement = null;
                Label_0152: {
                    if (char1 != '\"') {
                        if (char1 != '/') {
                            if (char1 != '\\') {
                                switch (char1) {
                                    default: {
                                        switch (char1) {
                                            default: {
                                                sb = sb2;
                                                continue;
                                            }
                                            case 13: {
                                                replacement = "\\\\r";
                                                break Label_0152;
                                            }
                                            case 12: {
                                                replacement = "\\\\f";
                                                break Label_0152;
                                            }
                                        }
                                        break;
                                    }
                                    case 10: {
                                        replacement = "\\\\n";
                                        break;
                                    }
                                    case 9: {
                                        replacement = "\\\\t";
                                        break;
                                    }
                                    case 8: {
                                        replacement = "\\\\b";
                                        break;
                                    }
                                }
                            }
                            else {
                                replacement = "\\\\\\\\";
                            }
                        }
                        else {
                            replacement = "\\\\/";
                        }
                    }
                    else {
                        replacement = "\\\\\\\"";
                    }
                }
                matcher.appendReplacement(sb2, replacement);
                sb = sb2;
            }
            if (sb == null) {
                return input;
            }
            matcher.appendTail(sb);
            string = sb.toString();
        }
        return string;
    }
    
    public static boolean zzd(final Object o, final Object obj) {
        if (o == null && obj == null) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (obj == null) {
            return false;
        }
        Label_0121: {
            if (!(o instanceof JSONObject) || !(obj instanceof JSONObject)) {
                break Label_0121;
            }
            final JSONObject jsonObject = (JSONObject)o;
            final JSONObject jsonObject2 = (JSONObject)obj;
            if (jsonObject.length() != jsonObject2.length()) {
                return false;
            }
            final Iterator keys = jsonObject.keys();
            String s;
            int n;
            JSONArray jsonArray;
            JSONArray jsonArray2;
            Label_0160_Outer:Block_12_Outer:
            while (true) {
                if (!keys.hasNext()) {
                    return true;
                }
                s = keys.next();
                if (!jsonObject2.has(s)) {
                    return false;
                }
                try {
                    if (!zzd(jsonObject.get(s), jsonObject2.get(s))) {
                        return false;
                    }
                    continue Label_0160_Outer;
                    while (true) {
                        while (true) {
                            iftrue(Label_0197:)(n >= jsonArray.length());
                            Block_14: {
                                break Block_14;
                                Label_0197: {
                                    return true;
                                }
                                jsonArray = (JSONArray)o;
                                jsonArray2 = (JSONArray)obj;
                                iftrue(Label_0158:)(jsonArray.length() == jsonArray2.length());
                                return false;
                            }
                            try {
                                if (!zzd(jsonArray.get(n), jsonArray2.get(n))) {
                                    return false;
                                }
                                ++n;
                                continue Block_12_Outer;
                            }
                            catch (final JSONException ex) {
                                return false;
                            }
                            return true;
                            Label_0158: {
                                n = 0;
                            }
                            continue Block_12_Outer;
                        }
                        iftrue(Label_0199:)(!(o instanceof JSONArray) || !(obj instanceof JSONArray));
                        continue;
                    }
                    Label_0199: {
                        return o.equals(obj);
                    }
                }
                catch (final JSONException ex2) {
                    return false;
                }
                break;
            }
        }
    }
}
