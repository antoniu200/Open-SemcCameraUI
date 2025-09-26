package com.sonyericsson.android.camera.configuration;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum UserSettingSelectability {
    INVALID(false),
    FIXED(false),
    UNAVAILABLE(false),
    SELECTABLE(true),
    FORCE_CHANGED(true);

    public static final String TAG = "UserSettingSelectability";
    private final boolean mIsUpdatable;

    UserSettingSelectability(boolean z) {
        this.mIsUpdatable = z;
    }

    public boolean isUpdatable() {
        return this.mIsUpdatable;
    }

    public static UserSettingSelectability getSelectability(int i) {
        switch (i) {
            case 0:
                return INVALID;
            case 1:
                return FIXED;
            default:
                return SELECTABLE;
        }
    }
}
