// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.executor;

import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;

public interface SettingExecutorInterface<T>
{
    void onExecute(final TypedSettingItem<T> p0);
}
