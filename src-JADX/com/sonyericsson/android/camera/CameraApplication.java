package com.sonyericsson.android.camera;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.ClassStaticBlockPreLoadThread;
import com.sonyericsson.android.camera.debug.DebugParameterUtils;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.parameter.UserSettingsLoaderImpl;
import com.sonyericsson.android.camera.setting.UserSettingsLoader;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraApplication extends Application {
    private static final int CLASS_PRELOAD_WAKELOCK_TIMEOUT_MILLIS = 2000;
    public static final String TAG = "CameraApplication";
    private static Context sContext;
    private static final Handler sUiThreadHandler = new Handler();
    private CameraDeviceHandler mCameraDeviceHandler;
    private StorageImpl mStorage;
    private UserSettingsLoaderImpl mUserSettingsLoader;
    private ClassStaticBlockPreLoadThread mClassPreloadThread = null;
    private ClassStaticBlockPreLoadThread.PreloadDoneCallback mClassPreloadDoneCallback = null;

    public interface Pausable {
        void pause();

        void resume();
    }

    public CameraApplication() {
        sContext = this;
    }

    public static final Handler getUiThreadHandler() {
        return sUiThreadHandler;
    }

    @Override // android.app.Application
    public void onCreate() {
        PerfLog.APPLICATION_ON_CREATE.begin();
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : E");
        }
        this.mStorage = new StorageImpl();
        this.mStorage.open(getApplicationContext());
        this.mUserSettingsLoader = new UserSettingsLoaderImpl(this, this.mStorage);
        PlatformCapability.prepareAsync(new PlatformCapability.OnPlatformCapabilityPreparedCallback() { // from class: com.sonyericsson.android.camera.CameraApplication.1
            @Override // com.sonyericsson.android.camera.util.capability.PlatformCapability.OnPlatformCapabilityPreparedCallback
            public void onPrepared() {
                if (CamLog.DEBUG) {
                    CamLog.d("PlatformCapability Prepared");
                }
                CameraApplication.this.mUserSettingsLoader.load();
            }
        });
        this.mCameraDeviceHandler = new CameraDeviceHandler(sContext);
        this.mClassPreloadDoneCallback = new ClassPreloadDoneCallback();
        this.mClassPreloadThread = new ClassStaticBlockPreLoadThread(this.mClassPreloadDoneCallback);
        this.mClassPreloadThread.setPriority(10);
        this.mClassPreloadThread.setName(ClassStaticBlockPreLoadThread.TAG);
        this.mClassPreloadThread.start();
        ((PowerManager) getSystemService("power")).newWakeLock(1, ClassStaticBlockPreLoadThread.TAG).acquire(2000L);
        super.onCreate();
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mStorage);
        registerActivityLifecycleCallbacks(new ActivityLifeCycleCallbackImpl(arrayList));
        DebugParameterUtils.INSTANCE.preload(this);
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : X");
        }
        PerfLog.APPLICATION_ON_CREATE.end();
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        this.mUserSettingsLoader.release();
        this.mStorage.close();
    }

    public static Context getContext() {
        return sContext;
    }

    public UserSettingsLoader getUserSettingsLoader() {
        return this.mUserSettingsLoader;
    }

    Storage getStorage() {
        return this.mStorage;
    }

    private static class ActivityLifeCycleCallbackImpl implements Application.ActivityLifecycleCallbacks {
        List<Activity> mForegroundActivity = new LinkedList();
        List<Pausable> mPausables;

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }

        public ActivityLifeCycleCallbackImpl(@NonNull List<Pausable> list) {
            this.mPausables = list;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
            if (activity instanceof CameraActivity) {
                if (this.mForegroundActivity.isEmpty()) {
                    Iterator<Pausable> it = this.mPausables.iterator();
                    while (it.hasNext()) {
                        it.next().resume();
                    }
                }
                this.mForegroundActivity.add(activity);
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
            if (activity instanceof CameraActivity) {
                this.mForegroundActivity.remove(activity);
                if (this.mForegroundActivity.isEmpty()) {
                    Iterator<Pausable> it = this.mPausables.iterator();
                    while (it.hasNext()) {
                        it.next().pause();
                    }
                }
            }
        }
    }

    CameraDeviceHandler getCameraDevice() {
        return this.mCameraDeviceHandler;
    }

    private class ClassPreloadDoneCallback implements ClassStaticBlockPreLoadThread.PreloadDoneCallback {
        private ClassPreloadDoneCallback() {
        }

        @Override // com.sonyericsson.android.camera.ClassStaticBlockPreLoadThread.PreloadDoneCallback
        public void onPreloadDone() {
            CameraApplication.this.mClassPreloadThread = null;
            CameraApplication.this.mClassPreloadDoneCallback = null;
        }
    }
}
