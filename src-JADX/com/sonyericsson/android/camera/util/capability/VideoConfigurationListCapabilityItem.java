package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;
import java.util.Collections;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VideoConfigurationListCapabilityItem extends CapabilityItem<List<VideoConfiguration>> {
    VideoConfigurationListCapabilityItem(String str, List<VideoConfiguration> list) {
        super(str, list);
    }

    VideoConfigurationListCapabilityItem(String str, SharedPreferences sharedPreferences) {
        super(str, sharedPreferences);
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public List<VideoConfiguration> read(SharedPreferences sharedPreferences, String str) {
        if (sharedPreferences.contains(str)) {
            return SharedPrefsTranslator.getVideoConfigurationList(sharedPreferences.getString(str, ""));
        }
        return Collections.emptyList();
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public void write(SharedPreferences.Editor editor) {
        List<VideoConfiguration> list = get();
        if (list != null) {
            editor.putString(getName(), SharedPrefsTranslator.fromVideoConfigurationList(list));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public List<VideoConfiguration> getDefaultValue() {
        return Collections.emptyList();
    }
}
