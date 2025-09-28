package com.sonyericsson.android.camera.configuration.parameters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.parameter.dependency.DependencyCheckUtil;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.util.capability.VideoConfiguration;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum VideoStabilizer implements UserSettingValue {
    STEADY_SHOT(-1, R.string.cam_strings_standard_txt, "on"),
    INTELLIGENT_ACTIVE(-1, R.string.cam_strings_core_intelligentactive_txt, CameraParameters.VS_ON_INTELLIGENT_ACTIVE),
    ON(-1, R.string.cam_strings_settings_on_txt, "on"),
    OFF(-1, R.string.cam_strings_settings_off_txt, "off");

    public static final String TAG = "VideoStabilizer";
    private static final int TEXT_ID_SS = 2131690170;
    private static final int TEXT_ID_VS = 2131690250;
    private static final String VIDEOSTABILIZER_TYPE_INTELLIGENT_ACTIVE = "INTELLIGENT_ACTIVE";
    private static final String VIDEOSTABILIZER_TYPE_OFF = "OFF";
    private static final String VIDEOSTABILIZER_TYPE_STEADY_SHOT = "STEADY_SHOT";
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    public static int getParameterKeyTitleText() {
        return 2131690170;
    }

    public static final void preload() {
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690170;
    }

    VideoStabilizer(int i, int i2, String str) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mValue = str;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.VIDEO_STABILIZER;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return this.mValue;
    }

    public static VideoStabilizer[] getOptions(CapturingMode capturingMode) {
        return getVideoStabilizerOptions(capturingMode);
    }

    /* renamed from: com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode = new int[CapturingMode.values().length];

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SLOW_MOTION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public static VideoStabilizer[] getVideoStabilizerOptions(CapturingMode capturingMode) {
        if (AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()] == 1) {
            return new VideoStabilizer[]{OFF};
        }
        CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
        ArrayList arrayList = new ArrayList();
        if (cameraCapability.VIDEO_STABILIZER.get().contains(CameraParameters.VS_ON_INTELLIGENT_ACTIVE)) {
            arrayList.add(INTELLIGENT_ACTIVE);
        }
        if (cameraCapability.VIDEO_STABILIZER.get().contains("on")) {
            arrayList.add(STEADY_SHOT);
        }
        arrayList.add(OFF);
        return (VideoStabilizer[]) arrayList.toArray(new VideoStabilizer[0]);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public static VideoStabilizer getRecommendedVideoStabilizerValue(Context context, CapturingMode capturingMode, VideoSize videoSize) throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("getRecommendedVideoStabilizerValue() mode:" + capturingMode.name() + " size:" + videoSize.name());
        }
        if (AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()] == 1) {
            return OFF;
        }
        if (capturingMode.isFront()) {
            if (CamLog.VERBOSE) {
                CamLog.d("getRecommendedVideoStabilizerValue() size:" + videoSize.name());
            }
            Rect maxPixelsPictureSize = PlatformCapability.getMaxPixelsPictureSize(capturingMode.getCameraId());
            if (maxPixelsPictureSize.width() == 4160 && maxPixelsPictureSize.height() == 3120 && isIntelligentActiveSupported(capturingMode.getCameraId(), videoSize)) {
                return INTELLIGENT_ACTIVE;
            }
            if (isSteadyShotSupported(capturingMode.getCameraId(), videoSize)) {
                return STEADY_SHOT;
            }
        } else {
            String string = context.getResources().getString(R.string.default_backcamera_videostabilizer_setting);
            if (CamLog.VERBOSE) {
                CamLog.d("getRecommendedVideoStabilizerValue() mode:" + capturingMode.name() + " size:" + videoSize.name() + " default:" + string);
            }
            if (VIDEOSTABILIZER_TYPE_INTELLIGENT_ACTIVE.equals(string)) {
                if (isIntelligentActiveSupported(capturingMode.getCameraId(), videoSize)) {
                    return INTELLIGENT_ACTIVE;
                }
                if (isSteadyShotSupported(capturingMode.getCameraId(), videoSize)) {
                    return STEADY_SHOT;
                }
            } else if (VIDEOSTABILIZER_TYPE_STEADY_SHOT.equals(string) && isSteadyShotSupported(capturingMode.getCameraId(), videoSize)) {
                return STEADY_SHOT;
            }
        }
        return OFF;
    }

    public boolean isValueEnabled(CameraInfo.CameraId cameraId, VideoSize videoSize, VideoHdr videoHdr) {
        if (this == INTELLIGENT_ACTIVE) {
            return DependencyCheckUtil.isIntelligentActiveAvailable(cameraId, videoSize, videoHdr);
        }
        if (this == STEADY_SHOT) {
            return isSteadyShotSupported(cameraId, videoSize);
        }
        return true;
    }

    public static boolean isSteadyShotSupported(CameraInfo.CameraId cameraId, VideoSize videoSize) {
        for (VideoConfiguration videoConfiguration : PlatformCapability.getCameraCapability(cameraId).STEADY_SHOT_CONFIGURATION.get()) {
            if (videoSize.getVideoRect().width() == videoConfiguration.mWidth && videoSize.getVideoRect().height() == videoConfiguration.mHeight && RecordingProfile.getVideoFrameRate(videoSize, VideoHdr.HDR_OFF) <= videoConfiguration.mFps) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIntelligentActiveSupported(CameraInfo.CameraId cameraId, VideoSize videoSize) {
        for (VideoConfiguration videoConfiguration : PlatformCapability.getCameraCapability(cameraId).INTELLIGENT_ACTIVE_CONFIGURATION.get()) {
            if (videoSize.getVideoRect().width() == videoConfiguration.mWidth && videoSize.getVideoRect().height() == videoConfiguration.mHeight && RecordingProfile.getVideoFrameRate(videoSize, VideoHdr.HDR_OFF) <= videoConfiguration.mFps) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIntelligentActive(VideoStabilizer videoStabilizer) {
        return videoStabilizer == INTELLIGENT_ACTIVE;
    }
}
