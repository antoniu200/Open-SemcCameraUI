// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import android.os.SystemClock;
import android.util.Log;

public enum PerfLog
{
    private static final PerfLog[] $VALUES;
    
    ACTIVITY_ON_CREATE, 
    ACTIVITY_ON_DESTROY, 
    ACTIVITY_ON_PAUSE, 
    ACTIVITY_ON_RESUME, 
    ACTIVITY_ON_STOP, 
    APPLICATION_ON_CREATE, 
    APPLICATION_PRELOAD_THREAD, 
    BIND_SYSMON_SERVICE, 
    BURST_STORE_COMPLETE, 
    BYPASSCAMERA_ON_IMAGE_AVAILABLE, 
    BYPASSCAMERA_ON_SHUTTER_DONE, 
    BYPASSCAMERA_ON_SNAPSHOT_DONE, 
    BYPASSCAMERA_ON_STORE_COMPLETE, 
    BYPASSCAMERA_PREPARE, 
    BYPASSCAMERA_REQUEST_SNAPSHOT, 
    CAPTURE_BUTTON_TAP, 
    CREATE_CAPTURE_SESSION_TASK, 
    DCF_PATH_BUILDER_SCAN, 
    EVF_REQUEST_RESIZE, 
    FAST_CAMERA_BUTTON_INTENT_RECEIVED, 
    FAST_PRE_CAPTURE, 
    FAST_PRE_SCAN, 
    FAST_STORE_DONE;
    
    public static final boolean IS_ENABLE;
    
    LOAD_USER_SETTING_ALL, 
    LOAD_USER_SETTING_CURRENT, 
    MODE_CHANGE_SHOW_SURFACE, 
    MODE_CHANGE_TASK_END, 
    MODE_CHANGE_TASK_START, 
    ON_CONFIGURED, 
    OPEN_BYPASS_CAMERA_TASK, 
    OPEN_CAMERA_TASK, 
    PLATFORM_CAPABILITY_PREPARE, 
    PREPARE_IMAGE_READER_STREAMING, 
    PREPARE_IMAGE_READER_VIDEO_THUMBNAIL, 
    RESIZE_EVF, 
    SET_REPEATING_REQUEST_TASK, 
    START_PREVIEW, 
    START_REC, 
    STATE_RESUME, 
    STOP_REC, 
    STORAGE_MANAGER_SETUP, 
    STORE_COMPLETE, 
    SURFACE_CHANGED, 
    SURFACE_CREATED, 
    SURFACE_DESTROYED, 
    SWIPE_ANIMATION_END, 
    SWIPE_ANIMATION_START;
    
    private static final String TAG = "CAMPERF";
    
    TASK_INFLATE, 
    TASK_VIEW_FINDER_INITIALIZATION, 
    THUMBNAIL_SHOW, 
    VIEWFINDER_FIRST_DRAW, 
    VIEWFINDER_SETUP_HEADUP_DISPLAY;
    
    private final String mText;
    
    static {
        $VALUES = new PerfLog[] { PerfLog.APPLICATION_ON_CREATE, PerfLog.APPLICATION_PRELOAD_THREAD, PerfLog.ACTIVITY_ON_CREATE, PerfLog.ACTIVITY_ON_RESUME, PerfLog.ACTIVITY_ON_PAUSE, PerfLog.ACTIVITY_ON_STOP, PerfLog.ACTIVITY_ON_DESTROY, PerfLog.OPEN_BYPASS_CAMERA_TASK, PerfLog.OPEN_CAMERA_TASK, PerfLog.CREATE_CAPTURE_SESSION_TASK, PerfLog.ON_CONFIGURED, PerfLog.SET_REPEATING_REQUEST_TASK, PerfLog.START_PREVIEW, PerfLog.PLATFORM_CAPABILITY_PREPARE, PerfLog.RESIZE_EVF, PerfLog.SURFACE_CREATED, PerfLog.SURFACE_CHANGED, PerfLog.SURFACE_DESTROYED, PerfLog.EVF_REQUEST_RESIZE, PerfLog.BIND_SYSMON_SERVICE, PerfLog.LOAD_USER_SETTING_CURRENT, PerfLog.LOAD_USER_SETTING_ALL, PerfLog.STATE_RESUME, PerfLog.VIEWFINDER_SETUP_HEADUP_DISPLAY, PerfLog.VIEWFINDER_FIRST_DRAW, PerfLog.STORAGE_MANAGER_SETUP, PerfLog.DCF_PATH_BUILDER_SCAN, PerfLog.SWIPE_ANIMATION_START, PerfLog.SWIPE_ANIMATION_END, PerfLog.MODE_CHANGE_TASK_START, PerfLog.MODE_CHANGE_TASK_END, PerfLog.MODE_CHANGE_SHOW_SURFACE, PerfLog.START_REC, PerfLog.STOP_REC, PerfLog.CAPTURE_BUTTON_TAP, PerfLog.THUMBNAIL_SHOW, PerfLog.STORE_COMPLETE, PerfLog.BURST_STORE_COMPLETE, PerfLog.FAST_CAMERA_BUTTON_INTENT_RECEIVED, PerfLog.FAST_PRE_SCAN, PerfLog.FAST_PRE_CAPTURE, PerfLog.FAST_STORE_DONE, PerfLog.BYPASSCAMERA_PREPARE, PerfLog.BYPASSCAMERA_REQUEST_SNAPSHOT, PerfLog.BYPASSCAMERA_ON_SHUTTER_DONE, PerfLog.BYPASSCAMERA_ON_SNAPSHOT_DONE, PerfLog.BYPASSCAMERA_ON_IMAGE_AVAILABLE, PerfLog.BYPASSCAMERA_ON_STORE_COMPLETE, PerfLog.TASK_VIEW_FINDER_INITIALIZATION, PerfLog.TASK_INFLATE, PerfLog.PREPARE_IMAGE_READER_VIDEO_THUMBNAIL, PerfLog.PREPARE_IMAGE_READER_STREAMING };
        IS_ENABLE = Log.isLoggable("CAMPERF", 3);
    }
    
    private PerfLog() {
        this.mText = this.name();
    }
    
    private void log(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append(SystemClock.uptimeMillis());
        sb.append(',');
        sb.append(str);
        sb.append('}');
        Log.e("CAMPERF", sb.toString());
    }
    
    public void begin() {
        if (PerfLog.IS_ENABLE) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mText);
            sb.append("_E");
            this.log(sb.toString());
        }
    }
    
    public void end() {
        if (PerfLog.IS_ENABLE) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mText);
            sb.append("_X");
            this.log(sb.toString());
        }
    }
    
    public void transit() {
        if (PerfLog.IS_ENABLE) {
            this.log(this.mText);
        }
    }
}
