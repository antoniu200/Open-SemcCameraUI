// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client;

import com.sonymobile.cameracommon.vanilla.wearablebridge.common.AbstractCapturableState;

public interface NotifyWearableInterface
{
    public interface LifeCycleNotifier
    {
        void onPause();
        
        void onResume();
    }
    
    public interface PhotoStateNotifier
    {
        void onCaptureFailed();
        
        void onCaptureSucceeded();
        
        void onStateChanged(final AbstractCapturableState.AbstractPhotoState p0);
    }
    
    public interface VideoStateNotifier
    {
        void onStartRecordingFailed();
        
        void onStartRecordingSucceeded();
        
        void onStateChanged(final AbstractCapturableState.AbstractVideoState p0);
        
        void onStopRecordingFailed();
        
        void onStopRecordingSucceeded();
    }
}
