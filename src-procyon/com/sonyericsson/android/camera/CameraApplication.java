// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import java.util.Iterator;
import android.os.Bundle;
import java.util.LinkedList;
import android.support.annotation.NonNull;
import android.app.Activity;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import android.app.Application$ActivityLifecycleCallbacks;
import java.util.List;
import java.util.ArrayList;
import android.os.PowerManager;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.setting.UserSettingsLoader;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.parameter.UserSettingsLoaderImpl;
import com.sonyericsson.cameracommon.storage.StorageImpl;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import android.os.Handler;
import android.content.Context;
import android.app.Application;

public class CameraApplication extends Application
{
    private static final int CLASS_PRELOAD_WAKELOCK_TIMEOUT_MILLIS = 2000;
    public static final String TAG = "CameraApplication";
    private static Context sContext;
    private static final Handler sUiThreadHandler;
    private CameraDeviceHandler mCameraDeviceHandler;
    private ClassStaticBlockPreLoadThread.PreloadDoneCallback mClassPreloadDoneCallback;
    private ClassStaticBlockPreLoadThread mClassPreloadThread;
    private StorageImpl mStorage;
    private UserSettingsLoaderImpl mUserSettingsLoader;
    
    static {
        sUiThreadHandler = new Handler();
    }
    
    public CameraApplication() {
        this.mClassPreloadThread = null;
        this.mClassPreloadDoneCallback = null;
        CameraApplication.sContext = (Context)this;
    }
    
    public static Context getContext() {
        return CameraApplication.sContext;
    }
    
    public static final Handler getUiThreadHandler() {
        return CameraApplication.sUiThreadHandler;
    }
    
    CameraDeviceHandler getCameraDevice() {
        return this.mCameraDeviceHandler;
    }
    
    Storage getStorage() {
        return this.mStorage;
    }
    
    public UserSettingsLoader getUserSettingsLoader() {
        return this.mUserSettingsLoader;
    }
    
    public void onCreate() {
        PerfLog.APPLICATION_ON_CREATE.begin();
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : E");
        }
        (this.mStorage = new StorageImpl()).open(this.getApplicationContext());
        this.mUserSettingsLoader = new UserSettingsLoaderImpl((Context)this, this.mStorage);
        PlatformCapability.prepareAsync((PlatformCapability.OnPlatformCapabilityPreparedCallback)new PlatformCapability.OnPlatformCapabilityPreparedCallback(this) {
            final CameraApplication this$0;
            
            @Override
            public void onPrepared() {
                if (CamLog.DEBUG) {
                    CamLog.d("PlatformCapability Prepared");
                }
                this.this$0.mUserSettingsLoader.load();
            }
        });
        this.mCameraDeviceHandler = new CameraDeviceHandler(CameraApplication.sContext);
        this.mClassPreloadDoneCallback = new ClassPreloadDoneCallback();
        (this.mClassPreloadThread = new ClassStaticBlockPreLoadThread(this.mClassPreloadDoneCallback)).setPriority(10);
        this.mClassPreloadThread.setName("ClassStaticBlockPreLoadThread");
        this.mClassPreloadThread.start();
        ((PowerManager)this.getSystemService("power")).newWakeLock(1, "ClassStaticBlockPreLoadThread").acquire(2000L);
        super.onCreate();
        final ArrayList list = new ArrayList();
        list.add(this.mStorage);
        this.registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)new ActivityLifeCycleCallbackImpl(list));
        DebugParameterUtils.INSTANCE.preload((Context)this);
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : X");
        }
        PerfLog.APPLICATION_ON_CREATE.end();
    }
    
    public void onTerminate() {
        super.onTerminate();
        this.mUserSettingsLoader.release();
        this.mStorage.close();
    }
    
    private static class ActivityLifeCycleCallbackImpl implements Application$ActivityLifecycleCallbacks
    {
        List<Activity> mForegroundActivity;
        List<Pausable> mPausables;
        
        public ActivityLifeCycleCallbackImpl(@NonNull final List<Pausable> mPausables) {
            this.mForegroundActivity = new LinkedList<Activity>();
            this.mPausables = mPausables;
        }
        
        public void onActivityCreated(final Activity activity, final Bundle bundle) {
        }
        
        public void onActivityDestroyed(final Activity activity) {
        }
        
        public void onActivityPaused(final Activity activity) {
            if (!(activity instanceof CameraActivity)) {
                return;
            }
            this.mForegroundActivity.remove(activity);
            if (this.mForegroundActivity.isEmpty()) {
                final Iterator<Pausable> iterator = this.mPausables.iterator();
                while (iterator.hasNext()) {
                    iterator.next().pause();
                }
            }
        }
        
        public void onActivityResumed(final Activity activity) {
            if (!(activity instanceof CameraActivity)) {
                return;
            }
            if (this.mForegroundActivity.isEmpty()) {
                final Iterator<Pausable> iterator = this.mPausables.iterator();
                while (iterator.hasNext()) {
                    iterator.next().resume();
                }
            }
            this.mForegroundActivity.add(activity);
        }
        
        public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
        }
        
        public void onActivityStarted(final Activity activity) {
        }
        
        public void onActivityStopped(final Activity activity) {
        }
    }
    
    private class ClassPreloadDoneCallback implements PreloadDoneCallback
    {
        final CameraApplication this$0;
        
        private ClassPreloadDoneCallback(final CameraApplication this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onPreloadDone() {
            this.this$0.mClassPreloadThread = null;
            this.this$0.mClassPreloadDoneCallback = null;
        }
    }
    
    public interface Pausable
    {
        void pause();
        
        void resume();
    }
}
