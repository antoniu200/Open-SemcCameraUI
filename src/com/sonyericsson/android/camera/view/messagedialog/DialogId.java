package com.sonyericsson.android.camera.view.messagedialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder;
import com.sonyericsson.android.camera.view.messagedialog.MessageDialogController;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.utility.BrandConfig;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'THERMAL_WARNING' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:451)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByField(EnumVisitor.java:372)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByWrappedInsn(EnumVisitor.java:337)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:322)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:262)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class DialogId {
    private static final /* synthetic */ DialogId[] $VALUES;
    public static final DialogId COOLING_MODE;
    public static final DialogId COULD_NOT_SAVE_PHOTO;
    public static final DialogId COULD_NOT_START_RECORDING;
    public static final DialogId DESTINATION_TO_SAVE_CHANGED_INTERNAL;
    public static final DialogId DLG_INVALID;
    public static final DialogId ERROR_IN_USE_BY_ANOTHER_APPLICATION;
    public static final DialogId ERROR_UNKNOWN;
    public static final DialogId ERROR_USE_OF_CAMERA_RESTRICTED;
    public static final DialogId FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_DESTINATION_CHANGE;
    public static final DialogId FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_VIDEOSIZE_CHANGE;
    public static final DialogId HIGH_SPEED_SD_RECOMMENDATION_ON_MODE_CHANGE;
    public static final DialogId HIGH_SPEED_SD_RECOMMENDATION_ON_SETTING_CHANGE;
    public static final DialogId LOW_BATTERY_CRITICAL_ON_PHOTO;
    public static final DialogId LOW_BATTERY_CRITICAL_ON_RECORDING;
    public static final DialogId LOW_BATTERY_WARNING;
    public static final DialogId MAX_DURATION_REACHED;
    public static final DialogId MAX_FILESIZE_REACHED;
    public static final DialogId MEMORY_FULL;
    public static final DialogId MEMORY_FULL_IN_BURST_MODE;
    public static final DialogId MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL;
    public static final DialogId MEMORY_FULL_PROPOSE_CHANGE_TO_SD;
    public static final DialogId MEMORY_INTERNAL_UNAVAILABLE;
    public static final DialogId MEMORY_SD_UNAVAILABLE;
    public static final DialogId MEMORY_SD_UNAVAILABLE_FOR_CORRUPT;
    public static final DialogId MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO;
    public static final DialogId MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL;
    public static final DialogId MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD;
    public static final DialogId PREDICTIVE_LAUNCH_DESCRIPTION;
    public static final DialogId REQUEST_SD_CARD_PERMISSION;
    public static final DialogId RESET_CONFIRMATION;
    public static final DialogId RESTRICT_PHOTO_RESOLUTION;
    public static final DialogId RESTRICT_SLOW_VIDEO_RESOLUTION;
    public static final DialogId RESTRICT_STEADYSHOT_VIDEO_RESOLUTION;
    public static final DialogId SD_CARD_PERMISSION_UNAVAILABLE;
    public static final DialogId SIDE_SENSE_DISABLE_ON_CONTEXTUAL_SETTINGS;
    public static final DialogId SIDE_SENSE_DISABLE_ON_LAUNCH;
    public static final DialogId THERMAL_CRITICAL;
    public static final DialogId THERMAL_WARNING;
    public static final DialogId UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP;
    public static final DialogId UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU;
    public static final DialogId VIDEO_HDR_CAUTION;
    public static final DialogId VIDEO_HDR_RESTRICTION;
    final MessageDialogBuilder builderType;
    final int checkBoxResourceID;
    final boolean hasOnCheckBox;
    final RotatableDialog.Cancelable isCancelable;
    final RotatableDialog.Cancelable isCancelableOnTouchOutside;
    final int layoutResourceID;
    final int messageFooterResourceID;
    final int messageResourceID;
    final MessageType messageType;
    final int negativeButtonResourceID;
    final int positiveButtonResourceID;
    final MessageDialogController.Priority priority;
    final int titleResourceID;
    public static final DialogId LOCATION_SERVICE_DISABLE_ON_LAUNCH = new DialogId("LOCATION_SERVICE_DISABLE_ON_LAUNCH", 0, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
        protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
            RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
            builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
            builder.setOrientation(i);
            builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
            builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
            builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
            builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
            builder.setOnCancelListener(onCancelListener);
            builder.setOnDismissListener(onDismissListener);
            builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
            return builder.createRotatableDialog();
        }
    }, MessageDialogController.Priority.LOW, R.string.cam_strings_advanced_setting_geo_tag_title_txt, R.string.cam_strings_advanced_setting_geo_tag_both_off_txt, -1, R.string.cam_strings_ok_txt, R.string.cam_strings_cancel_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
    public static final DialogId LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS = new DialogId("LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS", 1, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
        @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
        protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
            RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
            builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
            builder.setOrientation(i);
            builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
            builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
            builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
            builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
            builder.setOnCancelListener(onCancelListener);
            builder.setOnDismissListener(onDismissListener);
            builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
            return builder.createRotatableDialog();
        }
    }, MessageDialogController.Priority.LOW, R.string.cam_strings_advanced_setting_geo_tag_title_txt, R.string.cam_strings_advanced_setting_geo_tag_both_off_txt, -1, R.string.cam_strings_ok_txt, R.string.cam_strings_cancel_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
    public static final DialogId THERMAL_NOTE = new DialogId("THERMAL_NOTE", 2, new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.NORMAL, R.string.cam_strings_dialog_high_temp_title_txt, R.string.cam_strings_dialog_high_temp_txt, -1, R.string.cam_strings_ok_txt, -1, R.layout.dialog_content_withcheckbox, R.id.check_box_do_not_show_again, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.THERMAL_NOTE, true);

    public static DialogId valueOf(String str) {
        return (DialogId) Enum.valueOf(DialogId.class, str);
    }

    public static DialogId[] values() {
        return (DialogId[]) $VALUES.clone();
    }

    static {
        OkWithCheckBoxDialogBuilder okWithCheckBoxDialogBuilder = new OkWithCheckBoxDialogBuilder();
        MessageDialogController.Priority priority = MessageDialogController.Priority.NORMAL;
        RotatableDialog.Cancelable cancelable = RotatableDialog.Cancelable.TRUE;
        RotatableDialog.Cancelable cancelable2 = RotatableDialog.Cancelable.FALSE;
        MessageType messageType = MessageType.NO_MESSAGE;
        THERMAL_WARNING = new DialogId("THERMAL_WARNING", 3, okWithCheckBoxDialogBuilder, priority, R.string.cam_strings_dialog_high_temp_title_txt, R.string.cam_strings_error_high_temp_info_txt, -1, R.string.cam_strings_ok_txt, -1, R.layout.dialog_content_withcheckbox, R.id.check_box_do_not_show_again, cancelable, cancelable2, MessageType.THERMAL_WARNING, true);
        THERMAL_CRITICAL = new DialogId("THERMAL_CRITICAL", 4, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_dialog_high_temp_title_txt, R.string.cam_strings_error_high_temp_shutting_down_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        ERROR_IN_USE_BY_ANOTHER_APPLICATION = new DialogId("ERROR_IN_USE_BY_ANOTHER_APPLICATION", 5, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.NoButtonDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_error_dialog_title_txt, R.string.cam_strings_error_device_not_available_txt, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        ERROR_USE_OF_CAMERA_RESTRICTED = new DialogId("ERROR_USE_OF_CAMERA_RESTRICTED", 6, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.NoButtonDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_error_dialog_title_txt, R.string.cam_strings_use_of_camera_not_authorized_txt, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        ERROR_UNKNOWN = new DialogId("ERROR_UNKNOWN", 7, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.NoButtonDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_error_dialog_title_txt, R.string.cam_strings_error_fatal_txt, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.USE_DEFAULT, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO = new DialogId("MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO", 8, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.NoButtonDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_error_dialog_title_txt, R.string.cam_strings_error_mms_rec_size_limit_txt, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_VIDEOSIZE_CHANGE = new DialogId("FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_VIDEOSIZE_CHANGE", 9, new OkCancelWithCheckBoxDialogBuilder(), MessageDialogController.Priority.LOW, R.string.cam_strings_change_save_destination_title_txt, R.string.cam_strings_video_explanatory_txt, -1, R.string.cam_strings_error_high_temp_shut_down_yes_txt, R.string.cam_strings_error_high_temp_shut_down_no_txt, R.layout.dialog_content_withcheckbox, R.id.check_box_do_not_show_again, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.FOURK_STORAGE_EXPLANATORY_FOR_VIDEOSIZE, true);
        FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_DESTINATION_CHANGE = new DialogId("FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_DESTINATION_CHANGE", 10, new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.LOW, R.string.cam_strings_save_destination_title_txt, R.string.cam_strings_video_explanatory_4k2k_setting_txt, -1, R.string.cam_strings_ok_txt, -1, R.layout.dialog_content_withcheckbox, R.id.check_box_do_not_show_again, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.FOURK_STORAGE_EXPLANATORY_FOR_DESTINATION, true);
        MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL = new DialogId("MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL", 11, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_save_destination_title_txt, R.string.cam_strings_common_error_sdcard_full_txt, -1, R.string.cam_strings_error_high_temp_shut_down_yes_txt, R.string.cam_strings_error_high_temp_shut_down_no_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL = new DialogId("MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL", 12, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_save_destination_title_txt, R.string.cam_strings_common_error_sdcard_unavailable_txt, -1, R.string.cam_strings_error_high_temp_shut_down_yes_txt, R.string.cam_strings_error_high_temp_shut_down_no_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        MEMORY_FULL = new DialogId("MEMORY_FULL", 13, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.NORMAL, R.string.cam_strings_error_memory_title_txt, R.string.cam_strings_error_internal_sd_full_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        MEMORY_SD_UNAVAILABLE = new DialogId("MEMORY_SD_UNAVAILABLE", 14, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.NORMAL, R.string.cam_strings_error_memory_title_txt, R.string.cam_strings_error_memory_unavailable_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        MEMORY_FULL_PROPOSE_CHANGE_TO_SD = new DialogId("MEMORY_FULL_PROPOSE_CHANGE_TO_SD", 15, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_error_memory_title_txt, R.string.cam_strings_common_error_internal_memory_full_txt, -1, R.string.cam_strings_error_high_temp_shut_down_yes_txt, R.string.cam_strings_error_high_temp_shut_down_no_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        MEMORY_INTERNAL_UNAVAILABLE = new DialogId("MEMORY_INTERNAL_UNAVAILABLE", 16, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.NORMAL, R.string.cam_strings_error_memory_title_txt, R.string.cam_strings_error_memory_ims_unavailable_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD = new DialogId("MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD", 17, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_error_memory_title_txt, R.string.cam_strings_common_error_internal_memory_unavailable_txt, -1, R.string.cam_strings_error_high_temp_shut_down_yes_txt, R.string.cam_strings_error_high_temp_shut_down_no_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP = new DialogId("UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP", 18, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_fast_capturing_screen_locked_title_txt, R.string.cam_strings_fast_capturing_screen_locked_apps_list_txt, -1, R.string.cam_strings_unlock_button_txt, R.string.cam_strings_cancel_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU = new DialogId("UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU", 19, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_fast_capturing_screen_locked_title_txt, R.string.cam_strings_fast_capturing_screen_locked_settings_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, true);
        COULD_NOT_SAVE_PHOTO = new DialogId("COULD_NOT_SAVE_PHOTO", 20, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.NoButtonDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_error_dialog_title_txt, R.string.cam_strings_error_fatal_sd_photo_txt, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        COULD_NOT_START_RECORDING = new DialogId("COULD_NOT_START_RECORDING", 21, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.NoButtonDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_error_dialog_title_txt, R.string.cam_strings_error_fatal_sd_video_txt, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        COOLING_MODE = new DialogId("COOLING_MODE", 22, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkAndListDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                StringBuilder sb = new StringBuilder();
                Resources resources = context.getResources();
                if (messageDialogRequest.mDialogId.messageResourceID != -1) {
                    sb.append(resources.getString(messageDialogRequest.mDialogId.messageResourceID));
                    sb.append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                }
                if (messageDialogRequest.mMessageList != null) {
                    sb.append(messageDialogRequest.mMessageList);
                }
                if (messageDialogRequest.mDialogId.messageFooterResourceID != -1) {
                    sb.append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                    sb.append(resources.getString(messageDialogRequest.mDialogId.messageFooterResourceID));
                }
                if (isLargeTextEnabled(context)) {
                    LayoutInflater layoutInflaterFrom = LayoutInflater.from(context);
                    if (layoutInflaterFrom == null) {
                        return null;
                    }
                    TextView textView = (TextView) layoutInflaterFrom.inflate(messageDialogRequest.mDialogId.layoutResourceID, (ViewGroup) null);
                    textView.setMovementMethod(new ScrollingMovementMethod());
                    textView.setText(sb.toString());
                    builder.setView(textView);
                } else {
                    builder.setMessage(sb.toString());
                }
                return builder.createRotatableDialog();
            }

            protected boolean isLargeTextEnabled(Context context) {
                return context.getResources().getConfiguration().fontScale > 1.0f;
            }
        }, MessageDialogController.Priority.NORMAL, R.string.cam_strings_cooling_mode_title_txt, R.string.cam_strings_cooling_mode_message1_txt, R.string.cam_strings_cooling_mode_message2_txt, R.string.cam_strings_tutorial_button_txt, -1, R.layout.dialog_scrollable_message, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        RESTRICT_PHOTO_RESOLUTION = new DialogId("RESTRICT_PHOTO_RESOLUTION", 23, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_restricted_setting_dialog_title_txt, R.string.cam_strings_restricted_setting_photo_resolution_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        RESTRICT_STEADYSHOT_VIDEO_RESOLUTION = new DialogId("RESTRICT_STEADYSHOT_VIDEO_RESOLUTION", 24, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_restricted_setting_dialog_title_txt, R.string.cam_strings_restricted_setting_video_resolution_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        LOW_BATTERY_CRITICAL_ON_RECORDING = new DialogId("LOW_BATTERY_CRITICAL_ON_RECORDING", 25, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_dialog_high_temp_title_txt, R.string.cam_strings_dialog_battery_level_low_recording_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        LOW_BATTERY_CRITICAL_ON_PHOTO = new DialogId("LOW_BATTERY_CRITICAL_ON_PHOTO", 26, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_dialog_high_temp_title_txt, R.string.cam_strings_dialog_battery_level_low_using_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        LOW_BATTERY_WARNING = new DialogId("LOW_BATTERY_WARNING", 27, new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.NORMAL, R.string.cam_strings_dialog_high_temp_title_txt, R.string.cam_strings_dialog_battery_level_low_info_txt, -1, R.string.cam_strings_ok_txt, -1, R.layout.dialog_content_withcheckbox, R.id.check_box_do_not_show_again, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.BATTERY_WARNING, true);
        HIGH_SPEED_SD_RECOMMENDATION_ON_MODE_CHANGE = new DialogId("HIGH_SPEED_SD_RECOMMENDATION_ON_MODE_CHANGE", 28, new OkCancelWithCheckBoxDialogBuilder(), MessageDialogController.Priority.LOW, R.string.cam_strings_change_save_destination_slowmotion_title_txt, R.string.cam_strings_video_explanatory_txt, -1, R.string.cam_strings_error_high_temp_shut_down_yes_txt, R.string.cam_strings_error_high_temp_shut_down_no_txt, R.layout.dialog_content_withcheckbox, R.id.check_box_do_not_show_again, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.STORAGE_EXPLANATORY, true);
        HIGH_SPEED_SD_RECOMMENDATION_ON_SETTING_CHANGE = new DialogId("HIGH_SPEED_SD_RECOMMENDATION_ON_SETTING_CHANGE", 29, new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.LOW, R.string.cam_strings_save_destination_title_txt, R.string.cam_strings_video_explanatory_slowmotion_setting_txt, -1, R.string.cam_strings_ok_txt, -1, R.layout.dialog_content_withcheckbox, R.id.check_box_do_not_show_again, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.STORAGE_EXPLANATORY_FOR_SETTING, true);
        RESTRICT_SLOW_VIDEO_RESOLUTION = new DialogId("RESTRICT_SLOW_VIDEO_RESOLUTION", 30, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_restricted_setting_dialog_title_txt, R.string.cam_strings_restricted_setting_slow_motion_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        MEMORY_SD_UNAVAILABLE_FOR_CORRUPT = new DialogId("MEMORY_SD_UNAVAILABLE_FOR_CORRUPT", 31, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.IMMEDIATELY, R.string.cam_strings_error_memory_title_txt, R.string.cam_strings_error_memory_unavailable_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        MEMORY_FULL_IN_BURST_MODE = new DialogId("MEMORY_FULL_IN_BURST_MODE", 32, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.NORMAL, R.string.cam_strings_error_memory_title_txt, R.string.cam_strings_dialog_storage_full_burst_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        MAX_FILESIZE_REACHED = new DialogId("MAX_FILESIZE_REACHED", 33, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.NORMAL, R.string.cam_strings_error_memory_title_txt, R.string.cam_strings_max_file_size_save_failed_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        MAX_DURATION_REACHED = new DialogId("MAX_DURATION_REACHED", 34, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.NORMAL, R.string.cam_strings_dialog_hight_temp_title_txt, R.string.cam_strings_max_recroding_time_save_failed_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.FALSE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        VIDEO_HDR_CAUTION = new DialogId("VIDEO_HDR_CAUTION", 35, new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.NORMAL, R.string.cam_strings_dialog_high_temp_title_txt, R.string.cam_strings_dialog_video_hdr_hlg_supported_txt, -1, R.string.cam_strings_ok_txt, -1, R.layout.dialog_content_withcheckbox, R.id.check_box_do_not_show_again, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.VIDEO_HDR_CAUTION, false);
        VIDEO_HDR_RESTRICTION = new DialogId("VIDEO_HDR_RESTRICTION", 36, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_restricted_setting_dialog_title_txt, R.string.cam_strings_restricted_setting_video_hdr_hlg_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
        SIDE_SENSE_DISABLE_ON_LAUNCH = new DialogId("SIDE_SENSE_DISABLE_ON_LAUNCH", 37, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_side_touch_on_title_txt, R.string.cam_strings_side_touch_on_txt, -1, R.string.cam_strings_ok_txt, R.string.cam_strings_cancel_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        SIDE_SENSE_DISABLE_ON_CONTEXTUAL_SETTINGS = new DialogId("SIDE_SENSE_DISABLE_ON_CONTEXTUAL_SETTINGS", 38, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_side_touch_on_title_txt, R.string.cam_strings_side_touch_on_txt, -1, R.string.cam_strings_ok_txt, R.string.cam_strings_cancel_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        DLG_INVALID = new DialogId("DLG_INVALID", 39, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, -1, -1, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.FALSE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        RESET_CONFIRMATION = new DialogId("RESET_CONFIRMATION", 40, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.NORMAL, R.string.cam_strings_reset_settings_txt, R.string.cam_strings_reset_settings_confirm_title_txt, -1, R.string.cam_strings_ok_txt, R.string.cam_strings_cancel_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        DESTINATION_TO_SAVE_CHANGED_INTERNAL = new DialogId("DESTINATION_TO_SAVE_CHANGED_INTERNAL", 41, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_dialog_high_temp_title_txt, R.string.cam_strings_sd_permission_info_txt, -1, R.string.cam_strings_ok_txt, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        REQUEST_SD_CARD_PERMISSION = new DialogId("REQUEST_SD_CARD_PERMISSION", 42, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_dialog_high_temp_title_txt, R.string.cam_strings_sd_permission_info_2_txt, -1, R.string.cam_strings_ok_txt, R.string.cam_strings_cancel_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        SD_CARD_PERMISSION_UNAVAILABLE = new DialogId("SD_CARD_PERMISSION_UNAVAILABLE", 43, new MessageDialogBuilder() { // from class: com.sonyericsson.android.camera.view.messagedialog.OkCancelDialogBuilder
            @Override // com.sonyericsson.android.camera.view.messagedialog.MessageDialogBuilder
            protected RotatableDialog create(Context context, int i, MessageSettings messageSettings, MessageDialogRequest messageDialogRequest, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
                RotatableDialog.Builder builder = new RotatableDialog.Builder(context);
                builder.setOnKeyListener(new MessageDialogBuilder.KeyEventKiller());
                builder.setOrientation(i);
                builder.setTitle(messageDialogRequest.mDialogId.titleResourceID);
                builder.setMessage(messageDialogRequest.mDialogId.messageResourceID);
                builder.setPositiveButton(messageDialogRequest.mDialogId.positiveButtonResourceID, onClickListener);
                builder.setNegativeButton(messageDialogRequest.mDialogId.negativeButtonResourceID, onClickListener2);
                builder.setOnCancelListener(onCancelListener);
                builder.setOnDismissListener(onDismissListener);
                builder.setCancelable(messageDialogRequest.mDialogId.isCancelable, messageDialogRequest.mDialogId.isCancelableOnTouchOutside);
                return builder.createRotatableDialog();
            }
        }, MessageDialogController.Priority.LOW, R.string.cam_strings_sd_permission_dialog_title_txt, R.string.cam_strings_sd_permission_info_3_txt, -1, R.string.cam_strings_ok_txt, R.string.cam_strings_cancel_txt, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false);
        PREDICTIVE_LAUNCH_DESCRIPTION = new DialogId("PREDICTIVE_LAUNCH_DESCRIPTION", 44, new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.NORMAL, R.string.cam_strings_predictive_launch_txt, R.string.cam_strings_predictive_launch_dialog_guide_txt, -1, R.string.cam_strings_ok_txt, -1, R.layout.dialog_content_withcheckbox, R.id.check_box_do_not_show_again, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.PREDICTIVE_LAUNCH_DESCRIPTION, false);
        $VALUES = new DialogId[]{LOCATION_SERVICE_DISABLE_ON_LAUNCH, LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS, THERMAL_NOTE, THERMAL_WARNING, THERMAL_CRITICAL, ERROR_IN_USE_BY_ANOTHER_APPLICATION, ERROR_USE_OF_CAMERA_RESTRICTED, ERROR_UNKNOWN, MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO, FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_VIDEOSIZE_CHANGE, FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_DESTINATION_CHANGE, MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL, MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL, MEMORY_FULL, MEMORY_SD_UNAVAILABLE, MEMORY_FULL_PROPOSE_CHANGE_TO_SD, MEMORY_INTERNAL_UNAVAILABLE, MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD, UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP, UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU, COULD_NOT_SAVE_PHOTO, COULD_NOT_START_RECORDING, COOLING_MODE, RESTRICT_PHOTO_RESOLUTION, RESTRICT_STEADYSHOT_VIDEO_RESOLUTION, LOW_BATTERY_CRITICAL_ON_RECORDING, LOW_BATTERY_CRITICAL_ON_PHOTO, LOW_BATTERY_WARNING, HIGH_SPEED_SD_RECOMMENDATION_ON_MODE_CHANGE, HIGH_SPEED_SD_RECOMMENDATION_ON_SETTING_CHANGE, RESTRICT_SLOW_VIDEO_RESOLUTION, MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, MEMORY_FULL_IN_BURST_MODE, MAX_FILESIZE_REACHED, MAX_DURATION_REACHED, VIDEO_HDR_CAUTION, VIDEO_HDR_RESTRICTION, SIDE_SENSE_DISABLE_ON_LAUNCH, SIDE_SENSE_DISABLE_ON_CONTEXTUAL_SETTINGS, DLG_INVALID, RESET_CONFIRMATION, DESTINATION_TO_SAVE_CHANGED_INTERNAL, REQUEST_SD_CARD_PERMISSION, SD_CARD_PERMISSION_UNAVAILABLE, PREDICTIVE_LAUNCH_DESCRIPTION};
    }

    private DialogId(String str, int i, MessageDialogBuilder messageDialogBuilder, MessageDialogController.Priority priority, int i2, int i3, int i4, int i5, int i6, int i7, int i8, RotatableDialog.Cancelable cancelable, RotatableDialog.Cancelable cancelable2, MessageType messageType, boolean z) {
        this.builderType = messageDialogBuilder;
        this.priority = priority;
        this.titleResourceID = i2;
        if (messageType == MessageType.THERMAL_NOTE && BrandConfig.isVerizonBrand()) {
            i3 = R.string.cam_strings_dialog_high_temp_vzw_txt;
        }
        this.messageResourceID = i3;
        this.messageFooterResourceID = i4;
        this.positiveButtonResourceID = i5;
        this.negativeButtonResourceID = i6;
        this.layoutResourceID = i7;
        this.checkBoxResourceID = i8;
        this.isCancelable = cancelable;
        this.isCancelableOnTouchOutside = cancelable2;
        this.messageType = messageType;
        this.hasOnCheckBox = z;
    }

    public MessageType getMessageType() {
        return this.messageType;
    }
}
