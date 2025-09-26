// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.sonyericsson.cameracommon.sound.SoundPlayer;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;

public enum SelfTimer implements UserSettingValue, SelfTimerInterface
{
    private static final SelfTimer[] $VALUES;
    
    GESTURE_SHUTTER_COUNT_DOWN(-1, -1, -1, false, 1500, 2131231234, (SoundPlayer.Type)null), 
    LAUNCH_AND_CAPTURE_COUNT_DOWN(-1, -1, -1, false, 0, -1, (SoundPlayer.Type)null), 
    LONG(2131231172, 2131231181, 2131690105, true, 10000, -1, SoundPlayer.Type.SELF_TIMER_1SEC), 
    OFF(2131231175, 2131231183, 2131690115, false, 0, -1, (SoundPlayer.Type)null), 
    SHORT(2131231174, 2131231182, 2131690107, true, 3000, -1, SoundPlayer.Type.SELF_TIMER_3SEC), 
    SIDE_COUNT_DOWN(-1, -1, -1, false, 500, 2131231234, (SoundPlayer.Type)null);
    
    public static final String TAG = "SelfTimer";
    private static final int sParameterTextId = 2131690113;
    private static final int sShortcutTitleTextId = 2131690112;
    private final boolean mBooleanValue;
    private final int mCountDownIconId;
    private final int mIconId;
    private int mMilliSeconds;
    private final int mShortcutIconId;
    private SoundPlayer.Type mSoundType;
    private final int mTextId;
    
    static {
        $VALUES = new SelfTimer[] { SelfTimer.LONG, SelfTimer.SHORT, SelfTimer.GESTURE_SHUTTER_COUNT_DOWN, SelfTimer.SIDE_COUNT_DOWN, SelfTimer.LAUNCH_AND_CAPTURE_COUNT_DOWN, SelfTimer.OFF };
    }
    
    private SelfTimer(final int mIconId, final int mShortcutIconId, final int mTextId, final boolean mBooleanValue, final int mMilliSeconds, final int mCountDownIconId, final SoundPlayer.Type mSoundType) {
        this.mIconId = mIconId;
        this.mShortcutIconId = mShortcutIconId;
        this.mTextId = mTextId;
        this.mBooleanValue = mBooleanValue;
        this.mMilliSeconds = mMilliSeconds;
        this.mCountDownIconId = mCountDownIconId;
        this.mSoundType = mSoundType;
    }
    
    public static SelfTimer getDefaultValue(final CapturingMode capturingMode) {
        return SelfTimer.OFF;
    }
    
    public static SelfTimer[] getOptions() {
        final ArrayList list = new ArrayList((Collection<? extends E>)Arrays.asList(values()));
        list.remove(SelfTimer.GESTURE_SHUTTER_COUNT_DOWN);
        list.remove(SelfTimer.SIDE_COUNT_DOWN);
        list.remove(SelfTimer.LAUNCH_AND_CAPTURE_COUNT_DOWN);
        final SelfTimer[] a = new SelfTimer[list.size()];
        list.toArray(a);
        return a;
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getCountDownIconId() {
        return this.mCountDownIconId;
    }
    
    @Override
    public int getDurationInMillisecond() {
        return this.mMilliSeconds;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.SELF_TIMER;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690113;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    public int getShortcutId() {
        return this.mShortcutIconId;
    }
    
    @Override
    public SoundPlayer.Type getSoundType() {
        return this.mSoundType;
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
    
    public void setDurationInMillisecond(final int mMilliSeconds) {
        if (this == SelfTimer.LAUNCH_AND_CAPTURE_COUNT_DOWN) {
            this.mMilliSeconds = mMilliSeconds;
            if (this.mMilliSeconds < 4000) {
                this.mSoundType = SoundPlayer.Type.SELF_TIMER_3SEC;
            }
            else if (this.mMilliSeconds == 4000) {
                this.mSoundType = SoundPlayer.Type.SELF_TIMER_4SEC;
            }
            else {
                this.mSoundType = SoundPlayer.Type.SELF_TIMER_1SEC;
            }
        }
    }
}
