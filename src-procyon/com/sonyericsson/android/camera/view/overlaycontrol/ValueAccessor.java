// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol;

public interface ValueAccessor<Value>
{
    Value get();
    
    Value reset();
    
    void set(final Value p0);
}
