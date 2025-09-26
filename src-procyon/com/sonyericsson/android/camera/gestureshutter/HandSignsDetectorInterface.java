// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.gestureshutter;

import android.graphics.Rect;
import com.sonyericsson.android.camera.device.ImageRetriever;
import com.sonyericsson.android.camera.CameraActivity;

public interface HandSignsDetectorInterface
{
    int getDetectHeight();
    
    int getDetectWidth();
    
    boolean isStarted();
    
    void release();
    
    void setLayoutOrientation(final CameraActivity.LayoutOrientation p0);
    
    void startDetect(final ImageRetriever p0);
    
    void stopDetect();
    
    public interface DetectResultInterface
    {
        Rect getArea();
        
        HandStatus getStatus();
        
        public enum HandStatus
        {
            private static final HandStatus[] $VALUES;
            
            NONE, 
            PALM;
            
            static {
                $VALUES = new HandStatus[] { HandStatus.NONE, HandStatus.PALM };
            }
        }
    }
    
    public interface DetectResultListener
    {
        void onDetectResult(final DetectResultInterface p0);
    }
}
