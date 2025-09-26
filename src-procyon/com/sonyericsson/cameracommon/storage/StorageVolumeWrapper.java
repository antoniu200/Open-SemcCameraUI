// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import java.lang.reflect.InvocationTargetException;
import android.os.storage.StorageVolume;

public class StorageVolumeWrapper
{
    private StorageVolume mStorageVolume;
    
    public StorageVolumeWrapper(final StorageVolume mStorageVolume) {
        this.mStorageVolume = mStorageVolume;
    }
    
    public long getMaxFileSize() {
        try {
            return (long)this.mStorageVolume.getClass().getDeclaredMethod("getMaxFileSize", (Class<?>[])new Class[0]).invoke(this.mStorageVolume, new Object[0]);
        }
        catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            return Long.MAX_VALUE;
        }
    }
    
    public String getUuid() {
        String s;
        try {
            s = (String)this.mStorageVolume.getClass().getDeclaredMethod("getUuid", (Class<?>[])new Class[0]).invoke(this.mStorageVolume, new Object[0]);
        }
        catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            ((Throwable)ex).printStackTrace();
            s = null;
        }
        return s;
    }
}
