package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum AspectRatio implements UserSettingValue {
    SIXTEEN_TO_NINE(R.drawable.cam_core_aspect_ratio_shortcut_16x9_icn, R.string.cam_strings_aspect_ratio_16x9_txt),
    FOUR_TO_THREE(R.drawable.cam_core_aspect_ratio_shortcut_4x3_icn, R.string.cam_strings_aspect_ratio_4x3_txt),
    ONE_TO_ONE(R.drawable.cam_core_aspect_ratio_shortcut_1x1_icn, R.string.cam_strings_aspect_ratio_1x1_txt);

    public static final String TAG = "AspectRatio";
    private static final int sParameterTextId = 2131689641;
    private final int mIconId;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131689641;
    }

    AspectRatio(int i, int i2) {
        this.mIconId = i;
        this.mTextId = i2;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.ASPECT_RATIO;
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

    public static AspectRatio[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        if (!capturingMode.isVideo()) {
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            for (Resolution resolution : Resolution.getOptions(capturingMode)) {
                int iWidth = resolution.getPictureRect().width();
                int iHeight = resolution.getPictureRect().height();
                if (getAspectRatio(iWidth, iHeight) == SIXTEEN_TO_NINE) {
                    z = true;
                } else if (getAspectRatio(iWidth, iHeight) == FOUR_TO_THREE) {
                    z2 = true;
                } else if (getAspectRatio(iWidth, iHeight) == ONE_TO_ONE) {
                    z3 = true;
                }
            }
            if (z) {
                arrayList.add(SIXTEEN_TO_NINE);
            }
            if (z2) {
                arrayList.add(FOUR_TO_THREE);
            }
            if (z3) {
                arrayList.add(ONE_TO_ONE);
            }
        }
        return (AspectRatio[]) arrayList.toArray(new AspectRatio[0]);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public static AspectRatio getDefaultValue(CapturingMode capturingMode) {
        Resolution defaultValue = Resolution.getDefaultValue(capturingMode);
        return getAspectRatio(defaultValue.getPictureRect().width(), defaultValue.getPictureRect().height());
    }

    public static AspectRatio getAspectRatio(int i, int i2) {
        if (i == 0 || i2 == 0) {
            return null;
        }
        if (i * 9 == i2 * 16) {
            return SIXTEEN_TO_NINE;
        }
        if (i * 3 == i2 * 4) {
            return FOUR_TO_THREE;
        }
        if (i == i2) {
            return ONE_TO_ONE;
        }
        return null;
    }
}
