package com.sonymobile.imageprocessor.bypasscamera2;

import com.sonyericsson.android.camera.device.CameraParameters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class BypassCameraParameters {
    public static final String BURST_OFF = "off";
    public static final String BURST_ON = "on";
    public static final String CLIMAX_RECOGNITION_AUTO = "auto";
    public static final String CLIMAX_RECOGNITION_OFF = "off";
    public static final String CLIMAX_RECOGNITION_ON = "on";
    public static final String KEY_BURST_VALUES = "burst-values";
    public static final String KEY_CLIMAX_RECOGNITION = "climax-recognition";
    public static final String KEY_CLIMAX_RECOGNITION_VALUES = "climax-recognition-values";
    public static final String KEY_SUPER_SLOW_VALUES = "super-slow-values";
    public static final String KEY_VIDEO_HDR_VALUES = "video-hdr-values";
    public static final String KEY_VIDEO_HIGH_FRAME_RATE_CONFIGURATION = "video-high-frame-rate-configuration";
    public static final String KEY_VIDEO_STABILIZERS_INTELLIGENT_ACTIVE_CONFIGURATION = "vs-intelligent-active-configuration";
    public static final String KEY_VIDEO_STABILIZERS_STEADY_SHOT_CONFIGURATION = "vs-steady-shot-configuration";
    public static final String KEY_VIDEO_STABILIZERS_VALUES = "vs-values";
    public static final String KEY_VIDEO_SUPER_SLOW_CONFIGURATION = "video-super-slow-configuration";
    public static final String SUPER_SLOW_OFF = "off";
    public static final String SUPER_SLOW_ON = "on";
    public static final String VIDEO_HDR_OFF = "off";
    public static final String VIDEO_HDR_ON = "on";
    private final Map<String, String> mMap = new LinkedHashMap();

    public void set(String str, String str2) {
        this.mMap.put(str, str2);
    }

    public void setInt(String str, int i) {
        this.mMap.put(str, String.valueOf(i));
    }

    public String get(String str) {
        return this.mMap.get(str);
    }

    public int getInt(String str) {
        return Integer.parseInt(this.mMap.get(str));
    }

    static class Capability {
        public List<String> mVideoStabilizationMode = new ArrayList();
        public List<String> mSuperSlowMode = new ArrayList();
        public List<String> mClimaxRecognitionMode = new ArrayList();
        public List<String> mBurstMode = new ArrayList();
        public List<String> mVideoHdrMode = new ArrayList();
        public List<Integer> mSuperSlowFrameNumList = new ArrayList();
        public List<SupportedInfo> mHighFrameRateSupportedInfoList = new ArrayList();
        public List<SupportedInfo> mSteadyShotSupportedInfoList = new ArrayList();
        public List<SupportedInfo> mIntelligentActiveSupportedInfoList = new ArrayList();
        public List<SupportedInfo> mSuperSlowSupportedInfoList = new ArrayList();

        Capability() {
        }

        public static class SupportedInfo {
            public final int fps;
            public final int height;
            public final int width;

            public SupportedInfo(int i, int i2, int i3) {
                this.width = i;
                this.height = i2;
                this.fps = i3;
            }
        }

        public enum VideoStabilizationMode {
            OFF(0, "off"),
            STEADY_SHOT(1, "on"),
            INTELLIGENT_ACTIVE(2, CameraParameters.VS_ON_INTELLIGENT_ACTIVE);

            public final int code;
            public final String value;

            VideoStabilizationMode(int i, String str) {
                this.code = i;
                this.value = str;
            }
        }

        public enum SuperSlowMode {
            OFF(0, "off"),
            ON(1, "on");

            public final int code;
            public final String value;

            SuperSlowMode(int i, String str) {
                this.code = i;
                this.value = str;
            }
        }

        public enum ClimaxRecognitionMode {
            OFF(0, "off"),
            AUTO(1, "auto"),
            ON(2, "on");

            public final int code;
            public final String value;

            ClimaxRecognitionMode(int i, String str) {
                this.code = i;
                this.value = str;
            }
        }

        public enum BurstMode {
            OFF(0, "off"),
            ON(1, "on");

            public final int code;
            public final String value;

            BurstMode(int i, String str) {
                this.code = i;
                this.value = str;
            }
        }

        public enum VideoHdrMode {
            OFF(0, "off"),
            ON(1, "on");

            public final int code;
            public final String value;

            VideoHdrMode(int i, String str) {
                this.code = i;
                this.value = str;
            }
        }

        public void setVideoStabilizationMode(int i) {
            for (VideoStabilizationMode videoStabilizationMode : VideoStabilizationMode.values()) {
                if (videoStabilizationMode.code == (videoStabilizationMode.code & i)) {
                    this.mVideoStabilizationMode.add(videoStabilizationMode.value);
                }
            }
        }

        public void setSuperSlowMode(int i) {
            for (SuperSlowMode superSlowMode : SuperSlowMode.values()) {
                if (superSlowMode.code == (superSlowMode.code & i)) {
                    this.mSuperSlowMode.add(superSlowMode.value);
                }
            }
        }

        public void setClimaxRecognitionMode(int i) {
            for (ClimaxRecognitionMode climaxRecognitionMode : ClimaxRecognitionMode.values()) {
                if (climaxRecognitionMode.code == (climaxRecognitionMode.code & i)) {
                    this.mClimaxRecognitionMode.add(climaxRecognitionMode.value);
                }
            }
        }

        public void setBurstMode(int i) {
            for (BurstMode burstMode : BurstMode.values()) {
                if (burstMode.code == (burstMode.code & i)) {
                    this.mBurstMode.add(burstMode.value);
                }
            }
        }

        public void setVideoHdrMode(int i) {
            for (VideoHdrMode videoHdrMode : VideoHdrMode.values()) {
                if (videoHdrMode.code == (videoHdrMode.code & i)) {
                    this.mVideoHdrMode.add(videoHdrMode.value);
                }
            }
        }

        public void addSuperSlowFrameNum(int i) {
            this.mSuperSlowFrameNumList.add(Integer.valueOf(i));
        }

        public void addHighFrameRateSupportedInfo(int i, int i2, int i3) {
            this.mHighFrameRateSupportedInfoList.add(new SupportedInfo(i, i2, i3));
        }

        public void addSteadyShotSupportedInfo(int i, int i2, int i3) {
            this.mSteadyShotSupportedInfoList.add(new SupportedInfo(i, i2, i3));
        }

        public void addIntelligentActiveSupportedInfo(int i, int i2, int i3) {
            this.mIntelligentActiveSupportedInfoList.add(new SupportedInfo(i, i2, i3));
        }

        public void addSuperSlowSupportedInfo(int i, int i2, int i3) {
            this.mSuperSlowSupportedInfoList.add(new SupportedInfo(i, i2, i3));
        }
    }
}
