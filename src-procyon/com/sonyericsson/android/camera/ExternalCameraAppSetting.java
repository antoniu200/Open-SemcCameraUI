// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import java.util.HashMap;
import java.util.Iterator;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import java.util.Map;
import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum ExternalCameraAppSetting
{
    private static final ExternalCameraAppSetting[] $VALUES;
    
    AUTO_PHOTO_PREVIEW("AUTO_PHOTO_PREVIEW", UserSettingKey.AUTO_REVIEW, ShareSettingCategory.COMMON, items().add("off", AutoReview.OFF).add("on", AutoReview.ALWAYS).add("only_front_camera", AutoReview.FRONT_ONLY)), 
    DATA_STORAGE("DATA_STORAGE", UserSettingKey.DESTINATION_TO_SAVE, ShareSettingCategory.COMMON, items().add("sdcard", DestinationToSave.SDCARD).add("internal", DestinationToSave.EMMC)), 
    DISTORTION_CORRECTION_MODE("DISTORTION_CORRECTION_MODE", UserSettingKey.DISTORTION_CORRECTION, ShareSettingCategory.COMMON, items().add(Boolean.FALSE, DistortionCorrection.OFF).add(Boolean.TRUE, DistortionCorrection.ON)), 
    FLASH("FLASH_MODE", UserSettingKey.FLASH, ShareSettingCategory.PHOTO, items().add("auto", Flash.AUTO).add("fill_flash", Flash.ON).add("flashlight", Flash.LED_ON).add("red_eye", Flash.RED_EYE).add("off", Flash.OFF)), 
    FRONT_ANGLE("FRONT_ANGLE", UserSettingKey.FRONT_ANGLE, ShareSettingCategory.COMMON, items().add("default", FrontAngle.DEFAULT).add("cropped", FrontAngle.CROPPED)), 
    GRID_LINES("GRID_LINES", UserSettingKey.GRID_LINE, ShareSettingCategory.COMMON, items().add(Boolean.FALSE, GridLine.OFF).add(Boolean.TRUE, GridLine.ON));
    
    private static final String INTENT_KEY_PREFIX = "com.sonyericsson.android.camera.extra.";
    
    SAVE_LOCATION("SAVE_LOCATION", UserSettingKey.GEO_TAG, ShareSettingCategory.COMMON, items().add(Boolean.FALSE, Geotag.OFF).add(Boolean.TRUE, Geotag.ON)), 
    SOUND("SOUND", UserSettingKey.SHUTTER_SOUND, ShareSettingCategory.COMMON, items().add(Boolean.FALSE, ShutterSound.OFF).add(Boolean.TRUE, ShutterSound.SOUND1)), 
    USE_VOLUME_KEY_AS("USE_VOLUME_KEY_AS", UserSettingKey.VOLUME_KEY, ShareSettingCategory.COMMON, items().add("shutter", VolumeKey.HW_CAMERA_KEY).add("volume", VolumeKey.VOLUME).add("zoom", VolumeKey.ZOOM));
    
    public final String intentKey;
    public final UserSettingKey key;
    private final Map<Object, UserSettingValue> mIntentToUserSetting;
    private final ShareSettingCategory mSettingCategory;
    
    static {
        $VALUES = new ExternalCameraAppSetting[] { ExternalCameraAppSetting.FLASH, ExternalCameraAppSetting.FRONT_ANGLE, ExternalCameraAppSetting.GRID_LINES, ExternalCameraAppSetting.SOUND, ExternalCameraAppSetting.SAVE_LOCATION, ExternalCameraAppSetting.DATA_STORAGE, ExternalCameraAppSetting.USE_VOLUME_KEY_AS, ExternalCameraAppSetting.AUTO_PHOTO_PREVIEW, ExternalCameraAppSetting.DISTORTION_CORRECTION_MODE };
    }
    
    private ExternalCameraAppSetting(final String str, final UserSettingKey key, final ShareSettingCategory mSettingCategory, final ItemsBuilder itemsBuilder) {
        this.key = key;
        final StringBuilder sb = new StringBuilder();
        sb.append("com.sonyericsson.android.camera.extra.");
        sb.append(str);
        this.intentKey = sb.toString();
        this.mSettingCategory = mSettingCategory;
        this.mIntentToUserSetting = itemsBuilder.entries;
    }
    
    private static ItemsBuilder items() {
        return new ItemsBuilder();
    }
    
    public boolean isShared(final ShareSettingCategory shareSettingCategory) {
        return this.mSettingCategory.isAccepted(shareSettingCategory);
    }
    
    public Object toIntentValue(final UserSettingValue obj) {
        for (final Map.Entry<K, UserSettingValue> entry : this.mIntentToUserSetting.entrySet()) {
            if (entry.getValue().equals(obj)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public UserSettingValue toValue(final Object o) {
        return this.mIntentToUserSetting.get(o);
    }
    
    private static class ItemsBuilder
    {
        public final Map<Object, UserSettingValue> entries;
        
        private ItemsBuilder() {
            this.entries = new HashMap<Object, UserSettingValue>();
        }
        
        public ItemsBuilder add(final Object o, final UserSettingValue userSettingValue) {
            this.entries.put(o, userSettingValue);
            return this;
        }
    }
    
    public enum ShareSettingCategory
    {
        private static final ShareSettingCategory[] $VALUES;
        
        COMMON, 
        PHOTO, 
        VIDEO;
        
        static {
            $VALUES = new ShareSettingCategory[] { ShareSettingCategory.PHOTO, ShareSettingCategory.VIDEO, ShareSettingCategory.COMMON };
        }
        
        public boolean isAccepted(final ShareSettingCategory shareSettingCategory) {
            final int n = ExternalCameraAppSetting$1.$SwitchMap$com$sonyericsson$android$camera$ExternalCameraAppSetting$ShareSettingCategory[this.ordinal()];
            boolean b = true;
            switch (n) {
                default: {
                    return false;
                }
                case 3: {
                    return true;
                }
                case 1:
                case 2: {
                    if (this != shareSettingCategory) {
                        b = false;
                    }
                    return b;
                }
            }
        }
    }
}
