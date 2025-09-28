package com.sonyericsson.android.camera.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.configuration.SharedPreferencesReader;
import com.sonyericsson.android.camera.configuration.SharedPreferencesWriter;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SharedPreferencesAccessor {
    public static final String TAG = "SharedPreferencesAccessor";
    private SharedPreferences mPreferences;
    private SharedPreferencesReader mReader;
    private SharedPreferencesWriter mWriter;
    private List<String> mPrefixList = new ArrayList();
    private Map<String, Map<String, String>> mMaps = new HashMap();

    public SharedPreferences getSharedPreferences() {
        return this.mPreferences;
    }

    public SharedPreferencesAccessor(Context context) {
        this.mPreferences = null;
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.mWriter = new SharedPreferencesWriter(this.mPreferences);
        this.mReader = new SharedPreferencesReader(this.mPreferences);
    }

    public SharedPreferencesAccessor(Context context, String str) {
        this.mPreferences = null;
        this.mPreferences = context.getSharedPreferences(str, 0);
        this.mWriter = new SharedPreferencesWriter(this.mPreferences);
        this.mReader = new SharedPreferencesReader(this.mPreferences);
    }

    public void readParameters(List<UserSettingKey> list) {
        if (this.mPreferences == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("SharedPreferences is null.");
                return;
            }
            return;
        }
        for (String str : this.mPrefixList) {
            if (CamLog.VERBOSE) {
                CamLog.d("loadParameters: prefix: " + str);
            }
            this.mMaps.put(str, this.mReader.readStringMap(list, str));
        }
    }

    public void writeParameters(boolean z) {
        for (String str : this.mMaps.keySet()) {
            if (CamLog.VERBOSE) {
                CamLog.d("writeSharedPreferences: prefix: " + str);
            }
            this.mWriter.writeString(this.mMaps.get(str), str);
        }
        apply(z);
    }

    public void clearParameters(boolean z) {
        reset();
        clear(z);
    }

    public void writeBoolean(String str, boolean z, boolean z2) {
        if (CamLog.VERBOSE) {
            CamLog.d("writeBoolean: key: " + str + ", value: " + z);
        }
        this.mWriter.writeBoolean(str, z);
        apply(z2);
    }

    public boolean readBoolean(String str, boolean z) {
        boolean z2 = this.mReader.readBoolean(str, z);
        if (CamLog.VERBOSE) {
            CamLog.d("readBoolean: key: " + str + ", value: " + z2);
        }
        return z2;
    }

    public void writeInt(String str, int i, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("writeInt: key: " + str + ", value: " + i);
        }
        this.mWriter.writeInt(str, i);
        apply(z);
    }

    public int readInt(String str, int i) {
        int i2 = this.mReader.readInt(str, i);
        if (CamLog.VERBOSE) {
            CamLog.d("readInt: key: " + str + ", value: " + i2);
        }
        return i2;
    }

    public void writeString(String str, String str2, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("writeString: key: " + str + ", value: " + str2);
        }
        this.mWriter.writeString(str, str2);
        apply(z);
    }

    public String readString(String str, String str2) {
        String string = this.mReader.readString(str, str2);
        if (CamLog.VERBOSE) {
            CamLog.d("readString: key: " + str + ", value: " + string);
        }
        return string;
    }

    public void writeLong(String str, long j, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d(TAG, "writeLong: key: " + str + ", value: " + j);
        }
        this.mWriter.writeLong(str, j);
        apply(z);
    }

    public long readLong(String str, long j) {
        long jLongValue = this.mReader.readLong(str, j).longValue();
        if (CamLog.VERBOSE) {
            CamLog.d(TAG, "readLong: key: " + str + ", value: " + jLongValue);
        }
        return jLongValue;
    }

    public void remove(String str, boolean z) {
        this.mWriter.remove(str);
        apply(z);
    }

    public void reset() {
        this.mMaps.clear();
        this.mPrefixList.clear();
    }

    public void clear(boolean z) {
        this.mWriter.clear();
        apply(z);
    }

    private void apply(boolean z) {
        if (z) {
            apply();
        }
    }

    public void apply() {
        this.mWriter.apply();
    }

    public void registerKey(String str) {
        if (this.mPrefixList.contains(str)) {
            return;
        }
        this.mPrefixList.add(str);
    }

    public Map<String, String> getStringMap(String str) {
        if (this.mMaps.containsKey(str)) {
            return this.mMaps.get(str);
        }
        return Collections.emptyMap();
    }

    public void setStringMap(String str, Map<String, String> map) {
        this.mMaps.put(str, map);
    }

    public static String createPrefix(ParameterCategory parameterCategory, CapturingMode capturingMode, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(parameterCategory.toString());
        sb.append('_');
        switch (parameterCategory) {
            case CAPTURING_MODE:
                sb.append(capturingMode);
                sb.append('_');
                break;
        }
        if (str != null && !str.equals("")) {
            sb.append(str);
            sb.append('_');
        }
        sb.append(SharedPreferencesConstants.KEY_PARAMS);
        return sb.toString();
    }

    public static SharedPreferences getSharedPreferences(Context context, String str, int i) {
        return context.getSharedPreferences(str, i);
    }
}
