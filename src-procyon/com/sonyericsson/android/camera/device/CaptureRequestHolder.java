// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import android.graphics.Rect;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.Iterator;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;
import android.hardware.camera2.CameraDevice;
import com.sonyericsson.android.camera.util.CamLog;
import android.hardware.camera2.CaptureRequest$Builder;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import android.hardware.camera2.CaptureRequest$Key;
import java.util.Map;

class CaptureRequestHolder
{
    private static final String TAG = "CaptureRequestHolder";
    private final Map<CaptureRequest$Key<?>, Object> mCaptureRequests;
    private final ReadWriteLock mReadWriteLock;
    
    CaptureRequestHolder() {
        this.mReadWriteLock = new ReentrantReadWriteLock(true);
        this.mCaptureRequests = new HashMap<CaptureRequest$Key<?>, Object>();
    }
    
    private void clear() {
        synchronized (this) {
            this.mCaptureRequests.clear();
        }
    }
    
    private <T> void setRequest(final CaptureRequest$Builder captureRequest$Builder, final CaptureRequest$Key<T> captureRequest$Key) {
        try {
            captureRequest$Builder.set((CaptureRequest$Key)captureRequest$Key, this.get((android.hardware.camera2.CaptureRequest$Key<Object>)captureRequest$Key));
        }
        catch (final IllegalArgumentException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setRequest(): key (");
            sb.append(captureRequest$Key.getName());
            sb.append(") is not valid.");
            CamLog.e(sb.toString());
        }
    }
    
    public CaptureRequestHolder copy() {
        synchronized (this) {
            final CaptureRequestHolder captureRequestHolder = new CaptureRequestHolder();
            captureRequestHolder.mCaptureRequests.putAll(this.mCaptureRequests);
            return captureRequestHolder;
        }
    }
    
    CaptureRequest createCaptureRequest(final CameraDevice cameraDevice, int i, final Object tag, final Surface... array) {
        synchronized (this) {
            if (CamLog.VERBOSE) {
                CamLog.d("createCaptureRequest() E");
            }
            try {
                final CaptureRequest$Builder captureRequest = cameraDevice.createCaptureRequest(i);
                final Iterator<CaptureRequest$Key<?>> iterator = this.mCaptureRequests.keySet().iterator();
                while (iterator.hasNext()) {
                    this.setRequest(captureRequest, iterator.next());
                }
                int length;
                for (length = array.length, i = 0; i < length; ++i) {
                    captureRequest.addTarget(array[i]);
                }
                captureRequest.setTag(tag);
                if (CamLog.VERBOSE) {
                    CamLog.d("createCaptureRequest() X");
                }
                return captureRequest.build();
            }
            catch (final CameraAccessException ex) {
                CamLog.e("createCaptureRequest() X : Exception", (Throwable)ex);
                return null;
            }
        }
    }
    
    CaptureRequest createCaptureRequest(final CameraDevice cameraDevice, final int n, final Surface... array) {
        synchronized (this) {
            return this.createCaptureRequest(cameraDevice, n, (Object)null, array);
        }
    }
    
     <T> T get(final CaptureRequest$Key<T> captureRequest$Key) {
        synchronized (this) {
            this.mReadWriteLock.readLock().lock();
            try {
                final Object value = this.mCaptureRequests.get(captureRequest$Key);
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("get() : key = ");
                    sb.append(captureRequest$Key.getName());
                    sb.append(", value = ");
                    sb.append(value);
                    CamLog.d(sb.toString());
                }
                return (T)value;
            }
            finally {
                this.mReadWriteLock.readLock().unlock();
            }
        }
    }
    
     <T> void set(final CaptureRequest$Key<T> captureRequest$Key, final T obj) {
        synchronized (this) {
            this.mReadWriteLock.writeLock().lock();
            try {
                this.mCaptureRequests.put(captureRequest$Key, obj);
                this.mReadWriteLock.writeLock().unlock();
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("set() : key = ");
                    sb.append(captureRequest$Key.getName());
                    sb.append(", value = ");
                    sb.append(obj);
                    CamLog.d(sb.toString());
                }
            }
            finally {
                this.mReadWriteLock.writeLock().unlock();
            }
        }
    }
    
    void setDefault(final CameraInfo.CameraId cameraId) {
        this.clear();
        final Rect activeArraySize = PlatformCapability.getActiveArraySize(cameraId);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_OBJECT_SELECT_TRIGGER_AREA, new int[] { activeArraySize.left, activeArraySize.top, activeArraySize.right, activeArraySize.bottom });
        this.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SELECT_TRIGGER_AREA, new int[] { activeArraySize.left, activeArraySize.top, activeArraySize.right, activeArraySize.bottom });
        this.set((android.hardware.camera2.CaptureRequest$Key<Integer>)CaptureRequest.STATISTICS_FACE_DETECT_MODE, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_FACE_SMILE_SCORES_MODE, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_SCENE_DETECT_MODE, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_STATISTICS_CONDITION_DETECT_MODE, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_MODE, 1);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AE_REGION_MODE, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AF_REGION_MODE, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_AB, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_AWB_COLOR_COMPENSATION_GM, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_SKIN_SMOOTH_LEVEL, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_STILL_HDR_MODE, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_POWER_SAVE_MODE, 0);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_EXPOSURE_TIME_LIMIT, PlatformCapability.getMinExposureTimeLimit(cameraId));
        this.set(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_SENSITIVITY_HINT, 50);
        this.set(SomcCaptureRequestKeys.SONYMOBILE_SENSOR_EXPOSURE_TIME_HINT, PlatformCapability.getMaxShutterSpeed(cameraId));
        this.set(SomcCaptureRequestKeys.SONYMOBILE_CONTROL_FUSION_MODE, 0);
    }
}
