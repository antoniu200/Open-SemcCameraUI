// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import java.util.Iterator;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Collections;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import android.preference.PreferenceManager;
import java.util.HashMap;
import java.util.ArrayList;
import android.content.Context;
import com.sonyericsson.android.camera.configuration.SharedPreferencesWriter;
import com.sonyericsson.android.camera.configuration.SharedPreferencesReader;
import java.util.List;
import android.content.SharedPreferences;
import java.util.Map;

public class SharedPreferencesAccessor
{
    public static final String TAG = "SharedPreferencesAccessor";
    private Map<String, Map<String, String>> mMaps;
    private SharedPreferences mPreferences;
    private List<String> mPrefixList;
    private SharedPreferencesReader mReader;
    private SharedPreferencesWriter mWriter;
    
    public SharedPreferencesAccessor(final Context context) {
        this.mPreferences = null;
        this.mPrefixList = new ArrayList<String>();
        this.mMaps = new HashMap<String, Map<String, String>>();
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.mWriter = new SharedPreferencesWriter(this.mPreferences);
        this.mReader = new SharedPreferencesReader(this.mPreferences);
    }
    
    public SharedPreferencesAccessor(final Context context, final String s) {
        this.mPreferences = null;
        this.mPrefixList = new ArrayList<String>();
        this.mMaps = new HashMap<String, Map<String, String>>();
        this.mPreferences = context.getSharedPreferences(s, 0);
        this.mWriter = new SharedPreferencesWriter(this.mPreferences);
        this.mReader = new SharedPreferencesReader(this.mPreferences);
    }
    
    private void apply(final boolean b) {
        if (b) {
            this.apply();
        }
    }
    
    public static String createPrefix(final ParameterCategory parameterCategory, final CapturingMode obj, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(parameterCategory.toString());
        sb.append('_');
        switch (SharedPreferencesAccessor$1.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterCategory[parameterCategory.ordinal()]) {
            case 2: {
                sb.append(obj);
                sb.append('_');
            }
            default:
            case 1: {
                if (str != null && !str.equals("")) {
                    sb.append(str);
                    sb.append('_');
                }
                sb.append("PARAMS_");
                return sb.toString();
            }
        }
    }
    
    public static SharedPreferences getSharedPreferences(final Context context, final String s, final int n) {
        return context.getSharedPreferences(s, n);
    }
    
    public void apply() {
        this.mWriter.apply();
    }
    
    public void clear(final boolean b) {
        this.mWriter.clear();
        this.apply(b);
    }
    
    public void clearParameters(final boolean b) {
        this.reset();
        this.clear(b);
    }
    
    public SharedPreferences getSharedPreferences() {
        return this.mPreferences;
    }
    
    public Map<String, String> getStringMap(final String s) {
        if (this.mMaps.containsKey(s)) {
            return this.mMaps.get(s);
        }
        return Collections.emptyMap();
    }
    
    public boolean readBoolean(final String str, final boolean b) {
        final boolean boolean1 = this.mReader.readBoolean(str, b);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("readBoolean: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(boolean1);
            CamLog.d(sb.toString());
        }
        return boolean1;
    }
    
    public int readInt(final String str, int int1) {
        int1 = this.mReader.readInt(str, int1);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("readInt: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(int1);
            CamLog.d(sb.toString());
        }
        return int1;
    }
    
    public long readLong(final String str, long longValue) {
        longValue = this.mReader.readLong(str, longValue);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("readLong: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(longValue);
            CamLog.d("SharedPreferencesAccessor", sb.toString());
        }
        return longValue;
    }
    
    public void readParameters(final List<UserSettingKey> list) {
        if (this.mPreferences == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("SharedPreferences is null.");
            }
            return;
        }
        for (final String str : this.mPrefixList) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("loadParameters: prefix: ");
                sb.append(str);
                CamLog.d(sb.toString());
            }
            this.mMaps.put(str, this.mReader.readStringMap(list, str));
        }
    }
    
    public String readString(final String str, String string) {
        string = this.mReader.readString(str, string);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("readString: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(string);
            CamLog.d(sb.toString());
        }
        return string;
    }
    
    public void registerKey(final String s) {
        if (!this.mPrefixList.contains(s)) {
            this.mPrefixList.add(s);
        }
    }
    
    public void remove(final String s, final boolean b) {
        this.mWriter.remove(s);
        this.apply(b);
    }
    
    public void reset() {
        this.mMaps.clear();
        this.mPrefixList.clear();
    }
    
    public void setStringMap(final String s, final Map<String, String> map) {
        this.mMaps.put(s, map);
    }
    
    public void writeBoolean(final String str, final boolean b, final boolean b2) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("writeBoolean: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        this.mWriter.writeBoolean(str, b);
        this.apply(b2);
    }
    
    public void writeInt(final String str, final int i, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("writeInt: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        this.mWriter.writeInt(str, i);
        this.apply(b);
    }
    
    public void writeLong(final String str, final long lng, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("writeLong: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(lng);
            CamLog.d("SharedPreferencesAccessor", sb.toString());
        }
        this.mWriter.writeLong(str, lng);
        this.apply(b);
    }
    
    public void writeParameters(final boolean b) {
        for (final String str : this.mMaps.keySet()) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("writeSharedPreferences: prefix: ");
                sb.append(str);
                CamLog.d(sb.toString());
            }
            this.mWriter.writeString(this.mMaps.get(str), str);
        }
        this.apply(b);
    }
    
    public void writeString(final String str, final String str2, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("writeString: key: ");
            sb.append(str);
            sb.append(", value: ");
            sb.append(str2);
            CamLog.d(sb.toString());
        }
        this.mWriter.writeString(str, str2);
        this.apply(b);
    }
}
