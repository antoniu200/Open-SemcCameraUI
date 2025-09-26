// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import com.sonyericsson.cameracommon.storage.Storage;
import android.content.Context;

public class SettingsFactory
{
    public static StoredSettings create(final Context context, final Storage storage) {
        return new StoredSettingsProxy(context, storage);
    }
}
