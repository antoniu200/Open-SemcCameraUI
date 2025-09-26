// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;

public interface CameraStatusValue
{
    String getKey();
    
    String getValueForDebug();
    
    int minRequiredVersion();
    
    void putInto(final ContentValues p0, final String p1);
}
