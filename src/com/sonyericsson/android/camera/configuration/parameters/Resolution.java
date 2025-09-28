package com.sonyericsson.android.camera.configuration.parameters;

import android.graphics.Rect;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.util.capability.ResolutionOptions;
import com.sonyericsson.cameracommon.device.SizeConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum Resolution implements UserSettingValue {
    WIDE_SEVENTEEN_MP(-1, R.string.cam_strings_photo_resolution_16x9_17mp_txt, new Rect(0, 0, SizeConstants.WIDTH_17MP_WIDE, SizeConstants.HEIGHT_17MP_WIDE)),
    WIDE_THIRTEEN_MP(-1, R.string.cam_strings_photo_resolution_16x9_13mp_txt, new Rect(0, 0, SizeConstants.WIDTH_13MP_WIDE, SizeConstants.HEIGHT_13MP_WIDE)),
    WIDE_TWELVE_MP(-1, R.string.cam_strings_photo_resolution_16x9_12mp_txt, new Rect(0, 0, SizeConstants.WIDTH_12MP_WIDE, 2592)),
    WIDE_TEN_MP(-1, R.string.cam_strings_photo_resolution_16x9_10mp_txt, new Rect(0, 0, SizeConstants.WIDTH_10MP_WIDE, SizeConstants.HEIGHT_10MP_WIDE)),
    WIDE_FIVE_POINT_EIGHT_MP(-1, R.string.cam_strings_photo_resolution_16x9_5_8mp_txt, new Rect(0, 0, SizeConstants.WIDTH_5_8MP_WIDE, SizeConstants.HEIGHT_5_8MP_WIDE)),
    WIDE_FOUR_MP(-1, R.string.cam_strings_photo_resolution_16x9_4mp_txt, new Rect(0, 0, 2592, SizeConstants.HEIGHT_4MP_WIDE)),
    NINETEEN_MP(-1, R.string.cam_strings_photo_resolution_4x3_19mp_txt, new Rect(0, 0, SizeConstants.WIDTH_19MP, 3792)),
    SEVENTEEN_MP(-1, R.string.cam_strings_photo_resolution_4x3_17mp_txt, new Rect(0, 0, SizeConstants.WIDTH_17MP, SizeConstants.HEIGHT_17MP)),
    THIRTEEN_MP(-1, R.string.cam_strings_photo_resolution_4x3_13mp_txt, new Rect(0, 0, SizeConstants.WIDTH_13MP, 3120)),
    TWELVE_MP(-1, R.string.cam_strings_photo_resolution_4x3_12mp_txt, new Rect(0, 0, 4000, 3000)),
    EIGHT_MP(-1, R.string.cam_strings_photo_resolution_4x3_8mp_txt, new Rect(0, 0, SizeConstants.WIDTH_8MP, SizeConstants.HEIGHT_8MP)),
    FIVE_MP(-1, R.string.cam_strings_photo_resolution_4x3_5mp_txt, new Rect(0, 0, 2592, 1944)),
    SQUARE_FOURTEEN_MP(-1, R.string.cam_strings_photo_resolution_1x1_14mp_txt, new Rect(0, 0, 3792, 3792)),
    SQUARE_TWELVE_MP(-1, R.string.cam_strings_photo_resolution_1x1_12mp_txt, new Rect(0, 0, 3528, 3528)),
    SQUARE_NINE_POINT_SEVEN(-1, R.string.cam_strings_photo_resolution_1x1_9mp_txt, new Rect(0, 0, 3120, 3120)),
    SQUARE_NINE_MP(-1, R.string.cam_strings_photo_resolution_1x1_9mp_txt, new Rect(0, 0, 3000, 3000)),
    SQUARE_FOUR_MP(-1, R.string.cam_strings_photo_resolution_1x1_4mp_txt, new Rect(0, 0, 1944, 1944)),
    VGA(-1, -1, new Rect(0, 0, 640, 480));

    public static final String TAG = "Resolution";
    private static final int sParameterTextId = 2131689910;
    private final int mIconId;
    private final Rect mPictureRect;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131689910;
    }

    Resolution(int i, int i2, Rect rect) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mPictureRect = rect;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.RESOLUTION;
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
        return toString();
    }

    public static Resolution[] getOptions(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            return getSuperiorAutoOptions(capturingMode.getCameraId());
        }
        ArrayList arrayList = new ArrayList();
        if (capturingMode.getType() == 1) {
            CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
            List<Rect> list = cameraCapability.PICTURE_SIZE.get();
            if (!list.isEmpty()) {
                for (Resolution resolution : getExpectedOptions(cameraCapability.RESOLUTION_CAPABILITY.get().getResolutionOptions())) {
                    Iterator<Rect> it = list.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            Rect next = it.next();
                            if (resolution.mPictureRect.width() == next.width() && resolution.mPictureRect.height() == next.height()) {
                                arrayList.add(resolution);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return (Resolution[]) arrayList.toArray(new Resolution[0]);
    }

    private static Resolution[] getSuperiorAutoOptions(CameraInfo.CameraId cameraId) {
        ArrayList arrayList = new ArrayList();
        CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(cameraId);
        List<Rect> list = cameraCapability.PICTURE_SIZE.get();
        for (Resolution resolution : getExpectedOptions(cameraCapability.RESOLUTION_CAPABILITY.get().getSuperiorAutoResolutionOptions())) {
            Iterator<Rect> it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    Rect next = it.next();
                    if (resolution.mPictureRect.width() == next.width() && resolution.mPictureRect.height() == next.height()) {
                        arrayList.add(resolution);
                        break;
                    }
                }
            }
        }
        return (Resolution[]) arrayList.toArray(new Resolution[0]);
    }

    private static Resolution[] getExpectedOptions(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        if (strArr != null) {
            for (String str : strArr) {
                arrayList.add(valueOf(Resolution.class, str));
            }
            return (Resolution[]) arrayList.toArray(new Resolution[0]);
        }
        return values();
    }

    public static Resolution getDefaultValue(CapturingMode capturingMode) {
        ResolutionOptions resolutionOptions = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).RESOLUTION_CAPABILITY.get();
        Resolution resolutionValueOf = valueOf(resolutionOptions.getDefaultResolution());
        for (Resolution resolution : getOptions(capturingMode)) {
            if (resolution.equals(resolutionValueOf)) {
                return valueOf(resolutionOptions.getDefaultResolution());
            }
        }
        return VGA;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public Rect getPictureRect() {
        return this.mPictureRect;
    }
}
