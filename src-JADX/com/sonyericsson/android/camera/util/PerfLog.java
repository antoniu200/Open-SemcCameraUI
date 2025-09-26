package com.sonyericsson.android.camera.util;

import android.os.SystemClock;
import android.util.Log;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum PerfLog {
    APPLICATION_ON_CREATE,
    APPLICATION_PRELOAD_THREAD,
    ACTIVITY_ON_CREATE,
    ACTIVITY_ON_RESUME,
    ACTIVITY_ON_PAUSE,
    ACTIVITY_ON_STOP,
    ACTIVITY_ON_DESTROY,
    OPEN_BYPASS_CAMERA_TASK,
    OPEN_CAMERA_TASK,
    CREATE_CAPTURE_SESSION_TASK,
    ON_CONFIGURED,
    SET_REPEATING_REQUEST_TASK,
    START_PREVIEW,
    PLATFORM_CAPABILITY_PREPARE,
    RESIZE_EVF,
    SURFACE_CREATED,
    SURFACE_CHANGED,
    SURFACE_DESTROYED,
    EVF_REQUEST_RESIZE,
    BIND_SYSMON_SERVICE,
    LOAD_USER_SETTING_CURRENT,
    LOAD_USER_SETTING_ALL,
    STATE_RESUME,
    VIEWFINDER_SETUP_HEADUP_DISPLAY,
    VIEWFINDER_FIRST_DRAW,
    STORAGE_MANAGER_SETUP,
    DCF_PATH_BUILDER_SCAN,
    SWIPE_ANIMATION_START,
    SWIPE_ANIMATION_END,
    MODE_CHANGE_TASK_START,
    MODE_CHANGE_TASK_END,
    MODE_CHANGE_SHOW_SURFACE,
    START_REC,
    STOP_REC,
    CAPTURE_BUTTON_TAP,
    THUMBNAIL_SHOW,
    STORE_COMPLETE,
    BURST_STORE_COMPLETE,
    FAST_CAMERA_BUTTON_INTENT_RECEIVED,
    FAST_PRE_SCAN,
    FAST_PRE_CAPTURE,
    FAST_STORE_DONE,
    BYPASSCAMERA_PREPARE,
    BYPASSCAMERA_REQUEST_SNAPSHOT,
    BYPASSCAMERA_ON_SHUTTER_DONE,
    BYPASSCAMERA_ON_SNAPSHOT_DONE,
    BYPASSCAMERA_ON_IMAGE_AVAILABLE,
    BYPASSCAMERA_ON_STORE_COMPLETE,
    TASK_VIEW_FINDER_INITIALIZATION,
    TASK_INFLATE,
    PREPARE_IMAGE_READER_VIDEO_THUMBNAIL,
    PREPARE_IMAGE_READER_STREAMING;

    private final String mText = name();
    private static final String TAG = "CAMPERF";
    public static final boolean IS_ENABLE = Log.isLoggable(TAG, 3);

    PerfLog() {
    }

    public void begin() {
        if (IS_ENABLE) {
            log(this.mText + "_E");
        }
    }

    public void end() {
        if (IS_ENABLE) {
            log(this.mText + "_X");
        }
    }

    public void transit() {
        if (IS_ENABLE) {
            log(this.mText);
        }
    }

    private void log(String str) {
        Log.e(TAG, '{' + SystemClock.uptimeMillis() + ',' + str + '}');
    }
}
