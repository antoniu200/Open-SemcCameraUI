package com.sonyericsson.android.camera.view.setting;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ContextualSettingList {
    private final Group mAuto;
    private final Group mManual;
    private final Group mOneShotPhoto;
    private final Group mOneShotVideo;
    private final Group mSlowMotion;
    private final Group mVideo;

    public ContextualSettingList(boolean z) {
        Category category = category(R.string.cam_strings_more_common_setting_title_txt, UserSettingKey.GEO_TAG, UserSettingKey.TOUCH_CAPTURE, UserSettingKey.GRID_LINE, UserSettingKey.AUTO_REVIEW, UserSettingKey.CAMERA_KEY, UserSettingKey.VOLUME_KEY, UserSettingKey.SHUTTER_SOUND, UserSettingKey.DESTINATION_TO_SAVE, UserSettingKey.PREDICTIVE_LAUNCH, UserSettingKey.FAST_CAPTURE, UserSettingKey.HELP_GUIDE, UserSettingKey.RESET_SETTINGS);
        Category category2 = category(R.string.cam_strings_more_common_setting_title_txt, new UserSettingKey[0]);
        this.mAuto = group(category(R.string.cam_strings_capturing_mode_auto_txt, UserSettingKey.ISO, UserSettingKey.RESOLUTION, UserSettingKey.PREDICTIVE_CAPTURE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.METERING, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.SOFT_SKIN, UserSettingKey.FUSION_MODE, UserSettingKey.DISTORTION_CORRECTION, UserSettingKey.SIDE_SENSE), category);
        this.mOneShotPhoto = group(category(R.string.cam_strings_capturing_mode_auto_txt, UserSettingKey.ISO, UserSettingKey.RESOLUTION, UserSettingKey.OBJECT_TRACKING, UserSettingKey.METERING, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.SOFT_SKIN, UserSettingKey.FUSION_MODE, UserSettingKey.DISTORTION_CORRECTION, UserSettingKey.SIDE_SENSE), category2);
        this.mManual = group(category(R.string.cam_strings_capturing_mode_manual_txt, UserSettingKey.RESOLUTION, UserSettingKey.TOUCH_INTENTION, UserSettingKey.METERING, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.SOFT_SKIN, UserSettingKey.DISTORTION_CORRECTION), category);
        this.mSlowMotion = group(category(R.string.cam_strings_capturing_mode_slow_motion_txt, UserSettingKey.VIDEO_SIZE, UserSettingKey.SLOW_MOTION), category);
        if (z) {
            this.mVideo = group(category(R.string.cam_strings_application_name_video_txt, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.VIDEO_CODEC, UserSettingKey.SIDE_SENSE), category);
            this.mOneShotVideo = group(category(R.string.cam_strings_application_name_video_txt, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.VIDEO_CODEC, UserSettingKey.SIDE_SENSE), category2);
        } else {
            this.mVideo = group(category(R.string.cam_strings_application_name_video_txt, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.SIDE_SENSE), category);
            this.mOneShotVideo = group(category(R.string.cam_strings_application_name_video_txt, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.SIDE_SENSE), category2);
        }
    }

    public Group get(CapturingMode capturingMode, boolean z) {
        switch (capturingMode) {
            case SCENE_RECOGNITION:
            case SUPERIOR_FRONT:
                return z ? this.mOneShotPhoto : this.mAuto;
            case NORMAL:
            case FRONT_PHOTO:
                return this.mManual;
            case VIDEO:
            case FRONT_VIDEO:
                return z ? this.mOneShotVideo : this.mVideo;
            case SLOW_MOTION:
                return this.mSlowMotion;
            default:
                throw new IllegalArgumentException("The specified mode is not supported. mode:" + capturingMode.name());
        }
    }

    public static class Category {
        public final UserSettingKey[] keys;
        public final int titleResource;

        public Category(int i, UserSettingKey... userSettingKeyArr) {
            this.titleResource = i;
            this.keys = userSettingKeyArr;
        }
    }

    public static class Group {
        public final Category common;
        public final Category priorityHigh;

        public Group(Category category, Category category2) {
            this.priorityHigh = category;
            this.common = category2;
        }
    }

    private static Category category(int i, UserSettingKey... userSettingKeyArr) {
        return new Category(i, userSettingKeyArr);
    }

    private static Group group(Category category, Category category2) {
        return new Group(category, category2);
    }
}
