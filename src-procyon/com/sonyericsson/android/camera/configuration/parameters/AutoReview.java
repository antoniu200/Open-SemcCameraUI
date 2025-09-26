// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.ActionMode;

public enum AutoReview implements UserSettingValue
{
    private static final AutoReview[] $VALUES;
    
    ALWAYS(-1, 2131690017, 3000), 
    FRONT_ONLY(-1, 2131690024, 3000), 
    OFF(-1, 2131690115, 0);
    
    public static final String TAG = "AutoReview";
    private static final int sParameterTextId = 2131690021;
    private final int mDuration;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new AutoReview[] { AutoReview.ALWAYS, AutoReview.FRONT_ONLY, AutoReview.OFF };
    }
    
    private AutoReview(final int mIconId, final int mTextId, final int mDuration) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mDuration = mDuration;
    }
    
    public static AutoReview getDefaultValue(final boolean b) {
        if (b) {
            return AutoReview.OFF;
        }
        return AutoReview.FRONT_ONLY;
    }
    
    public static AutoReview[] getOptions(final ActionMode actionMode) {
        if (actionMode.mIsOneShot) {
            return new AutoReview[] { AutoReview.OFF };
        }
        return values();
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public int getDuration() {
        return this.mDuration;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.AUTO_REVIEW;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690021;
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
        return this.toString();
    }
}
