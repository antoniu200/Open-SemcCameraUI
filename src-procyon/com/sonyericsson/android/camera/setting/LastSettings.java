// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import android.graphics.Rect;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.content.Context;

public class LastSettings
{
    private static final String LAUNCH_AND_RECORDING = "LAUNCH_AND_RECORDING";
    private final SharedPreferencesAccessor mAccessor;
    
    LastSettings(final Context context) {
        this.mAccessor = new SharedPreferencesAccessor(context, "com.sonyericsson.android.camera.shared_preferences");
    }
    
    private String getLastPreviewSizeKey(final CapturingMode capturingMode) {
        final StringBuilder sb = new StringBuilder();
        sb.append("last-preview-size-");
        sb.append(capturingMode.name());
        return sb.toString();
    }
    
    private Rect getRect(final String s) {
        if (s != null) {
            final String[] split = s.split("x");
            if (split.length == 2) {
                final Rect rect = new Rect();
                rect.right = Integer.valueOf(split[0]);
                rect.bottom = Integer.valueOf(split[1]);
                return rect;
            }
        }
        return null;
    }
    
    public void clear() {
        synchronized (this.mAccessor) {
            this.mAccessor.remove("key-last-fast-capture-setting", false);
            this.mAccessor.remove("KEY_LAST_MODE", false);
            for (final CapturingMode capturingMode : CapturingMode.values()) {
                if (capturingMode != CapturingMode.UNKNOWN) {
                    this.mAccessor.remove(this.getLastPreviewSizeKey(capturingMode), false);
                }
            }
            this.mAccessor.apply();
        }
    }
    
    public CapturingMode getCapturingMode() {
        return CapturingMode.convertFrom(this.mAccessor.readString("KEY_LAST_MODE", CapturingMode.UNKNOWN.name()), CapturingMode.UNKNOWN);
    }
    
    public FastCapture getFastCapture() {
        final String string = this.mAccessor.readString("key-last-fast-capture-setting", FastCapture.getDefault().name());
        if ("LAUNCH_AND_RECORDING".equals(string)) {
            return FastCapture.getDefault();
        }
        try {
            return FastCapture.valueOf(string);
        }
        catch (final IllegalArgumentException ex) {
            return FastCapture.getDefault();
        }
    }
    
    public Rect getPreviewSize(final CapturingMode capturingMode) {
        return this.getRect(this.mAccessor.readString(this.getLastPreviewSizeKey(capturingMode), null));
    }
    
    public void save() {
        this.mAccessor.apply();
    }
    
    public void setCapturingMode(final CapturingMode capturingMode) {
        this.mAccessor.writeString("KEY_LAST_MODE", capturingMode.name(), false);
    }
    
    public void setFastCapture(final FastCapture fastCapture) {
        this.mAccessor.writeString("key-last-fast-capture-setting", fastCapture.name(), false);
    }
    
    public void setPreviewSize(final Rect rect, final CapturingMode capturingMode) {
        final SharedPreferencesAccessor mAccessor = this.mAccessor;
        final String lastPreviewSizeKey = this.getLastPreviewSizeKey(capturingMode);
        final StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(rect.width()));
        sb.append("x");
        sb.append(rect.height());
        mAccessor.writeString(lastPreviewSizeKey, sb.toString(), false);
    }
    
    public void writePauseTime() {
        this.mAccessor.writeLong("KEY_TIME_APP_PAUSED", System.currentTimeMillis(), false);
    }
}
