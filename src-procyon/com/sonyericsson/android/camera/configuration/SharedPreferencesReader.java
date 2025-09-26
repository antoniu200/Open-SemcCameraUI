// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import android.content.SharedPreferences;

public class SharedPreferencesReader
{
    public static final String TAG = "SharedPreferencesReader";
    private SharedPreferences mPreferences;
    
    public SharedPreferencesReader(final SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
    }
    
    public boolean readBoolean(final String s, final boolean b) {
        if (this.mPreferences == null) {
            return b;
        }
        return this.mPreferences.getBoolean(s, b);
    }
    
    public int readInt(final String s, final int n) {
        if (this.mPreferences == null) {
            return n;
        }
        return this.mPreferences.getInt(s, n);
    }
    
    public Long readLong(final String s, final long l) {
        if (this.mPreferences == null) {
            return l;
        }
        return this.mPreferences.getLong(s, l);
    }
    
    public String readString(final String s, final String s2) {
        if (this.mPreferences == null) {
            return s2;
        }
        return this.mPreferences.getString(s, s2);
    }
    
    public Map<String, String> readStringMap(final List<?> list, final String str) {
        final HashMap hashMap = new HashMap();
        if (this.mPreferences != null) {
            for (final Object next : list) {
                final StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(next.toString());
                final String string = this.mPreferences.getString(sb.toString(), (String)null);
                if (string != null) {
                    hashMap.put(next.toString(), string);
                }
            }
        }
        return hashMap;
    }
}
