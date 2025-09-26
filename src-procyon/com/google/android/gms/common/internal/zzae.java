// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.content.res.Resources;
import android.content.res.Resources$NotFoundException;
import android.util.Log;
import android.util.TypedValue;
import android.util.AttributeSet;
import android.content.Context;

public class zzae
{
    public static String zza(String attributeValue, final String str, final Context context, AttributeSet obj, final boolean b, final boolean b2, final String s) {
        if (obj == null) {
            attributeValue = null;
        }
        else {
            attributeValue = obj.getAttributeValue(attributeValue, str);
        }
        String string = attributeValue;
        if (attributeValue != null) {
            string = attributeValue;
            if (attributeValue.startsWith("@string/")) {
                string = attributeValue;
                if (b) {
                    final String substring = attributeValue.substring("@string/".length());
                    final String packageName = context.getPackageName();
                    obj = (AttributeSet)new TypedValue();
                    try {
                        final Resources resources = context.getResources();
                        final StringBuilder sb = new StringBuilder();
                        sb.append(packageName);
                        sb.append(":string/");
                        sb.append(substring);
                        resources.getValue(sb.toString(), (TypedValue)obj, true);
                    }
                    catch (final Resources$NotFoundException ex) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Could not find resource for ");
                        sb2.append(str);
                        sb2.append(": ");
                        sb2.append(attributeValue);
                        Log.w(s, sb2.toString());
                    }
                    if (((TypedValue)obj).string != null) {
                        string = ((TypedValue)obj).string.toString();
                    }
                    else {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Resource ");
                        sb3.append(str);
                        sb3.append(" was not a string: ");
                        sb3.append(obj);
                        Log.w(s, sb3.toString());
                        string = attributeValue;
                    }
                }
            }
        }
        if (b2 && string == null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Required XML attribute \"");
            sb4.append(str);
            sb4.append("\" missing");
            Log.w(s, sb4.toString());
        }
        return string;
    }
}
