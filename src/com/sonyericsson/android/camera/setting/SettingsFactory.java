package com.sonyericsson.android.camera.setting;

import android.content.Context;
import com.sonyericsson.cameracommon.storage.Storage;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingsFactory {
    public static StoredSettings create(Context context, Storage storage) {
        return new StoredSettingsProxy(context, storage);
    }
}
