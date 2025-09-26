package com.sonyericsson.android.camera.setting;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum MessageType {
    NO_MESSAGE("", false),
    THERMAL_NOTE("THERMAL_NOTE_DISABLED", true),
    FOURK_STORAGE_EXPLANATORY_FOR_VIDEOSIZE("FOURK_STORAGE_EXPLANATORY_FOR_VIDEOSIZE_DISABLED", false),
    FOURK_STORAGE_EXPLANATORY_FOR_DESTINATION("FOURK_STORAGE_EXPLANATORY_FOR_DESTINATION_DISABLED", false),
    STORAGE_EXPLANATORY("STORAGE_EXPLANATORY_DISABLED", false),
    STORAGE_EXPLANATORY_FOR_SETTING("STORAGE_EXPLANATORY_FOR_SETTINGS_DISABLED", false),
    BATTERY_WARNING("BATTERY_WARNING_DISABLED_", true),
    SECURITY_CONTEXTUAL_SETTING("security-dialog-contextual_setting-checked", false),
    THERMAL_WARNING("THERMAL_WARNING_DISABLED_", true),
    THERMAL_FORCE_FINISH("THERMAL_DISABLED", false),
    EXTRA_HINT_STARTUP_COUNT_KEY("EXTRA_HINT_STARTUP_COUNT_KEY", false),
    SETUP_WIZARD("do-not-show-again-tutorial-setup-wizard", false),
    TUTORIAL_EYE_GUIDE("do-not-show-again-tutorial-switch-to-eye-guide", false),
    TUTORIAL_HAND_SHUTTER("do-not-show-again-tutorial-switch-to-hand-shutter", false),
    TUTORIAL_SUPER_SLOW_MOTION("do-not-show-again-super-slow-motion", false),
    TUTORIAL_SUPER_SLOW_MOTION_SHOT("do-not-show-again-super-slow-motion-shot", false),
    TUTORIAL_STANDARD_SLOW_MOTION("do-not-show-again-standard-slow-motion", false),
    TUTORIAL_MANUAL_FUSION("do-not-show-again-manual-fusion", false),
    TUTORIAL_VIDEO_FUSION("do-not-show-again-video-fusion", false),
    VIDEO_HDR_CAUTION("do-not-show-again-video-hdr", false),
    PREDICTIVE_LAUNCH_DESCRIPTION("do-not-show-again-predictive-launch-description", false);

    private final boolean mIsPrefix;
    private final String mKey;

    MessageType(String str, boolean z) {
        this.mKey = str;
        this.mIsPrefix = z;
    }

    String getKey() {
        return this.mKey;
    }

    boolean isPrefix() {
        return this.mIsPrefix;
    }
}
