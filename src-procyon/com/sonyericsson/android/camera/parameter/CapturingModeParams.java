// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.device.CameraInfo;
import android.content.Context;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.ActionMode;

public class CapturingModeParams
{
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
    
    public CapturingModeParams(final Context context, final CapturingMode capturingMode, final boolean b) {
        this.mCapturingMode = new UserSettingValueHolder<CapturingMode>(capturingMode);
        if (capturingMode.getCameraId() == CameraInfo.CameraId.FRONT) {
            this.mFacing = new UserSettingValueHolder<Facing>(Facing.FRONT);
        }
        else {
            this.mFacing = new UserSettingValueHolder<Facing>(Facing.BACK);
        }
        this.mEv = new UserSettingValueHolder<Ev>(Ev.ZERO);
        this.mWhiteBalance = new UserSettingValueHolder<WhiteBalance>(WhiteBalance.AUTO);
        this.mFusionMode = new UserSettingValueHolder<FusionMode>(FusionMode.getDefaultValue(capturingMode));
        this.mResolution = new UserSettingValueHolder<Resolution>(Resolution.getDefaultValue(capturingMode));
        this.mAspectRatio = new UserSettingValueHolder<AspectRatio>(AspectRatio.getDefaultValue(capturingMode));
        this.mSelfTimer = new UserSettingValueHolder<SelfTimer>(SelfTimer.getDefaultValue(capturingMode));
        this.mShutterTrigger = new UserSettingValueHolder<ShutterTrigger>(ShutterTrigger.getDefaultValue(capturingMode));
        this.mFocusMode = new UserSettingValueHolder<FocusMode>(FocusMode.getDefaultValue(capturingMode));
        this.mHdr = new UserSettingValueHolder<Hdr>(Hdr.HDR_OFF);
        this.mIso = new UserSettingValueHolder<Iso>(Iso.ISO_AUTO);
        this.mMetering = new UserSettingValueHolder<Metering>(Metering.getDefaultValue(capturingMode));
        this.mSoftSkin = new UserSettingValueHolder<SoftSkin>(SoftSkin.getDefaultValue(context, capturingMode));
        this.mPredictiveCapture = new UserSettingValueHolder<PredictiveCapture>(PredictiveCapture.getDefaultValue(b, capturingMode));
        this.mObjectTracking = new UserSettingValueHolder<ObjectTracking>(ObjectTracking.getDefault(capturingMode));
        this.mShutterSpeed = new UserSettingValueHolder<ShutterSpeed>(ShutterSpeed.AUTO);
        this.mFocusRange = new UserSettingValueHolder<FocusRange>(FocusRange.AF);
        this.mTouchIntention = new UserSettingValueHolder<TouchIntention>(TouchIntention.getDefaultValue(capturingMode));
        if (SlowMotion.getDefaultValue(capturingMode) != SlowMotion.SUPER_SLOW_MOTION && SlowMotion.getDefaultValue(capturingMode) != SlowMotion.SUPER_SLOW_SHOT) {
            this.mVideoSize = new UserSettingValueHolder<VideoSize>(VideoSize.FULL_HD);
        }
        else {
            this.mVideoSize = new UserSettingValueHolder<VideoSize>(VideoSize.HD);
        }
        this.mVideoHdr = new UserSettingValueHolder<VideoHdr>(VideoHdr.getDefault());
        this.mVideoShutterTrigger = new UserSettingValueHolder<VideoShutterTrigger>(VideoShutterTrigger.OFF);
        this.mVideoStabilizer = new UserSettingValueHolder<VideoStabilizer>(VideoStabilizer.getRecommendedVideoStabilizerValue(context, capturingMode, this.mVideoSize.get()));
        this.mVideoCodec = new UserSettingValueHolder<VideoCodec>(VideoCodec.H264);
        this.mSlowMotion = new UserSettingValueHolder<SlowMotion>(SlowMotion.getDefaultValue(capturingMode));
    }
    
    public ActionMode getActionMode() {
        return this.mActionMode;
    }
    
    public Configurations getConfig() {
        return this.mConfig;
    }
    
    public void init(final boolean b, final Configurations mConfig) {
        final CapturingMode capturingMode = this.mCapturingMode.get();
        this.mActionMode = new ActionMode(b, capturingMode.getType(), capturingMode.getCameraId());
        this.mConfig = mConfig;
        this.mCapturingMode.setOptions(new CapturingMode[] { capturingMode });
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
        this.mIso.setOptions(Iso.getOptions(capturingMode, this.mResolution.get(), this.mFusionMode.get()));
        this.mMetering.setOptions(Metering.getOptions(capturingMode));
        this.mSoftSkin.setOptions(SoftSkin.getOptions(capturingMode));
        this.mPredictiveCapture.setOptions(PredictiveCapture.getOptions(b, capturingMode));
        this.mObjectTracking.setOptions(ObjectTracking.getOptions(capturingMode));
        this.mTouchIntention.setOptions(TouchIntention.getOptions(capturingMode));
        this.mShutterSpeed.setOptions(ShutterSpeed.getOptions(capturingMode));
        this.mFocusRange.setOptions(FocusRange.getOptions(capturingMode.getCameraId()));
        this.mVideoSize.setOptions(VideoSize.getOptions(this.mActionMode, mConfig));
        this.mVideoHdr.setOptions(VideoHdr.getOptions(capturingMode));
        this.mVideoShutterTrigger.setOptions(VideoShutterTrigger.getOptions(capturingMode, b));
        this.mVideoStabilizer.setOptions(VideoStabilizer.getOptions(capturingMode));
        this.mVideoCodec.setOptions(VideoCodec.getOptions(capturingMode));
        this.mSlowMotion.setOptions(SlowMotion.getOptions(capturingMode));
        final Iterator<UserSettingValueHolder<?>> iterator = this.values().iterator();
        while (iterator.hasNext()) {
            ParameterUtil.updateDefaultValue(iterator.next());
        }
    }
    
    public List<UserSettingValueHolder<?>> values() {
        final ArrayList list = new ArrayList();
        list.add(this.mCapturingMode);
        list.add(this.mFacing);
        list.add(this.mEv);
        list.add(this.mWhiteBalance);
        list.add(this.mFusionMode);
        list.add(this.mResolution);
        list.add(this.mAspectRatio);
        list.add(this.mSelfTimer);
        list.add(this.mShutterTrigger);
        list.add(this.mFocusMode);
        list.add(this.mHdr);
        list.add(this.mIso);
        list.add(this.mMetering);
        list.add(this.mSoftSkin);
        list.add(this.mPredictiveCapture);
        list.add(this.mObjectTracking);
        list.add(this.mTouchIntention);
        list.add(this.mShutterSpeed);
        list.add(this.mFocusRange);
        list.add(this.mVideoSize);
        list.add(this.mVideoHdr);
        list.add(this.mVideoShutterTrigger);
        list.add(this.mVideoStabilizer);
        list.add(this.mVideoCodec);
        list.add(this.mSlowMotion);
        return list;
    }
}
