// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;

public class BooleanCapabilityItem extends CapabilityItem<Boolean>
{
    BooleanCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    BooleanCapabilityItem(final String s, final Boolean b) {
        super(s, b);
    }
    
    @Override
    Boolean getDefaultValue() {
        return false;
    }
    
    public Boolean read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return sharedPreferences.getBoolean(s, false);
        }
        return false;
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final Boolean b = this.get();
        if (b != null) {
            sharedPreferences$Editor.putBoolean(this.getName(), (boolean)b);
        }
    }
}
