package com.sonyericsson.cameracommon.storage;

import android.os.storage.StorageVolume;
import java.lang.reflect.InvocationTargetException;
import kotlin.jvm.internal.LongCompanionObject;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class StorageVolumeWrapper {
    private StorageVolume mStorageVolume;

    public StorageVolumeWrapper(StorageVolume storageVolume) {
        this.mStorageVolume = storageVolume;
    }

    public long getMaxFileSize() {
        try {
            return ((Long) this.mStorageVolume.getClass().getDeclaredMethod("getMaxFileSize", new Class[0]).invoke(this.mStorageVolume, new Object[0])).longValue();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
            return LongCompanionObject.MAX_VALUE;
        }
    }

    public String getUuid() {
        try {
            return (String) this.mStorageVolume.getClass().getDeclaredMethod("getUuid", new Class[0]).invoke(this.mStorageVolume, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
