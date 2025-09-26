// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.research;

import android.os.SystemClock;
import java.util.List;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonymobile.cameracommon.research.parameters.ShootingLabel;
import java.util.ArrayList;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.cameracommon.research.parameters.Screen;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import java.util.Arrays;
import java.util.Iterator;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import android.util.ArrayMap;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonyericsson.cameracommon.systemmonitor.BatteryChangedReceiver;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Map;

public class LocalResearchUtil
{
    public static final String TAG = "LocalResearchUtil";
    private static final LocalResearchUtil sInstance;
    private final Map<UserSettingKey, UserSettingValue> mAllSettingsPhoto;
    private final Map<UserSettingKey, UserSettingValue> mAllSettingsVideo;
    private BatteryChangedReceiver mBatteryChangedReceiver;
    private int mCurrentPageIndex;
    private GestureShutterValues mGestureShutterValues;
    private boolean mIsHeated;
    private boolean mIsReadMore;
    private String mModeChangeMethod;
    private ObjectTrackingValues mObjectTrackingValues;
    private Map<MeasurementKey, PerformanceData> mPerformanceDataMap;
    private boolean mPredictiveLaunchState;
    private String mRecognizedScene;
    private SemiAutoSettingValues mSemiAutoSettingValuesPhoto;
    private SemiAutoSettingValues mSemiAutoSettingValuesVideo;
    private final Map<UserSettingKey, BasisAndChange<UserSettingValue>> mSettingsPhoto;
    private final Map<UserSettingKey, BasisAndChange<UserSettingValue>> mSettingsVideo;
    private TutorialController.TutorialType mTutorialType;
    private long mWizardStartTime;
    
    static {
        sInstance = new LocalResearchUtil();
    }
    
    private LocalResearchUtil() {
        this.mSettingsPhoto = (Map<UserSettingKey, BasisAndChange<UserSettingValue>>)new ArrayMap();
        this.mSettingsVideo = (Map<UserSettingKey, BasisAndChange<UserSettingValue>>)new ArrayMap();
        this.mSemiAutoSettingValuesPhoto = null;
        this.mSemiAutoSettingValuesVideo = null;
        this.mGestureShutterValues = null;
        this.mObjectTrackingValues = null;
        this.mPredictiveLaunchState = false;
        this.mAllSettingsPhoto = (Map<UserSettingKey, UserSettingValue>)new ArrayMap();
        this.mAllSettingsVideo = (Map<UserSettingKey, UserSettingValue>)new ArrayMap();
        this.mRecognizedScene = CameraParameterConverter.SceneMode.AUTO.toString();
        this.mPerformanceDataMap = (Map<MeasurementKey, PerformanceData>)new ArrayMap();
        this.mIsHeated = false;
        this.mWizardStartTime = 0L;
        this.mTutorialType = null;
        this.mIsReadMore = false;
        this.mCurrentPageIndex = -1;
    }
    
    private Map<UserSettingKey, UserSettingValue> getAllSettingsMap(final Event.Category category) {
        Map<UserSettingKey, UserSettingValue> map = null;
        switch (LocalResearchUtil$1.$SwitchMap$com$sonymobile$cameracommon$research$parameters$Event$Category[category.ordinal()]) {
            default: {
                map = null;
                break;
            }
            case 4: {
                map = this.mAllSettingsVideo;
                break;
            }
            case 3: {
                map = this.mAllSettingsPhoto;
                break;
            }
        }
        return map;
    }
    
    private String getBatteryLevel() {
        int batteryLevel;
        if (this.mBatteryChangedReceiver != null) {
            batteryLevel = this.mBatteryChangedReceiver.getBatteryLevel();
        }
        else {
            batteryLevel = -1;
        }
        return String.valueOf(batteryLevel);
    }
    
    private Map<UserSettingKey, UserSettingValue> getCommonSettings(final Map<UserSettingKey, UserSettingValue> map) {
        final ArrayMap arrayMap = new ArrayMap();
        for (final Map.Entry<UserSettingKey, V> entry : map.entrySet()) {
            switch (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[entry.getKey().ordinal()]) {
                default: {
                    continue;
                }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10: {
                    ((Map<UserSettingKey, UserSettingValue>)arrayMap).put(entry.getKey(), map.get(entry.getKey()));
                    continue;
                }
            }
        }
        return (Map<UserSettingKey, UserSettingValue>)arrayMap;
    }
    
    private UserSettingValue getCurrentSetting(final UserSettingKey userSettingKey) {
        final Iterator<Event.Category> iterator = Arrays.asList(Event.Category.ALL_SETTINGS_PHOTO, Event.Category.ALL_SETTINGS_VIDEO).iterator();
        UserSettingValue userSettingValue = null;
        UserSettingValue userSettingValue2;
        while (true) {
            userSettingValue2 = userSettingValue;
            if (!iterator.hasNext()) {
                break;
            }
            final Map<UserSettingKey, UserSettingValue> allSettingsMap = this.getAllSettingsMap(iterator.next());
            if (allSettingsMap == null) {
                continue;
            }
            userSettingValue2 = allSettingsMap.get(userSettingKey);
            if ((userSettingValue = userSettingValue2) != null) {
                break;
            }
        }
        return userSettingValue2;
    }
    
    private GestureShutterValues getGestureShutterValues() {
        if (this.mGestureShutterValues == null) {
            this.mGestureShutterValues = new GestureShutterValues();
        }
        return this.mGestureShutterValues;
    }
    
    public static LocalResearchUtil getInstance() {
        return LocalResearchUtil.sInstance;
    }
    
    private String getModeName(final ModeSelectorInternalMode modeSelectorInternalMode) {
        if (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[modeSelectorInternalMode.ordinal()] != 1) {
            return "";
        }
        return "PORTRAIT_SELFIE";
    }
    
    private ObjectTrackingValues getObjectTrackingValues() {
        if (this.mObjectTrackingValues == null) {
            this.mObjectTrackingValues = new ObjectTrackingValues();
        }
        return this.mObjectTrackingValues;
    }
    
    private PerformanceData getPerformanceData(final MeasurementKey measurementKey) {
        PerformanceData performanceData;
        if ((performanceData = this.mPerformanceDataMap.get(measurementKey)) == null) {
            performanceData = new PerformanceData(measurementKey);
            this.mPerformanceDataMap.put(measurementKey, performanceData);
        }
        return performanceData;
    }
    
    private Screen getScreen(final CapturingMode obj) {
        Screen screen = null;
        switch (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[obj.ordinal()]) {
            default: {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("getScreen() : Not supported : ");
                    sb.append(obj);
                    CamLog.d(sb.toString());
                }
                screen = null;
                break;
            }
            case 7: {
                screen = Screen.SLOW_MOTION;
                break;
            }
            case 6: {
                screen = Screen.VIDEO_FRONT;
                break;
            }
            case 5: {
                screen = Screen.VIDEO_MAIN;
                break;
            }
            case 4: {
                screen = Screen.MANUAL_FRONT;
                break;
            }
            case 3: {
                screen = Screen.SUPERIOR_AUTO_FRONT;
                break;
            }
            case 2: {
                screen = Screen.MANUAL_MAIN;
                break;
            }
            case 1: {
                screen = Screen.SUPERIOR_AUTO_MAIN;
                break;
            }
        }
        return screen;
    }
    
    private SemiAutoSettingValues getSemiAutoSettingValues(final Event.Category category) {
        switch (LocalResearchUtil$1.$SwitchMap$com$sonymobile$cameracommon$research$parameters$Event$Category[category.ordinal()]) {
            default: {
                return null;
            }
            case 2:
            case 4: {
                if (this.mSemiAutoSettingValuesVideo == null) {
                    this.mSemiAutoSettingValuesVideo = new SemiAutoSettingValues();
                }
                return this.mSemiAutoSettingValuesVideo;
            }
            case 1:
            case 3: {
                if (this.mSemiAutoSettingValuesPhoto == null) {
                    this.mSemiAutoSettingValuesPhoto = new SemiAutoSettingValues();
                }
                return this.mSemiAutoSettingValuesPhoto;
            }
        }
    }
    
    private Map<UserSettingKey, BasisAndChange<UserSettingValue>> getSettingsMap(final Event.Category category) {
        Map<UserSettingKey, BasisAndChange<UserSettingValue>> map = null;
        switch (LocalResearchUtil$1.$SwitchMap$com$sonymobile$cameracommon$research$parameters$Event$Category[category.ordinal()]) {
            default: {
                map = null;
                break;
            }
            case 2: {
                map = this.mSettingsVideo;
                break;
            }
            case 1: {
                map = this.mSettingsPhoto;
                break;
            }
        }
        return map;
    }
    
    private boolean isHeated() {
        return this.mIsHeated;
    }
    
    private void sendEventSettings(final Event.Category category) {
        final Map<UserSettingKey, BasisAndChange<UserSettingValue>> settingsMap = this.getSettingsMap(category);
        if (settingsMap == null) {
            return;
        }
        if (!settingsMap.isEmpty()) {
            for (final Map.Entry<UserSettingKey, V> entry : settingsMap.entrySet()) {
                final UserSettingKey userSettingKey = entry.getKey();
                final BasisAndChange basisAndChange = (BasisAndChange)entry.getValue();
                if (basisAndChange.mChange == null) {
                    continue;
                }
                ResearchUtil.getInstance().sendEvent(category, userSettingKey.toString(), basisAndChange.mChange.toString());
            }
            settingsMap.clear();
        }
    }
    
    private void setAllSettingsPhotoVideo(final UserSettingValue userSettingValue, final Event.Category category) {
        final UserSettingKey key = userSettingValue.getKey();
        final Map<UserSettingKey, UserSettingValue> allSettingsMap = this.getAllSettingsMap(category);
        if (allSettingsMap == null) {
            return;
        }
        allSettingsMap.put(key, userSettingValue);
    }
    
    private void setSettingsPhotoVideo(final BasisAndChange<UserSettingValue> basisAndChange, final Event.Category obj) {
        final UserSettingKey key = ((UserSettingValue)((BasisAndChange<Object>)basisAndChange).mChange).getKey();
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setSettingsPhotoVideo() : Category = ");
            sb.append(obj);
            sb.append(", Key = ");
            sb.append(key);
            CamLog.d(sb.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("newValues    : Basis = ");
            sb2.append(((BasisAndChange<Object>)basisAndChange).mBasis);
            sb2.append(", Change = ");
            sb2.append(((BasisAndChange<Object>)basisAndChange).mChange);
            CamLog.d(sb2.toString());
        }
        final Map<UserSettingKey, BasisAndChange<UserSettingValue>> settingsMap = this.getSettingsMap(obj);
        if (settingsMap == null) {
            return;
        }
        if (settingsMap.containsKey(key)) {
            final BasisAndChange basisAndChange2 = settingsMap.get(key);
            if (basisAndChange2.mBasis.equals(((BasisAndChange<Object>)basisAndChange).mChange)) {
                settingsMap.remove(key);
            }
            else {
                basisAndChange2.mChange = (T)((BasisAndChange<Object>)basisAndChange).mChange;
                settingsMap.put(key, basisAndChange2);
            }
        }
        else {
            settingsMap.put(key, basisAndChange);
        }
    }
    
    public void clearAllSettings() {
        final Map<UserSettingKey, UserSettingValue> commonSettings = this.getCommonSettings(this.mAllSettingsPhoto);
        this.mAllSettingsPhoto.clear();
        this.mAllSettingsVideo.clear();
        this.mAllSettingsPhoto.putAll(commonSettings);
        this.mAllSettingsVideo.putAll(commonSettings);
    }
    
    public void clearRecognizedScene() {
        this.mRecognizedScene = CameraParameterConverter.SceneMode.AUTO.toString();
    }
    
    public void clearSemiAutoSettingValues() {
        this.mSemiAutoSettingValuesPhoto = null;
        this.mSemiAutoSettingValuesVideo = null;
    }
    
    public void clearSettings() {
        this.mSettingsPhoto.clear();
        this.mSettingsVideo.clear();
    }
    
    public void clearTemporarySettingValues() {
        this.clearSemiAutoSettingValues();
        this.clearRecognizedScene();
        ResearchUtil.getInstance().clearTemporarySettingValues();
    }
    
    public void closeSetupWizard() {
        this.mWizardStartTime = 0L;
        this.mCurrentPageIndex = -1;
        this.mTutorialType = null;
    }
    
    public void countUpHandSignLostNum() {
        this.getGestureShutterValues().countUpHandSignLostNum();
    }
    
    public Map<String, String> getAllSettingsMapString(final Event.Category category) {
        final ArrayMap arrayMap = new ArrayMap();
        final Map<UserSettingKey, UserSettingValue> allSettingsMap = this.getAllSettingsMap(category);
        if (allSettingsMap != null) {
            for (final Map.Entry<UserSettingKey, V> entry : allSettingsMap.entrySet()) {
                ((Map<String, String>)arrayMap).put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return (Map<String, String>)arrayMap;
    }
    
    public String getModeName(final CapturingMode capturingMode) {
        switch (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                return "";
            }
            case 7: {
                return "SLOW_MOTION";
            }
            case 6: {
                return "VIDEO_FRONT";
            }
            case 5: {
                return "VIDEO_MAIN";
            }
            case 4: {
                return "MUNAUL_FRONT";
            }
            case 3: {
                return "SUPERIOR_AUTO_FRONT";
            }
            case 2: {
                return "MUNAUL_MAIN";
            }
            case 1: {
                return "SUPERIOR_AUTO_MAIN";
            }
        }
    }
    
    public void initSetupwizard(final boolean mIsReadMore) {
        this.mIsReadMore = mIsReadMore;
    }
    
    public boolean isMeasurementValid(final MeasurementKey measurementKey) {
        return this.getPerformanceData(measurementKey).isValid();
    }
    
    public void resetHandSignLostNum() {
        this.getGestureShutterValues().resetHandSignLostNum();
    }
    
    public void sendEventAddonModeChange(final Event.Category category, final String s, final String s2) {
        ResearchUtil.getInstance().sendEventAddonModeChange(category, s, s2, this.mModeChangeMethod);
    }
    
    public void sendEventInternalModeChange(final CapturingMode capturingMode, final CapturingMode capturingMode2) {
        ResearchUtil.getInstance().sendEventInternalModeChange(this.getModeName(capturingMode), this.getModeName(capturingMode2), this.mModeChangeMethod);
    }
    
    public void sendEventInternalModeChange(final CapturingMode capturingMode, final ModeSelectorInternalMode modeSelectorInternalMode) {
        ResearchUtil.getInstance().sendEventInternalModeChange(this.getModeName(capturingMode), this.getModeName(modeSelectorInternalMode), this.mModeChangeMethod);
    }
    
    public void sendEventSettings() {
        this.sendEventSettings(Event.Category.SETTINGS_PHOTO);
        this.sendEventSettings(Event.Category.SETTINGS_VIDEO);
    }
    
    public void sendRecordingEvent(final Event.UserOperation userOperation, final Event.StopOperation stopOperation, final int n, final boolean b) {
        ResearchUtil.getInstance().sendRecordingEvent(userOperation, stopOperation, n, b, this.getAllSettingsMapString(Event.Category.ALL_SETTINGS_VIDEO));
    }
    
    public void sendSemiAutoSettingValues(final Event.Category category) {
        final SemiAutoSettingValues semiAutoSettingValues = this.getSemiAutoSettingValues(category);
        if (semiAutoSettingValues.hasChange()) {
            semiAutoSettingValues.applyChange();
            ResearchUtil.getInstance().sendEvent(category, UserSettingKey.SEMI_AUTO.toString(), semiAutoSettingValues.toString());
        }
    }
    
    public void sendSetupWizardEvent(final TutorialController.TutorialType mTutorialType, final int mCurrentPageIndex, final Event.WizardResult wizardResult) {
        final Event.WizardPage unknown = Event.WizardPage.UNKNOWN;
        this.mCurrentPageIndex = mCurrentPageIndex;
        this.mTutorialType = mTutorialType;
        if (this.mTutorialType == null) {
            CamLog.w("TutorialType is null");
            return;
        }
        Event.Action action = null;
        Label_0453: {
            switch (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[this.mTutorialType.ordinal()]) {
                default: {
                    action = unknown;
                    break;
                }
                case 12: {
                    switch (mCurrentPageIndex) {
                        default: {
                            action = unknown;
                            break Label_0453;
                        }
                        case 1: {
                            action = Event.WizardPage.VIDEO_FUSION_WIZARD2;
                            break Label_0453;
                        }
                        case 0: {
                            action = Event.WizardPage.VIDEO_FUSION_WIZARD1;
                            break Label_0453;
                        }
                    }
                    break;
                }
                case 11: {
                    switch (mCurrentPageIndex) {
                        default: {
                            action = unknown;
                            break Label_0453;
                        }
                        case 1: {
                            action = Event.WizardPage.MANUAL_FUSION_WIZARD2;
                            break Label_0453;
                        }
                        case 0: {
                            action = Event.WizardPage.MANUAL_FUSION_WIZARD1;
                            break Label_0453;
                        }
                    }
                    break;
                }
                case 10: {
                    switch (mCurrentPageIndex) {
                        default: {
                            action = unknown;
                            break Label_0453;
                        }
                        case 1: {
                            if (this.mIsReadMore) {
                                action = Event.WizardPage.READMORE_SLOWMOTION_WIZARD2;
                                break Label_0453;
                            }
                            action = Event.WizardPage.SLOWMOTION_WIZARD2;
                            break Label_0453;
                        }
                        case 0: {
                            if (this.mIsReadMore) {
                                action = Event.WizardPage.READMORE_SLOWMOTION_WIZARD1;
                                break Label_0453;
                            }
                            action = Event.WizardPage.SLOWMOTION_WIZARD1;
                            break Label_0453;
                        }
                    }
                    break;
                }
                case 9: {
                    if (this.mIsReadMore) {
                        action = Event.WizardPage.READMORE_ONE_SHOT_WIZARD;
                        break;
                    }
                    action = Event.WizardPage.ONE_SHOT_WIZARD;
                    break;
                }
                case 8: {
                    switch (mCurrentPageIndex) {
                        default: {
                            action = unknown;
                            break Label_0453;
                        }
                        case 2: {
                            action = Event.WizardPage.READMORE_SUPER_SLOWMOTION_WIZARD3;
                            break Label_0453;
                        }
                        case 1: {
                            action = Event.WizardPage.READMORE_SUPER_SLOWMOTION_WIZARD2;
                            break Label_0453;
                        }
                        case 0: {
                            action = Event.WizardPage.READMORE_SUPER_SLOWMOTION_WIZARD1;
                            break Label_0453;
                        }
                    }
                    break;
                }
                case 7: {
                    switch (mCurrentPageIndex) {
                        default: {
                            action = unknown;
                            break Label_0453;
                        }
                        case 3: {
                            action = Event.WizardPage.SUPER_SLOWMOTION_WIZARD4;
                            break Label_0453;
                        }
                        case 2: {
                            action = Event.WizardPage.SUPER_SLOWMOTION_WIZARD3;
                            break Label_0453;
                        }
                        case 1: {
                            action = Event.WizardPage.SUPER_SLOWMOTION_WIZARD2;
                            break Label_0453;
                        }
                        case 0: {
                            action = Event.WizardPage.SUPER_SLOWMOTION_WIZARD1;
                            break Label_0453;
                        }
                    }
                    break;
                }
                case 6: {
                    action = Event.WizardPage.HAND_SHUTTER_WIZARD;
                    break;
                }
                case 5: {
                    action = Event.WizardPage.EYE_POSITION_WIZARD;
                    break;
                }
                case 4: {
                    action = Event.WizardPage.SUPERIOR_AUTO_FUSION_WIZARD;
                    break;
                }
                case 3: {
                    action = Event.WizardPage.SIDE_SENSING_WIZARD;
                    break;
                }
                case 2: {
                    action = Event.WizardPage.PREDICTIVE_LAUNCH_WIZARD;
                    break;
                }
                case 1: {
                    action = Event.WizardPage.LOCATION_WIZARD;
                    break;
                }
            }
        }
        if (this.mWizardStartTime > 0L) {
            ResearchUtil.getInstance().sendSetupWizardEvent((Event.WizardPage)action, wizardResult, System.currentTimeMillis() - this.mWizardStartTime);
            this.mWizardStartTime = System.currentTimeMillis();
        }
    }
    
    public void sendSetupWizardEvent(final Event.WizardResult wizardResult) {
        this.sendSetupWizardEvent(this.mTutorialType, this.mCurrentPageIndex, wizardResult);
    }
    
    public void sendView(final LaunchCondition.LaunchTrigger launchTrigger, final CapturingMode capturingMode) {
        ResearchUtil.getInstance().sendView(launchTrigger, this.getScreen(capturingMode));
    }
    
    public void setAllSettingsValue(final UserSettingValue userSettingValue, final CapturingMode capturingMode) {
        final UserSettingKey key = userSettingValue.getKey();
        Label_0419: {
            switch (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[key.ordinal()]) {
                default: {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("setAllSettingsValue() : Not supported : ");
                        sb.append(key);
                        CamLog.d(sb.toString());
                        break;
                    }
                    break;
                }
                case 38: {
                    final int n = LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()];
                    if (n == 6) {
                        this.setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_VIDEO);
                        break;
                    }
                    switch (n) {
                        default: {
                            break Label_0419;
                        }
                        case 3:
                        case 4: {
                            this.setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_PHOTO);
                            break Label_0419;
                        }
                    }
                    break;
                }
                case 36:
                case 37: {
                    switch (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
                        default: {
                            break Label_0419;
                        }
                        case 5:
                        case 6:
                        case 7: {
                            this.setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_VIDEO);
                            break Label_0419;
                        }
                        case 1:
                        case 2:
                        case 3:
                        case 4: {
                            this.setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_PHOTO);
                            break Label_0419;
                        }
                    }
                    break;
                }
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35: {
                    this.setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_VIDEO);
                    break;
                }
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28: {
                    this.setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_PHOTO);
                    break;
                }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 39:
                case 40:
                case 41: {
                    this.setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_PHOTO);
                    this.setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_VIDEO);
                    break;
                }
            }
        }
    }
    
    public void setBatteryChangedReceiver(final BatteryChangedReceiver mBatteryChangedReceiver) {
        this.mBatteryChangedReceiver = mBatteryChangedReceiver;
    }
    
    public void setLaunchBy(final LaunchCondition.LaunchTrigger launchBy) {
        ResearchUtil.getInstance().setLaunchBy(launchBy);
    }
    
    public void setMeasurementInvalid(final MeasurementKey measurementKey) {
        this.getPerformanceData(measurementKey).setInvalid();
    }
    
    public void setMeasurementThermal(final boolean mIsHeated) {
        this.mIsHeated = mIsHeated;
    }
    
    public void setMeasurementValid(final MeasurementKey measurementKey) {
        this.getPerformanceData(measurementKey).setValid();
    }
    
    public void setModeChangeMethod(final ModeChangeMethod modeChangeMethod) {
        this.mModeChangeMethod = modeChangeMethod.toString();
    }
    
    public void setObjectTrackingTarget(final boolean objectTrackingTarget) {
        this.getObjectTrackingValues().setObjectTrackingTarget(objectTrackingTarget);
    }
    
    public void setPredictiveLaunchState(final boolean mPredictiveLaunchState) {
        this.mPredictiveLaunchState = mPredictiveLaunchState;
    }
    
    public void setRecognizedScene(final String mRecognizedScene) {
        this.mRecognizedScene = mRecognizedScene;
    }
    
    public void setSemiAutoSettingAmberBlueValue(final int n) {
        this.getSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO).updateAmberBlue(n);
        this.getSemiAutoSettingValues(Event.Category.SETTINGS_VIDEO).updateAmberBlue(n);
    }
    
    public void setSemiAutoSettingBrightnessValue(final int n) {
        this.getSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO).updateBrightness(n);
        this.getSemiAutoSettingValues(Event.Category.SETTINGS_VIDEO).updateBrightness(n);
    }
    
    public void setSettingsValue(final UserSettingValue userSettingValue, final UserSettingValue userSettingValue2, final CapturingMode capturingMode) {
        final UserSettingKey key = userSettingValue2.getKey();
        UserSettingValue currentSetting = userSettingValue;
        if (userSettingValue == null) {
            currentSetting = this.getCurrentSetting(key);
        }
        final BasisAndChange basisAndChange = new BasisAndChange(currentSetting, userSettingValue2);
        if (!basisAndChange.hasChange()) {
            if (CamLog.VERBOSE) {
                CamLog.d("setSettingsValue() : Not changed.");
            }
            return;
        }
        while (true) {
            switch (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[key.ordinal()]) {
                default: {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("setSettingsValue() : Not supported : ");
                        sb.append(key);
                        CamLog.d(sb.toString());
                    }
                    return;
                }
                case 38: {
                    final int n = LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()];
                    if (n == 6) {
                        this.setSettingsPhotoVideo((BasisAndChange<UserSettingValue>)basisAndChange, Event.Category.SETTINGS_VIDEO);
                        break Label_0456;
                    }
                    switch (n) {
                        default: {
                            break Label_0456;
                        }
                        case 3:
                        case 4: {
                            this.setSettingsPhotoVideo((BasisAndChange<UserSettingValue>)basisAndChange, Event.Category.SETTINGS_PHOTO);
                            break Label_0456;
                        }
                    }
                    break;
                }
                case 36:
                case 37: {
                    switch (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
                        default: {
                            break Label_0456;
                        }
                        case 5:
                        case 6:
                        case 7: {
                            this.setSettingsPhotoVideo((BasisAndChange<UserSettingValue>)basisAndChange, Event.Category.SETTINGS_VIDEO);
                            break Label_0456;
                        }
                        case 1:
                        case 2:
                        case 3:
                        case 4: {
                            this.setSettingsPhotoVideo((BasisAndChange<UserSettingValue>)basisAndChange, Event.Category.SETTINGS_PHOTO);
                            break Label_0456;
                        }
                    }
                    break;
                }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 39:
                case 40:
                case 41: {
                    ResearchUtil.getInstance().sendEventChangedSetting(key.toString(), ((BasisAndChange<Object>)basisAndChange).mBasis.toString(), ((BasisAndChange<Object>)basisAndChange).mChange.toString());
                    this.setAllSettingsValue((UserSettingValue)((BasisAndChange<Object>)basisAndChange).mChange, capturingMode);
                    return;
                }
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35: {
                    this.setSettingsPhotoVideo((BasisAndChange<UserSettingValue>)basisAndChange, Event.Category.SETTINGS_VIDEO);
                    continue;
                }
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28: {
                    this.setSettingsPhotoVideo((BasisAndChange<UserSettingValue>)basisAndChange, Event.Category.SETTINGS_PHOTO);
                    continue;
                }
            }
            break;
        }
    }
    
    public void setSettingsValue(final UserSettings userSettings, final CapturingMode capturingMode) {
        final UserSettingKey[] values = UserSettingKey.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final UserSettingValue value = userSettings.get(values[i]);
            if (value != null) {
                this.setAllSettingsValue(value, capturingMode);
            }
        }
    }
    
    public void setUserOperation(final Event.UserOperation userOperation, final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        final Event.Category all_SETTINGS_PHOTO = Event.Category.ALL_SETTINGS_PHOTO;
        list.add(ShootingLabel.getRecognizedSceneParameter(this.mRecognizedScene));
        UserSettingValue off;
        if ((off = this.getAllSettingsMap(all_SETTINGS_PHOTO).get(UserSettingKey.FLASH)) == null) {
            off = Flash.OFF;
        }
        list.add(ShootingLabel.getFlashParameter(off.toString()));
        UserSettingValue off2;
        if ((off2 = this.getAllSettingsMap(all_SETTINGS_PHOTO).get(UserSettingKey.SELF_TIMER)) == null) {
            off2 = SelfTimer.OFF;
        }
        list.add(ShootingLabel.getSelfTimerParameter(off2.toString()));
        String string;
        if (capturingMode.isFront() && PlatformCapability.isSuperWideSupported(CameraInfo.CameraId.FRONT)) {
            UserSettingValue default1;
            if ((default1 = this.getAllSettingsMap(all_SETTINGS_PHOTO).get(UserSettingKey.FRONT_ANGLE)) == null) {
                default1 = FrontAngle.DEFAULT;
            }
            string = default1.toString();
        }
        else {
            string = "INVALID";
        }
        list.add(ShootingLabel.getFrontAngleParameter(string));
        final SemiAutoSettingValues semiAutoSettingValues = this.getSemiAutoSettingValues(all_SETTINGS_PHOTO);
        if (semiAutoSettingValues != null) {
            list.add(ShootingLabel.getSemiAutoParameter((int)((semiAutoSettingValues.isEnabled() ^ true) ? 1 : 0)));
        }
        if (this.mGestureShutterValues != null) {
            list.add(this.mGestureShutterValues.getParameter());
        }
        UserSettingValue off3;
        if ((off3 = this.getAllSettingsMap(all_SETTINGS_PHOTO).get(UserSettingKey.OBJECT_TRACKING)) == null) {
            off3 = ObjectTracking.OFF;
        }
        list.add(this.getObjectTrackingValues().getParameter(off3.toString()));
        String s;
        if (this.mPredictiveLaunchState) {
            s = "True";
        }
        else {
            s = "False";
        }
        list.add(ShootingLabel.getPredictiveLaunchParameter(s));
        ResearchUtil.getInstance().setUserOperation(userOperation, list, this.getAllSettingsMapString(Event.Category.ALL_SETTINGS_PHOTO));
    }
    
    public void setView(final CapturingMode capturingMode) {
        ResearchUtil.getInstance().setView(this.getScreen(capturingMode));
    }
    
    public void startHandSignLostNumCounting() {
        this.getGestureShutterValues().startHandSignLostNumCounting();
    }
    
    public void startMeasurement(final MeasurementKey measurementKey) {
        this.getPerformanceData(measurementKey).start();
    }
    
    public void startSetupWizard(final TutorialController.TutorialType mTutorialType, final int mCurrentPageIndex) {
        this.mWizardStartTime = System.currentTimeMillis();
        this.mCurrentPageIndex = mCurrentPageIndex;
        this.mTutorialType = mTutorialType;
    }
    
    public void stopMeasurement(final MeasurementKey measurementKey) {
        final PerformanceData performanceData = this.getPerformanceData(measurementKey);
        if (performanceData.isValid()) {
            performanceData.stop();
        }
        performanceData.clear();
    }
    
    private static class BasisAndChange<T>
    {
        private T mBasis;
        private T mChange;
        
        public BasisAndChange(final T mBasis, final T mChange) {
            this.mBasis = null;
            this.mChange = null;
            this.mBasis = mBasis;
            this.mChange = mChange;
        }
        
        boolean hasChange() {
            return this.mBasis != this.mChange;
        }
    }
    
    private static final class GestureShutterValues
    {
        private int mHandSignLostNum;
        
        private GestureShutterValues() {
            this.mHandSignLostNum = -1;
        }
        
        public void countUpHandSignLostNum() {
            if (this.mHandSignLostNum == -1 && CamLog.VERBOSE) {
                CamLog.d("Counting up hand signs lost number from -1.");
            }
            ++this.mHandSignLostNum;
        }
        
        public ShootingLabel.Parameter getParameter() {
            return ShootingLabel.getHandSignLostParameter(this.mHandSignLostNum);
        }
        
        public void resetHandSignLostNum() {
            this.mHandSignLostNum = -1;
        }
        
        public void startHandSignLostNumCounting() {
            this.mHandSignLostNum = 0;
        }
    }
    
    public enum MeasurementKey
    {
        private static final MeasurementKey[] $VALUES;
        
        CLOSE_INITIAL_RESPONSE, 
        CLOSE_READY_FOR_USE, 
        LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE, 
        LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE, 
        LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE, 
        LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE, 
        LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE, 
        LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE, 
        LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE, 
        LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE, 
        SHOT_TO_SHOT_DELAY, 
        VIDEO_RECORDING_STOP_READY_FOR_USE;
        
        static {
            $VALUES = new MeasurementKey[] { MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE, MeasurementKey.LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE, MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE, MeasurementKey.LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE, MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE, MeasurementKey.LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE, MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE, MeasurementKey.LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE, MeasurementKey.VIDEO_RECORDING_STOP_READY_FOR_USE, MeasurementKey.CLOSE_INITIAL_RESPONSE, MeasurementKey.CLOSE_READY_FOR_USE, MeasurementKey.SHOT_TO_SHOT_DELAY };
        }
    }
    
    public enum ModeChangeMethod
    {
        private static final ModeChangeMethod[] $VALUES;
        
        ICON_SWIPE, 
        ICON_TOUCH, 
        MODE_SELECTOR, 
        MRU_SHORTCUT, 
        SWIPE;
        
        static {
            $VALUES = new ModeChangeMethod[] { ModeChangeMethod.SWIPE, ModeChangeMethod.ICON_SWIPE, ModeChangeMethod.ICON_TOUCH, ModeChangeMethod.MODE_SELECTOR, ModeChangeMethod.MRU_SHORTCUT };
        }
    }
    
    private static final class ObjectTrackingValues
    {
        private String mTarget;
        
        private ObjectTrackingValues() {
            this.mTarget = "OFF";
        }
        
        public ShootingLabel.Parameter getParameter(final String s) {
            if ("OFF".equals(s)) {
                return ShootingLabel.getObjectTrackingParameter("OFF_OFF");
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("_");
            sb.append(this.mTarget);
            return ShootingLabel.getObjectTrackingParameter(sb.toString());
        }
        
        public void setObjectTrackingTarget(final boolean b) {
            if (b) {
                this.mTarget = "ON";
            }
            else {
                this.mTarget = "OFF";
            }
        }
    }
    
    private class PerformanceData
    {
        private boolean mIsValid;
        private final MeasurementKey mKey;
        private long mStartInMillis;
        private long mStopInMillis;
        final LocalResearchUtil this$0;
        
        public PerformanceData(final LocalResearchUtil this$0, final MeasurementKey mKey) {
            this.this$0 = this$0;
            this.mStartInMillis = 0L;
            this.mStopInMillis = 0L;
            this.mIsValid = false;
            this.mKey = mKey;
        }
        
        private boolean isValid() {
            return this.mStartInMillis != 0L && this.mIsValid;
        }
        
        public void clear() {
            this.mStartInMillis = 0L;
            this.mStopInMillis = 0L;
            this.mIsValid = false;
        }
        
        public void setInvalid() {
            this.mIsValid = false;
        }
        
        public void setValid() {
            this.mIsValid = true;
        }
        
        public void start() {
            this.mStartInMillis = SystemClock.uptimeMillis();
        }
        
        public void stop() {
            this.mStopInMillis = SystemClock.uptimeMillis();
            switch (LocalResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[this.mKey.ordinal()]) {
                case 9:
                case 10:
                case 11:
                case 12: {
                    ResearchUtil.getInstance().sendPerformanceData(this.mKey.toString(), this.mStopInMillis - this.mStartInMillis, this.this$0.isHeated());
                    if (CamLog.DEBUG) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(this.mKey.toString());
                        sb.append(": ");
                        sb.append(this.mStopInMillis - this.mStartInMillis);
                        sb.append(", isHeated: ");
                        sb.append(this.this$0.isHeated());
                        CamLog.d(sb.toString());
                        break;
                    }
                    break;
                }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8: {
                    ResearchUtil.getInstance().sendPerformanceData(this.mKey.toString(), this.mStopInMillis - this.mStartInMillis, this.this$0.isHeated(), this.this$0.getBatteryLevel());
                    if (CamLog.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(this.mKey.toString());
                        sb2.append(": ");
                        sb2.append(this.mStopInMillis - this.mStartInMillis);
                        sb2.append(", isHeated: ");
                        sb2.append(this.this$0.isHeated());
                        sb2.append(", BatteryLevel: ");
                        sb2.append(this.this$0.getBatteryLevel());
                        CamLog.d(sb2.toString());
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private static class SemiAutoSettingValues
    {
        private BasisAndChange<Integer> mAmberBlue;
        private BasisAndChange<Integer> mBrightness;
        
        public SemiAutoSettingValues() {
            this.mAmberBlue = (BasisAndChange<Integer>)new BasisAndChange(0, 0);
            this.mBrightness = (BasisAndChange<Integer>)new BasisAndChange(0, 0);
        }
        
        public void applyChange() {
            ((BasisAndChange<Object>)this.mAmberBlue).mBasis = ((BasisAndChange<Object>)this.mAmberBlue).mChange;
            ((BasisAndChange<Object>)this.mBrightness).mBasis = ((BasisAndChange<Object>)this.mBrightness).mChange;
        }
        
        public boolean hasChange() {
            return this.mAmberBlue.hasChange() || this.mBrightness.hasChange();
        }
        
        public boolean isEnabled() {
            return (int)((BasisAndChange<Object>)this.mAmberBlue).mChange != 0 || (int)((BasisAndChange<Object>)this.mBrightness).mChange != 0;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("AMB_");
            sb.append(((BasisAndChange<Object>)this.mAmberBlue).mChange);
            sb.append("_BR_");
            sb.append(((BasisAndChange<Object>)this.mBrightness).mChange);
            return sb.toString();
        }
        
        public void updateAmberBlue(final int i) {
            ((BasisAndChange<Object>)this.mAmberBlue).mChange = i;
        }
        
        public void updateBrightness(final int i) {
            ((BasisAndChange<Object>)this.mBrightness).mChange = i;
        }
    }
}
