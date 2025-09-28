package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BooleanCapabilityItem extends CapabilityItem<Boolean> {
    BooleanCapabilityItem(String str, Boolean bool) {
        super(str, bool);
    }

    BooleanCapabilityItem(String str, SharedPreferences sharedPreferences) {
        super(str, sharedPreferences);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Boolean read(SharedPreferences sharedPreferences, String str) {
        if (sharedPreferences.contains(str)) {
            return Boolean.valueOf(sharedPreferences.getBoolean(str, false));
        }
        return false;
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public void write(SharedPreferences.Editor editor) {
        Boolean bool = get();
        if (bool != null) {
            editor.putBoolean(getName(), bool.booleanValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Boolean getDefaultValue() {
        return false;
    }
}
