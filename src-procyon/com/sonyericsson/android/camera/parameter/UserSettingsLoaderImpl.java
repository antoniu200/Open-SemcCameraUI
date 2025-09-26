// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.util.PerfLog;
import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;
import java.util.Iterator;
import android.os.Build;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.util.ThreadUtil;
import java.util.HashMap;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import java.util.concurrent.ExecutorService;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.LinkedList;
import android.content.Context;
import com.sonyericsson.android.camera.setting.UserSettingsLoader;

public class UserSettingsLoaderImpl implements UserSettingsLoader
{
    private static final String FINGERPRINT_KEY = "android.os.Build.FINGERPRINT";
    private static final String THREAD_NAME = "SettingLoadTask";
    private final ModeIndependentParams mCommonParameters;
    private final Context mContext;
    private final Object mEntryLock;
    private boolean mIsCompleted;
    private final LinkedList<OnLoadCompletedListener> mListeners;
    private Future<?> mLoadTaskFuture;
    private final Map<CapturingMode, Parameters> mMigrateParametersEntries;
    private final Map<CapturingMode, Parameters> mParametersEntries;
    private final ExecutorService mSetupExecutor;
    private final SharedPreferencesAccessor mSharedPrefsAccessor;
    private final Storage mStorage;
    
    public UserSettingsLoaderImpl(final Context mContext, final Storage mStorage) {
        this.mParametersEntries = new HashMap<CapturingMode, Parameters>();
        this.mMigrateParametersEntries = new HashMap<CapturingMode, Parameters>();
        this.mListeners = new LinkedList<OnLoadCompletedListener>();
        this.mEntryLock = new Object();
        this.mIsCompleted = false;
        this.mLoadTaskFuture = null;
        this.mContext = mContext;
        this.mStorage = mStorage;
        (this.mCommonParameters = new ModeIndependentParams()).clear(mStorage);
        this.mSharedPrefsAccessor = new SharedPreferencesAccessor(mContext, "com.sonyericsson.android.camera.shared_preferences");
        this.mSetupExecutor = ThreadUtil.buildExecutor("SettingLoadTask");
    }
    
    private void applyDefaultParameters(final Context context, final Storage storage) {
        final Configurations configurations = new Configurations();
        final SharedPreferencesAccessor sharedPreferencesAccessor = new SharedPreferencesAccessor(context, "com.sonyericsson.android.camera.shared_preferences");
        final ModeIndependentParams modeIndependentParams = new ModeIndependentParams();
        final Iterator<CapturingMode> iterator = CapturingMode.getValidOptions().iterator();
        while (iterator.hasNext()) {
            final Parameters create = Parameters.create(this.mContext, iterator.next(), false, modeIndependentParams);
            create.prepareHolder(configurations, sharedPreferencesAccessor, storage);
            create.writeSharedPrefs(sharedPreferencesAccessor);
        }
        sharedPreferencesAccessor.writeString("android.os.Build.FINGERPRINT", Build.FINGERPRINT, false);
        sharedPreferencesAccessor.writeParameters(false);
        sharedPreferencesAccessor.apply();
    }
    
    private void loadInternal(final Map<CapturingMode, Parameters> map) {
        final HashMap hashMap = new HashMap();
        for (final CapturingMode capturingMode : CapturingMode.getValidOptions()) {
            final Parameters create = Parameters.create(this.mContext, capturingMode, false, this.mCommonParameters);
            create.prepareHolder(new Configurations(), this.mSharedPrefsAccessor, this.mStorage);
            hashMap.put(capturingMode, create);
        }
        final ArrayList list = new ArrayList();
        list.addAll(Arrays.asList(UserSettingKey.values()));
        this.mSharedPrefsAccessor.readParameters(list);
        for (final Parameters parameters : hashMap.values()) {
            parameters.readSharedPrefs(this.mSharedPrefsAccessor);
            parameters.commit();
            map.put(parameters.capturingMode, parameters);
            this.notifyEntryReady();
        }
        hashMap.clear();
    }
    
    private void notifyEntryReady() {
        new Thread(new Runnable(this) {
            final UserSettingsLoaderImpl this$0;
            
            @Override
            public void run() {
                synchronized (this.this$0.mEntryLock) {
                    this.this$0.mEntryLock.notifyAll();
                }
            }
        }).start();
    }
    
    private void saveInternal(final Map<CapturingMode, Parameters> map, final CapturingMode capturingMode, final Map<CapturingMode, Parameters> map2) {
        for (final Map.Entry<Object, Parameters> entry : map.entrySet()) {
            if (map2.containsKey(entry.getKey())) {
                ParameterUtil.copy(entry.getValue().mHolders, map2.get(entry.getKey()).mHolders);
            }
            if (entry.getKey() == capturingMode) {
                this.mCommonParameters.setValues(entry.getValue().mIndependentParams);
            }
        }
        for (final Map.Entry<K, Parameters> entry2 : map2.entrySet()) {
            entry2.getValue().mIndependentParams.setValues(this.mCommonParameters);
            entry2.getValue().writeSharedPrefs(this.mSharedPrefsAccessor);
        }
        this.mSharedPrefsAccessor.writeParameters(false);
        if (PlatformCapability.isFrontCameraSupported() && !this.mSharedPrefsAccessor.getSharedPreferences().contains("FRONT_FAST")) {
            CapturingMode capturingMode2;
            if (PlatformCapability.isSceneRecognitionSupported(CameraInfo.CameraId.FRONT)) {
                capturingMode2 = CapturingMode.SUPERIOR_FRONT;
            }
            else {
                capturingMode2 = CapturingMode.FRONT_PHOTO;
            }
            this.mSharedPrefsAccessor.writeString("FRONT_FAST", capturingMode2.name(), false);
        }
        this.mSharedPrefsAccessor.apply();
    }
    
    @Override
    public void clearMasterData() {
        this.mSharedPrefsAccessor.clear(true);
    }
    
    @Override
    public SharedPreferencesAccessor getSharedPreferencesAccessor() {
        return this.mSharedPrefsAccessor;
    }
    
    @Override
    public Parameters getUserSettingParameters(final Context context, final CapturingMode capturingMode, final Storage storage, final Configurations configurations, final boolean b, final ModeIndependentParams modeIndependentParams, final boolean b2) {
        CamLog.d("invoked");
        final long uptimeMillis = SystemClock.uptimeMillis();
        while (!this.mParametersEntries.containsKey(capturingMode)) {
            final Object mEntryLock = this.mEntryLock;
            monitorenter(mEntryLock);
            try {
                try {
                    this.mEntryLock.wait(5L);
                }
                finally {
                    monitorexit(mEntryLock);
                    monitorexit(mEntryLock);
                }
            }
            catch (final InterruptedException ex) {}
            break;
        }
        final long uptimeMillis2 = SystemClock.uptimeMillis();
        final StringBuilder sb = new StringBuilder();
        sb.append("getUserSettingParameters(): wait loading for ");
        sb.append(uptimeMillis2 - uptimeMillis);
        sb.append("ms");
        CamLog.d(sb.toString());
        return this.mParametersEntries.get(capturingMode).copy(context, capturingMode, configurations, storage, b, modeIndependentParams, b2);
    }
    
    @Override
    public void load() {
        synchronized (this) {
            if (this.mLoadTaskFuture != null && !this.mLoadTaskFuture.isDone()) {
                CamLog.d("duplicated load call");
                return;
            }
            this.mLoadTaskFuture = this.mSetupExecutor.submit(new LoadTask());
        }
    }
    
    Map<CapturingMode, Parameters> loadMigrateParameters() {
        this.loadInternal(this.mMigrateParametersEntries);
        return this.mMigrateParametersEntries;
    }
    
    @Override
    public void registerLoadCompletedListener(final OnLoadCompletedListener e) {
        monitorenter(this);
        if (e != null) {
            try {
                this.mListeners.add(e);
                if (this.mIsCompleted) {
                    e.onLoadCompleted();
                }
            }
            finally {
                monitorexit(this);
            }
        }
        monitorexit(this);
    }
    
    @Override
    public void release() {
        synchronized (this) {
            this.mListeners.clear();
            this.mMigrateParametersEntries.clear();
            this.mParametersEntries.clear();
            this.mCommonParameters.clear(this.mStorage);
            this.mIsCompleted = false;
        }
    }
    
    @Override
    public void save(final Map<CapturingMode, Parameters> map, final CapturingMode capturingMode) {
        synchronized (this) {
            this.saveInternal(map, capturingMode, this.mParametersEntries);
        }
    }
    
    void saveMigrateParameters(final Map<CapturingMode, Parameters> map, final CapturingMode capturingMode) {
        this.saveInternal(map, capturingMode, this.mMigrateParametersEntries);
    }
    
    @Override
    public void unregisterLoadCompletedListener(final OnLoadCompletedListener o) {
        synchronized (this) {
            this.mListeners.remove(o);
        }
    }
    
    private class LoadTask implements Runnable
    {
        final UserSettingsLoaderImpl this$0;
        
        private LoadTask(final UserSettingsLoaderImpl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            Object str = this.this$0;
            synchronized (str) {
                if (this.this$0.mIsCompleted) {
                    return;
                }
                monitorexit(str);
                PerfLog.LOAD_USER_SETTING_ALL.begin();
                str = Build.FINGERPRINT;
                final String string = this.this$0.getSharedPreferencesAccessor().readString("android.os.Build.FINGERPRINT", "");
                if ("".equals(string)) {
                    CamLog.d("Initialize UserSettings data by default values due to no fingerprint.");
                    this.this$0.applyDefaultParameters(this.this$0.mContext, this.this$0.mStorage);
                }
                else if (!((String)str).equals(string)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Migrate UserSettings. stored-fingerprint:");
                    sb.append(string);
                    sb.append(" current-fingerprint:");
                    sb.append((String)str);
                    CamLog.d(sb.toString());
                    CameraSettingsMigrator.migrate(this.this$0.mContext, this.this$0.mStorage, this.this$0);
                    this.this$0.getSharedPreferencesAccessor().writeString("android.os.Build.FINGERPRINT", (String)str, true);
                    synchronized (this.this$0) {
                        this.this$0.mMigrateParametersEntries.clear();
                        this.this$0.mCommonParameters.clear(this.this$0.mStorage);
                        monitorexit(this.this$0);
                        this.this$0.mSharedPrefsAccessor.reset();
                    }
                }
                this.this$0.loadInternal(this.this$0.mParametersEntries);
                PerfLog.LOAD_USER_SETTING_ALL.end();
                Object o = this.this$0;
                synchronized (o) {
                    this.this$0.mIsCompleted = true;
                    final LinkedList access$900 = this.this$0.mListeners;
                    monitorexit(o);
                    o = access$900.iterator();
                    while (((Iterator)o).hasNext()) {
                        ((OnLoadCompletedListener)((Iterator)o).next()).onLoadCompleted();
                    }
                }
            }
        }
    }
}
