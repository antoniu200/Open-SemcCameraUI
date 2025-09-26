// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;

public abstract class IntegerValue implements CameraStatusValue
{
    protected final int mValue;
    
    public IntegerValue(final int mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public String getValueForDebug() {
        return String.valueOf(this.mValue);
    }
    
    @Override
    public void putInto(final ContentValues contentValues, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(this.getKey());
        contentValues.put(sb.toString(), Integer.valueOf(this.mValue));
    }
}
