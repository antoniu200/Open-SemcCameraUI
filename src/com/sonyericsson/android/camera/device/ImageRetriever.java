package com.sonyericsson.android.camera.device;

import android.graphics.Rect;
import android.os.Handler;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface ImageRetriever {

    public interface OnImageRetrieverCallback {
        void onRetrieved(ByteBuffer byteBuffer, int i, Rect rect);
    }

    void registerPreviewStreamingCallback(OnImageRetrieverCallback onImageRetrieverCallback, Handler handler);

    void requestOneShotPreviewCallback(OnImageRetrieverCallback onImageRetrieverCallback, Handler handler);

    void unregisterPreviewStreamingCallback(OnImageRetrieverCallback onImageRetrieverCallback);

    public static class CaptureImageRequest {
        protected final OnImageRetrieverCallback callback;
        protected final Handler handler;
        protected final boolean isOneShot;

        public CaptureImageRequest(OnImageRetrieverCallback onImageRetrieverCallback, boolean z, Handler handler) {
            this.callback = onImageRetrieverCallback;
            this.isOneShot = z;
            this.handler = handler;
        }
    }
}
