// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.imageprocessor.bypasscamera2;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BypassCameraParameters
{
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
    private final Map<String, String> mMap;
    
    public BypassCameraParameters() {
        this.mMap = new LinkedHashMap<String, String>();
    }
    
    public String get(final String s) {
        return this.mMap.get(s);
    }
    
    public int getInt(final String s) {
        return Integer.parseInt(this.mMap.get(s));
    }
    
    public void set(final String s, final String s2) {
        this.mMap.put(s, s2);
    }
    
    public void setInt(final String s, final int i) {
        this.mMap.put(s, String.valueOf(i));
    }
    
    static class Capability
    {
        public List<String> mBurstMode;
        public List<String> mClimaxRecognitionMode;
        public List<SupportedInfo> mHighFrameRateSupportedInfoList;
        public List<SupportedInfo> mIntelligentActiveSupportedInfoList;
        public List<SupportedInfo> mSteadyShotSupportedInfoList;
        public List<Integer> mSuperSlowFrameNumList;
        public List<String> mSuperSlowMode;
        public List<SupportedInfo> mSuperSlowSupportedInfoList;
        public List<String> mVideoHdrMode;
        public List<String> mVideoStabilizationMode;
        
        Capability() {
            this.mVideoStabilizationMode = new ArrayList<String>();
            this.mSuperSlowMode = new ArrayList<String>();
            this.mClimaxRecognitionMode = new ArrayList<String>();
            this.mBurstMode = new ArrayList<String>();
            this.mVideoHdrMode = new ArrayList<String>();
            this.mSuperSlowFrameNumList = new ArrayList<Integer>();
            this.mHighFrameRateSupportedInfoList = new ArrayList<SupportedInfo>();
            this.mSteadyShotSupportedInfoList = new ArrayList<SupportedInfo>();
            this.mIntelligentActiveSupportedInfoList = new ArrayList<SupportedInfo>();
            this.mSuperSlowSupportedInfoList = new ArrayList<SupportedInfo>();
        }
        
        public void addHighFrameRateSupportedInfo(final int n, final int n2, final int n3) {
            this.mHighFrameRateSupportedInfoList.add(new SupportedInfo(n, n2, n3));
        }
        
        public void addIntelligentActiveSupportedInfo(final int n, final int n2, final int n3) {
            this.mIntelligentActiveSupportedInfoList.add(new SupportedInfo(n, n2, n3));
        }
        
        public void addSteadyShotSupportedInfo(final int n, final int n2, final int n3) {
            this.mSteadyShotSupportedInfoList.add(new SupportedInfo(n, n2, n3));
        }
        
        public void addSuperSlowFrameNum(final int i) {
            this.mSuperSlowFrameNumList.add(i);
        }
        
        public void addSuperSlowSupportedInfo(final int n, final int n2, final int n3) {
            this.mSuperSlowSupportedInfoList.add(new SupportedInfo(n, n2, n3));
        }
        
        public void setBurstMode(final int n) {
            for (final BurstMode burstMode : BurstMode.values()) {
                if (burstMode.code == (burstMode.code & n)) {
                    this.mBurstMode.add(burstMode.value);
                }
            }
        }
        
        public void setClimaxRecognitionMode(final int n) {
            for (final ClimaxRecognitionMode climaxRecognitionMode : ClimaxRecognitionMode.values()) {
                if (climaxRecognitionMode.code == (climaxRecognitionMode.code & n)) {
                    this.mClimaxRecognitionMode.add(climaxRecognitionMode.value);
                }
            }
        }
        
        public void setSuperSlowMode(final int n) {
            for (final SuperSlowMode superSlowMode : SuperSlowMode.values()) {
                if (superSlowMode.code == (superSlowMode.code & n)) {
                    this.mSuperSlowMode.add(superSlowMode.value);
                }
            }
        }
        
        public void setVideoHdrMode(final int n) {
            for (final VideoHdrMode videoHdrMode : VideoHdrMode.values()) {
                if (videoHdrMode.code == (videoHdrMode.code & n)) {
                    this.mVideoHdrMode.add(videoHdrMode.value);
                }
            }
        }
        
        public void setVideoStabilizationMode(final int n) {
            for (final VideoStabilizationMode videoStabilizationMode : VideoStabilizationMode.values()) {
                if (videoStabilizationMode.code == (videoStabilizationMode.code & n)) {
                    this.mVideoStabilizationMode.add(videoStabilizationMode.value);
                }
            }
        }
        
        public enum BurstMode
        {
            private static final BurstMode[] $VALUES;
            
            OFF(0, "off"), 
            ON(1, "on");
            
            public final int code;
            public final String value;
            
            static {
                $VALUES = new BurstMode[] { BurstMode.OFF, BurstMode.ON };
            }
            
            private BurstMode(final int code, final String value) {
                this.code = code;
                this.value = value;
            }
        }
        
        public enum ClimaxRecognitionMode
        {
            private static final ClimaxRecognitionMode[] $VALUES;
            
            AUTO(1, "auto"), 
            OFF(0, "off"), 
            ON(2, "on");
            
            public final int code;
            public final String value;
            
            static {
                $VALUES = new ClimaxRecognitionMode[] { ClimaxRecognitionMode.OFF, ClimaxRecognitionMode.AUTO, ClimaxRecognitionMode.ON };
            }
            
            private ClimaxRecognitionMode(final int code, final String value) {
                this.code = code;
                this.value = value;
            }
        }
        
        public enum SuperSlowMode
        {
            private static final SuperSlowMode[] $VALUES;
            
            OFF(0, "off"), 
            ON(1, "on");
            
            public final int code;
            public final String value;
            
            static {
                $VALUES = new SuperSlowMode[] { SuperSlowMode.OFF, SuperSlowMode.ON };
            }
            
            private SuperSlowMode(final int code, final String value) {
                this.code = code;
                this.value = value;
            }
        }
        
        public static class SupportedInfo
        {
            public final int fps;
            public final int height;
            public final int width;
            
            public SupportedInfo(final int width, final int height, final int fps) {
                this.width = width;
                this.height = height;
                this.fps = fps;
            }
        }
        
        public enum VideoHdrMode
        {
            private static final VideoHdrMode[] $VALUES;
            
            OFF(0, "off"), 
            ON(1, "on");
            
            public final int code;
            public final String value;
            
            static {
                $VALUES = new VideoHdrMode[] { VideoHdrMode.OFF, VideoHdrMode.ON };
            }
            
            private VideoHdrMode(final int code, final String value) {
                this.code = code;
                this.value = value;
            }
        }
        
        public enum VideoStabilizationMode
        {
            private static final VideoStabilizationMode[] $VALUES;
            
            INTELLIGENT_ACTIVE(2, "intelligent_active"), 
            OFF(0, "off"), 
            STEADY_SHOT(1, "on");
            
            public final int code;
            public final String value;
            
            static {
                $VALUES = new VideoStabilizationMode[] { VideoStabilizationMode.OFF, VideoStabilizationMode.STEADY_SHOT, VideoStabilizationMode.INTELLIGENT_ACTIVE };
            }
            
            private VideoStabilizationMode(final int code, final String value) {
                this.code = code;
                this.value = value;
            }
        }
    }
}
