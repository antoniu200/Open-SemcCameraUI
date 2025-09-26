// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences$Editor;

public class ParamSharedPref
{
    public static final String TAG = "ParamSharedPref";
    private SharedPreferences$Editor mEditor;
    private SharedPreferences mPref;
    
    ParamSharedPref(final Context context, final String s) {
        this.mPref = null;
        this.mEditor = null;
        try {
            this.mPref = context.getSharedPreferences(s, 0);
            this.mEditor = this.mPref.edit();
        }
        catch (final Exception ex) {}
    }
    
    public void clear() {
        if (this.mEditor != null) {
            this.mEditor.clear().commit();
        }
    }
    
    public float getParamFromSP(final String s, final float n) {
        if (this.mPref != null) {
            return this.mPref.getFloat(s, n);
        }
        return n;
    }
    
    public int getParamFromSP(final String s, final int n) {
        if (this.mPref != null) {
            return this.mPref.getInt(s, n);
        }
        return 0;
    }
    
    public String getParamFromSP(final String s, final String s2) {
        if (this.mPref != null) {
            return this.mPref.getString(s, s2);
        }
        return s2;
    }
    
    public boolean getParamFromSP(final String s, final boolean b) {
        if (this.mPref != null) {
            return this.mPref.getBoolean(s, b);
        }
        return b;
    }
    
    public void setParamToSP(final String s, final float n) {
        if (this.mEditor != null) {
            this.mEditor.putFloat(s, n);
            this.mEditor.apply();
        }
    }
    
    public void setParamToSP(final String s, final int n) {
        if (this.mEditor != null) {
            this.mEditor.putInt(s, n);
            this.mEditor.apply();
        }
    }
    
    public void setParamToSP(final String s, final String s2) {
        if (this.mEditor != null) {
            this.mEditor.putString(s, s2);
            this.mEditor.apply();
        }
    }
    
    public void setParamToSP(final String s, final boolean b) {
        if (this.mEditor != null) {
            this.mEditor.putBoolean(s, b);
            this.mEditor.apply();
        }
    }
}
