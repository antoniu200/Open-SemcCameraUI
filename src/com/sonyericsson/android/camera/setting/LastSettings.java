package com.sonyericsson.android.camera.setting;

import android.content.Context;
import android.graphics.Rect;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LastSettings {
    private static final String LAUNCH_AND_RECORDING = "LAUNCH_AND_RECORDING";
    private final SharedPreferencesAccessor mAccessor;

    LastSettings(Context context) {
        this.mAccessor = new SharedPreferencesAccessor(context, SharedPreferencesConstants.CAMERA_SHARED_PREFERENCES_NAME);
    }

    public CapturingMode getCapturingMode() {
        return CapturingMode.convertFrom(this.mAccessor.readString(SharedPreferencesConstants.KEY_LAST_MODE, CapturingMode.UNKNOWN.name()), CapturingMode.UNKNOWN);
    }

    public void setCapturingMode(CapturingMode capturingMode) {
        this.mAccessor.writeString(SharedPreferencesConstants.KEY_LAST_MODE, capturingMode.name(), false);
    }

    public void writePauseTime() {
        this.mAccessor.writeLong(SharedPreferencesConstants.KEY_TIME_APP_PAUSED, System.currentTimeMillis(), false);
    }

    public FastCapture getFastCapture() {
        String string = this.mAccessor.readString(SharedPreferencesConstants.SP_KEY_LAST_FAST_CAPTURE_SETTING, FastCapture.getDefault().name());
        if (LAUNCH_AND_RECORDING.equals(string)) {
            return FastCapture.getDefault();
        }
        try {
            return FastCapture.valueOf(string);
        } catch (IllegalArgumentException unused) {
            return FastCapture.getDefault();
        }
    }

    public void setFastCapture(FastCapture fastCapture) {
        this.mAccessor.writeString(SharedPreferencesConstants.SP_KEY_LAST_FAST_CAPTURE_SETTING, fastCapture.name(), false);
    }

    public Rect getPreviewSize(CapturingMode capturingMode) {
        return getRect(this.mAccessor.readString(getLastPreviewSizeKey(capturingMode), null));
    }

    private Rect getRect(String str) {
        if (str != null) {
            String[] strArrSplit = str.split(SharedPrefsTranslator.CONNECTOR_CROSS);
            if (strArrSplit.length == 2) {
                Rect rect = new Rect();
                rect.right = Integer.valueOf(strArrSplit[0]).intValue();
                rect.bottom = Integer.valueOf(strArrSplit[1]).intValue();
                return rect;
            }
        }
        return null;
    }

    public void setPreviewSize(Rect rect, CapturingMode capturingMode) {
        this.mAccessor.writeString(getLastPreviewSizeKey(capturingMode), String.valueOf(rect.width()) + SharedPrefsTranslator.CONNECTOR_CROSS + rect.height(), false);
    }

    private String getLastPreviewSizeKey(CapturingMode capturingMode) {
        return "last-preview-size-" + capturingMode.name();
    }

    public void save() {
        this.mAccessor.apply();
    }

    public void clear() {
        synchronized (this.mAccessor) {
            this.mAccessor.remove(SharedPreferencesConstants.SP_KEY_LAST_FAST_CAPTURE_SETTING, false);
            this.mAccessor.remove(SharedPreferencesConstants.KEY_LAST_MODE, false);
            for (CapturingMode capturingMode : CapturingMode.values()) {
                if (capturingMode != CapturingMode.UNKNOWN) {
                    this.mAccessor.remove(getLastPreviewSizeKey(capturingMode), false);
                }
            }
            this.mAccessor.apply();
        }
    }
}
