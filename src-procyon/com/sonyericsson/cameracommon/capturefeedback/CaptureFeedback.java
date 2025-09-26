// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.capturefeedback;

import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;

public interface CaptureFeedback
{
    void onPause();
    
    void onResume();
    
    void release();
    
    void start(final CaptureFeedbackAnimation p0);
}
