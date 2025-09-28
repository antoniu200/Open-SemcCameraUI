package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;
import android.graphics.Rect;
import java.util.Collections;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RectListCapabilityItem extends CapabilityItem<List<Rect>> {
    RectListCapabilityItem(String str, List<Rect> list) {
        super(str, list);
    }

    RectListCapabilityItem(String str, SharedPreferences sharedPreferences) {
        super(str, sharedPreferences);
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public List<Rect> read(SharedPreferences sharedPreferences, String str) {
        if (sharedPreferences.contains(str)) {
            return SharedPrefsTranslator.getRectList(sharedPreferences.getString(str, ""));
        }
        return Collections.emptyList();
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public void write(SharedPreferences.Editor editor) {
        List<Rect> list = get();
        if (list != null) {
            editor.putString(getName(), SharedPrefsTranslator.fromRectList(list));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public List<Rect> getDefaultValue() {
        return Collections.emptyList();
    }
}
