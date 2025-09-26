// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import android.graphics.Rect;

public class RectCapabilityItem extends CapabilityItem<Rect>
{
    RectCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    RectCapabilityItem(final String s, final Rect rect) {
        super(s, rect);
    }
    
    @Override
    Rect getDefaultValue() {
        return new Rect();
    }
    
    public Rect read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return SharedPrefsTranslator.getRect(sharedPreferences.getString(s, ""));
        }
        return new Rect();
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final Rect rect = this.get();
        if (rect != null) {
            sharedPreferences$Editor.putString(this.getName(), SharedPrefsTranslator.fromRect(rect));
        }
    }
}
