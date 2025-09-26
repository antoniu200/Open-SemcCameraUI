// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;

public class StringCapabilityItem extends CapabilityItem<String>
{
    StringCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    StringCapabilityItem(final String s, final String s2) {
        super(s, s2);
    }
    
    @Override
    String getDefaultValue() {
        return "";
    }
    
    public String read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return sharedPreferences.getString(s, "");
        }
        return "";
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final String s = this.get();
        if (s != null) {
            sharedPreferences$Editor.putString(this.getName(), s);
        }
    }
}
