// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import android.util.Range;
import java.util.ArrayList;
import java.util.Iterator;
import android.graphics.Rect;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;

public enum Iso implements UserSettingValue
{
    private static final Iso[] $VALUES;
    
    ISO_100(-1, 2131689915, "iso-prio", 100), 
    ISO_12800(-1, 2131689916, "iso-prio", 12800), 
    ISO_1600(-1, 2131689917, "iso-prio", 1600), 
    ISO_200(-1, 2131689918, "iso-prio", 200), 
    ISO_25600(-1, 2131689919, "iso-prio", 25600), 
    ISO_3200(-1, 2131689920, "iso-prio", 3200), 
    ISO_400(-1, 2131689921, "iso-prio", 400), 
    ISO_50(-1, 2131689922, "iso-prio", 50), 
    ISO_51200(-1, 2131689923, "iso-prio", 51200), 
    ISO_6400(-1, 2131689924, "iso-prio", 6400), 
    ISO_800(-1, 2131689925, "iso-prio", 800), 
    ISO_AUTO(-1, 2131689894, "auto", -1);
    
    public static final String TAG = "Iso";
    private static int mIndexOfDefault = 1;
    private static final int sParameterTextId = 2131689928;
    private final String mAeMode;
    private final int mIconId;
    private final int mIsoValue;
    private final int mTextId;
    
    static {
        $VALUES = new Iso[] { Iso.ISO_AUTO, Iso.ISO_50, Iso.ISO_100, Iso.ISO_200, Iso.ISO_400, Iso.ISO_800, Iso.ISO_1600, Iso.ISO_3200, Iso.ISO_6400, Iso.ISO_12800, Iso.ISO_25600, Iso.ISO_51200 };
    }
    
    private Iso(final int mIconId, final int mTextId, final String mAeMode, final int mIsoValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mAeMode = mAeMode;
        this.mIsoValue = mIsoValue;
    }
    
    public static Iso adjustToSupportedValue(final Iso iso, final Iso[] array) {
        final Iso iso_AUTO = Iso.ISO_AUTO;
        int n = 0;
        if ((iso == iso_AUTO || array.length == 1) && array[0] == Iso.ISO_AUTO) {
            return Iso.ISO_AUTO;
        }
        if (array[0] == Iso.ISO_AUTO) {
            n = 1;
        }
        Iso iso2;
        if (iso.getIsoValue() < array[n].getIsoValue()) {
            iso2 = array[n];
        }
        else {
            iso2 = iso;
            if (iso.getIsoValue() > array[array.length - 1].getIsoValue()) {
                iso2 = array[array.length - 1];
            }
        }
        return iso2;
    }
    
    public static boolean canBeManuallySetWith(final CapturingMode capturingMode, final Resolution resolution) {
        for (final Rect rect : PlatformCapability.getCameraCapability(capturingMode.getCameraId()).MANUAL_ISO_SUPPORTED_PICTURE_SIZE.get()) {
            if (rect.width() == resolution.getPictureRect().width() && rect.height() == resolution.getPictureRect().height()) {
                return true;
            }
        }
        return false;
    }
    
    public static int getIndexOfDefault() {
        return Iso.mIndexOfDefault;
    }
    
    public static Iso[] getOptions(final CapturingMode capturingMode, final Resolution resolution, final FusionMode fusionMode) {
        final ArrayList list = new ArrayList();
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
        final List list2 = cameraCapability.AE.get();
        if (list2.isEmpty()) {
            return list.toArray(new Iso[0]);
        }
        if (list2.contains(Iso.ISO_AUTO.getValue())) {
            list.add(Iso.ISO_AUTO);
        }
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT || capturingMode == CapturingMode.FRONT_PHOTO || capturingMode.getType() == 2) {
            return list.toArray(new Iso[0]);
        }
        if (!list2.contains("iso-prio")) {
            return list.toArray(new Iso[0]);
        }
        Range range = null;
        int n = 0;
        switch (Iso$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$FusionMode[fusionMode.ordinal()]) {
            default: {
                range = cameraCapability.ISO_RANGE.get();
                n = Iso.ISO_100.getIsoValue();
                break;
            }
            case 2: {
                range = cameraCapability.FUSION_ISO_RANGE.get();
                n = Iso.ISO_1600.getIsoValue();
                break;
            }
            case 1: {
                range = new Range((Comparable)Math.min((int)cameraCapability.ISO_RANGE.get().getLower(), (int)cameraCapability.FUSION_ISO_RANGE.get().getLower()), (Comparable)Math.max((int)cameraCapability.ISO_RANGE.get().getUpper(), (int)cameraCapability.FUSION_ISO_RANGE.get().getUpper()));
                n = Iso.ISO_1600.getIsoValue();
                break;
            }
        }
        for (final Iso e : values()) {
            if (e.getIsoValue() >= (int)range.getLower() && e.getIsoValue() <= (int)range.getUpper() && canBeManuallySetWith(capturingMode, resolution)) {
                list.add(e);
                if (e.getIsoValue() <= n) {
                    Iso.mIndexOfDefault = list.size() - 1;
                }
            }
        }
        return list.toArray(new Iso[0]);
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    public int getIsoValue() {
        return this.mIsoValue;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.ISO;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689928;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.mAeMode;
    }
}
