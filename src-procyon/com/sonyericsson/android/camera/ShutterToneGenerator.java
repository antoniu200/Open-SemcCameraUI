// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;

public class ShutterToneGenerator
{
    private static final String SOUND_HOME = "/system/media/audio/camera/";
    public static final String TAG = "ShutterToneGenerator";
    
    public static String getSoundFilePath(final Type type, final ShutterSound shutterSound) {
        final StringBuffer sb = new StringBuffer();
        if (shutterSound.getBooleanValue()) {
            sb.append("/system/media/audio/camera/");
            if (type.isCommonSound()) {
                sb.append(type.getFileName());
            }
            else {
                sb.append(shutterSound.getDirectoryName());
                sb.append(type.getFileName());
            }
        }
        else {
            sb.append("off");
        }
        return sb.toString();
    }
    
    public enum Type
    {
        private static final Type[] $VALUES;
        
        SOUND_AF_SUCCESS("common/af_success.m4a", true), 
        SOUND_BURST_SHUTTER("shutter_done.wav", false), 
        SOUND_FAST_CAPTURE_SHUTTER_DONE("fastcapture_launch_and_capture_done.wav", false), 
        SOUND_OFF("no_sound.m4a", false), 
        SOUND_SELFTIMER_10SEC("common/selftimer_10sec.m4a", true), 
        SOUND_SELFTIMER_3SEC("common/selftimer_3sec.m4a", true);
        
        private String mFileName;
        private boolean mIsCommonSound;
        
        static {
            $VALUES = new Type[] { Type.SOUND_AF_SUCCESS, Type.SOUND_SELFTIMER_3SEC, Type.SOUND_SELFTIMER_10SEC, Type.SOUND_OFF, Type.SOUND_BURST_SHUTTER, Type.SOUND_FAST_CAPTURE_SHUTTER_DONE };
        }
        
        private Type(final String mFileName, final boolean mIsCommonSound) {
            this.mFileName = mFileName;
            this.mIsCommonSound = mIsCommonSound;
        }
        
        public String getFileName() {
            return this.mFileName;
        }
        
        public boolean isCommonSound() {
            return this.mIsCommonSound;
        }
    }
}
