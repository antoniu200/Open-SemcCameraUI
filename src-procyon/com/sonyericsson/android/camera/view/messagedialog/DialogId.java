// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.messagedialog;

import com.sonyericsson.cameracommon.utility.BrandConfig;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;

public enum DialogId
{
    private static final DialogId[] $VALUES;
    
    COOLING_MODE((MessageDialogBuilder)new OkAndListDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689707, 2131689705, 2131689706, 2131690202, -1, 2131492919, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    COULD_NOT_SAVE_PHOTO((MessageDialogBuilder)new NoButtonDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689770, 2131689771, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    COULD_NOT_START_RECORDING((MessageDialogBuilder)new NoButtonDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689770, 2131689772, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    DESTINATION_TO_SAVE_CHANGED_INTERNAL((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.LOW, 2131689738, 2131690101, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    DLG_INVALID((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.LOW, -1, -1, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.FALSE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    ERROR_IN_USE_BY_ANOTHER_APPLICATION((MessageDialogBuilder)new NoButtonDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689770, 2131689769, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    ERROR_UNKNOWN((MessageDialogBuilder)new NoButtonDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689770, 2131689773, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.USE_DEFAULT, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    ERROR_USE_OF_CAMERA_RESTRICTED((MessageDialogBuilder)new NoButtonDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689770, 2131690207, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_DESTINATION_CHANGE((MessageDialogBuilder)new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.LOW, 2131690058, 2131690212, -1, 2131689975, -1, 2131492915, 2131296352, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.FOURK_STORAGE_EXPLANATORY_FOR_DESTINATION, true), 
    FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_VIDEOSIZE_CHANGE((MessageDialogBuilder)new OkCancelWithCheckBoxDialogBuilder(), MessageDialogController.Priority.LOW, 2131689690, 2131690215, -1, 2131689778, 2131689777, 2131492915, 2131296352, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.FOURK_STORAGE_EXPLANATORY_FOR_VIDEOSIZE, true), 
    HIGH_SPEED_SD_RECOMMENDATION_ON_MODE_CHANGE((MessageDialogBuilder)new OkCancelWithCheckBoxDialogBuilder(), MessageDialogController.Priority.LOW, 2131689689, 2131690215, -1, 2131689778, 2131689777, 2131492915, 2131296352, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.STORAGE_EXPLANATORY, true), 
    HIGH_SPEED_SD_RECOMMENDATION_ON_SETTING_CHANGE((MessageDialogBuilder)new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.LOW, 2131690058, 2131690214, -1, 2131689975, -1, 2131492915, 2131296352, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.STORAGE_EXPLANATORY_FOR_SETTING, true), 
    LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131689620, 2131689619, -1, 2131689975, 2131689666, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    LOCATION_SERVICE_DISABLE_ON_LAUNCH((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131689620, 2131689619, -1, 2131689975, 2131689666, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    LOW_BATTERY_CRITICAL_ON_PHOTO((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689738, 2131689726, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    LOW_BATTERY_CRITICAL_ON_RECORDING((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689738, 2131689725, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    LOW_BATTERY_WARNING((MessageDialogBuilder)new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689738, 2131689724, -1, 2131689975, -1, 2131492915, 2131296352, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.BATTERY_WARNING, true), 
    MAX_DURATION_REACHED((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689741, 2131689944, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.FALSE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    MAX_FILESIZE_REACHED((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689789, 2131689942, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    MEMORY_FULL((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689789, 2131689783, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    MEMORY_FULL_IN_BURST_MODE((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689789, 2131689742, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131690058, 2131689699, -1, 2131689778, 2131689777, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    MEMORY_FULL_PROPOSE_CHANGE_TO_SD((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131689789, 2131689696, -1, 2131689778, 2131689777, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    MEMORY_INTERNAL_UNAVAILABLE((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689789, 2131689788, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    MEMORY_SD_UNAVAILABLE((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689789, 2131689790, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    MEMORY_SD_UNAVAILABLE_FOR_CORRUPT((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689789, 2131689790, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO((MessageDialogBuilder)new NoButtonDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689770, 2131689793, -1, -1, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131690058, 2131689700, -1, 2131689778, 2131689777, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131689789, 2131689697, -1, 2131689778, 2131689777, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    PREDICTIVE_LAUNCH_DESCRIPTION((MessageDialogBuilder)new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131690016, 2131690006, -1, 2131689975, -1, 2131492915, 2131296352, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.PREDICTIVE_LAUNCH_DESCRIPTION, false), 
    REQUEST_SD_CARD_PERMISSION((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131689738, 2131690099, -1, 2131689975, 2131689666, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    RESET_CONFIRMATION((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131690033, 2131690032, -1, 2131689975, 2131689666, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    RESTRICT_PHOTO_RESOLUTION((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.LOW, 2131690037, 2131690040, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    RESTRICT_SLOW_VIDEO_RESOLUTION((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.LOW, 2131690037, 2131690042, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    RESTRICT_STEADYSHOT_VIDEO_RESOLUTION((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.LOW, 2131690037, 2131690045, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    SD_CARD_PERMISSION_UNAVAILABLE((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131690098, 2131690100, -1, 2131689975, 2131689666, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    SIDE_SENSE_DISABLE_ON_CONTEXTUAL_SETTINGS((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131690124, 2131690125, -1, 2131689975, 2131689666, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    SIDE_SENSE_DISABLE_ON_LAUNCH((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131690124, 2131690125, -1, 2131689975, 2131689666, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    THERMAL_CRITICAL((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.IMMEDIATELY, 2131689738, 2131689779, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, false), 
    THERMAL_NOTE((MessageDialogBuilder)new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689738, 2131689739, -1, 2131689975, -1, 2131492915, 2131296352, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.THERMAL_NOTE, true), 
    THERMAL_WARNING((MessageDialogBuilder)okWithCheckBoxDialogBuilder, normal, 2131689738, 2131689775, -1, 2131689975, -1, 2131492915, 2131296352, true, false, MessageType.THERMAL_WARNING, true), 
    UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP((MessageDialogBuilder)new OkCancelDialogBuilder(), MessageDialogController.Priority.LOW, 2131689826, 2131689824, -1, 2131690206, 2131689666, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false), 
    UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.LOW, 2131689826, 2131689825, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.FALSE, MessageType.NO_MESSAGE, true), 
    VIDEO_HDR_CAUTION((MessageDialogBuilder)new OkWithCheckBoxDialogBuilder(), MessageDialogController.Priority.NORMAL, 2131689738, 2131689743, -1, 2131689975, -1, 2131492915, 2131296352, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.VIDEO_HDR_CAUTION, false), 
    VIDEO_HDR_RESTRICTION((MessageDialogBuilder)new OkDialogBuilder(), MessageDialogController.Priority.LOW, 2131690037, 2131690043, -1, 2131689975, -1, -1, -1, RotatableDialog.Cancelable.TRUE, RotatableDialog.Cancelable.USE_DEFAULT, MessageType.NO_MESSAGE, false);
    
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
    
    static {
        final OkWithCheckBoxDialogBuilder okWithCheckBoxDialogBuilder = new OkWithCheckBoxDialogBuilder();
        final MessageDialogController.Priority normal = MessageDialogController.Priority.NORMAL;
        final RotatableDialog.Cancelable true = RotatableDialog.Cancelable.TRUE;
        final RotatableDialog.Cancelable false = RotatableDialog.Cancelable.FALSE;
        final MessageType no_MESSAGE = MessageType.NO_MESSAGE;
        $VALUES = new DialogId[] { DialogId.LOCATION_SERVICE_DISABLE_ON_LAUNCH, DialogId.LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS, DialogId.THERMAL_NOTE, DialogId.THERMAL_WARNING, DialogId.THERMAL_CRITICAL, DialogId.ERROR_IN_USE_BY_ANOTHER_APPLICATION, DialogId.ERROR_USE_OF_CAMERA_RESTRICTED, DialogId.ERROR_UNKNOWN, DialogId.MEMORY_SHORTAGE_ON_ONE_SHOT_VIDEO, DialogId.FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_VIDEOSIZE_CHANGE, DialogId.FOURK_HIGH_SPEED_SD_RECOMMENDATION_ON_DESTINATION_CHANGE, DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_INTERNAL, DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_INTERNAL, DialogId.MEMORY_FULL, DialogId.MEMORY_SD_UNAVAILABLE, DialogId.MEMORY_FULL_PROPOSE_CHANGE_TO_SD, DialogId.MEMORY_INTERNAL_UNAVAILABLE, DialogId.MEMORY_UNAVAILABLE_PROPOSE_CHANGE_TO_SD, DialogId.UNLOCK_REQUEST_FOR_OPENING_ADD_ON_APP, DialogId.UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU, DialogId.COULD_NOT_SAVE_PHOTO, DialogId.COULD_NOT_START_RECORDING, DialogId.COOLING_MODE, DialogId.RESTRICT_PHOTO_RESOLUTION, DialogId.RESTRICT_STEADYSHOT_VIDEO_RESOLUTION, DialogId.LOW_BATTERY_CRITICAL_ON_RECORDING, DialogId.LOW_BATTERY_CRITICAL_ON_PHOTO, DialogId.LOW_BATTERY_WARNING, DialogId.HIGH_SPEED_SD_RECOMMENDATION_ON_MODE_CHANGE, DialogId.HIGH_SPEED_SD_RECOMMENDATION_ON_SETTING_CHANGE, DialogId.RESTRICT_SLOW_VIDEO_RESOLUTION, DialogId.MEMORY_SD_UNAVAILABLE_FOR_CORRUPT, DialogId.MEMORY_FULL_IN_BURST_MODE, DialogId.MAX_FILESIZE_REACHED, DialogId.MAX_DURATION_REACHED, DialogId.VIDEO_HDR_CAUTION, DialogId.VIDEO_HDR_RESTRICTION, DialogId.SIDE_SENSE_DISABLE_ON_LAUNCH, DialogId.SIDE_SENSE_DISABLE_ON_CONTEXTUAL_SETTINGS, DialogId.DLG_INVALID, DialogId.RESET_CONFIRMATION, DialogId.DESTINATION_TO_SAVE_CHANGED_INTERNAL, DialogId.REQUEST_SD_CARD_PERMISSION, DialogId.SD_CARD_PERMISSION_UNAVAILABLE, DialogId.PREDICTIVE_LAUNCH_DESCRIPTION };
    }
    
    private DialogId(final MessageDialogBuilder builderType, final MessageDialogController.Priority priority, final int titleResourceID, final int n2, final int messageFooterResourceID, final int positiveButtonResourceID, final int negativeButtonResourceID, final int layoutResourceID, final int checkBoxResourceID, final RotatableDialog.Cancelable isCancelable, final RotatableDialog.Cancelable isCancelableOnTouchOutside, final MessageType messageType, final boolean hasOnCheckBox) {
        this.builderType = builderType;
        this.priority = priority;
        this.titleResourceID = titleResourceID;
        n = n2;
        if (messageType == MessageType.THERMAL_NOTE) {
            n = n2;
            if (BrandConfig.isVerizonBrand()) {
                n = 2131689740;
            }
        }
        this.messageResourceID = n;
        this.messageFooterResourceID = messageFooterResourceID;
        this.positiveButtonResourceID = positiveButtonResourceID;
        this.negativeButtonResourceID = negativeButtonResourceID;
        this.layoutResourceID = layoutResourceID;
        this.checkBoxResourceID = checkBoxResourceID;
        this.isCancelable = isCancelable;
        this.isCancelableOnTouchOutside = isCancelableOnTouchOutside;
        this.messageType = messageType;
        this.hasOnCheckBox = hasOnCheckBox;
    }
    
    public MessageType getMessageType() {
        return this.messageType;
    }
}
