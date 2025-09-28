package com.sonyericsson.android.camera.configuration;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum UserSettingKey {
    AUTO_REVIEW(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_photo_autoreview_txt),
    VIDEO_AUTO_REVIEW(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_preview_duration_title_txt),
    CAPTURING_MODE(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_capturing_mode_title_txt),
    DESTINATION_TO_SAVE(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_save_destination_title_txt),
    EV(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_exposure_level_title_txt),
    FACING(false, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_camera_switching_txt),
    SETTING_MENU(false, false, false, ParameterCategory.COMMON, -1),
    FAST_CAPTURE(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_launch_with_camera_key_txt),
    FLASH(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_flash_title_txt),
    DISPLAY_FLASH(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_flash_title_txt),
    FOCUS_MODE(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_focus_mode_title_txt),
    GEO_TAG(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_geotagging_title_txt),
    HDR(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_hdr_title_txt),
    ISO(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_iso_title_txt),
    METERING(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_photometry_title_txt),
    MICROPHONE(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_microphone_title_txt),
    PHOTO_LIGHT(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_flash_torch_txt),
    RESOLUTION(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_image_size_txt),
    ASPECT_RATIO(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_aspect_ratio_txt),
    SELF_TIMER(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_self_timer_txt),
    SHUTTER_SOUND(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_camera_sound_txt),
    SMILE_CAPTURE(false, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_smile_shutter_title_cy_txt),
    CAMERA_KEY(true, false, true, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_camera_key_setting_txt),
    SOFT_SKIN(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_soft_skin_effect_cy_txt),
    VIDEO_STABILIZER(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_steadyshot_cy_txt),
    SUPER_RESOLUTION(false, false, false, ParameterCategory.CAPTURING_MODE, -1),
    TOUCH_CAPTURE(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_touch_capturing_title_txt),
    VIDEO_SELF_TIMER(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_self_timer_txt),
    VIDEO_SIZE(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_video_size_txt),
    VIDEO_HDR(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_video_hdr_setting_hlg_txt),
    VIDEO_SMILE_CAPTURE(false, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_video_smile_shutter_txt),
    VIDEO_SHUTTER_TRIGGER(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_video_shutter_trigger_txt),
    VOLUME_KEY(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_volumekey_txt),
    WHITE_BALANCE(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_white_balance_title_txt),
    SEMI_AUTO(false, false, false, ParameterCategory.CAPTURING_MODE, 0),
    GRID_LINE(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_gridline_txt),
    SIDE_SENSE(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_side_touch_txt),
    HELP_GUIDE(false, false, true, ParameterCategory.COMMON, R.string.cam_strings_help_txt),
    RESET_SETTINGS(false, false, true, ParameterCategory.COMMON, R.string.cam_strings_reset_settings_txt),
    VIDEO_CODEC(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_video_resolution_4k_file_type_txt),
    OBJECT_TRACKING(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_focus_mode_object_tracking_txt),
    SHUTTER_TRIGGER(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_shutter_trigger_txt),
    SHUTTER_SPEED(true, false, false, ParameterCategory.CAPTURING_MODE, 0),
    FOCUS_RANGE(true, false, false, ParameterCategory.CAPTURING_MODE, 0),
    TOUCH_INTENTION(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_touch_to_focus_adjust_exposure_txt),
    PREDICTIVE_CAPTURE(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_predictive_capture_txt),
    SLOW_MOTION(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_slow_motion_txt),
    FRONT_ANGLE(true, false, true, ParameterCategory.COMMON, -1),
    FUSION_MODE(true, false, false, ParameterCategory.CAPTURING_MODE, R.string.cam_strings_fusion_setting_txt),
    DISTORTION_CORRECTION(true, false, true, ParameterCategory.COMMON, R.string.cam_strings_lens_correction_for_photo_txt),
    PREDICTIVE_LAUNCH(true, true, true, ParameterCategory.COMMON, R.string.cam_strings_predictive_launch_txt);

    public static final String TAG = "UserSettingKey";
    private final ParameterCategory mCategory;
    private final boolean mIsCommon;
    private boolean mIsSaved;
    private final boolean mIsSecureSetting;
    private UserSettingSelectability mSelectability;
    private final int mTitleTextId;

    UserSettingKey(boolean z, boolean z2, boolean z3, ParameterCategory parameterCategory, int i) {
        this.mIsSaved = z;
        this.mIsSecureSetting = z2;
        this.mIsCommon = z3;
        this.mCategory = parameterCategory;
        this.mTitleTextId = i;
    }

    public boolean isSaved() {
        return this.mIsSaved;
    }

    public void setSaved(boolean z) {
        this.mIsSaved = z;
    }

    public boolean isSecureSetting() {
        return this.mIsSecureSetting;
    }

    public boolean isCommon() {
        return this.mIsCommon;
    }

    public ParameterCategory getCategory() {
        return this.mCategory;
    }

    public int getTitleTextId() {
        switch (this) {
            case FLASH:
                return Flash.getParameterKeyTitleTextId();
            case DISPLAY_FLASH:
                return DisplayFlash.getParameterKeyTitleTextId();
            case VIDEO_STABILIZER:
                return VideoStabilizer.getParameterKeyTitleText();
            default:
                return this.mTitleTextId;
        }
    }

    public void setSelectability(UserSettingSelectability userSettingSelectability) {
        this.mSelectability = userSettingSelectability;
    }

    public UserSettingSelectability getSelectability() {
        return this.mSelectability;
    }

    public DialogId getRestrictMessageDialogId(UserSettings userSettings) {
        DialogId dialogId = DialogId.DLG_INVALID;
        VideoHdr videoHdr = (VideoHdr) userSettings.get(VIDEO_HDR);
        CapturingMode capturingMode = (CapturingMode) userSettings.get(CAPTURING_MODE);
        switch (this) {
            case VIDEO_STABILIZER:
            case VIDEO_SHUTTER_TRIGGER:
            case VIDEO_CODEC:
                if (videoHdr == VideoHdr.HDR_ON) {
                    return DialogId.VIDEO_HDR_RESTRICTION;
                }
                return DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
            case ISO:
            case METERING:
            case SHUTTER_TRIGGER:
            default:
                return dialogId;
            case OBJECT_TRACKING:
                if (videoHdr == VideoHdr.HDR_ON) {
                    return DialogId.VIDEO_HDR_RESTRICTION;
                }
                return capturingMode.isVideo() ? DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION : dialogId;
            case FUSION_MODE:
                if (videoHdr == VideoHdr.HDR_ON) {
                    return DialogId.VIDEO_HDR_RESTRICTION;
                }
                if (capturingMode.isVideo()) {
                    return DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
                }
                return capturingMode == CapturingMode.NORMAL ? DialogId.RESTRICT_PHOTO_RESOLUTION : dialogId;
            case VIDEO_SIZE:
                if (videoHdr == VideoHdr.HDR_ON) {
                    return DialogId.VIDEO_HDR_RESTRICTION;
                }
                if (capturingMode == CapturingMode.SLOW_MOTION) {
                    return DialogId.RESTRICT_SLOW_VIDEO_RESOLUTION;
                }
                return DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
            case VIDEO_HDR:
                return DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
        }
    }

    public boolean isSelectable() {
        return getSelectability() == UserSettingSelectability.SELECTABLE;
    }

    public boolean isInvalid() {
        return getSelectability() == UserSettingSelectability.INVALID;
    }
}
