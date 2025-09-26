// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import com.sonyericsson.android.camera.util.CamLog;
import java.util.ArrayList;
import java.util.List;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCameraParameters;

final class BypassCameraStaticParameters
{
    private static final String DELIMITER_COMMA = ",";
    private static final String TAG = "BypassCameraStaticParameters";
    private final BypassCameraParameters mParams;
    
    public BypassCameraStaticParameters(final BypassCameraParameters mParams) {
        this.mParams = mParams;
    }
    
    public List<String> getSupportedBurst() {
        final ArrayList obj = new ArrayList();
        final String value = this.mParams.get("burst-values");
        if (value != null) {
            final String[] split = value.split(",");
            if (split != null) {
                for (int i = 0; i < split.length; ++i) {
                    obj.add(split[i]);
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedBurst() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedClimaxRecognition() {
        final ArrayList obj = new ArrayList();
        final String value = this.mParams.get("climax-recognition-values");
        if (value != null) {
            final String[] split = value.split(",");
            if (split != null) {
                for (int i = 0; i < split.length; ++i) {
                    obj.add(split[i]);
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedClimaxRecognition() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<VideoConfiguration> getSupportedHighFrameRateVideoConfiguration() {
        return VideoConfiguration.parse(this.mParams.get("video-high-frame-rate-configuration"));
    }
    
    public List<VideoConfiguration> getSupportedIntelligentActiveConfiguration() {
        return VideoConfiguration.parse(this.mParams.get("vs-intelligent-active-configuration"));
    }
    
    public List<VideoConfiguration> getSupportedSteadyShotConfiguration() {
        return VideoConfiguration.parse(this.mParams.get("vs-steady-shot-configuration"));
    }
    
    public List<VideoConfiguration> getSupportedSuperSlowConfiguration() {
        return VideoConfiguration.parse(this.mParams.get("video-super-slow-configuration"));
    }
    
    public List<String> getSupportedSuperSlowmotion() {
        final ArrayList obj = new ArrayList();
        final String value = this.mParams.get("super-slow-values");
        if (value != null) {
            final String[] split = value.split(",");
            if (split != null) {
                for (int i = 0; i < split.length; ++i) {
                    obj.add(split[i]);
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedSuperSlowmotion() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public List<String> getSupportedVideoStabilizer() {
        final ArrayList obj = new ArrayList();
        final String value = this.mParams.get("vs-values");
        if (value != null) {
            final String[] split = value.split(",");
            if (split != null) {
                for (int i = 0; i < split.length; ++i) {
                    obj.add(split[i]);
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getSupportedVideoStabilizer() : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    public boolean isVideoHdrSupported() {
        final String value = this.mParams.get("video-hdr-values");
        if (value == null) {
            return false;
        }
        final String[] split = value.split(",");
        for (int length = split.length, i = 0; i < length; ++i) {
            if ("on".equals(split[i])) {
                return true;
            }
        }
        return false;
    }
}
