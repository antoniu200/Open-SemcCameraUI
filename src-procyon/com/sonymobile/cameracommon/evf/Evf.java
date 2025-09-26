// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.evf;

import android.content.Context;
import android.util.Size;
import android.graphics.Rect;
import android.view.View;
import android.view.Surface;

public interface Evf
{
    Surface asSurface();
    
    View asView();
    
    void clear();
    
    Rect getRect();
    
    Size getSurfaceSize();
    
    void hide();
    
    boolean isShown();
    
    void onCreate(final Context p0);
    
    void onDestroy();
    
    void onPause();
    
    void onResume();
    
    void resize(final int p0, final int p1);
    
    void setFixedSurfaceSize(final int p0, final int p1);
    
    void setLifeCycleCallback(final LifeCycleCallback p0);
    
    void show();
    
    public static class EvfFactory
    {
        public static Evf generate() {
            return new SurfaceViewEvf();
        }
    }
    
    public interface LifeCycleCallback
    {
        void onEvfFinalized(final Evf p0);
        
        void onEvfInitialized(final Evf p0, final int p1, final int p2);
        
        void onEvfSizeChanged(final Evf p0, final int p1, final int p2);
    }
}
