// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public interface UserSettingValue
{
    void apply(final UserSettingApplicable p0);
    
    int getIconId();
    
    UserSettingKey getKey();
    
    int getKeyTextId();
    
    String getName();
    
    int getTextId();
    
    String getValue();
}
