// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Array;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Iterator;
import android.hardware.camera2.CaptureResult$Key;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import android.hardware.camera2.CaptureResult;
import java.util.concurrent.LinkedBlockingDeque;

class CaptureResultHolder
{
    private static final int QUEUE_CAPACITY = 10;
    private static final String TAG = "CaptureResultHolder";
    private LinkedBlockingDeque<CaptureResult> mCaptureResultQueue;
    private final ReadWriteLock mReadWriteLock;
    
    CaptureResultHolder() {
        this.mReadWriteLock = new ReentrantReadWriteLock(true);
        this.mCaptureResultQueue = new LinkedBlockingDeque<CaptureResult>(10);
    }
    
    private CaptureResult$Key<?> getApplicationCaptureResultKey(final CaptureResult$Key<?> captureResult$Key) {
        final String name = captureResult$Key.getName();
        if (name != null) {
            for (final CaptureResult$Key captureResult$Key2 : SomcCameraDeviceInfo.getAllCaptureResultKeys()) {
                if (captureResult$Key2.getName().equals(name)) {
                    return (CaptureResult$Key<?>)captureResult$Key2;
                }
            }
        }
        return captureResult$Key;
    }
    
    void add(final CaptureResult e) {
        this.mReadWriteLock.writeLock().lock();
        try {
            if (this.mCaptureResultQueue.remainingCapacity() == 0) {
                this.mCaptureResultQueue.poll();
            }
            this.mCaptureResultQueue.put(e);
            goto Label_0060;
        }
        catch (final InterruptedException ex) {
            goto Label_0060;
        }
        finally {
            this.mReadWriteLock.writeLock().unlock();
        }
    }
    
    void dumpLatest() {
        this.mReadWriteLock.readLock().lock();
        try {
            if (CamLog.VERBOSE) {
                CamLog.d("dumpLatest()");
            }
            final CaptureResult captureResult = this.mCaptureResultQueue.peekLast();
            if (captureResult == null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("empty");
                }
                return;
            }
            final Iterator iterator = captureResult.getKeys().iterator();
            while (iterator.hasNext()) {
                final CaptureResult$Key<?> applicationCaptureResultKey = this.getApplicationCaptureResultKey((CaptureResult$Key<?>)iterator.next());
                final StringBuilder sb = new StringBuilder();
                sb.append("key: ");
                sb.append(applicationCaptureResultKey);
                sb.append(',');
                sb.append("val: ");
                if (captureResult.get((CaptureResult$Key)applicationCaptureResultKey) != null) {
                    if (captureResult.get((CaptureResult$Key)applicationCaptureResultKey).getClass().isArray()) {
                        for (int i = 0; i < Array.getLength(captureResult.get((CaptureResult$Key)applicationCaptureResultKey)); ++i) {
                            sb.append(Array.get(captureResult.get((CaptureResult$Key)applicationCaptureResultKey), i));
                            sb.append(',');
                        }
                    }
                    else {
                        sb.append(captureResult.get((CaptureResult$Key)applicationCaptureResultKey));
                    }
                }
                else {
                    sb.append("null");
                }
                if (CamLog.VERBOSE) {
                    CamLog.d(sb.toString());
                }
            }
        }
        finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }
    
    CaptureResult getLatest() {
        this.mReadWriteLock.readLock().lock();
        try {
            if (CamLog.VERBOSE) {
                CamLog.d("getLatest()");
            }
            return this.mCaptureResultQueue.peekLast();
        }
        finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }
    
     <T> T getLatestValue(final CaptureResult$Key<T> captureResult$Key) {
        this.mReadWriteLock.readLock().lock();
        try {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getLatestValue(): ");
                sb.append(captureResult$Key.getName());
                CamLog.d(sb.toString());
            }
            final CaptureResult latest = this.getLatest();
            if (latest != null) {
                return (T)latest.get((CaptureResult$Key)captureResult$Key);
            }
            return null;
        }
        finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }
    
     <T> List<T> getValueList(final CaptureResult$Key<T> captureResult$Key) {
        this.mReadWriteLock.readLock().lock();
        try {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getValueList(): ");
                sb.append(captureResult$Key.getName());
                CamLog.d(sb.toString());
            }
            final ArrayList list = new ArrayList();
            final Iterator<CaptureResult> iterator = this.mCaptureResultQueue.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next().get((CaptureResult$Key)captureResult$Key));
            }
            return list;
        }
        finally {
            this.mReadWriteLock.readLock().unlock();
        }
    }
}
