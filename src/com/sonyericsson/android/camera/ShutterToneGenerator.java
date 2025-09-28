package com.sonyericsson.android.camera;

import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ShutterToneGenerator {
    private static final String SOUND_HOME = "/system/media/audio/camera/";
    public static final String TAG = "ShutterToneGenerator";

    public enum Type {
        SOUND_AF_SUCCESS("common/af_success.m4a", true),
        SOUND_SELFTIMER_3SEC("common/selftimer_3sec.m4a", true),
        SOUND_SELFTIMER_10SEC("common/selftimer_10sec.m4a", true),
        SOUND_OFF("no_sound.m4a", false),
        SOUND_BURST_SHUTTER("shutter_done.wav", false),
        SOUND_FAST_CAPTURE_SHUTTER_DONE("fastcapture_launch_and_capture_done.wav", false);

        private String mFileName;
        private boolean mIsCommonSound;

        Type(String str, boolean z) {
            this.mFileName = str;
            this.mIsCommonSound = z;
        }

        public String getFileName() {
            return this.mFileName;
        }

        public boolean isCommonSound() {
            return this.mIsCommonSound;
        }
    }

    public static String getSoundFilePath(Type type, ShutterSound shutterSound) {
        StringBuffer stringBuffer = new StringBuffer();
        if (shutterSound.getBooleanValue().booleanValue()) {
            stringBuffer.append(SOUND_HOME);
            if (type.isCommonSound()) {
                stringBuffer.append(type.getFileName());
            } else {
                stringBuffer.append(shutterSound.getDirectoryName());
                stringBuffer.append(type.getFileName());
            }
        } else {
            stringBuffer.append("off");
        }
        return stringBuffer.toString();
    }
}
