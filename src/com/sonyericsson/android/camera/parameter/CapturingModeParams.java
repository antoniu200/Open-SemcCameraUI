package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.device.CameraInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CapturingModeParams {
    private ActionMode mActionMode;
    public final UserSettingValueHolder<AspectRatio> mAspectRatio;
    public final UserSettingValueHolder<CapturingMode> mCapturingMode;
    private Configurations mConfig;
    public final UserSettingValueHolder<Ev> mEv;
    public final UserSettingValueHolder<Facing> mFacing;
    public final UserSettingValueHolder<FocusMode> mFocusMode;
    public final UserSettingValueHolder<FocusRange> mFocusRange;
    public final UserSettingValueHolder<FusionMode> mFusionMode;
    public final UserSettingValueHolder<Hdr> mHdr;
    public final UserSettingValueHolder<Iso> mIso;
    public final UserSettingValueHolder<Metering> mMetering;
    public final UserSettingValueHolder<ObjectTracking> mObjectTracking;
    public final UserSettingValueHolder<PredictiveCapture> mPredictiveCapture;
    public final UserSettingValueHolder<Resolution> mResolution;
    public final UserSettingValueHolder<SelfTimer> mSelfTimer;
    public final UserSettingValueHolder<ShutterSpeed> mShutterSpeed;
    public final UserSettingValueHolder<ShutterTrigger> mShutterTrigger;
    public final UserSettingValueHolder<SlowMotion> mSlowMotion;
    public final UserSettingValueHolder<SoftSkin> mSoftSkin;
    public final UserSettingValueHolder<TouchIntention> mTouchIntention;
    public final UserSettingValueHolder<VideoCodec> mVideoCodec;
    public final UserSettingValueHolder<VideoHdr> mVideoHdr;
    public final UserSettingValueHolder<VideoShutterTrigger> mVideoShutterTrigger;
    public final UserSettingValueHolder<VideoSize> mVideoSize;
    public final UserSettingValueHolder<VideoStabilizer> mVideoStabilizer;
    public final UserSettingValueHolder<WhiteBalance> mWhiteBalance;

    public CapturingModeParams(Context context, CapturingMode capturingMode, boolean z) {
        this.mCapturingMode = new UserSettingValueHolder<>(capturingMode);
        if (capturingMode.getCameraId() == CameraInfo.CameraId.FRONT) {
            this.mFacing = new UserSettingValueHolder<>(Facing.FRONT);
        } else {
            this.mFacing = new UserSettingValueHolder<>(Facing.BACK);
        }
        this.mEv = new UserSettingValueHolder<>(Ev.ZERO);
        this.mWhiteBalance = new UserSettingValueHolder<>(WhiteBalance.AUTO);
        this.mFusionMode = new UserSettingValueHolder<>(FusionMode.getDefaultValue(capturingMode));
        this.mResolution = new UserSettingValueHolder<>(Resolution.getDefaultValue(capturingMode));
        this.mAspectRatio = new UserSettingValueHolder<>(AspectRatio.getDefaultValue(capturingMode));
        this.mSelfTimer = new UserSettingValueHolder<>(SelfTimer.getDefaultValue(capturingMode));
        this.mShutterTrigger = new UserSettingValueHolder<>(ShutterTrigger.getDefaultValue(capturingMode));
        this.mFocusMode = new UserSettingValueHolder<>(FocusMode.getDefaultValue(capturingMode));
        this.mHdr = new UserSettingValueHolder<>(Hdr.HDR_OFF);
        this.mIso = new UserSettingValueHolder<>(Iso.ISO_AUTO);
        this.mMetering = new UserSettingValueHolder<>(Metering.getDefaultValue(capturingMode));
        this.mSoftSkin = new UserSettingValueHolder<>(SoftSkin.getDefaultValue(context, capturingMode));
        this.mPredictiveCapture = new UserSettingValueHolder<>(PredictiveCapture.getDefaultValue(z, capturingMode));
        this.mObjectTracking = new UserSettingValueHolder<>(ObjectTracking.getDefault(capturingMode));
        this.mShutterSpeed = new UserSettingValueHolder<>(ShutterSpeed.AUTO);
        this.mFocusRange = new UserSettingValueHolder<>(FocusRange.AF);
        this.mTouchIntention = new UserSettingValueHolder<>(TouchIntention.getDefaultValue(capturingMode));
        if (SlowMotion.getDefaultValue(capturingMode) == SlowMotion.SUPER_SLOW_MOTION || SlowMotion.getDefaultValue(capturingMode) == SlowMotion.SUPER_SLOW_SHOT) {
            this.mVideoSize = new UserSettingValueHolder<>(VideoSize.HD);
        } else {
            this.mVideoSize = new UserSettingValueHolder<>(VideoSize.FULL_HD);
        }
        this.mVideoHdr = new UserSettingValueHolder<>(VideoHdr.getDefault());
        this.mVideoShutterTrigger = new UserSettingValueHolder<>(VideoShutterTrigger.OFF);
        this.mVideoStabilizer = new UserSettingValueHolder<>(VideoStabilizer.getRecommendedVideoStabilizerValue(context, capturingMode, (VideoSize) this.mVideoSize.get()));
        this.mVideoCodec = new UserSettingValueHolder<>(VideoCodec.H264);
        this.mSlowMotion = new UserSettingValueHolder<>(SlowMotion.getDefaultValue(capturingMode));
    }

    public void init(boolean z, Configurations configurations) {
        CapturingMode capturingMode = (CapturingMode) this.mCapturingMode.get();
        this.mActionMode = new ActionMode(z, capturingMode.getType(), capturingMode.getCameraId());
        this.mConfig = configurations;
        this.mCapturingMode.setOptions(new CapturingMode[]{capturingMode});
        this.mFacing.setOptions(Facing.getOptions());
        this.mEv.setOptions(Ev.getOptions(capturingMode));
        this.mWhiteBalance.setOptions(WhiteBalance.getOptions(capturingMode));
        this.mFusionMode.setOptions(FusionMode.getOptions(capturingMode));
        this.mResolution.setOptions(Resolution.getOptions(capturingMode));
        this.mAspectRatio.setOptions(AspectRatio.getOptions(capturingMode));
        this.mSelfTimer.setOptions(SelfTimer.getOptions());
        this.mShutterTrigger.setOptions(ShutterTrigger.getOptions(capturingMode));
        this.mFocusMode.setOptions(FocusMode.getOptions(capturingMode));
        this.mHdr.setOptions(Hdr.getOptions(capturingMode));
        this.mIso.setOptions(Iso.getOptions(capturingMode, (Resolution) this.mResolution.get(), (FusionMode) this.mFusionMode.get()));
        this.mMetering.setOptions(Metering.getOptions(capturingMode));
        this.mSoftSkin.setOptions(SoftSkin.getOptions(capturingMode));
        this.mPredictiveCapture.setOptions(PredictiveCapture.getOptions(z, capturingMode));
        this.mObjectTracking.setOptions(ObjectTracking.getOptions(capturingMode));
        this.mTouchIntention.setOptions(TouchIntention.getOptions(capturingMode));
        this.mShutterSpeed.setOptions(ShutterSpeed.getOptions(capturingMode));
        this.mFocusRange.setOptions(FocusRange.getOptions(capturingMode.getCameraId()));
        this.mVideoSize.setOptions(VideoSize.getOptions(this.mActionMode, configurations));
        this.mVideoHdr.setOptions(VideoHdr.getOptions(capturingMode));
        this.mVideoShutterTrigger.setOptions(VideoShutterTrigger.getOptions(capturingMode, z));
        this.mVideoStabilizer.setOptions(VideoStabilizer.getOptions(capturingMode));
        this.mVideoCodec.setOptions(VideoCodec.getOptions(capturingMode));
        this.mSlowMotion.setOptions(SlowMotion.getOptions(capturingMode));
        Iterator<UserSettingValueHolder<?>> it = values().iterator();
        while (it.hasNext()) {
            ParameterUtil.updateDefaultValue(it.next());
        }
    }

    public List<UserSettingValueHolder<?>> values() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mCapturingMode);
        arrayList.add(this.mFacing);
        arrayList.add(this.mEv);
        arrayList.add(this.mWhiteBalance);
        arrayList.add(this.mFusionMode);
        arrayList.add(this.mResolution);
        arrayList.add(this.mAspectRatio);
        arrayList.add(this.mSelfTimer);
        arrayList.add(this.mShutterTrigger);
        arrayList.add(this.mFocusMode);
        arrayList.add(this.mHdr);
        arrayList.add(this.mIso);
        arrayList.add(this.mMetering);
        arrayList.add(this.mSoftSkin);
        arrayList.add(this.mPredictiveCapture);
        arrayList.add(this.mObjectTracking);
        arrayList.add(this.mTouchIntention);
        arrayList.add(this.mShutterSpeed);
        arrayList.add(this.mFocusRange);
        arrayList.add(this.mVideoSize);
        arrayList.add(this.mVideoHdr);
        arrayList.add(this.mVideoShutterTrigger);
        arrayList.add(this.mVideoStabilizer);
        arrayList.add(this.mVideoCodec);
        arrayList.add(this.mSlowMotion);
        return arrayList;
    }

    public ActionMode getActionMode() {
        return this.mActionMode;
    }

    public Configurations getConfig() {
        return this.mConfig;
    }
}
