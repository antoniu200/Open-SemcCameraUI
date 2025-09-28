package com.sonyericsson.android.camera.device;

import android.hardware.camera2.CaptureResult;
import com.sonyericsson.android.camera.util.CamLog;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class CaptureResultHolder {
    private static final int QUEUE_CAPACITY = 10;
    private static final String TAG = "CaptureResultHolder";
    private final ReadWriteLock mReadWriteLock = new ReentrantReadWriteLock(true);
    private LinkedBlockingDeque<CaptureResult> mCaptureResultQueue = new LinkedBlockingDeque<>(10);

    CaptureResultHolder() {
    }

    void add(CaptureResult captureResult) {
        this.mReadWriteLock.writeLock().lock();
        try {
            if (this.mCaptureResultQueue.remainingCapacity() == 0) {
                this.mCaptureResultQueue.poll();
            }
            this.mCaptureResultQueue.put(captureResult);
        } catch (InterruptedException unused) {
        } catch (Throwable th) {
            this.mReadWriteLock.writeLock().unlock();
            throw th;
        }
        this.mReadWriteLock.writeLock().unlock();
    }

    CaptureResult getLatest() {
        this.mReadWriteLock.readLock().lock();
        try {
            if (CamLog.VERBOSE) {
                CamLog.d("getLatest()");
            }
            return this.mCaptureResultQueue.peekLast();
        } finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }

    <T> List<T> getValueList(CaptureResult.Key<T> key) {
        this.mReadWriteLock.readLock().lock();
        try {
            if (CamLog.VERBOSE) {
                CamLog.d("getValueList(): " + key.getName());
            }
            ArrayList arrayList = new ArrayList();
            Iterator<CaptureResult> it = this.mCaptureResultQueue.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().get(key));
            }
            return arrayList;
        } finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }

    <T> T getLatestValue(CaptureResult.Key<T> key) {
        this.mReadWriteLock.readLock().lock();
        try {
            if (CamLog.VERBOSE) {
                CamLog.d("getLatestValue(): " + key.getName());
            }
            CaptureResult latest = getLatest();
            if (latest != null) {
                return (T) latest.get(key);
            }
            return null;
        } finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }

    void dumpLatest() {
        this.mReadWriteLock.readLock().lock();
        try {
            if (CamLog.VERBOSE) {
                CamLog.d("dumpLatest()");
            }
            CaptureResult captureResultPeekLast = this.mCaptureResultQueue.peekLast();
            if (captureResultPeekLast == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("empty");
                }
                return;
            }
            Iterator<CaptureResult.Key<?>> it = captureResultPeekLast.getKeys().iterator();
            while (it.hasNext()) {
                CaptureResult.Key<?> applicationCaptureResultKey = getApplicationCaptureResultKey(it.next());
                StringBuilder sb = new StringBuilder();
                sb.append("key: ");
                sb.append(applicationCaptureResultKey);
                sb.append(',');
                sb.append("val: ");
                if (captureResultPeekLast.get(applicationCaptureResultKey) != null) {
                    if (captureResultPeekLast.get(applicationCaptureResultKey).getClass().isArray()) {
                        for (int i = 0; i < Array.getLength(captureResultPeekLast.get(applicationCaptureResultKey)); i++) {
                            sb.append(Array.get(captureResultPeekLast.get(applicationCaptureResultKey), i));
                            sb.append(',');
                        }
                    } else {
                        sb.append(captureResultPeekLast.get(applicationCaptureResultKey));
                    }
                } else {
                    sb.append("null");
                }
                if (CamLog.VERBOSE) {
                    CamLog.d(sb.toString());
                }
            }
        } finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }

    private CaptureResult.Key<?> getApplicationCaptureResultKey(CaptureResult.Key<?> key) {
        String name = key.getName();
        if (name != null) {
            for (CaptureResult.Key<?> key2 : SomcCameraDeviceInfo.getAllCaptureResultKeys()) {
                if (key2.getName().equals(name)) {
                    return key2;
                }
            }
        }
        return key;
    }
}
