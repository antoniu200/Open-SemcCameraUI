package com.sonyericsson.android.camera.device;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import com.sonyericsson.android.camera.util.CamLog;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CaptureRequestDumper {
    private Map<String, String> mLast;
    private Map<String, String> mPrev;
    private final String mTag;
    private final Type mType;

    public enum Type {
        LAST,
        DIFF,
        SILENT
    }

    public CaptureRequestDumper(Type type, CameraCaptureSession cameraCaptureSession) {
        this.mType = type;
        this.mTag = "[CaptureRequest:" + cameraCaptureSession.hashCode() + "] ";
    }

    public void update(CaptureRequest captureRequest) {
        synchronized (this) {
            this.mPrev = this.mLast;
            this.mLast = Collections.unmodifiableMap(toKeyValueMap(captureRequest));
        }
    }

    public void dump() {
        switch (this.mType) {
            case LAST:
                dumpLatest();
                break;
            case DIFF:
                dumpDiff();
                break;
        }
    }

    public void dumpLatest() {
        Map<String, String> map;
        synchronized (this) {
            map = this.mLast;
        }
        CamLog.i(this.mTag + "== LAST ==");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            CamLog.i(this.mTag + entry.getKey() + "=" + entry.getValue());
        }
    }

    public void dumpDiff() {
        Map<String, String> map;
        Map<String, String> map2;
        synchronized (this) {
            map = this.mPrev;
            map2 = this.mLast;
        }
        TreeSet<String> treeSet = new TreeSet();
        if (map != null) {
            treeSet.addAll(map.keySet());
        }
        if (map2 != null) {
            treeSet.addAll(map2.keySet());
        }
        CamLog.i(this.mTag + "== DIFF ==");
        for (String str : treeSet) {
            String str2 = map == null ? null : map.get(str);
            String str3 = map2 != null ? map2.get(str) : null;
            if (str3 != null) {
                if (!str3.equals(str2)) {
                    CamLog.i(this.mTag + str + "=" + str3);
                }
            } else if (str2 != null) {
                CamLog.i(this.mTag + str + " is removed");
            }
        }
    }

    private Map<String, String> toKeyValueMap(CaptureRequest captureRequest) {
        HashMap map = new HashMap();
        for (CaptureRequest.Key<?> key : captureRequest.getKeys()) {
            String captureRequestValueString = toCaptureRequestValueString(captureRequest.get(key));
            if (captureRequestValueString != null) {
                map.put(key.getName(), captureRequestValueString);
            }
        }
        return map;
    }

    private String toCaptureRequestValueString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof byte[]) {
                return flatten(convertPrimitiveArrayToObjectArray(obj, Byte.class));
            }
            if (obj instanceof int[]) {
                return flatten(convertPrimitiveArrayToObjectArray(obj, Integer.class));
            }
            if (obj instanceof long[]) {
                return flatten(convertPrimitiveArrayToObjectArray(obj, Long.class));
            }
            if (obj instanceof MeteringRectangle[]) {
                return flatten((Object[]) obj);
            }
            if (CamLog.DEBUG) {
                CamLog.w("Un-supported type:" + obj.getClass().getSimpleName());
            }
        }
        return obj.toString();
    }

    private static <T> T[] convertPrimitiveArrayToObjectArray(Object obj, Class<T> cls) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int length = Array.getLength(obj);
        if (length == 0) {
            throw new IllegalArgumentException("Input array shouldn't be empty");
        }
        T[] tArr = (T[]) ((Object[]) Array.newInstance((Class<?>) cls, length));
        for (int i = 0; i < length; i++) {
            Array.set(tArr, i, Array.get(obj, i));
        }
        return tArr;
    }

    private static String flatten(Object[] objArr) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Object obj : objArr) {
            if (sb.length() > 1) {
                sb.append(',');
            }
            sb.append(obj);
        }
        sb.append(']');
        return sb.toString();
    }
}
