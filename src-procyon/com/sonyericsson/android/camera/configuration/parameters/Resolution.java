// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraInfo;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import java.util.List;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.util.capability.ResolutionOptions;
import android.graphics.Rect;

public enum Resolution implements UserSettingValue
{
    private static final Resolution[] $VALUES;
    
    EIGHT_MP(-1, 2131689996, new Rect(0, 0, 3264, 2448)), 
    FIVE_MP(-1, 2131689995, new Rect(0, 0, 2592, 1944)), 
    NINETEEN_MP(-1, 2131689994, new Rect(0, 0, 5056, 3792)), 
    SEVENTEEN_MP(-1, 2131689993, new Rect(0, 0, 4736, 3552)), 
    SQUARE_FOURTEEN_MP(-1, 2131689988, new Rect(0, 0, 3792, 3792)), 
    SQUARE_FOUR_MP(-1, 2131689989, new Rect(0, 0, 1944, 1944)), 
    SQUARE_NINE_MP(-1, 2131689990, new Rect(0, 0, 3000, 3000)), 
    SQUARE_NINE_POINT_SEVEN(-1, 2131689990, new Rect(0, 0, 3120, 3120)), 
    SQUARE_TWELVE_MP(-1, 2131689987, new Rect(0, 0, 3528, 3528));
    
    public static final String TAG = "Resolution";
    
    THIRTEEN_MP(-1, 2131689992, new Rect(0, 0, 4160, 3120)), 
    TWELVE_MP(-1, 2131689991, new Rect(0, 0, 4000, 3000)), 
    VGA(-1, -1, new Rect(0, 0, 640, 480)), 
    WIDE_FIVE_POINT_EIGHT_MP(-1, 2131689986, new Rect(0, 0, 3200, 1800)), 
    WIDE_FOUR_MP(-1, 2131689985, new Rect(0, 0, 2592, 1458)), 
    WIDE_SEVENTEEN_MP(-1, 2131689984, new Rect(0, 0, 5504, 3096)), 
    WIDE_TEN_MP(-1, 2131689981, new Rect(0, 0, 4192, 2358)), 
    WIDE_THIRTEEN_MP(-1, 2131689983, new Rect(0, 0, 4864, 2736)), 
    WIDE_TWELVE_MP(-1, 2131689982, new Rect(0, 0, 4608, 2592));
    
    private static final int sParameterTextId = 2131689910;
    private final int mIconId;
    private final Rect mPictureRect;
    private final int mTextId;
    
    static {
        $VALUES = new Resolution[] { Resolution.WIDE_SEVENTEEN_MP, Resolution.WIDE_THIRTEEN_MP, Resolution.WIDE_TWELVE_MP, Resolution.WIDE_TEN_MP, Resolution.WIDE_FIVE_POINT_EIGHT_MP, Resolution.WIDE_FOUR_MP, Resolution.NINETEEN_MP, Resolution.SEVENTEEN_MP, Resolution.THIRTEEN_MP, Resolution.TWELVE_MP, Resolution.EIGHT_MP, Resolution.FIVE_MP, Resolution.SQUARE_FOURTEEN_MP, Resolution.SQUARE_TWELVE_MP, Resolution.SQUARE_NINE_POINT_SEVEN, Resolution.SQUARE_NINE_MP, Resolution.SQUARE_FOUR_MP, Resolution.VGA };
    }
    
    private Resolution(final int mIconId, final int mTextId, final Rect mPictureRect) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mPictureRect = mPictureRect;
    }
    
    public static Resolution getDefaultValue(final CapturingMode capturingMode) {
        final ResolutionOptions resolutionOptions = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).RESOLUTION_CAPABILITY.get();
        final Resolution value = valueOf(resolutionOptions.getDefaultResolution());
        final Resolution[] options = getOptions(capturingMode);
        for (int length = options.length, i = 0; i < length; ++i) {
            if (options[i].equals(value)) {
                return valueOf(resolutionOptions.getDefaultResolution());
            }
        }
        return Resolution.VGA;
    }
    
    private static Resolution[] getExpectedOptions(final String[] array) {
        final ArrayList list = new ArrayList();
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                list.add(Enum.valueOf(Resolution.class, array[i]));
            }
            return (Resolution[])list.toArray(new Resolution[0]);
        }
        return values();
    }
    
    public static Resolution[] getOptions(final CapturingMode capturingMode) {
        if (capturingMode != CapturingMode.SCENE_RECOGNITION && capturingMode != CapturingMode.SUPERIOR_FRONT) {
            final ArrayList list = new ArrayList();
            if (capturingMode.getType() == 1) {
                final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
                final List list2 = cameraCapability.PICTURE_SIZE.get();
                if (!list2.isEmpty()) {
                    for (final Resolution e : getExpectedOptions(cameraCapability.RESOLUTION_CAPABILITY.get().getResolutionOptions())) {
                        for (final Rect rect : list2) {
                            if (e.mPictureRect.width() == rect.width() && e.mPictureRect.height() == rect.height()) {
                                list.add(e);
                                break;
                            }
                        }
                    }
                }
            }
            return list.toArray(new Resolution[0]);
        }
        return getSuperiorAutoOptions(capturingMode.getCameraId());
    }
    
    private static Resolution[] getSuperiorAutoOptions(final CameraInfo.CameraId cameraId) {
        final ArrayList list = new ArrayList();
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(cameraId);
        final List list2 = cameraCapability.PICTURE_SIZE.get();
        for (final Resolution e : getExpectedOptions(cameraCapability.RESOLUTION_CAPABILITY.get().getSuperiorAutoResolutionOptions())) {
            for (final Rect rect : list2) {
                if (e.mPictureRect.width() == rect.width() && e.mPictureRect.height() == rect.height()) {
                    list.add(e);
                    break;
                }
            }
        }
        return list.toArray(new Resolution[0]);
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
        return UserSettingKey.RESOLUTION;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689910;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    public Rect getPictureRect() {
        return this.mPictureRect;
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
}
