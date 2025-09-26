// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;

public class VideoHdrApplier extends DependencyApplier
{
    private static final String TAG = "VideoHdrApplier";
    private final VideoHdr mValue;
    
    public VideoHdrApplier(@NonNull final VideoHdr mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        final VideoSize videoSize = capturingModeParams.mVideoSize.get();
        if (this.mValue == VideoHdr.HDR_ON) {
            ParameterUtil.unavailable(capturingModeParams.mVideoCodec, capturingModeParams.mVideoCodec.get());
            ParameterUtil.unavailable(capturingModeParams.mVideoShutterTrigger, VideoShutterTrigger.OFF);
            if (PlatformCapability.isVideoStabilizerOnHdrSupported(capturingModeParams.getActionMode().mCameraId)) {
                if (capturingModeParams.mVideoStabilizer.get() == VideoStabilizer.INTELLIGENT_ACTIVE) {
                    ParameterUtil.applyRecommendedValue(capturingModeParams.mVideoStabilizer, VideoStabilizer.STEADY_SHOT);
                }
            }
            else {
                ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
            }
            ParameterUtil.unavailable(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
            if (capturingModeParams.mFusionMode.get().getKey().getSelectability() != UserSettingSelectability.FIXED) {
                ParameterUtil.unavailable(capturingModeParams.mFusionMode, capturingModeParams.mFusionMode.get());
                final DependencyApplier create = DependencyApplier.create(capturingModeParams.mFusionMode.get());
                if (create != null) {
                    create.apply(capturingModeParams);
                }
            }
        }
        else {
            final CameraInfo.CameraId mCameraId = capturingModeParams.getActionMode().mCameraId;
            ParameterUtil.reset(capturingModeParams.mVideoCodec);
            if (videoSize != VideoSize.FOUR_K_UHD_H264 && videoSize != VideoSize.FOUR_K_UHD_H265) {
                ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
                ParameterUtil.reset(capturingModeParams.mObjectTracking);
            }
            else {
                ParameterUtil.unavailable(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
            }
            ParameterUtil.reset(capturingModeParams.mVideoCodec, capturingModeParams.mVideoCodec.get());
            if (capturingModeParams.mFusionMode.get().getKey().getSelectability() != UserSettingSelectability.FIXED && PlatformCapability.isFusionSupportedWith(mCameraId, capturingModeParams.mVideoSize.get())) {
                ParameterUtil.reset(capturingModeParams.mFusionMode);
                final DependencyApplier create2 = DependencyApplier.create(capturingModeParams.mFusionMode.get());
                if (create2 != null) {
                    create2.apply(capturingModeParams);
                }
            }
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
    }
}
