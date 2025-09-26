package com.sonyericsson.android.camera.configuration;

import android.content.SharedPreferences;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SharedPreferencesWriter {
    public static final String TAG = "SharedPreferencesWriter";
    private SharedPreferences.Editor mPreferencesEditor;
    private final SharedPreferences mPrefs;

    public SharedPreferencesWriter(SharedPreferences sharedPreferences) {
        if (sharedPreferences == null) {
            throw new IllegalArgumentException("SharedPreferences = null");
        }
        this.mPrefs = sharedPreferences;
    }

    public void writeString(Map<String, String> map, String str) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            writeString(str + entry.getKey(), entry.getValue());
        }
    }

    public void writeString(String str, String str2) {
        if (CamLog.VERBOSE) {
            CamLog.d("writeString: key: " + str + ", value: " + str2);
        }
        getEditor().putString(str, str2);
    }

    private SharedPreferences.Editor getEditor() {
        if (this.mPreferencesEditor == null) {
            this.mPreferencesEditor = this.mPrefs.edit();
        }
        return this.mPreferencesEditor;
    }

    public void writeBoolean(String str, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("writeBoolean: key: " + str + ", value: " + z);
        }
        getEditor().putBoolean(str, z);
    }

    public void writeInt(String str, int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("writeInt: key: " + str + ", value: " + i);
        }
        getEditor().putInt(str, i);
    }

    public void writeLong(String str, long j) {
        if (CamLog.VERBOSE) {
            CamLog.d("writeLong: key: " + str + ", value: " + j);
        }
        getEditor().putLong(str, j);
    }

    public void remove(String str) {
        getEditor().remove(str);
    }

    public void clear() {
        getEditor().clear();
    }

    public void apply() {
        getEditor().apply();
    }
}
