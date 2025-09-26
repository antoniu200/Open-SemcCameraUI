// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.util.SignatureUtil;
import android.view.InputDevice;
import java.util.Iterator;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import java.util.ArrayList;
import android.content.SharedPreferences;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.Context;
import java.util.List;

public class PlatformCapabilityList
{
    private static final String KEY_SENSOR_CAMERA_LIFT_TRIGGER = "sensor_camera_lift_trigger";
    private static final String KEY_SIDE_SENSE = "somc_side_sense";
    private static final String KEY_WEARABLE = "somc_wearable";
    private static final String SENSOR_CAMERA_LIFT_TRIGGER = "com.sonymobile.sensor.camera_lift_trigger";
    private static final String TAG = "PlatformCapabilityList";
    private static final String WEARABLE_BRIDGE_PACKAGE_NAME = "com.sonymobile.cameracommon.wearablebridge";
    public final CapabilityItem<Boolean> CAMERA_LIFT_TRIGGER;
    public final CapabilityItem<Boolean> SIDE_SENSE;
    public final CapabilityItem<Boolean> WEARABLE;
    private final List<CapabilityItem<?>> mValues;
    
    public PlatformCapabilityList(final Context context) {
        if (CamLog.VERBOSE) {
            CamLog.d("loadPlatformCapabilityFromDevice");
        }
        this.CAMERA_LIFT_TRIGGER = new BooleanCapabilityItem("sensor_camera_lift_trigger", this.isLiftTriggerSupported(context));
        this.SIDE_SENSE = new BooleanCapabilityItem("somc_side_sense", this.isSideSenseSupported());
        this.WEARABLE = new BooleanCapabilityItem("somc_wearable", this.isWearableSupported(context));
        this.mValues = this.createList();
    }
    
    public PlatformCapabilityList(final Context context, final SharedPreferences sharedPreferences) {
        if (CamLog.VERBOSE) {
            CamLog.d("loadPlatformCapabilityFromSharedPreferences");
        }
        this.CAMERA_LIFT_TRIGGER = new BooleanCapabilityItem("sensor_camera_lift_trigger", sharedPreferences);
        this.SIDE_SENSE = new BooleanCapabilityItem("somc_side_sense", sharedPreferences);
        this.WEARABLE = new BooleanCapabilityItem("somc_wearable", sharedPreferences);
        this.mValues = this.createList();
    }
    
    private List<CapabilityItem<?>> createList() {
        final ArrayList list = new ArrayList();
        list.add(this.CAMERA_LIFT_TRIGGER);
        list.add(this.SIDE_SENSE);
        list.add(this.WEARABLE);
        return list;
    }
    
    private boolean isLiftTriggerSupported(final Context context) {
        final Iterator iterator = ((SensorManager)context.getSystemService("sensor")).getSensorList(-1).iterator();
        while (iterator.hasNext()) {
            if (((Sensor)iterator.next()).getStringType().equals("com.sonymobile.sensor.camera_lift_trigger")) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isSideSenseSupported() {
        final int[] deviceIds = InputDevice.getDeviceIds();
        for (int length = deviceIds.length, i = 0; i < length; ++i) {
            if (InputDevice.getDevice(deviceIds[i]).supportsSource(536870912)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isWearableSupported(final Context context) {
        return SignatureUtil.isAvailable(context, "com.sonymobile.cameracommon.wearablebridge");
    }
    
    @NonNull
    public List<CapabilityItem<?>> values() {
        return this.mValues;
    }
}
