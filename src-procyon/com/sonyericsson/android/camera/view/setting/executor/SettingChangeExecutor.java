// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.executor;

import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;

public class SettingChangeExecutor<T> implements SettingExecutorInterface<T>
{
    private final SettingChangerInterface<T> mSettingChanger;
    
    public SettingChangeExecutor(final SettingChangerInterface<T> mSettingChanger) {
        this.mSettingChanger = mSettingChanger;
    }
    
    @Override
    public void onExecute(final TypedSettingItem<T> typedSettingItem) {
        this.mSettingChanger.changeValue(typedSettingItem);
    }
}
