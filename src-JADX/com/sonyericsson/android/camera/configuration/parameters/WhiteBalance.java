package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum WhiteBalance implements UserSettingValue {
    INCANDESCENT(R.drawable.cam_core_image_quality_control_white_balance_incandescent_icn, R.string.cam_strings_white_balance_incandescent_txt, CameraParameters.WHITE_BALANCE_INCANDESCENT, R.drawable.cam_core_image_quality_control_white_balance_tab_incandescent_icn),
    FLUORESCENT(R.drawable.cam_core_image_quality_control_white_balance_fluorescent_icn, R.string.cam_strings_white_balance_fluorescent_txt, CameraParameters.WHITE_BALANCE_FLUORESCENT, R.drawable.cam_core_image_quality_control_white_balance_tab_fluorescent_icn),
    DAYLIGHT(R.drawable.cam_core_image_quality_control_white_balance_daylight_icn, R.string.cam_strings_white_balance_day_light_txt, CameraParameters.WHITE_BALANCE_DAYLIGHT, R.drawable.cam_core_image_quality_control_white_balance_tab_daylight_icn),
    CLOUDY_DAYLIGHT(R.drawable.cam_core_image_quality_control_white_balance_cloudy_icn, R.string.cam_strings_white_balance_cloudy_txt, CameraParameters.WHITE_BALANCE_CLOUDY_DAYLIGHT, R.drawable.cam_core_image_quality_control_white_balance_tab_cloudy_icn),
    AUTO(R.drawable.cam_core_image_quality_control_white_balance_auto_icn, R.string.cam_strings_white_balance_auto_txt, "auto", R.drawable.cam_core_image_quality_control_white_balance_tab_auto_icn);

    public static final String TAG = "WhiteBalance";
    private static final int sParameterTextId = 2131690261;
    private final int mIconId;
    private final int mTabIconId;
    private final int mTextId;
    private final String mValue;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690261;
    }

    WhiteBalance(int i, int i2, String str, int i3) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mValue = str;
        this.mTabIconId = i3;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.WHITE_BALANCE;
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
        return this.mValue;
    }

    public static WhiteBalance[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        List<String> list = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).WHITE_BALANCE.get();
        if (!list.isEmpty()) {
            if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
                arrayList.add(AUTO);
            } else {
                for (WhiteBalance whiteBalance : values()) {
                    Iterator<String> it = list.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (whiteBalance.getValue().equals(it.next())) {
                                arrayList.add(whiteBalance);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return (WhiteBalance[]) arrayList.toArray(new WhiteBalance[0]);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public int getTabIconId() {
        return this.mTabIconId;
    }
}
