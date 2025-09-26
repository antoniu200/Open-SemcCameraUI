package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LongCapabilityItem extends CapabilityItem<Long> {
    LongCapabilityItem(String str, Long l) {
        super(str, l);
    }

    LongCapabilityItem(String str, SharedPreferences sharedPreferences) {
        super(str, sharedPreferences);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Long read(SharedPreferences sharedPreferences, String str) {
        if (sharedPreferences.contains(str)) {
            return Long.valueOf(sharedPreferences.getLong(str, 0L));
        }
        return 0L;
    }

    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public void write(SharedPreferences.Editor editor) {
        Long l = get();
        if (l != null) {
            editor.putLong(getName(), l.longValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public Long getDefaultValue() {
        return 0L;
    }
}
