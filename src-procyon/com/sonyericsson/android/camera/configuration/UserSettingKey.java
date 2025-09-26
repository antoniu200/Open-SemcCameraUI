// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration;

import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.android.camera.setting.UserSettings;

public enum UserSettingKey
{
    private static final UserSettingKey[] $VALUES;
    
    ASPECT_RATIO(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689641), 
    AUTO_REVIEW(true, false, true, ParameterCategory.COMMON, 2131689980), 
    CAMERA_KEY(true, false, true, ParameterCategory.CAPTURING_MODE, 2131689663), 
    CAPTURING_MODE(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689686), 
    DESTINATION_TO_SAVE(true, false, true, ParameterCategory.COMMON, 2131690058), 
    DISPLAY_FLASH(true, false, true, ParameterCategory.COMMON, 2131689840), 
    DISTORTION_CORRECTION(true, false, true, ParameterCategory.COMMON, 2131689930), 
    EV(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689811), 
    FACING(false, false, false, ParameterCategory.CAPTURING_MODE, 2131689665), 
    FAST_CAPTURE(true, false, true, ParameterCategory.COMMON, 2131689929), 
    FLASH(true, false, true, ParameterCategory.COMMON, 2131689840), 
    FOCUS_MODE(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689849), 
    FOCUS_RANGE(true, false, false, ParameterCategory.CAPTURING_MODE, 0), 
    FRONT_ANGLE(true, false, true, ParameterCategory.COMMON, -1), 
    FUSION_MODE(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689855), 
    GEO_TAG(true, false, true, ParameterCategory.COMMON, 2131689856), 
    GRID_LINE(true, false, true, ParameterCategory.COMMON, 2131689865), 
    HDR(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689870), 
    HELP_GUIDE(false, false, true, ParameterCategory.COMMON, 2131689872), 
    ISO(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689927), 
    METERING(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690001), 
    MICROPHONE(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689948), 
    OBJECT_TRACKING(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689847), 
    PHOTO_LIGHT(true, false, true, ParameterCategory.COMMON, 2131689841), 
    PREDICTIVE_CAPTURE(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690005), 
    PREDICTIVE_LAUNCH(true, true, true, ParameterCategory.COMMON, 2131690016), 
    RESET_SETTINGS(false, false, true, ParameterCategory.COMMON, 2131690033), 
    RESOLUTION(true, false, false, ParameterCategory.CAPTURING_MODE, 2131689910), 
    SELF_TIMER(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690113), 
    SEMI_AUTO(false, false, false, ParameterCategory.CAPTURING_MODE, 0), 
    SETTING_MENU(false, false, false, ParameterCategory.COMMON, -1), 
    SHUTTER_SOUND(true, false, true, ParameterCategory.COMMON, 2131689664), 
    SHUTTER_SPEED(true, false, false, ParameterCategory.CAPTURING_MODE, 0), 
    SHUTTER_TRIGGER(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690121), 
    SIDE_SENSE(true, false, true, ParameterCategory.COMMON, 2131690131), 
    SLOW_MOTION(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690158), 
    SMILE_CAPTURE(false, false, false, ParameterCategory.CAPTURING_MODE, 2131690164), 
    SOFT_SKIN(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690167), 
    SUPER_RESOLUTION(false, false, false, ParameterCategory.CAPTURING_MODE, -1);
    
    public static final String TAG = "UserSettingKey";
    
    TOUCH_CAPTURE(true, false, true, ParameterCategory.COMMON, 2131690194), 
    TOUCH_INTENTION(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690198), 
    VIDEO_AUTO_REVIEW(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690020), 
    VIDEO_CODEC(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690228), 
    VIDEO_HDR(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690223), 
    VIDEO_SELF_TIMER(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690113), 
    VIDEO_SHUTTER_TRIGGER(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690244), 
    VIDEO_SIZE(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690248), 
    VIDEO_SMILE_CAPTURE(false, false, false, ParameterCategory.CAPTURING_MODE, 2131690249), 
    VIDEO_STABILIZER(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690170), 
    VOLUME_KEY(true, false, true, ParameterCategory.COMMON, 2131690252), 
    WHITE_BALANCE(true, false, false, ParameterCategory.CAPTURING_MODE, 2131690260);
    
    private final ParameterCategory mCategory;
    private final boolean mIsCommon;
    private boolean mIsSaved;
    private final boolean mIsSecureSetting;
    private UserSettingSelectability mSelectability;
    private final int mTitleTextId;
    
    static {
        $VALUES = new UserSettingKey[] { UserSettingKey.AUTO_REVIEW, UserSettingKey.VIDEO_AUTO_REVIEW, UserSettingKey.CAPTURING_MODE, UserSettingKey.DESTINATION_TO_SAVE, UserSettingKey.EV, UserSettingKey.FACING, UserSettingKey.SETTING_MENU, UserSettingKey.FAST_CAPTURE, UserSettingKey.FLASH, UserSettingKey.DISPLAY_FLASH, UserSettingKey.FOCUS_MODE, UserSettingKey.GEO_TAG, UserSettingKey.HDR, UserSettingKey.ISO, UserSettingKey.METERING, UserSettingKey.MICROPHONE, UserSettingKey.PHOTO_LIGHT, UserSettingKey.RESOLUTION, UserSettingKey.ASPECT_RATIO, UserSettingKey.SELF_TIMER, UserSettingKey.SHUTTER_SOUND, UserSettingKey.SMILE_CAPTURE, UserSettingKey.CAMERA_KEY, UserSettingKey.SOFT_SKIN, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.SUPER_RESOLUTION, UserSettingKey.TOUCH_CAPTURE, UserSettingKey.VIDEO_SELF_TIMER, UserSettingKey.VIDEO_SIZE, UserSettingKey.VIDEO_HDR, UserSettingKey.VIDEO_SMILE_CAPTURE, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VOLUME_KEY, UserSettingKey.WHITE_BALANCE, UserSettingKey.SEMI_AUTO, UserSettingKey.GRID_LINE, UserSettingKey.SIDE_SENSE, UserSettingKey.HELP_GUIDE, UserSettingKey.RESET_SETTINGS, UserSettingKey.VIDEO_CODEC, UserSettingKey.OBJECT_TRACKING, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.SHUTTER_SPEED, UserSettingKey.FOCUS_RANGE, UserSettingKey.TOUCH_INTENTION, UserSettingKey.PREDICTIVE_CAPTURE, UserSettingKey.SLOW_MOTION, UserSettingKey.FRONT_ANGLE, UserSettingKey.FUSION_MODE, UserSettingKey.DISTORTION_CORRECTION, UserSettingKey.PREDICTIVE_LAUNCH };
    }
    
    private UserSettingKey(final boolean mIsSaved, final boolean mIsSecureSetting, final boolean mIsCommon, final ParameterCategory mCategory, final int mTitleTextId) {
        this.mIsSaved = mIsSaved;
        this.mIsSecureSetting = mIsSecureSetting;
        this.mIsCommon = mIsCommon;
        this.mCategory = mCategory;
        this.mTitleTextId = mTitleTextId;
    }
    
    public ParameterCategory getCategory() {
        return this.mCategory;
    }
    
    public DialogId getRestrictMessageDialogId(final UserSettings userSettings) {
        final DialogId dlg_INVALID = DialogId.DLG_INVALID;
        final VideoHdr videoHdr = (VideoHdr)userSettings.get(UserSettingKey.VIDEO_HDR);
        final CapturingMode capturingMode = (CapturingMode)userSettings.get(UserSettingKey.CAPTURING_MODE);
        DialogId dialogId = dlg_INVALID;
        switch (UserSettingKey$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[this.ordinal()]) {
            default: {
                dialogId = dlg_INVALID;
                return dialogId;
            }
            case 11: {
                if (videoHdr == VideoHdr.HDR_ON) {
                    dialogId = DialogId.VIDEO_HDR_RESTRICTION;
                    return dialogId;
                }
                if (capturingMode == CapturingMode.SLOW_MOTION) {
                    dialogId = DialogId.RESTRICT_SLOW_VIDEO_RESOLUTION;
                    return dialogId;
                }
                dialogId = DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
                return dialogId;
            }
            case 10: {
                if (videoHdr == VideoHdr.HDR_ON) {
                    dialogId = DialogId.VIDEO_HDR_RESTRICTION;
                    return dialogId;
                }
                if (capturingMode.isVideo()) {
                    dialogId = DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
                    return dialogId;
                }
                dialogId = dlg_INVALID;
                if (capturingMode == CapturingMode.NORMAL) {
                    dialogId = DialogId.RESTRICT_PHOTO_RESOLUTION;
                    return dialogId;
                }
                return dialogId;
            }
            case 9: {
                if (videoHdr == VideoHdr.HDR_ON) {
                    dialogId = DialogId.VIDEO_HDR_RESTRICTION;
                    return dialogId;
                }
                dialogId = dlg_INVALID;
                if (capturingMode.isVideo()) {
                    dialogId = DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
                    return dialogId;
                }
                return dialogId;
            }
            case 3:
            case 7:
            case 8: {
                if (videoHdr == VideoHdr.HDR_ON) {
                    dialogId = DialogId.VIDEO_HDR_RESTRICTION;
                    return dialogId;
                }
                dialogId = DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
                return dialogId;
            }
            case 4:
            case 5:
            case 6: {
                return dialogId;
            }
            case 12: {
                dialogId = DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
                return dialogId;
            }
        }
    }
    
    public UserSettingSelectability getSelectability() {
        return this.mSelectability;
    }
    
    public int getTitleTextId() {
        switch (UserSettingKey$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[this.ordinal()]) {
            default: {
                return this.mTitleTextId;
            }
            case 3: {
                return VideoStabilizer.getParameterKeyTitleText();
            }
            case 2: {
                return DisplayFlash.getParameterKeyTitleTextId();
            }
            case 1: {
                return Flash.getParameterKeyTitleTextId();
            }
        }
    }
    
    public boolean isCommon() {
        return this.mIsCommon;
    }
    
    public boolean isInvalid() {
        return this.getSelectability() == UserSettingSelectability.INVALID;
    }
    
    public boolean isSaved() {
        return this.mIsSaved;
    }
    
    public boolean isSecureSetting() {
        return this.mIsSecureSetting;
    }
    
    public boolean isSelectable() {
        return this.getSelectability() == UserSettingSelectability.SELECTABLE;
    }
    
    public void setSaved(final boolean mIsSaved) {
        this.mIsSaved = mIsSaved;
    }
    
    public void setSelectability(final UserSettingSelectability mSelectability) {
        this.mSelectability = mSelectability;
    }
}
