package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum FastCapture implements UserSettingValue {
    LAUNCH_AND_CAPTURE(-1, R.string.cam_strings_fast_capturing_launch_and_capture_photo_txt, 1, true),
    LAUNCH_ONLY(-1, R.string.cam_strings_fast_capturing_launch_only_txt, 1, true),
    OFF(-1, R.string.cam_strings_settings_off_txt, 0, false);

    public static final String TAG = "FastCapture";
    private static final int sParameterTextId = 2131689828;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mTextId;
    private final int mType;
    private String mValue;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131689828;
    }

    public int getParameterkeyTitleTextId() {
        return R.string.cam_strings_fast_capturing_title_txt;
    }

    FastCapture(int i, int i2, int i3, boolean z) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mType = i3;
        this.mBooleanValue = z;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.FAST_CAPTURE;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }

    public int getCameraType() {
        return this.mType;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return this.mValue;
    }

    public boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public static FastCapture[] getOptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(LAUNCH_ONLY);
        arrayList.add(LAUNCH_AND_CAPTURE);
        arrayList.add(OFF);
        return (FastCapture[]) arrayList.toArray(new FastCapture[0]);
    }

    public static FastCapture getDefault() {
        return LAUNCH_ONLY;
    }
}
