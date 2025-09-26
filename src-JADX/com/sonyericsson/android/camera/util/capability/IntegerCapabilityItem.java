package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IntegerCapabilityItem extends CapabilityItem<Integer> {
    IntegerCapabilityItem(String str, Integer num) {
        super(str, num);
    }

    IntegerCapabilityItem(String str, SharedPreferences sharedPreferences) {
        super(str, sharedPreferences);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Integer read(SharedPreferences sharedPreferences, String str) {
        if (sharedPreferences.contains(str)) {
            return Integer.valueOf(sharedPreferences.getInt(str, 0));
        }
        return 0;
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public void write(SharedPreferences.Editor editor) {
        Integer num = get();
        if (num != null) {
            editor.putInt(getName(), num.intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Integer getDefaultValue() {
        return 0;
    }
}
