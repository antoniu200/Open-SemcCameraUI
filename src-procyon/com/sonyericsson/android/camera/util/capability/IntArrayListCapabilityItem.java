// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import java.util.Collections;
import android.content.SharedPreferences;
import java.util.List;

public class IntArrayListCapabilityItem extends CapabilityItem<List<int[]>>
{
    IntArrayListCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    IntArrayListCapabilityItem(final String s, final List<int[]> list) {
        super(s, list);
    }
    
    @Override
    List<int[]> getDefaultValue() {
        return Collections.emptyList();
    }
    
    public List<int[]> read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return SharedPrefsTranslator.getIntArrayList(sharedPreferences.getString(s, ""));
        }
        return Collections.emptyList();
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final List list = ((CapabilityItem<List>)this).get();
        if (list != null) {
            sharedPreferences$Editor.putString(this.getName(), SharedPrefsTranslator.fromIntArrayList(list));
        }
    }
}
