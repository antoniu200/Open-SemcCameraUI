// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import android.graphics.Rect;
import java.nio.ByteBuffer;
import android.os.Handler;

public interface ImageRetriever
{
    void registerPreviewStreamingCallback(final OnImageRetrieverCallback p0, final Handler p1);
    
    void requestOneShotPreviewCallback(final OnImageRetrieverCallback p0, final Handler p1);
    
    void unregisterPreviewStreamingCallback(final OnImageRetrieverCallback p0);
    
    public static class CaptureImageRequest
    {
        protected final OnImageRetrieverCallback callback;
        protected final Handler handler;
        protected final boolean isOneShot;
        
        public CaptureImageRequest(final OnImageRetrieverCallback callback, final boolean isOneShot, final Handler handler) {
            this.callback = callback;
            this.isOneShot = isOneShot;
            this.handler = handler;
        }
    }
    
    public interface OnImageRetrieverCallback
    {
        void onRetrieved(final ByteBuffer p0, final int p1, final Rect p2);
    }
}
