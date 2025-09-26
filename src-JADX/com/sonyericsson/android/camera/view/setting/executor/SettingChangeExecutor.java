package com.sonyericsson.android.camera.view.setting.executor;

import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingChangeExecutor<T> implements SettingExecutorInterface<T> {
    private final SettingChangerInterface<T> mSettingChanger;

    public SettingChangeExecutor(SettingChangerInterface<T> settingChangerInterface) {
        this.mSettingChanger = settingChangerInterface;
    }

    @Override // com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface
    public void onExecute(TypedSettingItem<T> typedSettingItem) {
        this.mSettingChanger.changeValue(typedSettingItem);
    }
}
