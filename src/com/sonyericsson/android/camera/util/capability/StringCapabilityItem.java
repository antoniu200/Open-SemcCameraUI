package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class StringCapabilityItem extends CapabilityItem<String> {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public String getDefaultValue() {
        return "";
    }

    StringCapabilityItem(String str, String str2) {
        super(str, str2);
    }

    StringCapabilityItem(String str, SharedPreferences sharedPreferences) {
        super(str, sharedPreferences);
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public String read(SharedPreferences sharedPreferences, String str) {
        return sharedPreferences.contains(str) ? sharedPreferences.getString(str, "") : "";
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public void write(SharedPreferences.Editor editor) {
        String str = get();
        if (str != null) {
            editor.putString(getName(), str);
        }
    }
}
