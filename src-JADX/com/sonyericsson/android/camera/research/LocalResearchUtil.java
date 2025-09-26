package com.sonyericsson.android.camera.research;

import android.os.SystemClock;
import android.util.ArrayMap;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonyericsson.cameracommon.systemmonitor.BatteryChangedReceiver;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonymobile.cameracommon.research.parameters.Screen;
import com.sonymobile.cameracommon.research.parameters.ShootingLabel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LocalResearchUtil {
    public static final String TAG = "LocalResearchUtil";
    private static final LocalResearchUtil sInstance = new LocalResearchUtil();
    private BatteryChangedReceiver mBatteryChangedReceiver;
    private String mModeChangeMethod;
    private final Map<UserSettingKey, BasisAndChange<UserSettingValue>> mSettingsPhoto = new ArrayMap();
    private final Map<UserSettingKey, BasisAndChange<UserSettingValue>> mSettingsVideo = new ArrayMap();
    private SemiAutoSettingValues mSemiAutoSettingValuesPhoto = null;
    private SemiAutoSettingValues mSemiAutoSettingValuesVideo = null;
    private GestureShutterValues mGestureShutterValues = null;
    private ObjectTrackingValues mObjectTrackingValues = null;
    private boolean mPredictiveLaunchState = false;
    private final Map<UserSettingKey, UserSettingValue> mAllSettingsPhoto = new ArrayMap();
    private final Map<UserSettingKey, UserSettingValue> mAllSettingsVideo = new ArrayMap();
    private String mRecognizedScene = CameraParameterConverter.SceneMode.AUTO.toString();
    private Map<MeasurementKey, PerformanceData> mPerformanceDataMap = new ArrayMap();
    private boolean mIsHeated = false;
    private long mWizardStartTime = 0;
    private TutorialController.TutorialType mTutorialType = null;
    private boolean mIsReadMore = false;
    private int mCurrentPageIndex = -1;

    public enum MeasurementKey {
        LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE,
        LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE,
        LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE,
        LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE,
        LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE,
        LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE,
        LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE,
        LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE,
        VIDEO_RECORDING_STOP_READY_FOR_USE,
        CLOSE_INITIAL_RESPONSE,
        CLOSE_READY_FOR_USE,
        SHOT_TO_SHOT_DELAY
    }

    public enum ModeChangeMethod {
        SWIPE,
        ICON_SWIPE,
        ICON_TOUCH,
        MODE_SELECTOR,
        MRU_SHORTCUT
    }

    private static class BasisAndChange<T> {
        private T mBasis;
        private T mChange;

        public BasisAndChange(T t, T t2) {
            this.mBasis = null;
            this.mChange = null;
            this.mBasis = t;
            this.mChange = t2;
        }

        boolean hasChange() {
            return this.mBasis != this.mChange;
        }
    }

    private LocalResearchUtil() {
    }

    public static LocalResearchUtil getInstance() {
        return sInstance;
    }

    public void clearSettings() {
        this.mSettingsPhoto.clear();
        this.mSettingsVideo.clear();
    }

    public void clearAllSettings() {
        Map<UserSettingKey, UserSettingValue> commonSettings = getCommonSettings(this.mAllSettingsPhoto);
        this.mAllSettingsPhoto.clear();
        this.mAllSettingsVideo.clear();
        this.mAllSettingsPhoto.putAll(commonSettings);
        this.mAllSettingsVideo.putAll(commonSettings);
    }

    private Map<UserSettingKey, UserSettingValue> getCommonSettings(Map<UserSettingKey, UserSettingValue> map) {
        ArrayMap arrayMap = new ArrayMap();
        for (Map.Entry<UserSettingKey, UserSettingValue> entry : map.entrySet()) {
            switch (entry.getKey()) {
                case GEO_TAG:
                case TOUCH_CAPTURE:
                case VOLUME_KEY:
                case SHUTTER_SOUND:
                case DESTINATION_TO_SAVE:
                case FAST_CAPTURE:
                case GRID_LINE:
                case AUTO_REVIEW:
                case PREDICTIVE_LAUNCH:
                case SIDE_SENSE:
                    arrayMap.put(entry.getKey(), map.get(entry.getKey()));
                    break;
            }
        }
        return arrayMap;
    }

    private Screen getScreen(CapturingMode capturingMode) {
        switch (capturingMode) {
            case SCENE_RECOGNITION:
                return Screen.SUPERIOR_AUTO_MAIN;
            case NORMAL:
                return Screen.MANUAL_MAIN;
            case SUPERIOR_FRONT:
                return Screen.SUPERIOR_AUTO_FRONT;
            case FRONT_PHOTO:
                return Screen.MANUAL_FRONT;
            case VIDEO:
                return Screen.VIDEO_MAIN;
            case FRONT_VIDEO:
                return Screen.VIDEO_FRONT;
            case SLOW_MOTION:
                return Screen.SLOW_MOTION;
            default:
                if (CamLog.VERBOSE) {
                    CamLog.d("getScreen() : Not supported : " + capturingMode);
                }
                return null;
        }
    }

    public void sendView(LaunchCondition.LaunchTrigger launchTrigger, CapturingMode capturingMode) {
        ResearchUtil.getInstance().sendView(launchTrigger, getScreen(capturingMode));
    }

    public void setLaunchBy(LaunchCondition.LaunchTrigger launchTrigger) {
        ResearchUtil.getInstance().setLaunchBy(launchTrigger);
    }

    public void setView(CapturingMode capturingMode) {
        ResearchUtil.getInstance().setView(getScreen(capturingMode));
    }

    public void setSettingsValue(UserSettingValue userSettingValue, UserSettingValue userSettingValue2, CapturingMode capturingMode) {
        UserSettingKey key = userSettingValue2.getKey();
        if (userSettingValue == null) {
            userSettingValue = getCurrentSetting(key);
        }
        BasisAndChange<UserSettingValue> basisAndChange = new BasisAndChange<>(userSettingValue, userSettingValue2);
        if (!basisAndChange.hasChange()) {
            if (CamLog.VERBOSE) {
                CamLog.d("setSettingsValue() : Not changed.");
                return;
            }
            return;
        }
        switch (key) {
            case GEO_TAG:
            case TOUCH_CAPTURE:
            case VOLUME_KEY:
            case SHUTTER_SOUND:
            case DESTINATION_TO_SAVE:
            case FAST_CAPTURE:
            case GRID_LINE:
            case AUTO_REVIEW:
            case PREDICTIVE_LAUNCH:
            case SIDE_SENSE:
            case DISTORTION_CORRECTION:
            case HELP_GUIDE:
            case RESET_SETTINGS:
                break;
            case FLASH:
            case ASPECT_RATIO:
            case RESOLUTION:
            case SELF_TIMER:
            case SHUTTER_TRIGGER:
            case HDR:
            case ISO:
            case SOFT_SKIN:
            case EV:
            case WHITE_BALANCE:
            case METERING:
            case SHUTTER_SPEED:
            case FOCUS_RANGE:
            case CAMERA_KEY:
            case DISPLAY_FLASH:
            case TOUCH_INTENTION:
            case FUSION_MODE:
            case PREDICTIVE_CAPTURE:
                setSettingsPhotoVideo(basisAndChange, Event.Category.SETTINGS_PHOTO);
                break;
            case PHOTO_LIGHT:
            case VIDEO_SIZE:
            case VIDEO_SHUTTER_TRIGGER:
            case VIDEO_STABILIZER:
            case VIDEO_CODEC:
            case VIDEO_HDR:
            case SLOW_MOTION:
                setSettingsPhotoVideo(basisAndChange, Event.Category.SETTINGS_VIDEO);
                break;
            case FOCUS_MODE:
            case OBJECT_TRACKING:
                switch (capturingMode) {
                    case SCENE_RECOGNITION:
                    case NORMAL:
                    case SUPERIOR_FRONT:
                    case FRONT_PHOTO:
                        setSettingsPhotoVideo(basisAndChange, Event.Category.SETTINGS_PHOTO);
                        break;
                    case VIDEO:
                    case FRONT_VIDEO:
                    case SLOW_MOTION:
                        setSettingsPhotoVideo(basisAndChange, Event.Category.SETTINGS_VIDEO);
                        break;
                }
            case FRONT_ANGLE:
                int i = AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()];
                if (i != 6) {
                    switch (i) {
                        case 3:
                        case 4:
                            setSettingsPhotoVideo(basisAndChange, Event.Category.SETTINGS_PHOTO);
                            break;
                    }
                } else {
                    setSettingsPhotoVideo(basisAndChange, Event.Category.SETTINGS_VIDEO);
                    break;
                }
                break;
            default:
                if (CamLog.VERBOSE) {
                    CamLog.d("setSettingsValue() : Not supported : " + key);
                    return;
                }
                return;
        }
        ResearchUtil.getInstance().sendEventChangedSetting(key.toString(), ((UserSettingValue) ((BasisAndChange) basisAndChange).mBasis).toString(), ((UserSettingValue) ((BasisAndChange) basisAndChange).mChange).toString());
        setAllSettingsValue((UserSettingValue) ((BasisAndChange) basisAndChange).mChange, capturingMode);
    }

    private Map<UserSettingKey, BasisAndChange<UserSettingValue>> getSettingsMap(Event.Category category) {
        switch (category) {
            case SETTINGS_PHOTO:
                return this.mSettingsPhoto;
            case SETTINGS_VIDEO:
                return this.mSettingsVideo;
            default:
                return null;
        }
    }

    private void setSettingsPhotoVideo(BasisAndChange<UserSettingValue> basisAndChange, Event.Category category) {
        UserSettingKey key = ((UserSettingValue) ((BasisAndChange) basisAndChange).mChange).getKey();
        if (CamLog.VERBOSE) {
            CamLog.d("setSettingsPhotoVideo() : Category = " + category + ", Key = " + key);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("newValues    : Basis = " + ((BasisAndChange) basisAndChange).mBasis + ", Change = " + ((BasisAndChange) basisAndChange).mChange);
        }
        Map<UserSettingKey, BasisAndChange<UserSettingValue>> settingsMap = getSettingsMap(category);
        if (settingsMap == null) {
            return;
        }
        if (settingsMap.containsKey(key)) {
            BasisAndChange<UserSettingValue> basisAndChange2 = settingsMap.get(key);
            if (!((UserSettingValue) ((BasisAndChange) basisAndChange2).mBasis).equals(((BasisAndChange) basisAndChange).mChange)) {
                ((BasisAndChange) basisAndChange2).mChange = ((BasisAndChange) basisAndChange).mChange;
                settingsMap.put(key, basisAndChange2);
                return;
            } else {
                settingsMap.remove(key);
                return;
            }
        }
        settingsMap.put(key, basisAndChange);
    }

    private void sendEventSettings(Event.Category category) {
        Map<UserSettingKey, BasisAndChange<UserSettingValue>> settingsMap = getSettingsMap(category);
        if (settingsMap == null || settingsMap.isEmpty()) {
            return;
        }
        for (Map.Entry<UserSettingKey, BasisAndChange<UserSettingValue>> entry : settingsMap.entrySet()) {
            UserSettingKey key = entry.getKey();
            BasisAndChange<UserSettingValue> value = entry.getValue();
            if (((BasisAndChange) value).mChange != null) {
                ResearchUtil.getInstance().sendEvent(category, key.toString(), ((UserSettingValue) ((BasisAndChange) value).mChange).toString());
            }
        }
        settingsMap.clear();
    }

    public void sendEventSettings() {
        sendEventSettings(Event.Category.SETTINGS_PHOTO);
        sendEventSettings(Event.Category.SETTINGS_VIDEO);
    }

    public void sendEventInternalModeChange(CapturingMode capturingMode, CapturingMode capturingMode2) {
        ResearchUtil.getInstance().sendEventInternalModeChange(getModeName(capturingMode), getModeName(capturingMode2), this.mModeChangeMethod);
    }

    public void sendEventInternalModeChange(CapturingMode capturingMode, ModeSelectorInternalMode modeSelectorInternalMode) {
        ResearchUtil.getInstance().sendEventInternalModeChange(getModeName(capturingMode), getModeName(modeSelectorInternalMode), this.mModeChangeMethod);
    }

    public void sendEventAddonModeChange(Event.Category category, String str, String str2) {
        ResearchUtil.getInstance().sendEventAddonModeChange(category, str, str2, this.mModeChangeMethod);
    }

    public String getModeName(CapturingMode capturingMode) {
        switch (capturingMode) {
            case SCENE_RECOGNITION:
                return "SUPERIOR_AUTO_MAIN";
            case NORMAL:
                return "MUNAUL_MAIN";
            case SUPERIOR_FRONT:
                return "SUPERIOR_AUTO_FRONT";
            case FRONT_PHOTO:
                return "MUNAUL_FRONT";
            case VIDEO:
                return "VIDEO_MAIN";
            case FRONT_VIDEO:
                return "VIDEO_FRONT";
            case SLOW_MOTION:
                return "SLOW_MOTION";
            default:
                return "";
        }
    }

    private String getModeName(ModeSelectorInternalMode modeSelectorInternalMode) {
        return AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[modeSelectorInternalMode.ordinal()] != 1 ? "" : "PORTRAIT_SELFIE";
    }

    public void setModeChangeMethod(ModeChangeMethod modeChangeMethod) {
        this.mModeChangeMethod = modeChangeMethod.toString();
    }

    private static class SemiAutoSettingValues {
        private BasisAndChange<Integer> mAmberBlue = new BasisAndChange<>(0, 0);
        private BasisAndChange<Integer> mBrightness = new BasisAndChange<>(0, 0);

        public void updateAmberBlue(int i) {
            ((BasisAndChange) this.mAmberBlue).mChange = Integer.valueOf(i);
        }

        public void updateBrightness(int i) {
            ((BasisAndChange) this.mBrightness).mChange = Integer.valueOf(i);
        }

        public boolean hasChange() {
            return this.mAmberBlue.hasChange() || this.mBrightness.hasChange();
        }

        public void applyChange() {
            ((BasisAndChange) this.mAmberBlue).mBasis = ((BasisAndChange) this.mAmberBlue).mChange;
            ((BasisAndChange) this.mBrightness).mBasis = ((BasisAndChange) this.mBrightness).mChange;
        }

        public String toString() {
            return "AMB_" + ((BasisAndChange) this.mAmberBlue).mChange + "_BR_" + ((BasisAndChange) this.mBrightness).mChange;
        }

        public boolean isEnabled() {
            return (((Integer) ((BasisAndChange) this.mAmberBlue).mChange).intValue() == 0 && ((Integer) ((BasisAndChange) this.mBrightness).mChange).intValue() == 0) ? false : true;
        }
    }

    private SemiAutoSettingValues getSemiAutoSettingValues(Event.Category category) {
        switch (category) {
            case SETTINGS_PHOTO:
            case ALL_SETTINGS_PHOTO:
                if (this.mSemiAutoSettingValuesPhoto == null) {
                    this.mSemiAutoSettingValuesPhoto = new SemiAutoSettingValues();
                }
                return this.mSemiAutoSettingValuesPhoto;
            case SETTINGS_VIDEO:
            case ALL_SETTINGS_VIDEO:
                if (this.mSemiAutoSettingValuesVideo == null) {
                    this.mSemiAutoSettingValuesVideo = new SemiAutoSettingValues();
                }
                return this.mSemiAutoSettingValuesVideo;
            default:
                return null;
        }
    }

    public void setSemiAutoSettingAmberBlueValue(int i) {
        getSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO).updateAmberBlue(i);
        getSemiAutoSettingValues(Event.Category.SETTINGS_VIDEO).updateAmberBlue(i);
    }

    public void setSemiAutoSettingBrightnessValue(int i) {
        getSemiAutoSettingValues(Event.Category.SETTINGS_PHOTO).updateBrightness(i);
        getSemiAutoSettingValues(Event.Category.SETTINGS_VIDEO).updateBrightness(i);
    }

    public void clearSemiAutoSettingValues() {
        this.mSemiAutoSettingValuesPhoto = null;
        this.mSemiAutoSettingValuesVideo = null;
    }

    public void sendSemiAutoSettingValues(Event.Category category) {
        SemiAutoSettingValues semiAutoSettingValues = getSemiAutoSettingValues(category);
        if (semiAutoSettingValues.hasChange()) {
            semiAutoSettingValues.applyChange();
            ResearchUtil.getInstance().sendEvent(category, UserSettingKey.SEMI_AUTO.toString(), semiAutoSettingValues.toString());
        }
    }

    private static final class ObjectTrackingValues {
        private String mTarget;

        private ObjectTrackingValues() {
            this.mTarget = "OFF";
        }

        /* synthetic */ ObjectTrackingValues(AnonymousClass1 anonymousClass1) {
            this();
        }

        public void setObjectTrackingTarget(boolean z) {
            if (z) {
                this.mTarget = "ON";
            } else {
                this.mTarget = "OFF";
            }
        }

        public ShootingLabel.Parameter getParameter(String str) {
            if ("OFF".equals(str)) {
                return ShootingLabel.getObjectTrackingParameter("OFF_OFF");
            }
            return ShootingLabel.getObjectTrackingParameter(str + "_" + this.mTarget);
        }
    }

    private ObjectTrackingValues getObjectTrackingValues() {
        if (this.mObjectTrackingValues == null) {
            this.mObjectTrackingValues = new ObjectTrackingValues(null);
        }
        return this.mObjectTrackingValues;
    }

    public void setObjectTrackingTarget(boolean z) {
        getObjectTrackingValues().setObjectTrackingTarget(z);
    }

    private static final class GestureShutterValues {
        private int mHandSignLostNum;

        private GestureShutterValues() {
            this.mHandSignLostNum = -1;
        }

        /* synthetic */ GestureShutterValues(AnonymousClass1 anonymousClass1) {
            this();
        }

        public void startHandSignLostNumCounting() {
            this.mHandSignLostNum = 0;
        }

        public void resetHandSignLostNum() {
            this.mHandSignLostNum = -1;
        }

        public void countUpHandSignLostNum() {
            if (this.mHandSignLostNum == -1 && CamLog.VERBOSE) {
                CamLog.d("Counting up hand signs lost number from -1.");
            }
            this.mHandSignLostNum++;
        }

        public ShootingLabel.Parameter getParameter() {
            return ShootingLabel.getHandSignLostParameter(this.mHandSignLostNum);
        }
    }

    private GestureShutterValues getGestureShutterValues() {
        if (this.mGestureShutterValues == null) {
            this.mGestureShutterValues = new GestureShutterValues(null);
        }
        return this.mGestureShutterValues;
    }

    public void setPredictiveLaunchState(boolean z) {
        this.mPredictiveLaunchState = z;
    }

    public void resetHandSignLostNum() {
        getGestureShutterValues().resetHandSignLostNum();
    }

    public void startHandSignLostNumCounting() {
        getGestureShutterValues().startHandSignLostNumCounting();
    }

    public void countUpHandSignLostNum() {
        getGestureShutterValues().countUpHandSignLostNum();
    }

    public void clearTemporarySettingValues() {
        clearSemiAutoSettingValues();
        clearRecognizedScene();
        ResearchUtil.getInstance().clearTemporarySettingValues();
    }

    public void setAllSettingsValue(UserSettingValue userSettingValue, CapturingMode capturingMode) {
        UserSettingKey key = userSettingValue.getKey();
        switch (key) {
            case GEO_TAG:
            case TOUCH_CAPTURE:
            case VOLUME_KEY:
            case SHUTTER_SOUND:
            case DESTINATION_TO_SAVE:
            case FAST_CAPTURE:
            case GRID_LINE:
            case AUTO_REVIEW:
            case PREDICTIVE_LAUNCH:
            case SIDE_SENSE:
            case DISTORTION_CORRECTION:
            case HELP_GUIDE:
            case RESET_SETTINGS:
                setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_PHOTO);
                setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_VIDEO);
                break;
            case FLASH:
            case ASPECT_RATIO:
            case RESOLUTION:
            case SELF_TIMER:
            case SHUTTER_TRIGGER:
            case HDR:
            case ISO:
            case SOFT_SKIN:
            case EV:
            case WHITE_BALANCE:
            case METERING:
            case SHUTTER_SPEED:
            case FOCUS_RANGE:
            case CAMERA_KEY:
            case DISPLAY_FLASH:
            case TOUCH_INTENTION:
            case FUSION_MODE:
            case PREDICTIVE_CAPTURE:
                setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_PHOTO);
                break;
            case PHOTO_LIGHT:
            case VIDEO_SIZE:
            case VIDEO_SHUTTER_TRIGGER:
            case VIDEO_STABILIZER:
            case VIDEO_CODEC:
            case VIDEO_HDR:
            case SLOW_MOTION:
                setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_VIDEO);
                break;
            case FOCUS_MODE:
            case OBJECT_TRACKING:
                switch (capturingMode) {
                    case SCENE_RECOGNITION:
                    case NORMAL:
                    case SUPERIOR_FRONT:
                    case FRONT_PHOTO:
                        setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_PHOTO);
                        break;
                    case VIDEO:
                    case FRONT_VIDEO:
                    case SLOW_MOTION:
                        setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_VIDEO);
                        break;
                }
            case FRONT_ANGLE:
                int i = AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()];
                if (i != 6) {
                    switch (i) {
                        case 3:
                        case 4:
                            setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_PHOTO);
                            break;
                    }
                } else {
                    setAllSettingsPhotoVideo(userSettingValue, Event.Category.ALL_SETTINGS_VIDEO);
                    break;
                }
                break;
            default:
                if (CamLog.VERBOSE) {
                    CamLog.d("setAllSettingsValue() : Not supported : " + key);
                    break;
                }
                break;
        }
    }

    private void setAllSettingsPhotoVideo(UserSettingValue userSettingValue, Event.Category category) {
        UserSettingKey key = userSettingValue.getKey();
        Map<UserSettingKey, UserSettingValue> allSettingsMap = getAllSettingsMap(category);
        if (allSettingsMap == null) {
            return;
        }
        allSettingsMap.put(key, userSettingValue);
    }

    private Map<UserSettingKey, UserSettingValue> getAllSettingsMap(Event.Category category) {
        switch (category) {
            case ALL_SETTINGS_PHOTO:
                return this.mAllSettingsPhoto;
            case ALL_SETTINGS_VIDEO:
                return this.mAllSettingsVideo;
            default:
                return null;
        }
    }

    private UserSettingValue getCurrentSetting(UserSettingKey userSettingKey) {
        Map<UserSettingKey, UserSettingValue> allSettingsMap;
        Iterator it = Arrays.asList(Event.Category.ALL_SETTINGS_PHOTO, Event.Category.ALL_SETTINGS_VIDEO).iterator();
        UserSettingValue userSettingValue = null;
        while (it.hasNext() && ((allSettingsMap = getAllSettingsMap((Event.Category) it.next())) == null || (userSettingValue = allSettingsMap.get(userSettingKey)) == null)) {
        }
        return userSettingValue;
    }

    public void setRecognizedScene(String str) {
        this.mRecognizedScene = str;
    }

    public void clearRecognizedScene() {
        this.mRecognizedScene = CameraParameterConverter.SceneMode.AUTO.toString();
    }

    public void setUserOperation(Event.UserOperation userOperation, CapturingMode capturingMode) {
        String string;
        ArrayList arrayList = new ArrayList();
        Event.Category category = Event.Category.ALL_SETTINGS_PHOTO;
        arrayList.add(ShootingLabel.getRecognizedSceneParameter(this.mRecognizedScene));
        UserSettingValue userSettingValue = getAllSettingsMap(category).get(UserSettingKey.FLASH);
        if (userSettingValue == null) {
            userSettingValue = Flash.OFF;
        }
        arrayList.add(ShootingLabel.getFlashParameter(userSettingValue.toString()));
        UserSettingValue userSettingValue2 = getAllSettingsMap(category).get(UserSettingKey.SELF_TIMER);
        if (userSettingValue2 == null) {
            userSettingValue2 = SelfTimer.OFF;
        }
        arrayList.add(ShootingLabel.getSelfTimerParameter(userSettingValue2.toString()));
        if (capturingMode.isFront() && PlatformCapability.isSuperWideSupported(CameraInfo.CameraId.FRONT)) {
            UserSettingValue userSettingValue3 = getAllSettingsMap(category).get(UserSettingKey.FRONT_ANGLE);
            if (userSettingValue3 == null) {
                userSettingValue3 = FrontAngle.DEFAULT;
            }
            string = userSettingValue3.toString();
        } else {
            string = "INVALID";
        }
        arrayList.add(ShootingLabel.getFrontAngleParameter(string));
        SemiAutoSettingValues semiAutoSettingValues = getSemiAutoSettingValues(category);
        if (semiAutoSettingValues != null) {
            arrayList.add(ShootingLabel.getSemiAutoParameter(!semiAutoSettingValues.isEnabled() ? 1 : 0));
        }
        if (this.mGestureShutterValues != null) {
            arrayList.add(this.mGestureShutterValues.getParameter());
        }
        UserSettingValue userSettingValue4 = getAllSettingsMap(category).get(UserSettingKey.OBJECT_TRACKING);
        if (userSettingValue4 == null) {
            userSettingValue4 = ObjectTracking.OFF;
        }
        arrayList.add(getObjectTrackingValues().getParameter(userSettingValue4.toString()));
        arrayList.add(ShootingLabel.getPredictiveLaunchParameter(this.mPredictiveLaunchState ? ShootingLabel.PREDICTIVE_LAUNCH_ON : ShootingLabel.PREDICTIVE_LAUNCH_OFF));
        ResearchUtil.getInstance().setUserOperation(userOperation, arrayList, getAllSettingsMapString(Event.Category.ALL_SETTINGS_PHOTO));
    }

    private class PerformanceData {
        private final MeasurementKey mKey;
        private long mStartInMillis = 0;
        private long mStopInMillis = 0;
        private boolean mIsValid = false;

        public PerformanceData(MeasurementKey measurementKey) {
            this.mKey = measurementKey;
        }

        public void start() {
            this.mStartInMillis = SystemClock.uptimeMillis();
        }

        public void stop() {
            this.mStopInMillis = SystemClock.uptimeMillis();
            switch (this.mKey) {
                case LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE:
                case LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE:
                case LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE:
                case LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE:
                case LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE:
                case LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE:
                case LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE:
                case LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE:
                    ResearchUtil.getInstance().sendPerformanceData(this.mKey.toString(), this.mStopInMillis - this.mStartInMillis, LocalResearchUtil.this.isHeated(), LocalResearchUtil.this.getBatteryLevel());
                    if (CamLog.DEBUG) {
                        CamLog.d(this.mKey.toString() + ": " + (this.mStopInMillis - this.mStartInMillis) + ", isHeated: " + LocalResearchUtil.this.isHeated() + ", BatteryLevel: " + LocalResearchUtil.this.getBatteryLevel());
                        break;
                    }
                    break;
                case VIDEO_RECORDING_STOP_READY_FOR_USE:
                case CLOSE_INITIAL_RESPONSE:
                case CLOSE_READY_FOR_USE:
                case SHOT_TO_SHOT_DELAY:
                    ResearchUtil.getInstance().sendPerformanceData(this.mKey.toString(), this.mStopInMillis - this.mStartInMillis, LocalResearchUtil.this.isHeated());
                    if (CamLog.DEBUG) {
                        CamLog.d(this.mKey.toString() + ": " + (this.mStopInMillis - this.mStartInMillis) + ", isHeated: " + LocalResearchUtil.this.isHeated());
                        break;
                    }
                    break;
            }
        }

        public void setValid() {
            this.mIsValid = true;
        }

        public void setInvalid() {
            this.mIsValid = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isValid() {
            return this.mStartInMillis != 0 && this.mIsValid;
        }

        public void clear() {
            this.mStartInMillis = 0L;
            this.mStopInMillis = 0L;
            this.mIsValid = false;
        }
    }

    public Map<String, String> getAllSettingsMapString(Event.Category category) {
        ArrayMap arrayMap = new ArrayMap();
        Map<UserSettingKey, UserSettingValue> allSettingsMap = getAllSettingsMap(category);
        if (allSettingsMap != null) {
            for (Map.Entry<UserSettingKey, UserSettingValue> entry : allSettingsMap.entrySet()) {
                arrayMap.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return arrayMap;
    }

    public void startMeasurement(MeasurementKey measurementKey) {
        getPerformanceData(measurementKey).start();
    }

    public void stopMeasurement(MeasurementKey measurementKey) {
        PerformanceData performanceData = getPerformanceData(measurementKey);
        if (performanceData.isValid()) {
            performanceData.stop();
        }
        performanceData.clear();
    }

    public void setMeasurementValid(MeasurementKey measurementKey) {
        getPerformanceData(measurementKey).setValid();
    }

    public void setMeasurementInvalid(MeasurementKey measurementKey) {
        getPerformanceData(measurementKey).setInvalid();
    }

    public boolean isMeasurementValid(MeasurementKey measurementKey) {
        return getPerformanceData(measurementKey).isValid();
    }

    public void setMeasurementThermal(boolean z) {
        this.mIsHeated = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isHeated() {
        return this.mIsHeated;
    }

    private PerformanceData getPerformanceData(MeasurementKey measurementKey) {
        PerformanceData performanceData = this.mPerformanceDataMap.get(measurementKey);
        if (performanceData != null) {
            return performanceData;
        }
        PerformanceData performanceData2 = new PerformanceData(measurementKey);
        this.mPerformanceDataMap.put(measurementKey, performanceData2);
        return performanceData2;
    }

    public void setBatteryChangedReceiver(BatteryChangedReceiver batteryChangedReceiver) {
        this.mBatteryChangedReceiver = batteryChangedReceiver;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBatteryLevel() {
        return String.valueOf(this.mBatteryChangedReceiver != null ? this.mBatteryChangedReceiver.getBatteryLevel() : -1);
    }

    public void sendRecordingEvent(Event.UserOperation userOperation, Event.StopOperation stopOperation, int i, boolean z) {
        ResearchUtil.getInstance().sendRecordingEvent(userOperation, stopOperation, i, z, getAllSettingsMapString(Event.Category.ALL_SETTINGS_VIDEO));
    }

    public void setSettingsValue(UserSettings userSettings, CapturingMode capturingMode) {
        for (UserSettingKey userSettingKey : UserSettingKey.values()) {
            UserSettingValue userSettingValue = userSettings.get(userSettingKey);
            if (userSettingValue != null) {
                setAllSettingsValue(userSettingValue, capturingMode);
            }
        }
    }

    public void initSetupwizard(boolean z) {
        this.mIsReadMore = z;
    }

    public void startSetupWizard(TutorialController.TutorialType tutorialType, int i) {
        this.mWizardStartTime = System.currentTimeMillis();
        this.mCurrentPageIndex = i;
        this.mTutorialType = tutorialType;
    }

    public void closeSetupWizard() {
        this.mWizardStartTime = 0L;
        this.mCurrentPageIndex = -1;
        this.mTutorialType = null;
    }

    public void sendSetupWizardEvent(TutorialController.TutorialType tutorialType, int i, Event.WizardResult wizardResult) {
        Event.WizardPage wizardPage = Event.WizardPage.UNKNOWN;
        this.mCurrentPageIndex = i;
        this.mTutorialType = tutorialType;
        if (this.mTutorialType == null) {
            CamLog.w("TutorialType is null");
            return;
        }
        switch (this.mTutorialType) {
            case SAVE_LOCATION:
                wizardPage = Event.WizardPage.LOCATION_WIZARD;
                break;
            case PREDICTIVE_LAUNCH:
                wizardPage = Event.WizardPage.PREDICTIVE_LAUNCH_WIZARD;
                break;
            case SIDE_SENSE:
                wizardPage = Event.WizardPage.SIDE_SENSING_WIZARD;
                break;
            case DUAL_CAMERA:
                wizardPage = Event.WizardPage.SUPERIOR_AUTO_FUSION_WIZARD;
                break;
            case EYE_GUIDE:
                wizardPage = Event.WizardPage.EYE_POSITION_WIZARD;
                break;
            case HAND_SHUTTER:
                wizardPage = Event.WizardPage.HAND_SHUTTER_WIZARD;
                break;
            case SUPER_SLOW_MOTION_MORE_OPTIONS:
                switch (i) {
                    case 0:
                        wizardPage = Event.WizardPage.SUPER_SLOWMOTION_WIZARD1;
                        break;
                    case 1:
                        wizardPage = Event.WizardPage.SUPER_SLOWMOTION_WIZARD2;
                        break;
                    case 2:
                        wizardPage = Event.WizardPage.SUPER_SLOWMOTION_WIZARD3;
                        break;
                    case 3:
                        wizardPage = Event.WizardPage.SUPER_SLOWMOTION_WIZARD4;
                        break;
                }
            case SUPER_SLOW_MOTION:
                switch (i) {
                    case 0:
                        wizardPage = Event.WizardPage.READMORE_SUPER_SLOWMOTION_WIZARD1;
                        break;
                    case 1:
                        wizardPage = Event.WizardPage.READMORE_SUPER_SLOWMOTION_WIZARD2;
                        break;
                    case 2:
                        wizardPage = Event.WizardPage.READMORE_SUPER_SLOWMOTION_WIZARD3;
                        break;
                }
            case SUPER_SLOW_MOTION_SHOT:
                if (this.mIsReadMore) {
                    wizardPage = Event.WizardPage.READMORE_ONE_SHOT_WIZARD;
                    break;
                } else {
                    wizardPage = Event.WizardPage.ONE_SHOT_WIZARD;
                    break;
                }
            case STANDARD_SLOW_MOTION:
                switch (i) {
                    case 0:
                        if (this.mIsReadMore) {
                            wizardPage = Event.WizardPage.READMORE_SLOWMOTION_WIZARD1;
                            break;
                        } else {
                            wizardPage = Event.WizardPage.SLOWMOTION_WIZARD1;
                            break;
                        }
                    case 1:
                        if (this.mIsReadMore) {
                            wizardPage = Event.WizardPage.READMORE_SLOWMOTION_WIZARD2;
                            break;
                        } else {
                            wizardPage = Event.WizardPage.SLOWMOTION_WIZARD2;
                            break;
                        }
                }
            case MANUAL_FUSION:
                switch (i) {
                    case 0:
                        wizardPage = Event.WizardPage.MANUAL_FUSION_WIZARD1;
                        break;
                    case 1:
                        wizardPage = Event.WizardPage.MANUAL_FUSION_WIZARD2;
                        break;
                }
            case VIDEO_FUSION:
                switch (i) {
                    case 0:
                        wizardPage = Event.WizardPage.VIDEO_FUSION_WIZARD1;
                        break;
                    case 1:
                        wizardPage = Event.WizardPage.VIDEO_FUSION_WIZARD2;
                        break;
                }
        }
        if (this.mWizardStartTime > 0) {
            ResearchUtil.getInstance().sendSetupWizardEvent(wizardPage, wizardResult, System.currentTimeMillis() - this.mWizardStartTime);
            this.mWizardStartTime = System.currentTimeMillis();
        }
    }

    /* renamed from: com.sonyericsson.android.camera.research.LocalResearchUtil$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode;

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SAVE_LOCATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.PREDICTIVE_LAUNCH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SIDE_SENSE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.DUAL_CAMERA.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.EYE_GUIDE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.HAND_SHUTTER.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SUPER_SLOW_MOTION_MORE_OPTIONS.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SUPER_SLOW_MOTION.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SUPER_SLOW_MOTION_SHOT.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.STANDARD_SLOW_MOTION.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.MANUAL_FUSION.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.VIDEO_FUSION.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey = new int[MeasurementKey.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.LAUNCH_COLD_BOOT_FROM_HOME_READY_FOR_USE.ordinal()] = 1;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.LAUNCH_COLD_BOOT_FROM_LOCKSCREEN_READY_FOR_USE.ordinal()] = 2;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.LAUNCH_COLD_BOOT_FROM_CAMERAKEY_READY_FOR_USE.ordinal()] = 3;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE.ordinal()] = 4;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.LAUNCH_WARM_BOOT_FROM_HOME_READY_FOR_USE.ordinal()] = 5;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.LAUNCH_WARM_BOOT_FROM_LOCKSCREEN_READY_FOR_USE.ordinal()] = 6;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.LAUNCH_WARM_BOOT_FROM_CAMERAKEY_READY_FOR_USE.ordinal()] = 7;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE.ordinal()] = 8;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.VIDEO_RECORDING_STOP_READY_FOR_USE.ordinal()] = 9;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.CLOSE_INITIAL_RESPONSE.ordinal()] = 10;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.CLOSE_READY_FOR_USE.ordinal()] = 11;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$research$LocalResearchUtil$MeasurementKey[MeasurementKey.SHOT_TO_SHOT_DELAY.ordinal()] = 12;
            } catch (NoSuchFieldError unused24) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode = new int[ModeSelectorInternalMode.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[ModeSelectorInternalMode.PORTRAIT_SELFIE.ordinal()] = 1;
            } catch (NoSuchFieldError unused25) {
            }
            $SwitchMap$com$sonymobile$cameracommon$research$parameters$Event$Category = new int[Event.Category.values().length];
            try {
                $SwitchMap$com$sonymobile$cameracommon$research$parameters$Event$Category[Event.Category.SETTINGS_PHOTO.ordinal()] = 1;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$sonymobile$cameracommon$research$parameters$Event$Category[Event.Category.SETTINGS_VIDEO.ordinal()] = 2;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$com$sonymobile$cameracommon$research$parameters$Event$Category[Event.Category.ALL_SETTINGS_PHOTO.ordinal()] = 3;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$com$sonymobile$cameracommon$research$parameters$Event$Category[Event.Category.ALL_SETTINGS_VIDEO.ordinal()] = 4;
            } catch (NoSuchFieldError unused29) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode = new int[CapturingMode.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SCENE_RECOGNITION.ordinal()] = 1;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SUPERIOR_FRONT.ordinal()] = 3;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.FRONT_PHOTO.ordinal()] = 4;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.VIDEO.ordinal()] = 5;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.FRONT_VIDEO.ordinal()] = 6;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[CapturingMode.SLOW_MOTION.ordinal()] = 7;
            } catch (NoSuchFieldError unused36) {
            }
            $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey = new int[UserSettingKey.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.GEO_TAG.ordinal()] = 1;
            } catch (NoSuchFieldError unused37) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.TOUCH_CAPTURE.ordinal()] = 2;
            } catch (NoSuchFieldError unused38) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.VOLUME_KEY.ordinal()] = 3;
            } catch (NoSuchFieldError unused39) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.SHUTTER_SOUND.ordinal()] = 4;
            } catch (NoSuchFieldError unused40) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.DESTINATION_TO_SAVE.ordinal()] = 5;
            } catch (NoSuchFieldError unused41) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.FAST_CAPTURE.ordinal()] = 6;
            } catch (NoSuchFieldError unused42) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.GRID_LINE.ordinal()] = 7;
            } catch (NoSuchFieldError unused43) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.AUTO_REVIEW.ordinal()] = 8;
            } catch (NoSuchFieldError unused44) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.PREDICTIVE_LAUNCH.ordinal()] = 9;
            } catch (NoSuchFieldError unused45) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.SIDE_SENSE.ordinal()] = 10;
            } catch (NoSuchFieldError unused46) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.FLASH.ordinal()] = 11;
            } catch (NoSuchFieldError unused47) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.ASPECT_RATIO.ordinal()] = 12;
            } catch (NoSuchFieldError unused48) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.RESOLUTION.ordinal()] = 13;
            } catch (NoSuchFieldError unused49) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.SELF_TIMER.ordinal()] = 14;
            } catch (NoSuchFieldError unused50) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.SHUTTER_TRIGGER.ordinal()] = 15;
            } catch (NoSuchFieldError unused51) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.HDR.ordinal()] = 16;
            } catch (NoSuchFieldError unused52) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.ISO.ordinal()] = 17;
            } catch (NoSuchFieldError unused53) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.SOFT_SKIN.ordinal()] = 18;
            } catch (NoSuchFieldError unused54) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.EV.ordinal()] = 19;
            } catch (NoSuchFieldError unused55) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.WHITE_BALANCE.ordinal()] = 20;
            } catch (NoSuchFieldError unused56) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.METERING.ordinal()] = 21;
            } catch (NoSuchFieldError unused57) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.SHUTTER_SPEED.ordinal()] = 22;
            } catch (NoSuchFieldError unused58) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.FOCUS_RANGE.ordinal()] = 23;
            } catch (NoSuchFieldError unused59) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.CAMERA_KEY.ordinal()] = 24;
            } catch (NoSuchFieldError unused60) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.DISPLAY_FLASH.ordinal()] = 25;
            } catch (NoSuchFieldError unused61) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.TOUCH_INTENTION.ordinal()] = 26;
            } catch (NoSuchFieldError unused62) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.FUSION_MODE.ordinal()] = 27;
            } catch (NoSuchFieldError unused63) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.PREDICTIVE_CAPTURE.ordinal()] = 28;
            } catch (NoSuchFieldError unused64) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.PHOTO_LIGHT.ordinal()] = 29;
            } catch (NoSuchFieldError unused65) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.VIDEO_SIZE.ordinal()] = 30;
            } catch (NoSuchFieldError unused66) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.VIDEO_SHUTTER_TRIGGER.ordinal()] = 31;
            } catch (NoSuchFieldError unused67) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.VIDEO_STABILIZER.ordinal()] = 32;
            } catch (NoSuchFieldError unused68) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.VIDEO_CODEC.ordinal()] = 33;
            } catch (NoSuchFieldError unused69) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.VIDEO_HDR.ordinal()] = 34;
            } catch (NoSuchFieldError unused70) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.SLOW_MOTION.ordinal()] = 35;
            } catch (NoSuchFieldError unused71) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.FOCUS_MODE.ordinal()] = 36;
            } catch (NoSuchFieldError unused72) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.OBJECT_TRACKING.ordinal()] = 37;
            } catch (NoSuchFieldError unused73) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.FRONT_ANGLE.ordinal()] = 38;
            } catch (NoSuchFieldError unused74) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.DISTORTION_CORRECTION.ordinal()] = 39;
            } catch (NoSuchFieldError unused75) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.HELP_GUIDE.ordinal()] = 40;
            } catch (NoSuchFieldError unused76) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.RESET_SETTINGS.ordinal()] = 41;
            } catch (NoSuchFieldError unused77) {
            }
        }
    }

    public void sendSetupWizardEvent(Event.WizardResult wizardResult) {
        sendSetupWizardEvent(this.mTutorialType, this.mCurrentPageIndex, wizardResult);
    }
}
