// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.parameter.dependency.DependencyCheckUtil;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Iterator;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.util.capability.VideoConfiguration;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import java.util.List;
import java.util.ArrayList;
import android.graphics.Rect;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.Context;

public enum VideoStabilizer implements UserSettingValue
{
    private static final VideoStabilizer[] $VALUES;
    
    INTELLIGENT_ACTIVE(-1, 2131689709, "intelligent_active"), 
    OFF(-1, 2131690115, "off"), 
    ON(-1, 2131690116, "on"), 
    STEADY_SHOT(-1, 2131690169, "on");
    
    public static final String TAG = "VideoStabilizer";
    private static final int TEXT_ID_SS = 2131690170;
    private static final int TEXT_ID_VS = 2131690250;
    private static final String VIDEOSTABILIZER_TYPE_INTELLIGENT_ACTIVE = "INTELLIGENT_ACTIVE";
    private static final String VIDEOSTABILIZER_TYPE_OFF = "OFF";
    private static final String VIDEOSTABILIZER_TYPE_STEADY_SHOT = "STEADY_SHOT";
    private final int mIconId;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new VideoStabilizer[] { VideoStabilizer.STEADY_SHOT, VideoStabilizer.INTELLIGENT_ACTIVE, VideoStabilizer.ON, VideoStabilizer.OFF };
    }
    
    private VideoStabilizer(final int mIconId, final int mTextId, final String mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static VideoStabilizer[] getOptions(final CapturingMode capturingMode) {
        return getVideoStabilizerOptions(capturingMode);
    }
    
    public static int getParameterKeyTitleText() {
        return 2131690170;
    }
    
    public static VideoStabilizer getRecommendedVideoStabilizerValue(final Context context, final CapturingMode capturingMode, final VideoSize videoSize) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getRecommendedVideoStabilizerValue() mode:");
            sb.append(capturingMode.name());
            sb.append(" size:");
            sb.append(videoSize.name());
            CamLog.d(sb.toString());
        }
        if (VideoStabilizer$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()] != 1) {
            if (capturingMode.isFront()) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("getRecommendedVideoStabilizerValue() size:");
                    sb2.append(videoSize.name());
                    CamLog.d(sb2.toString());
                }
                final Rect maxPixelsPictureSize = PlatformCapability.getMaxPixelsPictureSize(capturingMode.getCameraId());
                if (maxPixelsPictureSize.width() == 4160 && maxPixelsPictureSize.height() == 3120 && isIntelligentActiveSupported(capturingMode.getCameraId(), videoSize)) {
                    return VideoStabilizer.INTELLIGENT_ACTIVE;
                }
                if (isSteadyShotSupported(capturingMode.getCameraId(), videoSize)) {
                    return VideoStabilizer.STEADY_SHOT;
                }
            }
            else {
                final String string = context.getResources().getString(2131690315);
                if (CamLog.VERBOSE) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("getRecommendedVideoStabilizerValue() mode:");
                    sb3.append(capturingMode.name());
                    sb3.append(" size:");
                    sb3.append(videoSize.name());
                    sb3.append(" default:");
                    sb3.append(string);
                    CamLog.d(sb3.toString());
                }
                if ("INTELLIGENT_ACTIVE".equals(string)) {
                    if (isIntelligentActiveSupported(capturingMode.getCameraId(), videoSize)) {
                        return VideoStabilizer.INTELLIGENT_ACTIVE;
                    }
                    if (isSteadyShotSupported(capturingMode.getCameraId(), videoSize)) {
                        return VideoStabilizer.STEADY_SHOT;
                    }
                }
                else if ("STEADY_SHOT".equals(string) && isSteadyShotSupported(capturingMode.getCameraId(), videoSize)) {
                    return VideoStabilizer.STEADY_SHOT;
                }
            }
            return VideoStabilizer.OFF;
        }
        return VideoStabilizer.OFF;
    }
    
    public static VideoStabilizer[] getVideoStabilizerOptions(final CapturingMode capturingMode) {
        if (VideoStabilizer$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()] != 1) {
            final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
            final ArrayList list = new ArrayList();
            if (cameraCapability.VIDEO_STABILIZER.get().contains("intelligent_active")) {
                list.add(VideoStabilizer.INTELLIGENT_ACTIVE);
            }
            if (cameraCapability.VIDEO_STABILIZER.get().contains("on")) {
                list.add(VideoStabilizer.STEADY_SHOT);
            }
            list.add(VideoStabilizer.OFF);
            return list.toArray(new VideoStabilizer[0]);
        }
        return new VideoStabilizer[] { VideoStabilizer.OFF };
    }
    
    public static boolean isIntelligentActive(final VideoStabilizer videoStabilizer) {
        return videoStabilizer == VideoStabilizer.INTELLIGENT_ACTIVE;
    }
    
    public static boolean isIntelligentActiveSupported(final CameraInfo.CameraId cameraId, final VideoSize videoSize) {
        for (final VideoConfiguration videoConfiguration : PlatformCapability.getCameraCapability(cameraId).INTELLIGENT_ACTIVE_CONFIGURATION.get()) {
            if (videoSize.getVideoRect().width() == videoConfiguration.mWidth && videoSize.getVideoRect().height() == videoConfiguration.mHeight && RecordingProfile.getVideoFrameRate(videoSize, VideoHdr.HDR_OFF) <= videoConfiguration.mFps) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSteadyShotSupported(final CameraInfo.CameraId cameraId, final VideoSize videoSize) {
        for (final VideoConfiguration videoConfiguration : PlatformCapability.getCameraCapability(cameraId).STEADY_SHOT_CONFIGURATION.get()) {
            if (videoSize.getVideoRect().width() == videoConfiguration.mWidth && videoSize.getVideoRect().height() == videoConfiguration.mHeight && RecordingProfile.getVideoFrameRate(videoSize, VideoHdr.HDR_OFF) <= videoConfiguration.mFps) {
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
        return UserSettingKey.VIDEO_STABILIZER;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690170;
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
        return this.mValue;
    }
    
    public boolean isValueEnabled(final CameraInfo.CameraId cameraId, final VideoSize videoSize, final VideoHdr videoHdr) {
        boolean intelligentActiveAvailable;
        if (this == VideoStabilizer.INTELLIGENT_ACTIVE) {
            intelligentActiveAvailable = DependencyCheckUtil.isIntelligentActiveAvailable(cameraId, videoSize, videoHdr);
        }
        else {
            intelligentActiveAvailable = (this != VideoStabilizer.STEADY_SHOT || isSteadyShotSupported(cameraId, videoSize));
        }
        return intelligentActiveAvailable;
    }
}
