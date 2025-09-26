// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.onscreenbutton;

import android.view.MotionEvent;

public interface OnScreenButtonListener
{
    void onCancel(final OnScreenButton p0, final MotionEvent p1);
    
    void onDown(final OnScreenButton p0, final MotionEvent p1);
    
    void onLongPress(final OnScreenButton p0);
    
    void onMove(final OnScreenButton p0, final MotionEvent p1);
    
    void onUp(final OnScreenButton p0, final MotionEvent p1);
}
