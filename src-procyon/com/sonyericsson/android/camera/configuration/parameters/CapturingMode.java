// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;
import java.util.List;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.device.CameraInfo;

public enum CapturingMode implements UserSettingValue
{
    private static final CapturingMode[] $VALUES;
    
    FRONT_PHOTO(-1, -1, 1, CameraInfo.CameraId.FRONT), 
    FRONT_VIDEO(-1, -1, 2, CameraInfo.CameraId.FRONT), 
    NORMAL(2131230881, 2131689677, 1, CameraInfo.CameraId.BACK), 
    SCENE_RECOGNITION(-1, 2131689683, 1, CameraInfo.CameraId.BACK), 
    SLOW_MOTION(2131230884, 2131689600, 2, CameraInfo.CameraId.BACK), 
    SUPERIOR_FRONT(-1, -1, 1, CameraInfo.CameraId.FRONT);
    
    public static final String TAG = "CapturingMode";
    
    UNKNOWN(-1, -1, 0, CameraInfo.CameraId.BACK), 
    VIDEO(-1, 2131689677, 2, CameraInfo.CameraId.BACK);
    
    private static final int sParameterTextId = 2131689630;
    private static final CapturingMode[] sPhotoOptions;
    private final CameraInfo.CameraId mCameraId;
    private final int mIconId;
    private final int mTextId;
    private final int mType;
    
    static {
        $VALUES = new CapturingMode[] { CapturingMode.UNKNOWN, CapturingMode.SCENE_RECOGNITION, CapturingMode.NORMAL, CapturingMode.VIDEO, CapturingMode.SUPERIOR_FRONT, CapturingMode.FRONT_VIDEO, CapturingMode.SLOW_MOTION, CapturingMode.FRONT_PHOTO };
        sPhotoOptions = new CapturingMode[] { CapturingMode.SCENE_RECOGNITION, CapturingMode.NORMAL };
    }
    
    private CapturingMode(final int mIconId, final int mTextId, final int mType, final CameraInfo.CameraId mCameraId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mType = mType;
        this.mCameraId = mCameraId;
    }
    
    public static CapturingMode convertFrom(String value, final CapturingMode capturingMode) {
        try {
            value = (String)valueOf(value);
        }
        catch (final IllegalArgumentException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Mode[");
            sb.append(value);
            sb.append("] is not supported.");
            CamLog.w(sb.toString());
            value = (String)capturingMode;
        }
        return (CapturingMode)value;
    }
    
    public static CapturingMode[] getPhotoOptions() {
        return CapturingMode.sPhotoOptions.clone();
    }
    
    public static List<CapturingMode> getValidOptions() {
        final ArrayList list = new ArrayList();
        if (!PlatformCapability.isSceneRecognitionSupported(CameraInfo.CameraId.BACK)) {
            CamLog.w("Back camera doesn't support Scene recognition.");
        }
        list.add(CapturingMode.SCENE_RECOGNITION);
        if (PlatformCapability.isFrontCameraSupported()) {
            if (!PlatformCapability.isSceneRecognitionSupported(CameraInfo.CameraId.FRONT)) {
                CamLog.w("Front camera doesn't support Scene recognition.");
            }
            list.add(CapturingMode.SUPERIOR_FRONT);
        }
        list.add(CapturingMode.VIDEO);
        if (PlatformCapability.isFrontCameraSupported()) {
            list.add(CapturingMode.FRONT_VIDEO);
        }
        list.add(CapturingMode.SLOW_MOTION);
        list.add(CapturingMode.NORMAL);
        if (PlatformCapability.isFrontCameraSupported()) {
            list.add(CapturingMode.FRONT_PHOTO);
        }
        return list;
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public CameraInfo.CameraId getCameraId() {
        return this.mCameraId;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.CAPTURING_MODE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689630;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    public int getType() {
        return this.mType;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
    
    public boolean isFront() {
        return this.mCameraId == CameraInfo.CameraId.FRONT;
    }
    
    public boolean isMainPhoto() {
        return this.getType() == 1 && this.getCameraId() == CameraInfo.CameraId.BACK;
    }
    
    public boolean isSuperiorAuto() {
        return this == CapturingMode.SCENE_RECOGNITION || this == CapturingMode.SUPERIOR_FRONT;
    }
    
    public boolean isVideo() {
        return this.mType == 2;
    }
}
