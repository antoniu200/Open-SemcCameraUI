// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import android.util.ArrayMap;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import java.util.List;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import java.util.Map;

public class ExtraSettings
{
    private Map<CapturingMode, List<UserSettingValue>> mSettingMap;
    
    public ExtraSettings() {
        this.mSettingMap = (Map<CapturingMode, List<UserSettingValue>>)new ArrayMap();
    }
    
    public void clearAll() {
        this.mSettingMap.clear();
    }
    
    public void clearValue(final CapturingMode capturingMode) {
        this.mSettingMap.remove(capturingMode);
    }
    
    public Map<CapturingMode, List<UserSettingValue>> getValues() {
        return this.mSettingMap;
    }
    
    public void set(final CapturingMode capturingMode, final List<UserSettingValue> list) {
        this.mSettingMap.put(capturingMode, list);
    }
}
