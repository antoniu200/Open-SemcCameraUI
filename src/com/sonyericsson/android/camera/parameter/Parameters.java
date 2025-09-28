package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.CameraKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.DistortionCorrection;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.GridLine;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.HelpGuide;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import com.sonyericsson.android.camera.configuration.parameters.ResetSettings;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoSmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class Parameters implements UserSettingApplicable {
    public static final String TAG = "Parameters";
    public final CapturingMode capturingMode;
    protected CapturingModeParams mCapturingModeParams;
    protected final Context mContext;
    protected final Map<UserSettingKey, UserSettingValueHolder<?>> mHolders = new EnumMap(UserSettingKey.class);
    protected final ModeIndependentParams mIndependentParams;
    public final boolean mIsOneShot;

    public abstract EnumMap<UserSettingKey, UserSettingValue> getTargetParameters();

    protected abstract void prepare();

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(CapturingMode capturingMode) {
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Facing facing) {
    }

    protected abstract void updateFocusParameters();

    protected abstract void updatePhotoLight();

    protected abstract void updateSelectability();

    public static Parameters create(Context context, CapturingMode capturingMode, boolean z, ModeIndependentParams modeIndependentParams) {
        switch (capturingMode) {
            case SCENE_RECOGNITION:
                return new SuperiorParameters(context, capturingMode, z, modeIndependentParams);
            case SUPERIOR_FRONT:
                return new SuperiorFrontParameters(context, capturingMode, z, modeIndependentParams);
            case FRONT_PHOTO:
                return new FrontPhotoParameters(context, capturingMode, z, modeIndependentParams);
            case VIDEO:
                return new VideoParameters(context, capturingMode, z, modeIndependentParams);
            case SLOW_MOTION:
                return new SlowMotionParameters(context, capturingMode, z, modeIndependentParams);
            case FRONT_VIDEO:
                return new FrontVideoParameters(context, capturingMode, z, modeIndependentParams);
            default:
                return new NormalParameters(context, capturingMode, z, modeIndependentParams);
        }
    }

    public Parameters(CapturingMode capturingMode, boolean z, Context context, ModeIndependentParams modeIndependentParams) {
        this.capturingMode = capturingMode;
        this.mIsOneShot = z;
        this.mContext = context;
        this.mIndependentParams = modeIndependentParams;
    }

    public Parameters copy(Context context, CapturingMode capturingMode, Configurations configurations, Storage storage, boolean z, ModeIndependentParams modeIndependentParams, boolean z2) {
        Parameters parametersCreate = create(context, capturingMode, z, modeIndependentParams);
        parametersCreate.prepareHolder(configurations, null, storage);
        ParameterUtil.copy(this.mHolders, parametersCreate.mHolders);
        if (z2) {
            parametersCreate.mIndependentParams.setValues(this.mIndependentParams);
        }
        return parametersCreate;
    }

    public void prepareHolder(Configurations configurations, SharedPreferencesAccessor sharedPreferencesAccessor, Storage storage) {
        if (CamLog.VERBOSE) {
            CamLog.d("init: mode: " + this.capturingMode);
        }
        this.mCapturingModeParams = new CapturingModeParams(this.mContext, this.capturingMode, this.mIsOneShot);
        this.mCapturingModeParams.init(this.mIsOneShot, configurations);
        this.mIndependentParams.init(new ActionMode(this.mIsOneShot, this.capturingMode.getType(), this.capturingMode.getCameraId()), storage);
        List<UserSettingValueHolder<?>> listValues = this.mCapturingModeParams.values();
        listValues.addAll(this.mIndependentParams.values());
        putHolders(listValues);
        prepare();
        if (sharedPreferencesAccessor != null) {
            sharedPreferencesAccessor.registerKey(SharedPreferencesAccessor.createPrefix(ParameterCategory.CAPTURING_MODE, this.capturingMode, ""));
        }
    }

    protected void putHolders(List<UserSettingValueHolder<?>> list) {
        Iterator<UserSettingValueHolder<?>> it = list.iterator();
        while (it.hasNext()) {
            updateHolder(it.next());
        }
    }

    protected void updateHolder(UserSettingValueHolder<?> userSettingValueHolder) {
        UserSettingKey key = userSettingValueHolder.get().getKey();
        if (UserSettingSelectability.getSelectability(userSettingValueHolder.getOptions().length) != UserSettingSelectability.INVALID) {
            this.mHolders.put(key, userSettingValueHolder);
            if (CamLog.VERBOSE) {
                CamLog.d("put: Param has been put: " + key);
                return;
            }
            return;
        }
        if (this.mHolders.containsKey(key)) {
            this.mHolders.remove(key);
            if (CamLog.VERBOSE) {
                CamLog.d("put: Invalid param removed: " + key);
                return;
            }
            return;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("put: Invalid param: " + key);
        }
    }

    public void clearHolder() {
        this.mHolders.clear();
    }

    public Map<UserSettingKey, UserSettingValueHolder<?>> getHolder() {
        return Collections.unmodifiableMap(this.mHolders);
    }

    public void readSharedPrefs(SharedPreferencesAccessor sharedPreferencesAccessor) {
        Map<String, String> stringMap = sharedPreferencesAccessor.getStringMap(SharedPreferencesAccessor.createPrefix(ParameterCategory.CAPTURING_MODE, this.capturingMode, ""));
        List<UserSettingValueHolder<?>> listValues = this.mCapturingModeParams.values();
        listValues.addAll(this.mIndependentParams.values());
        parseStringMap(listValues, stringMap);
    }

    protected void writeSharedPrefs(SharedPreferencesAccessor sharedPreferencesAccessor) {
        if (this.mIsOneShot) {
            return;
        }
        String strCreatePrefix = SharedPreferencesAccessor.createPrefix(ParameterCategory.CAPTURING_MODE, this.capturingMode, "");
        List<UserSettingValueHolder<?>> listValues = this.mCapturingModeParams.values();
        listValues.addAll(this.mIndependentParams.values());
        sharedPreferencesAccessor.setStringMap(strCreatePrefix, createStringMap(listValues));
    }

    private static Map<String, String> createStringMap(List<UserSettingValueHolder<?>> list) {
        HashMap map = new HashMap();
        for (UserSettingValueHolder<?> userSettingValueHolder : list) {
            UserSettingKey key = userSettingValueHolder.get().getKey();
            String strCreateValueString = userSettingValueHolder.createValueString();
            map.put(key.toString(), strCreateValueString);
            if (CamLog.VERBOSE) {
                CamLog.d("writeSharedPrefs: key: " + key + ", value: " + strCreateValueString);
            }
        }
        return map;
    }

    private static void parseStringMap(List<UserSettingValueHolder<?>> list, Map<String, String> map) {
        for (UserSettingValueHolder<?> userSettingValueHolder : list) {
            UserSettingKey key = userSettingValueHolder.get().getKey();
            String str = map.get(key.toString());
            if (str != null) {
                userSettingValueHolder.parseValueString(str);
                if (CamLog.VERBOSE) {
                    CamLog.d("readSharedPrefs: key: " + key + ", value: " + userSettingValueHolder.get());
                }
            }
        }
    }

    protected void writeSharedPrefs(SharedPreferencesAccessor sharedPreferencesAccessor, UserSettingKey userSettingKey) {
        if (this.mIsOneShot || !userSettingKey.isSaved()) {
            return;
        }
        sharedPreferencesAccessor.writeString(SharedPreferencesAccessor.createPrefix(userSettingKey.getCategory(), this.capturingMode, "") + userSettingKey, this.mHolders.get(userSettingKey).createValueString(), true);
    }

    public List<UserSettingValue> getChangedValues() {
        ArrayList arrayList = new ArrayList();
        for (UserSettingKey userSettingKey : this.mHolders.keySet()) {
            if (this.mHolders.get(userSettingKey).hasChanged()) {
                arrayList.add(this.mHolders.get(userSettingKey).get());
            }
        }
        return arrayList;
    }

    public UserSettingValue[] getOptions(UserSettingKey userSettingKey) {
        if (this.mHolders.containsKey(userSettingKey)) {
            return this.mHolders.get(userSettingKey).getOptions();
        }
        return new UserSettingValue[0];
    }

    public void resetTempParameters() {
        Iterator<UserSettingValueHolder<?>> it = this.mCapturingModeParams.values().iterator();
        while (it.hasNext()) {
            UserSettingKey key = it.next().get().getKey();
            if (!key.isSaved() && this.mHolders.keySet().contains(key)) {
                this.mHolders.get(key).reset();
                this.mHolders.get(key).setDefaultValue().apply(this);
            }
        }
        Iterator<UserSettingValueHolder<?>> it2 = this.mIndependentParams.values().iterator();
        while (it2.hasNext()) {
            UserSettingKey key2 = it2.next().get().getKey();
            if (!key2.isSaved() && this.mHolders.keySet().contains(key2)) {
                this.mHolders.get(key2).reset();
                this.mHolders.get(key2).setDefaultValue().apply(this);
            }
        }
        commit();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(PredictiveLaunch predictiveLaunch) {
        this.mIndependentParams.mPredictiveLaunch.set(predictiveLaunch);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(CameraKey cameraKey) {
        this.mIndependentParams.mBurstByCameraKey.set(cameraKey);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(FrontAngle frontAngle) {
        this.mIndependentParams.mFrontAngle.set(frontAngle);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Flash flash) {
        ParameterUtil.reset(this.mIndependentParams.mFlash);
        this.mIndependentParams.mFlash.set(flash);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(DisplayFlash displayFlash) {
        ParameterUtil.reset(this.mIndependentParams.mDisplayFlash);
        this.mIndependentParams.mDisplayFlash.set(displayFlash);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(PhotoLight photoLight) {
        this.mIndependentParams.mPhotoLight.set(photoLight);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Ev ev) {
        this.mCapturingModeParams.mEv.set(ev);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(WhiteBalance whiteBalance) {
        this.mCapturingModeParams.mWhiteBalance.set(whiteBalance);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Resolution resolution) {
        this.mCapturingModeParams.mResolution.set(resolution);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(AspectRatio aspectRatio) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mAspectRatio.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mAspectRatio.set(aspectRatio);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(aspectRatio);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(FocusMode focusMode) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mFocusMode.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mFocusMode.set(focusMode);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(focusMode);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(SelfTimer selfTimer) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mSelfTimer.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mSelfTimer.set(selfTimer);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(selfTimer);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(ShutterTrigger shutterTrigger) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mShutterTrigger.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mShutterTrigger.set(shutterTrigger);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(shutterTrigger);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Hdr hdr) {
        ParameterUtil.reset(this.mCapturingModeParams.mHdr, hdr);
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mHdr.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mHdr.set(hdr);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(hdr);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Iso iso) {
        ParameterUtil.reset(this.mCapturingModeParams.mIso, iso);
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mIso.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mIso.set(iso);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(iso);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Metering metering) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mMetering.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mMetering.set(metering);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(metering);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(SoftSkin softSkin) {
        this.mCapturingModeParams.mSoftSkin.set(softSkin);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(PredictiveCapture predictiveCapture) {
        this.mCapturingModeParams.mPredictiveCapture.set(predictiveCapture);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(ObjectTracking objectTracking) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mObjectTracking.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mObjectTracking.set(objectTracking);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(objectTracking);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoSize videoSize) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mVideoSize.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mVideoSize.set(videoSize);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(videoSize);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoHdr videoHdr) {
        this.mCapturingModeParams.mVideoHdr.set(videoHdr);
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(videoHdr);
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoShutterTrigger videoShutterTrigger) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mVideoShutterTrigger.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mVideoShutterTrigger.set(videoShutterTrigger);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(videoShutterTrigger);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoStabilizer videoStabilizer) {
        this.mCapturingModeParams.mVideoStabilizer.set(videoStabilizer);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoCodec videoCodec) {
        this.mCapturingModeParams.mVideoCodec.set(videoCodec);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(TouchIntention touchIntention) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mTouchIntention.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mTouchIntention.set(touchIntention);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(touchIntention);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(FocusRange focusRange) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mFocusRange.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mFocusRange.set(focusRange);
        if (FocusRange.MF == focusRange) {
            this.mCapturingModeParams.mFocusRange.canChanged();
        }
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(focusRange);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(ShutterSpeed shutterSpeed) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mShutterSpeed.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mShutterSpeed.set(shutterSpeed);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(shutterSpeed);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(FusionMode fusionMode) {
        ParameterUtil.reset(this.mCapturingModeParams.mFusionMode, fusionMode);
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mFusionMode.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        this.mCapturingModeParams.mFusionMode.set(fusionMode);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(fusionMode);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(AutoReview autoReview) {
        this.mIndependentParams.mAutoReview.set(autoReview);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Geotag geotag) {
        this.mIndependentParams.mGeoTag.set(geotag);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(FastCapture fastCapture) {
        this.mIndependentParams.mFastCapture.set(fastCapture);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(TouchCapture touchCapture) {
        this.mIndependentParams.mTouchCapture.set(touchCapture);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(ShutterSound shutterSound) {
        this.mIndependentParams.mShutterSound.set(shutterSound);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(DestinationToSave destinationToSave) {
        this.mIndependentParams.mDestinationToSave.set(destinationToSave);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VolumeKey volumeKey) {
        this.mIndependentParams.mVolumeKey.set(volumeKey);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(GridLine gridLine) {
        this.mIndependentParams.mGridLine.set(gridLine);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(SideSense sideSense) {
        this.mIndependentParams.mSideSense.set(sideSense);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(HelpGuide helpGuide) {
        this.mIndependentParams.mHelpGuide.set(helpGuide);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(ResetSettings resetSettings) {
        this.mIndependentParams.mResetSettings.set(resetSettings);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(DistortionCorrection distortionCorrection) {
        this.mIndependentParams.mDistortionCorrection.set(distortionCorrection);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void commit() {
        for (UserSettingKey userSettingKey : this.mHolders.keySet()) {
            if (this.mHolders.get(userSettingKey).hasChanged()) {
                this.mHolders.get(userSettingKey).onApplied();
            }
        }
    }

    UserSettingValue get(UserSettingKey userSettingKey) {
        switch (userSettingKey) {
            case CAPTURING_MODE:
                return this.capturingMode;
            case CAMERA_KEY:
                return this.mIndependentParams.mBurstByCameraKey.get();
            case PREDICTIVE_CAPTURE:
                return getPredictiveCapture();
            case EV:
                return getEv();
            case FACING:
                return getFacing();
            case FLASH:
                return getFlash();
            case DISPLAY_FLASH:
                return getDisplayFlash();
            case FOCUS_MODE:
                return getFocusMode();
            case OBJECT_TRACKING:
                return getObjectTracking();
            case HDR:
                return getHdr();
            case ISO:
                return getIso();
            case METERING:
                return getMetering();
            case PHOTO_LIGHT:
                return getPhotoLight();
            case RESOLUTION:
                return getResolution();
            case ASPECT_RATIO:
                return getAspectRatio();
            case SELF_TIMER:
                return getSelfTimer();
            case SMILE_CAPTURE:
                return getSmileCapture();
            case SHUTTER_TRIGGER:
                return getShutterTrigger();
            case SOFT_SKIN:
                return getSoftSkin();
            case VIDEO_SHUTTER_TRIGGER:
                return getVideoShutterTrigger();
            case VIDEO_STABILIZER:
                return getVideoStabilizer();
            case VIDEO_SIZE:
                return getVideoSize();
            case VIDEO_HDR:
                return getVideoHdr();
            case WHITE_BALANCE:
                return getWhiteBalance();
            case VIDEO_SMILE_CAPTURE:
                return getVideoSmileCapture();
            case VIDEO_CODEC:
                return getVideoCodec();
            case SHUTTER_SPEED:
                return getShutterSpeed();
            case FOCUS_RANGE:
                return getFocusRange();
            case TOUCH_INTENTION:
                return getTouchIntention();
            case AUTO_REVIEW:
                return this.mIndependentParams.mAutoReview.get();
            case GEO_TAG:
                return this.mIndependentParams.mGeoTag.get();
            case FAST_CAPTURE:
                return this.mIndependentParams.mFastCapture.get();
            case TOUCH_CAPTURE:
                return this.mIndependentParams.mTouchCapture.get();
            case SHUTTER_SOUND:
                return this.mIndependentParams.mShutterSound.get();
            case DESTINATION_TO_SAVE:
                return this.mIndependentParams.mDestinationToSave.get();
            case VOLUME_KEY:
                return this.mIndependentParams.mVolumeKey.get();
            case GRID_LINE:
                return this.mIndependentParams.mGridLine.get();
            case SIDE_SENSE:
                return this.mIndependentParams.mSideSense.get();
            case SLOW_MOTION:
                return getSlowMotion();
            case FRONT_ANGLE:
                return this.mIndependentParams.mFrontAngle.get();
            case FUSION_MODE:
                return getFusionMode();
            case DISTORTION_CORRECTION:
                return this.mIndependentParams.mDistortionCorrection.get();
            case PREDICTIVE_LAUNCH:
                return this.mIndependentParams.mPredictiveLaunch.get();
            default:
                return null;
        }
    }

    public PredictiveCapture getPredictiveCapture() {
        return (PredictiveCapture) this.mCapturingModeParams.mPredictiveCapture.get();
    }

    public Ev getEv() {
        return (Ev) this.mCapturingModeParams.mEv.get();
    }

    public Facing getFacing() {
        return (Facing) this.mCapturingModeParams.mFacing.get();
    }

    public Flash getFlash() {
        return (Flash) this.mIndependentParams.mFlash.get();
    }

    public DisplayFlash getDisplayFlash() {
        return (DisplayFlash) this.mIndependentParams.mDisplayFlash.get();
    }

    public FocusMode getFocusMode() {
        return (FocusMode) this.mCapturingModeParams.mFocusMode.get();
    }

    public ObjectTracking getObjectTracking() {
        return (ObjectTracking) this.mCapturingModeParams.mObjectTracking.get();
    }

    public Hdr getHdr() {
        return (Hdr) this.mCapturingModeParams.mHdr.get();
    }

    public Iso getIso() {
        return (Iso) this.mCapturingModeParams.mIso.get();
    }

    public Metering getMetering() {
        return (Metering) this.mCapturingModeParams.mMetering.get();
    }

    public PhotoLight getPhotoLight() {
        return (PhotoLight) this.mIndependentParams.mPhotoLight.get();
    }

    public Resolution getResolution() {
        return (Resolution) this.mCapturingModeParams.mResolution.get();
    }

    public AspectRatio getAspectRatio() {
        return (AspectRatio) this.mCapturingModeParams.mAspectRatio.get();
    }

    public SelfTimer getSelfTimer() {
        return (SelfTimer) this.mCapturingModeParams.mSelfTimer.get();
    }

    public SmileCapture getSmileCapture() {
        return ((ShutterTrigger) this.mCapturingModeParams.mShutterTrigger.get()).getSmileCapture();
    }

    public ShutterTrigger getShutterTrigger() {
        return (ShutterTrigger) this.mCapturingModeParams.mShutterTrigger.get();
    }

    public SoftSkin getSoftSkin() {
        return (SoftSkin) this.mCapturingModeParams.mSoftSkin.get();
    }

    public VideoStabilizer getVideoStabilizer() {
        return (VideoStabilizer) this.mCapturingModeParams.mVideoStabilizer.get();
    }

    public VideoSize getVideoSize() {
        return (VideoSize) this.mCapturingModeParams.mVideoSize.get();
    }

    public VideoHdr getVideoHdr() {
        return (VideoHdr) this.mCapturingModeParams.mVideoHdr.get();
    }

    public WhiteBalance getWhiteBalance() {
        return (WhiteBalance) this.mCapturingModeParams.mWhiteBalance.get();
    }

    public VideoSmileCapture getVideoSmileCapture() {
        return ((VideoShutterTrigger) this.mCapturingModeParams.mVideoShutterTrigger.get()).getVideoSmileCapture();
    }

    public VideoCodec getVideoCodec() {
        return (VideoCodec) this.mCapturingModeParams.mVideoCodec.get();
    }

    public ShutterSpeed getShutterSpeed() {
        return (ShutterSpeed) this.mCapturingModeParams.mShutterSpeed.get();
    }

    public FocusRange getFocusRange() {
        return (FocusRange) this.mCapturingModeParams.mFocusRange.get();
    }

    public TouchIntention getTouchIntention() {
        return (TouchIntention) this.mCapturingModeParams.mTouchIntention.get();
    }

    public VideoShutterTrigger getVideoShutterTrigger() {
        return (VideoShutterTrigger) this.mCapturingModeParams.mVideoShutterTrigger.get();
    }

    public SlowMotion getSlowMotion() {
        return (SlowMotion) this.mCapturingModeParams.mSlowMotion.get();
    }

    public FusionMode getFusionMode() {
        return (FusionMode) this.mCapturingModeParams.mFusionMode.get();
    }
}
