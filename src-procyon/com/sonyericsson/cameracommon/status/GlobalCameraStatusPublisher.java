// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status;

import com.sonyericsson.cameracommon.status.global.RemovableCameraClients;
import com.sonyericsson.cameracommon.status.global.BuiltInCameraIds;
import com.sonyericsson.cameracommon.status.global.CameraId;
import android.content.Context;

public class GlobalCameraStatusPublisher extends CameraStatusPublisher<GlobalCameraStatusValue>
{
    public GlobalCameraStatusPublisher(final Context context) {
        super(context);
    }
    
    @Override
    public CameraStatusPublisher<GlobalCameraStatusValue> putDefaultAll() {
        ((CameraStatusPublisher<CameraId>)this).put(new CameraId(CameraId.defaultValue(this.getCameraCommonVersion())));
        ((CameraStatusPublisher<BuiltInCameraIds>)this).put(new BuiltInCameraIds(BuiltInCameraIds.DEFAULT_VALUE));
        ((CameraStatusPublisher<RemovableCameraClients>)this).put(new RemovableCameraClients(0));
        return this;
    }
}
