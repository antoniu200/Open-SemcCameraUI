package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VideoSizeApplier extends DependencyApplier {
    public static final String TAG = "VideoSizeApplier";
    private final VideoSize mValue;

    public VideoSizeApplier(VideoSize videoSize) {
        this.mValue = videoSize;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        CameraInfo.CameraId cameraId = capturingModeParams.getActionMode().mCameraId;
        VideoSize videoSize = (VideoSize) capturingModeParams.mVideoSize.get();
        switch (this.mValue) {
            case VGA:
            case MMS:
                applyCollections(cameraId, videoSize, capturingModeParams);
                break;
            case FULL_HD_60FPS:
                apply60Fps(cameraId, videoSize, capturingModeParams);
                break;
            case FOUR_K_UHD_H264:
            case FOUR_K_UHD_H265:
                apply4k(cameraId, videoSize, capturingModeParams);
                break;
            case FULL_HD:
                apply30Fps(cameraId, videoSize, capturingModeParams);
                break;
            default:
                applyOther(cameraId, videoSize, capturingModeParams);
                break;
        }
        if (((FusionMode) capturingModeParams.mFusionMode.get()).getKey().getSelectability() != UserSettingSelectability.FIXED) {
            if (!PlatformCapability.isFusionSupportedWith(cameraId, videoSize) || capturingModeParams.mVideoHdr.get() == VideoHdr.HDR_ON) {
                ParameterUtil.unavailable(capturingModeParams.mFusionMode, capturingModeParams.mFusionMode.get());
            } else {
                ParameterUtil.reset(capturingModeParams.mFusionMode);
            }
            DependencyApplier dependencyApplierCreate = DependencyApplier.create(capturingModeParams.mFusionMode.get());
            if (dependencyApplierCreate != null) {
                dependencyApplierCreate.apply(capturingModeParams);
            }
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        if ((this.mValue == VideoSize.FOUR_K_UHD_H264 || this.mValue == VideoSize.FOUR_K_UHD_H265) && capturingModeParams.mVideoHdr.get() != VideoHdr.HDR_ON) {
            ParameterUtil.reset(capturingModeParams.mObjectTracking);
            ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
        }
    }

    private void applyCollections(CameraInfo.CameraId cameraId, VideoSize videoSize, CapturingModeParams capturingModeParams) {
        boolean zIsSteadyShotSupported = VideoStabilizer.isSteadyShotSupported(cameraId, videoSize);
        boolean zIsIntelligentActiveAvailable = DependencyCheckUtil.isIntelligentActiveAvailable(cameraId, videoSize, (VideoHdr) capturingModeParams.mVideoHdr.get());
        if (zIsSteadyShotSupported || zIsIntelligentActiveAvailable) {
            ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
        } else {
            ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
        }
        ParameterUtil.unavailable(capturingModeParams.mVideoHdr, VideoHdr.HDR_OFF);
        ParameterUtil.reset(capturingModeParams.mVideoCodec);
        ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
        ParameterUtil.reset(capturingModeParams.mObjectTracking);
    }

    private void apply60Fps(CameraInfo.CameraId cameraId, VideoSize videoSize, CapturingModeParams capturingModeParams) {
        if (VideoStabilizer.isSteadyShotSupported(cameraId, videoSize)) {
            if (capturingModeParams.mVideoStabilizer.get() == VideoStabilizer.INTELLIGENT_ACTIVE) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mVideoStabilizer, VideoStabilizer.STEADY_SHOT);
            } else {
                ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
            }
        } else {
            ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
        }
        ParameterUtil.unavailable(capturingModeParams.mVideoHdr, VideoHdr.HDR_OFF);
        ParameterUtil.reset(capturingModeParams.mVideoCodec);
        ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
        ParameterUtil.reset(capturingModeParams.mObjectTracking);
    }

    private void apply4k(CameraInfo.CameraId cameraId, VideoSize videoSize, CapturingModeParams capturingModeParams) {
        ParameterUtil.unavailable(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
        ParameterUtil.unavailable(capturingModeParams.mVideoShutterTrigger, VideoShutterTrigger.OFF);
        if (capturingModeParams.mVideoHdr.get() != VideoHdr.HDR_ON) {
            if (VideoStabilizer.isSteadyShotSupported(cameraId, videoSize)) {
                if (capturingModeParams.mVideoStabilizer.get() == VideoStabilizer.INTELLIGENT_ACTIVE) {
                    ParameterUtil.applyRecommendedValue(capturingModeParams.mVideoStabilizer, VideoStabilizer.STEADY_SHOT);
                } else {
                    ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
                }
            } else {
                ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
            }
            ParameterUtil.reset(capturingModeParams.mVideoCodec);
        }
        ParameterUtil.reset(capturingModeParams.mVideoHdr);
    }

    private void applyOther(CameraInfo.CameraId cameraId, VideoSize videoSize, CapturingModeParams capturingModeParams) {
        boolean zIsSteadyShotSupported = VideoStabilizer.isSteadyShotSupported(cameraId, videoSize);
        boolean zIsIntelligentActiveAvailable = DependencyCheckUtil.isIntelligentActiveAvailable(cameraId, videoSize, (VideoHdr) capturingModeParams.mVideoHdr.get());
        if (zIsSteadyShotSupported || zIsIntelligentActiveAvailable) {
            ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
        } else {
            ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
        }
        ParameterUtil.unavailable(capturingModeParams.mVideoHdr, VideoHdr.HDR_OFF);
        ParameterUtil.reset(capturingModeParams.mVideoCodec);
        ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
        ParameterUtil.reset(capturingModeParams.mObjectTracking);
    }

    private void apply30Fps(CameraInfo.CameraId cameraId, VideoSize videoSize, CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mVideoHdr.get() != VideoHdr.HDR_ON) {
            boolean zIsSteadyShotSupported = VideoStabilizer.isSteadyShotSupported(cameraId, videoSize);
            boolean zIsIntelligentActiveAvailable = DependencyCheckUtil.isIntelligentActiveAvailable(cameraId, videoSize, (VideoHdr) capturingModeParams.mVideoHdr.get());
            if (zIsSteadyShotSupported || zIsIntelligentActiveAvailable) {
                ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
            } else {
                ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
            }
            ParameterUtil.reset(capturingModeParams.mVideoHdr);
            ParameterUtil.reset(capturingModeParams.mVideoCodec);
            ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
            return;
        }
        ParameterUtil.unavailable(capturingModeParams.mVideoShutterTrigger, VideoShutterTrigger.OFF);
    }
}
