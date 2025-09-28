package com.sonyericsson.android.camera.util.capability;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Range;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.cameracommon.constants.CommonConstants;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PlatformCapability {
    private static int CAPABILITY_VERSION = 1;
    private static final String FILE_NAME = "com.sonyericsson.android.camera.supported_values.";
    private static final String KEY_VERSION = "capability-version";
    private static final String PLATFORM_NAME = "platform";
    private static final long PREPARING_START_DELAY = 2000;
    private static final long PREPARING_TIMEOUT = 1000;
    private static boolean mHasDeviceError;
    private static Map<CameraInfo.CameraId, CameraCapabilityList> sCameraCapabilityListMap;
    private static CameraManager sCameraManager;
    private static PlatformCapabilityList sPlatformCapabilityList;
    private static CountDownLatch sPrepareLatch;
    private static Future<Map<HolderType, ParameterHolder>> sPreparingTaskFuture;
    private static Map<HolderType, ParameterHolder> sParameterHolderMap = new HashMap();
    public static final String TAG = "PlatformCapability";
    private static final ScheduledExecutorService sPrepareTaskExecutor = ThreadUtil.buildScheduledExecutor(TAG, 10);
    private static PrepareState sPrepareState = PrepareState.IDLE;
    private static Object sPrepareStateLock = new Object();

    private enum HolderType {
        MEDIA_CODEC
    }

    public interface OnPlatformCapabilityPreparedCallback {
        void onPrepared();
    }

    enum PrepareState {
        IDLE,
        RUNNING,
        TIMED_OUT
    }

    public static String getFileNameForPlatformCapability() {
        return "com.sonyericsson.android.camera.supported_values.platform";
    }

    public static boolean isVideoSnapshotSupported(int i) {
        return true;
    }

    public static boolean isVideoStabilizerOnHdrSupported(CameraInfo.CameraId cameraId) {
        return true;
    }

    public static String getFileNameForCameraCapability(CameraInfo.CameraId cameraId) {
        return FILE_NAME + cameraId.getCameraDeviceId();
    }

    private static class PrepareParametersTask implements Callable<Map<HolderType, ParameterHolder>> {
        private PrepareParametersTask() {
        }

        @Override // java.util.concurrent.Callable
        public Map<HolderType, ParameterHolder> call() throws Exception {
            if (CamLog.DEBUG) {
                CamLog.d("PrepareParametersTask: E");
            }
            HashMap map = new HashMap();
            map.put(HolderType.MEDIA_CODEC, new MediaCodecParametersHolder());
            Iterator it = map.values().iterator();
            while (it.hasNext()) {
                ((ParameterHolder) it.next()).prepare();
            }
            if (CamLog.DEBUG) {
                CamLog.d("PrepareParametersTask: X");
            }
            return map;
        }
    }

    public static void prepareAsync(OnPlatformCapabilityPreparedCallback onPlatformCapabilityPreparedCallback) {
        boolean zHasDeviceError = hasDeviceError();
        setDeviceError(false);
        synchronized (sPrepareStateLock) {
            if (CamLog.DEBUG) {
                CamLog.d("Invoked state:" + sPrepareState);
            }
            if (zHasDeviceError) {
                sPrepareState = PrepareState.IDLE;
                if (sPlatformCapabilityList != null) {
                    sPlatformCapabilityList = null;
                }
                if (sCameraCapabilityListMap != null) {
                    sCameraCapabilityListMap.clear();
                    sCameraCapabilityListMap = null;
                }
            }
            if (AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[sPrepareState.ordinal()] != 1) {
                sPrepareState = PrepareState.RUNNING;
                sPrepareLatch = new CountDownLatch(1);
                sPrepareTaskExecutor.execute(new PrepareTask(sPrepareLatch, onPlatformCapabilityPreparedCallback));
            }
        }
    }

    private static class PrepareTask implements Runnable {
        private OnPlatformCapabilityPreparedCallback mCallback;
        private final CountDownLatch mLatch;

        private PrepareTask(CountDownLatch countDownLatch, OnPlatformCapabilityPreparedCallback onPlatformCapabilityPreparedCallback) {
            this.mLatch = countDownLatch;
            this.mCallback = onPlatformCapabilityPreparedCallback;
        }

        @Override // java.lang.Runnable
        public void run() {
            PlatformCapability.prepareInternal(this.mLatch, this.mCallback);
        }
    }

    public static boolean awaitPrepare() {
        return awaitPrepare(0L, TimeUnit.MILLISECONDS);
    }

    public static boolean awaitPrepare(long j, TimeUnit timeUnit) throws InterruptedException {
        synchronized (sPrepareStateLock) {
            if (CamLog.DEBUG) {
                CamLog.d("Invoked state:" + sPrepareState);
            }
            switch (sPrepareState) {
                case TIMED_OUT:
                case IDLE:
                    return true;
                default:
                    CountDownLatch countDownLatch = sPrepareLatch;
                    if (countDownLatch == null) {
                        CamLog.e("Latch object for preparation of platform capability doesn't exist.");
                        return true;
                    }
                    try {
                        if (j > 0) {
                            if (!countDownLatch.await(j, timeUnit)) {
                                CamLog.e("Preparation of platform capability is timed-out.");
                            }
                        } else {
                            countDownLatch.await();
                        }
                    } catch (InterruptedException unused) {
                        CamLog.e("Preparation of platform capability is interrupted.");
                    }
                    synchronized (sPrepareStateLock) {
                        if (AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$util$capability$PlatformCapability$PrepareState[sPrepareState.ordinal()] == 3) {
                            return true;
                        }
                        if (CamLog.DEBUG) {
                            CamLog.d("Fail state:" + sPrepareState);
                        }
                        if (countDownLatch == sPrepareLatch) {
                            sPrepareState = PrepareState.TIMED_OUT;
                        }
                        return false;
                    }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void prepareInternal(CountDownLatch countDownLatch, OnPlatformCapabilityPreparedCallback onPlatformCapabilityPreparedCallback) {
        boolean z;
        PerfLog.PLATFORM_CAPABILITY_PREPARE.begin();
        Context context = CameraApplication.getContext();
        CameraManager cameraManager = (CameraManager) context.getSystemService(CameraManager.class);
        HashMap map = new HashMap();
        try {
            List<CameraInfo.CameraId> cameraIdList = getCameraIdList(cameraManager);
            if (cameraIdList != null && !cameraIdList.isEmpty()) {
                boolean z2 = false;
                for (CameraInfo.CameraId cameraId : cameraIdList) {
                    CameraCapabilityList cameraCapabilityListLoadCameraCapabilityFromPreferences = loadCameraCapabilityFromPreferences(context, cameraId);
                    if (cameraCapabilityListLoadCameraCapabilityFromPreferences == null) {
                        cameraCapabilityListLoadCameraCapabilityFromPreferences = loadCameraCapabilityFromDevice(context, cameraManager, cameraId);
                        z2 = true;
                    }
                    if (cameraCapabilityListLoadCameraCapabilityFromPreferences == null) {
                        CamLog.e("Platform capability could not load cameraCapabilityList for camera:" + cameraId.name());
                        synchronized (sPrepareStateLock) {
                            switch (sPrepareState) {
                                case RUNNING:
                                    if (countDownLatch == sPrepareLatch) {
                                        sPrepareState = PrepareState.IDLE;
                                        break;
                                    }
                                    break;
                                case TIMED_OUT:
                                case IDLE:
                                    CamLog.w("Preparation of platform capability is finished with Illegal state. state:" + sPrepareState);
                                    break;
                            }
                            countDownLatch.countDown();
                        }
                        onPlatformCapabilityPreparedCallback.onPrepared();
                        if (CamLog.DEBUG) {
                            CamLog.d("prepare: request preparing parameters");
                        }
                        sPreparingTaskFuture = sPrepareTaskExecutor.schedule(new PrepareParametersTask(), PREPARING_START_DELAY, TimeUnit.MILLISECONDS);
                        return;
                    }
                    map.put(cameraId, cameraCapabilityListLoadCameraCapabilityFromPreferences);
                }
                PlatformCapabilityList platformCapabilityListLoadPlatformCapabilityFromPreferences = loadPlatformCapabilityFromPreferences(context);
                if (platformCapabilityListLoadPlatformCapabilityFromPreferences == null) {
                    platformCapabilityListLoadPlatformCapabilityFromPreferences = loadPlatformCapabilityFromDevice(context);
                    z = true;
                } else {
                    z = false;
                }
                if (platformCapabilityListLoadPlatformCapabilityFromPreferences == null) {
                    synchronized (sPrepareStateLock) {
                        switch (sPrepareState) {
                            case RUNNING:
                                if (countDownLatch == sPrepareLatch) {
                                    sPrepareState = PrepareState.IDLE;
                                    break;
                                }
                                break;
                            case TIMED_OUT:
                            case IDLE:
                                CamLog.w("Preparation of platform capability is finished with Illegal state. state:" + sPrepareState);
                                break;
                        }
                        countDownLatch.countDown();
                    }
                    onPlatformCapabilityPreparedCallback.onPrepared();
                    if (CamLog.DEBUG) {
                        CamLog.d("prepare: request preparing parameters");
                    }
                    sPreparingTaskFuture = sPrepareTaskExecutor.schedule(new PrepareParametersTask(), PREPARING_START_DELAY, TimeUnit.MILLISECONDS);
                    return;
                }
                synchronized (sPrepareStateLock) {
                    switch (sPrepareState) {
                        case RUNNING:
                            if (countDownLatch == sPrepareLatch) {
                                sPrepareState = PrepareState.IDLE;
                                sCameraManager = cameraManager;
                                sCameraCapabilityListMap = map;
                                sPlatformCapabilityList = platformCapabilityListLoadPlatformCapabilityFromPreferences;
                                if (z2) {
                                    for (CameraInfo.CameraId cameraId2 : sCameraCapabilityListMap.keySet()) {
                                        store(context, getFileNameForCameraCapability(cameraId2), sCameraCapabilityListMap.get(cameraId2).values());
                                    }
                                }
                                if (z && sPlatformCapabilityList != null) {
                                    store(context, getFileNameForPlatformCapability(), sPlatformCapabilityList.values());
                                    break;
                                }
                            }
                            break;
                        case TIMED_OUT:
                        case IDLE:
                            CamLog.w("Preparation of platform capability is finished with Illegal state. state:" + sPrepareState);
                            break;
                    }
                    countDownLatch.countDown();
                }
                onPlatformCapabilityPreparedCallback.onPrepared();
                if (CamLog.DEBUG) {
                    CamLog.d("prepare: request preparing parameters");
                }
                sPreparingTaskFuture = sPrepareTaskExecutor.schedule(new PrepareParametersTask(), PREPARING_START_DELAY, TimeUnit.MILLISECONDS);
                PerfLog.PLATFORM_CAPABILITY_PREPARE.end();
                return;
            }
            CamLog.e("Camera list could not be retrieved from camera device.");
            synchronized (sPrepareStateLock) {
                switch (sPrepareState) {
                    case RUNNING:
                        if (countDownLatch == sPrepareLatch) {
                            sPrepareState = PrepareState.IDLE;
                            break;
                        }
                        break;
                    case TIMED_OUT:
                    case IDLE:
                        CamLog.w("Preparation of platform capability is finished with Illegal state. state:" + sPrepareState);
                        break;
                }
                countDownLatch.countDown();
            }
            onPlatformCapabilityPreparedCallback.onPrepared();
            if (CamLog.DEBUG) {
                CamLog.d("prepare: request preparing parameters");
            }
            sPreparingTaskFuture = sPrepareTaskExecutor.schedule(new PrepareParametersTask(), PREPARING_START_DELAY, TimeUnit.MILLISECONDS);
        } catch (Throwable th) {
            synchronized (sPrepareStateLock) {
                switch (sPrepareState) {
                    case RUNNING:
                        if (countDownLatch == sPrepareLatch) {
                            sPrepareState = PrepareState.IDLE;
                            break;
                        }
                        break;
                    case TIMED_OUT:
                    case IDLE:
                        CamLog.w("Preparation of platform capability is finished with Illegal state. state:" + sPrepareState);
                        break;
                }
                countDownLatch.countDown();
                onPlatformCapabilityPreparedCallback.onPrepared();
                if (CamLog.DEBUG) {
                    CamLog.d("prepare: request preparing parameters");
                }
                sPreparingTaskFuture = sPrepareTaskExecutor.schedule(new PrepareParametersTask(), PREPARING_START_DELAY, TimeUnit.MILLISECONDS);
                throw th;
            }
        }
    }

    public static boolean isPrepared() {
        synchronized (sPrepareStateLock) {
            boolean z = false;
            switch (sPrepareState) {
                case TIMED_OUT:
                    return false;
                case IDLE:
                    if (sCameraCapabilityListMap != null && sPlatformCapabilityList != null) {
                        z = true;
                    }
                    return z;
                default:
                    return false;
            }
        }
    }

    private static List<CameraInfo.CameraId> getCameraIdList(CameraManager cameraManager) throws CameraAccessException {
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (String str : cameraIdList) {
                if (str.equals(CameraInfo.CameraId.BACK.getCameraDeviceId())) {
                    arrayList.add(CameraInfo.CameraId.BACK);
                } else if (str.equals(CameraInfo.CameraId.FRONT.getCameraDeviceId())) {
                    arrayList.add(CameraInfo.CameraId.FRONT);
                }
            }
            return arrayList;
        } catch (CameraAccessException e) {
            CamLog.e("Camera Ids could not be retrieved from CameraManager.", e);
            return null;
        }
    }

    public static CameraCapabilityList getCameraCapability(CameraInfo.CameraId cameraId) {
        return getList(cameraId);
    }

    private static CameraCapabilityList getList(CameraInfo.CameraId cameraId) {
        if (!isPrepared()) {
            throw new IllegalArgumentException("PlatformCapability is not prepared.");
        }
        if (cameraId == null) {
            throw new IllegalArgumentException("CameraId is null");
        }
        if (sCameraCapabilityListMap.containsKey(cameraId)) {
            return sCameraCapabilityListMap.get(cameraId);
        }
        throw new IllegalArgumentException("Capabilities are not cached for camera:" + cameraId.name());
    }

    public static Rect getMaxPixelsPictureSize(CameraInfo.CameraId cameraId) {
        Rect rect = null;
        for (Rect rect2 : getList(cameraId).PICTURE_SIZE.get()) {
            if (pixels(rect2) > pixels(rect)) {
                rect = rect2;
            }
        }
        return new Rect(0, 0, rect.height(), rect.width());
    }

    private static int pixels(Rect rect) {
        if (rect == null) {
            return 0;
        }
        return rect.height() * rect.width();
    }

    private static CameraCapabilityList loadCameraCapabilityFromPreferences(Context context, CameraInfo.CameraId cameraId) {
        CamLog.d("invoked cameraId:" + cameraId.name());
        SharedPreferences sharedPreferencesLoadPreferences = loadPreferences(context, getFileNameForCameraCapability(cameraId));
        if (sharedPreferencesLoadPreferences == null) {
            return null;
        }
        return new CameraCapabilityList(context, sharedPreferencesLoadPreferences);
    }

    private static PlatformCapabilityList loadPlatformCapabilityFromPreferences(Context context) {
        SharedPreferences sharedPreferencesLoadPreferences = loadPreferences(context, getFileNameForPlatformCapability());
        if (sharedPreferencesLoadPreferences == null) {
            return null;
        }
        return new PlatformCapabilityList(context, sharedPreferencesLoadPreferences);
    }

    private static SharedPreferences loadPreferences(Context context, String str) {
        SharedPreferences sharedPreferences = SharedPreferencesAccessor.getSharedPreferences(context, str, 0);
        if (sharedPreferences == null) {
            CamLog.d("shared-preferences is not valid: name = " + str);
            return null;
        }
        if (isSharedPreferencesValid(sharedPreferences)) {
            CamLog.d("loaded from shared-preferences: name = " + str);
            return sharedPreferences;
        }
        CamLog.i("shared-preferences is not valid: name = " + str);
        sharedPreferences.edit().clear().commit();
        return null;
    }

    private static CameraCapabilityList loadCameraCapabilityFromDevice(Context context, CameraManager cameraManager, CameraInfo.CameraId cameraId) {
        CamLog.d("invoked cameraId:" + cameraId.name());
        try {
            CameraCapabilityList cameraCapabilityList = new CameraCapabilityList(context, new CameraStaticParameters(cameraManager.getCameraCharacteristics(cameraId.getCameraDeviceId())), new BypassCameraStaticParameters(BypassCamera.getCaps(getBypassCameraFacing(cameraId))));
            boolean zIsEmpty = cameraCapabilityList.FPS_RANGE.get().isEmpty();
            boolean zIsEmpty2 = cameraCapabilityList.PREVIEW_SIZE.get().isEmpty();
            boolean zIsEmpty3 = cameraCapabilityList.PICTURE_SIZE.get().isEmpty();
            if (!zIsEmpty && !zIsEmpty2 && !zIsEmpty3) {
                CamLog.d("CameraCapabilities are loadded from device.");
                return cameraCapabilityList;
            }
            CamLog.e("Mandatory capabilities could not be retrieved. fps-range is empty:" + zIsEmpty + " preview-size is empty:" + zIsEmpty2 + " picture-size is empty:" + zIsEmpty3);
            return null;
        } catch (CameraAccessException e) {
            CamLog.e("Fail to load capabilities from device.", e);
            return null;
        }
    }

    private static PlatformCapabilityList loadPlatformCapabilityFromDevice(Context context) {
        try {
            PlatformCapabilityList platformCapabilityList = new PlatformCapabilityList(context);
            CamLog.d("PlatformCapabilities are loaded from device.");
            return platformCapabilityList;
        } catch (Exception e) {
            CamLog.e("Fail to load PlatformCapabilities from device.", e);
            return null;
        }
    }

    private static BypassCamera.Facing getBypassCameraFacing(CameraInfo.CameraId cameraId) {
        if (cameraId == CameraInfo.CameraId.BACK) {
            return BypassCamera.Facing.BACK;
        }
        return BypassCamera.Facing.FRONT;
    }

    private static boolean isSharedPreferencesValid(SharedPreferences sharedPreferences) {
        if (!isBuildFingerprintModified(sharedPreferences) && !isVersionModified(sharedPreferences)) {
            return true;
        }
        if (!CamLog.VERBOSE) {
            return false;
        }
        CamLog.d("prefs is invalid.");
        return false;
    }

    private static boolean isBuildFingerprintModified(SharedPreferences sharedPreferences) {
        if (!sharedPreferences.contains(CommonConstants.FINGERPRINT)) {
            return true;
        }
        String string = sharedPreferences.getString(CommonConstants.FINGERPRINT, "");
        String str = Build.FINGERPRINT;
        if (string.equals(str)) {
            return false;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("version mismatch: cached: " + string + ", current : " + str);
        }
        return true;
    }

    private static boolean isVersionModified(SharedPreferences sharedPreferences) {
        if (!sharedPreferences.contains(KEY_VERSION)) {
            return true;
        }
        int i = sharedPreferences.getInt(KEY_VERSION, 0);
        if (i == CAPABILITY_VERSION) {
            return false;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("version mismatch: cached: " + i + ", current : " + CAPABILITY_VERSION);
        }
        return true;
    }

    private static boolean isFusionSupported(CameraInfo.CameraId cameraId) {
        for (String str : getList(cameraId).FUSION_MODE.get()) {
            if (str.equals("on") || str.equals("auto")) {
                return true;
            }
        }
        return false;
    }

    static boolean store(Context context, String str, List<CapabilityItem<?>> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("store: " + list);
        }
        SharedPreferences sharedPreferences = SharedPreferencesAccessor.getSharedPreferences(context, str, 0);
        if (sharedPreferences == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("Failed to obtain shared prefs.");
            }
            return false;
        }
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        String str2 = Build.FINGERPRINT;
        editorEdit.putString(CommonConstants.FINGERPRINT, str2);
        if (CamLog.VERBOSE) {
            CamLog.d("android.os.Build.FINGERPRINT: " + str2);
        }
        int i = CAPABILITY_VERSION;
        editorEdit.putInt(KEY_VERSION, i);
        if (CamLog.VERBOSE) {
            CamLog.d("KEY_VERSION: " + i);
        }
        Iterator<CapabilityItem<?>> it = list.iterator();
        while (it.hasNext()) {
            it.next().write(editorEdit);
        }
        editorEdit.apply();
        if (CamLog.VERBOSE) {
            CamLog.d("store: success.");
        }
        return true;
    }

    public static boolean isFrontCameraSupported() {
        Iterator<CameraInfo.CameraId> it = sCameraCapabilityListMap.keySet().iterator();
        while (it.hasNext()) {
            if (it.next() == CameraInfo.CameraId.FRONT) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFullHdVideoFpsSupported(CameraInfo.CameraId cameraId, int i) {
        for (VideoConfiguration videoConfiguration : getList(cameraId).VIDEO_CONFIGURATION.get()) {
            if (videoConfiguration.mWidth == 1920 && videoConfiguration.mHeight == 1080 && videoConfiguration.mFps >= i) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStillHdrSupportedWith(CameraInfo.CameraId cameraId, Resolution resolution) {
        for (Rect rect : getList(cameraId).STILL_HDR_SUPPORTED_PICTURE_SIZE.get()) {
            if (rect.width() == resolution.getPictureRect().width() && rect.height() == resolution.getPictureRect().height()) {
                return true;
            }
        }
        return false;
    }

    public static int getMaxSoftSkinLevel(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_SOFT_SKIN_LEVEL.get().intValue();
    }

    public static int getMinSoftSkinLevel(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MIN_SOFT_SKIN_LEVEL.get().intValue();
    }

    public static boolean isSoftSkinSupported(CameraInfo.CameraId cameraId) {
        return getMaxSoftSkinLevel(cameraId) > getMinSoftSkinLevel(cameraId);
    }

    public static boolean isAwbAbCompensationSupported(CameraInfo.CameraId cameraId) {
        Integer num = getList(cameraId).MAX_AWB_AB.get();
        Integer num2 = getList(cameraId).MIN_AWB_AB.get();
        return (num == null || num2 == null || num.intValue() <= num2.intValue()) ? false : true;
    }

    public static boolean isManualFocusSupported(CameraInfo.CameraId cameraId) {
        Boolean bool = getList(cameraId).MANUAL_FOCUS.get();
        return bool != null && bool.booleanValue();
    }

    public static boolean isShutterSpeedSupported(CameraInfo.CameraId cameraId) {
        List<String> list = getList(cameraId).AE.get();
        if (list.isEmpty() || !list.contains(CameraParameters.AE_MODE_SHUTTER_PRIO)) {
            return false;
        }
        Long l = getList(cameraId).MAX_SHUTTER_SPEED.get();
        Long l2 = getList(cameraId).MIN_SHUTTER_SPEED.get();
        return (l == null || l2 == null || l.longValue() <= l2.longValue()) ? false : true;
    }

    public static boolean isObjectTrackingSupported(CameraInfo.CameraId cameraId) {
        return getList(cameraId).OBJECT_TRACKING.get().booleanValue();
    }

    public static boolean isPredictiveCaptureShotSupported(CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREDICTIVE_CAPTURE.get().size() != 0;
    }

    public static boolean isManualBurstSupported(CameraInfo.CameraId cameraId) {
        return getList(cameraId).BURST.get().contains("on");
    }

    public static boolean isTrackingFocusDuringLockSupported(CameraInfo.CameraId cameraId) {
        return getList(cameraId).TRACKING_FOCUS_DURING_LOCK.get().booleanValue();
    }

    public static boolean isFocusSupported(CameraInfo.CameraId cameraId) {
        return !getList(cameraId).FOCUS_MODE.get().contains(CameraParameters.FOCUS_MODE_FIXED);
    }

    public static boolean isSuperWideSupported(CameraInfo.CameraId cameraId) {
        if (getList(cameraId).WIDE_ZOOM_TARGET_RATIO.get().floatValue() > 1.01d) {
            if (!CamLog.VERBOSE) {
                return true;
            }
            CamLog.d("isSuperWideSupported() SUPPORT");
            return true;
        }
        if (!CamLog.VERBOSE) {
            return false;
        }
        CamLog.d("isSuperWideSupported() NOT SUPPORT");
        return false;
    }

    public static float getMacroValue(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MACRO_FOCUS_RANGE.get().floatValue();
    }

    public static List<String> getSupportedFocusModes(CameraInfo.CameraId cameraId) {
        return getList(cameraId).FOCUS_MODE.get();
    }

    public static int getMaxNumFocusAreas(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_NUM_FOCUS_AREA.get().intValue();
    }

    public static boolean isTouchFocusSupported(CameraInfo.CameraId cameraId) {
        return getMaxNumFocusAreas(cameraId) >= 1;
    }

    public static List<String> getSupportedFocusAreaModes(CameraInfo.CameraId cameraId) {
        return getList(cameraId).FOCUS_AREA.get();
    }

    public static int getMaxExposureCompensation(CameraInfo.CameraId cameraId) {
        return getList(cameraId).EV_MAX.get().intValue();
    }

    public static int getMinExposureCompensation(CameraInfo.CameraId cameraId) {
        return getList(cameraId).EV_MIN.get().intValue();
    }

    public static float getExposureCompensationStep(CameraInfo.CameraId cameraId) {
        return getList(cameraId).EV_STEP.get().floatValue();
    }

    public static List<String> getSupportedMeteringModes(CameraInfo.CameraId cameraId) {
        return getList(cameraId).METERING.get();
    }

    public static List<String> getSupportedAeModes(CameraInfo.CameraId cameraId) {
        return getList(cameraId).AE.get();
    }

    public static List<String> getSupportedWhiteBalance(CameraInfo.CameraId cameraId) {
        return getList(cameraId).WHITE_BALANCE.get();
    }

    public static List<String> getSupportedShutterSpeedValues(CameraInfo.CameraId cameraId) {
        return getList(cameraId).SHUTTER_SPEED_VALUES.get();
    }

    public static long getMaxShutterSpeed(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_SHUTTER_SPEED.get().longValue();
    }

    public static long getMinShutterSpeed(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MIN_SHUTTER_SPEED.get().longValue();
    }

    public static List<String> getSupportedFlashModes(CameraInfo.CameraId cameraId) {
        return getList(cameraId).FLASH.get();
    }

    public static Range<Integer> getSupportedIsoRange(CameraInfo.CameraId cameraId) {
        return getList(cameraId).ISO_RANGE.get();
    }

    public static Range<Integer> getSupportedFusionIsoRange(CameraInfo.CameraId cameraId) {
        return getList(cameraId).FUSION_ISO_RANGE.get();
    }

    public static boolean isFlashModeSupported(CameraInfo.CameraId cameraId) {
        List<String> supportedFlashModes = getSupportedFlashModes(cameraId);
        return supportedFlashModes != null && supportedFlashModes.contains("on");
    }

    public static boolean isDisplayFlashModeSupported(CameraInfo.CameraId cameraId) {
        List<String> supportedFlashModes = getSupportedFlashModes(cameraId);
        return supportedFlashModes != null && supportedFlashModes.contains(CameraParameters.DISPLAY_FLASH_MODE_ON);
    }

    public static List<String> getSupportedStillHdrValues(CameraInfo.CameraId cameraId) {
        return getList(cameraId).HDR.get();
    }

    public static List<Rect> getStillHdrSupportedPictureSizes(CameraInfo.CameraId cameraId) {
        return getList(cameraId).STILL_HDR_SUPPORTED_PICTURE_SIZE.get();
    }

    public static Integer getMaxAwbColorCompensationAb(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_AWB_AB.get();
    }

    public static Integer getMinAwbColorCompensationAb(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MIN_AWB_AB.get();
    }

    public static boolean isSceneRecognitionSupported(CameraInfo.CameraId cameraId) {
        return getList(cameraId).SCENE_RECOGNITION.get().booleanValue();
    }

    public static int getMaxNumDetectedFaces(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_NUM_FACE.get().intValue();
    }

    public static boolean isFaceDetectionAvailable(CameraInfo.CameraId cameraId) {
        return isSmileDetectionAvailable(cameraId);
    }

    public static boolean isSmileDetectionAvailable(CameraInfo.CameraId cameraId) {
        return getList(cameraId).SMILE_DETECTION.get().booleanValue();
    }

    public static List<Rect> getSupportedPreviewSizes(CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREVIEW_SIZE.get();
    }

    public static List<int[]> getSupportedPreviewFpsRange(CameraInfo.CameraId cameraId) {
        return getList(cameraId).FPS_RANGE.get();
    }

    public static int getMaxPreviewFps(CameraInfo.CameraId cameraId) {
        List<int[]> supportedPreviewFpsRange = getSupportedPreviewFpsRange(cameraId);
        int i = 0;
        if (supportedPreviewFpsRange != null) {
            for (int[] iArr : supportedPreviewFpsRange) {
                if (iArr != null && iArr.length == 2 && i < iArr[1]) {
                    i = iArr[1];
                }
            }
        }
        return i;
    }

    public static Rect getPreferredPreviewSizeForStill(CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREVIEW_SIZE_FOR_STILL.get();
    }

    public static List<Rect> getSupportedPictureSizes(CameraInfo.CameraId cameraId) {
        return getList(cameraId).PICTURE_SIZE.get();
    }

    public static List<Rect> getManualIsoSupportedPictureSizes(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MANUAL_ISO_SUPPORTED_PICTURE_SIZE.get();
    }

    public static Rect getPreferredPreviewSizeForVideo(CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREVIEW_SIZE_FOR_VIDEO.get();
    }

    public static Rect getPreferredPreviewSizeForHdrVideo(CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREVIEW_SIZE_FOR_HDR_VIDEO.get();
    }

    public static List<VideoConfiguration> getSupportedVideoConfiguration(CameraInfo.CameraId cameraId) {
        return getList(cameraId).VIDEO_CONFIGURATION.get();
    }

    public static Rect getActiveArraySize(CameraInfo.CameraId cameraId) {
        return getList(cameraId).ACTIVE_ARRAY_SIZE.get();
    }

    public static int getLensFacing(CameraInfo.CameraId cameraId) {
        return getList(cameraId).LENS_FACING.get().intValue();
    }

    public static List<String> getSupportedClimaxRecognition(CameraInfo.CameraId cameraId) {
        return getList(cameraId).PREDICTIVE_CAPTURE.get();
    }

    public static float getMaxZoomRatio(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MAX_ZOOM_RATIO.get().floatValue();
    }

    public static float getWideZoomTargetRatio(CameraInfo.CameraId cameraId) {
        return getList(cameraId).WIDE_ZOOM_TARGET_RATIO.get().floatValue();
    }

    public static List<Integer> getZoomRatios(CameraInfo.CameraId cameraId) {
        float maxZoomRatio = getMaxZoomRatio(cameraId);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i <= 120; i++) {
            arrayList.add(Integer.valueOf(Math.round(((((maxZoomRatio - 1.0f) * i) / 120.0f) + 1.0f) * 100.0f)));
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getZoomRatios() : " + arrayList);
        }
        return arrayList;
    }

    public static boolean isTouchAeSupported(CameraInfo.CameraId cameraId) {
        return getSupportedMeteringModes(cameraId).contains("user");
    }

    public static boolean isPowerSavingSupported(CameraInfo.CameraId cameraId) {
        List<String> list = getList(cameraId).POWER_SAVING_MODE.get();
        return (list.size() == 0 || (list.size() == 1 && list.contains("off"))) ? false : true;
    }

    public static long getMinExposureTimeLimit(CameraInfo.CameraId cameraId) {
        return getList(cameraId).MIN_SHUTTER_SPEED_LIMIT.get().longValue();
    }

    public static boolean isLiftTriggerSupported() {
        return sPlatformCapabilityList.CAMERA_LIFT_TRIGGER.get().booleanValue();
    }

    public static long getSuperSlowFrameRate(CameraInfo.CameraId cameraId, VideoSize videoSize) {
        for (VideoConfiguration videoConfiguration : getList(cameraId).SUPER_SLOW_CONFIGURATION.get()) {
            if (videoConfiguration.mWidth == videoSize.getVideoRect().width() && videoConfiguration.mHeight == videoSize.getVideoRect().height()) {
                return videoConfiguration.mFps;
            }
        }
        return 0L;
    }

    public static long getSuperSlowFrameNum(CameraInfo.CameraId cameraId, VideoSize videoSize) {
        for (VideoConfiguration videoConfiguration : getList(cameraId).SUPER_SLOW_CONFIGURATION.get()) {
            if (videoConfiguration.mWidth == videoSize.getVideoRect().width() && videoConfiguration.mHeight == videoSize.getVideoRect().height()) {
                return videoConfiguration.mFrameNum;
            }
        }
        return 0L;
    }

    public static boolean isSuperSlowFullHdSupported(CameraInfo.CameraId cameraId) {
        for (VideoConfiguration videoConfiguration : getList(cameraId).SUPER_SLOW_CONFIGURATION.get()) {
            if (videoConfiguration.mWidth == VideoSize.FULL_HD.getVideoRect().width() && videoConfiguration.mHeight == VideoSize.FULL_HD.getVideoRect().height()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVideoHdrSupported(CameraInfo.CameraId cameraId) {
        return getList(cameraId).HDR_VIDEO_SUPPORTED.get().booleanValue();
    }

    public static int getVideoHdrRecordingProfile() {
        MediaCodecParametersHolder mediaCodecParametersHolder = (MediaCodecParametersHolder) getHolder(HolderType.MEDIA_CODEC);
        if (mediaCodecParametersHolder == null) {
            CamLog.e("parameter is not prepared");
            return 0;
        }
        return mediaCodecParametersHolder.getVideoHdrProfile();
    }

    public static boolean isHighSensitivityFusionSupported(CameraInfo.CameraId cameraId) {
        if (!isFusionSupported(cameraId)) {
            return false;
        }
        Range<Integer> range = getList(cameraId).ISO_RANGE.get();
        Range<Integer> range2 = getList(cameraId).FUSION_ISO_RANGE.get();
        return range == null || range2 == null || range.getLower() != range2.getLower() || range.getUpper() != range2.getUpper();
    }

    public static boolean isSideTouchSupported() {
        return sPlatformCapabilityList.SIDE_SENSE.get().booleanValue();
    }

    public static boolean isWearableSupported() {
        return sPlatformCapabilityList.WEARABLE.get().booleanValue();
    }

    public static boolean isFusionSupportedWith(CameraInfo.CameraId cameraId, Resolution resolution) {
        if (!isFusionSupported(cameraId)) {
            return false;
        }
        for (Rect rect : getList(cameraId).FUSION_SUPPORTED_PICTURE_SIZES.get()) {
            if (resolution.getPictureRect().width() == rect.width() && resolution.getPictureRect().height() == rect.height()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFusionSupportedWith(CameraInfo.CameraId cameraId, VideoSize videoSize) {
        if (!isFusionSupported(cameraId)) {
            return false;
        }
        for (VideoConfiguration videoConfiguration : getList(cameraId).FUSION_SUPPORTED_VIDEO_CONFIGURATION.get()) {
            if (videoSize.getVideoRect().width() == videoConfiguration.mWidth && videoSize.getVideoRect().height() == videoConfiguration.mHeight && RecordingProfile.getVideoFrameRate(videoSize, VideoHdr.HDR_OFF) <= videoConfiguration.mFps) {
                return true;
            }
        }
        return false;
    }

    public static void getCameraInfo(CameraInfo.CameraId cameraId, CameraInfo cameraInfo) {
        if (!isPrepared() || hasDeviceError()) {
            CamLog.e("CameraInfo cannot be retrieved. Because PlatformCapability is not prepared.");
            return;
        }
        cameraInfo.facing = cameraId;
        try {
            cameraInfo.orientation = ((Integer) sCameraManager.getCameraCharacteristics(cameraId.getCameraDeviceId()).get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
            Camera.CameraInfo cameraInfo2 = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId.getCameraDeviceIdApi1(), cameraInfo2);
            cameraInfo.canDisableShutterSound = cameraInfo2.canDisableShutterSound;
            if (CamLog.VERBOSE) {
                CamLog.d("facing = " + cameraInfo.facing + ", orientation = " + cameraInfo.orientation + ", canDisableShutterSound = " + cameraInfo.canDisableShutterSound);
            }
        } catch (CameraAccessException | IllegalArgumentException e) {
            CamLog.e("Failed in getCameraCharacteristics", e);
        }
    }

    public static boolean isForceSound(CameraInfo.CameraId cameraId) {
        getCameraInfo(cameraId, new CameraInfo());
        return !r0.canDisableShutterSound;
    }

    public static boolean isBypassCameraSupported() {
        return isBypassCameraSupportStateHolder.sIsSupported;
    }

    private static class isBypassCameraSupportStateHolder {
        private static final boolean sIsSupported = isSupported();

        private isBypassCameraSupportStateHolder() {
        }

        private static boolean isSupported() throws ClassNotFoundException {
            try {
                Class.forName("com.sonymobile.imageprocessor.bypasscamera2.BypassCamera");
                return true;
            } catch (ClassNotFoundException unused) {
                CamLog.i("BypassCamera is NOT SUPPORTED");
                return false;
            }
        }
    }

    public static boolean isDistortionCorrectionSupported(CameraInfo.CameraId cameraId) {
        List<String> list = getList(cameraId).DISTORTION_CORRECTION.get();
        return list != null && list.contains("on");
    }

    @Nullable
    private static ParameterHolder getHolder(HolderType holderType) {
        if (sParameterHolderMap.isEmpty()) {
            try {
                if (sPreparingTaskFuture.isDone()) {
                    try {
                        try {
                            if (CamLog.DEBUG) {
                                CamLog.d("getHolder: get holders: E");
                            }
                            sParameterHolderMap.putAll(sPreparingTaskFuture.get(1000L, TimeUnit.MILLISECONDS));
                            if (CamLog.DEBUG) {
                                CamLog.d("getHolder: get holders: X");
                            }
                        } catch (InterruptedException e) {
                            CamLog.e("Preparing interrupted", e);
                        } catch (ExecutionException e2) {
                            CamLog.e("Preparing failed", e2);
                        }
                    } catch (TimeoutException e3) {
                        CamLog.e("Preparing timed out", e3);
                    }
                } else {
                    sPreparingTaskFuture.cancel(false);
                    sPrepareTaskExecutor.shutdown();
                    try {
                        if (CamLog.DEBUG) {
                            CamLog.d("getHolder: call directly: E");
                        }
                        sParameterHolderMap.putAll(new PrepareParametersTask().call());
                        if (CamLog.DEBUG) {
                            CamLog.d("getHolder: call directly: X");
                        }
                    } catch (Exception e4) {
                        CamLog.e("Failed to call directly", e4);
                    }
                }
            } finally {
                sPrepareTaskExecutor.shutdown();
            }
        }
        return sParameterHolderMap.get(holderType);
    }

    public static boolean isSuperSlowMotionSupported(CameraInfo.CameraId cameraId) {
        List<String> list = getList(cameraId).SUPER_SLOW_VALUES.get();
        return list != null && list.contains("on");
    }

    public static void setDeviceError(boolean z) {
        mHasDeviceError = z;
    }

    public static boolean hasDeviceError() {
        return mHasDeviceError;
    }
}
