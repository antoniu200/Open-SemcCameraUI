package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ParamSharedPref {
    public static final String TAG = "ParamSharedPref";
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPref;

    ParamSharedPref(Context context, String str) {
        this.mPref = null;
        this.mEditor = null;
        try {
            this.mPref = context.getSharedPreferences(str, 0);
            this.mEditor = this.mPref.edit();
        } catch (Exception unused) {
        }
    }

    public int getParamFromSP(String str, int i) {
        if (this.mPref != null) {
            return this.mPref.getInt(str, i);
        }
        return 0;
    }

    public void setParamToSP(String str, int i) {
        if (this.mEditor != null) {
            this.mEditor.putInt(str, i);
            this.mEditor.apply();
        }
    }

    public boolean getParamFromSP(String str, boolean z) {
        return this.mPref != null ? this.mPref.getBoolean(str, z) : z;
    }

    public void setParamToSP(String str, boolean z) {
        if (this.mEditor != null) {
            this.mEditor.putBoolean(str, z);
            this.mEditor.apply();
        }
    }

    public float getParamFromSP(String str, float f) {
        return this.mPref != null ? this.mPref.getFloat(str, f) : f;
    }

    public void setParamToSP(String str, float f) {
        if (this.mEditor != null) {
            this.mEditor.putFloat(str, f);
            this.mEditor.apply();
        }
    }

    public String getParamFromSP(String str, String str2) {
        return this.mPref != null ? this.mPref.getString(str, str2) : str2;
    }

    public void setParamToSP(String str, String str2) {
        if (this.mEditor != null) {
            this.mEditor.putString(str, str2);
            this.mEditor.apply();
        }
    }

    public void clear() {
        if (this.mEditor != null) {
            this.mEditor.clear().commit();
        }
    }
}
