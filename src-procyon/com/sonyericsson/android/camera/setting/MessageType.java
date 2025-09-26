// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

public enum MessageType
{
    private static final MessageType[] $VALUES;
    
    BATTERY_WARNING("BATTERY_WARNING_DISABLED_", true), 
    EXTRA_HINT_STARTUP_COUNT_KEY("EXTRA_HINT_STARTUP_COUNT_KEY", false), 
    FOURK_STORAGE_EXPLANATORY_FOR_DESTINATION("FOURK_STORAGE_EXPLANATORY_FOR_DESTINATION_DISABLED", false), 
    FOURK_STORAGE_EXPLANATORY_FOR_VIDEOSIZE("FOURK_STORAGE_EXPLANATORY_FOR_VIDEOSIZE_DISABLED", false), 
    NO_MESSAGE("", false), 
    PREDICTIVE_LAUNCH_DESCRIPTION("do-not-show-again-predictive-launch-description", false), 
    SECURITY_CONTEXTUAL_SETTING("security-dialog-contextual_setting-checked", false), 
    SETUP_WIZARD("do-not-show-again-tutorial-setup-wizard", false), 
    STORAGE_EXPLANATORY("STORAGE_EXPLANATORY_DISABLED", false), 
    STORAGE_EXPLANATORY_FOR_SETTING("STORAGE_EXPLANATORY_FOR_SETTINGS_DISABLED", false), 
    THERMAL_FORCE_FINISH("THERMAL_DISABLED", false), 
    THERMAL_NOTE("THERMAL_NOTE_DISABLED", true), 
    THERMAL_WARNING("THERMAL_WARNING_DISABLED_", true), 
    TUTORIAL_EYE_GUIDE("do-not-show-again-tutorial-switch-to-eye-guide", false), 
    TUTORIAL_HAND_SHUTTER("do-not-show-again-tutorial-switch-to-hand-shutter", false), 
    TUTORIAL_MANUAL_FUSION("do-not-show-again-manual-fusion", false), 
    TUTORIAL_STANDARD_SLOW_MOTION("do-not-show-again-standard-slow-motion", false), 
    TUTORIAL_SUPER_SLOW_MOTION("do-not-show-again-super-slow-motion", false), 
    TUTORIAL_SUPER_SLOW_MOTION_SHOT("do-not-show-again-super-slow-motion-shot", false), 
    TUTORIAL_VIDEO_FUSION("do-not-show-again-video-fusion", false), 
    VIDEO_HDR_CAUTION("do-not-show-again-video-hdr", false);
    
    private final boolean mIsPrefix;
    private final String mKey;
    
    static {
        $VALUES = new MessageType[] { MessageType.NO_MESSAGE, MessageType.THERMAL_NOTE, MessageType.FOURK_STORAGE_EXPLANATORY_FOR_VIDEOSIZE, MessageType.FOURK_STORAGE_EXPLANATORY_FOR_DESTINATION, MessageType.STORAGE_EXPLANATORY, MessageType.STORAGE_EXPLANATORY_FOR_SETTING, MessageType.BATTERY_WARNING, MessageType.SECURITY_CONTEXTUAL_SETTING, MessageType.THERMAL_WARNING, MessageType.THERMAL_FORCE_FINISH, MessageType.EXTRA_HINT_STARTUP_COUNT_KEY, MessageType.SETUP_WIZARD, MessageType.TUTORIAL_EYE_GUIDE, MessageType.TUTORIAL_HAND_SHUTTER, MessageType.TUTORIAL_SUPER_SLOW_MOTION, MessageType.TUTORIAL_SUPER_SLOW_MOTION_SHOT, MessageType.TUTORIAL_STANDARD_SLOW_MOTION, MessageType.TUTORIAL_MANUAL_FUSION, MessageType.TUTORIAL_VIDEO_FUSION, MessageType.VIDEO_HDR_CAUTION, MessageType.PREDICTIVE_LAUNCH_DESCRIPTION };
    }
    
    private MessageType(final String mKey, final boolean mIsPrefix) {
        this.mKey = mKey;
        this.mIsPrefix = mIsPrefix;
    }
    
    String getKey() {
        return this.mKey;
    }
    
    boolean isPrefix() {
        return this.mIsPrefix;
    }
}
