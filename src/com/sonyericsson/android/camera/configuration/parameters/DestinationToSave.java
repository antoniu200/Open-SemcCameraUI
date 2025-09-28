package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum DestinationToSave implements UserSettingValue {
    EMMC(R.drawable.cam_core_data_storage_setting_internal_icn, R.string.cam_strings_save_destination_ims_txt, Storage.StorageType.INTERNAL, null),
    SDCARD(R.drawable.cam_core_data_storage_setting_sd_icn, R.string.cam_strings_save_destination_sd_txt, Storage.StorageType.EXTERNAL_CARD, null),
    INTERNAL_MASS_STORAGE(R.drawable.cam_core_data_storage_setting_internal_icn, R.string.cam_strings_save_destination_ims_txt, Storage.StorageType.INTERNAL, EMMC),
    MEMORY_CARD(R.drawable.cam_core_data_storage_setting_sd_icn, R.string.cam_strings_save_destination_sd_txt, Storage.StorageType.EXTERNAL_CARD, SDCARD);

    public static final String TAG = "DestinationToSave";
    private static int sParameterTextId = 2131690059;
    private final DestinationToSave mCompatibleValue;
    private int mIconId;
    private boolean mIsEquipped = false;
    private int mTextId;
    private Storage.StorageType mType;
    private static DestinationToSave sPrimaryStorage = EMMC;

    public static final void preload() {
    }

    DestinationToSave(int i, int i2, Storage.StorageType storageType, DestinationToSave destinationToSave) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mType = storageType;
        this.mCompatibleValue = destinationToSave;
    }

    public static void setMountPoint(List<Storage.StorageType> list) {
        for (Storage.StorageType storageType : list) {
            if (CamLog.VERBOSE) {
                CamLog.d("setMountPoint: type: " + storageType);
            }
            DestinationToSave[] destinationToSaveArrValues = values();
            int length = destinationToSaveArrValues.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    DestinationToSave destinationToSave = destinationToSaveArrValues[i];
                    if (destinationToSave.mCompatibleValue == null && storageType == destinationToSave.getType()) {
                        destinationToSave.mIsEquipped = true;
                        if (CamLog.VERBOSE) {
                            CamLog.d("setMountPoint: valid mount point: " + destinationToSave);
                        }
                    } else {
                        i++;
                    }
                }
            }
        }
        updatePrimaryStorage();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.DESTINATION_TO_SAVE;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return sParameterTextId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return this.mType.toString();
    }

    private boolean isEquipped() {
        return this.mIsEquipped;
    }

    public static DestinationToSave getValueFromType(Storage.StorageType storageType) {
        if (storageType == null) {
            return null;
        }
        for (DestinationToSave destinationToSave : values()) {
            if (storageType == destinationToSave.getType()) {
                if (CamLog.VERBOSE) {
                    CamLog.d("getValueFromType: type: " + storageType + ", value : " + destinationToSave);
                }
                return destinationToSave;
            }
        }
        return null;
    }

    public static DestinationToSave[] getOptions() {
        return new DestinationToSave[]{EMMC, SDCARD};
    }

    public Storage.StorageType getType() {
        return this.mType;
    }

    private static void updatePrimaryStorage() {
        if (EMMC.isEquipped() || !SDCARD.isEquipped()) {
            return;
        }
        sPrimaryStorage = SDCARD;
    }

    public static DestinationToSave getPrimaryStorage() {
        return sPrimaryStorage;
    }
}
