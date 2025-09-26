// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.capability.VideoConfiguration;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import java.util.List;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.util.capability.ResolutionOptions;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.configuration.Configurations;
import android.graphics.Rect;

public enum VideoSize implements UserSettingValue
{
    private static final VideoSize[] $VALUES;
    
    FOUR_K_UHD_H264(-1, 2131690229, new Rect(0, 0, 3840, 2160), false), 
    FOUR_K_UHD_H265(-1, 2131690229, new Rect(0, 0, 3840, 2160), false), 
    FULL_HD(-1, 2131690234, new Rect(0, 0, 1920, 1080), false), 
    FULL_HD_60FPS(-1, 2131690231, new Rect(0, 0, 1920, 1080), false), 
    HD(-1, 2131690238, new Rect(0, 0, 1280, 720), false), 
    HD_120FPS(-1, -1, new Rect(0, 0, 1280, 720), false), 
    MMS(-1, 2131690239, new Rect(0, 0, 176, 144), true);
    
    public static final String TAG = "VideoSize";
    
    VGA(-1, 2131690243, new Rect(0, 0, 640, 480), false);
    
    private static final int sParameterTextId = 2131690248;
    private final int mIconId;
    private final boolean mIsConstraint;
    private int mTextId;
    private Rect mVideoRect;
    
    static {
        $VALUES = new VideoSize[] { VideoSize.FOUR_K_UHD_H264, VideoSize.FOUR_K_UHD_H265, VideoSize.FULL_HD_60FPS, VideoSize.FULL_HD, VideoSize.HD_120FPS, VideoSize.HD, VideoSize.VGA, VideoSize.MMS };
    }
    
    private VideoSize(final int mIconId, final int mTextId, final Rect mVideoRect, final boolean mIsConstraint) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mVideoRect = mVideoRect;
        this.mIsConstraint = mIsConstraint;
    }
    
    private static boolean equals(final Rect rect, final Rect rect2) {
        return rect.width() == rect2.width() && rect.height() == rect2.height();
    }
    
    private static String findVideoSizeWithConfiguration(final Configurations configurations, final CameraCapabilityList list, final VideoSize[] array, final Storage storage, final Storage.StorageType storageType) {
        final long videoQuality = configurations.getVideoQuality();
        VideoSize videoSize;
        if (videoQuality == 1L && isContents(array, VideoSize.FULL_HD)) {
            videoSize = VideoSize.FULL_HD;
        }
        else if (videoQuality == 5L && isContents(array, VideoSize.HD)) {
            videoSize = VideoSize.HD;
        }
        else if (videoQuality == 0L && isContents(array, VideoSize.MMS)) {
            videoSize = VideoSize.MMS;
        }
        else if (videoQuality == 4L && isContents(array, VideoSize.VGA)) {
            videoSize = VideoSize.VGA;
        }
        else {
            videoSize = null;
        }
        String s;
        if (videoSize == null) {
            s = list.RESOLUTION_CAPABILITY.get().getDefaultVideoSize();
        }
        else {
            VideoSize videoSizeWithRecordTimeMoreThanGuaranteedTime = videoSize;
            if (storage != null) {
                videoSizeWithRecordTimeMoreThanGuaranteedTime = getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, videoSize, array, storage, storageType);
            }
            if (videoSizeWithRecordTimeMoreThanGuaranteedTime != null) {
                s = videoSizeWithRecordTimeMoreThanGuaranteedTime.name();
            }
            else {
                s = list.RESOLUTION_CAPABILITY.get().getDefaultVideoSize();
            }
        }
        return s;
    }
    
    public static VideoSize getDefaultValue(final ActionMode actionMode, final Configurations configurations, final Storage storage, final Storage.StorageType storageType) {
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(actionMode.mCameraId);
        String s;
        if (actionMode.mIsOneShot) {
            s = findVideoSizeWithConfiguration(configurations, cameraCapability, getOptions(actionMode, configurations), storage, storageType);
        }
        else {
            s = cameraCapability.RESOLUTION_CAPABILITY.get().getDefaultVideoSize();
        }
        return valueOf(s);
    }
    
    private static VideoSize[] getExpectedOptions(final String[] array) {
        final ArrayList list = new ArrayList();
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                list.add(Enum.valueOf(VideoSize.class, array[i]));
            }
            return (VideoSize[])list.toArray(new VideoSize[0]);
        }
        return values();
    }
    
    public static VideoSize[] getOptions(final ActionMode actionMode, final Configurations configurations) {
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(actionMode.mCameraId);
        final List list = cameraCapability.VIDEO_CONFIGURATION.get();
        final boolean fullHdVideoFpsSupported = PlatformCapability.isFullHdVideoFpsSupported(actionMode.mCameraId, RecordingProfile.getVideoFrameRate(VideoSize.FULL_HD_60FPS, VideoHdr.HDR_OFF));
        final VideoSize[] expectedOptions = getExpectedOptions(cameraCapability.RESOLUTION_CAPABILITY.get().getVideoSizeOptions());
        final ArrayList list2 = new ArrayList();
        for (final VideoSize videoSize : expectedOptions) {
            for (final VideoConfiguration videoConfiguration : list) {
                if (equals(videoSize.mVideoRect, new Rect(0, 0, videoConfiguration.mWidth, videoConfiguration.mHeight))) {
                    switch (VideoSize$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$VideoSize[videoSize.ordinal()]) {
                        case 3: {
                            list2.add(videoSize);
                            continue;
                        }
                        case 4: {
                            list2.add(videoSize);
                            continue;
                        }
                        case 5: {
                            list2.add(videoSize);
                        }
                        case 1: {
                            continue;
                        }
                        default: {
                            list2.add(videoSize);
                            continue;
                        }
                        case 2: {
                            if ((boolean)fullHdVideoFpsSupported) {
                                list2.add(videoSize);
                                continue;
                            }
                            continue;
                        }
                    }
                }
            }
        }
        if (actionMode.mIsOneShot && (int)configurations.getVideoQuality() == 0) {
            list2.clear();
            list2.add(VideoSize.MMS);
        }
        return (VideoSize[])list2.toArray(new VideoSize[0]);
    }
    
    private static VideoSize getVideoSizeWithRecordTimeMoreThanGuaranteedTime(final Configurations configurations, final VideoSize videoSize, final VideoSize[] array, final Storage storage, final Storage.StorageType storageType) {
        final long n = MaxVideoSize.create(configurations, new RecordingProfile.Builder().videoSize(videoSize).setOneShot(true).build(), storage, storageType).getMaxDuration();
        if (n == configurations.getVideoMaxDurationInMillisecs()) {
            return videoSize;
        }
        if (!isContents(array, videoSize) || n < 3000L) {
            final int n2 = VideoSize$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$VideoSize[videoSize.ordinal()];
            if (n2 == 1) {
                return VideoSize.MMS;
            }
            if (n2 == 3) {
                return getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, VideoSize.HD, array, storage, storageType);
            }
            switch (n2) {
                case 7: {
                    if (isContents(array, VideoSize.MMS)) {
                        return getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, VideoSize.MMS, array, storage, storageType);
                    }
                    break;
                }
                case 6: {
                    if (isContents(array, VideoSize.VGA)) {
                        return getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, VideoSize.VGA, array, storage, storageType);
                    }
                    return getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, VideoSize.MMS, array, storage, storageType);
                }
            }
        }
        return videoSize;
    }
    
    private static boolean isContents(final VideoSize[] array, final VideoSize other) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i].equals(other)) {
                return true;
            }
        }
        return false;
    }
    
    public static final void preload() {
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.VIDEO_SIZE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690248;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
    
    public Rect getVideoRect() {
        return this.mVideoRect;
    }
    
    public boolean is4KVideo() {
        return this.mVideoRect.width() == 3840 && this.mVideoRect.height() == 2160;
    }
    
    public boolean isConstraint() {
        return this.mIsConstraint;
    }
}
