// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import android.content.ContentValues;
import android.graphics.Rect;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.CameraStatusValue;

public abstract class ResolutionValue implements CameraStatusValue, EachCameraStatusValue
{
    private static int REQUIRED_PROVIDER_VERSION = 1;
    private int mHeight;
    private int mWidth;
    
    public ResolutionValue(final int mWidth, final int mHeight) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;
    }
    
    public ResolutionValue(final Rect rect) {
        this.mWidth = rect.width();
        this.mHeight = rect.height();
    }
    
    @Override
    public String getValueForDebug() {
        return this.toString();
    }
    
    @Override
    public int minRequiredVersion() {
        return ResolutionValue.REQUIRED_PROVIDER_VERSION;
    }
    
    @Override
    public void putInto(final ContentValues contentValues, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(this.getKey());
        contentValues.put(sb.toString(), this.toString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.mWidth);
        sb.append("x");
        sb.append(this.mHeight);
        return sb.toString();
    }
}
