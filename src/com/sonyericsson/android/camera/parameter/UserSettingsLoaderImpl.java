package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.setting.UserSettingsLoader;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.storage.Storage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class UserSettingsLoaderImpl implements UserSettingsLoader {
    private static final String FINGERPRINT_KEY = "android.os.Build.FINGERPRINT";
    private static final String THREAD_NAME = "SettingLoadTask";
    private final Context mContext;
    private final ExecutorService mSetupExecutor;
    private final SharedPreferencesAccessor mSharedPrefsAccessor;
    private final Storage mStorage;
    private final Map<CapturingMode, Parameters> mParametersEntries = new HashMap();
    private final Map<CapturingMode, Parameters> mMigrateParametersEntries = new HashMap();
    private final LinkedList<UserSettingsLoader.OnLoadCompletedListener> mListeners = new LinkedList<>();
    private final Object mEntryLock = new Object();
    private boolean mIsCompleted = false;
    private Future<?> mLoadTaskFuture = null;
    private final ModeIndependentParams mCommonParameters = new ModeIndependentParams();

    private class LoadTask implements Runnable {
        private LoadTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            LinkedList linkedList;
            synchronized (UserSettingsLoaderImpl.this) {
                if (UserSettingsLoaderImpl.this.mIsCompleted) {
                    return;
                }
                PerfLog.LOAD_USER_SETTING_ALL.begin();
                String str = Build.FINGERPRINT;
                String string = UserSettingsLoaderImpl.this.getSharedPreferencesAccessor().readString("android.os.Build.FINGERPRINT", "");
                if ("".equals(string)) {
                    CamLog.d("Initialize UserSettings data by default values due to no fingerprint.");
                    UserSettingsLoaderImpl.this.applyDefaultParameters(UserSettingsLoaderImpl.this.mContext, UserSettingsLoaderImpl.this.mStorage);
                } else if (!str.equals(string)) {
                    CamLog.d("Migrate UserSettings. stored-fingerprint:" + string + " current-fingerprint:" + str);
                    CameraSettingsMigrator.migrate(UserSettingsLoaderImpl.this.mContext, UserSettingsLoaderImpl.this.mStorage, UserSettingsLoaderImpl.this);
                    UserSettingsLoaderImpl.this.getSharedPreferencesAccessor().writeString("android.os.Build.FINGERPRINT", str, true);
                    synchronized (UserSettingsLoaderImpl.this) {
                        UserSettingsLoaderImpl.this.mMigrateParametersEntries.clear();
                        UserSettingsLoaderImpl.this.mCommonParameters.clear(UserSettingsLoaderImpl.this.mStorage);
                    }
                    UserSettingsLoaderImpl.this.mSharedPrefsAccessor.reset();
                }
                UserSettingsLoaderImpl.this.loadInternal(UserSettingsLoaderImpl.this.mParametersEntries);
                PerfLog.LOAD_USER_SETTING_ALL.end();
                synchronized (UserSettingsLoaderImpl.this) {
                    UserSettingsLoaderImpl.this.mIsCompleted = true;
                    linkedList = UserSettingsLoaderImpl.this.mListeners;
                }
                Iterator it = linkedList.iterator();
                while (it.hasNext()) {
                    ((UserSettingsLoader.OnLoadCompletedListener) it.next()).onLoadCompleted();
                }
            }
        }
    }

    public UserSettingsLoaderImpl(Context context, Storage storage) {
        this.mContext = context;
        this.mStorage = storage;
        this.mCommonParameters.clear(storage);
        this.mSharedPrefsAccessor = new SharedPreferencesAccessor(context, SharedPreferencesConstants.CAMERA_SHARED_PREFERENCES_NAME);
        this.mSetupExecutor = ThreadUtil.buildExecutor(THREAD_NAME);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettingsLoader
    public synchronized void load() {
        if (this.mLoadTaskFuture != null && !this.mLoadTaskFuture.isDone()) {
            CamLog.d("duplicated load call");
        } else {
            this.mLoadTaskFuture = this.mSetupExecutor.submit(new LoadTask());
        }
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettingsLoader
    public synchronized void save(Map<CapturingMode, Parameters> map, CapturingMode capturingMode) {
        saveInternal(map, capturingMode, this.mParametersEntries);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettingsLoader
    public synchronized void release() {
        this.mListeners.clear();
        this.mMigrateParametersEntries.clear();
        this.mParametersEntries.clear();
        this.mCommonParameters.clear(this.mStorage);
        this.mIsCompleted = false;
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettingsLoader
    public void clearMasterData() {
        this.mSharedPrefsAccessor.clear(true);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettingsLoader
    public Parameters getUserSettingParameters(Context context, CapturingMode capturingMode, Storage storage, Configurations configurations, boolean z, ModeIndependentParams modeIndependentParams, boolean z2) {
        CamLog.d("invoked");
        long jUptimeMillis = SystemClock.uptimeMillis();
        while (!this.mParametersEntries.containsKey(capturingMode)) {
            synchronized (this.mEntryLock) {
                try {
                    this.mEntryLock.wait(5L);
                } catch (InterruptedException unused) {
                    CamLog.d("Interrupted to wait");
                }
            }
        }
        CamLog.d("getUserSettingParameters(): wait loading for " + (SystemClock.uptimeMillis() - jUptimeMillis) + "ms");
        return this.mParametersEntries.get(capturingMode).copy(context, capturingMode, configurations, storage, z, modeIndependentParams, z2);
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettingsLoader
    public SharedPreferencesAccessor getSharedPreferencesAccessor() {
        return this.mSharedPrefsAccessor;
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettingsLoader
    public synchronized void registerLoadCompletedListener(UserSettingsLoader.OnLoadCompletedListener onLoadCompletedListener) {
        if (onLoadCompletedListener != null) {
            this.mListeners.add(onLoadCompletedListener);
            if (this.mIsCompleted) {
                onLoadCompletedListener.onLoadCompleted();
            }
        }
    }

    @Override // com.sonyericsson.android.camera.setting.UserSettingsLoader
    public synchronized void unregisterLoadCompletedListener(UserSettingsLoader.OnLoadCompletedListener onLoadCompletedListener) {
        this.mListeners.remove(onLoadCompletedListener);
    }

    Map<CapturingMode, Parameters> loadMigrateParameters() {
        loadInternal(this.mMigrateParametersEntries);
        return this.mMigrateParametersEntries;
    }

    void saveMigrateParameters(Map<CapturingMode, Parameters> map, CapturingMode capturingMode) {
        saveInternal(map, capturingMode, this.mMigrateParametersEntries);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadInternal(Map<CapturingMode, Parameters> map) {
        HashMap map2 = new HashMap();
        for (CapturingMode capturingMode : CapturingMode.getValidOptions()) {
            Parameters parametersCreate = Parameters.create(this.mContext, capturingMode, false, this.mCommonParameters);
            parametersCreate.prepareHolder(new Configurations(), this.mSharedPrefsAccessor, this.mStorage);
            map2.put(capturingMode, parametersCreate);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(Arrays.asList(UserSettingKey.values()));
        this.mSharedPrefsAccessor.readParameters(arrayList);
        for (Parameters parameters : map2.values()) {
            parameters.readSharedPrefs(this.mSharedPrefsAccessor);
            parameters.commit();
            map.put(parameters.capturingMode, parameters);
            notifyEntryReady();
        }
        map2.clear();
    }

    private void saveInternal(Map<CapturingMode, Parameters> map, CapturingMode capturingMode, Map<CapturingMode, Parameters> map2) {
        CapturingMode capturingMode2;
        for (Map.Entry<CapturingMode, Parameters> entry : map.entrySet()) {
            if (map2.containsKey(entry.getKey())) {
                ParameterUtil.copy(entry.getValue().mHolders, map2.get(entry.getKey()).mHolders);
            }
            if (entry.getKey() == capturingMode) {
                this.mCommonParameters.setValues(entry.getValue().mIndependentParams);
            }
        }
        for (Map.Entry<CapturingMode, Parameters> entry2 : map2.entrySet()) {
            entry2.getValue().mIndependentParams.setValues(this.mCommonParameters);
            entry2.getValue().writeSharedPrefs(this.mSharedPrefsAccessor);
        }
        this.mSharedPrefsAccessor.writeParameters(false);
        if (PlatformCapability.isFrontCameraSupported() && !this.mSharedPrefsAccessor.getSharedPreferences().contains(SharedPreferencesConstants.KEY_FRONT_FAST)) {
            if (PlatformCapability.isSceneRecognitionSupported(CameraInfo.CameraId.FRONT)) {
                capturingMode2 = CapturingMode.SUPERIOR_FRONT;
            } else {
                capturingMode2 = CapturingMode.FRONT_PHOTO;
            }
            this.mSharedPrefsAccessor.writeString(SharedPreferencesConstants.KEY_FRONT_FAST, capturingMode2.name(), false);
        }
        this.mSharedPrefsAccessor.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyDefaultParameters(Context context, Storage storage) {
        Configurations configurations = new Configurations();
        SharedPreferencesAccessor sharedPreferencesAccessor = new SharedPreferencesAccessor(context, SharedPreferencesConstants.CAMERA_SHARED_PREFERENCES_NAME);
        ModeIndependentParams modeIndependentParams = new ModeIndependentParams();
        Iterator<CapturingMode> it = CapturingMode.getValidOptions().iterator();
        while (it.hasNext()) {
            Parameters parametersCreate = Parameters.create(this.mContext, it.next(), false, modeIndependentParams);
            parametersCreate.prepareHolder(configurations, sharedPreferencesAccessor, storage);
            parametersCreate.writeSharedPrefs(sharedPreferencesAccessor);
        }
        sharedPreferencesAccessor.writeString("android.os.Build.FINGERPRINT", Build.FINGERPRINT, false);
        sharedPreferencesAccessor.writeParameters(false);
        sharedPreferencesAccessor.apply();
    }

    private void notifyEntryReady() {
        new Thread(new Runnable() { // from class: com.sonyericsson.android.camera.parameter.UserSettingsLoaderImpl.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (UserSettingsLoaderImpl.this.mEntryLock) {
                    UserSettingsLoaderImpl.this.mEntryLock.notifyAll();
                }
            }
        }).start();
    }
}
