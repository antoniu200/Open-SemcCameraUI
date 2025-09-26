// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.ResetSettings;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.configuration.parameters.HelpGuide;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.CameraKey;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import java.util.Collection;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VideoSmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import java.util.Collections;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import java.util.ArrayList;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.configuration.Configurations;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.HashMap;
import java.util.List;
import java.util.EnumMap;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Map;
import android.content.Context;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable;

public abstract class Parameters implements UserSettingApplicable
{
    public static final String TAG = "Parameters";
    public final CapturingMode capturingMode;
    protected CapturingModeParams mCapturingModeParams;
    protected final Context mContext;
    protected final Map<UserSettingKey, UserSettingValueHolder<?>> mHolders;
    protected final ModeIndependentParams mIndependentParams;
    public final boolean mIsOneShot;
    
    public Parameters(final CapturingMode capturingMode, final boolean mIsOneShot, final Context mContext, final ModeIndependentParams mIndependentParams) {
        this.capturingMode = capturingMode;
        this.mIsOneShot = mIsOneShot;
        this.mContext = mContext;
        this.mIndependentParams = mIndependentParams;
        this.mHolders = new EnumMap<UserSettingKey, UserSettingValueHolder<?>>(UserSettingKey.class);
    }
    
    public static Parameters create(final Context context, final CapturingMode capturingMode, final boolean b, final ModeIndependentParams modeIndependentParams) {
        switch (Parameters$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                return new NormalParameters(context, capturingMode, b, modeIndependentParams);
            }
            case 6: {
                return new FrontVideoParameters(context, capturingMode, b, modeIndependentParams);
            }
            case 5: {
                return new SlowMotionParameters(context, capturingMode, b, modeIndependentParams);
            }
            case 4: {
                return new VideoParameters(context, capturingMode, b, modeIndependentParams);
            }
            case 3: {
                return new FrontPhotoParameters(context, capturingMode, b, modeIndependentParams);
            }
            case 2: {
                return new SuperiorFrontParameters(context, capturingMode, b, modeIndependentParams);
            }
            case 1: {
                return new SuperiorParameters(context, capturingMode, b, modeIndependentParams);
            }
        }
    }
    
    private static Map<String, String> createStringMap(final List<UserSettingValueHolder<?>> list) {
        final HashMap hashMap = new HashMap();
        for (final UserSettingValueHolder userSettingValueHolder : list) {
            final UserSettingKey key = userSettingValueHolder.get().getKey();
            final String valueString = userSettingValueHolder.createValueString();
            hashMap.put(key.toString(), valueString);
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("writeSharedPrefs: key: ");
                sb.append(key);
                sb.append(", value: ");
                sb.append(valueString);
                CamLog.d(sb.toString());
            }
        }
        return hashMap;
    }
    
    private static void parseStringMap(final List<UserSettingValueHolder<?>> list, final Map<String, String> map) {
        for (final UserSettingValueHolder userSettingValueHolder : list) {
            final UserSettingKey key = userSettingValueHolder.get().getKey();
            final String s = map.get(key.toString());
            if (s != null) {
                userSettingValueHolder.parseValueString(s);
                if (!CamLog.VERBOSE) {
                    continue;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("readSharedPrefs: key: ");
                sb.append(key);
                sb.append(", value: ");
                sb.append(userSettingValueHolder.get());
                CamLog.d(sb.toString());
            }
        }
    }
    
    public void clearHolder() {
        this.mHolders.clear();
    }
    
    @Override
    public void commit() {
        for (final UserSettingKey userSettingKey : this.mHolders.keySet()) {
            if (this.mHolders.get(userSettingKey).hasChanged()) {
                this.mHolders.get(userSettingKey).onApplied();
            }
        }
    }
    
    public Parameters copy(final Context context, final CapturingMode capturingMode, final Configurations configurations, final Storage storage, final boolean b, final ModeIndependentParams modeIndependentParams, final boolean b2) {
        final Parameters create = create(context, capturingMode, b, modeIndependentParams);
        create.prepareHolder(configurations, null, storage);
        ParameterUtil.copy(this.mHolders, create.mHolders);
        if (b2) {
            create.mIndependentParams.setValues(this.mIndependentParams);
        }
        return create;
    }
    
    UserSettingValue get(final UserSettingKey userSettingKey) {
        switch (Parameters$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()]) {
            default: {
                return null;
            }
            case 43: {
                return this.mIndependentParams.mPredictiveLaunch.get();
            }
            case 42: {
                return this.mIndependentParams.mDistortionCorrection.get();
            }
            case 41: {
                return this.getFusionMode();
            }
            case 40: {
                return this.mIndependentParams.mFrontAngle.get();
            }
            case 39: {
                return this.getSlowMotion();
            }
            case 38: {
                return this.mIndependentParams.mSideSense.get();
            }
            case 37: {
                return this.mIndependentParams.mGridLine.get();
            }
            case 36: {
                return this.mIndependentParams.mVolumeKey.get();
            }
            case 35: {
                return this.mIndependentParams.mDestinationToSave.get();
            }
            case 34: {
                return this.mIndependentParams.mShutterSound.get();
            }
            case 33: {
                return this.mIndependentParams.mTouchCapture.get();
            }
            case 32: {
                return this.mIndependentParams.mFastCapture.get();
            }
            case 31: {
                return this.mIndependentParams.mGeoTag.get();
            }
            case 30: {
                return this.mIndependentParams.mAutoReview.get();
            }
            case 29: {
                return this.getTouchIntention();
            }
            case 28: {
                return this.getFocusRange();
            }
            case 27: {
                return this.getShutterSpeed();
            }
            case 26: {
                return this.getVideoCodec();
            }
            case 25: {
                return this.getVideoSmileCapture();
            }
            case 24: {
                return this.getWhiteBalance();
            }
            case 23: {
                return this.getVideoHdr();
            }
            case 22: {
                return this.getVideoSize();
            }
            case 21: {
                return this.getVideoStabilizer();
            }
            case 20: {
                return this.getVideoShutterTrigger();
            }
            case 19: {
                return this.getSoftSkin();
            }
            case 18: {
                return this.getShutterTrigger();
            }
            case 17: {
                return this.getSmileCapture();
            }
            case 16: {
                return this.getSelfTimer();
            }
            case 15: {
                return this.getAspectRatio();
            }
            case 14: {
                return this.getResolution();
            }
            case 13: {
                return this.getPhotoLight();
            }
            case 12: {
                return this.getMetering();
            }
            case 11: {
                return this.getIso();
            }
            case 10: {
                return this.getHdr();
            }
            case 9: {
                return this.getObjectTracking();
            }
            case 8: {
                return this.getFocusMode();
            }
            case 7: {
                return this.getDisplayFlash();
            }
            case 6: {
                return this.getFlash();
            }
            case 5: {
                return this.getFacing();
            }
            case 4: {
                return this.getEv();
            }
            case 3: {
                return this.getPredictiveCapture();
            }
            case 2: {
                return this.mIndependentParams.mBurstByCameraKey.get();
            }
            case 1: {
                return this.capturingMode;
            }
        }
    }
    
    public AspectRatio getAspectRatio() {
        return this.mCapturingModeParams.mAspectRatio.get();
    }
    
    public List<UserSettingValue> getChangedValues() {
        final ArrayList list = new ArrayList();
        for (final UserSettingKey userSettingKey : this.mHolders.keySet()) {
            if (this.mHolders.get(userSettingKey).hasChanged()) {
                list.add(((UserSettingValueHolder<Object>)this.mHolders.get(userSettingKey)).get());
            }
        }
        return list;
    }
    
    public DisplayFlash getDisplayFlash() {
        return this.mIndependentParams.mDisplayFlash.get();
    }
    
    public Ev getEv() {
        return this.mCapturingModeParams.mEv.get();
    }
    
    public Facing getFacing() {
        return this.mCapturingModeParams.mFacing.get();
    }
    
    public Flash getFlash() {
        return this.mIndependentParams.mFlash.get();
    }
    
    public FocusMode getFocusMode() {
        return this.mCapturingModeParams.mFocusMode.get();
    }
    
    public FocusRange getFocusRange() {
        return this.mCapturingModeParams.mFocusRange.get();
    }
    
    public FusionMode getFusionMode() {
        return this.mCapturingModeParams.mFusionMode.get();
    }
    
    public Hdr getHdr() {
        return this.mCapturingModeParams.mHdr.get();
    }
    
    public Map<UserSettingKey, UserSettingValueHolder<?>> getHolder() {
        return Collections.unmodifiableMap((Map<? extends UserSettingKey, ? extends UserSettingValueHolder<?>>)this.mHolders);
    }
    
    public Iso getIso() {
        return this.mCapturingModeParams.mIso.get();
    }
    
    public Metering getMetering() {
        return this.mCapturingModeParams.mMetering.get();
    }
    
    public ObjectTracking getObjectTracking() {
        return this.mCapturingModeParams.mObjectTracking.get();
    }
    
    public UserSettingValue[] getOptions(final UserSettingKey userSettingKey) {
        if (this.mHolders.containsKey(userSettingKey)) {
            return this.mHolders.get(userSettingKey).getOptions();
        }
        return new UserSettingValue[0];
    }
    
    public PhotoLight getPhotoLight() {
        return this.mIndependentParams.mPhotoLight.get();
    }
    
    public PredictiveCapture getPredictiveCapture() {
        return this.mCapturingModeParams.mPredictiveCapture.get();
    }
    
    public Resolution getResolution() {
        return this.mCapturingModeParams.mResolution.get();
    }
    
    public SelfTimer getSelfTimer() {
        return this.mCapturingModeParams.mSelfTimer.get();
    }
    
    public ShutterSpeed getShutterSpeed() {
        return this.mCapturingModeParams.mShutterSpeed.get();
    }
    
    public ShutterTrigger getShutterTrigger() {
        return this.mCapturingModeParams.mShutterTrigger.get();
    }
    
    public SlowMotion getSlowMotion() {
        return this.mCapturingModeParams.mSlowMotion.get();
    }
    
    public SmileCapture getSmileCapture() {
        return this.mCapturingModeParams.mShutterTrigger.get().getSmileCapture();
    }
    
    public SoftSkin getSoftSkin() {
        return this.mCapturingModeParams.mSoftSkin.get();
    }
    
    public abstract EnumMap<UserSettingKey, UserSettingValue> getTargetParameters();
    
    public TouchIntention getTouchIntention() {
        return this.mCapturingModeParams.mTouchIntention.get();
    }
    
    public VideoCodec getVideoCodec() {
        return this.mCapturingModeParams.mVideoCodec.get();
    }
    
    public VideoHdr getVideoHdr() {
        return this.mCapturingModeParams.mVideoHdr.get();
    }
    
    public VideoShutterTrigger getVideoShutterTrigger() {
        return this.mCapturingModeParams.mVideoShutterTrigger.get();
    }
    
    public VideoSize getVideoSize() {
        return this.mCapturingModeParams.mVideoSize.get();
    }
    
    public VideoSmileCapture getVideoSmileCapture() {
        return this.mCapturingModeParams.mVideoShutterTrigger.get().getVideoSmileCapture();
    }
    
    public VideoStabilizer getVideoStabilizer() {
        return this.mCapturingModeParams.mVideoStabilizer.get();
    }
    
    public WhiteBalance getWhiteBalance() {
        return this.mCapturingModeParams.mWhiteBalance.get();
    }
    
    protected abstract void prepare();
    
    public void prepareHolder(final Configurations configurations, final SharedPreferencesAccessor sharedPreferencesAccessor, final Storage storage) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("init: mode: ");
            sb.append(this.capturingMode);
            CamLog.d(sb.toString());
        }
        (this.mCapturingModeParams = new CapturingModeParams(this.mContext, this.capturingMode, this.mIsOneShot)).init(this.mIsOneShot, configurations);
        this.mIndependentParams.init(new ActionMode(this.mIsOneShot, this.capturingMode.getType(), this.capturingMode.getCameraId()), storage);
        final List<UserSettingValueHolder<?>> values = this.mCapturingModeParams.values();
        values.addAll(this.mIndependentParams.values());
        this.putHolders(values);
        this.prepare();
        if (sharedPreferencesAccessor != null) {
            sharedPreferencesAccessor.registerKey(SharedPreferencesAccessor.createPrefix(ParameterCategory.CAPTURING_MODE, this.capturingMode, ""));
        }
    }
    
    protected void putHolders(final List<UserSettingValueHolder<?>> list) {
        final Iterator<UserSettingValueHolder<?>> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.updateHolder(iterator.next());
        }
    }
    
    public void readSharedPrefs(final SharedPreferencesAccessor sharedPreferencesAccessor) {
        final Map<String, String> stringMap = sharedPreferencesAccessor.getStringMap(SharedPreferencesAccessor.createPrefix(ParameterCategory.CAPTURING_MODE, this.capturingMode, ""));
        final List<UserSettingValueHolder<?>> values = this.mCapturingModeParams.values();
        values.addAll(this.mIndependentParams.values());
        parseStringMap(values, stringMap);
    }
    
    public void resetTempParameters() {
        final Iterator<UserSettingValueHolder<?>> iterator = this.mCapturingModeParams.values().iterator();
        while (iterator.hasNext()) {
            final UserSettingKey key = iterator.next().get().getKey();
            if (!key.isSaved() && this.mHolders.keySet().contains(key)) {
                this.mHolders.get(key).reset();
                ((UserSettingValueHolder<UserSettingValue>)this.mHolders.get(key)).setDefaultValue().apply(this);
            }
        }
        final Iterator<UserSettingValueHolder<?>> iterator2 = this.mIndependentParams.values().iterator();
        while (iterator2.hasNext()) {
            final UserSettingKey key2 = iterator2.next().get().getKey();
            if (!key2.isSaved() && this.mHolders.keySet().contains(key2)) {
                this.mHolders.get(key2).reset();
                ((UserSettingValueHolder<UserSettingValue>)this.mHolders.get(key2)).setDefaultValue().apply(this);
            }
        }
        this.commit();
    }
    
    @Override
    public void set(final AspectRatio aspectRatio) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mAspectRatio.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mAspectRatio.set(aspectRatio);
        final DependencyApplier create2 = DependencyApplier.create(aspectRatio);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final AutoReview autoReview) {
        this.mIndependentParams.mAutoReview.set(autoReview);
    }
    
    @Override
    public void set(final CameraKey cameraKey) {
        this.mIndependentParams.mBurstByCameraKey.set(cameraKey);
    }
    
    @Override
    public void set(final CapturingMode capturingMode) {
    }
    
    @Override
    public void set(final DestinationToSave destinationToSave) {
        this.mIndependentParams.mDestinationToSave.set(destinationToSave);
    }
    
    @Override
    public void set(final DisplayFlash displayFlash) {
        ParameterUtil.reset(this.mIndependentParams.mDisplayFlash);
        this.mIndependentParams.mDisplayFlash.set(displayFlash);
    }
    
    @Override
    public void set(final DistortionCorrection distortionCorrection) {
        this.mIndependentParams.mDistortionCorrection.set(distortionCorrection);
    }
    
    @Override
    public void set(final Ev ev) {
        this.mCapturingModeParams.mEv.set(ev);
    }
    
    @Override
    public void set(final Facing facing) {
    }
    
    @Override
    public void set(final FastCapture fastCapture) {
        this.mIndependentParams.mFastCapture.set(fastCapture);
    }
    
    @Override
    public void set(final Flash flash) {
        ParameterUtil.reset(this.mIndependentParams.mFlash);
        this.mIndependentParams.mFlash.set(flash);
    }
    
    @Override
    public void set(final FocusMode focusMode) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mFocusMode.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mFocusMode.set(focusMode);
        final DependencyApplier create2 = DependencyApplier.create(focusMode);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final FocusRange focusRange) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mFocusRange.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mFocusRange.set(focusRange);
        if (FocusRange.MF == focusRange) {
            this.mCapturingModeParams.mFocusRange.canChanged();
        }
        final DependencyApplier create2 = DependencyApplier.create(focusRange);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final FrontAngle frontAngle) {
        this.mIndependentParams.mFrontAngle.set(frontAngle);
    }
    
    @Override
    public void set(final FusionMode fusionMode) {
        ParameterUtil.reset(this.mCapturingModeParams.mFusionMode, fusionMode);
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mFusionMode.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mFusionMode.set(fusionMode);
        final DependencyApplier create2 = DependencyApplier.create(fusionMode);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final Geotag geotag) {
        this.mIndependentParams.mGeoTag.set(geotag);
    }
    
    @Override
    public void set(final GridLine gridLine) {
        this.mIndependentParams.mGridLine.set(gridLine);
    }
    
    @Override
    public void set(final Hdr hdr) {
        ParameterUtil.reset(this.mCapturingModeParams.mHdr, hdr);
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mHdr.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mHdr.set(hdr);
        final DependencyApplier create2 = DependencyApplier.create(hdr);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final HelpGuide helpGuide) {
        this.mIndependentParams.mHelpGuide.set(helpGuide);
    }
    
    @Override
    public void set(final Iso iso) {
        ParameterUtil.reset(this.mCapturingModeParams.mIso, iso);
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mIso.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mIso.set(iso);
        final DependencyApplier create2 = DependencyApplier.create(iso);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final Metering metering) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mMetering.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mMetering.set(metering);
        final DependencyApplier create2 = DependencyApplier.create(metering);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final ObjectTracking objectTracking) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mObjectTracking.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mObjectTracking.set(objectTracking);
        final DependencyApplier create2 = DependencyApplier.create(objectTracking);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final PhotoLight photoLight) {
        this.mIndependentParams.mPhotoLight.set(photoLight);
    }
    
    @Override
    public void set(final PredictiveCapture predictiveCapture) {
        this.mCapturingModeParams.mPredictiveCapture.set(predictiveCapture);
    }
    
    @Override
    public void set(final PredictiveLaunch predictiveLaunch) {
        this.mIndependentParams.mPredictiveLaunch.set(predictiveLaunch);
    }
    
    @Override
    public void set(final ResetSettings resetSettings) {
        this.mIndependentParams.mResetSettings.set(resetSettings);
    }
    
    @Override
    public void set(final Resolution resolution) {
        this.mCapturingModeParams.mResolution.set(resolution);
    }
    
    @Override
    public void set(final SelfTimer selfTimer) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mSelfTimer.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mSelfTimer.set(selfTimer);
        final DependencyApplier create2 = DependencyApplier.create(selfTimer);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final ShutterSound shutterSound) {
        this.mIndependentParams.mShutterSound.set(shutterSound);
    }
    
    @Override
    public void set(final ShutterSpeed shutterSpeed) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mShutterSpeed.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mShutterSpeed.set(shutterSpeed);
        final DependencyApplier create2 = DependencyApplier.create(shutterSpeed);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final ShutterTrigger shutterTrigger) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mShutterTrigger.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mShutterTrigger.set(shutterTrigger);
        final DependencyApplier create2 = DependencyApplier.create(shutterTrigger);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final SideSense sideSense) {
        this.mIndependentParams.mSideSense.set(sideSense);
    }
    
    @Override
    public void set(final SoftSkin softSkin) {
        this.mCapturingModeParams.mSoftSkin.set(softSkin);
    }
    
    @Override
    public void set(final TouchCapture touchCapture) {
        this.mIndependentParams.mTouchCapture.set(touchCapture);
    }
    
    @Override
    public void set(final TouchIntention touchIntention) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mTouchIntention.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mTouchIntention.set(touchIntention);
        final DependencyApplier create2 = DependencyApplier.create(touchIntention);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final VideoCodec videoCodec) {
        this.mCapturingModeParams.mVideoCodec.set(videoCodec);
    }
    
    @Override
    public void set(final VideoHdr videoHdr) {
        this.mCapturingModeParams.mVideoHdr.set(videoHdr);
        final DependencyApplier create = DependencyApplier.create(videoHdr);
        if (create != null) {
            create.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final VideoShutterTrigger videoShutterTrigger) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mVideoShutterTrigger.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mVideoShutterTrigger.set(videoShutterTrigger);
        final DependencyApplier create2 = DependencyApplier.create(videoShutterTrigger);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final VideoSize videoSize) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mVideoSize.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mVideoSize.set(videoSize);
        final DependencyApplier create2 = DependencyApplier.create(videoSize);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final VideoStabilizer videoStabilizer) {
        this.mCapturingModeParams.mVideoStabilizer.set(videoStabilizer);
    }
    
    @Override
    public void set(final VolumeKey volumeKey) {
        this.mIndependentParams.mVolumeKey.set(volumeKey);
    }
    
    @Override
    public void set(final WhiteBalance whiteBalance) {
        this.mCapturingModeParams.mWhiteBalance.set(whiteBalance);
    }
    
    protected abstract void updateFocusParameters();
    
    protected void updateHolder(final UserSettingValueHolder<?> userSettingValueHolder) {
        final UserSettingKey key = ((UserSettingValue)userSettingValueHolder.get()).getKey();
        if (UserSettingSelectability.getSelectability(userSettingValueHolder.getOptions().length) != UserSettingSelectability.INVALID) {
            this.mHolders.put(key, userSettingValueHolder);
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("put: Param has been put: ");
                sb.append(key);
                CamLog.d(sb.toString());
            }
        }
        else if (this.mHolders.containsKey(key)) {
            this.mHolders.remove(key);
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("put: Invalid param removed: ");
                sb2.append(key);
                CamLog.d(sb2.toString());
            }
        }
        else if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("put: Invalid param: ");
            sb3.append(key);
            CamLog.d(sb3.toString());
        }
    }
    
    protected abstract void updatePhotoLight();
    
    protected abstract void updateSelectability();
    
    protected void writeSharedPrefs(final SharedPreferencesAccessor sharedPreferencesAccessor) {
        if (!this.mIsOneShot) {
            final String prefix = SharedPreferencesAccessor.createPrefix(ParameterCategory.CAPTURING_MODE, this.capturingMode, "");
            final List<UserSettingValueHolder<?>> values = this.mCapturingModeParams.values();
            values.addAll(this.mIndependentParams.values());
            sharedPreferencesAccessor.setStringMap(prefix, createStringMap(values));
        }
    }
    
    protected void writeSharedPrefs(final SharedPreferencesAccessor sharedPreferencesAccessor, final UserSettingKey obj) {
        if (!this.mIsOneShot && obj.isSaved()) {
            final String valueString = this.mHolders.get(obj).createValueString();
            final String prefix = SharedPreferencesAccessor.createPrefix(obj.getCategory(), this.capturingMode, "");
            final StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            sb.append(obj);
            sharedPreferencesAccessor.writeString(sb.toString(), valueString, true);
        }
    }
}
