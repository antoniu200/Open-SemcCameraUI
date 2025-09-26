// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import android.database.CharArrayBuffer;

public final class zzmo
{
    public static void zzb(final String s, final CharArrayBuffer charArrayBuffer) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            charArrayBuffer.sizeCopied = 0;
        }
        else if (charArrayBuffer.data != null && charArrayBuffer.data.length >= s.length()) {
            s.getChars(0, s.length(), charArrayBuffer.data, 0);
        }
        else {
            charArrayBuffer.data = s.toCharArray();
        }
        charArrayBuffer.sizeCopied = s.length();
    }
}
