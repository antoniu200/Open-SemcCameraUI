// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.IntentReader;
import android.net.Uri;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import java.util.Collection;
import com.sonyericsson.android.camera.configuration.parameters.CameraKey;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.PerfLog;
import java.util.Iterator;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import java.util.HashMap;
import java.util.ArrayList;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable;
import java.util.List;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import java.util.Map;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.setting.UserSettingsLoader;
import com.sonyericsson.android.camera.setting.ExtraSettings;
import android.content.Context;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.setting.UserSettings;

public class UserSettingManager implements UserSettings
{
    public static final String TAG = "UserSettingManager";
    private boolean mCanWrite;
    private Configurations mConfig;
    private final Context mContext;
    private Parameters mCurrentParameters;
    private ExtraSettings mExtraSettings;
    private final ModeIndependentParams mIndependentParams;
    private final UserSettingsLoader.OnLoadCompletedListener mLoadCompletedListener;
    private LaunchCondition.OneShotMode mOneShotMode;
    private volatile Map<CapturingMode, Parameters> mParametersEntries;
    private DestinationToSave mRequestedDestination;
    private final SecureSetting mSecureSetting;
    private final Storage mStorage;
    private final List<UserSettingApplicable> mUserSettingApplicableEntries;
    private final UserSettingsLoader mUserSettingLoader;
    
    public UserSettingManager(final Context mContext, final Storage mStorage) {
        this.mRequestedDestination = null;
        this.mLoadCompletedListener = new UserSettingsLoader.OnLoadCompletedListener() {
            final UserSettingManager this$0;
            
            @Override
            public void onLoadCompleted() {
                if (CamLog.VERBOSE) {
                    CamLog.d("all user settings load completed");
                }
            }
        };
        this.mContext = mContext;
        this.mStorage = mStorage;
        this.mUserSettingLoader = ((CameraApplication)mContext.getApplicationContext()).getUserSettingsLoader();
        this.mUserSettingApplicableEntries = new ArrayList<UserSettingApplicable>();
        this.mParametersEntries = new HashMap<CapturingMode, Parameters>();
        this.mSecureSetting = new SecureSetting(mContext);
        (this.mIndependentParams = new ModeIndependentParams()).clear(mStorage);
    }
    
    private void applyChangedValues(final List<UserSettingValue> list) {
        for (final UserSettingValue obj : list) {
            final Iterator<UserSettingApplicable> iterator2 = this.mUserSettingApplicableEntries.iterator();
            while (iterator2.hasNext()) {
                obj.apply(iterator2.next());
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("set: applied(key, value) = (");
                sb.append(obj.getKey());
                sb.append(", ");
                sb.append(obj);
                sb.append(")");
                CamLog.d(sb.toString());
            }
            this.saveImmediatelyIfNeeded(obj);
        }
        LocalResearchUtil.getInstance().clearAllSettings();
        final Iterator<UserSettingValue> iterator3 = this.mCurrentParameters.getTargetParameters().values().iterator();
        while (iterator3.hasNext()) {
            LocalResearchUtil.getInstance().setAllSettingsValue(iterator3.next(), this.mCurrentParameters.capturingMode);
        }
        if (list.size() > 0) {
            final Iterator<UserSettingApplicable> iterator4 = this.mUserSettingApplicableEntries.iterator();
            while (iterator4.hasNext()) {
                iterator4.next().commit();
            }
        }
    }
    
    private void applyExtraSettings(final Parameters parameters, final List<UserSettingValue> list) {
        if (list != null && !list.isEmpty()) {
            final Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                ((UserSettingValue)iterator.next()).apply(parameters);
            }
        }
    }
    
    private void clearParametersEntries(final boolean b) {
        for (final Parameters parameters : this.mParametersEntries.values()) {
            if (b || parameters.capturingMode != this.mCurrentParameters.capturingMode) {
                parameters.clearHolder();
            }
        }
        this.mParametersEntries.clear();
        if (!b) {
            this.mParametersEntries.put(this.mCurrentParameters.capturingMode, this.mCurrentParameters);
        }
    }
    
    private void loadCurrentCapturingMode(final CapturingMode capturingMode) {
        if (capturingMode == null) {
            return;
        }
        if (!this.needReload(capturingMode)) {
            return;
        }
        PerfLog.LOAD_USER_SETTING_CURRENT.begin();
        final Parameters userSettingParameters = this.mUserSettingLoader.getUserSettingParameters(this.mContext, capturingMode, this.mStorage, this.mConfig, this.mOneShotMode.isEnabled(), this.mIndependentParams, this.mParametersEntries.isEmpty());
        for (final UserSettingKey userSettingKey : UserSettingKey.values()) {
            if (!this.isNeededToLoad(userSettingKey, this.mOneShotMode)) {
                userSettingParameters.mHolders.remove(userSettingKey);
            }
        }
        userSettingParameters.updatePhotoLight();
        this.setDefaultToNonExistentResolution(userSettingParameters);
        this.setDefaultToNonExistentVideoSize(userSettingParameters);
        this.setDefaultToNonExistentVideoShutterTrigger(userSettingParameters);
        if (this.mOneShotMode.isEnabled()) {
            this.setDefaultToUserSettingForOneshot(userSettingParameters);
        }
        Geotag geotag;
        if (GeotagManager.isGeoTagEnabled(userSettingParameters.mIndependentParams.mGeoTag.get(), this.mContext)) {
            geotag = Geotag.ON;
        }
        else {
            geotag = Geotag.OFF;
        }
        userSettingParameters.set(geotag);
        if (capturingMode.getType() == 2) {
            this.setupVideoOption(userSettingParameters);
        }
        this.setExtraSettings(this.mExtraSettings, userSettingParameters);
        userSettingParameters.commit();
        this.mParametersEntries.put(capturingMode, userSettingParameters);
        PerfLog.LOAD_USER_SETTING_CURRENT.end();
    }
    
    private boolean needReload(final CapturingMode capturingMode) {
        final int size = this.mParametersEntries.size();
        final boolean b = true;
        if (size == 1) {
            final boolean b2 = b;
            if (this.mParametersEntries.containsKey(capturingMode)) {
                return b2;
            }
        }
        return !this.mParametersEntries.containsKey(capturingMode) && b;
    }
    
    private void saveImmediatelyIfNeeded(final UserSettingValue userSettingValue) {
        final UserSettingKey key = userSettingValue.getKey();
        switch (UserSettingManager$2.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[key.ordinal()]) {
            default: {
                if (key.isSecureSetting()) {
                    this.mSecureSetting.set(userSettingValue);
                    break;
                }
                break;
            }
            case 3:
            case 4:
            case 5: {
                if (!this.mOneShotMode.isEnabled() && !this.mCanWrite) {
                    throw new IllegalStateException("Settings SHOULD not be saved after pausing");
                }
                this.mCurrentParameters.writeSharedPrefs(this.mUserSettingLoader.getSharedPreferencesAccessor(), key);
                break;
            }
        }
    }
    
    private void setDefaultToNonExistentResolution(final Parameters parameters) {
        final Resolution[] options = Resolution.getOptions(parameters.capturingMode);
        final int length = options.length;
        int i = 0;
        boolean b = false;
        while (i < length) {
            if (options[i].equals(parameters.getResolution())) {
                b = true;
            }
            ++i;
        }
        if (!b) {
            parameters.set(Resolution.getDefaultValue(parameters.capturingMode));
        }
    }
    
    private void setDefaultToNonExistentVideoShutterTrigger(final Parameters parameters) {
        final VideoShutterTrigger[] options = VideoShutterTrigger.getOptions(parameters.capturingMode, this.mOneShotMode.isVideo());
        final int length = options.length;
        int i = 0;
        boolean b = false;
        while (i < length) {
            if (options[i].equals(parameters.getVideoShutterTrigger())) {
                b = true;
            }
            ++i;
        }
        if (!b) {
            parameters.set(VideoShutterTrigger.getDefaultValue(parameters.capturingMode, this.mOneShotMode.isVideo()));
        }
    }
    
    private void setDefaultToNonExistentVideoSize(final Parameters parameters) {
        final ActionMode actionMode = new ActionMode(this.mOneShotMode.isEnabled(), parameters.capturingMode.getType(), parameters.capturingMode.getCameraId());
        final VideoSize[] options = VideoSize.getOptions(actionMode, this.mConfig);
        final int length = options.length;
        int i = 0;
        boolean b = false;
        while (i < length) {
            if (options[i].equals(parameters.getVideoSize())) {
                b = true;
            }
            ++i;
        }
        if (!b) {
            parameters.set(VideoSize.getDefaultValue(actionMode, this.mConfig, null, this.mIndependentParams.mDestinationToSave.get().getType()));
        }
    }
    
    private void setDefaultToUserSettingForOneshot(final Parameters parameters) {
        parameters.set(PredictiveCapture.getDefaultValue(this.mOneShotMode.isEnabled(), parameters.capturingMode));
        parameters.set(AutoReview.getDefaultValue(this.mOneShotMode.isEnabled()));
        parameters.set(CameraKey.getDefaultValue());
    }
    
    private void setExtraSettings(final ExtraSettings extraSettings, final Parameters parameters) {
        if (extraSettings != null) {
            final Map<CapturingMode, List<UserSettingValue>> values = extraSettings.getValues();
            if (!values.isEmpty()) {
                for (final CapturingMode capturingMode : extraSettings.getValues().keySet()) {
                    final List list = values.get(capturingMode);
                    if (parameters.capturingMode == capturingMode) {
                        this.applyExtraSettings(parameters, list);
                        extraSettings.clearValue(capturingMode);
                    }
                    if (this.mParametersEntries.keySet().contains(capturingMode)) {
                        this.applyExtraSettings(this.mParametersEntries.get(capturingMode), list);
                        extraSettings.clearValue(capturingMode);
                    }
                }
            }
        }
    }
    
    private void suspend() {
        this.mCanWrite = false;
        if (!this.mOneShotMode.isEnabled()) {
            this.mUserSettingLoader.save(this.mParametersEntries, this.mCurrentParameters.capturingMode);
        }
        this.clearParametersEntries(false);
    }
    
    private void updateVideoOption() {
        this.mCurrentParameters.mCapturingModeParams.mVideoSize.setOptions(VideoSize.getOptions(new ActionMode(this.mOneShotMode.isEnabled(), this.mCurrentParameters.capturingMode.getType(), this.mCurrentParameters.capturingMode.getCameraId()), this.mConfig));
    }
    
    @Override
    public void applyCapturingMode() {
        final ArrayList list = new ArrayList((Collection<? extends E>)this.mCurrentParameters.getTargetParameters().values());
        this.mCurrentParameters.commit();
        this.applyChangedValues(list);
        this.updateVideoOption();
    }
    
    @Override
    public void changeCapturingMode(final CapturingMode obj) {
        final StringBuilder sb = new StringBuilder();
        sb.append("change capturing mode to ");
        sb.append(obj);
        final String string = sb.toString();
        int i = 0;
        CamLog.d(string);
        this.mCanWrite = true;
        this.loadCurrentCapturingMode(obj);
        (this.mCurrentParameters = this.mParametersEntries.get(obj)).updateFocusParameters();
        this.mCurrentParameters.capturingMode.apply(this.mCurrentParameters);
        final ActionMode actionMode = new ActionMode(this.mOneShotMode.isEnabled(), obj.getType(), obj.getCameraId());
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
        for (UserSettingKey[] values = UserSettingKey.values(); i < values.length; ++i) {
            final UserSettingKey userSettingKey = values[i];
            userSettingKey.setSelectability(UserSettingSelectability.getSelectability(this.mCurrentParameters.getOptions(userSettingKey).length));
        }
        this.mCurrentParameters.updateSelectability();
        LocalResearchUtil.getInstance().clearAllSettings();
        final Iterator<UserSettingValue> iterator = this.mCurrentParameters.getTargetParameters().values().iterator();
        while (iterator.hasNext()) {
            LocalResearchUtil.getInstance().setAllSettingsValue(iterator.next(), this.mCurrentParameters.capturingMode);
        }
    }
    
    @Override
    public void clearCachedUserSetting() {
        this.clearParametersEntries(true);
    }
    
    @Override
    public void clearSavedUserSetting() {
        this.mSecureSetting.clear();
        this.mUserSettingLoader.clearMasterData();
        this.mUserSettingLoader.release();
        this.mUserSettingLoader.load();
    }
    
    @Override
    public void commit() {
        this.suspend();
    }
    
    @Override
    public UserSettingValue get(final UserSettingKey userSettingKey) {
        if (this.mCurrentParameters == null) {
            return null;
        }
        return this.mCurrentParameters.get(userSettingKey);
    }
    
    @Override
    public UserSettingValue get(final CapturingMode capturingMode, final UserSettingKey userSettingKey) {
        if (this.mParametersEntries.containsKey(capturingMode)) {
            return this.mParametersEntries.get(capturingMode).get(userSettingKey);
        }
        return this.get(userSettingKey);
    }
    
    @Override
    public MaxVideoSize getMaxVideoSize(final Storage storage, final Storage.StorageType storageType, final RecordingProfile recordingProfile) {
        return MaxVideoSize.create(this.mConfig, recordingProfile, storage, storageType);
    }
    
    @Override
    public UserSettingValue[] getOptions(final UserSettingKey userSettingKey) {
        return this.mCurrentParameters.getOptions(userSettingKey);
    }
    
    @Override
    public Parameters getParameters() {
        return this.mCurrentParameters;
    }
    
    @Override
    public boolean isLimitForSizeOrDuration() {
        return this.mConfig.hasLimitForSizeOrDuration();
    }
    
    boolean isNeededToLoad(final UserSettingKey userSettingKey, final LaunchCondition.OneShotMode oneShotMode) {
        if (!userSettingKey.isSaved()) {
            return userSettingKey.isCommon();
        }
        if (oneShotMode.isVideo()) {
            return UserSettingManager$2.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] != 1;
        }
        return !oneShotMode.isPhoto() || UserSettingManager$2.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] != 2;
    }
    
    @Override
    public void prepare(final Context context, final LaunchCondition.OneShotMode mOneShotMode, final Uri uri, final IntentReader.VideoQualityConfigurations videoQualityConfigurations, final ExtraSettings mExtraSettings) {
        if (!PlatformCapability.isPrepared()) {
            throw new IllegalStateException("UseSettings has been used before PlatformCapability is prepared.");
        }
        this.mOneShotMode = mOneShotMode;
        (this.mConfig = new Configurations()).initInSync(videoQualityConfigurations);
        this.mExtraSettings = mExtraSettings;
        if (this.mOneShotMode.isEnabled() && uri != null) {
            this.mRequestedDestination = DestinationToSave.EMMC;
            if (StorageUtil.getStorageTypeFromUri(uri, this.mContext) == Storage.StorageType.EXTERNAL_CARD) {
                this.mRequestedDestination = DestinationToSave.SDCARD;
            }
        }
        this.mUserSettingLoader.registerLoadCompletedListener(this.mLoadCompletedListener);
    }
    
    @Override
    public void register(final UserSettingApplicable userSettingApplicable) {
        this.mUserSettingApplicableEntries.add(userSettingApplicable);
    }
    
    @Override
    public void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("release() is called.");
        }
        this.mUserSettingLoader.unregisterLoadCompletedListener(this.mLoadCompletedListener);
        this.clearParametersEntries(true);
        this.mUserSettingApplicableEntries.clear();
        this.mIndependentParams.clear(this.mStorage);
    }
    
    void replaceParameterEntries(final Map<CapturingMode, Parameters> map) {
        this.mParametersEntries.clear();
        this.mParametersEntries.putAll(map);
    }
    
    @Override
    public void resetTempParameters() {
        this.mCurrentParameters.resetTempParameters();
    }
    
    @Override
    public void set(final UserSettingValue userSettingValue) {
        final ArrayList list = new ArrayList();
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("set(");
            sb.append(userSettingValue.getKey());
            sb.append(")");
            CamLog.d(sb.toString());
        }
        userSettingValue.apply(this.mCurrentParameters);
        list.addAll(this.mCurrentParameters.getChangedValues());
        this.mCurrentParameters.commit();
        this.applyChangedValues(list);
    }
    
    void setDefaultToNonExistentResolution(final List<Parameters> list) {
        final Iterator<Parameters> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.setDefaultToNonExistentResolution(iterator.next());
        }
    }
    
    void setDefaultToNonExistentVideoShutterTrigger(final List<Parameters> list) {
        final Iterator<Parameters> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.setDefaultToNonExistentVideoShutterTrigger(iterator.next());
        }
    }
    
    void setDefaultToNonExistentVideoSize(final List<Parameters> list) {
        final Iterator<Parameters> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.setDefaultToNonExistentVideoSize(iterator.next());
        }
    }
    
    void setupVideoOption(final Parameters parameters) {
        final ActionMode actionMode = new ActionMode(this.mOneShotMode.isEnabled(), parameters.capturingMode.getType(), parameters.capturingMode.getCameraId());
        final VideoSize[] options = VideoSize.getOptions(actionMode, this.mConfig);
        if (this.mOneShotMode.isVideo()) {
            if (options.length == 1) {
                parameters.set(options[0]);
            }
            else {
                parameters.set(VideoSize.getDefaultValue(actionMode, this.mConfig, this.mStorage, this.mIndependentParams.mDestinationToSave.get().getType()));
                parameters.set(VideoHdr.HDR_OFF);
            }
        }
        parameters.mCapturingModeParams.mVideoSize.setOptions(options);
    }
}
