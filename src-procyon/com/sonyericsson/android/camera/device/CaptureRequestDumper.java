// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.util.Collections;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Iterator;
import android.hardware.camera2.CaptureRequest$Key;
import java.util.HashMap;
import android.hardware.camera2.CaptureRequest;
import com.sonyericsson.android.camera.util.CamLog;
import android.hardware.camera2.params.MeteringRectangle;
import java.lang.reflect.Array;
import android.hardware.camera2.CameraCaptureSession;
import java.util.Map;

public class CaptureRequestDumper
{
    private Map<String, String> mLast;
    private Map<String, String> mPrev;
    private final String mTag;
    private final Type mType;
    
    public CaptureRequestDumper(final Type mType, final CameraCaptureSession cameraCaptureSession) {
        this.mType = mType;
        final StringBuilder sb = new StringBuilder();
        sb.append("[CaptureRequest:");
        sb.append(cameraCaptureSession.hashCode());
        sb.append("] ");
        this.mTag = sb.toString();
    }
    
    private static <T> T[] convertPrimitiveArrayToObjectArray(final Object o, final Class<T> componentType) {
        final int length = Array.getLength(o);
        if (length == 0) {
            throw new IllegalArgumentException("Input array shouldn't be empty");
        }
        final Object[] array = (Object[])Array.newInstance(componentType, length);
        for (int i = 0; i < length; ++i) {
            Array.set(array, i, Array.get(o, i));
        }
        return (T[])array;
    }
    
    private static String flatten(final Object[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (final Object obj : array) {
            if (sb.length() > 1) {
                sb.append(',');
            }
            sb.append(obj);
        }
        sb.append(']');
        return sb.toString();
    }
    
    private String toCaptureRequestValueString(final Object o) {
        if (o == null) {
            return null;
        }
        if (o.getClass().isArray()) {
            if (o instanceof byte[]) {
                return flatten(convertPrimitiveArrayToObjectArray(o, Byte.class));
            }
            if (o instanceof int[]) {
                return flatten(convertPrimitiveArrayToObjectArray(o, Integer.class));
            }
            if (o instanceof long[]) {
                return flatten(convertPrimitiveArrayToObjectArray(o, Long.class));
            }
            if (o instanceof MeteringRectangle[]) {
                return flatten((Object[])o);
            }
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Un-supported type:");
                sb.append(o.getClass().getSimpleName());
                CamLog.w(sb.toString());
            }
        }
        return o.toString();
    }
    
    private Map<String, String> toKeyValueMap(final CaptureRequest captureRequest) {
        final HashMap hashMap = new HashMap();
        for (final CaptureRequest$Key captureRequest$Key : captureRequest.getKeys()) {
            final String captureRequestValueString = this.toCaptureRequestValueString(captureRequest.get(captureRequest$Key));
            if (captureRequestValueString != null) {
                hashMap.put(captureRequest$Key.getName(), captureRequestValueString);
            }
        }
        return hashMap;
    }
    
    public void dump() {
        switch (CaptureRequestDumper$1.$SwitchMap$com$sonyericsson$android$camera$device$CaptureRequestDumper$Type[this.mType.ordinal()]) {
            case 2: {
                this.dumpDiff();
                break;
            }
            case 1: {
                this.dumpLatest();
                break;
            }
        }
    }
    
    public void dumpDiff() {
        synchronized (this) {
            final Map<String, String> mPrev = this.mPrev;
            final Map<String, String> mLast = this.mLast;
            monitorexit(this);
            final TreeSet set = new TreeSet();
            if (mPrev != null) {
                set.addAll(mPrev.keySet());
            }
            if (mLast != null) {
                set.addAll(mLast.keySet());
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mTag);
            sb.append("== DIFF ==");
            CamLog.i(sb.toString());
            for (final String s : set) {
                String str = null;
                Object anObject;
                if (mPrev == null) {
                    anObject = null;
                }
                else {
                    anObject = mPrev.get(s);
                }
                if (mLast != null) {
                    str = mLast.get(s);
                }
                if (str != null) {
                    if (str.equals(anObject)) {
                        continue;
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.mTag);
                    sb2.append(s);
                    sb2.append("=");
                    sb2.append(str);
                    CamLog.i(sb2.toString());
                }
                else {
                    if (anObject == null) {
                        continue;
                    }
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(this.mTag);
                    sb3.append(s);
                    sb3.append(" is removed");
                    CamLog.i(sb3.toString());
                }
            }
        }
    }
    
    public void dumpLatest() {
        synchronized (this) {
            final Map<String, String> mLast = this.mLast;
            monitorexit(this);
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mTag);
            sb.append("== LAST ==");
            CamLog.i(sb.toString());
            for (final Map.Entry<String, V> entry : mLast.entrySet()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.mTag);
                sb2.append(entry.getKey());
                sb2.append("=");
                sb2.append((String)entry.getValue());
                CamLog.i(sb2.toString());
            }
        }
    }
    
    public void update(final CaptureRequest captureRequest) {
        synchronized (this) {
            this.mPrev = this.mLast;
            this.mLast = Collections.unmodifiableMap((Map<? extends String, ? extends String>)this.toKeyValueMap(captureRequest));
        }
    }
    
    public enum Type
    {
        private static final Type[] $VALUES;
        
        DIFF, 
        LAST, 
        SILENT;
        
        static {
            $VALUES = new Type[] { Type.LAST, Type.DIFF, Type.SILENT };
        }
    }
}
