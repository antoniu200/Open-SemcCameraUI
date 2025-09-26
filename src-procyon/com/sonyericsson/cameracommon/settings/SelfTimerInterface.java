// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.settings;

import com.sonyericsson.cameracommon.sound.SoundPlayer;

public interface SelfTimerInterface
{
    int getCountDownIconId();
    
    int getDurationInMillisecond();
    
    SoundPlayer.Type getSoundType();
}
