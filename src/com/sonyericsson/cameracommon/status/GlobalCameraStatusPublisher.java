package com.sonyericsson.cameracommon.status;

import android.content.Context;
import com.sonyericsson.cameracommon.status.global.BuiltInCameraIds;
import com.sonyericsson.cameracommon.status.global.CameraId;
import com.sonyericsson.cameracommon.status.global.RemovableCameraClients;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GlobalCameraStatusPublisher extends CameraStatusPublisher<GlobalCameraStatusValue> {
    public GlobalCameraStatusPublisher(Context context) {
        super(context);
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusPublisher
    public CameraStatusPublisher<GlobalCameraStatusValue> putDefaultAll() {
        put(new CameraId(CameraId.defaultValue(getCameraCommonVersion())));
        put(new BuiltInCameraIds(BuiltInCameraIds.DEFAULT_VALUE));
        put(new RemovableCameraClients(0));
        return this;
    }
}
