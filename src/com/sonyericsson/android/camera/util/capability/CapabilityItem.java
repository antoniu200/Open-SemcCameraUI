package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class CapabilityItem<T> {
    public static final String TAG = "CapabilityItem";
    private final String mName;
    private final T mValue;

    abstract T getDefaultValue();

    T read(SharedPreferences sharedPreferences, String str) {
        return null;
    }

    void write(SharedPreferences.Editor editor) {
    }

    CapabilityItem(String str, T t) {
        this.mName = str;
        this.mValue = t;
        if (CamLog.VERBOSE) {
            CamLog.d(str, " value: " + t);
        }
    }

    CapabilityItem(String str, SharedPreferences sharedPreferences) {
        this.mName = str;
        this.mValue = read(sharedPreferences, str);
    }

    public final String getName() {
        return this.mName;
    }

    public final T get() {
        if (this.mValue == null) {
            return getDefaultValue();
        }
        return this.mValue;
    }
}
