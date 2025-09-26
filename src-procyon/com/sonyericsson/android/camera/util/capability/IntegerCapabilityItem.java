// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;

public class IntegerCapabilityItem extends CapabilityItem<Integer>
{
    IntegerCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    IntegerCapabilityItem(final String s, final Integer n) {
        super(s, n);
    }
    
    @Override
    Integer getDefaultValue() {
        return 0;
    }
    
    public Integer read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return sharedPreferences.getInt(s, 0);
        }
        return 0;
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final Integer n = this.get();
        if (n != null) {
            sharedPreferences$Editor.putInt(this.getName(), (int)n);
        }
    }
}
