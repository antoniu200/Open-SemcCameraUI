// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import java.util.Collections;
import android.content.SharedPreferences;
import android.graphics.Rect;
import java.util.List;

public class RectListCapabilityItem extends CapabilityItem<List<Rect>>
{
    RectListCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    RectListCapabilityItem(final String s, final List<Rect> list) {
        super(s, list);
    }
    
    @Override
    List<Rect> getDefaultValue() {
        return Collections.emptyList();
    }
    
    public List<Rect> read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return SharedPrefsTranslator.getRectList(sharedPreferences.getString(s, ""));
        }
        return Collections.emptyList();
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final List list = ((CapabilityItem<List>)this).get();
        if (list != null) {
            sharedPreferences$Editor.putString(this.getName(), SharedPrefsTranslator.fromRectList(list));
        }
    }
}
