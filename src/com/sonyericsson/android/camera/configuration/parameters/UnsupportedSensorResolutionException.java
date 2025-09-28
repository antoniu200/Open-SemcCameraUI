package com.sonyericsson.android.camera.configuration.parameters;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class UnsupportedSensorResolutionException extends RuntimeException {
    private static final long serialVersionUID = -9181369815613920691L;

    public UnsupportedSensorResolutionException(int i) {
        super("Sensor which max picture width = " + i + " is not supported");
    }
}
