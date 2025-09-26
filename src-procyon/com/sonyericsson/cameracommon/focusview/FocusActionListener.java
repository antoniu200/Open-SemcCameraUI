// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

import android.graphics.Point;

public interface FocusActionListener
{
    void onCanceled();
    
    void onFaceSelected(final Point p0);
    
    void onLongPressed();
    
    void onReleased();
    
    void onTouched();
}
