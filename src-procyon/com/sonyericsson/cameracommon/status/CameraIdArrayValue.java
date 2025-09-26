// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;
import com.sonyericsson.android.camera.device.CameraInfo;

public abstract class CameraIdArrayValue implements CameraStatusValue
{
    private static final String INLALID_VALUE = "N/A";
    private static final char SEPARATOR = ',';
    protected final CameraInfo.CameraId[] mValues;
    
    public CameraIdArrayValue(final CameraInfo.CameraId... mValues) {
        this.mValues = mValues;
    }
    
    private String getValue() {
        if (this.mValues.length > 0) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.mValues.length; ++i) {
                if (i != 0) {
                    sb.append(',');
                }
                sb.append(this.mValues[i].getCameraDeviceIdApi1());
            }
            return sb.toString();
        }
        return "N/A";
    }
    
    @Override
    public String getValueForDebug() {
        return this.getValue();
    }
    
    @Override
    public void putInto(final ContentValues contentValues, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(this.getKey());
        contentValues.put(sb.toString(), this.getValue());
    }
}
