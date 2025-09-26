package com.sonyericsson.android.camera.configuration.parameters;

import android.graphics.Rect;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.util.capability.VideoConfiguration;
import com.sonyericsson.cameracommon.device.SizeConstants;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum VideoSize implements UserSettingValue {
    FOUR_K_UHD_H264(-1, R.string.cam_strings_video_resolution_4k_txt, new Rect(0, 0, 3840, SizeConstants.HEIGHT_PREVIEW_4K_UHD), false),
    FOUR_K_UHD_H265(-1, R.string.cam_strings_video_resolution_4k_txt, new Rect(0, 0, 3840, SizeConstants.HEIGHT_PREVIEW_4K_UHD), false),
    FULL_HD_60FPS(-1, R.string.cam_strings_video_resolution_full_hd_60fps_txt, new Rect(0, 0, SizeConstants.WIDTH_PREVIEW_FULL_HD, SizeConstants.HEIGHT_PREVIEW_FULL_HD), false),
    FULL_HD(-1, R.string.cam_strings_video_resolution_full_hd_txt, new Rect(0, 0, SizeConstants.WIDTH_PREVIEW_FULL_HD, SizeConstants.HEIGHT_PREVIEW_FULL_HD), false),
    HD_120FPS(-1, -1, new Rect(0, 0, SizeConstants.WIDTH_PREVIEW_HD, SizeConstants.HEIGHT_PREVIEW_HD), false),
    HD(-1, R.string.cam_strings_video_resolution_hd_txt, new Rect(0, 0, SizeConstants.WIDTH_PREVIEW_HD, SizeConstants.HEIGHT_PREVIEW_HD), false),
    VGA(-1, R.string.cam_strings_video_resolution_vga_txt, new Rect(0, 0, 640, 480), false),
    MMS(-1, R.string.cam_strings_video_resolution_mms_txt, new Rect(0, 0, 176, 144), true);

    public static final String TAG = "VideoSize";
    private static final int sParameterTextId = 2131690248;
    private final int mIconId;
    private final boolean mIsConstraint;
    private int mTextId;
    private Rect mVideoRect;

    public static final void preload() {
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690248;
    }

    VideoSize(int i, int i2, Rect rect, boolean z) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mVideoRect = rect;
        this.mIsConstraint = z;
    }

    public boolean isConstraint() {
        return this.mIsConstraint;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.VIDEO_SIZE;
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
        return toString();
    }

    public static VideoSize[] getOptions(ActionMode actionMode, Configurations configurations) {
        CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(actionMode.mCameraId);
        List<VideoConfiguration> list = cameraCapability.VIDEO_CONFIGURATION.get();
        Boolean boolValueOf = Boolean.valueOf(PlatformCapability.isFullHdVideoFpsSupported(actionMode.mCameraId, RecordingProfile.getVideoFrameRate(FULL_HD_60FPS, VideoHdr.HDR_OFF)));
        VideoSize[] expectedOptions = getExpectedOptions(cameraCapability.RESOLUTION_CAPABILITY.get().getVideoSizeOptions());
        ArrayList arrayList = new ArrayList();
        for (VideoSize videoSize : expectedOptions) {
            for (VideoConfiguration videoConfiguration : list) {
                if (equals(videoSize.mVideoRect, new Rect(0, 0, videoConfiguration.mWidth, videoConfiguration.mHeight))) {
                    switch (videoSize) {
                        case MMS:
                            break;
                        case FULL_HD_60FPS:
                            if (boolValueOf.booleanValue()) {
                                arrayList.add(videoSize);
                                break;
                            } else {
                                break;
                            }
                        case FULL_HD:
                            arrayList.add(videoSize);
                            break;
                        case FOUR_K_UHD_H264:
                            arrayList.add(videoSize);
                            break;
                        case FOUR_K_UHD_H265:
                            arrayList.add(videoSize);
                            break;
                        default:
                            arrayList.add(videoSize);
                            break;
                    }
                }
            }
        }
        if (actionMode.mIsOneShot && ((int) configurations.getVideoQuality()) == 0) {
            arrayList.clear();
            arrayList.add(MMS);
        }
        return (VideoSize[]) arrayList.toArray(new VideoSize[0]);
    }

    private static boolean equals(Rect rect, Rect rect2) {
        return rect.width() == rect2.width() && rect.height() == rect2.height();
    }

    private static VideoSize[] getExpectedOptions(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        if (strArr != null) {
            for (String str : strArr) {
                arrayList.add(valueOf(VideoSize.class, str));
            }
            return (VideoSize[]) arrayList.toArray(new VideoSize[0]);
        }
        return values();
    }

    public static VideoSize getDefaultValue(ActionMode actionMode, Configurations configurations, Storage storage, Storage.StorageType storageType) {
        String defaultVideoSize;
        CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(actionMode.mCameraId);
        if (actionMode.mIsOneShot) {
            defaultVideoSize = findVideoSizeWithConfiguration(configurations, cameraCapability, getOptions(actionMode, configurations), storage, storageType);
        } else {
            defaultVideoSize = cameraCapability.RESOLUTION_CAPABILITY.get().getDefaultVideoSize();
        }
        return valueOf(defaultVideoSize);
    }

    private static String findVideoSizeWithConfiguration(Configurations configurations, CameraCapabilityList cameraCapabilityList, VideoSize[] videoSizeArr, Storage storage, Storage.StorageType storageType) {
        VideoSize videoSizeWithRecordTimeMoreThanGuaranteedTime;
        long videoQuality = configurations.getVideoQuality();
        if (videoQuality == 1 && isContents(videoSizeArr, FULL_HD)) {
            videoSizeWithRecordTimeMoreThanGuaranteedTime = FULL_HD;
        } else if (videoQuality == 5 && isContents(videoSizeArr, HD)) {
            videoSizeWithRecordTimeMoreThanGuaranteedTime = HD;
        } else if (videoQuality == 0 && isContents(videoSizeArr, MMS)) {
            videoSizeWithRecordTimeMoreThanGuaranteedTime = MMS;
        } else {
            videoSizeWithRecordTimeMoreThanGuaranteedTime = (videoQuality == 4 && isContents(videoSizeArr, VGA)) ? VGA : null;
        }
        if (videoSizeWithRecordTimeMoreThanGuaranteedTime == null) {
            return cameraCapabilityList.RESOLUTION_CAPABILITY.get().getDefaultVideoSize();
        }
        if (storage != null) {
            videoSizeWithRecordTimeMoreThanGuaranteedTime = getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, videoSizeWithRecordTimeMoreThanGuaranteedTime, videoSizeArr, storage, storageType);
        }
        if (videoSizeWithRecordTimeMoreThanGuaranteedTime != null) {
            return videoSizeWithRecordTimeMoreThanGuaranteedTime.name();
        }
        return cameraCapabilityList.RESOLUTION_CAPABILITY.get().getDefaultVideoSize();
    }

    private static boolean isContents(VideoSize[] videoSizeArr, VideoSize videoSize) {
        for (VideoSize videoSize2 : videoSizeArr) {
            if (videoSize2.equals(videoSize)) {
                return true;
            }
        }
        return false;
    }

    private static VideoSize getVideoSizeWithRecordTimeMoreThanGuaranteedTime(Configurations configurations, VideoSize videoSize, VideoSize[] videoSizeArr, Storage storage, Storage.StorageType storageType) {
        long maxDuration = MaxVideoSize.create(configurations, new RecordingProfile.Builder().videoSize(videoSize).setOneShot(true).build(), storage, storageType).getMaxDuration();
        if (maxDuration == configurations.getVideoMaxDurationInMillisecs()) {
            return videoSize;
        }
        if (!isContents(videoSizeArr, videoSize) || maxDuration < MaxVideoSize.GUARANTEED_MIN_DURATION_IN_MILLIS) {
            int i = AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$VideoSize[videoSize.ordinal()];
            if (i == 1) {
                return MMS;
            }
            if (i == 3) {
                return getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, HD, videoSizeArr, storage, storageType);
            }
            switch (i) {
                case 6:
                    if (isContents(videoSizeArr, VGA)) {
                        return getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, VGA, videoSizeArr, storage, storageType);
                    }
                    return getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, MMS, videoSizeArr, storage, storageType);
                case 7:
                    if (isContents(videoSizeArr, MMS)) {
                        return getVideoSizeWithRecordTimeMoreThanGuaranteedTime(configurations, MMS, videoSizeArr, storage, storageType);
                    }
                default:
                    return videoSize;
            }
        }
        return videoSize;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public Rect getVideoRect() {
        return this.mVideoRect;
    }

    public boolean is4KVideo() {
        return this.mVideoRect.width() == 3840 && this.mVideoRect.height() == 2160;
    }
}
