package com.sonymobile.cameracommon.research;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonymobile.cameracommon.research.idd.IddUtil;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonymobile.cameracommon.research.parameters.Screen;
import com.sonymobile.cameracommon.research.parameters.ShootingLabel;
import com.sonymobile.getmore.api.ContributionContract;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ResearchUtil {
    private static final String DUAL_CAMERA_EFFECT_USAGE_DATA_CONTENT_URI = "content://com.sonymobile.addoncamera.dualcameraeffect.provider/UsageData";
    private static final String PORTRAIT_SELFIE_USAGE_DATA_CONTENT_URI = "content://com.sonymobile.addoncamera.beautyportrait.provider/UsageData";
    public static final String TAG = "ResearchUtil";
    private static final ResearchUtil sInstance = new ResearchUtil();
    private ExecutorService mBackWorker;
    private CurrentUserOperationHolder mUserOperationInfo = null;
    private PanoramaInfo mPanoramaInfo = null;
    private Context mContext = null;
    private boolean mIsViewEventSent = false;
    private boolean mIsFailedToOpenCamera = false;
    private int mCountRecordResume = 0;
    private int mCountRecordSnapshot = 0;
    private TemporarySettingValues mTemporarySettingValues = null;
    private AfDoneKeepingTimeHolder mAfDoneKeepingTimeHolder = null;

    static /* synthetic */ int access$2708(ResearchUtil researchUtil) {
        int i = researchUtil.mCountRecordResume;
        researchUtil.mCountRecordResume = i + 1;
        return i;
    }

    static /* synthetic */ int access$2808(ResearchUtil researchUtil) {
        int i = researchUtil.mCountRecordSnapshot;
        researchUtil.mCountRecordSnapshot = i + 1;
        return i;
    }

    private class CurrentUserOperationHolder {
        private Event.UserOperation mUserOperation;

        private CurrentUserOperationHolder() {
            this.mUserOperation = null;
        }

        public void setUserOperation(Event.UserOperation userOperation) {
            setUserOperation(userOperation, null, null);
        }

        public void setUserOperation(Event.UserOperation userOperation, List<ShootingLabel.Parameter> list, Map<String, String> map) {
            ShootingLabel.Parameter parameter;
            if (CamLog.VERBOSE) {
                CamLog.d("setUserOperation() : operation = " + userOperation);
            }
            if (userOperation.isShooting()) {
                Point point = new Point();
                ArrayMap arrayMap = new ArrayMap();
                if (ResearchUtil.this.mTemporarySettingValues != null) {
                    arrayMap.put("ManualBurst", ResearchUtil.this.mTemporarySettingValues.getManualBurstParameterForIDD());
                    if (ResearchUtil.this.mTemporarySettingValues.mCaptureTrigger == Event.CaptureTrigger.SIDE_SENSE) {
                        point = ResearchUtil.this.mTemporarySettingValues.mSideSensePosition;
                    }
                    for (ShootingLabel.Parameter parameter2 : ResearchUtil.this.mTemporarySettingValues.getParameterList()) {
                        arrayMap.put(parameter2.getClass().getSimpleName(), parameter2.toString());
                    }
                    if (ResearchUtil.this.mTemporarySettingValues.mAssistSelfTimer != -1) {
                        arrayMap.put("assist_self_timer", String.valueOf(ResearchUtil.this.mTemporarySettingValues.mAssistSelfTimer));
                        ResearchUtil.this.mTemporarySettingValues.clearAssistSelfTimer();
                    }
                }
                arrayMap.put("side_sense_position_x", String.valueOf(point.x));
                arrayMap.put("side_sense_position_y", String.valueOf(point.y));
                if (list != null) {
                    for (ShootingLabel.Parameter parameter3 : list) {
                        arrayMap.put(parameter3.getClass().getSimpleName(), parameter3.toString());
                    }
                }
                if (ResearchUtil.this.mAfDoneKeepingTimeHolder != null && (parameter = ResearchUtil.this.mAfDoneKeepingTimeHolder.getParameter()) != null) {
                    arrayMap.put(parameter.getClass().getSimpleName(), parameter.toString());
                }
                ResearchUtil researchUtil = ResearchUtil.this;
                Event.Category category = Event.Category.ALL_SETTINGS_PHOTO;
                if (map == null) {
                    map = new ArrayMap<>();
                }
                researchUtil.sendEventAllSettings(category, arrayMap, map);
            }
            if (this.mUserOperation == null || this.mUserOperation == Event.CaptureOperation.EMPTY) {
                this.mUserOperation = userOperation;
            }
            this.mUserOperation = userOperation.updateOperation(this.mUserOperation);
        }

        public void setViewerLaunched() {
            if (this.mUserOperation == null) {
                this.mUserOperation = Event.CaptureOperation.EMPTY;
            }
            setUserOperation(this.mUserOperation.getViewer());
        }

        public void clear() {
            if (CamLog.VERBOSE) {
                CamLog.d("UserOperationInfo#clear()");
            }
            this.mUserOperation = null;
        }
    }

    private static class PanoramaInfo {
        private int mSuccessNum;
        private int mTryNum;
        private Event.ViewerLaunched mViewerLaunched;

        private PanoramaInfo() {
            this.mTryNum = 0;
            this.mSuccessNum = 0;
            this.mViewerLaunched = Event.ViewerLaunched.NOT_LAUNCHED;
        }

        public void tryPanorama() {
            if (CamLog.VERBOSE) {
                CamLog.d("tryPanorama()");
            }
            this.mTryNum++;
        }

        public void succeedInPanorama() {
            if (CamLog.VERBOSE) {
                CamLog.d("succeedInPanorama()");
            }
            this.mSuccessNum++;
        }

        public void setViewerLaunched() {
            if (CamLog.VERBOSE) {
                CamLog.d("setViewerLaunched()");
            }
            this.mViewerLaunched = Event.ViewerLaunched.LAUNCHED;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendPanoramaInfo() {
            if (this.mTryNum > 0 || this.mViewerLaunched == Event.ViewerLaunched.LAUNCHED) {
                String strValueOf = String.valueOf(this.mTryNum);
                String strValueOf2 = String.valueOf(this.mSuccessNum);
                IddUtil.sendEvent(Event.Category.PANORAMA, strValueOf + "_try", strValueOf2 + "_success", this.mViewerLaunched.mValue);
            }
            clear();
        }

        public void clear() {
            if (CamLog.VERBOSE) {
                CamLog.d("PanoramaInfo#clear()");
            }
            this.mTryNum = 0;
            this.mSuccessNum = 0;
            this.mViewerLaunched = Event.ViewerLaunched.NOT_LAUNCHED;
        }
    }

    private ResearchUtil() {
        this.mBackWorker = null;
        if (this.mBackWorker == null) {
            this.mBackWorker = ThreadUtil.buildExecutor("R-Thread", 1);
        }
    }

    public static ResearchUtil getInstance() {
        return sInstance;
    }

    public void onCreate(Context context) {
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate()");
        }
        this.mContext = context;
        this.mBackWorker.execute(new OnCreateTask());
    }

    private static class OnCreateTask implements Runnable {
        private OnCreateTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            IddUtil.onCreate();
        }
    }

    public void onResume(boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("onResume()");
        }
        this.mBackWorker.execute(new OnResumeTask(z));
    }

    private class OnResumeTask implements Runnable {
        private final boolean mIsPanorama;

        private OnResumeTask(boolean z) {
            this.mIsPanorama = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mIsPanorama) {
                ResearchUtil.this.mUserOperationInfo = null;
                ResearchUtil.this.mPanoramaInfo = new PanoramaInfo();
            } else {
                ResearchUtil.this.mUserOperationInfo = new CurrentUserOperationHolder();
                ResearchUtil.this.mPanoramaInfo = null;
            }
            IddUtil.onResume();
        }
    }

    public void onPause(boolean z) {
        this.mBackWorker.execute(new OnPauseTask(z));
        if (CamLog.VERBOSE) {
            CamLog.d("onPause()");
        }
    }

    private class OnPauseTask implements Runnable {
        private final boolean mIsSameActivity;

        private OnPauseTask(boolean z) {
            this.mIsSameActivity = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            ResearchUtil.this.mIsViewEventSent = false;
            if (ResearchUtil.this.mUserOperationInfo != null) {
                ResearchUtil.this.mUserOperationInfo.clear();
            }
            if (ResearchUtil.this.mPanoramaInfo != null) {
                ResearchUtil.this.mPanoramaInfo.sendPanoramaInfo();
            }
            IddUtil.onPause(this.mIsSameActivity);
            ResearchUtil.this.clearTemporarySettingValues();
        }
    }

    public void onDestroy() {
        try {
            this.mBackWorker.submit(new OnDestroyTask(this.mContext)).get(1000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            CamLog.e("OnDestroyTask has been interrupted.", e);
        } catch (ExecutionException e2) {
            CamLog.e("OnDestroyTask failed.", e2);
        } catch (TimeoutException e3) {
            CamLog.e("OnDestroyTask is Timeout.", e3);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onDestroy()");
        }
    }

    private static class OnDestroyTask implements Callable<Boolean> {
        private final Context mContextInner;

        private OnDestroyTask(Context context) {
            this.mContextInner = context;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        public Boolean call() {
            IddUtil.onDestroy();
            return true;
        }
    }

    public void sendView(LaunchCondition.LaunchTrigger launchTrigger, Screen screen) {
        this.mBackWorker.execute(new SendViewTask(launchTrigger, screen));
    }

    private class SendViewTask implements Runnable {
        private final LaunchCondition.LaunchTrigger mLaunchTrigger;
        private final Screen mScreen;

        private SendViewTask(LaunchCondition.LaunchTrigger launchTrigger, Screen screen) {
            this.mLaunchTrigger = launchTrigger;
            this.mScreen = screen;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!ResearchUtil.this.mIsViewEventSent || this.mLaunchTrigger == LaunchCondition.LaunchTrigger.SAME_ACTIVITY) {
                CamLog.d("SendViewTask() LaunchTrigger:" + this.mLaunchTrigger);
                IddUtil.setLaunchedBy(this.mLaunchTrigger.toString());
                XperiaTipsLaunchTrigger.setLaunchTriggerEvent(ResearchUtil.this.mContext, this.mLaunchTrigger);
                IddUtil.setView(this.mScreen);
                ResearchUtil.this.mIsViewEventSent = true;
            }
        }
    }

    public void setLaunchBy(LaunchCondition.LaunchTrigger launchTrigger) {
        IddUtil.setLaunchedBy(launchTrigger.toString());
    }

    public void setView(Screen screen) {
        IddUtil.setView(screen);
    }

    public void sendThermalEvent(boolean z, boolean z2) {
        sendEvent(Event.Category.THERMAL_MITIGATION, Event.ThermalMitigation.getType(z), Event.ForceQuit.getType(z2));
    }

    public void sendCoolModeEvent(Event.CoolMode coolMode, boolean z) {
        sendEvent(Event.Category.THERMAL_MITIGATION, coolMode, Event.ForceQuit.getType(z));
    }

    public void sendLowBatteryEvent(boolean z, boolean z2) {
        sendEvent(Event.Category.LOWBATTERY_MITIGATION, Event.LowBatteryMitigation.getType(z), Event.ForceQuit.getType(z2));
    }

    public void sendSlowMotionEvent(String str, int i) {
        sendEvent(Event.Category.SLOW_MOTION, str, String.valueOf(i));
    }

    public void setCameraNotAvailableFailedToOpen() {
        this.mBackWorker.execute(new UpdateFailedToOpenCameraFlagTask());
    }

    private class UpdateFailedToOpenCameraFlagTask implements Runnable {
        private UpdateFailedToOpenCameraFlagTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ResearchUtil.this.mIsFailedToOpenCamera = true;
        }
    }

    public void sendCameraNotAvailableEvent() {
        this.mBackWorker.execute(new SendCameraNotAvailableEventTask());
    }

    private class SendCameraNotAvailableEventTask implements Runnable {
        private SendCameraNotAvailableEventTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ResearchUtil.this.sendEvent(Event.Category.CAMERA_NOT_AVAILABLE, Event.CameraNotAvailable.getType(ResearchUtil.this.mIsFailedToOpenCamera).toString(), (String) null);
            ResearchUtil.this.mIsFailedToOpenCamera = false;
        }
    }

    public void sendSettingsCommon(UserSettingValue userSettingValue) {
        this.mBackWorker.execute(new SendSettingsCommonValueTask(userSettingValue));
    }

    private class SendSettingsCommonValueTask implements Runnable {
        private final UserSettingValue mValue;

        private SendSettingsCommonValueTask(UserSettingValue userSettingValue) {
            this.mValue = userSettingValue;
        }

        @Override // java.lang.Runnable
        public void run() {
            switch (this.mValue.getKey()) {
                case AUTO_REVIEW:
                case GEO_TAG:
                case TOUCH_CAPTURE:
                case VOLUME_KEY:
                case SHUTTER_SOUND:
                case DESTINATION_TO_SAVE:
                case FAST_CAPTURE:
                case GRID_LINE:
                case SIDE_SENSE:
                case DISTORTION_CORRECTION:
                case PREDICTIVE_LAUNCH:
                case HELP_GUIDE:
                case RESET_SETTINGS:
                    ResearchUtil.this.sendEvent(Event.Category.SETTINGS_COMMON, this.mValue.getKey().toString(), this.mValue.toString());
                    break;
            }
        }
    }

    public void sendSettingsCommon(String str) {
        this.mBackWorker.execute(new SendSettingsCommonKeyTask(str));
    }

    private class SendSettingsCommonKeyTask implements Runnable {
        private final String mKey;

        private SendSettingsCommonKeyTask(String str) {
            this.mKey = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            ResearchUtil.this.sendEvent(Event.Category.SETTINGS_COMMON, this.mKey, (String) null);
        }
    }

    public void setViewerLaunched() {
        this.mBackWorker.execute(new SetViewerLaunchedTask());
    }

    private class SetViewerLaunchedTask implements Runnable {
        private SetViewerLaunchedTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mUserOperationInfo != null) {
                ResearchUtil.this.mUserOperationInfo.setViewerLaunched();
            }
            if (ResearchUtil.this.mPanoramaInfo != null) {
                ResearchUtil.this.mPanoramaInfo.setViewerLaunched();
            }
        }
    }

    public void sendRecordingEvent(Event.UserOperation userOperation, Event.StopOperation stopOperation, int i, boolean z) {
        sendRecordingEvent(userOperation, stopOperation, i, z, null);
    }

    public void sendRecordingEvent(Event.UserOperation userOperation, Event.StopOperation stopOperation, int i, boolean z, Map<String, String> map) {
        this.mBackWorker.execute(new SendRecordingEventTask(userOperation, stopOperation, i, z, map));
    }

    private class SendRecordingEventTask implements Runnable {
        private final boolean mIsLaunchViewerAfterRec;
        private final int mRecTimeMillis;
        private final Map<String, String> mSettings;
        private final Event.StopOperation mStopOperation;
        private final Event.UserOperation mUserOperation;

        private SendRecordingEventTask(Event.UserOperation userOperation, Event.StopOperation stopOperation, int i, boolean z, Map<String, String> map) {
            this.mUserOperation = userOperation;
            this.mStopOperation = stopOperation;
            this.mRecTimeMillis = i;
            this.mIsLaunchViewerAfterRec = z;
            this.mSettings = map;
        }

        @Override // java.lang.Runnable
        public void run() {
            int i = this.mRecTimeMillis / 1000;
            if (i >= 0) {
                if (!this.mIsLaunchViewerAfterRec && this.mUserOperation != null) {
                    ResearchUtil.this.setUserOperation(this.mUserOperation);
                }
                Point point = new Point();
                Event.StopOperation stopOperation = this.mStopOperation;
                ArrayMap arrayMap = new ArrayMap();
                if (ResearchUtil.this.mTemporarySettingValues != null) {
                    if (ResearchUtil.this.mTemporarySettingValues.mRecordBySideSense) {
                        ResearchUtil.this.mTemporarySettingValues.mRecordBySideSense = false;
                        point = ResearchUtil.this.mTemporarySettingValues.mSideSensePosition;
                        if (stopOperation == Event.StopOperation.USER_STOP) {
                            stopOperation = Event.StopOperation.SIDE_SENSE_STOP;
                        }
                    }
                    for (ShootingLabel.Parameter parameter : ResearchUtil.this.mTemporarySettingValues.getParameterListForRecording()) {
                        arrayMap.put(parameter.getClass().getSimpleName(), parameter.toString());
                    }
                }
                arrayMap.put("side_sense_position_x", String.valueOf(point.x));
                arrayMap.put("side_sense_position_y", String.valueOf(point.y));
                arrayMap.put("rec_time", String.valueOf(i));
                arrayMap.put("resume", String.valueOf(ResearchUtil.this.mCountRecordResume));
                arrayMap.put("snapshot", String.valueOf(ResearchUtil.this.mCountRecordSnapshot));
                arrayMap.put("stop_factor", stopOperation.toString());
                if (this.mSettings == null || !"OFF".equals(this.mSettings.get("SLOW_MOTION")) || TextUtils.isEmpty(this.mSettings.get("VIDEO_SIZE"))) {
                    arrayMap.put("video_size", "Other");
                } else {
                    arrayMap.put("video_size", this.mSettings.get("VIDEO_SIZE"));
                }
                ResearchUtil.this.sendEventAllSettings(Event.Category.ALL_SETTINGS_VIDEO, arrayMap, this.mSettings == null ? new ArrayMap() : this.mSettings);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(String.valueOf(i));
                stringBuffer.append("_Sec_");
                stringBuffer.append(String.valueOf(ResearchUtil.this.mCountRecordResume));
                stringBuffer.append("_Resume_");
                stringBuffer.append(String.valueOf(ResearchUtil.this.mCountRecordSnapshot));
                stringBuffer.append("_Snapshot_");
                ResearchUtil.this.mCountRecordResume = 0;
                ResearchUtil.this.mCountRecordSnapshot = 0;
                if (ResearchUtil.this.mTemporarySettingValues != null) {
                    stringBuffer.append(ResearchUtil.this.mTemporarySettingValues.toStringForRecording());
                    ResearchUtil.this.mTemporarySettingValues.clearRecordingMaxFaceNum();
                }
                if (this.mSettings == null || !"OFF".equals(this.mSettings.get("SLOW_MOTION")) || TextUtils.isEmpty(this.mSettings.get("VIDEO_SIZE"))) {
                    stringBuffer.append(ResearchUtil.getVideoSizeEventLabel("Other"));
                } else {
                    stringBuffer.append(ResearchUtil.getVideoSizeEventLabel(this.mSettings.get("VIDEO_SIZE")));
                }
                ResearchUtil.this.sendEvent(Event.Category.RECORDING, stopOperation.toString(), stringBuffer.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getVideoSizeEventLabel(String str) {
        if (TextUtils.isEmpty(str) || str.equals("Other")) {
            return "_V0";
        }
        switch (VideoSize.getVideoSize(str)) {
        }
        return "_V0";
    }

    public void incrementCountRecordResume() {
        this.mBackWorker.execute(new IncrementCountRecordResumeTask());
    }

    private class IncrementCountRecordResumeTask implements Runnable {
        private IncrementCountRecordResumeTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ResearchUtil.access$2708(ResearchUtil.this);
        }
    }

    public void incrementCountSnapshotInRecording() {
        this.mBackWorker.execute(new IncrementCountSnapshotInRecordingTask());
    }

    private class IncrementCountSnapshotInRecordingTask implements Runnable {
        private IncrementCountSnapshotInRecordingTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ResearchUtil.access$2808(ResearchUtil.this);
        }
    }

    public void setUserOperation(Event.UserOperation userOperation) {
        setUserOperation(userOperation, null, null);
    }

    public void setUserOperation(Event.UserOperation userOperation, List<ShootingLabel.Parameter> list, Map<String, String> map) {
        this.mBackWorker.execute(new SetUserOperationTask(userOperation, list, map));
    }

    private class SetUserOperationTask implements Runnable {
        private final List<ShootingLabel.Parameter> mParams;
        private final Map<String, String> mSettings;
        private final Event.UserOperation mUserOperation;

        private SetUserOperationTask(Event.UserOperation userOperation, List<ShootingLabel.Parameter> list, Map<String, String> map) {
            this.mUserOperation = userOperation;
            this.mParams = list;
            this.mSettings = map;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mUserOperationInfo != null) {
                ResearchUtil.this.mUserOperationInfo.setUserOperation(this.mUserOperation, this.mParams, this.mSettings);
            }
        }
    }

    public void sendEvent(Event.Category category, Event.Action action, Event.Label label) {
        sendEvent(category, action, label, 0L);
    }

    public void sendEvent(Event.Category category, Event.Action action, Event.Label label, long j) {
        sendEvent(category, action == null ? null : action.toString(), label != null ? label.toString() : null, j);
    }

    public void sendEvent(Event.Category category, String str, String str2) {
        sendEvent(category, str, str2, 0L);
    }

    private void sendEvent(Event.Category category, String str, String str2, long j) {
        this.mBackWorker.execute(new SendEventTask(category, str, str2, j));
    }

    private static class SendEventTask implements Runnable {
        private final String mAction;
        private final Event.Category mCategory;
        private final String mLabel;
        private final long mValue;

        private SendEventTask(Event.Category category, String str, String str2, long j) {
            this.mCategory = category;
            this.mAction = str;
            this.mLabel = str2;
            this.mValue = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            IddUtil.sendEvent(this.mCategory, this.mAction, this.mLabel, this.mValue);
        }
    }

    private static class TemporarySettingValues {
        private boolean mRecordBySideSense;
        private float mZoomRatio = 1.0f;
        private int mOrientation = 0;
        private int mFaceNum = 0;
        private int mRecordingMaxFaceNum = 0;
        private int mPredictiveCaptureNum = 0;
        private int mManualBurstCount = 0;
        private Event.CaptureTrigger mCaptureTrigger = Event.CaptureTrigger.OTHER;
        private Point mSideSensePosition = new Point();
        private int mAssistSelfTimer = -1;

        public void updateZoomRatio(float f) {
            this.mZoomRatio = f;
        }

        public void updateOrientation(int i) {
            this.mOrientation = i;
        }

        public void updateFaceNum(int i) {
            this.mFaceNum = i;
        }

        public void updateRecordingMaxFaceNum(int i) {
            if (this.mRecordingMaxFaceNum < i) {
                this.mRecordingMaxFaceNum = i;
            }
        }

        public void updatePredictiveCaptureNum(int i) {
            this.mPredictiveCaptureNum = i;
        }

        public void updateManualBurstCount(int i) {
            this.mManualBurstCount = i;
        }

        public void updateCaptureTrigger(Event.CaptureTrigger captureTrigger) {
            this.mCaptureTrigger = captureTrigger;
        }

        public void updateSideSensePosition(int i, int i2) {
            this.mSideSensePosition = new Point(i, i2);
        }

        public void updateRecordBySideSense(boolean z) {
            this.mRecordBySideSense = z;
        }

        public void updateAssistSelfTimer(int i) {
            this.mAssistSelfTimer = i;
        }

        public void clearFaceNum() {
            this.mFaceNum = 0;
        }

        public void clearRecordingMaxFaceNum() {
            this.mRecordingMaxFaceNum = 0;
        }

        public void clearCaptureTrigger() {
            this.mCaptureTrigger = Event.CaptureTrigger.OTHER;
        }

        public void clearAssistSelfTimer() {
            this.mAssistSelfTimer = -1;
        }

        private int roundZoomRatio() {
            if (this.mZoomRatio - 0.05f < 1.0f) {
                return 0;
            }
            return (int) this.mZoomRatio;
        }

        public String toString() {
            return "Z" + roundZoomRatio() + "_O" + this.mOrientation + "_F" + this.mFaceNum;
        }

        public String toStringForRecording() {
            return "Z" + roundZoomRatio() + "_O" + this.mOrientation + "_F" + this.mRecordingMaxFaceNum;
        }

        public String getManualBurstParameterForIDD() {
            if (this.mManualBurstCount == 0) {
                return ShootingLabel.PREDICTIVE_LAUNCH_OFF;
            }
            return String.valueOf(this.mManualBurstCount) + "_Shots";
        }

        public List<ShootingLabel.Parameter> getParameterList() {
            ArrayList arrayList = new ArrayList();
            arrayList.add(ShootingLabel.getOrientationParameter(this.mOrientation));
            arrayList.add(ShootingLabel.getZoomParameter(roundZoomRatio()));
            arrayList.add(ShootingLabel.getFaceNumParameter(this.mFaceNum));
            arrayList.add(ShootingLabel.getPredictiveCaptureNumParameter(this.mPredictiveCaptureNum));
            arrayList.add(ShootingLabel.getCaptureTriggerParameter(this.mCaptureTrigger.toString()));
            return arrayList;
        }

        public List<ShootingLabel.Parameter> getParameterListForRecording() {
            ArrayList arrayList = new ArrayList();
            arrayList.add(ShootingLabel.getOrientationParameter(this.mOrientation));
            arrayList.add(ShootingLabel.getZoomParameter(roundZoomRatio()));
            arrayList.add(ShootingLabel.getFaceNumParameter(this.mRecordingMaxFaceNum));
            return arrayList;
        }
    }

    public void clearTemporarySettingValues() {
        this.mBackWorker.execute(new ClearTemporarySettingValuesTask());
    }

    private class ClearTemporarySettingValuesTask implements Runnable {
        private ClearTemporarySettingValuesTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ResearchUtil.this.mTemporarySettingValues = null;
        }
    }

    public void setZoomRatio(float f) {
        this.mBackWorker.execute(new SetZoomRatioTask(f));
    }

    private class SetZoomRatioTask implements Runnable {
        private final float mZoomRatio;

        public SetZoomRatioTask(float f) {
            this.mZoomRatio = f;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updateZoomRatio(this.mZoomRatio);
        }
    }

    public void setOrientation(int i) {
        this.mBackWorker.execute(new SetOrientationTask(i));
    }

    private class SetOrientationTask implements Runnable {
        private final int mOrientation;

        public SetOrientationTask(int i) {
            this.mOrientation = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updateOrientation(this.mOrientation);
        }
    }

    public void setFaceNum(int i) {
        this.mBackWorker.execute(new SetFaceNumTask(i));
    }

    private class SetFaceNumTask implements Runnable {
        private final int mNum;

        public SetFaceNumTask(int i) {
            this.mNum = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updateFaceNum(this.mNum);
        }
    }

    public void setRecordingMaxFaceNum(int i) {
        this.mBackWorker.execute(new SetRecordingMaxFaceNumTask(i));
    }

    private class SetRecordingMaxFaceNumTask implements Runnable {
        private final int mNum;

        public SetRecordingMaxFaceNumTask(int i) {
            this.mNum = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updateRecordingMaxFaceNum(this.mNum);
        }
    }

    public void clearFaceNum() {
        this.mBackWorker.execute(new ClearFaceNumTask());
    }

    private class ClearFaceNumTask implements Runnable {
        private ClearFaceNumTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.clearFaceNum();
        }
    }

    public void setPredictiveCaptureNum(int i) {
        this.mBackWorker.execute(new SetPredictiveCaptureNumTask(i));
    }

    private class SetPredictiveCaptureNumTask implements Runnable {
        private final int mPredictiveCaptureNum;

        public SetPredictiveCaptureNumTask(int i) {
            this.mPredictiveCaptureNum = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updatePredictiveCaptureNum(this.mPredictiveCaptureNum);
        }
    }

    public void setManualBurstCount(int i) {
        this.mBackWorker.execute(new SetManualBurstCountTask(i));
    }

    private class SetManualBurstCountTask implements Runnable {
        private final int mManualBurstCount;

        public SetManualBurstCountTask(int i) {
            this.mManualBurstCount = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updateManualBurstCount(this.mManualBurstCount);
        }
    }

    public void setCaptureTrigger(Event.CaptureTrigger captureTrigger) {
        this.mBackWorker.execute(new SetCaptureTriggerTask(captureTrigger));
    }

    private class SetCaptureTriggerTask implements Runnable {
        private final Event.CaptureTrigger mCaptureTrigger;

        public SetCaptureTriggerTask(Event.CaptureTrigger captureTrigger) {
            this.mCaptureTrigger = captureTrigger;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updateCaptureTrigger(this.mCaptureTrigger);
        }
    }

    public void setSideSensePosition(int i, int i2) {
        this.mBackWorker.execute(new SetSideSensePositionTask(i, i2));
    }

    private class SetSideSensePositionTask implements Runnable {
        private final int mX;
        private final int mY;

        public SetSideSensePositionTask(int i, int i2) {
            this.mX = i;
            this.mY = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updateSideSensePosition(this.mX, this.mY);
        }
    }

    public void setRecordBySideSense(boolean z) {
        this.mBackWorker.execute(new SetRecordBySideSenseTask(z));
    }

    private class SetRecordBySideSenseTask implements Runnable {
        private final boolean mRecordBySideSense;

        public SetRecordBySideSenseTask(boolean z) {
            this.mRecordBySideSense = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updateRecordBySideSense(this.mRecordBySideSense);
        }
    }

    private static class AfDoneKeepingTimeHolder {
        private boolean mContinuousCapture;
        private long mTimeAfDone;
        private long mTimeCapture;

        private AfDoneKeepingTimeHolder() {
            this.mContinuousCapture = false;
            this.mTimeAfDone = 0L;
            this.mTimeCapture = 0L;
        }

        public void updateContinuousCapture(boolean z) {
            this.mContinuousCapture = z;
        }

        public void updateTimeAfDone(long j) {
            this.mTimeAfDone = j;
        }

        public void updateTimeCapture(long j) {
            if (this.mTimeCapture == 0) {
                this.mTimeCapture = j;
            }
        }

        public ShootingLabel.Parameter getParameter() {
            Event.TimeFromAfDoneToCaptureStart type;
            if (this.mContinuousCapture) {
                type = Event.TimeFromAfDoneToCaptureStart.CONTINUOUS_CAPTURE;
            } else if (this.mTimeAfDone == 0) {
                type = Event.TimeFromAfDoneToCaptureStart.NOT_TARGET;
            } else {
                if (CamLog.DEBUG) {
                    CamLog.d("AfDoneKeepingTime: " + (this.mTimeCapture - this.mTimeAfDone));
                }
                type = Event.TimeFromAfDoneToCaptureStart.getType(this.mTimeCapture - this.mTimeAfDone);
            }
            return ShootingLabel.getAfDoneKeepingTimeParameter(type.toString());
        }

        public void clear() {
            this.mTimeAfDone = 0L;
            this.mTimeCapture = 0L;
            this.mContinuousCapture = false;
        }
    }

    public void setContinuousCapture() {
        this.mBackWorker.execute(new SetContinuousCaptureTask());
    }

    private class SetContinuousCaptureTask implements Runnable {
        private SetContinuousCaptureTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mAfDoneKeepingTimeHolder == null) {
                ResearchUtil.this.mAfDoneKeepingTimeHolder = new AfDoneKeepingTimeHolder();
            }
            ResearchUtil.this.mAfDoneKeepingTimeHolder.updateContinuousCapture(true);
        }
    }

    public void setTimeAfDone() {
        setTime(true, System.currentTimeMillis());
    }

    public void setTimeCaptureStart() {
        setTime(false, System.currentTimeMillis());
    }

    private void setTime(boolean z, long j) {
        this.mBackWorker.execute(new SetTimeTask(z, j));
    }

    private class SetTimeTask implements Runnable {
        private final boolean mIsStart;
        private final long mTime;

        public SetTimeTask(boolean z, long j) {
            this.mIsStart = z;
            this.mTime = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mAfDoneKeepingTimeHolder == null) {
                ResearchUtil.this.mAfDoneKeepingTimeHolder = new AfDoneKeepingTimeHolder();
            }
            if (this.mIsStart) {
                ResearchUtil.this.mAfDoneKeepingTimeHolder.updateTimeAfDone(this.mTime);
            } else {
                ResearchUtil.this.mAfDoneKeepingTimeHolder.updateTimeCapture(this.mTime);
            }
        }
    }

    private enum XperiaTipsLaunchTrigger {
        HW_KEY_SLEEP_OR_LOCK,
        HW_KEY_OTHER,
        SHORTCUT_LOCKSCREEN,
        POWER_KEY_DOUBLE_PRESS,
        HOME_APP,
        NOT_TARGET;

        private static final String EVENT_NAME = "launch_trigger";

        /* JADX INFO: Access modifiers changed from: private */
        public static void setLaunchTriggerEvent(Context context, LaunchCondition.LaunchTrigger launchTrigger) {
            XperiaTipsLaunchTrigger xperiaTipsLaunchTrigger;
            if (launchTrigger == LaunchCondition.LaunchTrigger.HW_CAMERA_KEY_LOCK) {
                xperiaTipsLaunchTrigger = HW_KEY_SLEEP_OR_LOCK;
            } else if (launchTrigger == LaunchCondition.LaunchTrigger.HW_CAMERA_KEY) {
                xperiaTipsLaunchTrigger = HW_KEY_OTHER;
            } else if (launchTrigger == LaunchCondition.LaunchTrigger.LOCK_SCREEN) {
                xperiaTipsLaunchTrigger = SHORTCUT_LOCKSCREEN;
            } else if (launchTrigger == LaunchCondition.LaunchTrigger.POWER_KEY_DOUBLE_TAP) {
                xperiaTipsLaunchTrigger = POWER_KEY_DOUBLE_PRESS;
            } else if (launchTrigger != LaunchCondition.LaunchTrigger.HOME) {
                return;
            } else {
                xperiaTipsLaunchTrigger = HOME_APP;
            }
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", EVENT_NAME);
                contentValues.put("data", xperiaTipsLaunchTrigger.toString());
                context.getContentResolver().insert(ContributionContract.Event.CONTENT_URI, contentValues);
            } catch (Exception e) {
                if (CamLog.VERBOSE) {
                    CamLog.e("Failed setting data to XPERIA Tips : " + e);
                }
            } catch (NoClassDefFoundError e2) {
                CamLog.e("Not found getmore api : " + e2);
            }
        }
    }

    public void sendSelfTimerCancelledEvent(Event.SelfTimerTrigger selfTimerTrigger) {
        sendEvent(Event.Category.SELFTIMER_CANCELLED, selfTimerTrigger, (Event.Label) null);
    }

    public void sendPredictiveLaunchEvent(Event.PredictiveLaunchAction predictiveLaunchAction) {
        sendEvent(Event.Category.PREDICTIVE_LAUNCH, predictiveLaunchAction, (Event.Label) null);
    }

    public void sendAutoPowerOffEvent(Event.AutoPowerOffAction autoPowerOffAction) {
        sendEvent(Event.Category.AUTO_POWEROFF, autoPowerOffAction, (Event.Label) null);
    }

    public void sendPerformanceData(String str, long j, boolean z) {
        sendPerformanceData(str, j, z, null);
    }

    public void sendPerformanceData(String str, long j, boolean z, String str2) {
        this.mBackWorker.execute(new SendPerformanceDataTask(str, j, z, str2));
    }

    private static class SendPerformanceDataTask implements Runnable {
        private final String mBatteryLevel;
        private final boolean mIsHeated;
        private final String mKey;
        private final long mMillis;

        private SendPerformanceDataTask(String str, long j, boolean z, String str2) {
            this.mKey = str;
            this.mMillis = j;
            this.mIsHeated = z;
            this.mBatteryLevel = str2;
        }

        @Override // java.lang.Runnable
        public void run() {
            IddUtil.sendPerformanceData(this.mKey, this.mMillis, this.mIsHeated, this.mBatteryLevel);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEventAllSettings(Event.Category category, Map<String, String> map, Map<String, String> map2) {
        this.mBackWorker.execute(new SendEventAllSettingsTask(category, map, map2));
    }

    private static class SendEventAllSettingsTask implements Runnable {
        private final Event.Category mCategory;
        private final Map<String, String> mEnv;
        private final Map<String, String> mSettings;

        private SendEventAllSettingsTask(Event.Category category, Map<String, String> map, Map<String, String> map2) {
            this.mCategory = category;
            this.mEnv = map;
            this.mSettings = map2;
        }

        @Override // java.lang.Runnable
        public void run() {
            IddUtil.sendEventAllSettings(this.mCategory, this.mEnv, this.mSettings);
        }
    }

    public void sendEventChangedSetting(String str, String str2, String str3) {
        this.mBackWorker.execute(new SendEventChangedSettingTask(str, str2, str3));
    }

    private static class SendEventChangedSettingTask implements Runnable {
        private final String mAfter;
        private final String mBefore;
        private final String mSetting;

        private SendEventChangedSettingTask(String str, String str2, String str3) {
            this.mSetting = str;
            this.mBefore = str2;
            this.mAfter = str3;
        }

        @Override // java.lang.Runnable
        public void run() {
            IddUtil.sendEventChangedSetting(this.mSetting, this.mBefore, this.mAfter);
        }
    }

    public enum VideoSize {
        FULL_HD,
        FULL_HD_60FPS,
        HD,
        VGA,
        FWVGA,
        QVGA,
        MMS,
        FOUR_K_UHD_H264,
        FOUR_K_UHD_H265;

        public static VideoSize getVideoSize(String str) {
            return valueOf(str);
        }
    }

    public void sendEventInternalModeChange(String str, String str2, String str3) {
        this.mBackWorker.execute(new SendEventInternalModeChangeTask(str, str2, str3));
    }

    private static class SendEventInternalModeChangeTask implements Runnable {
        private final String mCurrentMode;
        private final String mMethod;
        private final String mTargetMode;

        private SendEventInternalModeChangeTask(String str, String str2, String str3) {
            this.mCurrentMode = str;
            this.mTargetMode = str2;
            this.mMethod = str3;
        }

        @Override // java.lang.Runnable
        public void run() {
            IddUtil.sendEventInternalModeChange(this.mCurrentMode, this.mTargetMode, this.mMethod);
        }
    }

    public void sendEventAddonModeChange(Event.Category category, String str, String str2, String str3) {
        this.mBackWorker.execute(new SendEventAddonModeChangeTask(category, str, str2, str3));
    }

    private static class SendEventAddonModeChangeTask implements Runnable {
        private final String mAction;
        private final Event.Category mCategory;
        private final String mLabel;
        private final String mMethod;

        private SendEventAddonModeChangeTask(Event.Category category, String str, String str2, String str3) {
            this.mCategory = category;
            this.mAction = str;
            this.mLabel = str2;
            this.mMethod = str3;
        }

        @Override // java.lang.Runnable
        public void run() {
            IddUtil.sendEventAddonModeChange(this.mCategory, this.mAction, this.mLabel, this.mMethod);
        }
    }

    public void sendSetupWizardEvent(Event.WizardPage wizardPage, Event.WizardResult wizardResult, long j) {
        this.mBackWorker.execute(new sendWizardEventTask(wizardPage, wizardResult, j));
    }

    private static class sendWizardEventTask implements Runnable {
        private final String mPage;
        private final String mResult;
        private final long mTime;

        private sendWizardEventTask(Event.WizardPage wizardPage, Event.WizardResult wizardResult, long j) {
            this.mPage = wizardPage.toString();
            this.mResult = wizardResult.toString();
            this.mTime = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            IddUtil.sendWizardEvent(this.mPage, String.valueOf(this.mTime), this.mResult);
        }
    }

    public void sendDualCameraEffectEvent(Context context) {
        this.mBackWorker.execute(new SendExternalCameraAppEventTask(context, Uri.parse(DUAL_CAMERA_EFFECT_USAGE_DATA_CONTENT_URI), null));
    }

    public void sendPortraitSelfieEvent(Context context, String str) {
        this.mBackWorker.execute(new SendExternalCameraAppEventTask(context, Uri.parse(PORTRAIT_SELFIE_USAGE_DATA_CONTENT_URI), str));
    }

    private static class SendExternalCameraAppEventTask implements Runnable {
        private final Context mContext;
        private final String mModeTo;
        private final Uri mUri;

        private SendExternalCameraAppEventTask(Context context, Uri uri, String str) {
            this.mContext = context;
            this.mUri = uri;
            this.mModeTo = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            ContentResolver contentResolver;
            Uri uri;
            StringBuilder sb;
            Cursor cursorQuery = this.mContext.getContentResolver().query(this.mUri, null, null, null, null);
            if (cursorQuery != null) {
                if (cursorQuery.moveToFirst()) {
                    do {
                        try {
                            try {
                                String string = cursorQuery.getString(cursorQuery.getColumnIndex("data"));
                                long j = cursorQuery.getLong(cursorQuery.getColumnIndex("created_at"));
                                JSONObject jSONObject = new JSONObject(string);
                                if (this.mModeTo != null) {
                                    jSONObject.put(IddUtil.KEY_VALUE_TO, this.mModeTo);
                                }
                                IddUtil.sendExternalCameraAppEvent(jSONObject, j);
                                contentResolver = this.mContext.getContentResolver();
                                uri = this.mUri;
                                sb = new StringBuilder();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                contentResolver = this.mContext.getContentResolver();
                                uri = this.mUri;
                                sb = new StringBuilder();
                            }
                            sb.append("_id = ");
                            sb.append(cursorQuery.getInt(cursorQuery.getColumnIndex("_id")));
                            contentResolver.delete(uri, sb.toString(), null);
                        } catch (Throwable th) {
                            this.mContext.getContentResolver().delete(this.mUri, "_id = " + cursorQuery.getInt(cursorQuery.getColumnIndex("_id")), null);
                            throw th;
                        }
                    } while (cursorQuery.moveToNext());
                }
                cursorQuery.close();
            }
        }
    }

    public void setAssistSelfTimer(int i) {
        this.mBackWorker.execute(new setAssistSelfTimerTask(i));
    }

    private class setAssistSelfTimerTask implements Runnable {
        private final int mAssistSelfTimer;

        public setAssistSelfTimerTask(int i) {
            this.mAssistSelfTimer = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ResearchUtil.this.mTemporarySettingValues == null) {
                ResearchUtil.this.mTemporarySettingValues = new TemporarySettingValues();
            }
            ResearchUtil.this.mTemporarySettingValues.updateAssistSelfTimer(this.mAssistSelfTimer);
        }
    }
}
