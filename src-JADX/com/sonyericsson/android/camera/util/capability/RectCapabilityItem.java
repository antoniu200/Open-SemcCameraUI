package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;
import android.graphics.Rect;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RectCapabilityItem extends CapabilityItem<Rect> {
    RectCapabilityItem(String str, Rect rect) {
        super(str, rect);
    }

    RectCapabilityItem(String str, SharedPreferences sharedPreferences) {
        super(str, sharedPreferences);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Rect read(SharedPreferences sharedPreferences, String str) {
        if (sharedPreferences.contains(str)) {
            return SharedPrefsTranslator.getRect(sharedPreferences.getString(str, ""));
        }
        return new Rect();
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public void write(SharedPreferences.Editor editor) {
        Rect rect = get();
        if (rect != null) {
            editor.putString(getName(), SharedPrefsTranslator.fromRect(rect));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Rect getDefaultValue() {
        return new Rect();
    }
}
