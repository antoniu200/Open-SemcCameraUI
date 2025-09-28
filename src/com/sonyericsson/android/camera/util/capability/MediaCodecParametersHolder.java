package com.sonyericsson.android.camera.util.capability;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MediaCodecParametersHolder implements ParameterHolder {
    public static final int INVALID_PROFILE = 0;
    private Bundle mParameters;

    private enum Key {
        HDR_VIDEO_RECORDING_PROFILE
    }

    @Override // com.sonyericsson.android.camera.util.capability.ParameterHolder
    public void prepare() {
        if (this.mParameters == null) {
            if (CamLog.DEBUG) {
                CamLog.d("prepare parameters from media codec: E");
            }
            this.mParameters = new Bundle();
            this.mParameters.putInt(Key.HDR_VIDEO_RECORDING_PROFILE.name(), decideVideoHdrProfile());
            if (CamLog.DEBUG) {
                CamLog.d("prepare parameters from media codec: X");
                return;
            }
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("already prepared");
        }
    }

    public int getVideoHdrProfile() {
        return getParameters().getInt(Key.HDR_VIDEO_RECORDING_PROFILE.name(), 1);
    }

    @NonNull
    private Bundle getParameters() {
        if (this.mParameters == null) {
            CamLog.w("get parameters but not prepared it.");
            prepare();
        }
        return this.mParameters;
    }

    private int decideVideoHdrProfile() {
        List<Integer> supportedEncoderProfiles = getSupportedEncoderProfiles("video/hevc");
        if (supportedEncoderProfiles == null || supportedEncoderProfiles.isEmpty()) {
            return 1;
        }
        int i = 4096;
        if (!supportedEncoderProfiles.contains(4096)) {
            i = supportedEncoderProfiles.contains(2) ? 2 : 1;
        }
        if (CamLog.DEBUG) {
            CamLog.d("decideVideoHdrProfile: Profile Found: " + i);
        }
        return i;
    }

    @NonNull
    private List<Integer> getSupportedEncoderProfiles(String str) {
        ArrayList arrayList = new ArrayList();
        MediaCodecInfo mediaCodecInfoFindEncoderCodec = findEncoderCodec(str);
        if (mediaCodecInfoFindEncoderCodec == null) {
            CamLog.e("codec is not found: type = " + str);
            return arrayList;
        }
        MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfoFindEncoderCodec.getCapabilitiesForType(str);
        if (capabilitiesForType == null) {
            CamLog.e("capabilities is not found: type = " + str + ", codec = " + mediaCodecInfoFindEncoderCodec.getName());
            return arrayList;
        }
        if (capabilitiesForType.profileLevels == null) {
            CamLog.e("profileLevel is not found: type = " + str + ", codec = " + mediaCodecInfoFindEncoderCodec.getName());
            return arrayList;
        }
        for (MediaCodecInfo.CodecProfileLevel codecProfileLevel : capabilitiesForType.profileLevels) {
            arrayList.add(Integer.valueOf(codecProfileLevel.profile));
        }
        return arrayList;
    }

    @Nullable
    private MediaCodecInfo findEncoderCodec(String str) {
        for (MediaCodecInfo mediaCodecInfo : new MediaCodecList(1).getCodecInfos()) {
            if (mediaCodecInfo.isEncoder()) {
                for (String str2 : mediaCodecInfo.getSupportedTypes()) {
                    if (str.equalsIgnoreCase(str2)) {
                        if (CamLog.DEBUG) {
                            CamLog.d("findEncoderCodec: found info for " + str + ": name = " + mediaCodecInfo.getName());
                        }
                        return mediaCodecInfo;
                    }
                }
            }
        }
        CamLog.i("Not found CodecInfo for: " + str);
        return null;
    }
}
