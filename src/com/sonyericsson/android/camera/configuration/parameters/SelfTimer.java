package com.sonyericsson.android.camera.configuration.parameters;

import com.google.android.gms.common.ConnectionResult;
import com.sonyericsson.android.camera.Constants;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;
import com.sonyericsson.cameracommon.sound.SoundPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum SelfTimer implements UserSettingValue, SelfTimerInterface {
    LONG(R.drawable.cam_core_selftimer_setting_10sec_icn, R.drawable.cam_core_shortcut_selftimer_10sec_icn, R.string.cam_strings_self_timer_10_txt, true, IptcConstants.IMAGE_RESOURCE_BLOCK_PRINT_FLAGS_INFO, -1, SoundPlayer.Type.SELF_TIMER_1SEC),
    SHORT(R.drawable.cam_core_selftimer_setting_3sec_icn, R.drawable.cam_core_shortcut_selftimer_3sec_icn, R.string.cam_strings_self_timer_3_txt, true, 3000, -1, SoundPlayer.Type.SELF_TIMER_3SEC),
    GESTURE_SHUTTER_COUNT_DOWN(-1, -1, -1, false, ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED, R.drawable.cam_handshutter_timer_hand_icn, null),
    SIDE_COUNT_DOWN(-1, -1, -1, false, Constants.INTERVAL_OPEN_CAMERA, R.drawable.cam_handshutter_timer_hand_icn, null),
    LAUNCH_AND_CAPTURE_COUNT_DOWN(-1, -1, -1, false, 0, -1, null),
    OFF(R.drawable.cam_core_selftimer_setting_off_icn, R.drawable.cam_core_shortcut_selftimer_off_icn, R.string.cam_strings_settings_off_txt, false, 0, -1, null);

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

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690113;
    }

    SelfTimer(int i, int i2, int i3, boolean z, int i4, int i5, SoundPlayer.Type type) {
        this.mIconId = i;
        this.mShortcutIconId = i2;
        this.mTextId = i3;
        this.mBooleanValue = z;
        this.mMilliSeconds = i4;
        this.mCountDownIconId = i5;
        this.mSoundType = type;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.SELF_TIMER;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mIconId;
    }

    public int getShortcutId() {
        return this.mShortcutIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return toString();
    }

    @Override // com.sonyericsson.cameracommon.settings.SelfTimerInterface
    public int getDurationInMillisecond() {
        return this.mMilliSeconds;
    }

    @Override // com.sonyericsson.cameracommon.settings.SelfTimerInterface
    public int getCountDownIconId() {
        return this.mCountDownIconId;
    }

    public static SelfTimer[] getOptions() {
        ArrayList arrayList = new ArrayList(Arrays.asList(values()));
        arrayList.remove(GESTURE_SHUTTER_COUNT_DOWN);
        arrayList.remove(SIDE_COUNT_DOWN);
        arrayList.remove(LAUNCH_AND_CAPTURE_COUNT_DOWN);
        SelfTimer[] selfTimerArr = new SelfTimer[arrayList.size()];
        arrayList.toArray(selfTimerArr);
        return selfTimerArr;
    }

    public static SelfTimer getDefaultValue(CapturingMode capturingMode) {
        return OFF;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    @Override // com.sonyericsson.cameracommon.settings.SelfTimerInterface
    public SoundPlayer.Type getSoundType() {
        return this.mSoundType;
    }

    public void setDurationInMillisecond(int i) {
        if (this == LAUNCH_AND_CAPTURE_COUNT_DOWN) {
            this.mMilliSeconds = i;
            if (this.mMilliSeconds < 4000) {
                this.mSoundType = SoundPlayer.Type.SELF_TIMER_3SEC;
            } else if (this.mMilliSeconds == 4000) {
                this.mSoundType = SoundPlayer.Type.SELF_TIMER_4SEC;
            } else {
                this.mSoundType = SoundPlayer.Type.SELF_TIMER_1SEC;
            }
        }
    }
}
