// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration;

public enum UserSettingSelectability
{
    private static final UserSettingSelectability[] $VALUES;
    
    FIXED(false), 
    FORCE_CHANGED(true), 
    INVALID(false), 
    SELECTABLE(true);
    
    public static final String TAG = "UserSettingSelectability";
    
    UNAVAILABLE(false);
    
    private final boolean mIsUpdatable;
    
    static {
        $VALUES = new UserSettingSelectability[] { UserSettingSelectability.INVALID, UserSettingSelectability.FIXED, UserSettingSelectability.UNAVAILABLE, UserSettingSelectability.SELECTABLE, UserSettingSelectability.FORCE_CHANGED };
    }
    
    private UserSettingSelectability(final boolean mIsUpdatable) {
        this.mIsUpdatable = mIsUpdatable;
    }
    
    public static UserSettingSelectability getSelectability(final int n) {
        switch (n) {
            default: {
                return UserSettingSelectability.SELECTABLE;
            }
            case 1: {
                return UserSettingSelectability.FIXED;
            }
            case 0: {
                return UserSettingSelectability.INVALID;
            }
        }
    }
    
    public boolean isUpdatable() {
        return this.mIsUpdatable;
    }
}
