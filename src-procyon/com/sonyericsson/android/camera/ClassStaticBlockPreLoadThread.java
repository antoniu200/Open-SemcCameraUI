// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import com.sonyericsson.cameracommon.focusview.SmileScore;
import com.sonyericsson.cameracommon.capturefeedback.contextview.GLSurfaceContextView;
import com.sonyericsson.android.camera.view.LayoutAsyncInflateItems;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.util.PerfLog;

class ClassStaticBlockPreLoadThread extends Thread
{
    private static final boolean IS_CLASS_LOAD_TIME_MEASUREMENT = false;
    public static final String TAG = "ClassStaticBlockPreLoadThread";
    private final PreloadDoneCallback mCallback;
    
    public ClassStaticBlockPreLoadThread(final PreloadDoneCallback mCallback) {
        this.mCallback = mCallback;
    }
    
    private final void preload() {
        this.preloadViaFunctionCall();
        if (this.mCallback != null) {
            this.mCallback.onPreloadDone();
        }
    }
    
    private final void preloadViaFunctionCall() {
        PerfLog.APPLICATION_PRELOAD_THREAD.begin();
        CameraButtonIntentReceiver.preload();
        CameraActivity.preload();
        CameraDeviceHandler.preload();
        StateMachine.preload();
        ViewFinderImpl.preload();
        VideoSize.preload();
        com.sonyericsson.android.camera.Constants.preload();
        Configurations.preload();
        SharedPreferencesConstants.preload();
        DestinationToSave.preload();
        Facing.preload();
        VideoStabilizer.preload();
        LayoutAsyncInflateItems.preload();
        GLSurfaceContextView.preload();
        SmileScore.preload();
        PerfLog.APPLICATION_PRELOAD_THREAD.end();
    }
    
    @Override
    public void run() {
        this.preload();
    }
    
    interface PreloadDoneCallback
    {
        void onPreloadDone();
    }
}
