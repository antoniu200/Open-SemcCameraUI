// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration;

import android.view.KeyCharacterMap;

public class Configurations
{
    public static final String TAG = "Configurations";
    static final boolean sHasShutterKey;
    private IntentReader.VideoQualityConfigurations mVideoConfig;
    
    static {
        sHasShutterKey = KeyCharacterMap.deviceHasKey(27);
    }
    
    public static final void preload() {
    }
    
    public long getVideoMaxDurationInMillisecs() {
        return this.mVideoConfig.maxDuration;
    }
    
    public long getVideoMaxFileSizeInBytes() {
        return this.mVideoConfig.maxFileSize;
    }
    
    public long getVideoQuality() {
        return this.mVideoConfig.quality;
    }
    
    public boolean hasLimitForSizeOrDuration() {
        return this.mVideoConfig.hasSizeLimit;
    }
    
    public void initInSync(final IntentReader.VideoQualityConfigurations mVideoConfig) {
        this.mVideoConfig = mVideoConfig;
    }
}
