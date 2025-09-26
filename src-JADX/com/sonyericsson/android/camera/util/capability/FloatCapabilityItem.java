package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FloatCapabilityItem extends CapabilityItem<Float> {
    FloatCapabilityItem(String str, Float f) {
        super(str, f);
    }

    FloatCapabilityItem(String str, SharedPreferences sharedPreferences) {
        super(str, sharedPreferences);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Float read(SharedPreferences sharedPreferences, String str) {
        if (sharedPreferences.contains(str)) {
            return Float.valueOf(sharedPreferences.getFloat(str, 0.0f));
        }
        return Float.valueOf(0.0f);
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public void write(SharedPreferences.Editor editor) {
        Float f = get();
        if (f != null) {
            editor.putFloat(getName(), f.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Float getDefaultValue() {
        return Float.valueOf(0.0f);
    }
}
