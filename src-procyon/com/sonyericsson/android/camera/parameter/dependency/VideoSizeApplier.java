// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;

public class VideoSizeApplier extends DependencyApplier
{
    public static final String TAG = "VideoSizeApplier";
    private final VideoSize mValue;
    
    public VideoSizeApplier(final VideoSize mValue) {
        this.mValue = mValue;
    }
    
    private void apply30Fps(final CameraInfo.CameraId cameraId, final VideoSize videoSize, final CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mVideoHdr.get() != VideoHdr.HDR_ON) {
            final boolean steadyShotSupported = VideoStabilizer.isSteadyShotSupported(cameraId, videoSize);
            final boolean intelligentActiveAvailable = DependencyCheckUtil.isIntelligentActiveAvailable(cameraId, videoSize, capturingModeParams.mVideoHdr.get());
            if (!steadyShotSupported && !intelligentActiveAvailable) {
                ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
            }
            else {
                ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
            }
            ParameterUtil.reset(capturingModeParams.mVideoHdr);
            ParameterUtil.reset(capturingModeParams.mVideoCodec);
            ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
        }
        else {
            ParameterUtil.unavailable(capturingModeParams.mVideoShutterTrigger, VideoShutterTrigger.OFF);
        }
    }
    
    private void apply4k(final CameraInfo.CameraId cameraId, final VideoSize videoSize, final CapturingModeParams capturingModeParams) {
        ParameterUtil.unavailable(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
        ParameterUtil.unavailable(capturingModeParams.mVideoShutterTrigger, VideoShutterTrigger.OFF);
        if (capturingModeParams.mVideoHdr.get() != VideoHdr.HDR_ON) {
            if (VideoStabilizer.isSteadyShotSupported(cameraId, videoSize)) {
                if (capturingModeParams.mVideoStabilizer.get() == VideoStabilizer.INTELLIGENT_ACTIVE) {
                    ParameterUtil.applyRecommendedValue(capturingModeParams.mVideoStabilizer, VideoStabilizer.STEADY_SHOT);
                }
                else {
                    ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
                }
            }
            else {
                ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
            }
            ParameterUtil.reset(capturingModeParams.mVideoCodec);
        }
        ParameterUtil.reset(capturingModeParams.mVideoHdr);
    }
    
    private void apply60Fps(final CameraInfo.CameraId cameraId, final VideoSize videoSize, final CapturingModeParams capturingModeParams) {
        if (VideoStabilizer.isSteadyShotSupported(cameraId, videoSize)) {
            if (capturingModeParams.mVideoStabilizer.get() == VideoStabilizer.INTELLIGENT_ACTIVE) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mVideoStabilizer, VideoStabilizer.STEADY_SHOT);
            }
            else {
                ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
            }
        }
        else {
            ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
        }
        ParameterUtil.unavailable(capturingModeParams.mVideoHdr, VideoHdr.HDR_OFF);
        ParameterUtil.reset(capturingModeParams.mVideoCodec);
        ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
        ParameterUtil.reset(capturingModeParams.mObjectTracking);
    }
    
    private void applyCollections(final CameraInfo.CameraId cameraId, final VideoSize videoSize, final CapturingModeParams capturingModeParams) {
        final boolean steadyShotSupported = VideoStabilizer.isSteadyShotSupported(cameraId, videoSize);
        final boolean intelligentActiveAvailable = DependencyCheckUtil.isIntelligentActiveAvailable(cameraId, videoSize, capturingModeParams.mVideoHdr.get());
        if (!steadyShotSupported && !intelligentActiveAvailable) {
            ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
        }
        else {
            ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
        }
        ParameterUtil.unavailable(capturingModeParams.mVideoHdr, VideoHdr.HDR_OFF);
        ParameterUtil.reset(capturingModeParams.mVideoCodec);
        ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
        ParameterUtil.reset(capturingModeParams.mObjectTracking);
    }
    
    private void applyOther(final CameraInfo.CameraId cameraId, final VideoSize videoSize, final CapturingModeParams capturingModeParams) {
        final boolean steadyShotSupported = VideoStabilizer.isSteadyShotSupported(cameraId, videoSize);
        final boolean intelligentActiveAvailable = DependencyCheckUtil.isIntelligentActiveAvailable(cameraId, videoSize, capturingModeParams.mVideoHdr.get());
        if (!steadyShotSupported && !intelligentActiveAvailable) {
            ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
        }
        else {
            ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
        }
        ParameterUtil.unavailable(capturingModeParams.mVideoHdr, VideoHdr.HDR_OFF);
        ParameterUtil.reset(capturingModeParams.mVideoCodec);
        ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
        ParameterUtil.reset(capturingModeParams.mObjectTracking);
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        final CameraInfo.CameraId mCameraId = capturingModeParams.getActionMode().mCameraId;
        final VideoSize videoSize = capturingModeParams.mVideoSize.get();
        switch (VideoSizeApplier$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$VideoSize[this.mValue.ordinal()]) {
            default: {
                this.applyOther(mCameraId, videoSize, capturingModeParams);
                break;
            }
            case 6: {
                this.apply30Fps(mCameraId, videoSize, capturingModeParams);
                break;
            }
            case 4:
            case 5: {
                this.apply4k(mCameraId, videoSize, capturingModeParams);
                break;
            }
            case 3: {
                this.apply60Fps(mCameraId, videoSize, capturingModeParams);
                break;
            }
            case 1:
            case 2: {
                this.applyCollections(mCameraId, videoSize, capturingModeParams);
                break;
            }
        }
        if (capturingModeParams.mFusionMode.get().getKey().getSelectability() != UserSettingSelectability.FIXED) {
            if (PlatformCapability.isFusionSupportedWith(mCameraId, videoSize) && capturingModeParams.mVideoHdr.get() != VideoHdr.HDR_ON) {
                ParameterUtil.reset(capturingModeParams.mFusionMode);
            }
            else {
                ParameterUtil.unavailable(capturingModeParams.mFusionMode, capturingModeParams.mFusionMode.get());
            }
            final DependencyApplier create = DependencyApplier.create(capturingModeParams.mFusionMode.get());
            if (create != null) {
                create.apply(capturingModeParams);
            }
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        if ((this.mValue == VideoSize.FOUR_K_UHD_H264 || this.mValue == VideoSize.FOUR_K_UHD_H265) && capturingModeParams.mVideoHdr.get() != VideoHdr.HDR_ON) {
            ParameterUtil.reset(capturingModeParams.mObjectTracking);
            ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
        }
    }
}
