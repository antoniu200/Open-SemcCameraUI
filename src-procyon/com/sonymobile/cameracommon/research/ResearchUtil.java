// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.research;

import com.sonymobile.getmore.api.ContributionContract$Event;
import android.content.ContentValues;
import java.util.ArrayList;
import android.content.ContentResolver;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;
import android.util.ArrayMap;
import android.graphics.Point;
import com.sonymobile.cameracommon.research.parameters.ShootingLabel;
import java.util.List;
import com.sonymobile.cameracommon.research.idd.IddUtil;
import com.sonymobile.cameracommon.research.parameters.Screen;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import android.net.Uri;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import com.sonyericsson.android.camera.util.CamLog;
import android.text.TextUtils;
import java.util.Map;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonyericsson.android.camera.util.ThreadUtil;
import android.content.Context;
import java.util.concurrent.ExecutorService;

public class ResearchUtil
{
    private static final String DUAL_CAMERA_EFFECT_USAGE_DATA_CONTENT_URI = "content://com.sonymobile.addoncamera.dualcameraeffect.provider/UsageData";
    private static final String PORTRAIT_SELFIE_USAGE_DATA_CONTENT_URI = "content://com.sonymobile.addoncamera.beautyportrait.provider/UsageData";
    public static final String TAG = "ResearchUtil";
    private static final ResearchUtil sInstance;
    private AfDoneKeepingTimeHolder mAfDoneKeepingTimeHolder;
    private ExecutorService mBackWorker;
    private Context mContext;
    private int mCountRecordResume;
    private int mCountRecordSnapshot;
    private boolean mIsFailedToOpenCamera;
    private boolean mIsViewEventSent;
    private PanoramaInfo mPanoramaInfo;
    private TemporarySettingValues mTemporarySettingValues;
    private CurrentUserOperationHolder mUserOperationInfo;
    
    static {
        sInstance = new ResearchUtil();
    }
    
    private ResearchUtil() {
        this.mUserOperationInfo = null;
        this.mPanoramaInfo = null;
        this.mContext = null;
        this.mIsViewEventSent = false;
        this.mIsFailedToOpenCamera = false;
        this.mCountRecordResume = 0;
        this.mCountRecordSnapshot = 0;
        this.mBackWorker = null;
        this.mTemporarySettingValues = null;
        this.mAfDoneKeepingTimeHolder = null;
        if (this.mBackWorker == null) {
            this.mBackWorker = ThreadUtil.buildExecutor("R-Thread", 1);
        }
    }
    
    public static ResearchUtil getInstance() {
        return ResearchUtil.sInstance;
    }
    
    private static String getVideoSizeEventLabel(String s) {
        if (!TextUtils.isEmpty((CharSequence)s) && !s.equals("Other")) {
            switch (ResearchUtil$1.$SwitchMap$com$sonymobile$cameracommon$research$ResearchUtil$VideoSize[VideoSize.getVideoSize(s).ordinal()]) {
                default: {
                    s = "_V0";
                    break;
                }
                case 9: {
                    s = "_V10";
                    break;
                }
                case 8: {
                    s = "_V9";
                    break;
                }
                case 7: {
                    s = "_V8";
                    break;
                }
                case 6: {
                    s = "_V7";
                    break;
                }
                case 5: {
                    s = "_V6";
                    break;
                }
                case 4: {
                    s = "_V5";
                    break;
                }
                case 3: {
                    s = "_V4";
                    break;
                }
                case 2: {
                    s = "_V3";
                    break;
                }
                case 1: {
                    s = "_V2";
                    break;
                }
            }
            return s;
        }
        return "_V0";
    }
    
    private void sendEvent(final Event.Category category, final String s, final String s2, final long n) {
        this.mBackWorker.execute(new SendEventTask(category, s, s2, n));
    }
    
    private void sendEventAllSettings(final Event.Category category, final Map<String, String> map, final Map<String, String> map2) {
        this.mBackWorker.execute(new SendEventAllSettingsTask(category, (Map)map, (Map)map2));
    }
    
    private void setTime(final boolean b, final long n) {
        this.mBackWorker.execute(new SetTimeTask(b, n));
    }
    
    public void clearFaceNum() {
        this.mBackWorker.execute(new ClearFaceNumTask());
    }
    
    public void clearTemporarySettingValues() {
        this.mBackWorker.execute(new ClearTemporarySettingValuesTask());
    }
    
    public void incrementCountRecordResume() {
        this.mBackWorker.execute(new IncrementCountRecordResumeTask());
    }
    
    public void incrementCountSnapshotInRecording() {
        this.mBackWorker.execute(new IncrementCountSnapshotInRecordingTask());
    }
    
    public void onCreate(final Context mContext) {
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate()");
        }
        this.mContext = mContext;
        this.mBackWorker.execute(new OnCreateTask());
    }
    
    public void onDestroy() {
        final Future<Object> submit = this.mBackWorker.submit((Callable<Object>)new OnDestroyTask(this.mContext));
        try {
            submit.get(1000L, TimeUnit.MILLISECONDS);
        }
        catch (final TimeoutException ex) {
            CamLog.e("OnDestroyTask is Timeout.", ex);
        }
        catch (final ExecutionException ex2) {
            CamLog.e("OnDestroyTask failed.", ex2);
        }
        catch (final InterruptedException ex3) {
            CamLog.e("OnDestroyTask has been interrupted.", ex3);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onDestroy()");
        }
    }
    
    public void onPause(final boolean b) {
        this.mBackWorker.execute(new OnPauseTask(b));
        if (CamLog.VERBOSE) {
            CamLog.d("onPause()");
        }
    }
    
    public void onResume(final boolean b) {
        if (CamLog.VERBOSE) {
            CamLog.d("onResume()");
        }
        this.mBackWorker.execute(new OnResumeTask(b));
    }
    
    public void sendAutoPowerOffEvent(final Event.AutoPowerOffAction autoPowerOffAction) {
        this.sendEvent(Event.Category.AUTO_POWEROFF, autoPowerOffAction, null);
    }
    
    public void sendCameraNotAvailableEvent() {
        this.mBackWorker.execute(new SendCameraNotAvailableEventTask());
    }
    
    public void sendCoolModeEvent(final Event.CoolMode coolMode, final boolean b) {
        this.sendEvent(Event.Category.THERMAL_MITIGATION, coolMode, Event.ForceQuit.getType(b));
    }
    
    public void sendDualCameraEffectEvent(final Context context) {
        this.mBackWorker.execute(new SendExternalCameraAppEventTask(context, Uri.parse("content://com.sonymobile.addoncamera.dualcameraeffect.provider/UsageData"), (String)null));
    }
    
    public void sendEvent(final Event.Category category, final Event.Action action, final Event.Label label) {
        this.sendEvent(category, action, label, 0L);
    }
    
    public void sendEvent(final Event.Category category, final Event.Action action, final Event.Label label, final long n) {
        final String s = null;
        String string;
        if (action == null) {
            string = null;
        }
        else {
            string = action.toString();
        }
        String string2;
        if (label == null) {
            string2 = s;
        }
        else {
            string2 = label.toString();
        }
        this.sendEvent(category, string, string2, n);
    }
    
    public void sendEvent(final Event.Category category, final String s, final String s2) {
        this.sendEvent(category, s, s2, 0L);
    }
    
    public void sendEventAddonModeChange(final Event.Category category, final String s, final String s2, final String s3) {
        this.mBackWorker.execute(new SendEventAddonModeChangeTask(category, s, s2, s3));
    }
    
    public void sendEventChangedSetting(final String s, final String s2, final String s3) {
        this.mBackWorker.execute(new SendEventChangedSettingTask(s, s2, s3));
    }
    
    public void sendEventInternalModeChange(final String s, final String s2, final String s3) {
        this.mBackWorker.execute(new SendEventInternalModeChangeTask(s, s2, s3));
    }
    
    public void sendLowBatteryEvent(final boolean b, final boolean b2) {
        this.sendEvent(Event.Category.LOWBATTERY_MITIGATION, Event.LowBatteryMitigation.getType(b), Event.ForceQuit.getType(b2));
    }
    
    public void sendPerformanceData(final String s, final long n, final boolean b) {
        this.sendPerformanceData(s, n, b, null);
    }
    
    public void sendPerformanceData(final String s, final long n, final boolean b, final String s2) {
        this.mBackWorker.execute(new SendPerformanceDataTask(s, n, b, s2));
    }
    
    public void sendPortraitSelfieEvent(final Context context, final String s) {
        this.mBackWorker.execute(new SendExternalCameraAppEventTask(context, Uri.parse("content://com.sonymobile.addoncamera.beautyportrait.provider/UsageData"), s));
    }
    
    public void sendPredictiveLaunchEvent(final Event.PredictiveLaunchAction predictiveLaunchAction) {
        this.sendEvent(Event.Category.PREDICTIVE_LAUNCH, predictiveLaunchAction, null);
    }
    
    public void sendRecordingEvent(final Event.UserOperation userOperation, final Event.StopOperation stopOperation, final int n, final boolean b) {
        this.sendRecordingEvent(userOperation, stopOperation, n, b, null);
    }
    
    public void sendRecordingEvent(final Event.UserOperation userOperation, final Event.StopOperation stopOperation, final int n, final boolean b, final Map<String, String> map) {
        this.mBackWorker.execute(new SendRecordingEventTask(userOperation, stopOperation, n, b, (Map)map));
    }
    
    public void sendSelfTimerCancelledEvent(final Event.SelfTimerTrigger selfTimerTrigger) {
        this.sendEvent(Event.Category.SELFTIMER_CANCELLED, selfTimerTrigger, null);
    }
    
    public void sendSettingsCommon(final UserSettingValue userSettingValue) {
        this.mBackWorker.execute(new SendSettingsCommonValueTask(userSettingValue));
    }
    
    public void sendSettingsCommon(final String s) {
        this.mBackWorker.execute(new SendSettingsCommonKeyTask(s));
    }
    
    public void sendSetupWizardEvent(final Event.WizardPage wizardPage, final Event.WizardResult wizardResult, final long n) {
        this.mBackWorker.execute(new sendWizardEventTask(wizardPage, wizardResult, n));
    }
    
    public void sendSlowMotionEvent(final String s, final int i) {
        this.sendEvent(Event.Category.SLOW_MOTION, s, String.valueOf(i));
    }
    
    public void sendThermalEvent(final boolean b, final boolean b2) {
        this.sendEvent(Event.Category.THERMAL_MITIGATION, Event.ThermalMitigation.getType(b), Event.ForceQuit.getType(b2));
    }
    
    public void sendView(final LaunchCondition.LaunchTrigger launchTrigger, final Screen screen) {
        this.mBackWorker.execute(new SendViewTask(launchTrigger, screen));
    }
    
    public void setAssistSelfTimer(final int n) {
        this.mBackWorker.execute(new setAssistSelfTimerTask(n));
    }
    
    public void setCameraNotAvailableFailedToOpen() {
        this.mBackWorker.execute(new UpdateFailedToOpenCameraFlagTask());
    }
    
    public void setCaptureTrigger(final Event.CaptureTrigger captureTrigger) {
        this.mBackWorker.execute(new SetCaptureTriggerTask(captureTrigger));
    }
    
    public void setContinuousCapture() {
        this.mBackWorker.execute(new SetContinuousCaptureTask());
    }
    
    public void setFaceNum(final int n) {
        this.mBackWorker.execute(new SetFaceNumTask(n));
    }
    
    public void setLaunchBy(final LaunchCondition.LaunchTrigger launchTrigger) {
        IddUtil.setLaunchedBy(launchTrigger.toString());
    }
    
    public void setManualBurstCount(final int n) {
        this.mBackWorker.execute(new SetManualBurstCountTask(n));
    }
    
    public void setOrientation(final int n) {
        this.mBackWorker.execute(new SetOrientationTask(n));
    }
    
    public void setPredictiveCaptureNum(final int n) {
        this.mBackWorker.execute(new SetPredictiveCaptureNumTask(n));
    }
    
    public void setRecordBySideSense(final boolean b) {
        this.mBackWorker.execute(new SetRecordBySideSenseTask(b));
    }
    
    public void setRecordingMaxFaceNum(final int n) {
        this.mBackWorker.execute(new SetRecordingMaxFaceNumTask(n));
    }
    
    public void setSideSensePosition(final int n, final int n2) {
        this.mBackWorker.execute(new SetSideSensePositionTask(n, n2));
    }
    
    public void setTimeAfDone() {
        this.setTime(true, System.currentTimeMillis());
    }
    
    public void setTimeCaptureStart() {
        this.setTime(false, System.currentTimeMillis());
    }
    
    public void setUserOperation(final Event.UserOperation userOperation) {
        this.setUserOperation(userOperation, null, null);
    }
    
    public void setUserOperation(final Event.UserOperation userOperation, final List<ShootingLabel.Parameter> list, final Map<String, String> map) {
        this.mBackWorker.execute(new SetUserOperationTask(userOperation, (List)list, (Map)map));
    }
    
    public void setView(final Screen view) {
        IddUtil.setView(view);
    }
    
    public void setViewerLaunched() {
        this.mBackWorker.execute(new SetViewerLaunchedTask());
    }
    
    public void setZoomRatio(final float n) {
        this.mBackWorker.execute(new SetZoomRatioTask(n));
    }
    
    private static class AfDoneKeepingTimeHolder
    {
        private boolean mContinuousCapture;
        private long mTimeAfDone;
        private long mTimeCapture;
        
        private AfDoneKeepingTimeHolder() {
            this.mContinuousCapture = false;
            this.mTimeAfDone = 0L;
            this.mTimeCapture = 0L;
        }
        
        public void clear() {
            this.mTimeAfDone = 0L;
            this.mTimeCapture = 0L;
            this.mContinuousCapture = false;
        }
        
        public ShootingLabel.Parameter getParameter() {
            Event.TimeFromAfDoneToCaptureStart timeFromAfDoneToCaptureStart;
            if (this.mContinuousCapture) {
                timeFromAfDoneToCaptureStart = Event.TimeFromAfDoneToCaptureStart.CONTINUOUS_CAPTURE;
            }
            else if (this.mTimeAfDone == 0L) {
                timeFromAfDoneToCaptureStart = Event.TimeFromAfDoneToCaptureStart.NOT_TARGET;
            }
            else {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("AfDoneKeepingTime: ");
                    sb.append(this.mTimeCapture - this.mTimeAfDone);
                    CamLog.d(sb.toString());
                }
                timeFromAfDoneToCaptureStart = Event.TimeFromAfDoneToCaptureStart.getType(this.mTimeCapture - this.mTimeAfDone);
            }
            return ShootingLabel.getAfDoneKeepingTimeParameter(timeFromAfDoneToCaptureStart.toString());
        }
        
        public void updateContinuousCapture(final boolean mContinuousCapture) {
            this.mContinuousCapture = mContinuousCapture;
        }
        
        public void updateTimeAfDone(final long mTimeAfDone) {
            this.mTimeAfDone = mTimeAfDone;
        }
        
        public void updateTimeCapture(final long mTimeCapture) {
            if (this.mTimeCapture == 0L) {
                this.mTimeCapture = mTimeCapture;
            }
        }
    }
    
    private class ClearFaceNumTask implements Runnable
    {
        final ResearchUtil this$0;
        
        private ClearFaceNumTask(final ResearchUtil this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.clearFaceNum();
        }
    }
    
    private class ClearTemporarySettingValuesTask implements Runnable
    {
        final ResearchUtil this$0;
        
        private ClearTemporarySettingValuesTask(final ResearchUtil this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mTemporarySettingValues = null;
        }
    }
    
    private class CurrentUserOperationHolder
    {
        private Event.UserOperation mUserOperation;
        final ResearchUtil this$0;
        
        private CurrentUserOperationHolder(final ResearchUtil this$0) {
            this.this$0 = this$0;
            this.mUserOperation = null;
        }
        
        public void clear() {
            if (CamLog.VERBOSE) {
                CamLog.d("UserOperationInfo#clear()");
            }
            this.mUserOperation = null;
        }
        
        public void setUserOperation(final Event.UserOperation userOperation) {
            this.setUserOperation(userOperation, null, null);
        }
        
        public void setUserOperation(final Event.UserOperation userOperation, final List<ShootingLabel.Parameter> list, final Map<String, String> map) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("setUserOperation() : operation = ");
                sb.append(userOperation);
                CamLog.d(sb.toString());
            }
            if (userOperation.isShooting()) {
                Point access$200 = new Point();
                final ArrayMap arrayMap = new ArrayMap();
                Point point = access$200;
                if (this.this$0.mTemporarySettingValues != null) {
                    ((Map<String, String>)arrayMap).put("ManualBurst", this.this$0.mTemporarySettingValues.getManualBurstParameterForIDD());
                    if (this.this$0.mTemporarySettingValues.mCaptureTrigger == Event.CaptureTrigger.SIDE_SENSE) {
                        access$200 = this.this$0.mTemporarySettingValues.mSideSensePosition;
                    }
                    for (final ShootingLabel.Parameter parameter : this.this$0.mTemporarySettingValues.getParameterList()) {
                        ((Map<String, String>)arrayMap).put(parameter.getClass().getSimpleName(), parameter.toString());
                    }
                    point = access$200;
                    if (this.this$0.mTemporarySettingValues.mAssistSelfTimer != -1) {
                        ((Map<String, String>)arrayMap).put("assist_self_timer", String.valueOf(this.this$0.mTemporarySettingValues.mAssistSelfTimer));
                        this.this$0.mTemporarySettingValues.clearAssistSelfTimer();
                        point = access$200;
                    }
                }
                ((Map<String, String>)arrayMap).put("side_sense_position_x", String.valueOf(point.x));
                ((Map<String, String>)arrayMap).put("side_sense_position_y", String.valueOf(point.y));
                if (list != null) {
                    for (final ShootingLabel.Parameter parameter2 : list) {
                        ((Map<String, String>)arrayMap).put(parameter2.getClass().getSimpleName(), parameter2.toString());
                    }
                }
                if (this.this$0.mAfDoneKeepingTimeHolder != null) {
                    final ShootingLabel.Parameter parameter3 = this.this$0.mAfDoneKeepingTimeHolder.getParameter();
                    if (parameter3 != null) {
                        ((Map<String, String>)arrayMap).put(parameter3.getClass().getSimpleName(), parameter3.toString());
                    }
                }
                final ResearchUtil this$0 = this.this$0;
                final Event.Category all_SETTINGS_PHOTO = Event.Category.ALL_SETTINGS_PHOTO;
                Object o;
                if ((o = map) == null) {
                    o = new ArrayMap();
                }
                this$0.sendEventAllSettings(all_SETTINGS_PHOTO, (Map)arrayMap, (Map)o);
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
            this.setUserOperation(this.mUserOperation.getViewer());
        }
    }
    
    private class IncrementCountRecordResumeTask implements Runnable
    {
        final ResearchUtil this$0;
        
        private IncrementCountRecordResumeTask(final ResearchUtil this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mCountRecordResume++;
        }
    }
    
    private class IncrementCountSnapshotInRecordingTask implements Runnable
    {
        final ResearchUtil this$0;
        
        private IncrementCountSnapshotInRecordingTask(final ResearchUtil this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mCountRecordSnapshot++;
        }
    }
    
    private static class OnCreateTask implements Runnable
    {
        @Override
        public void run() {
            IddUtil.onCreate();
        }
    }
    
    private static class OnDestroyTask implements Callable<Boolean>
    {
        private final Context mContextInner;
        
        private OnDestroyTask(final Context mContextInner) {
            this.mContextInner = mContextInner;
        }
        
        @Override
        public Boolean call() {
            IddUtil.onDestroy();
            return true;
        }
    }
    
    private class OnPauseTask implements Runnable
    {
        private final boolean mIsSameActivity;
        final ResearchUtil this$0;
        
        private OnPauseTask(final ResearchUtil this$0, final boolean mIsSameActivity) {
            this.this$0 = this$0;
            this.mIsSameActivity = mIsSameActivity;
        }
        
        @Override
        public void run() {
            this.this$0.mIsViewEventSent = false;
            if (this.this$0.mUserOperationInfo != null) {
                this.this$0.mUserOperationInfo.clear();
            }
            if (this.this$0.mPanoramaInfo != null) {
                this.this$0.mPanoramaInfo.sendPanoramaInfo();
            }
            IddUtil.onPause(this.mIsSameActivity);
            this.this$0.clearTemporarySettingValues();
        }
    }
    
    private class OnResumeTask implements Runnable
    {
        private final boolean mIsPanorama;
        final ResearchUtil this$0;
        
        private OnResumeTask(final ResearchUtil this$0, final boolean mIsPanorama) {
            this.this$0 = this$0;
            this.mIsPanorama = mIsPanorama;
        }
        
        @Override
        public void run() {
            if (this.mIsPanorama) {
                this.this$0.mUserOperationInfo = null;
                this.this$0.mPanoramaInfo = new PanoramaInfo();
            }
            else {
                this.this$0.mUserOperationInfo = new CurrentUserOperationHolder();
                this.this$0.mPanoramaInfo = null;
            }
            IddUtil.onResume();
        }
    }
    
    private static class PanoramaInfo
    {
        private int mSuccessNum;
        private int mTryNum;
        private Event.ViewerLaunched mViewerLaunched;
        
        private PanoramaInfo() {
            this.mTryNum = 0;
            this.mSuccessNum = 0;
            this.mViewerLaunched = Event.ViewerLaunched.NOT_LAUNCHED;
        }
        
        private void sendPanoramaInfo() {
            if (this.mTryNum > 0 || this.mViewerLaunched == Event.ViewerLaunched.LAUNCHED) {
                final int mTryNum = this.mTryNum;
                final int mSuccessNum = this.mSuccessNum;
                final Event.Category panorama = Event.Category.PANORAMA;
                final StringBuilder sb = new StringBuilder();
                sb.append(String.valueOf(mTryNum));
                sb.append("_try");
                final String string = sb.toString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(String.valueOf(mSuccessNum));
                sb2.append("_success");
                IddUtil.sendEvent(panorama, string, sb2.toString(), this.mViewerLaunched.mValue);
            }
            this.clear();
        }
        
        public void clear() {
            if (CamLog.VERBOSE) {
                CamLog.d("PanoramaInfo#clear()");
            }
            this.mTryNum = 0;
            this.mSuccessNum = 0;
            this.mViewerLaunched = Event.ViewerLaunched.NOT_LAUNCHED;
        }
        
        public void setViewerLaunched() {
            if (CamLog.VERBOSE) {
                CamLog.d("setViewerLaunched()");
            }
            this.mViewerLaunched = Event.ViewerLaunched.LAUNCHED;
        }
        
        public void succeedInPanorama() {
            if (CamLog.VERBOSE) {
                CamLog.d("succeedInPanorama()");
            }
            ++this.mSuccessNum;
        }
        
        public void tryPanorama() {
            if (CamLog.VERBOSE) {
                CamLog.d("tryPanorama()");
            }
            ++this.mTryNum;
        }
    }
    
    private class SendCameraNotAvailableEventTask implements Runnable
    {
        final ResearchUtil this$0;
        
        private SendCameraNotAvailableEventTask(final ResearchUtil this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.sendEvent(Event.Category.CAMERA_NOT_AVAILABLE, Event.CameraNotAvailable.getType(this.this$0.mIsFailedToOpenCamera).toString(), null);
            this.this$0.mIsFailedToOpenCamera = false;
        }
    }
    
    private static class SendEventAddonModeChangeTask implements Runnable
    {
        private final String mAction;
        private final Event.Category mCategory;
        private final String mLabel;
        private final String mMethod;
        
        private SendEventAddonModeChangeTask(final Event.Category mCategory, final String mAction, final String mLabel, final String mMethod) {
            this.mCategory = mCategory;
            this.mAction = mAction;
            this.mLabel = mLabel;
            this.mMethod = mMethod;
        }
        
        @Override
        public void run() {
            IddUtil.sendEventAddonModeChange(this.mCategory, this.mAction, this.mLabel, this.mMethod);
        }
    }
    
    private static class SendEventAllSettingsTask implements Runnable
    {
        private final Event.Category mCategory;
        private final Map<String, String> mEnv;
        private final Map<String, String> mSettings;
        
        private SendEventAllSettingsTask(final Event.Category mCategory, final Map<String, String> mEnv, final Map<String, String> mSettings) {
            this.mCategory = mCategory;
            this.mEnv = mEnv;
            this.mSettings = mSettings;
        }
        
        @Override
        public void run() {
            IddUtil.sendEventAllSettings(this.mCategory, this.mEnv, this.mSettings);
        }
    }
    
    private static class SendEventChangedSettingTask implements Runnable
    {
        private final String mAfter;
        private final String mBefore;
        private final String mSetting;
        
        private SendEventChangedSettingTask(final String mSetting, final String mBefore, final String mAfter) {
            this.mSetting = mSetting;
            this.mBefore = mBefore;
            this.mAfter = mAfter;
        }
        
        @Override
        public void run() {
            IddUtil.sendEventChangedSetting(this.mSetting, this.mBefore, this.mAfter);
        }
    }
    
    private static class SendEventInternalModeChangeTask implements Runnable
    {
        private final String mCurrentMode;
        private final String mMethod;
        private final String mTargetMode;
        
        private SendEventInternalModeChangeTask(final String mCurrentMode, final String mTargetMode, final String mMethod) {
            this.mCurrentMode = mCurrentMode;
            this.mTargetMode = mTargetMode;
            this.mMethod = mMethod;
        }
        
        @Override
        public void run() {
            IddUtil.sendEventInternalModeChange(this.mCurrentMode, this.mTargetMode, this.mMethod);
        }
    }
    
    private static class SendEventTask implements Runnable
    {
        private final String mAction;
        private final Event.Category mCategory;
        private final String mLabel;
        private final long mValue;
        
        private SendEventTask(final Event.Category mCategory, final String mAction, final String mLabel, final long mValue) {
            this.mCategory = mCategory;
            this.mAction = mAction;
            this.mLabel = mLabel;
            this.mValue = mValue;
        }
        
        @Override
        public void run() {
            IddUtil.sendEvent(this.mCategory, this.mAction, this.mLabel, this.mValue);
        }
    }
    
    private static class SendExternalCameraAppEventTask implements Runnable
    {
        private final Context mContext;
        private final String mModeTo;
        private final Uri mUri;
        
        private SendExternalCameraAppEventTask(final Context mContext, final Uri mUri, final String mModeTo) {
            this.mContext = mContext;
            this.mUri = mUri;
            this.mModeTo = mModeTo;
        }
        
        @Override
        public void run() {
            final Cursor query = this.mContext.getContentResolver().query(this.mUri, (String[])null, (String)null, (String[])null, (String)null);
            if (query != null) {
                if (query.moveToFirst()) {
                    while (true) {
                        Object o;
                        Object o2;
                        ContentResolver contentResolver;
                        try {
                            try {
                                o = query.getString(query.getColumnIndex("data"));
                                final long long1 = query.getLong(query.getColumnIndex("created_at"));
                                o2 = new JSONObject((String)o);
                                if (this.mModeTo != null) {
                                    ((JSONObject)o2).put("to", (Object)this.mModeTo);
                                }
                                IddUtil.sendExternalCameraAppEvent((JSONObject)o2, long1);
                                contentResolver = this.mContext.getContentResolver();
                                o = this.mUri;
                                o2 = new StringBuilder();
                            }
                            finally {}
                        }
                        catch (final JSONException ex) {
                            ex.printStackTrace();
                            contentResolver = this.mContext.getContentResolver();
                            o = this.mUri;
                            o2 = new StringBuilder();
                        }
                        ((StringBuilder)o2).append("_id = ");
                        ((StringBuilder)o2).append(query.getInt(query.getColumnIndex("_id")));
                        contentResolver.delete((Uri)o, ((StringBuilder)o2).toString(), (String[])null);
                        if (!query.moveToNext()) {
                            break;
                        }
                        continue;
                        final ContentResolver contentResolver2 = this.mContext.getContentResolver();
                        final Uri mUri = this.mUri;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("_id = ");
                        sb.append(query.getInt(query.getColumnIndex("_id")));
                        contentResolver2.delete(mUri, sb.toString(), (String[])null);
                    }
                }
                query.close();
            }
        }
    }
    
    private static class SendPerformanceDataTask implements Runnable
    {
        private final String mBatteryLevel;
        private final boolean mIsHeated;
        private final String mKey;
        private final long mMillis;
        
        private SendPerformanceDataTask(final String mKey, final long mMillis, final boolean mIsHeated, final String mBatteryLevel) {
            this.mKey = mKey;
            this.mMillis = mMillis;
            this.mIsHeated = mIsHeated;
            this.mBatteryLevel = mBatteryLevel;
        }
        
        @Override
        public void run() {
            IddUtil.sendPerformanceData(this.mKey, this.mMillis, this.mIsHeated, this.mBatteryLevel);
        }
    }
    
    private class SendRecordingEventTask implements Runnable
    {
        private final boolean mIsLaunchViewerAfterRec;
        private final int mRecTimeMillis;
        private final Map<String, String> mSettings;
        private final Event.StopOperation mStopOperation;
        private final Event.UserOperation mUserOperation;
        final ResearchUtil this$0;
        
        private SendRecordingEventTask(final ResearchUtil this$0, final Event.UserOperation mUserOperation, final Event.StopOperation mStopOperation, final int mRecTimeMillis, final boolean mIsLaunchViewerAfterRec, final Map<String, String> mSettings) {
            this.this$0 = this$0;
            this.mUserOperation = mUserOperation;
            this.mStopOperation = mStopOperation;
            this.mRecTimeMillis = mRecTimeMillis;
            this.mIsLaunchViewerAfterRec = mIsLaunchViewerAfterRec;
            this.mSettings = mSettings;
        }
        
        @Override
        public void run() {
            final int n = this.mRecTimeMillis / 1000;
            if (n >= 0) {
                if (!this.mIsLaunchViewerAfterRec && this.mUserOperation != null) {
                    this.this$0.setUserOperation(this.mUserOperation);
                }
                Point access$200 = new Point();
                final Event.StopOperation mStopOperation = this.mStopOperation;
                final ArrayMap arrayMap = new ArrayMap();
                Point point = access$200;
                Event.Action action = mStopOperation;
                if (this.this$0.mTemporarySettingValues != null) {
                    Event.Action side_SENSE_STOP = mStopOperation;
                    if (this.this$0.mTemporarySettingValues.mRecordBySideSense) {
                        this.this$0.mTemporarySettingValues.mRecordBySideSense = false;
                        final Point point2 = access$200 = this.this$0.mTemporarySettingValues.mSideSensePosition;
                        if ((side_SENSE_STOP = mStopOperation) == Event.StopOperation.USER_STOP) {
                            side_SENSE_STOP = Event.StopOperation.SIDE_SENSE_STOP;
                            access$200 = point2;
                        }
                    }
                    final Iterator<ShootingLabel.Parameter> iterator = this.this$0.mTemporarySettingValues.getParameterListForRecording().iterator();
                    while (true) {
                        point = access$200;
                        action = side_SENSE_STOP;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        final ShootingLabel.Parameter parameter = iterator.next();
                        ((Map<String, String>)arrayMap).put(parameter.getClass().getSimpleName(), parameter.toString());
                    }
                }
                ((Map<String, String>)arrayMap).put("side_sense_position_x", String.valueOf(point.x));
                ((Map<String, String>)arrayMap).put("side_sense_position_y", String.valueOf(point.y));
                ((Map<String, String>)arrayMap).put("rec_time", String.valueOf(n));
                ((Map<String, String>)arrayMap).put("resume", String.valueOf(this.this$0.mCountRecordResume));
                ((Map<String, String>)arrayMap).put("snapshot", String.valueOf(this.this$0.mCountRecordSnapshot));
                ((Map<String, String>)arrayMap).put("stop_factor", ((Enum)action).toString());
                if (this.mSettings != null && "OFF".equals(this.mSettings.get("SLOW_MOTION")) && !TextUtils.isEmpty((CharSequence)this.mSettings.get("VIDEO_SIZE"))) {
                    ((Map<String, String>)arrayMap).put("video_size", this.mSettings.get("VIDEO_SIZE"));
                }
                else {
                    ((Map<String, String>)arrayMap).put("video_size", "Other");
                }
                final ResearchUtil this$0 = this.this$0;
                final Event.Category all_SETTINGS_VIDEO = Event.Category.ALL_SETTINGS_VIDEO;
                Object mSettings;
                if (this.mSettings == null) {
                    mSettings = new ArrayMap();
                }
                else {
                    mSettings = this.mSettings;
                }
                this$0.sendEventAllSettings(all_SETTINGS_VIDEO, (Map)arrayMap, (Map)mSettings);
                final StringBuffer sb = new StringBuffer();
                sb.append(String.valueOf(n));
                sb.append("_Sec_");
                sb.append(String.valueOf(this.this$0.mCountRecordResume));
                sb.append("_Resume_");
                sb.append(String.valueOf(this.this$0.mCountRecordSnapshot));
                sb.append("_Snapshot_");
                this.this$0.mCountRecordResume = 0;
                this.this$0.mCountRecordSnapshot = 0;
                if (this.this$0.mTemporarySettingValues != null) {
                    sb.append(this.this$0.mTemporarySettingValues.toStringForRecording());
                    this.this$0.mTemporarySettingValues.clearRecordingMaxFaceNum();
                }
                if (this.mSettings != null && "OFF".equals(this.mSettings.get("SLOW_MOTION")) && !TextUtils.isEmpty((CharSequence)this.mSettings.get("VIDEO_SIZE"))) {
                    sb.append(getVideoSizeEventLabel(this.mSettings.get("VIDEO_SIZE")));
                }
                else {
                    sb.append(getVideoSizeEventLabel("Other"));
                }
                this.this$0.sendEvent(Event.Category.RECORDING, ((Enum)action).toString(), sb.toString());
            }
        }
    }
    
    private class SendSettingsCommonKeyTask implements Runnable
    {
        private final String mKey;
        final ResearchUtil this$0;
        
        private SendSettingsCommonKeyTask(final ResearchUtil this$0, final String mKey) {
            this.this$0 = this$0;
            this.mKey = mKey;
        }
        
        @Override
        public void run() {
            this.this$0.sendEvent(Event.Category.SETTINGS_COMMON, this.mKey, null);
        }
    }
    
    private class SendSettingsCommonValueTask implements Runnable
    {
        private final UserSettingValue mValue;
        final ResearchUtil this$0;
        
        private SendSettingsCommonValueTask(final ResearchUtil this$0, final UserSettingValue mValue) {
            this.this$0 = this$0;
            this.mValue = mValue;
        }
        
        @Override
        public void run() {
            switch (ResearchUtil$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[this.mValue.getKey().ordinal()]) {
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
                case 11:
                case 12:
                case 13: {
                    this.this$0.sendEvent(Event.Category.SETTINGS_COMMON, this.mValue.getKey().toString(), this.mValue.toString());
                    break;
                }
            }
        }
    }
    
    private class SendViewTask implements Runnable
    {
        private final LaunchCondition.LaunchTrigger mLaunchTrigger;
        private final Screen mScreen;
        final ResearchUtil this$0;
        
        private SendViewTask(final ResearchUtil this$0, final LaunchCondition.LaunchTrigger mLaunchTrigger, final Screen mScreen) {
            this.this$0 = this$0;
            this.mLaunchTrigger = mLaunchTrigger;
            this.mScreen = mScreen;
        }
        
        @Override
        public void run() {
            if (!this.this$0.mIsViewEventSent || this.mLaunchTrigger == LaunchCondition.LaunchTrigger.SAME_ACTIVITY) {
                final StringBuilder sb = new StringBuilder();
                sb.append("SendViewTask() LaunchTrigger:");
                sb.append(this.mLaunchTrigger);
                CamLog.d(sb.toString());
                IddUtil.setLaunchedBy(this.mLaunchTrigger.toString());
                setLaunchTriggerEvent(this.this$0.mContext, this.mLaunchTrigger);
                IddUtil.setView(this.mScreen);
                this.this$0.mIsViewEventSent = true;
            }
        }
    }
    
    private class SetCaptureTriggerTask implements Runnable
    {
        private final Event.CaptureTrigger mCaptureTrigger;
        final ResearchUtil this$0;
        
        public SetCaptureTriggerTask(final ResearchUtil this$0, final Event.CaptureTrigger mCaptureTrigger) {
            this.this$0 = this$0;
            this.mCaptureTrigger = mCaptureTrigger;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updateCaptureTrigger(this.mCaptureTrigger);
        }
    }
    
    private class SetContinuousCaptureTask implements Runnable
    {
        final ResearchUtil this$0;
        
        private SetContinuousCaptureTask(final ResearchUtil this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (this.this$0.mAfDoneKeepingTimeHolder == null) {
                this.this$0.mAfDoneKeepingTimeHolder = new AfDoneKeepingTimeHolder();
            }
            this.this$0.mAfDoneKeepingTimeHolder.updateContinuousCapture(true);
        }
    }
    
    private class SetFaceNumTask implements Runnable
    {
        private final int mNum;
        final ResearchUtil this$0;
        
        public SetFaceNumTask(final ResearchUtil this$0, final int mNum) {
            this.this$0 = this$0;
            this.mNum = mNum;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updateFaceNum(this.mNum);
        }
    }
    
    private class SetManualBurstCountTask implements Runnable
    {
        private final int mManualBurstCount;
        final ResearchUtil this$0;
        
        public SetManualBurstCountTask(final ResearchUtil this$0, final int mManualBurstCount) {
            this.this$0 = this$0;
            this.mManualBurstCount = mManualBurstCount;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updateManualBurstCount(this.mManualBurstCount);
        }
    }
    
    private class SetOrientationTask implements Runnable
    {
        private final int mOrientation;
        final ResearchUtil this$0;
        
        public SetOrientationTask(final ResearchUtil this$0, final int mOrientation) {
            this.this$0 = this$0;
            this.mOrientation = mOrientation;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updateOrientation(this.mOrientation);
        }
    }
    
    private class SetPredictiveCaptureNumTask implements Runnable
    {
        private final int mPredictiveCaptureNum;
        final ResearchUtil this$0;
        
        public SetPredictiveCaptureNumTask(final ResearchUtil this$0, final int mPredictiveCaptureNum) {
            this.this$0 = this$0;
            this.mPredictiveCaptureNum = mPredictiveCaptureNum;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updatePredictiveCaptureNum(this.mPredictiveCaptureNum);
        }
    }
    
    private class SetRecordBySideSenseTask implements Runnable
    {
        private final boolean mRecordBySideSense;
        final ResearchUtil this$0;
        
        public SetRecordBySideSenseTask(final ResearchUtil this$0, final boolean mRecordBySideSense) {
            this.this$0 = this$0;
            this.mRecordBySideSense = mRecordBySideSense;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updateRecordBySideSense(this.mRecordBySideSense);
        }
    }
    
    private class SetRecordingMaxFaceNumTask implements Runnable
    {
        private final int mNum;
        final ResearchUtil this$0;
        
        public SetRecordingMaxFaceNumTask(final ResearchUtil this$0, final int mNum) {
            this.this$0 = this$0;
            this.mNum = mNum;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updateRecordingMaxFaceNum(this.mNum);
        }
    }
    
    private class SetSideSensePositionTask implements Runnable
    {
        private final int mX;
        private final int mY;
        final ResearchUtil this$0;
        
        public SetSideSensePositionTask(final ResearchUtil this$0, final int mx, final int my) {
            this.this$0 = this$0;
            this.mX = mx;
            this.mY = my;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updateSideSensePosition(this.mX, this.mY);
        }
    }
    
    private class SetTimeTask implements Runnable
    {
        private final boolean mIsStart;
        private final long mTime;
        final ResearchUtil this$0;
        
        public SetTimeTask(final ResearchUtil this$0, final boolean mIsStart, final long mTime) {
            this.this$0 = this$0;
            this.mIsStart = mIsStart;
            this.mTime = mTime;
        }
        
        @Override
        public void run() {
            if (this.this$0.mAfDoneKeepingTimeHolder == null) {
                this.this$0.mAfDoneKeepingTimeHolder = new AfDoneKeepingTimeHolder();
            }
            if (this.mIsStart) {
                this.this$0.mAfDoneKeepingTimeHolder.updateTimeAfDone(this.mTime);
            }
            else {
                this.this$0.mAfDoneKeepingTimeHolder.updateTimeCapture(this.mTime);
            }
        }
    }
    
    private class SetUserOperationTask implements Runnable
    {
        private final List<ShootingLabel.Parameter> mParams;
        private final Map<String, String> mSettings;
        private final Event.UserOperation mUserOperation;
        final ResearchUtil this$0;
        
        private SetUserOperationTask(final ResearchUtil this$0, final Event.UserOperation mUserOperation, final List<ShootingLabel.Parameter> mParams, final Map<String, String> mSettings) {
            this.this$0 = this$0;
            this.mUserOperation = mUserOperation;
            this.mParams = mParams;
            this.mSettings = mSettings;
        }
        
        @Override
        public void run() {
            if (this.this$0.mUserOperationInfo != null) {
                this.this$0.mUserOperationInfo.setUserOperation(this.mUserOperation, this.mParams, this.mSettings);
            }
        }
    }
    
    private class SetViewerLaunchedTask implements Runnable
    {
        final ResearchUtil this$0;
        
        private SetViewerLaunchedTask(final ResearchUtil this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (this.this$0.mUserOperationInfo != null) {
                this.this$0.mUserOperationInfo.setViewerLaunched();
            }
            if (this.this$0.mPanoramaInfo != null) {
                this.this$0.mPanoramaInfo.setViewerLaunched();
            }
        }
    }
    
    private class SetZoomRatioTask implements Runnable
    {
        private final float mZoomRatio;
        final ResearchUtil this$0;
        
        public SetZoomRatioTask(final ResearchUtil this$0, final float mZoomRatio) {
            this.this$0 = this$0;
            this.mZoomRatio = mZoomRatio;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updateZoomRatio(this.mZoomRatio);
        }
    }
    
    private static class TemporarySettingValues
    {
        private int mAssistSelfTimer;
        private Event.CaptureTrigger mCaptureTrigger;
        private int mFaceNum;
        private int mManualBurstCount;
        private int mOrientation;
        private int mPredictiveCaptureNum;
        private boolean mRecordBySideSense;
        private int mRecordingMaxFaceNum;
        private Point mSideSensePosition;
        private float mZoomRatio;
        
        public TemporarySettingValues() {
            this.mZoomRatio = 1.0f;
            this.mOrientation = 0;
            this.mFaceNum = 0;
            this.mRecordingMaxFaceNum = 0;
            this.mPredictiveCaptureNum = 0;
            this.mManualBurstCount = 0;
            this.mCaptureTrigger = Event.CaptureTrigger.OTHER;
            this.mSideSensePosition = new Point();
            this.mAssistSelfTimer = -1;
        }
        
        private int roundZoomRatio() {
            if (this.mZoomRatio - 0.05f < 1.0f) {
                return 0;
            }
            return (int)this.mZoomRatio;
        }
        
        public void clearAssistSelfTimer() {
            this.mAssistSelfTimer = -1;
        }
        
        public void clearCaptureTrigger() {
            this.mCaptureTrigger = Event.CaptureTrigger.OTHER;
        }
        
        public void clearFaceNum() {
            this.mFaceNum = 0;
        }
        
        public void clearRecordingMaxFaceNum() {
            this.mRecordingMaxFaceNum = 0;
        }
        
        public String getManualBurstParameterForIDD() {
            if (this.mManualBurstCount == 0) {
                return "False";
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(this.mManualBurstCount));
            sb.append("_Shots");
            return sb.toString();
        }
        
        public List<ShootingLabel.Parameter> getParameterList() {
            final ArrayList list = new ArrayList();
            list.add(ShootingLabel.getOrientationParameter(this.mOrientation));
            list.add(ShootingLabel.getZoomParameter(this.roundZoomRatio()));
            list.add(ShootingLabel.getFaceNumParameter(this.mFaceNum));
            list.add(ShootingLabel.getPredictiveCaptureNumParameter(this.mPredictiveCaptureNum));
            list.add(ShootingLabel.getCaptureTriggerParameter(this.mCaptureTrigger.toString()));
            return list;
        }
        
        public List<ShootingLabel.Parameter> getParameterListForRecording() {
            final ArrayList list = new ArrayList();
            list.add(ShootingLabel.getOrientationParameter(this.mOrientation));
            list.add(ShootingLabel.getZoomParameter(this.roundZoomRatio()));
            list.add(ShootingLabel.getFaceNumParameter(this.mRecordingMaxFaceNum));
            return list;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Z");
            sb.append(this.roundZoomRatio());
            sb.append("_O");
            sb.append(this.mOrientation);
            sb.append("_F");
            sb.append(this.mFaceNum);
            return sb.toString();
        }
        
        public String toStringForRecording() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Z");
            sb.append(this.roundZoomRatio());
            sb.append("_O");
            sb.append(this.mOrientation);
            sb.append("_F");
            sb.append(this.mRecordingMaxFaceNum);
            return sb.toString();
        }
        
        public void updateAssistSelfTimer(final int mAssistSelfTimer) {
            this.mAssistSelfTimer = mAssistSelfTimer;
        }
        
        public void updateCaptureTrigger(final Event.CaptureTrigger mCaptureTrigger) {
            this.mCaptureTrigger = mCaptureTrigger;
        }
        
        public void updateFaceNum(final int mFaceNum) {
            this.mFaceNum = mFaceNum;
        }
        
        public void updateManualBurstCount(final int mManualBurstCount) {
            this.mManualBurstCount = mManualBurstCount;
        }
        
        public void updateOrientation(final int mOrientation) {
            this.mOrientation = mOrientation;
        }
        
        public void updatePredictiveCaptureNum(final int mPredictiveCaptureNum) {
            this.mPredictiveCaptureNum = mPredictiveCaptureNum;
        }
        
        public void updateRecordBySideSense(final boolean mRecordBySideSense) {
            this.mRecordBySideSense = mRecordBySideSense;
        }
        
        public void updateRecordingMaxFaceNum(final int mRecordingMaxFaceNum) {
            if (this.mRecordingMaxFaceNum < mRecordingMaxFaceNum) {
                this.mRecordingMaxFaceNum = mRecordingMaxFaceNum;
            }
        }
        
        public void updateSideSensePosition(final int n, final int n2) {
            this.mSideSensePosition = new Point(n, n2);
        }
        
        public void updateZoomRatio(final float mZoomRatio) {
            this.mZoomRatio = mZoomRatio;
        }
    }
    
    private class UpdateFailedToOpenCameraFlagTask implements Runnable
    {
        final ResearchUtil this$0;
        
        private UpdateFailedToOpenCameraFlagTask(final ResearchUtil this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mIsFailedToOpenCamera = true;
        }
    }
    
    public enum VideoSize
    {
        private static final VideoSize[] $VALUES;
        
        FOUR_K_UHD_H264, 
        FOUR_K_UHD_H265, 
        FULL_HD, 
        FULL_HD_60FPS, 
        FWVGA, 
        HD, 
        MMS, 
        QVGA, 
        VGA;
        
        static {
            $VALUES = new VideoSize[] { VideoSize.FULL_HD, VideoSize.FULL_HD_60FPS, VideoSize.HD, VideoSize.VGA, VideoSize.FWVGA, VideoSize.QVGA, VideoSize.MMS, VideoSize.FOUR_K_UHD_H264, VideoSize.FOUR_K_UHD_H265 };
        }
        
        public static VideoSize getVideoSize(final String s) {
            return valueOf(s);
        }
    }
    
    private enum XperiaTipsLaunchTrigger
    {
        private static final XperiaTipsLaunchTrigger[] $VALUES;
        private static final String EVENT_NAME = "launch_trigger";
        
        HOME_APP, 
        HW_KEY_OTHER, 
        HW_KEY_SLEEP_OR_LOCK, 
        NOT_TARGET, 
        POWER_KEY_DOUBLE_PRESS, 
        SHORTCUT_LOCKSCREEN;
        
        static {
            $VALUES = new XperiaTipsLaunchTrigger[] { XperiaTipsLaunchTrigger.HW_KEY_SLEEP_OR_LOCK, XperiaTipsLaunchTrigger.HW_KEY_OTHER, XperiaTipsLaunchTrigger.SHORTCUT_LOCKSCREEN, XperiaTipsLaunchTrigger.POWER_KEY_DOUBLE_PRESS, XperiaTipsLaunchTrigger.HOME_APP, XperiaTipsLaunchTrigger.NOT_TARGET };
        }
        
        private static void setLaunchTriggerEvent(final Context context, final LaunchCondition.LaunchTrigger launchTrigger) {
            XperiaTipsLaunchTrigger xperiaTipsLaunchTrigger;
            if (launchTrigger == LaunchCondition.LaunchTrigger.HW_CAMERA_KEY_LOCK) {
                xperiaTipsLaunchTrigger = XperiaTipsLaunchTrigger.HW_KEY_SLEEP_OR_LOCK;
            }
            else if (launchTrigger == LaunchCondition.LaunchTrigger.HW_CAMERA_KEY) {
                xperiaTipsLaunchTrigger = XperiaTipsLaunchTrigger.HW_KEY_OTHER;
            }
            else if (launchTrigger == LaunchCondition.LaunchTrigger.LOCK_SCREEN) {
                xperiaTipsLaunchTrigger = XperiaTipsLaunchTrigger.SHORTCUT_LOCKSCREEN;
            }
            else if (launchTrigger == LaunchCondition.LaunchTrigger.POWER_KEY_DOUBLE_TAP) {
                xperiaTipsLaunchTrigger = XperiaTipsLaunchTrigger.POWER_KEY_DOUBLE_PRESS;
            }
            else {
                if (launchTrigger != LaunchCondition.LaunchTrigger.HOME) {
                    return;
                }
                xperiaTipsLaunchTrigger = XperiaTipsLaunchTrigger.HOME_APP;
            }
            try {
                final ContentValues contentValues = new ContentValues();
                contentValues.put("name", "launch_trigger");
                contentValues.put("data", xperiaTipsLaunchTrigger.toString());
                context.getContentResolver().insert(ContributionContract$Event.CONTENT_URI, contentValues);
            }
            catch (final Exception obj) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed setting data to XPERIA Tips : ");
                    sb.append(obj);
                    CamLog.e(sb.toString());
                }
            }
            catch (final NoClassDefFoundError obj2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Not found getmore api : ");
                sb2.append(obj2);
                CamLog.e(sb2.toString());
            }
        }
    }
    
    private static class sendWizardEventTask implements Runnable
    {
        private final String mPage;
        private final String mResult;
        private final long mTime;
        
        private sendWizardEventTask(final Event.WizardPage wizardPage, final Event.WizardResult wizardResult, final long mTime) {
            this.mPage = wizardPage.toString();
            this.mResult = wizardResult.toString();
            this.mTime = mTime;
        }
        
        @Override
        public void run() {
            IddUtil.sendWizardEvent(this.mPage, String.valueOf(this.mTime), this.mResult);
        }
    }
    
    private class setAssistSelfTimerTask implements Runnable
    {
        private final int mAssistSelfTimer;
        final ResearchUtil this$0;
        
        public setAssistSelfTimerTask(final ResearchUtil this$0, final int mAssistSelfTimer) {
            this.this$0 = this$0;
            this.mAssistSelfTimer = mAssistSelfTimer;
        }
        
        @Override
        public void run() {
            if (this.this$0.mTemporarySettingValues == null) {
                this.this$0.mTemporarySettingValues = new TemporarySettingValues();
            }
            this.this$0.mTemporarySettingValues.updateAssistSelfTimer(this.mAssistSelfTimer);
        }
    }
}
