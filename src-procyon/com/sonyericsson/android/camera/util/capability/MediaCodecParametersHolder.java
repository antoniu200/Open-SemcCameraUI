// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.media.MediaCodecInfo$CodecProfileLevel;
import android.media.MediaCodecInfo$CodecCapabilities;
import java.util.ArrayList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.media.MediaCodecList;
import android.media.MediaCodecInfo;
import java.util.List;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.Bundle;

public class MediaCodecParametersHolder implements ParameterHolder
{
    public static final int INVALID_PROFILE = 0;
    private Bundle mParameters;
    
    private int decideVideoHdrProfile() {
        final List<Integer> supportedEncoderProfiles = this.getSupportedEncoderProfiles("video/hevc");
        if (supportedEncoderProfiles != null && !supportedEncoderProfiles.isEmpty()) {
            int i = 4096;
            if (!supportedEncoderProfiles.contains(4096)) {
                if (supportedEncoderProfiles.contains(2)) {
                    i = 2;
                }
                else {
                    i = 1;
                }
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("decideVideoHdrProfile: Profile Found: ");
                sb.append(i);
                CamLog.d(sb.toString());
            }
            return i;
        }
        return 1;
    }
    
    @Nullable
    private MediaCodecInfo findEncoderCodec(final String s) {
        for (final MediaCodecInfo mediaCodecInfo : new MediaCodecList(1).getCodecInfos()) {
            if (mediaCodecInfo.isEncoder()) {
                final String[] supportedTypes = mediaCodecInfo.getSupportedTypes();
                for (int length2 = supportedTypes.length, j = 0; j < length2; ++j) {
                    if (s.equalsIgnoreCase(supportedTypes[j])) {
                        if (CamLog.DEBUG) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("findEncoderCodec: found info for ");
                            sb.append(s);
                            sb.append(": name = ");
                            sb.append(mediaCodecInfo.getName());
                            CamLog.d(sb.toString());
                        }
                        return mediaCodecInfo;
                    }
                }
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Not found CodecInfo for: ");
        sb2.append(s);
        CamLog.i(sb2.toString());
        return null;
    }
    
    @NonNull
    private Bundle getParameters() {
        if (this.mParameters == null) {
            CamLog.w("get parameters but not prepared it.");
            this.prepare();
        }
        return this.mParameters;
    }
    
    @NonNull
    private List<Integer> getSupportedEncoderProfiles(final String str) {
        final ArrayList list = new ArrayList();
        final MediaCodecInfo encoderCodec = this.findEncoderCodec(str);
        int i = 0;
        if (encoderCodec == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("codec is not found: type = ");
            sb.append(str);
            CamLog.e(sb.toString());
            return list;
        }
        final MediaCodecInfo$CodecCapabilities capabilitiesForType = encoderCodec.getCapabilitiesForType(str);
        if (capabilitiesForType == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("capabilities is not found: type = ");
            sb2.append(str);
            sb2.append(", codec = ");
            sb2.append(encoderCodec.getName());
            CamLog.e(sb2.toString());
            return list;
        }
        if (capabilitiesForType.profileLevels == null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("profileLevel is not found: type = ");
            sb3.append(str);
            sb3.append(", codec = ");
            sb3.append(encoderCodec.getName());
            CamLog.e(sb3.toString());
            return list;
        }
        for (MediaCodecInfo$CodecProfileLevel[] profileLevels = capabilitiesForType.profileLevels; i < profileLevels.length; ++i) {
            list.add(profileLevels[i].profile);
        }
        return list;
    }
    
    public int getVideoHdrProfile() {
        return this.getParameters().getInt(Key.HDR_VIDEO_RECORDING_PROFILE.name(), 1);
    }
    
    @Override
    public void prepare() {
        if (this.mParameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("prepare parameters from media codec: E");
            }
            (this.mParameters = new Bundle()).putInt(Key.HDR_VIDEO_RECORDING_PROFILE.name(), this.decideVideoHdrProfile());
            if (CamLog.DEBUG) {
                CamLog.d("prepare parameters from media codec: X");
            }
        }
        else if (CamLog.DEBUG) {
            CamLog.d("already prepared");
        }
    }
    
    private enum Key
    {
        private static final Key[] $VALUES;
        
        HDR_VIDEO_RECORDING_PROFILE;
        
        static {
            $VALUES = new Key[] { Key.HDR_VIDEO_RECORDING_PROFILE };
        }
    }
}
