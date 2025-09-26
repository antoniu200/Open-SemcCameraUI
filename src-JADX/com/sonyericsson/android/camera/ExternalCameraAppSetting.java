package com.sonyericsson.android.camera;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum ExternalCameraAppSetting {
    FLASH("FLASH_MODE", UserSettingKey.FLASH, ShareSettingCategory.PHOTO, items().add("auto", Flash.AUTO).add("fill_flash", Flash.ON).add("flashlight", Flash.LED_ON).add("red_eye", Flash.RED_EYE).add("off", Flash.OFF)),
    FRONT_ANGLE("FRONT_ANGLE", UserSettingKey.FRONT_ANGLE, ShareSettingCategory.COMMON, items().add("default", FrontAngle.DEFAULT).add("cropped", FrontAngle.CROPPED)),
    GRID_LINES("GRID_LINES", UserSettingKey.GRID_LINE, ShareSettingCategory.COMMON, items().add(Boolean.FALSE, GridLine.OFF).add(Boolean.TRUE, GridLine.ON)),
    SOUND("SOUND", UserSettingKey.SHUTTER_SOUND, ShareSettingCategory.COMMON, items().add(Boolean.FALSE, ShutterSound.OFF).add(Boolean.TRUE, ShutterSound.SOUND1)),
    SAVE_LOCATION("SAVE_LOCATION", UserSettingKey.GEO_TAG, ShareSettingCategory.COMMON, items().add(Boolean.FALSE, Geotag.OFF).add(Boolean.TRUE, Geotag.ON)),
    DATA_STORAGE("DATA_STORAGE", UserSettingKey.DESTINATION_TO_SAVE, ShareSettingCategory.COMMON, items().add("sdcard", DestinationToSave.SDCARD).add("internal", DestinationToSave.EMMC)),
    USE_VOLUME_KEY_AS("USE_VOLUME_KEY_AS", UserSettingKey.VOLUME_KEY, ShareSettingCategory.COMMON, items().add("shutter", VolumeKey.HW_CAMERA_KEY).add("volume", VolumeKey.VOLUME).add("zoom", VolumeKey.ZOOM)),
    AUTO_PHOTO_PREVIEW("AUTO_PHOTO_PREVIEW", UserSettingKey.AUTO_REVIEW, ShareSettingCategory.COMMON, items().add("off", AutoReview.OFF).add("on", AutoReview.ALWAYS).add("only_front_camera", AutoReview.FRONT_ONLY)),
    DISTORTION_CORRECTION_MODE("DISTORTION_CORRECTION_MODE", UserSettingKey.DISTORTION_CORRECTION, ShareSettingCategory.COMMON, items().add(Boolean.FALSE, DistortionCorrection.OFF).add(Boolean.TRUE, DistortionCorrection.ON));

    private static final String INTENT_KEY_PREFIX = "com.sonyericsson.android.camera.extra.";
    public final String intentKey;
    public final UserSettingKey key;
    private final Map<Object, UserSettingValue> mIntentToUserSetting;
    private final ShareSettingCategory mSettingCategory;

    ExternalCameraAppSetting(String str, UserSettingKey userSettingKey, ShareSettingCategory shareSettingCategory, ItemsBuilder itemsBuilder) {
        this.key = userSettingKey;
        this.intentKey = INTENT_KEY_PREFIX + str;
        this.mSettingCategory = shareSettingCategory;
        this.mIntentToUserSetting = itemsBuilder.entries;
    }

    public UserSettingValue toValue(Object obj) {
        return this.mIntentToUserSetting.get(obj);
    }

    public Object toIntentValue(UserSettingValue userSettingValue) {
        for (Map.Entry<Object, UserSettingValue> entry : this.mIntentToUserSetting.entrySet()) {
            if (entry.getValue().equals(userSettingValue)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean isShared(ShareSettingCategory shareSettingCategory) {
        return this.mSettingCategory.isAccepted(shareSettingCategory);
    }

    public enum ShareSettingCategory {
        PHOTO,
        VIDEO,
        COMMON;

        public boolean isAccepted(ShareSettingCategory shareSettingCategory) {
            switch (this) {
                case PHOTO:
                case VIDEO:
                    return this == shareSettingCategory;
                case COMMON:
                    return true;
                default:
                    return false;
            }
        }
    }

    private static ItemsBuilder items() {
        return new ItemsBuilder();
    }

    private static class ItemsBuilder {
        public final Map<Object, UserSettingValue> entries;

        private ItemsBuilder() {
            this.entries = new HashMap();
        }

        public ItemsBuilder add(Object obj, UserSettingValue userSettingValue) {
            this.entries.put(obj, userSettingValue);
            return this;
        }
    }
}
