// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.UserSettingKey;

public class ContextualSettingList
{
    private final Group mAuto;
    private final Group mManual;
    private final Group mOneShotPhoto;
    private final Group mOneShotVideo;
    private final Group mSlowMotion;
    private final Group mVideo;
    
    public ContextualSettingList(final boolean b) {
        final Category category = category(2131689953, UserSettingKey.GEO_TAG, UserSettingKey.TOUCH_CAPTURE, UserSettingKey.GRID_LINE, UserSettingKey.AUTO_REVIEW, UserSettingKey.CAMERA_KEY, UserSettingKey.VOLUME_KEY, UserSettingKey.SHUTTER_SOUND, UserSettingKey.DESTINATION_TO_SAVE, UserSettingKey.PREDICTIVE_LAUNCH, UserSettingKey.FAST_CAPTURE, UserSettingKey.HELP_GUIDE, UserSettingKey.RESET_SETTINGS);
        final Category category2 = category(2131689953, new UserSettingKey[0]);
        this.mAuto = group(category(2131689668, UserSettingKey.ISO, UserSettingKey.RESOLUTION, UserSettingKey.PREDICTIVE_CAPTURE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.METERING, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.SOFT_SKIN, UserSettingKey.FUSION_MODE, UserSettingKey.DISTORTION_CORRECTION, UserSettingKey.SIDE_SENSE), category);
        this.mOneShotPhoto = group(category(2131689668, UserSettingKey.ISO, UserSettingKey.RESOLUTION, UserSettingKey.OBJECT_TRACKING, UserSettingKey.METERING, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.SOFT_SKIN, UserSettingKey.FUSION_MODE, UserSettingKey.DISTORTION_CORRECTION, UserSettingKey.SIDE_SENSE), category2);
        this.mManual = group(category(2131689677, UserSettingKey.RESOLUTION, UserSettingKey.TOUCH_INTENTION, UserSettingKey.METERING, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.SOFT_SKIN, UserSettingKey.DISTORTION_CORRECTION), category);
        this.mSlowMotion = group(category(2131689681, UserSettingKey.VIDEO_SIZE, UserSettingKey.SLOW_MOTION), category);
        if (b) {
            this.mVideo = group(category(2131689631, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.VIDEO_CODEC, UserSettingKey.SIDE_SENSE), category);
            this.mOneShotVideo = group(category(2131689631, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.VIDEO_CODEC, UserSettingKey.SIDE_SENSE), category2);
        }
        else {
            this.mVideo = group(category(2131689631, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.SIDE_SENSE), category);
            this.mOneShotVideo = group(category(2131689631, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.SIDE_SENSE), category2);
        }
    }
    
    private static Category category(final int n, final UserSettingKey... array) {
        return new Category(n, array);
    }
    
    private static Group group(final Category category, final Category category2) {
        return new Group(category, category2);
    }
    
    public Group get(final CapturingMode capturingMode, final boolean b) {
        switch (ContextualSettingList$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("The specified mode is not supported. mode:");
                sb.append(capturingMode.name());
                throw new IllegalArgumentException(sb.toString());
            }
            case 7: {
                return this.mSlowMotion;
            }
            case 5:
            case 6: {
                Group group;
                if (b) {
                    group = this.mOneShotVideo;
                }
                else {
                    group = this.mVideo;
                }
                return group;
            }
            case 3:
            case 4: {
                return this.mManual;
            }
            case 1:
            case 2: {
                Group group2;
                if (b) {
                    group2 = this.mOneShotPhoto;
                }
                else {
                    group2 = this.mAuto;
                }
                return group2;
            }
        }
    }
    
    public static class Category
    {
        public final UserSettingKey[] keys;
        public final int titleResource;
        
        public Category(final int titleResource, final UserSettingKey... keys) {
            this.titleResource = titleResource;
            this.keys = keys;
        }
    }
    
    public static class Group
    {
        public final Category common;
        public final Category priorityHigh;
        
        public Group(final Category priorityHigh, final Category common) {
            this.priorityHigh = priorityHigh;
            this.common = common;
        }
    }
}
