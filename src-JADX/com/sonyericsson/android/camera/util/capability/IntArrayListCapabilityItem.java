package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;
import java.util.Collections;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IntArrayListCapabilityItem extends CapabilityItem<List<int[]>> {
    IntArrayListCapabilityItem(String str, List<int[]> list) {
        super(str, list);
    }

    IntArrayListCapabilityItem(String str, SharedPreferences sharedPreferences) {
        super(str, sharedPreferences);
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public List<int[]> read(SharedPreferences sharedPreferences, String str) {
        if (sharedPreferences.contains(str)) {
            return SharedPrefsTranslator.getIntArrayList(sharedPreferences.getString(str, ""));
        }
        return Collections.emptyList();
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public void write(SharedPreferences.Editor editor) {
        List<int[]> list = get();
        if (list != null) {
            editor.putString(getName(), SharedPrefsTranslator.fromIntArrayList(list));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public List<int[]> getDefaultValue() {
        return Collections.emptyList();
    }
}
