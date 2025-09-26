// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder;

public interface ViewFinderInterface
{
    boolean isHeadUpDisplayReady();
    
    void onCaptureDone();
    
    void onShutterDone(final boolean p0);
}
