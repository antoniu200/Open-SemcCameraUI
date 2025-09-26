// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences$Editor;
import java.util.Collections;
import android.content.SharedPreferences;
import java.util.List;

public class VideoConfigurationListCapabilityItem extends CapabilityItem<List<VideoConfiguration>>
{
    VideoConfigurationListCapabilityItem(final String s, final SharedPreferences sharedPreferences) {
        super(s, sharedPreferences);
    }
    
    VideoConfigurationListCapabilityItem(final String s, final List<VideoConfiguration> list) {
        super(s, list);
    }
    
    @Override
    List<VideoConfiguration> getDefaultValue() {
        return Collections.emptyList();
    }
    
    public List<VideoConfiguration> read(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences.contains(s)) {
            return SharedPrefsTranslator.getVideoConfigurationList(sharedPreferences.getString(s, ""));
        }
        return Collections.emptyList();
    }
    
    public void write(final SharedPreferences$Editor sharedPreferences$Editor) {
        final List list = ((CapabilityItem<List>)this).get();
        if (list != null) {
            sharedPreferences$Editor.putString(this.getName(), SharedPrefsTranslator.fromVideoConfigurationList(list));
        }
    }
}
