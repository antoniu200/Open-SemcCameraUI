// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.intent;

import android.content.ComponentName;

public class IntentConstants
{
    public static final ComponentName CAMERA_UI_COMPONENT_NAME;
    public static final ComponentName CAMERA_UI_ONE_SHOT_COMPONENT_NAME;
    public static final String EXTRA_CALLING_ACTIVITY = "calling-activity";
    public static final String EXTRA_CALLING_MODE = "calling-mode";
    public static final String EXTRA_CALLING_PACKAGE = "calling-package";
    public static final String EXTRA_CAPTURING_MODE = "com.sonymobile.camera.addon.intent.extra.CAPTURING_MODE";
    public static final String EXTRA_PERMISSIONS_LIST = "permissions_list";
    public static final String TAG = "IntentConstants";
    
    static {
        CAMERA_UI_COMPONENT_NAME = new ComponentName("com.sonyericsson.android.camera", "com.sonyericsson.android.camera.CameraActivity");
        CAMERA_UI_ONE_SHOT_COMPONENT_NAME = new ComponentName("com.sonyericsson.android.camera", "com.sonyericsson.android.camera.CameraActivityForCaptureOnlyPhoto");
    }
    
    public static class BroadcastIntent
    {
        public static final String ACTION_CAMERA_FINISH = "com.sonyericsson.android.camera.intent.action.FINISH";
        public static final String ACTION_CAMERA_FINISH_CAMERAACTIVITY = "com.sonyericsson.android.camera.intent.action.FINISH_CAMERAACTIVITY";
        public static final String ACTION_CAMERA_LAUNCH = "com.sonyericsson.android.camera.intent.action.LAUNCH";
        public static final String ACTION_CAMERA_NEW_PICT = "com.android.camera.NEW_PICTURE";
        public static final String ACTION_MUSICSERVICE_COMMAND = "com.android.music.musicservicecommand";
        public static final String EXTRA_PREDICTIVE_CAPTURE_DIRECTORY_PATH = "com.sonyericsson.android.camera.extra.PREDICTIVE_CAPTURE_DIRECTORY_PATH";
        public static final String MUSICSERVICE_COMMAND = "command";
        public static final String MUSICSERVICE_COMMAND_PAUSE = "pause";
        public static final String PREDICTIVE_CAPTURE_SAVE_COMPLETED = "com.sonyericsson.android.camera.intent.action.PREDICTIVE_CAPTURE_SAVE_COMPLETED";
    }
}
