// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;

public class LongCapabilityItem extends CapabilityItem<Long>
{
    LongCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    LongCapabilityItem(final String s, final Long n) {
        super(s, n);
    }
    
    @Override
    Long getDefaultValue() {
        return 0L;
    }
    
    public Long read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return sharedPreferences.getLong(s, 0L);
        }
        return 0L;
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final Long n = this.get();
        if (n != null) {
            sharedPreferences$Editor.putLong(this.getName(), (long)n);
        }
    }
}
