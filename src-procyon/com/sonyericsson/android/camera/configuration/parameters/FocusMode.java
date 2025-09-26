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

public enum FocusMode implements UserSettingValue
{
    private static final FocusMode[] $VALUES;
    
    FACE_DETECTION(-1, 2131689843, "continuous-picture", "continuous-video", "center", true), 
    FIXED(-1, 2131689848, "fixed", "fixed", "center", false), 
    INFINITY(-1, 2131689848, "infinity", "infinity", "center", true), 
    OBJECT_TRACKING(-1, 2131689847, "continuous-picture", "continuous-video", "center", true), 
    SINGLE(-1, 2131689848, "continuous-picture", "continuous-video", "center", true);
    
    public static final String TAG = "FocusMode";
    
    TOUCH_FOCUS(-1, 2131689850, "continuous-picture", "continuous-video", "center", true);
    
    private static final int sParameterTextId = 2131689851;
    private final String mFocusArea;
    private final int mIconId;
    private final boolean mSuccessSound;
    private final int mTextId;
    private String mValue;
    private String mValueForVideo;
    
    static {
        $VALUES = new FocusMode[] { FocusMode.SINGLE, FocusMode.FIXED, FocusMode.FACE_DETECTION, FocusMode.TOUCH_FOCUS, FocusMode.INFINITY, FocusMode.OBJECT_TRACKING };
    }
    
    private FocusMode(final int mIconId, final int mTextId, final String mValue, final String mValueForVideo, final String mFocusArea, final boolean mSuccessSound) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
        this.mValueForVideo = mValueForVideo;
        this.mFocusArea = mFocusArea;
        this.mSuccessSound = mSuccessSound;
    }
    
    public static FocusMode getDefaultValue(final CapturingMode capturingMode) {
        switch (FocusMode$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                return FocusMode.FACE_DETECTION;
            }
            case 4:
            case 5:
            case 6: {
                if (PlatformCapability.isFocusSupported(capturingMode.getCameraId())) {
                    return FocusMode.FACE_DETECTION;
                }
                return FocusMode.FIXED;
            }
            case 1:
            case 2:
            case 3: {
                return FocusMode.FACE_DETECTION;
            }
        }
    }
    
    private static FocusMode[] getExpectedOptions(final String[] array) {
        final ArrayList list = new ArrayList();
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                list.add(Enum.valueOf(FocusMode.class, array[i]));
            }
            return (FocusMode[])list.toArray(new FocusMode[0]);
        }
        return values();
    }
    
    public static FocusMode[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
        final List list2 = cameraCapability.FOCUS_MODE.get();
        final List list3 = cameraCapability.FOCUS_AREA.get();
        ArrayList list4 = list;
        if (!list2.isEmpty()) {
            ArrayList list5 = new ArrayList();
            for (final FocusMode e : values()) {
                if (list2.contains(e.getValue())) {
                    list5.add(e);
                }
            }
            if (!list3.isEmpty()) {
                final Iterator iterator = list5.iterator();
                while (true) {
                    list5 = list;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    final FocusMode focusMode = (FocusMode)iterator.next();
                    if (!list3.contains(focusMode.getFocusArea())) {
                        continue;
                    }
                    list.add(focusMode);
                }
            }
            if (cameraCapability.MAX_NUM_FACE.get() < 1) {
                remove(FocusMode.FACE_DETECTION, list5);
            }
            if (cameraCapability.MAX_NUM_FOCUS_AREA.get() < 1) {
                remove(FocusMode.TOUCH_FOCUS, list5);
            }
            list4 = list5;
            if (!cameraCapability.OBJECT_TRACKING.get()) {
                remove(FocusMode.OBJECT_TRACKING, list5);
                list4 = list5;
            }
        }
        return (FocusMode[])list4.toArray(new FocusMode[0]);
    }
    
    private static void remove(final FocusMode focusMode, final List<FocusMode> list) {
        if (list.contains(focusMode)) {
            list.remove(focusMode);
        }
    }
    
    public static void updateValue(final CameraInfo.CameraId cameraId, final List<String> list) {
        if (cameraId == CameraInfo.CameraId.BACK && !list.contains(FocusMode.SINGLE.getValue())) {
            FocusMode.SINGLE.mValue = "auto";
        }
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public String getFocusArea() {
        return this.mFocusArea;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.FOCUS_MODE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689851;
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
        return this.mValue;
    }
    
    public String getValueForVideo() {
        return this.mValueForVideo;
    }
    
    public boolean isSuccessSound() {
        return this.mSuccessSound;
    }
}
