package com.sonyericsson.android.camera.gestureshutter;

import android.graphics.Rect;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.device.ImageRetriever;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface HandSignsDetectorInterface {

    public interface DetectResultInterface {

        public enum HandStatus {
            NONE,
            PALM
        }

        Rect getArea();

        HandStatus getStatus();
    }

    public interface DetectResultListener {
        void onDetectResult(DetectResultInterface detectResultInterface);
    }

    int getDetectHeight();

    int getDetectWidth();

    boolean isStarted();

    void release();

    void setLayoutOrientation(CameraActivity.LayoutOrientation layoutOrientation);

    void startDetect(ImageRetriever imageRetriever);

    void stopDetect();
}
