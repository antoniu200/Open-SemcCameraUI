// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;

public class FloatCapabilityItem extends CapabilityItem<Float>
{
    FloatCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    FloatCapabilityItem(final String s, final Float n) {
        super(s, n);
    }
    
    @Override
    Float getDefaultValue() {
        return 0.0f;
    }
    
    public Float read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return sharedPreferences.getFloat(s, 0.0f);
        }
        return 0.0f;
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final Float n = this.get();
        if (n != null) {
            sharedPreferences$Editor.putFloat(this.getName(), (float)n);
        }
    }
}
