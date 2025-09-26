// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Iterator;
import java.util.List;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.Storage;

public enum DestinationToSave implements UserSettingValue
{
    private static final DestinationToSave[] $VALUES;
    
    EMMC(2131231018, 2131690056, Storage.StorageType.INTERNAL, (DestinationToSave)null), 
    INTERNAL_MASS_STORAGE(2131231018, 2131690056, Storage.StorageType.INTERNAL, DestinationToSave.EMMC), 
    MEMORY_CARD(2131231019, 2131690057, Storage.StorageType.EXTERNAL_CARD, DestinationToSave.SDCARD), 
    SDCARD(2131231019, 2131690057, Storage.StorageType.EXTERNAL_CARD, (DestinationToSave)null);
    
    public static final String TAG = "DestinationToSave";
    private static int sParameterTextId = 2131690059;
    private static DestinationToSave sPrimaryStorage;
    private final DestinationToSave mCompatibleValue;
    private int mIconId;
    private boolean mIsEquipped;
    private int mTextId;
    private Storage.StorageType mType;
    
    static {
        $VALUES = new DestinationToSave[] { DestinationToSave.EMMC, DestinationToSave.SDCARD, DestinationToSave.INTERNAL_MASS_STORAGE, DestinationToSave.MEMORY_CARD };
        DestinationToSave.sPrimaryStorage = DestinationToSave.EMMC;
    }
    
    private DestinationToSave(final int mIconId, final int mTextId, final Storage.StorageType mType, final DestinationToSave mCompatibleValue) {
        this.mIsEquipped = false;
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mType = mType;
        this.mCompatibleValue = mCompatibleValue;
    }
    
    public static DestinationToSave[] getOptions() {
        return new DestinationToSave[] { DestinationToSave.EMMC, DestinationToSave.SDCARD };
    }
    
    public static DestinationToSave getPrimaryStorage() {
        return DestinationToSave.sPrimaryStorage;
    }
    
    public static DestinationToSave getValueFromType(final Storage.StorageType obj) {
        if (obj == null) {
            return null;
        }
        for (final DestinationToSave obj2 : values()) {
            if (obj == obj2.getType()) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("getValueFromType: type: ");
                    sb.append(obj);
                    sb.append(", value : ");
                    sb.append(obj2);
                    CamLog.d(sb.toString());
                }
                return obj2;
            }
        }
        return null;
    }
    
    private boolean isEquipped() {
        return this.mIsEquipped;
    }
    
    public static final void preload() {
    }
    
    public static void setMountPoint(final List<Storage.StorageType> list) {
        for (final Storage.StorageType obj : list) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("setMountPoint: type: ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            final DestinationToSave[] values = values();
            final int length = values.length;
            int i = 0;
            while (i < length) {
                final DestinationToSave obj2 = values[i];
                if (obj2.mCompatibleValue == null && obj == obj2.getType()) {
                    obj2.mIsEquipped = true;
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("setMountPoint: valid mount point: ");
                        sb2.append(obj2);
                        CamLog.d(sb2.toString());
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
        }
        updatePrimaryStorage();
    }
    
    private static void updatePrimaryStorage() {
        if (!DestinationToSave.EMMC.isEquipped() && DestinationToSave.SDCARD.isEquipped()) {
            DestinationToSave.sPrimaryStorage = DestinationToSave.SDCARD;
        }
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.DESTINATION_TO_SAVE;
    }
    
    @Override
    public int getKeyTextId() {
        return DestinationToSave.sParameterTextId;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    public Storage.StorageType getType() {
        return this.mType;
    }
    
    @Override
    public String getValue() {
        return this.mType.toString();
    }
}
