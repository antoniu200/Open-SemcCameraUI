package com.sonyericsson.android.camera.setting;

import android.util.ArrayMap;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ExtraSettings {
    private Map<CapturingMode, List<UserSettingValue>> mSettingMap = new ArrayMap();

    public void set(CapturingMode capturingMode, List<UserSettingValue> list) {
        this.mSettingMap.put(capturingMode, list);
    }

    public Map<CapturingMode, List<UserSettingValue>> getValues() {
        return this.mSettingMap;
    }

    public void clearValue(CapturingMode capturingMode) {
        this.mSettingMap.remove(capturingMode);
    }

    public void clearAll() {
        this.mSettingMap.clear();
    }
}
