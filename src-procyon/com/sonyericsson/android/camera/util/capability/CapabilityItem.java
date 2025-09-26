// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.SharedPreferences;

public abstract class CapabilityItem<T>
{
    public static final String TAG = "CapabilityItem";
    private final String mName;
    private final T mValue;
    
    CapabilityItem(final String mName, final SharedPreferences sharedPreferences) {
        this.mName = mName;
        this.mValue = this.read(sharedPreferences, mName);
    }
    
    CapabilityItem(final String mName, final T t) {
        this.mName = mName;
        this.mValue = t;
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append(" value: ");
            sb.append(t);
            CamLog.d(mName, sb.toString());
        }
    }
    
    public final T get() {
        if (this.mValue == null) {
            return this.getDefaultValue();
        }
        return this.mValue;
    }
    
    abstract T getDefaultValue();
    
    public final String getName() {
        return this.mName;
    }
    
    T read(final SharedPreferences sharedPreferences, final String s) {
        return null;
    }
    
    void write(final SharedPreferences$Editor sharedPreferences$Editor) {
    }
}
