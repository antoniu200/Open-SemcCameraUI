package com.sonyericsson.android.camera.parameter.dependency;

import android.support.annotation.NonNull;
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
public class VideoHdrApplier extends DependencyApplier {
    private static final String TAG = "VideoHdrApplier";
    private final VideoHdr mValue;

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
    }

    public VideoHdrApplier(@NonNull VideoHdr videoHdr) {
        this.mValue = videoHdr;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        VideoSize videoSize = (VideoSize) capturingModeParams.mVideoSize.get();
        if (this.mValue == VideoHdr.HDR_ON) {
            ParameterUtil.unavailable(capturingModeParams.mVideoCodec, capturingModeParams.mVideoCodec.get());
            ParameterUtil.unavailable(capturingModeParams.mVideoShutterTrigger, VideoShutterTrigger.OFF);
            if (PlatformCapability.isVideoStabilizerOnHdrSupported(capturingModeParams.getActionMode().mCameraId)) {
                if (((VideoStabilizer) capturingModeParams.mVideoStabilizer.get()) == VideoStabilizer.INTELLIGENT_ACTIVE) {
                    ParameterUtil.applyRecommendedValue(capturingModeParams.mVideoStabilizer, VideoStabilizer.STEADY_SHOT);
                }
            } else {
                ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
            }
            ParameterUtil.unavailable(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
            if (((FusionMode) capturingModeParams.mFusionMode.get()).getKey().getSelectability() != UserSettingSelectability.FIXED) {
                ParameterUtil.unavailable(capturingModeParams.mFusionMode, capturingModeParams.mFusionMode.get());
                DependencyApplier dependencyApplierCreate = DependencyApplier.create(capturingModeParams.mFusionMode.get());
                if (dependencyApplierCreate != null) {
                    dependencyApplierCreate.apply(capturingModeParams);
                    return;
                }
                return;
            }
            return;
        }
        CameraInfo.CameraId cameraId = capturingModeParams.getActionMode().mCameraId;
        ParameterUtil.reset(capturingModeParams.mVideoCodec);
        if (videoSize != VideoSize.FOUR_K_UHD_H264 && videoSize != VideoSize.FOUR_K_UHD_H265) {
            ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
            ParameterUtil.reset(capturingModeParams.mObjectTracking);
        } else {
            ParameterUtil.unavailable(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
        }
        ParameterUtil.reset(capturingModeParams.mVideoCodec, capturingModeParams.mVideoCodec.get());
        if (((FusionMode) capturingModeParams.mFusionMode.get()).getKey().getSelectability() == UserSettingSelectability.FIXED || !PlatformCapability.isFusionSupportedWith(cameraId, (VideoSize) capturingModeParams.mVideoSize.get())) {
            return;
        }
        ParameterUtil.reset(capturingModeParams.mFusionMode);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(capturingModeParams.mFusionMode.get());
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(capturingModeParams);
        }
    }
}
