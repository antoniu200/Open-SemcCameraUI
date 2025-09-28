package com.sonyericsson.android.camera.util.capability;

import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCameraParameters;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class BypassCameraStaticParameters {
    private static final String DELIMITER_COMMA = ",";
    private static final String TAG = "BypassCameraStaticParameters";
    private final BypassCameraParameters mParams;

    public BypassCameraStaticParameters(BypassCameraParameters bypassCameraParameters) {
        this.mParams = bypassCameraParameters;
    }

    public List<String> getSupportedSuperSlowmotion() {
        String[] strArrSplit;
        ArrayList arrayList = new ArrayList();
        String str = this.mParams.get(BypassCameraParameters.KEY_SUPER_SLOW_VALUES);
        if (str != null && (strArrSplit = str.split(DELIMITER_COMMA)) != null) {
            for (String str2 : strArrSplit) {
                arrayList.add(str2);
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedSuperSlowmotion() : " + arrayList);
        }
        return arrayList;
    }

    public List<VideoConfiguration> getSupportedSuperSlowConfiguration() {
        return VideoConfiguration.parse(this.mParams.get(BypassCameraParameters.KEY_VIDEO_SUPER_SLOW_CONFIGURATION));
    }

    public List<String> getSupportedClimaxRecognition() {
        String[] strArrSplit;
        ArrayList arrayList = new ArrayList();
        String str = this.mParams.get(BypassCameraParameters.KEY_CLIMAX_RECOGNITION_VALUES);
        if (str != null && (strArrSplit = str.split(DELIMITER_COMMA)) != null) {
            for (String str2 : strArrSplit) {
                arrayList.add(str2);
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedClimaxRecognition() : " + arrayList);
        }
        return arrayList;
    }

    public List<String> getSupportedBurst() {
        String[] strArrSplit;
        ArrayList arrayList = new ArrayList();
        String str = this.mParams.get(BypassCameraParameters.KEY_BURST_VALUES);
        if (str != null && (strArrSplit = str.split(DELIMITER_COMMA)) != null) {
            for (String str2 : strArrSplit) {
                arrayList.add(str2);
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedBurst() : " + arrayList);
        }
        return arrayList;
    }

    public List<String> getSupportedVideoStabilizer() {
        String[] strArrSplit;
        ArrayList arrayList = new ArrayList();
        String str = this.mParams.get(BypassCameraParameters.KEY_VIDEO_STABILIZERS_VALUES);
        if (str != null && (strArrSplit = str.split(DELIMITER_COMMA)) != null) {
            for (String str2 : strArrSplit) {
                arrayList.add(str2);
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getSupportedVideoStabilizer() : " + arrayList);
        }
        return arrayList;
    }

    public List<VideoConfiguration> getSupportedHighFrameRateVideoConfiguration() {
        return VideoConfiguration.parse(this.mParams.get(BypassCameraParameters.KEY_VIDEO_HIGH_FRAME_RATE_CONFIGURATION));
    }

    public List<VideoConfiguration> getSupportedSteadyShotConfiguration() {
        return VideoConfiguration.parse(this.mParams.get(BypassCameraParameters.KEY_VIDEO_STABILIZERS_STEADY_SHOT_CONFIGURATION));
    }

    public List<VideoConfiguration> getSupportedIntelligentActiveConfiguration() {
        return VideoConfiguration.parse(this.mParams.get(BypassCameraParameters.KEY_VIDEO_STABILIZERS_INTELLIGENT_ACTIVE_CONFIGURATION));
    }

    public boolean isVideoHdrSupported() {
        String str = this.mParams.get(BypassCameraParameters.KEY_VIDEO_HDR_VALUES);
        if (str == null) {
            return false;
        }
        for (String str2 : str.split(DELIMITER_COMMA)) {
            if ("on".equals(str2)) {
                return true;
            }
        }
        return false;
    }
}
