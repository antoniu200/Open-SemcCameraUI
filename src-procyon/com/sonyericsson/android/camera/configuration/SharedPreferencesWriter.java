// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration;

import java.util.Iterator;
import java.util.Map;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.SharedPreferences;
import android.content.SharedPreferences$Editor;

public class SharedPreferencesWriter
{
    public static final String TAG = "SharedPreferencesWriter";
    private SharedPreferences$Editor mPreferencesEditor;
    private final SharedPreferences mPrefs;
    
    public SharedPreferencesWriter(final SharedPreferences mPrefs) {
        if (mPrefs == null) {
            throw new IllegalArgumentException("SharedPreferences = null");
        }
        this.mPrefs = mPrefs;
    }
    
    private SharedPreferences$Editor getEditor() {
        if (this.mPreferencesEditor == null) {
            this.mPreferencesEditor = this.mPrefs.edit();
        }
        return this.mPreferencesEditor;
    }
    
    public void apply() {
        this.getEditor().apply();
    }
    
    public void clear() {
        this.getEditor().clear();
    }
    
    public void remove(final String s) {
        this.getEditor().remove(s);
    }
    
    public void writeBoolean(final String str, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("writeBoolean: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        this.getEditor().putBoolean(str, b);
    }
    
    public void writeInt(final String str, final int i) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("writeInt: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        this.getEditor().putInt(str, i);
    }
    
    public void writeLong(final String str, final long lng) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("writeLong: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(lng);
            CamLog.d(sb.toString());
        }
        this.getEditor().putLong(str, lng);
    }
    
    public void writeString(final String str, final String str2) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("writeString: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(str2);
            CamLog.d(sb.toString());
        }
        this.getEditor().putString(str, str2);
    }
    
    public void writeString(final Map<String, String> map, final String str) {
        for (final Map.Entry<String, V> entry : map.entrySet()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(entry.getKey());
            this.writeString(sb.toString(), (String)entry.getValue());
        }
    }
}
