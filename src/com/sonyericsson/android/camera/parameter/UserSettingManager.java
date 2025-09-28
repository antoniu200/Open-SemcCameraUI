package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import android.net.Uri;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.IntentReader;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.CameraKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.ExtraSettings;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.setting.UserSettingsLoader;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class UserSettingManager implements UserSettings {
    public static final String TAG = "UserSettingManager";
    private boolean mCanWrite;
    private Configurations mConfig;
    private final Context mContext;
    private Parameters mCurrentParameters;
    private ExtraSettings mExtraSettings;
    private LaunchCondition.OneShotMode mOneShotMode;
    private final SecureSetting mSecureSetting;
    private final Storage mStorage;
    private final UserSettingsLoader mUserSettingLoader;
    private DestinationToSave mRequestedDestination = null;
    private final UserSettingsLoader.OnLoadCompletedListener mLoadCompletedListener = new UserSettingsLoader.OnLoadCompletedListener() { // from class: com.sonyericsson.android.camera.parameter.UserSettingManager.1
        @Override // com.sonyericsson.android.camera.setting.UserSettingsLoader.OnLoadCompletedListener
        public void onLoadCompleted() {
            if (CamLog.VERBOSE) {
                CamLog.d("all user settings load completed");
            }
        }
    };
    private final List<UserSettingApplicable> mUserSettingApplicableEntries = new ArrayList();
    private volatile Map<CapturingMode, Parameters> mParametersEntries = new HashMap();
    private final ModeIndependentParams mIndependentParams = new ModeIndependentParams();

    public UserSettingManager(Context context, Storage storage) {
        this.mContext = context;
        this.mStorage = storage;
        this.mUserSettingLoader = ((CameraApplication) context.getApplicationContext()).getUserSettingsLoader();
        this.mSecureSetting = new SecureSetting(context);
        this.mIndependentParams.clear(storage);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void prepare(Context context, LaunchCondition.OneShotMode oneShotMode, Uri uri, IntentReader.VideoQualityConfigurations videoQualityConfigurations, ExtraSettings extraSettings) {
        if (!PlatformCapability.isPrepared()) {
            throw new IllegalStateException("UseSettings has been used before PlatformCapability is prepared.");
        }
        this.mOneShotMode = oneShotMode;
        this.mConfig = new Configurations();
        this.mConfig.initInSync(videoQualityConfigurations);
        this.mExtraSettings = extraSettings;
        if (this.mOneShotMode.isEnabled() && uri != null) {
            this.mRequestedDestination = DestinationToSave.EMMC;
            if (StorageUtil.getStorageTypeFromUri(uri, this.mContext) == Storage.StorageType.EXTERNAL_CARD) {
                this.mRequestedDestination = DestinationToSave.SDCARD;
            }
        }
        this.mUserSettingLoader.registerLoadCompletedListener(this.mLoadCompletedListener);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void register(UserSettingApplicable userSettingApplicable) {
        this.mUserSettingApplicableEntries.add(userSettingApplicable);
    }

    void replaceParameterEntries(Map<CapturingMode, Parameters> map) {
        this.mParametersEntries.clear();
        this.mParametersEntries.putAll(map);
    }

    private void loadCurrentCapturingMode(CapturingMode capturingMode) {
        if (capturingMode != null && needReload(capturingMode)) {
            PerfLog.LOAD_USER_SETTING_CURRENT.begin();
            Parameters userSettingParameters = this.mUserSettingLoader.getUserSettingParameters(this.mContext, capturingMode, this.mStorage, this.mConfig, this.mOneShotMode.isEnabled(), this.mIndependentParams, this.mParametersEntries.isEmpty());
            for (UserSettingKey userSettingKey : UserSettingKey.values()) {
                if (!isNeededToLoad(userSettingKey, this.mOneShotMode)) {
                    userSettingParameters.mHolders.remove(userSettingKey);
                }
            }
            userSettingParameters.updatePhotoLight();
            setDefaultToNonExistentResolution(userSettingParameters);
            setDefaultToNonExistentVideoSize(userSettingParameters);
            setDefaultToNonExistentVideoShutterTrigger(userSettingParameters);
            if (this.mOneShotMode.isEnabled()) {
                setDefaultToUserSettingForOneshot(userSettingParameters);
            }
            userSettingParameters.set(GeotagManager.isGeoTagEnabled((Geotag) userSettingParameters.mIndependentParams.mGeoTag.get(), this.mContext) ? Geotag.ON : Geotag.OFF);
            if (capturingMode.getType() == 2) {
                setupVideoOption(userSettingParameters);
            }
            setExtraSettings(this.mExtraSettings, userSettingParameters);
            userSettingParameters.commit();
            this.mParametersEntries.put(capturingMode, userSettingParameters);
            PerfLog.LOAD_USER_SETTING_CURRENT.end();
        }
    }

    private boolean needReload(CapturingMode capturingMode) {
        return (this.mParametersEntries.size() == 1 && this.mParametersEntries.containsKey(capturingMode)) || !this.mParametersEntries.containsKey(capturingMode);
    }

    boolean isNeededToLoad(UserSettingKey userSettingKey, LaunchCondition.OneShotMode oneShotMode) {
        if (userSettingKey.isSaved()) {
            return oneShotMode.isVideo() ? AnonymousClass2.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] != 1 : (oneShotMode.isPhoto() && AnonymousClass2.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] == 2) ? false : true;
        }
        return userSettingKey.isCommon();
    }

    private void setExtraSettings(ExtraSettings extraSettings, Parameters parameters) {
        if (extraSettings != null) {
            Map<CapturingMode, List<UserSettingValue>> values = extraSettings.getValues();
            if (values.isEmpty()) {
                return;
            }
            for (CapturingMode capturingMode : extraSettings.getValues().keySet()) {
                List<UserSettingValue> list = values.get(capturingMode);
                if (parameters.capturingMode == capturingMode) {
                    applyExtraSettings(parameters, list);
                    extraSettings.clearValue(capturingMode);
                }
                if (this.mParametersEntries.keySet().contains(capturingMode)) {
                    applyExtraSettings(this.mParametersEntries.get(capturingMode), list);
                    extraSettings.clearValue(capturingMode);
                }
            }
        }
    }

    private void applyExtraSettings(Parameters parameters, List<UserSettingValue> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Iterator<UserSettingValue> it = list.iterator();
        while (it.hasNext()) {
            it.next().apply(parameters);
        }
    }

    void setupVideoOption(Parameters parameters) {
        ActionMode actionMode = new ActionMode(this.mOneShotMode.isEnabled(), parameters.capturingMode.getType(), parameters.capturingMode.getCameraId());
        VideoSize[] options = VideoSize.getOptions(actionMode, this.mConfig);
        if (this.mOneShotMode.isVideo()) {
            if (options.length == 1) {
                parameters.set(options[0]);
            } else {
                parameters.set(VideoSize.getDefaultValue(actionMode, this.mConfig, this.mStorage, ((DestinationToSave) this.mIndependentParams.mDestinationToSave.get()).getType()));
                parameters.set(VideoHdr.HDR_OFF);
            }
        }
        parameters.mCapturingModeParams.mVideoSize.setOptions(options);
    }

    private void updateVideoOption() {
        this.mCurrentParameters.mCapturingModeParams.mVideoSize.setOptions(VideoSize.getOptions(new ActionMode(this.mOneShotMode.isEnabled(), this.mCurrentParameters.capturingMode.getType(), this.mCurrentParameters.capturingMode.getCameraId()), this.mConfig));
    }

    private void setDefaultToUserSettingForOneshot(Parameters parameters) {
        parameters.set(PredictiveCapture.getDefaultValue(this.mOneShotMode.isEnabled(), parameters.capturingMode));
        parameters.set(AutoReview.getDefaultValue(this.mOneShotMode.isEnabled()));
        parameters.set(CameraKey.getDefaultValue());
    }

    void setDefaultToNonExistentResolution(List<Parameters> list) {
        Iterator<Parameters> it = list.iterator();
        while (it.hasNext()) {
            setDefaultToNonExistentResolution(it.next());
        }
    }

    private void setDefaultToNonExistentResolution(Parameters parameters) {
        boolean z = false;
        for (Resolution resolution : Resolution.getOptions(parameters.capturingMode)) {
            if (resolution.equals(parameters.getResolution())) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        parameters.set(Resolution.getDefaultValue(parameters.capturingMode));
    }

    void setDefaultToNonExistentVideoSize(List<Parameters> list) {
        Iterator<Parameters> it = list.iterator();
        while (it.hasNext()) {
            setDefaultToNonExistentVideoSize(it.next());
        }
    }

    private void setDefaultToNonExistentVideoSize(Parameters parameters) {
        ActionMode actionMode = new ActionMode(this.mOneShotMode.isEnabled(), parameters.capturingMode.getType(), parameters.capturingMode.getCameraId());
        boolean z = false;
        for (VideoSize videoSize : VideoSize.getOptions(actionMode, this.mConfig)) {
            if (videoSize.equals(parameters.getVideoSize())) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        parameters.set(VideoSize.getDefaultValue(actionMode, this.mConfig, null, ((DestinationToSave) this.mIndependentParams.mDestinationToSave.get()).getType()));
    }

    void setDefaultToNonExistentVideoShutterTrigger(List<Parameters> list) {
        Iterator<Parameters> it = list.iterator();
        while (it.hasNext()) {
            setDefaultToNonExistentVideoShutterTrigger(it.next());
        }
    }

    private void setDefaultToNonExistentVideoShutterTrigger(Parameters parameters) {
        boolean z = false;
        for (VideoShutterTrigger videoShutterTrigger : VideoShutterTrigger.getOptions(parameters.capturingMode, this.mOneShotMode.isVideo())) {
            if (videoShutterTrigger.equals(parameters.getVideoShutterTrigger())) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        parameters.set(VideoShutterTrigger.getDefaultValue(parameters.capturingMode, this.mOneShotMode.isVideo()));
    }

    private void suspend() {
        this.mCanWrite = false;
        if (!this.mOneShotMode.isEnabled()) {
            this.mUserSettingLoader.save(this.mParametersEntries, this.mCurrentParameters.capturingMode);
        }
        clearParametersEntries(false);
    }

    private void clearParametersEntries(boolean z) {
        for (Parameters parameters : this.mParametersEntries.values()) {
            if (z || parameters.capturingMode != this.mCurrentParameters.capturingMode) {
                parameters.clearHolder();
            }
        }
        this.mParametersEntries.clear();
        if (z) {
            return;
        }
        this.mParametersEntries.put(this.mCurrentParameters.capturingMode, this.mCurrentParameters);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("release() is called.");
        }
        this.mUserSettingLoader.unregisterLoadCompletedListener(this.mLoadCompletedListener);
        clearParametersEntries(true);
        this.mUserSettingApplicableEntries.clear();
        this.mIndependentParams.clear(this.mStorage);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public Parameters getParameters() {
        return this.mCurrentParameters;
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public UserSettingValue[] getOptions(UserSettingKey userSettingKey) {
        return this.mCurrentParameters.getOptions(userSettingKey);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public UserSettingValue get(UserSettingKey userSettingKey) {
        if (this.mCurrentParameters == null) {
            return null;
        }
        return this.mCurrentParameters.get(userSettingKey);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public UserSettingValue get(CapturingMode capturingMode, UserSettingKey userSettingKey) {
        if (this.mParametersEntries.containsKey(capturingMode)) {
            return this.mParametersEntries.get(capturingMode).get(userSettingKey);
        }
        return get(userSettingKey);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void set(UserSettingValue userSettingValue) throws IllegalArgumentException {
        ArrayList arrayList = new ArrayList();
        if (CamLog.VERBOSE) {
            CamLog.d("set(" + userSettingValue.getKey() + ")");
        }
        userSettingValue.apply(this.mCurrentParameters);
        arrayList.addAll(this.mCurrentParameters.getChangedValues());
        this.mCurrentParameters.commit();
        applyChangedValues(arrayList);
    }

    private void applyChangedValues(List<UserSettingValue> list) throws IllegalArgumentException {
        for (UserSettingValue userSettingValue : list) {
            Iterator<UserSettingApplicable> it = this.mUserSettingApplicableEntries.iterator();
            while (it.hasNext()) {
                userSettingValue.apply(it.next());
            }
            if (CamLog.VERBOSE) {
                CamLog.d("set: applied(key, value) = (" + userSettingValue.getKey() + ", " + userSettingValue + ")");
            }
            saveImmediatelyIfNeeded(userSettingValue);
        }
        LocalResearchUtil.getInstance().clearAllSettings();
        Iterator<UserSettingValue> it2 = this.mCurrentParameters.getTargetParameters().values().iterator();
        while (it2.hasNext()) {
            LocalResearchUtil.getInstance().setAllSettingsValue(it2.next(), this.mCurrentParameters.capturingMode);
        }
        if (list.size() > 0) {
            Iterator<UserSettingApplicable> it3 = this.mUserSettingApplicableEntries.iterator();
            while (it3.hasNext()) {
                it3.next().commit();
            }
        }
    }

    private void saveImmediatelyIfNeeded(UserSettingValue userSettingValue) throws IllegalArgumentException {
        UserSettingKey key = userSettingValue.getKey();
        switch (key) {
            case RESOLUTION:
            case VIDEO_SIZE:
            case VIDEO_STABILIZER:
                if (!this.mOneShotMode.isEnabled() && !this.mCanWrite) {
                    throw new IllegalStateException("Settings SHOULD not be saved after pausing");
                }
                this.mCurrentParameters.writeSharedPrefs(this.mUserSettingLoader.getSharedPreferencesAccessor(), key);
                return;
            default:
                if (key.isSecureSetting()) {
                    this.mSecureSetting.set(userSettingValue);
                    return;
                }
                return;
        }
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void changeCapturingMode(CapturingMode capturingMode) {
        CamLog.d("change capturing mode to " + capturingMode);
        this.mCanWrite = true;
        loadCurrentCapturingMode(capturingMode);
        this.mCurrentParameters = this.mParametersEntries.get(capturingMode);
        this.mCurrentParameters.updateFocusParameters();
        this.mCurrentParameters.capturingMode.apply(this.mCurrentParameters);
        ActionMode actionMode = new ActionMode(this.mOneShotMode.isEnabled(), capturingMode.getType(), capturingMode.getCameraId());
        this.mIndependentParams.mPhotoLight.setDefaultValue();
        if (this.mIndependentParams.mFlash.get() == Flash.LED_ON) {
            this.mIndependentParams.mFlash.setDefaultValue();
        }
        if (this.mRequestedDestination != null) {
            this.mIndependentParams.mDestinationToSave.set(this.mRequestedDestination);
        }
        this.mIndependentParams.mFlash.setOptions(Flash.getOptions(actionMode));
        this.mCurrentParameters.updateHolder(this.mIndependentParams.mFlash);
        this.mIndependentParams.mDisplayFlash.setOptions(DisplayFlash.getOptions(actionMode));
        this.mCurrentParameters.updateHolder(this.mIndependentParams.mDisplayFlash);
        this.mIndependentParams.mPhotoLight.setOptions(PhotoLight.getOptions(actionMode));
        this.mCurrentParameters.updateHolder(this.mIndependentParams.mPhotoLight);
        for (UserSettingKey userSettingKey : UserSettingKey.values()) {
            userSettingKey.setSelectability(UserSettingSelectability.getSelectability(this.mCurrentParameters.getOptions(userSettingKey).length));
        }
        this.mCurrentParameters.updateSelectability();
        LocalResearchUtil.getInstance().clearAllSettings();
        Iterator<UserSettingValue> it = this.mCurrentParameters.getTargetParameters().values().iterator();
        while (it.hasNext()) {
            LocalResearchUtil.getInstance().setAllSettingsValue(it.next(), this.mCurrentParameters.capturingMode);
        }
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void applyCapturingMode() throws IllegalArgumentException {
        ArrayList arrayList = new ArrayList(this.mCurrentParameters.getTargetParameters().values());
        this.mCurrentParameters.commit();
        applyChangedValues(arrayList);
        updateVideoOption();
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void commit() {
        suspend();
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void clearSavedUserSetting() throws IllegalArgumentException {
        this.mSecureSetting.clear();
        this.mUserSettingLoader.clearMasterData();
        this.mUserSettingLoader.release();
        this.mUserSettingLoader.load();
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void clearCachedUserSetting() {
        clearParametersEntries(true);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public MaxVideoSize getMaxVideoSize(Storage storage, Storage.StorageType storageType, RecordingProfile recordingProfile) {
        return MaxVideoSize.create(this.mConfig, recordingProfile, storage, storageType);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public boolean isLimitForSizeOrDuration() {
        return this.mConfig.hasLimitForSizeOrDuration();
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettings
    public void resetTempParameters() {
        this.mCurrentParameters.resetTempParameters();
    }
}
