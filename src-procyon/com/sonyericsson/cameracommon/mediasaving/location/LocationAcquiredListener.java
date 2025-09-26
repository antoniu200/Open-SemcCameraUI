// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.location;

public interface LocationAcquiredListener
{
    void onAcquired(final boolean p0, final boolean p1);
    
    void onDisabled();
    
    void onLost();
}
