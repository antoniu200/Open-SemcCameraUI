package com.sonyericsson.android.camera.configuration;

import android.view.KeyCharacterMap;
import com.sonyericsson.android.camera.configuration.IntentReader;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Configurations {
    public static final String TAG = "Configurations";
    static final boolean sHasShutterKey = KeyCharacterMap.deviceHasKey(27);
    private IntentReader.VideoQualityConfigurations mVideoConfig;

    public static final void preload() {
    }

    public void initInSync(IntentReader.VideoQualityConfigurations videoQualityConfigurations) {
        this.mVideoConfig = videoQualityConfigurations;
    }

    public long getVideoMaxFileSizeInBytes() {
        return this.mVideoConfig.maxFileSize;
    }

    public long getVideoMaxDurationInMillisecs() {
        return this.mVideoConfig.maxDuration;
    }

    public long getVideoQuality() {
        return this.mVideoConfig.quality;
    }

    public boolean hasLimitForSizeOrDuration() {
        return this.mVideoConfig.hasSizeLimit;
    }
}
