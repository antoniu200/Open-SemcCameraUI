package com.sonyericsson.android.camera.configuration;

import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SharedPreferencesReader {
    public static final String TAG = "SharedPreferencesReader";
    private SharedPreferences mPreferences;

    public SharedPreferencesReader(SharedPreferences sharedPreferences) {
        this.mPreferences = sharedPreferences;
    }

    public Map<String, String> readStringMap(List<?> list, String str) {
        HashMap map = new HashMap();
        if (this.mPreferences != null) {
            for (Object obj : list) {
                String string = this.mPreferences.getString(str + obj.toString(), null);
                if (string != null) {
                    map.put(obj.toString(), string);
                }
            }
        }
        return map;
    }

    public String readString(String str, String str2) {
        return this.mPreferences == null ? str2 : this.mPreferences.getString(str, str2);
    }

    public boolean readBoolean(String str, boolean z) {
        return this.mPreferences == null ? z : this.mPreferences.getBoolean(str, z);
    }

    public int readInt(String str, int i) {
        return this.mPreferences == null ? i : this.mPreferences.getInt(str, i);
    }

    public Long readLong(String str, long j) {
        if (this.mPreferences == null) {
            return Long.valueOf(j);
        }
        return Long.valueOf(this.mPreferences.getLong(str, j));
    }
}
