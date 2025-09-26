// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Iterator;
import java.util.HashMap;

public class zzmv
{
    public static void zza(final StringBuilder sb, final HashMap<String, String> hashMap) {
        sb.append("{");
        final Iterator<String> iterator = hashMap.keySet().iterator();
        int n = 1;
        while (iterator.hasNext()) {
            final String s = iterator.next();
            if (n == 0) {
                sb.append(",");
            }
            else {
                n = 0;
            }
            final String str = hashMap.get(s);
            sb.append("\"");
            sb.append(s);
            sb.append("\":");
            String str2;
            if (str == null) {
                str2 = "null";
            }
            else {
                sb.append("\"");
                sb.append(str);
                str2 = "\"";
            }
            sb.append(str2);
        }
        sb.append("}");
    }
}
