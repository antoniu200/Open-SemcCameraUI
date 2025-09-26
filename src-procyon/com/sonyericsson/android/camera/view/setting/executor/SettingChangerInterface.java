// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.executor;

import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;

public interface SettingChangerInterface<T>
{
    void changeValue(final TypedSettingItem<T> p0);
}
