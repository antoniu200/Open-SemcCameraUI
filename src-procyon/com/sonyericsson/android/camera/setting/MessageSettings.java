// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

public interface MessageSettings
{
    void clearSavedMessageSettings();
    
    int getDisplayCount(final MessageType p0);
    
    boolean isNeverShow(final MessageType p0);
    
    void save();
    
    void setDisplayCount(final MessageType p0, final int p1);
    
    void setNeverShow(final MessageType p0, final boolean p1);
}
