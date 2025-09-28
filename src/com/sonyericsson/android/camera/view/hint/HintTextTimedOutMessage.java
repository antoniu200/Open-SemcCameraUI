package com.sonyericsson.android.camera.view.hint;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.hint.HintTextContent;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HintTextTimedOutMessage extends HintTextContent {
    private static final int FADE_OUT_DURATION = 1000;
    private static final int SHOW_DURATION = 5000;
    private final boolean mIsToast;
    private final MessageType mType;

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getButtonMessageResourceId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getFadeDuration() {
        return 1000;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public long getTimedOutDuration() {
        return 5000L;
    }

    public enum MessageType {
        CANNOT_BURST_DUE_TO_LOW_BATTERY(R.string.cam_strings_burst_battery_level_low_txt, HintTextContent.HintPriority.HIGH, true),
        CANNOT_BURST_IN_DARK_CONDITION(R.string.cam_strings_burst_in_dark_condition_txt, HintTextContent.HintPriority.HIGH, true),
        BURST_CHANGE_CAMERA_KEY_SETTING(R.string.cam_strings_burst_change_camera_key_setting_txt, HintTextContent.HintPriority.HIGH, true),
        CANNOT_BURST_USING_FRONT_CAMERA(R.string.cam_strings_burst_not_supported_front_camera_txt, HintTextContent.HintPriority.HIGH, true),
        CANNOT_BURST_DUE_TO_FUSION_MODE(R.string.cam_strings_burst_not_supported_fusion_on_txt, HintTextContent.HintPriority.HIGH, true),
        BURST_IMAGES_ARE_SAVED_TO_INTERNAL_STORAGE(R.string.cam_strings_burst_saved_internal_storage_txt, HintTextContent.HintPriority.HIGH, true),
        ZOOM_NOT_AVAILABLE(R.string.cam_strings_zoom_not_supported_txt, HintTextContent.HintPriority.HIGH, true),
        ISO_CHANGED_BY_FUSION(R.string.cam_strings_iso_changed_txt, HintTextContent.HintPriority.HIGH, true);

        private final boolean mIsToast;
        private final int mMessageResourceId;
        private final HintTextContent.HintPriority mPriority;

        MessageType(int i, HintTextContent.HintPriority hintPriority, boolean z) {
            this.mMessageResourceId = i;
            this.mPriority = hintPriority;
            this.mIsToast = z;
        }
    }

    public HintTextTimedOutMessage(MessageType messageType) {
        this.mType = messageType;
        this.mIsToast = this.mType.mIsToast;
        this.mTransparentBackground = false;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public String getTag() {
        return createTag(this.mType);
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public boolean isToast() {
        return this.mIsToast;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public HintTextContent.HintPriority getPriority() {
        return this.mType.mPriority;
    }

    @Override // com.sonyericsson.android.camera.view.hint.HintTextContent
    public int getMessageResourceId() {
        return this.mType.mMessageResourceId;
    }

    public static String createTag(MessageType messageType) {
        return HintTextTimedOutMessage.class.getSimpleName() + ":" + messageType;
    }
}
